package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Feature2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerSearchUserAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ExLinearLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vmage on 2017/11/21.
 */

public class FragmentCategoryUser extends Fragment implements View.OnClickListener {

    private FragmentCategoryUser fragment = this;

    private RecyclerView rvUser;
    private TextView tvClose, tvTitle;

    private List<ItemUser> userList;

    private ArrayList<HashMap<String, Object>> mapUserList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getArguments();

        if (bundle != null) {
            userList = (List<ItemUser>) bundle.getSerializable("userList");


            mapUserList = new ArrayList<>();

            for (int i = 0; i < userList.size(); i++) {

                HashMap<String, Object> map = new HashMap<>();

                map.put(Key.picture, userList.get(i).getPicture());
                map.put(Key.name, userList.get(i).getName());
                map.put(Key.user_id, userList.get(i).getUser_id() + "");

                mapUserList.add(map);

            }


        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_categoryuser, container, false);

        rvUser = (RecyclerView) v.findViewById(R.id.rvUser);
        tvClose = (TextView) v.findViewById(R.id.tvClose);
        tvTitle = (TextView) v.findViewById(R.id.tvTitle);

        TextUtility.setBold(tvTitle, true);


        tvClose.setOnClickListener(this);


        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setUserRecycler();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Animation animation = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.right_enter);

                rvUser.startAnimation(animation);

                rvUser.setVisibility(View.VISIBLE);

            }
        }, 100);

    }

    private void setUserRecycler() {

        ExLinearLayoutManager layoutManager = new ExLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvUser.setLayoutManager(layoutManager);
        RecyclerSearchUserAdapter userAdapter = new RecyclerSearchUserAdapter(getActivity(), mapUserList);
        rvUser.setAdapter(userAdapter);

        userAdapter.setOnRecyclerViewListener(new RecyclerSearchUserAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (mapUserList == null || mapUserList.size() < 1) {
                    return;
                }

                ActivityIntent.toUser(
                        getActivity(),
                        true,
                        false,
                        (String) mapUserList.get(position).get(Key.user_id),
                        (String) mapUserList.get(position).get(Key.picture),
                        (String) mapUserList.get(position).get(Key.name),
                        v.findViewById(R.id.userImg)
                );


//                Bundle bundle = new Bundle();
//                bundle.putString(Key.author_id, (String) mapUserList.get(position).get(Key.user_id));
//                bundle.putString(Key.picture, (String) mapUserList.get(position).get(Key.picture));
//                bundle.putString(Key.name, (String) mapUserList.get(position).get(Key.name));
//
//
//                if (SystemUtility.Above_Equal_V5()) {
//
//                    Intent intent = new Intent(getActivity(), Author2Activity.class).putExtras(bundle);
//                    RoundCornerImageView userImg = (RoundCornerImageView) v.findViewById(R.id.userImg);
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation(getActivity(),
//                                    userImg,
//                                    ViewCompat.getTransitionName(userImg));
//                    startActivity(intent, options.toBundle());
//
//
//                } else {
//
//                    Intent intent = new Intent(getActivity(), Author2Activity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    ActivityAnim.StartAnim(getActivity());
//
//                }


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tvClose:

                Animation animation = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.right_exist);

                rvUser.startAnimation(animation);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.right_enter);
                        ((Feature2Activity) getActivity()).getLinUser().startAnimation(animation);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.top_enter, R.anim.top_exit)
                                .remove(fragment)
                                .commitAllowingStateLoss();
                        fragment = null;

                        ((Feature2Activity) getActivity()).removeFragment();

                    }
                }, 100);

                break;

        }

    }


}

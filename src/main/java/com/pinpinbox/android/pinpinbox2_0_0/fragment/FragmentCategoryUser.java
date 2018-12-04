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
import com.pinpinbox.android.pinpinbox2_0_0.activity.CategoryBookCase2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerCategoryUserLargeAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ExLinearLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;

import java.util.List;

/**
 * Created by vmage on 2017/11/21.
 */

public class FragmentCategoryUser extends Fragment implements View.OnClickListener {

    private FragmentCategoryUser fragment = this;

    private RecyclerView rvUser;
    private TextView tvClose, tvTitle;



    private List<ItemUser>itemUserList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getArguments();

        if (bundle != null) {
            itemUserList = (List<ItemUser>) bundle.getSerializable("userList");
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_categoryuser, container, false);

        rvUser =  v.findViewById(R.id.rvUser);
        tvClose = v.findViewById(R.id.tvClose);
        tvTitle = v.findViewById(R.id.tvTitle);

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
        RecyclerCategoryUserLargeAdapter userAdapter = new RecyclerCategoryUserLargeAdapter(getActivity(), itemUserList);
        rvUser.setAdapter(userAdapter);

        userAdapter.setOnRecyclerViewListener(new RecyclerCategoryUserLargeAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (itemUserList == null || itemUserList.size() < 1) {
                    return;
                }

                ActivityIntent.toUser(
                        getActivity(),
                        true,
                        false,
                        itemUserList.get(position).getUser_id(),
                        itemUserList.get(position).getPicture(),
                        itemUserList.get(position).getName(),
                        v.findViewById(R.id.userImg)
                );


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
                        ((CategoryBookCase2Activity) getActivity()).getLinUser().startAnimation(animation);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.top_enter, R.anim.top_exit)
                                .remove(fragment)
                                .commitAllowingStateLoss();
                        fragment = null;

                        ((CategoryBookCase2Activity) getActivity()).removeFragment();

                    }
                }, 100);

                break;

        }

    }


}

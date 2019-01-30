package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.activity.ExchangeInfoActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.ExchangeListActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerExchangeListAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.RequestCodeClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2018/2/1.
 */

public class FragmentExchangeUnfinished extends Fragment {

    private RecyclerExchangeListAdapter adapter;

    private List<ItemExchange> itemExchangeList;

    private RecyclerView rvExchangelist;

    private int clickPosition = -1;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_exchangelist, container, false);

        rvExchangelist = v.findViewById(R.id.rvExchangelist);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBundle();

        init();

        setRecycler();

        adapter.notifyDataSetChanged();


    }

    private void getBundle(){

        Bundle bundle = getArguments();

        if(bundle!=null){

            List<ItemExchange> allExchangeList = (List<ItemExchange>) bundle.getSerializable("exchangeList");
            itemExchangeList = new ArrayList<>();

            for (int i = 0; i < allExchangeList.size(); i++) {
                boolean hasGained = allExchangeList.get(i).isHas_gained();
                if(!hasGained){
                    itemExchangeList.add(allExchangeList.get(i));
                }

            }

        }

    }

    private void init(){



    }


    private void setRecycler() {

        adapter = new RecyclerExchangeListAdapter(getActivity(), itemExchangeList, true);
        rvExchangelist.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvExchangelist.setLayoutManager(manager);
        rvExchangelist.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnRecyclerViewListener(new RecyclerExchangeListAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                clickPosition = position;

                RoundCornerImageView detaillistImg = v.findViewById(R.id.detaillistImg);

                Bundle bundle = new Bundle();
                bundle.putSerializable("exchangeItem", itemExchangeList.get(position));
                bundle.putBoolean("isExchanged", false);

                if(itemExchangeList.get(position).getPhotousefor_user_id()>0){
                    bundle.putBoolean("isSlotType", true);
                }

                Intent intent = new Intent(getActivity(), ExchangeInfoActivity.class).putExtras(bundle);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(),
                                detaillistImg,
                                ViewCompat.getTransitionName(detaillistImg));


                //需要添加getActivity()，否則會亂碼
                getActivity().startActivityForResult(intent, RequestCodeClass.CloseToScrollDonePage, options.toBundle());


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    public void moveItem(){

        FragmentExchangeDone fragmentExchangeDone = (FragmentExchangeDone)((ExchangeListActivity)getActivity()).getFragment(FragmentExchangeDone.class.getSimpleName());

        //成功兌換 將狀態改為已領取移至"已完成"
        itemExchangeList.get(clickPosition).setHas_gained(true);
        fragmentExchangeDone.addItem(itemExchangeList.get(clickPosition));

        itemExchangeList.remove(clickPosition);
        adapter.notifyDataSetChanged();

    }

    private void cleanCache(){

        if(itemExchangeList!=null && itemExchangeList.size()>0){
            for (int i = 0; i < itemExchangeList.size(); i++) {
                Picasso.get().invalidate(itemExchangeList.get(i).getImage());
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {


        cleanCache();

        System.gc();

        super.onDestroy();
    }


}

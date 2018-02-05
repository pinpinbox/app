package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.SystemType;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.activity.ExchangeInfo2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerExchangeListAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2018/2/1.
 */

public class FragmentExchangeUnfinished2 extends Fragment {

    private RecyclerExchangeListAdapter adapter;

    private List<ItemExchange> exchangeList;

    private RecyclerView rvExchangelist;

    private int deviceType;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_exchangelist, container, false);

        rvExchangelist = (RecyclerView)v.findViewById(R.id.rvExchangelist);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        setRecycler();

        ItemExchange itemExchange = new ItemExchange();
        itemExchange.setName("LASKO保溫瓶");
        itemExchange.setDescription("LASKO你耀紅保溫瓶");
        itemExchange.setImage("https://ppb.sharemomo.com/upload/pinpinbox/diy/20180201/5a720323180ba_358x501.jpg");
        itemExchange.setTime("剩餘時間:50天20小時16分");
        itemExchange.setImageWidth(PPBApplication.getInstance().getStaggeredWidth());
        itemExchange.setImageHeight(PPBApplication.getInstance().getStaggeredWidth());

        exchangeList.add(itemExchange);

        adapter.notifyDataSetChanged();


    }

    private void init(){

        exchangeList = new ArrayList<>();

    }


    private void setRecycler() {

        adapter = new RecyclerExchangeListAdapter(getActivity(), exchangeList, true);
        rvExchangelist.setAdapter(adapter);

        ExStaggeredGridLayoutManager manager = null;

        if (SystemUtility.isTablet(getActivity().getApplicationContext())) {
            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            deviceType = SystemType.TABLE;

        } else {
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            deviceType = SystemType.PHONE;
        }

        rvExchangelist.setLayoutManager(manager);
        rvExchangelist.addItemDecoration(new SpacesItemDecoration(16, deviceType, false));
        rvExchangelist.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnRecyclerViewListener(new RecyclerExchangeListAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                RoundCornerImageView exchangeImg = (RoundCornerImageView)v.findViewById(R.id.exchangeImg);

                Bundle bundle = new Bundle();
                bundle.putSerializable("exchangeItem", exchangeList.get(position));

                Intent intent = new Intent(getActivity(), ExchangeInfo2Activity.class).putExtras(bundle);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(),
                                exchangeImg,
                                ViewCompat.getTransitionName(exchangeImg));
                startActivity(intent, options.toBundle());



            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


}

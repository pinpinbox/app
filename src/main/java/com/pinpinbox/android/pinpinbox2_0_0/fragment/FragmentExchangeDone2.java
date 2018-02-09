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
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.activity.ExchangeInfo2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerExchangeListAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SystemType;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2018/2/1.
 */

public class FragmentExchangeDone2 extends Fragment {


    private RecyclerExchangeListAdapter adapter;

    private List<ItemExchange> itemExchangeList;

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
                if(hasGained){
                    itemExchangeList.add(allExchangeList.get(i));
                }

            }

        }

    }

    private void init(){



    }


    private void setRecycler() {

        adapter = new RecyclerExchangeListAdapter(getActivity(), itemExchangeList, false);
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
                bundle.putSerializable("exchangeItem", itemExchangeList.get(position));
                bundle.putBoolean("isExchanged", true);
                bundle.putString(Key.photo_id, itemExchangeList.get(position).getPhoto_id() + "");

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


    public void addItem(ItemExchange itemExchange){
        itemExchangeList.add(0, itemExchange);
        adapter.notifyDataSetChanged();
    }

    private void cleanCache(){

        if(itemExchangeList!=null && itemExchangeList.size()>0){
            for (int i = 0; i < itemExchangeList.size(); i++) {
                com.squareup.picasso.Picasso.with(getActivity().getApplicationContext()).invalidate(itemExchangeList.get(i).getImage());
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

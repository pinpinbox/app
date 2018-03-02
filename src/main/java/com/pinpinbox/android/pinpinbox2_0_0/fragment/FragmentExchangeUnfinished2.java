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
import com.pinpinbox.android.pinpinbox2_0_0.activity.ExchangeList2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerExchangeListAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.RequestCodeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SystemType;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SpacesItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2018/2/1.
 */

public class FragmentExchangeUnfinished2 extends Fragment {

    private RecyclerExchangeListAdapter adapter;

    private List<ItemExchange> itemExchangeList;

    private RecyclerView rvExchangelist;

    private int deviceType;
    private int clickPosition = -1;


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

                clickPosition = position;

                RoundCornerImageView exchangeImg = (RoundCornerImageView)v.findViewById(R.id.exchangeImg);

                Bundle bundle = new Bundle();
                bundle.putSerializable("exchangeItem", itemExchangeList.get(position));
                bundle.putBoolean("isExchanged", false);

                if(itemExchangeList.get(position).getPhotousefor_user_id()>0){
                    bundle.putBoolean("isSlotType", true);
                }

                Intent intent = new Intent(getActivity(), ExchangeInfo2Activity.class).putExtras(bundle);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(),
                                exchangeImg,
                                ViewCompat.getTransitionName(exchangeImg));


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

        FragmentExchangeDone2 fragmentExchangeDone2 = (FragmentExchangeDone2)((ExchangeList2Activity)getActivity()).getFragment(FragmentExchangeDone2.class.getSimpleName());

        //成功兌換 將狀態改為已領取移至"已完成"
        itemExchangeList.get(clickPosition).setHas_gained(true);
        fragmentExchangeDone2.addItem(itemExchangeList.get(clickPosition));

        itemExchangeList.remove(clickPosition);
        adapter.notifyDataSetChanged();

    }

    private void cleanCache(){

        if(itemExchangeList!=null && itemExchangeList.size()>0){
            for (int i = 0; i < itemExchangeList.size(); i++) {
                Picasso.with(getActivity().getApplicationContext()).invalidate(itemExchangeList.get(i).getImage());
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
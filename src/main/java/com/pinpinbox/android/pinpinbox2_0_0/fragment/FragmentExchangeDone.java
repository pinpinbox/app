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
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerExchangeListAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2018/2/1.
 */

public class FragmentExchangeDone extends Fragment {


    private RecyclerExchangeListAdapter adapter;

    private List<ItemExchange> itemExchangeList;

    private RecyclerView rvExchangelist;


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

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvExchangelist.setLayoutManager(manager);
        rvExchangelist.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnRecyclerViewListener(new RecyclerExchangeListAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                RoundCornerImageView detaillistImg = v.findViewById(R.id.detaillistImg);

                Bundle bundle = new Bundle();
                bundle.putSerializable("exchangeItem", itemExchangeList.get(position));
                bundle.putBoolean("isExchanged", true);

                if(itemExchangeList.get(position).getPhotousefor_user_id()>0){
                    bundle.putBoolean("isSlotType", true);
                }


                Intent intent = new Intent(getActivity(), ExchangeInfoActivity.class).putExtras(bundle);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(),
                                detaillistImg,
                                ViewCompat.getTransitionName(detaillistImg));
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

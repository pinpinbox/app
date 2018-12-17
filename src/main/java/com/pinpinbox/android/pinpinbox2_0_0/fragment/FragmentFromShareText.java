package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

public class FragmentFromShareText extends Fragment implements View.OnClickListener {


    private FragmentPagerItemAdapter adapter;

    private ImageView backImg;
    private TextView tvTabMy, tvTabShare, tvNewCreate, tvSend;
    private ViewPager viewPager;
    private LinearLayout linBottom;

    private String album_id = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_fromsharetext, container, false);

        backImg = v.findViewById(R.id.backImg);
        tvTabMy = v.findViewById(R.id.tvTabMy);
        tvTabShare = v.findViewById(R.id.tvTabShare);
        tvNewCreate = v.findViewById(R.id.tvNewCreate);
        tvSend = v.findViewById(R.id.tvSend);
        viewPager = v.findViewById(R.id.viewPager);
        linBottom = v.findViewById(R.id.linBottom);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        setFragment();

        setPageListener();

    }

    private void init() {

        //default
        pageMyType();

        backImg.setOnClickListener(this);
        tvTabMy.setOnClickListener(this);
        tvTabShare.setOnClickListener(this);
        tvNewCreate.setOnClickListener(this);
        tvSend.setOnClickListener(this);

    }

    private void pageMyType() {

        tvTabMy.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        TextUtility.setBold(tvTabMy, true);

        tvTabShare.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        TextUtility.setBold(tvTabShare, false);

    }

    private void pageShareType() {


        tvTabMy.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        TextUtility.setBold(tvTabMy, false);

        tvTabShare.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        TextUtility.setBold(tvTabShare, true);


    }

    private void setFragment() {

        Bundle bundleMine = new Bundle();
        bundleMine.putString("rank", FragmentSelectAlbum.MINE);

        Bundle bundleShare = new Bundle();
        bundleShare.putString("rank", FragmentSelectAlbum.COOPERATION);


        adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add("", FragmentSelectAlbum.class, bundleMine)
                .add("", FragmentSelectAlbum.class, bundleShare)
                .create());

        viewPager.setAdapter(adapter);

    }

    private void setPageListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {

                    case 0:
                        pageMyType();
                        break;

                    case 1:
                        pageShareType();
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public void clearOtherPageItem(int otherPage) {

        FragmentSelectAlbum fragmentSelectAlbum = (FragmentSelectAlbum) adapter.getPage(otherPage);
        fragmentSelectAlbum.resetItem();

    }

    public void showBottom() {

        if(linBottom.getVisibility()==View.GONE) {
            linBottom.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.backImg:

                getActivity().finish();

                break;

            case R.id.tvTabMy:

                viewPager.setCurrentItem(0);

                break;

            case R.id.tvTabShare:

                viewPager.setCurrentItem(1);

                break;

            case R.id.tvNewCreate:


                break;

            case R.id.tvSend:

                MyLog.Set("e", getClass(), "選擇的 album_id => " + album_id);

                break;


        }


    }
}
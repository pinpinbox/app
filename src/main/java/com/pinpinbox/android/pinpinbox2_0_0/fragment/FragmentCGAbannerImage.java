package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebViewActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.squareup.picasso.Picasso;

/**
 * Created by vmage on 2018/4/18.
 */

public class FragmentCGAbannerImage extends Fragment implements View.OnClickListener {

    private ImageView imageImg;

    private String imageUrl;
    private String imageLink;

    public static FragmentCGAbannerImage newInstance(String imageUrl, String imageLink) {

        FragmentCGAbannerImage fragmentCGAbannerImage = new FragmentCGAbannerImage();

        Bundle bundle = new Bundle();
        bundle.putString(Key.image, imageUrl);
        bundle.putString(Key.imageLink, imageLink);

        fragmentCGAbannerImage.setArguments(bundle);

        return fragmentCGAbannerImage;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle!=null){
            imageUrl = bundle.getString(Key.image,"");
            imageLink = bundle.getString(Key.imageLink, "");

            MyLog.Set("e", getClass(), "imageUrl => " + imageUrl);
            MyLog.Set("e", getClass(), "imageLink => " + imageLink);

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_cga_banner_image, container, false);


        imageImg = v.findViewById(R.id.imageImg);


        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setImage();


    }

    private void setImage() {

        if(imageUrl!=null && !imageUrl.equals("null") && !imageUrl.equals("")){

            Picasso.get()
                    .load(imageUrl)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(getActivity().getApplicationContext())
                    .into(imageImg);


        }else {

            imageImg.setImageResource(R.drawable.bg_2_0_0_no_image);

        }


        imageImg.setOnClickListener(this);

    }





    @Override
    public void onClick(View v) {

        if(ClickUtils.ButtonContinuousClick()){
            return;
        }


        switch (v.getId()){

            case R.id.imageImg:

                if(imageLink==null || imageLink.equals("") || imageLink.equals("null")){
                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_null_intent);
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString(Key.url, imageLink);

                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                ActivityAnim.StartAnim(getActivity());

                break;


        }


    }


}

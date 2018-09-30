package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;

import java.util.HashMap;
import java.util.List;

public class RecyclerAudioFileAdapter extends RecyclerView.Adapter {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);
        boolean onItemLongClick(int position, View v);
    }

    public interface OnUploadListener{
        void onClick(int position);
    }


    private OnRecyclerViewListener onRecyclerViewListener;
    private OnUploadListener onUploadListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public void setOnUploadListener(OnUploadListener onUploadListener) {
        this.onUploadListener = onUploadListener;
    }




    private Activity mActivity;

    private  List<HashMap<String, Object>> filePathList;

    private int buttonType = -1;
    public static final int UPLOAD = 0;
    public static final int SELECT = 1;

    public RecyclerAudioFileAdapter(Activity activity,  List<HashMap<String, Object>> filePathList, int buttonType) {
        this.mActivity = activity;
        this.filePathList = filePathList;
        this.buttonType = buttonType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_select_audio_file, null);
        return new RecyclerAudioFileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.position = position;

        mHolder.tvFileName.setText((String)filePathList.get(position).get("name"));

        mHolder.tvTime.setText((String)filePathList.get(position).get("time"));

        if(buttonType == UPLOAD){
            mHolder.tvUpload.setText(R.string.pinpinbox_2_0_0_button_upload);
        }else if(buttonType == SELECT){
            mHolder.tvUpload.setText(R.string.pinpinbox_2_0_0_button_select);
        }

        mHolder.tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                if (onUploadListener != null) {
                    onUploadListener.onClick(position);
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return filePathList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private int position;
        private RelativeLayout rBackground;
        private TextView tvFileName, tvUpload, tvTime;



        public ViewHolder(View itemView) {
            super(itemView);

            rBackground = (RelativeLayout) itemView.findViewById(R.id.rBackground);

            tvFileName = (TextView) itemView.findViewById(R.id.tvFileName);
            tvUpload = (TextView) itemView.findViewById(R.id.tvUpload);
            tvTime = (TextView)itemView.findViewById(R.id.tvTime);
            rBackground.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {


            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position, v);
            }

        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(position, v);
            }
            return false;
        }

    }



    public void setButtonUpload(){

        buttonType = UPLOAD;

        this.notifyDataSetChanged();

    }


    public void setButtonSelect(){

        buttonType = SELECT;

        this.notifyDataSetChanged();


    }


}

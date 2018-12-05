package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.PinchImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemPhoto;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin9594 on 2017/2/26.
 */

public class PhotoPageAdapter extends PagerAdapter {

    private Activity mActivity;

    private List<ItemPhoto> itemPhotoList;

    private ItemAlbum itemAlbum;

//    private Picasso.Builder picasso;

//    public Picasso.Builder getPicasso(){
//        return this.picasso;
//    }


    public PhotoPageAdapter(Activity mActivity, List<ItemPhoto> itemPhotoList, ItemAlbum itemAlbum) {

        this.mActivity = mActivity;
        this.itemPhotoList = itemPhotoList;
        this.itemAlbum = itemAlbum;


//        OkHttpClientManager.getInstance().getOKHttp().setConnectTimeout(8, TimeUnit.SECONDS);
//        OkHttpClientManager.getInstance().getOKHttp().setReadTimeout(8, TimeUnit.SECONDS);
//        OkHttpClientManager.getInstance().getOKHttp().setWriteTimeout(8, TimeUnit.SECONDS);


//        picasso = new Picasso.Builder(mActivity.getApplicationContext()).downloader(
//                new OkHttpDownloader(OkHttpClientManager.getInstance().getOKHttp()));

    }


    public void destroyItem(final ViewGroup view, final int position, final Object object) {


        MyLog.Set("d", PhotoPageAdapter.class, "destroyItem position => " + position);


        PinchImageView picImg = (PinchImageView) ((View) object).findViewById(R.id.photoImg);
        picImg.reset();
        picImg.setImageBitmap(null);
        view.removeView((View) object);

        if (position != itemPhotoList.size()) {

            String url = itemPhotoList.get(position).getImage_url();

            if (url != null && !url.equals("") && !url.equals("null")) {
                Picasso.with(mActivity).invalidate(url);
            }
        }

    }

    private void checkType(View vPage, int position){

        View vType = vPage.findViewById(R.id.vType);

        String usefor = itemPhotoList.get(position).getUsefor();

        if(usefor.equals("image") || usefor.equals("lastPage")){
            vType.setVisibility(View.GONE);
        }else {
            vType.setVisibility(View.VISIBLE);
        }

    }

    private void setImageContents(View vPage, int position) {

        final PinchImageView picImg = (PinchImageView) vPage.findViewById(R.id.photoImg);

        final ImageView refreshImg = (ImageView) vPage.findViewById(R.id.refreshImg);

        if (picImg != null) {

            String url = itemPhotoList.get(position).getImage_url();

            if (itemAlbum.isOwn()) {
                //已收藏或贊助
                setImage(url, picImg, refreshImg);
            } else {
                //尚未收藏贊助
                if (position == itemPhotoList.size() - 1) {
                    picImg.setBackgroundResource(R.drawable.bg200_preview_normal);
                } else {
                    setImage(url, picImg, refreshImg);
                }
            }

        }

    }

    private void setImage(final String url, final PinchImageView picImg, final ImageView refreshImg) {
        if (url == null || url.equals("") || url.equals("null")) {
            picImg.setImageResource(R.drawable.bg_2_0_0_no_image);
            refreshImg.setVisibility(View.VISIBLE);
            refreshImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickUtils.ButtonContinuousClick()) {
                        return;
                    }

                    if (url != null && !url.equals("") && !url.equals("null") && !url.isEmpty()) {

                        Picasso.with(mActivity.getApplicationContext())
                                .load(url)
                                .config(Bitmap.Config.RGB_565)
                                .error(R.drawable.bg_2_0_0_no_image)
                                .tag(mActivity.getApplicationContext())
                                .into(picImg, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        refreshImg.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {
                                        refreshImg.setVisibility(View.VISIBLE);
                                    }
                                });

                    } else {

                        PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_photo_does_not_exist);

                    }


                }
            });


        } else {

            Picasso.with(mActivity.getApplicationContext())
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(picImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            refreshImg.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            refreshImg.setVisibility(View.VISIBLE);
                        }
                    });


        }
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        View vPage = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.page_2_0_0_photo, null);

        vPage.setId(position);

        checkType(vPage, position);

        setImageContents(vPage, position);


        container.addView(vPage, 0);
        return vPage;

    }


    @Override
    public int getCount() {
        return itemPhotoList.size();
    }


    //判斷是否由對象生成介面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


//    private void setVideoContents(View vPage, int position){
//
//        ImageView centerImg = (ImageView)vPage.findViewById(R.id.centerImg);
//
//        String usefor = itemPhotoList.get(position).getUsefor();
//
//        if(usefor.equals("video")){
//            String videoRefer = itemPhotoList.get(position).getVideo_refer();
//            final String videoTarget = itemPhotoList.get(position).getVideo_target();
//
//
//            if (videoRefer.equals("file")) {
//
//                setVideo(vPage, videoTarget, position);
//
//            } else if (videoRefer.equals("embed")) {
//
//                String youtubeUrl = StringUtil.checkYoutubeId(videoTarget);
//                if (youtubeUrl == null || youtubeUrl.equals("null")) {
//
//                    if (!StringUtil.containsString(videoTarget, "vimeo") &&
//                            !StringUtil.containsString(videoTarget, "dailymotion") &&
//                            !StringUtil.containsString(videoTarget, "facebook")) {
//                        setVideo(vPage, videoTarget, position);
//                    } else {
//                        setVideoClick(centerImg, position);
//                    }
//
//                } else {
//                    setVideoClick(centerImg, position);
//                }
//
//            }
//
//
//        }
//
//    }
//
//
//    private void setVideo(View vPage, final String videoTarget, int position) {
//
//        RelativeLayout rVideo = (RelativeLayout) vPage.findViewById(R.id.rVideo);
//        rVideo.setVisibility(View.VISIBLE);
//
//        VideoView videoView = null;
//        if (itemPhotoList.get(position).getVideoView() == null) {
//            videoView = (VideoView) vPage.findViewById(R.id.videoview);
//            itemPhotoList.get(position).setVideoView(videoView);
//        } else {
//            videoView = itemPhotoList.get(position).getVideoView();
//        }
//
//
//
//        vPage.findViewById(R.id.vModeChange).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ReaderActivity)mActivity).setModeChange();
//            }
//        });
//
//        Uri uri = null;
//        uri = Uri.parse(videoTarget);
//        videoView.setVideoURI(uri);
//
//        final boolean[] isEnd = {false};
//
//        final VideoView finalVideoView = videoView;
//        videoView.setOnPreparedListener(new OnPreparedListener() {
//            @Override
//            public void onPrepared() {
////                finalVideoView.start();
//            }
//        });
//
//        videoView.setOnCompletionListener(new OnCompletionListener() {
//            @Override
//            public void onCompletion() {
//                isEnd[0] = true;
//            }
//        });
//
//        videoView.getVideoControls().setButtonListener(new VideoControlsButtonListener() {
//            @Override
//            public boolean onPlayPauseClicked() {
//
//                MyLog.Set("e", mActivity.getClass(), "onPlayPauseClicked");
//
//                if (isEnd[0]) {
//                    finalVideoView.restart();
//                    isEnd[0] = false;
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onPreviousClicked() {
//                MyLog.Set("e", this.getClass(), "onPreviousClicked");
//                return false;
//            }
//
//            @Override
//            public boolean onNextClicked() {
//                MyLog.Set("e", this.getClass(), "onNextClicked");
//                return false;
//            }
//
//            @Override
//            public boolean onRewindClicked() {
//                MyLog.Set("e", this.getClass(), "onRewindClicked");
//                return false;
//            }
//
//            @Override
//            public boolean onFastForwardClicked() {
//                MyLog.Set("e", this.getClass(), "onFastForwardClicked");
//                return false;
//            }
//        });
//
//
//
//    }
//
//    private void setVideoClick(ImageView centerImg, final int position) {
//        centerImg.setImageResource(R.drawable.click_2_0_0_video_white);
//        centerImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ClickUtils.ButtonContinuousClick()) {
//                    return;
//                }
//
//                if (!HttpUtility.isConnect(mActivity)) {
//                    ((ReaderActivity)mActivity).setNoConnect();
//                    return;
//                }
//
//                PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_play_audio_ready);
//
//                checkAudioType(position);
//            }
//        });
//
//    }
//
//    private void checkAudioType(int position) {
//
//        String videoRefer = itemPhotoList.get(position).getVideo_refer();
//        final String videoTarget = itemPhotoList.get(position).getVideo_target();
//
//        MyLog.Set("d", getClass(), "videoRefer => " + videoRefer);
//
//        switch (videoRefer) {
//
//            case "file":
//                toPlayVideo(videoTarget);
//                break;
//
//            case "embed":
//                String youtubeUrl = StringUtil.checkYoutubeId(videoTarget);
//                try {
//                    if (youtubeUrl == null || youtubeUrl.equals("null")) {
//                        if (StringUtil.containsString(videoTarget, "vimeo")) {
//                            MyLog.Set("d", mActivity.getClass(), "vimeo播放 => " + videoTarget);
//                            VimeoExtractor.getInstance().fetchVideoWithURL(videoTarget, null, new OnVimeoExtractionListener() {
//                                @Override
//                                public void onSuccess(VimeoVideo video) {
//                                    MyLog.Set("d", mActivity.getClass(), "video.getStreams().toString() =>" + video.getStreams().toString());
//                                    String stream = "";
//                                    stream = video.getStreams().get("1080p");
//                                    if (stream == null || stream.equals("null")) {
//
//                                        stream = video.getStreams().get("720p");
//
//                                        if (stream == null || stream.equals("null")) {
//
//                                            stream = video.getStreams().get("540p");
//
//                                            if (stream == null || stream.equals("null")) {
//                                                stream = video.getStreams().get("360p");
//                                            }
//
//                                        }
//
//
//                                    }
//
//                                    MyLog.Set("d", mActivity.getClass(), "stream => " + stream);
//
//                                    if (stream == null) {
//
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString(Key.url, videoTarget);
//                                        Intent intent = new Intent(mActivity, WebViewActivity.class);
//                                        intent.putExtras(bundle);
//                                        mActivity.startActivity(intent);
//                                        ActivityAnim.StartAnim(mActivity);
//
//                                    } else {
//                                        toPlayVideo(stream);
//                                    }
//
//
//                                }
//
//                                @Override
//                                public void onFailure(Throwable throwable) {
//                                }
//                            });
//
//
//                        } else if (StringUtil.containsString(videoTarget, "dailymotion")) {
//
////                            PinPinToast.ShowToast(mActivity, "dailymotion");
//
//                            toPlayVideo(videoTarget);
//
//                        } else {
//                            toPlayVideo(videoTarget);
//                        }
//
//
//                    } else {
//                        MyLog.Set("d", mActivity.getClass(), "youtube播放 => " + youtubeUrl);
//                        Intent intent = new Intent(mActivity, YouTubeActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("path", videoTarget);
//                        intent.putExtras(bundle);
//                        mActivity.startActivity(intent);
//                        ActivityAnim.StartAnim(mActivity);
//                    }
//                } catch (Exception e) {
//                    toPlayVideo(videoTarget);
//                    e.printStackTrace();
//                }
//
//                break;
//            case "none":
//                MyLog.Set("d", getClass(), "play mode => none");
//                break;
//            case "system":
//                MyLog.Set("d", getClass(), "play mode => system");
//                break;
//        }
//    }
//
//
//    private void toPlayVideo(String url) {
//        Intent intent = new Intent(mActivity, VideoPlayActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("videopath", url);
//        intent.putExtras(bundle);
//        mActivity.startActivity(intent);
//        ActivityAnim.StartAnim(mActivity);
//    }


}


//public class PhotoPageAdapter extends PagerAdapter {
//
//    private Activity mActivity;
//
//    private List<ItemPhoto> itemPhotoList;
//
//    private ItemAlbum itemAlbum;
//
//
//    public PhotoPageAdapter(Activity mActivity, List<ItemPhoto> itemPhotoList, ItemAlbum itemAlbum) {
//
//        this.mActivity = mActivity;
//        this.itemPhotoList = itemPhotoList;
//        this.itemAlbum = itemAlbum;
//
//
//    }
//
//
//    public void destroyItem(final ViewGroup view, final int position, final Object object) {
//
//        PinchImageView picImg = (PinchImageView) ((View) object).findViewById(R.id.photoImg);
//        picImg.reset();
//        picImg.setImageBitmap(null);
//        view.removeView((View) object);
//
//        if (position != itemPhotoList.size()) {
//
//            String url = itemPhotoList.get(position).getImage_url();
//
//            String end = url.substring(url.lastIndexOf(".") + 1, url.length()).toLowerCase();
//
//
//            if (url != null && !url.equals("") && !url.equals("null")) {
//
//
//                if (end.equals("gif")) {
//
////                    if (!((GifDrawable)picImg.getDrawable()).isRecycled()) {
////                        ((GifDrawable)picImg.getDrawable()).recycle();
////                    }
//
//                } else {
//
//                    Glide.get(mActivity.getApplicationContext()).clearMemory();
//
//                }
//
//            }
//        }
//
//    }
//
//
//    @Override
//    public Object instantiateItem(final ViewGroup container, final int position) {
//
//        View v = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.page_2_0_0_photo, null);
//
//        v.setId(position);
//
//        final PinchImageView picImg = (PinchImageView) v.findViewById(R.id.photoImg);
//
//        String url = itemPhotoList.get(position).getImage_url();
//
//
//        if (picImg != null) {
//
//
//            if (itemAlbum.isOwn()) {
//                //已收藏或贊助
//                setImg(url, position, picImg);
//
//            } else {
//                //尚未收藏贊助
//                if (position == itemPhotoList.size() - 1) {
//
//                    picImg.setBackgroundResource(R.drawable.bg200_preview_normal);
//
//                } else {
//                    setImg(url, position, picImg);
//                }
//            }
//        }
//
//
//        container.addView(v, 0);
//        return v;
//
//    }
//
//
//    private void setImg(String url, int position, PinchImageView picImg) {
//        if (url == null || url.equals("") || url.equals("null")) {
//            picImg.setImageResource(R.drawable.bg_2_0_0_no_image);
//        } else {
//
//            RequestOptions opts = new RequestOptions()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .error(R.drawable.bg_2_0_0_no_image);
//
//            String end = url.substring(url.lastIndexOf(".") + 1, url.length()).toLowerCase();
//
//            if (end.equals("gif")) {
//
//                final LoadGifTask task = new LoadGifTask(picImg, position);
//                task.execute();
//
////                picImg.setOnLongClickListener(new View.OnLongClickListener() {
////                    @Override
////                    public boolean onLongClick(View view) {
////
////                        if(task!=null){
////                            task.startGif();
////                        }
////
////                        return false;
////                    }
////                });
//
//            } else {
//
//                Glide.with(mActivity.getApplicationContext())
//                        .load(url)
//                        .apply(opts)
//                        .into(picImg);
//
//            }
//
//        }
//    }
//
//
//    @Override
//    public int getCount() {
//        return itemPhotoList.size();
//    }
//
//
//    //判斷是否由對象生成介面
//    @Override
//    public boolean isViewFromObject(View arg0, Object arg1) {
//        return (arg0 == arg1);
//    }
//
//    private class LoadGifTask extends AsyncTask<Void, Void, Object> {
//
//        private GifImageView gifImg;
//        private int position;
//
//        private ByteBuffer buffer;
//        private GifDrawable gifFromBytes;
//
//
//        public LoadGifTask(GifImageView gifImg, int position) {
//
//            this.gifImg = gifImg;
//            this.position = position;
//
//        }
//
//        public void startGif() {
//            if(gifFromBytes!=null) {
//                gifFromBytes.start();
//            }
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            try {
//
//                String url = itemPhotoList.get(position).getImage_url();
//
//                URLConnection urlConnection = new URL(url).openConnection();
//                urlConnection.connect();
//
//                int contentLength = urlConnection.getContentLength();
//                buffer = ByteBuffer.allocateDirect(contentLength);
//                ReadableByteChannel channel = Channels.newChannel(urlConnection.getInputStream());
//
//                while (buffer.remaining() > 0)
//                    channel.read(buffer);
//                channel.close();
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//
//            try {
//                gifFromBytes = new GifDrawable(buffer);
//
//                gifImg.setImageDrawable(gifFromBytes);
//
////                gifFromBytes.stop();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }
//
//}

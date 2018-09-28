package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Creation2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerAudioFileAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentSelectAudio2 extends Fragment implements View.OnClickListener {


    private LoaderManager loaderManager;

    private MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask timerTask;

    private RecyclerAudioFileAdapter adapter;

    private List<HashMap<String, Object>> filePathList;

    private RecyclerView rvAudioFile;
    private SeekBar seekBar;
    private ImageView controlImg;

    private String audio_mode = "";

    private static final String NONE = "none";
    private static final String SINGULAR = "singular"; //單首
    private static final String PLURAL = "plural"; //多首
    private static final String CUSTOM = "custom"; //上傳 自定義 不是由server給予


    /*
     * onCreate
     * onCreateView
     * onActivityCreated
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_2_0_0_select_audio, container, false);

        rvAudioFile = (RecyclerView) v.findViewById(R.id.rvAudioFile);
        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        controlImg = (ImageView) v.findViewById(R.id.controlImg);

        (v.findViewById(R.id.backImg)).setOnClickListener(this);
        controlImg.setOnClickListener(this);


        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBundle();

        init();

        setRecycler();

        loaderManager.initLoader(2, null, loaderCallbacks);


    }


    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        private CursorLoader cursorLoader;

        private String[] projection = {MediaStore.Audio.Media._ID,//0
                MediaStore.Audio.Media.DATA,//1
                MediaStore.Audio.Media.DATE_ADDED,//2
                MediaStore.Audio.Media.DURATION//3
        };


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            MyLog.Set("d", FragmentSelectAudio2.class, "loader.getId => " + id);


            cursorLoader = new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    MediaStore.Images.Media.DATE_ADDED);

            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
            MyLog.Set("d", FragmentSelectAudio2.class, "onLoadFinished");

            getAudioContent(cursor);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            MyLog.Set("d", FragmentSelectAudio2.class, "onLoaderReset");
        }


        private void getAudioContent(Cursor cursor) {

            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");

            cursor.moveToLast();

            do {

                String path = cursor.getString(cursor.getColumnIndex(projection[1]));
                File file = new File(path);
                if (file.exists()) {

                    long duration = cursor.getLong(cursor.getColumnIndex(projection[3]));
                    String time = formatter.format(duration);

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", file.getName());
                    map.put("time", time);
                    map.put("path", path);


                    MyLog.Set("e", FragmentSelectAudio2.class, "path => " + path);
                    MyLog.Set("e", FragmentSelectAudio2.class, "duration => " + duration);
                    MyLog.Set("e", FragmentSelectAudio2.class, "time => " + time);
                    MyLog.Set("d", FragmentSelectAudio2.class, "---------------------------------------------------------------------");


                    filePathList.add(map);

                }

            } while (cursor.moveToPrevious());

            adapter.notifyDataSetChanged();

        }

    };


    private void getBundle() {
        audio_mode = getArguments().getString(Key.audio_mode);
    }


    private void init() {


        loaderManager = getLoaderManager();

        filePathList = new ArrayList<>();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null) {
                    this.progress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);

                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        controlImg.setImageResource(R.drawable.ic200_recording_dark);
                    }

                }
            }
        });


    }

    private void setRecycler() {

        adapter = new RecyclerAudioFileAdapter(getActivity(), filePathList);
        rvAudioFile.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAudioFile.setLayoutManager(manager);

        adapter.setOnRecyclerViewListener(new RecyclerAudioFileAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {


                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource((String) filePathList.get(position).get("path"));
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer = mp;
                            audioPrepared();
                        }
                    });
                    mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            audioErrorState();
                            return false;
                        }
                    });

                } catch (Exception e) {
                    audioErrorState();
                    e.printStackTrace();

                }


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

        adapter.setOnUploadListener(new RecyclerAudioFileAdapter.OnUploadListener() {
            @Override
            public void onClick(int position) {

                Activity ac = SystemUtility.getActivity(Creation2Activity.class.getSimpleName());

                if (ac != null) {
                    switch (audio_mode) {

                        case SINGULAR:


                            break;

                        case PLURAL:

                            ((Creation2Activity) getActivity()).uploadMyAudioFile((String) filePathList.get(position).get("path"));

                            break;

                    }
                }





                hideFragment();


            }
        });

    }

    private void audioPrepared() {


        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);
        mediaPlayer.start();
        controlImg.setImageResource(R.drawable.ic200_recording_dark);

        if (timer == null) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    if (mediaPlayer != null && !seekBar.isPressed()) {
                        try {
                            int position = mediaPlayer.getCurrentPosition();
                            seekBar.setProgress(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 0, 500);
        }


    }

    private void audioErrorState() {
        cleanMedia();
        cleanTimer();
        PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_audio_error);
    }

    private void cleanTimer() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        timerTask = null;

        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = null;
    }

    public void cleanMedia() {

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        mediaPlayer = null;
        seekBar.setProgress(0);
        controlImg.setImageResource(R.drawable.ic200_video_dark);
        cleanTimer();

    }

    private void hideFragment() {

        cleanMedia();

        if (audio_mode.equals(SINGULAR)) {
            ((Creation2Activity) getActivity()).showPopCreateAudio();
        }


        getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();

    }

    public void setAudio_mode(String audio_mode) {
        this.audio_mode = audio_mode;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.backImg:

                hideFragment();

                break;

            case R.id.controlImg:

                if (mediaPlayer != null) {

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        controlImg.setImageResource(R.drawable.ic200_video_dark);

                    } else {
                        mediaPlayer.start();
                        controlImg.setImageResource(R.drawable.ic200_recording_dark);

                    }

                }

                break;


        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that fragment is currently visible
        if (!isVisibleToUser && isResumed()) {
            // 调用Fragment隐藏的代码段

            MyLog.Set("e", getClass(), "setUserVisibleHint => " + "!isVisibleToUser && isResumed()");

            cleanMedia();


        } else if (isVisibleToUser && isResumed()) {
            // 调用Fragment显示的代码段


        }
    }


    @Override
    public void onPause() {
        super.onPause();


        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            controlImg.setImageResource(R.drawable.ic200_video_dark);
        }

        cleanTimer();

    }

    @Override
    public void onDestroy() {

        cleanMedia();

        cleanTimer();

        super.onDestroy();
    }


}

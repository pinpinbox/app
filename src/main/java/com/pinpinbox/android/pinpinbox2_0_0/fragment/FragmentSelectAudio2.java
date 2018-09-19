package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.database.Cursor;
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

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerAudioFileAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FragmentSelectAudio2 extends Fragment implements View.OnClickListener{


    private LoaderManager loaderManager;

    private RecyclerAudioFileAdapter adapter;

    private List<String> filePathList;

    private RecyclerView rvAudioFile;



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

        (v.findViewById(R.id.backImg)).setOnClickListener(this);


        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBundle();

        init();

        setRecycler();

        loaderManager.initLoader(0, null, loaderCallbacks);


    }





    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        private CursorLoader cursorLoader;

        private String[] projection = {MediaStore.Audio.Media._ID,//0
                MediaStore.Audio.Media.DATA,//1
                MediaStore.Audio.Media.DATE_ADDED//2
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

            cursor.moveToLast();

                do {

                    String path = cursor.getString(cursor.getColumnIndex(projection[1]));
                    File file = new File(path);
                    if (file.exists()) {


                        filePathList.add(file.getName());

                        int _id = cursor.getInt(cursor.getColumnIndex(projection[0]));

                        MyLog.Set("e", FragmentSelectAudio2.class, "path => " + path);
                        MyLog.Set("e", FragmentSelectAudio2.class, "_id => " + _id);
                        MyLog.Set("d", FragmentSelectAudio2.class, "---------------------------------------------------------------------");

                    }



                } while (cursor.moveToPrevious());

                adapter.notifyDataSetChanged();



        }

    };







    private void getBundle(){


    }



    private void init(){


        loaderManager = getLoaderManager();

        filePathList = new ArrayList<>();


    }

    private void setRecycler() {

        adapter = new RecyclerAudioFileAdapter(getActivity(), filePathList);
        rvAudioFile.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAudioFile.setLayoutManager(manager);

        adapter.setOnRecyclerViewListener(new RecyclerAudioFileAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

        adapter.setOnUploadListener(new RecyclerAudioFileAdapter.OnUploadListener() {
            @Override
            public void onClick(int position) {

            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.backImg:

                getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();

                break;


        }


    }
}

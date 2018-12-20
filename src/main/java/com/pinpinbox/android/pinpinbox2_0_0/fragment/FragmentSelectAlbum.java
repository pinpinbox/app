package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.FromSharePhoto2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerSelectAlbumAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class FragmentSelectAlbum extends Fragment {


    public static final String MINE = "mine";

    public static final String COOPERATION = "cooperation";


    private GetAlbumTask getAlbumTask;

    private LoadingAnimation loading;

    private RecyclerSelectAlbumAdapter adapter;

    private List<ItemAlbum> itemAlbumList;

    private RecyclerView rvSelectAlbum;

    private String strRank;

    private int lastPosition = -1;
    private int thisPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {

            strRank = bundle.getString("rank");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_select_album, container, false);

        rvSelectAlbum = v.findViewById(R.id.rvSelectAlbum);


        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        setRecycler();

        doGetAlbum();

    }

    private void init() {

        loading = ((FromSharePhoto2Activity) getActivity()).getLoading();

        itemAlbumList = new ArrayList<>();

    }

    private void setRecycler() {

        adapter = new RecyclerSelectAlbumAdapter(getActivity(), itemAlbumList);

        rvSelectAlbum.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSelectAlbum.setLayoutManager(manager);

        adapter.setOnRecyclerViewListener(new RecyclerSelectAlbumAdapter.OnRecyclerViewListener() {

            @Override
            public void onItemClick(int position, View v) {

                /*取消上次點擊*/
                if (lastPosition != -1) {
                    itemAlbumList.get(lastPosition).setSelect(false);
                    adapter.notifyItemChanged(lastPosition, "no_animation");
                }
                lastPosition = position;


                /*設置當前點擊*/
                itemAlbumList.get(position).setSelect(true);
                adapter.notifyItemChanged(position, "no_animation");
                thisPosition = position;


                FragmentFromShareText fragmentFromShareText = (FragmentFromShareText) ((FromSharePhoto2Activity) getActivity()).getFragment(FragmentFromShareText.class.getSimpleName());

                if (fragmentFromShareText != null) {

                    /*設置選取的album_id*/
                    fragmentFromShareText.setItemAlbum(itemAlbumList.get(position));


                    /*設置選取的頁面*/
                    switch (strRank) {
                        case MINE://0
                            fragmentFromShareText.clearOtherPageItem(1);
                            break;

                        case COOPERATION://1
                            fragmentFromShareText.clearOtherPageItem(0);
                            break;
                    }

                }


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                doGetAlbum();

            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);
    }

    private void doGetAlbum() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((FromSharePhoto2Activity) getActivity()).setNoConnect();
            return;
        }

        getAlbumTask = new GetAlbumTask();
        getAlbumTask.execute();

    }

    @SuppressLint("StaticFieldLeak")
    private class GetAlbumTask extends AsyncTask<Void, Void, Object> {

        private int p17Result = -1;
        private String p17Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getAlbumList();

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

            if (p17Result == 1) {

                adapter.notifyDataSetChanged();

            } else if (p17Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p17Message);
            } else if (p17Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }


        }


        private void getAlbumList() {

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P17_GetCloudAlbumList, SetMapByProtocol.setParam17_getcloudalbumlist(
                        PPBApplication.getInstance().getId(),
                        PPBApplication.getInstance().getToken(),
                        strRank,
                        "0,1000"), null);
            } catch (SocketTimeoutException timeout) {
                p17Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p17Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p17Result == 1) {

                        String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONArray jsonArray = new JSONArray(data);

                        int count = jsonArray.length();

                        for (int i = 0; i < count; i++) {

                            JSONObject obj = (JSONObject) jsonArray.get(i);

                            ItemAlbum itemAlbum = new ItemAlbum();

                            /*album*/
                            String album = JsonUtility.GetString(obj, ProtocolKey.album);

                            JSONObject jsonAlbum = new JSONObject(album);

                            itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                            itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_name));
                            itemAlbum.setCover(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover));
                            itemAlbum.setCount_photo(JsonUtility.GetInt(jsonAlbum, ProtocolKey.count_photo));
                            itemAlbum.setSelect(false);

                            /*usergrade*/
                            String userGrade = JsonUtility.GetString(obj, ProtocolKey.usergrade);

                            JSONObject jsonUserGrade = new JSONObject(userGrade);

                            itemAlbum.setPhoto_limit_of_album(JsonUtility.GetInt(jsonUserGrade, ProtocolKey.photo_limit_of_album));

                            /*cooperation*/
                            String cooperation = JsonUtility.GetString(obj, ProtocolKey.cooperation);

                            JSONObject jsonCooperation = new JSONObject(cooperation);

                            itemAlbum.setIdentity(JsonUtility.GetString(jsonCooperation, ProtocolKey.identity));

                            /*template*/
                            String template = JsonUtility.GetString(obj, ProtocolKey.template);

                            JSONObject jsonTemplate = new JSONObject(template);

                            itemAlbum.setTemplate_id(JsonUtility.GetInt(jsonTemplate, ProtocolKey.template_id));

                            itemAlbumList.add(itemAlbum);

                        }

                    } else if (p17Result == 0) {
                        p17Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }


    }


    public void resetItem() {
        lastPosition = -1;
        if (thisPosition != -1) {
            itemAlbumList.get(thisPosition).setSelect(false);
            adapter.notifyItemChanged(thisPosition, "no_animation");
        }
    }


    @Override
    public void onDestroy() {

        if(getAlbumTask!=null && !getAlbumTask.isCancelled()){
            getAlbumTask.cancel(true);
        }
        getAlbumTask = null;

        super.onDestroy();
    }


}

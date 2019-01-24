package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MainActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerSearchAlbumAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerSearchUserAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ExLinearLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SpacesItemDecoration;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StaggeredHeight;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
 * Created by kevin9594 on 2017/3/4.
 */
public class FragmentSearch extends Fragment {


    private NoConnect noConnect;
    private CountDownTimer countDownTimer;
    private LoadingAnimation loading;
    private InputMethodManager inputMethodManager;


    private UserListTask userListTask;
    private AlbumListTask albumListTask;

    private RecyclerSearchUserAdapter userAdapter;
    private RecyclerSearchAlbumAdapter albumAdapter;


    private List<ItemUser> itemUserList;

    private ArrayList<ItemAlbum> searchAlbumList;

    private String id, token;
    private String strSearch = "";

    private int doingType;

    private View viewHeader;
    private RecyclerView rvSearch;
    private EditText edSearch;

    private TextView tvGuideNoAlbum;

    private SmoothProgressBar pbRefresh;

    /*header*/
    private RecyclerView rvUser;
    private TextView tvSearchUserTitle, tvSearchAlbumTitle;
    private TextView tvGuideNoUser;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_search, container, false);

        rvSearch = v.findViewById(R.id.rvSearch);


        tvGuideNoAlbum = v.findViewById(R.id.tvGuideNoAlbum);

        if (getActivity() != null) {
            FragmentHome fragmentHome = (FragmentHome) (((MainActivity) getActivity()).getFragment(FragmentHome.class.getSimpleName()));
            if (fragmentHome != null) {
                pbRefresh = fragmentHome.getPbRefresh();
                edSearch = fragmentHome.getEdSearch();
            }
        }

        viewHeader = getActivity().getLayoutInflater().inflate(R.layout.header_2_0_0_search, null);
        rvUser = viewHeader.findViewById(R.id.rvUser);
        tvSearchUserTitle = viewHeader.findViewById(R.id.tvSearchUserTitle);
        tvSearchAlbumTitle = viewHeader.findViewById(R.id.tvSearchAlbumTitle);
        tvGuideNoUser = viewHeader.findViewById(R.id.tvGuideNoUser);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        setUserRecycler();
        setAlbumRecycler();
        setSearch();

    }

    private void init() {

        loading = ((MainActivity) getActivity()).getLoading();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        itemUserList = new ArrayList<>();
        searchAlbumList = new ArrayList<>();

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();
    }

    private void setUserRecycler() {
        ExLinearLayoutManager layoutManager = new ExLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvUser.setLayoutManager(layoutManager);
        userAdapter = new RecyclerSearchUserAdapter(getActivity(), itemUserList);
        rvUser.setAdapter(userAdapter);

        userAdapter.setOnRecyclerViewListener(new RecyclerSearchUserAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                if (itemUserList == null || itemUserList.size() < 1) {
                    return;
                }

                String strSearch = edSearch.getText().toString();

                if (strSearch.equals("")) {
                    FlurryUtil.onEvent(FlurryKey.search_select_default_user);
                } else {
                    FlurryUtil.onEvent(FlurryKey.search_success_user_to_select);
                }

                if (itemUserList.get(position).getUser_id().equals(id)) {

                    ((MainActivity) getActivity()).toMePage(false);

                } else {

                    ActivityIntent.toUser(
                            getActivity(),
                            true,
                            false,
                            itemUserList.get(position).getUser_id(),
                            itemUserList.get(position).getPicture(),
                            itemUserList.get(position).getName(),
                            v.findViewById(R.id.userImg)
                    );

                }

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    private void setAlbumRecycler() {

        rvSearch.addItemDecoration(new SpacesItemDecoration(16, true));

        albumAdapter = new RecyclerSearchAlbumAdapter(getActivity(), searchAlbumList);
        rvSearch.setAdapter(albumAdapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(albumAdapter);
        rvSearch.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        ExStaggeredGridLayoutManager manager = null;

        if (SystemUtility.isTablet(getActivity().getApplicationContext())) {

            //平版
            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        } else {

            //手機
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        }


//        ExStaggeredGridLayoutManager manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvSearch.getAdapter(), manager.getSpanCount()));
        rvSearch.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(rvSearch, viewHeader);

        albumAdapter.setOnRecyclerViewListener(new RecyclerSearchAlbumAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                if (searchAlbumList == null || searchAlbumList.size() < 1) {
                    return;
                }

                String strSearch = edSearch.getText().toString();

                if (strSearch.equals("")) {
                    FlurryUtil.onEvent(FlurryKey.search_select_default_album);
                } else {
                    FlurryUtil.onEvent(FlurryKey.search_success_album_to_select);
                }

                ActivityIntent.toAlbumInfo(
                        getActivity(),
                        true,
                        searchAlbumList.get(position).getAlbum_id(),
                        searchAlbumList.get(position).getCover(),
                        searchAlbumList.get(position).getImage_orientation(),
                        searchAlbumList.get(position).getCover_width(),
                        searchAlbumList.get(position).getCover_height(),
                        v.findViewById(R.id.coverImg)
                );

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

        rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1) {
                    //在用手指滾動
                    inputMethodManager.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
                }
            }
        });

    }


    public void setOnFinish() {
        if (edSearch.getText().toString().equals(strSearch)) {

            MyLog.Set("d", FragmentSearch.class, "字串沒變更");

        } else {

            strSearch = edSearch.getText().toString();

            loadDataBegin();
            doSearchUser();
            doSearchAlbum();

        }
    }

    public void setAfterTextChanged(Editable s) {

        if (s.toString().equals("")) {

            if (countDownTimer != null) {
                countDownTimer.cancel();
            }


        } else {

            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer.start();

                MyLog.Set("d", FragmentSearch.class, "重新倒數");
            }


        }

    }

    private void setSearch() {

        countDownTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MyLog.Set("d", FragmentSearch.class, "timer =>" + (millisUntilFinished / 1000) + "");
            }

            @Override
            public void onFinish() {
                MyLog.Set("d", FragmentSearch.class, "timer => finish()");
                countDownTimer.cancel();

                setOnFinish();


            }
        };

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                MyLog.Set("d", FragmentSearch.class, "afterTextChanged");


                setAfterTextChanged(s);


            }
        });


    }

    private void loadDataBegin() {
        pbRefresh.setVisibility(View.VISIBLE);
        pbRefresh.progressiveStart();

    }

    private void loadDataEnd() {
        pbRefresh.progressiveStop();
        pbRefresh.setVisibility(View.GONE);

    }

    private void setSearchUserData() {


        if (itemUserList.size() < 1) {
            tvGuideNoUser.setVisibility(View.VISIBLE);
        } else {
            tvGuideNoUser.setVisibility(View.GONE);
        }

        userAdapter.notifyItemRangeInserted(0, itemUserList.size());

    }

    private void setSearchAlbumData() {


        if (searchAlbumList.size() < 1) {
            tvGuideNoAlbum.setVisibility(View.VISIBLE);
        } else {
            tvGuideNoAlbum.setVisibility(View.GONE);
        }

        albumAdapter.notifyItemRangeInserted(0, searchAlbumList.size());

    }

    public void scrollToTop() {

        try {

            ExStaggeredGridLayoutManager linearLayoutManager = (ExStaggeredGridLayoutManager) rvSearch.getLayoutManager();

            int[] firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPositions(null);

            if (firstVisibleItemPosition[0] > 10) {
                rvSearch.scrollToPosition(10);
                MyLog.Set("d", getClass(), "先移動至第10項目");
            }

            rvSearch.smoothScrollToPosition(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cleanPicasso() {

        if (itemUserList != null && itemUserList.size() > 0) {
            for (int i = 0; i < itemUserList.size(); i++) {
                String strPicture = itemUserList.get(i).getPicture();
                Picasso.with(getActivity().getApplicationContext()).invalidate(strPicture);
            }
            itemUserList.clear();
        }

        if (searchAlbumList != null && searchAlbumList.size() > 0) {
            for (int i = 0; i < searchAlbumList.size(); i++) {
                String cover = searchAlbumList.get(i).getCover();
                Picasso.with(getActivity().getApplicationContext()).invalidate(cover);
            }
            searchAlbumList.clear();
        }


    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoingTypeClass.DoGetSearchDefaultData:
//                        doGetDefaultData();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);
    }


    private void doSearchUser() {
        if (checkConnect()) {
            userListTask = new UserListTask();
            userListTask.execute();
        }
    }

    private void doSearchAlbum() {

        if (checkConnect()) {
            albumListTask = new AlbumListTask();
            albumListTask.execute();
        }

    }

    private boolean checkConnect() {
        if (!HttpUtility.isConnect(getActivity())) {
//            noConnect = new NoConnect(getActivity());
            ((MainActivity) getActivity()).setNoConnect();
            return false;
        } else {
            return true;
        }
    }


    private class UserListTask extends AsyncTask<Void, Void, Object> {

        private int p41Result = -1;
        private String p41Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSearchUserList;

            /*執行搜尋前全部移除*/
            userAdapter.notifyItemRangeRemoved(0, itemUserList.size());


            if (itemUserList.size() > 0) {
                for (int i = 0; i < itemUserList.size(); i++) {
                    String strPicture = itemUserList.get(i).getPicture();
                    Picasso.with(getActivity().getApplicationContext()).invalidate(strPicture);
                }
                itemUserList.clear();
            }

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P41_Search,
                        SetMapByProtocol.setParam41_search(id, token, "user", strSearch, "0,32"), null);
                MyLog.Set("d", getClass(), "p41strJson(user) =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p41Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p41Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p41Result == 1) {

                        String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);


                            String user = JsonUtility.GetString(j, ProtocolKey.user);
                            JSONObject object = new JSONObject(user);


                            ItemUser itemUser = new ItemUser();
                            itemUser.setName(JsonUtility.GetString(object, ProtocolKey.name));
                            itemUser.setPicture(JsonUtility.GetString(object, ProtocolKey.picture));

                            String user_id = JsonUtility.GetString(object, ProtocolKey.user_id);
                            itemUser.setUser_id(user_id);
                            itemUserList.add(itemUser);


                        }


                    } else if (p41Result == 0) {
                        p41Message = jsonObject.getString(Key.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loadDataEnd();


            if (p41Result == 1) {

                tvSearchUserTitle.setText(R.string.pinpinbox_2_0_0_title_find_creator);
                setSearchUserData();

                //20171113
                if (rvSearch.getAlpha() == 0f) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ViewPropertyAnimator alphaTo1 = rvSearch.animate();
                            alphaTo1.setDuration(400)
                                    .alpha(1)
                                    .start();
                        }
                    }, 200);

                }


            } else if (p41Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p41Message);
            } else if (p41Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }

        }


    }

    private class AlbumListTask extends AsyncTask<Void, Void, Object> {

        private int p41Result = -1;
        private String p41Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSearchAlbumList;

            /*執行搜尋前全部移除*/
            albumAdapter.notifyItemRangeRemoved(0, searchAlbumList.size());


            if (searchAlbumList.size() > 0) {
                for (int i = 0; i < searchAlbumList.size(); i++) {
                    String cover = searchAlbumList.get(i).getCover();
                    Picasso.with(getActivity().getApplicationContext()).invalidate(cover);
                }
                searchAlbumList.clear();
            }


        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P41_Search,
                        SetMapByProtocol.setParam41_search(id, token, "album", strSearch, "0,32"), null);
                MyLog.Set("d", getClass(), "p41strJson(album) =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p41Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p41Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p41Result == 1) {

                        String jdata = JsonUtility.GetString(jsonObject, Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);

                        int minHeight = DensityUtility.dip2px(getActivity().getApplicationContext(), 72);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);

                            ItemAlbum itemAlbum = new ItemAlbum();


                            String album = JsonUtility.GetString(j, ProtocolKey.album);
                            JSONObject jsonAlbum = new JSONObject(album);

                            itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                            itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.name));

                            String cover = JsonUtility.GetString(jsonAlbum, ProtocolKey.cover);
                            itemAlbum.setCover(cover);

                            try {
                                int width = jsonAlbum.getInt(ProtocolKey.cover_width);
                                int height = jsonAlbum.getInt(ProtocolKey.cover_height);
                                int image_height = StaggeredHeight.setImageHeight(width, height);

                                if (image_height < minHeight) {
                                    image_height = minHeight;
                                }

                                itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                                itemAlbum.setCover_height(image_height);
                                itemAlbum.setCover_hex(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover_hex));
                                if (width > height) {
                                    itemAlbum.setImage_orientation(ItemAlbum.LANDSCAPE);
                                } else if (height > width) {
                                    itemAlbum.setImage_orientation(ItemAlbum.PORTRAIT);
                                } else {
                                    itemAlbum.setImage_orientation(0);
                                }

                            } catch (Exception e) {
                                itemAlbum.setCover_hex("");
                                itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                                itemAlbum.setCover_height(PPBApplication.getInstance().getStaggeredWidth());
                                MyLog.Set("e", this.getClass(), "圖片長寬無法讀取");
                            }

                            String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.usefor);
                            JSONObject jsonUsefor = new JSONObject(usefor);
                            itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.exchange));
                            itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.slot));
                            itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.video));
                            itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio));


                            String user = JsonUtility.GetString(j, ProtocolKey.user);
                            JSONObject jsonUser = new JSONObject(user);
                            itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.name));

                            searchAlbumList.add(itemAlbum);
                        }


                    } else if (p41Result == 0) {
                        p41Message = jsonObject.getString(ProtocolKey.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loadDataEnd();

            if (p41Result == 1) {

                tvSearchAlbumTitle.setText(R.string.pinpinbox_2_0_0_title_find_album);
                setSearchAlbumData();


            } else if (p41Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p41Message);
            } else if (p41Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }


        }

    }


    @Override
    public void onPause() {

        if (itemUserList != null && searchAlbumList != null) {

            if (itemUserList.size() > 0) {
                for (int i = 0; i < itemUserList.size(); i++) {
                    String strPicture = itemUserList.get(i).getPicture();
                    Picasso.with(getActivity().getApplicationContext()).invalidate(strPicture);
                }
            }

            if (searchAlbumList.size() > 0) {
                for (int i = 0; i < searchAlbumList.size(); i++) {
                    String cover = searchAlbumList.get(i).getCover();
                    Picasso.with(getActivity().getApplicationContext()).invalidate(cover);
                }
            }

            System.gc();
        }


        super.onPause();
    }

    @Override
    public void onDestroy() {

        if (userListTask != null && !userListTask.isCancelled()) {
            userListTask.cancel(true);
        }
        userListTask = null;

        if (albumListTask != null && !albumListTask.isCancelled()) {
            albumListTask.cancel(true);
        }
        albumListTask = null;

        cleanPicasso();
        System.gc();


        super.onDestroy();
    }

}

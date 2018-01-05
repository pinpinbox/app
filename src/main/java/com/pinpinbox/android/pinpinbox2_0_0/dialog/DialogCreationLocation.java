package com.pinpinbox.android.pinpinbox2_0_0.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pinpinbox.android.R;
import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.Utility.MapUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by vmage on 2016/8/30.
 */
public class DialogCreationLocation {

    public FragmentActivity mActivity;
    public Dialog mDialog;
    private GoogleMap mapPhoto;
    private Location mLocation = null;

    private ImageView confirmImg, cancelImg;
    private EditText edLocation;


    public DialogCreationLocation(final FragmentActivity activity, Location location) {
        this.mActivity = activity;
        this.mLocation = location;
        mDialog = new Dialog(activity, R.style.myDialog);

        Window window = mDialog.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
        }

        window.setWindowAnimations(R.style.dialog_enter_exit);
        window.setContentView(R.layout.dialog_2_0_0_creation_location);

        init();

        cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

                if (mapPhoto != null) {
                    mapPhoto.clear();
                }

            }
        });
        mDialog.show();
    }


    private void init() {
        confirmImg = (ImageView) mDialog.findViewById(R.id.confirmImg);
        cancelImg = (ImageView) mDialog.findViewById(R.id.cancelImg);
        edLocation = (EditText) mDialog.findViewById(R.id.edLocation);

        try {

            SupportMapFragment mapFragment = (SupportMapFragment) mActivity.getSupportFragmentManager()
                    .findFragmentById(R.id.mapSetLocation);
            mapFragment.getMapAsync(new PageMapCallBack());


        } catch (Exception e) {
            e.printStackTrace();
        }


        mDialog.findViewById(R.id.tvPreview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String l = edLocation.getText().toString();

                if (l.equals("")) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_location);
                    return;
                }
                setLocation(edLocation.getText().toString());
            }
        });

//        dialog.findViewById(R.id.tvTitle)

    }

    private class PageMapCallBack implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mapPhoto = googleMap;
            mapPhoto.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            UiSettings setting = mapPhoto.getUiSettings();
            setting.setTiltGesturesEnabled(true);
//            mapPhoto.setMyLocationEnabled(true);
        }
    }


    public void setLocation(final String location) {

        final String strNewLocation = location.replaceAll(" ", "");
        new Thread(new Runnable() {

            private Double locLat = 0.0, locLng = 0.0;

            @Override
            public void run() {
                JSONObject obj = MapUtility.getLocationInfo(strNewLocation);
                try {
                    if (obj.get("results") != null && !obj.get("results").equals("")) {
                        if (((JSONArray) obj.get("results")).length() != 0) {
                            locLat = ((JSONArray) obj.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            locLng = ((JSONArray) obj.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            MyLog.Set("d", mActivity.getClass(), "locLat(緯度) => " + locLat);
                            MyLog.Set("d", mActivity.getClass(), "locLng(經度) => " + locLng);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (mapPhoto != null) {
                            mapPhoto.clear();
                        }


                        if (mLocation == null) {
                            mLocation = new Location("");
                        }

                        if (location.equals("")) {

                            MyLog.Set("d", getClass(), "沒有輸入地點");

//                            locLat = mLocation.getLatitude();
//                            locLng = mLocation.getLongitude();
                            locLat = 23.9036873;
                            locLng = 121.0793705;

                        }


                        final LatLng latlng = new LatLng(locLat, locLng);

//                        mapPhoto.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//                            @Override
//                            public boolean onMyLocationButtonClick() {
//                                MyLog.Set("d", this.getClass(), "onMyLocationButtonClick");
//                                double lat = mLocation.getLatitude();
//                                double lng = mLocation.getLongitude();
//
//                                MyLog.Set("d", this.getClass(), "onMyLocationButtonClick mLocation lat => " + lat);
//                                MyLog.Set("d", this.getClass(), "onMyLocationButtonClick mLocation lng => " + lng);
//
//                                return false;
//                            }
//                        });


                        mapPhoto.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 7), 2000, new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                if (!location.equals("")) {
                                    mapPhoto.addMarker(new MarkerOptions()
                                            .title(location)
                                            .position(latlng))
                                            .showInfoWindow();
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                });
            }
        }).start();
    }

    public ImageView getConfirmImg() {
        return this.confirmImg;
    }

    public EditText getEdLocation() {
        return this.edLocation;
    }

    public Dialog getDialog() {
        return this.mDialog;
    }


//    public String getLngAndLatWithNetwork() {
//        double latitude = 0.0;
//        double longitude = 0.0;
//        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        if (location != null) {
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//        }
//        return longitude + "," + latitude;
//    }
//
//    private LocationListener locationListener = new LocationListener() {
//
//        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        // Provider被enable时触发此函数，比如GPS被打开
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        // Provider被disable时触发此函数，比如GPS被关闭
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//
//        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//        @Override
//        public void onLocationChanged(Location location) {
//        }
//    };


}

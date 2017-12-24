package com.pinpinbox.android.Utility;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import okhttp3.MultipartBody;


/**
 * Created by vmage on 2015/1/14
 */
public class HttpUtility {

    public static Bitmap getNetBitmapByNoFit(String s) {
        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();

            BitmapFactory.Options opts = new BitmapFactory.Options();

            opts.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(is, null, opts);

            opts.inJustDecodeBounds = false;

            int w = opts.outWidth;
            int h = opts.outHeight;

            int wh = w * h;

            if (wh > 4000000) {
                Log.e("HttpUtility", "  opts.inSampleSize = 3");
                opts.inSampleSize = 3;

            } else if (wh < 4000000 && wh > 500000) {
                Log.e("HttpUtility", "  opts.inSampleSize = 2");
                opts.inSampleSize = 2;
            } else {
                Log.e("HttpUtility", "  opts.inSampleSize = 1");
                opts.inSampleSize = 1;
            }
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            try {
                bitmap = BitmapFactory.decodeStream(is, null, opts);

            } catch (OutOfMemoryError error) {
                Log.e("HttpUtility", "OutOfMemoryError error");
                error.printStackTrace();
            }


            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getNetBitmap(String s) {

        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();


            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            try {
                bitmap = BitmapFactory.decodeStream(is, null, opts);
            } catch (OutOfMemoryError error) {
                Log.e("HttpUtility", "OutOfMemoryError error");
                error.printStackTrace();
            }


            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    //
    public static String uploadSubmit(String url, Map<String, String> param, File file) throws Exception {


        String resultString = "";

        OkHttpClientManager.Param[] params = OkHttpClientManager.map2Params(param);


        if (file == null) {
            resultString = OkHttpClientManager.postAsString(url, params);
        } else {

            resultString = OkHttpClientManager.post(url, file, "file", params).body().string();

        }

        return resultString;

//        ByteArrayBuffer bt = new ByteArrayBuffer(4096);
//
//        String resultString = "";
//        MultipartEntity entity = new MultipartEntity();
//        if (param != null && !param.isEmpty()) {
//            for (Map.Entry<String, String> entry : param.entrySet()) {
//                entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
//            }
//        }
//        Log.e("log", param.toString());
//        // 加文件參數
//        if (file != null && file.exists()) {
//            entity.addPart("file", new FileBody(file));//file   傳送檔案 後台接受的key
//        }
//        HttpClient client = new DefaultHttpClient();
//
//            HttpPost post = new HttpPost(url);
//            post.setEntity(entity);
//            post.addHeader("accept-encoding", "gzip, deflate");
//
//
//
////            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
////
////            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
//
//
//        HttpResponse response = new DefaultHttpClient().execute(post);
//
//            HttpResponse response = client.execute(post);
//            int stateCode = response.getStatusLine().getStatusCode();
//
//
//            if (stateCode == HttpStatus.SC_OK) {
//                HttpEntity result = response.getEntity();
//                if (result != null) {
//                    GZIPInputStream gis = new GZIPInputStream(result.getContent());
//                    int l;
//                    byte[] tmp = new byte[4096];
//
//                    while ((l = gis.read(tmp)) != -1) {
//                        bt.append(tmp, 0, l);
//
//                    }
//                    resultString = new String(bt.toByteArray(), "utf-8");
//                }
//            }
//
//            client.getConnectionManager().shutdown();
//        return resultString[0];

    }

    private static OkHttpClientManager.Param[] validateParam(OkHttpClientManager.Param[] params) {
        if (params == null)
            return new OkHttpClientManager.Param[0];
        else return params;
    }


    private static okhttp3.RequestBody buildMultipartFormRequestByOkHttp3(File[] files,
                                                      String[] fileKeys, OkHttpClientManager.Param[] params){

        params = validateParam(params);

        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (OkHttpClientManager.Param param : params) {
            multipartBody.addPart(okhttp3.Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    okhttp3.RequestBody.create(null, param.value));
        }

        if (files != null) {
            okhttp3.RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = okhttp3.RequestBody.create(okhttp3.MediaType.parse(guessMimeType(fileName)), file);

                //TODO 根据文件名设置contentType
                multipartBody.addPart(okhttp3.Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }


        return multipartBody.build();
    }


    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    public static okhttp3.RequestBody uploadSubmitWithFileLengthByOkHttp3(Map<String, String> param, File file) throws Exception {

        okhttp3.RequestBody requestBody = null;

        OkHttpClientManager.Param[] params = OkHttpClientManager.map2Params(param);

        requestBody = buildMultipartFormRequestByOkHttp3(new File[]{file}, new String[]{"file"}, params);

        return requestBody;
    }

    public static String uploadSubmit(boolean setTimeOut, String url, Map<String, String> param, File file) throws Exception {

        String resultString = "";

        OkHttpClientManager.Param[] params = OkHttpClientManager.map2Params(param);

        if (file == null) {

            if (setTimeOut) {

                    resultString = OkHttpClientManager.postAsStringByTimeOut(8000, url, params);

            } else {

                /**default 20 min*/
                resultString = OkHttpClientManager.postAsString(url, params);

            }

        } else {
            resultString = OkHttpClientManager.post(url, file, "file", params).body().string();
        }
        return resultString;
    }

    public static String uploadSubmit(long millionTime, String url, Map<String, String> param, File file) throws Exception {

        String resultString = "";

        OkHttpClientManager.Param[] params = OkHttpClientManager.map2Params(param);

        if (file == null) {

            resultString = OkHttpClientManager.postAsStringByTimeOut(millionTime, url, params);

        } else {
            resultString = OkHttpClientManager.post(url, file, "file", params).body().string();
        }
        return resultString;
    }

    public static BitmapFactory.Options getBitmapWidthHeightFromUrl(String url) {
        Bitmap bitmap = null;
        byte[] datas = getImage(url);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        if (datas != null && datas.length > 0) {
            bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
        }
        return opts;
    }

    public static Bitmap getFitBitmap(String url, int width, int height) {
        Bitmap bitmap = null;
        byte[] datas = getImage(url);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;

        if (datas != null && datas.length > 0) {
            bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
        }

        opts.inJustDecodeBounds = false;

        // 計算縮放比
        int h = opts.outHeight;
        int w = opts.outWidth;

        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }

        opts.inSampleSize = be;

        if (w * 3 == h * 2) {

            if (datas != null && datas.length > 0) {
                bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
                try {
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                } catch (OutOfMemoryError e) {
                    Log.e("HttpUtility => getFitBitmap", "OutOfMemoryError");
                    e.printStackTrace();
                }
            }

            Log.e("HttpUtility => getFitBitmap", "獲得修正bitmap比例為2 : 3");
        } else {

            Log.e("HttpUtility => getFitBitmap", "獲得原尺寸picBmp");
//            bitmap = HttpUtility.getNetBitmapByNoFit(url);

            int wh = w * h;

            if (wh > 4000000) {
                Log.e("HttpUtility", "  opts.inSampleSize = 3");
                opts.inSampleSize = 3;

            } else if (wh < 4000000 && wh > 500000) {
                Log.e("HttpUtility", "  opts.inSampleSize = 2");
                opts.inSampleSize = 2;
            } else {
                Log.e("HttpUtility", "  opts.inSampleSize = 1");
                opts.inSampleSize = 1;
            }
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            try {
                bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
            } catch (OutOfMemoryError error) {
                Log.e("HttpUtility", "OutOfMemoryError error");
                error.printStackTrace();
            }

        }

        return bitmap;
    }


    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param acti.nUrl
     * @param params
     * @param files
     * @return
     * @throws IOException
     */




    //下載圖片
    public void downPic(String url, String fileName, String filepath, Context context) {
        try {

            byte[] data = getImage(url);
            if (data != null) {
                Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
                saveFile(mBitmap, fileName, filepath);
            } else {

                //timeout
//                Toast.makeText(context, "Image error!", 1).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveFile(Bitmap bm, String fileName, String path) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    public static byte[] getImage(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(6 * 1000);
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readStream(inStream);
            } else if (conn.getResponseCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static String readData(InputStream inSream, String charsetName) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inSream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inSream.close();

        return new String(data, charsetName);
    }

    public static boolean isConnect(Activity activity) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Log.v("error", e.toString());
        }
        return false;
    }

    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }


    /**
     * 續傳下載
     */

//    public static void DownloadFileByBreakpoints(final Activity activity, DownloadBean bean,
//                                                 Handler handler,
//                                                 Map<String, String> params,
//                                                 String albumid,
//                                                 Handler barHandler,
//                                                 int refresh) {
//
//        SharedPreferences getdata = activity.getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
//        String id = getdata.getString("id", "");
//        String myDir = "/PinPinBox" + id + "/";
//
//        String sdPath = Environment.getExternalStorageDirectory() + "/";
//        String dirAlbumlist = "albumlist/";
//        String dirZip = "zip/";
//
//        StringBuilder sbdata = new StringBuilder();
//        if (params != null && !params.isEmpty()) {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                sbdata.append(entry.getKey()).append("=");
//                try {
//                    sbdata.append(URLEncoder.encode(entry.getValue(), "utf-8"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                sbdata.append("&");
//            }
//            sbdata.deleteCharAt(sbdata.length() - 1);
//        }
//        System.out.println(sbdata.toString());
//        byte[] entity = sbdata.toString().getBytes();// 生成实体数据
//
//        FileUtility fu = new FileUtility();
//        fu.createDirInIn(myDir, dirAlbumlist, albumid + "/");//先創造一個跟檔名一樣的資料夾用來存放解壓縮之後的檔案
//
//        String filePath = sdPath + myDir + dirZip + bean.name + ".tmp";
//        HttpURLConnection httpConn = null;
//        File file = new File(filePath);
//        RandomAccessFile randomFile = null;
//        FileOutputStream fos = null;
//        int dataBlockLength = 4096;
//        byte[] data = new byte[dataBlockLength];
//        int readLength = -1;
//        InputStream is = null;
//
//        ByteArrayBuffer bt = new ByteArrayBuffer(dataBlockLength);
//        String resultString = "";
//
//
//        try {
//            if (bean.size <= 0) {
//                bean.loadedSize = 0;
//                if (!file.getParentFile().exists()) {
//                    file.getParentFile().mkdirs();
//                }
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
//                // 采用普通的下载方式
//                fos = new FileOutputStream(file);
//                httpConn = openUrl(activity, bean.url);
//
//                httpConn.setRequestMethod("POST");
//                httpConn.getOutputStream().write(entity);
//
//                int respondCode = connect(httpConn);
//
//                if (respondCode == HttpURLConnection.HTTP_OK) {
//
//                    String a = httpConn.getHeaderField("Last-Modified");
//                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa => " + a);
//
//                    long lt = getLongByGMT(a);
//
//                    Date d1 = new Date(lt);
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//
//                    String strModifiedTime = sdf.format(d1);
//
//                    Log.e("strModifiedTime", strModifiedTime);
//
//
//                    long epoch = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(strModifiedTime).getTime() / 1000;
//
//                    Log.e("epoch", epoch + "");
//
//                    getdata.edit().putLong(albumid + "_modified_time", epoch).commit();
//
//                    bean.size = httpConn.getContentLength();
//                    is = httpConn.getInputStream();
//                    Message msg = new Message();
//                    msg.what = 0;
//                    barHandler.sendMessage(msg);
//
//                    int i = 0;
//                    while ((readLength = is.read(data)) != -1 && bean.enable) {
//
//                        i++;
//                        if (i < 150) {
//                            bt.append(data, 0, readLength);
//                        }
//
//                        fos.write(data, 0, readLength);
//                        bean.loadedSize += readLength;
//                        handler.sendEmptyMessage(refresh);
//                    }
//
//
//                }
//
//
//            } else {
//                // 采用断点续传方式
//                randomFile = new RandomAccessFile(file, "rw");
//                randomFile.setLength(bean.size);
//                httpConn = openUrl(activity, bean.url);
//                httpConn.setRequestMethod("POST");
//
//                httpConn.setRequestProperty("Range", "bytes=" + bean.loadedSize + "-" + (bean.size - 1));
//
//
//                httpConn.getOutputStream().write(entity);
//                int respondCode = connect(httpConn);
//                if (respondCode == HttpURLConnection.HTTP_PARTIAL) {
//                    is = httpConn.getInputStream();
//                    while ((readLength = is.read(data)) != -1 && bean.enable) {
//                        randomFile.seek(bean.loadedSize);
//                        randomFile.write(data, 0, readLength);
//                        bean.loadedSize += readLength;
//                        handler.sendEmptyMessage(refresh);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (is != null) {
//                    is.close();
//                }
//                if (httpConn != null) {
//                    disconnect(httpConn);
//                }
//                if (fos != null) {
//                    fos.close();
//                }
//                if (randomFile != null) {
//                    randomFile.close();
//                }
//
//                try {
//                    resultString = new String(bt.toByteArray(), "utf-8");
//                } catch (OutOfMemoryError error) {
//                    Log.e("HttpUtility", "OutOfMemoryError");
//                    error.printStackTrace();
//                }
//                final JSONObject jsonObject = new JSONObject(resultString);
//                String result = jsonObject.getString("result");
//                if (result != null && result.equals("0")) {
//                    File f = new File(sdPath + myDir + dirAlbumlist + albumid);
//                    if (f.exists()) {
//                        FileUtility.delAllFile(sdPath + myDir + dirAlbumlist + albumid);
//                        FileUtility.delFolder(sdPath + myDir + dirAlbumlist + albumid);
//                        System.out.println("此album_id已刪除");
//                        activity.runOnUiThread(new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Toast.makeText(activity, "result = 0 , " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                    activity.finish();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }));
//
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public static void disconnect(HttpURLConnection httpConn) {
        if (httpConn != null) {
            httpConn.disconnect();
        }
    }

//    public static void DisconnectionToDialog(final Activity activity) {
//        if (!HttpUtility.isConnect(activity)) {
//
//            Log.d("Http => ", "DisconnectionToDialog");
//
//
//                    DialogSet d = new DialogSet(activity);
//                    d.setNoConnect();
//                    return;
//        }
//
//        String aa = "";
//
//    }


}




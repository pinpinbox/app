package com.pinpinbox.android.Utility;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vmage on 2014/12/10.
 */
public class FileUtility {




    /*建立pinpinbox用資料夾*/
    public static void createMyDir(Activity mActivity, String id){
        File mydir = new File(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id));
        if(!mydir.exists()){
            mydir.mkdirs();
        }
    }

    /*建立相片處理用資料夾*/
    public static void createCopyDir(Activity mActivity, String id){

        createMyDir(mActivity, id);

        File copyDir = new File(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.dirCopy);
        if(!copyDir.exists()){
            copyDir.mkdirs();
        }
    }


    /*建立照相處理用資料夾*/
    public static void createCamDir(Activity mActivity, String id){

        createMyDir(mActivity, id);

        File camDir = new File(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.dirCamera);
        if(!camDir.exists()){
            camDir.mkdirs();
        }
    }


    public static void createPdfFile(Activity mActivity, String id){


        createMyDir(mActivity, id);

        File pdfDir = new File(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.dirPdf);
        if(!pdfDir.exists()){
            pdfDir.mkdirs();
        }

    }








    /*建立並返回拍照後的相片文件*/
    public static File createCamFile(Activity mActivity, String id){

        Date dt = new Date();
        Long time = dt.getTime();

        File fileCam = new File(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.dirCamera + time + ".jpg");
        if(!fileCam.exists()){
            try{
                fileCam.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return fileCam;
    }


    /*建立並返回頭像文件*/
    public static File createHeadFile(Activity mActivity, String id){

        createMyDir(mActivity, id);

        File fileHeadPicture = new File(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.pathName_headpicture);
        if(!fileHeadPicture.exists()){
            try{
                fileHeadPicture.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return fileHeadPicture;
    }

    /*建立並返回個人專區banner文件*/
    public static File createUserBannerFile(Activity mActivity, String id){

        createMyDir(mActivity, id);

        File fileUserBanner = new File(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.pathName_UserBanner);
        if(!fileUserBanner.exists()){
            try{
                fileUserBanner.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return fileUserBanner;
    }


    /*建立並返回濾鏡後的相片文件*/
    public static File createAviaryFile(Activity mActivity, String id){

        createMyDir(mActivity, id);

        File fileAviary = new File(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.pathName_FromAviary);
        if(!fileAviary.exists()){
            try{
                fileAviary.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return fileAviary;
    }







    private String SDPATH;

    private int FILESIZE = 4 * 1024;

    public String getSDPATH() {
        return SDPATH;
    }

    public FileUtility() {

        // 得到當前外部儲存設備的目錄 sdcard
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    public static String MemoryUnit(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }

    // 在SD卡上創建文件
    // completePath 完整路徑
    public static File createSDFile(String completePath) throws IOException {
        File file = new File(completePath);
        file.createNewFile();
        return file;
    }





    // 在XD卡上創建目錄

    public static File CreateSDDir(String dirName){
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + dirName);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    public File createSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }


    public File createDirIn(String outdir, String indirName) {
        File dir = new File(SDPATH + outdir + indirName);

        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    public File createDirInIn(String a, String b, String c) {
        File dir = new File(SDPATH + a + b + c);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    public File createDirInInIn(String a, String b, String c, String d) {
        File dir = new File(SDPATH + a + b + c + d);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }


    // 判斷SD卡上的文件夾是否存在

    public boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    public static List<String> getPictureListByAlbumSuffix_asdf(String dir) {

        List<String> it = new ArrayList<String>();
        File f = new File(dir);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (getAllImage_asdf(file.getPath()))
                it.add(file.getPath());
        }
        return it;
    }

    public static List<String> getInSDAllPic(String dir) {

        List<String> it = new ArrayList<String>();
        File f = new File(dir);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (isImage(file.getPath()))
                it.add(file.getPath());
        }

        return it;
    }

    public static List<String> getPictureByDir(String path_dir) {
        List<String> it = new ArrayList<String>();
        File f = new File(path_dir);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (isImage(file.getPath()))
                it.add(file.getPath());
        }

        return it;
    }

    public static List<String> getMp4List(String path_dir) {
        List<String> it = new ArrayList<String>();
        File f = new File(path_dir);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (isVideo_mp4(file.getPath()))
                it.add(file.getPath());
        }

        return it;
    }

    public static List<String> getPdfList(String path_dir) {
        List<String> it = new ArrayList<String>();
        File f = new File(path_dir);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (isPdf(file.getPath()))
                it.add(file.getPath());
        }

        return it;
    }


    //獲取圖片縮圖
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = true;
        //  獲取圖片寬高 此Bitmap為null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 計算縮放比
        int h = options.outHeight;
        int w = options.outWidth;
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
        options.inSampleSize = be;
        //  重新讀取圖片 讀取縮放後的bitmap 注意這次要把options.inJustDecodeBounds設為false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        //  利用ThumbnailUtils創縮放圖 這裡要指定要縮放哪個bitmap對象
        try {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        } catch (OutOfMemoryError e) {
            Log.e("FileUtility", "OutOfMemoryError");
            e.printStackTrace();
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            long totalPixels = width / inSampleSize * height / inSampleSize;

            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }

    public static void saveBitmap(Bitmap bitmap, String path, String name) {

        File f = new File(path, name);
        if (f.exists()) {
            f.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static Bitmap getImageThumbnail_RGB8888(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = true;
        //  獲取圖片寬高 此Bitmap為null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 計算縮放比
        int h = options.outHeight;
        int w = options.outWidth;
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
        options.inSampleSize = be;
        //  重新讀取圖片 讀取縮放後的bitmap 注意這次要把options.inJustDecodeBounds設為false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        //  利用ThumbnailUtils創縮放圖 這裡要指定要縮放哪個bitmap對象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    // 將一個InputStream理面的數據寫入SD卡中

    public File write2SDFromInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[FILESIZE];

            int length;
            while ((length = (input.read(buffer))) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    //判斷圖片文件屬性
    public static boolean isImage(String fName) {
        boolean re;
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            re = true;
        } else {
            re = false;
        }
        return re;
    }

    //|| end.equals("png#asdf")
    public static boolean getAllImage_asdf(String fName) {
        boolean re;
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        if (end.equals("jpg#asdf") || end.equals("gif#asdf")
                || end.equals("jpeg#asdf") || end.equals("bmp#asdf")) {
            re = true;
        } else {
            re = false;
        }
        return re;
    }

    public static boolean isVideo_mp4(String fName) {
        boolean re;
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        if (end.equals("mp4")) {
            re = true;
        } else {
            re = false;
        }
        return re;
    }

    public static boolean isPdf(String fName) {
        boolean re;
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        if (end.equals("pdf")) {
            re = true;
        } else {
            re = false;
        }
        return re;
    }

    public static boolean isPdf_(String fName){

        boolean re;

        if(fName.length()>4){

            String end = fName
                    .substring(fName.lastIndexOf(".") + 1, fName.length())
                    .toLowerCase();
            if (end.equals("pdf")) {
                re = true;
            } else {
                re = false;
            }
        }else {
            re = false;
        }

        return re;
    }

    public static boolean getMusic(String mName) {
        boolean re;
        String end = mName
                .substring(mName.lastIndexOf(".") + 1, mName.length())
                .toLowerCase();
        if (end.equals("mp3")) {
            re = true;
        } else {
            re = false;
        }
        return re;
    }

    public static void getMp4path(String sdpath, List<String> pathList) {
        //打开SD卡目录
        File file = new File(sdpath);
        //获取SD卡目录列表
        File[] files = file.listFiles();
        for (int z = 0; z < files.length; z++) {
            File f = files[z];
            if (f.isFile()) {
                isfile(f, pathList);
            } else {
                notfile(f, pathList);
            }
        }
    }

    public static void isfile(File file, List<String> pathList) {
        String fnm = file.getPath();
        String filename = file.getName();
        int idx = filename.lastIndexOf(".");

        if (idx <= 0) {
            return;
        }
        String suffix = filename.substring(idx + 1, filename.length());
        if (suffix.toLowerCase().equals("mp4")) {
            pathList.add(file.getPath());

        }
    }

    public static void notfile(File file, List<String> pathList){

        File[] files = file.listFiles();
        if(files == null){
            return;
        }
        for(int i = 0;i<files.length;i++){

            File fis=files[i];
            if(fis.isFile()){
                isfile(fis,pathList);
            }else{
                String SDpath=fis.getPath();
                File fileSD=new File(SDpath);

                File[] filess=fileSD.listFiles();
                if(filess == null){
                    return;
                }
                for(int j=0;j<filess.length;j++){
                    getMp4path(fileSD.toString(), pathList);
                }
            }
        }
    }

    public static void deleteFileFolder(File file) {
        if (!file.exists()) {

        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFileFolder(f);
                }
                file.delete();
            }
        }
    }


    //  文件夾 完整路徑
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
            Log.d("-----------delFolder", "delete => " + folderPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //刪除指定文件夾下所有文件
    // 文件夾 完整路徑
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        try {

            if(tempList!=null) {

                for (int i = 0; i < tempList.length; i++) {
                    if (path.endsWith(File.separator)) {
                        temp = new File(path + tempList[i]);
                    } else {
                        temp = new File(path + File.separator + tempList[i]);
                    }
                    if (temp.isFile()) {
                        temp.delete();
                        Log.d("-----------temp.isFile", "delete => " + temp);
                    }
                    if (temp.isDirectory()) {
                        delAllFile(path + tempList[i]);//先删除文件夹里面的文件
                        delFolder(path + tempList[i]);//再删除空文件夹
                        flag = true;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    public static String readSDFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        File file = new File(fileName);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
//        makeFilePath(filePath, fileName);


        File file0;

        try {
            file0 = new File(filePath + fileName);
            if (!file0.exists()) {
                file0.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    public static String getTXTdata(String path) {
        String s = null;
        try {
            StringBuffer sb = new StringBuffer();
            File file2 = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file2));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            s = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    //文件複製
    public static void copyfile(File fromFile, File toFile, Boolean rewrite) {

        if (!fromFile.exists()) {
            return;
        }

        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }


        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);

            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            //关闭输入、输出流
            fosfrom.close();
            fosto.close();


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static String savePicToSdcard(Bitmap bitmap, String path,
                                         String fileName) {
        String filePath = "";
        if (bitmap == null) {
            return filePath;
        } else {

            filePath = path + fileName;
            File destFile = new File(filePath);
            OutputStream os = null;
            try {
                os = new FileOutputStream(destFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (IOException e) {
                filePath = "";
            }
        }
        return filePath;
    }


    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }



        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


}





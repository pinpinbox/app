package com.pinpinbox.android.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vmage on 2015/11/25.
 */
public class BitmapUtility {


    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public static Bitmap createBlurViewBitmap(View v) {

        if(v.getWidth()>0 && v.getHeight()>0) {

            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                    Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            v.draw(canvas);
            return bitmap;

        }else {

            return null;
        }

    }


    //判斷旋轉角度
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        }

        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     * @param bm 需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,int screenWidth, int screenHeight) {
        final int imageHeight = options.outHeight;
        final int imageWidth = options.outWidth;
        int inSampleSize = 1;

        if (imageHeight > screenHeight || imageWidth > screenWidth) {
            final int heightRatio = Math.round((float) imageHeight/ (float) screenHeight);
            final int widthRatio = Math.round((float) imageWidth / (float) screenWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        Log.d("BitmapUtility", "inSampleSize => " + inSampleSize);

        return inSampleSize;
    }



    public static Bitmap getSmallBitmap(String filePath, int w , int h) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565; //測試
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, w, h);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        int degree = getExifOrientation(filePath);


        Log.d("BitmapUtility", "選轉角度 => " + degree);
        Log.d("BitmapUtility", "w => " + options.outWidth);
        Log.d("BitmapUtility", "h => " + options.outHeight);

        InputStream is = null;
        try {
            is = new FileInputStream(new File(filePath));
        }catch (Exception e){
            e.printStackTrace();
        }


        return rotateBitmapByDegree(BitmapFactory.decodeStream(is, null,  options), degree);

    }

    public static Bitmap getSmallBitmap(String filePath, int w , int h, int degree) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565; //測試
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, w, h);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        InputStream is = null;
        try {
            is = new FileInputStream(new File(filePath));
        }catch (Exception e){
            e.printStackTrace();
        }


        Log.d("BitmapUtility", "選轉角度 => " + degree);
        Log.d("BitmapUtility", "w => " + options.outWidth);
        Log.d("BitmapUtility", "h => " + options.outHeight);


        return rotateBitmapByDegree(BitmapFactory.decodeStream(is, null,  options), degree);

    }

    public static Bitmap zoom(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }


    /**
     * 保存图片到本地(JPG)
     *
     * @param bm
     *            保存的图片
     * @return 图片路径
     */
    public static void saveToLocal(Bitmap bm, String newPicPath, int size) {
        FileOutputStream fileOutputStream = null;
        File f = new File(newPicPath);
        if (!f.exists()) {
            try {


                f.createNewFile();

                Log.d("BitmapUtility", "newPicPath => " +  newPicPath + " => 已建立");

                fileOutputStream = new FileOutputStream(newPicPath);
                bm.compress(Bitmap.CompressFormat.JPEG, size, fileOutputStream);


                fileOutputStream.flush();
                fileOutputStream.close();


            } catch (IOException e) {

                e.printStackTrace();

            }
        }else {

            Log.d("BitmapUtility", "newPicPath => 已存在");

        }
    }

    private static Bitmap bitMatrix(Bitmap bitmap, float x, float y) {
        Matrix matrix = new Matrix();
        matrix.postScale(x, y); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }



    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}

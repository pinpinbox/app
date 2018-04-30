package com.pinpinbox.android.SampleTest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.pinpinbox.android.R;

//import com.soundcloud.android.crop.Crop;

/**
 * Created by vmage on 2016/8/18.
 */
public class FragmentPhotoCrop extends Fragment {

    private Context context;

    private ImageView isCropImg;
    private Button btCrop;

    private int ResultOK = -1;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crop, container, false);

        isCropImg = (ImageView)v.findViewById(R.id.isCropImg);
        btCrop = (Button)v.findViewById(R.id.btCrop);


        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity().getApplicationContext();

        btCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCropImg.setImageDrawable(null);
//                Crop.pickImage(context,FragmentPhotoCrop.this);


            }
        });


    }
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTO_RESOULT = 3;// 结果
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent result) {
//        if (requestCode == Crop.REQUEST_PICK && resultCode == ResultOK) {
//
//            MyLog.Set("d", getClass(), "requestCode == Crop.REQUEST_PICK && resultCode == ResultOK");
//
//            Intent intent = new Intent("com.android.camera.action.CROP");
//            intent.setDataAndType(result.getData(), IMAGE_UNSPECIFIED);
//            intent.putExtra("crop", "true");//进行修剪
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("outputX", 300);
//            intent.putExtra("outputY", 300);
//            intent.putExtra("return-data", true);
//            startActivityForResult(intent, PHOTO_RESOULT);
//
////            beginCrop(result.getData());
//        } else if (requestCode == Crop.REQUEST_CROP) {
//
//            MyLog.Set("d", getClass(), "requestCode == Crop.REQUEST_CROP");
//
//            handleCrop(resultCode, result);
//
//
//
//        }else if(requestCode == PHOTO_RESOULT){
//
//            MyLog.Set("d", getClass(), "requestCode == PHOTO_RESOULT");
//
//            Bundle extras = result.getExtras();
//
//            if (extras != null) {
//                 Bitmap bitmap = extras.getParcelable("data");
//                isCropImg.setImageBitmap(bitmap);
//            }
//
//
//
//        }
//
//
//        super.onActivityResult(requestCode, resultCode, result);
//    }
//
//    private void beginCrop(Uri source) {
//        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
//        Crop.of(source, destination).asSquare().start(context, FragmentPhotoCrop.this);
//    }

//    private void handleCrop(int resultCode, Intent result) {
//        if (resultCode == ResultOK) {
//
//            Uri u = Crop.getOutput(result);
//
//            String path = FileUtility.getImageAbsolutePath(getActivity(), u);
//
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            opts.inPreferredConfig = Bitmap.Config.RGB_565;
//
//
//            Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
//
//            int rotate = BitmapUtility.getExifOrientation(path);
//
//            bitmap = BitmapUtility.rotateBitmapByDegree(bitmap, rotate);
//
//            isCropImg.setImageBitmap(bitmap);
//
//
////            isCropImg.setImageURI(Crop.getOutput(result));
//        } else if (resultCode == Crop.RESULT_ERROR) {
//            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }


}

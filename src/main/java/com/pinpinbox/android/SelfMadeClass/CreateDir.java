package com.pinpinbox.android.SelfMadeClass;

import com.pinpinbox.android.StringClass.DirClass;
import com.pinpinbox.android.Utility.FileUtility;

/**
 * Created by vmage on 2017/6/7.
 */
public class CreateDir {

    public static void create(){

        String strMyDir = PPBApplication.getInstance().getMyDir();

        FileUtility fu = new FileUtility();//建立資料夾
        fu.createSDDir(strMyDir);
        fu.createDirIn(strMyDir, DirClass.dirAlbumList);
        fu.createDirIn(strMyDir, DirClass.dirZip);
        fu.createDirIn(strMyDir, DirClass.dirCopy);

    }


}

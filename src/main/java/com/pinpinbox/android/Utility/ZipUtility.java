package com.pinpinbox.android.Utility;

import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by vmage on 2014/12/10
 */
public class ZipUtility {

    // 解壓縮方法, 第一個參數為zip文件完整路徑 第二個為解壓縮後 存放的文件夾
    public static void Unzip(String zipFile, String targetDir) {
        int BUFFER = 4096; // 這裡緩衝區 使用4KB
        String strEntry; // 保存每個zip的條目名稱
        String visableEntry;

        try {
            BufferedOutputStream dest = null; // 緩衝輸出流
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(fis));
            ZipEntry entry; // 每個zip條目的實例
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();
                    visableEntry = strEntry + "#asdf";

//                    File entryFile = new File(targetDir + strEntry);
                    File entryFile = new File(targetDir + visableEntry);

                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }

                    dest.flush();
                    dest.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();

            MyLog.Set("d", ZipUtility.class, "解壓縮完成");

        } catch (Exception cwj) {
            MyLog.Set("d", ZipUtility.class, "解壓縮出錯");
            cwj.printStackTrace();
        }
    }

    public static void zip(String path,String[] files,String _zipFile) {
        int BUFFER = 2048;
        try  {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(_zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for(int i=0; i < files.length; i++) {
                String p = path + files[i];
                FileInputStream fi = new FileInputStream(p);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(p.substring(p.lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }



}

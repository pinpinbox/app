package com.pinpinbox.android.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by kevin9594 on 2016/12/23.
 */
public class FormFile {

    /* 上传文件的数据 */
    private byte[] data;
    private InputStream inStream;
    private File file;
    /* 文件名称 */
    private String filname;
    /* 请求参数名称*/
    private String parameterName;
    /* 内容类型 */
    private String contentType = "application/octet-stream";

//文件比较小的时候用这个方法,这时候就是直接把内存中的数据拿出来,放到磁盘上
    public FormFile(String filname, byte[] data, String parameterName, String

            contentType) {
        this.data = data;
        this.filname = filname;
        this.parameterName = parameterName;
        if(contentType!=null) this.contentType = contentType;
    }
//要求传入文件的对象,实现一边读文件,一边上传写到磁盘上,这样不会占用太多内存,
    //上传大文件的时候,一定要用这个方式.
    public FormFile(File file, String parameterName, String contentType) {
        this.filname = file.getName();
        this.parameterName = parameterName;
        this.file = file;
        try {
            this.inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(contentType!=null) this.contentType = contentType;
    }

    public File getFile() {
        return file;
    }

    public InputStream getInStream() {
        return inStream;
    }

    public byte[] getData() {
        return data;
    }

    public String getFilname() {
        return filname;
    }

    public void setFilname(String filname) {
        this.filname = filname;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}



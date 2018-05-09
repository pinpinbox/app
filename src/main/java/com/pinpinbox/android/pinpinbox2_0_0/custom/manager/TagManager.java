package com.pinpinbox.android.pinpinbox2_0_0.custom.manager;

import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemTagUser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vmage on 2018/3/27.
 */

public class TagManager {


    /*數字判斷*/
    private static Pattern pNumeral = Pattern.compile("^[-+]?[0-9]");

    /*分斷判斷*/
    private static final String sectionReg = "\\[";

    private String message;

    private List<ItemTagUser> itemTagUserList;

    public TagManager(String message){
        this.message = message;
        returnMessageToShow(message);
    }


    private List<ItemTagUser> getTagList(String message) {

        List<ItemTagUser> itemTagUserList = new ArrayList<>();

        /*已 [ 分割字段*/
        Pattern p = Pattern.compile(sectionReg);
        String parts[] = p.split(message);

        int checkIndex = 0;


        for (int i = 0; i < parts.length; i++) {
            if (parts[i].length() > 0) {

//                MyLog.Set("e", TagManager.class, "parts[i] => " + parts[i]);

                if(!parts[i].contains("]")){
//                    MyLog.Set("e", TagManager.class, "無字元 ] 循環繼續");
                    continue;
                }

                String part = parts[i].substring(0, parts[i].indexOf("]"));

                String id = part.substring(0, part.indexOf(":"));
                String name = part.substring(part.indexOf(":") + 1, part.length());

                /*條件強制 : 前必須為數字*/
                Matcher matcher = pNumeral.matcher(id);
                if (matcher.find()) {

                    /*建立Tag物件*/
                    ItemTagUser tagUser = new ItemTagUser();
                    tagUser.setName(name);
                    tagUser.setUser_id(id);

                    /*組合送出字串*/
                    String send = "[" + part + "]";
                    tagUser.setSendType(send);

                    /*設置字段位置*/
                    Pattern pIndex = Pattern.compile("\\[" + part + "\\]");
                    Matcher mIndex = pIndex.matcher(message);
                    if (mIndex.find(checkIndex)) {
                        tagUser.setStartIndex(mIndex.start());
                        tagUser.setEndIndex(mIndex.end());

                        checkIndex = mIndex.end();
                    }


                    itemTagUserList.add(tagUser);

                }


            }
        }


        return itemTagUserList;
    }

    private void returnMessageToShow(String message) {

         itemTagUserList = getTagList(message);

        int len = 0;

        /*格式轉化*/
        for (int i = 0; i < itemTagUserList.size(); i++) {

            message = message.replace(itemTagUserList.get(i).getSendType(), itemTagUserList.get(i).getName());
//            MyLog.Set("e", TagManager.class, "message => " + message);

             /*位置*/
            itemTagUserList.get(i).setStartIndex(itemTagUserList.get(i).getStartIndex() - len);

            len = len + (itemTagUserList.get(i).getUser_id().length() + 3);//兩個引號 + 冒號

            itemTagUserList.get(i).setEndIndex(itemTagUserList.get(i).getEndIndex() - len);


//            MyLog.Set("e", TagManager.class, "*****start => " + itemTagUserList.get(i).getStartIndex());
//            MyLog.Set("e", TagManager.class, "*****end => " + itemTagUserList.get(i).getEndIndex());

//            /*位置*/
//            Pattern pattern = Pattern.compile(itemTagUserList.get(i).getName());
//            Matcher matcher = pattern.matcher(message);
//            if (matcher.find()) {
//                itemTagUserList.get(i).setStartIndex(matcher.start());
//                itemTagUserList.get(i).setEndIndex(matcher.end());
//                MyLog.Set("d", TagManager.class, "startIndex => " + itemTagUserList.get(i).getStartIndex());
//                MyLog.Set("d", TagManager.class, "endIndex => " + itemTagUserList.get(i).getEndIndex());
//            }

        }

//        MyLog.Set("e", TagManager.class, "finish message => " + message);
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public List<ItemTagUser> getItemTagUserList() {
        return itemTagUserList;
    }
}

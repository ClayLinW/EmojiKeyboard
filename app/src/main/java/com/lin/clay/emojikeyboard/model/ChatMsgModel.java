package com.lin.clay.emojikeyboard.model;

public class ChatMsgModel {
    public static String TEXT_MSG_TYPE = "TEXT_MSG";
    public static String GIFT_MSG_TYPE = "GIFT_MSG";
    public String msgType = TEXT_MSG_TYPE;
    public String text;
    public String giftTitle;
    public int giftIconId;
    public String strSvga;
    public boolean isSend = true;
}

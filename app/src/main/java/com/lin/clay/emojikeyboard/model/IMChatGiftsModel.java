package com.lin.clay.emojikeyboard.model;

import java.io.Serializable;
import java.util.ArrayList;

public class IMChatGiftsModel {

    public int id;
    public int iconId;
    public ArrayList<GiftDetail> items;

    public static class GiftDetail {
        public String id;
        public String title;
        public int coverImgId;
        public String scene;
        public int price;
        public String strSvga;
    }
}

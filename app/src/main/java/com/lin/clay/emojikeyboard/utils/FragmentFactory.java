package com.lin.clay.emojikeyboard.utils;

import android.os.Bundle;

import com.lin.clay.emojikeyboard.fragment.EmotionFactoryFragment;
import com.lin.clay.emojikeyboard.fragment.GiftsFactoryFragment;
import com.lin.clay.emojikeyboard.model.IMChatGiftsModel;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class FragmentFactory {

    public static final String EMOTION_MAP_TYPE = "EMOTION_MAP_TYPE";
    public static final String ITEM_WIDTH_HEIGHT = "ITEM_WIDTH_HEIGHT";
    public static final String EMOTION_COLUMNS = "EMOTION_COLUMNS";
    public static final String EMOTION_TYPE = "EMOTION_TYPE";
    private static FragmentFactory factory;

    private FragmentFactory() {

    }

    /**
     * 双重检查锁定，获取工厂单例对象
     *
     * @return
     */
    public static FragmentFactory getSingleFactoryInstance() {
        if (factory == null) {
            synchronized (FragmentFactory.class) {
                if (factory == null) {
                    factory = new FragmentFactory();
                }
            }
        }
        return factory;
    }

    /**
     * 根据需求获取表情fragment的方法
     *
     * @param emotionType 表情类型，用于判断使用哪个map集合的表情
     * @param itemSize    表情大小
     * @param columns     表情列数
     * @param type        表情类型，0:表示经典表情  1:表示其它表情
     * @return
     */
    public Fragment getEmotiomFragment(int emotionType, int itemSize, int columns, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(FragmentFactory.EMOTION_MAP_TYPE, emotionType);
        bundle.putInt(FragmentFactory.ITEM_WIDTH_HEIGHT, itemSize);
        bundle.putInt(FragmentFactory.EMOTION_COLUMNS, columns);
        bundle.putInt(FragmentFactory.EMOTION_TYPE, type);

        EmotionFactoryFragment emotionFactoryFragment = EmotionFactoryFragment.newInstance(EmotionFactoryFragment.class, bundle);
        return emotionFactoryFragment;
    }

    /**
     * 根据需求获取礼物fragment的方法
     *
     * @return
     * @param data
     */
    public Fragment getGiftsFragment(ArrayList<IMChatGiftsModel.GiftDetail> data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("imchatgifts",data);
        Fragment giftsFactoryFragment = GiftsFactoryFragment.newInstance(GiftsFactoryFragment.class, bundle);
        return giftsFactoryFragment;
    }

}

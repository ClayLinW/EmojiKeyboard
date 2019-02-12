package com.lin.clay.emojikeyboard.fragment;

import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lin.clay.emojikeyboard.utils.GlobalOnItemClickManagerUtils;
import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.adapter.EmotionFactoryAdapter;
import com.lin.clay.emojikeyboard.utils.EmotionManager;
import com.lin.clay.emojikeyboard.utils.FragmentFactory;
import com.lin.clay.emojikeyboard.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EmotionFactoryFragment extends BaseFragment {

    private RecyclerView rvEmotion;
    private int emotion_map_type;
    private int item_width_height;
    private int emotion_columns;
    private int emotion_type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View factoryEmotionView = inflater.inflate(R.layout.factory_emotion_gifts_fragment, container, false);
        rvEmotion = factoryEmotionView.findViewById(R.id.rv_emotionGifts);
        emotion_map_type = args.getInt(FragmentFactory.EMOTION_MAP_TYPE);
        item_width_height = args.getInt(FragmentFactory.ITEM_WIDTH_HEIGHT);
        emotion_columns = args.getInt(FragmentFactory.EMOTION_COLUMNS);
        emotion_type = args.getInt(FragmentFactory.EMOTION_TYPE);
        setRVPaddingValue();
        setEmotionDatas();
        return factoryEmotionView;
    }

    /**
     * 计算recyclerview的需要设置的左右padding值
     */
    private void setRVPaddingValue() {
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int screenW = Utils.Px2Dp(getActivity(), width);
        int itemWH = Utils.Px2Dp(getActivity(), item_width_height);
        int padding = Utils.Px2Dp(getActivity(), 54);
        int paddingReal = padding - (screenW - padding * 2 - emotion_columns * itemWH) / (2 * emotion_columns - 2);
        rvEmotion.setPadding(Utils.Dp2Px(getActivity(), paddingReal), 0, Utils.Dp2Px(getActivity(), paddingReal), 0);
    }

    private void setEmotionDatas() {
        List<String> emotionNames = new ArrayList<>();
        Collections.addAll(emotionNames, EmotionManager.getInstance().EMOJI_TEXT_ARRAY);
        EmotionFactoryAdapter emotionFactoryAdapter = new EmotionFactoryAdapter(getActivity(), emotionNames, emotion_map_type, item_width_height, emotion_columns, emotion_type);
        rvEmotion.setLayoutManager(new GridLayoutManager(getActivity(), emotion_columns, RecyclerView.VERTICAL, false));
        rvEmotion.setAdapter(emotionFactoryAdapter);
        if (emotion_type == 0) {
            emotionFactoryAdapter.setOnEmotionClickItemListener(GlobalOnItemClickManagerUtils.getInstance(getActivity()).getOnClassicEmotionClickItemListener(emotion_map_type));
        } else if (emotion_type == 1) {
            emotionFactoryAdapter.setOnEmotionClickItemListener(GlobalOnItemClickManagerUtils.getInstance(getActivity()).getOnOtherEmotionClickItemListener(emotion_map_type));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}


package com.lin.clay.emojikeyboard.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lin.clay.emojikeyboard.adapter.EmotionGiftsFragmentAdapter;
import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.adapter.HorizontalRVAdapter;
import com.lin.clay.emojikeyboard.model.EmotionGiftsCheckedModel;
import com.lin.clay.emojikeyboard.utils.EmotionManager;
import com.lin.clay.emojikeyboard.utils.FragmentFactory;
import com.lin.clay.emojikeyboard.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class EmotionFragment extends Fragment
{

    private ViewPager vpEmotion;
    private RecyclerView rvEmotion;

    //当前表情被选中底部tab,记录底部默认选中第一个
    private int CurrentPosition = 0;

    List<Fragment> emotionFragments = new ArrayList<>();
    private HorizontalRVAdapter rvEmotionAdapter;
    private List<EmotionGiftsCheckedModel> emotionCheckedList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_emotion_fragment, container, false);
        vpEmotion = view.findViewById(R.id.vp_emotion);
        rvEmotion = view.findViewById(R.id.rv_emotion);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setVPEmotion();
        setRVEmotion();
        setListener();
    }

    private void setVPEmotion() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();

        //经典表情
        EmotionFactoryFragment classicEmotionFragment = (EmotionFactoryFragment) factory.getEmotiomFragment(EmotionManager.getInstance().EMOTION_CLASSIC_TYPE, Utils.Dp2Px(getActivity(), 25), 7, 0);
        emotionFragments.add(classicEmotionFragment);

        //经典表情2
//        EmotionFactoryFragment otherEmotionFragment = (EmotionFactoryFragment) factory.getEmotiomFragment(EmotionUtils.EMOTION_OTHER_TYPE, Utils.Dp2Px(getActivity(), 60), 4, 1);
//        emotionFragments.add(otherEmotionFragment);

        EmotionGiftsFragmentAdapter adapter = new EmotionGiftsFragmentAdapter(getChildFragmentManager(), emotionFragments);
        vpEmotion.setAdapter(adapter);
    }

    private void setRVEmotion() {

        emotionCheckedList = new ArrayList<>();
        for (int i = 0; i < emotionFragments.size(); i++) {
            if (i == 0) {
                EmotionGiftsCheckedModel model = new EmotionGiftsCheckedModel();
                model.icon = getResources().getDrawable(R.drawable.chat_classic_emoji);
                model.flag = "经典笑脸";
                model.isSelected = true;
                model.isGift = false;
                emotionCheckedList.add(model);
            } else {
                EmotionGiftsCheckedModel model = new EmotionGiftsCheckedModel();
                model.icon = getResources().getDrawable(R.drawable.chat_classic_emoji);
                model.flag = "其它笑脸" + i;
                model.isSelected = false;
                model.isGift = false;
                emotionCheckedList.add(model);
            }
        }

        //底部tab
        rvEmotionAdapter = new HorizontalRVAdapter(getActivity(), emotionCheckedList);
        rvEmotion.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        rvEmotion.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        rvEmotion.setAdapter(rvEmotionAdapter);
    }

    private void setListener() {
        //初始化recyclerview_horizontal监听器
        rvEmotionAdapter.setOnClickItemListener(new HorizontalRVAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<EmotionGiftsCheckedModel> datas) {
                if (CurrentPosition == position)
                    return;
                //获取先前被点击tab,修改背景颜色的标记
                datas.get(CurrentPosition).isSelected = false;
                //获取当前被点击tab,修改背景颜色的标记
                datas.get(position).isSelected = true;
                //通知更新，这里我们选择性更新就行了
                rvEmotionAdapter.notifyItemChanged(position);
                rvEmotionAdapter.notifyItemChanged(CurrentPosition);
                //记录当前被选中tab下标
                CurrentPosition = position;
                //viewpager界面切换
                vpEmotion.setCurrentItem(position, false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<EmotionGiftsCheckedModel> datas) {
            }
        });

        vpEmotion.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (CurrentPosition == position)
                    return;
                //获取先前被点击tab,修改背景颜色的标记
                emotionCheckedList.get(CurrentPosition).isSelected = false;
                //获取当前被点击tab,修改背景颜色的标记
                emotionCheckedList.get(position).isSelected = true;
                //通知更新，这里我们选择性更新就行了
                rvEmotionAdapter.notifyItemChanged(position);
                rvEmotionAdapter.notifyItemChanged(CurrentPosition);
                //记录当前被选中tab下标
                CurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

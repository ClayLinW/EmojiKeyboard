package com.lin.clay.emojikeyboard.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.lin.clay.emojikeyboard.MainActivity;
import com.lin.clay.emojikeyboard.utils.listener.OnEmotionClickItemListener;
import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.adapter.GiftsFactoryAdapter;
import com.lin.clay.emojikeyboard.model.IMChatGiftsModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GiftsFactoryFragment extends BaseFragment {
    private RecyclerView rvGifts;
    private ArrayList<IMChatGiftsModel.GiftDetail> mData;

    private boolean mIsVisible = false;
    private int mCheckedItemGifts = -1;
    private GiftsFactoryAdapter mGiftsFactoryAdapter;
    private PopupWindow mChatGiftLongClickPW;
    private static Handler mHandler = new Handler();
    private int downLoadGiftPosition;
    private String downLoadGiftId;
    private String downLoadGiftUrl;
    private boolean isShowLongClickPrompt;
    private PopupWindow mGiftLongClickPromptPW;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View factoryGiftsView = inflater.inflate(R.layout.factory_emotion_gifts_fragment, container, false);
        rvGifts = factoryGiftsView.findViewById(R.id.rv_emotionGifts);
        setEmotionDatas();
        return factoryGiftsView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setEmotionDatas() {
        mData = (ArrayList<IMChatGiftsModel.GiftDetail>) args.getSerializable("imchatgifts");
        mGiftsFactoryAdapter = new GiftsFactoryAdapter(getActivity(), mData, mCheckedItemGifts);
        rvGifts.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        rvGifts.setAdapter(mGiftsFactoryAdapter);
        mGiftsFactoryAdapter.setRecyclerView(rvGifts);
        mGiftsFactoryAdapter.setOnEmotionClickItemListener(new OnEmotionClickItemListener() {
            @Override
            public void onItemClick(RecyclerView.Adapter adapter, View view, int position) {
                if (!(mCheckedItemGifts == position)) {
                    mCheckedItemGifts = position;
                    mGiftsFactoryAdapter.setCheckedItemGifts(mCheckedItemGifts);
                }
                setCheckedItemGifts();
            }

            @Override
            public void onItemLongClick(RecyclerView.Adapter adapter, View view, int position) {
                if (!(mCheckedItemGifts == position)) {
                    mCheckedItemGifts = position;
                    mGiftsFactoryAdapter.setCheckedItemGifts(mCheckedItemGifts);
                }
                setCheckedItemGifts();
                if (!(getActivity() instanceof MainActivity)) {
                    return;
                }
                IMChatGiftsModel.GiftDetail giftDetail = mData.get(position);
                String id = giftDetail.id;
                String title = giftDetail.title;
                String strSvga = giftDetail.strSvga;
                ((MainActivity) getActivity()).playFrameGift(id, title, strSvga);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mIsVisible = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    public boolean isCheckVisible() {
        return mIsVisible;
    }

    public boolean isChackedGift() {
        if (mCheckedItemGifts == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 不选中礼物
     */
    public void setCheckedItemGifts() {
        GiftsFragment parentFragment = (GiftsFragment) getParentFragment();
        parentFragment.setNeedClick();
        List<Fragment> fragments = getFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                GiftsFactoryFragment fragment = (GiftsFactoryFragment) fragments.get(i);
                if (!fragment.isCheckVisible()) {
                    fragment.mCheckedItemGifts = -1;
                    fragment.mGiftsFactoryAdapter.setCheckedItemGifts(fragment.mCheckedItemGifts);
                }
            }
        }
    }


    /**
     * 选中的礼物
     *
     * @return
     */
    public IMChatGiftsModel.GiftDetail getCheckGifts() {
        if (mData.size() > 0 && mData.size() > mCheckedItemGifts) {
            IMChatGiftsModel.GiftDetail giftDetail = mData.get(mCheckedItemGifts);
            return giftDetail;
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGiftsFactoryAdapter.releaseAnimation();
    }
}

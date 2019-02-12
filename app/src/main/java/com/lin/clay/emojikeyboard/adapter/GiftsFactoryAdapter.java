package com.lin.clay.emojikeyboard.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lin.clay.emojikeyboard.utils.listener.OnEmotionClickItemListener;
import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.model.IMChatGiftsModel;
import com.lin.clay.emojikeyboard.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GiftsFactoryAdapter extends RecyclerView.Adapter<GiftsFactoryAdapter.GiftsViewHolder> {

    private Context mContext;
    private ArrayList<IMChatGiftsModel.GiftDetail> mData;
    private int mCheckedItemGifts;
    private final LayoutInflater mLayoutInflater;
    private RecyclerView rvGifts;

    public GiftsFactoryAdapter(Context context, ArrayList<IMChatGiftsModel.GiftDetail> mData, int checkedItemGifts) {
        this.mContext = context;
        this.mData = mData;
        this.mCheckedItemGifts = checkedItemGifts;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public GiftsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.factory_gifts, parent, false);
        GiftsViewHolder giftsViewHolder = new GiftsViewHolder(view);
        return giftsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GiftsViewHolder holder, final int position) {
        if (position < 3) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.llGifts.getLayoutParams();
            layoutParams.topMargin = Utils.Dp2Px(mContext, 10);
            layoutParams.bottomMargin = 0;
            holder.llGifts.setLayoutParams(layoutParams);
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.llGifts.getLayoutParams();
            layoutParams.topMargin = Utils.Dp2Px(mContext, 4);
            layoutParams.bottomMargin = 0;
            int bottomCount = getItemCount() % 3;
            if (position >= getItemCount() - (bottomCount == 0 ? 3 : bottomCount)) {
                layoutParams.bottomMargin = Utils.Dp2Px(mContext, 10);
            }
            holder.llGifts.setLayoutParams(layoutParams);
        }

        if (position == mCheckedItemGifts) {
            holder.llGifts.setSelected(true);
            holder.llGifts.clearAnimation();
            holder.imgGifts.setScaleY(1);
            holder.imgGifts.setScaleX(1);

            Collection<AnimatorSet> values = animatorSetMap.values();
            for (AnimatorSet value : values) {
                value.cancel();
                value = null;
            }
            animatorSetMap.clear();

            AnimatorSet animatorSet = addAnimatorSet(position, holder.imgGifts);
            animatorSet.start();
        } else {
            holder.llGifts.setSelected(false);
            holder.llGifts.clearAnimation();
            holder.imgGifts.setScaleY(1);
            holder.imgGifts.setScaleX(1);

            GiftsViewHolder giftsViewHolder = (GiftsViewHolder) rvGifts.findViewHolderForLayoutPosition(mCheckedItemGifts);
            if (giftsViewHolder == null && Math.abs(position - mCheckedItemGifts) > 3) {
                AnimatorSet animatorySet = getAnimatorySet(mCheckedItemGifts);
                if (animatorySet != null) {
                    animatorSetMap.remove(new Integer(mCheckedItemGifts));
                    animatorySet.cancel();
                    animatorySet = null;
                }
            }
        }

        IMChatGiftsModel.GiftDetail giftDetail = mData.get(position);
        int price = giftDetail.price;
        String title = giftDetail.title;
        String scene = giftDetail.scene;
        int coverImgId = giftDetail.coverImgId;

        holder.tvFlowerConsume.setText("￥" + price);
        holder.tvGiftDescription.setText(title);
        holder.tvGiftsType.setText(scene);
        holder.imgGifts.setImageResource(coverImgId);
        holder.pbWaitLoad.setVisibility(View.GONE);

        if (mOnEmotionClickItemListener != null) {
            holder.llGifts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnEmotionClickItemListener.onItemClick(GiftsFactoryAdapter.this, view, position);
                }
            });
        }

        if (mOnEmotionClickItemListener != null) {
            holder.llGifts.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnEmotionClickItemListener.onItemLongClick(GiftsFactoryAdapter.this, view, position);
                    return false;
                }
            });
        }
    }

    private OnEmotionClickItemListener mOnEmotionClickItemListener;

    public void setOnEmotionClickItemListener(OnEmotionClickItemListener mOnEmotionClickItemListener) {
        this.mOnEmotionClickItemListener = mOnEmotionClickItemListener;
    }

    public void setRecyclerView(RecyclerView rvGifts) {
        this.rvGifts = rvGifts;
    }

    public interface OnGiftLongClickPromptListener {
        void giftLongClickPrompt(View view, int position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class GiftsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llGifts;
        ImageView imgGifts;
        ProgressBar pbWaitLoad;
        TextView tvGiftDescription;
        TextView tvGiftsType;
        TextView tvFlowerConsume;

        public GiftsViewHolder(View itemView) {
            super(itemView);
            llGifts = itemView.findViewById(R.id.ll_gifts);
            imgGifts = itemView.findViewById(R.id.img_gifts);
            pbWaitLoad = itemView.findViewById(R.id.pb_waitLoad);
            tvGiftDescription = itemView.findViewById(R.id.tv_giftDescription);
            tvGiftsType = itemView.findViewById(R.id.tv_giftsType);
            tvFlowerConsume = itemView.findViewById(R.id.tv_flowerConsume);
        }
    }

    public void setCheckedItemGifts(int mCheckedItemGifts) {
        this.mCheckedItemGifts = mCheckedItemGifts;
        Collection<AnimatorSet> values = animatorSetMap.values();
        for (AnimatorSet value : values) {
            value.cancel();
            value = null;
        }
        animatorSetMap.clear();
        for (int i = 0; i < mData.size(); i++) {
            GiftsViewHolder giftsViewHolder = (GiftsViewHolder) rvGifts.findViewHolderForLayoutPosition(i);
            if (giftsViewHolder != null) {
                giftsViewHolder.imgGifts.clearAnimation();
                giftsViewHolder.imgGifts.setScaleX(1);
                giftsViewHolder.imgGifts.setScaleY(1);

                if (i == mCheckedItemGifts) {
                    giftsViewHolder.llGifts.setSelected(true);
                    giftsViewHolder.llGifts.startAnimation(getmGiftGlobalScaleAnimation());

                    AnimatorSet animatorSet = addAnimatorSet(i, giftsViewHolder.imgGifts);
                    animatorSet.start();
                } else {
                    giftsViewHolder.llGifts.setSelected(false);
                    giftsViewHolder.llGifts.clearAnimation();

                    AnimatorSet animatorySet = getAnimatorySet(i);
                    if (animatorySet != null) {
                        animatorSetMap.remove(new Integer(i));
                        animatorySet.cancel();
                        animatorySet = null;
                        giftsViewHolder.imgGifts.setScaleX(1);
                        giftsViewHolder.imgGifts.setScaleY(1);
                    }

                }
            } else {
                notifyItemChanged(i);
            }
        }
    }

    private Map<Integer, AnimatorSet> animatorSetMap = new HashMap<>();

    private AnimatorSet addAnimatorSet(int position, View view) {
        if (!animatorSetMap.containsKey(new Integer(position))) {
            AnimatorSet set = new AnimatorSet();
            final ObjectAnimator oa3 = ObjectAnimator.ofFloat(view, "scaleX", 1, 0.7f);
            oa3.setDuration(800);
            oa3.setRepeatCount(-1);
            oa3.setRepeatMode(ValueAnimator.REVERSE);

            final ObjectAnimator oa4 = ObjectAnimator.ofFloat(view, "scaleY", 1, 0.7f);
            oa4.setDuration(800);
            oa4.setRepeatCount(-1);
            oa4.setRepeatMode(ValueAnimator.REVERSE);
            //设置一起飞
            set.playTogether(oa3, oa4);
            set.setStartDelay(300);
            animatorSetMap.put(new Integer(position), set);
            return set;
        } else {
            return animatorSetMap.get(new Integer(position));
        }
    }

    private AnimatorSet getAnimatorySet(int position) {
        if (animatorSetMap.containsKey(new Integer(position))) {
            return animatorSetMap.get(new Integer(position));
        }
        return null;
    }

    private ScaleAnimation mGlobalGiftScaleAnimation;

    private ScaleAnimation getmGiftGlobalScaleAnimation() {
        if (mGlobalGiftScaleAnimation == null) {
            mGlobalGiftScaleAnimation = new ScaleAnimation(1, 0.9f, 1, 0.9f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mGlobalGiftScaleAnimation.setDuration(250);
            mGlobalGiftScaleAnimation.setRepeatCount(1);
            mGlobalGiftScaleAnimation.setRepeatMode(Animation.REVERSE);
        }
        return mGlobalGiftScaleAnimation;
    }

    private ScaleAnimation mThumbGiftScaleAnimation;

    private ScaleAnimation getmThumbGiftScaleAnimation() {
        if (mThumbGiftScaleAnimation == null) {
            mThumbGiftScaleAnimation = new ScaleAnimation(1, 0.7f, 1, 0.7f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mThumbGiftScaleAnimation.setDuration(800);
            mThumbGiftScaleAnimation.setStartOffset(250);
            mThumbGiftScaleAnimation.setRepeatMode(Animation.REVERSE);
            mThumbGiftScaleAnimation.setRepeatCount(Integer.MAX_VALUE);
        }
        return mThumbGiftScaleAnimation;
    }

    public void releaseAnimation() {
        if (mGlobalGiftScaleAnimation != null) {
            mGlobalGiftScaleAnimation.cancel();
            mGlobalGiftScaleAnimation.reset();
            mGlobalGiftScaleAnimation = null;
        }

        if (mThumbGiftScaleAnimation != null) {
            mThumbGiftScaleAnimation.cancel();
            mThumbGiftScaleAnimation.reset();
            mThumbGiftScaleAnimation = null;
        }

        for (int i = 0; i < animatorSetMap.size(); i++) {
            Collection<AnimatorSet> values = animatorSetMap.values();
            for (AnimatorSet value : values) {
                if (value != null) {
                    value.cancel();
                    value = null;
                }
            }
        }
    }
}

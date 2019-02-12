package com.lin.clay.emojikeyboard.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lin.clay.emojikeyboard.MainActivity;
import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.utils.listener.SoftKeyBoardListener;
import com.lin.clay.emojikeyboard.utils.widget.NoHorizontalScrollerViewPager;

public class EmotionKeyboard {

    private static final String SHARE_PREFERENCE_NAME = "EmotionKeyboard";
    private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";

    private Activity mActivity;
    private InputMethodManager mInputManager;//软键盘管理类
    private SharedPreferences sp;
    private View mEmotionLayout;//表情布局
    private EditText mEditText;//
    private View mContentView;//内容布局view,即除了表情布局或者软键盘布局以外的布局，用于固定bar的高度，防止跳闪

    private int screenHeight;       //用来处理一些隐藏的导航栏导致表情布局弹出高度不一致
    private int rootViewHeight;
    private int virtualBarHeigh;

    private ImageView emojiIcon;

    private EmotionKeyboard() {

    }

    /**
     * 外部静态调用
     *
     * @param activity
     * @return
     */
    public static EmotionKeyboard with(Activity activity) {
        EmotionKeyboard emotionInputDetector = new EmotionKeyboard();
        emotionInputDetector.mActivity = activity;
        emotionInputDetector.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        emotionInputDetector.sp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return emotionInputDetector;
    }

    /**
     * 绑定内容view，此view用于固定bar的高度，防止跳闪
     *
     * @param contentView
     * @return
     */
    public EmotionKeyboard bindToContent(View contentView) {
        mContentView = contentView;
        return this;
    }

    /**
     * 绑定编辑框
     *
     * @param editText
     * @return
     */
    public EmotionKeyboard bindToEditText(EditText editText) {
        mEditText = editText;
        mEditText.requestFocus();
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rootViewHeight == 0) {
                    return true;
                }
                emojiIcon.setImageResource(R.drawable.chat_page_emoji);
                if (event.getAction() == MotionEvent.ACTION_UP && mEmotionLayout.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideEmotionLayout(true);//隐藏表情布局，显示软件盘
                    //软件盘显示后，释放内容高度
                    unlockContentHeightDelayed();
                    //	                    mEditText.postDelayed(new Runnable() {
                    //	                        @Override
                    //	                        public void run() {
                    //	                            unlockContentHeightDelayed();
                    //	                        }
                    //	                    }, 200L);
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                    ((MainActivity) mActivity).chatListScroolDelay(200);

                return false;
            }
        });
        return this;
    }

    public void setGiveGiftThanks() {
        if (mEmotionLayout.isShown()) {
            lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
            hideEmotionLayout(true);//隐藏表情布局，显示软件盘
            //软件盘显示后，释放内容高度
            unlockContentHeightDelayed();
        }
    }

    private View mChatControlView;

    public EmotionKeyboard bindToChatControl(View linearLayout) {
        this.mChatControlView = linearLayout;
        return this;
    }

    private void setChatControlViewParams(int marginButton) {
        // TODO: 2018/8/14  适应首页动画，改变了6.0以下的状态栏，解除限制
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mChatControlView.getLayoutParams();
        layoutParams.bottomMargin = marginButton;
        mChatControlView.setLayoutParams(layoutParams);
        //}
    }

    public EmotionKeyboard bindKeyboardListener() {
        checkHasHintNavigationBar();
        virtualBarHeigh = Utils.getVirtualBarHeigh(mActivity);
        // TODO: 2018/8/14  适应首页动画，改变了6.0以下的状态栏，解除限制
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        SoftKeyBoardListener.setListener(mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, height).apply();
                if (virtualBarHeigh != 0 && screenHeight != rootViewHeight) {
                    height = height - virtualBarHeigh;
                }
                setChatControlViewParams(height);
                ((MainActivity) mActivity).chatListScroolDelay(200);
            }

            @Override
            public void keyBoardHide(int height) {
                if (!mEmotionLayout.isShown()) {
                    setChatControlViewParams(0);
                }
            }
        });
        //}
        return this;
    }

    private void checkHasHintNavigationBar() {
        if (screenHeight == 0 || rootViewHeight == 0) {
            screenHeight = Utils.getScreenH();
            if (screenHeight <= 0) {
                WindowManager manager = mActivity.getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
                screenHeight = outMetrics.heightPixels;
            }
            final View rootView = mActivity.findViewById(android.R.id.content);
            rootViewHeight = rootView.getHeight();
            if (rootViewHeight <= 0) {
                rootView.post(new Runnable() {
                    @Override
                    public void run() {
                        rootViewHeight = rootView.getHeight();
                    }
                });
            }
        }
    }

    /**
     * 绑定表情按钮
     *
     * @param emotionButton
     * @return
     */
    public EmotionKeyboard bindToEmotionButton(final View emotionButton) {
        emojiIcon = (ImageView) emotionButton;
        emotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiIcon.setImageResource(R.drawable.chat_page_keyboard);
                if (mEmotionLayout.isShown()) {
                    if (vpEmotion.getCurrentItem() == 1) {
                        vpEmotion.setCurrentItem(0);
                        return;
                    }
                    if (vpEmotion.getCurrentItem() == 2) {
                        vpEmotion.setCurrentItem(0);
                        return;
                    }
                    if (vpEmotion.getCurrentItem() == 0) {
                        emojiIcon.setImageResource(R.drawable.chat_page_emoji);
                        //TODO: 2018/5/23 表情已经弹出，再点击时不变化，不让其弹出软键盘
                        lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                        hideEmotionLayout(true);//隐藏表情布局，显示软件盘
                        unlockContentHeightDelayed();//软件盘显示后，释放内容 高度
                        return;
                    }
                } else {
                    vpEmotion.setCurrentItem(0);
                    if (isSoftInputShown()) {//同上
                        setChatControlViewParams(0);
                        lockContentHeight();
                        showEmotionLayout();
                        unlockContentHeightDelayed();
                    } else {
                        showEmotionLayout();//两者都没显示，直接显示表情布局
                    }
                }
                ((MainActivity) mActivity).chatListScroolDelay(200);
            }
        });
        return this;
    }

    /**
     * 绑定礼物按钮
     *
     * @param giftsButton
     * @return
     */
    public EmotionKeyboard bindToGiftsButton(View giftsButton) {
        giftsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiIcon.setImageResource(R.drawable.chat_page_emoji);
                if (mEmotionLayout.isShown()) {
                    if (vpEmotion.getCurrentItem() != 1) {
                        vpEmotion.setCurrentItem(1);
                        return;
                    }
                    //                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    //                    hideEmotionLayout(true);//隐藏表情布局，显示软件盘
                    //                    unlockContentHeightDelayed();//软件盘显示后，释放内容 高度
                } else {
                    vpEmotion.setCurrentItem(1);
                    if (isSoftInputShown()) {//同上
                        setChatControlViewParams(0);
                        lockContentHeight();
                        showEmotionLayout();
                        unlockContentHeightDelayed();
                    } else {
                        showEmotionLayout();//两者都没显示，直接显示表情布局
                    }
                }
                ((MainActivity) mActivity).chatListScroolDelay(200);
            }
        });
        return this;
    }

    /**
     * 绑定一键发送按钮
     *
     * @param oneKeySendButton
     * @return
     */
    public EmotionKeyboard bindToOneKeySendButton(View oneKeySendButton) {
        oneKeySendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiIcon.setImageResource(R.drawable.chat_page_emoji);
                if (mEmotionLayout.isShown()) {
                    if (vpEmotion.getCurrentItem() != 2) {
                        vpEmotion.setCurrentItem(2);
                        return;
                    }
                    if (vpEmotion.getCurrentItem() == 2) {
                        lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                        hideEmotionLayout(true);//隐藏表情布局，显示软件盘
                        unlockContentHeightDelayed();//软件盘显示后，释放内容 高度
                        return;
                    }
                } else {
                    vpEmotion.setCurrentItem(2);
                    if (isSoftInputShown()) {//同上
                        setChatControlViewParams(0);
                        lockContentHeight();
                        showEmotionLayout();
                        unlockContentHeightDelayed();
                    } else {
                        showEmotionLayout();//两者都没显示，直接显示表情布局
                    }
                }
                ((MainActivity) mActivity).chatListScroolDelay(200);
            }
        });
        return this;
    }


    private NoHorizontalScrollerViewPager vpEmotion;

    /**
     * 设置表情与礼物选择显示布局
     *
     * @param vpEmotion
     * @return
     */
    public EmotionKeyboard setEmotionGiftsView(NoHorizontalScrollerViewPager vpEmotion) {
        this.vpEmotion = vpEmotion;
        return this;
    }

    /**
     * 设置表情内容布局
     *
     * @param emotionView
     * @return
     */
    public EmotionKeyboard setEmotionView(View emotionView) {
        mEmotionLayout = emotionView;
        return this;
    }

    public EmotionKeyboard build() {
        //设置软件盘的模式：SOFT_INPUT_ADJUST_RESIZE  这个属性表示Activity的主窗口总是会被调整大小，从而保证软键盘显示空间。
        //从而方便我们计算软件盘的高度
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //隐藏软件盘
        hideSoftInput();
        return this;
    }

    /**
     * 点击返回键时先隐藏表情布局
     *
     * @return
     */
    public boolean interceptBackPress() {
        if (mEmotionLayout.isShown()) {
            if (vpEmotion.getCurrentItem() == 0) {
                emojiIcon.setImageResource(R.drawable.chat_page_emoji);
            }
            hideEmotionLayout(false);
            return true;
        }
        return false;
    }

    /**
     * 显示表情礼物mEmotionLayout，并设定高度等于键盘的高度
     */
    private void showEmotionLayout() {
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = getKeyBoardHeight();
        }
        softInputHeight = getKeyBoardHeight();
        hideSoftInput();
        mEmotionLayout.getLayoutParams().height = softInputHeight;
        mEmotionLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    private void hideEmotionLayout(boolean showSoftInput) {
        if (mEmotionLayout.isShown()) {
            mEmotionLayout.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    private void unlockContentHeightDelayed() {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private void showSoftInput() {
        mEditText.requestFocus();
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(mEditText, 0);
            }
        });
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 是否显示软件盘
     *
     * @return
     */
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }

        if (softInputHeight < 0) {
            LogUtils.w("EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        //        if (softInputHeight > 0) {
        //            if (sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, -1) == -1) {
        //                sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
        //            }
        //        }
        if (softInputHeight < 0) {
            softInputHeight = 0;
        }
        return softInputHeight;
    }


    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 获取软键盘高度
     *
     * @return
     */
    public int getKeyBoardHeight() {
        return sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, Utils.Dp2Px(mActivity, 262));
    }

}

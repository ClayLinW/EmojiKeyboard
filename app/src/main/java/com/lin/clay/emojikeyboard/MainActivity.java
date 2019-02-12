package com.lin.clay.emojikeyboard;


import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lin.clay.emojikeyboard.adapter.ChatAdapter;
import com.lin.clay.emojikeyboard.adapter.EmotionGiftsFragmentAdapter;
import com.lin.clay.emojikeyboard.fragment.EmotionFragment;
import com.lin.clay.emojikeyboard.fragment.GiftsFragment;
import com.lin.clay.emojikeyboard.fragment.OneKeySendFragment;
import com.lin.clay.emojikeyboard.model.ChatMsgModel;
import com.lin.clay.emojikeyboard.model.IMChatGiftsModel;
import com.lin.clay.emojikeyboard.utils.widget.CustomPasteEditText;
import com.lin.clay.emojikeyboard.utils.EmotionKeyboard;
import com.lin.clay.emojikeyboard.utils.GlobalOnItemClickManagerUtils;
import com.lin.clay.emojikeyboard.utils.widget.NoHorizontalScrollerViewPager;
import com.lin.clay.emojikeyboard.utils.ToastUtils;

import com.lin.clay.emojikeyboard.utils.Utils;
import com.lin.clay.statusbarutils.StatusBarUtil;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ibBackChat)
    ImageButton ibBackChat;
    @BindView(R.id.tv_nikeName)
    TextView tvNikeName;
    @BindView(R.id.rv_matchMsg)
    RecyclerView rvMatchMsg;
    @BindView(R.id.edit_chatMsg)
    CustomPasteEditText editChatMsg;
    @BindView(R.id.ll_chatMsg)
    LinearLayout llChatMsg;
    @BindView(R.id.img_emoji)
    ImageView imgEmoji;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.img_oneKeySend)
    ImageView imgOneKeySend;
    @BindView(R.id.rl_oneKeySend)
    RelativeLayout rlOneKeySend;
    @BindView(R.id.img_giftCommon)
    ImageView imgGiftCommon;
    @BindView(R.id.rl_giftCommon)
    RelativeLayout rlGiftCommon;
    @BindView(R.id.ll_moreOperate)
    LinearLayout llMoreOperate;
    @BindView(R.id.ll_chatControl)
    LinearLayout llChatControl;
    @BindView(R.id.vp_emotionGifts)
    NoHorizontalScrollerViewPager vpEmotionGifts;
    @BindView(R.id.ll_emotionGifts)
    LinearLayout llEmotionGifts;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.svga_gift)
    SVGAImageView svgaGift;

    private static Handler mHandler = new Handler();
    private ArrayList<String> mSvgaList = new ArrayList();
    private ArrayList<ChatMsgModel> mChatMsgList = new ArrayList<>();
    private ChatAdapter mChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initKEG();
    }

    private void initData() {
        StatusBarUtil.setSocialityStatusBar(this);
        ButterKnife.bind(this);
        Utils.init(this);
        svgaGift.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
            }

            @Override
            public void onFinished() {
                svgaGift.setVisibility(View.GONE);
            }

            @Override
            public void onRepeat() {
            }

            @Override
            public void onStep(int i, double v) {
            }
        });

        initRecyclerViewData();

        tvSend.setOnTouchListener(Utils.getTouchBackListener(0.8f));
    }

    private void initRecyclerViewData() {
        for (int i = 0; i < 2; i++) {
            ChatMsgModel chatMsgModel = new ChatMsgModel();
            if (i == 0) {
                chatMsgModel.msgType = ChatMsgModel.TEXT_MSG_TYPE;
                chatMsgModel.text = "Hello World!";
                chatMsgModel.isSend = true;
            } else {
                chatMsgModel.msgType = ChatMsgModel.GIFT_MSG_TYPE;
                chatMsgModel.giftIconId = R.drawable.gift_car_v1;
                chatMsgModel.giftTitle = "car_1";
                chatMsgModel.strSvga = "posche.svga";
                chatMsgModel.isSend = true;
            }
            mChatMsgList.add(chatMsgModel);
        }

        mChatAdapter = new ChatAdapter(this, mChatMsgList);
        rvMatchMsg.setLayoutManager(new LinearLayoutManager(this));
        rvMatchMsg.setAdapter(mChatAdapter);
        mChatAdapter.setmOnGiftClickPlayListener(new ChatAdapter.OnGiftClickPlayListener() {
            @Override
            public void giftClick(String strSvga) {
                loadAnimation(strSvga);
            }
        });

        rvMatchMsg.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(!isInterceptBackPress())
                {
                    Utils.hideInput(MainActivity.this);
                }
                return false;
            }
        });
    }

    //表情面板
    private EmotionKeyboard mEmotionKeyboard;
    List<Fragment> emotionFragments = new ArrayList<>();
    private GlobalOnItemClickManagerUtils GlobalOnItemClickManager;        //表情和编辑框的绑定

    private void initKEG() {
        mEmotionKeyboard = EmotionKeyboard.with(this)
                .setEmotionView(llEmotionGifts)//绑定表情面板
                .setEmotionGiftsView(vpEmotionGifts)//绑定礼物表情选择
                .bindToContent(rvMatchMsg)//绑定内容view
                .bindToEditText(editChatMsg)//判断绑定那种EditView
                .bindToEmotionButton(imgEmoji)//绑定表情按钮
                .bindToGiftsButton(rlGiftCommon)//绑定礼物按钮
                .bindToOneKeySendButton(rlOneKeySend)//u定一键发送按钮
                .bindToChatControl(llChatControl)   //绑定整个底部表情礼物布局
                .bindKeyboardListener() //绑定键盘弹出隐藏监听
                .build();

        //创建全局监听
        GlobalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance(this);
        GlobalOnItemClickManager.attachToEditText(editChatMsg);

        emotionFragments.add(new EmotionFragment());
        emotionFragments.add(new GiftsFragment());
        emotionFragments.add(new OneKeySendFragment());
        vpEmotionGifts.setAdapter(new EmotionGiftsFragmentAdapter(getSupportFragmentManager(), emotionFragments));
        //设置缓存View的个数
        vpEmotionGifts.setOffscreenPageLimit(emotionFragments.size() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (GlobalOnItemClickManager != null) {
            GlobalOnItemClickManager.onDestrouGlobalOnItemClickManager();
        }
    }

    /**
     * 聊天列表滚动到底部
     *
     * @param delay
     */
    public void chatListScroolDelay(int delay) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rvMatchMsg.smoothScrollToPosition(mChatMsgList.size());
            }
        }, delay);
    }

    @Override
    public void onBackPressed() {
        /**
         * 判断是否拦截返回键操作
         */
        if (!isInterceptBackPress()) {
            super.onBackPressed();
        }
    }

    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     *
     * @return true则隐藏表情布局，拦截返回键操作
     * false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress() {
        return mEmotionKeyboard.interceptBackPress();
    }

    /**
     * 模拟从服务器加载礼物信息
     */
    public void loadChatGifts() {
        ArrayList<IMChatGiftsModel> data = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            IMChatGiftsModel imChatGiftsModel = new IMChatGiftsModel();
            imChatGiftsModel.id = i;
            if (i == 0) {
                imChatGiftsModel.iconId = R.drawable.gift_car_home;

            } else {
                imChatGiftsModel.iconId = R.drawable.gift_other_home;
            }
            ArrayList<IMChatGiftsModel.GiftDetail> items = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                IMChatGiftsModel.GiftDetail giftDetail = new IMChatGiftsModel.GiftDetail();
                giftDetail.id = String.valueOf((1000 + j));
                if (i == 0) {
                    giftDetail.title = "car_" + (j + 1);
                    giftDetail.coverImgId = getResources().getIdentifier("gift_car_v" + (j + 1), "drawable", getPackageName());
                    giftDetail.scene = "汽车";
                    giftDetail.strSvga = "posche.svga";
                } else {
                    giftDetail.title = "other_" + (j + 1);
                    giftDetail.coverImgId = getResources().getIdentifier("gift_other_v" + (j + 1), "drawable", getPackageName());
                    giftDetail.scene = "其它";
                    giftDetail.strSvga = randomSvga();
                }
                giftDetail.price = (int) (Math.random() * 1000);
                items.add(giftDetail);
            }
            imChatGiftsModel.items = items;
            data.add(imChatGiftsModel);
        }
        loadIMChatGiftsSuccess(data);
    }

    private String randomSvga() {
        if (mSvgaList.size() == 0) {
            mSvgaList.add("angel.svga");
            mSvgaList.add("alarm.svga");
            mSvgaList.add("EmptyState.svga");
            mSvgaList.add("heartbeat.svga");
            mSvgaList.add("rose_1.5.0.svga");
            mSvgaList.add("rose_2.0.0.svga");
            mSvgaList.add("test.svga");
            mSvgaList.add("test2.svga");
        }
        return mSvgaList.get((int) Math.floor(Math.random() * mSvgaList.size()));
    }

    /**
     * 将礼物对象提交给fragment
     *
     * @param data
     */
    private void loadIMChatGiftsSuccess(ArrayList<IMChatGiftsModel> data) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof GiftsFragment) {
                    GiftsFragment giftsFragment = (GiftsFragment) fragment;
                    giftsFragment.loadIMChatGiftsSuccess(data);
                }
            }
        }
    }

    /**
     * 赠送礼物
     *
     * @param checkGifts
     */
    public void gradualGifts(IMChatGiftsModel.GiftDetail checkGifts) {
        loadAnimation(checkGifts.strSvga);
        ChatMsgModel chatMsgModel = new ChatMsgModel();
        chatMsgModel.msgType = ChatMsgModel.GIFT_MSG_TYPE;
        chatMsgModel.giftIconId = checkGifts.coverImgId;
        chatMsgModel.giftTitle = checkGifts.title;
        chatMsgModel.strSvga = checkGifts.strSvga;
        chatMsgModel.isSend = true;
        mChatMsgList.add(chatMsgModel);
        mChatAdapter.notifyItemInserted(mChatMsgList.size() - 1);
        chatListScroolDelay(100);
    }

    /**
     * 发送一键发送消息
     *
     * @param oneKeyMsg
     */
    public void sendOneKeyMsg(String oneKeyMsg) {
        ChatMsgModel chatMsgModel = new ChatMsgModel();
        chatMsgModel.isSend = true;
        chatMsgModel.text = oneKeyMsg;
        mChatMsgList.add(chatMsgModel);
        mChatAdapter.notifyItemInserted(mChatMsgList.size() - 1);
        chatListScroolDelay(100);
    }

    /**
     * 长按播放礼物
     *
     * @param giftId
     * @param title
     * @param strSvga
     */
    public void playFrameGift(String giftId, String title, String strSvga) {
        loadAnimation(strSvga);
    }

    private void loadAnimation(String strSvga) {
        SVGAParser parser = new SVGAParser(this);
        parser.parse(strSvga, new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                svgaGift.setVisibility(View.VISIBLE);
                svgaGift.setVideoItem(videoItem);
                svgaGift.setLoops(1);
                svgaGift.startAnimation();
            }

            @Override
            public void onError() {

            }
        });
    }

    @OnClick({R.id.ibBackChat, R.id.svga_gift, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibBackChat:
                onBackPressed();
                break;
            case R.id.svga_gift:
                svgaGift.stopAnimation();
                break;
            case R.id.tv_send:
                sendTextMsg();
                break;
        }
    }

    private void sendTextMsg() {
        String textMsg = editChatMsg.getText().toString();
        if (TextUtils.isEmpty(textMsg)) {
            ToastUtils.showToast(this, "请先输入消息内容", Toast.LENGTH_SHORT, ToastUtils.DEFEAT);
            return;
        }
        editChatMsg.setText("");
        ChatMsgModel chatMsgModel = new ChatMsgModel();
        chatMsgModel.isSend = true;
        chatMsgModel.text = textMsg;
        mChatMsgList.add(chatMsgModel);
        mChatAdapter.notifyItemInserted(mChatMsgList.size() - 1);
        chatListScroolDelay(100);
    }
}

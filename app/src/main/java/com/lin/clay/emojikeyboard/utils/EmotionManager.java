
package com.lin.clay.emojikeyboard.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import com.lin.clay.emojikeyboard.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.collection.ArrayMap;

public class EmotionManager {

    /**
     * 表情类型标志符
     */
    public static final int EMOTION_CLASSIC_TYPE = 0x0001;//经典表情
    public static final int EMOTION_OTHER_TYPE = 0x0002;//经典表情

    /**
     * key-表情文字;
     * value-表情图片资源
     */
    public ArrayMap<String, Integer> EMOTION_CLASSIC_MAP;
    public ArrayMap<String, Integer> EMOTION_OTHER_MAP;
    public ArrayMap<String, Integer> EMPTY_MAP;

    public ArrayMap<String, Integer> mEmojiSmileyToRes;
    public Pattern mPatternEmoji;

    private static EmotionManager emotionManager;

    public static EmotionManager getInstance() {
        if (emotionManager == null) {
            synchronized (EmotionManager.class) {
                if (emotionManager == null) {
                    emotionManager = new EmotionManager();
                }
            }
        }
        return emotionManager;
    }

    private EmotionManager() {
        mPatternEmoji = buildPatternEmoji();
        EMOTION_CLASSIC_MAP = buildEmojiSmileyToRes();

        //其它表情
        EMOTION_OTHER_MAP = new ArrayMap<>();
//        EMOTION_OTHER_MAP.put("[clay呵呵]", R.drawable.lt_001_s);
//        EMOTION_OTHER_MAP.put("[clay嘻嘻]", R.drawable.lt_002_s);
//        EMOTION_OTHER_MAP.put("[clay哈哈]", R.drawable.lt_003_s);
        //空表情
        EMPTY_MAP = new ArrayMap<>();
    }

    private final int[] EMOJI_SMILEY_RES_IDS = {
            R.drawable.emoji_001, R.drawable.emoji_002, R.drawable.emoji_003, R.drawable.emoji_004,
            R.drawable.emoji_005, R.drawable.emoji_006, R.drawable.emoji_007, R.drawable.emoji_008, R.drawable.emoji_009, R.drawable.emoji_010,
            R.drawable.emoji_011, R.drawable.emoji_012, R.drawable.emoji_013, R.drawable.emoji_014, R.drawable.emoji_015, R.drawable.emoji_016,
            R.drawable.emoji_017, R.drawable.emoji_018, R.drawable.emoji_019, R.drawable.emoji_020, R.drawable.emoji_021, R.drawable.emoji_022,
            R.drawable.emoji_023, R.drawable.emoji_024, R.drawable.emoji_025, R.drawable.emoji_026, R.drawable.emoji_027, R.drawable.emoji_028,
            R.drawable.emoji_029, R.drawable.emoji_030, R.drawable.emoji_031, R.drawable.emoji_032, R.drawable.emoji_033, R.drawable.emoji_034,
            R.drawable.emoji_035, R.drawable.emoji_036, R.drawable.emoji_037, R.drawable.emoji_038, R.drawable.emoji_039, R.drawable.emoji_040,
            R.drawable.emoji_041, R.drawable.emoji_042, R.drawable.emoji_043, R.drawable.emoji_044, R.drawable.emoji_045, R.drawable.emoji_046,
            R.drawable.emoji_047, R.drawable.emoji_048, R.drawable.emoji_049, R.drawable.emoji_050, R.drawable.emoji_051, R.drawable.emoji_052,
            R.drawable.emoji_053, R.drawable.emoji_054, R.drawable.emoji_055, R.drawable.emoji_056, R.drawable.emoji_057, R.drawable.emoji_058,
            R.drawable.emoji_059, R.drawable.emoji_060, R.drawable.emoji_061, R.drawable.emoji_062, R.drawable.emoji_063, R.drawable.emoji_064,
            R.drawable.emoji_065, R.drawable.emoji_066, R.drawable.emoji_067, R.drawable.emoji_068, R.drawable.emoji_069, R.drawable.emoji_070,
            R.drawable.emoji_071, R.drawable.emoji_072, R.drawable.emoji_073, R.drawable.emoji_074, R.drawable.emoji_075, R.drawable.emoji_076,
            R.drawable.emoji_077, R.drawable.emoji_078, R.drawable.emoji_079, R.drawable.emoji_080, R.drawable.emoji_081, R.drawable.emoji_082,
            R.drawable.emoji_083, R.drawable.emoji_084, R.drawable.emoji_085, R.drawable.emoji_086, R.drawable.emoji_087, R.drawable.emoji_088,
            R.drawable.emoji_089, R.drawable.emoji_090, R.drawable.emoji_091, R.drawable.emoji_092, R.drawable.emoji_093, R.drawable.emoji_094,
            R.drawable.emoji_095, R.drawable.emoji_096, R.drawable.emoji_097, R.drawable.emoji_098, R.drawable.emoji_099, R.drawable.emoji_100,
            R.drawable.emoji_101, R.drawable.emoji_102, R.drawable.emoji_103, R.drawable.emoji_104, R.drawable.emoji_105, R.drawable.emoji_106,
            R.drawable.emoji_107, R.drawable.emoji_108, R.drawable.emoji_109, R.drawable.emoji_110, R.drawable.emoji_111, R.drawable.emoji_112,
            R.drawable.emoji_113, R.drawable.emoji_114, R.drawable.emoji_115, R.drawable.emoji_116, R.drawable.emoji_117, R.drawable.emoji_118,
            R.drawable.emoji_119, R.drawable.emoji_120, R.drawable.emoji_121, R.drawable.emoji_122, R.drawable.emoji_123, R.drawable.emoji_124,
    };

    public final String[] EMOJI_TEXT_ARRAY = new String[]{
            "\uD83D\uDE0C", "\uD83D\uDE28", "\uD83D\uDE37", "\uD83D\uDE33", "\uD83D\uDE12", "\uD83D\uDE30", "\uD83D\uDE0A", "\uD83D\uDE03", "\uD83D\uDE1E",
            "\uD83D\uDE20", "\uD83D\uDE1C", "\uD83D\uDE0D", "\uD83D\uDE31", "\uD83D\uDE13", "\uD83D\uDE25", "\uD83D\uDE0F", "\uD83D\uDE14", "\uD83D\uDE01",
            "\uD83D\uDE09", "\uD83D\uDE23", "\uD83D\uDE16", "\uD83D\uDE2A", "\uD83D\uDE1D", "\uD83D\uDE32", "\uD83D\uDE2D", "\uD83D\uDE02", "\uD83D\uDE22",
            "☺", "\uD83D\uDE04", "\uD83D\uDE21", "\uD83D\uDE1A", "\uD83D\uDE18", "\uD83D\uDC4F", "\uD83D\uDC4D", "\uD83D\uDC4C", "\uD83D\uDC4E", "\uD83D\uDCAA",
            "\uD83D\uDC4A", "\uD83D\uDC46", "✌", "✋", "\uD83D\uDCA1", "\uD83C\uDF39", "\uD83C\uDF84", "\uD83D\uDEA4", "\uD83D\uDC8A", "\uD83D\uDEC1", "⭕",
            "❌", "❓", "❗", "\uD83D\uDEB9", "\uD83D\uDEBA", "\uD83D\uDC8B", "❤", "\uD83D\uDC94", "\uD83D\uDC98", "\uD83C\uDF81", "\uD83C\uDF89", "\uD83D\uDCA4",
            "\uD83D\uDCA8", "\uD83D\uDD25", "\uD83D\uDCA6", "⭐", "\uD83C\uDFC0", "⚽", "\uD83C\uDFBE", "\uD83C\uDF74", "\uD83C\uDF5A", "\uD83C\uDF5C", "\uD83C\uDF70",
            "\uD83C\uDF54", "\uD83C\uDF82", "\uD83C\uDF59", "☕", "\uD83C\uDF7B", "\uD83C\uDF49", "\uD83C\uDF4E", "\uD83C\uDF4A", "\uD83C\uDF53", "☀", "☔", "\uD83C\uDF19",
            "⚡", "⛄", "☁", "\uD83C\uDFC3", "\uD83D\uDEB2", "\uD83D\uDE8C", "\uD83D\uDE85", "\uD83D\uDE95", "\uD83D\uDE99", "✈", "\uD83D\uDC78", "\uD83D\uDD31",
            "\uD83D\uDC51", "\uD83D\uDC8D", "\uD83D\uDC8E", "\uD83D\uDC84", "\uD83D\uDC85", "\uD83D\uDC60", "\uD83D\uDC62", "\uD83D\uDC52", "\uD83D\uDC57", "\uD83C\uDF80",
            "\uD83D\uDC5C", "\uD83C\uDF40", "\uD83D\uDC9D", "\uD83D\uDC36", "\uD83D\uDC2E", "\uD83D\uDC35", "\uD83D\uDC2F", "\uD83D\uDC3B", "\uD83D\uDC37", "\uD83D\uDC30",
            "\uD83D\uDC24", "\uD83D\uDC2C", "\uD83D\uDC33", "\uD83C\uDFB5", "\uD83D\uDCF7", "\uD83C\uDFA5", "\uD83D\uDCBB", "\uD83D\uDCF1", "\uD83D\uDD52"
    };

    //聊天emoji表情
    private ArrayMap<String, Integer> buildEmojiSmileyToRes() {
        if (EMOJI_SMILEY_RES_IDS.length != EMOJI_TEXT_ARRAY.length) {
            //表情的数量需要和数组定义的长度一致！
            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }

        ArrayMap<String, Integer> smileyToRes = new ArrayMap<String, Integer>(EMOJI_TEXT_ARRAY.length);
        for (int i = 0; i < EMOJI_TEXT_ARRAY.length; i++) {
            smileyToRes.put(EMOJI_TEXT_ARRAY[i], EMOJI_SMILEY_RES_IDS[i]);
        }

        return smileyToRes;
    }

    //构建Emoji正则表达式
    private Pattern buildPatternEmoji() {
        StringBuilder patternString = new StringBuilder(EMOJI_TEXT_ARRAY.length * 3);
        patternString.append('(');
        for (String s : EMOJI_TEXT_ARRAY) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");

        return Pattern.compile(patternString.toString());
    }

    /**
     * 根据文本替换成emoji表情图片
     *
     * @param text
     * @param size
     * @return
     */
    public CharSequence replaceEmoji(Context mContext, CharSequence text, int size) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPatternEmoji.matcher(text);
        while (matcher.find()) {
            int resId = mEmojiSmileyToRes.get(matcher.group());
            Drawable drawable = mContext.getResources().getDrawable(resId);
            drawable.setBounds(0, 0, Utils.getRealPixel(size), Utils.getRealPixel(size));//这里设置图片的大小
            MyImageSpan imageSpan = new MyImageSpan(drawable);
            builder.setSpan(imageSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    /**
     * 根据名称获取当前表情图标R值
     *
     * @param EmotionType 表情类型标志符
     * @param imgName     名称
     * @return
     */
    public int getImgByName(int EmotionType, String imgName) {
        Integer integer = null;
        switch (EmotionType) {
            case EMOTION_CLASSIC_TYPE:
                integer = EMOTION_CLASSIC_MAP.get(imgName);
                break;
            case EMOTION_OTHER_TYPE:
                integer = EMOTION_OTHER_MAP.get(imgName);
                break;
            default:
                LogUtils.e("the emojiMap is null!! Handle Yourself ");
                break;
        }
        return integer == null ? -1 : integer;
    }

    /**
     * 根据类型获取表情数据
     *
     * @param EmotionType
     * @return
     */
    public ArrayMap<String, Integer> getEmojiMap(int EmotionType) {
        ArrayMap EmojiMap = null;
        switch (EmotionType) {
            case EMOTION_CLASSIC_TYPE:
                EmojiMap = EMOTION_CLASSIC_MAP;
                break;
            case EMOTION_OTHER_TYPE:
                EmojiMap = EMOTION_OTHER_MAP;
                break;
            default:
                EmojiMap = EMPTY_MAP;
                break;
        }
        return EmojiMap;
    }
}

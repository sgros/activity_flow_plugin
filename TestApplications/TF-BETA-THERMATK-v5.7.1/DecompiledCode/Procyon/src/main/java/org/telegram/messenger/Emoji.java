// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.text.style.ImageSpan;
import android.graphics.ColorFilter;
import android.graphics.Canvas;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import android.text.Spannable$Factory;
import android.text.Spannable;
import android.graphics.Paint$FontMetricsInt;
import android.content.SharedPreferences;
import java.io.InputStream;
import android.content.res.AssetManager;
import java.io.File;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap$Config;
import android.os.Build$VERSION;
import android.graphics.BitmapFactory$Options;
import java.util.Locale;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import java.util.ArrayList;
import android.graphics.Paint;
import java.util.HashMap;
import android.graphics.Bitmap;

public class Emoji
{
    private static final int MAX_RECENT_EMOJI_COUNT = 48;
    private static int bigImgSize = 0;
    private static final int[][] cols;
    private static int drawImgSize = 0;
    private static Bitmap[][] emojiBmp;
    public static HashMap<String, String> emojiColor;
    public static HashMap<String, Integer> emojiUseHistory;
    private static boolean inited = false;
    private static boolean[][] loadingEmoji;
    private static Paint placeholderPaint;
    public static ArrayList<String> recentEmoji;
    private static boolean recentEmojiLoaded = false;
    private static HashMap<CharSequence, DrawableInfo> rects;
    private static final int splitCount = 4;
    
    static {
        Emoji.rects = new HashMap<CharSequence, DrawableInfo>();
        Emoji.inited = false;
        Emoji.emojiBmp = new Bitmap[8][4];
        Emoji.loadingEmoji = new boolean[8][4];
        Emoji.emojiUseHistory = new HashMap<String, Integer>();
        Emoji.recentEmoji = new ArrayList<String>();
        Emoji.emojiColor = new HashMap<String, String>();
        final int[] array = { 16, 16, 16, 16 };
        int n = 2;
        cols = new int[][] { array, { 6, 6, 6, 6 }, { 5, 5, 5, 5 }, { 7, 7, 7, 7 }, { 5, 5, 5, 5 }, { 7, 7, 7, 7 }, { 8, 8, 8, 8 }, { 8, 8, 8, 8 } };
        final float density = AndroidUtilities.density;
        int n2 = 66;
        if (density <= 1.0f) {
            n2 = 33;
            n = 1;
        }
        else if (density <= 1.5f) {}
        Emoji.drawImgSize = AndroidUtilities.dp(20.0f);
        float n3;
        if (AndroidUtilities.isTablet()) {
            n3 = 40.0f;
        }
        else {
            n3 = 34.0f;
        }
        Emoji.bigImgSize = AndroidUtilities.dp(n3);
        int n4 = 0;
        while (true) {
            final String[][] data = EmojiData.data;
            if (n4 >= data.length) {
                break;
            }
            final int n5 = (int)Math.ceil(data[n4].length / 4.0f);
            for (int i = 0; i < EmojiData.data[n4].length; ++i) {
                final int n6 = i / n5;
                final int n7 = i - n6 * n5;
                final int[][] cols2 = Emoji.cols;
                final int n8 = n7 % cols2[n4][n6];
                final int n9 = n7 / cols2[n4][n6];
                final int n10 = n8 * n;
                final int n11 = n9 * n;
                Emoji.rects.put(EmojiData.data[n4][i], new DrawableInfo(new Rect(n8 * n2 + n10, n9 * n2 + n11, (n8 + 1) * n2 + n10, (n9 + 1) * n2 + n11), (byte)n4, (byte)n6, i));
            }
            ++n4;
        }
        (Emoji.placeholderPaint = new Paint()).setColor(0);
    }
    
    public static void addRecentEmoji(final String key) {
        Integer value;
        if ((value = Emoji.emojiUseHistory.get(key)) == null) {
            value = 0;
        }
        if (value == 0 && Emoji.emojiUseHistory.size() >= 48) {
            final ArrayList<String> recentEmoji = Emoji.recentEmoji;
            Emoji.emojiUseHistory.remove(recentEmoji.get(recentEmoji.size() - 1));
            final ArrayList<String> recentEmoji2 = Emoji.recentEmoji;
            recentEmoji2.set(recentEmoji2.size() - 1, key);
        }
        Emoji.emojiUseHistory.put(key, value + 1);
    }
    
    public static void clearRecentEmoji() {
        MessagesController.getGlobalEmojiSettings().edit().putBoolean("filled_default", true).commit();
        Emoji.emojiUseHistory.clear();
        Emoji.recentEmoji.clear();
        saveRecentEmoji();
    }
    
    public static String fixEmoji(String s) {
        int beginIndex;
        int n;
        String s2;
        for (int length = s.length(), i = 0; i < length; i = beginIndex + 1, length = n, s = s2) {
            final char char1 = s.charAt(i);
            if (char1 >= '\ud83c' && char1 <= '\ud83e') {
                if (char1 != '\ud83c' || i >= length - 1) {
                    beginIndex = i + 1;
                    n = length;
                    s2 = s;
                    continue;
                }
                beginIndex = i + 1;
                final char char2 = s.charAt(beginIndex);
                if (char2 != '\ude2f' && char2 != '\udc04' && char2 != '\ude1a' && char2 != '\udd7f') {
                    n = length;
                    s2 = s;
                    continue;
                }
                final StringBuilder sb = new StringBuilder();
                beginIndex = i + 2;
                sb.append(s.substring(0, beginIndex));
                sb.append("\ufe0f");
                sb.append(s.substring(beginIndex));
                s = sb.toString();
            }
            else {
                if (char1 == '\u20e3') {
                    return s;
                }
                n = length;
                s2 = s;
                beginIndex = i;
                if (char1 < '\u203c') {
                    continue;
                }
                n = length;
                s2 = s;
                beginIndex = i;
                if (char1 > '\u3299') {
                    continue;
                }
                n = length;
                s2 = s;
                beginIndex = i;
                if (!EmojiData.emojiToFE0FMap.containsKey(char1)) {
                    continue;
                }
                final StringBuilder sb2 = new StringBuilder();
                beginIndex = i + 1;
                sb2.append(s.substring(0, beginIndex));
                sb2.append("\ufe0f");
                sb2.append(s.substring(beginIndex));
                s = sb2.toString();
            }
            n = length + 1;
            s2 = s;
        }
        return s;
    }
    
    public static Drawable getEmojiBigDrawable(final String key) {
        EmojiDrawable emojiDrawable2;
        final EmojiDrawable emojiDrawable = emojiDrawable2 = getEmojiDrawable(key);
        if (emojiDrawable == null) {
            final CharSequence charSequence = EmojiData.emojiAliasMap.get(key);
            emojiDrawable2 = emojiDrawable;
            if (charSequence != null) {
                emojiDrawable2 = getEmojiDrawable(charSequence);
            }
        }
        if (emojiDrawable2 == null) {
            return null;
        }
        final int bigImgSize = Emoji.bigImgSize;
        emojiDrawable2.setBounds(0, 0, bigImgSize, bigImgSize);
        emojiDrawable2.fullSize = true;
        return emojiDrawable2;
    }
    
    public static EmojiDrawable getEmojiDrawable(final CharSequence obj) {
        DrawableInfo drawableInfo2;
        final DrawableInfo drawableInfo = drawableInfo2 = Emoji.rects.get(obj);
        if (drawableInfo == null) {
            final CharSequence key = EmojiData.emojiAliasMap.get(obj);
            drawableInfo2 = drawableInfo;
            if (key != null) {
                drawableInfo2 = Emoji.rects.get(key);
            }
        }
        if (drawableInfo2 == null) {
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("No drawable for emoji ");
                sb.append((Object)obj);
                FileLog.d(sb.toString());
            }
            return null;
        }
        final EmojiDrawable emojiDrawable = new EmojiDrawable(drawableInfo2);
        final int drawImgSize = Emoji.drawImgSize;
        emojiDrawable.setBounds(0, 0, drawImgSize, drawImgSize);
        return emojiDrawable;
    }
    
    private static boolean inArray(final char c, final char[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == c) {
                return true;
            }
        }
        return false;
    }
    
    public static void invalidateAll(final View view) {
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                invalidateAll(viewGroup.getChildAt(i));
            }
        }
        else if (view instanceof TextView) {
            view.invalidate();
        }
    }
    
    public static boolean isValidEmoji(final String s) {
        DrawableInfo drawableInfo2;
        final DrawableInfo drawableInfo = drawableInfo2 = Emoji.rects.get(s);
        if (drawableInfo == null) {
            final CharSequence key = EmojiData.emojiAliasMap.get(s);
            drawableInfo2 = drawableInfo;
            if (key != null) {
                drawableInfo2 = Emoji.rects.get(key);
            }
        }
        return drawableInfo2 != null;
    }
    
    private static void loadEmoji(final int n, final int i) {
        try {
            int inSampleSize;
            if (AndroidUtilities.density <= 1.0f) {
                inSampleSize = 2;
            }
            else {
                if (AndroidUtilities.density > 1.5f) {
                    final float density = AndroidUtilities.density;
                }
                inSampleSize = 1;
            }
            int j = 12;
            while (j < 14) {
                try {
                    final File fileStreamPath = ApplicationLoader.applicationContext.getFileStreamPath(String.format(Locale.US, "v%d_emoji%.01fx_%d.png", j, 2.0f, n));
                    if (fileStreamPath.exists()) {
                        fileStreamPath.delete();
                    }
                    ++j;
                    continue;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                break;
            }
            Bitmap decodeStream;
            final Bitmap bitmap = decodeStream = null;
            try {
                final AssetManager assets = ApplicationLoader.applicationContext.getAssets();
                decodeStream = bitmap;
                decodeStream = bitmap;
                final StringBuilder sb = new StringBuilder();
                decodeStream = bitmap;
                sb.append("emoji/");
                decodeStream = bitmap;
                sb.append(String.format(Locale.US, "v14_emoji%.01fx_%d_%d.png", 2.0f, n, i));
                decodeStream = bitmap;
                final InputStream open = assets.open(sb.toString());
                decodeStream = bitmap;
                decodeStream = bitmap;
                final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
                decodeStream = bitmap;
                bitmapFactory$Options.inJustDecodeBounds = false;
                decodeStream = bitmap;
                bitmapFactory$Options.inSampleSize = inSampleSize;
                decodeStream = bitmap;
                if (Build$VERSION.SDK_INT >= 26) {
                    decodeStream = bitmap;
                    bitmapFactory$Options.inPreferredConfig = Bitmap$Config.HARDWARE;
                }
                decodeStream = bitmap;
                decodeStream = BitmapFactory.decodeStream(open, (Rect)null, bitmapFactory$Options);
                open.close();
                decodeStream = decodeStream;
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
            AndroidUtilities.runOnUIThread(new _$$Lambda$Emoji$8PuKHwVD_cYGVxmUFCHOFHCQ5bM(n, i, decodeStream));
        }
        catch (Throwable t2) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error loading emoji", t2);
            }
        }
    }
    
    public static void loadRecentEmoji() {
        if (Emoji.recentEmojiLoaded) {
            return;
        }
        Emoji.recentEmojiLoaded = true;
        final SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        try {
            Emoji.emojiUseHistory.clear();
            if (globalEmojiSettings.contains("emojis")) {
                final String string = globalEmojiSettings.getString("emojis", "");
                if (string != null && string.length() > 0) {
                    final String[] split = string.split(",");
                    for (int length = split.length, i = 0; i < length; ++i) {
                        final String[] split2 = split[i].split("=");
                        long longValue = Utilities.parseLong(split2[0]);
                        final StringBuilder sb = new StringBuilder();
                        for (int j = 0; j < 4; ++j) {
                            sb.insert(0, String.valueOf((char)longValue));
                            longValue >>= 16;
                            if (longValue == 0L) {
                                break;
                            }
                        }
                        if (sb.length() > 0) {
                            Emoji.emojiUseHistory.put(sb.toString(), Utilities.parseInt(split2[1]));
                        }
                    }
                }
                globalEmojiSettings.edit().remove("emojis").commit();
                saveRecentEmoji();
            }
            else {
                final String string2 = globalEmojiSettings.getString("emojis2", "");
                if (string2 != null && string2.length() > 0) {
                    final String[] split3 = string2.split(",");
                    for (int length2 = split3.length, k = 0; k < length2; ++k) {
                        final String[] split4 = split3[k].split("=");
                        Emoji.emojiUseHistory.put(split4[0], Utilities.parseInt(split4[1]));
                    }
                }
            }
            if (Emoji.emojiUseHistory.isEmpty() && !globalEmojiSettings.getBoolean("filled_default", false)) {
                final String[] array = { "\ud83d\ude02", "\ud83d\ude18", "\u2764", "\ud83d\ude0d", "\ud83d\ude0a", "\ud83d\ude01", "\ud83d\udc4d", "\u263a", "\ud83d\ude14", "\ud83d\ude04", "\ud83d\ude2d", "\ud83d\udc8b", "\ud83d\ude12", "\ud83d\ude33", "\ud83d\ude1c", "\ud83d\ude48", "\ud83d\ude09", "\ud83d\ude03", "\ud83d\ude22", "\ud83d\ude1d", "\ud83d\ude31", "\ud83d\ude21", "\ud83d\ude0f", "\ud83d\ude1e", "\ud83d\ude05", "\ud83d\ude1a", "\ud83d\ude4a", "\ud83d\ude0c", "\ud83d\ude00", "\ud83d\ude0b", "\ud83d\ude06", "\ud83d\udc4c", "\ud83d\ude10", "\ud83d\ude15" };
                for (int l = 0; l < array.length; ++l) {
                    Emoji.emojiUseHistory.put(array[l], array.length - l);
                }
                globalEmojiSettings.edit().putBoolean("filled_default", true).commit();
                saveRecentEmoji();
            }
            sortEmoji();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            final String string3 = globalEmojiSettings.getString("color", "");
            if (string3 != null && string3.length() > 0) {
                final String[] split5 = string3.split(",");
                for (int n = 0; n < split5.length; ++n) {
                    final String[] split6 = split5[n].split("=");
                    Emoji.emojiColor.put(split6[0], split6[1]);
                }
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
    }
    
    public static CharSequence replaceEmoji(final CharSequence charSequence, final Paint$FontMetricsInt paint$FontMetricsInt, final int n, final boolean b) {
        return replaceEmoji(charSequence, paint$FontMetricsInt, n, b, null);
    }
    
    public static CharSequence replaceEmoji(final CharSequence charSequence, final Paint$FontMetricsInt paint$FontMetricsInt, final int n, final boolean b, int[] array) {
        if (!SharedConfig.useSystemEmoji && charSequence != null && charSequence.length() != 0) {
            Spannable spannable;
            if (!b && charSequence instanceof Spannable) {
                spannable = (Spannable)charSequence;
            }
            else {
                spannable = Spannable$Factory.getInstance().newSpannable((CharSequence)charSequence.toString());
            }
            final StringBuilder sb = new StringBuilder(16);
            new StringBuilder(2);
            final int length = charSequence.length();
            long n2 = 0L;
            int i = 0;
            int n3 = -1;
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
        Label_0105:
            while (i < length) {
                try {
                    final char char1 = charSequence.charAt(i);
                    int n9 = 0;
                    int n10 = 0;
                    Label_0640: {
                        if ((char1 >= '\ud83c' && char1 <= '\ud83e') || (n2 != 0L && (n2 & 0xFFFFFFFF00000000L) == 0x0L && (n2 & 0xFFFFL) == 0xD83CL && char1 >= '\udde6' && char1 <= '\uddff')) {
                            int n8;
                            if ((n8 = n3) == -1) {
                                n8 = i;
                            }
                            sb.append(char1);
                            n9 = n4 + 1;
                            n2 = (n2 << 16 | (long)char1);
                            n10 = n8;
                        }
                        else {
                            int n11 = 0;
                            Label_0279: {
                                if (sb.length() > 0 && (char1 == '\u2640' || char1 == '\u2642' || char1 == '\u2695')) {
                                    sb.append(char1);
                                    ++n4;
                                    n2 = 0L;
                                    n11 = n3;
                                }
                                else {
                                    if (n2 <= 0L || ('\uf000' & char1) != 0xD000) {
                                        int n12;
                                        int n13;
                                        int[] array2;
                                        int n14;
                                        if (char1 == '\u20e3') {
                                            n12 = n3;
                                            n13 = n4;
                                            array2 = array;
                                            n14 = n6;
                                            if (i > 0) {
                                                final char char2 = charSequence.charAt(n5);
                                                int n15;
                                                if ((char2 < '0' || char2 > '9') && char2 != '#' && char2 != '*') {
                                                    n15 = n6;
                                                }
                                                else {
                                                    sb.append(char2);
                                                    sb.append(char1);
                                                    n4 = i - n5 + 1;
                                                    n3 = n5;
                                                    n15 = 1;
                                                }
                                                n12 = n3;
                                                n13 = n4;
                                                array2 = array;
                                                n14 = n15;
                                            }
                                        }
                                        else {
                                            if ((char1 == '©' || char1 == '®' || (char1 >= '\u203c' && char1 <= '\u3299')) && EmojiData.dataCharsMap.containsKey(char1)) {
                                                if ((n11 = n3) == -1) {
                                                    n11 = i;
                                                }
                                                ++n4;
                                                sb.append(char1);
                                                break Label_0279;
                                            }
                                            if (n3 != -1) {
                                                sb.setLength(0);
                                                n9 = 0;
                                                n10 = -1;
                                                n6 = 0;
                                                break Label_0640;
                                            }
                                            n12 = n3;
                                            n13 = n4;
                                            array2 = array;
                                            n14 = n6;
                                            if (char1 != '\ufe0f') {
                                                n12 = n3;
                                                n13 = n4;
                                                array2 = array;
                                                n14 = n6;
                                                if (array != null) {
                                                    array[0] = 0;
                                                    array2 = null;
                                                    n14 = n6;
                                                    n13 = n4;
                                                    n12 = n3;
                                                }
                                            }
                                        }
                                        n9 = n13;
                                        n6 = n14;
                                        array = array2;
                                        n10 = n12;
                                        break Label_0640;
                                    }
                                    sb.append(char1);
                                    ++n4;
                                    n2 = 0L;
                                    n11 = n3;
                                }
                            }
                            n6 = 1;
                            n9 = n4;
                            n10 = n11;
                        }
                    }
                    Label_0844: {
                        if (n6 == 0) {
                            break Label_0844;
                        }
                        n5 = i + 2;
                        if (n5 >= length) {
                            break Label_0844;
                        }
                        final int n16 = i + 1;
                        final char char3 = charSequence.charAt(n16);
                        Label_0848: {
                            int n17;
                            if (char3 == '\ud83c') {
                                final char char4 = charSequence.charAt(n5);
                                if (char4 >= '\udffb' && char4 <= '\udfff') {
                                    sb.append(charSequence.subSequence(n16, i + 3));
                                    n9 += 2;
                                    break Label_0848;
                                }
                                break Label_0844;
                            }
                            else {
                                if (sb.length() < 2 || sb.charAt(0) != '\ud83c' || sb.charAt(1) != '\udff4' || char3 != '\udb40') {
                                    break Label_0844;
                                }
                                n17 = n16;
                            }
                            while (true) {
                                final int n18 = n17 + 2;
                                sb.append(charSequence.subSequence(n17, n18));
                                n9 += 2;
                                try {
                                    if (n18 < charSequence.length() && charSequence.charAt(n18) == '\udb40') {
                                        n17 = n18;
                                        continue;
                                    }
                                    n5 = n18 - 1;
                                    int n19 = n9;
                                    final int n20 = n5;
                                    int n21 = n6;
                                    int j = 0;
                                    int n22 = n20;
                                    while (j < 3) {
                                        final int n23 = n22 + 1;
                                        int n24 = 0;
                                        int n25 = 0;
                                        Label_1018: {
                                            if (n23 < length) {
                                                final char char5 = charSequence.charAt(n23);
                                                if (j == 1) {
                                                    if (char5 == '\u200d' && sb.length() > 0) {
                                                        sb.append(char5);
                                                        n24 = n19 + 1;
                                                        n22 = n23;
                                                        n25 = 0;
                                                        break Label_1018;
                                                    }
                                                }
                                                else if ((n10 != -1 || char1 == '*' || (char1 >= '1' && char1 <= '9')) && char5 >= '\ufe00') {
                                                    n25 = n21;
                                                    n24 = n19;
                                                    if (char5 <= '\ufe0f') {
                                                        n24 = n19 + 1;
                                                        n22 = n23;
                                                        n25 = n21;
                                                    }
                                                    break Label_1018;
                                                }
                                            }
                                            n24 = n19;
                                            n25 = n21;
                                        }
                                        ++j;
                                        n21 = n25;
                                        n19 = n24;
                                    }
                                    int n26 = 0;
                                    Label_1123: {
                                        if (n21 != 0) {
                                            n26 = n22 + 2;
                                            if (n26 < length) {
                                                final int n27 = n22 + 1;
                                                if (charSequence.charAt(n27) == '\ud83c') {
                                                    final char char6 = charSequence.charAt(n26);
                                                    if (char6 >= '\udffb' && char6 <= '\udfff') {
                                                        sb.append(charSequence.subSequence(n27, n22 + 3));
                                                        n19 += 2;
                                                        break Label_1123;
                                                    }
                                                }
                                            }
                                        }
                                        n26 = n22;
                                    }
                                    int n28;
                                    if (n21 != 0) {
                                        if (array != null) {
                                            ++array[0];
                                        }
                                        final EmojiDrawable emojiDrawable = getEmojiDrawable(sb.subSequence(0, sb.length()));
                                        if (emojiDrawable != null) {
                                            spannable.setSpan((Object)new EmojiSpan(emojiDrawable, 0, n, paint$FontMetricsInt), n10, n19 + n10, 33);
                                            ++n7;
                                        }
                                        sb.setLength(0);
                                        n28 = n7;
                                        n4 = 0;
                                        n3 = -1;
                                        n6 = 0;
                                    }
                                    else {
                                        n6 = n21;
                                        n3 = n10;
                                        n4 = n19;
                                        n28 = n7;
                                    }
                                    if (Build$VERSION.SDK_INT < 23 && n28 >= 50) {
                                        break;
                                    }
                                    n7 = n28;
                                    i = n26 + 1;
                                    continue Label_0105;
                                    n5 = i;
                                }
                                catch (Exception ex) {
                                    FileLog.e(ex);
                                    return charSequence;
                                }
                            }
                        }
                    }
                }
                catch (Exception ex2) {}
                break;
            }
            return (CharSequence)spannable;
        }
        return charSequence;
    }
    
    public static void saveEmojiColors() {
        final SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, String> entry : Emoji.emojiColor.entrySet()) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        globalEmojiSettings.edit().putString("color", sb.toString()).commit();
    }
    
    public static void saveRecentEmoji() {
        final SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, Integer> entry : Emoji.emojiUseHistory.entrySet()) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        globalEmojiSettings.edit().putString("emojis2", sb.toString()).commit();
    }
    
    public static void sortEmoji() {
        Emoji.recentEmoji.clear();
        final Iterator<Map.Entry<String, Integer>> iterator = Emoji.emojiUseHistory.entrySet().iterator();
        while (iterator.hasNext()) {
            Emoji.recentEmoji.add(iterator.next().getKey());
        }
        Collections.sort(Emoji.recentEmoji, (Comparator<? super String>)_$$Lambda$Emoji$IRtAaHh32_YY7tgie8_WycuV8i0.INSTANCE);
        while (Emoji.recentEmoji.size() > 48) {
            final ArrayList<String> recentEmoji = Emoji.recentEmoji;
            recentEmoji.remove(recentEmoji.size() - 1);
        }
    }
    
    private static class DrawableInfo
    {
        public int emojiIndex;
        public byte page;
        public byte page2;
        public Rect rect;
        
        public DrawableInfo(final Rect rect, final byte b, final byte b2, final int emojiIndex) {
            this.rect = rect;
            this.page = b;
            this.page2 = b2;
            this.emojiIndex = emojiIndex;
        }
    }
    
    public static class EmojiDrawable extends Drawable
    {
        private static Paint paint;
        private static Rect rect;
        private boolean fullSize;
        private DrawableInfo info;
        
        static {
            EmojiDrawable.paint = new Paint(2);
            EmojiDrawable.rect = new Rect();
        }
        
        public EmojiDrawable(final DrawableInfo info) {
            this.fullSize = false;
            this.info = info;
        }
        
        public void draw(final Canvas canvas) {
            final Bitmap[][] access$300 = Emoji.emojiBmp;
            final DrawableInfo info = this.info;
            if (access$300[info.page][info.page2] != null) {
                Rect rect;
                if (this.fullSize) {
                    rect = this.getDrawRect();
                }
                else {
                    rect = this.getBounds();
                }
                final Bitmap[][] access$301 = Emoji.emojiBmp;
                final DrawableInfo info2 = this.info;
                canvas.drawBitmap(access$301[info2.page][info2.page2], info2.rect, rect, EmojiDrawable.paint);
                return;
            }
            final boolean[][] access$302 = Emoji.loadingEmoji;
            final DrawableInfo info3 = this.info;
            if (access$302[info3.page][info3.page2]) {
                return;
            }
            final boolean[][] access$303 = Emoji.loadingEmoji;
            final DrawableInfo info4 = this.info;
            access$303[info4.page][info4.page2] = true;
            Utilities.globalQueue.postRunnable(new _$$Lambda$Emoji$EmojiDrawable$tIn098DVTEVbhZUc7ywBHxfGQOU(this));
            canvas.drawRect(this.getBounds(), Emoji.placeholderPaint);
        }
        
        public Rect getDrawRect() {
            final Rect bounds = this.getBounds();
            final int centerX = bounds.centerX();
            final int centerY = bounds.centerY();
            final Rect rect = EmojiDrawable.rect;
            int n;
            if (this.fullSize) {
                n = Emoji.bigImgSize;
            }
            else {
                n = Emoji.drawImgSize;
            }
            rect.left = centerX - n / 2;
            final Rect rect2 = EmojiDrawable.rect;
            int n2;
            if (this.fullSize) {
                n2 = Emoji.bigImgSize;
            }
            else {
                n2 = Emoji.drawImgSize;
            }
            rect2.right = centerX + n2 / 2;
            final Rect rect3 = EmojiDrawable.rect;
            int n3;
            if (this.fullSize) {
                n3 = Emoji.bigImgSize;
            }
            else {
                n3 = Emoji.drawImgSize;
            }
            rect3.top = centerY - n3 / 2;
            final Rect rect4 = EmojiDrawable.rect;
            int n4;
            if (this.fullSize) {
                n4 = Emoji.bigImgSize;
            }
            else {
                n4 = Emoji.drawImgSize;
            }
            rect4.bottom = centerY + n4 / 2;
            return EmojiDrawable.rect;
        }
        
        public DrawableInfo getDrawableInfo() {
            return this.info;
        }
        
        public int getOpacity() {
            return -2;
        }
        
        public void setAlpha(final int n) {
        }
        
        public void setColorFilter(final ColorFilter colorFilter) {
        }
    }
    
    public static class EmojiSpan extends ImageSpan
    {
        private Paint$FontMetricsInt fontMetrics;
        private int size;
        
        public EmojiSpan(final EmojiDrawable emojiDrawable, final int n, final int n2, final Paint$FontMetricsInt fontMetrics) {
            super((Drawable)emojiDrawable, n);
            this.size = AndroidUtilities.dp(20.0f);
            this.fontMetrics = fontMetrics;
            if (fontMetrics != null) {
                this.size = Math.abs(this.fontMetrics.descent) + Math.abs(this.fontMetrics.ascent);
                if (this.size == 0) {
                    this.size = AndroidUtilities.dp(20.0f);
                }
            }
        }
        
        public int getSize(final Paint paint, final CharSequence charSequence, int size, int size2, Paint$FontMetricsInt fontMetrics) {
            Paint$FontMetricsInt paint$FontMetricsInt = fontMetrics;
            if (fontMetrics == null) {
                paint$FontMetricsInt = new Paint$FontMetricsInt();
            }
            fontMetrics = this.fontMetrics;
            if (fontMetrics == null) {
                size2 = super.getSize(paint, charSequence, size, size2, paint$FontMetricsInt);
                final int dp = AndroidUtilities.dp(8.0f);
                final int dp2 = AndroidUtilities.dp(10.0f);
                size = -dp2 - dp;
                paint$FontMetricsInt.top = size;
                final int n = dp2 - dp;
                paint$FontMetricsInt.bottom = n;
                paint$FontMetricsInt.ascent = size;
                paint$FontMetricsInt.leading = 0;
                paint$FontMetricsInt.descent = n;
                return size2;
            }
            if (paint$FontMetricsInt != null) {
                paint$FontMetricsInt.ascent = fontMetrics.ascent;
                paint$FontMetricsInt.descent = fontMetrics.descent;
                paint$FontMetricsInt.top = fontMetrics.top;
                paint$FontMetricsInt.bottom = fontMetrics.bottom;
            }
            if (this.getDrawable() != null) {
                final Drawable drawable = this.getDrawable();
                size = this.size;
                drawable.setBounds(0, 0, size, size);
            }
            return this.size;
        }
        
        public void replaceFontMetrics(final Paint$FontMetricsInt fontMetrics, final int size) {
            this.fontMetrics = fontMetrics;
            this.size = size;
        }
    }
}

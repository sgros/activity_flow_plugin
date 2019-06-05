// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.utils;

import android.view.VelocityTracker;
import android.view.MotionEvent;
import android.annotation.SuppressLint;
import android.view.View;
import android.content.res.Resources;
import android.view.ViewConfiguration;
import android.content.Context;
import android.os.Build$VERSION;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import android.text.Layout$Alignment;
import android.graphics.Paint$Align;
import android.text.TextPaint;
import android.text.StaticLayout;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import java.util.List;
import android.util.Log;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.graphics.Paint$FontMetrics;
import com.github.mikephil.charting.formatter.IValueFormatter;
import android.graphics.Rect;

public abstract class Utils
{
    public static final double DEG2RAD = 0.017453292519943295;
    public static final double DOUBLE_EPSILON;
    public static final float FDEG2RAD = 0.017453292f;
    public static final float FLOAT_EPSILON;
    private static final int[] POW_10;
    private static Rect mCalcTextHeightRect;
    private static Rect mCalcTextSizeRect;
    private static IValueFormatter mDefaultValueFormatter;
    private static Rect mDrawTextRectBuffer;
    private static Rect mDrawableBoundsCache;
    private static Paint$FontMetrics mFontMetrics;
    private static Paint$FontMetrics mFontMetricsBuffer;
    private static int mMaximumFlingVelocity = 8000;
    private static DisplayMetrics mMetrics;
    private static int mMinimumFlingVelocity = 50;
    
    static {
        DOUBLE_EPSILON = Double.longBitsToDouble(1L);
        FLOAT_EPSILON = Float.intBitsToFloat(1);
        Utils.mCalcTextHeightRect = new Rect();
        Utils.mFontMetrics = new Paint$FontMetrics();
        Utils.mCalcTextSizeRect = new Rect();
        POW_10 = new int[] { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
        Utils.mDefaultValueFormatter = generateDefaultValueFormatter();
        Utils.mDrawableBoundsCache = new Rect();
        Utils.mDrawTextRectBuffer = new Rect();
        Utils.mFontMetricsBuffer = new Paint$FontMetrics();
    }
    
    public static int calcTextHeight(final Paint paint, final String s) {
        final Rect mCalcTextHeightRect = Utils.mCalcTextHeightRect;
        mCalcTextHeightRect.set(0, 0, 0, 0);
        paint.getTextBounds(s, 0, s.length(), mCalcTextHeightRect);
        return mCalcTextHeightRect.height();
    }
    
    public static FSize calcTextSize(final Paint paint, final String s) {
        final FSize instance = FSize.getInstance(0.0f, 0.0f);
        calcTextSize(paint, s, instance);
        return instance;
    }
    
    public static void calcTextSize(final Paint paint, final String s, final FSize fSize) {
        final Rect mCalcTextSizeRect = Utils.mCalcTextSizeRect;
        mCalcTextSizeRect.set(0, 0, 0, 0);
        paint.getTextBounds(s, 0, s.length(), mCalcTextSizeRect);
        fSize.width = (float)mCalcTextSizeRect.width();
        fSize.height = (float)mCalcTextSizeRect.height();
    }
    
    public static int calcTextWidth(final Paint paint, final String s) {
        return (int)paint.measureText(s);
    }
    
    public static float convertDpToPixel(final float n) {
        if (Utils.mMetrics == null) {
            Log.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertDpToPixel(...). Otherwise conversion does not take place.");
            return n;
        }
        return n * Utils.mMetrics.density;
    }
    
    public static int[] convertIntegers(final List<Integer> list) {
        final int[] array = new int[list.size()];
        copyIntegers(list, array);
        return array;
    }
    
    public static float convertPixelsToDp(final float n) {
        if (Utils.mMetrics == null) {
            Log.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertPixelsToDp(...). Otherwise conversion does not take place.");
            return n;
        }
        return n / Utils.mMetrics.density;
    }
    
    public static String[] convertStrings(final List<String> list) {
        final String[] array = new String[list.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = list.get(i);
        }
        return array;
    }
    
    public static void copyIntegers(final List<Integer> list, final int[] array) {
        int n;
        if (array.length < list.size()) {
            n = array.length;
        }
        else {
            n = list.size();
        }
        for (int i = 0; i < n; ++i) {
            array[i] = list.get(i);
        }
    }
    
    public static void copyStrings(final List<String> list, final String[] array) {
        int n;
        if (array.length < list.size()) {
            n = array.length;
        }
        else {
            n = list.size();
        }
        for (int i = 0; i < n; ++i) {
            array[i] = list.get(i);
        }
    }
    
    public static void drawImage(final Canvas canvas, final Drawable drawable, int save, final int n, final int n2, final int n3) {
        final MPPointF instance = MPPointF.getInstance();
        instance.x = (float)(save - n2 / 2);
        instance.y = (float)(n - n3 / 2);
        drawable.copyBounds(Utils.mDrawableBoundsCache);
        drawable.setBounds(Utils.mDrawableBoundsCache.left, Utils.mDrawableBoundsCache.top, Utils.mDrawableBoundsCache.left + n2, Utils.mDrawableBoundsCache.top + n2);
        save = canvas.save();
        canvas.translate(instance.x, instance.y);
        drawable.draw(canvas);
        canvas.restoreToCount(save);
    }
    
    public static void drawMultilineText(final Canvas canvas, final StaticLayout staticLayout, final float n, final float n2, final TextPaint textPaint, final MPPointF mpPointF, float n3) {
        final float fontMetrics = textPaint.getFontMetrics(Utils.mFontMetricsBuffer);
        final float n4 = (float)staticLayout.getWidth();
        final float n5 = staticLayout.getLineCount() * fontMetrics;
        final float n6 = 0.0f - Utils.mDrawTextRectBuffer.left;
        final float n7 = 0.0f + n5;
        final Paint$Align textAlign = textPaint.getTextAlign();
        textPaint.setTextAlign(Paint$Align.LEFT);
        if (n3 != 0.0f) {
            float n8 = 0.0f;
            float n9 = 0.0f;
            Label_0150: {
                if (mpPointF.x == 0.5f) {
                    n8 = n;
                    n9 = n2;
                    if (mpPointF.y == 0.5f) {
                        break Label_0150;
                    }
                }
                final FSize sizeOfRotatedRectangleByDegrees = getSizeOfRotatedRectangleByDegrees(n4, n5, n3);
                n8 = n - sizeOfRotatedRectangleByDegrees.width * (mpPointF.x - 0.5f);
                n9 = n2 - sizeOfRotatedRectangleByDegrees.height * (mpPointF.y - 0.5f);
                FSize.recycleInstance(sizeOfRotatedRectangleByDegrees);
            }
            canvas.save();
            canvas.translate(n8, n9);
            canvas.rotate(n3);
            canvas.translate(n6 - n4 * 0.5f, n7 - n5 * 0.5f);
            staticLayout.draw(canvas);
            canvas.restore();
        }
        else {
            float n10 = 0.0f;
            Label_0257: {
                if (mpPointF.x == 0.0f) {
                    n10 = n6;
                    n3 = n7;
                    if (mpPointF.y == 0.0f) {
                        break Label_0257;
                    }
                }
                n10 = n6 - n4 * mpPointF.x;
                n3 = n7 - n5 * mpPointF.y;
            }
            canvas.save();
            canvas.translate(n10 + n, n3 + n2);
            staticLayout.draw(canvas);
            canvas.restore();
        }
        textPaint.setTextAlign(textAlign);
    }
    
    public static void drawMultilineText(final Canvas canvas, final String s, final float n, final float n2, final TextPaint textPaint, final FSize fSize, final MPPointF mpPointF, final float n3) {
        drawMultilineText(canvas, new StaticLayout((CharSequence)s, 0, s.length(), textPaint, (int)Math.max(Math.ceil(fSize.width), 1.0), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false), n, n2, textPaint, mpPointF, n3);
    }
    
    public static void drawXAxisValue(final Canvas canvas, final String s, final float n, final float n2, final Paint paint, final MPPointF mpPointF, float n3) {
        final float fontMetrics = paint.getFontMetrics(Utils.mFontMetricsBuffer);
        paint.getTextBounds(s, 0, s.length(), Utils.mDrawTextRectBuffer);
        final float n4 = 0.0f - Utils.mDrawTextRectBuffer.left;
        final float n5 = -Utils.mFontMetricsBuffer.ascent + 0.0f;
        final Paint$Align textAlign = paint.getTextAlign();
        paint.setTextAlign(Paint$Align.LEFT);
        if (n3 != 0.0f) {
            final float n6 = (float)Utils.mDrawTextRectBuffer.width();
            float n7 = 0.0f;
            float n8 = 0.0f;
            Label_0166: {
                if (mpPointF.x == 0.5f) {
                    n7 = n;
                    n8 = n2;
                    if (mpPointF.y == 0.5f) {
                        break Label_0166;
                    }
                }
                final FSize sizeOfRotatedRectangleByDegrees = getSizeOfRotatedRectangleByDegrees((float)Utils.mDrawTextRectBuffer.width(), fontMetrics, n3);
                n7 = n - sizeOfRotatedRectangleByDegrees.width * (mpPointF.x - 0.5f);
                n8 = n2 - sizeOfRotatedRectangleByDegrees.height * (mpPointF.y - 0.5f);
                FSize.recycleInstance(sizeOfRotatedRectangleByDegrees);
            }
            canvas.save();
            canvas.translate(n7, n8);
            canvas.rotate(n3);
            canvas.drawText(s, n4 - n6 * 0.5f, n5 - fontMetrics * 0.5f, paint);
            canvas.restore();
        }
        else {
            float n9 = 0.0f;
            Label_0276: {
                if (mpPointF.x == 0.0f) {
                    n9 = n4;
                    n3 = n5;
                    if (mpPointF.y == 0.0f) {
                        break Label_0276;
                    }
                }
                n9 = n4 - Utils.mDrawTextRectBuffer.width() * mpPointF.x;
                n3 = n5 - fontMetrics * mpPointF.y;
            }
            canvas.drawText(s, n9 + n, n3 + n2, paint);
        }
        paint.setTextAlign(textAlign);
    }
    
    public static String formatNumber(final float n, final int n2, final boolean b) {
        return formatNumber(n, n2, b, '.');
    }
    
    public static String formatNumber(float n, int offset, final boolean b, final char c) {
        final char[] data = new char[35];
        if (n == 0.0f) {
            return "0";
        }
        final int n2 = 0;
        final boolean b2 = n < 1.0f && n > -1.0f;
        boolean b3;
        if (n < 0.0f) {
            n = -n;
            b3 = true;
        }
        else {
            b3 = false;
        }
        int n3;
        if (offset > Utils.POW_10.length) {
            n3 = Utils.POW_10.length - 1;
        }
        else {
            n3 = offset;
        }
        long n4 = Math.round(n * Utils.POW_10[n3]);
        final int n5 = data.length - 1;
        boolean b4 = false;
        offset = n2;
        int n6 = n5;
        while (n4 != 0L || offset < n3 + 1) {
            final int n7 = (int)(n4 % 10L);
            n4 /= 10L;
            final int n8 = n6 - 1;
            data[n6] = (char)(n7 + 48);
            if (++offset == n3) {
                data[n8] = 44;
                ++offset;
                n6 = n8 - 1;
                b4 = true;
            }
            else {
                if (b && n4 != 0L && offset > n3) {
                    if (b4) {
                        if ((offset - n3) % 4 == 0) {
                            n6 = n8 - 1;
                            data[n8] = c;
                            ++offset;
                            continue;
                        }
                    }
                    else if ((offset - n3) % 4 == 3) {
                        n6 = n8 - 1;
                        data[n8] = c;
                        ++offset;
                        continue;
                    }
                }
                n6 = n8;
            }
        }
        int n9 = n6;
        int n10 = offset;
        if (b2) {
            data[n6] = 48;
            n10 = offset + 1;
            n9 = n6 - 1;
        }
        offset = n10;
        if (b3) {
            data[n9] = 45;
            offset = n10 + 1;
        }
        offset = data.length - offset;
        return String.valueOf(data, offset, data.length - offset);
    }
    
    private static IValueFormatter generateDefaultValueFormatter() {
        return new DefaultValueFormatter(1);
    }
    
    public static int getDecimals(float roundToNextSignificant) {
        roundToNextSignificant = roundToNextSignificant(roundToNextSignificant);
        if (Float.isInfinite(roundToNextSignificant)) {
            return 0;
        }
        return (int)Math.ceil(-Math.log10(roundToNextSignificant)) + 2;
    }
    
    public static IValueFormatter getDefaultValueFormatter() {
        return Utils.mDefaultValueFormatter;
    }
    
    public static float getLineHeight(final Paint paint) {
        return getLineHeight(paint, Utils.mFontMetrics);
    }
    
    public static float getLineHeight(final Paint paint, final Paint$FontMetrics paint$FontMetrics) {
        paint.getFontMetrics(paint$FontMetrics);
        return paint$FontMetrics.descent - paint$FontMetrics.ascent;
    }
    
    public static float getLineSpacing(final Paint paint) {
        return getLineSpacing(paint, Utils.mFontMetrics);
    }
    
    public static float getLineSpacing(final Paint paint, final Paint$FontMetrics paint$FontMetrics) {
        paint.getFontMetrics(paint$FontMetrics);
        return paint$FontMetrics.ascent - paint$FontMetrics.top + paint$FontMetrics.bottom;
    }
    
    public static int getMaximumFlingVelocity() {
        return Utils.mMaximumFlingVelocity;
    }
    
    public static int getMinimumFlingVelocity() {
        return Utils.mMinimumFlingVelocity;
    }
    
    public static float getNormalizedAngle(float n) {
        while (n < 0.0f) {
            n += 360.0f;
        }
        return n % 360.0f;
    }
    
    public static MPPointF getPosition(final MPPointF mpPointF, final float n, final float n2) {
        final MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        getPosition(mpPointF, n, n2, instance);
        return instance;
    }
    
    public static void getPosition(final MPPointF mpPointF, final float n, final float n2, final MPPointF mpPointF2) {
        final double n3 = mpPointF.x;
        final double n4 = n;
        final double n5 = n2;
        mpPointF2.x = (float)(n3 + Math.cos(Math.toRadians(n5)) * n4);
        mpPointF2.y = (float)(mpPointF.y + n4 * Math.sin(Math.toRadians(n5)));
    }
    
    public static int getSDKInt() {
        return Build$VERSION.SDK_INT;
    }
    
    public static FSize getSizeOfRotatedRectangleByDegrees(final float n, final float n2, final float n3) {
        return getSizeOfRotatedRectangleByRadians(n, n2, n3 * 0.017453292f);
    }
    
    public static FSize getSizeOfRotatedRectangleByDegrees(final FSize fSize, final float n) {
        return getSizeOfRotatedRectangleByRadians(fSize.width, fSize.height, n * 0.017453292f);
    }
    
    public static FSize getSizeOfRotatedRectangleByRadians(final float n, final float n2, final float n3) {
        final double n4 = n3;
        return FSize.getInstance(Math.abs((float)Math.cos(n4) * n) + Math.abs((float)Math.sin(n4) * n2), Math.abs(n * (float)Math.sin(n4)) + Math.abs(n2 * (float)Math.cos(n4)));
    }
    
    public static FSize getSizeOfRotatedRectangleByRadians(final FSize fSize, final float n) {
        return getSizeOfRotatedRectangleByRadians(fSize.width, fSize.height, n);
    }
    
    public static void init(final Context context) {
        if (context == null) {
            Utils.mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            Utils.mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
            Log.e("MPChartLib-Utils", "Utils.init(...) PROVIDED CONTEXT OBJECT IS NULL");
        }
        else {
            final ViewConfiguration value = ViewConfiguration.get(context);
            Utils.mMinimumFlingVelocity = value.getScaledMinimumFlingVelocity();
            Utils.mMaximumFlingVelocity = value.getScaledMaximumFlingVelocity();
            Utils.mMetrics = context.getResources().getDisplayMetrics();
        }
    }
    
    @Deprecated
    public static void init(final Resources resources) {
        Utils.mMetrics = resources.getDisplayMetrics();
        Utils.mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
        Utils.mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
    }
    
    public static double nextUp(double n) {
        if (n == Double.POSITIVE_INFINITY) {
            return n;
        }
        n += 0.0;
        final long doubleToRawLongBits = Double.doubleToRawLongBits(n);
        long n2;
        if (n >= 0.0) {
            n2 = 1L;
        }
        else {
            n2 = -1L;
        }
        return Double.longBitsToDouble(doubleToRawLongBits + n2);
    }
    
    @SuppressLint({ "NewApi" })
    public static void postInvalidateOnAnimation(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation();
        }
        else {
            view.postInvalidateDelayed(10L);
        }
    }
    
    public static float roundToNextSignificant(final double n) {
        if (!Double.isInfinite(n) && !Double.isNaN(n) && n != 0.0) {
            double a;
            if (n < 0.0) {
                a = -n;
            }
            else {
                a = n;
            }
            final float n2 = (float)Math.pow(10.0, 1 - (int)(float)Math.ceil((float)Math.log10(a)));
            return Math.round(n * n2) / n2;
        }
        return 0.0f;
    }
    
    public static void velocityTrackerPointerUpCleanUpIfNecessary(final MotionEvent motionEvent, final VelocityTracker velocityTracker) {
        velocityTracker.computeCurrentVelocity(1000, (float)Utils.mMaximumFlingVelocity);
        final int actionIndex = motionEvent.getActionIndex();
        final int pointerId = motionEvent.getPointerId(actionIndex);
        final float xVelocity = velocityTracker.getXVelocity(pointerId);
        final float yVelocity = velocityTracker.getYVelocity(pointerId);
        for (int pointerCount = motionEvent.getPointerCount(), i = 0; i < pointerCount; ++i) {
            if (i != actionIndex) {
                final int pointerId2 = motionEvent.getPointerId(i);
                if (velocityTracker.getXVelocity(pointerId2) * xVelocity + velocityTracker.getYVelocity(pointerId2) * yVelocity < 0.0f) {
                    velocityTracker.clear();
                    break;
                }
            }
        }
    }
}

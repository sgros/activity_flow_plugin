// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.View$MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.graphics.Paint$Align;
import android.view.ViewConfiguration;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.LinearLayout$LayoutParams;
import android.util.TypedValue;
import org.telegram.ui.ActionBar.Theme;
import java.util.Locale;
import android.util.AttributeSet;
import android.content.Context;
import android.view.VelocityTracker;
import android.util.SparseArray;
import android.graphics.Paint;
import android.widget.TextView;
import android.widget.LinearLayout;

public class NumberPicker extends LinearLayout
{
    private static final int DEFAULT_LAYOUT_RESOURCE_ID = 0;
    private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300L;
    private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 800;
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
    private static final int SELECTOR_MIDDLE_ITEM_INDEX = 1;
    private static final int SELECTOR_WHEEL_ITEM_COUNT = 3;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final int SNAP_SCROLL_DURATION = 300;
    private static final float TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9f;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDERS_DISTANCE = 48;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT = 2;
    private Scroller mAdjustScroller;
    private int mBottomSelectionDividerBottom;
    private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
    private boolean mComputeMaxWidth;
    private int mCurrentScrollOffset;
    private boolean mDecrementVirtualButtonPressed;
    private String[] mDisplayedValues;
    private Scroller mFlingScroller;
    private Formatter mFormatter;
    private boolean mIncrementVirtualButtonPressed;
    private boolean mIngonreMoveEvents;
    private int mInitialScrollOffset;
    private TextView mInputText;
    private long mLastDownEventTime;
    private float mLastDownEventY;
    private float mLastDownOrMoveEventY;
    private int mLastHandledDownDpadKeyCode;
    private int mLastHoveredChildVirtualViewId;
    private long mLongPressUpdateInterval;
    private int mMaxHeight;
    private int mMaxValue;
    private int mMaxWidth;
    private int mMaximumFlingVelocity;
    private int mMinHeight;
    private int mMinValue;
    private int mMinWidth;
    private int mMinimumFlingVelocity;
    private OnScrollListener mOnScrollListener;
    private OnValueChangeListener mOnValueChangeListener;
    private PressedStateHelper mPressedStateHelper;
    private int mPreviousScrollerY;
    private int mScrollState;
    private Paint mSelectionDivider;
    private int mSelectionDividerHeight;
    private int mSelectionDividersDistance;
    private int mSelectorElementHeight;
    private final SparseArray<String> mSelectorIndexToStringCache;
    private final int[] mSelectorIndices;
    private int mSelectorTextGapHeight;
    private Paint mSelectorWheelPaint;
    private int mSolidColor;
    private int mTextSize;
    private int mTopSelectionDividerTop;
    private int mTouchSlop;
    private int mValue;
    private VelocityTracker mVelocityTracker;
    private boolean mWrapSelectorWheel;
    
    public NumberPicker(final Context context) {
        super(context);
        this.mLongPressUpdateInterval = 300L;
        this.mSelectorIndexToStringCache = (SparseArray<String>)new SparseArray();
        this.mSelectorIndices = new int[3];
        this.mInitialScrollOffset = Integer.MIN_VALUE;
        this.mScrollState = 0;
        this.mLastHandledDownDpadKeyCode = -1;
        this.init();
    }
    
    public NumberPicker(final Context context, final AttributeSet set) {
        super(context, set);
        this.mLongPressUpdateInterval = 300L;
        this.mSelectorIndexToStringCache = (SparseArray<String>)new SparseArray();
        this.mSelectorIndices = new int[3];
        this.mInitialScrollOffset = Integer.MIN_VALUE;
        this.mScrollState = 0;
        this.mLastHandledDownDpadKeyCode = -1;
        this.init();
    }
    
    public NumberPicker(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mLongPressUpdateInterval = 300L;
        this.mSelectorIndexToStringCache = (SparseArray<String>)new SparseArray();
        this.mSelectorIndices = new int[3];
        this.mInitialScrollOffset = Integer.MIN_VALUE;
        this.mScrollState = 0;
        this.mLastHandledDownDpadKeyCode = -1;
        this.init();
    }
    
    private void changeValueByOne(final boolean b) {
        this.mInputText.setVisibility(4);
        if (!this.moveToFinalScrollerPosition(this.mFlingScroller)) {
            this.moveToFinalScrollerPosition(this.mAdjustScroller);
        }
        this.mPreviousScrollerY = 0;
        if (b) {
            this.mFlingScroller.startScroll(0, 0, 0, -this.mSelectorElementHeight, 300);
        }
        else {
            this.mFlingScroller.startScroll(0, 0, 0, this.mSelectorElementHeight, 300);
        }
        this.invalidate();
    }
    
    private void decrementSelectorIndices(final int[] array) {
        System.arraycopy(array, 0, array, 1, array.length - 1);
        int mMaxValue = array[1] - 1;
        if (this.mWrapSelectorWheel && (mMaxValue = mMaxValue) < this.mMinValue) {
            mMaxValue = this.mMaxValue;
        }
        this.ensureCachedScrollSelectorValue(array[0] = mMaxValue);
    }
    
    private void ensureCachedScrollSelectorValue(final int n) {
        final SparseArray<String> mSelectorIndexToStringCache = this.mSelectorIndexToStringCache;
        if (mSelectorIndexToStringCache.get(n) != null) {
            return;
        }
        final int mMinValue = this.mMinValue;
        String formatNumber;
        if (n >= mMinValue && n <= this.mMaxValue) {
            final String[] mDisplayedValues = this.mDisplayedValues;
            if (mDisplayedValues != null) {
                formatNumber = mDisplayedValues[n - mMinValue];
            }
            else {
                formatNumber = this.formatNumber(n);
            }
        }
        else {
            formatNumber = "";
        }
        mSelectorIndexToStringCache.put(n, (Object)formatNumber);
    }
    
    private boolean ensureScrollWheelAdjusted() {
        final int a = this.mInitialScrollOffset - this.mCurrentScrollOffset;
        if (a != 0) {
            this.mPreviousScrollerY = 0;
            final int abs = Math.abs(a);
            final int mSelectorElementHeight = this.mSelectorElementHeight;
            int n = a;
            if (abs > mSelectorElementHeight / 2) {
                int n2 = mSelectorElementHeight;
                if (a > 0) {
                    n2 = -mSelectorElementHeight;
                }
                n = a + n2;
            }
            this.mAdjustScroller.startScroll(0, 0, 0, n, 800);
            this.invalidate();
            return true;
        }
        return false;
    }
    
    private void fling(final int n) {
        this.mPreviousScrollerY = 0;
        if (n > 0) {
            this.mFlingScroller.fling(0, 0, 0, n, 0, 0, 0, Integer.MAX_VALUE);
        }
        else {
            this.mFlingScroller.fling(0, Integer.MAX_VALUE, 0, n, 0, 0, 0, Integer.MAX_VALUE);
        }
        this.invalidate();
    }
    
    private String formatNumber(final int n) {
        final Formatter mFormatter = this.mFormatter;
        String s;
        if (mFormatter != null) {
            s = mFormatter.format(n);
        }
        else {
            s = formatNumberWithLocale(n);
        }
        return s;
    }
    
    private static String formatNumberWithLocale(final int i) {
        return String.format(Locale.getDefault(), "%d", i);
    }
    
    private int getSelectedPos(String lowerCase) {
        Label_0014: {
            if (this.mDisplayedValues != null) {
                break Label_0014;
            }
            try {
                return Integer.parseInt(lowerCase);
                int n = 0;
                Label_0053: {
                    ++n;
                }
                Label_0016: {
                    break Label_0016;
                    Label_0059:
                    return Integer.parseInt(lowerCase);
                    n = 0;
                }
                // iftrue(Label_0059:, n >= this.mDisplayedValues.length)
                lowerCase = lowerCase.toLowerCase();
                // iftrue(Label_0053:, !this.mDisplayedValues[n].toLowerCase().startsWith(lowerCase))
                return this.mMinValue + n;
            }
            catch (NumberFormatException ex) {
                return this.mMinValue;
            }
        }
    }
    
    private int getWrappedSelectorIndex(final int n) {
        final int mMaxValue = this.mMaxValue;
        if (n > mMaxValue) {
            final int mMinValue = this.mMinValue;
            return mMinValue + (n - mMaxValue) % (mMaxValue - mMinValue) - 1;
        }
        final int mMinValue2 = this.mMinValue;
        if (n < mMinValue2) {
            return mMaxValue - (mMinValue2 - n) % (mMaxValue - mMinValue2) + 1;
        }
        return n;
    }
    
    private void incrementSelectorIndices(final int[] array) {
        System.arraycopy(array, 1, array, 0, array.length - 1);
        int mMinValue = array[array.length - 2] + 1;
        if (this.mWrapSelectorWheel && (mMinValue = mMinValue) > this.mMaxValue) {
            mMinValue = this.mMinValue;
        }
        this.ensureCachedScrollSelectorValue(array[array.length - 1] = mMinValue);
    }
    
    private void init() {
        this.mSolidColor = 0;
        (this.mSelectionDivider = new Paint()).setColor(Theme.getColor("dialogButton"));
        this.mSelectionDividerHeight = (int)TypedValue.applyDimension(1, 2.0f, this.getResources().getDisplayMetrics());
        this.mSelectionDividersDistance = (int)TypedValue.applyDimension(1, 48.0f, this.getResources().getDisplayMetrics());
        this.mMinHeight = -1;
        this.mMaxHeight = (int)TypedValue.applyDimension(1, 180.0f, this.getResources().getDisplayMetrics());
        final int mMinHeight = this.mMinHeight;
        if (mMinHeight != -1) {
            final int mMaxHeight = this.mMaxHeight;
            if (mMaxHeight != -1) {
                if (mMinHeight > mMaxHeight) {
                    throw new IllegalArgumentException("minHeight > maxHeight");
                }
            }
        }
        this.mMinWidth = (int)TypedValue.applyDimension(1, 64.0f, this.getResources().getDisplayMetrics());
        this.mMaxWidth = -1;
        final int mMinWidth = this.mMinWidth;
        if (mMinWidth != -1) {
            final int mMaxWidth = this.mMaxWidth;
            if (mMaxWidth != -1) {
                if (mMinWidth > mMaxWidth) {
                    throw new IllegalArgumentException("minWidth > maxWidth");
                }
            }
        }
        this.mComputeMaxWidth = (this.mMaxWidth == -1);
        this.mPressedStateHelper = new PressedStateHelper();
        this.setWillNotDraw(false);
        (this.mInputText = new TextView(this.getContext())).setGravity(17);
        this.mInputText.setSingleLine(true);
        this.mInputText.setTextColor(Theme.getColor("dialogTextBlack"));
        this.mInputText.setBackgroundResource(0);
        this.mInputText.setTextSize(1, 18.0f);
        this.addView((View)this.mInputText, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -2));
        final ViewConfiguration value = ViewConfiguration.get(this.getContext());
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mMinimumFlingVelocity = value.getScaledMinimumFlingVelocity();
        this.mMaximumFlingVelocity = value.getScaledMaximumFlingVelocity() / 8;
        this.mTextSize = (int)this.mInputText.getTextSize();
        final Paint mSelectorWheelPaint = new Paint();
        mSelectorWheelPaint.setAntiAlias(true);
        mSelectorWheelPaint.setTextAlign(Paint$Align.CENTER);
        mSelectorWheelPaint.setTextSize((float)this.mTextSize);
        mSelectorWheelPaint.setTypeface(this.mInputText.getTypeface());
        mSelectorWheelPaint.setColor(this.mInputText.getTextColors().getColorForState(LinearLayout.ENABLED_STATE_SET, -1));
        this.mSelectorWheelPaint = mSelectorWheelPaint;
        this.mFlingScroller = new Scroller(this.getContext(), null, true);
        this.mAdjustScroller = new Scroller(this.getContext(), (Interpolator)new DecelerateInterpolator(2.5f));
        this.updateInputTextView();
    }
    
    private void initializeFadingEdges() {
        this.setVerticalFadingEdgeEnabled(true);
        this.setFadingEdgeLength((this.getBottom() - this.getTop() - this.mTextSize) / 2);
    }
    
    private void initializeSelectorWheel() {
        this.initializeSelectorWheelIndices();
        final int[] mSelectorIndices = this.mSelectorIndices;
        this.mSelectorTextGapHeight = (int)((this.getBottom() - this.getTop() - mSelectorIndices.length * this.mTextSize) / (float)mSelectorIndices.length + 0.5f);
        this.mSelectorElementHeight = this.mTextSize + this.mSelectorTextGapHeight;
        this.mInitialScrollOffset = this.mInputText.getBaseline() + this.mInputText.getTop() - this.mSelectorElementHeight * 1;
        this.mCurrentScrollOffset = this.mInitialScrollOffset;
        this.updateInputTextView();
    }
    
    private void initializeSelectorWheelIndices() {
        this.mSelectorIndexToStringCache.clear();
        final int[] mSelectorIndices = this.mSelectorIndices;
        final int value = this.getValue();
        for (int i = 0; i < this.mSelectorIndices.length; ++i) {
            int wrappedSelectorIndex = i - 1 + value;
            if (this.mWrapSelectorWheel) {
                wrappedSelectorIndex = this.getWrappedSelectorIndex(wrappedSelectorIndex);
            }
            this.ensureCachedScrollSelectorValue(mSelectorIndices[i] = wrappedSelectorIndex);
        }
    }
    
    private int makeMeasureSpec(final int n, final int b) {
        if (b == -1) {
            return n;
        }
        final int size = View$MeasureSpec.getSize(n);
        final int mode = View$MeasureSpec.getMode(n);
        if (mode == Integer.MIN_VALUE) {
            return View$MeasureSpec.makeMeasureSpec(Math.min(size, b), 1073741824);
        }
        if (mode == 0) {
            return View$MeasureSpec.makeMeasureSpec(b, 1073741824);
        }
        if (mode == 1073741824) {
            return n;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown measure mode: ");
        sb.append(mode);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private boolean moveToFinalScrollerPosition(final Scroller scroller) {
        scroller.forceFinished(true);
        final int n = scroller.getFinalY() - scroller.getCurrY();
        final int a = this.mInitialScrollOffset - (this.mCurrentScrollOffset + n) % this.mSelectorElementHeight;
        if (a != 0) {
            final int abs = Math.abs(a);
            final int mSelectorElementHeight = this.mSelectorElementHeight;
            int n2 = a;
            if (abs > mSelectorElementHeight / 2) {
                if (a > 0) {
                    n2 = a - mSelectorElementHeight;
                }
                else {
                    n2 = a + mSelectorElementHeight;
                }
            }
            this.scrollBy(0, n + n2);
            return true;
        }
        return false;
    }
    
    private void notifyChange(final int n, final int n2) {
        final OnValueChangeListener mOnValueChangeListener = this.mOnValueChangeListener;
        if (mOnValueChangeListener != null) {
            mOnValueChangeListener.onValueChange(this, n, this.mValue);
        }
    }
    
    private void onScrollStateChange(final int mScrollState) {
        if (this.mScrollState == mScrollState) {
            return;
        }
        this.mScrollState = mScrollState;
        final OnScrollListener mOnScrollListener = this.mOnScrollListener;
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChange(this, mScrollState);
        }
        if (mScrollState == 0) {
            final AccessibilityManager accessibilityManager = (AccessibilityManager)this.getContext().getSystemService("accessibility");
            if (accessibilityManager.isTouchExplorationEnabled()) {
                final String[] mDisplayedValues = this.mDisplayedValues;
                String formatNumber;
                if (mDisplayedValues == null) {
                    formatNumber = this.formatNumber(this.mValue);
                }
                else {
                    formatNumber = mDisplayedValues[this.mValue - this.mMinValue];
                }
                final AccessibilityEvent obtain = AccessibilityEvent.obtain();
                obtain.setEventType(16384);
                obtain.getText().add(formatNumber);
                accessibilityManager.sendAccessibilityEvent(obtain);
            }
        }
    }
    
    private void onScrollerFinished(final Scroller scroller) {
        if (scroller == this.mFlingScroller) {
            if (!this.ensureScrollWheelAdjusted()) {
                this.updateInputTextView();
            }
            this.onScrollStateChange(0);
        }
        else if (this.mScrollState != 1) {
            this.updateInputTextView();
        }
    }
    
    private void postChangeCurrentByOneFromLongPress(final boolean b, final long n) {
        final ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (mChangeCurrentByOneFromLongPressCommand == null) {
            this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        }
        else {
            this.removeCallbacks((Runnable)mChangeCurrentByOneFromLongPressCommand);
        }
        this.mChangeCurrentByOneFromLongPressCommand.setStep(b);
        this.postDelayed((Runnable)this.mChangeCurrentByOneFromLongPressCommand, n);
    }
    
    private void removeAllCallbacks() {
        final ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (mChangeCurrentByOneFromLongPressCommand != null) {
            this.removeCallbacks((Runnable)mChangeCurrentByOneFromLongPressCommand);
        }
        this.mPressedStateHelper.cancel();
    }
    
    private void removeChangeCurrentByOneFromLongPress() {
        final ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (mChangeCurrentByOneFromLongPressCommand != null) {
            this.removeCallbacks((Runnable)mChangeCurrentByOneFromLongPressCommand);
        }
    }
    
    public static int resolveSizeAndState(final int n, int n2, final int n3) {
        final int mode = View$MeasureSpec.getMode(n2);
        final int size = View$MeasureSpec.getSize(n2);
        if (mode != Integer.MIN_VALUE) {
            n2 = n;
            if (mode != 0) {
                if (mode != 1073741824) {
                    n2 = n;
                }
                else {
                    n2 = size;
                }
            }
        }
        else if (size < (n2 = n)) {
            n2 = (0x1000000 | size);
        }
        return n2 | (0xFF000000 & n3);
    }
    
    private int resolveSizeAndStateRespectingMinSize(final int a, final int b, final int n) {
        if (a != -1) {
            return resolveSizeAndState(Math.max(a, b), n, 0);
        }
        return b;
    }
    
    private void setValueInternal(int n, final boolean b) {
        if (this.mValue == n) {
            return;
        }
        if (this.mWrapSelectorWheel) {
            n = this.getWrappedSelectorIndex(n);
        }
        else {
            n = Math.min(Math.max(n, this.mMinValue), this.mMaxValue);
        }
        final int mValue = this.mValue;
        this.mValue = n;
        this.updateInputTextView();
        if (b) {
            this.notifyChange(mValue, n);
        }
        this.initializeSelectorWheelIndices();
        this.invalidate();
    }
    
    private void tryComputeMaxWidth() {
        if (!this.mComputeMaxWidth) {
            return;
        }
        final String[] mDisplayedValues = this.mDisplayedValues;
        int i = 0;
        int n = 0;
        int n4;
        if (mDisplayedValues == null) {
            int j = 0;
            float n2 = 0.0f;
            while (j <= 9) {
                final float measureText = this.mSelectorWheelPaint.measureText(formatNumberWithLocale(j));
                float n3 = n2;
                if (measureText > n2) {
                    n3 = measureText;
                }
                ++j;
                n2 = n3;
            }
            for (int k = this.mMaxValue; k > 0; k /= 10) {
                ++n;
            }
            n4 = (int)(n * n2);
        }
        else {
            final int length = mDisplayedValues.length;
            n4 = 0;
            while (i < length) {
                final float measureText2 = this.mSelectorWheelPaint.measureText(mDisplayedValues[i]);
                int n5 = n4;
                if (measureText2 > n4) {
                    n5 = (int)measureText2;
                }
                ++i;
                n4 = n5;
            }
        }
        final int mMaxWidth = n4 + (this.mInputText.getPaddingLeft() + this.mInputText.getPaddingRight());
        if (this.mMaxWidth != mMaxWidth) {
            final int mMinWidth = this.mMinWidth;
            if (mMaxWidth > mMinWidth) {
                this.mMaxWidth = mMaxWidth;
            }
            else {
                this.mMaxWidth = mMinWidth;
            }
            this.invalidate();
        }
    }
    
    private boolean updateInputTextView() {
        final String[] mDisplayedValues = this.mDisplayedValues;
        String formatNumber;
        if (mDisplayedValues == null) {
            formatNumber = this.formatNumber(this.mValue);
        }
        else {
            formatNumber = mDisplayedValues[this.mValue - this.mMinValue];
        }
        if (!TextUtils.isEmpty((CharSequence)formatNumber) && !formatNumber.equals(this.mInputText.getText().toString())) {
            this.mInputText.setText((CharSequence)formatNumber);
            return true;
        }
        return false;
    }
    
    public void computeScroll() {
        Scroller scroller;
        if ((scroller = this.mFlingScroller).isFinished() && (scroller = this.mAdjustScroller).isFinished()) {
            return;
        }
        scroller.computeScrollOffset();
        final int currY = scroller.getCurrY();
        if (this.mPreviousScrollerY == 0) {
            this.mPreviousScrollerY = scroller.getStartY();
        }
        this.scrollBy(0, currY - this.mPreviousScrollerY);
        this.mPreviousScrollerY = currY;
        if (scroller.isFinished()) {
            this.onScrollerFinished(scroller);
        }
        else {
            this.invalidate();
        }
    }
    
    protected int computeVerticalScrollExtent() {
        return this.getHeight();
    }
    
    protected int computeVerticalScrollOffset() {
        return this.mCurrentScrollOffset;
    }
    
    protected int computeVerticalScrollRange() {
        return (this.mMaxValue - this.mMinValue + 1) * this.mSelectorElementHeight;
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        final int keyCode = keyEvent.getKeyCode();
        if (keyCode != 19 && keyCode != 20) {
            if (keyCode == 23 || keyCode == 66) {
                this.removeAllCallbacks();
            }
        }
        else {
            final int action = keyEvent.getAction();
            if (action == 0) {
                if (!this.mWrapSelectorWheel && keyCode != 20) {
                    if (this.getValue() <= this.getMinValue()) {
                        return super.dispatchKeyEvent(keyEvent);
                    }
                }
                else if (this.getValue() >= this.getMaxValue()) {
                    return super.dispatchKeyEvent(keyEvent);
                }
                this.requestFocus();
                this.mLastHandledDownDpadKeyCode = keyCode;
                this.removeAllCallbacks();
                if (this.mFlingScroller.isFinished()) {
                    this.changeValueByOne(keyCode == 20);
                }
                return true;
            }
            if (action == 1) {
                if (this.mLastHandledDownDpadKeyCode == keyCode) {
                    this.mLastHandledDownDpadKeyCode = -1;
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }
    
    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            this.removeAllCallbacks();
        }
        return super.dispatchTouchEvent(motionEvent);
    }
    
    public boolean dispatchTrackballEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            this.removeAllCallbacks();
        }
        return super.dispatchTrackballEvent(motionEvent);
    }
    
    public void finishScroll() {
        if (!this.mFlingScroller.isFinished() || !this.mAdjustScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
            this.invalidate();
        }
    }
    
    protected float getBottomFadingEdgeStrength() {
        return 0.9f;
    }
    
    public String[] getDisplayedValues() {
        return this.mDisplayedValues;
    }
    
    public int getMaxValue() {
        return this.mMaxValue;
    }
    
    public int getMinValue() {
        return this.mMinValue;
    }
    
    public int getSolidColor() {
        return this.mSolidColor;
    }
    
    protected float getTopFadingEdgeStrength() {
        return 0.9f;
    }
    
    public int getValue() {
        return this.mValue;
    }
    
    public boolean getWrapSelectorWheel() {
        return this.mWrapSelectorWheel;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeAllCallbacks();
    }
    
    protected void onDraw(final Canvas canvas) {
        final float n = (float)((this.getRight() - this.getLeft()) / 2);
        float n2 = (float)this.mCurrentScrollOffset;
        final int[] mSelectorIndices = this.mSelectorIndices;
        for (int i = 0; i < mSelectorIndices.length; ++i) {
            final String s = (String)this.mSelectorIndexToStringCache.get(mSelectorIndices[i]);
            if (i != 1 || this.mInputText.getVisibility() != 0) {
                canvas.drawText(s, n, n2, this.mSelectorWheelPaint);
            }
            n2 += this.mSelectorElementHeight;
        }
        final int mTopSelectionDividerTop = this.mTopSelectionDividerTop;
        canvas.drawRect(0.0f, (float)mTopSelectionDividerTop, (float)this.getRight(), (float)(this.mSelectionDividerHeight + mTopSelectionDividerTop), this.mSelectionDivider);
        final int mBottomSelectionDividerBottom = this.mBottomSelectionDividerBottom;
        canvas.drawRect(0.0f, (float)(mBottomSelectionDividerBottom - this.mSelectionDividerHeight), (float)this.getRight(), (float)mBottomSelectionDividerBottom, this.mSelectionDivider);
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        if (!this.isEnabled()) {
            return false;
        }
        if (motionEvent.getActionMasked() != 0) {
            return false;
        }
        this.removeAllCallbacks();
        this.mInputText.setVisibility(4);
        final float y = motionEvent.getY();
        this.mLastDownEventY = y;
        this.mLastDownOrMoveEventY = y;
        this.mLastDownEventTime = motionEvent.getEventTime();
        this.mIngonreMoveEvents = false;
        final float mLastDownEventY = this.mLastDownEventY;
        if (mLastDownEventY < this.mTopSelectionDividerTop) {
            if (this.mScrollState == 0) {
                this.mPressedStateHelper.buttonPressDelayed(2);
            }
        }
        else if (mLastDownEventY > this.mBottomSelectionDividerBottom && this.mScrollState == 0) {
            this.mPressedStateHelper.buttonPressDelayed(1);
        }
        this.getParent().requestDisallowInterceptTouchEvent(true);
        if (!this.mFlingScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
            this.onScrollStateChange(0);
        }
        else if (!this.mAdjustScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
        }
        else {
            final float mLastDownEventY2 = this.mLastDownEventY;
            if (mLastDownEventY2 < this.mTopSelectionDividerTop) {
                this.postChangeCurrentByOneFromLongPress(false, ViewConfiguration.getLongPressTimeout());
            }
            else if (mLastDownEventY2 > this.mBottomSelectionDividerBottom) {
                this.postChangeCurrentByOneFromLongPress(true, ViewConfiguration.getLongPressTimeout());
            }
        }
        return true;
    }
    
    protected void onLayout(final boolean b, int n, int n2, int measuredHeight, int measuredWidth) {
        measuredWidth = this.getMeasuredWidth();
        measuredHeight = this.getMeasuredHeight();
        n2 = this.mInputText.getMeasuredWidth();
        n = this.mInputText.getMeasuredHeight();
        measuredWidth = (measuredWidth - n2) / 2;
        measuredHeight = (measuredHeight - n) / 2;
        this.mInputText.layout(measuredWidth, measuredHeight, n2 + measuredWidth, n + measuredHeight);
        if (b) {
            this.initializeSelectorWheel();
            this.initializeFadingEdges();
            n2 = this.getHeight();
            n = this.mSelectionDividersDistance;
            measuredHeight = (n2 - n) / 2;
            n2 = this.mSelectionDividerHeight;
            this.mTopSelectionDividerTop = measuredHeight - n2;
            this.mBottomSelectionDividerBottom = this.mTopSelectionDividerTop + n2 * 2 + n;
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(this.makeMeasureSpec(n, this.mMaxWidth), this.makeMeasureSpec(n2, this.mMaxHeight));
        this.setMeasuredDimension(this.resolveSizeAndStateRespectingMinSize(this.mMinWidth, this.getMeasuredWidth(), n), this.resolveSizeAndStateRespectingMinSize(this.mMinHeight, this.getMeasuredHeight(), n2));
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (!this.isEnabled()) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                if (!this.mIngonreMoveEvents) {
                    final float y = motionEvent.getY();
                    if (this.mScrollState != 1) {
                        if ((int)Math.abs(y - this.mLastDownEventY) > this.mTouchSlop) {
                            this.removeAllCallbacks();
                            this.onScrollStateChange(1);
                        }
                    }
                    else {
                        this.scrollBy(0, (int)(y - this.mLastDownOrMoveEventY));
                        this.invalidate();
                    }
                    this.mLastDownOrMoveEventY = y;
                }
            }
        }
        else {
            this.removeChangeCurrentByOneFromLongPress();
            this.mPressedStateHelper.cancel();
            final VelocityTracker mVelocityTracker = this.mVelocityTracker;
            mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
            final int a = (int)mVelocityTracker.getYVelocity();
            if (Math.abs(a) > this.mMinimumFlingVelocity) {
                this.fling(a);
                this.onScrollStateChange(2);
            }
            else {
                final int n = (int)motionEvent.getY();
                final int n2 = (int)Math.abs(n - this.mLastDownEventY);
                final long eventTime = motionEvent.getEventTime();
                final long mLastDownEventTime = this.mLastDownEventTime;
                if (n2 <= this.mTouchSlop && eventTime - mLastDownEventTime < ViewConfiguration.getTapTimeout()) {
                    final int n3 = n / this.mSelectorElementHeight - 1;
                    if (n3 > 0) {
                        this.changeValueByOne(true);
                        this.mPressedStateHelper.buttonTapped(1);
                    }
                    else if (n3 < 0) {
                        this.changeValueByOne(false);
                        this.mPressedStateHelper.buttonTapped(2);
                    }
                }
                else {
                    this.ensureScrollWheelAdjusted();
                }
                this.onScrollStateChange(0);
            }
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        return true;
    }
    
    public void scrollBy(int n, final int n2) {
        final int[] mSelectorIndices = this.mSelectorIndices;
        if (!this.mWrapSelectorWheel && n2 > 0 && mSelectorIndices[1] <= this.mMinValue) {
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
            return;
        }
        if (!this.mWrapSelectorWheel && n2 < 0 && mSelectorIndices[1] >= this.mMaxValue) {
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
            return;
        }
        this.mCurrentScrollOffset += n2;
        while (true) {
            n = this.mCurrentScrollOffset;
            if (n - this.mInitialScrollOffset <= this.mSelectorTextGapHeight) {
                break;
            }
            this.mCurrentScrollOffset = n - this.mSelectorElementHeight;
            this.decrementSelectorIndices(mSelectorIndices);
            this.setValueInternal(mSelectorIndices[1], true);
            if (this.mWrapSelectorWheel || mSelectorIndices[1] > this.mMinValue) {
                continue;
            }
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        }
        while (true) {
            n = this.mCurrentScrollOffset;
            if (n - this.mInitialScrollOffset >= -this.mSelectorTextGapHeight) {
                break;
            }
            this.mCurrentScrollOffset = n + this.mSelectorElementHeight;
            this.incrementSelectorIndices(mSelectorIndices);
            this.setValueInternal(mSelectorIndices[1], true);
            if (this.mWrapSelectorWheel || mSelectorIndices[1] < this.mMaxValue) {
                continue;
            }
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        }
    }
    
    public void setDisplayedValues(final String[] mDisplayedValues) {
        if (this.mDisplayedValues == mDisplayedValues) {
            return;
        }
        this.mDisplayedValues = mDisplayedValues;
        this.updateInputTextView();
        this.initializeSelectorWheelIndices();
        this.tryComputeMaxWidth();
    }
    
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        this.mInputText.setEnabled(b);
    }
    
    public void setFormatter(final Formatter mFormatter) {
        if (mFormatter == this.mFormatter) {
            return;
        }
        this.mFormatter = mFormatter;
        this.initializeSelectorWheelIndices();
        this.updateInputTextView();
    }
    
    public void setMaxValue(int mMaxValue) {
        if (this.mMaxValue == mMaxValue) {
            return;
        }
        if (mMaxValue >= 0) {
            this.mMaxValue = mMaxValue;
            mMaxValue = this.mMaxValue;
            if (mMaxValue < this.mValue) {
                this.mValue = mMaxValue;
            }
            this.setWrapSelectorWheel(this.mMaxValue - this.mMinValue > this.mSelectorIndices.length);
            this.initializeSelectorWheelIndices();
            this.updateInputTextView();
            this.tryComputeMaxWidth();
            this.invalidate();
            return;
        }
        throw new IllegalArgumentException("maxValue must be >= 0");
    }
    
    public void setMinValue(int mMinValue) {
        if (this.mMinValue == mMinValue) {
            return;
        }
        if (mMinValue >= 0) {
            this.mMinValue = mMinValue;
            mMinValue = this.mMinValue;
            if (mMinValue > this.mValue) {
                this.mValue = mMinValue;
            }
            this.setWrapSelectorWheel(this.mMaxValue - this.mMinValue > this.mSelectorIndices.length);
            this.initializeSelectorWheelIndices();
            this.updateInputTextView();
            this.tryComputeMaxWidth();
            this.invalidate();
            return;
        }
        throw new IllegalArgumentException("minValue must be >= 0");
    }
    
    public void setOnLongPressUpdateInterval(final long mLongPressUpdateInterval) {
        this.mLongPressUpdateInterval = mLongPressUpdateInterval;
    }
    
    public void setOnScrollListener(final OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }
    
    public void setOnValueChangedListener(final OnValueChangeListener mOnValueChangeListener) {
        this.mOnValueChangeListener = mOnValueChangeListener;
    }
    
    public void setSelectorColor(final int color) {
        this.mSelectionDivider.setColor(color);
    }
    
    public void setTextColor(final int n) {
        this.mInputText.setTextColor(n);
        this.mSelectorWheelPaint.setColor(n);
    }
    
    public void setValue(final int n) {
        this.setValueInternal(n, false);
    }
    
    public void setWrapSelectorWheel(final boolean mWrapSelectorWheel) {
        final boolean b = this.mMaxValue - this.mMinValue >= this.mSelectorIndices.length;
        if ((!mWrapSelectorWheel || b) && mWrapSelectorWheel != this.mWrapSelectorWheel) {
            this.mWrapSelectorWheel = mWrapSelectorWheel;
        }
    }
    
    class ChangeCurrentByOneFromLongPressCommand implements Runnable
    {
        private boolean mIncrement;
        
        private void setStep(final boolean mIncrement) {
            this.mIncrement = mIncrement;
        }
        
        @Override
        public void run() {
            NumberPicker.this.changeValueByOne(this.mIncrement);
            final NumberPicker this$0 = NumberPicker.this;
            this$0.postDelayed((Runnable)this, this$0.mLongPressUpdateInterval);
        }
    }
    
    public interface Formatter
    {
        String format(final int p0);
    }
    
    public interface OnScrollListener
    {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;
        
        void onScrollStateChange(final NumberPicker p0, final int p1);
    }
    
    public interface OnValueChangeListener
    {
        void onValueChange(final NumberPicker p0, final int p1, final int p2);
    }
    
    class PressedStateHelper implements Runnable
    {
        public static final int BUTTON_DECREMENT = 2;
        public static final int BUTTON_INCREMENT = 1;
        private final int MODE_PRESS;
        private final int MODE_TAPPED;
        private int mManagedButton;
        private int mMode;
        
        PressedStateHelper() {
            this.MODE_PRESS = 1;
            this.MODE_TAPPED = 2;
        }
        
        public void buttonPressDelayed(final int mManagedButton) {
            this.cancel();
            this.mMode = 1;
            this.mManagedButton = mManagedButton;
            NumberPicker.this.postDelayed((Runnable)this, (long)ViewConfiguration.getTapTimeout());
        }
        
        public void buttonTapped(final int mManagedButton) {
            this.cancel();
            this.mMode = 2;
            this.mManagedButton = mManagedButton;
            NumberPicker.this.post((Runnable)this);
        }
        
        public void cancel() {
            this.mMode = 0;
            this.mManagedButton = 0;
            NumberPicker.this.removeCallbacks((Runnable)this);
            if (NumberPicker.this.mIncrementVirtualButtonPressed) {
                NumberPicker.this.mIncrementVirtualButtonPressed = false;
                final NumberPicker this$0 = NumberPicker.this;
                this$0.invalidate(0, this$0.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
            }
            NumberPicker.this.mDecrementVirtualButtonPressed = false;
            if (NumberPicker.this.mDecrementVirtualButtonPressed) {
                final NumberPicker this$2 = NumberPicker.this;
                this$2.invalidate(0, 0, this$2.getRight(), NumberPicker.this.mTopSelectionDividerTop);
            }
        }
        
        @Override
        public void run() {
            final int mMode = this.mMode;
            if (mMode != 1) {
                if (mMode == 2) {
                    final int mManagedButton = this.mManagedButton;
                    if (mManagedButton != 1) {
                        if (mManagedButton == 2) {
                            if (!NumberPicker.this.mDecrementVirtualButtonPressed) {
                                NumberPicker.this.postDelayed((Runnable)this, (long)ViewConfiguration.getPressedStateDuration());
                            }
                            final NumberPicker this$0 = NumberPicker.this;
                            this$0.mDecrementVirtualButtonPressed ^= true;
                            final NumberPicker this$2 = NumberPicker.this;
                            this$2.invalidate(0, 0, this$2.getRight(), NumberPicker.this.mTopSelectionDividerTop);
                        }
                    }
                    else {
                        if (!NumberPicker.this.mIncrementVirtualButtonPressed) {
                            NumberPicker.this.postDelayed((Runnable)this, (long)ViewConfiguration.getPressedStateDuration());
                        }
                        final NumberPicker this$3 = NumberPicker.this;
                        this$3.mIncrementVirtualButtonPressed ^= true;
                        final NumberPicker this$4 = NumberPicker.this;
                        this$4.invalidate(0, this$4.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
                    }
                }
            }
            else {
                final int mManagedButton2 = this.mManagedButton;
                if (mManagedButton2 != 1) {
                    if (mManagedButton2 == 2) {
                        NumberPicker.this.mDecrementVirtualButtonPressed = true;
                        final NumberPicker this$5 = NumberPicker.this;
                        this$5.invalidate(0, 0, this$5.getRight(), NumberPicker.this.mTopSelectionDividerTop);
                    }
                }
                else {
                    NumberPicker.this.mIncrementVirtualButtonPressed = true;
                    final NumberPicker this$6 = NumberPicker.this;
                    this$6.invalidate(0, this$6.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
                }
            }
        }
    }
}

package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class Legend extends ComponentBase {
    private List<Boolean> mCalculatedLabelBreakPoints;
    private List<FSize> mCalculatedLabelSizes;
    private List<FSize> mCalculatedLineSizes;
    private LegendDirection mDirection;
    private boolean mDrawInside;
    private LegendEntry[] mEntries;
    private LegendEntry[] mExtraEntries;
    private DashPathEffect mFormLineDashEffect;
    private float mFormLineWidth;
    private float mFormSize;
    private float mFormToTextSpace;
    private LegendHorizontalAlignment mHorizontalAlignment;
    private boolean mIsLegendCustom;
    private float mMaxSizePercent;
    public float mNeededHeight;
    public float mNeededWidth;
    private LegendOrientation mOrientation;
    private LegendForm mShape;
    private float mStackSpace;
    public float mTextHeightMax;
    public float mTextWidthMax;
    private LegendVerticalAlignment mVerticalAlignment;
    private boolean mWordWrapEnabled;
    private float mXEntrySpace;
    private float mYEntrySpace;

    public enum LegendDirection {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    public enum LegendForm {
        NONE,
        EMPTY,
        DEFAULT,
        SQUARE,
        CIRCLE,
        LINE
    }

    public enum LegendHorizontalAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum LegendOrientation {
        HORIZONTAL,
        VERTICAL
    }

    @Deprecated
    public enum LegendPosition {
        RIGHT_OF_CHART,
        RIGHT_OF_CHART_CENTER,
        RIGHT_OF_CHART_INSIDE,
        LEFT_OF_CHART,
        LEFT_OF_CHART_CENTER,
        LEFT_OF_CHART_INSIDE,
        BELOW_CHART_LEFT,
        BELOW_CHART_RIGHT,
        BELOW_CHART_CENTER,
        ABOVE_CHART_LEFT,
        ABOVE_CHART_RIGHT,
        ABOVE_CHART_CENTER,
        PIECHART_CENTER
    }

    public enum LegendVerticalAlignment {
        TOP,
        CENTER,
        BOTTOM
    }

    public Legend() {
        this.mEntries = new LegendEntry[0];
        this.mIsLegendCustom = false;
        this.mHorizontalAlignment = LegendHorizontalAlignment.LEFT;
        this.mVerticalAlignment = LegendVerticalAlignment.BOTTOM;
        this.mOrientation = LegendOrientation.HORIZONTAL;
        this.mDrawInside = false;
        this.mDirection = LegendDirection.LEFT_TO_RIGHT;
        this.mShape = LegendForm.SQUARE;
        this.mFormSize = 8.0f;
        this.mFormLineWidth = 3.0f;
        this.mFormLineDashEffect = null;
        this.mXEntrySpace = 6.0f;
        this.mYEntrySpace = 0.0f;
        this.mFormToTextSpace = 5.0f;
        this.mStackSpace = 3.0f;
        this.mMaxSizePercent = 0.95f;
        this.mNeededWidth = 0.0f;
        this.mNeededHeight = 0.0f;
        this.mTextHeightMax = 0.0f;
        this.mTextWidthMax = 0.0f;
        this.mWordWrapEnabled = false;
        this.mCalculatedLabelSizes = new ArrayList(16);
        this.mCalculatedLabelBreakPoints = new ArrayList(16);
        this.mCalculatedLineSizes = new ArrayList(16);
        this.mTextSize = Utils.convertDpToPixel(10.0f);
        this.mXOffset = Utils.convertDpToPixel(5.0f);
        this.mYOffset = Utils.convertDpToPixel(3.0f);
    }

    public Legend(LegendEntry[] legendEntryArr) {
        this();
        if (legendEntryArr == null) {
            throw new IllegalArgumentException("entries array is NULL");
        }
        this.mEntries = legendEntryArr;
    }

    @Deprecated
    public Legend(int[] iArr, String[] strArr) {
        this();
        if (iArr == null || strArr == null) {
            throw new IllegalArgumentException("colors array or labels array is NULL");
        } else if (iArr.length != strArr.length) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
        } else {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < Math.min(iArr.length, strArr.length); i++) {
                LegendEntry legendEntry = new LegendEntry();
                legendEntry.formColor = iArr[i];
                legendEntry.label = strArr[i];
                if (legendEntry.formColor == ColorTemplate.COLOR_SKIP) {
                    legendEntry.form = LegendForm.NONE;
                } else if (legendEntry.formColor == ColorTemplate.COLOR_NONE || legendEntry.formColor == 0) {
                    legendEntry.form = LegendForm.EMPTY;
                }
                arrayList.add(legendEntry);
            }
            this.mEntries = (LegendEntry[]) arrayList.toArray(new LegendEntry[arrayList.size()]);
        }
    }

    @Deprecated
    public Legend(List<Integer> list, List<String> list2) {
        this(Utils.convertIntegers(list), Utils.convertStrings(list2));
    }

    public void setEntries(List<LegendEntry> list) {
        this.mEntries = (LegendEntry[]) list.toArray(new LegendEntry[list.size()]);
    }

    public LegendEntry[] getEntries() {
        return this.mEntries;
    }

    public float getMaximumEntryWidth(Paint paint) {
        float convertDpToPixel = Utils.convertDpToPixel(this.mFormToTextSpace);
        float f = 0.0f;
        float f2 = 0.0f;
        for (LegendEntry legendEntry : this.mEntries) {
            float convertDpToPixel2 = Utils.convertDpToPixel(Float.isNaN(legendEntry.formSize) ? this.mFormSize : legendEntry.formSize);
            if (convertDpToPixel2 > f2) {
                f2 = convertDpToPixel2;
            }
            String str = legendEntry.label;
            if (str != null) {
                float calcTextWidth = (float) Utils.calcTextWidth(paint, str);
                if (calcTextWidth > f) {
                    f = calcTextWidth;
                }
            }
        }
        return (f + f2) + convertDpToPixel;
    }

    public float getMaximumEntryHeight(Paint paint) {
        float f = 0.0f;
        for (LegendEntry legendEntry : this.mEntries) {
            String str = legendEntry.label;
            if (str != null) {
                float calcTextHeight = (float) Utils.calcTextHeight(paint, str);
                if (calcTextHeight > f) {
                    f = calcTextHeight;
                }
            }
        }
        return f;
    }

    @Deprecated
    public int[] getColors() {
        int[] iArr = new int[this.mEntries.length];
        for (int i = 0; i < this.mEntries.length; i++) {
            int i2 = this.mEntries[i].form == LegendForm.NONE ? ColorTemplate.COLOR_SKIP : this.mEntries[i].form == LegendForm.EMPTY ? ColorTemplate.COLOR_NONE : this.mEntries[i].formColor;
            iArr[i] = i2;
        }
        return iArr;
    }

    @Deprecated
    public String[] getLabels() {
        String[] strArr = new String[this.mEntries.length];
        for (int i = 0; i < this.mEntries.length; i++) {
            strArr[i] = this.mEntries[i].label;
        }
        return strArr;
    }

    @Deprecated
    public int[] getExtraColors() {
        int[] iArr = new int[this.mExtraEntries.length];
        for (int i = 0; i < this.mExtraEntries.length; i++) {
            int i2 = this.mExtraEntries[i].form == LegendForm.NONE ? ColorTemplate.COLOR_SKIP : this.mExtraEntries[i].form == LegendForm.EMPTY ? ColorTemplate.COLOR_NONE : this.mExtraEntries[i].formColor;
            iArr[i] = i2;
        }
        return iArr;
    }

    @Deprecated
    public String[] getExtraLabels() {
        String[] strArr = new String[this.mExtraEntries.length];
        for (int i = 0; i < this.mExtraEntries.length; i++) {
            strArr[i] = this.mExtraEntries[i].label;
        }
        return strArr;
    }

    public LegendEntry[] getExtraEntries() {
        return this.mExtraEntries;
    }

    public void setExtra(List<LegendEntry> list) {
        this.mExtraEntries = (LegendEntry[]) list.toArray(new LegendEntry[list.size()]);
    }

    public void setExtra(LegendEntry[] legendEntryArr) {
        if (legendEntryArr == null) {
            legendEntryArr = new LegendEntry[0];
        }
        this.mExtraEntries = legendEntryArr;
    }

    @Deprecated
    public void setExtra(List<Integer> list, List<String> list2) {
        setExtra(Utils.convertIntegers(list), Utils.convertStrings(list2));
    }

    public void setExtra(int[] iArr, String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < Math.min(iArr.length, strArr.length); i++) {
            LegendEntry legendEntry = new LegendEntry();
            legendEntry.formColor = iArr[i];
            legendEntry.label = strArr[i];
            if (legendEntry.formColor == ColorTemplate.COLOR_SKIP || legendEntry.formColor == 0) {
                legendEntry.form = LegendForm.NONE;
            } else if (legendEntry.formColor == ColorTemplate.COLOR_NONE) {
                legendEntry.form = LegendForm.EMPTY;
            }
            arrayList.add(legendEntry);
        }
        this.mExtraEntries = (LegendEntry[]) arrayList.toArray(new LegendEntry[arrayList.size()]);
    }

    public void setCustom(LegendEntry[] legendEntryArr) {
        this.mEntries = legendEntryArr;
        this.mIsLegendCustom = true;
    }

    public void setCustom(List<LegendEntry> list) {
        this.mEntries = (LegendEntry[]) list.toArray(new LegendEntry[list.size()]);
        this.mIsLegendCustom = true;
    }

    public void resetCustom() {
        this.mIsLegendCustom = false;
    }

    public boolean isLegendCustom() {
        return this.mIsLegendCustom;
    }

    @Deprecated
    public LegendPosition getPosition() {
        if (this.mOrientation == LegendOrientation.VERTICAL && this.mHorizontalAlignment == LegendHorizontalAlignment.CENTER && this.mVerticalAlignment == LegendVerticalAlignment.CENTER) {
            return LegendPosition.PIECHART_CENTER;
        }
        LegendPosition legendPosition;
        if (this.mOrientation == LegendOrientation.HORIZONTAL) {
            if (this.mVerticalAlignment == LegendVerticalAlignment.TOP) {
                legendPosition = this.mHorizontalAlignment == LegendHorizontalAlignment.LEFT ? LegendPosition.ABOVE_CHART_LEFT : this.mHorizontalAlignment == LegendHorizontalAlignment.RIGHT ? LegendPosition.ABOVE_CHART_RIGHT : LegendPosition.ABOVE_CHART_CENTER;
                return legendPosition;
            }
            legendPosition = this.mHorizontalAlignment == LegendHorizontalAlignment.LEFT ? LegendPosition.BELOW_CHART_LEFT : this.mHorizontalAlignment == LegendHorizontalAlignment.RIGHT ? LegendPosition.BELOW_CHART_RIGHT : LegendPosition.BELOW_CHART_CENTER;
            return legendPosition;
        } else if (this.mHorizontalAlignment == LegendHorizontalAlignment.LEFT) {
            legendPosition = (this.mVerticalAlignment == LegendVerticalAlignment.TOP && this.mDrawInside) ? LegendPosition.LEFT_OF_CHART_INSIDE : this.mVerticalAlignment == LegendVerticalAlignment.CENTER ? LegendPosition.LEFT_OF_CHART_CENTER : LegendPosition.LEFT_OF_CHART;
            return legendPosition;
        } else {
            legendPosition = (this.mVerticalAlignment == LegendVerticalAlignment.TOP && this.mDrawInside) ? LegendPosition.RIGHT_OF_CHART_INSIDE : this.mVerticalAlignment == LegendVerticalAlignment.CENTER ? LegendPosition.RIGHT_OF_CHART_CENTER : LegendPosition.RIGHT_OF_CHART;
            return legendPosition;
        }
    }

    @Deprecated
    public void setPosition(LegendPosition legendPosition) {
        LegendHorizontalAlignment legendHorizontalAlignment;
        switch (legendPosition) {
            case LEFT_OF_CHART:
            case LEFT_OF_CHART_INSIDE:
            case LEFT_OF_CHART_CENTER:
                this.mHorizontalAlignment = LegendHorizontalAlignment.LEFT;
                this.mVerticalAlignment = legendPosition == LegendPosition.LEFT_OF_CHART_CENTER ? LegendVerticalAlignment.CENTER : LegendVerticalAlignment.TOP;
                this.mOrientation = LegendOrientation.VERTICAL;
                break;
            case RIGHT_OF_CHART:
            case RIGHT_OF_CHART_INSIDE:
            case RIGHT_OF_CHART_CENTER:
                this.mHorizontalAlignment = LegendHorizontalAlignment.RIGHT;
                this.mVerticalAlignment = legendPosition == LegendPosition.RIGHT_OF_CHART_CENTER ? LegendVerticalAlignment.CENTER : LegendVerticalAlignment.TOP;
                this.mOrientation = LegendOrientation.VERTICAL;
                break;
            case ABOVE_CHART_LEFT:
            case ABOVE_CHART_CENTER:
            case ABOVE_CHART_RIGHT:
                legendHorizontalAlignment = legendPosition == LegendPosition.ABOVE_CHART_LEFT ? LegendHorizontalAlignment.LEFT : legendPosition == LegendPosition.ABOVE_CHART_RIGHT ? LegendHorizontalAlignment.RIGHT : LegendHorizontalAlignment.CENTER;
                this.mHorizontalAlignment = legendHorizontalAlignment;
                this.mVerticalAlignment = LegendVerticalAlignment.TOP;
                this.mOrientation = LegendOrientation.HORIZONTAL;
                break;
            case BELOW_CHART_LEFT:
            case BELOW_CHART_CENTER:
            case BELOW_CHART_RIGHT:
                legendHorizontalAlignment = legendPosition == LegendPosition.BELOW_CHART_LEFT ? LegendHorizontalAlignment.LEFT : legendPosition == LegendPosition.BELOW_CHART_RIGHT ? LegendHorizontalAlignment.RIGHT : LegendHorizontalAlignment.CENTER;
                this.mHorizontalAlignment = legendHorizontalAlignment;
                this.mVerticalAlignment = LegendVerticalAlignment.BOTTOM;
                this.mOrientation = LegendOrientation.HORIZONTAL;
                break;
            case PIECHART_CENTER:
                this.mHorizontalAlignment = LegendHorizontalAlignment.CENTER;
                this.mVerticalAlignment = LegendVerticalAlignment.CENTER;
                this.mOrientation = LegendOrientation.VERTICAL;
                break;
        }
        boolean z = legendPosition == LegendPosition.LEFT_OF_CHART_INSIDE || legendPosition == LegendPosition.RIGHT_OF_CHART_INSIDE;
        this.mDrawInside = z;
    }

    public LegendHorizontalAlignment getHorizontalAlignment() {
        return this.mHorizontalAlignment;
    }

    public void setHorizontalAlignment(LegendHorizontalAlignment legendHorizontalAlignment) {
        this.mHorizontalAlignment = legendHorizontalAlignment;
    }

    public LegendVerticalAlignment getVerticalAlignment() {
        return this.mVerticalAlignment;
    }

    public void setVerticalAlignment(LegendVerticalAlignment legendVerticalAlignment) {
        this.mVerticalAlignment = legendVerticalAlignment;
    }

    public LegendOrientation getOrientation() {
        return this.mOrientation;
    }

    public void setOrientation(LegendOrientation legendOrientation) {
        this.mOrientation = legendOrientation;
    }

    public boolean isDrawInsideEnabled() {
        return this.mDrawInside;
    }

    public void setDrawInside(boolean z) {
        this.mDrawInside = z;
    }

    public LegendDirection getDirection() {
        return this.mDirection;
    }

    public void setDirection(LegendDirection legendDirection) {
        this.mDirection = legendDirection;
    }

    public LegendForm getForm() {
        return this.mShape;
    }

    public void setForm(LegendForm legendForm) {
        this.mShape = legendForm;
    }

    public void setFormSize(float f) {
        this.mFormSize = f;
    }

    public float getFormSize() {
        return this.mFormSize;
    }

    public void setFormLineWidth(float f) {
        this.mFormLineWidth = f;
    }

    public float getFormLineWidth() {
        return this.mFormLineWidth;
    }

    public void setFormLineDashEffect(DashPathEffect dashPathEffect) {
        this.mFormLineDashEffect = dashPathEffect;
    }

    public DashPathEffect getFormLineDashEffect() {
        return this.mFormLineDashEffect;
    }

    public float getXEntrySpace() {
        return this.mXEntrySpace;
    }

    public void setXEntrySpace(float f) {
        this.mXEntrySpace = f;
    }

    public float getYEntrySpace() {
        return this.mYEntrySpace;
    }

    public void setYEntrySpace(float f) {
        this.mYEntrySpace = f;
    }

    public float getFormToTextSpace() {
        return this.mFormToTextSpace;
    }

    public void setFormToTextSpace(float f) {
        this.mFormToTextSpace = f;
    }

    public float getStackSpace() {
        return this.mStackSpace;
    }

    public void setStackSpace(float f) {
        this.mStackSpace = f;
    }

    public void setWordWrapEnabled(boolean z) {
        this.mWordWrapEnabled = z;
    }

    public boolean isWordWrapEnabled() {
        return this.mWordWrapEnabled;
    }

    public float getMaxSizePercent() {
        return this.mMaxSizePercent;
    }

    public void setMaxSizePercent(float f) {
        this.mMaxSizePercent = f;
    }

    public List<FSize> getCalculatedLabelSizes() {
        return this.mCalculatedLabelSizes;
    }

    public List<Boolean> getCalculatedLabelBreakPoints() {
        return this.mCalculatedLabelBreakPoints;
    }

    public List<FSize> getCalculatedLineSizes() {
        return this.mCalculatedLineSizes;
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x00ee  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0140  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x012e  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x0147 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0146  */
    public void calculateDimensions(android.graphics.Paint r27, com.github.mikephil.charting.utils.ViewPortHandler r28) {
        /*
        r26 = this;
        r0 = r26;
        r1 = r27;
        r2 = r0.mFormSize;
        r2 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r2);
        r3 = r0.mStackSpace;
        r3 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r3);
        r4 = r0.mFormToTextSpace;
        r4 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r4);
        r5 = r0.mXEntrySpace;
        r5 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r5);
        r6 = r0.mYEntrySpace;
        r6 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r6);
        r7 = r0.mWordWrapEnabled;
        r8 = r0.mEntries;
        r12 = r8.length;
        r13 = r26.getMaximumEntryWidth(r27);
        r0.mTextWidthMax = r13;
        r13 = r26.getMaximumEntryHeight(r27);
        r0.mTextHeightMax = r13;
        r13 = com.github.mikephil.charting.components.Legend.C04291.f50x9c9dbef;
        r14 = r0.mOrientation;
        r14 = r14.ordinal();
        r13 = r13[r14];
        switch(r13) {
            case 1: goto L_0x017d;
            case 2: goto L_0x0042;
            default: goto L_0x0040;
        };
    L_0x0040:
        goto L_0x01ea;
    L_0x0042:
        r13 = com.github.mikephil.charting.utils.Utils.getLineHeight(r27);
        r14 = com.github.mikephil.charting.utils.Utils.getLineSpacing(r27);
        r14 = r14 + r6;
        r6 = r28.contentWidth();
        r15 = r0.mMaxSizePercent;
        r6 = r6 * r15;
        r15 = r0.mCalculatedLabelBreakPoints;
        r15.clear();
        r15 = r0.mCalculatedLabelSizes;
        r15.clear();
        r15 = r0.mCalculatedLineSizes;
        r15.clear();
        r10 = 0;
        r11 = -1;
        r16 = 0;
        r17 = 0;
        r18 = 0;
    L_0x0069:
        if (r10 >= r12) goto L_0x0153;
    L_0x006b:
        r15 = r8[r10];
        r9 = r15.form;
        r20 = r2;
        r2 = com.github.mikephil.charting.components.Legend.LegendForm.NONE;
        if (r9 == r2) goto L_0x0077;
    L_0x0075:
        r2 = 1;
        goto L_0x0078;
    L_0x0077:
        r2 = 0;
    L_0x0078:
        r9 = r15.formSize;
        r9 = java.lang.Float.isNaN(r9);
        if (r9 == 0) goto L_0x0083;
    L_0x0080:
        r9 = r20;
        goto L_0x0089;
    L_0x0083:
        r9 = r15.formSize;
        r9 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r9);
    L_0x0089:
        r15 = r15.label;
        r21 = r5;
        r5 = r0.mCalculatedLabelBreakPoints;
        r22 = r8;
        r23 = r14;
        r8 = 0;
        r14 = java.lang.Boolean.valueOf(r8);
        r5.add(r14);
        r5 = -1;
        if (r11 != r5) goto L_0x00a0;
    L_0x009e:
        r5 = 0;
        goto L_0x00a2;
    L_0x00a0:
        r5 = r17 + r3;
    L_0x00a2:
        if (r15 == 0) goto L_0x00c2;
    L_0x00a4:
        r14 = r0.mCalculatedLabelSizes;
        r8 = com.github.mikephil.charting.utils.Utils.calcTextSize(r1, r15);
        r14.add(r8);
        if (r2 == 0) goto L_0x00b2;
    L_0x00af:
        r2 = r4 + r9;
        goto L_0x00b3;
    L_0x00b2:
        r2 = 0;
    L_0x00b3:
        r5 = r5 + r2;
        r2 = r0.mCalculatedLabelSizes;
        r2 = r2.get(r10);
        r2 = (com.github.mikephil.charting.utils.FSize) r2;
        r2 = r2.width;
        r5 = r5 + r2;
    L_0x00bf:
        r17 = r5;
        goto L_0x00db;
    L_0x00c2:
        r8 = r0.mCalculatedLabelSizes;
        r24 = r9;
        r14 = 0;
        r9 = com.github.mikephil.charting.utils.FSize.getInstance(r14, r14);
        r8.add(r9);
        if (r2 == 0) goto L_0x00d1;
    L_0x00d0:
        goto L_0x00d3;
    L_0x00d1:
        r24 = 0;
    L_0x00d3:
        r5 = r5 + r24;
        r2 = -1;
        if (r11 != r2) goto L_0x00bf;
    L_0x00d8:
        r17 = r5;
        r11 = r10;
    L_0x00db:
        if (r15 != 0) goto L_0x00e5;
    L_0x00dd:
        r2 = r12 + -1;
        if (r10 != r2) goto L_0x00e2;
    L_0x00e1:
        goto L_0x00e5;
    L_0x00e2:
        r14 = -1;
        goto L_0x0144;
    L_0x00e5:
        r2 = r18;
        r5 = 0;
        r8 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1));
        if (r8 != 0) goto L_0x00ee;
    L_0x00ec:
        r8 = r5;
        goto L_0x00f0;
    L_0x00ee:
        r8 = r21;
    L_0x00f0:
        if (r7 == 0) goto L_0x0124;
    L_0x00f2:
        r9 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1));
        if (r9 == 0) goto L_0x0124;
    L_0x00f6:
        r9 = r6 - r2;
        r14 = r8 + r17;
        r9 = (r9 > r14 ? 1 : (r9 == r14 ? 0 : -1));
        if (r9 < 0) goto L_0x00ff;
    L_0x00fe:
        goto L_0x0124;
    L_0x00ff:
        r8 = r0.mCalculatedLineSizes;
        r9 = com.github.mikephil.charting.utils.FSize.getInstance(r2, r13);
        r8.add(r9);
        r9 = r16;
        r16 = java.lang.Math.max(r9, r2);
        r2 = r0.mCalculatedLabelBreakPoints;
        r14 = -1;
        if (r11 <= r14) goto L_0x0116;
    L_0x0113:
        r8 = r11;
    L_0x0114:
        r9 = 1;
        goto L_0x0118;
    L_0x0116:
        r8 = r10;
        goto L_0x0114;
    L_0x0118:
        r5 = java.lang.Boolean.valueOf(r9);
        r2.set(r8, r5);
        r9 = r16;
        r2 = r17;
        goto L_0x012a;
    L_0x0124:
        r9 = r16;
        r14 = -1;
        r8 = r8 + r17;
        r2 = r2 + r8;
    L_0x012a:
        r5 = r12 + -1;
        if (r10 != r5) goto L_0x0140;
    L_0x012e:
        r5 = r0.mCalculatedLineSizes;
        r8 = com.github.mikephil.charting.utils.FSize.getInstance(r2, r13);
        r5.add(r8);
        r5 = java.lang.Math.max(r9, r2);
        r18 = r2;
        r16 = r5;
        goto L_0x0144;
    L_0x0140:
        r18 = r2;
        r16 = r9;
    L_0x0144:
        if (r15 == 0) goto L_0x0147;
    L_0x0146:
        r11 = r14;
    L_0x0147:
        r10 = r10 + 1;
        r2 = r20;
        r5 = r21;
        r8 = r22;
        r14 = r23;
        goto L_0x0069;
    L_0x0153:
        r23 = r14;
        r9 = r16;
        r0.mNeededWidth = r9;
        r1 = r0.mCalculatedLineSizes;
        r1 = r1.size();
        r1 = (float) r1;
        r13 = r13 * r1;
        r1 = r0.mCalculatedLineSizes;
        r1 = r1.size();
        if (r1 != 0) goto L_0x016b;
    L_0x0169:
        r9 = 0;
        goto L_0x0175;
    L_0x016b:
        r1 = r0.mCalculatedLineSizes;
        r1 = r1.size();
        r19 = 1;
        r9 = r1 + -1;
    L_0x0175:
        r1 = (float) r9;
        r14 = r23 * r1;
        r13 = r13 + r14;
        r0.mNeededHeight = r13;
        goto L_0x01ea;
    L_0x017d:
        r20 = r2;
        r22 = r8;
        r19 = 1;
        r2 = com.github.mikephil.charting.utils.Utils.getLineHeight(r27);
        r5 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r11 = 0;
    L_0x018c:
        if (r5 >= r12) goto L_0x01e6;
    L_0x018e:
        r10 = r22[r5];
        r13 = r10.form;
        r14 = com.github.mikephil.charting.components.Legend.LegendForm.NONE;
        if (r13 == r14) goto L_0x0199;
    L_0x0196:
        r13 = r19;
        goto L_0x019a;
    L_0x0199:
        r13 = 0;
    L_0x019a:
        r14 = r10.formSize;
        r14 = java.lang.Float.isNaN(r14);
        if (r14 == 0) goto L_0x01a5;
    L_0x01a2:
        r14 = r20;
        goto L_0x01ab;
    L_0x01a5:
        r14 = r10.formSize;
        r14 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r14);
    L_0x01ab:
        r10 = r10.label;
        if (r9 != 0) goto L_0x01b0;
    L_0x01af:
        r11 = 0;
    L_0x01b0:
        if (r13 == 0) goto L_0x01b6;
    L_0x01b2:
        if (r9 == 0) goto L_0x01b5;
    L_0x01b4:
        r11 = r11 + r3;
    L_0x01b5:
        r11 = r11 + r14;
    L_0x01b6:
        if (r10 == 0) goto L_0x01d7;
    L_0x01b8:
        if (r13 == 0) goto L_0x01be;
    L_0x01ba:
        if (r9 != 0) goto L_0x01be;
    L_0x01bc:
        r11 = r11 + r4;
        goto L_0x01c9;
    L_0x01be:
        if (r9 == 0) goto L_0x01c9;
    L_0x01c0:
        r7 = java.lang.Math.max(r7, r11);
        r9 = r2 + r6;
        r8 = r8 + r9;
        r9 = 0;
        r11 = 0;
    L_0x01c9:
        r10 = com.github.mikephil.charting.utils.Utils.calcTextWidth(r1, r10);
        r10 = (float) r10;
        r11 = r11 + r10;
        r10 = r12 + -1;
        if (r5 >= r10) goto L_0x01df;
    L_0x01d3:
        r10 = r2 + r6;
        r8 = r8 + r10;
        goto L_0x01df;
    L_0x01d7:
        r11 = r11 + r14;
        r9 = r12 + -1;
        if (r5 >= r9) goto L_0x01dd;
    L_0x01dc:
        r11 = r11 + r3;
    L_0x01dd:
        r9 = r19;
    L_0x01df:
        r7 = java.lang.Math.max(r7, r11);
        r5 = r5 + 1;
        goto L_0x018c;
    L_0x01e6:
        r0.mNeededWidth = r7;
        r0.mNeededHeight = r8;
    L_0x01ea:
        r1 = r0.mNeededHeight;
        r2 = r0.mYOffset;
        r1 = r1 + r2;
        r0.mNeededHeight = r1;
        r1 = r0.mNeededWidth;
        r2 = r0.mXOffset;
        r1 = r1 + r2;
        r0.mNeededWidth = r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.components.Legend.calculateDimensions(android.graphics.Paint, com.github.mikephil.charting.utils.ViewPortHandler):void");
    }
}

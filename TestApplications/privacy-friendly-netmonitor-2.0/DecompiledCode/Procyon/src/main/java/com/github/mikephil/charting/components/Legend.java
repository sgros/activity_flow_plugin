// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import android.graphics.DashPathEffect;
import com.github.mikephil.charting.utils.FSize;
import java.util.List;

public class Legend extends ComponentBase
{
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
        this.mCalculatedLabelSizes = new ArrayList<FSize>(16);
        this.mCalculatedLabelBreakPoints = new ArrayList<Boolean>(16);
        this.mCalculatedLineSizes = new ArrayList<FSize>(16);
        this.mTextSize = Utils.convertDpToPixel(10.0f);
        this.mXOffset = Utils.convertDpToPixel(5.0f);
        this.mYOffset = Utils.convertDpToPixel(3.0f);
    }
    
    @Deprecated
    public Legend(final List<Integer> list, final List<String> list2) {
        this(Utils.convertIntegers(list), Utils.convertStrings(list2));
    }
    
    @Deprecated
    public Legend(final int[] array, final String[] array2) {
        this();
        if (array == null || array2 == null) {
            throw new IllegalArgumentException("colors array or labels array is NULL");
        }
        if (array.length != array2.length) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
        }
        final ArrayList<LegendEntry> list = new ArrayList<LegendEntry>();
        for (int i = 0; i < Math.min(array.length, array2.length); ++i) {
            final LegendEntry legendEntry = new LegendEntry();
            legendEntry.formColor = array[i];
            legendEntry.label = array2[i];
            if (legendEntry.formColor == 1122868) {
                legendEntry.form = LegendForm.NONE;
            }
            else if (legendEntry.formColor == 1122867 || legendEntry.formColor == 0) {
                legendEntry.form = LegendForm.EMPTY;
            }
            list.add(legendEntry);
        }
        this.mEntries = list.toArray(new LegendEntry[list.size()]);
    }
    
    public Legend(final LegendEntry[] mEntries) {
        this();
        if (mEntries == null) {
            throw new IllegalArgumentException("entries array is NULL");
        }
        this.mEntries = mEntries;
    }
    
    public void calculateDimensions(final Paint paint, final ViewPortHandler viewPortHandler) {
        final float convertDpToPixel = Utils.convertDpToPixel(this.mFormSize);
        final float convertDpToPixel2 = Utils.convertDpToPixel(this.mStackSpace);
        final float convertDpToPixel3 = Utils.convertDpToPixel(this.mFormToTextSpace);
        final float convertDpToPixel4 = Utils.convertDpToPixel(this.mXEntrySpace);
        final float convertDpToPixel5 = Utils.convertDpToPixel(this.mYEntrySpace);
        final boolean mWordWrapEnabled = this.mWordWrapEnabled;
        final LegendEntry[] mEntries = this.mEntries;
        final int length = mEntries.length;
        this.mTextWidthMax = this.getMaximumEntryWidth(paint);
        this.mTextHeightMax = this.getMaximumEntryHeight(paint);
        switch (Legend$1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendOrientation[this.mOrientation.ordinal()]) {
            case 2: {
                final float lineHeight = Utils.getLineHeight(paint);
                final float n = Utils.getLineSpacing(paint) + convertDpToPixel5;
                final float contentWidth = viewPortHandler.contentWidth();
                final float mMaxSizePercent = this.mMaxSizePercent;
                this.mCalculatedLabelBreakPoints.clear();
                this.mCalculatedLabelSizes.clear();
                this.mCalculatedLineSizes.clear();
                int i = 0;
                int n2 = -1;
                float n3 = 0.0f;
                float n4 = 0.0f;
                float b = 0.0f;
                final LegendEntry[] array = mEntries;
                final float n5 = convertDpToPixel;
                while (i < length) {
                    final LegendEntry legendEntry = array[i];
                    final boolean b2 = legendEntry.form != LegendForm.NONE;
                    float convertDpToPixel6;
                    if (Float.isNaN(legendEntry.formSize)) {
                        convertDpToPixel6 = n5;
                    }
                    else {
                        convertDpToPixel6 = Utils.convertDpToPixel(legendEntry.formSize);
                    }
                    final String label = legendEntry.label;
                    this.mCalculatedLabelBreakPoints.add(false);
                    float n6;
                    if (n2 == -1) {
                        n6 = 0.0f;
                    }
                    else {
                        n6 = n4 + convertDpToPixel2;
                    }
                    float n8;
                    if (label != null) {
                        this.mCalculatedLabelSizes.add(Utils.calcTextSize(paint, label));
                        float n7;
                        if (b2) {
                            n7 = convertDpToPixel3 + convertDpToPixel6;
                        }
                        else {
                            n7 = 0.0f;
                        }
                        n8 = n6 + n7 + this.mCalculatedLabelSizes.get(i).width;
                    }
                    else {
                        this.mCalculatedLabelSizes.add(FSize.getInstance(0.0f, 0.0f));
                        if (!b2) {
                            convertDpToPixel6 = 0.0f;
                        }
                        final float n9 = n8 = n6 + convertDpToPixel6;
                        if (n2 == -1) {
                            n8 = n9;
                            n2 = i;
                        }
                    }
                    if (label != null || i == length - 1) {
                        float n10;
                        if (b == 0.0f) {
                            n10 = 0.0f;
                        }
                        else {
                            n10 = convertDpToPixel4;
                        }
                        float max;
                        float b3;
                        if (mWordWrapEnabled && b != 0.0f && contentWidth * mMaxSizePercent - b < n10 + n8) {
                            this.mCalculatedLineSizes.add(FSize.getInstance(b, lineHeight));
                            max = Math.max(n3, b);
                            final List<Boolean> mCalculatedLabelBreakPoints = this.mCalculatedLabelBreakPoints;
                            int n11;
                            if (n2 > -1) {
                                n11 = n2;
                            }
                            else {
                                n11 = i;
                            }
                            mCalculatedLabelBreakPoints.set(n11, true);
                            b3 = n8;
                        }
                        else {
                            final float n12 = b + (n10 + n8);
                            max = n3;
                            b3 = n12;
                        }
                        if (i == length - 1) {
                            this.mCalculatedLineSizes.add(FSize.getInstance(b3, lineHeight));
                            final float max2 = Math.max(max, b3);
                            b = b3;
                            n3 = max2;
                        }
                        else {
                            final float n13 = max;
                            b = b3;
                            n3 = n13;
                        }
                    }
                    if (label != null) {
                        n2 = -1;
                    }
                    ++i;
                    n4 = n8;
                }
                this.mNeededWidth = n3;
                final float n14 = (float)this.mCalculatedLineSizes.size();
                int n15;
                if (this.mCalculatedLineSizes.size() == 0) {
                    n15 = 0;
                }
                else {
                    n15 = this.mCalculatedLineSizes.size() - 1;
                }
                this.mNeededHeight = lineHeight * n14 + n * n15;
                break;
            }
            case 1: {
                final float lineHeight2 = Utils.getLineHeight(paint);
                int j = 0;
                float max3 = 0.0f;
                float mNeededHeight = 0.0f;
                int n16 = 0;
                float n17 = 0.0f;
                while (j < length) {
                    final LegendEntry legendEntry2 = mEntries[j];
                    final boolean b4 = legendEntry2.form != LegendForm.NONE;
                    float convertDpToPixel7;
                    if (Float.isNaN(legendEntry2.formSize)) {
                        convertDpToPixel7 = convertDpToPixel;
                    }
                    else {
                        convertDpToPixel7 = Utils.convertDpToPixel(legendEntry2.formSize);
                    }
                    final String label2 = legendEntry2.label;
                    if (n16 == 0) {
                        n17 = 0.0f;
                    }
                    float b5 = n17;
                    if (b4) {
                        float n18 = n17;
                        if (n16 != 0) {
                            n18 = n17 + convertDpToPixel2;
                        }
                        b5 = n18 + convertDpToPixel7;
                    }
                    float b6;
                    if (label2 != null) {
                        float n19;
                        float max4;
                        float n20;
                        int n21;
                        if (b4 && n16 == 0) {
                            n19 = b5 + convertDpToPixel3;
                            max4 = max3;
                            n20 = mNeededHeight;
                            n21 = n16;
                        }
                        else {
                            max4 = max3;
                            n20 = mNeededHeight;
                            n21 = n16;
                            n19 = b5;
                            if (n16 != 0) {
                                max4 = Math.max(max3, b5);
                                n20 = mNeededHeight + (lineHeight2 + convertDpToPixel5);
                                n21 = 0;
                                n19 = 0.0f;
                            }
                        }
                        final float n22 = n19 + Utils.calcTextWidth(paint, label2);
                        max3 = max4;
                        mNeededHeight = n20;
                        n16 = n21;
                        b6 = n22;
                        if (j < length - 1) {
                            mNeededHeight = n20 + (lineHeight2 + convertDpToPixel5);
                            max3 = max4;
                            n16 = n21;
                            b6 = n22;
                        }
                    }
                    else {
                        final float n23 = b6 = b5 + convertDpToPixel7;
                        if (j < length - 1) {
                            b6 = n23 + convertDpToPixel2;
                        }
                        n16 = 1;
                    }
                    max3 = Math.max(max3, b6);
                    ++j;
                    n17 = b6;
                }
                this.mNeededWidth = max3;
                this.mNeededHeight = mNeededHeight;
                break;
            }
        }
        this.mNeededHeight += this.mYOffset;
        this.mNeededWidth += this.mXOffset;
    }
    
    public List<Boolean> getCalculatedLabelBreakPoints() {
        return this.mCalculatedLabelBreakPoints;
    }
    
    public List<FSize> getCalculatedLabelSizes() {
        return this.mCalculatedLabelSizes;
    }
    
    public List<FSize> getCalculatedLineSizes() {
        return this.mCalculatedLineSizes;
    }
    
    @Deprecated
    public int[] getColors() {
        final LegendEntry[] mEntries = this.mEntries;
        int i = 0;
        final int[] array = new int[mEntries.length];
        while (i < this.mEntries.length) {
            int formColor;
            if (this.mEntries[i].form == LegendForm.NONE) {
                formColor = 1122868;
            }
            else if (this.mEntries[i].form == LegendForm.EMPTY) {
                formColor = 1122867;
            }
            else {
                formColor = this.mEntries[i].formColor;
            }
            array[i] = formColor;
            ++i;
        }
        return array;
    }
    
    public LegendDirection getDirection() {
        return this.mDirection;
    }
    
    public LegendEntry[] getEntries() {
        return this.mEntries;
    }
    
    @Deprecated
    public int[] getExtraColors() {
        final LegendEntry[] mExtraEntries = this.mExtraEntries;
        int i = 0;
        final int[] array = new int[mExtraEntries.length];
        while (i < this.mExtraEntries.length) {
            int formColor;
            if (this.mExtraEntries[i].form == LegendForm.NONE) {
                formColor = 1122868;
            }
            else if (this.mExtraEntries[i].form == LegendForm.EMPTY) {
                formColor = 1122867;
            }
            else {
                formColor = this.mExtraEntries[i].formColor;
            }
            array[i] = formColor;
            ++i;
        }
        return array;
    }
    
    public LegendEntry[] getExtraEntries() {
        return this.mExtraEntries;
    }
    
    @Deprecated
    public String[] getExtraLabels() {
        final LegendEntry[] mExtraEntries = this.mExtraEntries;
        int i = 0;
        final String[] array = new String[mExtraEntries.length];
        while (i < this.mExtraEntries.length) {
            array[i] = this.mExtraEntries[i].label;
            ++i;
        }
        return array;
    }
    
    public LegendForm getForm() {
        return this.mShape;
    }
    
    public DashPathEffect getFormLineDashEffect() {
        return this.mFormLineDashEffect;
    }
    
    public float getFormLineWidth() {
        return this.mFormLineWidth;
    }
    
    public float getFormSize() {
        return this.mFormSize;
    }
    
    public float getFormToTextSpace() {
        return this.mFormToTextSpace;
    }
    
    public LegendHorizontalAlignment getHorizontalAlignment() {
        return this.mHorizontalAlignment;
    }
    
    @Deprecated
    public String[] getLabels() {
        final LegendEntry[] mEntries = this.mEntries;
        int i = 0;
        final String[] array = new String[mEntries.length];
        while (i < this.mEntries.length) {
            array[i] = this.mEntries[i].label;
            ++i;
        }
        return array;
    }
    
    public float getMaxSizePercent() {
        return this.mMaxSizePercent;
    }
    
    public float getMaximumEntryHeight(final Paint paint) {
        final LegendEntry[] mEntries = this.mEntries;
        float n = 0.0f;
        float n2;
        for (int i = 0; i < mEntries.length; ++i, n = n2) {
            final String label = mEntries[i].label;
            if (label == null) {
                n2 = n;
            }
            else {
                final float n3 = (float)Utils.calcTextHeight(paint, label);
                n2 = n;
                if (n3 > n) {
                    n2 = n3;
                }
            }
        }
        return n;
    }
    
    public float getMaximumEntryWidth(final Paint paint) {
        final float convertDpToPixel = Utils.convertDpToPixel(this.mFormToTextSpace);
        final LegendEntry[] mEntries = this.mEntries;
        float n = 0.0f;
        int i = 0;
        final int length = mEntries.length;
        float n2 = 0.0f;
        while (i < length) {
            final LegendEntry legendEntry = mEntries[i];
            float n3;
            if (Float.isNaN(legendEntry.formSize)) {
                n3 = this.mFormSize;
            }
            else {
                n3 = legendEntry.formSize;
            }
            final float convertDpToPixel2 = Utils.convertDpToPixel(n3);
            float n4 = n2;
            if (convertDpToPixel2 > n2) {
                n4 = convertDpToPixel2;
            }
            final String label = legendEntry.label;
            float n5;
            if (label == null) {
                n5 = n;
            }
            else {
                final float n6 = (float)Utils.calcTextWidth(paint, label);
                n5 = n;
                if (n6 > n) {
                    n5 = n6;
                }
            }
            ++i;
            n = n5;
            n2 = n4;
        }
        return n + n2 + convertDpToPixel;
    }
    
    public LegendOrientation getOrientation() {
        return this.mOrientation;
    }
    
    @Deprecated
    public LegendPosition getPosition() {
        if (this.mOrientation == LegendOrientation.VERTICAL && this.mHorizontalAlignment == LegendHorizontalAlignment.CENTER && this.mVerticalAlignment == LegendVerticalAlignment.CENTER) {
            return LegendPosition.PIECHART_CENTER;
        }
        if (this.mOrientation == LegendOrientation.HORIZONTAL) {
            if (this.mVerticalAlignment == LegendVerticalAlignment.TOP) {
                LegendPosition legendPosition;
                if (this.mHorizontalAlignment == LegendHorizontalAlignment.LEFT) {
                    legendPosition = LegendPosition.ABOVE_CHART_LEFT;
                }
                else if (this.mHorizontalAlignment == LegendHorizontalAlignment.RIGHT) {
                    legendPosition = LegendPosition.ABOVE_CHART_RIGHT;
                }
                else {
                    legendPosition = LegendPosition.ABOVE_CHART_CENTER;
                }
                return legendPosition;
            }
            LegendPosition legendPosition2;
            if (this.mHorizontalAlignment == LegendHorizontalAlignment.LEFT) {
                legendPosition2 = LegendPosition.BELOW_CHART_LEFT;
            }
            else if (this.mHorizontalAlignment == LegendHorizontalAlignment.RIGHT) {
                legendPosition2 = LegendPosition.BELOW_CHART_RIGHT;
            }
            else {
                legendPosition2 = LegendPosition.BELOW_CHART_CENTER;
            }
            return legendPosition2;
        }
        else {
            if (this.mHorizontalAlignment == LegendHorizontalAlignment.LEFT) {
                LegendPosition legendPosition3;
                if (this.mVerticalAlignment == LegendVerticalAlignment.TOP && this.mDrawInside) {
                    legendPosition3 = LegendPosition.LEFT_OF_CHART_INSIDE;
                }
                else if (this.mVerticalAlignment == LegendVerticalAlignment.CENTER) {
                    legendPosition3 = LegendPosition.LEFT_OF_CHART_CENTER;
                }
                else {
                    legendPosition3 = LegendPosition.LEFT_OF_CHART;
                }
                return legendPosition3;
            }
            LegendPosition legendPosition4;
            if (this.mVerticalAlignment == LegendVerticalAlignment.TOP && this.mDrawInside) {
                legendPosition4 = LegendPosition.RIGHT_OF_CHART_INSIDE;
            }
            else if (this.mVerticalAlignment == LegendVerticalAlignment.CENTER) {
                legendPosition4 = LegendPosition.RIGHT_OF_CHART_CENTER;
            }
            else {
                legendPosition4 = LegendPosition.RIGHT_OF_CHART;
            }
            return legendPosition4;
        }
    }
    
    public float getStackSpace() {
        return this.mStackSpace;
    }
    
    public LegendVerticalAlignment getVerticalAlignment() {
        return this.mVerticalAlignment;
    }
    
    public float getXEntrySpace() {
        return this.mXEntrySpace;
    }
    
    public float getYEntrySpace() {
        return this.mYEntrySpace;
    }
    
    public boolean isDrawInsideEnabled() {
        return this.mDrawInside;
    }
    
    public boolean isLegendCustom() {
        return this.mIsLegendCustom;
    }
    
    public boolean isWordWrapEnabled() {
        return this.mWordWrapEnabled;
    }
    
    public void resetCustom() {
        this.mIsLegendCustom = false;
    }
    
    public void setCustom(final List<LegendEntry> list) {
        this.mEntries = list.toArray(new LegendEntry[list.size()]);
        this.mIsLegendCustom = true;
    }
    
    public void setCustom(final LegendEntry[] mEntries) {
        this.mEntries = mEntries;
        this.mIsLegendCustom = true;
    }
    
    public void setDirection(final LegendDirection mDirection) {
        this.mDirection = mDirection;
    }
    
    public void setDrawInside(final boolean mDrawInside) {
        this.mDrawInside = mDrawInside;
    }
    
    public void setEntries(final List<LegendEntry> list) {
        this.mEntries = list.toArray(new LegendEntry[list.size()]);
    }
    
    public void setExtra(final List<LegendEntry> list) {
        this.mExtraEntries = list.toArray(new LegendEntry[list.size()]);
    }
    
    @Deprecated
    public void setExtra(final List<Integer> list, final List<String> list2) {
        this.setExtra(Utils.convertIntegers(list), Utils.convertStrings(list2));
    }
    
    public void setExtra(final int[] array, final String[] array2) {
        final ArrayList<LegendEntry> list = new ArrayList<LegendEntry>();
        for (int i = 0; i < Math.min(array.length, array2.length); ++i) {
            final LegendEntry legendEntry = new LegendEntry();
            legendEntry.formColor = array[i];
            legendEntry.label = array2[i];
            if (legendEntry.formColor != 1122868 && legendEntry.formColor != 0) {
                if (legendEntry.formColor == 1122867) {
                    legendEntry.form = LegendForm.EMPTY;
                }
            }
            else {
                legendEntry.form = LegendForm.NONE;
            }
            list.add(legendEntry);
        }
        this.mExtraEntries = list.toArray(new LegendEntry[list.size()]);
    }
    
    public void setExtra(final LegendEntry[] array) {
        LegendEntry[] mExtraEntries = array;
        if (array == null) {
            mExtraEntries = new LegendEntry[0];
        }
        this.mExtraEntries = mExtraEntries;
    }
    
    public void setForm(final LegendForm mShape) {
        this.mShape = mShape;
    }
    
    public void setFormLineDashEffect(final DashPathEffect mFormLineDashEffect) {
        this.mFormLineDashEffect = mFormLineDashEffect;
    }
    
    public void setFormLineWidth(final float mFormLineWidth) {
        this.mFormLineWidth = mFormLineWidth;
    }
    
    public void setFormSize(final float mFormSize) {
        this.mFormSize = mFormSize;
    }
    
    public void setFormToTextSpace(final float mFormToTextSpace) {
        this.mFormToTextSpace = mFormToTextSpace;
    }
    
    public void setHorizontalAlignment(final LegendHorizontalAlignment mHorizontalAlignment) {
        this.mHorizontalAlignment = mHorizontalAlignment;
    }
    
    public void setMaxSizePercent(final float mMaxSizePercent) {
        this.mMaxSizePercent = mMaxSizePercent;
    }
    
    public void setOrientation(final LegendOrientation mOrientation) {
        this.mOrientation = mOrientation;
    }
    
    @Deprecated
    public void setPosition(final LegendPosition legendPosition) {
        switch (Legend$1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendPosition[legendPosition.ordinal()]) {
            case 13: {
                this.mHorizontalAlignment = LegendHorizontalAlignment.CENTER;
                this.mVerticalAlignment = LegendVerticalAlignment.CENTER;
                this.mOrientation = LegendOrientation.VERTICAL;
                break;
            }
            case 10:
            case 11:
            case 12: {
                LegendHorizontalAlignment mHorizontalAlignment;
                if (legendPosition == LegendPosition.BELOW_CHART_LEFT) {
                    mHorizontalAlignment = LegendHorizontalAlignment.LEFT;
                }
                else if (legendPosition == LegendPosition.BELOW_CHART_RIGHT) {
                    mHorizontalAlignment = LegendHorizontalAlignment.RIGHT;
                }
                else {
                    mHorizontalAlignment = LegendHorizontalAlignment.CENTER;
                }
                this.mHorizontalAlignment = mHorizontalAlignment;
                this.mVerticalAlignment = LegendVerticalAlignment.BOTTOM;
                this.mOrientation = LegendOrientation.HORIZONTAL;
                break;
            }
            case 7:
            case 8:
            case 9: {
                LegendHorizontalAlignment mHorizontalAlignment2;
                if (legendPosition == LegendPosition.ABOVE_CHART_LEFT) {
                    mHorizontalAlignment2 = LegendHorizontalAlignment.LEFT;
                }
                else if (legendPosition == LegendPosition.ABOVE_CHART_RIGHT) {
                    mHorizontalAlignment2 = LegendHorizontalAlignment.RIGHT;
                }
                else {
                    mHorizontalAlignment2 = LegendHorizontalAlignment.CENTER;
                }
                this.mHorizontalAlignment = mHorizontalAlignment2;
                this.mVerticalAlignment = LegendVerticalAlignment.TOP;
                this.mOrientation = LegendOrientation.HORIZONTAL;
                break;
            }
            case 4:
            case 5:
            case 6: {
                this.mHorizontalAlignment = LegendHorizontalAlignment.RIGHT;
                LegendVerticalAlignment mVerticalAlignment;
                if (legendPosition == LegendPosition.RIGHT_OF_CHART_CENTER) {
                    mVerticalAlignment = LegendVerticalAlignment.CENTER;
                }
                else {
                    mVerticalAlignment = LegendVerticalAlignment.TOP;
                }
                this.mVerticalAlignment = mVerticalAlignment;
                this.mOrientation = LegendOrientation.VERTICAL;
                break;
            }
            case 1:
            case 2:
            case 3: {
                this.mHorizontalAlignment = LegendHorizontalAlignment.LEFT;
                LegendVerticalAlignment mVerticalAlignment2;
                if (legendPosition == LegendPosition.LEFT_OF_CHART_CENTER) {
                    mVerticalAlignment2 = LegendVerticalAlignment.CENTER;
                }
                else {
                    mVerticalAlignment2 = LegendVerticalAlignment.TOP;
                }
                this.mVerticalAlignment = mVerticalAlignment2;
                this.mOrientation = LegendOrientation.VERTICAL;
                break;
            }
        }
        this.mDrawInside = (legendPosition == LegendPosition.LEFT_OF_CHART_INSIDE || legendPosition == LegendPosition.RIGHT_OF_CHART_INSIDE);
    }
    
    public void setStackSpace(final float mStackSpace) {
        this.mStackSpace = mStackSpace;
    }
    
    public void setVerticalAlignment(final LegendVerticalAlignment mVerticalAlignment) {
        this.mVerticalAlignment = mVerticalAlignment;
    }
    
    public void setWordWrapEnabled(final boolean mWordWrapEnabled) {
        this.mWordWrapEnabled = mWordWrapEnabled;
    }
    
    public void setXEntrySpace(final float mxEntrySpace) {
        this.mXEntrySpace = mxEntrySpace;
    }
    
    public void setYEntrySpace(final float myEntrySpace) {
        this.mYEntrySpace = myEntrySpace;
    }
    
    public enum LegendDirection
    {
        LEFT_TO_RIGHT, 
        RIGHT_TO_LEFT;
    }
    
    public enum LegendForm
    {
        CIRCLE, 
        DEFAULT, 
        EMPTY, 
        LINE, 
        NONE, 
        SQUARE;
    }
    
    public enum LegendHorizontalAlignment
    {
        CENTER, 
        LEFT, 
        RIGHT;
    }
    
    public enum LegendOrientation
    {
        HORIZONTAL, 
        VERTICAL;
    }
    
    @Deprecated
    public enum LegendPosition
    {
        ABOVE_CHART_CENTER, 
        ABOVE_CHART_LEFT, 
        ABOVE_CHART_RIGHT, 
        BELOW_CHART_CENTER, 
        BELOW_CHART_LEFT, 
        BELOW_CHART_RIGHT, 
        LEFT_OF_CHART, 
        LEFT_OF_CHART_CENTER, 
        LEFT_OF_CHART_INSIDE, 
        PIECHART_CENTER, 
        RIGHT_OF_CHART, 
        RIGHT_OF_CHART_CENTER, 
        RIGHT_OF_CHART_INSIDE;
    }
    
    public enum LegendVerticalAlignment
    {
        BOTTOM, 
        CENTER, 
        TOP;
    }
}

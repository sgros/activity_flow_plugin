// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import java.util.HashMap;
import android.view.ViewGroup$LayoutParams;
import android.view.ViewGroup$MarginLayoutParams;
import android.graphics.Paint;
import android.graphics.Path$Direction;
import org.telegram.ui.ArticleViewer;
import java.util.Iterator;
import java.util.List;
import android.util.Pair;
import android.graphics.Canvas;
import org.telegram.tgnet.TLRPC;
import java.util.Arrays;
import java.lang.reflect.Array;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.graphics.RectF;
import java.util.ArrayList;
import android.graphics.Path;
import android.view.View;

public class TableLayout extends View
{
    public static final int ALIGN_BOUNDS = 0;
    public static final int ALIGN_MARGINS = 1;
    public static final Alignment BASELINE;
    public static final Alignment BOTTOM;
    private static final int CAN_STRETCH = 2;
    public static final Alignment CENTER;
    private static final int DEFAULT_ALIGNMENT_MODE = 1;
    private static final int DEFAULT_COUNT = Integer.MIN_VALUE;
    private static final boolean DEFAULT_ORDER_PRESERVED = true;
    private static final int DEFAULT_ORIENTATION = 0;
    private static final boolean DEFAULT_USE_DEFAULT_MARGINS = false;
    public static final Alignment END;
    public static final Alignment FILL;
    public static final int HORIZONTAL = 0;
    private static final int INFLEXIBLE = 0;
    private static final Alignment LEADING;
    public static final Alignment LEFT;
    static final int MAX_SIZE = 100000;
    public static final Alignment RIGHT;
    public static final Alignment START;
    public static final Alignment TOP;
    private static final Alignment TRAILING;
    public static final int UNDEFINED = Integer.MIN_VALUE;
    static final Alignment UNDEFINED_ALIGNMENT;
    static final int UNINITIALIZED_HASH = 0;
    public static final int VERTICAL = 1;
    private Path backgroundPath;
    private ArrayList<Child> cellsToFixHeight;
    private ArrayList<Child> childrens;
    private int colCount;
    private TableLayoutDelegate delegate;
    private boolean drawLines;
    private boolean isRtl;
    private boolean isStriped;
    private int itemPaddingLeft;
    private int itemPaddingTop;
    private Path linePath;
    private int mAlignmentMode;
    private int mDefaultGap;
    private final Axis mHorizontalAxis;
    private int mLastLayoutParamsHashCode;
    private int mOrientation;
    private boolean mUseDefaultMargins;
    private final Axis mVerticalAxis;
    private float[] radii;
    private RectF rect;
    private ArrayList<Point> rowSpans;
    
    static {
        UNDEFINED_ALIGNMENT = (Alignment)new Alignment() {
            public int getAlignmentValue(final Child child, final int n) {
                return Integer.MIN_VALUE;
            }
            
            @Override
            int getGravityOffset(final Child child, final int n) {
                return Integer.MIN_VALUE;
            }
        };
        LEADING = (Alignment)new Alignment() {
            public int getAlignmentValue(final Child child, final int n) {
                return 0;
            }
            
            @Override
            int getGravityOffset(final Child child, final int n) {
                return 0;
            }
        };
        TRAILING = (Alignment)new Alignment() {
            public int getAlignmentValue(final Child child, final int n) {
                return n;
            }
            
            @Override
            int getGravityOffset(final Child child, final int n) {
                return n;
            }
        };
        final Alignment start = TOP = TableLayout.LEADING;
        final Alignment end = BOTTOM = TableLayout.TRAILING;
        START = start;
        END = end;
        LEFT = createSwitchingAlignment(TableLayout.START);
        RIGHT = createSwitchingAlignment(TableLayout.END);
        CENTER = (Alignment)new Alignment() {
            public int getAlignmentValue(final Child child, final int n) {
                return n >> 1;
            }
            
            @Override
            int getGravityOffset(final Child child, final int n) {
                return n >> 1;
            }
        };
        BASELINE = (Alignment)new Alignment() {
            public int getAlignmentValue(final Child child, final int n) {
                return Integer.MIN_VALUE;
            }
            
            public Bounds getBounds() {
                return new Bounds() {
                    private int size;
                    
                    @Override
                    protected int getOffset(final TableLayout tableLayout, final Child child, final Alignment alignment, final int n, final boolean b) {
                        return Math.max(0, super.getOffset(tableLayout, child, alignment, n, b));
                    }
                    
                    @Override
                    protected void include(final int n, final int n2) {
                        super.include(n, n2);
                        this.size = Math.max(this.size, n + n2);
                    }
                    
                    @Override
                    protected void reset() {
                        super.reset();
                        this.size = Integer.MIN_VALUE;
                    }
                    
                    @Override
                    protected int size(final boolean b) {
                        return Math.max(super.size(b), this.size);
                    }
                };
            }
            
            @Override
            int getGravityOffset(final Child child, final int n) {
                return 0;
            }
        };
        FILL = (Alignment)new Alignment() {
            public int getAlignmentValue(final Child child, final int n) {
                return Integer.MIN_VALUE;
            }
            
            @Override
            int getGravityOffset(final Child child, final int n) {
                return 0;
            }
            
            public int getSizeInCell(final Child child, final int n, final int n2) {
                return n2;
            }
        };
    }
    
    public TableLayout(final Context context, final TableLayoutDelegate delegate) {
        super(context);
        this.mHorizontalAxis = new Axis(true);
        this.mVerticalAxis = new Axis(false);
        this.mOrientation = 0;
        this.mUseDefaultMargins = false;
        this.mAlignmentMode = 1;
        this.mLastLayoutParamsHashCode = 0;
        this.itemPaddingTop = AndroidUtilities.dp(7.0f);
        this.itemPaddingLeft = AndroidUtilities.dp(8.0f);
        this.cellsToFixHeight = new ArrayList<Child>();
        this.rowSpans = new ArrayList<Point>();
        this.linePath = new Path();
        this.backgroundPath = new Path();
        this.rect = new RectF();
        this.radii = new float[8];
        this.childrens = new ArrayList<Child>();
        this.setRowCount(Integer.MIN_VALUE);
        this.setColumnCount(Integer.MIN_VALUE);
        this.setOrientation(0);
        this.setUseDefaultMargins(false);
        this.setAlignmentMode(1);
        this.setRowOrderPreserved(true);
        this.setColumnOrderPreserved(true);
        this.delegate = delegate;
    }
    
    static /* synthetic */ void access$1700(final String s) {
        handleInvalidParams(s);
        throw null;
    }
    
    static int adjust(final int n, final int n2) {
        return View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n2 + n), View$MeasureSpec.getMode(n));
    }
    
    static <T> T[] append(final T[] array, final T[] array2) {
        final Object[] array3 = (Object[])Array.newInstance(array.getClass().getComponentType(), array.length + array2.length);
        System.arraycopy(array, 0, array3, 0, array.length);
        System.arraycopy(array2, 0, array3, array.length, array2.length);
        return (T[])array3;
    }
    
    static boolean canStretch(final int n) {
        return (n & 0x2) != 0x0;
    }
    
    private void checkLayoutParams(final LayoutParams layoutParams, final boolean b) {
        String str;
        if (b) {
            str = "column";
        }
        else {
            str = "row";
        }
        Spec spec;
        if (b) {
            spec = layoutParams.columnSpec;
        }
        else {
            spec = layoutParams.rowSpec;
        }
        final Interval span = spec.span;
        final int min = span.min;
        if (min != Integer.MIN_VALUE && min < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" indices must be positive");
            handleInvalidParams(sb.toString());
            throw null;
        }
        Axis axis;
        if (b) {
            axis = this.mHorizontalAxis;
        }
        else {
            axis = this.mVerticalAxis;
        }
        final int definedCount = axis.definedCount;
        if (definedCount != Integer.MIN_VALUE) {
            if (span.max > definedCount) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(" indices (start + span) mustn't exceed the ");
                sb2.append(str);
                sb2.append(" count");
                handleInvalidParams(sb2.toString());
                throw null;
            }
            if (span.size() > definedCount) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(" span mustn't exceed the ");
                sb3.append(str);
                sb3.append(" count");
                handleInvalidParams(sb3.toString());
                throw null;
            }
        }
    }
    
    private static int clip(final Interval interval, final boolean b, final int b2) {
        final int size = interval.size();
        if (b2 == 0) {
            return size;
        }
        int min;
        if (b) {
            min = Math.min(interval.min, b2);
        }
        else {
            min = 0;
        }
        return Math.min(size, b2 - min);
    }
    
    private int computeLayoutParamsHashCode() {
        final int childCount = this.getChildCount();
        int n = 1;
        for (int i = 0; i < childCount; ++i) {
            n = n * 31 + this.getChildAt(i).getLayoutParams().hashCode();
        }
        return n;
    }
    
    private void consistencyCheck() {
        final int mLastLayoutParamsHashCode = this.mLastLayoutParamsHashCode;
        if (mLastLayoutParamsHashCode == 0) {
            this.validateLayoutParams();
            this.mLastLayoutParamsHashCode = this.computeLayoutParamsHashCode();
        }
        else if (mLastLayoutParamsHashCode != this.computeLayoutParamsHashCode()) {
            this.invalidateStructure();
            this.consistencyCheck();
        }
    }
    
    private static Alignment createSwitchingAlignment(final Alignment alignment) {
        return (Alignment)new Alignment() {
            public int getAlignmentValue(final Child child, final int n) {
                return alignment.getAlignmentValue(child, n);
            }
            
            @Override
            int getGravityOffset(final Child child, final int n) {
                return alignment.getGravityOffset(child, n);
            }
        };
    }
    
    private static boolean fits(final int[] array, final int n, int i, final int n2) {
        if (n2 > array.length) {
            return false;
        }
        while (i < n2) {
            if (array[i] > n) {
                return false;
            }
            ++i;
        }
        return true;
    }
    
    static Alignment getAlignment(int n, final boolean b) {
        int n2;
        if (b) {
            n2 = 7;
        }
        else {
            n2 = 112;
        }
        int n3;
        if (b) {
            n3 = 0;
        }
        else {
            n3 = 4;
        }
        n = (n & n2) >> n3;
        if (n == 1) {
            return TableLayout.CENTER;
        }
        if (n == 3) {
            Alignment alignment;
            if (b) {
                alignment = TableLayout.LEFT;
            }
            else {
                alignment = TableLayout.TOP;
            }
            return alignment;
        }
        if (n == 5) {
            Alignment alignment2;
            if (b) {
                alignment2 = TableLayout.RIGHT;
            }
            else {
                alignment2 = TableLayout.BOTTOM;
            }
            return alignment2;
        }
        if (n == 7) {
            return TableLayout.FILL;
        }
        if (n == 8388611) {
            return TableLayout.START;
        }
        if (n != 8388613) {
            return TableLayout.UNDEFINED_ALIGNMENT;
        }
        return TableLayout.END;
    }
    
    private int getDefaultMargin(final Child child, final LayoutParams layoutParams, final boolean b, final boolean b2) {
        final boolean mUseDefaultMargins = this.mUseDefaultMargins;
        final boolean b3 = false;
        if (!mUseDefaultMargins) {
            return 0;
        }
        Spec spec;
        if (b) {
            spec = layoutParams.columnSpec;
        }
        else {
            spec = layoutParams.rowSpec;
        }
        Axis axis;
        if (b) {
            axis = this.mHorizontalAxis;
        }
        else {
            axis = this.mVerticalAxis;
        }
        final Interval span = spec.span;
        if ((b && this.isRtl) != b2) {
            final boolean b4 = b3;
            if (span.min != 0) {
                return this.getDefaultMargin(child, b4, b, b2);
            }
        }
        else {
            final boolean b4 = b3;
            if (span.max != axis.getCount()) {
                return this.getDefaultMargin(child, b4, b, b2);
            }
        }
        final boolean b4 = true;
        return this.getDefaultMargin(child, b4, b, b2);
    }
    
    private int getDefaultMargin(final Child child, final boolean b, final boolean b2) {
        return this.mDefaultGap / 2;
    }
    
    private int getDefaultMargin(final Child child, final boolean b, final boolean b2, final boolean b3) {
        return this.getDefaultMargin(child, b2, b3);
    }
    
    private int getMargin(final Child child, final boolean b, final boolean b2) {
        if (this.mAlignmentMode == 1) {
            return this.getMargin1(child, b, b2);
        }
        Axis axis;
        if (b) {
            axis = this.mHorizontalAxis;
        }
        else {
            axis = this.mVerticalAxis;
        }
        int[] array;
        if (b2) {
            array = axis.getLeadingMargins();
        }
        else {
            array = axis.getTrailingMargins();
        }
        final LayoutParams layoutParams = child.getLayoutParams();
        Spec spec;
        if (b) {
            spec = layoutParams.columnSpec;
        }
        else {
            spec = layoutParams.rowSpec;
        }
        int n;
        if (b2) {
            n = spec.span.min;
        }
        else {
            n = spec.span.max;
        }
        return array[n];
    }
    
    private int getMeasurement(final Child child, final boolean b) {
        int n;
        if (b) {
            n = child.getMeasuredWidth();
        }
        else {
            n = child.getMeasuredHeight();
        }
        return n;
    }
    
    private int getTotalMargin(final Child child, final boolean b) {
        return this.getMargin(child, b, true) + this.getMargin(child, b, false);
    }
    
    private static void handleInvalidParams(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(". ");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void invalidateStructure() {
        this.mLastLayoutParamsHashCode = 0;
        this.mHorizontalAxis.invalidateStructure();
        this.mVerticalAxis.invalidateStructure();
        this.invalidateValues();
    }
    
    private void invalidateValues() {
        final Axis mHorizontalAxis = this.mHorizontalAxis;
        if (mHorizontalAxis != null && this.mVerticalAxis != null) {
            mHorizontalAxis.invalidateValues();
            this.mVerticalAxis.invalidateValues();
        }
    }
    
    static int max2(final int[] array, int max) {
        for (int length = array.length, i = 0; i < length; ++i) {
            max = Math.max(max, array[i]);
        }
        return max;
    }
    
    private void measureChildWithMargins2(final Child child, final int n, final int n2, final int n3, final int n4, final boolean b) {
        child.measure(this.getTotalMargin(child, true) + n3, this.getTotalMargin(child, false) + n4, b);
    }
    
    private void measureChildrenWithMargins(final int n, final int n2, final boolean b) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final Child child = this.getChildAt(i);
            final LayoutParams layoutParams = child.getLayoutParams();
            if (b) {
                final int size = View$MeasureSpec.getSize(n);
                int n3;
                if (this.colCount == 2) {
                    n3 = (int)(size / 2.0f) - this.itemPaddingLeft * 4;
                }
                else {
                    n3 = (int)(size / 1.5f);
                }
                child.setTextLayout(this.delegate.createTextLayout(child.cell, n3));
                if (child.textLayout != null) {
                    layoutParams.width = child.textWidth + this.itemPaddingLeft * 2;
                    layoutParams.height = child.textHeight + this.itemPaddingTop * 2;
                }
                else {
                    layoutParams.width = 0;
                    layoutParams.height = 0;
                }
                this.measureChildWithMargins2(child, n, n2, layoutParams.width, layoutParams.height, true);
            }
            else {
                final boolean b2 = this.mOrientation == 0;
                Spec spec;
                if (b2) {
                    spec = layoutParams.columnSpec;
                }
                else {
                    spec = layoutParams.rowSpec;
                }
                if (spec.getAbsoluteAlignment(b2) == TableLayout.FILL) {
                    final Interval span = spec.span;
                    Axis axis;
                    if (b2) {
                        axis = this.mHorizontalAxis;
                    }
                    else {
                        axis = this.mVerticalAxis;
                    }
                    final int[] locations = axis.getLocations();
                    final int n4 = locations[span.max] - locations[span.min] - this.getTotalMargin(child, b2);
                    if (b2) {
                        this.measureChildWithMargins2(child, n, n2, n4, layoutParams.height, false);
                    }
                    else {
                        this.measureChildWithMargins2(child, n, n2, layoutParams.width, n4, false);
                    }
                }
            }
        }
    }
    
    private static void procrusteanFill(final int[] a, final int a2, final int a3, final int val) {
        final int length = a.length;
        Arrays.fill(a, Math.min(a2, length), Math.min(a3, length), val);
    }
    
    private static void setCellGroup(final LayoutParams layoutParams, final int n, final int n2, final int n3, final int n4) {
        layoutParams.setRowSpecSpan(new Interval(n, n2 + n));
        layoutParams.setColumnSpecSpan(new Interval(n3, n4 + n3));
    }
    
    public static Spec spec(final int n) {
        return spec(n, 1);
    }
    
    public static Spec spec(final int n, final float n2) {
        return spec(n, 1, n2);
    }
    
    public static Spec spec(final int n, final int n2) {
        return spec(n, n2, TableLayout.UNDEFINED_ALIGNMENT);
    }
    
    public static Spec spec(final int n, final int n2, final float n3) {
        return spec(n, n2, TableLayout.UNDEFINED_ALIGNMENT, n3);
    }
    
    public static Spec spec(final int n, final int n2, final Alignment alignment) {
        return spec(n, n2, alignment, 0.0f);
    }
    
    public static Spec spec(final int n, final int n2, final Alignment alignment, final float n3) {
        return new Spec(n != Integer.MIN_VALUE, n, n2, alignment, n3);
    }
    
    public static Spec spec(final int n, final Alignment alignment) {
        return spec(n, 1, alignment);
    }
    
    public static Spec spec(final int n, final Alignment alignment, final float n2) {
        return spec(n, 1, alignment, n2);
    }
    
    private void validateLayoutParams() {
        final boolean b = this.mOrientation == 0;
        Axis axis;
        if (b) {
            axis = this.mHorizontalAxis;
        }
        else {
            axis = this.mVerticalAxis;
        }
        int definedCount = axis.definedCount;
        if (definedCount == Integer.MIN_VALUE) {
            definedCount = 0;
        }
        final int[] array = new int[definedCount];
        final int childCount = this.getChildCount();
        int i = 0;
        int min = 0;
        int min2 = 0;
        while (i < childCount) {
            final LayoutParams layoutParams = this.getChildAt(i).getLayoutParams();
            Spec spec;
            if (b) {
                spec = layoutParams.rowSpec;
            }
            else {
                spec = layoutParams.columnSpec;
            }
            final Interval span = spec.span;
            final boolean startDefined = spec.startDefined;
            final int size = span.size();
            if (startDefined) {
                min = span.min;
            }
            Spec spec2;
            if (b) {
                spec2 = layoutParams.columnSpec;
            }
            else {
                spec2 = layoutParams.rowSpec;
            }
            final Interval span2 = spec2.span;
            final boolean startDefined2 = spec2.startDefined;
            final int clip = clip(span2, startDefined2, definedCount);
            if (startDefined2) {
                min2 = span2.min;
            }
            int n = min;
            int n2 = min2;
            if (definedCount != 0) {
                int n3 = min;
                int n4 = min2;
                Label_0295: {
                    if (startDefined) {
                        n = min;
                        n2 = min2;
                        if (startDefined2) {
                            break Label_0295;
                        }
                        n4 = min2;
                        n3 = min;
                    }
                    while (true) {
                        final int n5 = n4 + clip;
                        n = n3;
                        n2 = n4;
                        if (fits(array, n3, n4, n5)) {
                            break;
                        }
                        if (startDefined2) {
                            ++n3;
                        }
                        else if (n5 <= definedCount) {
                            ++n4;
                        }
                        else {
                            ++n3;
                            n4 = 0;
                        }
                    }
                }
                procrusteanFill(array, n2, n2 + clip, n + size);
            }
            if (b) {
                setCellGroup(layoutParams, n, size, n2, clip);
            }
            else {
                setCellGroup(layoutParams, n2, clip, n, size);
            }
            min2 = n2 + clip;
            ++i;
            min = n;
        }
    }
    
    public void addChild(final int n, final int n2, final int n3, final int n4) {
        final Child e = new Child(this.childrens.size());
        final LayoutParams layoutParams = new LayoutParams();
        layoutParams.rowSpec = new Spec(false, new Interval(n2, n2 + n4), TableLayout.FILL, 0.0f);
        layoutParams.columnSpec = new Spec(false, new Interval(n, n + n3), TableLayout.FILL, 0.0f);
        e.layoutParams = layoutParams;
        this.childrens.add(e);
        this.invalidateStructure();
    }
    
    public void addChild(final TLRPC.TL_pageTableCell tl_pageTableCell, int rowspan, final int n, int n2) {
        if (n2 == 0) {
            n2 = 1;
        }
        final Child e = new Child(this.childrens.size());
        e.cell = tl_pageTableCell;
        final LayoutParams layoutParams = new LayoutParams();
        int rowspan2 = tl_pageTableCell.rowspan;
        if (rowspan2 == 0) {
            rowspan2 = 1;
        }
        layoutParams.rowSpec = new Spec(false, new Interval(n, rowspan2 + n), TableLayout.FILL, 0.0f);
        layoutParams.columnSpec = new Spec(false, new Interval(rowspan, n2 + rowspan), TableLayout.FILL, 1.0f);
        e.layoutParams = layoutParams;
        this.childrens.add(e);
        rowspan = tl_pageTableCell.rowspan;
        if (rowspan > 1) {
            this.rowSpans.add(new Point((float)n, (float)(rowspan + n)));
        }
        this.invalidateStructure();
    }
    
    public int getAlignmentMode() {
        return this.mAlignmentMode;
    }
    
    public Child getChildAt(final int index) {
        if (index >= 0 && index < this.childrens.size()) {
            return this.childrens.get(index);
        }
        return null;
    }
    
    public int getChildCount() {
        return this.childrens.size();
    }
    
    public int getColumnCount() {
        return this.mHorizontalAxis.getCount();
    }
    
    int getMargin1(final Child child, final boolean b, final boolean b2) {
        final LayoutParams layoutParams = child.getLayoutParams();
        int n;
        if (b) {
            if (b2) {
                n = layoutParams.leftMargin;
            }
            else {
                n = layoutParams.rightMargin;
            }
        }
        else if (b2) {
            n = layoutParams.topMargin;
        }
        else {
            n = layoutParams.bottomMargin;
        }
        int defaultMargin = n;
        if (n == Integer.MIN_VALUE) {
            defaultMargin = this.getDefaultMargin(child, layoutParams, b, b2);
        }
        return defaultMargin;
    }
    
    final int getMeasurementIncludingMargin(final Child child, final boolean b) {
        return this.getMeasurement(child, b) + this.getTotalMargin(child, b);
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public int getRowCount() {
        return this.mVerticalAxis.getCount();
    }
    
    public boolean getUseDefaultMargins() {
        return this.mUseDefaultMargins;
    }
    
    public boolean isColumnOrderPreserved() {
        return this.mHorizontalAxis.isOrderPreserved();
    }
    
    public boolean isRowOrderPreserved() {
        return this.mVerticalAxis.isOrderPreserved();
    }
    
    protected void onDraw(final Canvas canvas) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            this.getChildAt(i).draw(canvas);
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        this.consistencyCheck();
    }
    
    protected void onMeasure(int i, int j) {
        this.consistencyCheck();
        this.invalidateValues();
        this.colCount = 0;
        for (int childCount = this.getChildCount(), k = 0; k < childCount; ++k) {
            this.colCount = Math.max(this.colCount, this.getChildAt(k).layoutParams.columnSpec.span.max);
        }
        this.measureChildrenWithMargins(i, j, true);
        int a;
        if (this.mOrientation == 0) {
            a = this.mHorizontalAxis.getMeasure(i);
            this.measureChildrenWithMargins(i, j, false);
            j = this.mVerticalAxis.getMeasure(j);
        }
        else {
            final int measure = this.mVerticalAxis.getMeasure(j);
            this.measureChildrenWithMargins(i, j, false);
            a = this.mHorizontalAxis.getMeasure(i);
            j = measure;
        }
        final int max = Math.max(a, View$MeasureSpec.getSize(i));
        i = Math.max(j, this.getSuggestedMinimumHeight());
        this.setMeasuredDimension(max, i);
        this.mHorizontalAxis.layout(max);
        this.mVerticalAxis.layout(i);
        final int[] locations = this.mHorizontalAxis.getLocations();
        final int[] locations2 = this.mVerticalAxis.getLocations();
        this.cellsToFixHeight.clear();
        final int n = locations[locations.length - 1];
        int childCount2;
        Child child;
        LayoutParams layoutParams;
        Spec columnSpec;
        Spec rowSpec;
        Interval span;
        Interval span2;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int measurement;
        int measurement2;
        Alignment access$1200;
        Alignment access$1201;
        Bounds bounds;
        Bounds bounds2;
        int gravityOffset;
        int gravityOffset2;
        int margin;
        int margin2;
        int margin3;
        int margin4;
        int n8;
        int n9;
        int offset;
        int offset2;
        int sizeInCell;
        int sizeInCell2;
        int n10;
        int n11;
        int n12;
        int size;
        int l;
        Point point;
        boolean b;
        for (childCount2 = this.getChildCount(), j = 0; j < childCount2; ++j) {
            child = this.getChildAt(j);
            layoutParams = child.getLayoutParams();
            columnSpec = layoutParams.columnSpec;
            rowSpec = layoutParams.rowSpec;
            span = columnSpec.span;
            span2 = rowSpec.span;
            n2 = locations[span.min];
            n3 = locations2[span2.min];
            n4 = locations[span.max];
            n5 = locations2[span2.max];
            n6 = n4 - n2;
            n7 = n5 - n3;
            measurement = this.getMeasurement(child, true);
            measurement2 = this.getMeasurement(child, false);
            access$1200 = columnSpec.getAbsoluteAlignment(true);
            access$1201 = rowSpec.getAbsoluteAlignment(false);
            bounds = (Bounds)this.mHorizontalAxis.getGroupBounds().getValue(j);
            bounds2 = (Bounds)this.mVerticalAxis.getGroupBounds().getValue(j);
            gravityOffset = access$1200.getGravityOffset(child, n6 - bounds.size(true));
            gravityOffset2 = access$1201.getGravityOffset(child, n7 - bounds2.size(true));
            margin = this.getMargin(child, true, true);
            margin2 = this.getMargin(child, false, true);
            margin3 = this.getMargin(child, true, false);
            margin4 = this.getMargin(child, false, false);
            n8 = margin + margin3;
            n9 = margin2 + margin4;
            offset = bounds.getOffset(this, child, access$1200, measurement + n8, true);
            offset2 = bounds2.getOffset(this, child, access$1201, measurement2 + n9, false);
            sizeInCell = access$1200.getSizeInCell(child, measurement, n6 - n8);
            sizeInCell2 = access$1201.getSizeInCell(child, measurement2, n7 - n9);
            n10 = n2 + gravityOffset + offset;
            if (!this.isRtl) {
                n11 = margin + n10;
            }
            else {
                n11 = n - sizeInCell - margin3 - n10;
            }
            n12 = n3 + gravityOffset2 + offset2 + margin2;
            Label_0800: {
                if (child.cell != null) {
                    if (sizeInCell != child.getMeasuredWidth() || sizeInCell2 != child.getMeasuredHeight()) {
                        child.measure(sizeInCell, sizeInCell2, false);
                    }
                    if (child.fixedHeight != 0 && child.fixedHeight != sizeInCell2 && child.layoutParams.rowSpec.span.max - child.layoutParams.rowSpec.span.min <= 1) {
                        size = this.rowSpans.size();
                        l = 0;
                        while (true) {
                            while (l < size) {
                                point = this.rowSpans.get(l);
                                if (point.x <= child.layoutParams.rowSpec.span.min && point.y > child.layoutParams.rowSpec.span.min) {
                                    b = true;
                                    if (!b) {
                                        this.cellsToFixHeight.add(child);
                                    }
                                    break Label_0800;
                                }
                                else {
                                    ++l;
                                }
                            }
                            b = false;
                            continue;
                        }
                    }
                }
            }
            child.layout(n11, n12, sizeInCell + n11, sizeInCell2 + n12);
        }
        int size2 = this.cellsToFixHeight.size();
        j = 0;
        int n13 = i;
        i = j;
    Label_0841:
        while (i < size2) {
            final Child child2 = this.cellsToFixHeight.get(i);
            j = child2.measuredHeight - child2.fixedHeight;
            while (true) {
                int min2;
                for (int index = child2.index + 1; index < this.childrens.size(); ++index, j = min2) {
                    final Child child3 = this.childrens.get(index);
                    if (child2.layoutParams.rowSpec.span.min != child3.layoutParams.rowSpec.span.min) {
                        break;
                    }
                    if (child2.fixedHeight < child3.fixedHeight) {
                        final int n14 = 1;
                        int n15 = j;
                        boolean b2 = n14 != 0;
                        if (n14 == 0) {
                            int index2 = child2.index - 1;
                            while (true) {
                                n15 = j;
                                b2 = (n14 != 0);
                                if (index2 < 0) {
                                    break;
                                }
                                final Child child4 = this.childrens.get(index2);
                                n15 = j;
                                b2 = (n14 != 0);
                                if (child2.layoutParams.rowSpec.span.min != child4.layoutParams.rowSpec.span.min) {
                                    break;
                                }
                                if (child2.fixedHeight < child4.fixedHeight) {
                                    b2 = true;
                                    n15 = j;
                                    break;
                                }
                                final int b3 = child4.measuredHeight - child4.fixedHeight;
                                int min = j;
                                if (b3 > 0) {
                                    min = Math.min(j, b3);
                                }
                                --index2;
                                j = min;
                            }
                        }
                        if (!b2) {
                            child2.setFixedHeight(child2.fixedHeight);
                            final int n16 = n13 - n15;
                            final int size3 = this.childrens.size();
                            j = size2;
                            int n17;
                            int n18;
                            for (int index3 = 0; index3 < size3; ++index3, i = n17, j = n18) {
                                final Child o = this.childrens.get(index3);
                                if (child2 == o) {
                                    n17 = i;
                                    n18 = j;
                                }
                                else if (child2.layoutParams.rowSpec.span.min == o.layoutParams.rowSpec.span.min) {
                                    n17 = i;
                                    n18 = j;
                                    if (o.fixedHeight != o.measuredHeight) {
                                        this.cellsToFixHeight.remove(o);
                                        n17 = i;
                                        if (o.index < child2.index) {
                                            n17 = i - 1;
                                        }
                                        n18 = j - 1;
                                    }
                                    o.measuredHeight -= n15;
                                    o.measure(o.measuredWidth, o.measuredHeight, true);
                                }
                                else {
                                    n17 = i;
                                    n18 = j;
                                    if (child2.layoutParams.rowSpec.span.min < o.layoutParams.rowSpec.span.min) {
                                        o.y -= n15;
                                        n18 = j;
                                        n17 = i;
                                    }
                                }
                            }
                            size2 = j;
                            n13 = n16;
                        }
                        ++i;
                        continue Label_0841;
                    }
                    final int b4 = child3.measuredHeight - child3.fixedHeight;
                    min2 = j;
                    if (b4 > 0) {
                        min2 = Math.min(j, b4);
                    }
                }
                final int n14 = false ? 1 : 0;
                continue;
            }
        }
        this.setMeasuredDimension(n, n13);
    }
    
    public void removeAllChildrens() {
        this.childrens.clear();
        this.rowSpans.clear();
        this.invalidateStructure();
    }
    
    public void requestLayout() {
        super.requestLayout();
        this.invalidateValues();
    }
    
    public void setAlignmentMode(final int mAlignmentMode) {
        this.mAlignmentMode = mAlignmentMode;
        this.requestLayout();
    }
    
    public void setColumnCount(final int count) {
        this.mHorizontalAxis.setCount(count);
        this.invalidateStructure();
        this.requestLayout();
    }
    
    public void setColumnOrderPreserved(final boolean orderPreserved) {
        this.mHorizontalAxis.setOrderPreserved(orderPreserved);
        this.invalidateStructure();
        this.requestLayout();
    }
    
    public void setDrawLines(final boolean drawLines) {
        this.drawLines = drawLines;
    }
    
    public void setOrientation(final int mOrientation) {
        if (this.mOrientation != mOrientation) {
            this.mOrientation = mOrientation;
            this.invalidateStructure();
            this.requestLayout();
        }
    }
    
    public void setRowCount(final int count) {
        this.mVerticalAxis.setCount(count);
        this.invalidateStructure();
        this.requestLayout();
    }
    
    public void setRowOrderPreserved(final boolean orderPreserved) {
        this.mVerticalAxis.setOrderPreserved(orderPreserved);
        this.invalidateStructure();
        this.requestLayout();
    }
    
    public void setRtl(final boolean isRtl) {
        this.isRtl = isRtl;
    }
    
    public void setStriped(final boolean isStriped) {
        this.isStriped = isStriped;
    }
    
    public void setUseDefaultMargins(final boolean mUseDefaultMargins) {
        this.mUseDefaultMargins = mUseDefaultMargins;
        this.requestLayout();
    }
    
    public abstract static class Alignment
    {
        Alignment() {
        }
        
        abstract int getAlignmentValue(final Child p0, final int p1);
        
        Bounds getBounds() {
            return new Bounds();
        }
        
        abstract int getGravityOffset(final Child p0, final int p1);
        
        int getSizeInCell(final Child child, final int n, final int n2) {
            return n;
        }
    }
    
    static final class Arc
    {
        public final Interval span;
        public boolean valid;
        public final MutableInt value;
        
        public Arc(final Interval span, final MutableInt value) {
            this.valid = true;
            this.span = span;
            this.value = value;
        }
    }
    
    static final class Assoc<K, V> extends ArrayList<Pair<K, V>>
    {
        private final Class<K> keyType;
        private final Class<V> valueType;
        
        private Assoc(final Class<K> keyType, final Class<V> valueType) {
            this.keyType = keyType;
            this.valueType = valueType;
        }
        
        public static <K, V> Assoc<K, V> of(final Class<K> clazz, final Class<V> clazz2) {
            return new Assoc<K, V>(clazz, clazz2);
        }
        
        public PackedMap<K, V> pack() {
            final int size = this.size();
            final Object[] array = (Object[])Array.newInstance(this.keyType, size);
            final Object[] array2 = (Object[])Array.newInstance(this.valueType, size);
            for (int i = 0; i < size; ++i) {
                array[i] = this.get(i).first;
                array2[i] = this.get(i).second;
            }
            return (PackedMap<K, V>)new PackedMap(array, array2);
        }
        
        public void put(final K k, final V v) {
            this.add((Pair<K, V>)Pair.create((Object)k, (Object)v));
        }
    }
    
    final class Axis
    {
        private static final int COMPLETE = 2;
        private static final int NEW = 0;
        private static final int PENDING = 1;
        public Arc[] arcs;
        public boolean arcsValid;
        PackedMap<Interval, MutableInt> backwardLinks;
        public boolean backwardLinksValid;
        public int definedCount;
        public int[] deltas;
        PackedMap<Interval, MutableInt> forwardLinks;
        public boolean forwardLinksValid;
        PackedMap<Spec, Bounds> groupBounds;
        public boolean groupBoundsValid;
        public boolean hasWeights;
        public boolean hasWeightsValid;
        public final boolean horizontal;
        public int[] leadingMargins;
        public boolean leadingMarginsValid;
        public int[] locations;
        public boolean locationsValid;
        private int maxIndex;
        boolean orderPreserved;
        private MutableInt parentMax;
        private MutableInt parentMin;
        public int[] trailingMargins;
        public boolean trailingMarginsValid;
        
        private Axis(final boolean horizontal) {
            this.definedCount = Integer.MIN_VALUE;
            this.maxIndex = Integer.MIN_VALUE;
            this.groupBoundsValid = false;
            this.forwardLinksValid = false;
            this.backwardLinksValid = false;
            this.leadingMarginsValid = false;
            this.trailingMarginsValid = false;
            this.arcsValid = false;
            this.locationsValid = false;
            this.hasWeightsValid = false;
            this.orderPreserved = true;
            this.parentMin = new MutableInt(0);
            this.parentMax = new MutableInt(-100000);
            this.horizontal = horizontal;
        }
        
        private void addComponentSizes(final List<Arc> list, final PackedMap<Interval, MutableInt> packedMap) {
            int n = 0;
            while (true) {
                final Interval[] keys = packedMap.keys;
                if (n >= keys.length) {
                    break;
                }
                this.include(list, keys[n], packedMap.values[n], false);
                ++n;
            }
        }
        
        private int calculateMaxIndex() {
            final int childCount = TableLayout.this.getChildCount();
            int i = 0;
            int max = -1;
            while (i < childCount) {
                final LayoutParams layoutParams = TableLayout.this.getChildAt(i).getLayoutParams();
                Spec spec;
                if (this.horizontal) {
                    spec = layoutParams.columnSpec;
                }
                else {
                    spec = layoutParams.rowSpec;
                }
                final Interval span = spec.span;
                max = Math.max(Math.max(Math.max(max, span.min), span.max), span.size());
                ++i;
            }
            int n;
            if ((n = max) == -1) {
                n = Integer.MIN_VALUE;
            }
            return n;
        }
        
        private float calculateTotalWeight() {
            final int childCount = TableLayout.this.getChildCount();
            float n = 0.0f;
            for (int i = 0; i < childCount; ++i) {
                final LayoutParams layoutParams = TableLayout.this.getChildAt(i).getLayoutParams();
                Spec spec;
                if (this.horizontal) {
                    spec = layoutParams.columnSpec;
                }
                else {
                    spec = layoutParams.rowSpec;
                }
                n += spec.weight;
            }
            return n;
        }
        
        private void computeArcs() {
            this.getForwardLinks();
            this.getBackwardLinks();
        }
        
        private void computeGroupBounds() {
            final Bounds[] array = this.groupBounds.values;
            for (int i = 0; i < array.length; ++i) {
                array[i].reset();
            }
            for (int childCount = TableLayout.this.getChildCount(), j = 0; j < childCount; ++j) {
                final Child child = TableLayout.this.getChildAt(j);
                final LayoutParams layoutParams = child.getLayoutParams();
                Spec spec;
                if (this.horizontal) {
                    spec = layoutParams.columnSpec;
                }
                else {
                    spec = layoutParams.rowSpec;
                }
                final int measurementIncludingMargin = TableLayout.this.getMeasurementIncludingMargin(child, this.horizontal);
                int n;
                if (spec.weight == 0.0f) {
                    n = 0;
                }
                else {
                    n = this.deltas[j];
                }
                this.groupBounds.getValue(j).include(TableLayout.this, child, spec, this, measurementIncludingMargin + n);
            }
        }
        
        private boolean computeHasWeights() {
            for (int childCount = TableLayout.this.getChildCount(), i = 0; i < childCount; ++i) {
                final LayoutParams layoutParams = TableLayout.this.getChildAt(i).getLayoutParams();
                Spec spec;
                if (this.horizontal) {
                    spec = layoutParams.columnSpec;
                }
                else {
                    spec = layoutParams.rowSpec;
                }
                if (spec.weight != 0.0f) {
                    return true;
                }
            }
            return false;
        }
        
        private void computeLinks(final PackedMap<Interval, MutableInt> packedMap, final boolean b) {
            final MutableInt[] array = packedMap.values;
            final int n = 0;
            for (int i = 0; i < array.length; ++i) {
                array[i].reset();
            }
            final Bounds[] array2 = this.getGroupBounds().values;
            for (int j = n; j < array2.length; ++j) {
                int size = array2[j].size(b);
                final MutableInt mutableInt = packedMap.getValue(j);
                final int value = mutableInt.value;
                if (!b) {
                    size = -size;
                }
                mutableInt.value = Math.max(value, size);
            }
        }
        
        private void computeLocations(final int[] array) {
            if (!this.hasWeights()) {
                this.solve(array);
            }
            else {
                this.solveAndDistributeSpace(array);
            }
            if (!this.orderPreserved) {
                int i = 0;
                final int n = array[0];
                while (i < array.length) {
                    array[i] -= n;
                    ++i;
                }
            }
        }
        
        private void computeMargins(final boolean b) {
            int[] array;
            if (b) {
                array = this.leadingMargins;
            }
            else {
                array = this.trailingMargins;
            }
            for (int i = 0; i < TableLayout.this.getChildCount(); ++i) {
                final Child child = TableLayout.this.getChildAt(i);
                final LayoutParams layoutParams = child.getLayoutParams();
                Spec spec;
                if (this.horizontal) {
                    spec = layoutParams.columnSpec;
                }
                else {
                    spec = layoutParams.rowSpec;
                }
                final Interval span = spec.span;
                int n;
                if (b) {
                    n = span.min;
                }
                else {
                    n = span.max;
                }
                array[n] = Math.max(array[n], TableLayout.this.getMargin1(child, this.horizontal, b));
            }
        }
        
        private Arc[] createArcs() {
            final ArrayList<Arc> list = new ArrayList<Arc>();
            final ArrayList<Arc> list2 = new ArrayList<Arc>();
            this.addComponentSizes(list, this.getForwardLinks());
            this.addComponentSizes(list2, this.getBackwardLinks());
            if (this.orderPreserved) {
                int n;
                for (int i = 0; i < this.getCount(); i = n) {
                    n = i + 1;
                    this.include(list, new Interval(i, n), new MutableInt(0));
                }
            }
            final int count = this.getCount();
            this.include(list, new Interval(0, count), this.parentMin, false);
            this.include(list2, new Interval(count, 0), this.parentMax, false);
            return TableLayout.append(this.topologicalSort(list), this.topologicalSort(list2));
        }
        
        private PackedMap<Spec, Bounds> createGroupBounds() {
            final Assoc<Spec, Bounds> of = Assoc.of(Spec.class, Bounds.class);
            for (int childCount = TableLayout.this.getChildCount(), i = 0; i < childCount; ++i) {
                final LayoutParams layoutParams = TableLayout.this.getChildAt(i).getLayoutParams();
                Spec spec;
                if (this.horizontal) {
                    spec = layoutParams.columnSpec;
                }
                else {
                    spec = layoutParams.rowSpec;
                }
                of.put(spec, spec.getAbsoluteAlignment(this.horizontal).getBounds());
            }
            return of.pack();
        }
        
        private PackedMap<Interval, MutableInt> createLinks(final boolean b) {
            final Assoc<Interval, MutableInt> of = Assoc.of(Interval.class, MutableInt.class);
            final Spec[] array = this.getGroupBounds().keys;
            for (int length = array.length, i = 0; i < length; ++i) {
                Object o;
                if (b) {
                    o = array[i].span;
                }
                else {
                    o = array[i].span.inverse();
                }
                of.put((Interval)o, new MutableInt());
            }
            return of.pack();
        }
        
        private PackedMap<Interval, MutableInt> getBackwardLinks() {
            if (this.backwardLinks == null) {
                this.backwardLinks = this.createLinks(false);
            }
            if (!this.backwardLinksValid) {
                this.computeLinks(this.backwardLinks, false);
                this.backwardLinksValid = true;
            }
            return this.backwardLinks;
        }
        
        private PackedMap<Interval, MutableInt> getForwardLinks() {
            if (this.forwardLinks == null) {
                this.forwardLinks = this.createLinks(true);
            }
            if (!this.forwardLinksValid) {
                this.computeLinks(this.forwardLinks, true);
                this.forwardLinksValid = true;
            }
            return this.forwardLinks;
        }
        
        private int getMaxIndex() {
            if (this.maxIndex == Integer.MIN_VALUE) {
                this.maxIndex = Math.max(0, this.calculateMaxIndex());
            }
            return this.maxIndex;
        }
        
        private int getMeasure(final int n, final int n2) {
            this.setParentConstraints(n, n2);
            return this.size(this.getLocations());
        }
        
        private boolean hasWeights() {
            if (!this.hasWeightsValid) {
                this.hasWeights = this.computeHasWeights();
                this.hasWeightsValid = true;
            }
            return this.hasWeights;
        }
        
        private void include(final List<Arc> list, final Interval interval, final MutableInt mutableInt) {
            this.include(list, interval, mutableInt, true);
        }
        
        private void include(final List<Arc> list, final Interval interval, final MutableInt mutableInt, final boolean b) {
            if (interval.size() == 0) {
                return;
            }
            if (b) {
                final Iterator<Arc> iterator = list.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().span.equals(interval)) {
                        return;
                    }
                }
            }
            list.add(new Arc(interval, mutableInt));
        }
        
        private void init(final int[] a) {
            Arrays.fill(a, 0);
        }
        
        private boolean relax(final int[] array, final Arc arc) {
            if (!arc.valid) {
                return false;
            }
            final Interval span = arc.span;
            final int min = span.min;
            final int max = span.max;
            final int n = array[min] + arc.value.value;
            if (n > array[max]) {
                array[max] = n;
                return true;
            }
            return false;
        }
        
        private void setParentConstraints(final int value, final int n) {
            this.parentMin.value = value;
            this.parentMax.value = -n;
            this.locationsValid = false;
        }
        
        private void shareOutDelta(int i, float n) {
            final int[] deltas = this.deltas;
            final int n2 = 0;
            Arrays.fill(deltas, 0);
            final int childCount = TableLayout.this.getChildCount();
            int n3 = i;
            LayoutParams layoutParams;
            Spec spec;
            float weight;
            int n4;
            float n5;
            int round;
            for (i = n2; i < childCount; ++i, n3 = n4, n = n5) {
                layoutParams = TableLayout.this.getChildAt(i).getLayoutParams();
                if (this.horizontal) {
                    spec = layoutParams.columnSpec;
                }
                else {
                    spec = layoutParams.rowSpec;
                }
                weight = spec.weight;
                n4 = n3;
                n5 = n;
                if (weight != 0.0f) {
                    round = Math.round(n3 * weight / n);
                    this.deltas[i] = round;
                    n4 = n3 - round;
                    n5 = n - weight;
                }
            }
        }
        
        private int size(final int[] array) {
            return array[this.getCount()];
        }
        
        private boolean solve(final int[] array) {
            return this.solve(this.getArcs(), array);
        }
        
        private boolean solve(final Arc[] array, final int[] array2) {
            return this.solve(array, array2, true);
        }
        
        private boolean solve(final Arc[] array, final int[] array2, final boolean b) {
            final int n = this.getCount() + 1;
            for (int i = 0; i < array.length; ++i) {
                this.init(array2);
                for (int j = 0; j < n; ++j) {
                    final int length = array.length;
                    int k = 0;
                    boolean b2 = false;
                    while (k < length) {
                        b2 |= this.relax(array2, array[k]);
                        ++k;
                    }
                    if (!b2) {
                        return true;
                    }
                }
                if (!b) {
                    return false;
                }
                final boolean[] array3 = new boolean[array.length];
                for (int l = 0; l < n; ++l) {
                    for (int length2 = array.length, n2 = 0; n2 < length2; ++n2) {
                        array3[n2] |= this.relax(array2, array[n2]);
                    }
                }
                for (int n3 = 0; n3 < array.length; ++n3) {
                    if (array3[n3]) {
                        final Arc arc = array[n3];
                        final Interval span = arc.span;
                        if (span.min >= span.max) {
                            arc.valid = false;
                            break;
                        }
                    }
                }
            }
            return true;
        }
        
        private void solveAndDistributeSpace(final int[] array) {
            Arrays.fill(this.getDeltas(), 0);
            this.solve(array);
            int n = this.parentMin.value * TableLayout.this.getChildCount() + 1;
            if (n < 2) {
                return;
            }
            final float calculateTotalWeight = this.calculateTotalWeight();
            int n2 = -1;
            int i = 0;
            boolean solve = true;
            while (i < n) {
                final int n3 = (int)((i + (long)n) / 2L);
                this.invalidateValues();
                this.shareOutDelta(n3, calculateTotalWeight);
                solve = this.solve(this.getArcs(), array, false);
                if (solve) {
                    i = n3 + 1;
                    n2 = n3;
                }
                else {
                    n = n3;
                }
            }
            if (n2 > 0 && !solve) {
                this.invalidateValues();
                this.shareOutDelta(n2, calculateTotalWeight);
                this.solve(array);
            }
        }
        
        private Arc[] topologicalSort(final List<Arc> list) {
            return this.topologicalSort(list.toArray(new Arc[list.size()]));
        }
        
        private Arc[] topologicalSort(final Arc[] array) {
            return new Object() {
                Arc[][] arcsByVertex;
                int cursor;
                Arc[] result;
                int[] visited;
                
                {
                    final Arc[] val$arcs2 = array;
                    this.result = new Arc[val$arcs2.length];
                    this.cursor = this.result.length - 1;
                    this.arcsByVertex = Axis.this.groupArcsByFirstVertex(val$arcs2);
                    this.visited = new int[Axis.this.getCount() + 1];
                }
                
                Arc[] sort() {
                    for (int length = this.arcsByVertex.length, i = 0; i < length; ++i) {
                        this.walk(i);
                    }
                    return this.result;
                }
                
                void walk(final int n) {
                    final int[] visited = this.visited;
                    if (visited[n] == 0) {
                        visited[n] = 1;
                        for (final Arc arc : this.arcsByVertex[n]) {
                            this.walk(arc.span.max);
                            this.result[this.cursor--] = arc;
                        }
                        this.visited[n] = 2;
                    }
                }
            }.sort();
        }
        
        public Arc[] getArcs() {
            if (this.arcs == null) {
                this.arcs = this.createArcs();
            }
            if (!this.arcsValid) {
                this.computeArcs();
                this.arcsValid = true;
            }
            return this.arcs;
        }
        
        public int getCount() {
            return Math.max(this.definedCount, this.getMaxIndex());
        }
        
        public int[] getDeltas() {
            if (this.deltas == null) {
                this.deltas = new int[TableLayout.this.getChildCount()];
            }
            return this.deltas;
        }
        
        public PackedMap<Spec, Bounds> getGroupBounds() {
            if (this.groupBounds == null) {
                this.groupBounds = this.createGroupBounds();
            }
            if (!this.groupBoundsValid) {
                this.computeGroupBounds();
                this.groupBoundsValid = true;
            }
            return this.groupBounds;
        }
        
        public int[] getLeadingMargins() {
            if (this.leadingMargins == null) {
                this.leadingMargins = new int[this.getCount() + 1];
            }
            if (!this.leadingMarginsValid) {
                this.computeMargins(true);
                this.leadingMarginsValid = true;
            }
            return this.leadingMargins;
        }
        
        public int[] getLocations() {
            if (this.locations == null) {
                this.locations = new int[this.getCount() + 1];
            }
            if (!this.locationsValid) {
                this.computeLocations(this.locations);
                this.locationsValid = true;
            }
            return this.locations;
        }
        
        public int getMeasure(int size) {
            final int mode = View$MeasureSpec.getMode(size);
            size = View$MeasureSpec.getSize(size);
            if (mode == Integer.MIN_VALUE) {
                return this.getMeasure(0, size);
            }
            if (mode == 0) {
                return this.getMeasure(0, 100000);
            }
            if (mode != 1073741824) {
                return 0;
            }
            return this.getMeasure(size, size);
        }
        
        public int[] getTrailingMargins() {
            if (this.trailingMargins == null) {
                this.trailingMargins = new int[this.getCount() + 1];
            }
            if (!this.trailingMarginsValid) {
                this.computeMargins(false);
                this.trailingMarginsValid = true;
            }
            return this.trailingMargins;
        }
        
        Arc[][] groupArcsByFirstVertex(final Arc[] array) {
            final int n = this.getCount() + 1;
            final Arc[][] array2 = new Arc[n][];
            final int[] a = new int[n];
            final int length = array.length;
            final int n2 = 0;
            for (int i = 0; i < length; ++i) {
                final int min = array[i].span.min;
                ++a[min];
            }
            for (int j = 0; j < a.length; ++j) {
                array2[j] = new Arc[a[j]];
            }
            Arrays.fill(a, 0);
            for (int length2 = array.length, k = n2; k < length2; ++k) {
                final Arc arc = array[k];
                final int min2 = arc.span.min;
                array2[min2][a[min2]++] = arc;
            }
            return array2;
        }
        
        public void invalidateStructure() {
            this.maxIndex = Integer.MIN_VALUE;
            this.groupBounds = null;
            this.forwardLinks = null;
            this.backwardLinks = null;
            this.leadingMargins = null;
            this.trailingMargins = null;
            this.arcs = null;
            this.locations = null;
            this.deltas = null;
            this.hasWeightsValid = false;
            this.invalidateValues();
        }
        
        public void invalidateValues() {
            this.groupBoundsValid = false;
            this.forwardLinksValid = false;
            this.backwardLinksValid = false;
            this.leadingMarginsValid = false;
            this.trailingMarginsValid = false;
            this.arcsValid = false;
            this.locationsValid = false;
        }
        
        public boolean isOrderPreserved() {
            return this.orderPreserved;
        }
        
        public void layout(final int n) {
            this.setParentConstraints(n, n);
            this.getLocations();
        }
        
        public void setCount(final int definedCount) {
            if (definedCount != Integer.MIN_VALUE && definedCount < this.getMaxIndex()) {
                final StringBuilder sb = new StringBuilder();
                String str;
                if (this.horizontal) {
                    str = "column";
                }
                else {
                    str = "row";
                }
                sb.append(str);
                sb.append("Count must be greater than or equal to the maximum of all grid indices (and spans) defined in the LayoutParams of each child");
                TableLayout.access$1700(sb.toString());
                throw null;
            }
            this.definedCount = definedCount;
        }
        
        public void setOrderPreserved(final boolean orderPreserved) {
            this.orderPreserved = orderPreserved;
            this.invalidateStructure();
        }
    }
    
    static class Bounds
    {
        public int after;
        public int before;
        public int flexibility;
        
        private Bounds() {
            this.reset();
        }
        
        protected int getOffset(final TableLayout tableLayout, final Child child, final Alignment alignment, final int n, final boolean b) {
            return this.before - alignment.getAlignmentValue(child, n);
        }
        
        protected void include(final int b, final int b2) {
            this.before = Math.max(this.before, b);
            this.after = Math.max(this.after, b2);
        }
        
        protected final void include(final TableLayout tableLayout, final Child child, final Spec spec, final Axis axis, final int n) {
            this.flexibility &= spec.getFlexibility();
            final int alignmentValue = spec.getAbsoluteAlignment(axis.horizontal).getAlignmentValue(child, n);
            this.include(alignmentValue, n - alignmentValue);
        }
        
        protected void reset() {
            this.before = Integer.MIN_VALUE;
            this.after = Integer.MIN_VALUE;
            this.flexibility = 2;
        }
        
        protected int size(final boolean b) {
            if (!b && TableLayout.canStretch(this.flexibility)) {
                return 100000;
            }
            return this.before + this.after;
        }
    }
    
    public class Child
    {
        private TLRPC.TL_pageTableCell cell;
        private int fixedHeight;
        private int index;
        private LayoutParams layoutParams;
        private int measuredHeight;
        private int measuredWidth;
        public int textHeight;
        public ArticleViewer.DrawingText textLayout;
        public int textLeft;
        public int textWidth;
        public int textX;
        public int textY;
        public int x;
        public int y;
        
        public Child(final int index) {
            this.index = index;
        }
        
        public void draw(final Canvas canvas) {
            if (this.cell == null) {
                return;
            }
            final int x = this.x;
            final int measuredWidth = this.measuredWidth;
            final int measuredWidth2 = TableLayout.this.getMeasuredWidth();
            boolean b = false;
            final boolean b2 = x + measuredWidth == measuredWidth2;
            final boolean b3 = this.y + this.measuredHeight == TableLayout.this.getMeasuredHeight();
            final int dp = AndroidUtilities.dp(3.0f);
            if (this.cell.header || (TableLayout.this.isStriped && this.layoutParams.rowSpec.span.min % 2 == 0)) {
                if (this.x == 0 && this.y == 0) {
                    TableLayout.this.radii[0] = (TableLayout.this.radii[1] = (float)dp);
                    b = true;
                }
                else {
                    TableLayout.this.radii[0] = (TableLayout.this.radii[1] = 0.0f);
                }
                if (b2 && this.y == 0) {
                    TableLayout.this.radii[2] = (TableLayout.this.radii[3] = (float)dp);
                    b = true;
                }
                else {
                    TableLayout.this.radii[2] = (TableLayout.this.radii[3] = 0.0f);
                }
                if (b2 && b3) {
                    TableLayout.this.radii[4] = (TableLayout.this.radii[5] = (float)dp);
                    b = true;
                }
                else {
                    TableLayout.this.radii[4] = (TableLayout.this.radii[5] = 0.0f);
                }
                if (this.x == 0 && b3) {
                    TableLayout.this.radii[6] = (TableLayout.this.radii[7] = (float)dp);
                    b = true;
                }
                else {
                    TableLayout.this.radii[6] = (TableLayout.this.radii[7] = 0.0f);
                }
                if (b) {
                    final RectF access$600 = TableLayout.this.rect;
                    final int x2 = this.x;
                    final float n = (float)x2;
                    final int y = this.y;
                    access$600.set(n, (float)y, (float)(x2 + this.measuredWidth), (float)(y + this.measuredHeight));
                    TableLayout.this.backgroundPath.reset();
                    TableLayout.this.backgroundPath.addRoundRect(TableLayout.this.rect, TableLayout.this.radii, Path$Direction.CW);
                    if (this.cell.header) {
                        canvas.drawPath(TableLayout.this.backgroundPath, TableLayout.this.delegate.getHeaderPaint());
                    }
                    else {
                        canvas.drawPath(TableLayout.this.backgroundPath, TableLayout.this.delegate.getStripPaint());
                    }
                }
                else if (this.cell.header) {
                    final int x3 = this.x;
                    final float n2 = (float)x3;
                    final int y2 = this.y;
                    canvas.drawRect(n2, (float)y2, (float)(x3 + this.measuredWidth), (float)(y2 + this.measuredHeight), TableLayout.this.delegate.getHeaderPaint());
                }
                else {
                    final int x4 = this.x;
                    final float n3 = (float)x4;
                    final int y3 = this.y;
                    canvas.drawRect(n3, (float)y3, (float)(x4 + this.measuredWidth), (float)(y3 + this.measuredHeight), TableLayout.this.delegate.getStripPaint());
                }
            }
            if (this.textLayout != null) {
                canvas.save();
                canvas.translate((float)this.getTextX(), (float)this.getTextY());
                this.textLayout.draw(canvas);
                canvas.restore();
            }
            if (TableLayout.this.drawLines) {
                final Paint linePaint = TableLayout.this.delegate.getLinePaint();
                final Paint linePaint2 = TableLayout.this.delegate.getLinePaint();
                final float n4 = linePaint.getStrokeWidth() / 2.0f;
                final float n5 = linePaint2.getStrokeWidth() / 2.0f;
                final int x5 = this.x;
                if (x5 == 0) {
                    final int y4 = this.y;
                    final float n6 = (float)y4;
                    final float n7 = (float)(this.measuredHeight + y4);
                    float n8 = n6;
                    if (y4 == 0) {
                        n8 = n6 + dp;
                    }
                    float n9 = n7;
                    if (n7 == TableLayout.this.getMeasuredHeight()) {
                        n9 = n7 - dp;
                    }
                    final int x6 = this.x;
                    canvas.drawLine(x6 + n4, n8, x6 + n4, n9, linePaint);
                }
                else {
                    final float n10 = (float)x5;
                    final int y5 = this.y;
                    canvas.drawLine(n10 - n5, (float)y5, x5 - n5, (float)(y5 + this.measuredHeight), linePaint2);
                }
                final int y6 = this.y;
                if (y6 == 0) {
                    final int x7 = this.x;
                    final float n11 = (float)x7;
                    final float n12 = (float)(this.measuredWidth + x7);
                    float n13 = n11;
                    if (x7 == 0) {
                        n13 = n11 + dp;
                    }
                    float n14 = n12;
                    if (n12 == TableLayout.this.getMeasuredWidth()) {
                        n14 = n12 - dp;
                    }
                    final int y7 = this.y;
                    canvas.drawLine(n13, y7 + n4, n14, y7 + n4, linePaint);
                }
                else {
                    final int x8 = this.x;
                    canvas.drawLine((float)x8, y6 - n5, (float)(x8 + this.measuredWidth), y6 - n5, linePaint2);
                }
                float n15 = 0.0f;
                Label_1112: {
                    if (b2) {
                        final int y8 = this.y;
                        if (y8 == 0) {
                            n15 = (float)(y8 + dp);
                            break Label_1112;
                        }
                    }
                    n15 = this.y - n4;
                }
                float n16;
                if (b2 && b3) {
                    n16 = (float)(this.y + this.measuredHeight - dp);
                }
                else {
                    n16 = this.y + this.measuredHeight - n4;
                }
                final int x9 = this.x;
                final int measuredWidth3 = this.measuredWidth;
                canvas.drawLine(x9 + measuredWidth3 - n4, n15, x9 + measuredWidth3 - n4, n16, linePaint);
                final int x10 = this.x;
                float n17;
                if (x10 == 0 && b3) {
                    n17 = (float)(x10 + dp);
                }
                else {
                    n17 = this.x - n4;
                }
                float n18;
                if (b2 && b3) {
                    n18 = (float)(this.x + this.measuredWidth - dp);
                }
                else {
                    n18 = this.x + this.measuredWidth - n4;
                }
                final int y9 = this.y;
                final int measuredHeight = this.measuredHeight;
                canvas.drawLine(n17, y9 + measuredHeight - n4, n18, y9 + measuredHeight - n4, linePaint);
                if (this.x == 0 && this.y == 0) {
                    final RectF access$601 = TableLayout.this.rect;
                    final int x11 = this.x;
                    final float n19 = (float)x11;
                    final int y10 = this.y;
                    final float n20 = (float)y10;
                    final float n21 = (float)x11;
                    final float n22 = (float)(dp * 2);
                    access$601.set(n19 + n4, n20 + n4, n21 + n4 + n22, y10 + n4 + n22);
                    canvas.drawArc(TableLayout.this.rect, -180.0f, 90.0f, false, linePaint);
                }
                if (b2 && this.y == 0) {
                    final RectF access$602 = TableLayout.this.rect;
                    final int x12 = this.x;
                    final int measuredWidth4 = this.measuredWidth;
                    final float n23 = (float)(x12 + measuredWidth4);
                    final float n24 = (float)(dp * 2);
                    final int y11 = this.y;
                    access$602.set(n23 - n4 - n24, y11 + n4, x12 + measuredWidth4 - n4, y11 + n4 + n24);
                    canvas.drawArc(TableLayout.this.rect, 0.0f, -90.0f, false, linePaint);
                }
                if (this.x == 0 && b3) {
                    final RectF access$603 = TableLayout.this.rect;
                    final int x13 = this.x;
                    final float n25 = (float)x13;
                    final int y12 = this.y;
                    final int measuredHeight2 = this.measuredHeight;
                    final float n26 = (float)(y12 + measuredHeight2);
                    final float n27 = (float)(dp * 2);
                    access$603.set(n25 + n4, n26 - n4 - n27, x13 + n4 + n27, y12 + measuredHeight2 - n4);
                    canvas.drawArc(TableLayout.this.rect, 180.0f, -90.0f, false, linePaint);
                }
                if (b2 && b3) {
                    final RectF access$604 = TableLayout.this.rect;
                    final int x14 = this.x;
                    final int measuredWidth5 = this.measuredWidth;
                    final float n28 = (float)(x14 + measuredWidth5);
                    final float n29 = (float)(dp * 2);
                    final int y13 = this.y;
                    final int measuredHeight3 = this.measuredHeight;
                    access$604.set(n28 - n4 - n29, y13 + measuredHeight3 - n4 - n29, x14 + measuredWidth5 - n4, y13 + measuredHeight3 - n4);
                    canvas.drawArc(TableLayout.this.rect, 0.0f, 90.0f, false, linePaint);
                }
            }
        }
        
        public LayoutParams getLayoutParams() {
            return this.layoutParams;
        }
        
        public int getMeasuredHeight() {
            return this.measuredHeight;
        }
        
        public int getMeasuredWidth() {
            return this.measuredWidth;
        }
        
        public int getTextX() {
            return this.x + this.textX;
        }
        
        public int getTextY() {
            return this.y + this.textY;
        }
        
        public void layout(final int x, final int y, final int n, final int n2) {
            this.x = x;
            this.y = y;
        }
        
        public void measure(int measuredWidth, final int measuredHeight, final boolean b) {
            this.measuredWidth = measuredWidth;
            this.measuredHeight = measuredHeight;
            if (b) {
                this.fixedHeight = this.measuredHeight;
            }
            final TLRPC.TL_pageTableCell cell = this.cell;
            if (cell != null) {
                if (cell.valign_middle) {
                    this.textY = (this.measuredHeight - this.textHeight) / 2;
                }
                else if (cell.valign_bottom) {
                    this.textY = this.measuredHeight - this.textHeight - TableLayout.this.itemPaddingTop;
                }
                else {
                    this.textY = TableLayout.this.itemPaddingTop;
                }
                final ArticleViewer.DrawingText textLayout = this.textLayout;
                if (textLayout != null) {
                    measuredWidth = textLayout.getLineCount();
                    Label_0206: {
                        if (!b) {
                            if (measuredWidth <= 1) {
                                if (measuredWidth <= 0) {
                                    break Label_0206;
                                }
                                final TLRPC.TL_pageTableCell cell2 = this.cell;
                                if (!cell2.align_center && !cell2.align_right) {
                                    break Label_0206;
                                }
                            }
                            this.setTextLayout(TableLayout.this.delegate.createTextLayout(this.cell, this.measuredWidth - TableLayout.this.itemPaddingLeft * 2));
                            this.fixedHeight = this.textHeight + TableLayout.this.itemPaddingTop * 2;
                        }
                    }
                    measuredWidth = this.textLeft;
                    if (measuredWidth != 0) {
                        this.textX = -measuredWidth;
                        final TLRPC.TL_pageTableCell cell3 = this.cell;
                        if (cell3.align_right) {
                            this.textX += this.measuredWidth - this.textWidth - TableLayout.this.itemPaddingLeft;
                        }
                        else if (cell3.align_center) {
                            this.textX += Math.round((float)((this.measuredWidth - this.textWidth) / 2));
                        }
                        else {
                            this.textX += TableLayout.this.itemPaddingLeft;
                        }
                    }
                    else {
                        this.textX = TableLayout.this.itemPaddingLeft;
                    }
                }
            }
        }
        
        public void setFixedHeight(final int n) {
            this.measuredHeight = this.fixedHeight;
            final TLRPC.TL_pageTableCell cell = this.cell;
            if (cell.valign_middle) {
                this.textY = (this.measuredHeight - this.textHeight) / 2;
            }
            else if (cell.valign_bottom) {
                this.textY = this.measuredHeight - this.textHeight - TableLayout.this.itemPaddingTop;
            }
        }
        
        public void setTextLayout(final ArticleViewer.DrawingText textLayout) {
            this.textLayout = textLayout;
            int i = 0;
            if (textLayout != null) {
                this.textWidth = 0;
                this.textLeft = 0;
                while (i < textLayout.getLineCount()) {
                    final float lineLeft = textLayout.getLineLeft(i);
                    int min;
                    if (i == 0) {
                        min = (int)Math.ceil(lineLeft);
                    }
                    else {
                        min = Math.min(this.textLeft, (int)Math.ceil(lineLeft));
                    }
                    this.textLeft = min;
                    this.textWidth = (int)Math.ceil(Math.max(textLayout.getLineWidth(i), (float)this.textWidth));
                    ++i;
                }
                this.textHeight = textLayout.getHeight();
            }
            else {
                this.textLeft = 0;
                this.textWidth = 0;
                this.textHeight = 0;
            }
        }
    }
    
    static final class Interval
    {
        public final int max;
        public final int min;
        
        public Interval(final int min, final int max) {
            this.min = min;
            this.max = max;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o != null && Interval.class == o.getClass()) {
                final Interval interval = (Interval)o;
                return this.max == interval.max && this.min == interval.min;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.min * 31 + this.max;
        }
        
        Interval inverse() {
            return new Interval(this.max, this.min);
        }
        
        int size() {
            return this.max - this.min;
        }
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        private static final int DEFAULT_HEIGHT = -2;
        private static final int DEFAULT_MARGIN = Integer.MIN_VALUE;
        private static final Interval DEFAULT_SPAN;
        private static final int DEFAULT_SPAN_SIZE;
        private static final int DEFAULT_WIDTH = -2;
        public Spec columnSpec;
        public Spec rowSpec;
        
        static {
            DEFAULT_SPAN = new Interval(Integer.MIN_VALUE, -2147483647);
            DEFAULT_SPAN_SIZE = LayoutParams.DEFAULT_SPAN.size();
        }
        
        public LayoutParams() {
            final Spec undefined = Spec.UNDEFINED;
            this(undefined, undefined);
        }
        
        private LayoutParams(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final Spec rowSpec, final Spec columnSpec) {
            super(n, n2);
            final Spec undefined = Spec.UNDEFINED;
            this.rowSpec = undefined;
            this.columnSpec = undefined;
            this.setMargins(n3, n4, n5, n6);
            this.rowSpec = rowSpec;
            this.columnSpec = columnSpec;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            final Spec undefined = Spec.UNDEFINED;
            this.rowSpec = undefined;
            this.columnSpec = undefined;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            final Spec undefined = Spec.UNDEFINED;
            this.rowSpec = undefined;
            this.columnSpec = undefined;
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            final Spec undefined = Spec.UNDEFINED;
            this.rowSpec = undefined;
            this.columnSpec = undefined;
            this.rowSpec = layoutParams.rowSpec;
            this.columnSpec = layoutParams.columnSpec;
        }
        
        public LayoutParams(final Spec spec, final Spec spec2) {
            this(-2, -2, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, spec, spec2);
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o != null && LayoutParams.class == o.getClass()) {
                final LayoutParams layoutParams = (LayoutParams)o;
                return this.columnSpec.equals(layoutParams.columnSpec) && this.rowSpec.equals(layoutParams.rowSpec);
            }
            return false;
        }
        
        public int hashCode() {
            return this.rowSpec.hashCode() * 31 + this.columnSpec.hashCode();
        }
        
        final void setColumnSpecSpan(final Interval interval) {
            this.columnSpec = this.columnSpec.copyWriteSpan(interval);
        }
        
        public void setGravity(final int n) {
            this.rowSpec = this.rowSpec.copyWriteAlignment(TableLayout.getAlignment(n, false));
            this.columnSpec = this.columnSpec.copyWriteAlignment(TableLayout.getAlignment(n, true));
        }
        
        final void setRowSpecSpan(final Interval interval) {
            this.rowSpec = this.rowSpec.copyWriteSpan(interval);
        }
    }
    
    static final class MutableInt
    {
        public int value;
        
        public MutableInt() {
            this.reset();
        }
        
        public MutableInt(final int value) {
            this.value = value;
        }
        
        public void reset() {
            this.value = Integer.MIN_VALUE;
        }
    }
    
    static final class PackedMap<K, V>
    {
        public final int[] index;
        public final K[] keys;
        public final V[] values;
        
        private PackedMap(final K[] array, final V[] array2) {
            this.index = createIndex(array);
            this.keys = compact(array, this.index);
            this.values = compact(array2, this.index);
        }
        
        private static <K> K[] compact(final K[] array, final int[] array2) {
            final int length = array.length;
            final Object[] array3 = (Object[])Array.newInstance(array.getClass().getComponentType(), TableLayout.max2(array2, -1) + 1);
            for (int i = 0; i < length; ++i) {
                array3[array2[i]] = array[i];
            }
            return (K[])array3;
        }
        
        private static <K> int[] createIndex(final K[] array) {
            final int length = array.length;
            final int[] array2 = new int[length];
            final HashMap<K, Integer> hashMap = (HashMap<K, Integer>)new HashMap<Object, Integer>();
            for (int i = 0; i < length; ++i) {
                final K k = array[i];
                Integer value;
                if ((value = hashMap.get(k)) == null) {
                    value = hashMap.size();
                    hashMap.put(k, value);
                }
                array2[i] = value;
            }
            return array2;
        }
        
        public V getValue(final int n) {
            return this.values[this.index[n]];
        }
    }
    
    public static class Spec
    {
        static final float DEFAULT_WEIGHT = 0.0f;
        static final Spec UNDEFINED;
        final Alignment alignment;
        final Interval span;
        final boolean startDefined;
        float weight;
        
        static {
            UNDEFINED = TableLayout.spec(Integer.MIN_VALUE);
        }
        
        private Spec(final boolean b, final int n, final int n2, final Alignment alignment, final float n3) {
            this(b, new Interval(n, n2 + n), alignment, n3);
        }
        
        private Spec(final boolean startDefined, final Interval span, final Alignment alignment, final float weight) {
            this.startDefined = startDefined;
            this.span = span;
            this.alignment = alignment;
            this.weight = weight;
        }
        
        private Alignment getAbsoluteAlignment(final boolean b) {
            final Alignment alignment = this.alignment;
            if (alignment != TableLayout.UNDEFINED_ALIGNMENT) {
                return alignment;
            }
            if (this.weight == 0.0f) {
                Alignment alignment2;
                if (b) {
                    alignment2 = TableLayout.START;
                }
                else {
                    alignment2 = TableLayout.BASELINE;
                }
                return alignment2;
            }
            return TableLayout.FILL;
        }
        
        final Spec copyWriteAlignment(final Alignment alignment) {
            return new Spec(this.startDefined, this.span, alignment, this.weight);
        }
        
        final Spec copyWriteSpan(final Interval interval) {
            return new Spec(this.startDefined, interval, this.alignment, this.weight);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o != null && Spec.class == o.getClass()) {
                final Spec spec = (Spec)o;
                return this.alignment.equals(spec.alignment) && this.span.equals(spec.span);
            }
            return false;
        }
        
        final int getFlexibility() {
            int n;
            if (this.alignment == TableLayout.UNDEFINED_ALIGNMENT && this.weight == 0.0f) {
                n = 0;
            }
            else {
                n = 2;
            }
            return n;
        }
        
        @Override
        public int hashCode() {
            return this.span.hashCode() * 31 + this.alignment.hashCode();
        }
    }
    
    public interface TableLayoutDelegate
    {
        ArticleViewer.DrawingText createTextLayout(final TLRPC.TL_pageTableCell p0, final int p1);
        
        Paint getHalfLinePaint();
        
        Paint getHeaderPaint();
        
        Paint getLinePaint();
        
        Paint getStripPaint();
    }
}

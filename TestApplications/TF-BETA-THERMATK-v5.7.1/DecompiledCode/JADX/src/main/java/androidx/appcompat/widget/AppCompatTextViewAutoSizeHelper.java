package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.appcompat.R$styleable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

class AppCompatTextViewAutoSizeHelper {
    private static final RectF TEMP_RECTF = new RectF();
    private static ConcurrentHashMap<String, Method> sTextViewMethodByNameCache = new ConcurrentHashMap();
    private float mAutoSizeMaxTextSizeInPx = -1.0f;
    private float mAutoSizeMinTextSizeInPx = -1.0f;
    private float mAutoSizeStepGranularityInPx = -1.0f;
    private int[] mAutoSizeTextSizesInPx = new int[0];
    private int mAutoSizeTextType = 0;
    private final Context mContext;
    private boolean mHasPresetAutoSizeValues = false;
    private boolean mNeedsAutoSizeText = false;
    private TextPaint mTempTextPaint;
    private final TextView mTextView;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:12:0x0031 in {6, 7, 9, 11} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private int findLargestTextSizeWhichFits(android.graphics.RectF r6) {
        /*
        r5 = this;
        r0 = r5.mAutoSizeTextSizesInPx;
        r0 = r0.length;
        if (r0 == 0) goto L_0x0029;
        r1 = 0;
        r2 = 1;
        r0 = r0 - r2;
        r1 = 1;
        r2 = 0;
        if (r1 > r0) goto L_0x0024;
        r2 = r1 + r0;
        r2 = r2 / 2;
        r3 = r5.mAutoSizeTextSizesInPx;
        r3 = r3[r2];
        r3 = r5.suggestedSizeFitsInSpace(r3, r6);
        if (r3 == 0) goto L_0x0020;
        r2 = r2 + 1;
        r4 = r2;
        r2 = r1;
        r1 = r4;
        goto L_0x000a;
        r2 = r2 + -1;
        r0 = r2;
        goto L_0x000a;
        r6 = r5.mAutoSizeTextSizesInPx;
        r6 = r6[r2];
        return r6;
        r6 = new java.lang.IllegalStateException;
        r0 = "No available text sizes to choose from.";
        r6.<init>(r0);
        throw r6;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatTextViewAutoSizeHelper.findLargestTextSizeWhichFits(android.graphics.RectF):int");
    }

    AppCompatTextViewAutoSizeHelper(TextView textView) {
        this.mTextView = textView;
        this.mContext = this.mTextView.getContext();
    }

    /* Access modifiers changed, original: 0000 */
    public void loadFromAttributes(AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(attributeSet, R$styleable.AppCompatTextView, i, 0);
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextView_autoSizeTextType)) {
            this.mAutoSizeTextType = obtainStyledAttributes.getInt(R$styleable.AppCompatTextView_autoSizeTextType, 0);
        }
        float dimension = obtainStyledAttributes.hasValue(R$styleable.AppCompatTextView_autoSizeStepGranularity) ? obtainStyledAttributes.getDimension(R$styleable.AppCompatTextView_autoSizeStepGranularity, -1.0f) : -1.0f;
        float dimension2 = obtainStyledAttributes.hasValue(R$styleable.AppCompatTextView_autoSizeMinTextSize) ? obtainStyledAttributes.getDimension(R$styleable.AppCompatTextView_autoSizeMinTextSize, -1.0f) : -1.0f;
        float dimension3 = obtainStyledAttributes.hasValue(R$styleable.AppCompatTextView_autoSizeMaxTextSize) ? obtainStyledAttributes.getDimension(R$styleable.AppCompatTextView_autoSizeMaxTextSize, -1.0f) : -1.0f;
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextView_autoSizePresetSizes)) {
            int resourceId = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_autoSizePresetSizes, 0);
            if (resourceId > 0) {
                TypedArray obtainTypedArray = obtainStyledAttributes.getResources().obtainTypedArray(resourceId);
                setupAutoSizeUniformPresetSizes(obtainTypedArray);
                obtainTypedArray.recycle();
            }
        }
        obtainStyledAttributes.recycle();
        if (!supportsAutoSizeText()) {
            this.mAutoSizeTextType = 0;
        } else if (this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues) {
                DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                if (dimension2 == -1.0f) {
                    dimension2 = TypedValue.applyDimension(2, 12.0f, displayMetrics);
                }
                if (dimension3 == -1.0f) {
                    dimension3 = TypedValue.applyDimension(2, 112.0f, displayMetrics);
                }
                if (dimension == -1.0f) {
                    dimension = 1.0f;
                }
                validateAndSetAutoSizeTextTypeUniformConfiguration(dimension2, dimension3, dimension);
            }
            setupAutoSizeText();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setAutoSizeTextTypeWithDefaults(int i) {
        if (!supportsAutoSizeText()) {
            return;
        }
        if (i == 0) {
            clearAutoSizeConfiguration();
        } else if (i == 1) {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(2, 12.0f, displayMetrics), TypedValue.applyDimension(2, 112.0f, displayMetrics), 1.0f);
            if (setupAutoSizeText()) {
                autoSizeText();
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown auto-size text type: ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setAutoSizeTextTypeUniformWithConfiguration(int i, int i2, int i3, int i4) throws IllegalArgumentException {
        if (supportsAutoSizeText()) {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(i4, (float) i, displayMetrics), TypedValue.applyDimension(i4, (float) i2, displayMetrics), TypedValue.applyDimension(i4, (float) i3, displayMetrics));
            if (setupAutoSizeText()) {
                autoSizeText();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setAutoSizeTextTypeUniformWithPresetSizes(int[] iArr, int i) throws IllegalArgumentException {
        if (supportsAutoSizeText()) {
            int length = iArr.length;
            int i2 = 0;
            if (length > 0) {
                int[] iArr2 = new int[length];
                if (i == 0) {
                    iArr2 = Arrays.copyOf(iArr, length);
                } else {
                    DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    while (i2 < length) {
                        iArr2[i2] = Math.round(TypedValue.applyDimension(i, (float) iArr[i2], displayMetrics));
                        i2++;
                    }
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(iArr2);
                if (!setupAutoSizeUniformPresetSizesConfiguration()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("None of the preset sizes is valid: ");
                    stringBuilder.append(Arrays.toString(iArr));
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            this.mHasPresetAutoSizeValues = false;
            if (setupAutoSizeText()) {
                autoSizeText();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getAutoSizeTextType() {
        return this.mAutoSizeTextType;
    }

    /* Access modifiers changed, original: 0000 */
    public int getAutoSizeStepGranularity() {
        return Math.round(this.mAutoSizeStepGranularityInPx);
    }

    /* Access modifiers changed, original: 0000 */
    public int getAutoSizeMinTextSize() {
        return Math.round(this.mAutoSizeMinTextSizeInPx);
    }

    /* Access modifiers changed, original: 0000 */
    public int getAutoSizeMaxTextSize() {
        return Math.round(this.mAutoSizeMaxTextSizeInPx);
    }

    /* Access modifiers changed, original: 0000 */
    public int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextSizesInPx;
    }

    private void setupAutoSizeUniformPresetSizes(TypedArray typedArray) {
        int length = typedArray.length();
        int[] iArr = new int[length];
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                iArr[i] = typedArray.getDimensionPixelSize(i, -1);
            }
            this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(iArr);
            setupAutoSizeUniformPresetSizesConfiguration();
        }
    }

    private boolean setupAutoSizeUniformPresetSizesConfiguration() {
        int length = this.mAutoSizeTextSizesInPx.length;
        this.mHasPresetAutoSizeValues = length > 0;
        if (this.mHasPresetAutoSizeValues) {
            this.mAutoSizeTextType = 1;
            int[] iArr = this.mAutoSizeTextSizesInPx;
            this.mAutoSizeMinTextSizeInPx = (float) iArr[0];
            this.mAutoSizeMaxTextSizeInPx = (float) iArr[length - 1];
            this.mAutoSizeStepGranularityInPx = -1.0f;
        }
        return this.mHasPresetAutoSizeValues;
    }

    private int[] cleanupAutoSizePresetSizes(int[] iArr) {
        if (r0 == 0) {
            return iArr;
        }
        Arrays.sort(iArr);
        ArrayList arrayList = new ArrayList();
        for (int i : iArr) {
            if (i > 0 && Collections.binarySearch(arrayList, Integer.valueOf(i)) < 0) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        if (r0 == arrayList.size()) {
            return iArr;
        }
        int size = arrayList.size();
        int[] iArr2 = new int[size];
        for (int i2 = 0; i2 < size; i2++) {
            iArr2[i2] = ((Integer) arrayList.get(i2)).intValue();
        }
        return iArr2;
    }

    private void validateAndSetAutoSizeTextTypeUniformConfiguration(float f, float f2, float f3) throws IllegalArgumentException {
        String str = "px) is less or equal to (0px)";
        if (f <= 0.0f) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Minimum auto-size text size (");
            stringBuilder.append(f);
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (f2 <= f) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Maximum auto-size text size (");
            stringBuilder2.append(f2);
            stringBuilder2.append("px) is less or equal to minimum auto-size ");
            stringBuilder2.append("text size (");
            stringBuilder2.append(f);
            stringBuilder2.append("px)");
            throw new IllegalArgumentException(stringBuilder2.toString());
        } else if (f3 > 0.0f) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = f;
            this.mAutoSizeMaxTextSizeInPx = f2;
            this.mAutoSizeStepGranularityInPx = f3;
            this.mHasPresetAutoSizeValues = false;
        } else {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("The auto-size step granularity (");
            stringBuilder3.append(f3);
            stringBuilder3.append(str);
            throw new IllegalArgumentException(stringBuilder3.toString());
        }
    }

    private boolean setupAutoSizeText() {
        int i = 0;
        if (supportsAutoSizeText() && this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
                float round = (float) Math.round(this.mAutoSizeMinTextSizeInPx);
                int i2 = 1;
                while (Math.round(this.mAutoSizeStepGranularityInPx + round) <= Math.round(this.mAutoSizeMaxTextSizeInPx)) {
                    i2++;
                    round += this.mAutoSizeStepGranularityInPx;
                }
                int[] iArr = new int[i2];
                float f = this.mAutoSizeMinTextSizeInPx;
                while (i < i2) {
                    iArr[i] = Math.round(f);
                    f += this.mAutoSizeStepGranularityInPx;
                    i++;
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(iArr);
            }
            this.mNeedsAutoSizeText = true;
        } else {
            this.mNeedsAutoSizeText = false;
        }
        return this.mNeedsAutoSizeText;
    }

    /* Access modifiers changed, original: 0000 */
    public void autoSizeText() {
        if (isAutoSizeEnabled()) {
            if (this.mNeedsAutoSizeText) {
                if (this.mTextView.getMeasuredHeight() > 0 && this.mTextView.getMeasuredWidth() > 0) {
                    int i;
                    if (((Boolean) invokeAndReturnWithDefault(this.mTextView, "getHorizontallyScrolling", Boolean.valueOf(false))).booleanValue()) {
                        i = 1048576;
                    } else {
                        i = (this.mTextView.getMeasuredWidth() - this.mTextView.getTotalPaddingLeft()) - this.mTextView.getTotalPaddingRight();
                    }
                    int height = (this.mTextView.getHeight() - this.mTextView.getCompoundPaddingBottom()) - this.mTextView.getCompoundPaddingTop();
                    if (i > 0 && height > 0) {
                        synchronized (TEMP_RECTF) {
                            TEMP_RECTF.setEmpty();
                            TEMP_RECTF.right = (float) i;
                            TEMP_RECTF.bottom = (float) height;
                            float findLargestTextSizeWhichFits = (float) findLargestTextSizeWhichFits(TEMP_RECTF);
                            if (findLargestTextSizeWhichFits != this.mTextView.getTextSize()) {
                                setTextSizeInternal(0, findLargestTextSizeWhichFits);
                            }
                        }
                    }
                }
                return;
            }
            this.mNeedsAutoSizeText = true;
        }
    }

    private void clearAutoSizeConfiguration() {
        this.mAutoSizeTextType = 0;
        this.mAutoSizeMinTextSizeInPx = -1.0f;
        this.mAutoSizeMaxTextSizeInPx = -1.0f;
        this.mAutoSizeStepGranularityInPx = -1.0f;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mNeedsAutoSizeText = false;
    }

    /* Access modifiers changed, original: 0000 */
    public void setTextSizeInternal(int i, float f) {
        Resources system;
        Context context = this.mContext;
        if (context == null) {
            system = Resources.getSystem();
        } else {
            system = context.getResources();
        }
        setRawTextSize(TypedValue.applyDimension(i, f, system.getDisplayMetrics()));
    }

    private void setRawTextSize(float f) {
        if (f != this.mTextView.getPaint().getTextSize()) {
            this.mTextView.getPaint().setTextSize(f);
            boolean isInLayout = VERSION.SDK_INT >= 18 ? this.mTextView.isInLayout() : false;
            if (this.mTextView.getLayout() != null) {
                this.mNeedsAutoSizeText = false;
                try {
                    Method textViewMethod = getTextViewMethod("nullLayouts");
                    if (textViewMethod != null) {
                        textViewMethod.invoke(this.mTextView, new Object[0]);
                    }
                } catch (Exception e) {
                    Log.w("ACTVAutoSizeHelper", "Failed to invoke TextView#nullLayouts() method", e);
                }
                if (isInLayout) {
                    this.mTextView.forceLayout();
                } else {
                    this.mTextView.requestLayout();
                }
                this.mTextView.invalidate();
            }
        }
    }

    /* JADX WARNING: Missing block: B:23:0x0086, code skipped:
            return false;
     */
    private boolean suggestedSizeFitsInSpace(int r6, android.graphics.RectF r7) {
        /*
        r5 = this;
        r0 = r5.mTextView;
        r0 = r0.getText();
        r1 = r5.mTextView;
        r1 = r1.getTransformationMethod();
        if (r1 == 0) goto L_0x0017;
    L_0x000e:
        r2 = r5.mTextView;
        r1 = r1.getTransformation(r0, r2);
        if (r1 == 0) goto L_0x0017;
    L_0x0016:
        r0 = r1;
    L_0x0017:
        r1 = android.os.Build.VERSION.SDK_INT;
        r2 = 16;
        r3 = -1;
        if (r1 < r2) goto L_0x0025;
    L_0x001e:
        r1 = r5.mTextView;
        r1 = r1.getMaxLines();
        goto L_0x0026;
    L_0x0025:
        r1 = -1;
    L_0x0026:
        r2 = r5.mTempTextPaint;
        if (r2 != 0) goto L_0x0032;
    L_0x002a:
        r2 = new android.text.TextPaint;
        r2.<init>();
        r5.mTempTextPaint = r2;
        goto L_0x0035;
    L_0x0032:
        r2.reset();
    L_0x0035:
        r2 = r5.mTempTextPaint;
        r4 = r5.mTextView;
        r4 = r4.getPaint();
        r2.set(r4);
        r2 = r5.mTempTextPaint;
        r6 = (float) r6;
        r2.setTextSize(r6);
        r6 = r5.mTextView;
        r2 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r4 = "getLayoutAlignment";
        r6 = r5.invokeAndReturnWithDefault(r6, r4, r2);
        r6 = (android.text.Layout.Alignment) r6;
        r2 = android.os.Build.VERSION.SDK_INT;
        r4 = 23;
        if (r2 < r4) goto L_0x0063;
    L_0x0058:
        r2 = r7.right;
        r2 = java.lang.Math.round(r2);
        r6 = r5.createStaticLayoutForMeasuring(r0, r6, r2, r1);
        goto L_0x006d;
    L_0x0063:
        r2 = r7.right;
        r2 = java.lang.Math.round(r2);
        r6 = r5.createStaticLayoutForMeasuringPre23(r0, r6, r2);
    L_0x006d:
        r2 = 0;
        r4 = 1;
        if (r1 == r3) goto L_0x0087;
    L_0x0071:
        r3 = r6.getLineCount();
        if (r3 > r1) goto L_0x0086;
    L_0x0077:
        r1 = r6.getLineCount();
        r1 = r1 - r4;
        r1 = r6.getLineEnd(r1);
        r0 = r0.length();
        if (r1 == r0) goto L_0x0087;
    L_0x0086:
        return r2;
    L_0x0087:
        r6 = r6.getHeight();
        r6 = (float) r6;
        r7 = r7.bottom;
        r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r6 <= 0) goto L_0x0093;
    L_0x0092:
        return r2;
    L_0x0093:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatTextViewAutoSizeHelper.suggestedSizeFitsInSpace(int, android.graphics.RectF):boolean");
    }

    private StaticLayout createStaticLayoutForMeasuring(CharSequence charSequence, Alignment alignment, int i, int i2) {
        TextDirectionHeuristic textDirectionHeuristic = (TextDirectionHeuristic) invokeAndReturnWithDefault(this.mTextView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR);
        Builder hyphenationFrequency = Builder.obtain(charSequence, 0, charSequence.length(), this.mTempTextPaint, i).setAlignment(alignment).setLineSpacing(this.mTextView.getLineSpacingExtra(), this.mTextView.getLineSpacingMultiplier()).setIncludePad(this.mTextView.getIncludeFontPadding()).setBreakStrategy(this.mTextView.getBreakStrategy()).setHyphenationFrequency(this.mTextView.getHyphenationFrequency());
        if (i2 == -1) {
            i2 = Integer.MAX_VALUE;
        }
        return hyphenationFrequency.setMaxLines(i2).setTextDirection(textDirectionHeuristic).build();
    }

    private StaticLayout createStaticLayoutForMeasuringPre23(CharSequence charSequence, Alignment alignment, int i) {
        float lineSpacingMultiplier;
        float lineSpacingExtra;
        boolean includeFontPadding;
        if (VERSION.SDK_INT >= 16) {
            lineSpacingMultiplier = this.mTextView.getLineSpacingMultiplier();
            lineSpacingExtra = this.mTextView.getLineSpacingExtra();
            includeFontPadding = this.mTextView.getIncludeFontPadding();
        } else {
            lineSpacingMultiplier = ((Float) invokeAndReturnWithDefault(this.mTextView, "getLineSpacingMultiplier", Float.valueOf(1.0f))).floatValue();
            lineSpacingExtra = ((Float) invokeAndReturnWithDefault(this.mTextView, "getLineSpacingExtra", Float.valueOf(0.0f))).floatValue();
            includeFontPadding = ((Boolean) invokeAndReturnWithDefault(this.mTextView, "getIncludeFontPadding", Boolean.valueOf(true))).booleanValue();
        }
        return new StaticLayout(charSequence, this.mTempTextPaint, i, alignment, lineSpacingMultiplier, lineSpacingExtra, includeFontPadding);
    }

    private <T> T invokeAndReturnWithDefault(Object obj, String str, T t) {
        try {
            t = getTextViewMethod(str).invoke(obj, new Object[0]);
            return t;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to invoke TextView#");
            stringBuilder.append(str);
            stringBuilder.append("() method");
            Log.w("ACTVAutoSizeHelper", stringBuilder.toString(), e);
            return t;
        }
    }

    private Method getTextViewMethod(String str) {
        try {
            Method method = (Method) sTextViewMethodByNameCache.get(str);
            if (method == null) {
                method = TextView.class.getDeclaredMethod(str, new Class[0]);
                if (method != null) {
                    method.setAccessible(true);
                    sTextViewMethodByNameCache.put(str, method);
                }
            }
            return method;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to retrieve TextView#");
            stringBuilder.append(str);
            stringBuilder.append("() method");
            Log.w("ACTVAutoSizeHelper", stringBuilder.toString(), e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isAutoSizeEnabled() {
        return supportsAutoSizeText() && this.mAutoSizeTextType != 0;
    }

    private boolean supportsAutoSizeText() {
        return !(this.mTextView instanceof AppCompatEditText);
    }
}

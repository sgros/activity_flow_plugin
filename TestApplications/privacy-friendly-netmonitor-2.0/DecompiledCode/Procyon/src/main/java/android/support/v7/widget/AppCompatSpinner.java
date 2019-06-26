// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.view.ViewTreeObserver;
import android.widget.PopupWindow$OnDismissListener;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.support.v4.view.ViewCompat;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.database.DataSetObserver;
import android.widget.ThemedSpinnerAdapter;
import android.support.v7.content.res.AppCompatResources;
import android.support.annotation.DrawableRes;
import android.widget.ListAdapter;
import android.widget.Adapter;
import android.view.MotionEvent;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.RestrictTo;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.os.Build$VERSION;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.view.ViewGroup;
import android.view.View$MeasureSpec;
import android.graphics.drawable.Drawable;
import android.content.res.Resources$Theme;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.graphics.Rect;
import android.widget.SpinnerAdapter;
import android.content.Context;
import android.support.v4.view.TintableBackgroundView;
import android.widget.Spinner;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView
{
    private static final int[] ATTRS_ANDROID_SPINNERMODE;
    private static final int MAX_ITEMS_MEASURED = 15;
    private static final int MODE_DIALOG = 0;
    private static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "AppCompatSpinner";
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    private int mDropDownWidth;
    private ForwardingListener mForwardingListener;
    private DropdownPopup mPopup;
    private final Context mPopupContext;
    private final boolean mPopupSet;
    private SpinnerAdapter mTempAdapter;
    private final Rect mTempRect;
    
    static {
        ATTRS_ANDROID_SPINNERMODE = new int[] { 16843505 };
    }
    
    public AppCompatSpinner(final Context context) {
        this(context, null);
    }
    
    public AppCompatSpinner(final Context context, final int n) {
        this(context, null, R.attr.spinnerStyle, n);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set) {
        this(context, set, R.attr.spinnerStyle);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set, final int n) {
        this(context, set, n, -1);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set, final int n, final int n2) {
        this(context, set, n, n2, null);
    }
    
    public AppCompatSpinner(final Context p0, final AttributeSet p1, final int p2, final int p3, final Resources$Theme p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: aload_2        
        //     3: iload_3        
        //     4: invokespecial   android/widget/Spinner.<init>:(Landroid/content/Context;Landroid/util/AttributeSet;I)V
        //     7: aload_0        
        //     8: new             Landroid/graphics/Rect;
        //    11: dup            
        //    12: invokespecial   android/graphics/Rect.<init>:()V
        //    15: putfield        android/support/v7/widget/AppCompatSpinner.mTempRect:Landroid/graphics/Rect;
        //    18: aload_1        
        //    19: aload_2        
        //    20: getstatic       android/support/v7/appcompat/R$styleable.Spinner:[I
        //    23: iload_3        
        //    24: iconst_0       
        //    25: invokestatic    android/support/v7/widget/TintTypedArray.obtainStyledAttributes:(Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroid/support/v7/widget/TintTypedArray;
        //    28: astore          6
        //    30: aload_0        
        //    31: new             Landroid/support/v7/widget/AppCompatBackgroundHelper;
        //    34: dup            
        //    35: aload_0        
        //    36: invokespecial   android/support/v7/widget/AppCompatBackgroundHelper.<init>:(Landroid/view/View;)V
        //    39: putfield        android/support/v7/widget/AppCompatSpinner.mBackgroundTintHelper:Landroid/support/v7/widget/AppCompatBackgroundHelper;
        //    42: aload           5
        //    44: ifnull          64
        //    47: aload_0        
        //    48: new             Landroid/support/v7/view/ContextThemeWrapper;
        //    51: dup            
        //    52: aload_1        
        //    53: aload           5
        //    55: invokespecial   android/support/v7/view/ContextThemeWrapper.<init>:(Landroid/content/Context;Landroid/content/res/Resources$Theme;)V
        //    58: putfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //    61: goto            120
        //    64: aload           6
        //    66: getstatic       android/support/v7/appcompat/R$styleable.Spinner_popupTheme:I
        //    69: iconst_0       
        //    70: invokevirtual   android/support/v7/widget/TintTypedArray.getResourceId:(II)I
        //    73: istore          7
        //    75: iload           7
        //    77: ifeq            97
        //    80: aload_0        
        //    81: new             Landroid/support/v7/view/ContextThemeWrapper;
        //    84: dup            
        //    85: aload_1        
        //    86: iload           7
        //    88: invokespecial   android/support/v7/view/ContextThemeWrapper.<init>:(Landroid/content/Context;I)V
        //    91: putfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //    94: goto            120
        //    97: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   100: bipush          23
        //   102: if_icmpge       111
        //   105: aload_1        
        //   106: astore          5
        //   108: goto            114
        //   111: aconst_null    
        //   112: astore          5
        //   114: aload_0        
        //   115: aload           5
        //   117: putfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //   120: aload_0        
        //   121: getfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //   124: ifnull          374
        //   127: iload           4
        //   129: istore          7
        //   131: iload           4
        //   133: iconst_m1      
        //   134: if_icmpne       271
        //   137: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   140: bipush          11
        //   142: if_icmplt       268
        //   145: aload_1        
        //   146: aload_2        
        //   147: getstatic       android/support/v7/widget/AppCompatSpinner.ATTRS_ANDROID_SPINNERMODE:[I
        //   150: iload_3        
        //   151: iconst_0       
        //   152: invokevirtual   android/content/Context.obtainStyledAttributes:(Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;
        //   155: astore          5
        //   157: iload           4
        //   159: istore          8
        //   161: aload           5
        //   163: astore          9
        //   165: aload           5
        //   167: iconst_0       
        //   168: invokevirtual   android/content/res/TypedArray.hasValue:(I)Z
        //   171: ifeq            187
        //   174: aload           5
        //   176: astore          9
        //   178: aload           5
        //   180: iconst_0       
        //   181: iconst_0       
        //   182: invokevirtual   android/content/res/TypedArray.getInt:(II)I
        //   185: istore          8
        //   187: iload           8
        //   189: istore          7
        //   191: aload           5
        //   193: ifnull          271
        //   196: iload           8
        //   198: istore          4
        //   200: aload           5
        //   202: invokevirtual   android/content/res/TypedArray.recycle:()V
        //   205: iload           4
        //   207: istore          7
        //   209: goto            271
        //   212: astore          10
        //   214: goto            229
        //   217: astore_1       
        //   218: aconst_null    
        //   219: astore          9
        //   221: goto            256
        //   224: astore          10
        //   226: aconst_null    
        //   227: astore          5
        //   229: aload           5
        //   231: astore          9
        //   233: ldc             "AppCompatSpinner"
        //   235: ldc             "Could not read android:spinnerMode"
        //   237: aload           10
        //   239: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   242: pop            
        //   243: iload           4
        //   245: istore          7
        //   247: aload           5
        //   249: ifnull          271
        //   252: goto            200
        //   255: astore_1       
        //   256: aload           9
        //   258: ifnull          266
        //   261: aload           9
        //   263: invokevirtual   android/content/res/TypedArray.recycle:()V
        //   266: aload_1        
        //   267: athrow         
        //   268: iconst_1       
        //   269: istore          7
        //   271: iload           7
        //   273: iconst_1       
        //   274: if_icmpne       374
        //   277: new             Landroid/support/v7/widget/AppCompatSpinner$DropdownPopup;
        //   280: dup            
        //   281: aload_0        
        //   282: aload_0        
        //   283: getfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //   286: aload_2        
        //   287: iload_3        
        //   288: invokespecial   android/support/v7/widget/AppCompatSpinner$DropdownPopup.<init>:(Landroid/support/v7/widget/AppCompatSpinner;Landroid/content/Context;Landroid/util/AttributeSet;I)V
        //   291: astore          5
        //   293: aload_0        
        //   294: getfield        android/support/v7/widget/AppCompatSpinner.mPopupContext:Landroid/content/Context;
        //   297: aload_2        
        //   298: getstatic       android/support/v7/appcompat/R$styleable.Spinner:[I
        //   301: iload_3        
        //   302: iconst_0       
        //   303: invokestatic    android/support/v7/widget/TintTypedArray.obtainStyledAttributes:(Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroid/support/v7/widget/TintTypedArray;
        //   306: astore          9
        //   308: aload_0        
        //   309: aload           9
        //   311: getstatic       android/support/v7/appcompat/R$styleable.Spinner_android_dropDownWidth:I
        //   314: bipush          -2
        //   316: invokevirtual   android/support/v7/widget/TintTypedArray.getLayoutDimension:(II)I
        //   319: putfield        android/support/v7/widget/AppCompatSpinner.mDropDownWidth:I
        //   322: aload           5
        //   324: aload           9
        //   326: getstatic       android/support/v7/appcompat/R$styleable.Spinner_android_popupBackground:I
        //   329: invokevirtual   android/support/v7/widget/TintTypedArray.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   332: invokevirtual   android/support/v7/widget/AppCompatSpinner$DropdownPopup.setBackgroundDrawable:(Landroid/graphics/drawable/Drawable;)V
        //   335: aload           5
        //   337: aload           6
        //   339: getstatic       android/support/v7/appcompat/R$styleable.Spinner_android_prompt:I
        //   342: invokevirtual   android/support/v7/widget/TintTypedArray.getString:(I)Ljava/lang/String;
        //   345: invokevirtual   android/support/v7/widget/AppCompatSpinner$DropdownPopup.setPromptText:(Ljava/lang/CharSequence;)V
        //   348: aload           9
        //   350: invokevirtual   android/support/v7/widget/TintTypedArray.recycle:()V
        //   353: aload_0        
        //   354: aload           5
        //   356: putfield        android/support/v7/widget/AppCompatSpinner.mPopup:Landroid/support/v7/widget/AppCompatSpinner$DropdownPopup;
        //   359: aload_0        
        //   360: new             Landroid/support/v7/widget/AppCompatSpinner$1;
        //   363: dup            
        //   364: aload_0        
        //   365: aload_0        
        //   366: aload           5
        //   368: invokespecial   android/support/v7/widget/AppCompatSpinner$1.<init>:(Landroid/support/v7/widget/AppCompatSpinner;Landroid/view/View;Landroid/support/v7/widget/AppCompatSpinner$DropdownPopup;)V
        //   371: putfield        android/support/v7/widget/AppCompatSpinner.mForwardingListener:Landroid/support/v7/widget/ForwardingListener;
        //   374: aload           6
        //   376: getstatic       android/support/v7/appcompat/R$styleable.Spinner_android_entries:I
        //   379: invokevirtual   android/support/v7/widget/TintTypedArray.getTextArray:(I)[Ljava/lang/CharSequence;
        //   382: astore          5
        //   384: aload           5
        //   386: ifnull          414
        //   389: new             Landroid/widget/ArrayAdapter;
        //   392: dup            
        //   393: aload_1        
        //   394: ldc             17367048
        //   396: aload           5
        //   398: invokespecial   android/widget/ArrayAdapter.<init>:(Landroid/content/Context;I[Ljava/lang/Object;)V
        //   401: astore_1       
        //   402: aload_1        
        //   403: getstatic       android/support/v7/appcompat/R$layout.support_simple_spinner_dropdown_item:I
        //   406: invokevirtual   android/widget/ArrayAdapter.setDropDownViewResource:(I)V
        //   409: aload_0        
        //   410: aload_1        
        //   411: invokevirtual   android/support/v7/widget/AppCompatSpinner.setAdapter:(Landroid/widget/SpinnerAdapter;)V
        //   414: aload           6
        //   416: invokevirtual   android/support/v7/widget/TintTypedArray.recycle:()V
        //   419: aload_0        
        //   420: iconst_1       
        //   421: putfield        android/support/v7/widget/AppCompatSpinner.mPopupSet:Z
        //   424: aload_0        
        //   425: getfield        android/support/v7/widget/AppCompatSpinner.mTempAdapter:Landroid/widget/SpinnerAdapter;
        //   428: ifnull          444
        //   431: aload_0        
        //   432: aload_0        
        //   433: getfield        android/support/v7/widget/AppCompatSpinner.mTempAdapter:Landroid/widget/SpinnerAdapter;
        //   436: invokevirtual   android/support/v7/widget/AppCompatSpinner.setAdapter:(Landroid/widget/SpinnerAdapter;)V
        //   439: aload_0        
        //   440: aconst_null    
        //   441: putfield        android/support/v7/widget/AppCompatSpinner.mTempAdapter:Landroid/widget/SpinnerAdapter;
        //   444: aload_0        
        //   445: getfield        android/support/v7/widget/AppCompatSpinner.mBackgroundTintHelper:Landroid/support/v7/widget/AppCompatBackgroundHelper;
        //   448: aload_2        
        //   449: iload_3        
        //   450: invokevirtual   android/support/v7/widget/AppCompatBackgroundHelper.loadFromAttributes:(Landroid/util/AttributeSet;I)V
        //   453: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  145    157    224    229    Ljava/lang/Exception;
        //  145    157    217    224    Any
        //  165    174    212    217    Ljava/lang/Exception;
        //  165    174    255    256    Any
        //  178    187    212    217    Ljava/lang/Exception;
        //  178    187    255    256    Any
        //  233    243    255    256    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0187:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    int compatMeasureContentWidth(final SpinnerAdapter spinnerAdapter, final Drawable drawable) {
        int n = 0;
        if (spinnerAdapter == null) {
            return 0;
        }
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 0);
        final int measureSpec2 = View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 0);
        final int max = Math.max(0, this.getSelectedItemPosition());
        final int min = Math.min(spinnerAdapter.getCount(), max + 15);
        int i = Math.max(0, max - (15 - (min - max)));
        int max2 = 0;
        View view = null;
        while (i < min) {
            final int itemViewType = spinnerAdapter.getItemViewType(i);
            int n2;
            if (itemViewType != (n2 = n)) {
                view = null;
                n2 = itemViewType;
            }
            view = spinnerAdapter.getView(i, view, (ViewGroup)this);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup$LayoutParams(-2, -2));
            }
            view.measure(measureSpec, measureSpec2);
            max2 = Math.max(max2, view.getMeasuredWidth());
            ++i;
            n = n2;
        }
        int n3 = max2;
        if (drawable != null) {
            drawable.getPadding(this.mTempRect);
            n3 = max2 + (this.mTempRect.left + this.mTempRect.right);
        }
        return n3;
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.applySupportBackgroundTint();
        }
    }
    
    public int getDropDownHorizontalOffset() {
        if (this.mPopup != null) {
            return this.mPopup.getHorizontalOffset();
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getDropDownHorizontalOffset();
        }
        return 0;
    }
    
    public int getDropDownVerticalOffset() {
        if (this.mPopup != null) {
            return this.mPopup.getVerticalOffset();
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getDropDownVerticalOffset();
        }
        return 0;
    }
    
    public int getDropDownWidth() {
        if (this.mPopup != null) {
            return this.mDropDownWidth;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getDropDownWidth();
        }
        return 0;
    }
    
    public Drawable getPopupBackground() {
        if (this.mPopup != null) {
            return this.mPopup.getBackground();
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getPopupBackground();
        }
        return null;
    }
    
    public Context getPopupContext() {
        if (this.mPopup != null) {
            return this.mPopupContext;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            return super.getPopupContext();
        }
        return null;
    }
    
    public CharSequence getPrompt() {
        CharSequence charSequence;
        if (this.mPopup != null) {
            charSequence = this.mPopup.getHintText();
        }
        else {
            charSequence = super.getPrompt();
        }
        return charSequence;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public ColorStateList getSupportBackgroundTintList() {
        ColorStateList supportBackgroundTintList;
        if (this.mBackgroundTintHelper != null) {
            supportBackgroundTintList = this.mBackgroundTintHelper.getSupportBackgroundTintList();
        }
        else {
            supportBackgroundTintList = null;
        }
        return supportBackgroundTintList;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        PorterDuff$Mode supportBackgroundTintMode;
        if (this.mBackgroundTintHelper != null) {
            supportBackgroundTintMode = this.mBackgroundTintHelper.getSupportBackgroundTintMode();
        }
        else {
            supportBackgroundTintMode = null;
        }
        return supportBackgroundTintMode;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mPopup != null && this.mPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        if (this.mPopup != null && View$MeasureSpec.getMode(n) == Integer.MIN_VALUE) {
            this.setMeasuredDimension(Math.min(Math.max(this.getMeasuredWidth(), this.compatMeasureContentWidth(this.getAdapter(), this.getBackground())), View$MeasureSpec.getSize(n)), this.getMeasuredHeight());
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return (this.mForwardingListener != null && this.mForwardingListener.onTouch((View)this, motionEvent)) || super.onTouchEvent(motionEvent);
    }
    
    public boolean performClick() {
        if (this.mPopup != null) {
            if (!this.mPopup.isShowing()) {
                this.mPopup.show();
            }
            return true;
        }
        return super.performClick();
    }
    
    public void setAdapter(final SpinnerAdapter spinnerAdapter) {
        if (!this.mPopupSet) {
            this.mTempAdapter = spinnerAdapter;
            return;
        }
        super.setAdapter(spinnerAdapter);
        if (this.mPopup != null) {
            Context context;
            if (this.mPopupContext == null) {
                context = this.getContext();
            }
            else {
                context = this.mPopupContext;
            }
            this.mPopup.setAdapter((ListAdapter)new DropDownAdapter(spinnerAdapter, context.getTheme()));
        }
    }
    
    public void setBackgroundDrawable(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundDrawable(backgroundDrawable);
        }
    }
    
    public void setBackgroundResource(@DrawableRes final int backgroundResource) {
        super.setBackgroundResource(backgroundResource);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundResource(backgroundResource);
        }
    }
    
    public void setDropDownHorizontalOffset(final int n) {
        if (this.mPopup != null) {
            this.mPopup.setHorizontalOffset(n);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setDropDownHorizontalOffset(n);
        }
    }
    
    public void setDropDownVerticalOffset(final int n) {
        if (this.mPopup != null) {
            this.mPopup.setVerticalOffset(n);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setDropDownVerticalOffset(n);
        }
    }
    
    public void setDropDownWidth(final int n) {
        if (this.mPopup != null) {
            this.mDropDownWidth = n;
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setDropDownWidth(n);
        }
    }
    
    public void setPopupBackgroundDrawable(final Drawable drawable) {
        if (this.mPopup != null) {
            this.mPopup.setBackgroundDrawable(drawable);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setPopupBackgroundDrawable(drawable);
        }
    }
    
    public void setPopupBackgroundResource(@DrawableRes final int n) {
        this.setPopupBackgroundDrawable(AppCompatResources.getDrawable(this.getPopupContext(), n));
    }
    
    public void setPrompt(final CharSequence charSequence) {
        if (this.mPopup != null) {
            this.mPopup.setPromptText(charSequence);
        }
        else {
            super.setPrompt(charSequence);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportBackgroundTintList(@Nullable final ColorStateList supportBackgroundTintList) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintList(supportBackgroundTintList);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportBackgroundTintMode(@Nullable final PorterDuff$Mode supportBackgroundTintMode) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintMode(supportBackgroundTintMode);
        }
    }
    
    private static class DropDownAdapter implements ListAdapter, SpinnerAdapter
    {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;
        
        public DropDownAdapter(@Nullable final SpinnerAdapter mAdapter, @Nullable final Resources$Theme resources$Theme) {
            this.mAdapter = mAdapter;
            if (mAdapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter)mAdapter;
            }
            if (resources$Theme != null) {
                if (Build$VERSION.SDK_INT >= 23 && mAdapter instanceof ThemedSpinnerAdapter) {
                    final ThemedSpinnerAdapter themedSpinnerAdapter = (ThemedSpinnerAdapter)mAdapter;
                    if (themedSpinnerAdapter.getDropDownViewTheme() != resources$Theme) {
                        themedSpinnerAdapter.setDropDownViewTheme(resources$Theme);
                    }
                }
                else if (mAdapter instanceof android.support.v7.widget.ThemedSpinnerAdapter) {
                    final android.support.v7.widget.ThemedSpinnerAdapter themedSpinnerAdapter2 = (android.support.v7.widget.ThemedSpinnerAdapter)mAdapter;
                    if (themedSpinnerAdapter2.getDropDownViewTheme() == null) {
                        themedSpinnerAdapter2.setDropDownViewTheme(resources$Theme);
                    }
                }
            }
        }
        
        public boolean areAllItemsEnabled() {
            final ListAdapter mListAdapter = this.mListAdapter;
            return mListAdapter == null || mListAdapter.areAllItemsEnabled();
        }
        
        public int getCount() {
            int count;
            if (this.mAdapter == null) {
                count = 0;
            }
            else {
                count = this.mAdapter.getCount();
            }
            return count;
        }
        
        public View getDropDownView(final int n, View dropDownView, final ViewGroup viewGroup) {
            if (this.mAdapter == null) {
                dropDownView = null;
            }
            else {
                dropDownView = this.mAdapter.getDropDownView(n, dropDownView, viewGroup);
            }
            return dropDownView;
        }
        
        public Object getItem(final int n) {
            Object item;
            if (this.mAdapter == null) {
                item = null;
            }
            else {
                item = this.mAdapter.getItem(n);
            }
            return item;
        }
        
        public long getItemId(final int n) {
            long itemId;
            if (this.mAdapter == null) {
                itemId = -1L;
            }
            else {
                itemId = this.mAdapter.getItemId(n);
            }
            return itemId;
        }
        
        public int getItemViewType(final int n) {
            return 0;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            return this.getDropDownView(n, view, viewGroup);
        }
        
        public int getViewTypeCount() {
            return 1;
        }
        
        public boolean hasStableIds() {
            return this.mAdapter != null && this.mAdapter.hasStableIds();
        }
        
        public boolean isEmpty() {
            return this.getCount() == 0;
        }
        
        public boolean isEnabled(final int n) {
            final ListAdapter mListAdapter = this.mListAdapter;
            return mListAdapter == null || mListAdapter.isEnabled(n);
        }
        
        public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
            if (this.mAdapter != null) {
                this.mAdapter.registerDataSetObserver(dataSetObserver);
            }
        }
        
        public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
            if (this.mAdapter != null) {
                this.mAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }
    
    private class DropdownPopup extends ListPopupWindow
    {
        ListAdapter mAdapter;
        private CharSequence mHintText;
        private final Rect mVisibleRect;
        
        public DropdownPopup(final Context context, final AttributeSet set, final int n) {
            super(context, set, n);
            this.mVisibleRect = new Rect();
            this.setAnchorView((View)AppCompatSpinner.this);
            this.setModal(true);
            this.setPromptPosition(0);
            this.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                public void onItemClick(final AdapterView<?> adapterView, final View view, final int selection, final long n) {
                    AppCompatSpinner.this.setSelection(selection);
                    if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                        AppCompatSpinner.this.performItemClick(view, selection, DropdownPopup.this.mAdapter.getItemId(selection));
                    }
                    DropdownPopup.this.dismiss();
                }
            });
        }
        
        void computeContentWidth() {
            final Drawable background = this.getBackground();
            int right = 0;
            if (background != null) {
                background.getPadding(AppCompatSpinner.this.mTempRect);
                if (ViewUtils.isLayoutRtl((View)AppCompatSpinner.this)) {
                    right = AppCompatSpinner.this.mTempRect.right;
                }
                else {
                    right = -AppCompatSpinner.this.mTempRect.left;
                }
            }
            else {
                final Rect access$100 = AppCompatSpinner.this.mTempRect;
                AppCompatSpinner.this.mTempRect.right = 0;
                access$100.left = 0;
            }
            final int paddingLeft = AppCompatSpinner.this.getPaddingLeft();
            final int paddingRight = AppCompatSpinner.this.getPaddingRight();
            final int width = AppCompatSpinner.this.getWidth();
            if (AppCompatSpinner.this.mDropDownWidth == -2) {
                final int compatMeasureContentWidth = AppCompatSpinner.this.compatMeasureContentWidth((SpinnerAdapter)this.mAdapter, this.getBackground());
                final int n = AppCompatSpinner.this.getContext().getResources().getDisplayMetrics().widthPixels - AppCompatSpinner.this.mTempRect.left - AppCompatSpinner.this.mTempRect.right;
                int a;
                if ((a = compatMeasureContentWidth) > n) {
                    a = n;
                }
                this.setContentWidth(Math.max(a, width - paddingLeft - paddingRight));
            }
            else if (AppCompatSpinner.this.mDropDownWidth == -1) {
                this.setContentWidth(width - paddingLeft - paddingRight);
            }
            else {
                this.setContentWidth(AppCompatSpinner.this.mDropDownWidth);
            }
            int horizontalOffset;
            if (ViewUtils.isLayoutRtl((View)AppCompatSpinner.this)) {
                horizontalOffset = right + (width - paddingRight - this.getWidth());
            }
            else {
                horizontalOffset = right + paddingLeft;
            }
            this.setHorizontalOffset(horizontalOffset);
        }
        
        public CharSequence getHintText() {
            return this.mHintText;
        }
        
        boolean isVisibleToUser(final View view) {
            return ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(this.mVisibleRect);
        }
        
        @Override
        public void setAdapter(final ListAdapter listAdapter) {
            super.setAdapter(listAdapter);
            this.mAdapter = listAdapter;
        }
        
        public void setPromptText(final CharSequence mHintText) {
            this.mHintText = mHintText;
        }
        
        @Override
        public void show() {
            final boolean showing = this.isShowing();
            this.computeContentWidth();
            this.setInputMethodMode(2);
            super.show();
            this.getListView().setChoiceMode(1);
            this.setSelection(AppCompatSpinner.this.getSelectedItemPosition());
            if (showing) {
                return;
            }
            final ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
            if (viewTreeObserver != null) {
                final ViewTreeObserver$OnGlobalLayoutListener viewTreeObserver$OnGlobalLayoutListener = (ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        if (!DropdownPopup.this.isVisibleToUser((View)AppCompatSpinner.this)) {
                            DropdownPopup.this.dismiss();
                        }
                        else {
                            DropdownPopup.this.computeContentWidth();
                            ListPopupWindow.this.show();
                        }
                    }
                };
                viewTreeObserver.addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)viewTreeObserver$OnGlobalLayoutListener);
                this.setOnDismissListener((PopupWindow$OnDismissListener)new PopupWindow$OnDismissListener() {
                    public void onDismiss() {
                        final ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                        if (viewTreeObserver != null) {
                            viewTreeObserver.removeGlobalOnLayoutListener(viewTreeObserver$OnGlobalLayoutListener);
                        }
                    }
                });
            }
        }
    }
}

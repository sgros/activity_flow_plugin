// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.widget.TextView$BufferType;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Canvas;
import androidx.annotation.Keep;
import android.text.Layout$Alignment;
import android.text.TextUtils;
import android.annotation.TargetApi;
import org.telegram.messenger.AndroidUtilities;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.os.Build$VERSION;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.graphics.Paint;
import android.animation.AnimatorSet;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.text.StaticLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import android.widget.EditText;

public class EditTextBoldCursor extends EditText
{
    private static Class editorClass;
    private static Method getVerticalOffsetMethod;
    private static Field mCursorDrawableField;
    private static Field mCursorDrawableResField;
    private static Field mEditor;
    private static Field mScrollYField;
    private static Field mShowCursorField;
    private int activeLineColor;
    private boolean allowDrawCursor;
    private boolean currentDrawHintAsHeader;
    private int cursorSize;
    private float cursorWidth;
    private Object editor;
    private StaticLayout errorLayout;
    private int errorLineColor;
    private TextPaint errorPaint;
    private CharSequence errorText;
    private boolean fixed;
    private GradientDrawable gradientDrawable;
    private float headerAnimationProgress;
    private int headerHintColor;
    private AnimatorSet headerTransformAnimation;
    private float hintAlpha;
    private int hintColor;
    private StaticLayout hintLayout;
    private boolean hintVisible;
    private int ignoreBottomCount;
    private int ignoreTopCount;
    private long lastUpdateTime;
    private int lineColor;
    private Paint linePaint;
    private float lineSpacingExtra;
    private float lineY;
    private ViewTreeObserver$OnPreDrawListener listenerFixer;
    private Drawable mCursorDrawable;
    private boolean nextSetTextAnimated;
    private Rect rect;
    private int scrollY;
    private boolean supportRtlHint;
    private boolean transformHintToHeader;
    
    public EditTextBoldCursor(final Context context) {
        super(context);
        this.rect = new Rect();
        this.hintVisible = true;
        this.hintAlpha = 1.0f;
        this.allowDrawCursor = true;
        this.cursorWidth = 2.0f;
        if (Build$VERSION.SDK_INT >= 26) {
            this.setImportantForAutofill(2);
        }
        this.init();
    }
    
    private void checkHeaderVisibility(final boolean b) {
        final boolean currentDrawHintAsHeader = this.transformHintToHeader && (this.isFocused() || this.getText().length() > 0);
        if (this.currentDrawHintAsHeader != currentDrawHintAsHeader) {
            final AnimatorSet headerTransformAnimation = this.headerTransformAnimation;
            if (headerTransformAnimation != null) {
                headerTransformAnimation.cancel();
                this.headerTransformAnimation = null;
            }
            this.currentDrawHintAsHeader = currentDrawHintAsHeader;
            float headerAnimationProgress = 1.0f;
            if (b) {
                this.headerTransformAnimation = new AnimatorSet();
                final AnimatorSet headerTransformAnimation2 = this.headerTransformAnimation;
                if (!currentDrawHintAsHeader) {
                    headerAnimationProgress = 0.0f;
                }
                headerTransformAnimation2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "headerAnimationProgress", new float[] { headerAnimationProgress }) });
                this.headerTransformAnimation.setDuration(200L);
                this.headerTransformAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT_QUINT);
                this.headerTransformAnimation.start();
            }
            else {
                if (!currentDrawHintAsHeader) {
                    headerAnimationProgress = 0.0f;
                }
                this.headerAnimationProgress = headerAnimationProgress;
            }
            this.invalidate();
        }
    }
    
    @SuppressLint({ "PrivateApi" })
    private void init() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: new             Landroid/graphics/Paint;
        //     4: dup            
        //     5: invokespecial   android/graphics/Paint.<init>:()V
        //     8: putfield        org/telegram/ui/Components/EditTextBoldCursor.linePaint:Landroid/graphics/Paint;
        //    11: aload_0        
        //    12: new             Landroid/text/TextPaint;
        //    15: dup            
        //    16: iconst_1       
        //    17: invokespecial   android/text/TextPaint.<init>:(I)V
        //    20: putfield        org/telegram/ui/Components/EditTextBoldCursor.errorPaint:Landroid/text/TextPaint;
        //    23: aload_0        
        //    24: getfield        org/telegram/ui/Components/EditTextBoldCursor.errorPaint:Landroid/text/TextPaint;
        //    27: ldc             11.0
        //    29: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //    32: i2f            
        //    33: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //    36: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    39: bipush          26
        //    41: if_icmplt       49
        //    44: aload_0        
        //    45: iconst_2       
        //    46: invokevirtual   android/widget/EditText.setImportantForAutofill:(I)V
        //    49: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mScrollYField:Ljava/lang/reflect/Field;
        //    52: ifnonnull       72
        //    55: ldc             Landroid/view/View;.class
        //    57: ldc             "mScrollY"
        //    59: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    62: putstatic       org/telegram/ui/Components/EditTextBoldCursor.mScrollYField:Ljava/lang/reflect/Field;
        //    65: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mScrollYField:Ljava/lang/reflect/Field;
        //    68: iconst_1       
        //    69: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    72: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawableResField:Ljava/lang/reflect/Field;
        //    75: ifnonnull       95
        //    78: ldc             Landroid/widget/TextView;.class
        //    80: ldc             "mCursorDrawableRes"
        //    82: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    85: putstatic       org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawableResField:Ljava/lang/reflect/Field;
        //    88: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawableResField:Ljava/lang/reflect/Field;
        //    91: iconst_1       
        //    92: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    95: getstatic       org/telegram/ui/Components/EditTextBoldCursor.editorClass:Ljava/lang/Class;
        //    98: ifnonnull       253
        //   101: ldc             Landroid/widget/TextView;.class
        //   103: ldc             "mEditor"
        //   105: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //   108: putstatic       org/telegram/ui/Components/EditTextBoldCursor.mEditor:Ljava/lang/reflect/Field;
        //   111: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mEditor:Ljava/lang/reflect/Field;
        //   114: iconst_1       
        //   115: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //   118: ldc             "android.widget.Editor"
        //   120: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   123: putstatic       org/telegram/ui/Components/EditTextBoldCursor.editorClass:Ljava/lang/Class;
        //   126: getstatic       org/telegram/ui/Components/EditTextBoldCursor.editorClass:Ljava/lang/Class;
        //   129: ldc             "mShowCursor"
        //   131: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //   134: putstatic       org/telegram/ui/Components/EditTextBoldCursor.mShowCursorField:Ljava/lang/reflect/Field;
        //   137: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mShowCursorField:Ljava/lang/reflect/Field;
        //   140: iconst_1       
        //   141: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //   144: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   147: bipush          28
        //   149: if_icmplt       155
        //   152: goto            173
        //   155: getstatic       org/telegram/ui/Components/EditTextBoldCursor.editorClass:Ljava/lang/Class;
        //   158: ldc             "mCursorDrawable"
        //   160: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //   163: putstatic       org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawableField:Ljava/lang/reflect/Field;
        //   166: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawableField:Ljava/lang/reflect/Field;
        //   169: iconst_1       
        //   170: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //   173: ldc             Landroid/widget/TextView;.class
        //   175: ldc             "getVerticalOffset"
        //   177: iconst_1       
        //   178: anewarray       Ljava/lang/Class;
        //   181: dup            
        //   182: iconst_0       
        //   183: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
        //   186: aastore        
        //   187: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //   190: putstatic       org/telegram/ui/Components/EditTextBoldCursor.getVerticalOffsetMethod:Ljava/lang/reflect/Method;
        //   193: getstatic       org/telegram/ui/Components/EditTextBoldCursor.getVerticalOffsetMethod:Ljava/lang/reflect/Method;
        //   196: iconst_1       
        //   197: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
        //   200: getstatic       org/telegram/ui/Components/EditTextBoldCursor.editorClass:Ljava/lang/Class;
        //   203: ldc             "mShowCursor"
        //   205: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //   208: putstatic       org/telegram/ui/Components/EditTextBoldCursor.mShowCursorField:Ljava/lang/reflect/Field;
        //   211: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mShowCursorField:Ljava/lang/reflect/Field;
        //   214: iconst_1       
        //   215: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //   218: ldc             Landroid/widget/TextView;.class
        //   220: ldc             "getVerticalOffset"
        //   222: iconst_1       
        //   223: anewarray       Ljava/lang/Class;
        //   226: dup            
        //   227: iconst_0       
        //   228: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
        //   231: aastore        
        //   232: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //   235: putstatic       org/telegram/ui/Components/EditTextBoldCursor.getVerticalOffsetMethod:Ljava/lang/reflect/Method;
        //   238: getstatic       org/telegram/ui/Components/EditTextBoldCursor.getVerticalOffsetMethod:Ljava/lang/reflect/Method;
        //   241: iconst_1       
        //   242: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
        //   245: goto            253
        //   248: astore_1       
        //   249: aload_1        
        //   250: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   253: new             Landroid/graphics/drawable/GradientDrawable;
        //   256: astore_1       
        //   257: aload_1        
        //   258: getstatic       android/graphics/drawable/GradientDrawable$Orientation.TOP_BOTTOM:Landroid/graphics/drawable/GradientDrawable$Orientation;
        //   261: iconst_2       
        //   262: newarray        I
        //   264: dup            
        //   265: iconst_0       
        //   266: ldc_w           -11230757
        //   269: iastore        
        //   270: dup            
        //   271: iconst_1       
        //   272: ldc_w           -11230757
        //   275: iastore        
        //   276: invokespecial   android/graphics/drawable/GradientDrawable.<init>:(Landroid/graphics/drawable/GradientDrawable$Orientation;[I)V
        //   279: aload_0        
        //   280: aload_1        
        //   281: putfield        org/telegram/ui/Components/EditTextBoldCursor.gradientDrawable:Landroid/graphics/drawable/GradientDrawable;
        //   284: aload_0        
        //   285: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mEditor:Ljava/lang/reflect/Field;
        //   288: aload_0        
        //   289: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   292: putfield        org/telegram/ui/Components/EditTextBoldCursor.editor:Ljava/lang/Object;
        //   295: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawableField:Ljava/lang/reflect/Field;
        //   298: ifnull          314
        //   301: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawableResField:Ljava/lang/reflect/Field;
        //   304: aload_0        
        //   305: ldc_w           2131165378
        //   308: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   311: invokevirtual   java/lang/reflect/Field.set:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   314: aload_0        
        //   315: ldc_w           24.0
        //   318: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   321: putfield        org/telegram/ui/Components/EditTextBoldCursor.cursorSize:I
        //   324: return         
        //   325: astore_1       
        //   326: goto            72
        //   329: astore_1       
        //   330: goto            95
        //   333: astore_1       
        //   334: goto            314
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  49     72     325    329    Ljava/lang/Throwable;
        //  72     95     329    333    Ljava/lang/Throwable;
        //  95     152    248    253    Ljava/lang/Throwable;
        //  155    173    248    253    Ljava/lang/Throwable;
        //  173    245    248    253    Ljava/lang/Throwable;
        //  253    314    333    337    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 149 out-of-bounds for length 149
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
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
    
    @SuppressLint({ "PrivateApi" })
    public void fixHandleView(final boolean b) {
        if (b) {
            this.fixed = false;
            return;
        }
        if (this.fixed) {
            return;
        }
        while (true) {
            try {
                if (EditTextBoldCursor.editorClass == null) {
                    EditTextBoldCursor.editorClass = Class.forName("android.widget.Editor");
                    (EditTextBoldCursor.mEditor = TextView.class.getDeclaredField("mEditor")).setAccessible(true);
                    this.editor = EditTextBoldCursor.mEditor.get(this);
                }
                if (this.listenerFixer == null) {
                    final Method declaredMethod = EditTextBoldCursor.editorClass.getDeclaredMethod("getPositionListener", (Class[])new Class[0]);
                    declaredMethod.setAccessible(true);
                    this.listenerFixer = (ViewTreeObserver$OnPreDrawListener)declaredMethod.invoke(this.editor, new Object[0]);
                }
                final ViewTreeObserver$OnPreDrawListener listenerFixer = this.listenerFixer;
                listenerFixer.getClass();
                AndroidUtilities.runOnUIThread(new _$$Lambda$qzh_QoBZ7K2XdUWK2VAJcGTe1OY(listenerFixer), 500L);
                this.fixed = true;
            }
            catch (Throwable t) {
                continue;
            }
            break;
        }
    }
    
    @TargetApi(26)
    public int getAutofillType() {
        return 0;
    }
    
    public StaticLayout getErrorLayout(final int n) {
        if (TextUtils.isEmpty(this.errorText)) {
            return null;
        }
        return new StaticLayout(this.errorText, this.errorPaint, n, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }
    
    public int getExtendedPaddingBottom() {
        final int ignoreBottomCount = this.ignoreBottomCount;
        if (ignoreBottomCount != 0) {
            this.ignoreBottomCount = ignoreBottomCount - 1;
            final int scrollY = this.scrollY;
            int n;
            if (scrollY != Integer.MAX_VALUE) {
                n = -scrollY;
            }
            else {
                n = 0;
            }
            return n;
        }
        return super.getExtendedPaddingBottom();
    }
    
    public int getExtendedPaddingTop() {
        final int ignoreTopCount = this.ignoreTopCount;
        if (ignoreTopCount != 0) {
            this.ignoreTopCount = ignoreTopCount - 1;
            return 0;
        }
        return super.getExtendedPaddingTop();
    }
    
    @Keep
    public float getHeaderAnimationProgress() {
        return this.headerAnimationProgress;
    }
    
    public float getLineY() {
        return this.lineY;
    }
    
    public boolean hasErrorText() {
        return TextUtils.isEmpty(this.errorText) ^ true;
    }
    
    protected void onDraw(final Canvas p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/telegram/ui/Components/EditTextBoldCursor.getExtendedPaddingTop:()I
        //     4: istore_2       
        //     5: aload_0        
        //     6: ldc_w           2147483647
        //     9: putfield        org/telegram/ui/Components/EditTextBoldCursor.scrollY:I
        //    12: aload_0        
        //    13: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mScrollYField:Ljava/lang/reflect/Field;
        //    16: aload_0        
        //    17: invokevirtual   java/lang/reflect/Field.getInt:(Ljava/lang/Object;)I
        //    20: putfield        org/telegram/ui/Components/EditTextBoldCursor.scrollY:I
        //    23: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mScrollYField:Ljava/lang/reflect/Field;
        //    26: aload_0        
        //    27: iconst_0       
        //    28: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    31: invokevirtual   java/lang/reflect/Field.set:(Ljava/lang/Object;Ljava/lang/Object;)V
        //    34: aload_0        
        //    35: iconst_1       
        //    36: putfield        org/telegram/ui/Components/EditTextBoldCursor.ignoreTopCount:I
        //    39: aload_0        
        //    40: iconst_1       
        //    41: putfield        org/telegram/ui/Components/EditTextBoldCursor.ignoreBottomCount:I
        //    44: aload_1        
        //    45: invokevirtual   android/graphics/Canvas.save:()I
        //    48: pop            
        //    49: aload_1        
        //    50: fconst_0       
        //    51: iload_2        
        //    52: i2f            
        //    53: invokevirtual   android/graphics/Canvas.translate:(FF)V
        //    56: aload_0        
        //    57: aload_1        
        //    58: invokespecial   android/widget/EditText.onDraw:(Landroid/graphics/Canvas;)V
        //    61: aload_0        
        //    62: getfield        org/telegram/ui/Components/EditTextBoldCursor.scrollY:I
        //    65: istore_2       
        //    66: iload_2        
        //    67: ldc_w           2147483647
        //    70: if_icmpeq       88
        //    73: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mScrollYField:Ljava/lang/reflect/Field;
        //    76: aload_0        
        //    77: iload_2        
        //    78: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    81: invokevirtual   java/lang/reflect/Field.set:(Ljava/lang/Object;Ljava/lang/Object;)V
        //    84: goto            88
        //    87: astore_3       
        //    88: aload_1        
        //    89: invokevirtual   android/graphics/Canvas.restore:()V
        //    92: aload_0        
        //    93: invokevirtual   android/widget/EditText.length:()I
        //    96: ifeq            106
        //    99: aload_0        
        //   100: getfield        org/telegram/ui/Components/EditTextBoldCursor.transformHintToHeader:Z
        //   103: ifeq            750
        //   106: aload_0        
        //   107: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintLayout:Landroid/text/StaticLayout;
        //   110: ifnull          750
        //   113: aload_0        
        //   114: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintVisible:Z
        //   117: ifne            129
        //   120: aload_0        
        //   121: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   124: fconst_0       
        //   125: fcmpl          
        //   126: ifeq            750
        //   129: aload_0        
        //   130: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintVisible:Z
        //   133: ifeq            145
        //   136: aload_0        
        //   137: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   140: fconst_1       
        //   141: fcmpl          
        //   142: ifne            161
        //   145: aload_0        
        //   146: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintVisible:Z
        //   149: ifne            280
        //   152: aload_0        
        //   153: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   156: fconst_0       
        //   157: fcmpl          
        //   158: ifeq            280
        //   161: invokestatic    java/lang/System.currentTimeMillis:()J
        //   164: lstore          4
        //   166: lload           4
        //   168: aload_0        
        //   169: getfield        org/telegram/ui/Components/EditTextBoldCursor.lastUpdateTime:J
        //   172: lsub           
        //   173: lstore          6
        //   175: lload           6
        //   177: lconst_0       
        //   178: lcmp           
        //   179: iflt            195
        //   182: lload           6
        //   184: lstore          8
        //   186: lload           6
        //   188: ldc2_w          17
        //   191: lcmp           
        //   192: ifle            200
        //   195: ldc2_w          17
        //   198: lstore          8
        //   200: aload_0        
        //   201: lload           4
        //   203: putfield        org/telegram/ui/Components/EditTextBoldCursor.lastUpdateTime:J
        //   206: aload_0        
        //   207: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintVisible:Z
        //   210: ifeq            246
        //   213: aload_0        
        //   214: aload_0        
        //   215: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   218: lload           8
        //   220: l2f            
        //   221: ldc_w           150.0
        //   224: fdiv           
        //   225: fadd           
        //   226: putfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   229: aload_0        
        //   230: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   233: fconst_1       
        //   234: fcmpl          
        //   235: ifle            276
        //   238: aload_0        
        //   239: fconst_1       
        //   240: putfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   243: goto            276
        //   246: aload_0        
        //   247: aload_0        
        //   248: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   251: lload           8
        //   253: l2f            
        //   254: ldc_w           150.0
        //   257: fdiv           
        //   258: fsub           
        //   259: putfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   262: aload_0        
        //   263: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   266: fconst_0       
        //   267: fcmpg          
        //   268: ifge            276
        //   271: aload_0        
        //   272: fconst_0       
        //   273: putfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   276: aload_0        
        //   277: invokevirtual   android/widget/EditText.invalidate:()V
        //   280: aload_0        
        //   281: invokevirtual   android/widget/EditText.getPaint:()Landroid/text/TextPaint;
        //   284: invokevirtual   android/text/TextPaint.getColor:()I
        //   287: istore          10
        //   289: aload_1        
        //   290: invokevirtual   android/graphics/Canvas.save:()I
        //   293: pop            
        //   294: aload_0        
        //   295: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintLayout:Landroid/text/StaticLayout;
        //   298: iconst_0       
        //   299: invokevirtual   android/text/StaticLayout.getLineLeft:(I)F
        //   302: fstore          11
        //   304: aload_0        
        //   305: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintLayout:Landroid/text/StaticLayout;
        //   308: iconst_0       
        //   309: invokevirtual   android/text/StaticLayout.getLineWidth:(I)F
        //   312: fstore          12
        //   314: fload           11
        //   316: fconst_0       
        //   317: fcmpl          
        //   318: ifeq            331
        //   321: iconst_0       
        //   322: i2f            
        //   323: fload           11
        //   325: fsub           
        //   326: f2i            
        //   327: istore_2       
        //   328: goto            333
        //   331: iconst_0       
        //   332: istore_2       
        //   333: aload_0        
        //   334: getfield        org/telegram/ui/Components/EditTextBoldCursor.supportRtlHint:Z
        //   337: ifeq            394
        //   340: getstatic       org/telegram/messenger/LocaleController.isRTL:Z
        //   343: ifeq            394
        //   346: aload_0        
        //   347: invokevirtual   android/widget/EditText.getMeasuredWidth:()I
        //   350: i2f            
        //   351: fstore          13
        //   353: aload_1        
        //   354: iload_2        
        //   355: aload_0        
        //   356: invokevirtual   android/widget/EditText.getScrollX:()I
        //   359: iadd           
        //   360: i2f            
        //   361: fload           13
        //   363: fload           12
        //   365: fsub           
        //   366: fadd           
        //   367: aload_0        
        //   368: getfield        org/telegram/ui/Components/EditTextBoldCursor.lineY:F
        //   371: aload_0        
        //   372: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintLayout:Landroid/text/StaticLayout;
        //   375: invokevirtual   android/text/StaticLayout.getHeight:()I
        //   378: i2f            
        //   379: fsub           
        //   380: ldc_w           6.0
        //   383: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   386: i2f            
        //   387: fsub           
        //   388: invokevirtual   android/graphics/Canvas.translate:(FF)V
        //   391: goto            426
        //   394: aload_1        
        //   395: iload_2        
        //   396: aload_0        
        //   397: invokevirtual   android/widget/EditText.getScrollX:()I
        //   400: iadd           
        //   401: i2f            
        //   402: aload_0        
        //   403: getfield        org/telegram/ui/Components/EditTextBoldCursor.lineY:F
        //   406: aload_0        
        //   407: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintLayout:Landroid/text/StaticLayout;
        //   410: invokevirtual   android/text/StaticLayout.getHeight:()I
        //   413: i2f            
        //   414: fsub           
        //   415: ldc_w           6.0
        //   418: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   421: i2f            
        //   422: fsub           
        //   423: invokevirtual   android/graphics/Canvas.translate:(FF)V
        //   426: aload_0        
        //   427: getfield        org/telegram/ui/Components/EditTextBoldCursor.transformHintToHeader:Z
        //   430: ifeq            689
        //   433: fconst_1       
        //   434: aload_0        
        //   435: getfield        org/telegram/ui/Components/EditTextBoldCursor.headerAnimationProgress:F
        //   438: ldc_w           0.3
        //   441: fmul           
        //   442: fsub           
        //   443: fstore          13
        //   445: ldc_w           22.0
        //   448: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   451: ineg           
        //   452: i2f            
        //   453: fstore          14
        //   455: aload_0        
        //   456: getfield        org/telegram/ui/Components/EditTextBoldCursor.headerAnimationProgress:F
        //   459: fstore          15
        //   461: aload_0        
        //   462: getfield        org/telegram/ui/Components/EditTextBoldCursor.headerHintColor:I
        //   465: invokestatic    android/graphics/Color.red:(I)I
        //   468: istore_2       
        //   469: aload_0        
        //   470: getfield        org/telegram/ui/Components/EditTextBoldCursor.headerHintColor:I
        //   473: invokestatic    android/graphics/Color.green:(I)I
        //   476: istore          16
        //   478: aload_0        
        //   479: getfield        org/telegram/ui/Components/EditTextBoldCursor.headerHintColor:I
        //   482: invokestatic    android/graphics/Color.blue:(I)I
        //   485: istore          17
        //   487: aload_0        
        //   488: getfield        org/telegram/ui/Components/EditTextBoldCursor.headerHintColor:I
        //   491: invokestatic    android/graphics/Color.alpha:(I)I
        //   494: istore          18
        //   496: aload_0        
        //   497: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintColor:I
        //   500: invokestatic    android/graphics/Color.red:(I)I
        //   503: istore          19
        //   505: aload_0        
        //   506: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintColor:I
        //   509: invokestatic    android/graphics/Color.green:(I)I
        //   512: istore          20
        //   514: aload_0        
        //   515: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintColor:I
        //   518: invokestatic    android/graphics/Color.blue:(I)I
        //   521: istore          21
        //   523: aload_0        
        //   524: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintColor:I
        //   527: invokestatic    android/graphics/Color.alpha:(I)I
        //   530: istore          22
        //   532: aload_0        
        //   533: getfield        org/telegram/ui/Components/EditTextBoldCursor.supportRtlHint:Z
        //   536: ifeq            568
        //   539: getstatic       org/telegram/messenger/LocaleController.isRTL:Z
        //   542: ifeq            568
        //   545: fload           12
        //   547: fload           11
        //   549: fadd           
        //   550: fstore          11
        //   552: aload_1        
        //   553: fload           11
        //   555: fload           11
        //   557: fload           13
        //   559: fmul           
        //   560: fsub           
        //   561: fconst_0       
        //   562: invokevirtual   android/graphics/Canvas.translate:(FF)V
        //   565: goto            587
        //   568: fload           11
        //   570: fconst_0       
        //   571: fcmpl          
        //   572: ifeq            587
        //   575: aload_1        
        //   576: fload           11
        //   578: fconst_1       
        //   579: fload           13
        //   581: fsub           
        //   582: fmul           
        //   583: fconst_0       
        //   584: invokevirtual   android/graphics/Canvas.translate:(FF)V
        //   587: aload_1        
        //   588: fload           13
        //   590: fload           13
        //   592: invokevirtual   android/graphics/Canvas.scale:(FF)V
        //   595: aload_1        
        //   596: fconst_0       
        //   597: fload           14
        //   599: fload           15
        //   601: fmul           
        //   602: invokevirtual   android/graphics/Canvas.translate:(FF)V
        //   605: aload_0        
        //   606: invokevirtual   android/widget/EditText.getPaint:()Landroid/text/TextPaint;
        //   609: astore_3       
        //   610: iload           22
        //   612: i2f            
        //   613: fstore          11
        //   615: iload           18
        //   617: iload           22
        //   619: isub           
        //   620: i2f            
        //   621: fstore          13
        //   623: aload_0        
        //   624: getfield        org/telegram/ui/Components/EditTextBoldCursor.headerAnimationProgress:F
        //   627: fstore          15
        //   629: aload_3        
        //   630: fload           11
        //   632: fload           13
        //   634: fload           15
        //   636: fmul           
        //   637: fadd           
        //   638: f2i            
        //   639: iload           19
        //   641: i2f            
        //   642: iload_2        
        //   643: iload           19
        //   645: isub           
        //   646: i2f            
        //   647: fload           15
        //   649: fmul           
        //   650: fadd           
        //   651: f2i            
        //   652: iload           20
        //   654: i2f            
        //   655: iload           16
        //   657: iload           20
        //   659: isub           
        //   660: i2f            
        //   661: fload           15
        //   663: fmul           
        //   664: fadd           
        //   665: f2i            
        //   666: iload           21
        //   668: i2f            
        //   669: iload           17
        //   671: iload           21
        //   673: isub           
        //   674: i2f            
        //   675: fload           15
        //   677: fmul           
        //   678: fadd           
        //   679: f2i            
        //   680: invokestatic    android/graphics/Color.argb:(IIII)I
        //   683: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   686: goto            729
        //   689: aload_0        
        //   690: invokevirtual   android/widget/EditText.getPaint:()Landroid/text/TextPaint;
        //   693: aload_0        
        //   694: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintColor:I
        //   697: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   700: aload_0        
        //   701: invokevirtual   android/widget/EditText.getPaint:()Landroid/text/TextPaint;
        //   704: aload_0        
        //   705: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintAlpha:F
        //   708: ldc_w           255.0
        //   711: fmul           
        //   712: aload_0        
        //   713: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintColor:I
        //   716: invokestatic    android/graphics/Color.alpha:(I)I
        //   719: i2f            
        //   720: ldc_w           255.0
        //   723: fdiv           
        //   724: fmul           
        //   725: f2i            
        //   726: invokevirtual   android/text/TextPaint.setAlpha:(I)V
        //   729: aload_0        
        //   730: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintLayout:Landroid/text/StaticLayout;
        //   733: aload_1        
        //   734: invokevirtual   android/text/StaticLayout.draw:(Landroid/graphics/Canvas;)V
        //   737: aload_0        
        //   738: invokevirtual   android/widget/EditText.getPaint:()Landroid/text/TextPaint;
        //   741: iload           10
        //   743: invokevirtual   android/text/TextPaint.setColor:(I)V
        //   746: aload_1        
        //   747: invokevirtual   android/graphics/Canvas.restore:()V
        //   750: aload_0        
        //   751: getfield        org/telegram/ui/Components/EditTextBoldCursor.allowDrawCursor:Z
        //   754: ifeq            1115
        //   757: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mShowCursorField:Ljava/lang/reflect/Field;
        //   760: ifnull          1115
        //   763: aload_0        
        //   764: getfield        org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawable:Landroid/graphics/drawable/Drawable;
        //   767: ifnonnull       817
        //   770: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   773: bipush          28
        //   775: if_icmplt       798
        //   778: aload_0        
        //   779: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawableField:Ljava/lang/reflect/Field;
        //   782: aload_0        
        //   783: getfield        org/telegram/ui/Components/EditTextBoldCursor.editor:Ljava/lang/Object;
        //   786: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   789: checkcast       Landroid/graphics/drawable/Drawable;
        //   792: putfield        org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawable:Landroid/graphics/drawable/Drawable;
        //   795: goto            817
        //   798: aload_0        
        //   799: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawableField:Ljava/lang/reflect/Field;
        //   802: aload_0        
        //   803: getfield        org/telegram/ui/Components/EditTextBoldCursor.editor:Ljava/lang/Object;
        //   806: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   809: checkcast       [Landroid/graphics/drawable/Drawable;
        //   812: iconst_0       
        //   813: aaload         
        //   814: putfield        org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawable:Landroid/graphics/drawable/Drawable;
        //   817: aload_0        
        //   818: getfield        org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawable:Landroid/graphics/drawable/Drawable;
        //   821: ifnonnull       825
        //   824: return         
        //   825: getstatic       org/telegram/ui/Components/EditTextBoldCursor.mShowCursorField:Ljava/lang/reflect/Field;
        //   828: aload_0        
        //   829: getfield        org/telegram/ui/Components/EditTextBoldCursor.editor:Ljava/lang/Object;
        //   832: invokevirtual   java/lang/reflect/Field.getLong:(Ljava/lang/Object;)J
        //   835: lstore          8
        //   837: invokestatic    android/os/SystemClock.uptimeMillis:()J
        //   840: lload           8
        //   842: lsub           
        //   843: ldc2_w          1000
        //   846: lrem           
        //   847: ldc2_w          500
        //   850: lcmp           
        //   851: ifge            866
        //   854: aload_0        
        //   855: invokevirtual   android/widget/EditText.isFocused:()Z
        //   858: ifeq            866
        //   861: iconst_1       
        //   862: istore_2       
        //   863: goto            868
        //   866: iconst_0       
        //   867: istore_2       
        //   868: iload_2        
        //   869: ifeq            1115
        //   872: aload_1        
        //   873: invokevirtual   android/graphics/Canvas.save:()I
        //   876: pop            
        //   877: aload_0        
        //   878: invokevirtual   android/widget/EditText.getGravity:()I
        //   881: bipush          112
        //   883: iand           
        //   884: bipush          48
        //   886: if_icmpeq       917
        //   889: getstatic       org/telegram/ui/Components/EditTextBoldCursor.getVerticalOffsetMethod:Ljava/lang/reflect/Method;
        //   892: aload_0        
        //   893: iconst_1       
        //   894: anewarray       Ljava/lang/Object;
        //   897: dup            
        //   898: iconst_0       
        //   899: iconst_1       
        //   900: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   903: aastore        
        //   904: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   907: checkcast       Ljava/lang/Integer;
        //   910: invokevirtual   java/lang/Integer.intValue:()I
        //   913: istore_2       
        //   914: goto            919
        //   917: iconst_0       
        //   918: istore_2       
        //   919: aload_1        
        //   920: aload_0        
        //   921: invokevirtual   android/widget/EditText.getPaddingLeft:()I
        //   924: i2f            
        //   925: aload_0        
        //   926: invokevirtual   org/telegram/ui/Components/EditTextBoldCursor.getExtendedPaddingTop:()I
        //   929: iload_2        
        //   930: iadd           
        //   931: i2f            
        //   932: invokevirtual   android/graphics/Canvas.translate:(FF)V
        //   935: aload_0        
        //   936: invokevirtual   android/widget/EditText.getLayout:()Landroid/text/Layout;
        //   939: astore_3       
        //   940: aload_3        
        //   941: aload_0        
        //   942: invokevirtual   android/widget/EditText.getSelectionStart:()I
        //   945: invokevirtual   android/text/Layout.getLineForOffset:(I)I
        //   948: istore          10
        //   950: aload_3        
        //   951: invokevirtual   android/text/Layout.getLineCount:()I
        //   954: istore_2       
        //   955: aload_0        
        //   956: getfield        org/telegram/ui/Components/EditTextBoldCursor.mCursorDrawable:Landroid/graphics/drawable/Drawable;
        //   959: invokevirtual   android/graphics/drawable/Drawable.getBounds:()Landroid/graphics/Rect;
        //   962: astore_3       
        //   963: aload_0        
        //   964: getfield        org/telegram/ui/Components/EditTextBoldCursor.rect:Landroid/graphics/Rect;
        //   967: aload_3        
        //   968: getfield        android/graphics/Rect.left:I
        //   971: putfield        android/graphics/Rect.left:I
        //   974: aload_0        
        //   975: getfield        org/telegram/ui/Components/EditTextBoldCursor.rect:Landroid/graphics/Rect;
        //   978: aload_3        
        //   979: getfield        android/graphics/Rect.left:I
        //   982: aload_0        
        //   983: getfield        org/telegram/ui/Components/EditTextBoldCursor.cursorWidth:F
        //   986: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   989: iadd           
        //   990: putfield        android/graphics/Rect.right:I
        //   993: aload_0        
        //   994: getfield        org/telegram/ui/Components/EditTextBoldCursor.rect:Landroid/graphics/Rect;
        //   997: aload_3        
        //   998: getfield        android/graphics/Rect.bottom:I
        //  1001: putfield        android/graphics/Rect.bottom:I
        //  1004: aload_0        
        //  1005: getfield        org/telegram/ui/Components/EditTextBoldCursor.rect:Landroid/graphics/Rect;
        //  1008: aload_3        
        //  1009: getfield        android/graphics/Rect.top:I
        //  1012: putfield        android/graphics/Rect.top:I
        //  1015: aload_0        
        //  1016: getfield        org/telegram/ui/Components/EditTextBoldCursor.lineSpacingExtra:F
        //  1019: fconst_0       
        //  1020: fcmpl          
        //  1021: ifeq            1052
        //  1024: iload           10
        //  1026: iload_2        
        //  1027: iconst_1       
        //  1028: isub           
        //  1029: if_icmpge       1052
        //  1032: aload_0        
        //  1033: getfield        org/telegram/ui/Components/EditTextBoldCursor.rect:Landroid/graphics/Rect;
        //  1036: astore_3       
        //  1037: aload_3        
        //  1038: aload_3        
        //  1039: getfield        android/graphics/Rect.bottom:I
        //  1042: i2f            
        //  1043: aload_0        
        //  1044: getfield        org/telegram/ui/Components/EditTextBoldCursor.lineSpacingExtra:F
        //  1047: fsub           
        //  1048: f2i            
        //  1049: putfield        android/graphics/Rect.bottom:I
        //  1052: aload_0        
        //  1053: getfield        org/telegram/ui/Components/EditTextBoldCursor.rect:Landroid/graphics/Rect;
        //  1056: aload_0        
        //  1057: getfield        org/telegram/ui/Components/EditTextBoldCursor.rect:Landroid/graphics/Rect;
        //  1060: invokevirtual   android/graphics/Rect.centerY:()I
        //  1063: aload_0        
        //  1064: getfield        org/telegram/ui/Components/EditTextBoldCursor.cursorSize:I
        //  1067: iconst_2       
        //  1068: idiv           
        //  1069: isub           
        //  1070: putfield        android/graphics/Rect.top:I
        //  1073: aload_0        
        //  1074: getfield        org/telegram/ui/Components/EditTextBoldCursor.rect:Landroid/graphics/Rect;
        //  1077: aload_0        
        //  1078: getfield        org/telegram/ui/Components/EditTextBoldCursor.rect:Landroid/graphics/Rect;
        //  1081: getfield        android/graphics/Rect.top:I
        //  1084: aload_0        
        //  1085: getfield        org/telegram/ui/Components/EditTextBoldCursor.cursorSize:I
        //  1088: iadd           
        //  1089: putfield        android/graphics/Rect.bottom:I
        //  1092: aload_0        
        //  1093: getfield        org/telegram/ui/Components/EditTextBoldCursor.gradientDrawable:Landroid/graphics/drawable/GradientDrawable;
        //  1096: aload_0        
        //  1097: getfield        org/telegram/ui/Components/EditTextBoldCursor.rect:Landroid/graphics/Rect;
        //  1100: invokevirtual   android/graphics/drawable/GradientDrawable.setBounds:(Landroid/graphics/Rect;)V
        //  1103: aload_0        
        //  1104: getfield        org/telegram/ui/Components/EditTextBoldCursor.gradientDrawable:Landroid/graphics/drawable/GradientDrawable;
        //  1107: aload_1        
        //  1108: invokevirtual   android/graphics/drawable/GradientDrawable.draw:(Landroid/graphics/Canvas;)V
        //  1111: aload_1        
        //  1112: invokevirtual   android/graphics/Canvas.restore:()V
        //  1115: aload_0        
        //  1116: getfield        org/telegram/ui/Components/EditTextBoldCursor.lineColor:I
        //  1119: ifeq            1236
        //  1122: aload_0        
        //  1123: getfield        org/telegram/ui/Components/EditTextBoldCursor.hintLayout:Landroid/text/StaticLayout;
        //  1126: ifnull          1236
        //  1129: aload_0        
        //  1130: getfield        org/telegram/ui/Components/EditTextBoldCursor.errorText:Ljava/lang/CharSequence;
        //  1133: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  1136: ifne            1158
        //  1139: aload_0        
        //  1140: getfield        org/telegram/ui/Components/EditTextBoldCursor.linePaint:Landroid/graphics/Paint;
        //  1143: aload_0        
        //  1144: getfield        org/telegram/ui/Components/EditTextBoldCursor.errorLineColor:I
        //  1147: invokevirtual   android/graphics/Paint.setColor:(I)V
        //  1150: fconst_2       
        //  1151: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1154: istore_2       
        //  1155: goto            1200
        //  1158: aload_0        
        //  1159: invokevirtual   android/widget/EditText.isFocused:()Z
        //  1162: ifeq            1184
        //  1165: aload_0        
        //  1166: getfield        org/telegram/ui/Components/EditTextBoldCursor.linePaint:Landroid/graphics/Paint;
        //  1169: aload_0        
        //  1170: getfield        org/telegram/ui/Components/EditTextBoldCursor.activeLineColor:I
        //  1173: invokevirtual   android/graphics/Paint.setColor:(I)V
        //  1176: fconst_2       
        //  1177: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1180: istore_2       
        //  1181: goto            1200
        //  1184: aload_0        
        //  1185: getfield        org/telegram/ui/Components/EditTextBoldCursor.linePaint:Landroid/graphics/Paint;
        //  1188: aload_0        
        //  1189: getfield        org/telegram/ui/Components/EditTextBoldCursor.lineColor:I
        //  1192: invokevirtual   android/graphics/Paint.setColor:(I)V
        //  1195: fconst_1       
        //  1196: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1199: istore_2       
        //  1200: aload_1        
        //  1201: aload_0        
        //  1202: invokevirtual   android/widget/EditText.getScrollX:()I
        //  1205: i2f            
        //  1206: aload_0        
        //  1207: getfield        org/telegram/ui/Components/EditTextBoldCursor.lineY:F
        //  1210: f2i            
        //  1211: i2f            
        //  1212: aload_0        
        //  1213: invokevirtual   android/widget/EditText.getScrollX:()I
        //  1216: aload_0        
        //  1217: invokevirtual   android/widget/EditText.getMeasuredWidth:()I
        //  1220: iadd           
        //  1221: i2f            
        //  1222: aload_0        
        //  1223: getfield        org/telegram/ui/Components/EditTextBoldCursor.lineY:F
        //  1226: iload_2        
        //  1227: i2f            
        //  1228: fadd           
        //  1229: aload_0        
        //  1230: getfield        org/telegram/ui/Components/EditTextBoldCursor.linePaint:Landroid/graphics/Paint;
        //  1233: invokevirtual   android/graphics/Canvas.drawRect:(FFFFLandroid/graphics/Paint;)V
        //  1236: return         
        //  1237: astore_3       
        //  1238: goto            34
        //  1241: astore_3       
        //  1242: goto            61
        //  1245: astore_3       
        //  1246: goto            1115
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  12     34     1237   1241   Ljava/lang/Exception;
        //  56     61     1241   1245   Ljava/lang/Exception;
        //  73     84     87     88     Ljava/lang/Exception;
        //  750    795    1245   1249   Ljava/lang/Throwable;
        //  798    817    1245   1249   Ljava/lang/Throwable;
        //  817    824    1245   1249   Ljava/lang/Throwable;
        //  825    861    1245   1249   Ljava/lang/Throwable;
        //  872    914    1245   1249   Ljava/lang/Throwable;
        //  919    1024   1245   1249   Ljava/lang/Throwable;
        //  1032   1052   1245   1249   Ljava/lang/Throwable;
        //  1052   1115   1245   1249   Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 647 out-of-bounds for length 647
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
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
    
    protected void onFocusChanged(final boolean b, final int n, final Rect rect) {
        super.onFocusChanged(b, n, rect);
        this.checkHeaderVisibility(true);
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.EditText");
        final StaticLayout hintLayout = this.hintLayout;
        if (hintLayout != null) {
            accessibilityNodeInfo.setContentDescription(hintLayout.getText());
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        if (this.hintLayout != null) {
            this.lineY = (this.getMeasuredHeight() - this.hintLayout.getHeight()) / 2.0f + this.hintLayout.getHeight() + AndroidUtilities.dp(6.0f);
        }
    }
    
    public boolean requestFocus(final int n, final Rect rect) {
        return super.requestFocus(n, rect);
    }
    
    public void setAllowDrawCursor(final boolean allowDrawCursor) {
        this.allowDrawCursor = allowDrawCursor;
        this.invalidate();
    }
    
    public void setCursorColor(final int color) {
        this.gradientDrawable.setColor(color);
        this.invalidate();
    }
    
    public void setCursorSize(final int cursorSize) {
        this.cursorSize = cursorSize;
    }
    
    public void setCursorWidth(final float cursorWidth) {
        this.cursorWidth = cursorWidth;
    }
    
    public void setErrorLineColor(final int errorLineColor) {
        this.errorLineColor = errorLineColor;
        this.errorPaint.setColor(this.errorLineColor);
        this.invalidate();
    }
    
    public void setErrorText(final CharSequence errorText) {
        if (TextUtils.equals(errorText, this.errorText)) {
            return;
        }
        this.errorText = errorText;
        this.requestLayout();
    }
    
    @Keep
    public void setHeaderAnimationProgress(final float headerAnimationProgress) {
        this.headerAnimationProgress = headerAnimationProgress;
        this.invalidate();
    }
    
    public void setHeaderHintColor(final int headerHintColor) {
        this.headerHintColor = headerHintColor;
        this.invalidate();
    }
    
    public void setHintColor(final int hintColor) {
        this.hintColor = hintColor;
        this.invalidate();
    }
    
    public void setHintText(final String s) {
        this.hintLayout = new StaticLayout((CharSequence)s, this.getPaint(), AndroidUtilities.dp(1000.0f), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }
    
    public void setHintVisible(final boolean hintVisible) {
        if (this.hintVisible == hintVisible) {
            return;
        }
        this.lastUpdateTime = System.currentTimeMillis();
        this.hintVisible = hintVisible;
        this.invalidate();
    }
    
    public void setLineColors(final int lineColor, final int activeLineColor, final int errorLineColor) {
        this.lineColor = lineColor;
        this.activeLineColor = activeLineColor;
        this.errorLineColor = errorLineColor;
        this.errorPaint.setColor(this.errorLineColor);
        this.invalidate();
    }
    
    public void setLineSpacing(final float lineSpacingExtra, final float n) {
        super.setLineSpacing(lineSpacingExtra, n);
        this.lineSpacingExtra = lineSpacingExtra;
    }
    
    public void setNextSetTextAnimated(final boolean nextSetTextAnimated) {
        this.nextSetTextAnimated = nextSetTextAnimated;
    }
    
    public void setSupportRtlHint(final boolean supportRtlHint) {
        this.supportRtlHint = supportRtlHint;
    }
    
    public void setText(final CharSequence charSequence, final TextView$BufferType textView$BufferType) {
        super.setText(charSequence, textView$BufferType);
        this.checkHeaderVisibility(this.nextSetTextAnimated);
        this.nextSetTextAnimated = false;
    }
    
    public void setTransformHintToHeader(final boolean transformHintToHeader) {
        if (this.transformHintToHeader == transformHintToHeader) {
            return;
        }
        this.transformHintToHeader = transformHintToHeader;
        final AnimatorSet headerTransformAnimation = this.headerTransformAnimation;
        if (headerTransformAnimation != null) {
            headerTransformAnimation.cancel();
            this.headerTransformAnimation = null;
        }
    }
}

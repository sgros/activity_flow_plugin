// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.view;

import java.util.Iterator;
import java.util.Map;
import android.view.View$OnAttachStateChangeListener;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.view.View$OnApplyWindowInsetsListener;
import android.graphics.drawable.Drawable;
import android.animation.ValueAnimator;
import android.os.Bundle;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.view.WindowManager;
import android.view.Display;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.view.View$AccessibilityDelegate;
import android.view.WindowInsets;
import android.os.Build$VERSION;
import android.annotation.TargetApi;
import android.annotation.SuppressLint;
import androidx.core.R$id;
import android.view.View;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.reflect.Field;

public class ViewCompat
{
    private static final int[] ACCESSIBILITY_ACTIONS_RESOURCE_IDS;
    private static boolean sAccessibilityDelegateCheckFailed;
    private static Field sAccessibilityDelegateField;
    private static AccessibilityPaneVisibilityManager sAccessibilityPaneVisibilityManager;
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;
    private static final AtomicInteger sNextGeneratedId;
    private static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap;
    
    static {
        sNextGeneratedId = new AtomicInteger(1);
        ViewCompat.sViewPropertyAnimatorMap = null;
        ViewCompat.sAccessibilityDelegateCheckFailed = false;
        ACCESSIBILITY_ACTIONS_RESOURCE_IDS = new int[] { R$id.accessibility_custom_action_0, R$id.accessibility_custom_action_1, R$id.accessibility_custom_action_2, R$id.accessibility_custom_action_3, R$id.accessibility_custom_action_4, R$id.accessibility_custom_action_5, R$id.accessibility_custom_action_6, R$id.accessibility_custom_action_7, R$id.accessibility_custom_action_8, R$id.accessibility_custom_action_9, R$id.accessibility_custom_action_10, R$id.accessibility_custom_action_11, R$id.accessibility_custom_action_12, R$id.accessibility_custom_action_13, R$id.accessibility_custom_action_14, R$id.accessibility_custom_action_15, R$id.accessibility_custom_action_16, R$id.accessibility_custom_action_17, R$id.accessibility_custom_action_18, R$id.accessibility_custom_action_19, R$id.accessibility_custom_action_20, R$id.accessibility_custom_action_21, R$id.accessibility_custom_action_22, R$id.accessibility_custom_action_23, R$id.accessibility_custom_action_24, R$id.accessibility_custom_action_25, R$id.accessibility_custom_action_26, R$id.accessibility_custom_action_27, R$id.accessibility_custom_action_28, R$id.accessibility_custom_action_29, R$id.accessibility_custom_action_30, R$id.accessibility_custom_action_31 };
        ViewCompat.sAccessibilityPaneVisibilityManager = new AccessibilityPaneVisibilityManager();
    }
    
    @SuppressLint({ "BanTargetApiAnnotation" })
    @TargetApi(28)
    private static AccessibilityViewProperty<Boolean> accessibilityHeadingProperty() {
        return (AccessibilityViewProperty<Boolean>)new AccessibilityViewProperty<Boolean>(R$id.tag_accessibility_heading, Boolean.class, 28) {
            Boolean frameworkGet(final View view) {
                return view.isAccessibilityHeading();
            }
        };
    }
    
    public static WindowInsetsCompat dispatchApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        if (Build$VERSION.SDK_INT >= 21) {
            final WindowInsets windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            final WindowInsets dispatchApplyWindowInsets = view.dispatchApplyWindowInsets(windowInsets);
            WindowInsets windowInsets2 = windowInsets;
            if (!dispatchApplyWindowInsets.equals((Object)windowInsets)) {
                windowInsets2 = new WindowInsets(dispatchApplyWindowInsets);
            }
            return WindowInsetsCompat.wrap(windowInsets2);
        }
        return windowInsetsCompat;
    }
    
    public static AccessibilityDelegateCompat getAccessibilityDelegate(final View view) {
        final View$AccessibilityDelegate accessibilityDelegateInternal = getAccessibilityDelegateInternal(view);
        if (accessibilityDelegateInternal == null) {
            return null;
        }
        if (accessibilityDelegateInternal instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
            return ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter)accessibilityDelegateInternal).mCompat;
        }
        return new AccessibilityDelegateCompat(accessibilityDelegateInternal);
    }
    
    private static View$AccessibilityDelegate getAccessibilityDelegateInternal(final View obj) {
        if (ViewCompat.sAccessibilityDelegateCheckFailed) {
            return null;
        }
        if (ViewCompat.sAccessibilityDelegateField == null) {
            try {
                (ViewCompat.sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate")).setAccessible(true);
            }
            catch (Throwable t) {
                ViewCompat.sAccessibilityDelegateCheckFailed = true;
                return null;
            }
        }
        try {
            final Object value = ViewCompat.sAccessibilityDelegateField.get(obj);
            if (value instanceof View$AccessibilityDelegate) {
                return (View$AccessibilityDelegate)value;
            }
            return null;
        }
        catch (Throwable t2) {
            ViewCompat.sAccessibilityDelegateCheckFailed = true;
            return null;
        }
    }
    
    public static int getAccessibilityLiveRegion(final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.getAccessibilityLiveRegion();
        }
        return 0;
    }
    
    public static CharSequence getAccessibilityPaneTitle(final View view) {
        return paneTitleProperty().get(view);
    }
    
    public static ColorStateList getBackgroundTintList(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getBackgroundTintList();
        }
        ColorStateList supportBackgroundTintList;
        if (view instanceof TintableBackgroundView) {
            supportBackgroundTintList = ((TintableBackgroundView)view).getSupportBackgroundTintList();
        }
        else {
            supportBackgroundTintList = null;
        }
        return supportBackgroundTintList;
    }
    
    public static PorterDuff$Mode getBackgroundTintMode(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getBackgroundTintMode();
        }
        PorterDuff$Mode supportBackgroundTintMode;
        if (view instanceof TintableBackgroundView) {
            supportBackgroundTintMode = ((TintableBackgroundView)view).getSupportBackgroundTintMode();
        }
        else {
            supportBackgroundTintMode = null;
        }
        return supportBackgroundTintMode;
    }
    
    public static Display getDisplay(final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getDisplay();
        }
        if (isAttachedToWindow(view)) {
            return ((WindowManager)view.getContext().getSystemService("window")).getDefaultDisplay();
        }
        return null;
    }
    
    public static float getElevation(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getElevation();
        }
        return 0.0f;
    }
    
    public static int getImportantForAccessibility(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getImportantForAccessibility();
        }
        return 0;
    }
    
    @SuppressLint({ "InlinedApi" })
    public static int getImportantForAutofill(final View view) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.getImportantForAutofill();
        }
        return 0;
    }
    
    public static int getLayoutDirection(final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getLayoutDirection();
        }
        return 0;
    }
    
    public static int getMinimumHeight(final View p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: bipush          16
        //     5: if_icmplt       13
        //     8: aload_0        
        //     9: invokevirtual   android/view/View.getMinimumHeight:()I
        //    12: ireturn        
        //    13: getstatic       androidx/core/view/ViewCompat.sMinHeightFieldFetched:Z
        //    16: ifne            41
        //    19: ldc             Landroid/view/View;.class
        //    21: ldc_w           "mMinHeight"
        //    24: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    27: putstatic       androidx/core/view/ViewCompat.sMinHeightField:Ljava/lang/reflect/Field;
        //    30: getstatic       androidx/core/view/ViewCompat.sMinHeightField:Ljava/lang/reflect/Field;
        //    33: iconst_1       
        //    34: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    37: iconst_1       
        //    38: putstatic       androidx/core/view/ViewCompat.sMinHeightFieldFetched:Z
        //    41: getstatic       androidx/core/view/ViewCompat.sMinHeightField:Ljava/lang/reflect/Field;
        //    44: astore_1       
        //    45: aload_1        
        //    46: ifnull          63
        //    49: aload_1        
        //    50: aload_0        
        //    51: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    54: checkcast       Ljava/lang/Integer;
        //    57: invokevirtual   java/lang/Integer.intValue:()I
        //    60: istore_2       
        //    61: iload_2        
        //    62: ireturn        
        //    63: iconst_0       
        //    64: ireturn        
        //    65: astore_1       
        //    66: goto            37
        //    69: astore_0       
        //    70: goto            63
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  19     37     65     69     Ljava/lang/NoSuchFieldException;
        //  49     61     69     73     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0063:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    public static int getMinimumWidth(final View p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: bipush          16
        //     5: if_icmplt       13
        //     8: aload_0        
        //     9: invokevirtual   android/view/View.getMinimumWidth:()I
        //    12: ireturn        
        //    13: getstatic       androidx/core/view/ViewCompat.sMinWidthFieldFetched:Z
        //    16: ifne            41
        //    19: ldc             Landroid/view/View;.class
        //    21: ldc_w           "mMinWidth"
        //    24: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    27: putstatic       androidx/core/view/ViewCompat.sMinWidthField:Ljava/lang/reflect/Field;
        //    30: getstatic       androidx/core/view/ViewCompat.sMinWidthField:Ljava/lang/reflect/Field;
        //    33: iconst_1       
        //    34: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    37: iconst_1       
        //    38: putstatic       androidx/core/view/ViewCompat.sMinWidthFieldFetched:Z
        //    41: getstatic       androidx/core/view/ViewCompat.sMinWidthField:Ljava/lang/reflect/Field;
        //    44: astore_1       
        //    45: aload_1        
        //    46: ifnull          63
        //    49: aload_1        
        //    50: aload_0        
        //    51: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    54: checkcast       Ljava/lang/Integer;
        //    57: invokevirtual   java/lang/Integer.intValue:()I
        //    60: istore_2       
        //    61: iload_2        
        //    62: ireturn        
        //    63: iconst_0       
        //    64: ireturn        
        //    65: astore_1       
        //    66: goto            37
        //    69: astore_0       
        //    70: goto            63
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  19     37     65     69     Ljava/lang/NoSuchFieldException;
        //  49     61     69     73     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0063:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    public static int getPaddingEnd(final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getPaddingEnd();
        }
        return view.getPaddingRight();
    }
    
    public static int getPaddingStart(final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getPaddingStart();
        }
        return view.getPaddingLeft();
    }
    
    public static int getWindowSystemUiVisibility(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getWindowSystemUiVisibility();
        }
        return 0;
    }
    
    public static boolean hasTransientState(final View view) {
        return Build$VERSION.SDK_INT >= 16 && view.hasTransientState();
    }
    
    public static boolean isAccessibilityHeading(final View view) {
        final Boolean b = accessibilityHeadingProperty().get(view);
        return b != null && b;
    }
    
    public static boolean isAttachedToWindow(final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.isAttachedToWindow();
        }
        return view.getWindowToken() != null;
    }
    
    public static boolean isScreenReaderFocusable(final View view) {
        final Boolean b = screenReaderFocusableProperty().get(view);
        return b != null && b;
    }
    
    @SuppressLint({ "BanTargetApiAnnotation" })
    @TargetApi(19)
    static void notifyViewAccessibilityStateChangedIfNeeded(final View view, final int contentChangeTypes) {
        if (!((AccessibilityManager)view.getContext().getSystemService("accessibility")).isEnabled()) {
            return;
        }
        final boolean b = getAccessibilityPaneTitle(view) != null;
        if (getAccessibilityLiveRegion(view) == 0 && (!b || view.getVisibility() != 0)) {
            if (view.getParent() != null) {
                try {
                    view.getParent().notifySubtreeAccessibilityStateChanged(view, view, contentChangeTypes);
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(view.getParent().getClass().getSimpleName());
                    sb.append(" does not fully implement ViewParent");
                    Log.e("ViewCompat", sb.toString(), (Throwable)abstractMethodError);
                }
            }
        }
        else {
            final AccessibilityEvent obtain = AccessibilityEvent.obtain();
            int eventType;
            if (b) {
                eventType = 32;
            }
            else {
                eventType = 2048;
            }
            obtain.setEventType(eventType);
            obtain.setContentChangeTypes(contentChangeTypes);
            view.sendAccessibilityEventUnchecked(obtain);
        }
    }
    
    public static WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        if (Build$VERSION.SDK_INT >= 21) {
            final WindowInsets windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            final WindowInsets onApplyWindowInsets = view.onApplyWindowInsets(windowInsets);
            WindowInsets windowInsets2 = windowInsets;
            if (!onApplyWindowInsets.equals((Object)windowInsets)) {
                windowInsets2 = new WindowInsets(onApplyWindowInsets);
            }
            return WindowInsetsCompat.wrap(windowInsets2);
        }
        return windowInsetsCompat;
    }
    
    public static void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        view.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat.unwrap());
    }
    
    @SuppressLint({ "BanTargetApiAnnotation" })
    @TargetApi(28)
    private static AccessibilityViewProperty<CharSequence> paneTitleProperty() {
        return (AccessibilityViewProperty<CharSequence>)new AccessibilityViewProperty<CharSequence>(R$id.tag_accessibility_pane_title, CharSequence.class, 8, 28) {
            CharSequence frameworkGet(final View view) {
                return view.getAccessibilityPaneTitle();
            }
        };
    }
    
    public static boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
        return Build$VERSION.SDK_INT >= 16 && view.performAccessibilityAction(n, bundle);
    }
    
    public static void postInvalidateOnAnimation(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation();
        }
        else {
            view.postInvalidate();
        }
    }
    
    public static void postOnAnimation(final View view, final Runnable runnable) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postOnAnimation(runnable);
        }
        else {
            view.postDelayed(runnable, ValueAnimator.getFrameDelay());
        }
    }
    
    public static void postOnAnimationDelayed(final View view, final Runnable runnable, final long n) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postOnAnimationDelayed(runnable, n);
        }
        else {
            view.postDelayed(runnable, ValueAnimator.getFrameDelay() + n);
        }
    }
    
    public static void requestApplyInsets(final View view) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 20) {
            view.requestApplyInsets();
        }
        else if (sdk_INT >= 16) {
            view.requestFitSystemWindows();
        }
    }
    
    @SuppressLint({ "BanTargetApiAnnotation" })
    @TargetApi(28)
    private static AccessibilityViewProperty<Boolean> screenReaderFocusableProperty() {
        return (AccessibilityViewProperty<Boolean>)new AccessibilityViewProperty<Boolean>(R$id.tag_screen_reader_focusable, Boolean.class, 28) {
            Boolean frameworkGet(final View view) {
                return view.isScreenReaderFocusable();
            }
        };
    }
    
    public static void setAccessibilityDelegate(final View view, final AccessibilityDelegateCompat accessibilityDelegateCompat) {
        AccessibilityDelegateCompat accessibilityDelegateCompat2 = accessibilityDelegateCompat;
        if (accessibilityDelegateCompat == null) {
            accessibilityDelegateCompat2 = accessibilityDelegateCompat;
            if (getAccessibilityDelegateInternal(view) instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
                accessibilityDelegateCompat2 = new AccessibilityDelegateCompat();
            }
        }
        View$AccessibilityDelegate bridge;
        if (accessibilityDelegateCompat2 == null) {
            bridge = null;
        }
        else {
            bridge = accessibilityDelegateCompat2.getBridge();
        }
        view.setAccessibilityDelegate(bridge);
    }
    
    public static void setBackground(final View view, final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        }
        else {
            view.setBackgroundDrawable(drawable);
        }
    }
    
    public static void setBackgroundTintList(final View view, final ColorStateList list) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setBackgroundTintList(list);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable background = view.getBackground();
                final boolean b = view.getBackgroundTintList() != null || view.getBackgroundTintMode() != null;
                if (background != null && b) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    view.setBackground(background);
                }
            }
        }
        else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintList(list);
        }
    }
    
    public static void setBackgroundTintMode(final View view, final PorterDuff$Mode porterDuff$Mode) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setBackgroundTintMode(porterDuff$Mode);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable background = view.getBackground();
                final boolean b = view.getBackgroundTintList() != null || view.getBackgroundTintMode() != null;
                if (background != null && b) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    view.setBackground(background);
                }
            }
        }
        else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintMode(porterDuff$Mode);
        }
    }
    
    public static void setElevation(final View view, final float elevation) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setElevation(elevation);
        }
    }
    
    public static void setImportantForAccessibility(final View view, final int importantForAccessibility) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 19) {
            view.setImportantForAccessibility(importantForAccessibility);
        }
        else if (sdk_INT >= 16) {
            int importantForAccessibility2;
            if ((importantForAccessibility2 = importantForAccessibility) == 4) {
                importantForAccessibility2 = 2;
            }
            view.setImportantForAccessibility(importantForAccessibility2);
        }
    }
    
    public static void setImportantForAutofill(final View view, final int importantForAutofill) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setImportantForAutofill(importantForAutofill);
        }
    }
    
    public static void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        if (Build$VERSION.SDK_INT >= 21) {
            if (onApplyWindowInsetsListener == null) {
                view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)null);
                return;
            }
            view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new View$OnApplyWindowInsetsListener() {
                public WindowInsets onApplyWindowInsets(final View view, final WindowInsets windowInsets) {
                    return (WindowInsets)WindowInsetsCompat.unwrap(onApplyWindowInsetsListener.onApplyWindowInsets(view, WindowInsetsCompat.wrap(windowInsets)));
                }
            });
        }
    }
    
    public static void stopNestedScroll(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.stopNestedScroll();
        }
        else if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild)view).stopNestedScroll();
        }
    }
    
    @SuppressLint({ "BanTargetApiAnnotation" })
    @TargetApi(19)
    static class AccessibilityPaneVisibilityManager implements ViewTreeObserver$OnGlobalLayoutListener, View$OnAttachStateChangeListener
    {
        private WeakHashMap<View, Boolean> mPanesToVisible;
        
        AccessibilityPaneVisibilityManager() {
            this.mPanesToVisible = new WeakHashMap<View, Boolean>();
        }
        
        private void checkPaneVisibility(final View key, final boolean b) {
            final boolean b2 = key.getVisibility() == 0;
            if (b != b2) {
                if (b2) {
                    ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(key, 16);
                }
                this.mPanesToVisible.put(key, b2);
            }
        }
        
        private void registerForLayoutCallback(final View view) {
            view.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
        }
        
        public void onGlobalLayout() {
            for (final Map.Entry<View, Boolean> entry : this.mPanesToVisible.entrySet()) {
                this.checkPaneVisibility(entry.getKey(), entry.getValue());
            }
        }
        
        public void onViewAttachedToWindow(final View view) {
            this.registerForLayoutCallback(view);
        }
        
        public void onViewDetachedFromWindow(final View view) {
        }
    }
    
    abstract static class AccessibilityViewProperty<T>
    {
        private final int mContentChangeType;
        private final int mFrameworkMinimumSdk;
        private final int mTagKey;
        private final Class<T> mType;
        
        AccessibilityViewProperty(final int n, final Class<T> clazz, final int n2) {
            this(n, clazz, 0, n2);
        }
        
        AccessibilityViewProperty(final int mTagKey, final Class<T> mType, final int mContentChangeType, final int mFrameworkMinimumSdk) {
            this.mTagKey = mTagKey;
            this.mType = mType;
            this.mContentChangeType = mContentChangeType;
            this.mFrameworkMinimumSdk = mFrameworkMinimumSdk;
        }
        
        private boolean extrasAvailable() {
            return Build$VERSION.SDK_INT >= 19;
        }
        
        private boolean frameworkAvailable() {
            return Build$VERSION.SDK_INT >= this.mFrameworkMinimumSdk;
        }
        
        abstract T frameworkGet(final View p0);
        
        T get(final View view) {
            if (this.frameworkAvailable()) {
                return this.frameworkGet(view);
            }
            if (this.extrasAvailable()) {
                final Object tag = view.getTag(this.mTagKey);
                if (this.mType.isInstance(tag)) {
                    return (T)tag;
                }
            }
            return null;
        }
    }
}

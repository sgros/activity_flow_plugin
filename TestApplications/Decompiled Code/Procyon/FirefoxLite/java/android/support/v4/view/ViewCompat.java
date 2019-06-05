// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view;

import android.view.ViewGroup;
import android.support.compat.R;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.view.PointerIcon;
import android.view.View$OnApplyWindowInsetsListener;
import android.graphics.drawable.Drawable;
import android.view.View$AccessibilityDelegate;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.WindowManager;
import android.view.Display;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.view.KeyEvent;
import android.view.WindowInsets;
import android.os.Build$VERSION;
import android.view.ViewParent;
import android.view.View;
import java.util.WeakHashMap;
import android.graphics.Rect;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.reflect.Field;

public class ViewCompat
{
    private static boolean sAccessibilityDelegateCheckFailed;
    private static Field sAccessibilityDelegateField;
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;
    private static final AtomicInteger sNextGeneratedId;
    private static ThreadLocal<Rect> sThreadLocalRect;
    private static WeakHashMap<View, String> sTransitionNameMap;
    private static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap;
    
    static {
        sNextGeneratedId = new AtomicInteger(1);
        ViewCompat.sViewPropertyAnimatorMap = null;
        ViewCompat.sAccessibilityDelegateCheckFailed = false;
    }
    
    public static ViewPropertyAnimatorCompat animate(final View view) {
        if (ViewCompat.sViewPropertyAnimatorMap == null) {
            ViewCompat.sViewPropertyAnimatorMap = new WeakHashMap<View, ViewPropertyAnimatorCompat>();
        }
        ViewPropertyAnimatorCompat value;
        if ((value = ViewCompat.sViewPropertyAnimatorMap.get(view)) == null) {
            value = new ViewPropertyAnimatorCompat(view);
            ViewCompat.sViewPropertyAnimatorMap.put(view, value);
        }
        return value;
    }
    
    private static void compatOffsetLeftAndRight(final View view, final int n) {
        view.offsetLeftAndRight(n);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View)parent);
            }
        }
    }
    
    private static void compatOffsetTopAndBottom(final View view, final int n) {
        view.offsetTopAndBottom(n);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View)parent);
            }
        }
    }
    
    public static WindowInsetsCompat dispatchApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        if (Build$VERSION.SDK_INT >= 21) {
            final WindowInsets windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            final WindowInsets dispatchApplyWindowInsets = view.dispatchApplyWindowInsets(windowInsets);
            WindowInsets windowInsets2;
            if (dispatchApplyWindowInsets != (windowInsets2 = windowInsets)) {
                windowInsets2 = new WindowInsets(dispatchApplyWindowInsets);
            }
            return WindowInsetsCompat.wrap(windowInsets2);
        }
        return windowInsetsCompat;
    }
    
    static boolean dispatchUnhandledKeyEventBeforeCallback(final View view, final KeyEvent keyEvent) {
        return Build$VERSION.SDK_INT < 28 && UnhandledKeyEventManager.at(view).dispatch(view, keyEvent);
    }
    
    static boolean dispatchUnhandledKeyEventBeforeHierarchy(final View view, final KeyEvent keyEvent) {
        return Build$VERSION.SDK_INT < 28 && UnhandledKeyEventManager.at(view).preDispatch(keyEvent);
    }
    
    public static int getAccessibilityLiveRegion(final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.getAccessibilityLiveRegion();
        }
        return 0;
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
    
    public static Rect getClipBounds(final View view) {
        if (Build$VERSION.SDK_INT >= 18) {
            return view.getClipBounds();
        }
        return null;
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
    
    private static Rect getEmptyTempRect() {
        if (ViewCompat.sThreadLocalRect == null) {
            ViewCompat.sThreadLocalRect = new ThreadLocal<Rect>();
        }
        Rect value;
        if ((value = ViewCompat.sThreadLocalRect.get()) == null) {
            value = new Rect();
            ViewCompat.sThreadLocalRect.set(value);
        }
        value.setEmpty();
        return value;
    }
    
    public static boolean getFitsSystemWindows(final View view) {
        return Build$VERSION.SDK_INT >= 16 && view.getFitsSystemWindows();
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
        //    13: getstatic       android/support/v4/view/ViewCompat.sMinHeightFieldFetched:Z
        //    16: ifne            40
        //    19: ldc             Landroid/view/View;.class
        //    21: ldc             "mMinHeight"
        //    23: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    26: putstatic       android/support/v4/view/ViewCompat.sMinHeightField:Ljava/lang/reflect/Field;
        //    29: getstatic       android/support/v4/view/ViewCompat.sMinHeightField:Ljava/lang/reflect/Field;
        //    32: iconst_1       
        //    33: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    36: iconst_1       
        //    37: putstatic       android/support/v4/view/ViewCompat.sMinHeightFieldFetched:Z
        //    40: getstatic       android/support/v4/view/ViewCompat.sMinHeightField:Ljava/lang/reflect/Field;
        //    43: ifnull          62
        //    46: getstatic       android/support/v4/view/ViewCompat.sMinHeightField:Ljava/lang/reflect/Field;
        //    49: aload_0        
        //    50: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    53: checkcast       Ljava/lang/Integer;
        //    56: invokevirtual   java/lang/Integer.intValue:()I
        //    59: istore_1       
        //    60: iload_1        
        //    61: ireturn        
        //    62: iconst_0       
        //    63: ireturn        
        //    64: astore_2       
        //    65: goto            36
        //    68: astore_0       
        //    69: goto            62
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  19     36     64     68     Ljava/lang/NoSuchFieldException;
        //  46     60     68     72     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0062:
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
        //    13: getstatic       android/support/v4/view/ViewCompat.sMinWidthFieldFetched:Z
        //    16: ifne            41
        //    19: ldc             Landroid/view/View;.class
        //    21: ldc_w           "mMinWidth"
        //    24: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    27: putstatic       android/support/v4/view/ViewCompat.sMinWidthField:Ljava/lang/reflect/Field;
        //    30: getstatic       android/support/v4/view/ViewCompat.sMinWidthField:Ljava/lang/reflect/Field;
        //    33: iconst_1       
        //    34: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    37: iconst_1       
        //    38: putstatic       android/support/v4/view/ViewCompat.sMinWidthFieldFetched:Z
        //    41: getstatic       android/support/v4/view/ViewCompat.sMinWidthField:Ljava/lang/reflect/Field;
        //    44: ifnull          63
        //    47: getstatic       android/support/v4/view/ViewCompat.sMinWidthField:Ljava/lang/reflect/Field;
        //    50: aload_0        
        //    51: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    54: checkcast       Ljava/lang/Integer;
        //    57: invokevirtual   java/lang/Integer.intValue:()I
        //    60: istore_1       
        //    61: iload_1        
        //    62: ireturn        
        //    63: iconst_0       
        //    64: ireturn        
        //    65: astore_2       
        //    66: goto            37
        //    69: astore_0       
        //    70: goto            63
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  19     37     65     69     Ljava/lang/NoSuchFieldException;
        //  47     61     69     73     Ljava/lang/Exception;
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
    
    public static ViewParent getParentForAccessibility(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getParentForAccessibility();
        }
        return view.getParent();
    }
    
    public static String getTransitionName(final View key) {
        if (Build$VERSION.SDK_INT >= 21) {
            return key.getTransitionName();
        }
        if (ViewCompat.sTransitionNameMap == null) {
            return null;
        }
        return ViewCompat.sTransitionNameMap.get(key);
    }
    
    public static int getWindowSystemUiVisibility(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getWindowSystemUiVisibility();
        }
        return 0;
    }
    
    public static float getZ(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getZ();
        }
        return 0.0f;
    }
    
    public static boolean hasAccessibilityDelegate(final View obj) {
        final boolean sAccessibilityDelegateCheckFailed = ViewCompat.sAccessibilityDelegateCheckFailed;
        boolean b = false;
        if (sAccessibilityDelegateCheckFailed) {
            return false;
        }
        if (ViewCompat.sAccessibilityDelegateField == null) {
            try {
                (ViewCompat.sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate")).setAccessible(true);
            }
            catch (Throwable t) {
                ViewCompat.sAccessibilityDelegateCheckFailed = true;
                return false;
            }
        }
        try {
            if (ViewCompat.sAccessibilityDelegateField.get(obj) != null) {
                b = true;
            }
            return b;
        }
        catch (Throwable t2) {
            ViewCompat.sAccessibilityDelegateCheckFailed = true;
            return false;
        }
    }
    
    public static boolean hasOnClickListeners(final View view) {
        return Build$VERSION.SDK_INT >= 15 && view.hasOnClickListeners();
    }
    
    public static boolean hasOverlappingRendering(final View view) {
        return Build$VERSION.SDK_INT < 16 || view.hasOverlappingRendering();
    }
    
    public static boolean hasTransientState(final View view) {
        return Build$VERSION.SDK_INT >= 16 && view.hasTransientState();
    }
    
    public static boolean isAttachedToWindow(final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.isAttachedToWindow();
        }
        return view.getWindowToken() != null;
    }
    
    public static boolean isLaidOut(final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.isLaidOut();
        }
        return view.getWidth() > 0 && view.getHeight() > 0;
    }
    
    public static boolean isNestedScrollingEnabled(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.isNestedScrollingEnabled();
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).isNestedScrollingEnabled();
    }
    
    public static boolean isPaddingRelative(final View view) {
        return Build$VERSION.SDK_INT >= 17 && view.isPaddingRelative();
    }
    
    public static void offsetLeftAndRight(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.offsetLeftAndRight(n);
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            final Rect emptyTempRect = getEmptyTempRect();
            boolean b = false;
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                final View view2 = (View)parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                b = (emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) ^ true);
            }
            compatOffsetLeftAndRight(view, n);
            if (b && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)parent).invalidate(emptyTempRect);
            }
        }
        else {
            compatOffsetLeftAndRight(view, n);
        }
    }
    
    public static void offsetTopAndBottom(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.offsetTopAndBottom(n);
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            final Rect emptyTempRect = getEmptyTempRect();
            boolean b = false;
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                final View view2 = (View)parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                b = (emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) ^ true);
            }
            compatOffsetTopAndBottom(view, n);
            if (b && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)parent).invalidate(emptyTempRect);
            }
        }
        else {
            compatOffsetTopAndBottom(view, n);
        }
    }
    
    public static WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        if (Build$VERSION.SDK_INT >= 21) {
            final WindowInsets windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            final WindowInsets onApplyWindowInsets = view.onApplyWindowInsets(windowInsets);
            WindowInsets windowInsets2;
            if (onApplyWindowInsets != (windowInsets2 = windowInsets)) {
                windowInsets2 = new WindowInsets(onApplyWindowInsets);
            }
            return WindowInsetsCompat.wrap(windowInsets2);
        }
        return windowInsetsCompat;
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
        if (Build$VERSION.SDK_INT >= 20) {
            view.requestApplyInsets();
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            view.requestFitSystemWindows();
        }
    }
    
    public static void setAccessibilityDelegate(final View view, final AccessibilityDelegateCompat accessibilityDelegateCompat) {
        View$AccessibilityDelegate bridge;
        if (accessibilityDelegateCompat == null) {
            bridge = null;
        }
        else {
            bridge = accessibilityDelegateCompat.getBridge();
        }
        view.setAccessibilityDelegate(bridge);
    }
    
    public static void setAccessibilityLiveRegion(final View view, final int accessibilityLiveRegion) {
        if (Build$VERSION.SDK_INT >= 19) {
            view.setAccessibilityLiveRegion(accessibilityLiveRegion);
        }
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
    
    public static void setClipBounds(final View view, final Rect clipBounds) {
        if (Build$VERSION.SDK_INT >= 18) {
            view.setClipBounds(clipBounds);
        }
    }
    
    public static void setElevation(final View view, final float elevation) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setElevation(elevation);
        }
    }
    
    @Deprecated
    public static void setFitsSystemWindows(final View view, final boolean fitsSystemWindows) {
        view.setFitsSystemWindows(fitsSystemWindows);
    }
    
    public static void setHasTransientState(final View view, final boolean hasTransientState) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.setHasTransientState(hasTransientState);
        }
    }
    
    public static void setImportantForAccessibility(final View view, final int importantForAccessibility) {
        if (Build$VERSION.SDK_INT >= 19) {
            view.setImportantForAccessibility(importantForAccessibility);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
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
    
    public static void setLayoutDirection(final View view, final int layoutDirection) {
        if (Build$VERSION.SDK_INT >= 17) {
            view.setLayoutDirection(layoutDirection);
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
    
    public static void setPaddingRelative(final View view, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 17) {
            view.setPaddingRelative(n, n2, n3, n4);
        }
        else {
            view.setPadding(n, n2, n3, n4);
        }
    }
    
    public static void setPointerIcon(final View view, final PointerIconCompat pointerIconCompat) {
        if (Build$VERSION.SDK_INT >= 24) {
            Object pointerIcon;
            if (pointerIconCompat != null) {
                pointerIcon = pointerIconCompat.getPointerIcon();
            }
            else {
                pointerIcon = null;
            }
            view.setPointerIcon((PointerIcon)pointerIcon);
        }
    }
    
    public static void setScrollIndicators(final View view, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.setScrollIndicators(n, n2);
        }
    }
    
    public static void setTransitionName(final View key, final String s) {
        if (Build$VERSION.SDK_INT >= 21) {
            key.setTransitionName(s);
        }
        else {
            if (ViewCompat.sTransitionNameMap == null) {
                ViewCompat.sTransitionNameMap = new WeakHashMap<View, String>();
            }
            ViewCompat.sTransitionNameMap.put(key, s);
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
    
    public static void stopNestedScroll(final View view, final int n) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2)view).stopNestedScroll(n);
        }
        else if (n == 0) {
            stopNestedScroll(view);
        }
    }
    
    private static void tickleInvalidationFlag(final View view) {
        final float translationY = view.getTranslationY();
        view.setTranslationY(1.0f + translationY);
        view.setTranslationY(translationY);
    }
    
    public interface OnUnhandledKeyEventListenerCompat
    {
        boolean onUnhandledKeyEvent(final View p0, final KeyEvent p1);
    }
    
    static class UnhandledKeyEventManager
    {
        private static final ArrayList<WeakReference<View>> sViewsWithListeners;
        private SparseArray<WeakReference<View>> mCapturedKeys;
        private WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent;
        private WeakHashMap<View, Boolean> mViewsContainingListeners;
        
        static {
            sViewsWithListeners = new ArrayList<WeakReference<View>>();
        }
        
        UnhandledKeyEventManager() {
            this.mViewsContainingListeners = null;
            this.mCapturedKeys = null;
            this.mLastDispatchedPreViewKeyEvent = null;
        }
        
        static UnhandledKeyEventManager at(final View view) {
            UnhandledKeyEventManager unhandledKeyEventManager;
            if ((unhandledKeyEventManager = (UnhandledKeyEventManager)view.getTag(R.id.tag_unhandled_key_event_manager)) == null) {
                unhandledKeyEventManager = new UnhandledKeyEventManager();
                view.setTag(R.id.tag_unhandled_key_event_manager, (Object)unhandledKeyEventManager);
            }
            return unhandledKeyEventManager;
        }
        
        private View dispatchInOrder(final View key, final KeyEvent keyEvent) {
            if (this.mViewsContainingListeners == null || !this.mViewsContainingListeners.containsKey(key)) {
                return null;
            }
            if (key instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup)key;
                for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                    final View dispatchInOrder = this.dispatchInOrder(viewGroup.getChildAt(i), keyEvent);
                    if (dispatchInOrder != null) {
                        return dispatchInOrder;
                    }
                }
            }
            if (this.onUnhandledKeyEvent(key, keyEvent)) {
                return key;
            }
            return null;
        }
        
        private SparseArray<WeakReference<View>> getCapturedKeys() {
            if (this.mCapturedKeys == null) {
                this.mCapturedKeys = (SparseArray<WeakReference<View>>)new SparseArray();
            }
            return this.mCapturedKeys;
        }
        
        private boolean onUnhandledKeyEvent(final View view, final KeyEvent keyEvent) {
            final ArrayList list = (ArrayList)view.getTag(R.id.tag_unhandled_key_listeners);
            if (list != null) {
                for (int i = list.size() - 1; i >= 0; --i) {
                    if (list.get(i).onUnhandledKeyEvent(view, keyEvent)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        private void recalcViewsWithUnhandled() {
            if (this.mViewsContainingListeners != null) {
                this.mViewsContainingListeners.clear();
            }
            if (UnhandledKeyEventManager.sViewsWithListeners.isEmpty()) {
                return;
            }
            synchronized (UnhandledKeyEventManager.sViewsWithListeners) {
                if (this.mViewsContainingListeners == null) {
                    this.mViewsContainingListeners = new WeakHashMap<View, Boolean>();
                }
                for (int i = UnhandledKeyEventManager.sViewsWithListeners.size() - 1; i >= 0; --i) {
                    final View key = UnhandledKeyEventManager.sViewsWithListeners.get(i).get();
                    if (key == null) {
                        UnhandledKeyEventManager.sViewsWithListeners.remove(i);
                    }
                    else {
                        this.mViewsContainingListeners.put(key, Boolean.TRUE);
                        for (ViewParent viewParent = key.getParent(); viewParent instanceof View; viewParent = viewParent.getParent()) {
                            this.mViewsContainingListeners.put((View)viewParent, Boolean.TRUE);
                        }
                    }
                }
            }
        }
        
        boolean dispatch(View dispatchInOrder, final KeyEvent keyEvent) {
            if (keyEvent.getAction() == 0) {
                this.recalcViewsWithUnhandled();
            }
            dispatchInOrder = this.dispatchInOrder(dispatchInOrder, keyEvent);
            if (keyEvent.getAction() == 0) {
                final int keyCode = keyEvent.getKeyCode();
                if (dispatchInOrder != null && !KeyEvent.isModifierKey(keyCode)) {
                    this.getCapturedKeys().put(keyCode, (Object)new WeakReference(dispatchInOrder));
                }
            }
            return dispatchInOrder != null;
        }
        
        boolean preDispatch(final KeyEvent referent) {
            if (this.mLastDispatchedPreViewKeyEvent != null && this.mLastDispatchedPreViewKeyEvent.get() == referent) {
                return false;
            }
            this.mLastDispatchedPreViewKeyEvent = new WeakReference<KeyEvent>(referent);
            final WeakReference<View> weakReference = null;
            final SparseArray<WeakReference<View>> capturedKeys = this.getCapturedKeys();
            WeakReference<View> weakReference2 = weakReference;
            if (referent.getAction() == 1) {
                final int indexOfKey = capturedKeys.indexOfKey(referent.getKeyCode());
                weakReference2 = weakReference;
                if (indexOfKey >= 0) {
                    weakReference2 = (WeakReference<View>)capturedKeys.valueAt(indexOfKey);
                    capturedKeys.removeAt(indexOfKey);
                }
            }
            WeakReference<View> weakReference3;
            if ((weakReference3 = weakReference2) == null) {
                weakReference3 = (WeakReference<View>)capturedKeys.get(referent.getKeyCode());
            }
            if (weakReference3 != null) {
                final View view = weakReference3.get();
                if (view != null && ViewCompat.isAttachedToWindow(view)) {
                    this.onUnhandledKeyEvent(view, referent);
                }
                return true;
            }
            return false;
        }
    }
}

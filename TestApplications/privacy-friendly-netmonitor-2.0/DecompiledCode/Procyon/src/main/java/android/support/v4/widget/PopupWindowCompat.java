// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import java.lang.reflect.Method;
import android.util.Log;
import java.lang.reflect.Field;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.PopupWindow;
import android.os.Build$VERSION;

public final class PopupWindowCompat
{
    static final PopupWindowCompatBaseImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 23) {
            IMPL = (PopupWindowCompatBaseImpl)new PopupWindowCompatApi23Impl();
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (PopupWindowCompatBaseImpl)new PopupWindowCompatApi21Impl();
        }
        else if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (PopupWindowCompatBaseImpl)new PopupWindowCompatApi19Impl();
        }
        else {
            IMPL = new PopupWindowCompatBaseImpl();
        }
    }
    
    private PopupWindowCompat() {
    }
    
    public static boolean getOverlapAnchor(final PopupWindow popupWindow) {
        return PopupWindowCompat.IMPL.getOverlapAnchor(popupWindow);
    }
    
    public static int getWindowLayoutType(final PopupWindow popupWindow) {
        return PopupWindowCompat.IMPL.getWindowLayoutType(popupWindow);
    }
    
    public static void setOverlapAnchor(final PopupWindow popupWindow, final boolean b) {
        PopupWindowCompat.IMPL.setOverlapAnchor(popupWindow, b);
    }
    
    public static void setWindowLayoutType(final PopupWindow popupWindow, final int n) {
        PopupWindowCompat.IMPL.setWindowLayoutType(popupWindow, n);
    }
    
    public static void showAsDropDown(final PopupWindow popupWindow, final View view, final int n, final int n2, final int n3) {
        PopupWindowCompat.IMPL.showAsDropDown(popupWindow, view, n, n2, n3);
    }
    
    @RequiresApi(19)
    static class PopupWindowCompatApi19Impl extends PopupWindowCompatBaseImpl
    {
        @Override
        public void showAsDropDown(final PopupWindow popupWindow, final View view, final int n, final int n2, final int n3) {
            popupWindow.showAsDropDown(view, n, n2, n3);
        }
    }
    
    @RequiresApi(21)
    static class PopupWindowCompatApi21Impl extends PopupWindowCompatApi19Impl
    {
        private static final String TAG = "PopupWindowCompatApi21";
        private static Field sOverlapAnchorField;
        
        static {
            try {
                (PopupWindowCompatApi21Impl.sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)ex);
            }
        }
        
        @Override
        public boolean getOverlapAnchor(final PopupWindow obj) {
            if (PopupWindowCompatApi21Impl.sOverlapAnchorField != null) {
                try {
                    return (boolean)PopupWindowCompatApi21Impl.sOverlapAnchorField.get(obj);
                }
                catch (IllegalAccessException ex) {
                    Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", (Throwable)ex);
                }
            }
            return false;
        }
        
        @Override
        public void setOverlapAnchor(final PopupWindow obj, final boolean b) {
            if (PopupWindowCompatApi21Impl.sOverlapAnchorField != null) {
                try {
                    PopupWindowCompatApi21Impl.sOverlapAnchorField.set(obj, b);
                }
                catch (IllegalAccessException ex) {
                    Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", (Throwable)ex);
                }
            }
        }
    }
    
    @RequiresApi(23)
    static class PopupWindowCompatApi23Impl extends PopupWindowCompatApi21Impl
    {
        @Override
        public boolean getOverlapAnchor(final PopupWindow popupWindow) {
            return popupWindow.getOverlapAnchor();
        }
        
        @Override
        public int getWindowLayoutType(final PopupWindow popupWindow) {
            return popupWindow.getWindowLayoutType();
        }
        
        @Override
        public void setOverlapAnchor(final PopupWindow popupWindow, final boolean overlapAnchor) {
            popupWindow.setOverlapAnchor(overlapAnchor);
        }
        
        @Override
        public void setWindowLayoutType(final PopupWindow popupWindow, final int windowLayoutType) {
            popupWindow.setWindowLayoutType(windowLayoutType);
        }
    }
    
    static class PopupWindowCompatBaseImpl
    {
        private static Method sGetWindowLayoutTypeMethod;
        private static boolean sGetWindowLayoutTypeMethodAttempted;
        private static Method sSetWindowLayoutTypeMethod;
        private static boolean sSetWindowLayoutTypeMethodAttempted;
        
        public boolean getOverlapAnchor(final PopupWindow popupWindow) {
            return false;
        }
        
        public int getWindowLayoutType(final PopupWindow p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: ifne            31
            //     6: ldc             Landroid/widget/PopupWindow;.class
            //     8: ldc             "getWindowLayoutType"
            //    10: iconst_0       
            //    11: anewarray       Ljava/lang/Class;
            //    14: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
            //    17: putstatic       android/support/v4/widget/PopupWindowCompat$PopupWindowCompatBaseImpl.sGetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
            //    20: getstatic       android/support/v4/widget/PopupWindowCompat$PopupWindowCompatBaseImpl.sGetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
            //    23: iconst_1       
            //    24: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
            //    27: iconst_1       
            //    28: putstatic       android/support/v4/widget/PopupWindowCompat$PopupWindowCompatBaseImpl.sGetWindowLayoutTypeMethodAttempted:Z
            //    31: getstatic       android/support/v4/widget/PopupWindowCompat$PopupWindowCompatBaseImpl.sGetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
            //    34: ifnull          57
            //    37: getstatic       android/support/v4/widget/PopupWindowCompat$PopupWindowCompatBaseImpl.sGetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
            //    40: aload_1        
            //    41: iconst_0       
            //    42: anewarray       Ljava/lang/Object;
            //    45: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
            //    48: checkcast       Ljava/lang/Integer;
            //    51: invokevirtual   java/lang/Integer.intValue:()I
            //    54: istore_2       
            //    55: iload_2        
            //    56: ireturn        
            //    57: iconst_0       
            //    58: ireturn        
            //    59: astore_3       
            //    60: goto            27
            //    63: astore_1       
            //    64: goto            57
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  6      27     59     63     Ljava/lang/Exception;
            //  37     55     63     67     Ljava/lang/Exception;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0057:
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        public void setOverlapAnchor(final PopupWindow popupWindow, final boolean b) {
        }
        
        public void setWindowLayoutType(final PopupWindow p0, final int p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: ifne            37
            //     6: ldc             Landroid/widget/PopupWindow;.class
            //     8: ldc             "setWindowLayoutType"
            //    10: iconst_1       
            //    11: anewarray       Ljava/lang/Class;
            //    14: dup            
            //    15: iconst_0       
            //    16: getstatic       java/lang/Integer.TYPE:Ljava/lang/Class;
            //    19: aastore        
            //    20: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
            //    23: putstatic       android/support/v4/widget/PopupWindowCompat$PopupWindowCompatBaseImpl.sSetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
            //    26: getstatic       android/support/v4/widget/PopupWindowCompat$PopupWindowCompatBaseImpl.sSetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
            //    29: iconst_1       
            //    30: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
            //    33: iconst_1       
            //    34: putstatic       android/support/v4/widget/PopupWindowCompat$PopupWindowCompatBaseImpl.sSetWindowLayoutTypeMethodAttempted:Z
            //    37: getstatic       android/support/v4/widget/PopupWindowCompat$PopupWindowCompatBaseImpl.sSetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
            //    40: ifnull          62
            //    43: getstatic       android/support/v4/widget/PopupWindowCompat$PopupWindowCompatBaseImpl.sSetWindowLayoutTypeMethod:Ljava/lang/reflect/Method;
            //    46: aload_1        
            //    47: iconst_1       
            //    48: anewarray       Ljava/lang/Object;
            //    51: dup            
            //    52: iconst_0       
            //    53: iload_2        
            //    54: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //    57: aastore        
            //    58: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
            //    61: pop            
            //    62: return         
            //    63: astore_3       
            //    64: goto            33
            //    67: astore_1       
            //    68: goto            62
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  6      33     63     67     Ljava/lang/Exception;
            //  43     62     67     71     Ljava/lang/Exception;
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        public void showAsDropDown(final PopupWindow popupWindow, final View view, final int n, final int n2, final int n3) {
            int n4 = n;
            if ((GravityCompat.getAbsoluteGravity(n3, ViewCompat.getLayoutDirection(view)) & 0x7) == 0x5) {
                n4 = n - (popupWindow.getWidth() - view.getWidth());
            }
            popupWindow.showAsDropDown(view, n4, n2);
        }
    }
}

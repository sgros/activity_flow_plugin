package android.support.v4.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.KeyEvent.DispatcherState;
import android.view.Window.Callback;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KeyEventDispatcher {
   private static boolean sActionBarFieldsFetched;
   private static Method sActionBarOnMenuKeyMethod;
   private static boolean sDialogFieldsFetched;
   private static Field sDialogKeyListenerField;

   private static boolean actionBarOnMenuKeyEventPre28(ActionBar var0, KeyEvent var1) {
      if (!sActionBarFieldsFetched) {
         try {
            sActionBarOnMenuKeyMethod = var0.getClass().getMethod("onMenuKeyEvent", KeyEvent.class);
         } catch (NoSuchMethodException var4) {
         }

         sActionBarFieldsFetched = true;
      }

      if (sActionBarOnMenuKeyMethod != null) {
         try {
            boolean var2 = (Boolean)sActionBarOnMenuKeyMethod.invoke(var0, var1);
            return var2;
         } catch (InvocationTargetException | IllegalAccessException var5) {
         }
      }

      return false;
   }

   private static boolean activitySuperDispatchKeyEventPre28(Activity var0, KeyEvent var1) {
      var0.onUserInteraction();
      Window var2 = var0.getWindow();
      if (var2.hasFeature(8)) {
         ActionBar var3 = var0.getActionBar();
         if (var1.getKeyCode() == 82 && var3 != null && actionBarOnMenuKeyEventPre28(var3, var1)) {
            return true;
         }
      }

      if (var2.superDispatchKeyEvent(var1)) {
         return true;
      } else {
         View var4 = var2.getDecorView();
         if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(var4, var1)) {
            return true;
         } else {
            DispatcherState var5;
            if (var4 != null) {
               var5 = var4.getKeyDispatcherState();
            } else {
               var5 = null;
            }

            return var1.dispatch(var0, var5, var0);
         }
      }
   }

   private static boolean dialogSuperDispatchKeyEventPre28(Dialog var0, KeyEvent var1) {
      OnKeyListener var2 = getDialogKeyListenerPre28(var0);
      if (var2 != null && var2.onKey(var0, var1.getKeyCode(), var1)) {
         return true;
      } else {
         Window var3 = var0.getWindow();
         if (var3.superDispatchKeyEvent(var1)) {
            return true;
         } else {
            View var4 = var3.getDecorView();
            if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(var4, var1)) {
               return true;
            } else {
               DispatcherState var5;
               if (var4 != null) {
                  var5 = var4.getKeyDispatcherState();
               } else {
                  var5 = null;
               }

               return var1.dispatch(var0, var5, var0);
            }
         }
      }
   }

   public static boolean dispatchBeforeHierarchy(View var0, KeyEvent var1) {
      return ViewCompat.dispatchUnhandledKeyEventBeforeHierarchy(var0, var1);
   }

   public static boolean dispatchKeyEvent(KeyEventDispatcher.Component var0, View var1, Callback var2, KeyEvent var3) {
      boolean var4 = false;
      if (var0 == null) {
         return false;
      } else if (VERSION.SDK_INT >= 28) {
         return var0.superDispatchKeyEvent(var3);
      } else if (var2 instanceof Activity) {
         return activitySuperDispatchKeyEventPre28((Activity)var2, var3);
      } else if (var2 instanceof Dialog) {
         return dialogSuperDispatchKeyEventPre28((Dialog)var2, var3);
      } else {
         if (var1 != null && ViewCompat.dispatchUnhandledKeyEventBeforeCallback(var1, var3) || var0.superDispatchKeyEvent(var3)) {
            var4 = true;
         }

         return var4;
      }
   }

   private static OnKeyListener getDialogKeyListenerPre28(Dialog var0) {
      if (!sDialogFieldsFetched) {
         try {
            sDialogKeyListenerField = Dialog.class.getDeclaredField("mOnKeyListener");
            sDialogKeyListenerField.setAccessible(true);
         } catch (NoSuchFieldException var2) {
         }

         sDialogFieldsFetched = true;
      }

      if (sDialogKeyListenerField != null) {
         try {
            OnKeyListener var4 = (OnKeyListener)sDialogKeyListenerField.get(var0);
            return var4;
         } catch (IllegalAccessException var3) {
         }
      }

      return null;
   }

   public interface Component {
      boolean superDispatchKeyEvent(KeyEvent var1);
   }
}

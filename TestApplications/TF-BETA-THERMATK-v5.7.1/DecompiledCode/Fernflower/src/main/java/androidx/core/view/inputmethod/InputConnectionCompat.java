package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

public final class InputConnectionCompat {
   public static InputConnection createWrapper(InputConnection var0, EditorInfo var1, final InputConnectionCompat.OnCommitContentListener var2) {
      if (var0 != null) {
         if (var1 != null) {
            if (var2 != null) {
               if (VERSION.SDK_INT >= 25) {
                  return new InputConnectionWrapper(var0, false) {
                     public boolean commitContent(InputContentInfo var1, int var2x, Bundle var3) {
                        return var2.onCommitContent(InputContentInfoCompat.wrap(var1), var2x, var3) ? true : super.commitContent(var1, var2x, var3);
                     }
                  };
               } else {
                  return (InputConnection)(EditorInfoCompat.getContentMimeTypes(var1).length == 0 ? var0 : new InputConnectionWrapper(var0, false) {
                     public boolean performPrivateCommand(String var1, Bundle var2x) {
                        return InputConnectionCompat.handlePerformPrivateCommand(var1, var2x, var2) ? true : super.performPrivateCommand(var1, var2x);
                     }
                  });
               }
            } else {
               throw new IllegalArgumentException("onCommitContentListener must be non-null");
            }
         } else {
            throw new IllegalArgumentException("editorInfo must be non-null");
         }
      } else {
         throw new IllegalArgumentException("inputConnection must be non-null");
      }
   }

   static boolean handlePerformPrivateCommand(String var0, Bundle var1, InputConnectionCompat.OnCommitContentListener var2) {
      if (!TextUtils.equals("android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", var0)) {
         return false;
      } else if (var1 == null) {
         return false;
      } else {
         boolean var12 = false;

         ResultReceiver var3;
         try {
            var12 = true;
            var3 = (ResultReceiver)var1.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER");
            var12 = false;
         } finally {
            if (var12) {
               var1 = null;
               if (var1 != null) {
                  var1.send(0, (Bundle)null);
               }

            }
         }

         byte var17;
         try {
            Uri var4 = (Uri)var1.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI");
            ClipDescription var5 = (ClipDescription)var1.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION");
            Uri var15 = (Uri)var1.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI");
            int var6 = var1.getInt("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS");
            Bundle var7 = (Bundle)var1.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS");
            InputContentInfoCompat var16 = new InputContentInfoCompat(var4, var5, var15);
            var17 = var2.onCommitContent(var16, var6, var7);
         } finally {
            ;
         }

         if (var3 != null) {
            var3.send(var17, (Bundle)null);
         }

         return (boolean)var17;
      }
   }

   public interface OnCommitContentListener {
      boolean onCommitContent(InputContentInfoCompat var1, int var2, Bundle var3);
   }
}

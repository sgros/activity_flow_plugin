package android.support.v4.content;

import android.support.v4.util.DebugUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Loader {
   boolean mAbandoned;
   boolean mContentChanged;
   int mId;
   Loader.OnLoadCompleteListener mListener;
   boolean mProcessingChange;
   boolean mReset;
   boolean mStarted;

   public void abandon() {
      this.mAbandoned = true;
      this.onAbandon();
   }

   public boolean cancelLoad() {
      return this.onCancelLoad();
   }

   public String dataToString(Object var1) {
      StringBuilder var2 = new StringBuilder(64);
      DebugUtils.buildShortClassTag(var1, var2);
      var2.append("}");
      return var2.toString();
   }

   @Deprecated
   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      var3.print(var1);
      var3.print("mId=");
      var3.print(this.mId);
      var3.print(" mListener=");
      var3.println(this.mListener);
      if (this.mStarted || this.mContentChanged || this.mProcessingChange) {
         var3.print(var1);
         var3.print("mStarted=");
         var3.print(this.mStarted);
         var3.print(" mContentChanged=");
         var3.print(this.mContentChanged);
         var3.print(" mProcessingChange=");
         var3.println(this.mProcessingChange);
      }

      if (this.mAbandoned || this.mReset) {
         var3.print(var1);
         var3.print("mAbandoned=");
         var3.print(this.mAbandoned);
         var3.print(" mReset=");
         var3.println(this.mReset);
      }

   }

   protected void onAbandon() {
   }

   protected boolean onCancelLoad() {
      return false;
   }

   protected void onReset() {
   }

   protected void onStartLoading() {
   }

   protected void onStopLoading() {
   }

   public void reset() {
      this.onReset();
      this.mReset = true;
      this.mStarted = false;
      this.mAbandoned = false;
      this.mContentChanged = false;
      this.mProcessingChange = false;
   }

   public final void startLoading() {
      this.mStarted = true;
      this.mReset = false;
      this.mAbandoned = false;
      this.onStartLoading();
   }

   public void stopLoading() {
      this.mStarted = false;
      this.onStopLoading();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(64);
      DebugUtils.buildShortClassTag(this, var1);
      var1.append(" id=");
      var1.append(this.mId);
      var1.append("}");
      return var1.toString();
   }

   public void unregisterListener(Loader.OnLoadCompleteListener var1) {
      if (this.mListener != null) {
         if (this.mListener == var1) {
            this.mListener = null;
         } else {
            throw new IllegalArgumentException("Attempting to unregister the wrong listener");
         }
      } else {
         throw new IllegalStateException("No listener register");
      }
   }

   public interface OnLoadCompleteListener {
   }
}

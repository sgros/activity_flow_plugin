package org.mozilla.focus.download;

import android.os.AsyncTask;

public class GetImgHeaderTask extends AsyncTask {
   public GetImgHeaderTask.Callback callback;

   protected String doInBackground(String... param1) {
      // $FF: Couldn't be decompiled
   }

   protected void onPostExecute(String var1) {
      super.onPostExecute(var1);
      this.callback.setMIMEType(var1);
   }

   public void setCallback(GetImgHeaderTask.Callback var1) {
      this.callback = var1;
   }

   public interface Callback {
      void setMIMEType(String var1);
   }
}

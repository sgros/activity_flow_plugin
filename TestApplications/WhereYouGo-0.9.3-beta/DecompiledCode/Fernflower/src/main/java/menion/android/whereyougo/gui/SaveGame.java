package menion.android.whereyougo.gui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import cz.matejcik.openwig.Engine;
import menion.android.whereyougo.openwig.WUI;
import menion.android.whereyougo.utils.Logger;

public class SaveGame extends AsyncTask {
   protected static final String TAG = "SaveGame";
   protected Context context;
   protected ProgressDialog dialog;

   public SaveGame(Context var1) {
      this.context = var1;
   }

   protected Void doInBackground(Void... var1) {
      while(true) {
         try {
            if (WUI.saving) {
               Thread.sleep(100L);
               continue;
            }
         } catch (InterruptedException var2) {
         }

         return null;
      }
   }

   protected void onPostExecute(Void var1) {
      try {
         if (this.dialog != null) {
            this.dialog.cancel();
            this.dialog = null;
         }
      } catch (Exception var2) {
         Logger.w("SaveGame", "onPostExecute(), e:" + var2.toString());
      }

   }

   protected void onPreExecute() {
      Engine.requestSync();
      this.dialog = ProgressDialog.show(this.context, (CharSequence)null, this.context.getString(2131165316));
   }
}

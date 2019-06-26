// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui;

import cz.matejcik.openwig.Engine;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.openwig.WUI;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class SaveGame extends AsyncTask<Void, Void, Void>
{
    protected static final String TAG = "SaveGame";
    protected Context context;
    protected ProgressDialog dialog;
    
    public SaveGame(final Context context) {
        this.context = context;
    }
    
    protected Void doInBackground(final Void... array) {
        try {
            while (WUI.saving) {
                Thread.sleep(100L);
            }
        }
        catch (InterruptedException ex) {}
        return null;
    }
    
    protected void onPostExecute(final Void void1) {
        try {
            if (this.dialog != null) {
                this.dialog.cancel();
                this.dialog = null;
            }
        }
        catch (Exception ex) {
            Logger.w("SaveGame", "onPostExecute(), e:" + ex.toString());
        }
    }
    
    protected void onPreExecute() {
        Engine.requestSync();
        this.dialog = ProgressDialog.show(this.context, (CharSequence)null, (CharSequence)this.context.getString(2131165316));
    }
}

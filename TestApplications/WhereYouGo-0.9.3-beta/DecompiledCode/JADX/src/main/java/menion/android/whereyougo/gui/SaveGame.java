package menion.android.whereyougo.gui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.openwig.WUI;
import menion.android.whereyougo.utils.Logger;
import p005cz.matejcik.openwig.Engine;

public class SaveGame extends AsyncTask<Void, Void, Void> {
    protected static final String TAG = "SaveGame";
    protected Context context;
    protected ProgressDialog dialog;

    public SaveGame(Context context) {
        this.context = context;
    }

    /* Access modifiers changed, original: protected */
    public void onPreExecute() {
        Engine.requestSync();
        this.dialog = ProgressDialog.show(this.context, null, this.context.getString(C0254R.string.working));
    }

    /* Access modifiers changed, original: protected|varargs */
    public Void doInBackground(Void... params) {
        while (WUI.saving) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public void onPostExecute(Void result) {
        try {
            if (this.dialog != null) {
                this.dialog.cancel();
                this.dialog = null;
            }
        } catch (Exception e) {
            Logger.m26w(TAG, "onPostExecute(), e:" + e.toString());
        }
    }
}

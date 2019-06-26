// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.content.SharedPreferences;
import android.net.Uri;
import android.content.Intent;
import org.telegram.messenger.FileLog;
import android.content.DialogInterface$OnDismissListener;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.SerializedData;
import org.telegram.messenger.Utilities;
import android.text.TextUtils;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.content.Context;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import android.os.Bundle;
import android.content.DialogInterface;
import android.app.Dialog;
import android.app.Activity;

public class ShareActivity extends Activity
{
    private Dialog visibleDialog;
    
    protected void onCreate(final Bundle bundle) {
        ApplicationLoader.postInitApplication();
        AndroidUtilities.checkDisplaySize((Context)this, this.getResources().getConfiguration());
        this.requestWindowFeature(1);
        this.setTheme(2131624216);
        super.onCreate(bundle);
        this.setContentView(new View((Context)this), new ViewGroup$LayoutParams(-1, -1));
        final Intent intent = this.getIntent();
        if (intent == null || !"android.intent.action.VIEW".equals(intent.getAction()) || intent.getData() == null) {
            this.finish();
            return;
        }
        final Uri data = intent.getData();
        final String scheme = data.getScheme();
        final String string = data.toString();
        final String queryParameter = data.getQueryParameter("hash");
        if (!"tgb".equals(scheme) || !string.toLowerCase().startsWith("tgb://share_game_score") || TextUtils.isEmpty((CharSequence)queryParameter)) {
            this.finish();
            return;
        }
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("botshare", 0);
        final StringBuilder sb = new StringBuilder();
        sb.append(queryParameter);
        sb.append("_m");
        final String string2 = sharedPreferences.getString(sb.toString(), (String)null);
        if (TextUtils.isEmpty((CharSequence)string2)) {
            this.finish();
            return;
        }
        final SerializedData serializedData = new SerializedData(Utilities.hexToBytes(string2));
        final TLRPC.Message tLdeserialize = TLRPC.Message.TLdeserialize(serializedData, serializedData.readInt32(false), false);
        tLdeserialize.readAttachPath(serializedData, 0);
        serializedData.cleanup();
        if (tLdeserialize == null) {
            this.finish();
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(queryParameter);
        sb2.append("_link");
        final String string3 = sharedPreferences.getString(sb2.toString(), (String)null);
        final MessageObject messageObject = new MessageObject(UserConfig.selectedAccount, tLdeserialize, false);
        messageObject.messageOwner.with_my_score = true;
        try {
            (this.visibleDialog = ShareAlert.createShareAlert((Context)this, messageObject, null, false, string3, false)).setCanceledOnTouchOutside(true);
            this.visibleDialog.setOnDismissListener((DialogInterface$OnDismissListener)new _$$Lambda$ShareActivity$8CDJt1az5uGqAsSjal6N7RJDepQ(this));
            this.visibleDialog.show();
        }
        catch (Exception ex) {
            FileLog.e(ex);
            this.finish();
        }
    }
    
    public void onPause() {
        super.onPause();
        try {
            if (this.visibleDialog != null && this.visibleDialog.isShowing()) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
}

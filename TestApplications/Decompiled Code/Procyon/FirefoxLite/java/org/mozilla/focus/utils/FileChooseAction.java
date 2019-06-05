// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.content.Context;
import android.text.TextUtils;
import android.content.Intent;
import android.webkit.WebChromeClient$FileChooserParams;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.webkit.ValueCallback;

public class FileChooseAction
{
    private ValueCallback<Uri[]> callback;
    private Fragment hostFragment;
    private WebChromeClient$FileChooserParams params;
    private Uri[] uris;
    
    public FileChooseAction(final Fragment hostFragment, final ValueCallback<Uri[]> callback, final WebChromeClient$FileChooserParams params) {
        this.hostFragment = hostFragment;
        this.callback = callback;
        this.params = params;
    }
    
    private Intent createChooserIntent(final WebChromeClient$FileChooserParams webChromeClient$FileChooserParams) {
        final String[] acceptTypes = webChromeClient$FileChooserParams.getAcceptTypes();
        CharSequence charSequence;
        if (TextUtils.isEmpty(charSequence = webChromeClient$FileChooserParams.getTitle())) {
            charSequence = this.hostFragment.getString(2131755196);
        }
        return FilePickerUtil.getFilePickerIntent((Context)this.hostFragment.getActivity(), charSequence, acceptTypes);
    }
    
    public void cancel() {
        this.callback.onReceiveValue((Object)null);
    }
    
    public boolean onFileChose(final int n, final Intent intent) {
        if (this.callback == null) {
            return true;
        }
        if (n == -1 && intent != null) {
            try {
                final Uri data = intent.getData();
                Uri[] uris;
                if (data == null) {
                    uris = null;
                }
                else {
                    uris = new Uri[] { data };
                }
                this.uris = uris;
                this.callback.onReceiveValue((Object)this.uris);
            }
            catch (Exception ex) {
                this.callback.onReceiveValue((Object)null);
                ex.printStackTrace();
            }
            this.callback = null;
            return true;
        }
        this.callback.onReceiveValue((Object)null);
        this.callback = null;
        return true;
    }
    
    public void startChooserActivity() {
        this.hostFragment.startActivityForResult(this.createChooserIntent(this.params), 103);
    }
}

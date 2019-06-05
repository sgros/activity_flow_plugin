package org.mozilla.focus.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.p001v4.app.Fragment;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import org.mozilla.rocket.C0769R;

public class FileChooseAction {
    private ValueCallback<Uri[]> callback;
    private Fragment hostFragment;
    private FileChooserParams params;
    private Uri[] uris;

    public FileChooseAction(Fragment fragment, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        this.hostFragment = fragment;
        this.callback = valueCallback;
        this.params = fileChooserParams;
    }

    public void cancel() {
        this.callback.onReceiveValue(null);
    }

    public boolean onFileChose(int i, Intent intent) {
        if (this.callback == null) {
            return true;
        }
        if (i != -1 || intent == null) {
            this.callback.onReceiveValue(null);
            this.callback = null;
            return true;
        }
        try {
            this.uris = intent.getData() == null ? null : new Uri[]{intent.getData()};
            this.callback.onReceiveValue(this.uris);
        } catch (Exception e) {
            this.callback.onReceiveValue(null);
            e.printStackTrace();
        }
        this.callback = null;
        return true;
    }

    public void startChooserActivity() {
        this.hostFragment.startActivityForResult(createChooserIntent(this.params), 103);
    }

    private Intent createChooserIntent(FileChooserParams fileChooserParams) {
        String[] acceptTypes = fileChooserParams.getAcceptTypes();
        CharSequence title = fileChooserParams.getTitle();
        if (TextUtils.isEmpty(title)) {
            title = this.hostFragment.getString(C0769R.string.file_picker_title);
        }
        return FilePickerUtil.getFilePickerIntent(this.hostFragment.getActivity(), title, acceptTypes);
    }
}

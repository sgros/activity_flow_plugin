package org.mozilla.focus.widget;

import android.content.Context;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import org.mozilla.focus.utils.NoRemovableStorageException;
import org.mozilla.focus.utils.StorageUtils;
import org.mozilla.rocket.C0769R;
import org.mozilla.threadutils.ThreadUtils;

public class DataSavingPathPreference extends ListPreference {
    private boolean hasRemovableStorage;

    /* renamed from: org.mozilla.focus.widget.DataSavingPathPreference$1 */
    class C05691 implements Runnable {
        C05691() {
        }

        public void run() {
            DataSavingPathPreference.this.pingRemovableStorage();
        }
    }

    /* renamed from: org.mozilla.focus.widget.DataSavingPathPreference$2 */
    class C05702 implements Runnable {
        C05702() {
        }

        public void run() {
            DataSavingPathPreference.this.notifyChanged();
        }
    }

    public DataSavingPathPreference(Context context) {
        this(context, null);
    }

    public DataSavingPathPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.hasRemovableStorage = false;
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToActivity() {
        super.onAttachedToActivity();
        buildList();
        ThreadUtils.postToBackgroundThread(new C05691());
    }

    /* Access modifiers changed, original: protected */
    public void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
        if (z) {
            persistString(getValue());
        }
    }

    public CharSequence getSummary() {
        if (!this.hasRemovableStorage) {
            return getContext().getResources().getString(C0769R.string.setting_dialog_internal_storage);
        }
        if (!TextUtils.isEmpty(getEntry())) {
            return getEntry();
        }
        String[] stringArray = getContext().getResources().getStringArray(C0769R.array.data_saving_path_entries);
        setValueIndex(0);
        return stringArray[0];
    }

    private void buildList() {
        String[] stringArray = getContext().getResources().getStringArray(C0769R.array.data_saving_path_entries);
        String[] stringArray2 = getContext().getResources().getStringArray(C0769R.array.data_saving_path_values);
        setEntries(stringArray);
        setEntryValues(stringArray2);
    }

    private void pingRemovableStorage() {
        try {
            StorageUtils.getAppMediaDirOnRemovableStorage(getContext());
            this.hasRemovableStorage = true;
        } catch (NoRemovableStorageException unused) {
            this.hasRemovableStorage = false;
        }
        super.setEnabled(this.hasRemovableStorage);
        ThreadUtils.postToMainThread(new C05702());
    }
}

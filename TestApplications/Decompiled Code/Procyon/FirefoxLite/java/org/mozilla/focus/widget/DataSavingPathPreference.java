// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.text.TextUtils;
import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.focus.utils.NoRemovableStorageException;
import org.mozilla.focus.utils.StorageUtils;
import android.util.AttributeSet;
import android.content.Context;
import android.preference.ListPreference;

public class DataSavingPathPreference extends ListPreference
{
    private boolean hasRemovableStorage;
    
    public DataSavingPathPreference(final Context context) {
        this(context, null);
    }
    
    public DataSavingPathPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.hasRemovableStorage = false;
    }
    
    static /* synthetic */ void access$100(final DataSavingPathPreference dataSavingPathPreference) {
        dataSavingPathPreference.notifyChanged();
    }
    
    private void buildList() {
        final String[] stringArray = this.getContext().getResources().getStringArray(2130903042);
        final String[] stringArray2 = this.getContext().getResources().getStringArray(2130903043);
        this.setEntries((CharSequence[])stringArray);
        this.setEntryValues((CharSequence[])stringArray2);
    }
    
    private void pingRemovableStorage() {
        try {
            StorageUtils.getAppMediaDirOnRemovableStorage(this.getContext());
            this.hasRemovableStorage = true;
        }
        catch (NoRemovableStorageException ex) {
            this.hasRemovableStorage = false;
        }
        super.setEnabled(this.hasRemovableStorage);
        ThreadUtils.postToMainThread(new Runnable() {
            @Override
            public void run() {
                DataSavingPathPreference.access$100(DataSavingPathPreference.this);
            }
        });
    }
    
    public CharSequence getSummary() {
        if (!this.hasRemovableStorage) {
            return this.getContext().getResources().getString(2131755404);
        }
        if (TextUtils.isEmpty(this.getEntry())) {
            final String[] stringArray = this.getContext().getResources().getStringArray(2130903042);
            this.setValueIndex(0);
            return stringArray[0];
        }
        return this.getEntry();
    }
    
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();
        this.buildList();
        ThreadUtils.postToBackgroundThread(new Runnable() {
            @Override
            public void run() {
                DataSavingPathPreference.this.pingRemovableStorage();
            }
        });
    }
    
    protected void onDialogClosed(final boolean b) {
        super.onDialogClosed(b);
        if (b) {
            this.persistString(this.getValue());
        }
    }
}

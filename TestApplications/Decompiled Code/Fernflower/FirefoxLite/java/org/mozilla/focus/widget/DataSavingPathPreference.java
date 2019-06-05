package org.mozilla.focus.widget;

import android.content.Context;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import org.mozilla.focus.utils.NoRemovableStorageException;
import org.mozilla.focus.utils.StorageUtils;
import org.mozilla.threadutils.ThreadUtils;

public class DataSavingPathPreference extends ListPreference {
   private boolean hasRemovableStorage;

   public DataSavingPathPreference(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public DataSavingPathPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.hasRemovableStorage = false;
   }

   private void buildList() {
      String[] var1 = this.getContext().getResources().getStringArray(2130903042);
      String[] var2 = this.getContext().getResources().getStringArray(2130903043);
      this.setEntries(var1);
      this.setEntryValues(var2);
   }

   private void pingRemovableStorage() {
      try {
         StorageUtils.getAppMediaDirOnRemovableStorage(this.getContext());
         this.hasRemovableStorage = true;
      } catch (NoRemovableStorageException var2) {
         this.hasRemovableStorage = false;
      }

      super.setEnabled(this.hasRemovableStorage);
      ThreadUtils.postToMainThread(new Runnable() {
         public void run() {
            DataSavingPathPreference.this.notifyChanged();
         }
      });
   }

   public CharSequence getSummary() {
      if (!this.hasRemovableStorage) {
         return this.getContext().getResources().getString(2131755404);
      } else if (TextUtils.isEmpty(this.getEntry())) {
         String[] var1 = this.getContext().getResources().getStringArray(2130903042);
         this.setValueIndex(0);
         return var1[0];
      } else {
         return this.getEntry();
      }
   }

   protected void onAttachedToActivity() {
      super.onAttachedToActivity();
      this.buildList();
      ThreadUtils.postToBackgroundThread(new Runnable() {
         public void run() {
            DataSavingPathPreference.this.pingRemovableStorage();
         }
      });
   }

   protected void onDialogClosed(boolean var1) {
      super.onDialogClosed(var1);
      if (var1) {
         this.persistString(this.getValue());
      }

   }
}

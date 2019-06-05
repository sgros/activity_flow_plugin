package org.mozilla.focus.search;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import org.mozilla.focus.utils.Settings;

public class SearchEnginePreference extends DialogPreference {
   public SearchEnginePreference(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public SearchEnginePreference(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   private void persistSearchEngine(SearchEngine var1) {
      this.setSummary(var1.getName());
      Settings.getInstance(this.getContext()).setDefaultSearchEngine(var1);
   }

   protected void onAttachedToActivity() {
      this.setSummary(SearchEngineManager.getInstance().getDefaultSearchEngine(this.getContext()).getName());
      super.onAttachedToActivity();
   }

   protected void onPrepareDialogBuilder(Builder var1) {
      final SearchEngineAdapter var2 = new SearchEngineAdapter(this.getContext());
      var1.setTitle(2131755346);
      var1.setAdapter(var2, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2x) {
            SearchEnginePreference.this.persistSearchEngine(var2.getItem(var2x));
            var1.dismiss();
         }
      });
      var1.setPositiveButton((CharSequence)null, (OnClickListener)null);
      var1.setNegativeButton((CharSequence)null, this);
   }
}

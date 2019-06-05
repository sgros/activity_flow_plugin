// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.search;

import android.widget.ListAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import org.mozilla.focus.utils.Settings;
import android.util.AttributeSet;
import android.content.Context;
import android.preference.DialogPreference;

public class SearchEnginePreference extends DialogPreference
{
    public SearchEnginePreference(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public SearchEnginePreference(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    private void persistSearchEngine(final SearchEngine defaultSearchEngine) {
        this.setSummary((CharSequence)defaultSearchEngine.getName());
        Settings.getInstance(this.getContext()).setDefaultSearchEngine(defaultSearchEngine);
    }
    
    protected void onAttachedToActivity() {
        this.setSummary((CharSequence)SearchEngineManager.getInstance().getDefaultSearchEngine(this.getContext()).getName());
        super.onAttachedToActivity();
    }
    
    protected void onPrepareDialogBuilder(final AlertDialog$Builder alertDialog$Builder) {
        final SearchEngineAdapter searchEngineAdapter = new SearchEngineAdapter(this.getContext());
        alertDialog$Builder.setTitle(2131755346);
        alertDialog$Builder.setAdapter((ListAdapter)searchEngineAdapter, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                SearchEnginePreference.this.persistSearchEngine(searchEngineAdapter.getItem(n));
                dialogInterface.dismiss();
            }
        });
        alertDialog$Builder.setPositiveButton((CharSequence)null, (DialogInterface$OnClickListener)null);
        alertDialog$Builder.setNegativeButton((CharSequence)null, (DialogInterface$OnClickListener)this);
    }
}

package org.mozilla.focus.search;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import org.mozilla.focus.utils.Settings;
import org.mozilla.rocket.C0769R;

public class SearchEnginePreference extends DialogPreference {
    public SearchEnginePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SearchEnginePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToActivity() {
        setSummary(SearchEngineManager.getInstance().getDefaultSearchEngine(getContext()).getName());
        super.onAttachedToActivity();
    }

    /* Access modifiers changed, original: protected */
    public void onPrepareDialogBuilder(Builder builder) {
        final SearchEngineAdapter searchEngineAdapter = new SearchEngineAdapter(getContext());
        builder.setTitle(C0769R.string.preference_dialog_title_search_engine);
        builder.setAdapter(searchEngineAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SearchEnginePreference.this.persistSearchEngine(searchEngineAdapter.getItem(i));
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, this);
    }

    private void persistSearchEngine(SearchEngine searchEngine) {
        setSummary(searchEngine.getName());
        Settings.getInstance(getContext()).setDefaultSearchEngine(searchEngine);
    }
}

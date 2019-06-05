// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.search;

import android.widget.ImageView;
import android.graphics.Typeface;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.content.Context;
import java.util.List;
import android.widget.BaseAdapter;

public class SearchEngineAdapter extends BaseAdapter
{
    private SearchEngine defaultSearchEngine;
    private List<SearchEngine> searchEngines;
    
    public SearchEngineAdapter(final Context context) {
        final SearchEngineManager instance = SearchEngineManager.getInstance();
        this.searchEngines = instance.getSearchEngines();
        this.defaultSearchEngine = instance.getDefaultSearchEngine(context);
    }
    
    public int getCount() {
        return this.searchEngines.size();
    }
    
    public SearchEngine getItem(final int n) {
        return this.searchEngines.get(n);
    }
    
    public long getItemId(final int n) {
        return n;
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        final SearchEngine item = this.getItem(n);
        final boolean equals = item.getName().equals(this.defaultSearchEngine.getName());
        View inflate = view;
        if (view == null) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(2131492983, viewGroup, false);
        }
        final TextView textView = (TextView)inflate.findViewById(2131296697);
        textView.setText((CharSequence)item.getName());
        Typeface typeface;
        if (equals) {
            typeface = Typeface.DEFAULT_BOLD;
        }
        else {
            typeface = Typeface.DEFAULT;
        }
        textView.setTypeface(typeface);
        textView.setTextColor(-16777216);
        ((ImageView)inflate.findViewById(2131296473)).setImageBitmap(item.getIcon());
        return inflate;
    }
}

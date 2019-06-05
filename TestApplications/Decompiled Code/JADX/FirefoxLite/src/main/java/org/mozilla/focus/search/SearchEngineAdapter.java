package org.mozilla.focus.search;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import org.mozilla.rocket.C0769R;

public class SearchEngineAdapter extends BaseAdapter {
    private SearchEngine defaultSearchEngine;
    private List<SearchEngine> searchEngines;

    public long getItemId(int i) {
        return (long) i;
    }

    public SearchEngineAdapter(Context context) {
        SearchEngineManager instance = SearchEngineManager.getInstance();
        this.searchEngines = instance.getSearchEngines();
        this.defaultSearchEngine = instance.getDefaultSearchEngine(context);
    }

    public int getCount() {
        return this.searchEngines.size();
    }

    public SearchEngine getItem(int i) {
        return (SearchEngine) this.searchEngines.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        SearchEngine item = getItem(i);
        boolean equals = item.getName().equals(this.defaultSearchEngine.getName());
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.item_search_engine, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(2131296697);
        textView.setText(item.getName());
        textView.setTypeface(equals ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        textView.setTextColor(-16777216);
        ((ImageView) view.findViewById(2131296473)).setImageBitmap(item.getIcon());
        return view;
    }
}

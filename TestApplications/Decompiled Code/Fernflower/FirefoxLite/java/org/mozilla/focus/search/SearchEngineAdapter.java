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

public class SearchEngineAdapter extends BaseAdapter {
   private SearchEngine defaultSearchEngine;
   private List searchEngines;

   public SearchEngineAdapter(Context var1) {
      SearchEngineManager var2 = SearchEngineManager.getInstance();
      this.searchEngines = var2.getSearchEngines();
      this.defaultSearchEngine = var2.getDefaultSearchEngine(var1);
   }

   public int getCount() {
      return this.searchEngines.size();
   }

   public SearchEngine getItem(int var1) {
      return (SearchEngine)this.searchEngines.get(var1);
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      SearchEngine var4 = this.getItem(var1);
      boolean var5 = var4.getName().equals(this.defaultSearchEngine.getName());
      View var6 = var2;
      if (var2 == null) {
         var6 = LayoutInflater.from(var3.getContext()).inflate(2131492983, var3, false);
      }

      TextView var8 = (TextView)var6.findViewById(2131296697);
      var8.setText(var4.getName());
      Typeface var7;
      if (var5) {
         var7 = Typeface.DEFAULT_BOLD;
      } else {
         var7 = Typeface.DEFAULT;
      }

      var8.setTypeface(var7);
      var8.setTextColor(-16777216);
      ((ImageView)var6.findViewById(2131296473)).setImageBitmap(var4.getIcon());
      return var6;
   }
}

package org.mozilla.focus.urlinput;

import android.os.AsyncTask;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.mozilla.focus.search.SearchEngine;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.httptask.SimpleLoadUrlTask;

public class UrlInputPresenter implements UrlInputContract.Presenter {
   private AsyncTask queryTask;
   private final SearchEngine searchEngine;
   private final String userAgent;
   private UrlInputContract.View view;

   UrlInputPresenter(SearchEngine var1, String var2) {
      this.searchEngine = var1;
      this.userAgent = var2;
   }

   public void onInput(CharSequence var1, boolean var2) {
      if (var2 && this.queryTask != null) {
         this.queryTask.cancel(true);
      }

      if (this.view != null) {
         if (var1.length() == 0) {
            this.view.setSuggestions((List)null);
            this.view.setQuickSearchVisible(false);
         } else {
            this.view.setQuickSearchVisible(true);
            if (!SupportUtils.isUrl(var1.toString())) {
               if (this.queryTask != null) {
                  this.queryTask.cancel(true);
                  this.queryTask = null;
               }

               this.queryTask = (new UrlInputPresenter.QueryTask(this.view)).execute(new String[]{this.searchEngine.buildSearchSuggestionUrl(var1.toString()), this.userAgent, Integer.toString(10000)});
            }
         }
      }
   }

   public void setView(UrlInputContract.View var1) {
      this.view = var1;
      if (var1 == null && this.queryTask != null) {
         this.queryTask.cancel(false);
      }

   }

   private static class QueryTask extends SimpleLoadUrlTask {
      private WeakReference viewWeakReference;

      QueryTask(UrlInputContract.View var1) {
         this.viewWeakReference = new WeakReference(var1);
      }

      protected void onPostExecute(String var1) {
         if (!TextUtils.isEmpty(var1)) {
            Object var2 = null;

            List var21;
            label170: {
               label176: {
                  ArrayList var19;
                  JSONArray var4;
                  int var5;
                  label168: {
                     try {
                        JSONArray var3 = new JSONArray(var1);
                        var4 = var3.getJSONArray(1);
                        var5 = var4.length();
                        var19 = new ArrayList(var5);
                        break label168;
                     } catch (JSONException var17) {
                     } finally {
                        ;
                     }

                     var1 = null;
                     break label176;
                  }

                  byte var6 = 0;

                  while(true) {
                     Object var22 = var19;

                     try {
                        if (var6 >= Math.min(var5, 5)) {
                           break label170;
                        }

                        var19.add(var4.getString(var6));
                     } catch (JSONException var15) {
                        var22 = var15;
                        break;
                     } finally {
                        if (var19 == null) {
                           Collections.emptyList();
                        }

                        throw var22;
                     }

                  }
               }

               var21 = var1;
               if (var1 == null) {
                  var21 = Collections.emptyList();
               }
            }

            UrlInputContract.View var20 = (UrlInputContract.View)this.viewWeakReference.get();
            if (var20 != null) {
               var20.setSuggestions(var21);
            }

         }
      }
   }
}

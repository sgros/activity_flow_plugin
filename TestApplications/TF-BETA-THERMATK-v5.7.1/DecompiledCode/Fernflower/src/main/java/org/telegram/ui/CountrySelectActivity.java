package org.telegram.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.LetterSectionCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class CountrySelectActivity extends BaseFragment {
   private CountrySelectActivity.CountrySelectActivityDelegate delegate;
   private EmptyTextProgressView emptyView;
   private RecyclerListView listView;
   private CountrySelectActivity.CountryAdapter listViewAdapter;
   private boolean needPhoneCode;
   private CountrySelectActivity.CountrySearchAdapter searchListViewAdapter;
   private boolean searchWas;
   private boolean searching;

   public CountrySelectActivity(boolean var1) {
      this.needPhoneCode = var1;
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      ActionBar var2 = super.actionBar;
      byte var3 = 1;
      var2.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("ChooseCountry", 2131559086));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               CountrySelectActivity.this.finishFragment();
            }

         }
      });
      super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public void onSearchCollapse() {
            CountrySelectActivity.this.searchListViewAdapter.search((String)null);
            CountrySelectActivity.this.searching = false;
            CountrySelectActivity.this.searchWas = false;
            CountrySelectActivity.this.listView.setAdapter(CountrySelectActivity.this.listViewAdapter);
            CountrySelectActivity.this.listView.setFastScrollVisible(true);
            CountrySelectActivity.this.emptyView.setText(LocaleController.getString("ChooseCountry", 2131559086));
         }

         public void onSearchExpand() {
            CountrySelectActivity.this.searching = true;
         }

         public void onTextChanged(EditText var1) {
            String var2 = var1.getText().toString();
            CountrySelectActivity.this.searchListViewAdapter.search(var2);
            if (var2.length() != 0) {
               CountrySelectActivity.this.searchWas = true;
               if (CountrySelectActivity.this.listView != null) {
                  CountrySelectActivity.this.listView.setAdapter(CountrySelectActivity.this.searchListViewAdapter);
                  CountrySelectActivity.this.listView.setFastScrollVisible(false);
               }

               CountrySelectActivity.this.emptyView;
            }

         }
      }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      this.searching = false;
      this.searchWas = false;
      this.listViewAdapter = new CountrySelectActivity.CountryAdapter(var1);
      this.searchListViewAdapter = new CountrySelectActivity.CountrySearchAdapter(var1, this.listViewAdapter.getCountries());
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var5 = (FrameLayout)super.fragmentView;
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.showTextView();
      this.emptyView.setShowAtCenter(true);
      this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
      var5.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setSectionsType(1);
      this.listView.setEmptyView(this.emptyView);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setFastScrollEnabled();
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      this.listView.setAdapter(this.listViewAdapter);
      RecyclerListView var4 = this.listView;
      if (!LocaleController.isRTL) {
         var3 = 2;
      }

      var4.setVerticalScrollbarPosition(var3);
      var5.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$CountrySelectActivity$JqHWqr_68DyDf_WzOb4K8Bisl88(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1 && CountrySelectActivity.this.searching && CountrySelectActivity.this.searchWas) {
               AndroidUtilities.hideKeyboard(CountrySelectActivity.this.getParentActivity().getCurrentFocus());
            }

         }
      });
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var3 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCH, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearch");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearchPlaceholder");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var10 = this.listView;
      Paint var11 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, new ThemeDescription(var10, 0, new Class[]{View.class}, var11, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollActive"), new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollInactive"), new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollText"), new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_SECTIONS, new Class[]{LetterSectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$CountrySelectActivity(View var1, int var2) {
      CountrySelectActivity.Country var6;
      if (this.searching && this.searchWas) {
         var6 = this.searchListViewAdapter.getItem(var2);
      } else {
         int var3 = this.listViewAdapter.getSectionForPosition(var2);
         int var4 = this.listViewAdapter.getPositionInSectionForPosition(var2);
         if (var4 < 0 || var3 < 0) {
            return;
         }

         var6 = this.listViewAdapter.getItem(var3, var4);
      }

      if (var2 >= 0) {
         this.finishFragment();
         if (var6 != null) {
            CountrySelectActivity.CountrySelectActivityDelegate var5 = this.delegate;
            if (var5 != null) {
               var5.didSelectCountry(var6.name, var6.shortname);
            }
         }

      }
   }

   public boolean onFragmentCreate() {
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
   }

   public void onResume() {
      super.onResume();
      CountrySelectActivity.CountryAdapter var1 = this.listViewAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   public void setCountrySelectActivityDelegate(CountrySelectActivity.CountrySelectActivityDelegate var1) {
      this.delegate = var1;
   }

   public static class Country {
      public String code;
      public String name;
      public String shortname;
   }

   public class CountryAdapter extends RecyclerListView.SectionsAdapter {
      private HashMap countries = new HashMap();
      private Context mContext;
      private ArrayList sortedCountries = new ArrayList();

      public CountryAdapter(Context var2) {
         this.mContext = var2;

         label57: {
            Exception var10000;
            label56: {
               InputStream var3;
               BufferedReader var4;
               boolean var10001;
               try {
                  var3 = ApplicationLoader.applicationContext.getResources().getAssets().open("countries.txt");
                  InputStreamReader var13 = new InputStreamReader(var3);
                  var4 = new BufferedReader(var13);
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label56;
               }

               while(true) {
                  String var14;
                  try {
                     var14 = var4.readLine();
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break;
                  }

                  if (var14 == null) {
                     try {
                        var4.close();
                        var3.close();
                        break label57;
                     } catch (Exception var7) {
                        var10000 = var7;
                        var10001 = false;
                        break;
                     }
                  }

                  CountrySelectActivity.Country var5;
                  String var6;
                  ArrayList var16;
                  try {
                     String[] var15 = var14.split(";");
                     var5 = new CountrySelectActivity.Country();
                     var5.name = var15[2];
                     var5.code = var15[0];
                     var5.shortname = var15[1];
                     var6 = var5.name.substring(0, 1).toUpperCase();
                     var16 = (ArrayList)this.countries.get(var6);
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break;
                  }

                  ArrayList var17 = var16;
                  if (var16 == null) {
                     try {
                        var17 = new ArrayList();
                        this.countries.put(var6, var17);
                        this.sortedCountries.add(var6);
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break;
                     }
                  }

                  try {
                     var17.add(var5);
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break;
                  }
               }
            }

            Exception var18 = var10000;
            FileLog.e((Throwable)var18);
         }

         Collections.sort(this.sortedCountries, _$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
         Iterator var19 = this.countries.values().iterator();

         while(var19.hasNext()) {
            Collections.sort((ArrayList)var19.next(), _$$Lambda$CountrySelectActivity$CountryAdapter$GbbT4_eUqPD5K07oYJV3z7sG8q4.INSTANCE);
         }

      }

      // $FF: synthetic method
      static int lambda$new$0(CountrySelectActivity.Country var0, CountrySelectActivity.Country var1) {
         return var0.name.compareTo(var1.name);
      }

      public int getCountForSection(int var1) {
         int var2 = ((ArrayList)this.countries.get(this.sortedCountries.get(var1))).size();
         int var3 = var2;
         if (var1 != this.sortedCountries.size() - 1) {
            var3 = var2 + 1;
         }

         return var3;
      }

      public HashMap getCountries() {
         return this.countries;
      }

      public CountrySelectActivity.Country getItem(int var1, int var2) {
         if (var1 >= 0 && var1 < this.sortedCountries.size()) {
            ArrayList var3 = (ArrayList)this.countries.get(this.sortedCountries.get(var1));
            if (var2 >= 0 && var2 < var3.size()) {
               return (CountrySelectActivity.Country)var3.get(var2);
            }
         }

         return null;
      }

      public int getItemViewType(int var1, int var2) {
         byte var3;
         if (var2 < ((ArrayList)this.countries.get(this.sortedCountries.get(var1))).size()) {
            var3 = 0;
         } else {
            var3 = 1;
         }

         return var3;
      }

      public String getLetter(int var1) {
         int var2 = this.getSectionForPosition(var1);
         var1 = var2;
         if (var2 == -1) {
            var1 = this.sortedCountries.size() - 1;
         }

         return (String)this.sortedCountries.get(var1);
      }

      public int getPositionForScrollProgress(float var1) {
         return (int)((float)this.getItemCount() * var1);
      }

      public int getSectionCount() {
         return this.sortedCountries.size();
      }

      public View getSectionHeaderView(int var1, View var2) {
         Object var3 = var2;
         if (var2 == null) {
            var3 = new LetterSectionCell(this.mContext);
            ((LetterSectionCell)var3).setCellHeight(AndroidUtilities.dp(48.0F));
         }

         ((LetterSectionCell)var3).setLetter(((String)this.sortedCountries.get(var1)).toUpperCase());
         return (View)var3;
      }

      public boolean isEnabled(int var1, int var2) {
         boolean var3;
         if (var2 < ((ArrayList)this.countries.get(this.sortedCountries.get(var1))).size()) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(int var1, int var2, RecyclerView.ViewHolder var3) {
         if (var3.getItemViewType() == 0) {
            CountrySelectActivity.Country var4 = (CountrySelectActivity.Country)((ArrayList)this.countries.get(this.sortedCountries.get(var1))).get(var2);
            TextSettingsCell var5 = (TextSettingsCell)var3.itemView;
            String var6 = var4.name;
            String var8;
            if (CountrySelectActivity.this.needPhoneCode) {
               StringBuilder var7 = new StringBuilder();
               var7.append("+");
               var7.append(var4.code);
               var8 = var7.toString();
            } else {
               var8 = null;
            }

            var5.setTextAndValue(var6, var8, false);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         boolean var3;
         float var4;
         float var5;
         Object var7;
         if (var2 != 0) {
            var7 = new DividerCell(this.mContext);
            var3 = LocaleController.isRTL;
            var4 = 24.0F;
            if (var3) {
               var5 = 24.0F;
            } else {
               var5 = 72.0F;
            }

            var2 = AndroidUtilities.dp(var5);
            int var6 = AndroidUtilities.dp(8.0F);
            var5 = var4;
            if (LocaleController.isRTL) {
               var5 = 72.0F;
            }

            ((View)var7).setPadding(var2, var6, AndroidUtilities.dp(var5), AndroidUtilities.dp(8.0F));
         } else {
            var7 = new TextSettingsCell(this.mContext);
            var3 = LocaleController.isRTL;
            var4 = 16.0F;
            if (var3) {
               var5 = 16.0F;
            } else {
               var5 = 54.0F;
            }

            var2 = AndroidUtilities.dp(var5);
            var5 = var4;
            if (LocaleController.isRTL) {
               var5 = 54.0F;
            }

            ((View)var7).setPadding(var2, 0, AndroidUtilities.dp(var5), 0);
         }

         return new RecyclerListView.Holder((View)var7);
      }
   }

   public class CountrySearchAdapter extends RecyclerListView.SelectionAdapter {
      private HashMap countries;
      private Context mContext;
      private ArrayList searchResult;
      private Timer searchTimer;

      public CountrySearchAdapter(Context var2, HashMap var3) {
         this.mContext = var2;
         this.countries = var3;
      }

      private void processSearch(String var1) {
         Utilities.searchQueue.postRunnable(new _$$Lambda$CountrySelectActivity$CountrySearchAdapter$udtIr0WBUnvIEbTuJKkynwxgLek(this, var1));
      }

      private void updateSearchResults(ArrayList var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$CountrySelectActivity$CountrySearchAdapter$XZsv25DJkZo_nbKtZwVZiPFyURs(this, var1));
      }

      public CountrySelectActivity.Country getItem(int var1) {
         return var1 >= 0 && var1 < this.searchResult.size() ? (CountrySelectActivity.Country)this.searchResult.get(var1) : null;
      }

      public int getItemCount() {
         ArrayList var1 = this.searchResult;
         return var1 == null ? 0 : var1.size();
      }

      public int getItemViewType(int var1) {
         return 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return true;
      }

      // $FF: synthetic method
      public void lambda$processSearch$0$CountrySelectActivity$CountrySearchAdapter(String var1) {
         if (var1.trim().toLowerCase().length() == 0) {
            this.updateSearchResults(new ArrayList());
         } else {
            ArrayList var2 = new ArrayList();
            String var3 = var1.substring(0, 1);
            ArrayList var5 = (ArrayList)this.countries.get(var3.toUpperCase());
            if (var5 != null) {
               Iterator var6 = var5.iterator();

               while(var6.hasNext()) {
                  CountrySelectActivity.Country var4 = (CountrySelectActivity.Country)var6.next();
                  if (var4.name.toLowerCase().startsWith(var1)) {
                     var2.add(var4);
                  }
               }
            }

            this.updateSearchResults(var2);
         }
      }

      // $FF: synthetic method
      public void lambda$updateSearchResults$1$CountrySelectActivity$CountrySearchAdapter(ArrayList var1) {
         this.searchResult = var1;
         this.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         CountrySelectActivity.Country var3 = (CountrySelectActivity.Country)this.searchResult.get(var2);
         TextSettingsCell var4 = (TextSettingsCell)var1.itemView;
         String var5 = var3.name;
         String var9;
         if (CountrySelectActivity.this.needPhoneCode) {
            StringBuilder var8 = new StringBuilder();
            var8.append("+");
            var8.append(var3.code);
            var9 = var8.toString();
         } else {
            var9 = null;
         }

         int var6 = this.searchResult.size();
         boolean var7 = true;
         if (var2 == var6 - 1) {
            var7 = false;
         }

         var4.setTextAndValue(var5, var9, var7);
      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         return new RecyclerListView.Holder(new TextSettingsCell(this.mContext));
      }

      public void search(final String var1) {
         if (var1 == null) {
            this.searchResult = null;
         } else {
            try {
               if (this.searchTimer != null) {
                  this.searchTimer.cancel();
               }
            } catch (Exception var3) {
               FileLog.e((Throwable)var3);
            }

            this.searchTimer = new Timer();
            this.searchTimer.schedule(new TimerTask() {
               public void run() {
                  try {
                     CountrySearchAdapter.this.searchTimer.cancel();
                     CountrySearchAdapter.this.searchTimer = null;
                  } catch (Exception var2) {
                     FileLog.e((Throwable)var2);
                  }

                  CountrySearchAdapter.this.processSearch(var1);
               }
            }, 100L, 300L);
         }

      }
   }

   public interface CountrySelectActivityDelegate {
      void didSelectCountry(String var1, String var2);
   }
}

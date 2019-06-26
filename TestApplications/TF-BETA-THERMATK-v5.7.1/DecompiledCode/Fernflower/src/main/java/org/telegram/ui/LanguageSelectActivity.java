package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class LanguageSelectActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private EmptyTextProgressView emptyView;
   private LanguageSelectActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private LanguageSelectActivity.ListAdapter searchListViewAdapter;
   private ArrayList searchResult;
   private Timer searchTimer;
   private boolean searchWas;
   private boolean searching;
   private ArrayList sortedLanguages;
   private ArrayList unofficialLanguages;

   private void fillLanguages() {
      _$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU var1 = new _$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU(LocaleController.getInstance().getCurrentLocaleInfo());
      this.sortedLanguages = new ArrayList();
      this.unofficialLanguages = new ArrayList(LocaleController.getInstance().unofficialLanguages);
      ArrayList var2 = LocaleController.getInstance().languages;
      int var3 = var2.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         LocaleController.LocaleInfo var5 = (LocaleController.LocaleInfo)var2.get(var4);
         if (var5.serverIndex != Integer.MAX_VALUE) {
            this.sortedLanguages.add(var5);
         } else {
            this.unofficialLanguages.add(var5);
         }
      }

      Collections.sort(this.sortedLanguages, var1);
      Collections.sort(this.unofficialLanguages, var1);
   }

   // $FF: synthetic method
   static int lambda$fillLanguages$3(LocaleController.LocaleInfo var0, LocaleController.LocaleInfo var1, LocaleController.LocaleInfo var2) {
      if (var1 == var0) {
         return -1;
      } else if (var2 == var0) {
         return 1;
      } else {
         int var3 = var1.serverIndex;
         int var4 = var2.serverIndex;
         if (var3 == var4) {
            return var1.name.compareTo(var2.name);
         } else if (var3 > var4) {
            return 1;
         } else {
            return var3 < var4 ? -1 : 0;
         }
      }
   }

   private void processSearch(String var1) {
      Utilities.searchQueue.postRunnable(new _$$Lambda$LanguageSelectActivity$eAs20MfDQaWJVrE3O9UMwR_59HM(this, var1));
   }

   private void updateSearchResults(ArrayList var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$LanguageSelectActivity$W3KO_no_vIkiCROuRNp2vphR3Bk(this, var1));
   }

   public View createView(Context var1) {
      this.searching = false;
      this.searchWas = false;
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("Language", 2131559715));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               LanguageSelectActivity.this.finishFragment();
            }

         }
      });
      super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public void onSearchCollapse() {
            LanguageSelectActivity.this.search((String)null);
            LanguageSelectActivity.this.searching = false;
            LanguageSelectActivity.this.searchWas = false;
            if (LanguageSelectActivity.this.listView != null) {
               LanguageSelectActivity.this.emptyView.setVisibility(8);
               LanguageSelectActivity.this.listView.setAdapter(LanguageSelectActivity.this.listAdapter);
            }

         }

         public void onSearchExpand() {
            LanguageSelectActivity.this.searching = true;
         }

         public void onTextChanged(EditText var1) {
            String var2 = var1.getText().toString();
            LanguageSelectActivity.this.search(var2);
            if (var2.length() != 0) {
               LanguageSelectActivity.this.searchWas = true;
               if (LanguageSelectActivity.this.listView != null) {
                  LanguageSelectActivity.this.listView.setAdapter(LanguageSelectActivity.this.searchListViewAdapter);
               }
            }

         }
      }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      this.listAdapter = new LanguageSelectActivity.ListAdapter(var1, false);
      this.searchListViewAdapter = new LanguageSelectActivity.ListAdapter(var1, true);
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
      this.emptyView.showTextView();
      this.emptyView.setShowAtCenter(true);
      var2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setEmptyView(this.emptyView);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setAdapter(this.listAdapter);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$LanguageSelectActivity$_kDK86Zracai_nye8r1jdu_vwQ0(this)));
      this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$LanguageSelectActivity$3pzz3aV3o29w5n6tkl_ts3VGtNU(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1 && LanguageSelectActivity.this.searching && LanguageSelectActivity.this.searchWas) {
               AndroidUtilities.hideKeyboard(LanguageSelectActivity.this.getParentActivity().getCurrentFocus());
            }

         }
      });
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.suggestedLangpack && this.listAdapter != null) {
         this.fillLanguages();
         this.listAdapter.notifyDataSetChanged();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{LanguageCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCH, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearch");
      ThemeDescription var9 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearchPlaceholder");
      ThemeDescription var10 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      ThemeDescription var11 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder");
      RecyclerListView var12 = this.listView;
      Paint var13 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, new ThemeDescription(var12, 0, new Class[]{View.class}, var13, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"textView2"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"checkImage"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "featuredStickers_addedIcon")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$LanguageSelectActivity(View var1, int var2) {
      if (this.getParentActivity() != null && super.parentLayout != null && var1 instanceof LanguageCell) {
         LocaleController.LocaleInfo var3 = ((LanguageCell)var1).getCurrentLocale();
         if (var3 != null) {
            LocaleController.getInstance().applyLanguage(var3, true, false, false, true, super.currentAccount);
            super.parentLayout.rebuildAllFragmentViews(false, false);
         }

         this.finishFragment();
      }

   }

   // $FF: synthetic method
   public boolean lambda$createView$2$LanguageSelectActivity(View var1, int var2) {
      if (this.getParentActivity() != null && super.parentLayout != null && var1 instanceof LanguageCell) {
         LocaleController.LocaleInfo var3 = ((LanguageCell)var1).getCurrentLocale();
         if (var3 != null && var3.pathToFile != null && (!var3.isRemote() || var3.serverIndex == Integer.MAX_VALUE)) {
            AlertDialog.Builder var4 = new AlertDialog.Builder(this.getParentActivity());
            var4.setMessage(LocaleController.getString("DeleteLocalization", 2131559247));
            var4.setTitle(LocaleController.getString("AppName", 2131558635));
            var4.setPositiveButton(LocaleController.getString("Delete", 2131559227), new _$$Lambda$LanguageSelectActivity$oSx7KAjKIG5eHsslTckzl6eK4_U(this, var3));
            var4.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            this.showDialog(var4.create());
            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$null$1$LanguageSelectActivity(LocaleController.LocaleInfo var1, DialogInterface var2, int var3) {
      if (LocaleController.getInstance().deleteLanguage(var1, super.currentAccount)) {
         this.fillLanguages();
         ArrayList var5 = this.searchResult;
         if (var5 != null) {
            var5.remove(var1);
         }

         LanguageSelectActivity.ListAdapter var4 = this.listAdapter;
         if (var4 != null) {
            var4.notifyDataSetChanged();
         }

         var4 = this.searchListViewAdapter;
         if (var4 != null) {
            var4.notifyDataSetChanged();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$processSearch$4$LanguageSelectActivity(String var1) {
      if (var1.trim().toLowerCase().length() == 0) {
         this.updateSearchResults(new ArrayList());
      } else {
         System.currentTimeMillis();
         ArrayList var2 = new ArrayList();
         int var3 = this.unofficialLanguages.size();
         byte var4 = 0;

         int var5;
         LocaleController.LocaleInfo var6;
         for(var5 = 0; var5 < var3; ++var5) {
            var6 = (LocaleController.LocaleInfo)this.unofficialLanguages.get(var5);
            if (var6.name.toLowerCase().startsWith(var1) || var6.nameEnglish.toLowerCase().startsWith(var1)) {
               var2.add(var6);
            }
         }

         var3 = this.sortedLanguages.size();

         for(var5 = var4; var5 < var3; ++var5) {
            var6 = (LocaleController.LocaleInfo)this.sortedLanguages.get(var5);
            if (var6.name.toLowerCase().startsWith(var1) || var6.nameEnglish.toLowerCase().startsWith(var1)) {
               var2.add(var6);
            }
         }

         this.updateSearchResults(var2);
      }
   }

   // $FF: synthetic method
   public void lambda$updateSearchResults$5$LanguageSelectActivity(ArrayList var1) {
      this.searchResult = var1;
      this.searchListViewAdapter.notifyDataSetChanged();
   }

   public boolean onFragmentCreate() {
      this.fillLanguages();
      LocaleController.getInstance().loadRemoteLanguages(super.currentAccount);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
   }

   public void onResume() {
      super.onResume();
      LanguageSelectActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

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
                  LanguageSelectActivity.this.searchTimer.cancel();
                  LanguageSelectActivity.this.searchTimer = null;
               } catch (Exception var2) {
                  FileLog.e((Throwable)var2);
               }

               LanguageSelectActivity.this.processSearch(var1);
            }
         }, 100L, 300L);
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;
      private boolean search;

      public ListAdapter(Context var2, boolean var3) {
         this.mContext = var2;
         this.search = var3;
      }

      public int getItemCount() {
         if (this.search) {
            return LanguageSelectActivity.this.searchResult == null ? 0 : LanguageSelectActivity.this.searchResult.size();
         } else {
            int var1 = LanguageSelectActivity.this.sortedLanguages.size();
            int var2 = var1;
            if (var1 != 0) {
               var2 = var1 + 1;
            }

            var1 = var2;
            if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty()) {
               var1 = var2 + LanguageSelectActivity.this.unofficialLanguages.size() + 1;
            }

            return var1;
         }
      }

      public int getItemViewType(int var1) {
         return (LanguageSelectActivity.this.unofficialLanguages.isEmpty() || var1 != LanguageSelectActivity.this.unofficialLanguages.size() && var1 != LanguageSelectActivity.this.unofficialLanguages.size() + LanguageSelectActivity.this.sortedLanguages.size() + 1) && (!LanguageSelectActivity.this.unofficialLanguages.isEmpty() || var1 != LanguageSelectActivity.this.sortedLanguages.size()) ? 0 : 1;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getItemViewType() == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = true;
         if (var3 != 0) {
            if (var3 == 1) {
               ShadowSectionCell var7 = (ShadowSectionCell)var1.itemView;
               if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty() && var2 == LanguageSelectActivity.this.unofficialLanguages.size()) {
                  var7.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               } else {
                  var7.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               }
            }
         } else {
            LanguageCell var5;
            LocaleController.LocaleInfo var8;
            boolean var9;
            label52: {
               label64: {
                  var5 = (LanguageCell)var1.itemView;
                  LocaleController.LocaleInfo var6;
                  if (this.search) {
                     var6 = (LocaleController.LocaleInfo)LanguageSelectActivity.this.searchResult.get(var2);
                     var8 = var6;
                     if (var2 == LanguageSelectActivity.this.searchResult.size() - 1) {
                        var8 = var6;
                        break label64;
                     }
                  } else if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty() && var2 >= 0 && var2 < LanguageSelectActivity.this.unofficialLanguages.size()) {
                     var6 = (LocaleController.LocaleInfo)LanguageSelectActivity.this.unofficialLanguages.get(var2);
                     var8 = var6;
                     if (var2 == LanguageSelectActivity.this.unofficialLanguages.size() - 1) {
                        var8 = var6;
                        break label64;
                     }
                  } else {
                     var3 = var2;
                     if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty()) {
                        var3 = var2 - (LanguageSelectActivity.this.unofficialLanguages.size() + 1);
                     }

                     var6 = (LocaleController.LocaleInfo)LanguageSelectActivity.this.sortedLanguages.get(var3);
                     var8 = var6;
                     if (var3 == LanguageSelectActivity.this.sortedLanguages.size() - 1) {
                        var8 = var6;
                        break label64;
                     }
                  }

                  var9 = false;
                  break label52;
               }

               var9 = true;
            }

            if (var8.isLocal()) {
               var5.setLanguage(var8, String.format("%1$s (%2$s)", var8.name, LocaleController.getString("LanguageCustom", 2131559718)), var9 ^ true);
            } else {
               var5.setLanguage(var8, (String)null, var9 ^ true);
            }

            if (var8 != LocaleController.getInstance().getCurrentLocaleInfo()) {
               var4 = false;
            }

            var5.setLanguageSelected(var4);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            var3 = new ShadowSectionCell(this.mContext);
         } else {
            var3 = new LanguageCell(this.mContext, false);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }
}

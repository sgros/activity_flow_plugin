package org.telegram.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ClearCacheService;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class CacheControlActivity extends BaseFragment {
   private long audioSize = -1L;
   private int cacheInfoRow;
   private int cacheRow;
   private long cacheSize = -1L;
   private boolean calculating = true;
   private volatile boolean canceled = false;
   private boolean[] clear = new boolean[6];
   private int databaseInfoRow;
   private int databaseRow;
   private long databaseSize = -1L;
   private long documentsSize = -1L;
   private int keepMediaInfoRow;
   private int keepMediaRow;
   private LinearLayoutManager layoutManager;
   private CacheControlActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private long musicSize = -1L;
   private long photoSize = -1L;
   private int rowCount;
   private long totalSize = -1L;
   private long videoSize = -1L;

   private void cleanupFolders() {
      AlertDialog var1 = new AlertDialog(this.getParentActivity(), 3);
      var1.setCanCacnel(false);
      var1.show();
      Utilities.globalQueue.postRunnable(new _$$Lambda$CacheControlActivity$jBkbYZANDW5o41l52WQZczRyijs(this, var1));
   }

   private long getDirectorySize(File var1, int var2) {
      long var3 = 0L;
      long var5 = var3;
      if (var1 != null) {
         if (this.canceled) {
            var5 = var3;
         } else if (var1.isDirectory()) {
            var5 = Utilities.getDirSize(var1.getAbsolutePath(), var2);
         } else {
            var5 = var3;
            if (var1.isFile()) {
               var5 = 0L + var1.length();
            }
         }
      }

      return var5;
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("StorageUsage", 2131560832));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               CacheControlActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new CacheControlActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.listView = new RecyclerListView(var1);
      this.listView.setVerticalScrollBarEnabled(false);
      RecyclerListView var3 = this.listView;
      LinearLayoutManager var4 = new LinearLayoutManager(var1, 1, false);
      this.layoutManager = var4;
      var3.setLayoutManager(var4);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$CacheControlActivity$5oIUhVifUjQ5reXmB61Qqo1Afzs(this)));
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[]{new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4")};
   }

   // $FF: synthetic method
   public void lambda$cleanupFolders$3$CacheControlActivity(AlertDialog var1) {
      int var2 = 0;

      boolean var3;
      boolean var4;
      for(var3 = false; var2 < 6; var3 = var4) {
         if (!this.clear[var2]) {
            var4 = var3;
         } else {
            byte var5;
            byte var6;
            label68: {
               if (var2 == 0) {
                  var5 = 0;
               } else if (var2 == 1) {
                  var5 = 2;
               } else {
                  if (var2 == 2) {
                     var5 = 3;
                     var6 = 1;
                     break label68;
                  }

                  if (var2 == 3) {
                     var5 = 3;
                     var6 = 2;
                     break label68;
                  }

                  if (var2 == 4) {
                     var5 = 1;
                  } else if (var2 == 5) {
                     var5 = 4;
                  } else {
                     var5 = -1;
                  }
               }

               var6 = 0;
            }

            if (var5 == -1) {
               var4 = var3;
            } else {
               label78: {
                  File var7 = FileLoader.checkDirectory(var5);
                  if (var7 != null) {
                     Utilities.clearDir(var7.getAbsolutePath(), var6, Long.MAX_VALUE);
                  }

                  if (var5 == 4) {
                     this.cacheSize = this.getDirectorySize(FileLoader.checkDirectory(4), var6);
                  } else {
                     if (var5 == 1) {
                        this.audioSize = this.getDirectorySize(FileLoader.checkDirectory(1), var6);
                        var4 = var3;
                        break label78;
                     }

                     if (var5 == 3) {
                        if (var6 == 1) {
                           this.documentsSize = this.getDirectorySize(FileLoader.checkDirectory(3), var6);
                           var4 = var3;
                        } else {
                           this.musicSize = this.getDirectorySize(FileLoader.checkDirectory(3), var6);
                           var4 = var3;
                        }
                        break label78;
                     }

                     if (var5 != 0) {
                        var4 = var3;
                        if (var5 == 2) {
                           this.videoSize = this.getDirectorySize(FileLoader.checkDirectory(2), var6);
                           var4 = var3;
                        }
                        break label78;
                     }

                     this.photoSize = this.getDirectorySize(FileLoader.checkDirectory(0), var6);
                  }

                  var4 = true;
               }
            }
         }

         ++var2;
      }

      this.totalSize = this.cacheSize + this.videoSize + this.audioSize + this.photoSize + this.documentsSize + this.musicSize;
      AndroidUtilities.runOnUIThread(new _$$Lambda$CacheControlActivity$Lvpblwm67qF5Tz21D4W9HaTE1WA(this, var3, var1));
   }

   // $FF: synthetic method
   public void lambda$createView$10$CacheControlActivity(View var1, int var2) {
      if (this.getParentActivity() != null) {
         BottomSheet.Builder var3;
         String var10;
         if (var2 == this.keepMediaRow) {
            var3 = new BottomSheet.Builder(this.getParentActivity());
            var10 = LocaleController.formatPluralString("Days", 3);
            String var4 = LocaleController.formatPluralString("Weeks", 1);
            String var5 = LocaleController.formatPluralString("Months", 1);
            String var6 = LocaleController.getString("KeepMediaForever", 2131559711);
            _$$Lambda$CacheControlActivity$Z3kCWEOqHs90j5WRi_5avKV3MH4 var7 = new _$$Lambda$CacheControlActivity$Z3kCWEOqHs90j5WRi_5avKV3MH4(this);
            var3.setItems(new CharSequence[]{var10, var4, var5, var6}, var7);
            this.showDialog(var3.create());
         } else if (var2 == this.databaseRow) {
            AlertDialog.Builder var11 = new AlertDialog.Builder(this.getParentActivity());
            var11.setTitle(LocaleController.getString("AppName", 2131558635));
            var11.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            var11.setMessage(LocaleController.getString("LocalDatabaseClear", 2131559773));
            var11.setPositiveButton(LocaleController.getString("CacheClear", 2131558866), new _$$Lambda$CacheControlActivity$OxPoOxpLG_g1G1jasfpkphw5fag(this));
            this.showDialog(var11.create());
         } else if (var2 == this.cacheRow && this.totalSize > 0L && this.getParentActivity() != null) {
            var3 = new BottomSheet.Builder(this.getParentActivity());
            var3.setApplyTopPadding(false);
            var3.setApplyBottomPadding(false);
            LinearLayout var12 = new LinearLayout(this.getParentActivity());
            var12.setOrientation(1);

            for(var2 = 0; var2 < 6; ++var2) {
               long var8;
               if (var2 == 0) {
                  var8 = this.photoSize;
                  var10 = LocaleController.getString("LocalPhotoCache", 2131559778);
               } else if (var2 == 1) {
                  var8 = this.videoSize;
                  var10 = LocaleController.getString("LocalVideoCache", 2131559779);
               } else if (var2 == 2) {
                  var8 = this.documentsSize;
                  var10 = LocaleController.getString("LocalDocumentCache", 2131559775);
               } else if (var2 == 3) {
                  var8 = this.musicSize;
                  var10 = LocaleController.getString("LocalMusicCache", 2131559777);
               } else if (var2 == 4) {
                  var8 = this.audioSize;
                  var10 = LocaleController.getString("LocalAudioCache", 2131559770);
               } else if (var2 == 5) {
                  var8 = this.cacheSize;
                  var10 = LocaleController.getString("LocalCache", 2131559771);
               } else {
                  var10 = null;
                  var8 = 0L;
               }

               if (var8 > 0L) {
                  this.clear[var2] = true;
                  CheckBoxCell var14 = new CheckBoxCell(this.getParentActivity(), 1, 21);
                  var14.setTag(var2);
                  var14.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                  var12.addView(var14, LayoutHelper.createLinear(-1, 50));
                  var14.setText(var10, AndroidUtilities.formatFileSize(var8), true, true);
                  var14.setTextColor(Theme.getColor("dialogTextBlack"));
                  var14.setOnClickListener(new _$$Lambda$CacheControlActivity$nToR5mmUsDX6DDZcwMPXsRZbyZs(this));
               } else {
                  this.clear[var2] = false;
               }
            }

            BottomSheet.BottomSheetCell var13 = new BottomSheet.BottomSheetCell(this.getParentActivity(), 1);
            var13.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            var13.setTextAndIcon(LocaleController.getString("ClearMediaCache", 2131559110).toUpperCase(), 0);
            var13.setTextColor(Theme.getColor("windowBackgroundWhiteRedText"));
            var13.setOnClickListener(new _$$Lambda$CacheControlActivity$xjCzZWiHEv1HCRKFRUbJfrKMn_g(this));
            var12.addView(var13, LayoutHelper.createLinear(-1, 50));
            var3.setCustomView(var12);
            this.showDialog(var3.create());
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$0$CacheControlActivity() {
      this.calculating = false;
      CacheControlActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$CacheControlActivity(boolean var1, AlertDialog var2) {
      if (var1) {
         ImageLoader.getInstance().clearMemory();
      }

      CacheControlActivity.ListAdapter var3 = this.listAdapter;
      if (var3 != null) {
         var3.notifyDataSetChanged();
      }

      try {
         var2.dismiss();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$null$4$CacheControlActivity(DialogInterface var1, int var2) {
      Editor var4 = MessagesController.getGlobalMainSettings().edit();
      if (var2 == 0) {
         var4.putInt("keep_media", 3);
      } else if (var2 == 1) {
         var4.putInt("keep_media", 0);
      } else if (var2 == 2) {
         var4.putInt("keep_media", 1);
      } else if (var2 == 3) {
         var4.putInt("keep_media", 2);
      }

      var4.commit();
      CacheControlActivity.ListAdapter var5 = this.listAdapter;
      if (var5 != null) {
         var5.notifyDataSetChanged();
      }

      PendingIntent var6 = PendingIntent.getService(ApplicationLoader.applicationContext, 1, new Intent(ApplicationLoader.applicationContext, ClearCacheService.class), 0);
      AlarmManager var3 = (AlarmManager)ApplicationLoader.applicationContext.getSystemService("alarm");
      var3.cancel(var6);
      if (var2 != 3) {
         var3.setInexactRepeating(0, 0L, 86400000L, var6);
      }

   }

   // $FF: synthetic method
   public void lambda$null$5$CacheControlActivity(AlertDialog var1) {
      try {
         var1.dismiss();
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      if (this.listAdapter != null) {
         this.databaseSize = MessagesStorage.getInstance(super.currentAccount).getDatabaseSize();
         this.listAdapter.notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   public void lambda$null$6$CacheControlActivity(AlertDialog param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$null$7$CacheControlActivity(DialogInterface var1, int var2) {
      AlertDialog var3 = new AlertDialog(this.getParentActivity(), 3);
      var3.setCanCacnel(false);
      var3.show();
      MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$CacheControlActivity$2BeyhPeIil2krQM27Ksn4kqSTlk(this, var3));
   }

   // $FF: synthetic method
   public void lambda$null$8$CacheControlActivity(View var1) {
      CheckBoxCell var2 = (CheckBoxCell)var1;
      int var3 = (Integer)var2.getTag();
      boolean[] var4 = this.clear;
      var4[var3] ^= true;
      var2.setChecked(var4[var3], true);
   }

   // $FF: synthetic method
   public void lambda$null$9$CacheControlActivity(View var1) {
      try {
         if (super.visibleDialog != null) {
            super.visibleDialog.dismiss();
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      this.cleanupFolders();
   }

   // $FF: synthetic method
   public void lambda$onFragmentCreate$1$CacheControlActivity() {
      this.cacheSize = this.getDirectorySize(FileLoader.checkDirectory(4), 0);
      if (!this.canceled) {
         this.photoSize = this.getDirectorySize(FileLoader.checkDirectory(0), 0);
         if (!this.canceled) {
            this.videoSize = this.getDirectorySize(FileLoader.checkDirectory(2), 0);
            if (!this.canceled) {
               this.documentsSize = this.getDirectorySize(FileLoader.checkDirectory(3), 1);
               if (!this.canceled) {
                  this.musicSize = this.getDirectorySize(FileLoader.checkDirectory(3), 2);
                  if (!this.canceled) {
                     this.audioSize = this.getDirectorySize(FileLoader.checkDirectory(1), 0);
                     this.totalSize = this.cacheSize + this.videoSize + this.audioSize + this.photoSize + this.documentsSize + this.musicSize;
                     AndroidUtilities.runOnUIThread(new _$$Lambda$CacheControlActivity$rl9mnpe2QS7cCoiKVnOTFMsRWgA(this));
                  }
               }
            }
         }
      }
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.rowCount = 0;
      int var1 = this.rowCount++;
      this.keepMediaRow = var1;
      var1 = this.rowCount++;
      this.keepMediaInfoRow = var1;
      var1 = this.rowCount++;
      this.cacheRow = var1;
      var1 = this.rowCount++;
      this.cacheInfoRow = var1;
      var1 = this.rowCount++;
      this.databaseRow = var1;
      var1 = this.rowCount++;
      this.databaseInfoRow = var1;
      this.databaseSize = MessagesStorage.getInstance(super.currentAccount).getDatabaseSize();
      Utilities.globalQueue.postRunnable(new _$$Lambda$CacheControlActivity$2KHbuhNmXFdFJaa7SHWCPv8UB_I(this));
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      this.canceled = true;
   }

   public void onResume() {
      super.onResume();
      CacheControlActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return CacheControlActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         return var1 != CacheControlActivity.this.databaseInfoRow && var1 != CacheControlActivity.this.cacheInfoRow && var1 != CacheControlActivity.this.keepMediaInfoRow ? 0 : 1;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if (var2 != CacheControlActivity.this.databaseRow && (var2 != CacheControlActivity.this.cacheRow || CacheControlActivity.this.totalSize <= 0L) && var2 != CacheControlActivity.this.keepMediaRow) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 == 1) {
               TextInfoPrivacyCell var6 = (TextInfoPrivacyCell)var1.itemView;
               if (var2 == CacheControlActivity.this.databaseInfoRow) {
                  var6.setText(LocaleController.getString("LocalDatabaseInfo", 2131559774));
                  var6.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               } else if (var2 == CacheControlActivity.this.cacheInfoRow) {
                  var6.setText("");
                  var6.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               } else if (var2 == CacheControlActivity.this.keepMediaInfoRow) {
                  var6.setText(AndroidUtilities.replaceTags(LocaleController.getString("KeepMediaInfo", 2131559712)));
                  var6.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               }
            }
         } else {
            TextSettingsCell var4 = (TextSettingsCell)var1.itemView;
            if (var2 == CacheControlActivity.this.databaseRow) {
               var4.setTextAndValue(LocaleController.getString("LocalDatabase", 2131559772), AndroidUtilities.formatFileSize(CacheControlActivity.this.databaseSize), false);
            } else {
               String var7;
               if (var2 == CacheControlActivity.this.cacheRow) {
                  if (CacheControlActivity.this.calculating) {
                     var4.setTextAndValue(LocaleController.getString("ClearMediaCache", 2131559110), LocaleController.getString("CalculatingSize", 2131558868), false);
                  } else {
                     String var5 = LocaleController.getString("ClearMediaCache", 2131559110);
                     if (CacheControlActivity.this.totalSize == 0L) {
                        var7 = LocaleController.getString("CacheEmpty", 2131558867);
                     } else {
                        var7 = AndroidUtilities.formatFileSize(CacheControlActivity.this.totalSize);
                     }

                     var4.setTextAndValue(var5, var7, false);
                  }
               } else if (var2 == CacheControlActivity.this.keepMediaRow) {
                  var2 = MessagesController.getGlobalMainSettings().getInt("keep_media", 2);
                  if (var2 == 0) {
                     var7 = LocaleController.formatPluralString("Weeks", 1);
                  } else if (var2 == 1) {
                     var7 = LocaleController.formatPluralString("Months", 1);
                  } else if (var2 == 3) {
                     var7 = LocaleController.formatPluralString("Days", 3);
                  } else {
                     var7 = LocaleController.getString("KeepMediaForever", 2131559711);
                  }

                  var4.setTextAndValue(LocaleController.getString("KeepMedia", 2131559710), var7, false);
               }
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            var3 = new TextInfoPrivacyCell(this.mContext);
         } else {
            var3 = new TextSettingsCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }
}

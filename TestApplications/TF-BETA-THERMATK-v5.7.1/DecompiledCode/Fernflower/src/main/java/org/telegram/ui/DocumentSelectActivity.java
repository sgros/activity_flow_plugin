package org.telegram.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.SharedDocumentCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.RecyclerListView;

public class DocumentSelectActivity extends BaseFragment {
   private static final int done = 3;
   private ArrayList actionModeViews = new ArrayList();
   private boolean allowMusic;
   private boolean canSelectOnlyImageFiles;
   private File currentDir;
   private DocumentSelectActivity.DocumentSelectActivityDelegate delegate;
   private EmptyTextProgressView emptyView;
   private ArrayList history = new ArrayList();
   private ArrayList items = new ArrayList();
   private LinearLayoutManager layoutManager;
   private DocumentSelectActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private int maxSelectedFiles = -1;
   private BroadcastReceiver receiver = new BroadcastReceiver() {
      // $FF: synthetic method
      public void lambda$onReceive$0$DocumentSelectActivity$1() {
         try {
            if (DocumentSelectActivity.this.currentDir == null) {
               DocumentSelectActivity.this.listRoots();
            } else {
               DocumentSelectActivity.this.listFiles(DocumentSelectActivity.this.currentDir);
            }
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

      }

      public void onReceive(Context var1, Intent var2) {
         _$$Lambda$DocumentSelectActivity$1$QUHI_wOoyDp1Dwx1fGWii_dVi9g var3 = new _$$Lambda$DocumentSelectActivity$1$QUHI_wOoyDp1Dwx1fGWii_dVi9g(this);
         if ("android.intent.action.MEDIA_UNMOUNTED".equals(var2.getAction())) {
            DocumentSelectActivity.this.listView.postDelayed(var3, 1000L);
         } else {
            var3.run();
         }

      }
   };
   private boolean receiverRegistered = false;
   private ArrayList recentItems = new ArrayList();
   private boolean scrolling;
   private HashMap selectedFiles = new HashMap();
   private NumberTextView selectedMessagesCountTextView;
   private long sizeLimit = 1610612736L;

   public DocumentSelectActivity(boolean var1) {
      this.allowMusic = var1;
   }

   // $FF: synthetic method
   static ActionBar access$1400(DocumentSelectActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$400(DocumentSelectActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$600(DocumentSelectActivity var0) {
      return var0.actionBar;
   }

   private void fixLayoutInternal() {
      if (this.selectedMessagesCountTextView != null) {
         if (!AndroidUtilities.isTablet() && ApplicationLoader.applicationContext.getResources().getConfiguration().orientation == 2) {
            this.selectedMessagesCountTextView.setTextSize(18);
         } else {
            this.selectedMessagesCountTextView.setTextSize(20);
         }

      }
   }

   private String getRootSubtitle(String param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static int lambda$listFiles$4(File var0, File var1) {
      if (var0.isDirectory() != var1.isDirectory()) {
         byte var2;
         if (var0.isDirectory()) {
            var2 = -1;
         } else {
            var2 = 1;
         }

         return var2;
      } else {
         return var0.getName().compareToIgnoreCase(var1.getName());
      }
   }

   // $FF: synthetic method
   static int lambda$loadRecentFiles$3(DocumentSelectActivity.ListItem var0, DocumentSelectActivity.ListItem var1) {
      long var2 = var0.file.lastModified();
      long var4 = var1.file.lastModified();
      if (var2 == var4) {
         return 0;
      } else {
         return var2 > var4 ? -1 : 1;
      }
   }

   private boolean listFiles(File var1) {
      if (!var1.canRead()) {
         if ((var1.getAbsolutePath().startsWith(Environment.getExternalStorageDirectory().toString()) || var1.getAbsolutePath().startsWith("/sdcard") || var1.getAbsolutePath().startsWith("/mnt/sdcard")) && !Environment.getExternalStorageState().equals("mounted") && !Environment.getExternalStorageState().equals("mounted_ro")) {
            this.currentDir = var1;
            this.items.clear();
            if ("shared".equals(Environment.getExternalStorageState())) {
               this.emptyView.setText(LocaleController.getString("UsbActive", 2131560966));
            } else {
               this.emptyView.setText(LocaleController.getString("NotMounted", 2131559960));
            }

            AndroidUtilities.clearDrawableAnimation(this.listView);
            this.scrolling = true;
            this.listAdapter.notifyDataSetChanged();
            return true;
         } else {
            this.showErrorBox(LocaleController.getString("AccessError", 2131558485));
            return false;
         }
      } else {
         File[] var2;
         try {
            var2 = var1.listFiles();
         } catch (Exception var7) {
            this.showErrorBox(var7.getLocalizedMessage());
            return false;
         }

         if (var2 == null) {
            this.showErrorBox(LocaleController.getString("UnknownError", 2131560937));
            return false;
         } else {
            this.currentDir = var1;
            this.items.clear();
            Arrays.sort(var2, _$$Lambda$DocumentSelectActivity$KoBsEgVmPEJw_tYKCZKJ_jB7oso.INSTANCE);

            for(int var3 = 0; var3 < var2.length; ++var3) {
               File var4 = var2[var3];
               if (var4.getName().indexOf(46) != 0) {
                  DocumentSelectActivity.ListItem var5 = new DocumentSelectActivity.ListItem();
                  var5.title = var4.getName();
                  var5.file = var4;
                  if (var4.isDirectory()) {
                     var5.icon = 2131165438;
                     var5.subtitle = LocaleController.getString("Folder", 2131559497);
                  } else {
                     String var6 = var4.getName();
                     String[] var8 = var6.split("\\.");
                     String var9;
                     if (var8.length > 1) {
                        var9 = var8[var8.length - 1];
                     } else {
                        var9 = "?";
                     }

                     var5.ext = var9;
                     var5.subtitle = AndroidUtilities.formatFileSize(var4.length());
                     var9 = var6.toLowerCase();
                     if (var9.endsWith(".jpg") || var9.endsWith(".png") || var9.endsWith(".gif") || var9.endsWith(".jpeg")) {
                        var5.thumb = var4.getAbsolutePath();
                     }
                  }

                  this.items.add(var5);
               }
            }

            DocumentSelectActivity.ListItem var12 = new DocumentSelectActivity.ListItem();
            var12.title = "..";
            if (this.history.size() > 0) {
               ArrayList var10 = this.history;
               File var11 = ((DocumentSelectActivity.HistoryEntry)var10.get(var10.size() - 1)).dir;
               if (var11 == null) {
                  var12.subtitle = LocaleController.getString("Folder", 2131559497);
               } else {
                  var12.subtitle = var11.toString();
               }
            } else {
               var12.subtitle = LocaleController.getString("Folder", 2131559497);
            }

            var12.icon = 2131165438;
            var12.file = null;
            this.items.add(0, var12);
            AndroidUtilities.clearDrawableAnimation(this.listView);
            this.scrolling = true;
            this.listAdapter.notifyDataSetChanged();
            return true;
         }
      }
   }

   @SuppressLint({"NewApi"})
   private void listRoots() {
      // $FF: Couldn't be decompiled
   }

   private void showErrorBox(String var1) {
      if (this.getParentActivity() != null) {
         (new AlertDialog.Builder(this.getParentActivity())).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(var1).setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null).show();
      }
   }

   public View createView(Context var1) {
      if (!this.receiverRegistered) {
         this.receiverRegistered = true;
         IntentFilter var2 = new IntentFilter();
         var2.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
         var2.addAction("android.intent.action.MEDIA_CHECKING");
         var2.addAction("android.intent.action.MEDIA_EJECT");
         var2.addAction("android.intent.action.MEDIA_MOUNTED");
         var2.addAction("android.intent.action.MEDIA_NOFS");
         var2.addAction("android.intent.action.MEDIA_REMOVED");
         var2.addAction("android.intent.action.MEDIA_SHARED");
         var2.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
         var2.addAction("android.intent.action.MEDIA_UNMOUNTED");
         var2.addDataScheme("file");
         ApplicationLoader.applicationContext.registerReceiver(this.receiver, var2);
      }

      super.actionBar.setBackButtonDrawable(new BackDrawable(false));
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("SelectFile", 2131560682));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               if (DocumentSelectActivity.access$400(DocumentSelectActivity.this).isActionModeShowed()) {
                  DocumentSelectActivity.this.selectedFiles.clear();
                  DocumentSelectActivity.access$600(DocumentSelectActivity.this).hideActionMode();
                  int var2 = DocumentSelectActivity.this.listView.getChildCount();

                  for(var1 = 0; var1 < var2; ++var1) {
                     View var3 = DocumentSelectActivity.this.listView.getChildAt(var1);
                     if (var3 instanceof SharedDocumentCell) {
                        ((SharedDocumentCell)var3).setChecked(false, true);
                     }
                  }
               } else {
                  DocumentSelectActivity.this.finishFragment();
               }
            } else if (var1 == 3 && DocumentSelectActivity.this.delegate != null) {
               ArrayList var4 = new ArrayList(DocumentSelectActivity.this.selectedFiles.keySet());
               DocumentSelectActivity.this.delegate.didSelectFiles(DocumentSelectActivity.this, var4);

               for(Iterator var5 = DocumentSelectActivity.this.selectedFiles.values().iterator(); var5.hasNext(); ((DocumentSelectActivity.ListItem)var5.next()).date = System.currentTimeMillis()) {
               }
            }

         }
      });
      this.selectedFiles.clear();
      this.actionModeViews.clear();
      ActionBarMenu var6 = super.actionBar.createActionMode();
      this.selectedMessagesCountTextView = new NumberTextView(var6.getContext());
      this.selectedMessagesCountTextView.setTextSize(18);
      this.selectedMessagesCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.selectedMessagesCountTextView.setTextColor(Theme.getColor("actionBarActionModeDefaultIcon"));
      this.selectedMessagesCountTextView.setOnTouchListener(_$$Lambda$DocumentSelectActivity$wOAW0__TUXtkjQ_Z0vg8d4FmxpE.INSTANCE);
      var6.addView(this.selectedMessagesCountTextView, LayoutHelper.createLinear(0, -1, 1.0F, 65, 0, 0, 0));
      this.actionModeViews.add(var6.addItemWithWidth(3, 2131165412, AndroidUtilities.dp(54.0F)));
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var7 = (FrameLayout)super.fragmentView;
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.showTextView();
      var7.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setVerticalScrollBarEnabled(false);
      RecyclerListView var3 = this.listView;
      LinearLayoutManager var4 = new LinearLayoutManager(var1, 1, false);
      this.layoutManager = var4;
      var3.setLayoutManager(var4);
      this.listView.setEmptyView(this.emptyView);
      var3 = this.listView;
      DocumentSelectActivity.ListAdapter var5 = new DocumentSelectActivity.ListAdapter(var1);
      this.listAdapter = var5;
      var3.setAdapter(var5);
      var7.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            DocumentSelectActivity var4 = DocumentSelectActivity.this;
            boolean var3;
            if (var2 != 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            var4.scrolling = var3;
         }
      });
      this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$DocumentSelectActivity$CyKwYdaVewVAaKBAnA3V_bH1l5E(this)));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$DocumentSelectActivity$lBQknjeUipxjA7znufbDrc42LRU(this)));
      this.listRoots();
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_AM_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarActionModeDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_AM_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarActionModeDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_AM_TOPBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarActionModeDefaultTop"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_AM_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarActionModeDefaultSelector"), new ThemeDescription(this.selectedMessagesCountTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarActionModeDefaultIcon"), new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_graySectionText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "graySection"), new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"dateTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedDocumentCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkbox"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedDocumentCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxCheck"), new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"thumbImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "files_folderIcon"), new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{SharedDocumentCell.class}, new String[]{"thumbImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "files_folderIconBackground"), new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"extTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "files_iconText")};
   }

   // $FF: synthetic method
   public boolean lambda$createView$1$DocumentSelectActivity(View var1, int var2) {
      if (super.actionBar.isActionModeShowed()) {
         return false;
      } else {
         DocumentSelectActivity.ListItem var3 = this.listAdapter.getItem(var2);
         if (var3 == null) {
            return false;
         } else {
            File var4 = var3.file;
            if (var4 != null && !var4.isDirectory()) {
               if (!var4.canRead()) {
                  this.showErrorBox(LocaleController.getString("AccessError", 2131558485));
                  return false;
               }

               if (this.canSelectOnlyImageFiles && var3.thumb == null) {
                  this.showErrorBox(LocaleController.formatString("PassportUploadNotImage", 2131560342));
                  return false;
               }

               if (this.sizeLimit != 0L) {
                  long var5 = var4.length();
                  long var7 = this.sizeLimit;
                  if (var5 > var7) {
                     this.showErrorBox(LocaleController.formatString("FileUploadLimit", 2131559482, AndroidUtilities.formatFileSize(var7)));
                     return false;
                  }
               }

               if (this.maxSelectedFiles >= 0) {
                  int var9 = this.selectedFiles.size();
                  var2 = this.maxSelectedFiles;
                  if (var9 >= var2) {
                     this.showErrorBox(LocaleController.formatString("PassportUploadMaxReached", 2131560341, LocaleController.formatPluralString("Files", var2)));
                     return false;
                  }
               }

               if (var4.length() == 0L) {
                  return false;
               }

               this.selectedFiles.put(var4.toString(), var3);
               this.selectedMessagesCountTextView.setNumber(1, false);
               AnimatorSet var10 = new AnimatorSet();
               ArrayList var11 = new ArrayList();

               for(var2 = 0; var2 < this.actionModeViews.size(); ++var2) {
                  View var12 = (View)this.actionModeViews.get(var2);
                  AndroidUtilities.clearDrawableAnimation(var12);
                  var11.add(ObjectAnimator.ofFloat(var12, "scaleY", new float[]{0.1F, 1.0F}));
               }

               var10.playTogether(var11);
               var10.setDuration(250L);
               var10.start();
               this.scrolling = false;
               if (var1 instanceof SharedDocumentCell) {
                  ((SharedDocumentCell)var1).setChecked(true, true);
               }

               super.actionBar.showActionMode();
            }

            return true;
         }
      }
   }

   // $FF: synthetic method
   public void lambda$createView$2$DocumentSelectActivity(View var1, int var2) {
      DocumentSelectActivity.ListItem var3 = this.listAdapter.getItem(var2);
      if (var3 != null) {
         File var4 = var3.file;
         File var5;
         ArrayList var12;
         if (var4 == null) {
            var2 = var3.icon;
            DocumentSelectActivity.DocumentSelectActivityDelegate var11;
            if (var2 == 2131165473) {
               var11 = this.delegate;
               if (var11 != null) {
                  var11.startDocumentSelectActivity();
               }

               this.finishFragment(false);
            } else if (var2 == 2131165474) {
               var11 = this.delegate;
               if (var11 != null) {
                  var11.startMusicSelectActivity(this);
               }
            } else {
               var12 = this.history;
               DocumentSelectActivity.HistoryEntry var13 = (DocumentSelectActivity.HistoryEntry)var12.remove(var12.size() - 1);
               super.actionBar.setTitle(var13.title);
               var5 = var13.dir;
               if (var5 != null) {
                  this.listFiles(var5);
               } else {
                  this.listRoots();
               }

               this.layoutManager.scrollToPositionWithOffset(var13.scrollItem, var13.scrollOffset);
            }
         } else if (var4.isDirectory()) {
            DocumentSelectActivity.HistoryEntry var14 = new DocumentSelectActivity.HistoryEntry();
            var14.scrollItem = this.layoutManager.findLastVisibleItemPosition();
            var1 = this.layoutManager.findViewByPosition(var14.scrollItem);
            if (var1 != null) {
               var14.scrollOffset = var1.getTop();
            }

            var14.dir = this.currentDir;
            var14.title = super.actionBar.getTitle();
            this.history.add(var14);
            if (!this.listFiles(var4)) {
               this.history.remove(var14);
               return;
            }

            super.actionBar.setTitle(var3.title);
         } else {
            var5 = var4;
            if (!var4.canRead()) {
               this.showErrorBox(LocaleController.getString("AccessError", 2131558485));
               var5 = new File("/mnt/sdcard");
            }

            if (this.canSelectOnlyImageFiles && var3.thumb == null) {
               this.showErrorBox(LocaleController.formatString("PassportUploadNotImage", 2131560342));
               return;
            }

            if (this.sizeLimit != 0L) {
               long var6 = var5.length();
               long var8 = this.sizeLimit;
               if (var6 > var8) {
                  this.showErrorBox(LocaleController.formatString("FileUploadLimit", 2131559482, AndroidUtilities.formatFileSize(var8)));
                  return;
               }
            }

            if (var5.length() == 0L) {
               return;
            }

            if (super.actionBar.isActionModeShowed()) {
               if (this.selectedFiles.containsKey(var5.toString())) {
                  this.selectedFiles.remove(var5.toString());
               } else {
                  if (this.maxSelectedFiles >= 0) {
                     int var10 = this.selectedFiles.size();
                     var2 = this.maxSelectedFiles;
                     if (var10 >= var2) {
                        this.showErrorBox(LocaleController.formatString("PassportUploadMaxReached", 2131560341, LocaleController.formatPluralString("Files", var2)));
                        return;
                     }
                  }

                  this.selectedFiles.put(var5.toString(), var3);
               }

               if (this.selectedFiles.isEmpty()) {
                  super.actionBar.hideActionMode();
               } else {
                  this.selectedMessagesCountTextView.setNumber(this.selectedFiles.size(), true);
               }

               this.scrolling = false;
               if (var1 instanceof SharedDocumentCell) {
                  ((SharedDocumentCell)var1).setChecked(this.selectedFiles.containsKey(var3.file.toString()), true);
               }
            } else if (this.delegate != null) {
               var12 = new ArrayList();
               var12.add(var5.getAbsolutePath());
               this.delegate.didSelectFiles(this, var12);
            }
         }

      }
   }

   public void loadRecentFiles() {
      Exception var10000;
      label85: {
         File[] var1;
         boolean var10001;
         try {
            var1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles();
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
            break label85;
         }

         int var2 = 0;

         while(true) {
            try {
               if (var2 >= var1.length) {
                  break;
               }
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label85;
            }

            File var3 = var1[var2];

            label88: {
               try {
                  if (var3.isDirectory()) {
                     break label88;
                  }
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label85;
               }

               DocumentSelectActivity.ListItem var4;
               String var5;
               String var15;
               label68: {
                  try {
                     var4 = new DocumentSelectActivity.ListItem();
                     var4.title = var3.getName();
                     var4.file = var3;
                     var5 = var3.getName();
                     String[] var6 = var5.split("\\.");
                     if (var6.length > 1) {
                        var15 = var6[var6.length - 1];
                        break label68;
                     }
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label85;
                  }

                  var15 = "?";
               }

               label60: {
                  try {
                     var4.ext = var15;
                     var4.subtitle = AndroidUtilities.formatFileSize(var3.length());
                     var15 = var5.toLowerCase();
                     if (!var15.endsWith(".jpg") && !var15.endsWith(".png") && !var15.endsWith(".gif") && !var15.endsWith(".jpeg")) {
                        break label60;
                     }
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label85;
                  }

                  try {
                     var4.thumb = var3.getAbsolutePath();
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label85;
                  }
               }

               try {
                  this.recentItems.add(var4);
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label85;
               }
            }

            ++var2;
         }

         try {
            Collections.sort(this.recentItems, _$$Lambda$DocumentSelectActivity$9fKm6qZG_Rnisjsmtz1XWQccmXA.INSTANCE);
            return;
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
         }
      }

      Exception var16 = var10000;
      FileLog.e((Throwable)var16);
   }

   public boolean onBackPressed() {
      if (this.history.size() > 0) {
         ArrayList var1 = this.history;
         DocumentSelectActivity.HistoryEntry var3 = (DocumentSelectActivity.HistoryEntry)var1.remove(var1.size() - 1);
         super.actionBar.setTitle(var3.title);
         File var2 = var3.dir;
         if (var2 != null) {
            this.listFiles(var2);
         } else {
            this.listRoots();
         }

         this.layoutManager.scrollToPositionWithOffset(var3.scrollItem, var3.scrollOffset);
         return false;
      } else {
         return super.onBackPressed();
      }
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      RecyclerListView var2 = this.listView;
      if (var2 != null) {
         var2.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               DocumentSelectActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
               DocumentSelectActivity.this.fixLayoutInternal();
               return true;
            }
         });
      }

   }

   public boolean onFragmentCreate() {
      this.loadRecentFiles();
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      try {
         if (this.receiverRegistered) {
            ApplicationLoader.applicationContext.unregisterReceiver(this.receiver);
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      super.onFragmentDestroy();
   }

   public void onResume() {
      super.onResume();
      DocumentSelectActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      this.fixLayoutInternal();
   }

   public void setCanSelectOnlyImageFiles(boolean var1) {
      this.canSelectOnlyImageFiles = true;
   }

   public void setDelegate(DocumentSelectActivity.DocumentSelectActivityDelegate var1) {
      this.delegate = var1;
   }

   public void setMaxSelectedFiles(int var1) {
      this.maxSelectedFiles = var1;
   }

   public interface DocumentSelectActivityDelegate {
      void didSelectFiles(DocumentSelectActivity var1, ArrayList var2);

      void startDocumentSelectActivity();

      void startMusicSelectActivity(BaseFragment var1);
   }

   private class HistoryEntry {
      File dir;
      int scrollItem;
      int scrollOffset;
      String title;

      private HistoryEntry() {
      }

      // $FF: synthetic method
      HistoryEntry(Object var2) {
         this();
      }
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public DocumentSelectActivity.ListItem getItem(int var1) {
         if (var1 < DocumentSelectActivity.this.items.size()) {
            return (DocumentSelectActivity.ListItem)DocumentSelectActivity.this.items.get(var1);
         } else {
            if (DocumentSelectActivity.this.history.isEmpty() && !DocumentSelectActivity.this.recentItems.isEmpty() && var1 != DocumentSelectActivity.this.items.size()) {
               var1 -= DocumentSelectActivity.this.items.size() + 1;
               if (var1 < DocumentSelectActivity.this.recentItems.size()) {
                  return (DocumentSelectActivity.ListItem)DocumentSelectActivity.this.recentItems.get(var1);
               }
            }

            return null;
         }
      }

      public int getItemCount() {
         int var1 = DocumentSelectActivity.this.items.size();
         int var2 = var1;
         if (DocumentSelectActivity.this.history.isEmpty()) {
            var2 = var1;
            if (!DocumentSelectActivity.this.recentItems.isEmpty()) {
               var2 = var1 + DocumentSelectActivity.this.recentItems.size() + 1;
            }
         }

         return var2;
      }

      public int getItemViewType(int var1) {
         byte var2;
         if (this.getItem(var1) != null) {
            var2 = 1;
         } else {
            var2 = 0;
         }

         return var2;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getItemViewType() != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 1) {
            DocumentSelectActivity.ListItem var3 = this.getItem(var2);
            SharedDocumentCell var4 = (SharedDocumentCell)var1.itemView;
            var2 = var3.icon;
            if (var2 != 0) {
               var4.setTextAndValueAndTypeAndThumb(var3.title, var3.subtitle, (String)null, (String)null, var2);
            } else {
               String var5 = var3.ext.toUpperCase().substring(0, Math.min(var3.ext.length(), 4));
               var4.setTextAndValueAndTypeAndThumb(var3.title, var3.subtitle, var5, var3.thumb, 0);
            }

            if (var3.file != null && DocumentSelectActivity.access$1400(DocumentSelectActivity.this).isActionModeShowed()) {
               var4.setChecked(DocumentSelectActivity.this.selectedFiles.containsKey(var3.file.toString()), DocumentSelectActivity.this.scrolling ^ true);
            } else {
               var4.setChecked(false, DocumentSelectActivity.this.scrolling ^ true);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            var3 = new SharedDocumentCell(this.mContext);
         } else {
            var3 = new GraySectionCell(this.mContext);
            ((GraySectionCell)var3).setText(LocaleController.getString("Recent", 2131560537));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   private class ListItem {
      long date;
      String ext;
      File file;
      int icon;
      String subtitle;
      String thumb;
      String title;

      private ListItem() {
         this.subtitle = "";
         this.ext = "";
      }

      // $FF: synthetic method
      ListItem(Object var2) {
         this();
      }
   }
}

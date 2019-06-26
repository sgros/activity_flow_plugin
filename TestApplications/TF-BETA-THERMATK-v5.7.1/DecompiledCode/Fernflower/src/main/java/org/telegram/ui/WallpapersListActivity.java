package org.telegram.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.LongSparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.WallpaperCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.WallpaperUpdater;

public class WallpapersListActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   public static final int TYPE_ALL = 0;
   public static final int TYPE_COLOR = 1;
   private static final int[] defaultColors = new int[]{-1, -2826262, -4993567, -9783318, -16740912, -2891046, -3610935, -3808859, -10375058, -3289169, -5789547, -8622222, -10322, -18835, -2193583, -1059360, -2383431, -20561, -955808, -1524502, -6974739, -2507680, -5145015, -2765065, -2142101, -7613748, -12811138, -14524116, -14398084, -12764283, -10129027, -15195603, -16777216};
   private static final int delete = 4;
   private static final int forward = 3;
   private static final int[] searchColors = new int[]{-16746753, -65536, -30208, -13824, -16718798, -14702165, -9240406, -409915, -9224159, -16777216, -10725281, -1};
   private static final String[] searchColorsNames = new String[]{"Blue", "Red", "Orange", "Yellow", "Green", "Teal", "Purple", "Pink", "Brown", "Black", "Gray", "White"};
   private static final int[] searchColorsNamesR = new int[]{2131558843, 2131560550, 2131560129, 2131561133, 2131559600, 2131560862, 2131560520, 2131560450, 2131558862, 2131558832, 2131559599, 2131561114};
   private ArrayList actionModeViews = new ArrayList();
   private WallpapersListActivity.ColorWallpaper addedColorWallpaper;
   private WallpapersListActivity.FileWallpaper addedFileWallpaper;
   private ArrayList allWallPapers = new ArrayList();
   private LongSparseArray allWallPapersDict = new LongSparseArray();
   private WallpapersListActivity.FileWallpaper catsWallpaper;
   private Paint colorFramePaint;
   private Paint colorPaint;
   private int columnsCount = 3;
   private int currentType;
   private LinearLayoutManager layoutManager;
   private WallpapersListActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private boolean loadingWallpapers;
   private ArrayList patterns = new ArrayList();
   private AlertDialog progressDialog;
   private int resetInfoRow;
   private int resetRow;
   private int resetSectionRow;
   private int rowCount;
   private boolean scrolling;
   private WallpapersListActivity.SearchAdapter searchAdapter;
   private EmptyTextProgressView searchEmptyView;
   private ActionBarMenuItem searchItem;
   private int sectionRow;
   private long selectedBackground;
   private boolean selectedBackgroundBlurred;
   private boolean selectedBackgroundMotion;
   private int selectedColor;
   private float selectedIntensity;
   private NumberTextView selectedMessagesCountTextView;
   private long selectedPattern;
   private LongSparseArray selectedWallPapers = new LongSparseArray();
   private int setColorRow;
   private WallpapersListActivity.FileWallpaper themeWallpaper;
   private int totalWallpaperRows;
   private WallpaperUpdater updater;
   private int uploadImageRow;
   private int wallPaperStartRow;
   private ArrayList wallPapers = new ArrayList();

   public WallpapersListActivity(int var1) {
      this.currentType = var1;
   }

   // $FF: synthetic method
   static ActionBar access$000(WallpapersListActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$1000(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1100(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1200(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1500(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$1600(WallpapersListActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$1700(WallpapersListActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$200(WallpapersListActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$3200(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3300(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3500(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3600(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3700(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3800(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3900(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$400(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4000(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4100(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4200(WallpapersListActivity var0) {
      return var0.classGuid;
   }

   // $FF: synthetic method
   static int access$4300(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4900(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$500(WallpapersListActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$5000(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5100(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$600(WallpapersListActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$6100(WallpapersListActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$700(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$800(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$900(WallpapersListActivity var0) {
      return var0.currentAccount;
   }

   private void fillWallpapersWithCustom() {
      if (this.currentType == 0) {
         MessagesController.getGlobalMainSettings();
         WallpapersListActivity.ColorWallpaper var1 = this.addedColorWallpaper;
         if (var1 != null) {
            this.wallPapers.remove(var1);
            this.addedColorWallpaper = null;
         }

         WallpapersListActivity.FileWallpaper var8 = this.addedFileWallpaper;
         if (var8 != null) {
            this.wallPapers.remove(var8);
            this.addedFileWallpaper = null;
         }

         var8 = this.catsWallpaper;
         if (var8 == null) {
            this.catsWallpaper = new WallpapersListActivity.FileWallpaper(1000001L, 2131165299, 2131165338);
         } else {
            this.wallPapers.remove(var8);
         }

         var8 = this.themeWallpaper;
         if (var8 != null) {
            this.wallPapers.remove(var8);
         }

         boolean var2 = Theme.getCurrentTheme().isDark();
         Collections.sort(this.wallPapers, new _$$Lambda$WallpapersListActivity$_kR2j3QKuwClJW1mMrZH7ooQBYo(this, var2));
         if (Theme.hasWallpaperFromTheme()) {
            if (this.themeWallpaper == null) {
               this.themeWallpaper = new WallpapersListActivity.FileWallpaper(-2L, -2, -2);
            }

            this.wallPapers.add(0, this.themeWallpaper);
         } else {
            this.themeWallpaper = null;
         }

         long var3 = this.selectedBackground;
         int var5;
         if (var3 == -1L || var3 != 1000001L && (var3 < -100L || var3 > 0L) && this.allWallPapersDict.indexOfKey(this.selectedBackground) < 0) {
            var3 = this.selectedPattern;
            String var9 = "wallpaper.jpg";
            if (var3 != 0L) {
               this.addedColorWallpaper = new WallpapersListActivity.ColorWallpaper(this.selectedBackground, this.selectedColor, var3, this.selectedIntensity, this.selectedBackgroundMotion, new File(ApplicationLoader.getFilesDirFixed(), "wallpaper.jpg"));
               this.wallPapers.add(0, this.addedColorWallpaper);
            } else {
               var5 = this.selectedColor;
               if (var5 != 0) {
                  this.addedColorWallpaper = new WallpapersListActivity.ColorWallpaper(this.selectedBackground, var5);
                  this.wallPapers.add(0, this.addedColorWallpaper);
               } else {
                  var3 = this.selectedBackground;
                  File var6 = new File(ApplicationLoader.getFilesDirFixed(), "wallpaper.jpg");
                  File var7 = ApplicationLoader.getFilesDirFixed();
                  if (this.selectedBackgroundBlurred) {
                     var9 = "wallpaper_original.jpg";
                  }

                  this.addedFileWallpaper = new WallpapersListActivity.FileWallpaper(var3, var6, new File(var7, var9));
                  this.wallPapers.add(0, this.addedFileWallpaper);
               }
            }
         } else {
            var5 = this.selectedColor;
            if (var5 != 0) {
               var3 = this.selectedBackground;
               if (var3 >= -100L && this.selectedPattern < -1L) {
                  this.addedColorWallpaper = new WallpapersListActivity.ColorWallpaper(var3, var5);
                  this.wallPapers.add(0, this.addedColorWallpaper);
               }
            }
         }

         if (this.selectedBackground == 1000001L) {
            this.wallPapers.add(0, this.catsWallpaper);
         } else {
            this.wallPapers.add(this.catsWallpaper);
         }

         this.updateRows();
      }
   }

   private void fixLayout() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         var1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               WallpapersListActivity.this.fixLayoutInternal();
               if (WallpapersListActivity.this.listView != null) {
                  WallpapersListActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
               }

               return true;
            }
         });
      }

   }

   private void fixLayoutInternal() {
      if (this.getParentActivity() != null) {
         int var1 = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
         if (AndroidUtilities.isTablet()) {
            this.columnsCount = 3;
         } else if (var1 != 3 && var1 != 1) {
            this.columnsCount = 3;
         } else {
            this.columnsCount = 5;
         }

         this.updateRows();
      }
   }

   private long getWallPaperId(Object var1) {
      if (var1 instanceof TLRPC.TL_wallPaper) {
         return ((TLRPC.TL_wallPaper)var1).id;
      } else if (var1 instanceof WallpapersListActivity.ColorWallpaper) {
         return ((WallpapersListActivity.ColorWallpaper)var1).id;
      } else {
         return var1 instanceof WallpapersListActivity.FileWallpaper ? ((WallpapersListActivity.FileWallpaper)var1).id : 0L;
      }
   }

   // $FF: synthetic method
   public static void lambda$KngR6Mye_mVGWrCIie5abe_0Iqw/* $FF was: lambda$KngR6Mye-mVGWrCIie5abe_0Iqw*/(WallpapersListActivity var0) {
      var0.loadWallpapers();
   }

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   private void loadWallpapers() {
      int var1 = this.allWallPapers.size();
      long var2 = 0L;

      int var4;
      for(var4 = 0; var4 < var1; ++var4) {
         Object var5 = this.allWallPapers.get(var4);
         if (var5 instanceof TLRPC.TL_wallPaper) {
            long var6 = ((TLRPC.TL_wallPaper)var5).id;
            int var8 = (int)(var6 >> 32);
            int var9 = (int)var6;
            var2 = ((var2 * 20261L + 2147483648L + (long)var8) % 2147483648L * 20261L + 2147483648L + (long)var9) % 2147483648L;
         }
      }

      TLRPC.TL_account_getWallPapers var10 = new TLRPC.TL_account_getWallPapers();
      var10.hash = (int)var2;
      var4 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var10, new _$$Lambda$WallpapersListActivity$rkTFtQlkaXRQn8loH7KW7DjD08g(this));
      ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var4, super.classGuid);
   }

   private void onItemClick(WallpaperCell var1, Object var2, int var3) {
      if (super.actionBar.isActionModeShowed()) {
         if (!(var2 instanceof TLRPC.TL_wallPaper)) {
            return;
         }

         TLRPC.TL_wallPaper var12 = (TLRPC.TL_wallPaper)var2;
         if (this.selectedWallPapers.indexOfKey(var12.id) >= 0) {
            this.selectedWallPapers.remove(var12.id);
         } else {
            this.selectedWallPapers.put(var12.id, var12);
         }

         if (this.selectedWallPapers.size() == 0) {
            super.actionBar.hideActionMode();
         } else {
            this.selectedMessagesCountTextView.setNumber(this.selectedWallPapers.size(), true);
         }

         this.scrolling = false;
         boolean var4;
         if (this.selectedWallPapers.indexOfKey(var12.id) >= 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         var1.setChecked(var3, var4, true);
      } else {
         long var5 = this.getWallPaperId(var2);
         Object var10 = var2;
         if (var2 instanceof TLRPC.TL_wallPaper) {
            TLRPC.TL_wallPaper var7 = (TLRPC.TL_wallPaper)var2;
            var10 = var2;
            if (var7.pattern) {
               long var8 = var7.id;
               TLRPC.TL_wallPaperSettings var11 = var7.settings;
               var10 = new WallpapersListActivity.ColorWallpaper(var8, var11.background_color, var8, (float)var11.intensity / 100.0F, var11.motion, (File)null);
            }
         }

         WallpaperActivity var13 = new WallpaperActivity(var10, (Bitmap)null);
         if (this.currentType == 1) {
            var13.setDelegate(new _$$Lambda$tPCre3L2K_38M9O_G5mv57D0Uc4(this));
         }

         if (this.selectedBackground == var5) {
            var13.setInitialModes(this.selectedBackgroundBlurred, this.selectedBackgroundMotion);
         }

         var13.setPatterns(this.patterns);
         this.presentFragment(var13);
      }

   }

   private boolean onItemLongClick(WallpaperCell var1, Object var2, int var3) {
      if (!super.actionBar.isActionModeShowed() && this.getParentActivity() != null && var2 instanceof TLRPC.TL_wallPaper) {
         TLRPC.TL_wallPaper var7 = (TLRPC.TL_wallPaper)var2;
         AndroidUtilities.hideKeyboard(this.getParentActivity().getCurrentFocus());
         this.selectedWallPapers.put(var7.id, var7);
         this.selectedMessagesCountTextView.setNumber(1, false);
         AnimatorSet var4 = new AnimatorSet();
         ArrayList var5 = new ArrayList();

         for(int var6 = 0; var6 < this.actionModeViews.size(); ++var6) {
            View var8 = (View)this.actionModeViews.get(var6);
            AndroidUtilities.clearDrawableAnimation(var8);
            var5.add(ObjectAnimator.ofFloat(var8, View.SCALE_Y, new float[]{0.1F, 1.0F}));
         }

         var4.playTogether(var5);
         var4.setDuration(250L);
         var4.start();
         this.scrolling = false;
         super.actionBar.showActionMode();
         var1.setChecked(var3, true, true);
         return true;
      } else {
         return false;
      }
   }

   private void updateRows() {
      this.rowCount = 0;
      int var1;
      if (this.currentType == 0) {
         var1 = this.rowCount++;
         this.uploadImageRow = var1;
         var1 = this.rowCount++;
         this.setColorRow = var1;
         var1 = this.rowCount++;
         this.sectionRow = var1;
      } else {
         this.uploadImageRow = -1;
         this.setColorRow = -1;
         this.sectionRow = -1;
      }

      if (!this.wallPapers.isEmpty()) {
         this.totalWallpaperRows = (int)Math.ceil((double)((float)this.wallPapers.size() / (float)this.columnsCount));
         var1 = this.rowCount;
         this.wallPaperStartRow = var1;
         this.rowCount = var1 + this.totalWallpaperRows;
      } else {
         this.wallPaperStartRow = -1;
      }

      if (this.currentType == 0) {
         var1 = this.rowCount++;
         this.resetSectionRow = var1;
         var1 = this.rowCount++;
         this.resetRow = var1;
         var1 = this.rowCount++;
         this.resetInfoRow = var1;
      } else {
         this.resetSectionRow = -1;
         this.resetRow = -1;
         this.resetInfoRow = -1;
      }

      WallpapersListActivity.ListAdapter var2 = this.listAdapter;
      if (var2 != null) {
         this.scrolling = true;
         var2.notifyDataSetChanged();
      }

   }

   private void updateRowsSelection() {
      int var1 = this.listView.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.listView.getChildAt(var2);
         if (var3 instanceof WallpaperCell) {
            WallpaperCell var5 = (WallpaperCell)var3;

            for(int var4 = 0; var4 < 5; ++var4) {
               var5.setChecked(var4, false, true);
            }
         }
      }

   }

   public View createView(Context var1) {
      this.colorPaint = new Paint(1);
      this.colorFramePaint = new Paint(1);
      this.colorFramePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
      this.colorFramePaint.setStyle(Style.STROKE);
      this.colorFramePaint.setColor(855638016);
      this.updater = new WallpaperUpdater(this.getParentActivity(), this, new WallpaperUpdater.WallpaperUpdaterDelegate() {
         public void didSelectWallpaper(File var1, Bitmap var2, boolean var3) {
            WallpapersListActivity.this.presentFragment(new WallpaperActivity(new WallpapersListActivity.FileWallpaper(-1L, var1, var1), var2), var3);
         }

         public void needOpenColorPicker() {
         }
      });
      super.hasOwnBackground = true;
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      int var2 = this.currentType;
      if (var2 == 0) {
         super.actionBar.setTitle(LocaleController.getString("ChatBackground", 2131559024));
      } else if (var2 == 1) {
         super.actionBar.setTitle(LocaleController.getString("SelectColorTitle", 2131560679));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         // $FF: synthetic method
         public void lambda$null$0$WallpapersListActivity$2(int[] var1) {
            int var10002 = var1[0]--;
            if (var1[0] == 0) {
               WallpapersListActivity.this.loadWallpapers();
            }

         }

         // $FF: synthetic method
         public void lambda$null$1$WallpapersListActivity$2(int[] var1, TLObject var2, TLRPC.TL_error var3) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$WallpapersListActivity$2$O4h5LLObyWcOKyfH3kpnNS7geto(this, var1));
         }

         // $FF: synthetic method
         public void lambda$onItemClick$2$WallpapersListActivity$2(DialogInterface var1, int var2) {
            WallpapersListActivity var8 = WallpapersListActivity.this;
            var8.progressDialog = new AlertDialog(var8.getParentActivity(), 3);
            WallpapersListActivity.this.progressDialog.setCanCacnel(false);
            WallpapersListActivity.this.progressDialog.show();
            new ArrayList();
            int var3 = WallpapersListActivity.this.selectedWallPapers.size();

            for(var2 = 0; var2 < WallpapersListActivity.this.selectedWallPapers.size(); ++var2) {
               TLRPC.TL_wallPaper var4 = (TLRPC.TL_wallPaper)WallpapersListActivity.this.selectedWallPapers.valueAt(var2);
               TLRPC.TL_account_saveWallPaper var9 = new TLRPC.TL_account_saveWallPaper();
               var9.settings = new TLRPC.TL_wallPaperSettings();
               var9.unsave = true;
               TLRPC.TL_inputWallPaper var5 = new TLRPC.TL_inputWallPaper();
               long var6 = var4.id;
               var5.id = var6;
               var5.access_hash = var4.access_hash;
               var9.wallpaper = var5;
               if (var6 == WallpapersListActivity.this.selectedBackground) {
                  WallpapersListActivity.this.selectedBackground = 1000001L;
                  Editor var10 = MessagesController.getGlobalMainSettings().edit();
                  var10.putLong("selectedBackground2", WallpapersListActivity.this.selectedBackground);
                  var10.putBoolean("selectedBackgroundBlurred", false);
                  var10.putBoolean("selectedBackgroundMotion", false);
                  var10.putInt("selectedColor", 0);
                  var10.putFloat("selectedIntensity", 1.0F);
                  var10.putLong("selectedPattern", 0L);
                  var10.putBoolean("overrideThemeWallpaper", true);
                  var10.commit();
                  Theme.reloadWallpaper();
               }

               ConnectionsManager.getInstance(WallpapersListActivity.access$1500(WallpapersListActivity.this)).sendRequest(var9, new _$$Lambda$WallpapersListActivity$2$tLgeSLFOeRml08rU2UfCXZWRhT0(this, new int[]{var3}));
            }

            WallpapersListActivity.this.selectedWallPapers.clear();
            WallpapersListActivity.access$1600(WallpapersListActivity.this).hideActionMode();
            WallpapersListActivity.access$1700(WallpapersListActivity.this).closeSearchField();
         }

         // $FF: synthetic method
         public void lambda$onItemClick$3$WallpapersListActivity$2(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4) {
            StringBuilder var5 = new StringBuilder();
            byte var6 = 0;

            int var7;
            for(var7 = 0; var7 < WallpapersListActivity.this.selectedWallPapers.size(); ++var7) {
               String var8 = AndroidUtilities.getWallPaperUrl((TLRPC.TL_wallPaper)WallpapersListActivity.this.selectedWallPapers.valueAt(var7), WallpapersListActivity.access$400(WallpapersListActivity.this));
               if (!TextUtils.isEmpty(var8)) {
                  if (var5.length() > 0) {
                     var5.append('\n');
                  }

                  var5.append(var8);
               }
            }

            WallpapersListActivity.this.selectedWallPapers.clear();
            WallpapersListActivity.access$500(WallpapersListActivity.this).hideActionMode();
            WallpapersListActivity.access$600(WallpapersListActivity.this).closeSearchField();
            long var9;
            if (var2.size() <= 1 && (Long)var2.get(0) != (long)UserConfig.getInstance(WallpapersListActivity.access$700(WallpapersListActivity.this)).getClientUserId() && var3 == null) {
               var9 = (Long)var2.get(0);
               var7 = (int)var9;
               int var13 = (int)(var9 >> 32);
               Bundle var12 = new Bundle();
               var12.putBoolean("scrollToTopOnResume", true);
               if (var7 != 0) {
                  if (var7 > 0) {
                     var12.putInt("user_id", var7);
                  } else if (var7 < 0) {
                     var12.putInt("chat_id", -var7);
                  }
               } else {
                  var12.putInt("enc_id", var13);
               }

               if (var7 != 0 && !MessagesController.getInstance(WallpapersListActivity.access$1000(WallpapersListActivity.this)).checkCanOpenChat(var12, var1)) {
                  return;
               }

               NotificationCenter.getInstance(WallpapersListActivity.access$1100(WallpapersListActivity.this)).postNotificationName(NotificationCenter.closeChats);
               ChatActivity var11 = new ChatActivity(var12);
               WallpapersListActivity.this.presentFragment(var11, true);
               SendMessagesHelper.getInstance(WallpapersListActivity.access$1200(WallpapersListActivity.this)).sendMessage(var5.toString(), var9, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
            } else {
               WallpapersListActivity.this.updateRowsSelection();

               for(var7 = var6; var7 < var2.size(); ++var7) {
                  var9 = (Long)var2.get(var7);
                  if (var3 != null) {
                     SendMessagesHelper.getInstance(WallpapersListActivity.access$800(WallpapersListActivity.this)).sendMessage(var3.toString(), var9, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
                  }

                  SendMessagesHelper.getInstance(WallpapersListActivity.access$900(WallpapersListActivity.this)).sendMessage(var5.toString(), var9, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
               }

               var1.finishFragment();
            }

         }

         public void onItemClick(int var1) {
            if (var1 == -1) {
               if (WallpapersListActivity.access$000(WallpapersListActivity.this).isActionModeShowed()) {
                  WallpapersListActivity.this.selectedWallPapers.clear();
                  WallpapersListActivity.access$200(WallpapersListActivity.this).hideActionMode();
                  WallpapersListActivity.this.updateRowsSelection();
               } else {
                  WallpapersListActivity.this.finishFragment();
               }
            } else if (var1 == 4) {
               if (WallpapersListActivity.this.getParentActivity() == null) {
                  return;
               }

               AlertDialog.Builder var2 = new AlertDialog.Builder(WallpapersListActivity.this.getParentActivity());
               var2.setMessage(LocaleController.formatString("DeleteChatBackgroundsAlert", 2131559239));
               var2.setTitle(LocaleController.getString("AppName", 2131558635));
               var2.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$WallpapersListActivity$2$K8aHn505ku1qQXr9dNGqIjZPpyE(this));
               var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               WallpapersListActivity.this.showDialog(var2.create());
            } else if (var1 == 3) {
               Bundle var3 = new Bundle();
               var3.putBoolean("onlySelect", true);
               var3.putInt("dialogsType", 3);
               DialogsActivity var4 = new DialogsActivity(var3);
               var4.setDelegate(new _$$Lambda$WallpapersListActivity$2$_pR5kSEFy3SmzguvrtK_EnN_KAw(this));
               WallpapersListActivity.this.presentFragment(var4);
            }

         }
      });
      if (this.currentType == 0) {
         this.searchItem = super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            public void onCaptionCleared() {
               WallpapersListActivity.this.searchAdapter.clearColor();
               WallpapersListActivity.this.searchItem.setSearchFieldHint(LocaleController.getString("SearchBackgrounds", 2131560641));
            }

            public void onSearchCollapse() {
               WallpapersListActivity.this.listView.setAdapter(WallpapersListActivity.this.listAdapter);
               WallpapersListActivity.this.listView.invalidate();
               WallpapersListActivity.this.searchAdapter.processSearch((String)null, true);
               WallpapersListActivity.this.searchItem.setSearchFieldCaption((CharSequence)null);
               this.onCaptionCleared();
            }

            public void onSearchExpand() {
               WallpapersListActivity.this.listView.setAdapter(WallpapersListActivity.this.searchAdapter);
               WallpapersListActivity.this.listView.invalidate();
            }

            public void onTextChanged(EditText var1) {
               WallpapersListActivity.this.searchAdapter.processSearch(var1.getText().toString(), false);
            }
         });
         this.searchItem.setSearchFieldHint(LocaleController.getString("SearchBackgrounds", 2131560641));
         ActionBarMenu var3 = super.actionBar.createActionMode(false);
         var3.setBackgroundColor(Theme.getColor("actionBarDefault"));
         super.actionBar.setItemsColor(Theme.getColor("actionBarDefaultIcon"), true);
         super.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarDefaultSelector"), true);
         this.selectedMessagesCountTextView = new NumberTextView(var3.getContext());
         this.selectedMessagesCountTextView.setTextSize(18);
         this.selectedMessagesCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.selectedMessagesCountTextView.setTextColor(Theme.getColor("actionBarDefaultIcon"));
         this.selectedMessagesCountTextView.setOnTouchListener(_$$Lambda$WallpapersListActivity$kDCUFe0ixQyVZvdd5on4Sg4XaNc.INSTANCE);
         var3.addView(this.selectedMessagesCountTextView, LayoutHelper.createLinear(0, -1, 1.0F, 65, 0, 0, 0));
         this.actionModeViews.add(var3.addItemWithWidth(3, 2131165627, AndroidUtilities.dp(54.0F)));
         this.actionModeViews.add(var3.addItemWithWidth(4, 2131165623, AndroidUtilities.dp(54.0F)));
         this.selectedWallPapers.clear();
      }

      super.fragmentView = new FrameLayout(var1);
      FrameLayout var6 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1) {
         private Paint paint = new Paint();

         public boolean hasOverlappingRendering() {
            return false;
         }

         public void onDraw(Canvas var1) {
            RecyclerView.ViewHolder var2;
            if (this.getAdapter() == WallpapersListActivity.this.listAdapter && WallpapersListActivity.this.resetInfoRow != -1) {
               var2 = this.findViewHolderForAdapterPosition(WallpapersListActivity.this.resetInfoRow);
            } else {
               var2 = null;
            }

            int var3;
            int var4;
            label19: {
               var3 = this.getMeasuredHeight();
               if (var2 != null) {
                  var4 = var2.itemView.getBottom();
                  if (var2.itemView.getBottom() < var3) {
                     break label19;
                  }
               }

               var4 = var3;
            }

            this.paint.setColor(Theme.getColor("windowBackgroundWhite"));
            float var5 = (float)this.getMeasuredWidth();
            float var6 = (float)var4;
            var1.drawRect(0.0F, 0.0F, var5, var6, this.paint);
            if (var4 != var3) {
               this.paint.setColor(Theme.getColor("windowBackgroundGray"));
               var1.drawRect(0.0F, var6, (float)this.getMeasuredWidth(), (float)var3, this.paint);
            }

         }
      };
      this.listView.setClipToPadding(false);
      this.listView.setHorizontalScrollBarEnabled(false);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      RecyclerListView var4 = this.listView;
      LinearLayoutManager var5 = new LinearLayoutManager(var1, 1, false) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.layoutManager = var5;
      var4.setLayoutManager(var5);
      var6.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      var4 = this.listView;
      WallpapersListActivity.ListAdapter var7 = new WallpapersListActivity.ListAdapter(var1);
      this.listAdapter = var7;
      var4.setAdapter(var7);
      this.searchAdapter = new WallpapersListActivity.SearchAdapter(var1);
      this.listView.setGlowColor(Theme.getColor("avatar_backgroundActionBarBlue"));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$WallpapersListActivity$PdMdfLigg_iWQcF5CiT4cSCfNQ8(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            boolean var3 = true;
            if (var2 == 1) {
               AndroidUtilities.hideKeyboard(WallpapersListActivity.this.getParentActivity().getCurrentFocus());
            }

            WallpapersListActivity var4 = WallpapersListActivity.this;
            if (var2 == 0) {
               var3 = false;
            }

            var4.scrolling = var3;
         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            if (WallpapersListActivity.this.listView.getAdapter() == WallpapersListActivity.this.searchAdapter) {
               var3 = WallpapersListActivity.this.layoutManager.findFirstVisibleItemPosition();
               if (var3 == -1) {
                  var2 = 0;
               } else {
                  var2 = Math.abs(WallpapersListActivity.this.layoutManager.findLastVisibleItemPosition() - var3) + 1;
               }

               if (var2 > 0) {
                  int var4 = WallpapersListActivity.this.layoutManager.getItemCount();
                  if (var2 != 0 && var3 + var2 > var4 - 2) {
                     WallpapersListActivity.this.searchAdapter.loadMoreResults();
                  }
               }
            }

         }
      });
      this.searchEmptyView = new EmptyTextProgressView(var1);
      this.searchEmptyView.setVisibility(8);
      this.searchEmptyView.setShowAtCenter(true);
      this.searchEmptyView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.searchEmptyView.setText(LocaleController.getString("NoResult", 2131559943));
      this.listView.setEmptyView(this.searchEmptyView);
      var6.addView(this.searchEmptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.updateRows();
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.wallpapersDidLoad) {
         var1 = 0;
         ArrayList var5 = (ArrayList)var3[0];
         this.patterns.clear();
         if (this.currentType != 1) {
            this.wallPapers.clear();
            this.allWallPapers.clear();
            this.allWallPapersDict.clear();
            this.allWallPapers.addAll(var5);
         }

         for(var2 = var5.size(); var1 < var2; ++var1) {
            TLRPC.TL_wallPaper var4 = (TLRPC.TL_wallPaper)var5.get(var1);
            if (var4.pattern) {
               this.patterns.add(var4);
            }

            if (this.currentType != 1 && (!var4.pattern || var4.settings != null)) {
               this.allWallPapersDict.put(var4.id, var4);
               this.wallPapers.add(var4);
            }
         }

         this.selectedBackground = Theme.getSelectedBackgroundId();
         this.fillWallpapersWithCustom();
         this.loadWallpapers();
      } else if (var1 == NotificationCenter.didSetNewWallpapper) {
         RecyclerListView var6 = this.listView;
         if (var6 != null) {
            var6.invalidateViews();
         }

         ActionBar var7 = super.actionBar;
         if (var7 != null) {
            var7.closeSearchField();
         }
      } else if (var1 == NotificationCenter.wallpapersNeedReload) {
         MessagesStorage.getInstance(super.currentAccount).getWallpapers();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"), new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_graySectionText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "graySection"), new ThemeDescription(this.searchEmptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder"), new ThemeDescription(this.searchEmptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.searchEmptyView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite")};
   }

   // $FF: synthetic method
   public void lambda$createView$3$WallpapersListActivity(View var1, int var2) {
      if (this.getParentActivity() != null && this.listView.getAdapter() != this.searchAdapter) {
         if (var2 == this.uploadImageRow) {
            this.updater.openGallery();
         } else if (var2 == this.setColorRow) {
            WallpapersListActivity var3 = new WallpapersListActivity(1);
            var3.patterns = this.patterns;
            this.presentFragment(var3);
         } else if (var2 == this.resetRow) {
            AlertDialog.Builder var4 = new AlertDialog.Builder(this.getParentActivity());
            var4.setTitle(LocaleController.getString("ResetChatBackgroundsAlertTitle", 2131560595));
            var4.setMessage(LocaleController.getString("ResetChatBackgroundsAlert", 2131560594));
            var4.setPositiveButton(LocaleController.getString("Reset", 2131560583), new _$$Lambda$WallpapersListActivity$IP2pJT3LolM38wDLb7Brfxjps6Q(this));
            var4.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            AlertDialog var5 = var4.create();
            this.showDialog(var5);
            TextView var6 = (TextView)var5.getButton(-1);
            if (var6 != null) {
               var6.setTextColor(Theme.getColor("dialogTextRed2"));
            }
         }
      }

   }

   // $FF: synthetic method
   public int lambda$fillWallpapersWithCustom$6$WallpapersListActivity(boolean var1, Object var2, Object var3) {
      if (var2 instanceof TLRPC.TL_wallPaper && var3 instanceof TLRPC.TL_wallPaper) {
         TLRPC.TL_wallPaper var10 = (TLRPC.TL_wallPaper)var2;
         TLRPC.TL_wallPaper var11 = (TLRPC.TL_wallPaper)var3;
         long var4 = var10.id;
         long var6 = this.selectedBackground;
         if (var4 == var6) {
            return -1;
         } else if (var11.id == var6) {
            return 1;
         } else {
            int var8 = this.allWallPapers.indexOf(var10);
            int var9 = this.allWallPapers.indexOf(var11);
            if (var10.dark && var11.dark || !var10.dark && !var11.dark) {
               if (var8 > var9) {
                  return 1;
               } else {
                  return var8 < var9 ? -1 : 0;
               }
            } else if (var10.dark && !var11.dark) {
               return var1 ? -1 : 1;
            } else {
               return var1 ? 1 : -1;
            }
         }
      } else {
         return 0;
      }
   }

   // $FF: synthetic method
   public void lambda$loadWallpapers$5$WallpapersListActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$WallpapersListActivity$oC7eCMcO7bYehKvUV1pnkpGiLa0(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$1$WallpapersListActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$WallpapersListActivity$KngR6Mye_mVGWrCIie5abe_0Iqw(this));
   }

   // $FF: synthetic method
   public void lambda$null$2$WallpapersListActivity(DialogInterface var1, int var2) {
      if (super.actionBar.isActionModeShowed()) {
         this.selectedWallPapers.clear();
         super.actionBar.hideActionMode();
         this.updateRowsSelection();
      }

      this.progressDialog = new AlertDialog(this.getParentActivity(), 3);
      this.progressDialog.setCanCacnel(false);
      this.progressDialog.show();
      TLRPC.TL_account_resetWallPapers var3 = new TLRPC.TL_account_resetWallPapers();
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$WallpapersListActivity$YGDNH55IuocBWL16sPri4eLBLwQ(this));
   }

   // $FF: synthetic method
   public void lambda$null$4$WallpapersListActivity(TLObject var1) {
      if (var1 instanceof TLRPC.TL_account_wallPapers) {
         TLRPC.TL_account_wallPapers var2 = (TLRPC.TL_account_wallPapers)var1;
         this.patterns.clear();
         if (this.currentType != 1) {
            this.wallPapers.clear();
            this.allWallPapersDict.clear();
            this.allWallPapers.clear();
            this.allWallPapers.addAll(var2.wallpapers);
         }

         int var3 = var2.wallpapers.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            TLRPC.TL_wallPaper var5 = (TLRPC.TL_wallPaper)var2.wallpapers.get(var4);
            this.allWallPapersDict.put(var5.id, var5);
            if (var5.pattern) {
               this.patterns.add(var5);
            }

            if (this.currentType != 1 && (!var5.pattern || var5.settings != null)) {
               this.wallPapers.add(var5);
            }
         }

         this.fillWallpapersWithCustom();
         MessagesStorage.getInstance(super.currentAccount).putWallpapers(var2.wallpapers, 1);
      }

      AlertDialog var6 = this.progressDialog;
      if (var6 != null) {
         var6.dismiss();
         this.listView.smoothScrollToPosition(0);
      }

   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
      this.updater.onActivityResult(var1, var2, var3);
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.fixLayout();
   }

   public boolean onFragmentCreate() {
      if (this.currentType == 0) {
         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersDidLoad);
         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersNeedReload);
         MessagesStorage.getInstance(super.currentAccount).getWallpapers();
      } else {
         int var1 = 0;

         while(true) {
            int[] var2 = defaultColors;
            if (var1 >= var2.length) {
               if (this.currentType == 1 && this.patterns.isEmpty()) {
                  NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersDidLoad);
                  MessagesStorage.getInstance(super.currentAccount).getWallpapers();
               }
               break;
            }

            this.wallPapers.add(new WallpapersListActivity.ColorWallpaper((long)(-(var1 + 3)), var2[var1]));
            ++var1;
         }
      }

      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      int var1 = this.currentType;
      if (var1 == 0) {
         this.searchAdapter.onDestroy();
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersDidLoad);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersNeedReload);
      } else if (var1 == 1) {
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersDidLoad);
      }

      this.updater.cleanup();
      super.onFragmentDestroy();
   }

   public void onResume() {
      super.onResume();
      SharedPreferences var1 = MessagesController.getGlobalMainSettings();
      this.selectedBackground = Theme.getSelectedBackgroundId();
      this.selectedPattern = var1.getLong("selectedPattern", 0L);
      this.selectedColor = var1.getInt("selectedColor", 0);
      this.selectedIntensity = var1.getFloat("selectedIntensity", 1.0F);
      this.selectedBackgroundMotion = var1.getBoolean("selectedBackgroundMotion", false);
      this.selectedBackgroundBlurred = var1.getBoolean("selectedBackgroundBlurred", false);
      this.fillWallpapersWithCustom();
      this.fixLayout();
   }

   public void restoreSelfArgs(Bundle var1) {
      this.updater.setCurrentPicturePath(var1.getString("path"));
   }

   public void saveSelfArgs(Bundle var1) {
      String var2 = this.updater.getCurrentPicturePath();
      if (var2 != null) {
         var1.putString("path", var2);
      }

   }

   private class ColorCell extends View {
      private int color;

      public ColorCell(Context var2) {
         super(var2);
      }

      protected void onDraw(Canvas var1) {
         WallpapersListActivity.this.colorPaint.setColor(this.color);
         var1.drawCircle((float)AndroidUtilities.dp(25.0F), (float)AndroidUtilities.dp(31.0F), (float)AndroidUtilities.dp(18.0F), WallpapersListActivity.this.colorPaint);
         if (this.color == Theme.getColor("windowBackgroundWhite")) {
            var1.drawCircle((float)AndroidUtilities.dp(25.0F), (float)AndroidUtilities.dp(31.0F), (float)AndroidUtilities.dp(18.0F), WallpapersListActivity.this.colorFramePaint);
         }

      }

      protected void onMeasure(int var1, int var2) {
         this.setMeasuredDimension(AndroidUtilities.dp(50.0F), AndroidUtilities.dp(62.0F));
      }

      public void setColor(int var1) {
         this.color = var1;
      }
   }

   public static class ColorWallpaper {
      public int color;
      public long id;
      public float intensity;
      public boolean motion;
      public File path;
      public TLRPC.TL_wallPaper pattern;
      public long patternId;

      public ColorWallpaper(long var1, int var3) {
         this.id = var1;
         this.color = -16777216 | var3;
         this.intensity = 1.0F;
      }

      public ColorWallpaper(long var1, int var3, long var4, float var6, boolean var7, File var8) {
         this.id = var1;
         this.color = -16777216 | var3;
         this.patternId = var4;
         this.intensity = var6;
         this.path = var8;
         this.motion = var7;
      }
   }

   public static class FileWallpaper {
      public long id;
      public File originalPath;
      public File path;
      public int resId;
      public int thumbResId;

      public FileWallpaper(long var1, int var3, int var4) {
         this.id = var1;
         this.resId = var3;
         this.thumbResId = var4;
      }

      public FileWallpaper(long var1, File var3, File var4) {
         this.id = var1;
         this.path = var3;
         this.originalPath = var4;
      }

      public FileWallpaper(long var1, String var3) {
         this.id = var1;
         this.path = new File(var3);
      }
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return WallpapersListActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != WallpapersListActivity.this.uploadImageRow && var1 != WallpapersListActivity.this.setColorRow && var1 != WallpapersListActivity.this.resetRow) {
            if (var1 != WallpapersListActivity.this.sectionRow && var1 != WallpapersListActivity.this.resetSectionRow) {
               return var1 == WallpapersListActivity.this.resetInfoRow ? 3 : 2;
            } else {
               return 1;
            }
         } else {
            return 0;
         }
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
         if (var3 != 0) {
            if (var3 != 2) {
               if (var3 == 3) {
                  TextInfoPrivacyCell var10 = (TextInfoPrivacyCell)var1.itemView;
                  if (var2 == WallpapersListActivity.this.resetInfoRow) {
                     var10.setText(LocaleController.getString("ResetChatBackgroundsInfo", 2131560596));
                  }
               }
            } else {
               WallpaperCell var4 = (WallpaperCell)var1.itemView;
               var3 = (var2 - WallpapersListActivity.this.wallPaperStartRow) * WallpapersListActivity.this.columnsCount;
               var2 = WallpapersListActivity.this.columnsCount;
               boolean var5;
               if (var3 == 0) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               boolean var6;
               if (var3 / WallpapersListActivity.this.columnsCount == WallpapersListActivity.this.totalWallpaperRows - 1) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               var4.setParams(var2, var5, var6);

               for(var2 = 0; var2 < WallpapersListActivity.this.columnsCount; ++var2) {
                  int var7 = var3 + var2;
                  Object var11;
                  if (var7 < WallpapersListActivity.this.wallPapers.size()) {
                     var11 = WallpapersListActivity.this.wallPapers.get(var7);
                  } else {
                     var11 = null;
                  }

                  var4.setWallpaper(WallpapersListActivity.this.currentType, var2, var11, WallpapersListActivity.this.selectedBackground, (Drawable)null, false);
                  long var8;
                  if (var11 instanceof TLRPC.TL_wallPaper) {
                     var8 = ((TLRPC.TL_wallPaper)var11).id;
                  } else {
                     var8 = 0L;
                  }

                  if (WallpapersListActivity.access$6100(WallpapersListActivity.this).isActionModeShowed()) {
                     if (WallpapersListActivity.this.selectedWallPapers.indexOfKey(var8) >= 0) {
                        var5 = true;
                     } else {
                        var5 = false;
                     }

                     var4.setChecked(var2, var5, WallpapersListActivity.this.scrolling ^ true);
                  } else {
                     var4.setChecked(var2, false, WallpapersListActivity.this.scrolling ^ true);
                  }
               }
            }
         } else {
            TextCell var12 = (TextCell)var1.itemView;
            if (var2 == WallpapersListActivity.this.uploadImageRow) {
               var12.setTextAndIcon(LocaleController.getString("SelectFromGallery", 2131560683), 2131165792, true);
            } else if (var2 == WallpapersListActivity.this.setColorRow) {
               var12.setTextAndIcon(LocaleController.getString("SetColor", 2131560733), 2131165589, false);
            } else if (var2 == WallpapersListActivity.this.resetRow) {
               var12.setText(LocaleController.getString("ResetChatBackgrounds", 2131560593), false);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var5;
         if (var2 != 0) {
            int var3 = 2131165395;
            Drawable var4;
            CombinedDrawable var6;
            if (var2 != 1) {
               if (var2 != 3) {
                  var5 = new WallpaperCell(this.mContext) {
                     protected void onWallpaperClick(Object var1, int var2) {
                        WallpapersListActivity.this.onItemClick(this, var1, var2);
                     }

                     protected boolean onWallpaperLongClick(Object var1, int var2) {
                        return WallpapersListActivity.this.onItemLongClick(this, var1, var2);
                     }
                  };
               } else {
                  var5 = new TextInfoPrivacyCell(this.mContext);
                  var4 = Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow");
                  var6 = new CombinedDrawable(new ColorDrawable(Theme.getColor("windowBackgroundGray")), var4);
                  var6.setFullsize(true);
                  ((View)var5).setBackgroundDrawable(var6);
               }
            } else {
               var5 = new ShadowSectionCell(this.mContext);
               Context var7 = this.mContext;
               if (WallpapersListActivity.this.wallPaperStartRow == -1) {
                  var2 = var3;
               } else {
                  var2 = 2131165394;
               }

               var4 = Theme.getThemedDrawable(var7, var2, "windowBackgroundGrayShadow");
               var6 = new CombinedDrawable(new ColorDrawable(Theme.getColor("windowBackgroundGray")), var4);
               var6.setFullsize(true);
               ((View)var5).setBackgroundDrawable(var6);
            }
         } else {
            var5 = new TextCell(this.mContext);
         }

         return new RecyclerListView.Holder((View)var5);
      }
   }

   private class SearchAdapter extends RecyclerListView.SelectionAdapter {
      private boolean bingSearchEndReached = true;
      private int imageReqId;
      private RecyclerListView innerListView;
      private String lastSearchImageString;
      private String lastSearchString;
      private int lastSearchToken;
      private Context mContext;
      private String nextImagesSearchOffset;
      private ArrayList searchResult = new ArrayList();
      private HashMap searchResultKeys = new HashMap();
      private Runnable searchRunnable;
      private boolean searchingUser;
      private String selectedColor;

      public SearchAdapter(Context var2) {
         this.mContext = var2;
      }

      private void doSearch(String var1) {
         this.searchResult.clear();
         this.searchResultKeys.clear();
         this.bingSearchEndReached = true;
         this.searchImages(var1, "", true);
         this.lastSearchString = var1;
         this.notifyDataSetChanged();
      }

      private void processSearch(String var1, boolean var2) {
         String var3 = var1;
         if (var1 != null) {
            var3 = var1;
            if (this.selectedColor != null) {
               StringBuilder var5 = new StringBuilder();
               var5.append("#color");
               var5.append(this.selectedColor);
               var5.append(" ");
               var5.append(var1);
               var3 = var5.toString();
            }
         }

         Runnable var4 = this.searchRunnable;
         if (var4 != null) {
            AndroidUtilities.cancelRunOnUIThread(var4);
            this.searchRunnable = null;
         }

         if (TextUtils.isEmpty(var3)) {
            this.searchResult.clear();
            this.searchResultKeys.clear();
            this.bingSearchEndReached = true;
            this.lastSearchString = null;
            if (this.imageReqId != 0) {
               ConnectionsManager.getInstance(WallpapersListActivity.access$3300(WallpapersListActivity.this)).cancelRequest(this.imageReqId, true);
               this.imageReqId = 0;
            }

            WallpapersListActivity.this.searchEmptyView.showTextView();
         } else {
            WallpapersListActivity.this.searchEmptyView.showProgress();
            if (var2) {
               this.doSearch(var3);
            } else {
               this.searchRunnable = new _$$Lambda$WallpapersListActivity$SearchAdapter$A_mUNZ6ShjO2kmNuqCpt4p0OR_4(this, var3);
               AndroidUtilities.runOnUIThread(this.searchRunnable, 500L);
            }
         }

         this.notifyDataSetChanged();
      }

      private void searchBotUser() {
         if (!this.searchingUser) {
            this.searchingUser = true;
            TLRPC.TL_contacts_resolveUsername var1 = new TLRPC.TL_contacts_resolveUsername();
            var1.username = MessagesController.getInstance(WallpapersListActivity.access$3500(WallpapersListActivity.this)).imageSearchBot;
            ConnectionsManager.getInstance(WallpapersListActivity.access$3600(WallpapersListActivity.this)).sendRequest(var1, new _$$Lambda$WallpapersListActivity$SearchAdapter$4mWogDs9zLmdvnE5jd5DGo_Q4ak(this));
         }
      }

      private void searchImages(String var1, String var2, boolean var3) {
         if (this.imageReqId != 0) {
            ConnectionsManager.getInstance(WallpapersListActivity.access$3700(WallpapersListActivity.this)).cancelRequest(this.imageReqId, true);
            this.imageReqId = 0;
         }

         this.lastSearchImageString = var1;
         TLObject var4 = MessagesController.getInstance(WallpapersListActivity.access$3900(WallpapersListActivity.this)).getUserOrChat(MessagesController.getInstance(WallpapersListActivity.access$3800(WallpapersListActivity.this)).imageSearchBot);
         if (!(var4 instanceof TLRPC.User)) {
            if (var3) {
               this.searchBotUser();
            }

         } else {
            TLRPC.User var5 = (TLRPC.User)var4;
            TLRPC.TL_messages_getInlineBotResults var8 = new TLRPC.TL_messages_getInlineBotResults();
            StringBuilder var6 = new StringBuilder();
            var6.append("#wallpaper ");
            var6.append(var1);
            var8.query = var6.toString();
            var8.bot = MessagesController.getInstance(WallpapersListActivity.access$4000(WallpapersListActivity.this)).getInputUser(var5);
            var8.offset = var2;
            var8.peer = new TLRPC.TL_inputPeerEmpty();
            int var7 = this.lastSearchToken + 1;
            this.lastSearchToken = var7;
            this.imageReqId = ConnectionsManager.getInstance(WallpapersListActivity.access$4100(WallpapersListActivity.this)).sendRequest(var8, new _$$Lambda$WallpapersListActivity$SearchAdapter$EUxupb60ZwBIv1eg9iblwmPpQ9k(this, var7));
            ConnectionsManager.getInstance(WallpapersListActivity.access$4300(WallpapersListActivity.this)).bindRequestToGuid(this.imageReqId, WallpapersListActivity.access$4200(WallpapersListActivity.this));
         }
      }

      public void clearColor() {
         this.selectedColor = null;
         this.processSearch((String)null, true);
      }

      public RecyclerListView getInnerListView() {
         return this.innerListView;
      }

      public int getItemCount() {
         return TextUtils.isEmpty(this.lastSearchString) ? 2 : (int)Math.ceil((double)((float)this.searchResult.size() / (float)WallpapersListActivity.this.columnsCount));
      }

      public int getItemViewType(int var1) {
         if (TextUtils.isEmpty(this.lastSearchString)) {
            return var1 == 0 ? 2 : 1;
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getItemViewType() != 2) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      // $FF: synthetic method
      public void lambda$null$1$WallpapersListActivity$SearchAdapter(TLObject var1) {
         TLRPC.TL_contacts_resolvedPeer var2 = (TLRPC.TL_contacts_resolvedPeer)var1;
         MessagesController.getInstance(WallpapersListActivity.access$4900(WallpapersListActivity.this)).putUsers(var2.users, false);
         MessagesController.getInstance(WallpapersListActivity.access$5000(WallpapersListActivity.this)).putChats(var2.chats, false);
         MessagesStorage.getInstance(WallpapersListActivity.access$5100(WallpapersListActivity.this)).putUsersAndChats(var2.users, var2.chats, true, true);
         String var3 = this.lastSearchImageString;
         this.lastSearchImageString = null;
         this.searchImages(var3, "", false);
      }

      // $FF: synthetic method
      public void lambda$null$3$WallpapersListActivity$SearchAdapter(int var1, TLObject var2) {
         if (var1 == this.lastSearchToken) {
            boolean var3 = false;
            this.imageReqId = 0;
            int var4 = this.searchResult.size();
            int var10;
            if (var2 != null) {
               TLRPC.messages_BotResults var5 = (TLRPC.messages_BotResults)var2;
               this.nextImagesSearchOffset = var5.next_offset;
               int var6 = var5.results.size();

               for(var1 = 0; var1 < var6; ++var1) {
                  TLRPC.BotInlineResult var7 = (TLRPC.BotInlineResult)var5.results.get(var1);
                  if ("photo".equals(var7.type) && !this.searchResultKeys.containsKey(var7.id)) {
                     MediaController.SearchImage var12 = new MediaController.SearchImage();
                     TLRPC.Photo var8 = var7.photo;
                     if (var8 != null) {
                        TLRPC.PhotoSize var13 = FileLoader.getClosestPhotoSizeWithSize(var8.sizes, AndroidUtilities.getPhotoSize());
                        TLRPC.PhotoSize var9 = FileLoader.getClosestPhotoSizeWithSize(var7.photo.sizes, 320);
                        if (var13 == null) {
                           continue;
                        }

                        var12.width = var13.w;
                        var12.height = var13.h;
                        var12.photoSize = var13;
                        var12.photo = var7.photo;
                        var12.size = var13.size;
                        var12.thumbPhotoSize = var9;
                     } else {
                        if (var7.content == null) {
                           continue;
                        }

                        for(var10 = 0; var10 < var7.content.attributes.size(); ++var10) {
                           TLRPC.DocumentAttribute var14 = (TLRPC.DocumentAttribute)var7.content.attributes.get(var10);
                           if (var14 instanceof TLRPC.TL_documentAttributeImageSize) {
                              var12.width = var14.w;
                              var12.height = var14.h;
                              break;
                           }
                        }

                        TLRPC.WebDocument var15 = var7.thumb;
                        if (var15 != null) {
                           var12.thumbUrl = var15.url;
                        } else {
                           var12.thumbUrl = null;
                        }

                        var15 = var7.content;
                        var12.imageUrl = var15.url;
                        var12.size = var15.size;
                     }

                     var12.id = var7.id;
                     var12.type = 0;
                     var12.localUrl = "";
                     this.searchResult.add(var12);
                     this.searchResultKeys.put(var12.id, var12);
                  }
               }

               if (var4 == this.searchResult.size() || this.nextImagesSearchOffset == null) {
                  var3 = true;
               }

               this.bingSearchEndReached = var3;
            }

            if (var4 != this.searchResult.size()) {
               var10 = WallpapersListActivity.this.columnsCount;
               float var11 = (float)var4;
               var1 = (int)Math.ceil((double)(var11 / (float)WallpapersListActivity.this.columnsCount));
               if (var4 % var10 != 0) {
                  this.notifyItemChanged((int)Math.ceil((double)(var11 / (float)WallpapersListActivity.this.columnsCount)) - 1);
               }

               var10 = (int)Math.ceil((double)((float)this.searchResult.size() / (float)WallpapersListActivity.this.columnsCount));
               WallpapersListActivity.this.searchAdapter.notifyItemRangeInserted(var1, var10 - var1);
            }

            WallpapersListActivity.this.searchEmptyView.showTextView();
         }
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$5$WallpapersListActivity$SearchAdapter(View var1, int var2) {
         String var4 = LocaleController.getString("BackgroundSearchColor", 2131558825);
         StringBuilder var3 = new StringBuilder();
         var3.append(var4);
         var3.append(" ");
         var3.append(LocaleController.getString(WallpapersListActivity.searchColorsNames[var2], WallpapersListActivity.searchColorsNamesR[var2]));
         SpannableString var5 = new SpannableString(var3.toString());
         var5.setSpan(new ForegroundColorSpan(Theme.getColor("actionBarDefaultSubtitle")), var4.length(), var5.length(), 33);
         WallpapersListActivity.this.searchItem.setSearchFieldCaption(var5);
         WallpapersListActivity.this.searchItem.setSearchFieldHint((CharSequence)null);
         WallpapersListActivity.this.searchItem.setSearchFieldText("", true);
         this.selectedColor = WallpapersListActivity.searchColorsNames[var2];
         this.processSearch("", true);
      }

      // $FF: synthetic method
      public void lambda$processSearch$0$WallpapersListActivity$SearchAdapter(String var1) {
         this.doSearch(var1);
         this.searchRunnable = null;
      }

      // $FF: synthetic method
      public void lambda$searchBotUser$2$WallpapersListActivity$SearchAdapter(TLObject var1, TLRPC.TL_error var2) {
         if (var1 != null) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$WallpapersListActivity$SearchAdapter$pRImZ4AMa9Bj9s9qQH5_U_q_hcU(this, var1));
         }

      }

      // $FF: synthetic method
      public void lambda$searchImages$4$WallpapersListActivity$SearchAdapter(int var1, TLObject var2, TLRPC.TL_error var3) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$WallpapersListActivity$SearchAdapter$pzB92okjUzYhICjYxXfrmR5fs8g(this, var1, var2));
      }

      public void loadMoreResults() {
         if (!this.bingSearchEndReached && this.imageReqId == 0) {
            this.searchImages(this.lastSearchString, this.nextImagesSearchOffset, true);
         }

      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 == 2) {
               ((GraySectionCell)var1.itemView).setText(LocaleController.getString("SearchByColor", 2131560642));
            }
         } else {
            WallpaperCell var4 = (WallpaperCell)var1.itemView;
            var3 = var2 * WallpapersListActivity.this.columnsCount;
            int var5 = (int)Math.ceil((double)((float)this.searchResult.size() / (float)WallpapersListActivity.this.columnsCount));
            var2 = WallpapersListActivity.this.columnsCount;
            boolean var6 = true;
            boolean var7;
            if (var3 == 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            if (var3 / WallpapersListActivity.this.columnsCount != var5 - 1) {
               var6 = false;
            }

            var4.setParams(var2, var7, var6);

            for(var2 = 0; var2 < WallpapersListActivity.this.columnsCount; ++var2) {
               var5 = var3 + var2;
               Object var8;
               if (var5 < this.searchResult.size()) {
                  var8 = this.searchResult.get(var5);
               } else {
                  var8 = null;
               }

               var4.setWallpaper(WallpapersListActivity.this.currentType, var2, var8, 0L, (Drawable)null, false);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4 = null;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 == 2) {
                  var4 = new GraySectionCell(this.mContext);
               }
            } else {
               var4 = new RecyclerListView(this.mContext) {
                  public boolean onInterceptTouchEvent(MotionEvent var1) {
                     if (this.getParent() != null && this.getParent().getParent() != null) {
                        this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                     }

                     return super.onInterceptTouchEvent(var1);
                  }
               };
               ((RecyclerView)var4).setItemAnimator((RecyclerView.ItemAnimator)null);
               ((ViewGroup)var4).setLayoutAnimation((LayoutAnimationController)null);
               LinearLayoutManager var3 = new LinearLayoutManager(this.mContext) {
                  public boolean supportsPredictiveItemAnimations() {
                     return false;
                  }
               };
               ((ViewGroup)var4).setPadding(AndroidUtilities.dp(7.0F), 0, AndroidUtilities.dp(7.0F), 0);
               ((RecyclerView)var4).setClipToPadding(false);
               var3.setOrientation(0);
               ((RecyclerView)var4).setLayoutManager(var3);
               ((RecyclerListView)var4).setAdapter(new WallpapersListActivity.SearchAdapter.CategoryAdapterRecycler());
               ((RecyclerListView)var4).setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$WallpapersListActivity$SearchAdapter$oLQrjTlnoZYzJvyN_dx2q_irBAw(this)));
               this.innerListView = (RecyclerListView)var4;
            }
         } else {
            var4 = new WallpaperCell(this.mContext) {
               protected void onWallpaperClick(Object var1, int var2) {
                  WallpapersListActivity.this.presentFragment(new WallpaperActivity(var1, (Bitmap)null));
               }
            };
         }

         if (var2 == 1) {
            ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(60.0F)));
         } else {
            ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         }

         return new RecyclerListView.Holder((View)var4);
      }

      public void onDestroy() {
         if (this.imageReqId != 0) {
            ConnectionsManager.getInstance(WallpapersListActivity.access$3200(WallpapersListActivity.this)).cancelRequest(this.imageReqId, true);
            this.imageReqId = 0;
         }

      }

      private class CategoryAdapterRecycler extends RecyclerListView.SelectionAdapter {
         private CategoryAdapterRecycler() {
         }

         // $FF: synthetic method
         CategoryAdapterRecycler(Object var2) {
            this();
         }

         public int getItemCount() {
            return WallpapersListActivity.searchColors.length;
         }

         public boolean isEnabled(RecyclerView.ViewHolder var1) {
            return true;
         }

         public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
            ((WallpapersListActivity.ColorCell)var1.itemView).setColor(WallpapersListActivity.searchColors[var2]);
         }

         public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
            WallpapersListActivity.SearchAdapter var3 = SearchAdapter.this;
            return new RecyclerListView.Holder(WallpapersListActivity.this.new ColorCell(var3.mContext));
         }
      }
   }
}

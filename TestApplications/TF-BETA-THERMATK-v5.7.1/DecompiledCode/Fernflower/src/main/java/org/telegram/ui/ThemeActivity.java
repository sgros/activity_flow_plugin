package org.telegram.ui;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.time.SunDate;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.BrightnessControlCell;
import org.telegram.ui.Cells.ChatListCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$_CC;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.ThemeCell;
import org.telegram.ui.Cells.ThemeTypeCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadioButton;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Components.ThemeEditorView;

public class ThemeActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   public static final int THEME_TYPE_ALL = 2;
   public static final int THEME_TYPE_BASIC = 0;
   public static final int THEME_TYPE_NIGHT = 1;
   private static final int create_theme = 1;
   private int automaticBrightnessInfoRow;
   private int automaticBrightnessRow;
   private int automaticHeaderRow;
   private int backgroundRow;
   private int chatListHeaderRow;
   private int chatListInfoRow;
   private int chatListRow;
   private int contactsReimportRow;
   private int contactsSortRow;
   private int currentType;
   private int customTabsRow;
   private ArrayList darkThemes = new ArrayList();
   private ArrayList defaultThemes = new ArrayList();
   private int directShareRow;
   private int emojiRow;
   private int enableAnimationsRow;
   private ThemeActivity.GpsLocationListener gpsLocationListener = new ThemeActivity.GpsLocationListener();
   boolean hasCustomThemes;
   private RecyclerListView innerListView;
   private LinearLayoutManager layoutManager;
   private ThemeActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private ThemeActivity.GpsLocationListener networkLocationListener = new ThemeActivity.GpsLocationListener();
   private int newThemeInfoRow;
   private int nightAutomaticRow;
   private int nightDisabledRow;
   private int nightScheduledRow;
   private int nightThemeRow;
   private int nightTypeInfoRow;
   private int preferedHeaderRow;
   private boolean previousByLocation;
   private int previousUpdatedType;
   private int raiseToSpeakRow;
   private int rowCount;
   private int saveToGalleryRow;
   private int scheduleFromRow;
   private int scheduleFromToInfoRow;
   private int scheduleHeaderRow;
   private int scheduleLocationInfoRow;
   private int scheduleLocationRow;
   private int scheduleToRow;
   private int scheduleUpdateLocationRow;
   private int sendByEnterRow;
   private int settings2Row;
   private int settingsRow;
   private int showThemesRows;
   private int stickersRow;
   private int stickersSection2Row;
   private int textSizeHeaderRow;
   private int textSizeRow;
   private int themeEnd2Row;
   private int themeEndRow;
   private int themeHeader2Row;
   private int themeHeaderRow;
   private int themeInfo2Row;
   private int themeInfoRow;
   private int themeListRow;
   private int themeStart2Row;
   private int themeStartRow;
   private boolean updatingLocation;

   public ThemeActivity(int var1) {
      this.currentType = var1;
      this.updateRows();
   }

   // $FF: synthetic method
   static int access$1000(ThemeActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1100(ThemeActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBarLayout access$300(ThemeActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBarLayout access$400(ThemeActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static int access$600(ThemeActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBarLayout access$6500(ThemeActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBarLayout access$6600(ThemeActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBarLayout access$6700(ThemeActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static int access$700(ThemeActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$800(ThemeActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$900(ThemeActivity var0) {
      return var0.currentAccount;
   }

   private String getLocationSunString() {
      int var1 = Theme.autoNightSunriseTime;
      int var2 = var1 / 60;
      String var3 = String.format("%02d:%02d", var2, var1 - var2 * 60);
      var2 = Theme.autoNightSunsetTime;
      var1 = var2 / 60;
      return LocaleController.formatString("AutoNightUpdateLocationInfo", 2131558795, String.format("%02d:%02d", var1, var2 - var1 * 60), var3);
   }

   // $FF: synthetic method
   static void lambda$null$2(boolean[] var0, View var1) {
      CheckBoxCell var3 = (CheckBoxCell)var1;
      int var2 = (Integer)var3.getTag();
      var0[var2] ^= true;
      var3.setChecked(var0[var2], true);
   }

   // $FF: synthetic method
   static void lambda$null$8(EditTextBoldCursor var0) {
      var0.requestFocus();
      AndroidUtilities.showKeyboard(var0);
   }

   // $FF: synthetic method
   static void lambda$openThemeCreate$6(DialogInterface var0, int var1) {
   }

   // $FF: synthetic method
   static boolean lambda$openThemeCreate$7(TextView var0, int var1, KeyEvent var2) {
      AndroidUtilities.hideKeyboard(var0);
      return false;
   }

   // $FF: synthetic method
   static void lambda$openThemeCreate$9(EditTextBoldCursor var0, DialogInterface var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ThemeActivity$m4EmqHR619o6faN5Q_KiPQtpKTk(var0));
   }

   // $FF: synthetic method
   static int lambda$updateRows$0(Theme.ThemeInfo var0, Theme.ThemeInfo var1) {
      int var2 = var0.sortIndex;
      int var3 = var1.sortIndex;
      if (var2 > var3) {
         return 1;
      } else {
         return var2 < var3 ? -1 : 0;
      }
   }

   private void openThemeCreate() {
      EditTextBoldCursor var1 = new EditTextBoldCursor(this.getParentActivity());
      var1.setBackgroundDrawable(Theme.createEditTextDrawable(this.getParentActivity(), true));
      AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
      var2.setTitle(LocaleController.getString("NewTheme", 2131559910));
      var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      var2.setPositiveButton(LocaleController.getString("OK", 2131560097), _$$Lambda$ThemeActivity$Fq_Zy67h0RlXwDUh_E75MeVk8fE.INSTANCE);
      LinearLayout var3 = new LinearLayout(this.getParentActivity());
      var3.setOrientation(1);
      var2.setView(var3);
      TextView var4 = new TextView(this.getParentActivity());
      var4.setText(LocaleController.formatString("EnterThemeName", 2131559373));
      var4.setTextSize(16.0F);
      var4.setPadding(AndroidUtilities.dp(23.0F), AndroidUtilities.dp(12.0F), AndroidUtilities.dp(23.0F), AndroidUtilities.dp(6.0F));
      var4.setTextColor(Theme.getColor("dialogTextBlack"));
      var3.addView(var4, LayoutHelper.createLinear(-1, -2));
      var1.setTextSize(1, 16.0F);
      var1.setTextColor(Theme.getColor("dialogTextBlack"));
      var1.setMaxLines(1);
      var1.setLines(1);
      var1.setInputType(16385);
      var1.setGravity(51);
      var1.setSingleLine(true);
      var1.setImeOptions(6);
      var1.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      var1.setCursorSize(AndroidUtilities.dp(20.0F));
      var1.setCursorWidth(1.5F);
      var1.setPadding(0, AndroidUtilities.dp(4.0F), 0, 0);
      var3.addView(var1, LayoutHelper.createLinear(-1, 36, 51, 24, 6, 24, 0));
      var1.setOnEditorActionListener(_$$Lambda$ThemeActivity$oWMEMCEfOWz5AenMBirLxMHUSLA.INSTANCE);
      AlertDialog var5 = var2.create();
      var5.setOnShowListener(new _$$Lambda$ThemeActivity$pMIDVqVy4Cqc6NEOajbkSuFQwZA(var1));
      this.showDialog(var5);
      var5.getButton(-1).setOnClickListener(new _$$Lambda$ThemeActivity$9RPKhWFFO8KxhnQ8K0AhpMvKhFA(this, var1, var5));
   }

   private void showPermissionAlert(boolean var1) {
      if (this.getParentActivity() != null) {
         AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
         var2.setTitle(LocaleController.getString("AppName", 2131558635));
         if (var1) {
            var2.setMessage(LocaleController.getString("PermissionNoLocationPosition", 2131560418));
         } else {
            var2.setMessage(LocaleController.getString("PermissionNoLocation", 2131560417));
         }

         var2.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131560419), new _$$Lambda$ThemeActivity$xPe7XQuRsh6WWeEQTpdUdkf0Mig(this));
         var2.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         this.showDialog(var2.create());
      }
   }

   private void startLocationUpdate() {
      if (!this.updatingLocation) {
         this.updatingLocation = true;
         LocationManager var1 = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");

         try {
            var1.requestLocationUpdates("gps", 1L, 0.0F, this.gpsLocationListener);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         try {
            var1.requestLocationUpdates("network", 1L, 0.0F, this.networkLocationListener);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

      }
   }

   private void stopLocationUpdate() {
      this.updatingLocation = false;
      LocationManager var1 = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
      var1.removeUpdates(this.gpsLocationListener);
      var1.removeUpdates(this.networkLocationListener);
   }

   private void updateRows() {
      int var1 = this.rowCount;
      this.rowCount = 0;
      this.emojiRow = -1;
      this.contactsReimportRow = -1;
      this.contactsSortRow = -1;
      this.scheduleLocationRow = -1;
      this.scheduleUpdateLocationRow = -1;
      this.scheduleLocationInfoRow = -1;
      this.nightDisabledRow = -1;
      this.nightScheduledRow = -1;
      this.nightAutomaticRow = -1;
      this.nightTypeInfoRow = -1;
      this.scheduleHeaderRow = -1;
      this.nightThemeRow = -1;
      this.newThemeInfoRow = -1;
      this.scheduleFromRow = -1;
      this.scheduleToRow = -1;
      this.scheduleFromToInfoRow = -1;
      this.themeStartRow = -1;
      this.themeHeader2Row = -1;
      this.themeInfo2Row = -1;
      this.themeStart2Row = -1;
      this.themeEnd2Row = -1;
      this.themeListRow = -1;
      this.themeEndRow = -1;
      this.showThemesRows = -1;
      this.themeInfoRow = -1;
      this.preferedHeaderRow = -1;
      this.automaticHeaderRow = -1;
      this.automaticBrightnessRow = -1;
      this.automaticBrightnessInfoRow = -1;
      this.textSizeHeaderRow = -1;
      this.themeHeaderRow = -1;
      this.chatListHeaderRow = -1;
      this.chatListRow = -1;
      this.chatListInfoRow = -1;
      this.textSizeRow = -1;
      this.backgroundRow = -1;
      this.settingsRow = -1;
      this.customTabsRow = -1;
      this.directShareRow = -1;
      this.enableAnimationsRow = -1;
      this.raiseToSpeakRow = -1;
      this.sendByEnterRow = -1;
      this.saveToGalleryRow = -1;
      this.settings2Row = -1;
      this.stickersRow = -1;
      this.stickersSection2Row = -1;
      int var2 = this.currentType;
      byte var3 = 2;
      int var4;
      Theme.ThemeInfo var5;
      if (var2 == 0) {
         this.hasCustomThemes = false;
         this.defaultThemes.clear();
         var4 = Theme.themes.size();

         for(var2 = 0; var2 < var4; ++var2) {
            var5 = (Theme.ThemeInfo)Theme.themes.get(var2);
            if (var5.pathToFile == null) {
               this.defaultThemes.add(var5);
            } else {
               this.hasCustomThemes = true;
            }
         }

         Collections.sort(this.defaultThemes, _$$Lambda$ThemeActivity$cs0N3OVBAa2T6bewE_YVZM_eTCA.INSTANCE);
         var2 = this.rowCount++;
         this.textSizeHeaderRow = var2;
         var2 = this.rowCount++;
         this.textSizeRow = var2;
         var2 = this.rowCount++;
         this.backgroundRow = var2;
         var2 = this.rowCount++;
         this.newThemeInfoRow = var2;
         var2 = this.rowCount++;
         this.themeHeaderRow = var2;
         var2 = this.rowCount++;
         this.themeListRow = var2;
         if (this.hasCustomThemes) {
            var2 = this.rowCount++;
            this.showThemesRows = var2;
         }

         var2 = this.rowCount++;
         this.themeInfoRow = var2;
         var2 = this.rowCount++;
         this.chatListHeaderRow = var2;
         var2 = this.rowCount++;
         this.chatListRow = var2;
         var2 = this.rowCount++;
         this.chatListInfoRow = var2;
         var2 = this.rowCount++;
         this.settingsRow = var2;
         var2 = this.rowCount++;
         this.nightThemeRow = var2;
         var2 = this.rowCount++;
         this.customTabsRow = var2;
         var2 = this.rowCount++;
         this.directShareRow = var2;
         var2 = this.rowCount++;
         this.enableAnimationsRow = var2;
         var2 = this.rowCount++;
         this.raiseToSpeakRow = var2;
         var2 = this.rowCount++;
         this.sendByEnterRow = var2;
         var2 = this.rowCount++;
         this.saveToGalleryRow = var2;
         var2 = this.rowCount++;
         this.settings2Row = var2;
         var2 = this.rowCount++;
         this.stickersRow = var2;
         var2 = this.rowCount++;
         this.stickersSection2Row = var2;
      } else if (var2 == 2) {
         this.darkThemes.clear();
         this.defaultThemes.clear();
         var4 = Theme.themes.size();

         for(var2 = 0; var2 < var4; ++var2) {
            var5 = (Theme.ThemeInfo)Theme.themes.get(var2);
            if (var5.pathToFile != null) {
               this.darkThemes.add(var5);
            } else {
               this.defaultThemes.add(var5);
            }
         }

         var2 = this.rowCount++;
         this.themeHeaderRow = var2;
         var2 = this.rowCount;
         this.themeStartRow = var2;
         this.rowCount = var2 + this.defaultThemes.size();
         var2 = this.rowCount;
         this.themeEndRow = var2;
         this.rowCount = var2 + 1;
         this.themeInfoRow = var2;
         if (!this.darkThemes.isEmpty()) {
            var2 = this.rowCount++;
            this.themeHeader2Row = var2;
            var2 = this.rowCount;
            this.themeStart2Row = var2;
            this.rowCount = var2 + this.darkThemes.size();
            var2 = this.rowCount;
            this.themeEnd2Row = var2;
            this.rowCount = var2 + 1;
            this.themeInfo2Row = var2;
         }
      } else {
         this.darkThemes.clear();
         var4 = Theme.themes.size();

         for(var2 = 0; var2 < var4; ++var2) {
            var5 = (Theme.ThemeInfo)Theme.themes.get(var2);
            if (!var5.isLight()) {
               this.darkThemes.add(var5);
            }
         }

         var2 = this.rowCount++;
         this.nightDisabledRow = var2;
         var2 = this.rowCount++;
         this.nightScheduledRow = var2;
         var2 = this.rowCount++;
         this.nightAutomaticRow = var2;
         var2 = this.rowCount++;
         this.nightTypeInfoRow = var2;
         var2 = Theme.selectedAutoNightType;
         if (var2 == 1) {
            var2 = this.rowCount++;
            this.scheduleHeaderRow = var2;
            var2 = this.rowCount++;
            this.scheduleLocationRow = var2;
            if (Theme.autoNightScheduleByLocation) {
               var2 = this.rowCount++;
               this.scheduleUpdateLocationRow = var2;
               var2 = this.rowCount++;
               this.scheduleLocationInfoRow = var2;
            } else {
               var2 = this.rowCount++;
               this.scheduleFromRow = var2;
               var2 = this.rowCount++;
               this.scheduleToRow = var2;
               var2 = this.rowCount++;
               this.scheduleFromToInfoRow = var2;
            }
         } else if (var2 == 2) {
            var2 = this.rowCount++;
            this.automaticHeaderRow = var2;
            var2 = this.rowCount++;
            this.automaticBrightnessRow = var2;
            var2 = this.rowCount++;
            this.automaticBrightnessInfoRow = var2;
         }

         if (Theme.selectedAutoNightType != 0) {
            var2 = this.rowCount++;
            this.preferedHeaderRow = var2;
            var2 = this.rowCount;
            this.themeStartRow = var2;
            this.rowCount = var2 + this.darkThemes.size();
            var2 = this.rowCount;
            this.themeEndRow = var2;
            this.rowCount = var2 + 1;
            this.themeInfoRow = var2;
         }
      }

      ThemeActivity.ListAdapter var9 = this.listAdapter;
      if (var9 != null) {
         label127: {
            if (this.currentType == 1) {
               var2 = this.previousUpdatedType;
               if (var2 != -1) {
                  var4 = this.nightTypeInfoRow + 1;
                  boolean var6;
                  byte var12;
                  if (var2 != Theme.selectedAutoNightType) {
                     for(var2 = 0; var2 < 3; ++var2) {
                        RecyclerListView.Holder var10 = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(var2);
                        if (var10 != null) {
                           ThemeTypeCell var11 = (ThemeTypeCell)var10.itemView;
                           if (var2 == Theme.selectedAutoNightType) {
                              var6 = true;
                           } else {
                              var6 = false;
                           }

                           var11.setTypeChecked(var6);
                        }
                     }

                     int var8 = Theme.selectedAutoNightType;
                     if (var8 == 0) {
                        this.listAdapter.notifyItemRangeRemoved(var4, var1 - var4);
                     } else {
                        var12 = 4;
                        if (var8 == 1) {
                           var8 = this.previousUpdatedType;
                           if (var8 == 0) {
                              this.listAdapter.notifyItemRangeInserted(var4, this.rowCount - var4);
                           } else if (var8 == 2) {
                              this.listAdapter.notifyItemRangeRemoved(var4, 3);
                              var9 = this.listAdapter;
                              if (!Theme.autoNightScheduleByLocation) {
                                 var12 = 5;
                              }

                              var9.notifyItemRangeInserted(var4, var12);
                           }
                        } else if (var8 == 2) {
                           var8 = this.previousUpdatedType;
                           if (var8 == 0) {
                              this.listAdapter.notifyItemRangeInserted(var4, this.rowCount - var4);
                           } else if (var8 == 1) {
                              var9 = this.listAdapter;
                              if (!Theme.autoNightScheduleByLocation) {
                                 var12 = 5;
                              }

                              var9.notifyItemRangeRemoved(var4, var12);
                              this.listAdapter.notifyItemRangeInserted(var4, 3);
                           }
                        }
                     }
                  } else {
                     var6 = this.previousByLocation;
                     boolean var7 = Theme.autoNightScheduleByLocation;
                     if (var6 != var7) {
                        var1 = var4 + 2;
                        if (var7) {
                           var12 = 3;
                        } else {
                           var12 = 2;
                        }

                        var9.notifyItemRangeRemoved(var1, var12);
                        var9 = this.listAdapter;
                        if (Theme.autoNightScheduleByLocation) {
                           var12 = var3;
                        } else {
                           var12 = 3;
                        }

                        var9.notifyItemRangeInserted(var1, var12);
                     }
                  }
                  break label127;
               }
            }

            this.listAdapter.notifyDataSetChanged();
         }
      }

      if (this.currentType == 1) {
         this.previousByLocation = Theme.autoNightScheduleByLocation;
         this.previousUpdatedType = Theme.selectedAutoNightType;
      }

   }

   private void updateSunTime(Location var1, boolean var2) {
      LocationManager var3 = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
      if (VERSION.SDK_INT >= 23) {
         Activity var4 = this.getParentActivity();
         if (var4 != null && var4.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            var4.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
            return;
         }
      }

      if (this.getParentActivity() != null) {
         if (!this.getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
            return;
         }

         try {
            if (!((LocationManager)ApplicationLoader.applicationContext.getSystemService("location")).isProviderEnabled("gps")) {
               AlertDialog.Builder var17 = new AlertDialog.Builder(this.getParentActivity());
               var17.setTitle(LocaleController.getString("AppName", 2131558635));
               var17.setMessage(LocaleController.getString("GpsDisabledAlert", 2131559597));
               String var5 = LocaleController.getString("ConnectingToProxyEnable", 2131559140);
               _$$Lambda$ThemeActivity$fvRbp4i9JBIdpZn90Y9NJP_7GVc var6 = new _$$Lambda$ThemeActivity$fvRbp4i9JBIdpZn90Y9NJP_7GVc(this);
               var17.setPositiveButton(var5, var6);
               var17.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               this.showDialog(var17.create());
               return;
            }
         } catch (Exception var7) {
            FileLog.e((Throwable)var7);
         }
      }

      Location var15;
      label76: {
         Exception var10000;
         label82: {
            boolean var10001;
            try {
               var15 = var3.getLastKnownLocation("gps");
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label82;
            }

            var1 = var15;
            if (var15 == null) {
               var1 = var15;

               try {
                  var15 = var3.getLastKnownLocation("network");
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label82;
               }

               var1 = var15;
            }

            var15 = var1;
            if (var1 != null) {
               break label76;
            }

            try {
               var15 = var3.getLastKnownLocation("passive");
               break label76;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
            }
         }

         Exception var16 = var10000;
         FileLog.e((Throwable)var16);
         var15 = var1;
      }

      if (var15 == null || var2) {
         this.startLocationUpdate();
         if (var15 == null) {
            return;
         }
      }

      Theme.autoNightLocationLatitude = var15.getLatitude();
      Theme.autoNightLocationLongitude = var15.getLongitude();
      int[] var11 = SunDate.calculateSunriseSunset(Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude);
      Theme.autoNightSunriseTime = var11[0];
      Theme.autoNightSunsetTime = var11[1];
      Theme.autoNightCityName = null;
      Calendar var12 = Calendar.getInstance();
      var12.setTimeInMillis(System.currentTimeMillis());
      Theme.autoNightLastSunCheckDay = var12.get(5);
      Utilities.globalQueue.postRunnable(new _$$Lambda$ThemeActivity$bw9bigoIiGmqIM9XZH7TFdPN3XE(this));
      RecyclerListView.Holder var13 = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(this.scheduleLocationInfoRow);
      if (var13 != null) {
         View var14 = var13.itemView;
         if (var14 instanceof TextInfoPrivacyCell) {
            ((TextInfoPrivacyCell)var14).setText(this.getLocationSunString());
         }
      }

      if (Theme.autoNightScheduleByLocation && Theme.selectedAutoNightType == 1) {
         Theme.checkAutoNightThemeConditions();
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(false);
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      int var2 = this.currentType;
      if (var2 == 0) {
         super.actionBar.setTitle(LocaleController.getString("ChatSettings", 2131559043));
         ActionBarMenuItem var3 = super.actionBar.createMenu().addItem(0, 2131165416);
         var3.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131558443));
         var3.addSubItem(1, 2131165589, LocaleController.getString("CreateNewThemeMenu", 2131559173));
      } else if (var2 == 2) {
         super.actionBar.setTitle(LocaleController.getString("ColorThemes", 2131559130));
      } else {
         super.actionBar.setTitle(LocaleController.getString("AutoNightTheme", 2131558791));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         // $FF: synthetic method
         public void lambda$onItemClick$0$ThemeActivity$1(DialogInterface var1, int var2) {
            ThemeActivity.this.openThemeCreate();
         }

         public void onItemClick(int var1) {
            if (var1 == -1) {
               ThemeActivity.this.finishFragment();
            } else if (var1 == 1) {
               if (ThemeActivity.this.getParentActivity() == null) {
                  return;
               }

               AlertDialog.Builder var2 = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
               var2.setTitle(LocaleController.getString("NewTheme", 2131559910));
               var2.setMessage(LocaleController.getString("CreateNewThemeAlert", 2131559171));
               var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               var2.setPositiveButton(LocaleController.getString("CreateTheme", 2131559174), new _$$Lambda$ThemeActivity$1$ZQnhOSOAx8cfjiv91xqtf3q_RU0(this));
               ThemeActivity.this.showDialog(var2.create());
            }

         }
      });
      this.listAdapter = new ThemeActivity.ListAdapter(var1);
      FrameLayout var6 = new FrameLayout(var1);
      var6.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      super.fragmentView = var6;
      this.listView = new RecyclerListView(var1);
      RecyclerListView var4 = this.listView;
      LinearLayoutManager var5 = new LinearLayoutManager(var1, 1, false);
      this.layoutManager = var5;
      var4.setLayoutManager(var5);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setAdapter(this.listAdapter);
      ((DefaultItemAnimator)this.listView.getItemAnimator()).setDelayAnimations(false);
      var6.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)(new _$$Lambda$ThemeActivity$6AbNGVXM3fzlqnkK4ORVG2_WTt4(this)));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.locationPermissionGranted) {
         this.updateSunTime((Location)null, true);
      } else if (var1 == NotificationCenter.didSetNewWallpapper) {
         RecyclerListView var4 = this.listView;
         if (var4 != null) {
            var4.invalidateViews();
         }
      } else if (var1 == NotificationCenter.themeListUpdated) {
         this.updateRows();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, BrightnessControlCell.class, ThemeTypeCell.class, ThemeCell.class, ThemeActivity.TextSizeCell.class, ChatListCell.class, NotificationsCheckCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(this.innerListView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var9 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuBackground");
      ThemeDescription var10 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItem");
      ThemeDescription var11 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItemIcon");
      ThemeDescription var12 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var13 = this.listView;
      Paint var14 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, new ThemeDescription(var13, 0, new Class[]{View.class}, var14, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, 0, new Class[]{ThemeCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{ThemeCell.class}, new String[]{"checkImage"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "featuredStickers_addedIcon"), new ThemeDescription(this.listView, 0, new Class[]{ThemeCell.class}, new String[]{"optionsButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "stickers_menu"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{BrightnessControlCell.class}, new String[]{"leftImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionIcon"), new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{BrightnessControlCell.class}, new String[]{"rightImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionIcon"), new ThemeDescription(this.listView, 0, new Class[]{BrightnessControlCell.class}, new String[]{"seekBarView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_progressBackground"), new ThemeDescription(this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{BrightnessControlCell.class}, new String[]{"seekBarView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_progress"), new ThemeDescription(this.listView, 0, new Class[]{ThemeTypeCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{ThemeTypeCell.class}, new String[]{"checkImage"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "featuredStickers_addedIcon"), new ThemeDescription(this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{ThemeActivity.TextSizeCell.class}, new String[]{"sizeBar"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_progress"), new ThemeDescription(this.listView, 0, new Class[]{ThemeActivity.TextSizeCell.class}, new String[]{"sizeBar"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_progressBackground"), new ThemeDescription(this.listView, 0, new Class[]{ChatListCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackground"), new ThemeDescription(this.listView, 0, new Class[]{ChatListCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackgroundChecked"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubble"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubbleSelected"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgInShadowDrawable, Theme.chat_msgInMediaShadowDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inBubbleShadow"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubble"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubbleSelected"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgOutShadowDrawable, Theme.chat_msgOutMediaShadowDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outBubbleShadow"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messageTextIn"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messageTextOut"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgOutCheckDrawable, Theme.chat_msgOutHalfCheckDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSentCheck"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgOutCheckSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outSentCheckSelected"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_mediaSentCheck"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyLine"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyLine"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyNameText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyNameText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyMessageText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyMessageText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inReplyMediaMessageSelectedText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outReplyMediaMessageSelectedText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inTimeText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outTimeText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_inTimeSelectedText"), new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_outTimeSelectedText")};
   }

   // $FF: synthetic method
   public void lambda$createView$5$ThemeActivity(View var1, int var2, float var3, float var4) {
      SharedPreferences var5;
      boolean var6;
      Editor var16;
      if (var2 == this.enableAnimationsRow) {
         var5 = MessagesController.getGlobalMainSettings();
         var6 = var5.getBoolean("view_animations", true);
         var16 = var5.edit();
         var16.putBoolean("view_animations", var6 ^ true);
         var16.commit();
         if (var1 instanceof TextCheckCell) {
            ((TextCheckCell)var1).setChecked(var6 ^ true);
         }
      } else {
         int var7 = this.backgroundRow;
         var6 = false;
         byte var8 = 0;
         if (var2 == var7) {
            this.presentFragment(new WallpapersListActivity(0));
         } else if (var2 == this.sendByEnterRow) {
            var5 = MessagesController.getGlobalMainSettings();
            var6 = var5.getBoolean("send_by_enter", false);
            var16 = var5.edit();
            var16.putBoolean("send_by_enter", var6 ^ true);
            var16.commit();
            if (var1 instanceof TextCheckCell) {
               ((TextCheckCell)var1).setChecked(var6 ^ true);
            }
         } else if (var2 == this.raiseToSpeakRow) {
            SharedConfig.toogleRaiseToSpeak();
            if (var1 instanceof TextCheckCell) {
               ((TextCheckCell)var1).setChecked(SharedConfig.raiseToSpeak);
            }
         } else if (var2 == this.saveToGalleryRow) {
            SharedConfig.toggleSaveToGallery();
            if (var1 instanceof TextCheckCell) {
               ((TextCheckCell)var1).setChecked(SharedConfig.saveToGallery);
            }
         } else if (var2 == this.customTabsRow) {
            SharedConfig.toggleCustomTabs();
            if (var1 instanceof TextCheckCell) {
               ((TextCheckCell)var1).setChecked(SharedConfig.customTabs);
            }
         } else if (var2 == this.directShareRow) {
            SharedConfig.toggleDirectShare();
            if (var1 instanceof TextCheckCell) {
               ((TextCheckCell)var1).setChecked(SharedConfig.directShare);
            }
         } else if (var2 != this.contactsReimportRow) {
            String var12;
            String var17;
            if (var2 == this.contactsSortRow) {
               if (this.getParentActivity() == null) {
                  return;
               }

               AlertDialog.Builder var9 = new AlertDialog.Builder(this.getParentActivity());
               var9.setTitle(LocaleController.getString("SortBy", 2131560795));
               var12 = LocaleController.getString("Default", 2131559225);
               var17 = LocaleController.getString("SortFirstName", 2131560796);
               String var10 = LocaleController.getString("SortLastName", 2131560797);
               _$$Lambda$ThemeActivity$Ur1wYdChFFO5JBnOUCuxyfGs_Qw var11 = new _$$Lambda$ThemeActivity$Ur1wYdChFFO5JBnOUCuxyfGs_Qw(this, var2);
               var9.setItems(new CharSequence[]{var12, var17, var10}, var11);
               var9.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               this.showDialog(var9.create());
            } else if (var2 == this.stickersRow) {
               this.presentFragment(new StickersActivity(0));
            } else if (var2 == this.showThemesRows) {
               this.presentFragment(new ThemeActivity(2));
            } else {
               int var21;
               if (var2 == this.emojiRow) {
                  if (this.getParentActivity() == null) {
                     return;
                  }

                  boolean[] var18 = new boolean[2];
                  BottomSheet.Builder var24 = new BottomSheet.Builder(this.getParentActivity());
                  var24.setApplyTopPadding(false);
                  var24.setApplyBottomPadding(false);
                  LinearLayout var26 = new LinearLayout(this.getParentActivity());
                  var26.setOrientation(1);
                  var21 = 0;

                  while(true) {
                     byte var20;
                     if (VERSION.SDK_INT >= 19) {
                        var20 = 2;
                     } else {
                        var20 = 1;
                     }

                     if (var21 >= var20) {
                        BottomSheet.BottomSheetCell var13 = new BottomSheet.BottomSheetCell(this.getParentActivity(), 1);
                        var13.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        var13.setTextAndIcon(LocaleController.getString("Save", 2131560626).toUpperCase(), 0);
                        var13.setTextColor(Theme.getColor("dialogTextBlue2"));
                        var13.setOnClickListener(new _$$Lambda$ThemeActivity$Qmfg6ZM5i1uh_yBWU4bHsWNdC00(this, var18, var2));
                        var26.addView(var13, LayoutHelper.createLinear(-1, 50));
                        var24.setCustomView(var26);
                        this.showDialog(var24.create());
                        break;
                     }

                     if (var21 == 0) {
                        var18[var21] = SharedConfig.allowBigEmoji;
                        var12 = LocaleController.getString("EmojiBigSize", 2131559340);
                     } else if (var21 == 1) {
                        var18[var21] = SharedConfig.useSystemEmoji;
                        var12 = LocaleController.getString("EmojiUseDefault", 2131559344);
                     } else {
                        var12 = null;
                     }

                     CheckBoxCell var23 = new CheckBoxCell(this.getParentActivity(), 1, 21);
                     var23.setTag(var21);
                     var23.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                     var26.addView(var23, LayoutHelper.createLinear(-1, 50));
                     var23.setText(var12, "", var18[var21], true);
                     var23.setTextColor(Theme.getColor("dialogTextBlack"));
                     var23.setOnClickListener(new _$$Lambda$ThemeActivity$EM3ewToEngglZwU9SAL1G0JUpz0(var18));
                     ++var21;
                  }
               } else if ((var2 < this.themeStartRow || var2 >= this.themeEndRow) && (var2 < this.themeStart2Row || var2 >= this.themeEnd2Row)) {
                  if (var2 == this.nightThemeRow) {
                     if ((!LocaleController.isRTL || var3 > (float)AndroidUtilities.dp(76.0F)) && (LocaleController.isRTL || var3 < (float)(var1.getMeasuredWidth() - AndroidUtilities.dp(76.0F)))) {
                        this.presentFragment(new ThemeActivity(1));
                     } else {
                        NotificationsCheckCell var25 = (NotificationsCheckCell)var1;
                        if (Theme.selectedAutoNightType == 0) {
                           Theme.selectedAutoNightType = 2;
                           var25.setChecked(true);
                        } else {
                           Theme.selectedAutoNightType = 0;
                           var25.setChecked(false);
                        }

                        Theme.saveAutoNightThemeConfig();
                        Theme.checkAutoNightThemeConditions();
                        if (Theme.selectedAutoNightType != 0) {
                           var6 = true;
                        }

                        if (var6) {
                           var12 = Theme.getCurrentNightThemeName();
                        } else {
                           var12 = LocaleController.getString("AutoNightThemeOff", 2131558792);
                        }

                        var17 = var12;
                        if (var6) {
                           if (Theme.selectedAutoNightType == 1) {
                              var2 = 2131558790;
                              var17 = "AutoNightScheduled";
                           } else {
                              var2 = 2131558782;
                              var17 = "AutoNightAdaptive";
                           }

                           String var27 = LocaleController.getString(var17, var2);
                           StringBuilder var22 = new StringBuilder();
                           var22.append(var27);
                           var22.append(" ");
                           var22.append(var12);
                           var17 = var22.toString();
                        }

                        var25.setTextAndValueAndCheck(LocaleController.getString("AutoNightTheme", 2131558791), var17, var6, true);
                     }
                  } else if (var2 == this.nightDisabledRow) {
                     Theme.selectedAutoNightType = 0;
                     this.updateRows();
                     Theme.checkAutoNightThemeConditions();
                  } else if (var2 == this.nightScheduledRow) {
                     Theme.selectedAutoNightType = 1;
                     if (Theme.autoNightScheduleByLocation) {
                        this.updateSunTime((Location)null, true);
                     }

                     this.updateRows();
                     Theme.checkAutoNightThemeConditions();
                  } else if (var2 == this.nightAutomaticRow) {
                     Theme.selectedAutoNightType = 2;
                     this.updateRows();
                     Theme.checkAutoNightThemeConditions();
                  } else if (var2 == this.scheduleLocationRow) {
                     Theme.autoNightScheduleByLocation ^= true;
                     ((TextCheckCell)var1).setChecked(Theme.autoNightScheduleByLocation);
                     this.updateRows();
                     if (Theme.autoNightScheduleByLocation) {
                        this.updateSunTime((Location)null, true);
                     }

                     Theme.checkAutoNightThemeConditions();
                  } else if (var2 != this.scheduleFromRow && var2 != this.scheduleToRow) {
                     if (var2 == this.scheduleUpdateLocationRow) {
                        this.updateSunTime((Location)null, true);
                     }
                  } else {
                     if (this.getParentActivity() == null) {
                        return;
                     }

                     if (var2 == this.scheduleFromRow) {
                        var7 = Theme.autoNightDayStartTime;
                        var21 = var7 / 60;
                     } else {
                        var7 = Theme.autoNightDayEndTime;
                        var21 = var7 / 60;
                     }

                     TextSettingsCell var19 = (TextSettingsCell)var1;
                     this.showDialog(new TimePickerDialog(this.getParentActivity(), new _$$Lambda$ThemeActivity$NM7fAI0FGrIygn_Tl1Tnvhrr91Y(this, var2, var19), var21, var7 - var21 * 60, true));
                  }
               } else {
                  var7 = this.themeStart2Row;
                  ArrayList var14;
                  if (var7 >= 0 && var2 >= var7) {
                     var2 -= var7;
                     var14 = this.darkThemes;
                  } else {
                     var2 -= this.themeStartRow;
                     var7 = this.currentType;
                     if (var7 == 1) {
                        var14 = this.darkThemes;
                     } else if (var7 == 2) {
                        var14 = this.defaultThemes;
                     } else {
                        var14 = Theme.themes;
                     }
                  }

                  if (var2 >= 0 && var2 < var14.size()) {
                     Theme.ThemeInfo var15 = (Theme.ThemeInfo)var14.get(var2);
                     if (this.currentType != 1) {
                        if (var15 == Theme.getCurrentTheme()) {
                           return;
                        }

                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, var15, false);
                     } else {
                        Theme.setCurrentNightTheme(var15);
                     }

                     var7 = this.listView.getChildCount();

                     for(var2 = var8; var2 < var7; ++var2) {
                        var1 = this.listView.getChildAt(var2);
                        if (var1 instanceof ThemeCell) {
                           ((ThemeCell)var1).updateCurrentThemeCheck();
                        }
                     }
                  }
               }
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$1$ThemeActivity(int var1, DialogInterface var2, int var3) {
      Editor var4 = MessagesController.getGlobalMainSettings().edit();
      var4.putInt("sortContactsBy", var3);
      var4.commit();
      ThemeActivity.ListAdapter var5 = this.listAdapter;
      if (var5 != null) {
         var5.notifyItemChanged(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$null$12$ThemeActivity(String var1) {
      Theme.autoNightCityName = var1;
      if (Theme.autoNightCityName == null) {
         Theme.autoNightCityName = String.format("(%.06f, %.06f)", Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude);
      }

      Theme.saveAutoNightThemeConfig();
      RecyclerListView var2 = this.listView;
      if (var2 != null) {
         RecyclerListView.Holder var3 = (RecyclerListView.Holder)var2.findViewHolderForAdapterPosition(this.scheduleUpdateLocationRow);
         if (var3 != null) {
            View var4 = var3.itemView;
            if (var4 instanceof TextSettingsCell) {
               ((TextSettingsCell)var4).setTextAndValue(LocaleController.getString("AutoNightUpdateLocation", 2131558794), Theme.autoNightCityName, false);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$ThemeActivity(boolean[] var1, int var2, View var3) {
      try {
         if (super.visibleDialog != null) {
            super.visibleDialog.dismiss();
         }
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

      Editor var7 = MessagesController.getGlobalMainSettings().edit();
      boolean var4 = var1[0];
      SharedConfig.allowBigEmoji = var4;
      var7.putBoolean("allowBigEmoji", var4);
      var4 = var1[1];
      SharedConfig.useSystemEmoji = var4;
      var7.putBoolean("useSystemEmoji", var4);
      var7.commit();
      ThemeActivity.ListAdapter var6 = this.listAdapter;
      if (var6 != null) {
         var6.notifyItemChanged(var2);
      }

   }

   // $FF: synthetic method
   public void lambda$null$4$ThemeActivity(int var1, TextSettingsCell var2, TimePicker var3, int var4, int var5) {
      int var6 = var4 * 60 + var5;
      if (var1 == this.scheduleFromRow) {
         Theme.autoNightDayStartTime = var6;
         var2.setTextAndValue(LocaleController.getString("AutoNightFrom", 2131558786), String.format("%02d:%02d", var4, var5), true);
      } else {
         Theme.autoNightDayEndTime = var6;
         var2.setTextAndValue(LocaleController.getString("AutoNightTo", 2131558793), String.format("%02d:%02d", var4, var5), true);
      }

   }

   // $FF: synthetic method
   public void lambda$openThemeCreate$10$ThemeActivity(EditTextBoldCursor var1, AlertDialog var2, View var3) {
      if (var1.length() == 0) {
         Vibrator var8 = (Vibrator)ApplicationLoader.applicationContext.getSystemService("vibrator");
         if (var8 != null) {
            var8.vibrate(200L);
         }

         AndroidUtilities.shakeView(var1, 2.0F, 0);
      } else {
         ThemeEditorView var9 = new ThemeEditorView();
         StringBuilder var4 = new StringBuilder();
         var4.append(var1.getText().toString());
         var4.append(".attheme");
         String var6 = var4.toString();
         var9.show(this.getParentActivity(), var6);
         Theme.saveCurrentTheme(var6, true);
         this.updateRows();
         var2.dismiss();
         SharedPreferences var7 = MessagesController.getGlobalMainSettings();
         if (!var7.getBoolean("themehint", false)) {
            var7.edit().putBoolean("themehint", true).commit();

            try {
               Toast.makeText(this.getParentActivity(), LocaleController.getString("CreateNewThemeHelp", 2131559172), 1).show();
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
            }

         }
      }
   }

   // $FF: synthetic method
   public void lambda$showPermissionAlert$14$ThemeActivity(DialogInterface var1, int var2) {
      if (this.getParentActivity() != null) {
         try {
            Intent var5 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            StringBuilder var3 = new StringBuilder();
            var3.append("package:");
            var3.append(ApplicationLoader.applicationContext.getPackageName());
            var5.setData(Uri.parse(var3.toString()));
            this.getParentActivity().startActivity(var5);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$updateSunTime$11$ThemeActivity(DialogInterface var1, int var2) {
      if (this.getParentActivity() != null) {
         try {
            Activity var3 = this.getParentActivity();
            Intent var5 = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
            var3.startActivity(var5);
         } catch (Exception var4) {
         }

      }
   }

   // $FF: synthetic method
   public void lambda$updateSunTime$13$ThemeActivity() {
      Object var1 = null;

      String var6;
      label24: {
         label28: {
            boolean var10001;
            List var3;
            try {
               Geocoder var2 = new Geocoder(ApplicationLoader.applicationContext, Locale.getDefault());
               var3 = var2.getFromLocation(Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude, 1);
            } catch (Exception var5) {
               var10001 = false;
               break label28;
            }

            var6 = (String)var1;

            try {
               if (var3.size() > 0) {
                  var6 = ((Address)var3.get(0)).getLocality();
               }
               break label24;
            } catch (Exception var4) {
               var10001 = false;
            }
         }

         var6 = (String)var1;
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$ThemeActivity$5EVizJzluGYCke_mYmlNF_TBd_Q(this, var6));
   }

   public boolean onFragmentCreate() {
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.themeListUpdated);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      this.stopLocationUpdate();
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.themeListUpdated);
      Theme.saveAutoNightThemeConfig();
   }

   public void onResume() {
      super.onResume();
      ThemeActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   private class GpsLocationListener implements LocationListener {
      private GpsLocationListener() {
      }

      // $FF: synthetic method
      GpsLocationListener(Object var2) {
         this();
      }

      public void onLocationChanged(Location var1) {
         if (var1 != null) {
            ThemeActivity.this.stopLocationUpdate();
            ThemeActivity.this.updateSunTime(var1, false);
         }
      }

      public void onProviderDisabled(String var1) {
      }

      public void onProviderEnabled(String var1) {
      }

      public void onStatusChanged(String var1, int var2, Bundle var3) {
      }
   }

   private class InnerListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public InnerListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return ThemeActivity.this.defaultThemes.size();
      }

      public int getItemViewType(int var1) {
         return 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         ThemeActivity.InnerThemeView var3 = (ThemeActivity.InnerThemeView)var1.itemView;
         Theme.ThemeInfo var7 = (Theme.ThemeInfo)ThemeActivity.this.defaultThemes.get(var2);
         int var4 = ThemeActivity.this.defaultThemes.size();
         boolean var5 = true;
         boolean var6;
         if (var2 == var4 - 1) {
            var6 = true;
         } else {
            var6 = false;
         }

         if (var2 != 0) {
            var5 = false;
         }

         var3.setTheme(var7, var6, var5);
      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         return new RecyclerListView.Holder(ThemeActivity.this.new InnerThemeView(this.mContext));
      }
   }

   private class InnerThemeView extends FrameLayout {
      private RadioButton button;
      private Drawable inDrawable;
      private boolean isFirst;
      private boolean isLast;
      private Drawable outDrawable;
      private Paint paint = new Paint(1);
      private RectF rect = new RectF();
      private TextPaint textPaint = new TextPaint(1);
      private Theme.ThemeInfo themeInfo;

      public InnerThemeView(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.inDrawable = var2.getResources().getDrawable(2131165602).mutate();
         this.outDrawable = var2.getResources().getDrawable(2131165603).mutate();
         this.textPaint.setTextSize((float)AndroidUtilities.dp(13.0F));
         this.button = new RadioButton(var2) {
            public void invalidate() {
               super.invalidate();
            }
         };
         this.button.setSize(AndroidUtilities.dp(20.0F));
         this.button.setColor(1728053247, -1);
         this.addView(this.button, LayoutHelper.createFrame(22, 22.0F, 51, 27.0F, 75.0F, 0.0F, 0.0F));
      }

      protected void onAttachedToWindow() {
         super.onAttachedToWindow();
         RadioButton var1 = this.button;
         boolean var2;
         if (this.themeInfo == Theme.getCurrentTheme()) {
            var2 = true;
         } else {
            var2 = false;
         }

         var1.setChecked(var2, false);
      }

      protected void onDraw(Canvas var1) {
         this.paint.setColor(this.themeInfo.previewBackgroundColor);
         int var2;
         if (this.isFirst) {
            var2 = AndroidUtilities.dp(22.0F);
         } else {
            var2 = 0;
         }

         this.rect.set((float)var2, (float)AndroidUtilities.dp(11.0F), (float)(AndroidUtilities.dp(76.0F) + var2), (float)AndroidUtilities.dp(108.0F));
         var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0F), (float)AndroidUtilities.dp(6.0F), this.paint);
         int var5;
         if ("Arctic Blue".equals(this.themeInfo.name)) {
            int var3 = Color.red(-5196358);
            int var4 = Color.green(-5196358);
            var5 = Color.blue(-5196358);
            this.button.setColor(-5000269, -13129232);
            Theme.chat_instantViewRectPaint.setColor(Color.argb(43, var3, var4, var5));
            var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0F), (float)AndroidUtilities.dp(6.0F), Theme.chat_instantViewRectPaint);
         } else {
            this.button.setColor(1728053247, -1);
         }

         this.inDrawable.setBounds(AndroidUtilities.dp(6.0F) + var2, AndroidUtilities.dp(22.0F), AndroidUtilities.dp(49.0F) + var2, AndroidUtilities.dp(36.0F));
         this.inDrawable.draw(var1);
         this.outDrawable.setBounds(AndroidUtilities.dp(27.0F) + var2, AndroidUtilities.dp(41.0F), AndroidUtilities.dp(70.0F) + var2, AndroidUtilities.dp(55.0F));
         this.outDrawable.draw(var1);
         String var6 = TextUtils.ellipsize(this.themeInfo.getName(), this.textPaint, (float)(this.getMeasuredWidth() - AndroidUtilities.dp(10.0F)), TruncateAt.END).toString();
         var5 = (int)Math.ceil((double)this.textPaint.measureText(var6));
         this.textPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         var1.drawText(var6, (float)(var2 + (AndroidUtilities.dp(76.0F) - var5) / 2), (float)AndroidUtilities.dp(131.0F), this.textPaint);
      }

      protected void onMeasure(int var1, int var2) {
         boolean var3 = this.isLast;
         byte var5 = 22;
         byte var4;
         if (var3) {
            var4 = 22;
         } else {
            var4 = 15;
         }

         if (!this.isFirst) {
            var5 = 0;
         }

         super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)(var4 + 76 + var5)), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(148.0F), 1073741824));
      }

      public void setTheme(Theme.ThemeInfo var1, boolean var2, boolean var3) {
         this.themeInfo = var1;
         this.isFirst = var3;
         this.isLast = var2;
         LayoutParams var4 = (LayoutParams)this.button.getLayoutParams();
         float var5;
         if (this.isFirst) {
            var5 = 49.0F;
         } else {
            var5 = 27.0F;
         }

         var4.leftMargin = AndroidUtilities.dp(var5);
         this.button.setLayoutParams(var4);
         this.inDrawable.setColorFilter(new PorterDuffColorFilter(var1.previewInColor, Mode.MULTIPLY));
         this.outDrawable.setColorFilter(new PorterDuffColorFilter(var1.previewOutColor, Mode.MULTIPLY));
      }

      public void updateCurrentThemeCheck() {
         RadioButton var1 = this.button;
         boolean var2;
         if (this.themeInfo == Theme.getCurrentTheme()) {
            var2 = true;
         } else {
            var2 = false;
         }

         var1.setChecked(var2, true);
      }
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      private void showOptionsForTheme(Theme.ThemeInfo var1) {
         if (ThemeActivity.this.getParentActivity() != null) {
            BottomSheet.Builder var2 = new BottomSheet.Builder(ThemeActivity.this.getParentActivity());
            CharSequence[] var3;
            if (var1.pathToFile == null) {
               var3 = new CharSequence[]{LocaleController.getString("ShareFile", 2131560748)};
            } else {
               var3 = new CharSequence[]{LocaleController.getString("ShareFile", 2131560748), LocaleController.getString("Edit", 2131559301), LocaleController.getString("Delete", 2131559227)};
            }

            var2.setItems(var3, new _$$Lambda$ThemeActivity$ListAdapter$mOT1foTAY8nRoymoRurolXzJymU(this, var1));
            ThemeActivity.this.showDialog(var2.create());
         }
      }

      public int getItemCount() {
         return ThemeActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != ThemeActivity.this.scheduleFromRow && var1 != ThemeActivity.this.emojiRow && var1 != ThemeActivity.this.showThemesRows && var1 != ThemeActivity.this.scheduleToRow && var1 != ThemeActivity.this.scheduleUpdateLocationRow && var1 != ThemeActivity.this.backgroundRow && var1 != ThemeActivity.this.contactsReimportRow && var1 != ThemeActivity.this.contactsSortRow && var1 != ThemeActivity.this.stickersRow) {
            if (var1 != ThemeActivity.this.automaticBrightnessInfoRow && var1 != ThemeActivity.this.scheduleLocationInfoRow) {
               if (var1 != ThemeActivity.this.themeInfoRow && var1 != ThemeActivity.this.nightTypeInfoRow && var1 != ThemeActivity.this.scheduleFromToInfoRow && var1 != ThemeActivity.this.stickersSection2Row && var1 != ThemeActivity.this.settings2Row && var1 != ThemeActivity.this.newThemeInfoRow && var1 != ThemeActivity.this.chatListInfoRow && var1 != ThemeActivity.this.themeInfo2Row) {
                  if (var1 != ThemeActivity.this.nightDisabledRow && var1 != ThemeActivity.this.nightScheduledRow && var1 != ThemeActivity.this.nightAutomaticRow) {
                     if (var1 != ThemeActivity.this.scheduleHeaderRow && var1 != ThemeActivity.this.automaticHeaderRow && var1 != ThemeActivity.this.preferedHeaderRow && var1 != ThemeActivity.this.settingsRow && var1 != ThemeActivity.this.themeHeaderRow && var1 != ThemeActivity.this.textSizeHeaderRow && var1 != ThemeActivity.this.chatListHeaderRow && var1 != ThemeActivity.this.themeHeader2Row) {
                        if (var1 == ThemeActivity.this.automaticBrightnessRow) {
                           return 6;
                        } else if (var1 != ThemeActivity.this.scheduleLocationRow && var1 != ThemeActivity.this.enableAnimationsRow && var1 != ThemeActivity.this.sendByEnterRow && var1 != ThemeActivity.this.saveToGalleryRow && var1 != ThemeActivity.this.raiseToSpeakRow && var1 != ThemeActivity.this.customTabsRow && var1 != ThemeActivity.this.directShareRow) {
                           if (var1 == ThemeActivity.this.textSizeRow) {
                              return 8;
                           } else if (var1 == ThemeActivity.this.chatListRow) {
                              return 9;
                           } else if (var1 == ThemeActivity.this.nightThemeRow) {
                              return 10;
                           } else {
                              return var1 == ThemeActivity.this.themeListRow ? 11 : 0;
                           }
                        } else {
                           return 7;
                        }
                     } else {
                        return 5;
                     }
                  } else {
                     return 4;
                  }
               } else {
                  return 3;
               }
            } else {
               return 2;
            }
         } else {
            return 1;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         boolean var3 = true;
         boolean var4 = var3;
         if (var2 != 0) {
            var4 = var3;
            if (var2 != 1) {
               var4 = var3;
               if (var2 != 4) {
                  var4 = var3;
                  if (var2 != 7) {
                     var4 = var3;
                     if (var2 != 10) {
                        if (var2 == 11) {
                           var4 = var3;
                        } else {
                           var4 = false;
                        }
                     }
                  }
               }
            }
         }

         return var4;
      }

      // $FF: synthetic method
      public void lambda$null$0$ThemeActivity$ListAdapter(Theme.ThemeInfo var1, DialogInterface var2, int var3) {
         if (Theme.deleteTheme(var1)) {
            ThemeActivity.access$6700(ThemeActivity.this).rebuildAllFragmentViews(true, true);
         }

         NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.themeListUpdated);
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$2$ThemeActivity$ListAdapter(View var1) {
         this.showOptionsForTheme(((ThemeCell)var1.getParent()).getCurrentThemeInfo());
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$3$ThemeActivity$ListAdapter(View var1, int var2) {
         Theme.ThemeInfo var3 = ((ThemeActivity.InnerThemeView)var1).themeInfo;
         if (var3 != Theme.getCurrentTheme()) {
            NotificationCenter var5 = NotificationCenter.getGlobalInstance();
            int var4 = NotificationCenter.needSetDayNightTheme;
            var2 = 0;
            var5.postNotificationName(var4, var3, false);

            for(var4 = ThemeActivity.this.innerListView.getChildCount(); var2 < var4; ++var2) {
               var1 = ThemeActivity.this.innerListView.getChildAt(var2);
               if (var1 instanceof ThemeActivity.InnerThemeView) {
                  ((ThemeActivity.InnerThemeView)var1).updateCurrentThemeCheck();
               }
            }

         }
      }

      // $FF: synthetic method
      public boolean lambda$onCreateViewHolder$4$ThemeActivity$ListAdapter(View var1, int var2) {
         this.showOptionsForTheme(((ThemeActivity.InnerThemeView)var1).themeInfo);
         return true;
      }

      // $FF: synthetic method
      public void lambda$showOptionsForTheme$1$ThemeActivity$ListAdapter(Theme.ThemeInfo param1, DialogInterface param2, int param3) {
         // $FF: Couldn't be decompiled
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = false;
         boolean var8 = true;
         String var16;
         String var18;
         if (var3 != 10) {
            switch(var3) {
            case 0:
               ArrayList var21;
               if (ThemeActivity.this.themeStart2Row >= 0 && var2 >= ThemeActivity.this.themeStart2Row) {
                  var2 -= ThemeActivity.this.themeStart2Row;
                  var21 = ThemeActivity.this.darkThemes;
               } else {
                  var2 -= ThemeActivity.this.themeStartRow;
                  if (ThemeActivity.this.currentType == 1) {
                     var21 = ThemeActivity.this.darkThemes;
                  } else if (ThemeActivity.this.currentType == 2) {
                     var21 = ThemeActivity.this.defaultThemes;
                  } else {
                     var21 = Theme.themes;
                  }
               }

               Theme.ThemeInfo var10;
               ThemeCell var17;
               label173: {
                  var10 = (Theme.ThemeInfo)var21.get(var2);
                  var17 = (ThemeCell)var1.itemView;
                  if (var2 == var21.size() - 1) {
                     var8 = var5;
                     if (!ThemeActivity.this.hasCustomThemes) {
                        break label173;
                     }
                  }

                  var8 = true;
               }

               var17.setTheme(var10, var8);
               break;
            case 1:
               TextSettingsCell var20 = (TextSettingsCell)var1.itemView;
               if (var2 == ThemeActivity.this.nightThemeRow) {
                  if (Theme.selectedAutoNightType != 0 && Theme.getCurrentNightTheme() != null) {
                     var20.setTextAndValue(LocaleController.getString("AutoNightTheme", 2131558791), Theme.getCurrentNightThemeName(), false);
                  } else {
                     var20.setTextAndValue(LocaleController.getString("AutoNightTheme", 2131558791), LocaleController.getString("AutoNightThemeOff", 2131558792), false);
                  }
               } else if (var2 == ThemeActivity.this.scheduleFromRow) {
                  var2 = Theme.autoNightDayStartTime;
                  var3 = var2 / 60;
                  var20.setTextAndValue(LocaleController.getString("AutoNightFrom", 2131558786), String.format("%02d:%02d", var3, var2 - var3 * 60), true);
               } else if (var2 == ThemeActivity.this.scheduleToRow) {
                  var3 = Theme.autoNightDayEndTime;
                  var2 = var3 / 60;
                  var20.setTextAndValue(LocaleController.getString("AutoNightTo", 2131558793), String.format("%02d:%02d", var2, var3 - var2 * 60), false);
               } else if (var2 == ThemeActivity.this.scheduleUpdateLocationRow) {
                  var20.setTextAndValue(LocaleController.getString("AutoNightUpdateLocation", 2131558794), Theme.autoNightCityName, false);
               } else if (var2 == ThemeActivity.this.contactsSortRow) {
                  var2 = MessagesController.getGlobalMainSettings().getInt("sortContactsBy", 0);
                  if (var2 == 0) {
                     var16 = LocaleController.getString("Default", 2131559225);
                  } else if (var2 == 1) {
                     var16 = LocaleController.getString("FirstName", 2131560796);
                  } else {
                     var16 = LocaleController.getString("LastName", 2131560797);
                  }

                  var20.setTextAndValue(LocaleController.getString("SortBy", 2131560795), var16, true);
               } else if (var2 == ThemeActivity.this.backgroundRow) {
                  var20.setText(LocaleController.getString("ChatBackground", 2131559024), false);
               } else if (var2 == ThemeActivity.this.contactsReimportRow) {
                  var20.setText(LocaleController.getString("ImportContacts", 2131559656), true);
               } else if (var2 == ThemeActivity.this.stickersRow) {
                  var20.setText(LocaleController.getString("StickersAndMasks", 2131560807), false);
               } else if (var2 == ThemeActivity.this.emojiRow) {
                  var20.setText(LocaleController.getString("Emoji", 2131559331), true);
               } else if (var2 == ThemeActivity.this.showThemesRows) {
                  var20.setText(LocaleController.getString("ShowAllThemes", 2131560779), false);
               }
               break;
            case 2:
               TextInfoPrivacyCell var15 = (TextInfoPrivacyCell)var1.itemView;
               if (var2 == ThemeActivity.this.automaticBrightnessInfoRow) {
                  var15.setText(LocaleController.formatString("AutoNightBrightnessInfo", 2131558784, (int)(Theme.autoNightBrighnessThreshold * 100.0F)));
               } else if (var2 == ThemeActivity.this.scheduleLocationInfoRow) {
                  var15.setText(ThemeActivity.this.getLocationSunString());
               }
               break;
            case 3:
               if (var2 == ThemeActivity.this.stickersSection2Row || var2 == ThemeActivity.this.themeInfo2Row || var2 == ThemeActivity.this.nightTypeInfoRow && ThemeActivity.this.themeInfoRow == -1 || var2 == ThemeActivity.this.themeInfoRow && ThemeActivity.this.nightTypeInfoRow != -1) {
                  var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               } else {
                  var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
               }
               break;
            case 4:
               ThemeTypeCell var14 = (ThemeTypeCell)var1.itemView;
               if (var2 == ThemeActivity.this.nightDisabledRow) {
                  var18 = LocaleController.getString("AutoNightDisabled", 2131558785);
                  var8 = var7;
                  if (Theme.selectedAutoNightType == 0) {
                     var8 = true;
                  }

                  var14.setValue(var18, var8, true);
               } else if (var2 == ThemeActivity.this.nightScheduledRow) {
                  var18 = LocaleController.getString("AutoNightScheduled", 2131558790);
                  var8 = var4;
                  if (Theme.selectedAutoNightType == 1) {
                     var8 = true;
                  }

                  var14.setValue(var18, var8, true);
               } else if (var2 == ThemeActivity.this.nightAutomaticRow) {
                  var18 = LocaleController.getString("AutoNightAdaptive", 2131558782);
                  if (Theme.selectedAutoNightType != 2) {
                     var8 = false;
                  }

                  var14.setValue(var18, var8, false);
               }
               break;
            case 5:
               HeaderCell var13 = (HeaderCell)var1.itemView;
               if (var2 == ThemeActivity.this.scheduleHeaderRow) {
                  var13.setText(LocaleController.getString("AutoNightSchedule", 2131558789));
               } else if (var2 == ThemeActivity.this.automaticHeaderRow) {
                  var13.setText(LocaleController.getString("AutoNightBrightness", 2131558783));
               } else if (var2 == ThemeActivity.this.preferedHeaderRow) {
                  var13.setText(LocaleController.getString("AutoNightPreferred", 2131558788));
               } else if (var2 == ThemeActivity.this.settingsRow) {
                  var13.setText(LocaleController.getString("SETTINGS", 2131560623));
               } else if (var2 == ThemeActivity.this.themeHeaderRow) {
                  if (ThemeActivity.this.currentType == 2) {
                     var13.setText(LocaleController.getString("BuiltInThemes", 2131558863));
                  } else {
                     var13.setText(LocaleController.getString("ColorTheme", 2131559129));
                  }
               } else if (var2 == ThemeActivity.this.textSizeHeaderRow) {
                  var13.setText(LocaleController.getString("TextSizeHeader", 2131560888));
               } else if (var2 == ThemeActivity.this.chatListHeaderRow) {
                  var13.setText(LocaleController.getString("ChatList", 2131559039));
               } else if (var2 == ThemeActivity.this.themeHeader2Row) {
                  var13.setText(LocaleController.getString("CustomThemes", 2131559192));
               }
               break;
            case 6:
               ((BrightnessControlCell)var1.itemView).setProgress(Theme.autoNightBrighnessThreshold);
               break;
            case 7:
               TextCheckCell var12 = (TextCheckCell)var1.itemView;
               if (var2 == ThemeActivity.this.scheduleLocationRow) {
                  var12.setTextAndCheck(LocaleController.getString("AutoNightLocation", 2131558787), Theme.autoNightScheduleByLocation, true);
               } else {
                  SharedPreferences var9;
                  if (var2 == ThemeActivity.this.enableAnimationsRow) {
                     var9 = MessagesController.getGlobalMainSettings();
                     var12.setTextAndCheck(LocaleController.getString("EnableAnimations", 2131559348), var9.getBoolean("view_animations", true), true);
                  } else if (var2 == ThemeActivity.this.sendByEnterRow) {
                     var9 = MessagesController.getGlobalMainSettings();
                     var12.setTextAndCheck(LocaleController.getString("SendByEnter", 2131560688), var9.getBoolean("send_by_enter", false), true);
                  } else if (var2 == ThemeActivity.this.saveToGalleryRow) {
                     var12.setTextAndCheck(LocaleController.getString("SaveToGallerySettings", 2131560631), SharedConfig.saveToGallery, false);
                  } else if (var2 == ThemeActivity.this.raiseToSpeakRow) {
                     var12.setTextAndCheck(LocaleController.getString("RaiseToSpeak", 2131560528), SharedConfig.raiseToSpeak, true);
                  } else if (var2 == ThemeActivity.this.customTabsRow) {
                     var12.setTextAndValueAndCheck(LocaleController.getString("ChromeCustomTabs", 2131559100), LocaleController.getString("ChromeCustomTabsInfo", 2131559101), SharedConfig.customTabs, false, true);
                  } else if (var2 == ThemeActivity.this.directShareRow) {
                     var12.setTextAndValueAndCheck(LocaleController.getString("DirectShare", 2131559268), LocaleController.getString("DirectShareInfo", 2131559269), SharedConfig.directShare, false, true);
                  }
               }
            }
         } else {
            NotificationsCheckCell var19 = (NotificationsCheckCell)var1.itemView;
            if (var2 == ThemeActivity.this.nightThemeRow) {
               var8 = var6;
               if (Theme.selectedAutoNightType != 0) {
                  var8 = true;
               }

               if (var8) {
                  var16 = Theme.getCurrentNightThemeName();
               } else {
                  var16 = LocaleController.getString("AutoNightThemeOff", 2131558792);
               }

               var18 = var16;
               if (var8) {
                  if (Theme.selectedAutoNightType == 1) {
                     var18 = LocaleController.getString("AutoNightScheduled", 2131558790);
                  } else {
                     var18 = LocaleController.getString("AutoNightAdaptive", 2131558782);
                  }

                  StringBuilder var11 = new StringBuilder();
                  var11.append(var18);
                  var11.append(" ");
                  var11.append(var16);
                  var18 = var11.toString();
               }

               var19.setTextAndValueAndCheck(LocaleController.getString("AutoNightTheme", 2131558791), var18, var8, true);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         boolean var3 = false;
         Object var5;
         switch(var2) {
         case 0:
            Context var7 = this.mContext;
            if (ThemeActivity.this.currentType == 1) {
               var3 = true;
            }

            ThemeCell var4 = new ThemeCell(var7, var3);
            var4.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            var5 = var4;
            if (ThemeActivity.this.currentType != 1) {
               var4.setOnOptionsClick(new _$$Lambda$ThemeActivity$ListAdapter$pjEslbWZHQ4g_Rxni_i_jc6xbJY(this));
               var5 = var4;
            }
            break;
         case 1:
            var5 = new TextSettingsCell(this.mContext);
            ((View)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 2:
            var5 = new TextInfoPrivacyCell(this.mContext);
            ((View)var5).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
            break;
         case 3:
            var5 = new ShadowSectionCell(this.mContext);
            break;
         case 4:
            var5 = new ThemeTypeCell(this.mContext);
            ((View)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 5:
            var5 = new HeaderCell(this.mContext);
            ((View)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 6:
            var5 = new BrightnessControlCell(this.mContext) {
               protected void didChangedValue(float var1) {
                  int var2 = (int)(Theme.autoNightBrighnessThreshold * 100.0F);
                  int var3 = (int)(var1 * 100.0F);
                  Theme.autoNightBrighnessThreshold = var1;
                  if (var2 != var3) {
                     RecyclerListView.Holder var4 = (RecyclerListView.Holder)ThemeActivity.this.listView.findViewHolderForAdapterPosition(ThemeActivity.this.automaticBrightnessInfoRow);
                     if (var4 != null) {
                        ((TextInfoPrivacyCell)var4.itemView).setText(LocaleController.formatString("AutoNightBrightnessInfo", 2131558784, (int)(Theme.autoNightBrighnessThreshold * 100.0F)));
                     }

                     Theme.checkAutoNightThemeConditions(true);
                  }

               }
            };
            ((View)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 7:
            var5 = new TextCheckCell(this.mContext);
            ((TextCheckCell)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 8:
            var5 = ThemeActivity.this.new TextSizeCell(this.mContext);
            ((View)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 9:
            var5 = new ChatListCell(this.mContext) {
               protected void didSelectChatType(boolean var1) {
                  SharedConfig.setUseThreeLinesLayout(var1);
               }
            };
            ((View)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 10:
            var5 = new NotificationsCheckCell(this.mContext, 21, 64);
            ((View)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         default:
            var5 = new RecyclerListView(this.mContext) {
               public void onDraw(Canvas var1) {
                  super.onDraw(var1);
                  if (ThemeActivity.this.hasCustomThemes) {
                     float var2;
                     if (LocaleController.isRTL) {
                        var2 = 0.0F;
                     } else {
                        var2 = (float)AndroidUtilities.dp(20.0F);
                     }

                     float var3 = (float)(this.getMeasuredHeight() - 1);
                     int var4 = this.getMeasuredWidth();
                     int var5;
                     if (LocaleController.isRTL) {
                        var5 = AndroidUtilities.dp(20.0F);
                     } else {
                        var5 = 0;
                     }

                     var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                  }

               }

               public boolean onInterceptTouchEvent(MotionEvent var1) {
                  if (this.getParent() != null && this.getParent().getParent() != null) {
                     this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                  }

                  return super.onInterceptTouchEvent(var1);
               }

               public void setBackgroundColor(int var1) {
                  super.setBackgroundColor(var1);
                  this.invalidateViews();
               }
            };
            ((ViewGroup)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            ((RecyclerView)var5).setItemAnimator((RecyclerView.ItemAnimator)null);
            ((ViewGroup)var5).setLayoutAnimation((LayoutAnimationController)null);
            LinearLayoutManager var6 = new LinearLayoutManager(this.mContext) {
               public boolean supportsPredictiveItemAnimations() {
                  return false;
               }
            };
            ((ViewGroup)var5).setPadding(0, 0, 0, 0);
            ((RecyclerView)var5).setClipToPadding(false);
            var6.setOrientation(0);
            ((RecyclerView)var5).setLayoutManager(var6);
            ((RecyclerListView)var5).setAdapter(ThemeActivity.this.new InnerListAdapter(this.mContext));
            ((RecyclerListView)var5).setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ThemeActivity$ListAdapter$dquXXwWPa2MGvu2xAYM60dhzabo(this)));
            ((RecyclerListView)var5).setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$ThemeActivity$ListAdapter$pvw4GcZiIzN9zYxDAOmGDBqZDj0(this)));
            ThemeActivity.this.innerListView = (RecyclerListView)var5;
            ((View)var5).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(148.0F)));
         }

         return new RecyclerListView.Holder((View)var5);
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         if (var2 == 4) {
            ThemeTypeCell var3 = (ThemeTypeCell)var1.itemView;
            boolean var4;
            if (var1.getAdapterPosition() == Theme.selectedAutoNightType) {
               var4 = true;
            } else {
               var4 = false;
            }

            var3.setTypeChecked(var4);
         } else if (var2 == 0) {
            ((ThemeCell)var1.itemView).updateCurrentThemeCheck();
         }

         if (var2 != 2 && var2 != 3) {
            var1.itemView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

      }
   }

   public interface SizeChooseViewDelegate {
      void onSizeChanged();
   }

   private class TextSizeCell extends FrameLayout {
      private ChatMessageCell[] cells = new ChatMessageCell[2];
      private int endFontSize = 30;
      private int lastWidth;
      private LinearLayout messagesContainer;
      private Drawable shadowDrawable;
      private SeekBarView sizeBar;
      private int startFontSize = 12;
      private TextPaint textPaint;

      public TextSizeCell(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.textPaint = new TextPaint(1);
         this.textPaint.setTextSize((float)AndroidUtilities.dp(16.0F));
         this.shadowDrawable = Theme.getThemedDrawable(var2, 2131165395, "windowBackgroundGrayShadow");
         this.sizeBar = new SeekBarView(var2);
         this.sizeBar.setReportChanges(true);
         this.sizeBar.setDelegate(new _$$Lambda$ThemeActivity$TextSizeCell$Ci0_0LdqTC4U6xi9evAA0pUhylM(this));
         this.addView(this.sizeBar, LayoutHelper.createFrame(-1, 38.0F, 51, 9.0F, 5.0F, 43.0F, 0.0F));
         this.messagesContainer = new LinearLayout(var2) {
            private Drawable backgroundDrawable;
            private Drawable oldBackgroundDrawable;

            protected void dispatchSetPressed(boolean var1) {
            }

            public boolean dispatchTouchEvent(MotionEvent var1) {
               return false;
            }

            protected void onDraw(Canvas var1) {
               Drawable var2 = Theme.getCachedWallpaperNonBlocking();
               if (var2 != this.backgroundDrawable && var2 != null) {
                  if (Theme.isAnimatingColor()) {
                     this.oldBackgroundDrawable = this.backgroundDrawable;
                  }

                  this.backgroundDrawable = var2;
               }

               float var3 = ThemeActivity.access$300(ThemeActivity.this).getThemeAnimationValue();

               for(int var4 = 0; var4 < 2; ++var4) {
                  if (var4 == 0) {
                     var2 = this.oldBackgroundDrawable;
                  } else {
                     var2 = this.backgroundDrawable;
                  }

                  if (var2 != null) {
                     if (var4 == 1 && this.oldBackgroundDrawable != null && ThemeActivity.access$400(ThemeActivity.this) != null) {
                        var2.setAlpha((int)(255.0F * var3));
                     } else {
                        var2.setAlpha(255);
                     }

                     if (var2 instanceof ColorDrawable) {
                        var2.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                        var2.draw(var1);
                     } else if (var2 instanceof BitmapDrawable) {
                        float var5;
                        if (((BitmapDrawable)var2).getTileModeX() == TileMode.REPEAT) {
                           var1.save();
                           var5 = 2.0F / AndroidUtilities.density;
                           var1.scale(var5, var5);
                           var2.setBounds(0, 0, (int)Math.ceil((double)((float)this.getMeasuredWidth() / var5)), (int)Math.ceil((double)((float)this.getMeasuredHeight() / var5)));
                           var2.draw(var1);
                           var1.restore();
                        } else {
                           int var6 = this.getMeasuredHeight();
                           float var7 = (float)this.getMeasuredWidth() / (float)var2.getIntrinsicWidth();
                           float var8 = (float)var6 / (float)var2.getIntrinsicHeight();
                           var5 = var7;
                           if (var7 < var8) {
                              var5 = var8;
                           }

                           int var9 = (int)Math.ceil((double)((float)var2.getIntrinsicWidth() * var5));
                           int var10 = (int)Math.ceil((double)((float)var2.getIntrinsicHeight() * var5));
                           int var11 = (this.getMeasuredWidth() - var9) / 2;
                           var6 = (var6 - var10) / 2;
                           var1.save();
                           var1.clipRect(0, 0, var9, this.getMeasuredHeight());
                           var2.setBounds(var11, var6, var9 + var11, var10 + var6);
                           var2.draw(var1);
                           var1.restore();
                        }
                     }

                     if (var4 == 0 && this.oldBackgroundDrawable != null && var3 >= 1.0F) {
                        this.oldBackgroundDrawable = null;
                     }
                  }
               }

               TextSizeCell.this.shadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
               TextSizeCell.this.shadowDrawable.draw(var1);
            }

            public boolean onInterceptTouchEvent(MotionEvent var1) {
               return false;
            }

            public boolean onTouchEvent(MotionEvent var1) {
               return false;
            }
         };
         this.messagesContainer.setOrientation(1);
         this.messagesContainer.setWillNotDraw(false);
         this.messagesContainer.setPadding(0, AndroidUtilities.dp(11.0F), 0, AndroidUtilities.dp(11.0F));
         this.addView(this.messagesContainer, LayoutHelper.createFrame(-1, -2.0F, 51, 0.0F, 53.0F, 0.0F, 0.0F));
         int var3 = (int)(System.currentTimeMillis() / 1000L) - 3600;
         TLRPC.TL_message var4 = new TLRPC.TL_message();
         var4.message = LocaleController.getString("FontSizePreviewReply", 2131559502);
         int var5 = var3 + 60;
         var4.date = var5;
         var4.dialog_id = 1L;
         var4.flags = 259;
         var4.from_id = UserConfig.getInstance(ThemeActivity.access$600(ThemeActivity.this)).getClientUserId();
         var4.id = 1;
         var4.media = new TLRPC.TL_messageMediaEmpty();
         var4.out = true;
         var4.to_id = new TLRPC.TL_peerUser();
         var4.to_id.user_id = 0;
         MessageObject var9 = new MessageObject(ThemeActivity.access$700(ThemeActivity.this), var4, true);
         TLRPC.TL_message var6 = new TLRPC.TL_message();
         var6.message = LocaleController.getString("FontSizePreviewLine2", 2131559500);
         var6.date = var3 + 960;
         var6.dialog_id = 1L;
         var6.flags = 259;
         var6.from_id = UserConfig.getInstance(ThemeActivity.access$800(ThemeActivity.this)).getClientUserId();
         var6.id = 1;
         var6.media = new TLRPC.TL_messageMediaEmpty();
         var6.out = true;
         var6.to_id = new TLRPC.TL_peerUser();
         var6.to_id.user_id = 0;
         MessageObject var11 = new MessageObject(ThemeActivity.access$900(ThemeActivity.this), var6, true);
         var11.resetLayout();
         var11.eventId = 1L;
         TLRPC.TL_message var7 = new TLRPC.TL_message();
         var7.message = LocaleController.getString("FontSizePreviewLine1", 2131559499);
         var7.date = var5;
         var7.dialog_id = 1L;
         var7.flags = 265;
         var7.from_id = 0;
         var7.id = 1;
         var7.reply_to_msg_id = 5;
         var7.media = new TLRPC.TL_messageMediaEmpty();
         var7.out = false;
         var7.to_id = new TLRPC.TL_peerUser();
         var7.to_id.user_id = UserConfig.getInstance(ThemeActivity.access$1000(ThemeActivity.this)).getClientUserId();
         MessageObject var12 = new MessageObject(ThemeActivity.access$1100(ThemeActivity.this), var7, true);
         var12.customReplyName = LocaleController.getString("FontSizePreviewName", 2131559501);
         var12.eventId = 1L;
         var12.resetLayout();
         var12.replyMessageObject = var9;
         var5 = 0;

         while(true) {
            ChatMessageCell[] var10 = this.cells;
            if (var5 >= var10.length) {
               return;
            }

            var10[var5] = new ChatMessageCell(var2);
            this.cells[var5].setDelegate(new ChatMessageCell.ChatMessageCellDelegate() {
               // $FF: synthetic method
               public boolean canPerformActions() {
                  return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$canPerformActions(this);
               }

               // $FF: synthetic method
               public void didLongPress(ChatMessageCell var1, float var2, float var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didLongPress(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressBotButton(ChatMessageCell var1, TLRPC.KeyboardButton var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressBotButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressCancelSendButton(ChatMessageCell var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressCancelSendButton(this, var1);
               }

               // $FF: synthetic method
               public void didPressChannelAvatar(ChatMessageCell var1, TLRPC.Chat var2, int var3, float var4, float var5) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressChannelAvatar(this, var1, var2, var3, var4, var5);
               }

               // $FF: synthetic method
               public void didPressHiddenForward(ChatMessageCell var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressHiddenForward(this, var1);
               }

               // $FF: synthetic method
               public void didPressImage(ChatMessageCell var1, float var2, float var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressImage(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressInstantButton(ChatMessageCell var1, int var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressInstantButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressOther(ChatMessageCell var1, float var2, float var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressOther(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressReplyMessage(ChatMessageCell var1, int var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressReplyMessage(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressShare(ChatMessageCell var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressShare(this, var1);
               }

               // $FF: synthetic method
               public void didPressUrl(MessageObject var1, CharacterStyle var2, boolean var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressUrl(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressUserAvatar(ChatMessageCell var1, TLRPC.User var2, float var3, float var4) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressUserAvatar(this, var1, var2, var3, var4);
               }

               // $FF: synthetic method
               public void didPressViaBot(ChatMessageCell var1, String var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressViaBot(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressVoteButton(ChatMessageCell var1, TLRPC.TL_pollAnswer var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressVoteButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didStartVideoStream(MessageObject var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didStartVideoStream(this, var1);
               }

               // $FF: synthetic method
               public boolean isChatAdminCell(int var1) {
                  return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$isChatAdminCell(this, var1);
               }

               // $FF: synthetic method
               public void needOpenWebView(String var1, String var2, String var3, String var4, int var5, int var6) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$needOpenWebView(this, var1, var2, var3, var4, var5, var6);
               }

               // $FF: synthetic method
               public boolean needPlayMessage(MessageObject var1) {
                  return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$needPlayMessage(this, var1);
               }

               // $FF: synthetic method
               public void videoTimerReached() {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$videoTimerReached(this);
               }
            });
            var10 = this.cells;
            var10[var5].isChat = false;
            var10[var5].setFullyDraw(true);
            ChatMessageCell var8 = this.cells[var5];
            if (var5 == 0) {
               var9 = var12;
            } else {
               var9 = var11;
            }

            var8.setMessageObject(var9, (MessageObject.GroupedMessages)null, false, false);
            this.messagesContainer.addView(this.cells[var5], LayoutHelper.createLinear(-1, -2));
            ++var5;
         }
      }

      public void invalidate() {
         super.invalidate();
         this.messagesContainer.invalidate();
         this.sizeBar.invalidate();
         int var1 = 0;

         while(true) {
            ChatMessageCell[] var2 = this.cells;
            if (var1 >= var2.length) {
               return;
            }

            var2[var1].invalidate();
            ++var1;
         }
      }

      // $FF: synthetic method
      public void lambda$new$0$ThemeActivity$TextSizeCell(float var1) {
         int var2 = this.startFontSize;
         var2 = Math.round((float)var2 + (float)(this.endFontSize - var2) * var1);
         if (var2 != SharedConfig.fontSize) {
            SharedConfig.fontSize = var2;
            Editor var3 = MessagesController.getGlobalMainSettings().edit();
            var3.putInt("fons_size", SharedConfig.fontSize);
            var3.commit();
            Theme.chat_msgTextPaint.setTextSize((float)AndroidUtilities.dp((float)SharedConfig.fontSize));
            var2 = 0;

            while(true) {
               ChatMessageCell[] var4 = this.cells;
               if (var2 >= var4.length) {
                  break;
               }

               var4[var2].getMessageObject().resetLayout();
               this.cells[var2].requestLayout();
               ++var2;
            }
         }

      }

      protected void onDraw(Canvas var1) {
         this.textPaint.setColor(Theme.getColor("windowBackgroundWhiteValueText"));
         StringBuilder var2 = new StringBuilder();
         var2.append("");
         var2.append(SharedConfig.fontSize);
         var1.drawText(var2.toString(), (float)(this.getMeasuredWidth() - AndroidUtilities.dp(39.0F)), (float)AndroidUtilities.dp(28.0F), this.textPaint);
      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         var2 = MeasureSpec.getSize(var1);
         if (this.lastWidth != var2) {
            SeekBarView var3 = this.sizeBar;
            var1 = SharedConfig.fontSize;
            int var4 = this.startFontSize;
            var3.setProgress((float)(var1 - var4) / (float)(this.endFontSize - var4));
            this.lastWidth = var2;
         }

      }
   }
}

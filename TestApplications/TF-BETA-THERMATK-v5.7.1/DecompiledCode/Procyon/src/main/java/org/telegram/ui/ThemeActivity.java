// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.text.style.CharacterStyle;
import org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$_CC;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import android.graphics.Shader$TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Cells.ChatMessageCell;
import android.view.animation.LayoutAnimationController;
import android.view.MotionEvent;
import java.util.Iterator;
import android.os.Parcelable;
import androidx.core.content.FileProvider;
import org.telegram.messenger.FileLoader;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Map;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.FrameLayout$LayoutParams;
import android.view.View$MeasureSpec;
import android.text.TextUtils;
import android.text.TextUtils$TruncateAt;
import android.graphics.Color;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.graphics.RectF;
import org.telegram.ui.Components.RadioButton;
import android.view.ViewGroup;
import android.os.Bundle;
import android.location.Address;
import android.location.Geocoder;
import java.util.Locale;
import android.net.Uri;
import android.content.Intent;
import android.widget.Toast;
import org.telegram.ui.Components.ThemeEditorView;
import android.os.Vibrator;
import android.widget.TimePicker;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import android.app.TimePickerDialog$OnTimeSetListener;
import android.app.TimePickerDialog;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ChatListCell;
import org.telegram.ui.Cells.ThemeCell;
import org.telegram.ui.Cells.BrightnessControlCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import android.app.Activity;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.messenger.Utilities;
import java.util.Calendar;
import org.telegram.messenger.time.SunDate;
import android.os.Build$VERSION;
import org.telegram.ui.Cells.ThemeTypeCell;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.telegram.messenger.FileLog;
import android.location.LocationListener;
import org.telegram.messenger.ApplicationLoader;
import android.location.LocationManager;
import android.view.View$OnClickListener;
import android.app.Dialog;
import android.content.DialogInterface$OnShowListener;
import android.widget.TextView$OnEditorActionListener;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.LinearLayout;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.TextView;
import android.content.DialogInterface;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Cells.CheckBoxCell;
import android.view.View;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBarLayout;
import android.location.Location;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.ActionBar.Theme;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ThemeActivity extends BaseFragment implements NotificationCenterDelegate
{
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
    private ArrayList<Theme.ThemeInfo> darkThemes;
    private ArrayList<Theme.ThemeInfo> defaultThemes;
    private int directShareRow;
    private int emojiRow;
    private int enableAnimationsRow;
    private GpsLocationListener gpsLocationListener;
    boolean hasCustomThemes;
    private RecyclerListView innerListView;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private GpsLocationListener networkLocationListener;
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
    
    public ThemeActivity(final int currentType) {
        this.darkThemes = new ArrayList<Theme.ThemeInfo>();
        this.defaultThemes = new ArrayList<Theme.ThemeInfo>();
        this.gpsLocationListener = new GpsLocationListener();
        this.networkLocationListener = new GpsLocationListener();
        this.currentType = currentType;
        this.updateRows();
    }
    
    private String getLocationSunString() {
        final int autoNightSunriseTime = Theme.autoNightSunriseTime;
        final int i = autoNightSunriseTime / 60;
        final String format = String.format("%02d:%02d", i, autoNightSunriseTime - i * 60);
        final int autoNightSunsetTime = Theme.autoNightSunsetTime;
        final int j = autoNightSunsetTime / 60;
        return LocaleController.formatString("AutoNightUpdateLocationInfo", 2131558795, String.format("%02d:%02d", j, autoNightSunsetTime - j * 60), format);
    }
    
    private void openThemeCreate() {
        final EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor((Context)this.getParentActivity());
        editTextBoldCursor.setBackgroundDrawable(Theme.createEditTextDrawable((Context)this.getParentActivity(), true));
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setTitle(LocaleController.getString("NewTheme", 2131559910));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)_$$Lambda$ThemeActivity$Fq_Zy67h0RlXwDUh_E75MeVk8fE.INSTANCE);
        final LinearLayout view = new LinearLayout((Context)this.getParentActivity());
        view.setOrientation(1);
        builder.setView((View)view);
        final TextView textView = new TextView((Context)this.getParentActivity());
        textView.setText((CharSequence)LocaleController.formatString("EnterThemeName", 2131559373, new Object[0]));
        textView.setTextSize(16.0f);
        textView.setPadding(AndroidUtilities.dp(23.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(23.0f), AndroidUtilities.dp(6.0f));
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        view.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        editTextBoldCursor.setTextSize(1, 16.0f);
        editTextBoldCursor.setTextColor(Theme.getColor("dialogTextBlack"));
        editTextBoldCursor.setMaxLines(1);
        editTextBoldCursor.setLines(1);
        editTextBoldCursor.setInputType(16385);
        editTextBoldCursor.setGravity(51);
        editTextBoldCursor.setSingleLine(true);
        editTextBoldCursor.setImeOptions(6);
        editTextBoldCursor.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        editTextBoldCursor.setCursorSize(AndroidUtilities.dp(20.0f));
        editTextBoldCursor.setCursorWidth(1.5f);
        editTextBoldCursor.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
        view.addView((View)editTextBoldCursor, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 51, 24, 6, 24, 0));
        editTextBoldCursor.setOnEditorActionListener((TextView$OnEditorActionListener)_$$Lambda$ThemeActivity$oWMEMCEfOWz5AenMBirLxMHUSLA.INSTANCE);
        final AlertDialog create = builder.create();
        create.setOnShowListener((DialogInterface$OnShowListener)new _$$Lambda$ThemeActivity$pMIDVqVy4Cqc6NEOajbkSuFQwZA(editTextBoldCursor));
        this.showDialog(create);
        create.getButton(-1).setOnClickListener((View$OnClickListener)new _$$Lambda$ThemeActivity$9RPKhWFFO8KxhnQ8K0AhpMvKhFA(this, editTextBoldCursor, create));
    }
    
    private void showPermissionAlert(final boolean b) {
        if (this.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131558635));
        if (b) {
            builder.setMessage(LocaleController.getString("PermissionNoLocationPosition", 2131560418));
        }
        else {
            builder.setMessage(LocaleController.getString("PermissionNoLocation", 2131560417));
        }
        builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131560419), (DialogInterface$OnClickListener)new _$$Lambda$ThemeActivity$xPe7XQuRsh6WWeEQTpdUdkf0Mig(this));
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        this.showDialog(builder.create());
    }
    
    private void startLocationUpdate() {
        if (this.updatingLocation) {
            return;
        }
        this.updatingLocation = true;
        final LocationManager locationManager = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
        try {
            locationManager.requestLocationUpdates("gps", 1L, 0.0f, (LocationListener)this.gpsLocationListener);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            locationManager.requestLocationUpdates("network", 1L, 0.0f, (LocationListener)this.networkLocationListener);
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
    }
    
    private void stopLocationUpdate() {
        this.updatingLocation = false;
        final LocationManager locationManager = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
        locationManager.removeUpdates((LocationListener)this.gpsLocationListener);
        locationManager.removeUpdates((LocationListener)this.networkLocationListener);
    }
    
    private void updateRows() {
        final int rowCount = this.rowCount;
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
        final int currentType = this.currentType;
        final int n = 2;
        if (currentType == 0) {
            this.hasCustomThemes = false;
            this.defaultThemes.clear();
            for (int size = Theme.themes.size(), i = 0; i < size; ++i) {
                final Theme.ThemeInfo e = Theme.themes.get(i);
                if (e.pathToFile == null) {
                    this.defaultThemes.add(e);
                }
                else {
                    this.hasCustomThemes = true;
                }
            }
            Collections.sort(this.defaultThemes, (Comparator<? super Theme.ThemeInfo>)_$$Lambda$ThemeActivity$cs0N3OVBAa2T6bewE_YVZM_eTCA.INSTANCE);
            this.textSizeHeaderRow = this.rowCount++;
            this.textSizeRow = this.rowCount++;
            this.backgroundRow = this.rowCount++;
            this.newThemeInfoRow = this.rowCount++;
            this.themeHeaderRow = this.rowCount++;
            this.themeListRow = this.rowCount++;
            if (this.hasCustomThemes) {
                this.showThemesRows = this.rowCount++;
            }
            this.themeInfoRow = this.rowCount++;
            this.chatListHeaderRow = this.rowCount++;
            this.chatListRow = this.rowCount++;
            this.chatListInfoRow = this.rowCount++;
            this.settingsRow = this.rowCount++;
            this.nightThemeRow = this.rowCount++;
            this.customTabsRow = this.rowCount++;
            this.directShareRow = this.rowCount++;
            this.enableAnimationsRow = this.rowCount++;
            this.raiseToSpeakRow = this.rowCount++;
            this.sendByEnterRow = this.rowCount++;
            this.saveToGalleryRow = this.rowCount++;
            this.settings2Row = this.rowCount++;
            this.stickersRow = this.rowCount++;
            this.stickersSection2Row = this.rowCount++;
        }
        else if (currentType == 2) {
            this.darkThemes.clear();
            this.defaultThemes.clear();
            for (int size2 = Theme.themes.size(), j = 0; j < size2; ++j) {
                final Theme.ThemeInfo themeInfo = Theme.themes.get(j);
                if (themeInfo.pathToFile != null) {
                    this.darkThemes.add(themeInfo);
                }
                else {
                    this.defaultThemes.add(themeInfo);
                }
            }
            this.themeHeaderRow = this.rowCount++;
            final int rowCount2 = this.rowCount;
            this.themeStartRow = rowCount2;
            this.rowCount = rowCount2 + this.defaultThemes.size();
            final int rowCount3 = this.rowCount;
            this.themeEndRow = rowCount3;
            this.rowCount = rowCount3 + 1;
            this.themeInfoRow = rowCount3;
            if (!this.darkThemes.isEmpty()) {
                this.themeHeader2Row = this.rowCount++;
                final int rowCount4 = this.rowCount;
                this.themeStart2Row = rowCount4;
                this.rowCount = rowCount4 + this.darkThemes.size();
                final int rowCount5 = this.rowCount;
                this.themeEnd2Row = rowCount5;
                this.rowCount = rowCount5 + 1;
                this.themeInfo2Row = rowCount5;
            }
        }
        else {
            this.darkThemes.clear();
            for (int size3 = Theme.themes.size(), k = 0; k < size3; ++k) {
                final Theme.ThemeInfo e2 = Theme.themes.get(k);
                if (!e2.isLight()) {
                    this.darkThemes.add(e2);
                }
            }
            this.nightDisabledRow = this.rowCount++;
            this.nightScheduledRow = this.rowCount++;
            this.nightAutomaticRow = this.rowCount++;
            this.nightTypeInfoRow = this.rowCount++;
            final int selectedAutoNightType = Theme.selectedAutoNightType;
            if (selectedAutoNightType == 1) {
                this.scheduleHeaderRow = this.rowCount++;
                this.scheduleLocationRow = this.rowCount++;
                if (Theme.autoNightScheduleByLocation) {
                    this.scheduleUpdateLocationRow = this.rowCount++;
                    this.scheduleLocationInfoRow = this.rowCount++;
                }
                else {
                    this.scheduleFromRow = this.rowCount++;
                    this.scheduleToRow = this.rowCount++;
                    this.scheduleFromToInfoRow = this.rowCount++;
                }
            }
            else if (selectedAutoNightType == 2) {
                this.automaticHeaderRow = this.rowCount++;
                this.automaticBrightnessRow = this.rowCount++;
                this.automaticBrightnessInfoRow = this.rowCount++;
            }
            if (Theme.selectedAutoNightType != 0) {
                this.preferedHeaderRow = this.rowCount++;
                final int rowCount6 = this.rowCount;
                this.themeStartRow = rowCount6;
                this.rowCount = rowCount6 + this.darkThemes.size();
                final int rowCount7 = this.rowCount;
                this.themeEndRow = rowCount7;
                this.rowCount = rowCount7 + 1;
                this.themeInfoRow = rowCount7;
            }
        }
        final ListAdapter listAdapter = this.listAdapter;
        Label_1702: {
            if (listAdapter != null) {
                if (this.currentType == 1) {
                    final int previousUpdatedType = this.previousUpdatedType;
                    if (previousUpdatedType != -1) {
                        final int n2 = this.nightTypeInfoRow + 1;
                        if (previousUpdatedType != Theme.selectedAutoNightType) {
                            for (int l = 0; l < 3; ++l) {
                                final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(l);
                                if (holder != null) {
                                    ((ThemeTypeCell)holder.itemView).setTypeChecked(l == Theme.selectedAutoNightType);
                                }
                            }
                            final int selectedAutoNightType2 = Theme.selectedAutoNightType;
                            if (selectedAutoNightType2 == 0) {
                                this.listAdapter.notifyItemRangeRemoved(n2, rowCount - n2);
                                break Label_1702;
                            }
                            int n3 = 4;
                            if (selectedAutoNightType2 == 1) {
                                final int previousUpdatedType2 = this.previousUpdatedType;
                                if (previousUpdatedType2 == 0) {
                                    this.listAdapter.notifyItemRangeInserted(n2, this.rowCount - n2);
                                    break Label_1702;
                                }
                                if (previousUpdatedType2 == 2) {
                                    this.listAdapter.notifyItemRangeRemoved(n2, 3);
                                    final ListAdapter listAdapter2 = this.listAdapter;
                                    if (!Theme.autoNightScheduleByLocation) {
                                        n3 = 5;
                                    }
                                    ((RecyclerView.Adapter)listAdapter2).notifyItemRangeInserted(n2, n3);
                                }
                                break Label_1702;
                            }
                            else {
                                if (selectedAutoNightType2 != 2) {
                                    break Label_1702;
                                }
                                final int previousUpdatedType3 = this.previousUpdatedType;
                                if (previousUpdatedType3 == 0) {
                                    this.listAdapter.notifyItemRangeInserted(n2, this.rowCount - n2);
                                    break Label_1702;
                                }
                                if (previousUpdatedType3 == 1) {
                                    final ListAdapter listAdapter3 = this.listAdapter;
                                    if (!Theme.autoNightScheduleByLocation) {
                                        n3 = 5;
                                    }
                                    ((RecyclerView.Adapter)listAdapter3).notifyItemRangeRemoved(n2, n3);
                                    this.listAdapter.notifyItemRangeInserted(n2, 3);
                                }
                                break Label_1702;
                            }
                        }
                        else {
                            final boolean previousByLocation = this.previousByLocation;
                            final boolean autoNightScheduleByLocation = Theme.autoNightScheduleByLocation;
                            if (previousByLocation != autoNightScheduleByLocation) {
                                final int n4 = n2 + 2;
                                int n5;
                                if (autoNightScheduleByLocation) {
                                    n5 = 3;
                                }
                                else {
                                    n5 = 2;
                                }
                                ((RecyclerView.Adapter)listAdapter).notifyItemRangeRemoved(n4, n5);
                                final ListAdapter listAdapter4 = this.listAdapter;
                                int n6;
                                if (Theme.autoNightScheduleByLocation) {
                                    n6 = n;
                                }
                                else {
                                    n6 = 3;
                                }
                                ((RecyclerView.Adapter)listAdapter4).notifyItemRangeInserted(n4, n6);
                            }
                            break Label_1702;
                        }
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
    
    private void updateSunTime(Location lastKnownLocation, final boolean b) {
        final LocationManager locationManager = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
        if (Build$VERSION.SDK_INT >= 23) {
            final Activity parentActivity = this.getParentActivity();
            if (parentActivity != null && parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                parentActivity.requestPermissions(new String[] { "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION" }, 2);
                return;
            }
        }
        if (this.getParentActivity() != null) {
            if (!this.getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
                return;
            }
            try {
                if (!((LocationManager)ApplicationLoader.applicationContext.getSystemService("location")).isProviderEnabled("gps")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder.setMessage(LocaleController.getString("GpsDisabledAlert", 2131559597));
                    builder.setPositiveButton(LocaleController.getString("ConnectingToProxyEnable", 2131559140), (DialogInterface$OnClickListener)new _$$Lambda$ThemeActivity$fvRbp4i9JBIdpZn90Y9NJP_7GVc(this));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                    this.showDialog(builder.create());
                    return;
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        Location lastKnownLocation3;
        try {
            final Location lastKnownLocation2 = locationManager.getLastKnownLocation("gps");
            if ((lastKnownLocation = lastKnownLocation2) == null) {
                lastKnownLocation = lastKnownLocation2;
                lastKnownLocation = locationManager.getLastKnownLocation("network");
            }
            if ((lastKnownLocation3 = lastKnownLocation) == null) {
                lastKnownLocation3 = locationManager.getLastKnownLocation("passive");
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
            lastKnownLocation3 = lastKnownLocation;
        }
        if (lastKnownLocation3 == null || b) {
            this.startLocationUpdate();
            if (lastKnownLocation3 == null) {
                return;
            }
        }
        Theme.autoNightLocationLatitude = lastKnownLocation3.getLatitude();
        Theme.autoNightLocationLongitude = lastKnownLocation3.getLongitude();
        final int[] calculateSunriseSunset = SunDate.calculateSunriseSunset(Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude);
        Theme.autoNightSunriseTime = calculateSunriseSunset[0];
        Theme.autoNightSunsetTime = calculateSunriseSunset[1];
        Theme.autoNightCityName = null;
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        Theme.autoNightLastSunCheckDay = instance.get(5);
        Utilities.globalQueue.postRunnable(new _$$Lambda$ThemeActivity$bw9bigoIiGmqIM9XZH7TFdPN3XE(this));
        final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(this.scheduleLocationInfoRow);
        if (holder != null) {
            final View itemView = holder.itemView;
            if (itemView instanceof TextInfoPrivacyCell) {
                ((TextInfoPrivacyCell)itemView).setText(this.getLocationSunString());
            }
        }
        if (Theme.autoNightScheduleByLocation && Theme.selectedAutoNightType == 1) {
            Theme.checkAutoNightThemeConditions();
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(false);
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        final int currentType = this.currentType;
        if (currentType == 0) {
            super.actionBar.setTitle(LocaleController.getString("ChatSettings", 2131559043));
            final ActionBarMenuItem addItem = super.actionBar.createMenu().addItem(0, 2131165416);
            addItem.setContentDescription((CharSequence)LocaleController.getString("AccDescrMoreOptions", 2131558443));
            addItem.addSubItem(1, 2131165589, LocaleController.getString("CreateNewThemeMenu", 2131559173));
        }
        else if (currentType == 2) {
            super.actionBar.setTitle(LocaleController.getString("ColorThemes", 2131559130));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("AutoNightTheme", 2131558791));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ThemeActivity.this.finishFragment();
                }
                else if (n == 1) {
                    if (ThemeActivity.this.getParentActivity() == null) {
                        return;
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)ThemeActivity.this.getParentActivity());
                    builder.setTitle(LocaleController.getString("NewTheme", 2131559910));
                    builder.setMessage(LocaleController.getString("CreateNewThemeAlert", 2131559171));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                    builder.setPositiveButton(LocaleController.getString("CreateTheme", 2131559174), (DialogInterface$OnClickListener)new _$$Lambda$ThemeActivity$1$ZQnhOSOAx8cfjiv91xqtf3q_RU0(this));
                    ThemeActivity.this.showDialog(builder.create());
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        final FrameLayout fragmentView = new FrameLayout(context);
        fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        super.fragmentView = (View)fragmentView;
        (this.listView = new RecyclerListView(context)).setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false)));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        ((DefaultItemAnimator)this.listView.getItemAnimator()).setDelayAnimations(false);
        fragmentView.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)new _$$Lambda$ThemeActivity$6AbNGVXM3fzlqnkK4ORVG2_WTt4(this));
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.locationPermissionGranted) {
            this.updateSunTime(null, true);
        }
        else if (n == NotificationCenter.didSetNewWallpapper) {
            final RecyclerListView listView = this.listView;
            if (listView != null) {
                listView.invalidateViews();
            }
        }
        else if (n == NotificationCenter.themeListUpdated) {
            this.updateRows();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, BrightnessControlCell.class, ThemeTypeCell.class, ThemeCell.class, TextSizeCell.class, ChatListCell.class, NotificationsCheckCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)this.innerListView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "actionBarDefaultSubmenuItemIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, 0, new Class[] { ThemeCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { ThemeCell.class }, new String[] { "checkImage" }, null, null, null, "featuredStickers_addedIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { ThemeCell.class }, new String[] { "optionsButton" }, null, null, null, "stickers_menu"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { BrightnessControlCell.class }, new String[] { "leftImageView" }, null, null, null, "profile_actionIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { BrightnessControlCell.class }, new String[] { "rightImageView" }, null, null, null, "profile_actionIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { BrightnessControlCell.class }, new String[] { "seekBarView" }, null, null, null, "player_progressBackground"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[] { BrightnessControlCell.class }, new String[] { "seekBarView" }, null, null, null, "player_progress"), new ThemeDescription((View)this.listView, 0, new Class[] { ThemeTypeCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { ThemeTypeCell.class }, new String[] { "checkImage" }, null, null, null, "featuredStickers_addedIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[] { TextSizeCell.class }, new String[] { "sizeBar" }, null, null, null, "player_progress"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSizeCell.class }, new String[] { "sizeBar" }, null, null, null, "player_progressBackground"), new ThemeDescription((View)this.listView, 0, new Class[] { ChatListCell.class }, null, null, null, "radioBackground"), new ThemeDescription((View)this.listView, 0, new Class[] { ChatListCell.class }, null, null, null, "radioBackgroundChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable }, null, "chat_inBubble"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable }, null, "chat_inBubbleSelected"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgInShadowDrawable, Theme.chat_msgInMediaShadowDrawable }, null, "chat_inBubbleShadow"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable }, null, "chat_outBubble"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable }, null, "chat_outBubbleSelected"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgOutShadowDrawable, Theme.chat_msgOutMediaShadowDrawable }, null, "chat_outBubbleShadow"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_messageTextIn"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_messageTextOut"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgOutCheckDrawable, Theme.chat_msgOutHalfCheckDrawable }, null, "chat_outSentCheck"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgOutCheckSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable }, null, "chat_outSentCheckSelected"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable }, null, "chat_mediaSentCheck"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inReplyLine"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outReplyLine"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inReplyNameText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outReplyNameText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inReplyMessageText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outReplyMessageText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inReplyMediaMessageSelectedText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outReplyMediaMessageSelectedText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inTimeText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outTimeText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inTimeSelectedText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outTimeSelectedText") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.themeListUpdated);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        this.stopLocationUpdate();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.themeListUpdated);
        Theme.saveAutoNightThemeConfig();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    private class GpsLocationListener implements LocationListener
    {
        public void onLocationChanged(final Location location) {
            if (location == null) {
                return;
            }
            ThemeActivity.this.stopLocationUpdate();
            ThemeActivity.this.updateSunTime(location, false);
        }
        
        public void onProviderDisabled(final String s) {
        }
        
        public void onProviderEnabled(final String s) {
        }
        
        public void onStatusChanged(final String s, final int n, final Bundle bundle) {
        }
    }
    
    private class InnerListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public InnerListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return ThemeActivity.this.defaultThemes.size();
        }
        
        @Override
        public int getItemViewType(final int n) {
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
            final InnerThemeView innerThemeView = (InnerThemeView)viewHolder.itemView;
            final Theme.ThemeInfo themeInfo = ThemeActivity.this.defaultThemes.get(index);
            final int size = ThemeActivity.this.defaultThemes.size();
            boolean b = true;
            final boolean b2 = index == size - 1;
            if (index != 0) {
                b = false;
            }
            innerThemeView.setTheme(themeInfo, b2, b);
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            return new RecyclerListView.Holder((View)new InnerThemeView(this.mContext));
        }
    }
    
    private class InnerThemeView extends FrameLayout
    {
        private RadioButton button;
        private Drawable inDrawable;
        private boolean isFirst;
        private boolean isLast;
        private Drawable outDrawable;
        private Paint paint;
        private RectF rect;
        private TextPaint textPaint;
        private Theme.ThemeInfo themeInfo;
        
        public InnerThemeView(final Context context) {
            super(context);
            this.rect = new RectF();
            this.paint = new Paint(1);
            this.textPaint = new TextPaint(1);
            this.setWillNotDraw(false);
            this.inDrawable = context.getResources().getDrawable(2131165602).mutate();
            this.outDrawable = context.getResources().getDrawable(2131165603).mutate();
            this.textPaint.setTextSize((float)AndroidUtilities.dp(13.0f));
            (this.button = new RadioButton(context) {
                public void invalidate() {
                    super.invalidate();
                }
            }).setSize(AndroidUtilities.dp(20.0f));
            this.button.setColor(1728053247, -1);
            this.addView((View)this.button, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 22.0f, 51, 27.0f, 75.0f, 0.0f, 0.0f));
        }
        
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.button.setChecked(this.themeInfo == Theme.getCurrentTheme(), false);
        }
        
        protected void onDraw(final Canvas canvas) {
            this.paint.setColor(this.themeInfo.previewBackgroundColor);
            int dp;
            if (this.isFirst) {
                dp = AndroidUtilities.dp(22.0f);
            }
            else {
                dp = 0;
            }
            this.rect.set((float)dp, (float)AndroidUtilities.dp(11.0f), (float)(AndroidUtilities.dp(76.0f) + dp), (float)AndroidUtilities.dp(108.0f));
            canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0f), (float)AndroidUtilities.dp(6.0f), this.paint);
            if ("Arctic Blue".equals(this.themeInfo.name)) {
                final int red = Color.red(-5196358);
                final int green = Color.green(-5196358);
                final int blue = Color.blue(-5196358);
                this.button.setColor(-5000269, -13129232);
                Theme.chat_instantViewRectPaint.setColor(Color.argb(43, red, green, blue));
                canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0f), (float)AndroidUtilities.dp(6.0f), Theme.chat_instantViewRectPaint);
            }
            else {
                this.button.setColor(1728053247, -1);
            }
            this.inDrawable.setBounds(AndroidUtilities.dp(6.0f) + dp, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(49.0f) + dp, AndroidUtilities.dp(36.0f));
            this.inDrawable.draw(canvas);
            this.outDrawable.setBounds(AndroidUtilities.dp(27.0f) + dp, AndroidUtilities.dp(41.0f), AndroidUtilities.dp(70.0f) + dp, AndroidUtilities.dp(55.0f));
            this.outDrawable.draw(canvas);
            final String string = TextUtils.ellipsize((CharSequence)this.themeInfo.getName(), this.textPaint, (float)(this.getMeasuredWidth() - AndroidUtilities.dp(10.0f)), TextUtils$TruncateAt.END).toString();
            final int n = (int)Math.ceil(this.textPaint.measureText(string));
            this.textPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            canvas.drawText(string, (float)(dp + (AndroidUtilities.dp(76.0f) - n) / 2), (float)AndroidUtilities.dp(131.0f), (Paint)this.textPaint);
        }
        
        protected void onMeasure(int n, int n2) {
            final boolean isLast = this.isLast;
            n2 = 22;
            if (isLast) {
                n = 22;
            }
            else {
                n = 15;
            }
            if (!this.isFirst) {
                n2 = 0;
            }
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)(n + 76 + n2)), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(148.0f), 1073741824));
        }
        
        public void setTheme(final Theme.ThemeInfo themeInfo, final boolean isLast, final boolean isFirst) {
            this.themeInfo = themeInfo;
            this.isFirst = isFirst;
            this.isLast = isLast;
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.button.getLayoutParams();
            float n;
            if (this.isFirst) {
                n = 49.0f;
            }
            else {
                n = 27.0f;
            }
            layoutParams.leftMargin = AndroidUtilities.dp(n);
            this.button.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.inDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(themeInfo.previewInColor, PorterDuff$Mode.MULTIPLY));
            this.outDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(themeInfo.previewOutColor, PorterDuff$Mode.MULTIPLY));
        }
        
        public void updateCurrentThemeCheck() {
            this.button.setChecked(this.themeInfo == Theme.getCurrentTheme(), true);
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        private void showOptionsForTheme(final Theme.ThemeInfo themeInfo) {
            if (ThemeActivity.this.getParentActivity() == null) {
                return;
            }
            final BottomSheet.Builder builder = new BottomSheet.Builder((Context)ThemeActivity.this.getParentActivity());
            CharSequence[] array;
            if (themeInfo.pathToFile == null) {
                array = new CharSequence[] { LocaleController.getString("ShareFile", 2131560748) };
            }
            else {
                array = new CharSequence[] { LocaleController.getString("ShareFile", 2131560748), LocaleController.getString("Edit", 2131559301), LocaleController.getString("Delete", 2131559227) };
            }
            builder.setItems(array, (DialogInterface$OnClickListener)new _$$Lambda$ThemeActivity$ListAdapter$mOT1foTAY8nRoymoRurolXzJymU(this, themeInfo));
            ThemeActivity.this.showDialog(builder.create());
        }
        
        @Override
        public int getItemCount() {
            return ThemeActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == ThemeActivity.this.scheduleFromRow || n == ThemeActivity.this.emojiRow || n == ThemeActivity.this.showThemesRows || n == ThemeActivity.this.scheduleToRow || n == ThemeActivity.this.scheduleUpdateLocationRow || n == ThemeActivity.this.backgroundRow || n == ThemeActivity.this.contactsReimportRow || n == ThemeActivity.this.contactsSortRow || n == ThemeActivity.this.stickersRow) {
                return 1;
            }
            if (n == ThemeActivity.this.automaticBrightnessInfoRow || n == ThemeActivity.this.scheduleLocationInfoRow) {
                return 2;
            }
            if (n == ThemeActivity.this.themeInfoRow || n == ThemeActivity.this.nightTypeInfoRow || n == ThemeActivity.this.scheduleFromToInfoRow || n == ThemeActivity.this.stickersSection2Row || n == ThemeActivity.this.settings2Row || n == ThemeActivity.this.newThemeInfoRow || n == ThemeActivity.this.chatListInfoRow || n == ThemeActivity.this.themeInfo2Row) {
                return 3;
            }
            if (n == ThemeActivity.this.nightDisabledRow || n == ThemeActivity.this.nightScheduledRow || n == ThemeActivity.this.nightAutomaticRow) {
                return 4;
            }
            if (n == ThemeActivity.this.scheduleHeaderRow || n == ThemeActivity.this.automaticHeaderRow || n == ThemeActivity.this.preferedHeaderRow || n == ThemeActivity.this.settingsRow || n == ThemeActivity.this.themeHeaderRow || n == ThemeActivity.this.textSizeHeaderRow || n == ThemeActivity.this.chatListHeaderRow || n == ThemeActivity.this.themeHeader2Row) {
                return 5;
            }
            if (n == ThemeActivity.this.automaticBrightnessRow) {
                return 6;
            }
            if (n == ThemeActivity.this.scheduleLocationRow || n == ThemeActivity.this.enableAnimationsRow || n == ThemeActivity.this.sendByEnterRow || n == ThemeActivity.this.saveToGalleryRow || n == ThemeActivity.this.raiseToSpeakRow || n == ThemeActivity.this.customTabsRow || n == ThemeActivity.this.directShareRow) {
                return 7;
            }
            if (n == ThemeActivity.this.textSizeRow) {
                return 8;
            }
            if (n == ThemeActivity.this.chatListRow) {
                return 9;
            }
            if (n == ThemeActivity.this.nightThemeRow) {
                return 10;
            }
            if (n == ThemeActivity.this.themeListRow) {
                return 11;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b2;
            final boolean b = b2 = true;
            if (itemViewType != 0) {
                b2 = b;
                if (itemViewType != 1) {
                    b2 = b;
                    if (itemViewType != 4) {
                        b2 = b;
                        if (itemViewType != 7) {
                            b2 = b;
                            if (itemViewType != 10) {
                                b2 = (itemViewType == 11 && b);
                            }
                        }
                    }
                }
            }
            return b2;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = false;
            final boolean b2 = false;
            final boolean b3 = false;
            final boolean b4 = false;
            boolean b5 = true;
            if (itemViewType != 10) {
                switch (itemViewType) {
                    case 7: {
                        final TextCheckCell textCheckCell = (TextCheckCell)viewHolder.itemView;
                        if (n == ThemeActivity.this.scheduleLocationRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("AutoNightLocation", 2131558787), Theme.autoNightScheduleByLocation, true);
                            break;
                        }
                        if (n == ThemeActivity.this.enableAnimationsRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("EnableAnimations", 2131559348), MessagesController.getGlobalMainSettings().getBoolean("view_animations", true), true);
                            break;
                        }
                        if (n == ThemeActivity.this.sendByEnterRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("SendByEnter", 2131560688), MessagesController.getGlobalMainSettings().getBoolean("send_by_enter", false), true);
                            break;
                        }
                        if (n == ThemeActivity.this.saveToGalleryRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("SaveToGallerySettings", 2131560631), SharedConfig.saveToGallery, false);
                            break;
                        }
                        if (n == ThemeActivity.this.raiseToSpeakRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("RaiseToSpeak", 2131560528), SharedConfig.raiseToSpeak, true);
                            break;
                        }
                        if (n == ThemeActivity.this.customTabsRow) {
                            textCheckCell.setTextAndValueAndCheck(LocaleController.getString("ChromeCustomTabs", 2131559100), LocaleController.getString("ChromeCustomTabsInfo", 2131559101), SharedConfig.customTabs, false, true);
                            break;
                        }
                        if (n == ThemeActivity.this.directShareRow) {
                            textCheckCell.setTextAndValueAndCheck(LocaleController.getString("DirectShare", 2131559268), LocaleController.getString("DirectShareInfo", 2131559269), SharedConfig.directShare, false, true);
                            break;
                        }
                        break;
                    }
                    case 6: {
                        ((BrightnessControlCell)viewHolder.itemView).setProgress(Theme.autoNightBrighnessThreshold);
                        break;
                    }
                    case 5: {
                        final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                        if (n == ThemeActivity.this.scheduleHeaderRow) {
                            headerCell.setText(LocaleController.getString("AutoNightSchedule", 2131558789));
                            break;
                        }
                        if (n == ThemeActivity.this.automaticHeaderRow) {
                            headerCell.setText(LocaleController.getString("AutoNightBrightness", 2131558783));
                            break;
                        }
                        if (n == ThemeActivity.this.preferedHeaderRow) {
                            headerCell.setText(LocaleController.getString("AutoNightPreferred", 2131558788));
                            break;
                        }
                        if (n == ThemeActivity.this.settingsRow) {
                            headerCell.setText(LocaleController.getString("SETTINGS", 2131560623));
                            break;
                        }
                        if (n == ThemeActivity.this.themeHeaderRow) {
                            if (ThemeActivity.this.currentType == 2) {
                                headerCell.setText(LocaleController.getString("BuiltInThemes", 2131558863));
                                break;
                            }
                            headerCell.setText(LocaleController.getString("ColorTheme", 2131559129));
                            break;
                        }
                        else {
                            if (n == ThemeActivity.this.textSizeHeaderRow) {
                                headerCell.setText(LocaleController.getString("TextSizeHeader", 2131560888));
                                break;
                            }
                            if (n == ThemeActivity.this.chatListHeaderRow) {
                                headerCell.setText(LocaleController.getString("ChatList", 2131559039));
                                break;
                            }
                            if (n == ThemeActivity.this.themeHeader2Row) {
                                headerCell.setText(LocaleController.getString("CustomThemes", 2131559192));
                                break;
                            }
                            break;
                        }
                        break;
                    }
                    case 4: {
                        final ThemeTypeCell themeTypeCell = (ThemeTypeCell)viewHolder.itemView;
                        if (n == ThemeActivity.this.nightDisabledRow) {
                            final String string = LocaleController.getString("AutoNightDisabled", 2131558785);
                            boolean b6 = b4;
                            if (Theme.selectedAutoNightType == 0) {
                                b6 = true;
                            }
                            themeTypeCell.setValue(string, b6, true);
                            break;
                        }
                        if (n == ThemeActivity.this.nightScheduledRow) {
                            final String string2 = LocaleController.getString("AutoNightScheduled", 2131558790);
                            boolean b7 = b;
                            if (Theme.selectedAutoNightType == 1) {
                                b7 = true;
                            }
                            themeTypeCell.setValue(string2, b7, true);
                            break;
                        }
                        if (n == ThemeActivity.this.nightAutomaticRow) {
                            final String string3 = LocaleController.getString("AutoNightAdaptive", 2131558782);
                            if (Theme.selectedAutoNightType != 2) {
                                b5 = false;
                            }
                            themeTypeCell.setValue(string3, b5, false);
                            break;
                        }
                        break;
                    }
                    case 3: {
                        if (n != ThemeActivity.this.stickersSection2Row && n != ThemeActivity.this.themeInfo2Row && (n != ThemeActivity.this.nightTypeInfoRow || ThemeActivity.this.themeInfoRow != -1) && (n != ThemeActivity.this.themeInfoRow || ThemeActivity.this.nightTypeInfoRow == -1)) {
                            viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                            break;
                        }
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        break;
                    }
                    case 2: {
                        final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                        if (n == ThemeActivity.this.automaticBrightnessInfoRow) {
                            textInfoPrivacyCell.setText(LocaleController.formatString("AutoNightBrightnessInfo", 2131558784, (int)(Theme.autoNightBrighnessThreshold * 100.0f)));
                            break;
                        }
                        if (n == ThemeActivity.this.scheduleLocationInfoRow) {
                            textInfoPrivacyCell.setText(ThemeActivity.this.getLocationSunString());
                            break;
                        }
                        break;
                    }
                    case 1: {
                        final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                        if (n == ThemeActivity.this.nightThemeRow) {
                            if (Theme.selectedAutoNightType != 0 && Theme.getCurrentNightTheme() != null) {
                                textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightTheme", 2131558791), Theme.getCurrentNightThemeName(), false);
                                break;
                            }
                            textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightTheme", 2131558791), LocaleController.getString("AutoNightThemeOff", 2131558792), false);
                            break;
                        }
                        else {
                            if (n == ThemeActivity.this.scheduleFromRow) {
                                n = Theme.autoNightDayStartTime;
                                final int i = n / 60;
                                textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightFrom", 2131558786), String.format("%02d:%02d", i, n - i * 60), true);
                                break;
                            }
                            if (n == ThemeActivity.this.scheduleToRow) {
                                final int autoNightDayEndTime = Theme.autoNightDayEndTime;
                                n = autoNightDayEndTime / 60;
                                textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightTo", 2131558793), String.format("%02d:%02d", n, autoNightDayEndTime - n * 60), false);
                                break;
                            }
                            if (n == ThemeActivity.this.scheduleUpdateLocationRow) {
                                textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightUpdateLocation", 2131558794), Theme.autoNightCityName, false);
                                break;
                            }
                            if (n == ThemeActivity.this.contactsSortRow) {
                                n = MessagesController.getGlobalMainSettings().getInt("sortContactsBy", 0);
                                String s;
                                if (n == 0) {
                                    s = LocaleController.getString("Default", 2131559225);
                                }
                                else if (n == 1) {
                                    s = LocaleController.getString("FirstName", 2131560796);
                                }
                                else {
                                    s = LocaleController.getString("LastName", 2131560797);
                                }
                                textSettingsCell.setTextAndValue(LocaleController.getString("SortBy", 2131560795), s, true);
                                break;
                            }
                            if (n == ThemeActivity.this.backgroundRow) {
                                textSettingsCell.setText(LocaleController.getString("ChatBackground", 2131559024), false);
                                break;
                            }
                            if (n == ThemeActivity.this.contactsReimportRow) {
                                textSettingsCell.setText(LocaleController.getString("ImportContacts", 2131559656), true);
                                break;
                            }
                            if (n == ThemeActivity.this.stickersRow) {
                                textSettingsCell.setText(LocaleController.getString("StickersAndMasks", 2131560807), false);
                                break;
                            }
                            if (n == ThemeActivity.this.emojiRow) {
                                textSettingsCell.setText(LocaleController.getString("Emoji", 2131559331), true);
                                break;
                            }
                            if (n == ThemeActivity.this.showThemesRows) {
                                textSettingsCell.setText(LocaleController.getString("ShowAllThemes", 2131560779), false);
                                break;
                            }
                            break;
                        }
                        break;
                    }
                    case 0: {
                        ArrayList<Theme.ThemeInfo> list;
                        if (ThemeActivity.this.themeStart2Row >= 0 && n >= ThemeActivity.this.themeStart2Row) {
                            n -= ThemeActivity.this.themeStart2Row;
                            list = ThemeActivity.this.darkThemes;
                        }
                        else {
                            n -= ThemeActivity.this.themeStartRow;
                            if (ThemeActivity.this.currentType == 1) {
                                list = ThemeActivity.this.darkThemes;
                            }
                            else if (ThemeActivity.this.currentType == 2) {
                                list = ThemeActivity.this.defaultThemes;
                            }
                            else {
                                list = Theme.themes;
                            }
                        }
                        final Theme.ThemeInfo themeInfo = list.get(n);
                        final ThemeCell themeCell = (ThemeCell)viewHolder.itemView;
                        boolean b8 = false;
                        Label_1589: {
                            if (n == list.size() - 1) {
                                b8 = b2;
                                if (!ThemeActivity.this.hasCustomThemes) {
                                    break Label_1589;
                                }
                            }
                            b8 = true;
                        }
                        themeCell.setTheme(themeInfo, b8);
                        break;
                    }
                }
            }
            else {
                final NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell)viewHolder.itemView;
                if (n == ThemeActivity.this.nightThemeRow) {
                    boolean b9 = b3;
                    if (Theme.selectedAutoNightType != 0) {
                        b9 = true;
                    }
                    String str;
                    if (b9) {
                        str = Theme.getCurrentNightThemeName();
                    }
                    else {
                        str = LocaleController.getString("AutoNightThemeOff", 2131558792);
                    }
                    String string4 = str;
                    if (b9) {
                        String str2;
                        if (Theme.selectedAutoNightType == 1) {
                            str2 = LocaleController.getString("AutoNightScheduled", 2131558790);
                        }
                        else {
                            str2 = LocaleController.getString("AutoNightAdaptive", 2131558782);
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(" ");
                        sb.append(str);
                        string4 = sb.toString();
                    }
                    notificationsCheckCell.setTextAndValueAndCheck(LocaleController.getString("AutoNightTheme", 2131558791), string4, b9, true);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            boolean b = false;
            Object o = null;
            switch (n) {
                default: {
                    o = new RecyclerListView(this.mContext) {
                        @Override
                        public void onDraw(final Canvas canvas) {
                            super.onDraw(canvas);
                            if (ThemeActivity.this.hasCustomThemes) {
                                float n;
                                if (LocaleController.isRTL) {
                                    n = 0.0f;
                                }
                                else {
                                    n = (float)AndroidUtilities.dp(20.0f);
                                }
                                final float n2 = (float)(this.getMeasuredHeight() - 1);
                                final int measuredWidth = this.getMeasuredWidth();
                                int dp;
                                if (LocaleController.isRTL) {
                                    dp = AndroidUtilities.dp(20.0f);
                                }
                                else {
                                    dp = 0;
                                }
                                canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                            }
                        }
                        
                        @Override
                        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                            if (this.getParent() != null && this.getParent().getParent() != null) {
                                this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                            }
                            return super.onInterceptTouchEvent(motionEvent);
                        }
                        
                        public void setBackgroundColor(final int backgroundColor) {
                            super.setBackgroundColor(backgroundColor);
                            this.invalidateViews();
                        }
                    };
                    ((ViewGroup)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    ((RecyclerView)o).setItemAnimator(null);
                    ((ViewGroup)o).setLayoutAnimation((LayoutAnimationController)null);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext) {
                        @Override
                        public boolean supportsPredictiveItemAnimations() {
                            return false;
                        }
                    };
                    ((ViewGroup)o).setPadding(0, 0, 0, 0);
                    ((RecyclerView)o).setClipToPadding(false);
                    layoutManager.setOrientation(0);
                    ((RecyclerView)o).setLayoutManager((RecyclerView.LayoutManager)layoutManager);
                    ((RecyclerListView)o).setAdapter(new InnerListAdapter(this.mContext));
                    ((RecyclerListView)o).setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ThemeActivity$ListAdapter$dquXXwWPa2MGvu2xAYM60dhzabo(this));
                    ((RecyclerListView)o).setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$ThemeActivity$ListAdapter$pvw4GcZiIzN9zYxDAOmGDBqZDj0(this));
                    ThemeActivity.this.innerListView = (RecyclerListView)o;
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(148.0f)));
                    break;
                }
                case 10: {
                    o = new NotificationsCheckCell(this.mContext, 21, 64);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 9: {
                    o = new ChatListCell(this.mContext) {
                        @Override
                        protected void didSelectChatType(final boolean useThreeLinesLayout) {
                            SharedConfig.setUseThreeLinesLayout(useThreeLinesLayout);
                        }
                    };
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 8: {
                    o = new TextSizeCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 7: {
                    o = new TextCheckCell(this.mContext);
                    ((TextCheckCell)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 6: {
                    o = new BrightnessControlCell(this.mContext) {
                        @Override
                        protected void didChangedValue(final float autoNightBrighnessThreshold) {
                            final int n = (int)(Theme.autoNightBrighnessThreshold * 100.0f);
                            final int n2 = (int)(autoNightBrighnessThreshold * 100.0f);
                            Theme.autoNightBrighnessThreshold = autoNightBrighnessThreshold;
                            if (n != n2) {
                                final Holder holder = (Holder)ThemeActivity.this.listView.findViewHolderForAdapterPosition(ThemeActivity.this.automaticBrightnessInfoRow);
                                if (holder != null) {
                                    ((TextInfoPrivacyCell)holder.itemView).setText(LocaleController.formatString("AutoNightBrightnessInfo", 2131558784, (int)(Theme.autoNightBrighnessThreshold * 100.0f)));
                                }
                                Theme.checkAutoNightThemeConditions(true);
                            }
                        }
                    };
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 5: {
                    o = new HeaderCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 4: {
                    o = new ThemeTypeCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 3: {
                    o = new ShadowSectionCell(this.mContext);
                    break;
                }
                case 2: {
                    o = new TextInfoPrivacyCell(this.mContext);
                    ((View)o).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    break;
                }
                case 1: {
                    o = new TextSettingsCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 0: {
                    final Context mContext = this.mContext;
                    if (ThemeActivity.this.currentType == 1) {
                        b = true;
                    }
                    final ThemeCell themeCell = new ThemeCell(mContext, b);
                    ((View)themeCell).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    o = themeCell;
                    if (ThemeActivity.this.currentType != 1) {
                        themeCell.setOnOptionsClick((View$OnClickListener)new _$$Lambda$ThemeActivity$ListAdapter$pjEslbWZHQ4g_Rxni_i_jc6xbJY(this));
                        o = themeCell;
                        break;
                    }
                    break;
                }
            }
            return new RecyclerListView.Holder((View)o);
        }
        
        @Override
        public void onViewAttachedToWindow(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 4) {
                ((ThemeTypeCell)viewHolder.itemView).setTypeChecked(viewHolder.getAdapterPosition() == Theme.selectedAutoNightType);
            }
            else if (itemViewType == 0) {
                ((ThemeCell)viewHolder.itemView).updateCurrentThemeCheck();
            }
            if (itemViewType != 2 && itemViewType != 3) {
                viewHolder.itemView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
        }
    }
    
    public interface SizeChooseViewDelegate
    {
        void onSizeChanged();
    }
    
    private class TextSizeCell extends FrameLayout
    {
        private ChatMessageCell[] cells;
        private int endFontSize;
        private int lastWidth;
        private LinearLayout messagesContainer;
        private Drawable shadowDrawable;
        private SeekBarView sizeBar;
        private int startFontSize;
        private TextPaint textPaint;
        
        public TextSizeCell(final Context context) {
            super(context);
            this.cells = new ChatMessageCell[2];
            this.startFontSize = 12;
            this.endFontSize = 30;
            this.setWillNotDraw(false);
            (this.textPaint = new TextPaint(1)).setTextSize((float)AndroidUtilities.dp(16.0f));
            this.shadowDrawable = Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow");
            (this.sizeBar = new SeekBarView(context)).setReportChanges(true);
            this.sizeBar.setDelegate((SeekBarView.SeekBarViewDelegate)new _$$Lambda$ThemeActivity$TextSizeCell$Ci0_0LdqTC4U6xi9evAA0pUhylM(this));
            this.addView((View)this.sizeBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 38.0f, 51, 9.0f, 5.0f, 43.0f, 0.0f));
            (this.messagesContainer = new LinearLayout(context) {
                private Drawable backgroundDrawable;
                private Drawable oldBackgroundDrawable;
                
                protected void dispatchSetPressed(final boolean b) {
                }
                
                public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
                    return false;
                }
                
                protected void onDraw(final Canvas canvas) {
                    final Drawable cachedWallpaperNonBlocking = Theme.getCachedWallpaperNonBlocking();
                    if (cachedWallpaperNonBlocking != this.backgroundDrawable && cachedWallpaperNonBlocking != null) {
                        if (Theme.isAnimatingColor()) {
                            this.oldBackgroundDrawable = this.backgroundDrawable;
                        }
                        this.backgroundDrawable = cachedWallpaperNonBlocking;
                    }
                    final float themeAnimationValue = ThemeActivity.this.parentLayout.getThemeAnimationValue();
                    for (int i = 0; i < 2; ++i) {
                        Drawable drawable;
                        if (i == 0) {
                            drawable = this.oldBackgroundDrawable;
                        }
                        else {
                            drawable = this.backgroundDrawable;
                        }
                        if (drawable != null) {
                            if (i == 1 && this.oldBackgroundDrawable != null && ThemeActivity.this.parentLayout != null) {
                                drawable.setAlpha((int)(255.0f * themeAnimationValue));
                            }
                            else {
                                drawable.setAlpha(255);
                            }
                            if (drawable instanceof ColorDrawable) {
                                drawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                                drawable.draw(canvas);
                            }
                            else if (drawable instanceof BitmapDrawable) {
                                if (((BitmapDrawable)drawable).getTileModeX() == Shader$TileMode.REPEAT) {
                                    canvas.save();
                                    final float n = 2.0f / AndroidUtilities.density;
                                    canvas.scale(n, n);
                                    drawable.setBounds(0, 0, (int)Math.ceil(this.getMeasuredWidth() / n), (int)Math.ceil(this.getMeasuredHeight() / n));
                                    drawable.draw(canvas);
                                    canvas.restore();
                                }
                                else {
                                    final int measuredHeight = this.getMeasuredHeight();
                                    final float n2 = this.getMeasuredWidth() / (float)drawable.getIntrinsicWidth();
                                    final float n3 = measuredHeight / (float)drawable.getIntrinsicHeight();
                                    float n4 = n2;
                                    if (n2 < n3) {
                                        n4 = n3;
                                    }
                                    final int n5 = (int)Math.ceil(drawable.getIntrinsicWidth() * n4);
                                    final int n6 = (int)Math.ceil(drawable.getIntrinsicHeight() * n4);
                                    final int n7 = (this.getMeasuredWidth() - n5) / 2;
                                    final int n8 = (measuredHeight - n6) / 2;
                                    canvas.save();
                                    canvas.clipRect(0, 0, n5, this.getMeasuredHeight());
                                    drawable.setBounds(n7, n8, n5 + n7, n6 + n8);
                                    drawable.draw(canvas);
                                    canvas.restore();
                                }
                            }
                            if (i == 0 && this.oldBackgroundDrawable != null && themeAnimationValue >= 1.0f) {
                                this.oldBackgroundDrawable = null;
                            }
                        }
                    }
                    TextSizeCell.this.shadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                    TextSizeCell.this.shadowDrawable.draw(canvas);
                }
                
                public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                    return false;
                }
                
                public boolean onTouchEvent(final MotionEvent motionEvent) {
                    return false;
                }
            }).setOrientation(1);
            this.messagesContainer.setWillNotDraw(false);
            this.messagesContainer.setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f));
            this.addView((View)this.messagesContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 53.0f, 0.0f, 0.0f));
            final int n = (int)(System.currentTimeMillis() / 1000L) - 3600;
            final TLRPC.TL_message tl_message = new TLRPC.TL_message();
            tl_message.message = LocaleController.getString("FontSizePreviewReply", 2131559502);
            final int n2 = n + 60;
            tl_message.date = n2;
            tl_message.dialog_id = 1L;
            tl_message.flags = 259;
            tl_message.from_id = UserConfig.getInstance(ThemeActivity.this.currentAccount).getClientUserId();
            tl_message.id = 1;
            tl_message.media = new TLRPC.TL_messageMediaEmpty();
            tl_message.out = true;
            tl_message.to_id = new TLRPC.TL_peerUser();
            tl_message.to_id.user_id = 0;
            final MessageObject replyMessageObject = new MessageObject(ThemeActivity.this.currentAccount, tl_message, true);
            final TLRPC.TL_message tl_message2 = new TLRPC.TL_message();
            tl_message2.message = LocaleController.getString("FontSizePreviewLine2", 2131559500);
            tl_message2.date = n + 960;
            tl_message2.dialog_id = 1L;
            tl_message2.flags = 259;
            tl_message2.from_id = UserConfig.getInstance(ThemeActivity.this.currentAccount).getClientUserId();
            tl_message2.id = 1;
            tl_message2.media = new TLRPC.TL_messageMediaEmpty();
            tl_message2.out = true;
            tl_message2.to_id = new TLRPC.TL_peerUser();
            tl_message2.to_id.user_id = 0;
            final MessageObject messageObject = new MessageObject(ThemeActivity.this.currentAccount, tl_message2, true);
            messageObject.resetLayout();
            messageObject.eventId = 1L;
            final TLRPC.TL_message tl_message3 = new TLRPC.TL_message();
            tl_message3.message = LocaleController.getString("FontSizePreviewLine1", 2131559499);
            tl_message3.date = n2;
            tl_message3.dialog_id = 1L;
            tl_message3.flags = 265;
            tl_message3.from_id = 0;
            tl_message3.id = 1;
            tl_message3.reply_to_msg_id = 5;
            tl_message3.media = new TLRPC.TL_messageMediaEmpty();
            tl_message3.out = false;
            tl_message3.to_id = new TLRPC.TL_peerUser();
            tl_message3.to_id.user_id = UserConfig.getInstance(ThemeActivity.this.currentAccount).getClientUserId();
            final MessageObject messageObject2 = new MessageObject(ThemeActivity.this.currentAccount, tl_message3, true);
            messageObject2.customReplyName = LocaleController.getString("FontSizePreviewName", 2131559501);
            messageObject2.eventId = 1L;
            messageObject2.resetLayout();
            messageObject2.replyMessageObject = replyMessageObject;
            int n3 = 0;
            while (true) {
                final ChatMessageCell[] cells = this.cells;
                if (n3 >= cells.length) {
                    break;
                }
                cells[n3] = new ChatMessageCell(context);
                this.cells[n3].setDelegate((ChatMessageCell.ChatMessageCellDelegate)new ChatMessageCell.ChatMessageCellDelegate() {});
                final ChatMessageCell[] cells2 = this.cells;
                cells2[n3].isChat = false;
                cells2[n3].setFullyDraw(true);
                final ChatMessageCell chatMessageCell = this.cells[n3];
                MessageObject messageObject3;
                if (n3 == 0) {
                    messageObject3 = messageObject2;
                }
                else {
                    messageObject3 = messageObject;
                }
                chatMessageCell.setMessageObject(messageObject3, null, false, false);
                this.messagesContainer.addView((View)this.cells[n3], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                ++n3;
            }
        }
        
        public void invalidate() {
            super.invalidate();
            this.messagesContainer.invalidate();
            this.sizeBar.invalidate();
            int n = 0;
            while (true) {
                final ChatMessageCell[] cells = this.cells;
                if (n >= cells.length) {
                    break;
                }
                cells[n].invalidate();
                ++n;
            }
        }
        
        protected void onDraw(final Canvas canvas) {
            this.textPaint.setColor(Theme.getColor("windowBackgroundWhiteValueText"));
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(SharedConfig.fontSize);
            canvas.drawText(sb.toString(), (float)(this.getMeasuredWidth() - AndroidUtilities.dp(39.0f)), (float)AndroidUtilities.dp(28.0f), (Paint)this.textPaint);
        }
        
        protected void onMeasure(int fontSize, int size) {
            super.onMeasure(fontSize, size);
            size = View$MeasureSpec.getSize(fontSize);
            if (this.lastWidth != size) {
                final SeekBarView sizeBar = this.sizeBar;
                fontSize = SharedConfig.fontSize;
                final int startFontSize = this.startFontSize;
                sizeBar.setProgress((fontSize - startFontSize) / (float)(this.endFontSize - startFontSize));
                this.lastWidth = size;
            }
        }
    }
}

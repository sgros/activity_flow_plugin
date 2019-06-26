package org.telegram.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.TextUtils.TruncateAt;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public class ProxySettingsActivity extends BaseFragment {
   private static final int FIELD_IP = 0;
   private static final int FIELD_PASSWORD = 3;
   private static final int FIELD_PORT = 1;
   private static final int FIELD_SECRET = 4;
   private static final int FIELD_USER = 2;
   private static final int done_button = 1;
   private boolean addingNewProxy;
   private TextInfoPrivacyCell bottomCell;
   private SharedConfig.ProxyInfo currentProxyInfo;
   private int currentType;
   private ActionBarMenuItem doneItem;
   private HeaderCell headerCell;
   private boolean ignoreOnTextChange;
   private EditTextBoldCursor[] inputFields;
   private LinearLayout linearLayout2;
   private ScrollView scrollView;
   private ShadowSectionCell[] sectionCell = new ShadowSectionCell[2];
   private TextSettingsCell shareCell;
   private ProxySettingsActivity.TypeCell[] typeCell = new ProxySettingsActivity.TypeCell[2];

   public ProxySettingsActivity() {
      this.currentProxyInfo = new SharedConfig.ProxyInfo("", 1080, "", "", "");
      this.addingNewProxy = true;
   }

   public ProxySettingsActivity(SharedConfig.ProxyInfo var1) {
      this.currentProxyInfo = var1;
      this.currentType = TextUtils.isEmpty(var1.secret) ^ 1;
   }

   private void checkShareButton() {
      if (this.shareCell != null && this.doneItem != null) {
         EditTextBoldCursor[] var1 = this.inputFields;
         if (var1[0] != null && var1[1] != null) {
            if (var1[0].length() != 0 && Utilities.parseInt(this.inputFields[1].getText().toString()) != 0) {
               this.shareCell.getTextView().setAlpha(1.0F);
               this.doneItem.setAlpha(1.0F);
               this.shareCell.setEnabled(true);
               this.doneItem.setEnabled(true);
            } else {
               this.shareCell.getTextView().setAlpha(0.5F);
               this.doneItem.setAlpha(0.5F);
               this.shareCell.setEnabled(false);
               this.doneItem.setEnabled(false);
            }
         }
      }

   }

   private void updateUiForType() {
      int var1 = this.currentType;
      boolean var2 = true;
      if (var1 == 0) {
         this.bottomCell.setText(LocaleController.getString("UseProxyInfo", 2131560974));
         ((View)this.inputFields[4].getParent()).setVisibility(8);
         ((View)this.inputFields[3].getParent()).setVisibility(0);
         ((View)this.inputFields[2].getParent()).setVisibility(0);
      } else if (var1 == 1) {
         TextInfoPrivacyCell var3 = this.bottomCell;
         StringBuilder var4 = new StringBuilder();
         var4.append(LocaleController.getString("UseProxyTelegramInfo", 2131560984));
         var4.append("\n\n");
         var4.append(LocaleController.getString("UseProxyTelegramInfo2", 2131560985));
         var3.setText(var4.toString());
         ((View)this.inputFields[4].getParent()).setVisibility(0);
         ((View)this.inputFields[3].getParent()).setVisibility(8);
         ((View)this.inputFields[2].getParent()).setVisibility(8);
      }

      ProxySettingsActivity.TypeCell var6 = this.typeCell[0];
      boolean var5;
      if (this.currentType == 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      var6.setTypeChecked(var5);
      var6 = this.typeCell[1];
      if (this.currentType == 1) {
         var5 = var2;
      } else {
         var5 = false;
      }

      var6.setTypeChecked(var5);
   }

   public View createView(Context var1) {
      super.actionBar.setTitle(LocaleController.getString("ProxyDetails", 2131560518));
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(false);
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ProxySettingsActivity.this.finishFragment();
            } else if (var1 == 1) {
               if (ProxySettingsActivity.this.getParentActivity() == null) {
                  return;
               }

               ProxySettingsActivity.this.currentProxyInfo.address = ProxySettingsActivity.this.inputFields[0].getText().toString();
               ProxySettingsActivity.this.currentProxyInfo.port = Utilities.parseInt(ProxySettingsActivity.this.inputFields[1].getText().toString());
               if (ProxySettingsActivity.this.currentType == 0) {
                  ProxySettingsActivity.this.currentProxyInfo.secret = "";
                  ProxySettingsActivity.this.currentProxyInfo.username = ProxySettingsActivity.this.inputFields[2].getText().toString();
                  ProxySettingsActivity.this.currentProxyInfo.password = ProxySettingsActivity.this.inputFields[3].getText().toString();
               } else {
                  ProxySettingsActivity.this.currentProxyInfo.secret = ProxySettingsActivity.this.inputFields[4].getText().toString();
                  ProxySettingsActivity.this.currentProxyInfo.username = "";
                  ProxySettingsActivity.this.currentProxyInfo.password = "";
               }

               SharedPreferences var2 = MessagesController.getGlobalMainSettings();
               Editor var3 = var2.edit();
               boolean var4;
               if (ProxySettingsActivity.this.addingNewProxy) {
                  SharedConfig.addProxy(ProxySettingsActivity.this.currentProxyInfo);
                  SharedConfig.currentProxy = ProxySettingsActivity.this.currentProxyInfo;
                  var3.putBoolean("proxy_enabled", true);
                  var4 = true;
               } else {
                  var4 = var2.getBoolean("proxy_enabled", false);
                  SharedConfig.saveProxyList();
               }

               if (ProxySettingsActivity.this.addingNewProxy || SharedConfig.currentProxy == ProxySettingsActivity.this.currentProxyInfo) {
                  var3.putString("proxy_ip", ProxySettingsActivity.this.currentProxyInfo.address);
                  var3.putString("proxy_pass", ProxySettingsActivity.this.currentProxyInfo.password);
                  var3.putString("proxy_user", ProxySettingsActivity.this.currentProxyInfo.username);
                  var3.putInt("proxy_port", ProxySettingsActivity.this.currentProxyInfo.port);
                  var3.putString("proxy_secret", ProxySettingsActivity.this.currentProxyInfo.secret);
                  ConnectionsManager.setProxySettings(var4, ProxySettingsActivity.this.currentProxyInfo.address, ProxySettingsActivity.this.currentProxyInfo.port, ProxySettingsActivity.this.currentProxyInfo.username, ProxySettingsActivity.this.currentProxyInfo.password, ProxySettingsActivity.this.currentProxyInfo.secret);
               }

               var3.commit();
               NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged);
               ProxySettingsActivity.this.finishFragment();
            }

         }
      });
      this.doneItem = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      this.doneItem.setContentDescription(LocaleController.getString("Done", 2131559299));
      super.fragmentView = new FrameLayout(var1);
      View var2 = super.fragmentView;
      FrameLayout var3 = (FrameLayout)var2;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.scrollView = new ScrollView(var1);
      this.scrollView.setFillViewport(true);
      AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
      var3.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0F));
      this.linearLayout2 = new LinearLayout(var1);
      this.linearLayout2.setOrientation(1);
      this.scrollView.addView(this.linearLayout2, new LayoutParams(-1, -2));

      int var4;
      for(var4 = 0; var4 < 2; ++var4) {
         this.typeCell[var4] = new ProxySettingsActivity.TypeCell(var1);
         this.typeCell[var4].setBackgroundDrawable(Theme.getSelectorDrawable(true));
         this.typeCell[var4].setTag(var4);
         boolean var5;
         String var8;
         ProxySettingsActivity.TypeCell var10;
         if (var4 == 0) {
            var10 = this.typeCell[var4];
            var8 = LocaleController.getString("UseProxySocks5", 2131560979);
            if (var4 == this.currentType) {
               var5 = true;
            } else {
               var5 = false;
            }

            var10.setValue(var8, var5, true);
         } else if (var4 == 1) {
            var10 = this.typeCell[var4];
            var8 = LocaleController.getString("UseProxyTelegram", 2131560982);
            if (var4 == this.currentType) {
               var5 = true;
            } else {
               var5 = false;
            }

            var10.setValue(var8, var5, false);
         }

         this.linearLayout2.addView(this.typeCell[var4], LayoutHelper.createLinear(-1, 50));
         this.typeCell[var4].setOnClickListener(new _$$Lambda$ProxySettingsActivity$m9BkTAI6IqJctow06HtssG_kLaQ(this));
      }

      this.sectionCell[0] = new ShadowSectionCell(var1);
      this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
      this.inputFields = new EditTextBoldCursor[5];

      for(var4 = 0; var4 < 5; ++var4) {
         FrameLayout var9 = new FrameLayout(var1);
         this.linearLayout2.addView(var9, LayoutHelper.createLinear(-1, 64));
         var9.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         this.inputFields[var4] = new EditTextBoldCursor(var1);
         this.inputFields[var4].setTag(var4);
         this.inputFields[var4].setTextSize(1, 16.0F);
         this.inputFields[var4].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.inputFields[var4].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var4].setBackgroundDrawable((Drawable)null);
         this.inputFields[var4].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var4].setCursorSize(AndroidUtilities.dp(20.0F));
         this.inputFields[var4].setCursorWidth(1.5F);
         this.inputFields[var4].setSingleLine(true);
         EditTextBoldCursor var11 = this.inputFields[var4];
         byte var6;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         var11.setGravity(var6 | 16);
         this.inputFields[var4].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
         this.inputFields[var4].setTransformHintToHeader(true);
         this.inputFields[var4].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
         if (var4 == 0) {
            this.inputFields[var4].setInputType(524305);
            this.inputFields[var4].addTextChangedListener(new TextWatcher() {
               public void afterTextChanged(Editable var1) {
                  ProxySettingsActivity.this.checkShareButton();
               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }
            });
         } else if (var4 == 1) {
            this.inputFields[var4].setInputType(2);
            this.inputFields[var4].addTextChangedListener(new TextWatcher() {
               public void afterTextChanged(Editable var1) {
                  if (!ProxySettingsActivity.this.ignoreOnTextChange) {
                     EditTextBoldCursor var2 = ProxySettingsActivity.this.inputFields[1];
                     int var3 = var2.getSelectionStart();
                     String var4 = var2.getText().toString();
                     StringBuilder var5 = new StringBuilder(var4.length());

                     int var6;
                     int var7;
                     for(var6 = 0; var6 < var4.length(); var6 = var7) {
                        var7 = var6 + 1;
                        String var8 = var4.substring(var6, var7);
                        if ("0123456789".contains(var8)) {
                           var5.append(var8);
                        }
                     }

                     ProxySettingsActivity.this.ignoreOnTextChange = true;
                     var6 = Utilities.parseInt(var5.toString());
                     if (var6 >= 0 && var6 <= 65535 && var4.equals(var5.toString())) {
                        if (var3 >= 0) {
                           if (var3 <= var2.length()) {
                              var6 = var3;
                           } else {
                              var6 = var2.length();
                           }

                           var2.setSelection(var6);
                        }
                     } else if (var6 < 0) {
                        var2.setText("0");
                     } else if (var6 > 65535) {
                        var2.setText("65535");
                     } else {
                        var2.setText(var5.toString());
                     }

                     ProxySettingsActivity.this.ignoreOnTextChange = false;
                     ProxySettingsActivity.this.checkShareButton();
                  }
               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }
            });
         } else if (var4 == 3) {
            this.inputFields[var4].setInputType(129);
            this.inputFields[var4].setTypeface(Typeface.DEFAULT);
            this.inputFields[var4].setTransformationMethod(PasswordTransformationMethod.getInstance());
         } else {
            this.inputFields[var4].setInputType(524289);
         }

         this.inputFields[var4].setImeOptions(268435461);
         if (var4 != 0) {
            if (var4 != 1) {
               if (var4 != 2) {
                  if (var4 != 3) {
                     if (var4 == 4) {
                        this.inputFields[var4].setHintText(LocaleController.getString("UseProxySecret", 2131560977));
                        this.inputFields[var4].setText(this.currentProxyInfo.secret);
                     }
                  } else {
                     this.inputFields[var4].setHintText(LocaleController.getString("UseProxyPassword", 2131560975));
                     this.inputFields[var4].setText(this.currentProxyInfo.password);
                  }
               } else {
                  this.inputFields[var4].setHintText(LocaleController.getString("UseProxyUsername", 2131560986));
                  this.inputFields[var4].setText(this.currentProxyInfo.username);
               }
            } else {
               this.inputFields[var4].setHintText(LocaleController.getString("UseProxyPort", 2131560976));
               var11 = this.inputFields[var4];
               StringBuilder var7 = new StringBuilder();
               var7.append("");
               var7.append(this.currentProxyInfo.port);
               var11.setText(var7.toString());
            }
         } else {
            this.inputFields[var4].setHintText(LocaleController.getString("UseProxyAddress", 2131560971));
            this.inputFields[var4].setText(this.currentProxyInfo.address);
         }

         EditTextBoldCursor[] var12 = this.inputFields;
         var12[var4].setSelection(var12[var4].length());
         this.inputFields[var4].setPadding(0, 0, 0, 0);
         var9.addView(this.inputFields[var4], LayoutHelper.createFrame(-1, -1.0F, 51, 17.0F, 0.0F, 17.0F, 0.0F));
         this.inputFields[var4].setOnEditorActionListener(new _$$Lambda$ProxySettingsActivity$6uZhE0rYQm5lNBN7Lia6YJLxnRo(this));
      }

      this.bottomCell = new TextInfoPrivacyCell(var1);
      this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      this.bottomCell.setText(LocaleController.getString("UseProxyInfo", 2131560974));
      this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
      this.shareCell = new TextSettingsCell(var1);
      this.shareCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
      this.shareCell.setText(LocaleController.getString("ShareFile", 2131560748), false);
      this.shareCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
      this.linearLayout2.addView(this.shareCell, LayoutHelper.createLinear(-1, -2));
      this.shareCell.setOnClickListener(new _$$Lambda$ProxySettingsActivity$4RcOr5eJR76wYO_TQE9eb5ryku8(this));
      this.sectionCell[1] = new ShadowSectionCell(var1);
      this.sectionCell[1].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      this.linearLayout2.addView(this.sectionCell[1], LayoutHelper.createLinear(-1, -2));
      this.checkShareButton();
      this.updateUiForType();
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      ArrayList var1 = new ArrayList();
      var1.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var1.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCH, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearch"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearchPlaceholder"));
      LinearLayout var2 = this.linearLayout2;
      Paint var3 = Theme.dividerPaint;
      var1.add(new ThemeDescription(var2, 0, new Class[]{View.class}, var3, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"));
      var1.add(new ThemeDescription(this.shareCell, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      var1.add(new ThemeDescription(this.shareCell, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      var1.add(new ThemeDescription(this.shareCell, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"));
      int var4 = 0;

      while(true) {
         ProxySettingsActivity.TypeCell[] var5 = this.typeCell;
         if (var4 >= var5.length) {
            if (this.inputFields != null) {
               var4 = 0;

               while(true) {
                  EditTextBoldCursor[] var6 = this.inputFields;
                  if (var4 >= var6.length) {
                     break;
                  }

                  var1.add(new ThemeDescription((View)var6[var4].getParent(), ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
                  var1.add(new ThemeDescription(this.inputFields[var4], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                  var1.add(new ThemeDescription(this.inputFields[var4], ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
                  ++var4;
               }
            } else {
               var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
               var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
            }

            var1.add(new ThemeDescription(this.headerCell, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
            var1.add(new ThemeDescription(this.headerCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"));

            for(var4 = 0; var4 < 2; ++var4) {
               var1.add(new ThemeDescription(this.sectionCell[var4], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"));
            }

            var1.add(new ThemeDescription(this.bottomCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"));
            var1.add(new ThemeDescription(this.bottomCell, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"));
            var1.add(new ThemeDescription(this.bottomCell, ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText"));
            return (ThemeDescription[])var1.toArray(new ThemeDescription[var1.size()]);
         }

         var1.add(new ThemeDescription(var5[var4], ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
         var1.add(new ThemeDescription(this.typeCell[var4], 0, new Class[]{ProxySettingsActivity.TypeCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         var1.add(new ThemeDescription(this.typeCell[var4], ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ProxySettingsActivity.TypeCell.class}, new String[]{"checkImage"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "featuredStickers_addedIcon"));
         ++var4;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$0$ProxySettingsActivity(View var1) {
      this.currentType = (Integer)var1.getTag();
      this.updateUiForType();
   }

   // $FF: synthetic method
   public boolean lambda$createView$1$ProxySettingsActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 5) {
         var2 = (Integer)var1.getTag() + 1;
         EditTextBoldCursor[] var4 = this.inputFields;
         if (var2 < var4.length) {
            var4[var2].requestFocus();
         }

         return true;
      } else if (var2 == 6) {
         this.finishFragment();
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$2$ProxySettingsActivity(View var1) {
      StringBuilder var2 = new StringBuilder("");
      String var3 = this.inputFields[0].getText().toString();
      String var4 = this.inputFields[3].getText().toString();
      String var20 = this.inputFields[2].getText().toString();
      String var5 = this.inputFields[1].getText().toString();
      String var6 = this.inputFields[4].getText().toString();

      label132: {
         boolean var10001;
         boolean var7;
         try {
            var7 = TextUtils.isEmpty(var3);
         } catch (Exception var19) {
            var10001 = false;
            return;
         }

         if (!var7) {
            try {
               var2.append("server=");
               var2.append(URLEncoder.encode(var3, "UTF-8"));
            } catch (Exception var18) {
               var10001 = false;
               return;
            }
         }

         try {
            var7 = TextUtils.isEmpty(var5);
         } catch (Exception var17) {
            var10001 = false;
            return;
         }

         if (!var7) {
            try {
               if (var2.length() != 0) {
                  var2.append("&");
               }
            } catch (Exception var16) {
               var10001 = false;
               return;
            }

            try {
               var2.append("port=");
               var2.append(URLEncoder.encode(var5, "UTF-8"));
            } catch (Exception var15) {
               var10001 = false;
               return;
            }
         }

         label129: {
            try {
               if (this.currentType != 1) {
                  break label129;
               }
            } catch (Exception var14) {
               var10001 = false;
               return;
            }

            var20 = "https://t.me/proxy?";

            try {
               if (var2.length() != 0) {
                  var2.append("&");
               }
            } catch (Exception var9) {
               var10001 = false;
               return;
            }

            try {
               var2.append("secret=");
               var2.append(URLEncoder.encode(var6, "UTF-8"));
               break label132;
            } catch (Exception var8) {
               var10001 = false;
               return;
            }
         }

         var6 = "https://t.me/socks?";

         label94: {
            try {
               if (TextUtils.isEmpty(var20)) {
                  break label94;
               }

               if (var2.length() != 0) {
                  var2.append("&");
               }
            } catch (Exception var13) {
               var10001 = false;
               return;
            }

            try {
               var2.append("user=");
               var2.append(URLEncoder.encode(var20, "UTF-8"));
            } catch (Exception var12) {
               var10001 = false;
               return;
            }
         }

         var20 = var6;

         try {
            if (TextUtils.isEmpty(var4)) {
               break label132;
            }

            if (var2.length() != 0) {
               var2.append("&");
            }
         } catch (Exception var11) {
            var10001 = false;
            return;
         }

         try {
            var2.append("pass=");
            var2.append(URLEncoder.encode(var4, "UTF-8"));
         } catch (Exception var10) {
            var10001 = false;
            return;
         }

         var20 = var6;
      }

      if (var2.length() != 0) {
         Intent var22 = new Intent("android.intent.action.SEND");
         var22.setType("text/plain");
         StringBuilder var23 = new StringBuilder();
         var23.append(var20);
         var23.append(var2.toString());
         var22.putExtra("android.intent.extra.TEXT", var23.toString());
         Intent var21 = Intent.createChooser(var22, LocaleController.getString("ShareLink", 2131560749));
         var21.setFlags(268435456);
         this.getParentActivity().startActivity(var21);
      }
   }

   public void onResume() {
      super.onResume();
      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
   }

   protected void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1 && !var2 && this.addingNewProxy) {
         this.inputFields[0].requestFocus();
         AndroidUtilities.showKeyboard(this.inputFields[0]);
      }

   }

   public class TypeCell extends FrameLayout {
      private ImageView checkImage;
      private boolean needDivider;
      private TextView textView;

      public TypeCell(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.textView = new TextView(var2);
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.textView.setTextSize(1, 16.0F);
         this.textView.setLines(1);
         this.textView.setMaxLines(1);
         this.textView.setSingleLine(true);
         this.textView.setEllipsize(TruncateAt.END);
         TextView var8 = this.textView;
         boolean var3 = LocaleController.isRTL;
         byte var4 = 5;
         byte var5;
         if (var3) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var8.setGravity(var5 | 16);
         var8 = this.textView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         float var6;
         if (LocaleController.isRTL) {
            var6 = 71.0F;
         } else {
            var6 = 21.0F;
         }

         float var7;
         if (LocaleController.isRTL) {
            var7 = 21.0F;
         } else {
            var7 = 23.0F;
         }

         this.addView(var8, LayoutHelper.createFrame(-1, -1.0F, var5 | 48, var6, 0.0F, var7, 0.0F));
         this.checkImage = new ImageView(var2);
         this.checkImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), Mode.MULTIPLY));
         this.checkImage.setImageResource(2131165858);
         ImageView var9 = this.checkImage;
         var5 = var4;
         if (LocaleController.isRTL) {
            var5 = 3;
         }

         this.addView(var9, LayoutHelper.createFrame(19, 14.0F, var5 | 16, 21.0F, 0.0F, 21.0F, 0.0F));
      }

      protected void onDraw(Canvas var1) {
         if (this.needDivider) {
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

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0F) + this.needDivider, 1073741824));
      }

      public void setTypeChecked(boolean var1) {
         ImageView var2 = this.checkImage;
         byte var3;
         if (var1) {
            var3 = 0;
         } else {
            var3 = 4;
         }

         var2.setVisibility(var3);
      }

      public void setValue(String var1, boolean var2, boolean var3) {
         this.textView.setText(var1);
         ImageView var5 = this.checkImage;
         byte var4;
         if (var2) {
            var4 = 0;
         } else {
            var4 = 4;
         }

         var5.setVisibility(var4);
         this.needDivider = var3;
      }
   }
}

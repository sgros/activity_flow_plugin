// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.text.TextUtils$TruncateAt;
import android.widget.ImageView;
import android.content.Intent;
import java.net.URLEncoder;
import android.view.KeyEvent;
import android.widget.TextView;
import android.graphics.Paint;
import java.util.ArrayList;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.widget.TextView$OnEditorActionListener;
import android.text.method.TransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.graphics.drawable.Drawable;
import android.view.View$OnClickListener;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.view.View;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.Utilities;
import android.text.TextUtils;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.ActionBar.BaseFragment;

public class ProxySettingsActivity extends BaseFragment
{
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
    private ShadowSectionCell[] sectionCell;
    private TextSettingsCell shareCell;
    private TypeCell[] typeCell;
    
    public ProxySettingsActivity() {
        this.sectionCell = new ShadowSectionCell[2];
        this.typeCell = new TypeCell[2];
        this.currentProxyInfo = new SharedConfig.ProxyInfo("", 1080, "", "", "");
        this.addingNewProxy = true;
    }
    
    public ProxySettingsActivity(final SharedConfig.ProxyInfo currentProxyInfo) {
        this.sectionCell = new ShadowSectionCell[2];
        this.typeCell = new TypeCell[2];
        this.currentProxyInfo = currentProxyInfo;
        this.currentType = ((TextUtils.isEmpty((CharSequence)currentProxyInfo.secret) ^ true) ? 1 : 0);
    }
    
    private void checkShareButton() {
        if (this.shareCell != null && this.doneItem != null) {
            final EditTextBoldCursor[] inputFields = this.inputFields;
            if (inputFields[0] != null) {
                if (inputFields[1] != null) {
                    if (inputFields[0].length() != 0 && Utilities.parseInt(this.inputFields[1].getText().toString()) != 0) {
                        this.shareCell.getTextView().setAlpha(1.0f);
                        this.doneItem.setAlpha(1.0f);
                        this.shareCell.setEnabled(true);
                        this.doneItem.setEnabled(true);
                    }
                    else {
                        this.shareCell.getTextView().setAlpha(0.5f);
                        this.doneItem.setAlpha(0.5f);
                        this.shareCell.setEnabled(false);
                        this.doneItem.setEnabled(false);
                    }
                }
            }
        }
    }
    
    private void updateUiForType() {
        final int currentType = this.currentType;
        final boolean b = true;
        if (currentType == 0) {
            this.bottomCell.setText(LocaleController.getString("UseProxyInfo", 2131560974));
            ((View)this.inputFields[4].getParent()).setVisibility(8);
            ((View)this.inputFields[3].getParent()).setVisibility(0);
            ((View)this.inputFields[2].getParent()).setVisibility(0);
        }
        else if (currentType == 1) {
            final TextInfoPrivacyCell bottomCell = this.bottomCell;
            final StringBuilder sb = new StringBuilder();
            sb.append(LocaleController.getString("UseProxyTelegramInfo", 2131560984));
            sb.append("\n\n");
            sb.append(LocaleController.getString("UseProxyTelegramInfo2", 2131560985));
            bottomCell.setText(sb.toString());
            ((View)this.inputFields[4].getParent()).setVisibility(0);
            ((View)this.inputFields[3].getParent()).setVisibility(8);
            ((View)this.inputFields[2].getParent()).setVisibility(8);
        }
        this.typeCell[0].setTypeChecked(this.currentType == 0);
        this.typeCell[1].setTypeChecked(this.currentType == 1 && b);
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setTitle(LocaleController.getString("ProxyDetails", 2131560518));
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(false);
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ProxySettingsActivity.this.finishFragment();
                }
                else if (n == 1) {
                    if (ProxySettingsActivity.this.getParentActivity() == null) {
                        return;
                    }
                    ProxySettingsActivity.this.currentProxyInfo.address = ProxySettingsActivity.this.inputFields[0].getText().toString();
                    ProxySettingsActivity.this.currentProxyInfo.port = Utilities.parseInt(ProxySettingsActivity.this.inputFields[1].getText().toString());
                    if (ProxySettingsActivity.this.currentType == 0) {
                        ProxySettingsActivity.this.currentProxyInfo.secret = "";
                        ProxySettingsActivity.this.currentProxyInfo.username = ProxySettingsActivity.this.inputFields[2].getText().toString();
                        ProxySettingsActivity.this.currentProxyInfo.password = ProxySettingsActivity.this.inputFields[3].getText().toString();
                    }
                    else {
                        ProxySettingsActivity.this.currentProxyInfo.secret = ProxySettingsActivity.this.inputFields[4].getText().toString();
                        ProxySettingsActivity.this.currentProxyInfo.username = "";
                        ProxySettingsActivity.this.currentProxyInfo.password = "";
                    }
                    final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                    final SharedPreferences$Editor edit = globalMainSettings.edit();
                    boolean boolean1;
                    if (ProxySettingsActivity.this.addingNewProxy) {
                        SharedConfig.addProxy(ProxySettingsActivity.this.currentProxyInfo);
                        SharedConfig.currentProxy = ProxySettingsActivity.this.currentProxyInfo;
                        edit.putBoolean("proxy_enabled", true);
                        boolean1 = true;
                    }
                    else {
                        boolean1 = globalMainSettings.getBoolean("proxy_enabled", false);
                        SharedConfig.saveProxyList();
                    }
                    if (ProxySettingsActivity.this.addingNewProxy || SharedConfig.currentProxy == ProxySettingsActivity.this.currentProxyInfo) {
                        edit.putString("proxy_ip", ProxySettingsActivity.this.currentProxyInfo.address);
                        edit.putString("proxy_pass", ProxySettingsActivity.this.currentProxyInfo.password);
                        edit.putString("proxy_user", ProxySettingsActivity.this.currentProxyInfo.username);
                        edit.putInt("proxy_port", ProxySettingsActivity.this.currentProxyInfo.port);
                        edit.putString("proxy_secret", ProxySettingsActivity.this.currentProxyInfo.secret);
                        ConnectionsManager.setProxySettings(boolean1, ProxySettingsActivity.this.currentProxyInfo.address, ProxySettingsActivity.this.currentProxyInfo.port, ProxySettingsActivity.this.currentProxyInfo.username, ProxySettingsActivity.this.currentProxyInfo.password, ProxySettingsActivity.this.currentProxyInfo.secret);
                    }
                    edit.commit();
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged, new Object[0]);
                    ProxySettingsActivity.this.finishFragment();
                }
            }
        });
        (this.doneItem = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f))).setContentDescription((CharSequence)LocaleController.getString("Done", 2131559299));
        super.fragmentView = (View)new FrameLayout(context);
        final View fragmentView = super.fragmentView;
        final FrameLayout frameLayout = (FrameLayout)fragmentView;
        fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.scrollView = new ScrollView(context)).setFillViewport(true);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
        frameLayout.addView((View)this.scrollView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.linearLayout2 = new LinearLayout(context)).setOrientation(1);
        this.scrollView.addView((View)this.linearLayout2, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -2));
        for (int i = 0; i < 2; ++i) {
            (this.typeCell[i] = new TypeCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.typeCell[i].setTag((Object)i);
            if (i == 0) {
                this.typeCell[i].setValue(LocaleController.getString("UseProxySocks5", 2131560979), i == this.currentType, true);
            }
            else if (i == 1) {
                this.typeCell[i].setValue(LocaleController.getString("UseProxyTelegram", 2131560982), i == this.currentType, false);
            }
            this.linearLayout2.addView((View)this.typeCell[i], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
            this.typeCell[i].setOnClickListener((View$OnClickListener)new _$$Lambda$ProxySettingsActivity$m9BkTAI6IqJctow06HtssG_kLaQ(this));
        }
        this.sectionCell[0] = new ShadowSectionCell(context);
        this.linearLayout2.addView((View)this.sectionCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.inputFields = new EditTextBoldCursor[5];
        for (int j = 0; j < 5; ++j) {
            final FrameLayout frameLayout2 = new FrameLayout(context);
            this.linearLayout2.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 64));
            frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            (this.inputFields[j] = new EditTextBoldCursor(context)).setTag((Object)j);
            this.inputFields[j].setTextSize(1, 16.0f);
            this.inputFields[j].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[j].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[j].setBackgroundDrawable((Drawable)null);
            this.inputFields[j].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[j].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[j].setCursorWidth(1.5f);
            this.inputFields[j].setSingleLine(true);
            final EditTextBoldCursor editTextBoldCursor = this.inputFields[j];
            int n;
            if (LocaleController.isRTL) {
                n = 5;
            }
            else {
                n = 3;
            }
            editTextBoldCursor.setGravity(n | 0x10);
            this.inputFields[j].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
            this.inputFields[j].setTransformHintToHeader(true);
            this.inputFields[j].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
            if (j == 0) {
                this.inputFields[j].setInputType(524305);
                this.inputFields[j].addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(final Editable editable) {
                        ProxySettingsActivity.this.checkShareButton();
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
            }
            else if (j == 1) {
                this.inputFields[j].setInputType(2);
                this.inputFields[j].addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(final Editable editable) {
                        if (ProxySettingsActivity.this.ignoreOnTextChange) {
                            return;
                        }
                        final EditTextBoldCursor editTextBoldCursor = ProxySettingsActivity.this.inputFields[1];
                        final int selectionStart = editTextBoldCursor.getSelectionStart();
                        final String string = editTextBoldCursor.getText().toString();
                        final StringBuilder sb = new StringBuilder(string.length());
                        int endIndex;
                        for (int i = 0; i < string.length(); i = endIndex) {
                            endIndex = i + 1;
                            final String substring = string.substring(i, endIndex);
                            if ("0123456789".contains(substring)) {
                                sb.append(substring);
                            }
                        }
                        ProxySettingsActivity.this.ignoreOnTextChange = true;
                        final int intValue = Utilities.parseInt(sb.toString());
                        if (intValue >= 0 && intValue <= 65535 && string.equals(sb.toString())) {
                            if (selectionStart >= 0) {
                                int length;
                                if (selectionStart <= editTextBoldCursor.length()) {
                                    length = selectionStart;
                                }
                                else {
                                    length = editTextBoldCursor.length();
                                }
                                editTextBoldCursor.setSelection(length);
                            }
                        }
                        else if (intValue < 0) {
                            editTextBoldCursor.setText((CharSequence)"0");
                        }
                        else if (intValue > 65535) {
                            editTextBoldCursor.setText((CharSequence)"65535");
                        }
                        else {
                            editTextBoldCursor.setText((CharSequence)sb.toString());
                        }
                        ProxySettingsActivity.this.ignoreOnTextChange = false;
                        ProxySettingsActivity.this.checkShareButton();
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
            }
            else if (j == 3) {
                this.inputFields[j].setInputType(129);
                this.inputFields[j].setTypeface(Typeface.DEFAULT);
                this.inputFields[j].setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            }
            else {
                this.inputFields[j].setInputType(524289);
            }
            this.inputFields[j].setImeOptions(268435461);
            if (j != 0) {
                if (j != 1) {
                    if (j != 2) {
                        if (j != 3) {
                            if (j == 4) {
                                this.inputFields[j].setHintText(LocaleController.getString("UseProxySecret", 2131560977));
                                this.inputFields[j].setText((CharSequence)this.currentProxyInfo.secret);
                            }
                        }
                        else {
                            this.inputFields[j].setHintText(LocaleController.getString("UseProxyPassword", 2131560975));
                            this.inputFields[j].setText((CharSequence)this.currentProxyInfo.password);
                        }
                    }
                    else {
                        this.inputFields[j].setHintText(LocaleController.getString("UseProxyUsername", 2131560986));
                        this.inputFields[j].setText((CharSequence)this.currentProxyInfo.username);
                    }
                }
                else {
                    this.inputFields[j].setHintText(LocaleController.getString("UseProxyPort", 2131560976));
                    final EditTextBoldCursor editTextBoldCursor2 = this.inputFields[j];
                    final StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(this.currentProxyInfo.port);
                    editTextBoldCursor2.setText((CharSequence)sb.toString());
                }
            }
            else {
                this.inputFields[j].setHintText(LocaleController.getString("UseProxyAddress", 2131560971));
                this.inputFields[j].setText((CharSequence)this.currentProxyInfo.address);
            }
            final EditTextBoldCursor[] inputFields = this.inputFields;
            inputFields[j].setSelection(inputFields[j].length());
            this.inputFields[j].setPadding(0, 0, 0, 0);
            frameLayout2.addView((View)this.inputFields[j], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 17.0f, 0.0f, 17.0f, 0.0f));
            this.inputFields[j].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ProxySettingsActivity$6uZhE0rYQm5lNBN7Lia6YJLxnRo(this));
        }
        (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        this.bottomCell.setText(LocaleController.getString("UseProxyInfo", 2131560974));
        this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.shareCell = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.shareCell.setText(LocaleController.getString("ShareFile", 2131560748), false);
        this.shareCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
        this.linearLayout2.addView((View)this.shareCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.shareCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ProxySettingsActivity$4RcOr5eJR76wYO_TQE9eb5ryku8(this));
        (this.sectionCell[1] = new ShadowSectionCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        this.linearLayout2.addView((View)this.sectionCell[1], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.checkShareButton();
        this.updateUiForType();
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final ArrayList<ThemeDescription> list = new ArrayList<ThemeDescription>();
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        list.add(new ThemeDescription((View)this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"));
        list.add(new ThemeDescription((View)this.linearLayout2, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"));
        list.add(new ThemeDescription((View)this.shareCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.shareCell, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueText4"));
        list.add(new ThemeDescription((View)this.shareCell, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"));
        int n = 0;
        while (true) {
            final TypeCell[] typeCell = this.typeCell;
            if (n >= typeCell.length) {
                break;
            }
            list.add(new ThemeDescription((View)typeCell[n], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
            list.add(new ThemeDescription((View)this.typeCell[n], 0, new Class[] { TypeCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription((View)this.typeCell[n], ThemeDescription.FLAG_IMAGECOLOR, new Class[] { TypeCell.class }, new String[] { "checkImage" }, null, null, null, "featuredStickers_addedIcon"));
            ++n;
        }
        if (this.inputFields != null) {
            int n2 = 0;
            while (true) {
                final EditTextBoldCursor[] inputFields = this.inputFields;
                if (n2 >= inputFields.length) {
                    break;
                }
                list.add(new ThemeDescription((View)inputFields[n2].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
                list.add(new ThemeDescription((View)this.inputFields[n2], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)this.inputFields[n2], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                ++n2;
            }
        }
        else {
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        }
        list.add(new ThemeDescription((View)this.headerCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.headerCell, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"));
        for (int i = 0; i < 2; ++i) {
            list.add(new ThemeDescription(this.sectionCell[i], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"));
        }
        list.add(new ThemeDescription((View)this.bottomCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"));
        list.add(new ThemeDescription((View)this.bottomCell, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"));
        list.add(new ThemeDescription((View)this.bottomCell, ThemeDescription.FLAG_LINKCOLOR, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteLinkText"));
        return list.toArray(new ThemeDescription[list.size()]);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    @Override
    protected void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b && !b2 && this.addingNewProxy) {
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard((View)this.inputFields[0]);
        }
    }
    
    public class TypeCell extends FrameLayout
    {
        private ImageView checkImage;
        private boolean needDivider;
        private TextView textView;
        
        public TypeCell(final Context context) {
            super(context);
            this.setWillNotDraw(false);
            (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils$TruncateAt.END);
            final TextView textView = this.textView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int n2;
            if (isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            textView.setGravity(n2 | 0x10);
            final TextView textView2 = this.textView;
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            float n4;
            if (LocaleController.isRTL) {
                n4 = 71.0f;
            }
            else {
                n4 = 21.0f;
            }
            float n5;
            if (LocaleController.isRTL) {
                n5 = 21.0f;
            }
            else {
                n5 = 23.0f;
            }
            this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n4, 0.0f, n5, 0.0f));
            (this.checkImage = new ImageView(context)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), PorterDuff$Mode.MULTIPLY));
            this.checkImage.setImageResource(2131165858);
            final ImageView checkImage = this.checkImage;
            int n6 = n;
            if (LocaleController.isRTL) {
                n6 = 3;
            }
            this.addView((View)checkImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(19, 14.0f, n6 | 0x10, 21.0f, 0.0f, 21.0f, 0.0f));
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.needDivider) {
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
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0), 1073741824));
        }
        
        public void setTypeChecked(final boolean b) {
            final ImageView checkImage = this.checkImage;
            int visibility;
            if (b) {
                visibility = 0;
            }
            else {
                visibility = 4;
            }
            checkImage.setVisibility(visibility);
        }
        
        public void setValue(final String text, final boolean b, final boolean needDivider) {
            this.textView.setText((CharSequence)text);
            final ImageView checkImage = this.checkImage;
            int visibility;
            if (b) {
                visibility = 0;
            }
            else {
                visibility = 4;
            }
            checkImage.setVisibility(visibility);
            this.needDivider = needDivider;
        }
    }
}

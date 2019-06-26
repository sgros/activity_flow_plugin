// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.content.res.Configuration;
import android.content.DialogInterface;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.UserConfig;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.ActionBar.AlertDialog;
import android.view.KeyEvent;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ActionBarMenu;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.text.TextUtils$TruncateAt;
import android.view.View$OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView$OnEditorActionListener;
import android.graphics.Typeface;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.support.fingerprint.FingerprintManagerCompat;
import android.text.method.TransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.KeyListener;
import android.text.method.DigitsKeyListener;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.FileLog;
import android.content.Context;
import android.widget.Toast;
import android.view.View;
import android.os.Vibrator;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.ApplicationLoader;
import android.view.ViewGroup$LayoutParams;
import android.os.Build$VERSION;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.RecyclerListView;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import android.widget.TextView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class PasscodeActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int done_button = 1;
    private static final int password_item = 3;
    private static final int pin_item = 2;
    private int autoLockDetailRow;
    private int autoLockRow;
    private int badPasscodeTries;
    private int captureDetailRow;
    private int captureRow;
    private int changePasscodeRow;
    private int currentPasswordType;
    private TextView dropDown;
    private ActionBarMenuItem dropDownContainer;
    private Drawable dropDownDrawable;
    private int fingerprintRow;
    private String firstPassword;
    private long lastPasscodeTry;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int passcodeDetailRow;
    private int passcodeRow;
    private int passcodeSetStep;
    private EditTextBoldCursor passwordEditText;
    private int rowCount;
    private TextView titleTextView;
    private int type;
    
    public PasscodeActivity(final int type) {
        this.currentPasswordType = 0;
        this.passcodeSetStep = 0;
        this.type = type;
    }
    
    private void fixLayoutInternal() {
        if (this.dropDownContainer != null) {
            if (!AndroidUtilities.isTablet()) {
                final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.dropDownContainer.getLayoutParams();
                int statusBarHeight;
                if (Build$VERSION.SDK_INT >= 21) {
                    statusBarHeight = AndroidUtilities.statusBarHeight;
                }
                else {
                    statusBarHeight = 0;
                }
                layoutParams.topMargin = statusBarHeight;
                this.dropDownContainer.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            }
            if (!AndroidUtilities.isTablet() && ApplicationLoader.applicationContext.getResources().getConfiguration().orientation == 2) {
                this.dropDown.setTextSize(18.0f);
            }
            else {
                this.dropDown.setTextSize(20.0f);
            }
        }
    }
    
    private void onPasscodeError() {
        if (this.getParentActivity() == null) {
            return;
        }
        final Vibrator vibrator = (Vibrator)this.getParentActivity().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200L);
        }
        AndroidUtilities.shakeView((View)this.titleTextView, 2.0f, 0);
    }
    
    private void processDone() {
        if (this.passwordEditText.getText().length() == 0) {
            this.onPasscodeError();
            return;
        }
        final int type = this.type;
        if (type == 1) {
            if (!this.firstPassword.equals(this.passwordEditText.getText().toString())) {
                try {
                    Toast.makeText((Context)this.getParentActivity(), (CharSequence)LocaleController.getString("PasscodeDoNotMatch", 2131560161), 0).show();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                AndroidUtilities.shakeView((View)this.titleTextView, 2.0f, 0);
                this.passwordEditText.setText((CharSequence)"");
                return;
            }
            try {
                SharedConfig.passcodeSalt = new byte[16];
                Utilities.random.nextBytes(SharedConfig.passcodeSalt);
                final byte[] bytes = this.firstPassword.getBytes("UTF-8");
                final byte[] array = new byte[bytes.length + 32];
                System.arraycopy(SharedConfig.passcodeSalt, 0, array, 0, 16);
                System.arraycopy(bytes, 0, array, 16, bytes.length);
                System.arraycopy(SharedConfig.passcodeSalt, 0, array, bytes.length + 16, 16);
                SharedConfig.passcodeHash = Utilities.bytesToHex(Utilities.computeSHA256(array, 0, array.length));
            }
            catch (Exception ex2) {
                FileLog.e(ex2);
            }
            SharedConfig.allowScreenCapture = true;
            SharedConfig.passcodeType = this.currentPasswordType;
            SharedConfig.saveConfig();
            this.finishFragment();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode, new Object[0]);
            this.passwordEditText.clearFocus();
            AndroidUtilities.hideKeyboard((View)this.passwordEditText);
        }
        else if (type == 2) {
            final long passcodeRetryInMs = SharedConfig.passcodeRetryInMs;
            if (passcodeRetryInMs > 0L) {
                final double v = (double)passcodeRetryInMs;
                Double.isNaN(v);
                Toast.makeText((Context)this.getParentActivity(), (CharSequence)LocaleController.formatString("TooManyTries", 2131560909, LocaleController.formatPluralString("Seconds", Math.max(1, (int)Math.ceil(v / 1000.0)))), 0).show();
                this.passwordEditText.setText((CharSequence)"");
                this.onPasscodeError();
                return;
            }
            if (!SharedConfig.checkPasscode(this.passwordEditText.getText().toString())) {
                SharedConfig.increaseBadPasscodeTries();
                this.passwordEditText.setText((CharSequence)"");
                this.onPasscodeError();
                return;
            }
            SharedConfig.badPasscodeTries = 0;
            SharedConfig.saveConfig();
            this.passwordEditText.clearFocus();
            AndroidUtilities.hideKeyboard((View)this.passwordEditText);
            this.presentFragment(new PasscodeActivity(0), true);
        }
    }
    
    private void processNext() {
        if (this.passwordEditText.getText().length() != 0 && (this.currentPasswordType != 0 || this.passwordEditText.getText().length() == 4)) {
            if (this.currentPasswordType == 0) {
                super.actionBar.setTitle(LocaleController.getString("PasscodePIN", 2131560162));
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("PasscodePassword", 2131560163));
            }
            this.dropDownContainer.setVisibility(8);
            this.titleTextView.setText((CharSequence)LocaleController.getString("ReEnterYourPasscode", 2131560536));
            this.firstPassword = this.passwordEditText.getText().toString();
            this.passwordEditText.setText((CharSequence)"");
            this.passcodeSetStep = 1;
            return;
        }
        this.onPasscodeError();
    }
    
    private void updateDropDownTextView() {
        final TextView dropDown = this.dropDown;
        if (dropDown != null) {
            final int currentPasswordType = this.currentPasswordType;
            if (currentPasswordType == 0) {
                dropDown.setText((CharSequence)LocaleController.getString("PasscodePIN", 2131560162));
            }
            else if (currentPasswordType == 1) {
                dropDown.setText((CharSequence)LocaleController.getString("PasscodePassword", 2131560163));
            }
        }
        if ((this.type == 1 && this.currentPasswordType == 0) || (this.type == 2 && SharedConfig.passcodeType == 0)) {
            this.passwordEditText.setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(4) });
            this.passwordEditText.setInputType(3);
            this.passwordEditText.setKeyListener((KeyListener)DigitsKeyListener.getInstance("1234567890"));
        }
        else if ((this.type == 1 && this.currentPasswordType == 1) || (this.type == 2 && SharedConfig.passcodeType == 1)) {
            this.passwordEditText.setFilters(new InputFilter[0]);
            this.passwordEditText.setKeyListener((KeyListener)null);
            this.passwordEditText.setInputType(129);
        }
        this.passwordEditText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
    }
    
    private void updateRows() {
        this.rowCount = 0;
        this.passcodeRow = this.rowCount++;
        this.changePasscodeRow = this.rowCount++;
        this.passcodeDetailRow = this.rowCount++;
        if (SharedConfig.passcodeHash.length() > 0) {
            try {
                if (Build$VERSION.SDK_INT >= 23 && FingerprintManagerCompat.from(ApplicationLoader.applicationContext).isHardwareDetected()) {
                    this.fingerprintRow = this.rowCount++;
                }
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
            this.autoLockRow = this.rowCount++;
            this.autoLockDetailRow = this.rowCount++;
            this.captureRow = this.rowCount++;
            this.captureDetailRow = this.rowCount++;
        }
        else {
            this.captureRow = -1;
            this.captureDetailRow = -1;
            this.fingerprintRow = -1;
            this.autoLockRow = -1;
            this.autoLockDetailRow = -1;
        }
    }
    
    @Override
    public View createView(final Context context) {
        if (this.type != 3) {
            super.actionBar.setBackButtonImage(2131165409);
        }
        super.actionBar.setAllowOverlayTitle(false);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    PasscodeActivity.this.finishFragment();
                }
                else if (n == 1) {
                    if (PasscodeActivity.this.passcodeSetStep == 0) {
                        PasscodeActivity.this.processNext();
                    }
                    else if (PasscodeActivity.this.passcodeSetStep == 1) {
                        PasscodeActivity.this.processDone();
                    }
                }
                else if (n == 2) {
                    PasscodeActivity.this.currentPasswordType = 0;
                    PasscodeActivity.this.updateDropDownTextView();
                }
                else if (n == 3) {
                    PasscodeActivity.this.currentPasswordType = 1;
                    PasscodeActivity.this.updateDropDownTextView();
                }
            }
        });
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        if (this.type != 0) {
            final ActionBarMenu menu = super.actionBar.createMenu();
            menu.addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
            (this.titleTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            if (this.type == 1) {
                if (SharedConfig.passcodeHash.length() != 0) {
                    this.titleTextView.setText((CharSequence)LocaleController.getString("EnterNewPasscode", 2131559372));
                }
                else {
                    this.titleTextView.setText((CharSequence)LocaleController.getString("EnterNewFirstPasscode", 2131559371));
                }
            }
            else {
                this.titleTextView.setText((CharSequence)LocaleController.getString("EnterCurrentPasscode", 2131559368));
            }
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setGravity(1);
            frameLayout.addView((View)this.titleTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 1, 0.0f, 38.0f, 0.0f, 0.0f));
            (this.passwordEditText = new EditTextBoldCursor(context)).setTextSize(1, 20.0f);
            this.passwordEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.passwordEditText.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
            this.passwordEditText.setMaxLines(1);
            this.passwordEditText.setLines(1);
            this.passwordEditText.setGravity(1);
            this.passwordEditText.setSingleLine(true);
            if (this.type == 1) {
                this.passcodeSetStep = 0;
                this.passwordEditText.setImeOptions(5);
            }
            else {
                this.passcodeSetStep = 1;
                this.passwordEditText.setImeOptions(6);
            }
            this.passwordEditText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            this.passwordEditText.setTypeface(Typeface.DEFAULT);
            this.passwordEditText.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.passwordEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            this.passwordEditText.setCursorWidth(1.5f);
            frameLayout.addView((View)this.passwordEditText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 51, 40.0f, 90.0f, 40.0f, 0.0f));
            this.passwordEditText.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PasscodeActivity$Dw5wi6axlaDg9f9aOPWpPJRaBU0(this));
            this.passwordEditText.addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                    if (PasscodeActivity.this.passwordEditText.length() == 4) {
                        if (PasscodeActivity.this.type == 2 && SharedConfig.passcodeType == 0) {
                            PasscodeActivity.this.processDone();
                        }
                        else if (PasscodeActivity.this.type == 1 && PasscodeActivity.this.currentPasswordType == 0) {
                            if (PasscodeActivity.this.passcodeSetStep == 0) {
                                PasscodeActivity.this.processNext();
                            }
                            else if (PasscodeActivity.this.passcodeSetStep == 1) {
                                PasscodeActivity.this.processDone();
                            }
                        }
                    }
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
            });
            this.passwordEditText.setCustomSelectionActionModeCallback((ActionMode$Callback)new ActionMode$Callback() {
                public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                    return false;
                }
                
                public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                    return false;
                }
                
                public void onDestroyActionMode(final ActionMode actionMode) {
                }
                
                public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                    return false;
                }
            });
            if (this.type == 1) {
                frameLayout.setTag((Object)"windowBackgroundWhite");
                (this.dropDownContainer = new ActionBarMenuItem(context, menu, 0, 0)).setSubMenuOpenSide(1);
                this.dropDownContainer.addSubItem(2, LocaleController.getString("PasscodePIN", 2131560162));
                this.dropDownContainer.addSubItem(3, LocaleController.getString("PasscodePassword", 2131560163));
                final ActionBar actionBar = super.actionBar;
                final ActionBarMenuItem dropDownContainer = this.dropDownContainer;
                float n;
                if (AndroidUtilities.isTablet()) {
                    n = 64.0f;
                }
                else {
                    n = 56.0f;
                }
                actionBar.addView((View)dropDownContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, 51, n, 0.0f, 40.0f, 0.0f));
                this.dropDownContainer.setOnClickListener((View$OnClickListener)new _$$Lambda$PasscodeActivity$nthmgeFTBNbMbbgybaEUi4bvQ7I(this));
                (this.dropDown = new TextView(context)).setGravity(3);
                this.dropDown.setSingleLine(true);
                this.dropDown.setLines(1);
                this.dropDown.setMaxLines(1);
                this.dropDown.setEllipsize(TextUtils$TruncateAt.END);
                this.dropDown.setTextColor(Theme.getColor("actionBarDefaultTitle"));
                this.dropDown.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                (this.dropDownDrawable = context.getResources().getDrawable(2131165427).mutate()).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("actionBarDefaultTitle"), PorterDuff$Mode.MULTIPLY));
                this.dropDown.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.dropDownDrawable, (Drawable)null);
                this.dropDown.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                this.dropDown.setPadding(0, 0, AndroidUtilities.dp(10.0f), 0);
                this.dropDownContainer.addView((View)this.dropDown, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 16, 16.0f, 0.0f, 0.0f, 1.0f));
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("Passcode", 2131560160));
            }
            this.updateDropDownTextView();
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("Passcode", 2131560160));
            frameLayout.setTag((Object)"windowBackgroundGray");
            frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            (this.listView = new RecyclerListView(context)).setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false) {
                @Override
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            });
            this.listView.setVerticalScrollBarEnabled(false);
            this.listView.setItemAnimator(null);
            this.listView.setLayoutAnimation((LayoutAnimationController)null);
            frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            this.listView.setAdapter(this.listAdapter = new ListAdapter(context));
            this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$PasscodeActivity$mrP17AePE_jxJGc4Lp8zfeRkzb0(this));
        }
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.didSetPasscode && this.type == 0) {
            this.updateRows();
            final ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextCheckCell.class, TextSettingsCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "actionBarDefaultSubmenuItemIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"), new ThemeDescription((View)this.passwordEditText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.dropDown, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)this.dropDown, 0, null, null, new Drawable[] { this.dropDownDrawable }, null, "actionBarDefaultTitle"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText7"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4") };
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            listView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                public boolean onPreDraw() {
                    PasscodeActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                    PasscodeActivity.this.fixLayoutInternal();
                    return true;
                }
            });
        }
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.updateRows();
        if (this.type == 0) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
        }
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.type == 0) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
        if (this.type != 0) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$PasscodeActivity$uOu47HjuX9AWXmApKYN2F1Z_SDQ(this), 200L);
        }
        this.fixLayoutInternal();
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b && this.type != 0) {
            AndroidUtilities.showKeyboard((View)this.passwordEditText);
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return PasscodeActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == PasscodeActivity.this.passcodeRow || n == PasscodeActivity.this.fingerprintRow || n == PasscodeActivity.this.captureRow) {
                return 0;
            }
            if (n == PasscodeActivity.this.changePasscodeRow || n == PasscodeActivity.this.autoLockRow) {
                return 1;
            }
            if (n != PasscodeActivity.this.passcodeDetailRow && n != PasscodeActivity.this.autoLockDetailRow && n != PasscodeActivity.this.captureDetailRow) {
                return 0;
            }
            return 2;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == PasscodeActivity.this.passcodeRow || adapterPosition == PasscodeActivity.this.fingerprintRow || adapterPosition == PasscodeActivity.this.autoLockRow || adapterPosition == PasscodeActivity.this.captureRow || (SharedConfig.passcodeHash.length() != 0 && adapterPosition == PasscodeActivity.this.changePasscodeRow);
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int autoLockIn) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = false;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType == 2) {
                        final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                        if (autoLockIn == PasscodeActivity.this.passcodeDetailRow) {
                            textInfoPrivacyCell.setText(LocaleController.getString("ChangePasscodeInfo", 2131558908));
                            if (PasscodeActivity.this.autoLockDetailRow != -1) {
                                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                            }
                            else {
                                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                            }
                        }
                        else if (autoLockIn == PasscodeActivity.this.autoLockDetailRow) {
                            textInfoPrivacyCell.setText(LocaleController.getString("AutoLockInfo", 2131558781));
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                        }
                        else if (autoLockIn == PasscodeActivity.this.captureDetailRow) {
                            textInfoPrivacyCell.setText(LocaleController.getString("ScreenCaptureInfo", 2131560638));
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        }
                    }
                }
                else {
                    final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                    if (autoLockIn == PasscodeActivity.this.changePasscodeRow) {
                        textSettingsCell.setText(LocaleController.getString("ChangePasscode", 2131558907), false);
                        if (SharedConfig.passcodeHash.length() == 0) {
                            textSettingsCell.setTag((Object)"windowBackgroundWhiteGrayText7");
                            textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText7"));
                        }
                        else {
                            textSettingsCell.setTag((Object)"windowBackgroundWhiteBlackText");
                            textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                        }
                    }
                    else if (autoLockIn == PasscodeActivity.this.autoLockRow) {
                        autoLockIn = SharedConfig.autoLockIn;
                        String s;
                        if (autoLockIn == 0) {
                            s = LocaleController.formatString("AutoLockDisabled", 2131558779, new Object[0]);
                        }
                        else if (autoLockIn < 3600) {
                            s = LocaleController.formatString("AutoLockInTime", 2131558780, LocaleController.formatPluralString("Minutes", autoLockIn / 60));
                        }
                        else if (autoLockIn < 86400) {
                            s = LocaleController.formatString("AutoLockInTime", 2131558780, LocaleController.formatPluralString("Hours", (int)Math.ceil(autoLockIn / 60.0f / 60.0f)));
                        }
                        else {
                            s = LocaleController.formatString("AutoLockInTime", 2131558780, LocaleController.formatPluralString("Days", (int)Math.ceil(autoLockIn / 60.0f / 60.0f / 24.0f)));
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("AutoLock", 2131558778), s, true);
                        textSettingsCell.setTag((Object)"windowBackgroundWhiteBlackText");
                        textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    }
                }
            }
            else {
                final TextCheckCell textCheckCell = (TextCheckCell)viewHolder.itemView;
                if (autoLockIn == PasscodeActivity.this.passcodeRow) {
                    final String string = LocaleController.getString("Passcode", 2131560160);
                    if (SharedConfig.passcodeHash.length() > 0) {
                        b = true;
                    }
                    textCheckCell.setTextAndCheck(string, b, true);
                }
                else if (autoLockIn == PasscodeActivity.this.fingerprintRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("UnlockFingerprint", 2131560938), SharedConfig.useFingerprint, true);
                }
                else if (autoLockIn == PasscodeActivity.this.captureRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("ScreenCapture", 2131560636), SharedConfig.allowScreenCapture, false);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                if (n != 1) {
                    frameLayout = new TextInfoPrivacyCell(this.mContext);
                }
                else {
                    frameLayout = new TextSettingsCell(this.mContext);
                    ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                }
            }
            else {
                frameLayout = new TextCheckCell(this.mContext);
                ((TextCheckCell)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
}

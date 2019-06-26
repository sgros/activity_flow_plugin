// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.graphics.Canvas;
import android.content.SharedPreferences;
import android.telephony.PhoneNumberUtils;
import org.telegram.messenger.BuildVars;
import android.app.PendingIntent;
import org.telegram.messenger.SmsReceiver;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import org.telegram.ui.Components.RadialProgressView;
import android.widget.TextView$OnEditorActionListener;
import android.view.View$OnKeyListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.content.DialogInterface$OnClickListener;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import org.telegram.ui.Components.AlertsCreator;
import android.os.Build;
import android.content.Intent;
import java.util.Locale;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import java.util.TimerTask;
import android.view.View$OnClickListener;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.widget.TextView;
import java.util.Timer;
import android.widget.LinearLayout;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.widget.ImageView;
import org.telegram.messenger.NotificationCenter;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.animation.AnimatorSet;
import android.os.Build$VERSION;
import org.telegram.messenger.FileLog;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout;
import android.graphics.Rect;
import android.view.View$MeasureSpec;
import android.widget.ScrollView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import android.os.Bundle;
import org.telegram.ui.Components.SlideView;
import org.telegram.ui.ActionBar.AlertDialog;
import java.util.ArrayList;
import android.app.Dialog;
import android.view.View;
import org.telegram.ui.ActionBar.BaseFragment;

public class CancelAccountDeletionActivity extends BaseFragment
{
    private static final int done_button = 1;
    private boolean checkPermissions;
    private int currentViewNum;
    private View doneButton;
    private Dialog errorDialog;
    private String hash;
    private Dialog permissionsDialog;
    private ArrayList<String> permissionsItems;
    private String phone;
    private AlertDialog progressDialog;
    private int scrollHeight;
    private SlideView[] views;
    
    public CancelAccountDeletionActivity(final Bundle bundle) {
        super(bundle);
        this.currentViewNum = 0;
        this.views = new SlideView[5];
        this.permissionsItems = new ArrayList<String>();
        this.checkPermissions = false;
        this.hash = bundle.getString("hash");
        this.phone = bundle.getString("phone");
    }
    
    private void fillNextCodeParams(final Bundle bundle, final TLRPC.TL_auth_sentCode tl_auth_sentCode) {
        bundle.putString("phoneHash", tl_auth_sentCode.phone_code_hash);
        final TLRPC.auth_CodeType next_type = tl_auth_sentCode.next_type;
        if (next_type instanceof TLRPC.TL_auth_codeTypeCall) {
            bundle.putInt("nextType", 4);
        }
        else if (next_type instanceof TLRPC.TL_auth_codeTypeFlashCall) {
            bundle.putInt("nextType", 3);
        }
        else if (next_type instanceof TLRPC.TL_auth_codeTypeSms) {
            bundle.putInt("nextType", 2);
        }
        if (tl_auth_sentCode.type instanceof TLRPC.TL_auth_sentCodeTypeApp) {
            bundle.putInt("type", 1);
            bundle.putInt("length", tl_auth_sentCode.type.length);
            this.setPage(1, true, bundle, false);
        }
        else {
            if (tl_auth_sentCode.timeout == 0) {
                tl_auth_sentCode.timeout = 60;
            }
            bundle.putInt("timeout", tl_auth_sentCode.timeout * 1000);
            final TLRPC.auth_SentCodeType type = tl_auth_sentCode.type;
            if (type instanceof TLRPC.TL_auth_sentCodeTypeCall) {
                bundle.putInt("type", 4);
                bundle.putInt("length", tl_auth_sentCode.type.length);
                this.setPage(4, true, bundle, false);
            }
            else if (type instanceof TLRPC.TL_auth_sentCodeTypeFlashCall) {
                bundle.putInt("type", 3);
                bundle.putString("pattern", tl_auth_sentCode.type.pattern);
                this.setPage(3, true, bundle, false);
            }
            else if (type instanceof TLRPC.TL_auth_sentCodeTypeSms) {
                bundle.putInt("type", 2);
                bundle.putInt("length", tl_auth_sentCode.type.length);
                this.setPage(2, true, bundle, false);
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setTitle(LocaleController.getString("AppName", 2131558635));
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == 1) {
                    CancelAccountDeletionActivity.this.views[CancelAccountDeletionActivity.this.currentViewNum].onNextPressed();
                }
                else if (n == -1) {
                    CancelAccountDeletionActivity.this.finishFragment();
                }
            }
        });
        (this.doneButton = (View)super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f))).setVisibility(8);
        final ScrollView fragmentView = new ScrollView(context) {
            protected void onMeasure(final int n, final int n2) {
                CancelAccountDeletionActivity.this.scrollHeight = View$MeasureSpec.getSize(n2) - AndroidUtilities.dp(30.0f);
                super.onMeasure(n, n2);
            }
            
            public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
                if (CancelAccountDeletionActivity.this.currentViewNum == 1 || CancelAccountDeletionActivity.this.currentViewNum == 2 || CancelAccountDeletionActivity.this.currentViewNum == 4) {
                    rect.bottom += AndroidUtilities.dp(40.0f);
                }
                return super.requestChildRectangleOnScreen(view, rect, b);
            }
        };
        fragmentView.setFillViewport(true);
        super.fragmentView = (View)fragmentView;
        final FrameLayout frameLayout = new FrameLayout(context);
        fragmentView.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createScroll(-1, -2, 51));
        this.views[0] = new PhoneView(context);
        this.views[1] = new LoginActivitySmsView(context, 1);
        this.views[2] = new LoginActivitySmsView(context, 2);
        this.views[3] = new LoginActivitySmsView(context, 3);
        this.views[4] = new LoginActivitySmsView(context, 4);
        int n = 0;
        SlideView[] views;
        while (true) {
            views = this.views;
            if (n >= views.length) {
                break;
            }
            final SlideView slideView = views[n];
            int visibility;
            if (n == 0) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            slideView.setVisibility(visibility);
            final SlideView slideView2 = this.views[n];
            float n2;
            if (n == 0) {
                n2 = -2.0f;
            }
            else {
                n2 = -1.0f;
            }
            float n3;
            if (AndroidUtilities.isTablet()) {
                n3 = 26.0f;
            }
            else {
                n3 = 18.0f;
            }
            float n4;
            if (AndroidUtilities.isTablet()) {
                n4 = 26.0f;
            }
            else {
                n4 = 18.0f;
            }
            frameLayout.addView((View)slideView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, n2, 51, n3, 30.0f, n4, 0.0f));
            ++n;
        }
        super.actionBar.setTitle(views[0].getHeaderName());
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final SlideView[] views = this.views;
        final PhoneView phoneView = (PhoneView)views[0];
        final LoginActivitySmsView loginActivitySmsView = (LoginActivitySmsView)views[1];
        final LoginActivitySmsView loginActivitySmsView2 = (LoginActivitySmsView)views[2];
        final LoginActivitySmsView loginActivitySmsView3 = (LoginActivitySmsView)views[3];
        final LoginActivitySmsView loginActivitySmsView4 = (LoginActivitySmsView)views[4];
        final ArrayList<ThemeDescription> list = new ArrayList<ThemeDescription>();
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        list.add(new ThemeDescription(phoneView.progressBar, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
        list.add(new ThemeDescription((View)loginActivitySmsView.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        list.add(new ThemeDescription((View)loginActivitySmsView.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        if (loginActivitySmsView.codeField != null) {
            for (int i = 0; i < loginActivitySmsView.codeField.length; ++i) {
                list.add(new ThemeDescription((View)loginActivitySmsView.codeField[i], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivitySmsView.codeField[i], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
            }
        }
        list.add(new ThemeDescription((View)loginActivitySmsView.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        list.add(new ThemeDescription((View)loginActivitySmsView.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
        list.add(new ThemeDescription(loginActivitySmsView.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressInner"));
        list.add(new ThemeDescription(loginActivitySmsView.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressOuter"));
        list.add(new ThemeDescription((View)loginActivitySmsView.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)loginActivitySmsView.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionBackground"));
        list.add(new ThemeDescription((View)loginActivitySmsView2.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        list.add(new ThemeDescription((View)loginActivitySmsView2.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        if (loginActivitySmsView2.codeField != null) {
            for (int j = 0; j < loginActivitySmsView2.codeField.length; ++j) {
                list.add(new ThemeDescription((View)loginActivitySmsView2.codeField[j], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivitySmsView2.codeField[j], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
            }
        }
        list.add(new ThemeDescription((View)loginActivitySmsView2.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        list.add(new ThemeDescription((View)loginActivitySmsView2.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
        list.add(new ThemeDescription(loginActivitySmsView2.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressInner"));
        list.add(new ThemeDescription(loginActivitySmsView2.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressOuter"));
        list.add(new ThemeDescription((View)loginActivitySmsView2.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)loginActivitySmsView2.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionBackground"));
        list.add(new ThemeDescription((View)loginActivitySmsView3.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        list.add(new ThemeDescription((View)loginActivitySmsView3.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        if (loginActivitySmsView3.codeField != null) {
            for (int k = 0; k < loginActivitySmsView3.codeField.length; ++k) {
                list.add(new ThemeDescription((View)loginActivitySmsView3.codeField[k], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivitySmsView3.codeField[k], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
            }
        }
        list.add(new ThemeDescription((View)loginActivitySmsView3.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        list.add(new ThemeDescription((View)loginActivitySmsView3.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
        list.add(new ThemeDescription(loginActivitySmsView3.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressInner"));
        list.add(new ThemeDescription(loginActivitySmsView3.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressOuter"));
        list.add(new ThemeDescription((View)loginActivitySmsView3.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)loginActivitySmsView3.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionBackground"));
        list.add(new ThemeDescription((View)loginActivitySmsView4.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        list.add(new ThemeDescription((View)loginActivitySmsView4.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        if (loginActivitySmsView4.codeField != null) {
            for (int l = 0; l < loginActivitySmsView4.codeField.length; ++l) {
                list.add(new ThemeDescription((View)loginActivitySmsView4.codeField[l], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivitySmsView4.codeField[l], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
            }
        }
        list.add(new ThemeDescription((View)loginActivitySmsView4.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        list.add(new ThemeDescription((View)loginActivitySmsView4.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
        list.add(new ThemeDescription(loginActivitySmsView4.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressInner"));
        list.add(new ThemeDescription(loginActivitySmsView4.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressOuter"));
        list.add(new ThemeDescription((View)loginActivitySmsView4.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)loginActivitySmsView4.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionBackground"));
        return list.toArray(new ThemeDescription[0]);
    }
    
    public void needHideProgress() {
        final AlertDialog progressDialog = this.progressDialog;
        if (progressDialog == null) {
            return;
        }
        try {
            progressDialog.dismiss();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        this.progressDialog = null;
    }
    
    public void needShowProgress() {
        if (this.getParentActivity() != null && !this.getParentActivity().isFinishing()) {
            if (this.progressDialog == null) {
                (this.progressDialog = new AlertDialog((Context)this.getParentActivity(), 3)).setCanCacnel(false);
                this.progressDialog.show();
            }
        }
    }
    
    @Override
    public boolean onBackPressed() {
        int n = 0;
        while (true) {
            final SlideView[] views = this.views;
            if (n >= views.length) {
                break;
            }
            if (views[n] != null) {
                views[n].onDestroyActivity();
            }
            ++n;
        }
        return true;
    }
    
    @Override
    protected void onDialogDismiss(final Dialog dialog) {
        if (Build$VERSION.SDK_INT >= 23 && dialog == this.permissionsDialog && !this.permissionsItems.isEmpty()) {
            this.getParentActivity().requestPermissions((String[])this.permissionsItems.toArray(new String[0]), 6);
        }
        if (dialog == this.errorDialog) {
            this.finishFragment();
        }
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        int n = 0;
        while (true) {
            final SlideView[] views = this.views;
            if (n >= views.length) {
                break;
            }
            if (views[n] != null) {
                views[n].onDestroyActivity();
            }
            ++n;
        }
        final AlertDialog progressDialog = this.progressDialog;
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            this.progressDialog = null;
        }
        AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    @Override
    public void onRequestPermissionsResultFragment(int currentViewNum, final String[] array, final int[] array2) {
        if (currentViewNum == 6) {
            this.checkPermissions = false;
            currentViewNum = this.currentViewNum;
            if (currentViewNum == 0) {
                this.views[currentViewNum].onNextPressed();
            }
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b) {
            this.views[this.currentViewNum].onShow();
        }
    }
    
    public void setPage(int currentViewNum, final boolean b, final Bundle bundle, final boolean b2) {
        if (currentViewNum != 3 && currentViewNum != 0) {
            this.doneButton.setVisibility(0);
        }
        else {
            this.doneButton.setVisibility(8);
        }
        final SlideView[] views = this.views;
        final SlideView slideView = views[this.currentViewNum];
        final SlideView slideView2 = views[currentViewNum];
        this.currentViewNum = currentViewNum;
        slideView2.setParams(bundle, false);
        super.actionBar.setTitle(slideView2.getHeaderName());
        slideView2.onShow();
        if (b2) {
            currentViewNum = -AndroidUtilities.displaySize.x;
        }
        else {
            currentViewNum = AndroidUtilities.displaySize.x;
        }
        slideView2.setX((float)currentViewNum);
        final AnimatorSet set = new AnimatorSet();
        set.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
        set.setDuration(300L);
        if (b2) {
            currentViewNum = AndroidUtilities.displaySize.x;
        }
        else {
            currentViewNum = -AndroidUtilities.displaySize.x;
        }
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)slideView, "translationX", new float[] { (float)currentViewNum }), (Animator)ObjectAnimator.ofFloat((Object)slideView2, "translationX", new float[] { 0.0f }) });
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                slideView.setVisibility(8);
                slideView.setX(0.0f);
            }
            
            public void onAnimationStart(final Animator animator) {
                slideView2.setVisibility(0);
            }
        });
        set.start();
    }
    
    public class LoginActivitySmsView extends SlideView implements NotificationCenterDelegate
    {
        private ImageView blackImageView;
        private ImageView blueImageView;
        private EditTextBoldCursor[] codeField;
        private LinearLayout codeFieldContainer;
        private int codeTime;
        private Timer codeTimer;
        private TextView confirmTextView;
        private Bundle currentParams;
        private int currentType;
        private boolean ignoreOnTextChange;
        private double lastCodeTime;
        private double lastCurrentTime;
        private String lastError;
        private int length;
        private boolean nextPressed;
        private int nextType;
        private int openTime;
        private String pattern;
        private String phone;
        private String phoneHash;
        private TextView problemText;
        private ProgressView progressView;
        private int time;
        private TextView timeText;
        private Timer timeTimer;
        private int timeout;
        private final Object timerSync;
        private TextView titleTextView;
        private boolean waitingForEvent;
        
        public LoginActivitySmsView(final Context context, int gravity) {
            super(context);
            this.timerSync = new Object();
            this.time = 60000;
            this.codeTime = 15000;
            this.lastError = "";
            this.pattern = "*";
            this.currentType = gravity;
            this.setOrientation(1);
            (this.confirmTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.confirmTextView.setTextSize(1, 14.0f);
            this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            (this.titleTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            final TextView titleTextView = this.titleTextView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 3;
            if (isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            titleTextView.setGravity(gravity);
            this.titleTextView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(49);
            if (this.currentType == 3) {
                final TextView confirmTextView = this.confirmTextView;
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                else {
                    gravity = 3;
                }
                confirmTextView.setGravity(gravity | 0x30);
                final FrameLayout frameLayout = new FrameLayout(context);
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                else {
                    gravity = 3;
                }
                this.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, gravity));
                final ImageView imageView = new ImageView(context);
                imageView.setImageResource(2131165739);
                final boolean isRTL2 = LocaleController.isRTL;
                if (isRTL2) {
                    frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 76.0f, 19, 2.0f, 2.0f, 0.0f, 0.0f));
                    final TextView confirmTextView2 = this.confirmTextView;
                    if (LocaleController.isRTL) {
                        gravity = 5;
                    }
                    else {
                        gravity = 3;
                    }
                    frameLayout.addView((View)confirmTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, gravity, 82.0f, 0.0f, 0.0f, 0.0f));
                }
                else {
                    final TextView confirmTextView3 = this.confirmTextView;
                    if (isRTL2) {
                        gravity = 5;
                    }
                    else {
                        gravity = 3;
                    }
                    frameLayout.addView((View)confirmTextView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, gravity, 0.0f, 0.0f, 82.0f, 0.0f));
                    frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 76.0f, 21, 0.0f, 2.0f, 0.0f, 2.0f));
                }
            }
            else {
                this.confirmTextView.setGravity(49);
                final FrameLayout frameLayout2 = new FrameLayout(context);
                this.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
                if (this.currentType == 1) {
                    (this.blackImageView = new ImageView(context)).setImageResource(2131165856);
                    this.blackImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), PorterDuff$Mode.MULTIPLY));
                    frameLayout2.addView((View)this.blackImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    (this.blueImageView = new ImageView(context)).setImageResource(2131165854);
                    this.blueImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), PorterDuff$Mode.MULTIPLY));
                    frameLayout2.addView((View)this.blueImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.titleTextView.setText((CharSequence)LocaleController.getString("SentAppCodeTitle", 2131560718));
                }
                else {
                    (this.blueImageView = new ImageView(context)).setImageResource(2131165855);
                    this.blueImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), PorterDuff$Mode.MULTIPLY));
                    frameLayout2.addView((View)this.blueImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.titleTextView.setText((CharSequence)LocaleController.getString("SentSmsCodeTitle", 2131560722));
                }
                this.addView((View)this.titleTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 0, 18, 0, 0));
                this.addView((View)this.confirmTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 0, 17, 0, 0));
            }
            (this.codeFieldContainer = new LinearLayout(context)).setOrientation(0);
            this.addView((View)this.codeFieldContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 36, 1));
            if (this.currentType == 3) {
                this.codeFieldContainer.setVisibility(8);
            }
            (this.timeText = new TextView(context) {
                protected void onMeasure(final int n, final int n2) {
                    super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                }
            }).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.timeText.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            if (this.currentType == 3) {
                this.timeText.setTextSize(1, 14.0f);
                final TextView timeText = this.timeText;
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                else {
                    gravity = 3;
                }
                this.addView((View)timeText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, gravity));
                this.progressView = new ProgressView(context);
                final TextView timeText2 = this.timeText;
                gravity = n;
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                timeText2.setGravity(gravity);
                this.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 3, 0.0f, 12.0f, 0.0f, 0.0f));
            }
            else {
                this.timeText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
                this.timeText.setTextSize(1, 15.0f);
                this.timeText.setGravity(49);
                this.addView((View)this.timeText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
            }
            (this.problemText = new TextView(context) {
                protected void onMeasure(final int n, final int n2) {
                    super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                }
            }).setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.problemText.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.problemText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
            this.problemText.setTextSize(1, 15.0f);
            this.problemText.setGravity(49);
            if (this.currentType == 1) {
                this.problemText.setText((CharSequence)LocaleController.getString("DidNotGetTheCodeSms", 2131559267));
            }
            else {
                this.problemText.setText((CharSequence)LocaleController.getString("DidNotGetTheCode", 2131559266));
            }
            this.addView((View)this.problemText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
            this.problemText.setOnClickListener((View$OnClickListener)new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$72NXgFF_13BImj3ROsBvF0l8AmQ(this));
        }
        
        private void createCodeTimer() {
            if (this.codeTimer != null) {
                return;
            }
            this.codeTime = 15000;
            this.codeTimer = new Timer();
            this.lastCodeTime = (double)System.currentTimeMillis();
            this.codeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    AndroidUtilities.runOnUIThread(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$4$30Wvb2875vVECuq8Lqz3EDACKYg(this));
                }
            }, 0L, 1000L);
        }
        
        private void createTimer() {
            if (this.timeTimer != null) {
                return;
            }
            (this.timeTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    if (LoginActivitySmsView.this.timeTimer == null) {
                        return;
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            final double v = (double)System.currentTimeMillis();
                            final double access$2000 = LoginActivitySmsView.this.lastCurrentTime;
                            Double.isNaN(v);
                            final LoginActivitySmsView this$1 = LoginActivitySmsView.this;
                            final double v2 = this$1.time;
                            Double.isNaN(v2);
                            this$1.time = (int)(v2 - (v - access$2000));
                            LoginActivitySmsView.this.lastCurrentTime = v;
                            if (LoginActivitySmsView.this.time >= 1000) {
                                final int n = LoginActivitySmsView.this.time / 1000 / 60;
                                final int n2 = LoginActivitySmsView.this.time / 1000 - n * 60;
                                if (LoginActivitySmsView.this.nextType != 4 && LoginActivitySmsView.this.nextType != 3) {
                                    if (LoginActivitySmsView.this.nextType == 2) {
                                        LoginActivitySmsView.this.timeText.setText((CharSequence)LocaleController.formatString("SmsText", 2131560793, n, n2));
                                    }
                                }
                                else {
                                    LoginActivitySmsView.this.timeText.setText((CharSequence)LocaleController.formatString("CallText", 2131558885, n, n2));
                                }
                                if (LoginActivitySmsView.this.progressView != null) {
                                    LoginActivitySmsView.this.progressView.setProgress(1.0f - LoginActivitySmsView.this.time / (float)LoginActivitySmsView.this.timeout);
                                }
                            }
                            else {
                                if (LoginActivitySmsView.this.progressView != null) {
                                    LoginActivitySmsView.this.progressView.setProgress(1.0f);
                                }
                                LoginActivitySmsView.this.destroyTimer();
                                if (LoginActivitySmsView.this.currentType == 3) {
                                    AndroidUtilities.setWaitingForCall(false);
                                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
                                    LoginActivitySmsView.this.waitingForEvent = false;
                                    LoginActivitySmsView.this.destroyCodeTimer();
                                    LoginActivitySmsView.this.resendCode();
                                }
                                else if (LoginActivitySmsView.this.currentType == 2 || LoginActivitySmsView.this.currentType == 4) {
                                    if (LoginActivitySmsView.this.nextType != 4 && LoginActivitySmsView.this.nextType != 2) {
                                        if (LoginActivitySmsView.this.nextType == 3) {
                                            AndroidUtilities.setWaitingForSms(false);
                                            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                                            LoginActivitySmsView.this.waitingForEvent = false;
                                            LoginActivitySmsView.this.destroyCodeTimer();
                                            LoginActivitySmsView.this.resendCode();
                                        }
                                    }
                                    else {
                                        if (LoginActivitySmsView.this.nextType == 4) {
                                            LoginActivitySmsView.this.timeText.setText((CharSequence)LocaleController.getString("Calling", 2131558887));
                                        }
                                        else {
                                            LoginActivitySmsView.this.timeText.setText((CharSequence)LocaleController.getString("SendingSms", 2131560714));
                                        }
                                        LoginActivitySmsView.this.createCodeTimer();
                                        final TLRPC.TL_auth_resendCode tl_auth_resendCode = new TLRPC.TL_auth_resendCode();
                                        tl_auth_resendCode.phone_number = LoginActivitySmsView.this.phone;
                                        tl_auth_resendCode.phone_code_hash = LoginActivitySmsView.this.phoneHash;
                                        ConnectionsManager.getInstance(CancelAccountDeletionActivity.this.currentAccount).sendRequest(tl_auth_resendCode, new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$5$1$TKosr_VcccEYaoxCJjoMXxLoY3g(this), 2);
                                    }
                                }
                            }
                        }
                    });
                }
            }, 0L, 1000L);
        }
        
        private void destroyCodeTimer() {
            try {
                synchronized (this.timerSync) {
                    if (this.codeTimer != null) {
                        this.codeTimer.cancel();
                        this.codeTimer = null;
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        private void destroyTimer() {
            try {
                synchronized (this.timerSync) {
                    if (this.timeTimer != null) {
                        this.timeTimer.cancel();
                        this.timeTimer = null;
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        private String getCode() {
            if (this.codeField == null) {
                return "";
            }
            final StringBuilder sb = new StringBuilder();
            int n = 0;
            while (true) {
                final EditTextBoldCursor[] codeField = this.codeField;
                if (n >= codeField.length) {
                    break;
                }
                sb.append(PhoneFormat.stripExceptNumbers(codeField[n].getText().toString()));
                ++n;
            }
            return sb.toString();
        }
        
        private void resendCode() {
            final Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            this.nextPressed = true;
            CancelAccountDeletionActivity.this.needShowProgress();
            final TLRPC.TL_auth_resendCode tl_auth_resendCode = new TLRPC.TL_auth_resendCode();
            tl_auth_resendCode.phone_number = this.phone;
            tl_auth_resendCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(CancelAccountDeletionActivity.this.currentAccount).sendRequest(tl_auth_resendCode, new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$ymkWVQFIhlznIih0Xq953phiTOo(this, bundle, tl_auth_resendCode), 2);
        }
        
        @Override
        public void didReceivedNotification(final int n, final int n2, final Object... array) {
            if (this.waitingForEvent) {
                final EditTextBoldCursor[] codeField = this.codeField;
                if (codeField != null) {
                    if (n == NotificationCenter.didReceiveSmsCode) {
                        final EditTextBoldCursor editTextBoldCursor = codeField[0];
                        final StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(array[0]);
                        editTextBoldCursor.setText((CharSequence)sb.toString());
                        this.onNextPressed();
                    }
                    else if (n == NotificationCenter.didReceiveCall) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("");
                        sb2.append(array[0]);
                        final String string = sb2.toString();
                        if (!AndroidUtilities.checkPhonePattern(this.pattern, string)) {
                            return;
                        }
                        this.ignoreOnTextChange = true;
                        this.codeField[0].setText((CharSequence)string);
                        this.ignoreOnTextChange = false;
                        this.onNextPressed();
                    }
                }
            }
        }
        
        @Override
        public String getHeaderName() {
            if (this.currentType == 1) {
                return this.phone;
            }
            return LocaleController.getString("CancelAccountReset", 2131558892);
        }
        
        @Override
        public boolean needBackButton() {
            return true;
        }
        
        @Override
        public void onDestroyActivity() {
            super.onDestroyActivity();
            final int currentType = this.currentType;
            if (currentType == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (currentType == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            this.destroyTimer();
            this.destroyCodeTimer();
        }
        
        protected void onLayout(final boolean b, int n, int bottom, int n2, final int n3) {
            super.onLayout(b, n, bottom, n2, n3);
            if (this.currentType != 3 && this.blueImageView != null) {
                bottom = this.confirmTextView.getBottom();
                n = this.getMeasuredHeight() - bottom;
                if (this.problemText.getVisibility() == 0) {
                    n2 = this.problemText.getMeasuredHeight();
                    n = n + bottom - n2;
                    final TextView problemText = this.problemText;
                    problemText.layout(problemText.getLeft(), n, this.problemText.getRight(), n2 + n);
                }
                else if (this.timeText.getVisibility() == 0) {
                    n2 = this.timeText.getMeasuredHeight();
                    n = n + bottom - n2;
                    final TextView timeText = this.timeText;
                    timeText.layout(timeText.getLeft(), n, this.timeText.getRight(), n2 + n);
                }
                else {
                    n += bottom;
                }
                n2 = this.codeFieldContainer.getMeasuredHeight();
                n = (n - bottom - n2) / 2 + bottom;
                final LinearLayout codeFieldContainer = this.codeFieldContainer;
                codeFieldContainer.layout(codeFieldContainer.getLeft(), n, this.codeFieldContainer.getRight(), n2 + n);
            }
        }
        
        protected void onMeasure(int dp, int n) {
            super.onMeasure(dp, n);
            if (this.currentType != 3) {
                final ImageView blueImageView = this.blueImageView;
                if (blueImageView != null) {
                    n = blueImageView.getMeasuredHeight() + this.titleTextView.getMeasuredHeight() + this.confirmTextView.getMeasuredHeight() + AndroidUtilities.dp(35.0f);
                    dp = AndroidUtilities.dp(80.0f);
                    final int dp2 = AndroidUtilities.dp(291.0f);
                    if (CancelAccountDeletionActivity.this.scrollHeight - n < dp) {
                        this.setMeasuredDimension(this.getMeasuredWidth(), n + dp);
                    }
                    else if (CancelAccountDeletionActivity.this.scrollHeight > dp2) {
                        this.setMeasuredDimension(this.getMeasuredWidth(), dp2);
                    }
                    else {
                        this.setMeasuredDimension(this.getMeasuredWidth(), CancelAccountDeletionActivity.this.scrollHeight);
                    }
                }
            }
        }
        
        @Override
        public void onNextPressed() {
            if (this.nextPressed) {
                return;
            }
            final String code = this.getCode();
            if (TextUtils.isEmpty((CharSequence)code)) {
                AndroidUtilities.shakeView((View)this.codeFieldContainer, 2.0f, 0);
                return;
            }
            this.nextPressed = true;
            final int currentType = this.currentType;
            if (currentType == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (currentType == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            final TLRPC.TL_account_confirmPhone tl_account_confirmPhone = new TLRPC.TL_account_confirmPhone();
            tl_account_confirmPhone.phone_code = code;
            tl_account_confirmPhone.phone_code_hash = this.phoneHash;
            this.destroyTimer();
            CancelAccountDeletionActivity.this.needShowProgress();
            ConnectionsManager.getInstance(CancelAccountDeletionActivity.this.currentAccount).sendRequest(tl_account_confirmPhone, new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$1W4kZzPjzRDtXCpazovyaTPi9gk(this, tl_account_confirmPhone), 2);
        }
        
        @Override
        public void onShow() {
            super.onShow();
            if (this.currentType == 3) {
                return;
            }
            AndroidUtilities.runOnUIThread(new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$dLIqV278x4RUePN_aKF0rcdGRXU(this), 100L);
        }
        
        @Override
        public void setParams(final Bundle currentParams, final boolean b) {
            if (currentParams == null) {
                return;
            }
            this.waitingForEvent = true;
            final int currentType = this.currentType;
            if (currentType == 2) {
                AndroidUtilities.setWaitingForSms(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (currentType == 3) {
                AndroidUtilities.setWaitingForCall(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }
            this.currentParams = currentParams;
            this.phone = currentParams.getString("phone");
            this.phoneHash = currentParams.getString("phoneHash");
            final int int1 = currentParams.getInt("timeout");
            this.time = int1;
            this.timeout = int1;
            this.openTime = (int)(System.currentTimeMillis() / 1000L);
            this.nextType = currentParams.getInt("nextType");
            this.pattern = currentParams.getString("pattern");
            this.length = currentParams.getInt("length");
            if (this.length == 0) {
                this.length = 5;
            }
            final EditTextBoldCursor[] codeField = this.codeField;
            int n = 8;
            if (codeField != null && codeField.length == this.length) {
                int n2 = 0;
                while (true) {
                    final EditTextBoldCursor[] codeField2 = this.codeField;
                    if (n2 >= codeField2.length) {
                        break;
                    }
                    codeField2[n2].setText((CharSequence)"");
                    ++n2;
                }
            }
            else {
                this.codeField = new EditTextBoldCursor[this.length];
                for (int i = 0; i < this.length; ++i) {
                    (this.codeField[i] = new EditTextBoldCursor(this.getContext())).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.codeField[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.codeField[i].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.codeField[i].setCursorWidth(1.5f);
                    final Drawable mutate = this.getResources().getDrawable(2131165811).mutate();
                    mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteInputFieldActivated"), PorterDuff$Mode.MULTIPLY));
                    this.codeField[i].setBackgroundDrawable(mutate);
                    this.codeField[i].setImeOptions(268435461);
                    this.codeField[i].setTextSize(1, 20.0f);
                    this.codeField[i].setMaxLines(1);
                    this.codeField[i].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    this.codeField[i].setPadding(0, 0, 0, 0);
                    this.codeField[i].setGravity(49);
                    if (this.currentType == 3) {
                        this.codeField[i].setEnabled(false);
                        this.codeField[i].setInputType(0);
                        this.codeField[i].setVisibility(8);
                    }
                    else {
                        this.codeField[i].setInputType(3);
                    }
                    final LinearLayout codeFieldContainer = this.codeFieldContainer;
                    final EditTextBoldCursor editTextBoldCursor = this.codeField[i];
                    int n3;
                    if (i != this.length - 1) {
                        n3 = 7;
                    }
                    else {
                        n3 = 0;
                    }
                    codeFieldContainer.addView((View)editTextBoldCursor, (ViewGroup$LayoutParams)LayoutHelper.createLinear(34, 36, 1, 0, 0, n3, 0));
                    this.codeField[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                        public void afterTextChanged(final Editable editable) {
                            if (LoginActivitySmsView.this.ignoreOnTextChange) {
                                return;
                            }
                            final int length = editable.length();
                            if (length >= 1) {
                                if (length > 1) {
                                    final String string = editable.toString();
                                    LoginActivitySmsView.this.ignoreOnTextChange = true;
                                    for (int i = 0; i < Math.min(LoginActivitySmsView.this.length - i, length); ++i) {
                                        if (i == 0) {
                                            editable.replace(0, length, (CharSequence)string.substring(i, i + 1));
                                        }
                                        else {
                                            LoginActivitySmsView.this.codeField[i + i].setText((CharSequence)string.substring(i, i + 1));
                                        }
                                    }
                                    LoginActivitySmsView.this.ignoreOnTextChange = false;
                                }
                                if (i != LoginActivitySmsView.this.length - 1) {
                                    LoginActivitySmsView.this.codeField[i + 1].setSelection(LoginActivitySmsView.this.codeField[i + 1].length());
                                    LoginActivitySmsView.this.codeField[i + 1].requestFocus();
                                }
                                if ((i == LoginActivitySmsView.this.length - 1 || (i == LoginActivitySmsView.this.length - 2 && length >= 2)) && LoginActivitySmsView.this.getCode().length() == LoginActivitySmsView.this.length) {
                                    LoginActivitySmsView.this.onNextPressed();
                                }
                            }
                        }
                        
                        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                        
                        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                    });
                    this.codeField[i].setOnKeyListener((View$OnKeyListener)new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$ZA7_mAFfTFMlQCzBbBXS9uV3KIM(this, i));
                    this.codeField[i].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$jgzzj4_t91kdP6uXqtjWCi0O3d8(this));
                }
            }
            final ProgressView progressView = this.progressView;
            if (progressView != null) {
                int visibility;
                if (this.nextType != 0) {
                    visibility = 0;
                }
                else {
                    visibility = 8;
                }
                progressView.setVisibility(visibility);
            }
            if (this.phone == null) {
                return;
            }
            final String format = PhoneFormat.getInstance().format(this.phone);
            final PhoneFormat instance = PhoneFormat.getInstance();
            final StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(format);
            this.confirmTextView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("CancelAccountResetInfo", 2131558893, instance.format(sb.toString()))));
            if (this.currentType != 3) {
                AndroidUtilities.showKeyboard((View)this.codeField[0]);
                this.codeField[0].requestFocus();
            }
            else {
                AndroidUtilities.hideKeyboard((View)this.codeField[0]);
            }
            this.destroyTimer();
            this.destroyCodeTimer();
            this.lastCurrentTime = (double)System.currentTimeMillis();
            final int currentType2 = this.currentType;
            if (currentType2 == 1) {
                this.problemText.setVisibility(0);
                this.timeText.setVisibility(8);
            }
            else {
                if (currentType2 == 3) {
                    final int nextType = this.nextType;
                    if (nextType == 4 || nextType == 2) {
                        this.problemText.setVisibility(8);
                        this.timeText.setVisibility(0);
                        final int nextType2 = this.nextType;
                        if (nextType2 == 4) {
                            this.timeText.setText((CharSequence)LocaleController.formatString("CallText", 2131558885, 1, 0));
                        }
                        else if (nextType2 == 2) {
                            this.timeText.setText((CharSequence)LocaleController.formatString("SmsText", 2131560793, 1, 0));
                        }
                        this.createTimer();
                        return;
                    }
                }
                if (this.currentType == 2) {
                    final int nextType3 = this.nextType;
                    if (nextType3 == 4 || nextType3 == 3) {
                        this.timeText.setText((CharSequence)LocaleController.formatString("CallText", 2131558885, 2, 0));
                        final TextView problemText = this.problemText;
                        int visibility2;
                        if (this.time < 1000) {
                            visibility2 = 0;
                        }
                        else {
                            visibility2 = 8;
                        }
                        problemText.setVisibility(visibility2);
                        final TextView timeText = this.timeText;
                        if (this.time >= 1000) {
                            n = 0;
                        }
                        timeText.setVisibility(n);
                        this.createTimer();
                        return;
                    }
                }
                if (this.currentType == 4 && this.nextType == 2) {
                    this.timeText.setText((CharSequence)LocaleController.formatString("SmsText", 2131560793, 2, 0));
                    final TextView problemText2 = this.problemText;
                    int visibility3;
                    if (this.time < 1000) {
                        visibility3 = 0;
                    }
                    else {
                        visibility3 = 8;
                    }
                    problemText2.setVisibility(visibility3);
                    final TextView timeText2 = this.timeText;
                    if (this.time >= 1000) {
                        n = 0;
                    }
                    timeText2.setVisibility(n);
                    this.createTimer();
                }
                else {
                    this.timeText.setVisibility(8);
                    this.problemText.setVisibility(8);
                    this.createCodeTimer();
                }
            }
        }
    }
    
    public class PhoneView extends SlideView
    {
        private boolean nextPressed;
        private RadialProgressView progressBar;
        
        public PhoneView(final Context context) {
            super(context);
            this.nextPressed = false;
            this.setOrientation(1);
            final FrameLayout frameLayout = new FrameLayout(context);
            this.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 200));
            frameLayout.addView((View)(this.progressBar = new RadialProgressView(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
        }
        
        @Override
        public String getHeaderName() {
            return LocaleController.getString("CancelAccountReset", 2131558892);
        }
        
        @Override
        public void onNextPressed() {
            if (CancelAccountDeletionActivity.this.getParentActivity() != null) {
                if (!this.nextPressed) {
                    final TelephonyManager telephonyManager = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
                    if (telephonyManager.getSimState() != 1) {
                        telephonyManager.getPhoneType();
                    }
                    final int sdk_INT = Build$VERSION.SDK_INT;
                    final TLRPC.TL_account_sendConfirmPhoneCode tl_account_sendConfirmPhoneCode = new TLRPC.TL_account_sendConfirmPhoneCode();
                    tl_account_sendConfirmPhoneCode.hash = CancelAccountDeletionActivity.this.hash;
                    tl_account_sendConfirmPhoneCode.settings = new TLRPC.TL_codeSettings();
                    final TLRPC.TL_codeSettings settings = tl_account_sendConfirmPhoneCode.settings;
                    settings.allow_flashcall = false;
                    if (Build$VERSION.SDK_INT >= 26) {
                        try {
                            settings.app_hash = SmsManager.getDefault().createAppSpecificSmsToken(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, (Class)SmsReceiver.class), 134217728));
                        }
                        catch (Throwable t) {
                            FileLog.e(t);
                        }
                    }
                    else {
                        settings.app_hash = BuildVars.SMS_HASH;
                        settings.app_hash_persistent = true;
                    }
                    final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                    if (!TextUtils.isEmpty((CharSequence)tl_account_sendConfirmPhoneCode.settings.app_hash)) {
                        final TLRPC.TL_codeSettings settings2 = tl_account_sendConfirmPhoneCode.settings;
                        settings2.flags |= 0x8;
                        sharedPreferences.edit().putString("sms_hash", tl_account_sendConfirmPhoneCode.settings.app_hash).commit();
                    }
                    else {
                        sharedPreferences.edit().remove("sms_hash").commit();
                    }
                    if (tl_account_sendConfirmPhoneCode.settings.allow_flashcall) {
                        try {
                            final String line1Number = telephonyManager.getLine1Number();
                            if (!TextUtils.isEmpty((CharSequence)line1Number)) {
                                if (!(tl_account_sendConfirmPhoneCode.settings.current_number = PhoneNumberUtils.compare(CancelAccountDeletionActivity.this.phone, line1Number))) {
                                    tl_account_sendConfirmPhoneCode.settings.allow_flashcall = false;
                                }
                            }
                            else {
                                tl_account_sendConfirmPhoneCode.settings.current_number = false;
                            }
                        }
                        catch (Exception ex) {
                            tl_account_sendConfirmPhoneCode.settings.allow_flashcall = false;
                            FileLog.e(ex);
                        }
                    }
                    final Bundle bundle = new Bundle();
                    bundle.putString("phone", CancelAccountDeletionActivity.this.phone);
                    this.nextPressed = true;
                    ConnectionsManager.getInstance(CancelAccountDeletionActivity.this.currentAccount).sendRequest(tl_account_sendConfirmPhoneCode, new _$$Lambda$CancelAccountDeletionActivity$PhoneView$k7vfL3HDSHw4EqLEYwRz3mk5u4I(this, bundle, tl_account_sendConfirmPhoneCode), 2);
                }
            }
        }
        
        @Override
        public void onShow() {
            super.onShow();
            this.onNextPressed();
        }
    }
    
    private class ProgressView extends View
    {
        private Paint paint;
        private Paint paint2;
        private float progress;
        
        public ProgressView(final Context context) {
            super(context);
            this.paint = new Paint();
            this.paint2 = new Paint();
            this.paint.setColor(Theme.getColor("login_progressInner"));
            this.paint2.setColor(Theme.getColor("login_progressOuter"));
        }
        
        protected void onDraw(final Canvas canvas) {
            final float n = (float)(int)(this.getMeasuredWidth() * this.progress);
            canvas.drawRect(0.0f, 0.0f, n, (float)this.getMeasuredHeight(), this.paint2);
            canvas.drawRect(n, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.paint);
        }
        
        public void setProgress(final float progress) {
            this.progress = progress;
            this.invalidate();
        }
    }
}

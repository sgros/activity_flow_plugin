// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.webkit.JavascriptInterface;
import org.telegram.messenger.BuildVars;
import android.view.ViewParent;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.annotation.SuppressLint;
import org.telegram.ui.ActionBar.ActionBarMenu;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.graphics.Paint;
import android.os.Build$VERSION;
import android.widget.FrameLayout;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.app.Dialog;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View;
import android.os.Build;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.tgnet.TLRPC;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import org.telegram.messenger.FileLog;
import android.content.Context;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.SerializedData;
import org.telegram.messenger.Utilities;
import java.net.URLEncoder;
import org.telegram.messenger.ApplicationLoader;
import android.app.Activity;
import android.text.TextUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesController;
import android.webkit.WebView;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.ActionBar.BaseFragment;

public class WebviewActivity extends BaseFragment
{
    private static final int TYPE_GAME = 0;
    private static final int TYPE_STAT = 1;
    private static final int open_in = 2;
    private static final int share = 1;
    private String currentBot;
    private long currentDialogId;
    private String currentGame;
    private MessageObject currentMessageObject;
    private String currentUrl;
    private String linkToCopy;
    private boolean loadStats;
    private ActionBarMenuItem progressItem;
    private ContextProgressView progressView;
    private String short_param;
    private int type;
    public Runnable typingRunnable;
    private WebView webView;
    
    public WebviewActivity(final String currentUrl, final long currentDialogId) {
        this.typingRunnable = new Runnable() {
            @Override
            public void run() {
                if (WebviewActivity.this.currentMessageObject != null && WebviewActivity.this.getParentActivity() != null) {
                    final WebviewActivity this$0 = WebviewActivity.this;
                    if (this$0.typingRunnable != null) {
                        MessagesController.getInstance(this$0.currentAccount).sendTyping(WebviewActivity.this.currentMessageObject.getDialogId(), 6, 0);
                        AndroidUtilities.runOnUIThread(WebviewActivity.this.typingRunnable, 25000L);
                    }
                }
            }
        };
        this.currentUrl = currentUrl;
        this.currentDialogId = currentDialogId;
        this.type = 1;
    }
    
    public WebviewActivity(String string, final String currentBot, final String currentGame, final String s, final MessageObject currentMessageObject) {
        this.typingRunnable = new Runnable() {
            @Override
            public void run() {
                if (WebviewActivity.this.currentMessageObject != null && WebviewActivity.this.getParentActivity() != null) {
                    final WebviewActivity this$0 = WebviewActivity.this;
                    if (this$0.typingRunnable != null) {
                        MessagesController.getInstance(this$0.currentAccount).sendTyping(WebviewActivity.this.currentMessageObject.getDialogId(), 6, 0);
                        AndroidUtilities.runOnUIThread(WebviewActivity.this.typingRunnable, 25000L);
                    }
                }
            }
        };
        this.currentUrl = string;
        this.currentBot = currentBot;
        this.currentGame = currentGame;
        this.currentMessageObject = currentMessageObject;
        this.short_param = s;
        final StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
        sb.append("/");
        sb.append(this.currentBot);
        if (TextUtils.isEmpty((CharSequence)s)) {
            string = "";
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("?game=");
            sb2.append(s);
            string = sb2.toString();
        }
        sb.append(string);
        this.linkToCopy = sb.toString();
        this.type = 0;
    }
    
    public static void openGameInBrowser(String s, final MessageObject messageObject, final Activity activity, final String str, final String str2) {
        final String s2 = "";
        try {
            final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("botshare", 0);
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(messageObject.getId());
            final String string = sharedPreferences.getString(sb.toString(), (String)null);
            String str3;
            if (string != null) {
                str3 = string;
            }
            else {
                str3 = "";
            }
            final StringBuilder sb2 = new StringBuilder(str3);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("tgShareScoreUrl=");
            sb3.append(URLEncoder.encode("tgb://share_game_score?hash=", "UTF-8"));
            final StringBuilder sb4 = new StringBuilder(sb3.toString());
            if (string == null) {
                final char[] charArray = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                for (int i = 0; i < 20; ++i) {
                    sb2.append(charArray[Utilities.random.nextInt(charArray.length)]);
                }
            }
            sb4.append((CharSequence)sb2);
            final int index = s.indexOf(35);
            if (index < 0) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(s);
                sb5.append("#");
                sb5.append((Object)sb4);
                s = sb5.toString();
            }
            else {
                final String substring = s.substring(index + 1);
                if (substring.indexOf(61) < 0 && substring.indexOf(63) < 0) {
                    if (substring.length() > 0) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append(s);
                        sb6.append("?");
                        sb6.append((Object)sb4);
                        s = sb6.toString();
                    }
                    else {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append(s);
                        sb7.append((Object)sb4);
                        s = sb7.toString();
                    }
                }
                else {
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append(s);
                    sb8.append("&");
                    sb8.append((Object)sb4);
                    s = sb8.toString();
                }
            }
            final SharedPreferences$Editor edit = sharedPreferences.edit();
            final StringBuilder sb9 = new StringBuilder();
            sb9.append((Object)sb2);
            sb9.append("_date");
            edit.putInt(sb9.toString(), (int)(System.currentTimeMillis() / 1000L));
            final SerializedData serializedData = new SerializedData(messageObject.messageOwner.getObjectSize());
            messageObject.messageOwner.serializeToStream(serializedData);
            final StringBuilder sb10 = new StringBuilder();
            sb10.append((Object)sb2);
            sb10.append("_m");
            edit.putString(sb10.toString(), Utilities.bytesToHex(serializedData.toByteArray()));
            final StringBuilder sb11 = new StringBuilder();
            sb11.append((Object)sb2);
            sb11.append("_link");
            final String string2 = sb11.toString();
            final StringBuilder sb12 = new StringBuilder();
            sb12.append("https://");
            sb12.append(MessagesController.getInstance(messageObject.currentAccount).linkPrefix);
            sb12.append("/");
            sb12.append(str2);
            String string3;
            if (TextUtils.isEmpty((CharSequence)str)) {
                string3 = s2;
            }
            else {
                final StringBuilder sb13 = new StringBuilder();
                sb13.append("?game=");
                sb13.append(str);
                string3 = sb13.toString();
            }
            sb12.append(string3);
            edit.putString(string2, sb12.toString());
            edit.commit();
            Browser.openUrl((Context)activity, s, false);
            serializedData.cleanup();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void reloadStats(String params) {
        if (this.loadStats) {
            return;
        }
        this.loadStats = true;
        final TLRPC.TL_messages_getStatsURL tl_messages_getStatsURL = new TLRPC.TL_messages_getStatsURL();
        tl_messages_getStatsURL.peer = MessagesController.getInstance(super.currentAccount).getInputPeer((int)this.currentDialogId);
        if (params == null) {
            params = "";
        }
        tl_messages_getStatsURL.params = params;
        tl_messages_getStatsURL.dark = Theme.getCurrentTheme().isDark();
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_getStatsURL, new _$$Lambda$WebviewActivity$O62eLYvTahA2PRe6UItNx5NI6ao(this));
    }
    
    public static boolean supportWebview() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        return !"samsung".equals(manufacturer) || !"GT-I9500".equals(model);
    }
    
    @SuppressLint({ "SetJavaScriptEnabled", "AddJavascriptInterface" })
    @Override
    public View createView(final Context context) {
        super.swipeBackEnabled = false;
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    WebviewActivity.this.finishFragment();
                }
                else if (n == 1) {
                    if (WebviewActivity.this.currentMessageObject != null) {
                        WebviewActivity.this.currentMessageObject.messageOwner.with_my_score = false;
                        final WebviewActivity this$0 = WebviewActivity.this;
                        this$0.showDialog(ShareAlert.createShareAlert((Context)this$0.getParentActivity(), WebviewActivity.this.currentMessageObject, null, false, WebviewActivity.this.linkToCopy, false));
                    }
                }
                else if (n == 2) {
                    WebviewActivity.openGameInBrowser(WebviewActivity.this.currentUrl, WebviewActivity.this.currentMessageObject, WebviewActivity.this.getParentActivity(), WebviewActivity.this.short_param, WebviewActivity.this.currentBot);
                }
            }
        });
        final ActionBarMenu menu = super.actionBar.createMenu();
        this.progressItem = menu.addItemWithWidth(1, 2131165818, AndroidUtilities.dp(54.0f));
        final int type = this.type;
        if (type == 0) {
            menu.addItem(0, 2131165416).addSubItem(2, 2131165649, LocaleController.getString("OpenInExternalApp", 2131560116));
            super.actionBar.setTitle(this.currentGame);
            final ActionBar actionBar = super.actionBar;
            final StringBuilder sb = new StringBuilder();
            sb.append("@");
            sb.append(this.currentBot);
            actionBar.setSubtitle(sb.toString());
            this.progressView = new ContextProgressView(context, 1);
            this.progressItem.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            this.progressView.setAlpha(0.0f);
            this.progressView.setScaleX(0.1f);
            this.progressView.setScaleY(0.1f);
            this.progressView.setVisibility(4);
        }
        else if (type == 1) {
            super.actionBar.setBackgroundColor(Theme.getColor("player_actionBar"));
            super.actionBar.setItemsColor(Theme.getColor("player_actionBarItems"), false);
            super.actionBar.setItemsBackgroundColor(Theme.getColor("player_actionBarSelector"), false);
            super.actionBar.setTitleColor(Theme.getColor("player_actionBarTitle"));
            super.actionBar.setSubtitleColor(Theme.getColor("player_actionBarSubtitle"));
            super.actionBar.setTitle(LocaleController.getString("Statistics", 2131560806));
            this.progressView = new ContextProgressView(context, 3);
            this.progressItem.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            this.progressView.setAlpha(1.0f);
            this.progressView.setScaleX(1.0f);
            this.progressView.setScaleY(1.0f);
            this.progressView.setVisibility(0);
            this.progressItem.getImageView().setVisibility(8);
            this.progressItem.setEnabled(false);
        }
        this.webView = new WebView(context);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        if (Build$VERSION.SDK_INT >= 19) {
            this.webView.setLayerType(2, (Paint)null);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.webView.getSettings().setMixedContentMode(0);
            CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
            if (this.type == 0) {
                this.webView.addJavascriptInterface((Object)new TelegramWebviewProxy(), "TelegramWebviewProxy");
            }
        }
        this.webView.setWebViewClient((WebViewClient)new WebViewClient() {
            private boolean isInternalUrl(final String s) {
                if (TextUtils.isEmpty((CharSequence)s)) {
                    return false;
                }
                final Uri parse = Uri.parse(s);
                if ("tg".equals(parse.getScheme())) {
                    if (WebviewActivity.this.type == 1) {
                        try {
                            WebviewActivity.this.reloadStats(Uri.parse(s.replace("tg:statsrefresh", "tg://telegram.org")).getQueryParameter("params"));
                        }
                        catch (Throwable t) {
                            FileLog.e(t);
                        }
                    }
                    else {
                        WebviewActivity.this.finishFragment(false);
                        try {
                            final Intent intent = new Intent("android.intent.action.VIEW", parse);
                            intent.setComponent(new ComponentName(ApplicationLoader.applicationContext.getPackageName(), LaunchActivity.class.getName()));
                            intent.putExtra("com.android.browser.application_id", ApplicationLoader.applicationContext.getPackageName());
                            ApplicationLoader.applicationContext.startActivity(intent);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                    return true;
                }
                return false;
            }
            
            public void onLoadResource(final WebView webView, final String s) {
                if (this.isInternalUrl(s)) {
                    return;
                }
                super.onLoadResource(webView, s);
            }
            
            public void onPageFinished(final WebView webView, final String s) {
                super.onPageFinished(webView, s);
                if (WebviewActivity.this.progressView != null && WebviewActivity.this.progressView.getVisibility() == 0) {
                    final AnimatorSet set = new AnimatorSet();
                    if (WebviewActivity.this.type == 0) {
                        WebviewActivity.this.progressItem.getImageView().setVisibility(0);
                        WebviewActivity.this.progressItem.setEnabled(true);
                        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)WebviewActivity.this.progressView, "scaleX", new float[] { 1.0f, 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)WebviewActivity.this.progressView, "scaleY", new float[] { 1.0f, 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)WebviewActivity.this.progressView, "alpha", new float[] { 1.0f, 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)WebviewActivity.this.progressItem.getImageView(), "scaleX", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)WebviewActivity.this.progressItem.getImageView(), "scaleY", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)WebviewActivity.this.progressItem.getImageView(), "alpha", new float[] { 0.0f, 1.0f }) });
                    }
                    else {
                        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)WebviewActivity.this.progressView, "scaleX", new float[] { 1.0f, 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)WebviewActivity.this.progressView, "scaleY", new float[] { 1.0f, 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)WebviewActivity.this.progressView, "alpha", new float[] { 1.0f, 0.0f }) });
                    }
                    set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            if (WebviewActivity.this.type == 1) {
                                WebviewActivity.this.progressItem.setVisibility(8);
                            }
                            else {
                                WebviewActivity.this.progressView.setVisibility(4);
                            }
                        }
                    });
                    set.setDuration(150L);
                    set.start();
                }
            }
            
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                return this.isInternalUrl(s) || super.shouldOverrideUrlLoading(webView, s);
            }
        });
        frameLayout.addView((View)this.webView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        if (this.type == 0) {
            return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "actionBarDefaultSubmenuItemIcon"), new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressInner2"), new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressOuter2") };
        }
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "player_actionBar"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "player_actionBarItems"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "player_actionBarTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBTITLECOLOR, null, null, null, null, "player_actionBarTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "player_actionBarSelector"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "actionBarDefaultSubmenuItemIcon"), new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressInner4"), new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressOuter4") };
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        AndroidUtilities.cancelRunOnUIThread(this.typingRunnable);
        this.webView.setLayerType(0, (Paint)null);
        this.typingRunnable = null;
        try {
            final ViewParent parent = this.webView.getParent();
            if (parent != null) {
                ((FrameLayout)parent).removeView((View)this.webView);
            }
            this.webView.stopLoading();
            this.webView.loadUrl("about:blank");
            this.webView.destroy();
            this.webView = null;
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        AndroidUtilities.cancelRunOnUIThread(this.typingRunnable);
        this.typingRunnable.run();
    }
    
    @Override
    protected void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b && !b2) {
            final WebView webView = this.webView;
            if (webView != null) {
                webView.loadUrl(this.currentUrl);
            }
        }
    }
    
    private class TelegramWebviewProxy
    {
        @JavascriptInterface
        public void postEvent(final String s, final String s2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$WebviewActivity$TelegramWebviewProxy$4b6v9uSCLRENGHQEEzvYSfqVMkw(this, s));
        }
    }
}

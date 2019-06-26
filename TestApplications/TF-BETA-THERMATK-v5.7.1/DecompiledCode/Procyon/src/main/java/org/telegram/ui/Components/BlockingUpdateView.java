// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import java.util.Locale;
import org.telegram.messenger.MessageObject;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.UserConfig;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import java.io.File;
import androidx.core.content.FileProvider;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.FileLoader;
import android.app.Activity;
import org.telegram.messenger.FileLog;
import android.content.Intent;
import android.net.Uri;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.ApplicationLoader;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.text.method.MovementMethod;
import org.telegram.messenger.LocaleController;
import android.widget.ScrollView;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.animation.AnimatorSet;
import org.telegram.tgnet.TLRPC;
import android.widget.TextView;
import org.telegram.messenger.NotificationCenter;
import android.widget.FrameLayout;

public class BlockingUpdateView extends FrameLayout implements NotificationCenterDelegate
{
    private FrameLayout acceptButton;
    private TextView acceptTextView;
    private int accountNum;
    private TLRPC.TL_help_appUpdate appUpdate;
    private String fileName;
    private int pressCount;
    private AnimatorSet progressAnimation;
    private RadialProgress radialProgress;
    private FrameLayout radialProgressView;
    private TextView textView;
    
    public BlockingUpdateView(final Context context) {
        super(context);
        this.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        int n;
        if (Build$VERSION.SDK_INT >= 21) {
            n = (int)(AndroidUtilities.statusBarHeight / AndroidUtilities.density);
        }
        else {
            n = 0;
        }
        final FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(-11556378);
        final int dp = AndroidUtilities.dp(176.0f);
        int statusBarHeight;
        if (Build$VERSION.SDK_INT >= 21) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        this.addView((View)frameLayout, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, dp + statusBarHeight));
        final ImageView imageView = new ImageView(context);
        imageView.setImageResource(2131165518);
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        imageView.setPadding(0, 0, 0, AndroidUtilities.dp(14.0f));
        frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, (float)n, 0.0f, 0.0f));
        imageView.setOnClickListener((View$OnClickListener)new _$$Lambda$BlockingUpdateView$ninaHPZEm5ZV8HP69_4gylboxn4(this));
        final ScrollView scrollView = new ScrollView(context);
        AndroidUtilities.setScrollViewEdgeEffectColor(scrollView, Theme.getColor("actionBarDefault"));
        this.addView((View)scrollView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 27.0f, (float)(n + 206), 27.0f, 130.0f));
        final FrameLayout frameLayout2 = new FrameLayout(context);
        scrollView.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createScroll(-1, -2, 17));
        final TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        textView.setTextSize(1, 20.0f);
        textView.setGravity(49);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setText((CharSequence)LocaleController.getString("UpdateTelegram", 2131560955));
        frameLayout2.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 49));
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setMovementMethod((MovementMethod)new AndroidUtilities.LinkMovementMethodMy());
        this.textView.setGravity(49);
        this.textView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
        frameLayout2.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 44.0f, 0.0f, 0.0f));
        (this.acceptButton = new FrameLayout(context)).setBackgroundResource(2131165800);
        if (Build$VERSION.SDK_INT >= 21) {
            final StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.acceptButton, "translationZ", new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
            stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.acceptButton, "translationZ", new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
            this.acceptButton.setStateListAnimator(stateListAnimator);
        }
        this.acceptButton.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.addView((View)this.acceptButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 56.0f, 81, 0.0f, 0.0f, 0.0f, 45.0f));
        this.acceptButton.setOnClickListener((View$OnClickListener)new _$$Lambda$BlockingUpdateView$58XjGl4nF8__CcazfB65zVJEC_c(this));
        (this.acceptTextView = new TextView(context)).setGravity(17);
        this.acceptTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.acceptTextView.setTextColor(-1);
        this.acceptTextView.setTextSize(1, 16.0f);
        this.acceptButton.addView((View)this.acceptTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
        (this.radialProgressView = new FrameLayout(context) {
            protected void onDraw(final Canvas canvas) {
                BlockingUpdateView.this.radialProgress.draw(canvas);
            }
            
            protected void onLayout(final boolean b, int n, int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                final int dp = AndroidUtilities.dp(36.0f);
                n = (n3 - n - dp) / 2;
                n2 = (n4 - n2 - dp) / 2;
                BlockingUpdateView.this.radialProgress.setProgressRect(n, n2, n + dp, dp + n2);
            }
        }).setWillNotDraw(false);
        this.radialProgressView.setAlpha(0.0f);
        this.radialProgressView.setScaleX(0.1f);
        this.radialProgressView.setScaleY(0.1f);
        this.radialProgressView.setVisibility(4);
        (this.radialProgress = new RadialProgress((View)this.radialProgressView)).setBackground(null, true, false);
        this.radialProgress.setProgressColor(-1);
        this.acceptButton.addView((View)this.radialProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36, 17));
    }
    
    public static boolean checkApkInstallPermissions(final Context context) {
        if (Build$VERSION.SDK_INT >= 26 && !ApplicationLoader.applicationContext.getPackageManager().canRequestPackageInstalls()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(LocaleController.getString("AppName", 2131558635));
            builder.setMessage(LocaleController.getString("ApkRestricted", 2131558634));
            builder.setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131560419), (DialogInterface$OnClickListener)new _$$Lambda$BlockingUpdateView$ftz1vauUG0mc962Y32crTPmAh74(context));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
            builder.show();
            return false;
        }
        return true;
    }
    
    public static boolean openApkInstall(final Activity activity, final TLRPC.Document document) {
        boolean exists;
        final boolean b = exists = false;
        try {
            FileLoader.getAttachFileName(document);
            exists = b;
            final File pathToAttach = FileLoader.getPathToAttach(document, true);
            exists = b;
            final boolean b2 = exists = pathToAttach.exists();
            if (b2) {
                exists = b2;
                exists = b2;
                final Intent intent = new Intent("android.intent.action.VIEW");
                exists = b2;
                intent.setFlags(1);
                exists = b2;
                if (Build$VERSION.SDK_INT >= 24) {
                    exists = b2;
                    intent.setDataAndType(FileProvider.getUriForFile((Context)activity, "org.telegram.messenger.provider", pathToAttach), "application/vnd.android.package-archive");
                }
                else {
                    exists = b2;
                    intent.setDataAndType(Uri.fromFile(pathToAttach), "application/vnd.android.package-archive");
                }
                try {
                    activity.startActivityForResult(intent, 500);
                    exists = b2;
                }
                catch (Exception ex) {
                    exists = b2;
                    FileLog.e(ex);
                    exists = b2;
                }
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        return exists;
    }
    
    private void showProgress(final boolean b) {
        final AnimatorSet progressAnimation = this.progressAnimation;
        if (progressAnimation != null) {
            progressAnimation.cancel();
        }
        this.progressAnimation = new AnimatorSet();
        if (b) {
            this.radialProgressView.setVisibility(0);
            this.acceptButton.setEnabled(false);
            this.progressAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "alpha", new float[] { 1.0f }) });
        }
        else {
            this.acceptTextView.setVisibility(0);
            this.acceptButton.setEnabled(true);
            this.progressAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, "alpha", new float[] { 1.0f }) });
        }
        this.progressAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator obj) {
                if (BlockingUpdateView.this.progressAnimation != null && BlockingUpdateView.this.progressAnimation.equals(obj)) {
                    BlockingUpdateView.this.progressAnimation = null;
                }
            }
            
            public void onAnimationEnd(final Animator obj) {
                if (BlockingUpdateView.this.progressAnimation != null && BlockingUpdateView.this.progressAnimation.equals(obj)) {
                    if (!b) {
                        BlockingUpdateView.this.radialProgressView.setVisibility(4);
                    }
                    else {
                        BlockingUpdateView.this.acceptTextView.setVisibility(4);
                    }
                }
            }
        });
        this.progressAnimation.setDuration(150L);
        this.progressAnimation.start();
    }
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.fileDidLoad) {
            final String anObject = (String)array[0];
            final String fileName = this.fileName;
            if (fileName != null && fileName.equals(anObject)) {
                this.showProgress(false);
                openApkInstall((Activity)this.getContext(), this.appUpdate.document);
            }
        }
        else if (n == NotificationCenter.fileDidFailedLoad) {
            final String anObject2 = (String)array[0];
            final String fileName2 = this.fileName;
            if (fileName2 != null && fileName2.equals(anObject2)) {
                this.showProgress(false);
            }
        }
        else if (n == NotificationCenter.FileLoadProgressChanged) {
            final String anObject3 = (String)array[0];
            final String fileName3 = this.fileName;
            if (fileName3 != null && fileName3.equals(anObject3)) {
                this.radialProgress.setProgress((float)array[1], true);
            }
        }
    }
    
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        if (visibility == 8) {
            NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.fileDidLoad);
            NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.fileDidFailedLoad);
            NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.FileLoadProgressChanged);
        }
    }
    
    public void show(final int accountNum, final TLRPC.TL_help_appUpdate appUpdate) {
        this.pressCount = 0;
        this.appUpdate = appUpdate;
        this.accountNum = accountNum;
        final TLRPC.Document document = appUpdate.document;
        if (document instanceof TLRPC.TL_document) {
            this.fileName = FileLoader.getAttachFileName(document);
        }
        if (this.getVisibility() != 0) {
            this.setVisibility(0);
        }
        final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)appUpdate.text);
        MessageObject.addEntitiesToText((CharSequence)text, appUpdate.entities, false, 0, false, false, false);
        this.textView.setText((CharSequence)text);
        if (appUpdate.document instanceof TLRPC.TL_document) {
            final TextView acceptTextView = this.acceptTextView;
            final StringBuilder sb = new StringBuilder();
            sb.append(LocaleController.getString("Update", 2131560949).toUpperCase());
            sb.append(String.format(Locale.US, " (%1$s)", AndroidUtilities.formatFileSize(appUpdate.document.size)));
            acceptTextView.setText((CharSequence)sb.toString());
        }
        else {
            this.acceptTextView.setText((CharSequence)LocaleController.getString("Update", 2131560949).toUpperCase());
        }
        NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.fileDidLoad);
        NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.fileDidFailedLoad);
        NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.FileLoadProgressChanged);
    }
}

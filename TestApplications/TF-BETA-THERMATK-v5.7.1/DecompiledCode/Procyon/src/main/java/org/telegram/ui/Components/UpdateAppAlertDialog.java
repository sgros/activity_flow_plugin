// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.ViewGroup$LayoutParams;
import android.os.Bundle;
import org.telegram.messenger.browser.Browser;
import android.content.DialogInterface;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.graphics.Canvas;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.FileLoader;
import android.content.Context;
import android.widget.FrameLayout;
import android.animation.AnimatorSet;
import android.app.Activity;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.AlertDialog;

public class UpdateAppAlertDialog extends AlertDialog implements NotificationCenterDelegate
{
    private int accountNum;
    private TLRPC.TL_help_appUpdate appUpdate;
    private String fileName;
    private Activity parentActivity;
    private AnimatorSet progressAnimation;
    private RadialProgress radialProgress;
    private FrameLayout radialProgressView;
    
    public UpdateAppAlertDialog(final Activity parentActivity, final TLRPC.TL_help_appUpdate appUpdate, final int accountNum) {
        super((Context)parentActivity, 0);
        this.appUpdate = appUpdate;
        this.accountNum = accountNum;
        final TLRPC.Document document = appUpdate.document;
        if (document instanceof TLRPC.TL_document) {
            this.fileName = FileLoader.getAttachFileName(document);
        }
        this.parentActivity = parentActivity;
        this.setTopImage(2131165892, Theme.getColor("dialogTopBackground"));
        this.setTopHeight(175);
        this.setMessage(this.appUpdate.text);
        final TLRPC.Document document2 = this.appUpdate.document;
        if (document2 instanceof TLRPC.TL_document) {
            this.setSecondTitle(AndroidUtilities.formatFileSize(document2.size));
        }
        this.setDismissDialogByButtons(false);
        this.setTitle(LocaleController.getString("UpdateTelegram", 2131560955));
        this.setPositiveButton(LocaleController.getString("UpdateNow", 2131560954), (DialogInterface$OnClickListener)new _$$Lambda$UpdateAppAlertDialog$voSGHXhYuACsnb2_x2STYrPYmkw(this));
        this.setNeutralButton(LocaleController.getString("Later", 2131559743), (DialogInterface$OnClickListener)new _$$Lambda$UpdateAppAlertDialog$egnS6_js0sES87wcIflPDbL7olc(this));
        (this.radialProgressView = new FrameLayout(this.parentActivity) {
            protected void onDraw(final Canvas canvas) {
                UpdateAppAlertDialog.this.radialProgress.draw(canvas);
            }
            
            protected void onLayout(final boolean b, int n, int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                final int dp = AndroidUtilities.dp(24.0f);
                n = (n3 - n - dp) / 2;
                n2 = (n4 - n2 - dp) / 2 + AndroidUtilities.dp(2.0f);
                UpdateAppAlertDialog.this.radialProgress.setProgressRect(n, n2, n + dp, dp + n2);
            }
        }).setWillNotDraw(false);
        this.radialProgressView.setAlpha(0.0f);
        this.radialProgressView.setScaleX(0.1f);
        this.radialProgressView.setScaleY(0.1f);
        this.radialProgressView.setVisibility(4);
        (this.radialProgress = new RadialProgress((View)this.radialProgressView)).setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.radialProgress.setBackground(null, true, false);
        this.radialProgress.setProgressColor(Theme.getColor("dialogButton"));
    }
    
    private void showProgress(final boolean b) {
        final AnimatorSet progressAnimation = this.progressAnimation;
        if (progressAnimation != null) {
            progressAnimation.cancel();
        }
        this.progressAnimation = new AnimatorSet();
        final View viewWithTag = super.buttonsLayout.findViewWithTag((Object)(-1));
        if (b) {
            this.radialProgressView.setVisibility(0);
            viewWithTag.setEnabled(false);
            this.progressAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)viewWithTag, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)viewWithTag, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)viewWithTag, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "alpha", new float[] { 1.0f }) });
        }
        else {
            viewWithTag.setVisibility(0);
            viewWithTag.setEnabled(true);
            this.progressAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.radialProgressView, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)viewWithTag, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)viewWithTag, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)viewWithTag, "alpha", new float[] { 1.0f }) });
        }
        this.progressAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator obj) {
                if (UpdateAppAlertDialog.this.progressAnimation != null && UpdateAppAlertDialog.this.progressAnimation.equals(obj)) {
                    UpdateAppAlertDialog.this.progressAnimation = null;
                }
            }
            
            public void onAnimationEnd(final Animator obj) {
                if (UpdateAppAlertDialog.this.progressAnimation != null && UpdateAppAlertDialog.this.progressAnimation.equals(obj)) {
                    if (!b) {
                        UpdateAppAlertDialog.this.radialProgressView.setVisibility(4);
                    }
                    else {
                        viewWithTag.setVisibility(4);
                    }
                }
            }
        });
        this.progressAnimation.setDuration(150L);
        this.progressAnimation.start();
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.fileDidLoad) {
            final String anObject = (String)array[0];
            final String fileName = this.fileName;
            if (fileName != null && fileName.equals(anObject)) {
                this.showProgress(false);
                BlockingUpdateView.openApkInstall(this.parentActivity, this.appUpdate.document);
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
    
    @Override
    public void dismiss() {
        super.dismiss();
        NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.fileDidLoad);
        NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.fileDidFailedLoad);
        NotificationCenter.getInstance(this.accountNum).removeObserver(this, NotificationCenter.FileLoadProgressChanged);
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.fileDidLoad);
        NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.fileDidFailedLoad);
        NotificationCenter.getInstance(this.accountNum).addObserver(this, NotificationCenter.FileLoadProgressChanged);
        super.buttonsLayout.addView((View)this.radialProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f));
    }
}

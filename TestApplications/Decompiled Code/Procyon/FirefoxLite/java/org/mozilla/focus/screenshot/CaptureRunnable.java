// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot;

import org.mozilla.focus.Inject;
import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import android.text.TextUtils;
import android.graphics.Bitmap;
import android.widget.Toast;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.fragment.ScreenCaptureDialogFragment;
import android.content.Context;
import android.view.View;
import java.lang.ref.WeakReference;
import org.mozilla.focus.fragment.BrowserFragment;

public class CaptureRunnable extends ScreenshotCaptureTask implements Runnable, ScreenshotCallback
{
    final WeakReference<BrowserFragment> refBrowserFragment;
    final WeakReference<View> refContainerView;
    final WeakReference<Context> refContext;
    final WeakReference<ScreenCaptureDialogFragment> refScreenCaptureDialogFragment;
    
    public CaptureRunnable(final Context referent, final BrowserFragment referent2, final ScreenCaptureDialogFragment referent3, final View referent4) {
        super(referent);
        this.refContext = new WeakReference<Context>(referent);
        this.refBrowserFragment = new WeakReference<BrowserFragment>(referent2);
        this.refScreenCaptureDialogFragment = new WeakReference<ScreenCaptureDialogFragment>(referent3);
        this.refContainerView = new WeakReference<View>(referent4);
    }
    
    private void promptScreenshotResult(final boolean b) {
        final Context context = this.refContext.get();
        if (context == null) {
            return;
        }
        if (this.refBrowserFragment != null) {
            final BrowserFragment browserFragment = this.refBrowserFragment.get();
            if (browserFragment != null && browserFragment.getCaptureStateListener() != null) {
                browserFragment.getCaptureStateListener().onPromptScreenshotResult();
            }
            if (browserFragment != null && b && !Settings.getInstance(context).getEventHistory().contains("show_my_shot_on_boarding_dialog")) {
                browserFragment.showMyShotOnBoarding();
                return;
            }
        }
        int n;
        if (b) {
            n = 2131755389;
        }
        else {
            n = 2131755376;
        }
        Toast.makeText(context, n, 0).show();
    }
    
    @Override
    public void onCaptureComplete(final String s, final String s2, final Bitmap bitmap) {
        if (this.refContext.get() == null) {
            return;
        }
        this.execute(new Object[] { s, s2, bitmap });
    }
    
    protected void onPostExecute(final String s) {
        final ScreenCaptureDialogFragment screenCaptureDialogFragment = this.refScreenCaptureDialogFragment.get();
        if (screenCaptureDialogFragment == null) {
            this.cancel(true);
            return;
        }
        final boolean b = TextUtils.isEmpty((CharSequence)s) ^ true;
        if (b) {
            Settings.getInstance(this.refContext.get()).setHasUnreadMyShot(true);
        }
        screenCaptureDialogFragment.getDialog().setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                CaptureRunnable.this.promptScreenshotResult(b);
            }
        });
        if (TextUtils.isEmpty((CharSequence)s)) {
            screenCaptureDialogFragment.dismiss();
        }
        else {
            screenCaptureDialogFragment.dismiss(Inject.isUnderEspressoTest() ^ true);
        }
    }
    
    @Override
    public void run() {
        final BrowserFragment browserFragment = this.refBrowserFragment.get();
        if (browserFragment == null) {
            return;
        }
        if (!browserFragment.capturePage((BrowserFragment.ScreenshotCallback)this)) {
            final ScreenCaptureDialogFragment screenCaptureDialogFragment = this.refScreenCaptureDialogFragment.get();
            if (screenCaptureDialogFragment != null) {
                screenCaptureDialogFragment.dismiss();
            }
            this.promptScreenshotResult(false);
        }
    }
    
    public interface CaptureStateListener
    {
        void onPromptScreenshotResult();
    }
}

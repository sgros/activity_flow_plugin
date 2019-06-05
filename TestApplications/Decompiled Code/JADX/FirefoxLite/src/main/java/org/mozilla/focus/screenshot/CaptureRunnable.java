package org.mozilla.focus.screenshot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import org.mozilla.focus.Inject;
import org.mozilla.focus.fragment.BrowserFragment;
import org.mozilla.focus.fragment.BrowserFragment.ScreenshotCallback;
import org.mozilla.focus.fragment.ScreenCaptureDialogFragment;
import org.mozilla.focus.utils.Settings;
import org.mozilla.rocket.C0769R;

public class CaptureRunnable extends ScreenshotCaptureTask implements Runnable, ScreenshotCallback {
    final WeakReference<BrowserFragment> refBrowserFragment;
    final WeakReference<View> refContainerView;
    final WeakReference<Context> refContext;
    final WeakReference<ScreenCaptureDialogFragment> refScreenCaptureDialogFragment;

    public interface CaptureStateListener {
        void onPromptScreenshotResult();
    }

    public CaptureRunnable(Context context, BrowserFragment browserFragment, ScreenCaptureDialogFragment screenCaptureDialogFragment, View view) {
        super(context);
        this.refContext = new WeakReference(context);
        this.refBrowserFragment = new WeakReference(browserFragment);
        this.refScreenCaptureDialogFragment = new WeakReference(screenCaptureDialogFragment);
        this.refContainerView = new WeakReference(view);
    }

    public void run() {
        BrowserFragment browserFragment = (BrowserFragment) this.refBrowserFragment.get();
        if (!(browserFragment == null || browserFragment.capturePage(this))) {
            ScreenCaptureDialogFragment screenCaptureDialogFragment = (ScreenCaptureDialogFragment) this.refScreenCaptureDialogFragment.get();
            if (screenCaptureDialogFragment != null) {
                screenCaptureDialogFragment.dismiss();
            }
            promptScreenshotResult(false);
        }
    }

    public void onCaptureComplete(String str, String str2, Bitmap bitmap) {
        if (((Context) this.refContext.get()) != null) {
            execute(new Object[]{str, str2, bitmap});
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPostExecute(String str) {
        ScreenCaptureDialogFragment screenCaptureDialogFragment = (ScreenCaptureDialogFragment) this.refScreenCaptureDialogFragment.get();
        if (screenCaptureDialogFragment == null) {
            cancel(true);
            return;
        }
        final int isEmpty = TextUtils.isEmpty(str) ^ 1;
        if (isEmpty != 0) {
            Settings.getInstance((Context) this.refContext.get()).setHasUnreadMyShot(true);
        }
        screenCaptureDialogFragment.getDialog().setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                CaptureRunnable.this.promptScreenshotResult(isEmpty);
            }
        });
        if (TextUtils.isEmpty(str)) {
            screenCaptureDialogFragment.dismiss();
        } else {
            screenCaptureDialogFragment.dismiss(Inject.isUnderEspressoTest() ^ 1);
        }
    }

    private void promptScreenshotResult(boolean z) {
        Context context = (Context) this.refContext.get();
        if (context != null) {
            if (this.refBrowserFragment != null) {
                BrowserFragment browserFragment = (BrowserFragment) this.refBrowserFragment.get();
                if (!(browserFragment == null || browserFragment.getCaptureStateListener() == null)) {
                    browserFragment.getCaptureStateListener().onPromptScreenshotResult();
                }
                if (!(browserFragment == null || !z || Settings.getInstance(context).getEventHistory().contains("show_my_shot_on_boarding_dialog"))) {
                    browserFragment.showMyShotOnBoarding();
                    return;
                }
            }
            Toast.makeText(context, z ? C0769R.string.screenshot_saved : C0769R.string.screenshot_failed, 0).show();
        }
    }
}

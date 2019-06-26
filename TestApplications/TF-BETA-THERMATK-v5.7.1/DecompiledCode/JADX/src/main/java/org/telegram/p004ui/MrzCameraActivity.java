package org.telegram.p004ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MrzRecognizer;
import org.telegram.messenger.MrzRecognizer.Result;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.camera.CameraView;
import org.telegram.messenger.camera.CameraView.CameraViewDelegate;
import org.telegram.messenger.camera.Size;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Components.CubicBezierInterpolator;
import org.telegram.p004ui.Components.LayoutHelper;

@TargetApi(18)
/* renamed from: org.telegram.ui.MrzCameraActivity */
public class MrzCameraActivity extends BaseFragment implements PreviewCallback {
    private HandlerThread backgroundHandlerThread = new HandlerThread("MrzCamera");
    private CameraView cameraView;
    private MrzCameraActivityDelegate delegate;
    private TextView descriptionText;
    private Handler handler;
    private boolean recognized;
    private TextView recognizedMrzView;
    private TextView titleTextView;

    /* renamed from: org.telegram.ui.MrzCameraActivity$3 */
    class C30933 implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }

        C30933() {
        }
    }

    /* renamed from: org.telegram.ui.MrzCameraActivity$5 */
    class C30945 implements Runnable {
        C30945() {
        }

        public void run() {
            if (MrzCameraActivity.this.cameraView != null && !MrzCameraActivity.this.recognized && MrzCameraActivity.this.cameraView.getCameraSession() != null) {
                MrzCameraActivity.this.cameraView.getCameraSession().setOneShotPreviewCallback(MrzCameraActivity.this);
                AndroidUtilities.runOnUIThread(this, 500);
            }
        }
    }

    /* renamed from: org.telegram.ui.MrzCameraActivity$MrzCameraActivityDelegate */
    public interface MrzCameraActivityDelegate {
        void didFindMrzInfo(Result result);
    }

    /* renamed from: org.telegram.ui.MrzCameraActivity$1 */
    class C42391 extends ActionBarMenuOnItemClick {
        C42391() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                MrzCameraActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.MrzCameraActivity$4 */
    class C42404 implements CameraViewDelegate {
        C42404() {
        }

        public void onCameraCreated(Camera camera) {
            Parameters parameters = camera.getParameters();
            float exposureCompensationStep = parameters.getExposureCompensationStep();
            parameters.setExposureCompensation(((float) parameters.getMaxExposureCompensation()) * exposureCompensationStep <= 2.0f ? parameters.getMaxExposureCompensation() : Math.round(2.0f / exposureCompensationStep));
            camera.setParameters(parameters);
        }

        public void onCameraInit() {
            MrzCameraActivity.this.startRecognizing();
        }
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        destroy(false, null);
        getParentActivity().setRequestedOrientation(-1);
    }

    public View createView(Context context) {
        getParentActivity().setRequestedOrientation(1);
        C2190ActionBar c2190ActionBar = this.actionBar;
        String str = Theme.key_windowBackgroundWhite;
        c2190ActionBar.setBackgroundColor(Theme.getColor(str));
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setItemsColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarWhiteSelector), false);
        this.actionBar.setCastShadows(false);
        if (!AndroidUtilities.isTablet()) {
            this.actionBar.showActionModeTop();
        }
        this.actionBar.setActionBarMenuOnItemClick(new C42391());
        this.fragmentView = new ViewGroup(context) {
            /* Access modifiers changed, original: protected */
            public void onMeasure(int i, int i2) {
                i = MeasureSpec.getSize(i);
                i2 = MeasureSpec.getSize(i2);
                float f = (float) i;
                MrzCameraActivity.this.cameraView.measure(MeasureSpec.makeMeasureSpec(i, 1073741824), MeasureSpec.makeMeasureSpec((int) (0.704f * f), 1073741824));
                MrzCameraActivity.this.titleTextView.measure(MeasureSpec.makeMeasureSpec(i, 1073741824), MeasureSpec.makeMeasureSpec(i2, 0));
                MrzCameraActivity.this.descriptionText.measure(MeasureSpec.makeMeasureSpec((int) (f * 0.9f), 1073741824), MeasureSpec.makeMeasureSpec(i2, 0));
                setMeasuredDimension(i, i2);
            }

            /* Access modifiers changed, original: protected */
            public void onLayout(boolean z, int i, int i2, int i3, int i4) {
                i3 -= i;
                i4 -= i2;
                MrzCameraActivity.this.cameraView.layout(0, 0, MrzCameraActivity.this.cameraView.getMeasuredWidth(), MrzCameraActivity.this.cameraView.getMeasuredHeight() + 0);
                MrzCameraActivity.this.recognizedMrzView.setTextSize(0, (float) (MrzCameraActivity.this.cameraView.getMeasuredHeight() / 22));
                MrzCameraActivity.this.recognizedMrzView.setPadding(0, 0, 0, MrzCameraActivity.this.cameraView.getMeasuredHeight() / 15);
                float f = (float) i4;
                i = (int) (0.65f * f);
                MrzCameraActivity.this.titleTextView.layout(0, i, MrzCameraActivity.this.titleTextView.getMeasuredWidth(), MrzCameraActivity.this.titleTextView.getMeasuredHeight() + i);
                int i5 = (int) (f * 0.74f);
                i = (int) (((float) i3) * 0.05f);
                MrzCameraActivity.this.descriptionText.layout(i, i5, MrzCameraActivity.this.descriptionText.getMeasuredWidth() + i, MrzCameraActivity.this.descriptionText.getMeasuredHeight() + i5);
            }
        };
        this.fragmentView.setBackgroundColor(Theme.getColor(str));
        ViewGroup viewGroup = (ViewGroup) this.fragmentView;
        viewGroup.setOnTouchListener(new C30933());
        this.cameraView = new CameraView(context, false);
        this.cameraView.setDelegate(new C42404());
        viewGroup.addView(this.cameraView, LayoutHelper.createFrame(-1, -1.0f));
        this.titleTextView = new TextView(context);
        this.titleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.titleTextView.setGravity(1);
        this.titleTextView.setTextSize(1, 24.0f);
        this.titleTextView.setText(LocaleController.getString("PassportScanPassport", C1067R.string.PassportScanPassport));
        viewGroup.addView(this.titleTextView);
        this.descriptionText = new TextView(context);
        this.descriptionText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText6));
        this.descriptionText.setGravity(1);
        this.descriptionText.setTextSize(1, 16.0f);
        this.descriptionText.setText(LocaleController.getString("PassportScanPassportInfo", C1067R.string.PassportScanPassportInfo));
        viewGroup.addView(this.descriptionText);
        this.recognizedMrzView = new TextView(context);
        this.recognizedMrzView.setTypeface(Typeface.MONOSPACE);
        this.recognizedMrzView.setTextColor(-1);
        this.recognizedMrzView.setGravity(81);
        this.recognizedMrzView.setBackgroundColor(Integer.MIN_VALUE);
        this.recognizedMrzView.setAlpha(0.0f);
        this.cameraView.addView(this.recognizedMrzView);
        this.fragmentView.setKeepScreenOn(true);
        return this.fragmentView;
    }

    public void setDelegate(MrzCameraActivityDelegate mrzCameraActivityDelegate) {
        this.delegate = mrzCameraActivityDelegate;
    }

    public void destroy(boolean z, Runnable runnable) {
        this.cameraView.destroy(z, runnable);
        this.cameraView = null;
        this.backgroundHandlerThread.quitSafely();
    }

    public void cancel() {
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.recordStopped, Integer.valueOf(0));
    }

    public void hideCamera(boolean z) {
        destroy(z, null);
    }

    private void startRecognizing() {
        this.backgroundHandlerThread.start();
        this.handler = new Handler(this.backgroundHandlerThread.getLooper());
        AndroidUtilities.runOnUIThread(new C30945());
    }

    public void onPreviewFrame(final byte[] bArr, final Camera camera) {
        this.handler.post(new Runnable() {
            public void run() {
                try {
                    Size previewSize = MrzCameraActivity.this.cameraView.getPreviewSize();
                    final Result recognize = MrzRecognizer.recognize(bArr, previewSize.getWidth(), previewSize.getHeight(), MrzCameraActivity.this.cameraView.getCameraSession().getDisplayOrientation());
                    if (recognize != null && !TextUtils.isEmpty(recognize.firstName) && !TextUtils.isEmpty(recognize.lastName) && !TextUtils.isEmpty(recognize.number) && recognize.birthDay != 0) {
                        if ((recognize.expiryDay != 0 || recognize.doesNotExpire) && recognize.gender != 0) {
                            MrzCameraActivity.this.recognized = true;
                            camera.stopPreview();
                            AndroidUtilities.runOnUIThread(new Runnable() {

                                /* renamed from: org.telegram.ui.MrzCameraActivity$6$1$1 */
                                class C30951 implements Runnable {
                                    C30951() {
                                    }

                                    public void run() {
                                        MrzCameraActivity.this.finishFragment();
                                    }
                                }

                                public void run() {
                                    MrzCameraActivity.this.recognizedMrzView.setText(recognize.rawMRZ);
                                    MrzCameraActivity.this.recognizedMrzView.animate().setDuration(200).alpha(1.0f).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                                    if (MrzCameraActivity.this.delegate != null) {
                                        MrzCameraActivity.this.delegate.didFindMrzInfo(recognize);
                                    }
                                    AndroidUtilities.runOnUIThread(new C30951(), 1200);
                                }
                            });
                        }
                    }
                } catch (Exception unused) {
                }
            }
        });
    }

    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[]{new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite), new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite), new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText2), new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarWhiteSelector), new ThemeDescription(this.titleTextView, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText), new ThemeDescription(this.descriptionText, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6)};
    }
}

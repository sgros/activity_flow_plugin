// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.messenger.camera.Size;
import android.animation.TimeInterpolator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import android.text.TextUtils;
import org.telegram.messenger.MrzRecognizer;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.graphics.Typeface;
import org.telegram.messenger.LocaleController;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.hardware.Camera$Parameters;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.view.View$MeasureSpec;
import android.view.ViewGroup;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import android.view.View;
import android.content.Context;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.AndroidUtilities;
import android.os.Handler;
import android.widget.TextView;
import org.telegram.messenger.camera.CameraView;
import android.os.HandlerThread;
import android.annotation.TargetApi;
import android.hardware.Camera$PreviewCallback;
import org.telegram.ui.ActionBar.BaseFragment;

@TargetApi(18)
public class MrzCameraActivity extends BaseFragment implements Camera$PreviewCallback
{
    private HandlerThread backgroundHandlerThread;
    private CameraView cameraView;
    private MrzCameraActivityDelegate delegate;
    private TextView descriptionText;
    private Handler handler;
    private boolean recognized;
    private TextView recognizedMrzView;
    private TextView titleTextView;
    
    public MrzCameraActivity() {
        this.backgroundHandlerThread = new HandlerThread("MrzCamera");
    }
    
    private void startRecognizing() {
        this.backgroundHandlerThread.start();
        this.handler = new Handler(this.backgroundHandlerThread.getLooper());
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (MrzCameraActivity.this.cameraView != null && !MrzCameraActivity.this.recognized && MrzCameraActivity.this.cameraView.getCameraSession() != null) {
                    MrzCameraActivity.this.cameraView.getCameraSession().setOneShotPreviewCallback((Camera$PreviewCallback)MrzCameraActivity.this);
                    AndroidUtilities.runOnUIThread(this, 500L);
                }
            }
        });
    }
    
    public void cancel() {
        NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.recordStopped, 0);
    }
    
    @Override
    public View createView(final Context context) {
        this.getParentActivity().setRequestedOrientation(1);
        super.actionBar.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setItemsColor(Theme.getColor("windowBackgroundWhiteGrayText2"), false);
        super.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarWhiteSelector"), false);
        super.actionBar.setCastShadows(false);
        if (!AndroidUtilities.isTablet()) {
            super.actionBar.showActionModeTop();
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    MrzCameraActivity.this.finishFragment();
                }
            }
        });
        (super.fragmentView = (View)new ViewGroup(context) {
            protected void onLayout(final boolean b, int n, int n2, final int n3, final int n4) {
                MrzCameraActivity.this.cameraView.layout(0, 0, MrzCameraActivity.this.cameraView.getMeasuredWidth(), MrzCameraActivity.this.cameraView.getMeasuredHeight() + 0);
                MrzCameraActivity.this.recognizedMrzView.setTextSize(0, (float)(MrzCameraActivity.this.cameraView.getMeasuredHeight() / 22));
                MrzCameraActivity.this.recognizedMrzView.setPadding(0, 0, 0, MrzCameraActivity.this.cameraView.getMeasuredHeight() / 15);
                final float n5 = (float)(n4 - n2);
                n2 = (int)(0.65f * n5);
                MrzCameraActivity.this.titleTextView.layout(0, n2, MrzCameraActivity.this.titleTextView.getMeasuredWidth(), MrzCameraActivity.this.titleTextView.getMeasuredHeight() + n2);
                n2 = (int)(n5 * 0.74f);
                n = (int)((n3 - n) * 0.05f);
                MrzCameraActivity.this.descriptionText.layout(n, n2, MrzCameraActivity.this.descriptionText.getMeasuredWidth() + n, MrzCameraActivity.this.descriptionText.getMeasuredHeight() + n2);
            }
            
            protected void onMeasure(int size, int size2) {
                size = View$MeasureSpec.getSize(size);
                size2 = View$MeasureSpec.getSize(size2);
                final CameraView access$000 = MrzCameraActivity.this.cameraView;
                final int measureSpec = View$MeasureSpec.makeMeasureSpec(size, 1073741824);
                final float n = (float)size;
                access$000.measure(measureSpec, View$MeasureSpec.makeMeasureSpec((int)(0.704f * n), 1073741824));
                MrzCameraActivity.this.titleTextView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 0));
                MrzCameraActivity.this.descriptionText.measure(View$MeasureSpec.makeMeasureSpec((int)(n * 0.9f), 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 0));
                this.setMeasuredDimension(size, size2);
            }
        }).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        final ViewGroup viewGroup = (ViewGroup)super.fragmentView;
        viewGroup.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return true;
            }
        });
        (this.cameraView = new CameraView(context, false)).setDelegate((CameraView.CameraViewDelegate)new CameraView.CameraViewDelegate() {
            @Override
            public void onCameraCreated(final Camera camera) {
                final Camera$Parameters parameters = camera.getParameters();
                final float exposureCompensationStep = parameters.getExposureCompensationStep();
                int exposureCompensation;
                if (parameters.getMaxExposureCompensation() * exposureCompensationStep <= 2.0f) {
                    exposureCompensation = parameters.getMaxExposureCompensation();
                }
                else {
                    exposureCompensation = Math.round(2.0f / exposureCompensationStep);
                }
                parameters.setExposureCompensation(exposureCompensation);
                camera.setParameters(parameters);
            }
            
            @Override
            public void onCameraInit() {
                MrzCameraActivity.this.startRecognizing();
            }
        });
        viewGroup.addView((View)this.cameraView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.titleTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.titleTextView.setGravity(1);
        this.titleTextView.setTextSize(1, 24.0f);
        this.titleTextView.setText((CharSequence)LocaleController.getString("PassportScanPassport", 2131560322));
        viewGroup.addView((View)this.titleTextView);
        (this.descriptionText = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
        this.descriptionText.setGravity(1);
        this.descriptionText.setTextSize(1, 16.0f);
        this.descriptionText.setText((CharSequence)LocaleController.getString("PassportScanPassportInfo", 2131560323));
        viewGroup.addView((View)this.descriptionText);
        (this.recognizedMrzView = new TextView(context)).setTypeface(Typeface.MONOSPACE);
        this.recognizedMrzView.setTextColor(-1);
        this.recognizedMrzView.setGravity(81);
        this.recognizedMrzView.setBackgroundColor(Integer.MIN_VALUE);
        this.recognizedMrzView.setAlpha(0.0f);
        this.cameraView.addView((View)this.recognizedMrzView);
        super.fragmentView.setKeepScreenOn(true);
        return super.fragmentView;
    }
    
    public void destroy(final boolean b, final Runnable runnable) {
        this.cameraView.destroy(b, runnable);
        this.cameraView = null;
        this.backgroundHandlerThread.quitSafely();
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarWhiteSelector"), new ThemeDescription((View)this.titleTextView, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.descriptionText, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6") };
    }
    
    public void hideCamera(final boolean b) {
        this.destroy(b, null);
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        this.destroy(false, null);
        this.getParentActivity().setRequestedOrientation(-1);
    }
    
    public void onPreviewFrame(final byte[] array, final Camera camera) {
        this.handler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    final Size previewSize = MrzCameraActivity.this.cameraView.getPreviewSize();
                    final MrzRecognizer.Result recognize = MrzRecognizer.recognize(array, previewSize.getWidth(), previewSize.getHeight(), MrzCameraActivity.this.cameraView.getCameraSession().getDisplayOrientation());
                    if (recognize != null && !TextUtils.isEmpty((CharSequence)recognize.firstName) && !TextUtils.isEmpty((CharSequence)recognize.lastName) && !TextUtils.isEmpty((CharSequence)recognize.number) && recognize.birthDay != 0 && (recognize.expiryDay != 0 || recognize.doesNotExpire) && recognize.gender != 0) {
                        MrzCameraActivity.this.recognized = true;
                        camera.stopPreview();
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                MrzCameraActivity.this.recognizedMrzView.setText((CharSequence)recognize.rawMRZ);
                                MrzCameraActivity.this.recognizedMrzView.animate().setDuration(200L).alpha(1.0f).setInterpolator((TimeInterpolator)CubicBezierInterpolator.DEFAULT).start();
                                if (MrzCameraActivity.this.delegate != null) {
                                    MrzCameraActivity.this.delegate.didFindMrzInfo(recognize);
                                }
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MrzCameraActivity.this.finishFragment();
                                    }
                                }, 1200L);
                            }
                        });
                    }
                }
                catch (Exception ex) {}
            }
        });
    }
    
    public void setDelegate(final MrzCameraActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public interface MrzCameraActivityDelegate
    {
        void didFindMrzInfo(final MrzRecognizer.Result p0);
    }
}

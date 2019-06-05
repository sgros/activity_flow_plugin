package org.mozilla.rocket.nightmode;

import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.rocket.nightmode.AdjustBrightnessDialog.Constants;

/* compiled from: AdjustBrightnessDialog.kt */
public final class AdjustBrightnessDialog$mSeekListener$1 implements OnSeekBarChangeListener {
    final /* synthetic */ AdjustBrightnessDialog this$0;

    public void onStartTrackingTouch(SeekBar seekBar) {
        Intrinsics.checkParameterIsNotNull(seekBar, "seekBar");
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        Intrinsics.checkParameterIsNotNull(seekBar, "seekBar");
    }

    AdjustBrightnessDialog$mSeekListener$1(AdjustBrightnessDialog adjustBrightnessDialog) {
        this.this$0 = adjustBrightnessDialog;
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        Intrinsics.checkParameterIsNotNull(seekBar, "seekBar");
        if (z) {
            Window window = this.this$0.getWindow();
            Intrinsics.checkExpressionValueIsNotNull(window, "window");
            LayoutParams attributes = window.getAttributes();
            float progressToValue = Constants.INSTANCE.progressToValue(i);
            if (((double) progressToValue) < 0.01d) {
                progressToValue = 0.01f;
            }
            attributes.screenBrightness = progressToValue;
            Window window2 = this.this$0.getWindow();
            Intrinsics.checkExpressionValueIsNotNull(window2, "window");
            window2.setAttributes(attributes);
        }
    }
}

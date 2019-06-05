// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.nightmode;

import android.content.Intent;
import android.view.WindowManager$LayoutParams;
import android.view.Window;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.Settings;
import android.content.Context;
import org.mozilla.focus.utils.ViewUtils;
import android.view.View$OnClickListener;
import android.widget.SeekBar$OnSeekBarChangeListener;
import kotlin.jvm.internal.Intrinsics;
import android.os.Bundle;
import android.widget.SeekBar;
import org.mozilla.focus.activity.BaseActivity;

public final class AdjustBrightnessDialog extends BaseActivity
{
    private final AdjustBrightnessDialog$mSeekListener.AdjustBrightnessDialog$mSeekListener$1 mSeekListener;
    private SeekBar seekBar;
    
    public AdjustBrightnessDialog() {
        this.mSeekListener = new AdjustBrightnessDialog$mSeekListener.AdjustBrightnessDialog$mSeekListener$1(this);
    }
    
    @Override
    public void applyLocale() {
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131492898);
        final SeekBar viewById = this.findViewById(2131296330);
        Intrinsics.checkExpressionValueIsNotNull(viewById, "findViewById(R.id.brightness_slider)");
        this.seekBar = viewById;
        final SeekBar seekBar = this.seekBar;
        if (seekBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("seekBar");
        }
        seekBar.setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)this.mSeekListener);
        this.findViewById(2131296329).setOnClickListener((View$OnClickListener)new AdjustBrightnessDialog$onCreate.AdjustBrightnessDialog$onCreate$1(this));
        ViewUtils.updateStatusBarStyle(false, this.getWindow());
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        final boolean equal = Intrinsics.areEqual(this.getIntent().getStringExtra("extra_source"), "setting");
        final Window window = this.getWindow();
        Intrinsics.checkExpressionValueIsNotNull(window, "window");
        final WindowManager$LayoutParams attributes = window.getAttributes();
        final Settings instance = Settings.getInstance((Context)this);
        Intrinsics.checkExpressionValueIsNotNull(instance, "Settings.getInstance(this)");
        instance.setNightModeBrightnessValue(attributes.screenBrightness);
        TelemetryWrapper.nightModeBrightnessChangeTo(Constants.INSTANCE.valueToProgress(attributes.screenBrightness), equal);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        final Settings instance = Settings.getInstance((Context)this);
        Intrinsics.checkExpressionValueIsNotNull(instance, "Settings.getInstance(this)");
        final int valueToProgress = Constants.INSTANCE.valueToProgress(instance.getNightModeBrightnessValue());
        final SeekBar seekBar = this.seekBar;
        if (seekBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("seekBar");
        }
        seekBar.setProgress(valueToProgress);
    }
    
    public static final class Constants
    {
        public static final Constants INSTANCE;
        
        static {
            INSTANCE = new Constants();
        }
        
        private Constants() {
        }
        
        public final float progressToValue(final int n) {
            return (float)(n * 0.5 / 100.0f);
        }
        
        public final int valueToProgress(final float n) {
            return (int)(n * 100.0f / 0.5);
        }
    }
    
    public static final class Intents
    {
        public static final Intents INSTANCE;
        
        static {
            INSTANCE = new Intents();
        }
        
        private Intents() {
        }
        
        private final Intent getStartIntent(final Context context, final String s) {
            final Intent intent = new Intent(context, (Class)AdjustBrightnessDialog.class);
            final int hashCode = s.hashCode();
            if (hashCode != 3347807) {
                if (hashCode == 1985941072) {
                    if (s.equals("setting")) {
                        intent.putExtra("extra_source", "setting");
                    }
                }
            }
            else if (s.equals("menu")) {
                intent.putExtra("extra_source", "menu");
            }
            return intent;
        }
        
        public final Intent getStartIntentFromMenu(final Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return this.getStartIntent(context, "menu");
        }
        
        public final Intent getStartIntentFromSetting(final Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return this.getStartIntent(context, "setting");
        }
    }
}

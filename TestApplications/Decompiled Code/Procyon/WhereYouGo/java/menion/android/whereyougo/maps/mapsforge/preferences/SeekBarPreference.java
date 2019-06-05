// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.preferences;

import android.widget.LinearLayout;
import android.view.View;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.SeekBar;
import android.content.SharedPreferences$Editor;
import android.widget.TextView;
import android.widget.SeekBar$OnSeekBarChangeListener;
import android.preference.DialogPreference;

abstract class SeekBarPreference extends DialogPreference implements SeekBar$OnSeekBarChangeListener
{
    private TextView currentValueTextView;
    private SharedPreferences$Editor editor;
    int increment;
    int max;
    String messageText;
    private SeekBar preferenceSeekBar;
    final SharedPreferences preferencesDefault;
    int seekBarCurrentValue;
    
    SeekBarPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.increment = 1;
        this.preferencesDefault = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    abstract String getCurrentValueText(final int p0);
    
    public void onClick(final DialogInterface dialogInterface, final int n) {
        if (n == -1 && this.seekBarCurrentValue != this.preferenceSeekBar.getProgress()) {
            this.seekBarCurrentValue = this.preferenceSeekBar.getProgress();
            (this.editor = this.preferencesDefault.edit()).putInt(this.getKey(), this.seekBarCurrentValue);
            this.editor.commit();
        }
    }
    
    protected View onCreateDialogView() {
        final LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setPadding(20, 10, 20, 10);
        if (this.messageText != null) {
            final TextView textView = new TextView(this.getContext());
            textView.setText((CharSequence)this.messageText);
            textView.setPadding(0, 0, 0, 20);
            linearLayout.addView((View)textView);
        }
        (this.preferenceSeekBar = new SeekBar(this.getContext())).setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)this);
        this.preferenceSeekBar.setMax(this.max);
        this.preferenceSeekBar.setProgress(Math.min(this.seekBarCurrentValue, this.max));
        this.preferenceSeekBar.setKeyProgressIncrement(this.increment);
        this.preferenceSeekBar.setPadding(0, 0, 0, 10);
        linearLayout.addView((View)this.preferenceSeekBar);
        (this.currentValueTextView = new TextView(this.getContext())).setText((CharSequence)this.getCurrentValueText(this.preferenceSeekBar.getProgress()));
        this.currentValueTextView.setGravity(1);
        linearLayout.addView((View)this.currentValueTextView);
        return (View)linearLayout;
    }
    
    public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
        if (this.currentValueTextView != null) {
            this.currentValueTextView.setText((CharSequence)this.getCurrentValueText(n));
        }
    }
    
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }
    
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }
}

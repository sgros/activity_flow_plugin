// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar$OnSeekBarChangeListener;

class SeekBarChangeListener implements SeekBar$OnSeekBarChangeListener
{
    private final TextView textView;
    
    SeekBarChangeListener(final TextView textView) {
        this.textView = textView;
    }
    
    public void onProgressChanged(final SeekBar seekBar, final int i, final boolean b) {
        this.textView.setText((CharSequence)String.valueOf(i));
    }
    
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }
    
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }
}

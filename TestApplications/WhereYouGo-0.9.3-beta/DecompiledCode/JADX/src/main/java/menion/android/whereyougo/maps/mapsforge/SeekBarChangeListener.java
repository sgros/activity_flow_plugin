package menion.android.whereyougo.maps.mapsforge;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

class SeekBarChangeListener implements OnSeekBarChangeListener {
    private final TextView textView;

    SeekBarChangeListener(TextView textView) {
        this.textView = textView;
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.textView.setText(String.valueOf(progress));
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}

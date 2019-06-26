package menion.android.whereyougo.maps.mapsforge;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

class SeekBarChangeListener implements OnSeekBarChangeListener {
   private final TextView textView;

   SeekBarChangeListener(TextView var1) {
      this.textView = var1;
   }

   public void onProgressChanged(SeekBar var1, int var2, boolean var3) {
      this.textView.setText(String.valueOf(var2));
   }

   public void onStartTrackingTouch(SeekBar var1) {
   }

   public void onStopTrackingTouch(SeekBar var1) {
   }
}

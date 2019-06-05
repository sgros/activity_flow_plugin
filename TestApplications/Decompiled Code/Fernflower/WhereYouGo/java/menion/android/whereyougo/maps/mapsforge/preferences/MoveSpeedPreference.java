package menion.android.whereyougo.maps.mapsforge.preferences;

import android.content.Context;
import android.util.AttributeSet;

public class MoveSpeedPreference extends SeekBarPreference {
   public MoveSpeedPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.messageText = this.getContext().getString(2131165469);
      this.seekBarCurrentValue = this.preferencesDefault.getInt(this.getKey(), 10);
      this.max = 30;
   }

   String getCurrentValueText(int var1) {
      return String.format(this.getContext().getString(2131165470), var1 * 10);
   }
}

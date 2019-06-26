package menion.android.whereyougo.maps.mapsforge.preferences;

import android.content.Context;
import android.util.AttributeSet;

public class CacheSizePreference extends SeekBarPreference {
   private static final double ONE_MEGABYTE = 1000000.0D;
   private static final int TILE_SIZE_IN_BYTES = 131072;

   public CacheSizePreference(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.messageText = this.getContext().getString(2131165461);
      this.seekBarCurrentValue = this.preferencesDefault.getInt(this.getKey(), 250);
      this.max = 500;
   }

   String getCurrentValueText(int var1) {
      return String.format(this.getContext().getString(2131165462), (double)(131072 * var1) / 1000000.0D);
   }
}

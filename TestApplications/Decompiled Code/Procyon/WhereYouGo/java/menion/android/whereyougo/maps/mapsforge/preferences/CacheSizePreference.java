// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.preferences;

import android.widget.SeekBar;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.content.Context;

public class CacheSizePreference extends SeekBarPreference
{
    private static final double ONE_MEGABYTE = 1000000.0;
    private static final int TILE_SIZE_IN_BYTES = 131072;
    
    public CacheSizePreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.messageText = this.getContext().getString(2131165461);
        this.seekBarCurrentValue = this.preferencesDefault.getInt(this.getKey(), 250);
        this.max = 500;
    }
    
    @Override
    String getCurrentValueText(final int n) {
        return String.format(this.getContext().getString(2131165462), 131072 * n / 1000000.0);
    }
}

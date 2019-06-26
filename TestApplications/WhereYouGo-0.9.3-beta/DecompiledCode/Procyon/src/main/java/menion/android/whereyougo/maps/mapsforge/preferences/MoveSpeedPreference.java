// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.preferences;

import android.widget.SeekBar;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.content.Context;

public class MoveSpeedPreference extends SeekBarPreference
{
    public MoveSpeedPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.messageText = this.getContext().getString(2131165469);
        this.seekBarCurrentValue = this.preferencesDefault.getInt(this.getKey(), 10);
        this.max = 30;
    }
    
    @Override
    String getCurrentValueText(final int n) {
        return String.format(this.getContext().getString(2131165470), n * 10);
    }
}

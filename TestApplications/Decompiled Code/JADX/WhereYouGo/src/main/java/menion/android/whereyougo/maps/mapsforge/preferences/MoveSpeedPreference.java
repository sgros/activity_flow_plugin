package menion.android.whereyougo.maps.mapsforge.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.SeekBar;
import menion.android.whereyougo.C0254R;

public class MoveSpeedPreference extends SeekBarPreference {
    public /* bridge */ /* synthetic */ void onClick(DialogInterface dialogInterface, int i) {
        super.onClick(dialogInterface, i);
    }

    public /* bridge */ /* synthetic */ void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        super.onProgressChanged(seekBar, i, z);
    }

    public /* bridge */ /* synthetic */ void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
    }

    public /* bridge */ /* synthetic */ void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
    }

    public MoveSpeedPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.messageText = getContext().getString(C0254R.string.preferences_move_speed_desc);
        this.seekBarCurrentValue = this.preferencesDefault.getInt(getKey(), 10);
        this.max = 30;
    }

    /* Access modifiers changed, original: 0000 */
    public String getCurrentValueText(int progress) {
        return String.format(getContext().getString(C0254R.string.preferences_move_speed_value), new Object[]{Integer.valueOf(progress * 10)});
    }
}

package menion.android.whereyougo.maps.mapsforge.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.SeekBar;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.maps.mapsforge.MapsforgeActivity;

public class CacheSizePreference extends SeekBarPreference {
    private static final double ONE_MEGABYTE = 1000000.0d;
    private static final int TILE_SIZE_IN_BYTES = 131072;

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

    public CacheSizePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.messageText = getContext().getString(C0254R.string.preferences_cache_size_desc);
        this.seekBarCurrentValue = this.preferencesDefault.getInt(getKey(), MapsforgeActivity.FILE_SYSTEM_CACHE_SIZE_DEFAULT);
        this.max = MapsforgeActivity.FILE_SYSTEM_CACHE_SIZE_MAX;
    }

    /* Access modifiers changed, original: 0000 */
    public String getCurrentValueText(int progress) {
        String format = getContext().getString(C0254R.string.preferences_cache_size_value);
        Double value = Double.valueOf(((double) (131072 * progress)) / ONE_MEGABYTE);
        return String.format(format, new Object[]{value});
    }
}

package menion.android.whereyougo.maps.mapsforge.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

abstract class SeekBarPreference extends DialogPreference implements OnSeekBarChangeListener {
    private TextView currentValueTextView;
    private Editor editor;
    int increment = 1;
    int max;
    String messageText;
    private SeekBar preferenceSeekBar;
    final SharedPreferences preferencesDefault;
    int seekBarCurrentValue;

    public abstract String getCurrentValueText(int i);

    SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.preferencesDefault = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == -1 && this.seekBarCurrentValue != this.preferenceSeekBar.getProgress()) {
            this.seekBarCurrentValue = this.preferenceSeekBar.getProgress();
            this.editor = this.preferencesDefault.edit();
            this.editor.putInt(getKey(), this.seekBarCurrentValue);
            this.editor.commit();
        }
    }

    /* Access modifiers changed, original: protected */
    public View onCreateDialogView() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setPadding(20, 10, 20, 10);
        if (this.messageText != null) {
            TextView messageTextView = new TextView(getContext());
            messageTextView.setText(this.messageText);
            messageTextView.setPadding(0, 0, 0, 20);
            linearLayout.addView(messageTextView);
        }
        this.preferenceSeekBar = new SeekBar(getContext());
        this.preferenceSeekBar.setOnSeekBarChangeListener(this);
        this.preferenceSeekBar.setMax(this.max);
        this.preferenceSeekBar.setProgress(Math.min(this.seekBarCurrentValue, this.max));
        this.preferenceSeekBar.setKeyProgressIncrement(this.increment);
        this.preferenceSeekBar.setPadding(0, 0, 0, 10);
        linearLayout.addView(this.preferenceSeekBar);
        this.currentValueTextView = new TextView(getContext());
        this.currentValueTextView.setText(getCurrentValueText(this.preferenceSeekBar.getProgress()));
        this.currentValueTextView.setGravity(1);
        linearLayout.addView(this.currentValueTextView);
        return linearLayout;
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (this.currentValueTextView != null) {
            this.currentValueTextView.setText(getCurrentValueText(progress));
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}

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
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

abstract class SeekBarPreference extends DialogPreference implements OnSeekBarChangeListener {
   private TextView currentValueTextView;
   private Editor editor;
   int increment = 1;
   int max;
   String messageText;
   private SeekBar preferenceSeekBar;
   final SharedPreferences preferencesDefault;
   int seekBarCurrentValue;

   SeekBarPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.preferencesDefault = PreferenceManager.getDefaultSharedPreferences(var1);
   }

   abstract String getCurrentValueText(int var1);

   public void onClick(DialogInterface var1, int var2) {
      if (var2 == -1 && this.seekBarCurrentValue != this.preferenceSeekBar.getProgress()) {
         this.seekBarCurrentValue = this.preferenceSeekBar.getProgress();
         this.editor = this.preferencesDefault.edit();
         this.editor.putInt(this.getKey(), this.seekBarCurrentValue);
         this.editor.commit();
      }

   }

   protected View onCreateDialogView() {
      LinearLayout var1 = new LinearLayout(this.getContext());
      var1.setOrientation(1);
      var1.setPadding(20, 10, 20, 10);
      if (this.messageText != null) {
         TextView var2 = new TextView(this.getContext());
         var2.setText(this.messageText);
         var2.setPadding(0, 0, 0, 20);
         var1.addView(var2);
      }

      this.preferenceSeekBar = new SeekBar(this.getContext());
      this.preferenceSeekBar.setOnSeekBarChangeListener(this);
      this.preferenceSeekBar.setMax(this.max);
      this.preferenceSeekBar.setProgress(Math.min(this.seekBarCurrentValue, this.max));
      this.preferenceSeekBar.setKeyProgressIncrement(this.increment);
      this.preferenceSeekBar.setPadding(0, 0, 0, 10);
      var1.addView(this.preferenceSeekBar);
      this.currentValueTextView = new TextView(this.getContext());
      this.currentValueTextView.setText(this.getCurrentValueText(this.preferenceSeekBar.getProgress()));
      this.currentValueTextView.setGravity(1);
      var1.addView(this.currentValueTextView);
      return var1;
   }

   public void onProgressChanged(SeekBar var1, int var2, boolean var3) {
      if (this.currentValueTextView != null) {
         this.currentValueTextView.setText(this.getCurrentValueText(var2));
      }

   }

   public void onStartTrackingTouch(SeekBar var1) {
   }

   public void onStopTrackingTouch(SeekBar var1) {
   }
}

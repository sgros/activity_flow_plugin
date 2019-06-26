package org.telegram.ui.Cells;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;

public class AudioCell extends FrameLayout {
   private MediaController.AudioEntry audioEntry;
   private TextView authorTextView;
   private CheckBox checkBox;
   private int currentAccount;
   private AudioCell.AudioCellDelegate delegate;
   private TextView genreTextView;
   private boolean needDivider;
   private ImageView playButton;
   private TextView timeTextView;
   private TextView titleTextView;

   public AudioCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.playButton = new ImageView(var1);
      ImageView var2 = this.playButton;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      float var6;
      if (LocaleController.isRTL) {
         var6 = 0.0F;
      } else {
         var6 = 13.0F;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 13.0F;
      } else {
         var7 = 0.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(46, 46.0F, var5 | 48, var6, 13.0F, var7, 0.0F));
      this.playButton.setOnClickListener(new _$$Lambda$AudioCell$4mzKXwu8KpZzCgq5bTdFt_gHiq4(this));
      this.titleTextView = new TextView(var1);
      this.titleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.titleTextView.setTextSize(1, 16.0F);
      this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.titleTextView.setLines(1);
      this.titleTextView.setMaxLines(1);
      this.titleTextView.setSingleLine(true);
      this.titleTextView.setEllipsize(TruncateAt.END);
      TextView var9 = this.titleTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var9.setGravity(var5 | 48);
      var9 = this.titleTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 50.0F;
      } else {
         var6 = 72.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 72.0F;
      } else {
         var7 = 50.0F;
      }

      this.addView(var9, LayoutHelper.createFrame(-1, -2.0F, var5 | 48, var6, 7.0F, var7, 0.0F));
      this.genreTextView = new TextView(var1);
      this.genreTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.genreTextView.setTextSize(1, 14.0F);
      this.genreTextView.setLines(1);
      this.genreTextView.setMaxLines(1);
      this.genreTextView.setSingleLine(true);
      this.genreTextView.setEllipsize(TruncateAt.END);
      var9 = this.genreTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var9.setGravity(var5 | 48);
      var9 = this.genreTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 50.0F;
      } else {
         var6 = 72.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 72.0F;
      } else {
         var7 = 50.0F;
      }

      this.addView(var9, LayoutHelper.createFrame(-1, -2.0F, var5 | 48, var6, 28.0F, var7, 0.0F));
      this.authorTextView = new TextView(var1);
      this.authorTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.authorTextView.setTextSize(1, 14.0F);
      this.authorTextView.setLines(1);
      this.authorTextView.setMaxLines(1);
      this.authorTextView.setSingleLine(true);
      this.authorTextView.setEllipsize(TruncateAt.END);
      var9 = this.authorTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var9.setGravity(var5 | 48);
      var9 = this.authorTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 50.0F;
      } else {
         var6 = 72.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 72.0F;
      } else {
         var7 = 50.0F;
      }

      this.addView(var9, LayoutHelper.createFrame(-1, -2.0F, var5 | 48, var6, 44.0F, var7, 0.0F));
      this.timeTextView = new TextView(var1);
      this.timeTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      this.timeTextView.setTextSize(1, 13.0F);
      this.timeTextView.setLines(1);
      this.timeTextView.setMaxLines(1);
      this.timeTextView.setSingleLine(true);
      this.timeTextView.setEllipsize(TruncateAt.END);
      var9 = this.timeTextView;
      if (LocaleController.isRTL) {
         var5 = 3;
      } else {
         var5 = 5;
      }

      var9.setGravity(var5 | 48);
      var9 = this.timeTextView;
      if (LocaleController.isRTL) {
         var5 = 3;
      } else {
         var5 = 5;
      }

      if (LocaleController.isRTL) {
         var6 = 18.0F;
      } else {
         var6 = 0.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 0.0F;
      } else {
         var7 = 18.0F;
      }

      this.addView(var9, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var6, 11.0F, var7, 0.0F));
      this.checkBox = new CheckBox(var1, 2131165802);
      this.checkBox.setVisibility(0);
      this.checkBox.setColor(Theme.getColor("musicPicker_checkbox"), Theme.getColor("musicPicker_checkboxCheck"));
      CheckBox var8 = this.checkBox;
      var5 = var4;
      if (LocaleController.isRTL) {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 18.0F;
      } else {
         var6 = 0.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 0.0F;
      } else {
         var7 = 18.0F;
      }

      this.addView(var8, LayoutHelper.createFrame(22, 22.0F, var5 | 48, var6, 39.0F, var7, 0.0F));
   }

   private void setPlayDrawable(boolean var1) {
      Drawable var2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(46.0F), Theme.getColor("musicPicker_buttonBackground"), Theme.getColor("musicPicker_buttonBackground"));
      Resources var3 = this.getResources();
      int var4;
      if (var1) {
         var4 = 2131165296;
      } else {
         var4 = 2131165297;
      }

      Drawable var7 = var3.getDrawable(var4);
      var7.setColorFilter(new PorterDuffColorFilter(Theme.getColor("musicPicker_buttonIcon"), Mode.MULTIPLY));
      CombinedDrawable var5 = new CombinedDrawable(var2, var7);
      var5.setCustomSize(AndroidUtilities.dp(46.0F), AndroidUtilities.dp(46.0F));
      this.playButton.setBackgroundDrawable(var5);
      ImageView var8 = this.playButton;
      String var6;
      if (var1) {
         var4 = 2131558408;
         var6 = "AccActionPause";
      } else {
         var4 = 2131558409;
         var6 = "AccActionPlay";
      }

      var8.setContentDescription(LocaleController.getString(var6, var4));
   }

   public MediaController.AudioEntry getAudioEntry() {
      return this.audioEntry;
   }

   public TextView getAuthorTextView() {
      return this.authorTextView;
   }

   public CheckBox getCheckBox() {
      return this.checkBox;
   }

   public TextView getGenreTextView() {
      return this.genreTextView;
   }

   public ImageView getPlayButton() {
      return this.playButton;
   }

   public TextView getTimeTextView() {
      return this.timeTextView;
   }

   public TextView getTitleTextView() {
      return this.titleTextView;
   }

   // $FF: synthetic method
   public void lambda$new$0$AudioCell(View var1) {
      if (this.audioEntry != null) {
         if (MediaController.getInstance().isPlayingMessage(this.audioEntry.messageObject) && !MediaController.getInstance().isMessagePaused()) {
            MediaController.getInstance().pauseMessage(this.audioEntry.messageObject);
            this.setPlayDrawable(false);
         } else {
            ArrayList var2 = new ArrayList();
            var2.add(this.audioEntry.messageObject);
            if (MediaController.getInstance().setPlaylist(var2, this.audioEntry.messageObject)) {
               this.setPlayDrawable(true);
               AudioCell.AudioCellDelegate var3 = this.delegate;
               if (var3 != null) {
                  var3.startedPlayingAudio(this.audioEntry.messageObject);
               }
            }
         }
      }

   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         var1.drawLine((float)AndroidUtilities.dp(72.0F), (float)(this.getHeight() - 1), (float)this.getWidth(), (float)(this.getHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(72.0F) + this.needDivider, 1073741824));
   }

   public void setAudio(MediaController.AudioEntry var1, boolean var2, boolean var3) {
      this.audioEntry = var1;
      this.titleTextView.setText(this.audioEntry.title);
      this.genreTextView.setText(this.audioEntry.genre);
      this.authorTextView.setText(this.audioEntry.author);
      this.timeTextView.setText(String.format("%d:%02d", this.audioEntry.duration / 60, this.audioEntry.duration % 60));
      boolean var4;
      if (MediaController.getInstance().isPlayingMessage(this.audioEntry.messageObject) && !MediaController.getInstance().isMessagePaused()) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.setPlayDrawable(var4);
      this.needDivider = var2;
      this.setWillNotDraw(var2 ^ true);
      this.checkBox.setChecked(var3, false);
   }

   public void setChecked(boolean var1) {
      this.checkBox.setChecked(var1, true);
   }

   public void setDelegate(AudioCell.AudioCellDelegate var1) {
      this.delegate = var1;
   }

   public interface AudioCellDelegate {
      void startedPlayingAudio(MessageObject var1);
   }
}

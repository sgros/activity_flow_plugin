package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ColorSpanUnderline;
import org.telegram.ui.Components.LayoutHelper;

public class StickerSetNameCell extends FrameLayout {
   private ImageView buttonView;
   private boolean empty;
   private boolean isEmoji;
   private TextView textView;
   private TextView urlTextView;

   public StickerSetNameCell(Context var1, boolean var2) {
      super(var1);
      this.isEmoji = var2;
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("chat_emojiPanelStickerSetName"));
      this.textView.setTextSize(1, 15.0F);
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView.setEllipsize(TruncateAt.END);
      this.textView.setSingleLine(true);
      TextView var3 = this.textView;
      float var4;
      if (var2) {
         var4 = 15.0F;
      } else {
         var4 = 17.0F;
      }

      this.addView(var3, LayoutHelper.createFrame(-2, -2.0F, 51, var4, 4.0F, 57.0F, 0.0F));
      this.urlTextView = new TextView(var1);
      this.urlTextView.setTextColor(Theme.getColor("chat_emojiPanelStickerSetName"));
      this.urlTextView.setTextSize(1, 12.0F);
      this.urlTextView.setEllipsize(TruncateAt.END);
      this.urlTextView.setSingleLine(true);
      this.urlTextView.setVisibility(4);
      this.addView(this.urlTextView, LayoutHelper.createFrame(-2, -2.0F, 53, 17.0F, 6.0F, 17.0F, 0.0F));
      this.buttonView = new ImageView(var1);
      this.buttonView.setScaleType(ScaleType.CENTER);
      this.buttonView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelStickerSetNameIcon"), Mode.MULTIPLY));
      this.addView(this.buttonView, LayoutHelper.createFrame(24, 24.0F, 53, 0.0F, 0.0F, 16.0F, 0.0F));
   }

   public void invalidate() {
      this.textView.invalidate();
      super.invalidate();
   }

   protected void onMeasure(int var1, int var2) {
      if (this.empty) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(1, 1073741824));
      } else {
         var1 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824);
         float var3;
         if (this.isEmoji) {
            var3 = 28.0F;
         } else {
            var3 = 24.0F;
         }

         super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(var3), 1073741824));
      }

   }

   public void setOnIconClickListener(OnClickListener var1) {
      this.buttonView.setOnClickListener(var1);
   }

   public void setText(CharSequence var1, int var2) {
      this.setText(var1, var2, 0, 0);
   }

   public void setText(CharSequence var1, int var2, int var3, int var4) {
      if (var1 == null) {
         this.empty = true;
         this.textView.setText("");
         this.buttonView.setVisibility(4);
      } else {
         if (var4 != 0) {
            SpannableStringBuilder var7 = new SpannableStringBuilder(var1);

            try {
               ForegroundColorSpan var5 = new ForegroundColorSpan(Theme.getColor("chat_emojiPanelStickerSetNameHighlight"));
               var7.setSpan(var5, var3, var4 + var3, 33);
            } catch (Exception var6) {
            }

            this.textView.setText(var7);
         } else {
            TextView var8 = this.textView;
            var8.setText(Emoji.replaceEmoji(var1, var8.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0F), false));
         }

         if (var2 != 0) {
            this.buttonView.setImageResource(var2);
            this.buttonView.setVisibility(0);
         } else {
            this.buttonView.setVisibility(4);
         }
      }

   }

   public void setUrl(CharSequence var1, int var2) {
      if (var1 != null) {
         SpannableStringBuilder var3 = new SpannableStringBuilder(var1);

         try {
            ColorSpanUnderline var4 = new ColorSpanUnderline(Theme.getColor("chat_emojiPanelStickerSetNameHighlight"));
            var3.setSpan(var4, 0, var2, 33);
            var4 = new ColorSpanUnderline(Theme.getColor("chat_emojiPanelStickerSetName"));
            var3.setSpan(var4, var2, var1.length(), 33);
         } catch (Exception var5) {
         }

         this.urlTextView.setText(var3);
         this.urlTextView.setVisibility(0);
      } else {
         this.urlTextView.setVisibility(8);
      }

   }
}

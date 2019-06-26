package org.telegram.ui.Cells;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadioButton;

public class PhotoEditRadioCell extends FrameLayout {
   private int currentColor;
   private int currentType;
   private TextView nameTextView;
   private OnClickListener onClickListener;
   private LinearLayout tintButtonsContainer;
   private final int[] tintHighlighsColors = new int[]{0, -1076602, -1388894, -859780, -5968466, -7742235, -13726776, -3303195};
   private final int[] tintShadowColors = new int[]{0, -45747, -753630, -13056, -8269183, -9321002, -16747844, -10080879};

   public PhotoEditRadioCell(Context var1) {
      super(var1);
      this.nameTextView = new TextView(var1);
      this.nameTextView.setGravity(5);
      this.nameTextView.setTextColor(-1);
      this.nameTextView.setTextSize(1, 12.0F);
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.addView(this.nameTextView, LayoutHelper.createFrame(80, -2.0F, 19, 0.0F, 0.0F, 0.0F, 0.0F));
      this.tintButtonsContainer = new LinearLayout(var1);
      this.tintButtonsContainer.setOrientation(0);

      for(int var2 = 0; var2 < this.tintShadowColors.length; ++var2) {
         RadioButton var3 = new RadioButton(var1);
         var3.setSize(AndroidUtilities.dp(20.0F));
         var3.setTag(var2);
         this.tintButtonsContainer.addView(var3, LayoutHelper.createLinear(0, -1, 1.0F / (float)this.tintShadowColors.length));
         var3.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               RadioButton var3 = (RadioButton)var1;
               PhotoEditRadioCell var2;
               if (PhotoEditRadioCell.this.currentType == 0) {
                  var2 = PhotoEditRadioCell.this;
                  var2.currentColor = var2.tintShadowColors[(Integer)var3.getTag()];
               } else {
                  var2 = PhotoEditRadioCell.this;
                  var2.currentColor = var2.tintHighlighsColors[(Integer)var3.getTag()];
               }

               PhotoEditRadioCell.this.updateSelectedTintButton(true);
               PhotoEditRadioCell.this.onClickListener.onClick(PhotoEditRadioCell.this);
            }
         });
      }

      this.addView(this.tintButtonsContainer, LayoutHelper.createFrame(-1, 40.0F, 51, 96.0F, 0.0F, 24.0F, 0.0F));
   }

   private void updateSelectedTintButton(boolean var1) {
      int var2 = this.tintButtonsContainer.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = this.tintButtonsContainer.getChildAt(var3);
         if (var4 instanceof RadioButton) {
            RadioButton var9 = (RadioButton)var4;
            int var5 = (Integer)var9.getTag();
            int var6;
            if (this.currentType == 0) {
               var6 = this.tintShadowColors[var5];
            } else {
               var6 = this.tintHighlighsColors[var5];
            }

            boolean var7;
            if (this.currentColor == var6) {
               var7 = true;
            } else {
               var7 = false;
            }

            var9.setChecked(var7, var1);
            int var8 = -1;
            if (var5 == 0) {
               var6 = -1;
            } else if (this.currentType == 0) {
               var6 = this.tintShadowColors[var5];
            } else {
               var6 = this.tintHighlighsColors[var5];
            }

            if (var5 != 0) {
               if (this.currentType == 0) {
                  var8 = this.tintShadowColors[var5];
               } else {
                  var8 = this.tintHighlighsColors[var5];
               }
            }

            var9.setColor(var6, var8);
         }
      }

   }

   public int getCurrentColor() {
      return this.currentColor;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0F), 1073741824));
   }

   public void setIconAndTextAndValue(String var1, int var2, int var3) {
      this.currentType = var2;
      this.currentColor = var3;
      TextView var4 = this.nameTextView;
      StringBuilder var5 = new StringBuilder();
      var5.append(var1.substring(0, 1).toUpperCase());
      var5.append(var1.substring(1).toLowerCase());
      var4.setText(var5.toString());
      this.updateSelectedTintButton(false);
   }

   public void setOnClickListener(OnClickListener var1) {
      this.onClickListener = var1;
   }
}

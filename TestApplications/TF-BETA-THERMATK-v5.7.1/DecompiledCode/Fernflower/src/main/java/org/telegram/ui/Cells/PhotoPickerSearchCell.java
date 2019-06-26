package org.telegram.ui.Cells;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class PhotoPickerSearchCell extends LinearLayout {
   private PhotoPickerSearchCell.PhotoPickerSearchCellDelegate delegate;

   public PhotoPickerSearchCell(Context var1, boolean var2) {
      super(var1);
      this.setOrientation(0);
      PhotoPickerSearchCell.SearchButton var3 = new PhotoPickerSearchCell.SearchButton(var1);
      var3.textView1.setText(LocaleController.getString("SearchImages", 2131560650));
      var3.textView2.setText(LocaleController.getString("SearchImagesInfo", 2131560651));
      var3.imageView.setImageResource(2131165813);
      this.addView(var3);
      LayoutParams var4 = (LayoutParams)var3.getLayoutParams();
      var4.weight = 0.5F;
      var4.topMargin = AndroidUtilities.dp(4.0F);
      var4.height = AndroidUtilities.dp(48.0F);
      var4.width = 0;
      var3.setLayoutParams(var4);
      var3.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (PhotoPickerSearchCell.this.delegate != null) {
               PhotoPickerSearchCell.this.delegate.didPressedSearchButton(0);
            }

         }
      });
      FrameLayout var6 = new FrameLayout(var1);
      var6.setBackgroundColor(0);
      this.addView(var6);
      var4 = (LayoutParams)var6.getLayoutParams();
      var4.topMargin = AndroidUtilities.dp(4.0F);
      var4.height = AndroidUtilities.dp(48.0F);
      var4.width = AndroidUtilities.dp(4.0F);
      var6.setLayoutParams(var4);
      PhotoPickerSearchCell.SearchButton var5 = new PhotoPickerSearchCell.SearchButton(var1);
      var5.textView1.setText(LocaleController.getString("SearchGifs", 2131560648));
      var5.textView2.setText("GIPHY");
      var5.imageView.setImageResource(2131165812);
      this.addView(var5);
      LayoutParams var7 = (LayoutParams)var5.getLayoutParams();
      var7.weight = 0.5F;
      var7.topMargin = AndroidUtilities.dp(4.0F);
      var7.height = AndroidUtilities.dp(48.0F);
      var7.width = 0;
      var5.setLayoutParams(var7);
      if (var2) {
         var5.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               if (PhotoPickerSearchCell.this.delegate != null) {
                  PhotoPickerSearchCell.this.delegate.didPressedSearchButton(1);
               }

            }
         });
      } else {
         var5.setAlpha(0.5F);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(52.0F), 1073741824));
   }

   public void setDelegate(PhotoPickerSearchCell.PhotoPickerSearchCellDelegate var1) {
      this.delegate = var1;
   }

   public interface PhotoPickerSearchCellDelegate {
      void didPressedSearchButton(int var1);
   }

   private class SearchButton extends FrameLayout {
      private ImageView imageView;
      private View selector;
      private TextView textView1;
      private TextView textView2;

      public SearchButton(Context var2) {
         super(var2);
         this.setBackgroundColor(-15066598);
         this.selector = new View(var2);
         this.selector.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.addView(this.selector, LayoutHelper.createFrame(-1, -1.0F));
         this.imageView = new ImageView(var2);
         this.imageView.setScaleType(ScaleType.CENTER);
         this.addView(this.imageView, LayoutHelper.createFrame(48, 48, 51));
         this.textView1 = new TextView(var2);
         this.textView1.setGravity(16);
         this.textView1.setTextSize(1, 14.0F);
         this.textView1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.textView1.setTextColor(-1);
         this.textView1.setSingleLine(true);
         this.textView1.setEllipsize(TruncateAt.END);
         this.addView(this.textView1, LayoutHelper.createFrame(-1, -2.0F, 51, 51.0F, 8.0F, 4.0F, 0.0F));
         this.textView2 = new TextView(var2);
         this.textView2.setGravity(16);
         this.textView2.setTextSize(1, 10.0F);
         this.textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.textView2.setTextColor(-10066330);
         this.textView2.setSingleLine(true);
         this.textView2.setEllipsize(TruncateAt.END);
         this.addView(this.textView2, LayoutHelper.createFrame(-1, -2.0F, 51, 51.0F, 26.0F, 4.0F, 0.0F));
      }

      public boolean onTouchEvent(MotionEvent var1) {
         if (VERSION.SDK_INT >= 21) {
            this.selector.drawableHotspotChanged(var1.getX(), var1.getY());
         }

         return super.onTouchEvent(var1);
      }
   }
}

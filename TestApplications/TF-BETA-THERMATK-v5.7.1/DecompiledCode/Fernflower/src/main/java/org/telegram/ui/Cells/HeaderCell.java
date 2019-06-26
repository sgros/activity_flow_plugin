package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class HeaderCell extends FrameLayout {
   private int height;
   private TextView textView;
   private SimpleTextView textView2;

   public HeaderCell(Context var1) {
      this(var1, false, 21, 15, false);
   }

   public HeaderCell(Context var1, int var2) {
      this(var1, false, var2, 15, false);
   }

   public HeaderCell(Context var1, boolean var2, int var3, int var4, boolean var5) {
      super(var1);
      this.height = 40;
      this.textView = new TextView(this.getContext());
      this.textView.setTextSize(1, 15.0F);
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView.setEllipsize(TruncateAt.END);
      TextView var10 = this.textView;
      boolean var6 = LocaleController.isRTL;
      byte var7 = 5;
      byte var8;
      if (var6) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      var10.setGravity(var8 | 16);
      this.textView.setMinHeight(AndroidUtilities.dp((float)(this.height - var4)));
      if (var2) {
         this.textView.setTextColor(Theme.getColor("dialogTextBlue2"));
      } else {
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
      }

      var10 = this.textView;
      if (LocaleController.isRTL) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      float var9 = (float)var3;
      this.addView(var10, LayoutHelper.createFrame(-1, -1.0F, var8 | 48, var9, (float)var4, var9, 0.0F));
      if (var5) {
         this.textView2 = new SimpleTextView(this.getContext());
         this.textView2.setTextSize(13);
         SimpleTextView var11 = this.textView2;
         byte var12;
         if (LocaleController.isRTL) {
            var12 = 3;
         } else {
            var12 = 5;
         }

         var11.setGravity(var12 | 48);
         var11 = this.textView2;
         var12 = var7;
         if (LocaleController.isRTL) {
            var12 = 3;
         }

         this.addView(var11, LayoutHelper.createFrame(-1, -1.0F, var12 | 48, var9, 21.0F, var9, 0.0F));
      }

   }

   public SimpleTextView getTextView2() {
      return this.textView2;
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      if (VERSION.SDK_INT >= 19) {
         CollectionItemInfo var2 = var1.getCollectionItemInfo();
         if (var2 != null) {
            var1.setCollectionItemInfo(CollectionItemInfo.obtain(var2.getRowIndex(), var2.getRowSpan(), var2.getColumnIndex(), var2.getColumnSpan(), true));
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
   }

   public void setEnabled(boolean var1, ArrayList var2) {
      float var3 = 1.0F;
      if (var2 != null) {
         TextView var4 = this.textView;
         if (!var1) {
            var3 = 0.5F;
         }

         var2.add(ObjectAnimator.ofFloat(var4, "alpha", new float[]{var3}));
      } else {
         TextView var5 = this.textView;
         if (!var1) {
            var3 = 0.5F;
         }

         var5.setAlpha(var3);
      }

   }

   public void setHeight(int var1) {
      this.textView.setMinHeight(AndroidUtilities.dp((float)this.height) - ((LayoutParams)this.textView.getLayoutParams()).topMargin);
   }

   public void setText(String var1) {
      this.textView.setText(var1);
   }

   public void setText2(String var1) {
      SimpleTextView var2 = this.textView2;
      if (var2 != null) {
         var2.setText(var1);
      }
   }
}

package org.osmdroid.views.overlay.infowindow;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayWithIW;

public class BasicInfoWindow extends InfoWindow {
   static int mDescriptionId;
   static int mImageId;
   static int mSubDescriptionId;
   static int mTitleId;

   public BasicInfoWindow(int var1, MapView var2) {
      super(var1, var2);
      if (mTitleId == 0) {
         setResIds(var2.getContext());
      }

      super.mView.setOnTouchListener(new OnTouchListener() {
         public boolean onTouch(View var1, MotionEvent var2) {
            if (var2.getAction() == 1) {
               BasicInfoWindow.this.close();
            }

            return true;
         }
      });
   }

   private static void setResIds(Context var0) {
      String var1 = var0.getPackageName();
      mTitleId = var0.getResources().getIdentifier("id/bubble_title", (String)null, var1);
      mDescriptionId = var0.getResources().getIdentifier("id/bubble_description", (String)null, var1);
      mSubDescriptionId = var0.getResources().getIdentifier("id/bubble_subdescription", (String)null, var1);
      mImageId = var0.getResources().getIdentifier("id/bubble_image", (String)null, var1);
      if (mTitleId == 0 || mDescriptionId == 0 || mSubDescriptionId == 0 || mImageId == 0) {
         StringBuilder var2 = new StringBuilder();
         var2.append("BasicInfoWindow: unable to get res ids in ");
         var2.append(var1);
         Log.e("OsmDroid", var2.toString());
      }

   }

   public void onClose() {
   }

   public void onOpen(Object var1) {
      OverlayWithIW var2 = (OverlayWithIW)var1;
      String var3 = var2.getTitle();
      String var4 = var3;
      if (var3 == null) {
         var4 = "";
      }

      View var5 = super.mView;
      if (var5 == null) {
         Log.w("OsmDroid", "Error trapped, BasicInfoWindow.open, mView is null!");
      } else {
         TextView var7 = (TextView)var5.findViewById(mTitleId);
         if (var7 != null) {
            var7.setText(var4);
         }

         var3 = var2.getSnippet();
         var4 = var3;
         if (var3 == null) {
            var4 = "";
         }

         Spanned var6 = Html.fromHtml(var4);
         ((TextView)super.mView.findViewById(mDescriptionId)).setText(var6);
         TextView var8 = (TextView)super.mView.findViewById(mSubDescriptionId);
         var3 = var2.getSubDescription();
         if (var3 != null && !"".equals(var3)) {
            var8.setText(Html.fromHtml(var3));
            var8.setVisibility(0);
         } else {
            var8.setVisibility(8);
         }

      }
   }
}

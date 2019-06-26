package com.airbnb.lottie.manager;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable.Callback;
import android.view.View;
import com.airbnb.lottie.FontAssetDelegate;
import com.airbnb.lottie.model.MutablePair;
import com.airbnb.lottie.utils.Logger;
import java.util.HashMap;
import java.util.Map;

public class FontAssetManager {
   private final AssetManager assetManager;
   private String defaultFontFileExtension = ".ttf";
   private FontAssetDelegate delegate;
   private final Map fontFamilies = new HashMap();
   private final Map fontMap = new HashMap();
   private final MutablePair tempPair = new MutablePair();

   public FontAssetManager(Callback var1, FontAssetDelegate var2) {
      this.delegate = var2;
      if (!(var1 instanceof View)) {
         Logger.warning("LottieDrawable must be inside of a view for images to work.");
         this.assetManager = null;
      } else {
         this.assetManager = ((View)var1).getContext().getAssets();
      }
   }

   private Typeface getFontFamily(String var1) {
      Typeface var2 = (Typeface)this.fontFamilies.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         FontAssetDelegate var3 = this.delegate;
         if (var3 == null) {
            if (var3 != null) {
               var3.getFontPath(var1);
               throw null;
            } else {
               StringBuilder var4 = new StringBuilder();
               var4.append("fonts/");
               var4.append(var1);
               var4.append(this.defaultFontFileExtension);
               String var5 = var4.toString();
               var2 = Typeface.createFromAsset(this.assetManager, var5);
               this.fontFamilies.put(var1, var2);
               return var2;
            }
         } else {
            var3.fetchFont(var1);
            throw null;
         }
      }
   }

   private Typeface typefaceForStyle(Typeface var1, String var2) {
      boolean var3 = var2.contains("Italic");
      boolean var4 = var2.contains("Bold");
      byte var5;
      if (var3 && var4) {
         var5 = 3;
      } else if (var3) {
         var5 = 2;
      } else if (var4) {
         var5 = 1;
      } else {
         var5 = 0;
      }

      return var1.getStyle() == var5 ? var1 : Typeface.create(var1, var5);
   }

   public Typeface getTypeface(String var1, String var2) {
      this.tempPair.set(var1, var2);
      Typeface var3 = (Typeface)this.fontMap.get(this.tempPair);
      if (var3 != null) {
         return var3;
      } else {
         Typeface var4 = this.typefaceForStyle(this.getFontFamily(var1), var2);
         this.fontMap.put(this.tempPair, var4);
         return var4;
      }
   }

   public void setDelegate(FontAssetDelegate var1) {
      this.delegate = var1;
   }
}

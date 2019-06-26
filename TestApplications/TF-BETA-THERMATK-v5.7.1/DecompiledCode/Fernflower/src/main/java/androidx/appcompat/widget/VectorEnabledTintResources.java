package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import java.lang.ref.WeakReference;

public class VectorEnabledTintResources extends Resources {
   private static boolean sCompatVectorFromResourcesEnabled;
   private final WeakReference mContextRef;

   public VectorEnabledTintResources(Context var1, Resources var2) {
      super(var2.getAssets(), var2.getDisplayMetrics(), var2.getConfiguration());
      this.mContextRef = new WeakReference(var1);
   }

   public static boolean isCompatVectorFromResourcesEnabled() {
      return sCompatVectorFromResourcesEnabled;
   }

   public static boolean shouldBeUsed() {
      boolean var0;
      if (isCompatVectorFromResourcesEnabled() && VERSION.SDK_INT <= 20) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public Drawable getDrawable(int var1) throws NotFoundException {
      Context var2 = (Context)this.mContextRef.get();
      return var2 != null ? AppCompatDrawableManager.get().onDrawableLoadedFromResources(var2, this, var1) : super.getDrawable(var1);
   }

   final Drawable superGetDrawable(int var1) {
      return super.getDrawable(var1);
   }
}

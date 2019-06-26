package pl.droidsonroids.gif.transforms;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public interface Transform {
   void onBoundsChange(Rect var1);

   void onDraw(Canvas var1, Paint var2, Bitmap var3);
}

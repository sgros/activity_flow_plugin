package android.support.p003v7.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.RequiresApi;
import android.support.p003v7.widget.RoundRectDrawableWithShadow.RoundRectHelper;

@RequiresApi(17)
/* renamed from: android.support.v7.widget.CardViewApi17Impl */
class CardViewApi17Impl extends CardViewBaseImpl {

    /* renamed from: android.support.v7.widget.CardViewApi17Impl$1 */
    class C05371 implements RoundRectHelper {
        C05371() {
        }

        public void drawRoundRect(Canvas canvas, RectF rectF, float f, Paint paint) {
            canvas.drawRoundRect(rectF, f, f, paint);
        }
    }

    CardViewApi17Impl() {
    }

    public void initStatic() {
        RoundRectDrawableWithShadow.sRoundRectHelper = new C05371();
    }
}

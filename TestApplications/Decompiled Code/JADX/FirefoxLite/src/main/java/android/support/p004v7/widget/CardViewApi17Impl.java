package android.support.p004v7.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.p004v7.widget.RoundRectDrawableWithShadow.RoundRectHelper;

/* renamed from: android.support.v7.widget.CardViewApi17Impl */
class CardViewApi17Impl extends CardViewBaseImpl {

    /* renamed from: android.support.v7.widget.CardViewApi17Impl$1 */
    class C06401 implements RoundRectHelper {
        C06401() {
        }

        public void drawRoundRect(Canvas canvas, RectF rectF, float f, Paint paint) {
            canvas.drawRoundRect(rectF, f, f, paint);
        }
    }

    CardViewApi17Impl() {
    }

    public void initStatic() {
        RoundRectDrawableWithShadow.sRoundRectHelper = new C06401();
    }
}

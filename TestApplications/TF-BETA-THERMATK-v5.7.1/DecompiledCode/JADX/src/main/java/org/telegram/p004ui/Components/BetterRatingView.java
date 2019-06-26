package org.telegram.p004ui.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.BetterRatingView */
public class BetterRatingView extends View {
    private Bitmap filledStar = BitmapFactory.decodeResource(getResources(), C1067R.C1065drawable.ic_rating_star_filled).extractAlpha();
    private Bitmap hollowStar = BitmapFactory.decodeResource(getResources(), C1067R.C1065drawable.ic_rating_star).extractAlpha();
    private OnRatingChangeListener listener;
    private int numStars = 5;
    private Paint paint = new Paint();
    private int selectedRating = 0;

    /* renamed from: org.telegram.ui.Components.BetterRatingView$OnRatingChangeListener */
    public interface OnRatingChangeListener {
        void onRatingChanged(int i);
    }

    public BetterRatingView(Context context) {
        super(context);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension((this.numStars * AndroidUtilities.m26dp(32.0f)) + ((this.numStars - 1) * AndroidUtilities.m26dp(16.0f)), AndroidUtilities.m26dp(32.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int i = 0;
        while (i < this.numStars) {
            this.paint.setColor(Theme.getColor(i < this.selectedRating ? Theme.key_dialogTextBlue : Theme.key_dialogTextHint));
            canvas.drawBitmap(i < this.selectedRating ? this.filledStar : this.hollowStar, (float) (AndroidUtilities.m26dp(48.0f) * i), 0.0f, this.paint);
            i++;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float dp = (float) AndroidUtilities.m26dp(-8.0f);
        for (int i = 0; i < this.numStars; i++) {
            if (motionEvent.getX() > dp && motionEvent.getX() < ((float) AndroidUtilities.m26dp(48.0f)) + dp) {
                int i2 = i + 1;
                if (this.selectedRating != i2) {
                    this.selectedRating = i2;
                    OnRatingChangeListener onRatingChangeListener = this.listener;
                    if (onRatingChangeListener != null) {
                        onRatingChangeListener.onRatingChanged(this.selectedRating);
                    }
                    invalidate();
                    return true;
                }
            }
            dp += (float) AndroidUtilities.m26dp(48.0f);
        }
        return true;
    }

    public int getRating() {
        return this.selectedRating;
    }

    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.listener = onRatingChangeListener;
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.MotionEvent;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.view.View;

public class BetterRatingView extends View
{
    private Bitmap filledStar;
    private Bitmap hollowStar;
    private OnRatingChangeListener listener;
    private int numStars;
    private Paint paint;
    private int selectedRating;
    
    public BetterRatingView(final Context context) {
        super(context);
        this.paint = new Paint();
        this.numStars = 5;
        this.selectedRating = 0;
        this.filledStar = BitmapFactory.decodeResource(this.getResources(), 2131165466).extractAlpha();
        this.hollowStar = BitmapFactory.decodeResource(this.getResources(), 2131165465).extractAlpha();
    }
    
    public int getRating() {
        return this.selectedRating;
    }
    
    protected void onDraw(final Canvas canvas) {
        for (int i = 0; i < this.numStars; ++i) {
            final Paint paint = this.paint;
            String s;
            if (i < this.selectedRating) {
                s = "dialogTextBlue";
            }
            else {
                s = "dialogTextHint";
            }
            paint.setColor(Theme.getColor(s));
            Bitmap bitmap;
            if (i < this.selectedRating) {
                bitmap = this.filledStar;
            }
            else {
                bitmap = this.hollowStar;
            }
            canvas.drawBitmap(bitmap, (float)(AndroidUtilities.dp(48.0f) * i), 0.0f, this.paint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(this.numStars * AndroidUtilities.dp(32.0f) + (this.numStars - 1) * AndroidUtilities.dp(16.0f), AndroidUtilities.dp(32.0f));
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        float n = (float)AndroidUtilities.dp(-8.0f);
        for (int i = 0; i < this.numStars; ++i) {
            if (motionEvent.getX() > n && motionEvent.getX() < AndroidUtilities.dp(48.0f) + n) {
                final int selectedRating = this.selectedRating;
                final int selectedRating2 = i + 1;
                if (selectedRating != selectedRating2) {
                    this.selectedRating = selectedRating2;
                    final OnRatingChangeListener listener = this.listener;
                    if (listener != null) {
                        listener.onRatingChanged(this.selectedRating);
                    }
                    this.invalidate();
                    break;
                }
            }
            n += AndroidUtilities.dp(48.0f);
        }
        return true;
    }
    
    public void setOnRatingChangeListener(final OnRatingChangeListener listener) {
        this.listener = listener;
    }
    
    public interface OnRatingChangeListener
    {
        void onRatingChanged(final int p0);
    }
}

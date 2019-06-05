// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint$Align;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.view.View;

public class Placeholder extends View
{
    private View mContent;
    private int mContentId;
    private int mEmptyVisibility;
    
    public View getContent() {
        return this.mContent;
    }
    
    public int getEmptyVisibility() {
        return this.mEmptyVisibility;
    }
    
    public void onDraw(final Canvas canvas) {
        if (this.isInEditMode()) {
            canvas.drawRGB(223, 223, 223);
            final Paint paint = new Paint();
            paint.setARGB(255, 210, 210, 210);
            paint.setTextAlign(Paint$Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, 0));
            final Rect rect = new Rect();
            canvas.getClipBounds(rect);
            paint.setTextSize((float)rect.height());
            final int height = rect.height();
            final int width = rect.width();
            paint.setTextAlign(Paint$Align.LEFT);
            paint.getTextBounds("?", 0, "?".length(), rect);
            canvas.drawText("?", width / 2.0f - rect.width() / 2.0f - rect.left, height / 2.0f + rect.height() / 2.0f - rect.bottom, paint);
        }
    }
    
    public void setContentId(final int mContentId) {
        if (this.mContentId == mContentId) {
            return;
        }
        if (this.mContent != null) {
            this.mContent.setVisibility(0);
            ((ConstraintLayout.LayoutParams)this.mContent.getLayoutParams()).isInPlaceholder = false;
            this.mContent = null;
        }
        if ((this.mContentId = mContentId) != -1) {
            final View viewById = ((View)this.getParent()).findViewById(mContentId);
            if (viewById != null) {
                viewById.setVisibility(8);
            }
        }
    }
    
    public void setEmptyVisibility(final int mEmptyVisibility) {
        this.mEmptyVisibility = mEmptyVisibility;
    }
    
    public void updatePostMeasure(final ConstraintLayout constraintLayout) {
        if (this.mContent == null) {
            return;
        }
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        final ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams)this.mContent.getLayoutParams();
        layoutParams2.widget.setVisibility(0);
        layoutParams.widget.setWidth(layoutParams2.widget.getWidth());
        layoutParams.widget.setHeight(layoutParams2.widget.getHeight());
        layoutParams2.widget.setVisibility(8);
    }
    
    public void updatePreLayout(final ConstraintLayout constraintLayout) {
        if (this.mContentId == -1 && !this.isInEditMode()) {
            this.setVisibility(this.mEmptyVisibility);
        }
        this.mContent = constraintLayout.findViewById(this.mContentId);
        if (this.mContent != null) {
            ((ConstraintLayout.LayoutParams)this.mContent.getLayoutParams()).isInPlaceholder = true;
            this.mContent.setVisibility(0);
            this.setVisibility(0);
        }
    }
}

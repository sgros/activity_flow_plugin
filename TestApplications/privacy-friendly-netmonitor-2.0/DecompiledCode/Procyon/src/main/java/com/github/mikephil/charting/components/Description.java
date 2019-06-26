// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.Utils;
import android.graphics.Paint$Align;
import com.github.mikephil.charting.utils.MPPointF;

public class Description extends ComponentBase
{
    private MPPointF mPosition;
    private Paint$Align mTextAlign;
    private String text;
    
    public Description() {
        this.text = "Description Label";
        this.mTextAlign = Paint$Align.RIGHT;
        this.mTextSize = Utils.convertDpToPixel(8.0f);
    }
    
    public MPPointF getPosition() {
        return this.mPosition;
    }
    
    public String getText() {
        return this.text;
    }
    
    public Paint$Align getTextAlign() {
        return this.mTextAlign;
    }
    
    public void setPosition(final float x, final float y) {
        if (this.mPosition == null) {
            this.mPosition = MPPointF.getInstance(x, y);
        }
        else {
            this.mPosition.x = x;
            this.mPosition.y = y;
        }
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    public void setTextAlign(final Paint$Align mTextAlign) {
        this.mTextAlign = mTextAlign;
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.content.Context;
import android.view.View;

public class CheckBox2 extends View
{
    private CheckBoxBase checkBoxBase;
    
    public CheckBox2(final Context context) {
        super(context);
        this.checkBoxBase = new CheckBoxBase(this);
    }
    
    public float getProgress() {
        return this.checkBoxBase.getProgress();
    }
    
    public boolean isChecked() {
        return this.checkBoxBase.isChecked();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.checkBoxBase.onAttachedToWindow();
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.checkBoxBase.onDetachedFromWindow();
    }
    
    protected void onDraw(final Canvas canvas) {
        this.checkBoxBase.draw(canvas);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.checkBoxBase.setBounds(0, 0, n3 - n, n4 - n2);
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        this.checkBoxBase.setChecked(b, b2);
    }
    
    public void setColor(final String s, final String s2, final String s3) {
        this.checkBoxBase.setColor(s, s2, s3);
    }
    
    public void setDrawBackgroundAsArc(final int drawBackgroundAsArc) {
        this.checkBoxBase.setDrawBackgroundAsArc(drawBackgroundAsArc);
    }
    
    public void setDrawUnchecked(final boolean drawUnchecked) {
        this.checkBoxBase.setDrawUnchecked(drawUnchecked);
    }
    
    public void setEnabled(final boolean b) {
        this.checkBoxBase.setEnabled(b);
        super.setEnabled(b);
    }
    
    public void setProgressDelegate(final CheckBoxBase.ProgressDelegate progressDelegate) {
        this.checkBoxBase.setProgressDelegate(progressDelegate);
    }
    
    public void setSize(final int size) {
        this.checkBoxBase.setSize(size);
    }
}

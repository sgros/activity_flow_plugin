// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Paint;

public class HintEditText extends EditTextBoldCursor
{
    private String hintText;
    private float numberSize;
    private Paint paint;
    private Rect rect;
    private float spaceSize;
    private float textOffset;
    
    public HintEditText(final Context context) {
        super(context);
        this.paint = new Paint();
        this.rect = new Rect();
        this.paint.setColor(Theme.getColor("windowBackgroundWhiteHintText"));
    }
    
    public String getHintText() {
        return this.hintText;
    }
    
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.hintText != null && this.length() < this.hintText.length()) {
            final int n = this.getMeasuredHeight() / 2;
            float textOffset = this.textOffset;
            for (int i = this.length(); i < this.hintText.length(); ++i) {
                float n2;
                if (this.hintText.charAt(i) == ' ') {
                    n2 = this.spaceSize;
                }
                else {
                    this.rect.set((int)textOffset + AndroidUtilities.dp(1.0f), n, (int)(this.numberSize + textOffset) - AndroidUtilities.dp(1.0f), AndroidUtilities.dp(2.0f) + n);
                    canvas.drawRect(this.rect, this.paint);
                    n2 = this.numberSize;
                }
                textOffset += n2;
            }
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.onTextChange();
    }
    
    public void onTextChange() {
        float measureText;
        if (this.length() > 0) {
            measureText = this.getPaint().measureText((CharSequence)this.getText(), 0, this.length());
        }
        else {
            measureText = 0.0f;
        }
        this.textOffset = measureText;
        this.spaceSize = this.getPaint().measureText(" ");
        this.numberSize = this.getPaint().measureText("1");
        this.invalidate();
    }
    
    @Override
    public void setHintText(final String hintText) {
        this.hintText = hintText;
        this.onTextChange();
        this.setText((CharSequence)this.getText());
    }
}

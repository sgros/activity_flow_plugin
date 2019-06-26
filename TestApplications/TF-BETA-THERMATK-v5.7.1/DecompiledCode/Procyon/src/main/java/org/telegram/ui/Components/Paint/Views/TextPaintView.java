// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint.Views;

import android.graphics.Canvas;
import android.view.ViewGroup;
import org.telegram.ui.Components.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.Build$VERSION;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.Typeface;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.Point;
import android.content.Context;
import org.telegram.ui.Components.Paint.Swatch;

public class TextPaintView extends EntityView
{
    private int baseFontSize;
    private EditTextOutline editText;
    private boolean stroke;
    private Swatch swatch;
    
    public TextPaintView(final Context context, final TextPaintView textPaintView, final Point point) {
        this(context, point, textPaintView.baseFontSize, textPaintView.getText(), textPaintView.getSwatch(), textPaintView.stroke);
        this.setRotation(textPaintView.getRotation());
        this.setScale(textPaintView.getScale());
    }
    
    public TextPaintView(final Context context, final Point point, final int baseFontSize, final String text, final Swatch swatch, final boolean stroke) {
        super(context, point);
        this.baseFontSize = baseFontSize;
        (this.editText = new EditTextOutline(context)).setBackgroundColor(0);
        this.editText.setPadding(AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f));
        this.editText.setClickable(false);
        this.editText.setEnabled(false);
        this.editText.setTextSize(0, (float)this.baseFontSize);
        this.editText.setText((CharSequence)text);
        this.editText.setTextColor(swatch.color);
        this.editText.setTypeface((Typeface)null, 1);
        this.editText.setGravity(17);
        this.editText.setHorizontallyScrolling(false);
        this.editText.setImeOptions(268435456);
        this.editText.setFocusableInTouchMode(true);
        final EditTextOutline editText = this.editText;
        editText.setInputType(editText.getInputType() | 0x4000);
        this.addView((View)this.editText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 51));
        if (Build$VERSION.SDK_INT >= 23) {
            this.editText.setBreakStrategy(0);
        }
        this.setSwatch(swatch);
        this.setStroke(stroke);
        this.updatePosition();
        this.editText.addTextChangedListener((TextWatcher)new TextWatcher() {
            private int beforeCursorPosition = 0;
            private String text;
            
            public void afterTextChanged(final Editable editable) {
                TextPaintView.this.editText.removeTextChangedListener((TextWatcher)this);
                if (TextPaintView.this.editText.getLineCount() > 9) {
                    TextPaintView.this.editText.setText((CharSequence)this.text);
                    TextPaintView.this.editText.setSelection(this.beforeCursorPosition);
                }
                TextPaintView.this.editText.addTextChangedListener((TextWatcher)this);
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int beforeCursorPosition, final int n, final int n2) {
                this.text = charSequence.toString();
                this.beforeCursorPosition = beforeCursorPosition;
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
    }
    
    private void updateColor() {
        if (this.stroke) {
            this.editText.setTextColor(-1);
            this.editText.setStrokeColor(this.swatch.color);
            this.editText.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        }
        else {
            this.editText.setTextColor(this.swatch.color);
            this.editText.setStrokeColor(0);
            this.editText.setShadowLayer(8.0f, 0.0f, 2.0f, -1442840576);
        }
    }
    
    public void beginEditing() {
        this.editText.setEnabled(true);
        this.editText.setClickable(true);
        this.editText.requestFocus();
        final EditTextOutline editText = this.editText;
        editText.setSelection(editText.getText().length());
    }
    
    protected TextViewSelectionView createSelectionView() {
        return new TextViewSelectionView(this.getContext());
    }
    
    public void endEditing() {
        this.editText.clearFocus();
        this.editText.setEnabled(false);
        this.editText.setClickable(false);
        this.updateSelectionView();
    }
    
    public View getFocusedView() {
        return (View)this.editText;
    }
    
    @Override
    protected Rect getSelectionBounds() {
        final float scaleX = ((ViewGroup)this.getParent()).getScaleX();
        final float n = this.getWidth() * this.getScale() + AndroidUtilities.dp(46.0f) / scaleX;
        final float n2 = this.getHeight() * this.getScale() + AndroidUtilities.dp(20.0f) / scaleX;
        final Point position = super.position;
        return new Rect((position.x - n / 2.0f) * scaleX, (position.y - n2 / 2.0f) * scaleX, n * scaleX, n2 * scaleX);
    }
    
    public Swatch getSwatch() {
        return this.swatch;
    }
    
    public String getText() {
        return this.editText.getText().toString();
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.updatePosition();
    }
    
    public void setMaxWidth(final int maxWidth) {
        this.editText.setMaxWidth(maxWidth);
    }
    
    public void setStroke(final boolean stroke) {
        this.stroke = stroke;
        this.updateColor();
    }
    
    public void setSwatch(final Swatch swatch) {
        this.swatch = swatch;
        this.updateColor();
    }
    
    public void setText(final String text) {
        this.editText.setText((CharSequence)text);
    }
    
    public class TextViewSelectionView extends SelectionView
    {
        public TextViewSelectionView(final Context context) {
            super(context);
        }
        
        protected void onDraw(final Canvas canvas) {
            super.onDraw(canvas);
            final float n = (float)AndroidUtilities.dp(3.0f);
            final float n2 = (float)AndroidUtilities.dp(3.0f);
            final float n3 = (float)AndroidUtilities.dp(1.0f);
            final float n4 = (float)AndroidUtilities.dp(4.5f);
            final float n5 = n4 + n3 + AndroidUtilities.dp(15.0f);
            final float n6 = (float)this.getWidth();
            final float n7 = n5 * 2.0f;
            final float n8 = n6 - n7;
            final float n9 = this.getHeight() - n7;
            final float n10 = n + n2;
            final int n11 = (int)Math.floor(n8 / n10);
            final float n12 = (float)Math.ceil((n8 - n11 * n10 + n) / 2.0f);
            for (int i = 0; i < n11; ++i) {
                final float n13 = n12 + n5 + i * n10;
                final float n14 = n3 / 2.0f;
                final float n15 = n13 + n2;
                canvas.drawRect(n13, n5 - n14, n15, n5 + n14, super.paint);
                final float n16 = n5 + n9;
                canvas.drawRect(n13, n16 - n14, n15, n16 + n14, super.paint);
            }
            final int n17 = (int)Math.floor(n9 / n10);
            final float n18 = (float)Math.ceil((n9 - n17 * n10 + n) / 2.0f);
            for (int j = 0; j < n17; ++j) {
                final float n19 = n18 + n5 + j * n10;
                final float n20 = n3 / 2.0f;
                final float n21 = n19 + n2;
                canvas.drawRect(n5 - n20, n19, n5 + n20, n21, super.paint);
                final float n22 = n5 + n8;
                canvas.drawRect(n22 - n20, n19, n22 + n20, n21, super.paint);
            }
            final float n23 = n9 / 2.0f + n5;
            canvas.drawCircle(n5, n23, n4, super.dotPaint);
            canvas.drawCircle(n5, n23, n4, super.dotStrokePaint);
            final float n24 = n5 + n8;
            canvas.drawCircle(n24, n23, n4, super.dotPaint);
            canvas.drawCircle(n24, n23, n4, super.dotStrokePaint);
        }
        
        @Override
        protected int pointInsideHandle(final float n, final float n2) {
            final float n3 = (float)AndroidUtilities.dp(1.0f);
            final float n4 = (float)AndroidUtilities.dp(19.5f);
            final float n5 = n3 + n4;
            final float n6 = (float)this.getWidth();
            final float n7 = n5 * 2.0f;
            final float n8 = n6 - n7;
            final float n9 = this.getHeight() - n7;
            final float n10 = n9 / 2.0f + n5;
            if (n > n5 - n4 && n2 > n10 - n4 && n < n5 + n4 && n2 < n10 + n4) {
                return 1;
            }
            final float n11 = n5 + n8;
            if (n > n11 - n4 && n2 > n10 - n4 && n < n11 + n4 && n2 < n10 + n4) {
                return 2;
            }
            if (n > n5 && n < n8 && n2 > n5 && n2 < n9) {
                return 3;
            }
            return 0;
        }
    }
}

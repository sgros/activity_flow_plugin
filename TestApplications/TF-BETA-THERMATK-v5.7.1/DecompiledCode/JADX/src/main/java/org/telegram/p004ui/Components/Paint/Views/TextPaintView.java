package org.telegram.p004ui.Components.Paint.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.Paint.Swatch;
import org.telegram.p004ui.Components.Paint.Views.EntityView.SelectionView;
import org.telegram.p004ui.Components.Point;
import org.telegram.p004ui.Components.Rect;

/* renamed from: org.telegram.ui.Components.Paint.Views.TextPaintView */
public class TextPaintView extends EntityView {
    private int baseFontSize;
    private EditTextOutline editText;
    private boolean stroke;
    private Swatch swatch;

    /* renamed from: org.telegram.ui.Components.Paint.Views.TextPaintView$1 */
    class C28961 implements TextWatcher {
        private int beforeCursorPosition = 0;
        private String text;

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C28961() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            this.text = charSequence.toString();
            this.beforeCursorPosition = i;
        }

        public void afterTextChanged(Editable editable) {
            TextPaintView.this.editText.removeTextChangedListener(this);
            if (TextPaintView.this.editText.getLineCount() > 9) {
                TextPaintView.this.editText.setText(this.text);
                TextPaintView.this.editText.setSelection(this.beforeCursorPosition);
            }
            TextPaintView.this.editText.addTextChangedListener(this);
        }
    }

    /* renamed from: org.telegram.ui.Components.Paint.Views.TextPaintView$TextViewSelectionView */
    public class TextViewSelectionView extends SelectionView {
        public TextViewSelectionView(Context context) {
            super(context);
        }

        /* Access modifiers changed, original: protected */
        public int pointInsideHandle(float f, float f2) {
            float dp = (float) AndroidUtilities.m26dp(19.5f);
            float dp2 = ((float) AndroidUtilities.m26dp(1.0f)) + dp;
            float f3 = dp2 * 2.0f;
            float width = ((float) getWidth()) - f3;
            float height = ((float) getHeight()) - f3;
            float f4 = (height / 2.0f) + dp2;
            if (f > dp2 - dp && f2 > f4 - dp && f < dp2 + dp && f2 < f4 + dp) {
                return 1;
            }
            f3 = dp2 + width;
            if (f <= f3 - dp || f2 <= f4 - dp || f >= f3 + dp || f2 >= f4 + dp) {
                return (f <= dp2 || f >= width || f2 <= dp2 || f2 >= height) ? 0 : 3;
            } else {
                return 2;
            }
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas canvas) {
            float f;
            float f2;
            int i;
            float f3;
            Canvas canvas2 = canvas;
            super.onDraw(canvas);
            float dp = (float) AndroidUtilities.m26dp(3.0f);
            float dp2 = (float) AndroidUtilities.m26dp(3.0f);
            float dp3 = (float) AndroidUtilities.m26dp(1.0f);
            float dp4 = (float) AndroidUtilities.m26dp(4.5f);
            float dp5 = (dp4 + dp3) + ((float) AndroidUtilities.m26dp(15.0f));
            float f4 = dp5 * 2.0f;
            float width = ((float) getWidth()) - f4;
            float height = ((float) getHeight()) - f4;
            float f5 = dp + dp2;
            int floor = (int) Math.floor((double) (width / f5));
            float ceil = (float) Math.ceil((double) (((width - (((float) floor) * f5)) + dp) / 2.0f));
            int i2 = 0;
            while (i2 < floor) {
                f = (ceil + dp5) + (((float) i2) * f5);
                f2 = dp3 / 2.0f;
                float f6 = f + dp2;
                float f7 = dp5 + f2;
                f4 = f;
                int i3 = i2;
                float f8 = f6;
                float f9 = ceil;
                ceil = f7;
                i = floor;
                canvas.drawRect(f4, dp5 - f2, f8, ceil, this.paint);
                f3 = dp5 + height;
                canvas.drawRect(f4, f3 - f2, f8, f3 + f2, this.paint);
                i2 = i3 + 1;
                floor = i;
                ceil = f9;
            }
            floor = (int) Math.floor((double) (height / f5));
            dp = (float) Math.ceil((double) (((height - (((float) floor) * f5)) + dp) / 2.0f));
            int i4 = 0;
            while (i4 < floor) {
                float f10 = (dp + dp5) + (((float) i4) * f5);
                f = dp3 / 2.0f;
                f2 = f10 + dp2;
                float f11 = f10;
                i = i4;
                ceil = f2;
                int i5 = floor;
                canvas.drawRect(dp5 - f, f11, dp5 + f, ceil, this.paint);
                f3 = dp5 + width;
                canvas.drawRect(f3 - f, f11, f3 + f, ceil, this.paint);
                i4 = i + 1;
                floor = i5;
            }
            height = (height / 2.0f) + dp5;
            canvas2.drawCircle(dp5, height, dp4, this.dotPaint);
            canvas2.drawCircle(dp5, height, dp4, this.dotStrokePaint);
            dp5 += width;
            canvas2.drawCircle(dp5, height, dp4, this.dotPaint);
            canvas2.drawCircle(dp5, height, dp4, this.dotStrokePaint);
        }
    }

    public TextPaintView(Context context, Point point, int i, String str, Swatch swatch, boolean z) {
        super(context, point);
        this.baseFontSize = i;
        this.editText = new EditTextOutline(context);
        this.editText.setBackgroundColor(0);
        this.editText.setPadding(AndroidUtilities.m26dp(7.0f), AndroidUtilities.m26dp(7.0f), AndroidUtilities.m26dp(7.0f), AndroidUtilities.m26dp(7.0f));
        this.editText.setClickable(false);
        this.editText.setEnabled(false);
        this.editText.setTextSize(0, (float) this.baseFontSize);
        this.editText.setText(str);
        this.editText.setTextColor(swatch.color);
        this.editText.setTypeface(null, 1);
        this.editText.setGravity(17);
        this.editText.setHorizontallyScrolling(false);
        this.editText.setImeOptions(268435456);
        this.editText.setFocusableInTouchMode(true);
        EditTextOutline editTextOutline = this.editText;
        editTextOutline.setInputType(editTextOutline.getInputType() | 16384);
        addView(this.editText, LayoutHelper.createFrame(-2, -2, 51));
        if (VERSION.SDK_INT >= 23) {
            this.editText.setBreakStrategy(0);
        }
        setSwatch(swatch);
        setStroke(z);
        updatePosition();
        this.editText.addTextChangedListener(new C28961());
    }

    public TextPaintView(Context context, TextPaintView textPaintView, Point point) {
        this(context, point, textPaintView.baseFontSize, textPaintView.getText(), textPaintView.getSwatch(), textPaintView.stroke);
        setRotation(textPaintView.getRotation());
        setScale(textPaintView.getScale());
    }

    public void setMaxWidth(int i) {
        this.editText.setMaxWidth(i);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updatePosition();
    }

    public String getText() {
        return this.editText.getText().toString();
    }

    public void setText(String str) {
        this.editText.setText(str);
    }

    public View getFocusedView() {
        return this.editText;
    }

    public void beginEditing() {
        this.editText.setEnabled(true);
        this.editText.setClickable(true);
        this.editText.requestFocus();
        EditTextOutline editTextOutline = this.editText;
        editTextOutline.setSelection(editTextOutline.getText().length());
    }

    public void endEditing() {
        this.editText.clearFocus();
        this.editText.setEnabled(false);
        this.editText.setClickable(false);
        updateSelectionView();
    }

    public Swatch getSwatch() {
        return this.swatch;
    }

    public void setSwatch(Swatch swatch) {
        this.swatch = swatch;
        updateColor();
    }

    public void setStroke(boolean z) {
        this.stroke = z;
        updateColor();
    }

    private void updateColor() {
        if (this.stroke) {
            this.editText.setTextColor(-1);
            this.editText.setStrokeColor(this.swatch.color);
            this.editText.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
            return;
        }
        this.editText.setTextColor(this.swatch.color);
        this.editText.setStrokeColor(0);
        this.editText.setShadowLayer(8.0f, 0.0f, 2.0f, -1442840576);
    }

    /* Access modifiers changed, original: protected */
    public Rect getSelectionBounds() {
        float scaleX = ((ViewGroup) getParent()).getScaleX();
        float width = (((float) getWidth()) * getScale()) + (((float) AndroidUtilities.m26dp(46.0f)) / scaleX);
        float height = (((float) getHeight()) * getScale()) + (((float) AndroidUtilities.m26dp(20.0f)) / scaleX);
        Point point = this.position;
        return new Rect((point.f600x - (width / 2.0f)) * scaleX, (point.f601y - (height / 2.0f)) * scaleX, width * scaleX, height * scaleX);
    }

    /* Access modifiers changed, original: protected */
    public TextViewSelectionView createSelectionView() {
        return new TextViewSelectionView(getContext());
    }
}

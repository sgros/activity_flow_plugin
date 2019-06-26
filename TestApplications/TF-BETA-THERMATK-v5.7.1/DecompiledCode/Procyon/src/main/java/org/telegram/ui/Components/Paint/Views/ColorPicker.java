// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint.Views;

import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.annotation.SuppressLint;
import android.graphics.Shader;
import android.graphics.LinearGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Canvas;
import org.telegram.ui.Components.Paint.Swatch;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.ImageView$ScaleType;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Style;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.graphics.RectF;
import android.view.animation.OvershootInterpolator;
import android.graphics.Paint;
import android.widget.FrameLayout;

public class ColorPicker extends FrameLayout
{
    private static final int[] COLORS;
    private static final float[] LOCATIONS;
    private Paint backgroundPaint;
    private boolean changingWeight;
    private ColorPickerDelegate delegate;
    private boolean dragging;
    private float draggingFactor;
    private Paint gradientPaint;
    private boolean interacting;
    private OvershootInterpolator interpolator;
    private float location;
    private RectF rectF;
    private ImageView settingsButton;
    private Drawable shadowDrawable;
    private Paint swatchPaint;
    private Paint swatchStrokePaint;
    private ImageView undoButton;
    private boolean wasChangingWeight;
    private float weight;
    
    static {
        COLORS = new int[] { -1431751, -2409774, -13610525, -11942419, -8337308, -205211, -223667, -16777216, -1 };
        LOCATIONS = new float[] { 0.0f, 0.14f, 0.24f, 0.39f, 0.49f, 0.62f, 0.73f, 0.85f, 1.0f };
    }
    
    public ColorPicker(final Context context) {
        super(context);
        this.interpolator = new OvershootInterpolator(1.02f);
        this.gradientPaint = new Paint(1);
        this.backgroundPaint = new Paint(1);
        this.swatchPaint = new Paint(1);
        this.swatchStrokePaint = new Paint(1);
        this.rectF = new RectF();
        this.location = 1.0f;
        this.weight = 0.27f;
        this.setWillNotDraw(false);
        this.shadowDrawable = this.getResources().getDrawable(2131165520);
        this.backgroundPaint.setColor(-1);
        this.swatchStrokePaint.setStyle(Paint$Style.STROKE);
        this.swatchStrokePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0f));
        (this.settingsButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.settingsButton.setImageResource(2131165746);
        this.addView((View)this.settingsButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(60, 52.0f));
        this.settingsButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (ColorPicker.this.delegate != null) {
                    ColorPicker.this.delegate.onSettingsPressed();
                }
            }
        });
        (this.undoButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.undoButton.setImageResource(2131165752);
        this.addView((View)this.undoButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(60, 52.0f));
        this.undoButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (ColorPicker.this.delegate != null) {
                    ColorPicker.this.delegate.onUndoPressed();
                }
            }
        });
        this.setLocation(this.location = context.getSharedPreferences("paint", 0).getFloat("last_color_location", 1.0f));
    }
    
    private int interpolateColors(int blue, int blue2, float min) {
        min = Math.min(Math.max(min, 0.0f), 1.0f);
        final int red = Color.red(blue);
        final int red2 = Color.red(blue2);
        final int green = Color.green(blue);
        final int green2 = Color.green(blue2);
        blue = Color.blue(blue);
        blue2 = Color.blue(blue2);
        return Color.argb(255, Math.min(255, (int)(red + (red2 - red) * min)), Math.min(255, (int)(green + (green2 - green) * min)), Math.min(255, (int)(blue + (blue2 - blue) * min)));
    }
    
    private void setDragging(final boolean dragging, final boolean b) {
        if (this.dragging == dragging) {
            return;
        }
        this.dragging = dragging;
        float draggingFactor;
        if (this.dragging) {
            draggingFactor = 1.0f;
        }
        else {
            draggingFactor = 0.0f;
        }
        if (b) {
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "draggingFactor", new float[] { this.draggingFactor, draggingFactor });
            ((ValueAnimator)ofFloat).setInterpolator((TimeInterpolator)this.interpolator);
            int n = 300;
            if (this.wasChangingWeight) {
                n = (int)(300 + this.weight * 75.0f);
            }
            ofFloat.setDuration((long)n);
            ofFloat.start();
        }
        else {
            this.setDraggingFactor(draggingFactor);
        }
    }
    
    private void setDraggingFactor(final float draggingFactor) {
        this.draggingFactor = draggingFactor;
        this.invalidate();
    }
    
    public int colorForLocation(final float n) {
        if (n <= 0.0f) {
            return ColorPicker.COLORS[0];
        }
        int n2 = 1;
        if (n >= 1.0f) {
            final int[] colors = ColorPicker.COLORS;
            return colors[colors.length - 1];
        }
        int n3;
        while (true) {
            final float[] locations = ColorPicker.LOCATIONS;
            final int length = locations.length;
            n3 = -1;
            if (n2 >= length) {
                n2 = -1;
                break;
            }
            if (locations[n2] > n) {
                n3 = n2 - 1;
                break;
            }
            ++n2;
        }
        final float[] locations2 = ColorPicker.LOCATIONS;
        final float n4 = locations2[n3];
        final int[] colors2 = ColorPicker.COLORS;
        return this.interpolateColors(colors2[n3], colors2[n2], (n - n4) / (locations2[n2] - n4));
    }
    
    public float getDraggingFactor() {
        return this.draggingFactor;
    }
    
    public View getSettingsButton() {
        return (View)this.settingsButton;
    }
    
    public Swatch getSwatch() {
        return new Swatch(this.colorForLocation(this.location), this.location, this.weight);
    }
    
    protected void onDraw(final Canvas canvas) {
        canvas.drawRoundRect(this.rectF, (float)AndroidUtilities.dp(6.0f), (float)AndroidUtilities.dp(6.0f), this.gradientPaint);
        final RectF rectF = this.rectF;
        final int n = (int)(rectF.left + rectF.width() * this.location);
        final float centerY = this.rectF.centerY();
        final float draggingFactor = this.draggingFactor;
        final float n2 = (float)(-AndroidUtilities.dp(70.0f));
        float n3;
        if (this.changingWeight) {
            n3 = this.weight * AndroidUtilities.dp(190.0f);
        }
        else {
            n3 = 0.0f;
        }
        final int n4 = (int)(centerY + draggingFactor * n2 - n3);
        final int n5 = (int)(AndroidUtilities.dp(24.0f) * ((this.draggingFactor + 1.0f) * 0.5f));
        this.shadowDrawable.setBounds(n - n5, n4 - n5, n + n5, n5 + n4);
        this.shadowDrawable.draw(canvas);
        final float n6 = (int)Math.floor(AndroidUtilities.dp(4.0f) + (AndroidUtilities.dp(19.0f) - AndroidUtilities.dp(4.0f)) * this.weight) * (this.draggingFactor + 1.0f) / 2.0f;
        final float n7 = (float)n;
        final float n8 = (float)n4;
        canvas.drawCircle(n7, n8, AndroidUtilities.dp(22.0f) / 2 * (this.draggingFactor + 1.0f), this.backgroundPaint);
        canvas.drawCircle(n7, n8, n6, this.swatchPaint);
        canvas.drawCircle(n7, n8, n6 - AndroidUtilities.dp(0.5f), this.swatchStrokePaint);
    }
    
    @SuppressLint({ "DrawAllocation" })
    protected void onLayout(final boolean b, int n, int n2, int n3, final int n4) {
        n = n3 - n;
        n2 = n4 - n2;
        this.gradientPaint.setShader((Shader)new LinearGradient((float)AndroidUtilities.dp(56.0f), 0.0f, (float)(n - AndroidUtilities.dp(56.0f)), 0.0f, ColorPicker.COLORS, ColorPicker.LOCATIONS, Shader$TileMode.REPEAT));
        n3 = n2 - AndroidUtilities.dp(32.0f);
        this.rectF.set((float)AndroidUtilities.dp(56.0f), (float)n3, (float)(n - AndroidUtilities.dp(56.0f)), (float)(n3 + AndroidUtilities.dp(12.0f)));
        final ImageView settingsButton = this.settingsButton;
        settingsButton.layout(n - settingsButton.getMeasuredWidth(), n2 - AndroidUtilities.dp(52.0f), n, n2);
        this.undoButton.layout(0, n2 - AndroidUtilities.dp(52.0f), this.settingsButton.getMeasuredWidth(), n2);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() > 1) {
            return false;
        }
        final float x = motionEvent.getX();
        final float left = this.rectF.left;
        final float n = motionEvent.getY() - this.rectF.top;
        if (!this.interacting && n < -AndroidUtilities.dp(10.0f)) {
            return false;
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 3 && actionMasked != 1 && actionMasked != 6) {
            if (actionMasked == 0 || actionMasked == 2) {
                if (!this.interacting) {
                    this.interacting = true;
                    final ColorPickerDelegate delegate = this.delegate;
                    if (delegate != null) {
                        delegate.onBeganColorPicking();
                    }
                }
                this.setLocation(Math.max(0.0f, Math.min(1.0f, (x - left) / this.rectF.width())));
                this.setDragging(true, true);
                if (n < -AndroidUtilities.dp(10.0f)) {
                    this.changingWeight = true;
                    this.setWeight(Math.max(0.0f, Math.min(1.0f, (-n - AndroidUtilities.dp(10.0f)) / AndroidUtilities.dp(190.0f))));
                }
                final ColorPickerDelegate delegate2 = this.delegate;
                if (delegate2 != null) {
                    delegate2.onColorValueChanged();
                }
                return true;
            }
        }
        else {
            if (this.interacting) {
                final ColorPickerDelegate delegate3 = this.delegate;
                if (delegate3 != null) {
                    delegate3.onFinishedColorPicking();
                    this.getContext().getSharedPreferences("paint", 0).edit().putFloat("last_color_location", this.location).commit();
                }
            }
            this.interacting = false;
            this.wasChangingWeight = this.changingWeight;
            this.setDragging(this.changingWeight = false, true);
        }
        return false;
    }
    
    public void setDelegate(final ColorPickerDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setLocation(final float location) {
        this.location = location;
        final int colorForLocation = this.colorForLocation(location);
        this.swatchPaint.setColor(colorForLocation);
        final float[] array = new float[3];
        Color.colorToHSV(colorForLocation, array);
        if (array[0] < 0.001 && array[1] < 0.001 && array[2] > 0.92f) {
            final int n = (int)((1.0f - (array[2] - 0.92f) / 0.08f * 0.22f) * 255.0f);
            this.swatchStrokePaint.setColor(Color.rgb(n, n, n));
        }
        else {
            this.swatchStrokePaint.setColor(colorForLocation);
        }
        this.invalidate();
    }
    
    public void setSettingsButtonImage(final int imageResource) {
        this.settingsButton.setImageResource(imageResource);
    }
    
    public void setSwatch(final Swatch swatch) {
        this.setLocation(swatch.colorLocation);
        this.setWeight(swatch.brushWeight);
    }
    
    public void setUndoEnabled(final boolean enabled) {
        final ImageView undoButton = this.undoButton;
        float alpha;
        if (enabled) {
            alpha = 1.0f;
        }
        else {
            alpha = 0.3f;
        }
        undoButton.setAlpha(alpha);
        this.undoButton.setEnabled(enabled);
    }
    
    public void setWeight(final float weight) {
        this.weight = weight;
        this.invalidate();
    }
    
    public interface ColorPickerDelegate
    {
        void onBeganColorPicking();
        
        void onColorValueChanged();
        
        void onFinishedColorPicking();
        
        void onSettingsPressed();
        
        void onUndoPressed();
    }
}

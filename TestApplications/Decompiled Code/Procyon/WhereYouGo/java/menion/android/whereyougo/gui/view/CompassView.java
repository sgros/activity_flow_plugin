// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.view;

import menion.android.whereyougo.utils.A;
import android.graphics.Typeface;
import android.graphics.Color;
import menion.android.whereyougo.utils.Utils;
import android.graphics.Paint$Align;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.UtilsFormat;
import menion.android.whereyougo.preferences.Locale;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;

public class CompassView extends View
{
    private Drawable bitCompassArrow;
    private Drawable bitCompassBg;
    private float cX1;
    private float cY1;
    private int lastWidth;
    private float mAzimuth;
    private float mAzimuthToTarget;
    private double mDistanceToTarget;
    private Paint paintValueAzimuth;
    private Paint paintValueDistance;
    private Paint paintValueLabel;
    private Paint paintValueTilt;
    private float r1;
    
    public CompassView(final Context context) {
        super(context);
        this.initialize();
    }
    
    public CompassView(final Context context, final AttributeSet set) {
        super(context, set);
        this.initialize();
    }
    
    private void drawCompassTexts(final Canvas canvas) {
        final float n = this.r1 / 20.0f;
        canvas.drawText(Locale.getString(2131165195), this.cX1, this.cY1 - this.paintValueDistance.getTextSize() - n, this.paintValueLabel);
        canvas.drawText(UtilsFormat.formatDistance(this.mDistanceToTarget, false), this.cX1, this.cY1 - n, this.paintValueDistance);
        canvas.drawText(Locale.getString(2131165189), this.cX1, this.cY1 + this.paintValueLabel.getTextSize() + n, this.paintValueLabel);
        canvas.drawText(UtilsFormat.formatAngle(this.mAzimuthToTarget - this.mAzimuth), this.cX1, this.cY1 + this.paintValueLabel.getTextSize() + this.paintValueAzimuth.getTextSize() + n, this.paintValueAzimuth);
    }
    
    private void initialize() {
        this.mAzimuth = 0.0f;
        this.mAzimuthToTarget = 0.0f;
        this.bitCompassBg = Images.getImageD(2130837574);
        this.bitCompassArrow = Images.getImageD(2130837575);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        (this.paintValueLabel = new Paint()).setAntiAlias(true);
        this.paintValueLabel.setTextAlign(Paint$Align.CENTER);
        this.paintValueLabel.setColor(-1);
        this.paintValueLabel.setTextSize(Utils.getDpPixels(12.0f));
        this.paintValueDistance = new Paint(this.paintValueLabel);
        this.paintValueAzimuth = new Paint(this.paintValueDistance);
        (this.paintValueTilt = new Paint(this.paintValueDistance)).setColor(Color.parseColor("#00a2e6"));
        this.paintValueTilt.setTypeface(Typeface.DEFAULT_BOLD);
        this.paintValueTilt.setShadowLayer(Utils.getDpPixels(3.0f), 0.0f, 0.0f, -16777216);
    }
    
    private void setConstants(final Canvas canvas) {
        if (this.lastWidth != canvas.getWidth()) {
            this.lastWidth = canvas.getWidth();
            final int width = canvas.getClipBounds().width();
            final int height = canvas.getClipBounds().height();
            this.r1 = Math.min(width, height) / 2.0f * 0.9f;
            this.cX1 = width / 2.0f;
            this.cY1 = height / 2.0f;
            this.paintValueDistance.setTextSize(this.r1 / 5.0f);
            this.paintValueAzimuth.setTextSize(this.r1 / 6.0f);
            this.paintValueTilt.setTextSize(this.r1 / 8.0f);
        }
    }
    
    public void draw(final Canvas constants) {
        super.draw(constants);
        this.setConstants(constants);
        constants.save();
        constants.translate(this.cX1, this.cY1);
        constants.rotate(-this.mAzimuth);
        this.bitCompassBg.setBounds((int)(-this.r1), (int)(-this.r1), (int)this.r1, (int)this.r1);
        this.bitCompassBg.draw(constants);
        constants.restore();
        if (A.getGuidingContent().isGuiding()) {
            constants.save();
            constants.translate(this.cX1, this.cY1);
            constants.rotate(this.mAzimuthToTarget - this.mAzimuth);
            this.bitCompassArrow.setBounds((int)(-this.r1), (int)(-this.r1), (int)this.r1, (int)this.r1);
            this.bitCompassArrow.draw(constants);
            constants.restore();
        }
        this.drawCompassTexts(constants);
    }
    
    public void moveAngles(final float mAzimuthToTarget, final float mAzimuth, final float n, final float n2) {
        this.mAzimuthToTarget = mAzimuthToTarget;
        this.mAzimuth = mAzimuth;
        this.invalidate();
    }
    
    public void setDistance(final double mDistanceToTarget) {
        this.mDistanceToTarget = mDistanceToTarget;
        this.invalidate();
    }
}

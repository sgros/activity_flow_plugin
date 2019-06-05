package menion.android.whereyougo.gui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.UtilsFormat;

public class CompassView extends View {
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
    /* renamed from: r1 */
    private float f55r1;

    public CompassView(Context context) {
        super(context);
        initialize();
    }

    public CompassView(Context context, AttributeSet attr) {
        super(context, attr);
        initialize();
    }

    public void draw(Canvas c) {
        super.draw(c);
        setConstants(c);
        c.save();
        c.translate(this.cX1, this.cY1);
        c.rotate(-this.mAzimuth);
        this.bitCompassBg.setBounds((int) (-this.f55r1), (int) (-this.f55r1), (int) this.f55r1, (int) this.f55r1);
        this.bitCompassBg.draw(c);
        c.restore();
        if (C0322A.getGuidingContent().isGuiding()) {
            c.save();
            c.translate(this.cX1, this.cY1);
            c.rotate(this.mAzimuthToTarget - this.mAzimuth);
            this.bitCompassArrow.setBounds((int) (-this.f55r1), (int) (-this.f55r1), (int) this.f55r1, (int) this.f55r1);
            this.bitCompassArrow.draw(c);
            c.restore();
        }
        drawCompassTexts(c);
    }

    private void drawCompassTexts(Canvas c) {
        float space = this.f55r1 / 20.0f;
        c.drawText(Locale.getString(C0254R.string.distance), this.cX1, (this.cY1 - this.paintValueDistance.getTextSize()) - space, this.paintValueLabel);
        c.drawText(UtilsFormat.formatDistance(this.mDistanceToTarget, false), this.cX1, this.cY1 - space, this.paintValueDistance);
        c.drawText(Locale.getString(C0254R.string.azimuth), this.cX1, (this.cY1 + this.paintValueLabel.getTextSize()) + space, this.paintValueLabel);
        c.drawText(UtilsFormat.formatAngle((double) (this.mAzimuthToTarget - this.mAzimuth)), this.cX1, ((this.cY1 + this.paintValueLabel.getTextSize()) + this.paintValueAzimuth.getTextSize()) + space, this.paintValueAzimuth);
    }

    private void initialize() {
        this.mAzimuth = 0.0f;
        this.mAzimuthToTarget = 0.0f;
        this.bitCompassBg = Images.getImageD((int) C0254R.C0252drawable.var_compass);
        this.bitCompassArrow = Images.getImageD((int) C0254R.C0252drawable.var_compass_arrow);
        Paint mPaintBitmap = new Paint();
        mPaintBitmap.setAntiAlias(true);
        mPaintBitmap.setFilterBitmap(true);
        this.paintValueLabel = new Paint();
        this.paintValueLabel.setAntiAlias(true);
        this.paintValueLabel.setTextAlign(Align.CENTER);
        this.paintValueLabel.setColor(-1);
        this.paintValueLabel.setTextSize(Utils.getDpPixels(12.0f));
        this.paintValueDistance = new Paint(this.paintValueLabel);
        this.paintValueAzimuth = new Paint(this.paintValueDistance);
        this.paintValueTilt = new Paint(this.paintValueDistance);
        this.paintValueTilt.setColor(Color.parseColor("#00a2e6"));
        this.paintValueTilt.setTypeface(Typeface.DEFAULT_BOLD);
        this.paintValueTilt.setShadowLayer(Utils.getDpPixels(3.0f), 0.0f, 0.0f, -16777216);
    }

    public void moveAngles(float azimuthToTarget, float azimuth, float pitch, float roll) {
        this.mAzimuthToTarget = azimuthToTarget;
        this.mAzimuth = azimuth;
        invalidate();
    }

    private void setConstants(Canvas c) {
        if (this.lastWidth != c.getWidth()) {
            this.lastWidth = c.getWidth();
            int w = c.getClipBounds().width();
            int h = c.getClipBounds().height();
            this.f55r1 = (((float) Math.min(w, h)) / 2.0f) * 0.9f;
            this.cX1 = ((float) w) / 2.0f;
            this.cY1 = ((float) h) / 2.0f;
            this.paintValueDistance.setTextSize(this.f55r1 / 5.0f);
            this.paintValueAzimuth.setTextSize(this.f55r1 / 6.0f);
            this.paintValueTilt.setTextSize(this.f55r1 / 8.0f);
        }
    }

    public void setDistance(double distance) {
        this.mDistanceToTarget = distance;
        invalidate();
    }
}

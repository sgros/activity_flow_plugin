package menion.android.whereyougo.gui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.geo.location.SatellitePosition;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;

public class Satellite2DView extends View {
    private static final float SAT_TEXT_SIZE = Utils.getDpPixels(10.0f);
    private static final String TAG = "Satellite2DView";
    private Drawable bitCompassBg;
    private Bitmap bitSnr;
    private boolean drawLock;
    private int lastWidth;
    private float lineWidth;
    private Paint mPaintBitmap;
    private Paint mPaintSignalLine;
    private Paint mPaintText;
    /* renamed from: r1 */
    private float f56r1;
    private Bitmap[] satImages;
    private final ArrayList<SatellitePosition> satellites;
    private float satsPanelHeigh;
    private float snrWidth;
    private float spSize;
    private float spX;
    private float spY;
    private float space;

    public Satellite2DView(Context context, ArrayList<SatellitePosition> satellites) {
        super(context);
        setBasics();
        this.satellites = satellites;
    }

    public Satellite2DView(Context context, AttributeSet att, ArrayList<SatellitePosition> satellites) {
        super(context, att);
        setBasics();
        this.satellites = satellites;
    }

    private Bitmap getSatImage(float snr) {
        if (snr < 25.0f) {
            return this.satImages[0];
        }
        if (snr < 20.0f || snr >= 40.0f) {
            return this.satImages[2];
        }
        return this.satImages[1];
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas c) {
        if (!this.drawLock) {
            try {
                this.drawLock = true;
                setConstants(c);
                int satCount = this.satellites.size();
                this.bitCompassBg.setBounds((int) (this.spX - this.f56r1), (int) (this.spY - this.f56r1), (int) (this.spX + this.f56r1), (int) (this.spY + this.f56r1));
                this.bitCompassBg.draw(c);
                this.mPaintText.setColor(-16777216);
                if (satCount == 0) {
                    this.mPaintText.setTextSize(Utils.getDpPixels(20.0f));
                    c.drawText(Locale.getString(C0254R.string.no_satellites), this.spX, this.spY + this.mPaintText.descent(), this.mPaintText);
                    this.drawLock = false;
                    return;
                }
                this.mPaintText.setTextSize(SAT_TEXT_SIZE);
                int snrHeight = this.bitSnr.getHeight();
                c.drawLine(0.0f, ((float) snrHeight) + this.spSize, (float) c.getClipBounds().width(), ((float) snrHeight) + this.spSize, this.mPaintSignalLine);
                double ln100 = Math.log(100.0d);
                for (int i = 0; i < satCount; i++) {
                    float xCenter;
                    SatellitePosition sat = (SatellitePosition) this.satellites.get(i);
                    if (satCount % 2 == 0) {
                        xCenter = (this.spX + (((float) (i - (satCount / 2))) * this.lineWidth)) + (this.lineWidth / 2.0f);
                    } else {
                        xCenter = this.spX + (((float) (i - ((satCount - 1) / 2))) * this.lineWidth);
                    }
                    if (sat.isFixed()) {
                        this.mPaintText.setColor(-16711936);
                    } else {
                        this.mPaintText.setColor(-3355444);
                    }
                    c.drawText((sat.getPrn().intValue() < 10 ? "0" : "") + sat.getPrn(), xCenter, this.spSize + this.satsPanelHeigh, this.mPaintText);
                    int height = (int) ((Math.log(sat.getSnr() > 0 ? (double) sat.getSnr() : 1.0d) / ln100) * ((double) snrHeight));
                    if (height <= 0) {
                        height = 1;
                    }
                    c.drawBitmap(Bitmap.createBitmap(this.bitSnr, 0, snrHeight - height, this.bitSnr.getWidth(), height), xCenter - (this.snrWidth / 2.0f), (this.spSize + ((float) snrHeight)) - ((float) height), this.mPaintBitmap);
                    float angle = sat.getAzimuth();
                    float dist = ((float) (((double) this.f56r1) - (Math.sin((double) (sat.getElevation() / 57.29578f)) * ((double) this.f56r1)))) * 0.9f;
                    float x = (float) (((double) this.spX) + (((double) dist) * Math.sin((double) (angle / 57.29578f))));
                    float y = (float) (((double) this.spY) - (((double) dist) * Math.cos((double) (angle / 57.29578f))));
                    this.mPaintText.setColor(-16777216);
                    Bitmap imgSat = getSatImage((float) sat.getSnr());
                    c.drawText("" + sat.getPrn(), x, (y - ((float) (imgSat.getHeight() / 2))) - 5.0f, this.mPaintText);
                    c.drawBitmap(imgSat, x - ((float) (imgSat.getWidth() / 2)), y - ((float) (imgSat.getHeight() / 2)), this.mPaintBitmap);
                }
                this.drawLock = false;
            } catch (Exception e) {
                Logger.m22e(TAG, "onDraw()", e);
            }
        }
    }

    private void setBasics() {
        this.drawLock = false;
        this.space = Utils.getDpPixels(6.0f);
        this.bitCompassBg = Images.getImageD((int) C0254R.C0252drawable.var_skyplot);
        int imgSize = (int) Utils.getDpPixels(20.0f);
        this.satImages = new Bitmap[3];
        this.satImages[0] = Images.getImageB(C0254R.C0252drawable.ic_sat_01, imgSize);
        this.satImages[1] = Images.getImageB(C0254R.C0252drawable.ic_sat_02, imgSize);
        this.satImages[2] = Images.getImageB(C0254R.C0252drawable.ic_sat_03, imgSize);
        this.mPaintBitmap = new Paint();
        this.mPaintBitmap.setAntiAlias(true);
        this.mPaintBitmap.setFilterBitmap(true);
        this.mPaintText = new Paint();
        this.mPaintText.setAntiAlias(true);
        this.mPaintText.setTextAlign(Align.CENTER);
        this.mPaintText.setTextSize(SAT_TEXT_SIZE);
        this.mPaintText.setShadowLayer(SAT_TEXT_SIZE / 4.0f, 0.0f, 0.0f, -1);
        this.mPaintSignalLine = new Paint();
        this.mPaintSignalLine.setAntiAlias(true);
        this.mPaintSignalLine.setStyle(Style.STROKE);
        this.mPaintSignalLine.setStrokeWidth(2.0f);
        this.mPaintSignalLine.setColor(-7829368);
    }

    private void setConstants(Canvas c) {
        if (this.lastWidth != c.getWidth()) {
            this.lastWidth = c.getWidth();
            int w = c.getClipBounds().width();
            int h = c.getClipBounds().height();
            this.lineWidth = (((float) w) - (this.space * 2.0f)) / 20.0f;
            this.snrWidth = this.lineWidth - 2.0f;
            this.bitSnr = Images.getImageB(C0254R.C0252drawable.var_skyplot_bar, (int) this.snrWidth);
            this.satsPanelHeigh = (((float) this.bitSnr.getHeight()) + this.space) + this.mPaintText.getTextSize();
            this.spSize = Math.min((float) w, (((float) h) - this.satsPanelHeigh) - this.space);
            this.f56r1 = (this.spSize / 2.0f) * 0.95f;
            this.spX = (float) (c.getClipBounds().width() / 2);
            this.spY = this.spSize / 2.0f;
        }
    }
}

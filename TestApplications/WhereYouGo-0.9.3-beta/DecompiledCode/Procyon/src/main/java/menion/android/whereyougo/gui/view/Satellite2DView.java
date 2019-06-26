// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.view;

import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.preferences.Locale;
import android.graphics.Canvas;
import android.graphics.Paint$Style;
import android.graphics.Paint$Align;
import menion.android.whereyougo.utils.Images;
import android.util.AttributeSet;
import android.content.Context;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.geo.location.SatellitePosition;
import java.util.ArrayList;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Satellite2DView extends View
{
    private static final float SAT_TEXT_SIZE;
    private static final String TAG = "Satellite2DView";
    private Drawable bitCompassBg;
    private Bitmap bitSnr;
    private boolean drawLock;
    private int lastWidth;
    private float lineWidth;
    private Paint mPaintBitmap;
    private Paint mPaintSignalLine;
    private Paint mPaintText;
    private float r1;
    private Bitmap[] satImages;
    private final ArrayList<SatellitePosition> satellites;
    private float satsPanelHeigh;
    private float snrWidth;
    private float spSize;
    private float spX;
    private float spY;
    private float space;
    
    static {
        SAT_TEXT_SIZE = Utils.getDpPixels(10.0f);
    }
    
    public Satellite2DView(final Context context, final AttributeSet set, final ArrayList<SatellitePosition> satellites) {
        super(context, set);
        this.setBasics();
        this.satellites = satellites;
    }
    
    public Satellite2DView(final Context context, final ArrayList<SatellitePosition> satellites) {
        super(context);
        this.setBasics();
        this.satellites = satellites;
    }
    
    private Bitmap getSatImage(final float n) {
        Bitmap bitmap;
        if (n < 25.0f) {
            bitmap = this.satImages[0];
        }
        else if (n >= 20.0f && n < 40.0f) {
            bitmap = this.satImages[1];
        }
        else {
            bitmap = this.satImages[2];
        }
        return bitmap;
    }
    
    private void setBasics() {
        this.drawLock = false;
        this.space = Utils.getDpPixels(6.0f);
        this.bitCompassBg = Images.getImageD(2130837582);
        final int n = (int)Utils.getDpPixels(20.0f);
        (this.satImages = new Bitmap[3])[0] = Images.getImageB(2130837548, n);
        this.satImages[1] = Images.getImageB(2130837549, n);
        this.satImages[2] = Images.getImageB(2130837550, n);
        (this.mPaintBitmap = new Paint()).setAntiAlias(true);
        this.mPaintBitmap.setFilterBitmap(true);
        (this.mPaintText = new Paint()).setAntiAlias(true);
        this.mPaintText.setTextAlign(Paint$Align.CENTER);
        this.mPaintText.setTextSize(Satellite2DView.SAT_TEXT_SIZE);
        this.mPaintText.setShadowLayer(Satellite2DView.SAT_TEXT_SIZE / 4.0f, 0.0f, 0.0f, -1);
        (this.mPaintSignalLine = new Paint()).setAntiAlias(true);
        this.mPaintSignalLine.setStyle(Paint$Style.STROKE);
        this.mPaintSignalLine.setStrokeWidth(2.0f);
        this.mPaintSignalLine.setColor(-7829368);
    }
    
    private void setConstants(final Canvas canvas) {
        if (this.lastWidth != canvas.getWidth()) {
            this.lastWidth = canvas.getWidth();
            final int width = canvas.getClipBounds().width();
            final int height = canvas.getClipBounds().height();
            this.lineWidth = (width - this.space * 2.0f) / 20.0f;
            this.snrWidth = this.lineWidth - 2.0f;
            this.bitSnr = Images.getImageB(2130837583, (int)this.snrWidth);
            this.satsPanelHeigh = this.bitSnr.getHeight() + this.space + this.mPaintText.getTextSize();
            this.spSize = Math.min((float)width, height - this.satsPanelHeigh - this.space);
            this.r1 = this.spSize / 2.0f * 0.95f;
            this.spX = (float)(canvas.getClipBounds().width() / 2);
            this.spY = this.spSize / 2.0f;
        }
    }
    
    protected void onDraw(final Canvas constants) {
        if (!this.drawLock) {
            while (true) {
                int size = 0;
                Label_0160: {
                    try {
                        this.drawLock = true;
                        this.setConstants(constants);
                        size = this.satellites.size();
                        this.bitCompassBg.setBounds((int)(this.spX - this.r1), (int)(this.spY - this.r1), (int)(this.spX + this.r1), (int)(this.spY + this.r1));
                        this.bitCompassBg.draw(constants);
                        this.mPaintText.setColor(-16777216);
                        if (size == 0) {
                            this.mPaintText.setTextSize(Utils.getDpPixels(20.0f));
                            constants.drawText(Locale.getString(2131165227), this.spX, this.spY + this.mPaintText.descent(), this.mPaintText);
                            this.drawLock = false;
                            return;
                        }
                        break Label_0160;
                    }
                    catch (Exception ex) {
                        Logger.e("Satellite2DView", "onDraw()", ex);
                    }
                    this.drawLock = false;
                    return;
                }
                this.mPaintText.setTextSize(Satellite2DView.SAT_TEXT_SIZE);
                final int height = this.bitSnr.getHeight();
                constants.drawLine(0.0f, height + this.spSize, (float)constants.getClipBounds().width(), height + this.spSize, this.mPaintSignalLine);
                final double log = Math.log(100.0);
                for (int i = 0; i < size; ++i) {
                    final SatellitePosition satellitePosition = this.satellites.get(i);
                    float n;
                    if (size % 2 == 0) {
                        n = this.spX + (i - size / 2) * this.lineWidth + this.lineWidth / 2.0f;
                    }
                    else {
                        n = this.spX + (i - (size - 1) / 2) * this.lineWidth;
                    }
                    if (satellitePosition.isFixed()) {
                        this.mPaintText.setColor(-16711936);
                    }
                    else {
                        this.mPaintText.setColor(-3355444);
                    }
                    final StringBuilder sb = new StringBuilder();
                    String str;
                    if (satellitePosition.getPrn() < 10) {
                        str = "0";
                    }
                    else {
                        str = "";
                    }
                    constants.drawText(sb.append(str).append(satellitePosition.getPrn()).toString(), n, this.spSize + this.satsPanelHeigh, this.mPaintText);
                    double a;
                    if (satellitePosition.getSnr() > 0) {
                        a = satellitePosition.getSnr();
                    }
                    else {
                        a = 1.0;
                    }
                    int n2;
                    if ((n2 = (int)(Math.log(a) / log * height)) <= 0) {
                        n2 = 1;
                    }
                    constants.drawBitmap(Bitmap.createBitmap(this.bitSnr, 0, height - n2, this.bitSnr.getWidth(), n2), n - this.snrWidth / 2.0f, this.spSize + height - n2, this.mPaintBitmap);
                    final float azimuth = satellitePosition.getAzimuth();
                    final float n3 = (float)(this.r1 - Math.sin(satellitePosition.getElevation() / 57.29578f) * this.r1) * 0.9f;
                    final float n4 = (float)(this.spX + n3 * Math.sin(azimuth / 57.29578f));
                    final float n5 = (float)(this.spY - n3 * Math.cos(azimuth / 57.29578f));
                    this.mPaintText.setColor(-16777216);
                    final Bitmap satImage = this.getSatImage((float)satellitePosition.getSnr());
                    constants.drawText("" + satellitePosition.getPrn(), n4, n5 - satImage.getHeight() / 2 - 5.0f, this.mPaintText);
                    constants.drawBitmap(satImage, n4 - satImage.getWidth() / 2, n5 - satImage.getHeight() / 2, this.mPaintBitmap);
                }
                continue;
            }
        }
    }
}

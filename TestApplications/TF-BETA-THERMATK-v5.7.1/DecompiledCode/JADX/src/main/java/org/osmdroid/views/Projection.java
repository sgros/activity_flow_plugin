package org.osmdroid.views;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IProjection;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.GeometryMath;
import org.osmdroid.util.PointL;
import org.osmdroid.util.RectL;
import org.osmdroid.util.TileSystem;

public class Projection implements IProjection {
    private boolean horizontalWrapEnabled;
    private final BoundingBox mBoundingBoxProjection;
    private final GeoPoint mCurrentCenter;
    private final Rect mIntrinsicScreenRectProjection;
    private final double mMercatorMapSize;
    private long mOffsetX;
    private long mOffsetY;
    private final float mOrientation;
    public final double mProjectedMapSize;
    private final Matrix mRotateAndScaleMatrix;
    private final float[] mRotateScalePoints;
    private final Rect mScreenRectProjection;
    private long mScrollX;
    private long mScrollY;
    private final double mTileSize;
    private final TileSystem mTileSystem;
    private final Matrix mUnrotateAndScaleMatrix;
    private final double mZoomLevelProjection;
    private boolean verticalWrapEnabled;

    public void detach() {
    }

    Projection(MapView mapView) {
        this(mapView.getZoomLevelDouble(), mapView.getIntrinsicScreenRect(null), mapView.getExpectedCenter(), mapView.getMapScrollX(), mapView.getMapScrollY(), mapView.getMapOrientation(), mapView.isHorizontalMapRepetitionEnabled(), mapView.isVerticalMapRepetitionEnabled(), MapView.getTileSystem());
    }

    public Projection(double d, Rect rect, GeoPoint geoPoint, long j, long j2, float f, boolean z, boolean z2, TileSystem tileSystem) {
        GeoPoint geoPoint2;
        this.mProjectedMapSize = TileSystem.MapSize(30.0d);
        this.mRotateAndScaleMatrix = new Matrix();
        this.mUnrotateAndScaleMatrix = new Matrix();
        this.mRotateScalePoints = new float[2];
        this.mBoundingBoxProjection = new BoundingBox();
        this.mScreenRectProjection = new Rect();
        this.mCurrentCenter = new GeoPoint(0.0d, 0.0d);
        this.mZoomLevelProjection = d;
        this.horizontalWrapEnabled = z;
        this.verticalWrapEnabled = z2;
        this.mTileSystem = tileSystem;
        this.mMercatorMapSize = TileSystem.MapSize(this.mZoomLevelProjection);
        this.mTileSize = TileSystem.getTileSize(this.mZoomLevelProjection);
        this.mIntrinsicScreenRectProjection = rect;
        if (geoPoint != null) {
            geoPoint2 = geoPoint;
        } else {
            geoPoint2 = new GeoPoint(0.0d, 0.0d);
        }
        this.mScrollX = j;
        this.mScrollY = j2;
        this.mOffsetX = (((long) getScreenCenterX()) - this.mScrollX) - this.mTileSystem.getMercatorXFromLongitude(geoPoint2.getLongitude(), this.mMercatorMapSize, this.horizontalWrapEnabled);
        this.mOffsetY = (((long) getScreenCenterY()) - this.mScrollY) - this.mTileSystem.getMercatorYFromLatitude(geoPoint2.getLatitude(), this.mMercatorMapSize, this.verticalWrapEnabled);
        this.mOrientation = f;
        this.mRotateAndScaleMatrix.preRotate(this.mOrientation, (float) getScreenCenterX(), (float) getScreenCenterY());
        this.mRotateAndScaleMatrix.invert(this.mUnrotateAndScaleMatrix);
        refresh();
    }

    public Projection(double d, int i, int i2, GeoPoint geoPoint, float f, boolean z, boolean z2) {
        this(d, new Rect(0, 0, i, i2), geoPoint, 0, 0, f, z, z2, MapView.getTileSystem());
    }

    public double getZoomLevel() {
        return this.mZoomLevelProjection;
    }

    public BoundingBox getBoundingBox() {
        return this.mBoundingBoxProjection;
    }

    public IGeoPoint fromPixels(int i, int i2) {
        return fromPixels(i, i2, null, false);
    }

    public IGeoPoint fromPixels(int i, int i2, GeoPoint geoPoint) {
        return fromPixels(i, i2, geoPoint, false);
    }

    public IGeoPoint fromPixels(int i, int i2, GeoPoint geoPoint, boolean z) {
        TileSystem tileSystem = this.mTileSystem;
        long cleanMercator = getCleanMercator(getMercatorXFromPixel(i), this.horizontalWrapEnabled);
        long cleanMercator2 = getCleanMercator(getMercatorYFromPixel(i2), this.verticalWrapEnabled);
        double d = this.mMercatorMapSize;
        boolean z2 = this.horizontalWrapEnabled || z;
        boolean z3 = this.verticalWrapEnabled || z;
        return tileSystem.getGeoFromMercator(cleanMercator, cleanMercator2, d, geoPoint, z2, z3);
    }

    public Point toPixels(IGeoPoint iGeoPoint, Point point) {
        return toPixels(iGeoPoint, point, false);
    }

    public Point toPixels(IGeoPoint iGeoPoint, Point point, boolean z) {
        if (point == null) {
            point = new Point();
        }
        point.x = TileSystem.truncateToInt(getLongPixelXFromLongitude(iGeoPoint.getLongitude(), z));
        point.y = TileSystem.truncateToInt(getLongPixelYFromLatitude(iGeoPoint.getLatitude(), z));
        return point;
    }

    public long getLongPixelXFromLongitude(double d, boolean z) {
        TileSystem tileSystem = this.mTileSystem;
        double d2 = this.mMercatorMapSize;
        boolean z2 = this.horizontalWrapEnabled || z;
        return getLongPixelXFromMercator(tileSystem.getMercatorXFromLongitude(d, d2, z2), this.horizontalWrapEnabled);
    }

    public long getLongPixelXFromLongitude(double d) {
        return getLongPixelXFromMercator(this.mTileSystem.getMercatorXFromLongitude(d, this.mMercatorMapSize, false), false);
    }

    public long getLongPixelYFromLatitude(double d, boolean z) {
        TileSystem tileSystem = this.mTileSystem;
        double d2 = this.mMercatorMapSize;
        boolean z2 = this.verticalWrapEnabled || z;
        return getLongPixelYFromMercator(tileSystem.getMercatorYFromLatitude(d, d2, z2), this.verticalWrapEnabled);
    }

    public long getLongPixelYFromLatitude(double d) {
        return getLongPixelYFromMercator(this.mTileSystem.getMercatorYFromLatitude(d, this.mMercatorMapSize, false), false);
    }

    public PointL toMercatorPixels(int i, int i2, PointL pointL) {
        if (pointL == null) {
            pointL = new PointL();
        }
        pointL.f44x = getCleanMercator(getMercatorXFromPixel(i), this.horizontalWrapEnabled);
        pointL.f45y = getCleanMercator(getMercatorYFromPixel(i2), this.verticalWrapEnabled);
        return pointL;
    }

    public Matrix getInvertedScaleRotateCanvasMatrix() {
        return this.mUnrotateAndScaleMatrix;
    }

    public Point unrotateAndScalePoint(int i, int i2, Point point) {
        return applyMatrixToPoint(i, i2, point, this.mUnrotateAndScaleMatrix, this.mOrientation != 0.0f);
    }

    public Point rotateAndScalePoint(int i, int i2, Point point) {
        return applyMatrixToPoint(i, i2, point, this.mRotateAndScaleMatrix, this.mOrientation != 0.0f);
    }

    private Point applyMatrixToPoint(int i, int i2, Point point, Matrix matrix, boolean z) {
        if (point == null) {
            point = new Point();
        }
        if (z) {
            float[] fArr = this.mRotateScalePoints;
            fArr[0] = (float) i;
            fArr[1] = (float) i2;
            matrix.mapPoints(fArr);
            float[] fArr2 = this.mRotateScalePoints;
            point.x = (int) fArr2[0];
            point.y = (int) fArr2[1];
        } else {
            point.x = i;
            point.y = i2;
        }
        return point;
    }

    public Rect getPixelFromTile(int i, int i2, Rect rect) {
        if (rect == null) {
            rect = new Rect();
        }
        rect.left = TileSystem.truncateToInt(getLongPixelXFromMercator(getMercatorFromTile(i), false));
        rect.top = TileSystem.truncateToInt(getLongPixelYFromMercator(getMercatorFromTile(i2), false));
        rect.right = TileSystem.truncateToInt(getLongPixelXFromMercator(getMercatorFromTile(i + 1), false));
        rect.bottom = TileSystem.truncateToInt(getLongPixelYFromMercator(getMercatorFromTile(i2 + 1), false));
        return rect;
    }

    public long getMercatorFromTile(int i) {
        return TileSystem.getMercatorFromTile(i, this.mTileSize);
    }

    private long getCloserPixel(long j, int i, int i2, double d) {
        long j2 = (long) ((i + i2) / 2);
        long j3 = (long) i;
        long j4 = 0;
        long j5;
        double d2;
        if (j < j3) {
            while (true) {
                j5 = j;
                j = j4;
                j4 = j5;
                if (j4 >= j3) {
                    break;
                }
                d2 = (double) j4;
                Double.isNaN(d2);
                j = (long) (d2 + d);
            }
            return (j4 >= ((long) i2) && Math.abs(j2 - j4) >= Math.abs(j2 - j)) ? j : j4;
        } else {
            while (true) {
                j5 = j;
                j = j4;
                j4 = j5;
                if (j4 < j3) {
                    break;
                }
                d2 = (double) j4;
                Double.isNaN(d2);
                j = (long) (d2 - d);
            }
            return (j >= ((long) i2) && Math.abs(j2 - j4) < Math.abs(j2 - j)) ? j4 : j;
        }
    }

    private long getLongPixelXFromMercator(long j, boolean z) {
        long j2 = this.mOffsetX;
        Rect rect = this.mIntrinsicScreenRectProjection;
        return getLongPixelFromMercator(j, z, j2, rect.left, rect.right);
    }

    private long getLongPixelYFromMercator(long j, boolean z) {
        long j2 = this.mOffsetY;
        Rect rect = this.mIntrinsicScreenRectProjection;
        return getLongPixelFromMercator(j, z, j2, rect.top, rect.bottom);
    }

    private long getLongPixelFromMercator(long j, boolean z, long j2, int i, int i2) {
        long j3 = j + j2;
        if (!z) {
            return j3;
        }
        return getCloserPixel(j3, i, i2, this.mMercatorMapSize);
    }

    public RectL getMercatorViewPort(RectL rectL) {
        if (rectL == null) {
            rectL = new RectL();
        }
        Rect rect = this.mIntrinsicScreenRectProjection;
        int i = rect.left;
        float f = (float) i;
        int i2 = rect.right;
        float f2 = (float) i2;
        int i3 = rect.top;
        float f3 = (float) i3;
        int i4 = rect.bottom;
        float f4 = (float) i4;
        if (this.mOrientation != 0.0f) {
            r9 = new float[8];
            int i5 = 0;
            r9[0] = (float) i;
            r9[1] = (float) i3;
            r9[2] = (float) i2;
            r9[3] = (float) i4;
            r9[4] = (float) i;
            r9[5] = (float) i4;
            r9[6] = (float) i2;
            r9[7] = (float) i3;
            this.mUnrotateAndScaleMatrix.mapPoints(r9);
            while (i5 < 8) {
                if (f > r9[i5]) {
                    f = r9[i5];
                }
                if (f2 < r9[i5]) {
                    f2 = r9[i5];
                }
                i4 = i5 + 1;
                if (f3 > r9[i4]) {
                    f3 = r9[i4];
                }
                if (f4 < r9[i4]) {
                    f4 = r9[i4];
                }
                i5 += 2;
            }
        }
        rectL.left = getMercatorXFromPixel((int) f);
        rectL.top = getMercatorYFromPixel((int) f3);
        rectL.right = getMercatorXFromPixel((int) f2);
        rectL.bottom = getMercatorYFromPixel((int) f4);
        return rectL;
    }

    public int getScreenCenterX() {
        Rect rect = this.mIntrinsicScreenRectProjection;
        return (rect.right + rect.left) / 2;
    }

    public int getScreenCenterY() {
        Rect rect = this.mIntrinsicScreenRectProjection;
        return (rect.bottom + rect.top) / 2;
    }

    public long getMercatorXFromPixel(int i) {
        return ((long) i) - this.mOffsetX;
    }

    public long getMercatorYFromPixel(int i) {
        return ((long) i) - this.mOffsetY;
    }

    public long getCleanMercator(long j, boolean z) {
        return this.mTileSystem.getCleanMercator(j, this.mMercatorMapSize, z);
    }

    public GeoPoint getCurrentCenter() {
        return this.mCurrentCenter;
    }

    public void save(Canvas canvas, boolean z, boolean z2) {
        if (this.mOrientation != 0.0f || z2) {
            canvas.save();
            canvas.concat(z ? this.mRotateAndScaleMatrix : this.mUnrotateAndScaleMatrix);
        }
    }

    public void restore(Canvas canvas, boolean z) {
        if (this.mOrientation != 0.0f || z) {
            canvas.restore();
        }
    }

    private void refresh() {
        Rect rect = this.mIntrinsicScreenRectProjection;
        fromPixels((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2, this.mCurrentCenter);
        rect = this.mIntrinsicScreenRectProjection;
        IGeoPoint fromPixels = fromPixels(rect.right, rect.top, null, true);
        Rect rect2 = this.mIntrinsicScreenRectProjection;
        IGeoPoint fromPixels2 = fromPixels(rect2.left, rect2.bottom, null, true);
        this.mBoundingBoxProjection.set(fromPixels.getLatitude(), fromPixels.getLongitude(), fromPixels2.getLatitude(), fromPixels2.getLongitude());
        float f = this.mOrientation;
        if (f == 0.0f || f == 180.0f) {
            rect = this.mScreenRectProjection;
            rect2 = this.mIntrinsicScreenRectProjection;
            rect.left = rect2.left;
            rect.top = rect2.top;
            rect.right = rect2.right;
            rect.bottom = rect2.bottom;
            return;
        }
        GeometryMath.getBoundingBoxForRotatatedRectangle(this.mIntrinsicScreenRectProjection, getScreenCenterX(), getScreenCenterY(), this.mOrientation, this.mScreenRectProjection);
    }

    public void adjustOffsets(IGeoPoint iGeoPoint, PointF pointF) {
        if (pointF != null) {
            Point unrotateAndScalePoint = unrotateAndScalePoint((int) pointF.x, (int) pointF.y, null);
            Point toPixels = toPixels(iGeoPoint, null);
            adjustOffsets((long) (unrotateAndScalePoint.x - toPixels.x), (long) (unrotateAndScalePoint.y - toPixels.y));
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void adjustOffsets(double d, double d2, boolean z, int i) {
        long j;
        double d3 = d2;
        long j2 = 0;
        if (z) {
            long scrollableOffset = getScrollableOffset(getLongPixelYFromLatitude(d), getLongPixelYFromLatitude(d3), this.mMercatorMapSize, this.mIntrinsicScreenRectProjection.height(), i);
            j = 0;
            j2 = scrollableOffset;
        } else {
            j = getScrollableOffset(getLongPixelXFromLongitude(d), getLongPixelXFromLongitude(d3), this.mMercatorMapSize, this.mIntrinsicScreenRectProjection.width(), i);
        }
        adjustOffsets(j, j2);
    }

    /* Access modifiers changed, original: 0000 */
    public void adjustOffsets(long j, long j2) {
        if (j != 0 || j2 != 0) {
            this.mOffsetX += j;
            this.mOffsetY += j2;
            this.mScrollX -= j;
            this.mScrollY -= j2;
            refresh();
        }
    }

    public static long getScrollableOffset(long j, long j2, double d, int i, int i2) {
        long j3;
        while (true) {
            j3 = j2 - j;
            if (j3 >= 0) {
                break;
            }
            double d2 = (double) j2;
            Double.isNaN(d2);
            j2 = (long) (d2 + d);
        }
        long j4;
        if (j3 < ((long) (i - (i2 * 2)))) {
            j3 /= 2;
            j4 = (long) (i / 2);
            long j5 = (j4 - j3) - j;
            if (j5 > 0) {
                return j5;
            }
            j4 = (j4 + j3) - j2;
            if (j4 < 0) {
                return j4;
            }
            return 0;
        }
        j4 = ((long) i2) - j;
        if (j4 < 0) {
            return j4;
        }
        j = ((long) (i - i2)) - j2;
        if (j > 0) {
            return j;
        }
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean setMapScroll(MapView mapView) {
        if (mapView.getMapScrollX() == this.mScrollX && mapView.getMapScrollY() == this.mScrollY) {
            return false;
        }
        mapView.setMapScroll(this.mScrollX, this.mScrollY);
        return true;
    }

    public float getOrientation() {
        return this.mOrientation;
    }
}

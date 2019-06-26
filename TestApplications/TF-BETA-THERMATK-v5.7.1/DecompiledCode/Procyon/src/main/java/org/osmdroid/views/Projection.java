// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views;

import org.osmdroid.util.PointL;
import android.graphics.Canvas;
import org.osmdroid.util.RectL;
import android.graphics.PointF;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeometryMath;
import android.graphics.Point;
import org.osmdroid.util.TileSystem;
import android.graphics.Matrix;
import android.graphics.Rect;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.api.IProjection;

public class Projection implements IProjection
{
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
    
    public Projection(final double n, final int n2, final int n3, final GeoPoint geoPoint, final float n4, final boolean b, final boolean b2) {
        this(n, new Rect(0, 0, n2, n3), geoPoint, 0L, 0L, n4, b, b2, MapView.getTileSystem());
    }
    
    public Projection(final double mZoomLevelProjection, final Rect mIntrinsicScreenRectProjection, GeoPoint geoPoint, final long mScrollX, final long mScrollY, final float mOrientation, final boolean horizontalWrapEnabled, final boolean verticalWrapEnabled, final TileSystem mTileSystem) {
        this.mProjectedMapSize = TileSystem.MapSize(30.0);
        this.mRotateAndScaleMatrix = new Matrix();
        this.mUnrotateAndScaleMatrix = new Matrix();
        this.mRotateScalePoints = new float[2];
        this.mBoundingBoxProjection = new BoundingBox();
        this.mScreenRectProjection = new Rect();
        this.mCurrentCenter = new GeoPoint(0.0, 0.0);
        this.mZoomLevelProjection = mZoomLevelProjection;
        this.horizontalWrapEnabled = horizontalWrapEnabled;
        this.verticalWrapEnabled = verticalWrapEnabled;
        this.mTileSystem = mTileSystem;
        this.mMercatorMapSize = TileSystem.MapSize(this.mZoomLevelProjection);
        this.mTileSize = TileSystem.getTileSize(this.mZoomLevelProjection);
        this.mIntrinsicScreenRectProjection = mIntrinsicScreenRectProjection;
        if (geoPoint == null) {
            geoPoint = new GeoPoint(0.0, 0.0);
        }
        this.mScrollX = mScrollX;
        this.mScrollY = mScrollY;
        this.mOffsetX = this.getScreenCenterX() - this.mScrollX - this.mTileSystem.getMercatorXFromLongitude(geoPoint.getLongitude(), this.mMercatorMapSize, this.horizontalWrapEnabled);
        this.mOffsetY = this.getScreenCenterY() - this.mScrollY - this.mTileSystem.getMercatorYFromLatitude(geoPoint.getLatitude(), this.mMercatorMapSize, this.verticalWrapEnabled);
        this.mOrientation = mOrientation;
        this.mRotateAndScaleMatrix.preRotate(this.mOrientation, (float)this.getScreenCenterX(), (float)this.getScreenCenterY());
        this.mRotateAndScaleMatrix.invert(this.mUnrotateAndScaleMatrix);
        this.refresh();
    }
    
    Projection(final MapView mapView) {
        this(mapView.getZoomLevelDouble(), mapView.getIntrinsicScreenRect(null), mapView.getExpectedCenter(), mapView.getMapScrollX(), mapView.getMapScrollY(), mapView.getMapOrientation(), mapView.isHorizontalMapRepetitionEnabled(), mapView.isVerticalMapRepetitionEnabled(), MapView.getTileSystem());
    }
    
    private Point applyMatrixToPoint(final int x, final int y, Point point, final Matrix matrix, final boolean b) {
        if (point == null) {
            point = new Point();
        }
        if (b) {
            final float[] mRotateScalePoints = this.mRotateScalePoints;
            mRotateScalePoints[0] = (float)x;
            mRotateScalePoints[1] = (float)y;
            matrix.mapPoints(mRotateScalePoints);
            final float[] mRotateScalePoints2 = this.mRotateScalePoints;
            point.x = (int)mRotateScalePoints2[0];
            point.y = (int)mRotateScalePoints2[1];
        }
        else {
            point.x = x;
            point.y = y;
        }
        return point;
    }
    
    private long getCloserPixel(long n, final int n2, final int n3, final double n4) {
        final long n5 = (n2 + n3) / 2;
        final long n6 = n2;
        long n8;
        final long n7 = n8 = 0L;
        long n9 = n;
        if (n < n6) {
            long n10 = n7;
            while (n < n6) {
                final double v = (double)n;
                Double.isNaN(v);
                final long n11 = (long)(v + n4);
                n10 = n;
                n = n11;
            }
            if (n < n3) {
                return n;
            }
            if (Math.abs(n5 - n) < Math.abs(n5 - n10)) {
                return n;
            }
            return n10;
        }
        else {
            while (n9 >= n6) {
                final double v2 = (double)n9;
                Double.isNaN(v2);
                n = (long)(v2 - n4);
                n8 = n9;
                n9 = n;
            }
            if (n8 < n3) {
                return n8;
            }
            if (Math.abs(n5 - n9) < Math.abs(n5 - n8)) {
                return n9;
            }
            return n8;
        }
    }
    
    private long getLongPixelFromMercator(long closerPixel, final boolean b, long n, final int n2, final int n3) {
        n = (closerPixel += n);
        if (b) {
            closerPixel = this.getCloserPixel(n, n2, n3, this.mMercatorMapSize);
        }
        return closerPixel;
    }
    
    private long getLongPixelXFromMercator(final long n, final boolean b) {
        final long mOffsetX = this.mOffsetX;
        final Rect mIntrinsicScreenRectProjection = this.mIntrinsicScreenRectProjection;
        return this.getLongPixelFromMercator(n, b, mOffsetX, mIntrinsicScreenRectProjection.left, mIntrinsicScreenRectProjection.right);
    }
    
    private long getLongPixelYFromMercator(final long n, final boolean b) {
        final long mOffsetY = this.mOffsetY;
        final Rect mIntrinsicScreenRectProjection = this.mIntrinsicScreenRectProjection;
        return this.getLongPixelFromMercator(n, b, mOffsetY, mIntrinsicScreenRectProjection.top, mIntrinsicScreenRectProjection.bottom);
    }
    
    public static long getScrollableOffset(long n, long n2, final double n3, final int n4, final int n5) {
        long n6;
        while (true) {
            n6 = n2 - n;
            if (n6 >= 0L) {
                break;
            }
            final double v = (double)n2;
            Double.isNaN(v);
            n2 = (long)(v + n3);
        }
        if (n6 < n4 - n5 * 2) {
            final long n7 = n6 / 2L;
            final long n8 = n4 / 2;
            n = n8 - n7 - n;
            if (n > 0L) {
                return n;
            }
            n = n8 + n7 - n2;
            if (n < 0L) {
                return n;
            }
            return 0L;
        }
        else {
            n = n5 - n;
            if (n < 0L) {
                return n;
            }
            n = n4 - n5 - n2;
            if (n > 0L) {
                return n;
            }
            return 0L;
        }
    }
    
    private void refresh() {
        final Rect mIntrinsicScreenRectProjection = this.mIntrinsicScreenRectProjection;
        this.fromPixels((mIntrinsicScreenRectProjection.left + mIntrinsicScreenRectProjection.right) / 2, (mIntrinsicScreenRectProjection.top + mIntrinsicScreenRectProjection.bottom) / 2, this.mCurrentCenter);
        final Rect mIntrinsicScreenRectProjection2 = this.mIntrinsicScreenRectProjection;
        final IGeoPoint fromPixels = this.fromPixels(mIntrinsicScreenRectProjection2.right, mIntrinsicScreenRectProjection2.top, null, true);
        final Rect mIntrinsicScreenRectProjection3 = this.mIntrinsicScreenRectProjection;
        final IGeoPoint fromPixels2 = this.fromPixels(mIntrinsicScreenRectProjection3.left, mIntrinsicScreenRectProjection3.bottom, null, true);
        this.mBoundingBoxProjection.set(fromPixels.getLatitude(), fromPixels.getLongitude(), fromPixels2.getLatitude(), fromPixels2.getLongitude());
        final float mOrientation = this.mOrientation;
        if (mOrientation != 0.0f && mOrientation != 180.0f) {
            GeometryMath.getBoundingBoxForRotatatedRectangle(this.mIntrinsicScreenRectProjection, this.getScreenCenterX(), this.getScreenCenterY(), this.mOrientation, this.mScreenRectProjection);
        }
        else {
            final Rect mScreenRectProjection = this.mScreenRectProjection;
            final Rect mIntrinsicScreenRectProjection4 = this.mIntrinsicScreenRectProjection;
            mScreenRectProjection.left = mIntrinsicScreenRectProjection4.left;
            mScreenRectProjection.top = mIntrinsicScreenRectProjection4.top;
            mScreenRectProjection.right = mIntrinsicScreenRectProjection4.right;
            mScreenRectProjection.bottom = mIntrinsicScreenRectProjection4.bottom;
        }
    }
    
    void adjustOffsets(final double n, final double n2, final boolean b, final int n3) {
        long scrollableOffset = 0L;
        long scrollableOffset2;
        if (b) {
            scrollableOffset = getScrollableOffset(this.getLongPixelYFromLatitude(n), this.getLongPixelYFromLatitude(n2), this.mMercatorMapSize, this.mIntrinsicScreenRectProjection.height(), n3);
            scrollableOffset2 = 0L;
        }
        else {
            scrollableOffset2 = getScrollableOffset(this.getLongPixelXFromLongitude(n), this.getLongPixelXFromLongitude(n2), this.mMercatorMapSize, this.mIntrinsicScreenRectProjection.width(), n3);
        }
        this.adjustOffsets(scrollableOffset2, scrollableOffset);
    }
    
    void adjustOffsets(final long n, final long n2) {
        if (n == 0L && n2 == 0L) {
            return;
        }
        this.mOffsetX += n;
        this.mOffsetY += n2;
        this.mScrollX -= n;
        this.mScrollY -= n2;
        this.refresh();
    }
    
    public void adjustOffsets(final IGeoPoint geoPoint, final PointF pointF) {
        if (pointF == null) {
            return;
        }
        final Point unrotateAndScalePoint = this.unrotateAndScalePoint((int)pointF.x, (int)pointF.y, null);
        final Point pixels = this.toPixels(geoPoint, null);
        this.adjustOffsets(unrotateAndScalePoint.x - pixels.x, unrotateAndScalePoint.y - pixels.y);
    }
    
    public void detach() {
    }
    
    public IGeoPoint fromPixels(final int n, final int n2) {
        return this.fromPixels(n, n2, null, false);
    }
    
    public IGeoPoint fromPixels(final int n, final int n2, final GeoPoint geoPoint) {
        return this.fromPixels(n, n2, geoPoint, false);
    }
    
    public IGeoPoint fromPixels(final int n, final int n2, final GeoPoint geoPoint, final boolean b) {
        return this.mTileSystem.getGeoFromMercator(this.getCleanMercator(this.getMercatorXFromPixel(n), this.horizontalWrapEnabled), this.getCleanMercator(this.getMercatorYFromPixel(n2), this.verticalWrapEnabled), this.mMercatorMapSize, geoPoint, this.horizontalWrapEnabled || b, this.verticalWrapEnabled || b);
    }
    
    public BoundingBox getBoundingBox() {
        return this.mBoundingBoxProjection;
    }
    
    public long getCleanMercator(final long n, final boolean b) {
        return this.mTileSystem.getCleanMercator(n, this.mMercatorMapSize, b);
    }
    
    public GeoPoint getCurrentCenter() {
        return this.mCurrentCenter;
    }
    
    public Matrix getInvertedScaleRotateCanvasMatrix() {
        return this.mUnrotateAndScaleMatrix;
    }
    
    public long getLongPixelXFromLongitude(final double n) {
        return this.getLongPixelXFromMercator(this.mTileSystem.getMercatorXFromLongitude(n, this.mMercatorMapSize, false), false);
    }
    
    public long getLongPixelXFromLongitude(final double n, final boolean b) {
        return this.getLongPixelXFromMercator(this.mTileSystem.getMercatorXFromLongitude(n, this.mMercatorMapSize, this.horizontalWrapEnabled || b), this.horizontalWrapEnabled);
    }
    
    public long getLongPixelYFromLatitude(final double n) {
        return this.getLongPixelYFromMercator(this.mTileSystem.getMercatorYFromLatitude(n, this.mMercatorMapSize, false), false);
    }
    
    public long getLongPixelYFromLatitude(final double n, final boolean b) {
        return this.getLongPixelYFromMercator(this.mTileSystem.getMercatorYFromLatitude(n, this.mMercatorMapSize, this.verticalWrapEnabled || b), this.verticalWrapEnabled);
    }
    
    public long getMercatorFromTile(final int n) {
        return TileSystem.getMercatorFromTile(n, this.mTileSize);
    }
    
    public RectL getMercatorViewPort(RectL rectL) {
        if (rectL == null) {
            rectL = new RectL();
        }
        final Rect mIntrinsicScreenRectProjection = this.mIntrinsicScreenRectProjection;
        final int left = mIntrinsicScreenRectProjection.left;
        float n = (float)left;
        final int right = mIntrinsicScreenRectProjection.right;
        float n2 = (float)right;
        final int top = mIntrinsicScreenRectProjection.top;
        float n3 = (float)top;
        final int bottom = mIntrinsicScreenRectProjection.bottom;
        float n4 = (float)bottom;
        float n5 = n;
        float n6 = n2;
        float n7 = n3;
        float n8 = n4;
        if (this.mOrientation != 0.0f) {
            final float[] array = new float[8];
            final float n9 = (float)left;
            int n10 = 0;
            array[0] = n9;
            array[1] = (float)top;
            array[2] = (float)right;
            array[3] = (float)bottom;
            array[4] = (float)left;
            array[5] = (float)bottom;
            array[6] = (float)right;
            array[7] = (float)top;
            this.mUnrotateAndScaleMatrix.mapPoints(array);
            while (true) {
                n5 = n;
                n6 = n2;
                n7 = n3;
                n8 = n4;
                if (n10 >= 8) {
                    break;
                }
                float n11 = n;
                if (n > array[n10]) {
                    n11 = array[n10];
                }
                float n12 = n2;
                if (n2 < array[n10]) {
                    n12 = array[n10];
                }
                final int n13 = n10 + 1;
                float n14 = n3;
                if (n3 > array[n13]) {
                    n14 = array[n13];
                }
                float n15 = n4;
                if (n4 < array[n13]) {
                    n15 = array[n13];
                }
                n10 += 2;
                n = n11;
                n2 = n12;
                n3 = n14;
                n4 = n15;
            }
        }
        rectL.left = this.getMercatorXFromPixel((int)n5);
        rectL.top = this.getMercatorYFromPixel((int)n7);
        rectL.right = this.getMercatorXFromPixel((int)n6);
        rectL.bottom = this.getMercatorYFromPixel((int)n8);
        return rectL;
    }
    
    public long getMercatorXFromPixel(final int n) {
        return n - this.mOffsetX;
    }
    
    public long getMercatorYFromPixel(final int n) {
        return n - this.mOffsetY;
    }
    
    public float getOrientation() {
        return this.mOrientation;
    }
    
    public Rect getPixelFromTile(final int n, final int n2, Rect rect) {
        if (rect == null) {
            rect = new Rect();
        }
        rect.left = TileSystem.truncateToInt(this.getLongPixelXFromMercator(this.getMercatorFromTile(n), false));
        rect.top = TileSystem.truncateToInt(this.getLongPixelYFromMercator(this.getMercatorFromTile(n2), false));
        rect.right = TileSystem.truncateToInt(this.getLongPixelXFromMercator(this.getMercatorFromTile(n + 1), false));
        rect.bottom = TileSystem.truncateToInt(this.getLongPixelYFromMercator(this.getMercatorFromTile(n2 + 1), false));
        return rect;
    }
    
    public int getScreenCenterX() {
        final Rect mIntrinsicScreenRectProjection = this.mIntrinsicScreenRectProjection;
        return (mIntrinsicScreenRectProjection.right + mIntrinsicScreenRectProjection.left) / 2;
    }
    
    public int getScreenCenterY() {
        final Rect mIntrinsicScreenRectProjection = this.mIntrinsicScreenRectProjection;
        return (mIntrinsicScreenRectProjection.bottom + mIntrinsicScreenRectProjection.top) / 2;
    }
    
    public double getZoomLevel() {
        return this.mZoomLevelProjection;
    }
    
    public void restore(final Canvas canvas, final boolean b) {
        if (this.mOrientation != 0.0f || b) {
            canvas.restore();
        }
    }
    
    public Point rotateAndScalePoint(final int n, final int n2, final Point point) {
        return this.applyMatrixToPoint(n, n2, point, this.mRotateAndScaleMatrix, this.mOrientation != 0.0f);
    }
    
    public void save(final Canvas canvas, final boolean b, final boolean b2) {
        if (this.mOrientation != 0.0f || b2) {
            canvas.save();
            Matrix matrix;
            if (b) {
                matrix = this.mRotateAndScaleMatrix;
            }
            else {
                matrix = this.mUnrotateAndScaleMatrix;
            }
            canvas.concat(matrix);
        }
    }
    
    boolean setMapScroll(final MapView mapView) {
        if (mapView.getMapScrollX() == this.mScrollX && mapView.getMapScrollY() == this.mScrollY) {
            return false;
        }
        mapView.setMapScroll(this.mScrollX, this.mScrollY);
        return true;
    }
    
    public PointL toMercatorPixels(final int n, final int n2, PointL pointL) {
        if (pointL == null) {
            pointL = new PointL();
        }
        pointL.x = this.getCleanMercator(this.getMercatorXFromPixel(n), this.horizontalWrapEnabled);
        pointL.y = this.getCleanMercator(this.getMercatorYFromPixel(n2), this.verticalWrapEnabled);
        return pointL;
    }
    
    public Point toPixels(final IGeoPoint geoPoint, final Point point) {
        return this.toPixels(geoPoint, point, false);
    }
    
    public Point toPixels(final IGeoPoint geoPoint, Point point, final boolean b) {
        if (point == null) {
            point = new Point();
        }
        point.x = TileSystem.truncateToInt(this.getLongPixelXFromLongitude(geoPoint.getLongitude(), b));
        point.y = TileSystem.truncateToInt(this.getLongPixelYFromLatitude(geoPoint.getLatitude(), b));
        return point;
    }
    
    public Point unrotateAndScalePoint(final int n, final int n2, final Point point) {
        return this.applyMatrixToPoint(n, n2, point, this.mUnrotateAndScaleMatrix, this.mOrientation != 0.0f);
    }
}

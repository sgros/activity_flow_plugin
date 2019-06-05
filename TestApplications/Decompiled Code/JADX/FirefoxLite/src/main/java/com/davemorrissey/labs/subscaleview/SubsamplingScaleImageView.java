package com.davemorrissey.labs.subscaleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnLongClickListener;
import android.view.ViewParent;
import com.davemorrissey.labs.subscaleview.decoder.CompatDecoderFactory;
import com.davemorrissey.labs.subscaleview.decoder.DecoderFactory;
import com.davemorrissey.labs.subscaleview.decoder.ImageDecoder;
import com.davemorrissey.labs.subscaleview.decoder.ImageRegionDecoder;
import com.davemorrissey.labs.subscaleview.decoder.SkiaImageDecoder;
import com.davemorrissey.labs.subscaleview.decoder.SkiaImageRegionDecoder;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

public class SubsamplingScaleImageView extends View {
    private static final String TAG = "SubsamplingScaleImageView";
    public static int TILE_SIZE_AUTO = Integer.MAX_VALUE;
    private static final List<Integer> VALID_EASING_STYLES = Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(1)});
    private static final List<Integer> VALID_ORIENTATIONS = Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(90), Integer.valueOf(180), Integer.valueOf(270), Integer.valueOf(-1)});
    private static final List<Integer> VALID_PAN_LIMITS = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)});
    private static final List<Integer> VALID_SCALE_TYPES = Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(3)});
    private static final List<Integer> VALID_ZOOM_STYLES = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)});
    private Anim anim;
    private Bitmap bitmap;
    private DecoderFactory<? extends ImageDecoder> bitmapDecoderFactory;
    private boolean bitmapIsCached;
    private boolean bitmapIsPreview;
    private Paint bitmapPaint;
    private boolean debug;
    private Paint debugPaint;
    private ImageRegionDecoder decoder;
    private final Object decoderLock;
    private float density;
    private GestureDetector detector;
    private int doubleTapZoomDuration;
    private float doubleTapZoomScale;
    private int doubleTapZoomStyle;
    private float[] dstArray;
    private int fullImageSampleSize;
    private Handler handler;
    private boolean imageLoadedSent;
    private boolean isPanning;
    private boolean isQuickScaling;
    private boolean isZooming;
    private Matrix matrix;
    private float maxScale;
    private int maxTileHeight;
    private int maxTileWidth;
    private int maxTouchCount;
    private float minScale;
    private int minimumScaleType;
    private int minimumTileDpi;
    private OnImageEventListener onImageEventListener;
    private OnLongClickListener onLongClickListener;
    private OnStateChangedListener onStateChangedListener;
    private int orientation;
    private Rect pRegion;
    private boolean panEnabled;
    private int panLimit;
    private boolean parallelLoadingEnabled;
    private Float pendingScale;
    private boolean quickScaleEnabled;
    private float quickScaleLastDistance;
    private boolean quickScaleMoved;
    private PointF quickScaleSCenter;
    private final float quickScaleThreshold;
    private PointF quickScaleVLastPoint;
    private PointF quickScaleVStart;
    private boolean readySent;
    private DecoderFactory<? extends ImageRegionDecoder> regionDecoderFactory;
    private int sHeight;
    private int sOrientation;
    private PointF sPendingCenter;
    private RectF sRect;
    private Rect sRegion;
    private PointF sRequestedCenter;
    private int sWidth;
    private ScaleAndTranslate satTemp;
    private float scale;
    private float scaleStart;
    private float[] srcArray;
    private Paint tileBgPaint;
    private Map<Integer, List<Tile>> tileMap;
    private Uri uri;
    private PointF vCenterStart;
    private float vDistStart;
    private PointF vTranslate;
    private PointF vTranslateBefore;
    private PointF vTranslateStart;
    private boolean zoomEnabled;

    /* renamed from: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView$1 */
    class C04171 implements Callback {
        C04171() {
        }

        public boolean handleMessage(Message message) {
            if (message.what == 1 && SubsamplingScaleImageView.this.onLongClickListener != null) {
                SubsamplingScaleImageView.this.maxTouchCount = 0;
                super.setOnLongClickListener(SubsamplingScaleImageView.this.onLongClickListener);
                SubsamplingScaleImageView.this.performLongClick();
                super.setOnLongClickListener(null);
            }
            return true;
        }
    }

    private static class Anim {
        private long duration;
        private int easing;
        private boolean interruptible;
        private OnAnimationEventListener listener;
        private int origin;
        private PointF sCenterEnd;
        private PointF sCenterEndRequested;
        private PointF sCenterStart;
        private float scaleEnd;
        private float scaleStart;
        private long time;
        private PointF vFocusEnd;
        private PointF vFocusStart;

        private Anim() {
            this.duration = 500;
            this.interruptible = true;
            this.easing = 2;
            this.origin = 1;
            this.time = System.currentTimeMillis();
        }

        /* synthetic */ Anim(C04171 c04171) {
            this();
        }
    }

    public final class AnimationBuilder {
        private long duration;
        private int easing;
        private boolean interruptible;
        private OnAnimationEventListener listener;
        private int origin;
        private boolean panLimited;
        private final PointF targetSCenter;
        private final float targetScale;
        private final PointF vFocus;

        /* synthetic */ AnimationBuilder(SubsamplingScaleImageView subsamplingScaleImageView, float f, PointF pointF, PointF pointF2, C04171 c04171) {
            this(f, pointF, pointF2);
        }

        private AnimationBuilder(PointF pointF) {
            this.duration = 500;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = SubsamplingScaleImageView.this.scale;
            this.targetSCenter = pointF;
            this.vFocus = null;
        }

        private AnimationBuilder(float f, PointF pointF) {
            this.duration = 500;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = f;
            this.targetSCenter = pointF;
            this.vFocus = null;
        }

        private AnimationBuilder(float f, PointF pointF, PointF pointF2) {
            this.duration = 500;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = f;
            this.targetSCenter = pointF;
            this.vFocus = pointF2;
        }

        public AnimationBuilder withDuration(long j) {
            this.duration = j;
            return this;
        }

        public AnimationBuilder withInterruptible(boolean z) {
            this.interruptible = z;
            return this;
        }

        public AnimationBuilder withEasing(int i) {
            if (SubsamplingScaleImageView.VALID_EASING_STYLES.contains(Integer.valueOf(i))) {
                this.easing = i;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown easing type: ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private AnimationBuilder withPanLimited(boolean z) {
            this.panLimited = z;
            return this;
        }

        private AnimationBuilder withOrigin(int i) {
            this.origin = i;
            return this;
        }

        public void start() {
            if (!(SubsamplingScaleImageView.this.anim == null || SubsamplingScaleImageView.this.anim.listener == null)) {
                try {
                    SubsamplingScaleImageView.this.anim.listener.onInterruptedByNewAnim();
                } catch (Exception e) {
                    Log.w(SubsamplingScaleImageView.TAG, "Error thrown by animation listener", e);
                }
            }
            int paddingLeft = SubsamplingScaleImageView.this.getPaddingLeft() + (((SubsamplingScaleImageView.this.getWidth() - SubsamplingScaleImageView.this.getPaddingRight()) - SubsamplingScaleImageView.this.getPaddingLeft()) / 2);
            int paddingTop = SubsamplingScaleImageView.this.getPaddingTop() + (((SubsamplingScaleImageView.this.getHeight() - SubsamplingScaleImageView.this.getPaddingBottom()) - SubsamplingScaleImageView.this.getPaddingTop()) / 2);
            float access$6500 = SubsamplingScaleImageView.this.limitedScale(this.targetScale);
            PointF access$6600 = this.panLimited ? SubsamplingScaleImageView.this.limitedSCenter(this.targetSCenter.x, this.targetSCenter.y, access$6500, new PointF()) : this.targetSCenter;
            SubsamplingScaleImageView.this.anim = new Anim();
            SubsamplingScaleImageView.this.anim.scaleStart = SubsamplingScaleImageView.this.scale;
            SubsamplingScaleImageView.this.anim.scaleEnd = access$6500;
            SubsamplingScaleImageView.this.anim.time = System.currentTimeMillis();
            SubsamplingScaleImageView.this.anim.sCenterEndRequested = access$6600;
            SubsamplingScaleImageView.this.anim.sCenterStart = SubsamplingScaleImageView.this.getCenter();
            SubsamplingScaleImageView.this.anim.sCenterEnd = access$6600;
            SubsamplingScaleImageView.this.anim.vFocusStart = SubsamplingScaleImageView.this.sourceToViewCoord(access$6600);
            SubsamplingScaleImageView.this.anim.vFocusEnd = new PointF((float) paddingLeft, (float) paddingTop);
            SubsamplingScaleImageView.this.anim.duration = this.duration;
            SubsamplingScaleImageView.this.anim.interruptible = this.interruptible;
            SubsamplingScaleImageView.this.anim.easing = this.easing;
            SubsamplingScaleImageView.this.anim.origin = this.origin;
            SubsamplingScaleImageView.this.anim.time = System.currentTimeMillis();
            SubsamplingScaleImageView.this.anim.listener = this.listener;
            if (this.vFocus != null) {
                float f = this.vFocus.x - (SubsamplingScaleImageView.this.anim.sCenterStart.x * access$6500);
                float f2 = this.vFocus.y - (SubsamplingScaleImageView.this.anim.sCenterStart.y * access$6500);
                ScaleAndTranslate scaleAndTranslate = new ScaleAndTranslate(access$6500, new PointF(f, f2), null);
                SubsamplingScaleImageView.this.fitToBounds(true, scaleAndTranslate);
                SubsamplingScaleImageView.this.anim.vFocusEnd = new PointF(this.vFocus.x + (scaleAndTranslate.vTranslate.x - f), this.vFocus.y + (scaleAndTranslate.vTranslate.y - f2));
            }
            SubsamplingScaleImageView.this.invalidate();
        }
    }

    private static class BitmapLoadTask extends AsyncTask<Void, Void, Integer> {
        private Bitmap bitmap;
        private final WeakReference<Context> contextRef;
        private final WeakReference<DecoderFactory<? extends ImageDecoder>> decoderFactoryRef;
        private Exception exception;
        private final boolean preview;
        private final Uri source;
        private final WeakReference<SubsamplingScaleImageView> viewRef;

        BitmapLoadTask(SubsamplingScaleImageView subsamplingScaleImageView, Context context, DecoderFactory<? extends ImageDecoder> decoderFactory, Uri uri, boolean z) {
            this.viewRef = new WeakReference(subsamplingScaleImageView);
            this.contextRef = new WeakReference(context);
            this.decoderFactoryRef = new WeakReference(decoderFactory);
            this.source = uri;
            this.preview = z;
        }

        /* Access modifiers changed, original: protected|varargs */
        public Integer doInBackground(Void... voidArr) {
            try {
                String uri = this.source.toString();
                Context context = (Context) this.contextRef.get();
                DecoderFactory decoderFactory = (DecoderFactory) this.decoderFactoryRef.get();
                SubsamplingScaleImageView subsamplingScaleImageView = (SubsamplingScaleImageView) this.viewRef.get();
                if (!(context == null || decoderFactory == null || subsamplingScaleImageView == null)) {
                    subsamplingScaleImageView.debug("BitmapLoadTask.doInBackground", new Object[0]);
                    this.bitmap = ((ImageDecoder) decoderFactory.make()).decode(context, this.source);
                    return Integer.valueOf(subsamplingScaleImageView.getExifOrientation(context, uri));
                }
            } catch (Exception e) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to load bitmap", e);
                this.exception = e;
            } catch (OutOfMemoryError e2) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to load bitmap - OutOfMemoryError", e2);
                this.exception = new RuntimeException(e2);
            }
            return null;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Integer num) {
            SubsamplingScaleImageView subsamplingScaleImageView = (SubsamplingScaleImageView) this.viewRef.get();
            if (subsamplingScaleImageView == null) {
                return;
            }
            if (this.bitmap == null || num == null) {
                if (this.exception != null && subsamplingScaleImageView.onImageEventListener != null) {
                    if (this.preview) {
                        subsamplingScaleImageView.onImageEventListener.onPreviewLoadError(this.exception);
                    } else {
                        subsamplingScaleImageView.onImageEventListener.onImageLoadError(this.exception);
                    }
                }
            } else if (this.preview) {
                subsamplingScaleImageView.onPreviewLoaded(this.bitmap);
            } else {
                subsamplingScaleImageView.onImageLoaded(this.bitmap, num.intValue(), false);
            }
        }
    }

    public interface OnAnimationEventListener {
        void onComplete();

        void onInterruptedByNewAnim();

        void onInterruptedByUser();
    }

    public interface OnImageEventListener {
        void onImageLoadError(Exception exception);

        void onImageLoaded();

        void onPreviewLoadError(Exception exception);

        void onPreviewReleased();

        void onReady();

        void onTileLoadError(Exception exception);
    }

    public interface OnStateChangedListener {
        void onCenterChanged(PointF pointF, int i);

        void onScaleChanged(float f, int i);
    }

    private static class ScaleAndTranslate {
        private float scale;
        private PointF vTranslate;

        /* synthetic */ ScaleAndTranslate(float f, PointF pointF, C04171 c04171) {
            this(f, pointF);
        }

        private ScaleAndTranslate(float f, PointF pointF) {
            this.scale = f;
            this.vTranslate = pointF;
        }
    }

    private static class Tile {
        private Bitmap bitmap;
        private Rect fileSRect;
        private boolean loading;
        private Rect sRect;
        private int sampleSize;
        private Rect vRect;
        private boolean visible;

        private Tile() {
        }

        /* synthetic */ Tile(C04171 c04171) {
            this();
        }
    }

    private static class TileLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private final WeakReference<ImageRegionDecoder> decoderRef;
        private Exception exception;
        private final WeakReference<Tile> tileRef;
        private final WeakReference<SubsamplingScaleImageView> viewRef;

        TileLoadTask(SubsamplingScaleImageView subsamplingScaleImageView, ImageRegionDecoder imageRegionDecoder, Tile tile) {
            this.viewRef = new WeakReference(subsamplingScaleImageView);
            this.decoderRef = new WeakReference(imageRegionDecoder);
            this.tileRef = new WeakReference(tile);
            tile.loading = true;
        }

        /* Access modifiers changed, original: protected|varargs */
        public Bitmap doInBackground(Void... voidArr) {
            try {
                SubsamplingScaleImageView subsamplingScaleImageView = (SubsamplingScaleImageView) this.viewRef.get();
                ImageRegionDecoder imageRegionDecoder = (ImageRegionDecoder) this.decoderRef.get();
                Tile tile = (Tile) this.tileRef.get();
                if (imageRegionDecoder == null || tile == null || subsamplingScaleImageView == null || !imageRegionDecoder.isReady() || !tile.visible) {
                    if (tile != null) {
                        tile.loading = false;
                    }
                    return null;
                }
                Bitmap decodeRegion;
                subsamplingScaleImageView.debug("TileLoadTask.doInBackground, tile.sRect=%s, tile.sampleSize=%d", tile.sRect, Integer.valueOf(tile.sampleSize));
                synchronized (subsamplingScaleImageView.decoderLock) {
                    subsamplingScaleImageView.fileSRect(tile.sRect, tile.fileSRect);
                    if (subsamplingScaleImageView.sRegion != null) {
                        tile.fileSRect.offset(subsamplingScaleImageView.sRegion.left, subsamplingScaleImageView.sRegion.top);
                    }
                    decodeRegion = imageRegionDecoder.decodeRegion(tile.fileSRect, tile.sampleSize);
                }
                return decodeRegion;
            } catch (Exception e) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to decode tile", e);
                this.exception = e;
            } catch (OutOfMemoryError e2) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to decode tile - OutOfMemoryError", e2);
                this.exception = new RuntimeException(e2);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Bitmap bitmap) {
            SubsamplingScaleImageView subsamplingScaleImageView = (SubsamplingScaleImageView) this.viewRef.get();
            Tile tile = (Tile) this.tileRef.get();
            if (subsamplingScaleImageView != null && tile != null) {
                if (bitmap != null) {
                    tile.bitmap = bitmap;
                    tile.loading = false;
                    subsamplingScaleImageView.onTileLoaded();
                } else if (this.exception != null && subsamplingScaleImageView.onImageEventListener != null) {
                    subsamplingScaleImageView.onImageEventListener.onTileLoadError(this.exception);
                }
            }
        }
    }

    private static class TilesInitTask extends AsyncTask<Void, Void, int[]> {
        private final WeakReference<Context> contextRef;
        private ImageRegionDecoder decoder;
        private final WeakReference<DecoderFactory<? extends ImageRegionDecoder>> decoderFactoryRef;
        private Exception exception;
        private final Uri source;
        private final WeakReference<SubsamplingScaleImageView> viewRef;

        TilesInitTask(SubsamplingScaleImageView subsamplingScaleImageView, Context context, DecoderFactory<? extends ImageRegionDecoder> decoderFactory, Uri uri) {
            this.viewRef = new WeakReference(subsamplingScaleImageView);
            this.contextRef = new WeakReference(context);
            this.decoderFactoryRef = new WeakReference(decoderFactory);
            this.source = uri;
        }

        /* Access modifiers changed, original: protected|varargs */
        public int[] doInBackground(Void... voidArr) {
            try {
                String uri = this.source.toString();
                Context context = (Context) this.contextRef.get();
                DecoderFactory decoderFactory = (DecoderFactory) this.decoderFactoryRef.get();
                SubsamplingScaleImageView subsamplingScaleImageView = (SubsamplingScaleImageView) this.viewRef.get();
                if (!(context == null || decoderFactory == null || subsamplingScaleImageView == null)) {
                    subsamplingScaleImageView.debug("TilesInitTask.doInBackground", new Object[0]);
                    this.decoder = (ImageRegionDecoder) decoderFactory.make();
                    Point init = this.decoder.init(context, this.source);
                    int i = init.x;
                    int i2 = init.y;
                    int access$5200 = subsamplingScaleImageView.getExifOrientation(context, uri);
                    if (subsamplingScaleImageView.sRegion != null) {
                        i = subsamplingScaleImageView.sRegion.width();
                        i2 = subsamplingScaleImageView.sRegion.height();
                    }
                    return new int[]{i, i2, access$5200};
                }
            } catch (Exception e) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to initialise bitmap decoder", e);
                this.exception = e;
            }
            return null;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(int[] iArr) {
            SubsamplingScaleImageView subsamplingScaleImageView = (SubsamplingScaleImageView) this.viewRef.get();
            if (subsamplingScaleImageView == null) {
                return;
            }
            if (this.decoder != null && iArr != null && iArr.length == 3) {
                subsamplingScaleImageView.onTilesInited(this.decoder, iArr[0], iArr[1], iArr[2]);
            } else if (this.exception != null && subsamplingScaleImageView.onImageEventListener != null) {
                subsamplingScaleImageView.onImageEventListener.onImageLoadError(this.exception);
            }
        }
    }

    private float easeInOutQuad(long j, float f, float f2, long j2) {
        float f3 = ((float) j) / (((float) j2) / 2.0f);
        if (f3 < 1.0f) {
            return (((f2 / 2.0f) * f3) * f3) + f;
        }
        f3 -= 1.0f;
        return (((-f2) / 2.0f) * ((f3 * (f3 - 2.0f)) - 1.0f)) + f;
    }

    private float easeOutQuad(long j, float f, float f2, long j2) {
        float f3 = ((float) j) / ((float) j2);
        return (((-f2) * f3) * (f3 - 2.0f)) + f;
    }

    /* Access modifiers changed, original: protected */
    public void onImageLoaded() {
    }

    /* Access modifiers changed, original: protected */
    public void onReady() {
    }

    public SubsamplingScaleImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.orientation = 0;
        this.maxScale = 2.0f;
        this.minScale = minScale();
        this.minimumTileDpi = -1;
        this.panLimit = 1;
        this.minimumScaleType = 1;
        this.maxTileWidth = TILE_SIZE_AUTO;
        this.maxTileHeight = TILE_SIZE_AUTO;
        this.panEnabled = true;
        this.zoomEnabled = true;
        this.quickScaleEnabled = true;
        this.doubleTapZoomScale = 1.0f;
        this.doubleTapZoomStyle = 1;
        this.doubleTapZoomDuration = 500;
        this.decoderLock = new Object();
        this.bitmapDecoderFactory = new CompatDecoderFactory(SkiaImageDecoder.class);
        this.regionDecoderFactory = new CompatDecoderFactory(SkiaImageRegionDecoder.class);
        this.srcArray = new float[8];
        this.dstArray = new float[8];
        this.density = getResources().getDisplayMetrics().density;
        setMinimumDpi(160);
        setDoubleTapZoomDpi(160);
        setGestureDetector(context);
        this.handler = new Handler(new C04171());
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, C0416R.styleable.SubsamplingScaleImageView);
            if (obtainStyledAttributes.hasValue(C0416R.styleable.SubsamplingScaleImageView_assetName)) {
                String string = obtainStyledAttributes.getString(C0416R.styleable.SubsamplingScaleImageView_assetName);
                if (string != null && string.length() > 0) {
                    setImage(ImageSource.asset(string).tilingEnabled());
                }
            }
            if (obtainStyledAttributes.hasValue(C0416R.styleable.SubsamplingScaleImageView_src)) {
                int resourceId = obtainStyledAttributes.getResourceId(C0416R.styleable.SubsamplingScaleImageView_src, 0);
                if (resourceId > 0) {
                    setImage(ImageSource.resource(resourceId).tilingEnabled());
                }
            }
            if (obtainStyledAttributes.hasValue(C0416R.styleable.SubsamplingScaleImageView_panEnabled)) {
                setPanEnabled(obtainStyledAttributes.getBoolean(C0416R.styleable.SubsamplingScaleImageView_panEnabled, true));
            }
            if (obtainStyledAttributes.hasValue(C0416R.styleable.SubsamplingScaleImageView_zoomEnabled)) {
                setZoomEnabled(obtainStyledAttributes.getBoolean(C0416R.styleable.SubsamplingScaleImageView_zoomEnabled, true));
            }
            if (obtainStyledAttributes.hasValue(C0416R.styleable.SubsamplingScaleImageView_quickScaleEnabled)) {
                setQuickScaleEnabled(obtainStyledAttributes.getBoolean(C0416R.styleable.SubsamplingScaleImageView_quickScaleEnabled, true));
            }
            if (obtainStyledAttributes.hasValue(C0416R.styleable.SubsamplingScaleImageView_tileBackgroundColor)) {
                setTileBackgroundColor(obtainStyledAttributes.getColor(C0416R.styleable.SubsamplingScaleImageView_tileBackgroundColor, Color.argb(0, 0, 0, 0)));
            }
            obtainStyledAttributes.recycle();
        }
        this.quickScaleThreshold = TypedValue.applyDimension(1, 20.0f, context.getResources().getDisplayMetrics());
    }

    public SubsamplingScaleImageView(Context context) {
        this(context, null);
    }

    public final void setOrientation(int i) {
        if (VALID_ORIENTATIONS.contains(Integer.valueOf(i))) {
            this.orientation = i;
            reset(false);
            invalidate();
            requestLayout();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid orientation: ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public final void setImage(ImageSource imageSource) {
        setImage(imageSource, null, null);
    }

    public final void setImage(ImageSource imageSource, ImageViewState imageViewState) {
        setImage(imageSource, null, imageViewState);
    }

    public final void setImage(ImageSource imageSource, ImageSource imageSource2, ImageViewState imageViewState) {
        if (imageSource != null) {
            reset(true);
            if (imageViewState != null) {
                restoreState(imageViewState);
            }
            if (imageSource2 != null) {
                if (imageSource.getBitmap() != null) {
                    throw new IllegalArgumentException("Preview image cannot be used when a bitmap is provided for the main image");
                } else if (imageSource.getSWidth() <= 0 || imageSource.getSHeight() <= 0) {
                    throw new IllegalArgumentException("Preview image cannot be used unless dimensions are provided for the main image");
                } else {
                    this.sWidth = imageSource.getSWidth();
                    this.sHeight = imageSource.getSHeight();
                    this.pRegion = imageSource2.getSRegion();
                    if (imageSource2.getBitmap() != null) {
                        this.bitmapIsCached = imageSource2.isCached();
                        onPreviewLoaded(imageSource2.getBitmap());
                    } else {
                        Uri uri = imageSource2.getUri();
                        if (uri == null && imageSource2.getResource() != null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("android.resource://");
                            stringBuilder.append(getContext().getPackageName());
                            stringBuilder.append("/");
                            stringBuilder.append(imageSource2.getResource());
                            uri = Uri.parse(stringBuilder.toString());
                        }
                        execute(new BitmapLoadTask(this, getContext(), this.bitmapDecoderFactory, uri, true));
                    }
                }
            }
            if (imageSource.getBitmap() != null && imageSource.getSRegion() != null) {
                onImageLoaded(Bitmap.createBitmap(imageSource.getBitmap(), imageSource.getSRegion().left, imageSource.getSRegion().top, imageSource.getSRegion().width(), imageSource.getSRegion().height()), 0, false);
                return;
            } else if (imageSource.getBitmap() != null) {
                onImageLoaded(imageSource.getBitmap(), 0, imageSource.isCached());
                return;
            } else {
                this.sRegion = imageSource.getSRegion();
                this.uri = imageSource.getUri();
                if (this.uri == null && imageSource.getResource() != null) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("android.resource://");
                    stringBuilder2.append(getContext().getPackageName());
                    stringBuilder2.append("/");
                    stringBuilder2.append(imageSource.getResource());
                    this.uri = Uri.parse(stringBuilder2.toString());
                }
                if (imageSource.getTile() || this.sRegion != null) {
                    execute(new TilesInitTask(this, getContext(), this.regionDecoderFactory, this.uri));
                    return;
                }
                execute(new BitmapLoadTask(this, getContext(), this.bitmapDecoderFactory, this.uri, false));
                return;
            }
        }
        throw new NullPointerException("imageSource must not be null");
    }

    private void reset(boolean z) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("reset newImage=");
        stringBuilder.append(z);
        debug(stringBuilder.toString(), new Object[0]);
        this.scale = 0.0f;
        this.scaleStart = 0.0f;
        this.vTranslate = null;
        this.vTranslateStart = null;
        this.vTranslateBefore = null;
        this.pendingScale = Float.valueOf(0.0f);
        this.sPendingCenter = null;
        this.sRequestedCenter = null;
        this.isZooming = false;
        this.isPanning = false;
        this.isQuickScaling = false;
        this.maxTouchCount = 0;
        this.fullImageSampleSize = 0;
        this.vCenterStart = null;
        this.vDistStart = 0.0f;
        this.quickScaleLastDistance = 0.0f;
        this.quickScaleMoved = false;
        this.quickScaleSCenter = null;
        this.quickScaleVLastPoint = null;
        this.quickScaleVStart = null;
        this.anim = null;
        this.satTemp = null;
        this.matrix = null;
        this.sRect = null;
        if (z) {
            this.uri = null;
            if (this.decoder != null) {
                synchronized (this.decoderLock) {
                    this.decoder.recycle();
                    this.decoder = null;
                }
            }
            if (!(this.bitmap == null || this.bitmapIsCached)) {
                this.bitmap.recycle();
            }
            if (!(this.bitmap == null || !this.bitmapIsCached || this.onImageEventListener == null)) {
                this.onImageEventListener.onPreviewReleased();
            }
            this.sWidth = 0;
            this.sHeight = 0;
            this.sOrientation = 0;
            this.sRegion = null;
            this.pRegion = null;
            this.readySent = false;
            this.imageLoadedSent = false;
            this.bitmap = null;
            this.bitmapIsPreview = false;
            this.bitmapIsCached = false;
        }
        if (this.tileMap != null) {
            for (Entry value : this.tileMap.entrySet()) {
                for (Tile tile : (List) value.getValue()) {
                    tile.visible = false;
                    if (tile.bitmap != null) {
                        tile.bitmap.recycle();
                        tile.bitmap = null;
                    }
                }
            }
            this.tileMap = null;
        }
        setGestureDetector(getContext());
    }

    private void setGestureDetector(final Context context) {
        this.detector = new GestureDetector(context, new SimpleOnGestureListener() {
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (!SubsamplingScaleImageView.this.panEnabled || !SubsamplingScaleImageView.this.readySent || SubsamplingScaleImageView.this.vTranslate == null || motionEvent == null || motionEvent2 == null || ((Math.abs(motionEvent.getX() - motionEvent2.getX()) <= 50.0f && Math.abs(motionEvent.getY() - motionEvent2.getY()) <= 50.0f) || ((Math.abs(f) <= 500.0f && Math.abs(f2) <= 500.0f) || SubsamplingScaleImageView.this.isZooming))) {
                    return super.onFling(motionEvent, motionEvent2, f, f2);
                }
                PointF pointF = new PointF(SubsamplingScaleImageView.this.vTranslate.x + (f * 0.25f), SubsamplingScaleImageView.this.vTranslate.y + (f2 * 0.25f));
                new AnimationBuilder(SubsamplingScaleImageView.this, new PointF((((float) (SubsamplingScaleImageView.this.getWidth() / 2)) - pointF.x) / SubsamplingScaleImageView.this.scale, (((float) (SubsamplingScaleImageView.this.getHeight() / 2)) - pointF.y) / SubsamplingScaleImageView.this.scale), null).withEasing(1).withPanLimited(false).withOrigin(3).start();
                return true;
            }

            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                SubsamplingScaleImageView.this.performClick();
                return true;
            }

            public boolean onDoubleTap(MotionEvent motionEvent) {
                if (!SubsamplingScaleImageView.this.zoomEnabled || !SubsamplingScaleImageView.this.readySent || SubsamplingScaleImageView.this.vTranslate == null) {
                    return super.onDoubleTapEvent(motionEvent);
                }
                SubsamplingScaleImageView.this.setGestureDetector(context);
                if (SubsamplingScaleImageView.this.quickScaleEnabled) {
                    SubsamplingScaleImageView.this.vCenterStart = new PointF(motionEvent.getX(), motionEvent.getY());
                    SubsamplingScaleImageView.this.vTranslateStart = new PointF(SubsamplingScaleImageView.this.vTranslate.x, SubsamplingScaleImageView.this.vTranslate.y);
                    SubsamplingScaleImageView.this.scaleStart = SubsamplingScaleImageView.this.scale;
                    SubsamplingScaleImageView.this.isQuickScaling = true;
                    SubsamplingScaleImageView.this.isZooming = true;
                    SubsamplingScaleImageView.this.quickScaleLastDistance = -1.0f;
                    SubsamplingScaleImageView.this.quickScaleSCenter = SubsamplingScaleImageView.this.viewToSourceCoord(SubsamplingScaleImageView.this.vCenterStart);
                    SubsamplingScaleImageView.this.quickScaleVStart = new PointF(motionEvent.getX(), motionEvent.getY());
                    SubsamplingScaleImageView.this.quickScaleVLastPoint = new PointF(SubsamplingScaleImageView.this.quickScaleSCenter.x, SubsamplingScaleImageView.this.quickScaleSCenter.y);
                    SubsamplingScaleImageView.this.quickScaleMoved = false;
                    return false;
                }
                SubsamplingScaleImageView.this.doubleTapZoom(SubsamplingScaleImageView.this.viewToSourceCoord(new PointF(motionEvent.getX(), motionEvent.getY())), new PointF(motionEvent.getX(), motionEvent.getY()));
                return true;
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        debug("onSizeChanged %dx%d -> %dx%d", Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i), Integer.valueOf(i2));
        PointF center = getCenter();
        if (this.readySent && center != null) {
            this.anim = null;
            this.pendingScale = Float.valueOf(this.scale);
            this.sPendingCenter = center;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        i = MeasureSpec.getSize(i);
        i2 = MeasureSpec.getSize(i2);
        Object obj = null;
        Object obj2 = mode != 1073741824 ? 1 : null;
        if (mode2 != 1073741824) {
            obj = 1;
        }
        if (this.sWidth > 0 && this.sHeight > 0) {
            if (obj2 != null && obj != null) {
                i = sWidth();
                i2 = sHeight();
            } else if (obj != null) {
                i2 = (int) ((((double) sHeight()) / ((double) sWidth())) * ((double) i));
            } else if (obj2 != null) {
                i = (int) ((((double) sWidth()) / ((double) sHeight())) * ((double) i2));
            }
        }
        setMeasuredDimension(Math.max(i, getSuggestedMinimumWidth()), Math.max(i2, getSuggestedMinimumHeight()));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z = true;
        if (this.anim == null || this.anim.interruptible) {
            if (!(this.anim == null || this.anim.listener == null)) {
                try {
                    this.anim.listener.onInterruptedByUser();
                } catch (Exception e) {
                    Log.w(TAG, "Error thrown by animation listener", e);
                }
            }
            this.anim = null;
            if (this.vTranslate == null) {
                return true;
            }
            if (this.isQuickScaling || !(this.detector == null || this.detector.onTouchEvent(motionEvent))) {
                if (this.vTranslateStart == null) {
                    this.vTranslateStart = new PointF(0.0f, 0.0f);
                }
                if (this.vTranslateBefore == null) {
                    this.vTranslateBefore = new PointF(0.0f, 0.0f);
                }
                if (this.vCenterStart == null) {
                    this.vCenterStart = new PointF(0.0f, 0.0f);
                }
                float f = this.scale;
                this.vTranslateBefore.set(this.vTranslate);
                boolean onTouchEventInternal = onTouchEventInternal(motionEvent);
                sendStateChanged(f, this.vTranslateBefore, 2);
                if (!(onTouchEventInternal || super.onTouchEvent(motionEvent))) {
                    z = false;
                }
                return z;
            }
            this.isZooming = false;
            this.isPanning = false;
            this.maxTouchCount = 0;
            return true;
        }
        requestDisallowInterceptTouchEvent(true);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:122:0x03d2  */
    private boolean onTouchEventInternal(android.view.MotionEvent r13) {
        /*
        r12 = this;
        r0 = r13.getPointerCount();
        r1 = r13.getAction();
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r3 = 2;
        r4 = 0;
        r5 = 1;
        switch(r1) {
            case 0: goto L_0x044b;
            case 1: goto L_0x03db;
            case 2: goto L_0x0012;
            case 5: goto L_0x044b;
            case 6: goto L_0x03db;
            case 261: goto L_0x044b;
            case 262: goto L_0x03db;
            default: goto L_0x0010;
        };
    L_0x0010:
        goto L_0x04ce;
    L_0x0012:
        r1 = r12.maxTouchCount;
        if (r1 <= 0) goto L_0x03cf;
    L_0x0016:
        r1 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        if (r0 < r3) goto L_0x0184;
    L_0x001a:
        r0 = r13.getX(r4);
        r6 = r13.getX(r5);
        r7 = r13.getY(r4);
        r8 = r13.getY(r5);
        r0 = r12.distance(r0, r6, r7, r8);
        r6 = r13.getX(r4);
        r7 = r13.getX(r5);
        r6 = r6 + r7;
        r6 = r6 / r2;
        r7 = r13.getY(r4);
        r13 = r13.getY(r5);
        r7 = r7 + r13;
        r7 = r7 / r2;
        r13 = r12.zoomEnabled;
        if (r13 == 0) goto L_0x03cf;
    L_0x0046:
        r13 = r12.vCenterStart;
        r13 = r13.x;
        r2 = r12.vCenterStart;
        r2 = r2.y;
        r13 = r12.distance(r13, r6, r2, r7);
        r13 = (r13 > r1 ? 1 : (r13 == r1 ? 0 : -1));
        if (r13 > 0) goto L_0x0066;
    L_0x0056:
        r13 = r12.vDistStart;
        r13 = r0 - r13;
        r13 = java.lang.Math.abs(r13);
        r13 = (r13 > r1 ? 1 : (r13 == r1 ? 0 : -1));
        if (r13 > 0) goto L_0x0066;
    L_0x0062:
        r13 = r12.isPanning;
        if (r13 == 0) goto L_0x03cf;
    L_0x0066:
        r12.isZooming = r5;
        r12.isPanning = r5;
        r13 = r12.scale;
        r1 = (double) r13;
        r13 = r12.maxScale;
        r8 = r12.vDistStart;
        r8 = r0 / r8;
        r9 = r12.scaleStart;
        r8 = r8 * r9;
        r13 = java.lang.Math.min(r13, r8);
        r12.scale = r13;
        r13 = r12.scale;
        r8 = r12.minScale();
        r13 = (r13 > r8 ? 1 : (r13 == r8 ? 0 : -1));
        if (r13 > 0) goto L_0x009d;
    L_0x0087:
        r12.vDistStart = r0;
        r13 = r12.minScale();
        r12.scaleStart = r13;
        r13 = r12.vCenterStart;
        r13.set(r6, r7);
        r13 = r12.vTranslateStart;
        r0 = r12.vTranslate;
        r13.set(r0);
        goto L_0x017c;
    L_0x009d:
        r13 = r12.panEnabled;
        if (r13 == 0) goto L_0x0127;
    L_0x00a1:
        r13 = r12.vCenterStart;
        r13 = r13.x;
        r3 = r12.vTranslateStart;
        r3 = r3.x;
        r13 = r13 - r3;
        r3 = r12.vCenterStart;
        r3 = r3.y;
        r8 = r12.vTranslateStart;
        r8 = r8.y;
        r3 = r3 - r8;
        r8 = r12.scale;
        r9 = r12.scaleStart;
        r8 = r8 / r9;
        r13 = r13 * r8;
        r8 = r12.scale;
        r9 = r12.scaleStart;
        r8 = r8 / r9;
        r3 = r3 * r8;
        r8 = r12.vTranslate;
        r13 = r6 - r13;
        r8.x = r13;
        r13 = r12.vTranslate;
        r3 = r7 - r3;
        r13.y = r3;
        r13 = r12.sHeight();
        r8 = (double) r13;
        r8 = r8 * r1;
        r13 = r12.getHeight();
        r10 = (double) r13;
        r13 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r13 >= 0) goto L_0x00ef;
    L_0x00dd:
        r13 = r12.scale;
        r3 = r12.sHeight();
        r3 = (float) r3;
        r13 = r13 * r3;
        r3 = r12.getHeight();
        r3 = (float) r3;
        r13 = (r13 > r3 ? 1 : (r13 == r3 ? 0 : -1));
        if (r13 >= 0) goto L_0x0111;
    L_0x00ef:
        r13 = r12.sWidth();
        r8 = (double) r13;
        r1 = r1 * r8;
        r13 = r12.getWidth();
        r8 = (double) r13;
        r13 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1));
        if (r13 >= 0) goto L_0x017c;
    L_0x00ff:
        r13 = r12.scale;
        r1 = r12.sWidth();
        r1 = (float) r1;
        r13 = r13 * r1;
        r1 = r12.getWidth();
        r1 = (float) r1;
        r13 = (r13 > r1 ? 1 : (r13 == r1 ? 0 : -1));
        if (r13 < 0) goto L_0x017c;
    L_0x0111:
        r12.fitToBounds(r5);
        r13 = r12.vCenterStart;
        r13.set(r6, r7);
        r13 = r12.vTranslateStart;
        r1 = r12.vTranslate;
        r13.set(r1);
        r13 = r12.scale;
        r12.scaleStart = r13;
        r12.vDistStart = r0;
        goto L_0x017c;
    L_0x0127:
        r13 = r12.sRequestedCenter;
        if (r13 == 0) goto L_0x0152;
    L_0x012b:
        r13 = r12.vTranslate;
        r0 = r12.getWidth();
        r0 = r0 / r3;
        r0 = (float) r0;
        r1 = r12.scale;
        r2 = r12.sRequestedCenter;
        r2 = r2.x;
        r1 = r1 * r2;
        r0 = r0 - r1;
        r13.x = r0;
        r13 = r12.vTranslate;
        r0 = r12.getHeight();
        r0 = r0 / r3;
        r0 = (float) r0;
        r1 = r12.scale;
        r2 = r12.sRequestedCenter;
        r2 = r2.y;
        r1 = r1 * r2;
        r0 = r0 - r1;
        r13.y = r0;
        goto L_0x017c;
    L_0x0152:
        r13 = r12.vTranslate;
        r0 = r12.getWidth();
        r0 = r0 / r3;
        r0 = (float) r0;
        r1 = r12.scale;
        r2 = r12.sWidth();
        r2 = r2 / r3;
        r2 = (float) r2;
        r1 = r1 * r2;
        r0 = r0 - r1;
        r13.x = r0;
        r13 = r12.vTranslate;
        r0 = r12.getHeight();
        r0 = r0 / r3;
        r0 = (float) r0;
        r1 = r12.scale;
        r2 = r12.sHeight();
        r2 = r2 / r3;
        r2 = (float) r2;
        r1 = r1 * r2;
        r0 = r0 - r1;
        r13.y = r0;
    L_0x017c:
        r12.fitToBounds(r5);
        r12.refreshRequiredTiles(r4);
        goto L_0x02ef;
    L_0x0184:
        r0 = r12.isQuickScaling;
        if (r0 == 0) goto L_0x02f2;
    L_0x0188:
        r0 = r12.quickScaleVStart;
        r0 = r0.y;
        r1 = r13.getY();
        r0 = r0 - r1;
        r0 = java.lang.Math.abs(r0);
        r0 = r0 * r2;
        r1 = r12.quickScaleThreshold;
        r0 = r0 + r1;
        r1 = r12.quickScaleLastDistance;
        r2 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 != 0) goto L_0x01a4;
    L_0x01a2:
        r12.quickScaleLastDistance = r0;
    L_0x01a4:
        r1 = r13.getY();
        r2 = r12.quickScaleVLastPoint;
        r2 = r2.y;
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 <= 0) goto L_0x01b2;
    L_0x01b0:
        r1 = 1;
        goto L_0x01b3;
    L_0x01b2:
        r1 = 0;
    L_0x01b3:
        r2 = r12.quickScaleVLastPoint;
        r13 = r13.getY();
        r6 = 0;
        r2.set(r6, r13);
        r13 = r12.quickScaleLastDistance;
        r13 = r0 / r13;
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r13 = r2 - r13;
        r13 = java.lang.Math.abs(r13);
        r7 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r13 = r13 * r7;
        r7 = 1022739087; // 0x3cf5c28f float:0.03 double:5.053002475E-315;
        r7 = (r13 > r7 ? 1 : (r13 == r7 ? 0 : -1));
        if (r7 > 0) goto L_0x01d8;
    L_0x01d4:
        r7 = r12.quickScaleMoved;
        if (r7 == 0) goto L_0x02e7;
    L_0x01d8:
        r12.quickScaleMoved = r5;
        r7 = r12.quickScaleLastDistance;
        r7 = (r7 > r6 ? 1 : (r7 == r6 ? 0 : -1));
        if (r7 <= 0) goto L_0x01e6;
    L_0x01e0:
        if (r1 == 0) goto L_0x01e5;
    L_0x01e2:
        r13 = r13 + r2;
        r2 = r13;
        goto L_0x01e6;
    L_0x01e5:
        r2 = r2 - r13;
    L_0x01e6:
        r13 = r12.scale;
        r7 = (double) r13;
        r13 = r12.minScale();
        r1 = r12.maxScale;
        r9 = r12.scale;
        r9 = r9 * r2;
        r1 = java.lang.Math.min(r1, r9);
        r13 = java.lang.Math.max(r13, r1);
        r12.scale = r13;
        r13 = r12.panEnabled;
        if (r13 == 0) goto L_0x0292;
    L_0x0201:
        r13 = r12.vCenterStart;
        r13 = r13.x;
        r1 = r12.vTranslateStart;
        r1 = r1.x;
        r13 = r13 - r1;
        r1 = r12.vCenterStart;
        r1 = r1.y;
        r2 = r12.vTranslateStart;
        r2 = r2.y;
        r1 = r1 - r2;
        r2 = r12.scale;
        r3 = r12.scaleStart;
        r2 = r2 / r3;
        r13 = r13 * r2;
        r2 = r12.scale;
        r3 = r12.scaleStart;
        r2 = r2 / r3;
        r1 = r1 * r2;
        r2 = r12.vTranslate;
        r3 = r12.vCenterStart;
        r3 = r3.x;
        r3 = r3 - r13;
        r2.x = r3;
        r13 = r12.vTranslate;
        r2 = r12.vCenterStart;
        r2 = r2.y;
        r2 = r2 - r1;
        r13.y = r2;
        r13 = r12.sHeight();
        r1 = (double) r13;
        r1 = r1 * r7;
        r13 = r12.getHeight();
        r9 = (double) r13;
        r13 = (r1 > r9 ? 1 : (r1 == r9 ? 0 : -1));
        if (r13 >= 0) goto L_0x0255;
    L_0x0243:
        r13 = r12.scale;
        r1 = r12.sHeight();
        r1 = (float) r1;
        r13 = r13 * r1;
        r1 = r12.getHeight();
        r1 = (float) r1;
        r13 = (r13 > r1 ? 1 : (r13 == r1 ? 0 : -1));
        if (r13 >= 0) goto L_0x0277;
    L_0x0255:
        r13 = r12.sWidth();
        r1 = (double) r13;
        r7 = r7 * r1;
        r13 = r12.getWidth();
        r1 = (double) r13;
        r13 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1));
        if (r13 >= 0) goto L_0x02e7;
    L_0x0265:
        r13 = r12.scale;
        r1 = r12.sWidth();
        r1 = (float) r1;
        r13 = r13 * r1;
        r1 = r12.getWidth();
        r1 = (float) r1;
        r13 = (r13 > r1 ? 1 : (r13 == r1 ? 0 : -1));
        if (r13 < 0) goto L_0x02e7;
    L_0x0277:
        r12.fitToBounds(r5);
        r13 = r12.vCenterStart;
        r0 = r12.quickScaleSCenter;
        r0 = r12.sourceToViewCoord(r0);
        r13.set(r0);
        r13 = r12.vTranslateStart;
        r0 = r12.vTranslate;
        r13.set(r0);
        r13 = r12.scale;
        r12.scaleStart = r13;
        r0 = 0;
        goto L_0x02e7;
    L_0x0292:
        r13 = r12.sRequestedCenter;
        if (r13 == 0) goto L_0x02bd;
    L_0x0296:
        r13 = r12.vTranslate;
        r1 = r12.getWidth();
        r1 = r1 / r3;
        r1 = (float) r1;
        r2 = r12.scale;
        r6 = r12.sRequestedCenter;
        r6 = r6.x;
        r2 = r2 * r6;
        r1 = r1 - r2;
        r13.x = r1;
        r13 = r12.vTranslate;
        r1 = r12.getHeight();
        r1 = r1 / r3;
        r1 = (float) r1;
        r2 = r12.scale;
        r3 = r12.sRequestedCenter;
        r3 = r3.y;
        r2 = r2 * r3;
        r1 = r1 - r2;
        r13.y = r1;
        goto L_0x02e7;
    L_0x02bd:
        r13 = r12.vTranslate;
        r1 = r12.getWidth();
        r1 = r1 / r3;
        r1 = (float) r1;
        r2 = r12.scale;
        r6 = r12.sWidth();
        r6 = r6 / r3;
        r6 = (float) r6;
        r2 = r2 * r6;
        r1 = r1 - r2;
        r13.x = r1;
        r13 = r12.vTranslate;
        r1 = r12.getHeight();
        r1 = r1 / r3;
        r1 = (float) r1;
        r2 = r12.scale;
        r6 = r12.sHeight();
        r6 = r6 / r3;
        r3 = (float) r6;
        r2 = r2 * r3;
        r1 = r1 - r2;
        r13.y = r1;
    L_0x02e7:
        r12.quickScaleLastDistance = r0;
        r12.fitToBounds(r5);
        r12.refreshRequiredTiles(r4);
    L_0x02ef:
        r13 = 1;
        goto L_0x03d0;
    L_0x02f2:
        r0 = r12.isZooming;
        if (r0 != 0) goto L_0x03cf;
    L_0x02f6:
        r0 = r13.getX();
        r2 = r12.vCenterStart;
        r2 = r2.x;
        r0 = r0 - r2;
        r0 = java.lang.Math.abs(r0);
        r2 = r13.getY();
        r3 = r12.vCenterStart;
        r3 = r3.y;
        r2 = r2 - r3;
        r2 = java.lang.Math.abs(r2);
        r3 = r12.density;
        r3 = r3 * r1;
        r1 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r1 > 0) goto L_0x0320;
    L_0x0318:
        r6 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r6 > 0) goto L_0x0320;
    L_0x031c:
        r6 = r12.isPanning;
        if (r6 == 0) goto L_0x03cf;
    L_0x0320:
        r6 = r12.vTranslate;
        r7 = r12.vTranslateStart;
        r7 = r7.x;
        r8 = r13.getX();
        r9 = r12.vCenterStart;
        r9 = r9.x;
        r8 = r8 - r9;
        r7 = r7 + r8;
        r6.x = r7;
        r6 = r12.vTranslate;
        r7 = r12.vTranslateStart;
        r7 = r7.y;
        r13 = r13.getY();
        r8 = r12.vCenterStart;
        r8 = r8.y;
        r13 = r13 - r8;
        r7 = r7 + r13;
        r6.y = r7;
        r13 = r12.vTranslate;
        r13 = r13.x;
        r6 = r12.vTranslate;
        r6 = r6.y;
        r12.fitToBounds(r5);
        r7 = r12.vTranslate;
        r7 = r7.x;
        r13 = (r13 > r7 ? 1 : (r13 == r7 ? 0 : -1));
        if (r13 == 0) goto L_0x0359;
    L_0x0357:
        r13 = 1;
        goto L_0x035a;
    L_0x0359:
        r13 = 0;
    L_0x035a:
        r7 = r12.vTranslate;
        r7 = r7.y;
        r7 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r7 == 0) goto L_0x0364;
    L_0x0362:
        r7 = 1;
        goto L_0x0365;
    L_0x0364:
        r7 = 0;
    L_0x0365:
        if (r13 == 0) goto L_0x0371;
    L_0x0367:
        r8 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r8 <= 0) goto L_0x0371;
    L_0x036b:
        r8 = r12.isPanning;
        if (r8 != 0) goto L_0x0371;
    L_0x036f:
        r8 = 1;
        goto L_0x0372;
    L_0x0371:
        r8 = 0;
    L_0x0372:
        if (r7 == 0) goto L_0x037e;
    L_0x0374:
        r0 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1));
        if (r0 <= 0) goto L_0x037e;
    L_0x0378:
        r0 = r12.isPanning;
        if (r0 != 0) goto L_0x037e;
    L_0x037c:
        r0 = 1;
        goto L_0x037f;
    L_0x037e:
        r0 = 0;
    L_0x037f:
        r9 = r12.vTranslate;
        r9 = r9.y;
        r6 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1));
        if (r6 != 0) goto L_0x0391;
    L_0x0387:
        r6 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r6 = r6 * r3;
        r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r6 <= 0) goto L_0x0391;
    L_0x038f:
        r6 = 1;
        goto L_0x0392;
    L_0x0391:
        r6 = 0;
    L_0x0392:
        if (r8 != 0) goto L_0x03a3;
    L_0x0394:
        if (r0 != 0) goto L_0x03a3;
    L_0x0396:
        if (r13 == 0) goto L_0x03a0;
    L_0x0398:
        if (r7 == 0) goto L_0x03a0;
    L_0x039a:
        if (r6 != 0) goto L_0x03a0;
    L_0x039c:
        r13 = r12.isPanning;
        if (r13 == 0) goto L_0x03a3;
    L_0x03a0:
        r12.isPanning = r5;
        goto L_0x03b3;
    L_0x03a3:
        if (r1 > 0) goto L_0x03a9;
    L_0x03a5:
        r13 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r13 <= 0) goto L_0x03b3;
    L_0x03a9:
        r12.maxTouchCount = r4;
        r13 = r12.handler;
        r13.removeMessages(r5);
        r12.requestDisallowInterceptTouchEvent(r4);
    L_0x03b3:
        r13 = r12.panEnabled;
        if (r13 != 0) goto L_0x03ca;
    L_0x03b7:
        r13 = r12.vTranslate;
        r0 = r12.vTranslateStart;
        r0 = r0.x;
        r13.x = r0;
        r13 = r12.vTranslate;
        r0 = r12.vTranslateStart;
        r0 = r0.y;
        r13.y = r0;
        r12.requestDisallowInterceptTouchEvent(r4);
    L_0x03ca:
        r12.refreshRequiredTiles(r4);
        goto L_0x02ef;
    L_0x03cf:
        r13 = 0;
    L_0x03d0:
        if (r13 == 0) goto L_0x04ce;
    L_0x03d2:
        r13 = r12.handler;
        r13.removeMessages(r5);
        r12.invalidate();
        return r5;
    L_0x03db:
        r1 = r12.handler;
        r1.removeMessages(r5);
        r1 = r12.isQuickScaling;
        if (r1 == 0) goto L_0x03f1;
    L_0x03e4:
        r12.isQuickScaling = r4;
        r1 = r12.quickScaleMoved;
        if (r1 != 0) goto L_0x03f1;
    L_0x03ea:
        r1 = r12.quickScaleSCenter;
        r2 = r12.vCenterStart;
        r12.doubleTapZoom(r1, r2);
    L_0x03f1:
        r1 = r12.maxTouchCount;
        if (r1 <= 0) goto L_0x0442;
    L_0x03f5:
        r1 = r12.isZooming;
        if (r1 != 0) goto L_0x03fd;
    L_0x03f9:
        r1 = r12.isPanning;
        if (r1 == 0) goto L_0x0442;
    L_0x03fd:
        r1 = r12.isZooming;
        if (r1 == 0) goto L_0x0433;
    L_0x0401:
        if (r0 != r3) goto L_0x0433;
    L_0x0403:
        r12.isPanning = r5;
        r1 = r12.vTranslateStart;
        r2 = r12.vTranslate;
        r2 = r2.x;
        r6 = r12.vTranslate;
        r6 = r6.y;
        r1.set(r2, r6);
        r1 = r13.getActionIndex();
        if (r1 != r5) goto L_0x0426;
    L_0x0418:
        r1 = r12.vCenterStart;
        r2 = r13.getX(r4);
        r13 = r13.getY(r4);
        r1.set(r2, r13);
        goto L_0x0433;
    L_0x0426:
        r1 = r12.vCenterStart;
        r2 = r13.getX(r5);
        r13 = r13.getY(r5);
        r1.set(r2, r13);
    L_0x0433:
        r13 = 3;
        if (r0 >= r13) goto L_0x0438;
    L_0x0436:
        r12.isZooming = r4;
    L_0x0438:
        if (r0 >= r3) goto L_0x043e;
    L_0x043a:
        r12.isPanning = r4;
        r12.maxTouchCount = r4;
    L_0x043e:
        r12.refreshRequiredTiles(r5);
        return r5;
    L_0x0442:
        if (r0 != r5) goto L_0x044a;
    L_0x0444:
        r12.isZooming = r4;
        r12.isPanning = r4;
        r12.maxTouchCount = r4;
    L_0x044a:
        return r5;
    L_0x044b:
        r1 = 0;
        r12.anim = r1;
        r12.requestDisallowInterceptTouchEvent(r5);
        r1 = r12.maxTouchCount;
        r1 = java.lang.Math.max(r1, r0);
        r12.maxTouchCount = r1;
        if (r0 < r3) goto L_0x04a8;
    L_0x045b:
        r0 = r12.zoomEnabled;
        if (r0 == 0) goto L_0x04a0;
    L_0x045f:
        r0 = r13.getX(r4);
        r1 = r13.getX(r5);
        r3 = r13.getY(r4);
        r6 = r13.getY(r5);
        r0 = r12.distance(r0, r1, r3, r6);
        r1 = r12.scale;
        r12.scaleStart = r1;
        r12.vDistStart = r0;
        r0 = r12.vTranslateStart;
        r1 = r12.vTranslate;
        r1 = r1.x;
        r3 = r12.vTranslate;
        r3 = r3.y;
        r0.set(r1, r3);
        r0 = r12.vCenterStart;
        r1 = r13.getX(r4);
        r3 = r13.getX(r5);
        r1 = r1 + r3;
        r1 = r1 / r2;
        r3 = r13.getY(r4);
        r13 = r13.getY(r5);
        r3 = r3 + r13;
        r3 = r3 / r2;
        r0.set(r1, r3);
        goto L_0x04a2;
    L_0x04a0:
        r12.maxTouchCount = r4;
    L_0x04a2:
        r13 = r12.handler;
        r13.removeMessages(r5);
        goto L_0x04cd;
    L_0x04a8:
        r0 = r12.isQuickScaling;
        if (r0 != 0) goto L_0x04cd;
    L_0x04ac:
        r0 = r12.vTranslateStart;
        r1 = r12.vTranslate;
        r1 = r1.x;
        r2 = r12.vTranslate;
        r2 = r2.y;
        r0.set(r1, r2);
        r0 = r12.vCenterStart;
        r1 = r13.getX();
        r13 = r13.getY();
        r0.set(r1, r13);
        r13 = r12.handler;
        r0 = 600; // 0x258 float:8.41E-43 double:2.964E-321;
        r13.sendEmptyMessageDelayed(r5, r0);
    L_0x04cd:
        return r5;
    L_0x04ce:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.onTouchEventInternal(android.view.MotionEvent):boolean");
    }

    private void requestDisallowInterceptTouchEvent(boolean z) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z);
        }
    }

    private void doubleTapZoom(PointF pointF, PointF pointF2) {
        if (!this.panEnabled) {
            if (this.sRequestedCenter != null) {
                pointF.x = this.sRequestedCenter.x;
                pointF.y = this.sRequestedCenter.y;
            } else {
                pointF.x = (float) (sWidth() / 2);
                pointF.y = (float) (sHeight() / 2);
            }
        }
        float min = Math.min(this.maxScale, this.doubleTapZoomScale);
        Object obj = ((double) this.scale) <= ((double) min) * 0.9d ? 1 : null;
        if (obj == null) {
            min = minScale();
        }
        float f = min;
        if (this.doubleTapZoomStyle == 3) {
            setScaleAndCenter(f, pointF);
        } else if (this.doubleTapZoomStyle == 2 || obj == null || !this.panEnabled) {
            new AnimationBuilder(this, f, pointF, null).withInterruptible(false).withDuration((long) this.doubleTapZoomDuration).withOrigin(4).start();
        } else if (this.doubleTapZoomStyle == 1) {
            new AnimationBuilder(this, f, pointF, pointF2, null).withInterruptible(false).withDuration((long) this.doubleTapZoomDuration).withOrigin(4).start();
        }
        invalidate();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.onDraw(canvas);
        createPaints();
        if (this.sWidth != 0 && this.sHeight != 0 && getWidth() != 0 && getHeight() != 0) {
            if (this.tileMap == null && this.decoder != null) {
                initialiseBaseLayer(getMaxBitmapDimensions(canvas));
            }
            if (checkReady()) {
                float f;
                float ease;
                StringBuilder stringBuilder;
                preDraw();
                if (this.anim != null) {
                    f = this.scale;
                    if (this.vTranslateBefore == null) {
                        this.vTranslateBefore = new PointF(0.0f, 0.0f);
                    }
                    this.vTranslateBefore.set(this.vTranslate);
                    long currentTimeMillis = System.currentTimeMillis() - this.anim.time;
                    boolean z = currentTimeMillis > this.anim.duration;
                    long min = Math.min(currentTimeMillis, this.anim.duration);
                    this.scale = ease(this.anim.easing, min, this.anim.scaleStart, this.anim.scaleEnd - this.anim.scaleStart, this.anim.duration);
                    float ease2 = ease(this.anim.easing, min, this.anim.vFocusStart.x, this.anim.vFocusEnd.x - this.anim.vFocusStart.x, this.anim.duration);
                    ease = ease(this.anim.easing, min, this.anim.vFocusStart.y, this.anim.vFocusEnd.y - this.anim.vFocusStart.y, this.anim.duration);
                    PointF pointF = this.vTranslate;
                    pointF.x -= sourceToViewX(this.anim.sCenterEnd.x) - ease2;
                    pointF = this.vTranslate;
                    pointF.y -= sourceToViewY(this.anim.sCenterEnd.y) - ease;
                    boolean z2 = z || this.anim.scaleStart == this.anim.scaleEnd;
                    fitToBounds(z2);
                    sendStateChanged(f, this.vTranslateBefore, this.anim.origin);
                    refreshRequiredTiles(z);
                    if (z) {
                        if (this.anim.listener != null) {
                            try {
                                this.anim.listener.onComplete();
                            } catch (Exception e) {
                                Log.w(TAG, "Error thrown by animation listener", e);
                            }
                        }
                        this.anim = null;
                    }
                    invalidate();
                }
                if (this.tileMap != null && isBaseLayerReady()) {
                    int min2 = Math.min(this.fullImageSampleSize, calculateInSampleSize(this.scale));
                    Object obj = null;
                    for (Entry entry : this.tileMap.entrySet()) {
                        if (((Integer) entry.getKey()).intValue() == min2) {
                            for (Tile tile : (List) entry.getValue()) {
                                if (tile.visible && (tile.loading || tile.bitmap == null)) {
                                    obj = 1;
                                }
                            }
                        }
                    }
                    for (Entry entry2 : this.tileMap.entrySet()) {
                        if (((Integer) entry2.getKey()).intValue() == min2 || obj != null) {
                            for (Tile tile2 : (List) entry2.getValue()) {
                                sourceToViewRect(tile2.sRect, tile2.vRect);
                                if (!tile2.loading && tile2.bitmap != null) {
                                    if (this.tileBgPaint != null) {
                                        canvas2.drawRect(tile2.vRect, this.tileBgPaint);
                                    }
                                    if (this.matrix == null) {
                                        this.matrix = new Matrix();
                                    }
                                    this.matrix.reset();
                                    setMatrixArray(this.srcArray, 0.0f, 0.0f, (float) tile2.bitmap.getWidth(), 0.0f, (float) tile2.bitmap.getWidth(), (float) tile2.bitmap.getHeight(), 0.0f, (float) tile2.bitmap.getHeight());
                                    if (getRequiredRotation() == 0) {
                                        setMatrixArray(this.dstArray, (float) tile2.vRect.left, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.bottom);
                                    } else if (getRequiredRotation() == 90) {
                                        setMatrixArray(this.dstArray, (float) tile2.vRect.right, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.top);
                                    } else if (getRequiredRotation() == 180) {
                                        setMatrixArray(this.dstArray, (float) tile2.vRect.right, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.top);
                                    } else if (getRequiredRotation() == 270) {
                                        setMatrixArray(this.dstArray, (float) tile2.vRect.left, (float) tile2.vRect.bottom, (float) tile2.vRect.left, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.top, (float) tile2.vRect.right, (float) tile2.vRect.bottom);
                                    }
                                    this.matrix.setPolyToPoly(this.srcArray, 0, this.dstArray, 0, 4);
                                    canvas2.drawBitmap(tile2.bitmap, this.matrix, this.bitmapPaint);
                                    if (this.debug) {
                                        canvas2.drawRect(tile2.vRect, this.debugPaint);
                                    }
                                } else if (tile2.loading && this.debug) {
                                    canvas2.drawText("LOADING", (float) (tile2.vRect.left + 5), (float) (tile2.vRect.top + 35), this.debugPaint);
                                }
                                if (tile2.visible && this.debug) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("ISS ");
                                    stringBuilder.append(tile2.sampleSize);
                                    stringBuilder.append(" RECT ");
                                    stringBuilder.append(tile2.sRect.top);
                                    stringBuilder.append(",");
                                    stringBuilder.append(tile2.sRect.left);
                                    stringBuilder.append(",");
                                    stringBuilder.append(tile2.sRect.bottom);
                                    stringBuilder.append(",");
                                    stringBuilder.append(tile2.sRect.right);
                                    canvas2.drawText(stringBuilder.toString(), (float) (tile2.vRect.left + 5), (float) (tile2.vRect.top + 15), this.debugPaint);
                                }
                            }
                        }
                    }
                } else if (this.bitmap != null) {
                    f = this.scale;
                    ease = this.scale;
                    if (this.bitmapIsPreview) {
                        f = this.scale * (((float) this.sWidth) / ((float) this.bitmap.getWidth()));
                        ease = this.scale * (((float) this.sHeight) / ((float) this.bitmap.getHeight()));
                    }
                    if (this.matrix == null) {
                        this.matrix = new Matrix();
                    }
                    this.matrix.reset();
                    this.matrix.postScale(f, ease);
                    this.matrix.postRotate((float) getRequiredRotation());
                    this.matrix.postTranslate(this.vTranslate.x, this.vTranslate.y);
                    if (getRequiredRotation() == 180) {
                        this.matrix.postTranslate(this.scale * ((float) this.sWidth), this.scale * ((float) this.sHeight));
                    } else if (getRequiredRotation() == 90) {
                        this.matrix.postTranslate(this.scale * ((float) this.sHeight), 0.0f);
                    } else if (getRequiredRotation() == 270) {
                        this.matrix.postTranslate(0.0f, this.scale * ((float) this.sWidth));
                    }
                    if (this.tileBgPaint != null) {
                        if (this.sRect == null) {
                            this.sRect = new RectF();
                        }
                        this.sRect.set(0.0f, 0.0f, (float) (this.bitmapIsPreview ? this.bitmap.getWidth() : this.sWidth), (float) (this.bitmapIsPreview ? this.bitmap.getHeight() : this.sHeight));
                        this.matrix.mapRect(this.sRect);
                        canvas2.drawRect(this.sRect, this.tileBgPaint);
                    }
                    canvas2.drawBitmap(this.bitmap, this.matrix, this.bitmapPaint);
                }
                if (this.debug) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Scale: ");
                    stringBuilder2.append(String.format(Locale.ENGLISH, "%.2f", new Object[]{Float.valueOf(this.scale)}));
                    canvas2.drawText(stringBuilder2.toString(), 5.0f, 15.0f, this.debugPaint);
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Translate: ");
                    stringBuilder2.append(String.format(Locale.ENGLISH, "%.2f", new Object[]{Float.valueOf(this.vTranslate.x)}));
                    stringBuilder2.append(":");
                    stringBuilder2.append(String.format(Locale.ENGLISH, "%.2f", new Object[]{Float.valueOf(this.vTranslate.y)}));
                    canvas2.drawText(stringBuilder2.toString(), 5.0f, 35.0f, this.debugPaint);
                    PointF center = getCenter();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Source center: ");
                    stringBuilder.append(String.format(Locale.ENGLISH, "%.2f", new Object[]{Float.valueOf(center.x)}));
                    stringBuilder.append(":");
                    stringBuilder.append(String.format(Locale.ENGLISH, "%.2f", new Object[]{Float.valueOf(center.y)}));
                    canvas2.drawText(stringBuilder.toString(), 5.0f, 55.0f, this.debugPaint);
                    this.debugPaint.setStrokeWidth(2.0f);
                    if (this.anim != null) {
                        center = sourceToViewCoord(this.anim.sCenterStart);
                        PointF sourceToViewCoord = sourceToViewCoord(this.anim.sCenterEndRequested);
                        PointF sourceToViewCoord2 = sourceToViewCoord(this.anim.sCenterEnd);
                        canvas2.drawCircle(center.x, center.y, 10.0f, this.debugPaint);
                        this.debugPaint.setColor(-65536);
                        canvas2.drawCircle(sourceToViewCoord.x, sourceToViewCoord.y, 20.0f, this.debugPaint);
                        this.debugPaint.setColor(-16776961);
                        canvas2.drawCircle(sourceToViewCoord2.x, sourceToViewCoord2.y, 25.0f, this.debugPaint);
                        this.debugPaint.setColor(-16711681);
                        canvas2.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), 30.0f, this.debugPaint);
                    }
                    if (this.vCenterStart != null) {
                        this.debugPaint.setColor(-65536);
                        canvas2.drawCircle(this.vCenterStart.x, this.vCenterStart.y, 20.0f, this.debugPaint);
                    }
                    if (this.quickScaleSCenter != null) {
                        this.debugPaint.setColor(-16776961);
                        canvas2.drawCircle(sourceToViewX(this.quickScaleSCenter.x), sourceToViewY(this.quickScaleSCenter.y), 35.0f, this.debugPaint);
                    }
                    if (this.quickScaleVStart != null) {
                        this.debugPaint.setColor(-16711681);
                        canvas2.drawCircle(this.quickScaleVStart.x, this.quickScaleVStart.y, 30.0f, this.debugPaint);
                    }
                    this.debugPaint.setColor(-65281);
                    this.debugPaint.setStrokeWidth(1.0f);
                }
            }
        }
    }

    private void setMatrixArray(float[] fArr, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        fArr[0] = f;
        fArr[1] = f2;
        fArr[2] = f3;
        fArr[3] = f4;
        fArr[4] = f5;
        fArr[5] = f6;
        fArr[6] = f7;
        fArr[7] = f8;
    }

    private boolean isBaseLayerReady() {
        boolean z = true;
        if (this.bitmap != null && !this.bitmapIsPreview) {
            return true;
        }
        if (this.tileMap == null) {
            return false;
        }
        for (Entry entry : this.tileMap.entrySet()) {
            if (((Integer) entry.getKey()).intValue() == this.fullImageSampleSize) {
                for (Tile tile : (List) entry.getValue()) {
                    if (tile.loading || tile.bitmap == null) {
                        z = false;
                    }
                }
            }
        }
        return z;
    }

    private boolean checkReady() {
        boolean z = getWidth() > 0 && getHeight() > 0 && this.sWidth > 0 && this.sHeight > 0 && (this.bitmap != null || isBaseLayerReady());
        if (!this.readySent && z) {
            preDraw();
            this.readySent = true;
            onReady();
            if (this.onImageEventListener != null) {
                this.onImageEventListener.onReady();
            }
        }
        return z;
    }

    private boolean checkImageLoaded() {
        boolean isBaseLayerReady = isBaseLayerReady();
        if (!this.imageLoadedSent && isBaseLayerReady) {
            preDraw();
            this.imageLoadedSent = true;
            onImageLoaded();
            if (this.onImageEventListener != null) {
                this.onImageEventListener.onImageLoaded();
            }
        }
        return isBaseLayerReady;
    }

    private void createPaints() {
        if (this.bitmapPaint == null) {
            this.bitmapPaint = new Paint();
            this.bitmapPaint.setAntiAlias(true);
            this.bitmapPaint.setFilterBitmap(true);
            this.bitmapPaint.setDither(true);
        }
        if (this.debugPaint == null && this.debug) {
            this.debugPaint = new Paint();
            this.debugPaint.setTextSize(18.0f);
            this.debugPaint.setColor(-65281);
            this.debugPaint.setStyle(Style.STROKE);
        }
    }

    private synchronized void initialiseBaseLayer(Point point) {
        debug("initialiseBaseLayer maxTileDimensions=%dx%d", Integer.valueOf(point.x), Integer.valueOf(point.y));
        this.satTemp = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f), null);
        fitToBounds(true, this.satTemp);
        this.fullImageSampleSize = calculateInSampleSize(this.satTemp.scale);
        if (this.fullImageSampleSize > 1) {
            this.fullImageSampleSize /= 2;
        }
        if (this.fullImageSampleSize != 1 || this.sRegion != null || sWidth() >= point.x || sHeight() >= point.y) {
            initialiseTileMap(point);
            for (Tile tileLoadTask : (List) this.tileMap.get(Integer.valueOf(this.fullImageSampleSize))) {
                execute(new TileLoadTask(this, this.decoder, tileLoadTask));
            }
            refreshRequiredTiles(true);
        } else {
            this.decoder.recycle();
            this.decoder = null;
            execute(new BitmapLoadTask(this, getContext(), this.bitmapDecoderFactory, this.uri, false));
        }
    }

    private void refreshRequiredTiles(boolean z) {
        if (this.decoder != null && this.tileMap != null) {
            int min = Math.min(this.fullImageSampleSize, calculateInSampleSize(this.scale));
            for (Entry value : this.tileMap.entrySet()) {
                for (Tile tile : (List) value.getValue()) {
                    if (tile.sampleSize < min || (tile.sampleSize > min && tile.sampleSize != this.fullImageSampleSize)) {
                        tile.visible = false;
                        if (tile.bitmap != null) {
                            tile.bitmap.recycle();
                            tile.bitmap = null;
                        }
                    }
                    if (tile.sampleSize == min) {
                        if (tileVisible(tile)) {
                            tile.visible = true;
                            if (!tile.loading && tile.bitmap == null && z) {
                                execute(new TileLoadTask(this, this.decoder, tile));
                            }
                        } else if (tile.sampleSize != this.fullImageSampleSize) {
                            tile.visible = false;
                            if (tile.bitmap != null) {
                                tile.bitmap.recycle();
                                tile.bitmap = null;
                            }
                        }
                    } else if (tile.sampleSize == this.fullImageSampleSize) {
                        tile.visible = true;
                    }
                }
            }
        }
    }

    private boolean tileVisible(Tile tile) {
        return viewToSourceX(0.0f) <= ((float) tile.sRect.right) && ((float) tile.sRect.left) <= viewToSourceX((float) getWidth()) && viewToSourceY(0.0f) <= ((float) tile.sRect.bottom) && ((float) tile.sRect.top) <= viewToSourceY((float) getHeight());
    }

    private void preDraw() {
        if (getWidth() != 0 && getHeight() != 0 && this.sWidth > 0 && this.sHeight > 0) {
            if (!(this.sPendingCenter == null || this.pendingScale == null)) {
                this.scale = this.pendingScale.floatValue();
                if (this.vTranslate == null) {
                    this.vTranslate = new PointF();
                }
                this.vTranslate.x = ((float) (getWidth() / 2)) - (this.scale * this.sPendingCenter.x);
                this.vTranslate.y = ((float) (getHeight() / 2)) - (this.scale * this.sPendingCenter.y);
                this.sPendingCenter = null;
                this.pendingScale = null;
                fitToBounds(true);
                refreshRequiredTiles(true);
            }
            fitToBounds(false);
        }
    }

    private int calculateInSampleSize(float f) {
        if (this.minimumTileDpi > 0) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            f *= ((float) this.minimumTileDpi) / ((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f);
        }
        int sWidth = (int) (((float) sWidth()) * f);
        int sHeight = (int) (((float) sHeight()) * f);
        if (sWidth == 0 || sHeight == 0) {
            return 32;
        }
        int i = 1;
        if (sHeight() > sHeight || sWidth() > sWidth) {
            sHeight = Math.round(((float) sHeight()) / ((float) sHeight));
            sWidth = Math.round(((float) sWidth()) / ((float) sWidth));
            if (sHeight >= sWidth) {
                sHeight = sWidth;
            }
        } else {
            sHeight = 1;
        }
        while (true) {
            sWidth = i * 2;
            if (sWidth >= sHeight) {
                return i;
            }
            i = sWidth;
        }
    }

    private void fitToBounds(boolean z, ScaleAndTranslate scaleAndTranslate) {
        float max;
        float max2;
        if (this.panLimit == 2 && isReady()) {
            z = false;
        }
        PointF access$4800 = scaleAndTranslate.vTranslate;
        float limitedScale = limitedScale(scaleAndTranslate.scale);
        float sWidth = ((float) sWidth()) * limitedScale;
        float sHeight = ((float) sHeight()) * limitedScale;
        if (this.panLimit == 3 && isReady()) {
            access$4800.x = Math.max(access$4800.x, ((float) (getWidth() / 2)) - sWidth);
            access$4800.y = Math.max(access$4800.y, ((float) (getHeight() / 2)) - sHeight);
        } else if (z) {
            access$4800.x = Math.max(access$4800.x, ((float) getWidth()) - sWidth);
            access$4800.y = Math.max(access$4800.y, ((float) getHeight()) - sHeight);
        } else {
            access$4800.x = Math.max(access$4800.x, -sWidth);
            access$4800.y = Math.max(access$4800.y, -sHeight);
        }
        float f = 0.5f;
        float paddingLeft = (getPaddingLeft() > 0 || getPaddingRight() > 0) ? ((float) getPaddingLeft()) / ((float) (getPaddingLeft() + getPaddingRight())) : 0.5f;
        if (getPaddingTop() > 0 || getPaddingBottom() > 0) {
            f = ((float) getPaddingTop()) / ((float) (getPaddingTop() + getPaddingBottom()));
        }
        if (this.panLimit == 3 && isReady()) {
            max = (float) Math.max(0, getWidth() / 2);
            max2 = (float) Math.max(0, getHeight() / 2);
        } else if (z) {
            max = Math.max(0.0f, (((float) getWidth()) - sWidth) * paddingLeft);
            max2 = Math.max(0.0f, (((float) getHeight()) - sHeight) * f);
        } else {
            max = (float) Math.max(0, getWidth());
            max2 = (float) Math.max(0, getHeight());
        }
        access$4800.x = Math.min(access$4800.x, max);
        access$4800.y = Math.min(access$4800.y, max2);
        scaleAndTranslate.scale = limitedScale;
    }

    private void fitToBounds(boolean z) {
        Object obj;
        if (this.vTranslate == null) {
            obj = 1;
            this.vTranslate = new PointF(0.0f, 0.0f);
        } else {
            obj = null;
        }
        if (this.satTemp == null) {
            this.satTemp = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f), null);
        }
        this.satTemp.scale = this.scale;
        this.satTemp.vTranslate.set(this.vTranslate);
        fitToBounds(z, this.satTemp);
        this.scale = this.satTemp.scale;
        this.vTranslate.set(this.satTemp.vTranslate);
        if (obj != null) {
            this.vTranslate.set(vTranslateForSCenter((float) (sWidth() / 2), (float) (sHeight() / 2), this.scale));
        }
    }

    private void initialiseTileMap(Point point) {
        Point point2 = point;
        Object[] objArr = new Object[2];
        objArr[0] = Integer.valueOf(point2.x);
        int i = 1;
        objArr[1] = Integer.valueOf(point2.y);
        debug("initialiseTileMap maxTileDimensions=%dx%d", objArr);
        this.tileMap = new LinkedHashMap();
        int i2 = this.fullImageSampleSize;
        int i3 = 1;
        int i4 = 1;
        while (true) {
            int sWidth = sWidth() / i3;
            int sHeight = sHeight() / i4;
            int i5 = sWidth / i2;
            int i6 = sHeight / i2;
            while (true) {
                if ((i5 + i3) + i > point2.x || (((double) i5) > ((double) getWidth()) * 1.25d && i2 < this.fullImageSampleSize)) {
                    i3++;
                    sWidth = sWidth() / i3;
                    i5 = sWidth / i2;
                    i = 1;
                }
            }
            while (true) {
                if ((i6 + i4) + i > point2.y || (((double) i6) > ((double) getHeight()) * 1.25d && i2 < this.fullImageSampleSize)) {
                    i4++;
                    sHeight = sHeight() / i4;
                    i6 = sHeight / i2;
                    i = 1;
                }
            }
            ArrayList arrayList = new ArrayList(i3 * i4);
            i6 = 0;
            while (i6 < i3) {
                int i7 = 0;
                while (i7 < i4) {
                    Tile tile = new Tile();
                    tile.sampleSize = i2;
                    tile.visible = i2 == this.fullImageSampleSize;
                    tile.sRect = new Rect(i6 * sWidth, i7 * sHeight, i6 == i3 + -1 ? sWidth() : (i6 + 1) * sWidth, i7 == i4 + -1 ? sHeight() : (i7 + 1) * sHeight);
                    tile.vRect = new Rect(0, 0, 0, 0);
                    tile.fileSRect = new Rect(tile.sRect);
                    arrayList.add(tile);
                    i7++;
                }
                i6++;
            }
            this.tileMap.put(Integer.valueOf(i2), arrayList);
            if (i2 != 1) {
                i2 /= 2;
                i = 1;
            } else {
                return;
            }
        }
    }

    private synchronized void onTilesInited(ImageRegionDecoder imageRegionDecoder, int i, int i2, int i3) {
        debug("onTilesInited sWidth=%d, sHeight=%d, sOrientation=%d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(this.orientation));
        if (this.sWidth > 0 && this.sHeight > 0 && !(this.sWidth == i && this.sHeight == i2)) {
            reset(false);
            if (this.bitmap != null) {
                if (!this.bitmapIsCached) {
                    this.bitmap.recycle();
                }
                this.bitmap = null;
                if (this.onImageEventListener != null && this.bitmapIsCached) {
                    this.onImageEventListener.onPreviewReleased();
                }
                this.bitmapIsPreview = false;
                this.bitmapIsCached = false;
            }
        }
        this.decoder = imageRegionDecoder;
        this.sWidth = i;
        this.sHeight = i2;
        this.sOrientation = i3;
        checkReady();
        if (!checkImageLoaded() && this.maxTileWidth > 0 && this.maxTileWidth != TILE_SIZE_AUTO && this.maxTileHeight > 0 && this.maxTileHeight != TILE_SIZE_AUTO && getWidth() > 0 && getHeight() > 0) {
            initialiseBaseLayer(new Point(this.maxTileWidth, this.maxTileHeight));
        }
        invalidate();
        requestLayout();
    }

    private synchronized void onTileLoaded() {
        debug("onTileLoaded", new Object[0]);
        checkReady();
        checkImageLoaded();
        if (isBaseLayerReady() && this.bitmap != null) {
            if (!this.bitmapIsCached) {
                this.bitmap.recycle();
            }
            this.bitmap = null;
            if (this.onImageEventListener != null && this.bitmapIsCached) {
                this.onImageEventListener.onPreviewReleased();
            }
            this.bitmapIsPreview = false;
            this.bitmapIsCached = false;
        }
        invalidate();
    }

    /* JADX WARNING: Missing block: B:15:0x0043, code skipped:
            return;
     */
    private synchronized void onPreviewLoaded(android.graphics.Bitmap r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = "onPreviewLoaded";
        r1 = 0;
        r1 = new java.lang.Object[r1];	 Catch:{ all -> 0x0049 }
        r4.debug(r0, r1);	 Catch:{ all -> 0x0049 }
        r0 = r4.bitmap;	 Catch:{ all -> 0x0049 }
        if (r0 != 0) goto L_0x0044;
    L_0x000d:
        r0 = r4.imageLoadedSent;	 Catch:{ all -> 0x0049 }
        if (r0 == 0) goto L_0x0012;
    L_0x0011:
        goto L_0x0044;
    L_0x0012:
        r0 = r4.pRegion;	 Catch:{ all -> 0x0049 }
        if (r0 == 0) goto L_0x0031;
    L_0x0016:
        r0 = r4.pRegion;	 Catch:{ all -> 0x0049 }
        r0 = r0.left;	 Catch:{ all -> 0x0049 }
        r1 = r4.pRegion;	 Catch:{ all -> 0x0049 }
        r1 = r1.top;	 Catch:{ all -> 0x0049 }
        r2 = r4.pRegion;	 Catch:{ all -> 0x0049 }
        r2 = r2.width();	 Catch:{ all -> 0x0049 }
        r3 = r4.pRegion;	 Catch:{ all -> 0x0049 }
        r3 = r3.height();	 Catch:{ all -> 0x0049 }
        r5 = android.graphics.Bitmap.createBitmap(r5, r0, r1, r2, r3);	 Catch:{ all -> 0x0049 }
        r4.bitmap = r5;	 Catch:{ all -> 0x0049 }
        goto L_0x0033;
    L_0x0031:
        r4.bitmap = r5;	 Catch:{ all -> 0x0049 }
    L_0x0033:
        r5 = 1;
        r4.bitmapIsPreview = r5;	 Catch:{ all -> 0x0049 }
        r5 = r4.checkReady();	 Catch:{ all -> 0x0049 }
        if (r5 == 0) goto L_0x0042;
    L_0x003c:
        r4.invalidate();	 Catch:{ all -> 0x0049 }
        r4.requestLayout();	 Catch:{ all -> 0x0049 }
    L_0x0042:
        monitor-exit(r4);
        return;
    L_0x0044:
        r5.recycle();	 Catch:{ all -> 0x0049 }
        monitor-exit(r4);
        return;
    L_0x0049:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.onPreviewLoaded(android.graphics.Bitmap):void");
    }

    private synchronized void onImageLoaded(Bitmap bitmap, int i, boolean z) {
        debug("onImageLoaded", new Object[0]);
        if (this.sWidth > 0 && this.sHeight > 0 && !(this.sWidth == bitmap.getWidth() && this.sHeight == bitmap.getHeight())) {
            reset(false);
        }
        if (!(this.bitmap == null || this.bitmapIsCached)) {
            this.bitmap.recycle();
        }
        if (!(this.bitmap == null || !this.bitmapIsCached || this.onImageEventListener == null)) {
            this.onImageEventListener.onPreviewReleased();
        }
        this.bitmapIsPreview = false;
        this.bitmapIsCached = z;
        this.bitmap = bitmap;
        this.sWidth = bitmap.getWidth();
        this.sHeight = bitmap.getHeight();
        this.sOrientation = i;
        boolean checkReady = checkReady();
        boolean checkImageLoaded = checkImageLoaded();
        if (checkReady || checkImageLoaded) {
            invalidate();
            requestLayout();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0059  */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0062 */
    /* JADX WARNING: Removed duplicated region for block: B:54:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x006b  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0071  */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:27:0x006b, code skipped:
            r0.close();
     */
    /* JADX WARNING: Missing block: B:54:?, code skipped:
            return 0;
     */
    /* JADX WARNING: Missing block: B:55:?, code skipped:
            return 0;
     */
    private int getExifOrientation(android.content.Context r10, java.lang.String r11) {
        /*
        r9 = this;
        r0 = "content";
        r0 = r11.startsWith(r0);
        r1 = 0;
        if (r0 == 0) goto L_0x0075;
    L_0x0009:
        r0 = 0;
        r2 = "orientation";
        r5 = new java.lang.String[]{r2};	 Catch:{ Exception -> 0x0062 }
        r3 = r10.getContentResolver();	 Catch:{ Exception -> 0x0062 }
        r4 = android.net.Uri.parse(r11);	 Catch:{ Exception -> 0x0062 }
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r10 = r3.query(r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x0062 }
        if (r10 == 0) goto L_0x0056;
    L_0x0021:
        r11 = r10.moveToFirst();	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        if (r11 == 0) goto L_0x0056;
    L_0x0027:
        r11 = r10.getInt(r1);	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        r0 = VALID_ORIENTATIONS;	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        r2 = java.lang.Integer.valueOf(r11);	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        r0 = r0.contains(r2);	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        if (r0 == 0) goto L_0x003b;
    L_0x0037:
        r0 = -1;
        if (r11 == r0) goto L_0x003b;
    L_0x003a:
        goto L_0x0057;
    L_0x003b:
        r0 = TAG;	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        r2.<init>();	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        r3 = "Unsupported orientation: ";
        r2.append(r3);	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        r2.append(r11);	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        r11 = r2.toString();	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        android.util.Log.w(r0, r11);	 Catch:{ Exception -> 0x0054, all -> 0x0052 }
        goto L_0x0056;
    L_0x0052:
        r11 = move-exception;
        goto L_0x006f;
    L_0x0054:
        r0 = r10;
        goto L_0x0062;
    L_0x0056:
        r11 = 0;
    L_0x0057:
        if (r10 == 0) goto L_0x005c;
    L_0x0059:
        r10.close();
    L_0x005c:
        r1 = r11;
        goto L_0x00d8;
    L_0x005f:
        r11 = move-exception;
        r10 = r0;
        goto L_0x006f;
    L_0x0062:
        r10 = TAG;	 Catch:{ all -> 0x005f }
        r11 = "Could not get orientation of image from media store";
        android.util.Log.w(r10, r11);	 Catch:{ all -> 0x005f }
        if (r0 == 0) goto L_0x00d8;
    L_0x006b:
        r0.close();
        goto L_0x00d8;
    L_0x006f:
        if (r10 == 0) goto L_0x0074;
    L_0x0071:
        r10.close();
    L_0x0074:
        throw r11;
    L_0x0075:
        r10 = "file:///";
        r10 = r11.startsWith(r10);
        if (r10 == 0) goto L_0x00d8;
    L_0x007d:
        r10 = "file:///android_asset/";
        r10 = r11.startsWith(r10);
        if (r10 != 0) goto L_0x00d8;
    L_0x0085:
        r10 = new android.media.ExifInterface;	 Catch:{ Exception -> 0x00d1 }
        r0 = "file:///";
        r0 = r0.length();	 Catch:{ Exception -> 0x00d1 }
        r2 = 1;
        r0 = r0 - r2;
        r11 = r11.substring(r0);	 Catch:{ Exception -> 0x00d1 }
        r10.<init>(r11);	 Catch:{ Exception -> 0x00d1 }
        r11 = "Orientation";
        r10 = r10.getAttributeInt(r11, r2);	 Catch:{ Exception -> 0x00d1 }
        if (r10 == r2) goto L_0x00d8;
    L_0x009e:
        if (r10 != 0) goto L_0x00a1;
    L_0x00a0:
        goto L_0x00d8;
    L_0x00a1:
        r11 = 6;
        if (r10 != r11) goto L_0x00a9;
    L_0x00a4:
        r10 = 90;
        r1 = 90;
        goto L_0x00d8;
    L_0x00a9:
        r11 = 3;
        if (r10 != r11) goto L_0x00b1;
    L_0x00ac:
        r10 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        r1 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        goto L_0x00d8;
    L_0x00b1:
        r11 = 8;
        if (r10 != r11) goto L_0x00ba;
    L_0x00b5:
        r10 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        r1 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        goto L_0x00d8;
    L_0x00ba:
        r11 = TAG;	 Catch:{ Exception -> 0x00d1 }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00d1 }
        r0.<init>();	 Catch:{ Exception -> 0x00d1 }
        r2 = "Unsupported EXIF orientation: ";
        r0.append(r2);	 Catch:{ Exception -> 0x00d1 }
        r0.append(r10);	 Catch:{ Exception -> 0x00d1 }
        r10 = r0.toString();	 Catch:{ Exception -> 0x00d1 }
        android.util.Log.w(r11, r10);	 Catch:{ Exception -> 0x00d1 }
        goto L_0x00d8;
    L_0x00d1:
        r10 = TAG;
        r11 = "Could not get EXIF orientation of image";
        android.util.Log.w(r10, r11);
    L_0x00d8:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.getExifOrientation(android.content.Context, java.lang.String):int");
    }

    private void execute(AsyncTask<Void, Void, ?> asyncTask) {
        if (this.parallelLoadingEnabled && VERSION.SDK_INT >= 11) {
            try {
                Executor executor = (Executor) AsyncTask.class.getField("THREAD_POOL_EXECUTOR").get(null);
                AsyncTask.class.getMethod("executeOnExecutor", new Class[]{Executor.class, Object[].class}).invoke(asyncTask, new Object[]{executor, null});
                return;
            } catch (Exception e) {
                Log.i(TAG, "Failed to execute AsyncTask on thread pool executor, falling back to single threaded executor", e);
            }
        }
        asyncTask.execute(new Void[0]);
    }

    private void restoreState(ImageViewState imageViewState) {
        if (imageViewState != null && imageViewState.getCenter() != null && VALID_ORIENTATIONS.contains(Integer.valueOf(imageViewState.getOrientation()))) {
            this.orientation = imageViewState.getOrientation();
            this.pendingScale = Float.valueOf(imageViewState.getScale());
            this.sPendingCenter = imageViewState.getCenter();
            invalidate();
        }
    }

    public void setMaxTileSize(int i) {
        this.maxTileWidth = i;
        this.maxTileHeight = i;
    }

    private Point getMaxBitmapDimensions(Canvas canvas) {
        int intValue;
        int i = 2048;
        if (VERSION.SDK_INT >= 14) {
            try {
                intValue = ((Integer) Canvas.class.getMethod("getMaximumBitmapWidth", new Class[0]).invoke(canvas, new Object[0])).intValue();
                try {
                    i = ((Integer) Canvas.class.getMethod("getMaximumBitmapHeight", new Class[0]).invoke(canvas, new Object[0])).intValue();
                } catch (Exception unused) {
                }
            } catch (Exception unused2) {
            }
            return new Point(Math.min(intValue, this.maxTileWidth), Math.min(i, this.maxTileHeight));
        }
        intValue = 2048;
        return new Point(Math.min(intValue, this.maxTileWidth), Math.min(i, this.maxTileHeight));
    }

    private int sWidth() {
        int requiredRotation = getRequiredRotation();
        if (requiredRotation == 90 || requiredRotation == 270) {
            return this.sHeight;
        }
        return this.sWidth;
    }

    private int sHeight() {
        int requiredRotation = getRequiredRotation();
        if (requiredRotation == 90 || requiredRotation == 270) {
            return this.sWidth;
        }
        return this.sHeight;
    }

    private void fileSRect(Rect rect, Rect rect2) {
        if (getRequiredRotation() == 0) {
            rect2.set(rect);
        } else if (getRequiredRotation() == 90) {
            rect2.set(rect.top, this.sHeight - rect.right, rect.bottom, this.sHeight - rect.left);
        } else if (getRequiredRotation() == 180) {
            rect2.set(this.sWidth - rect.right, this.sHeight - rect.bottom, this.sWidth - rect.left, this.sHeight - rect.top);
        } else {
            rect2.set(this.sWidth - rect.bottom, rect.left, this.sWidth - rect.top, rect.right);
        }
    }

    private int getRequiredRotation() {
        if (this.orientation == -1) {
            return this.sOrientation;
        }
        return this.orientation;
    }

    private float distance(float f, float f2, float f3, float f4) {
        f -= f2;
        f3 -= f4;
        return (float) Math.sqrt((double) ((f * f) + (f3 * f3)));
    }

    private float viewToSourceX(float f) {
        if (this.vTranslate == null) {
            return Float.NaN;
        }
        return (f - this.vTranslate.x) / this.scale;
    }

    private float viewToSourceY(float f) {
        if (this.vTranslate == null) {
            return Float.NaN;
        }
        return (f - this.vTranslate.y) / this.scale;
    }

    public final PointF viewToSourceCoord(PointF pointF) {
        return viewToSourceCoord(pointF.x, pointF.y, new PointF());
    }

    public final PointF viewToSourceCoord(float f, float f2) {
        return viewToSourceCoord(f, f2, new PointF());
    }

    public final PointF viewToSourceCoord(float f, float f2, PointF pointF) {
        if (this.vTranslate == null) {
            return null;
        }
        pointF.set(viewToSourceX(f), viewToSourceY(f2));
        return pointF;
    }

    private float sourceToViewX(float f) {
        if (this.vTranslate == null) {
            return Float.NaN;
        }
        return (f * this.scale) + this.vTranslate.x;
    }

    private float sourceToViewY(float f) {
        if (this.vTranslate == null) {
            return Float.NaN;
        }
        return (f * this.scale) + this.vTranslate.y;
    }

    public final PointF sourceToViewCoord(PointF pointF) {
        return sourceToViewCoord(pointF.x, pointF.y, new PointF());
    }

    public final PointF sourceToViewCoord(float f, float f2, PointF pointF) {
        if (this.vTranslate == null) {
            return null;
        }
        pointF.set(sourceToViewX(f), sourceToViewY(f2));
        return pointF;
    }

    private Rect sourceToViewRect(Rect rect, Rect rect2) {
        rect2.set((int) sourceToViewX((float) rect.left), (int) sourceToViewY((float) rect.top), (int) sourceToViewX((float) rect.right), (int) sourceToViewY((float) rect.bottom));
        return rect2;
    }

    private PointF vTranslateForSCenter(float f, float f2, float f3) {
        int paddingLeft = getPaddingLeft() + (((getWidth() - getPaddingRight()) - getPaddingLeft()) / 2);
        int paddingTop = getPaddingTop() + (((getHeight() - getPaddingBottom()) - getPaddingTop()) / 2);
        if (this.satTemp == null) {
            this.satTemp = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f), null);
        }
        this.satTemp.scale = f3;
        this.satTemp.vTranslate.set(((float) paddingLeft) - (f * f3), ((float) paddingTop) - (f2 * f3));
        fitToBounds(true, this.satTemp);
        return this.satTemp.vTranslate;
    }

    private PointF limitedSCenter(float f, float f2, float f3, PointF pointF) {
        PointF vTranslateForSCenter = vTranslateForSCenter(f, f2, f3);
        pointF.set((((float) (getPaddingLeft() + (((getWidth() - getPaddingRight()) - getPaddingLeft()) / 2))) - vTranslateForSCenter.x) / f3, (((float) (getPaddingTop() + (((getHeight() - getPaddingBottom()) - getPaddingTop()) / 2))) - vTranslateForSCenter.y) / f3);
        return pointF;
    }

    private float minScale() {
        int paddingBottom = getPaddingBottom() + getPaddingTop();
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        if (this.minimumScaleType == 2) {
            return Math.max(((float) (getWidth() - paddingLeft)) / ((float) sWidth()), ((float) (getHeight() - paddingBottom)) / ((float) sHeight()));
        }
        if (this.minimumScaleType != 3 || this.minScale <= 0.0f) {
            return Math.min(((float) (getWidth() - paddingLeft)) / ((float) sWidth()), ((float) (getHeight() - paddingBottom)) / ((float) sHeight()));
        }
        return this.minScale;
    }

    private float limitedScale(float f) {
        return Math.min(this.maxScale, Math.max(minScale(), f));
    }

    private float ease(int i, long j, float f, float f2, long j2) {
        switch (i) {
            case 1:
                return easeOutQuad(j, f, f2, j2);
            case 2:
                return easeInOutQuad(j, f, f2, j2);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected easing type: ");
                stringBuilder.append(i);
                throw new IllegalStateException(stringBuilder.toString());
        }
    }

    private void debug(String str, Object... objArr) {
        if (this.debug) {
            Log.d(TAG, String.format(str, objArr));
        }
    }

    public final void setRegionDecoderClass(Class<? extends ImageRegionDecoder> cls) {
        if (cls != null) {
            this.regionDecoderFactory = new CompatDecoderFactory(cls);
            return;
        }
        throw new IllegalArgumentException("Decoder class cannot be set to null");
    }

    public final void setRegionDecoderFactory(DecoderFactory<? extends ImageRegionDecoder> decoderFactory) {
        if (decoderFactory != null) {
            this.regionDecoderFactory = decoderFactory;
            return;
        }
        throw new IllegalArgumentException("Decoder factory cannot be set to null");
    }

    public final void setBitmapDecoderClass(Class<? extends ImageDecoder> cls) {
        if (cls != null) {
            this.bitmapDecoderFactory = new CompatDecoderFactory(cls);
            return;
        }
        throw new IllegalArgumentException("Decoder class cannot be set to null");
    }

    public final void setBitmapDecoderFactory(DecoderFactory<? extends ImageDecoder> decoderFactory) {
        if (decoderFactory != null) {
            this.bitmapDecoderFactory = decoderFactory;
            return;
        }
        throw new IllegalArgumentException("Decoder factory cannot be set to null");
    }

    public final void setPanLimit(int i) {
        if (VALID_PAN_LIMITS.contains(Integer.valueOf(i))) {
            this.panLimit = i;
            if (isReady()) {
                fitToBounds(true);
                invalidate();
                return;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid pan limit: ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public final void setMinimumScaleType(int i) {
        if (VALID_SCALE_TYPES.contains(Integer.valueOf(i))) {
            this.minimumScaleType = i;
            if (isReady()) {
                fitToBounds(true);
                invalidate();
                return;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid scale type: ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public final void setMaxScale(float f) {
        this.maxScale = f;
    }

    public final void setMinScale(float f) {
        this.minScale = f;
    }

    public final void setMinimumDpi(int i) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        setMaxScale(((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f) / ((float) i));
    }

    public final void setMaximumDpi(int i) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        setMinScale(((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f) / ((float) i));
    }

    public float getMaxScale() {
        return this.maxScale;
    }

    public final float getMinScale() {
        return minScale();
    }

    public void setMinimumTileDpi(int i) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.minimumTileDpi = (int) Math.min((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f, (float) i);
        if (isReady()) {
            reset(false);
            invalidate();
        }
    }

    public final PointF getCenter() {
        return viewToSourceCoord((float) (getWidth() / 2), (float) (getHeight() / 2));
    }

    public final float getScale() {
        return this.scale;
    }

    public final void setScaleAndCenter(float f, PointF pointF) {
        this.anim = null;
        this.pendingScale = Float.valueOf(f);
        this.sPendingCenter = pointF;
        this.sRequestedCenter = pointF;
        invalidate();
    }

    public final boolean isReady() {
        return this.readySent;
    }

    public final boolean isImageLoaded() {
        return this.imageLoadedSent;
    }

    public final int getSWidth() {
        return this.sWidth;
    }

    public final int getSHeight() {
        return this.sHeight;
    }

    public final int getOrientation() {
        return this.orientation;
    }

    public final int getAppliedOrientation() {
        return getRequiredRotation();
    }

    public final ImageViewState getState() {
        return (this.vTranslate == null || this.sWidth <= 0 || this.sHeight <= 0) ? null : new ImageViewState(getScale(), getCenter(), getOrientation());
    }

    public final void setZoomEnabled(boolean z) {
        this.zoomEnabled = z;
    }

    public final void setQuickScaleEnabled(boolean z) {
        this.quickScaleEnabled = z;
    }

    public final void setPanEnabled(boolean z) {
        this.panEnabled = z;
        if (!z && this.vTranslate != null) {
            this.vTranslate.x = ((float) (getWidth() / 2)) - (this.scale * ((float) (sWidth() / 2)));
            this.vTranslate.y = ((float) (getHeight() / 2)) - (this.scale * ((float) (sHeight() / 2)));
            if (isReady()) {
                refreshRequiredTiles(true);
                invalidate();
            }
        }
    }

    public final void setTileBackgroundColor(int i) {
        if (Color.alpha(i) == 0) {
            this.tileBgPaint = null;
        } else {
            this.tileBgPaint = new Paint();
            this.tileBgPaint.setStyle(Style.FILL);
            this.tileBgPaint.setColor(i);
        }
        invalidate();
    }

    public final void setDoubleTapZoomScale(float f) {
        this.doubleTapZoomScale = f;
    }

    public final void setDoubleTapZoomDpi(int i) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        setDoubleTapZoomScale(((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f) / ((float) i));
    }

    public final void setDoubleTapZoomStyle(int i) {
        if (VALID_ZOOM_STYLES.contains(Integer.valueOf(i))) {
            this.doubleTapZoomStyle = i;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid zoom style: ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public final void setDoubleTapZoomDuration(int i) {
        this.doubleTapZoomDuration = Math.max(0, i);
    }

    public void setParallelLoadingEnabled(boolean z) {
        this.parallelLoadingEnabled = z;
    }

    public final void setDebug(boolean z) {
        this.debug = z;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void setOnImageEventListener(OnImageEventListener onImageEventListener) {
        this.onImageEventListener = onImageEventListener;
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    private void sendStateChanged(float f, PointF pointF, int i) {
        if (this.onStateChangedListener != null) {
            if (this.scale != f) {
                this.onStateChangedListener.onScaleChanged(this.scale, i);
            }
            if (!this.vTranslate.equals(pointF)) {
                this.onStateChangedListener.onCenterChanged(getCenter(), i);
            }
        }
    }
}

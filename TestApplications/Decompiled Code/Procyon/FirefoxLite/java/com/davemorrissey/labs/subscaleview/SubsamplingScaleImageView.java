// 
// Decompiled by Procyon v0.5.34
// 

package com.davemorrissey.labs.subscaleview;

import java.lang.ref.WeakReference;
import android.view.View$MeasureSpec;
import java.util.Locale;
import android.view.GestureDetector$OnGestureListener;
import android.view.GestureDetector$SimpleOnGestureListener;
import android.view.ViewParent;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import android.graphics.Point;
import android.graphics.Canvas;
import android.database.Cursor;
import android.media.ExifInterface;
import java.util.concurrent.Executor;
import android.os.Build$VERSION;
import android.os.AsyncTask;
import android.util.Log;
import android.graphics.Paint$Style;
import android.util.DisplayMetrics;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.graphics.Color;
import android.os.Message;
import android.os.Handler$Callback;
import com.davemorrissey.labs.subscaleview.decoder.SkiaImageRegionDecoder;
import com.davemorrissey.labs.subscaleview.decoder.CompatDecoderFactory;
import com.davemorrissey.labs.subscaleview.decoder.SkiaImageDecoder;
import android.util.AttributeSet;
import android.content.Context;
import java.util.Arrays;
import android.net.Uri;
import java.util.Map;
import android.graphics.RectF;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View$OnLongClickListener;
import android.graphics.Matrix;
import android.os.Handler;
import android.view.GestureDetector;
import com.davemorrissey.labs.subscaleview.decoder.ImageRegionDecoder;
import android.graphics.Paint;
import com.davemorrissey.labs.subscaleview.decoder.ImageDecoder;
import com.davemorrissey.labs.subscaleview.decoder.DecoderFactory;
import android.graphics.Bitmap;
import java.util.List;
import android.view.View;

public class SubsamplingScaleImageView extends View
{
    private static final String TAG = "SubsamplingScaleImageView";
    public static int TILE_SIZE_AUTO;
    private static final List<Integer> VALID_EASING_STYLES;
    private static final List<Integer> VALID_ORIENTATIONS;
    private static final List<Integer> VALID_PAN_LIMITS;
    private static final List<Integer> VALID_SCALE_TYPES;
    private static final List<Integer> VALID_ZOOM_STYLES;
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
    private View$OnLongClickListener onLongClickListener;
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
    
    static {
        VALID_ORIENTATIONS = Arrays.asList(0, 90, 180, 270, -1);
        VALID_ZOOM_STYLES = Arrays.asList(1, 2, 3);
        VALID_EASING_STYLES = Arrays.asList(2, 1);
        VALID_PAN_LIMITS = Arrays.asList(1, 2, 3);
        VALID_SCALE_TYPES = Arrays.asList(2, 1, 3);
        SubsamplingScaleImageView.TILE_SIZE_AUTO = Integer.MAX_VALUE;
    }
    
    public SubsamplingScaleImageView(final Context context) {
        this(context, null);
    }
    
    public SubsamplingScaleImageView(final Context gestureDetector, final AttributeSet set) {
        super(gestureDetector, set);
        this.orientation = 0;
        this.maxScale = 2.0f;
        this.minScale = this.minScale();
        this.minimumTileDpi = -1;
        this.panLimit = 1;
        this.minimumScaleType = 1;
        this.maxTileWidth = SubsamplingScaleImageView.TILE_SIZE_AUTO;
        this.maxTileHeight = SubsamplingScaleImageView.TILE_SIZE_AUTO;
        this.panEnabled = true;
        this.zoomEnabled = true;
        this.quickScaleEnabled = true;
        this.doubleTapZoomScale = 1.0f;
        this.doubleTapZoomStyle = 1;
        this.doubleTapZoomDuration = 500;
        this.decoderLock = new Object();
        this.bitmapDecoderFactory = new CompatDecoderFactory<ImageDecoder>(SkiaImageDecoder.class);
        this.regionDecoderFactory = new CompatDecoderFactory<ImageRegionDecoder>(SkiaImageRegionDecoder.class);
        this.srcArray = new float[8];
        this.dstArray = new float[8];
        this.density = this.getResources().getDisplayMetrics().density;
        this.setMinimumDpi(160);
        this.setDoubleTapZoomDpi(160);
        this.setGestureDetector(gestureDetector);
        this.handler = new Handler((Handler$Callback)new Handler$Callback() {
            public boolean handleMessage(final Message message) {
                if (message.what == 1 && SubsamplingScaleImageView.this.onLongClickListener != null) {
                    SubsamplingScaleImageView.this.maxTouchCount = 0;
                    SubsamplingScaleImageView.access$201(SubsamplingScaleImageView.this, SubsamplingScaleImageView.this.onLongClickListener);
                    SubsamplingScaleImageView.this.performLongClick();
                    SubsamplingScaleImageView.access$301(SubsamplingScaleImageView.this, null);
                }
                return true;
            }
        });
        if (set != null) {
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.SubsamplingScaleImageView);
            if (obtainStyledAttributes.hasValue(R.styleable.SubsamplingScaleImageView_assetName)) {
                final String string = obtainStyledAttributes.getString(R.styleable.SubsamplingScaleImageView_assetName);
                if (string != null && string.length() > 0) {
                    this.setImage(ImageSource.asset(string).tilingEnabled());
                }
            }
            if (obtainStyledAttributes.hasValue(R.styleable.SubsamplingScaleImageView_src)) {
                final int resourceId = obtainStyledAttributes.getResourceId(R.styleable.SubsamplingScaleImageView_src, 0);
                if (resourceId > 0) {
                    this.setImage(ImageSource.resource(resourceId).tilingEnabled());
                }
            }
            if (obtainStyledAttributes.hasValue(R.styleable.SubsamplingScaleImageView_panEnabled)) {
                this.setPanEnabled(obtainStyledAttributes.getBoolean(R.styleable.SubsamplingScaleImageView_panEnabled, true));
            }
            if (obtainStyledAttributes.hasValue(R.styleable.SubsamplingScaleImageView_zoomEnabled)) {
                this.setZoomEnabled(obtainStyledAttributes.getBoolean(R.styleable.SubsamplingScaleImageView_zoomEnabled, true));
            }
            if (obtainStyledAttributes.hasValue(R.styleable.SubsamplingScaleImageView_quickScaleEnabled)) {
                this.setQuickScaleEnabled(obtainStyledAttributes.getBoolean(R.styleable.SubsamplingScaleImageView_quickScaleEnabled, true));
            }
            if (obtainStyledAttributes.hasValue(R.styleable.SubsamplingScaleImageView_tileBackgroundColor)) {
                this.setTileBackgroundColor(obtainStyledAttributes.getColor(R.styleable.SubsamplingScaleImageView_tileBackgroundColor, Color.argb(0, 0, 0, 0)));
            }
            obtainStyledAttributes.recycle();
        }
        this.quickScaleThreshold = TypedValue.applyDimension(1, 20.0f, gestureDetector.getResources().getDisplayMetrics());
    }
    
    static /* synthetic */ void access$201(final SubsamplingScaleImageView subsamplingScaleImageView, final View$OnLongClickListener onLongClickListener) {
        subsamplingScaleImageView.setOnLongClickListener(onLongClickListener);
    }
    
    static /* synthetic */ void access$301(final SubsamplingScaleImageView subsamplingScaleImageView, final View$OnLongClickListener onLongClickListener) {
        subsamplingScaleImageView.setOnLongClickListener(onLongClickListener);
    }
    
    private int calculateInSampleSize(final float n) {
        float n2 = n;
        if (this.minimumTileDpi > 0) {
            final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
            n2 = n * (this.minimumTileDpi / ((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f));
        }
        final int n3 = (int)(this.sWidth() * n2);
        final int n4 = (int)(this.sHeight() * n2);
        if (n3 != 0 && n4 != 0) {
            final int sHeight = this.sHeight();
            int n5 = 1;
            int round;
            if (sHeight <= n4 && this.sWidth() <= n3) {
                round = 1;
            }
            else {
                round = Math.round(this.sHeight() / (float)n4);
                final int round2 = Math.round(this.sWidth() / (float)n3);
                if (round >= round2) {
                    round = round2;
                }
            }
            while (true) {
                final int n6 = n5 * 2;
                if (n6 >= round) {
                    break;
                }
                n5 = n6;
            }
            return n5;
        }
        return 32;
    }
    
    private boolean checkImageLoaded() {
        final boolean baseLayerReady = this.isBaseLayerReady();
        if (!this.imageLoadedSent && baseLayerReady) {
            this.preDraw();
            this.imageLoadedSent = true;
            this.onImageLoaded();
            if (this.onImageEventListener != null) {
                this.onImageEventListener.onImageLoaded();
            }
        }
        return baseLayerReady;
    }
    
    private boolean checkReady() {
        final boolean b = this.getWidth() > 0 && this.getHeight() > 0 && this.sWidth > 0 && this.sHeight > 0 && (this.bitmap != null || this.isBaseLayerReady());
        if (!this.readySent && b) {
            this.preDraw();
            this.readySent = true;
            this.onReady();
            if (this.onImageEventListener != null) {
                this.onImageEventListener.onReady();
            }
        }
        return b;
    }
    
    private void createPaints() {
        if (this.bitmapPaint == null) {
            (this.bitmapPaint = new Paint()).setAntiAlias(true);
            this.bitmapPaint.setFilterBitmap(true);
            this.bitmapPaint.setDither(true);
        }
        if (this.debugPaint == null && this.debug) {
            (this.debugPaint = new Paint()).setTextSize(18.0f);
            this.debugPaint.setColor(-65281);
            this.debugPaint.setStyle(Paint$Style.STROKE);
        }
    }
    
    private void debug(final String format, final Object... args) {
        if (this.debug) {
            Log.d(SubsamplingScaleImageView.TAG, String.format(format, args));
        }
    }
    
    private float distance(float n, float n2, final float n3, final float n4) {
        n -= n2;
        n2 = n3 - n4;
        return (float)Math.sqrt(n * n + n2 * n2);
    }
    
    private void doubleTapZoom(final PointF pointF, final PointF pointF2) {
        if (!this.panEnabled) {
            if (this.sRequestedCenter != null) {
                pointF.x = this.sRequestedCenter.x;
                pointF.y = this.sRequestedCenter.y;
            }
            else {
                pointF.x = (float)(this.sWidth() / 2);
                pointF.y = (float)(this.sHeight() / 2);
            }
        }
        float n = Math.min(this.maxScale, this.doubleTapZoomScale);
        final boolean b = this.scale <= n * 0.9;
        if (!b) {
            n = this.minScale();
        }
        if (this.doubleTapZoomStyle == 3) {
            this.setScaleAndCenter(n, pointF);
        }
        else if (this.doubleTapZoomStyle != 2 && b && this.panEnabled) {
            if (this.doubleTapZoomStyle == 1) {
                new AnimationBuilder(n, pointF, pointF2).withInterruptible(false).withDuration(this.doubleTapZoomDuration).withOrigin(4).start();
            }
        }
        else {
            new AnimationBuilder(n, pointF).withInterruptible(false).withDuration(this.doubleTapZoomDuration).withOrigin(4).start();
        }
        this.invalidate();
    }
    
    private float ease(final int i, final long n, final float n2, final float n3, final long n4) {
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected easing type: ");
                sb.append(i);
                throw new IllegalStateException(sb.toString());
            }
            case 2: {
                return this.easeInOutQuad(n, n2, n3, n4);
            }
            case 1: {
                return this.easeOutQuad(n, n2, n3, n4);
            }
        }
    }
    
    private float easeInOutQuad(final long n, final float n2, final float n3, final long n4) {
        final float n5 = n / (n4 / 2.0f);
        if (n5 < 1.0f) {
            return n3 / 2.0f * n5 * n5 + n2;
        }
        final float n6 = n5 - 1.0f;
        return -n3 / 2.0f * (n6 * (n6 - 2.0f) - 1.0f) + n2;
    }
    
    private float easeOutQuad(final long n, final float n2, final float n3, final long n4) {
        final float n5 = n / (float)n4;
        return -n3 * n5 * (n5 - 2.0f) + n2;
    }
    
    private void execute(final AsyncTask<Void, Void, ?> obj) {
        if (this.parallelLoadingEnabled && Build$VERSION.SDK_INT >= 11) {
            try {
                AsyncTask.class.getMethod("executeOnExecutor", Executor.class, Object[].class).invoke(obj, (Executor)AsyncTask.class.getField("THREAD_POOL_EXECUTOR").get(null), null);
                return;
            }
            catch (Exception ex) {
                Log.i(SubsamplingScaleImageView.TAG, "Failed to execute AsyncTask on thread pool executor, falling back to single threaded executor", (Throwable)ex);
            }
        }
        obj.execute((Object[])new Void[0]);
    }
    
    private void fileSRect(final Rect rect, final Rect rect2) {
        if (this.getRequiredRotation() == 0) {
            rect2.set(rect);
        }
        else if (this.getRequiredRotation() == 90) {
            rect2.set(rect.top, this.sHeight - rect.right, rect.bottom, this.sHeight - rect.left);
        }
        else if (this.getRequiredRotation() == 180) {
            rect2.set(this.sWidth - rect.right, this.sHeight - rect.bottom, this.sWidth - rect.left, this.sHeight - rect.top);
        }
        else {
            rect2.set(this.sWidth - rect.bottom, rect.left, this.sWidth - rect.top, rect.right);
        }
    }
    
    private void fitToBounds(final boolean b) {
        boolean b2;
        if (this.vTranslate == null) {
            b2 = true;
            this.vTranslate = new PointF(0.0f, 0.0f);
        }
        else {
            b2 = false;
        }
        if (this.satTemp == null) {
            this.satTemp = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f));
        }
        this.satTemp.scale = this.scale;
        this.satTemp.vTranslate.set(this.vTranslate);
        this.fitToBounds(b, this.satTemp);
        this.scale = this.satTemp.scale;
        this.vTranslate.set(this.satTemp.vTranslate);
        if (b2) {
            this.vTranslate.set(this.vTranslateForSCenter((float)(this.sWidth() / 2), (float)(this.sHeight() / 2), this.scale));
        }
    }
    
    private void fitToBounds(final boolean b, final ScaleAndTranslate scaleAndTranslate) {
        boolean b2 = b;
        if (this.panLimit == 2) {
            b2 = b;
            if (this.isReady()) {
                b2 = false;
            }
        }
        final PointF access$4800 = scaleAndTranslate.vTranslate;
        final float limitedScale = this.limitedScale(scaleAndTranslate.scale);
        final float n = this.sWidth() * limitedScale;
        final float n2 = this.sHeight() * limitedScale;
        if (this.panLimit == 3 && this.isReady()) {
            access$4800.x = Math.max(access$4800.x, this.getWidth() / 2 - n);
            access$4800.y = Math.max(access$4800.y, this.getHeight() / 2 - n2);
        }
        else if (b2) {
            access$4800.x = Math.max(access$4800.x, this.getWidth() - n);
            access$4800.y = Math.max(access$4800.y, this.getHeight() - n2);
        }
        else {
            access$4800.x = Math.max(access$4800.x, -n);
            access$4800.y = Math.max(access$4800.y, -n2);
        }
        final int paddingLeft = this.getPaddingLeft();
        float n3 = 0.5f;
        float n4;
        if (paddingLeft <= 0 && this.getPaddingRight() <= 0) {
            n4 = 0.5f;
        }
        else {
            n4 = this.getPaddingLeft() / (float)(this.getPaddingLeft() + this.getPaddingRight());
        }
        if (this.getPaddingTop() > 0 || this.getPaddingBottom() > 0) {
            n3 = this.getPaddingTop() / (float)(this.getPaddingTop() + this.getPaddingBottom());
        }
        float b3;
        float max;
        if (this.panLimit == 3 && this.isReady()) {
            b3 = (float)Math.max(0, this.getWidth() / 2);
            max = (float)Math.max(0, this.getHeight() / 2);
        }
        else if (b2) {
            final float max2 = Math.max(0.0f, (this.getWidth() - n) * n4);
            max = Math.max(0.0f, (this.getHeight() - n2) * n3);
            b3 = max2;
        }
        else {
            b3 = (float)Math.max(0, this.getWidth());
            max = (float)Math.max(0, this.getHeight());
        }
        access$4800.x = Math.min(access$4800.x, b3);
        access$4800.y = Math.min(access$4800.y, max);
        scaleAndTranslate.scale = limitedScale;
    }
    
    private int getExifOrientation(Context query, String tag) {
        final boolean startsWith = tag.startsWith("content");
        final int n = 0;
        Label_0211: {
            if (!startsWith) {
                break Label_0211;
            }
            final Cursor cursor = null;
            Object tag2 = null;
            while (true) {
                try {
                    int int1 = 0;
                    Object o = null;
                    Label_0199: {
                        try {
                            query = (Context)query.getContentResolver().query(Uri.parse(tag), new String[] { "orientation" }, (String)null, (String[])null, (String)null);
                            Label_0145: {
                                if (query != null) {
                                    try {
                                        if (((Cursor)query).moveToFirst()) {
                                            int1 = ((Cursor)query).getInt(0);
                                            if (SubsamplingScaleImageView.VALID_ORIENTATIONS.contains(int1) && int1 != -1) {
                                                break Label_0145;
                                            }
                                            tag2 = SubsamplingScaleImageView.TAG;
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("Unsupported orientation: ");
                                            sb.append(int1);
                                            Log.w((String)tag2, sb.toString());
                                        }
                                    }
                                    catch (Exception ex) {
                                        break Label_0165;
                                    }
                                    finally {
                                        break Label_0199;
                                    }
                                }
                                int1 = 0;
                            }
                            if (query != null) {
                                ((Cursor)query).close();
                            }
                            return int1;
                        }
                        finally {
                            o = tag2;
                            break Label_0199;
                        }
                        Log.w(SubsamplingScaleImageView.TAG, "Could not get orientation of image from media store");
                        int1 = n;
                        if (o != null) {
                            ((Cursor)o).close();
                            int1 = n;
                            return int1;
                        }
                        return int1;
                    }
                    if (o != null) {
                        ((Cursor)o).close();
                    }
                    Label_0392: {
                        return int1;
                    }
                    // iftrue(Label_0392:, tag.startsWith("file:///android_asset/"))
                    // iftrue(Label_0392:, !tag.startsWith("file:///"))
                Block_7:
                    while (true) {
                        int1 = n;
                        break Block_7;
                        int1 = n;
                        continue;
                    }
                    try {
                        final int attributeInt = new ExifInterface(tag.substring("file:///".length() - 1)).getAttributeInt("Orientation", 1);
                        int1 = n;
                        if (attributeInt != 1) {
                            if (attributeInt == 0) {
                                int1 = n;
                            }
                            else if (attributeInt == 6) {
                                int1 = 90;
                            }
                            else if (attributeInt == 3) {
                                int1 = 180;
                            }
                            else if (attributeInt == 8) {
                                int1 = 270;
                            }
                            else {
                                tag = SubsamplingScaleImageView.TAG;
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Unsupported EXIF orientation: ");
                                sb2.append(attributeInt);
                                Log.w(tag, sb2.toString());
                                int1 = n;
                            }
                        }
                    }
                    catch (Exception ex2) {
                        Log.w(SubsamplingScaleImageView.TAG, "Could not get EXIF orientation of image");
                        int1 = n;
                    }
                    return int1;
                }
                catch (Exception ex3) {
                    final Object o = cursor;
                    continue;
                }
                break;
            }
        }
    }
    
    private Point getMaxBitmapDimensions(final Canvas p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: istore_2       
        //     4: sipush          2048
        //     7: istore_3       
        //     8: iload_2        
        //     9: bipush          14
        //    11: if_icmplt       77
        //    14: ldc_w           Landroid/graphics/Canvas;.class
        //    17: ldc_w           "getMaximumBitmapWidth"
        //    20: iconst_0       
        //    21: anewarray       Ljava/lang/Class;
        //    24: invokevirtual   java/lang/Class.getMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    27: aload_1        
        //    28: iconst_0       
        //    29: anewarray       Ljava/lang/Object;
        //    32: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    35: checkcast       Ljava/lang/Integer;
        //    38: invokevirtual   java/lang/Integer.intValue:()I
        //    41: istore_2       
        //    42: ldc_w           Landroid/graphics/Canvas;.class
        //    45: ldc_w           "getMaximumBitmapHeight"
        //    48: iconst_0       
        //    49: anewarray       Ljava/lang/Class;
        //    52: invokevirtual   java/lang/Class.getMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    55: aload_1        
        //    56: iconst_0       
        //    57: anewarray       Ljava/lang/Object;
        //    60: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    63: checkcast       Ljava/lang/Integer;
        //    66: invokevirtual   java/lang/Integer.intValue:()I
        //    69: istore          4
        //    71: iload           4
        //    73: istore_3       
        //    74: goto            81
        //    77: sipush          2048
        //    80: istore_2       
        //    81: new             Landroid/graphics/Point;
        //    84: dup            
        //    85: iload_2        
        //    86: aload_0        
        //    87: getfield        com/davemorrissey/labs/subscaleview/SubsamplingScaleImageView.maxTileWidth:I
        //    90: invokestatic    java/lang/Math.min:(II)I
        //    93: iload_3        
        //    94: aload_0        
        //    95: getfield        com/davemorrissey/labs/subscaleview/SubsamplingScaleImageView.maxTileHeight:I
        //    98: invokestatic    java/lang/Math.min:(II)I
        //   101: invokespecial   android/graphics/Point.<init>:(II)V
        //   104: areturn        
        //   105: astore_1       
        //   106: goto            77
        //   109: astore_1       
        //   110: goto            81
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  14     42     105    109    Ljava/lang/Exception;
        //  42     71     109    113    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0077:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private int getRequiredRotation() {
        if (this.orientation == -1) {
            return this.sOrientation;
        }
        return this.orientation;
    }
    
    private void initialiseBaseLayer(final Point point) {
        synchronized (this) {
            this.debug("initialiseBaseLayer maxTileDimensions=%dx%d", point.x, point.y);
            this.fitToBounds(true, this.satTemp = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f)));
            this.fullImageSampleSize = this.calculateInSampleSize(this.satTemp.scale);
            if (this.fullImageSampleSize > 1) {
                this.fullImageSampleSize /= 2;
            }
            if (this.fullImageSampleSize == 1 && this.sRegion == null && this.sWidth() < point.x && this.sHeight() < point.y) {
                this.decoder.recycle();
                this.decoder = null;
                this.execute(new BitmapLoadTask(this, this.getContext(), this.bitmapDecoderFactory, this.uri, false));
            }
            else {
                this.initialiseTileMap(point);
                final Iterator<Tile> iterator = this.tileMap.get(this.fullImageSampleSize).iterator();
                while (iterator.hasNext()) {
                    this.execute(new TileLoadTask(this, this.decoder, (Tile)iterator.next()));
                }
                this.refreshRequiredTiles(true);
            }
        }
    }
    
    private void initialiseTileMap(final Point point) {
        this.debug("initialiseTileMap maxTileDimensions=%dx%d", point.x, point.y);
        this.tileMap = new LinkedHashMap<Integer, List<Tile>>();
        int fullImageSampleSize = this.fullImageSampleSize;
        int n = 1;
        int n2 = 1;
        while (true) {
            int n3 = this.sWidth() / n;
            final int n4 = this.sHeight() / n2;
            int n5 = n3 / fullImageSampleSize;
            final int n6 = n4 / fullImageSampleSize;
            int n7;
            int n8;
            int n9;
            while (true) {
                if (n5 + n + 1 <= point.x) {
                    n7 = n2;
                    n8 = n4;
                    n9 = n6;
                    if (n5 <= this.getWidth() * 1.25) {
                        break;
                    }
                    n7 = n2;
                    n8 = n4;
                    n9 = n6;
                    if (fullImageSampleSize >= this.fullImageSampleSize) {
                        break;
                    }
                }
                ++n;
                n3 = this.sWidth() / n;
                n5 = n3 / fullImageSampleSize;
            }
            while (n9 + n7 + 1 > point.y || (n9 > this.getHeight() * 1.25 && fullImageSampleSize < this.fullImageSampleSize)) {
                ++n7;
                n8 = this.sHeight() / n7;
                n9 = n8 / fullImageSampleSize;
            }
            final ArrayList list = new ArrayList<Tile>(n * n7);
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n7; ++j) {
                    final Tile tile = new Tile();
                    tile.sampleSize = fullImageSampleSize;
                    tile.visible = (fullImageSampleSize == this.fullImageSampleSize);
                    int sWidth;
                    if (i == n - 1) {
                        sWidth = this.sWidth();
                    }
                    else {
                        sWidth = (i + 1) * n3;
                    }
                    int sHeight;
                    if (j == n7 - 1) {
                        sHeight = this.sHeight();
                    }
                    else {
                        sHeight = (j + 1) * n8;
                    }
                    tile.sRect = new Rect(i * n3, j * n8, sWidth, sHeight);
                    tile.vRect = new Rect(0, 0, 0, 0);
                    tile.fileSRect = new Rect(tile.sRect);
                    list.add(tile);
                }
            }
            this.tileMap.put(fullImageSampleSize, (ArrayList<Tile>)list);
            if (fullImageSampleSize == 1) {
                break;
            }
            fullImageSampleSize /= 2;
            n2 = n7;
        }
    }
    
    private boolean isBaseLayerReady() {
        final Bitmap bitmap = this.bitmap;
        boolean b = true;
        if (bitmap != null && !this.bitmapIsPreview) {
            return true;
        }
        if (this.tileMap != null) {
            for (final Map.Entry<Integer, List<Tile>> entry : this.tileMap.entrySet()) {
                if (entry.getKey() == this.fullImageSampleSize) {
                    final Iterator<Tile> iterator2 = entry.getValue().iterator();
                    boolean b2 = b;
                    while (true) {
                        b = b2;
                        if (!iterator2.hasNext()) {
                            break;
                        }
                        final Tile tile = iterator2.next();
                        if (!tile.loading && tile.bitmap != null) {
                            continue;
                        }
                        b2 = false;
                    }
                }
            }
            return b;
        }
        return false;
    }
    
    private PointF limitedSCenter(final float n, final float n2, final float n3, final PointF pointF) {
        final PointF vTranslateForSCenter = this.vTranslateForSCenter(n, n2, n3);
        pointF.set((this.getPaddingLeft() + (this.getWidth() - this.getPaddingRight() - this.getPaddingLeft()) / 2 - vTranslateForSCenter.x) / n3, (this.getPaddingTop() + (this.getHeight() - this.getPaddingBottom() - this.getPaddingTop()) / 2 - vTranslateForSCenter.y) / n3);
        return pointF;
    }
    
    private float limitedScale(float max) {
        max = Math.max(this.minScale(), max);
        return Math.min(this.maxScale, max);
    }
    
    private float minScale() {
        final int n = this.getPaddingBottom() + this.getPaddingTop();
        final int n2 = this.getPaddingLeft() + this.getPaddingRight();
        if (this.minimumScaleType == 2) {
            return Math.max((this.getWidth() - n2) / (float)this.sWidth(), (this.getHeight() - n) / (float)this.sHeight());
        }
        if (this.minimumScaleType == 3 && this.minScale > 0.0f) {
            return this.minScale;
        }
        return Math.min((this.getWidth() - n2) / (float)this.sWidth(), (this.getHeight() - n) / (float)this.sHeight());
    }
    
    private void onImageLoaded(final Bitmap bitmap, final int sOrientation, final boolean bitmapIsCached) {
        synchronized (this) {
            this.debug("onImageLoaded", new Object[0]);
            if (this.sWidth > 0 && this.sHeight > 0 && (this.sWidth != bitmap.getWidth() || this.sHeight != bitmap.getHeight())) {
                this.reset(false);
            }
            if (this.bitmap != null && !this.bitmapIsCached) {
                this.bitmap.recycle();
            }
            if (this.bitmap != null && this.bitmapIsCached && this.onImageEventListener != null) {
                this.onImageEventListener.onPreviewReleased();
            }
            this.bitmapIsPreview = false;
            this.bitmapIsCached = bitmapIsCached;
            this.bitmap = bitmap;
            this.sWidth = bitmap.getWidth();
            this.sHeight = bitmap.getHeight();
            this.sOrientation = sOrientation;
            final boolean checkReady = this.checkReady();
            final boolean checkImageLoaded = this.checkImageLoaded();
            if (checkReady || checkImageLoaded) {
                this.invalidate();
                this.requestLayout();
            }
        }
    }
    
    private void onPreviewLoaded(final Bitmap bitmap) {
        synchronized (this) {
            this.debug("onPreviewLoaded", new Object[0]);
            if (this.bitmap == null && !this.imageLoadedSent) {
                if (this.pRegion != null) {
                    this.bitmap = Bitmap.createBitmap(bitmap, this.pRegion.left, this.pRegion.top, this.pRegion.width(), this.pRegion.height());
                }
                else {
                    this.bitmap = bitmap;
                }
                this.bitmapIsPreview = true;
                if (this.checkReady()) {
                    this.invalidate();
                    this.requestLayout();
                }
                return;
            }
            bitmap.recycle();
        }
    }
    
    private void onTileLoaded() {
        synchronized (this) {
            this.debug("onTileLoaded", new Object[0]);
            this.checkReady();
            this.checkImageLoaded();
            if (this.isBaseLayerReady() && this.bitmap != null) {
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
            this.invalidate();
        }
    }
    
    private void onTilesInited(final ImageRegionDecoder decoder, final int n, final int n2, final int sOrientation) {
        synchronized (this) {
            this.debug("onTilesInited sWidth=%d, sHeight=%d, sOrientation=%d", n, n2, this.orientation);
            if (this.sWidth > 0 && this.sHeight > 0 && (this.sWidth != n || this.sHeight != n2)) {
                this.reset(false);
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
            this.decoder = decoder;
            this.sWidth = n;
            this.sHeight = n2;
            this.sOrientation = sOrientation;
            this.checkReady();
            if (!this.checkImageLoaded() && this.maxTileWidth > 0 && this.maxTileWidth != SubsamplingScaleImageView.TILE_SIZE_AUTO && this.maxTileHeight > 0 && this.maxTileHeight != SubsamplingScaleImageView.TILE_SIZE_AUTO && this.getWidth() > 0 && this.getHeight() > 0) {
                this.initialiseBaseLayer(new Point(this.maxTileWidth, this.maxTileHeight));
            }
            this.invalidate();
            this.requestLayout();
        }
    }
    
    private boolean onTouchEventInternal(final MotionEvent motionEvent) {
        final int pointerCount = motionEvent.getPointerCount();
        switch (motionEvent.getAction()) {
            case 2: {
                boolean b7 = false;
                Label_1626: {
                    Label_1624: {
                        if (this.maxTouchCount > 0) {
                            if (pointerCount >= 2) {
                                final float distance = this.distance(motionEvent.getX(0), motionEvent.getX(1), motionEvent.getY(0), motionEvent.getY(1));
                                final float n = (motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f;
                                final float n2 = (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f;
                                if (!this.zoomEnabled || (this.distance(this.vCenterStart.x, n, this.vCenterStart.y, n2) <= 5.0f && Math.abs(distance - this.vDistStart) <= 5.0f && !this.isPanning)) {
                                    break Label_1624;
                                }
                                this.isZooming = true;
                                this.isPanning = true;
                                final double n3 = this.scale;
                                this.scale = Math.min(this.maxScale, distance / this.vDistStart * this.scaleStart);
                                if (this.scale <= this.minScale()) {
                                    this.vDistStart = distance;
                                    this.scaleStart = this.minScale();
                                    this.vCenterStart.set(n, n2);
                                    this.vTranslateStart.set(this.vTranslate);
                                }
                                else if (this.panEnabled) {
                                    final float x = this.vCenterStart.x;
                                    final float x2 = this.vTranslateStart.x;
                                    final float y = this.vCenterStart.y;
                                    final float y2 = this.vTranslateStart.y;
                                    final float n4 = this.scale / this.scaleStart;
                                    final float n5 = this.scale / this.scaleStart;
                                    this.vTranslate.x = n - (x - x2) * n4;
                                    this.vTranslate.y = n2 - (y - y2) * n5;
                                    if ((this.sHeight() * n3 < this.getHeight() && this.scale * this.sHeight() >= this.getHeight()) || (n3 * this.sWidth() < this.getWidth() && this.scale * this.sWidth() >= this.getWidth())) {
                                        this.fitToBounds(true);
                                        this.vCenterStart.set(n, n2);
                                        this.vTranslateStart.set(this.vTranslate);
                                        this.scaleStart = this.scale;
                                        this.vDistStart = distance;
                                    }
                                }
                                else if (this.sRequestedCenter != null) {
                                    this.vTranslate.x = this.getWidth() / 2 - this.scale * this.sRequestedCenter.x;
                                    this.vTranslate.y = this.getHeight() / 2 - this.scale * this.sRequestedCenter.y;
                                }
                                else {
                                    this.vTranslate.x = this.getWidth() / 2 - this.scale * (this.sWidth() / 2);
                                    this.vTranslate.y = this.getHeight() / 2 - this.scale * (this.sHeight() / 2);
                                }
                                this.fitToBounds(true);
                                this.refreshRequiredTiles(false);
                            }
                            else if (this.isQuickScaling) {
                                final float quickScaleLastDistance = Math.abs(this.quickScaleVStart.y - motionEvent.getY()) * 2.0f + this.quickScaleThreshold;
                                if (this.quickScaleLastDistance == -1.0f) {
                                    this.quickScaleLastDistance = quickScaleLastDistance;
                                }
                                final boolean b = motionEvent.getY() > this.quickScaleVLastPoint.y;
                                this.quickScaleVLastPoint.set(0.0f, motionEvent.getY());
                                final float n6 = quickScaleLastDistance / this.quickScaleLastDistance;
                                final float n7 = 1.0f;
                                final float n8 = Math.abs(1.0f - n6) * 0.5f;
                                float quickScaleLastDistance2 = 0.0f;
                                Label_1197: {
                                    if (n8 <= 0.03f) {
                                        quickScaleLastDistance2 = quickScaleLastDistance;
                                        if (!this.quickScaleMoved) {
                                            break Label_1197;
                                        }
                                    }
                                    this.quickScaleMoved = true;
                                    float n9 = n7;
                                    if (this.quickScaleLastDistance > 0.0f) {
                                        if (b) {
                                            n9 = n8 + 1.0f;
                                        }
                                        else {
                                            n9 = 1.0f - n8;
                                        }
                                    }
                                    final double n10 = this.scale;
                                    this.scale = Math.max(this.minScale(), Math.min(this.maxScale, this.scale * n9));
                                    if (this.panEnabled) {
                                        final float x3 = this.vCenterStart.x;
                                        final float x4 = this.vTranslateStart.x;
                                        final float y3 = this.vCenterStart.y;
                                        final float y4 = this.vTranslateStart.y;
                                        final float n11 = this.scale / this.scaleStart;
                                        final float n12 = this.scale / this.scaleStart;
                                        this.vTranslate.x = this.vCenterStart.x - (x3 - x4) * n11;
                                        this.vTranslate.y = this.vCenterStart.y - (y3 - y4) * n12;
                                        if (this.sHeight() * n10 >= this.getHeight() || this.scale * this.sHeight() < this.getHeight()) {
                                            quickScaleLastDistance2 = quickScaleLastDistance;
                                            if (n10 * this.sWidth() >= this.getWidth()) {
                                                break Label_1197;
                                            }
                                            quickScaleLastDistance2 = quickScaleLastDistance;
                                            if (this.scale * this.sWidth() < this.getWidth()) {
                                                break Label_1197;
                                            }
                                        }
                                        this.fitToBounds(true);
                                        this.vCenterStart.set(this.sourceToViewCoord(this.quickScaleSCenter));
                                        this.vTranslateStart.set(this.vTranslate);
                                        this.scaleStart = this.scale;
                                        quickScaleLastDistance2 = 0.0f;
                                    }
                                    else if (this.sRequestedCenter != null) {
                                        this.vTranslate.x = this.getWidth() / 2 - this.scale * this.sRequestedCenter.x;
                                        this.vTranslate.y = this.getHeight() / 2 - this.scale * this.sRequestedCenter.y;
                                        quickScaleLastDistance2 = quickScaleLastDistance;
                                    }
                                    else {
                                        this.vTranslate.x = this.getWidth() / 2 - this.scale * (this.sWidth() / 2);
                                        this.vTranslate.y = this.getHeight() / 2 - this.scale * (this.sHeight() / 2);
                                        quickScaleLastDistance2 = quickScaleLastDistance;
                                    }
                                }
                                this.quickScaleLastDistance = quickScaleLastDistance2;
                                this.fitToBounds(true);
                                this.refreshRequiredTiles(false);
                            }
                            else {
                                if (this.isZooming) {
                                    break Label_1624;
                                }
                                final float abs = Math.abs(motionEvent.getX() - this.vCenterStart.x);
                                final float abs2 = Math.abs(motionEvent.getY() - this.vCenterStart.y);
                                final float n13 = this.density * 5.0f;
                                final float n14 = fcmpl(abs, n13);
                                if (n14 <= 0 && abs2 <= n13 && !this.isPanning) {
                                    break Label_1624;
                                }
                                this.vTranslate.x = this.vTranslateStart.x + (motionEvent.getX() - this.vCenterStart.x);
                                this.vTranslate.y = this.vTranslateStart.y + (motionEvent.getY() - this.vCenterStart.y);
                                final float x5 = this.vTranslate.x;
                                final float y5 = this.vTranslate.y;
                                this.fitToBounds(true);
                                final boolean b2 = x5 != this.vTranslate.x;
                                final boolean b3 = y5 != this.vTranslate.y;
                                final boolean b4 = b2 && abs > abs2 && !this.isPanning;
                                final boolean b5 = b3 && abs2 > abs && !this.isPanning;
                                final boolean b6 = y5 == this.vTranslate.y && abs2 > 3.0f * n13;
                                if (!b4 && !b5 && (!b2 || !b3 || b6 || this.isPanning)) {
                                    this.isPanning = true;
                                }
                                else if (n14 > 0 || abs2 > n13) {
                                    this.maxTouchCount = 0;
                                    this.handler.removeMessages(1);
                                    this.requestDisallowInterceptTouchEvent(false);
                                }
                                if (!this.panEnabled) {
                                    this.vTranslate.x = this.vTranslateStart.x;
                                    this.vTranslate.y = this.vTranslateStart.y;
                                    this.requestDisallowInterceptTouchEvent(false);
                                }
                                this.refreshRequiredTiles(false);
                            }
                            b7 = true;
                            break Label_1626;
                        }
                    }
                    b7 = false;
                }
                if (b7) {
                    this.handler.removeMessages(1);
                    this.invalidate();
                    return true;
                }
                break;
            }
            case 1:
            case 6:
            case 262: {
                this.handler.removeMessages(1);
                if (this.isQuickScaling) {
                    this.isQuickScaling = false;
                    if (!this.quickScaleMoved) {
                        this.doubleTapZoom(this.quickScaleSCenter, this.vCenterStart);
                    }
                }
                if (this.maxTouchCount > 0 && (this.isZooming || this.isPanning)) {
                    if (this.isZooming && pointerCount == 2) {
                        this.isPanning = true;
                        this.vTranslateStart.set(this.vTranslate.x, this.vTranslate.y);
                        if (motionEvent.getActionIndex() == 1) {
                            this.vCenterStart.set(motionEvent.getX(0), motionEvent.getY(0));
                        }
                        else {
                            this.vCenterStart.set(motionEvent.getX(1), motionEvent.getY(1));
                        }
                    }
                    if (pointerCount < 3) {
                        this.isZooming = false;
                    }
                    if (pointerCount < 2) {
                        this.isPanning = false;
                        this.maxTouchCount = 0;
                    }
                    this.refreshRequiredTiles(true);
                    return true;
                }
                if (pointerCount == 1) {
                    this.isZooming = false;
                    this.isPanning = false;
                    this.maxTouchCount = 0;
                }
                return true;
            }
            case 0:
            case 5:
            case 261: {
                this.anim = null;
                this.requestDisallowInterceptTouchEvent(true);
                this.maxTouchCount = Math.max(this.maxTouchCount, pointerCount);
                if (pointerCount >= 2) {
                    if (this.zoomEnabled) {
                        final float distance2 = this.distance(motionEvent.getX(0), motionEvent.getX(1), motionEvent.getY(0), motionEvent.getY(1));
                        this.scaleStart = this.scale;
                        this.vDistStart = distance2;
                        this.vTranslateStart.set(this.vTranslate.x, this.vTranslate.y);
                        this.vCenterStart.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
                    }
                    else {
                        this.maxTouchCount = 0;
                    }
                    this.handler.removeMessages(1);
                }
                else if (!this.isQuickScaling) {
                    this.vTranslateStart.set(this.vTranslate.x, this.vTranslate.y);
                    this.vCenterStart.set(motionEvent.getX(), motionEvent.getY());
                    this.handler.sendEmptyMessageDelayed(1, 600L);
                }
                return true;
            }
        }
        return false;
    }
    
    private void preDraw() {
        if (this.getWidth() != 0 && this.getHeight() != 0 && this.sWidth > 0 && this.sHeight > 0) {
            if (this.sPendingCenter != null && this.pendingScale != null) {
                this.scale = this.pendingScale;
                if (this.vTranslate == null) {
                    this.vTranslate = new PointF();
                }
                this.vTranslate.x = this.getWidth() / 2 - this.scale * this.sPendingCenter.x;
                this.vTranslate.y = this.getHeight() / 2 - this.scale * this.sPendingCenter.y;
                this.sPendingCenter = null;
                this.pendingScale = null;
                this.fitToBounds(true);
                this.refreshRequiredTiles(true);
            }
            this.fitToBounds(false);
        }
    }
    
    private void refreshRequiredTiles(final boolean b) {
        if (this.decoder != null && this.tileMap != null) {
            final int min = Math.min(this.fullImageSampleSize, this.calculateInSampleSize(this.scale));
            final Iterator<Map.Entry<Integer, List<Tile>>> iterator = this.tileMap.entrySet().iterator();
            while (iterator.hasNext()) {
                for (final Tile tile : iterator.next().getValue()) {
                    if (tile.sampleSize < min || (tile.sampleSize > min && tile.sampleSize != this.fullImageSampleSize)) {
                        tile.visible = false;
                        if (tile.bitmap != null) {
                            tile.bitmap.recycle();
                            tile.bitmap = null;
                        }
                    }
                    if (tile.sampleSize == min) {
                        if (this.tileVisible(tile)) {
                            tile.visible = true;
                            if (tile.loading || tile.bitmap != null || !b) {
                                continue;
                            }
                            this.execute(new TileLoadTask(this, this.decoder, tile));
                        }
                        else {
                            if (tile.sampleSize == this.fullImageSampleSize) {
                                continue;
                            }
                            tile.visible = false;
                            if (tile.bitmap == null) {
                                continue;
                            }
                            tile.bitmap.recycle();
                            tile.bitmap = null;
                        }
                    }
                    else {
                        if (tile.sampleSize != this.fullImageSampleSize) {
                            continue;
                        }
                        tile.visible = true;
                    }
                }
            }
        }
    }
    
    private void requestDisallowInterceptTouchEvent(final boolean b) {
        final ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(b);
        }
    }
    
    private void reset(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append("reset newImage=");
        sb.append(b);
        this.debug(sb.toString(), new Object[0]);
        this.scale = 0.0f;
        this.scaleStart = 0.0f;
        this.vTranslate = null;
        this.vTranslateStart = null;
        this.vTranslateBefore = null;
        this.pendingScale = 0.0f;
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
        if (b) {
            this.uri = null;
            if (this.decoder != null) {
                synchronized (this.decoderLock) {
                    this.decoder.recycle();
                    this.decoder = null;
                }
            }
            if (this.bitmap != null && !this.bitmapIsCached) {
                this.bitmap.recycle();
            }
            if (this.bitmap != null && this.bitmapIsCached && this.onImageEventListener != null) {
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
            final Iterator<Map.Entry<Integer, List<Tile>>> iterator = this.tileMap.entrySet().iterator();
            while (iterator.hasNext()) {
                for (final Tile tile : iterator.next().getValue()) {
                    tile.visible = false;
                    if (tile.bitmap != null) {
                        tile.bitmap.recycle();
                        tile.bitmap = null;
                    }
                }
            }
            this.tileMap = null;
        }
        this.setGestureDetector(this.getContext());
    }
    
    private void restoreState(final ImageViewState imageViewState) {
        if (imageViewState != null && imageViewState.getCenter() != null && SubsamplingScaleImageView.VALID_ORIENTATIONS.contains(imageViewState.getOrientation())) {
            this.orientation = imageViewState.getOrientation();
            this.pendingScale = imageViewState.getScale();
            this.sPendingCenter = imageViewState.getCenter();
            this.invalidate();
        }
    }
    
    private int sHeight() {
        final int requiredRotation = this.getRequiredRotation();
        if (requiredRotation != 90 && requiredRotation != 270) {
            return this.sHeight;
        }
        return this.sWidth;
    }
    
    private int sWidth() {
        final int requiredRotation = this.getRequiredRotation();
        if (requiredRotation != 90 && requiredRotation != 270) {
            return this.sWidth;
        }
        return this.sHeight;
    }
    
    private void sendStateChanged(final float n, final PointF pointF, final int n2) {
        if (this.onStateChangedListener != null) {
            if (this.scale != n) {
                this.onStateChangedListener.onScaleChanged(this.scale, n2);
            }
            if (!this.vTranslate.equals((Object)pointF)) {
                this.onStateChangedListener.onCenterChanged(this.getCenter(), n2);
            }
        }
    }
    
    private void setGestureDetector(final Context context) {
        this.detector = new GestureDetector(context, (GestureDetector$OnGestureListener)new GestureDetector$SimpleOnGestureListener() {
            public boolean onDoubleTap(final MotionEvent motionEvent) {
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
            
            public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, float a, float a2) {
                if (SubsamplingScaleImageView.this.panEnabled && SubsamplingScaleImageView.this.readySent && SubsamplingScaleImageView.this.vTranslate != null && motionEvent != null && motionEvent2 != null && (Math.abs(motionEvent.getX() - motionEvent2.getX()) > 50.0f || Math.abs(motionEvent.getY() - motionEvent2.getY()) > 50.0f) && (Math.abs(a) > 500.0f || Math.abs(a2) > 500.0f) && !SubsamplingScaleImageView.this.isZooming) {
                    final PointF pointF = new PointF(SubsamplingScaleImageView.this.vTranslate.x + a * 0.25f, SubsamplingScaleImageView.this.vTranslate.y + a2 * 0.25f);
                    a2 = (SubsamplingScaleImageView.this.getWidth() / 2 - pointF.x) / SubsamplingScaleImageView.this.scale;
                    a = (SubsamplingScaleImageView.this.getHeight() / 2 - pointF.y) / SubsamplingScaleImageView.this.scale;
                    new AnimationBuilder(new PointF(a2, a)).withEasing(1).withPanLimited(false).withOrigin(3).start();
                    return true;
                }
                return super.onFling(motionEvent, motionEvent2, a, a2);
            }
            
            public boolean onSingleTapConfirmed(final MotionEvent motionEvent) {
                SubsamplingScaleImageView.this.performClick();
                return true;
            }
        });
    }
    
    private void setMatrixArray(final float[] array, final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8) {
        array[0] = n;
        array[1] = n2;
        array[2] = n3;
        array[3] = n4;
        array[4] = n5;
        array[5] = n6;
        array[6] = n7;
        array[7] = n8;
    }
    
    private Rect sourceToViewRect(final Rect rect, final Rect rect2) {
        rect2.set((int)this.sourceToViewX((float)rect.left), (int)this.sourceToViewY((float)rect.top), (int)this.sourceToViewX((float)rect.right), (int)this.sourceToViewY((float)rect.bottom));
        return rect2;
    }
    
    private float sourceToViewX(final float n) {
        if (this.vTranslate == null) {
            return Float.NaN;
        }
        return n * this.scale + this.vTranslate.x;
    }
    
    private float sourceToViewY(final float n) {
        if (this.vTranslate == null) {
            return Float.NaN;
        }
        return n * this.scale + this.vTranslate.y;
    }
    
    private boolean tileVisible(final Tile tile) {
        final float viewToSourceX = this.viewToSourceX(0.0f);
        final float viewToSourceX2 = this.viewToSourceX((float)this.getWidth());
        final float viewToSourceY = this.viewToSourceY(0.0f);
        final float viewToSourceY2 = this.viewToSourceY((float)this.getHeight());
        return viewToSourceX <= tile.sRect.right && tile.sRect.left <= viewToSourceX2 && viewToSourceY <= tile.sRect.bottom && tile.sRect.top <= viewToSourceY2;
    }
    
    private PointF vTranslateForSCenter(final float n, final float n2, final float n3) {
        final int paddingLeft = this.getPaddingLeft();
        final int n4 = (this.getWidth() - this.getPaddingRight() - this.getPaddingLeft()) / 2;
        final int paddingTop = this.getPaddingTop();
        final int n5 = (this.getHeight() - this.getPaddingBottom() - this.getPaddingTop()) / 2;
        if (this.satTemp == null) {
            this.satTemp = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f));
        }
        this.satTemp.scale = n3;
        this.satTemp.vTranslate.set(paddingLeft + n4 - n * n3, paddingTop + n5 - n2 * n3);
        this.fitToBounds(true, this.satTemp);
        return this.satTemp.vTranslate;
    }
    
    private float viewToSourceX(final float n) {
        if (this.vTranslate == null) {
            return Float.NaN;
        }
        return (n - this.vTranslate.x) / this.scale;
    }
    
    private float viewToSourceY(final float n) {
        if (this.vTranslate == null) {
            return Float.NaN;
        }
        return (n - this.vTranslate.y) / this.scale;
    }
    
    public final int getAppliedOrientation() {
        return this.getRequiredRotation();
    }
    
    public final PointF getCenter() {
        return this.viewToSourceCoord((float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
    }
    
    public float getMaxScale() {
        return this.maxScale;
    }
    
    public final float getMinScale() {
        return this.minScale();
    }
    
    public final int getOrientation() {
        return this.orientation;
    }
    
    public final int getSHeight() {
        return this.sHeight;
    }
    
    public final int getSWidth() {
        return this.sWidth;
    }
    
    public final float getScale() {
        return this.scale;
    }
    
    public final ImageViewState getState() {
        if (this.vTranslate != null && this.sWidth > 0 && this.sHeight > 0) {
            return new ImageViewState(this.getScale(), this.getCenter(), this.getOrientation());
        }
        return null;
    }
    
    public final boolean isImageLoaded() {
        return this.imageLoadedSent;
    }
    
    public final boolean isReady() {
        return this.readySent;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.createPaints();
        if (this.sWidth == 0 || this.sHeight == 0 || this.getWidth() == 0 || this.getHeight() == 0) {
            return;
        }
        if (this.tileMap == null && this.decoder != null) {
            this.initialiseBaseLayer(this.getMaxBitmapDimensions(canvas));
        }
        if (!this.checkReady()) {
            return;
        }
        this.preDraw();
        if (this.anim != null) {
            final float scale = this.scale;
            if (this.vTranslateBefore == null) {
                this.vTranslateBefore = new PointF(0.0f, 0.0f);
            }
            this.vTranslateBefore.set(this.vTranslate);
            final long a = System.currentTimeMillis() - this.anim.time;
            final boolean b = a > this.anim.duration;
            final long min = Math.min(a, this.anim.duration);
            this.scale = this.ease(this.anim.easing, min, this.anim.scaleStart, this.anim.scaleEnd - this.anim.scaleStart, this.anim.duration);
            final float ease = this.ease(this.anim.easing, min, this.anim.vFocusStart.x, this.anim.vFocusEnd.x - this.anim.vFocusStart.x, this.anim.duration);
            final float ease2 = this.ease(this.anim.easing, min, this.anim.vFocusStart.y, this.anim.vFocusEnd.y - this.anim.vFocusStart.y, this.anim.duration);
            final PointF vTranslate = this.vTranslate;
            vTranslate.x -= this.sourceToViewX(this.anim.sCenterEnd.x) - ease;
            final PointF vTranslate2 = this.vTranslate;
            vTranslate2.y -= this.sourceToViewY(this.anim.sCenterEnd.y) - ease2;
            this.fitToBounds(b || this.anim.scaleStart == this.anim.scaleEnd);
            this.sendStateChanged(scale, this.vTranslateBefore, this.anim.origin);
            this.refreshRequiredTiles(b);
            if (b) {
                if (this.anim.listener != null) {
                    try {
                        this.anim.listener.onComplete();
                    }
                    catch (Exception ex) {
                        Log.w(SubsamplingScaleImageView.TAG, "Error thrown by animation listener", (Throwable)ex);
                    }
                }
                this.anim = null;
            }
            this.invalidate();
        }
        if (this.tileMap != null && this.isBaseLayerReady()) {
            final int min2 = Math.min(this.fullImageSampleSize, this.calculateInSampleSize(this.scale));
            final Iterator<Map.Entry<Integer, List<Tile>>> iterator = this.tileMap.entrySet().iterator();
            int n = 0;
            while (iterator.hasNext()) {
                final Map.Entry<Integer, List<Tile>> entry = iterator.next();
                if (entry.getKey() == min2) {
                    final Iterator<Tile> iterator2 = entry.getValue().iterator();
                    int n2 = n;
                    while (true) {
                        n = n2;
                        if (!iterator2.hasNext()) {
                            break;
                        }
                        final Tile tile = iterator2.next();
                        if (!tile.visible || (!tile.loading && tile.bitmap != null)) {
                            continue;
                        }
                        n2 = 1;
                    }
                }
            }
            for (final Map.Entry<Integer, List<Tile>> entry2 : this.tileMap.entrySet()) {
                if (entry2.getKey() != min2 && n == 0) {
                    continue;
                }
                for (final Tile tile2 : entry2.getValue()) {
                    this.sourceToViewRect(tile2.sRect, tile2.vRect);
                    if (!tile2.loading && tile2.bitmap != null) {
                        if (this.tileBgPaint != null) {
                            canvas.drawRect(tile2.vRect, this.tileBgPaint);
                        }
                        if (this.matrix == null) {
                            this.matrix = new Matrix();
                        }
                        this.matrix.reset();
                        this.setMatrixArray(this.srcArray, 0.0f, 0.0f, (float)tile2.bitmap.getWidth(), 0.0f, (float)tile2.bitmap.getWidth(), (float)tile2.bitmap.getHeight(), 0.0f, (float)tile2.bitmap.getHeight());
                        if (this.getRequiredRotation() == 0) {
                            this.setMatrixArray(this.dstArray, (float)tile2.vRect.left, (float)tile2.vRect.top, (float)tile2.vRect.right, (float)tile2.vRect.top, (float)tile2.vRect.right, (float)tile2.vRect.bottom, (float)tile2.vRect.left, (float)tile2.vRect.bottom);
                        }
                        else if (this.getRequiredRotation() == 90) {
                            this.setMatrixArray(this.dstArray, (float)tile2.vRect.right, (float)tile2.vRect.top, (float)tile2.vRect.right, (float)tile2.vRect.bottom, (float)tile2.vRect.left, (float)tile2.vRect.bottom, (float)tile2.vRect.left, (float)tile2.vRect.top);
                        }
                        else if (this.getRequiredRotation() == 180) {
                            this.setMatrixArray(this.dstArray, (float)tile2.vRect.right, (float)tile2.vRect.bottom, (float)tile2.vRect.left, (float)tile2.vRect.bottom, (float)tile2.vRect.left, (float)tile2.vRect.top, (float)tile2.vRect.right, (float)tile2.vRect.top);
                        }
                        else if (this.getRequiredRotation() == 270) {
                            this.setMatrixArray(this.dstArray, (float)tile2.vRect.left, (float)tile2.vRect.bottom, (float)tile2.vRect.left, (float)tile2.vRect.top, (float)tile2.vRect.right, (float)tile2.vRect.top, (float)tile2.vRect.right, (float)tile2.vRect.bottom);
                        }
                        this.matrix.setPolyToPoly(this.srcArray, 0, this.dstArray, 0, 4);
                        canvas.drawBitmap(tile2.bitmap, this.matrix, this.bitmapPaint);
                        if (this.debug) {
                            canvas.drawRect(tile2.vRect, this.debugPaint);
                        }
                    }
                    else if (tile2.loading && this.debug) {
                        canvas.drawText("LOADING", (float)(tile2.vRect.left + 5), (float)(tile2.vRect.top + 35), this.debugPaint);
                    }
                    if (tile2.visible && this.debug) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("ISS ");
                        sb.append(tile2.sampleSize);
                        sb.append(" RECT ");
                        sb.append(tile2.sRect.top);
                        sb.append(",");
                        sb.append(tile2.sRect.left);
                        sb.append(",");
                        sb.append(tile2.sRect.bottom);
                        sb.append(",");
                        sb.append(tile2.sRect.right);
                        canvas.drawText(sb.toString(), (float)(tile2.vRect.left + 5), (float)(tile2.vRect.top + 15), this.debugPaint);
                    }
                }
            }
        }
        else if (this.bitmap != null) {
            float scale2 = this.scale;
            float scale3 = this.scale;
            if (this.bitmapIsPreview) {
                scale2 = this.scale * (this.sWidth / (float)this.bitmap.getWidth());
                scale3 = this.scale * (this.sHeight / (float)this.bitmap.getHeight());
            }
            if (this.matrix == null) {
                this.matrix = new Matrix();
            }
            this.matrix.reset();
            this.matrix.postScale(scale2, scale3);
            this.matrix.postRotate((float)this.getRequiredRotation());
            this.matrix.postTranslate(this.vTranslate.x, this.vTranslate.y);
            if (this.getRequiredRotation() == 180) {
                this.matrix.postTranslate(this.scale * this.sWidth, this.scale * this.sHeight);
            }
            else if (this.getRequiredRotation() == 90) {
                this.matrix.postTranslate(this.scale * this.sHeight, 0.0f);
            }
            else if (this.getRequiredRotation() == 270) {
                this.matrix.postTranslate(0.0f, this.scale * this.sWidth);
            }
            if (this.tileBgPaint != null) {
                if (this.sRect == null) {
                    this.sRect = new RectF();
                }
                final RectF sRect = this.sRect;
                int n3;
                if (this.bitmapIsPreview) {
                    n3 = this.bitmap.getWidth();
                }
                else {
                    n3 = this.sWidth;
                }
                final float n4 = (float)n3;
                int n5;
                if (this.bitmapIsPreview) {
                    n5 = this.bitmap.getHeight();
                }
                else {
                    n5 = this.sHeight;
                }
                sRect.set(0.0f, 0.0f, n4, (float)n5);
                this.matrix.mapRect(this.sRect);
                canvas.drawRect(this.sRect, this.tileBgPaint);
            }
            canvas.drawBitmap(this.bitmap, this.matrix, this.bitmapPaint);
        }
        if (this.debug) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Scale: ");
            sb2.append(String.format(Locale.ENGLISH, "%.2f", this.scale));
            canvas.drawText(sb2.toString(), 5.0f, 15.0f, this.debugPaint);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Translate: ");
            sb3.append(String.format(Locale.ENGLISH, "%.2f", this.vTranslate.x));
            sb3.append(":");
            sb3.append(String.format(Locale.ENGLISH, "%.2f", this.vTranslate.y));
            canvas.drawText(sb3.toString(), 5.0f, 35.0f, this.debugPaint);
            final PointF center = this.getCenter();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Source center: ");
            sb4.append(String.format(Locale.ENGLISH, "%.2f", center.x));
            sb4.append(":");
            sb4.append(String.format(Locale.ENGLISH, "%.2f", center.y));
            canvas.drawText(sb4.toString(), 5.0f, 55.0f, this.debugPaint);
            this.debugPaint.setStrokeWidth(2.0f);
            if (this.anim != null) {
                final PointF sourceToViewCoord = this.sourceToViewCoord(this.anim.sCenterStart);
                final PointF sourceToViewCoord2 = this.sourceToViewCoord(this.anim.sCenterEndRequested);
                final PointF sourceToViewCoord3 = this.sourceToViewCoord(this.anim.sCenterEnd);
                canvas.drawCircle(sourceToViewCoord.x, sourceToViewCoord.y, 10.0f, this.debugPaint);
                this.debugPaint.setColor(-65536);
                canvas.drawCircle(sourceToViewCoord2.x, sourceToViewCoord2.y, 20.0f, this.debugPaint);
                this.debugPaint.setColor(-16776961);
                canvas.drawCircle(sourceToViewCoord3.x, sourceToViewCoord3.y, 25.0f, this.debugPaint);
                this.debugPaint.setColor(-16711681);
                canvas.drawCircle((float)(this.getWidth() / 2), (float)(this.getHeight() / 2), 30.0f, this.debugPaint);
            }
            if (this.vCenterStart != null) {
                this.debugPaint.setColor(-65536);
                canvas.drawCircle(this.vCenterStart.x, this.vCenterStart.y, 20.0f, this.debugPaint);
            }
            if (this.quickScaleSCenter != null) {
                this.debugPaint.setColor(-16776961);
                canvas.drawCircle(this.sourceToViewX(this.quickScaleSCenter.x), this.sourceToViewY(this.quickScaleSCenter.y), 35.0f, this.debugPaint);
            }
            if (this.quickScaleVStart != null) {
                this.debugPaint.setColor(-16711681);
                canvas.drawCircle(this.quickScaleVStart.x, this.quickScaleVStart.y, 30.0f, this.debugPaint);
            }
            this.debugPaint.setColor(-65281);
            this.debugPaint.setStrokeWidth(1.0f);
        }
    }
    
    protected void onImageLoaded() {
    }
    
    protected void onMeasure(int sWidth, int sHeight) {
        final int mode = View$MeasureSpec.getMode(sWidth);
        final int mode2 = View$MeasureSpec.getMode(sHeight);
        final int size = View$MeasureSpec.getSize(sWidth);
        final int size2 = View$MeasureSpec.getSize(sHeight);
        boolean b = false;
        final boolean b2 = mode != 1073741824;
        if (mode2 != 1073741824) {
            b = true;
        }
        sWidth = size;
        sHeight = size2;
        if (this.sWidth > 0) {
            sWidth = size;
            sHeight = size2;
            if (this.sHeight > 0) {
                if (b2 && b) {
                    sWidth = this.sWidth();
                    sHeight = this.sHeight();
                }
                else if (b) {
                    sHeight = (int)(this.sHeight() / (double)this.sWidth() * size);
                    sWidth = size;
                }
                else {
                    sWidth = size;
                    sHeight = size2;
                    if (b2) {
                        sWidth = (int)(this.sWidth() / (double)this.sHeight() * size2);
                        sHeight = size2;
                    }
                }
            }
        }
        this.setMeasuredDimension(Math.max(sWidth, this.getSuggestedMinimumWidth()), Math.max(sHeight, this.getSuggestedMinimumHeight()));
    }
    
    protected void onReady() {
    }
    
    protected void onSizeChanged(final int i, final int j, final int k, final int l) {
        this.debug("onSizeChanged %dx%d -> %dx%d", k, l, i, j);
        final PointF center = this.getCenter();
        if (this.readySent && center != null) {
            this.anim = null;
            this.pendingScale = this.scale;
            this.sPendingCenter = center;
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final Anim anim = this.anim;
        final boolean b = true;
        if (anim != null && !this.anim.interruptible) {
            this.requestDisallowInterceptTouchEvent(true);
            return true;
        }
        if (this.anim != null && this.anim.listener != null) {
            try {
                this.anim.listener.onInterruptedByUser();
            }
            catch (Exception ex) {
                Log.w(SubsamplingScaleImageView.TAG, "Error thrown by animation listener", (Throwable)ex);
            }
        }
        this.anim = null;
        if (this.vTranslate == null) {
            return true;
        }
        if (!this.isQuickScaling && (this.detector == null || this.detector.onTouchEvent(motionEvent))) {
            this.isZooming = false;
            this.isPanning = false;
            this.maxTouchCount = 0;
            return true;
        }
        if (this.vTranslateStart == null) {
            this.vTranslateStart = new PointF(0.0f, 0.0f);
        }
        if (this.vTranslateBefore == null) {
            this.vTranslateBefore = new PointF(0.0f, 0.0f);
        }
        if (this.vCenterStart == null) {
            this.vCenterStart = new PointF(0.0f, 0.0f);
        }
        final float scale = this.scale;
        this.vTranslateBefore.set(this.vTranslate);
        final boolean onTouchEventInternal = this.onTouchEventInternal(motionEvent);
        this.sendStateChanged(scale, this.vTranslateBefore, 2);
        boolean b2 = b;
        if (!onTouchEventInternal) {
            b2 = (super.onTouchEvent(motionEvent) && b);
        }
        return b2;
    }
    
    public final void setBitmapDecoderClass(final Class<? extends ImageDecoder> clazz) {
        if (clazz != null) {
            this.bitmapDecoderFactory = new CompatDecoderFactory<ImageDecoder>(clazz);
            return;
        }
        throw new IllegalArgumentException("Decoder class cannot be set to null");
    }
    
    public final void setBitmapDecoderFactory(final DecoderFactory<? extends ImageDecoder> bitmapDecoderFactory) {
        if (bitmapDecoderFactory != null) {
            this.bitmapDecoderFactory = bitmapDecoderFactory;
            return;
        }
        throw new IllegalArgumentException("Decoder factory cannot be set to null");
    }
    
    public final void setDebug(final boolean debug) {
        this.debug = debug;
    }
    
    public final void setDoubleTapZoomDpi(final int n) {
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        this.setDoubleTapZoomScale((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f / n);
    }
    
    public final void setDoubleTapZoomDuration(final int b) {
        this.doubleTapZoomDuration = Math.max(0, b);
    }
    
    public final void setDoubleTapZoomScale(final float doubleTapZoomScale) {
        this.doubleTapZoomScale = doubleTapZoomScale;
    }
    
    public final void setDoubleTapZoomStyle(final int i) {
        if (SubsamplingScaleImageView.VALID_ZOOM_STYLES.contains(i)) {
            this.doubleTapZoomStyle = i;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid zoom style: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public final void setImage(final ImageSource imageSource) {
        this.setImage(imageSource, null, null);
    }
    
    public final void setImage(final ImageSource imageSource, final ImageSource imageSource2, final ImageViewState imageViewState) {
        if (imageSource != null) {
            this.reset(true);
            if (imageViewState != null) {
                this.restoreState(imageViewState);
            }
            if (imageSource2 != null) {
                if (imageSource.getBitmap() != null) {
                    throw new IllegalArgumentException("Preview image cannot be used when a bitmap is provided for the main image");
                }
                if (imageSource.getSWidth() <= 0 || imageSource.getSHeight() <= 0) {
                    throw new IllegalArgumentException("Preview image cannot be used unless dimensions are provided for the main image");
                }
                this.sWidth = imageSource.getSWidth();
                this.sHeight = imageSource.getSHeight();
                this.pRegion = imageSource2.getSRegion();
                if (imageSource2.getBitmap() != null) {
                    this.bitmapIsCached = imageSource2.isCached();
                    this.onPreviewLoaded(imageSource2.getBitmap());
                }
                else {
                    final Uri uri = imageSource2.getUri();
                    Uri parse;
                    if ((parse = uri) == null) {
                        parse = uri;
                        if (imageSource2.getResource() != null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("android.resource://");
                            sb.append(this.getContext().getPackageName());
                            sb.append("/");
                            sb.append(imageSource2.getResource());
                            parse = Uri.parse(sb.toString());
                        }
                    }
                    this.execute(new BitmapLoadTask(this, this.getContext(), this.bitmapDecoderFactory, parse, true));
                }
            }
            if (imageSource.getBitmap() != null && imageSource.getSRegion() != null) {
                this.onImageLoaded(Bitmap.createBitmap(imageSource.getBitmap(), imageSource.getSRegion().left, imageSource.getSRegion().top, imageSource.getSRegion().width(), imageSource.getSRegion().height()), 0, false);
            }
            else if (imageSource.getBitmap() != null) {
                this.onImageLoaded(imageSource.getBitmap(), 0, imageSource.isCached());
            }
            else {
                this.sRegion = imageSource.getSRegion();
                this.uri = imageSource.getUri();
                if (this.uri == null && imageSource.getResource() != null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("android.resource://");
                    sb2.append(this.getContext().getPackageName());
                    sb2.append("/");
                    sb2.append(imageSource.getResource());
                    this.uri = Uri.parse(sb2.toString());
                }
                if (!imageSource.getTile() && this.sRegion == null) {
                    this.execute(new BitmapLoadTask(this, this.getContext(), this.bitmapDecoderFactory, this.uri, false));
                }
                else {
                    this.execute(new TilesInitTask(this, this.getContext(), this.regionDecoderFactory, this.uri));
                }
            }
            return;
        }
        throw new NullPointerException("imageSource must not be null");
    }
    
    public final void setImage(final ImageSource imageSource, final ImageViewState imageViewState) {
        this.setImage(imageSource, null, imageViewState);
    }
    
    public final void setMaxScale(final float maxScale) {
        this.maxScale = maxScale;
    }
    
    public void setMaxTileSize(final int n) {
        this.maxTileWidth = n;
        this.maxTileHeight = n;
    }
    
    public final void setMaximumDpi(final int n) {
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        this.setMinScale((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f / n);
    }
    
    public final void setMinScale(final float minScale) {
        this.minScale = minScale;
    }
    
    public final void setMinimumDpi(final int n) {
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        this.setMaxScale((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f / n);
    }
    
    public final void setMinimumScaleType(final int i) {
        if (SubsamplingScaleImageView.VALID_SCALE_TYPES.contains(i)) {
            this.minimumScaleType = i;
            if (this.isReady()) {
                this.fitToBounds(true);
                this.invalidate();
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid scale type: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void setMinimumTileDpi(final int n) {
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        this.minimumTileDpi = (int)Math.min((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f, (float)n);
        if (this.isReady()) {
            this.reset(false);
            this.invalidate();
        }
    }
    
    public void setOnImageEventListener(final OnImageEventListener onImageEventListener) {
        this.onImageEventListener = onImageEventListener;
    }
    
    public void setOnLongClickListener(final View$OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
    
    public void setOnStateChangedListener(final OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }
    
    public final void setOrientation(final int i) {
        if (SubsamplingScaleImageView.VALID_ORIENTATIONS.contains(i)) {
            this.orientation = i;
            this.reset(false);
            this.invalidate();
            this.requestLayout();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid orientation: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public final void setPanEnabled(final boolean panEnabled) {
        this.panEnabled = panEnabled;
        if (!panEnabled && this.vTranslate != null) {
            this.vTranslate.x = this.getWidth() / 2 - this.scale * (this.sWidth() / 2);
            this.vTranslate.y = this.getHeight() / 2 - this.scale * (this.sHeight() / 2);
            if (this.isReady()) {
                this.refreshRequiredTiles(true);
                this.invalidate();
            }
        }
    }
    
    public final void setPanLimit(final int i) {
        if (SubsamplingScaleImageView.VALID_PAN_LIMITS.contains(i)) {
            this.panLimit = i;
            if (this.isReady()) {
                this.fitToBounds(true);
                this.invalidate();
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid pan limit: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void setParallelLoadingEnabled(final boolean parallelLoadingEnabled) {
        this.parallelLoadingEnabled = parallelLoadingEnabled;
    }
    
    public final void setQuickScaleEnabled(final boolean quickScaleEnabled) {
        this.quickScaleEnabled = quickScaleEnabled;
    }
    
    public final void setRegionDecoderClass(final Class<? extends ImageRegionDecoder> clazz) {
        if (clazz != null) {
            this.regionDecoderFactory = new CompatDecoderFactory<ImageRegionDecoder>(clazz);
            return;
        }
        throw new IllegalArgumentException("Decoder class cannot be set to null");
    }
    
    public final void setRegionDecoderFactory(final DecoderFactory<? extends ImageRegionDecoder> regionDecoderFactory) {
        if (regionDecoderFactory != null) {
            this.regionDecoderFactory = regionDecoderFactory;
            return;
        }
        throw new IllegalArgumentException("Decoder factory cannot be set to null");
    }
    
    public final void setScaleAndCenter(final float f, final PointF pointF) {
        this.anim = null;
        this.pendingScale = f;
        this.sPendingCenter = pointF;
        this.sRequestedCenter = pointF;
        this.invalidate();
    }
    
    public final void setTileBackgroundColor(final int color) {
        if (Color.alpha(color) == 0) {
            this.tileBgPaint = null;
        }
        else {
            (this.tileBgPaint = new Paint()).setStyle(Paint$Style.FILL);
            this.tileBgPaint.setColor(color);
        }
        this.invalidate();
    }
    
    public final void setZoomEnabled(final boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
    }
    
    public final PointF sourceToViewCoord(final float n, final float n2, final PointF pointF) {
        if (this.vTranslate == null) {
            return null;
        }
        pointF.set(this.sourceToViewX(n), this.sourceToViewY(n2));
        return pointF;
    }
    
    public final PointF sourceToViewCoord(final PointF pointF) {
        return this.sourceToViewCoord(pointF.x, pointF.y, new PointF());
    }
    
    public final PointF viewToSourceCoord(final float n, final float n2) {
        return this.viewToSourceCoord(n, n2, new PointF());
    }
    
    public final PointF viewToSourceCoord(final float n, final float n2, final PointF pointF) {
        if (this.vTranslate == null) {
            return null;
        }
        pointF.set(this.viewToSourceX(n), this.viewToSourceY(n2));
        return pointF;
    }
    
    public final PointF viewToSourceCoord(final PointF pointF) {
        return this.viewToSourceCoord(pointF.x, pointF.y, new PointF());
    }
    
    private static class Anim
    {
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
            this.duration = 500L;
            this.interruptible = true;
            this.easing = 2;
            this.origin = 1;
            this.time = System.currentTimeMillis();
        }
    }
    
    public final class AnimationBuilder
    {
        private long duration;
        private int easing;
        private boolean interruptible;
        private OnAnimationEventListener listener;
        private int origin;
        private boolean panLimited;
        private final PointF targetSCenter;
        private final float targetScale;
        private final PointF vFocus;
        
        private AnimationBuilder(final float targetScale, final PointF targetSCenter) {
            this.duration = 500L;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = targetScale;
            this.targetSCenter = targetSCenter;
            this.vFocus = null;
        }
        
        private AnimationBuilder(final float targetScale, final PointF targetSCenter, final PointF vFocus) {
            this.duration = 500L;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = targetScale;
            this.targetSCenter = targetSCenter;
            this.vFocus = vFocus;
        }
        
        private AnimationBuilder(final PointF targetSCenter) {
            this.duration = 500L;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = SubsamplingScaleImageView.this.scale;
            this.targetSCenter = targetSCenter;
            this.vFocus = null;
        }
        
        private AnimationBuilder withOrigin(final int origin) {
            this.origin = origin;
            return this;
        }
        
        private AnimationBuilder withPanLimited(final boolean panLimited) {
            this.panLimited = panLimited;
            return this;
        }
        
        public void start() {
            if (SubsamplingScaleImageView.this.anim != null && SubsamplingScaleImageView.this.anim.listener != null) {
                try {
                    SubsamplingScaleImageView.this.anim.listener.onInterruptedByNewAnim();
                }
                catch (Exception ex) {
                    Log.w(SubsamplingScaleImageView.TAG, "Error thrown by animation listener", (Throwable)ex);
                }
            }
            final int paddingLeft = SubsamplingScaleImageView.this.getPaddingLeft();
            final int n = (SubsamplingScaleImageView.this.getWidth() - SubsamplingScaleImageView.this.getPaddingRight() - SubsamplingScaleImageView.this.getPaddingLeft()) / 2;
            final int paddingTop = SubsamplingScaleImageView.this.getPaddingTop();
            final int n2 = (SubsamplingScaleImageView.this.getHeight() - SubsamplingScaleImageView.this.getPaddingBottom() - SubsamplingScaleImageView.this.getPaddingTop()) / 2;
            final float access$6500 = SubsamplingScaleImageView.this.limitedScale(this.targetScale);
            PointF pointF;
            if (this.panLimited) {
                pointF = SubsamplingScaleImageView.this.limitedSCenter(this.targetSCenter.x, this.targetSCenter.y, access$6500, new PointF());
            }
            else {
                pointF = this.targetSCenter;
            }
            SubsamplingScaleImageView.this.anim = new Anim();
            SubsamplingScaleImageView.this.anim.scaleStart = SubsamplingScaleImageView.this.scale;
            SubsamplingScaleImageView.this.anim.scaleEnd = access$6500;
            SubsamplingScaleImageView.this.anim.time = System.currentTimeMillis();
            SubsamplingScaleImageView.this.anim.sCenterEndRequested = pointF;
            SubsamplingScaleImageView.this.anim.sCenterStart = SubsamplingScaleImageView.this.getCenter();
            SubsamplingScaleImageView.this.anim.sCenterEnd = pointF;
            SubsamplingScaleImageView.this.anim.vFocusStart = SubsamplingScaleImageView.this.sourceToViewCoord(pointF);
            SubsamplingScaleImageView.this.anim.vFocusEnd = new PointF((float)(paddingLeft + n), (float)(paddingTop + n2));
            SubsamplingScaleImageView.this.anim.duration = this.duration;
            SubsamplingScaleImageView.this.anim.interruptible = this.interruptible;
            SubsamplingScaleImageView.this.anim.easing = this.easing;
            SubsamplingScaleImageView.this.anim.origin = this.origin;
            SubsamplingScaleImageView.this.anim.time = System.currentTimeMillis();
            SubsamplingScaleImageView.this.anim.listener = this.listener;
            if (this.vFocus != null) {
                final float n3 = this.vFocus.x - SubsamplingScaleImageView.this.anim.sCenterStart.x * access$6500;
                final float n4 = this.vFocus.y - SubsamplingScaleImageView.this.anim.sCenterStart.y * access$6500;
                final ScaleAndTranslate scaleAndTranslate = new ScaleAndTranslate(access$6500, new PointF(n3, n4));
                SubsamplingScaleImageView.this.fitToBounds(true, scaleAndTranslate);
                SubsamplingScaleImageView.this.anim.vFocusEnd = new PointF(this.vFocus.x + (scaleAndTranslate.vTranslate.x - n3), this.vFocus.y + (scaleAndTranslate.vTranslate.y - n4));
            }
            SubsamplingScaleImageView.this.invalidate();
        }
        
        public AnimationBuilder withDuration(final long duration) {
            this.duration = duration;
            return this;
        }
        
        public AnimationBuilder withEasing(final int i) {
            if (SubsamplingScaleImageView.VALID_EASING_STYLES.contains(i)) {
                this.easing = i;
                return this;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown easing type: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        
        public AnimationBuilder withInterruptible(final boolean interruptible) {
            this.interruptible = interruptible;
            return this;
        }
    }
    
    private static class BitmapLoadTask extends AsyncTask<Void, Void, Integer>
    {
        private Bitmap bitmap;
        private final WeakReference<Context> contextRef;
        private final WeakReference<DecoderFactory<? extends ImageDecoder>> decoderFactoryRef;
        private Exception exception;
        private final boolean preview;
        private final Uri source;
        private final WeakReference<SubsamplingScaleImageView> viewRef;
        
        BitmapLoadTask(final SubsamplingScaleImageView referent, final Context referent2, final DecoderFactory<? extends ImageDecoder> referent3, final Uri source, final boolean preview) {
            this.viewRef = new WeakReference<SubsamplingScaleImageView>(referent);
            this.contextRef = new WeakReference<Context>(referent2);
            this.decoderFactoryRef = new WeakReference<DecoderFactory<? extends ImageDecoder>>(referent3);
            this.source = source;
            this.preview = preview;
        }
        
        protected Integer doInBackground(final Void... array) {
            try {
                final String string = this.source.toString();
                final Context context = this.contextRef.get();
                final DecoderFactory<ImageDecoder> decoderFactory = this.decoderFactoryRef.get();
                final SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
                if (context != null && decoderFactory != null && subsamplingScaleImageView != null) {
                    subsamplingScaleImageView.debug("BitmapLoadTask.doInBackground", new Object[0]);
                    this.bitmap = decoderFactory.make().decode(context, this.source);
                    return subsamplingScaleImageView.getExifOrientation(context, string);
                }
            }
            catch (OutOfMemoryError cause) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to load bitmap - OutOfMemoryError", (Throwable)cause);
                this.exception = new RuntimeException(cause);
            }
            catch (Exception exception) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to load bitmap", (Throwable)exception);
                this.exception = exception;
            }
            return null;
        }
        
        protected void onPostExecute(final Integer n) {
            final SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
            if (subsamplingScaleImageView != null) {
                if (this.bitmap != null && n != null) {
                    if (this.preview) {
                        subsamplingScaleImageView.onPreviewLoaded(this.bitmap);
                    }
                    else {
                        subsamplingScaleImageView.onImageLoaded(this.bitmap, n, false);
                    }
                }
                else if (this.exception != null && subsamplingScaleImageView.onImageEventListener != null) {
                    if (this.preview) {
                        subsamplingScaleImageView.onImageEventListener.onPreviewLoadError(this.exception);
                    }
                    else {
                        subsamplingScaleImageView.onImageEventListener.onImageLoadError(this.exception);
                    }
                }
            }
        }
    }
    
    public interface OnAnimationEventListener
    {
        void onComplete();
        
        void onInterruptedByNewAnim();
        
        void onInterruptedByUser();
    }
    
    public interface OnImageEventListener
    {
        void onImageLoadError(final Exception p0);
        
        void onImageLoaded();
        
        void onPreviewLoadError(final Exception p0);
        
        void onPreviewReleased();
        
        void onReady();
        
        void onTileLoadError(final Exception p0);
    }
    
    public interface OnStateChangedListener
    {
        void onCenterChanged(final PointF p0, final int p1);
        
        void onScaleChanged(final float p0, final int p1);
    }
    
    private static class ScaleAndTranslate
    {
        private float scale;
        private PointF vTranslate;
        
        private ScaleAndTranslate(final float scale, final PointF vTranslate) {
            this.scale = scale;
            this.vTranslate = vTranslate;
        }
    }
    
    private static class Tile
    {
        private Bitmap bitmap;
        private Rect fileSRect;
        private boolean loading;
        private Rect sRect;
        private int sampleSize;
        private Rect vRect;
        private boolean visible;
    }
    
    private static class TileLoadTask extends AsyncTask<Void, Void, Bitmap>
    {
        private final WeakReference<ImageRegionDecoder> decoderRef;
        private Exception exception;
        private final WeakReference<Tile> tileRef;
        private final WeakReference<SubsamplingScaleImageView> viewRef;
        
        TileLoadTask(final SubsamplingScaleImageView referent, final ImageRegionDecoder referent2, final Tile referent3) {
            this.viewRef = new WeakReference<SubsamplingScaleImageView>(referent);
            this.decoderRef = new WeakReference<ImageRegionDecoder>(referent2);
            this.tileRef = new WeakReference<Tile>(referent3);
            referent3.loading = true;
        }
        
        protected Bitmap doInBackground(final Void... array) {
            try {
                final SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
                final ImageRegionDecoder imageRegionDecoder = this.decoderRef.get();
                final Tile tile = this.tileRef.get();
                if (imageRegionDecoder != null && tile != null && subsamplingScaleImageView != null && imageRegionDecoder.isReady() && tile.visible) {
                    subsamplingScaleImageView.debug("TileLoadTask.doInBackground, tile.sRect=%s, tile.sampleSize=%d", tile.sRect, tile.sampleSize);
                    synchronized (subsamplingScaleImageView.decoderLock) {
                        subsamplingScaleImageView.fileSRect(tile.sRect, tile.fileSRect);
                        if (subsamplingScaleImageView.sRegion != null) {
                            tile.fileSRect.offset(subsamplingScaleImageView.sRegion.left, subsamplingScaleImageView.sRegion.top);
                        }
                        return imageRegionDecoder.decodeRegion(tile.fileSRect, tile.sampleSize);
                    }
                }
                if (tile != null) {
                    tile.loading = false;
                }
            }
            catch (OutOfMemoryError cause) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to decode tile - OutOfMemoryError", (Throwable)cause);
                this.exception = new RuntimeException(cause);
            }
            catch (Exception exception) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to decode tile", (Throwable)exception);
                this.exception = exception;
            }
            return null;
        }
        
        protected void onPostExecute(final Bitmap bitmap) {
            final SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
            final Tile tile = this.tileRef.get();
            if (subsamplingScaleImageView != null && tile != null) {
                if (bitmap != null) {
                    tile.bitmap = bitmap;
                    tile.loading = false;
                    subsamplingScaleImageView.onTileLoaded();
                }
                else if (this.exception != null && subsamplingScaleImageView.onImageEventListener != null) {
                    subsamplingScaleImageView.onImageEventListener.onTileLoadError(this.exception);
                }
            }
        }
    }
    
    private static class TilesInitTask extends AsyncTask<Void, Void, int[]>
    {
        private final WeakReference<Context> contextRef;
        private ImageRegionDecoder decoder;
        private final WeakReference<DecoderFactory<? extends ImageRegionDecoder>> decoderFactoryRef;
        private Exception exception;
        private final Uri source;
        private final WeakReference<SubsamplingScaleImageView> viewRef;
        
        TilesInitTask(final SubsamplingScaleImageView referent, final Context referent2, final DecoderFactory<? extends ImageRegionDecoder> referent3, final Uri source) {
            this.viewRef = new WeakReference<SubsamplingScaleImageView>(referent);
            this.contextRef = new WeakReference<Context>(referent2);
            this.decoderFactoryRef = new WeakReference<DecoderFactory<? extends ImageRegionDecoder>>(referent3);
            this.source = source;
        }
        
        protected int[] doInBackground(final Void... array) {
            try {
                final String string = this.source.toString();
                final Context context = this.contextRef.get();
                final DecoderFactory<ImageRegionDecoder> decoderFactory = this.decoderFactoryRef.get();
                final SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
                if (context != null && decoderFactory != null && subsamplingScaleImageView != null) {
                    subsamplingScaleImageView.debug("TilesInitTask.doInBackground", new Object[0]);
                    this.decoder = decoderFactory.make();
                    final Point init = this.decoder.init(context, this.source);
                    int n = init.x;
                    int n2 = init.y;
                    final int access$5200 = subsamplingScaleImageView.getExifOrientation(context, string);
                    if (subsamplingScaleImageView.sRegion != null) {
                        n = subsamplingScaleImageView.sRegion.width();
                        n2 = subsamplingScaleImageView.sRegion.height();
                    }
                    return new int[] { n, n2, access$5200 };
                }
            }
            catch (Exception exception) {
                Log.e(SubsamplingScaleImageView.TAG, "Failed to initialise bitmap decoder", (Throwable)exception);
                this.exception = exception;
            }
            return null;
        }
        
        protected void onPostExecute(final int[] array) {
            final SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
            if (subsamplingScaleImageView != null) {
                if (this.decoder != null && array != null && array.length == 3) {
                    subsamplingScaleImageView.onTilesInited(this.decoder, array[0], array[1], array[2]);
                }
                else if (this.exception != null && subsamplingScaleImageView.onImageEventListener != null) {
                    subsamplingScaleImageView.onImageEventListener.onImageLoadError(this.exception);
                }
            }
        }
    }
}

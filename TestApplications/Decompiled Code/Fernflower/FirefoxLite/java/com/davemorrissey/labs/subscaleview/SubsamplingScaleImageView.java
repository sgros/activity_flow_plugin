package com.davemorrissey.labs.subscaleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.MeasureSpec;
import android.view.View.OnLongClickListener;
import com.davemorrissey.labs.subscaleview.decoder.CompatDecoderFactory;
import com.davemorrissey.labs.subscaleview.decoder.DecoderFactory;
import com.davemorrissey.labs.subscaleview.decoder.ImageRegionDecoder;
import com.davemorrissey.labs.subscaleview.decoder.SkiaImageDecoder;
import com.davemorrissey.labs.subscaleview.decoder.SkiaImageRegionDecoder;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

public class SubsamplingScaleImageView extends View {
   private static final String TAG = "SubsamplingScaleImageView";
   public static int TILE_SIZE_AUTO = Integer.MAX_VALUE;
   private static final List VALID_EASING_STYLES = Arrays.asList(2, 1);
   private static final List VALID_ORIENTATIONS = Arrays.asList(0, 90, 180, 270, -1);
   private static final List VALID_PAN_LIMITS = Arrays.asList(1, 2, 3);
   private static final List VALID_SCALE_TYPES = Arrays.asList(2, 1, 3);
   private static final List VALID_ZOOM_STYLES = Arrays.asList(1, 2, 3);
   private SubsamplingScaleImageView.Anim anim;
   private Bitmap bitmap;
   private DecoderFactory bitmapDecoderFactory;
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
   private SubsamplingScaleImageView.OnImageEventListener onImageEventListener;
   private OnLongClickListener onLongClickListener;
   private SubsamplingScaleImageView.OnStateChangedListener onStateChangedListener;
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
   private DecoderFactory regionDecoderFactory;
   private int sHeight;
   private int sOrientation;
   private PointF sPendingCenter;
   private RectF sRect;
   private Rect sRegion;
   private PointF sRequestedCenter;
   private int sWidth;
   private SubsamplingScaleImageView.ScaleAndTranslate satTemp;
   private float scale;
   private float scaleStart;
   private float[] srcArray;
   private Paint tileBgPaint;
   private Map tileMap;
   private Uri uri;
   private PointF vCenterStart;
   private float vDistStart;
   private PointF vTranslate;
   private PointF vTranslateBefore;
   private PointF vTranslateStart;
   private boolean zoomEnabled;

   public SubsamplingScaleImageView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SubsamplingScaleImageView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.orientation = 0;
      this.maxScale = 2.0F;
      this.minScale = this.minScale();
      this.minimumTileDpi = -1;
      this.panLimit = 1;
      this.minimumScaleType = 1;
      this.maxTileWidth = TILE_SIZE_AUTO;
      this.maxTileHeight = TILE_SIZE_AUTO;
      this.panEnabled = true;
      this.zoomEnabled = true;
      this.quickScaleEnabled = true;
      this.doubleTapZoomScale = 1.0F;
      this.doubleTapZoomStyle = 1;
      this.doubleTapZoomDuration = 500;
      this.decoderLock = new Object();
      this.bitmapDecoderFactory = new CompatDecoderFactory(SkiaImageDecoder.class);
      this.regionDecoderFactory = new CompatDecoderFactory(SkiaImageRegionDecoder.class);
      this.srcArray = new float[8];
      this.dstArray = new float[8];
      this.density = this.getResources().getDisplayMetrics().density;
      this.setMinimumDpi(160);
      this.setDoubleTapZoomDpi(160);
      this.setGestureDetector(var1);
      this.handler = new Handler(new Callback() {
         public boolean handleMessage(Message var1) {
            if (var1.what == 1 && SubsamplingScaleImageView.this.onLongClickListener != null) {
               SubsamplingScaleImageView.this.maxTouchCount = 0;
               SubsamplingScaleImageView.super.setOnLongClickListener(SubsamplingScaleImageView.this.onLongClickListener);
               SubsamplingScaleImageView.this.performLongClick();
               SubsamplingScaleImageView.super.setOnLongClickListener((OnLongClickListener)null);
            }

            return true;
         }
      });
      if (var2 != null) {
         TypedArray var3 = this.getContext().obtainStyledAttributes(var2, R.styleable.SubsamplingScaleImageView);
         if (var3.hasValue(R.styleable.SubsamplingScaleImageView_assetName)) {
            String var5 = var3.getString(R.styleable.SubsamplingScaleImageView_assetName);
            if (var5 != null && var5.length() > 0) {
               this.setImage(ImageSource.asset(var5).tilingEnabled());
            }
         }

         if (var3.hasValue(R.styleable.SubsamplingScaleImageView_src)) {
            int var4 = var3.getResourceId(R.styleable.SubsamplingScaleImageView_src, 0);
            if (var4 > 0) {
               this.setImage(ImageSource.resource(var4).tilingEnabled());
            }
         }

         if (var3.hasValue(R.styleable.SubsamplingScaleImageView_panEnabled)) {
            this.setPanEnabled(var3.getBoolean(R.styleable.SubsamplingScaleImageView_panEnabled, true));
         }

         if (var3.hasValue(R.styleable.SubsamplingScaleImageView_zoomEnabled)) {
            this.setZoomEnabled(var3.getBoolean(R.styleable.SubsamplingScaleImageView_zoomEnabled, true));
         }

         if (var3.hasValue(R.styleable.SubsamplingScaleImageView_quickScaleEnabled)) {
            this.setQuickScaleEnabled(var3.getBoolean(R.styleable.SubsamplingScaleImageView_quickScaleEnabled, true));
         }

         if (var3.hasValue(R.styleable.SubsamplingScaleImageView_tileBackgroundColor)) {
            this.setTileBackgroundColor(var3.getColor(R.styleable.SubsamplingScaleImageView_tileBackgroundColor, Color.argb(0, 0, 0, 0)));
         }

         var3.recycle();
      }

      this.quickScaleThreshold = TypedValue.applyDimension(1, 20.0F, var1.getResources().getDisplayMetrics());
   }

   // $FF: synthetic method
   static void access$5100(SubsamplingScaleImageView var0, String var1, Object[] var2) {
      var0.debug(var1, var2);
   }

   // $FF: synthetic method
   static int access$5200(SubsamplingScaleImageView var0, Context var1, String var2) {
      return var0.getExifOrientation(var1, var2);
   }

   // $FF: synthetic method
   static Rect access$5300(SubsamplingScaleImageView var0) {
      return var0.sRegion;
   }

   // $FF: synthetic method
   static Object access$5700(SubsamplingScaleImageView var0) {
      return var0.decoderLock;
   }

   // $FF: synthetic method
   static void access$5800(SubsamplingScaleImageView var0, Rect var1, Rect var2) {
      var0.fileSRect(var1, var2);
   }

   private int calculateInSampleSize(float var1) {
      float var2 = var1;
      if (this.minimumTileDpi > 0) {
         DisplayMetrics var3 = this.getResources().getDisplayMetrics();
         var2 = (var3.xdpi + var3.ydpi) / 2.0F;
         var2 = var1 * ((float)this.minimumTileDpi / var2);
      }

      int var4 = (int)((float)this.sWidth() * var2);
      int var5 = (int)((float)this.sHeight() * var2);
      if (var4 != 0 && var5 != 0) {
         int var6 = this.sHeight();
         int var7 = 1;
         if (var6 <= var5 && this.sWidth() <= var4) {
            var6 = 1;
         } else {
            var6 = Math.round((float)this.sHeight() / (float)var5);
            var4 = Math.round((float)this.sWidth() / (float)var4);
            if (var6 >= var4) {
               var6 = var4;
            }
         }

         while(true) {
            var4 = var7 * 2;
            if (var4 >= var6) {
               return var7;
            }

            var7 = var4;
         }
      } else {
         return 32;
      }
   }

   private boolean checkImageLoaded() {
      boolean var1 = this.isBaseLayerReady();
      if (!this.imageLoadedSent && var1) {
         this.preDraw();
         this.imageLoadedSent = true;
         this.onImageLoaded();
         if (this.onImageEventListener != null) {
            this.onImageEventListener.onImageLoaded();
         }
      }

      return var1;
   }

   private boolean checkReady() {
      boolean var1;
      if (this.getWidth() <= 0 || this.getHeight() <= 0 || this.sWidth <= 0 || this.sHeight <= 0 || this.bitmap == null && !this.isBaseLayerReady()) {
         var1 = false;
      } else {
         var1 = true;
      }

      if (!this.readySent && var1) {
         this.preDraw();
         this.readySent = true;
         this.onReady();
         if (this.onImageEventListener != null) {
            this.onImageEventListener.onReady();
         }
      }

      return var1;
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
         this.debugPaint.setTextSize(18.0F);
         this.debugPaint.setColor(-65281);
         this.debugPaint.setStyle(Style.STROKE);
      }

   }

   private void debug(String var1, Object... var2) {
      if (this.debug) {
         Log.d(TAG, String.format(var1, var2));
      }

   }

   private float distance(float var1, float var2, float var3, float var4) {
      var1 -= var2;
      var2 = var3 - var4;
      return (float)Math.sqrt((double)(var1 * var1 + var2 * var2));
   }

   private void doubleTapZoom(PointF var1, PointF var2) {
      if (!this.panEnabled) {
         if (this.sRequestedCenter != null) {
            var1.x = this.sRequestedCenter.x;
            var1.y = this.sRequestedCenter.y;
         } else {
            var1.x = (float)(this.sWidth() / 2);
            var1.y = (float)(this.sHeight() / 2);
         }
      }

      float var3 = Math.min(this.maxScale, this.doubleTapZoomScale);
      boolean var4;
      if ((double)this.scale <= (double)var3 * 0.9D) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (!var4) {
         var3 = this.minScale();
      }

      if (this.doubleTapZoomStyle == 3) {
         this.setScaleAndCenter(var3, var1);
      } else if (this.doubleTapZoomStyle != 2 && var4 && this.panEnabled) {
         if (this.doubleTapZoomStyle == 1) {
            (new SubsamplingScaleImageView.AnimationBuilder(var3, var1, var2)).withInterruptible(false).withDuration((long)this.doubleTapZoomDuration).withOrigin(4).start();
         }
      } else {
         (new SubsamplingScaleImageView.AnimationBuilder(var3, var1)).withInterruptible(false).withDuration((long)this.doubleTapZoomDuration).withOrigin(4).start();
      }

      this.invalidate();
   }

   private float ease(int var1, long var2, float var4, float var5, long var6) {
      switch(var1) {
      case 1:
         return this.easeOutQuad(var2, var4, var5, var6);
      case 2:
         return this.easeInOutQuad(var2, var4, var5, var6);
      default:
         StringBuilder var8 = new StringBuilder();
         var8.append("Unexpected easing type: ");
         var8.append(var1);
         throw new IllegalStateException(var8.toString());
      }
   }

   private float easeInOutQuad(long var1, float var3, float var4, long var5) {
      float var7 = (float)var1 / ((float)var5 / 2.0F);
      if (var7 < 1.0F) {
         return var4 / 2.0F * var7 * var7 + var3;
      } else {
         --var7;
         return -var4 / 2.0F * (var7 * (var7 - 2.0F) - 1.0F) + var3;
      }
   }

   private float easeOutQuad(long var1, float var3, float var4, long var5) {
      float var7 = (float)var1 / (float)var5;
      return -var4 * var7 * (var7 - 2.0F) + var3;
   }

   private void execute(AsyncTask var1) {
      if (this.parallelLoadingEnabled && VERSION.SDK_INT >= 11) {
         try {
            Executor var2 = (Executor)AsyncTask.class.getField("THREAD_POOL_EXECUTOR").get((Object)null);
            AsyncTask.class.getMethod("executeOnExecutor", Executor.class, Object[].class).invoke(var1, var2, null);
            return;
         } catch (Exception var3) {
            Log.i(TAG, "Failed to execute AsyncTask on thread pool executor, falling back to single threaded executor", var3);
         }
      }

      var1.execute(new Void[0]);
   }

   private void fileSRect(Rect var1, Rect var2) {
      if (this.getRequiredRotation() == 0) {
         var2.set(var1);
      } else if (this.getRequiredRotation() == 90) {
         var2.set(var1.top, this.sHeight - var1.right, var1.bottom, this.sHeight - var1.left);
      } else if (this.getRequiredRotation() == 180) {
         var2.set(this.sWidth - var1.right, this.sHeight - var1.bottom, this.sWidth - var1.left, this.sHeight - var1.top);
      } else {
         var2.set(this.sWidth - var1.bottom, var1.left, this.sWidth - var1.top, var1.right);
      }

   }

   private void fitToBounds(boolean var1) {
      boolean var2;
      if (this.vTranslate == null) {
         var2 = true;
         this.vTranslate = new PointF(0.0F, 0.0F);
      } else {
         var2 = false;
      }

      if (this.satTemp == null) {
         this.satTemp = new SubsamplingScaleImageView.ScaleAndTranslate(0.0F, new PointF(0.0F, 0.0F));
      }

      this.satTemp.scale = this.scale;
      this.satTemp.vTranslate.set(this.vTranslate);
      this.fitToBounds(var1, this.satTemp);
      this.scale = this.satTemp.scale;
      this.vTranslate.set(this.satTemp.vTranslate);
      if (var2) {
         this.vTranslate.set(this.vTranslateForSCenter((float)(this.sWidth() / 2), (float)(this.sHeight() / 2), this.scale));
      }

   }

   private void fitToBounds(boolean var1, SubsamplingScaleImageView.ScaleAndTranslate var2) {
      boolean var3 = var1;
      if (this.panLimit == 2) {
         var3 = var1;
         if (this.isReady()) {
            var3 = false;
         }
      }

      PointF var4 = var2.vTranslate;
      float var5 = this.limitedScale(var2.scale);
      float var6 = (float)this.sWidth() * var5;
      float var7 = (float)this.sHeight() * var5;
      if (this.panLimit == 3 && this.isReady()) {
         var4.x = Math.max(var4.x, (float)(this.getWidth() / 2) - var6);
         var4.y = Math.max(var4.y, (float)(this.getHeight() / 2) - var7);
      } else if (var3) {
         var4.x = Math.max(var4.x, (float)this.getWidth() - var6);
         var4.y = Math.max(var4.y, (float)this.getHeight() - var7);
      } else {
         var4.x = Math.max(var4.x, -var6);
         var4.y = Math.max(var4.y, -var7);
      }

      int var8 = this.getPaddingLeft();
      float var9 = 0.5F;
      float var10;
      if (var8 <= 0 && this.getPaddingRight() <= 0) {
         var10 = 0.5F;
      } else {
         var10 = (float)this.getPaddingLeft() / (float)(this.getPaddingLeft() + this.getPaddingRight());
      }

      if (this.getPaddingTop() > 0 || this.getPaddingBottom() > 0) {
         var9 = (float)this.getPaddingTop() / (float)(this.getPaddingTop() + this.getPaddingBottom());
      }

      if (this.panLimit == 3 && this.isReady()) {
         var9 = (float)Math.max(0, this.getWidth() / 2);
         var10 = (float)Math.max(0, this.getHeight() / 2);
      } else if (var3) {
         var6 = Math.max(0.0F, ((float)this.getWidth() - var6) * var10);
         var10 = Math.max(0.0F, ((float)this.getHeight() - var7) * var9);
         var9 = var6;
      } else {
         var9 = (float)Math.max(0, this.getWidth());
         var10 = (float)Math.max(0, this.getHeight());
      }

      var4.x = Math.min(var4.x, var9);
      var4.y = Math.min(var4.y, var10);
      var2.scale = var5;
   }

   private int getExifOrientation(Context param1, String param2) {
      // $FF: Couldn't be decompiled
   }

   private Point getMaxBitmapDimensions(Canvas var1) {
      int var2 = VERSION.SDK_INT;
      int var3 = 2048;
      if (var2 >= 14) {
         label33: {
            try {
               var2 = (Integer)Canvas.class.getMethod("getMaximumBitmapWidth").invoke(var1);
            } catch (Exception var6) {
               break label33;
            }

            int var4;
            try {
               var4 = (Integer)Canvas.class.getMethod("getMaximumBitmapHeight").invoke(var1);
            } catch (Exception var5) {
               return new Point(Math.min(var2, this.maxTileWidth), Math.min(var3, this.maxTileHeight));
            }

            var3 = var4;
            return new Point(Math.min(var2, this.maxTileWidth), Math.min(var3, this.maxTileHeight));
         }
      }

      var2 = 2048;
      return new Point(Math.min(var2, this.maxTileWidth), Math.min(var3, this.maxTileHeight));
   }

   private int getRequiredRotation() {
      return this.orientation == -1 ? this.sOrientation : this.orientation;
   }

   private void initialiseBaseLayer(Point var1) {
      synchronized(this){}

      Throwable var10000;
      label331: {
         boolean var10001;
         try {
            this.debug("initialiseBaseLayer maxTileDimensions=%dx%d", var1.x, var1.y);
            PointF var3 = new PointF(0.0F, 0.0F);
            SubsamplingScaleImageView.ScaleAndTranslate var2 = new SubsamplingScaleImageView.ScaleAndTranslate(0.0F, var3);
            this.satTemp = var2;
            this.fitToBounds(true, this.satTemp);
            this.fullImageSampleSize = this.calculateInSampleSize(this.satTemp.scale);
            if (this.fullImageSampleSize > 1) {
               this.fullImageSampleSize /= 2;
            }
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label331;
         }

         try {
            if (this.fullImageSampleSize == 1 && this.sRegion == null && this.sWidth() < var1.x && this.sHeight() < var1.y) {
               this.decoder.recycle();
               this.decoder = null;
               SubsamplingScaleImageView.BitmapLoadTask var36 = new SubsamplingScaleImageView.BitmapLoadTask(this, this.getContext(), this.bitmapDecoderFactory, this.uri, false);
               this.execute(var36);
               return;
            }
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label331;
         }

         Iterator var38;
         try {
            this.initialiseTileMap(var1);
            var38 = ((List)this.tileMap.get(this.fullImageSampleSize)).iterator();
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label331;
         }

         while(true) {
            try {
               if (!var38.hasNext()) {
                  break;
               }

               SubsamplingScaleImageView.Tile var34 = (SubsamplingScaleImageView.Tile)var38.next();
               SubsamplingScaleImageView.TileLoadTask var37 = new SubsamplingScaleImageView.TileLoadTask(this, this.decoder, var34);
               this.execute(var37);
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label331;
            }
         }

         label310:
         try {
            this.refreshRequiredTiles(true);
            return;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label310;
         }
      }

      Throwable var35 = var10000;
      throw var35;
   }

   private void initialiseTileMap(Point var1) {
      this.debug("initialiseTileMap maxTileDimensions=%dx%d", var1.x, var1.y);
      this.tileMap = new LinkedHashMap();
      int var2 = this.fullImageSampleSize;
      int var3 = 1;
      int var4 = 1;

      while(true) {
         int var5 = this.sWidth() / var3;
         int var6 = this.sHeight() / var4;
         int var7 = var5 / var2;
         int var8 = var6 / var2;

         int var9;
         int var10;
         int var11;
         while(true) {
            if (var7 + var3 + 1 <= var1.x) {
               var9 = var4;
               var10 = var6;
               var11 = var8;
               if ((double)var7 <= (double)this.getWidth() * 1.25D) {
                  break;
               }

               var9 = var4;
               var10 = var6;
               var11 = var8;
               if (var2 >= this.fullImageSampleSize) {
                  break;
               }
            }

            ++var3;
            var5 = this.sWidth() / var3;
            var7 = var5 / var2;
         }

         while(var11 + var9 + 1 > var1.y || (double)var11 > (double)this.getHeight() * 1.25D && var2 < this.fullImageSampleSize) {
            ++var9;
            var10 = this.sHeight() / var9;
            var11 = var10 / var2;
         }

         ArrayList var12 = new ArrayList(var3 * var9);

         for(var4 = 0; var4 < var3; ++var4) {
            for(var7 = 0; var7 < var9; ++var7) {
               SubsamplingScaleImageView.Tile var13 = new SubsamplingScaleImageView.Tile();
               var13.sampleSize = var2;
               boolean var14;
               if (var2 == this.fullImageSampleSize) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               var13.visible = var14;
               if (var4 == var3 - 1) {
                  var11 = this.sWidth();
               } else {
                  var11 = (var4 + 1) * var5;
               }

               if (var7 == var9 - 1) {
                  var6 = this.sHeight();
               } else {
                  var6 = (var7 + 1) * var10;
               }

               var13.sRect = new Rect(var4 * var5, var7 * var10, var11, var6);
               var13.vRect = new Rect(0, 0, 0, 0);
               var13.fileSRect = new Rect(var13.sRect);
               var12.add(var13);
            }
         }

         this.tileMap.put(var2, var12);
         if (var2 == 1) {
            return;
         }

         var2 /= 2;
         var4 = var9;
      }
   }

   private boolean isBaseLayerReady() {
      Bitmap var1 = this.bitmap;
      boolean var2 = true;
      if (var1 != null && !this.bitmapIsPreview) {
         return true;
      } else if (this.tileMap == null) {
         return false;
      } else {
         Iterator var6 = this.tileMap.entrySet().iterator();

         label40:
         while(true) {
            Entry var3;
            do {
               if (!var6.hasNext()) {
                  return var2;
               }

               var3 = (Entry)var6.next();
            } while((Integer)var3.getKey() != this.fullImageSampleSize);

            Iterator var7 = ((List)var3.getValue()).iterator();
            boolean var4 = var2;

            while(true) {
               SubsamplingScaleImageView.Tile var5;
               do {
                  var2 = var4;
                  if (!var7.hasNext()) {
                     continue label40;
                  }

                  var5 = (SubsamplingScaleImageView.Tile)var7.next();
               } while(!var5.loading && var5.bitmap != null);

               var4 = false;
            }
         }
      }
   }

   private PointF limitedSCenter(float var1, float var2, float var3, PointF var4) {
      PointF var5 = this.vTranslateForSCenter(var1, var2, var3);
      int var6 = this.getPaddingLeft();
      int var7 = (this.getWidth() - this.getPaddingRight() - this.getPaddingLeft()) / 2;
      int var8 = this.getPaddingTop();
      int var9 = (this.getHeight() - this.getPaddingBottom() - this.getPaddingTop()) / 2;
      var4.set(((float)(var6 + var7) - var5.x) / var3, ((float)(var8 + var9) - var5.y) / var3);
      return var4;
   }

   private float limitedScale(float var1) {
      var1 = Math.max(this.minScale(), var1);
      return Math.min(this.maxScale, var1);
   }

   private float minScale() {
      int var1 = this.getPaddingBottom() + this.getPaddingTop();
      int var2 = this.getPaddingLeft() + this.getPaddingRight();
      if (this.minimumScaleType == 2) {
         return Math.max((float)(this.getWidth() - var2) / (float)this.sWidth(), (float)(this.getHeight() - var1) / (float)this.sHeight());
      } else {
         return this.minimumScaleType == 3 && this.minScale > 0.0F ? this.minScale : Math.min((float)(this.getWidth() - var2) / (float)this.sWidth(), (float)(this.getHeight() - var1) / (float)this.sHeight());
      }
   }

   private void onImageLoaded(Bitmap var1, int var2, boolean var3) {
      synchronized(this){}

      Throwable var10000;
      label512: {
         boolean var10001;
         label504: {
            try {
               this.debug("onImageLoaded");
               if (this.sWidth <= 0 || this.sHeight <= 0 || this.sWidth == var1.getWidth() && this.sHeight == var1.getHeight()) {
                  break label504;
               }
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label512;
            }

            try {
               this.reset(false);
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label512;
            }
         }

         try {
            if (this.bitmap != null && !this.bitmapIsCached) {
               this.bitmap.recycle();
            }
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label512;
         }

         try {
            if (this.bitmap != null && this.bitmapIsCached && this.onImageEventListener != null) {
               this.onImageEventListener.onPreviewReleased();
            }
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label512;
         }

         boolean var4;
         try {
            this.bitmapIsPreview = false;
            this.bitmapIsCached = var3;
            this.bitmap = var1;
            this.sWidth = var1.getWidth();
            this.sHeight = var1.getHeight();
            this.sOrientation = var2;
            var4 = this.checkReady();
            var3 = this.checkImageLoaded();
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label512;
         }

         if (!var4 && !var3) {
            return;
         }

         label481:
         try {
            this.invalidate();
            this.requestLayout();
            return;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label481;
         }
      }

      Throwable var47 = var10000;
      throw var47;
   }

   private void onPreviewLoaded(Bitmap var1) {
      synchronized(this){}

      Throwable var10000;
      label350: {
         boolean var10001;
         label352: {
            label343:
            try {
               this.debug("onPreviewLoaded");
               if (this.bitmap == null && !this.imageLoadedSent) {
                  break label343;
               }
               break label352;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label350;
            }

            label353: {
               try {
                  if (this.pRegion != null) {
                     this.bitmap = Bitmap.createBitmap(var1, this.pRegion.left, this.pRegion.top, this.pRegion.width(), this.pRegion.height());
                     break label353;
                  }
               } catch (Throwable var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label350;
               }

               try {
                  this.bitmap = var1;
               } catch (Throwable var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label350;
               }
            }

            try {
               this.bitmapIsPreview = true;
               if (this.checkReady()) {
                  this.invalidate();
                  this.requestLayout();
               }
            } catch (Throwable var27) {
               var10000 = var27;
               var10001 = false;
               break label350;
            }

            return;
         }

         try {
            var1.recycle();
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label350;
         }

         return;
      }

      Throwable var32 = var10000;
      throw var32;
   }

   private void onTileLoaded() {
      synchronized(this){}

      try {
         this.debug("onTileLoaded");
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
      } finally {
         ;
      }

   }

   private void onTilesInited(ImageRegionDecoder var1, int var2, int var3, int var4) {
      synchronized(this){}

      try {
         this.debug("onTilesInited sWidth=%d, sHeight=%d, sOrientation=%d", var2, var3, this.orientation);
         if (this.sWidth > 0 && this.sHeight > 0 && (this.sWidth != var2 || this.sHeight != var3)) {
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

         this.decoder = var1;
         this.sWidth = var2;
         this.sHeight = var3;
         this.sOrientation = var4;
         this.checkReady();
         if (!this.checkImageLoaded() && this.maxTileWidth > 0 && this.maxTileWidth != TILE_SIZE_AUTO && this.maxTileHeight > 0 && this.maxTileHeight != TILE_SIZE_AUTO && this.getWidth() > 0 && this.getHeight() > 0) {
            Point var7 = new Point(this.maxTileWidth, this.maxTileHeight);
            this.initialiseBaseLayer(var7);
         }

         this.invalidate();
         this.requestLayout();
      } finally {
         ;
      }

   }

   private boolean onTouchEventInternal(MotionEvent var1) {
      int var2 = var1.getPointerCount();
      float var9;
      switch(var1.getAction()) {
      case 0:
      case 5:
      case 261:
         this.anim = null;
         this.requestDisallowInterceptTouchEvent(true);
         this.maxTouchCount = Math.max(this.maxTouchCount, var2);
         if (var2 >= 2) {
            if (this.zoomEnabled) {
               var9 = this.distance(var1.getX(0), var1.getX(1), var1.getY(0), var1.getY(1));
               this.scaleStart = this.scale;
               this.vDistStart = var9;
               this.vTranslateStart.set(this.vTranslate.x, this.vTranslate.y);
               this.vCenterStart.set((var1.getX(0) + var1.getX(1)) / 2.0F, (var1.getY(0) + var1.getY(1)) / 2.0F);
            } else {
               this.maxTouchCount = 0;
            }

            this.handler.removeMessages(1);
         } else if (!this.isQuickScaling) {
            this.vTranslateStart.set(this.vTranslate.x, this.vTranslate.y);
            this.vCenterStart.set(var1.getX(), var1.getY());
            this.handler.sendEmptyMessageDelayed(1, 600L);
         }

         return true;
      case 1:
      case 6:
      case 262:
         this.handler.removeMessages(1);
         if (this.isQuickScaling) {
            this.isQuickScaling = false;
            if (!this.quickScaleMoved) {
               this.doubleTapZoom(this.quickScaleSCenter, this.vCenterStart);
            }
         }

         if (this.maxTouchCount <= 0 || !this.isZooming && !this.isPanning) {
            if (var2 == 1) {
               this.isZooming = false;
               this.isPanning = false;
               this.maxTouchCount = 0;
            }

            return true;
         }

         if (this.isZooming && var2 == 2) {
            this.isPanning = true;
            this.vTranslateStart.set(this.vTranslate.x, this.vTranslate.y);
            if (var1.getActionIndex() == 1) {
               this.vCenterStart.set(var1.getX(0), var1.getY(0));
            } else {
               this.vCenterStart.set(var1.getX(1), var1.getY(1));
            }
         }

         if (var2 < 3) {
            this.isZooming = false;
         }

         if (var2 < 2) {
            this.isPanning = false;
            this.maxTouchCount = 0;
         }

         this.refreshRequiredTiles(true);
         return true;
      case 2:
         boolean var19;
         label258: {
            if (this.maxTouchCount > 0) {
               label254: {
                  float var3;
                  float var4;
                  double var6;
                  float var8;
                  float var10;
                  float var11;
                  float var12;
                  if (var2 >= 2) {
                     var3 = this.distance(var1.getX(0), var1.getX(1), var1.getY(0), var1.getY(1));
                     var4 = (var1.getX(0) + var1.getX(1)) / 2.0F;
                     float var5 = (var1.getY(0) + var1.getY(1)) / 2.0F;
                     if (!this.zoomEnabled || this.distance(this.vCenterStart.x, var4, this.vCenterStart.y, var5) <= 5.0F && Math.abs(var3 - this.vDistStart) <= 5.0F && !this.isPanning) {
                        break label254;
                     }

                     this.isZooming = true;
                     this.isPanning = true;
                     var6 = (double)this.scale;
                     this.scale = Math.min(this.maxScale, var3 / this.vDistStart * this.scaleStart);
                     if (this.scale <= this.minScale()) {
                        this.vDistStart = var3;
                        this.scaleStart = this.minScale();
                        this.vCenterStart.set(var4, var5);
                        this.vTranslateStart.set(this.vTranslate);
                     } else if (!this.panEnabled) {
                        if (this.sRequestedCenter != null) {
                           this.vTranslate.x = (float)(this.getWidth() / 2) - this.scale * this.sRequestedCenter.x;
                           this.vTranslate.y = (float)(this.getHeight() / 2) - this.scale * this.sRequestedCenter.y;
                        } else {
                           this.vTranslate.x = (float)(this.getWidth() / 2) - this.scale * (float)(this.sWidth() / 2);
                           this.vTranslate.y = (float)(this.getHeight() / 2) - this.scale * (float)(this.sHeight() / 2);
                        }
                     } else {
                        var8 = this.vCenterStart.x;
                        var9 = this.vTranslateStart.x;
                        var10 = this.vCenterStart.y;
                        var11 = this.vTranslateStart.y;
                        var12 = this.scale / this.scaleStart;
                        float var13 = this.scale / this.scaleStart;
                        this.vTranslate.x = var4 - (var8 - var9) * var12;
                        this.vTranslate.y = var5 - (var10 - var11) * var13;
                        if ((double)this.sHeight() * var6 < (double)this.getHeight() && this.scale * (float)this.sHeight() >= (float)this.getHeight() || var6 * (double)this.sWidth() < (double)this.getWidth() && this.scale * (float)this.sWidth() >= (float)this.getWidth()) {
                           this.fitToBounds(true);
                           this.vCenterStart.set(var4, var5);
                           this.vTranslateStart.set(this.vTranslate);
                           this.scaleStart = this.scale;
                           this.vDistStart = var3;
                        }
                     }

                     this.fitToBounds(true);
                     this.refreshRequiredTiles(false);
                  } else if (this.isQuickScaling) {
                     var11 = Math.abs(this.quickScaleVStart.y - var1.getY()) * 2.0F + this.quickScaleThreshold;
                     if (this.quickScaleLastDistance == -1.0F) {
                        this.quickScaleLastDistance = var11;
                     }

                     if (var1.getY() > this.quickScaleVLastPoint.y) {
                        var19 = true;
                     } else {
                        var19 = false;
                     }

                     label247: {
                        this.quickScaleVLastPoint.set(0.0F, var1.getY());
                        var9 = var11 / this.quickScaleLastDistance;
                        var10 = 1.0F;
                        var4 = Math.abs(1.0F - var9) * 0.5F;
                        if (var4 <= 0.03F) {
                           var9 = var11;
                           if (!this.quickScaleMoved) {
                              break label247;
                           }
                        }

                        this.quickScaleMoved = true;
                        var9 = var10;
                        if (this.quickScaleLastDistance > 0.0F) {
                           if (var19) {
                              var9 = var4 + 1.0F;
                           } else {
                              var9 = 1.0F - var4;
                           }
                        }

                        var6 = (double)this.scale;
                        this.scale = Math.max(this.minScale(), Math.min(this.maxScale, this.scale * var9));
                        if (!this.panEnabled) {
                           if (this.sRequestedCenter != null) {
                              this.vTranslate.x = (float)(this.getWidth() / 2) - this.scale * this.sRequestedCenter.x;
                              this.vTranslate.y = (float)(this.getHeight() / 2) - this.scale * this.sRequestedCenter.y;
                              var9 = var11;
                           } else {
                              this.vTranslate.x = (float)(this.getWidth() / 2) - this.scale * (float)(this.sWidth() / 2);
                              this.vTranslate.y = (float)(this.getHeight() / 2) - this.scale * (float)(this.sHeight() / 2);
                              var9 = var11;
                           }
                        } else {
                           label265: {
                              var10 = this.vCenterStart.x;
                              var9 = this.vTranslateStart.x;
                              var12 = this.vCenterStart.y;
                              var4 = this.vTranslateStart.y;
                              var8 = this.scale / this.scaleStart;
                              var3 = this.scale / this.scaleStart;
                              this.vTranslate.x = this.vCenterStart.x - (var10 - var9) * var8;
                              this.vTranslate.y = this.vCenterStart.y - (var12 - var4) * var3;
                              if ((double)this.sHeight() * var6 >= (double)this.getHeight() || this.scale * (float)this.sHeight() < (float)this.getHeight()) {
                                 var9 = var11;
                                 if (var6 * (double)this.sWidth() >= (double)this.getWidth()) {
                                    break label265;
                                 }

                                 var9 = var11;
                                 if (this.scale * (float)this.sWidth() < (float)this.getWidth()) {
                                    break label265;
                                 }
                              }

                              this.fitToBounds(true);
                              this.vCenterStart.set(this.sourceToViewCoord(this.quickScaleSCenter));
                              this.vTranslateStart.set(this.vTranslate);
                              this.scaleStart = this.scale;
                              var9 = 0.0F;
                           }
                        }
                     }

                     this.quickScaleLastDistance = var9;
                     this.fitToBounds(true);
                     this.refreshRequiredTiles(false);
                  } else {
                     if (this.isZooming) {
                        break label254;
                     }

                     var10 = Math.abs(var1.getX() - this.vCenterStart.x);
                     var4 = Math.abs(var1.getY() - this.vCenterStart.y);
                     var8 = this.density * 5.0F;
                     float var20;
                     int var14 = (var20 = var10 - var8) == 0.0F ? 0 : (var20 < 0.0F ? -1 : 1);
                     if (var14 <= 0 && var4 <= var8 && !this.isPanning) {
                        break label254;
                     }

                     this.vTranslate.x = this.vTranslateStart.x + (var1.getX() - this.vCenterStart.x);
                     this.vTranslate.y = this.vTranslateStart.y + (var1.getY() - this.vCenterStart.y);
                     var11 = this.vTranslate.x;
                     var9 = this.vTranslate.y;
                     this.fitToBounds(true);
                     if (var11 != this.vTranslate.x) {
                        var19 = true;
                     } else {
                        var19 = false;
                     }

                     boolean var15;
                     if (var9 != this.vTranslate.y) {
                        var15 = true;
                     } else {
                        var15 = false;
                     }

                     boolean var16;
                     if (var19 && var10 > var4 && !this.isPanning) {
                        var16 = true;
                     } else {
                        var16 = false;
                     }

                     boolean var17;
                     if (var15 && var4 > var10 && !this.isPanning) {
                        var17 = true;
                     } else {
                        var17 = false;
                     }

                     boolean var18;
                     if (var9 == this.vTranslate.y && var4 > 3.0F * var8) {
                        var18 = true;
                     } else {
                        var18 = false;
                     }

                     if (var16 || var17 || var19 && var15 && !var18 && !this.isPanning) {
                        if (var14 > 0 || var4 > var8) {
                           this.maxTouchCount = 0;
                           this.handler.removeMessages(1);
                           this.requestDisallowInterceptTouchEvent(false);
                        }
                     } else {
                        this.isPanning = true;
                     }

                     if (!this.panEnabled) {
                        this.vTranslate.x = this.vTranslateStart.x;
                        this.vTranslate.y = this.vTranslateStart.y;
                        this.requestDisallowInterceptTouchEvent(false);
                     }

                     this.refreshRequiredTiles(false);
                  }

                  var19 = true;
                  break label258;
               }
            }

            var19 = false;
         }

         if (var19) {
            this.handler.removeMessages(1);
            this.invalidate();
            return true;
         }
      default:
         return false;
      }
   }

   private void preDraw() {
      if (this.getWidth() != 0 && this.getHeight() != 0 && this.sWidth > 0 && this.sHeight > 0) {
         if (this.sPendingCenter != null && this.pendingScale != null) {
            this.scale = this.pendingScale;
            if (this.vTranslate == null) {
               this.vTranslate = new PointF();
            }

            this.vTranslate.x = (float)(this.getWidth() / 2) - this.scale * this.sPendingCenter.x;
            this.vTranslate.y = (float)(this.getHeight() / 2) - this.scale * this.sPendingCenter.y;
            this.sPendingCenter = null;
            this.pendingScale = null;
            this.fitToBounds(true);
            this.refreshRequiredTiles(true);
         }

         this.fitToBounds(false);
      }
   }

   private void refreshRequiredTiles(boolean var1) {
      if (this.decoder != null && this.tileMap != null) {
         int var2 = Math.min(this.fullImageSampleSize, this.calculateInSampleSize(this.scale));
         Iterator var3 = this.tileMap.entrySet().iterator();

         while(var3.hasNext()) {
            Iterator var4 = ((List)((Entry)var3.next()).getValue()).iterator();

            while(var4.hasNext()) {
               SubsamplingScaleImageView.Tile var5 = (SubsamplingScaleImageView.Tile)var4.next();
               if (var5.sampleSize < var2 || var5.sampleSize > var2 && var5.sampleSize != this.fullImageSampleSize) {
                  var5.visible = false;
                  if (var5.bitmap != null) {
                     var5.bitmap.recycle();
                     var5.bitmap = null;
                  }
               }

               if (var5.sampleSize == var2) {
                  if (this.tileVisible(var5)) {
                     var5.visible = true;
                     if (!var5.loading && var5.bitmap == null && var1) {
                        this.execute(new SubsamplingScaleImageView.TileLoadTask(this, this.decoder, var5));
                     }
                  } else if (var5.sampleSize != this.fullImageSampleSize) {
                     var5.visible = false;
                     if (var5.bitmap != null) {
                        var5.bitmap.recycle();
                        var5.bitmap = null;
                     }
                  }
               } else if (var5.sampleSize == this.fullImageSampleSize) {
                  var5.visible = true;
               }
            }
         }

      }
   }

   private void requestDisallowInterceptTouchEvent(boolean var1) {
      ViewParent var2 = this.getParent();
      if (var2 != null) {
         var2.requestDisallowInterceptTouchEvent(var1);
      }

   }

   private void reset(boolean param1) {
      // $FF: Couldn't be decompiled
   }

   private void restoreState(ImageViewState var1) {
      if (var1 != null && var1.getCenter() != null && VALID_ORIENTATIONS.contains(var1.getOrientation())) {
         this.orientation = var1.getOrientation();
         this.pendingScale = var1.getScale();
         this.sPendingCenter = var1.getCenter();
         this.invalidate();
      }

   }

   private int sHeight() {
      int var1 = this.getRequiredRotation();
      return var1 != 90 && var1 != 270 ? this.sHeight : this.sWidth;
   }

   private int sWidth() {
      int var1 = this.getRequiredRotation();
      return var1 != 90 && var1 != 270 ? this.sWidth : this.sHeight;
   }

   private void sendStateChanged(float var1, PointF var2, int var3) {
      if (this.onStateChangedListener != null) {
         if (this.scale != var1) {
            this.onStateChangedListener.onScaleChanged(this.scale, var3);
         }

         if (!this.vTranslate.equals(var2)) {
            this.onStateChangedListener.onCenterChanged(this.getCenter(), var3);
         }
      }

   }

   private void setGestureDetector(final Context var1) {
      this.detector = new GestureDetector(var1, new SimpleOnGestureListener() {
         public boolean onDoubleTap(MotionEvent var1x) {
            if (SubsamplingScaleImageView.this.zoomEnabled && SubsamplingScaleImageView.this.readySent && SubsamplingScaleImageView.this.vTranslate != null) {
               SubsamplingScaleImageView.this.setGestureDetector(var1);
               if (SubsamplingScaleImageView.this.quickScaleEnabled) {
                  SubsamplingScaleImageView.this.vCenterStart = new PointF(var1x.getX(), var1x.getY());
                  SubsamplingScaleImageView.this.vTranslateStart = new PointF(SubsamplingScaleImageView.this.vTranslate.x, SubsamplingScaleImageView.this.vTranslate.y);
                  SubsamplingScaleImageView.this.scaleStart = SubsamplingScaleImageView.this.scale;
                  SubsamplingScaleImageView.this.isQuickScaling = true;
                  SubsamplingScaleImageView.this.isZooming = true;
                  SubsamplingScaleImageView.this.quickScaleLastDistance = -1.0F;
                  SubsamplingScaleImageView.this.quickScaleSCenter = SubsamplingScaleImageView.this.viewToSourceCoord(SubsamplingScaleImageView.this.vCenterStart);
                  SubsamplingScaleImageView.this.quickScaleVStart = new PointF(var1x.getX(), var1x.getY());
                  SubsamplingScaleImageView.this.quickScaleVLastPoint = new PointF(SubsamplingScaleImageView.this.quickScaleSCenter.x, SubsamplingScaleImageView.this.quickScaleSCenter.y);
                  SubsamplingScaleImageView.this.quickScaleMoved = false;
                  return false;
               } else {
                  SubsamplingScaleImageView.this.doubleTapZoom(SubsamplingScaleImageView.this.viewToSourceCoord(new PointF(var1x.getX(), var1x.getY())), new PointF(var1x.getX(), var1x.getY()));
                  return true;
               }
            } else {
               return super.onDoubleTapEvent(var1x);
            }
         }

         public boolean onFling(MotionEvent var1x, MotionEvent var2, float var3, float var4) {
            if (SubsamplingScaleImageView.this.panEnabled && SubsamplingScaleImageView.this.readySent && SubsamplingScaleImageView.this.vTranslate != null && var1x != null && var2 != null && (Math.abs(var1x.getX() - var2.getX()) > 50.0F || Math.abs(var1x.getY() - var2.getY()) > 50.0F) && (Math.abs(var3) > 500.0F || Math.abs(var4) > 500.0F) && !SubsamplingScaleImageView.this.isZooming) {
               PointF var5 = new PointF(SubsamplingScaleImageView.this.vTranslate.x + var3 * 0.25F, SubsamplingScaleImageView.this.vTranslate.y + var4 * 0.25F);
               var4 = ((float)(SubsamplingScaleImageView.this.getWidth() / 2) - var5.x) / SubsamplingScaleImageView.this.scale;
               var3 = ((float)(SubsamplingScaleImageView.this.getHeight() / 2) - var5.y) / SubsamplingScaleImageView.this.scale;
               (SubsamplingScaleImageView.this.new AnimationBuilder(new PointF(var4, var3))).withEasing(1).withPanLimited(false).withOrigin(3).start();
               return true;
            } else {
               return super.onFling(var1x, var2, var3, var4);
            }
         }

         public boolean onSingleTapConfirmed(MotionEvent var1x) {
            SubsamplingScaleImageView.this.performClick();
            return true;
         }
      });
   }

   private void setMatrixArray(float[] var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      var1[0] = var2;
      var1[1] = var3;
      var1[2] = var4;
      var1[3] = var5;
      var1[4] = var6;
      var1[5] = var7;
      var1[6] = var8;
      var1[7] = var9;
   }

   private Rect sourceToViewRect(Rect var1, Rect var2) {
      var2.set((int)this.sourceToViewX((float)var1.left), (int)this.sourceToViewY((float)var1.top), (int)this.sourceToViewX((float)var1.right), (int)this.sourceToViewY((float)var1.bottom));
      return var2;
   }

   private float sourceToViewX(float var1) {
      return this.vTranslate == null ? Float.NaN : var1 * this.scale + this.vTranslate.x;
   }

   private float sourceToViewY(float var1) {
      return this.vTranslate == null ? Float.NaN : var1 * this.scale + this.vTranslate.y;
   }

   private boolean tileVisible(SubsamplingScaleImageView.Tile var1) {
      float var2 = this.viewToSourceX(0.0F);
      float var3 = this.viewToSourceX((float)this.getWidth());
      float var4 = this.viewToSourceY(0.0F);
      float var5 = this.viewToSourceY((float)this.getHeight());
      boolean var6;
      if (var2 <= (float)var1.sRect.right && (float)var1.sRect.left <= var3 && var4 <= (float)var1.sRect.bottom && (float)var1.sRect.top <= var5) {
         var6 = true;
      } else {
         var6 = false;
      }

      return var6;
   }

   private PointF vTranslateForSCenter(float var1, float var2, float var3) {
      int var4 = this.getPaddingLeft();
      int var5 = (this.getWidth() - this.getPaddingRight() - this.getPaddingLeft()) / 2;
      int var6 = this.getPaddingTop();
      int var7 = (this.getHeight() - this.getPaddingBottom() - this.getPaddingTop()) / 2;
      if (this.satTemp == null) {
         this.satTemp = new SubsamplingScaleImageView.ScaleAndTranslate(0.0F, new PointF(0.0F, 0.0F));
      }

      this.satTemp.scale = var3;
      this.satTemp.vTranslate.set((float)(var4 + var5) - var1 * var3, (float)(var6 + var7) - var2 * var3);
      this.fitToBounds(true, this.satTemp);
      return this.satTemp.vTranslate;
   }

   private float viewToSourceX(float var1) {
      return this.vTranslate == null ? Float.NaN : (var1 - this.vTranslate.x) / this.scale;
   }

   private float viewToSourceY(float var1) {
      return this.vTranslate == null ? Float.NaN : (var1 - this.vTranslate.y) / this.scale;
   }

   public final int getAppliedOrientation() {
      return this.getRequiredRotation();
   }

   public final PointF getCenter() {
      int var1 = this.getWidth() / 2;
      int var2 = this.getHeight() / 2;
      return this.viewToSourceCoord((float)var1, (float)var2);
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
      return this.vTranslate != null && this.sWidth > 0 && this.sHeight > 0 ? new ImageViewState(this.getScale(), this.getCenter(), this.getOrientation()) : null;
   }

   public final boolean isImageLoaded() {
      return this.imageLoadedSent;
   }

   public final boolean isReady() {
      return this.readySent;
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      this.createPaints();
      if (this.sWidth != 0 && this.sHeight != 0 && this.getWidth() != 0 && this.getHeight() != 0) {
         if (this.tileMap == null && this.decoder != null) {
            this.initialiseBaseLayer(this.getMaxBitmapDimensions(var1));
         }

         if (this.checkReady()) {
            this.preDraw();
            float var2;
            float var6;
            PointF var8;
            if (this.anim != null) {
               var2 = this.scale;
               if (this.vTranslateBefore == null) {
                  this.vTranslateBefore = new PointF(0.0F, 0.0F);
               }

               this.vTranslateBefore.set(this.vTranslate);
               long var3 = System.currentTimeMillis() - this.anim.time;
               boolean var5;
               if (var3 > this.anim.duration) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               var3 = Math.min(var3, this.anim.duration);
               this.scale = this.ease(this.anim.easing, var3, this.anim.scaleStart, this.anim.scaleEnd - this.anim.scaleStart, this.anim.duration);
               var6 = this.ease(this.anim.easing, var3, this.anim.vFocusStart.x, this.anim.vFocusEnd.x - this.anim.vFocusStart.x, this.anim.duration);
               float var7 = this.ease(this.anim.easing, var3, this.anim.vFocusStart.y, this.anim.vFocusEnd.y - this.anim.vFocusStart.y, this.anim.duration);
               var8 = this.vTranslate;
               var8.x -= this.sourceToViewX(this.anim.sCenterEnd.x) - var6;
               var8 = this.vTranslate;
               var8.y -= this.sourceToViewY(this.anim.sCenterEnd.y) - var7;
               boolean var9;
               if (!var5 && this.anim.scaleStart != this.anim.scaleEnd) {
                  var9 = false;
               } else {
                  var9 = true;
               }

               this.fitToBounds(var9);
               this.sendStateChanged(var2, this.vTranslateBefore, this.anim.origin);
               this.refreshRequiredTiles(var5);
               if (var5) {
                  if (this.anim.listener != null) {
                     try {
                        this.anim.listener.onComplete();
                     } catch (Exception var16) {
                        Log.w(TAG, "Error thrown by animation listener", var16);
                     }
                  }

                  this.anim = null;
               }

               this.invalidate();
            }

            if (this.tileMap != null && this.isBaseLayerReady()) {
               int var10 = Math.min(this.fullImageSampleSize, this.calculateInSampleSize(this.scale));
               Iterator var18 = this.tileMap.entrySet().iterator();
               boolean var11 = false;

               label205:
               while(true) {
                  Entry var12;
                  do {
                     if (!var18.hasNext()) {
                        var18 = this.tileMap.entrySet().iterator();

                        while(true) {
                           do {
                              if (!var18.hasNext()) {
                                 break label205;
                              }

                              var12 = (Entry)var18.next();
                           } while((Integer)var12.getKey() != var10 && !var11);

                           Iterator var23 = ((List)var12.getValue()).iterator();

                           while(var23.hasNext()) {
                              SubsamplingScaleImageView.Tile var21 = (SubsamplingScaleImageView.Tile)var23.next();
                              this.sourceToViewRect(var21.sRect, var21.vRect);
                              if (!var21.loading && var21.bitmap != null) {
                                 if (this.tileBgPaint != null) {
                                    var1.drawRect(var21.vRect, this.tileBgPaint);
                                 }

                                 if (this.matrix == null) {
                                    this.matrix = new Matrix();
                                 }

                                 this.matrix.reset();
                                 this.setMatrixArray(this.srcArray, 0.0F, 0.0F, (float)var21.bitmap.getWidth(), 0.0F, (float)var21.bitmap.getWidth(), (float)var21.bitmap.getHeight(), 0.0F, (float)var21.bitmap.getHeight());
                                 if (this.getRequiredRotation() == 0) {
                                    this.setMatrixArray(this.dstArray, (float)var21.vRect.left, (float)var21.vRect.top, (float)var21.vRect.right, (float)var21.vRect.top, (float)var21.vRect.right, (float)var21.vRect.bottom, (float)var21.vRect.left, (float)var21.vRect.bottom);
                                 } else if (this.getRequiredRotation() == 90) {
                                    this.setMatrixArray(this.dstArray, (float)var21.vRect.right, (float)var21.vRect.top, (float)var21.vRect.right, (float)var21.vRect.bottom, (float)var21.vRect.left, (float)var21.vRect.bottom, (float)var21.vRect.left, (float)var21.vRect.top);
                                 } else if (this.getRequiredRotation() == 180) {
                                    this.setMatrixArray(this.dstArray, (float)var21.vRect.right, (float)var21.vRect.bottom, (float)var21.vRect.left, (float)var21.vRect.bottom, (float)var21.vRect.left, (float)var21.vRect.top, (float)var21.vRect.right, (float)var21.vRect.top);
                                 } else if (this.getRequiredRotation() == 270) {
                                    this.setMatrixArray(this.dstArray, (float)var21.vRect.left, (float)var21.vRect.bottom, (float)var21.vRect.left, (float)var21.vRect.top, (float)var21.vRect.right, (float)var21.vRect.top, (float)var21.vRect.right, (float)var21.vRect.bottom);
                                 }

                                 this.matrix.setPolyToPoly(this.srcArray, 0, this.dstArray, 0, 4);
                                 var1.drawBitmap(var21.bitmap, this.matrix, this.bitmapPaint);
                                 if (this.debug) {
                                    var1.drawRect(var21.vRect, this.debugPaint);
                                 }
                              } else if (var21.loading && this.debug) {
                                 var1.drawText("LOADING", (float)(var21.vRect.left + 5), (float)(var21.vRect.top + 35), this.debugPaint);
                              }

                              if (var21.visible && this.debug) {
                                 StringBuilder var15 = new StringBuilder();
                                 var15.append("ISS ");
                                 var15.append(var21.sampleSize);
                                 var15.append(" RECT ");
                                 var15.append(var21.sRect.top);
                                 var15.append(",");
                                 var15.append(var21.sRect.left);
                                 var15.append(",");
                                 var15.append(var21.sRect.bottom);
                                 var15.append(",");
                                 var15.append(var21.sRect.right);
                                 var1.drawText(var15.toString(), (float)(var21.vRect.left + 5), (float)(var21.vRect.top + 15), this.debugPaint);
                              }
                           }
                        }
                     }

                     var12 = (Entry)var18.next();
                  } while((Integer)var12.getKey() != var10);

                  Iterator var20 = ((List)var12.getValue()).iterator();
                  boolean var25 = var11;

                  while(true) {
                     SubsamplingScaleImageView.Tile var14;
                     do {
                        do {
                           var11 = var25;
                           if (!var20.hasNext()) {
                              continue label205;
                           }

                           var14 = (SubsamplingScaleImageView.Tile)var20.next();
                        } while(!var14.visible);
                     } while(!var14.loading && var14.bitmap != null);

                     var25 = true;
                  }
               }
            } else if (this.bitmap != null) {
               var6 = this.scale;
               var2 = this.scale;
               if (this.bitmapIsPreview) {
                  var6 = this.scale * ((float)this.sWidth / (float)this.bitmap.getWidth());
                  var2 = this.scale * ((float)this.sHeight / (float)this.bitmap.getHeight());
               }

               if (this.matrix == null) {
                  this.matrix = new Matrix();
               }

               this.matrix.reset();
               this.matrix.postScale(var6, var2);
               this.matrix.postRotate((float)this.getRequiredRotation());
               this.matrix.postTranslate(this.vTranslate.x, this.vTranslate.y);
               if (this.getRequiredRotation() == 180) {
                  this.matrix.postTranslate(this.scale * (float)this.sWidth, this.scale * (float)this.sHeight);
               } else if (this.getRequiredRotation() == 90) {
                  this.matrix.postTranslate(this.scale * (float)this.sHeight, 0.0F);
               } else if (this.getRequiredRotation() == 270) {
                  this.matrix.postTranslate(0.0F, this.scale * (float)this.sWidth);
               }

               if (this.tileBgPaint != null) {
                  if (this.sRect == null) {
                     this.sRect = new RectF();
                  }

                  RectF var17 = this.sRect;
                  int var13;
                  if (this.bitmapIsPreview) {
                     var13 = this.bitmap.getWidth();
                  } else {
                     var13 = this.sWidth;
                  }

                  var6 = (float)var13;
                  if (this.bitmapIsPreview) {
                     var13 = this.bitmap.getHeight();
                  } else {
                     var13 = this.sHeight;
                  }

                  var2 = (float)var13;
                  var17.set(0.0F, 0.0F, var6, var2);
                  this.matrix.mapRect(this.sRect);
                  var1.drawRect(this.sRect, this.tileBgPaint);
               }

               var1.drawBitmap(this.bitmap, this.matrix, this.bitmapPaint);
            }

            if (this.debug) {
               StringBuilder var19 = new StringBuilder();
               var19.append("Scale: ");
               var19.append(String.format(Locale.ENGLISH, "%.2f", this.scale));
               var1.drawText(var19.toString(), 5.0F, 15.0F, this.debugPaint);
               var19 = new StringBuilder();
               var19.append("Translate: ");
               var19.append(String.format(Locale.ENGLISH, "%.2f", this.vTranslate.x));
               var19.append(":");
               var19.append(String.format(Locale.ENGLISH, "%.2f", this.vTranslate.y));
               var1.drawText(var19.toString(), 5.0F, 35.0F, this.debugPaint);
               PointF var22 = this.getCenter();
               var19 = new StringBuilder();
               var19.append("Source center: ");
               var19.append(String.format(Locale.ENGLISH, "%.2f", var22.x));
               var19.append(":");
               var19.append(String.format(Locale.ENGLISH, "%.2f", var22.y));
               var1.drawText(var19.toString(), 5.0F, 55.0F, this.debugPaint);
               this.debugPaint.setStrokeWidth(2.0F);
               if (this.anim != null) {
                  var8 = this.sourceToViewCoord(this.anim.sCenterStart);
                  var22 = this.sourceToViewCoord(this.anim.sCenterEndRequested);
                  PointF var24 = this.sourceToViewCoord(this.anim.sCenterEnd);
                  var1.drawCircle(var8.x, var8.y, 10.0F, this.debugPaint);
                  this.debugPaint.setColor(-65536);
                  var1.drawCircle(var22.x, var22.y, 20.0F, this.debugPaint);
                  this.debugPaint.setColor(-16776961);
                  var1.drawCircle(var24.x, var24.y, 25.0F, this.debugPaint);
                  this.debugPaint.setColor(-16711681);
                  var1.drawCircle((float)(this.getWidth() / 2), (float)(this.getHeight() / 2), 30.0F, this.debugPaint);
               }

               if (this.vCenterStart != null) {
                  this.debugPaint.setColor(-65536);
                  var1.drawCircle(this.vCenterStart.x, this.vCenterStart.y, 20.0F, this.debugPaint);
               }

               if (this.quickScaleSCenter != null) {
                  this.debugPaint.setColor(-16776961);
                  var1.drawCircle(this.sourceToViewX(this.quickScaleSCenter.x), this.sourceToViewY(this.quickScaleSCenter.y), 35.0F, this.debugPaint);
               }

               if (this.quickScaleVStart != null) {
                  this.debugPaint.setColor(-16711681);
                  var1.drawCircle(this.quickScaleVStart.x, this.quickScaleVStart.y, 30.0F, this.debugPaint);
               }

               this.debugPaint.setColor(-65281);
               this.debugPaint.setStrokeWidth(1.0F);
            }

         }
      }
   }

   protected void onImageLoaded() {
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      int var4 = MeasureSpec.getMode(var2);
      int var5 = MeasureSpec.getSize(var1);
      int var6 = MeasureSpec.getSize(var2);
      boolean var7 = false;
      boolean var8;
      if (var3 != 1073741824) {
         var8 = true;
      } else {
         var8 = false;
      }

      if (var4 != 1073741824) {
         var7 = true;
      }

      var1 = var5;
      var2 = var6;
      if (this.sWidth > 0) {
         var1 = var5;
         var2 = var6;
         if (this.sHeight > 0) {
            if (var8 && var7) {
               var1 = this.sWidth();
               var2 = this.sHeight();
            } else if (var7) {
               var2 = (int)((double)this.sHeight() / (double)this.sWidth() * (double)var5);
               var1 = var5;
            } else {
               var1 = var5;
               var2 = var6;
               if (var8) {
                  var1 = (int)((double)this.sWidth() / (double)this.sHeight() * (double)var6);
                  var2 = var6;
               }
            }
         }
      }

      this.setMeasuredDimension(Math.max(var1, this.getSuggestedMinimumWidth()), Math.max(var2, this.getSuggestedMinimumHeight()));
   }

   protected void onReady() {
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      this.debug("onSizeChanged %dx%d -> %dx%d", var3, var4, var1, var2);
      PointF var5 = this.getCenter();
      if (this.readySent && var5 != null) {
         this.anim = null;
         this.pendingScale = this.scale;
         this.sPendingCenter = var5;
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      SubsamplingScaleImageView.Anim var2 = this.anim;
      boolean var3 = true;
      if (var2 != null && !this.anim.interruptible) {
         this.requestDisallowInterceptTouchEvent(true);
         return true;
      } else {
         if (this.anim != null && this.anim.listener != null) {
            try {
               this.anim.listener.onInterruptedByUser();
            } catch (Exception var7) {
               Log.w(TAG, "Error thrown by animation listener", var7);
            }
         }

         this.anim = null;
         if (this.vTranslate == null) {
            return true;
         } else if (!this.isQuickScaling && (this.detector == null || this.detector.onTouchEvent(var1))) {
            this.isZooming = false;
            this.isPanning = false;
            this.maxTouchCount = 0;
            return true;
         } else {
            if (this.vTranslateStart == null) {
               this.vTranslateStart = new PointF(0.0F, 0.0F);
            }

            if (this.vTranslateBefore == null) {
               this.vTranslateBefore = new PointF(0.0F, 0.0F);
            }

            if (this.vCenterStart == null) {
               this.vCenterStart = new PointF(0.0F, 0.0F);
            }

            float var4 = this.scale;
            this.vTranslateBefore.set(this.vTranslate);
            boolean var5 = this.onTouchEventInternal(var1);
            this.sendStateChanged(var4, this.vTranslateBefore, 2);
            boolean var6 = var3;
            if (!var5) {
               if (super.onTouchEvent(var1)) {
                  var6 = var3;
               } else {
                  var6 = false;
               }
            }

            return var6;
         }
      }
   }

   public final void setBitmapDecoderClass(Class var1) {
      if (var1 != null) {
         this.bitmapDecoderFactory = new CompatDecoderFactory(var1);
      } else {
         throw new IllegalArgumentException("Decoder class cannot be set to null");
      }
   }

   public final void setBitmapDecoderFactory(DecoderFactory var1) {
      if (var1 != null) {
         this.bitmapDecoderFactory = var1;
      } else {
         throw new IllegalArgumentException("Decoder factory cannot be set to null");
      }
   }

   public final void setDebug(boolean var1) {
      this.debug = var1;
   }

   public final void setDoubleTapZoomDpi(int var1) {
      DisplayMetrics var2 = this.getResources().getDisplayMetrics();
      this.setDoubleTapZoomScale((var2.xdpi + var2.ydpi) / 2.0F / (float)var1);
   }

   public final void setDoubleTapZoomDuration(int var1) {
      this.doubleTapZoomDuration = Math.max(0, var1);
   }

   public final void setDoubleTapZoomScale(float var1) {
      this.doubleTapZoomScale = var1;
   }

   public final void setDoubleTapZoomStyle(int var1) {
      if (VALID_ZOOM_STYLES.contains(var1)) {
         this.doubleTapZoomStyle = var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid zoom style: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public final void setImage(ImageSource var1) {
      this.setImage(var1, (ImageSource)null, (ImageViewState)null);
   }

   public final void setImage(ImageSource var1, ImageSource var2, ImageViewState var3) {
      if (var1 != null) {
         this.reset(true);
         if (var3 != null) {
            this.restoreState(var3);
         }

         if (var2 != null) {
            if (var1.getBitmap() != null) {
               throw new IllegalArgumentException("Preview image cannot be used when a bitmap is provided for the main image");
            }

            if (var1.getSWidth() <= 0 || var1.getSHeight() <= 0) {
               throw new IllegalArgumentException("Preview image cannot be used unless dimensions are provided for the main image");
            }

            this.sWidth = var1.getSWidth();
            this.sHeight = var1.getSHeight();
            this.pRegion = var2.getSRegion();
            if (var2.getBitmap() != null) {
               this.bitmapIsCached = var2.isCached();
               this.onPreviewLoaded(var2.getBitmap());
            } else {
               Uri var4 = var2.getUri();
               Uri var6 = var4;
               if (var4 == null) {
                  var6 = var4;
                  if (var2.getResource() != null) {
                     StringBuilder var7 = new StringBuilder();
                     var7.append("android.resource://");
                     var7.append(this.getContext().getPackageName());
                     var7.append("/");
                     var7.append(var2.getResource());
                     var6 = Uri.parse(var7.toString());
                  }
               }

               this.execute(new SubsamplingScaleImageView.BitmapLoadTask(this, this.getContext(), this.bitmapDecoderFactory, var6, true));
            }
         }

         if (var1.getBitmap() != null && var1.getSRegion() != null) {
            this.onImageLoaded(Bitmap.createBitmap(var1.getBitmap(), var1.getSRegion().left, var1.getSRegion().top, var1.getSRegion().width(), var1.getSRegion().height()), 0, false);
         } else if (var1.getBitmap() != null) {
            this.onImageLoaded(var1.getBitmap(), 0, var1.isCached());
         } else {
            this.sRegion = var1.getSRegion();
            this.uri = var1.getUri();
            if (this.uri == null && var1.getResource() != null) {
               StringBuilder var5 = new StringBuilder();
               var5.append("android.resource://");
               var5.append(this.getContext().getPackageName());
               var5.append("/");
               var5.append(var1.getResource());
               this.uri = Uri.parse(var5.toString());
            }

            if (!var1.getTile() && this.sRegion == null) {
               this.execute(new SubsamplingScaleImageView.BitmapLoadTask(this, this.getContext(), this.bitmapDecoderFactory, this.uri, false));
            } else {
               this.execute(new SubsamplingScaleImageView.TilesInitTask(this, this.getContext(), this.regionDecoderFactory, this.uri));
            }
         }

      } else {
         throw new NullPointerException("imageSource must not be null");
      }
   }

   public final void setImage(ImageSource var1, ImageViewState var2) {
      this.setImage(var1, (ImageSource)null, var2);
   }

   public final void setMaxScale(float var1) {
      this.maxScale = var1;
   }

   public void setMaxTileSize(int var1) {
      this.maxTileWidth = var1;
      this.maxTileHeight = var1;
   }

   public final void setMaximumDpi(int var1) {
      DisplayMetrics var2 = this.getResources().getDisplayMetrics();
      this.setMinScale((var2.xdpi + var2.ydpi) / 2.0F / (float)var1);
   }

   public final void setMinScale(float var1) {
      this.minScale = var1;
   }

   public final void setMinimumDpi(int var1) {
      DisplayMetrics var2 = this.getResources().getDisplayMetrics();
      this.setMaxScale((var2.xdpi + var2.ydpi) / 2.0F / (float)var1);
   }

   public final void setMinimumScaleType(int var1) {
      if (VALID_SCALE_TYPES.contains(var1)) {
         this.minimumScaleType = var1;
         if (this.isReady()) {
            this.fitToBounds(true);
            this.invalidate();
         }

      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid scale type: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public void setMinimumTileDpi(int var1) {
      DisplayMetrics var2 = this.getResources().getDisplayMetrics();
      this.minimumTileDpi = (int)Math.min((var2.xdpi + var2.ydpi) / 2.0F, (float)var1);
      if (this.isReady()) {
         this.reset(false);
         this.invalidate();
      }

   }

   public void setOnImageEventListener(SubsamplingScaleImageView.OnImageEventListener var1) {
      this.onImageEventListener = var1;
   }

   public void setOnLongClickListener(OnLongClickListener var1) {
      this.onLongClickListener = var1;
   }

   public void setOnStateChangedListener(SubsamplingScaleImageView.OnStateChangedListener var1) {
      this.onStateChangedListener = var1;
   }

   public final void setOrientation(int var1) {
      if (VALID_ORIENTATIONS.contains(var1)) {
         this.orientation = var1;
         this.reset(false);
         this.invalidate();
         this.requestLayout();
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid orientation: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public final void setPanEnabled(boolean var1) {
      this.panEnabled = var1;
      if (!var1 && this.vTranslate != null) {
         this.vTranslate.x = (float)(this.getWidth() / 2) - this.scale * (float)(this.sWidth() / 2);
         this.vTranslate.y = (float)(this.getHeight() / 2) - this.scale * (float)(this.sHeight() / 2);
         if (this.isReady()) {
            this.refreshRequiredTiles(true);
            this.invalidate();
         }
      }

   }

   public final void setPanLimit(int var1) {
      if (VALID_PAN_LIMITS.contains(var1)) {
         this.panLimit = var1;
         if (this.isReady()) {
            this.fitToBounds(true);
            this.invalidate();
         }

      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid pan limit: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public void setParallelLoadingEnabled(boolean var1) {
      this.parallelLoadingEnabled = var1;
   }

   public final void setQuickScaleEnabled(boolean var1) {
      this.quickScaleEnabled = var1;
   }

   public final void setRegionDecoderClass(Class var1) {
      if (var1 != null) {
         this.regionDecoderFactory = new CompatDecoderFactory(var1);
      } else {
         throw new IllegalArgumentException("Decoder class cannot be set to null");
      }
   }

   public final void setRegionDecoderFactory(DecoderFactory var1) {
      if (var1 != null) {
         this.regionDecoderFactory = var1;
      } else {
         throw new IllegalArgumentException("Decoder factory cannot be set to null");
      }
   }

   public final void setScaleAndCenter(float var1, PointF var2) {
      this.anim = null;
      this.pendingScale = var1;
      this.sPendingCenter = var2;
      this.sRequestedCenter = var2;
      this.invalidate();
   }

   public final void setTileBackgroundColor(int var1) {
      if (Color.alpha(var1) == 0) {
         this.tileBgPaint = null;
      } else {
         this.tileBgPaint = new Paint();
         this.tileBgPaint.setStyle(Style.FILL);
         this.tileBgPaint.setColor(var1);
      }

      this.invalidate();
   }

   public final void setZoomEnabled(boolean var1) {
      this.zoomEnabled = var1;
   }

   public final PointF sourceToViewCoord(float var1, float var2, PointF var3) {
      if (this.vTranslate == null) {
         return null;
      } else {
         var3.set(this.sourceToViewX(var1), this.sourceToViewY(var2));
         return var3;
      }
   }

   public final PointF sourceToViewCoord(PointF var1) {
      return this.sourceToViewCoord(var1.x, var1.y, new PointF());
   }

   public final PointF viewToSourceCoord(float var1, float var2) {
      return this.viewToSourceCoord(var1, var2, new PointF());
   }

   public final PointF viewToSourceCoord(float var1, float var2, PointF var3) {
      if (this.vTranslate == null) {
         return null;
      } else {
         var3.set(this.viewToSourceX(var1), this.viewToSourceY(var2));
         return var3;
      }
   }

   public final PointF viewToSourceCoord(PointF var1) {
      return this.viewToSourceCoord(var1.x, var1.y, new PointF());
   }

   private static class Anim {
      private long duration;
      private int easing;
      private boolean interruptible;
      private SubsamplingScaleImageView.OnAnimationEventListener listener;
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

      // $FF: synthetic method
      Anim(Object var1) {
         this();
      }
   }

   public final class AnimationBuilder {
      private long duration;
      private int easing;
      private boolean interruptible;
      private SubsamplingScaleImageView.OnAnimationEventListener listener;
      private int origin;
      private boolean panLimited;
      private final PointF targetSCenter;
      private final float targetScale;
      private final PointF vFocus;

      private AnimationBuilder(float var2, PointF var3) {
         this.duration = 500L;
         this.easing = 2;
         this.origin = 1;
         this.interruptible = true;
         this.panLimited = true;
         this.targetScale = var2;
         this.targetSCenter = var3;
         this.vFocus = null;
      }

      private AnimationBuilder(float var2, PointF var3, PointF var4) {
         this.duration = 500L;
         this.easing = 2;
         this.origin = 1;
         this.interruptible = true;
         this.panLimited = true;
         this.targetScale = var2;
         this.targetSCenter = var3;
         this.vFocus = var4;
      }

      // $FF: synthetic method
      AnimationBuilder(float var2, PointF var3, PointF var4, Object var5) {
         this(var2, var3, (PointF)var4);
      }

      // $FF: synthetic method
      AnimationBuilder(float var2, PointF var3, Object var4) {
         this(var2, var3);
      }

      private AnimationBuilder(PointF var2) {
         this.duration = 500L;
         this.easing = 2;
         this.origin = 1;
         this.interruptible = true;
         this.panLimited = true;
         this.targetScale = SubsamplingScaleImageView.this.scale;
         this.targetSCenter = var2;
         this.vFocus = null;
      }

      // $FF: synthetic method
      AnimationBuilder(PointF var2, Object var3) {
         this(var2);
      }

      private SubsamplingScaleImageView.AnimationBuilder withOrigin(int var1) {
         this.origin = var1;
         return this;
      }

      private SubsamplingScaleImageView.AnimationBuilder withPanLimited(boolean var1) {
         this.panLimited = var1;
         return this;
      }

      public void start() {
         if (SubsamplingScaleImageView.this.anim != null && SubsamplingScaleImageView.this.anim.listener != null) {
            try {
               SubsamplingScaleImageView.this.anim.listener.onInterruptedByNewAnim();
            } catch (Exception var9) {
               Log.w(SubsamplingScaleImageView.TAG, "Error thrown by animation listener", var9);
            }
         }

         int var2 = SubsamplingScaleImageView.this.getPaddingLeft();
         int var3 = (SubsamplingScaleImageView.this.getWidth() - SubsamplingScaleImageView.this.getPaddingRight() - SubsamplingScaleImageView.this.getPaddingLeft()) / 2;
         int var4 = SubsamplingScaleImageView.this.getPaddingTop();
         int var5 = (SubsamplingScaleImageView.this.getHeight() - SubsamplingScaleImageView.this.getPaddingBottom() - SubsamplingScaleImageView.this.getPaddingTop()) / 2;
         float var6 = SubsamplingScaleImageView.this.limitedScale(this.targetScale);
         PointF var1;
         if (this.panLimited) {
            var1 = SubsamplingScaleImageView.this.limitedSCenter(this.targetSCenter.x, this.targetSCenter.y, var6, new PointF());
         } else {
            var1 = this.targetSCenter;
         }

         SubsamplingScaleImageView.this.anim = new SubsamplingScaleImageView.Anim();
         SubsamplingScaleImageView.this.anim.scaleStart = SubsamplingScaleImageView.this.scale;
         SubsamplingScaleImageView.this.anim.scaleEnd = var6;
         SubsamplingScaleImageView.this.anim.time = System.currentTimeMillis();
         SubsamplingScaleImageView.this.anim.sCenterEndRequested = var1;
         SubsamplingScaleImageView.this.anim.sCenterStart = SubsamplingScaleImageView.this.getCenter();
         SubsamplingScaleImageView.this.anim.sCenterEnd = var1;
         SubsamplingScaleImageView.this.anim.vFocusStart = SubsamplingScaleImageView.this.sourceToViewCoord(var1);
         SubsamplingScaleImageView.this.anim.vFocusEnd = new PointF((float)(var2 + var3), (float)(var4 + var5));
         SubsamplingScaleImageView.this.anim.duration = this.duration;
         SubsamplingScaleImageView.this.anim.interruptible = this.interruptible;
         SubsamplingScaleImageView.this.anim.easing = this.easing;
         SubsamplingScaleImageView.this.anim.origin = this.origin;
         SubsamplingScaleImageView.this.anim.time = System.currentTimeMillis();
         SubsamplingScaleImageView.this.anim.listener = this.listener;
         if (this.vFocus != null) {
            float var7 = this.vFocus.x - SubsamplingScaleImageView.this.anim.sCenterStart.x * var6;
            float var8 = this.vFocus.y - SubsamplingScaleImageView.this.anim.sCenterStart.y * var6;
            SubsamplingScaleImageView.ScaleAndTranslate var10 = new SubsamplingScaleImageView.ScaleAndTranslate(var6, new PointF(var7, var8));
            SubsamplingScaleImageView.this.fitToBounds(true, var10);
            SubsamplingScaleImageView.this.anim.vFocusEnd = new PointF(this.vFocus.x + (var10.vTranslate.x - var7), this.vFocus.y + (var10.vTranslate.y - var8));
         }

         SubsamplingScaleImageView.this.invalidate();
      }

      public SubsamplingScaleImageView.AnimationBuilder withDuration(long var1) {
         this.duration = var1;
         return this;
      }

      public SubsamplingScaleImageView.AnimationBuilder withEasing(int var1) {
         if (SubsamplingScaleImageView.VALID_EASING_STYLES.contains(var1)) {
            this.easing = var1;
            return this;
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Unknown easing type: ");
            var2.append(var1);
            throw new IllegalArgumentException(var2.toString());
         }
      }

      public SubsamplingScaleImageView.AnimationBuilder withInterruptible(boolean var1) {
         this.interruptible = var1;
         return this;
      }
   }

   private static class BitmapLoadTask extends AsyncTask {
      private Bitmap bitmap;
      private final WeakReference contextRef;
      private final WeakReference decoderFactoryRef;
      private Exception exception;
      private final boolean preview;
      private final Uri source;
      private final WeakReference viewRef;

      BitmapLoadTask(SubsamplingScaleImageView var1, Context var2, DecoderFactory var3, Uri var4, boolean var5) {
         this.viewRef = new WeakReference(var1);
         this.contextRef = new WeakReference(var2);
         this.decoderFactoryRef = new WeakReference(var3);
         this.source = var4;
         this.preview = var5;
      }

      protected Integer doInBackground(Void... param1) {
         // $FF: Couldn't be decompiled
      }

      protected void onPostExecute(Integer var1) {
         SubsamplingScaleImageView var2 = (SubsamplingScaleImageView)this.viewRef.get();
         if (var2 != null) {
            if (this.bitmap != null && var1 != null) {
               if (this.preview) {
                  var2.onPreviewLoaded(this.bitmap);
               } else {
                  var2.onImageLoaded(this.bitmap, var1, false);
               }
            } else if (this.exception != null && var2.onImageEventListener != null) {
               if (this.preview) {
                  var2.onImageEventListener.onPreviewLoadError(this.exception);
               } else {
                  var2.onImageEventListener.onImageLoadError(this.exception);
               }
            }
         }

      }
   }

   public interface OnAnimationEventListener {
      void onComplete();

      void onInterruptedByNewAnim();

      void onInterruptedByUser();
   }

   public interface OnImageEventListener {
      void onImageLoadError(Exception var1);

      void onImageLoaded();

      void onPreviewLoadError(Exception var1);

      void onPreviewReleased();

      void onReady();

      void onTileLoadError(Exception var1);
   }

   public interface OnStateChangedListener {
      void onCenterChanged(PointF var1, int var2);

      void onScaleChanged(float var1, int var2);
   }

   private static class ScaleAndTranslate {
      private float scale;
      private PointF vTranslate;

      private ScaleAndTranslate(float var1, PointF var2) {
         this.scale = var1;
         this.vTranslate = var2;
      }

      // $FF: synthetic method
      ScaleAndTranslate(float var1, PointF var2, Object var3) {
         this(var1, var2);
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

      // $FF: synthetic method
      Tile(Object var1) {
         this();
      }

      // $FF: synthetic method
      static Rect access$5000(SubsamplingScaleImageView.Tile var0) {
         return var0.fileSRect;
      }
   }

   private static class TileLoadTask extends AsyncTask {
      private final WeakReference decoderRef;
      private Exception exception;
      private final WeakReference tileRef;
      private final WeakReference viewRef;

      TileLoadTask(SubsamplingScaleImageView var1, ImageRegionDecoder var2, SubsamplingScaleImageView.Tile var3) {
         this.viewRef = new WeakReference(var1);
         this.decoderRef = new WeakReference(var2);
         this.tileRef = new WeakReference(var3);
         var3.loading = true;
      }

      protected Bitmap doInBackground(Void... param1) {
         // $FF: Couldn't be decompiled
      }

      protected void onPostExecute(Bitmap var1) {
         SubsamplingScaleImageView var2 = (SubsamplingScaleImageView)this.viewRef.get();
         SubsamplingScaleImageView.Tile var3 = (SubsamplingScaleImageView.Tile)this.tileRef.get();
         if (var2 != null && var3 != null) {
            if (var1 != null) {
               var3.bitmap = var1;
               var3.loading = false;
               var2.onTileLoaded();
            } else if (this.exception != null && var2.onImageEventListener != null) {
               var2.onImageEventListener.onTileLoadError(this.exception);
            }
         }

      }
   }

   private static class TilesInitTask extends AsyncTask {
      private final WeakReference contextRef;
      private ImageRegionDecoder decoder;
      private final WeakReference decoderFactoryRef;
      private Exception exception;
      private final Uri source;
      private final WeakReference viewRef;

      TilesInitTask(SubsamplingScaleImageView var1, Context var2, DecoderFactory var3, Uri var4) {
         this.viewRef = new WeakReference(var1);
         this.contextRef = new WeakReference(var2);
         this.decoderFactoryRef = new WeakReference(var3);
         this.source = var4;
      }

      protected int[] doInBackground(Void... param1) {
         // $FF: Couldn't be decompiled
      }

      protected void onPostExecute(int[] var1) {
         SubsamplingScaleImageView var2 = (SubsamplingScaleImageView)this.viewRef.get();
         if (var2 != null) {
            if (this.decoder != null && var1 != null && var1.length == 3) {
               var2.onTilesInited(this.decoder, var1[0], var1[1], var1[2]);
            } else if (this.exception != null && var2.onImageEventListener != null) {
               var2.onImageEventListener.onImageLoadError(this.exception);
            }
         }

      }
   }
}

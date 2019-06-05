package org.mapsforge.android.maps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mapsforge.android.AndroidUtils;
import org.mapsforge.android.maps.inputhandling.MapMover;
import org.mapsforge.android.maps.inputhandling.TapEventListener;
import org.mapsforge.android.maps.inputhandling.TouchEventHandler;
import org.mapsforge.android.maps.inputhandling.ZoomAnimator;
import org.mapsforge.android.maps.mapgenerator.FileSystemTileCache;
import org.mapsforge.android.maps.mapgenerator.InMemoryTileCache;
import org.mapsforge.android.maps.mapgenerator.JobParameters;
import org.mapsforge.android.maps.mapgenerator.JobQueue;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.android.maps.mapgenerator.MapWorker;
import org.mapsforge.android.maps.mapgenerator.TileCache;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;
import org.mapsforge.android.maps.overlay.OverlayController;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.rendertheme.ExternalRenderTheme;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

public class MapView extends ViewGroup {
   public static final InternalRenderTheme DEFAULT_RENDER_THEME;
   private static final float DEFAULT_TEXT_SCALE = 1.0F;
   private static final int DEFAULT_TILE_CACHE_SIZE_FILE_SYSTEM = 100;
   private static final int DEFAULT_TILE_CACHE_SIZE_IN_MEMORY = 20;
   private DatabaseRenderer databaseRenderer;
   private DebugSettings debugSettings;
   private final TileCache fileSystemTileCache;
   private final FpsCounter fpsCounter;
   private final FrameBuffer frameBuffer;
   private final TileCache inMemoryTileCache;
   private JobParameters jobParameters;
   private final JobQueue jobQueue;
   private final MapDatabase mapDatabase;
   private File mapFile;
   private MapGenerator mapGenerator;
   private int mapGeneratorId;
   private final MapMover mapMover;
   private final MapScaleBar mapScaleBar;
   private final MapViewPosition mapViewPosition;
   private final MapWorker mapWorker;
   private final MapZoomControls mapZoomControls;
   private final OverlayController overlayController;
   private final List overlays;
   private final Projection projection;
   private File renderThemeFile;
   private TapEventListener tapListener;
   private final TouchEventHandler touchEventHandler;
   private final ZoomAnimator zoomAnimator;

   static {
      DEFAULT_RENDER_THEME = InternalRenderTheme.OSMARENDER;
   }

   public MapView(Context var1) {
      this(var1, (AttributeSet)null, new DatabaseRenderer());
   }

   public MapView(Context var1, AttributeSet var2) {
      this(var1, var2, new DatabaseRenderer());
   }

   private MapView(Context var1, AttributeSet var2, MapGenerator var3) {
      super(var1, var2);
      if (!(var1 instanceof MapActivity)) {
         throw new IllegalArgumentException("context is not an instance of MapActivity");
      } else {
         MapActivity var4 = (MapActivity)var1;
         this.setBackgroundColor(FrameBuffer.MAP_VIEW_BACKGROUND);
         this.setDescendantFocusability(393216);
         this.setWillNotDraw(false);
         this.debugSettings = new DebugSettings(false, false, false);
         this.fileSystemTileCache = new FileSystemTileCache(100, var4.getMapViewId());
         this.fpsCounter = new FpsCounter();
         this.frameBuffer = new FrameBuffer(this);
         this.inMemoryTileCache = new InMemoryTileCache(20);
         this.jobParameters = new JobParameters(DEFAULT_RENDER_THEME, 1.0F);
         this.jobQueue = new JobQueue(this);
         this.mapDatabase = new MapDatabase();
         this.mapViewPosition = new MapViewPosition(this);
         this.mapScaleBar = new MapScaleBar(this);
         this.mapZoomControls = new MapZoomControls(var1, this);
         this.overlays = Collections.synchronizedList(new ArrayList());
         this.projection = new MapViewProjection(this);
         this.touchEventHandler = new TouchEventHandler(var4, this);
         this.databaseRenderer = new DatabaseRenderer(this.mapDatabase);
         this.mapWorker = new MapWorker(this);
         this.mapWorker.start();
         this.mapMover = new MapMover(this);
         this.mapMover.start();
         this.zoomAnimator = new ZoomAnimator(this);
         this.zoomAnimator.start();
         this.overlayController = new OverlayController(this);
         this.overlayController.start();
         this.setMapGeneratorInternal(var3, -1);
         GeoPoint var6 = this.mapGenerator.getStartPoint();
         Byte var5 = this.mapGenerator.getStartZoomLevel();
         if (var3 instanceof DatabaseRenderer) {
            var6 = this.databaseRenderer.getStartPoint();
            var5 = this.databaseRenderer.getStartZoomLevel();
         }

         if (var6 != null) {
            this.mapViewPosition.setCenter(var6);
         }

         if (var5 != null) {
            this.mapViewPosition.setZoomLevel(var5);
         }

         var4.registerMapView(this);
      }
   }

   public MapView(Context var1, MapGenerator var2) {
      this(var1, (AttributeSet)null, var2);
   }

   private void setMapGeneratorInternal(MapGenerator var1, int var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("mapGenerator must not be null");
      } else {
         this.mapWorker.setOnline(true);
         if (var1 instanceof DatabaseRenderer) {
            this.databaseRenderer = new DatabaseRenderer(this.mapDatabase);
            this.mapWorker.setOnline(false);
         }

         this.mapGeneratorId = var2;
         this.mapGenerator = var1;
         this.mapWorker.setDatabaseRenderer(this.databaseRenderer);
         this.mapWorker.setMapGenerator(this.mapGenerator);
      }
   }

   void clearAndRedrawMapView() {
      this.jobQueue.clear();
      this.frameBuffer.clear();
      this.redraw();
   }

   void destroy() {
      this.overlayController.interrupt();
      this.mapMover.interrupt();
      this.mapWorker.interrupt();
      this.zoomAnimator.interrupt();

      try {
         this.mapWorker.join();
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
      }

      this.frameBuffer.destroy();
      this.mapScaleBar.destroy();
      this.inMemoryTileCache.destroy();
      this.fileSystemTileCache.destroy();
      this.databaseRenderer.destroy();
      this.mapDatabase.closeFile();
   }

   public DatabaseRenderer getDatabaseRenderer() {
      return this.databaseRenderer;
   }

   public DebugSettings getDebugSettings() {
      return this.debugSettings;
   }

   public TileCache getFileSystemTileCache() {
      return this.fileSystemTileCache;
   }

   public FpsCounter getFpsCounter() {
      return this.fpsCounter;
   }

   public FrameBuffer getFrameBuffer() {
      return this.frameBuffer;
   }

   public TileCache getInMemoryTileCache() {
      return this.inMemoryTileCache;
   }

   public JobQueue getJobQueue() {
      return this.jobQueue;
   }

   public MapDatabase getMapDatabase() {
      return this.mapDatabase;
   }

   public File getMapFile() {
      return this.mapFile;
   }

   public MapGenerator getMapGenerator() {
      return this.mapGenerator;
   }

   public MapMover getMapMover() {
      return this.mapMover;
   }

   public MapScaleBar getMapScaleBar() {
      return this.mapScaleBar;
   }

   public MapViewPosition getMapViewPosition() {
      return this.mapViewPosition;
   }

   public MapZoomControls getMapZoomControls() {
      return this.mapZoomControls;
   }

   public OverlayController getOverlayController() {
      return this.overlayController;
   }

   public List getOverlays() {
      return this.overlays;
   }

   public Projection getProjection() {
      return this.projection;
   }

   public File getRenderThemeFile() {
      return this.renderThemeFile;
   }

   public ZoomAnimator getZoomAnimator() {
      return this.zoomAnimator;
   }

   byte getZoomLevelMax() {
      return (byte)Math.min(this.mapZoomControls.getZoomLevelMax(), this.databaseRenderer.getZoomLevelMax());
   }

   public void invalidateOnUiThread() {
      if (AndroidUtils.currentThreadIsUiThread()) {
         this.invalidate();
      } else {
         this.postInvalidate();
      }

   }

   public boolean isZoomAnimatorRunning() {
      return this.zoomAnimator.isExecuting();
   }

   protected void onDraw(Canvas var1) {
      this.frameBuffer.draw(var1);
      this.overlayController.draw(var1);
      if (this.mapScaleBar.isShowMapScaleBar()) {
         this.mapScaleBar.draw(var1);
      }

      if (this.fpsCounter.isShowFpsCounter()) {
         this.fpsCounter.draw(var1);
      }

   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      return this.mapMover.onKeyDown(var1, var2);
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      return this.mapMover.onKeyUp(var1, var2);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.mapZoomControls.onLayout(var1, var2, var3, var4, var5);
   }

   protected final void onMeasure(int var1, int var2) {
      this.mapZoomControls.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var2), Integer.MIN_VALUE));
      this.setMeasuredDimension(Math.max(MeasureSpec.getSize(var1), this.mapZoomControls.getMeasuredWidth()), Math.max(MeasureSpec.getSize(var2), this.mapZoomControls.getMeasuredHeight()));
   }

   void onPause() {
      this.mapWorker.pause();
      this.mapMover.pause();
      this.zoomAnimator.pause();
   }

   void onResume() {
      this.mapWorker.proceed();
      this.mapMover.proceed();
      this.zoomAnimator.proceed();
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      synchronized(this){}

      Throwable var10000;
      label84: {
         boolean var10001;
         try {
            this.frameBuffer.destroy();
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label84;
         }

         if (var1 <= 0 || var2 <= 0) {
            return;
         }

         label75:
         try {
            this.frameBuffer.onSizeChanged();
            this.overlayController.onSizeChanged();
            this.redraw();
            return;
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label75;
         }
      }

      Throwable var5 = var10000;
      throw var5;
   }

   public void onTapEvent(GeoPoint var1) {
      if (this.tapListener != null) {
         try {
            this.tapListener.onTap(var1);
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = TouchEventHandler.getAction(var1);
      this.mapZoomControls.onMapViewTouchEvent(var2);
      boolean var3;
      if (!this.isClickable()) {
         var3 = true;
      } else {
         var3 = this.touchEventHandler.onTouchEvent(var1);
      }

      return var3;
   }

   public boolean onTrackballEvent(MotionEvent var1) {
      return this.mapMover.onTrackballEvent(var1);
   }

   public void redraw() {
      // $FF: Couldn't be decompiled
   }

   public void setBuiltInZoomControls(boolean var1) {
      this.mapZoomControls.setShowMapZoomControls(var1);
   }

   public void setDebugSettings(DebugSettings var1) {
      this.debugSettings = var1;
      this.clearAndRedrawMapView();
   }

   public FileOpenResult setMapFile(File var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("mapFile must not be null");
      } else {
         FileOpenResult var3;
         if (var1.equals(this.mapFile)) {
            var3 = FileOpenResult.SUCCESS;
         } else {
            this.zoomAnimator.pause();
            this.mapWorker.pause();
            this.mapMover.pause();
            this.zoomAnimator.awaitPausing();
            this.mapMover.awaitPausing();
            this.mapWorker.awaitPausing();
            this.mapMover.stopMove();
            this.jobQueue.clear();
            this.zoomAnimator.proceed();
            this.mapWorker.proceed();
            this.mapMover.proceed();
            this.mapDatabase.closeFile();
            FileOpenResult var2 = this.mapDatabase.openFile(var1);
            if (var2.isSuccess()) {
               this.mapFile = var1;
               GeoPoint var4 = this.databaseRenderer.getStartPoint();
               if (var4 != null) {
                  this.mapViewPosition.setCenter(var4);
               }

               Byte var5 = this.databaseRenderer.getStartZoomLevel();
               if (var5 != null) {
                  this.mapViewPosition.setZoomLevel(var5);
               }

               this.clearAndRedrawMapView();
               var3 = FileOpenResult.SUCCESS;
            } else {
               this.mapFile = null;
               this.clearAndRedrawMapView();
               var3 = var2;
            }
         }

         return var3;
      }
   }

   public void setMapGenerator(MapGenerator var1) {
      if (this.mapGenerator != var1) {
         this.setMapGeneratorInternal(var1, -1);
         this.clearAndRedrawMapView();
      }

   }

   public void setMapGenerator(MapGenerator var1, int var2) {
      if (this.mapGenerator != var1) {
         this.setMapGeneratorInternal(var1, var2);
         this.clearAndRedrawMapView();
      }

   }

   public void setOnTapListener(TapEventListener var1) {
      this.tapListener = var1;
   }

   public void setRenderTheme(File var1) throws FileNotFoundException {
      if (var1 == null) {
         throw new IllegalArgumentException("render theme file must not be null");
      } else {
         this.jobParameters = new JobParameters(new ExternalRenderTheme(var1), this.jobParameters.textScale);
         this.renderThemeFile = var1;
         this.clearAndRedrawMapView();
      }
   }

   public void setRenderTheme(InternalRenderTheme var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("render theme must not be null");
      } else {
         this.jobParameters = new JobParameters(var1, this.jobParameters.textScale);
         this.renderThemeFile = null;
         this.clearAndRedrawMapView();
      }
   }

   public void setTextScale(float var1) {
      this.jobParameters = new JobParameters(this.jobParameters.jobTheme, var1);
      this.clearAndRedrawMapView();
   }

   public boolean takeScreenshot(CompressFormat var1, int var2, File var3) throws IOException {
      FileOutputStream var5 = new FileOutputStream(var3);
      boolean var4 = this.frameBuffer.compress(var1, var2, var5);
      var5.close();
      return var4;
   }
}

package org.mapsforge.android.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
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
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.android.maps.mapgenerator.MapWorker;
import org.mapsforge.android.maps.mapgenerator.TileCache;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.android.maps.overlay.OverlayController;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.rendertheme.ExternalRenderTheme;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

public class MapView extends ViewGroup {
    public static final InternalRenderTheme DEFAULT_RENDER_THEME = InternalRenderTheme.OSMARENDER;
    private static final float DEFAULT_TEXT_SCALE = 1.0f;
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
    private final List<Overlay> overlays;
    private final Projection projection;
    private File renderThemeFile;
    private TapEventListener tapListener;
    private final TouchEventHandler touchEventHandler;
    private final ZoomAnimator zoomAnimator;

    public MapView(Context context) {
        this(context, null, new DatabaseRenderer());
    }

    public MapView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, new DatabaseRenderer());
    }

    public MapView(Context context, MapGenerator mapGenerator) {
        this(context, null, mapGenerator);
    }

    private MapView(Context context, AttributeSet attributeSet, MapGenerator mapGenerator) {
        super(context, attributeSet);
        if (context instanceof MapActivity) {
            MapActivity mapActivity = (MapActivity) context;
            setBackgroundColor(FrameBuffer.MAP_VIEW_BACKGROUND);
            setDescendantFocusability(393216);
            setWillNotDraw(false);
            this.debugSettings = new DebugSettings(false, false, false);
            this.fileSystemTileCache = new FileSystemTileCache(100, mapActivity.getMapViewId());
            this.fpsCounter = new FpsCounter();
            this.frameBuffer = new FrameBuffer(this);
            this.inMemoryTileCache = new InMemoryTileCache(20);
            this.jobParameters = new JobParameters(DEFAULT_RENDER_THEME, DEFAULT_TEXT_SCALE);
            this.jobQueue = new JobQueue(this);
            this.mapDatabase = new MapDatabase();
            this.mapViewPosition = new MapViewPosition(this);
            this.mapScaleBar = new MapScaleBar(this);
            this.mapZoomControls = new MapZoomControls(context, this);
            this.overlays = Collections.synchronizedList(new ArrayList());
            this.projection = new MapViewProjection(this);
            this.touchEventHandler = new TouchEventHandler(mapActivity, this);
            this.databaseRenderer = new DatabaseRenderer(this.mapDatabase);
            this.mapWorker = new MapWorker(this);
            this.mapWorker.start();
            this.mapMover = new MapMover(this);
            this.mapMover.start();
            this.zoomAnimator = new ZoomAnimator(this);
            this.zoomAnimator.start();
            this.overlayController = new OverlayController(this);
            this.overlayController.start();
            setMapGeneratorInternal(mapGenerator, -1);
            GeoPoint startPoint = this.mapGenerator.getStartPoint();
            Byte startZoomLevel = this.mapGenerator.getStartZoomLevel();
            if (mapGenerator instanceof DatabaseRenderer) {
                startPoint = this.databaseRenderer.getStartPoint();
                startZoomLevel = this.databaseRenderer.getStartZoomLevel();
            }
            if (startPoint != null) {
                this.mapViewPosition.setCenter(startPoint);
            }
            if (startZoomLevel != null) {
                this.mapViewPosition.setZoomLevel(startZoomLevel.byteValue());
            }
            mapActivity.registerMapView(this);
            return;
        }
        throw new IllegalArgumentException("context is not an instance of MapActivity");
    }

    public void setMapGenerator(MapGenerator mapGenerator) {
        if (this.mapGenerator != mapGenerator) {
            setMapGeneratorInternal(mapGenerator, -1);
            clearAndRedrawMapView();
        }
    }

    public void setMapGenerator(MapGenerator mapGenerator, int id) {
        if (this.mapGenerator != mapGenerator) {
            setMapGeneratorInternal(mapGenerator, id);
            clearAndRedrawMapView();
        }
    }

    private void setMapGeneratorInternal(MapGenerator mapGenerator, int id) {
        if (mapGenerator == null) {
            throw new IllegalArgumentException("mapGenerator must not be null");
        }
        this.mapWorker.setOnline(Boolean.valueOf(true));
        if (mapGenerator instanceof DatabaseRenderer) {
            this.databaseRenderer = new DatabaseRenderer(this.mapDatabase);
            this.mapWorker.setOnline(Boolean.valueOf(false));
        }
        this.mapGeneratorId = id;
        this.mapGenerator = mapGenerator;
        this.mapWorker.setDatabaseRenderer(this.databaseRenderer);
        this.mapWorker.setMapGenerator(this.mapGenerator);
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

    public MapGenerator getMapGenerator() {
        return this.mapGenerator;
    }

    public File getMapFile() {
        return this.mapFile;
    }

    public File getRenderThemeFile() {
        return this.renderThemeFile;
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

    public List<Overlay> getOverlays() {
        return this.overlays;
    }

    public Projection getProjection() {
        return this.projection;
    }

    public ZoomAnimator getZoomAnimator() {
        return this.zoomAnimator;
    }

    public void invalidateOnUiThread() {
        if (AndroidUtils.currentThreadIsUiThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public boolean isZoomAnimatorRunning() {
        return this.zoomAnimator.isExecuting();
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        return this.mapMover.onKeyDown(keyCode, keyEvent);
    }

    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        return this.mapMover.onKeyUp(keyCode, keyEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mapZoomControls.onMapViewTouchEvent(TouchEventHandler.getAction(motionEvent));
        if (isClickable()) {
            return this.touchEventHandler.onTouchEvent(motionEvent);
        }
        return true;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent) {
        return this.mapMover.onTrackballEvent(motionEvent);
    }

    public void setOnTapListener(TapEventListener l) {
        this.tapListener = l;
    }

    public void onTapEvent(GeoPoint p) {
        if (this.tapListener != null) {
            try {
                this.tapListener.onTap(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void redraw() {
        if (getWidth() > 0 && getHeight() > 0 && !isZoomAnimatorRunning()) {
            MapPosition mapPosition = this.mapViewPosition.getMapPosition();
            if (this.mapGenerator.requiresInternetConnection() || this.mapFile != null) {
                GeoPoint geoPoint = mapPosition.geoPoint;
                double pixelLeft = MercatorProjection.longitudeToPixelX(geoPoint.longitude, mapPosition.zoomLevel);
                pixelLeft -= (double) (getWidth() >> 1);
                double pixelTop = MercatorProjection.latitudeToPixelY(geoPoint.latitude, mapPosition.zoomLevel) - ((double) (getHeight() >> 1));
                long tileLeft = MercatorProjection.pixelXToTileX(pixelLeft, mapPosition.zoomLevel);
                long tileTop = MercatorProjection.pixelYToTileY(pixelTop, mapPosition.zoomLevel);
                long tileRight = MercatorProjection.pixelXToTileX(((double) getWidth()) + pixelLeft, mapPosition.zoomLevel);
                long tileBottom = MercatorProjection.pixelYToTileY(((double) getHeight()) + pixelTop, mapPosition.zoomLevel);
                for (long tileY = tileTop; tileY <= tileBottom; tileY++) {
                    for (long tileX = tileLeft; tileX <= tileRight; tileX++) {
                        MapGeneratorJob mapGeneratorJob = new MapGeneratorJob(new Tile(tileX, tileY, mapPosition.zoomLevel), this.mapGenerator, this.mapGeneratorId, this.mapFile, this.jobParameters, this.debugSettings);
                        if (this.inMemoryTileCache.containsKey(mapGeneratorJob)) {
                            this.frameBuffer.drawBitmap(mapGeneratorJob.tile, this.inMemoryTileCache.get(mapGeneratorJob));
                        } else if (this.fileSystemTileCache.containsKey(mapGeneratorJob)) {
                            Bitmap bitmap = this.fileSystemTileCache.get(mapGeneratorJob);
                            if (bitmap != null) {
                                this.frameBuffer.drawBitmap(mapGeneratorJob.tile, bitmap);
                                this.inMemoryTileCache.put(mapGeneratorJob, bitmap);
                            } else {
                                this.jobQueue.addJob(mapGeneratorJob);
                            }
                        } else {
                            this.jobQueue.addJob(mapGeneratorJob);
                        }
                    }
                }
                this.jobQueue.requestSchedule();
                synchronized (this.mapWorker) {
                    this.mapWorker.notify();
                }
            }
            this.overlayController.redrawOverlays();
            if (this.mapScaleBar.isShowMapScaleBar()) {
                this.mapScaleBar.redrawScaleBar();
            }
            invalidateOnUiThread();
        }
    }

    public void setBuiltInZoomControls(boolean showZoomControls) {
        this.mapZoomControls.setShowMapZoomControls(showZoomControls);
    }

    public void setDebugSettings(DebugSettings debugSettings) {
        this.debugSettings = debugSettings;
        clearAndRedrawMapView();
    }

    public FileOpenResult setMapFile(File mapFile) {
        if (mapFile == null) {
            throw new IllegalArgumentException("mapFile must not be null");
        } else if (mapFile.equals(this.mapFile)) {
            return FileOpenResult.SUCCESS;
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
            FileOpenResult fileOpenResult = this.mapDatabase.openFile(mapFile);
            if (fileOpenResult.isSuccess()) {
                this.mapFile = mapFile;
                GeoPoint startPoint = this.databaseRenderer.getStartPoint();
                if (startPoint != null) {
                    this.mapViewPosition.setCenter(startPoint);
                }
                Byte startZoomLevel = this.databaseRenderer.getStartZoomLevel();
                if (startZoomLevel != null) {
                    this.mapViewPosition.setZoomLevel(startZoomLevel.byteValue());
                }
                clearAndRedrawMapView();
                return FileOpenResult.SUCCESS;
            }
            this.mapFile = null;
            clearAndRedrawMapView();
            return fileOpenResult;
        }
    }

    public void setRenderTheme(File renderThemeFile) throws FileNotFoundException {
        if (renderThemeFile == null) {
            throw new IllegalArgumentException("render theme file must not be null");
        }
        this.jobParameters = new JobParameters(new ExternalRenderTheme(renderThemeFile), this.jobParameters.textScale);
        this.renderThemeFile = renderThemeFile;
        clearAndRedrawMapView();
    }

    public void setRenderTheme(InternalRenderTheme internalRenderTheme) {
        if (internalRenderTheme == null) {
            throw new IllegalArgumentException("render theme must not be null");
        }
        this.jobParameters = new JobParameters(internalRenderTheme, this.jobParameters.textScale);
        this.renderThemeFile = null;
        clearAndRedrawMapView();
    }

    public void setTextScale(float textScale) {
        this.jobParameters = new JobParameters(this.jobParameters.jobTheme, textScale);
        clearAndRedrawMapView();
    }

    public boolean takeScreenshot(CompressFormat compressFormat, int quality, File outputFile) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        boolean success = this.frameBuffer.compress(compressFormat, quality, outputStream);
        outputStream.close();
        return success;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        this.frameBuffer.draw(canvas);
        this.overlayController.draw(canvas);
        if (this.mapScaleBar.isShowMapScaleBar()) {
            this.mapScaleBar.draw(canvas);
        }
        if (this.fpsCounter.isShowFpsCounter()) {
            this.fpsCounter.draw(canvas);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mapZoomControls.onLayout(changed, left, top, right, bottom);
    }

    /* Access modifiers changed, original: protected|final */
    public final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mapZoomControls.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), Integer.MIN_VALUE));
        setMeasuredDimension(Math.max(MeasureSpec.getSize(widthMeasureSpec), this.mapZoomControls.getMeasuredWidth()), Math.max(MeasureSpec.getSize(heightMeasureSpec), this.mapZoomControls.getMeasuredHeight()));
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        this.frameBuffer.destroy();
        if (width > 0 && height > 0) {
            this.frameBuffer.onSizeChanged();
            this.overlayController.onSizeChanged();
            redraw();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void clearAndRedrawMapView() {
        this.jobQueue.clear();
        this.frameBuffer.clear();
        redraw();
    }

    /* Access modifiers changed, original: 0000 */
    public void destroy() {
        this.overlayController.interrupt();
        this.mapMover.interrupt();
        this.mapWorker.interrupt();
        this.zoomAnimator.interrupt();
        try {
            this.mapWorker.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.frameBuffer.destroy();
        this.mapScaleBar.destroy();
        this.inMemoryTileCache.destroy();
        this.fileSystemTileCache.destroy();
        this.databaseRenderer.destroy();
        this.mapDatabase.closeFile();
    }

    /* Access modifiers changed, original: 0000 */
    public byte getZoomLevelMax() {
        return (byte) Math.min(this.mapZoomControls.getZoomLevelMax(), this.databaseRenderer.getZoomLevelMax());
    }

    /* Access modifiers changed, original: 0000 */
    public void onPause() {
        this.mapWorker.pause();
        this.mapMover.pause();
        this.zoomAnimator.pause();
    }

    /* Access modifiers changed, original: 0000 */
    public void onResume() {
        this.mapWorker.proceed();
        this.mapMover.proceed();
        this.zoomAnimator.proceed();
    }
}

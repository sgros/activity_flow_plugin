// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.FileNotFoundException;
import org.mapsforge.map.rendertheme.ExternalRenderTheme;
import org.mapsforge.map.reader.header.FileOpenResult;
import android.graphics.Bitmap;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import android.view.KeyEvent;
import android.graphics.Canvas;
import org.mapsforge.android.AndroidUtils;
import org.mapsforge.core.model.GeoPoint;
import java.util.Collections;
import java.util.ArrayList;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.android.maps.mapgenerator.InMemoryTileCache;
import org.mapsforge.android.maps.mapgenerator.FileSystemTileCache;
import android.util.AttributeSet;
import android.content.Context;
import org.mapsforge.android.maps.inputhandling.ZoomAnimator;
import org.mapsforge.android.maps.inputhandling.TouchEventHandler;
import org.mapsforge.android.maps.inputhandling.TapEventListener;
import org.mapsforge.android.maps.overlay.Overlay;
import java.util.List;
import org.mapsforge.android.maps.overlay.OverlayController;
import org.mapsforge.android.maps.mapgenerator.MapWorker;
import org.mapsforge.android.maps.inputhandling.MapMover;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import java.io.File;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.android.maps.mapgenerator.JobQueue;
import org.mapsforge.android.maps.mapgenerator.JobParameters;
import org.mapsforge.android.maps.mapgenerator.TileCache;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import android.view.ViewGroup;

public class MapView extends ViewGroup
{
    public static final InternalRenderTheme DEFAULT_RENDER_THEME;
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
    
    static {
        DEFAULT_RENDER_THEME = InternalRenderTheme.OSMARENDER;
    }
    
    public MapView(final Context context) {
        this(context, null, new DatabaseRenderer());
    }
    
    public MapView(final Context context, final AttributeSet set) {
        this(context, set, new DatabaseRenderer());
    }
    
    private MapView(final Context context, final AttributeSet set, final MapGenerator mapGenerator) {
        super(context, set);
        if (!(context instanceof MapActivity)) {
            throw new IllegalArgumentException("context is not an instance of MapActivity");
        }
        final MapActivity mapActivity = (MapActivity)context;
        this.setBackgroundColor(FrameBuffer.MAP_VIEW_BACKGROUND);
        this.setDescendantFocusability(393216);
        this.setWillNotDraw(false);
        this.debugSettings = new DebugSettings(false, false, false);
        this.fileSystemTileCache = new FileSystemTileCache(100, mapActivity.getMapViewId());
        this.fpsCounter = new FpsCounter();
        this.frameBuffer = new FrameBuffer(this);
        this.inMemoryTileCache = new InMemoryTileCache(20);
        this.jobParameters = new JobParameters(MapView.DEFAULT_RENDER_THEME, 1.0f);
        this.jobQueue = new JobQueue(this);
        this.mapDatabase = new MapDatabase();
        this.mapViewPosition = new MapViewPosition(this);
        this.mapScaleBar = new MapScaleBar(this);
        this.mapZoomControls = new MapZoomControls(context, this);
        this.overlays = Collections.synchronizedList(new ArrayList<Overlay>());
        this.projection = new MapViewProjection(this);
        this.touchEventHandler = new TouchEventHandler((Context)mapActivity, this);
        this.databaseRenderer = new DatabaseRenderer(this.mapDatabase);
        (this.mapWorker = new MapWorker(this)).start();
        (this.mapMover = new MapMover(this)).start();
        (this.zoomAnimator = new ZoomAnimator(this)).start();
        (this.overlayController = new OverlayController(this)).start();
        this.setMapGeneratorInternal(mapGenerator, -1);
        GeoPoint center = this.mapGenerator.getStartPoint();
        Byte b = this.mapGenerator.getStartZoomLevel();
        if (mapGenerator instanceof DatabaseRenderer) {
            center = this.databaseRenderer.getStartPoint();
            b = this.databaseRenderer.getStartZoomLevel();
        }
        if (center != null) {
            this.mapViewPosition.setCenter(center);
        }
        if (b != null) {
            this.mapViewPosition.setZoomLevel(b);
        }
        mapActivity.registerMapView(this);
    }
    
    public MapView(final Context context, final MapGenerator mapGenerator) {
        this(context, null, mapGenerator);
    }
    
    private void setMapGeneratorInternal(final MapGenerator mapGenerator, final int mapGeneratorId) {
        if (mapGenerator == null) {
            throw new IllegalArgumentException("mapGenerator must not be null");
        }
        this.mapWorker.setOnline(true);
        if (mapGenerator instanceof DatabaseRenderer) {
            this.databaseRenderer = new DatabaseRenderer(this.mapDatabase);
            this.mapWorker.setOnline(false);
        }
        this.mapGeneratorId = mapGeneratorId;
        this.mapGenerator = mapGenerator;
        this.mapWorker.setDatabaseRenderer(this.databaseRenderer);
        this.mapWorker.setMapGenerator(this.mapGenerator);
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
        while (true) {
            try {
                this.mapWorker.join();
                this.frameBuffer.destroy();
                this.mapScaleBar.destroy();
                this.inMemoryTileCache.destroy();
                this.fileSystemTileCache.destroy();
                this.databaseRenderer.destroy();
                this.mapDatabase.closeFile();
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                continue;
            }
            break;
        }
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
    
    public List<Overlay> getOverlays() {
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
        }
        else {
            this.postInvalidate();
        }
    }
    
    public boolean isZoomAnimatorRunning() {
        return this.zoomAnimator.isExecuting();
    }
    
    protected void onDraw(final Canvas canvas) {
        this.frameBuffer.draw(canvas);
        this.overlayController.draw(canvas);
        if (this.mapScaleBar.isShowMapScaleBar()) {
            this.mapScaleBar.draw(canvas);
        }
        if (this.fpsCounter.isShowFpsCounter()) {
            this.fpsCounter.draw(canvas);
        }
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return this.mapMover.onKeyDown(n, keyEvent);
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return this.mapMover.onKeyUp(n, keyEvent);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        this.mapZoomControls.onLayout(b, n, n2, n3, n4);
    }
    
    protected final void onMeasure(final int n, final int n2) {
        this.mapZoomControls.measure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n2), Integer.MIN_VALUE));
        this.setMeasuredDimension(Math.max(View$MeasureSpec.getSize(n), this.mapZoomControls.getMeasuredWidth()), Math.max(View$MeasureSpec.getSize(n2), this.mapZoomControls.getMeasuredHeight()));
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
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        synchronized (this) {
            this.frameBuffer.destroy();
            if (n > 0 && n2 > 0) {
                this.frameBuffer.onSizeChanged();
                this.overlayController.onSizeChanged();
                this.redraw();
            }
        }
    }
    
    public void onTapEvent(final GeoPoint geoPoint) {
        if (this.tapListener == null) {
            return;
        }
        try {
            this.tapListener.onTap(geoPoint);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.mapZoomControls.onMapViewTouchEvent(TouchEventHandler.getAction(motionEvent));
        return !this.isClickable() || this.touchEventHandler.onTouchEvent(motionEvent);
    }
    
    public boolean onTrackballEvent(final MotionEvent motionEvent) {
        return this.mapMover.onTrackballEvent(motionEvent);
    }
    
    public void redraw() {
        if (this.getWidth() > 0 && this.getHeight() > 0 && !this.isZoomAnimatorRunning()) {
            final MapPosition mapPosition = this.mapViewPosition.getMapPosition();
            Label_0379: {
                if (!this.mapGenerator.requiresInternetConnection() && this.mapFile == null) {
                    break Label_0379;
                }
                final GeoPoint geoPoint = mapPosition.geoPoint;
                final double longitudeToPixelX = MercatorProjection.longitudeToPixelX(geoPoint.longitude, mapPosition.zoomLevel);
                final double latitudeToPixelY = MercatorProjection.latitudeToPixelY(geoPoint.latitude, mapPosition.zoomLevel);
                final double n = longitudeToPixelX - (this.getWidth() >> 1);
                final double n2 = latitudeToPixelY - (this.getHeight() >> 1);
                final long pixelXToTileX = MercatorProjection.pixelXToTileX(n, mapPosition.zoomLevel);
                long pixelYToTileY = MercatorProjection.pixelYToTileY(n2, mapPosition.zoomLevel);
                final long pixelXToTileX2 = MercatorProjection.pixelXToTileX(this.getWidth() + n, mapPosition.zoomLevel);
                while (pixelYToTileY <= MercatorProjection.pixelYToTileY(this.getHeight() + n2, mapPosition.zoomLevel)) {
                    for (long n3 = pixelXToTileX; n3 <= pixelXToTileX2; ++n3) {
                        final MapGeneratorJob mapGeneratorJob = new MapGeneratorJob(new Tile(n3, pixelYToTileY, mapPosition.zoomLevel), this.mapGenerator, this.mapGeneratorId, this.mapFile, this.jobParameters, this.debugSettings);
                        if (this.inMemoryTileCache.containsKey(mapGeneratorJob)) {
                            this.frameBuffer.drawBitmap(mapGeneratorJob.tile, this.inMemoryTileCache.get(mapGeneratorJob));
                        }
                        else if (this.fileSystemTileCache.containsKey(mapGeneratorJob)) {
                            final Bitmap value = this.fileSystemTileCache.get(mapGeneratorJob);
                            if (value != null) {
                                this.frameBuffer.drawBitmap(mapGeneratorJob.tile, value);
                                this.inMemoryTileCache.put(mapGeneratorJob, value);
                            }
                            else {
                                this.jobQueue.addJob(mapGeneratorJob);
                            }
                        }
                        else {
                            this.jobQueue.addJob(mapGeneratorJob);
                        }
                    }
                    ++pixelYToTileY;
                }
                this.jobQueue.requestSchedule();
                synchronized (this.mapWorker) {
                    this.mapWorker.notify();
                    // monitorexit(this.mapWorker)
                    this.overlayController.redrawOverlays();
                    if (this.mapScaleBar.isShowMapScaleBar()) {
                        this.mapScaleBar.redrawScaleBar();
                    }
                    this.invalidateOnUiThread();
                }
            }
        }
    }
    
    public void setBuiltInZoomControls(final boolean showMapZoomControls) {
        this.mapZoomControls.setShowMapZoomControls(showMapZoomControls);
    }
    
    public void setDebugSettings(final DebugSettings debugSettings) {
        this.debugSettings = debugSettings;
        this.clearAndRedrawMapView();
    }
    
    public FileOpenResult setMapFile(final File mapFile) {
        if (mapFile == null) {
            throw new IllegalArgumentException("mapFile must not be null");
        }
        FileOpenResult fileOpenResult;
        if (mapFile.equals(this.mapFile)) {
            fileOpenResult = FileOpenResult.SUCCESS;
        }
        else {
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
            final FileOpenResult openFile = this.mapDatabase.openFile(mapFile);
            if (openFile.isSuccess()) {
                this.mapFile = mapFile;
                final GeoPoint startPoint = this.databaseRenderer.getStartPoint();
                if (startPoint != null) {
                    this.mapViewPosition.setCenter(startPoint);
                }
                final Byte startZoomLevel = this.databaseRenderer.getStartZoomLevel();
                if (startZoomLevel != null) {
                    this.mapViewPosition.setZoomLevel(startZoomLevel);
                }
                this.clearAndRedrawMapView();
                fileOpenResult = FileOpenResult.SUCCESS;
            }
            else {
                this.mapFile = null;
                this.clearAndRedrawMapView();
                fileOpenResult = openFile;
            }
        }
        return fileOpenResult;
    }
    
    public void setMapGenerator(final MapGenerator mapGenerator) {
        if (this.mapGenerator != mapGenerator) {
            this.setMapGeneratorInternal(mapGenerator, -1);
            this.clearAndRedrawMapView();
        }
    }
    
    public void setMapGenerator(final MapGenerator mapGenerator, final int n) {
        if (this.mapGenerator != mapGenerator) {
            this.setMapGeneratorInternal(mapGenerator, n);
            this.clearAndRedrawMapView();
        }
    }
    
    public void setOnTapListener(final TapEventListener tapListener) {
        this.tapListener = tapListener;
    }
    
    public void setRenderTheme(final File renderThemeFile) throws FileNotFoundException {
        if (renderThemeFile == null) {
            throw new IllegalArgumentException("render theme file must not be null");
        }
        this.jobParameters = new JobParameters(new ExternalRenderTheme(renderThemeFile), this.jobParameters.textScale);
        this.renderThemeFile = renderThemeFile;
        this.clearAndRedrawMapView();
    }
    
    public void setRenderTheme(final InternalRenderTheme internalRenderTheme) {
        if (internalRenderTheme == null) {
            throw new IllegalArgumentException("render theme must not be null");
        }
        this.jobParameters = new JobParameters(internalRenderTheme, this.jobParameters.textScale);
        this.renderThemeFile = null;
        this.clearAndRedrawMapView();
    }
    
    public void setTextScale(final float n) {
        this.jobParameters = new JobParameters(this.jobParameters.jobTheme, n);
        this.clearAndRedrawMapView();
    }
    
    public boolean takeScreenshot(final Bitmap$CompressFormat bitmap$CompressFormat, final int n, final File file) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        final boolean compress = this.frameBuffer.compress(bitmap$CompressFormat, n, fileOutputStream);
        fileOutputStream.close();
        return compress;
    }
}

package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileContainer;
import org.osmdroid.util.MapTileIndex;

public class MapTileProviderArray extends MapTileProviderBase implements MapTileContainer {
    private IRegisterReceiver mRegisterReceiver;
    protected final List<MapTileModuleProviderBase> mTileProviderList;
    private final Map<Long, Integer> mWorking;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:26:0x0037 in {6, 15, 17, 21, 25} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public void detach() {
        /*
        r3 = this;
        r0 = r3.mTileProviderList;
        monitor-enter(r0);
        r1 = r3.mTileProviderList;	 Catch:{ all -> 0x0034 }
        r1 = r1.iterator();	 Catch:{ all -> 0x0034 }
        r2 = r1.hasNext();	 Catch:{ all -> 0x0034 }
        if (r2 == 0) goto L_0x0019;	 Catch:{ all -> 0x0034 }
        r2 = r1.next();	 Catch:{ all -> 0x0034 }
        r2 = (org.osmdroid.tileprovider.modules.MapTileModuleProviderBase) r2;	 Catch:{ all -> 0x0034 }
        r2.detach();	 Catch:{ all -> 0x0034 }
        goto L_0x0009;	 Catch:{ all -> 0x0034 }
        monitor-exit(r0);	 Catch:{ all -> 0x0034 }
        r1 = r3.mWorking;
        monitor-enter(r1);
        r0 = r3.mWorking;	 Catch:{ all -> 0x0031 }
        r0.clear();	 Catch:{ all -> 0x0031 }
        monitor-exit(r1);	 Catch:{ all -> 0x0031 }
        r0 = r3.mRegisterReceiver;
        if (r0 == 0) goto L_0x002d;
        r0.destroy();
        r0 = 0;
        r3.mRegisterReceiver = r0;
        super.detach();
        return;
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0031 }
        throw r0;
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0034 }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.MapTileProviderArray.detach():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:14:0x0026 in {8, 10, 13} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public int getMaximumZoomLevel() {
        /*
        r5 = this;
        r0 = r5.mTileProviderList;
        monitor-enter(r0);
        r1 = r5.mTileProviderList;	 Catch:{ all -> 0x0023 }
        r1 = r1.iterator();	 Catch:{ all -> 0x0023 }
        r2 = 0;	 Catch:{ all -> 0x0023 }
        r3 = r1.hasNext();	 Catch:{ all -> 0x0023 }
        if (r3 == 0) goto L_0x0021;	 Catch:{ all -> 0x0023 }
        r3 = r1.next();	 Catch:{ all -> 0x0023 }
        r3 = (org.osmdroid.tileprovider.modules.MapTileModuleProviderBase) r3;	 Catch:{ all -> 0x0023 }
        r4 = r3.getMaximumZoomLevel();	 Catch:{ all -> 0x0023 }
        if (r4 <= r2) goto L_0x000a;	 Catch:{ all -> 0x0023 }
        r2 = r3.getMaximumZoomLevel();	 Catch:{ all -> 0x0023 }
        goto L_0x000a;	 Catch:{ all -> 0x0023 }
        monitor-exit(r0);	 Catch:{ all -> 0x0023 }
        return r2;	 Catch:{ all -> 0x0023 }
        r1 = move-exception;	 Catch:{ all -> 0x0023 }
        monitor-exit(r0);	 Catch:{ all -> 0x0023 }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.MapTileProviderArray.getMaximumZoomLevel():int");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:14:0x0029 in {8, 10, 13} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public int getMinimumZoomLevel() {
        /*
        r5 = this;
        r0 = org.osmdroid.util.TileSystem.getMaximumZoomLevel();
        r1 = r5.mTileProviderList;
        monitor-enter(r1);
        r2 = r5.mTileProviderList;	 Catch:{ all -> 0x0026 }
        r2 = r2.iterator();	 Catch:{ all -> 0x0026 }
        r3 = r2.hasNext();	 Catch:{ all -> 0x0026 }
        if (r3 == 0) goto L_0x0024;	 Catch:{ all -> 0x0026 }
        r3 = r2.next();	 Catch:{ all -> 0x0026 }
        r3 = (org.osmdroid.tileprovider.modules.MapTileModuleProviderBase) r3;	 Catch:{ all -> 0x0026 }
        r4 = r3.getMinimumZoomLevel();	 Catch:{ all -> 0x0026 }
        if (r4 >= r0) goto L_0x000d;	 Catch:{ all -> 0x0026 }
        r0 = r3.getMinimumZoomLevel();	 Catch:{ all -> 0x0026 }
        goto L_0x000d;	 Catch:{ all -> 0x0026 }
        monitor-exit(r1);	 Catch:{ all -> 0x0026 }
        return r0;	 Catch:{ all -> 0x0026 }
        r0 = move-exception;	 Catch:{ all -> 0x0026 }
        monitor-exit(r1);	 Catch:{ all -> 0x0026 }
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.MapTileProviderArray.getMinimumZoomLevel():int");
    }

    /* Access modifiers changed, original: protected */
    public boolean isDowngradedMode(long j) {
        return false;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:12:0x0024 in {6, 8, 11} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public void setTileSource(org.osmdroid.tileprovider.tilesource.ITileSource r4) {
        /*
        r3 = this;
        super.setTileSource(r4);
        r0 = r3.mTileProviderList;
        monitor-enter(r0);
        r1 = r3.mTileProviderList;	 Catch:{ all -> 0x0021 }
        r1 = r1.iterator();	 Catch:{ all -> 0x0021 }
        r2 = r1.hasNext();	 Catch:{ all -> 0x0021 }
        if (r2 == 0) goto L_0x001f;	 Catch:{ all -> 0x0021 }
        r2 = r1.next();	 Catch:{ all -> 0x0021 }
        r2 = (org.osmdroid.tileprovider.modules.MapTileModuleProviderBase) r2;	 Catch:{ all -> 0x0021 }
        r2.setTileSource(r4);	 Catch:{ all -> 0x0021 }
        r3.clearTileCache();	 Catch:{ all -> 0x0021 }
        goto L_0x000c;	 Catch:{ all -> 0x0021 }
        monitor-exit(r0);	 Catch:{ all -> 0x0021 }
        return;	 Catch:{ all -> 0x0021 }
        r4 = move-exception;	 Catch:{ all -> 0x0021 }
        monitor-exit(r0);	 Catch:{ all -> 0x0021 }
        throw r4;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.MapTileProviderArray.setTileSource(org.osmdroid.tileprovider.tilesource.ITileSource):void");
    }

    protected MapTileProviderArray(ITileSource iTileSource, IRegisterReceiver iRegisterReceiver) {
        this(iTileSource, iRegisterReceiver, new MapTileModuleProviderBase[0]);
    }

    public MapTileProviderArray(ITileSource iTileSource, IRegisterReceiver iRegisterReceiver, MapTileModuleProviderBase[] mapTileModuleProviderBaseArr) {
        super(iTileSource);
        this.mWorking = new HashMap();
        this.mRegisterReceiver = null;
        this.mRegisterReceiver = iRegisterReceiver;
        this.mTileProviderList = new ArrayList();
        Collections.addAll(this.mTileProviderList, mapTileModuleProviderBaseArr);
    }

    public boolean contains(long j) {
        boolean containsKey;
        synchronized (this.mWorking) {
            containsKey = this.mWorking.containsKey(Long.valueOf(j));
        }
        return containsKey;
    }

    public Drawable getMapTile(long j) {
        Drawable mapTile = this.mTileCache.getMapTile(j);
        if (mapTile != null && (ExpirableBitmapDrawable.getState(mapTile) == -1 || isDowngradedMode(j))) {
            return mapTile;
        }
        synchronized (this.mWorking) {
            if (this.mWorking.containsKey(Long.valueOf(j))) {
                return mapTile;
            }
            this.mWorking.put(Long.valueOf(j), Integer.valueOf(0));
            runAsyncNextProvider(new MapTileRequestState(j, this.mTileProviderList, this));
            return mapTile;
        }
    }

    private void remove(long j) {
        synchronized (this.mWorking) {
            this.mWorking.remove(Long.valueOf(j));
        }
    }

    public void mapTileRequestCompleted(MapTileRequestState mapTileRequestState, Drawable drawable) {
        super.mapTileRequestCompleted(mapTileRequestState, drawable);
        remove(mapTileRequestState.getMapTile());
    }

    public void mapTileRequestFailed(MapTileRequestState mapTileRequestState) {
        runAsyncNextProvider(mapTileRequestState);
    }

    public void mapTileRequestFailedExceedsMaxQueueSize(MapTileRequestState mapTileRequestState) {
        super.mapTileRequestFailed(mapTileRequestState);
        remove(mapTileRequestState.getMapTile());
    }

    public void mapTileRequestExpiredTile(MapTileRequestState mapTileRequestState, Drawable drawable) {
        super.mapTileRequestExpiredTile(mapTileRequestState, drawable);
        synchronized (this.mWorking) {
            this.mWorking.put(Long.valueOf(mapTileRequestState.getMapTile()), Integer.valueOf(1));
        }
        runAsyncNextProvider(mapTileRequestState);
    }

    /* Access modifiers changed, original: protected */
    public MapTileModuleProviderBase findNextAppropriateProvider(MapTileRequestState mapTileRequestState) {
        MapTileModuleProviderBase nextProvider;
        int i = 0;
        Object obj = null;
        int i2 = 0;
        while (true) {
            nextProvider = mapTileRequestState.getNextProvider();
            if (nextProvider != null) {
                int i3 = 1;
                i = getProviderExists(nextProvider) ^ 1;
                Object obj2 = (useDataConnection() || !nextProvider.getUsesDataConnection()) ? null : 1;
                int zoom = MapTileIndex.getZoom(mapTileRequestState.getMapTile());
                if (zoom <= nextProvider.getMaximumZoomLevel() && zoom >= nextProvider.getMinimumZoomLevel()) {
                    i3 = 0;
                }
                Object obj3 = obj2;
                i2 = i3;
                obj = obj3;
            }
            if (nextProvider == null || (i == 0 && obj == null && i2 == 0)) {
                return nextProvider;
            }
        }
        return nextProvider;
    }

    private void runAsyncNextProvider(MapTileRequestState mapTileRequestState) {
        MapTileModuleProviderBase findNextAppropriateProvider = findNextAppropriateProvider(mapTileRequestState);
        if (findNextAppropriateProvider != null) {
            findNextAppropriateProvider.loadMapTileAsync(mapTileRequestState);
            return;
        }
        Integer num;
        synchronized (this.mWorking) {
            num = (Integer) this.mWorking.get(Long.valueOf(mapTileRequestState.getMapTile()));
        }
        if (num != null && num.intValue() == 0) {
            super.mapTileRequestFailed(mapTileRequestState);
        }
        remove(mapTileRequestState.getMapTile());
    }

    public boolean getProviderExists(MapTileModuleProviderBase mapTileModuleProviderBase) {
        return this.mTileProviderList.contains(mapTileModuleProviderBase);
    }
}

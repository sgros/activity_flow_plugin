package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.osmdroid.tileprovider.modules.CantContinueException;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.GarbageCollector;
import org.osmdroid.util.MapTileAreaList;

public class MapTilePreCache {
    private final MapTileCache mCache;
    private final GarbageCollector mGC = new GarbageCollector(new C02521());
    private final List<MapTileModuleProviderBase> mProviders = new ArrayList();
    private final MapTileAreaList mTileAreas = new MapTileAreaList();
    private Iterator<Long> mTileIndices;

    /* renamed from: org.osmdroid.tileprovider.MapTilePreCache$1 */
    class C02521 implements Runnable {
        C02521() {
        }

        public void run() {
            while (true) {
                long access$000 = MapTilePreCache.this.next();
                if (access$000 != -1) {
                    MapTilePreCache.this.search(access$000);
                } else {
                    return;
                }
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:17:0x0028 in {7, 12, 16} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private long next() {
        /*
        r3 = this;
        r0 = r3.mTileAreas;
        monitor-enter(r0);
        r1 = r3.mTileIndices;	 Catch:{ all -> 0x0025 }
        r1 = r1.hasNext();	 Catch:{ all -> 0x0025 }
        if (r1 != 0) goto L_0x000f;	 Catch:{ all -> 0x0025 }
        r1 = -1;	 Catch:{ all -> 0x0025 }
        monitor-exit(r0);	 Catch:{ all -> 0x0025 }
        return r1;	 Catch:{ all -> 0x0025 }
        r1 = r3.mTileIndices;	 Catch:{ all -> 0x0025 }
        r1 = r1.next();	 Catch:{ all -> 0x0025 }
        r1 = (java.lang.Long) r1;	 Catch:{ all -> 0x0025 }
        r1 = r1.longValue();	 Catch:{ all -> 0x0025 }
        monitor-exit(r0);	 Catch:{ all -> 0x0025 }
        r0 = r3.mCache;
        r0 = r0.getMapTile(r1);
        if (r0 != 0) goto L_0x0000;
        return r1;
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0025 }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.MapTilePreCache.next():long");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:21:0x007a in {9, 10, 11, 14, 17, 20} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private void refresh() {
        /*
        r6 = this;
        r0 = r6.mTileAreas;
        monitor-enter(r0);
        r1 = 0;
        r2 = r6.mCache;	 Catch:{ all -> 0x0077 }
        r2 = r2.getAdditionalMapTileList();	 Catch:{ all -> 0x0077 }
        r2 = r2.getList();	 Catch:{ all -> 0x0077 }
        r2 = r2.iterator();	 Catch:{ all -> 0x0077 }
        r3 = r2.hasNext();	 Catch:{ all -> 0x0077 }
        if (r3 == 0) goto L_0x004b;	 Catch:{ all -> 0x0077 }
        r3 = r2.next();	 Catch:{ all -> 0x0077 }
        r3 = (org.osmdroid.util.MapTileArea) r3;	 Catch:{ all -> 0x0077 }
        r4 = r6.mTileAreas;	 Catch:{ all -> 0x0077 }
        r4 = r4.getList();	 Catch:{ all -> 0x0077 }
        r4 = r4.size();	 Catch:{ all -> 0x0077 }
        if (r1 >= r4) goto L_0x0037;	 Catch:{ all -> 0x0077 }
        r4 = r6.mTileAreas;	 Catch:{ all -> 0x0077 }
        r4 = r4.getList();	 Catch:{ all -> 0x0077 }
        r4 = r4.get(r1);	 Catch:{ all -> 0x0077 }
        r4 = (org.osmdroid.util.MapTileArea) r4;	 Catch:{ all -> 0x0077 }
        goto L_0x0045;	 Catch:{ all -> 0x0077 }
        r4 = new org.osmdroid.util.MapTileArea;	 Catch:{ all -> 0x0077 }
        r4.<init>();	 Catch:{ all -> 0x0077 }
        r5 = r6.mTileAreas;	 Catch:{ all -> 0x0077 }
        r5 = r5.getList();	 Catch:{ all -> 0x0077 }
        r5.add(r4);	 Catch:{ all -> 0x0077 }
        r4.set(r3);	 Catch:{ all -> 0x0077 }
        r1 = r1 + 1;	 Catch:{ all -> 0x0077 }
        goto L_0x0012;	 Catch:{ all -> 0x0077 }
        r2 = r6.mTileAreas;	 Catch:{ all -> 0x0077 }
        r2 = r2.getList();	 Catch:{ all -> 0x0077 }
        r2 = r2.size();	 Catch:{ all -> 0x0077 }
        if (r1 >= r2) goto L_0x006d;	 Catch:{ all -> 0x0077 }
        r2 = r6.mTileAreas;	 Catch:{ all -> 0x0077 }
        r2 = r2.getList();	 Catch:{ all -> 0x0077 }
        r3 = r6.mTileAreas;	 Catch:{ all -> 0x0077 }
        r3 = r3.getList();	 Catch:{ all -> 0x0077 }
        r3 = r3.size();	 Catch:{ all -> 0x0077 }
        r3 = r3 + -1;	 Catch:{ all -> 0x0077 }
        r2.remove(r3);	 Catch:{ all -> 0x0077 }
        goto L_0x004b;	 Catch:{ all -> 0x0077 }
        r1 = r6.mTileAreas;	 Catch:{ all -> 0x0077 }
        r1 = r1.iterator();	 Catch:{ all -> 0x0077 }
        r6.mTileIndices = r1;	 Catch:{ all -> 0x0077 }
        monitor-exit(r0);	 Catch:{ all -> 0x0077 }
        return;	 Catch:{ all -> 0x0077 }
        r1 = move-exception;	 Catch:{ all -> 0x0077 }
        monitor-exit(r0);	 Catch:{ all -> 0x0077 }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.MapTilePreCache.refresh():void");
    }

    public MapTilePreCache(MapTileCache mapTileCache) {
        this.mCache = mapTileCache;
    }

    public void addProvider(MapTileModuleProviderBase mapTileModuleProviderBase) {
        this.mProviders.add(mapTileModuleProviderBase);
    }

    public void fill() {
        if (!this.mGC.isRunning()) {
            refresh();
            this.mGC.mo4115gc();
        }
    }

    private void search(long j) {
        for (MapTileModuleProviderBase mapTileModuleProviderBase : this.mProviders) {
            try {
                if (mapTileModuleProviderBase instanceof MapTileDownloader) {
                    ITileSource tileSource = ((MapTileDownloader) mapTileModuleProviderBase).getTileSource();
                    if ((tileSource instanceof OnlineTileSourceBase) && !((OnlineTileSourceBase) tileSource).getTileSourcePolicy().acceptsPreventive()) {
                    }
                }
                Drawable loadTile = mapTileModuleProviderBase.getTileLoader().loadTile(j);
                if (loadTile != null) {
                    this.mCache.putTile(j, loadTile);
                    return;
                }
            } catch (CantContinueException unused) {
            }
        }
    }
}

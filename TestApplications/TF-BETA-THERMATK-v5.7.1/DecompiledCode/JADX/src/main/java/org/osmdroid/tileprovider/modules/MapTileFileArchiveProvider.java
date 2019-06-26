package org.osmdroid.tileprovider.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.TileSystem;

public class MapTileFileArchiveProvider extends MapTileFileStorageProviderBase {
    private final boolean ignoreTileSource;
    private final ArrayList<IArchiveFile> mArchiveFiles;
    private final boolean mSpecificArchivesProvided;
    private final AtomicReference<ITileSource> mTileSource;

    protected class TileLoader extends org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.TileLoader {
        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:32:0x0080 in {2, 6, 12, 14, 16, 17, 18, 20, 22, 26, 27, 28, 30, 31} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        public android.graphics.drawable.Drawable loadTile(long r7) {
            /*
            r6 = this;
            r0 = "OsmDroid";
            r1 = org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider.this;
            r1 = r1.mTileSource;
            r1 = r1.get();
            r1 = (org.osmdroid.tileprovider.tilesource.ITileSource) r1;
            r2 = 0;
            if (r1 != 0) goto L_0x0012;
            return r2;
            r3 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            r3 = r3.isDebugMode();	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            if (r3 == 0) goto L_0x0034;	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            r3.<init>();	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            r4 = "Archives - Tile doesn't exist: ";	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            r3.append(r4);	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            r4 = org.osmdroid.util.MapTileIndex.toString(r7);	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            r3.append(r4);	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            r3 = r3.toString();	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            android.util.Log.d(r0, r3);	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            r3 = org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider.this;	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            r3 = r3.getInputStream(r7, r1);	 Catch:{ Throwable -> 0x006e, all -> 0x006b }
            if (r3 == 0) goto L_0x0065;
            r4 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ Throwable -> 0x0063 }
            r4 = r4.isDebugMode();	 Catch:{ Throwable -> 0x0063 }
            if (r4 == 0) goto L_0x005e;	 Catch:{ Throwable -> 0x0063 }
            r4 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0063 }
            r4.<init>();	 Catch:{ Throwable -> 0x0063 }
            r5 = "Use tile from archive: ";	 Catch:{ Throwable -> 0x0063 }
            r4.append(r5);	 Catch:{ Throwable -> 0x0063 }
            r7 = org.osmdroid.util.MapTileIndex.toString(r7);	 Catch:{ Throwable -> 0x0063 }
            r4.append(r7);	 Catch:{ Throwable -> 0x0063 }
            r7 = r4.toString();	 Catch:{ Throwable -> 0x0063 }
            android.util.Log.d(r0, r7);	 Catch:{ Throwable -> 0x0063 }
            r2 = r1.getDrawable(r3);	 Catch:{ Throwable -> 0x0063 }
            goto L_0x0065;
            r7 = move-exception;
            goto L_0x0070;
            if (r3 == 0) goto L_0x0078;
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3);
            goto L_0x0078;
            r7 = move-exception;
            r3 = r2;
            goto L_0x007a;
            r7 = move-exception;
            r3 = r2;
            r8 = "Error loading tile";	 Catch:{ all -> 0x0079 }
            android.util.Log.e(r0, r8, r7);	 Catch:{ all -> 0x0079 }
            if (r3 == 0) goto L_0x0078;
            goto L_0x0067;
            return r2;
            r7 = move-exception;
            if (r3 == 0) goto L_0x007f;
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3);
            throw r7;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider$TileLoader.loadTile(long):android.graphics.drawable.Drawable");
        }

        protected TileLoader() {
            super();
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:20:0x004f in {11, 13, 16, 19} preds:[]
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
    private synchronized java.io.InputStream getInputStream(long r5, org.osmdroid.tileprovider.tilesource.ITileSource r7) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.mArchiveFiles;	 Catch:{ all -> 0x004c }
        r0 = r0.iterator();	 Catch:{ all -> 0x004c }
        r1 = r0.hasNext();	 Catch:{ all -> 0x004c }
        if (r1 == 0) goto L_0x0049;	 Catch:{ all -> 0x004c }
        r1 = r0.next();	 Catch:{ all -> 0x004c }
        r1 = (org.osmdroid.tileprovider.modules.IArchiveFile) r1;	 Catch:{ all -> 0x004c }
        if (r1 == 0) goto L_0x0007;	 Catch:{ all -> 0x004c }
        r2 = r1.getInputStream(r7, r5);	 Catch:{ all -> 0x004c }
        if (r2 == 0) goto L_0x0007;	 Catch:{ all -> 0x004c }
        r7 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ all -> 0x004c }
        r7 = r7.isDebugMode();	 Catch:{ all -> 0x004c }
        if (r7 == 0) goto L_0x0047;	 Catch:{ all -> 0x004c }
        r7 = "OsmDroid";	 Catch:{ all -> 0x004c }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004c }
        r0.<init>();	 Catch:{ all -> 0x004c }
        r3 = "Found tile ";	 Catch:{ all -> 0x004c }
        r0.append(r3);	 Catch:{ all -> 0x004c }
        r5 = org.osmdroid.util.MapTileIndex.toString(r5);	 Catch:{ all -> 0x004c }
        r0.append(r5);	 Catch:{ all -> 0x004c }
        r5 = " in ";	 Catch:{ all -> 0x004c }
        r0.append(r5);	 Catch:{ all -> 0x004c }
        r0.append(r1);	 Catch:{ all -> 0x004c }
        r5 = r0.toString();	 Catch:{ all -> 0x004c }
        android.util.Log.d(r7, r5);	 Catch:{ all -> 0x004c }
        monitor-exit(r4);
        return r2;
        r5 = 0;
        monitor-exit(r4);
        return r5;
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider.getInputStream(long, org.osmdroid.tileprovider.tilesource.ITileSource):java.io.InputStream");
    }

    /* Access modifiers changed, original: protected */
    public String getName() {
        return "File Archive Provider";
    }

    /* Access modifiers changed, original: protected */
    public String getThreadGroupName() {
        return "filearchive";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    public MapTileFileArchiveProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, IArchiveFile[] iArchiveFileArr) {
        this(iRegisterReceiver, iTileSource, iArchiveFileArr, false);
    }

    public MapTileFileArchiveProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, IArchiveFile[] iArchiveFileArr, boolean z) {
        super(iRegisterReceiver, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
        this.mArchiveFiles = new ArrayList();
        this.mTileSource = new AtomicReference();
        this.ignoreTileSource = z;
        setTileSource(iTileSource);
        if (iArchiveFileArr == null) {
            this.mSpecificArchivesProvided = false;
            findArchiveFiles();
            return;
        }
        this.mSpecificArchivesProvided = true;
        for (int length = iArchiveFileArr.length - 1; length >= 0; length--) {
            this.mArchiveFiles.add(iArchiveFileArr[length]);
        }
    }

    public MapTileFileArchiveProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource) {
        this(iRegisterReceiver, iTileSource, null);
    }

    public TileLoader getTileLoader() {
        return new TileLoader();
    }

    public int getMinimumZoomLevel() {
        ITileSource iTileSource = (ITileSource) this.mTileSource.get();
        return iTileSource != null ? iTileSource.getMinimumZoomLevel() : 0;
    }

    public int getMaximumZoomLevel() {
        ITileSource iTileSource = (ITileSource) this.mTileSource.get();
        if (iTileSource != null) {
            return iTileSource.getMaximumZoomLevel();
        }
        return TileSystem.getMaximumZoomLevel();
    }

    /* Access modifiers changed, original: protected */
    public void onMediaMounted() {
        if (!this.mSpecificArchivesProvided) {
            findArchiveFiles();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMediaUnmounted() {
        if (!this.mSpecificArchivesProvided) {
            findArchiveFiles();
        }
    }

    public void setTileSource(ITileSource iTileSource) {
        this.mTileSource.set(iTileSource);
    }

    public void detach() {
        clearArcives();
        super.detach();
    }

    private void clearArcives() {
        while (!this.mArchiveFiles.isEmpty()) {
            IArchiveFile iArchiveFile = (IArchiveFile) this.mArchiveFiles.get(0);
            if (iArchiveFile != null) {
                iArchiveFile.close();
            }
            this.mArchiveFiles.remove(0);
        }
    }

    private void findArchiveFiles() {
        clearArcives();
        File[] listFiles = Configuration.getInstance().getOsmdroidBasePath().listFiles();
        if (listFiles != null) {
            for (File archiveFile : listFiles) {
                IArchiveFile archiveFile2 = ArchiveFileFactory.getArchiveFile(archiveFile);
                if (archiveFile2 != null) {
                    archiveFile2.setIgnoreTileSource(this.ignoreTileSource);
                    this.mArchiveFiles.add(archiveFile2);
                }
            }
        }
    }
}

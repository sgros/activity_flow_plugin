package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class TileWriter implements IFilesystemCache {
    static boolean hasInited = false;
    private static long mUsedCacheSpace;
    Thread initThread = null;
    private long mMaximumCachedFileAge;

    /* renamed from: org.osmdroid.tileprovider.modules.TileWriter$1 */
    class C02571 extends Thread {
        C02571() {
        }

        public void run() {
            TileWriter.mUsedCacheSpace = 0;
            TileWriter.this.calculateDirectorySize(Configuration.getInstance().getOsmdroidTileCache());
            if (TileWriter.mUsedCacheSpace > Configuration.getInstance().getTileFileSystemCacheMaxBytes()) {
                TileWriter.this.cutCurrentCache();
            }
            if (Configuration.getInstance().isDebugMode()) {
                Log.d("OsmDroid", "Finished init thread");
            }
        }
    }

    /* renamed from: org.osmdroid.tileprovider.modules.TileWriter$2 */
    class C02582 implements Comparator<File> {
        C02582() {
        }

        public int compare(File file, File file2) {
            return Long.valueOf(file.lastModified()).compareTo(Long.valueOf(file2.lastModified()));
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:23:0x00b2 in {9, 14, 15, 16, 17, 19, 22} preds:[]
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
    private void cutCurrentCache() {
        /*
        r10 = this;
        r0 = org.osmdroid.config.Configuration.getInstance();
        r0 = r0.getOsmdroidTileCache();
        monitor-enter(r0);
        r1 = mUsedCacheSpace;	 Catch:{ all -> 0x00af }
        r3 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ all -> 0x00af }
        r3 = r3.getTileFileSystemCacheTrimBytes();	 Catch:{ all -> 0x00af }
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));	 Catch:{ all -> 0x00af }
        if (r5 <= 0) goto L_0x00ad;	 Catch:{ all -> 0x00af }
        r1 = "OsmDroid";	 Catch:{ all -> 0x00af }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00af }
        r2.<init>();	 Catch:{ all -> 0x00af }
        r3 = "Trimming tile cache from ";	 Catch:{ all -> 0x00af }
        r2.append(r3);	 Catch:{ all -> 0x00af }
        r3 = mUsedCacheSpace;	 Catch:{ all -> 0x00af }
        r2.append(r3);	 Catch:{ all -> 0x00af }
        r3 = " to ";	 Catch:{ all -> 0x00af }
        r2.append(r3);	 Catch:{ all -> 0x00af }
        r3 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ all -> 0x00af }
        r3 = r3.getTileFileSystemCacheTrimBytes();	 Catch:{ all -> 0x00af }
        r2.append(r3);	 Catch:{ all -> 0x00af }
        r2 = r2.toString();	 Catch:{ all -> 0x00af }
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x00af }
        r1 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ all -> 0x00af }
        r1 = r1.getOsmdroidTileCache();	 Catch:{ all -> 0x00af }
        r1 = r10.getDirectoryFileList(r1);	 Catch:{ all -> 0x00af }
        r2 = 0;	 Catch:{ all -> 0x00af }
        r3 = new java.io.File[r2];	 Catch:{ all -> 0x00af }
        r1 = r1.toArray(r3);	 Catch:{ all -> 0x00af }
        r1 = (java.io.File[]) r1;	 Catch:{ all -> 0x00af }
        r3 = new org.osmdroid.tileprovider.modules.TileWriter$2;	 Catch:{ all -> 0x00af }
        r3.<init>();	 Catch:{ all -> 0x00af }
        java.util.Arrays.sort(r1, r3);	 Catch:{ all -> 0x00af }
        r3 = r1.length;	 Catch:{ all -> 0x00af }
        if (r2 >= r3) goto L_0x00a6;	 Catch:{ all -> 0x00af }
        r4 = r1[r2];	 Catch:{ all -> 0x00af }
        r5 = mUsedCacheSpace;	 Catch:{ all -> 0x00af }
        r7 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ all -> 0x00af }
        r7 = r7.getTileFileSystemCacheTrimBytes();	 Catch:{ all -> 0x00af }
        r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));	 Catch:{ all -> 0x00af }
        if (r9 > 0) goto L_0x0070;	 Catch:{ all -> 0x00af }
        goto L_0x00a6;	 Catch:{ all -> 0x00af }
        r5 = r4.length();	 Catch:{ all -> 0x00af }
        r7 = r4.delete();	 Catch:{ all -> 0x00af }
        if (r7 == 0) goto L_0x00a3;	 Catch:{ all -> 0x00af }
        r7 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ all -> 0x00af }
        r7 = r7.isDebugTileProviders();	 Catch:{ all -> 0x00af }
        if (r7 == 0) goto L_0x009e;	 Catch:{ all -> 0x00af }
        r7 = "OsmDroid";	 Catch:{ all -> 0x00af }
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00af }
        r8.<init>();	 Catch:{ all -> 0x00af }
        r9 = "Cache trim deleting ";	 Catch:{ all -> 0x00af }
        r8.append(r9);	 Catch:{ all -> 0x00af }
        r4 = r4.getAbsolutePath();	 Catch:{ all -> 0x00af }
        r8.append(r4);	 Catch:{ all -> 0x00af }
        r4 = r8.toString();	 Catch:{ all -> 0x00af }
        android.util.Log.d(r7, r4);	 Catch:{ all -> 0x00af }
        r7 = mUsedCacheSpace;	 Catch:{ all -> 0x00af }
        r7 = r7 - r5;	 Catch:{ all -> 0x00af }
        mUsedCacheSpace = r7;	 Catch:{ all -> 0x00af }
        r2 = r2 + 1;	 Catch:{ all -> 0x00af }
        goto L_0x005d;	 Catch:{ all -> 0x00af }
        r1 = "OsmDroid";	 Catch:{ all -> 0x00af }
        r2 = "Finished trimming tile cache";	 Catch:{ all -> 0x00af }
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x00af }
        monitor-exit(r0);	 Catch:{ all -> 0x00af }
        return;	 Catch:{ all -> 0x00af }
        r1 = move-exception;	 Catch:{ all -> 0x00af }
        monitor-exit(r0);	 Catch:{ all -> 0x00af }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.TileWriter.cutCurrentCache():void");
    }

    public TileWriter() {
        if (!hasInited) {
            hasInited = true;
            this.initThread = new C02571();
            this.initThread.setPriority(1);
            this.initThread.start();
        }
    }

    public void setMaximumCachedFileAge(long j) {
        this.mMaximumCachedFileAge = j;
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x0071 */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007e  */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:25:0x0078, code skipped:
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
     */
    public boolean saveFile(org.osmdroid.tileprovider.tilesource.ITileSource r4, long r5, java.io.InputStream r7, java.lang.Long r8) {
        /*
        r3 = this;
        r4 = r3.getFile(r4, r5);
        r5 = org.osmdroid.config.Configuration.getInstance();
        r5 = r5.isDebugTileProviders();
        if (r5 == 0) goto L_0x0028;
    L_0x000e:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "TileWrite ";
        r5.append(r6);
        r6 = r4.getAbsolutePath();
        r5.append(r6);
        r5 = r5.toString();
        r6 = "OsmDroid";
        android.util.Log.d(r6, r5);
    L_0x0028:
        r5 = r4.getParentFile();
        r6 = r5.exists();
        r8 = 0;
        if (r6 != 0) goto L_0x003a;
    L_0x0033:
        r5 = r3.createFolderAndCheckIfExists(r5);
        if (r5 != 0) goto L_0x003a;
    L_0x0039:
        return r8;
    L_0x003a:
        r5 = 0;
        r6 = 1;
        r0 = new java.io.BufferedOutputStream;	 Catch:{ IOException -> 0x0071 }
        r1 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0071 }
        r4 = r4.getPath();	 Catch:{ IOException -> 0x0071 }
        r1.<init>(r4);	 Catch:{ IOException -> 0x0071 }
        r4 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r0.<init>(r1, r4);	 Catch:{ IOException -> 0x0071 }
        r4 = org.osmdroid.tileprovider.util.StreamUtils.copy(r7, r0);	 Catch:{ IOException -> 0x006d, all -> 0x006a }
        r1 = mUsedCacheSpace;	 Catch:{ IOException -> 0x006d, all -> 0x006a }
        r1 = r1 + r4;
        mUsedCacheSpace = r1;	 Catch:{ IOException -> 0x006d, all -> 0x006a }
        r4 = mUsedCacheSpace;	 Catch:{ IOException -> 0x006d, all -> 0x006a }
        r7 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ IOException -> 0x006d, all -> 0x006a }
        r1 = r7.getTileFileSystemCacheMaxBytes();	 Catch:{ IOException -> 0x006d, all -> 0x006a }
        r7 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1));
        if (r7 <= 0) goto L_0x0066;
    L_0x0063:
        r3.cutCurrentCache();	 Catch:{ IOException -> 0x006d, all -> 0x006a }
    L_0x0066:
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r0);
        return r6;
    L_0x006a:
        r4 = move-exception;
        r5 = r0;
        goto L_0x007c;
    L_0x006d:
        r5 = r0;
        goto L_0x0071;
    L_0x006f:
        r4 = move-exception;
        goto L_0x007c;
    L_0x0071:
        r4 = org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors;	 Catch:{ all -> 0x006f }
        r4 = r4 + r6;
        org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors = r4;	 Catch:{ all -> 0x006f }
        if (r5 == 0) goto L_0x007b;
    L_0x0078:
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
    L_0x007b:
        return r8;
    L_0x007c:
        if (r5 == 0) goto L_0x0081;
    L_0x007e:
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
    L_0x0081:
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.TileWriter.saveFile(org.osmdroid.tileprovider.tilesource.ITileSource, long, java.io.InputStream, java.lang.Long):boolean");
    }

    public void onDetach() {
        Thread thread = this.initThread;
        if (thread != null) {
            try {
                thread.interrupt();
            } catch (Throwable unused) {
            }
        }
    }

    public File getFile(ITileSource iTileSource, long j) {
        File osmdroidTileCache = Configuration.getInstance().getOsmdroidTileCache();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(iTileSource.getTileRelativeFilenameString(j));
        stringBuilder.append(".tile");
        return new File(osmdroidTileCache, stringBuilder.toString());
    }

    private boolean createFolderAndCheckIfExists(File file) {
        if (file.mkdirs()) {
            return true;
        }
        StringBuilder stringBuilder;
        String str = "OsmDroid";
        if (Configuration.getInstance().isDebugMode()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to create ");
            stringBuilder.append(file);
            stringBuilder.append(" - wait and check again");
            Log.d(str, stringBuilder.toString());
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException unused) {
        }
        if (file.exists()) {
            if (Configuration.getInstance().isDebugMode()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Seems like another thread created ");
                stringBuilder.append(file);
                Log.d(str, stringBuilder.toString());
            }
            return true;
        }
        if (Configuration.getInstance().isDebugMode()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("File still doesn't exist: ");
            stringBuilder.append(file);
            Log.d(str, stringBuilder.toString());
        }
        return false;
    }

    private void calculateDirectorySize(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isFile()) {
                    mUsedCacheSpace += file2.length();
                }
                if (file2.isDirectory() && !isSymbolicDirectoryLink(file, file2)) {
                    calculateDirectorySize(file2);
                }
            }
        }
    }

    private boolean isSymbolicDirectoryLink(File file, File file2) {
        try {
            return file.getCanonicalPath().equals(file2.getCanonicalFile().getParent()) ^ 1;
        } catch (IOException | NoSuchElementException unused) {
            return true;
        }
    }

    private List<File> getDirectoryFileList(File file) {
        ArrayList arrayList = new ArrayList();
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isFile()) {
                    arrayList.add(file2);
                }
                if (file2.isDirectory()) {
                    arrayList.addAll(getDirectoryFileList(file2));
                }
            }
        }
        return arrayList;
    }

    public Drawable loadTile(ITileSource iTileSource, long j) throws Exception {
        File file = getFile(iTileSource, j);
        if (!file.exists()) {
            return null;
        }
        Drawable drawable = iTileSource.getDrawable(file.getPath());
        if (!((file.lastModified() < System.currentTimeMillis() - this.mMaximumCachedFileAge ? 1 : null) == null || drawable == null)) {
            if (Configuration.getInstance().isDebugMode()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Tile expired: ");
                stringBuilder.append(MapTileIndex.toString(j));
                Log.d("OsmDroid", stringBuilder.toString());
            }
            ExpirableBitmapDrawable.setState(drawable, -2);
        }
        return drawable;
    }
}

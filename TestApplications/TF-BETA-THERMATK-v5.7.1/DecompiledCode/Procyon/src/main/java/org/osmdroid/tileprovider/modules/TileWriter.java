// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import org.osmdroid.tileprovider.util.Counters;
import java.io.Closeable;
import org.osmdroid.tileprovider.util.StreamUtils;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.util.MapTileIndex;
import android.graphics.drawable.Drawable;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.io.File;
import android.util.Log;
import org.osmdroid.config.Configuration;

public class TileWriter implements IFilesystemCache
{
    static boolean hasInited = false;
    private static long mUsedCacheSpace;
    Thread initThread;
    private long mMaximumCachedFileAge;
    
    public TileWriter() {
        this.initThread = null;
        if (!TileWriter.hasInited) {
            TileWriter.hasInited = true;
            (this.initThread = new Thread() {
                @Override
                public void run() {
                    TileWriter.mUsedCacheSpace = 0L;
                    TileWriter.this.calculateDirectorySize(Configuration.getInstance().getOsmdroidTileCache());
                    if (TileWriter.mUsedCacheSpace > Configuration.getInstance().getTileFileSystemCacheMaxBytes()) {
                        TileWriter.this.cutCurrentCache();
                    }
                    if (Configuration.getInstance().isDebugMode()) {
                        Log.d("OsmDroid", "Finished init thread");
                    }
                }
            }).setPriority(1);
            this.initThread.start();
        }
    }
    
    private void calculateDirectorySize(final File file) {
        final File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (final File file2 : listFiles) {
                if (file2.isFile()) {
                    TileWriter.mUsedCacheSpace += file2.length();
                }
                if (file2.isDirectory() && !this.isSymbolicDirectoryLink(file, file2)) {
                    this.calculateDirectorySize(file2);
                }
            }
        }
    }
    
    private boolean createFolderAndCheckIfExists(final File obj) {
        if (obj.mkdirs()) {
            return true;
        }
        if (Configuration.getInstance().isDebugMode()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to create ");
            sb.append(obj);
            sb.append(" - wait and check again");
            Log.d("OsmDroid", sb.toString());
        }
        while (true) {
            try {
                Thread.sleep(500L);
                if (obj.exists()) {
                    if (Configuration.getInstance().isDebugMode()) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Seems like another thread created ");
                        sb2.append(obj);
                        Log.d("OsmDroid", sb2.toString());
                    }
                    return true;
                }
                if (Configuration.getInstance().isDebugMode()) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("File still doesn't exist: ");
                    sb3.append(obj);
                    Log.d("OsmDroid", sb3.toString());
                }
                return false;
            }
            catch (InterruptedException ex) {
                continue;
            }
            break;
        }
    }
    
    private void cutCurrentCache() {
        synchronized (Configuration.getInstance().getOsmdroidTileCache()) {
            if (TileWriter.mUsedCacheSpace > Configuration.getInstance().getTileFileSystemCacheTrimBytes()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Trimming tile cache from ");
                sb.append(TileWriter.mUsedCacheSpace);
                sb.append(" to ");
                sb.append(Configuration.getInstance().getTileFileSystemCacheTrimBytes());
                Log.d("OsmDroid", sb.toString());
                final List<File> directoryFileList = this.getDirectoryFileList(Configuration.getInstance().getOsmdroidTileCache());
                int i = 0;
                final File[] a = directoryFileList.toArray(new File[0]);
                Arrays.sort(a, new Comparator<File>() {
                    @Override
                    public int compare(final File file, final File file2) {
                        return Long.valueOf(file.lastModified()).compareTo(file2.lastModified());
                    }
                });
                while (i < a.length) {
                    final File file = a[i];
                    if (TileWriter.mUsedCacheSpace <= Configuration.getInstance().getTileFileSystemCacheTrimBytes()) {
                        break;
                    }
                    final long length = file.length();
                    if (file.delete()) {
                        if (Configuration.getInstance().isDebugTileProviders()) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Cache trim deleting ");
                            sb2.append(file.getAbsolutePath());
                            Log.d("OsmDroid", sb2.toString());
                        }
                        TileWriter.mUsedCacheSpace -= length;
                    }
                    ++i;
                }
                Log.d("OsmDroid", "Finished trimming tile cache");
            }
        }
    }
    
    private List<File> getDirectoryFileList(final File file) {
        final ArrayList<Object> list = new ArrayList<Object>();
        final File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (final File file2 : listFiles) {
                if (file2.isFile()) {
                    list.add(file2);
                }
                if (file2.isDirectory()) {
                    list.addAll(this.getDirectoryFileList(file2));
                }
            }
        }
        return (List<File>)list;
    }
    
    private boolean isSymbolicDirectoryLink(final File file, final File file2) {
        try {
            return file.getCanonicalPath().equals(file2.getCanonicalFile().getParent()) ^ true;
        }
        catch (IOException | NoSuchElementException ex) {
            return true;
        }
    }
    
    public File getFile(final ITileSource tileSource, final long n) {
        final File osmdroidTileCache = Configuration.getInstance().getOsmdroidTileCache();
        final StringBuilder sb = new StringBuilder();
        sb.append(tileSource.getTileRelativeFilenameString(n));
        sb.append(".tile");
        return new File(osmdroidTileCache, sb.toString());
    }
    
    public Drawable loadTile(final ITileSource tileSource, final long n) throws Exception {
        final File file = this.getFile(tileSource, n);
        if (!file.exists()) {
            return null;
        }
        final Drawable drawable = tileSource.getDrawable(file.getPath());
        if (file.lastModified() < System.currentTimeMillis() - this.mMaximumCachedFileAge && drawable != null) {
            if (Configuration.getInstance().isDebugMode()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Tile expired: ");
                sb.append(MapTileIndex.toString(n));
                Log.d("OsmDroid", sb.toString());
            }
            ExpirableBitmapDrawable.setState(drawable, -2);
        }
        return drawable;
    }
    
    @Override
    public void onDetach() {
        final Thread initThread = this.initThread;
        if (initThread == null) {
            return;
        }
        try {
            initThread.interrupt();
        }
        catch (Throwable t) {}
    }
    
    @Override
    public boolean saveFile(ITileSource ex, long copy, final InputStream inputStream, Long n) {
        final File file = this.getFile((ITileSource)ex, copy);
        if (Configuration.getInstance().isDebugTileProviders()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("TileWrite ");
            sb.append(file.getAbsolutePath());
            Log.d("OsmDroid", sb.toString());
        }
        final File parentFile = file.getParentFile();
        if (!parentFile.exists() && !this.createFolderAndCheckIfExists(parentFile)) {
            return false;
        }
        final Closeable closeable = null;
        final IOException ex2 = ex = null;
        while (true) {
            try {
                final Closeable closeable2;
                Label_0212: {
                    try {
                        ex = ex2;
                        ex = ex2;
                        final FileOutputStream out = new FileOutputStream(file.getPath());
                        ex = ex2;
                        n = (Long)new BufferedOutputStream(out, 8192);
                        try {
                            copy = StreamUtils.copy(inputStream, (OutputStream)n);
                            TileWriter.mUsedCacheSpace += copy;
                            if (TileWriter.mUsedCacheSpace > Configuration.getInstance().getTileFileSystemCacheMaxBytes()) {
                                this.cutCurrentCache();
                            }
                            StreamUtils.closeStream((Closeable)n);
                            return true;
                        }
                        catch (IOException ex) {}
                        finally {
                            ex = (IOException)n;
                        }
                    }
                    finally {
                        break Label_0212;
                    }
                    ++Counters.fileCacheSaveErrors;
                    if (closeable2 != null) {
                        StreamUtils.closeStream(closeable2);
                    }
                    return false;
                }
                if (ex != null) {
                    StreamUtils.closeStream((Closeable)ex);
                }
                throw closeable2;
            }
            catch (IOException ex3) {
                final Closeable closeable2 = closeable;
                continue;
            }
            break;
        }
    }
    
    public void setMaximumCachedFileAge(final long mMaximumCachedFileAge) {
        this.mMaximumCachedFileAge = mMaximumCachedFileAge;
    }
}

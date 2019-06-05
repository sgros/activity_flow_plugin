package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapsforge.android.AndroidUtils;
import org.mapsforge.core.util.IOUtils;

public class FileSystemTileCache implements TileCache {
    private static final String CACHE_DIRECTORY = "/Android/data/org.mapsforge.android.maps/cache/";
    private static final String IMAGE_FILE_NAME_EXTENSION = ".tile";
    private static final float LOAD_FACTOR = 0.6f;
    private static final Logger LOGGER = Logger.getLogger(FileSystemTileCache.class.getName());
    private static final String SERIALIZATION_FILE_NAME = "cache.ser";
    private static final int TILE_SIZE_IN_BYTES = 131072;
    private final Bitmap bitmapGet;
    private final ByteBuffer byteBuffer;
    private File cacheDirectory;
    private long cacheId;
    private int capacity;
    private Map<MapGeneratorJob, File> map;
    private int mapViewId;
    private boolean persistent;

    private static final class ImageFileNameFilter implements FilenameFilter {
        static final FilenameFilter INSTANCE = new ImageFileNameFilter();

        private ImageFileNameFilter() {
        }

        public boolean accept(File directory, String fileName) {
            return fileName.endsWith(FileSystemTileCache.IMAGE_FILE_NAME_EXTENSION);
        }
    }

    private static Map<MapGeneratorJob, File> createMap(final int mapCapacity) {
        return new LinkedHashMap<MapGeneratorJob, File>(((int) (((float) mapCapacity) / LOAD_FACTOR)) + 2, LOAD_FACTOR, true) {
            private static final long serialVersionUID = 1;

            /* Access modifiers changed, original: protected */
            public boolean removeEldestEntry(Entry<MapGeneratorJob, File> eldestEntry) {
                if (size() > mapCapacity) {
                    remove(eldestEntry.getKey());
                    if (!((File) eldestEntry.getValue()).delete()) {
                        ((File) eldestEntry.getValue()).deleteOnExit();
                    }
                }
                return false;
            }
        };
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:22:0x0050=Splitter:B:22:0x0050, B:18:0x003f=Splitter:B:18:0x003f} */
    private static java.util.Map<org.mapsforge.android.maps.mapgenerator.MapGeneratorJob, java.io.File> deserializeMap(java.io.File r11) {
        /*
        r7 = 0;
        r6 = new java.io.File;
        r8 = "cache.ser";
        r6.<init>(r11, r8);
        r8 = r6.exists();
        if (r8 == 0) goto L_0x001a;
    L_0x000e:
        r8 = r6.isFile();
        if (r8 == 0) goto L_0x001a;
    L_0x0014:
        r8 = r6.canRead();
        if (r8 != 0) goto L_0x001c;
    L_0x001a:
        r3 = r7;
    L_0x001b:
        return r3;
    L_0x001c:
        r1 = 0;
        r4 = 0;
        r2 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x003e, ClassNotFoundException -> 0x004f }
        r2.<init>(r6);	 Catch:{ IOException -> 0x003e, ClassNotFoundException -> 0x004f }
        r5 = new java.io.ObjectInputStream;	 Catch:{ IOException -> 0x0076, ClassNotFoundException -> 0x006f, all -> 0x0068 }
        r5.<init>(r2);	 Catch:{ IOException -> 0x0076, ClassNotFoundException -> 0x006f, all -> 0x0068 }
        r3 = r5.readObject();	 Catch:{ IOException -> 0x0079, ClassNotFoundException -> 0x0072, all -> 0x006b }
        r3 = (java.util.Map) r3;	 Catch:{ IOException -> 0x0079, ClassNotFoundException -> 0x0072, all -> 0x006b }
        r8 = r6.delete();	 Catch:{ IOException -> 0x0079, ClassNotFoundException -> 0x0072, all -> 0x006b }
        if (r8 != 0) goto L_0x0037;
    L_0x0034:
        r6.deleteOnExit();	 Catch:{ IOException -> 0x0079, ClassNotFoundException -> 0x0072, all -> 0x006b }
    L_0x0037:
        org.mapsforge.core.util.IOUtils.closeQuietly(r5);
        org.mapsforge.core.util.IOUtils.closeQuietly(r2);
        goto L_0x001b;
    L_0x003e:
        r0 = move-exception;
    L_0x003f:
        r8 = LOGGER;	 Catch:{ all -> 0x0060 }
        r9 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0060 }
        r10 = 0;
        r8.log(r9, r10, r0);	 Catch:{ all -> 0x0060 }
        org.mapsforge.core.util.IOUtils.closeQuietly(r4);
        org.mapsforge.core.util.IOUtils.closeQuietly(r1);
        r3 = r7;
        goto L_0x001b;
    L_0x004f:
        r0 = move-exception;
    L_0x0050:
        r8 = LOGGER;	 Catch:{ all -> 0x0060 }
        r9 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0060 }
        r10 = 0;
        r8.log(r9, r10, r0);	 Catch:{ all -> 0x0060 }
        org.mapsforge.core.util.IOUtils.closeQuietly(r4);
        org.mapsforge.core.util.IOUtils.closeQuietly(r1);
        r3 = r7;
        goto L_0x001b;
    L_0x0060:
        r7 = move-exception;
    L_0x0061:
        org.mapsforge.core.util.IOUtils.closeQuietly(r4);
        org.mapsforge.core.util.IOUtils.closeQuietly(r1);
        throw r7;
    L_0x0068:
        r7 = move-exception;
        r1 = r2;
        goto L_0x0061;
    L_0x006b:
        r7 = move-exception;
        r4 = r5;
        r1 = r2;
        goto L_0x0061;
    L_0x006f:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0050;
    L_0x0072:
        r0 = move-exception;
        r4 = r5;
        r1 = r2;
        goto L_0x0050;
    L_0x0076:
        r0 = move-exception;
        r1 = r2;
        goto L_0x003f;
    L_0x0079:
        r0 = move-exception;
        r4 = r5;
        r1 = r2;
        goto L_0x003f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.android.maps.mapgenerator.FileSystemTileCache.deserializeMap(java.io.File):java.util.Map");
    }

    private File createCacheDirectory() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + CACHE_DIRECTORY + this.mapViewId);
        if (!file.exists() && !file.mkdirs()) {
            LOGGER.log(Level.SEVERE, "could not create directory: ", file);
            return null;
        } else if (!file.isDirectory()) {
            LOGGER.log(Level.SEVERE, "not a directory", file);
            return null;
        } else if (!file.canRead()) {
            LOGGER.log(Level.SEVERE, "cannot read directory", file);
            return null;
        } else if (file.canWrite()) {
            return file;
        } else {
            LOGGER.log(Level.SEVERE, "cannot write directory", file);
            return null;
        }
    }

    private int checkCapacity(int requestedCapacity) {
        if (requestedCapacity < 0) {
            throw new IllegalArgumentException("capacity must not be negative: " + requestedCapacity);
        } else if (AndroidUtils.applicationRunsOnAndroidEmulator()) {
            return 0;
        } else {
            if (!"mounted".equals(Environment.getExternalStorageState())) {
                return 0;
            }
            this.cacheDirectory = createCacheDirectory();
            if (this.cacheDirectory == null) {
                return 0;
            }
            return requestedCapacity;
        }
    }

    private static boolean serializeMap(File directory, Map<MapGeneratorJob, File> map) {
        IOException e;
        Throwable th;
        File serializedMapFile = new File(directory, SERIALIZATION_FILE_NAME);
        if (serializedMapFile.exists() && !serializedMapFile.delete()) {
            return false;
        }
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            ObjectOutputStream objectOutputStream2;
            FileOutputStream fileOutputStream2 = new FileOutputStream(serializedMapFile);
            try {
                objectOutputStream2 = new ObjectOutputStream(fileOutputStream2);
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = fileOutputStream2;
                try {
                    LOGGER.log(Level.SEVERE, null, e);
                    IOUtils.closeQuietly(objectOutputStream);
                    IOUtils.closeQuietly(fileOutputStream);
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    IOUtils.closeQuietly(objectOutputStream);
                    IOUtils.closeQuietly(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = fileOutputStream2;
                IOUtils.closeQuietly(objectOutputStream);
                IOUtils.closeQuietly(fileOutputStream);
                throw th;
            }
            try {
                objectOutputStream2.writeObject(map);
                IOUtils.closeQuietly(objectOutputStream2);
                IOUtils.closeQuietly(fileOutputStream2);
                return true;
            } catch (IOException e3) {
                e = e3;
                objectOutputStream = objectOutputStream2;
                fileOutputStream = fileOutputStream2;
                LOGGER.log(Level.SEVERE, null, e);
                IOUtils.closeQuietly(objectOutputStream);
                IOUtils.closeQuietly(fileOutputStream);
                return false;
            } catch (Throwable th4) {
                th = th4;
                objectOutputStream = objectOutputStream2;
                fileOutputStream = fileOutputStream2;
                IOUtils.closeQuietly(objectOutputStream);
                IOUtils.closeQuietly(fileOutputStream);
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            LOGGER.log(Level.SEVERE, null, e);
            IOUtils.closeQuietly(objectOutputStream);
            IOUtils.closeQuietly(fileOutputStream);
            return false;
        }
    }

    public FileSystemTileCache(int capacity, int mapViewId) {
        this.mapViewId = mapViewId;
        this.capacity = checkCapacity(capacity);
        if (this.capacity <= 0 || this.cacheDirectory == null) {
            this.byteBuffer = null;
            this.bitmapGet = null;
            this.map = createMap(0);
            return;
        }
        Map<MapGeneratorJob, File> deserializedMap = deserializeMap(this.cacheDirectory);
        if (deserializedMap == null) {
            this.map = createMap(this.capacity);
        } else {
            this.map = deserializedMap;
        }
        this.byteBuffer = ByteBuffer.allocate(131072);
        this.bitmapGet = Bitmap.createBitmap(256, 256, Config.RGB_565);
    }

    public synchronized boolean containsKey(MapGeneratorJob mapGeneratorJob) {
        return this.map.containsKey(mapGeneratorJob);
    }

    public synchronized void destroy() {
        if (this.bitmapGet != null) {
            this.bitmapGet.recycle();
        }
        if (this.capacity != 0) {
            if (!(this.persistent && serializeMap(this.cacheDirectory, this.map))) {
                for (File file : this.map.values()) {
                    if (!file.delete()) {
                        file.deleteOnExit();
                    }
                }
                this.map.clear();
                if (this.cacheDirectory != null) {
                    File[] filesToDelete = this.cacheDirectory.listFiles(ImageFileNameFilter.INSTANCE);
                    if (filesToDelete != null) {
                        for (File file2 : filesToDelete) {
                            if (!file2.delete()) {
                                file2.deleteOnExit();
                            }
                        }
                    }
                    if (!this.cacheDirectory.delete()) {
                        this.cacheDirectory.deleteOnExit();
                    }
                }
            }
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:30:0x0055=Splitter:B:30:0x0055, B:39:0x006b=Splitter:B:39:0x006b} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0086 A:{SYNTHETIC, Splitter:B:49:0x0086} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x005c A:{SYNTHETIC, Splitter:B:33:0x005c} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0075 A:{SYNTHETIC, Splitter:B:42:0x0075} */
    public synchronized android.graphics.Bitmap get(org.mapsforge.android.maps.mapgenerator.MapGeneratorJob r11) {
        /*
        r10 = this;
        r6 = 0;
        monitor-enter(r10);
        r7 = r10.capacity;	 Catch:{ all -> 0x0041 }
        if (r7 != 0) goto L_0x0008;
    L_0x0006:
        monitor-exit(r10);
        return r6;
    L_0x0008:
        r3 = 0;
        r7 = r10.map;	 Catch:{ FileNotFoundException -> 0x0054, IOException -> 0x006a }
        r5 = r7.get(r11);	 Catch:{ FileNotFoundException -> 0x0054, IOException -> 0x006a }
        r5 = (java.io.File) r5;	 Catch:{ FileNotFoundException -> 0x0054, IOException -> 0x006a }
        r4 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0054, IOException -> 0x006a }
        r4.<init>(r5);	 Catch:{ FileNotFoundException -> 0x0054, IOException -> 0x006a }
        r7 = r10.byteBuffer;	 Catch:{ FileNotFoundException -> 0x009a, IOException -> 0x0097, all -> 0x0094 }
        r0 = r7.array();	 Catch:{ FileNotFoundException -> 0x009a, IOException -> 0x0097, all -> 0x0094 }
        r1 = r4.read(r0);	 Catch:{ FileNotFoundException -> 0x009a, IOException -> 0x0097, all -> 0x0094 }
        r7 = r0.length;	 Catch:{ FileNotFoundException -> 0x009a, IOException -> 0x0097, all -> 0x0094 }
        if (r1 != r7) goto L_0x0044;
    L_0x0023:
        r7 = r10.byteBuffer;	 Catch:{ FileNotFoundException -> 0x009a, IOException -> 0x0097, all -> 0x0094 }
        r7.rewind();	 Catch:{ FileNotFoundException -> 0x009a, IOException -> 0x0097, all -> 0x0094 }
        r7 = r10.bitmapGet;	 Catch:{ FileNotFoundException -> 0x009a, IOException -> 0x0097, all -> 0x0094 }
        r8 = r10.byteBuffer;	 Catch:{ FileNotFoundException -> 0x009a, IOException -> 0x0097, all -> 0x0094 }
        r7.copyPixelsFromBuffer(r8);	 Catch:{ FileNotFoundException -> 0x009a, IOException -> 0x0097, all -> 0x0094 }
        r6 = r10.bitmapGet;	 Catch:{ FileNotFoundException -> 0x009a, IOException -> 0x0097, all -> 0x0094 }
        if (r4 == 0) goto L_0x0006;
    L_0x0033:
        r4.close();	 Catch:{ IOException -> 0x0037 }
        goto L_0x0006;
    L_0x0037:
        r2 = move-exception;
        r7 = LOGGER;	 Catch:{ all -> 0x0041 }
        r8 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0041 }
        r9 = 0;
        r7.log(r8, r9, r2);	 Catch:{ all -> 0x0041 }
        goto L_0x0006;
    L_0x0041:
        r6 = move-exception;
        monitor-exit(r10);
        throw r6;
    L_0x0044:
        if (r4 == 0) goto L_0x0006;
    L_0x0046:
        r4.close();	 Catch:{ IOException -> 0x004a }
        goto L_0x0006;
    L_0x004a:
        r2 = move-exception;
        r7 = LOGGER;	 Catch:{ all -> 0x0041 }
        r8 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0041 }
        r9 = 0;
        r7.log(r8, r9, r2);	 Catch:{ all -> 0x0041 }
        goto L_0x0006;
    L_0x0054:
        r2 = move-exception;
    L_0x0055:
        r7 = r10.map;	 Catch:{ all -> 0x0083 }
        r7.remove(r11);	 Catch:{ all -> 0x0083 }
        if (r3 == 0) goto L_0x0006;
    L_0x005c:
        r3.close();	 Catch:{ IOException -> 0x0060 }
        goto L_0x0006;
    L_0x0060:
        r2 = move-exception;
        r7 = LOGGER;	 Catch:{ all -> 0x0041 }
        r8 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0041 }
        r9 = 0;
        r7.log(r8, r9, r2);	 Catch:{ all -> 0x0041 }
        goto L_0x0006;
    L_0x006a:
        r2 = move-exception;
    L_0x006b:
        r7 = LOGGER;	 Catch:{ all -> 0x0083 }
        r8 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0083 }
        r9 = 0;
        r7.log(r8, r9, r2);	 Catch:{ all -> 0x0083 }
        if (r3 == 0) goto L_0x0006;
    L_0x0075:
        r3.close();	 Catch:{ IOException -> 0x0079 }
        goto L_0x0006;
    L_0x0079:
        r2 = move-exception;
        r7 = LOGGER;	 Catch:{ all -> 0x0041 }
        r8 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0041 }
        r9 = 0;
        r7.log(r8, r9, r2);	 Catch:{ all -> 0x0041 }
        goto L_0x0006;
    L_0x0083:
        r6 = move-exception;
    L_0x0084:
        if (r3 == 0) goto L_0x0089;
    L_0x0086:
        r3.close();	 Catch:{ IOException -> 0x008a }
    L_0x0089:
        throw r6;	 Catch:{ all -> 0x0041 }
    L_0x008a:
        r2 = move-exception;
        r7 = LOGGER;	 Catch:{ all -> 0x0041 }
        r8 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0041 }
        r9 = 0;
        r7.log(r8, r9, r2);	 Catch:{ all -> 0x0041 }
        goto L_0x0089;
    L_0x0094:
        r6 = move-exception;
        r3 = r4;
        goto L_0x0084;
    L_0x0097:
        r2 = move-exception;
        r3 = r4;
        goto L_0x006b;
    L_0x009a:
        r2 = move-exception;
        r3 = r4;
        goto L_0x0055;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.android.maps.mapgenerator.FileSystemTileCache.get(org.mapsforge.android.maps.mapgenerator.MapGeneratorJob):android.graphics.Bitmap");
    }

    public synchronized int getCapacity() {
        return this.capacity;
    }

    public synchronized boolean isPersistent() {
        return this.persistent;
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0071 A:{SYNTHETIC, Splitter:B:26:0x0071} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0085 A:{SYNTHETIC, Splitter:B:36:0x0085} */
    public synchronized void put(org.mapsforge.android.maps.mapgenerator.MapGeneratorJob r11, android.graphics.Bitmap r12) {
        /*
        r10 = this;
        monitor-enter(r10);
        r5 = r10.capacity;	 Catch:{ all -> 0x007f }
        if (r5 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r10);
        return;
    L_0x0007:
        r2 = 0;
    L_0x0008:
        r6 = r10.cacheId;	 Catch:{ IOException -> 0x0062 }
        r8 = 1;
        r6 = r6 + r8;
        r10.cacheId = r6;	 Catch:{ IOException -> 0x0062 }
        r4 = new java.io.File;	 Catch:{ IOException -> 0x0062 }
        r5 = r10.cacheDirectory;	 Catch:{ IOException -> 0x0062 }
        r6 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0062 }
        r6.<init>();	 Catch:{ IOException -> 0x0062 }
        r8 = r10.cacheId;	 Catch:{ IOException -> 0x0062 }
        r6 = r6.append(r8);	 Catch:{ IOException -> 0x0062 }
        r7 = ".tile";
        r6 = r6.append(r7);	 Catch:{ IOException -> 0x0062 }
        r6 = r6.toString();	 Catch:{ IOException -> 0x0062 }
        r4.<init>(r5, r6);	 Catch:{ IOException -> 0x0062 }
        r5 = r4.exists();	 Catch:{ IOException -> 0x0062 }
        if (r5 != 0) goto L_0x0008;
    L_0x0031:
        r5 = r10.byteBuffer;	 Catch:{ IOException -> 0x0062 }
        r5.rewind();	 Catch:{ IOException -> 0x0062 }
        r5 = r10.byteBuffer;	 Catch:{ IOException -> 0x0062 }
        r12.copyPixelsToBuffer(r5);	 Catch:{ IOException -> 0x0062 }
        r5 = r10.byteBuffer;	 Catch:{ IOException -> 0x0062 }
        r0 = r5.array();	 Catch:{ IOException -> 0x0062 }
        r3 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0062 }
        r3.<init>(r4);	 Catch:{ IOException -> 0x0062 }
        r5 = 0;
        r6 = r0.length;	 Catch:{ IOException -> 0x0096, all -> 0x0093 }
        r3.write(r0, r5, r6);	 Catch:{ IOException -> 0x0096, all -> 0x0093 }
        r5 = r10.map;	 Catch:{ IOException -> 0x0096, all -> 0x0093 }
        r5.put(r11, r4);	 Catch:{ IOException -> 0x0096, all -> 0x0093 }
        if (r3 == 0) goto L_0x0055;
    L_0x0052:
        r3.close();	 Catch:{ IOException -> 0x0057 }
    L_0x0055:
        r2 = r3;
        goto L_0x0005;
    L_0x0057:
        r1 = move-exception;
        r5 = LOGGER;	 Catch:{ all -> 0x007f }
        r6 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x007f }
        r7 = 0;
        r5.log(r6, r7, r1);	 Catch:{ all -> 0x007f }
        r2 = r3;
        goto L_0x0005;
    L_0x0062:
        r1 = move-exception;
    L_0x0063:
        r5 = LOGGER;	 Catch:{ all -> 0x0082 }
        r6 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0082 }
        r7 = "external storage appears full";
        r5.log(r6, r7, r1);	 Catch:{ all -> 0x0082 }
        r5 = 0;
        r10.capacity = r5;	 Catch:{ all -> 0x0082 }
        if (r2 == 0) goto L_0x0005;
    L_0x0071:
        r2.close();	 Catch:{ IOException -> 0x0075 }
        goto L_0x0005;
    L_0x0075:
        r1 = move-exception;
        r5 = LOGGER;	 Catch:{ all -> 0x007f }
        r6 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x007f }
        r7 = 0;
        r5.log(r6, r7, r1);	 Catch:{ all -> 0x007f }
        goto L_0x0005;
    L_0x007f:
        r5 = move-exception;
        monitor-exit(r10);
        throw r5;
    L_0x0082:
        r5 = move-exception;
    L_0x0083:
        if (r2 == 0) goto L_0x0088;
    L_0x0085:
        r2.close();	 Catch:{ IOException -> 0x0089 }
    L_0x0088:
        throw r5;	 Catch:{ all -> 0x007f }
    L_0x0089:
        r1 = move-exception;
        r6 = LOGGER;	 Catch:{ all -> 0x007f }
        r7 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x007f }
        r8 = 0;
        r6.log(r7, r8, r1);	 Catch:{ all -> 0x007f }
        goto L_0x0088;
    L_0x0093:
        r5 = move-exception;
        r2 = r3;
        goto L_0x0083;
    L_0x0096:
        r1 = move-exception;
        r2 = r3;
        goto L_0x0063;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.android.maps.mapgenerator.FileSystemTileCache.put(org.mapsforge.android.maps.mapgenerator.MapGeneratorJob, android.graphics.Bitmap):void");
    }

    public synchronized void setCapacity(int capacity) {
        if (this.capacity != capacity) {
            this.capacity = checkCapacity(capacity);
            if (this.capacity != 0) {
                Map<MapGeneratorJob, File> newMap = createMap(this.capacity);
                if (this.map != null) {
                    newMap.putAll(this.map);
                }
                this.map = newMap;
            }
        }
    }

    public synchronized void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
}

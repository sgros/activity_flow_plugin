// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import org.osmdroid.util.TileSystem;
import java.util.Iterator;
import android.util.Log;
import org.osmdroid.util.MapTileIndex;
import java.io.File;
import java.io.InputStream;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import java.util.concurrent.atomic.AtomicReference;
import java.util.ArrayList;

public class MapTileFileArchiveProvider extends MapTileFileStorageProviderBase
{
    private final boolean ignoreTileSource;
    private final ArrayList<IArchiveFile> mArchiveFiles;
    private final boolean mSpecificArchivesProvided;
    private final AtomicReference<ITileSource> mTileSource;
    
    public MapTileFileArchiveProvider(final IRegisterReceiver registerReceiver, final ITileSource tileSource) {
        this(registerReceiver, tileSource, null);
    }
    
    public MapTileFileArchiveProvider(final IRegisterReceiver registerReceiver, final ITileSource tileSource, final IArchiveFile[] array) {
        this(registerReceiver, tileSource, array, false);
    }
    
    public MapTileFileArchiveProvider(final IRegisterReceiver registerReceiver, final ITileSource tileSource, final IArchiveFile[] array, final boolean ignoreTileSource) {
        super(registerReceiver, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
        this.mArchiveFiles = new ArrayList<IArchiveFile>();
        this.mTileSource = new AtomicReference<ITileSource>();
        this.ignoreTileSource = ignoreTileSource;
        this.setTileSource(tileSource);
        if (array == null) {
            this.mSpecificArchivesProvided = false;
            this.findArchiveFiles();
        }
        else {
            this.mSpecificArchivesProvided = true;
            for (int i = array.length - 1; i >= 0; --i) {
                this.mArchiveFiles.add(array[i]);
            }
        }
    }
    
    private void clearArcives() {
        while (!this.mArchiveFiles.isEmpty()) {
            final IArchiveFile archiveFile = this.mArchiveFiles.get(0);
            if (archiveFile != null) {
                archiveFile.close();
            }
            this.mArchiveFiles.remove(0);
        }
    }
    
    private void findArchiveFiles() {
        this.clearArcives();
        final File[] listFiles = Configuration.getInstance().getOsmdroidBasePath().listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final IArchiveFile archiveFile = ArchiveFileFactory.getArchiveFile(listFiles[i]);
                if (archiveFile != null) {
                    archiveFile.setIgnoreTileSource(this.ignoreTileSource);
                    this.mArchiveFiles.add(archiveFile);
                }
            }
        }
    }
    
    private InputStream getInputStream(final long n, final ITileSource tileSource) {
        synchronized (this) {
            for (final IArchiveFile obj : this.mArchiveFiles) {
                if (obj != null) {
                    final InputStream inputStream = obj.getInputStream(tileSource, n);
                    if (inputStream != null) {
                        if (Configuration.getInstance().isDebugMode()) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Found tile ");
                            sb.append(MapTileIndex.toString(n));
                            sb.append(" in ");
                            sb.append(obj);
                            Log.d("OsmDroid", sb.toString());
                        }
                        return inputStream;
                    }
                    continue;
                }
            }
            return null;
        }
    }
    
    @Override
    public void detach() {
        this.clearArcives();
        super.detach();
    }
    
    @Override
    public int getMaximumZoomLevel() {
        final ITileSource tileSource = this.mTileSource.get();
        int n;
        if (tileSource != null) {
            n = tileSource.getMaximumZoomLevel();
        }
        else {
            n = TileSystem.getMaximumZoomLevel();
        }
        return n;
    }
    
    @Override
    public int getMinimumZoomLevel() {
        final ITileSource tileSource = this.mTileSource.get();
        int minimumZoomLevel;
        if (tileSource != null) {
            minimumZoomLevel = tileSource.getMinimumZoomLevel();
        }
        else {
            minimumZoomLevel = 0;
        }
        return minimumZoomLevel;
    }
    
    @Override
    protected String getName() {
        return "File Archive Provider";
    }
    
    @Override
    protected String getThreadGroupName() {
        return "filearchive";
    }
    
    public TileLoader getTileLoader() {
        return new TileLoader();
    }
    
    @Override
    public boolean getUsesDataConnection() {
        return false;
    }
    
    @Override
    protected void onMediaMounted() {
        if (!this.mSpecificArchivesProvided) {
            this.findArchiveFiles();
        }
    }
    
    @Override
    protected void onMediaUnmounted() {
        if (!this.mSpecificArchivesProvided) {
            this.findArchiveFiles();
        }
    }
    
    @Override
    public void setTileSource(final ITileSource newValue) {
        this.mTileSource.set(newValue);
    }
    
    protected class TileLoader extends MapTileModuleProviderBase.TileLoader
    {
        @Override
        public Drawable loadTile(final long p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        org/osmdroid/tileprovider/modules/MapTileFileArchiveProvider$TileLoader.this$0:Lorg/osmdroid/tileprovider/modules/MapTileFileArchiveProvider;
            //     4: invokestatic    org/osmdroid/tileprovider/modules/MapTileFileArchiveProvider.access$000:(Lorg/osmdroid/tileprovider/modules/MapTileFileArchiveProvider;)Ljava/util/concurrent/atomic/AtomicReference;
            //     7: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
            //    10: checkcast       Lorg/osmdroid/tileprovider/tilesource/ITileSource;
            //    13: astore_3       
            //    14: aconst_null    
            //    15: astore          4
            //    17: aconst_null    
            //    18: astore          5
            //    20: aconst_null    
            //    21: astore          6
            //    23: aload_3        
            //    24: ifnonnull       29
            //    27: aconst_null    
            //    28: areturn        
            //    29: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
            //    32: invokeinterface org/osmdroid/config/IConfigurationProvider.isDebugMode:()Z
            //    37: ifeq            79
            //    40: new             Ljava/lang/StringBuilder;
            //    43: astore          7
            //    45: aload           7
            //    47: invokespecial   java/lang/StringBuilder.<init>:()V
            //    50: aload           7
            //    52: ldc             "Archives - Tile doesn't exist: "
            //    54: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    57: pop            
            //    58: aload           7
            //    60: lload_1        
            //    61: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
            //    64: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    67: pop            
            //    68: ldc             "OsmDroid"
            //    70: aload           7
            //    72: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    75: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
            //    78: pop            
            //    79: aload_0        
            //    80: getfield        org/osmdroid/tileprovider/modules/MapTileFileArchiveProvider$TileLoader.this$0:Lorg/osmdroid/tileprovider/modules/MapTileFileArchiveProvider;
            //    83: lload_1        
            //    84: aload_3        
            //    85: invokestatic    org/osmdroid/tileprovider/modules/MapTileFileArchiveProvider.access$100:(Lorg/osmdroid/tileprovider/modules/MapTileFileArchiveProvider;JLorg/osmdroid/tileprovider/tilesource/ITileSource;)Ljava/io/InputStream;
            //    88: astore          7
            //    90: aload           7
            //    92: ifnull          199
            //    95: aload           7
            //    97: astore          8
            //    99: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
            //   102: invokeinterface org/osmdroid/config/IConfigurationProvider.isDebugMode:()Z
            //   107: ifeq            169
            //   110: aload           7
            //   112: astore          8
            //   114: new             Ljava/lang/StringBuilder;
            //   117: astore          6
            //   119: aload           7
            //   121: astore          8
            //   123: aload           6
            //   125: invokespecial   java/lang/StringBuilder.<init>:()V
            //   128: aload           7
            //   130: astore          8
            //   132: aload           6
            //   134: ldc             "Use tile from archive: "
            //   136: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   139: pop            
            //   140: aload           7
            //   142: astore          8
            //   144: aload           6
            //   146: lload_1        
            //   147: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
            //   150: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   153: pop            
            //   154: aload           7
            //   156: astore          8
            //   158: ldc             "OsmDroid"
            //   160: aload           6
            //   162: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   165: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
            //   168: pop            
            //   169: aload           7
            //   171: astore          8
            //   173: aload_3        
            //   174: aload           7
            //   176: invokeinterface org/osmdroid/tileprovider/tilesource/ITileSource.getDrawable:(Ljava/io/InputStream;)Landroid/graphics/drawable/Drawable;
            //   181: astore          6
            //   183: goto            199
            //   186: astore          8
            //   188: aload           7
            //   190: astore          6
            //   192: aload           8
            //   194: astore          7
            //   196: goto            233
            //   199: aload           6
            //   201: astore          8
            //   203: aload           7
            //   205: ifnull          267
            //   208: aload           6
            //   210: astore          8
            //   212: aload           7
            //   214: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
            //   217: goto            267
            //   220: astore          6
            //   222: aconst_null    
            //   223: astore          8
            //   225: goto            272
            //   228: astore          7
            //   230: aconst_null    
            //   231: astore          6
            //   233: aload           6
            //   235: astore          8
            //   237: ldc             "OsmDroid"
            //   239: ldc             "Error loading tile"
            //   241: aload           7
            //   243: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   246: pop            
            //   247: aload           5
            //   249: astore          8
            //   251: aload           6
            //   253: ifnull          267
            //   256: aload           4
            //   258: astore          8
            //   260: aload           6
            //   262: astore          7
            //   264: goto            212
            //   267: aload           8
            //   269: areturn        
            //   270: astore          6
            //   272: aload           8
            //   274: ifnull          282
            //   277: aload           8
            //   279: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
            //   282: aload           6
            //   284: athrow         
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  29     79     228    233    Ljava/lang/Throwable;
            //  29     79     220    228    Any
            //  79     90     228    233    Ljava/lang/Throwable;
            //  79     90     220    228    Any
            //  99     110    186    199    Ljava/lang/Throwable;
            //  99     110    270    272    Any
            //  114    119    186    199    Ljava/lang/Throwable;
            //  114    119    270    272    Any
            //  123    128    186    199    Ljava/lang/Throwable;
            //  123    128    270    272    Any
            //  132    140    186    199    Ljava/lang/Throwable;
            //  132    140    270    272    Any
            //  144    154    186    199    Ljava/lang/Throwable;
            //  144    154    270    272    Any
            //  158    169    186    199    Ljava/lang/Throwable;
            //  158    169    270    272    Any
            //  173    183    186    199    Ljava/lang/Throwable;
            //  173    183    270    272    Any
            //  237    247    270    272    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0169:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
    }
}

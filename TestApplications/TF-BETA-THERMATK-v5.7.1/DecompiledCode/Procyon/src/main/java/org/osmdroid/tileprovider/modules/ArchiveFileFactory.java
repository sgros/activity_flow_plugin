// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import android.util.Log;
import java.io.File;
import android.os.Build$VERSION;
import java.util.HashMap;
import java.util.Map;

public class ArchiveFileFactory
{
    static Map<String, Class<? extends IArchiveFile>> extensionMap;
    
    static {
        (ArchiveFileFactory.extensionMap = new HashMap<String, Class<? extends IArchiveFile>>()).put("zip", ZipFileArchive.class);
        if (Build$VERSION.SDK_INT >= 10) {
            ArchiveFileFactory.extensionMap.put("sqlite", DatabaseFileArchive.class);
            ArchiveFileFactory.extensionMap.put("mbtiles", MBTilesFileArchive.class);
            ArchiveFileFactory.extensionMap.put("gemf", GEMFFileArchive.class);
        }
    }
    
    public static IArchiveFile getArchiveFile(final File file) {
        String s2;
        final String s = s2 = file.getName();
        while (true) {
            if (!s.contains(".")) {
                break Label_0029;
            }
            try {
                s2 = s.substring(s.lastIndexOf(".") + 1);
                final Class<? extends IArchiveFile> clazz = ArchiveFileFactory.extensionMap.get(s2.toLowerCase());
                if (clazz != null) {
                    try {
                        final IArchiveFile archiveFile = (IArchiveFile)clazz.newInstance();
                        archiveFile.init(file);
                        return archiveFile;
                    }
                    catch (Exception s) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Error opening archive file ");
                        sb.append(file.getAbsolutePath());
                        Log.e("OsmDroid", sb.toString(), (Throwable)s);
                    }
                    catch (IllegalAccessException s) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Error initializing archive file provider ");
                        sb2.append(file.getAbsolutePath());
                        Log.e("OsmDroid", sb2.toString(), (Throwable)s);
                    }
                    catch (InstantiationException s) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Error initializing archive file provider ");
                        sb3.append(file.getAbsolutePath());
                        Log.e("OsmDroid", sb3.toString(), (Throwable)s);
                    }
                }
                return null;
            }
            catch (Exception ex) {
                s2 = s;
                continue;
            }
            break;
        }
    }
}

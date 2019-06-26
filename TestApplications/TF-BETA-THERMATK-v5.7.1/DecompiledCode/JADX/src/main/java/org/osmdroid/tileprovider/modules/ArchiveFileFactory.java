package org.osmdroid.tileprovider.modules;

import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ArchiveFileFactory {
    static Map<String, Class<? extends IArchiveFile>> extensionMap = new HashMap();

    static {
        extensionMap.put("zip", ZipFileArchive.class);
        if (VERSION.SDK_INT >= 10) {
            extensionMap.put("sqlite", DatabaseFileArchive.class);
            extensionMap.put("mbtiles", MBTilesFileArchive.class);
            extensionMap.put("gemf", GEMFFileArchive.class);
        }
    }

    public static IArchiveFile getArchiveFile(File file) {
        StringBuilder stringBuilder;
        String str = "Error initializing archive file provider ";
        String str2 = "OsmDroid";
        String name = file.getName();
        String str3 = ".";
        if (name.contains(str3)) {
            try {
                name = name.substring(name.lastIndexOf(str3) + 1);
            } catch (Exception unused) {
            }
        }
        Class cls = (Class) extensionMap.get(name.toLowerCase());
        if (cls != null) {
            try {
                IArchiveFile iArchiveFile = (IArchiveFile) cls.newInstance();
                iArchiveFile.init(file);
                return iArchiveFile;
            } catch (InstantiationException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(file.getAbsolutePath());
                Log.e(str2, stringBuilder.toString(), e);
            } catch (IllegalAccessException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(file.getAbsolutePath());
                Log.e(str2, stringBuilder.toString(), e2);
            } catch (Exception e3) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Error opening archive file ");
                stringBuilder2.append(file.getAbsolutePath());
                Log.e(str2, stringBuilder2.toString(), e3);
            }
        }
        return null;
    }
}

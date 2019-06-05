package android.support.p001v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParserException;

/* renamed from: android.support.v4.content.FileProvider */
public class FileProvider extends ContentProvider {
    private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
    private static final File DEVICE_ROOT = new File("/");
    private static HashMap<String, PathStrategy> sCache = new HashMap();
    private PathStrategy mStrategy;

    /* renamed from: android.support.v4.content.FileProvider$PathStrategy */
    interface PathStrategy {
        File getFileForUri(Uri uri);

        Uri getUriForFile(File file);
    }

    /* renamed from: android.support.v4.content.FileProvider$SimplePathStrategy */
    static class SimplePathStrategy implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        SimplePathStrategy(String str) {
            this.mAuthority = str;
        }

        /* Access modifiers changed, original: 0000 */
        public void addRoot(String str, File file) {
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            try {
                this.mRoots.put(str, file.getCanonicalFile());
            } catch (IOException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to resolve canonical path for ");
                stringBuilder.append(file);
                throw new IllegalArgumentException(stringBuilder.toString(), e);
            }
        }

        public Uri getUriForFile(File file) {
            StringBuilder stringBuilder;
            try {
                String canonicalPath = file.getCanonicalPath();
                Entry entry = null;
                for (Entry entry2 : this.mRoots.entrySet()) {
                    String path = ((File) entry2.getValue()).getPath();
                    if (canonicalPath.startsWith(path) && (entry == null || path.length() > ((File) entry.getValue()).getPath().length())) {
                        entry = entry2;
                    }
                }
                if (entry != null) {
                    String path2 = ((File) entry.getValue()).getPath();
                    if (path2.endsWith("/")) {
                        canonicalPath = canonicalPath.substring(path2.length());
                    } else {
                        canonicalPath = canonicalPath.substring(path2.length() + 1);
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(Uri.encode((String) entry.getKey()));
                    stringBuilder.append('/');
                    stringBuilder.append(Uri.encode(canonicalPath, "/"));
                    return new Builder().scheme("content").authority(this.mAuthority).encodedPath(stringBuilder.toString()).build();
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to find configured root that contains ");
                stringBuilder.append(canonicalPath);
                throw new IllegalArgumentException(stringBuilder.toString());
            } catch (IOException unused) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to resolve canonical path for ");
                stringBuilder.append(file);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public File getFileForUri(Uri uri) {
            StringBuilder stringBuilder;
            String encodedPath = uri.getEncodedPath();
            int indexOf = encodedPath.indexOf(47, 1);
            String decode = Uri.decode(encodedPath.substring(1, indexOf));
            encodedPath = Uri.decode(encodedPath.substring(indexOf + 1));
            File file = (File) this.mRoots.get(decode);
            if (file != null) {
                File file2 = new File(file, encodedPath);
                try {
                    File canonicalFile = file2.getCanonicalFile();
                    if (canonicalFile.getPath().startsWith(file.getPath())) {
                        return canonicalFile;
                    }
                    throw new SecurityException("Resolved path jumped beyond configured root");
                } catch (IOException unused) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to resolve canonical path for ");
                    stringBuilder.append(file2);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to find configured root for ");
            stringBuilder.append(uri);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public boolean onCreate() {
        return true;
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (providerInfo.exported) {
            throw new SecurityException("Provider must not be exported");
        } else if (providerInfo.grantUriPermissions) {
            this.mStrategy = FileProvider.getPathStrategy(context, providerInfo.authority);
        } else {
            throw new SecurityException("Provider must grant uri permissions");
        }
    }

    public static Uri getUriForFile(Context context, String str, File file) {
        return FileProvider.getPathStrategy(context, str).getUriForFile(file);
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        File fileForUri = this.mStrategy.getFileForUri(uri);
        if (strArr == null) {
            strArr = COLUMNS;
        }
        String[] strArr3 = new String[strArr.length];
        Object[] objArr = new Object[strArr.length];
        int i = 0;
        for (Object obj : strArr) {
            int i2;
            if ("_display_name".equals(obj)) {
                strArr3[i] = "_display_name";
                i2 = i + 1;
                objArr[i] = fileForUri.getName();
            } else if ("_size".equals(obj)) {
                strArr3[i] = "_size";
                i2 = i + 1;
                objArr[i] = Long.valueOf(fileForUri.length());
            } else {
            }
            i = i2;
        }
        String[] copyOf = FileProvider.copyOf(strArr3, i);
        Object[] copyOf2 = FileProvider.copyOf(objArr, i);
        MatrixCursor matrixCursor = new MatrixCursor(copyOf, 1);
        matrixCursor.addRow(copyOf2);
        return matrixCursor;
    }

    public String getType(Uri uri) {
        File fileForUri = this.mStrategy.getFileForUri(uri);
        int lastIndexOf = fileForUri.getName().lastIndexOf(46);
        if (lastIndexOf >= 0) {
            String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileForUri.getName().substring(lastIndexOf + 1));
            if (mimeTypeFromExtension != null) {
                return mimeTypeFromExtension;
            }
        }
        return "application/octet-stream";
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new UnsupportedOperationException("No external updates");
    }

    public int delete(Uri uri, String str, String[] strArr) {
        return this.mStrategy.getFileForUri(uri).delete();
    }

    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), FileProvider.modeToMode(str));
    }

    private static PathStrategy getPathStrategy(Context context, String str) {
        PathStrategy pathStrategy;
        synchronized (sCache) {
            pathStrategy = (PathStrategy) sCache.get(str);
            if (pathStrategy == null) {
                try {
                    pathStrategy = FileProvider.parsePathStrategy(context, str);
                    sCache.put(str, pathStrategy);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e);
                } catch (XmlPullParserException e2) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e2);
                }
            }
        }
        return pathStrategy;
    }

    private static PathStrategy parsePathStrategy(Context context, String str) throws IOException, XmlPullParserException {
        SimplePathStrategy simplePathStrategy = new SimplePathStrategy(str);
        XmlResourceParser loadXmlMetaData = context.getPackageManager().resolveContentProvider(str, 128).loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if (loadXmlMetaData != null) {
            while (true) {
                int next = loadXmlMetaData.next();
                if (next == 1) {
                    return simplePathStrategy;
                }
                if (next == 2) {
                    String name = loadXmlMetaData.getName();
                    File file = null;
                    String attributeValue = loadXmlMetaData.getAttributeValue(null, "name");
                    String attributeValue2 = loadXmlMetaData.getAttributeValue(null, "path");
                    File[] externalFilesDirs;
                    if ("root-path".equals(name)) {
                        file = DEVICE_ROOT;
                    } else if ("files-path".equals(name)) {
                        file = context.getFilesDir();
                    } else if ("cache-path".equals(name)) {
                        file = context.getCacheDir();
                    } else if ("external-path".equals(name)) {
                        file = Environment.getExternalStorageDirectory();
                    } else if ("external-files-path".equals(name)) {
                        externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
                        if (externalFilesDirs.length > 0) {
                            file = externalFilesDirs[0];
                        }
                    } else if ("external-cache-path".equals(name)) {
                        externalFilesDirs = ContextCompat.getExternalCacheDirs(context);
                        if (externalFilesDirs.length > 0) {
                            file = externalFilesDirs[0];
                        }
                    } else if (VERSION.SDK_INT >= 21 && "external-media-path".equals(name)) {
                        externalFilesDirs = context.getExternalMediaDirs();
                        if (externalFilesDirs.length > 0) {
                            file = externalFilesDirs[0];
                        }
                    }
                    if (file != null) {
                        simplePathStrategy.addRoot(attributeValue, FileProvider.buildPath(file, attributeValue2));
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        }
    }

    private static int modeToMode(String str) {
        if ("r".equals(str)) {
            return 268435456;
        }
        if ("w".equals(str) || "wt".equals(str)) {
            return 738197504;
        }
        if ("wa".equals(str)) {
            return 704643072;
        }
        if ("rw".equals(str)) {
            return 939524096;
        }
        if ("rwt".equals(str)) {
            return 1006632960;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid mode: ");
        stringBuilder.append(str);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static File buildPath(File file, String... strArr) {
        for (String str : strArr) {
            if (str != null) {
                file = new File(file, str);
            }
        }
        return file;
    }

    private static String[] copyOf(String[] strArr, int i) {
        String[] strArr2 = new String[i];
        System.arraycopy(strArr, 0, strArr2, 0, i);
        return strArr2;
    }

    private static Object[] copyOf(Object[] objArr, int i) {
        Object[] objArr2 = new Object[i];
        System.arraycopy(objArr, 0, objArr2, 0, i);
        return objArr2;
    }
}

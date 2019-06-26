package androidx.core.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
    private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
    private static final File DEVICE_ROOT = new File("/");
    private static HashMap<String, PathStrategy> sCache = new HashMap();
    private PathStrategy mStrategy;

    interface PathStrategy {
        File getFileForUri(Uri uri);

        Uri getUriForFile(File file);
    }

    static class SimplePathStrategy implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:23:0x00d4 in {9, 10, 14, 15, 17, 19, 22} preds:[]
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
        public android.net.Uri getUriForFile(java.io.File r6) {
            /*
            r5 = this;
            r6 = r6.getCanonicalPath();	 Catch:{ IOException -> 0x00bd }
            r0 = 0;
            r1 = r5.mRoots;
            r1 = r1.entrySet();
            r1 = r1.iterator();
            r2 = r1.hasNext();
            if (r2 == 0) goto L_0x0043;
            r2 = r1.next();
            r2 = (java.util.Map.Entry) r2;
            r3 = r2.getValue();
            r3 = (java.io.File) r3;
            r3 = r3.getPath();
            r4 = r6.startsWith(r3);
            if (r4 == 0) goto L_0x000f;
            if (r0 == 0) goto L_0x0041;
            r3 = r3.length();
            r4 = r0.getValue();
            r4 = (java.io.File) r4;
            r4 = r4.getPath();
            r4 = r4.length();
            if (r3 <= r4) goto L_0x000f;
            r0 = r2;
            goto L_0x000f;
            if (r0 == 0) goto L_0x00a6;
            r1 = r0.getValue();
            r1 = (java.io.File) r1;
            r1 = r1.getPath();
            r2 = "/";
            r3 = r1.endsWith(r2);
            if (r3 == 0) goto L_0x0060;
            r1 = r1.length();
            r6 = r6.substring(r1);
            goto L_0x006a;
            r1 = r1.length();
            r1 = r1 + 1;
            r6 = r6.substring(r1);
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r0 = r0.getKey();
            r0 = (java.lang.String) r0;
            r0 = android.net.Uri.encode(r0);
            r1.append(r0);
            r0 = 47;
            r1.append(r0);
            r6 = android.net.Uri.encode(r6, r2);
            r1.append(r6);
            r6 = r1.toString();
            r0 = new android.net.Uri$Builder;
            r0.<init>();
            r1 = "content";
            r0 = r0.scheme(r1);
            r1 = r5.mAuthority;
            r0 = r0.authority(r1);
            r6 = r0.encodedPath(r6);
            r6 = r6.build();
            return r6;
            r0 = new java.lang.IllegalArgumentException;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Failed to find configured root that contains ";
            r1.append(r2);
            r1.append(r6);
            r6 = r1.toString();
            r0.<init>(r6);
            throw r0;
            r0 = new java.lang.IllegalArgumentException;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Failed to resolve canonical path for ";
            r1.append(r2);
            r1.append(r6);
            r6 = r1.toString();
            r0.<init>(r6);
            throw r0;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.FileProvider$SimplePathStrategy.getUriForFile(java.io.File):android.net.Uri");
        }

        SimplePathStrategy(String str) {
            this.mAuthority = str;
        }

        /* Access modifiers changed, original: 0000 */
        public void addRoot(String str, File file) {
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            Object file2;
            try {
                file2 = file2.getCanonicalFile();
                this.mRoots.put(str, file2);
            } catch (IOException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to resolve canonical path for ");
                stringBuilder.append(file2);
                throw new IllegalArgumentException(stringBuilder.toString(), e);
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
                Object file2 = new File(file, encodedPath);
                try {
                    file2 = file2.getCanonicalFile();
                    if (file2.getPath().startsWith(file.getPath())) {
                        return file2;
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

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:44:0x00d5 in {10, 13, 16, 19, 24, 29, 36, 38, 39, 41, 43} preds:[]
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
    private static androidx.core.content.FileProvider.PathStrategy parsePathStrategy(android.content.Context r9, java.lang.String r10) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r0 = new androidx.core.content.FileProvider$SimplePathStrategy;
        r0.<init>(r10);
        r1 = r9.getPackageManager();
        r2 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r1 = r1.resolveContentProvider(r10, r2);
        if (r1 == 0) goto L_0x00be;
        r10 = r9.getPackageManager();
        r2 = "android.support.FILE_PROVIDER_PATHS";
        r10 = r1.loadXmlMetaData(r10, r2);
        if (r10 == 0) goto L_0x00b6;
        r1 = r10.next();
        r2 = 1;
        if (r1 == r2) goto L_0x00b5;
        r3 = 2;
        if (r1 != r3) goto L_0x001d;
        r1 = r10.getName();
        r3 = 0;
        r4 = "name";
        r4 = r10.getAttributeValue(r3, r4);
        r5 = "path";
        r5 = r10.getAttributeValue(r3, r5);
        r6 = "root-path";
        r6 = r6.equals(r1);
        r7 = 0;
        if (r6 == 0) goto L_0x0044;
        r3 = DEVICE_ROOT;
        goto L_0x00a6;
        r6 = "files-path";
        r6 = r6.equals(r1);
        if (r6 == 0) goto L_0x0051;
        r3 = r9.getFilesDir();
        goto L_0x00a6;
        r6 = "cache-path";
        r6 = r6.equals(r1);
        if (r6 == 0) goto L_0x005e;
        r3 = r9.getCacheDir();
        goto L_0x00a6;
        r6 = "external-path";
        r6 = r6.equals(r1);
        if (r6 == 0) goto L_0x006b;
        r3 = android.os.Environment.getExternalStorageDirectory();
        goto L_0x00a6;
        r6 = "external-files-path";
        r6 = r6.equals(r1);
        if (r6 == 0) goto L_0x007d;
        r1 = androidx.core.content.ContextCompat.getExternalFilesDirs(r9, r3);
        r6 = r1.length;
        if (r6 <= 0) goto L_0x00a6;
        r3 = r1[r7];
        goto L_0x00a6;
        r6 = "external-cache-path";
        r6 = r6.equals(r1);
        if (r6 == 0) goto L_0x008f;
        r1 = androidx.core.content.ContextCompat.getExternalCacheDirs(r9);
        r6 = r1.length;
        if (r6 <= 0) goto L_0x00a6;
        r3 = r1[r7];
        goto L_0x00a6;
        r6 = android.os.Build.VERSION.SDK_INT;
        r8 = 21;
        if (r6 < r8) goto L_0x00a6;
        r6 = "external-media-path";
        r1 = r6.equals(r1);
        if (r1 == 0) goto L_0x00a6;
        r1 = r9.getExternalMediaDirs();
        r6 = r1.length;
        if (r6 <= 0) goto L_0x00a6;
        r3 = r1[r7];
        if (r3 == 0) goto L_0x001d;
        r1 = new java.lang.String[r2];
        r1[r7] = r5;
        r1 = buildPath(r3, r1);
        r0.addRoot(r4, r1);
        goto L_0x001d;
        return r0;
        r9 = new java.lang.IllegalArgumentException;
        r10 = "Missing android.support.FILE_PROVIDER_PATHS meta-data";
        r9.<init>(r10);
        throw r9;
        r9 = new java.lang.IllegalArgumentException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Couldn't find meta-data for provider with authority ";
        r0.append(r1);
        r0.append(r10);
        r10 = r0.toString();
        r9.<init>(r10);
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.FileProvider.parsePathStrategy(android.content.Context, java.lang.String):androidx.core.content.FileProvider$PathStrategy");
    }

    public boolean onCreate() {
        return true;
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (providerInfo.exported) {
            throw new SecurityException("Provider must not be exported");
        } else if (providerInfo.grantUriPermissions) {
            this.mStrategy = getPathStrategy(context, providerInfo.authority);
        } else {
            throw new SecurityException("Provider must grant uri permissions");
        }
    }

    public static Uri getUriForFile(Context context, String str, File file) {
        return getPathStrategy(context, str).getUriForFile(file);
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
            String str3 = "_display_name";
            if (str3.equals(obj)) {
                strArr3[i] = str3;
                i2 = i + 1;
                objArr[i] = fileForUri.getName();
            } else {
                str3 = "_size";
                if (str3.equals(obj)) {
                    strArr3[i] = str3;
                    i2 = i + 1;
                    objArr[i] = Long.valueOf(fileForUri.length());
                } else {
                }
            }
            i = i2;
        }
        String[] copyOf = copyOf(strArr3, i);
        Object[] copyOf2 = copyOf(objArr, i);
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
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), modeToMode(str));
    }

    private static PathStrategy getPathStrategy(Context context, String str) {
        PathStrategy pathStrategy;
        synchronized (sCache) {
            pathStrategy = (PathStrategy) sCache.get(str);
            if (pathStrategy == null) {
                try {
                    pathStrategy = parsePathStrategy(context, str);
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

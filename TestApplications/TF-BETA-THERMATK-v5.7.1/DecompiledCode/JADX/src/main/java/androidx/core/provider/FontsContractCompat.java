package androidx.core.provider;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import androidx.collection.LruCache;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.provider.SelfDestructiveThread.ReplyCallback;
import androidx.core.util.Preconditions;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FontsContractCompat {
    private static final SelfDestructiveThread sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
    private static final Comparator<byte[]> sByteArrayComparator = new C00435();
    static final Object sLock = new Object();
    static final SimpleArrayMap<String, ArrayList<ReplyCallback<TypefaceResult>>> sPendingReplies = new SimpleArrayMap();
    static final LruCache<String, Typeface> sTypefaceCache = new LruCache(16);

    /* renamed from: androidx.core.provider.FontsContractCompat$5 */
    static class C00435 implements Comparator<byte[]> {
        C00435() {
        }

        public int compare(byte[] bArr, byte[] bArr2) {
            int length;
            int length2;
            if (bArr.length != bArr2.length) {
                length = bArr.length;
                length2 = bArr2.length;
            } else {
                int i = 0;
                while (i < bArr.length) {
                    if (bArr[i] != bArr2[i]) {
                        length = bArr[i];
                        length2 = bArr2[i];
                    } else {
                        i++;
                    }
                }
                return 0;
            }
            return length - length2;
        }
    }

    public static class FontFamilyResult {
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        public FontFamilyResult(int i, FontInfo[] fontInfoArr) {
            this.mStatusCode = i;
            this.mFonts = fontInfoArr;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }
    }

    public static class FontInfo {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        public FontInfo(Uri uri, int i, int i2, boolean z, int i3) {
            Preconditions.checkNotNull(uri);
            this.mUri = uri;
            this.mTtcIndex = i;
            this.mWeight = i2;
            this.mItalic = z;
            this.mResultCode = i3;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }

        public int getResultCode() {
            return this.mResultCode;
        }
    }

    private static final class TypefaceResult {
        final int mResult;
        final Typeface mTypeface;

        TypefaceResult(Typeface typeface, int i) {
            this.mTypeface = typeface;
            this.mResult = i;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:49:0x013f in {6, 7, 17, 18, 20, 21, 23, 24, 27, 28, 32, 33, 34, 36, 38, 39, 41, 43, 45, 47, 48} preds:[]
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
    static androidx.core.provider.FontsContractCompat.FontInfo[] getFontFromProvider(android.content.Context r19, androidx.core.provider.FontRequest r20, java.lang.String r21, android.os.CancellationSignal r22) {
        /*
        r0 = r21;
        r1 = new java.util.ArrayList;
        r1.<init>();
        r2 = new android.net.Uri$Builder;
        r2.<init>();
        r3 = "content";
        r2 = r2.scheme(r3);
        r2 = r2.authority(r0);
        r2 = r2.build();
        r4 = new android.net.Uri$Builder;
        r4.<init>();
        r3 = r4.scheme(r3);
        r0 = r3.authority(r0);
        r3 = "file";
        r0 = r0.appendPath(r3);
        r0 = r0.build();
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ all -> 0x0137 }
        r5 = 16;
        r7 = "font_variation_settings";
        r9 = 2;
        r10 = 7;
        r11 = "result_code";
        r12 = "font_italic";
        r13 = "font_weight";
        r14 = "font_ttc_index";
        r15 = "file_id";
        r3 = "_id";
        r6 = 1;
        r8 = 0;
        if (r4 <= r5) goto L_0x007e;
        r4 = r19.getContentResolver();	 Catch:{ all -> 0x0137 }
        r10 = new java.lang.String[r10];	 Catch:{ all -> 0x0137 }
        r10[r8] = r3;	 Catch:{ all -> 0x0137 }
        r10[r6] = r15;	 Catch:{ all -> 0x0137 }
        r10[r9] = r14;	 Catch:{ all -> 0x0137 }
        r5 = 3;	 Catch:{ all -> 0x0137 }
        r10[r5] = r7;	 Catch:{ all -> 0x0137 }
        r5 = 4;	 Catch:{ all -> 0x0137 }
        r10[r5] = r13;	 Catch:{ all -> 0x0137 }
        r5 = 5;	 Catch:{ all -> 0x0137 }
        r10[r5] = r12;	 Catch:{ all -> 0x0137 }
        r5 = 6;	 Catch:{ all -> 0x0137 }
        r10[r5] = r11;	 Catch:{ all -> 0x0137 }
        r7 = "query = ?";	 Catch:{ all -> 0x0137 }
        r9 = new java.lang.String[r6];	 Catch:{ all -> 0x0137 }
        r5 = r20.getQuery();	 Catch:{ all -> 0x0137 }
        r9[r8] = r5;	 Catch:{ all -> 0x0137 }
        r16 = 0;	 Catch:{ all -> 0x0137 }
        r5 = r2;	 Catch:{ all -> 0x0137 }
        r18 = r1;	 Catch:{ all -> 0x0137 }
        r1 = 1;	 Catch:{ all -> 0x0137 }
        r6 = r10;	 Catch:{ all -> 0x0137 }
        r10 = 0;	 Catch:{ all -> 0x0137 }
        r8 = r9;	 Catch:{ all -> 0x0137 }
        r9 = r16;	 Catch:{ all -> 0x0137 }
        r1 = 0;	 Catch:{ all -> 0x0137 }
        r10 = r22;	 Catch:{ all -> 0x0137 }
        r4 = r4.query(r5, r6, r7, r8, r9, r10);	 Catch:{ all -> 0x0137 }
        goto L_0x00ab;	 Catch:{ all -> 0x0137 }
        r18 = r1;	 Catch:{ all -> 0x0137 }
        r1 = 0;	 Catch:{ all -> 0x0137 }
        r4 = r19.getContentResolver();	 Catch:{ all -> 0x0137 }
        r6 = new java.lang.String[r10];	 Catch:{ all -> 0x0137 }
        r6[r1] = r3;	 Catch:{ all -> 0x0137 }
        r5 = 1;	 Catch:{ all -> 0x0137 }
        r6[r5] = r15;	 Catch:{ all -> 0x0137 }
        r6[r9] = r14;	 Catch:{ all -> 0x0137 }
        r5 = 3;	 Catch:{ all -> 0x0137 }
        r6[r5] = r7;	 Catch:{ all -> 0x0137 }
        r5 = 4;	 Catch:{ all -> 0x0137 }
        r6[r5] = r13;	 Catch:{ all -> 0x0137 }
        r5 = 5;	 Catch:{ all -> 0x0137 }
        r6[r5] = r12;	 Catch:{ all -> 0x0137 }
        r5 = 6;	 Catch:{ all -> 0x0137 }
        r6[r5] = r11;	 Catch:{ all -> 0x0137 }
        r7 = "query = ?";	 Catch:{ all -> 0x0137 }
        r5 = 1;	 Catch:{ all -> 0x0137 }
        r8 = new java.lang.String[r5];	 Catch:{ all -> 0x0137 }
        r5 = r20.getQuery();	 Catch:{ all -> 0x0137 }
        r8[r1] = r5;	 Catch:{ all -> 0x0137 }
        r9 = 0;	 Catch:{ all -> 0x0137 }
        r5 = r2;	 Catch:{ all -> 0x0137 }
        r4 = r4.query(r5, r6, r7, r8, r9);	 Catch:{ all -> 0x0137 }
        if (r4 == 0) goto L_0x0127;
        r5 = r4.getCount();	 Catch:{ all -> 0x0125 }
        if (r5 <= 0) goto L_0x0127;	 Catch:{ all -> 0x0125 }
        r5 = r4.getColumnIndex(r11);	 Catch:{ all -> 0x0125 }
        r6 = new java.util.ArrayList;	 Catch:{ all -> 0x0125 }
        r6.<init>();	 Catch:{ all -> 0x0125 }
        r3 = r4.getColumnIndex(r3);	 Catch:{ all -> 0x0125 }
        r7 = r4.getColumnIndex(r15);	 Catch:{ all -> 0x0125 }
        r8 = r4.getColumnIndex(r14);	 Catch:{ all -> 0x0125 }
        r9 = r4.getColumnIndex(r13);	 Catch:{ all -> 0x0125 }
        r10 = r4.getColumnIndex(r12);	 Catch:{ all -> 0x0125 }
        r11 = r4.moveToNext();	 Catch:{ all -> 0x0125 }
        if (r11 == 0) goto L_0x0129;	 Catch:{ all -> 0x0125 }
        r11 = -1;	 Catch:{ all -> 0x0125 }
        if (r5 == r11) goto L_0x00e0;	 Catch:{ all -> 0x0125 }
        r12 = r4.getInt(r5);	 Catch:{ all -> 0x0125 }
        r18 = r12;	 Catch:{ all -> 0x0125 }
        goto L_0x00e2;	 Catch:{ all -> 0x0125 }
        r18 = 0;	 Catch:{ all -> 0x0125 }
        if (r8 == r11) goto L_0x00ea;	 Catch:{ all -> 0x0125 }
        r12 = r4.getInt(r8);	 Catch:{ all -> 0x0125 }
        r15 = r12;	 Catch:{ all -> 0x0125 }
        goto L_0x00eb;	 Catch:{ all -> 0x0125 }
        r15 = 0;	 Catch:{ all -> 0x0125 }
        if (r7 != r11) goto L_0x00f6;	 Catch:{ all -> 0x0125 }
        r12 = r4.getLong(r3);	 Catch:{ all -> 0x0125 }
        r12 = android.content.ContentUris.withAppendedId(r2, r12);	 Catch:{ all -> 0x0125 }
        goto L_0x00fe;	 Catch:{ all -> 0x0125 }
        r12 = r4.getLong(r7);	 Catch:{ all -> 0x0125 }
        r12 = android.content.ContentUris.withAppendedId(r0, r12);	 Catch:{ all -> 0x0125 }
        r14 = r12;	 Catch:{ all -> 0x0125 }
        if (r9 == r11) goto L_0x0108;	 Catch:{ all -> 0x0125 }
        r12 = r4.getInt(r9);	 Catch:{ all -> 0x0125 }
        r16 = r12;	 Catch:{ all -> 0x0125 }
        goto L_0x010c;	 Catch:{ all -> 0x0125 }
        r12 = 400; // 0x190 float:5.6E-43 double:1.976E-321;	 Catch:{ all -> 0x0125 }
        r16 = 400; // 0x190 float:5.6E-43 double:1.976E-321;	 Catch:{ all -> 0x0125 }
        if (r10 == r11) goto L_0x0118;	 Catch:{ all -> 0x0125 }
        r11 = r4.getInt(r10);	 Catch:{ all -> 0x0125 }
        r12 = 1;	 Catch:{ all -> 0x0125 }
        if (r11 != r12) goto L_0x0119;	 Catch:{ all -> 0x0125 }
        r17 = 1;	 Catch:{ all -> 0x0125 }
        goto L_0x011b;	 Catch:{ all -> 0x0125 }
        r12 = 1;	 Catch:{ all -> 0x0125 }
        r17 = 0;	 Catch:{ all -> 0x0125 }
        r11 = new androidx.core.provider.FontsContractCompat$FontInfo;	 Catch:{ all -> 0x0125 }
        r13 = r11;	 Catch:{ all -> 0x0125 }
        r13.<init>(r14, r15, r16, r17, r18);	 Catch:{ all -> 0x0125 }
        r6.add(r11);	 Catch:{ all -> 0x0125 }
        goto L_0x00d0;
        r0 = move-exception;
        goto L_0x0139;
        r6 = r18;
        if (r4 == 0) goto L_0x012e;
        r4.close();
        r0 = new androidx.core.provider.FontsContractCompat.FontInfo[r1];
        r0 = r6.toArray(r0);
        r0 = (androidx.core.provider.FontsContractCompat.FontInfo[]) r0;
        return r0;
        r0 = move-exception;
        r4 = 0;
        if (r4 == 0) goto L_0x013e;
        r4.close();
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.provider.FontsContractCompat.getFontFromProvider(android.content.Context, androidx.core.provider.FontRequest, java.lang.String, android.os.CancellationSignal):androidx.core.provider.FontsContractCompat$FontInfo[]");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:17:0x008a in {9, 10, 12, 14, 16} preds:[]
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
    public static android.content.pm.ProviderInfo getProvider(android.content.pm.PackageManager r5, androidx.core.provider.FontRequest r6, android.content.res.Resources r7) throws android.content.pm.PackageManager.NameNotFoundException {
        /*
        r0 = r6.getProviderAuthority();
        r1 = 0;
        r2 = r5.resolveContentProvider(r0, r1);
        if (r2 == 0) goto L_0x0073;
        r3 = r2.packageName;
        r4 = r6.getProviderPackage();
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0050;
        r0 = r2.packageName;
        r3 = 64;
        r5 = r5.getPackageInfo(r0, r3);
        r5 = r5.signatures;
        r5 = convertToByteArrayList(r5);
        r0 = sByteArrayComparator;
        java.util.Collections.sort(r5, r0);
        r6 = getCertificates(r6, r7);
        r7 = r6.size();
        if (r1 >= r7) goto L_0x004e;
        r7 = new java.util.ArrayList;
        r0 = r6.get(r1);
        r0 = (java.util.Collection) r0;
        r7.<init>(r0);
        r0 = sByteArrayComparator;
        java.util.Collections.sort(r7, r0);
        r7 = equalsByteArrayList(r5, r7);
        if (r7 == 0) goto L_0x004b;
        return r2;
        r1 = r1 + 1;
        goto L_0x002e;
        r5 = 0;
        return r5;
        r5 = new android.content.pm.PackageManager$NameNotFoundException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r1 = "Found content provider ";
        r7.append(r1);
        r7.append(r0);
        r0 = ", but package was not ";
        r7.append(r0);
        r6 = r6.getProviderPackage();
        r7.append(r6);
        r6 = r7.toString();
        r5.<init>(r6);
        throw r5;
        r5 = new android.content.pm.PackageManager$NameNotFoundException;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "No package found for authority: ";
        r6.append(r7);
        r6.append(r0);
        r6 = r6.toString();
        r5.<init>(r6);
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.provider.FontsContractCompat.getProvider(android.content.pm.PackageManager, androidx.core.provider.FontRequest, android.content.res.Resources):android.content.pm.ProviderInfo");
    }

    static TypefaceResult getFontInternal(Context context, FontRequest fontRequest, int i) {
        try {
            FontFamilyResult fetchFonts = fetchFonts(context, null, fontRequest);
            int i2 = -3;
            if (fetchFonts.getStatusCode() == 0) {
                Typeface createFromFontInfo = TypefaceCompat.createFromFontInfo(context, null, fetchFonts.getFonts(), i);
                if (createFromFontInfo != null) {
                    i2 = 0;
                }
                return new TypefaceResult(createFromFontInfo, i2);
            }
            if (fetchFonts.getStatusCode() == 1) {
                i2 = -2;
            }
            return new TypefaceResult(null, i2);
        } catch (NameNotFoundException unused) {
            return new TypefaceResult(null, -1);
        }
    }

    /* JADX WARNING: Missing block: B:33:0x0072, code skipped:
            return r2;
     */
    /* JADX WARNING: Missing block: B:37:0x0083, code skipped:
            sBackgroundThread.postAndReply(r1, new androidx.core.provider.FontsContractCompat.C00453());
     */
    /* JADX WARNING: Missing block: B:38:0x008d, code skipped:
            return r2;
     */
    public static android.graphics.Typeface getFontSync(final android.content.Context r2, final androidx.core.provider.FontRequest r3, final androidx.core.content.res.ResourcesCompat.FontCallback r4, final android.os.Handler r5, boolean r6, int r7, final int r8) {
        /*
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r3.getIdentifier();
        r0.append(r1);
        r1 = "-";
        r0.append(r1);
        r0.append(r8);
        r0 = r0.toString();
        r1 = sTypefaceCache;
        r1 = r1.get(r0);
        r1 = (android.graphics.Typeface) r1;
        if (r1 == 0) goto L_0x0028;
    L_0x0022:
        if (r4 == 0) goto L_0x0027;
    L_0x0024:
        r4.onFontRetrieved(r1);
    L_0x0027:
        return r1;
    L_0x0028:
        if (r6 == 0) goto L_0x0043;
    L_0x002a:
        r1 = -1;
        if (r7 != r1) goto L_0x0043;
    L_0x002d:
        r2 = getFontInternal(r2, r3, r8);
        if (r4 == 0) goto L_0x0040;
    L_0x0033:
        r3 = r2.mResult;
        if (r3 != 0) goto L_0x003d;
    L_0x0037:
        r3 = r2.mTypeface;
        r4.callbackSuccessAsync(r3, r5);
        goto L_0x0040;
    L_0x003d:
        r4.callbackFailAsync(r3, r5);
    L_0x0040:
        r2 = r2.mTypeface;
        return r2;
    L_0x0043:
        r1 = new androidx.core.provider.FontsContractCompat$1;
        r1.<init>(r2, r3, r8, r0);
        r2 = 0;
        if (r6 == 0) goto L_0x0056;
    L_0x004b:
        r3 = sBackgroundThread;	 Catch:{ InterruptedException -> 0x0055 }
        r3 = r3.postAndWait(r1, r7);	 Catch:{ InterruptedException -> 0x0055 }
        r3 = (androidx.core.provider.FontsContractCompat.TypefaceResult) r3;	 Catch:{ InterruptedException -> 0x0055 }
        r2 = r3.mTypeface;	 Catch:{ InterruptedException -> 0x0055 }
    L_0x0055:
        return r2;
    L_0x0056:
        if (r4 != 0) goto L_0x005a;
    L_0x0058:
        r3 = r2;
        goto L_0x005f;
    L_0x005a:
        r3 = new androidx.core.provider.FontsContractCompat$2;
        r3.<init>(r4, r5);
    L_0x005f:
        r4 = sLock;
        monitor-enter(r4);
        r5 = sPendingReplies;	 Catch:{ all -> 0x008e }
        r5 = r5.get(r0);	 Catch:{ all -> 0x008e }
        r5 = (java.util.ArrayList) r5;	 Catch:{ all -> 0x008e }
        if (r5 == 0) goto L_0x0073;
    L_0x006c:
        if (r3 == 0) goto L_0x0071;
    L_0x006e:
        r5.add(r3);	 Catch:{ all -> 0x008e }
    L_0x0071:
        monitor-exit(r4);	 Catch:{ all -> 0x008e }
        return r2;
    L_0x0073:
        if (r3 == 0) goto L_0x0082;
    L_0x0075:
        r5 = new java.util.ArrayList;	 Catch:{ all -> 0x008e }
        r5.<init>();	 Catch:{ all -> 0x008e }
        r5.add(r3);	 Catch:{ all -> 0x008e }
        r3 = sPendingReplies;	 Catch:{ all -> 0x008e }
        r3.put(r0, r5);	 Catch:{ all -> 0x008e }
    L_0x0082:
        monitor-exit(r4);	 Catch:{ all -> 0x008e }
        r3 = sBackgroundThread;
        r4 = new androidx.core.provider.FontsContractCompat$3;
        r4.<init>(r0);
        r3.postAndReply(r1, r4);
        return r2;
    L_0x008e:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x008e }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.provider.FontsContractCompat.getFontSync(android.content.Context, androidx.core.provider.FontRequest, androidx.core.content.res.ResourcesCompat$FontCallback, android.os.Handler, boolean, int, int):android.graphics.Typeface");
    }

    public static Map<Uri, ByteBuffer> prepareFontData(Context context, FontInfo[] fontInfoArr, CancellationSignal cancellationSignal) {
        HashMap hashMap = new HashMap();
        for (FontInfo fontInfo : fontInfoArr) {
            if (fontInfo.getResultCode() == 0) {
                Uri uri = fontInfo.getUri();
                if (!hashMap.containsKey(uri)) {
                    hashMap.put(uri, TypefaceCompatUtil.mmap(context, cancellationSignal, uri));
                }
            }
        }
        return Collections.unmodifiableMap(hashMap);
    }

    public static FontFamilyResult fetchFonts(Context context, CancellationSignal cancellationSignal, FontRequest fontRequest) throws NameNotFoundException {
        ProviderInfo provider = getProvider(context.getPackageManager(), fontRequest, context.getResources());
        if (provider == null) {
            return new FontFamilyResult(1, null);
        }
        return new FontFamilyResult(0, getFontFromProvider(context, fontRequest, provider.authority, cancellationSignal));
    }

    private static List<List<byte[]>> getCertificates(FontRequest fontRequest, Resources resources) {
        if (fontRequest.getCertificates() != null) {
            return fontRequest.getCertificates();
        }
        return FontResourcesParserCompat.readCerts(resources, fontRequest.getCertificatesArrayResId());
    }

    private static boolean equalsByteArrayList(List<byte[]> list, List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!Arrays.equals((byte[]) list.get(i), (byte[]) list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static List<byte[]> convertToByteArrayList(Signature[] signatureArr) {
        ArrayList arrayList = new ArrayList();
        for (Signature toByteArray : signatureArr) {
            arrayList.add(toByteArray.toByteArray());
        }
        return arrayList;
    }
}

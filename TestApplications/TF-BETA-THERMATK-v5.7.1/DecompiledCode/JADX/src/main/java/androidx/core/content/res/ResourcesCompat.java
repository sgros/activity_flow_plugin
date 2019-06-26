package androidx.core.content.res;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;

public final class ResourcesCompat {

    public static abstract class FontCallback {
        public abstract void onFontRetrievalFailed(int i);

        public abstract void onFontRetrieved(Typeface typeface);

        public final void callbackSuccessAsync(final Typeface typeface, Handler handler) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            handler.post(new Runnable() {
                public void run() {
                    FontCallback.this.onFontRetrieved(typeface);
                }
            });
        }

        public final void callbackFailAsync(final int i, Handler handler) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            handler.post(new Runnable() {
                public void run() {
                    FontCallback.this.onFontRetrievalFailed(i);
                }
            });
        }
    }

    public static Drawable getDrawable(Resources resources, int i, Theme theme) throws NotFoundException {
        if (VERSION.SDK_INT >= 21) {
            return resources.getDrawable(i, theme);
        }
        return resources.getDrawable(i);
    }

    public static Typeface getFont(Context context, int i, TypedValue typedValue, int i2, FontCallback fontCallback) throws NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, i, typedValue, i2, fontCallback, null, true);
    }

    private static Typeface loadFont(Context context, int i, TypedValue typedValue, int i2, FontCallback fontCallback, Handler handler, boolean z) {
        Resources resources = context.getResources();
        resources.getValue(i, typedValue, true);
        Typeface loadFont = loadFont(context, resources, typedValue, i, i2, fontCallback, handler, z);
        if (loadFont != null || fontCallback != null) {
            return loadFont;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Font resource ID #0x");
        stringBuilder.append(Integer.toHexString(i));
        stringBuilder.append(" could not be retrieved.");
        throw new NotFoundException(stringBuilder.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a3  */
    private static android.graphics.Typeface loadFont(android.content.Context r15, android.content.res.Resources r16, android.util.TypedValue r17, int r18, int r19, androidx.core.content.res.ResourcesCompat.FontCallback r20, android.os.Handler r21, boolean r22) {
        /*
        r0 = r16;
        r1 = r17;
        r4 = r18;
        r5 = r19;
        r9 = r20;
        r10 = r21;
        r11 = "ResourcesCompat";
        r2 = r1.string;
        if (r2 == 0) goto L_0x00a7;
    L_0x0012:
        r12 = r2.toString();
        r1 = "res/";
        r1 = r12.startsWith(r1);
        r13 = 0;
        r14 = -3;
        if (r1 != 0) goto L_0x0026;
    L_0x0020:
        if (r9 == 0) goto L_0x0025;
    L_0x0022:
        r9.callbackFailAsync(r14, r10);
    L_0x0025:
        return r13;
    L_0x0026:
        r1 = androidx.core.graphics.TypefaceCompat.findFromCache(r0, r4, r5);
        if (r1 == 0) goto L_0x0032;
    L_0x002c:
        if (r9 == 0) goto L_0x0031;
    L_0x002e:
        r9.callbackSuccessAsync(r1, r10);
    L_0x0031:
        return r1;
    L_0x0032:
        r1 = r12.toLowerCase();	 Catch:{ XmlPullParserException -> 0x008c, IOException -> 0x0076 }
        r2 = ".xml";
        r1 = r1.endsWith(r2);	 Catch:{ XmlPullParserException -> 0x008c, IOException -> 0x0076 }
        if (r1 == 0) goto L_0x0065;
    L_0x003e:
        r1 = r0.getXml(r4);	 Catch:{ XmlPullParserException -> 0x008c, IOException -> 0x0076 }
        r2 = androidx.core.content.res.FontResourcesParserCompat.parse(r1, r0);	 Catch:{ XmlPullParserException -> 0x008c, IOException -> 0x0076 }
        if (r2 != 0) goto L_0x0053;
    L_0x0048:
        r0 = "Failed to find font-family tag";
        android.util.Log.e(r11, r0);	 Catch:{ XmlPullParserException -> 0x008c, IOException -> 0x0076 }
        if (r9 == 0) goto L_0x0052;
    L_0x004f:
        r9.callbackFailAsync(r14, r10);	 Catch:{ XmlPullParserException -> 0x008c, IOException -> 0x0076 }
    L_0x0052:
        return r13;
    L_0x0053:
        r1 = r15;
        r3 = r16;
        r4 = r18;
        r5 = r19;
        r6 = r20;
        r7 = r21;
        r8 = r22;
        r0 = androidx.core.graphics.TypefaceCompat.createFromResourcesFamilyXml(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ XmlPullParserException -> 0x008c, IOException -> 0x0076 }
        return r0;
    L_0x0065:
        r1 = r15;
        r0 = androidx.core.graphics.TypefaceCompat.createFromResourcesFontFile(r15, r0, r4, r12, r5);	 Catch:{ XmlPullParserException -> 0x008c, IOException -> 0x0076 }
        if (r9 == 0) goto L_0x0075;
    L_0x006c:
        if (r0 == 0) goto L_0x0072;
    L_0x006e:
        r9.callbackSuccessAsync(r0, r10);	 Catch:{ XmlPullParserException -> 0x008c, IOException -> 0x0076 }
        goto L_0x0075;
    L_0x0072:
        r9.callbackFailAsync(r14, r10);	 Catch:{ XmlPullParserException -> 0x008c, IOException -> 0x0076 }
    L_0x0075:
        return r0;
    L_0x0076:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Failed to read xml resource ";
        r1.append(r2);
        r1.append(r12);
        r1 = r1.toString();
        android.util.Log.e(r11, r1, r0);
        goto L_0x00a1;
    L_0x008c:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Failed to parse xml resource ";
        r1.append(r2);
        r1.append(r12);
        r1 = r1.toString();
        android.util.Log.e(r11, r1, r0);
    L_0x00a1:
        if (r9 == 0) goto L_0x00a6;
    L_0x00a3:
        r9.callbackFailAsync(r14, r10);
    L_0x00a6:
        return r13;
    L_0x00a7:
        r2 = new android.content.res.Resources$NotFoundException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "Resource \"";
        r3.append(r5);
        r0 = r0.getResourceName(r4);
        r3.append(r0);
        r0 = "\" (";
        r3.append(r0);
        r0 = java.lang.Integer.toHexString(r18);
        r3.append(r0);
        r0 = ") is not a Font: ";
        r3.append(r0);
        r3.append(r1);
        r0 = r3.toString();
        r2.<init>(r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.ResourcesCompat.loadFont(android.content.Context, android.content.res.Resources, android.util.TypedValue, int, int, androidx.core.content.res.ResourcesCompat$FontCallback, android.os.Handler, boolean):android.graphics.Typeface");
    }
}

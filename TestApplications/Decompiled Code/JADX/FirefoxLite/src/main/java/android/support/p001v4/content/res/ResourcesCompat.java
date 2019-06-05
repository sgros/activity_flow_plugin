package android.support.p001v4.content.res;

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

/* renamed from: android.support.v4.content.res.ResourcesCompat */
public final class ResourcesCompat {

    /* renamed from: android.support.v4.content.res.ResourcesCompat$FontCallback */
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
        return ResourcesCompat.loadFont(context, i, typedValue, i2, fontCallback, null, true);
    }

    private static Typeface loadFont(Context context, int i, TypedValue typedValue, int i2, FontCallback fontCallback, Handler handler, boolean z) {
        Resources resources = context.getResources();
        resources.getValue(i, typedValue, true);
        Typeface loadFont = ResourcesCompat.loadFont(context, resources, typedValue, i, i2, fontCallback, handler, z);
        if (loadFont != null || fontCallback != null) {
            return loadFont;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Font resource ID #0x");
        stringBuilder.append(Integer.toHexString(i));
        stringBuilder.append(" could not be retrieved.");
        throw new NotFoundException(stringBuilder.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a7  */
    private static android.graphics.Typeface loadFont(android.content.Context r14, android.content.res.Resources r15, android.util.TypedValue r16, int r17, int r18, android.support.p001v4.content.res.ResourcesCompat.FontCallback r19, android.os.Handler r20, boolean r21) {
        /*
        r0 = r15;
        r1 = r16;
        r4 = r17;
        r5 = r18;
        r9 = r19;
        r10 = r20;
        r2 = r1.string;
        if (r2 == 0) goto L_0x00ab;
    L_0x000f:
        r1 = r1.string;
        r11 = r1.toString();
        r1 = "res/";
        r1 = r11.startsWith(r1);
        r12 = 0;
        r13 = -3;
        if (r1 != 0) goto L_0x0025;
    L_0x001f:
        if (r9 == 0) goto L_0x0024;
    L_0x0021:
        r9.callbackFailAsync(r13, r10);
    L_0x0024:
        return r12;
    L_0x0025:
        r1 = android.support.p001v4.graphics.TypefaceCompat.findFromCache(r15, r4, r5);
        if (r1 == 0) goto L_0x0031;
    L_0x002b:
        if (r9 == 0) goto L_0x0030;
    L_0x002d:
        r9.callbackSuccessAsync(r1, r10);
    L_0x0030:
        return r1;
    L_0x0031:
        r1 = r11.toLowerCase();	 Catch:{ XmlPullParserException -> 0x008e, IOException -> 0x0076 }
        r2 = ".xml";
        r1 = r1.endsWith(r2);	 Catch:{ XmlPullParserException -> 0x008e, IOException -> 0x0076 }
        if (r1 == 0) goto L_0x0065;
    L_0x003d:
        r1 = r15.getXml(r4);	 Catch:{ XmlPullParserException -> 0x008e, IOException -> 0x0076 }
        r2 = android.support.p001v4.content.res.FontResourcesParserCompat.parse(r1, r15);	 Catch:{ XmlPullParserException -> 0x008e, IOException -> 0x0076 }
        if (r2 != 0) goto L_0x0054;
    L_0x0047:
        r0 = "ResourcesCompat";
        r1 = "Failed to find font-family tag";
        android.util.Log.e(r0, r1);	 Catch:{ XmlPullParserException -> 0x008e, IOException -> 0x0076 }
        if (r9 == 0) goto L_0x0053;
    L_0x0050:
        r9.callbackFailAsync(r13, r10);	 Catch:{ XmlPullParserException -> 0x008e, IOException -> 0x0076 }
    L_0x0053:
        return r12;
    L_0x0054:
        r1 = r14;
        r3 = r15;
        r4 = r17;
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r0 = android.support.p001v4.graphics.TypefaceCompat.createFromResourcesFamilyXml(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ XmlPullParserException -> 0x008e, IOException -> 0x0076 }
        return r0;
    L_0x0065:
        r1 = r14;
        r0 = android.support.p001v4.graphics.TypefaceCompat.createFromResourcesFontFile(r14, r15, r4, r11, r5);	 Catch:{ XmlPullParserException -> 0x008e, IOException -> 0x0076 }
        if (r9 == 0) goto L_0x0075;
    L_0x006c:
        if (r0 == 0) goto L_0x0072;
    L_0x006e:
        r9.callbackSuccessAsync(r0, r10);	 Catch:{ XmlPullParserException -> 0x008e, IOException -> 0x0076 }
        goto L_0x0075;
    L_0x0072:
        r9.callbackFailAsync(r13, r10);	 Catch:{ XmlPullParserException -> 0x008e, IOException -> 0x0076 }
    L_0x0075:
        return r0;
    L_0x0076:
        r0 = move-exception;
        r1 = "ResourcesCompat";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Failed to read xml resource ";
        r2.append(r3);
        r2.append(r11);
        r2 = r2.toString();
        android.util.Log.e(r1, r2, r0);
        goto L_0x00a5;
    L_0x008e:
        r0 = move-exception;
        r1 = "ResourcesCompat";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Failed to parse xml resource ";
        r2.append(r3);
        r2.append(r11);
        r2 = r2.toString();
        android.util.Log.e(r1, r2, r0);
    L_0x00a5:
        if (r9 == 0) goto L_0x00aa;
    L_0x00a7:
        r9.callbackFailAsync(r13, r10);
    L_0x00aa:
        return r12;
    L_0x00ab:
        r2 = new android.content.res.Resources$NotFoundException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "Resource \"";
        r3.append(r5);
        r0 = r15.getResourceName(r4);
        r3.append(r0);
        r0 = "\" (";
        r3.append(r0);
        r0 = java.lang.Integer.toHexString(r17);
        r3.append(r0);
        r0 = ") is not a Font: ";
        r3.append(r0);
        r3.append(r1);
        r0 = r3.toString();
        r2.<init>(r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.content.res.ResourcesCompat.loadFont(android.content.Context, android.content.res.Resources, android.util.TypedValue, int, int, android.support.v4.content.res.ResourcesCompat$FontCallback, android.os.Handler, boolean):android.graphics.Typeface");
    }
}

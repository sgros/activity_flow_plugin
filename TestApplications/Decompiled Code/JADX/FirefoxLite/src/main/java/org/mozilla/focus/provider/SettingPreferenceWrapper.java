package org.mozilla.focus.provider;

import android.content.ContentResolver;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SettingPreferenceWrapper.kt */
public final class SettingPreferenceWrapper {
    private final ContentResolver resolver;

    public SettingPreferenceWrapper(ContentResolver contentResolver) {
        Intrinsics.checkParameterIsNotNull(contentResolver, "resolver");
        this.resolver = contentResolver;
    }

    public final float getFloat(String str, float f) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Object value = getValue("getFloat", str, Float.valueOf(f));
        if (value != null) {
            return ((Float) value).floatValue();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Float");
    }

    public final boolean getBoolean(String str, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Object value = getValue("getBoolean", str, Boolean.valueOf(z));
        if (value != null) {
            return ((Boolean) value).booleanValue();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0046  */
    private final java.lang.Object getValue(java.lang.String r8, java.lang.String r9, java.lang.Object r10) {
        /*
        r7 = this;
        r0 = org.mozilla.focus.provider.SettingContract.INSTANCE;
        r0 = r0.getAUTHORITY_URI();
        r2 = android.net.Uri.withAppendedPath(r0, r8);
        r8 = 2;
        r5 = new java.lang.String[r8];
        r8 = 0;
        r5[r8] = r9;
        r8 = java.lang.String.valueOf(r10);
        r9 = 1;
        r5[r9] = r8;
        r8 = 0;
        r9 = r8;
        r9 = (android.database.Cursor) r9;
        r1 = r7.resolver;	 Catch:{ all -> 0x0042 }
        r3 = 0;
        r4 = 0;
        r6 = 0;
        r0 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x0042 }
        if (r0 == 0) goto L_0x0038;
    L_0x0026:
        r9 = r0.getExtras();	 Catch:{ all -> 0x0036 }
        if (r9 == 0) goto L_0x0038;
    L_0x002c:
        r8 = "key";
        r8 = r9.get(r8);	 Catch:{ all -> 0x0036 }
        r9.clear();	 Catch:{ all -> 0x0036 }
        goto L_0x0038;
    L_0x0036:
        r8 = move-exception;
        goto L_0x0044;
    L_0x0038:
        if (r0 == 0) goto L_0x003d;
    L_0x003a:
        r0.close();
    L_0x003d:
        if (r8 == 0) goto L_0x0040;
    L_0x003f:
        goto L_0x0041;
    L_0x0040:
        r8 = r10;
    L_0x0041:
        return r8;
    L_0x0042:
        r8 = move-exception;
        r0 = r9;
    L_0x0044:
        if (r0 == 0) goto L_0x0049;
    L_0x0046:
        r0.close();
    L_0x0049:
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.provider.SettingPreferenceWrapper.getValue(java.lang.String, java.lang.String, java.lang.Object):java.lang.Object");
    }
}

package org.mozilla.rocket.content.data;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: ShoppingLink.kt */
public final class ShoppingLink {
    private final String image;
    private final String name;
    private final String source;
    private final String url;

    /* JADX WARNING: Missing block: B:10:0x002e, code skipped:
            if (kotlin.jvm.internal.Intrinsics.areEqual(r2.source, r3.source) != false) goto L_0x0033;
     */
    public boolean equals(java.lang.Object r3) {
        /*
        r2 = this;
        if (r2 == r3) goto L_0x0033;
    L_0x0002:
        r0 = r3 instanceof org.mozilla.rocket.content.data.ShoppingLink;
        if (r0 == 0) goto L_0x0031;
    L_0x0006:
        r3 = (org.mozilla.rocket.content.data.ShoppingLink) r3;
        r0 = r2.url;
        r1 = r3.url;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x0031;
    L_0x0012:
        r0 = r2.name;
        r1 = r3.name;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x0031;
    L_0x001c:
        r0 = r2.image;
        r1 = r3.image;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x0031;
    L_0x0026:
        r0 = r2.source;
        r3 = r3.source;
        r3 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r3);
        if (r3 == 0) goto L_0x0031;
    L_0x0030:
        goto L_0x0033;
    L_0x0031:
        r3 = 0;
        return r3;
    L_0x0033:
        r3 = 1;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.rocket.content.data.ShoppingLink.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        String str = this.url;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.name;
        hashCode = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.image;
        hashCode = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.source;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ShoppingLink(url=");
        stringBuilder.append(this.url);
        stringBuilder.append(", name=");
        stringBuilder.append(this.name);
        stringBuilder.append(", image=");
        stringBuilder.append(this.image);
        stringBuilder.append(", source=");
        stringBuilder.append(this.source);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public ShoppingLink(String str, String str2, String str3, String str4) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        Intrinsics.checkParameterIsNotNull(str2, "name");
        Intrinsics.checkParameterIsNotNull(str3, "image");
        Intrinsics.checkParameterIsNotNull(str4, "source");
        this.url = str;
        this.name = str2;
        this.image = str3;
        this.source = str4;
    }

    public final String getImage() {
        return this.image;
    }

    public final String getName() {
        return this.name;
    }

    public final String getSource() {
        return this.source;
    }

    public final String getUrl() {
        return this.url;
    }
}

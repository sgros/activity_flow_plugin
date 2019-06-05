package android.support.p001v4.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.compat.C0017R;
import android.support.p001v4.provider.FontRequest;
import android.util.Base64;
import android.util.TypedValue;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* renamed from: android.support.v4.content.res.FontResourcesParserCompat */
public class FontResourcesParserCompat {

    /* renamed from: android.support.v4.content.res.FontResourcesParserCompat$FamilyResourceEntry */
    public interface FamilyResourceEntry {
    }

    /* renamed from: android.support.v4.content.res.FontResourcesParserCompat$FontFileResourceEntry */
    public static final class FontFileResourceEntry {
        private final String mFileName;
        private boolean mItalic;
        private int mResourceId;
        private int mTtcIndex;
        private String mVariationSettings;
        private int mWeight;

        public FontFileResourceEntry(String str, int i, boolean z, String str2, int i2, int i3) {
            this.mFileName = str;
            this.mWeight = i;
            this.mItalic = z;
            this.mVariationSettings = str2;
            this.mTtcIndex = i2;
            this.mResourceId = i3;
        }

        public String getFileName() {
            return this.mFileName;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }

        public String getVariationSettings() {
            return this.mVariationSettings;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public int getResourceId() {
            return this.mResourceId;
        }
    }

    /* renamed from: android.support.v4.content.res.FontResourcesParserCompat$FontFamilyFilesResourceEntry */
    public static final class FontFamilyFilesResourceEntry implements FamilyResourceEntry {
        private final FontFileResourceEntry[] mEntries;

        public FontFamilyFilesResourceEntry(FontFileResourceEntry[] fontFileResourceEntryArr) {
            this.mEntries = fontFileResourceEntryArr;
        }

        public FontFileResourceEntry[] getEntries() {
            return this.mEntries;
        }
    }

    /* renamed from: android.support.v4.content.res.FontResourcesParserCompat$ProviderResourceEntry */
    public static final class ProviderResourceEntry implements FamilyResourceEntry {
        private final FontRequest mRequest;
        private final int mStrategy;
        private final int mTimeoutMs;

        public ProviderResourceEntry(FontRequest fontRequest, int i, int i2) {
            this.mRequest = fontRequest;
            this.mStrategy = i;
            this.mTimeoutMs = i2;
        }

        public FontRequest getRequest() {
            return this.mRequest;
        }

        public int getFetchStrategy() {
            return this.mStrategy;
        }

        public int getTimeout() {
            return this.mTimeoutMs;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0012  */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x000d  */
    public static android.support.p001v4.content.res.FontResourcesParserCompat.FamilyResourceEntry parse(org.xmlpull.v1.XmlPullParser r3, android.content.res.Resources r4) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
    L_0x0000:
        r0 = r3.next();
        r1 = 2;
        if (r0 == r1) goto L_0x000b;
    L_0x0007:
        r2 = 1;
        if (r0 == r2) goto L_0x000b;
    L_0x000a:
        goto L_0x0000;
    L_0x000b:
        if (r0 != r1) goto L_0x0012;
    L_0x000d:
        r3 = android.support.p001v4.content.res.FontResourcesParserCompat.readFamilies(r3, r4);
        return r3;
    L_0x0012:
        r3 = new org.xmlpull.v1.XmlPullParserException;
        r4 = "No start tag found";
        r3.<init>(r4);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.content.res.FontResourcesParserCompat.parse(org.xmlpull.v1.XmlPullParser, android.content.res.Resources):android.support.v4.content.res.FontResourcesParserCompat$FamilyResourceEntry");
    }

    private static FamilyResourceEntry readFamilies(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, null, "font-family");
        if (xmlPullParser.getName().equals("font-family")) {
            return FontResourcesParserCompat.readFamily(xmlPullParser, resources);
        }
        FontResourcesParserCompat.skip(xmlPullParser);
        return null;
    }

    private static FamilyResourceEntry readFamily(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), C0017R.styleable.FontFamily);
        String string = obtainAttributes.getString(C0017R.styleable.FontFamily_fontProviderAuthority);
        String string2 = obtainAttributes.getString(C0017R.styleable.FontFamily_fontProviderPackage);
        String string3 = obtainAttributes.getString(C0017R.styleable.FontFamily_fontProviderQuery);
        int resourceId = obtainAttributes.getResourceId(C0017R.styleable.FontFamily_fontProviderCerts, 0);
        int integer = obtainAttributes.getInteger(C0017R.styleable.FontFamily_fontProviderFetchStrategy, 1);
        int integer2 = obtainAttributes.getInteger(C0017R.styleable.FontFamily_fontProviderFetchTimeout, 500);
        obtainAttributes.recycle();
        if (string == null || string2 == null || string3 == null) {
            ArrayList arrayList = new ArrayList();
            while (xmlPullParser.next() != 3) {
                if (xmlPullParser.getEventType() == 2) {
                    if (xmlPullParser.getName().equals("font")) {
                        arrayList.add(FontResourcesParserCompat.readFont(xmlPullParser, resources));
                    } else {
                        FontResourcesParserCompat.skip(xmlPullParser);
                    }
                }
            }
            if (arrayList.isEmpty()) {
                return null;
            }
            return new FontFamilyFilesResourceEntry((FontFileResourceEntry[]) arrayList.toArray(new FontFileResourceEntry[arrayList.size()]));
        }
        while (xmlPullParser.next() != 3) {
            FontResourcesParserCompat.skip(xmlPullParser);
        }
        return new ProviderResourceEntry(new FontRequest(string, string2, string3, FontResourcesParserCompat.readCerts(resources, resourceId)), integer, integer2);
    }

    private static int getType(TypedArray typedArray, int i) {
        if (VERSION.SDK_INT >= 21) {
            return typedArray.getType(i);
        }
        TypedValue typedValue = new TypedValue();
        typedArray.getValue(i, typedValue);
        return typedValue.type;
    }

    public static List<List<byte[]>> readCerts(Resources resources, int i) {
        if (i == 0) {
            return Collections.emptyList();
        }
        TypedArray obtainTypedArray = resources.obtainTypedArray(i);
        try {
            if (obtainTypedArray.length() == 0) {
                List<List<byte[]>> emptyList = Collections.emptyList();
                return emptyList;
            }
            ArrayList arrayList = new ArrayList();
            if (FontResourcesParserCompat.getType(obtainTypedArray, 0) == 1) {
                for (i = 0; i < obtainTypedArray.length(); i++) {
                    int resourceId = obtainTypedArray.getResourceId(i, 0);
                    if (resourceId != 0) {
                        arrayList.add(FontResourcesParserCompat.toByteArrayList(resources.getStringArray(resourceId)));
                    }
                }
            } else {
                arrayList.add(FontResourcesParserCompat.toByteArrayList(resources.getStringArray(i)));
            }
            obtainTypedArray.recycle();
            return arrayList;
        } finally {
            obtainTypedArray.recycle();
        }
    }

    private static List<byte[]> toByteArrayList(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (String decode : strArr) {
            arrayList.add(Base64.decode(decode, 0));
        }
        return arrayList;
    }

    private static FontFileResourceEntry readFont(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), C0017R.styleable.FontFamilyFont);
        int i = obtainAttributes.getInt(obtainAttributes.hasValue(C0017R.styleable.FontFamilyFont_fontWeight) ? C0017R.styleable.FontFamilyFont_fontWeight : C0017R.styleable.FontFamilyFont_android_fontWeight, 400);
        boolean z = 1 == obtainAttributes.getInt(obtainAttributes.hasValue(C0017R.styleable.FontFamilyFont_fontStyle) ? C0017R.styleable.FontFamilyFont_fontStyle : C0017R.styleable.FontFamilyFont_android_fontStyle, 0);
        int i2 = obtainAttributes.hasValue(C0017R.styleable.FontFamilyFont_ttcIndex) ? C0017R.styleable.FontFamilyFont_ttcIndex : C0017R.styleable.FontFamilyFont_android_ttcIndex;
        String string = obtainAttributes.getString(obtainAttributes.hasValue(C0017R.styleable.FontFamilyFont_fontVariationSettings) ? C0017R.styleable.FontFamilyFont_fontVariationSettings : C0017R.styleable.FontFamilyFont_android_fontVariationSettings);
        int i3 = obtainAttributes.getInt(i2, 0);
        i2 = obtainAttributes.hasValue(C0017R.styleable.FontFamilyFont_font) ? C0017R.styleable.FontFamilyFont_font : C0017R.styleable.FontFamilyFont_android_font;
        int resourceId = obtainAttributes.getResourceId(i2, 0);
        String string2 = obtainAttributes.getString(i2);
        obtainAttributes.recycle();
        while (xmlPullParser.next() != 3) {
            FontResourcesParserCompat.skip(xmlPullParser);
        }
        return new FontFileResourceEntry(string2, i, z, string, i3, resourceId);
    }

    private static void skip(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int i = 1;
        while (i > 0) {
            switch (xmlPullParser.next()) {
                case 2:
                    i++;
                    break;
                case 3:
                    i--;
                    break;
                default:
                    break;
            }
        }
    }
}

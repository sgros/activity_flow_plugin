package android.support.p001v4.content.res;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: android.support.v4.content.res.TypedArrayUtils */
public class TypedArrayUtils {
    public static boolean hasAttribute(XmlPullParser xmlPullParser, String str) {
        return xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", str) != null;
    }

    public static float getNamedFloat(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i, float f) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, str)) {
            return typedArray.getFloat(i, f);
        }
        return f;
    }

    public static boolean getNamedBoolean(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i, boolean z) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, str)) {
            return typedArray.getBoolean(i, z);
        }
        return z;
    }

    public static int getNamedInt(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i, int i2) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, str)) {
            return typedArray.getInt(i, i2);
        }
        return i2;
    }

    public static int getNamedColor(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i, int i2) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, str)) {
            return typedArray.getColor(i, i2);
        }
        return i2;
    }

    public static ComplexColorCompat getNamedComplexColor(TypedArray typedArray, XmlPullParser xmlPullParser, Theme theme, String str, int i, int i2) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, str)) {
            TypedValue typedValue = new TypedValue();
            typedArray.getValue(i, typedValue);
            if (typedValue.type >= 28 && typedValue.type <= 31) {
                return ComplexColorCompat.from(typedValue.data);
            }
            ComplexColorCompat inflate = ComplexColorCompat.inflate(typedArray.getResources(), typedArray.getResourceId(i, 0), theme);
            if (inflate != null) {
                return inflate;
            }
        }
        return ComplexColorCompat.from(i2);
    }

    public static int getNamedResourceId(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i, int i2) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, str)) {
            return typedArray.getResourceId(i, i2);
        }
        return i2;
    }

    public static String getNamedString(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, str)) {
            return typedArray.getString(i);
        }
        return null;
    }

    public static TypedValue peekNamedValue(TypedArray typedArray, XmlPullParser xmlPullParser, String str, int i) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, str)) {
            return typedArray.peekValue(i);
        }
        return null;
    }

    public static TypedArray obtainAttributes(Resources resources, Theme theme, AttributeSet attributeSet, int[] iArr) {
        if (theme == null) {
            return resources.obtainAttributes(attributeSet, iArr);
        }
        return theme.obtainStyledAttributes(attributeSet, iArr, 0, 0);
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.content.res;

import android.util.AttributeSet;
import android.content.res.Resources;
import android.util.TypedValue;
import android.content.res.Resources$Theme;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.TypedArray;

public class TypedArrayUtils
{
    public static boolean getNamedBoolean(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n, final boolean b) {
        if (!hasAttribute(xmlPullParser, s)) {
            return b;
        }
        return typedArray.getBoolean(n, b);
    }
    
    public static int getNamedColor(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n, final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getColor(n, n2);
    }
    
    public static ComplexColorCompat getNamedComplexColor(final TypedArray typedArray, final XmlPullParser xmlPullParser, final Resources$Theme resources$Theme, final String s, final int n, final int n2) {
        if (hasAttribute(xmlPullParser, s)) {
            final TypedValue typedValue = new TypedValue();
            typedArray.getValue(n, typedValue);
            final int type = typedValue.type;
            if (type >= 28 && type <= 31) {
                return ComplexColorCompat.from(typedValue.data);
            }
            final ComplexColorCompat inflate = ComplexColorCompat.inflate(typedArray.getResources(), typedArray.getResourceId(n, 0), resources$Theme);
            if (inflate != null) {
                return inflate;
            }
        }
        return ComplexColorCompat.from(n2);
    }
    
    public static float getNamedFloat(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n, final float n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getFloat(n, n2);
    }
    
    public static int getNamedInt(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n, final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getInt(n, n2);
    }
    
    public static int getNamedResourceId(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n, final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getResourceId(n, n2);
    }
    
    public static String getNamedString(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n) {
        if (!hasAttribute(xmlPullParser, s)) {
            return null;
        }
        return typedArray.getString(n);
    }
    
    public static boolean hasAttribute(final XmlPullParser xmlPullParser, final String s) {
        return xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", s) != null;
    }
    
    public static TypedArray obtainAttributes(final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final int[] array) {
        if (resources$Theme == null) {
            return resources.obtainAttributes(set, array);
        }
        return resources$Theme.obtainStyledAttributes(set, array, 0, 0);
    }
    
    public static TypedValue peekNamedValue(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n) {
        if (!hasAttribute(xmlPullParser, s)) {
            return null;
        }
        return typedArray.peekValue(n);
    }
}

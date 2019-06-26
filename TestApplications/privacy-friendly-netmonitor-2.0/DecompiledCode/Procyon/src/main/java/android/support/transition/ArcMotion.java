// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.graphics.Path;
import android.content.res.TypedArray;
import android.support.v4.content.res.TypedArrayUtils;
import org.xmlpull.v1.XmlPullParser;
import android.util.AttributeSet;
import android.content.Context;

public class ArcMotion extends PathMotion
{
    private static final float DEFAULT_MAX_ANGLE_DEGREES = 70.0f;
    private static final float DEFAULT_MAX_TANGENT;
    private static final float DEFAULT_MIN_ANGLE_DEGREES = 0.0f;
    private float mMaximumAngle;
    private float mMaximumTangent;
    private float mMinimumHorizontalAngle;
    private float mMinimumHorizontalTangent;
    private float mMinimumVerticalAngle;
    private float mMinimumVerticalTangent;
    
    static {
        DEFAULT_MAX_TANGENT = (float)Math.tan(Math.toRadians(35.0));
    }
    
    public ArcMotion() {
        this.mMinimumHorizontalAngle = 0.0f;
        this.mMinimumVerticalAngle = 0.0f;
        this.mMaximumAngle = 70.0f;
        this.mMinimumHorizontalTangent = 0.0f;
        this.mMinimumVerticalTangent = 0.0f;
        this.mMaximumTangent = ArcMotion.DEFAULT_MAX_TANGENT;
    }
    
    public ArcMotion(final Context context, final AttributeSet set) {
        super(context, set);
        this.mMinimumHorizontalAngle = 0.0f;
        this.mMinimumVerticalAngle = 0.0f;
        this.mMaximumAngle = 70.0f;
        this.mMinimumHorizontalTangent = 0.0f;
        this.mMinimumVerticalTangent = 0.0f;
        this.mMaximumTangent = ArcMotion.DEFAULT_MAX_TANGENT;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, Styleable.ARC_MOTION);
        final XmlPullParser xmlPullParser = (XmlPullParser)set;
        this.setMinimumVerticalAngle(TypedArrayUtils.getNamedFloat(obtainStyledAttributes, xmlPullParser, "minimumVerticalAngle", 1, 0.0f));
        this.setMinimumHorizontalAngle(TypedArrayUtils.getNamedFloat(obtainStyledAttributes, xmlPullParser, "minimumHorizontalAngle", 0, 0.0f));
        this.setMaximumAngle(TypedArrayUtils.getNamedFloat(obtainStyledAttributes, xmlPullParser, "maximumAngle", 2, 70.0f));
        obtainStyledAttributes.recycle();
    }
    
    private static float toTangent(final float n) {
        if (n >= 0.0f && n <= 90.0f) {
            return (float)Math.tan(Math.toRadians(n / 2.0f));
        }
        throw new IllegalArgumentException("Arc must be between 0 and 90 degrees");
    }
    
    public float getMaximumAngle() {
        return this.mMaximumAngle;
    }
    
    public float getMinimumHorizontalAngle() {
        return this.mMinimumHorizontalAngle;
    }
    
    public float getMinimumVerticalAngle() {
        return this.mMinimumVerticalAngle;
    }
    
    @Override
    public Path getPath(final float n, final float n2, final float n3, final float n4) {
        final Path path = new Path();
        path.moveTo(n, n2);
        final float a = n3 - n;
        final float a2 = n4 - n2;
        final float n5 = a * a + a2 * a2;
        final float n6 = (n + n3) / 2.0f;
        final float n7 = (n2 + n4) / 2.0f;
        final float n8 = 0.25f * n5;
        final boolean b = n2 > n4;
        float n9;
        float n10;
        float n11;
        if (Math.abs(a) < Math.abs(a2)) {
            final float abs = Math.abs(n5 / (a2 * 2.0f));
            if (b) {
                n9 = abs + n4;
                n10 = n3;
            }
            else {
                n9 = abs + n2;
                n10 = n;
            }
            n11 = this.mMinimumVerticalTangent * n8 * this.mMinimumVerticalTangent;
        }
        else {
            final float n12 = n5 / (a * 2.0f);
            if (b) {
                n9 = n2;
                n10 = n12 + n;
            }
            else {
                n10 = n3 - n12;
                n9 = n4;
            }
            n11 = this.mMinimumHorizontalTangent * n8 * this.mMinimumHorizontalTangent;
        }
        final float n13 = n6 - n10;
        final float n14 = n7 - n9;
        final float n15 = n13 * n13 + n14 * n14;
        final float n16 = n8 * this.mMaximumTangent * this.mMaximumTangent;
        if (n15 >= n11) {
            if (n15 > n16) {
                n11 = n16;
            }
            else {
                n11 = 0.0f;
            }
        }
        float n17 = n10;
        float n18 = n9;
        if (n11 != 0.0f) {
            final float n19 = (float)Math.sqrt(n11 / n15);
            n17 = (n10 - n6) * n19 + n6;
            n18 = n7 + n19 * (n9 - n7);
        }
        path.cubicTo((n + n17) / 2.0f, (n2 + n18) / 2.0f, (n17 + n3) / 2.0f, (n18 + n4) / 2.0f, n3, n4);
        return path;
    }
    
    public void setMaximumAngle(final float mMaximumAngle) {
        this.mMaximumAngle = mMaximumAngle;
        this.mMaximumTangent = toTangent(mMaximumAngle);
    }
    
    public void setMinimumHorizontalAngle(final float mMinimumHorizontalAngle) {
        this.mMinimumHorizontalAngle = mMinimumHorizontalAngle;
        this.mMinimumHorizontalTangent = toTangent(mMinimumHorizontalAngle);
    }
    
    public void setMinimumVerticalAngle(final float mMinimumVerticalAngle) {
        this.mMinimumVerticalAngle = mMinimumVerticalAngle;
        this.mMinimumVerticalTangent = toTangent(mMinimumVerticalAngle);
    }
}

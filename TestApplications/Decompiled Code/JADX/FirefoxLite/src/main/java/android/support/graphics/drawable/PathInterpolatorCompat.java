package android.support.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.p001v4.content.res.TypedArrayUtils;
import android.support.p001v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import org.xmlpull.v1.XmlPullParser;

public class PathInterpolatorCompat implements Interpolator {
    /* renamed from: mX */
    private float[] f14mX;
    /* renamed from: mY */
    private float[] f15mY;

    public PathInterpolatorCompat(Context context, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        this(context.getResources(), context.getTheme(), attributeSet, xmlPullParser);
    }

    public PathInterpolatorCompat(Resources resources, Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
        parseInterpolatorFromTypeArray(obtainAttributes, xmlPullParser);
        obtainAttributes.recycle();
    }

    private void parseInterpolatorFromTypeArray(TypedArray typedArray, XmlPullParser xmlPullParser) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "pathData")) {
            String namedString = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "pathData", 4);
            Path createPathFromPathData = PathParser.createPathFromPathData(namedString);
            if (createPathFromPathData != null) {
                initPath(createPathFromPathData);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The path is null, which is created from ");
            stringBuilder.append(namedString);
            throw new InflateException(stringBuilder.toString());
        } else if (!TypedArrayUtils.hasAttribute(xmlPullParser, "controlX1")) {
            throw new InflateException("pathInterpolator requires the controlX1 attribute");
        } else if (TypedArrayUtils.hasAttribute(xmlPullParser, "controlY1")) {
            float namedFloat = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlX1", 0, 0.0f);
            float namedFloat2 = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlY1", 1, 0.0f);
            boolean hasAttribute = TypedArrayUtils.hasAttribute(xmlPullParser, "controlX2");
            if (hasAttribute != TypedArrayUtils.hasAttribute(xmlPullParser, "controlY2")) {
                throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
            } else if (hasAttribute) {
                initCubic(namedFloat, namedFloat2, TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlX2", 2, 0.0f), TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlY2", 3, 0.0f));
            } else {
                initQuad(namedFloat, namedFloat2);
            }
        } else {
            throw new InflateException("pathInterpolator requires the controlY1 attribute");
        }
    }

    private void initQuad(float f, float f2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(f, f2, 1.0f, 1.0f);
        initPath(path);
    }

    private void initCubic(float f, float f2, float f3, float f4) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(f, f2, f3, f4, 1.0f, 1.0f);
        initPath(path);
    }

    private void initPath(Path path) {
        int i = 0;
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        int min = Math.min(3000, ((int) (length / 0.002f)) + 1);
        StringBuilder stringBuilder;
        if (min > 0) {
            int i2;
            this.f14mX = new float[min];
            this.f15mY = new float[min];
            float[] fArr = new float[2];
            for (i2 = 0; i2 < min; i2++) {
                pathMeasure.getPosTan((((float) i2) * length) / ((float) (min - 1)), fArr, null);
                this.f14mX[i2] = fArr[0];
                this.f15mY[i2] = fArr[1];
            }
            if (((double) Math.abs(this.f14mX[0])) <= 1.0E-5d && ((double) Math.abs(this.f15mY[0])) <= 1.0E-5d) {
                int i3 = min - 1;
                if (((double) Math.abs(this.f14mX[i3] - 1.0f)) <= 1.0E-5d && ((double) Math.abs(this.f15mY[i3] - 1.0f)) <= 1.0E-5d) {
                    int i4 = 0;
                    float f = 0.0f;
                    while (i < min) {
                        i2 = i4 + 1;
                        length = this.f14mX[i4];
                        if (length >= f) {
                            this.f14mX[i] = length;
                            i++;
                            f = length;
                            i4 = i2;
                        } else {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("The Path cannot loop back on itself, x :");
                            stringBuilder.append(length);
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                    }
                    if (pathMeasure.nextContour()) {
                        throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
                    }
                    return;
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("The Path must start at (0,0) and end at (1,1) start: ");
            stringBuilder2.append(this.f14mX[0]);
            stringBuilder2.append(",");
            stringBuilder2.append(this.f15mY[0]);
            stringBuilder2.append(" end:");
            min--;
            stringBuilder2.append(this.f14mX[min]);
            stringBuilder2.append(",");
            stringBuilder2.append(this.f15mY[min]);
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("The Path has a invalid length ");
        stringBuilder.append(length);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public float getInterpolation(float f) {
        if (f <= 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        int i = 0;
        int length = this.f14mX.length - 1;
        while (length - i > 1) {
            int i2 = (i + length) / 2;
            if (f < this.f14mX[i2]) {
                length = i2;
            } else {
                i = i2;
            }
        }
        float f2 = this.f14mX[length] - this.f14mX[i];
        if (f2 == 0.0f) {
            return this.f15mY[i];
        }
        f = (f - this.f14mX[i]) / f2;
        float f3 = this.f15mY[i];
        return f3 + (f * (this.f15mY[length] - f3));
    }
}

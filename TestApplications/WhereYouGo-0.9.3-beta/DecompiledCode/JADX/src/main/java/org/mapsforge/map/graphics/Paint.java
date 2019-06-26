package org.mapsforge.map.graphics;

public interface Paint {
    void destroy();

    int getColor();

    int getTextHeight(String str);

    int getTextWidth(String str);

    void setBitmapShader(Bitmap bitmap);

    void setColor(int i);

    void setDashPathEffect(float[] fArr);

    void setStrokeCap(Cap cap);

    void setStrokeWidth(float f);

    void setStyle(Style style);

    void setTextAlign(Align align);

    void setTextSize(float f);

    void setTypeface(FontFamily fontFamily, FontStyle fontStyle);
}

// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.util.JsonToken;
import java.util.ArrayList;
import android.util.JsonReader;
import com.airbnb.lottie.utils.MiscUtils;
import android.graphics.Color;
import java.util.List;
import com.airbnb.lottie.model.content.GradientColor;

public class GradientColorParser implements ValueParser<GradientColor>
{
    private int colorPoints;
    
    public GradientColorParser(final int colorPoints) {
        this.colorPoints = colorPoints;
    }
    
    private void addOpacityStopsToGradientIfNeeded(final GradientColor gradientColor, final List<Float> list) {
        int n = this.colorPoints * 4;
        if (list.size() <= n) {
            return;
        }
        final int n2 = (list.size() - n) / 2;
        final double[] array = new double[n2];
        final double[] array2 = new double[n2];
        final int n3 = 0;
        int n4 = 0;
        int i;
        while (true) {
            i = n3;
            if (n >= list.size()) {
                break;
            }
            if (n % 2 == 0) {
                array[n4] = list.get(n);
            }
            else {
                array2[n4] = list.get(n);
                ++n4;
            }
            ++n;
        }
        while (i < gradientColor.getSize()) {
            final int n5 = gradientColor.getColors()[i];
            gradientColor.getColors()[i] = Color.argb(this.getOpacityAtPosition(gradientColor.getPositions()[i], array, array2), Color.red(n5), Color.green(n5), Color.blue(n5));
            ++i;
        }
    }
    
    private int getOpacityAtPosition(double lerp, final double[] array, final double[] array2) {
        for (int i = 1; i < array.length; ++i) {
            final int n = i - 1;
            final double n2 = array[n];
            final double n3 = array[i];
            if (array[i] >= lerp) {
                lerp = (lerp - n2) / (n3 - n2);
                lerp = MiscUtils.lerp(array2[n], array2[i], lerp);
                return (int)(lerp * 255.0);
            }
        }
        lerp = array2[array2.length - 1];
        return (int)(lerp * 255.0);
    }
    
    @Override
    public GradientColor parse(final JsonReader jsonReader, final float n) throws IOException {
        final ArrayList<Object> list = new ArrayList<Object>();
        final JsonToken peek = jsonReader.peek();
        final JsonToken begin_ARRAY = JsonToken.BEGIN_ARRAY;
        final int n2 = 0;
        final boolean b = peek == begin_ARRAY;
        if (b) {
            jsonReader.beginArray();
        }
        while (jsonReader.hasNext()) {
            list.add((float)jsonReader.nextDouble());
        }
        if (b) {
            jsonReader.endArray();
        }
        if (this.colorPoints == -1) {
            this.colorPoints = list.size() / 4;
        }
        final int colorPoints = this.colorPoints;
        final float[] array = new float[colorPoints];
        final int[] array2 = new int[colorPoints];
        final int n3 = 0;
        int n4 = 0;
        int i = n2;
        int n5 = n3;
        while (i < this.colorPoints * 4) {
            final int n6 = i / 4;
            final double v = list.get(i);
            final int n7 = i % 4;
            if (n7 != 0) {
                if (n7 != 1) {
                    if (n7 != 2) {
                        if (n7 == 3) {
                            Double.isNaN(v);
                            array2[n6] = Color.argb(255, n5, n4, (int)(v * 255.0));
                        }
                    }
                    else {
                        Double.isNaN(v);
                        n4 = (int)(v * 255.0);
                    }
                }
                else {
                    Double.isNaN(v);
                    n5 = (int)(v * 255.0);
                }
            }
            else {
                array[n6] = (float)v;
            }
            ++i;
        }
        final GradientColor gradientColor = new GradientColor(array, array2);
        this.addOpacityStopsToGradientIfNeeded(gradientColor, (List<Float>)list);
        return gradientColor;
    }
}

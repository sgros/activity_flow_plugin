package org.mapsforge.map.rendertheme;

import java.io.InputStream;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Paint;

public interface GraphicAdapter {

    public enum Color {
        BLACK,
        CYAN,
        TRANSPARENT,
        WHITE
    }

    Bitmap decodeStream(InputStream inputStream);

    int getColor(Color color);

    Paint getPaint();

    int parseColor(String str);
}

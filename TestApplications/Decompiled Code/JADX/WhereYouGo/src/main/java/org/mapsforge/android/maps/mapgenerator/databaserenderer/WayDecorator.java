package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import java.util.List;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Paint;

final class WayDecorator {
    private static final int DISTANCE_BETWEEN_SYMBOLS = 200;
    private static final int DISTANCE_BETWEEN_WAY_NAMES = 500;
    private static final int SEGMENT_SAFETY_DISTANCE = 30;

    static void renderSymbol(Bitmap symbolBitmap, boolean alignCenter, boolean repeatSymbol, Point[][] coordinates, List<SymbolContainer> waySymbols) {
        int skipPixels = 30;
        double previousX = coordinates[0][0].f68x;
        double previousY = coordinates[0][0].f69y;
        for (int i = 1; i < coordinates[0].length; i++) {
            double currentX = coordinates[0][i].f68x;
            double currentY = coordinates[0][i].f69y;
            double diffX = currentX - previousX;
            double diffY = currentY - previousY;
            float segmentLengthRemaining = (float) Math.sqrt((diffX * diffX) + (diffY * diffY));
            while (segmentLengthRemaining - ((float) skipPixels) > 30.0f) {
                float segmentSkipPercentage = ((float) skipPixels) / segmentLengthRemaining;
                previousX += ((double) segmentSkipPercentage) * diffX;
                previousY += ((double) segmentSkipPercentage) * diffY;
                Bitmap bitmap = symbolBitmap;
                waySymbols.add(new SymbolContainer(bitmap, new Point(previousX, previousY), alignCenter, (float) Math.toDegrees(Math.atan2(currentY - previousY, currentX - previousX))));
                if (repeatSymbol) {
                    diffX = currentX - previousX;
                    diffY = currentY - previousY;
                    segmentLengthRemaining -= (float) skipPixels;
                    skipPixels = DISTANCE_BETWEEN_SYMBOLS;
                } else {
                    return;
                }
            }
            skipPixels = (int) (((float) skipPixels) - segmentLengthRemaining);
            if (skipPixels < 30) {
                skipPixels = 30;
            }
            previousX = currentX;
            previousY = currentY;
        }
    }

    static void renderText(String textKey, Paint fill, Paint stroke, Point[][] coordinates, List<WayTextContainer> wayNames) {
        int wayNameWidth = fill.getTextWidth(textKey) + 10;
        int skipPixels = 0;
        double previousX = coordinates[0][0].f68x;
        double previousY = coordinates[0][0].f69y;
        for (int i = 1; i < coordinates[0].length; i++) {
            double currentX = coordinates[0][i].f68x;
            double currentY = coordinates[0][i].f69y;
            double diffX = currentX - previousX;
            double diffY = currentY - previousY;
            double segmentLengthInPixel = Math.sqrt((diffX * diffX) + (diffY * diffY));
            if (skipPixels > 0) {
                skipPixels = (int) (((double) skipPixels) - segmentLengthInPixel);
            } else if (segmentLengthInPixel > ((double) wayNameWidth)) {
                double[] wayNamePath = new double[4];
                if (previousX <= currentX) {
                    wayNamePath[0] = previousX;
                    wayNamePath[1] = previousY;
                    wayNamePath[2] = currentX;
                    wayNamePath[3] = currentY;
                } else {
                    wayNamePath[0] = currentX;
                    wayNamePath[1] = currentY;
                    wayNamePath[2] = previousX;
                    wayNamePath[3] = previousY;
                }
                wayNames.add(new WayTextContainer(wayNamePath, textKey, fill));
                if (stroke != null) {
                    wayNames.add(new WayTextContainer(wayNamePath, textKey, stroke));
                }
                skipPixels = 500;
            }
            previousX = currentX;
            previousY = currentY;
        }
    }

    private WayDecorator() {
        throw new IllegalStateException();
    }
}

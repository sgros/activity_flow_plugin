// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.map.graphics.Paint;
import android.graphics.Rect;

class PointTextContainer
{
    final Rect boundary;
    final Paint paintBack;
    final Paint paintFront;
    SymbolContainer symbol;
    final String text;
    double x;
    double y;
    
    PointTextContainer(final String text, final double x, final double y, final Paint paintFront) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.paintFront = paintFront;
        this.paintBack = null;
        this.boundary = new Rect(0, 0, paintFront.getTextWidth(text), paintFront.getTextHeight(text));
    }
    
    PointTextContainer(final String text, final double x, final double y, final Paint paintFront, final Paint paintBack) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.paintFront = paintFront;
        this.paintBack = paintBack;
        if (paintBack != null) {
            paintBack.getTextHeight(text);
            paintBack.getTextWidth(text);
            this.boundary = new Rect(0, 0, paintBack.getTextWidth(text), paintBack.getTextHeight(text));
        }
        else {
            this.boundary = new Rect(0, 0, paintFront.getTextWidth(text), paintFront.getTextHeight(text));
        }
    }
    
    PointTextContainer(final String text, final double x, final double y, final Paint paintFront, final Paint paintBack, final SymbolContainer symbol) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.paintFront = paintFront;
        this.paintBack = paintBack;
        this.symbol = symbol;
        if (paintBack != null) {
            this.boundary = new Rect(0, 0, paintBack.getTextWidth(text), paintBack.getTextHeight(text));
        }
        else {
            this.boundary = new Rect(0, 0, paintFront.getTextWidth(text), paintFront.getTextHeight(text));
        }
    }
}

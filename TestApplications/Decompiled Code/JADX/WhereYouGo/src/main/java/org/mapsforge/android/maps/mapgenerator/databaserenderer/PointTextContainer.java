package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Rect;
import org.mapsforge.map.graphics.Paint;

class PointTextContainer {
    final Rect boundary;
    final Paint paintBack;
    final Paint paintFront;
    SymbolContainer symbol;
    final String text;
    /* renamed from: x */
    double f66x;
    /* renamed from: y */
    double f67y;

    PointTextContainer(String text, double x, double y, Paint paintFront) {
        this.text = text;
        this.f66x = x;
        this.f67y = y;
        this.paintFront = paintFront;
        this.paintBack = null;
        this.boundary = new Rect(0, 0, paintFront.getTextWidth(text), paintFront.getTextHeight(text));
    }

    PointTextContainer(String text, double x, double y, Paint paintFront, Paint paintBack) {
        this.text = text;
        this.f66x = x;
        this.f67y = y;
        this.paintFront = paintFront;
        this.paintBack = paintBack;
        if (paintBack != null) {
            paintBack.getTextHeight(text);
            paintBack.getTextWidth(text);
            this.boundary = new Rect(0, 0, paintBack.getTextWidth(text), paintBack.getTextHeight(text));
            return;
        }
        this.boundary = new Rect(0, 0, paintFront.getTextWidth(text), paintFront.getTextHeight(text));
    }

    PointTextContainer(String text, double x, double y, Paint paintFront, Paint paintBack, SymbolContainer symbol) {
        this.text = text;
        this.f66x = x;
        this.f67y = y;
        this.paintFront = paintFront;
        this.paintBack = paintBack;
        this.symbol = symbol;
        if (paintBack != null) {
            this.boundary = new Rect(0, 0, paintBack.getTextWidth(text), paintBack.getTextHeight(text));
        } else {
            this.boundary = new Rect(0, 0, paintFront.getTextWidth(text), paintFront.getTextHeight(text));
        }
    }
}

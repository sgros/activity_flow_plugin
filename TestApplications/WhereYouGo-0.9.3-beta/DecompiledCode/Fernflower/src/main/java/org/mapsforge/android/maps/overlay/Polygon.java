package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

public class Polygon implements OverlayItem {
   private Paint paintFill;
   private Paint paintStroke;
   private final List polygonalChains;

   public Polygon(Collection var1, Paint var2, Paint var3) {
      if (var1 == null) {
         this.polygonalChains = Collections.synchronizedList(new ArrayList());
      } else {
         this.polygonalChains = Collections.synchronizedList(new ArrayList(var1));
      }

      this.paintFill = var2;
      this.paintStroke = var3;
   }

   public boolean draw(BoundingBox param1, byte param2, Canvas param3, Point param4) {
      // $FF: Couldn't be decompiled
   }

   public Paint getPaintFill() {
      synchronized(this){}

      Paint var1;
      try {
         var1 = this.paintFill;
      } finally {
         ;
      }

      return var1;
   }

   public Paint getPaintStroke() {
      synchronized(this){}

      Paint var1;
      try {
         var1 = this.paintStroke;
      } finally {
         ;
      }

      return var1;
   }

   public List getPolygonalChains() {
      // $FF: Couldn't be decompiled
   }

   public void setPaintFill(Paint var1) {
      synchronized(this){}

      try {
         this.paintFill = var1;
      } finally {
         ;
      }

   }

   public void setPaintStroke(Paint var1) {
      synchronized(this){}

      try {
         this.paintStroke = var1;
      } finally {
         ;
      }

   }
}

package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.rendertheme.RenderCallback;

public class Circle implements RenderInstruction {
   private final Paint fill;
   private final int level;
   private final float radius;
   private float renderRadius;
   private final boolean scaleRadius;
   private final Paint stroke;
   private final float strokeWidth;

   Circle(CircleBuilder var1) {
      this.fill = var1.fill;
      this.level = var1.level;
      this.radius = var1.radius;
      this.scaleRadius = var1.scaleRadius;
      this.stroke = var1.stroke;
      this.strokeWidth = var1.strokeWidth;
      if (!this.scaleRadius) {
         this.renderRadius = this.radius;
         if (this.stroke != null) {
            this.stroke.setStrokeWidth(this.strokeWidth);
         }
      }

   }

   public void destroy() {
   }

   public void renderNode(RenderCallback var1, List var2) {
      var1.renderPointOfInterestCircle(this.renderRadius, this.fill, this.stroke, this.level);
   }

   public void renderWay(RenderCallback var1, List var2) {
   }

   public void scaleStrokeWidth(float var1) {
      if (this.scaleRadius) {
         this.renderRadius = this.radius * var1;
         if (this.stroke != null) {
            this.stroke.setStrokeWidth(this.strokeWidth * var1);
         }
      }

   }

   public void scaleTextSize(float var1) {
   }
}

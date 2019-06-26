package org.telegram.ui.Components.Crop;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class CropState {
   private float height;
   private Matrix matrix;
   private float minimumScale;
   private float rotation;
   private float scale;
   private float[] values;
   private float width;
   private float x;
   private float y;

   public CropState(Bitmap var1) {
      this.width = (float)var1.getWidth();
      this.height = (float)var1.getHeight();
      this.x = 0.0F;
      this.y = 0.0F;
      this.scale = 1.0F;
      this.rotation = 0.0F;
      this.matrix = new Matrix();
      this.values = new float[9];
   }

   private void updateValues() {
      this.matrix.getValues(this.values);
   }

   public void getConcatMatrix(Matrix var1) {
      var1.postConcat(this.matrix);
   }

   public float getHeight() {
      return this.height;
   }

   public Matrix getMatrix() {
      Matrix var1 = new Matrix();
      var1.set(this.matrix);
      return var1;
   }

   public float getRotation() {
      return this.rotation;
   }

   public float getScale() {
      return this.scale;
   }

   public float getWidth() {
      return this.width;
   }

   public float getX() {
      this.updateValues();
      float[] var1 = this.values;
      Matrix var2 = this.matrix;
      return var1[2];
   }

   public float getY() {
      this.updateValues();
      float[] var1 = this.values;
      Matrix var2 = this.matrix;
      return var1[5];
   }

   public void reset(CropAreaView var1) {
      this.matrix.reset();
      this.x = 0.0F;
      this.y = 0.0F;
      this.rotation = 0.0F;
      this.minimumScale = var1.getCropWidth() / this.width;
      this.scale = this.minimumScale;
      Matrix var3 = this.matrix;
      float var2 = this.scale;
      var3.postScale(var2, var2);
   }

   public void rotate(float var1, float var2, float var3) {
      this.rotation += var1;
      this.matrix.postRotate(var1, var2, var3);
   }

   public void scale(float var1, float var2, float var3) {
      this.scale *= var1;
      this.matrix.postScale(var1, var1, var2, var3);
   }

   public void translate(float var1, float var2) {
      this.x += var1;
      this.y += var2;
      this.matrix.postTranslate(var1, var2);
   }
}

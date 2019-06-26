package com.googlecode.mp4parser.util;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;

public class Matrix {
   public static final Matrix ROTATE_0 = new Matrix(1.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D);
   public static final Matrix ROTATE_180 = new Matrix(-1.0D, 0.0D, 0.0D, -1.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D);
   public static final Matrix ROTATE_270 = new Matrix(0.0D, -1.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D);
   public static final Matrix ROTATE_90 = new Matrix(0.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D);
   double a;
   double b;
   double c;
   double d;
   double tx;
   double ty;
   double u;
   double v;
   double w;

   public Matrix(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      this.u = var9;
      this.v = var11;
      this.w = var13;
      this.a = var1;
      this.b = var3;
      this.c = var5;
      this.d = var7;
      this.tx = var15;
      this.ty = var17;
   }

   public static Matrix fromByteBuffer(ByteBuffer var0) {
      return fromFileOrder(IsoTypeReader.readFixedPoint1616(var0), IsoTypeReader.readFixedPoint1616(var0), IsoTypeReader.readFixedPoint0230(var0), IsoTypeReader.readFixedPoint1616(var0), IsoTypeReader.readFixedPoint1616(var0), IsoTypeReader.readFixedPoint0230(var0), IsoTypeReader.readFixedPoint1616(var0), IsoTypeReader.readFixedPoint1616(var0), IsoTypeReader.readFixedPoint0230(var0));
   }

   public static Matrix fromFileOrder(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      return new Matrix(var0, var2, var6, var8, var4, var10, var16, var12, var14);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && Matrix.class == var1.getClass()) {
         Matrix var2 = (Matrix)var1;
         if (Double.compare(var2.a, this.a) != 0) {
            return false;
         } else if (Double.compare(var2.b, this.b) != 0) {
            return false;
         } else if (Double.compare(var2.c, this.c) != 0) {
            return false;
         } else if (Double.compare(var2.d, this.d) != 0) {
            return false;
         } else if (Double.compare(var2.tx, this.tx) != 0) {
            return false;
         } else if (Double.compare(var2.ty, this.ty) != 0) {
            return false;
         } else if (Double.compare(var2.u, this.u) != 0) {
            return false;
         } else if (Double.compare(var2.v, this.v) != 0) {
            return false;
         } else {
            return Double.compare(var2.w, this.w) == 0;
         }
      } else {
         return false;
      }
   }

   public void getContent(ByteBuffer var1) {
      IsoTypeWriter.writeFixedPoint1616(var1, this.a);
      IsoTypeWriter.writeFixedPoint1616(var1, this.b);
      IsoTypeWriter.writeFixedPoint0230(var1, this.u);
      IsoTypeWriter.writeFixedPoint1616(var1, this.c);
      IsoTypeWriter.writeFixedPoint1616(var1, this.d);
      IsoTypeWriter.writeFixedPoint0230(var1, this.v);
      IsoTypeWriter.writeFixedPoint1616(var1, this.tx);
      IsoTypeWriter.writeFixedPoint1616(var1, this.ty);
      IsoTypeWriter.writeFixedPoint0230(var1, this.w);
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.u);
      int var3 = (int)(var1 ^ var1 >>> 32);
      var1 = Double.doubleToLongBits(this.v);
      int var4 = (int)(var1 ^ var1 >>> 32);
      var1 = Double.doubleToLongBits(this.w);
      int var5 = (int)(var1 ^ var1 >>> 32);
      var1 = Double.doubleToLongBits(this.a);
      int var6 = (int)(var1 ^ var1 >>> 32);
      var1 = Double.doubleToLongBits(this.b);
      int var7 = (int)(var1 ^ var1 >>> 32);
      var1 = Double.doubleToLongBits(this.c);
      int var8 = (int)(var1 ^ var1 >>> 32);
      var1 = Double.doubleToLongBits(this.d);
      int var9 = (int)(var1 ^ var1 >>> 32);
      var1 = Double.doubleToLongBits(this.tx);
      int var10 = (int)(var1 ^ var1 >>> 32);
      var1 = Double.doubleToLongBits(this.ty);
      return (((((((var3 * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var7) * 31 + var8) * 31 + var9) * 31 + var10) * 31 + (int)(var1 ^ var1 >>> 32);
   }

   public String toString() {
      if (this.equals(ROTATE_0)) {
         return "Rotate 0°";
      } else if (this.equals(ROTATE_90)) {
         return "Rotate 90°";
      } else if (this.equals(ROTATE_180)) {
         return "Rotate 180°";
      } else if (this.equals(ROTATE_270)) {
         return "Rotate 270°";
      } else {
         StringBuilder var1 = new StringBuilder("Matrix{u=");
         var1.append(this.u);
         var1.append(", v=");
         var1.append(this.v);
         var1.append(", w=");
         var1.append(this.w);
         var1.append(", a=");
         var1.append(this.a);
         var1.append(", b=");
         var1.append(this.b);
         var1.append(", c=");
         var1.append(this.c);
         var1.append(", d=");
         var1.append(this.d);
         var1.append(", tx=");
         var1.append(this.tx);
         var1.append(", ty=");
         var1.append(this.ty);
         var1.append('}');
         return var1.toString();
      }
   }
}

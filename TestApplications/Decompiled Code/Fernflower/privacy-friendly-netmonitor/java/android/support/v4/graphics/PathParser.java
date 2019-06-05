package android.support.v4.graphics;

import android.graphics.Path;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class PathParser {
   private static final String LOGTAG = "PathParser";

   private static void addNode(ArrayList var0, char var1, float[] var2) {
      var0.add(new PathParser.PathDataNode(var1, var2));
   }

   public static boolean canMorph(PathParser.PathDataNode[] var0, PathParser.PathDataNode[] var1) {
      if (var0 != null && var1 != null) {
         if (var0.length != var1.length) {
            return false;
         } else {
            for(int var2 = 0; var2 < var0.length; ++var2) {
               if (var0[var2].mType != var1[var2].mType || var0[var2].mParams.length != var1[var2].mParams.length) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   static float[] copyOfRange(float[] var0, int var1, int var2) {
      if (var1 > var2) {
         throw new IllegalArgumentException();
      } else {
         int var3 = var0.length;
         if (var1 >= 0 && var1 <= var3) {
            var2 -= var1;
            var3 = Math.min(var2, var3 - var1);
            float[] var4 = new float[var2];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         } else {
            throw new ArrayIndexOutOfBoundsException();
         }
      }
   }

   public static PathParser.PathDataNode[] createNodesFromPathData(String var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         int var2 = 1;

         int var3;
         for(var3 = 0; var2 < var0.length(); var3 = var2++) {
            var2 = nextStart(var0, var2);
            String var4 = var0.substring(var3, var2).trim();
            if (var4.length() > 0) {
               float[] var5 = getFloats(var4);
               addNode(var1, var4.charAt(0), var5);
            }
         }

         if (var2 - var3 == 1 && var3 < var0.length()) {
            addNode(var1, var0.charAt(var3), new float[0]);
         }

         return (PathParser.PathDataNode[])var1.toArray(new PathParser.PathDataNode[var1.size()]);
      }
   }

   public static Path createPathFromPathData(String var0) {
      Path var1 = new Path();
      PathParser.PathDataNode[] var2 = createNodesFromPathData(var0);
      if (var2 != null) {
         try {
            PathParser.PathDataNode.nodesToPath(var2, var1);
            return var1;
         } catch (RuntimeException var3) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Error in parsing ");
            var4.append(var0);
            throw new RuntimeException(var4.toString(), var3);
         }
      } else {
         return null;
      }
   }

   public static PathParser.PathDataNode[] deepCopyNodes(PathParser.PathDataNode[] var0) {
      if (var0 == null) {
         return null;
      } else {
         PathParser.PathDataNode[] var1 = new PathParser.PathDataNode[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = new PathParser.PathDataNode(var0[var2]);
         }

         return var1;
      }
   }

   private static void extract(String var0, int var1, PathParser.ExtractFloatResult var2) {
      var2.mEndWithNegOrDot = false;
      int var3 = var1;
      boolean var4 = false;
      boolean var6 = var4;
      boolean var7 = var4;

      for(boolean var5 = var4; var3 < var0.length(); ++var3) {
         label52: {
            char var8 = var0.charAt(var3);
            if (var8 != ' ') {
               if (var8 == 'E' || var8 == 'e') {
                  var5 = true;
                  break label52;
               }

               switch(var8) {
               case ',':
                  break;
               case '-':
                  if (var3 != var1 && !var5) {
                     var2.mEndWithNegOrDot = true;
                     break;
                  }
               default:
                  var5 = false;
                  break label52;
               case '.':
                  if (!var7) {
                     var5 = false;
                     var7 = true;
                     break label52;
                  }

                  var2.mEndWithNegOrDot = true;
               }
            }

            var5 = false;
            var6 = true;
         }

         if (var6) {
            break;
         }
      }

      var2.mEndPosition = var3;
   }

   private static float[] getFloats(String var0) {
      if (var0.charAt(0) != 'z' && var0.charAt(0) != 'Z') {
         NumberFormatException var10000;
         label61: {
            float[] var1;
            PathParser.ExtractFloatResult var2;
            int var3;
            boolean var10001;
            try {
               var1 = new float[var0.length()];
               var2 = new PathParser.ExtractFloatResult();
               var3 = var0.length();
            } catch (NumberFormatException var12) {
               var10000 = var12;
               var10001 = false;
               break label61;
            }

            int var4 = 1;
            int var5 = 0;

            while(var4 < var3) {
               int var6;
               try {
                  extract(var0, var4, var2);
                  var6 = var2.mEndPosition;
               } catch (NumberFormatException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label61;
               }

               int var7 = var5;
               if (var4 < var6) {
                  try {
                     var1[var5] = Float.parseFloat(var0.substring(var4, var6));
                  } catch (NumberFormatException var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label61;
                  }

                  var7 = var5 + 1;
               }

               label48: {
                  try {
                     if (var2.mEndWithNegOrDot) {
                        break label48;
                     }
                  } catch (NumberFormatException var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label61;
                  }

                  var4 = var6 + 1;
                  var5 = var7;
                  continue;
               }

               var4 = var6;
               var5 = var7;
            }

            try {
               float[] var15 = copyOfRange(var1, 0, var5);
               return var15;
            } catch (NumberFormatException var8) {
               var10000 = var8;
               var10001 = false;
            }
         }

         NumberFormatException var13 = var10000;
         StringBuilder var14 = new StringBuilder();
         var14.append("error in parsing \"");
         var14.append(var0);
         var14.append("\"");
         throw new RuntimeException(var14.toString(), var13);
      } else {
         return new float[0];
      }
   }

   private static int nextStart(String var0, int var1) {
      while(var1 < var0.length()) {
         char var2 = var0.charAt(var1);
         if (((var2 - 65) * (var2 - 90) <= 0 || (var2 - 97) * (var2 - 122) <= 0) && var2 != 'e' && var2 != 'E') {
            return var1;
         }

         ++var1;
      }

      return var1;
   }

   public static void updateNodes(PathParser.PathDataNode[] var0, PathParser.PathDataNode[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var0[var2].mType = (char)var1[var2].mType;

         for(int var3 = 0; var3 < var1[var2].mParams.length; ++var3) {
            var0[var2].mParams[var3] = var1[var2].mParams[var3];
         }
      }

   }

   private static class ExtractFloatResult {
      int mEndPosition;
      boolean mEndWithNegOrDot;

      ExtractFloatResult() {
      }
   }

   public static class PathDataNode {
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public float[] mParams;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public char mType;

      PathDataNode(char var1, float[] var2) {
         this.mType = (char)var1;
         this.mParams = var2;
      }

      PathDataNode(PathParser.PathDataNode var1) {
         this.mType = (char)var1.mType;
         this.mParams = PathParser.copyOfRange(var1.mParams, 0, var1.mParams.length);
      }

      private static void addCommand(Path var0, float[] var1, char var2, char var3, float[] var4) {
         float var5;
         float var7;
         float var8;
         float var9;
         float var10;
         float var11;
         float var12;
         float var13;
         float var14;
         byte var15;
         label165: {
            label164: {
               var5 = var1[0];
               float var6 = var1[1];
               var7 = var1[2];
               var8 = var1[3];
               var9 = var1[4];
               var10 = var1[5];
               var11 = var5;
               var12 = var6;
               var13 = var7;
               var14 = var8;
               switch(var3) {
               case 'A':
               case 'a':
                  var15 = 7;
                  break;
               case 'C':
               case 'c':
                  var15 = 6;
                  break;
               case 'H':
               case 'V':
               case 'h':
               case 'v':
                  var15 = 1;
                  var11 = var5;
                  var12 = var6;
                  var13 = var7;
                  var14 = var8;
                  break label165;
               case 'L':
               case 'M':
               case 'T':
               case 'l':
               case 'm':
               case 't':
                  break label164;
               case 'Q':
               case 'S':
               case 'q':
               case 's':
                  var15 = 4;
                  var11 = var5;
                  var12 = var6;
                  var13 = var7;
                  var14 = var8;
                  break label165;
               case 'Z':
               case 'z':
                  var0.close();
                  var0.moveTo(var9, var10);
                  var11 = var9;
                  var13 = var9;
                  var12 = var10;
                  var14 = var10;
                  break label164;
               default:
                  var14 = var8;
                  var13 = var7;
                  var12 = var6;
                  var11 = var5;
                  break label164;
               }

               var14 = var8;
               var13 = var7;
               var12 = var6;
               var11 = var5;
               break label165;
            }

            var15 = 2;
         }

         byte var16 = 0;
         char var17 = var2;

         for(int var22 = var16; var22 < var4.length; var17 = var3) {
            label148: {
               label147: {
                  label146: {
                     var5 = 0.0F;
                     var8 = 0.0F;
                     int var18;
                     int var19;
                     boolean var20;
                     boolean var21;
                     int var23;
                     int var24;
                     switch(var3) {
                     case 'A':
                        var23 = var22 + 5;
                        var14 = var4[var23];
                        var19 = var22 + 6;
                        var13 = var4[var19];
                        var7 = var4[var22 + 0];
                        var5 = var4[var22 + 1];
                        var8 = var4[var22 + 2];
                        if (var4[var22 + 3] != 0.0F) {
                           var20 = true;
                        } else {
                           var20 = false;
                        }

                        if (var4[var22 + 4] != 0.0F) {
                           var21 = true;
                        } else {
                           var21 = false;
                        }

                        drawArc(var0, var11, var12, var14, var13, var7, var5, var8, var20, var21);
                        var11 = var4[var23];
                        var12 = var4[var19];
                        break label146;
                     case 'C':
                        var8 = var4[var22 + 0];
                        var13 = var4[var22 + 1];
                        var19 = var22 + 2;
                        var14 = var4[var19];
                        var18 = var22 + 3;
                        var12 = var4[var18];
                        var23 = var22 + 4;
                        var11 = var4[var23];
                        var24 = var22 + 5;
                        var0.cubicTo(var8, var13, var14, var12, var11, var4[var24]);
                        var11 = var4[var23];
                        var12 = var4[var24];
                        var13 = var4[var19];
                        var14 = var4[var18];
                        break label148;
                     case 'H':
                        var24 = var22 + 0;
                        var0.lineTo(var4[var24], var12);
                        var11 = var4[var24];
                        break label148;
                     case 'L':
                        var23 = var22 + 0;
                        var12 = var4[var23];
                        var24 = var22 + 1;
                        var0.lineTo(var12, var4[var24]);
                        var11 = var4[var23];
                        var12 = var4[var24];
                        break label148;
                     case 'M':
                        var23 = var22 + 0;
                        var11 = var4[var23];
                        var19 = var22 + 1;
                        var12 = var4[var19];
                        if (var22 > 0) {
                           var0.lineTo(var4[var23], var4[var19]);
                        } else {
                           var0.moveTo(var4[var23], var4[var19]);
                           var10 = var12;
                           var9 = var11;
                        }
                        break label148;
                     case 'Q':
                        var18 = var22 + 0;
                        var11 = var4[var18];
                        var23 = var22 + 1;
                        var13 = var4[var23];
                        var19 = var22 + 2;
                        var12 = var4[var19];
                        var24 = var22 + 3;
                        var0.quadTo(var11, var13, var12, var4[var24]);
                        var14 = var4[var18];
                        var13 = var4[var23];
                        var11 = var4[var19];
                        var12 = var4[var24];
                        break;
                     case 'S':
                        if (var17 == 'c' || var17 == 's' || var17 == 'C' || var17 == 'S') {
                           var11 = 2.0F * var11 - var13;
                           var12 = 2.0F * var12 - var14;
                        }

                        var18 = var22 + 0;
                        var14 = var4[var18];
                        var19 = var22 + 1;
                        var13 = var4[var19];
                        var24 = var22 + 2;
                        var8 = var4[var24];
                        var23 = var22 + 3;
                        var0.cubicTo(var11, var12, var14, var13, var8, var4[var23]);
                        var14 = var4[var18];
                        var13 = var4[var19];
                        var11 = var4[var24];
                        var12 = var4[var23];
                        break;
                     case 'T':
                        label113: {
                           if (var17 != 'q' && var17 != 't' && var17 != 'Q') {
                              var11 = var11;
                              var12 = var12;
                              if (var17 != 'T') {
                                 break label113;
                              }
                           }

                           var12 = 2.0F * var12 - var14;
                           var11 = 2.0F * var11 - var13;
                        }

                        var24 = var22 + 0;
                        var13 = var4[var24];
                        var23 = var22 + 1;
                        var0.quadTo(var11, var12, var13, var4[var23]);
                        var8 = var4[var24];
                        var5 = var4[var23];
                        var13 = var11;
                        var14 = var12;
                        var12 = var5;
                        var11 = var8;
                        break label148;
                     case 'V':
                        var24 = var22 + 0;
                        var0.lineTo(var11, var4[var24]);
                        var12 = var4[var24];
                        break label148;
                     case 'a':
                        var24 = var22 + 5;
                        var14 = var4[var24];
                        var23 = var22 + 6;
                        var7 = var4[var23];
                        var5 = var4[var22 + 0];
                        var8 = var4[var22 + 1];
                        var13 = var4[var22 + 2];
                        if (var4[var22 + 3] != 0.0F) {
                           var20 = true;
                        } else {
                           var20 = false;
                        }

                        if (var4[var22 + 4] != 0.0F) {
                           var21 = true;
                        } else {
                           var21 = false;
                        }

                        drawArc(var0, var11, var12, var14 + var11, var7 + var12, var5, var8, var13, var20, var21);
                        var11 += var4[var24];
                        var12 += var4[var23];
                        break label146;
                     case 'c':
                        var13 = var4[var22 + 0];
                        var5 = var4[var22 + 1];
                        var23 = var22 + 2;
                        var14 = var4[var23];
                        var18 = var22 + 3;
                        var8 = var4[var18];
                        var24 = var22 + 4;
                        var7 = var4[var24];
                        var19 = var22 + 5;
                        var0.rCubicTo(var13, var5, var14, var8, var7, var4[var19]);
                        var14 = var4[var23] + var11;
                        var13 = var4[var18] + var12;
                        var11 += var4[var24];
                        var12 += var4[var19];
                        break label147;
                     case 'h':
                        var24 = var22 + 0;
                        var0.rLineTo(var4[var24], 0.0F);
                        var11 += var4[var24];
                        break label148;
                     case 'l':
                        var23 = var22 + 0;
                        var8 = var4[var23];
                        var24 = var22 + 1;
                        var0.rLineTo(var8, var4[var24]);
                        var11 += var4[var23];
                        var12 += var4[var24];
                        break label148;
                     case 'm':
                        var23 = var22 + 0;
                        var11 += var4[var23];
                        var24 = var22 + 1;
                        var12 += var4[var24];
                        if (var22 > 0) {
                           var0.rLineTo(var4[var23], var4[var24]);
                        } else {
                           var0.rMoveTo(var4[var23], var4[var24]);
                           var10 = var12;
                           var9 = var11;
                        }
                        break label148;
                     case 'q':
                        var24 = var22 + 0;
                        var8 = var4[var24];
                        var23 = var22 + 1;
                        var14 = var4[var23];
                        var18 = var22 + 2;
                        var13 = var4[var18];
                        var19 = var22 + 3;
                        var0.rQuadTo(var8, var14, var13, var4[var19]);
                        var14 = var4[var24] + var11;
                        var13 = var4[var23] + var12;
                        var11 += var4[var18];
                        var12 += var4[var19];
                        break label147;
                     case 's':
                        if (var17 != 'c' && var17 != 's' && var17 != 'C' && var17 != 'S') {
                           var14 = 0.0F;
                           var13 = var5;
                        } else {
                           var14 = var12 - var14;
                           var13 = var11 - var13;
                        }

                        var23 = var22 + 0;
                        var5 = var4[var23];
                        var18 = var22 + 1;
                        var7 = var4[var18];
                        var19 = var22 + 2;
                        var8 = var4[var19];
                        var24 = var22 + 3;
                        var0.rCubicTo(var13, var14, var5, var7, var8, var4[var24]);
                        var14 = var4[var23] + var11;
                        var13 = var4[var18] + var12;
                        var11 += var4[var19];
                        var12 += var4[var24];
                        break label147;
                     case 't':
                        if (var17 != 'q' && var17 != 't' && var17 != 'Q' && var17 != 'T') {
                           var14 = 0.0F;
                           var13 = var8;
                        } else {
                           var13 = var11 - var13;
                           var14 = var12 - var14;
                        }

                        var24 = var22 + 0;
                        var8 = var4[var24];
                        var23 = var22 + 1;
                        var0.rQuadTo(var13, var14, var8, var4[var23]);
                        var8 = var11 + var4[var24];
                        var5 = var12 + var4[var23];
                        var14 += var12;
                        var13 += var11;
                        var12 = var5;
                        var11 = var8;
                        break label148;
                     case 'v':
                        var24 = var22 + 0;
                        var0.rLineTo(0.0F, var4[var24]);
                        var12 += var4[var24];
                     default:
                        break label148;
                     }

                     var8 = var13;
                     var13 = var14;
                     var14 = var8;
                     break label148;
                  }

                  var14 = var12;
                  var13 = var11;
                  break label148;
               }

               var8 = var14;
               var14 = var13;
               var13 = var8;
            }

            var22 += var15;
         }

         var1[0] = var11;
         var1[1] = var12;
         var1[2] = var13;
         var1[3] = var14;
         var1[4] = var9;
         var1[5] = var10;
      }

      private static void arcToBezier(Path var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
         int var19 = (int)Math.ceil(Math.abs(var17 * 4.0D / 3.141592653589793D));
         double var20 = Math.cos(var13);
         double var22 = Math.sin(var13);
         double var24 = Math.cos(var15);
         double var26 = Math.sin(var15);
         var13 = -var5;
         double var28 = var13 * var20;
         double var30 = var7 * var22;
         var13 *= var22;
         double var32 = var7 * var20;
         double var34 = var17 / (double)var19;
         int var36 = 0;
         var17 = var26 * var13 + var24 * var32;
         var26 = var28 * var26 - var30 * var24;
         var24 = var11;
         var11 = var9;
         double var37 = var15;
         var7 = var22;
         var9 = var20;
         var20 = var34;

         for(var15 = var32; var36 < var19; var37 = var32) {
            var32 = var37 + var20;
            double var39 = Math.sin(var32);
            double var41 = Math.cos(var32);
            var22 = var1 + var5 * var9 * var41 - var30 * var39;
            double var43 = var3 + var5 * var7 * var41 + var15 * var39;
            var34 = var28 * var39 - var30 * var41;
            var41 = var39 * var13 + var41 * var15;
            var37 = var32 - var37;
            var39 = Math.tan(var37 / 2.0D);
            var37 = Math.sin(var37) * (Math.sqrt(4.0D + 3.0D * var39 * var39) - 1.0D) / 3.0D;
            var0.rLineTo(0.0F, 0.0F);
            var0.cubicTo((float)(var11 + var26 * var37), (float)(var24 + var17 * var37), (float)(var22 - var37 * var34), (float)(var43 - var37 * var41), (float)var22, (float)var43);
            ++var36;
            var24 = var43;
            var11 = var22;
            var17 = var41;
            var26 = var34;
         }

      }

      private static void drawArc(Path var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, boolean var8, boolean var9) {
         double var10 = Math.toRadians((double)var7);
         double var12 = Math.cos(var10);
         double var14 = Math.sin(var10);
         double var16 = (double)var1;
         double var18 = (double)var2;
         double var20 = (double)var5;
         double var22 = (var16 * var12 + var18 * var14) / var20;
         double var24 = (double)(-var1);
         double var26 = (double)var6;
         double var28 = (var24 * var14 + var18 * var12) / var26;
         double var30 = (double)var3;
         var24 = (double)var4;
         double var32 = (var30 * var12 + var24 * var14) / var20;
         double var34 = ((double)(-var3) * var14 + var24 * var12) / var26;
         double var36 = var22 - var32;
         double var38 = var28 - var34;
         var30 = (var22 + var32) / 2.0D;
         var24 = (var28 + var34) / 2.0D;
         double var40 = var36 * var36 + var38 * var38;
         if (var40 == 0.0D) {
            Log.w("PathParser", " Points are coincident");
         } else {
            double var42 = 1.0D / var40 - 0.25D;
            if (var42 < 0.0D) {
               StringBuilder var44 = new StringBuilder();
               var44.append("Points are too far apart ");
               var44.append(var40);
               Log.w("PathParser", var44.toString());
               float var45 = (float)(Math.sqrt(var40) / 1.99999D);
               drawArc(var0, var1, var2, var3, var4, var5 * var45, var6 * var45, var7, var8, var9);
            } else {
               var40 = Math.sqrt(var42);
               var36 *= var40;
               var38 = var40 * var38;
               if (var8 == var9) {
                  var30 -= var38;
                  var24 += var36;
               } else {
                  var30 += var38;
                  var24 -= var36;
               }

               var28 = Math.atan2(var28 - var24, var22 - var30);
               var32 = Math.atan2(var34 - var24, var32 - var30) - var28;
               if (var32 >= 0.0D) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               var22 = var32;
               if (var9 != var8) {
                  if (var32 > 0.0D) {
                     var22 = var32 - 6.283185307179586D;
                  } else {
                     var22 = var32 + 6.283185307179586D;
                  }
               }

               var30 *= var20;
               var24 *= var26;
               arcToBezier(var0, var30 * var12 - var24 * var14, var30 * var14 + var24 * var12, var20, var26, var16, var18, var10, var28, var22);
            }
         }
      }

      public static void nodesToPath(PathParser.PathDataNode[] var0, Path var1) {
         float[] var2 = new float[6];
         byte var3 = 109;
         int var4 = 0;

         char var6;
         for(char var5 = (char)var3; var4 < var0.length; var5 = var6) {
            addCommand(var1, var2, var5, var0[var4].mType, var0[var4].mParams);
            var6 = var0[var4].mType;
            ++var4;
         }

      }

      public void interpolatePathDataNode(PathParser.PathDataNode var1, PathParser.PathDataNode var2, float var3) {
         for(int var4 = 0; var4 < var1.mParams.length; ++var4) {
            this.mParams[var4] = var1.mParams[var4] * (1.0F - var3) + var2.mParams[var4] * var3;
         }

      }
   }
}

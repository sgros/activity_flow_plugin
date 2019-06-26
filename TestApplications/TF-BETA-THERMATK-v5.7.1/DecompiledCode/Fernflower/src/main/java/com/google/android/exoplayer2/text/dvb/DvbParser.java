package com.google.android.exoplayer2.text.dvb;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Region.Op;
import android.util.SparseArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class DvbParser {
   private static final byte[] defaultMap2To4 = new byte[]{0, 7, 8, 15};
   private static final byte[] defaultMap2To8 = new byte[]{0, 119, -120, -1};
   private static final byte[] defaultMap4To8 = new byte[]{0, 17, 34, 51, 68, 85, 102, 119, -120, -103, -86, -69, -52, -35, -18, -1};
   private Bitmap bitmap;
   private final Canvas canvas;
   private final DvbParser.ClutDefinition defaultClutDefinition;
   private final DvbParser.DisplayDefinition defaultDisplayDefinition;
   private final Paint defaultPaint = new Paint();
   private final Paint fillRegionPaint;
   private final DvbParser.SubtitleService subtitleService;

   public DvbParser(int var1, int var2) {
      this.defaultPaint.setStyle(Style.FILL_AND_STROKE);
      this.defaultPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
      this.defaultPaint.setPathEffect((PathEffect)null);
      this.fillRegionPaint = new Paint();
      this.fillRegionPaint.setStyle(Style.FILL);
      this.fillRegionPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
      this.fillRegionPaint.setPathEffect((PathEffect)null);
      this.canvas = new Canvas();
      this.defaultDisplayDefinition = new DvbParser.DisplayDefinition(719, 575, 0, 719, 0, 575);
      this.defaultClutDefinition = new DvbParser.ClutDefinition(0, generateDefault2BitClutEntries(), generateDefault4BitClutEntries(), generateDefault8BitClutEntries());
      this.subtitleService = new DvbParser.SubtitleService(var1, var2);
   }

   private static byte[] buildClutMapTable(int var0, int var1, ParsableBitArray var2) {
      byte[] var3 = new byte[var0];

      for(int var4 = 0; var4 < var0; ++var4) {
         var3[var4] = (byte)((byte)var2.readBits(var1));
      }

      return var3;
   }

   private static int[] generateDefault2BitClutEntries() {
      return new int[]{0, -1, -16777216, -8421505};
   }

   private static int[] generateDefault4BitClutEntries() {
      int[] var0 = new int[16];
      var0[0] = 0;

      for(int var1 = 1; var1 < var0.length; ++var1) {
         if (var1 < 8) {
            short var2;
            if ((var1 & 1) != 0) {
               var2 = 255;
            } else {
               var2 = 0;
            }

            short var3;
            if ((var1 & 2) != 0) {
               var3 = 255;
            } else {
               var3 = 0;
            }

            short var4;
            if ((var1 & 4) != 0) {
               var4 = 255;
            } else {
               var4 = 0;
            }

            var0[var1] = getColor(255, var2, var3, var4);
         } else {
            byte var7 = 127;
            byte var5;
            if ((var1 & 1) != 0) {
               var5 = 127;
            } else {
               var5 = 0;
            }

            byte var6;
            if ((var1 & 2) != 0) {
               var6 = 127;
            } else {
               var6 = 0;
            }

            if ((var1 & 4) == 0) {
               var7 = 0;
            }

            var0[var1] = getColor(255, var5, var6, var7);
         }
      }

      return var0;
   }

   private static int[] generateDefault8BitClutEntries() {
      int[] var0 = new int[256];
      var0[0] = 0;

      for(int var1 = 0; var1 < var0.length; ++var1) {
         short var2 = 255;
         short var4;
         if (var1 < 8) {
            short var3;
            if ((var1 & 1) != 0) {
               var3 = 255;
            } else {
               var3 = 0;
            }

            if ((var1 & 2) != 0) {
               var4 = 255;
            } else {
               var4 = 0;
            }

            if ((var1 & 4) == 0) {
               var2 = 0;
            }

            var0[var1] = getColor(63, var3, var4, var2);
         } else {
            int var8 = var1 & 136;
            var4 = 170;
            byte var10 = 85;
            byte var6;
            byte var9;
            short var12;
            short var13;
            if (var8 != 0) {
               if (var8 != 8) {
                  byte var11 = 43;
                  byte var5;
                  byte var7;
                  if (var8 != 128) {
                     if (var8 == 136) {
                        if ((var1 & 1) != 0) {
                           var9 = 43;
                        } else {
                           var9 = 0;
                        }

                        if ((var1 & 16) != 0) {
                           var5 = 85;
                        } else {
                           var5 = 0;
                        }

                        if ((var1 & 2) != 0) {
                           var6 = 43;
                        } else {
                           var6 = 0;
                        }

                        if ((var1 & 32) != 0) {
                           var7 = 85;
                        } else {
                           var7 = 0;
                        }

                        if ((var1 & 4) == 0) {
                           var11 = 0;
                        }

                        if ((var1 & 64) == 0) {
                           var10 = 0;
                        }

                        var0[var1] = getColor(255, var9 + var5, var6 + var7, var11 + var10);
                     }
                  } else {
                     if ((var1 & 1) != 0) {
                        var9 = 43;
                     } else {
                        var9 = 0;
                     }

                     if ((var1 & 16) != 0) {
                        var5 = 85;
                     } else {
                        var5 = 0;
                     }

                     if ((var1 & 2) != 0) {
                        var6 = 43;
                     } else {
                        var6 = 0;
                     }

                     if ((var1 & 32) != 0) {
                        var7 = 85;
                     } else {
                        var7 = 0;
                     }

                     if ((var1 & 4) == 0) {
                        var11 = 0;
                     }

                     if ((var1 & 64) == 0) {
                        var10 = 0;
                     }

                     var0[var1] = getColor(255, var9 + 127 + var5, var6 + 127 + var7, var11 + 127 + var10);
                  }
               } else {
                  if ((var1 & 1) != 0) {
                     var9 = 85;
                  } else {
                     var9 = 0;
                  }

                  if ((var1 & 16) != 0) {
                     var12 = 170;
                  } else {
                     var12 = 0;
                  }

                  if ((var1 & 2) != 0) {
                     var6 = 85;
                  } else {
                     var6 = 0;
                  }

                  if ((var1 & 32) != 0) {
                     var13 = 170;
                  } else {
                     var13 = 0;
                  }

                  if ((var1 & 4) == 0) {
                     var10 = 0;
                  }

                  if ((var1 & 64) == 0) {
                     var4 = 0;
                  }

                  var0[var1] = getColor(127, var9 + var12, var6 + var13, var10 + var4);
               }
            } else {
               if ((var1 & 1) != 0) {
                  var9 = 85;
               } else {
                  var9 = 0;
               }

               if ((var1 & 16) != 0) {
                  var12 = 170;
               } else {
                  var12 = 0;
               }

               if ((var1 & 2) != 0) {
                  var6 = 85;
               } else {
                  var6 = 0;
               }

               if ((var1 & 32) != 0) {
                  var13 = 170;
               } else {
                  var13 = 0;
               }

               if ((var1 & 4) == 0) {
                  var10 = 0;
               }

               if ((var1 & 64) == 0) {
                  var4 = 0;
               }

               var0[var1] = getColor(255, var9 + var12, var6 + var13, var10 + var4);
            }
         }
      }

      return var0;
   }

   private static int getColor(int var0, int var1, int var2, int var3) {
      return var0 << 24 | var1 << 16 | var2 << 8 | var3;
   }

   private static int paint2BitPixelCodeString(ParsableBitArray var0, int[] var1, byte[] var2, int var3, int var4, Paint var5, Canvas var6) {
      boolean var7 = false;
      int var8 = var3;
      boolean var11 = var7;

      while(true) {
         int var10;
         label59: {
            int var12 = var0.readBits(2);
            boolean var9;
            if (var12 == 0) {
               label58: {
                  if (var0.readBit()) {
                     var10 = var0.readBits(3) + 3;
                     var12 = var0.readBits(2);
                  } else {
                     if (var0.readBit()) {
                        var12 = 0;
                        break label58;
                     }

                     var12 = var0.readBits(2);
                     if (var12 == 0) {
                        var3 = 0;
                        var10 = 0;
                        var7 = true;
                        break label59;
                     }

                     if (var12 == 1) {
                        var7 = var11;
                        var3 = 0;
                        var10 = 2;
                        break label59;
                     }

                     if (var12 != 2) {
                        if (var12 != 3) {
                           var7 = var11;
                           var3 = 0;
                           var10 = 0;
                           break label59;
                        }

                        var10 = var0.readBits(8) + 29;
                        var12 = var0.readBits(2);
                     } else {
                        var10 = var0.readBits(4) + 12;
                        var12 = var0.readBits(2);
                     }
                  }

                  var9 = var11;
                  var3 = var12;
                  var7 = var9;
                  break label59;
               }
            }

            var9 = var11;
            var10 = 1;
            var3 = var12;
            var7 = var9;
         }

         if (var10 != 0 && var5 != null) {
            int var13 = var3;
            if (var2 != null) {
               var13 = var2[var3];
            }

            var5.setColor(var1[var13]);
            var6.drawRect((float)var8, (float)var4, (float)(var8 + var10), (float)(var4 + 1), var5);
         }

         var8 += var10;
         if (var7) {
            return var8;
         }

         var11 = var7;
      }
   }

   private static int paint4BitPixelCodeString(ParsableBitArray var0, int[] var1, byte[] var2, int var3, int var4, Paint var5, Canvas var6) {
      boolean var7 = false;
      int var8 = var3;
      boolean var11 = var7;

      while(true) {
         int var10;
         label62: {
            int var12 = var0.readBits(4);
            boolean var9;
            if (var12 == 0) {
               label61: {
                  if (!var0.readBit()) {
                     var10 = var0.readBits(3);
                     if (var10 != 0) {
                        var7 = var11;
                        var10 += 2;
                        var3 = 0;
                     } else {
                        var3 = 0;
                        var10 = 0;
                        var7 = true;
                     }
                     break label62;
                  }

                  if (!var0.readBit()) {
                     var10 = var0.readBits(2) + 4;
                     var12 = var0.readBits(4);
                  } else {
                     var12 = var0.readBits(2);
                     if (var12 == 0) {
                        var12 = 0;
                        break label61;
                     }

                     if (var12 == 1) {
                        var7 = var11;
                        var3 = 0;
                        var10 = 2;
                        break label62;
                     }

                     if (var12 != 2) {
                        if (var12 != 3) {
                           var7 = var11;
                           var3 = 0;
                           var10 = 0;
                           break label62;
                        }

                        var10 = var0.readBits(8) + 25;
                        var12 = var0.readBits(4);
                     } else {
                        var10 = var0.readBits(4) + 9;
                        var12 = var0.readBits(4);
                     }
                  }

                  var9 = var11;
                  var3 = var12;
                  var7 = var9;
                  break label62;
               }
            }

            var9 = var11;
            var10 = 1;
            var3 = var12;
            var7 = var9;
         }

         if (var10 != 0 && var5 != null) {
            int var13 = var3;
            if (var2 != null) {
               var13 = var2[var3];
            }

            var5.setColor(var1[var13]);
            var6.drawRect((float)var8, (float)var4, (float)(var8 + var10), (float)(var4 + 1), var5);
         }

         var8 += var10;
         if (var7) {
            return var8;
         }

         var11 = var7;
      }
   }

   private static int paint8BitPixelCodeString(ParsableBitArray var0, int[] var1, byte[] var2, int var3, int var4, Paint var5, Canvas var6) {
      boolean var7 = false;
      int var8 = var3;
      boolean var11 = var7;

      while(true) {
         int var9 = var0.readBits(8);
         int var10;
         if (var9 != 0) {
            var7 = var11;
            var3 = var9;
            var9 = 1;
         } else if (!var0.readBit()) {
            var9 = var0.readBits(7);
            if (var9 != 0) {
               var7 = var11;
               var3 = 0;
            } else {
               var3 = 0;
               var9 = 0;
               var7 = true;
            }
         } else {
            var9 = var0.readBits(7);
            var10 = var0.readBits(8);
            var7 = var11;
            var3 = var10;
         }

         if (var9 != 0 && var5 != null) {
            var10 = var3;
            if (var2 != null) {
               var10 = var2[var3];
            }

            var5.setColor(var1[var10]);
            var6.drawRect((float)var8, (float)var4, (float)(var8 + var9), (float)(var4 + 1), var5);
         }

         var8 += var9;
         if (var7) {
            return var8;
         }

         var11 = var7;
      }
   }

   private static void paintPixelDataSubBlock(byte[] var0, int[] var1, int var2, int var3, int var4, Paint var5, Canvas var6) {
      ParsableBitArray var7 = new ParsableBitArray(var0);
      byte[] var9 = null;
      byte[] var10 = var9;
      int var11 = var4;
      var4 = var3;

      while(true) {
         while(true) {
            while(var7.bitsLeft() != 0) {
               int var8 = var7.readBits(8);
               if (var8 != 240) {
                  switch(var8) {
                  case 16:
                     if (var2 == 3) {
                        if (var9 == null) {
                           var0 = defaultMap2To8;
                        } else {
                           var0 = var9;
                        }
                     } else if (var2 == 2) {
                        if (var10 == null) {
                           var0 = defaultMap2To4;
                        } else {
                           var0 = var10;
                        }
                     } else {
                        var0 = null;
                     }

                     var4 = paint2BitPixelCodeString(var7, var1, var0, var4, var11, var5, var6);
                     var7.byteAlign();
                     break;
                  case 17:
                     if (var2 == 3) {
                        var0 = defaultMap4To8;
                     } else {
                        var0 = null;
                     }

                     var4 = paint4BitPixelCodeString(var7, var1, var0, var4, var11, var5, var6);
                     var7.byteAlign();
                     break;
                  case 18:
                     var4 = paint8BitPixelCodeString(var7, var1, (byte[])null, var4, var11, var5, var6);
                     break;
                  default:
                     switch(var8) {
                     case 32:
                        var10 = buildClutMapTable(4, 4, var7);
                        continue;
                     case 33:
                        var0 = buildClutMapTable(4, 8, var7);
                        break;
                     case 34:
                        var0 = buildClutMapTable(16, 8, var7);
                        break;
                     default:
                        continue;
                     }

                     var9 = var0;
                  }
               } else {
                  var11 += 2;
                  var4 = var3;
               }
            }

            return;
         }
      }
   }

   private static void paintPixelDataSubBlocks(DvbParser.ObjectData var0, DvbParser.ClutDefinition var1, int var2, int var3, int var4, Paint var5, Canvas var6) {
      int[] var7;
      if (var2 == 3) {
         var7 = var1.clutEntries8Bit;
      } else if (var2 == 2) {
         var7 = var1.clutEntries4Bit;
      } else {
         var7 = var1.clutEntries2Bit;
      }

      paintPixelDataSubBlock(var0.topFieldData, var7, var2, var3, var4, var5, var6);
      paintPixelDataSubBlock(var0.bottomFieldData, var7, var2, var3, var4 + 1, var5, var6);
   }

   private static DvbParser.ClutDefinition parseClutDefinition(ParsableBitArray var0, int var1) {
      int var2 = var0.readBits(8);
      var0.skipBits(8);
      var1 -= 2;
      int[] var3 = generateDefault2BitClutEntries();
      int[] var4 = generateDefault4BitClutEntries();

      int[] var5;
      int var6;
      int[] var8;
      int var9;
      int var10;
      int var11;
      byte var18;
      for(var5 = generateDefault8BitClutEntries(); var1 > 0; var8[var6] = getColor(var18, Util.constrainValue(var10, 0, 255), Util.constrainValue(var11, 0, 255), Util.constrainValue(var9, 0, 255))) {
         var6 = var0.readBits(8);
         int var7 = var0.readBits(8);
         var1 -= 2;
         if ((var7 & 128) != 0) {
            var8 = var3;
         } else if ((var7 & 64) != 0) {
            var8 = var4;
         } else {
            var8 = var5;
         }

         if ((var7 & 1) != 0) {
            var9 = var0.readBits(8);
            var10 = var0.readBits(8);
            var11 = var0.readBits(8);
            var7 = var0.readBits(8);
            var1 -= 4;
         } else {
            var9 = var0.readBits(6);
            var10 = var0.readBits(4);
            var11 = var0.readBits(4) << 4;
            var7 = var0.readBits(2);
            var1 -= 2;
            var7 <<= 6;
            var9 <<= 2;
            var10 <<= 4;
         }

         if (var9 == 0) {
            var10 = 0;
            var11 = 0;
            var7 = 255;
         }

         var18 = (byte)(255 - (var7 & 255));
         double var12 = (double)var9;
         double var14 = (double)(var10 - 128);
         Double.isNaN(var14);
         Double.isNaN(var12);
         var10 = (int)(var12 + 1.402D * var14);
         double var16 = (double)(var11 - 128);
         Double.isNaN(var16);
         Double.isNaN(var12);
         Double.isNaN(var14);
         var11 = (int)(var12 - 0.34414D * var16 - var14 * 0.71414D);
         Double.isNaN(var16);
         Double.isNaN(var12);
         var9 = (int)(var12 + var16 * 1.772D);
      }

      return new DvbParser.ClutDefinition(var2, var3, var4, var5);
   }

   private static DvbParser.DisplayDefinition parseDisplayDefinition(ParsableBitArray var0) {
      var0.skipBits(4);
      boolean var1 = var0.readBit();
      var0.skipBits(3);
      int var2 = var0.readBits(16);
      int var3 = var0.readBits(16);
      int var4;
      int var5;
      int var6;
      int var7;
      if (var1) {
         var4 = var0.readBits(16);
         var5 = var0.readBits(16);
         var6 = var0.readBits(16);
         var7 = var0.readBits(16);
      } else {
         var5 = var2;
         var7 = var3;
         var4 = 0;
         var6 = 0;
      }

      return new DvbParser.DisplayDefinition(var2, var3, var4, var5, var6, var7);
   }

   private static DvbParser.ObjectData parseObjectData(ParsableBitArray var0) {
      int var1 = var0.readBits(16);
      var0.skipBits(4);
      int var2 = var0.readBits(2);
      boolean var3 = var0.readBit();
      var0.skipBits(1);
      byte[] var4 = null;
      byte[] var5 = null;
      byte[] var7;
      if (var2 == 1) {
         var0.skipBits(var0.readBits(8) * 16);
      } else if (var2 == 0) {
         int var6 = var0.readBits(16);
         var2 = var0.readBits(16);
         if (var6 > 0) {
            var5 = new byte[var6];
            var0.readBytes(var5, 0, var6);
         }

         var4 = var5;
         if (var2 > 0) {
            var4 = new byte[var2];
            var0.readBytes(var4, 0, var2);
            var7 = var4;
            return new DvbParser.ObjectData(var1, var3, var5, var7);
         }
      }

      var7 = var4;
      var5 = var4;
      return new DvbParser.ObjectData(var1, var3, var5, var7);
   }

   private static DvbParser.PageComposition parsePageComposition(ParsableBitArray var0, int var1) {
      int var2 = var0.readBits(8);
      int var3 = var0.readBits(4);
      int var4 = var0.readBits(2);
      var0.skipBits(2);
      var1 -= 2;
      SparseArray var5 = new SparseArray();

      while(var1 > 0) {
         int var6 = var0.readBits(8);
         var0.skipBits(8);
         int var7 = var0.readBits(16);
         int var8 = var0.readBits(16);
         var1 -= 6;
         var5.put(var6, new DvbParser.PageRegion(var7, var8));
      }

      return new DvbParser.PageComposition(var2, var3, var4, var5);
   }

   private static DvbParser.RegionComposition parseRegionComposition(ParsableBitArray var0, int var1) {
      int var2 = var0.readBits(8);
      var0.skipBits(4);
      boolean var3 = var0.readBit();
      var0.skipBits(3);
      int var4 = var0.readBits(16);
      int var5 = var0.readBits(16);
      int var6 = var0.readBits(3);
      int var7 = var0.readBits(3);
      var0.skipBits(2);
      int var8 = var0.readBits(8);
      int var9 = var0.readBits(8);
      int var10 = var0.readBits(4);
      int var11 = var0.readBits(2);
      var0.skipBits(2);
      var1 -= 10;

      SparseArray var12;
      int var13;
      int var14;
      int var15;
      int var16;
      int var17;
      int var18;
      int var19;
      for(var12 = new SparseArray(); var1 > 0; var12.put(var13, new DvbParser.RegionObject(var14, var15, var16, var17, var18, var19))) {
         var13 = var0.readBits(16);
         var14 = var0.readBits(2);
         var15 = var0.readBits(2);
         var16 = var0.readBits(12);
         var0.skipBits(4);
         var17 = var0.readBits(12);
         var1 -= 6;
         if (var14 != 1 && var14 != 2) {
            var18 = 0;
            var19 = 0;
         } else {
            var18 = var0.readBits(8);
            var19 = var0.readBits(8);
            int var20 = var1 - 2;
            var18 = var18;
            var1 = var20;
         }
      }

      return new DvbParser.RegionComposition(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   private static void parseSubtitlingSegment(ParsableBitArray var0, DvbParser.SubtitleService var1) {
      int var2 = var0.readBits(8);
      int var3 = var0.readBits(16);
      int var4 = var0.readBits(16);
      int var5 = var0.getBytePosition();
      if (var4 * 8 > var0.bitsLeft()) {
         Log.w("DvbParser", "Data field length exceeds limit");
         var0.skipBits(var0.bitsLeft());
      } else {
         DvbParser.PageComposition var7;
         switch(var2) {
         case 16:
            if (var3 == var1.subtitlePageId) {
               DvbParser.PageComposition var10 = var1.pageComposition;
               var7 = parsePageComposition(var0, var4);
               if (var7.state != 0) {
                  var1.pageComposition = var7;
                  var1.regions.clear();
                  var1.cluts.clear();
                  var1.objects.clear();
               } else if (var10 != null && var10.version != var7.version) {
                  var1.pageComposition = var7;
               }
            }
            break;
         case 17:
            var7 = var1.pageComposition;
            if (var3 == var1.subtitlePageId && var7 != null) {
               DvbParser.RegionComposition var9 = parseRegionComposition(var0, var4);
               if (var7.state == 0) {
                  var9.mergeFrom((DvbParser.RegionComposition)var1.regions.get(var9.id));
               }

               var1.regions.put(var9.id, var9);
            }
            break;
         case 18:
            DvbParser.ClutDefinition var8;
            if (var3 == var1.subtitlePageId) {
               var8 = parseClutDefinition(var0, var4);
               var1.cluts.put(var8.id, var8);
            } else if (var3 == var1.ancillaryPageId) {
               var8 = parseClutDefinition(var0, var4);
               var1.ancillaryCluts.put(var8.id, var8);
            }
            break;
         case 19:
            DvbParser.ObjectData var6;
            if (var3 == var1.subtitlePageId) {
               var6 = parseObjectData(var0);
               var1.objects.put(var6.id, var6);
            } else if (var3 == var1.ancillaryPageId) {
               var6 = parseObjectData(var0);
               var1.ancillaryObjects.put(var6.id, var6);
            }
            break;
         case 20:
            if (var3 == var1.subtitlePageId) {
               var1.displayDefinition = parseDisplayDefinition(var0);
            }
         }

         var0.skipBytes(var5 + var4 - var0.getBytePosition());
      }
   }

   public List decode(byte[] var1, int var2) {
      ParsableBitArray var17 = new ParsableBitArray(var1, var2);

      while(var17.bitsLeft() >= 48 && var17.readBits(8) == 15) {
         parseSubtitlingSegment(var17, this.subtitleService);
      }

      DvbParser.SubtitleService var18 = this.subtitleService;
      if (var18.pageComposition == null) {
         return Collections.emptyList();
      } else {
         DvbParser.DisplayDefinition var3 = var18.displayDefinition;
         if (var3 == null) {
            var3 = this.defaultDisplayDefinition;
         }

         Bitmap var19 = this.bitmap;
         if (var19 == null || var3.width + 1 != var19.getWidth() || var3.height + 1 != this.bitmap.getHeight()) {
            this.bitmap = Bitmap.createBitmap(var3.width + 1, var3.height + 1, Config.ARGB_8888);
            this.canvas.setBitmap(this.bitmap);
         }

         ArrayList var4 = new ArrayList();
         SparseArray var5 = this.subtitleService.pageComposition.regions;

         for(int var6 = 0; var6 < var5.size(); ++var6) {
            DvbParser.PageRegion var20 = (DvbParser.PageRegion)var5.valueAt(var6);
            var2 = var5.keyAt(var6);
            DvbParser.RegionComposition var7 = (DvbParser.RegionComposition)this.subtitleService.regions.get(var2);
            int var8 = var20.horizontalAddress + var3.horizontalPositionMinimum;
            int var9 = var20.verticalAddress + var3.verticalPositionMinimum;
            var2 = Math.min(var7.width + var8, var3.horizontalPositionMaximum);
            int var10 = Math.min(var7.height + var9, var3.verticalPositionMaximum);
            Canvas var21 = this.canvas;
            float var11 = (float)var8;
            float var12 = (float)var9;
            var21.clipRect(var11, var12, (float)var2, (float)var10, Op.REPLACE);
            DvbParser.ClutDefinition var13 = (DvbParser.ClutDefinition)this.subtitleService.cluts.get(var7.clutId);
            DvbParser.ClutDefinition var22 = var13;
            if (var13 == null) {
               var13 = (DvbParser.ClutDefinition)this.subtitleService.ancillaryCluts.get(var7.clutId);
               var22 = var13;
               if (var13 == null) {
                  var22 = this.defaultClutDefinition;
               }
            }

            SparseArray var23 = var7.regionObjects;

            for(var2 = 0; var2 < var23.size(); ++var2) {
               var10 = var23.keyAt(var2);
               DvbParser.RegionObject var14 = (DvbParser.RegionObject)var23.valueAt(var2);
               DvbParser.ObjectData var15 = (DvbParser.ObjectData)this.subtitleService.objects.get(var10);
               if (var15 == null) {
                  var15 = (DvbParser.ObjectData)this.subtitleService.ancillaryObjects.get(var10);
               }

               if (var15 != null) {
                  Paint var16;
                  if (var15.nonModifyingColorFlag) {
                     var16 = null;
                  } else {
                     var16 = this.defaultPaint;
                  }

                  paintPixelDataSubBlocks(var15, var22, var7.depth, var14.horizontalPosition + var8, var9 + var14.verticalPosition, var16, this.canvas);
               }
            }

            if (var7.fillFlag) {
               var2 = var7.depth;
               if (var2 == 3) {
                  var2 = var22.clutEntries8Bit[var7.pixelCode8Bit];
               } else if (var2 == 2) {
                  var2 = var22.clutEntries4Bit[var7.pixelCode4Bit];
               } else {
                  var2 = var22.clutEntries2Bit[var7.pixelCode2Bit];
               }

               this.fillRegionPaint.setColor(var2);
               this.canvas.drawRect(var11, var12, (float)(var7.width + var8), (float)(var7.height + var9), this.fillRegionPaint);
            }

            var19 = Bitmap.createBitmap(this.bitmap, var8, var9, var7.width, var7.height);
            var2 = var3.width;
            var11 /= (float)var2;
            var8 = var3.height;
            var4.add(new Cue(var19, var11, 0, var12 / (float)var8, 0, (float)var7.width / (float)var2, (float)var7.height / (float)var8));
            this.canvas.drawColor(0, Mode.CLEAR);
         }

         return var4;
      }
   }

   public void reset() {
      this.subtitleService.reset();
   }

   private static final class ClutDefinition {
      public final int[] clutEntries2Bit;
      public final int[] clutEntries4Bit;
      public final int[] clutEntries8Bit;
      public final int id;

      public ClutDefinition(int var1, int[] var2, int[] var3, int[] var4) {
         this.id = var1;
         this.clutEntries2Bit = var2;
         this.clutEntries4Bit = var3;
         this.clutEntries8Bit = var4;
      }
   }

   private static final class DisplayDefinition {
      public final int height;
      public final int horizontalPositionMaximum;
      public final int horizontalPositionMinimum;
      public final int verticalPositionMaximum;
      public final int verticalPositionMinimum;
      public final int width;

      public DisplayDefinition(int var1, int var2, int var3, int var4, int var5, int var6) {
         this.width = var1;
         this.height = var2;
         this.horizontalPositionMinimum = var3;
         this.horizontalPositionMaximum = var4;
         this.verticalPositionMinimum = var5;
         this.verticalPositionMaximum = var6;
      }
   }

   private static final class ObjectData {
      public final byte[] bottomFieldData;
      public final int id;
      public final boolean nonModifyingColorFlag;
      public final byte[] topFieldData;

      public ObjectData(int var1, boolean var2, byte[] var3, byte[] var4) {
         this.id = var1;
         this.nonModifyingColorFlag = var2;
         this.topFieldData = var3;
         this.bottomFieldData = var4;
      }
   }

   private static final class PageComposition {
      public final SparseArray regions;
      public final int state;
      public final int timeOutSecs;
      public final int version;

      public PageComposition(int var1, int var2, int var3, SparseArray var4) {
         this.timeOutSecs = var1;
         this.version = var2;
         this.state = var3;
         this.regions = var4;
      }
   }

   private static final class PageRegion {
      public final int horizontalAddress;
      public final int verticalAddress;

      public PageRegion(int var1, int var2) {
         this.horizontalAddress = var1;
         this.verticalAddress = var2;
      }
   }

   private static final class RegionComposition {
      public final int clutId;
      public final int depth;
      public final boolean fillFlag;
      public final int height;
      public final int id;
      public final int levelOfCompatibility;
      public final int pixelCode2Bit;
      public final int pixelCode4Bit;
      public final int pixelCode8Bit;
      public final SparseArray regionObjects;
      public final int width;

      public RegionComposition(int var1, boolean var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, SparseArray var11) {
         this.id = var1;
         this.fillFlag = var2;
         this.width = var3;
         this.height = var4;
         this.levelOfCompatibility = var5;
         this.depth = var6;
         this.clutId = var7;
         this.pixelCode8Bit = var8;
         this.pixelCode4Bit = var9;
         this.pixelCode2Bit = var10;
         this.regionObjects = var11;
      }

      public void mergeFrom(DvbParser.RegionComposition var1) {
         if (var1 != null) {
            SparseArray var3 = var1.regionObjects;

            for(int var2 = 0; var2 < var3.size(); ++var2) {
               this.regionObjects.put(var3.keyAt(var2), var3.valueAt(var2));
            }

         }
      }
   }

   private static final class RegionObject {
      public final int backgroundPixelCode;
      public final int foregroundPixelCode;
      public final int horizontalPosition;
      public final int provider;
      public final int type;
      public final int verticalPosition;

      public RegionObject(int var1, int var2, int var3, int var4, int var5, int var6) {
         this.type = var1;
         this.provider = var2;
         this.horizontalPosition = var3;
         this.verticalPosition = var4;
         this.foregroundPixelCode = var5;
         this.backgroundPixelCode = var6;
      }
   }

   private static final class SubtitleService {
      public final SparseArray ancillaryCluts = new SparseArray();
      public final SparseArray ancillaryObjects = new SparseArray();
      public final int ancillaryPageId;
      public final SparseArray cluts = new SparseArray();
      public DvbParser.DisplayDefinition displayDefinition;
      public final SparseArray objects = new SparseArray();
      public DvbParser.PageComposition pageComposition;
      public final SparseArray regions = new SparseArray();
      public final int subtitlePageId;

      public SubtitleService(int var1, int var2) {
         this.subtitlePageId = var1;
         this.ancillaryPageId = var2;
      }

      public void reset() {
         this.regions.clear();
         this.cluts.clear();
         this.objects.clear();
         this.ancillaryCluts.clear();
         this.ancillaryObjects.clear();
         this.displayDefinition = null;
         this.pageComposition = null;
      }
   }
}

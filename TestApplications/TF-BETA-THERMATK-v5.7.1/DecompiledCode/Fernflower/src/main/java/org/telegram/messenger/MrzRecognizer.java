package org.telegram.messenger;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;
import java.util.HashMap;

public class MrzRecognizer {
   private static native Rect[][] binarizeAndFindCharacters(Bitmap var0, Bitmap var1);

   private static String capitalize(String var0) {
      if (var0 == null) {
         return null;
      } else {
         char[] var3 = var0.toCharArray();
         int var1 = 0;

         for(boolean var2 = true; var1 < var3.length; ++var1) {
            if (!var2 && Character.isLetter(var3[var1])) {
               var3[var1] = Character.toLowerCase(var3[var1]);
            } else if (var3[var1] == ' ') {
               var2 = true;
            } else {
               var2 = false;
            }
         }

         return new String(var3);
      }
   }

   private static int checksum(String var0) {
      char[] var5 = var0.toCharArray();
      int[] var1 = new int[]{7, 3, 1};
      int var2 = 0;

      int var3;
      for(var3 = 0; var2 < var5.length; ++var2) {
         int var4;
         if (var5[var2] >= '0' && var5[var2] <= '9') {
            var4 = var5[var2] - 48;
         } else if (var5[var2] >= 'A' && var5[var2] <= 'Z') {
            var4 = var5[var2] - 65 + 10;
         } else {
            var4 = 0;
         }

         var3 += var4 * var1[var2 % var1.length];
      }

      return var3 % 10;
   }

   private static String cyrillicToLatin(String var0) {
      String[] var1 = new String[33];
      int var2 = 0;
      var1[0] = "A";
      var1[1] = "B";
      var1[2] = "V";
      var1[3] = "G";
      var1[4] = "D";
      var1[5] = "E";
      var1[6] = "E";
      var1[7] = "ZH";
      var1[8] = "Z";
      var1[9] = "I";
      var1[10] = "I";
      var1[11] = "K";
      var1[12] = "L";
      var1[13] = "M";
      var1[14] = "N";
      var1[15] = "O";
      var1[16] = "P";
      var1[17] = "R";
      var1[18] = "S";
      var1[19] = "T";
      var1[20] = "U";
      var1[21] = "F";
      var1[22] = "KH";
      var1[23] = "TS";
      var1[24] = "CH";
      var1[25] = "SH";
      var1[26] = "SHCH";
      var1[27] = "IE";
      var1[28] = "Y";
      var1[29] = "";
      var1[30] = "E";
      var1[31] = "IU";

      int var3;
      for(var1[32] = "IA"; var2 < var1.length; var2 = var3) {
         var3 = var2 + 1;
         var0 = var0.replace("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".substring(var2, var3), var1[var2]);
      }

      return var0;
   }

   private static native int[] findCornerPoints(Bitmap var0);

   private static HashMap getCountriesMap() {
      HashMap var0 = new HashMap();
      var0.put("AFG", "AF");
      var0.put("ALA", "AX");
      var0.put("ALB", "AL");
      var0.put("DZA", "DZ");
      var0.put("ASM", "AS");
      var0.put("AND", "AD");
      var0.put("AGO", "AO");
      var0.put("AIA", "AI");
      var0.put("ATA", "AQ");
      var0.put("ATG", "AG");
      var0.put("ARG", "AR");
      var0.put("ARM", "AM");
      var0.put("ABW", "AW");
      var0.put("AUS", "AU");
      var0.put("AUT", "AT");
      var0.put("AZE", "AZ");
      var0.put("BHS", "BS");
      var0.put("BHR", "BH");
      var0.put("BGD", "BD");
      var0.put("BRB", "BB");
      var0.put("BLR", "BY");
      var0.put("BEL", "BE");
      var0.put("BLZ", "BZ");
      var0.put("BEN", "BJ");
      var0.put("BMU", "BM");
      var0.put("BTN", "BT");
      var0.put("BOL", "BO");
      var0.put("BES", "BQ");
      var0.put("BIH", "BA");
      var0.put("BWA", "BW");
      var0.put("BVT", "BV");
      var0.put("BRA", "BR");
      var0.put("IOT", "IO");
      var0.put("BRN", "BN");
      var0.put("BGR", "BG");
      var0.put("BFA", "BF");
      var0.put("BDI", "BI");
      var0.put("CPV", "CV");
      var0.put("KHM", "KH");
      var0.put("CMR", "CM");
      var0.put("CAN", "CA");
      var0.put("CYM", "KY");
      var0.put("CAF", "CF");
      var0.put("TCD", "TD");
      var0.put("CHL", "CL");
      var0.put("CHN", "CN");
      var0.put("CXR", "CX");
      var0.put("CCK", "CC");
      var0.put("COL", "CO");
      var0.put("COM", "KM");
      var0.put("COG", "CG");
      var0.put("COD", "CD");
      var0.put("COK", "CK");
      var0.put("CRI", "CR");
      var0.put("CIV", "CI");
      var0.put("HRV", "HR");
      var0.put("CUB", "CU");
      var0.put("CUW", "CW");
      var0.put("CYP", "CY");
      var0.put("CZE", "CZ");
      var0.put("DNK", "DK");
      var0.put("DJI", "DJ");
      var0.put("DMA", "DM");
      var0.put("DOM", "DO");
      var0.put("ECU", "EC");
      var0.put("EGY", "EG");
      var0.put("SLV", "SV");
      var0.put("GNQ", "GQ");
      var0.put("ERI", "ER");
      var0.put("EST", "EE");
      var0.put("ETH", "ET");
      var0.put("FLK", "FK");
      var0.put("FRO", "FO");
      var0.put("FJI", "FJ");
      var0.put("FIN", "FI");
      var0.put("FRA", "FR");
      var0.put("GUF", "GF");
      var0.put("PYF", "PF");
      var0.put("ATF", "TF");
      var0.put("GAB", "GA");
      var0.put("GMB", "GM");
      var0.put("GEO", "GE");
      var0.put("D<<", "DE");
      var0.put("GHA", "GH");
      var0.put("GIB", "GI");
      var0.put("GRC", "GR");
      var0.put("GRL", "GL");
      var0.put("GRD", "GD");
      var0.put("GLP", "GP");
      var0.put("GUM", "GU");
      var0.put("GTM", "GT");
      var0.put("GGY", "GG");
      var0.put("GIN", "GN");
      var0.put("GNB", "GW");
      var0.put("GUY", "GY");
      var0.put("HTI", "HT");
      var0.put("HMD", "HM");
      var0.put("VAT", "VA");
      var0.put("HND", "HN");
      var0.put("HKG", "HK");
      var0.put("HUN", "HU");
      var0.put("ISL", "IS");
      var0.put("IND", "IN");
      var0.put("IDN", "ID");
      var0.put("IRN", "IR");
      var0.put("IRQ", "IQ");
      var0.put("IRL", "IE");
      var0.put("IMN", "IM");
      var0.put("ISR", "IL");
      var0.put("ITA", "IT");
      var0.put("JAM", "JM");
      var0.put("JPN", "JP");
      var0.put("JEY", "JE");
      var0.put("JOR", "JO");
      var0.put("KAZ", "KZ");
      var0.put("KEN", "KE");
      var0.put("KIR", "KI");
      var0.put("PRK", "KP");
      var0.put("KOR", "KR");
      var0.put("KWT", "KW");
      var0.put("KGZ", "KG");
      var0.put("LAO", "LA");
      var0.put("LVA", "LV");
      var0.put("LBN", "LB");
      var0.put("LSO", "LS");
      var0.put("LBR", "LR");
      var0.put("LBY", "LY");
      var0.put("LIE", "LI");
      var0.put("LTU", "LT");
      var0.put("LUX", "LU");
      var0.put("MAC", "MO");
      var0.put("MKD", "MK");
      var0.put("MDG", "MG");
      var0.put("MWI", "MW");
      var0.put("MYS", "MY");
      var0.put("MDV", "MV");
      var0.put("MLI", "ML");
      var0.put("MLT", "MT");
      var0.put("MHL", "MH");
      var0.put("MTQ", "MQ");
      var0.put("MRT", "MR");
      var0.put("MUS", "MU");
      var0.put("MYT", "YT");
      var0.put("MEX", "MX");
      var0.put("FSM", "FM");
      var0.put("MDA", "MD");
      var0.put("MCO", "MC");
      var0.put("MNG", "MN");
      var0.put("MNE", "ME");
      var0.put("MSR", "MS");
      var0.put("MAR", "MA");
      var0.put("MOZ", "MZ");
      var0.put("MMR", "MM");
      var0.put("NAM", "NA");
      var0.put("NRU", "NR");
      var0.put("NPL", "NP");
      var0.put("NLD", "NL");
      var0.put("NCL", "NC");
      var0.put("NZL", "NZ");
      var0.put("NIC", "NI");
      var0.put("NER", "NE");
      var0.put("NGA", "NG");
      var0.put("NIU", "NU");
      var0.put("NFK", "NF");
      var0.put("MNP", "MP");
      var0.put("NOR", "NO");
      var0.put("OMN", "OM");
      var0.put("PAK", "PK");
      var0.put("PLW", "PW");
      var0.put("PSE", "PS");
      var0.put("PAN", "PA");
      var0.put("PNG", "PG");
      var0.put("PRY", "PY");
      var0.put("PER", "PE");
      var0.put("PHL", "PH");
      var0.put("PCN", "PN");
      var0.put("POL", "PL");
      var0.put("PRT", "PT");
      var0.put("PRI", "PR");
      var0.put("QAT", "QA");
      var0.put("REU", "RE");
      var0.put("ROU", "RO");
      var0.put("RUS", "RU");
      var0.put("RWA", "RW");
      var0.put("BLM", "BL");
      var0.put("SHN", "SH");
      var0.put("KNA", "KN");
      var0.put("LCA", "LC");
      var0.put("MAF", "MF");
      var0.put("SPM", "PM");
      var0.put("VCT", "VC");
      var0.put("WSM", "WS");
      var0.put("SMR", "SM");
      var0.put("STP", "ST");
      var0.put("SAU", "SA");
      var0.put("SEN", "SN");
      var0.put("SRB", "RS");
      var0.put("SYC", "SC");
      var0.put("SLE", "SL");
      var0.put("SGP", "SG");
      var0.put("SXM", "SX");
      var0.put("SVK", "SK");
      var0.put("SVN", "SI");
      var0.put("SLB", "SB");
      var0.put("SOM", "SO");
      var0.put("ZAF", "ZA");
      var0.put("SGS", "GS");
      var0.put("SSD", "SS");
      var0.put("ESP", "ES");
      var0.put("LKA", "LK");
      var0.put("SDN", "SD");
      var0.put("SUR", "SR");
      var0.put("SJM", "SJ");
      var0.put("SWZ", "SZ");
      var0.put("SWE", "SE");
      var0.put("CHE", "CH");
      var0.put("SYR", "SY");
      var0.put("TWN", "TW");
      var0.put("TJK", "TJ");
      var0.put("TZA", "TZ");
      var0.put("THA", "TH");
      var0.put("TLS", "TL");
      var0.put("TGO", "TG");
      var0.put("TKL", "TK");
      var0.put("TON", "TO");
      var0.put("TTO", "TT");
      var0.put("TUN", "TN");
      var0.put("TUR", "TR");
      var0.put("TKM", "TM");
      var0.put("TCA", "TC");
      var0.put("TUV", "TV");
      var0.put("UGA", "UG");
      var0.put("UKR", "UA");
      var0.put("ARE", "AE");
      var0.put("GBR", "GB");
      var0.put("USA", "US");
      var0.put("UMI", "UM");
      var0.put("URY", "UY");
      var0.put("UZB", "UZ");
      var0.put("VUT", "VU");
      var0.put("VEN", "VE");
      var0.put("VNM", "VN");
      var0.put("VGB", "VG");
      var0.put("VIR", "VI");
      var0.put("WLF", "WF");
      var0.put("ESH", "EH");
      var0.put("YEM", "YE");
      var0.put("ZMB", "ZM");
      var0.put("ZWE", "ZW");
      return var0;
   }

   private static int getNumber(char var0) {
      if (var0 == 'O') {
         return 0;
      } else if (var0 == 'I') {
         return 1;
      } else {
         return var0 == 'B' ? 8 : var0 - 48;
      }
   }

   private static void parseBirthDate(String param0, MrzRecognizer.Result param1) {
      // $FF: Couldn't be decompiled
   }

   private static void parseExpiryDate(String var0, MrzRecognizer.Result var1) {
      try {
         if ("<<<<<<".equals(var0)) {
            var1.doesNotExpire = true;
         } else {
            var1.expiryYear = Integer.parseInt(var0.substring(0, 2)) + 2000;
            var1.expiryMonth = Integer.parseInt(var0.substring(2, 4));
            var1.expiryDay = Integer.parseInt(var0.substring(4));
         }
      } catch (NumberFormatException var2) {
      }

   }

   private static int parseGender(char var0) {
      if (var0 != 'F') {
         return var0 != 'M' ? 0 : 1;
      } else {
         return 2;
      }
   }

   private static native String performRecognition(Bitmap var0, int var1, int var2, AssetManager var3);

   public static MrzRecognizer.Result recognize(Bitmap var0, boolean var1) {
      MrzRecognizer.Result var2;
      if (var1) {
         var2 = recognizeBarcode(var0);
         if (var2 != null) {
            return var2;
         }
      }

      label24: {
         try {
            var2 = recognizeMRZ(var0);
         } catch (Exception var3) {
            break label24;
         }

         if (var2 != null) {
            return var2;
         }
      }

      if (!var1) {
         MrzRecognizer.Result var4 = recognizeBarcode(var0);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   public static MrzRecognizer.Result recognize(byte[] var0, int var1, int var2, int var3) {
      Bitmap var4 = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
      setYuvBitmapPixels(var4, var0);
      Matrix var8 = new Matrix();
      var8.setRotate((float)var3);
      int var5 = Math.min(var1, var2);
      int var6 = Math.round((float)var5 * 0.704F);
      boolean var9;
      if (var3 != 90 && var3 != 270) {
         var9 = false;
      } else {
         var9 = true;
      }

      if (var9) {
         var1 = var1 / 2 - var6 / 2;
      } else {
         var1 = 0;
      }

      if (var9) {
         var2 = 0;
      } else {
         var2 = var2 / 2 - var6 / 2;
      }

      int var7;
      if (var9) {
         var7 = var6;
      } else {
         var7 = var5;
      }

      if (!var9) {
         var5 = var6;
      }

      return recognize(Bitmap.createBitmap(var4, var1, var2, var7, var5, var8, false), false);
   }

   private static MrzRecognizer.Result recognizeBarcode(Bitmap var0) {
      return null;
   }

   private static MrzRecognizer.Result recognizeMRZ(Bitmap var0) {
      float var2;
      Bitmap var3;
      if (var0.getWidth() <= 512 && var0.getHeight() <= 512) {
         var2 = 1.0F;
         var3 = var0;
      } else {
         var2 = 512.0F / (float)Math.max(var0.getWidth(), var0.getHeight());
         var3 = Bitmap.createScaledBitmap(var0, Math.round((float)var0.getWidth() * var2), Math.round((float)var0.getHeight() * var2), true);
      }

      int var23;
      label274: {
         int[] var4 = findCornerPoints(var3);
         float var5 = 1.0F / var2;
         if (var4 != null) {
            Point var31 = new Point(var4[0], var4[1]);
            Point var37 = new Point(var4[2], var4[3]);
            Point var6 = new Point(var4[4], var4[5]);
            Point var38 = new Point(var4[6], var4[7]);
            Point var7;
            if (var37.x < var31.x) {
               var7 = var37;
            } else {
               var7 = var31;
               var31 = var37;
               var37 = var38;
               var38 = var6;
               var6 = var37;
            }

            double var8 = Math.hypot((double)(var31.x - var7.x), (double)(var31.y - var7.y));
            double var10 = Math.hypot((double)(var6.x - var38.x), (double)(var6.y - var38.y));
            double var12 = Math.hypot((double)(var38.x - var7.x), (double)(var38.y - var7.y));
            double var14 = Math.hypot((double)(var6.x - var31.x), (double)(var6.y - var31.y));
            double var16 = var8 / var12;
            var8 /= var14;
            var12 = var10 / var12;
            var10 /= var14;
            var3 = var0;
            if (var16 >= 1.35D) {
               var3 = var0;
               if (var16 <= 1.75D) {
                  var3 = var0;
                  if (var12 >= 1.35D) {
                     var3 = var0;
                     if (var12 <= 1.75D) {
                        var3 = var0;
                        if (var8 >= 1.35D) {
                           var3 = var0;
                           if (var8 <= 1.75D) {
                              var3 = var0;
                              if (var10 >= 1.35D) {
                                 var3 = var0;
                                 if (var10 <= 1.75D) {
                                    var3 = Bitmap.createBitmap(1024, (int)Math.round(1024.0D / ((var16 + var8 + var12 + var10) / 4.0D)), Config.ARGB_8888);
                                    Canvas var18 = new Canvas(var3);
                                    float var19 = (float)var3.getWidth();
                                    var2 = (float)var3.getWidth();
                                    float var20 = (float)var3.getHeight();
                                    float var21 = (float)var3.getHeight();
                                    float[] var22 = new float[]{(float)var7.x * var5, (float)var7.y * var5, (float)var31.x * var5, (float)var31.y * var5, (float)var6.x * var5, (float)var6.y * var5, (float)var38.x * var5, (float)var38.y * var5};
                                    Matrix var33 = new Matrix();
                                    var23 = var22.length;
                                    var33.setPolyToPoly(var22, 0, new float[]{0.0F, 0.0F, var19, 0.0F, var2, var20, 0.0F, var21}, 0, var23 >> 1);
                                    var18.drawBitmap(var0, var33, new Paint(2));
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            label271: {
               if (var0.getWidth() <= 1500) {
                  var3 = var0;
                  if (var0.getHeight() <= 1500) {
                     break label271;
                  }
               }

               var2 = 1500.0F / (float)Math.max(var0.getWidth(), var0.getHeight());
               var0 = Bitmap.createScaledBitmap(var0, Math.round((float)var0.getWidth() * var2), Math.round((float)var0.getHeight() * var2), true);
               break label274;
            }
         }

         var0 = var3;
      }

      Bitmap var1 = null;
      Object var47 = var1;
      int var24 = 0;
      int var25 = 0;

      int var26;
      int var27;
      int var28;
      for(var23 = 0; var24 < 3; ++var24) {
         Matrix var32;
         if (var24 != 1) {
            if (var24 != 2) {
               var32 = null;
            } else {
               var32 = new Matrix();
               var32.setRotate(-1.0F, (float)(var0.getWidth() / 2), (float)(var0.getHeight() / 2));
            }
         } else {
            var32 = new Matrix();
            var32.setRotate(1.0F, (float)(var0.getWidth() / 2), (float)(var0.getHeight() / 2));
         }

         if (var32 != null) {
            var3 = Bitmap.createBitmap(var0, 0, 0, var0.getWidth(), var0.getHeight(), var32, true);
         } else {
            var3 = var0;
         }

         var1 = Bitmap.createBitmap(var3.getWidth(), var3.getHeight(), Config.ALPHA_8);
         var47 = binarizeAndFindCharacters(var3, var1);
         if (var47 == null) {
            return null;
         }

         var26 = ((Object[])var47).length;

         for(var27 = 0; var27 < var26; var23 = var28) {
            Object var40 = ((Object[])var47)[var27];
            var25 = Math.max(((Object[])var40).length, var25);
            var28 = var23;
            if (((Object[])var40).length > 0) {
               var28 = var23 + 1;
            }

            ++var27;
         }

         if (var23 >= 2 && var25 >= 30) {
            break;
         }
      }

      if (var25 >= 30 && var23 >= 2) {
         Bitmap var43 = Bitmap.createBitmap(((Object[])((Object[])var47)[0]).length * 10, ((Object[])var47).length * 15, Config.ALPHA_8);
         Canvas var41 = new Canvas(var43);
         Paint var46 = new Paint(2);
         Rect var49 = new Rect(0, 0, 10, 15);
         var24 = ((Object[])var47).length;
         var25 = 0;

         for(var23 = 0; var25 < var24; ++var25) {
            Object var34 = ((Object[])var47)[var25];
            var26 = ((Object[])var34).length;
            var28 = 0;

            for(var27 = 0; var28 < var26; ++var28) {
               Object var50 = ((Object[])var34)[var28];
               int var29 = var27 * 10;
               int var30 = var23 * 15;
               var49.set(var29, var30, var29 + 10, var30 + 15);
               var41.drawBitmap(var1, (Rect)var50, var49, var46);
               ++var27;
            }

            ++var23;
         }

         String var36 = performRecognition(var43, ((Object[])var47).length, ((Object[])((Object[])var47)[0]).length, ApplicationLoader.applicationContext.getAssets());
         if (var36 == null) {
            return null;
         } else {
            String[] var48 = TextUtils.split(var36, "\n");
            MrzRecognizer.Result var35 = new MrzRecognizer.Result();
            if (var48.length >= 2 && var48[0].length() >= 30 && var48[1].length() == var48[0].length()) {
               var35.rawMRZ = TextUtils.join("\n", var48);
               HashMap var39 = getCountriesMap();
               char var51 = var48[0].charAt(0);
               String var42;
               if (var51 == 'P') {
                  var35.type = 1;
                  if (var48[0].length() == 44) {
                     var35.issuingCountry = var48[0].substring(2, 5);
                     var23 = var48[0].indexOf("<<", 6);
                     if (var23 != -1) {
                        var35.lastName = var48[0].substring(5, var23).replace('<', ' ').replace('0', 'O').trim();
                        var35.firstName = var48[0].substring(var23 + 2).replace('<', ' ').replace('0', 'O').trim();
                        if (var35.firstName.contains("   ")) {
                           var42 = var35.firstName;
                           var35.firstName = var42.substring(0, var42.indexOf("   "));
                        }
                     }

                     var42 = var48[1].substring(0, 9).replace('<', ' ').replace('O', '0').trim();
                     if (checksum(var42) == getNumber(var48[1].charAt(9))) {
                        var35.number = var42;
                     }

                     var35.nationality = var48[1].substring(10, 13);
                     var42 = var48[1].substring(13, 19).replace('O', '0').replace('I', '1');
                     if (checksum(var42) == getNumber(var48[1].charAt(19))) {
                        parseBirthDate(var42, var35);
                     }

                     var35.gender = parseGender(var48[1].charAt(20));
                     var42 = var48[1].substring(21, 27).replace('O', '0').replace('I', '1');
                     if (checksum(var42) == getNumber(var48[1].charAt(27)) || var48[1].charAt(27) == '<') {
                        parseExpiryDate(var42, var35);
                     }

                     if ("RUS".equals(var35.issuingCountry) && var48[0].charAt(1) == 'N') {
                        var35.type = 3;
                        String[] var44 = var35.firstName.split(" ");
                        var35.firstName = cyrillicToLatin(russianPassportTranslit(var44[0]));
                        if (var44.length > 1) {
                           var35.middleName = cyrillicToLatin(russianPassportTranslit(var44[1]));
                        }

                        var35.lastName = cyrillicToLatin(russianPassportTranslit(var35.lastName));
                        if (var35.number != null) {
                           StringBuilder var45 = new StringBuilder();
                           var45.append(var35.number.substring(0, 3));
                           var45.append(var48[1].charAt(28));
                           var45.append(var35.number.substring(3));
                           var35.number = var45.toString();
                        }
                     } else {
                        var35.firstName = var35.firstName.replace('8', 'B');
                        var35.lastName = var35.lastName.replace('8', 'B');
                     }

                     var35.lastName = capitalize(var35.lastName);
                     var35.firstName = capitalize(var35.firstName);
                     var35.middleName = capitalize(var35.middleName);
                  }
               } else {
                  if (var51 != 'I' && var51 != 'A' && var51 != 'C') {
                     return null;
                  }

                  var35.type = 2;
                  if (var48.length == 3 && var48[0].length() == 30 && var48[2].length() == 30) {
                     var35.issuingCountry = var48[0].substring(2, 5);
                     var42 = var48[0].substring(5, 14).replace('<', ' ').replace('O', '0').trim();
                     if (checksum(var42) == var48[0].charAt(14) - 48) {
                        var35.number = var42;
                     }

                     var42 = var48[1].substring(0, 6).replace('O', '0').replace('I', '1');
                     if (checksum(var42) == getNumber(var48[1].charAt(6))) {
                        parseBirthDate(var42, var35);
                     }

                     var35.gender = parseGender(var48[1].charAt(7));
                     var42 = var48[1].substring(8, 14).replace('O', '0').replace('I', '1');
                     if (checksum(var42) == getNumber(var48[1].charAt(14)) || var48[1].charAt(14) == '<') {
                        parseExpiryDate(var42, var35);
                     }

                     var35.nationality = var48[1].substring(15, 18);
                     var23 = var48[2].indexOf("<<");
                     if (var23 != -1) {
                        var35.lastName = var48[2].substring(0, var23).replace('<', ' ').trim();
                        var35.firstName = var48[2].substring(var23 + 2).replace('<', ' ').trim();
                     }
                  } else if (var48.length == 2 && var48[0].length() == 36) {
                     var35.issuingCountry = var48[0].substring(2, 5);
                     if ("FRA".equals(var35.issuingCountry) && var51 == 'I' && var48[0].charAt(1) == 'D') {
                        var35.nationality = "FRA";
                        var35.lastName = var48[0].substring(5, 30).replace('<', ' ').trim();
                        var35.firstName = var48[1].substring(13, 27).replace("<<", ", ").replace('<', ' ').trim();
                        var42 = var48[1].substring(0, 12).replace('O', '0');
                        if (checksum(var42) == getNumber(var48[1].charAt(12))) {
                           var35.number = var42;
                        }

                        var42 = var48[1].substring(27, 33).replace('O', '0').replace('I', '1');
                        if (checksum(var42) == getNumber(var48[1].charAt(33))) {
                           parseBirthDate(var42, var35);
                        }

                        var35.gender = parseGender(var48[1].charAt(34));
                        var35.doesNotExpire = true;
                     } else {
                        var23 = var48[0].indexOf("<<");
                        if (var23 != -1) {
                           var35.lastName = var48[0].substring(5, var23).replace('<', ' ').trim();
                           var35.firstName = var48[0].substring(var23 + 2).replace('<', ' ').trim();
                        }

                        var42 = var48[1].substring(0, 9).replace('<', ' ').replace('O', '0').trim();
                        if (checksum(var42) == getNumber(var48[1].charAt(9))) {
                           var35.number = var42;
                        }

                        var35.nationality = var48[1].substring(10, 13);
                        var42 = var48[1].substring(13, 19).replace('O', '0').replace('I', '1');
                        if (checksum(var42) == getNumber(var48[1].charAt(19))) {
                           parseBirthDate(var42, var35);
                        }

                        var35.gender = parseGender(var48[1].charAt(20));
                        var42 = var48[1].substring(21, 27).replace('O', '0').replace('I', '1');
                        if (checksum(var42) == getNumber(var48[1].charAt(27)) || var48[1].charAt(27) == '<') {
                           parseExpiryDate(var42, var35);
                        }
                     }
                  }

                  var35.firstName = capitalize(var35.firstName.replace('0', 'O').replace('8', 'B'));
                  var35.lastName = capitalize(var35.lastName.replace('0', 'O').replace('8', 'B'));
               }

               if (TextUtils.isEmpty(var35.firstName) && TextUtils.isEmpty(var35.lastName)) {
                  return null;
               } else {
                  var35.issuingCountry = (String)var39.get(var35.issuingCountry);
                  var35.nationality = (String)var39.get(var35.nationality);
                  return var35;
               }
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   private static String russianPassportTranslit(String var0) {
      char[] var3 = var0.toCharArray();

      for(int var1 = 0; var1 < var3.length; ++var1) {
         int var2 = "ABVGDE2JZIQKLMNOPRSTUFHC34WXY9678".indexOf(var3[var1]);
         if (var2 != -1) {
            var3[var1] = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".charAt(var2);
         }
      }

      return new String(var3);
   }

   private static native void setYuvBitmapPixels(Bitmap var0, byte[] var1);

   public static class Result {
      public static final int GENDER_FEMALE = 2;
      public static final int GENDER_MALE = 1;
      public static final int GENDER_UNKNOWN = 0;
      public static final int TYPE_DRIVER_LICENSE = 4;
      public static final int TYPE_ID = 2;
      public static final int TYPE_INTERNAL_PASSPORT = 3;
      public static final int TYPE_PASSPORT = 1;
      public int birthDay;
      public int birthMonth;
      public int birthYear;
      public boolean doesNotExpire;
      public int expiryDay;
      public int expiryMonth;
      public int expiryYear;
      public String firstName;
      public int gender;
      public String issuingCountry;
      public String lastName;
      public boolean mainCheckDigitIsValid;
      public String middleName;
      public String nationality;
      public String number;
      public String rawMRZ;
      public int type;
   }
}

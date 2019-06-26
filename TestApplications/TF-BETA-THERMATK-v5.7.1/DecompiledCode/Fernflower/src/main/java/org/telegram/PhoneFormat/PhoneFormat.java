package org.telegram.PhoneFormat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.FileLog;

public class PhoneFormat {
   private static volatile PhoneFormat Instance;
   public ByteBuffer buffer;
   public HashMap callingCodeCountries;
   public HashMap callingCodeData;
   public HashMap callingCodeOffsets;
   public HashMap countryCallingCode;
   public byte[] data;
   public String defaultCallingCode;
   public String defaultCountry;
   private boolean initialzed = false;

   public PhoneFormat() {
      this.init((String)null);
   }

   public PhoneFormat(String var1) {
      this.init(var1);
   }

   public static PhoneFormat getInstance() {
      PhoneFormat var0 = Instance;
      PhoneFormat var1 = var0;
      if (var0 == null) {
         synchronized(PhoneFormat.class){}

         Throwable var10000;
         boolean var10001;
         label206: {
            try {
               var0 = Instance;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label206;
            }

            var1 = var0;
            if (var0 == null) {
               try {
                  var1 = new PhoneFormat();
                  Instance = var1;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label206;
               }
            }

            label193:
            try {
               return var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label193;
            }
         }

         while(true) {
            Throwable var22 = var10000;

            try {
               throw var22;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   public static String strip(String var0) {
      StringBuilder var2 = new StringBuilder(var0);

      for(int var1 = var2.length() - 1; var1 >= 0; --var1) {
         if (!"0123456789+*#".contains(var2.substring(var1, var1 + 1))) {
            var2.deleteCharAt(var1);
         }
      }

      return var2.toString();
   }

   public static String stripExceptNumbers(String var0) {
      return stripExceptNumbers(var0, false);
   }

   public static String stripExceptNumbers(String var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var2 = new StringBuilder(var0);
         var0 = "0123456789";
         if (var1) {
            StringBuilder var4 = new StringBuilder();
            var4.append("0123456789");
            var4.append("+");
            var0 = var4.toString();
         }

         for(int var3 = var2.length() - 1; var3 >= 0; --var3) {
            if (!var0.contains(var2.substring(var3, var3 + 1))) {
               var2.deleteCharAt(var3);
            }
         }

         return var2.toString();
      }
   }

   public String callingCodeForCountryCode(String var1) {
      return (String)this.countryCallingCode.get(var1.toLowerCase());
   }

   public CallingCodeInfo callingCodeInfo(String var1) {
      PhoneFormat var2 = this;
      CallingCodeInfo var3 = (CallingCodeInfo)this.callingCodeData.get(var1);
      CallingCodeInfo var4 = var3;
      if (var3 == null) {
         Integer var5 = (Integer)this.callingCodeOffsets.get(var1);
         var4 = var3;
         if (var5 != null) {
            byte[] var19 = this.data;
            int var6 = var5;
            var4 = new CallingCodeInfo();
            var4.callingCode = var1;
            var4.countries = (ArrayList)this.callingCodeCountries.get(var1);
            this.callingCodeData.put(var1, var4);
            short var7 = this.value16(var6);
            byte var8 = 2;
            int var9 = var6 + 2 + 2;
            short var10 = this.value16(var9);
            var9 = var9 + 2 + 2;
            short var11 = this.value16(var9);
            var9 = var9 + 2 + 2;
            ArrayList var21 = new ArrayList(5);

            while(true) {
               var1 = var2.valueString(var9);
               if (var1.length() == 0) {
                  var4.trunkPrefixes = var21;
                  ++var9;
                  var21 = new ArrayList(5);

                  while(true) {
                     var1 = var2.valueString(var9);
                     if (var1.length() == 0) {
                        var4.intlPrefixes = var21;
                        var21 = new ArrayList(var11);
                        var9 = var6 + var7;
                        int var22 = var9;
                        var6 = 0;

                        for(byte[] var18 = var19; var6 < var11; ++var6) {
                           RuleSet var20 = new RuleSet();
                           var20.matchLen = this.value16(var22);
                           var22 += var8;
                           short var12 = this.value16(var22);
                           ArrayList var13 = new ArrayList(var12);
                           var22 += var8;

                           for(int var14 = 0; var14 < var12; ++var14) {
                              PhoneRule var15 = new PhoneRule();
                              var15.minVal = this.value32(var22);
                              var22 += 4;
                              var15.maxVal = this.value32(var22);
                              int var16 = var22 + 4;
                              var22 = var16 + 1;
                              var15.byte8 = var18[var16];
                              var16 = var22 + 1;
                              var15.maxLen = var18[var22];
                              var22 = var16 + 1;
                              var15.otherFlag = var18[var16];
                              var16 = var22 + 1;
                              var15.prefixLen = var18[var22];
                              int var17 = var16 + 1;
                              var15.flag12 = var18[var16];
                              var22 = var17 + 1;
                              var15.flag13 = var18[var17];
                              short var24 = this.value16(var22);
                              var22 += var8;
                              var15.format = this.valueString(var9 + var10 + var24);
                              var16 = var15.format.indexOf("[[");
                              if (var16 != -1) {
                                 int var23 = var15.format.indexOf("]]");
                                 var15.format = String.format("%s%s", var15.format.substring(0, var16), var15.format.substring(var23 + 2));
                              }

                              var8 = 2;
                              var13.add(var15);
                              if (var15.hasIntlPrefix) {
                                 var20.hasRuleWithIntlPrefix = true;
                              }

                              if (var15.hasTrunkPrefix) {
                                 var20.hasRuleWithTrunkPrefix = true;
                              }
                           }

                           var20.rules = var13;
                           var21.add(var20);
                        }

                        var4.ruleSets = var21;
                        return var4;
                     }

                     var21.add(var1);
                     var9 += var1.length() + 1;
                  }
               }

               var21.add(var1);
               var9 += var1.length() + 1;
            }
         }
      }

      return var4;
   }

   public ArrayList countriesForCallingCode(String var1) {
      String var2 = var1;
      if (var1.startsWith("+")) {
         var2 = var1.substring(1);
      }

      return (ArrayList)this.callingCodeCountries.get(var2);
   }

   public String defaultCallingCode() {
      return this.callingCodeForCountryCode(this.defaultCountry);
   }

   public CallingCodeInfo findCallingCodeInfo(String var1) {
      CallingCodeInfo var2 = null;
      int var3 = 0;

      CallingCodeInfo var4;
      do {
         var4 = var2;
         if (var3 >= 3) {
            break;
         }

         var4 = var2;
         if (var3 >= var1.length()) {
            break;
         }

         ++var3;
         var4 = this.callingCodeInfo(var1.substring(0, var3));
         var2 = var4;
      } while(var4 == null);

      return var4;
   }

   public String format(String var1) {
      if (!this.initialzed) {
         return var1;
      } else {
         Exception var10000;
         label86: {
            String var2;
            CallingCodeInfo var15;
            String var16;
            boolean var10001;
            label87: {
               try {
                  var2 = strip(var1);
                  if (var2.startsWith("+")) {
                     var2 = var2.substring(1);
                     var15 = this.findCallingCodeInfo(var2);
                     break label87;
                  }
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label86;
               }

               CallingCodeInfo var4;
               try {
                  var4 = this.callingCodeInfo(this.defaultCallingCode);
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label86;
               }

               if (var4 == null) {
                  return var1;
               }

               String var3;
               try {
                  var3 = var4.matchingAccessCode(var2);
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label86;
               }

               if (var3 != null) {
                  CallingCodeInfo var5;
                  try {
                     var2 = var2.substring(var3.length());
                     var5 = this.findCallingCodeInfo(var2);
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label86;
                  }

                  var16 = var2;
                  if (var5 != null) {
                     try {
                        var16 = var5.format(var2);
                     } catch (Exception var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label86;
                     }
                  }

                  try {
                     if (var16.length() == 0) {
                        return var3;
                     }
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label86;
                  }

                  try {
                     return String.format("%s %s", var3, var16);
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label86;
                  }
               }

               try {
                  var16 = var4.format(var2);
                  return var16;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label86;
               }
            }

            var16 = var1;
            if (var15 != null) {
               try {
                  var2 = var15.format(var2);
                  StringBuilder var18 = new StringBuilder();
                  var18.append("+");
                  var18.append(var2);
                  var16 = var18.toString();
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label86;
               }
            }

            return var16;
         }

         Exception var17 = var10000;
         FileLog.e((Throwable)var17);
         return var1;
      }
   }

   public void init(String param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean isPhoneNumberValid(String var1) {
      boolean var2 = this.initialzed;
      boolean var3 = true;
      boolean var4 = true;
      if (!var2) {
         return true;
      } else {
         String var5 = strip(var1);
         CallingCodeInfo var6;
         if (var5.startsWith("+")) {
            var1 = var5.substring(1);
            var6 = this.findCallingCodeInfo(var1);
            if (var6 == null || !var6.isValidPhoneNumber(var1)) {
               var4 = false;
            }

            return var4;
         } else {
            var6 = this.callingCodeInfo(this.defaultCallingCode);
            if (var6 == null) {
               return false;
            } else {
               var1 = var6.matchingAccessCode(var5);
               if (var1 != null) {
                  var1 = var5.substring(var1.length());
                  if (var1.length() == 0) {
                     return false;
                  } else {
                     var6 = this.findCallingCodeInfo(var1);
                     if (var6 != null && var6.isValidPhoneNumber(var1)) {
                        var4 = var3;
                     } else {
                        var4 = false;
                     }

                     return var4;
                  }
               } else {
                  return var6.isValidPhoneNumber(var5);
               }
            }
         }
      }
   }

   public void parseDataHeader() {
      int var1 = 0;
      int var2 = this.value32(0);

      for(int var3 = 4; var1 < var2; ++var1) {
         String var4 = this.valueString(var3);
         var3 += 4;
         String var5 = this.valueString(var3);
         var3 += 4;
         int var6 = this.value32(var3);
         var3 += 4;
         if (var5.equals(this.defaultCountry)) {
            this.defaultCallingCode = var4;
         }

         this.countryCallingCode.put(var5, var4);
         this.callingCodeOffsets.put(var4, var6 + var2 * 12 + 4);
         ArrayList var7 = (ArrayList)this.callingCodeCountries.get(var4);
         ArrayList var8 = var7;
         if (var7 == null) {
            var8 = new ArrayList();
            this.callingCodeCountries.put(var4, var8);
         }

         var8.add(var5);
      }

      String var9 = this.defaultCallingCode;
      if (var9 != null) {
         this.callingCodeInfo(var9);
      }

   }

   short value16(int var1) {
      if (var1 + 2 <= this.data.length) {
         this.buffer.position(var1);
         return this.buffer.getShort();
      } else {
         return 0;
      }
   }

   int value32(int var1) {
      if (var1 + 4 <= this.data.length) {
         this.buffer.position(var1);
         return this.buffer.getInt();
      } else {
         return 0;
      }
   }

   public String valueString(int var1) {
      int var2 = var1;

      while(true) {
         label31: {
            Exception var10000;
            label35: {
               boolean var10001;
               try {
                  if (var2 >= this.data.length) {
                     return "";
                  }

                  if (this.data[var2] != 0) {
                     break label31;
                  }
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label35;
               }

               var2 -= var1;
               if (var1 == var2) {
                  return "";
               }

               try {
                  String var6 = new String(this.data, var1, var2);
                  return var6;
               } catch (Exception var4) {
                  var10000 = var4;
                  var10001 = false;
               }
            }

            Exception var3 = var10000;
            var3.printStackTrace();
            return "";
         }

         ++var2;
      }
   }
}

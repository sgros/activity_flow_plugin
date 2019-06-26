package org.telegram.messenger;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Emoji {
   private static final int MAX_RECENT_EMOJI_COUNT = 48;
   private static int bigImgSize;
   private static final int[][] cols;
   private static int drawImgSize;
   private static Bitmap[][] emojiBmp = new Bitmap[8][4];
   public static HashMap emojiColor = new HashMap();
   public static HashMap emojiUseHistory = new HashMap();
   private static boolean inited = false;
   private static boolean[][] loadingEmoji = new boolean[8][4];
   private static Paint placeholderPaint;
   public static ArrayList recentEmoji = new ArrayList();
   private static boolean recentEmojiLoaded;
   private static HashMap rects = new HashMap();
   private static final int splitCount = 4;

   static {
      int[] var0 = new int[]{16, 16, 16, 16};
      byte var1 = 2;
      int[] var2 = new int[]{5, 5, 5, 5};
      int[] var3 = new int[]{7, 7, 7, 7};
      int[] var4 = new int[]{5, 5, 5, 5};
      int[] var5 = new int[]{8, 8, 8, 8};
      cols = new int[][]{var0, {6, 6, 6, 6}, var2, var3, var4, {7, 7, 7, 7}, {8, 8, 8, 8}, var5};
      float var6 = AndroidUtilities.density;
      byte var7 = 66;
      if (var6 <= 1.0F) {
         var7 = 33;
         var1 = 1;
      } else if (var6 <= 1.5F) {
      }

      drawImgSize = AndroidUtilities.dp(20.0F);
      if (AndroidUtilities.isTablet()) {
         var6 = 40.0F;
      } else {
         var6 = 34.0F;
      }

      bigImgSize = AndroidUtilities.dp(var6);
      int var8 = 0;

      while(true) {
         String[][] var16 = EmojiData.data;
         if (var8 >= var16.length) {
            placeholderPaint = new Paint();
            placeholderPaint.setColor(0);
            return;
         }

         int var9 = (int)Math.ceil((double)((float)var16[var8].length / 4.0F));

         for(int var10 = 0; var10 < EmojiData.data[var8].length; ++var10) {
            int var11 = var10 / var9;
            int var12 = var10 - var11 * var9;
            int[][] var17 = cols;
            int var13 = var12 % var17[var8][var11];
            int var14 = var12 / var17[var8][var11];
            var12 = var13 * var1;
            int var15 = var14 * var1;
            Rect var18 = new Rect(var13 * var7 + var12, var14 * var7 + var15, (var13 + 1) * var7 + var12, (var14 + 1) * var7 + var15);
            rects.put(EmojiData.data[var8][var10], new Emoji.DrawableInfo(var18, (byte)var8, (byte)var11, var10));
         }

         ++var8;
      }
   }

   public static void addRecentEmoji(String var0) {
      Integer var1 = (Integer)emojiUseHistory.get(var0);
      Integer var2 = var1;
      if (var1 == null) {
         var2 = 0;
      }

      if (var2 == 0 && emojiUseHistory.size() >= 48) {
         ArrayList var3 = recentEmoji;
         String var4 = (String)var3.get(var3.size() - 1);
         emojiUseHistory.remove(var4);
         var3 = recentEmoji;
         var3.set(var3.size() - 1, var0);
      }

      emojiUseHistory.put(var0, var2 + 1);
   }

   public static void clearRecentEmoji() {
      MessagesController.getGlobalEmojiSettings().edit().putBoolean("filled_default", true).commit();
      emojiUseHistory.clear();
      recentEmoji.clear();
      saveRecentEmoji();
   }

   public static String fixEmoji(String var0) {
      int var1 = var0.length();

      String var6;
      for(int var2 = 0; var2 < var1; var0 = var6) {
         int var4;
         int var5;
         label58: {
            char var3 = var0.charAt(var2);
            StringBuilder var8;
            if (var3 >= '\ud83c' && var3 <= '\ud83e') {
               if (var3 != '\ud83c' || var2 >= var1 - 1) {
                  var4 = var2 + 1;
                  var5 = var1;
                  var6 = var0;
                  break label58;
               }

               var4 = var2 + 1;
               char var7 = var0.charAt(var4);
               if (var7 != '\ude2f' && var7 != '\udc04' && var7 != '\ude1a' && var7 != '\udd7f') {
                  var5 = var1;
                  var6 = var0;
                  break label58;
               }

               var8 = new StringBuilder();
               var4 = var2 + 2;
               var8.append(var0.substring(0, var4));
               var8.append("️");
               var8.append(var0.substring(var4));
               var0 = var8.toString();
            } else {
               if (var3 == 8419) {
                  return var0;
               }

               var5 = var1;
               var6 = var0;
               var4 = var2;
               if (var3 < 8252) {
                  break label58;
               }

               var5 = var1;
               var6 = var0;
               var4 = var2;
               if (var3 > 12953) {
                  break label58;
               }

               var5 = var1;
               var6 = var0;
               var4 = var2;
               if (!EmojiData.emojiToFE0FMap.containsKey(var3)) {
                  break label58;
               }

               var8 = new StringBuilder();
               var4 = var2 + 1;
               var8.append(var0.substring(0, var4));
               var8.append("️");
               var8.append(var0.substring(var4));
               var0 = var8.toString();
            }

            var5 = var1 + 1;
            var6 = var0;
         }

         var2 = var4 + 1;
         var1 = var5;
      }

      return var0;
   }

   public static Drawable getEmojiBigDrawable(String var0) {
      Emoji.EmojiDrawable var1 = getEmojiDrawable(var0);
      Emoji.EmojiDrawable var2 = var1;
      if (var1 == null) {
         CharSequence var4 = (CharSequence)EmojiData.emojiAliasMap.get(var0);
         var2 = var1;
         if (var4 != null) {
            var2 = getEmojiDrawable(var4);
         }
      }

      if (var2 == null) {
         return null;
      } else {
         int var3 = bigImgSize;
         var2.setBounds(0, 0, var3, var3);
         var2.fullSize = true;
         return var2;
      }
   }

   public static Emoji.EmojiDrawable getEmojiDrawable(CharSequence var0) {
      Emoji.DrawableInfo var1 = (Emoji.DrawableInfo)rects.get(var0);
      Emoji.DrawableInfo var2 = var1;
      if (var1 == null) {
         CharSequence var3 = (CharSequence)EmojiData.emojiAliasMap.get(var0);
         var2 = var1;
         if (var3 != null) {
            var2 = (Emoji.DrawableInfo)rects.get(var3);
         }
      }

      if (var2 == null) {
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var6 = new StringBuilder();
            var6.append("No drawable for emoji ");
            var6.append(var0);
            FileLog.d(var6.toString());
         }

         return null;
      } else {
         Emoji.EmojiDrawable var5 = new Emoji.EmojiDrawable(var2);
         int var4 = drawImgSize;
         var5.setBounds(0, 0, var4, var4);
         return var5;
      }
   }

   private static boolean inArray(char var0, char[] var1) {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var1[var3] == var0) {
            return true;
         }
      }

      return false;
   }

   public static void invalidateAll(View var0) {
      if (var0 instanceof ViewGroup) {
         ViewGroup var2 = (ViewGroup)var0;

         for(int var1 = 0; var1 < var2.getChildCount(); ++var1) {
            invalidateAll(var2.getChildAt(var1));
         }
      } else if (var0 instanceof TextView) {
         var0.invalidate();
      }

   }

   public static boolean isValidEmoji(String var0) {
      Emoji.DrawableInfo var1 = (Emoji.DrawableInfo)rects.get(var0);
      Emoji.DrawableInfo var2 = var1;
      if (var1 == null) {
         CharSequence var4 = (CharSequence)EmojiData.emojiAliasMap.get(var0);
         var2 = var1;
         if (var4 != null) {
            var2 = (Emoji.DrawableInfo)rects.get(var4);
         }
      }

      boolean var3;
      if (var2 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   // $FF: synthetic method
   static void lambda$loadEmoji$0(int var0, int var1, Bitmap var2) {
      emojiBmp[var0][var1] = var2;
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.emojiDidLoad);
   }

   // $FF: synthetic method
   static int lambda$sortEmoji$1(String var0, String var1) {
      Integer var2 = (Integer)emojiUseHistory.get(var0);
      Integer var3 = (Integer)emojiUseHistory.get(var1);
      Integer var5 = 0;
      Integer var4 = var2;
      if (var2 == null) {
         var4 = var5;
      }

      var2 = var3;
      if (var3 == null) {
         var2 = var5;
      }

      if (var4 > var2) {
         return -1;
      } else {
         return var4 < var2 ? 1 : 0;
      }
   }

   private static void loadEmoji(int param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static void loadRecentEmoji() {
      if (!recentEmojiLoaded) {
         recentEmojiLoaded = true;
         SharedPreferences var0 = MessagesController.getGlobalEmojiSettings();

         String var1;
         int var3;
         Exception var10000;
         boolean var10001;
         String[] var26;
         Exception var27;
         label185: {
            label191: {
               label192: {
                  int var8;
                  label204: {
                     try {
                        emojiUseHistory.clear();
                        if (!var0.contains("emojis")) {
                           break label204;
                        }

                        var1 = var0.getString("emojis", "");
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label191;
                     }

                     if (var1 != null) {
                        label203: {
                           int var2;
                           try {
                              if (var1.length() <= 0) {
                                 break label203;
                              }

                              var26 = var1.split(",");
                              var2 = var26.length;
                           } catch (Exception var22) {
                              var10000 = var22;
                              var10001 = false;
                              break label191;
                           }

                           for(var3 = 0; var3 < var2; ++var3) {
                              String[] var4;
                              long var5;
                              StringBuilder var7;
                              try {
                                 var4 = var26[var3].split("=");
                                 var5 = Utilities.parseLong(var4[0]);
                                 var7 = new StringBuilder();
                              } catch (Exception var20) {
                                 var10000 = var20;
                                 var10001 = false;
                                 break label191;
                              }

                              for(var8 = 0; var8 < 4; ++var8) {
                                 try {
                                    var7.insert(0, String.valueOf((char)((int)var5)));
                                 } catch (Exception var19) {
                                    var10000 = var19;
                                    var10001 = false;
                                    break label191;
                                 }

                                 var5 >>= 16;
                                 if (var5 == 0L) {
                                    break;
                                 }
                              }

                              try {
                                 if (var7.length() > 0) {
                                    emojiUseHistory.put(var7.toString(), Utilities.parseInt(var4[1]));
                                 }
                              } catch (Exception var21) {
                                 var10000 = var21;
                                 var10001 = false;
                                 break label191;
                              }
                           }
                        }
                     }

                     try {
                        var0.edit().remove("emojis").commit();
                        saveRecentEmoji();
                        break label192;
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label191;
                     }
                  }

                  try {
                     var1 = var0.getString("emojis2", "");
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label191;
                  }

                  if (var1 != null) {
                     label197: {
                        String[] var28;
                        try {
                           if (var1.length() <= 0) {
                              break label197;
                           }

                           var28 = var1.split(",");
                           var8 = var28.length;
                        } catch (Exception var23) {
                           var10000 = var23;
                           var10001 = false;
                           break label191;
                        }

                        for(var3 = 0; var3 < var8; ++var3) {
                           try {
                              var26 = var28[var3].split("=");
                              emojiUseHistory.put(var26[0], Utilities.parseInt(var26[1]));
                           } catch (Exception var16) {
                              var10000 = var16;
                              var10001 = false;
                              break label191;
                           }
                        }
                     }
                  }
               }

               label199: {
                  try {
                     if (!emojiUseHistory.isEmpty() || var0.getBoolean("filled_default", false)) {
                        break label199;
                     }

                     var26 = new String[34];
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label191;
                  }

                  var26[0] = "\ud83d\ude02";
                  var26[1] = "\ud83d\ude18";
                  var26[2] = "❤";
                  var26[3] = "\ud83d\ude0d";
                  var26[4] = "\ud83d\ude0a";
                  var26[5] = "\ud83d\ude01";
                  var26[6] = "\ud83d\udc4d";
                  var26[7] = "☺";
                  var26[8] = "\ud83d\ude14";
                  var26[9] = "\ud83d\ude04";
                  var26[10] = "\ud83d\ude2d";
                  var26[11] = "\ud83d\udc8b";
                  var26[12] = "\ud83d\ude12";
                  var26[13] = "\ud83d\ude33";
                  var26[14] = "\ud83d\ude1c";
                  var26[15] = "\ud83d\ude48";
                  var26[16] = "\ud83d\ude09";
                  var26[17] = "\ud83d\ude03";
                  var26[18] = "\ud83d\ude22";
                  var26[19] = "\ud83d\ude1d";
                  var26[20] = "\ud83d\ude31";
                  var26[21] = "\ud83d\ude21";
                  var26[22] = "\ud83d\ude0f";
                  var26[23] = "\ud83d\ude1e";
                  var26[24] = "\ud83d\ude05";
                  var26[25] = "\ud83d\ude1a";
                  var26[26] = "\ud83d\ude4a";
                  var26[27] = "\ud83d\ude0c";
                  var26[28] = "\ud83d\ude00";
                  var26[29] = "\ud83d\ude0b";
                  var26[30] = "\ud83d\ude06";
                  var26[31] = "\ud83d\udc4c";
                  var26[32] = "\ud83d\ude10";
                  var26[33] = "\ud83d\ude15";
                  var3 = 0;

                  while(true) {
                     try {
                        if (var3 >= var26.length) {
                           break;
                        }

                        emojiUseHistory.put(var26[var3], var26.length - var3);
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label191;
                     }

                     ++var3;
                  }

                  try {
                     var0.edit().putBoolean("filled_default", true).commit();
                     saveRecentEmoji();
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label191;
                  }
               }

               try {
                  sortEmoji();
                  break label185;
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
               }
            }

            var27 = var10000;
            FileLog.e((Throwable)var27);
         }

         label201: {
            try {
               var1 = var0.getString("color", "");
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label201;
            }

            if (var1 == null) {
               return;
            }

            String[] var25;
            try {
               if (var1.length() <= 0) {
                  return;
               }

               var25 = var1.split(",");
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label201;
            }

            var3 = 0;

            while(true) {
               try {
                  if (var3 >= var25.length) {
                     return;
                  }

                  var26 = var25[var3].split("=");
                  emojiColor.put(var26[0], var26[1]);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }

               ++var3;
            }
         }

         var27 = var10000;
         FileLog.e((Throwable)var27);
      }
   }

   public static CharSequence replaceEmoji(CharSequence var0, FontMetricsInt var1, int var2, boolean var3) {
      return replaceEmoji(var0, var1, var2, var3, (int[])null);
   }

   public static CharSequence replaceEmoji(CharSequence var0, FontMetricsInt var1, int var2, boolean var3, int[] var4) {
      if (!SharedConfig.useSystemEmoji && var0 != null && var0.length() != 0) {
         Spannable var5;
         if (!var3 && var0 instanceof Spannable) {
            var5 = (Spannable)var0;
         } else {
            var5 = Factory.getInstance().newSpannable(var0.toString());
         }

         StringBuilder var6 = new StringBuilder(16);
         new StringBuilder(2);
         int var7 = var0.length();
         long var8 = 0L;
         int var10 = 0;
         int var11 = -1;
         int var12 = 0;
         int var13 = 0;
         boolean var14 = false;

         int var17;
         for(int var15 = 0; var10 < var7; var10 = var17 + 1) {
            label376: {
               Exception var10000;
               label427: {
                  char var16;
                  boolean var10001;
                  try {
                     var16 = var0.charAt(var10);
                  } catch (Exception var47) {
                     var10000 = var47;
                     var10001 = false;
                     break label427;
                  }

                  char var21;
                  if ((var16 < '\ud83c' || var16 > '\ud83e') && (var8 == 0L || (var8 & -4294967296L) != 0L || (var8 & 65535L) != 55356L || var16 < '\udde6' || var16 > '\uddff')) {
                     label429: {
                        label397: {
                           label398: {
                              try {
                                 if (var6.length() <= 0) {
                                    break label398;
                                 }
                              } catch (Exception var46) {
                                 var10000 = var46;
                                 var10001 = false;
                                 break label427;
                              }

                              if (var16 == 9792 || var16 == 9794 || var16 == 9877) {
                                 try {
                                    var6.append(var16);
                                 } catch (Exception var43) {
                                    var10000 = var43;
                                    var10001 = false;
                                    break label427;
                                 }

                                 ++var12;
                                 var8 = 0L;
                                 var13 = var11;
                                 break label397;
                              }
                           }

                           if (var8 > 0L && ('\uf000' & var16) == 53248) {
                              try {
                                 var6.append(var16);
                              } catch (Exception var42) {
                                 var10000 = var42;
                                 var10001 = false;
                                 break label427;
                              }

                              ++var12;
                              var8 = 0L;
                              var13 = var11;
                           } else {
                              label416: {
                                 int var18;
                                 int[] var19;
                                 boolean var20;
                                 if (var16 == 8419) {
                                    var17 = var11;
                                    var18 = var12;
                                    var19 = var4;
                                    var20 = var14;
                                    if (var10 > 0) {
                                       try {
                                          var21 = var0.charAt(var13);
                                       } catch (Exception var39) {
                                          var10000 = var39;
                                          var10001 = false;
                                          break label427;
                                       }

                                       boolean var50;
                                       if ((var21 < '0' || var21 > '9') && var21 != '#' && var21 != '*') {
                                          var50 = var14;
                                       } else {
                                          try {
                                             var6.append(var21);
                                             var6.append(var16);
                                          } catch (Exception var38) {
                                             var10000 = var38;
                                             var10001 = false;
                                             break label427;
                                          }

                                          var12 = var10 - var13 + 1;
                                          var11 = var13;
                                          var50 = true;
                                       }

                                       var17 = var11;
                                       var18 = var12;
                                       var19 = var4;
                                       var20 = var50;
                                    }
                                 } else {
                                    if (var16 == 169 || var16 == 174 || var16 >= 8252 && var16 <= 12953) {
                                       try {
                                          if (EmojiData.dataCharsMap.containsKey(var16)) {
                                             break label416;
                                          }
                                       } catch (Exception var45) {
                                          var10000 = var45;
                                          var10001 = false;
                                          break label427;
                                       }
                                    }

                                    if (var11 != -1) {
                                       try {
                                          var6.setLength(0);
                                       } catch (Exception var40) {
                                          var10000 = var40;
                                          var10001 = false;
                                          break label427;
                                       }

                                       var11 = 0;
                                       var12 = -1;
                                       var14 = false;
                                       break label429;
                                    }

                                    var17 = var11;
                                    var18 = var12;
                                    var19 = var4;
                                    var20 = var14;
                                    if (var16 != '️') {
                                       var17 = var11;
                                       var18 = var12;
                                       var19 = var4;
                                       var20 = var14;
                                       if (var4 != null) {
                                          var4[0] = 0;
                                          var19 = null;
                                          var20 = var14;
                                          var18 = var12;
                                          var17 = var11;
                                       }
                                    }
                                 }

                                 var11 = var18;
                                 var14 = var20;
                                 var4 = var19;
                                 var12 = var17;
                                 break label429;
                              }

                              var13 = var11;
                              if (var11 == -1) {
                                 var13 = var10;
                              }

                              ++var12;

                              try {
                                 var6.append(var16);
                              } catch (Exception var41) {
                                 var10000 = var41;
                                 var10001 = false;
                                 break label427;
                              }
                           }
                        }

                        var14 = true;
                        var11 = var12;
                        var12 = var13;
                     }
                  } else {
                     var13 = var11;
                     if (var11 == -1) {
                        var13 = var10;
                     }

                     try {
                        var6.append(var16);
                     } catch (Exception var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label427;
                     }

                     var11 = var12 + 1;
                     var8 = var8 << 16 | (long)var16;
                     var12 = var13;
                  }

                  label291: {
                     if (var14) {
                        var13 = var10 + 2;
                        if (var13 < var7) {
                           var17 = var10 + 1;

                           char var55;
                           try {
                              var55 = var0.charAt(var17);
                           } catch (Exception var35) {
                              var10000 = var35;
                              var10001 = false;
                              break label427;
                           }

                           if (var55 == '\ud83c') {
                              try {
                                 var55 = var0.charAt(var13);
                              } catch (Exception var34) {
                                 var10000 = var34;
                                 var10001 = false;
                                 break label427;
                              }

                              if (var55 >= '\udffb' && var55 <= '\udfff') {
                                 try {
                                    var6.append(var0.subSequence(var17, var10 + 3));
                                 } catch (Exception var33) {
                                    var10000 = var33;
                                    var10001 = false;
                                    break label427;
                                 }

                                 var11 += 2;
                                 break label291;
                              }
                           } else {
                              label286: {
                                 try {
                                    if (var6.length() < 2 || var6.charAt(0) != '\ud83c' || var6.charAt(1) != '\udff4') {
                                       break label286;
                                    }
                                 } catch (Exception var37) {
                                    var10000 = var37;
                                    var10001 = false;
                                    break label427;
                                 }

                                 if (var55 == '\udb40') {
                                    var13 = var17;

                                    while(true) {
                                       var10 = var13 + 2;

                                       try {
                                          var6.append(var0.subSequence(var13, var10));
                                       } catch (Exception var32) {
                                          var10000 = var32;
                                          var10001 = false;
                                          break label427;
                                       }

                                       var11 += 2;

                                       try {
                                          if (var10 >= var0.length() || var0.charAt(var10) != '\udb40') {
                                             break;
                                          }
                                       } catch (Exception var36) {
                                          var10000 = var36;
                                          var10001 = false;
                                          break label427;
                                       }

                                       var13 = var10;
                                    }

                                    var13 = var10 - 1;
                                    break label291;
                                 }
                              }
                           }
                        }
                     }

                     var13 = var10;
                  }

                  var10 = var11;
                  boolean var49 = var14;
                  int var56 = 0;
                  int var51 = var13;

                  while(true) {
                     if (var56 >= 3) {
                        label223: {
                           if (var49) {
                              var17 = var51 + 2;
                              if (var17 < var7) {
                                 label408: {
                                    var56 = var51 + 1;

                                    char var54;
                                    try {
                                       if (var0.charAt(var56) != '\ud83c') {
                                          break label408;
                                       }

                                       var54 = var0.charAt(var17);
                                    } catch (Exception var29) {
                                       var10000 = var29;
                                       var10001 = false;
                                       break;
                                    }

                                    if (var54 >= '\udffb' && var54 <= '\udfff') {
                                       try {
                                          var6.append(var0.subSequence(var56, var51 + 3));
                                       } catch (Exception var28) {
                                          var10000 = var28;
                                          var10001 = false;
                                          break;
                                       }

                                       var10 += 2;
                                       break label223;
                                    }
                                 }
                              }
                           }

                           var17 = var51;
                        }

                        if (var49) {
                           if (var4 != null) {
                              int var10002 = var4[0]++;
                           }

                           Emoji.EmojiDrawable var53;
                           try {
                              var53 = getEmojiDrawable(var6.subSequence(0, var6.length()));
                           } catch (Exception var27) {
                              var10000 = var27;
                              var10001 = false;
                              break;
                           }

                           if (var53 != null) {
                              try {
                                 Emoji.EmojiSpan var23 = new Emoji.EmojiSpan(var53, 0, var2, var1);
                                 var5.setSpan(var23, var12, var10 + var12, 33);
                              } catch (Exception var26) {
                                 var10000 = var26;
                                 var10001 = false;
                                 break;
                              }

                              ++var15;
                           }

                           try {
                              var6.setLength(0);
                           } catch (Exception var25) {
                              var10000 = var25;
                              var10001 = false;
                              break;
                           }

                           var10 = var15;
                           var12 = 0;
                           var11 = -1;
                           var14 = false;
                        } else {
                           var14 = var49;
                           var11 = var12;
                           var12 = var10;
                           var10 = var15;
                        }

                        try {
                           var15 = VERSION.SDK_INT;
                           break label376;
                        } catch (Exception var24) {
                           var10000 = var24;
                           var10001 = false;
                           break;
                        }
                     }

                     boolean var52;
                     label417: {
                        int var22 = var51 + 1;
                        if (var22 < var7) {
                           try {
                              var21 = var0.charAt(var22);
                           } catch (Exception var30) {
                              var10000 = var30;
                              var10001 = false;
                              break;
                           }

                           if (var56 == 1) {
                              if (var21 == 8205) {
                                 label415: {
                                    try {
                                       if (var6.length() <= 0) {
                                          break label415;
                                       }

                                       var6.append(var21);
                                    } catch (Exception var31) {
                                       var10000 = var31;
                                       var10001 = false;
                                       break;
                                    }

                                    var17 = var10 + 1;
                                    var51 = var22;
                                    var52 = false;
                                    break label417;
                                 }
                              }
                           } else if ((var12 != -1 || var16 == '*' || var16 >= '1' && var16 <= '9') && var21 >= '︀') {
                              var52 = var49;
                              var17 = var10;
                              if (var21 <= '️') {
                                 var17 = var10 + 1;
                                 var51 = var22;
                                 var52 = var49;
                              }
                              break label417;
                           }
                        }

                        var17 = var10;
                        var52 = var49;
                     }

                     ++var56;
                     var49 = var52;
                     var10 = var17;
                  }
               }

               Exception var48 = var10000;
               FileLog.e((Throwable)var48);
               return var0;
            }

            if (var15 < 23 && var10 >= 50) {
               break;
            }

            var15 = var10;
         }

         return var5;
      } else {
         return var0;
      }
   }

   public static void saveEmojiColors() {
      SharedPreferences var0 = MessagesController.getGlobalEmojiSettings();
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = emojiColor.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (var1.length() != 0) {
            var1.append(",");
         }

         var1.append((String)var3.getKey());
         var1.append("=");
         var1.append((String)var3.getValue());
      }

      var0.edit().putString("color", var1.toString()).commit();
   }

   public static void saveRecentEmoji() {
      SharedPreferences var0 = MessagesController.getGlobalEmojiSettings();
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = emojiUseHistory.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (var1.length() != 0) {
            var1.append(",");
         }

         var1.append((String)var3.getKey());
         var1.append("=");
         var1.append(var3.getValue());
      }

      var0.edit().putString("emojis2", var1.toString()).commit();
   }

   public static void sortEmoji() {
      recentEmoji.clear();
      Iterator var0 = emojiUseHistory.entrySet().iterator();

      while(var0.hasNext()) {
         Entry var1 = (Entry)var0.next();
         recentEmoji.add(var1.getKey());
      }

      Collections.sort(recentEmoji, _$$Lambda$Emoji$IRtAaHh32_YY7tgie8_WycuV8i0.INSTANCE);

      while(recentEmoji.size() > 48) {
         ArrayList var2 = recentEmoji;
         var2.remove(var2.size() - 1);
      }

   }

   private static class DrawableInfo {
      public int emojiIndex;
      public byte page;
      public byte page2;
      public Rect rect;

      public DrawableInfo(Rect var1, byte var2, byte var3, int var4) {
         this.rect = var1;
         this.page = (byte)var2;
         this.page2 = (byte)var3;
         this.emojiIndex = var4;
      }
   }

   public static class EmojiDrawable extends Drawable {
      private static Paint paint = new Paint(2);
      private static Rect rect = new Rect();
      private boolean fullSize = false;
      private Emoji.DrawableInfo info;

      public EmojiDrawable(Emoji.DrawableInfo var1) {
         this.info = var1;
      }

      public void draw(Canvas var1) {
         Bitmap[][] var2 = Emoji.emojiBmp;
         Emoji.DrawableInfo var3 = this.info;
         if (var2[var3.page][var3.page2] == null) {
            boolean[][] var7 = Emoji.loadingEmoji;
            Emoji.DrawableInfo var6 = this.info;
            if (!var7[var6.page][var6.page2]) {
               var7 = Emoji.loadingEmoji;
               var6 = this.info;
               var7[var6.page][var6.page2] = true;
               Utilities.globalQueue.postRunnable(new _$$Lambda$Emoji$EmojiDrawable$tIn098DVTEVbhZUc7ywBHxfGQOU(this));
               var1.drawRect(this.getBounds(), Emoji.placeholderPaint);
            }
         } else {
            Rect var5;
            if (this.fullSize) {
               var5 = this.getDrawRect();
            } else {
               var5 = this.getBounds();
            }

            Bitmap[][] var4 = Emoji.emojiBmp;
            var3 = this.info;
            var1.drawBitmap(var4[var3.page][var3.page2], var3.rect, var5, paint);
         }
      }

      public Rect getDrawRect() {
         Rect var1 = this.getBounds();
         int var2 = var1.centerX();
         int var3 = var1.centerY();
         var1 = rect;
         int var4;
         if (this.fullSize) {
            var4 = Emoji.bigImgSize;
         } else {
            var4 = Emoji.drawImgSize;
         }

         var1.left = var2 - var4 / 2;
         var1 = rect;
         if (this.fullSize) {
            var4 = Emoji.bigImgSize;
         } else {
            var4 = Emoji.drawImgSize;
         }

         var1.right = var2 + var4 / 2;
         var1 = rect;
         if (this.fullSize) {
            var4 = Emoji.bigImgSize;
         } else {
            var4 = Emoji.drawImgSize;
         }

         var1.top = var3 - var4 / 2;
         var1 = rect;
         if (this.fullSize) {
            var4 = Emoji.bigImgSize;
         } else {
            var4 = Emoji.drawImgSize;
         }

         var1.bottom = var3 + var4 / 2;
         return rect;
      }

      public Emoji.DrawableInfo getDrawableInfo() {
         return this.info;
      }

      public int getOpacity() {
         return -2;
      }

      // $FF: synthetic method
      public void lambda$draw$0$Emoji$EmojiDrawable() {
         Emoji.DrawableInfo var1 = this.info;
         Emoji.loadEmoji(var1.page, var1.page2);
         boolean[][] var2 = Emoji.loadingEmoji;
         var1 = this.info;
         var2[var1.page][var1.page2] = false;
      }

      public void setAlpha(int var1) {
      }

      public void setColorFilter(ColorFilter var1) {
      }
   }

   public static class EmojiSpan extends ImageSpan {
      private FontMetricsInt fontMetrics;
      private int size = AndroidUtilities.dp(20.0F);

      public EmojiSpan(Emoji.EmojiDrawable var1, int var2, int var3, FontMetricsInt var4) {
         super(var1, var2);
         this.fontMetrics = var4;
         if (var4 != null) {
            this.size = Math.abs(this.fontMetrics.descent) + Math.abs(this.fontMetrics.ascent);
            if (this.size == 0) {
               this.size = AndroidUtilities.dp(20.0F);
            }
         }

      }

      public int getSize(Paint var1, CharSequence var2, int var3, int var4, FontMetricsInt var5) {
         FontMetricsInt var6 = var5;
         if (var5 == null) {
            var6 = new FontMetricsInt();
         }

         var5 = this.fontMetrics;
         if (var5 == null) {
            var4 = super.getSize(var1, var2, var3, var4, var6);
            int var7 = AndroidUtilities.dp(8.0F);
            int var8 = AndroidUtilities.dp(10.0F);
            var3 = -var8 - var7;
            var6.top = var3;
            var7 = var8 - var7;
            var6.bottom = var7;
            var6.ascent = var3;
            var6.leading = 0;
            var6.descent = var7;
            return var4;
         } else {
            if (var6 != null) {
               var6.ascent = var5.ascent;
               var6.descent = var5.descent;
               var6.top = var5.top;
               var6.bottom = var5.bottom;
            }

            if (this.getDrawable() != null) {
               Drawable var9 = this.getDrawable();
               var3 = this.size;
               var9.setBounds(0, 0, var3, var3);
            }

            return this.size;
         }
      }

      public void replaceFontMetrics(FontMetricsInt var1, int var2) {
         this.fontMetrics = var1;
         this.size = var2;
      }
   }
}

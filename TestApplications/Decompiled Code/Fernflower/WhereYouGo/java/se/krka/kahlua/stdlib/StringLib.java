package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

public final class StringLib implements JavaFunction {
   private static final int BYTE = 2;
   private static final int CAP_POSITION = -2;
   private static final int CAP_UNFINISHED = -1;
   private static final int CHAR = 1;
   private static final int FIND = 7;
   private static final int FORMAT = 6;
   private static final int GSUB = 9;
   private static final int LOWER = 3;
   private static final int LUA_MAXCAPTURES = 32;
   private static final char L_ESC = '%';
   private static final int MATCH = 8;
   private static final int NUM_FUNCTIONS = 10;
   private static final int REVERSE = 5;
   private static final boolean[] SPECIALS = new boolean[256];
   public static final Class STRING_CLASS;
   private static final int SUB = 0;
   private static final int UPPER = 4;
   private static final char[] digits;
   private static StringLib[] functions;
   private static final String[] names;
   private int methodId;

   static {
      int var0;
      for(var0 = 0; var0 < "^$*+?.([%-".length(); ++var0) {
         SPECIALS["^$*+?.([%-".charAt(var0)] = true;
      }

      STRING_CLASS = "".getClass();
      names = new String[10];
      names[0] = "sub";
      names[1] = "char";
      names[2] = "byte";
      names[3] = "lower";
      names[4] = "upper";
      names[5] = "reverse";
      names[6] = "format";
      names[7] = "find";
      names[8] = "match";
      names[9] = "gsub";
      functions = new StringLib[10];

      for(var0 = 0; var0 < 10; ++var0) {
         functions[var0] = new StringLib(var0);
      }

      digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   }

   public StringLib(int var1) {
      this.methodId = var1;
   }

   private static String addString(StringLib.MatchState var0, Object var1, StringLib.StringPointer var2, StringLib.StringPointer var3) {
      String var4 = BaseLib.tostring(var1, var0.callFrame.thread.state);
      StringLib.StringPointer var5 = new StringLib.StringPointer(var4);
      StringBuffer var11 = new StringBuffer();

      for(int var6 = 0; var6 < var4.length(); ++var6) {
         if (var5.getChar(var6) != '%') {
            var11.append(var5.getChar(var6));
         } else {
            int var7 = var6 + 1;
            if (!Character.isDigit(var5.getChar(var7))) {
               var11.append(var5.getChar(var7));
               var6 = var7;
            } else if (var5.getChar(var7) == '0') {
               String var8 = var2.getString();
               int var9 = var2.length() - var3.length();
               var6 = var9;
               if (var9 > var8.length()) {
                  var6 = var8.length();
               }

               var11.append(var8.substring(0, var6));
               var6 = var7;
            } else {
               var6 = var5.getChar(var7) - 49;
               Object[] var12 = var0.getCaptures();
               if (var12 == null || var6 > var0.level) {
                  throw new RuntimeException("invalid capture index");
               }

               Object var10 = var12[var6];
               if (var10 instanceof Double) {
                  Double var13 = (Double)var10;
                  if (var13 - (double)var13.intValue() == 0.0D) {
                     var11.append(String.valueOf(((Double)var10).intValue()));
                     var6 = var7;
                  } else {
                     var11.append(String.valueOf((Double)var10));
                     var6 = var7;
                  }
               } else {
                  var11.append(var10);
                  var6 = var7;
               }
            }
         }
      }

      return var11.toString();
   }

   private static void addValue(StringLib.MatchState var0, Object var1, StringBuffer var2, StringLib.StringPointer var3, StringLib.StringPointer var4) {
      String var5 = BaseLib.type(var1);
      if (var5 != "number" && var5 != "string") {
         String var7 = var3.getString().substring(0, var4.getIndex() - var3.getIndex());
         Object[] var8 = var0.getCaptures();
         if (var8 != null) {
            var7 = BaseLib.rawTostring(var8[0]);
         }

         var4 = null;
         Object var6;
         if (var5 == "function") {
            var6 = var0.callFrame.thread.state.call(var1, var7, (Object)null, (Object)null);
         } else {
            var6 = var4;
            if (var5 == "table") {
               var6 = ((LuaTable)var1).rawget(var7);
            }
         }

         var1 = var6;
         if (var6 == null) {
            var1 = var7;
         }

         var2.append(BaseLib.rawTostring(var1));
      } else {
         var2.append(addString(var0, var1, var3, var4));
      }

   }

   private void append(StringBuffer var1, String var2, int var3, int var4) {
      while(var3 < var4) {
         var1.append(var2.charAt(var3));
         ++var3;
      }

   }

   private void appendPrecisionNumber(StringBuffer var1, double var2, int var4, boolean var5) {
      var2 = MathLib.roundToPrecision(var2, var4);
      double var6 = Math.floor(var2);
      var2 -= var6;

      for(int var8 = 0; var8 < var4; ++var8) {
         var2 *= 10.0D;
      }

      var2 = MathLib.round(var6 + var2);
      stringBufferAppend(var1, var6, 10, true, 0);
      if (var5 || var4 > 0) {
         var1.append('.');
      }

      stringBufferAppend(var1, var2 - var6, 10, false, var4);
   }

   private void appendScientificNumber(StringBuffer var1, double var2, int var4, boolean var5, boolean var6) {
      int var7 = 0;

      int var9;
      for(int var8 = 0; var8 < 2; ++var8) {
         var9 = var7;
         double var10 = var2;
         double var12;
         if (var2 >= 1.0D) {
            var9 = var7;

            while(true) {
               var7 = var9;
               var12 = var2;
               if (var2 < 10.0D) {
                  break;
               }

               var2 /= 10.0D;
               ++var9;
            }
         } else {
            while(true) {
               var7 = var9;
               var12 = var10;
               if (var10 <= 0.0D) {
                  break;
               }

               var7 = var9;
               var12 = var10;
               if (var10 >= 1.0D) {
                  break;
               }

               var10 *= 10.0D;
               --var9;
            }
         }

         var2 = MathLib.roundToPrecision(var12, var4);
      }

      var9 = Math.abs(var7);
      char var14;
      byte var15;
      if (var7 >= 0) {
         var15 = 43;
         var14 = (char)var15;
      } else {
         var15 = 45;
         var14 = (char)var15;
      }

      if (var6) {
         this.appendSignificantNumber(var1, var2, var4, var5);
      } else {
         this.appendPrecisionNumber(var1, var2, var4, var5);
      }

      var1.append('e');
      var1.append(var14);
      stringBufferAppend(var1, (double)var9, 10, true, 2);
   }

   private void appendSignificantNumber(StringBuffer var1, double var2, int var4, boolean var5) {
      double var6 = Math.floor(var2);
      stringBufferAppend(var1, var6, 10, true, 0);
      var2 = MathLib.roundToSignificantNumbers(var2 - var6, var4);
      boolean var8;
      if (var6 == 0.0D && var2 != 0.0D) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var9 = 0;
      int var10 = var4;

      int var13;
      for(int var11 = 0; var11 < var10; var9 = var13) {
         var2 *= 10.0D;
         int var12 = var10;
         var13 = var9;
         if (Math.floor(var2) == 0.0D) {
            var12 = var10;
            var13 = var9;
            if (var2 != 0.0D) {
               ++var9;
               var12 = var10;
               var13 = var9;
               if (var8) {
                  var12 = var10 + 1;
                  var13 = var9;
               }
            }
         }

         ++var11;
         var10 = var12;
      }

      var2 = MathLib.round(var2);
      var6 = var2;
      int var14 = var4;
      if (!var5) {
         while(true) {
            var6 = var2;
            var14 = var4;
            if (var2 <= 0.0D) {
               break;
            }

            var6 = var2;
            var14 = var4;
            if (var2 % 10.0D != 0.0D) {
               break;
            }

            var2 /= 10.0D;
            --var4;
         }
      }

      var1.append('.');
      var4 = var1.length();
      this.extend(var1, var9, '0');
      var13 = var1.length();
      stringBufferAppend(var1, var6, 10, false, 0);
      var13 = var1.length() - var13;
      if (var5 && var13 < var14) {
         this.extend(var1, var14 - var13 - var9, '0');
      }

      if (!var5 && var4 == var1.length()) {
         var1.delete(var4 - 1, var1.length());
      }

   }

   private static int captureToClose(StringLib.MatchState var0) {
      for(int var1 = var0.level - 1; var1 >= 0; --var1) {
         if (var0.capture[var1].len == -1) {
            return var1;
         }
      }

      throw new RuntimeException("invalid pattern capture");
   }

   private static int checkCapture(StringLib.MatchState var0, int var1) {
      var1 -= 49;
      boolean var2;
      if (var1 >= 0 && var1 < var0.level && var0.capture[var1].len != -1) {
         var2 = false;
      } else {
         var2 = true;
      }

      BaseLib.luaAssert(var2, "invalid capture index");
      return var1;
   }

   private static StringLib.StringPointer classEnd(StringLib.MatchState var0, StringLib.StringPointer var1) {
      boolean var2 = false;
      StringLib.StringPointer var3 = var1.getClone();
      switch(var3.postIncrString(1)) {
      case '%':
         if (var3.getChar() != 0) {
            var2 = true;
         }

         BaseLib.luaAssert(var2, "malformed pattern (ends with '%')");
         var3.postIncrString(1);
         break;
      case '[':
         if (var3.getChar() == '^') {
            var3.postIncrString(1);
         }

         do {
            if (var3.getChar() != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            BaseLib.luaAssert(var2, "malformed pattern (missing ']')");
            if (var3.postIncrString(1) == '%' && var3.getChar() != 0) {
               var3.postIncrString(1);
            }
         } while(var3.getChar() != ']');

         var3.postIncrString(1);
      }

      return var3;
   }

   private static StringLib.StringPointer endCapture(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      int var3 = captureToClose(var0);
      var0.capture[var3].len = var0.capture[var3].init.length() - var1.length();
      var1 = match(var0, var1, var2);
      if (var1 == null) {
         var0.capture[var3].len = -1;
      }

      return var1;
   }

   private void extend(StringBuffer var1, int var2, char var3) {
      int var4 = var1.length();
      var1.setLength(var4 + var2);
      --var2;

      while(var2 >= 0) {
         var1.setCharAt(var4 + var2, var3);
         --var2;
      }

   }

   private static int findAux(LuaCallFrame var0, boolean var1) {
      String var2;
      if (var1) {
         var2 = names[7];
      } else {
         var2 = names[8];
      }

      String var3 = (String)BaseLib.getArg(var0, 1, "string", var2);
      String var4 = (String)BaseLib.getArg(var0, 2, "string", var2);
      Double var10 = (Double)BaseLib.getOptArg(var0, 3, "number");
      boolean var5 = LuaState.boolEval(BaseLib.getOptArg(var0, 4, "boolean"));
      int var6;
      if (var10 == null) {
         var6 = 0;
      } else {
         var6 = var10.intValue() - 1;
      }

      int var7;
      if (var6 < 0) {
         var6 += var3.length();
         var7 = var6;
         if (var6 < 0) {
            var7 = 0;
         }
      } else {
         var7 = var6;
         if (var6 > var3.length()) {
            var7 = var3.length();
         }
      }

      if (var1 && (var5 || noSpecialChars(var4))) {
         var7 = var3.indexOf(var4, var7);
         if (var7 > -1) {
            var7 = var0.push(LuaState.toDouble((long)(var7 + 1)), LuaState.toDouble((long)(var4.length() + var7)));
            return var7;
         }
      } else {
         StringLib.StringPointer var11 = new StringLib.StringPointer(var3);
         StringLib.StringPointer var13 = new StringLib.StringPointer(var4);
         StringLib.MatchState var8 = new StringLib.MatchState();
         boolean var14 = false;
         if (var13.getChar() == '^') {
            var14 = true;
            var13.postIncrString(1);
         }

         StringLib.StringPointer var12 = var11.getClone();
         var12.postIncrString(var7);
         var8.callFrame = var0;
         var8.src_init = var11.getClone();
         var8.endIndex = var11.getString().length();

         do {
            var8.level = 0;
            StringLib.StringPointer var9 = match(var8, var12, var13);
            if (var9 != null) {
               if (var1) {
                  var7 = var0.push(new Double((double)(var11.length() - var12.length() + 1)), new Double((double)(var11.length() - var9.length()))) + push_captures(var8, (StringLib.StringPointer)null, (StringLib.StringPointer)null);
               } else {
                  var7 = push_captures(var8, var12, var9);
               }

               return var7;
            }
         } while(var12.postIncrStringI(1) < var8.endIndex && !var14);
      }

      var7 = var0.pushNil();
      return var7;
   }

   private int format(LuaCallFrame var1, int var2) {
      String var3 = (String)BaseLib.getArg(var1, 1, "string", names[6]);
      int var4 = var3.length();
      int var5 = 2;
      StringBuffer var6 = new StringBuffer();

      label320:
      for(var2 = 0; var2 < var4; ++var2) {
         char var7 = var3.charAt(var2);
         if (var7 == '%') {
            int var8 = var2 + 1;
            boolean var9;
            if (var8 < var4) {
               var9 = true;
            } else {
               var9 = false;
            }

            BaseLib.luaAssert(var9, "incomplete option to 'format'");
            char var10 = var3.charAt(var8);
            if (var10 == '%') {
               var6.append('%');
               var2 = var8;
            } else {
               var9 = false;
               boolean var36 = false;
               boolean var11 = false;
               boolean var12 = false;
               boolean var13 = false;

               while(true) {
                  boolean var15;
                  switch(var10) {
                  case ' ':
                     var13 = true;
                     break;
                  case '#':
                     var9 = true;
                     break;
                  case '+':
                     var12 = true;
                     break;
                  case '-':
                     var11 = true;
                     break;
                  case '0':
                     var36 = true;
                     break;
                  default:
                     int var14;
                     for(var14 = 0; var10 >= '0' && var10 <= '9'; var10 = var3.charAt(var8)) {
                        var14 = var14 * 10 + (var10 - 48);
                        ++var8;
                        if (var8 < var4) {
                           var15 = true;
                        } else {
                           var15 = false;
                        }

                        BaseLib.luaAssert(var15, "incomplete option to 'format'");
                     }

                     int var16 = 0;
                     byte var17 = 0;
                     boolean var18 = false;
                     char var19 = var10;
                     int var20 = var8;
                     boolean var21;
                     int var40;
                     if (var10 == '.') {
                        var21 = true;
                        ++var8;
                        if (var8 < var4) {
                           var15 = true;
                        } else {
                           var15 = false;
                        }

                        BaseLib.luaAssert(var15, "incomplete option to 'format'");
                        char var22 = var3.charAt(var8);
                        var40 = var17;

                        while(true) {
                           var16 = var40;
                           var19 = var22;
                           var18 = var21;
                           var20 = var8;
                           if (var22 < '0') {
                              break;
                           }

                           var16 = var40;
                           var19 = var22;
                           var18 = var21;
                           var20 = var8;
                           if (var22 > '9') {
                              break;
                           }

                           var40 = var40 * 10 + (var22 - 48);
                           ++var8;
                           if (var8 < var4) {
                              var15 = true;
                           } else {
                              var15 = false;
                           }

                           BaseLib.luaAssert(var15, "incomplete option to 'format'");
                           var22 = var3.charAt(var8);
                        }
                     }

                     var21 = var36;
                     if (var11) {
                        var21 = false;
                     }

                     byte var23 = 10;
                     boolean var24 = false;
                     byte var25 = 6;
                     String var26 = "";
                     byte var41 = var23;
                     String var27 = var26;
                     byte var37 = var25;
                     boolean var39 = var24;
                     int var43 = var14;
                     boolean var45 = var21;
                     switch(var19) {
                     case 'E':
                        var39 = true;
                        var41 = var23;
                        var27 = var26;
                        var37 = var25;
                        var43 = var14;
                        var45 = var21;
                        break;
                     case 'G':
                        var39 = true;
                        var41 = var23;
                        var27 = var26;
                        var37 = var25;
                        var43 = var14;
                        var45 = var21;
                        break;
                     case 'X':
                        var41 = 16;
                        var37 = 1;
                        var39 = true;
                        var27 = "0X";
                        var43 = var14;
                        var45 = var21;
                        break;
                     case 'c':
                        var45 = false;
                        var43 = var14;
                        var39 = var24;
                        var37 = var25;
                        var27 = var26;
                        var41 = var23;
                        break;
                     case 'd':
                     case 'i':
                        var37 = 1;
                        var41 = var23;
                        var27 = var26;
                        var39 = var24;
                        var43 = var14;
                        var45 = var21;
                     case 'e':
                     case 'f':
                     case 'g':
                        break;
                     case 'o':
                        var41 = 8;
                        var37 = 1;
                        var27 = "0";
                        var39 = var24;
                        var43 = var14;
                        var45 = var21;
                        break;
                     case 'q':
                        var43 = 0;
                        var41 = var23;
                        var27 = var26;
                        var37 = var25;
                        var39 = var24;
                        var45 = var21;
                        break;
                     case 's':
                        var45 = false;
                        var41 = var23;
                        var27 = var26;
                        var37 = var25;
                        var39 = var24;
                        var43 = var14;
                        break;
                     case 'u':
                        var37 = 1;
                        var41 = var23;
                        var27 = var26;
                        var39 = var24;
                        var43 = var14;
                        var45 = var21;
                        break;
                     case 'x':
                        var41 = 16;
                        var37 = 1;
                        var27 = "0x";
                        var39 = var24;
                        var43 = var14;
                        var45 = var21;
                        break;
                     default:
                        throw new RuntimeException("invalid option '%" + var19 + "' to 'format'");
                     }

                     if (!var18) {
                        var16 = var37;
                     }

                     boolean var42 = var45;
                     if (var18) {
                        var42 = var45;
                        if (var41 != 10) {
                           var42 = false;
                        }
                     }

                     char var38;
                     if (var42) {
                        var37 = 48;
                        var38 = (char)var37;
                     } else {
                        var37 = 32;
                        var38 = (char)var37;
                     }

                     int var44 = var6.length();
                     if (!var11) {
                        this.extend(var6, var43, var38);
                     }

                     long var28;
                     long var30;
                     double var32;
                     double var34;
                     Double var47;
                     switch(var19) {
                     case 'E':
                     case 'e':
                     case 'f':
                        var47 = this.getDoubleArg(var1, var5);
                        if (!var47.isInfinite() && !var47.isNaN()) {
                           var36 = false;
                        } else {
                           var36 = true;
                        }

                        var32 = var47;
                        if (MathLib.isNegative(var32)) {
                           if (!var36) {
                              var6.append('-');
                           }

                           var34 = -var32;
                        } else if (var12) {
                           var6.append('+');
                           var34 = var32;
                        } else {
                           var34 = var32;
                           if (var13) {
                              var6.append(' ');
                              var34 = var32;
                           }
                        }

                        if (var36) {
                           var6.append(BaseLib.numberToString(var47));
                        } else if (var19 == 'f') {
                           this.appendPrecisionNumber(var6, var34, var16, var9);
                        } else {
                           this.appendScientificNumber(var6, var34, var16, var9, false);
                        }
                        break;
                     case 'G':
                     case 'g':
                        int var46 = var16;
                        if (var16 <= 0) {
                           var46 = 1;
                        }

                        var47 = this.getDoubleArg(var1, var5);
                        if (!var47.isInfinite() && !var47.isNaN()) {
                           var36 = false;
                        } else {
                           var36 = true;
                        }

                        var32 = var47;
                        if (MathLib.isNegative(var32)) {
                           if (!var36) {
                              var6.append('-');
                           }

                           var34 = -var32;
                        } else if (var12) {
                           var6.append('+');
                           var34 = var32;
                        } else {
                           var34 = var32;
                           if (var13) {
                              var6.append(' ');
                              var34 = var32;
                           }
                        }

                        if (var36) {
                           var6.append(BaseLib.numberToString(var47));
                        } else {
                           var32 = MathLib.roundToSignificantNumbers(var34, var46);
                           if (var32 == 0.0D || var32 >= 1.0E-4D && var32 < MathLib.ipow(10.0D, var46)) {
                              if (var32 == 0.0D) {
                                 var40 = 1;
                              } else if (Math.floor(var32) == 0.0D) {
                                 var40 = 0;
                              } else {
                                 var34 = var32;
                                 var2 = 1;

                                 while(true) {
                                    var40 = var2;
                                    if (var34 < 10.0D) {
                                       break;
                                    }

                                    var34 /= 10.0D;
                                    ++var2;
                                 }
                              }

                              this.appendSignificantNumber(var6, var32, var46 - var40, var9);
                              break;
                           }

                           this.appendScientificNumber(var6, var32, var46 - 1, var9, true);
                        }
                        break;
                     case 'X':
                     case 'o':
                     case 'u':
                     case 'x':
                        var28 = this.unsigned(this.getDoubleArg(var1, var5).longValue());
                        if (var9) {
                           if (var41 == 8) {
                              var2 = 0;

                              for(var30 = var28; var30 > 0L; ++var2) {
                                 var30 /= 8L;
                              }

                              if (var16 <= var2) {
                                 var6.append(var27);
                              }
                           } else if (var41 == 16 && var28 != 0L) {
                              var6.append(var27);
                           }
                        }

                        if (var28 != 0L || var16 > 0) {
                           stringBufferAppend(var6, (double)var28, var41, false, var16);
                        }
                        break;
                     case 'c':
                        var6.append((char)this.getDoubleArg(var1, var5).shortValue());
                        break;
                     case 'd':
                     case 'i':
                        var28 = this.getDoubleArg(var1, var5).longValue();
                        if (var28 < 0L) {
                           var6.append('-');
                           var30 = -var28;
                        } else if (var12) {
                           var6.append('+');
                           var30 = var28;
                        } else {
                           var30 = var28;
                           if (var13) {
                              var6.append(' ');
                              var30 = var28;
                           }
                        }

                        if (var30 != 0L || var16 > 0) {
                           stringBufferAppend(var6, (double)var30, var41, false, var16);
                        }
                        break;
                     case 'q':
                        var27 = this.getStringArg(var1, var5);
                        var6.append('"');

                        for(var2 = 0; var2 < var27.length(); ++var2) {
                           var7 = var27.charAt(var2);
                           switch(var7) {
                           case '\n':
                              var6.append("\\\n");
                              break;
                           case '\r':
                              var6.append("\\r");
                              break;
                           case '"':
                              var6.append("\\\"");
                              break;
                           case '\\':
                              var6.append("\\");
                              break;
                           default:
                              var6.append(var7);
                           }
                        }

                        var6.append('"');
                        break;
                     case 's':
                        var27 = this.getStringArg(var1, var5);
                        var2 = var27.length();
                        if (var18) {
                           var2 = Math.min(var16, var27.length());
                        }

                        this.append(var6, var27, 0, var2);
                        break;
                     default:
                        throw new RuntimeException("Internal error");
                     }

                     if (var11) {
                        var2 = var43 - (var6.length() - var44);
                        if (var2 > 0) {
                           this.extend(var6, var2, ' ');
                        }
                     } else {
                        var2 = Math.min(var6.length() - var44 - var43, var43);
                        if (var2 > 0) {
                           var6.delete(var44, var44 + var2);
                        }

                        if (var42) {
                           var2 = var44 + (var43 - var2);
                           var7 = var6.charAt(var2);
                           if (var7 == '+' || var7 == '-' || var7 == ' ') {
                              var6.setCharAt(var2, '0');
                              var6.setCharAt(var44, var7);
                           }
                        }
                     }

                     if (var39) {
                        this.stringBufferUpperCase(var6, var44);
                     }

                     ++var5;
                     var2 = var20;
                     continue label320;
                  }

                  ++var8;
                  if (var8 < var4) {
                     var15 = true;
                  } else {
                     var15 = false;
                  }

                  BaseLib.luaAssert(var15, "incomplete option to 'format'");
                  var10 = var3.charAt(var8);
               }
            }
         } else {
            var6.append(var7);
         }
      }

      var1.push(var6.toString());
      return 1;
   }

   private Double getDoubleArg(LuaCallFrame var1, int var2) {
      return this.getDoubleArg(var1, var2, names[6]);
   }

   private Double getDoubleArg(LuaCallFrame var1, int var2, String var3) {
      return (Double)BaseLib.getArg(var1, var2, "number", var3);
   }

   private String getStringArg(LuaCallFrame var1, int var2) {
      return this.getStringArg(var1, var2, names[6]);
   }

   private String getStringArg(LuaCallFrame var1, int var2, String var3) {
      return (String)BaseLib.getArg(var1, var2, "string", var3);
   }

   private static int gsub(LuaCallFrame var0, int var1) {
      String var2 = (String)BaseLib.getArg(var0, 1, "string", names[9]);
      String var3 = (String)BaseLib.getArg(var0, 2, "string", names[9]);
      Object var4 = BaseLib.getArg(var0, 3, (String)null, names[9]);
      String var5 = BaseLib.rawTostring(var4);
      if (var5 != null) {
         var4 = var5;
      }

      Double var14 = (Double)BaseLib.getOptArg(var0, 4, "number");
      int var6;
      if (var14 == null) {
         var6 = Integer.MAX_VALUE;
      } else {
         var6 = var14.intValue();
      }

      StringLib.StringPointer var15 = new StringLib.StringPointer(var3);
      StringLib.StringPointer var12 = new StringLib.StringPointer(var2);
      boolean var7 = false;
      if (var15.getChar() == '^') {
         var7 = true;
         var15.postIncrString(1);
      }

      var3 = BaseLib.type(var4);
      if (var3 != "function" && var3 != "string" && var3 != "table") {
         BaseLib.fail("string/function/table expected, got " + var3);
      }

      StringLib.MatchState var8 = new StringLib.MatchState();
      var8.callFrame = var0;
      var8.src_init = var12.getClone();
      var8.endIndex = var12.length();
      int var9 = 0;
      StringBuffer var13 = new StringBuffer();

      int var10;
      while(true) {
         var10 = var9;
         if (var9 >= var6) {
            break;
         }

         var8.level = 0;
         StringLib.StringPointer var11 = match(var8, var12, var15);
         var1 = var9;
         if (var11 != null) {
            var1 = var9 + 1;
            addValue(var8, var4, var13, var12, var11);
         }

         if (var11 != null && var11.getIndex() > var12.getIndex()) {
            var12.setIndex(var11.getIndex());
         } else {
            var10 = var1;
            if (var12.getIndex() >= var8.endIndex) {
               break;
            }

            var13.append(var12.postIncrString(1));
         }

         var9 = var1;
         if (var7) {
            var10 = var1;
            break;
         }
      }

      return var0.push(var13.append(var12.getString()).toString(), new Double((double)var10));
   }

   private static boolean isControl(char var0) {
      boolean var1;
      if ((var0 < 0 || var0 > 31) && var0 != 127) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isHex(char var0) {
      boolean var1;
      if ((var0 < '0' || var0 > '9') && (var0 < 'a' || var0 > 'f') && (var0 < 'A' || var0 > 'F')) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isPunct(char var0) {
      boolean var1;
      if ((var0 < '!' || var0 > '/') && (var0 < ':' || var0 > '@') && (var0 < '[' || var0 > '`') && (var0 < '{' || var0 > '~')) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isSpace(char var0) {
      boolean var1;
      if ((var0 < '\t' || var0 > '\r') && var0 != ' ') {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private int lower(LuaCallFrame var1, int var2) {
      boolean var3;
      if (var2 >= 1) {
         var3 = true;
      } else {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "not enough arguments");
      var1.push(this.getStringArg(var1, 1, names[3]).toLowerCase());
      return 1;
   }

   private static StringLib.StringPointer match(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      var1 = var1.getClone();
      var2 = var2.getClone();
      boolean var3 = true;

      StringLib.StringPointer var8;
      while(var3) {
         StringLib.StringPointer var4;
         StringLib.StringPointer var7;
         var3 = false;
         label80:
         switch(var2.getChar()) {
         case '\u0000':
            var8 = var1;
            return var8;
         case '$':
            if (var2.getChar(1) == 0) {
               if (var1.getIndex() == var0.endIndex) {
                  var8 = var1;
               } else {
                  var8 = null;
               }

               return var8;
            }
            break;
         case '%':
            switch(var2.getChar(1)) {
            case 'b':
               var4 = var2.getClone();
               var4.postIncrString(2);
               var1 = matchBalance(var0, var1, var4);
               if (var1 == null) {
                  var8 = null;
                  return var8;
               }

               var2.postIncrString(4);
               var3 = true;
               continue;
            case 'f':
               var2.postIncrString(2);
               boolean var5;
               if (var2.getChar() == '[') {
                  var5 = true;
               } else {
                  var5 = false;
               }

               BaseLib.luaAssert(var5, "missing '[' after '%%f' in pattern");
               var4 = classEnd(var0, var2);
               char var6;
               if (var1.getIndex() == var0.src_init.getIndex()) {
                  byte var9 = 0;
                  var6 = (char)var9;
               } else {
                  char var10 = var1.getChar(-1);
                  var6 = var10;
               }

               var7 = var4.getClone();
               var7.postIncrString(-1);
               if (!matchBracketClass(var6, var2, var7) && matchBracketClass(var1.getChar(), var2, var7)) {
                  var2 = var4;
                  var3 = true;
                  continue;
               }

               var8 = null;
               return var8;
            default:
               if (Character.isDigit(var2.getChar(1))) {
                  var1 = matchCapture(var0, var1, var2.getChar(1));
                  if (var1 == null) {
                     var8 = null;
                     return var8;
                  }

                  var2.postIncrString(2);
                  var3 = true;
                  continue;
               }
               break label80;
            }
         case '(':
            var4 = var2.getClone();
            if (var2.getChar(1) == ')') {
               var4.postIncrString(2);
               var8 = startCapture(var0, var1, var4, -2);
            } else {
               var4.postIncrString(1);
               var8 = startCapture(var0, var1, var4, -1);
            }

            return var8;
         case ')':
            var2 = var2.getClone();
            var2.postIncrString(1);
            var8 = endCapture(var0, var1, var2);
            return var8;
         }

         if (true) {
            var4 = classEnd(var0, var2);
            if (var1.getIndex() < var0.endIndex && singleMatch(var1.getChar(), var2, var4)) {
               var3 = true;
            } else {
               var3 = false;
            }

            switch(var4.getChar()) {
            case '*':
               var8 = maxExpand(var0, var1, var2, var4);
               return var8;
            case '+':
               var1 = var1.getClone();
               var1.postIncrString(1);
               if (var3) {
                  var8 = maxExpand(var0, var1, var2, var4);
               } else {
                  var8 = null;
               }

               return var8;
            case '-':
               var8 = minExpand(var0, var1, var2, var4);
               return var8;
            case '?':
               var2 = var1.getClone();
               var2.postIncrString(1);
               var7 = var4.getClone();
               var7.postIncrString(1);
               if (var3) {
                  var2 = match(var0, var2, var7);
                  if (var2 != null) {
                     var8 = var2;
                     return var8;
                  }
               }

               var2 = var4;
               var4.postIncrString(1);
               var3 = true;
               break;
            default:
               if (!var3) {
                  var8 = null;
                  return var8;
               }

               var1.postIncrString(1);
               var2 = var4;
               var3 = true;
            }
         }
      }

      var8 = null;
      return var8;
   }

   private static StringLib.StringPointer matchBalance(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      Object var3 = null;
      boolean var4;
      if (var2.getChar() != 0 && var2.getChar(1) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      BaseLib.luaAssert(var4, "unbalanced pattern");
      StringLib.StringPointer var5 = var1.getClone();
      if (var5.getChar() != var2.getChar()) {
         var1 = (StringLib.StringPointer)var3;
      } else {
         char var6 = var2.getChar();
         char var7 = var2.getChar(1);
         int var8 = 1;

         while(true) {
            var1 = (StringLib.StringPointer)var3;
            if (var5.preIncrStringI(1) >= var0.endIndex) {
               break;
            }

            if (var5.getChar() == var7) {
               int var9 = var8 - 1;
               var8 = var9;
               if (var9 == 0) {
                  var1 = var5.getClone();
                  var1.postIncrString(1);
                  break;
               }
            } else if (var5.getChar() == var6) {
               ++var8;
            }
         }
      }

      return var1;
   }

   private static boolean matchBracketClass(char var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      boolean var3 = true;
      var1 = var1.getClone();
      var2 = var2.getClone();
      boolean var4 = true;
      if (var1.getChar(1) == '^') {
         var4 = false;
         var1.postIncrString(1);
      }

      while(true) {
         if (var1.preIncrStringI(1) >= var2.getIndex()) {
            if (!var4) {
               var4 = var3;
            } else {
               var4 = false;
            }
            break;
         }

         if (var1.getChar() == '%') {
            var1.postIncrString(1);
            if (matchClass(var1.getChar(), var0)) {
               break;
            }
         } else if (var1.getChar(1) == '-' && var1.getIndex() + 2 < var2.getIndex()) {
            var1.postIncrString(2);
            if (var1.getChar(-2) <= var0 && var0 <= var1.getChar()) {
               break;
            }
         } else if (var1.getChar() == var0) {
            break;
         }
      }

      return var4;
   }

   private static StringLib.StringPointer matchCapture(StringLib.MatchState var0, StringLib.StringPointer var1, int var2) {
      var2 = checkCapture(var0, var2);
      int var3 = var0.capture[var2].len;
      StringLib.StringPointer var4;
      if (var0.endIndex - var1.length() >= var3 && var0.capture[var2].init.compareTo(var1, var3) == 0) {
         var4 = var1.getClone();
         var4.postIncrString(var3);
      } else {
         var4 = null;
      }

      return var4;
   }

   private static boolean matchClass(char var0, char var1) {
      boolean var2 = true;
      char var3 = Character.toLowerCase(var0);
      boolean var4;
      switch(var3) {
      case 'a':
         if (!Character.isLowerCase(var1) && !Character.isUpperCase(var1)) {
            var4 = false;
         } else {
            var4 = true;
         }
         break;
      case 'c':
         var4 = isControl(var1);
         break;
      case 'd':
         var4 = Character.isDigit(var1);
         break;
      case 'l':
         var4 = Character.isLowerCase(var1);
         break;
      case 'p':
         var4 = isPunct(var1);
         break;
      case 's':
         var4 = isSpace(var1);
         break;
      case 'u':
         var4 = Character.isUpperCase(var1);
         break;
      case 'w':
         if (!Character.isLowerCase(var1) && !Character.isUpperCase(var1) && !Character.isDigit(var1)) {
            var4 = false;
         } else {
            var4 = true;
         }
         break;
      case 'x':
         var4 = isHex(var1);
         break;
      case 'z':
         if (var1 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }
         break;
      default:
         if (var0 != var1) {
            var2 = false;
         }

         return var2;
      }

      boolean var5;
      if (var3 == var0) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5 != var4) {
         var2 = false;
      }

      return var2;
   }

   private static StringLib.StringPointer maxExpand(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2, StringLib.StringPointer var3) {
      int var4 = 0;

      int var5;
      while(true) {
         var5 = var4;
         if (var1.getIndex() + var4 >= var0.endIndex) {
            break;
         }

         var5 = var4;
         if (!singleMatch(var1.getChar(var4), var2, var3)) {
            break;
         }

         ++var4;
      }

      StringLib.StringPointer var7;
      while(true) {
         if (var5 < 0) {
            var7 = null;
            break;
         }

         StringLib.StringPointer var6 = var1.getClone();
         var6.postIncrString(var5);
         var2 = var3.getClone();
         var2.postIncrString(1);
         var2 = match(var0, var6, var2);
         if (var2 != null) {
            var7 = var2;
            break;
         }

         --var5;
      }

      return var7;
   }

   private static StringLib.StringPointer minExpand(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2, StringLib.StringPointer var3) {
      StringLib.StringPointer var4 = var3.getClone();
      StringLib.StringPointer var5 = var1.getClone();
      var4.postIncrString(1);

      StringLib.StringPointer var6;
      while(true) {
         var1 = match(var0, var5, var4);
         if (var1 != null) {
            var6 = var1;
            break;
         }

         if (var5.getIndex() >= var0.endIndex || !singleMatch(var5.getChar(), var2, var3)) {
            var6 = null;
            break;
         }

         var5.postIncrString(1);
      }

      return var6;
   }

   private static boolean noSpecialChars(String var0) {
      int var1 = 0;

      boolean var3;
      while(true) {
         if (var1 >= var0.length()) {
            var3 = true;
            break;
         }

         char var2 = var0.charAt(var1);
         if (var2 < 256 && SPECIALS[var2]) {
            var3 = false;
            break;
         }

         ++var1;
      }

      return var3;
   }

   private static int push_captures(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      boolean var3 = true;
      int var4;
      if (var0.level == 0 && var1 != null) {
         var4 = 1;
      } else {
         var4 = var0.level;
      }

      if (var4 > 32) {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "too many captures");

      for(int var5 = 0; var5 < var4; ++var5) {
         push_onecapture(var0, var5, var1, var2);
      }

      return var4;
   }

   private static Object push_onecapture(StringLib.MatchState var0, int var1, StringLib.StringPointer var2, StringLib.StringPointer var3) {
      Object var6;
      String var7;
      if (var1 >= var0.level) {
         if (var1 != 0) {
            throw new RuntimeException("invalid capture index");
         }

         var7 = var2.string.substring(var2.index, var3.index);
         var0.callFrame.push(var7);
         var6 = var7;
      } else {
         int var4 = var0.capture[var1].len;
         if (var4 == -1) {
            throw new RuntimeException("unfinished capture");
         }

         if (var4 == -2) {
            Double var8 = new Double((double)(var0.src_init.length() - var0.capture[var1].init.length() + 1));
            var0.callFrame.push(var8);
            var6 = var8;
         } else {
            int var5 = var0.capture[var1].init.index;
            var7 = var0.capture[var1].init.string.substring(var5, var5 + var4);
            var0.callFrame.push(var7);
            var6 = var7;
         }
      }

      return var6;
   }

   public static void register(LuaState var0) {
      LuaTableImpl var1 = new LuaTableImpl();
      var0.getEnvironment().rawset("string", var1);

      for(int var2 = 0; var2 < 10; ++var2) {
         var1.rawset(names[var2], functions[var2]);
      }

      var1.rawset("__index", var1);
      var0.setClassMetatable(STRING_CLASS, var1);
   }

   private int reverse(LuaCallFrame var1, int var2) {
      boolean var3;
      if (var2 >= 1) {
         var3 = true;
      } else {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "not enough arguments");
      var1.push((new StringBuffer(this.getStringArg(var1, 1, names[5]))).reverse().toString());
      return 1;
   }

   private static boolean singleMatch(char var0, StringLib.StringPointer var1, StringLib.StringPointer var2) {
      boolean var3 = true;
      boolean var4 = var3;
      switch(var1.getChar()) {
      case '%':
         var4 = matchClass(var1.getChar(1), var0);
      case '.':
         break;
      case '[':
         var2 = var2.getClone();
         var2.postIncrString(-1);
         var4 = matchBracketClass(var0, var1, var2);
         break;
      default:
         if (var1.getChar() == var0) {
            var4 = var3;
         } else {
            var4 = false;
         }
      }

      return var4;
   }

   private static StringLib.StringPointer startCapture(StringLib.MatchState var0, StringLib.StringPointer var1, StringLib.StringPointer var2, int var3) {
      int var4 = var0.level;
      boolean var5;
      if (var4 < 32) {
         var5 = true;
      } else {
         var5 = false;
      }

      BaseLib.luaAssert(var5, "too many captures");
      var0.capture[var4].init = var1.getClone();
      var0.capture[var4].init.setIndex(var1.getIndex());
      var0.capture[var4].len = var3;
      var0.level = var4 + 1;
      var1 = match(var0, var1, var2);
      if (var1 == null) {
         --var0.level;
      }

      return var1;
   }

   private static void stringBufferAppend(StringBuffer var0, double var1, int var3, boolean var4, int var5) {
      int var6;
      for(var6 = var0.length(); var1 > 0.0D || var5 > 0; --var5) {
         double var7 = Math.floor(var1 / (double)var3);
         var0.append(digits[(int)(var1 - (double)var3 * var7)]);
         var1 = var7;
      }

      var5 = var0.length() - 1;
      if (var6 > var5 && var4) {
         var0.append('0');
      } else {
         for(var3 = (var5 + 1 - var6) / 2 - 1; var3 >= 0; --var3) {
            int var9 = var6 + var3;
            int var10 = var5 - var3;
            char var11 = var0.charAt(var9);
            var0.setCharAt(var9, var0.charAt(var10));
            var0.setCharAt(var10, var11);
         }
      }

   }

   private void stringBufferUpperCase(StringBuffer var1, int var2) {
      for(int var3 = var1.length(); var2 < var3; ++var2) {
         char var4 = var1.charAt(var2);
         if (var4 >= 'a' && var4 <= 'z') {
            var1.setCharAt(var2, (char)(var4 - 32));
         }
      }

   }

   private int stringByte(LuaCallFrame var1, int var2) {
      boolean var3;
      if (var2 >= 1) {
         var3 = true;
      } else {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "not enough arguments");
      String var4 = this.getStringArg(var1, 1, names[2]);
      Double var5 = null;
      Object var6 = null;
      Double var7 = (Double)var6;
      if (var2 >= 2) {
         Double var8 = this.getDoubleArg(var1, 2, names[2]);
         var5 = var8;
         var7 = (Double)var6;
         if (var2 >= 3) {
            var7 = this.getDoubleArg(var1, 3, names[2]);
            var5 = var8;
         }
      }

      double var9 = 1.0D;
      if (var5 != null) {
         var9 = LuaState.fromDouble(var5);
      }

      double var11 = var9;
      if (var7 != null) {
         var11 = LuaState.fromDouble(var7);
      }

      int var13 = (int)var9;
      int var14 = (int)var11;
      int var15 = var4.length();
      var2 = var13;
      if (var13 < 0) {
         var2 = var13 + var15 + 1;
      }

      var13 = var2;
      if (var2 <= 0) {
         var13 = 1;
      }

      if (var14 < 0) {
         var2 = var14 + var15 + 1;
      } else {
         var2 = var14;
         if (var14 > var15) {
            var2 = var15;
         }
      }

      var15 = var2 + 1 - var13;
      if (var15 <= 0) {
         var14 = 0;
      } else {
         var1.setTop(var15);
         var2 = 0;

         while(true) {
            var14 = var15;
            if (var2 >= var15) {
               break;
            }

            var1.set(var2, new Double((double)var4.charAt(var13 - 1 + var2)));
            ++var2;
         }
      }

      return var14;
   }

   private int stringChar(LuaCallFrame var1, int var2) {
      StringBuffer var3 = new StringBuffer();

      for(int var4 = 0; var4 < var2; ++var4) {
         var3.append((char)this.getDoubleArg(var1, var4 + 1, names[1]).intValue());
      }

      return var1.push(var3.toString());
   }

   private int sub(LuaCallFrame var1, int var2) {
      String var3 = this.getStringArg(var1, 1, names[0]);
      double var4 = this.getDoubleArg(var1, 2, names[0]);
      double var6 = -1.0D;
      if (var2 >= 3) {
         var6 = this.getDoubleArg(var1, 3, names[0]);
      }

      int var8 = (int)var4;
      int var9 = (int)var6;
      int var10 = var3.length();
      if (var8 < 0) {
         var2 = Math.max(var10 + var8 + 1, 1);
      } else {
         var2 = var8;
         if (var8 == 0) {
            var2 = 1;
         }
      }

      if (var9 < 0) {
         var8 = Math.max(0, var9 + var10 + 1);
      } else {
         var8 = var9;
         if (var9 > var10) {
            var8 = var10;
         }
      }

      if (var2 > var8) {
         var2 = var1.push("");
      } else {
         var2 = var1.push(var3.substring(var2 - 1, var8));
      }

      return var2;
   }

   private long unsigned(long var1) {
      long var3 = var1;
      if (var1 < 0L) {
         var3 = var1 + 4294967296L;
      }

      return var3;
   }

   private int upper(LuaCallFrame var1, int var2) {
      boolean var3;
      if (var2 >= 1) {
         var3 = true;
      } else {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "not enough arguments");
      var1.push(this.getStringArg(var1, 1, names[4]).toUpperCase());
      return 1;
   }

   public int call(LuaCallFrame var1, int var2) {
      byte var3 = 0;
      switch(this.methodId) {
      case 0:
         var2 = this.sub(var1, var2);
         break;
      case 1:
         var2 = this.stringChar(var1, var2);
         break;
      case 2:
         var2 = this.stringByte(var1, var2);
         break;
      case 3:
         var2 = this.lower(var1, var2);
         break;
      case 4:
         var2 = this.upper(var1, var2);
         break;
      case 5:
         var2 = this.reverse(var1, var2);
         break;
      case 6:
         var2 = this.format(var1, var2);
         break;
      case 7:
         var2 = findAux(var1, true);
         break;
      case 8:
         var2 = findAux(var1, false);
         break;
      case 9:
         var2 = gsub(var1, var2);
         break;
      default:
         var2 = var3;
      }

      return var2;
   }

   public String toString() {
      return names[this.methodId];
   }

   public static class MatchState {
      public LuaCallFrame callFrame;
      public StringLib.MatchState.Capture[] capture = new StringLib.MatchState.Capture[32];
      public int endIndex;
      public int level;
      public StringLib.StringPointer src_init;

      public MatchState() {
         for(int var1 = 0; var1 < 32; ++var1) {
            this.capture[var1] = new StringLib.MatchState.Capture();
         }

      }

      public Object[] getCaptures() {
         String[] var1;
         if (this.level <= 0) {
            var1 = null;
         } else {
            String[] var2 = new String[this.level];
            int var3 = 0;

            while(true) {
               var1 = var2;
               if (var3 >= this.level) {
                  break;
               }

               if (this.capture[var3].len == -2) {
                  var2[var3] = new Double((double)(this.src_init.length() - this.capture[var3].init.length() + 1));
               } else {
                  var2[var3] = this.capture[var3].init.getString().substring(0, this.capture[var3].len);
               }

               ++var3;
            }
         }

         return var1;
      }

      public static class Capture {
         public StringLib.StringPointer init;
         public int len;
      }
   }

   public static class StringPointer {
      private int index = 0;
      private String string;

      public StringPointer(String var1) {
         this.string = var1;
      }

      public StringPointer(String var1, int var2) {
         this.string = var1;
         this.index = var2;
      }

      public int compareTo(StringLib.StringPointer var1, int var2) {
         return this.string.substring(this.index, this.index + var2).compareTo(var1.string.substring(var1.index, var1.index + var2));
      }

      public char getChar() {
         return this.getChar(0);
      }

      public char getChar(int var1) {
         char var2;
         if (this.index + var1 >= this.string.length()) {
            byte var3 = 0;
            var2 = (char)var3;
         } else {
            char var4 = this.string.charAt(this.index + var1);
            var2 = var4;
         }

         return var2;
      }

      public StringLib.StringPointer getClone() {
         return new StringLib.StringPointer(this.getOriginalString(), this.getIndex());
      }

      public int getIndex() {
         return this.index;
      }

      public String getOriginalString() {
         return this.string;
      }

      public String getString() {
         return this.getString(0);
      }

      public String getString(int var1) {
         return this.string.substring(this.index + var1, this.string.length());
      }

      public int length() {
         return this.string.length() - this.index;
      }

      public char postIncrString(int var1) {
         char var2 = this.getChar();
         this.index += var1;
         return var2;
      }

      public int postIncrStringI(int var1) {
         int var2 = this.index;
         this.index += var1;
         return var2;
      }

      public char preIncrString(int var1) {
         this.index += var1;
         return this.getChar();
      }

      public int preIncrStringI(int var1) {
         this.index += var1;
         return this.index;
      }

      public void setIndex(int var1) {
         this.index = var1;
      }

      public void setOriginalString(String var1) {
         this.string = var1;
      }
   }
}

package com.google.android.exoplayer2.text.cea;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Layout.Alignment;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Cea708Decoder extends CeaDecoder {
   private final ParsableByteArray ccData = new ParsableByteArray();
   private final Cea708Decoder.CueBuilder[] cueBuilders;
   private List cues;
   private Cea708Decoder.CueBuilder currentCueBuilder;
   private Cea708Decoder.DtvCcPacket currentDtvCcPacket;
   private int currentWindow;
   private List lastCues;
   private final int selectedServiceNumber;
   private final ParsableBitArray serviceBlockPacket = new ParsableBitArray();

   public Cea708Decoder(int var1, List var2) {
      int var3 = var1;
      if (var1 == -1) {
         var3 = 1;
      }

      this.selectedServiceNumber = var3;
      this.cueBuilders = new Cea708Decoder.CueBuilder[8];

      for(var1 = 0; var1 < 8; ++var1) {
         this.cueBuilders[var1] = new Cea708Decoder.CueBuilder();
      }

      this.currentCueBuilder = this.cueBuilders[0];
      this.resetCueBuilders();
   }

   private void finalizeCurrentPacket() {
      if (this.currentDtvCcPacket != null) {
         this.processCurrentPacket();
         this.currentDtvCcPacket = null;
      }
   }

   private List getDisplayCues() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 8; ++var2) {
         if (!this.cueBuilders[var2].isEmpty() && this.cueBuilders[var2].isVisible()) {
            var1.add(this.cueBuilders[var2].build());
         }
      }

      Collections.sort(var1);
      return Collections.unmodifiableList(var1);
   }

   private void handleC0Command(int var1) {
      if (var1 != 0) {
         if (var1 != 3) {
            if (var1 != 8) {
               switch(var1) {
               case 12:
                  this.resetCueBuilders();
                  break;
               case 13:
                  this.currentCueBuilder.append('\n');
               case 14:
                  break;
               default:
                  StringBuilder var2;
                  if (var1 >= 17 && var1 <= 23) {
                     var2 = new StringBuilder();
                     var2.append("Currently unsupported COMMAND_EXT1 Command: ");
                     var2.append(var1);
                     Log.w("Cea708Decoder", var2.toString());
                     this.serviceBlockPacket.skipBits(8);
                  } else if (var1 >= 24 && var1 <= 31) {
                     var2 = new StringBuilder();
                     var2.append("Currently unsupported COMMAND_P16 Command: ");
                     var2.append(var1);
                     Log.w("Cea708Decoder", var2.toString());
                     this.serviceBlockPacket.skipBits(16);
                  } else {
                     var2 = new StringBuilder();
                     var2.append("Invalid C0 command: ");
                     var2.append(var1);
                     Log.w("Cea708Decoder", var2.toString());
                  }
               }
            } else {
               this.currentCueBuilder.backspace();
            }
         } else {
            this.cues = this.getDisplayCues();
         }
      }

   }

   private void handleC1Command(int var1) {
      int var2 = 1;
      int var3 = 1;
      int var4 = 1;
      switch(var1) {
      case 128:
      case 129:
      case 130:
      case 131:
      case 132:
      case 133:
      case 134:
      case 135:
         var1 -= 128;
         if (this.currentWindow != var1) {
            this.currentWindow = var1;
            this.currentCueBuilder = this.cueBuilders[var1];
         }
         break;
      case 136:
         for(; var3 <= 8; ++var3) {
            if (this.serviceBlockPacket.readBit()) {
               this.cueBuilders[8 - var3].clear();
            }
         }

         return;
      case 137:
         for(var1 = 1; var1 <= 8; ++var1) {
            if (this.serviceBlockPacket.readBit()) {
               this.cueBuilders[8 - var1].setVisibility(true);
            }
         }

         return;
      case 138:
         for(; var2 <= 8; ++var2) {
            if (this.serviceBlockPacket.readBit()) {
               this.cueBuilders[8 - var2].setVisibility(false);
            }
         }

         return;
      case 139:
         for(var1 = 1; var1 <= 8; ++var1) {
            if (this.serviceBlockPacket.readBit()) {
               Cea708Decoder.CueBuilder var5 = this.cueBuilders[8 - var1];
               var5.setVisibility(var5.isVisible() ^ true);
            }
         }

         return;
      case 140:
         for(; var4 <= 8; ++var4) {
            if (this.serviceBlockPacket.readBit()) {
               this.cueBuilders[8 - var4].reset();
            }
         }

         return;
      case 141:
         this.serviceBlockPacket.skipBits(8);
      case 142:
         break;
      case 143:
         this.resetCueBuilders();
         break;
      case 144:
         if (!this.currentCueBuilder.isDefined()) {
            this.serviceBlockPacket.skipBits(16);
         } else {
            this.handleSetPenAttributes();
         }
         break;
      case 145:
         if (!this.currentCueBuilder.isDefined()) {
            this.serviceBlockPacket.skipBits(24);
         } else {
            this.handleSetPenColor();
         }
         break;
      case 146:
         if (!this.currentCueBuilder.isDefined()) {
            this.serviceBlockPacket.skipBits(16);
         } else {
            this.handleSetPenLocation();
         }
         break;
      case 147:
      case 148:
      case 149:
      case 150:
      default:
         StringBuilder var6 = new StringBuilder();
         var6.append("Invalid C1 command: ");
         var6.append(var1);
         Log.w("Cea708Decoder", var6.toString());
         break;
      case 151:
         if (!this.currentCueBuilder.isDefined()) {
            this.serviceBlockPacket.skipBits(32);
         } else {
            this.handleSetWindowAttributes();
         }
         break;
      case 152:
      case 153:
      case 154:
      case 155:
      case 156:
      case 157:
      case 158:
      case 159:
         var1 -= 152;
         this.handleDefineWindow(var1);
         if (this.currentWindow != var1) {
            this.currentWindow = var1;
            this.currentCueBuilder = this.cueBuilders[var1];
         }
      }

   }

   private void handleC2Command(int var1) {
      if (var1 > 7) {
         if (var1 <= 15) {
            this.serviceBlockPacket.skipBits(8);
         } else if (var1 <= 23) {
            this.serviceBlockPacket.skipBits(16);
         } else if (var1 <= 31) {
            this.serviceBlockPacket.skipBits(24);
         }
      }

   }

   private void handleC3Command(int var1) {
      if (var1 <= 135) {
         this.serviceBlockPacket.skipBits(32);
      } else if (var1 <= 143) {
         this.serviceBlockPacket.skipBits(40);
      } else if (var1 <= 159) {
         this.serviceBlockPacket.skipBits(2);
         var1 = this.serviceBlockPacket.readBits(6);
         this.serviceBlockPacket.skipBits(var1 * 8);
      }

   }

   private void handleDefineWindow(int var1) {
      Cea708Decoder.CueBuilder var2 = this.cueBuilders[var1];
      this.serviceBlockPacket.skipBits(2);
      boolean var3 = this.serviceBlockPacket.readBit();
      boolean var4 = this.serviceBlockPacket.readBit();
      boolean var5 = this.serviceBlockPacket.readBit();
      int var6 = this.serviceBlockPacket.readBits(3);
      boolean var7 = this.serviceBlockPacket.readBit();
      int var8 = this.serviceBlockPacket.readBits(7);
      int var9 = this.serviceBlockPacket.readBits(8);
      var1 = this.serviceBlockPacket.readBits(4);
      int var10 = this.serviceBlockPacket.readBits(4);
      this.serviceBlockPacket.skipBits(2);
      int var11 = this.serviceBlockPacket.readBits(6);
      this.serviceBlockPacket.skipBits(2);
      var2.defineWindow(var3, var4, var5, var6, var7, var8, var9, var10, var11, var1, this.serviceBlockPacket.readBits(3), this.serviceBlockPacket.readBits(3));
   }

   private void handleG0Character(int var1) {
      if (var1 == 127) {
         this.currentCueBuilder.append('♫');
      } else {
         this.currentCueBuilder.append((char)(var1 & 255));
      }

   }

   private void handleG1Character(int var1) {
      this.currentCueBuilder.append((char)(var1 & 255));
   }

   private void handleG2Character(int var1) {
      if (var1 != 32) {
         if (var1 != 33) {
            if (var1 != 37) {
               if (var1 != 42) {
                  if (var1 != 44) {
                     if (var1 != 63) {
                        if (var1 != 57) {
                           if (var1 != 58) {
                              if (var1 != 60) {
                                 if (var1 != 61) {
                                    switch(var1) {
                                    case 48:
                                       this.currentCueBuilder.append('█');
                                       break;
                                    case 49:
                                       this.currentCueBuilder.append('‘');
                                       break;
                                    case 50:
                                       this.currentCueBuilder.append('’');
                                       break;
                                    case 51:
                                       this.currentCueBuilder.append('“');
                                       break;
                                    case 52:
                                       this.currentCueBuilder.append('”');
                                       break;
                                    case 53:
                                       this.currentCueBuilder.append('•');
                                       break;
                                    default:
                                       switch(var1) {
                                       case 118:
                                          this.currentCueBuilder.append('⅛');
                                          break;
                                       case 119:
                                          this.currentCueBuilder.append('⅜');
                                          break;
                                       case 120:
                                          this.currentCueBuilder.append('⅝');
                                          break;
                                       case 121:
                                          this.currentCueBuilder.append('⅞');
                                          break;
                                       case 122:
                                          this.currentCueBuilder.append('│');
                                          break;
                                       case 123:
                                          this.currentCueBuilder.append('┐');
                                          break;
                                       case 124:
                                          this.currentCueBuilder.append('└');
                                          break;
                                       case 125:
                                          this.currentCueBuilder.append('─');
                                          break;
                                       case 126:
                                          this.currentCueBuilder.append('┘');
                                          break;
                                       case 127:
                                          this.currentCueBuilder.append('┌');
                                          break;
                                       default:
                                          StringBuilder var2 = new StringBuilder();
                                          var2.append("Invalid G2 character: ");
                                          var2.append(var1);
                                          Log.w("Cea708Decoder", var2.toString());
                                       }
                                    }
                                 } else {
                                    this.currentCueBuilder.append('℠');
                                 }
                              } else {
                                 this.currentCueBuilder.append('œ');
                              }
                           } else {
                              this.currentCueBuilder.append('š');
                           }
                        } else {
                           this.currentCueBuilder.append('™');
                        }
                     } else {
                        this.currentCueBuilder.append('Ÿ');
                     }
                  } else {
                     this.currentCueBuilder.append('Œ');
                  }
               } else {
                  this.currentCueBuilder.append('Š');
               }
            } else {
               this.currentCueBuilder.append('…');
            }
         } else {
            this.currentCueBuilder.append(' ');
         }
      } else {
         this.currentCueBuilder.append(' ');
      }

   }

   private void handleG3Character(int var1) {
      if (var1 == 160) {
         this.currentCueBuilder.append('㏄');
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid G3 character: ");
         var2.append(var1);
         Log.w("Cea708Decoder", var2.toString());
         this.currentCueBuilder.append('_');
      }

   }

   private void handleSetPenAttributes() {
      int var1 = this.serviceBlockPacket.readBits(4);
      int var2 = this.serviceBlockPacket.readBits(2);
      int var3 = this.serviceBlockPacket.readBits(2);
      boolean var4 = this.serviceBlockPacket.readBit();
      boolean var5 = this.serviceBlockPacket.readBit();
      int var6 = this.serviceBlockPacket.readBits(3);
      int var7 = this.serviceBlockPacket.readBits(3);
      this.currentCueBuilder.setPenAttributes(var1, var2, var3, var4, var5, var6, var7);
   }

   private void handleSetPenColor() {
      int var1 = this.serviceBlockPacket.readBits(2);
      var1 = Cea708Decoder.CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), var1);
      int var2 = this.serviceBlockPacket.readBits(2);
      var2 = Cea708Decoder.CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), var2);
      this.serviceBlockPacket.skipBits(2);
      int var3 = Cea708Decoder.CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2));
      this.currentCueBuilder.setPenColor(var1, var2, var3);
   }

   private void handleSetPenLocation() {
      this.serviceBlockPacket.skipBits(4);
      int var1 = this.serviceBlockPacket.readBits(4);
      this.serviceBlockPacket.skipBits(2);
      int var2 = this.serviceBlockPacket.readBits(6);
      this.currentCueBuilder.setPenLocation(var1, var2);
   }

   private void handleSetWindowAttributes() {
      int var1 = this.serviceBlockPacket.readBits(2);
      int var2 = Cea708Decoder.CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), var1);
      int var3 = this.serviceBlockPacket.readBits(2);
      int var4 = Cea708Decoder.CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2));
      var1 = var3;
      if (this.serviceBlockPacket.readBit()) {
         var1 = var3 | 4;
      }

      boolean var5 = this.serviceBlockPacket.readBit();
      int var6 = this.serviceBlockPacket.readBits(2);
      int var7 = this.serviceBlockPacket.readBits(2);
      var3 = this.serviceBlockPacket.readBits(2);
      this.serviceBlockPacket.skipBits(8);
      this.currentCueBuilder.setWindowAttributes(var2, var4, var5, var1, var6, var7, var3);
   }

   private void processCurrentPacket() {
      Cea708Decoder.DtvCcPacket var1 = this.currentDtvCcPacket;
      int var2 = var1.currentIndex;
      StringBuilder var5;
      if (var2 != var1.packetSize * 2 - 1) {
         var5 = new StringBuilder();
         var5.append("DtvCcPacket ended prematurely; size is ");
         var5.append(this.currentDtvCcPacket.packetSize * 2 - 1);
         var5.append(", but current index is ");
         var5.append(this.currentDtvCcPacket.currentIndex);
         var5.append(" (sequence number ");
         var5.append(this.currentDtvCcPacket.sequenceNumber);
         var5.append("); ignoring packet");
         Log.w("Cea708Decoder", var5.toString());
      } else {
         this.serviceBlockPacket.reset(var1.packetData, var2);
         int var3 = this.serviceBlockPacket.readBits(3);
         int var4 = this.serviceBlockPacket.readBits(5);
         var2 = var3;
         if (var3 == 7) {
            this.serviceBlockPacket.skipBits(2);
            var3 = this.serviceBlockPacket.readBits(6);
            var2 = var3;
            if (var3 < 7) {
               var5 = new StringBuilder();
               var5.append("Invalid extended service number: ");
               var5.append(var3);
               Log.w("Cea708Decoder", var5.toString());
               var2 = var3;
            }
         }

         if (var4 == 0) {
            if (var2 != 0) {
               var5 = new StringBuilder();
               var5.append("serviceNumber is non-zero (");
               var5.append(var2);
               var5.append(") when blockSize is 0");
               Log.w("Cea708Decoder", var5.toString());
            }

         } else if (var2 == this.selectedServiceNumber) {
            boolean var6 = false;

            while(true) {
               while(this.serviceBlockPacket.bitsLeft() > 0) {
                  var3 = this.serviceBlockPacket.readBits(8);
                  if (var3 != 16) {
                     if (var3 <= 31) {
                        this.handleC0Command(var3);
                        continue;
                     }

                     if (var3 <= 127) {
                        this.handleG0Character(var3);
                     } else if (var3 <= 159) {
                        this.handleC1Command(var3);
                     } else {
                        if (var3 > 255) {
                           var5 = new StringBuilder();
                           var5.append("Invalid base command: ");
                           var5.append(var3);
                           Log.w("Cea708Decoder", var5.toString());
                           continue;
                        }

                        this.handleG1Character(var3);
                     }
                  } else {
                     var3 = this.serviceBlockPacket.readBits(8);
                     if (var3 <= 31) {
                        this.handleC2Command(var3);
                        continue;
                     }

                     if (var3 <= 127) {
                        this.handleG2Character(var3);
                     } else {
                        if (var3 <= 159) {
                           this.handleC3Command(var3);
                           continue;
                        }

                        if (var3 > 255) {
                           var5 = new StringBuilder();
                           var5.append("Invalid extended command: ");
                           var5.append(var3);
                           Log.w("Cea708Decoder", var5.toString());
                           continue;
                        }

                        this.handleG3Character(var3);
                     }
                  }

                  var6 = true;
               }

               if (var6) {
                  this.cues = this.getDisplayCues();
               }

               return;
            }
         }
      }
   }

   private void resetCueBuilders() {
      for(int var1 = 0; var1 < 8; ++var1) {
         this.cueBuilders[var1].reset();
      }

   }

   protected Subtitle createSubtitle() {
      List var1 = this.cues;
      this.lastCues = var1;
      return new CeaSubtitle(var1);
   }

   protected void decode(SubtitleInputBuffer var1) {
      byte[] var2 = var1.data.array();
      this.ccData.reset(var2, var1.data.limit());

      while(true) {
         while(true) {
            int var3;
            int var4;
            boolean var5;
            byte var6;
            byte var7;
            boolean var11;
            do {
               do {
                  if (this.ccData.bytesLeft() < 3) {
                     return;
                  }

                  var3 = this.ccData.readUnsignedByte() & 7;
                  var4 = var3 & 3;
                  var5 = false;
                  if ((var3 & 4) == 4) {
                     var11 = true;
                  } else {
                     var11 = false;
                  }

                  var6 = (byte)this.ccData.readUnsignedByte();
                  var7 = (byte)this.ccData.readUnsignedByte();
               } while(var4 != 2 && var4 != 3);
            } while(!var11);

            Cea708Decoder.DtvCcPacket var8;
            if (var4 == 3) {
               this.finalizeCurrentPacket();
               var4 = var6 & 63;
               var3 = var4;
               if (var4 == 0) {
                  var3 = 64;
               }

               this.currentDtvCcPacket = new Cea708Decoder.DtvCcPacket((var6 & 192) >> 6, var3);
               var8 = this.currentDtvCcPacket;
               var2 = var8.packetData;
               var3 = var8.currentIndex++;
               var2[var3] = (byte)var7;
            } else {
               if (var4 == 2) {
                  var5 = true;
               }

               Assertions.checkArgument(var5);
               Cea708Decoder.DtvCcPacket var10 = this.currentDtvCcPacket;
               if (var10 == null) {
                  Log.e("Cea708Decoder", "Encountered DTVCC_PACKET_DATA before DTVCC_PACKET_START");
                  continue;
               }

               byte[] var9 = var10.packetData;
               var3 = var10.currentIndex++;
               var9[var3] = (byte)var6;
               var3 = var10.currentIndex++;
               var9[var3] = (byte)var7;
            }

            var8 = this.currentDtvCcPacket;
            if (var8.currentIndex == var8.packetSize * 2 - 1) {
               this.finalizeCurrentPacket();
            }
         }
      }
   }

   public void flush() {
      super.flush();
      this.cues = null;
      this.lastCues = null;
      this.currentWindow = 0;
      this.currentCueBuilder = this.cueBuilders[this.currentWindow];
      this.resetCueBuilders();
      this.currentDtvCcPacket = null;
   }

   protected boolean isNewSubtitleDataAvailable() {
      boolean var1;
      if (this.cues != this.lastCues) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static final class CueBuilder {
      public static final int COLOR_SOLID_BLACK = getArgbColorFromCeaColor(0, 0, 0, 0);
      public static final int COLOR_SOLID_WHITE = getArgbColorFromCeaColor(2, 2, 2, 0);
      public static final int COLOR_TRANSPARENT = getArgbColorFromCeaColor(0, 0, 0, 3);
      private static final int[] PEN_STYLE_BACKGROUND;
      private static final int[] PEN_STYLE_EDGE_TYPE;
      private static final int[] PEN_STYLE_FONT_STYLE;
      private static final int[] WINDOW_STYLE_FILL;
      private static final int[] WINDOW_STYLE_JUSTIFICATION = new int[]{0, 0, 0, 0, 0, 2, 0};
      private static final int[] WINDOW_STYLE_PRINT_DIRECTION = new int[]{0, 0, 0, 0, 0, 0, 2};
      private static final int[] WINDOW_STYLE_SCROLL_DIRECTION = new int[]{3, 3, 3, 3, 3, 3, 1};
      private static final boolean[] WINDOW_STYLE_WORD_WRAP = new boolean[]{(boolean)0, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)1, (boolean)0};
      private int anchorId;
      private int backgroundColor;
      private int backgroundColorStartPosition;
      private final SpannableStringBuilder captionStringBuilder = new SpannableStringBuilder();
      private boolean defined;
      private int foregroundColor;
      private int foregroundColorStartPosition;
      private int horizontalAnchor;
      private int italicsStartPosition;
      private int justification;
      private int penStyleId;
      private int priority;
      private boolean relativePositioning;
      private final List rolledUpCaptions = new ArrayList();
      private int row;
      private int rowCount;
      private boolean rowLock;
      private int underlineStartPosition;
      private int verticalAnchor;
      private boolean visible;
      private int windowFillColor;
      private int windowStyleId;

      static {
         int var0 = COLOR_SOLID_BLACK;
         int var1 = COLOR_TRANSPARENT;
         WINDOW_STYLE_FILL = new int[]{var0, var1, var0, var0, var1, var0, var0};
         PEN_STYLE_FONT_STYLE = new int[]{0, 1, 2, 3, 4, 3, 4};
         PEN_STYLE_EDGE_TYPE = new int[]{0, 0, 0, 0, 0, 3, 3};
         PEN_STYLE_BACKGROUND = new int[]{var0, var0, var0, var0, var0, var1, var1};
      }

      public CueBuilder() {
         this.reset();
      }

      public static int getArgbColorFromCeaColor(int var0, int var1, int var2) {
         return getArgbColorFromCeaColor(var0, var1, var2, 0);
      }

      public static int getArgbColorFromCeaColor(int var0, int var1, int var2, int var3) {
         short var4;
         short var7;
         label32: {
            var4 = 0;
            Assertions.checkIndex(var0, 0, 4);
            Assertions.checkIndex(var1, 0, 4);
            Assertions.checkIndex(var2, 0, 4);
            Assertions.checkIndex(var3, 0, 4);
            if (var3 != 0 && var3 != 1) {
               if (var3 == 2) {
                  var7 = 127;
                  break label32;
               }

               if (var3 == 3) {
                  var7 = 0;
                  break label32;
               }
            }

            var7 = 255;
         }

         short var5;
         if (var0 > 1) {
            var5 = 255;
         } else {
            var5 = 0;
         }

         short var6;
         if (var1 > 1) {
            var6 = 255;
         } else {
            var6 = 0;
         }

         if (var2 > 1) {
            var4 = 255;
         }

         return Color.argb(var7, var5, var6, var4);
      }

      public void append(char var1) {
         if (var1 == '\n') {
            this.rolledUpCaptions.add(this.buildSpannableString());
            this.captionStringBuilder.clear();
            if (this.italicsStartPosition != -1) {
               this.italicsStartPosition = 0;
            }

            if (this.underlineStartPosition != -1) {
               this.underlineStartPosition = 0;
            }

            if (this.foregroundColorStartPosition != -1) {
               this.foregroundColorStartPosition = 0;
            }

            if (this.backgroundColorStartPosition != -1) {
               this.backgroundColorStartPosition = 0;
            }

            while(this.rowLock && this.rolledUpCaptions.size() >= this.rowCount || this.rolledUpCaptions.size() >= 15) {
               this.rolledUpCaptions.remove(0);
            }
         } else {
            this.captionStringBuilder.append(var1);
         }

      }

      public void backspace() {
         int var1 = this.captionStringBuilder.length();
         if (var1 > 0) {
            this.captionStringBuilder.delete(var1 - 1, var1);
         }

      }

      public Cea708Cue build() {
         if (this.isEmpty()) {
            return null;
         } else {
            SpannableStringBuilder var1 = new SpannableStringBuilder();
            boolean var2 = false;

            int var3;
            for(var3 = 0; var3 < this.rolledUpCaptions.size(); ++var3) {
               var1.append((CharSequence)this.rolledUpCaptions.get(var3));
               var1.append('\n');
            }

            Alignment var4;
            label53: {
               var1.append(this.buildSpannableString());
               var3 = this.justification;
               if (var3 != 0) {
                  if (var3 == 1) {
                     var4 = Alignment.ALIGN_OPPOSITE;
                     break label53;
                  }

                  if (var3 == 2) {
                     var4 = Alignment.ALIGN_CENTER;
                     break label53;
                  }

                  if (var3 != 3) {
                     StringBuilder var8 = new StringBuilder();
                     var8.append("Unexpected justification value: ");
                     var8.append(this.justification);
                     throw new IllegalArgumentException(var8.toString());
                  }
               }

               var4 = Alignment.ALIGN_NORMAL;
            }

            float var5;
            float var6;
            if (this.relativePositioning) {
               var5 = (float)this.horizontalAnchor / 99.0F;
               var6 = (float)this.verticalAnchor / 99.0F;
            } else {
               var5 = (float)this.horizontalAnchor / 209.0F;
               var6 = (float)this.verticalAnchor / 74.0F;
            }

            var3 = this.anchorId;
            byte var9;
            if (var3 % 3 == 0) {
               var9 = 0;
            } else if (var3 % 3 == 1) {
               var9 = 1;
            } else {
               var9 = 2;
            }

            int var7 = this.anchorId;
            byte var10;
            if (var7 / 3 == 0) {
               var10 = 0;
            } else if (var7 / 3 == 1) {
               var10 = 1;
            } else {
               var10 = 2;
            }

            if (this.windowFillColor != COLOR_SOLID_BLACK) {
               var2 = true;
            }

            return new Cea708Cue(var1, var4, var6 * 0.9F + 0.05F, 0, var9, var5 * 0.9F + 0.05F, var10, Float.MIN_VALUE, var2, this.windowFillColor, this.priority);
         }
      }

      public SpannableString buildSpannableString() {
         SpannableStringBuilder var1 = new SpannableStringBuilder(this.captionStringBuilder);
         int var2 = var1.length();
         if (var2 > 0) {
            if (this.italicsStartPosition != -1) {
               var1.setSpan(new StyleSpan(2), this.italicsStartPosition, var2, 33);
            }

            if (this.underlineStartPosition != -1) {
               var1.setSpan(new UnderlineSpan(), this.underlineStartPosition, var2, 33);
            }

            if (this.foregroundColorStartPosition != -1) {
               var1.setSpan(new ForegroundColorSpan(this.foregroundColor), this.foregroundColorStartPosition, var2, 33);
            }

            if (this.backgroundColorStartPosition != -1) {
               var1.setSpan(new BackgroundColorSpan(this.backgroundColor), this.backgroundColorStartPosition, var2, 33);
            }
         }

         return new SpannableString(var1);
      }

      public void clear() {
         this.rolledUpCaptions.clear();
         this.captionStringBuilder.clear();
         this.italicsStartPosition = -1;
         this.underlineStartPosition = -1;
         this.foregroundColorStartPosition = -1;
         this.backgroundColorStartPosition = -1;
         this.row = 0;
      }

      public void defineWindow(boolean var1, boolean var2, boolean var3, int var4, boolean var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
         this.defined = true;
         this.visible = var1;
         this.rowLock = var2;
         this.priority = var4;
         this.relativePositioning = var5;
         this.verticalAnchor = var6;
         this.horizontalAnchor = var7;
         this.anchorId = var10;
         var4 = this.rowCount;
         var6 = var8 + 1;
         if (var4 != var6) {
            this.rowCount = var6;

            while(var2 && this.rolledUpCaptions.size() >= this.rowCount || this.rolledUpCaptions.size() >= 15) {
               this.rolledUpCaptions.remove(0);
            }
         }

         if (var11 != 0 && this.windowStyleId != var11) {
            this.windowStyleId = var11;
            var4 = var11 - 1;
            this.setWindowAttributes(WINDOW_STYLE_FILL[var4], COLOR_TRANSPARENT, WINDOW_STYLE_WORD_WRAP[var4], 0, WINDOW_STYLE_PRINT_DIRECTION[var4], WINDOW_STYLE_SCROLL_DIRECTION[var4], WINDOW_STYLE_JUSTIFICATION[var4]);
         }

         if (var12 != 0 && this.penStyleId != var12) {
            this.penStyleId = var12;
            var4 = var12 - 1;
            this.setPenAttributes(0, 1, 1, false, false, PEN_STYLE_EDGE_TYPE[var4], PEN_STYLE_FONT_STYLE[var4]);
            this.setPenColor(COLOR_SOLID_WHITE, PEN_STYLE_BACKGROUND[var4], COLOR_SOLID_BLACK);
         }

      }

      public boolean isDefined() {
         return this.defined;
      }

      public boolean isEmpty() {
         boolean var1;
         if (!this.isDefined() || this.rolledUpCaptions.isEmpty() && this.captionStringBuilder.length() == 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean isVisible() {
         return this.visible;
      }

      public void reset() {
         this.clear();
         this.defined = false;
         this.visible = false;
         this.priority = 4;
         this.relativePositioning = false;
         this.verticalAnchor = 0;
         this.horizontalAnchor = 0;
         this.anchorId = 0;
         this.rowCount = 15;
         this.rowLock = true;
         this.justification = 0;
         this.windowStyleId = 0;
         this.penStyleId = 0;
         int var1 = COLOR_SOLID_BLACK;
         this.windowFillColor = var1;
         this.foregroundColor = COLOR_SOLID_WHITE;
         this.backgroundColor = var1;
      }

      public void setPenAttributes(int var1, int var2, int var3, boolean var4, boolean var5, int var6, int var7) {
         if (this.italicsStartPosition != -1) {
            if (!var4) {
               this.captionStringBuilder.setSpan(new StyleSpan(2), this.italicsStartPosition, this.captionStringBuilder.length(), 33);
               this.italicsStartPosition = -1;
            }
         } else if (var4) {
            this.italicsStartPosition = this.captionStringBuilder.length();
         }

         if (this.underlineStartPosition != -1) {
            if (!var5) {
               this.captionStringBuilder.setSpan(new UnderlineSpan(), this.underlineStartPosition, this.captionStringBuilder.length(), 33);
               this.underlineStartPosition = -1;
            }
         } else if (var5) {
            this.underlineStartPosition = this.captionStringBuilder.length();
         }

      }

      public void setPenColor(int var1, int var2, int var3) {
         if (this.foregroundColorStartPosition != -1) {
            var3 = this.foregroundColor;
            if (var3 != var1) {
               this.captionStringBuilder.setSpan(new ForegroundColorSpan(var3), this.foregroundColorStartPosition, this.captionStringBuilder.length(), 33);
            }
         }

         if (var1 != COLOR_SOLID_WHITE) {
            this.foregroundColorStartPosition = this.captionStringBuilder.length();
            this.foregroundColor = var1;
         }

         if (this.backgroundColorStartPosition != -1) {
            var1 = this.backgroundColor;
            if (var1 != var2) {
               this.captionStringBuilder.setSpan(new BackgroundColorSpan(var1), this.backgroundColorStartPosition, this.captionStringBuilder.length(), 33);
            }
         }

         if (var2 != COLOR_SOLID_BLACK) {
            this.backgroundColorStartPosition = this.captionStringBuilder.length();
            this.backgroundColor = var2;
         }

      }

      public void setPenLocation(int var1, int var2) {
         if (this.row != var1) {
            this.append('\n');
         }

         this.row = var1;
      }

      public void setVisibility(boolean var1) {
         this.visible = var1;
      }

      public void setWindowAttributes(int var1, int var2, boolean var3, int var4, int var5, int var6, int var7) {
         this.windowFillColor = var1;
         this.justification = var7;
      }
   }

   private static final class DtvCcPacket {
      int currentIndex;
      public final byte[] packetData;
      public final int packetSize;
      public final int sequenceNumber;

      public DtvCcPacket(int var1, int var2) {
         this.sequenceNumber = var1;
         this.packetSize = var2;
         this.packetData = new byte[var2 * 2 - 1];
         this.currentIndex = 0;
      }
   }
}

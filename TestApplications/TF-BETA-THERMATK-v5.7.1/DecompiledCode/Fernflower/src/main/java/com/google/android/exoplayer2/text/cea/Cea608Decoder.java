package com.google.android.exoplayer2.text.cea;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Layout.Alignment;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Cea608Decoder extends CeaDecoder {
   private static final int[] BASIC_CHARACTER_SET = new int[]{32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 225, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 233, 93, 237, 243, 250, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 231, 247, 209, 241, 9632};
   private static final int[] COLUMN_INDICES = new int[]{0, 4, 8, 12, 16, 20, 24, 28};
   private static final boolean[] ODD_PARITY_BYTE_TABLE = new boolean[]{(boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)1, (boolean)0, (boolean)0, (boolean)1, (boolean)0, (boolean)1, (boolean)1, (boolean)0};
   private static final int[] ROW_INDICES = new int[]{11, 1, 3, 12, 14, 5, 7, 9};
   private static final int[] SPECIAL_CHARACTER_SET = new int[]{174, 176, 189, 191, 8482, 162, 163, 9834, 224, 32, 232, 226, 234, 238, 244, 251};
   private static final int[] SPECIAL_ES_FR_CHARACTER_SET = new int[]{193, 201, 211, 218, 220, 252, 8216, 161, 42, 39, 8212, 169, 8480, 8226, 8220, 8221, 192, 194, 199, 200, 202, 203, 235, 206, 207, 239, 212, 217, 249, 219, 171, 187};
   private static final int[] SPECIAL_PT_DE_CHARACTER_SET = new int[]{195, 227, 205, 204, 236, 210, 242, 213, 245, 123, 125, 92, 94, 95, 124, 126, 196, 228, 214, 246, 223, 165, 164, 9474, 197, 229, 216, 248, 9484, 9488, 9492, 9496};
   private static final int[] STYLE_COLORS = new int[]{-1, -16711936, -16776961, -16711681, -65536, -256, -65281};
   private int captionMode;
   private int captionRowCount;
   private boolean captionValid;
   private final ParsableByteArray ccData = new ParsableByteArray();
   private final ArrayList cueBuilders = new ArrayList();
   private List cues;
   private Cea608Decoder.CueBuilder currentCueBuilder = new Cea608Decoder.CueBuilder(0, 4);
   private List lastCues;
   private final int packetLength;
   private byte repeatableControlCc1;
   private byte repeatableControlCc2;
   private boolean repeatableControlSet;
   private final int selectedField;

   public Cea608Decoder(String var1, int var2) {
      byte var3;
      if ("application/x-mp4-cea-608".equals(var1)) {
         var3 = 2;
      } else {
         var3 = 3;
      }

      this.packetLength = var3;
      if (var2 != 3 && var2 != 4) {
         this.selectedField = 1;
      } else {
         this.selectedField = 2;
      }

      this.setCaptionMode(0);
      this.resetCueBuilders();
   }

   private static char getChar(byte var0) {
      return (char)BASIC_CHARACTER_SET[(var0 & 127) - 32];
   }

   private List getDisplayCues() {
      int var1 = this.cueBuilders.size();
      ArrayList var2 = new ArrayList(var1);
      byte var3 = 0;
      int var4 = 0;

      int var5;
      Cue var6;
      int var7;
      for(var5 = 2; var4 < var1; var5 = var7) {
         var6 = ((Cea608Decoder.CueBuilder)this.cueBuilders.get(var4)).build(Integer.MIN_VALUE);
         var2.add(var6);
         var7 = var5;
         if (var6 != null) {
            var7 = Math.min(var5, var6.positionAnchor);
         }

         ++var4;
      }

      ArrayList var8 = new ArrayList(var1);

      for(var7 = var3; var7 < var1; ++var7) {
         Cue var9 = (Cue)var2.get(var7);
         if (var9 != null) {
            var6 = var9;
            if (var9.positionAnchor != var5) {
               var6 = ((Cea608Decoder.CueBuilder)this.cueBuilders.get(var7)).build(var5);
            }

            var8.add(var6);
         }
      }

      return var8;
   }

   private static char getExtendedEsFrChar(byte var0) {
      return (char)SPECIAL_ES_FR_CHARACTER_SET[var0 & 31];
   }

   private static char getExtendedPtDeChar(byte var0) {
      return (char)SPECIAL_PT_DE_CHARACTER_SET[var0 & 31];
   }

   private static char getSpecialChar(byte var0) {
      return (char)SPECIAL_CHARACTER_SET[var0 & 15];
   }

   private void handleCtrl(byte var1, byte var2, boolean var3) {
      if (isRepeatable(var1)) {
         if (var3 && this.repeatableControlCc1 == var1 && this.repeatableControlCc2 == var2) {
            return;
         }

         this.repeatableControlSet = true;
         this.repeatableControlCc1 = var1;
         this.repeatableControlCc2 = var2;
      }

      if (isMidrowCtrlCode(var1, var2)) {
         this.handleMidrowCtrl(var2);
      } else if (isPreambleAddressCode(var1, var2)) {
         this.handlePreambleAddressCode(var1, var2);
      } else if (isTabCtrlCode(var1, var2)) {
         this.currentCueBuilder.tabOffset = var2 - 32;
      } else if (isMiscCode(var1, var2)) {
         this.handleMiscCode(var2);
      }

   }

   private void handleMidrowCtrl(byte var1) {
      this.currentCueBuilder.append(' ');
      boolean var2;
      if ((var1 & 1) == 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.currentCueBuilder.setStyle(var1 >> 1 & 7, var2);
   }

   private void handleMiscCode(byte var1) {
      if (var1 != 32) {
         if (var1 == 41) {
            this.setCaptionMode(3);
         } else {
            switch(var1) {
            case 37:
               this.setCaptionMode(1);
               this.setCaptionRowCount(2);
               return;
            case 38:
               this.setCaptionMode(1);
               this.setCaptionRowCount(3);
               return;
            case 39:
               this.setCaptionMode(1);
               this.setCaptionRowCount(4);
               return;
            default:
               int var2 = this.captionMode;
               if (var2 != 0) {
                  if (var1 != 33) {
                     if (var1 != 36) {
                        switch(var1) {
                        case 44:
                           this.cues = Collections.emptyList();
                           int var3 = this.captionMode;
                           if (var3 == 1 || var3 == 3) {
                              this.resetCueBuilders();
                           }
                           break;
                        case 45:
                           if (var2 == 1 && !this.currentCueBuilder.isEmpty()) {
                              this.currentCueBuilder.rollUp();
                           }
                           break;
                        case 46:
                           this.resetCueBuilders();
                           break;
                        case 47:
                           this.cues = this.getDisplayCues();
                           this.resetCueBuilders();
                        }
                     }
                  } else {
                     this.currentCueBuilder.backspace();
                  }

               }
            }
         }
      } else {
         this.setCaptionMode(2);
      }
   }

   private void handlePreambleAddressCode(byte var1, byte var2) {
      int var3 = ROW_INDICES[var1 & 7];
      boolean var4 = false;
      boolean var5;
      if ((var2 & 32) != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      int var7 = var3;
      if (var5) {
         var7 = var3 + 1;
      }

      if (var7 != this.currentCueBuilder.row) {
         if (this.captionMode != 1 && !this.currentCueBuilder.isEmpty()) {
            this.currentCueBuilder = new Cea608Decoder.CueBuilder(this.captionMode, this.captionRowCount);
            this.cueBuilders.add(this.currentCueBuilder);
         }

         this.currentCueBuilder.row = var7;
      }

      boolean var8;
      if ((var2 & 16) == 16) {
         var8 = true;
      } else {
         var8 = false;
      }

      if ((var2 & 1) == 1) {
         var4 = true;
      }

      int var10 = var2 >> 1 & 7;
      Cea608Decoder.CueBuilder var6 = this.currentCueBuilder;
      int var9;
      if (var8) {
         var9 = 8;
      } else {
         var9 = var10;
      }

      var6.setStyle(var9, var4);
      if (var8) {
         this.currentCueBuilder.indent = COLUMN_INDICES[var10];
      }

   }

   private static boolean isMidrowCtrlCode(byte var0, byte var1) {
      boolean var2;
      if ((var0 & 247) == 17 && (var1 & 240) == 32) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static boolean isMiscCode(byte var0, byte var1) {
      boolean var2;
      if ((var0 & 247) == 20 && (var1 & 240) == 32) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static boolean isPreambleAddressCode(byte var0, byte var1) {
      boolean var2;
      if ((var0 & 240) == 16 && (var1 & 192) == 64) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static boolean isRepeatable(byte var0) {
      boolean var1;
      if ((var0 & 240) == 16) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static boolean isTabCtrlCode(byte var0, byte var1) {
      boolean var2;
      if ((var0 & 247) == 23 && var1 >= 33 && var1 <= 35) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void resetCueBuilders() {
      this.currentCueBuilder.reset(this.captionMode);
      this.cueBuilders.clear();
      this.cueBuilders.add(this.currentCueBuilder);
   }

   private void setCaptionMode(int var1) {
      int var2 = this.captionMode;
      if (var2 != var1) {
         this.captionMode = var1;
         if (var1 == 3) {
            for(var2 = 0; var2 < this.cueBuilders.size(); ++var2) {
               ((Cea608Decoder.CueBuilder)this.cueBuilders.get(var2)).setCaptionMode(var1);
            }

         } else {
            this.resetCueBuilders();
            if (var2 == 3 || var1 == 1 || var1 == 0) {
               this.cues = Collections.emptyList();
            }

         }
      }
   }

   private void setCaptionRowCount(int var1) {
      this.captionRowCount = var1;
      this.currentCueBuilder.setCaptionRowCount(var1);
   }

   protected Subtitle createSubtitle() {
      List var1 = this.cues;
      this.lastCues = var1;
      return new CeaSubtitle(var1);
   }

   protected void decode(SubtitleInputBuffer var1) {
      this.ccData.reset(var1.data.array(), var1.data.limit());
      boolean var2 = false;

      while(true) {
         int var3 = this.ccData.bytesLeft();
         int var4 = this.packetLength;
         if (var3 < var4) {
            if (var2) {
               int var12 = this.captionMode;
               if (var12 == 1 || var12 == 3) {
                  this.cues = this.getDisplayCues();
               }
            }

            return;
         }

         byte var13;
         if (var4 == 2) {
            var13 = -4;
         } else {
            var13 = (byte)this.ccData.readUnsignedByte();
         }

         int var5 = this.ccData.readUnsignedByte();
         var4 = this.ccData.readUnsignedByte();
         if ((var13 & 2) == 0 && (this.selectedField != 1 || (var13 & 1) == 0) && (this.selectedField != 2 || (var13 & 1) == 1)) {
            byte var6 = (byte)(var5 & 127);
            byte var7 = (byte)(var4 & 127);
            if (var6 != 0 || var7 != 0) {
               boolean var8 = this.repeatableControlSet;
               this.repeatableControlSet = false;
               boolean var9 = this.captionValid;
               boolean var10;
               if ((var13 & 4) == 4) {
                  var10 = true;
               } else {
                  var10 = false;
               }

               this.captionValid = var10;
               if (!this.captionValid) {
                  if (!var9) {
                     continue;
                  }

                  this.resetCueBuilders();
               } else {
                  boolean[] var11 = ODD_PARITY_BYTE_TABLE;
                  if (var11[var5] && var11[var4]) {
                     if ((var6 & 247) == 17 && (var7 & 240) == 48) {
                        this.currentCueBuilder.append(getSpecialChar(var7));
                     } else if ((var6 & 246) == 18 && (var7 & 224) == 32) {
                        this.currentCueBuilder.backspace();
                        if ((var6 & 1) == 0) {
                           this.currentCueBuilder.append(getExtendedEsFrChar(var7));
                        } else {
                           this.currentCueBuilder.append(getExtendedPtDeChar(var7));
                        }
                     } else if ((var6 & 224) == 0) {
                        this.handleCtrl(var6, var7, var8);
                     } else {
                        this.currentCueBuilder.append(getChar(var6));
                        if ((var7 & 224) != 0) {
                           this.currentCueBuilder.append(getChar(var7));
                        }
                     }
                  } else {
                     this.resetCueBuilders();
                  }
               }

               var2 = true;
            }
         }
      }
   }

   public void flush() {
      super.flush();
      this.cues = null;
      this.lastCues = null;
      this.setCaptionMode(0);
      this.setCaptionRowCount(4);
      this.resetCueBuilders();
      this.captionValid = false;
      this.repeatableControlSet = false;
      this.repeatableControlCc1 = (byte)0;
      this.repeatableControlCc2 = (byte)0;
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

   public void release() {
   }

   private static class CueBuilder {
      private int captionMode;
      private int captionRowCount;
      private final StringBuilder captionStringBuilder = new StringBuilder();
      private final List cueStyles = new ArrayList();
      private int indent;
      private final List rolledUpCaptions = new ArrayList();
      private int row;
      private int tabOffset;

      public CueBuilder(int var1, int var2) {
         this.reset(var1);
         this.setCaptionRowCount(var2);
      }

      private SpannableString buildCurrentLine() {
         SpannableStringBuilder var1 = new SpannableStringBuilder(this.captionStringBuilder);
         int var2 = var1.length();
         int var3 = 0;
         int var4 = -1;
         int var5 = -1;
         int var6 = 0;
         int var7 = -1;
         int var8 = -1;
         boolean var9 = false;

         while(true) {
            while(var3 < this.cueStyles.size()) {
               Cea608Decoder.CueBuilder.CueStyle var10 = (Cea608Decoder.CueBuilder.CueStyle)this.cueStyles.get(var3);
               boolean var11 = var10.underline;
               int var12 = var10.style;
               int var13 = var8;
               boolean var14 = var9;
               if (var12 != 8) {
                  boolean var18;
                  if (var12 == 7) {
                     var18 = true;
                  } else {
                     var18 = false;
                  }

                  if (var12 != 7) {
                     var8 = Cea608Decoder.STYLE_COLORS[var12];
                  }

                  var14 = var18;
                  var13 = var8;
               }

               var12 = var10.start;
               int var15 = var3 + 1;
               if (var15 < this.cueStyles.size()) {
                  var8 = ((Cea608Decoder.CueBuilder.CueStyle)this.cueStyles.get(var15)).start;
               } else {
                  var8 = var2;
               }

               if (var12 == var8) {
                  var3 = var15;
                  var8 = var13;
                  var9 = var14;
               } else {
                  int var16;
                  if (var4 != -1 && !var11) {
                     setUnderlineSpan(var1, var4, var12);
                     var16 = -1;
                  } else {
                     var16 = var4;
                     if (var4 == -1) {
                        var16 = var4;
                        if (var11) {
                           var16 = var12;
                        }
                     }
                  }

                  int var17;
                  if (var5 != -1 && !var14) {
                     setItalicSpan(var1, var5, var12);
                     var17 = -1;
                  } else {
                     var17 = var5;
                     if (var5 == -1) {
                        var17 = var5;
                        if (var14) {
                           var17 = var12;
                        }
                     }
                  }

                  var3 = var15;
                  var4 = var16;
                  var5 = var17;
                  var8 = var13;
                  var9 = var14;
                  if (var13 != var7) {
                     setColorSpan(var1, var6, var12, var7);
                     var7 = var13;
                     var3 = var15;
                     var4 = var16;
                     var5 = var17;
                     var6 = var12;
                     var8 = var13;
                     var9 = var14;
                  }
               }
            }

            if (var4 != -1 && var4 != var2) {
               setUnderlineSpan(var1, var4, var2);
            }

            if (var5 != -1 && var5 != var2) {
               setItalicSpan(var1, var5, var2);
            }

            if (var6 != var2) {
               setColorSpan(var1, var6, var2, var7);
            }

            return new SpannableString(var1);
         }
      }

      private static void setColorSpan(SpannableStringBuilder var0, int var1, int var2, int var3) {
         if (var3 != -1) {
            var0.setSpan(new ForegroundColorSpan(var3), var1, var2, 33);
         }
      }

      private static void setItalicSpan(SpannableStringBuilder var0, int var1, int var2) {
         var0.setSpan(new StyleSpan(2), var1, var2, 33);
      }

      private static void setUnderlineSpan(SpannableStringBuilder var0, int var1, int var2) {
         var0.setSpan(new UnderlineSpan(), var1, var2, 33);
      }

      public void append(char var1) {
         this.captionStringBuilder.append(var1);
      }

      public void backspace() {
         int var1 = this.captionStringBuilder.length();
         if (var1 > 0) {
            this.captionStringBuilder.delete(var1 - 1, var1);

            for(int var2 = this.cueStyles.size() - 1; var2 >= 0; --var2) {
               Cea608Decoder.CueBuilder.CueStyle var3 = (Cea608Decoder.CueBuilder.CueStyle)this.cueStyles.get(var2);
               int var4 = var3.start;
               if (var4 != var1) {
                  break;
               }

               var3.start = var4 - 1;
            }
         }

      }

      public Cue build(int var1) {
         SpannableStringBuilder var2 = new SpannableStringBuilder();

         int var3;
         for(var3 = 0; var3 < this.rolledUpCaptions.size(); ++var3) {
            var2.append((CharSequence)this.rolledUpCaptions.get(var3));
            var2.append('\n');
         }

         var2.append(this.buildCurrentLine());
         if (var2.length() == 0) {
            return null;
         } else {
            var3 = this.indent + this.tabOffset;
            int var4 = 32 - var3 - var2.length();
            int var5 = var3 - var4;
            if (var1 == Integer.MIN_VALUE) {
               if (this.captionMode != 2 || Math.abs(var5) >= 3 && var4 >= 0) {
                  if (this.captionMode == 2 && var5 > 0) {
                     var1 = 2;
                  } else {
                     var1 = 0;
                  }
               } else {
                  var1 = 1;
               }
            }

            float var6;
            if (var1 != 1) {
               if (var1 == 2) {
                  var3 = 32 - var4;
               }

               var6 = (float)var3 / 32.0F * 0.8F + 0.1F;
            } else {
               var6 = 0.5F;
            }

            byte var7;
            if (this.captionMode != 1) {
               var4 = this.row;
               if (var4 <= 7) {
                  var7 = 0;
                  return new Cue(var2, Alignment.ALIGN_NORMAL, (float)var4, 1, var7, var6, var1, Float.MIN_VALUE);
               }
            }

            var4 = this.row - 15 - 2;
            var7 = 2;
            return new Cue(var2, Alignment.ALIGN_NORMAL, (float)var4, 1, var7, var6, var1, Float.MIN_VALUE);
         }
      }

      public boolean isEmpty() {
         boolean var1;
         if (this.cueStyles.isEmpty() && this.rolledUpCaptions.isEmpty() && this.captionStringBuilder.length() == 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void reset(int var1) {
         this.captionMode = var1;
         this.cueStyles.clear();
         this.rolledUpCaptions.clear();
         this.captionStringBuilder.setLength(0);
         this.row = 15;
         this.indent = 0;
         this.tabOffset = 0;
      }

      public void rollUp() {
         this.rolledUpCaptions.add(this.buildCurrentLine());
         this.captionStringBuilder.setLength(0);
         this.cueStyles.clear();
         int var1 = Math.min(this.captionRowCount, this.row);

         while(this.rolledUpCaptions.size() >= var1) {
            this.rolledUpCaptions.remove(0);
         }

      }

      public void setCaptionMode(int var1) {
         this.captionMode = var1;
      }

      public void setCaptionRowCount(int var1) {
         this.captionRowCount = var1;
      }

      public void setStyle(int var1, boolean var2) {
         this.cueStyles.add(new Cea608Decoder.CueBuilder.CueStyle(var1, var2, this.captionStringBuilder.length()));
      }

      private static class CueStyle {
         public int start;
         public final int style;
         public final boolean underline;

         public CueStyle(int var1, boolean var2, int var3) {
            this.style = var1;
            this.underline = var2;
            this.start = var3;
         }
      }
   }
}

package com.google.android.exoplayer2.text.webvtt;

import android.text.TextUtils;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.List;

public final class WebvttDecoder extends SimpleSubtitleDecoder {
   private final CssParser cssParser = new CssParser();
   private final WebvttCueParser cueParser = new WebvttCueParser();
   private final List definedStyles = new ArrayList();
   private final ParsableByteArray parsableWebvttData = new ParsableByteArray();
   private final WebvttCue.Builder webvttCueBuilder = new WebvttCue.Builder();

   public WebvttDecoder() {
      super("WebvttDecoder");
   }

   private static int getNextEvent(ParsableByteArray var0) {
      byte var1 = -1;
      int var2 = 0;

      while(var1 == -1) {
         var2 = var0.getPosition();
         String var3 = var0.readLine();
         if (var3 == null) {
            var1 = 0;
         } else if ("STYLE".equals(var3)) {
            var1 = 2;
         } else if (var3.startsWith("NOTE")) {
            var1 = 1;
         } else {
            var1 = 3;
         }
      }

      var0.setPosition(var2);
      return var1;
   }

   private static void skipComment(ParsableByteArray var0) {
      while(!TextUtils.isEmpty(var0.readLine())) {
      }

   }

   protected WebvttSubtitle decode(byte[] var1, int var2, boolean var3) throws SubtitleDecoderException {
      this.parsableWebvttData.reset(var1, var2);
      this.webvttCueBuilder.reset();
      this.definedStyles.clear();

      try {
         WebvttParserUtil.validateWebvttHeaderLine(this.parsableWebvttData);
      } catch (ParserException var5) {
         throw new SubtitleDecoderException(var5);
      }

      while(!TextUtils.isEmpty(this.parsableWebvttData.readLine())) {
      }

      ArrayList var4 = new ArrayList();

      while(true) {
         var2 = getNextEvent(this.parsableWebvttData);
         if (var2 == 0) {
            return new WebvttSubtitle(var4);
         }

         if (var2 == 1) {
            skipComment(this.parsableWebvttData);
         } else if (var2 == 2) {
            if (!var4.isEmpty()) {
               throw new SubtitleDecoderException("A style block was found after the first cue.");
            }

            this.parsableWebvttData.readLine();
            WebvttCssStyle var6 = this.cssParser.parseBlock(this.parsableWebvttData);
            if (var6 != null) {
               this.definedStyles.add(var6);
            }
         } else if (var2 == 3 && this.cueParser.parseCue(this.parsableWebvttData, this.webvttCueBuilder, this.definedStyles)) {
            var4.add(this.webvttCueBuilder.build());
            this.webvttCueBuilder.reset();
         }
      }
   }
}

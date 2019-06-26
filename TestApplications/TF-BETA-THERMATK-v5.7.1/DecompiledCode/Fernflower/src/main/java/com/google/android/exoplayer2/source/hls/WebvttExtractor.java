package com.google.android.exoplayer2.source.hls;

import android.text.TextUtils;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.text.webvtt.WebvttParserUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WebvttExtractor implements Extractor {
   private static final Pattern LOCAL_TIMESTAMP = Pattern.compile("LOCAL:([^,]+)");
   private static final Pattern MEDIA_TIMESTAMP = Pattern.compile("MPEGTS:(\\d+)");
   private final String language;
   private ExtractorOutput output;
   private byte[] sampleData;
   private final ParsableByteArray sampleDataWrapper;
   private int sampleSize;
   private final TimestampAdjuster timestampAdjuster;

   public WebvttExtractor(String var1, TimestampAdjuster var2) {
      this.language = var1;
      this.timestampAdjuster = var2;
      this.sampleDataWrapper = new ParsableByteArray();
      this.sampleData = new byte[1024];
   }

   private TrackOutput buildTrackOutput(long var1) {
      TrackOutput var3 = this.output.track(0, 3);
      var3.format(Format.createTextSampleFormat((String)null, "text/vtt", (String)null, -1, 0, this.language, (DrmInitData)null, var1));
      this.output.endTracks();
      return var3;
   }

   private void processSample() throws ParserException {
      ParsableByteArray var1 = new ParsableByteArray(this.sampleData);
      WebvttParserUtil.validateWebvttHeaderLine(var1);
      long var2 = 0L;
      long var4 = var2;

      while(true) {
         String var6 = var1.readLine();
         if (TextUtils.isEmpty(var6)) {
            Matcher var11 = WebvttParserUtil.findNextCueHeader(var1);
            if (var11 == null) {
               this.buildTrackOutput(0L);
               return;
            }

            long var9 = WebvttParserUtil.parseTimestampUs(var11.group(1));
            var2 = this.timestampAdjuster.adjustTsTimestamp(TimestampAdjuster.usToPts(var2 + var9 - var4));
            TrackOutput var12 = this.buildTrackOutput(var2 - var9);
            this.sampleDataWrapper.reset(this.sampleData, this.sampleSize);
            var12.sampleData(this.sampleDataWrapper, this.sampleSize);
            var12.sampleMetadata(var2, 1, this.sampleSize, 0, (TrackOutput.CryptoData)null);
            return;
         }

         if (var6.startsWith("X-TIMESTAMP-MAP")) {
            Matcher var7 = LOCAL_TIMESTAMP.matcher(var6);
            StringBuilder var13;
            if (!var7.find()) {
               var13 = new StringBuilder();
               var13.append("X-TIMESTAMP-MAP doesn't contain local timestamp: ");
               var13.append(var6);
               throw new ParserException(var13.toString());
            }

            Matcher var8 = MEDIA_TIMESTAMP.matcher(var6);
            if (!var8.find()) {
               var13 = new StringBuilder();
               var13.append("X-TIMESTAMP-MAP doesn't contain media timestamp: ");
               var13.append(var6);
               throw new ParserException(var13.toString());
            }

            var4 = WebvttParserUtil.parseTimestampUs(var7.group(1));
            var2 = TimestampAdjuster.ptsToUs(Long.parseLong(var8.group(1)));
         }
      }
   }

   public void init(ExtractorOutput var1) {
      this.output = var1;
      var1.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      int var3 = (int)var1.getLength();
      int var4 = this.sampleSize;
      byte[] var5 = this.sampleData;
      if (var4 == var5.length) {
         if (var3 != -1) {
            var4 = var3;
         } else {
            var4 = var5.length;
         }

         this.sampleData = Arrays.copyOf(var5, var4 * 3 / 2);
      }

      var5 = this.sampleData;
      var4 = this.sampleSize;
      var4 = var1.read(var5, var4, var5.length - var4);
      if (var4 != -1) {
         this.sampleSize += var4;
         if (var3 == -1 || this.sampleSize != var3) {
            return 0;
         }
      }

      this.processSample();
      return -1;
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      throw new IllegalStateException();
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      var1.peekFully(this.sampleData, 0, 6, false);
      this.sampleDataWrapper.reset(this.sampleData, 6);
      if (WebvttParserUtil.isWebvttHeaderLine(this.sampleDataWrapper)) {
         return true;
      } else {
         var1.peekFully(this.sampleData, 6, 3, false);
         this.sampleDataWrapper.reset(this.sampleData, 9);
         return WebvttParserUtil.isWebvttHeaderLine(this.sampleDataWrapper);
      }
   }
}

package com.google.android.exoplayer2.extractor.ts;

import android.util.SparseArray;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.util.Collections;
import java.util.List;

public interface TsPayloadReader {
   void consume(ParsableByteArray var1, int var2) throws ParserException;

   void init(TimestampAdjuster var1, ExtractorOutput var2, TsPayloadReader.TrackIdGenerator var3);

   void seek();

   public static final class DvbSubtitleInfo {
      public final byte[] initializationData;
      public final String language;
      public final int type;

      public DvbSubtitleInfo(String var1, int var2, byte[] var3) {
         this.language = var1;
         this.type = var2;
         this.initializationData = var3;
      }
   }

   public static final class EsInfo {
      public final byte[] descriptorBytes;
      public final List dvbSubtitleInfos;
      public final String language;
      public final int streamType;

      public EsInfo(int var1, String var2, List var3, byte[] var4) {
         this.streamType = var1;
         this.language = var2;
         List var5;
         if (var3 == null) {
            var5 = Collections.emptyList();
         } else {
            var5 = Collections.unmodifiableList(var3);
         }

         this.dvbSubtitleInfos = var5;
         this.descriptorBytes = var4;
      }
   }

   public interface Factory {
      SparseArray createInitialPayloadReaders();

      TsPayloadReader createPayloadReader(int var1, TsPayloadReader.EsInfo var2);
   }

   public static final class TrackIdGenerator {
      private final int firstTrackId;
      private String formatId;
      private final String formatIdPrefix;
      private int trackId;
      private final int trackIdIncrement;

      public TrackIdGenerator(int var1, int var2) {
         this(Integer.MIN_VALUE, var1, var2);
      }

      public TrackIdGenerator(int var1, int var2, int var3) {
         String var5;
         if (var1 != Integer.MIN_VALUE) {
            StringBuilder var4 = new StringBuilder();
            var4.append(var1);
            var4.append("/");
            var5 = var4.toString();
         } else {
            var5 = "";
         }

         this.formatIdPrefix = var5;
         this.firstTrackId = var2;
         this.trackIdIncrement = var3;
         this.trackId = Integer.MIN_VALUE;
      }

      private void maybeThrowUninitializedError() {
         if (this.trackId == Integer.MIN_VALUE) {
            throw new IllegalStateException("generateNewId() must be called before retrieving ids.");
         }
      }

      public void generateNewId() {
         int var1 = this.trackId;
         if (var1 == Integer.MIN_VALUE) {
            var1 = this.firstTrackId;
         } else {
            var1 += this.trackIdIncrement;
         }

         this.trackId = var1;
         StringBuilder var2 = new StringBuilder();
         var2.append(this.formatIdPrefix);
         var2.append(this.trackId);
         this.formatId = var2.toString();
      }

      public String getFormatId() {
         this.maybeThrowUninitializedError();
         return this.formatId;
      }

      public int getTrackId() {
         this.maybeThrowUninitializedError();
         return this.trackId;
      }
   }
}

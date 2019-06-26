package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class SampleToChunkBox extends AbstractFullBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_2;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_3;
   List entries = Collections.emptyList();

   static {
      ajc$preClinit();
   }

   public SampleToChunkBox() {
      super("stsc");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("SampleToChunkBox.java", SampleToChunkBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.SampleToChunkBox", "", "", "", "java.util.List"), 47);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.SampleToChunkBox", "java.util.List", "entries", "", "void"), 51);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SampleToChunkBox", "", "", "", "java.lang.String"), 84);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "blowup", "com.coremedia.iso.boxes.SampleToChunkBox", "int", "chunkCount", "", "[J"), 95);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      int var2 = CastUtils.l2i(IsoTypeReader.readUInt32(var1));
      this.entries = new ArrayList(var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         this.entries.add(new SampleToChunkBox.Entry(IsoTypeReader.readUInt32(var1), IsoTypeReader.readUInt32(var1), IsoTypeReader.readUInt32(var1)));
      }

   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      IsoTypeWriter.writeUInt32(var1, (long)this.entries.size());
      Iterator var2 = this.entries.iterator();

      while(var2.hasNext()) {
         SampleToChunkBox.Entry var3 = (SampleToChunkBox.Entry)var2.next();
         IsoTypeWriter.writeUInt32(var1, var3.getFirstChunk());
         IsoTypeWriter.writeUInt32(var1, var3.getSamplesPerChunk());
         IsoTypeWriter.writeUInt32(var1, var3.getSampleDescriptionIndex());
      }

   }

   protected long getContentSize() {
      return (long)(this.entries.size() * 12 + 8);
   }

   public List getEntries() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.entries;
   }

   public void setEntries(List var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_1, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.entries = var1;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_2, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder("SampleToChunkBox[entryCount=");
      var2.append(this.entries.size());
      var2.append("]");
      return var2.toString();
   }

   public static class Entry {
      long firstChunk;
      long sampleDescriptionIndex;
      long samplesPerChunk;

      public Entry(long var1, long var3, long var5) {
         this.firstChunk = var1;
         this.samplesPerChunk = var3;
         this.sampleDescriptionIndex = var5;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && SampleToChunkBox.Entry.class == var1.getClass()) {
            SampleToChunkBox.Entry var2 = (SampleToChunkBox.Entry)var1;
            if (this.firstChunk != var2.firstChunk) {
               return false;
            } else if (this.sampleDescriptionIndex != var2.sampleDescriptionIndex) {
               return false;
            } else {
               return this.samplesPerChunk == var2.samplesPerChunk;
            }
         } else {
            return false;
         }
      }

      public long getFirstChunk() {
         return this.firstChunk;
      }

      public long getSampleDescriptionIndex() {
         return this.sampleDescriptionIndex;
      }

      public long getSamplesPerChunk() {
         return this.samplesPerChunk;
      }

      public int hashCode() {
         long var1 = this.firstChunk;
         int var3 = (int)(var1 ^ var1 >>> 32);
         var1 = this.samplesPerChunk;
         int var4 = (int)(var1 ^ var1 >>> 32);
         var1 = this.sampleDescriptionIndex;
         return (var3 * 31 + var4) * 31 + (int)(var1 ^ var1 >>> 32);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("Entry{firstChunk=");
         var1.append(this.firstChunk);
         var1.append(", samplesPerChunk=");
         var1.append(this.samplesPerChunk);
         var1.append(", sampleDescriptionIndex=");
         var1.append(this.sampleDescriptionIndex);
         var1.append('}');
         return var1.toString();
      }
   }
}

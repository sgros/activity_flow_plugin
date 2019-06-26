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

public class CompositionTimeToSample extends AbstractFullBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   List entries = Collections.emptyList();

   static {
      ajc$preClinit();
   }

   public CompositionTimeToSample() {
      super("ctts");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("CompositionTimeToSample.java", CompositionTimeToSample.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.CompositionTimeToSample", "", "", "", "java.util.List"), 57);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.CompositionTimeToSample", "java.util.List", "entries", "", "void"), 61);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      int var2 = CastUtils.l2i(IsoTypeReader.readUInt32(var1));
      this.entries = new ArrayList(var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         CompositionTimeToSample.Entry var4 = new CompositionTimeToSample.Entry(CastUtils.l2i(IsoTypeReader.readUInt32(var1)), var1.getInt());
         this.entries.add(var4);
      }

   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      IsoTypeWriter.writeUInt32(var1, (long)this.entries.size());
      Iterator var2 = this.entries.iterator();

      while(var2.hasNext()) {
         CompositionTimeToSample.Entry var3 = (CompositionTimeToSample.Entry)var2.next();
         IsoTypeWriter.writeUInt32(var1, (long)var3.getCount());
         var1.putInt(var3.getOffset());
      }

   }

   protected long getContentSize() {
      return (long)(this.entries.size() * 8 + 8);
   }

   public void setEntries(List var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_1, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.entries = var1;
   }

   public static class Entry {
      int count;
      int offset;

      public Entry(int var1, int var2) {
         this.count = var1;
         this.offset = var2;
      }

      public int getCount() {
         return this.count;
      }

      public int getOffset() {
         return this.offset;
      }

      public void setCount(int var1) {
         this.count = var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("Entry{count=");
         var1.append(this.count);
         var1.append(", offset=");
         var1.append(this.offset);
         var1.append('}');
         return var1.toString();
      }
   }
}

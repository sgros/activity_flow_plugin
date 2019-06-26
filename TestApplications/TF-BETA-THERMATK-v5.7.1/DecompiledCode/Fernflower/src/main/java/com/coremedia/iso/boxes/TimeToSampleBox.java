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
import java.util.Map;
import java.util.WeakHashMap;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class TimeToSampleBox extends AbstractFullBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_2;
   static Map cache;
   List entries = Collections.emptyList();

   static {
      ajc$preClinit();
      cache = new WeakHashMap();
   }

   public TimeToSampleBox() {
      super("stts");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("TimeToSampleBox.java", TimeToSampleBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.TimeToSampleBox", "", "", "", "java.util.List"), 79);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.TimeToSampleBox", "java.util.List", "entries", "", "void"), 83);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.TimeToSampleBox", "", "", "", "java.lang.String"), 87);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      int var2 = CastUtils.l2i(IsoTypeReader.readUInt32(var1));
      this.entries = new ArrayList(var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         this.entries.add(new TimeToSampleBox.Entry(IsoTypeReader.readUInt32(var1), IsoTypeReader.readUInt32(var1)));
      }

   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      IsoTypeWriter.writeUInt32(var1, (long)this.entries.size());
      Iterator var2 = this.entries.iterator();

      while(var2.hasNext()) {
         TimeToSampleBox.Entry var3 = (TimeToSampleBox.Entry)var2.next();
         IsoTypeWriter.writeUInt32(var1, var3.getCount());
         IsoTypeWriter.writeUInt32(var1, var3.getDelta());
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

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_2, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder("TimeToSampleBox[entryCount=");
      var2.append(this.entries.size());
      var2.append("]");
      return var2.toString();
   }

   public static class Entry {
      long count;
      long delta;

      public Entry(long var1, long var3) {
         this.count = var1;
         this.delta = var3;
      }

      public long getCount() {
         return this.count;
      }

      public long getDelta() {
         return this.delta;
      }

      public void setCount(long var1) {
         this.count = var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("Entry{count=");
         var1.append(this.count);
         var1.append(", delta=");
         var1.append(this.delta);
         var1.append('}');
         return var1.toString();
      }
   }
}

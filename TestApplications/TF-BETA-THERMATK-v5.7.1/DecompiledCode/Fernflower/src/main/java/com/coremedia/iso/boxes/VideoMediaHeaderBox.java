package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class VideoMediaHeaderBox extends AbstractMediaHeaderBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_2;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_3;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_4;
   private int graphicsmode = 0;
   private int[] opcolor = new int[3];

   static {
      ajc$preClinit();
   }

   public VideoMediaHeaderBox() {
      super("vmhd");
      this.setFlags(1);
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("VideoMediaHeaderBox.java", VideoMediaHeaderBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getGraphicsmode", "com.coremedia.iso.boxes.VideoMediaHeaderBox", "", "", "", "int"), 39);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getOpcolor", "com.coremedia.iso.boxes.VideoMediaHeaderBox", "", "", "", "[I"), 43);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.VideoMediaHeaderBox", "", "", "", "java.lang.String"), 71);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setOpcolor", "com.coremedia.iso.boxes.VideoMediaHeaderBox", "[I", "opcolor", "", "void"), 75);
      ajc$tjp_4 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setGraphicsmode", "com.coremedia.iso.boxes.VideoMediaHeaderBox", "int", "graphicsmode", "", "void"), 79);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      this.graphicsmode = IsoTypeReader.readUInt16(var1);
      this.opcolor = new int[3];

      for(int var2 = 0; var2 < 3; ++var2) {
         this.opcolor[var2] = IsoTypeReader.readUInt16(var1);
      }

   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      IsoTypeWriter.writeUInt16(var1, this.graphicsmode);
      int[] var2 = this.opcolor;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         IsoTypeWriter.writeUInt16(var1, var2[var4]);
      }

   }

   protected long getContentSize() {
      return 12L;
   }

   public int getGraphicsmode() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_0, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.graphicsmode;
   }

   public int[] getOpcolor() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_1, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      return this.opcolor;
   }

   public String toString() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_2, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      StringBuilder var2 = new StringBuilder("VideoMediaHeaderBox[graphicsmode=");
      var2.append(this.getGraphicsmode());
      var2.append(";opcolor0=");
      var2.append(this.getOpcolor()[0]);
      var2.append(";opcolor1=");
      var2.append(this.getOpcolor()[1]);
      var2.append(";opcolor2=");
      var2.append(this.getOpcolor()[2]);
      var2.append("]");
      return var2.toString();
   }
}

package com.googlecode.mp4parser.boxes.mp4;

import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ObjectDescriptorFactory;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class AbstractDescriptorBox extends AbstractFullBox {
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
   private static Logger log;
   protected ByteBuffer data;
   protected BaseDescriptor descriptor;

   static {
      ajc$preClinit();
      log = Logger.getLogger(AbstractDescriptorBox.class.getName());
   }

   public AbstractDescriptorBox(String var1) {
      super(var1);
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("AbstractDescriptorBox.java", AbstractDescriptorBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getData", "com.googlecode.mp4parser.boxes.mp4.AbstractDescriptorBox", "", "", "", "java.nio.ByteBuffer"), 42);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getDescriptor", "com.googlecode.mp4parser.boxes.mp4.AbstractDescriptorBox", "", "", "", "com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor"), 58);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getDescriptorAsString", "com.googlecode.mp4parser.boxes.mp4.AbstractDescriptorBox", "", "", "", "java.lang.String"), 62);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setDescriptor", "com.googlecode.mp4parser.boxes.mp4.AbstractDescriptorBox", "com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor", "descriptor", "", "void"), 66);
      ajc$tjp_4 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setData", "com.googlecode.mp4parser.boxes.mp4.AbstractDescriptorBox", "java.nio.ByteBuffer", "data", "", "void"), 70);
   }

   public void _parseDetails(ByteBuffer var1) {
      this.parseVersionAndFlags(var1);
      this.data = var1.slice();
      var1.position(var1.position() + var1.remaining());

      try {
         this.data.rewind();
         this.descriptor = ObjectDescriptorFactory.createFrom(-1, this.data);
      } catch (IOException var2) {
         log.log(Level.WARNING, "Error parsing ObjectDescriptor", var2);
      } catch (IndexOutOfBoundsException var3) {
         log.log(Level.WARNING, "Error parsing ObjectDescriptor", var3);
      }

   }

   protected void getContent(ByteBuffer var1) {
      this.writeVersionAndFlags(var1);
      this.data.rewind();
      var1.put(this.data);
   }

   protected long getContentSize() {
      return (long)(this.data.limit() + 4);
   }

   public void setData(ByteBuffer var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_4, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.data = var1;
   }

   public void setDescriptor(BaseDescriptor var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_3, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      this.descriptor = var1;
   }
}

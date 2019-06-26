package com.googlecode.mp4parser.boxes.mp4;

import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import java.nio.ByteBuffer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

public class ESDescriptorBox extends AbstractDescriptorBox {
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_0;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_1;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_2;
   // $FF: synthetic field
   private static final JoinPoint.StaticPart ajc$tjp_3;

   static {
      ajc$preClinit();
   }

   public ESDescriptorBox() {
      super("esds");
   }

   // $FF: synthetic method
   private static void ajc$preClinit() {
      Factory var0 = new Factory("ESDescriptorBox.java", ESDescriptorBox.class);
      ajc$tjp_0 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "getEsDescriptor", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "", "", "", "com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor"), 33);
      ajc$tjp_1 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "setEsDescriptor", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor", "esDescriptor", "", "void"), 37);
      ajc$tjp_2 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "equals", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "java.lang.Object", "o", "", "boolean"), 42);
      ajc$tjp_3 = var0.makeSJP("method-execution", var0.makeMethodSig("1", "hashCode", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "", "", "", "int"), 53);
   }

   public boolean equals(Object var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_2, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      if (this == var1) {
         return true;
      } else if (var1 != null && ESDescriptorBox.class == var1.getClass()) {
         ESDescriptorBox var3 = (ESDescriptorBox)var1;
         ByteBuffer var4 = super.data;
         if (var4 != null) {
            if (!var4.equals(var3.data)) {
               return false;
            }
         } else if (var3.data != null) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      JoinPoint var1 = Factory.makeJP(ajc$tjp_3, this, this);
      RequiresParseDetailAspect.aspectOf().before(var1);
      ByteBuffer var3 = super.data;
      int var2;
      if (var3 != null) {
         var2 = var3.hashCode();
      } else {
         var2 = 0;
      }

      return var2;
   }

   public void setEsDescriptor(ESDescriptor var1) {
      JoinPoint var2 = Factory.makeJP(ajc$tjp_1, this, this, var1);
      RequiresParseDetailAspect.aspectOf().before(var2);
      super.setDescriptor(var1);
   }
}

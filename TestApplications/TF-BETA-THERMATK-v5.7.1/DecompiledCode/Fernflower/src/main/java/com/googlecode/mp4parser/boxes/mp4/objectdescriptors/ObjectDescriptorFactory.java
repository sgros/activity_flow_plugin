package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectDescriptorFactory {
   protected static Map descriptorRegistry = new HashMap();
   protected static Logger log = Logger.getLogger(ObjectDescriptorFactory.class.getName());

   static {
      HashSet var0 = new HashSet();
      var0.add(DecoderSpecificInfo.class);
      var0.add(SLConfigDescriptor.class);
      var0.add(BaseDescriptor.class);
      var0.add(ExtensionDescriptor.class);
      var0.add(ObjectDescriptorBase.class);
      var0.add(ProfileLevelIndicationDescriptor.class);
      var0.add(AudioSpecificConfig.class);
      var0.add(ExtensionProfileLevelDescriptor.class);
      var0.add(ESDescriptor.class);
      var0.add(DecoderConfigDescriptor.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         Class var2 = (Class)var1.next();
         Descriptor var8 = (Descriptor)var2.getAnnotation(Descriptor.class);
         int[] var3 = var8.tags();
         int var4 = var8.objectTypeIndication();
         Map var5 = (Map)descriptorRegistry.get(var4);
         Object var9 = var5;
         if (var5 == null) {
            var9 = new HashMap();
         }

         int var6 = var3.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            ((Map)var9).put(var3[var7], var2);
         }

         descriptorRegistry.put(var4, var9);
      }

   }

   public static BaseDescriptor createFrom(int var0, ByteBuffer var1) throws IOException {
      int var2 = IsoTypeReader.readUInt8(var1);
      Map var3 = (Map)descriptorRegistry.get(var0);
      Map var4 = var3;
      if (var3 == null) {
         var4 = (Map)descriptorRegistry.get(-1);
      }

      Class var8 = (Class)var4.get(var2);
      StringBuilder var9;
      Object var10;
      if (var8 != null && !var8.isInterface() && !Modifier.isAbstract(var8.getModifiers())) {
         try {
            var10 = (BaseDescriptor)var8.newInstance();
         } catch (Exception var7) {
            Logger var5 = log;
            Level var11 = Level.SEVERE;
            var9 = new StringBuilder("Couldn't instantiate BaseDescriptor class ");
            var9.append(var8);
            var9.append(" for objectTypeIndication ");
            var9.append(var0);
            var9.append(" and tag ");
            var9.append(var2);
            var5.log(var11, var9.toString(), var7);
            throw new RuntimeException(var7);
         }
      } else {
         Logger var6 = log;
         var9 = new StringBuilder("No ObjectDescriptor found for objectTypeIndication ");
         var9.append(Integer.toHexString(var0));
         var9.append(" and tag ");
         var9.append(Integer.toHexString(var2));
         var9.append(" found: ");
         var9.append(var8);
         var6.warning(var9.toString());
         var10 = new UnknownDescriptor();
      }

      ((BaseDescriptor)var10).parse(var2, var1);
      return (BaseDescriptor)var10;
   }
}

package com.googlecode.mp4parser;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.NoAspectBoundException;

public class RequiresParseDetailAspect {
   // $FF: synthetic field
   private static Throwable ajc$initFailureCause;
   // $FF: synthetic field
   public static final RequiresParseDetailAspect ajc$perSingletonInstance;

   static {
      try {
         ajc$postClinit();
      } catch (Throwable var1) {
         ajc$initFailureCause = var1;
      }

   }

   // $FF: synthetic method
   private static void ajc$postClinit() {
      ajc$perSingletonInstance = new RequiresParseDetailAspect();
   }

   public static RequiresParseDetailAspect aspectOf() {
      RequiresParseDetailAspect var0 = ajc$perSingletonInstance;
      if (var0 != null) {
         return var0;
      } else {
         throw new NoAspectBoundException("com.googlecode.mp4parser.RequiresParseDetailAspect", ajc$initFailureCause);
      }
   }

   public void before(JoinPoint var1) {
      if (var1.getTarget() instanceof AbstractBox) {
         if (!((AbstractBox)var1.getTarget()).isParsed()) {
            ((AbstractBox)var1.getTarget()).parseDetails();
         }

      } else {
         StringBuilder var2 = new StringBuilder("Only methods in subclasses of ");
         var2.append(AbstractBox.class.getName());
         var2.append(" can  be annotated with ParseDetail");
         throw new RuntimeException(var2.toString());
      }
   }
}

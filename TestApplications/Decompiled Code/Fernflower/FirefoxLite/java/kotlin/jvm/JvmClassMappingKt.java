package kotlin.jvm;

import kotlin.TypeCastException;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;

public final class JvmClassMappingKt {
   public static final Class getJavaObjectType(KClass var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Class var2 = ((ClassBasedDeclarationContainer)var0).getJClass();
      if (!var2.isPrimitive()) {
         if (var2 != null) {
            return var2;
         } else {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
         }
      } else {
         String var1 = var2.getName();
         if (var1 != null) {
            switch(var1.hashCode()) {
            case -1325958191:
               if (var1.equals("double")) {
                  var2 = Double.class;
               }
               break;
            case 104431:
               if (var1.equals("int")) {
                  var2 = Integer.class;
               }
               break;
            case 3039496:
               if (var1.equals("byte")) {
                  var2 = Byte.class;
               }
               break;
            case 3052374:
               if (var1.equals("char")) {
                  var2 = Character.class;
               }
               break;
            case 3327612:
               if (var1.equals("long")) {
                  var2 = Long.class;
               }
               break;
            case 3625364:
               if (var1.equals("void")) {
                  var2 = Void.class;
               }
               break;
            case 64711720:
               if (var1.equals("boolean")) {
                  var2 = Boolean.class;
               }
               break;
            case 97526364:
               if (var1.equals("float")) {
                  var2 = Float.class;
               }
               break;
            case 109413500:
               if (var1.equals("short")) {
                  var2 = Short.class;
               }
            }
         }

         if (var2 != null) {
            return var2;
         } else {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
         }
      }
   }
}

// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.jvm;

import kotlin.TypeCastException;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;

public final class JvmClassMappingKt
{
    public static final <T> Class<T> getJavaObjectType(final KClass<T> kClass) {
        Intrinsics.checkParameterIsNotNull(kClass, "receiver$0");
        Class<?> jClass = ((ClassBasedDeclarationContainer)kClass).getJClass();
        if (!jClass.isPrimitive()) {
            if (jClass != null) {
                return (Class<T>)jClass;
            }
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
        }
        else {
            final String name = jClass.getName();
            if (name != null) {
                switch (name.hashCode()) {
                    case 109413500: {
                        if (name.equals("short")) {
                            jClass = Short.class;
                            break;
                        }
                        break;
                    }
                    case 97526364: {
                        if (name.equals("float")) {
                            jClass = Float.class;
                            break;
                        }
                        break;
                    }
                    case 64711720: {
                        if (name.equals("boolean")) {
                            jClass = Boolean.class;
                            break;
                        }
                        break;
                    }
                    case 3625364: {
                        if (name.equals("void")) {
                            jClass = Void.class;
                            break;
                        }
                        break;
                    }
                    case 3327612: {
                        if (name.equals("long")) {
                            jClass = Long.class;
                            break;
                        }
                        break;
                    }
                    case 3052374: {
                        if (name.equals("char")) {
                            jClass = Character.class;
                            break;
                        }
                        break;
                    }
                    case 3039496: {
                        if (name.equals("byte")) {
                            jClass = Byte.class;
                            break;
                        }
                        break;
                    }
                    case 104431: {
                        if (name.equals("int")) {
                            jClass = Integer.class;
                            break;
                        }
                        break;
                    }
                    case -1325958191: {
                        if (name.equals("double")) {
                            jClass = Double.class;
                            break;
                        }
                        break;
                    }
                }
            }
            if (jClass != null) {
                return (Class<T>)jClass;
            }
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
        }
    }
}

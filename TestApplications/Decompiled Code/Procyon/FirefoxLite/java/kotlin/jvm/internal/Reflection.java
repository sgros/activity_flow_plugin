// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.jvm.internal;

import kotlin.reflect.KProperty1;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KFunction;
import kotlin.reflect.KClass;

public class Reflection
{
    private static final KClass[] EMPTY_K_CLASS_ARRAY;
    private static final ReflectionFactory factory;
    
    static {
        ReflectionFactory factory2 = null;
        try {
            factory2 = (ReflectionFactory)Class.forName("kotlin.reflect.jvm.internal.ReflectionFactoryImpl").newInstance();
        }
        catch (ClassCastException ex) {}
        catch (ClassNotFoundException ex2) {}
        catch (InstantiationException ex3) {}
        catch (IllegalAccessException ex4) {}
        if (factory2 == null) {
            factory2 = new ReflectionFactory();
        }
        factory = factory2;
        EMPTY_K_CLASS_ARRAY = new KClass[0];
    }
    
    public static KFunction function(final FunctionReference functionReference) {
        return Reflection.factory.function(functionReference);
    }
    
    public static KClass getOrCreateKotlinClass(final Class clazz) {
        return Reflection.factory.getOrCreateKotlinClass(clazz);
    }
    
    public static KMutableProperty1 mutableProperty1(final MutablePropertyReference1 mutablePropertyReference1) {
        return Reflection.factory.mutableProperty1(mutablePropertyReference1);
    }
    
    public static KProperty1 property1(final PropertyReference1 propertyReference1) {
        return Reflection.factory.property1(propertyReference1);
    }
    
    public static String renderLambdaToString(final Lambda lambda) {
        return Reflection.factory.renderLambdaToString(lambda);
    }
}

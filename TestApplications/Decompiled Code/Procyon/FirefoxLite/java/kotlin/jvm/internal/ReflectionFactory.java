// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.jvm.internal;

import kotlin.reflect.KProperty1;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;

public class ReflectionFactory
{
    public KFunction function(final FunctionReference functionReference) {
        return functionReference;
    }
    
    public KClass getOrCreateKotlinClass(final Class clazz) {
        return new ClassReference(clazz);
    }
    
    public KMutableProperty1 mutableProperty1(final MutablePropertyReference1 mutablePropertyReference1) {
        return mutablePropertyReference1;
    }
    
    public KProperty1 property1(final PropertyReference1 propertyReference1) {
        return propertyReference1;
    }
    
    public String renderLambdaToString(final FunctionBase functionBase) {
        String s2;
        final String s = s2 = functionBase.getClass().getGenericInterfaces()[0].toString();
        if (s.startsWith("kotlin.jvm.functions.")) {
            s2 = s.substring("kotlin.jvm.functions.".length());
        }
        return s2;
    }
    
    public String renderLambdaToString(final Lambda lambda) {
        return this.renderLambdaToString((FunctionBase)lambda);
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface OnLifecycleEvent {
    Lifecycle.Event value();
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.FIELD })
public @interface ToMany {
    JoinProperty[] joinProperties() default {};
    
    String referencedJoinProperty() default "";
}

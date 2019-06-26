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
@Target({ ElementType.TYPE })
public @interface Entity {
    boolean active() default false;
    
    boolean createInDb() default true;
    
    boolean generateConstructors() default true;
    
    boolean generateGettersSetters() default true;
    
    Index[] indexes() default {};
    
    String nameInDb() default "";
    
    Class protobuf() default void.class;
    
    String schema() default "default";
}

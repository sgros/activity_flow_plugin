// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.NoAspectBoundException;

public class RequiresParseDetailAspect
{
    private static /* synthetic */ Throwable ajc$initFailureCause;
    public static final /* synthetic */ RequiresParseDetailAspect ajc$perSingletonInstance;
    
    static {
        try {
            ajc$postClinit();
        }
        catch (Throwable ajc$initFailureCause) {
            RequiresParseDetailAspect.ajc$initFailureCause = ajc$initFailureCause;
        }
    }
    
    private static /* synthetic */ void ajc$postClinit() {
        ajc$perSingletonInstance = new RequiresParseDetailAspect();
    }
    
    public static RequiresParseDetailAspect aspectOf() {
        final RequiresParseDetailAspect ajc$perSingletonInstance = RequiresParseDetailAspect.ajc$perSingletonInstance;
        if (ajc$perSingletonInstance != null) {
            return ajc$perSingletonInstance;
        }
        throw new NoAspectBoundException("com.googlecode.mp4parser.RequiresParseDetailAspect", RequiresParseDetailAspect.ajc$initFailureCause);
    }
    
    public void before(final JoinPoint joinPoint) {
        if (joinPoint.getTarget() instanceof AbstractBox) {
            if (!((AbstractBox)joinPoint.getTarget()).isParsed()) {
                ((AbstractBox)joinPoint.getTarget()).parseDetails();
            }
            return;
        }
        final StringBuilder sb = new StringBuilder("Only methods in subclasses of ");
        sb.append(AbstractBox.class.getName());
        sb.append(" can  be annotated with ParseDetail");
        throw new RuntimeException(sb.toString());
    }
}

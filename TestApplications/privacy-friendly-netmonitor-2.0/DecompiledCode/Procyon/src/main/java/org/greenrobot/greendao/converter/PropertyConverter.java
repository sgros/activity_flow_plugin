// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.converter;

public interface PropertyConverter<P, D>
{
    D convertToDatabaseValue(final P p0);
    
    P convertToEntityProperty(final D p0);
}

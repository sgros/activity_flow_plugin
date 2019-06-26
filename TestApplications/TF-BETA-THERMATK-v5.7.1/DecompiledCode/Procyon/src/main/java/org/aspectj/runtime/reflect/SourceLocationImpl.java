// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.SourceLocation;

class SourceLocationImpl implements SourceLocation
{
    String fileName;
    int line;
    Class withinType;
    
    SourceLocationImpl(final Class withinType, final String fileName, final int line) {
        this.withinType = withinType;
        this.fileName = fileName;
        this.line = line;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public int getLine() {
        return this.line;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(this.getFileName());
        sb.append(":");
        sb.append(this.getLine());
        return sb.toString();
    }
}

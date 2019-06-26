// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.boxes.mp4;

import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import java.nio.ByteBuffer;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import org.aspectj.lang.JoinPoint;

public class ESDescriptorBox extends AbstractDescriptorBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3;
    
    static {
        ajc$preClinit();
    }
    
    public ESDescriptorBox() {
        super("esds");
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("ESDescriptorBox.java", ESDescriptorBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getEsDescriptor", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "", "", "", "com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor"), 33);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setEsDescriptor", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor", "esDescriptor", "", "void"), 37);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "equals", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "java.lang.Object", "o", "", "boolean"), 42);
        ajc$tjp_3 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "hashCode", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "", "", "", "int"), 53);
    }
    
    @Override
    public boolean equals(final Object o) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ESDescriptorBox.ajc$tjp_2, this, this, o));
        if (this == o) {
            return true;
        }
        if (o != null && ESDescriptorBox.class == o.getClass()) {
            final ESDescriptorBox esDescriptorBox = (ESDescriptorBox)o;
            final ByteBuffer data = super.data;
            if (data != null) {
                if (data.equals(esDescriptorBox.data)) {
                    return true;
                }
            }
            else if (esDescriptorBox.data == null) {
                return true;
            }
            return false;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ESDescriptorBox.ajc$tjp_3, this, this));
        final ByteBuffer data = super.data;
        int hashCode;
        if (data != null) {
            hashCode = data.hashCode();
        }
        else {
            hashCode = 0;
        }
        return hashCode;
    }
    
    public void setEsDescriptor(final ESDescriptor descriptor) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ESDescriptorBox.ajc$tjp_1, this, this, descriptor));
        super.setDescriptor(descriptor);
    }
}

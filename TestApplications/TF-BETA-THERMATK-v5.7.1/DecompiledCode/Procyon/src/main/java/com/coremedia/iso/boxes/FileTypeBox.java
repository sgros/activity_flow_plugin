// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.util.Iterator;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.IsoFile;
import java.util.LinkedList;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import java.util.Collections;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import com.googlecode.mp4parser.AbstractBox;

public class FileTypeBox extends AbstractBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3;
    private List<String> compatibleBrands;
    private String majorBrand;
    private long minorVersion;
    
    static {
        ajc$preClinit();
    }
    
    public FileTypeBox() {
        super("ftyp");
        this.compatibleBrands = Collections.emptyList();
    }
    
    public FileTypeBox(final String majorBrand, final long minorVersion, final List<String> compatibleBrands) {
        super("ftyp");
        this.compatibleBrands = Collections.emptyList();
        this.majorBrand = majorBrand;
        this.minorVersion = minorVersion;
        this.compatibleBrands = compatibleBrands;
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("FileTypeBox.java", FileTypeBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getMajorBrand", "com.coremedia.iso.boxes.FileTypeBox", "", "", "", "java.lang.String"), 85);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setMajorBrand", "com.coremedia.iso.boxes.FileTypeBox", "java.lang.String", "majorBrand", "", "void"), 94);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setMinorVersion", "com.coremedia.iso.boxes.FileTypeBox", "long", "minorVersion", "", "void"), 103);
        ajc$tjp_3 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getMinorVersion", "com.coremedia.iso.boxes.FileTypeBox", "", "", "", "long"), 113);
        ajc$tjp_4 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getCompatibleBrands", "com.coremedia.iso.boxes.FileTypeBox", "", "", "", "java.util.List"), 122);
        ajc$tjp_5 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setCompatibleBrands", "com.coremedia.iso.boxes.FileTypeBox", "java.util.List", "compatibleBrands", "", "void"), 126);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.majorBrand = IsoTypeReader.read4cc(byteBuffer);
        this.minorVersion = IsoTypeReader.readUInt32(byteBuffer);
        final int n = byteBuffer.remaining() / 4;
        this.compatibleBrands = new LinkedList<String>();
        for (int i = 0; i < n; ++i) {
            this.compatibleBrands.add(IsoTypeReader.read4cc(byteBuffer));
        }
    }
    
    @Override
    protected void getContent(final ByteBuffer byteBuffer) {
        byteBuffer.put(IsoFile.fourCCtoBytes(this.majorBrand));
        IsoTypeWriter.writeUInt32(byteBuffer, this.minorVersion);
        final Iterator<String> iterator = this.compatibleBrands.iterator();
        while (iterator.hasNext()) {
            byteBuffer.put(IsoFile.fourCCtoBytes(iterator.next()));
        }
    }
    
    @Override
    protected long getContentSize() {
        return this.compatibleBrands.size() * 4 + 8;
    }
    
    public String getMajorBrand() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(FileTypeBox.ajc$tjp_0, this, this));
        return this.majorBrand;
    }
    
    public long getMinorVersion() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(FileTypeBox.ajc$tjp_3, this, this));
        return this.minorVersion;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FileTypeBox[");
        sb.append("majorBrand=");
        sb.append(this.getMajorBrand());
        sb.append(";");
        sb.append("minorVersion=");
        sb.append(this.getMinorVersion());
        for (final String str : this.compatibleBrands) {
            sb.append(";");
            sb.append("compatibleBrand=");
            sb.append(str);
        }
        sb.append("]");
        return sb.toString();
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;
import org.aspectj.lang.Signature;
import org.aspectj.runtime.reflect.Factory;
import org.aspectj.lang.JoinPoint;

public class VideoMediaHeaderBox extends AbstractMediaHeaderBox
{
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2;
    private int graphicsmode;
    private int[] opcolor;
    
    static {
        ajc$preClinit();
    }
    
    public VideoMediaHeaderBox() {
        super("vmhd");
        this.graphicsmode = 0;
        this.opcolor = new int[3];
        this.setFlags(1);
    }
    
    private static /* synthetic */ void ajc$preClinit() {
        final Factory factory = new Factory("VideoMediaHeaderBox.java", VideoMediaHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getGraphicsmode", "com.coremedia.iso.boxes.VideoMediaHeaderBox", "", "", "", "int"), 39);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getOpcolor", "com.coremedia.iso.boxes.VideoMediaHeaderBox", "", "", "", "[I"), 43);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.VideoMediaHeaderBox", "", "", "", "java.lang.String"), 71);
        ajc$tjp_3 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setOpcolor", "com.coremedia.iso.boxes.VideoMediaHeaderBox", "[I", "opcolor", "", "void"), 75);
        ajc$tjp_4 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setGraphicsmode", "com.coremedia.iso.boxes.VideoMediaHeaderBox", "int", "graphicsmode", "", "void"), 79);
    }
    
    public void _parseDetails(final ByteBuffer byteBuffer) {
        this.parseVersionAndFlags(byteBuffer);
        this.graphicsmode = IsoTypeReader.readUInt16(byteBuffer);
        this.opcolor = new int[3];
        for (int i = 0; i < 3; ++i) {
            this.opcolor[i] = IsoTypeReader.readUInt16(byteBuffer);
        }
    }
    
    @Override
    protected void getContent(final ByteBuffer byteBuffer) {
        this.writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt16(byteBuffer, this.graphicsmode);
        final int[] opcolor = this.opcolor;
        for (int length = opcolor.length, i = 0; i < length; ++i) {
            IsoTypeWriter.writeUInt16(byteBuffer, opcolor[i]);
        }
    }
    
    @Override
    protected long getContentSize() {
        return 12L;
    }
    
    public int getGraphicsmode() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(VideoMediaHeaderBox.ajc$tjp_0, this, this));
        return this.graphicsmode;
    }
    
    public int[] getOpcolor() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(VideoMediaHeaderBox.ajc$tjp_1, this, this));
        return this.opcolor;
    }
    
    @Override
    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(VideoMediaHeaderBox.ajc$tjp_2, this, this));
        final StringBuilder sb = new StringBuilder("VideoMediaHeaderBox[graphicsmode=");
        sb.append(this.getGraphicsmode());
        sb.append(";opcolor0=");
        sb.append(this.getOpcolor()[0]);
        sb.append(";opcolor1=");
        sb.append(this.getOpcolor()[1]);
        sb.append(";opcolor2=");
        sb.append(this.getOpcolor()[2]);
        sb.append("]");
        return sb.toString();
    }
}

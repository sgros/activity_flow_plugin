// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.ParserException;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;

interface EbmlReaderOutput
{
    void binaryElement(final int p0, final int p1, final ExtractorInput p2) throws IOException, InterruptedException;
    
    void endMasterElement(final int p0) throws ParserException;
    
    void floatElement(final int p0, final double p1) throws ParserException;
    
    int getElementType(final int p0);
    
    void integerElement(final int p0, final long p1) throws ParserException;
    
    boolean isLevel1Element(final int p0);
    
    void startMasterElement(final int p0, final long p1, final long p2) throws ParserException;
    
    void stringElement(final int p0, final String p1) throws ParserException;
}

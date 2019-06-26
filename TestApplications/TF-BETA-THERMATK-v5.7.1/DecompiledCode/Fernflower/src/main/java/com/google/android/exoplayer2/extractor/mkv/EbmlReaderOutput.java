package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import java.io.IOException;

interface EbmlReaderOutput {
   void binaryElement(int var1, int var2, ExtractorInput var3) throws IOException, InterruptedException;

   void endMasterElement(int var1) throws ParserException;

   void floatElement(int var1, double var2) throws ParserException;

   int getElementType(int var1);

   void integerElement(int var1, long var2) throws ParserException;

   boolean isLevel1Element(int var1);

   void startMasterElement(int var1, long var2, long var4) throws ParserException;

   void stringElement(int var1, String var2) throws ParserException;
}

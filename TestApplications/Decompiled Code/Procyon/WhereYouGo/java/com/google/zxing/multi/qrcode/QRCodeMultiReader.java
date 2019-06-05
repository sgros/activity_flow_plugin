// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.multi.qrcode;

import java.io.Serializable;
import com.google.zxing.NotFoundException;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import com.google.zxing.BinaryBitmap;
import java.util.Iterator;
import com.google.zxing.BarcodeFormat;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import com.google.zxing.ResultMetadataType;
import java.util.List;
import com.google.zxing.ResultPoint;
import com.google.zxing.Result;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.qrcode.QRCodeReader;

public final class QRCodeMultiReader extends QRCodeReader implements MultipleBarcodeReader
{
    private static final Result[] EMPTY_RESULT_ARRAY;
    private static final ResultPoint[] NO_POINTS;
    
    static {
        EMPTY_RESULT_ARRAY = new Result[0];
        NO_POINTS = new ResultPoint[0];
    }
    
    private static List<Result> processStructuredAppend(List<Result> list) {
        final int n = 0;
        final Iterator<Object> iterator = list.iterator();
        while (true) {
            do {
                final int n2 = n;
                if (iterator.hasNext()) {
                    continue;
                }
                if (n2 != 0) {
                    final ArrayList<Result> list2 = new ArrayList<Result>();
                    final ArrayList<Object> list3 = new ArrayList<Object>();
                    for (final Result result : list) {
                        list2.add(result);
                        if (result.getResultMetadata().containsKey(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE)) {
                            list3.add(result);
                        }
                    }
                    Collections.sort(list3, (Comparator<? super Object>)new SAComparator());
                    final StringBuilder sb = new StringBuilder();
                    int n3 = 0;
                    int n4 = 0;
                    for (final Result result2 : list3) {
                        sb.append(result2.getText());
                        final int n5 = n3 += result2.getRawBytes().length;
                        if (result2.getResultMetadata().containsKey(ResultMetadataType.BYTE_SEGMENTS)) {
                            final Iterator iterator4 = result2.getResultMetadata().get(ResultMetadataType.BYTE_SEGMENTS).iterator();
                            int n6 = n4;
                            while (true) {
                                n4 = n6;
                                n3 = n5;
                                if (!iterator4.hasNext()) {
                                    break;
                                }
                                n6 += ((byte[])iterator4.next()).length;
                            }
                        }
                    }
                    final byte[] array = new byte[n3];
                    final byte[] array2 = new byte[n4];
                    int n7 = 0;
                    int n8 = 0;
                    for (final Result result3 : list3) {
                        System.arraycopy(result3.getRawBytes(), 0, array, n7, result3.getRawBytes().length);
                        final int n9 = n7 += result3.getRawBytes().length;
                        if (result3.getResultMetadata().containsKey(ResultMetadataType.BYTE_SEGMENTS)) {
                            final Iterator<byte[]> iterator6 = result3.getResultMetadata().get(ResultMetadataType.BYTE_SEGMENTS).iterator();
                            int n10 = n8;
                            while (true) {
                                n8 = n10;
                                n7 = n9;
                                if (!iterator6.hasNext()) {
                                    break;
                                }
                                final byte[] array3 = iterator6.next();
                                System.arraycopy(array3, 0, array2, n10, array3.length);
                                n10 += array3.length;
                            }
                        }
                    }
                    final Result result4 = new Result(sb.toString(), array, QRCodeMultiReader.NO_POINTS, BarcodeFormat.QR_CODE);
                    if (n4 > 0) {
                        final ArrayList<byte[]> list4 = new ArrayList<byte[]>();
                        list4.add(array2);
                        result4.putMetadata(ResultMetadataType.BYTE_SEGMENTS, list4);
                    }
                    list2.add(result4);
                    list = list2;
                }
                return list;
            } while (!iterator.next().getResultMetadata().containsKey(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE));
            final int n2 = 1;
            continue;
        }
    }
    
    @Override
    public Result[] decodeMultiple(final BinaryBitmap binaryBitmap) throws NotFoundException {
        return this.decodeMultiple(binaryBitmap, null);
    }
    
    @Override
    public Result[] decodeMultiple(final BinaryBitmap p0, final Map<DecodeHintType, ?> p1) throws NotFoundException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/ArrayList.<init>:()V
        //     7: astore_3       
        //     8: new             Lcom/google/zxing/multi/qrcode/detector/MultiDetector;
        //    11: dup            
        //    12: aload_1        
        //    13: invokevirtual   com/google/zxing/BinaryBitmap.getBlackMatrix:()Lcom/google/zxing/common/BitMatrix;
        //    16: invokespecial   com/google/zxing/multi/qrcode/detector/MultiDetector.<init>:(Lcom/google/zxing/common/BitMatrix;)V
        //    19: aload_2        
        //    20: invokevirtual   com/google/zxing/multi/qrcode/detector/MultiDetector.detectMulti:(Ljava/util/Map;)[Lcom/google/zxing/common/DetectorResult;
        //    23: astore_1       
        //    24: aload_1        
        //    25: arraylength    
        //    26: istore          4
        //    28: iconst_0       
        //    29: istore          5
        //    31: iload           5
        //    33: iload           4
        //    35: if_icmpge       214
        //    38: aload_1        
        //    39: iload           5
        //    41: aaload         
        //    42: astore          6
        //    44: aload_0        
        //    45: invokevirtual   com/google/zxing/multi/qrcode/QRCodeMultiReader.getDecoder:()Lcom/google/zxing/qrcode/decoder/Decoder;
        //    48: aload           6
        //    50: invokevirtual   com/google/zxing/common/DetectorResult.getBits:()Lcom/google/zxing/common/BitMatrix;
        //    53: aload_2        
        //    54: invokevirtual   com/google/zxing/qrcode/decoder/Decoder.decode:(Lcom/google/zxing/common/BitMatrix;Ljava/util/Map;)Lcom/google/zxing/common/DecoderResult;
        //    57: astore          7
        //    59: aload           6
        //    61: invokevirtual   com/google/zxing/common/DetectorResult.getPoints:()[Lcom/google/zxing/ResultPoint;
        //    64: astore          8
        //    66: aload           7
        //    68: invokevirtual   com/google/zxing/common/DecoderResult.getOther:()Ljava/lang/Object;
        //    71: instanceof      Lcom/google/zxing/qrcode/decoder/QRCodeDecoderMetaData;
        //    74: ifeq            90
        //    77: aload           7
        //    79: invokevirtual   com/google/zxing/common/DecoderResult.getOther:()Ljava/lang/Object;
        //    82: checkcast       Lcom/google/zxing/qrcode/decoder/QRCodeDecoderMetaData;
        //    85: aload           8
        //    87: invokevirtual   com/google/zxing/qrcode/decoder/QRCodeDecoderMetaData.applyMirroredCorrection:([Lcom/google/zxing/ResultPoint;)V
        //    90: new             Lcom/google/zxing/Result;
        //    93: astore          6
        //    95: aload           6
        //    97: aload           7
        //    99: invokevirtual   com/google/zxing/common/DecoderResult.getText:()Ljava/lang/String;
        //   102: aload           7
        //   104: invokevirtual   com/google/zxing/common/DecoderResult.getRawBytes:()[B
        //   107: aload           8
        //   109: getstatic       com/google/zxing/BarcodeFormat.QR_CODE:Lcom/google/zxing/BarcodeFormat;
        //   112: invokespecial   com/google/zxing/Result.<init>:(Ljava/lang/String;[B[Lcom/google/zxing/ResultPoint;Lcom/google/zxing/BarcodeFormat;)V
        //   115: aload           7
        //   117: invokevirtual   com/google/zxing/common/DecoderResult.getByteSegments:()Ljava/util/List;
        //   120: astore          8
        //   122: aload           8
        //   124: ifnull          137
        //   127: aload           6
        //   129: getstatic       com/google/zxing/ResultMetadataType.BYTE_SEGMENTS:Lcom/google/zxing/ResultMetadataType;
        //   132: aload           8
        //   134: invokevirtual   com/google/zxing/Result.putMetadata:(Lcom/google/zxing/ResultMetadataType;Ljava/lang/Object;)V
        //   137: aload           7
        //   139: invokevirtual   com/google/zxing/common/DecoderResult.getECLevel:()Ljava/lang/String;
        //   142: astore          8
        //   144: aload           8
        //   146: ifnull          159
        //   149: aload           6
        //   151: getstatic       com/google/zxing/ResultMetadataType.ERROR_CORRECTION_LEVEL:Lcom/google/zxing/ResultMetadataType;
        //   154: aload           8
        //   156: invokevirtual   com/google/zxing/Result.putMetadata:(Lcom/google/zxing/ResultMetadataType;Ljava/lang/Object;)V
        //   159: aload           7
        //   161: invokevirtual   com/google/zxing/common/DecoderResult.hasStructuredAppend:()Z
        //   164: ifeq            199
        //   167: aload           6
        //   169: getstatic       com/google/zxing/ResultMetadataType.STRUCTURED_APPEND_SEQUENCE:Lcom/google/zxing/ResultMetadataType;
        //   172: aload           7
        //   174: invokevirtual   com/google/zxing/common/DecoderResult.getStructuredAppendSequenceNumber:()I
        //   177: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   180: invokevirtual   com/google/zxing/Result.putMetadata:(Lcom/google/zxing/ResultMetadataType;Ljava/lang/Object;)V
        //   183: aload           6
        //   185: getstatic       com/google/zxing/ResultMetadataType.STRUCTURED_APPEND_PARITY:Lcom/google/zxing/ResultMetadataType;
        //   188: aload           7
        //   190: invokevirtual   com/google/zxing/common/DecoderResult.getStructuredAppendParity:()I
        //   193: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   196: invokevirtual   com/google/zxing/Result.putMetadata:(Lcom/google/zxing/ResultMetadataType;Ljava/lang/Object;)V
        //   199: aload_3        
        //   200: aload           6
        //   202: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   207: pop            
        //   208: iinc            5, 1
        //   211: goto            31
        //   214: aload_3        
        //   215: invokeinterface java/util/List.isEmpty:()Z
        //   220: ifeq            229
        //   223: getstatic       com/google/zxing/multi/qrcode/QRCodeMultiReader.EMPTY_RESULT_ARRAY:[Lcom/google/zxing/Result;
        //   226: astore_1       
        //   227: aload_1        
        //   228: areturn        
        //   229: aload_3        
        //   230: invokestatic    com/google/zxing/multi/qrcode/QRCodeMultiReader.processStructuredAppend:(Ljava/util/List;)Ljava/util/List;
        //   233: astore_1       
        //   234: aload_1        
        //   235: aload_1        
        //   236: invokeinterface java/util/List.size:()I
        //   241: anewarray       Lcom/google/zxing/Result;
        //   244: invokeinterface java/util/List.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   249: checkcast       [Lcom/google/zxing/Result;
        //   252: astore_1       
        //   253: goto            227
        //   256: astore          7
        //   258: goto            208
        //    Exceptions:
        //  throws com.google.zxing.NotFoundException
        //    Signature:
        //  (Lcom/google/zxing/BinaryBitmap;Ljava/util/Map<Lcom/google/zxing/DecodeHintType;*>;)[Lcom/google/zxing/Result;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  44     90     256    261    Lcom/google/zxing/ReaderException;
        //  90     122    256    261    Lcom/google/zxing/ReaderException;
        //  127    137    256    261    Lcom/google/zxing/ReaderException;
        //  137    144    256    261    Lcom/google/zxing/ReaderException;
        //  149    159    256    261    Lcom/google/zxing/ReaderException;
        //  159    199    256    261    Lcom/google/zxing/ReaderException;
        //  199    208    256    261    Lcom/google/zxing/ReaderException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static final class SAComparator implements Serializable, Comparator<Result>
    {
        @Override
        public int compare(final Result result, final Result result2) {
            final int intValue = result.getResultMetadata().get(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE);
            final int intValue2 = result2.getResultMetadata().get(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE);
            int n;
            if (intValue < intValue2) {
                n = -1;
            }
            else if (intValue > intValue2) {
                n = 1;
            }
            else {
                n = 0;
            }
            return n;
        }
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.aztec;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import com.google.zxing.Result;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Reader;

public final class AztecReader implements Reader
{
    @Override
    public Result decode(final BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return this.decode(binaryBitmap, null);
    }
    
    @Override
    public Result decode(final BinaryBitmap p0, final Map<DecodeHintType, ?> p1) throws NotFoundException, FormatException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_3       
        //     2: aconst_null    
        //     3: astore          4
        //     5: new             Lcom/google/zxing/aztec/detector/Detector;
        //     8: dup            
        //     9: aload_1        
        //    10: invokevirtual   com/google/zxing/BinaryBitmap.getBlackMatrix:()Lcom/google/zxing/common/BitMatrix;
        //    13: invokespecial   com/google/zxing/aztec/detector/Detector.<init>:(Lcom/google/zxing/common/BitMatrix;)V
        //    16: astore          5
        //    18: aconst_null    
        //    19: astore          6
        //    21: aconst_null    
        //    22: astore_1       
        //    23: aconst_null    
        //    24: astore          7
        //    26: aload_1        
        //    27: astore          8
        //    29: aload           6
        //    31: astore          9
        //    33: aload           5
        //    35: iconst_0       
        //    36: invokevirtual   com/google/zxing/aztec/detector/Detector.detect:(Z)Lcom/google/zxing/aztec/AztecDetectorResult;
        //    39: astore          10
        //    41: aload_1        
        //    42: astore          8
        //    44: aload           6
        //    46: astore          9
        //    48: aload           10
        //    50: invokevirtual   com/google/zxing/aztec/AztecDetectorResult.getPoints:()[Lcom/google/zxing/ResultPoint;
        //    53: astore_1       
        //    54: aload_1        
        //    55: astore          8
        //    57: aload_1        
        //    58: astore          9
        //    60: new             Lcom/google/zxing/aztec/decoder/Decoder;
        //    63: astore          6
        //    65: aload_1        
        //    66: astore          8
        //    68: aload_1        
        //    69: astore          9
        //    71: aload           6
        //    73: invokespecial   com/google/zxing/aztec/decoder/Decoder.<init>:()V
        //    76: aload_1        
        //    77: astore          8
        //    79: aload_1        
        //    80: astore          9
        //    82: aload           6
        //    84: aload           10
        //    86: invokevirtual   com/google/zxing/aztec/decoder/Decoder.decode:(Lcom/google/zxing/aztec/AztecDetectorResult;)Lcom/google/zxing/common/DecoderResult;
        //    89: astore          6
        //    91: aload           6
        //    93: astore          9
        //    95: aload           4
        //    97: astore          8
        //    99: aload_1        
        //   100: astore          4
        //   102: aload           9
        //   104: astore_1       
        //   105: aload           9
        //   107: ifnonnull       140
        //   110: aload           5
        //   112: iconst_1       
        //   113: invokevirtual   com/google/zxing/aztec/detector/Detector.detect:(Z)Lcom/google/zxing/aztec/AztecDetectorResult;
        //   116: astore_1       
        //   117: aload_1        
        //   118: invokevirtual   com/google/zxing/aztec/AztecDetectorResult.getPoints:()[Lcom/google/zxing/ResultPoint;
        //   121: astore          4
        //   123: new             Lcom/google/zxing/aztec/decoder/Decoder;
        //   126: astore          9
        //   128: aload           9
        //   130: invokespecial   com/google/zxing/aztec/decoder/Decoder.<init>:()V
        //   133: aload           9
        //   135: aload_1        
        //   136: invokevirtual   com/google/zxing/aztec/decoder/Decoder.decode:(Lcom/google/zxing/aztec/AztecDetectorResult;)Lcom/google/zxing/common/DecoderResult;
        //   139: astore_1       
        //   140: aload_2        
        //   141: ifnull          237
        //   144: aload_2        
        //   145: getstatic       com/google/zxing/DecodeHintType.NEED_RESULT_POINT_CALLBACK:Lcom/google/zxing/DecodeHintType;
        //   148: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   153: checkcast       Lcom/google/zxing/ResultPointCallback;
        //   156: astore_2       
        //   157: aload_2        
        //   158: ifnull          237
        //   161: aload           4
        //   163: arraylength    
        //   164: istore          11
        //   166: iconst_0       
        //   167: istore          12
        //   169: iload           12
        //   171: iload           11
        //   173: if_icmpge       237
        //   176: aload_2        
        //   177: aload           4
        //   179: iload           12
        //   181: aaload         
        //   182: invokeinterface com/google/zxing/ResultPointCallback.foundPossibleResultPoint:(Lcom/google/zxing/ResultPoint;)V
        //   187: iinc            12, 1
        //   190: goto            169
        //   193: astore_3       
        //   194: aload           8
        //   196: astore_1       
        //   197: aload           7
        //   199: astore          9
        //   201: aload           4
        //   203: astore          8
        //   205: goto            99
        //   208: astore          8
        //   210: aload           9
        //   212: astore_1       
        //   213: aload           7
        //   215: astore          9
        //   217: goto            99
        //   220: astore_1       
        //   221: aload_3        
        //   222: ifnull          227
        //   225: aload_3        
        //   226: athrow         
        //   227: aload           8
        //   229: ifnull          235
        //   232: aload           8
        //   234: athrow         
        //   235: aload_1        
        //   236: athrow         
        //   237: new             Lcom/google/zxing/Result;
        //   240: dup            
        //   241: aload_1        
        //   242: invokevirtual   com/google/zxing/common/DecoderResult.getText:()Ljava/lang/String;
        //   245: aload_1        
        //   246: invokevirtual   com/google/zxing/common/DecoderResult.getRawBytes:()[B
        //   249: aload_1        
        //   250: invokevirtual   com/google/zxing/common/DecoderResult.getNumBits:()I
        //   253: aload           4
        //   255: getstatic       com/google/zxing/BarcodeFormat.AZTEC:Lcom/google/zxing/BarcodeFormat;
        //   258: invokestatic    java/lang/System.currentTimeMillis:()J
        //   261: invokespecial   com/google/zxing/Result.<init>:(Ljava/lang/String;[BI[Lcom/google/zxing/ResultPoint;Lcom/google/zxing/BarcodeFormat;J)V
        //   264: astore_2       
        //   265: aload_1        
        //   266: invokevirtual   com/google/zxing/common/DecoderResult.getByteSegments:()Ljava/util/List;
        //   269: astore          9
        //   271: aload           9
        //   273: ifnull          285
        //   276: aload_2        
        //   277: getstatic       com/google/zxing/ResultMetadataType.BYTE_SEGMENTS:Lcom/google/zxing/ResultMetadataType;
        //   280: aload           9
        //   282: invokevirtual   com/google/zxing/Result.putMetadata:(Lcom/google/zxing/ResultMetadataType;Ljava/lang/Object;)V
        //   285: aload_1        
        //   286: invokevirtual   com/google/zxing/common/DecoderResult.getECLevel:()Ljava/lang/String;
        //   289: astore_1       
        //   290: aload_1        
        //   291: ifnull          302
        //   294: aload_2        
        //   295: getstatic       com/google/zxing/ResultMetadataType.ERROR_CORRECTION_LEVEL:Lcom/google/zxing/ResultMetadataType;
        //   298: aload_1        
        //   299: invokevirtual   com/google/zxing/Result.putMetadata:(Lcom/google/zxing/ResultMetadataType;Ljava/lang/Object;)V
        //   302: aload_2        
        //   303: areturn        
        //   304: astore_1       
        //   305: goto            221
        //    Exceptions:
        //  throws com.google.zxing.NotFoundException
        //  throws com.google.zxing.FormatException
        //    Signature:
        //  (Lcom/google/zxing/BinaryBitmap;Ljava/util/Map<Lcom/google/zxing/DecodeHintType;*>;)Lcom/google/zxing/Result;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  33     41     193    208    Lcom/google/zxing/NotFoundException;
        //  33     41     208    220    Lcom/google/zxing/FormatException;
        //  48     54     193    208    Lcom/google/zxing/NotFoundException;
        //  48     54     208    220    Lcom/google/zxing/FormatException;
        //  60     65     193    208    Lcom/google/zxing/NotFoundException;
        //  60     65     208    220    Lcom/google/zxing/FormatException;
        //  71     76     193    208    Lcom/google/zxing/NotFoundException;
        //  71     76     208    220    Lcom/google/zxing/FormatException;
        //  82     91     193    208    Lcom/google/zxing/NotFoundException;
        //  82     91     208    220    Lcom/google/zxing/FormatException;
        //  110    140    220    221    Lcom/google/zxing/NotFoundException;
        //  110    140    304    308    Lcom/google/zxing/FormatException;
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
    
    @Override
    public void reset() {
    }
}

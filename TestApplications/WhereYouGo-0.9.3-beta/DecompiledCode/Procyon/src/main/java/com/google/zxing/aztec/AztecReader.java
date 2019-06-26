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
        //    22: astore          7
        //    24: aconst_null    
        //    25: astore          8
        //    27: aload           7
        //    29: astore          9
        //    31: aload           6
        //    33: astore_1       
        //    34: aload           5
        //    36: iconst_0       
        //    37: invokevirtual   com/google/zxing/aztec/detector/Detector.detect:(Z)Lcom/google/zxing/aztec/AztecDetectorResult;
        //    40: astore          10
        //    42: aload           7
        //    44: astore          9
        //    46: aload           6
        //    48: astore_1       
        //    49: aload           10
        //    51: invokevirtual   com/google/zxing/aztec/AztecDetectorResult.getPoints:()[Lcom/google/zxing/ResultPoint;
        //    54: astore          7
        //    56: aload           7
        //    58: astore          9
        //    60: aload           7
        //    62: astore_1       
        //    63: new             Lcom/google/zxing/aztec/decoder/Decoder;
        //    66: astore          6
        //    68: aload           7
        //    70: astore          9
        //    72: aload           7
        //    74: astore_1       
        //    75: aload           6
        //    77: invokespecial   com/google/zxing/aztec/decoder/Decoder.<init>:()V
        //    80: aload           7
        //    82: astore          9
        //    84: aload           7
        //    86: astore_1       
        //    87: aload           6
        //    89: aload           10
        //    91: invokevirtual   com/google/zxing/aztec/decoder/Decoder.decode:(Lcom/google/zxing/aztec/AztecDetectorResult;)Lcom/google/zxing/common/DecoderResult;
        //    94: astore          6
        //    96: aload           6
        //    98: astore          9
        //   100: aload           7
        //   102: astore_1       
        //   103: aload_1        
        //   104: astore          7
        //   106: aload           9
        //   108: astore_1       
        //   109: aload           9
        //   111: ifnonnull       144
        //   114: aload           5
        //   116: iconst_1       
        //   117: invokevirtual   com/google/zxing/aztec/detector/Detector.detect:(Z)Lcom/google/zxing/aztec/AztecDetectorResult;
        //   120: astore_1       
        //   121: aload_1        
        //   122: invokevirtual   com/google/zxing/aztec/AztecDetectorResult.getPoints:()[Lcom/google/zxing/ResultPoint;
        //   125: astore          7
        //   127: new             Lcom/google/zxing/aztec/decoder/Decoder;
        //   130: astore          9
        //   132: aload           9
        //   134: invokespecial   com/google/zxing/aztec/decoder/Decoder.<init>:()V
        //   137: aload           9
        //   139: aload_1        
        //   140: invokevirtual   com/google/zxing/aztec/decoder/Decoder.decode:(Lcom/google/zxing/aztec/AztecDetectorResult;)Lcom/google/zxing/common/DecoderResult;
        //   143: astore_1       
        //   144: aload_2        
        //   145: ifnull          234
        //   148: aload_2        
        //   149: getstatic       com/google/zxing/DecodeHintType.NEED_RESULT_POINT_CALLBACK:Lcom/google/zxing/DecodeHintType;
        //   152: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   157: checkcast       Lcom/google/zxing/ResultPointCallback;
        //   160: astore_2       
        //   161: aload_2        
        //   162: ifnull          234
        //   165: aload           7
        //   167: arraylength    
        //   168: istore          11
        //   170: iconst_0       
        //   171: istore          12
        //   173: iload           12
        //   175: iload           11
        //   177: if_icmpge       234
        //   180: aload_2        
        //   181: aload           7
        //   183: iload           12
        //   185: aaload         
        //   186: invokeinterface com/google/zxing/ResultPointCallback.foundPossibleResultPoint:(Lcom/google/zxing/ResultPoint;)V
        //   191: iinc            12, 1
        //   194: goto            173
        //   197: astore_3       
        //   198: aload           9
        //   200: astore_1       
        //   201: aload           8
        //   203: astore          9
        //   205: goto            103
        //   208: astore          4
        //   210: aload           8
        //   212: astore          9
        //   214: goto            103
        //   217: astore_1       
        //   218: aload_3        
        //   219: ifnull          224
        //   222: aload_3        
        //   223: athrow         
        //   224: aload           4
        //   226: ifnull          232
        //   229: aload           4
        //   231: athrow         
        //   232: aload_1        
        //   233: athrow         
        //   234: new             Lcom/google/zxing/Result;
        //   237: dup            
        //   238: aload_1        
        //   239: invokevirtual   com/google/zxing/common/DecoderResult.getText:()Ljava/lang/String;
        //   242: aload_1        
        //   243: invokevirtual   com/google/zxing/common/DecoderResult.getRawBytes:()[B
        //   246: aload_1        
        //   247: invokevirtual   com/google/zxing/common/DecoderResult.getNumBits:()I
        //   250: aload           7
        //   252: getstatic       com/google/zxing/BarcodeFormat.AZTEC:Lcom/google/zxing/BarcodeFormat;
        //   255: invokestatic    java/lang/System.currentTimeMillis:()J
        //   258: invokespecial   com/google/zxing/Result.<init>:(Ljava/lang/String;[BI[Lcom/google/zxing/ResultPoint;Lcom/google/zxing/BarcodeFormat;J)V
        //   261: astore_2       
        //   262: aload_1        
        //   263: invokevirtual   com/google/zxing/common/DecoderResult.getByteSegments:()Ljava/util/List;
        //   266: astore          7
        //   268: aload           7
        //   270: ifnull          282
        //   273: aload_2        
        //   274: getstatic       com/google/zxing/ResultMetadataType.BYTE_SEGMENTS:Lcom/google/zxing/ResultMetadataType;
        //   277: aload           7
        //   279: invokevirtual   com/google/zxing/Result.putMetadata:(Lcom/google/zxing/ResultMetadataType;Ljava/lang/Object;)V
        //   282: aload_1        
        //   283: invokevirtual   com/google/zxing/common/DecoderResult.getECLevel:()Ljava/lang/String;
        //   286: astore_1       
        //   287: aload_1        
        //   288: ifnull          299
        //   291: aload_2        
        //   292: getstatic       com/google/zxing/ResultMetadataType.ERROR_CORRECTION_LEVEL:Lcom/google/zxing/ResultMetadataType;
        //   295: aload_1        
        //   296: invokevirtual   com/google/zxing/Result.putMetadata:(Lcom/google/zxing/ResultMetadataType;Ljava/lang/Object;)V
        //   299: aload_2        
        //   300: areturn        
        //   301: astore_1       
        //   302: goto            218
        //    Exceptions:
        //  throws com.google.zxing.NotFoundException
        //  throws com.google.zxing.FormatException
        //    Signature:
        //  (Lcom/google/zxing/BinaryBitmap;Ljava/util/Map<Lcom/google/zxing/DecodeHintType;*>;)Lcom/google/zxing/Result;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  34     42     197    208    Lcom/google/zxing/NotFoundException;
        //  34     42     208    217    Lcom/google/zxing/FormatException;
        //  49     56     197    208    Lcom/google/zxing/NotFoundException;
        //  49     56     208    217    Lcom/google/zxing/FormatException;
        //  63     68     197    208    Lcom/google/zxing/NotFoundException;
        //  63     68     208    217    Lcom/google/zxing/FormatException;
        //  75     80     197    208    Lcom/google/zxing/NotFoundException;
        //  75     80     208    217    Lcom/google/zxing/FormatException;
        //  87     96     197    208    Lcom/google/zxing/NotFoundException;
        //  87     96     208    217    Lcom/google/zxing/FormatException;
        //  114    144    217    218    Lcom/google/zxing/NotFoundException;
        //  114    144    301    305    Lcom/google/zxing/FormatException;
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

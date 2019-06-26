// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.multi.qrcode.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.detector.Detector;

public final class MultiDetector extends Detector
{
    private static final DetectorResult[] EMPTY_DETECTOR_RESULTS;
    
    static {
        EMPTY_DETECTOR_RESULTS = new DetectorResult[0];
    }
    
    public MultiDetector(final BitMatrix bitMatrix) {
        super(bitMatrix);
    }
    
    public DetectorResult[] detectMulti(final Map<DecodeHintType, ?> p0) throws NotFoundException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/google/zxing/multi/qrcode/detector/MultiDetector.getImage:()Lcom/google/zxing/common/BitMatrix;
        //     4: astore_2       
        //     5: aload_1        
        //     6: ifnonnull       34
        //     9: aconst_null    
        //    10: astore_3       
        //    11: new             Lcom/google/zxing/multi/qrcode/detector/MultiFinderPatternFinder;
        //    14: dup            
        //    15: aload_2        
        //    16: aload_3        
        //    17: invokespecial   com/google/zxing/multi/qrcode/detector/MultiFinderPatternFinder.<init>:(Lcom/google/zxing/common/BitMatrix;Lcom/google/zxing/ResultPointCallback;)V
        //    20: aload_1        
        //    21: invokevirtual   com/google/zxing/multi/qrcode/detector/MultiFinderPatternFinder.findMulti:(Ljava/util/Map;)[Lcom/google/zxing/qrcode/detector/FinderPatternInfo;
        //    24: astore_1       
        //    25: aload_1        
        //    26: arraylength    
        //    27: ifne            50
        //    30: invokestatic    com/google/zxing/NotFoundException.getNotFoundInstance:()Lcom/google/zxing/NotFoundException;
        //    33: athrow         
        //    34: aload_1        
        //    35: getstatic       com/google/zxing/DecodeHintType.NEED_RESULT_POINT_CALLBACK:Lcom/google/zxing/DecodeHintType;
        //    38: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    43: checkcast       Lcom/google/zxing/ResultPointCallback;
        //    46: astore_3       
        //    47: goto            11
        //    50: new             Ljava/util/ArrayList;
        //    53: dup            
        //    54: invokespecial   java/util/ArrayList.<init>:()V
        //    57: astore_3       
        //    58: aload_1        
        //    59: arraylength    
        //    60: istore          4
        //    62: iconst_0       
        //    63: istore          5
        //    65: iload           5
        //    67: iload           4
        //    69: if_icmpge       95
        //    72: aload_1        
        //    73: iload           5
        //    75: aaload         
        //    76: astore_2       
        //    77: aload_3        
        //    78: aload_0        
        //    79: aload_2        
        //    80: invokevirtual   com/google/zxing/multi/qrcode/detector/MultiDetector.processFinderPatternInfo:(Lcom/google/zxing/qrcode/detector/FinderPatternInfo;)Lcom/google/zxing/common/DetectorResult;
        //    83: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    88: pop            
        //    89: iinc            5, 1
        //    92: goto            65
        //    95: aload_3        
        //    96: invokeinterface java/util/List.isEmpty:()Z
        //   101: ifeq            110
        //   104: getstatic       com/google/zxing/multi/qrcode/detector/MultiDetector.EMPTY_DETECTOR_RESULTS:[Lcom/google/zxing/common/DetectorResult;
        //   107: astore_1       
        //   108: aload_1        
        //   109: areturn        
        //   110: aload_3        
        //   111: aload_3        
        //   112: invokeinterface java/util/List.size:()I
        //   117: anewarray       Lcom/google/zxing/common/DetectorResult;
        //   120: invokeinterface java/util/List.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   125: checkcast       [Lcom/google/zxing/common/DetectorResult;
        //   128: astore_1       
        //   129: goto            108
        //   132: astore_2       
        //   133: goto            89
        //    Exceptions:
        //  throws com.google.zxing.NotFoundException
        //    Signature:
        //  (Ljava/util/Map<Lcom/google/zxing/DecodeHintType;*>;)[Lcom/google/zxing/common/DetectorResult;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  77     89     132    136    Lcom/google/zxing/ReaderException;
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
}

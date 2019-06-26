// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.aztec.detector;

import com.google.zxing.aztec.AztecDetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

public final class Detector
{
    private static final int[] EXPECTED_CORNER_BITS;
    private boolean compact;
    private final BitMatrix image;
    private int nbCenterLayers;
    private int nbDataBlocks;
    private int nbLayers;
    private int shift;
    
    static {
        EXPECTED_CORNER_BITS = new int[] { 3808, 476, 2107, 1799 };
    }
    
    public Detector(final BitMatrix image) {
        this.image = image;
    }
    
    private static float distance(final ResultPoint resultPoint, final ResultPoint resultPoint2) {
        return MathUtils.distance(resultPoint.getX(), resultPoint.getY(), resultPoint2.getX(), resultPoint2.getY());
    }
    
    private static float distance(final Point point, final Point point2) {
        return MathUtils.distance(point.getX(), point.getY(), point2.getX(), point2.getY());
    }
    
    private static ResultPoint[] expandSquare(final ResultPoint[] array, float n, float n2) {
        n = n2 / (2.0f * n);
        final float n3 = array[0].getX() - array[2].getX();
        n2 = array[0].getY() - array[2].getY();
        final float n4 = (array[0].getX() + array[2].getX()) / 2.0f;
        final float n5 = (array[0].getY() + array[2].getY()) / 2.0f;
        final ResultPoint resultPoint = new ResultPoint(n * n3 + n4, n * n2 + n5);
        final ResultPoint resultPoint2 = new ResultPoint(n4 - n * n3, n5 - n * n2);
        final float n6 = array[1].getX() - array[3].getX();
        n2 = array[1].getY() - array[3].getY();
        final float n7 = (array[1].getX() + array[3].getX()) / 2.0f;
        final float n8 = (array[1].getY() + array[3].getY()) / 2.0f;
        return new ResultPoint[] { resultPoint, new ResultPoint(n * n6 + n7, n * n2 + n8), resultPoint2, new ResultPoint(n7 - n * n6, n8 - n * n2) };
    }
    
    private void extractParameters(final ResultPoint[] array) throws NotFoundException {
        if (!this.isValid(array[0]) || !this.isValid(array[1]) || !this.isValid(array[2]) || !this.isValid(array[3])) {
            throw NotFoundException.getNotFoundInstance();
        }
        final int n = this.nbCenterLayers * 2;
        final int[] array2 = { this.sampleLine(array[0], array[1], n), this.sampleLine(array[1], array[2], n), this.sampleLine(array[2], array[3], n), this.sampleLine(array[3], array[0], n) };
        this.shift = getRotation(array2, n);
        long n2 = 0L;
        for (int i = 0; i < 4; ++i) {
            final int n3 = array2[(this.shift + i) % 4];
            if (this.compact) {
                n2 = (n2 << 7) + (n3 >> 1 & 0x7F);
            }
            else {
                n2 = (n2 << 10) + ((n3 >> 2 & 0x3E0) + (n3 >> 1 & 0x1F));
            }
        }
        final int correctedParameterData = getCorrectedParameterData(n2, this.compact);
        if (this.compact) {
            this.nbLayers = (correctedParameterData >> 6) + 1;
            this.nbDataBlocks = (correctedParameterData & 0x3F) + 1;
        }
        else {
            this.nbLayers = (correctedParameterData >> 11) + 1;
            this.nbDataBlocks = (correctedParameterData & 0x7FF) + 1;
        }
    }
    
    private ResultPoint[] getBullsEyeCorners(Point point) throws NotFoundException {
        Point point2 = point;
        Point point3 = point;
        Point point4 = point;
        boolean b = true;
        this.nbCenterLayers = 1;
        while (this.nbCenterLayers < 9) {
            final Point firstDifferent = this.getFirstDifferent(point2, b, 1, -1);
            final Point firstDifferent2 = this.getFirstDifferent(point3, b, 1, 1);
            final Point firstDifferent3 = this.getFirstDifferent(point4, b, -1, 1);
            final Point firstDifferent4 = this.getFirstDifferent(point, b, -1, -1);
            if (this.nbCenterLayers > 2) {
                final float n = distance(firstDifferent4, firstDifferent) * this.nbCenterLayers / (distance(point, point2) * (this.nbCenterLayers + 2));
                if (n < 0.75 || n > 1.25 || !this.isWhiteOrBlackRectangle(firstDifferent, firstDifferent2, firstDifferent3, firstDifferent4)) {
                    break;
                }
            }
            point2 = firstDifferent;
            point3 = firstDifferent2;
            point4 = firstDifferent3;
            point = firstDifferent4;
            b = !b;
            ++this.nbCenterLayers;
        }
        if (this.nbCenterLayers != 5 && this.nbCenterLayers != 7) {
            throw NotFoundException.getNotFoundInstance();
        }
        this.compact = (this.nbCenterLayers == 5);
        return expandSquare(new ResultPoint[] { new ResultPoint(point2.getX() + 0.5f, point2.getY() - 0.5f), new ResultPoint(point3.getX() + 0.5f, point3.getY() + 0.5f), new ResultPoint(point4.getX() - 0.5f, point4.getY() + 0.5f), new ResultPoint(point.getX() - 0.5f, point.getY() - 0.5f) }, (float)(this.nbCenterLayers * 2 - 3), (float)(this.nbCenterLayers * 2));
    }
    
    private int getColor(final Point point, final Point point2) {
        final float distance = distance(point, point2);
        final float n = (point2.getX() - point.getX()) / distance;
        final float n2 = (point2.getY() - point.getY()) / distance;
        int n3 = 0;
        float n4 = (float)point.getX();
        float n5 = (float)point.getY();
        final boolean value = this.image.get(point.getX(), point.getY());
        int n7;
        for (int n6 = (int)Math.ceil(distance), i = 0; i < n6; ++i, n3 = n7) {
            n4 += n;
            n5 += n2;
            n7 = n3;
            if (this.image.get(MathUtils.round(n4), MathUtils.round(n5)) != value) {
                n7 = n3 + 1;
            }
        }
        final float n8 = n3 / distance;
        int n9;
        if (n8 > 0.1f && n8 < 0.9f) {
            n9 = 0;
        }
        else {
            int n10;
            if (n8 <= 0.1f) {
                n10 = 1;
            }
            else {
                n10 = 0;
            }
            if (n10 == (value ? 1 : 0)) {
                n9 = 1;
            }
            else {
                n9 = -1;
            }
        }
        return n9;
    }
    
    private static int getCorrectedParameterData(long n, final boolean b) throws NotFoundException {
        int n2;
        int n3;
        if (b) {
            n2 = 7;
            n3 = 2;
        }
        else {
            n2 = 10;
            n3 = 4;
        }
        final int[] array = new int[n2];
        for (int i = n2 - 1; i >= 0; --i) {
            array[i] = ((int)n & 0xF);
            n >>= 4;
        }
        int n4;
        try {
            new ReedSolomonDecoder(GenericGF.AZTEC_PARAM).decode(array, n2 - n3);
            n4 = 0;
            for (int j = 0; j < n3; ++j) {
                n4 = (n4 << 4) + array[j];
            }
        }
        catch (ReedSolomonException ex) {
            throw NotFoundException.getNotFoundInstance();
        }
        return n4;
    }
    
    private int getDimension() {
        int n;
        if (this.compact) {
            n = this.nbLayers * 4 + 11;
        }
        else if (this.nbLayers <= 4) {
            n = this.nbLayers * 4 + 15;
        }
        else {
            n = this.nbLayers * 4 + ((this.nbLayers - 4) / 8 + 1) * 2 + 15;
        }
        return n;
    }
    
    private Point getFirstDifferent(final Point point, final boolean b, int n, final int n2) {
        int n3;
        int n4;
        for (n3 = point.getX() + n, n4 = point.getY() + n2; this.isValid(n3, n4) && this.image.get(n3, n4) == b; n3 += n, n4 += n2) {}
        final int n5 = n3 - n;
        int n6;
        int n7;
        for (n6 = n4 - n2, n7 = n5; this.isValid(n7, n6) && this.image.get(n7, n6) == b; n7 += n) {}
        int n8;
        for (n8 = n7 - n, n = n6; this.isValid(n8, n) && this.image.get(n8, n) == b; n += n2) {}
        return new Point(n8, n - n2);
    }
    
    private Point getMatrixCenter() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_1       
        //     4: aload_1        
        //     5: aload_0        
        //     6: getfield        com/google/zxing/aztec/detector/Detector.image:Lcom/google/zxing/common/BitMatrix;
        //     9: invokespecial   com/google/zxing/common/detector/WhiteRectangleDetector.<init>:(Lcom/google/zxing/common/BitMatrix;)V
        //    12: aload_1        
        //    13: invokevirtual   com/google/zxing/common/detector/WhiteRectangleDetector.detect:()[Lcom/google/zxing/ResultPoint;
        //    16: astore_2       
        //    17: aload_2        
        //    18: iconst_0       
        //    19: aaload         
        //    20: astore_3       
        //    21: aload_2        
        //    22: iconst_1       
        //    23: aaload         
        //    24: astore          4
        //    26: aload_2        
        //    27: iconst_2       
        //    28: aaload         
        //    29: astore_1       
        //    30: aload_2        
        //    31: iconst_3       
        //    32: aaload         
        //    33: astore_2       
        //    34: aload_3        
        //    35: invokevirtual   com/google/zxing/ResultPoint.getX:()F
        //    38: aload_2        
        //    39: invokevirtual   com/google/zxing/ResultPoint.getX:()F
        //    42: fadd           
        //    43: aload           4
        //    45: invokevirtual   com/google/zxing/ResultPoint.getX:()F
        //    48: fadd           
        //    49: aload_1        
        //    50: invokevirtual   com/google/zxing/ResultPoint.getX:()F
        //    53: fadd           
        //    54: ldc             4.0
        //    56: fdiv           
        //    57: invokestatic    com/google/zxing/common/detector/MathUtils.round:(F)I
        //    60: istore          5
        //    62: aload_3        
        //    63: invokevirtual   com/google/zxing/ResultPoint.getY:()F
        //    66: aload_2        
        //    67: invokevirtual   com/google/zxing/ResultPoint.getY:()F
        //    70: fadd           
        //    71: aload           4
        //    73: invokevirtual   com/google/zxing/ResultPoint.getY:()F
        //    76: fadd           
        //    77: aload_1        
        //    78: invokevirtual   com/google/zxing/ResultPoint.getY:()F
        //    81: fadd           
        //    82: ldc             4.0
        //    84: fdiv           
        //    85: invokestatic    com/google/zxing/common/detector/MathUtils.round:(F)I
        //    88: istore          6
        //    90: new             Lcom/google/zxing/common/detector/WhiteRectangleDetector;
        //    93: astore_1       
        //    94: aload_1        
        //    95: aload_0        
        //    96: getfield        com/google/zxing/aztec/detector/Detector.image:Lcom/google/zxing/common/BitMatrix;
        //    99: bipush          15
        //   101: iload           5
        //   103: iload           6
        //   105: invokespecial   com/google/zxing/common/detector/WhiteRectangleDetector.<init>:(Lcom/google/zxing/common/BitMatrix;III)V
        //   108: aload_1        
        //   109: invokevirtual   com/google/zxing/common/detector/WhiteRectangleDetector.detect:()[Lcom/google/zxing/ResultPoint;
        //   112: astore_2       
        //   113: aload_2        
        //   114: iconst_0       
        //   115: aaload         
        //   116: astore_3       
        //   117: aload_2        
        //   118: iconst_1       
        //   119: aaload         
        //   120: astore          4
        //   122: aload_2        
        //   123: iconst_2       
        //   124: aaload         
        //   125: astore_1       
        //   126: aload_2        
        //   127: iconst_3       
        //   128: aaload         
        //   129: astore_2       
        //   130: new             Lcom/google/zxing/aztec/detector/Detector$Point;
        //   133: dup            
        //   134: aload_3        
        //   135: invokevirtual   com/google/zxing/ResultPoint.getX:()F
        //   138: aload_2        
        //   139: invokevirtual   com/google/zxing/ResultPoint.getX:()F
        //   142: fadd           
        //   143: aload           4
        //   145: invokevirtual   com/google/zxing/ResultPoint.getX:()F
        //   148: fadd           
        //   149: aload_1        
        //   150: invokevirtual   com/google/zxing/ResultPoint.getX:()F
        //   153: fadd           
        //   154: ldc             4.0
        //   156: fdiv           
        //   157: invokestatic    com/google/zxing/common/detector/MathUtils.round:(F)I
        //   160: aload_3        
        //   161: invokevirtual   com/google/zxing/ResultPoint.getY:()F
        //   164: aload_2        
        //   165: invokevirtual   com/google/zxing/ResultPoint.getY:()F
        //   168: fadd           
        //   169: aload           4
        //   171: invokevirtual   com/google/zxing/ResultPoint.getY:()F
        //   174: fadd           
        //   175: aload_1        
        //   176: invokevirtual   com/google/zxing/ResultPoint.getY:()F
        //   179: fadd           
        //   180: ldc             4.0
        //   182: fdiv           
        //   183: invokestatic    com/google/zxing/common/detector/MathUtils.round:(F)I
        //   186: invokespecial   com/google/zxing/aztec/detector/Detector$Point.<init>:(II)V
        //   189: areturn        
        //   190: astore_1       
        //   191: aload_0        
        //   192: getfield        com/google/zxing/aztec/detector/Detector.image:Lcom/google/zxing/common/BitMatrix;
        //   195: invokevirtual   com/google/zxing/common/BitMatrix.getWidth:()I
        //   198: iconst_2       
        //   199: idiv           
        //   200: istore          6
        //   202: aload_0        
        //   203: getfield        com/google/zxing/aztec/detector/Detector.image:Lcom/google/zxing/common/BitMatrix;
        //   206: invokevirtual   com/google/zxing/common/BitMatrix.getHeight:()I
        //   209: iconst_2       
        //   210: idiv           
        //   211: istore          5
        //   213: aload_0        
        //   214: new             Lcom/google/zxing/aztec/detector/Detector$Point;
        //   217: dup            
        //   218: iload           6
        //   220: bipush          7
        //   222: iadd           
        //   223: iload           5
        //   225: bipush          7
        //   227: isub           
        //   228: invokespecial   com/google/zxing/aztec/detector/Detector$Point.<init>:(II)V
        //   231: iconst_0       
        //   232: iconst_1       
        //   233: iconst_m1      
        //   234: invokespecial   com/google/zxing/aztec/detector/Detector.getFirstDifferent:(Lcom/google/zxing/aztec/detector/Detector$Point;ZII)Lcom/google/zxing/aztec/detector/Detector$Point;
        //   237: invokevirtual   com/google/zxing/aztec/detector/Detector$Point.toResultPoint:()Lcom/google/zxing/ResultPoint;
        //   240: astore_3       
        //   241: aload_0        
        //   242: new             Lcom/google/zxing/aztec/detector/Detector$Point;
        //   245: dup            
        //   246: iload           6
        //   248: bipush          7
        //   250: iadd           
        //   251: iload           5
        //   253: bipush          7
        //   255: iadd           
        //   256: invokespecial   com/google/zxing/aztec/detector/Detector$Point.<init>:(II)V
        //   259: iconst_0       
        //   260: iconst_1       
        //   261: iconst_1       
        //   262: invokespecial   com/google/zxing/aztec/detector/Detector.getFirstDifferent:(Lcom/google/zxing/aztec/detector/Detector$Point;ZII)Lcom/google/zxing/aztec/detector/Detector$Point;
        //   265: invokevirtual   com/google/zxing/aztec/detector/Detector$Point.toResultPoint:()Lcom/google/zxing/ResultPoint;
        //   268: astore          4
        //   270: aload_0        
        //   271: new             Lcom/google/zxing/aztec/detector/Detector$Point;
        //   274: dup            
        //   275: iload           6
        //   277: bipush          7
        //   279: isub           
        //   280: iload           5
        //   282: bipush          7
        //   284: iadd           
        //   285: invokespecial   com/google/zxing/aztec/detector/Detector$Point.<init>:(II)V
        //   288: iconst_0       
        //   289: iconst_m1      
        //   290: iconst_1       
        //   291: invokespecial   com/google/zxing/aztec/detector/Detector.getFirstDifferent:(Lcom/google/zxing/aztec/detector/Detector$Point;ZII)Lcom/google/zxing/aztec/detector/Detector$Point;
        //   294: invokevirtual   com/google/zxing/aztec/detector/Detector$Point.toResultPoint:()Lcom/google/zxing/ResultPoint;
        //   297: astore_1       
        //   298: aload_0        
        //   299: new             Lcom/google/zxing/aztec/detector/Detector$Point;
        //   302: dup            
        //   303: iload           6
        //   305: bipush          7
        //   307: isub           
        //   308: iload           5
        //   310: bipush          7
        //   312: isub           
        //   313: invokespecial   com/google/zxing/aztec/detector/Detector$Point.<init>:(II)V
        //   316: iconst_0       
        //   317: iconst_m1      
        //   318: iconst_m1      
        //   319: invokespecial   com/google/zxing/aztec/detector/Detector.getFirstDifferent:(Lcom/google/zxing/aztec/detector/Detector$Point;ZII)Lcom/google/zxing/aztec/detector/Detector$Point;
        //   322: invokevirtual   com/google/zxing/aztec/detector/Detector$Point.toResultPoint:()Lcom/google/zxing/ResultPoint;
        //   325: astore_2       
        //   326: goto            34
        //   329: astore_1       
        //   330: aload_0        
        //   331: new             Lcom/google/zxing/aztec/detector/Detector$Point;
        //   334: dup            
        //   335: iload           5
        //   337: bipush          7
        //   339: iadd           
        //   340: iload           6
        //   342: bipush          7
        //   344: isub           
        //   345: invokespecial   com/google/zxing/aztec/detector/Detector$Point.<init>:(II)V
        //   348: iconst_0       
        //   349: iconst_1       
        //   350: iconst_m1      
        //   351: invokespecial   com/google/zxing/aztec/detector/Detector.getFirstDifferent:(Lcom/google/zxing/aztec/detector/Detector$Point;ZII)Lcom/google/zxing/aztec/detector/Detector$Point;
        //   354: invokevirtual   com/google/zxing/aztec/detector/Detector$Point.toResultPoint:()Lcom/google/zxing/ResultPoint;
        //   357: astore_3       
        //   358: aload_0        
        //   359: new             Lcom/google/zxing/aztec/detector/Detector$Point;
        //   362: dup            
        //   363: iload           5
        //   365: bipush          7
        //   367: iadd           
        //   368: iload           6
        //   370: bipush          7
        //   372: iadd           
        //   373: invokespecial   com/google/zxing/aztec/detector/Detector$Point.<init>:(II)V
        //   376: iconst_0       
        //   377: iconst_1       
        //   378: iconst_1       
        //   379: invokespecial   com/google/zxing/aztec/detector/Detector.getFirstDifferent:(Lcom/google/zxing/aztec/detector/Detector$Point;ZII)Lcom/google/zxing/aztec/detector/Detector$Point;
        //   382: invokevirtual   com/google/zxing/aztec/detector/Detector$Point.toResultPoint:()Lcom/google/zxing/ResultPoint;
        //   385: astore          4
        //   387: aload_0        
        //   388: new             Lcom/google/zxing/aztec/detector/Detector$Point;
        //   391: dup            
        //   392: iload           5
        //   394: bipush          7
        //   396: isub           
        //   397: iload           6
        //   399: bipush          7
        //   401: iadd           
        //   402: invokespecial   com/google/zxing/aztec/detector/Detector$Point.<init>:(II)V
        //   405: iconst_0       
        //   406: iconst_m1      
        //   407: iconst_1       
        //   408: invokespecial   com/google/zxing/aztec/detector/Detector.getFirstDifferent:(Lcom/google/zxing/aztec/detector/Detector$Point;ZII)Lcom/google/zxing/aztec/detector/Detector$Point;
        //   411: invokevirtual   com/google/zxing/aztec/detector/Detector$Point.toResultPoint:()Lcom/google/zxing/ResultPoint;
        //   414: astore_1       
        //   415: aload_0        
        //   416: new             Lcom/google/zxing/aztec/detector/Detector$Point;
        //   419: dup            
        //   420: iload           5
        //   422: bipush          7
        //   424: isub           
        //   425: iload           6
        //   427: bipush          7
        //   429: isub           
        //   430: invokespecial   com/google/zxing/aztec/detector/Detector$Point.<init>:(II)V
        //   433: iconst_0       
        //   434: iconst_m1      
        //   435: iconst_m1      
        //   436: invokespecial   com/google/zxing/aztec/detector/Detector.getFirstDifferent:(Lcom/google/zxing/aztec/detector/Detector$Point;ZII)Lcom/google/zxing/aztec/detector/Detector$Point;
        //   439: invokevirtual   com/google/zxing/aztec/detector/Detector$Point.toResultPoint:()Lcom/google/zxing/ResultPoint;
        //   442: astore_2       
        //   443: goto            130
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  0      17     190    329    Lcom/google/zxing/NotFoundException;
        //  90     113    329    446    Lcom/google/zxing/NotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0130:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    private ResultPoint[] getMatrixCornerPoints(final ResultPoint[] array) {
        return expandSquare(array, (float)(this.nbCenterLayers * 2), (float)this.getDimension());
    }
    
    private static int getRotation(final int[] array, int i) throws NotFoundException {
        int n = 0;
        for (final int n2 : array) {
            n = (n << 3) + ((n2 >> i - 2 << 1) + (n2 & 0x1));
        }
        for (i = 0; i < 4; ++i) {
            if (Integer.bitCount(Detector.EXPECTED_CORNER_BITS[i] ^ ((n & 0x1) << 11) + (n >> 1)) <= 2) {
                return i;
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }
    
    private boolean isValid(final int n, final int n2) {
        return n >= 0 && n < this.image.getWidth() && n2 > 0 && n2 < this.image.getHeight();
    }
    
    private boolean isValid(final ResultPoint resultPoint) {
        return this.isValid(MathUtils.round(resultPoint.getX()), MathUtils.round(resultPoint.getY()));
    }
    
    private boolean isWhiteOrBlackRectangle(Point point, Point point2, Point point3, Point point4) {
        final boolean b = false;
        point = new Point(point.getX() - 3, point.getY() + 3);
        point2 = new Point(point2.getX() - 3, point2.getY() - 3);
        point3 = new Point(point3.getX() + 3, point3.getY() - 3);
        point4 = new Point(point4.getX() + 3, point4.getY() + 3);
        final int color = this.getColor(point4, point);
        boolean b2;
        if (color == 0) {
            b2 = b;
        }
        else {
            b2 = b;
            if (this.getColor(point, point2) == color) {
                b2 = b;
                if (this.getColor(point2, point3) == color) {
                    b2 = b;
                    if (this.getColor(point3, point4) == color) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    private BitMatrix sampleGrid(final BitMatrix bitMatrix, final ResultPoint resultPoint, final ResultPoint resultPoint2, final ResultPoint resultPoint3, final ResultPoint resultPoint4) throws NotFoundException {
        final GridSampler instance = GridSampler.getInstance();
        final int dimension = this.getDimension();
        final float n = dimension / 2.0f - this.nbCenterLayers;
        final float n2 = dimension / 2.0f + this.nbCenterLayers;
        return instance.sampleGrid(bitMatrix, dimension, dimension, n, n, n2, n, n2, n2, n, n2, resultPoint.getX(), resultPoint.getY(), resultPoint2.getX(), resultPoint2.getY(), resultPoint3.getX(), resultPoint3.getY(), resultPoint4.getX(), resultPoint4.getY());
    }
    
    private int sampleLine(final ResultPoint resultPoint, final ResultPoint resultPoint2, final int n) {
        int n2 = 0;
        final float distance = distance(resultPoint, resultPoint2);
        final float n3 = distance / n;
        final float x = resultPoint.getX();
        final float y = resultPoint.getY();
        final float n4 = (resultPoint2.getX() - resultPoint.getX()) * n3 / distance;
        final float n5 = (resultPoint2.getY() - resultPoint.getY()) * n3 / distance;
        int n6;
        for (int i = 0; i < n; ++i, n2 = n6) {
            n6 = n2;
            if (this.image.get(MathUtils.round(i * n4 + x), MathUtils.round(i * n5 + y))) {
                n6 = (n2 | 1 << n - i - 1);
            }
        }
        return n2;
    }
    
    public AztecDetectorResult detect() throws NotFoundException {
        return this.detect(false);
    }
    
    public AztecDetectorResult detect(final boolean b) throws NotFoundException {
        final ResultPoint[] bullsEyeCorners = this.getBullsEyeCorners(this.getMatrixCenter());
        if (b) {
            final ResultPoint resultPoint = bullsEyeCorners[0];
            bullsEyeCorners[0] = bullsEyeCorners[2];
            bullsEyeCorners[2] = resultPoint;
        }
        this.extractParameters(bullsEyeCorners);
        return new AztecDetectorResult(this.sampleGrid(this.image, bullsEyeCorners[this.shift % 4], bullsEyeCorners[(this.shift + 1) % 4], bullsEyeCorners[(this.shift + 2) % 4], bullsEyeCorners[(this.shift + 3) % 4]), this.getMatrixCornerPoints(bullsEyeCorners), this.compact, this.nbDataBlocks, this.nbLayers);
    }
    
    static final class Point
    {
        private final int x;
        private final int y;
        
        Point(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
        
        int getX() {
            return this.x;
        }
        
        int getY() {
            return this.y;
        }
        
        ResultPoint toResultPoint() {
            return new ResultPoint((float)this.getX(), (float)this.getY());
        }
        
        @Override
        public String toString() {
            return "<" + this.x + ' ' + this.y + '>';
        }
    }
}

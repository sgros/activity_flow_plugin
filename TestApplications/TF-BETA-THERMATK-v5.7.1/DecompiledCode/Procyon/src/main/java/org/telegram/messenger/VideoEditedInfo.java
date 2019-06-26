// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.util.Locale;
import org.telegram.tgnet.TLRPC;

public class VideoEditedInfo
{
    public int bitrate;
    public TLRPC.InputEncryptedFile encryptedFile;
    public long endTime;
    public long estimatedDuration;
    public long estimatedSize;
    public TLRPC.InputFile file;
    public int framerate;
    public byte[] iv;
    public byte[] key;
    public boolean muted;
    public int originalHeight;
    public String originalPath;
    public int originalWidth;
    public int resultHeight;
    public int resultWidth;
    public int rotationValue;
    public boolean roundVideo;
    public long startTime;
    
    public VideoEditedInfo() {
        this.framerate = 24;
    }
    
    public String getString() {
        return String.format(Locale.US, "-1_%d_%d_%d_%d_%d_%d_%d_%d_%d_%s", this.startTime, this.endTime, this.rotationValue, this.originalWidth, this.originalHeight, this.bitrate, this.resultWidth, this.resultHeight, this.framerate, this.originalPath);
    }
    
    public boolean needConvert() {
        final boolean roundVideo = this.roundVideo;
        if (roundVideo) {
            if (roundVideo) {
                if (this.startTime > 0L) {
                    return true;
                }
                final long endTime = this.endTime;
                if (endTime != -1L && endTime != this.estimatedDuration) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public boolean parseString(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/lang/String.length:()I
        //     4: bipush          6
        //     6: if_icmpge       11
        //     9: iconst_0       
        //    10: ireturn        
        //    11: aload_1        
        //    12: ldc             "_"
        //    14: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //    17: astore_1       
        //    18: aload_1        
        //    19: arraylength    
        //    20: istore_2       
        //    21: bipush          10
        //    23: istore_3       
        //    24: iload_2        
        //    25: bipush          10
        //    27: if_icmplt       234
        //    30: aload_0        
        //    31: aload_1        
        //    32: iconst_1       
        //    33: aaload         
        //    34: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //    37: putfield        org/telegram/messenger/VideoEditedInfo.startTime:J
        //    40: aload_0        
        //    41: aload_1        
        //    42: iconst_2       
        //    43: aaload         
        //    44: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //    47: putfield        org/telegram/messenger/VideoEditedInfo.endTime:J
        //    50: aload_0        
        //    51: aload_1        
        //    52: iconst_3       
        //    53: aaload         
        //    54: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    57: putfield        org/telegram/messenger/VideoEditedInfo.rotationValue:I
        //    60: aload_0        
        //    61: aload_1        
        //    62: iconst_4       
        //    63: aaload         
        //    64: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    67: putfield        org/telegram/messenger/VideoEditedInfo.originalWidth:I
        //    70: aload_0        
        //    71: aload_1        
        //    72: iconst_5       
        //    73: aaload         
        //    74: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    77: putfield        org/telegram/messenger/VideoEditedInfo.originalHeight:I
        //    80: aload_0        
        //    81: aload_1        
        //    82: bipush          6
        //    84: aaload         
        //    85: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    88: putfield        org/telegram/messenger/VideoEditedInfo.bitrate:I
        //    91: aload_0        
        //    92: aload_1        
        //    93: bipush          7
        //    95: aaload         
        //    96: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    99: putfield        org/telegram/messenger/VideoEditedInfo.resultWidth:I
        //   102: aload_0        
        //   103: aload_1        
        //   104: bipush          8
        //   106: aaload         
        //   107: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   110: putfield        org/telegram/messenger/VideoEditedInfo.resultHeight:I
        //   113: aload_1        
        //   114: arraylength    
        //   115: istore_2       
        //   116: iload_2        
        //   117: bipush          11
        //   119: if_icmplt       133
        //   122: aload_0        
        //   123: aload_1        
        //   124: bipush          9
        //   126: aaload         
        //   127: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   130: putfield        org/telegram/messenger/VideoEditedInfo.framerate:I
        //   133: aload_0        
        //   134: getfield        org/telegram/messenger/VideoEditedInfo.framerate:I
        //   137: ifle            150
        //   140: aload_0        
        //   141: getfield        org/telegram/messenger/VideoEditedInfo.framerate:I
        //   144: sipush          400
        //   147: if_icmple       159
        //   150: aload_0        
        //   151: bipush          25
        //   153: putfield        org/telegram/messenger/VideoEditedInfo.framerate:I
        //   156: bipush          9
        //   158: istore_3       
        //   159: iload_3        
        //   160: aload_1        
        //   161: arraylength    
        //   162: if_icmpge       234
        //   165: aload_0        
        //   166: getfield        org/telegram/messenger/VideoEditedInfo.originalPath:Ljava/lang/String;
        //   169: ifnonnull       182
        //   172: aload_0        
        //   173: aload_1        
        //   174: iload_3        
        //   175: aaload         
        //   176: putfield        org/telegram/messenger/VideoEditedInfo.originalPath:Ljava/lang/String;
        //   179: goto            228
        //   182: new             Ljava/lang/StringBuilder;
        //   185: astore          4
        //   187: aload           4
        //   189: invokespecial   java/lang/StringBuilder.<init>:()V
        //   192: aload           4
        //   194: aload_0        
        //   195: getfield        org/telegram/messenger/VideoEditedInfo.originalPath:Ljava/lang/String;
        //   198: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   201: pop            
        //   202: aload           4
        //   204: ldc             "_"
        //   206: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   209: pop            
        //   210: aload           4
        //   212: aload_1        
        //   213: iload_3        
        //   214: aaload         
        //   215: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   218: pop            
        //   219: aload_0        
        //   220: aload           4
        //   222: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   225: putfield        org/telegram/messenger/VideoEditedInfo.originalPath:Ljava/lang/String;
        //   228: iinc            3, 1
        //   231: goto            159
        //   234: iconst_1       
        //   235: ireturn        
        //   236: astore_1       
        //   237: aload_1        
        //   238: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   241: iconst_0       
        //   242: ireturn        
        //   243: astore          4
        //   245: goto            133
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  11     21     236    243    Ljava/lang/Exception;
        //  30     116    236    243    Ljava/lang/Exception;
        //  122    133    243    248    Ljava/lang/Exception;
        //  133    140    236    243    Ljava/lang/Exception;
        //  140    150    236    243    Ljava/lang/Exception;
        //  150    156    236    243    Ljava/lang/Exception;
        //  159    179    236    243    Ljava/lang/Exception;
        //  182    228    236    243    Ljava/lang/Exception;
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

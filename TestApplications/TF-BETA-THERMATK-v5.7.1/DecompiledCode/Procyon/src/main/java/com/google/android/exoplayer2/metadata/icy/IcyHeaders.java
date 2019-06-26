// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.icy;

import java.util.List;
import java.util.Map;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.google.android.exoplayer2.metadata.Metadata;

public final class IcyHeaders implements Entry
{
    public static final Parcelable$Creator<IcyHeaders> CREATOR;
    public final int bitrate;
    public final String genre;
    public final boolean isPublic;
    public final int metadataInterval;
    public final String name;
    public final String url;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<IcyHeaders>() {
            public IcyHeaders createFromParcel(final Parcel parcel) {
                return new IcyHeaders(parcel);
            }
            
            public IcyHeaders[] newArray(final int n) {
                return new IcyHeaders[n];
            }
        };
    }
    
    public IcyHeaders(final int bitrate, final String genre, final String name, final String url, final boolean isPublic, final int metadataInterval) {
        Assertions.checkArgument(metadataInterval == -1 || metadataInterval > 0);
        this.bitrate = bitrate;
        this.genre = genre;
        this.name = name;
        this.url = url;
        this.isPublic = isPublic;
        this.metadataInterval = metadataInterval;
    }
    
    IcyHeaders(final Parcel parcel) {
        this.bitrate = parcel.readInt();
        this.genre = parcel.readString();
        this.name = parcel.readString();
        this.url = parcel.readString();
        this.isPublic = Util.readBoolean(parcel);
        this.metadataInterval = parcel.readInt();
    }
    
    public static IcyHeaders parse(final Map<String, List<String>> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "icy-br"
        //     3: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //     8: checkcast       Ljava/util/List;
        //    11: astore_1       
        //    12: iconst_m1      
        //    13: istore_2       
        //    14: aload_1        
        //    15: ifnull          144
        //    18: aload_1        
        //    19: iconst_0       
        //    20: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    25: checkcast       Ljava/lang/String;
        //    28: astore_1       
        //    29: aload_1        
        //    30: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    33: istore_3       
        //    34: iload_3        
        //    35: sipush          1000
        //    38: imul           
        //    39: istore          4
        //    41: iload           4
        //    43: ifle            51
        //    46: iconst_1       
        //    47: istore_3       
        //    48: goto            91
        //    51: new             Ljava/lang/StringBuilder;
        //    54: astore          5
        //    56: aload           5
        //    58: invokespecial   java/lang/StringBuilder.<init>:()V
        //    61: aload           5
        //    63: ldc             "Invalid bitrate: "
        //    65: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    68: pop            
        //    69: aload           5
        //    71: aload_1        
        //    72: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    75: pop            
        //    76: ldc             "IcyHeaders"
        //    78: aload           5
        //    80: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    83: invokestatic    com/google/android/exoplayer2/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)V
        //    86: iconst_0       
        //    87: istore_3       
        //    88: iconst_m1      
        //    89: istore          4
        //    91: iload           4
        //    93: istore          6
        //    95: goto            149
        //    98: astore          5
        //   100: iconst_m1      
        //   101: istore_3       
        //   102: new             Ljava/lang/StringBuilder;
        //   105: dup            
        //   106: invokespecial   java/lang/StringBuilder.<init>:()V
        //   109: astore          5
        //   111: aload           5
        //   113: ldc             "Invalid bitrate header: "
        //   115: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   118: pop            
        //   119: aload           5
        //   121: aload_1        
        //   122: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   125: pop            
        //   126: ldc             "IcyHeaders"
        //   128: aload           5
        //   130: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   133: invokestatic    com/google/android/exoplayer2/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   136: iload_3        
        //   137: istore          6
        //   139: iconst_0       
        //   140: istore_3       
        //   141: goto            149
        //   144: iconst_0       
        //   145: istore_3       
        //   146: iconst_m1      
        //   147: istore          6
        //   149: aload_0        
        //   150: ldc             "icy-genre"
        //   152: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   157: checkcast       Ljava/util/List;
        //   160: astore_1       
        //   161: aload_1        
        //   162: ifnull          181
        //   165: aload_1        
        //   166: iconst_0       
        //   167: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   172: checkcast       Ljava/lang/String;
        //   175: astore_1       
        //   176: iconst_1       
        //   177: istore_3       
        //   178: goto            183
        //   181: aconst_null    
        //   182: astore_1       
        //   183: aload_0        
        //   184: ldc             "icy-name"
        //   186: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   191: checkcast       Ljava/util/List;
        //   194: astore          5
        //   196: aload           5
        //   198: ifnull          219
        //   201: aload           5
        //   203: iconst_0       
        //   204: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   209: checkcast       Ljava/lang/String;
        //   212: astore          5
        //   214: iconst_1       
        //   215: istore_3       
        //   216: goto            222
        //   219: aconst_null    
        //   220: astore          5
        //   222: aload_0        
        //   223: ldc             "icy-url"
        //   225: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   230: checkcast       Ljava/util/List;
        //   233: astore          7
        //   235: aload           7
        //   237: ifnull          258
        //   240: aload           7
        //   242: iconst_0       
        //   243: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   248: checkcast       Ljava/lang/String;
        //   251: astore          7
        //   253: iconst_1       
        //   254: istore_3       
        //   255: goto            261
        //   258: aconst_null    
        //   259: astore          7
        //   261: aload_0        
        //   262: ldc             "icy-pub"
        //   264: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   269: checkcast       Ljava/util/List;
        //   272: astore          8
        //   274: aload           8
        //   276: ifnull          302
        //   279: aload           8
        //   281: iconst_0       
        //   282: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   287: checkcast       Ljava/lang/String;
        //   290: ldc             "1"
        //   292: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   295: istore          9
        //   297: iconst_1       
        //   298: istore_3       
        //   299: goto            305
        //   302: iconst_0       
        //   303: istore          9
        //   305: aload_0        
        //   306: ldc             "icy-metaint"
        //   308: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   313: checkcast       Ljava/util/List;
        //   316: astore_0       
        //   317: aload_0        
        //   318: ifnull          428
        //   321: aload_0        
        //   322: iconst_0       
        //   323: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   328: checkcast       Ljava/lang/String;
        //   331: astore_0       
        //   332: aload_0        
        //   333: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   336: istore          4
        //   338: iload           4
        //   340: ifle            348
        //   343: iconst_1       
        //   344: istore_3       
        //   345: goto            425
        //   348: new             Ljava/lang/StringBuilder;
        //   351: astore          8
        //   353: aload           8
        //   355: invokespecial   java/lang/StringBuilder.<init>:()V
        //   358: aload           8
        //   360: ldc             "Invalid metadata interval: "
        //   362: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   365: pop            
        //   366: aload           8
        //   368: aload_0        
        //   369: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   372: pop            
        //   373: ldc             "IcyHeaders"
        //   375: aload           8
        //   377: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   380: invokestatic    com/google/android/exoplayer2/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   383: iload_2        
        //   384: istore          4
        //   386: goto            425
        //   389: astore          8
        //   391: new             Ljava/lang/StringBuilder;
        //   394: dup            
        //   395: invokespecial   java/lang/StringBuilder.<init>:()V
        //   398: astore          8
        //   400: aload           8
        //   402: ldc             "Invalid metadata interval: "
        //   404: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   407: pop            
        //   408: aload           8
        //   410: aload_0        
        //   411: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   414: pop            
        //   415: ldc             "IcyHeaders"
        //   417: aload           8
        //   419: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   422: invokestatic    com/google/android/exoplayer2/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   425: goto            431
        //   428: iconst_m1      
        //   429: istore          4
        //   431: iload_3        
        //   432: ifeq            457
        //   435: new             Lcom/google/android/exoplayer2/metadata/icy/IcyHeaders;
        //   438: dup            
        //   439: iload           6
        //   441: aload_1        
        //   442: aload           5
        //   444: aload           7
        //   446: iload           9
        //   448: iload           4
        //   450: invokespecial   com/google/android/exoplayer2/metadata/icy/IcyHeaders.<init>:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ZI)V
        //   453: astore_0       
        //   454: goto            459
        //   457: aconst_null    
        //   458: astore_0       
        //   459: aload_0        
        //   460: areturn        
        //   461: astore          5
        //   463: iload           4
        //   465: istore_3       
        //   466: goto            102
        //   469: astore          8
        //   471: iload_2        
        //   472: istore          4
        //   474: goto            391
        //    Signature:
        //  (Ljava/util/Map<Ljava/lang/String;Ljava/util/List<Ljava/lang/String;>;>;)Lcom/google/android/exoplayer2/metadata/icy/IcyHeaders;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  29     34     98     102    Ljava/lang/NumberFormatException;
        //  51     86     461    469    Ljava/lang/NumberFormatException;
        //  332    338    469    477    Ljava/lang/NumberFormatException;
        //  348    383    389    391    Ljava/lang/NumberFormatException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0051:
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
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && IcyHeaders.class == o.getClass()) {
            final IcyHeaders icyHeaders = (IcyHeaders)o;
            if (this.bitrate != icyHeaders.bitrate || !Util.areEqual(this.genre, icyHeaders.genre) || !Util.areEqual(this.name, icyHeaders.name) || !Util.areEqual(this.url, icyHeaders.url) || this.isPublic != icyHeaders.isPublic || this.metadataInterval != icyHeaders.metadataInterval) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int bitrate = this.bitrate;
        final String genre = this.genre;
        int hashCode = 0;
        int hashCode2;
        if (genre != null) {
            hashCode2 = genre.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String name = this.name;
        int hashCode3;
        if (name != null) {
            hashCode3 = name.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String url = this.url;
        if (url != null) {
            hashCode = url.hashCode();
        }
        return (((((527 + bitrate) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode) * 31 + (this.isPublic ? 1 : 0)) * 31 + this.metadataInterval;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IcyHeaders: name=\"");
        sb.append(this.name);
        sb.append("\", genre=\"");
        sb.append(this.genre);
        sb.append("\", bitrate=");
        sb.append(this.bitrate);
        sb.append(", metadataInterval=");
        sb.append(this.metadataInterval);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.bitrate);
        parcel.writeString(this.genre);
        parcel.writeString(this.name);
        parcel.writeString(this.url);
        Util.writeBoolean(parcel, this.isPublic);
        parcel.writeInt(this.metadataInterval);
    }
}

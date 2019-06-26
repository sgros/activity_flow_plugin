// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.dvb;

import android.graphics.Region$Op;
import java.util.ArrayList;
import android.graphics.Bitmap$Config;
import java.util.Collections;
import com.google.android.exoplayer2.text.Cue;
import java.util.List;
import com.google.android.exoplayer2.util.Log;
import android.util.SparseArray;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableBitArray;
import android.graphics.PathEffect;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint$Style;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;

final class DvbParser
{
    private static final byte[] defaultMap2To4;
    private static final byte[] defaultMap2To8;
    private static final byte[] defaultMap4To8;
    private Bitmap bitmap;
    private final Canvas canvas;
    private final ClutDefinition defaultClutDefinition;
    private final DisplayDefinition defaultDisplayDefinition;
    private final Paint defaultPaint;
    private final Paint fillRegionPaint;
    private final SubtitleService subtitleService;
    
    static {
        defaultMap2To4 = new byte[] { 0, 7, 8, 15 };
        defaultMap2To8 = new byte[] { 0, 119, -120, -1 };
        defaultMap4To8 = new byte[] { 0, 17, 34, 51, 68, 85, 102, 119, -120, -103, -86, -69, -52, -35, -18, -1 };
    }
    
    public DvbParser(final int n, final int n2) {
        (this.defaultPaint = new Paint()).setStyle(Paint$Style.FILL_AND_STROKE);
        this.defaultPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.SRC));
        this.defaultPaint.setPathEffect((PathEffect)null);
        (this.fillRegionPaint = new Paint()).setStyle(Paint$Style.FILL);
        this.fillRegionPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_OVER));
        this.fillRegionPaint.setPathEffect((PathEffect)null);
        this.canvas = new Canvas();
        this.defaultDisplayDefinition = new DisplayDefinition(719, 575, 0, 719, 0, 575);
        this.defaultClutDefinition = new ClutDefinition(0, generateDefault2BitClutEntries(), generateDefault4BitClutEntries(), generateDefault8BitClutEntries());
        this.subtitleService = new SubtitleService(n, n2);
    }
    
    private static byte[] buildClutMapTable(final int n, final int n2, final ParsableBitArray parsableBitArray) {
        final byte[] array = new byte[n];
        for (int i = 0; i < n; ++i) {
            array[i] = (byte)parsableBitArray.readBits(n2);
        }
        return array;
    }
    
    private static int[] generateDefault2BitClutEntries() {
        return new int[] { 0, -1, -16777216, -8421505 };
    }
    
    private static int[] generateDefault4BitClutEntries() {
        final int[] array = new int[16];
        array[0] = 0;
        for (int i = 1; i < array.length; ++i) {
            if (i < 8) {
                int n;
                if ((i & 0x1) != 0x0) {
                    n = 255;
                }
                else {
                    n = 0;
                }
                int n2;
                if ((i & 0x2) != 0x0) {
                    n2 = 255;
                }
                else {
                    n2 = 0;
                }
                int n3;
                if ((i & 0x4) != 0x0) {
                    n3 = 255;
                }
                else {
                    n3 = 0;
                }
                array[i] = getColor(255, n, n2, n3);
            }
            else {
                int n4 = 127;
                int n5;
                if ((i & 0x1) != 0x0) {
                    n5 = 127;
                }
                else {
                    n5 = 0;
                }
                int n6;
                if ((i & 0x2) != 0x0) {
                    n6 = 127;
                }
                else {
                    n6 = 0;
                }
                if ((i & 0x4) == 0x0) {
                    n4 = 0;
                }
                array[i] = getColor(255, n5, n6, n4);
            }
        }
        return array;
    }
    
    private static int[] generateDefault8BitClutEntries() {
        final int[] array = new int[256];
        array[0] = 0;
        for (int i = 0; i < array.length; ++i) {
            int n = 255;
            if (i < 8) {
                int n2;
                if ((i & 0x1) != 0x0) {
                    n2 = 255;
                }
                else {
                    n2 = 0;
                }
                int n3;
                if ((i & 0x2) != 0x0) {
                    n3 = 255;
                }
                else {
                    n3 = 0;
                }
                if ((i & 0x4) == 0x0) {
                    n = 0;
                }
                array[i] = getColor(63, n2, n3, n);
            }
            else {
                final int n4 = i & 0x88;
                int n5 = 170;
                int n6 = 85;
                if (n4 != 0) {
                    if (n4 != 8) {
                        int n7 = 43;
                        if (n4 != 128) {
                            if (n4 == 136) {
                                int n8;
                                if ((i & 0x1) != 0x0) {
                                    n8 = 43;
                                }
                                else {
                                    n8 = 0;
                                }
                                int n9;
                                if ((i & 0x10) != 0x0) {
                                    n9 = 85;
                                }
                                else {
                                    n9 = 0;
                                }
                                int n10;
                                if ((i & 0x2) != 0x0) {
                                    n10 = 43;
                                }
                                else {
                                    n10 = 0;
                                }
                                int n11;
                                if ((i & 0x20) != 0x0) {
                                    n11 = 85;
                                }
                                else {
                                    n11 = 0;
                                }
                                if ((i & 0x4) == 0x0) {
                                    n7 = 0;
                                }
                                if ((i & 0x40) == 0x0) {
                                    n6 = 0;
                                }
                                array[i] = getColor(255, n8 + n9, n10 + n11, n7 + n6);
                            }
                        }
                        else {
                            int n12;
                            if ((i & 0x1) != 0x0) {
                                n12 = 43;
                            }
                            else {
                                n12 = 0;
                            }
                            int n13;
                            if ((i & 0x10) != 0x0) {
                                n13 = 85;
                            }
                            else {
                                n13 = 0;
                            }
                            int n14;
                            if ((i & 0x2) != 0x0) {
                                n14 = 43;
                            }
                            else {
                                n14 = 0;
                            }
                            int n15;
                            if ((i & 0x20) != 0x0) {
                                n15 = 85;
                            }
                            else {
                                n15 = 0;
                            }
                            if ((i & 0x4) == 0x0) {
                                n7 = 0;
                            }
                            if ((i & 0x40) == 0x0) {
                                n6 = 0;
                            }
                            array[i] = getColor(255, n12 + 127 + n13, n14 + 127 + n15, n7 + 127 + n6);
                        }
                    }
                    else {
                        int n16;
                        if ((i & 0x1) != 0x0) {
                            n16 = 85;
                        }
                        else {
                            n16 = 0;
                        }
                        int n17;
                        if ((i & 0x10) != 0x0) {
                            n17 = 170;
                        }
                        else {
                            n17 = 0;
                        }
                        int n18;
                        if ((i & 0x2) != 0x0) {
                            n18 = 85;
                        }
                        else {
                            n18 = 0;
                        }
                        int n19;
                        if ((i & 0x20) != 0x0) {
                            n19 = 170;
                        }
                        else {
                            n19 = 0;
                        }
                        if ((i & 0x4) == 0x0) {
                            n6 = 0;
                        }
                        if ((i & 0x40) == 0x0) {
                            n5 = 0;
                        }
                        array[i] = getColor(127, n16 + n17, n18 + n19, n6 + n5);
                    }
                }
                else {
                    int n20;
                    if ((i & 0x1) != 0x0) {
                        n20 = 85;
                    }
                    else {
                        n20 = 0;
                    }
                    int n21;
                    if ((i & 0x10) != 0x0) {
                        n21 = 170;
                    }
                    else {
                        n21 = 0;
                    }
                    int n22;
                    if ((i & 0x2) != 0x0) {
                        n22 = 85;
                    }
                    else {
                        n22 = 0;
                    }
                    int n23;
                    if ((i & 0x20) != 0x0) {
                        n23 = 170;
                    }
                    else {
                        n23 = 0;
                    }
                    if ((i & 0x4) == 0x0) {
                        n6 = 0;
                    }
                    if ((i & 0x40) == 0x0) {
                        n5 = 0;
                    }
                    array[i] = getColor(255, n20 + n21, n22 + n23, n6 + n5);
                }
            }
        }
        return array;
    }
    
    private static int getColor(final int n, final int n2, final int n3, final int n4) {
        return n << 24 | n2 << 16 | n3 << 8 | n4;
    }
    
    private static int paint2BitPixelCodeString(final ParsableBitArray parsableBitArray, final int[] array, final byte[] array2, int n, final int n2, final Paint paint, final Canvas canvas) {
        final int n3 = 0;
        int n4 = n;
        n = n3;
        while (true) {
            int bits = parsableBitArray.readBits(2);
            int n5 = 0;
            int n7 = 0;
            Label_0187: {
                Label_0021: {
                    if (bits == 0) {
                        int n6;
                        if (parsableBitArray.readBit()) {
                            n5 = parsableBitArray.readBits(3) + 3;
                            n6 = parsableBitArray.readBits(2);
                        }
                        else {
                            if (parsableBitArray.readBit()) {
                                bits = 0;
                                break Label_0021;
                            }
                            final int bits2 = parsableBitArray.readBits(2);
                            if (bits2 == 0) {
                                n = 0;
                                n5 = 0;
                                n7 = 1;
                                break Label_0187;
                            }
                            if (bits2 == 1) {
                                n7 = n;
                                n = 0;
                                n5 = 2;
                                break Label_0187;
                            }
                            if (bits2 != 2) {
                                if (bits2 != 3) {
                                    n7 = n;
                                    n = 0;
                                    n5 = 0;
                                    break Label_0187;
                                }
                                n5 = parsableBitArray.readBits(8) + 29;
                                n6 = parsableBitArray.readBits(2);
                            }
                            else {
                                n5 = parsableBitArray.readBits(4) + 12;
                                n6 = parsableBitArray.readBits(2);
                            }
                        }
                        final int n8 = n;
                        n = n6;
                        n7 = n8;
                        break Label_0187;
                    }
                }
                final int n9 = n;
                n5 = 1;
                n = bits;
                n7 = n9;
            }
            if (n5 != 0 && paint != null) {
                int n10 = n;
                if (array2 != null) {
                    n10 = array2[n];
                }
                paint.setColor(array[n10]);
                canvas.drawRect((float)n4, (float)n2, (float)(n4 + n5), (float)(n2 + 1), paint);
            }
            n4 += n5;
            if (n7 != 0) {
                break;
            }
            n = n7;
        }
        return n4;
    }
    
    private static int paint4BitPixelCodeString(final ParsableBitArray parsableBitArray, final int[] array, final byte[] array2, int n, final int n2, final Paint paint, final Canvas canvas) {
        final int n3 = 0;
        int n4 = n;
        n = n3;
        while (true) {
            int bits = parsableBitArray.readBits(4);
            int bits2 = 0;
            int n6 = 0;
            Label_0213: {
                Label_0021: {
                    if (bits == 0) {
                        if (parsableBitArray.readBit()) {
                            int n5;
                            if (!parsableBitArray.readBit()) {
                                bits2 = parsableBitArray.readBits(2) + 4;
                                n5 = parsableBitArray.readBits(4);
                            }
                            else {
                                final int bits3 = parsableBitArray.readBits(2);
                                if (bits3 == 0) {
                                    bits = 0;
                                    break Label_0021;
                                }
                                if (bits3 == 1) {
                                    n6 = n;
                                    n = 0;
                                    bits2 = 2;
                                    break Label_0213;
                                }
                                if (bits3 != 2) {
                                    if (bits3 != 3) {
                                        n6 = n;
                                        n = 0;
                                        bits2 = 0;
                                        break Label_0213;
                                    }
                                    bits2 = parsableBitArray.readBits(8) + 25;
                                    n5 = parsableBitArray.readBits(4);
                                }
                                else {
                                    bits2 = parsableBitArray.readBits(4) + 9;
                                    n5 = parsableBitArray.readBits(4);
                                }
                            }
                            final int n7 = n;
                            n = n5;
                            n6 = n7;
                            break Label_0213;
                        }
                        bits2 = parsableBitArray.readBits(3);
                        if (bits2 != 0) {
                            n6 = n;
                            bits2 += 2;
                            n = 0;
                            break Label_0213;
                        }
                        n = 0;
                        bits2 = 0;
                        n6 = 1;
                        break Label_0213;
                    }
                }
                final int n8 = n;
                bits2 = 1;
                n = bits;
                n6 = n8;
            }
            if (bits2 != 0 && paint != null) {
                int n9 = n;
                if (array2 != null) {
                    n9 = array2[n];
                }
                paint.setColor(array[n9]);
                canvas.drawRect((float)n4, (float)n2, (float)(n4 + bits2), (float)(n2 + 1), paint);
            }
            n4 += bits2;
            if (n6 != 0) {
                break;
            }
            n = n6;
        }
        return n4;
    }
    
    private static int paint8BitPixelCodeString(final ParsableBitArray parsableBitArray, final int[] array, final byte[] array2, int n, final int n2, final Paint paint, final Canvas canvas) {
        final int n3 = 0;
        int n4 = n;
        n = n3;
        while (true) {
            final int bits = parsableBitArray.readBits(8);
            int n5;
            int n6;
            if (bits != 0) {
                n5 = n;
                n = bits;
                n6 = 1;
            }
            else if (!parsableBitArray.readBit()) {
                n6 = parsableBitArray.readBits(7);
                if (n6 != 0) {
                    n5 = n;
                    n = 0;
                }
                else {
                    n = 0;
                    n6 = 0;
                    n5 = 1;
                }
            }
            else {
                n6 = parsableBitArray.readBits(7);
                final int bits2 = parsableBitArray.readBits(8);
                n5 = n;
                n = bits2;
            }
            if (n6 != 0 && paint != null) {
                int n7 = n;
                if (array2 != null) {
                    n7 = array2[n];
                }
                paint.setColor(array[n7]);
                canvas.drawRect((float)n4, (float)n2, (float)(n4 + n6), (float)(n2 + 1), paint);
            }
            n4 += n6;
            if (n5 != 0) {
                break;
            }
            n = n5;
        }
        return n4;
    }
    
    private static void paintPixelDataSubBlock(byte[] array, final int[] array2, final int n, final int n2, int n3, final Paint paint, final Canvas canvas) {
        final ParsableBitArray parsableBitArray = new ParsableBitArray(array);
        byte[] buildClutMapTable;
        byte[] array3 = buildClutMapTable = null;
        int n4 = n3;
        n3 = n2;
        while (parsableBitArray.bitsLeft() != 0) {
            final int bits = parsableBitArray.readBits(8);
            if (bits != 240) {
                switch (bits) {
                    default: {
                        switch (bits) {
                            default: {
                                continue;
                            }
                            case 34: {
                                array = buildClutMapTable(16, 8, parsableBitArray);
                                break;
                            }
                            case 33: {
                                array = buildClutMapTable(4, 8, parsableBitArray);
                                break;
                            }
                            case 32: {
                                buildClutMapTable = buildClutMapTable(4, 4, parsableBitArray);
                                continue;
                            }
                        }
                        array3 = array;
                        continue;
                    }
                    case 18: {
                        n3 = paint8BitPixelCodeString(parsableBitArray, array2, null, n3, n4, paint, canvas);
                        continue;
                    }
                    case 17: {
                        if (n == 3) {
                            array = DvbParser.defaultMap4To8;
                        }
                        else {
                            array = null;
                        }
                        n3 = paint4BitPixelCodeString(parsableBitArray, array2, array, n3, n4, paint, canvas);
                        parsableBitArray.byteAlign();
                        continue;
                    }
                    case 16: {
                        if (n == 3) {
                            if (array3 == null) {
                                array = DvbParser.defaultMap2To8;
                            }
                            else {
                                array = array3;
                            }
                        }
                        else if (n == 2) {
                            if (buildClutMapTable == null) {
                                array = DvbParser.defaultMap2To4;
                            }
                            else {
                                array = buildClutMapTable;
                            }
                        }
                        else {
                            array = null;
                        }
                        n3 = paint2BitPixelCodeString(parsableBitArray, array2, array, n3, n4, paint, canvas);
                        parsableBitArray.byteAlign();
                        continue;
                    }
                }
            }
            else {
                n4 += 2;
                n3 = n2;
            }
        }
    }
    
    private static void paintPixelDataSubBlocks(final ObjectData objectData, final ClutDefinition clutDefinition, final int n, final int n2, final int n3, final Paint paint, final Canvas canvas) {
        int[] array;
        if (n == 3) {
            array = clutDefinition.clutEntries8Bit;
        }
        else if (n == 2) {
            array = clutDefinition.clutEntries4Bit;
        }
        else {
            array = clutDefinition.clutEntries2Bit;
        }
        paintPixelDataSubBlock(objectData.topFieldData, array, n, n2, n3, paint, canvas);
        paintPixelDataSubBlock(objectData.bottomFieldData, array, n, n2, n3 + 1, paint, canvas);
    }
    
    private static ClutDefinition parseClutDefinition(final ParsableBitArray parsableBitArray, int i) {
        final int bits = parsableBitArray.readBits(8);
        parsableBitArray.skipBits(8);
        i -= 2;
        final int[] generateDefault2BitClutEntries = generateDefault2BitClutEntries();
        final int[] generateDefault4BitClutEntries = generateDefault4BitClutEntries();
        final int[] generateDefault8BitClutEntries = generateDefault8BitClutEntries();
        while (i > 0) {
            final int bits2 = parsableBitArray.readBits(8);
            final int bits3 = parsableBitArray.readBits(8);
            i -= 2;
            int[] array;
            if ((bits3 & 0x80) != 0x0) {
                array = generateDefault2BitClutEntries;
            }
            else if ((bits3 & 0x40) != 0x0) {
                array = generateDefault4BitClutEntries;
            }
            else {
                array = generateDefault8BitClutEntries;
            }
            int bits4;
            int bits5;
            int bits6;
            int bits7;
            if ((bits3 & 0x1) != 0x0) {
                bits4 = parsableBitArray.readBits(8);
                bits5 = parsableBitArray.readBits(8);
                bits6 = parsableBitArray.readBits(8);
                bits7 = parsableBitArray.readBits(8);
                i -= 4;
            }
            else {
                final int bits8 = parsableBitArray.readBits(6);
                final int bits9 = parsableBitArray.readBits(4);
                bits6 = parsableBitArray.readBits(4) << 4;
                final int bits10 = parsableBitArray.readBits(2);
                i -= 2;
                bits7 = bits10 << 6;
                bits4 = bits8 << 2;
                bits5 = bits9 << 4;
            }
            if (bits4 == 0) {
                bits5 = 0;
                bits6 = 0;
                bits7 = 255;
            }
            final byte b = (byte)(255 - (bits7 & 0xFF));
            final double v = bits4;
            final double n = bits5 - 128;
            Double.isNaN(n);
            Double.isNaN(v);
            final int n2 = (int)(v + 1.402 * n);
            final double n3 = bits6 - 128;
            Double.isNaN(n3);
            Double.isNaN(v);
            Double.isNaN(n);
            final int n4 = (int)(v - 0.34414 * n3 - n * 0.71414);
            Double.isNaN(n3);
            Double.isNaN(v);
            array[bits2] = getColor(b, Util.constrainValue(n2, 0, 255), Util.constrainValue(n4, 0, 255), Util.constrainValue((int)(v + n3 * 1.772), 0, 255));
        }
        return new ClutDefinition(bits, generateDefault2BitClutEntries, generateDefault4BitClutEntries, generateDefault8BitClutEntries);
    }
    
    private static DisplayDefinition parseDisplayDefinition(final ParsableBitArray parsableBitArray) {
        parsableBitArray.skipBits(4);
        final boolean bit = parsableBitArray.readBit();
        parsableBitArray.skipBits(3);
        final int bits = parsableBitArray.readBits(16);
        final int bits2 = parsableBitArray.readBits(16);
        int bits3;
        int bits4;
        int bits5;
        int bits6;
        if (bit) {
            bits3 = parsableBitArray.readBits(16);
            bits4 = parsableBitArray.readBits(16);
            bits5 = parsableBitArray.readBits(16);
            bits6 = parsableBitArray.readBits(16);
        }
        else {
            bits4 = bits;
            bits6 = bits2;
            bits3 = 0;
            bits5 = 0;
        }
        return new DisplayDefinition(bits, bits2, bits3, bits4, bits5, bits6);
    }
    
    private static ObjectData parseObjectData(final ParsableBitArray parsableBitArray) {
        final int bits = parsableBitArray.readBits(16);
        parsableBitArray.skipBits(4);
        final int bits2 = parsableBitArray.readBits(2);
        final boolean bit = parsableBitArray.readBit();
        parsableBitArray.skipBits(1);
        byte[] array = null;
        byte[] array2 = null;
        if (bits2 == 1) {
            parsableBitArray.skipBits(parsableBitArray.readBits(8) * 16);
        }
        else if (bits2 == 0) {
            final int bits3 = parsableBitArray.readBits(16);
            final int bits4 = parsableBitArray.readBits(16);
            if (bits3 > 0) {
                array2 = new byte[bits3];
                parsableBitArray.readBytes(array2, 0, bits3);
            }
            array = array2;
            if (bits4 > 0) {
                final byte[] array3 = new byte[bits4];
                parsableBitArray.readBytes(array3, 0, bits4);
                final byte[] array4 = array3;
                return new ObjectData(bits, bit, array2, array4);
            }
        }
        final byte[] array4 = array;
        array2 = array;
        return new ObjectData(bits, bit, array2, array4);
    }
    
    private static PageComposition parsePageComposition(final ParsableBitArray parsableBitArray, int i) {
        final int bits = parsableBitArray.readBits(8);
        final int bits2 = parsableBitArray.readBits(4);
        final int bits3 = parsableBitArray.readBits(2);
        parsableBitArray.skipBits(2);
        i -= 2;
        final SparseArray sparseArray = new SparseArray();
        while (i > 0) {
            final int bits4 = parsableBitArray.readBits(8);
            parsableBitArray.skipBits(8);
            final int bits5 = parsableBitArray.readBits(16);
            final int bits6 = parsableBitArray.readBits(16);
            i -= 6;
            sparseArray.put(bits4, (Object)new PageRegion(bits5, bits6));
        }
        return new PageComposition(bits, bits2, bits3, (SparseArray<PageRegion>)sparseArray);
    }
    
    private static RegionComposition parseRegionComposition(final ParsableBitArray parsableBitArray, int i) {
        final int bits = parsableBitArray.readBits(8);
        parsableBitArray.skipBits(4);
        final boolean bit = parsableBitArray.readBit();
        parsableBitArray.skipBits(3);
        final int bits2 = parsableBitArray.readBits(16);
        final int bits3 = parsableBitArray.readBits(16);
        final int bits4 = parsableBitArray.readBits(3);
        final int bits5 = parsableBitArray.readBits(3);
        parsableBitArray.skipBits(2);
        final int bits6 = parsableBitArray.readBits(8);
        final int bits7 = parsableBitArray.readBits(8);
        final int bits8 = parsableBitArray.readBits(4);
        final int bits9 = parsableBitArray.readBits(2);
        parsableBitArray.skipBits(2);
        i -= 10;
        final SparseArray sparseArray = new SparseArray();
        while (i > 0) {
            final int bits10 = parsableBitArray.readBits(16);
            final int bits11 = parsableBitArray.readBits(2);
            final int bits12 = parsableBitArray.readBits(2);
            final int bits13 = parsableBitArray.readBits(12);
            parsableBitArray.skipBits(4);
            final int bits14 = parsableBitArray.readBits(12);
            i -= 6;
            int n;
            int bits15;
            if (bits11 != 1 && bits11 != 2) {
                n = 0;
                bits15 = 0;
            }
            else {
                final int bits16 = parsableBitArray.readBits(8);
                bits15 = parsableBitArray.readBits(8);
                final int n2 = i - 2;
                i = (n = bits16);
                i = n2;
            }
            sparseArray.put(bits10, (Object)new RegionObject(bits11, bits12, bits13, bits14, n, bits15));
        }
        return new RegionComposition(bits, bit, bits2, bits3, bits4, bits5, bits6, bits7, bits8, bits9, (SparseArray<RegionObject>)sparseArray);
    }
    
    private static void parseSubtitlingSegment(final ParsableBitArray parsableBitArray, final SubtitleService subtitleService) {
        final int bits = parsableBitArray.readBits(8);
        final int bits2 = parsableBitArray.readBits(16);
        final int bits3 = parsableBitArray.readBits(16);
        final int bytePosition = parsableBitArray.getBytePosition();
        if (bits3 * 8 > parsableBitArray.bitsLeft()) {
            Log.w("DvbParser", "Data field length exceeds limit");
            parsableBitArray.skipBits(parsableBitArray.bitsLeft());
            return;
        }
        switch (bits) {
            case 20: {
                if (bits2 == subtitleService.subtitlePageId) {
                    subtitleService.displayDefinition = parseDisplayDefinition(parsableBitArray);
                    break;
                }
                break;
            }
            case 19: {
                if (bits2 == subtitleService.subtitlePageId) {
                    final ObjectData objectData = parseObjectData(parsableBitArray);
                    subtitleService.objects.put(objectData.id, (Object)objectData);
                    break;
                }
                if (bits2 == subtitleService.ancillaryPageId) {
                    final ObjectData objectData2 = parseObjectData(parsableBitArray);
                    subtitleService.ancillaryObjects.put(objectData2.id, (Object)objectData2);
                    break;
                }
                break;
            }
            case 18: {
                if (bits2 == subtitleService.subtitlePageId) {
                    final ClutDefinition clutDefinition = parseClutDefinition(parsableBitArray, bits3);
                    subtitleService.cluts.put(clutDefinition.id, (Object)clutDefinition);
                    break;
                }
                if (bits2 == subtitleService.ancillaryPageId) {
                    final ClutDefinition clutDefinition2 = parseClutDefinition(parsableBitArray, bits3);
                    subtitleService.ancillaryCluts.put(clutDefinition2.id, (Object)clutDefinition2);
                    break;
                }
                break;
            }
            case 17: {
                final PageComposition pageComposition = subtitleService.pageComposition;
                if (bits2 == subtitleService.subtitlePageId && pageComposition != null) {
                    final RegionComposition regionComposition = parseRegionComposition(parsableBitArray, bits3);
                    if (pageComposition.state == 0) {
                        regionComposition.mergeFrom((RegionComposition)subtitleService.regions.get(regionComposition.id));
                    }
                    subtitleService.regions.put(regionComposition.id, (Object)regionComposition);
                    break;
                }
                break;
            }
            case 16: {
                if (bits2 != subtitleService.subtitlePageId) {
                    break;
                }
                final PageComposition pageComposition2 = subtitleService.pageComposition;
                final PageComposition pageComposition3 = parsePageComposition(parsableBitArray, bits3);
                if (pageComposition3.state != 0) {
                    subtitleService.pageComposition = pageComposition3;
                    subtitleService.regions.clear();
                    subtitleService.cluts.clear();
                    subtitleService.objects.clear();
                    break;
                }
                if (pageComposition2 != null && pageComposition2.version != pageComposition3.version) {
                    subtitleService.pageComposition = pageComposition3;
                    break;
                }
                break;
            }
        }
        parsableBitArray.skipBytes(bytePosition + bits3 - parsableBitArray.getBytePosition());
    }
    
    public List<Cue> decode(final byte[] array, int i) {
        final ParsableBitArray parsableBitArray = new ParsableBitArray(array, i);
        while (parsableBitArray.bitsLeft() >= 48 && parsableBitArray.readBits(8) == 15) {
            parseSubtitlingSegment(parsableBitArray, this.subtitleService);
        }
        final SubtitleService subtitleService = this.subtitleService;
        if (subtitleService.pageComposition == null) {
            return Collections.emptyList();
        }
        Object o = subtitleService.displayDefinition;
        if (o == null) {
            o = this.defaultDisplayDefinition;
        }
        final Bitmap bitmap = this.bitmap;
        if (bitmap == null || ((DisplayDefinition)o).width + 1 != bitmap.getWidth() || ((DisplayDefinition)o).height + 1 != this.bitmap.getHeight()) {
            this.bitmap = Bitmap.createBitmap(((DisplayDefinition)o).width + 1, ((DisplayDefinition)o).height + 1, Bitmap$Config.ARGB_8888);
            this.canvas.setBitmap(this.bitmap);
        }
        final ArrayList<Cue> list = new ArrayList<Cue>();
        final SparseArray<PageRegion> regions = this.subtitleService.pageComposition.regions;
        for (int j = 0; j < regions.size(); ++j) {
            final PageRegion pageRegion = (PageRegion)regions.valueAt(j);
            i = regions.keyAt(j);
            final RegionComposition regionComposition = (RegionComposition)this.subtitleService.regions.get(i);
            final int n = pageRegion.horizontalAddress + ((DisplayDefinition)o).horizontalPositionMinimum;
            final int n2 = pageRegion.verticalAddress + ((DisplayDefinition)o).verticalPositionMinimum;
            i = Math.min(regionComposition.width + n, ((DisplayDefinition)o).horizontalPositionMaximum);
            final int min = Math.min(regionComposition.height + n2, ((DisplayDefinition)o).verticalPositionMaximum);
            final Canvas canvas = this.canvas;
            final float n3 = (float)n;
            final float n4 = (float)n2;
            canvas.clipRect(n3, n4, (float)i, (float)min, Region$Op.REPLACE);
            ClutDefinition defaultClutDefinition;
            if ((defaultClutDefinition = (ClutDefinition)this.subtitleService.cluts.get(regionComposition.clutId)) == null && (defaultClutDefinition = (ClutDefinition)this.subtitleService.ancillaryCluts.get(regionComposition.clutId)) == null) {
                defaultClutDefinition = this.defaultClutDefinition;
            }
            SparseArray<RegionObject> regionObjects;
            int key;
            RegionObject regionObject;
            ObjectData objectData;
            Paint defaultPaint;
            for (regionObjects = regionComposition.regionObjects, i = 0; i < regionObjects.size(); ++i) {
                key = regionObjects.keyAt(i);
                regionObject = (RegionObject)regionObjects.valueAt(i);
                objectData = (ObjectData)this.subtitleService.objects.get(key);
                if (objectData == null) {
                    objectData = (ObjectData)this.subtitleService.ancillaryObjects.get(key);
                }
                if (objectData != null) {
                    if (objectData.nonModifyingColorFlag) {
                        defaultPaint = null;
                    }
                    else {
                        defaultPaint = this.defaultPaint;
                    }
                    paintPixelDataSubBlocks(objectData, defaultClutDefinition, regionComposition.depth, regionObject.horizontalPosition + n, n2 + regionObject.verticalPosition, defaultPaint, this.canvas);
                }
            }
            if (regionComposition.fillFlag) {
                i = regionComposition.depth;
                if (i == 3) {
                    i = defaultClutDefinition.clutEntries8Bit[regionComposition.pixelCode8Bit];
                }
                else if (i == 2) {
                    i = defaultClutDefinition.clutEntries4Bit[regionComposition.pixelCode4Bit];
                }
                else {
                    i = defaultClutDefinition.clutEntries2Bit[regionComposition.pixelCode2Bit];
                }
                this.fillRegionPaint.setColor(i);
                this.canvas.drawRect(n3, n4, (float)(regionComposition.width + n), (float)(regionComposition.height + n2), this.fillRegionPaint);
            }
            final Bitmap bitmap2 = Bitmap.createBitmap(this.bitmap, n, n2, regionComposition.width, regionComposition.height);
            i = ((DisplayDefinition)o).width;
            final float n5 = n3 / i;
            final int height = ((DisplayDefinition)o).height;
            list.add(new Cue(bitmap2, n5, 0, n4 / height, 0, regionComposition.width / (float)i, regionComposition.height / (float)height));
            this.canvas.drawColor(0, PorterDuff$Mode.CLEAR);
        }
        return list;
    }
    
    public void reset() {
        this.subtitleService.reset();
    }
    
    private static final class ClutDefinition
    {
        public final int[] clutEntries2Bit;
        public final int[] clutEntries4Bit;
        public final int[] clutEntries8Bit;
        public final int id;
        
        public ClutDefinition(final int id, final int[] clutEntries2Bit, final int[] clutEntries4Bit, final int[] clutEntries8Bit) {
            this.id = id;
            this.clutEntries2Bit = clutEntries2Bit;
            this.clutEntries4Bit = clutEntries4Bit;
            this.clutEntries8Bit = clutEntries8Bit;
        }
    }
    
    private static final class DisplayDefinition
    {
        public final int height;
        public final int horizontalPositionMaximum;
        public final int horizontalPositionMinimum;
        public final int verticalPositionMaximum;
        public final int verticalPositionMinimum;
        public final int width;
        
        public DisplayDefinition(final int width, final int height, final int horizontalPositionMinimum, final int horizontalPositionMaximum, final int verticalPositionMinimum, final int verticalPositionMaximum) {
            this.width = width;
            this.height = height;
            this.horizontalPositionMinimum = horizontalPositionMinimum;
            this.horizontalPositionMaximum = horizontalPositionMaximum;
            this.verticalPositionMinimum = verticalPositionMinimum;
            this.verticalPositionMaximum = verticalPositionMaximum;
        }
    }
    
    private static final class ObjectData
    {
        public final byte[] bottomFieldData;
        public final int id;
        public final boolean nonModifyingColorFlag;
        public final byte[] topFieldData;
        
        public ObjectData(final int id, final boolean nonModifyingColorFlag, final byte[] topFieldData, final byte[] bottomFieldData) {
            this.id = id;
            this.nonModifyingColorFlag = nonModifyingColorFlag;
            this.topFieldData = topFieldData;
            this.bottomFieldData = bottomFieldData;
        }
    }
    
    private static final class PageComposition
    {
        public final SparseArray<PageRegion> regions;
        public final int state;
        public final int timeOutSecs;
        public final int version;
        
        public PageComposition(final int timeOutSecs, final int version, final int state, final SparseArray<PageRegion> regions) {
            this.timeOutSecs = timeOutSecs;
            this.version = version;
            this.state = state;
            this.regions = regions;
        }
    }
    
    private static final class PageRegion
    {
        public final int horizontalAddress;
        public final int verticalAddress;
        
        public PageRegion(final int horizontalAddress, final int verticalAddress) {
            this.horizontalAddress = horizontalAddress;
            this.verticalAddress = verticalAddress;
        }
    }
    
    private static final class RegionComposition
    {
        public final int clutId;
        public final int depth;
        public final boolean fillFlag;
        public final int height;
        public final int id;
        public final int levelOfCompatibility;
        public final int pixelCode2Bit;
        public final int pixelCode4Bit;
        public final int pixelCode8Bit;
        public final SparseArray<RegionObject> regionObjects;
        public final int width;
        
        public RegionComposition(final int id, final boolean fillFlag, final int width, final int height, final int levelOfCompatibility, final int depth, final int clutId, final int pixelCode8Bit, final int pixelCode4Bit, final int pixelCode2Bit, final SparseArray<RegionObject> regionObjects) {
            this.id = id;
            this.fillFlag = fillFlag;
            this.width = width;
            this.height = height;
            this.levelOfCompatibility = levelOfCompatibility;
            this.depth = depth;
            this.clutId = clutId;
            this.pixelCode8Bit = pixelCode8Bit;
            this.pixelCode4Bit = pixelCode4Bit;
            this.pixelCode2Bit = pixelCode2Bit;
            this.regionObjects = regionObjects;
        }
        
        public void mergeFrom(final RegionComposition regionComposition) {
            if (regionComposition == null) {
                return;
            }
            final SparseArray<RegionObject> regionObjects = regionComposition.regionObjects;
            for (int i = 0; i < regionObjects.size(); ++i) {
                this.regionObjects.put(regionObjects.keyAt(i), regionObjects.valueAt(i));
            }
        }
    }
    
    private static final class RegionObject
    {
        public final int backgroundPixelCode;
        public final int foregroundPixelCode;
        public final int horizontalPosition;
        public final int provider;
        public final int type;
        public final int verticalPosition;
        
        public RegionObject(final int type, final int provider, final int horizontalPosition, final int verticalPosition, final int foregroundPixelCode, final int backgroundPixelCode) {
            this.type = type;
            this.provider = provider;
            this.horizontalPosition = horizontalPosition;
            this.verticalPosition = verticalPosition;
            this.foregroundPixelCode = foregroundPixelCode;
            this.backgroundPixelCode = backgroundPixelCode;
        }
    }
    
    private static final class SubtitleService
    {
        public final SparseArray<ClutDefinition> ancillaryCluts;
        public final SparseArray<ObjectData> ancillaryObjects;
        public final int ancillaryPageId;
        public final SparseArray<ClutDefinition> cluts;
        public DisplayDefinition displayDefinition;
        public final SparseArray<ObjectData> objects;
        public PageComposition pageComposition;
        public final SparseArray<RegionComposition> regions;
        public final int subtitlePageId;
        
        public SubtitleService(final int subtitlePageId, final int ancillaryPageId) {
            this.regions = (SparseArray<RegionComposition>)new SparseArray();
            this.cluts = (SparseArray<ClutDefinition>)new SparseArray();
            this.objects = (SparseArray<ObjectData>)new SparseArray();
            this.ancillaryCluts = (SparseArray<ClutDefinition>)new SparseArray();
            this.ancillaryObjects = (SparseArray<ObjectData>)new SparseArray();
            this.subtitlePageId = subtitlePageId;
            this.ancillaryPageId = ancillaryPageId;
        }
        
        public void reset() {
            this.regions.clear();
            this.cluts.clear();
            this.objects.clear();
            this.ancillaryCluts.clear();
            this.ancillaryObjects.clear();
            this.displayDefinition = null;
            this.pageComposition = null;
        }
    }
}

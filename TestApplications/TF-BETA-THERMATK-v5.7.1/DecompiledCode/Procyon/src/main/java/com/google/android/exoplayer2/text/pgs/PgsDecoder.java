// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.pgs;

import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;
import java.util.Arrays;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.Util;
import java.util.zip.Inflater;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

public final class PgsDecoder extends SimpleSubtitleDecoder
{
    private final ParsableByteArray buffer;
    private final CueBuilder cueBuilder;
    private final ParsableByteArray inflatedBuffer;
    private Inflater inflater;
    
    public PgsDecoder() {
        super("PgsDecoder");
        this.buffer = new ParsableByteArray();
        this.inflatedBuffer = new ParsableByteArray();
        this.cueBuilder = new CueBuilder();
    }
    
    private void maybeInflateData(final ParsableByteArray parsableByteArray) {
        if (parsableByteArray.bytesLeft() > 0 && parsableByteArray.peekUnsignedByte() == 120) {
            if (this.inflater == null) {
                this.inflater = new Inflater();
            }
            if (Util.inflate(parsableByteArray, this.inflatedBuffer, this.inflater)) {
                final ParsableByteArray inflatedBuffer = this.inflatedBuffer;
                parsableByteArray.reset(inflatedBuffer.data, inflatedBuffer.limit());
            }
        }
    }
    
    private static Cue readNextSection(final ParsableByteArray parsableByteArray, final CueBuilder cueBuilder) {
        final int limit = parsableByteArray.limit();
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        final int position = parsableByteArray.getPosition() + unsignedShort;
        final Cue cue = null;
        if (position > limit) {
            parsableByteArray.setPosition(limit);
            return null;
        }
        Cue cue2 = null;
        if (unsignedByte != 128) {
            switch (unsignedByte) {
                default: {
                    cue2 = cue;
                    break;
                }
                case 22: {
                    cueBuilder.parseIdentifierSection(parsableByteArray, unsignedShort);
                    cue2 = cue;
                    break;
                }
                case 21: {
                    cueBuilder.parseBitmapSection(parsableByteArray, unsignedShort);
                    cue2 = cue;
                    break;
                }
                case 20: {
                    cueBuilder.parsePaletteSection(parsableByteArray, unsignedShort);
                    cue2 = cue;
                    break;
                }
            }
        }
        else {
            final Cue build = cueBuilder.build();
            cueBuilder.reset();
            cue2 = build;
        }
        parsableByteArray.setPosition(position);
        return cue2;
    }
    
    @Override
    protected Subtitle decode(final byte[] array, final int n, final boolean b) throws SubtitleDecoderException {
        this.buffer.reset(array, n);
        this.maybeInflateData(this.buffer);
        this.cueBuilder.reset();
        final ArrayList<Cue> list = new ArrayList<Cue>();
        while (this.buffer.bytesLeft() >= 3) {
            final Cue nextSection = readNextSection(this.buffer, this.cueBuilder);
            if (nextSection != null) {
                list.add(nextSection);
            }
        }
        return new PgsSubtitle((List<Cue>)Collections.unmodifiableList((List<?>)list));
    }
    
    private static final class CueBuilder
    {
        private final ParsableByteArray bitmapData;
        private int bitmapHeight;
        private int bitmapWidth;
        private int bitmapX;
        private int bitmapY;
        private final int[] colors;
        private boolean colorsSet;
        private int planeHeight;
        private int planeWidth;
        
        public CueBuilder() {
            this.bitmapData = new ParsableByteArray();
            this.colors = new int[256];
        }
        
        private void parseBitmapSection(final ParsableByteArray parsableByteArray, int a) {
            if (a < 4) {
                return;
            }
            parsableByteArray.skipBytes(3);
            final boolean b = (parsableByteArray.readUnsignedByte() & 0x80) != 0x0;
            final int n = a -= 4;
            if (b) {
                if (n < 7) {
                    return;
                }
                a = parsableByteArray.readUnsignedInt24();
                if (a < 4) {
                    return;
                }
                this.bitmapWidth = parsableByteArray.readUnsignedShort();
                this.bitmapHeight = parsableByteArray.readUnsignedShort();
                this.bitmapData.reset(a - 4);
                a = n - 7;
            }
            final int position = this.bitmapData.getPosition();
            final int limit = this.bitmapData.limit();
            if (position < limit && a > 0) {
                a = Math.min(a, limit - position);
                parsableByteArray.readBytes(this.bitmapData.data, position, a);
                this.bitmapData.setPosition(position + a);
            }
        }
        
        private void parseIdentifierSection(final ParsableByteArray parsableByteArray, final int n) {
            if (n < 19) {
                return;
            }
            this.planeWidth = parsableByteArray.readUnsignedShort();
            this.planeHeight = parsableByteArray.readUnsignedShort();
            parsableByteArray.skipBytes(11);
            this.bitmapX = parsableByteArray.readUnsignedShort();
            this.bitmapY = parsableByteArray.readUnsignedShort();
        }
        
        private void parsePaletteSection(final ParsableByteArray parsableByteArray, int i) {
            if (i % 5 != 2) {
                return;
            }
            parsableByteArray.skipBytes(2);
            Arrays.fill(this.colors, 0);
            int n;
            int unsignedByte;
            int unsignedByte2;
            int unsignedByte3;
            int unsignedByte4;
            int unsignedByte5;
            double v;
            double n2;
            int n3;
            double n4;
            int n5;
            for (n = i / 5, i = 0; i < n; ++i) {
                unsignedByte = parsableByteArray.readUnsignedByte();
                unsignedByte2 = parsableByteArray.readUnsignedByte();
                unsignedByte3 = parsableByteArray.readUnsignedByte();
                unsignedByte4 = parsableByteArray.readUnsignedByte();
                unsignedByte5 = parsableByteArray.readUnsignedByte();
                v = unsignedByte2;
                n2 = unsignedByte3 - 128;
                Double.isNaN(n2);
                Double.isNaN(v);
                n3 = (int)(1.402 * n2 + v);
                n4 = unsignedByte4 - 128;
                Double.isNaN(n4);
                Double.isNaN(v);
                Double.isNaN(n2);
                n5 = (int)(v - 0.34414 * n4 - n2 * 0.71414);
                Double.isNaN(n4);
                Double.isNaN(v);
                this.colors[unsignedByte] = (Util.constrainValue((int)(v + n4 * 1.772), 0, 255) | (Util.constrainValue(n5, 0, 255) << 8 | (unsignedByte5 << 24 | Util.constrainValue(n3, 0, 255) << 16)));
            }
            this.colorsSet = true;
        }
        
        public Cue build() {
            if (this.planeWidth != 0 && this.planeHeight != 0 && this.bitmapWidth != 0 && this.bitmapHeight != 0 && this.bitmapData.limit() != 0 && this.bitmapData.getPosition() == this.bitmapData.limit() && this.colorsSet) {
                this.bitmapData.setPosition(0);
                final int[] a = new int[this.bitmapWidth * this.bitmapHeight];
                int i = 0;
                while (i < a.length) {
                    final int unsignedByte = this.bitmapData.readUnsignedByte();
                    if (unsignedByte != 0) {
                        final int n = i + 1;
                        a[i] = this.colors[unsignedByte];
                        i = n;
                    }
                    else {
                        final int unsignedByte2 = this.bitmapData.readUnsignedByte();
                        if (unsignedByte2 == 0) {
                            continue;
                        }
                        int n2;
                        if ((unsignedByte2 & 0x40) == 0x0) {
                            n2 = (unsignedByte2 & 0x3F);
                        }
                        else {
                            n2 = ((unsignedByte2 & 0x3F) << 8 | this.bitmapData.readUnsignedByte());
                        }
                        int val;
                        if ((unsignedByte2 & 0x80) == 0x0) {
                            val = 0;
                        }
                        else {
                            val = this.colors[this.bitmapData.readUnsignedByte()];
                        }
                        final int toIndex = n2 + i;
                        Arrays.fill(a, i, toIndex, val);
                        i = toIndex;
                    }
                }
                final Bitmap bitmap = Bitmap.createBitmap(a, this.bitmapWidth, this.bitmapHeight, Bitmap$Config.ARGB_8888);
                final float n3 = (float)this.bitmapX;
                final int planeWidth = this.planeWidth;
                final float n4 = n3 / planeWidth;
                final float n5 = (float)this.bitmapY;
                final int planeHeight = this.planeHeight;
                return new Cue(bitmap, n4, 0, n5 / planeHeight, 0, this.bitmapWidth / (float)planeWidth, this.bitmapHeight / (float)planeHeight);
            }
            return null;
        }
        
        public void reset() {
            this.planeWidth = 0;
            this.planeHeight = 0;
            this.bitmapX = 0;
            this.bitmapY = 0;
            this.bitmapWidth = 0;
            this.bitmapHeight = 0;
            this.bitmapData.reset(0);
            this.colorsSet = false;
        }
    }
}

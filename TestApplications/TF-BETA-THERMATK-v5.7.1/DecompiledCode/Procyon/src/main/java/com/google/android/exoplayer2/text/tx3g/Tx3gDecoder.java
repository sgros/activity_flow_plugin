// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.tx3g;

import android.text.Layout$Alignment;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import java.nio.charset.Charset;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.text.style.StyleSpan;
import android.text.style.ForegroundColorSpan;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import android.text.SpannableStringBuilder;
import java.util.List;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

public final class Tx3gDecoder extends SimpleSubtitleDecoder
{
    private static final int TYPE_STYL;
    private static final int TYPE_TBOX;
    private int calculatedVideoTrackHeight;
    private boolean customVerticalPlacement;
    private int defaultColorRgba;
    private int defaultFontFace;
    private String defaultFontFamily;
    private float defaultVerticalPlacement;
    private final ParsableByteArray parsableByteArray;
    
    static {
        TYPE_STYL = Util.getIntegerCodeForString("styl");
        TYPE_TBOX = Util.getIntegerCodeForString("tbox");
    }
    
    public Tx3gDecoder(final List<byte[]> list) {
        super("Tx3gDecoder");
        this.parsableByteArray = new ParsableByteArray();
        this.decodeInitializationData(list);
    }
    
    private void applyStyleRecord(final ParsableByteArray parsableByteArray, final SpannableStringBuilder spannableStringBuilder) throws SubtitleDecoderException {
        assertTrue(parsableByteArray.bytesLeft() >= 12);
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        final int unsignedShort2 = parsableByteArray.readUnsignedShort();
        parsableByteArray.skipBytes(2);
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        parsableByteArray.skipBytes(1);
        final int int1 = parsableByteArray.readInt();
        attachFontFace(spannableStringBuilder, unsignedByte, this.defaultFontFace, unsignedShort, unsignedShort2, 0);
        attachColor(spannableStringBuilder, int1, this.defaultColorRgba, unsignedShort, unsignedShort2, 0);
    }
    
    private static void assertTrue(final boolean b) throws SubtitleDecoderException {
        if (b) {
            return;
        }
        throw new SubtitleDecoderException("Unexpected subtitle format.");
    }
    
    private static void attachColor(final SpannableStringBuilder spannableStringBuilder, final int n, final int n2, final int n3, final int n4, final int n5) {
        if (n != n2) {
            spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(n >>> 8 | (n & 0xFF) << 24), n3, n4, n5 | 0x21);
        }
    }
    
    private static void attachFontFace(final SpannableStringBuilder spannableStringBuilder, int n, int n2, final int n3, final int n4, int n5) {
        if (n != n2) {
            final int n6 = n5 | 0x21;
            final int n7 = 1;
            if ((n & 0x1) != 0x0) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            if ((n & 0x2) != 0x0) {
                n5 = 1;
            }
            else {
                n5 = 0;
            }
            if (n2 != 0) {
                if (n5 != 0) {
                    spannableStringBuilder.setSpan((Object)new StyleSpan(3), n3, n4, n6);
                }
                else {
                    spannableStringBuilder.setSpan((Object)new StyleSpan(1), n3, n4, n6);
                }
            }
            else if (n5 != 0) {
                spannableStringBuilder.setSpan((Object)new StyleSpan(2), n3, n4, n6);
            }
            if ((n & 0x4) != 0x0) {
                n = n7;
            }
            else {
                n = 0;
            }
            if (n != 0) {
                spannableStringBuilder.setSpan((Object)new UnderlineSpan(), n3, n4, n6);
            }
            if (n == 0 && n2 == 0 && n5 == 0) {
                spannableStringBuilder.setSpan((Object)new StyleSpan(0), n3, n4, n6);
            }
        }
    }
    
    private static void attachFontFamily(final SpannableStringBuilder spannableStringBuilder, final String s, final String s2, final int n, final int n2, final int n3) {
        if (s != s2) {
            spannableStringBuilder.setSpan((Object)new TypefaceSpan(s), n, n2, n3 | 0x21);
        }
    }
    
    private void decodeInitializationData(final List<byte[]> list) {
        final String s = "sans-serif";
        boolean customVerticalPlacement = false;
        if (list != null && list.size() == 1 && (list.get(0).length == 48 || list.get(0).length == 53)) {
            final byte[] array = list.get(0);
            this.defaultFontFace = array[24];
            this.defaultColorRgba = ((array[26] & 0xFF) << 24 | (array[27] & 0xFF) << 16 | (array[28] & 0xFF) << 8 | (array[29] & 0xFF));
            String defaultFontFamily = s;
            if ("Serif".equals(Util.fromUtf8Bytes(array, 43, array.length - 43))) {
                defaultFontFamily = "serif";
            }
            this.defaultFontFamily = defaultFontFamily;
            this.calculatedVideoTrackHeight = array[25] * 20;
            if ((array[0] & 0x20) != 0x0) {
                customVerticalPlacement = true;
            }
            this.customVerticalPlacement = customVerticalPlacement;
            if (this.customVerticalPlacement) {
                this.defaultVerticalPlacement = ((array[11] & 0xFF) | (array[10] & 0xFF) << 8) / (float)this.calculatedVideoTrackHeight;
                this.defaultVerticalPlacement = Util.constrainValue(this.defaultVerticalPlacement, 0.0f, 0.95f);
            }
            else {
                this.defaultVerticalPlacement = 0.85f;
            }
        }
        else {
            this.defaultFontFace = 0;
            this.defaultColorRgba = -1;
            this.defaultFontFamily = "sans-serif";
            this.customVerticalPlacement = false;
            this.defaultVerticalPlacement = 0.85f;
        }
    }
    
    private static String readSubtitleText(final ParsableByteArray parsableByteArray) throws SubtitleDecoderException {
        assertTrue(parsableByteArray.bytesLeft() >= 2);
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        if (unsignedShort == 0) {
            return "";
        }
        if (parsableByteArray.bytesLeft() >= 2) {
            final char peekChar = parsableByteArray.peekChar();
            if (peekChar == '\ufeff' || peekChar == '\ufffe') {
                return parsableByteArray.readString(unsignedShort, Charset.forName("UTF-16"));
            }
        }
        return parsableByteArray.readString(unsignedShort, Charset.forName("UTF-8"));
    }
    
    @Override
    protected Subtitle decode(final byte[] array, int n, final boolean b) throws SubtitleDecoderException {
        this.parsableByteArray.reset(array, n);
        final String subtitleText = readSubtitleText(this.parsableByteArray);
        if (subtitleText.isEmpty()) {
            return Tx3gSubtitle.EMPTY;
        }
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)subtitleText);
        attachFontFace(spannableStringBuilder, this.defaultFontFace, 0, 0, spannableStringBuilder.length(), 16711680);
        attachColor(spannableStringBuilder, this.defaultColorRgba, -1, 0, spannableStringBuilder.length(), 16711680);
        attachFontFamily(spannableStringBuilder, this.defaultFontFamily, "sans-serif", 0, spannableStringBuilder.length(), 16711680);
        float defaultVerticalPlacement = this.defaultVerticalPlacement;
        while (this.parsableByteArray.bytesLeft() >= 8) {
            final int position = this.parsableByteArray.getPosition();
            final int int1 = this.parsableByteArray.readInt();
            final int int2 = this.parsableByteArray.readInt();
            final int type_STYL = Tx3gDecoder.TYPE_STYL;
            final boolean b2 = true;
            boolean b3 = true;
            n = 0;
            float constrainValue;
            if (int2 == type_STYL) {
                if (this.parsableByteArray.bytesLeft() < 2) {
                    b3 = false;
                }
                assertTrue(b3);
                final int unsignedShort = this.parsableByteArray.readUnsignedShort();
                while (true) {
                    constrainValue = defaultVerticalPlacement;
                    if (n >= unsignedShort) {
                        break;
                    }
                    this.applyStyleRecord(this.parsableByteArray, spannableStringBuilder);
                    ++n;
                }
            }
            else {
                constrainValue = defaultVerticalPlacement;
                if (int2 == Tx3gDecoder.TYPE_TBOX) {
                    constrainValue = defaultVerticalPlacement;
                    if (this.customVerticalPlacement) {
                        assertTrue(this.parsableByteArray.bytesLeft() >= 2 && b2);
                        constrainValue = Util.constrainValue(this.parsableByteArray.readUnsignedShort() / (float)this.calculatedVideoTrackHeight, 0.0f, 0.95f);
                    }
                }
            }
            this.parsableByteArray.setPosition(position + int1);
            defaultVerticalPlacement = constrainValue;
        }
        return new Tx3gSubtitle(new Cue((CharSequence)spannableStringBuilder, null, defaultVerticalPlacement, 0, 0, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE));
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.cea;

import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.text.style.StyleSpan;
import android.text.Layout$Alignment;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import com.google.android.exoplayer2.text.SubtitleOutputBuffer;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.Log;
import java.util.Collections;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.text.Cue;
import java.util.List;
import com.google.android.exoplayer2.util.ParsableByteArray;

public final class Cea708Decoder extends CeaDecoder
{
    private final ParsableByteArray ccData;
    private final CueBuilder[] cueBuilders;
    private List<Cue> cues;
    private CueBuilder currentCueBuilder;
    private DtvCcPacket currentDtvCcPacket;
    private int currentWindow;
    private List<Cue> lastCues;
    private final int selectedServiceNumber;
    private final ParsableBitArray serviceBlockPacket;
    
    public Cea708Decoder(int i, final List<byte[]> list) {
        this.ccData = new ParsableByteArray();
        this.serviceBlockPacket = new ParsableBitArray();
        int selectedServiceNumber = i;
        if (i == -1) {
            selectedServiceNumber = 1;
        }
        this.selectedServiceNumber = selectedServiceNumber;
        this.cueBuilders = new CueBuilder[8];
        for (i = 0; i < 8; ++i) {
            this.cueBuilders[i] = new CueBuilder();
        }
        this.currentCueBuilder = this.cueBuilders[0];
        this.resetCueBuilders();
    }
    
    private void finalizeCurrentPacket() {
        if (this.currentDtvCcPacket == null) {
            return;
        }
        this.processCurrentPacket();
        this.currentDtvCcPacket = null;
    }
    
    private List<Cue> getDisplayCues() {
        final ArrayList<Cue> list = (ArrayList<Cue>)new ArrayList<Comparable>();
        for (int i = 0; i < 8; ++i) {
            if (!this.cueBuilders[i].isEmpty() && this.cueBuilders[i].isVisible()) {
                list.add(this.cueBuilders[i].build());
            }
        }
        Collections.sort((List<Comparable>)list);
        return (List<Cue>)Collections.unmodifiableList((List<?>)list);
    }
    
    private void handleC0Command(final int i) {
        if (i != 0) {
            if (i != 3) {
                if (i != 8) {
                    switch (i) {
                        default: {
                            if (i >= 17 && i <= 23) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Currently unsupported COMMAND_EXT1 Command: ");
                                sb.append(i);
                                Log.w("Cea708Decoder", sb.toString());
                                this.serviceBlockPacket.skipBits(8);
                                break;
                            }
                            if (i >= 24 && i <= 31) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Currently unsupported COMMAND_P16 Command: ");
                                sb2.append(i);
                                Log.w("Cea708Decoder", sb2.toString());
                                this.serviceBlockPacket.skipBits(16);
                                break;
                            }
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Invalid C0 command: ");
                            sb3.append(i);
                            Log.w("Cea708Decoder", sb3.toString());
                            break;
                        }
                        case 14: {
                            break;
                        }
                        case 13: {
                            this.currentCueBuilder.append('\n');
                            break;
                        }
                        case 12: {
                            this.resetCueBuilders();
                            break;
                        }
                    }
                }
                else {
                    this.currentCueBuilder.backspace();
                }
            }
            else {
                this.cues = this.getDisplayCues();
            }
        }
    }
    
    private void handleC1Command(int i) {
        int j = 1;
        int k = 1;
        int l = 1;
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid C1 command: ");
                sb.append(i);
                Log.w("Cea708Decoder", sb.toString());
            }
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159: {
                i -= 152;
                this.handleDefineWindow(i);
                if (this.currentWindow != i) {
                    this.currentWindow = i;
                    this.currentCueBuilder = this.cueBuilders[i];
                }
            }
            case 151: {
                if (!this.currentCueBuilder.isDefined()) {
                    this.serviceBlockPacket.skipBits(32);
                    return;
                }
                this.handleSetWindowAttributes();
            }
            case 146: {
                if (!this.currentCueBuilder.isDefined()) {
                    this.serviceBlockPacket.skipBits(16);
                    return;
                }
                this.handleSetPenLocation();
            }
            case 145: {
                if (!this.currentCueBuilder.isDefined()) {
                    this.serviceBlockPacket.skipBits(24);
                    return;
                }
                this.handleSetPenColor();
            }
            case 144: {
                if (!this.currentCueBuilder.isDefined()) {
                    this.serviceBlockPacket.skipBits(16);
                    return;
                }
                this.handleSetPenAttributes();
            }
            case 128:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135: {
                i -= 128;
                if (this.currentWindow != i) {
                    this.currentWindow = i;
                    this.currentCueBuilder = this.cueBuilders[i];
                }
            }
            case 142: {}
            case 143: {
                this.resetCueBuilders();
            }
            case 141: {
                this.serviceBlockPacket.skipBits(8);
            }
            case 140: {
                while (l <= 8) {
                    if (this.serviceBlockPacket.readBit()) {
                        this.cueBuilders[8 - l].reset();
                    }
                    ++l;
                }
            }
            case 139: {
                CueBuilder cueBuilder;
                for (i = 1; i <= 8; ++i) {
                    if (this.serviceBlockPacket.readBit()) {
                        cueBuilder = this.cueBuilders[8 - i];
                        cueBuilder.setVisibility(cueBuilder.isVisible() ^ true);
                    }
                }
            }
            case 138: {
                while (j <= 8) {
                    if (this.serviceBlockPacket.readBit()) {
                        this.cueBuilders[8 - j].setVisibility(false);
                    }
                    ++j;
                }
            }
            case 137: {
                for (i = 1; i <= 8; ++i) {
                    if (this.serviceBlockPacket.readBit()) {
                        this.cueBuilders[8 - i].setVisibility(true);
                    }
                }
            }
            case 136: {
                while (k <= 8) {
                    if (this.serviceBlockPacket.readBit()) {
                        this.cueBuilders[8 - k].clear();
                    }
                    ++k;
                }
            }
        }
    }
    
    private void handleC2Command(final int n) {
        if (n > 7) {
            if (n <= 15) {
                this.serviceBlockPacket.skipBits(8);
            }
            else if (n <= 23) {
                this.serviceBlockPacket.skipBits(16);
            }
            else if (n <= 31) {
                this.serviceBlockPacket.skipBits(24);
            }
        }
    }
    
    private void handleC3Command(int bits) {
        if (bits <= 135) {
            this.serviceBlockPacket.skipBits(32);
        }
        else if (bits <= 143) {
            this.serviceBlockPacket.skipBits(40);
        }
        else if (bits <= 159) {
            this.serviceBlockPacket.skipBits(2);
            bits = this.serviceBlockPacket.readBits(6);
            this.serviceBlockPacket.skipBits(bits * 8);
        }
    }
    
    private void handleDefineWindow(int bits) {
        final CueBuilder cueBuilder = this.cueBuilders[bits];
        this.serviceBlockPacket.skipBits(2);
        final boolean bit = this.serviceBlockPacket.readBit();
        final boolean bit2 = this.serviceBlockPacket.readBit();
        final boolean bit3 = this.serviceBlockPacket.readBit();
        final int bits2 = this.serviceBlockPacket.readBits(3);
        final boolean bit4 = this.serviceBlockPacket.readBit();
        final int bits3 = this.serviceBlockPacket.readBits(7);
        final int bits4 = this.serviceBlockPacket.readBits(8);
        bits = this.serviceBlockPacket.readBits(4);
        final int bits5 = this.serviceBlockPacket.readBits(4);
        this.serviceBlockPacket.skipBits(2);
        final int bits6 = this.serviceBlockPacket.readBits(6);
        this.serviceBlockPacket.skipBits(2);
        cueBuilder.defineWindow(bit, bit2, bit3, bits2, bit4, bits3, bits4, bits5, bits6, bits, this.serviceBlockPacket.readBits(3), this.serviceBlockPacket.readBits(3));
    }
    
    private void handleG0Character(final int n) {
        if (n == 127) {
            this.currentCueBuilder.append('\u266b');
        }
        else {
            this.currentCueBuilder.append((char)(n & 0xFF));
        }
    }
    
    private void handleG1Character(final int n) {
        this.currentCueBuilder.append((char)(n & 0xFF));
    }
    
    private void handleG2Character(final int i) {
        Label_0523: {
            if (i != 32) {
                if (i != 33) {
                    if (i != 37) {
                        if (i != 42) {
                            if (i != 44) {
                                if (i != 63) {
                                    if (i != 57) {
                                        if (i != 58) {
                                            if (i != 60) {
                                                if (i != 61) {
                                                    switch (i) {
                                                        default: {
                                                            switch (i) {
                                                                default: {
                                                                    final StringBuilder sb = new StringBuilder();
                                                                    sb.append("Invalid G2 character: ");
                                                                    sb.append(i);
                                                                    Log.w("Cea708Decoder", sb.toString());
                                                                    break Label_0523;
                                                                }
                                                                case 127: {
                                                                    this.currentCueBuilder.append('\u250c');
                                                                    break Label_0523;
                                                                }
                                                                case 126: {
                                                                    this.currentCueBuilder.append('\u2518');
                                                                    break Label_0523;
                                                                }
                                                                case 125: {
                                                                    this.currentCueBuilder.append('\u2500');
                                                                    break Label_0523;
                                                                }
                                                                case 124: {
                                                                    this.currentCueBuilder.append('\u2514');
                                                                    break Label_0523;
                                                                }
                                                                case 123: {
                                                                    this.currentCueBuilder.append('\u2510');
                                                                    break Label_0523;
                                                                }
                                                                case 122: {
                                                                    this.currentCueBuilder.append('\u2502');
                                                                    break Label_0523;
                                                                }
                                                                case 121: {
                                                                    this.currentCueBuilder.append('\u215e');
                                                                    break Label_0523;
                                                                }
                                                                case 120: {
                                                                    this.currentCueBuilder.append('\u215d');
                                                                    break Label_0523;
                                                                }
                                                                case 119: {
                                                                    this.currentCueBuilder.append('\u215c');
                                                                    break Label_0523;
                                                                }
                                                                case 118: {
                                                                    this.currentCueBuilder.append('\u215b');
                                                                    break Label_0523;
                                                                }
                                                            }
                                                            break;
                                                        }
                                                        case 53: {
                                                            this.currentCueBuilder.append('\u2022');
                                                            break;
                                                        }
                                                        case 52: {
                                                            this.currentCueBuilder.append('\u201d');
                                                            break;
                                                        }
                                                        case 51: {
                                                            this.currentCueBuilder.append('\u201c');
                                                            break;
                                                        }
                                                        case 50: {
                                                            this.currentCueBuilder.append('\u2019');
                                                            break;
                                                        }
                                                        case 49: {
                                                            this.currentCueBuilder.append('\u2018');
                                                            break;
                                                        }
                                                        case 48: {
                                                            this.currentCueBuilder.append('\u2588');
                                                            break;
                                                        }
                                                    }
                                                }
                                                else {
                                                    this.currentCueBuilder.append('\u2120');
                                                }
                                            }
                                            else {
                                                this.currentCueBuilder.append('\u0153');
                                            }
                                        }
                                        else {
                                            this.currentCueBuilder.append('\u0161');
                                        }
                                    }
                                    else {
                                        this.currentCueBuilder.append('\u2122');
                                    }
                                }
                                else {
                                    this.currentCueBuilder.append('\u0178');
                                }
                            }
                            else {
                                this.currentCueBuilder.append('\u0152');
                            }
                        }
                        else {
                            this.currentCueBuilder.append('\u0160');
                        }
                    }
                    else {
                        this.currentCueBuilder.append('\u2026');
                    }
                }
                else {
                    this.currentCueBuilder.append(' ');
                }
            }
            else {
                this.currentCueBuilder.append(' ');
            }
        }
    }
    
    private void handleG3Character(final int i) {
        if (i == 160) {
            this.currentCueBuilder.append('\u33c4');
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid G3 character: ");
            sb.append(i);
            Log.w("Cea708Decoder", sb.toString());
            this.currentCueBuilder.append('_');
        }
    }
    
    private void handleSetPenAttributes() {
        this.currentCueBuilder.setPenAttributes(this.serviceBlockPacket.readBits(4), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBit(), this.serviceBlockPacket.readBit(), this.serviceBlockPacket.readBits(3), this.serviceBlockPacket.readBits(3));
    }
    
    private void handleSetPenColor() {
        final int argbColorFromCeaColor = CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2));
        final int argbColorFromCeaColor2 = CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2));
        this.serviceBlockPacket.skipBits(2);
        this.currentCueBuilder.setPenColor(argbColorFromCeaColor, argbColorFromCeaColor2, CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2)));
    }
    
    private void handleSetPenLocation() {
        this.serviceBlockPacket.skipBits(4);
        final int bits = this.serviceBlockPacket.readBits(4);
        this.serviceBlockPacket.skipBits(2);
        this.currentCueBuilder.setPenLocation(bits, this.serviceBlockPacket.readBits(6));
    }
    
    private void handleSetWindowAttributes() {
        final int argbColorFromCeaColor = CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2));
        final int bits = this.serviceBlockPacket.readBits(2);
        final int argbColorFromCeaColor2 = CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2));
        int n = bits;
        if (this.serviceBlockPacket.readBit()) {
            n = (bits | 0x4);
        }
        final boolean bit = this.serviceBlockPacket.readBit();
        final int bits2 = this.serviceBlockPacket.readBits(2);
        final int bits3 = this.serviceBlockPacket.readBits(2);
        final int bits4 = this.serviceBlockPacket.readBits(2);
        this.serviceBlockPacket.skipBits(8);
        this.currentCueBuilder.setWindowAttributes(argbColorFromCeaColor, argbColorFromCeaColor2, bit, n, bits2, bits3, bits4);
    }
    
    private void processCurrentPacket() {
        final DtvCcPacket currentDtvCcPacket = this.currentDtvCcPacket;
        final int currentIndex = currentDtvCcPacket.currentIndex;
        if (currentIndex != currentDtvCcPacket.packetSize * 2 - 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("DtvCcPacket ended prematurely; size is ");
            sb.append(this.currentDtvCcPacket.packetSize * 2 - 1);
            sb.append(", but current index is ");
            sb.append(this.currentDtvCcPacket.currentIndex);
            sb.append(" (sequence number ");
            sb.append(this.currentDtvCcPacket.sequenceNumber);
            sb.append("); ignoring packet");
            Log.w("Cea708Decoder", sb.toString());
            return;
        }
        this.serviceBlockPacket.reset(currentDtvCcPacket.packetData, currentIndex);
        final int bits = this.serviceBlockPacket.readBits(3);
        final int bits2 = this.serviceBlockPacket.readBits(5);
        int i;
        if ((i = bits) == 7) {
            this.serviceBlockPacket.skipBits(2);
            final int bits3 = this.serviceBlockPacket.readBits(6);
            if ((i = bits3) < 7) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid extended service number: ");
                sb2.append(bits3);
                Log.w("Cea708Decoder", sb2.toString());
                i = bits3;
            }
        }
        if (bits2 == 0) {
            if (i != 0) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("serviceNumber is non-zero (");
                sb3.append(i);
                sb3.append(") when blockSize is 0");
                Log.w("Cea708Decoder", sb3.toString());
            }
            return;
        }
        if (i != this.selectedServiceNumber) {
            return;
        }
        boolean b = false;
        while (this.serviceBlockPacket.bitsLeft() > 0) {
            final int bits4 = this.serviceBlockPacket.readBits(8);
            if (bits4 != 16) {
                if (bits4 <= 31) {
                    this.handleC0Command(bits4);
                    continue;
                }
                if (bits4 <= 127) {
                    this.handleG0Character(bits4);
                }
                else if (bits4 <= 159) {
                    this.handleC1Command(bits4);
                }
                else {
                    if (bits4 > 255) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("Invalid base command: ");
                        sb4.append(bits4);
                        Log.w("Cea708Decoder", sb4.toString());
                        continue;
                    }
                    this.handleG1Character(bits4);
                }
            }
            else {
                final int bits5 = this.serviceBlockPacket.readBits(8);
                if (bits5 <= 31) {
                    this.handleC2Command(bits5);
                    continue;
                }
                if (bits5 <= 127) {
                    this.handleG2Character(bits5);
                }
                else {
                    if (bits5 <= 159) {
                        this.handleC3Command(bits5);
                        continue;
                    }
                    if (bits5 > 255) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("Invalid extended command: ");
                        sb5.append(bits5);
                        Log.w("Cea708Decoder", sb5.toString());
                        continue;
                    }
                    this.handleG3Character(bits5);
                }
            }
            b = true;
        }
        if (b) {
            this.cues = this.getDisplayCues();
        }
    }
    
    private void resetCueBuilders() {
        for (int i = 0; i < 8; ++i) {
            this.cueBuilders[i].reset();
        }
    }
    
    @Override
    protected Subtitle createSubtitle() {
        final List<Cue> cues = this.cues;
        this.lastCues = cues;
        return new CeaSubtitle(cues);
    }
    
    @Override
    protected void decode(final SubtitleInputBuffer subtitleInputBuffer) {
        this.ccData.reset(subtitleInputBuffer.data.array(), subtitleInputBuffer.data.limit());
        while (this.ccData.bytesLeft() >= 3) {
            final int n = this.ccData.readUnsignedByte() & 0x7;
            final int n2 = n & 0x3;
            boolean b = false;
            final boolean b2 = (n & 0x4) == 0x4;
            final byte b3 = (byte)this.ccData.readUnsignedByte();
            final byte b4 = (byte)this.ccData.readUnsignedByte();
            if (n2 != 2 && n2 != 3) {
                continue;
            }
            if (!b2) {
                continue;
            }
            if (n2 == 3) {
                this.finalizeCurrentPacket();
                int n3;
                if ((n3 = (b3 & 0x3F)) == 0) {
                    n3 = 64;
                }
                this.currentDtvCcPacket = new DtvCcPacket((b3 & 0xC0) >> 6, n3);
                final DtvCcPacket currentDtvCcPacket = this.currentDtvCcPacket;
                currentDtvCcPacket.packetData[currentDtvCcPacket.currentIndex++] = b4;
            }
            else {
                if (n2 == 2) {
                    b = true;
                }
                Assertions.checkArgument(b);
                final DtvCcPacket currentDtvCcPacket2 = this.currentDtvCcPacket;
                if (currentDtvCcPacket2 == null) {
                    Log.e("Cea708Decoder", "Encountered DTVCC_PACKET_DATA before DTVCC_PACKET_START");
                    continue;
                }
                final byte[] packetData = currentDtvCcPacket2.packetData;
                packetData[currentDtvCcPacket2.currentIndex++] = b3;
                packetData[currentDtvCcPacket2.currentIndex++] = b4;
            }
            final DtvCcPacket currentDtvCcPacket3 = this.currentDtvCcPacket;
            if (currentDtvCcPacket3.currentIndex != currentDtvCcPacket3.packetSize * 2 - 1) {
                continue;
            }
            this.finalizeCurrentPacket();
        }
    }
    
    @Override
    public void flush() {
        super.flush();
        this.cues = null;
        this.lastCues = null;
        this.currentWindow = 0;
        this.currentCueBuilder = this.cueBuilders[this.currentWindow];
        this.resetCueBuilders();
        this.currentDtvCcPacket = null;
    }
    
    @Override
    protected boolean isNewSubtitleDataAvailable() {
        return this.cues != this.lastCues;
    }
    
    private static final class CueBuilder
    {
        public static final int COLOR_SOLID_BLACK;
        public static final int COLOR_SOLID_WHITE;
        public static final int COLOR_TRANSPARENT;
        private static final int[] PEN_STYLE_BACKGROUND;
        private static final int[] PEN_STYLE_EDGE_TYPE;
        private static final int[] PEN_STYLE_FONT_STYLE;
        private static final int[] WINDOW_STYLE_FILL;
        private static final int[] WINDOW_STYLE_JUSTIFICATION;
        private static final int[] WINDOW_STYLE_PRINT_DIRECTION;
        private static final int[] WINDOW_STYLE_SCROLL_DIRECTION;
        private static final boolean[] WINDOW_STYLE_WORD_WRAP;
        private int anchorId;
        private int backgroundColor;
        private int backgroundColorStartPosition;
        private final SpannableStringBuilder captionStringBuilder;
        private boolean defined;
        private int foregroundColor;
        private int foregroundColorStartPosition;
        private int horizontalAnchor;
        private int italicsStartPosition;
        private int justification;
        private int penStyleId;
        private int priority;
        private boolean relativePositioning;
        private final List<SpannableString> rolledUpCaptions;
        private int row;
        private int rowCount;
        private boolean rowLock;
        private int underlineStartPosition;
        private int verticalAnchor;
        private boolean visible;
        private int windowFillColor;
        private int windowStyleId;
        
        static {
            COLOR_SOLID_WHITE = getArgbColorFromCeaColor(2, 2, 2, 0);
            COLOR_SOLID_BLACK = getArgbColorFromCeaColor(0, 0, 0, 0);
            COLOR_TRANSPARENT = getArgbColorFromCeaColor(0, 0, 0, 3);
            WINDOW_STYLE_JUSTIFICATION = new int[] { 0, 0, 0, 0, 0, 2, 0 };
            WINDOW_STYLE_PRINT_DIRECTION = new int[] { 0, 0, 0, 0, 0, 0, 2 };
            WINDOW_STYLE_SCROLL_DIRECTION = new int[] { 3, 3, 3, 3, 3, 3, 1 };
            WINDOW_STYLE_WORD_WRAP = new boolean[] { false, false, false, true, true, true, false };
            final int color_SOLID_BLACK = CueBuilder.COLOR_SOLID_BLACK;
            final int color_TRANSPARENT = CueBuilder.COLOR_TRANSPARENT;
            WINDOW_STYLE_FILL = new int[] { color_SOLID_BLACK, color_TRANSPARENT, color_SOLID_BLACK, color_SOLID_BLACK, color_TRANSPARENT, color_SOLID_BLACK, color_SOLID_BLACK };
            PEN_STYLE_FONT_STYLE = new int[] { 0, 1, 2, 3, 4, 3, 4 };
            PEN_STYLE_EDGE_TYPE = new int[] { 0, 0, 0, 0, 0, 3, 3 };
            PEN_STYLE_BACKGROUND = new int[] { color_SOLID_BLACK, color_SOLID_BLACK, color_SOLID_BLACK, color_SOLID_BLACK, color_SOLID_BLACK, color_TRANSPARENT, color_TRANSPARENT };
        }
        
        public CueBuilder() {
            this.rolledUpCaptions = new ArrayList<SpannableString>();
            this.captionStringBuilder = new SpannableStringBuilder();
            this.reset();
        }
        
        public static int getArgbColorFromCeaColor(final int n, final int n2, final int n3) {
            return getArgbColorFromCeaColor(n, n2, n3, 0);
        }
        
        public static int getArgbColorFromCeaColor(int n, int n2, final int n3, int n4) {
            int n5 = 0;
            Assertions.checkIndex(n, 0, 4);
            Assertions.checkIndex(n2, 0, 4);
            Assertions.checkIndex(n3, 0, 4);
            Assertions.checkIndex(n4, 0, 4);
            Label_0065: {
                if (n4 != 0 && n4 != 1) {
                    if (n4 == 2) {
                        n4 = 127;
                        break Label_0065;
                    }
                    if (n4 == 3) {
                        n4 = 0;
                        break Label_0065;
                    }
                }
                n4 = 255;
            }
            if (n > 1) {
                n = 255;
            }
            else {
                n = 0;
            }
            if (n2 > 1) {
                n2 = 255;
            }
            else {
                n2 = 0;
            }
            if (n3 > 1) {
                n5 = 255;
            }
            return Color.argb(n4, n, n2, n5);
        }
        
        public void append(final char c) {
            if (c == '\n') {
                this.rolledUpCaptions.add(this.buildSpannableString());
                this.captionStringBuilder.clear();
                if (this.italicsStartPosition != -1) {
                    this.italicsStartPosition = 0;
                }
                if (this.underlineStartPosition != -1) {
                    this.underlineStartPosition = 0;
                }
                if (this.foregroundColorStartPosition != -1) {
                    this.foregroundColorStartPosition = 0;
                }
                if (this.backgroundColorStartPosition != -1) {
                    this.backgroundColorStartPosition = 0;
                }
                while ((this.rowLock && this.rolledUpCaptions.size() >= this.rowCount) || this.rolledUpCaptions.size() >= 15) {
                    this.rolledUpCaptions.remove(0);
                }
            }
            else {
                this.captionStringBuilder.append(c);
            }
        }
        
        public void backspace() {
            final int length = this.captionStringBuilder.length();
            if (length > 0) {
                this.captionStringBuilder.delete(length - 1, length);
            }
        }
        
        public Cea708Cue build() {
            if (this.isEmpty()) {
                return null;
            }
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            boolean b = false;
            for (int i = 0; i < this.rolledUpCaptions.size(); ++i) {
                spannableStringBuilder.append((CharSequence)this.rolledUpCaptions.get(i));
                spannableStringBuilder.append('\n');
            }
            spannableStringBuilder.append((CharSequence)this.buildSpannableString());
            final int justification = this.justification;
            Layout$Alignment layout$Alignment = null;
            Label_0162: {
                if (justification != 0) {
                    if (justification == 1) {
                        layout$Alignment = Layout$Alignment.ALIGN_OPPOSITE;
                        break Label_0162;
                    }
                    if (justification == 2) {
                        layout$Alignment = Layout$Alignment.ALIGN_CENTER;
                        break Label_0162;
                    }
                    if (justification != 3) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unexpected justification value: ");
                        sb.append(this.justification);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
                layout$Alignment = Layout$Alignment.ALIGN_NORMAL;
            }
            float n;
            float n2;
            if (this.relativePositioning) {
                n = this.horizontalAnchor / 99.0f;
                n2 = this.verticalAnchor / 99.0f;
            }
            else {
                n = this.horizontalAnchor / 209.0f;
                n2 = this.verticalAnchor / 74.0f;
            }
            final int anchorId = this.anchorId;
            int n3;
            if (anchorId % 3 == 0) {
                n3 = 0;
            }
            else if (anchorId % 3 == 1) {
                n3 = 1;
            }
            else {
                n3 = 2;
            }
            final int anchorId2 = this.anchorId;
            int n4;
            if (anchorId2 / 3 == 0) {
                n4 = 0;
            }
            else if (anchorId2 / 3 == 1) {
                n4 = 1;
            }
            else {
                n4 = 2;
            }
            if (this.windowFillColor != CueBuilder.COLOR_SOLID_BLACK) {
                b = true;
            }
            return new Cea708Cue((CharSequence)spannableStringBuilder, layout$Alignment, n2 * 0.9f + 0.05f, 0, n3, n * 0.9f + 0.05f, n4, Float.MIN_VALUE, b, this.windowFillColor, this.priority);
        }
        
        public SpannableString buildSpannableString() {
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)this.captionStringBuilder);
            final int length = spannableStringBuilder.length();
            if (length > 0) {
                if (this.italicsStartPosition != -1) {
                    spannableStringBuilder.setSpan((Object)new StyleSpan(2), this.italicsStartPosition, length, 33);
                }
                if (this.underlineStartPosition != -1) {
                    spannableStringBuilder.setSpan((Object)new UnderlineSpan(), this.underlineStartPosition, length, 33);
                }
                if (this.foregroundColorStartPosition != -1) {
                    spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(this.foregroundColor), this.foregroundColorStartPosition, length, 33);
                }
                if (this.backgroundColorStartPosition != -1) {
                    spannableStringBuilder.setSpan((Object)new BackgroundColorSpan(this.backgroundColor), this.backgroundColorStartPosition, length, 33);
                }
            }
            return new SpannableString((CharSequence)spannableStringBuilder);
        }
        
        public void clear() {
            this.rolledUpCaptions.clear();
            this.captionStringBuilder.clear();
            this.italicsStartPosition = -1;
            this.underlineStartPosition = -1;
            this.foregroundColorStartPosition = -1;
            this.backgroundColorStartPosition = -1;
            this.row = 0;
        }
        
        public void defineWindow(final boolean visible, final boolean rowLock, final boolean b, int rowCount, final boolean relativePositioning, int n, final int horizontalAnchor, final int n2, final int n3, final int anchorId, final int windowStyleId, final int penStyleId) {
            this.defined = true;
            this.visible = visible;
            this.rowLock = rowLock;
            this.priority = rowCount;
            this.relativePositioning = relativePositioning;
            this.verticalAnchor = n;
            this.horizontalAnchor = horizontalAnchor;
            this.anchorId = anchorId;
            rowCount = this.rowCount;
            n = n2 + 1;
            if (rowCount != n) {
                this.rowCount = n;
                while ((rowLock && this.rolledUpCaptions.size() >= this.rowCount) || this.rolledUpCaptions.size() >= 15) {
                    this.rolledUpCaptions.remove(0);
                }
            }
            if (windowStyleId != 0 && this.windowStyleId != windowStyleId) {
                this.windowStyleId = windowStyleId;
                rowCount = windowStyleId - 1;
                this.setWindowAttributes(CueBuilder.WINDOW_STYLE_FILL[rowCount], CueBuilder.COLOR_TRANSPARENT, CueBuilder.WINDOW_STYLE_WORD_WRAP[rowCount], 0, CueBuilder.WINDOW_STYLE_PRINT_DIRECTION[rowCount], CueBuilder.WINDOW_STYLE_SCROLL_DIRECTION[rowCount], CueBuilder.WINDOW_STYLE_JUSTIFICATION[rowCount]);
            }
            if (penStyleId != 0 && this.penStyleId != penStyleId) {
                this.penStyleId = penStyleId;
                rowCount = penStyleId - 1;
                this.setPenAttributes(0, 1, 1, false, false, CueBuilder.PEN_STYLE_EDGE_TYPE[rowCount], CueBuilder.PEN_STYLE_FONT_STYLE[rowCount]);
                this.setPenColor(CueBuilder.COLOR_SOLID_WHITE, CueBuilder.PEN_STYLE_BACKGROUND[rowCount], CueBuilder.COLOR_SOLID_BLACK);
            }
        }
        
        public boolean isDefined() {
            return this.defined;
        }
        
        public boolean isEmpty() {
            return !this.isDefined() || (this.rolledUpCaptions.isEmpty() && this.captionStringBuilder.length() == 0);
        }
        
        public boolean isVisible() {
            return this.visible;
        }
        
        public void reset() {
            this.clear();
            this.defined = false;
            this.visible = false;
            this.priority = 4;
            this.relativePositioning = false;
            this.verticalAnchor = 0;
            this.horizontalAnchor = 0;
            this.anchorId = 0;
            this.rowCount = 15;
            this.rowLock = true;
            this.justification = 0;
            this.windowStyleId = 0;
            this.penStyleId = 0;
            final int color_SOLID_BLACK = CueBuilder.COLOR_SOLID_BLACK;
            this.windowFillColor = color_SOLID_BLACK;
            this.foregroundColor = CueBuilder.COLOR_SOLID_WHITE;
            this.backgroundColor = color_SOLID_BLACK;
        }
        
        public void setPenAttributes(final int n, final int n2, final int n3, final boolean b, final boolean b2, final int n4, final int n5) {
            if (this.italicsStartPosition != -1) {
                if (!b) {
                    this.captionStringBuilder.setSpan((Object)new StyleSpan(2), this.italicsStartPosition, this.captionStringBuilder.length(), 33);
                    this.italicsStartPosition = -1;
                }
            }
            else if (b) {
                this.italicsStartPosition = this.captionStringBuilder.length();
            }
            if (this.underlineStartPosition != -1) {
                if (!b2) {
                    this.captionStringBuilder.setSpan((Object)new UnderlineSpan(), this.underlineStartPosition, this.captionStringBuilder.length(), 33);
                    this.underlineStartPosition = -1;
                }
            }
            else if (b2) {
                this.underlineStartPosition = this.captionStringBuilder.length();
            }
        }
        
        public void setPenColor(int backgroundColor, final int backgroundColor2, int foregroundColor) {
            if (this.foregroundColorStartPosition != -1) {
                foregroundColor = this.foregroundColor;
                if (foregroundColor != backgroundColor) {
                    this.captionStringBuilder.setSpan((Object)new ForegroundColorSpan(foregroundColor), this.foregroundColorStartPosition, this.captionStringBuilder.length(), 33);
                }
            }
            if (backgroundColor != CueBuilder.COLOR_SOLID_WHITE) {
                this.foregroundColorStartPosition = this.captionStringBuilder.length();
                this.foregroundColor = backgroundColor;
            }
            if (this.backgroundColorStartPosition != -1) {
                backgroundColor = this.backgroundColor;
                if (backgroundColor != backgroundColor2) {
                    this.captionStringBuilder.setSpan((Object)new BackgroundColorSpan(backgroundColor), this.backgroundColorStartPosition, this.captionStringBuilder.length(), 33);
                }
            }
            if (backgroundColor2 != CueBuilder.COLOR_SOLID_BLACK) {
                this.backgroundColorStartPosition = this.captionStringBuilder.length();
                this.backgroundColor = backgroundColor2;
            }
        }
        
        public void setPenLocation(final int row, final int n) {
            if (this.row != row) {
                this.append('\n');
            }
            this.row = row;
        }
        
        public void setVisibility(final boolean visible) {
            this.visible = visible;
        }
        
        public void setWindowAttributes(final int windowFillColor, final int n, final boolean b, final int n2, final int n3, final int n4, final int justification) {
            this.windowFillColor = windowFillColor;
            this.justification = justification;
        }
    }
    
    private static final class DtvCcPacket
    {
        int currentIndex;
        public final byte[] packetData;
        public final int packetSize;
        public final int sequenceNumber;
        
        public DtvCcPacket(final int sequenceNumber, final int packetSize) {
            this.sequenceNumber = sequenceNumber;
            this.packetSize = packetSize;
            this.packetData = new byte[packetSize * 2 - 1];
            this.currentIndex = 0;
        }
    }
}

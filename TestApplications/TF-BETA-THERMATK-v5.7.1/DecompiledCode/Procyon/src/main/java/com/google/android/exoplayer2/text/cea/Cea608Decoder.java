// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.cea;

import android.text.Layout$Alignment;
import android.text.style.UnderlineSpan;
import android.text.style.StyleSpan;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableStringBuilder;
import android.text.SpannableString;
import com.google.android.exoplayer2.text.SubtitleOutputBuffer;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.text.Subtitle;
import java.util.Collections;
import com.google.android.exoplayer2.text.Cue;
import java.util.List;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.ParsableByteArray;

public final class Cea608Decoder extends CeaDecoder
{
    private static final int[] BASIC_CHARACTER_SET;
    private static final int[] COLUMN_INDICES;
    private static final boolean[] ODD_PARITY_BYTE_TABLE;
    private static final int[] ROW_INDICES;
    private static final int[] SPECIAL_CHARACTER_SET;
    private static final int[] SPECIAL_ES_FR_CHARACTER_SET;
    private static final int[] SPECIAL_PT_DE_CHARACTER_SET;
    private static final int[] STYLE_COLORS;
    private int captionMode;
    private int captionRowCount;
    private boolean captionValid;
    private final ParsableByteArray ccData;
    private final ArrayList<CueBuilder> cueBuilders;
    private List<Cue> cues;
    private CueBuilder currentCueBuilder;
    private List<Cue> lastCues;
    private final int packetLength;
    private byte repeatableControlCc1;
    private byte repeatableControlCc2;
    private boolean repeatableControlSet;
    private final int selectedField;
    
    static {
        ROW_INDICES = new int[] { 11, 1, 3, 12, 14, 5, 7, 9 };
        COLUMN_INDICES = new int[] { 0, 4, 8, 12, 16, 20, 24, 28 };
        STYLE_COLORS = new int[] { -1, -16711936, -16776961, -16711681, -65536, -256, -65281 };
        BASIC_CHARACTER_SET = new int[] { 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 225, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 233, 93, 237, 243, 250, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 231, 247, 209, 241, 9632 };
        SPECIAL_CHARACTER_SET = new int[] { 174, 176, 189, 191, 8482, 162, 163, 9834, 224, 32, 232, 226, 234, 238, 244, 251 };
        SPECIAL_ES_FR_CHARACTER_SET = new int[] { 193, 201, 211, 218, 220, 252, 8216, 161, 42, 39, 8212, 169, 8480, 8226, 8220, 8221, 192, 194, 199, 200, 202, 203, 235, 206, 207, 239, 212, 217, 249, 219, 171, 187 };
        SPECIAL_PT_DE_CHARACTER_SET = new int[] { 195, 227, 205, 204, 236, 210, 242, 213, 245, 123, 125, 92, 94, 95, 124, 126, 196, 228, 214, 246, 223, 165, 164, 9474, 197, 229, 216, 248, 9484, 9488, 9492, 9496 };
        ODD_PARITY_BYTE_TABLE = new boolean[] { false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false };
    }
    
    public Cea608Decoder(final String anObject, final int n) {
        this.ccData = new ParsableByteArray();
        this.cueBuilders = new ArrayList<CueBuilder>();
        this.currentCueBuilder = new CueBuilder(0, 4);
        int packetLength;
        if ("application/x-mp4-cea-608".equals(anObject)) {
            packetLength = 2;
        }
        else {
            packetLength = 3;
        }
        this.packetLength = packetLength;
        if (n != 3 && n != 4) {
            this.selectedField = 1;
        }
        else {
            this.selectedField = 2;
        }
        this.setCaptionMode(0);
        this.resetCueBuilders();
    }
    
    private static char getChar(final byte b) {
        return (char)Cea608Decoder.BASIC_CHARACTER_SET[(b & 0x7F) - 32];
    }
    
    private List<Cue> getDisplayCues() {
        final int size = this.cueBuilders.size();
        final ArrayList list = new ArrayList<Cue>(size);
        final int n = 0;
        int i = 0;
        int a = 2;
        while (i < size) {
            final Cue build = this.cueBuilders.get(i).build(Integer.MIN_VALUE);
            list.add(build);
            int min = a;
            if (build != null) {
                min = Math.min(a, build.positionAnchor);
            }
            ++i;
            a = min;
        }
        final ArrayList list2 = new ArrayList<Cue>(size);
        for (int j = n; j < size; ++j) {
            final Cue cue = list.get(j);
            if (cue != null) {
                Cue build2 = cue;
                if (cue.positionAnchor != a) {
                    build2 = this.cueBuilders.get(j).build(a);
                }
                list2.add(build2);
            }
        }
        return (List<Cue>)list2;
    }
    
    private static char getExtendedEsFrChar(final byte b) {
        return (char)Cea608Decoder.SPECIAL_ES_FR_CHARACTER_SET[b & 0x1F];
    }
    
    private static char getExtendedPtDeChar(final byte b) {
        return (char)Cea608Decoder.SPECIAL_PT_DE_CHARACTER_SET[b & 0x1F];
    }
    
    private static char getSpecialChar(final byte b) {
        return (char)Cea608Decoder.SPECIAL_CHARACTER_SET[b & 0xF];
    }
    
    private void handleCtrl(final byte repeatableControlCc1, final byte repeatableControlCc2, final boolean b) {
        if (isRepeatable(repeatableControlCc1)) {
            if (b && this.repeatableControlCc1 == repeatableControlCc1 && this.repeatableControlCc2 == repeatableControlCc2) {
                return;
            }
            this.repeatableControlSet = true;
            this.repeatableControlCc1 = repeatableControlCc1;
            this.repeatableControlCc2 = repeatableControlCc2;
        }
        if (isMidrowCtrlCode(repeatableControlCc1, repeatableControlCc2)) {
            this.handleMidrowCtrl(repeatableControlCc2);
        }
        else if (isPreambleAddressCode(repeatableControlCc1, repeatableControlCc2)) {
            this.handlePreambleAddressCode(repeatableControlCc1, repeatableControlCc2);
        }
        else if (isTabCtrlCode(repeatableControlCc1, repeatableControlCc2)) {
            this.currentCueBuilder.tabOffset = repeatableControlCc2 - 32;
        }
        else if (isMiscCode(repeatableControlCc1, repeatableControlCc2)) {
            this.handleMiscCode(repeatableControlCc2);
        }
    }
    
    private void handleMidrowCtrl(final byte b) {
        this.currentCueBuilder.append(' ');
        this.currentCueBuilder.setStyle(b >> 1 & 0x7, (b & 0x1) == 0x1);
    }
    
    private void handleMiscCode(final byte b) {
        if (b == 32) {
            this.setCaptionMode(2);
            return;
        }
        if (b == 41) {
            this.setCaptionMode(3);
            return;
        }
        switch (b) {
            default: {
                final int captionMode = this.captionMode;
                if (captionMode == 0) {
                    return;
                }
                if (b != 33) {
                    if (b != 36) {
                        switch (b) {
                            case 47: {
                                this.cues = this.getDisplayCues();
                                this.resetCueBuilders();
                                break;
                            }
                            case 46: {
                                this.resetCueBuilders();
                                break;
                            }
                            case 45: {
                                if (captionMode == 1 && !this.currentCueBuilder.isEmpty()) {
                                    this.currentCueBuilder.rollUp();
                                    break;
                                }
                                break;
                            }
                            case 44: {
                                this.cues = Collections.emptyList();
                                final int captionMode2 = this.captionMode;
                                if (captionMode2 == 1 || captionMode2 == 3) {
                                    this.resetCueBuilders();
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
                else {
                    this.currentCueBuilder.backspace();
                }
            }
            case 39: {
                this.setCaptionMode(1);
                this.setCaptionRowCount(4);
            }
            case 38: {
                this.setCaptionMode(1);
                this.setCaptionRowCount(3);
            }
            case 37: {
                this.setCaptionMode(1);
                this.setCaptionRowCount(2);
            }
        }
    }
    
    private void handlePreambleAddressCode(final byte b, final byte b2) {
        final int n = Cea608Decoder.ROW_INDICES[b & 0x7];
        boolean b3 = false;
        final boolean b4 = (b2 & 0x20) != 0x0;
        int n2 = n;
        if (b4) {
            n2 = n + 1;
        }
        if (n2 != this.currentCueBuilder.row) {
            if (this.captionMode != 1 && !this.currentCueBuilder.isEmpty()) {
                this.currentCueBuilder = new CueBuilder(this.captionMode, this.captionRowCount);
                this.cueBuilders.add(this.currentCueBuilder);
            }
            this.currentCueBuilder.row = n2;
        }
        final boolean b5 = (b2 & 0x10) == 0x10;
        if ((b2 & 0x1) == 0x1) {
            b3 = true;
        }
        final int n3 = b2 >> 1 & 0x7;
        final CueBuilder currentCueBuilder = this.currentCueBuilder;
        int n4;
        if (b5) {
            n4 = 8;
        }
        else {
            n4 = n3;
        }
        currentCueBuilder.setStyle(n4, b3);
        if (b5) {
            this.currentCueBuilder.indent = Cea608Decoder.COLUMN_INDICES[n3];
        }
    }
    
    private static boolean isMidrowCtrlCode(final byte b, final byte b2) {
        return (b & 0xF7) == 0x11 && (b2 & 0xF0) == 0x20;
    }
    
    private static boolean isMiscCode(final byte b, final byte b2) {
        return (b & 0xF7) == 0x14 && (b2 & 0xF0) == 0x20;
    }
    
    private static boolean isPreambleAddressCode(final byte b, final byte b2) {
        return (b & 0xF0) == 0x10 && (b2 & 0xC0) == 0x40;
    }
    
    private static boolean isRepeatable(final byte b) {
        return (b & 0xF0) == 0x10;
    }
    
    private static boolean isTabCtrlCode(final byte b, final byte b2) {
        return (b & 0xF7) == 0x17 && b2 >= 33 && b2 <= 35;
    }
    
    private void resetCueBuilders() {
        this.currentCueBuilder.reset(this.captionMode);
        this.cueBuilders.clear();
        this.cueBuilders.add(this.currentCueBuilder);
    }
    
    private void setCaptionMode(final int n) {
        final int captionMode = this.captionMode;
        if (captionMode == n) {
            return;
        }
        if ((this.captionMode = n) == 3) {
            for (int i = 0; i < this.cueBuilders.size(); ++i) {
                this.cueBuilders.get(i).setCaptionMode(n);
            }
            return;
        }
        this.resetCueBuilders();
        if (captionMode == 3 || n == 1 || n == 0) {
            this.cues = Collections.emptyList();
        }
    }
    
    private void setCaptionRowCount(final int n) {
        this.captionRowCount = n;
        this.currentCueBuilder.setCaptionRowCount(n);
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
        boolean b = false;
        while (true) {
            final int bytesLeft = this.ccData.bytesLeft();
            final int packetLength = this.packetLength;
            if (bytesLeft < packetLength) {
                break;
            }
            int n;
            if (packetLength == 2) {
                n = -4;
            }
            else {
                n = (byte)this.ccData.readUnsignedByte();
            }
            final int unsignedByte = this.ccData.readUnsignedByte();
            final int unsignedByte2 = this.ccData.readUnsignedByte();
            if ((n & 0x2) != 0x0) {
                continue;
            }
            if (this.selectedField == 1 && (n & 0x1) != 0x0) {
                continue;
            }
            if (this.selectedField == 2 && (n & 0x1) != 0x1) {
                continue;
            }
            final byte b2 = (byte)(unsignedByte & 0x7F);
            final byte b3 = (byte)(unsignedByte2 & 0x7F);
            if (b2 == 0 && b3 == 0) {
                continue;
            }
            final boolean repeatableControlSet = this.repeatableControlSet;
            this.repeatableControlSet = false;
            final boolean captionValid = this.captionValid;
            if (!(this.captionValid = ((n & 0x4) == 0x4))) {
                if (!captionValid) {
                    continue;
                }
                this.resetCueBuilders();
            }
            else {
                final boolean[] odd_PARITY_BYTE_TABLE = Cea608Decoder.ODD_PARITY_BYTE_TABLE;
                if (odd_PARITY_BYTE_TABLE[unsignedByte] && odd_PARITY_BYTE_TABLE[unsignedByte2]) {
                    if ((b2 & 0xF7) == 0x11 && (b3 & 0xF0) == 0x30) {
                        this.currentCueBuilder.append(getSpecialChar(b3));
                    }
                    else if ((b2 & 0xF6) == 0x12 && (b3 & 0xE0) == 0x20) {
                        this.currentCueBuilder.backspace();
                        if ((b2 & 0x1) == 0x0) {
                            this.currentCueBuilder.append(getExtendedEsFrChar(b3));
                        }
                        else {
                            this.currentCueBuilder.append(getExtendedPtDeChar(b3));
                        }
                    }
                    else if ((b2 & 0xE0) == 0x0) {
                        this.handleCtrl(b2, b3, repeatableControlSet);
                    }
                    else {
                        this.currentCueBuilder.append(getChar(b2));
                        if ((b3 & 0xE0) != 0x0) {
                            this.currentCueBuilder.append(getChar(b3));
                        }
                    }
                }
                else {
                    this.resetCueBuilders();
                }
            }
            b = true;
        }
        if (b) {
            final int captionMode = this.captionMode;
            if (captionMode == 1 || captionMode == 3) {
                this.cues = this.getDisplayCues();
            }
        }
    }
    
    @Override
    public void flush() {
        super.flush();
        this.cues = null;
        this.lastCues = null;
        this.setCaptionMode(0);
        this.setCaptionRowCount(4);
        this.resetCueBuilders();
        this.captionValid = false;
        this.repeatableControlSet = false;
        this.repeatableControlCc1 = 0;
        this.repeatableControlCc2 = 0;
    }
    
    @Override
    protected boolean isNewSubtitleDataAvailable() {
        return this.cues != this.lastCues;
    }
    
    @Override
    public void release() {
    }
    
    private static class CueBuilder
    {
        private int captionMode;
        private int captionRowCount;
        private final StringBuilder captionStringBuilder;
        private final List<CueStyle> cueStyles;
        private int indent;
        private final List<SpannableString> rolledUpCaptions;
        private int row;
        private int tabOffset;
        
        public CueBuilder(final int n, final int captionRowCount) {
            this.cueStyles = new ArrayList<CueStyle>();
            this.rolledUpCaptions = new ArrayList<SpannableString>();
            this.captionStringBuilder = new StringBuilder();
            this.reset(n);
            this.setCaptionRowCount(captionRowCount);
        }
        
        private SpannableString buildCurrentLine() {
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)this.captionStringBuilder);
            final int length = spannableStringBuilder.length();
            int i = 0;
            int n = -1;
            int n2 = -1;
            int n3 = 0;
            int n4 = -1;
            int n5 = -1;
            int n6 = 0;
            while (i < this.cueStyles.size()) {
                final CueStyle cueStyle = this.cueStyles.get(i);
                final boolean underline = cueStyle.underline;
                final int style = cueStyle.style;
                int n7 = n5;
                int n8 = n6;
                if (style != 8) {
                    int n9;
                    if (style == 7) {
                        n9 = 1;
                    }
                    else {
                        n9 = 0;
                    }
                    if (style != 7) {
                        n5 = Cea608Decoder.STYLE_COLORS[style];
                    }
                    n8 = n9;
                    n7 = n5;
                }
                final int start = cueStyle.start;
                final int n10 = i + 1;
                int start2;
                if (n10 < this.cueStyles.size()) {
                    start2 = this.cueStyles.get(n10).start;
                }
                else {
                    start2 = length;
                }
                if (start == start2) {
                    i = n10;
                    n5 = n7;
                    n6 = n8;
                }
                else {
                    int n11;
                    if (n != -1 && !underline) {
                        setUnderlineSpan(spannableStringBuilder, n, start);
                        n11 = -1;
                    }
                    else if ((n11 = n) == -1) {
                        n11 = n;
                        if (underline) {
                            n11 = start;
                        }
                    }
                    int n12;
                    if (n2 != -1 && n8 == 0) {
                        setItalicSpan(spannableStringBuilder, n2, start);
                        n12 = -1;
                    }
                    else if ((n12 = n2) == -1) {
                        n12 = n2;
                        if (n8 != 0) {
                            n12 = start;
                        }
                    }
                    i = n10;
                    n = n11;
                    n2 = n12;
                    n5 = n7;
                    n6 = n8;
                    if (n7 == n4) {
                        continue;
                    }
                    setColorSpan(spannableStringBuilder, n3, start, n4);
                    n4 = n7;
                    i = n10;
                    n = n11;
                    n2 = n12;
                    n3 = start;
                    n5 = n7;
                    n6 = n8;
                }
            }
            if (n != -1 && n != length) {
                setUnderlineSpan(spannableStringBuilder, n, length);
            }
            if (n2 != -1 && n2 != length) {
                setItalicSpan(spannableStringBuilder, n2, length);
            }
            if (n3 != length) {
                setColorSpan(spannableStringBuilder, n3, length, n4);
            }
            return new SpannableString((CharSequence)spannableStringBuilder);
        }
        
        private static void setColorSpan(final SpannableStringBuilder spannableStringBuilder, final int n, final int n2, final int n3) {
            if (n3 == -1) {
                return;
            }
            spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(n3), n, n2, 33);
        }
        
        private static void setItalicSpan(final SpannableStringBuilder spannableStringBuilder, final int n, final int n2) {
            spannableStringBuilder.setSpan((Object)new StyleSpan(2), n, n2, 33);
        }
        
        private static void setUnderlineSpan(final SpannableStringBuilder spannableStringBuilder, final int n, final int n2) {
            spannableStringBuilder.setSpan((Object)new UnderlineSpan(), n, n2, 33);
        }
        
        public void append(final char c) {
            this.captionStringBuilder.append(c);
        }
        
        public void backspace() {
            final int length = this.captionStringBuilder.length();
            if (length > 0) {
                this.captionStringBuilder.delete(length - 1, length);
                for (int i = this.cueStyles.size() - 1; i >= 0; --i) {
                    final CueStyle cueStyle = this.cueStyles.get(i);
                    final int start = cueStyle.start;
                    if (start != length) {
                        break;
                    }
                    cueStyle.start = start - 1;
                }
            }
        }
        
        public Cue build(int n) {
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            for (int i = 0; i < this.rolledUpCaptions.size(); ++i) {
                spannableStringBuilder.append((CharSequence)this.rolledUpCaptions.get(i));
                spannableStringBuilder.append('\n');
            }
            spannableStringBuilder.append((CharSequence)this.buildCurrentLine());
            if (spannableStringBuilder.length() == 0) {
                return null;
            }
            int n2 = this.indent + this.tabOffset;
            final int n3 = 32 - n2 - spannableStringBuilder.length();
            final int a = n2 - n3;
            if (n == Integer.MIN_VALUE) {
                if (this.captionMode == 2 && (Math.abs(a) < 3 || n3 < 0)) {
                    n = 1;
                }
                else if (this.captionMode == 2 && a > 0) {
                    n = 2;
                }
                else {
                    n = 0;
                }
            }
            float n4;
            if (n != 1) {
                if (n == 2) {
                    n2 = 32 - n3;
                }
                n4 = n2 / 32.0f * 0.8f + 0.1f;
            }
            else {
                n4 = 0.5f;
            }
            if (this.captionMode != 1) {
                final int row = this.row;
                if (row <= 7) {
                    final int n5 = 0;
                    return new Cue((CharSequence)spannableStringBuilder, Layout$Alignment.ALIGN_NORMAL, (float)row, 1, n5, n4, n, Float.MIN_VALUE);
                }
            }
            final int row = this.row - 15 - 2;
            final int n5 = 2;
            return new Cue((CharSequence)spannableStringBuilder, Layout$Alignment.ALIGN_NORMAL, (float)row, 1, n5, n4, n, Float.MIN_VALUE);
        }
        
        public boolean isEmpty() {
            return this.cueStyles.isEmpty() && this.rolledUpCaptions.isEmpty() && this.captionStringBuilder.length() == 0;
        }
        
        public void reset(final int captionMode) {
            this.captionMode = captionMode;
            this.cueStyles.clear();
            this.rolledUpCaptions.clear();
            this.captionStringBuilder.setLength(0);
            this.row = 15;
            this.indent = 0;
            this.tabOffset = 0;
        }
        
        public void rollUp() {
            this.rolledUpCaptions.add(this.buildCurrentLine());
            this.captionStringBuilder.setLength(0);
            this.cueStyles.clear();
            while (this.rolledUpCaptions.size() >= Math.min(this.captionRowCount, this.row)) {
                this.rolledUpCaptions.remove(0);
            }
        }
        
        public void setCaptionMode(final int captionMode) {
            this.captionMode = captionMode;
        }
        
        public void setCaptionRowCount(final int captionRowCount) {
            this.captionRowCount = captionRowCount;
        }
        
        public void setStyle(final int n, final boolean b) {
            this.cueStyles.add(new CueStyle(n, b, this.captionStringBuilder.length()));
        }
        
        private static class CueStyle
        {
            public int start;
            public final int style;
            public final boolean underline;
            
            public CueStyle(final int style, final boolean underline, final int start) {
                this.style = style;
                this.underline = underline;
                this.start = start;
            }
        }
    }
}

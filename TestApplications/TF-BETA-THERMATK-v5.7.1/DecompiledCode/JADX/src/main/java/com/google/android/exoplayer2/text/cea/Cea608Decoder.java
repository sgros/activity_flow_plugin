package com.google.android.exoplayer2.text.cea;

import android.text.Layout.Alignment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.text.SubtitleOutputBuffer;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Cea608Decoder extends CeaDecoder {
    private static final int[] BASIC_CHARACTER_SET = new int[]{32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 225, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 233, 93, 237, 243, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 231, 247, 209, 241, 9632};
    private static final int[] COLUMN_INDICES = new int[]{0, 4, 8, 12, 16, 20, 24, 28};
    private static final boolean[] ODD_PARITY_BYTE_TABLE = new boolean[]{false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false};
    private static final int[] ROW_INDICES = new int[]{11, 1, 3, 12, 14, 5, 7, 9};
    private static final int[] SPECIAL_CHARACTER_SET = new int[]{174, 176, 189, 191, 8482, 162, 163, 9834, 224, 32, 232, 226, 234, 238, 244, 251};
    private static final int[] SPECIAL_ES_FR_CHARACTER_SET = new int[]{193, 201, 211, 218, 220, 252, 8216, 161, 42, 39, 8212, 169, 8480, 8226, 8220, 8221, 192, 194, 199, Callback.DEFAULT_DRAG_ANIMATION_DURATION, 202, 203, 235, 206, 207, 239, 212, 217, 249, 219, 171, 187};
    private static final int[] SPECIAL_PT_DE_CHARACTER_SET = new int[]{195, 227, 205, 204, 236, 210, 242, 213, 245, 123, 125, 92, 94, 95, 124, 126, 196, 228, 214, 246, 223, 165, 164, 9474, 197, 229, 216, 248, 9484, 9488, 9492, 9496};
    private static final int[] STYLE_COLORS = new int[]{-1, -16711936, -16776961, -16711681, -65536, -256, -65281};
    private int captionMode;
    private int captionRowCount;
    private boolean captionValid;
    private final ParsableByteArray ccData = new ParsableByteArray();
    private final ArrayList<CueBuilder> cueBuilders = new ArrayList();
    private List<Cue> cues;
    private CueBuilder currentCueBuilder = new CueBuilder(0, 4);
    private List<Cue> lastCues;
    private final int packetLength;
    private byte repeatableControlCc1;
    private byte repeatableControlCc2;
    private boolean repeatableControlSet;
    private final int selectedField;

    private static class CueBuilder {
        private int captionMode;
        private int captionRowCount;
        private final StringBuilder captionStringBuilder = new StringBuilder();
        private final List<CueStyle> cueStyles = new ArrayList();
        private int indent;
        private final List<SpannableString> rolledUpCaptions = new ArrayList();
        private int row;
        private int tabOffset;

        private static class CueStyle {
            public int start;
            public final int style;
            public final boolean underline;

            public CueStyle(int i, boolean z, int i2) {
                this.style = i;
                this.underline = z;
                this.start = i2;
            }
        }

        public CueBuilder(int i, int i2) {
            reset(i);
            setCaptionRowCount(i2);
        }

        public void reset(int i) {
            this.captionMode = i;
            this.cueStyles.clear();
            this.rolledUpCaptions.clear();
            this.captionStringBuilder.setLength(0);
            this.row = 15;
            this.indent = 0;
            this.tabOffset = 0;
        }

        public boolean isEmpty() {
            return this.cueStyles.isEmpty() && this.rolledUpCaptions.isEmpty() && this.captionStringBuilder.length() == 0;
        }

        public void setCaptionMode(int i) {
            this.captionMode = i;
        }

        public void setCaptionRowCount(int i) {
            this.captionRowCount = i;
        }

        public void setStyle(int i, boolean z) {
            this.cueStyles.add(new CueStyle(i, z, this.captionStringBuilder.length()));
        }

        public void backspace() {
            int length = this.captionStringBuilder.length();
            if (length > 0) {
                this.captionStringBuilder.delete(length - 1, length);
                int size = this.cueStyles.size() - 1;
                while (size >= 0) {
                    CueStyle cueStyle = (CueStyle) this.cueStyles.get(size);
                    int i = cueStyle.start;
                    if (i == length) {
                        cueStyle.start = i - 1;
                        size--;
                    } else {
                        return;
                    }
                }
            }
        }

        public void append(char c) {
            this.captionStringBuilder.append(c);
        }

        public void rollUp() {
            this.rolledUpCaptions.add(buildCurrentLine());
            this.captionStringBuilder.setLength(0);
            this.cueStyles.clear();
            int min = Math.min(this.captionRowCount, this.row);
            while (this.rolledUpCaptions.size() >= min) {
                this.rolledUpCaptions.remove(0);
            }
        }

        public Cue build(int i) {
            int i2;
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            for (i2 = 0; i2 < this.rolledUpCaptions.size(); i2++) {
                spannableStringBuilder.append((CharSequence) this.rolledUpCaptions.get(i2));
                spannableStringBuilder.append(10);
            }
            spannableStringBuilder.append(buildCurrentLine());
            if (spannableStringBuilder.length() == 0) {
                return null;
            }
            float f;
            int i3;
            i2 = this.indent + this.tabOffset;
            int length = (32 - i2) - spannableStringBuilder.length();
            int i4 = i2 - length;
            if (i == Integer.MIN_VALUE) {
                i = (this.captionMode != 2 || (Math.abs(i4) >= 3 && length >= 0)) ? (this.captionMode != 2 || i4 <= 0) ? 0 : 2 : 1;
            }
            if (i != 1) {
                if (i == 2) {
                    i2 = 32 - length;
                }
                f = ((((float) i2) / 32.0f) * 0.8f) + 0.1f;
            } else {
                f = 0.5f;
            }
            if (this.captionMode != 1) {
                i2 = this.row;
                if (i2 <= 7) {
                    i3 = 0;
                    return new Cue(spannableStringBuilder, Alignment.ALIGN_NORMAL, (float) i2, 1, i3, f, i, Float.MIN_VALUE);
                }
            }
            i2 = (this.row - 15) - 2;
            i3 = 2;
            return new Cue(spannableStringBuilder, Alignment.ALIGN_NORMAL, (float) i2, 1, i3, f, i, Float.MIN_VALUE);
        }

        private SpannableString buildCurrentLine() {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.captionStringBuilder);
            int length = spannableStringBuilder.length();
            int i = 0;
            int i2 = -1;
            int i3 = -1;
            int i4 = 0;
            int i5 = -1;
            int i6 = -1;
            Object obj = null;
            while (i < this.cueStyles.size()) {
                CueStyle cueStyle = (CueStyle) this.cueStyles.get(i);
                boolean z = cueStyle.underline;
                int i7 = cueStyle.style;
                if (i7 != 8) {
                    Object obj2 = i7 == 7 ? 1 : null;
                    if (i7 != 7) {
                        i6 = Cea608Decoder.STYLE_COLORS[i7];
                    }
                    obj = obj2;
                }
                int i8 = cueStyle.start;
                i++;
                if (i8 != (i < this.cueStyles.size() ? ((CueStyle) this.cueStyles.get(i)).start : length)) {
                    if (i2 != -1 && !z) {
                        setUnderlineSpan(spannableStringBuilder, i2, i8);
                        i2 = -1;
                    } else if (i2 == -1 && z) {
                        i2 = i8;
                    }
                    if (i3 != -1 && obj == null) {
                        setItalicSpan(spannableStringBuilder, i3, i8);
                        i3 = -1;
                    } else if (i3 == -1 && obj != null) {
                        i3 = i8;
                    }
                    if (i6 != i5) {
                        setColorSpan(spannableStringBuilder, i4, i8, i5);
                        i5 = i6;
                        i4 = i8;
                    }
                }
            }
            if (!(i2 == -1 || i2 == length)) {
                setUnderlineSpan(spannableStringBuilder, i2, length);
            }
            if (!(i3 == -1 || i3 == length)) {
                setItalicSpan(spannableStringBuilder, i3, length);
            }
            if (i4 != length) {
                setColorSpan(spannableStringBuilder, i4, length, i5);
            }
            return new SpannableString(spannableStringBuilder);
        }

        private static void setUnderlineSpan(SpannableStringBuilder spannableStringBuilder, int i, int i2) {
            spannableStringBuilder.setSpan(new UnderlineSpan(), i, i2, 33);
        }

        private static void setItalicSpan(SpannableStringBuilder spannableStringBuilder, int i, int i2) {
            spannableStringBuilder.setSpan(new StyleSpan(2), i, i2, 33);
        }

        private static void setColorSpan(SpannableStringBuilder spannableStringBuilder, int i, int i2, int i3) {
            if (i3 != -1) {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(i3), i, i2, 33);
            }
        }
    }

    private static boolean isMidrowCtrlCode(byte b, byte b2) {
        return (b & 247) == 17 && (b2 & 240) == 32;
    }

    private static boolean isMiscCode(byte b, byte b2) {
        return (b & 247) == 20 && (b2 & 240) == 32;
    }

    private static boolean isPreambleAddressCode(byte b, byte b2) {
        return (b & 240) == 16 && (b2 & 192) == 64;
    }

    private static boolean isRepeatable(byte b) {
        return (b & 240) == 16;
    }

    private static boolean isTabCtrlCode(byte b, byte b2) {
        return (b & 247) == 23 && b2 >= (byte) 33 && b2 <= (byte) 35;
    }

    public void release() {
    }

    public /* bridge */ /* synthetic */ SubtitleInputBuffer dequeueInputBuffer() throws SubtitleDecoderException {
        return super.dequeueInputBuffer();
    }

    public /* bridge */ /* synthetic */ SubtitleOutputBuffer dequeueOutputBuffer() throws SubtitleDecoderException {
        return super.dequeueOutputBuffer();
    }

    public /* bridge */ /* synthetic */ void queueInputBuffer(SubtitleInputBuffer subtitleInputBuffer) throws SubtitleDecoderException {
        super.queueInputBuffer(subtitleInputBuffer);
    }

    public /* bridge */ /* synthetic */ void setPositionUs(long j) {
        super.setPositionUs(j);
    }

    public Cea608Decoder(String str, int i) {
        this.packetLength = MimeTypes.APPLICATION_MP4CEA608.equals(str) ? 2 : 3;
        if (i == 3 || i == 4) {
            this.selectedField = 2;
        } else {
            this.selectedField = 1;
        }
        setCaptionMode(0);
        resetCueBuilders();
    }

    public void flush() {
        super.flush();
        this.cues = null;
        this.lastCues = null;
        setCaptionMode(0);
        setCaptionRowCount(4);
        resetCueBuilders();
        this.captionValid = false;
        this.repeatableControlSet = false;
        this.repeatableControlCc1 = (byte) 0;
        this.repeatableControlCc2 = (byte) 0;
    }

    /* Access modifiers changed, original: protected */
    public boolean isNewSubtitleDataAvailable() {
        return this.cues != this.lastCues;
    }

    /* Access modifiers changed, original: protected */
    public Subtitle createSubtitle() {
        List list = this.cues;
        this.lastCues = list;
        return new CeaSubtitle(list);
    }

    /* Access modifiers changed, original: protected */
    public void decode(SubtitleInputBuffer subtitleInputBuffer) {
        this.ccData.reset(subtitleInputBuffer.data.array(), subtitleInputBuffer.data.limit());
        Object obj = null;
        while (true) {
            int bytesLeft = this.ccData.bytesLeft();
            int i = this.packetLength;
            if (bytesLeft < i) {
                break;
            }
            if (i == 2) {
                i = -4;
            } else {
                i = (byte) this.ccData.readUnsignedByte();
            }
            int readUnsignedByte = this.ccData.readUnsignedByte();
            int readUnsignedByte2 = this.ccData.readUnsignedByte();
            if ((i & 2) == 0) {
                if (this.selectedField != 1 || (i & 1) == 0) {
                    if (this.selectedField != 2 || (i & 1) == 1) {
                        byte b = (byte) (readUnsignedByte & 127);
                        byte b2 = (byte) (readUnsignedByte2 & 127);
                        if (b != (byte) 0 || b2 != (byte) 0) {
                            boolean z = this.repeatableControlSet;
                            this.repeatableControlSet = false;
                            boolean z2 = this.captionValid;
                            this.captionValid = (i & 4) == 4;
                            if (this.captionValid) {
                                boolean[] zArr = ODD_PARITY_BYTE_TABLE;
                                if (!zArr[readUnsignedByte] || !zArr[readUnsignedByte2]) {
                                    resetCueBuilders();
                                } else if ((b & 247) == 17 && (b2 & 240) == 48) {
                                    this.currentCueBuilder.append(getSpecialChar(b2));
                                } else if ((b & 246) == 18 && (b2 & 224) == 32) {
                                    this.currentCueBuilder.backspace();
                                    if ((b & 1) == 0) {
                                        this.currentCueBuilder.append(getExtendedEsFrChar(b2));
                                    } else {
                                        this.currentCueBuilder.append(getExtendedPtDeChar(b2));
                                    }
                                } else if ((b & 224) == 0) {
                                    handleCtrl(b, b2, z);
                                } else {
                                    this.currentCueBuilder.append(getChar(b));
                                    if ((b2 & 224) != 0) {
                                        this.currentCueBuilder.append(getChar(b2));
                                    }
                                }
                            } else if (z2) {
                                resetCueBuilders();
                            }
                            obj = 1;
                        }
                    }
                }
            }
        }
        if (obj != null) {
            int i2 = this.captionMode;
            if (i2 == 1 || i2 == 3) {
                this.cues = getDisplayCues();
            }
        }
    }

    private void handleCtrl(byte b, byte b2, boolean z) {
        if (isRepeatable(b)) {
            if (!z || this.repeatableControlCc1 != b || this.repeatableControlCc2 != b2) {
                this.repeatableControlSet = true;
                this.repeatableControlCc1 = b;
                this.repeatableControlCc2 = b2;
            } else {
                return;
            }
        }
        if (isMidrowCtrlCode(b, b2)) {
            handleMidrowCtrl(b2);
        } else if (isPreambleAddressCode(b, b2)) {
            handlePreambleAddressCode(b, b2);
        } else if (isTabCtrlCode(b, b2)) {
            this.currentCueBuilder.tabOffset = b2 - 32;
        } else if (isMiscCode(b, b2)) {
            handleMiscCode(b2);
        }
    }

    private void handleMidrowCtrl(byte b) {
        this.currentCueBuilder.append(' ');
        this.currentCueBuilder.setStyle((b >> 1) & 7, (b & 1) == 1);
    }

    private void handlePreambleAddressCode(byte b, byte b2) {
        int i = ROW_INDICES[b & 7];
        boolean z = false;
        if (((b2 & 32) != 0 ? 1 : null) != null) {
            i++;
        }
        if (i != this.currentCueBuilder.row) {
            if (!(this.captionMode == 1 || this.currentCueBuilder.isEmpty())) {
                this.currentCueBuilder = new CueBuilder(this.captionMode, this.captionRowCount);
                this.cueBuilders.add(this.currentCueBuilder);
            }
            this.currentCueBuilder.row = i;
        }
        Object obj = (b2 & 16) == 16 ? 1 : null;
        if ((b2 & 1) == 1) {
            z = true;
        }
        int i2 = (b2 >> 1) & 7;
        this.currentCueBuilder.setStyle(obj != null ? 8 : i2, z);
        if (obj != null) {
            this.currentCueBuilder.indent = COLUMN_INDICES[i2];
        }
    }

    private void handleMiscCode(byte b) {
        if (b == (byte) 32) {
            setCaptionMode(2);
        } else if (b != (byte) 41) {
            switch (b) {
                case (byte) 37:
                    setCaptionMode(1);
                    setCaptionRowCount(2);
                    return;
                case (byte) 38:
                    setCaptionMode(1);
                    setCaptionRowCount(3);
                    return;
                case (byte) 39:
                    setCaptionMode(1);
                    setCaptionRowCount(4);
                    return;
                default:
                    int i = this.captionMode;
                    if (i != 0) {
                        if (b != (byte) 33) {
                            if (b != (byte) 36) {
                                switch (b) {
                                    case (byte) 44:
                                        this.cues = Collections.emptyList();
                                        int i2 = this.captionMode;
                                        if (i2 == 1 || i2 == 3) {
                                            resetCueBuilders();
                                            break;
                                        }
                                    case (byte) 45:
                                        if (i == 1 && !this.currentCueBuilder.isEmpty()) {
                                            this.currentCueBuilder.rollUp();
                                            break;
                                        }
                                    case (byte) 46:
                                        resetCueBuilders();
                                        break;
                                    case (byte) 47:
                                        this.cues = getDisplayCues();
                                        resetCueBuilders();
                                        break;
                                }
                            }
                        }
                        this.currentCueBuilder.backspace();
                        return;
                    }
                    return;
            }
        } else {
            setCaptionMode(3);
        }
    }

    private List<Cue> getDisplayCues() {
        int size = this.cueBuilders.size();
        ArrayList arrayList = new ArrayList(size);
        int i = 2;
        for (int i2 = 0; i2 < size; i2++) {
            Cue build = ((CueBuilder) this.cueBuilders.get(i2)).build(Integer.MIN_VALUE);
            arrayList.add(build);
            if (build != null) {
                i = Math.min(i, build.positionAnchor);
            }
        }
        ArrayList arrayList2 = new ArrayList(size);
        for (int i3 = 0; i3 < size; i3++) {
            Object obj = (Cue) arrayList.get(i3);
            if (obj != null) {
                if (obj.positionAnchor != i) {
                    obj = ((CueBuilder) this.cueBuilders.get(i3)).build(i);
                }
                arrayList2.add(obj);
            }
        }
        return arrayList2;
    }

    private void setCaptionMode(int i) {
        int i2 = this.captionMode;
        if (i2 != i) {
            this.captionMode = i;
            if (i == 3) {
                for (i2 = 0; i2 < this.cueBuilders.size(); i2++) {
                    ((CueBuilder) this.cueBuilders.get(i2)).setCaptionMode(i);
                }
                return;
            }
            resetCueBuilders();
            if (i2 == 3 || i == 1 || i == 0) {
                this.cues = Collections.emptyList();
            }
        }
    }

    private void setCaptionRowCount(int i) {
        this.captionRowCount = i;
        this.currentCueBuilder.setCaptionRowCount(i);
    }

    private void resetCueBuilders() {
        this.currentCueBuilder.reset(this.captionMode);
        this.cueBuilders.clear();
        this.cueBuilders.add(this.currentCueBuilder);
    }

    private static char getChar(byte b) {
        return (char) BASIC_CHARACTER_SET[(b & 127) - 32];
    }

    private static char getSpecialChar(byte b) {
        return (char) SPECIAL_CHARACTER_SET[b & 15];
    }

    private static char getExtendedEsFrChar(byte b) {
        return (char) SPECIAL_ES_FR_CHARACTER_SET[b & 31];
    }

    private static char getExtendedPtDeChar(byte b) {
        return (char) SPECIAL_PT_DE_CHARACTER_SET[b & 31];
    }
}

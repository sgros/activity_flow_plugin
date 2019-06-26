package androidx.core.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;

public final class BidiFormatter {
    static final BidiFormatter DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    static final BidiFormatter DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    static final TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
    private static final String LRM_STRING = Character.toString(8206);
    private static final String RLM_STRING = Character.toString(8207);
    private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    private final int mFlags;
    private final boolean mIsRtlContext;

    public static final class Builder {
        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

        public Builder() {
            initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
        }

        private void initialize(boolean z) {
            this.mIsRtlContext = z;
            this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
            this.mFlags = 2;
        }

        private static BidiFormatter getDefaultInstanceFromContext(boolean z) {
            return z ? BidiFormatter.DEFAULT_RTL_INSTANCE : BidiFormatter.DEFAULT_LTR_INSTANCE;
        }

        public BidiFormatter build() {
            if (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) {
                return getDefaultInstanceFromContext(this.mIsRtlContext);
            }
            return new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
        }
    }

    private static class DirectionalityEstimator {
        private static final byte[] DIR_TYPE_CACHE = new byte[1792];
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final CharSequence text;

        static {
            for (int i = 0; i < 1792; i++) {
                DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
            }
        }

        DirectionalityEstimator(CharSequence charSequence, boolean z) {
            this.text = charSequence;
            this.isHtml = z;
            this.length = charSequence.length();
        }

        /* Access modifiers changed, original: 0000 */
        public int getEntryDir() {
            this.charIndex = 0;
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            while (this.charIndex < this.length && i == 0) {
                byte dirTypeForward = dirTypeForward();
                if (dirTypeForward != (byte) 0) {
                    if (dirTypeForward == (byte) 1 || dirTypeForward == (byte) 2) {
                        if (i3 == 0) {
                            return 1;
                        }
                    } else if (dirTypeForward != (byte) 9) {
                        switch (dirTypeForward) {
                            case (byte) 14:
                            case (byte) 15:
                                i3++;
                                i2 = -1;
                                continue;
                            case (byte) 16:
                            case (byte) 17:
                                i3++;
                                i2 = 1;
                                continue;
                            case (byte) 18:
                                i3--;
                                i2 = 0;
                                continue;
                        }
                    }
                } else if (i3 == 0) {
                    return -1;
                }
                i = i3;
            }
            if (i == 0) {
                return 0;
            }
            if (i2 != 0) {
                return i2;
            }
            while (this.charIndex > 0) {
                switch (dirTypeBackward()) {
                    case (byte) 14:
                    case (byte) 15:
                        if (i == i3) {
                            return -1;
                        }
                        break;
                    case (byte) 16:
                    case (byte) 17:
                        if (i == i3) {
                            return 1;
                        }
                        break;
                    case (byte) 18:
                        i3++;
                        continue;
                    default:
                        continue;
                }
                i3--;
            }
            return 0;
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Missing block: B:18:0x002b, code skipped:
            r2 = r2 - 1;
     */
        public int getExitDir() {
            /*
            r7 = this;
            r0 = r7.length;
            r7.charIndex = r0;
            r0 = 0;
            r1 = 0;
            r2 = 0;
        L_0x0007:
            r3 = r7.charIndex;
            if (r3 <= 0) goto L_0x003b;
        L_0x000b:
            r3 = r7.dirTypeBackward();
            r4 = -1;
            if (r3 == 0) goto L_0x0034;
        L_0x0012:
            r5 = 1;
            if (r3 == r5) goto L_0x002e;
        L_0x0015:
            r6 = 2;
            if (r3 == r6) goto L_0x002e;
        L_0x0018:
            r6 = 9;
            if (r3 == r6) goto L_0x0007;
        L_0x001c:
            switch(r3) {
                case 14: goto L_0x0028;
                case 15: goto L_0x0028;
                case 16: goto L_0x0025;
                case 17: goto L_0x0025;
                case 18: goto L_0x0022;
                default: goto L_0x001f;
            };
        L_0x001f:
            if (r1 != 0) goto L_0x0007;
        L_0x0021:
            goto L_0x0039;
        L_0x0022:
            r2 = r2 + 1;
            goto L_0x0007;
        L_0x0025:
            if (r1 != r2) goto L_0x002b;
        L_0x0027:
            return r5;
        L_0x0028:
            if (r1 != r2) goto L_0x002b;
        L_0x002a:
            return r4;
        L_0x002b:
            r2 = r2 + -1;
            goto L_0x0007;
        L_0x002e:
            if (r2 != 0) goto L_0x0031;
        L_0x0030:
            return r5;
        L_0x0031:
            if (r1 != 0) goto L_0x0007;
        L_0x0033:
            goto L_0x0039;
        L_0x0034:
            if (r2 != 0) goto L_0x0037;
        L_0x0036:
            return r4;
        L_0x0037:
            if (r1 != 0) goto L_0x0007;
        L_0x0039:
            r1 = r2;
            goto L_0x0007;
        L_0x003b:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.text.BidiFormatter$DirectionalityEstimator.getExitDir():int");
        }

        private static byte getCachedDirectionality(char c) {
            return c < 1792 ? DIR_TYPE_CACHE[c] : Character.getDirectionality(c);
        }

        /* Access modifiers changed, original: 0000 */
        public byte dirTypeForward() {
            this.lastChar = this.text.charAt(this.charIndex);
            if (Character.isHighSurrogate(this.lastChar)) {
                int codePointAt = Character.codePointAt(this.text, this.charIndex);
                this.charIndex += Character.charCount(codePointAt);
                return Character.getDirectionality(codePointAt);
            }
            this.charIndex++;
            byte cachedDirectionality = getCachedDirectionality(this.lastChar);
            if (this.isHtml) {
                char c = this.lastChar;
                if (c == '<') {
                    cachedDirectionality = skipTagForward();
                } else if (c == '&') {
                    cachedDirectionality = skipEntityForward();
                }
            }
            return cachedDirectionality;
        }

        /* Access modifiers changed, original: 0000 */
        public byte dirTypeBackward() {
            this.lastChar = this.text.charAt(this.charIndex - 1);
            if (Character.isLowSurrogate(this.lastChar)) {
                int codePointBefore = Character.codePointBefore(this.text, this.charIndex);
                this.charIndex -= Character.charCount(codePointBefore);
                return Character.getDirectionality(codePointBefore);
            }
            this.charIndex--;
            byte cachedDirectionality = getCachedDirectionality(this.lastChar);
            if (this.isHtml) {
                char c = this.lastChar;
                if (c == '>') {
                    cachedDirectionality = skipTagBackward();
                } else if (c == ';') {
                    cachedDirectionality = skipEntityBackward();
                }
            }
            return cachedDirectionality;
        }

        private byte skipTagForward() {
            int i = this.charIndex;
            while (true) {
                int i2 = this.charIndex;
                if (i2 < this.length) {
                    CharSequence charSequence = this.text;
                    this.charIndex = i2 + 1;
                    this.lastChar = charSequence.charAt(i2);
                    char c = this.lastChar;
                    if (c == '>') {
                        return (byte) 12;
                    }
                    if (c == '\"' || c == '\'') {
                        c = this.lastChar;
                        while (true) {
                            int i3 = this.charIndex;
                            if (i3 >= this.length) {
                                break;
                            }
                            CharSequence charSequence2 = this.text;
                            this.charIndex = i3 + 1;
                            char charAt = charSequence2.charAt(i3);
                            this.lastChar = charAt;
                            if (charAt == c) {
                                break;
                            }
                        }
                    }
                } else {
                    this.charIndex = i;
                    this.lastChar = '<';
                    return (byte) 13;
                }
            }
        }

        private byte skipTagBackward() {
            int i = this.charIndex;
            while (true) {
                int i2 = this.charIndex;
                if (i2 <= 0) {
                    break;
                }
                CharSequence charSequence = this.text;
                i2--;
                this.charIndex = i2;
                this.lastChar = charSequence.charAt(i2);
                char c = this.lastChar;
                if (c == '<') {
                    return (byte) 12;
                }
                if (c == '>') {
                    break;
                } else if (c == '\"' || c == '\'') {
                    c = this.lastChar;
                    while (true) {
                        int i3 = this.charIndex;
                        if (i3 <= 0) {
                            break;
                        }
                        charSequence = this.text;
                        i3--;
                        this.charIndex = i3;
                        char charAt = charSequence.charAt(i3);
                        this.lastChar = charAt;
                        if (charAt == c) {
                            break;
                        }
                    }
                }
            }
            this.charIndex = i;
            this.lastChar = '>';
            return (byte) 13;
        }

        private byte skipEntityForward() {
            while (true) {
                int i = this.charIndex;
                if (i >= this.length) {
                    break;
                }
                CharSequence charSequence = this.text;
                this.charIndex = i + 1;
                char charAt = charSequence.charAt(i);
                this.lastChar = charAt;
                if (charAt == ';') {
                    break;
                }
            }
            return (byte) 12;
        }

        private byte skipEntityBackward() {
            int i = this.charIndex;
            char c;
            do {
                int i2 = this.charIndex;
                if (i2 <= 0) {
                    break;
                }
                CharSequence charSequence = this.text;
                i2--;
                this.charIndex = i2;
                this.lastChar = charSequence.charAt(i2);
                c = this.lastChar;
                if (c == '&') {
                    return (byte) 12;
                }
            } while (c != ';');
            this.charIndex = i;
            this.lastChar = ';';
            return (byte) 13;
        }
    }

    public static BidiFormatter getInstance() {
        return new Builder().build();
    }

    BidiFormatter(boolean z, int i, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        this.mIsRtlContext = z;
        this.mFlags = i;
        this.mDefaultTextDirectionHeuristicCompat = textDirectionHeuristicCompat;
    }

    public boolean getStereoReset() {
        return (this.mFlags & 2) != 0;
    }

    private String markAfter(CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        boolean isRtl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        if (this.mIsRtlContext || (!isRtl && getExitDir(charSequence) != 1)) {
            return (!this.mIsRtlContext || (isRtl && getExitDir(charSequence) != -1)) ? "" : RLM_STRING;
        } else {
            return LRM_STRING;
        }
    }

    private String markBefore(CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        boolean isRtl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        if (this.mIsRtlContext || (!isRtl && getEntryDir(charSequence) != 1)) {
            return (!this.mIsRtlContext || (isRtl && getEntryDir(charSequence) != -1)) ? "" : RLM_STRING;
        } else {
            return LRM_STRING;
        }
    }

    public CharSequence unicodeWrap(CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat, boolean z) {
        if (charSequence == null) {
            return null;
        }
        boolean isRtl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (getStereoReset() && z) {
            spannableStringBuilder.append(markBefore(charSequence, isRtl ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR));
        }
        if (isRtl != this.mIsRtlContext) {
            spannableStringBuilder.append(isRtl ? 8235 : 8234);
            spannableStringBuilder.append(charSequence);
            spannableStringBuilder.append(8236);
        } else {
            spannableStringBuilder.append(charSequence);
        }
        if (z) {
            spannableStringBuilder.append(markAfter(charSequence, isRtl ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR));
        }
        return spannableStringBuilder;
    }

    public CharSequence unicodeWrap(CharSequence charSequence) {
        return unicodeWrap(charSequence, this.mDefaultTextDirectionHeuristicCompat, true);
    }

    static boolean isRtlLocale(Locale locale) {
        return TextUtilsCompat.getLayoutDirectionFromLocale(locale) == 1;
    }

    private static int getExitDir(CharSequence charSequence) {
        return new DirectionalityEstimator(charSequence, false).getExitDir();
    }

    private static int getEntryDir(CharSequence charSequence) {
        return new DirectionalityEstimator(charSequence, false).getEntryDir();
    }
}

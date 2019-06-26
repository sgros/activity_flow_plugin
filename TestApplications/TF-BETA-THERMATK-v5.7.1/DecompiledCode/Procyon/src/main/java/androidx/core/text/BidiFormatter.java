// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;

public final class BidiFormatter
{
    static final BidiFormatter DEFAULT_LTR_INSTANCE;
    static final BidiFormatter DEFAULT_RTL_INSTANCE;
    static final TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
    private static final String LRM_STRING;
    private static final String RLM_STRING;
    private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    private final int mFlags;
    private final boolean mIsRtlContext;
    
    static {
        DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        LRM_STRING = Character.toString('\u200e');
        RLM_STRING = Character.toString('\u200f');
        DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC);
        DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC);
    }
    
    BidiFormatter(final boolean mIsRtlContext, final int mFlags, final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat) {
        this.mIsRtlContext = mIsRtlContext;
        this.mFlags = mFlags;
        this.mDefaultTextDirectionHeuristicCompat = mDefaultTextDirectionHeuristicCompat;
    }
    
    private static int getEntryDir(final CharSequence charSequence) {
        return new DirectionalityEstimator(charSequence, false).getEntryDir();
    }
    
    private static int getExitDir(final CharSequence charSequence) {
        return new DirectionalityEstimator(charSequence, false).getExitDir();
    }
    
    public static BidiFormatter getInstance() {
        return new Builder().build();
    }
    
    static boolean isRtlLocale(final Locale locale) {
        final int layoutDirectionFromLocale = TextUtilsCompat.getLayoutDirectionFromLocale(locale);
        boolean b = true;
        if (layoutDirectionFromLocale != 1) {
            b = false;
        }
        return b;
    }
    
    private String markAfter(final CharSequence charSequence, final TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        final boolean rtl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        if (!this.mIsRtlContext && (rtl || getExitDir(charSequence) == 1)) {
            return BidiFormatter.LRM_STRING;
        }
        if (this.mIsRtlContext && (!rtl || getExitDir(charSequence) == -1)) {
            return BidiFormatter.RLM_STRING;
        }
        return "";
    }
    
    private String markBefore(final CharSequence charSequence, final TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        final boolean rtl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        if (!this.mIsRtlContext && (rtl || getEntryDir(charSequence) == 1)) {
            return BidiFormatter.LRM_STRING;
        }
        if (this.mIsRtlContext && (!rtl || getEntryDir(charSequence) == -1)) {
            return BidiFormatter.RLM_STRING;
        }
        return "";
    }
    
    public boolean getStereoReset() {
        return (this.mFlags & 0x2) != 0x0;
    }
    
    public CharSequence unicodeWrap(final CharSequence charSequence) {
        return this.unicodeWrap(charSequence, this.mDefaultTextDirectionHeuristicCompat, true);
    }
    
    public CharSequence unicodeWrap(final CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat, final boolean b) {
        if (charSequence == null) {
            return null;
        }
        final boolean rtl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (this.getStereoReset() && b) {
            if (rtl) {
                textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
            }
            else {
                textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
            }
            spannableStringBuilder.append((CharSequence)this.markBefore(charSequence, textDirectionHeuristicCompat));
        }
        if (rtl != this.mIsRtlContext) {
            char c;
            if (rtl) {
                c = '\u202b';
            }
            else {
                c = '\u202a';
            }
            spannableStringBuilder.append(c);
            spannableStringBuilder.append(charSequence);
            spannableStringBuilder.append('\u202c');
        }
        else {
            spannableStringBuilder.append(charSequence);
        }
        if (b) {
            if (rtl) {
                textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
            }
            else {
                textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
            }
            spannableStringBuilder.append((CharSequence)this.markAfter(charSequence, textDirectionHeuristicCompat));
        }
        return (CharSequence)spannableStringBuilder;
    }
    
    public static final class Builder
    {
        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;
        
        public Builder() {
            this.initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
        }
        
        private static BidiFormatter getDefaultInstanceFromContext(final boolean b) {
            BidiFormatter bidiFormatter;
            if (b) {
                bidiFormatter = BidiFormatter.DEFAULT_RTL_INSTANCE;
            }
            else {
                bidiFormatter = BidiFormatter.DEFAULT_LTR_INSTANCE;
            }
            return bidiFormatter;
        }
        
        private void initialize(final boolean mIsRtlContext) {
            this.mIsRtlContext = mIsRtlContext;
            this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
            this.mFlags = 2;
        }
        
        public BidiFormatter build() {
            if (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) {
                return getDefaultInstanceFromContext(this.mIsRtlContext);
            }
            return new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
        }
    }
    
    private static class DirectionalityEstimator
    {
        private static final byte[] DIR_TYPE_CACHE;
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final CharSequence text;
        
        static {
            DIR_TYPE_CACHE = new byte[1792];
            for (int i = 0; i < 1792; ++i) {
                DirectionalityEstimator.DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
            }
        }
        
        DirectionalityEstimator(final CharSequence text, final boolean isHtml) {
            this.text = text;
            this.isHtml = isHtml;
            this.length = text.length();
        }
        
        private static byte getCachedDirectionality(final char ch) {
            byte directionality;
            if (ch < '\u0700') {
                directionality = DirectionalityEstimator.DIR_TYPE_CACHE[ch];
            }
            else {
                directionality = Character.getDirectionality(ch);
            }
            return directionality;
        }
        
        private byte skipEntityBackward() {
            final int charIndex = this.charIndex;
            char lastChar;
            do {
                int charIndex2 = this.charIndex;
                if (charIndex2 <= 0) {
                    break;
                }
                final CharSequence text = this.text;
                --charIndex2;
                this.charIndex = charIndex2;
                this.lastChar = text.charAt(charIndex2);
                lastChar = this.lastChar;
                if (lastChar == '&') {
                    return 12;
                }
            } while (lastChar != ';');
            this.charIndex = charIndex;
            this.lastChar = 59;
            return 13;
        }
        
        private byte skipEntityForward() {
            char char1;
            do {
                final int charIndex = this.charIndex;
                if (charIndex >= this.length) {
                    break;
                }
                final CharSequence text = this.text;
                this.charIndex = charIndex + 1;
                char1 = text.charAt(charIndex);
                this.lastChar = char1;
            } while (char1 != ';');
            return 12;
        }
        
        private byte skipTagBackward() {
            final int charIndex = this.charIndex;
            while (true) {
                int charIndex2 = this.charIndex;
                if (charIndex2 <= 0) {
                    break;
                }
                final CharSequence text = this.text;
                --charIndex2;
                this.charIndex = charIndex2;
                this.lastChar = text.charAt(charIndex2);
                final char lastChar = this.lastChar;
                if (lastChar == '<') {
                    return 12;
                }
                if (lastChar == '>') {
                    break;
                }
                if (lastChar != '\"' && lastChar != '\'') {
                    continue;
                }
                final char lastChar2 = this.lastChar;
                char char1;
                do {
                    int charIndex3 = this.charIndex;
                    if (charIndex3 <= 0) {
                        break;
                    }
                    final CharSequence text2 = this.text;
                    --charIndex3;
                    this.charIndex = charIndex3;
                    char1 = text2.charAt(charIndex3);
                    this.lastChar = char1;
                } while (char1 != lastChar2);
            }
            this.charIndex = charIndex;
            this.lastChar = 62;
            return 13;
        }
        
        private byte skipTagForward() {
            final int charIndex = this.charIndex;
            while (true) {
                final int charIndex2 = this.charIndex;
                if (charIndex2 >= this.length) {
                    this.charIndex = charIndex;
                    this.lastChar = 60;
                    return 13;
                }
                final CharSequence text = this.text;
                this.charIndex = charIndex2 + 1;
                this.lastChar = text.charAt(charIndex2);
                final char lastChar = this.lastChar;
                if (lastChar == '>') {
                    return 12;
                }
                if (lastChar != '\"' && lastChar != '\'') {
                    continue;
                }
                final char lastChar2 = this.lastChar;
                char char1;
                do {
                    final int charIndex3 = this.charIndex;
                    if (charIndex3 >= this.length) {
                        break;
                    }
                    final CharSequence text2 = this.text;
                    this.charIndex = charIndex3 + 1;
                    char1 = text2.charAt(charIndex3);
                    this.lastChar = char1;
                } while (char1 != lastChar2);
            }
        }
        
        byte dirTypeBackward() {
            this.lastChar = this.text.charAt(this.charIndex - 1);
            if (Character.isLowSurrogate(this.lastChar)) {
                final int codePointBefore = Character.codePointBefore(this.text, this.charIndex);
                this.charIndex -= Character.charCount(codePointBefore);
                return Character.getDirectionality(codePointBefore);
            }
            --this.charIndex;
            byte b = getCachedDirectionality(this.lastChar);
            if (this.isHtml) {
                final char lastChar = this.lastChar;
                if (lastChar == '>') {
                    b = this.skipTagBackward();
                }
                else {
                    b = b;
                    if (lastChar == ';') {
                        b = this.skipEntityBackward();
                    }
                }
            }
            return b;
        }
        
        byte dirTypeForward() {
            this.lastChar = this.text.charAt(this.charIndex);
            if (Character.isHighSurrogate(this.lastChar)) {
                final int codePoint = Character.codePointAt(this.text, this.charIndex);
                this.charIndex += Character.charCount(codePoint);
                return Character.getDirectionality(codePoint);
            }
            ++this.charIndex;
            byte b = getCachedDirectionality(this.lastChar);
            if (this.isHtml) {
                final char lastChar = this.lastChar;
                if (lastChar == '<') {
                    b = this.skipTagForward();
                }
                else {
                    b = b;
                    if (lastChar == '&') {
                        b = this.skipEntityForward();
                    }
                }
            }
            return b;
        }
        
        int getEntryDir() {
            this.charIndex = 0;
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            while (this.charIndex < this.length && n == 0) {
                final byte dirTypeForward = this.dirTypeForward();
                if (dirTypeForward != 0) {
                    if (dirTypeForward != 1 && dirTypeForward != 2) {
                        if (dirTypeForward == 9) {
                            continue;
                        }
                        switch (dirTypeForward) {
                            case 18: {
                                --n3;
                                n2 = 0;
                                continue;
                            }
                            case 16:
                            case 17: {
                                ++n3;
                                n2 = 1;
                                continue;
                            }
                            case 14:
                            case 15: {
                                ++n3;
                                n2 = -1;
                                continue;
                            }
                        }
                    }
                    else if (n3 == 0) {
                        return 1;
                    }
                }
                else if (n3 == 0) {
                    return -1;
                }
                n = n3;
            }
            if (n == 0) {
                return 0;
            }
            if (n2 != 0) {
                return n2;
            }
            while (this.charIndex > 0) {
                switch (this.dirTypeBackward()) {
                    default: {
                        continue;
                    }
                    case 18: {
                        ++n3;
                        continue;
                    }
                    case 16:
                    case 17: {
                        if (n == n3) {
                            return 1;
                        }
                        break;
                    }
                    case 14:
                    case 15: {
                        if (n == n3) {
                            return -1;
                        }
                        break;
                    }
                }
                --n3;
            }
            return 0;
        }
        
        int getExitDir() {
            this.charIndex = this.length;
            int n = 0;
            int n2 = 0;
            while (this.charIndex > 0) {
                final byte dirTypeBackward = this.dirTypeBackward();
                Label_0136: {
                    if (dirTypeBackward != 0) {
                        if (dirTypeBackward != 1 && dirTypeBackward != 2) {
                            if (dirTypeBackward != 9) {
                                switch (dirTypeBackward) {
                                    default: {
                                        if (n == 0) {
                                            break Label_0136;
                                        }
                                        continue;
                                    }
                                    case 18: {
                                        ++n2;
                                        continue;
                                    }
                                    case 16:
                                    case 17: {
                                        if (n == n2) {
                                            return 1;
                                        }
                                        break;
                                    }
                                    case 14:
                                    case 15: {
                                        if (n == n2) {
                                            return -1;
                                        }
                                        break;
                                    }
                                }
                                --n2;
                                continue;
                            }
                            continue;
                        }
                        else {
                            if (n2 == 0) {
                                return 1;
                            }
                            if (n != 0) {
                                continue;
                            }
                        }
                    }
                    else {
                        if (n2 == 0) {
                            return -1;
                        }
                        if (n != 0) {
                            continue;
                        }
                    }
                }
                n = n2;
            }
            return 0;
        }
    }
}

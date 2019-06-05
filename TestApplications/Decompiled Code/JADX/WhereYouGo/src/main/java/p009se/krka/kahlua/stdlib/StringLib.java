package p009se.krka.kahlua.stdlib;

import android.support.p000v4.view.MotionEventCompat;
import locus.api.objects.extra.ExtraData;
import locus.api.objects.geocaching.GeocachingData;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;

/* renamed from: se.krka.kahlua.stdlib.StringLib */
public final class StringLib implements JavaFunction {
    private static final int BYTE = 2;
    private static final int CAP_POSITION = -2;
    private static final int CAP_UNFINISHED = -1;
    private static final int CHAR = 1;
    private static final int FIND = 7;
    private static final int FORMAT = 6;
    private static final int GSUB = 9;
    private static final int LOWER = 3;
    private static final int LUA_MAXCAPTURES = 32;
    private static final char L_ESC = '%';
    private static final int MATCH = 8;
    private static final int NUM_FUNCTIONS = 10;
    private static final int REVERSE = 5;
    private static final boolean[] SPECIALS = new boolean[256];
    public static final Class STRING_CLASS = "".getClass();
    private static final int SUB = 0;
    private static final int UPPER = 4;
    private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static StringLib[] functions = new StringLib[10];
    private static final String[] names = new String[10];
    private int methodId;

    /* renamed from: se.krka.kahlua.stdlib.StringLib$MatchState */
    public static class MatchState {
        public LuaCallFrame callFrame;
        public Capture[] capture = new Capture[32];
        public int endIndex;
        public int level;
        public StringPointer src_init;

        /* renamed from: se.krka.kahlua.stdlib.StringLib$MatchState$Capture */
        public static class Capture {
            public StringPointer init;
            public int len;
        }

        public MatchState() {
            for (int i = 0; i < 32; i++) {
                this.capture[i] = new Capture();
            }
        }

        public Object[] getCaptures() {
            if (this.level <= 0) {
                return null;
            }
            Object[] caps = new String[this.level];
            for (int i = 0; i < this.level; i++) {
                if (this.capture[i].len == -2) {
                    caps[i] = new Double((double) ((this.src_init.length() - this.capture[i].init.length()) + 1));
                } else {
                    caps[i] = this.capture[i].init.getString().substring(0, this.capture[i].len);
                }
            }
            return caps;
        }
    }

    /* renamed from: se.krka.kahlua.stdlib.StringLib$StringPointer */
    public static class StringPointer {
        private int index = 0;
        private String string;

        public StringPointer(String original) {
            this.string = original;
        }

        public StringPointer(String original, int index) {
            this.string = original;
            this.index = index;
        }

        public StringPointer getClone() {
            return new StringPointer(getOriginalString(), getIndex());
        }

        public int getIndex() {
            return this.index;
        }

        public void setIndex(int ind) {
            this.index = ind;
        }

        public String getOriginalString() {
            return this.string;
        }

        public void setOriginalString(String orStr) {
            this.string = orStr;
        }

        public String getString() {
            return getString(0);
        }

        public String getString(int i) {
            return this.string.substring(this.index + i, this.string.length());
        }

        public char getChar() {
            return getChar(0);
        }

        public char getChar(int strIndex) {
            if (this.index + strIndex >= this.string.length()) {
                return 0;
            }
            return this.string.charAt(this.index + strIndex);
        }

        public int length() {
            return this.string.length() - this.index;
        }

        public int postIncrStringI(int num) {
            int oldIndex = this.index;
            this.index += num;
            return oldIndex;
        }

        public int preIncrStringI(int num) {
            this.index += num;
            return this.index;
        }

        public char postIncrString(int num) {
            char c = getChar();
            this.index += num;
            return c;
        }

        public char preIncrString(int num) {
            this.index += num;
            return getChar();
        }

        public int compareTo(StringPointer cmp, int len) {
            return this.string.substring(this.index, this.index + len).compareTo(cmp.string.substring(cmp.index, cmp.index + len));
        }
    }

    static {
        int i;
        String s = "^$*+?.([%-";
        for (i = 0; i < s.length(); i++) {
            SPECIALS[s.charAt(i)] = true;
        }
        names[0] = "sub";
        names[1] = "char";
        names[2] = "byte";
        names[3] = "lower";
        names[4] = "upper";
        names[5] = "reverse";
        names[6] = "format";
        names[7] = "find";
        names[8] = "match";
        names[9] = "gsub";
        for (i = 0; i < 10; i++) {
            functions[i] = new StringLib(i);
        }
    }

    public StringLib(int index) {
        this.methodId = index;
    }

    public static void register(LuaState state) {
        LuaTable string = new LuaTableImpl();
        state.getEnvironment().rawset(BaseLib.TYPE_STRING, string);
        for (int i = 0; i < 10; i++) {
            string.rawset(names[i], functions[i]);
        }
        string.rawset("__index", string);
        state.setClassMetatable(STRING_CLASS, string);
    }

    public String toString() {
        return names[this.methodId];
    }

    public int call(LuaCallFrame callFrame, int nArguments) {
        switch (this.methodId) {
            case 0:
                return sub(callFrame, nArguments);
            case 1:
                return stringChar(callFrame, nArguments);
            case 2:
                return stringByte(callFrame, nArguments);
            case 3:
                return lower(callFrame, nArguments);
            case 4:
                return upper(callFrame, nArguments);
            case 5:
                return reverse(callFrame, nArguments);
            case 6:
                return format(callFrame, nArguments);
            case 7:
                return StringLib.findAux(callFrame, true);
            case 8:
                return StringLib.findAux(callFrame, false);
            case 9:
                return StringLib.gsub(callFrame, nArguments);
            default:
                return 0;
        }
    }

    private long unsigned(long v) {
        if (v < 0) {
            return v + 4294967296L;
        }
        return v;
    }

    private int format(LuaCallFrame callFrame, int nArguments) {
        String f = (String) BaseLib.getArg(callFrame, 1, BaseLib.TYPE_STRING, names[6]);
        int len = f.length();
        int argc = 2;
        StringBuffer result = new StringBuffer();
        int i = 0;
        while (i < len) {
            char c = f.charAt(i);
            if (c == '%') {
                i++;
                BaseLib.luaAssert(i < len, "incomplete option to 'format'");
                c = f.charAt(i);
                if (c == '%') {
                    result.append(L_ESC);
                } else {
                    boolean repr = false;
                    boolean zeroPadding = false;
                    boolean leftJustify = false;
                    boolean showPlus = false;
                    boolean spaceForSign = false;
                    while (true) {
                        switch (c) {
                            case ' ':
                                spaceForSign = true;
                                break;
                            case '#':
                                repr = true;
                                break;
                            case MotionEventCompat.AXIS_GENERIC_12 /*43*/:
                                showPlus = true;
                                break;
                            case MotionEventCompat.AXIS_GENERIC_14 /*45*/:
                                leftJustify = true;
                                break;
                            case '0':
                                zeroPadding = true;
                                break;
                            default:
                                int width = 0;
                                while (c >= '0' && c <= '9') {
                                    width = (width * 10) + (c - 48);
                                    i++;
                                    BaseLib.luaAssert(i < len, "incomplete option to 'format'");
                                    c = f.charAt(i);
                                }
                                int precision = 0;
                                boolean hasPrecision = false;
                                if (c == '.') {
                                    hasPrecision = true;
                                    i++;
                                    BaseLib.luaAssert(i < len, "incomplete option to 'format'");
                                    c = f.charAt(i);
                                    while (c >= '0' && c <= '9') {
                                        precision = (precision * 10) + (c - 48);
                                        i++;
                                        BaseLib.luaAssert(i < len, "incomplete option to 'format'");
                                        c = f.charAt(i);
                                    }
                                }
                                if (leftJustify) {
                                    zeroPadding = false;
                                }
                                int base = 10;
                                boolean upperCase = false;
                                int defaultPrecision = 6;
                                String basePrepend = "";
                                switch (c) {
                                    case 'E':
                                        upperCase = true;
                                        break;
                                    case 'G':
                                        upperCase = true;
                                        break;
                                    case 'X':
                                        base = 16;
                                        defaultPrecision = 1;
                                        upperCase = true;
                                        basePrepend = "0X";
                                        break;
                                    case 'c':
                                        zeroPadding = false;
                                        break;
                                    case 'd':
                                    case GeocachingData.CACHE_SOURCE_OPENCACHING_NL /*105*/:
                                        defaultPrecision = 1;
                                        break;
                                    case 'e':
                                    case 'f':
                                    case 'g':
                                        break;
                                    case 'o':
                                        base = 8;
                                        defaultPrecision = 1;
                                        basePrepend = "0";
                                        break;
                                    case 'q':
                                        width = 0;
                                        break;
                                    case 's':
                                        zeroPadding = false;
                                        break;
                                    case 'u':
                                        defaultPrecision = 1;
                                        break;
                                    case ExtraData.PAR_RTE_COMPUTE_TYPE /*120*/:
                                        base = 16;
                                        defaultPrecision = 1;
                                        basePrepend = "0x";
                                        break;
                                    default:
                                        throw new RuntimeException("invalid option '%" + c + "' to 'format'");
                                }
                                if (!hasPrecision) {
                                    precision = defaultPrecision;
                                }
                                if (hasPrecision && base != 10) {
                                    zeroPadding = false;
                                }
                                char padCharacter = zeroPadding ? '0' : ' ';
                                int resultStartLength = result.length();
                                if (!leftJustify) {
                                    extend(result, width, padCharacter);
                                }
                                Double v;
                                boolean isNaN;
                                double vDouble;
                                long vLong;
                                switch (c) {
                                    case 'E':
                                    case 'e':
                                    case 'f':
                                        v = getDoubleArg(callFrame, argc);
                                        isNaN = v.isInfinite() || v.isNaN();
                                        vDouble = v.doubleValue();
                                        if (MathLib.isNegative(vDouble)) {
                                            if (!isNaN) {
                                                result.append('-');
                                            }
                                            vDouble = -vDouble;
                                        } else if (showPlus) {
                                            result.append('+');
                                        } else if (spaceForSign) {
                                            result.append(' ');
                                        }
                                        if (!isNaN) {
                                            if (c != 'f') {
                                                appendScientificNumber(result, vDouble, precision, repr, false);
                                                break;
                                            }
                                            appendPrecisionNumber(result, vDouble, precision, repr);
                                            break;
                                        }
                                        result.append(BaseLib.numberToString(v));
                                        break;
                                        break;
                                    case 'G':
                                    case 'g':
                                        if (precision <= 0) {
                                            precision = 1;
                                        }
                                        v = getDoubleArg(callFrame, argc);
                                        isNaN = v.isInfinite() || v.isNaN();
                                        vDouble = v.doubleValue();
                                        if (MathLib.isNegative(vDouble)) {
                                            if (!isNaN) {
                                                result.append('-');
                                            }
                                            vDouble = -vDouble;
                                        } else if (showPlus) {
                                            result.append('+');
                                        } else if (spaceForSign) {
                                            result.append(' ');
                                        }
                                        if (!isNaN) {
                                            double x = MathLib.roundToSignificantNumbers(vDouble, precision);
                                            if (x != 0.0d && (x < 1.0E-4d || x >= MathLib.ipow(10.0d, precision))) {
                                                appendScientificNumber(result, x, precision - 1, repr, true);
                                                break;
                                            }
                                            int iPartSize;
                                            if (x == 0.0d) {
                                                iPartSize = 1;
                                            } else if (Math.floor(x) == 0.0d) {
                                                iPartSize = 0;
                                            } else {
                                                double longValue = x;
                                                iPartSize = 1;
                                                while (longValue >= 10.0d) {
                                                    longValue /= 10.0d;
                                                    iPartSize++;
                                                }
                                            }
                                            appendSignificantNumber(result, x, precision - iPartSize, repr);
                                            break;
                                        }
                                        result.append(BaseLib.numberToString(v));
                                        break;
                                    case 'X':
                                    case 'o':
                                    case 'u':
                                    case ExtraData.PAR_RTE_COMPUTE_TYPE /*120*/:
                                        vLong = unsigned(getDoubleArg(callFrame, argc).longValue());
                                        if (repr) {
                                            if (base == 8) {
                                                int digits = 0;
                                                long vLong2 = vLong;
                                                while (vLong2 > 0) {
                                                    vLong2 /= 8;
                                                    digits++;
                                                }
                                                if (precision <= digits) {
                                                    result.append(basePrepend);
                                                }
                                            } else if (base == 16 && vLong != 0) {
                                                result.append(basePrepend);
                                            }
                                        }
                                        if (vLong != 0 || precision > 0) {
                                            StringLib.stringBufferAppend(result, (double) vLong, base, false, precision);
                                            break;
                                        }
                                    case 'c':
                                        result.append((char) getDoubleArg(callFrame, argc).shortValue());
                                        break;
                                    case 'd':
                                    case GeocachingData.CACHE_SOURCE_OPENCACHING_NL /*105*/:
                                        vLong = getDoubleArg(callFrame, argc).longValue();
                                        if (vLong < 0) {
                                            result.append('-');
                                            vLong = -vLong;
                                        } else if (showPlus) {
                                            result.append('+');
                                        } else if (spaceForSign) {
                                            result.append(' ');
                                        }
                                        if (vLong != 0 || precision > 0) {
                                            StringLib.stringBufferAppend(result, (double) vLong, base, false, precision);
                                            break;
                                        }
                                    case 'q':
                                        String q = getStringArg(callFrame, argc);
                                        result.append('\"');
                                        for (int j = 0; j < q.length(); j++) {
                                            char d = q.charAt(j);
                                            switch (d) {
                                                case 10:
                                                    result.append("\\\n");
                                                    break;
                                                case 13:
                                                    result.append("\\r");
                                                    break;
                                                case '\"':
                                                    result.append("\\\"");
                                                    break;
                                                case '\\':
                                                    result.append("\\");
                                                    break;
                                                default:
                                                    result.append(d);
                                                    break;
                                            }
                                        }
                                        result.append('\"');
                                        break;
                                    case 's':
                                        String s = getStringArg(callFrame, argc);
                                        int n = s.length();
                                        if (hasPrecision) {
                                            n = Math.min(precision, s.length());
                                        }
                                        append(result, s, 0, n);
                                        break;
                                    default:
                                        throw new RuntimeException("Internal error");
                                }
                                int d2;
                                if (leftJustify) {
                                    d2 = width - (result.length() - resultStartLength);
                                    if (d2 > 0) {
                                        extend(result, d2, ' ');
                                    }
                                } else {
                                    d2 = Math.min((result.length() - resultStartLength) - width, width);
                                    if (d2 > 0) {
                                        result.delete(resultStartLength, resultStartLength + d2);
                                    }
                                    if (zeroPadding) {
                                        int signPos = resultStartLength + (width - d2);
                                        char ch = result.charAt(signPos);
                                        if (ch == '+' || ch == '-' || ch == ' ') {
                                            result.setCharAt(signPos, '0');
                                            result.setCharAt(resultStartLength, ch);
                                        }
                                    }
                                }
                                if (upperCase) {
                                    stringBufferUpperCase(result, resultStartLength);
                                }
                                argc++;
                                continue;
                        }
                        i++;
                        BaseLib.luaAssert(i < len, "incomplete option to 'format'");
                        c = f.charAt(i);
                    }
                }
            } else {
                result.append(c);
            }
            i++;
        }
        callFrame.push(result.toString());
        return 1;
    }

    private void append(StringBuffer buffer, String s, int start, int end) {
        for (int i = start; i < end; i++) {
            buffer.append(s.charAt(i));
        }
    }

    private void extend(StringBuffer buffer, int extraWidth, char padCharacter) {
        int preLength = buffer.length();
        buffer.setLength(preLength + extraWidth);
        for (int i = extraWidth - 1; i >= 0; i--) {
            buffer.setCharAt(preLength + i, padCharacter);
        }
    }

    private void stringBufferUpperCase(StringBuffer buffer, int start) {
        int length = buffer.length();
        for (int i = start; i < length; i++) {
            char c = buffer.charAt(i);
            if (c >= 'a' && c <= 'z') {
                buffer.setCharAt(i, (char) (c - 32));
            }
        }
    }

    private static void stringBufferAppend(StringBuffer sb, double value, int base, boolean printZero, int minDigits) {
        int startPos = sb.length();
        while (true) {
            if (value <= 0.0d && minDigits <= 0) {
                break;
            }
            double newValue = Math.floor(value / ((double) base));
            sb.append(digits[(int) (value - (((double) base) * newValue))]);
            value = newValue;
            minDigits--;
        }
        int endPos = sb.length() - 1;
        if (startPos <= endPos || !printZero) {
            for (int i = (((endPos + 1) - startPos) / 2) - 1; i >= 0; i--) {
                int leftPos = startPos + i;
                int rightPos = endPos - i;
                char left = sb.charAt(leftPos);
                sb.setCharAt(leftPos, sb.charAt(rightPos));
                sb.setCharAt(rightPos, left);
            }
            return;
        }
        sb.append('0');
    }

    private void appendPrecisionNumber(StringBuffer buffer, double number, int precision, boolean requirePeriod) {
        number = MathLib.roundToPrecision(number, precision);
        double iPart = Math.floor(number);
        double fPart = number - iPart;
        for (int i = 0; i < precision; i++) {
            fPart *= 10.0d;
        }
        fPart = MathLib.round(iPart + fPart) - iPart;
        StringLib.stringBufferAppend(buffer, iPart, 10, true, 0);
        if (requirePeriod || precision > 0) {
            buffer.append('.');
        }
        StringLib.stringBufferAppend(buffer, fPart, 10, false, precision);
    }

    private void appendSignificantNumber(StringBuffer buffer, double number, int significantDecimals, boolean includeTrailingZeros) {
        double iPart = Math.floor(number);
        StringLib.stringBufferAppend(buffer, iPart, 10, true, 0);
        double fPart = MathLib.roundToSignificantNumbers(number - iPart, significantDecimals);
        boolean hasNotStarted = iPart == 0.0d && fPart != 0.0d;
        int zeroPaddingBefore = 0;
        int scanLength = significantDecimals;
        for (int i = 0; i < scanLength; i++) {
            fPart *= 10.0d;
            if (Math.floor(fPart) == 0.0d && fPart != 0.0d) {
                zeroPaddingBefore++;
                if (hasNotStarted) {
                    scanLength++;
                }
            }
        }
        fPart = MathLib.round(fPart);
        if (!includeTrailingZeros) {
            while (fPart > 0.0d && fPart % 10.0d == 0.0d) {
                fPart /= 10.0d;
                significantDecimals--;
            }
        }
        buffer.append('.');
        int periodPos = buffer.length();
        extend(buffer, zeroPaddingBefore, '0');
        int prePos = buffer.length();
        StringLib.stringBufferAppend(buffer, fPart, 10, false, 0);
        int len = buffer.length() - prePos;
        if (includeTrailingZeros && len < significantDecimals) {
            extend(buffer, (significantDecimals - len) - zeroPaddingBefore, '0');
        }
        if (!includeTrailingZeros && periodPos == buffer.length()) {
            buffer.delete(periodPos - 1, buffer.length());
        }
    }

    private void appendScientificNumber(StringBuffer buffer, double x, int precision, boolean repr, boolean useSignificantNumbers) {
        char expSign;
        int exponent = 0;
        for (int i = 0; i < 2; i++) {
            if (x >= 1.0d) {
                while (x >= 10.0d) {
                    x /= 10.0d;
                    exponent++;
                }
            } else {
                while (x > 0.0d && x < 1.0d) {
                    x *= 10.0d;
                    exponent--;
                }
            }
            x = MathLib.roundToPrecision(x, precision);
        }
        int absExponent = Math.abs(exponent);
        if (exponent >= 0) {
            expSign = '+';
        } else {
            expSign = '-';
        }
        if (useSignificantNumbers) {
            appendSignificantNumber(buffer, x, precision, repr);
        } else {
            appendPrecisionNumber(buffer, x, precision, repr);
        }
        buffer.append('e');
        buffer.append(expSign);
        StringLib.stringBufferAppend(buffer, (double) absExponent, 10, true, 2);
    }

    private String getStringArg(LuaCallFrame callFrame, int argc) {
        return getStringArg(callFrame, argc, names[6]);
    }

    private String getStringArg(LuaCallFrame callFrame, int argc, String funcname) {
        return (String) BaseLib.getArg(callFrame, argc, BaseLib.TYPE_STRING, funcname);
    }

    private Double getDoubleArg(LuaCallFrame callFrame, int argc) {
        return getDoubleArg(callFrame, argc, names[6]);
    }

    private Double getDoubleArg(LuaCallFrame callFrame, int argc, String funcname) {
        return (Double) BaseLib.getArg(callFrame, argc, BaseLib.TYPE_NUMBER, funcname);
    }

    private int lower(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "not enough arguments");
        callFrame.push(getStringArg(callFrame, 1, names[3]).toLowerCase());
        return 1;
    }

    private int upper(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "not enough arguments");
        callFrame.push(getStringArg(callFrame, 1, names[4]).toUpperCase());
        return 1;
    }

    private int reverse(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "not enough arguments");
        callFrame.push(new StringBuffer(getStringArg(callFrame, 1, names[5])).reverse().toString());
        return 1;
    }

    private int stringByte(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "not enough arguments");
        String s = getStringArg(callFrame, 1, names[2]);
        Double di = null;
        Double dj = null;
        if (nArguments >= 2) {
            di = getDoubleArg(callFrame, 2, names[2]);
            if (nArguments >= 3) {
                dj = getDoubleArg(callFrame, 3, names[2]);
            }
        }
        double di2 = 1.0d;
        if (di != null) {
            di2 = LuaState.fromDouble(di);
        }
        double dj2 = di2;
        if (dj != null) {
            dj2 = LuaState.fromDouble(dj);
        }
        int ii = (int) di2;
        int ij = (int) dj2;
        int len = s.length();
        if (ii < 0) {
            ii += len + 1;
        }
        if (ii <= 0) {
            ii = 1;
        }
        if (ij < 0) {
            ij += len + 1;
        } else if (ij > len) {
            ij = len;
        }
        int nReturns = (ij + 1) - ii;
        if (nReturns <= 0) {
            return 0;
        }
        callFrame.setTop(nReturns);
        int offset = ii - 1;
        for (int i = 0; i < nReturns; i++) {
            callFrame.set(i, new Double((double) s.charAt(offset + i)));
        }
        return nReturns;
    }

    private int stringChar(LuaCallFrame callFrame, int nArguments) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < nArguments; i++) {
            sb.append((char) getDoubleArg(callFrame, i + 1, names[1]).intValue());
        }
        return callFrame.push(sb.toString());
    }

    private int sub(LuaCallFrame callFrame, int nArguments) {
        String s = getStringArg(callFrame, 1, names[0]);
        double start = getDoubleArg(callFrame, 2, names[0]).doubleValue();
        double end = -1.0d;
        if (nArguments >= 3) {
            end = getDoubleArg(callFrame, 3, names[0]).doubleValue();
        }
        int istart = (int) start;
        int iend = (int) end;
        int len = s.length();
        if (istart < 0) {
            istart = Math.max((len + istart) + 1, 1);
        } else if (istart == 0) {
            istart = 1;
        }
        if (iend < 0) {
            iend = Math.max(0, (iend + len) + 1);
        } else if (iend > len) {
            iend = len;
        }
        if (istart > iend) {
            return callFrame.push("");
        }
        return callFrame.push(s.substring(istart - 1, iend));
    }

    private static Object push_onecapture(MatchState ms, int i, StringPointer s, StringPointer e) {
        String res;
        if (i < ms.level) {
            int l = ms.capture[i].len;
            if (l == -1) {
                throw new RuntimeException("unfinished capture");
            } else if (l == -2) {
                res = new Double((double) ((ms.src_init.length() - ms.capture[i].init.length()) + 1));
                ms.callFrame.push(res);
                return res;
            } else {
                int index = ms.capture[i].init.index;
                res = ms.capture[i].init.string.substring(index, index + l);
                ms.callFrame.push(res);
                return res;
            }
        } else if (i == 0) {
            res = s.string.substring(s.index, e.index);
            ms.callFrame.push(res);
            return res;
        } else {
            throw new RuntimeException("invalid capture index");
        }
    }

    private static int push_captures(MatchState ms, StringPointer s, StringPointer e) {
        boolean z = true;
        int nlevels = (ms.level != 0 || s == null) ? ms.level : 1;
        if (nlevels > 32) {
            z = false;
        }
        BaseLib.luaAssert(z, "too many captures");
        for (int i = 0; i < nlevels; i++) {
            StringLib.push_onecapture(ms, i, s, e);
        }
        return nlevels;
    }

    private static boolean noSpecialChars(String pattern) {
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c < 256 && SPECIALS[c]) {
                return false;
            }
        }
        return true;
    }

    private static int findAux(LuaCallFrame callFrame, boolean find) {
        String f = find ? names[7] : names[8];
        String source = (String) BaseLib.getArg(callFrame, 1, BaseLib.TYPE_STRING, f);
        String pattern = (String) BaseLib.getArg(callFrame, 2, BaseLib.TYPE_STRING, f);
        Double i = (Double) BaseLib.getOptArg(callFrame, 3, BaseLib.TYPE_NUMBER);
        boolean plain = LuaState.boolEval(BaseLib.getOptArg(callFrame, 4, BaseLib.TYPE_BOOLEAN));
        int init = i == null ? 0 : i.intValue() - 1;
        if (init < 0) {
            init += source.length();
            if (init < 0) {
                init = 0;
            }
        } else if (init > source.length()) {
            init = source.length();
        }
        if (!find || (!plain && !StringLib.noSpecialChars(pattern))) {
            StringPointer s = new StringPointer(source);
            StringPointer p = new StringPointer(pattern);
            MatchState ms = new MatchState();
            boolean anchor = false;
            if (p.getChar() == '^') {
                anchor = true;
                p.postIncrString(1);
            }
            StringPointer s1 = s.getClone();
            s1.postIncrString(init);
            ms.callFrame = callFrame;
            ms.src_init = s.getClone();
            ms.endIndex = s.getString().length();
            do {
                ms.level = 0;
                StringPointer res = StringLib.match(ms, s1, p);
                if (res == null) {
                    if (s1.postIncrStringI(1) >= ms.endIndex) {
                        break;
                    }
                } else if (!find) {
                    return StringLib.push_captures(ms, s1, res);
                } else {
                    return callFrame.push(new Double((double) ((s.length() - s1.length()) + 1)), new Double((double) (s.length() - res.length()))) + StringLib.push_captures(ms, null, null);
                }
            } while (!anchor);
        }
        int pos = source.indexOf(pattern, init);
        if (pos > -1) {
            return callFrame.push(LuaState.toDouble((long) (pos + 1)), LuaState.toDouble((long) (pattern.length() + pos)));
        }
        return callFrame.pushNil();
    }

    private static StringPointer startCapture(MatchState ms, StringPointer s, StringPointer p, int what) {
        int level = ms.level;
        BaseLib.luaAssert(level < 32, "too many captures");
        ms.capture[level].init = s.getClone();
        ms.capture[level].init.setIndex(s.getIndex());
        ms.capture[level].len = what;
        ms.level = level + 1;
        StringPointer res = StringLib.match(ms, s, p);
        if (res == null) {
            ms.level--;
        }
        return res;
    }

    private static int captureToClose(MatchState ms) {
        for (int level = ms.level - 1; level >= 0; level--) {
            if (ms.capture[level].len == -1) {
                return level;
            }
        }
        throw new RuntimeException("invalid pattern capture");
    }

    private static StringPointer endCapture(MatchState ms, StringPointer s, StringPointer p) {
        int l = StringLib.captureToClose(ms);
        ms.capture[l].len = ms.capture[l].init.length() - s.length();
        StringPointer res = StringLib.match(ms, s, p);
        if (res == null) {
            ms.capture[l].len = -1;
        }
        return res;
    }

    private static int checkCapture(MatchState ms, int l) {
        l -= 49;
        boolean z = l < 0 || l >= ms.level || ms.capture[l].len == -1;
        BaseLib.luaAssert(z, "invalid capture index");
        return l;
    }

    private static StringPointer matchCapture(MatchState ms, StringPointer s, int l) {
        l = StringLib.checkCapture(ms, l);
        int len = ms.capture[l].len;
        if (ms.endIndex - s.length() < len || ms.capture[l].init.compareTo(s, len) != 0) {
            return null;
        }
        StringPointer sp = s.getClone();
        sp.postIncrString(len);
        return sp;
    }

    private static StringPointer matchBalance(MatchState ms, StringPointer ss, StringPointer p) {
        boolean z = (p.getChar() == 0 || p.getChar(1) == 0) ? false : true;
        BaseLib.luaAssert(z, "unbalanced pattern");
        StringPointer s = ss.getClone();
        if (s.getChar() != p.getChar()) {
            return null;
        }
        char b = p.getChar();
        char e = p.getChar(1);
        int cont = 1;
        while (s.preIncrStringI(1) < ms.endIndex) {
            if (s.getChar() == e) {
                cont--;
                if (cont == 0) {
                    StringPointer sp = s.getClone();
                    sp.postIncrString(1);
                    return sp;
                }
            } else if (s.getChar() == b) {
                cont++;
            }
        }
        return null;
    }

    private static StringPointer classEnd(MatchState ms, StringPointer pp) {
        boolean z = false;
        StringPointer p = pp.getClone();
        switch (p.postIncrString(1)) {
            case '%':
                if (p.getChar() != 0) {
                    z = true;
                }
                BaseLib.luaAssert(z, "malformed pattern (ends with '%')");
                p.postIncrString(1);
                break;
            case '[':
                if (p.getChar() == '^') {
                    p.postIncrString(1);
                }
                do {
                    BaseLib.luaAssert(p.getChar() != 0, "malformed pattern (missing ']')");
                    if (p.postIncrString(1) == L_ESC && p.getChar() != 0) {
                        p.postIncrString(1);
                    }
                } while (p.getChar() != ']');
                p.postIncrString(1);
                break;
        }
        return p;
    }

    private static boolean singleMatch(char c, StringPointer p, StringPointer ep) {
        switch (p.getChar()) {
            case '%':
                return StringLib.matchClass(p.getChar(1), c);
            case MotionEventCompat.AXIS_GENERIC_15 /*46*/:
                return true;
            case '[':
                StringPointer sp = ep.getClone();
                sp.postIncrString(-1);
                return StringLib.matchBracketClass(c, p, sp);
            default:
                if (p.getChar() == c) {
                    return true;
                }
                return false;
        }
    }

    private static StringPointer minExpand(MatchState ms, StringPointer ss, StringPointer p, StringPointer ep) {
        StringPointer sp = ep.getClone();
        StringPointer s = ss.getClone();
        sp.postIncrString(1);
        while (true) {
            StringPointer res = StringLib.match(ms, s, sp);
            if (res != null) {
                return res;
            }
            if (s.getIndex() < ms.endIndex && StringLib.singleMatch(s.getChar(), p, ep)) {
                s.postIncrString(1);
            }
        }
        return null;
    }

    private static StringPointer maxExpand(MatchState ms, StringPointer s, StringPointer p, StringPointer ep) {
        int i = 0;
        while (s.getIndex() + i < ms.endIndex && StringLib.singleMatch(s.getChar(i), p, ep)) {
            i++;
        }
        while (i >= 0) {
            StringPointer sp1 = s.getClone();
            sp1.postIncrString(i);
            StringPointer sp2 = ep.getClone();
            sp2.postIncrString(1);
            StringPointer res = StringLib.match(ms, sp1, sp2);
            if (res != null) {
                return res;
            }
            i--;
        }
        return null;
    }

    private static boolean matchBracketClass(char c, StringPointer pp, StringPointer ecc) {
        boolean z = true;
        StringPointer p = pp.getClone();
        StringPointer ec = ecc.getClone();
        boolean sig = true;
        if (p.getChar(1) == '^') {
            sig = false;
            p.postIncrString(1);
        }
        while (p.preIncrStringI(1) < ec.getIndex()) {
            if (p.getChar() == L_ESC) {
                p.postIncrString(1);
                if (StringLib.matchClass(p.getChar(), c)) {
                    return sig;
                }
            } else if (p.getChar(1) == '-' && p.getIndex() + 2 < ec.getIndex()) {
                p.postIncrString(2);
                if (p.getChar(-2) <= c && c <= p.getChar()) {
                    return sig;
                }
            } else if (p.getChar() == c) {
                return sig;
            }
        }
        if (sig) {
            z = false;
        }
        return z;
    }

    private static p009se.krka.kahlua.stdlib.StringLib.StringPointer match(p009se.krka.kahlua.stdlib.StringLib.MatchState r13, p009se.krka.kahlua.stdlib.StringLib.StringPointer r14, p009se.krka.kahlua.stdlib.StringLib.StringPointer r15) {
        /*
        r9 = r14.getClone();
        r5 = r15.getClone();
        r2 = 1;
        r3 = 0;
    L_0x000a:
        if (r2 == 0) goto L_0x0159;
    L_0x000c:
        r2 = 0;
        r3 = 0;
        r11 = r5.getChar();
        switch(r11) {
            case 0: goto L_0x00f4;
            case 36: goto L_0x00f7;
            case 37: goto L_0x0069;
            case 40: goto L_0x003b;
            case 41: goto L_0x005c;
            default: goto L_0x0015;
        };
    L_0x0015:
        r3 = 1;
    L_0x0016:
        if (r3 == 0) goto L_0x000a;
    L_0x0018:
        r3 = 0;
        r0 = p009se.krka.kahlua.stdlib.StringLib.classEnd(r13, r5);
        r11 = r9.getIndex();
        r12 = r13.endIndex;
        if (r11 >= r12) goto L_0x010c;
    L_0x0025:
        r11 = r9.getChar();
        r11 = p009se.krka.kahlua.stdlib.StringLib.singleMatch(r11, r5, r0);
        if (r11 == 0) goto L_0x010c;
    L_0x002f:
        r4 = 1;
    L_0x0030:
        r11 = r0.getChar();
        switch(r11) {
            case 42: goto L_0x0132;
            case 43: goto L_0x0138;
            case 45: goto L_0x014b;
            case 63: goto L_0x010f;
            default: goto L_0x0037;
        };
    L_0x0037:
        if (r4 != 0) goto L_0x0151;
    L_0x0039:
        r11 = 0;
    L_0x003a:
        return r11;
    L_0x003b:
        r6 = r5.getClone();
        r11 = 1;
        r11 = r5.getChar(r11);
        r12 = 41;
        if (r11 != r12) goto L_0x0052;
    L_0x0048:
        r11 = 2;
        r6.postIncrString(r11);
        r11 = -2;
        r11 = p009se.krka.kahlua.stdlib.StringLib.startCapture(r13, r9, r6, r11);
        goto L_0x003a;
    L_0x0052:
        r11 = 1;
        r6.postIncrString(r11);
        r11 = -1;
        r11 = p009se.krka.kahlua.stdlib.StringLib.startCapture(r13, r9, r6, r11);
        goto L_0x003a;
    L_0x005c:
        r6 = r5.getClone();
        r11 = 1;
        r6.postIncrString(r11);
        r11 = p009se.krka.kahlua.stdlib.StringLib.endCapture(r13, r9, r6);
        goto L_0x003a;
    L_0x0069:
        r11 = 1;
        r11 = r5.getChar(r11);
        switch(r11) {
            case 98: goto L_0x0089;
            case 102: goto L_0x00a0;
            default: goto L_0x0071;
        };
    L_0x0071:
        r11 = 1;
        r11 = r5.getChar(r11);
        r11 = java.lang.Character.isDigit(r11);
        if (r11 == 0) goto L_0x00f1;
    L_0x007c:
        r11 = 1;
        r11 = r5.getChar(r11);
        r9 = p009se.krka.kahlua.stdlib.StringLib.matchCapture(r13, r9, r11);
        if (r9 != 0) goto L_0x00ea;
    L_0x0087:
        r11 = 0;
        goto L_0x003a;
    L_0x0089:
        r6 = r5.getClone();
        r11 = 2;
        r6.postIncrString(r11);
        r9 = p009se.krka.kahlua.stdlib.StringLib.matchBalance(r13, r9, r6);
        if (r9 != 0) goto L_0x0099;
    L_0x0097:
        r11 = 0;
        goto L_0x003a;
    L_0x0099:
        r11 = 4;
        r5.postIncrString(r11);
        r2 = 1;
        goto L_0x000a;
    L_0x00a0:
        r11 = 2;
        r5.postIncrString(r11);
        r11 = r5.getChar();
        r12 = 91;
        if (r11 != r12) goto L_0x00de;
    L_0x00ac:
        r11 = 1;
    L_0x00ad:
        r12 = "missing '[' after '%%f' in pattern";
        p009se.krka.kahlua.stdlib.BaseLib.luaAssert(r11, r12);
        r0 = p009se.krka.kahlua.stdlib.StringLib.classEnd(r13, r5);
        r11 = r9.getIndex();
        r12 = r13.src_init;
        r12 = r12.getIndex();
        if (r11 != r12) goto L_0x00e0;
    L_0x00c2:
        r7 = 0;
    L_0x00c3:
        r1 = r0.getClone();
        r11 = -1;
        r1.postIncrString(r11);
        r11 = p009se.krka.kahlua.stdlib.StringLib.matchBracketClass(r7, r5, r1);
        if (r11 != 0) goto L_0x00db;
    L_0x00d1:
        r11 = r9.getChar();
        r11 = p009se.krka.kahlua.stdlib.StringLib.matchBracketClass(r11, r5, r1);
        if (r11 != 0) goto L_0x00e6;
    L_0x00db:
        r11 = 0;
        goto L_0x003a;
    L_0x00de:
        r11 = 0;
        goto L_0x00ad;
    L_0x00e0:
        r11 = -1;
        r7 = r9.getChar(r11);
        goto L_0x00c3;
    L_0x00e6:
        r5 = r0;
        r2 = 1;
        goto L_0x000a;
    L_0x00ea:
        r11 = 2;
        r5.postIncrString(r11);
        r2 = 1;
        goto L_0x000a;
    L_0x00f1:
        r3 = 1;
        goto L_0x0016;
    L_0x00f4:
        r11 = r9;
        goto L_0x003a;
    L_0x00f7:
        r11 = 1;
        r11 = r5.getChar(r11);
        if (r11 != 0) goto L_0x0015;
    L_0x00fe:
        r11 = r9.getIndex();
        r12 = r13.endIndex;
        if (r11 != r12) goto L_0x0109;
    L_0x0106:
        r11 = r9;
        goto L_0x003a;
    L_0x0109:
        r11 = 0;
        goto L_0x003a;
    L_0x010c:
        r4 = 0;
        goto L_0x0030;
    L_0x010f:
        r10 = r9.getClone();
        r11 = 1;
        r10.postIncrString(r11);
        r1 = r0.getClone();
        r11 = 1;
        r1.postIncrString(r11);
        if (r4 == 0) goto L_0x012a;
    L_0x0121:
        r8 = p009se.krka.kahlua.stdlib.StringLib.match(r13, r10, r1);
        if (r8 == 0) goto L_0x012a;
    L_0x0127:
        r11 = r8;
        goto L_0x003a;
    L_0x012a:
        r5 = r0;
        r11 = 1;
        r5.postIncrString(r11);
        r2 = 1;
        goto L_0x000a;
    L_0x0132:
        r11 = p009se.krka.kahlua.stdlib.StringLib.maxExpand(r13, r9, r5, r0);
        goto L_0x003a;
    L_0x0138:
        r10 = r9.getClone();
        r11 = 1;
        r10.postIncrString(r11);
        if (r4 == 0) goto L_0x0148;
    L_0x0142:
        r11 = p009se.krka.kahlua.stdlib.StringLib.maxExpand(r13, r10, r5, r0);
        goto L_0x003a;
    L_0x0148:
        r11 = 0;
        goto L_0x003a;
    L_0x014b:
        r11 = p009se.krka.kahlua.stdlib.StringLib.minExpand(r13, r9, r5, r0);
        goto L_0x003a;
    L_0x0151:
        r11 = 1;
        r9.postIncrString(r11);
        r5 = r0;
        r2 = 1;
        goto L_0x000a;
    L_0x0159:
        r11 = 0;
        goto L_0x003a;
        */
        throw new UnsupportedOperationException("Method not decompiled: p009se.krka.kahlua.stdlib.StringLib.match(se.krka.kahlua.stdlib.StringLib$MatchState, se.krka.kahlua.stdlib.StringLib$StringPointer, se.krka.kahlua.stdlib.StringLib$StringPointer):se.krka.kahlua.stdlib.StringLib$StringPointer");
    }

    private static boolean matchClass(char classIdentifier, char c) {
        boolean res;
        boolean z;
        char lowerClassIdentifier = Character.toLowerCase(classIdentifier);
        switch (lowerClassIdentifier) {
            case 'a':
                if (!Character.isLowerCase(c) && !Character.isUpperCase(c)) {
                    res = false;
                    break;
                }
                res = true;
                break;
                break;
            case 'c':
                res = StringLib.isControl(c);
                break;
            case 'd':
                res = Character.isDigit(c);
                break;
            case GeocachingData.CACHE_SOURCE_OPENCACHING_UK /*108*/:
                res = Character.isLowerCase(c);
                break;
            case 'p':
                res = StringLib.isPunct(c);
                break;
            case 's':
                res = StringLib.isSpace(c);
                break;
            case 'u':
                res = Character.isUpperCase(c);
                break;
            case 'w':
                if (!Character.isLowerCase(c) && !Character.isUpperCase(c) && !Character.isDigit(c)) {
                    res = false;
                    break;
                }
                res = true;
                break;
                break;
            case ExtraData.PAR_RTE_COMPUTE_TYPE /*120*/:
                res = StringLib.isHex(c);
                break;
            case 'z':
                if (c != 0) {
                    res = false;
                    break;
                }
                res = true;
                break;
            default:
                if (classIdentifier == c) {
                    return true;
                }
                return false;
        }
        if (lowerClassIdentifier == classIdentifier) {
            z = true;
        } else {
            z = false;
        }
        if (z != res) {
            return false;
        }
        return true;
    }

    private static boolean isPunct(char c) {
        return (c >= '!' && c <= '/') || ((c >= ':' && c <= '@') || ((c >= '[' && c <= '`') || (c >= '{' && c <= '~')));
    }

    private static boolean isSpace(char c) {
        return (c >= 9 && c <= 13) || c == ' ';
    }

    private static boolean isControl(char c) {
        return (c >= 0 && c <= 31) || c == 127;
    }

    private static boolean isHex(char c) {
        return (c >= '0' && c <= '9') || ((c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'));
    }

    private static int gsub(LuaCallFrame cf, int nargs) {
        String srcTemp = (String) BaseLib.getArg(cf, 1, BaseLib.TYPE_STRING, names[9]);
        String pTemp = (String) BaseLib.getArg(cf, 2, BaseLib.TYPE_STRING, names[9]);
        Object repl = BaseLib.getArg(cf, 3, null, names[9]);
        String tmp = BaseLib.rawTostring(repl);
        if (tmp != null) {
            repl = tmp;
        }
        Double num = (Double) BaseLib.getOptArg(cf, 4, BaseLib.TYPE_NUMBER);
        int maxSubstitutions = num == null ? Integer.MAX_VALUE : num.intValue();
        StringPointer pattern = new StringPointer(pTemp);
        StringPointer src = new StringPointer(srcTemp);
        boolean anchor = false;
        if (pattern.getChar() == '^') {
            anchor = true;
            pattern.postIncrString(1);
        }
        String replType = BaseLib.type(repl);
        if (!(replType == BaseLib.TYPE_FUNCTION || replType == BaseLib.TYPE_STRING || replType == BaseLib.TYPE_TABLE)) {
            BaseLib.fail("string/function/table expected, got " + replType);
        }
        MatchState ms = new MatchState();
        ms.callFrame = cf;
        ms.src_init = src.getClone();
        ms.endIndex = src.length();
        int n = 0;
        StringBuffer b = new StringBuffer();
        while (n < maxSubstitutions) {
            ms.level = 0;
            StringPointer e = StringLib.match(ms, src, pattern);
            if (e != null) {
                n++;
                StringLib.addValue(ms, repl, b, src, e);
            }
            if (e == null || e.getIndex() <= src.getIndex()) {
                if (src.getIndex() >= ms.endIndex) {
                    break;
                }
                b.append(src.postIncrString(1));
                continue;
            } else {
                src.setIndex(e.getIndex());
                continue;
            }
            if (anchor) {
                break;
            }
        }
        return cf.push(b.append(src.getString()).toString(), new Double((double) n));
    }

    private static void addValue(MatchState ms, Object repl, StringBuffer b, StringPointer src, StringPointer e) {
        String type = BaseLib.type(repl);
        if (type == BaseLib.TYPE_NUMBER || type == BaseLib.TYPE_STRING) {
            b.append(StringLib.addString(ms, repl, src, e));
            return;
        }
        String match = src.getString().substring(0, e.getIndex() - src.getIndex());
        Object[] captures = ms.getCaptures();
        if (captures != null) {
            match = BaseLib.rawTostring(captures[0]);
        }
        Object res = null;
        if (type == BaseLib.TYPE_FUNCTION) {
            res = ms.callFrame.thread.state.call(repl, match, null, null);
        } else if (type == BaseLib.TYPE_TABLE) {
            res = ((LuaTable) repl).rawget(match);
        }
        if (res == null) {
            res = match;
        }
        b.append(BaseLib.rawTostring(res));
    }

    private static String addString(MatchState ms, Object repl, StringPointer s, StringPointer e) {
        String replTemp = BaseLib.tostring(repl, ms.callFrame.thread.state);
        StringPointer replStr = new StringPointer(replTemp);
        StringBuffer buf = new StringBuffer();
        int i = 0;
        while (i < replTemp.length()) {
            if (replStr.getChar(i) != L_ESC) {
                buf.append(replStr.getChar(i));
            } else {
                i++;
                if (!Character.isDigit(replStr.getChar(i))) {
                    buf.append(replStr.getChar(i));
                } else if (replStr.getChar(i) == '0') {
                    String str = s.getString();
                    int len = s.length() - e.length();
                    if (len > str.length()) {
                        len = str.length();
                    }
                    buf.append(str.substring(0, len));
                } else {
                    int captureIndex = replStr.getChar(i) - 49;
                    Object[] captures = ms.getCaptures();
                    if (captures == null || captureIndex > ms.level) {
                        throw new RuntimeException("invalid capture index");
                    }
                    Double o = captures[captureIndex];
                    if (o instanceof Double) {
                        Double doubleValue = o;
                        if (doubleValue.doubleValue() - ((double) doubleValue.intValue()) == 0.0d) {
                            buf.append(String.valueOf(o.intValue()));
                        } else {
                            buf.append(String.valueOf(o.doubleValue()));
                        }
                    } else {
                        buf.append(o);
                    }
                }
            }
            i++;
        }
        return buf.toString();
    }
}

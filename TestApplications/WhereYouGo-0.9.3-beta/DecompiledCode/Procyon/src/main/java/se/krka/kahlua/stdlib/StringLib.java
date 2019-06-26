// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.JavaFunction;

public final class StringLib implements JavaFunction
{
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
    private static final boolean[] SPECIALS;
    public static final Class STRING_CLASS;
    private static final int SUB = 0;
    private static final int UPPER = 4;
    private static final char[] digits;
    private static StringLib[] functions;
    private static final String[] names;
    private int methodId;
    
    static {
        SPECIALS = new boolean[256];
        for (int i = 0; i < "^$*+?.([%-".length(); ++i) {
            StringLib.SPECIALS["^$*+?.([%-".charAt(i)] = true;
        }
        STRING_CLASS = "".getClass();
        (names = new String[10])[0] = "sub";
        StringLib.names[1] = "char";
        StringLib.names[2] = "byte";
        StringLib.names[3] = "lower";
        StringLib.names[4] = "upper";
        StringLib.names[5] = "reverse";
        StringLib.names[6] = "format";
        StringLib.names[7] = "find";
        StringLib.names[8] = "match";
        StringLib.names[9] = "gsub";
        StringLib.functions = new StringLib[10];
        for (int j = 0; j < 10; ++j) {
            StringLib.functions[j] = new StringLib(j);
        }
        digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }
    
    public StringLib(final int methodId) {
        this.methodId = methodId;
    }
    
    private static String addString(final MatchState matchState, final Object o, final StringPointer stringPointer, final StringPointer stringPointer2) {
        final String tostring = BaseLib.tostring(o, matchState.callFrame.thread.state);
        final StringPointer stringPointer3 = new StringPointer(tostring);
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tostring.length(); ++i) {
            if (stringPointer3.getChar(i) != '%') {
                sb.append(stringPointer3.getChar(i));
            }
            else {
                final int n = i + 1;
                if (!Character.isDigit(stringPointer3.getChar(n))) {
                    sb.append(stringPointer3.getChar(n));
                    i = n;
                }
                else if (stringPointer3.getChar(n) == '0') {
                    final String string = stringPointer.getString();
                    int length;
                    if ((length = stringPointer.length() - stringPointer2.length()) > string.length()) {
                        length = string.length();
                    }
                    sb.append(string.substring(0, length));
                    i = n;
                }
                else {
                    final int n2 = stringPointer3.getChar(n) - '1';
                    final Object[] captures = matchState.getCaptures();
                    if (captures == null || n2 > matchState.level) {
                        throw new RuntimeException("invalid capture index");
                    }
                    final Object obj = captures[n2];
                    if (obj instanceof Double) {
                        final Double n3 = (Double)obj;
                        if (n3 - n3.intValue() == 0.0) {
                            sb.append(String.valueOf(((Double)obj).intValue()));
                            i = n;
                        }
                        else {
                            sb.append(String.valueOf((double)obj));
                            i = n;
                        }
                    }
                    else {
                        sb.append(obj);
                        i = n;
                    }
                }
            }
        }
        return sb.toString();
    }
    
    private static void addValue(final MatchState matchState, Object o, final StringBuffer sb, final StringPointer stringPointer, final StringPointer stringPointer2) {
        final String type = BaseLib.type(o);
        if (type == "number" || type == "string") {
            sb.append(addString(matchState, o, stringPointer, stringPointer2));
        }
        else {
            String s = stringPointer.getString().substring(0, stringPointer2.getIndex() - stringPointer.getIndex());
            final Object[] captures = matchState.getCaptures();
            if (captures != null) {
                s = BaseLib.rawTostring(captures[0]);
            }
            final Object o2 = null;
            Object o3;
            if (type == "function") {
                o3 = matchState.callFrame.thread.state.call(o, s, null, null);
            }
            else {
                o3 = o2;
                if (type == "table") {
                    o3 = ((LuaTable)o).rawget(s);
                }
            }
            if ((o = o3) == null) {
                o = s;
            }
            sb.append(BaseLib.rawTostring(o));
        }
    }
    
    private void append(final StringBuffer sb, final String s, int i, final int n) {
        while (i < n) {
            sb.append(s.charAt(i));
            ++i;
        }
    }
    
    private void appendPrecisionNumber(final StringBuffer sb, double a, final int n, final boolean b) {
        a = MathLib.roundToPrecision(a, n);
        final double floor = Math.floor(a);
        a -= floor;
        for (int i = 0; i < n; ++i) {
            a *= 10.0;
        }
        a = MathLib.round(floor + a);
        stringBufferAppend(sb, floor, 10, true, 0);
        if (b || n > 0) {
            sb.append('.');
        }
        stringBufferAppend(sb, a - floor, 10, false, n);
    }
    
    private void appendScientificNumber(final StringBuffer sb, double roundToPrecision, final int n, final boolean b, final boolean b2) {
        int a = 0;
        for (int i = 0; i < 2; ++i) {
            int n2 = a;
            double n3 = roundToPrecision;
            double n5;
            if (roundToPrecision >= 1.0) {
                int n4 = a;
                while (true) {
                    a = n4;
                    n5 = roundToPrecision;
                    if (roundToPrecision < 10.0) {
                        break;
                    }
                    roundToPrecision /= 10.0;
                    ++n4;
                }
            }
            else {
                while (true) {
                    a = n2;
                    n5 = n3;
                    if (n3 <= 0.0) {
                        break;
                    }
                    a = n2;
                    n5 = n3;
                    if (n3 >= 1.0) {
                        break;
                    }
                    n3 *= 10.0;
                    --n2;
                }
            }
            roundToPrecision = MathLib.roundToPrecision(n5, n);
        }
        final int abs = Math.abs(a);
        char c;
        if (a >= 0) {
            c = '+';
        }
        else {
            c = '-';
        }
        if (b2) {
            this.appendSignificantNumber(sb, roundToPrecision, n, b);
        }
        else {
            this.appendPrecisionNumber(sb, roundToPrecision, n, b);
        }
        sb.append('e');
        sb.append(c);
        stringBufferAppend(sb, abs, 10, true, 2);
    }
    
    private void appendSignificantNumber(final StringBuffer sb, double roundToSignificantNumbers, int length, final boolean b) {
        final double floor = Math.floor(roundToSignificantNumbers);
        stringBufferAppend(sb, floor, 10, true, 0);
        roundToSignificantNumbers = MathLib.roundToSignificantNumbers(roundToSignificantNumbers - floor, length);
        int n;
        if (floor == 0.0 && roundToSignificantNumbers != 0.0) {
            n = 1;
        }
        else {
            n = 0;
        }
        int n2 = 0;
        int n4;
        int n5;
        for (int n3 = length, i = 0; i < n3; ++i, n3 = n4, n2 = n5) {
            roundToSignificantNumbers *= 10.0;
            n4 = n3;
            n5 = n2;
            if (Math.floor(roundToSignificantNumbers) == 0.0) {
                n4 = n3;
                n5 = n2;
                if (roundToSignificantNumbers != 0.0) {
                    ++n2;
                    n4 = n3;
                    n5 = n2;
                    if (n != 0) {
                        n4 = n3 + 1;
                        n5 = n2;
                    }
                }
            }
        }
        double round;
        roundToSignificantNumbers = (round = MathLib.round(roundToSignificantNumbers));
        int n6 = length;
        if (!b) {
            while (true) {
                round = roundToSignificantNumbers;
                n6 = length;
                if (roundToSignificantNumbers <= 0.0) {
                    break;
                }
                round = roundToSignificantNumbers;
                n6 = length;
                if (roundToSignificantNumbers % 10.0 != 0.0) {
                    break;
                }
                roundToSignificantNumbers /= 10.0;
                --length;
            }
        }
        sb.append('.');
        length = sb.length();
        this.extend(sb, n2, '0');
        final int length2 = sb.length();
        stringBufferAppend(sb, round, 10, false, 0);
        final int n7 = sb.length() - length2;
        if (b && n7 < n6) {
            this.extend(sb, n6 - n7 - n2, '0');
        }
        if (!b && length == sb.length()) {
            sb.delete(length - 1, sb.length());
        }
    }
    
    private static int captureToClose(final MatchState matchState) {
        for (int i = matchState.level - 1; i >= 0; --i) {
            if (matchState.capture[i].len == -1) {
                return i;
            }
        }
        throw new RuntimeException("invalid pattern capture");
    }
    
    private static int checkCapture(final MatchState matchState, int n) {
        n -= 49;
        BaseLib.luaAssert(n < 0 || n >= matchState.level || matchState.capture[n].len == -1, "invalid capture index");
        return n;
    }
    
    private static StringPointer classEnd(final MatchState matchState, final StringPointer stringPointer) {
        boolean b = false;
        final StringPointer clone = stringPointer.getClone();
        switch (clone.postIncrString(1)) {
            case '%': {
                if (clone.getChar() != '\0') {
                    b = true;
                }
                BaseLib.luaAssert(b, "malformed pattern (ends with '%')");
                clone.postIncrString(1);
                break;
            }
            case '[': {
                if (clone.getChar() == '^') {
                    clone.postIncrString(1);
                }
                do {
                    BaseLib.luaAssert(clone.getChar() != '\0', "malformed pattern (missing ']')");
                    if (clone.postIncrString(1) == '%' && clone.getChar() != '\0') {
                        clone.postIncrString(1);
                    }
                } while (clone.getChar() != ']');
                clone.postIncrString(1);
                break;
            }
        }
        return clone;
    }
    
    private static StringPointer endCapture(final MatchState matchState, StringPointer match, final StringPointer stringPointer) {
        final int captureToClose = captureToClose(matchState);
        matchState.capture[captureToClose].len = matchState.capture[captureToClose].init.length() - match.length();
        match = match(matchState, match, stringPointer);
        if (match == null) {
            matchState.capture[captureToClose].len = -1;
        }
        return match;
    }
    
    private void extend(final StringBuffer sb, int i, final char ch) {
        final int length = sb.length();
        sb.setLength(length + i);
        --i;
        while (i >= 0) {
            sb.setCharAt(length + i, ch);
            --i;
        }
    }
    
    private static int findAux(final LuaCallFrame callFrame, final boolean b) {
        String s;
        if (b) {
            s = StringLib.names[7];
        }
        else {
            s = StringLib.names[8];
        }
        final String s2 = (String)BaseLib.getArg(callFrame, 1, "string", s);
        final String str = (String)BaseLib.getArg(callFrame, 2, "string", s);
        final Double n = (Double)BaseLib.getOptArg(callFrame, 3, "number");
        final boolean boolEval = LuaState.boolEval(BaseLib.getOptArg(callFrame, 4, "boolean"));
        int n2;
        if (n == null) {
            n2 = 0;
        }
        else {
            n2 = n.intValue() - 1;
        }
        int length;
        if (n2 < 0) {
            if ((length = n2 + s2.length()) < 0) {
                length = 0;
            }
        }
        else if ((length = n2) > s2.length()) {
            length = s2.length();
        }
        if (!b || (!boolEval && !noSpecialChars(str))) {
            final StringPointer stringPointer = new StringPointer(s2);
            final StringPointer stringPointer2 = new StringPointer(str);
            final MatchState matchState = new MatchState();
            boolean b2 = false;
            if (stringPointer2.getChar() == '^') {
                b2 = true;
                stringPointer2.postIncrString(1);
            }
            final StringPointer clone = stringPointer.getClone();
            clone.postIncrString(length);
            matchState.callFrame = callFrame;
            matchState.src_init = stringPointer.getClone();
            matchState.endIndex = stringPointer.getString().length();
            do {
                matchState.level = 0;
                final StringPointer match = match(matchState, clone, stringPointer2);
                if (match != null) {
                    if (b) {
                        return callFrame.push(new Double(stringPointer.length() - clone.length() + 1), new Double(stringPointer.length() - match.length())) + push_captures(matchState, null, null);
                    }
                    return push_captures(matchState, clone, match);
                }
            } while (clone.postIncrStringI(1) < matchState.endIndex && !b2);
            return callFrame.pushNil();
        }
        final int index = s2.indexOf(str, length);
        if (index <= -1) {
            return callFrame.pushNil();
        }
        return callFrame.push(LuaState.toDouble(index + 1), LuaState.toDouble(str.length() + index));
        n3 = callFrame.pushNil();
        return n3;
    }
    
    private int format(final LuaCallFrame luaCallFrame, int i) {
        final String s = (String)BaseLib.getArg(luaCallFrame, 1, "string", StringLib.names[6]);
        final int length = s.length();
        int n = 2;
        final StringBuffer sb = new StringBuffer();
    Label_0106:
        for (i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 == '%') {
                int index = i + 1;
                BaseLib.luaAssert(index < length, "incomplete option to 'format'");
                char c = s.charAt(index);
                if (c == '%') {
                    sb.append('%');
                    i = index;
                }
                else {
                    boolean b = false;
                    i = 0;
                    boolean b2 = false;
                    boolean b3 = false;
                    boolean b4 = false;
                    while (true) {
                        switch (c) {
                            default: {
                                int n2 = 0;
                                while (c >= '0' && c <= '9') {
                                    n2 = n2 * 10 + (c - '0');
                                    BaseLib.luaAssert(++index < length, "incomplete option to 'format'");
                                    c = s.charAt(index);
                                }
                                int a = 0;
                                final int n3 = 0;
                                int n4 = 0;
                                char c2 = c;
                                int n5 = index;
                                if (c == '.') {
                                    final int n6 = 1;
                                    BaseLib.luaAssert(++index < length, "incomplete option to 'format'");
                                    char c3 = s.charAt(index);
                                    int n7 = n3;
                                    while (true) {
                                        a = n7;
                                        c2 = c3;
                                        n4 = n6;
                                        n5 = index;
                                        if (c3 < '0') {
                                            break;
                                        }
                                        a = n7;
                                        c2 = c3;
                                        n4 = n6;
                                        n5 = index;
                                        if (c3 > '9') {
                                            break;
                                        }
                                        n7 = n7 * 10 + (c3 - '0');
                                        BaseLib.luaAssert(++index < length, "incomplete option to 'format'");
                                        c3 = s.charAt(index);
                                    }
                                }
                                int n8 = i;
                                if (b2) {
                                    n8 = 0;
                                }
                                final int n9 = 10;
                                final boolean b5 = false;
                                final int n10 = 6;
                                final String s2 = "";
                                int n11 = n9;
                                String s3 = s2;
                                i = n10;
                                int n12 = b5 ? 1 : 0;
                                int b6 = n2;
                                int n13 = n8;
                                Label_0709: {
                                    switch (c2) {
                                        default: {
                                            throw new RuntimeException("invalid option '%" + c2 + "' to 'format'");
                                        }
                                        case 99: {
                                            n13 = 0;
                                            b6 = n2;
                                            n12 = (b5 ? 1 : 0);
                                            i = n10;
                                            s3 = s2;
                                            n11 = n9;
                                            break Label_0709;
                                        }
                                        case 113: {
                                            b6 = 0;
                                            n11 = n9;
                                            s3 = s2;
                                            i = n10;
                                            n12 = (b5 ? 1 : 0);
                                            n13 = n8;
                                            break Label_0709;
                                        }
                                        case 115: {
                                            n13 = 0;
                                            n11 = n9;
                                            s3 = s2;
                                            i = n10;
                                            n12 = (b5 ? 1 : 0);
                                            b6 = n2;
                                            break Label_0709;
                                        }
                                        case 71: {
                                            n12 = 1;
                                            n11 = n9;
                                            s3 = s2;
                                            i = n10;
                                            b6 = n2;
                                            n13 = n8;
                                            break Label_0709;
                                        }
                                        case 69: {
                                            n12 = 1;
                                            n11 = n9;
                                            s3 = s2;
                                            i = n10;
                                            b6 = n2;
                                            n13 = n8;
                                            break Label_0709;
                                        }
                                        case 100:
                                        case 105: {
                                            i = 1;
                                            n11 = n9;
                                            s3 = s2;
                                            n12 = (b5 ? 1 : 0);
                                            b6 = n2;
                                            n13 = n8;
                                            break Label_0709;
                                        }
                                        case 117: {
                                            i = 1;
                                            n11 = n9;
                                            s3 = s2;
                                            n12 = (b5 ? 1 : 0);
                                            b6 = n2;
                                            n13 = n8;
                                            break Label_0709;
                                        }
                                        case 88: {
                                            n11 = 16;
                                            i = 1;
                                            n12 = 1;
                                            s3 = "0X";
                                            b6 = n2;
                                            n13 = n8;
                                            break Label_0709;
                                        }
                                        case 120: {
                                            n11 = 16;
                                            i = 1;
                                            s3 = "0x";
                                            n12 = (b5 ? 1 : 0);
                                            b6 = n2;
                                            n13 = n8;
                                            break Label_0709;
                                        }
                                        case 111: {
                                            n11 = 8;
                                            i = 1;
                                            s3 = "0";
                                            n12 = (b5 ? 1 : 0);
                                            b6 = n2;
                                            n13 = n8;
                                        }
                                        case 101:
                                        case 102:
                                        case 103: {
                                            if (n4 == 0) {
                                                a = i;
                                            }
                                            int n14 = n13;
                                            if (n4 != 0) {
                                                n14 = n13;
                                                if (n11 != 10) {
                                                    n14 = 0;
                                                }
                                            }
                                            char c4;
                                            if (n14 != 0) {
                                                i = (c4 = '0');
                                            }
                                            else {
                                                i = (c4 = ' ');
                                            }
                                            final int length2 = sb.length();
                                            if (!b2) {
                                                this.extend(sb, b6, c4);
                                            }
                                            switch (c2) {
                                                default: {
                                                    throw new RuntimeException("Internal error");
                                                }
                                                case 'c': {
                                                    sb.append((char)this.getDoubleArg(luaCallFrame, n).shortValue());
                                                    break;
                                                }
                                                case 'X':
                                                case 'o':
                                                case 'u':
                                                case 'x': {
                                                    final long unsigned = this.unsigned(this.getDoubleArg(luaCallFrame, n).longValue());
                                                    if (b) {
                                                        if (n11 == 8) {
                                                            long n15;
                                                            for (i = 0, n15 = unsigned; n15 > 0L; n15 /= 8L, ++i) {}
                                                            if (a <= i) {
                                                                sb.append(s3);
                                                            }
                                                        }
                                                        else if (n11 == 16 && unsigned != 0L) {
                                                            sb.append(s3);
                                                        }
                                                    }
                                                    if (unsigned != 0L || a > 0) {
                                                        stringBufferAppend(sb, (double)unsigned, n11, false, a);
                                                        break;
                                                    }
                                                    break;
                                                }
                                                case 'd':
                                                case 'i': {
                                                    final long longValue = this.getDoubleArg(luaCallFrame, n).longValue();
                                                    long n16;
                                                    if (longValue < 0L) {
                                                        sb.append('-');
                                                        n16 = -longValue;
                                                    }
                                                    else if (b3) {
                                                        sb.append('+');
                                                        n16 = longValue;
                                                    }
                                                    else {
                                                        n16 = longValue;
                                                        if (b4) {
                                                            sb.append(' ');
                                                            n16 = longValue;
                                                        }
                                                    }
                                                    if (n16 != 0L || a > 0) {
                                                        stringBufferAppend(sb, (double)n16, n11, false, a);
                                                        break;
                                                    }
                                                    break;
                                                }
                                                case 'E':
                                                case 'e':
                                                case 'f': {
                                                    final Double doubleArg = this.getDoubleArg(luaCallFrame, n);
                                                    if (doubleArg.isInfinite() || doubleArg.isNaN()) {
                                                        i = 1;
                                                    }
                                                    else {
                                                        i = 0;
                                                    }
                                                    final double doubleValue = doubleArg;
                                                    double n17;
                                                    if (MathLib.isNegative(doubleValue)) {
                                                        if (i == 0) {
                                                            sb.append('-');
                                                        }
                                                        n17 = -doubleValue;
                                                    }
                                                    else if (b3) {
                                                        sb.append('+');
                                                        n17 = doubleValue;
                                                    }
                                                    else {
                                                        n17 = doubleValue;
                                                        if (b4) {
                                                            sb.append(' ');
                                                            n17 = doubleValue;
                                                        }
                                                    }
                                                    if (i != 0) {
                                                        sb.append(BaseLib.numberToString(doubleArg));
                                                        break;
                                                    }
                                                    if (c2 == 'f') {
                                                        this.appendPrecisionNumber(sb, n17, a, b);
                                                        break;
                                                    }
                                                    this.appendScientificNumber(sb, n17, a, b, false);
                                                    break;
                                                }
                                                case 'G':
                                                case 'g': {
                                                    int n18 = a;
                                                    if (a <= 0) {
                                                        n18 = 1;
                                                    }
                                                    final Double doubleArg2 = this.getDoubleArg(luaCallFrame, n);
                                                    if (doubleArg2.isInfinite() || doubleArg2.isNaN()) {
                                                        i = 1;
                                                    }
                                                    else {
                                                        i = 0;
                                                    }
                                                    final double doubleValue2 = doubleArg2;
                                                    double n19;
                                                    if (MathLib.isNegative(doubleValue2)) {
                                                        if (i == 0) {
                                                            sb.append('-');
                                                        }
                                                        n19 = -doubleValue2;
                                                    }
                                                    else if (b3) {
                                                        sb.append('+');
                                                        n19 = doubleValue2;
                                                    }
                                                    else {
                                                        n19 = doubleValue2;
                                                        if (b4) {
                                                            sb.append(' ');
                                                            n19 = doubleValue2;
                                                        }
                                                    }
                                                    if (i != 0) {
                                                        sb.append(BaseLib.numberToString(doubleArg2));
                                                        break;
                                                    }
                                                    final double roundToSignificantNumbers = MathLib.roundToSignificantNumbers(n19, n18);
                                                    if (roundToSignificantNumbers == 0.0 || (roundToSignificantNumbers >= 1.0E-4 && roundToSignificantNumbers < MathLib.ipow(10.0, n18))) {
                                                        int n20;
                                                        if (roundToSignificantNumbers == 0.0) {
                                                            n20 = 1;
                                                        }
                                                        else if (Math.floor(roundToSignificantNumbers) == 0.0) {
                                                            n20 = 0;
                                                        }
                                                        else {
                                                            double n21 = roundToSignificantNumbers;
                                                            i = 1;
                                                            while (true) {
                                                                n20 = i;
                                                                if (n21 < 10.0) {
                                                                    break;
                                                                }
                                                                n21 /= 10.0;
                                                                ++i;
                                                            }
                                                        }
                                                        this.appendSignificantNumber(sb, roundToSignificantNumbers, n18 - n20, b);
                                                        break;
                                                    }
                                                    this.appendScientificNumber(sb, roundToSignificantNumbers, n18 - 1, b, true);
                                                    break;
                                                }
                                                case 's': {
                                                    final String stringArg = this.getStringArg(luaCallFrame, n);
                                                    i = stringArg.length();
                                                    if (n4 != 0) {
                                                        i = Math.min(a, stringArg.length());
                                                    }
                                                    this.append(sb, stringArg, 0, i);
                                                    break;
                                                }
                                                case 'q': {
                                                    final String stringArg2 = this.getStringArg(luaCallFrame, n);
                                                    sb.append('\"');
                                                    char char2;
                                                    for (i = 0; i < stringArg2.length(); ++i) {
                                                        char2 = stringArg2.charAt(i);
                                                        switch (char2) {
                                                            default: {
                                                                sb.append(char2);
                                                                break;
                                                            }
                                                            case 92: {
                                                                sb.append("\\");
                                                                break;
                                                            }
                                                            case 10: {
                                                                sb.append("\\\n");
                                                                break;
                                                            }
                                                            case 13: {
                                                                sb.append("\\r");
                                                                break;
                                                            }
                                                            case 34: {
                                                                sb.append("\\\"");
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    sb.append('\"');
                                                    break;
                                                }
                                            }
                                            if (b2) {
                                                i = b6 - (sb.length() - length2);
                                                if (i > 0) {
                                                    this.extend(sb, i, ' ');
                                                }
                                            }
                                            else {
                                                i = Math.min(sb.length() - length2 - b6, b6);
                                                if (i > 0) {
                                                    sb.delete(length2, length2 + i);
                                                }
                                                if (n14 != 0) {
                                                    i = length2 + (b6 - i);
                                                    final char char3 = sb.charAt(i);
                                                    if (char3 == '+' || char3 == '-' || char3 == ' ') {
                                                        sb.setCharAt(i, '0');
                                                        sb.setCharAt(length2, char3);
                                                    }
                                                }
                                            }
                                            if (n12 != 0) {
                                                this.stringBufferUpperCase(sb, length2);
                                            }
                                            ++n;
                                            i = n5;
                                            continue Label_0106;
                                        }
                                    }
                                }
                                break;
                            }
                            case 45: {
                                b2 = true;
                                break;
                            }
                            case 43: {
                                b3 = true;
                                break;
                            }
                            case 32: {
                                b4 = true;
                                break;
                            }
                            case 35: {
                                b = true;
                                break;
                            }
                            case 48: {
                                i = 1;
                                break;
                            }
                        }
                        BaseLib.luaAssert(++index < length, "incomplete option to 'format'");
                        c = s.charAt(index);
                    }
                }
            }
            else {
                sb.append(char1);
            }
        }
        luaCallFrame.push(sb.toString());
        return 1;
    }
    
    private Double getDoubleArg(final LuaCallFrame luaCallFrame, final int n) {
        return this.getDoubleArg(luaCallFrame, n, StringLib.names[6]);
    }
    
    private Double getDoubleArg(final LuaCallFrame luaCallFrame, final int n, final String s) {
        return (Double)BaseLib.getArg(luaCallFrame, n, "number", s);
    }
    
    private String getStringArg(final LuaCallFrame luaCallFrame, final int n) {
        return this.getStringArg(luaCallFrame, n, StringLib.names[6]);
    }
    
    private String getStringArg(final LuaCallFrame luaCallFrame, final int n, final String s) {
        return (String)BaseLib.getArg(luaCallFrame, n, "string", s);
    }
    
    private static int gsub(final LuaCallFrame callFrame, int n) {
        final String s = (String)BaseLib.getArg(callFrame, 1, "string", StringLib.names[9]);
        final String s2 = (String)BaseLib.getArg(callFrame, 2, "string", StringLib.names[9]);
        Object arg = BaseLib.getArg(callFrame, 3, null, StringLib.names[9]);
        final String rawTostring = BaseLib.rawTostring(arg);
        if (rawTostring != null) {
            arg = rawTostring;
        }
        final Double n2 = (Double)BaseLib.getOptArg(callFrame, 4, "number");
        int intValue;
        if (n2 == null) {
            intValue = Integer.MAX_VALUE;
        }
        else {
            intValue = n2.intValue();
        }
        final StringPointer stringPointer = new StringPointer(s2);
        final StringPointer stringPointer2 = new StringPointer(s);
        boolean b = false;
        if (stringPointer.getChar() == '^') {
            b = true;
            stringPointer.postIncrString(1);
        }
        final String type = BaseLib.type(arg);
        if (type != "function" && type != "string" && type != "table") {
            BaseLib.fail("string/function/table expected, got " + type);
        }
        final MatchState matchState = new MatchState();
        matchState.callFrame = callFrame;
        matchState.src_init = stringPointer2.getClone();
        matchState.endIndex = stringPointer2.length();
        int n3 = 0;
        final StringBuffer sb = new StringBuffer();
        int n4;
        while ((n4 = n3) < intValue) {
            matchState.level = 0;
            final StringPointer match = match(matchState, stringPointer2, stringPointer);
            n = n3;
            if (match != null) {
                n = n3 + 1;
                addValue(matchState, arg, sb, stringPointer2, match);
            }
            if (match != null && match.getIndex() > stringPointer2.getIndex()) {
                stringPointer2.setIndex(match.getIndex());
            }
            else {
                n4 = n;
                if (stringPointer2.getIndex() >= matchState.endIndex) {
                    break;
                }
                sb.append(stringPointer2.postIncrString(1));
            }
            n3 = n;
            if (b) {
                n4 = n;
                break;
            }
        }
        return callFrame.push(sb.append(stringPointer2.getString()).toString(), new Double(n4));
    }
    
    private static boolean isControl(final char c) {
        return (c >= '\0' && c <= '\u001f') || c == '\u007f';
    }
    
    private static boolean isHex(final char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }
    
    private static boolean isPunct(final char c) {
        return (c >= '!' && c <= '/') || (c >= ':' && c <= '@') || (c >= '[' && c <= '`') || (c >= '{' && c <= '~');
    }
    
    private static boolean isSpace(final char c) {
        return (c >= '\t' && c <= '\r') || c == ' ';
    }
    
    private int lower(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "not enough arguments");
        luaCallFrame.push(this.getStringArg(luaCallFrame, 1, StringLib.names[3]).toLowerCase());
        return 1;
    }
    
    private static StringPointer match(final MatchState matchState, StringPointer stringPointer, StringPointer stringPointer2) {
        stringPointer = stringPointer.getClone();
        stringPointer2 = stringPointer2.getClone();
        int i = 1;
        while (i != 0) {
            i = 0;
            StringPointer stringPointer3 = null;
            Label_0072: {
                switch (stringPointer2.getChar()) {
                    case '(': {
                        final StringPointer clone = stringPointer2.getClone();
                        if (stringPointer2.getChar(1) == ')') {
                            clone.postIncrString(2);
                            stringPointer3 = startCapture(matchState, stringPointer, clone, -2);
                            return stringPointer3;
                        }
                        clone.postIncrString(1);
                        stringPointer3 = startCapture(matchState, stringPointer, clone, -1);
                        return stringPointer3;
                    }
                    case ')': {
                        stringPointer2 = stringPointer2.getClone();
                        stringPointer2.postIncrString(1);
                        stringPointer3 = endCapture(matchState, stringPointer, stringPointer2);
                        return stringPointer3;
                    }
                    case '%': {
                        switch (stringPointer2.getChar(1)) {
                            default: {
                                if (!Character.isDigit(stringPointer2.getChar(1))) {
                                    break Label_0072;
                                }
                                stringPointer = matchCapture(matchState, stringPointer, stringPointer2.getChar(1));
                                if (stringPointer == null) {
                                    stringPointer3 = null;
                                    return stringPointer3;
                                }
                                stringPointer2.postIncrString(2);
                                i = 1;
                                continue;
                            }
                            case 'b': {
                                final StringPointer clone2 = stringPointer2.getClone();
                                clone2.postIncrString(2);
                                stringPointer = matchBalance(matchState, stringPointer, clone2);
                                if (stringPointer == null) {
                                    stringPointer3 = null;
                                    return stringPointer3;
                                }
                                stringPointer2.postIncrString(4);
                                i = 1;
                                continue;
                            }
                            case 'f': {
                                stringPointer2.postIncrString(2);
                                BaseLib.luaAssert(stringPointer2.getChar() == '[', "missing '[' after '%%f' in pattern");
                                final StringPointer classEnd = classEnd(matchState, stringPointer2);
                                char char1;
                                if (stringPointer.getIndex() == matchState.src_init.getIndex()) {
                                    char1 = '\0';
                                }
                                else {
                                    char1 = stringPointer.getChar(-1);
                                }
                                final StringPointer clone3 = classEnd.getClone();
                                clone3.postIncrString(-1);
                                if (matchBracketClass(char1, stringPointer2, clone3) || !matchBracketClass(stringPointer.getChar(), stringPointer2, clone3)) {
                                    stringPointer3 = null;
                                    return stringPointer3;
                                }
                                stringPointer2 = classEnd;
                                i = 1;
                                continue;
                            }
                        }
                        break;
                    }
                    case '\0': {
                        stringPointer3 = stringPointer;
                        return stringPointer3;
                    }
                    case '$': {
                        if (stringPointer2.getChar(1) != '\0') {
                            break;
                        }
                        if (stringPointer.getIndex() == matchState.endIndex) {
                            stringPointer3 = stringPointer;
                            return stringPointer3;
                        }
                        stringPointer3 = null;
                        return stringPointer3;
                    }
                }
            }
            if (!true) {
                continue;
            }
            final StringPointer classEnd2 = classEnd(matchState, stringPointer2);
            boolean b;
            if (stringPointer.getIndex() < matchState.endIndex && singleMatch(stringPointer.getChar(), stringPointer2, classEnd2)) {
                b = true;
            }
            else {
                b = false;
            }
            switch (classEnd2.getChar()) {
                default: {
                    if (!b) {
                        stringPointer3 = null;
                        break;
                    }
                    stringPointer.postIncrString(1);
                    stringPointer2 = classEnd2;
                    i = 1;
                    continue;
                }
                case '?': {
                    final StringPointer clone4 = stringPointer.getClone();
                    clone4.postIncrString(1);
                    stringPointer2 = classEnd2.getClone();
                    stringPointer2.postIncrString(1);
                    if (b) {
                        stringPointer2 = match(matchState, clone4, stringPointer2);
                        if (stringPointer2 != null) {
                            stringPointer3 = stringPointer2;
                            break;
                        }
                    }
                    stringPointer2 = classEnd2;
                    stringPointer2.postIncrString(1);
                    i = 1;
                    continue;
                }
                case '*': {
                    stringPointer3 = maxExpand(matchState, stringPointer, stringPointer2, classEnd2);
                    break;
                }
                case '+': {
                    stringPointer = stringPointer.getClone();
                    stringPointer.postIncrString(1);
                    if (b) {
                        stringPointer3 = maxExpand(matchState, stringPointer, stringPointer2, classEnd2);
                        break;
                    }
                    stringPointer3 = null;
                    break;
                }
                case '-': {
                    stringPointer3 = minExpand(matchState, stringPointer, stringPointer2, classEnd2);
                    break;
                }
            }
            return stringPointer3;
        }
        return null;
    }
    
    private static StringPointer matchBalance(final MatchState matchState, StringPointer clone, final StringPointer stringPointer) {
        final StringPointer stringPointer2 = null;
        BaseLib.luaAssert(stringPointer.getChar() != '\0' && stringPointer.getChar(1) != '\0', "unbalanced pattern");
        final StringPointer clone2 = clone.getClone();
        if (clone2.getChar() != stringPointer.getChar()) {
            clone = stringPointer2;
        }
        else {
            final char char1 = stringPointer.getChar();
            final char char2 = stringPointer.getChar(1);
            int n = 1;
            while (true) {
                clone = stringPointer2;
                if (clone2.preIncrStringI(1) >= matchState.endIndex) {
                    return clone;
                }
                if (clone2.getChar() == char2) {
                    if (--n == 0) {
                        break;
                    }
                    continue;
                }
                else {
                    if (clone2.getChar() != char1) {
                        continue;
                    }
                    ++n;
                }
            }
            clone = clone2.getClone();
            clone.postIncrString(1);
        }
        return clone;
    }
    
    private static boolean matchBracketClass(final char c, StringPointer clone, StringPointer clone2) {
        final boolean b = true;
        clone = clone.getClone();
        clone2 = clone2.getClone();
        boolean b2 = true;
        if (clone.getChar(1) == '^') {
            b2 = false;
            clone.postIncrString(1);
        }
        while (clone.preIncrStringI(1) < clone2.getIndex()) {
            if (clone.getChar() == '%') {
                clone.postIncrString(1);
                if (!matchClass(clone.getChar(), c)) {
                    continue;
                }
            }
            else if (clone.getChar(1) == '-' && clone.getIndex() + 2 < clone2.getIndex()) {
                clone.postIncrString(2);
                if (clone.getChar(-2) > c || c > clone.getChar()) {
                    continue;
                }
            }
            else if (clone.getChar() != c) {
                continue;
            }
            return b2;
        }
        b2 = (!b2 && b);
        return b2;
    }
    
    private static StringPointer matchCapture(final MatchState matchState, final StringPointer stringPointer, int checkCapture) {
        checkCapture = checkCapture(matchState, checkCapture);
        final int len = matchState.capture[checkCapture].len;
        StringPointer clone;
        if (matchState.endIndex - stringPointer.length() >= len && matchState.capture[checkCapture].init.compareTo(stringPointer, len) == 0) {
            clone = stringPointer.getClone();
            clone.postIncrString(len);
        }
        else {
            clone = null;
        }
        return clone;
    }
    
    private static boolean matchClass(final char ch, final char c) {
        boolean b = true;
        final char lowerCase = Character.toLowerCase(ch);
        int n = 0;
        Label_0124: {
            switch (lowerCase) {
                default: {
                    if (ch == c) {
                        break;
                    }
                    b = false;
                    break;
                }
                case 97: {
                    if (Character.isLowerCase(c) || Character.isUpperCase(c)) {
                        n = 1;
                        break Label_0124;
                    }
                    n = 0;
                    break Label_0124;
                }
                case 99: {
                    n = (isControl(c) ? 1 : 0);
                    break Label_0124;
                }
                case 100: {
                    n = (Character.isDigit(c) ? 1 : 0);
                    break Label_0124;
                }
                case 108: {
                    n = (Character.isLowerCase(c) ? 1 : 0);
                    break Label_0124;
                }
                case 112: {
                    n = (isPunct(c) ? 1 : 0);
                    break Label_0124;
                }
                case 115: {
                    n = (isSpace(c) ? 1 : 0);
                    break Label_0124;
                }
                case 117: {
                    n = (Character.isUpperCase(c) ? 1 : 0);
                    break Label_0124;
                }
                case 119: {
                    if (Character.isLowerCase(c) || Character.isUpperCase(c) || Character.isDigit(c)) {
                        n = 1;
                    }
                    else {
                        n = 0;
                    }
                    break Label_0124;
                }
                case 120: {
                    n = (isHex(c) ? 1 : 0);
                    break Label_0124;
                }
                case 122: {
                    if (c == '\0') {
                        n = 1;
                    }
                    else {
                        n = 0;
                    }
                    break Label_0124;
                }
            }
            return b;
        }
        int n2;
        if (lowerCase == ch) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        if (n2 != n) {
            b = false;
            return b;
        }
        return b;
    }
    
    private static StringPointer maxExpand(final MatchState matchState, final StringPointer stringPointer, StringPointer stringPointer2, final StringPointer stringPointer3) {
        int n = 0;
        int i;
        while (true) {
            i = n;
            if (stringPointer.getIndex() + n >= matchState.endIndex) {
                break;
            }
            i = n;
            if (!singleMatch(stringPointer.getChar(n), stringPointer2, stringPointer3)) {
                break;
            }
            ++n;
        }
        while (i >= 0) {
            stringPointer2 = stringPointer.getClone();
            stringPointer2.postIncrString(i);
            final StringPointer clone = stringPointer3.getClone();
            clone.postIncrString(1);
            stringPointer2 = match(matchState, stringPointer2, clone);
            if (stringPointer2 != null) {
                return stringPointer2;
            }
            --i;
        }
        return null;
    }
    
    private static StringPointer minExpand(final MatchState matchState, StringPointer match, final StringPointer stringPointer, final StringPointer stringPointer2) {
        final StringPointer clone = stringPointer2.getClone();
        final StringPointer clone2 = match.getClone();
        clone.postIncrString(1);
        StringPointer stringPointer3;
        while (true) {
            match = match(matchState, clone2, clone);
            if (match != null) {
                stringPointer3 = match;
                break;
            }
            if (clone2.getIndex() >= matchState.endIndex || !singleMatch(clone2.getChar(), stringPointer, stringPointer2)) {
                stringPointer3 = null;
                break;
            }
            clone2.postIncrString(1);
        }
        return stringPointer3;
    }
    
    private static boolean noSpecialChars(final String s) {
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 < '\u0100' && StringLib.SPECIALS[char1]) {
                return false;
            }
        }
        return true;
    }
    
    private static int push_captures(final MatchState matchState, final StringPointer stringPointer, final StringPointer stringPointer2) {
        boolean b = true;
        int level;
        if (matchState.level == 0 && stringPointer != null) {
            level = 1;
        }
        else {
            level = matchState.level;
        }
        if (level > 32) {
            b = false;
        }
        BaseLib.luaAssert(b, "too many captures");
        for (int i = 0; i < level; ++i) {
            push_onecapture(matchState, i, stringPointer, stringPointer2);
        }
        return level;
    }
    
    private static Object push_onecapture(final MatchState matchState, final int n, final StringPointer stringPointer, final StringPointer stringPointer2) {
        String s;
        if (n >= matchState.level) {
            if (n != 0) {
                throw new RuntimeException("invalid capture index");
            }
            final String substring = stringPointer.string.substring(stringPointer.index, stringPointer2.index);
            matchState.callFrame.push(substring);
            s = substring;
        }
        else {
            final int len = matchState.capture[n].len;
            if (len == -1) {
                throw new RuntimeException("unfinished capture");
            }
            if (len == -2) {
                final Double n2 = new Double(matchState.src_init.length() - matchState.capture[n].init.length() + 1);
                matchState.callFrame.push(n2);
                s = (String)n2;
            }
            else {
                final int access$000 = matchState.capture[n].init.index;
                final String substring2 = matchState.capture[n].init.string.substring(access$000, access$000 + len);
                matchState.callFrame.push(substring2);
                s = substring2;
            }
        }
        return s;
    }
    
    public static void register(final LuaState luaState) {
        final LuaTableImpl luaTableImpl = new LuaTableImpl();
        luaState.getEnvironment().rawset("string", luaTableImpl);
        for (int i = 0; i < 10; ++i) {
            luaTableImpl.rawset(StringLib.names[i], StringLib.functions[i]);
        }
        luaTableImpl.rawset("__index", luaTableImpl);
        luaState.setClassMetatable(StringLib.STRING_CLASS, luaTableImpl);
    }
    
    private int reverse(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "not enough arguments");
        luaCallFrame.push(new StringBuffer(this.getStringArg(luaCallFrame, 1, StringLib.names[5])).reverse().toString());
        return 1;
    }
    
    private static boolean singleMatch(final char c, final StringPointer stringPointer, StringPointer clone) {
        boolean b = true;
        switch (stringPointer.getChar()) {
            default: {
                b = (stringPointer.getChar() == c && b);
                return b;
            }
            case '[': {
                clone = clone.getClone();
                clone.postIncrString(-1);
                b = matchBracketClass(c, stringPointer, clone);
                return b;
            }
            case '%': {
                b = matchClass(stringPointer.getChar(1), c);
            }
            case '.': {
                return b;
            }
        }
    }
    
    private static StringPointer startCapture(final MatchState matchState, StringPointer match, final StringPointer stringPointer, final int len) {
        final int level = matchState.level;
        BaseLib.luaAssert(level < 32, "too many captures");
        (matchState.capture[level].init = match.getClone()).setIndex(match.getIndex());
        matchState.capture[level].len = len;
        matchState.level = level + 1;
        match = match(matchState, match, stringPointer);
        if (match == null) {
            --matchState.level;
        }
        return match;
    }
    
    private static void stringBufferAppend(final StringBuffer sb, double n, int i, final boolean b, int n2) {
        final int length = sb.length();
        while (n > 0.0 || n2 > 0) {
            final double floor = Math.floor(n / i);
            sb.append(StringLib.digits[(int)(n - i * floor)]);
            n = floor;
            --n2;
        }
        n2 = sb.length() - 1;
        if (length > n2 && b) {
            sb.append('0');
        }
        else {
            int n3;
            int n4;
            char char1;
            for (i = (n2 + 1 - length) / 2 - 1; i >= 0; --i) {
                n3 = length + i;
                n4 = n2 - i;
                char1 = sb.charAt(n3);
                sb.setCharAt(n3, sb.charAt(n4));
                sb.setCharAt(n4, char1);
            }
        }
    }
    
    private void stringBufferUpperCase(final StringBuffer sb, int i) {
        while (i < sb.length()) {
            final char char1 = sb.charAt(i);
            if (char1 >= 'a' && char1 <= 'z') {
                sb.setCharAt(i, (char)(char1 - ' '));
            }
            ++i;
        }
    }
    
    private int stringByte(final LuaCallFrame luaCallFrame, int n) {
        BaseLib.luaAssert(n >= 1, "not enough arguments");
        final String stringArg = this.getStringArg(luaCallFrame, 1, StringLib.names[2]);
        Object doubleArg = null;
        Object doubleArg2;
        final Object o = doubleArg2 = null;
        if (n >= 2) {
            final Double n2 = (Double)(doubleArg = this.getDoubleArg(luaCallFrame, 2, StringLib.names[2]));
            doubleArg2 = o;
            if (n >= 3) {
                doubleArg2 = this.getDoubleArg(luaCallFrame, 3, StringLib.names[2]);
                doubleArg = n2;
            }
        }
        double fromDouble = 1.0;
        if (doubleArg != null) {
            fromDouble = LuaState.fromDouble(doubleArg);
        }
        double fromDouble2 = fromDouble;
        if (doubleArg2 != null) {
            fromDouble2 = LuaState.fromDouble(doubleArg2);
        }
        final int n3 = (int)fromDouble;
        final int n4 = (int)fromDouble2;
        final int length = stringArg.length();
        if ((n = n3) < 0) {
            n = n3 + (length + 1);
        }
        int n5;
        if ((n5 = n) <= 0) {
            n5 = 1;
        }
        if (n4 < 0) {
            n = n4 + (length + 1);
        }
        else if ((n = n4) > length) {
            n = length;
        }
        final int top = n + 1 - n5;
        int n6;
        if (top <= 0) {
            n6 = 0;
        }
        else {
            luaCallFrame.setTop(top);
            n = 0;
            while (true) {
                n6 = top;
                if (n >= top) {
                    break;
                }
                luaCallFrame.set(n, new Double(stringArg.charAt(n5 - 1 + n)));
                ++n;
            }
        }
        return n6;
    }
    
    private int stringChar(final LuaCallFrame luaCallFrame, final int n) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; ++i) {
            sb.append((char)this.getDoubleArg(luaCallFrame, i + 1, StringLib.names[1]).intValue());
        }
        return luaCallFrame.push(sb.toString());
    }
    
    private int sub(final LuaCallFrame luaCallFrame, int n) {
        final String stringArg = this.getStringArg(luaCallFrame, 1, StringLib.names[0]);
        final double doubleValue = this.getDoubleArg(luaCallFrame, 2, StringLib.names[0]);
        double doubleValue2 = -1.0;
        if (n >= 3) {
            doubleValue2 = this.getDoubleArg(luaCallFrame, 3, StringLib.names[0]);
        }
        final int n2 = (int)doubleValue;
        final int n3 = (int)doubleValue2;
        final int length = stringArg.length();
        if (n2 < 0) {
            n = Math.max(length + n2 + 1, 1);
        }
        else if ((n = n2) == 0) {
            n = 1;
        }
        int max;
        if (n3 < 0) {
            max = Math.max(0, n3 + length + 1);
        }
        else if ((max = n3) > length) {
            max = length;
        }
        if (n > max) {
            n = luaCallFrame.push("");
        }
        else {
            n = luaCallFrame.push(stringArg.substring(n - 1, max));
        }
        return n;
    }
    
    private long unsigned(final long n) {
        long n2 = n;
        if (n < 0L) {
            n2 = n + 4294967296L;
        }
        return n2;
    }
    
    private int upper(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "not enough arguments");
        luaCallFrame.push(this.getStringArg(luaCallFrame, 1, StringLib.names[4]).toUpperCase());
        return 1;
    }
    
    @Override
    public int call(final LuaCallFrame luaCallFrame, int n) {
        final int n2 = 0;
        switch (this.methodId) {
            default: {
                n = n2;
                break;
            }
            case 0: {
                n = this.sub(luaCallFrame, n);
                break;
            }
            case 1: {
                n = this.stringChar(luaCallFrame, n);
                break;
            }
            case 2: {
                n = this.stringByte(luaCallFrame, n);
                break;
            }
            case 3: {
                n = this.lower(luaCallFrame, n);
                break;
            }
            case 4: {
                n = this.upper(luaCallFrame, n);
                break;
            }
            case 5: {
                n = this.reverse(luaCallFrame, n);
                break;
            }
            case 6: {
                n = this.format(luaCallFrame, n);
                break;
            }
            case 7: {
                n = findAux(luaCallFrame, true);
                break;
            }
            case 8: {
                n = findAux(luaCallFrame, false);
                break;
            }
            case 9: {
                n = gsub(luaCallFrame, n);
                break;
            }
        }
        return n;
    }
    
    @Override
    public String toString() {
        return StringLib.names[this.methodId];
    }
    
    public static class MatchState
    {
        public LuaCallFrame callFrame;
        public Capture[] capture;
        public int endIndex;
        public int level;
        public StringPointer src_init;
        
        public MatchState() {
            this.capture = new Capture[32];
            for (int i = 0; i < 32; ++i) {
                this.capture[i] = new Capture();
            }
        }
        
        public Object[] getCaptures() {
            Object[] array;
            if (this.level <= 0) {
                array = null;
            }
            else {
                final String[] array2 = new String[this.level];
                int n = 0;
                while (true) {
                    array = array2;
                    if (n >= this.level) {
                        break;
                    }
                    if (this.capture[n].len == -2) {
                        array2[n] = (String)new Double(this.src_init.length() - this.capture[n].init.length() + 1);
                    }
                    else {
                        array2[n] = this.capture[n].init.getString().substring(0, this.capture[n].len);
                    }
                    ++n;
                }
            }
            return array;
        }
        
        public static class Capture
        {
            public StringPointer init;
            public int len;
        }
    }
    
    public static class StringPointer
    {
        private int index;
        private String string;
        
        public StringPointer(final String string) {
            this.index = 0;
            this.string = string;
        }
        
        public StringPointer(final String string, final int index) {
            this.index = 0;
            this.string = string;
            this.index = index;
        }
        
        public int compareTo(final StringPointer stringPointer, final int n) {
            return this.string.substring(this.index, this.index + n).compareTo(stringPointer.string.substring(stringPointer.index, stringPointer.index + n));
        }
        
        public char getChar() {
            return this.getChar(0);
        }
        
        public char getChar(int n) {
            char char1;
            if (this.index + n >= this.string.length()) {
                n = (char1 = '\0');
            }
            else {
                n = (char1 = this.string.charAt(this.index + n));
            }
            return char1;
        }
        
        public StringPointer getClone() {
            return new StringPointer(this.getOriginalString(), this.getIndex());
        }
        
        public int getIndex() {
            return this.index;
        }
        
        public String getOriginalString() {
            return this.string;
        }
        
        public String getString() {
            return this.getString(0);
        }
        
        public String getString(final int n) {
            return this.string.substring(this.index + n, this.string.length());
        }
        
        public int length() {
            return this.string.length() - this.index;
        }
        
        public char postIncrString(final int n) {
            final char char1 = this.getChar();
            this.index += n;
            return char1;
        }
        
        public int postIncrStringI(final int n) {
            final int index = this.index;
            this.index += n;
            return index;
        }
        
        public char preIncrString(final int n) {
            this.index += n;
            return this.getChar();
        }
        
        public int preIncrStringI(final int n) {
            return this.index += n;
        }
        
        public void setIndex(final int index) {
            this.index = index;
        }
        
        public void setOriginalString(final String string) {
            this.string = string;
        }
    }
}

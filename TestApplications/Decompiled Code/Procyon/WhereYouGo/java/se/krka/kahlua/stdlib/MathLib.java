// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.LuaTableImpl;
import java.util.Random;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.JavaFunction;

public final class MathLib implements JavaFunction
{
    private static final int ABS = 0;
    private static final int ACOS = 1;
    private static final int ASIN = 2;
    private static final int ATAN = 3;
    private static final int ATAN2 = 4;
    private static final int CEIL = 5;
    private static final int COS = 6;
    private static final int COSH = 7;
    private static final int DEG = 8;
    public static final double EPS = 1.0E-15;
    private static final int EXP = 9;
    private static final int FLOOR = 10;
    private static final int FMOD = 11;
    private static final int FREXP = 12;
    private static final int LDEXP = 13;
    private static final double LN10_INV;
    private static final double LN2_INV;
    private static final int LOG = 14;
    private static final int LOG10 = 15;
    private static final int MODF = 16;
    private static final int NUM_FUNCTIONS = 26;
    static final double PIO2 = 1.5707963267948966;
    private static final int POW = 17;
    private static final int RAD = 18;
    private static final int RANDOM = 19;
    private static final int RANDOMSEED = 20;
    private static final int SIN = 21;
    private static final int SINH = 22;
    private static final int SQRT = 23;
    private static final int TAN = 24;
    private static final int TANH = 25;
    private static MathLib[] functions;
    private static String[] names;
    static final double p0 = 896.7859740366387;
    static final double p1 = 1780.406316433197;
    static final double p2 = 1153.029351540485;
    static final double p3 = 268.42548195503974;
    static final double p4 = 16.15364129822302;
    static final double q0 = 896.7859740366387;
    static final double q1 = 2079.33497444541;
    static final double q2 = 1666.7838148816338;
    static final double q3 = 536.2653740312153;
    static final double q4 = 58.95697050844462;
    static final double sq2m1 = 0.41421356237309503;
    static final double sq2p1 = 2.414213562373095;
    private int index;
    
    static {
        (MathLib.names = new String[26])[0] = "abs";
        MathLib.names[1] = "acos";
        MathLib.names[2] = "asin";
        MathLib.names[3] = "atan";
        MathLib.names[4] = "atan2";
        MathLib.names[5] = "ceil";
        MathLib.names[6] = "cos";
        MathLib.names[7] = "cosh";
        MathLib.names[8] = "deg";
        MathLib.names[9] = "exp";
        MathLib.names[10] = "floor";
        MathLib.names[11] = "fmod";
        MathLib.names[12] = "frexp";
        MathLib.names[13] = "ldexp";
        MathLib.names[14] = "log";
        MathLib.names[15] = "log10";
        MathLib.names[16] = "modf";
        MathLib.names[17] = "pow";
        MathLib.names[18] = "rad";
        MathLib.names[19] = "random";
        MathLib.names[20] = "randomseed";
        MathLib.names[21] = "sin";
        MathLib.names[22] = "sinh";
        MathLib.names[23] = "sqrt";
        MathLib.names[24] = "tan";
        MathLib.names[25] = "tanh";
        LN10_INV = 1.0 / ln(10.0);
        LN2_INV = 1.0 / ln(2.0);
    }
    
    public MathLib(final int index) {
        this.index = index;
    }
    
    private static int abs(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(Math.abs(getDoubleArg(luaCallFrame, 1, MathLib.names[0]))));
        return 1;
    }
    
    public static double acos(double n) {
        if (n > 1.0 || n < -1.0) {
            n = Double.NaN;
        }
        else {
            n = 1.5707963267948966 - asin(n);
        }
        return n;
    }
    
    private static int acos(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(acos(getDoubleArg(luaCallFrame, 1, MathLib.names[1]))));
        return 1;
    }
    
    public static double asin(double n) {
        int n2 = 0;
        double n3 = n;
        if (n < 0.0) {
            n3 = -n;
            n2 = 0 + 1;
        }
        double n4;
        if (n3 > 1.0) {
            n4 = Double.NaN;
        }
        else {
            n = Math.sqrt(1.0 - n3 * n3);
            if (n3 > 0.7) {
                n = 1.5707963267948966 - atan(n / n3);
            }
            else {
                n = atan(n3 / n);
            }
            n4 = n;
            if (n2 > 0) {
                n4 = -n;
            }
        }
        return n4;
    }
    
    private static int asin(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(asin(getDoubleArg(luaCallFrame, 1, MathLib.names[2]))));
        return 1;
    }
    
    public static double atan(double msatan) {
        if (msatan > 0.0) {
            msatan = msatan(msatan);
        }
        else {
            msatan = -msatan(-msatan);
        }
        return msatan;
    }
    
    private static int atan(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(atan(getDoubleArg(luaCallFrame, 1, MathLib.names[3]))));
        return 1;
    }
    
    public static double atan2(double atan, final double n) {
        if (atan + n == atan) {
            if (atan > 0.0) {
                atan = 1.5707963267948966;
            }
            else if (atan < 0.0) {
                atan = -1.5707963267948966;
            }
            else {
                atan = 0.0;
            }
        }
        else {
            final double n2 = atan = atan(atan / n);
            if (n < 0.0) {
                if (n2 <= 0.0) {
                    atan = n2 + 3.141592653589793;
                }
                else {
                    atan = n2 - 3.141592653589793;
                }
            }
        }
        return atan;
    }
    
    private static int atan2(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 2, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(atan2(getDoubleArg(luaCallFrame, 1, MathLib.names[4]), getDoubleArg(luaCallFrame, 2, MathLib.names[4]))));
        return 1;
    }
    
    private static int ceil(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(Math.ceil(getDoubleArg(luaCallFrame, 1, MathLib.names[5]))));
        return 1;
    }
    
    private static int cos(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(Math.cos(getDoubleArg(luaCallFrame, 1, MathLib.names[6]))));
        return 1;
    }
    
    private static int cosh(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        final double exp = exp(getDoubleArg(luaCallFrame, 1, MathLib.names[7]));
        luaCallFrame.push(LuaState.toDouble((1.0 / exp + exp) * 0.5));
        return 1;
    }
    
    private static int deg(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(Math.toDegrees(getDoubleArg(luaCallFrame, 1, MathLib.names[8]))));
        return 1;
    }
    
    public static double exp(final double n) {
        double a = 1.0;
        double n2 = 1.0;
        double n3 = 0.0;
        while (Math.abs(a) > 1.0E-15) {
            n3 += a;
            a = a * n / n2;
            ++n2;
        }
        return n3;
    }
    
    private static int exp(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(exp(getDoubleArg(luaCallFrame, 1, MathLib.names[9]))));
        return 1;
    }
    
    private static int floor(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(Math.floor(getDoubleArg(luaCallFrame, 1, MathLib.names[10]))));
        return 1;
    }
    
    private static int fmod(final LuaCallFrame luaCallFrame, int n) {
        BaseLib.luaAssert(n >= 2, "Not enough arguments");
        double doubleArg = getDoubleArg(luaCallFrame, 1, MathLib.names[11]);
        final double doubleArg2 = getDoubleArg(luaCallFrame, 2, MathLib.names[11]);
        if (Double.isInfinite(doubleArg) || Double.isNaN(doubleArg)) {
            doubleArg = Double.NaN;
        }
        else if (!Double.isInfinite(doubleArg2)) {
            final double abs = Math.abs(doubleArg2);
            n = 0;
            double n2 = doubleArg;
            if (isNegative(doubleArg)) {
                n = 1;
                n2 = -doubleArg;
            }
            final double n3 = doubleArg = n2 - Math.floor(n2 / abs) * abs;
            if (n != 0) {
                doubleArg = -n3;
            }
        }
        luaCallFrame.push(LuaState.toDouble(doubleArg));
        return 1;
    }
    
    private static double fpow(double exp, final double n) {
        if (exp < 0.0) {
            exp = Double.NaN;
        }
        else {
            exp = exp(ln(exp) * n);
        }
        return exp;
    }
    
    private static int frexp(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        double doubleArg = getDoubleArg(luaCallFrame, 1, MathLib.names[12]);
        double ceil;
        if (Double.isInfinite(doubleArg) || Double.isNaN(doubleArg)) {
            ceil = 0.0;
        }
        else {
            ceil = Math.ceil(ln(doubleArg) * MathLib.LN2_INV);
            doubleArg /= 1 << (int)ceil;
        }
        luaCallFrame.push(LuaState.toDouble(doubleArg), LuaState.toDouble(ceil));
        return 2;
    }
    
    private static double getDoubleArg(final LuaCallFrame luaCallFrame, final int n, final String s) {
        return (double)BaseLib.getArg(luaCallFrame, n, "number", s);
    }
    
    private static void initFunctions() {
        synchronized (MathLib.class) {
            if (MathLib.functions == null) {
                MathLib.functions = new MathLib[26];
                for (int i = 0; i < 26; ++i) {
                    MathLib.functions[i] = new MathLib(i);
                }
            }
        }
    }
    
    public static double ipow(double n, int i) {
        boolean b = false;
        int n2 = i;
        if (isNegative(i)) {
            n2 = -i;
            b = true;
        }
        double n3;
        if ((n2 & 0x1) != 0x0) {
            n3 = n;
        }
        else {
            n3 = 1.0;
        }
        i = n2 >> 1;
        double n4 = n;
        n = n3;
        while (i != 0) {
            n4 *= n4;
            double n5 = n;
            if ((i & 0x1) != 0x0) {
                n5 = n * n4;
            }
            i >>= 1;
            n = n5;
        }
        double n6 = n;
        if (b) {
            n6 = 1.0 / n;
        }
        return n6;
    }
    
    public static boolean isNegative(final double value) {
        return Double.doubleToLongBits(value) < 0L;
    }
    
    private static int ldexp(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 2, "Not enough arguments");
        double doubleArg = getDoubleArg(luaCallFrame, 1, MathLib.names[13]);
        final double doubleArg2 = getDoubleArg(luaCallFrame, 2, MathLib.names[13]);
        final double n2 = doubleArg + doubleArg2;
        if (!Double.isInfinite(n2) && !Double.isNaN(n2)) {
            doubleArg *= 1 << (int)doubleArg2;
        }
        luaCallFrame.push(LuaState.toDouble(doubleArg));
        return 1;
    }
    
    public static double ln(double v) {
        boolean b = false;
        if (v < 0.0) {
            v = Double.NaN;
        }
        else if (v == 0.0) {
            v = Double.NEGATIVE_INFINITY;
        }
        else if (Double.isInfinite(v)) {
            v = Double.POSITIVE_INFINITY;
        }
        else {
            double sqrt = v;
            if (v < 1.0) {
                b = true;
                sqrt = 1.0 / v;
            }
            int n = 1;
            while (sqrt >= 1.1) {
                n *= 2;
                sqrt = Math.sqrt(sqrt);
            }
            final double n2 = v = 1.0 - sqrt;
            int n3 = 1;
            double n4 = 0.0;
            while (true) {
                final double a = v / n3;
                if (Math.abs(a) <= 1.0E-15) {
                    break;
                }
                n4 -= a;
                v *= n2;
                ++n3;
            }
            final double n5 = v = n * n4;
            if (b) {
                v = -n5;
            }
        }
        return v;
    }
    
    private static int log(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(ln(getDoubleArg(luaCallFrame, 1, MathLib.names[14]))));
        return 1;
    }
    
    private static int log10(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(ln(getDoubleArg(luaCallFrame, 1, MathLib.names[15])) * MathLib.LN10_INV));
        return 1;
    }
    
    private static int modf(final LuaCallFrame luaCallFrame, int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        final double doubleArg = getDoubleArg(luaCallFrame, 1, MathLib.names[16]);
        n = 0;
        double a = doubleArg;
        if (isNegative(doubleArg)) {
            n = 1;
            a = -doubleArg;
        }
        final double floor = Math.floor(a);
        double n2;
        if (Double.isInfinite(floor)) {
            n2 = 0.0;
        }
        else {
            n2 = a - floor;
        }
        double n3 = n2;
        double n4 = floor;
        if (n != 0) {
            n4 = -floor;
            n3 = -n2;
        }
        luaCallFrame.push(LuaState.toDouble(n4), LuaState.toDouble(n3));
        return 2;
    }
    
    private static double msatan(double mxatan) {
        if (mxatan < 0.41421356237309503) {
            mxatan = mxatan(mxatan);
        }
        else if (mxatan > 2.414213562373095) {
            mxatan = 1.5707963267948966 - mxatan(1.0 / mxatan);
        }
        else {
            mxatan = 0.7853981633974483 + mxatan((mxatan - 1.0) / (1.0 + mxatan));
        }
        return mxatan;
    }
    
    private static double mxatan(final double n) {
        final double n2 = n * n;
        return ((((16.15364129822302 * n2 + 268.42548195503974) * n2 + 1153.029351540485) * n2 + 1780.406316433197) * n2 + 896.7859740366387) / (((((58.95697050844462 + n2) * n2 + 536.2653740312153) * n2 + 1666.7838148816338) * n2 + 2079.33497444541) * n2 + 896.7859740366387) * n;
    }
    
    public static double pow(double n, final double n2) {
        if ((int)n2 == n2) {
            n = ipow(n, (int)n2);
        }
        else {
            n = fpow(n, n2);
        }
        return n;
    }
    
    private static int pow(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 2, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(pow(getDoubleArg(luaCallFrame, 1, MathLib.names[17]), getDoubleArg(luaCallFrame, 2, MathLib.names[17]))));
        return 1;
    }
    
    private static int rad(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(Math.toRadians(getDoubleArg(luaCallFrame, 1, MathLib.names[18]))));
        return 1;
    }
    
    private int random(final LuaCallFrame luaCallFrame, int n) {
        final Random random = luaCallFrame.thread.state.random;
        if (n == 0) {
            luaCallFrame.push(LuaState.toDouble(random.nextDouble()));
        }
        else {
            int n2 = (int)getDoubleArg(luaCallFrame, 1, MathLib.names[19]);
            if (n == 1) {
                n = 1;
            }
            else {
                final int n3 = (int)getDoubleArg(luaCallFrame, 2, MathLib.names[19]);
                n = n2;
                n2 = n3;
            }
            luaCallFrame.push(LuaState.toDouble(random.nextInt(n2 - n + 1) + n));
        }
        return 1;
    }
    
    private int randomseed(final LuaCallFrame luaCallFrame, final int n) {
        boolean b = true;
        if (n < 1) {
            b = false;
        }
        BaseLib.luaAssert(b, "Not enough arguments");
        final Object value = luaCallFrame.get(0);
        if (value != null) {
            luaCallFrame.thread.state.random.setSeed(value.hashCode());
        }
        return 0;
    }
    
    public static void register(final LuaState luaState) {
        initFunctions();
        final LuaTableImpl luaTableImpl = new LuaTableImpl();
        luaState.getEnvironment().rawset("math", luaTableImpl);
        luaTableImpl.rawset("pi", LuaState.toDouble(3.141592653589793));
        luaTableImpl.rawset("huge", LuaState.toDouble(Double.POSITIVE_INFINITY));
        for (int i = 0; i < 26; ++i) {
            luaTableImpl.rawset(MathLib.names[i], MathLib.functions[i]);
        }
    }
    
    public static double round(double floor) {
        if (floor < 0.0) {
            floor = -round(-floor);
        }
        else {
            final double a = floor + 0.5;
            final double n = floor = Math.floor(a);
            if (n == a) {
                floor = n - ((long)n & 0x1L);
            }
        }
        return floor;
    }
    
    public static double roundToPrecision(final double n, final int n2) {
        final double ipow = ipow(10.0, n2);
        return round(n * ipow) / ipow;
    }
    
    public static double roundToSignificantNumbers(double n, final int n2) {
        final double n3 = 0.0;
        if (n == 0.0) {
            n = n3;
        }
        else if (n < 0.0) {
            n = -roundToSignificantNumbers(-n, n2);
        }
        else {
            final double ipow = ipow(10.0, n2 - 1);
            double n4 = 1.0;
            double n5;
            while (true) {
                n5 = n4;
                if (n4 * n >= ipow) {
                    break;
                }
                n4 *= 10.0;
            }
            while (n5 * n >= ipow * 10.0) {
                n5 /= 10.0;
            }
            n = round(n * n5) / n5;
        }
        return n;
    }
    
    private static int sin(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(Math.sin(getDoubleArg(luaCallFrame, 1, MathLib.names[21]))));
        return 1;
    }
    
    private static int sinh(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        final double exp = exp(getDoubleArg(luaCallFrame, 1, MathLib.names[22]));
        luaCallFrame.push(LuaState.toDouble((exp - 1.0 / exp) * 0.5));
        return 1;
    }
    
    private static int sqrt(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(Math.sqrt(getDoubleArg(luaCallFrame, 1, MathLib.names[23]))));
        return 1;
    }
    
    private static int tan(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        luaCallFrame.push(LuaState.toDouble(Math.tan(getDoubleArg(luaCallFrame, 1, MathLib.names[24]))));
        return 1;
    }
    
    private static int tanh(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "Not enough arguments");
        final double exp = exp(2.0 * getDoubleArg(luaCallFrame, 1, MathLib.names[25]));
        luaCallFrame.push(LuaState.toDouble((exp - 1.0) / (1.0 + exp)));
        return 1;
    }
    
    @Override
    public int call(final LuaCallFrame luaCallFrame, int n) {
        switch (this.index) {
            default: {
                n = 0;
                break;
            }
            case 0: {
                n = abs(luaCallFrame, n);
                break;
            }
            case 1: {
                n = acos(luaCallFrame, n);
                break;
            }
            case 2: {
                n = asin(luaCallFrame, n);
                break;
            }
            case 3: {
                n = atan(luaCallFrame, n);
                break;
            }
            case 4: {
                n = atan2(luaCallFrame, n);
                break;
            }
            case 5: {
                n = ceil(luaCallFrame, n);
                break;
            }
            case 6: {
                n = cos(luaCallFrame, n);
                break;
            }
            case 7: {
                n = cosh(luaCallFrame, n);
                break;
            }
            case 8: {
                n = deg(luaCallFrame, n);
                break;
            }
            case 9: {
                n = exp(luaCallFrame, n);
                break;
            }
            case 10: {
                n = floor(luaCallFrame, n);
                break;
            }
            case 11: {
                n = fmod(luaCallFrame, n);
                break;
            }
            case 12: {
                n = frexp(luaCallFrame, n);
                break;
            }
            case 13: {
                n = ldexp(luaCallFrame, n);
                break;
            }
            case 14: {
                n = log(luaCallFrame, n);
                break;
            }
            case 15: {
                n = log10(luaCallFrame, n);
                break;
            }
            case 16: {
                n = modf(luaCallFrame, n);
                break;
            }
            case 17: {
                n = pow(luaCallFrame, n);
                break;
            }
            case 18: {
                n = rad(luaCallFrame, n);
                break;
            }
            case 19: {
                n = this.random(luaCallFrame, n);
                break;
            }
            case 20: {
                n = this.randomseed(luaCallFrame, n);
                break;
            }
            case 21: {
                n = sin(luaCallFrame, n);
                break;
            }
            case 22: {
                n = sinh(luaCallFrame, n);
                break;
            }
            case 23: {
                n = sqrt(luaCallFrame, n);
                break;
            }
            case 24: {
                n = tan(luaCallFrame, n);
                break;
            }
            case 25: {
                n = tanh(luaCallFrame, n);
                break;
            }
        }
        return n;
    }
    
    @Override
    public String toString() {
        return "math." + MathLib.names[this.index];
    }
}

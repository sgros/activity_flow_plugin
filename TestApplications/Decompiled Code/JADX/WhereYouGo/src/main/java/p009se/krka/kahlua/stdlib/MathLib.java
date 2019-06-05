package p009se.krka.kahlua.stdlib;

import java.util.Random;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;

/* renamed from: se.krka.kahlua.stdlib.MathLib */
public final class MathLib implements JavaFunction {
    private static final int ABS = 0;
    private static final int ACOS = 1;
    private static final int ASIN = 2;
    private static final int ATAN = 3;
    private static final int ATAN2 = 4;
    private static final int CEIL = 5;
    private static final int COS = 6;
    private static final int COSH = 7;
    private static final int DEG = 8;
    public static final double EPS = 1.0E-15d;
    private static final int EXP = 9;
    private static final int FLOOR = 10;
    private static final int FMOD = 11;
    private static final int FREXP = 12;
    private static final int LDEXP = 13;
    private static final double LN10_INV = (1.0d / MathLib.m34ln(10.0d));
    private static final double LN2_INV = (1.0d / MathLib.m34ln(2.0d));
    private static final int LOG = 14;
    private static final int LOG10 = 15;
    private static final int MODF = 16;
    private static final int NUM_FUNCTIONS = 26;
    static final double PIO2 = 1.5707963267948966d;
    private static final int POW = 17;
    private static final int RAD = 18;
    private static final int RANDOM = 19;
    private static final int RANDOMSEED = 20;
    private static final int SIN = 21;
    private static final int SINH = 22;
    private static final int SQRT = 23;
    private static final int TAN = 24;
    private static final int TANH = 25;
    private static MathLib[] functions = null;
    private static String[] names = new String[26];
    /* renamed from: p0 */
    static final double f86p0 = 896.7859740366387d;
    /* renamed from: p1 */
    static final double f87p1 = 1780.406316433197d;
    /* renamed from: p2 */
    static final double f88p2 = 1153.029351540485d;
    /* renamed from: p3 */
    static final double f89p3 = 268.42548195503974d;
    /* renamed from: p4 */
    static final double f90p4 = 16.15364129822302d;
    /* renamed from: q0 */
    static final double f91q0 = 896.7859740366387d;
    /* renamed from: q1 */
    static final double f92q1 = 2079.33497444541d;
    /* renamed from: q2 */
    static final double f93q2 = 1666.7838148816338d;
    /* renamed from: q3 */
    static final double f94q3 = 536.2653740312153d;
    /* renamed from: q4 */
    static final double f95q4 = 58.95697050844462d;
    static final double sq2m1 = 0.41421356237309503d;
    static final double sq2p1 = 2.414213562373095d;
    private int index;

    static {
        names[0] = "abs";
        names[1] = "acos";
        names[2] = "asin";
        names[3] = "atan";
        names[4] = "atan2";
        names[5] = "ceil";
        names[6] = "cos";
        names[7] = "cosh";
        names[8] = "deg";
        names[9] = "exp";
        names[10] = "floor";
        names[11] = "fmod";
        names[12] = "frexp";
        names[13] = "ldexp";
        names[14] = "log";
        names[15] = "log10";
        names[16] = "modf";
        names[17] = "pow";
        names[18] = "rad";
        names[19] = "random";
        names[20] = "randomseed";
        names[21] = "sin";
        names[22] = "sinh";
        names[23] = "sqrt";
        names[24] = "tan";
        names[25] = "tanh";
    }

    public MathLib(int index) {
        this.index = index;
    }

    public static void register(LuaState state) {
        MathLib.initFunctions();
        LuaTable math = new LuaTableImpl();
        state.getEnvironment().rawset("math", math);
        math.rawset("pi", LuaState.toDouble(3.141592653589793d));
        math.rawset("huge", LuaState.toDouble(Double.POSITIVE_INFINITY));
        for (int i = 0; i < 26; i++) {
            math.rawset(names[i], functions[i]);
        }
    }

    private static synchronized void initFunctions() {
        synchronized (MathLib.class) {
            if (functions == null) {
                functions = new MathLib[26];
                for (int i = 0; i < 26; i++) {
                    functions[i] = new MathLib(i);
                }
            }
        }
    }

    public String toString() {
        return "math." + names[this.index];
    }

    public int call(LuaCallFrame callFrame, int nArguments) {
        switch (this.index) {
            case 0:
                return MathLib.abs(callFrame, nArguments);
            case 1:
                return MathLib.acos(callFrame, nArguments);
            case 2:
                return MathLib.asin(callFrame, nArguments);
            case 3:
                return MathLib.atan(callFrame, nArguments);
            case 4:
                return MathLib.atan2(callFrame, nArguments);
            case 5:
                return MathLib.ceil(callFrame, nArguments);
            case 6:
                return MathLib.cos(callFrame, nArguments);
            case 7:
                return MathLib.cosh(callFrame, nArguments);
            case 8:
                return MathLib.deg(callFrame, nArguments);
            case 9:
                return MathLib.exp(callFrame, nArguments);
            case 10:
                return MathLib.floor(callFrame, nArguments);
            case 11:
                return MathLib.fmod(callFrame, nArguments);
            case 12:
                return MathLib.frexp(callFrame, nArguments);
            case 13:
                return MathLib.ldexp(callFrame, nArguments);
            case 14:
                return MathLib.log(callFrame, nArguments);
            case 15:
                return MathLib.log10(callFrame, nArguments);
            case 16:
                return MathLib.modf(callFrame, nArguments);
            case 17:
                return MathLib.pow(callFrame, nArguments);
            case 18:
                return MathLib.rad(callFrame, nArguments);
            case 19:
                return random(callFrame, nArguments);
            case 20:
                return randomseed(callFrame, nArguments);
            case 21:
                return MathLib.sin(callFrame, nArguments);
            case 22:
                return MathLib.sinh(callFrame, nArguments);
            case 23:
                return MathLib.sqrt(callFrame, nArguments);
            case 24:
                return MathLib.tan(callFrame, nArguments);
            case 25:
                return MathLib.tanh(callFrame, nArguments);
            default:
                return 0;
        }
    }

    private static double getDoubleArg(LuaCallFrame callFrame, int argc, String funcname) {
        return ((Double) BaseLib.getArg(callFrame, argc, BaseLib.TYPE_NUMBER, funcname)).doubleValue();
    }

    private static int abs(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 1) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        callFrame.push(LuaState.toDouble(Math.abs(MathLib.getDoubleArg(callFrame, 1, names[0]))));
        return 1;
    }

    private static int ceil(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(Math.ceil(MathLib.getDoubleArg(callFrame, 1, names[5]))));
        return 1;
    }

    private static int floor(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(Math.floor(MathLib.getDoubleArg(callFrame, 1, names[10]))));
        return 1;
    }

    public static boolean isNegative(double vDouble) {
        return Double.doubleToLongBits(vDouble) < 0;
    }

    public static double round(double x) {
        if (x < 0.0d) {
            return -MathLib.round(-x);
        }
        x += 0.5d;
        double x2 = Math.floor(x);
        if (x2 == x) {
            return x2 - ((double) (((long) x2) & 1));
        }
        return x2;
    }

    public static double roundToPrecision(double x, int precision) {
        double roundingOffset = MathLib.ipow(10.0d, precision);
        return MathLib.round(x * roundingOffset) / roundingOffset;
    }

    public static double roundToSignificantNumbers(double x, int precision) {
        if (x == 0.0d) {
            return 0.0d;
        }
        if (x < 0.0d) {
            return -MathLib.roundToSignificantNumbers(-x, precision);
        }
        double lowerLimit = MathLib.ipow(10.0d, precision - 1);
        double upperLimit = lowerLimit * 10.0d;
        double multiplier = 1.0d;
        while (multiplier * x < lowerLimit) {
            multiplier *= 10.0d;
        }
        while (multiplier * x >= upperLimit) {
            multiplier /= 10.0d;
        }
        return MathLib.round(x * multiplier) / multiplier;
    }

    private static int modf(LuaCallFrame callFrame, int nArguments) {
        double fracPart;
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        double x = MathLib.getDoubleArg(callFrame, 1, names[16]);
        boolean negate = false;
        if (MathLib.isNegative(x)) {
            negate = true;
            x = -x;
        }
        double intPart = Math.floor(x);
        if (Double.isInfinite(intPart)) {
            fracPart = 0.0d;
        } else {
            fracPart = x - intPart;
        }
        if (negate) {
            intPart = -intPart;
            fracPart = -fracPart;
        }
        callFrame.push(LuaState.toDouble(intPart), LuaState.toDouble(fracPart));
        return 2;
    }

    private static int fmod(LuaCallFrame callFrame, int nArguments) {
        double res;
        BaseLib.luaAssert(nArguments >= 2, "Not enough arguments");
        double v1 = MathLib.getDoubleArg(callFrame, 1, names[11]);
        double v2 = MathLib.getDoubleArg(callFrame, 2, names[11]);
        if (Double.isInfinite(v1) || Double.isNaN(v1)) {
            res = Double.NaN;
        } else if (Double.isInfinite(v2)) {
            res = v1;
        } else {
            v2 = Math.abs(v2);
            boolean negate = false;
            if (MathLib.isNegative(v1)) {
                negate = true;
                v1 = -v1;
            }
            res = v1 - (Math.floor(v1 / v2) * v2);
            if (negate) {
                res = -res;
            }
        }
        callFrame.push(LuaState.toDouble(res));
        return 1;
    }

    private int random(LuaCallFrame callFrame, int nArguments) {
        Random random = callFrame.thread.state.random;
        if (nArguments == 0) {
            callFrame.push(LuaState.toDouble(random.nextDouble()));
        } else {
            int n;
            int m = (int) MathLib.getDoubleArg(callFrame, 1, names[19]);
            if (nArguments == 1) {
                n = m;
                m = 1;
            } else {
                n = (int) MathLib.getDoubleArg(callFrame, 2, names[19]);
            }
            callFrame.push(LuaState.toDouble((long) (random.nextInt((n - m) + 1) + m)));
        }
        return 1;
    }

    private int randomseed(LuaCallFrame callFrame, int nArguments) {
        boolean z = true;
        if (nArguments < 1) {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        Object o = callFrame.get(0);
        if (o != null) {
            callFrame.thread.state.random.setSeed((long) o.hashCode());
        }
        return 0;
    }

    private static int cosh(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        double exp_x = MathLib.exp(MathLib.getDoubleArg(callFrame, 1, names[7]));
        callFrame.push(LuaState.toDouble(((1.0d / exp_x) + exp_x) * 0.5d));
        return 1;
    }

    private static int sinh(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        double exp_x = MathLib.exp(MathLib.getDoubleArg(callFrame, 1, names[22]));
        callFrame.push(LuaState.toDouble((exp_x - (1.0d / exp_x)) * 0.5d));
        return 1;
    }

    private static int tanh(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        double exp_x = MathLib.exp(2.0d * MathLib.getDoubleArg(callFrame, 1, names[25]));
        callFrame.push(LuaState.toDouble((exp_x - 1.0d) / (1.0d + exp_x)));
        return 1;
    }

    private static int deg(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(Math.toDegrees(MathLib.getDoubleArg(callFrame, 1, names[8]))));
        return 1;
    }

    private static int rad(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(Math.toRadians(MathLib.getDoubleArg(callFrame, 1, names[18]))));
        return 1;
    }

    private static int acos(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(MathLib.acos(MathLib.getDoubleArg(callFrame, 1, names[1]))));
        return 1;
    }

    private static int asin(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(MathLib.asin(MathLib.getDoubleArg(callFrame, 1, names[2]))));
        return 1;
    }

    private static int atan(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(MathLib.atan(MathLib.getDoubleArg(callFrame, 1, names[3]))));
        return 1;
    }

    private static int atan2(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 2, "Not enough arguments");
        callFrame.push(LuaState.toDouble(MathLib.atan2(MathLib.getDoubleArg(callFrame, 1, names[4]), MathLib.getDoubleArg(callFrame, 2, names[4]))));
        return 1;
    }

    private static int cos(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(Math.cos(MathLib.getDoubleArg(callFrame, 1, names[6]))));
        return 1;
    }

    private static int sin(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(Math.sin(MathLib.getDoubleArg(callFrame, 1, names[21]))));
        return 1;
    }

    private static int tan(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(Math.tan(MathLib.getDoubleArg(callFrame, 1, names[24]))));
        return 1;
    }

    private static int sqrt(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(Math.sqrt(MathLib.getDoubleArg(callFrame, 1, names[23]))));
        return 1;
    }

    private static int exp(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(MathLib.exp(MathLib.getDoubleArg(callFrame, 1, names[9]))));
        return 1;
    }

    private static int pow(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 2, "Not enough arguments");
        callFrame.push(LuaState.toDouble(MathLib.pow(MathLib.getDoubleArg(callFrame, 1, names[17]), MathLib.getDoubleArg(callFrame, 2, names[17]))));
        return 1;
    }

    private static int log(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(MathLib.m34ln(MathLib.getDoubleArg(callFrame, 1, names[14]))));
        return 1;
    }

    private static int log10(LuaCallFrame callFrame, int nArguments) {
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        callFrame.push(LuaState.toDouble(MathLib.m34ln(MathLib.getDoubleArg(callFrame, 1, names[15])) * LN10_INV));
        return 1;
    }

    private static int frexp(LuaCallFrame callFrame, int nArguments) {
        double e;
        double m;
        BaseLib.luaAssert(nArguments >= 1, "Not enough arguments");
        double x = MathLib.getDoubleArg(callFrame, 1, names[12]);
        if (Double.isInfinite(x) || Double.isNaN(x)) {
            e = 0.0d;
            m = x;
        } else {
            e = Math.ceil(MathLib.m34ln(x) * LN2_INV);
            m = x / ((double) (1 << ((int) e)));
        }
        callFrame.push(LuaState.toDouble(m), LuaState.toDouble(e));
        return 2;
    }

    private static int ldexp(LuaCallFrame callFrame, int nArguments) {
        double ret;
        BaseLib.luaAssert(nArguments >= 2, "Not enough arguments");
        double m = MathLib.getDoubleArg(callFrame, 1, names[13]);
        double dE = MathLib.getDoubleArg(callFrame, 2, names[13]);
        double tmp = m + dE;
        if (Double.isInfinite(tmp) || Double.isNaN(tmp)) {
            ret = m;
        } else {
            ret = m * ((double) (1 << ((int) dE)));
        }
        callFrame.push(LuaState.toDouble(ret));
        return 1;
    }

    public static double exp(double x) {
        double x_acc = 1.0d;
        double div = 1.0d;
        double res = 0.0d;
        while (Math.abs(x_acc) > 1.0E-15d) {
            res += x_acc;
            x_acc = (x_acc * x) / div;
            div += 1.0d;
        }
        return res;
    }

    /* renamed from: ln */
    public static double m34ln(double x) {
        boolean negative = false;
        if (x < 0.0d) {
            return Double.NaN;
        }
        if (x == 0.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        if (Double.isInfinite(x)) {
            return Double.POSITIVE_INFINITY;
        }
        if (x < 1.0d) {
            negative = true;
            x = 1.0d / x;
        }
        int multiplier = 1;
        while (x >= 1.1d) {
            multiplier *= 2;
            x = Math.sqrt(x);
        }
        double t = 1.0d - x;
        double tpow = t;
        int divisor = 1;
        double result = 0.0d;
        while (true) {
            double toSubtract = tpow / ((double) divisor);
            if (Math.abs(toSubtract) <= 1.0E-15d) {
                break;
            }
            result -= toSubtract;
            tpow *= t;
            divisor++;
        }
        double res = ((double) multiplier) * result;
        if (negative) {
            return -res;
        }
        return res;
    }

    public static double pow(double base, double exponent) {
        if (((double) ((int) exponent)) == exponent) {
            return MathLib.ipow(base, (int) exponent);
        }
        return MathLib.fpow(base, exponent);
    }

    private static double fpow(double base, double exponent) {
        if (base < 0.0d) {
            return Double.NaN;
        }
        return MathLib.exp(MathLib.m34ln(base) * exponent);
    }

    public static double ipow(double base, int exponent) {
        boolean inverse = false;
        if (MathLib.isNegative((double) exponent)) {
            exponent = -exponent;
            inverse = true;
        }
        double b = (exponent & 1) != 0 ? base : 1.0d;
        for (exponent >>= 1; exponent != 0; exponent >>= 1) {
            base *= base;
            if ((exponent & 1) != 0) {
                b *= base;
            }
        }
        if (inverse) {
            return 1.0d / b;
        }
        return b;
    }

    private static double mxatan(double arg) {
        double argsq = arg * arg;
        return (((((((((f90p4 * argsq) + f89p3) * argsq) + f88p2) * argsq) + f87p1) * argsq) + 896.7859740366387d) / (((((((((f95q4 + argsq) * argsq) + f94q3) * argsq) + f93q2) * argsq) + f92q1) * argsq) + 896.7859740366387d)) * arg;
    }

    private static double msatan(double arg) {
        if (arg < sq2m1) {
            return MathLib.mxatan(arg);
        }
        if (arg > sq2p1) {
            return 1.5707963267948966d - MathLib.mxatan(1.0d / arg);
        }
        return 0.7853981633974483d + MathLib.mxatan((arg - 1.0d) / (1.0d + arg));
    }

    public static double atan(double arg) {
        if (arg > 0.0d) {
            return MathLib.msatan(arg);
        }
        return -MathLib.msatan(-arg);
    }

    public static double atan2(double arg1, double arg2) {
        if (arg1 + arg2 != arg1) {
            arg1 = MathLib.atan(arg1 / arg2);
            if (arg2 >= 0.0d) {
                return arg1;
            }
            if (arg1 <= 0.0d) {
                return arg1 + 3.141592653589793d;
            }
            return arg1 - 3.141592653589793d;
        } else if (arg1 > 0.0d) {
            return 1.5707963267948966d;
        } else {
            return arg1 < 0.0d ? -1.5707963267948966d : 0.0d;
        }
    }

    public static double asin(double arg) {
        int sign = 0;
        if (arg < 0.0d) {
            arg = -arg;
            sign = 0 + 1;
        }
        if (arg > 1.0d) {
            return Double.NaN;
        }
        double temp = Math.sqrt(1.0d - (arg * arg));
        if (arg > 0.7d) {
            temp = 1.5707963267948966d - MathLib.atan(temp / arg);
        } else {
            temp = MathLib.atan(arg / temp);
        }
        if (sign > 0) {
            return -temp;
        }
        return temp;
    }

    public static double acos(double arg) {
        if (arg > 1.0d || arg < -1.0d) {
            return Double.NaN;
        }
        return 1.5707963267948966d - MathLib.asin(arg);
    }
}

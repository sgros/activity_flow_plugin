package com.github.mikephil.charting.animation;

public class Easing {

    private static class EasingFunctions {
        public static final EasingFunction EaseInBack = new C040923();
        public static final EasingFunction EaseInBounce = new C041226();
        public static final EasingFunction EaseInCirc = new C040217();
        public static final EasingFunction EaseInCubic = new C04185();
        public static final EasingFunction EaseInElastic = new C040620();
        public static final EasingFunction EaseInExpo = new C039914();
        public static final EasingFunction EaseInOutBack = new C041125();
        public static final EasingFunction EaseInOutBounce = new C041428();
        public static final EasingFunction EaseInOutCirc = new C040419();
        public static final EasingFunction EaseInOutCubic = new C04207();
        public static final EasingFunction EaseInOutElastic = new C040822();
        public static final EasingFunction EaseInOutExpo = new C040116();
        public static final EasingFunction EaseInOutQuad = new C04174();
        public static final EasingFunction EaseInOutQuart = new C039510();
        public static final EasingFunction EaseInOutSine = new C039813();
        public static final EasingFunction EaseInQuad = new C04152();
        public static final EasingFunction EaseInQuart = new C04218();
        public static final EasingFunction EaseInSine = new C039611();
        public static final EasingFunction EaseOutBack = new C041024();
        public static final EasingFunction EaseOutBounce = new C041327();
        public static final EasingFunction EaseOutCirc = new C040318();
        public static final EasingFunction EaseOutCubic = new C04196();
        public static final EasingFunction EaseOutElastic = new C040721();
        public static final EasingFunction EaseOutExpo = new C040015();
        public static final EasingFunction EaseOutQuad = new C04163();
        public static final EasingFunction EaseOutQuart = new C04229();
        public static final EasingFunction EaseOutSine = new C039712();
        public static final EasingFunction Linear = new C04051();

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$10 */
        static class C039510 implements EasingFunction {
            public float getInterpolation(float f) {
                f /= 0.5f;
                if (f < 1.0f) {
                    return (((0.5f * f) * f) * f) * f;
                }
                f -= 2.0f;
                return -0.5f * ((((f * f) * f) * f) - 2.0f);
            }

            C039510() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$11 */
        static class C039611 implements EasingFunction {
            C039611() {
            }

            public float getInterpolation(float f) {
                return (-((float) Math.cos(((double) f) * 1.5707963267948966d))) + 1.0f;
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$12 */
        static class C039712 implements EasingFunction {
            C039712() {
            }

            public float getInterpolation(float f) {
                return (float) Math.sin(((double) f) * 1.5707963267948966d);
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$13 */
        static class C039813 implements EasingFunction {
            C039813() {
            }

            public float getInterpolation(float f) {
                return -0.5f * (((float) Math.cos(3.141592653589793d * ((double) f))) - 1.0f);
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$14 */
        static class C039914 implements EasingFunction {
            C039914() {
            }

            public float getInterpolation(float f) {
                return f == 0.0f ? 0.0f : (float) Math.pow(2.0d, (double) (10.0f * (f - 1.0f)));
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$15 */
        static class C040015 implements EasingFunction {
            C040015() {
            }

            public float getInterpolation(float f) {
                return f == 1.0f ? 1.0f : -((float) Math.pow(2.0d, (double) (-10.0f * (f + 1.0f))));
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$16 */
        static class C040116 implements EasingFunction {
            C040116() {
            }

            public float getInterpolation(float f) {
                if (f == 0.0f) {
                    return 0.0f;
                }
                if (f == 1.0f) {
                    return 1.0f;
                }
                f /= 0.5f;
                if (f < 1.0f) {
                    return 0.5f * ((float) Math.pow(2.0d, (double) (10.0f * (f - 1.0f))));
                }
                return 0.5f * ((-((float) Math.pow(2.0d, (double) (-10.0f * (f - 1.0f))))) + 2.0f);
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$17 */
        static class C040217 implements EasingFunction {
            C040217() {
            }

            public float getInterpolation(float f) {
                return -(((float) Math.sqrt((double) (1.0f - (f * f)))) - 1.0f);
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$18 */
        static class C040318 implements EasingFunction {
            C040318() {
            }

            public float getInterpolation(float f) {
                f -= 1.0f;
                return (float) Math.sqrt((double) (1.0f - (f * f)));
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$19 */
        static class C040419 implements EasingFunction {
            C040419() {
            }

            public float getInterpolation(float f) {
                f /= 0.5f;
                if (f < 1.0f) {
                    return -0.5f * (((float) Math.sqrt((double) (1.0f - (f * f)))) - 1.0f);
                }
                f -= 2.0f;
                return 0.5f * (((float) Math.sqrt((double) (1.0f - (f * f)))) + 1.0f);
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$1 */
        static class C04051 implements EasingFunction {
            public float getInterpolation(float f) {
                return f;
            }

            C04051() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$20 */
        static class C040620 implements EasingFunction {
            C040620() {
            }

            public float getInterpolation(float f) {
                if (f == 0.0f) {
                    return 0.0f;
                }
                if (f == 1.0f) {
                    return 1.0f;
                }
                f -= 1.0f;
                return -(((float) Math.pow(2.0d, (double) (10.0f * f))) * ((float) Math.sin((((double) (f - (0.047746483f * ((float) Math.asin(1.0d))))) * 6.283185307179586d) / ((double) 0.3f))));
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$21 */
        static class C040721 implements EasingFunction {
            C040721() {
            }

            public float getInterpolation(float f) {
                if (f == 0.0f) {
                    return 0.0f;
                }
                if (f == 1.0f) {
                    return 1.0f;
                }
                return (((float) Math.pow(2.0d, (double) (-10.0f * f))) * ((float) Math.sin((((double) (f - (0.047746483f * ((float) Math.asin(1.0d))))) * 6.283185307179586d) / ((double) 0.3f)))) + 1.0f;
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$22 */
        static class C040822 implements EasingFunction {
            C040822() {
            }

            public float getInterpolation(float f) {
                if (f == 0.0f) {
                    return 0.0f;
                }
                f /= 0.5f;
                if (f == 2.0f) {
                    return 1.0f;
                }
                float asin = 0.07161973f * ((float) Math.asin(1.0d));
                if (f < 1.0f) {
                    f -= 1.0f;
                    return -0.5f * (((float) Math.pow(2.0d, (double) (10.0f * f))) * ((float) Math.sin((((double) ((f * 1.0f) - asin)) * 6.283185307179586d) / ((double) 1055286887))));
                }
                f -= 1.0f;
                return ((((float) Math.pow(2.0d, (double) (-10.0f * f))) * ((float) Math.sin((((double) ((f * 1.0f) - asin)) * 6.283185307179586d) / ((double) 1055286887)))) * 0.5f) + 1.0f;
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$23 */
        static class C040923 implements EasingFunction {
            public float getInterpolation(float f) {
                return (f * f) * ((2.70158f * f) - 1.70158f);
            }

            C040923() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$24 */
        static class C041024 implements EasingFunction {
            public float getInterpolation(float f) {
                f -= 1.0f;
                return ((f * f) * ((2.70158f * f) + 1.70158f)) + 1.0f;
            }

            C041024() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$25 */
        static class C041125 implements EasingFunction {
            public float getInterpolation(float f) {
                f /= 0.5f;
                if (f < 1.0f) {
                    return 0.5f * ((f * f) * ((3.5949094f * f) - 2.5949094f));
                }
                f -= 2.0f;
                return 0.5f * (((f * f) * ((3.5949094f * f) + 2.5949094f)) + 2.0f);
            }

            C041125() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$26 */
        static class C041226 implements EasingFunction {
            C041226() {
            }

            public float getInterpolation(float f) {
                return 1.0f - EasingFunctions.EaseOutBounce.getInterpolation(1.0f - f);
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$27 */
        static class C041327 implements EasingFunction {
            public float getInterpolation(float f) {
                if (f < 0.36363637f) {
                    return (7.5625f * f) * f;
                }
                if (f < 0.72727275f) {
                    f -= 0.54545456f;
                    return ((7.5625f * f) * f) + 0.75f;
                } else if (f < 0.90909094f) {
                    f -= 0.8181818f;
                    return ((7.5625f * f) * f) + 0.9375f;
                } else {
                    f -= 0.95454544f;
                    return ((7.5625f * f) * f) + 0.984375f;
                }
            }

            C041327() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$28 */
        static class C041428 implements EasingFunction {
            C041428() {
            }

            public float getInterpolation(float f) {
                if (f < 0.5f) {
                    return EasingFunctions.EaseInBounce.getInterpolation(f * 2.0f) * 0.5f;
                }
                return (EasingFunctions.EaseOutBounce.getInterpolation((f * 2.0f) - 1.0f) * 0.5f) + 0.5f;
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$2 */
        static class C04152 implements EasingFunction {
            public float getInterpolation(float f) {
                return f * f;
            }

            C04152() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$3 */
        static class C04163 implements EasingFunction {
            public float getInterpolation(float f) {
                return (-f) * (f - 2.0f);
            }

            C04163() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$4 */
        static class C04174 implements EasingFunction {
            public float getInterpolation(float f) {
                f /= 0.5f;
                if (f < 1.0f) {
                    return (0.5f * f) * f;
                }
                f -= 1.0f;
                return -0.5f * ((f * (f - 2.0f)) - 1.0f);
            }

            C04174() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$5 */
        static class C04185 implements EasingFunction {
            public float getInterpolation(float f) {
                return (f * f) * f;
            }

            C04185() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$6 */
        static class C04196 implements EasingFunction {
            public float getInterpolation(float f) {
                f -= 1.0f;
                return ((f * f) * f) + 1.0f;
            }

            C04196() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$7 */
        static class C04207 implements EasingFunction {
            public float getInterpolation(float f) {
                f /= 0.5f;
                if (f < 1.0f) {
                    return ((0.5f * f) * f) * f;
                }
                f -= 2.0f;
                return 0.5f * (((f * f) * f) + 2.0f);
            }

            C04207() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$8 */
        static class C04218 implements EasingFunction {
            public float getInterpolation(float f) {
                return ((f * f) * f) * f;
            }

            C04218() {
            }
        }

        /* renamed from: com.github.mikephil.charting.animation.Easing$EasingFunctions$9 */
        static class C04229 implements EasingFunction {
            public float getInterpolation(float f) {
                f -= 1.0f;
                return -((((f * f) * f) * f) - 1.0f);
            }

            C04229() {
            }
        }

        private EasingFunctions() {
        }
    }

    public enum EasingOption {
        Linear,
        EaseInQuad,
        EaseOutQuad,
        EaseInOutQuad,
        EaseInCubic,
        EaseOutCubic,
        EaseInOutCubic,
        EaseInQuart,
        EaseOutQuart,
        EaseInOutQuart,
        EaseInSine,
        EaseOutSine,
        EaseInOutSine,
        EaseInExpo,
        EaseOutExpo,
        EaseInOutExpo,
        EaseInCirc,
        EaseOutCirc,
        EaseInOutCirc,
        EaseInElastic,
        EaseOutElastic,
        EaseInOutElastic,
        EaseInBack,
        EaseOutBack,
        EaseInOutBack,
        EaseInBounce,
        EaseOutBounce,
        EaseInOutBounce
    }

    public static EasingFunction getEasingFunctionFromOption(EasingOption easingOption) {
        switch (easingOption) {
            case EaseInQuad:
                return EasingFunctions.EaseInQuad;
            case EaseOutQuad:
                return EasingFunctions.EaseOutQuad;
            case EaseInOutQuad:
                return EasingFunctions.EaseInOutQuad;
            case EaseInCubic:
                return EasingFunctions.EaseInCubic;
            case EaseOutCubic:
                return EasingFunctions.EaseOutCubic;
            case EaseInOutCubic:
                return EasingFunctions.EaseInOutCubic;
            case EaseInQuart:
                return EasingFunctions.EaseInQuart;
            case EaseOutQuart:
                return EasingFunctions.EaseOutQuart;
            case EaseInOutQuart:
                return EasingFunctions.EaseInOutQuart;
            case EaseInSine:
                return EasingFunctions.EaseInSine;
            case EaseOutSine:
                return EasingFunctions.EaseOutSine;
            case EaseInOutSine:
                return EasingFunctions.EaseInOutSine;
            case EaseInExpo:
                return EasingFunctions.EaseInExpo;
            case EaseOutExpo:
                return EasingFunctions.EaseOutExpo;
            case EaseInOutExpo:
                return EasingFunctions.EaseInOutExpo;
            case EaseInCirc:
                return EasingFunctions.EaseInCirc;
            case EaseOutCirc:
                return EasingFunctions.EaseOutCirc;
            case EaseInOutCirc:
                return EasingFunctions.EaseInOutCirc;
            case EaseInElastic:
                return EasingFunctions.EaseInElastic;
            case EaseOutElastic:
                return EasingFunctions.EaseOutElastic;
            case EaseInOutElastic:
                return EasingFunctions.EaseInOutElastic;
            case EaseInBack:
                return EasingFunctions.EaseInBack;
            case EaseOutBack:
                return EasingFunctions.EaseOutBack;
            case EaseInOutBack:
                return EasingFunctions.EaseInOutBack;
            case EaseInBounce:
                return EasingFunctions.EaseInBounce;
            case EaseOutBounce:
                return EasingFunctions.EaseOutBounce;
            case EaseInOutBounce:
                return EasingFunctions.EaseInOutBounce;
            default:
                return EasingFunctions.Linear;
        }
    }
}

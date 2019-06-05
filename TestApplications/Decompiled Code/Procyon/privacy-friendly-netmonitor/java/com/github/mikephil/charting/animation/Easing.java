// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.animation;

public class Easing
{
    public static EasingFunction getEasingFunctionFromOption(final EasingOption easingOption) {
        switch (Easing$1.$SwitchMap$com$github$mikephil$charting$animation$Easing$EasingOption[easingOption.ordinal()]) {
            default: {
                return EasingFunctions.Linear;
            }
            case 28: {
                return EasingFunctions.EaseInOutBounce;
            }
            case 27: {
                return EasingFunctions.EaseOutBounce;
            }
            case 26: {
                return EasingFunctions.EaseInBounce;
            }
            case 25: {
                return EasingFunctions.EaseInOutBack;
            }
            case 24: {
                return EasingFunctions.EaseOutBack;
            }
            case 23: {
                return EasingFunctions.EaseInBack;
            }
            case 22: {
                return EasingFunctions.EaseInOutElastic;
            }
            case 21: {
                return EasingFunctions.EaseOutElastic;
            }
            case 20: {
                return EasingFunctions.EaseInElastic;
            }
            case 19: {
                return EasingFunctions.EaseInOutCirc;
            }
            case 18: {
                return EasingFunctions.EaseOutCirc;
            }
            case 17: {
                return EasingFunctions.EaseInCirc;
            }
            case 16: {
                return EasingFunctions.EaseInOutExpo;
            }
            case 15: {
                return EasingFunctions.EaseOutExpo;
            }
            case 14: {
                return EasingFunctions.EaseInExpo;
            }
            case 13: {
                return EasingFunctions.EaseInOutSine;
            }
            case 12: {
                return EasingFunctions.EaseOutSine;
            }
            case 11: {
                return EasingFunctions.EaseInSine;
            }
            case 10: {
                return EasingFunctions.EaseInOutQuart;
            }
            case 9: {
                return EasingFunctions.EaseOutQuart;
            }
            case 8: {
                return EasingFunctions.EaseInQuart;
            }
            case 7: {
                return EasingFunctions.EaseInOutCubic;
            }
            case 6: {
                return EasingFunctions.EaseOutCubic;
            }
            case 5: {
                return EasingFunctions.EaseInCubic;
            }
            case 4: {
                return EasingFunctions.EaseInOutQuad;
            }
            case 3: {
                return EasingFunctions.EaseOutQuad;
            }
            case 2: {
                return EasingFunctions.EaseInQuad;
            }
        }
    }
    
    private static class EasingFunctions
    {
        public static final EasingFunction EaseInBack;
        public static final EasingFunction EaseInBounce;
        public static final EasingFunction EaseInCirc;
        public static final EasingFunction EaseInCubic;
        public static final EasingFunction EaseInElastic;
        public static final EasingFunction EaseInExpo;
        public static final EasingFunction EaseInOutBack;
        public static final EasingFunction EaseInOutBounce;
        public static final EasingFunction EaseInOutCirc;
        public static final EasingFunction EaseInOutCubic;
        public static final EasingFunction EaseInOutElastic;
        public static final EasingFunction EaseInOutExpo;
        public static final EasingFunction EaseInOutQuad;
        public static final EasingFunction EaseInOutQuart;
        public static final EasingFunction EaseInOutSine;
        public static final EasingFunction EaseInQuad;
        public static final EasingFunction EaseInQuart;
        public static final EasingFunction EaseInSine;
        public static final EasingFunction EaseOutBack;
        public static final EasingFunction EaseOutBounce;
        public static final EasingFunction EaseOutCirc;
        public static final EasingFunction EaseOutCubic;
        public static final EasingFunction EaseOutElastic;
        public static final EasingFunction EaseOutExpo;
        public static final EasingFunction EaseOutQuad;
        public static final EasingFunction EaseOutQuart;
        public static final EasingFunction EaseOutSine;
        public static final EasingFunction Linear;
        
        static {
            Linear = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return n;
                }
            };
            EaseInQuad = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return n * n;
                }
            };
            EaseOutQuad = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return -n * (n - 2.0f);
                }
            };
            EaseInOutQuad = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    n /= 0.5f;
                    if (n < 1.0f) {
                        return 0.5f * n * n;
                    }
                    --n;
                    return -0.5f * (n * (n - 2.0f) - 1.0f);
                }
            };
            EaseInCubic = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return n * n * n;
                }
            };
            EaseOutCubic = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    --n;
                    return n * n * n + 1.0f;
                }
            };
            EaseInOutCubic = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    n /= 0.5f;
                    if (n < 1.0f) {
                        return 0.5f * n * n * n;
                    }
                    n -= 2.0f;
                    return 0.5f * (n * n * n + 2.0f);
                }
            };
            EaseInQuart = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return n * n * n * n;
                }
            };
            EaseOutQuart = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    --n;
                    return -(n * n * n * n - 1.0f);
                }
            };
            EaseInOutQuart = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    n /= 0.5f;
                    if (n < 1.0f) {
                        return 0.5f * n * n * n * n;
                    }
                    n -= 2.0f;
                    return -0.5f * (n * n * n * n - 2.0f);
                }
            };
            EaseInSine = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return -(float)Math.cos(n * 1.5707963267948966) + 1.0f;
                }
            };
            EaseOutSine = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return (float)Math.sin(n * 1.5707963267948966);
                }
            };
            EaseInOutSine = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return -0.5f * ((float)Math.cos(3.141592653589793 * n) - 1.0f);
                }
            };
            EaseInExpo = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    final float n2 = 0.0f;
                    if (n == 0.0f) {
                        n = n2;
                    }
                    else {
                        n = (float)Math.pow(2.0, 10.0f * (n - 1.0f));
                    }
                    return n;
                }
            };
            EaseOutExpo = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    final float n2 = 1.0f;
                    if (n == 1.0f) {
                        n = n2;
                    }
                    else {
                        n = -(float)Math.pow(2.0, -10.0f * (n + 1.0f));
                    }
                    return n;
                }
            };
            EaseInOutExpo = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    if (n == 0.0f) {
                        return 0.0f;
                    }
                    if (n == 1.0f) {
                        return 1.0f;
                    }
                    n /= 0.5f;
                    if (n < 1.0f) {
                        return 0.5f * (float)Math.pow(2.0, 10.0f * (n - 1.0f));
                    }
                    return 0.5f * (-(float)Math.pow(2.0, -10.0f * (n - 1.0f)) + 2.0f);
                }
            };
            EaseInCirc = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return -((float)Math.sqrt(1.0f - n * n) - 1.0f);
                }
            };
            EaseOutCirc = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    --n;
                    return (float)Math.sqrt(1.0f - n * n);
                }
            };
            EaseInOutCirc = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    n /= 0.5f;
                    if (n < 1.0f) {
                        return -0.5f * ((float)Math.sqrt(1.0f - n * n) - 1.0f);
                    }
                    n -= 2.0f;
                    return 0.5f * ((float)Math.sqrt(1.0f - n * n) + 1.0f);
                }
            };
            EaseInElastic = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    if (n == 0.0f) {
                        return 0.0f;
                    }
                    if (n == 1.0f) {
                        return 1.0f;
                    }
                    final float n2 = (float)Math.asin(1.0);
                    --n;
                    return -((float)Math.pow(2.0, 10.0f * n) * (float)Math.sin((n - 0.047746483f * n2) * 6.283185307179586 / 0.3f));
                }
            };
            EaseOutElastic = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    if (n == 0.0f) {
                        return 0.0f;
                    }
                    if (n == 1.0f) {
                        return 1.0f;
                    }
                    return (float)Math.pow(2.0, -10.0f * n) * (float)Math.sin((n - 0.047746483f * (float)Math.asin(1.0)) * 6.283185307179586 / 0.3f) + 1.0f;
                }
            };
            EaseInOutElastic = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    if (n == 0.0f) {
                        return 0.0f;
                    }
                    final float n2 = n / 0.5f;
                    if (n2 == 2.0f) {
                        return 1.0f;
                    }
                    n = 0.07161973f * (float)Math.asin(1.0);
                    if (n2 < 1.0f) {
                        final float n3 = n2 - 1.0f;
                        return -0.5f * ((float)Math.pow(2.0, 10.0f * n3) * (float)Math.sin((n3 * 1.0f - n) * 6.283185307179586 / 0.45000002f));
                    }
                    final float n4 = n2 - 1.0f;
                    return (float)Math.pow(2.0, -10.0f * n4) * (float)Math.sin((n4 * 1.0f - n) * 6.283185307179586 / 0.45000002f) * 0.5f + 1.0f;
                }
            };
            EaseInBack = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return n * n * (2.70158f * n - 1.70158f);
                }
            };
            EaseOutBack = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    --n;
                    return n * n * (2.70158f * n + 1.70158f) + 1.0f;
                }
            };
            EaseInOutBack = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    n /= 0.5f;
                    if (n < 1.0f) {
                        return 0.5f * (n * n * (3.5949094f * n - 2.5949094f));
                    }
                    n -= 2.0f;
                    return 0.5f * (n * n * (3.5949094f * n + 2.5949094f) + 2.0f);
                }
            };
            EaseInBounce = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    return 1.0f - EasingFunctions.EaseOutBounce.getInterpolation(1.0f - n);
                }
            };
            EaseOutBounce = new EasingFunction() {
                @Override
                public float getInterpolation(float n) {
                    if (n < 0.36363637f) {
                        return 7.5625f * n * n;
                    }
                    if (n < 0.72727275f) {
                        n -= 0.54545456f;
                        return 7.5625f * n * n + 0.75f;
                    }
                    if (n < 0.90909094f) {
                        n -= 0.8181818f;
                        return 7.5625f * n * n + 0.9375f;
                    }
                    n -= 0.95454544f;
                    return 7.5625f * n * n + 0.984375f;
                }
            };
            EaseInOutBounce = new EasingFunction() {
                @Override
                public float getInterpolation(final float n) {
                    if (n < 0.5f) {
                        return EasingFunctions.EaseInBounce.getInterpolation(n * 2.0f) * 0.5f;
                    }
                    return EasingFunctions.EaseOutBounce.getInterpolation(n * 2.0f - 1.0f) * 0.5f + 0.5f;
                }
            };
        }
    }
    
    public enum EasingOption
    {
        EaseInBack, 
        EaseInBounce, 
        EaseInCirc, 
        EaseInCubic, 
        EaseInElastic, 
        EaseInExpo, 
        EaseInOutBack, 
        EaseInOutBounce, 
        EaseInOutCirc, 
        EaseInOutCubic, 
        EaseInOutElastic, 
        EaseInOutExpo, 
        EaseInOutQuad, 
        EaseInOutQuart, 
        EaseInOutSine, 
        EaseInQuad, 
        EaseInQuart, 
        EaseInSine, 
        EaseOutBack, 
        EaseOutBounce, 
        EaseOutCirc, 
        EaseOutCubic, 
        EaseOutElastic, 
        EaseOutExpo, 
        EaseOutQuad, 
        EaseOutQuart, 
        EaseOutSine, 
        Linear;
    }
}

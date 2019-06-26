package com.github.mikephil.charting.animation;

public class Easing {
   public static EasingFunction getEasingFunctionFromOption(Easing.EasingOption var0) {
      switch(var0) {
      case EaseInQuad:
         return Easing.EasingFunctions.EaseInQuad;
      case EaseOutQuad:
         return Easing.EasingFunctions.EaseOutQuad;
      case EaseInOutQuad:
         return Easing.EasingFunctions.EaseInOutQuad;
      case EaseInCubic:
         return Easing.EasingFunctions.EaseInCubic;
      case EaseOutCubic:
         return Easing.EasingFunctions.EaseOutCubic;
      case EaseInOutCubic:
         return Easing.EasingFunctions.EaseInOutCubic;
      case EaseInQuart:
         return Easing.EasingFunctions.EaseInQuart;
      case EaseOutQuart:
         return Easing.EasingFunctions.EaseOutQuart;
      case EaseInOutQuart:
         return Easing.EasingFunctions.EaseInOutQuart;
      case EaseInSine:
         return Easing.EasingFunctions.EaseInSine;
      case EaseOutSine:
         return Easing.EasingFunctions.EaseOutSine;
      case EaseInOutSine:
         return Easing.EasingFunctions.EaseInOutSine;
      case EaseInExpo:
         return Easing.EasingFunctions.EaseInExpo;
      case EaseOutExpo:
         return Easing.EasingFunctions.EaseOutExpo;
      case EaseInOutExpo:
         return Easing.EasingFunctions.EaseInOutExpo;
      case EaseInCirc:
         return Easing.EasingFunctions.EaseInCirc;
      case EaseOutCirc:
         return Easing.EasingFunctions.EaseOutCirc;
      case EaseInOutCirc:
         return Easing.EasingFunctions.EaseInOutCirc;
      case EaseInElastic:
         return Easing.EasingFunctions.EaseInElastic;
      case EaseOutElastic:
         return Easing.EasingFunctions.EaseOutElastic;
      case EaseInOutElastic:
         return Easing.EasingFunctions.EaseInOutElastic;
      case EaseInBack:
         return Easing.EasingFunctions.EaseInBack;
      case EaseOutBack:
         return Easing.EasingFunctions.EaseOutBack;
      case EaseInOutBack:
         return Easing.EasingFunctions.EaseInOutBack;
      case EaseInBounce:
         return Easing.EasingFunctions.EaseInBounce;
      case EaseOutBounce:
         return Easing.EasingFunctions.EaseOutBounce;
      case EaseInOutBounce:
         return Easing.EasingFunctions.EaseInOutBounce;
      default:
         return Easing.EasingFunctions.Linear;
      }
   }

   private static class EasingFunctions {
      public static final EasingFunction EaseInBack = new EasingFunction() {
         public float getInterpolation(float var1) {
            return var1 * var1 * (2.70158F * var1 - 1.70158F);
         }
      };
      public static final EasingFunction EaseInBounce = new EasingFunction() {
         public float getInterpolation(float var1) {
            return 1.0F - Easing.EasingFunctions.EaseOutBounce.getInterpolation(1.0F - var1);
         }
      };
      public static final EasingFunction EaseInCirc = new EasingFunction() {
         public float getInterpolation(float var1) {
            return -((float)Math.sqrt((double)(1.0F - var1 * var1)) - 1.0F);
         }
      };
      public static final EasingFunction EaseInCubic = new EasingFunction() {
         public float getInterpolation(float var1) {
            return var1 * var1 * var1;
         }
      };
      public static final EasingFunction EaseInElastic = new EasingFunction() {
         public float getInterpolation(float var1) {
            if (var1 == 0.0F) {
               return 0.0F;
            } else if (var1 == 1.0F) {
               return 1.0F;
            } else {
               float var2 = (float)Math.asin(1.0D);
               --var1;
               return -((float)Math.pow(2.0D, (double)(10.0F * var1)) * (float)Math.sin((double)(var1 - 0.047746483F * var2) * 6.283185307179586D / (double)0.3F));
            }
         }
      };
      public static final EasingFunction EaseInExpo = new EasingFunction() {
         public float getInterpolation(float var1) {
            float var2 = 0.0F;
            if (var1 == 0.0F) {
               var1 = var2;
            } else {
               var1 = (float)Math.pow(2.0D, (double)(10.0F * (var1 - 1.0F)));
            }

            return var1;
         }
      };
      public static final EasingFunction EaseInOutBack = new EasingFunction() {
         public float getInterpolation(float var1) {
            var1 /= 0.5F;
            if (var1 < 1.0F) {
               return 0.5F * var1 * var1 * (3.5949094F * var1 - 2.5949094F);
            } else {
               var1 -= 2.0F;
               return 0.5F * (var1 * var1 * (3.5949094F * var1 + 2.5949094F) + 2.0F);
            }
         }
      };
      public static final EasingFunction EaseInOutBounce = new EasingFunction() {
         public float getInterpolation(float var1) {
            return var1 < 0.5F ? Easing.EasingFunctions.EaseInBounce.getInterpolation(var1 * 2.0F) * 0.5F : Easing.EasingFunctions.EaseOutBounce.getInterpolation(var1 * 2.0F - 1.0F) * 0.5F + 0.5F;
         }
      };
      public static final EasingFunction EaseInOutCirc = new EasingFunction() {
         public float getInterpolation(float var1) {
            var1 /= 0.5F;
            if (var1 < 1.0F) {
               return -0.5F * ((float)Math.sqrt((double)(1.0F - var1 * var1)) - 1.0F);
            } else {
               var1 -= 2.0F;
               return 0.5F * ((float)Math.sqrt((double)(1.0F - var1 * var1)) + 1.0F);
            }
         }
      };
      public static final EasingFunction EaseInOutCubic = new EasingFunction() {
         public float getInterpolation(float var1) {
            var1 /= 0.5F;
            if (var1 < 1.0F) {
               return 0.5F * var1 * var1 * var1;
            } else {
               var1 -= 2.0F;
               return 0.5F * (var1 * var1 * var1 + 2.0F);
            }
         }
      };
      public static final EasingFunction EaseInOutElastic = new EasingFunction() {
         public float getInterpolation(float var1) {
            if (var1 == 0.0F) {
               return 0.0F;
            } else {
               float var2 = var1 / 0.5F;
               if (var2 == 2.0F) {
                  return 1.0F;
               } else {
                  var1 = 0.07161973F * (float)Math.asin(1.0D);
                  if (var2 < 1.0F) {
                     --var2;
                     return -0.5F * (float)Math.pow(2.0D, (double)(10.0F * var2)) * (float)Math.sin((double)(var2 * 1.0F - var1) * 6.283185307179586D / (double)0.45000002F);
                  } else {
                     --var2;
                     return (float)Math.pow(2.0D, (double)(-10.0F * var2)) * (float)Math.sin((double)(var2 * 1.0F - var1) * 6.283185307179586D / (double)0.45000002F) * 0.5F + 1.0F;
                  }
               }
            }
         }
      };
      public static final EasingFunction EaseInOutExpo = new EasingFunction() {
         public float getInterpolation(float var1) {
            if (var1 == 0.0F) {
               return 0.0F;
            } else if (var1 == 1.0F) {
               return 1.0F;
            } else {
               var1 /= 0.5F;
               return var1 < 1.0F ? 0.5F * (float)Math.pow(2.0D, (double)(10.0F * (var1 - 1.0F))) : 0.5F * (-((float)Math.pow(2.0D, (double)(-10.0F * (var1 - 1.0F)))) + 2.0F);
            }
         }
      };
      public static final EasingFunction EaseInOutQuad = new EasingFunction() {
         public float getInterpolation(float var1) {
            var1 /= 0.5F;
            if (var1 < 1.0F) {
               return 0.5F * var1 * var1;
            } else {
               --var1;
               return -0.5F * (var1 * (var1 - 2.0F) - 1.0F);
            }
         }
      };
      public static final EasingFunction EaseInOutQuart = new EasingFunction() {
         public float getInterpolation(float var1) {
            var1 /= 0.5F;
            if (var1 < 1.0F) {
               return 0.5F * var1 * var1 * var1 * var1;
            } else {
               var1 -= 2.0F;
               return -0.5F * (var1 * var1 * var1 * var1 - 2.0F);
            }
         }
      };
      public static final EasingFunction EaseInOutSine = new EasingFunction() {
         public float getInterpolation(float var1) {
            return -0.5F * ((float)Math.cos(3.141592653589793D * (double)var1) - 1.0F);
         }
      };
      public static final EasingFunction EaseInQuad = new EasingFunction() {
         public float getInterpolation(float var1) {
            return var1 * var1;
         }
      };
      public static final EasingFunction EaseInQuart = new EasingFunction() {
         public float getInterpolation(float var1) {
            return var1 * var1 * var1 * var1;
         }
      };
      public static final EasingFunction EaseInSine = new EasingFunction() {
         public float getInterpolation(float var1) {
            return -((float)Math.cos((double)var1 * 1.5707963267948966D)) + 1.0F;
         }
      };
      public static final EasingFunction EaseOutBack = new EasingFunction() {
         public float getInterpolation(float var1) {
            --var1;
            return var1 * var1 * (2.70158F * var1 + 1.70158F) + 1.0F;
         }
      };
      public static final EasingFunction EaseOutBounce = new EasingFunction() {
         public float getInterpolation(float var1) {
            if (var1 < 0.36363637F) {
               return 7.5625F * var1 * var1;
            } else if (var1 < 0.72727275F) {
               var1 -= 0.54545456F;
               return 7.5625F * var1 * var1 + 0.75F;
            } else if (var1 < 0.90909094F) {
               var1 -= 0.8181818F;
               return 7.5625F * var1 * var1 + 0.9375F;
            } else {
               var1 -= 0.95454544F;
               return 7.5625F * var1 * var1 + 0.984375F;
            }
         }
      };
      public static final EasingFunction EaseOutCirc = new EasingFunction() {
         public float getInterpolation(float var1) {
            --var1;
            return (float)Math.sqrt((double)(1.0F - var1 * var1));
         }
      };
      public static final EasingFunction EaseOutCubic = new EasingFunction() {
         public float getInterpolation(float var1) {
            --var1;
            return var1 * var1 * var1 + 1.0F;
         }
      };
      public static final EasingFunction EaseOutElastic = new EasingFunction() {
         public float getInterpolation(float var1) {
            if (var1 == 0.0F) {
               return 0.0F;
            } else if (var1 == 1.0F) {
               return 1.0F;
            } else {
               float var2 = (float)Math.asin(1.0D);
               return (float)Math.pow(2.0D, (double)(-10.0F * var1)) * (float)Math.sin((double)(var1 - 0.047746483F * var2) * 6.283185307179586D / (double)0.3F) + 1.0F;
            }
         }
      };
      public static final EasingFunction EaseOutExpo = new EasingFunction() {
         public float getInterpolation(float var1) {
            float var2 = 1.0F;
            if (var1 == 1.0F) {
               var1 = var2;
            } else {
               var1 = -((float)Math.pow(2.0D, (double)(-10.0F * (var1 + 1.0F))));
            }

            return var1;
         }
      };
      public static final EasingFunction EaseOutQuad = new EasingFunction() {
         public float getInterpolation(float var1) {
            return -var1 * (var1 - 2.0F);
         }
      };
      public static final EasingFunction EaseOutQuart = new EasingFunction() {
         public float getInterpolation(float var1) {
            --var1;
            return -(var1 * var1 * var1 * var1 - 1.0F);
         }
      };
      public static final EasingFunction EaseOutSine = new EasingFunction() {
         public float getInterpolation(float var1) {
            return (float)Math.sin((double)var1 * 1.5707963267948966D);
         }
      };
      public static final EasingFunction Linear = new EasingFunction() {
         public float getInterpolation(float var1) {
            return var1;
         }
      };
   }

   public static enum EasingOption {
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

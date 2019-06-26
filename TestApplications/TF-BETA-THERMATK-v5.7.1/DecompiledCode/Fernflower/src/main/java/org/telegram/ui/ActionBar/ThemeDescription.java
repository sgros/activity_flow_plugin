package org.telegram.ui.ActionBar;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.text.SpannedString;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatBigEmptyView;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.GroupCreateCheckBox;
import org.telegram.ui.Components.GroupCreateSpan;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.LineProgressView;
import org.telegram.ui.Components.MessageBackgroundDrawable;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RadioButton;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScamDrawable;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Components.TypefaceSpan;

public class ThemeDescription {
   public static int FLAG_AB_AM_BACKGROUND;
   public static int FLAG_AB_AM_ITEMSCOLOR;
   public static int FLAG_AB_AM_SELECTORCOLOR;
   public static int FLAG_AB_AM_TOPBACKGROUND;
   public static int FLAG_AB_ITEMSCOLOR;
   public static int FLAG_AB_SEARCH;
   public static int FLAG_AB_SEARCHPLACEHOLDER;
   public static int FLAG_AB_SELECTORCOLOR;
   public static int FLAG_AB_SUBMENUBACKGROUND;
   public static int FLAG_AB_SUBMENUITEM;
   public static int FLAG_AB_SUBTITLECOLOR;
   public static int FLAG_AB_TITLECOLOR;
   public static int FLAG_BACKGROUND;
   public static int FLAG_BACKGROUNDFILTER;
   public static int FLAG_CELLBACKGROUNDCOLOR;
   public static int FLAG_CHECKBOX;
   public static int FLAG_CHECKBOXCHECK;
   public static int FLAG_CHECKTAG;
   public static int FLAG_CURSORCOLOR;
   public static int FLAG_DRAWABLESELECTEDSTATE;
   public static int FLAG_FASTSCROLL;
   public static int FLAG_HINTTEXTCOLOR;
   public static int FLAG_IMAGECOLOR;
   public static int FLAG_LINKCOLOR;
   public static int FLAG_LISTGLOWCOLOR;
   public static int FLAG_PROGRESSBAR;
   public static int FLAG_SECTIONS;
   public static int FLAG_SELECTOR;
   public static int FLAG_SELECTORWHITE;
   public static int FLAG_SERVICEBACKGROUND;
   public static int FLAG_TEXTCOLOR;
   public static int FLAG_USEBACKGROUNDDRAWABLE;
   private HashMap cachedFields;
   private int changeFlags;
   private int currentColor;
   private String currentKey;
   private int defaultColor;
   private ThemeDescription.ThemeDescriptionDelegate delegate;
   private Drawable[] drawablesToUpdate;
   private Class[] listClasses;
   private String[] listClassesFieldName;
   private String lottieLayerName;
   private HashMap notFoundCachedFields;
   private Paint[] paintToUpdate;
   private int previousColor;
   private boolean[] previousIsDefault = new boolean[1];
   private View viewToInvalidate;

   public ThemeDescription(View var1, int var2, Class[] var3, Paint var4, Drawable[] var5, ThemeDescription.ThemeDescriptionDelegate var6, String var7) {
      this.currentKey = var7;
      if (var4 != null) {
         this.paintToUpdate = new Paint[]{var4};
      }

      this.drawablesToUpdate = var5;
      this.viewToInvalidate = var1;
      this.changeFlags = var2;
      this.listClasses = var3;
      this.delegate = var6;
      var1 = this.viewToInvalidate;
      if (var1 instanceof EditTextEmoji) {
         this.viewToInvalidate = ((EditTextEmoji)var1).getEditText();
      }

   }

   public ThemeDescription(View var1, int var2, Class[] var3, Paint[] var4, Drawable[] var5, ThemeDescription.ThemeDescriptionDelegate var6, String var7, Object var8) {
      this.currentKey = var7;
      this.paintToUpdate = var4;
      this.drawablesToUpdate = var5;
      this.viewToInvalidate = var1;
      this.changeFlags = var2;
      this.listClasses = var3;
      this.delegate = var6;
      var1 = this.viewToInvalidate;
      if (var1 instanceof EditTextEmoji) {
         this.viewToInvalidate = ((EditTextEmoji)var1).getEditText();
      }

   }

   public ThemeDescription(View var1, int var2, Class[] var3, LottieDrawable[] var4, String var5, String var6) {
      this.currentKey = var6;
      this.lottieLayerName = var5;
      this.drawablesToUpdate = var4;
      this.viewToInvalidate = var1;
      this.changeFlags = var2;
      this.listClasses = var3;
      var1 = this.viewToInvalidate;
      if (var1 instanceof EditTextEmoji) {
         this.viewToInvalidate = ((EditTextEmoji)var1).getEditText();
      }

   }

   public ThemeDescription(View var1, int var2, Class[] var3, String[] var4, String var5, String var6) {
      this.currentKey = var6;
      this.lottieLayerName = var5;
      this.viewToInvalidate = var1;
      this.changeFlags = var2;
      this.listClasses = var3;
      this.listClassesFieldName = var4;
      this.cachedFields = new HashMap();
      this.notFoundCachedFields = new HashMap();
      var1 = this.viewToInvalidate;
      if (var1 instanceof EditTextEmoji) {
         this.viewToInvalidate = ((EditTextEmoji)var1).getEditText();
      }

   }

   public ThemeDescription(View var1, int var2, Class[] var3, String[] var4, Paint[] var5, Drawable[] var6, ThemeDescription.ThemeDescriptionDelegate var7, String var8) {
      this.currentKey = var8;
      this.paintToUpdate = var5;
      this.drawablesToUpdate = var6;
      this.viewToInvalidate = var1;
      this.changeFlags = var2;
      this.listClasses = var3;
      this.listClassesFieldName = var4;
      this.delegate = var7;
      this.cachedFields = new HashMap();
      this.notFoundCachedFields = new HashMap();
      var1 = this.viewToInvalidate;
      if (var1 instanceof EditTextEmoji) {
         this.viewToInvalidate = ((EditTextEmoji)var1).getEditText();
      }

   }

   private boolean checkTag(String var1, View var2) {
      if (var1 != null && var2 != null) {
         Object var3 = var2.getTag();
         if (var3 instanceof String) {
            return ((String)var3).contains(var1);
         }
      }

      return false;
   }

   private void processViewColor(View var1, int var2) {
      int var3 = 0;

      while(true) {
         Class[] var4 = this.listClasses;
         if (var3 >= var4.length) {
            return;
         }

         if (var4[var3].isInstance(var1)) {
            var1.invalidate();
            int var5;
            Drawable var6;
            boolean var7;
            Drawable var63;
            boolean var64;
            if ((this.changeFlags & FLAG_CHECKTAG) != 0 && !this.checkTag(this.currentKey, var1)) {
               var64 = false;
            } else {
               var1.invalidate();
               var5 = this.changeFlags;
               if ((FLAG_BACKGROUNDFILTER & var5) != 0) {
                  var6 = var1.getBackground();
                  if (var6 != null) {
                     if ((this.changeFlags & FLAG_CELLBACKGROUNDCOLOR) != 0) {
                        if (var6 instanceof CombinedDrawable) {
                           var63 = ((CombinedDrawable)var6).getBackground();
                           if (var63 instanceof ColorDrawable) {
                              ((ColorDrawable)var63).setColor(var2);
                           }
                        }
                     } else {
                        if (var6 instanceof CombinedDrawable) {
                           var63 = ((CombinedDrawable)var6).getIcon();
                        } else {
                           label635: {
                              if (!(var6 instanceof StateListDrawable)) {
                                 var63 = var6;
                                 if (VERSION.SDK_INT < 21) {
                                    break label635;
                                 }

                                 var63 = var6;
                                 if (!(var6 instanceof RippleDrawable)) {
                                    break label635;
                                 }
                              }

                              if ((this.changeFlags & FLAG_DRAWABLESELECTEDSTATE) != 0) {
                                 var7 = true;
                              } else {
                                 var7 = false;
                              }

                              Theme.setSelectorDrawableColor(var6, var2, var7);
                              var63 = var6;
                           }
                        }

                        var63.setColorFilter(new PorterDuffColorFilter(var2, Mode.MULTIPLY));
                     }
                  }
               } else if ((FLAG_CELLBACKGROUNDCOLOR & var5) != 0) {
                  var1.setBackgroundColor(var2);
               } else if ((FLAG_TEXTCOLOR & var5) != 0) {
                  if (var1 instanceof TextView) {
                     ((TextView)var1).setTextColor(var2);
                  }
               } else if ((FLAG_SERVICEBACKGROUND & var5) != 0) {
                  var63 = var1.getBackground();
                  if (var63 != null) {
                     var63.setColorFilter(Theme.colorFilter);
                  }
               } else if ((FLAG_SELECTOR & var5) != 0) {
                  var1.setBackgroundDrawable(Theme.getSelectorDrawable(false));
               } else if ((var5 & FLAG_SELECTORWHITE) != 0) {
                  var1.setBackgroundDrawable(Theme.getSelectorDrawable(true));
               }

               var64 = true;
            }

            if (this.listClassesFieldName == null) {
               if (var1 instanceof GroupCreateSpan) {
                  ((GroupCreateSpan)var1).updateColors();
               }
            } else {
               StringBuilder var66 = new StringBuilder();
               var66.append(this.listClasses[var3]);
               var66.append("_");
               var66.append(this.listClassesFieldName[var3]);
               String var8 = var66.toString();
               HashMap var67 = this.notFoundCachedFields;
               if (var67 == null || !var67.containsKey(var8)) {
                  label676: {
                     Throwable var10000;
                     label636: {
                        boolean var10001;
                        Field var65;
                        try {
                           var65 = (Field)this.cachedFields.get(var8);
                        } catch (Throwable var45) {
                           var10000 = var45;
                           var10001 = false;
                           break label636;
                        }

                        Field var69 = var65;
                        if (var65 == null) {
                           try {
                              var65 = this.listClasses[var3].getDeclaredField(this.listClassesFieldName[var3]);
                           } catch (Throwable var44) {
                              var10000 = var44;
                              var10001 = false;
                              break label636;
                           }

                           var69 = var65;
                           if (var65 != null) {
                              try {
                                 var65.setAccessible(true);
                                 this.cachedFields.put(var8, var65);
                              } catch (Throwable var43) {
                                 var10000 = var43;
                                 var10001 = false;
                                 break label636;
                              }

                              var69 = var65;
                           }
                        }

                        if (var69 == null) {
                           break label676;
                        }

                        Object var9;
                        try {
                           var9 = var69.get(var1);
                        } catch (Throwable var42) {
                           var10000 = var42;
                           var10001 = false;
                           break label636;
                        }

                        if (var9 == null) {
                           break label676;
                        }

                        if (!var64) {
                           try {
                              if (var9 instanceof View && !this.checkTag(this.currentKey, (View)var9)) {
                                 break label676;
                              }
                           } catch (Throwable var41) {
                              var10000 = var41;
                              var10001 = false;
                              break label636;
                           }
                        }

                        try {
                           if (var9 instanceof View) {
                              ((View)var9).invalidate();
                           }
                        } catch (Throwable var40) {
                           var10000 = var40;
                           var10001 = false;
                           break label636;
                        }

                        try {
                           if (this.lottieLayerName != null && var9 instanceof LottieAnimationView) {
                              LottieAnimationView var10 = (LottieAnimationView)var9;
                              KeyPath var11 = new KeyPath(new String[]{this.lottieLayerName, "**"});
                              ColorFilter var68 = LottieProperty.COLOR_FILTER;
                              SimpleColorFilter var13 = new SimpleColorFilter(var2);
                              LottieValueCallback var12 = new LottieValueCallback(var13);
                              var10.addValueCallback(var11, var68, var12);
                           }
                        } catch (Throwable var39) {
                           var10000 = var39;
                           var10001 = false;
                           break label636;
                        }

                        Object var70 = var9;

                        label637: {
                           try {
                              if ((this.changeFlags & FLAG_USEBACKGROUNDDRAWABLE) == 0) {
                                 break label637;
                              }
                           } catch (Throwable var62) {
                              var10000 = var62;
                              var10001 = false;
                              break label636;
                           }

                           var70 = var9;

                           try {
                              if (var9 instanceof View) {
                                 var70 = ((View)var9).getBackground();
                              }
                           } catch (Throwable var38) {
                              var10000 = var38;
                              var10001 = false;
                              break label636;
                           }
                        }

                        label589: {
                           View var71;
                           try {
                              if ((this.changeFlags & FLAG_BACKGROUND) == 0 || !(var70 instanceof View)) {
                                 break label589;
                              }

                              var71 = (View)var70;
                              var63 = var71.getBackground();
                              if (var63 instanceof MessageBackgroundDrawable) {
                                 ((MessageBackgroundDrawable)var63).setColor(var2);
                                 break label676;
                              }
                           } catch (Throwable var61) {
                              var10000 = var61;
                              var10001 = false;
                              break label636;
                           }

                           try {
                              var71.setBackgroundColor(var2);
                              break label676;
                           } catch (Throwable var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label636;
                           }
                        }

                        label639: {
                           try {
                              if (var70 instanceof EditTextCaption) {
                                 if ((this.changeFlags & FLAG_HINTTEXTCOLOR) == 0) {
                                    break label639;
                                 }

                                 ((EditTextCaption)var70).setHintColor(var2);
                                 ((EditTextCaption)var70).setHintTextColor(var2);
                                 break label676;
                              }
                           } catch (Throwable var60) {
                              var10000 = var60;
                              var10001 = false;
                              break label636;
                           }

                           label572: {
                              try {
                                 if (!(var70 instanceof SimpleTextView)) {
                                    break label572;
                                 }

                                 if ((this.changeFlags & FLAG_LINKCOLOR) != 0) {
                                    ((SimpleTextView)var70).setLinkTextColor(var2);
                                    break label676;
                                 }
                              } catch (Throwable var59) {
                                 var10000 = var59;
                                 var10001 = false;
                                 break label636;
                              }

                              try {
                                 ((SimpleTextView)var70).setTextColor(var2);
                                 break label676;
                              } catch (Throwable var16) {
                                 var10000 = var16;
                                 var10001 = false;
                                 break label636;
                              }
                           }

                           PorterDuffColorFilter var78;
                           label642: {
                              Drawable[] var73;
                              label643: {
                                 TextView var72;
                                 try {
                                    if (!(var70 instanceof TextView)) {
                                       break label642;
                                    }

                                    var72 = (TextView)var70;
                                    if ((this.changeFlags & FLAG_IMAGECOLOR) != 0) {
                                       var73 = var72.getCompoundDrawables();
                                       break label643;
                                    }
                                 } catch (Throwable var58) {
                                    var10000 = var58;
                                    var10001 = false;
                                    break label636;
                                 }

                                 try {
                                    if ((this.changeFlags & FLAG_LINKCOLOR) != 0) {
                                       var72.getPaint().linkColor = var2;
                                       var72.invalidate();
                                       break label676;
                                    }
                                 } catch (Throwable var23) {
                                    var10000 = var23;
                                    var10001 = false;
                                    break label636;
                                 }

                                 TypefaceSpan[] var76;
                                 label378: {
                                    try {
                                       if ((this.changeFlags & FLAG_FASTSCROLL) != 0) {
                                          CharSequence var74 = var72.getText();
                                          if (!(var74 instanceof SpannedString)) {
                                             break label676;
                                          }

                                          var76 = (TypefaceSpan[])((SpannedString)var74).getSpans(0, var74.length(), TypefaceSpan.class);
                                          break label378;
                                       }
                                    } catch (Throwable var22) {
                                       var10000 = var22;
                                       var10001 = false;
                                       break label636;
                                    }

                                    try {
                                       var72.setTextColor(var2);
                                       break label676;
                                    } catch (Throwable var21) {
                                       var10000 = var21;
                                       var10001 = false;
                                       break label636;
                                    }
                                 }

                                 if (var76 == null) {
                                    break label676;
                                 }

                                 try {
                                    if (var76.length <= 0) {
                                       break label676;
                                    }
                                 } catch (Throwable var20) {
                                    var10000 = var20;
                                    var10001 = false;
                                    break label636;
                                 }

                                 var5 = 0;

                                 while(true) {
                                    try {
                                       if (var5 >= var76.length) {
                                          break label676;
                                       }

                                       var76[var5].setColor(var2);
                                    } catch (Throwable var19) {
                                       var10000 = var19;
                                       var10001 = false;
                                       break label636;
                                    }

                                    ++var5;
                                 }
                              }

                              if (var73 == null) {
                                 break label676;
                              }

                              var5 = 0;

                              while(true) {
                                 try {
                                    if (var5 >= var73.length) {
                                       break label676;
                                    }
                                 } catch (Throwable var18) {
                                    var10000 = var18;
                                    var10001 = false;
                                    break label636;
                                 }

                                 if (var73[var5] != null) {
                                    Drawable var75 = var73[var5];

                                    try {
                                       var78 = new PorterDuffColorFilter(var2, Mode.MULTIPLY);
                                       var75.setColorFilter(var78);
                                    } catch (Throwable var17) {
                                       var10000 = var17;
                                       var10001 = false;
                                       break label636;
                                    }
                                 }

                                 ++var5;
                              }
                           }

                           PorterDuffColorFilter var77;
                           try {
                              if (var70 instanceof ImageView) {
                                 ImageView var79 = (ImageView)var70;
                                 var77 = new PorterDuffColorFilter(var2, Mode.MULTIPLY);
                                 var79.setColorFilter(var77);
                                 break label676;
                              }
                           } catch (Throwable var37) {
                              var10000 = var37;
                              var10001 = false;
                              break label636;
                           }

                           label647: {
                              label648: {
                                 try {
                                    if (var70 instanceof BackupImageView) {
                                       var63 = ((BackupImageView)var70).getImageReceiver().getStaticThumb();
                                       if (!(var63 instanceof CombinedDrawable)) {
                                          break label648;
                                       }

                                       if ((this.changeFlags & FLAG_BACKGROUNDFILTER) == 0) {
                                          break label647;
                                       }

                                       var63 = ((CombinedDrawable)var63).getBackground();
                                       var77 = new PorterDuffColorFilter(var2, Mode.MULTIPLY);
                                       var63.setColorFilter(var77);
                                       break label676;
                                    }
                                 } catch (Throwable var57) {
                                    var10000 = var57;
                                    var10001 = false;
                                    break label636;
                                 }

                                 label534: {
                                    label650: {
                                       try {
                                          if (var70 instanceof Drawable) {
                                             if (!(var70 instanceof LetterDrawable)) {
                                                break label534;
                                             }

                                             if ((this.changeFlags & FLAG_BACKGROUNDFILTER) == 0) {
                                                break label650;
                                             }

                                             ((LetterDrawable)var70).setBackgroundColor(var2);
                                             break label676;
                                          }
                                       } catch (Throwable var56) {
                                          var10000 = var56;
                                          var10001 = false;
                                          break label636;
                                       }

                                       label520: {
                                          try {
                                             if (!(var70 instanceof CheckBox)) {
                                                break label520;
                                             }

                                             if ((this.changeFlags & FLAG_CHECKBOX) != 0) {
                                                ((CheckBox)var70).setBackgroundColor(var2);
                                                break label676;
                                             }
                                          } catch (Throwable var55) {
                                             var10000 = var55;
                                             var10001 = false;
                                             break label636;
                                          }

                                          try {
                                             if ((this.changeFlags & FLAG_CHECKBOXCHECK) != 0) {
                                                ((CheckBox)var70).setCheckColor(var2);
                                             }
                                             break label676;
                                          } catch (Throwable var31) {
                                             var10000 = var31;
                                             var10001 = false;
                                             break label636;
                                          }
                                       }

                                       try {
                                          if (var70 instanceof GroupCreateCheckBox) {
                                             ((GroupCreateCheckBox)var70).updateColors();
                                             break label676;
                                          }
                                       } catch (Throwable var54) {
                                          var10000 = var54;
                                          var10001 = false;
                                          break label636;
                                       }

                                       try {
                                          if (var70 instanceof Integer) {
                                             var69.set(var1, var2);
                                             break label676;
                                          }
                                       } catch (Throwable var36) {
                                          var10000 = var36;
                                          var10001 = false;
                                          break label636;
                                       }

                                       label506: {
                                          try {
                                             if (!(var70 instanceof RadioButton)) {
                                                break label506;
                                             }

                                             if ((this.changeFlags & FLAG_CHECKBOX) != 0) {
                                                ((RadioButton)var70).setBackgroundColor(var2);
                                                ((RadioButton)var70).invalidate();
                                                break label676;
                                             }
                                          } catch (Throwable var53) {
                                             var10000 = var53;
                                             var10001 = false;
                                             break label636;
                                          }

                                          try {
                                             if ((this.changeFlags & FLAG_CHECKBOXCHECK) != 0) {
                                                ((RadioButton)var70).setCheckedColor(var2);
                                                ((RadioButton)var70).invalidate();
                                             }
                                             break label676;
                                          } catch (Throwable var32) {
                                             var10000 = var32;
                                             var10001 = false;
                                             break label636;
                                          }
                                       }

                                       label654: {
                                          try {
                                             if (var70 instanceof TextPaint) {
                                                if ((this.changeFlags & FLAG_LINKCOLOR) == 0) {
                                                   break label654;
                                                }

                                                ((TextPaint)var70).linkColor = var2;
                                                break label676;
                                             }
                                          } catch (Throwable var52) {
                                             var10000 = var52;
                                             var10001 = false;
                                             break label636;
                                          }

                                          label489: {
                                             try {
                                                if (!(var70 instanceof LineProgressView)) {
                                                   break label489;
                                                }

                                                if ((this.changeFlags & FLAG_PROGRESSBAR) != 0) {
                                                   ((LineProgressView)var70).setProgressColor(var2);
                                                   break label676;
                                                }
                                             } catch (Throwable var51) {
                                                var10000 = var51;
                                                var10001 = false;
                                                break label636;
                                             }

                                             try {
                                                ((LineProgressView)var70).setBackColor(var2);
                                                break label676;
                                             } catch (Throwable var34) {
                                                var10000 = var34;
                                                var10001 = false;
                                                break label636;
                                             }
                                          }

                                          try {
                                             if (var70 instanceof Paint) {
                                                ((Paint)var70).setColor(var2);
                                                break label676;
                                             }
                                          } catch (Throwable var50) {
                                             var10000 = var50;
                                             var10001 = false;
                                             break label636;
                                          }

                                          try {
                                             if (!(var70 instanceof SeekBarView)) {
                                                break label676;
                                             }

                                             if ((this.changeFlags & FLAG_PROGRESSBAR) != 0) {
                                                ((SeekBarView)var70).setOuterColor(var2);
                                                break label676;
                                             }
                                          } catch (Throwable var49) {
                                             var10000 = var49;
                                             var10001 = false;
                                             break label636;
                                          }

                                          try {
                                             ((SeekBarView)var70).setInnerColor(var2);
                                             break label676;
                                          } catch (Throwable var35) {
                                             var10000 = var35;
                                             var10001 = false;
                                             break label636;
                                          }
                                       }

                                       try {
                                          ((TextPaint)var70).setColor(var2);
                                          break label676;
                                       } catch (Throwable var33) {
                                          var10000 = var33;
                                          var10001 = false;
                                          break label636;
                                       }
                                    }

                                    try {
                                       ((LetterDrawable)var70).setColor(var2);
                                       break label676;
                                    } catch (Throwable var26) {
                                       var10000 = var26;
                                       var10001 = false;
                                       break label636;
                                    }
                                 }

                                 label658: {
                                    try {
                                       if (var70 instanceof CombinedDrawable) {
                                          if ((this.changeFlags & FLAG_BACKGROUNDFILTER) == 0) {
                                             break label658;
                                          }

                                          var6 = ((CombinedDrawable)var70).getBackground();
                                          var78 = new PorterDuffColorFilter(var2, Mode.MULTIPLY);
                                          var6.setColorFilter(var78);
                                          break label676;
                                       }
                                    } catch (Throwable var48) {
                                       var10000 = var48;
                                       var10001 = false;
                                       break label636;
                                    }

                                    label678: {
                                       label456:
                                       try {
                                          if (!(var70 instanceof StateListDrawable) && (VERSION.SDK_INT < 21 || !(var70 instanceof RippleDrawable))) {
                                             break label456;
                                          }
                                          break label678;
                                       } catch (Throwable var47) {
                                          var10000 = var47;
                                          var10001 = false;
                                          break label636;
                                       }

                                       try {
                                          if (var70 instanceof GradientDrawable) {
                                             ((GradientDrawable)var70).setColor(var2);
                                             break label676;
                                          }
                                       } catch (Throwable var46) {
                                          var10000 = var46;
                                          var10001 = false;
                                          break label636;
                                       }

                                       try {
                                          var63 = (Drawable)var70;
                                          var77 = new PorterDuffColorFilter(var2, Mode.MULTIPLY);
                                          var63.setColorFilter(var77);
                                          break label676;
                                       } catch (Throwable var28) {
                                          var10000 = var28;
                                          var10001 = false;
                                          break label636;
                                       }
                                    }

                                    label405: {
                                       label404: {
                                          try {
                                             var63 = (Drawable)var70;
                                             if ((this.changeFlags & FLAG_DRAWABLESELECTEDSTATE) == 0) {
                                                break label404;
                                             }
                                          } catch (Throwable var30) {
                                             var10000 = var30;
                                             var10001 = false;
                                             break label636;
                                          }

                                          var7 = true;
                                          break label405;
                                       }

                                       var7 = false;
                                    }

                                    try {
                                       Theme.setSelectorDrawableColor(var63, var2, var7);
                                       break label676;
                                    } catch (Throwable var29) {
                                       var10000 = var29;
                                       var10001 = false;
                                       break label636;
                                    }
                                 }

                                 try {
                                    var63 = ((CombinedDrawable)var70).getIcon();
                                    var77 = new PorterDuffColorFilter(var2, Mode.MULTIPLY);
                                    var63.setColorFilter(var77);
                                    break label676;
                                 } catch (Throwable var27) {
                                    var10000 = var27;
                                    var10001 = false;
                                    break label636;
                                 }
                              }

                              if (var63 == null) {
                                 break label676;
                              }

                              try {
                                 var77 = new PorterDuffColorFilter(var2, Mode.MULTIPLY);
                                 var63.setColorFilter(var77);
                                 break label676;
                              } catch (Throwable var25) {
                                 var10000 = var25;
                                 var10001 = false;
                                 break label636;
                              }
                           }

                           try {
                              var63 = ((CombinedDrawable)var63).getIcon();
                              var77 = new PorterDuffColorFilter(var2, Mode.MULTIPLY);
                              var63.setColorFilter(var77);
                              break label676;
                           } catch (Throwable var24) {
                              var10000 = var24;
                              var10001 = false;
                              break label636;
                           }
                        }

                        try {
                           ((EditTextCaption)var70).setTextColor(var2);
                           break label676;
                        } catch (Throwable var15) {
                           var10000 = var15;
                           var10001 = false;
                        }
                     }

                     Throwable var80 = var10000;
                     FileLog.e(var80);
                     this.notFoundCachedFields.put(var8, true);
                  }
               }
            }
         }

         ++var3;
      }
   }

   public int getCurrentColor() {
      return this.currentColor;
   }

   public String getCurrentKey() {
      return this.currentKey;
   }

   public int getSetColor() {
      return Theme.getColor(this.currentKey);
   }

   public String getTitle() {
      return this.currentKey;
   }

   public void setColor(int var1, boolean var2) {
      this.setColor(var1, var2, true);
   }

   public void setColor(int var1, boolean var2, boolean var3) {
      if (var3) {
         Theme.setColor(this.currentKey, var1, var2);
      }

      Paint[] var4 = this.paintToUpdate;
      byte var5 = 0;
      int var6;
      if (var4 != null) {
         var6 = 0;

         while(true) {
            var4 = this.paintToUpdate;
            if (var6 >= var4.length) {
               break;
            }

            if ((this.changeFlags & FLAG_LINKCOLOR) != 0 && var4[var6] instanceof TextPaint) {
               ((TextPaint)var4[var6]).linkColor = var1;
            } else {
               this.paintToUpdate[var6].setColor(var1);
            }

            ++var6;
         }
      }

      if (this.drawablesToUpdate != null) {
         var6 = 0;

         while(true) {
            Drawable[] var9 = this.drawablesToUpdate;
            if (var6 >= var9.length) {
               break;
            }

            if (var9[var6] != null) {
               if (var9[var6] instanceof ScamDrawable) {
                  ((ScamDrawable)var9[var6]).setColor(var1);
               } else if (var9[var6] instanceof LottieDrawable) {
                  String var7 = this.lottieLayerName;
                  if (var7 != null) {
                     ((LottieDrawable)var9[var6]).addValueCallback(new KeyPath(new String[]{var7, "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(var1)));
                  }
               } else if (var9[var6] instanceof CombinedDrawable) {
                  if ((this.changeFlags & FLAG_BACKGROUNDFILTER) != 0) {
                     ((CombinedDrawable)var9[var6]).getBackground().setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
                  } else {
                     ((CombinedDrawable)var9[var6]).getIcon().setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
                  }
               } else if (var9[var6] instanceof AvatarDrawable) {
                  ((AvatarDrawable)var9[var6]).setColor(var1);
               } else {
                  var9[var6].setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
               }
            }

            ++var6;
         }
      }

      View var10 = this.viewToInvalidate;
      Drawable var11;
      if (var10 != null && this.listClasses == null && this.listClassesFieldName == null && ((this.changeFlags & FLAG_CHECKTAG) == 0 || this.checkTag(this.currentKey, var10))) {
         if ((this.changeFlags & FLAG_BACKGROUND) != 0) {
            var11 = this.viewToInvalidate.getBackground();
            if (var11 instanceof MessageBackgroundDrawable) {
               ((MessageBackgroundDrawable)var11).setColor(var1);
            } else {
               this.viewToInvalidate.setBackgroundColor(var1);
            }
         }

         var6 = this.changeFlags;
         if ((FLAG_BACKGROUNDFILTER & var6) != 0) {
            if ((var6 & FLAG_PROGRESSBAR) != 0) {
               var10 = this.viewToInvalidate;
               if (var10 instanceof EditTextBoldCursor) {
                  ((EditTextBoldCursor)var10).setErrorLineColor(var1);
               }
            } else {
               Drawable var12 = this.viewToInvalidate.getBackground();
               var11 = var12;
               if (var12 instanceof CombinedDrawable) {
                  if ((this.changeFlags & FLAG_DRAWABLESELECTEDSTATE) != 0) {
                     var11 = ((CombinedDrawable)var12).getBackground();
                  } else {
                     var11 = ((CombinedDrawable)var12).getIcon();
                  }
               }

               if (var11 != null) {
                  if (!(var11 instanceof StateListDrawable) && (VERSION.SDK_INT < 21 || !(var11 instanceof RippleDrawable))) {
                     if (var11 instanceof ShapeDrawable) {
                        ((ShapeDrawable)var11).getPaint().setColor(var1);
                     } else {
                        var11.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
                     }
                  } else {
                     if ((this.changeFlags & FLAG_DRAWABLESELECTEDSTATE) != 0) {
                        var2 = true;
                     } else {
                        var2 = false;
                     }

                     Theme.setSelectorDrawableColor(var11, var1, var2);
                  }
               }
            }
         }
      }

      var10 = this.viewToInvalidate;
      if (var10 instanceof ActionBar) {
         if ((this.changeFlags & FLAG_AB_ITEMSCOLOR) != 0) {
            ((ActionBar)var10).setItemsColor(var1, false);
         }

         if ((this.changeFlags & FLAG_AB_TITLECOLOR) != 0) {
            ((ActionBar)this.viewToInvalidate).setTitleColor(var1);
         }

         if ((this.changeFlags & FLAG_AB_SELECTORCOLOR) != 0) {
            ((ActionBar)this.viewToInvalidate).setItemsBackgroundColor(var1, false);
         }

         if ((this.changeFlags & FLAG_AB_AM_SELECTORCOLOR) != 0) {
            ((ActionBar)this.viewToInvalidate).setItemsBackgroundColor(var1, true);
         }

         if ((this.changeFlags & FLAG_AB_AM_ITEMSCOLOR) != 0) {
            ((ActionBar)this.viewToInvalidate).setItemsColor(var1, true);
         }

         if ((this.changeFlags & FLAG_AB_SUBTITLECOLOR) != 0) {
            ((ActionBar)this.viewToInvalidate).setSubtitleColor(var1);
         }

         if ((this.changeFlags & FLAG_AB_AM_BACKGROUND) != 0) {
            ((ActionBar)this.viewToInvalidate).setActionModeColor(var1);
         }

         if ((this.changeFlags & FLAG_AB_AM_TOPBACKGROUND) != 0) {
            ((ActionBar)this.viewToInvalidate).setActionModeTopColor(var1);
         }

         if ((this.changeFlags & FLAG_AB_SEARCHPLACEHOLDER) != 0) {
            ((ActionBar)this.viewToInvalidate).setSearchTextColor(var1, true);
         }

         if ((this.changeFlags & FLAG_AB_SEARCH) != 0) {
            ((ActionBar)this.viewToInvalidate).setSearchTextColor(var1, false);
         }

         var6 = this.changeFlags;
         if ((FLAG_AB_SUBMENUITEM & var6) != 0) {
            ActionBar var13 = (ActionBar)this.viewToInvalidate;
            if ((var6 & FLAG_IMAGECOLOR) != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            var13.setPopupItemsColor(var1, var2);
         }

         if ((this.changeFlags & FLAG_AB_SUBMENUBACKGROUND) != 0) {
            ((ActionBar)this.viewToInvalidate).setPopupBackgroundColor(var1);
         }
      }

      var10 = this.viewToInvalidate;
      if (var10 instanceof EmptyTextProgressView) {
         var6 = this.changeFlags;
         if ((FLAG_TEXTCOLOR & var6) != 0) {
            ((EmptyTextProgressView)var10).setTextColor(var1);
         } else if ((var6 & FLAG_PROGRESSBAR) != 0) {
            ((EmptyTextProgressView)var10).setProgressBarColor(var1);
         }
      }

      var10 = this.viewToInvalidate;
      if (var10 instanceof RadialProgressView) {
         ((RadialProgressView)var10).setProgressColor(var1);
      } else if (var10 instanceof LineProgressView) {
         if ((this.changeFlags & FLAG_PROGRESSBAR) != 0) {
            ((LineProgressView)var10).setProgressColor(var1);
         } else {
            ((LineProgressView)var10).setBackColor(var1);
         }
      } else if (var10 instanceof ContextProgressView) {
         ((ContextProgressView)var10).updateColors();
      }

      var6 = this.changeFlags;
      if ((FLAG_TEXTCOLOR & var6) != 0 && ((var6 & FLAG_CHECKTAG) == 0 || this.checkTag(this.currentKey, this.viewToInvalidate))) {
         var10 = this.viewToInvalidate;
         if (var10 instanceof TextView) {
            ((TextView)var10).setTextColor(var1);
         } else if (var10 instanceof NumberTextView) {
            ((NumberTextView)var10).setTextColor(var1);
         } else if (var10 instanceof SimpleTextView) {
            ((SimpleTextView)var10).setTextColor(var1);
         } else if (var10 instanceof ChatBigEmptyView) {
            ((ChatBigEmptyView)var10).setTextColor(var1);
         }
      }

      if ((this.changeFlags & FLAG_CURSORCOLOR) != 0) {
         var10 = this.viewToInvalidate;
         if (var10 instanceof EditTextBoldCursor) {
            ((EditTextBoldCursor)var10).setCursorColor(var1);
         }
      }

      var6 = this.changeFlags;
      if ((FLAG_HINTTEXTCOLOR & var6) != 0) {
         var10 = this.viewToInvalidate;
         if (var10 instanceof EditTextBoldCursor) {
            if ((var6 & FLAG_PROGRESSBAR) != 0) {
               ((EditTextBoldCursor)var10).setHeaderHintColor(var1);
            } else {
               ((EditTextBoldCursor)var10).setHintColor(var1);
            }
         } else if (var10 instanceof EditText) {
            ((EditText)var10).setHintTextColor(var1);
         }
      }

      var10 = this.viewToInvalidate;
      if (var10 != null && (this.changeFlags & FLAG_SERVICEBACKGROUND) != 0) {
         var11 = var10.getBackground();
         if (var11 != null) {
            var11.setColorFilter(Theme.colorFilter);
         }
      }

      var6 = this.changeFlags;
      if ((FLAG_IMAGECOLOR & var6) != 0 && ((var6 & FLAG_CHECKTAG) == 0 || this.checkTag(this.currentKey, this.viewToInvalidate))) {
         var10 = this.viewToInvalidate;
         if (var10 instanceof ImageView) {
            if ((this.changeFlags & FLAG_USEBACKGROUNDDRAWABLE) != 0) {
               var11 = ((ImageView)var10).getDrawable();
               if (var11 instanceof StateListDrawable || VERSION.SDK_INT >= 21 && var11 instanceof RippleDrawable) {
                  if ((this.changeFlags & FLAG_DRAWABLESELECTEDSTATE) != 0) {
                     var2 = true;
                  } else {
                     var2 = false;
                  }

                  Theme.setSelectorDrawableColor(var11, var1, var2);
               }
            } else {
               ((ImageView)var10).setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
            }
         } else if (!(var10 instanceof BackupImageView) && var10 instanceof SimpleTextView) {
            ((SimpleTextView)var10).setSideDrawablesColor(var1);
         }
      }

      var10 = this.viewToInvalidate;
      if (var10 instanceof ScrollView && (this.changeFlags & FLAG_LISTGLOWCOLOR) != 0) {
         AndroidUtilities.setScrollViewEdgeEffectColor((ScrollView)var10, var1);
      }

      var10 = this.viewToInvalidate;
      if (var10 instanceof ViewPager && (this.changeFlags & FLAG_LISTGLOWCOLOR) != 0) {
         AndroidUtilities.setViewPagerEdgeEffectColor((ViewPager)var10, var1);
      }

      var10 = this.viewToInvalidate;
      RecyclerListView var15;
      if (var10 instanceof RecyclerListView) {
         var15 = (RecyclerListView)var10;
         if ((this.changeFlags & FLAG_SELECTOR) != 0 && this.currentKey.equals("listSelectorSDK21")) {
            var15.setListSelectorColor(var1);
         }

         if ((this.changeFlags & FLAG_FASTSCROLL) != 0) {
            var15.updateFastScrollColors();
         }

         if ((this.changeFlags & FLAG_LISTGLOWCOLOR) != 0) {
            var15.setGlowColor(var1);
         }

         if ((this.changeFlags & FLAG_SECTIONS) != 0) {
            ArrayList var14 = var15.getHeaders();
            if (var14 != null) {
               for(var6 = 0; var6 < var14.size(); ++var6) {
                  this.processViewColor((View)var14.get(var6), var1);
               }
            }

            var14 = var15.getHeadersCache();
            if (var14 != null) {
               for(var6 = 0; var6 < var14.size(); ++var6) {
                  this.processViewColor((View)var14.get(var6), var1);
               }
            }

            var10 = var15.getPinnedHeader();
            if (var10 != null) {
               this.processViewColor(var10, var1);
            }
         }
      } else if (var10 != null) {
         Class[] var16 = this.listClasses;
         if (var16 == null || var16.length == 0) {
            var6 = this.changeFlags;
            if ((FLAG_SELECTOR & var6) != 0) {
               this.viewToInvalidate.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            } else if ((var6 & FLAG_SELECTORWHITE) != 0) {
               this.viewToInvalidate.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            }
         }
      }

      if (this.listClasses != null) {
         var10 = this.viewToInvalidate;
         int var8;
         if (var10 instanceof RecyclerListView) {
            var15 = (RecyclerListView)var10;
            var15.getRecycledViewPool().clear();
            var8 = var15.getHiddenChildCount();

            for(var6 = 0; var6 < var8; ++var6) {
               this.processViewColor(var15.getHiddenChildAt(var6), var1);
            }

            var8 = var15.getCachedChildCount();

            for(var6 = 0; var6 < var8; ++var6) {
               this.processViewColor(var15.getCachedChildAt(var6), var1);
            }

            var8 = var15.getAttachedScrapChildCount();

            for(var6 = 0; var6 < var8; ++var6) {
               this.processViewColor(var15.getAttachedScrapChildAt(var6), var1);
            }
         }

         var10 = this.viewToInvalidate;
         if (var10 instanceof ViewGroup) {
            ViewGroup var17 = (ViewGroup)var10;
            var8 = var17.getChildCount();

            for(var6 = var5; var6 < var8; ++var6) {
               this.processViewColor(var17.getChildAt(var6), var1);
            }
         }

         this.processViewColor(this.viewToInvalidate, var1);
      }

      this.currentColor = var1;
      ThemeDescription.ThemeDescriptionDelegate var18 = this.delegate;
      if (var18 != null) {
         var18.didSetColor();
      }

      var10 = this.viewToInvalidate;
      if (var10 != null) {
         var10.invalidate();
      }

   }

   public void setDefaultColor() {
      this.setColor(Theme.getDefaultColor(this.currentKey), true);
   }

   public ThemeDescription.ThemeDescriptionDelegate setDelegateDisabled() {
      ThemeDescription.ThemeDescriptionDelegate var1 = this.delegate;
      this.delegate = null;
      return var1;
   }

   public void setPreviousColor() {
      this.setColor(this.previousColor, this.previousIsDefault[0]);
   }

   public void startEditing() {
      int var1 = Theme.getColor(this.currentKey, this.previousIsDefault);
      this.previousColor = var1;
      this.currentColor = var1;
   }

   public interface ThemeDescriptionDelegate {
      void didSetColor();
   }
}

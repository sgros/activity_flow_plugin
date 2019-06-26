package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.StaticLayout.Builder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.URLSpanNoUnderline;

public class AboutLinkCell extends FrameLayout {
   private String oldText;
   private ClickableSpan pressedLink;
   private SpannableStringBuilder stringBuilder;
   private StaticLayout textLayout;
   private int textX;
   private int textY;
   private LinkPath urlPath = new LinkPath();
   private TextView valueTextView;

   public AboutLinkCell(Context var1) {
      super(var1);
      this.valueTextView = new TextView(var1);
      this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.valueTextView.setTextSize(1, 13.0F);
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      TextView var5 = this.valueTextView;
      boolean var2 = LocaleController.isRTL;
      byte var3 = 5;
      byte var4;
      if (var2) {
         var4 = 5;
      } else {
         var4 = 3;
      }

      var5.setGravity(var4);
      var5 = this.valueTextView;
      if (LocaleController.isRTL) {
         var4 = var3;
      } else {
         var4 = 3;
      }

      this.addView(var5, LayoutHelper.createFrame(-2, -2.0F, var4 | 80, 23.0F, 0.0F, 23.0F, 10.0F));
      this.setWillNotDraw(false);
   }

   private void resetPressedLink() {
      if (this.pressedLink != null) {
         this.pressedLink = null;
      }

      this.invalidate();
   }

   protected void didPressUrl(String var1) {
   }

   protected void onDraw(Canvas var1) {
      var1.save();
      int var2 = AndroidUtilities.dp(23.0F);
      this.textX = var2;
      float var3 = (float)var2;
      var2 = AndroidUtilities.dp(8.0F);
      this.textY = var2;
      var1.translate(var3, (float)var2);
      if (this.pressedLink != null) {
         var1.drawPath(this.urlPath, Theme.linkSelectionPaint);
      }

      try {
         if (this.textLayout != null) {
            this.textLayout.draw(var1);
         }
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

      var1.restore();
   }

   @SuppressLint({"DrawAllocation"})
   protected void onMeasure(int var1, int var2) {
      if (this.stringBuilder != null) {
         var2 = MeasureSpec.getSize(var1) - AndroidUtilities.dp(46.0F);
         if (VERSION.SDK_INT >= 24) {
            SpannableStringBuilder var3 = this.stringBuilder;
            Builder var4 = Builder.obtain(var3, 0, var3.length(), Theme.profile_aboutTextPaint, var2).setBreakStrategy(1).setHyphenationFrequency(0);
            Alignment var6;
            if (LocaleController.isRTL) {
               var6 = Alignment.ALIGN_RIGHT;
            } else {
               var6 = Alignment.ALIGN_LEFT;
            }

            this.textLayout = var4.setAlignment(var6).build();
         } else {
            this.textLayout = new StaticLayout(this.stringBuilder, Theme.profile_aboutTextPaint, var2, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         }
      }

      StaticLayout var7 = this.textLayout;
      if (var7 != null) {
         var2 = var7.getHeight();
      } else {
         var2 = AndroidUtilities.dp(20.0F);
      }

      int var5 = var2 + AndroidUtilities.dp(16.0F);
      var2 = var5;
      if (this.valueTextView.getVisibility() == 0) {
         var2 = var5 + AndroidUtilities.dp(23.0F);
      }

      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var5;
      boolean var24;
      label154: {
         label153: {
            Exception var22;
            label167: {
               float var2 = var1.getX();
               float var3 = var1.getY();
               StaticLayout var4 = this.textLayout;
               var5 = false;
               if (var4 != null) {
                  if (var1.getAction() == 0 || this.pressedLink != null && var1.getAction() == 1) {
                     Exception var10000;
                     boolean var10001;
                     if (var1.getAction() == 0) {
                        label159: {
                           this.resetPressedLink();

                           label160: {
                              int var6;
                              int var7;
                              try {
                                 var6 = (int)(var2 - (float)this.textX);
                                 var7 = (int)(var3 - (float)this.textY);
                                 var7 = this.textLayout.getLineForVertical(var7);
                                 var4 = this.textLayout;
                              } catch (Exception var18) {
                                 var10000 = var18;
                                 var10001 = false;
                                 break label160;
                              }

                              var3 = (float)var6;

                              try {
                                 var6 = var4.getOffsetForHorizontal(var7, var3);
                                 var2 = this.textLayout.getLineLeft(var7);
                              } catch (Exception var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break label160;
                              }

                              label168: {
                                 if (var2 <= var3) {
                                    label166: {
                                       Spannable var23;
                                       try {
                                          if (var2 + this.textLayout.getLineWidth(var7) < var3) {
                                             break label166;
                                          }

                                          var23 = (Spannable)this.textLayout.getText();
                                          ClickableSpan[] var8 = (ClickableSpan[])var23.getSpans(var6, var6, ClickableSpan.class);
                                          if (var8.length == 0) {
                                             break label168;
                                          }

                                          this.resetPressedLink();
                                          this.pressedLink = var8[0];
                                       } catch (Exception var19) {
                                          var10000 = var19;
                                          var10001 = false;
                                          break label160;
                                       }

                                       try {
                                          var7 = var23.getSpanStart(this.pressedLink);
                                          this.urlPath.setCurrentLayout(this.textLayout, var7, 0.0F);
                                          this.textLayout.getSelectionPath(var7, var23.getSpanEnd(this.pressedLink), this.urlPath);
                                          break label153;
                                       } catch (Exception var14) {
                                          var22 = var14;

                                          try {
                                             FileLog.e((Throwable)var22);
                                             break label153;
                                          } catch (Exception var13) {
                                             var22 = var13;
                                             var24 = true;
                                             break label167;
                                          }
                                       }
                                    }
                                 }

                                 try {
                                    this.resetPressedLink();
                                    break label159;
                                 } catch (Exception var16) {
                                    var10000 = var16;
                                    var10001 = false;
                                    break label160;
                                 }
                              }

                              try {
                                 this.resetPressedLink();
                                 break label159;
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                              }
                           }

                           var22 = var10000;
                           var24 = false;
                           break label167;
                        }
                     } else {
                        ClickableSpan var20 = this.pressedLink;
                        if (var20 != null) {
                           label103: {
                              label163: {
                                 label101: {
                                    String var21;
                                    try {
                                       if (!(var20 instanceof URLSpanNoUnderline)) {
                                          break label101;
                                       }

                                       var21 = ((URLSpanNoUnderline)var20).getURL();
                                       if (!var21.startsWith("@") && !var21.startsWith("#") && !var21.startsWith("/")) {
                                          break label103;
                                       }
                                    } catch (Exception var12) {
                                       var10000 = var12;
                                       var10001 = false;
                                       break label163;
                                    }

                                    try {
                                       this.didPressUrl(var21);
                                       break label103;
                                    } catch (Exception var9) {
                                       var10000 = var9;
                                       var10001 = false;
                                       break label163;
                                    }
                                 }

                                 try {
                                    if (var20 instanceof URLSpan) {
                                       Browser.openUrl(this.getContext(), ((URLSpan)this.pressedLink).getURL());
                                       break label103;
                                    }
                                 } catch (Exception var11) {
                                    var10000 = var11;
                                    var10001 = false;
                                    break label163;
                                 }

                                 try {
                                    var20.onClick(this);
                                    break label103;
                                 } catch (Exception var10) {
                                    var10000 = var10;
                                    var10001 = false;
                                 }
                              }

                              var22 = var10000;
                              FileLog.e((Throwable)var22);
                           }

                           this.resetPressedLink();
                           break label153;
                        }
                     }
                  } else if (var1.getAction() == 3) {
                     this.resetPressedLink();
                  }
               }

               var24 = false;
               break label154;
            }

            this.resetPressedLink();
            FileLog.e((Throwable)var22);
            break label154;
         }

         var24 = true;
      }

      if (var24 || super.onTouchEvent(var1)) {
         var5 = true;
      }

      return var5;
   }

   public void setText(String var1, boolean var2) {
      this.setTextAndValue(var1, (String)null, var2);
   }

   public void setTextAndValue(String var1, String var2, boolean var3) {
      if (!TextUtils.isEmpty(var1)) {
         if (var1 != null) {
            String var4 = this.oldText;
            if (var4 != null && var1.equals(var4)) {
               return;
            }
         }

         this.oldText = var1;
         this.stringBuilder = new SpannableStringBuilder(this.oldText);
         if (var3) {
            MessageObject.addLinks(false, this.stringBuilder, false);
         }

         Emoji.replaceEmoji(this.stringBuilder, Theme.profile_aboutTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
         if (TextUtils.isEmpty(var2)) {
            this.valueTextView.setVisibility(8);
         } else {
            this.valueTextView.setText(var2);
            this.valueTextView.setVisibility(0);
         }

         this.requestLayout();
      }

   }
}

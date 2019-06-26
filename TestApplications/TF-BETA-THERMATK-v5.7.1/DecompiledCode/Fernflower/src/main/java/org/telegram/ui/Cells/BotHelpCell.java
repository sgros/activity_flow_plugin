package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.Layout.Alignment;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;

public class BotHelpCell extends View {
   private BotHelpCell.BotHelpCellDelegate delegate;
   private int height;
   private String oldText;
   private ClickableSpan pressedLink;
   private StaticLayout textLayout;
   private int textX;
   private int textY;
   private LinkPath urlPath = new LinkPath();
   private int width;

   public BotHelpCell(Context var1) {
      super(var1);
   }

   private void resetPressedLink() {
      if (this.pressedLink != null) {
         this.pressedLink = null;
      }

      this.invalidate();
   }

   protected void onDraw(Canvas var1) {
      int var2 = (this.getWidth() - this.width) / 2;
      int var3 = AndroidUtilities.dp(4.0F);
      Theme.chat_msgInMediaShadowDrawable.setBounds(var2, var3, this.width + var2, this.height + var3);
      Theme.chat_msgInMediaShadowDrawable.draw(var1);
      Theme.chat_msgInMediaDrawable.setBounds(var2, var3, this.width + var2, this.height + var3);
      Theme.chat_msgInMediaDrawable.draw(var1);
      Theme.chat_msgTextPaint.setColor(Theme.getColor("chat_messageTextIn"));
      Theme.chat_msgTextPaint.linkColor = Theme.getColor("chat_messageLinkIn");
      var1.save();
      var2 += AndroidUtilities.dp(11.0F);
      this.textX = var2;
      float var4 = (float)var2;
      var3 += AndroidUtilities.dp(11.0F);
      this.textY = var3;
      var1.translate(var4, (float)var3);
      if (this.pressedLink != null) {
         var1.drawPath(this.urlPath, Theme.chat_urlPaint);
      }

      StaticLayout var5 = this.textLayout;
      if (var5 != null) {
         var5.draw(var1);
      }

      var1.restore();
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setText(this.textLayout.getText());
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), this.height + AndroidUtilities.dp(8.0F));
   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var5;
      boolean var24;
      label152: {
         Exception var22;
         label151: {
            Exception var10000;
            label150: {
               label164: {
                  float var2 = var1.getX();
                  float var3 = var1.getY();
                  StaticLayout var4 = this.textLayout;
                  var5 = false;
                  if (var4 != null) {
                     if (var1.getAction() == 0 || this.pressedLink != null && var1.getAction() == 1) {
                        boolean var10001;
                        if (var1.getAction() == 0) {
                           label165: {
                              this.resetPressedLink();

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
                                 break label150;
                              }

                              var3 = (float)var6;

                              try {
                                 var6 = var4.getOffsetForHorizontal(var7, var3);
                                 var2 = this.textLayout.getLineLeft(var7);
                              } catch (Exception var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break label150;
                              }

                              if (var2 <= var3) {
                                 label163: {
                                    label133: {
                                       Spannable var8;
                                       try {
                                          if (var2 + this.textLayout.getLineWidth(var7) < var3) {
                                             break label163;
                                          }

                                          var8 = (Spannable)this.textLayout.getText();
                                          ClickableSpan[] var23 = (ClickableSpan[])var8.getSpans(var6, var6, ClickableSpan.class);
                                          if (var23.length == 0) {
                                             break label133;
                                          }

                                          this.resetPressedLink();
                                          this.pressedLink = var23[0];
                                       } catch (Exception var19) {
                                          var10000 = var19;
                                          var10001 = false;
                                          break label150;
                                       }

                                       try {
                                          var6 = var8.getSpanStart(this.pressedLink);
                                          this.urlPath.setCurrentLayout(this.textLayout, var6, 0.0F);
                                          this.textLayout.getSelectionPath(var6, var8.getSpanEnd(this.pressedLink), this.urlPath);
                                          break label164;
                                       } catch (Exception var14) {
                                          var22 = var14;

                                          try {
                                             FileLog.e((Throwable)var22);
                                             break label164;
                                          } catch (Exception var13) {
                                             var22 = var13;
                                             var24 = true;
                                             break label151;
                                          }
                                       }
                                    }

                                    try {
                                       this.resetPressedLink();
                                       break label165;
                                    } catch (Exception var16) {
                                       var10000 = var16;
                                       var10001 = false;
                                       break label150;
                                    }
                                 }
                              }

                              try {
                                 this.resetPressedLink();
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label150;
                              }
                           }
                        } else {
                           ClickableSpan var20 = this.pressedLink;
                           if (var20 != null) {
                              label105: {
                                 label160: {
                                    label103: {
                                       String var21;
                                       try {
                                          if (!(var20 instanceof URLSpanNoUnderline)) {
                                             break label103;
                                          }

                                          var21 = ((URLSpanNoUnderline)var20).getURL();
                                          if (!var21.startsWith("@") && !var21.startsWith("#") && !var21.startsWith("/")) {
                                             break label105;
                                          }
                                       } catch (Exception var12) {
                                          var10000 = var12;
                                          var10001 = false;
                                          break label160;
                                       }

                                       try {
                                          if (this.delegate != null) {
                                             this.delegate.didPressUrl(var21);
                                          }
                                          break label105;
                                       } catch (Exception var9) {
                                          var10000 = var9;
                                          var10001 = false;
                                          break label160;
                                       }
                                    }

                                    try {
                                       if (var20 instanceof URLSpan) {
                                          Browser.openUrl(this.getContext(), ((URLSpan)this.pressedLink).getURL());
                                          break label105;
                                       }
                                    } catch (Exception var11) {
                                       var10000 = var11;
                                       var10001 = false;
                                       break label160;
                                    }

                                    try {
                                       var20.onClick(this);
                                       break label105;
                                    } catch (Exception var10) {
                                       var10000 = var10;
                                       var10001 = false;
                                    }
                                 }

                                 var22 = var10000;
                                 FileLog.e((Throwable)var22);
                              }

                              this.resetPressedLink();
                              break label164;
                           }
                        }
                     } else if (var1.getAction() == 3) {
                        this.resetPressedLink();
                     }
                  }

                  var24 = false;
                  break label152;
               }

               var24 = true;
               break label152;
            }

            var22 = var10000;
            var24 = false;
         }

         this.resetPressedLink();
         FileLog.e((Throwable)var22);
      }

      if (var24 || super.onTouchEvent(var1)) {
         var5 = true;
      }

      return var5;
   }

   public void setDelegate(BotHelpCell.BotHelpCellDelegate var1) {
      this.delegate = var1;
   }

   public void setText(String var1) {
      if (var1 != null && var1.length() != 0) {
         if (var1 == null || !var1.equals(this.oldText)) {
            this.oldText = var1;
            byte var2 = 0;
            this.setVisibility(0);
            int var3;
            if (AndroidUtilities.isTablet()) {
               var3 = AndroidUtilities.getMinTabletSide();
            } else {
               Point var4 = AndroidUtilities.displaySize;
               var3 = Math.min(var4.x, var4.y);
            }

            int var5 = (int)((float)var3 * 0.7F);
            String[] var13 = var1.split("\n");
            SpannableStringBuilder var11 = new SpannableStringBuilder();
            String var6 = LocaleController.getString("BotInfoTitle", 2131558851);
            var11.append(var6);
            var11.append("\n\n");

            for(var3 = 0; var3 < var13.length; ++var3) {
               var11.append(var13[var3].trim());
               if (var3 != var13.length - 1) {
                  var11.append("\n");
               }
            }

            MessageObject.addLinks(false, var11);
            var11.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0, var6.length(), 33);
            Emoji.replaceEmoji(var11, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);

            label60: {
               Exception var10000;
               label74: {
                  int var7;
                  boolean var10001;
                  try {
                     StaticLayout var14 = new StaticLayout(var11, Theme.chat_msgTextPaint, var5, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                     this.textLayout = var14;
                     this.width = 0;
                     this.height = this.textLayout.getHeight() + AndroidUtilities.dp(22.0F);
                     var7 = this.textLayout.getLineCount();
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label74;
                  }

                  for(var3 = var2; var3 < var7; ++var3) {
                     try {
                        this.width = (int)Math.ceil((double)Math.max((float)this.width, this.textLayout.getLineWidth(var3) + this.textLayout.getLineLeft(var3)));
                     } catch (Exception var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label74;
                     }
                  }

                  try {
                     if (this.width > var5) {
                        this.width = var5;
                     }
                     break label60;
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                  }
               }

               Exception var12 = var10000;
               FileLog.e((Throwable)var12);
            }

            this.width += AndroidUtilities.dp(22.0F);
         }
      } else {
         this.setVisibility(8);
      }
   }

   public interface BotHelpCellDelegate {
      void didPressUrl(String var1);
   }
}

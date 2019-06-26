package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;

public class ChatActionCell extends BaseCell {
   private AvatarDrawable avatarDrawable;
   private int currentAccount;
   private MessageObject currentMessageObject;
   private int customDate;
   private CharSequence customText;
   private ChatActionCell.ChatActionCellDelegate delegate;
   private boolean hasReplyMessage;
   private boolean imagePressed;
   private ImageReceiver imageReceiver;
   private float lastTouchX;
   private float lastTouchY;
   private URLSpan pressedLink;
   private int previousWidth;
   private int textHeight;
   private StaticLayout textLayout;
   private int textWidth;
   private int textX;
   private int textXLeft;
   private int textY;
   private boolean wasLayout;

   public ChatActionCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.imageReceiver = new ImageReceiver(this);
      this.imageReceiver.setRoundRadius(AndroidUtilities.dp(32.0F));
      this.avatarDrawable = new AvatarDrawable();
   }

   private void buildLayout() {
      MessageObject var1 = this.currentMessageObject;
      Object var3;
      if (var1 != null) {
         label28: {
            TLRPC.Message var2 = var1.messageOwner;
            if (var2 != null) {
               TLRPC.MessageMedia var4 = var2.media;
               if (var4 != null && var4.ttl_seconds != 0) {
                  if (var4.photo instanceof TLRPC.TL_photoEmpty) {
                     var3 = LocaleController.getString("AttachPhotoExpired", 2131558728);
                  } else if (var4.document instanceof TLRPC.TL_documentEmpty) {
                     var3 = LocaleController.getString("AttachVideoExpired", 2131558734);
                  } else {
                     var3 = var1.messageText;
                  }
                  break label28;
               }
            }

            var3 = this.currentMessageObject.messageText;
         }
      } else {
         var3 = this.customText;
      }

      this.createLayout((CharSequence)var3, this.previousWidth);
      var1 = this.currentMessageObject;
      if (var1 != null && var1.type == 11) {
         this.imageReceiver.setImageCoords((this.previousWidth - AndroidUtilities.dp(64.0F)) / 2, this.textHeight + AndroidUtilities.dp(15.0F), AndroidUtilities.dp(64.0F), AndroidUtilities.dp(64.0F));
      }

   }

   private void createLayout(CharSequence var1, int var2) {
      int var3 = var2 - AndroidUtilities.dp(30.0F);
      this.textLayout = new StaticLayout(var1, Theme.chat_actionTextPaint, var3, Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
      int var4 = 0;
      this.textHeight = 0;
      this.textWidth = 0;

      label54: {
         Exception var14;
         Exception var10000;
         label53: {
            int var5;
            boolean var10001;
            try {
               var5 = this.textLayout.getLineCount();
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label53;
            }

            while(true) {
               if (var4 >= var5) {
                  break label54;
               }

               float var8;
               label59: {
                  label61: {
                     float var6;
                     try {
                        var6 = this.textLayout.getLineWidth(var4);
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label61;
                     }

                     float var7 = (float)var3;
                     var8 = var6;
                     if (var6 > var7) {
                        var8 = var7;
                     }

                     try {
                        this.textHeight = (int)Math.max((double)this.textHeight, Math.ceil((double)this.textLayout.getLineBottom(var4)));
                        break label59;
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                     }
                  }

                  var14 = var10000;

                  try {
                     FileLog.e((Throwable)var14);
                     return;
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  this.textWidth = (int)Math.max((double)this.textWidth, Math.ceil((double)var8));
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break;
               }

               ++var4;
            }
         }

         var14 = var10000;
         FileLog.e((Throwable)var14);
      }

      this.textX = (var2 - this.textWidth) / 2;
      this.textY = AndroidUtilities.dp(7.0F);
      this.textXLeft = (var2 - this.textLayout.getWidth()) / 2;
   }

   private int findMaxWidthAroundLine(int var1) {
      int var2 = (int)Math.ceil((double)this.textLayout.getLineWidth(var1));
      int var3 = this.textLayout.getLineCount();

      int var4;
      for(var4 = var1 + 1; var4 < var3; ++var4) {
         int var5 = (int)Math.ceil((double)this.textLayout.getLineWidth(var4));
         if (Math.abs(var5 - var2) >= AndroidUtilities.dp(10.0F)) {
            break;
         }

         var2 = Math.max(var5, var2);
      }

      --var1;

      while(var1 >= 0) {
         var4 = (int)Math.ceil((double)this.textLayout.getLineWidth(var1));
         if (Math.abs(var4 - var2) >= AndroidUtilities.dp(10.0F)) {
            break;
         }

         var2 = Math.max(var4, var2);
         --var1;
      }

      return var2;
   }

   private boolean isLineBottom(int var1, int var2, int var3, int var4, int var5) {
      boolean var6 = true;
      var2 = var4 - 1;
      boolean var7 = var6;
      if (var3 != var2) {
         if (var3 >= 0 && var3 <= var2 && this.findMaxWidthAroundLine(var3 + 1) + var5 * 3 < var1) {
            var7 = var6;
         } else {
            var7 = false;
         }
      }

      return var7;
   }

   private boolean isLineTop(int var1, int var2, int var3, int var4, int var5) {
      boolean var6 = true;
      boolean var7 = var6;
      if (var3 != 0) {
         if (var3 >= 0 && var3 < var4 && this.findMaxWidthAroundLine(var3 - 1) + var5 * 3 < var1) {
            var7 = var6;
         } else {
            var7 = false;
         }
      }

      return var7;
   }

   public int getCustomDate() {
      return this.customDate;
   }

   public MessageObject getMessageObject() {
      return this.currentMessageObject;
   }

   public ImageReceiver getPhotoImage() {
      return this.imageReceiver;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.wasLayout = false;
   }

   protected void onDraw(Canvas var1) {
      Canvas var3 = var1;
      MessageObject var4 = this.currentMessageObject;
      if (var4 != null && var4.type == 11) {
         this.imageReceiver.draw(var1);
      }

      StaticLayout var2 = this.textLayout;
      if (var2 != null) {
         int var5 = var2.getLineCount();
         int var6 = AndroidUtilities.dp(11.0F);
         int var7 = AndroidUtilities.dp(6.0F);
         int var8 = var6 - var7;
         int var9 = AndroidUtilities.dp(8.0F);
         int var10 = AndroidUtilities.dp(7.0F);
         int var11 = 0;
         int var12 = 0;

         int var14;
         for(int var13 = 0; var13 < var5; var12 = var14) {
            var14 = this.findMaxWidthAroundLine(var13);
            int var15 = (this.getMeasuredWidth() - var14 - var8) / 2;
            int var16 = var14 + var8;
            int var17 = this.textLayout.getLineBottom(var13);
            int var18 = var17 - var11;
            boolean var35;
            if (var13 == var5 - 1) {
               var35 = true;
            } else {
               var35 = false;
            }

            boolean var19;
            if (var13 == 0) {
               var19 = true;
            } else {
               var19 = false;
            }

            var14 = var18;
            int var20 = var10;
            if (var19) {
               var20 = var10 - AndroidUtilities.dp(3.0F);
               var14 = var18 + AndroidUtilities.dp(3.0F);
            }

            int var21 = var14;
            if (var35) {
               var21 = var14 + AndroidUtilities.dp(3.0F);
            }

            int var22;
            byte var34;
            boolean var38;
            label147: {
               if (!var35) {
                  var10 = var13 + 1;
                  if (var10 < var5) {
                     var10 = this.findMaxWidthAroundLine(var10) + var8;
                     var14 = var8 * 2;
                     if (var10 + var14 < var16) {
                        var22 = var10;
                        var34 = 1;
                        var38 = true;
                     } else if (var16 + var14 < var10) {
                        var22 = var10;
                        var34 = 2;
                        var38 = var35;
                     } else {
                        var22 = var10;
                        var34 = 3;
                        var38 = var35;
                     }
                     break label147;
                  }
               }

               var34 = 0;
               var22 = 0;
               var38 = var35;
            }

            boolean var23;
            byte var36;
            if (!var19 && var13 > 0) {
               var11 = this.findMaxWidthAroundLine(var13 - 1) + var8;
               var18 = var8 * 2;
               if (var11 + var18 < var16) {
                  var18 = var11;
                  var36 = 1;
                  var23 = true;
               } else if (var16 + var18 < var11) {
                  var18 = var11;
                  var36 = 2;
                  var23 = var19;
               } else {
                  var18 = var11;
                  var36 = 3;
                  var23 = var19;
               }
            } else {
               var36 = 0;
               var18 = 0;
               var23 = var19;
            }

            int var24;
            int var25;
            int var26;
            float var27;
            float var28;
            int var29;
            Drawable var32;
            int var39;
            byte var41;
            if (var34 != 0) {
               if (var34 == 1) {
                  var24 = (this.getMeasuredWidth() - var22) / 2;
                  var25 = AndroidUtilities.dp(3.0F);
                  if (this.isLineBottom(var22, var16, var13 + 1, var5, var8)) {
                     var27 = (float)(var15 + var7);
                     var26 = var20 + var21;
                     var28 = (float)var26;
                     var1.drawRect(var27, var28, (float)(var24 - var8), (float)(AndroidUtilities.dp(3.0F) + var26), Theme.chat_actionBackgroundPaint);
                     var1.drawRect((float)(var24 + var22 + var8), var28, (float)(var15 + var16 - var7), (float)(var26 + AndroidUtilities.dp(3.0F)), Theme.chat_actionBackgroundPaint);
                  } else {
                     var27 = (float)(var15 + var7);
                     var26 = var20 + var21;
                     var28 = (float)var26;
                     var1.drawRect(var27, var28, (float)var24, (float)(AndroidUtilities.dp(3.0F) + var26), Theme.chat_actionBackgroundPaint);
                     var1.drawRect((float)(var24 + var22), var28, (float)(var15 + var16 - var7), (float)(var26 + AndroidUtilities.dp(3.0F)), Theme.chat_actionBackgroundPaint);
                  }

                  var3 = var1;
                  var41 = var36;
                  var11 = var25;
               } else if (var34 == 2) {
                  var25 = AndroidUtilities.dp(3.0F);
                  var24 = var20 + var21 - AndroidUtilities.dp(11.0F);
                  var39 = var15 - var9;
                  var22 = var39;
                  if (var36 != 2) {
                     var22 = var39;
                     if (var36 != 3) {
                        var22 = var39 - var8;
                     }
                  }

                  if (var23 || var38) {
                     var39 = var22 + var9;
                     var1.drawRect((float)var39, (float)(AndroidUtilities.dp(3.0F) + var24), (float)(var39 + var6), (float)(var24 + var6), Theme.chat_actionBackgroundPaint);
                  }

                  byte var40 = var36;
                  Drawable var33 = Theme.chat_cornerInner[2];
                  var29 = var24 + var9;
                  var33.setBounds(var22, var24, var22 + var9, var29);
                  var32 = Theme.chat_cornerInner[2];
                  var3 = var1;
                  var32.draw(var1);
                  var22 = var15 + var16;
                  var11 = var22;
                  if (var40 != 2) {
                     var11 = var22;
                     if (var40 != 3) {
                        var11 = var22 + var8;
                     }
                  }

                  if (var23 || var38) {
                     var1.drawRect((float)(var11 - var6), (float)(AndroidUtilities.dp(3.0F) + var24), (float)var11, (float)(var24 + var6), Theme.chat_actionBackgroundPaint);
                  }

                  var41 = var40;
                  Theme.chat_cornerInner[3].setBounds(var11, var24, var11 + var9, var29);
                  Theme.chat_cornerInner[3].draw(var1);
                  var11 = var25;
               } else {
                  var3 = var1;
                  var39 = AndroidUtilities.dp(6.0F);
                  var41 = var36;
                  var11 = var39;
               }
            } else {
               var41 = var36;
               var11 = 0;
            }

            var39 = var15;
            var25 = var12;
            var12 = var6;
            var15 = var13;
            byte var37 = var34;
            if (var41 != 0) {
               if (var41 == 1) {
                  var25 = (this.getMeasuredWidth() - var18) / 2;
                  var10 = var20 - AndroidUtilities.dp(3.0F);
                  var6 = var21 + AndroidUtilities.dp(3.0F);
                  if (this.isLineTop(var18, var16, var15 - 1, var5, var8)) {
                     var28 = (float)(var39 + var7);
                     var27 = (float)var10;
                     var1.drawRect(var28, var27, (float)(var25 - var8), (float)(AndroidUtilities.dp(3.0F) + var10), Theme.chat_actionBackgroundPaint);
                     var1.drawRect((float)(var25 + var18 + var8), var27, (float)(var39 + var16 - var7), (float)(AndroidUtilities.dp(3.0F) + var10), Theme.chat_actionBackgroundPaint);
                  } else {
                     var28 = (float)(var39 + var7);
                     var27 = (float)var10;
                     var1.drawRect(var28, var27, (float)var25, (float)(AndroidUtilities.dp(3.0F) + var10), Theme.chat_actionBackgroundPaint);
                     var1.drawRect((float)(var25 + var18), var27, (float)(var39 + var16 - var7), (float)(AndroidUtilities.dp(3.0F) + var10), Theme.chat_actionBackgroundPaint);
                  }
               } else if (var41 == 2) {
                  var18 = var20 - AndroidUtilities.dp(3.0F);
                  var6 = var21 + AndroidUtilities.dp(3.0F);
                  var26 = var39 - var9;
                  var10 = var26;
                  if (var37 != 2) {
                     var10 = var26;
                     if (var37 != 3) {
                        var10 = var26 - var8;
                     }
                  }

                  if (var23 || var38) {
                     var26 = var10 + var9;
                     var1.drawRect((float)var26, (float)(AndroidUtilities.dp(3.0F) + var18), (float)(var26 + var12), (float)(var18 + AndroidUtilities.dp(11.0F)), Theme.chat_actionBackgroundPaint);
                  }

                  var32 = Theme.chat_cornerInner[0];
                  var29 = var25 + var9;
                  var32.setBounds(var10, var25, var10 + var9, var29);
                  Theme.chat_cornerInner[0].draw(var3);
                  var24 = var39 + var16;
                  var10 = var24;
                  if (var37 != 2) {
                     var10 = var24;
                     if (var37 != 3) {
                        var10 = var24 + var8;
                     }
                  }

                  if (var23 || var38) {
                     var1.drawRect((float)(var10 - var12), (float)(AndroidUtilities.dp(3.0F) + var18), (float)var10, (float)(AndroidUtilities.dp(11.0F) + var18), Theme.chat_actionBackgroundPaint);
                  }

                  Theme.chat_cornerInner[1].setBounds(var10, var25, var10 + var9, var29);
                  Theme.chat_cornerInner[1].draw(var3);
                  var10 = var18;
               } else {
                  var10 = AndroidUtilities.dp(6.0F);
                  var6 = var21 + AndroidUtilities.dp(6.0F);
                  var10 = var20 - var10;
               }
            } else {
               var10 = var20;
               var6 = var21;
            }

            if (!var23 && !var38) {
               var1.drawRect((float)var39, (float)var20, (float)(var39 + var16), (float)(var20 + var21), Theme.chat_actionBackgroundPaint);
            } else {
               var1.drawRect((float)(var39 + var7), (float)var20, (float)(var39 + var16 - var7), (float)(var20 + var21), Theme.chat_actionBackgroundPaint);
            }

            var18 = var39 - var8;
            var39 = var39 + var16 - var7;
            float var30;
            if (var23 && !var38 && var37 != 2) {
               var27 = (float)var18;
               var30 = (float)(var10 + var12);
               var28 = (float)(var18 + var12);
               var22 = var10 + var6 + var11;
               var1.drawRect(var27, var30, var28, (float)(var22 - AndroidUtilities.dp(6.0F)), Theme.chat_actionBackgroundPaint);
               var1.drawRect((float)var39, var30, (float)(var39 + var12), (float)(var22 - AndroidUtilities.dp(6.0F)), Theme.chat_actionBackgroundPaint);
            } else {
               float var31;
               if (var38 && !var23 && var41 != 2) {
                  var30 = (float)var18;
                  var22 = var10 + var12;
                  var27 = (float)(var22 - AndroidUtilities.dp(5.0F));
                  var31 = (float)(var18 + var12);
                  var28 = (float)(var10 + var6 + var11 - var12);
                  var1.drawRect(var30, var27, var31, var28, Theme.chat_actionBackgroundPaint);
                  var1.drawRect((float)var39, (float)(var22 - AndroidUtilities.dp(5.0F)), (float)(var39 + var12), var28, Theme.chat_actionBackgroundPaint);
               } else if (var23 || var38) {
                  var27 = (float)var18;
                  var28 = (float)(var10 + var12);
                  var31 = (float)(var18 + var12);
                  var30 = (float)(var10 + var6 + var11 - var12);
                  var1.drawRect(var27, var28, var31, var30, Theme.chat_actionBackgroundPaint);
                  var1.drawRect((float)var39, var28, (float)(var39 + var12), var30, Theme.chat_actionBackgroundPaint);
               }
            }

            if (var23) {
               var32 = Theme.chat_cornerOuter[0];
               var22 = var10 + var12;
               var32.setBounds(var18, var10, var18 + var12, var22);
               Theme.chat_cornerOuter[0].draw(var3);
               Theme.chat_cornerOuter[1].setBounds(var39, var10, var39 + var12, var22);
               Theme.chat_cornerOuter[1].draw(var3);
            }

            if (var38) {
               var22 = var10 + var6 + var11 - var12;
               var32 = Theme.chat_cornerOuter[2];
               var14 = var22 + var12;
               var32.setBounds(var39, var22, var39 + var12, var14);
               Theme.chat_cornerOuter[2].draw(var3);
               Theme.chat_cornerOuter[3].setBounds(var18, var22, var18 + var12, var14);
               Theme.chat_cornerOuter[3].draw(var3);
            }

            var10 += var6;
            var14 = var10 + var11;
            var13 = var15 + 1;
            var11 = var17;
            var6 = var12;
         }

         var1.save();
         var3.translate((float)this.textXLeft, (float)this.textY);
         this.textLayout.draw(var3);
         var1.restore();
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      if (!TextUtils.isEmpty(this.customText) || this.currentMessageObject != null) {
         CharSequence var2;
         if (!TextUtils.isEmpty(this.customText)) {
            var2 = this.customText;
         } else {
            var2 = this.currentMessageObject.messageText;
         }

         var1.setText(var2);
         var1.setEnabled(true);
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
   }

   protected void onLongPress() {
      ChatActionCell.ChatActionCellDelegate var1 = this.delegate;
      if (var1 != null) {
         var1.didLongPress(this, this.lastTouchX, this.lastTouchY);
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (this.currentMessageObject == null && this.customText == null) {
         this.setMeasuredDimension(MeasureSpec.getSize(var1), this.textHeight + AndroidUtilities.dp(14.0F));
      } else {
         int var3 = Math.max(AndroidUtilities.dp(30.0F), MeasureSpec.getSize(var1));
         if (this.previousWidth != var3) {
            this.wasLayout = true;
            this.previousWidth = var3;
            this.buildLayout();
         }

         var2 = this.textHeight;
         MessageObject var4 = this.currentMessageObject;
         byte var5;
         if (var4 != null && var4.type == 11) {
            var5 = 70;
         } else {
            var5 = 0;
         }

         this.setMeasuredDimension(var3, var2 + AndroidUtilities.dp((float)(14 + var5)));
      }
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (this.currentMessageObject == null) {
         return super.onTouchEvent(var1);
      } else {
         float var2;
         float var3;
         int var4;
         boolean var5;
         boolean var6;
         boolean var7;
         label100: {
            var2 = var1.getX();
            this.lastTouchX = var2;
            var3 = var1.getY();
            this.lastTouchY = var3;
            var4 = var1.getAction();
            var5 = true;
            if (var4 == 0) {
               if (this.delegate != null) {
                  if (this.currentMessageObject.type == 11 && this.imageReceiver.isInsideImage(var2, var3)) {
                     this.imagePressed = true;
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  var7 = var6;
                  if (var6) {
                     this.startCheckLongPress();
                     var7 = var6;
                  }
                  break label100;
               }
            } else {
               if (var1.getAction() != 2) {
                  this.cancelCheckLongPress();
               }

               if (this.imagePressed) {
                  if (var1.getAction() == 1) {
                     this.imagePressed = false;
                     ChatActionCell.ChatActionCellDelegate var8 = this.delegate;
                     if (var8 != null) {
                        var8.didClickImage(this);
                        this.playSoundEffect(0);
                     }
                  } else if (var1.getAction() == 3) {
                     this.imagePressed = false;
                  } else if (var1.getAction() == 2 && !this.imageReceiver.isInsideImage(var2, var3)) {
                     this.imagePressed = false;
                  }
               }
            }

            var7 = false;
         }

         var6 = var7;
         if (!var7) {
            label104: {
               if (var1.getAction() != 0) {
                  var6 = var7;
                  if (this.pressedLink == null) {
                     break label104;
                  }

                  var6 = var7;
                  if (var1.getAction() != 1) {
                     break label104;
                  }
               }

               int var9 = this.textX;
               if (var2 >= (float)var9) {
                  var4 = this.textY;
                  if (var3 >= (float)var4 && var2 <= (float)(var9 + this.textWidth) && var3 <= (float)(this.textHeight + var4)) {
                     float var10 = (float)var4;
                     var2 -= (float)this.textXLeft;
                     var9 = this.textLayout.getLineForVertical((int)(var3 - var10));
                     var4 = this.textLayout.getOffsetForHorizontal(var9, var2);
                     var10 = this.textLayout.getLineLeft(var9);
                     if (var10 <= var2 && var10 + this.textLayout.getLineWidth(var9) >= var2) {
                        CharSequence var11 = this.currentMessageObject.messageText;
                        if (var11 instanceof Spannable) {
                           URLSpan[] var12 = (URLSpan[])((Spannable)var11).getSpans(var4, var4, URLSpan.class);
                           if (var12.length != 0) {
                              if (var1.getAction() == 0) {
                                 this.pressedLink = var12[0];
                                 var7 = var5;
                              } else if (var12[0] == this.pressedLink) {
                                 var7 = var5;
                                 if (this.delegate != null) {
                                    String var13 = var12[0].getURL();
                                    if (var13.startsWith("game")) {
                                       this.delegate.didPressReplyMessage(this, this.currentMessageObject.messageOwner.reply_to_msg_id);
                                       var7 = var5;
                                    } else if (var13.startsWith("http")) {
                                       Browser.openUrl(this.getContext(), var13);
                                       var7 = var5;
                                    } else {
                                       this.delegate.needOpenUserProfile(Integer.parseInt(var13));
                                       var7 = var5;
                                    }
                                 }
                              }
                           } else {
                              this.pressedLink = null;
                           }

                           var6 = var7;
                           break label104;
                        }
                     }

                     this.pressedLink = null;
                     var6 = var7;
                     break label104;
                  }
               }

               this.pressedLink = null;
               var6 = var7;
            }
         }

         var7 = var6;
         if (!var6) {
            var7 = super.onTouchEvent(var1);
         }

         return var7;
      }
   }

   public void setCustomDate(int var1) {
      if (this.customDate != var1) {
         String var2 = LocaleController.formatDateChat((long)var1);
         CharSequence var3 = this.customText;
         if (var3 == null || !TextUtils.equals(var2, var3)) {
            this.customDate = var1;
            this.customText = var2;
            if (this.getMeasuredWidth() != 0) {
               this.createLayout(this.customText, this.getMeasuredWidth());
               this.invalidate();
            }

            if (!this.wasLayout) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$W3vgGEA8PP4iykiyHwC5GJEFtAc(this));
            } else {
               this.buildLayout();
            }

         }
      }
   }

   public void setDelegate(ChatActionCell.ChatActionCellDelegate var1) {
      this.delegate = var1;
   }

   public void setMessageObject(MessageObject var1) {
      if (this.currentMessageObject != var1 || !this.hasReplyMessage && var1.replyMessageObject != null) {
         this.currentMessageObject = var1;
         boolean var2;
         if (var1.replyMessageObject != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.hasReplyMessage = var2;
         this.previousWidth = 0;
         if (this.currentMessageObject.type == 11) {
            TLRPC.Peer var3 = var1.messageOwner.to_id;
            int var4;
            if (var3 != null) {
               var4 = var3.chat_id;
               if (var4 == 0) {
                  var4 = var3.channel_id;
                  if (var4 == 0) {
                     var4 = var3.user_id;
                     if (var4 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        var4 = var1.messageOwner.from_id;
                     }
                  }
               }
            } else {
               var4 = 0;
            }

            this.avatarDrawable.setInfo(var4, (String)null, (String)null, false);
            var1 = this.currentMessageObject;
            if (var1.messageOwner.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
               this.imageReceiver.setImage((ImageLocation)null, (String)null, this.avatarDrawable, (String)null, var1, 0);
            } else {
               TLRPC.PhotoSize var5 = FileLoader.getClosestPhotoSizeWithSize(var1.photoThumbs, AndroidUtilities.dp(64.0F));
               if (var5 != null) {
                  this.imageReceiver.setImage(ImageLocation.getForObject(var5, this.currentMessageObject.photoThumbsObject), "50_50", this.avatarDrawable, (String)null, this.currentMessageObject, 0);
               } else {
                  this.imageReceiver.setImageBitmap((Drawable)this.avatarDrawable);
               }
            }

            this.imageReceiver.setVisible(PhotoViewer.isShowingImage(this.currentMessageObject) ^ true, false);
         } else {
            this.imageReceiver.setImageBitmap((Bitmap)null);
         }

         this.requestLayout();
      }
   }

   public interface ChatActionCellDelegate {
      void didClickImage(ChatActionCell var1);

      void didLongPress(ChatActionCell var1, float var2, float var3);

      void didPressBotButton(MessageObject var1, TLRPC.KeyboardButton var2);

      void didPressReplyMessage(ChatActionCell var1, int var2);

      void needOpenUserProfile(int var1);
   }
}

package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.LinkPath;

public class SharedLinkCell extends FrameLayout {
   private CheckBox2 checkBox;
   private boolean checkingForLongPress = false;
   private SharedLinkCell.SharedLinkCellDelegate delegate;
   private int description2Y = AndroidUtilities.dp(30.0F);
   private StaticLayout descriptionLayout;
   private StaticLayout descriptionLayout2;
   private TextPaint descriptionTextPaint;
   private int descriptionY = AndroidUtilities.dp(30.0F);
   private boolean drawLinkImageView;
   private LetterDrawable letterDrawable;
   private ImageReceiver linkImageView;
   private ArrayList linkLayout = new ArrayList();
   private boolean linkPreviewPressed;
   private int linkY;
   ArrayList links = new ArrayList();
   private MessageObject message;
   private boolean needDivider;
   private SharedLinkCell.CheckForLongPress pendingCheckForLongPress = null;
   private SharedLinkCell.CheckForTap pendingCheckForTap = null;
   private int pressCount = 0;
   private int pressedLink;
   private StaticLayout titleLayout;
   private TextPaint titleTextPaint;
   private int titleY = AndroidUtilities.dp(10.0F);
   private LinkPath urlPath;

   public SharedLinkCell(Context var1) {
      super(var1);
      this.setFocusable(true);
      this.urlPath = new LinkPath();
      this.urlPath.setUseRoundRect(true);
      this.titleTextPaint = new TextPaint(1);
      this.titleTextPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.titleTextPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.descriptionTextPaint = new TextPaint(1);
      this.titleTextPaint.setTextSize((float)AndroidUtilities.dp(14.0F));
      this.descriptionTextPaint.setTextSize((float)AndroidUtilities.dp(14.0F));
      this.setWillNotDraw(false);
      this.linkImageView = new ImageReceiver(this);
      this.linkImageView.setRoundRadius(AndroidUtilities.dp(4.0F));
      this.letterDrawable = new LetterDrawable();
      this.checkBox = new CheckBox2(var1);
      this.checkBox.setVisibility(4);
      this.checkBox.setColor((String)null, "windowBackgroundWhite", "checkboxCheck");
      this.checkBox.setSize(21);
      this.checkBox.setDrawUnchecked(false);
      this.checkBox.setDrawBackgroundAsArc(2);
      CheckBox2 var5 = this.checkBox;
      byte var2;
      if (LocaleController.isRTL) {
         var2 = 5;
      } else {
         var2 = 3;
      }

      float var3;
      if (LocaleController.isRTL) {
         var3 = 0.0F;
      } else {
         var3 = 44.0F;
      }

      float var4;
      if (LocaleController.isRTL) {
         var4 = 44.0F;
      } else {
         var4 = 0.0F;
      }

      this.addView(var5, LayoutHelper.createFrame(24, 24.0F, var2 | 48, var3, 44.0F, var4, 0.0F));
   }

   // $FF: synthetic method
   static int access$104(SharedLinkCell var0) {
      int var1 = var0.pressCount + 1;
      var0.pressCount = var1;
      return var1;
   }

   protected void cancelCheckLongPress() {
      this.checkingForLongPress = false;
      SharedLinkCell.CheckForLongPress var1 = this.pendingCheckForLongPress;
      if (var1 != null) {
         this.removeCallbacks(var1);
      }

      SharedLinkCell.CheckForTap var2 = this.pendingCheckForTap;
      if (var2 != null) {
         this.removeCallbacks(var2);
      }

   }

   public String getLink(int var1) {
      return var1 >= 0 && var1 < this.links.size() ? (String)this.links.get(var1) : null;
   }

   public MessageObject getMessage() {
      return this.message;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.drawLinkImageView) {
         this.linkImageView.onAttachedToWindow();
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (this.drawLinkImageView) {
         this.linkImageView.onDetachedFromWindow();
      }

   }

   protected void onDraw(Canvas var1) {
      float var2;
      if (this.titleLayout != null) {
         var1.save();
         if (LocaleController.isRTL) {
            var2 = 8.0F;
         } else {
            var2 = (float)AndroidUtilities.leftBaseline;
         }

         var1.translate((float)AndroidUtilities.dp(var2), (float)this.titleY);
         this.titleLayout.draw(var1);
         var1.restore();
      }

      if (this.descriptionLayout != null) {
         this.descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         var1.save();
         if (LocaleController.isRTL) {
            var2 = 8.0F;
         } else {
            var2 = (float)AndroidUtilities.leftBaseline;
         }

         var1.translate((float)AndroidUtilities.dp(var2), (float)this.descriptionY);
         this.descriptionLayout.draw(var1);
         var1.restore();
      }

      if (this.descriptionLayout2 != null) {
         this.descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         var1.save();
         if (LocaleController.isRTL) {
            var2 = 8.0F;
         } else {
            var2 = (float)AndroidUtilities.leftBaseline;
         }

         var1.translate((float)AndroidUtilities.dp(var2), (float)this.description2Y);
         this.descriptionLayout2.draw(var1);
         var1.restore();
      }

      if (!this.linkLayout.isEmpty()) {
         this.descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteLinkText"));
         int var3 = 0;

         int var6;
         for(int var4 = 0; var3 < this.linkLayout.size(); var4 = var6) {
            StaticLayout var5 = (StaticLayout)this.linkLayout.get(var3);
            var6 = var4;
            if (var5.getLineCount() > 0) {
               var1.save();
               if (LocaleController.isRTL) {
                  var2 = 8.0F;
               } else {
                  var2 = (float)AndroidUtilities.leftBaseline;
               }

               var1.translate((float)AndroidUtilities.dp(var2), (float)(this.linkY + var4));
               if (this.pressedLink == var3) {
                  var1.drawPath(this.urlPath, Theme.linkSelectionPaint);
               }

               var5.draw(var1);
               var1.restore();
               var6 = var4 + var5.getLineBottom(var5.getLineCount() - 1);
            }

            ++var3;
         }
      }

      this.letterDrawable.draw(var1);
      if (this.drawLinkImageView) {
         this.linkImageView.draw(var1);
      }

      if (this.needDivider) {
         if (LocaleController.isRTL) {
            var1.drawLine(0.0F, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline)), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
         } else {
            var1.drawLine((float)AndroidUtilities.dp((float)AndroidUtilities.leftBaseline), (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
         }
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      StringBuilder var2 = new StringBuilder();
      StaticLayout var3 = this.titleLayout;
      if (var3 != null) {
         var2.append(var3.getText());
      }

      if (this.descriptionLayout != null) {
         var2.append(", ");
         var2.append(this.descriptionLayout.getText());
      }

      if (this.descriptionLayout2 != null) {
         var2.append(", ");
         var2.append(this.descriptionLayout2.getText());
      }

      if (this.checkBox.isChecked()) {
         var1.setChecked(true);
         var1.setCheckable(true);
      }

   }

   @SuppressLint({"DrawAllocation"})
   protected void onMeasure(int var1, int var2) {
      byte var3;
      int var4;
      String var8;
      boolean var62;
      String var63;
      String var64;
      label548: {
         var3 = 0;
         this.drawLinkImageView = false;
         this.descriptionLayout = null;
         this.titleLayout = null;
         this.descriptionLayout2 = null;
         this.linkLayout.clear();
         this.links.clear();
         var4 = MeasureSpec.getSize(var1) - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline) - AndroidUtilities.dp(8.0F);
         MessageObject var5 = this.message;
         TLRPC.MessageMedia var6 = var5.messageOwner.media;
         if (var6 instanceof TLRPC.TL_messageMediaWebPage) {
            TLRPC.WebPage var7 = var6.webpage;
            if (var7 instanceof TLRPC.TL_webPage) {
               if (var5.photoThumbs == null && var7.photo != null) {
                  var5.generateThumbs(true);
               }

               if (var7.photo != null && this.message.photoThumbs != null) {
                  var62 = true;
               } else {
                  var62 = false;
               }

               var63 = var7.title;
               var64 = var63;
               if (var63 == null) {
                  var64 = var7.site_name;
               }

               var63 = var7.description;
               var8 = var7.url;
               break label548;
            }
         }

         var8 = null;
         var64 = var8;
         var63 = var8;
         var62 = false;
      }

      MessageObject var65 = this.message;
      String var9;
      int var10;
      int var14;
      Exception var10000;
      boolean var10001;
      String var66;
      Exception var72;
      if (var65 != null && !var65.messageOwner.entities.isEmpty()) {
         var9 = null;
         var66 = var64;
         var10 = 0;
         var64 = var63;

         for(var63 = var9; var10 < this.message.messageOwner.entities.size(); var64 = var9) {
            TLRPC.MessageEntity var11 = (TLRPC.MessageEntity)this.message.messageOwner.entities.get(var10);
            String var12 = var63;
            String var13 = var66;
            var9 = var64;
            if (var11.length > 0) {
               var14 = var11.offset;
               var12 = var63;
               var13 = var66;
               var9 = var64;
               if (var14 >= 0) {
                  if (var14 >= this.message.messageOwner.message.length()) {
                     var12 = var63;
                     var13 = var66;
                     var9 = var64;
                  } else {
                     if (var11.offset + var11.length > this.message.messageOwner.message.length()) {
                        var11.length = this.message.messageOwner.message.length() - var11.offset;
                     }

                     var13 = var63;
                     if (var10 == 0) {
                        var13 = var63;
                        if (var8 != null) {
                           label523: {
                              if (var11.offset == 0) {
                                 var13 = var63;
                                 if (var11.length == this.message.messageOwner.message.length()) {
                                    break label523;
                                 }
                              }

                              if (this.message.messageOwner.entities.size() == 1) {
                                 var13 = var63;
                                 if (var64 == null) {
                                    var13 = this.message.messageOwner.message;
                                 }
                              } else {
                                 var13 = this.message.messageOwner.message;
                              }
                           }
                        }
                     }

                     var63 = var66;
                     String var15 = var64;

                     label517: {
                        label516: {
                           label553: {
                              String var17;
                              label554: {
                                 String var16;
                                 StringBuilder var68;
                                 label555: {
                                    try {
                                       if (var11 instanceof TLRPC.TL_messageEntityTextUrl) {
                                          break label555;
                                       }
                                    } catch (Exception var61) {
                                       var10000 = var61;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var63 = var66;
                                    var15 = var64;

                                    try {
                                       if (var11 instanceof TLRPC.TL_messageEntityUrl) {
                                          break label555;
                                       }
                                    } catch (Exception var60) {
                                       var10000 = var60;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var63 = var66;
                                    var15 = var64;

                                    label502: {
                                       label556: {
                                          try {
                                             if (!(var11 instanceof TLRPC.TL_messageEntityEmail)) {
                                                break label556;
                                             }
                                          } catch (Exception var59) {
                                             var10000 = var59;
                                             var10001 = false;
                                             break label553;
                                          }

                                          if (var66 == null) {
                                             break label502;
                                          }

                                          var63 = var66;
                                          var15 = var64;

                                          try {
                                             if (var66.length() == 0) {
                                                break label502;
                                             }
                                          } catch (Exception var58) {
                                             var10000 = var58;
                                             var10001 = false;
                                             break label553;
                                          }
                                       }

                                       var17 = null;
                                       var9 = var64;
                                       break label554;
                                    }

                                    var63 = var66;
                                    var15 = var64;

                                    try {
                                       var68 = new StringBuilder;
                                    } catch (Exception var50) {
                                       var10000 = var50;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var63 = var66;
                                    var15 = var64;

                                    try {
                                       var68.<init>();
                                    } catch (Exception var49) {
                                       var10000 = var49;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var63 = var66;
                                    var15 = var64;

                                    try {
                                       var68.append("mailto:");
                                    } catch (Exception var48) {
                                       var10000 = var48;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var63 = var66;
                                    var15 = var64;

                                    try {
                                       var68.append(this.message.messageOwner.message.substring(var11.offset, var11.offset + var11.length));
                                    } catch (Exception var47) {
                                       var10000 = var47;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var63 = var66;
                                    var15 = var64;

                                    try {
                                       var16 = var68.toString();
                                    } catch (Exception var46) {
                                       var10000 = var46;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var63 = var66;
                                    var15 = var64;

                                    try {
                                       var12 = this.message.messageOwner.message.substring(var11.offset, var11.offset + var11.length);
                                    } catch (Exception var45) {
                                       var10000 = var45;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var63 = var12;
                                    var15 = var64;

                                    label557: {
                                       try {
                                          if (var11.offset != 0) {
                                             break label557;
                                          }
                                       } catch (Exception var57) {
                                          var10000 = var57;
                                          var10001 = false;
                                          break label553;
                                       }

                                       var17 = var16;
                                       var66 = var12;
                                       var9 = var64;
                                       var63 = var12;
                                       var15 = var64;

                                       try {
                                          if (var11.length == this.message.messageOwner.message.length()) {
                                             break label554;
                                          }
                                       } catch (Exception var56) {
                                          var10000 = var56;
                                          var10001 = false;
                                          break label553;
                                       }
                                    }

                                    var63 = var12;
                                    var15 = var64;

                                    try {
                                       var9 = this.message.messageOwner.message;
                                    } catch (Exception var44) {
                                       var10000 = var44;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var17 = var16;
                                    var66 = var12;
                                    break label554;
                                 }

                                 var63 = var66;
                                 var15 = var64;

                                 label558: {
                                    label559: {
                                       try {
                                          if (!(var11 instanceof TLRPC.TL_messageEntityUrl)) {
                                             break label559;
                                          }
                                       } catch (Exception var55) {
                                          var10000 = var55;
                                          var10001 = false;
                                          break label553;
                                       }

                                       var63 = var66;
                                       var15 = var64;

                                       try {
                                          var12 = this.message.messageOwner.message.substring(var11.offset, var11.offset + var11.length);
                                          break label558;
                                       } catch (Exception var43) {
                                          var10000 = var43;
                                          var10001 = false;
                                          break label553;
                                       }
                                    }

                                    var63 = var66;
                                    var15 = var64;

                                    try {
                                       var12 = var11.url;
                                    } catch (Exception var42) {
                                       var10000 = var42;
                                       var10001 = false;
                                       break label553;
                                    }
                                 }

                                 if (var66 != null) {
                                    var63 = var66;
                                    var15 = var64;

                                    try {
                                       var14 = var66.length();
                                    } catch (Exception var41) {
                                       var10000 = var41;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var17 = var12;
                                    var9 = var64;
                                    if (var14 != 0) {
                                       break label554;
                                    }
                                 }

                                 try {
                                    var66 = Uri.parse(var12).getHost();
                                 } catch (Exception var25) {
                                    var72 = var25;
                                    var66 = var12;
                                    break label516;
                                 }

                                 if (var66 == null) {
                                    var66 = var12;
                                 }

                                 var16 = var66;
                                 if (var66 != null) {
                                    var63 = var66;
                                    var15 = var64;

                                    try {
                                       var14 = var66.lastIndexOf(46);
                                    } catch (Exception var40) {
                                       var10000 = var40;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var16 = var66;
                                    if (var14 >= 0) {
                                       var63 = var66;
                                       var15 = var64;

                                       try {
                                          var9 = var66.substring(0, var14);
                                       } catch (Exception var39) {
                                          var10000 = var39;
                                          var10001 = false;
                                          break label553;
                                       }

                                       var63 = var9;
                                       var15 = var64;

                                       try {
                                          var14 = var9.lastIndexOf(46);
                                       } catch (Exception var38) {
                                          var10000 = var38;
                                          var10001 = false;
                                          break label553;
                                       }

                                       var66 = var9;
                                       if (var14 >= 0) {
                                          var63 = var9;
                                          var15 = var64;

                                          try {
                                             var66 = var9.substring(var14 + 1);
                                          } catch (Exception var37) {
                                             var10000 = var37;
                                             var10001 = false;
                                             break label553;
                                          }
                                       }

                                       var63 = var66;
                                       var15 = var64;

                                       try {
                                          var68 = new StringBuilder;
                                       } catch (Exception var36) {
                                          var10000 = var36;
                                          var10001 = false;
                                          break label553;
                                       }

                                       var63 = var66;
                                       var15 = var64;

                                       try {
                                          var68.<init>();
                                       } catch (Exception var35) {
                                          var10000 = var35;
                                          var10001 = false;
                                          break label553;
                                       }

                                       var63 = var66;
                                       var15 = var64;

                                       try {
                                          var68.append(var66.substring(0, 1).toUpperCase());
                                       } catch (Exception var34) {
                                          var10000 = var34;
                                          var10001 = false;
                                          break label553;
                                       }

                                       var63 = var66;
                                       var15 = var64;

                                       try {
                                          var68.append(var66.substring(1));
                                       } catch (Exception var33) {
                                          var10000 = var33;
                                          var10001 = false;
                                          break label553;
                                       }

                                       var63 = var66;
                                       var15 = var64;

                                       try {
                                          var16 = var68.toString();
                                       } catch (Exception var32) {
                                          var10000 = var32;
                                          var10001 = false;
                                          break label553;
                                       }
                                    }
                                 }

                                 var63 = var16;
                                 var15 = var64;

                                 label563: {
                                    try {
                                       if (var11.offset != 0) {
                                          break label563;
                                       }
                                    } catch (Exception var54) {
                                       var10000 = var54;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var17 = var12;
                                    var66 = var16;
                                    var9 = var64;
                                    var63 = var16;
                                    var15 = var64;

                                    try {
                                       if (var11.length == this.message.messageOwner.message.length()) {
                                          break label554;
                                       }
                                    } catch (Exception var53) {
                                       var10000 = var53;
                                       var10001 = false;
                                       break label553;
                                    }
                                 }

                                 var63 = var16;
                                 var15 = var64;

                                 try {
                                    var9 = this.message.messageOwner.message;
                                 } catch (Exception var31) {
                                    var10000 = var31;
                                    var10001 = false;
                                    break label553;
                                 }

                                 var66 = var16;
                                 var17 = var12;
                              }

                              var64 = var66;
                              var63 = var9;
                              if (var17 == null) {
                                 break label517;
                              }

                              var63 = var66;
                              var15 = var9;

                              label564: {
                                 label565: {
                                    try {
                                       if (var17.toLowerCase().indexOf("http") == 0) {
                                          break label565;
                                       }
                                    } catch (Exception var52) {
                                       var10000 = var52;
                                       var10001 = false;
                                       break label553;
                                    }

                                    var63 = var66;
                                    var15 = var9;

                                    try {
                                       if (var17.toLowerCase().indexOf("mailto") != 0) {
                                          break label564;
                                       }
                                    } catch (Exception var51) {
                                       var10000 = var51;
                                       var10001 = false;
                                       break label553;
                                    }
                                 }

                                 var63 = var66;
                                 var15 = var9;

                                 try {
                                    this.links.add(var17);
                                 } catch (Exception var24) {
                                    var10000 = var24;
                                    var10001 = false;
                                    break label553;
                                 }

                                 var64 = var66;
                                 var63 = var9;
                                 break label517;
                              }

                              var63 = var66;
                              var15 = var9;

                              ArrayList var75;
                              try {
                                 var75 = this.links;
                              } catch (Exception var30) {
                                 var10000 = var30;
                                 var10001 = false;
                                 break label553;
                              }

                              var63 = var66;
                              var15 = var9;

                              StringBuilder var67;
                              try {
                                 var67 = new StringBuilder;
                              } catch (Exception var29) {
                                 var10000 = var29;
                                 var10001 = false;
                                 break label553;
                              }

                              var63 = var66;
                              var15 = var9;

                              try {
                                 var67.<init>();
                              } catch (Exception var28) {
                                 var10000 = var28;
                                 var10001 = false;
                                 break label553;
                              }

                              var63 = var66;
                              var15 = var9;

                              try {
                                 var67.append("http://");
                              } catch (Exception var27) {
                                 var10000 = var27;
                                 var10001 = false;
                                 break label553;
                              }

                              var63 = var66;
                              var15 = var9;

                              try {
                                 var67.append(var17);
                              } catch (Exception var26) {
                                 var10000 = var26;
                                 var10001 = false;
                                 break label553;
                              }

                              var63 = var66;
                              var15 = var9;

                              try {
                                 var75.add(var67.toString());
                              } catch (Exception var23) {
                                 var10000 = var23;
                                 var10001 = false;
                                 break label553;
                              }

                              var64 = var66;
                              var63 = var9;
                              break label517;
                           }

                           Exception var71 = var10000;
                           var64 = var15;
                           var66 = var63;
                           var72 = var71;
                        }

                        FileLog.e((Throwable)var72);
                        var63 = var64;
                        var64 = var66;
                     }

                     var12 = var13;
                     var9 = var63;
                     var13 = var64;
                  }
               }
            }

            ++var10;
            var63 = var12;
            var66 = var13;
         }
      } else {
         var66 = var64;
         var64 = var63;
         var63 = null;
      }

      if (var8 != null && this.links.isEmpty()) {
         this.links.add(var8);
      }

      if (var66 != null) {
         try {
            this.titleLayout = ChatMessageCell.generateStaticLayout(var66, this.titleTextPaint, var4, var4, 0, 3);
            if (this.titleLayout.getLineCount() > 0) {
               this.descriptionY = this.titleY + this.titleLayout.getLineBottom(this.titleLayout.getLineCount() - 1) + AndroidUtilities.dp(4.0F);
            }
         } catch (Exception var20) {
            FileLog.e((Throwable)var20);
         }

         this.letterDrawable.setTitle(var66);
      }

      this.description2Y = this.descriptionY;
      StaticLayout var73 = this.titleLayout;
      if (var73 != null) {
         var10 = var73.getLineCount();
      } else {
         var10 = 0;
      }

      var10 = Math.max(1, 4 - var10);
      if (var64 != null) {
         try {
            this.descriptionLayout = ChatMessageCell.generateStaticLayout(var64, this.descriptionTextPaint, var4, var4, 0, var10);
            if (this.descriptionLayout.getLineCount() > 0) {
               this.description2Y = this.descriptionY + this.descriptionLayout.getLineBottom(this.descriptionLayout.getLineCount() - 1) + AndroidUtilities.dp(5.0F);
            }
         } catch (Exception var19) {
            FileLog.e((Throwable)var19);
         }
      }

      if (var63 != null) {
         try {
            this.descriptionLayout2 = ChatMessageCell.generateStaticLayout(var63, this.descriptionTextPaint, var4, var4, 0, var10);
            if (this.descriptionLayout != null) {
               this.description2Y += AndroidUtilities.dp(10.0F);
            }
         } catch (Exception var18) {
            FileLog.e((Throwable)var18);
         }
      }

      StaticLayout var76;
      if (!this.links.isEmpty()) {
         for(var10 = 0; var10 < this.links.size(); ++var10) {
            label333: {
               try {
                  var63 = (String)this.links.get(var10);
                  var14 = (int)Math.ceil((double)this.descriptionTextPaint.measureText(var63));
                  CharSequence var69 = TextUtils.ellipsize(var63.replace('\n', ' '), this.descriptionTextPaint, (float)Math.min(var14, var4), TruncateAt.MIDDLE);
                  var76 = new StaticLayout(var69, this.descriptionTextPaint, var4, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                  this.linkY = this.description2Y;
                  if (this.descriptionLayout2 != null && this.descriptionLayout2.getLineCount() != 0) {
                     this.linkY += this.descriptionLayout2.getLineBottom(this.descriptionLayout2.getLineCount() - 1) + AndroidUtilities.dp(5.0F);
                  }
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label333;
               }

               try {
                  this.linkLayout.add(var76);
                  continue;
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
               }
            }

            var72 = var10000;
            FileLog.e((Throwable)var72);
         }
      }

      var4 = AndroidUtilities.dp(52.0F);
      if (LocaleController.isRTL) {
         var10 = MeasureSpec.getSize(var1) - AndroidUtilities.dp(10.0F) - var4;
      } else {
         var10 = AndroidUtilities.dp(10.0F);
      }

      this.letterDrawable.setBounds(var10, AndroidUtilities.dp(11.0F), var10 + var4, AndroidUtilities.dp(63.0F));
      if (var62) {
         TLRPC.PhotoSize var74 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, var4, true);
         TLRPC.PhotoSize var70 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, 80);
         TLRPC.PhotoSize var77 = var70;
         if (var70 == var74) {
            var77 = null;
         }

         var74.size = -1;
         if (var77 != null) {
            var77.size = -1;
         }

         this.linkImageView.setImageCoords(var10, AndroidUtilities.dp(11.0F), var4, var4);
         FileLoader.getAttachFileName(var74);
         var64 = String.format(Locale.US, "%d_%d", var4, var4);
         var9 = String.format(Locale.US, "%d_%d_b", var4, var4);
         this.linkImageView.setImage(ImageLocation.getForObject(var74, this.message.photoThumbsObject), var64, ImageLocation.getForObject(var77, this.message.photoThumbsObject), var9, 0, (String)null, this.message, 0);
         this.drawLinkImageView = true;
      }

      var76 = this.titleLayout;
      if (var76 != null && var76.getLineCount() != 0) {
         var76 = this.titleLayout;
         var2 = var76.getLineBottom(var76.getLineCount() - 1) + AndroidUtilities.dp(4.0F) + 0;
      } else {
         var2 = 0;
      }

      var76 = this.descriptionLayout;
      var10 = var2;
      if (var76 != null) {
         var10 = var2;
         if (var76.getLineCount() != 0) {
            var76 = this.descriptionLayout;
            var10 = var2 + var76.getLineBottom(var76.getLineCount() - 1) + AndroidUtilities.dp(5.0F);
         }
      }

      var76 = this.descriptionLayout2;
      var2 = var10;
      var4 = var3;
      if (var76 != null) {
         var2 = var10;
         var4 = var3;
         if (var76.getLineCount() != 0) {
            var76 = this.descriptionLayout2;
            var10 += var76.getLineBottom(var76.getLineCount() - 1) + AndroidUtilities.dp(5.0F);
            var2 = var10;
            var4 = var3;
            if (this.descriptionLayout != null) {
               var2 = var10 + AndroidUtilities.dp(10.0F);
               var4 = var3;
            }
         }
      }

      while(var4 < this.linkLayout.size()) {
         var76 = (StaticLayout)this.linkLayout.get(var4);
         var10 = var2;
         if (var76.getLineCount() > 0) {
            var10 = var2 + var76.getLineBottom(var76.getLineCount() - 1);
         }

         ++var4;
         var2 = var10;
      }

      this.checkBox.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0F), 1073741824));
      this.setMeasuredDimension(MeasureSpec.getSize(var1), Math.max(AndroidUtilities.dp(76.0F), var2 + AndroidUtilities.dp(17.0F)) + this.needDivider);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var3;
      boolean var8;
      label135: {
         label134: {
            MessageObject var2 = this.message;
            var3 = true;
            if (var2 != null && !this.linkLayout.isEmpty()) {
               SharedLinkCell.SharedLinkCellDelegate var17 = this.delegate;
               if (var17 != null && var17.canPerformActions()) {
                  if (var1.getAction() == 0 || this.linkPreviewPressed && var1.getAction() == 1) {
                     int var4 = (int)var1.getX();
                     int var5 = (int)var1.getY();
                     int var6 = 0;
                     int var7 = 0;

                     boolean var21;
                     boolean var22;
                     label117: {
                        while(true) {
                           int var23;
                           label142: {
                              if (var6 < this.linkLayout.size()) {
                                 label139: {
                                    StaticLayout var18 = (StaticLayout)this.linkLayout.get(var6);
                                    var23 = var7;
                                    if (var18.getLineCount() <= 0) {
                                       break label142;
                                    }

                                    var23 = var18.getLineBottom(var18.getLineCount() - 1);
                                    float var9;
                                    if (LocaleController.isRTL) {
                                       var9 = 8.0F;
                                    } else {
                                       var9 = (float)AndroidUtilities.leftBaseline;
                                    }

                                    int var10 = AndroidUtilities.dp(var9);
                                    var9 = (float)var4;
                                    float var11 = (float)var10;
                                    if (var9 >= var18.getLineLeft(0) + var11 && var9 <= var11 + var18.getLineWidth(0)) {
                                       var10 = this.linkY;
                                       if (var5 >= var10 + var7 && var5 <= var10 + var7 + var23) {
                                          if (var1.getAction() == 0) {
                                             this.resetPressedLink();
                                             this.pressedLink = var6;
                                             this.linkPreviewPressed = true;
                                             this.startCheckLongPress();

                                             try {
                                                this.urlPath.setCurrentLayout(var18, 0, 0.0F);
                                                var18.getSelectionPath(0, var18.getText().length(), this.urlPath);
                                             } catch (Exception var13) {
                                                FileLog.e((Throwable)var13);
                                             }
                                             break;
                                          }

                                          if (this.linkPreviewPressed) {
                                             label94: {
                                                Exception var10000;
                                                label141: {
                                                   boolean var10001;
                                                   TLRPC.WebPage var19;
                                                   label92: {
                                                      try {
                                                         if (this.pressedLink == 0 && this.message.messageOwner.media != null) {
                                                            var19 = this.message.messageOwner.media.webpage;
                                                            break label92;
                                                         }
                                                      } catch (Exception var16) {
                                                         var10000 = var16;
                                                         var10001 = false;
                                                         break label141;
                                                      }

                                                      var19 = null;
                                                   }

                                                   if (var19 != null) {
                                                      try {
                                                         if (var19.embed_url != null && var19.embed_url.length() != 0) {
                                                            this.delegate.needOpenWebView(var19);
                                                            break label94;
                                                         }
                                                      } catch (Exception var15) {
                                                         var10000 = var15;
                                                         var10001 = false;
                                                         break label141;
                                                      }
                                                   }

                                                   try {
                                                      Browser.openUrl(this.getContext(), (String)this.links.get(this.pressedLink));
                                                      break label94;
                                                   } catch (Exception var14) {
                                                      var10000 = var14;
                                                      var10001 = false;
                                                   }
                                                }

                                                Exception var20 = var10000;
                                                FileLog.e((Throwable)var20);
                                             }

                                             this.resetPressedLink();
                                             break;
                                          }

                                          var22 = true;
                                          break label139;
                                       }
                                    }

                                    var23 += var7;
                                    break label142;
                                 }
                              } else {
                                 var22 = false;
                              }

                              var21 = false;
                              break label117;
                           }

                           ++var6;
                           var7 = var23;
                        }

                        var22 = true;
                        var21 = true;
                     }

                     var8 = var21;
                     if (!var22) {
                        this.resetPressedLink();
                        var8 = var21;
                     }
                     break label135;
                  }

                  if (var1.getAction() == 3) {
                     this.resetPressedLink();
                  }
                  break label134;
               }
            }

            this.resetPressedLink();
         }

         var8 = false;
      }

      boolean var12 = var3;
      if (!var8) {
         if (super.onTouchEvent(var1)) {
            var12 = var3;
         } else {
            var12 = false;
         }
      }

      return var12;
   }

   protected void resetPressedLink() {
      this.pressedLink = -1;
      this.linkPreviewPressed = false;
      this.cancelCheckLongPress();
      this.invalidate();
   }

   public void setChecked(boolean var1, boolean var2) {
      if (this.checkBox.getVisibility() != 0) {
         this.checkBox.setVisibility(0);
      }

      this.checkBox.setChecked(var1, var2);
   }

   public void setDelegate(SharedLinkCell.SharedLinkCellDelegate var1) {
      this.delegate = var1;
   }

   public void setLink(MessageObject var1, boolean var2) {
      this.needDivider = var2;
      this.resetPressedLink();
      this.message = var1;
      this.requestLayout();
   }

   protected void startCheckLongPress() {
      if (!this.checkingForLongPress) {
         this.checkingForLongPress = true;
         if (this.pendingCheckForTap == null) {
            this.pendingCheckForTap = new SharedLinkCell.CheckForTap();
         }

         this.postDelayed(this.pendingCheckForTap, (long)ViewConfiguration.getTapTimeout());
      }
   }

   class CheckForLongPress implements Runnable {
      public int currentPressCount;

      public void run() {
         if (SharedLinkCell.this.checkingForLongPress && SharedLinkCell.this.getParent() != null && this.currentPressCount == SharedLinkCell.this.pressCount) {
            SharedLinkCell.this.checkingForLongPress = false;
            SharedLinkCell.this.performHapticFeedback(0);
            if (SharedLinkCell.this.pressedLink >= 0) {
               SharedLinkCell.SharedLinkCellDelegate var1 = SharedLinkCell.this.delegate;
               SharedLinkCell var2 = SharedLinkCell.this;
               var1.onLinkLongPress((String)var2.links.get(var2.pressedLink));
            }

            MotionEvent var3 = MotionEvent.obtain(0L, 0L, 3, 0.0F, 0.0F, 0);
            SharedLinkCell.this.onTouchEvent(var3);
            var3.recycle();
         }

      }
   }

   private final class CheckForTap implements Runnable {
      private CheckForTap() {
      }

      // $FF: synthetic method
      CheckForTap(Object var2) {
         this();
      }

      public void run() {
         SharedLinkCell var1;
         if (SharedLinkCell.this.pendingCheckForLongPress == null) {
            var1 = SharedLinkCell.this;
            var1.pendingCheckForLongPress = var1.new CheckForLongPress();
         }

         SharedLinkCell.this.pendingCheckForLongPress.currentPressCount = SharedLinkCell.access$104(SharedLinkCell.this);
         var1 = SharedLinkCell.this;
         var1.postDelayed(var1.pendingCheckForLongPress, (long)(ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout()));
      }
   }

   public interface SharedLinkCellDelegate {
      boolean canPerformActions();

      void needOpenWebView(TLRPC.WebPage var1);

      void onLinkLongPress(String var1);
   }
}

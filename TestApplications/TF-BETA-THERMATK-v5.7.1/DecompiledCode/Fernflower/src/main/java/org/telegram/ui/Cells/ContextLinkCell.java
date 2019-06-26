package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateInterpolator;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.RadialProgress2;

public class ContextLinkCell extends View implements DownloadController.FileDownloadProgressListener {
   private static final int DOCUMENT_ATTACH_TYPE_AUDIO = 3;
   private static final int DOCUMENT_ATTACH_TYPE_DOCUMENT = 1;
   private static final int DOCUMENT_ATTACH_TYPE_GEO = 8;
   private static final int DOCUMENT_ATTACH_TYPE_GIF = 2;
   private static final int DOCUMENT_ATTACH_TYPE_MUSIC = 5;
   private static final int DOCUMENT_ATTACH_TYPE_NONE = 0;
   private static final int DOCUMENT_ATTACH_TYPE_PHOTO = 7;
   private static final int DOCUMENT_ATTACH_TYPE_STICKER = 6;
   private static final int DOCUMENT_ATTACH_TYPE_VIDEO = 4;
   private static AccelerateInterpolator interpolator = new AccelerateInterpolator(0.5F);
   private int TAG;
   private boolean buttonPressed;
   private int buttonState;
   private boolean canPreviewGif;
   private int currentAccount;
   private MessageObject currentMessageObject;
   private TLRPC.PhotoSize currentPhotoObject;
   private ContextLinkCell.ContextLinkCellDelegate delegate;
   private StaticLayout descriptionLayout;
   private int descriptionY;
   private TLRPC.Document documentAttach;
   private int documentAttachType;
   private boolean drawLinkImageView;
   private TLRPC.BotInlineResult inlineResult;
   private long lastUpdateTime;
   private LetterDrawable letterDrawable;
   private ImageReceiver linkImageView;
   private StaticLayout linkLayout;
   private int linkY;
   private boolean mediaWebpage;
   private boolean needDivider;
   private boolean needShadow;
   private Object parentObject;
   private TLRPC.Photo photoAttach;
   private RadialProgress2 radialProgress;
   private float scale;
   private boolean scaled;
   private StaticLayout titleLayout;
   private int titleY;

   public ContextLinkCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.titleY = AndroidUtilities.dp(7.0F);
      this.descriptionY = AndroidUtilities.dp(27.0F);
      this.linkImageView = new ImageReceiver(this);
      this.linkImageView.setUseSharedAnimationQueue(true);
      this.letterDrawable = new LetterDrawable();
      this.radialProgress = new RadialProgress2(this);
      this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
      this.setFocusable(true);
   }

   private void didPressedButton() {
      int var1 = this.documentAttachType;
      if (var1 == 3 || var1 == 5) {
         var1 = this.buttonState;
         if (var1 == 0) {
            if (MediaController.getInstance().playMessage(this.currentMessageObject)) {
               this.buttonState = 1;
               this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
               this.invalidate();
            }
         } else if (var1 == 1) {
            if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
               this.buttonState = 0;
               this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
               this.invalidate();
            }
         } else if (var1 == 2) {
            this.radialProgress.setProgress(0.0F, false);
            if (this.documentAttach != null) {
               FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.inlineResult, 1, 0);
            } else if (this.inlineResult.content instanceof TLRPC.TL_webDocument) {
               FileLoader.getInstance(this.currentAccount).loadFile(WebFile.createWithWebDocument(this.inlineResult.content), 1, 1);
            }

            this.buttonState = 4;
            this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
            this.invalidate();
         } else if (var1 == 4) {
            if (this.documentAttach != null) {
               FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
            } else if (this.inlineResult.content instanceof TLRPC.TL_webDocument) {
               FileLoader.getInstance(this.currentAccount).cancelLoadFile(WebFile.createWithWebDocument(this.inlineResult.content));
            }

            this.buttonState = 2;
            this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
            this.invalidate();
         }
      }

   }

   private int getIconForCurrentState() {
      int var1 = this.documentAttachType;
      byte var2 = 4;
      if (var1 != 3 && var1 != 5) {
         this.radialProgress.setColors("chat_mediaLoaderPhoto", "chat_mediaLoaderPhotoSelected", "chat_mediaLoaderPhotoIcon", "chat_mediaLoaderPhotoIconSelected");
         if (this.buttonState == 1) {
            var2 = 10;
         }

         return var2;
      } else {
         this.radialProgress.setColors("chat_inLoader", "chat_inLoaderSelected", "chat_inMediaIcon", "chat_inMediaIconSelected");
         int var3 = this.buttonState;
         if (var3 == 1) {
            return 1;
         } else if (var3 == 2) {
            return 2;
         } else {
            return var3 == 4 ? 3 : 0;
         }
      }
   }

   private void setAttachType() {
      this.currentMessageObject = null;
      this.documentAttachType = 0;
      TLRPC.Document var1 = this.documentAttach;
      if (var1 != null) {
         if (MessageObject.isGifDocument(var1)) {
            this.documentAttachType = 2;
         } else if (MessageObject.isStickerDocument(this.documentAttach)) {
            this.documentAttachType = 6;
         } else if (MessageObject.isMusicDocument(this.documentAttach)) {
            this.documentAttachType = 5;
         } else if (MessageObject.isVoiceDocument(this.documentAttach)) {
            this.documentAttachType = 3;
         }
      } else {
         TLRPC.BotInlineResult var8 = this.inlineResult;
         if (var8 != null) {
            if (var8.photo != null) {
               this.documentAttachType = 7;
            } else if (var8.type.equals("audio")) {
               this.documentAttachType = 5;
            } else if (this.inlineResult.type.equals("voice")) {
               this.documentAttachType = 3;
            }
         }
      }

      int var2 = this.documentAttachType;
      if (var2 == 3 || var2 == 5) {
         TLRPC.TL_message var3 = new TLRPC.TL_message();
         var3.out = true;
         var3.id = -Utilities.random.nextInt();
         var3.to_id = new TLRPC.TL_peerUser();
         TLRPC.Peer var9 = var3.to_id;
         var2 = UserConfig.getInstance(this.currentAccount).getClientUserId();
         var3.from_id = var2;
         var9.user_id = var2;
         var3.date = (int)(System.currentTimeMillis() / 1000L);
         String var4 = "";
         var3.message = "";
         var3.media = new TLRPC.TL_messageMediaDocument();
         TLRPC.MessageMedia var10 = var3.media;
         var10.flags |= 3;
         var10.document = new TLRPC.TL_document();
         var10 = var3.media;
         var10.document.file_reference = new byte[0];
         var3.flags |= 768;
         TLRPC.Document var5 = this.documentAttach;
         if (var5 != null) {
            var10.document = var5;
            var3.attachPath = "";
         } else {
            String var6 = this.inlineResult.content.url;
            var2 = this.documentAttachType;
            String var14 = "mp3";
            String var11;
            if (var2 == 5) {
               var11 = "mp3";
            } else {
               var11 = "ogg";
            }

            var6 = ImageLoader.getHttpUrlExtension(var6, var11);
            var1 = var3.media.document;
            var1.id = 0L;
            var1.access_hash = 0L;
            var1.date = var3.date;
            StringBuilder var7 = new StringBuilder();
            var7.append("audio/");
            var7.append(var6);
            var1.mime_type = var7.toString();
            var1 = var3.media.document;
            var1.size = 0;
            var1.dc_id = 0;
            TLRPC.TL_documentAttributeAudio var16 = new TLRPC.TL_documentAttributeAudio();
            var16.duration = MessageObject.getInlineResultDuration(this.inlineResult);
            var11 = this.inlineResult.title;
            if (var11 == null) {
               var11 = "";
            }

            var16.title = var11;
            var6 = this.inlineResult.description;
            var11 = var4;
            if (var6 != null) {
               var11 = var6;
            }

            var16.performer = var11;
            var16.flags |= 3;
            if (this.documentAttachType == 3) {
               var16.voice = true;
            }

            var3.media.document.attributes.add(var16);
            TLRPC.TL_documentAttributeFilename var12 = new TLRPC.TL_documentAttributeFilename();
            StringBuilder var15 = new StringBuilder();
            var15.append(Utilities.MD5(this.inlineResult.content.url));
            var15.append(".");
            String var17 = this.inlineResult.content.url;
            if (this.documentAttachType == 5) {
               var11 = "mp3";
            } else {
               var11 = "ogg";
            }

            var15.append(ImageLoader.getHttpUrlExtension(var17, var11));
            var12.file_name = var15.toString();
            var3.media.document.attributes.add(var12);
            File var13 = FileLoader.getDirectory(4);
            var7 = new StringBuilder();
            var7.append(Utilities.MD5(this.inlineResult.content.url));
            var7.append(".");
            var6 = this.inlineResult.content.url;
            if (this.documentAttachType == 5) {
               var11 = var14;
            } else {
               var11 = "ogg";
            }

            var7.append(ImageLoader.getHttpUrlExtension(var6, var11));
            var3.attachPath = (new File(var13, var7.toString())).getAbsolutePath();
         }

         this.currentMessageObject = new MessageObject(this.currentAccount, var3, false);
      }

   }

   public TLRPC.BotInlineResult getBotInlineResult() {
      return this.inlineResult;
   }

   public TLRPC.Document getDocument() {
      return this.documentAttach;
   }

   public MessageObject getMessageObject() {
      return this.currentMessageObject;
   }

   public int getObserverTag() {
      return this.TAG;
   }

   public ImageReceiver getPhotoImage() {
      return this.linkImageView;
   }

   public TLRPC.BotInlineResult getResult() {
      return this.inlineResult;
   }

   public boolean isCanPreviewGif() {
      return this.canPreviewGif;
   }

   public boolean isGif() {
      boolean var1;
      if (this.documentAttachType == 2 && this.canPreviewGif) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isSticker() {
      boolean var1;
      if (this.documentAttachType == 6) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.drawLinkImageView && this.linkImageView.onAttachedToWindow()) {
         this.updateButtonState(false, false);
      }

      this.radialProgress.onAttachedToWindow();
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (this.drawLinkImageView) {
         this.linkImageView.onDetachedFromWindow();
      }

      this.radialProgress.onDetachedFromWindow();
      DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
   }

   protected void onDraw(Canvas var1) {
      StaticLayout var2 = this.titleLayout;
      float var3 = 8.0F;
      float var4;
      if (var2 != null) {
         var1.save();
         if (LocaleController.isRTL) {
            var4 = 8.0F;
         } else {
            var4 = (float)AndroidUtilities.leftBaseline;
         }

         var1.translate((float)AndroidUtilities.dp(var4), (float)this.titleY);
         this.titleLayout.draw(var1);
         var1.restore();
      }

      if (this.descriptionLayout != null) {
         Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
         var1.save();
         if (LocaleController.isRTL) {
            var4 = 8.0F;
         } else {
            var4 = (float)AndroidUtilities.leftBaseline;
         }

         var1.translate((float)AndroidUtilities.dp(var4), (float)this.descriptionY);
         this.descriptionLayout.draw(var1);
         var1.restore();
      }

      if (this.linkLayout != null) {
         Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteLinkText"));
         var1.save();
         if (LocaleController.isRTL) {
            var4 = var3;
         } else {
            var4 = (float)AndroidUtilities.leftBaseline;
         }

         var1.translate((float)AndroidUtilities.dp(var4), (float)this.linkY);
         this.linkLayout.draw(var1);
         var1.restore();
      }

      int var5;
      int var6;
      int var7;
      int var8;
      TLRPC.BotInlineResult var15;
      if (!this.mediaWebpage) {
         var5 = this.documentAttachType;
         if (var5 != 3 && var5 != 5) {
            var15 = this.inlineResult;
            if (var15 != null && var15.type.equals("file")) {
               var6 = Theme.chat_inlineResultFile.getIntrinsicWidth();
               var7 = Theme.chat_inlineResultFile.getIntrinsicHeight();
               var8 = this.linkImageView.getImageX() + (AndroidUtilities.dp(52.0F) - var6) / 2;
               var5 = this.linkImageView.getImageY() + (AndroidUtilities.dp(52.0F) - var7) / 2;
               var1.drawRect((float)this.linkImageView.getImageX(), (float)this.linkImageView.getImageY(), (float)(this.linkImageView.getImageX() + AndroidUtilities.dp(52.0F)), (float)(this.linkImageView.getImageY() + AndroidUtilities.dp(52.0F)), LetterDrawable.paint);
               Theme.chat_inlineResultFile.setBounds(var8, var5, var6 + var8, var7 + var5);
               Theme.chat_inlineResultFile.draw(var1);
            } else {
               var15 = this.inlineResult;
               if (var15 == null || !var15.type.equals("audio") && !this.inlineResult.type.equals("voice")) {
                  var15 = this.inlineResult;
                  if (var15 == null || !var15.type.equals("venue") && !this.inlineResult.type.equals("geo")) {
                     this.letterDrawable.draw(var1);
                  } else {
                     var6 = Theme.chat_inlineResultLocation.getIntrinsicWidth();
                     var5 = Theme.chat_inlineResultLocation.getIntrinsicHeight();
                     var8 = this.linkImageView.getImageX() + (AndroidUtilities.dp(52.0F) - var6) / 2;
                     var7 = this.linkImageView.getImageY() + (AndroidUtilities.dp(52.0F) - var5) / 2;
                     var1.drawRect((float)this.linkImageView.getImageX(), (float)this.linkImageView.getImageY(), (float)(this.linkImageView.getImageX() + AndroidUtilities.dp(52.0F)), (float)(this.linkImageView.getImageY() + AndroidUtilities.dp(52.0F)), LetterDrawable.paint);
                     Theme.chat_inlineResultLocation.setBounds(var8, var7, var6 + var8, var5 + var7);
                     Theme.chat_inlineResultLocation.draw(var1);
                  }
               } else {
                  var8 = Theme.chat_inlineResultAudio.getIntrinsicWidth();
                  var7 = Theme.chat_inlineResultAudio.getIntrinsicHeight();
                  var5 = this.linkImageView.getImageX() + (AndroidUtilities.dp(52.0F) - var8) / 2;
                  var6 = this.linkImageView.getImageY() + (AndroidUtilities.dp(52.0F) - var7) / 2;
                  var1.drawRect((float)this.linkImageView.getImageX(), (float)this.linkImageView.getImageY(), (float)(this.linkImageView.getImageX() + AndroidUtilities.dp(52.0F)), (float)(this.linkImageView.getImageY() + AndroidUtilities.dp(52.0F)), LetterDrawable.paint);
                  Theme.chat_inlineResultAudio.setBounds(var5, var6, var8 + var5, var7 + var6);
                  Theme.chat_inlineResultAudio.draw(var1);
               }
            }
         } else {
            RadialProgress2 var9 = this.radialProgress;
            String var14;
            if (this.buttonPressed) {
               var14 = "chat_inAudioSelectedProgress";
            } else {
               var14 = "chat_inAudioProgress";
            }

            var9.setProgressColor(Theme.getColor(var14));
            this.radialProgress.draw(var1);
         }
      } else {
         var15 = this.inlineResult;
         if (var15 != null) {
            TLRPC.BotInlineMessage var16 = var15.send_message;
            if (var16 instanceof TLRPC.TL_botInlineMessageMediaGeo || var16 instanceof TLRPC.TL_botInlineMessageMediaVenue) {
               var6 = Theme.chat_inlineResultLocation.getIntrinsicWidth();
               var5 = Theme.chat_inlineResultLocation.getIntrinsicHeight();
               var7 = this.linkImageView.getImageX() + (this.linkImageView.getImageWidth() - var6) / 2;
               var8 = this.linkImageView.getImageY() + (this.linkImageView.getImageHeight() - var5) / 2;
               var1.drawRect((float)this.linkImageView.getImageX(), (float)this.linkImageView.getImageY(), (float)(this.linkImageView.getImageX() + this.linkImageView.getImageWidth()), (float)(this.linkImageView.getImageY() + this.linkImageView.getImageHeight()), LetterDrawable.paint);
               Theme.chat_inlineResultLocation.setBounds(var7, var8, var6 + var7, var5 + var8);
               Theme.chat_inlineResultLocation.draw(var1);
            }
         }
      }

      if (this.drawLinkImageView) {
         var15 = this.inlineResult;
         if (var15 != null) {
            this.linkImageView.setVisible(PhotoViewer.isShowingImage(var15) ^ true, false);
         }

         var1.save();
         if (this.scaled && this.scale != 0.8F || !this.scaled && this.scale != 1.0F) {
            label102: {
               long var10 = System.currentTimeMillis();
               long var12 = var10 - this.lastUpdateTime;
               this.lastUpdateTime = var10;
               if (this.scaled) {
                  var4 = this.scale;
                  if (var4 != 0.8F) {
                     this.scale = var4 - (float)var12 / 400.0F;
                     if (this.scale < 0.8F) {
                        this.scale = 0.8F;
                     }
                     break label102;
                  }
               }

               this.scale += (float)var12 / 400.0F;
               if (this.scale > 1.0F) {
                  this.scale = 1.0F;
               }
            }

            this.invalidate();
         }

         var4 = this.scale;
         var1.scale(var4, var4, (float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2));
         this.linkImageView.draw(var1);
         var1.restore();
      }

      if (this.mediaWebpage) {
         var5 = this.documentAttachType;
         if (var5 == 7 || var5 == 2) {
            this.radialProgress.draw(var1);
         }
      }

      if (this.needDivider && !this.mediaWebpage) {
         if (LocaleController.isRTL) {
            var1.drawLine(0.0F, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline)), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
         } else {
            var1.drawLine((float)AndroidUtilities.dp((float)AndroidUtilities.leftBaseline), (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
         }
      }

      if (this.needShadow) {
         Theme.chat_contextResult_shadowUnderSwitchDrawable.setBounds(0, 0, this.getMeasuredWidth(), AndroidUtilities.dp(3.0F));
         Theme.chat_contextResult_shadowUnderSwitchDrawable.draw(var1);
      }

   }

   public void onFailedDownload(String var1, boolean var2) {
      this.updateButtonState(true, var2);
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      StringBuilder var2 = new StringBuilder();
      switch(this.documentAttachType) {
      case 1:
         var2.append(LocaleController.getString("AttachDocument", 2131558714));
         break;
      case 2:
         var2.append(LocaleController.getString("AttachGif", 2131558716));
         break;
      case 3:
         var2.append(LocaleController.getString("AttachAudio", 2131558709));
         break;
      case 4:
         var2.append(LocaleController.getString("AttachVideo", 2131558733));
         break;
      case 5:
         var2.append(LocaleController.getString("AttachMusic", 2131558726));
         if (this.descriptionLayout != null && this.titleLayout != null) {
            var2.append(", ");
            var2.append(LocaleController.formatString("AccDescrMusicInfo", 2131558447, this.descriptionLayout.getText(), this.titleLayout.getText()));
         }
         break;
      case 6:
         var2.append(LocaleController.getString("AttachSticker", 2131558730));
         break;
      case 7:
         var2.append(LocaleController.getString("AttachPhoto", 2131558727));
         break;
      case 8:
         var2.append(LocaleController.getString("AttachLocation", 2131558723));
         break;
      default:
         StaticLayout var3 = this.titleLayout;
         if (var3 != null && !TextUtils.isEmpty(var3.getText())) {
            var2.append(this.titleLayout.getText());
         }

         var3 = this.descriptionLayout;
         if (var3 != null && !TextUtils.isEmpty(var3.getText())) {
            if (var2.length() > 0) {
               var2.append(", ");
            }

            var2.append(this.descriptionLayout.getText());
         }
      }

      var1.setText(var2);
   }

   @SuppressLint({"DrawAllocation"})
   protected void onMeasure(int var1, int var2) {
      byte var3 = 0;
      this.drawLinkImageView = false;
      this.descriptionLayout = null;
      this.titleLayout = null;
      this.linkLayout = null;
      this.currentPhotoObject = null;
      this.linkY = AndroidUtilities.dp(27.0F);
      if (this.inlineResult == null && this.documentAttach == null) {
         this.setMeasuredDimension(AndroidUtilities.dp(100.0F), AndroidUtilities.dp(100.0F));
      } else {
         int var4 = MeasureSpec.getSize(var1);
         int var5 = var4 - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline) - AndroidUtilities.dp(8.0F);
         TLRPC.Document var6 = this.documentAttach;
         ArrayList var25;
         TLRPC.BotInlineResult var26;
         if (var6 != null) {
            var25 = new ArrayList(var6.thumbs);
         } else {
            label293: {
               var26 = this.inlineResult;
               if (var26 != null) {
                  TLRPC.Photo var27 = var26.photo;
                  if (var27 != null) {
                     var25 = new ArrayList(var27.sizes);
                     break label293;
                  }
               }

               var25 = null;
            }
         }

         TLRPC.BotInlineResult var7;
         int var8;
         String var28;
         if (!this.mediaWebpage) {
            var7 = this.inlineResult;
            if (var7 != null) {
               var28 = var7.title;
               StaticLayout var29;
               if (var28 != null) {
                  try {
                     var8 = (int)Math.ceil((double)Theme.chat_contextResult_titleTextPaint.measureText(var28));
                     CharSequence var9 = TextUtils.ellipsize(Emoji.replaceEmoji(this.inlineResult.title.replace('\n', ' '), Theme.chat_contextResult_titleTextPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0F), false), Theme.chat_contextResult_titleTextPaint, (float)Math.min(var8, var5), TruncateAt.END);
                     var29 = new StaticLayout(var9, Theme.chat_contextResult_titleTextPaint, var5 + AndroidUtilities.dp(4.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                     this.titleLayout = var29;
                  } catch (Exception var20) {
                     FileLog.e((Throwable)var20);
                  }

                  this.letterDrawable.setTitle(this.inlineResult.title);
               }

               var28 = this.inlineResult.description;
               Exception var31;
               if (var28 != null) {
                  label306: {
                     label282: {
                        CharSequence var32;
                        TextPaint var33;
                        try {
                           var32 = Emoji.replaceEmoji(var28, Theme.chat_contextResult_descriptionTextPaint.getFontMetricsInt(), AndroidUtilities.dp(13.0F), false);
                           var33 = Theme.chat_contextResult_descriptionTextPaint;
                        } catch (Exception var24) {
                           var31 = var24;
                           break label282;
                        }

                        try {
                           this.descriptionLayout = ChatMessageCell.generateStaticLayout(var32, var33, var5, var5, 0, 3);
                           if (this.descriptionLayout.getLineCount() > 0) {
                              this.linkY = this.descriptionY + this.descriptionLayout.getLineBottom(this.descriptionLayout.getLineCount() - 1) + AndroidUtilities.dp(1.0F);
                           }
                           break label306;
                        } catch (Exception var23) {
                           var31 = var23;
                        }
                     }

                     FileLog.e((Throwable)var31);
                  }
               }

               var28 = this.inlineResult.url;
               if (var28 != null) {
                  label273: {
                     label272: {
                        CharSequence var10;
                        TextPaint var11;
                        Alignment var34;
                        try {
                           var8 = (int)Math.ceil((double)Theme.chat_contextResult_descriptionTextPaint.measureText(var28));
                           var10 = TextUtils.ellipsize(this.inlineResult.url.replace('\n', ' '), Theme.chat_contextResult_descriptionTextPaint, (float)Math.min(var8, var5), TruncateAt.MIDDLE);
                           var29 = new StaticLayout;
                           var11 = Theme.chat_contextResult_descriptionTextPaint;
                           var34 = Alignment.ALIGN_NORMAL;
                        } catch (Exception var22) {
                           var31 = var22;
                           break label272;
                        }

                        try {
                           var29.<init>(var10, var11, var5, var34, 1.0F, 0.0F, false);
                           this.linkLayout = var29;
                           break label273;
                        } catch (Exception var21) {
                           var31 = var21;
                        }
                     }

                     FileLog.e((Throwable)var31);
                  }
               }
            }
         }

         String var38;
         TLRPC.PhotoSize var41;
         TLRPC.PhotoSize var44;
         label265: {
            TLRPC.PhotoSize var30;
            label264: {
               TLRPC.Document var40 = this.documentAttach;
               if (var40 != null) {
                  if (MessageObject.isGifDocument(var40)) {
                     this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                  } else {
                     if (MessageObject.isStickerDocument(this.documentAttach)) {
                        this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                        var38 = "webp";
                        var41 = null;
                        break label265;
                     }

                     var5 = this.documentAttachType;
                     if (var5 != 5 && var5 != 3) {
                        this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                     }
                  }
               } else {
                  var7 = this.inlineResult;
                  if (var7 != null && var7.photo != null) {
                     this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var25, AndroidUtilities.getPhotoSize(), true);
                     var44 = FileLoader.getClosestPhotoSizeWithSize(var25, 80);
                     var30 = var44;
                     if (var44 != this.currentPhotoObject) {
                        break label264;
                     }
                  }
               }

               var30 = null;
            }

            var38 = null;
            var41 = var30;
         }

         var26 = this.inlineResult;
         String var16;
         WebFile var47;
         if (var26 != null) {
            TLRPC.TL_webDocument var36;
            label251: {
               if (var26.content instanceof TLRPC.TL_webDocument) {
                  String var35 = var26.type;
                  if (var35 != null) {
                     if (var35.startsWith("gif")) {
                        var36 = (TLRPC.TL_webDocument)this.inlineResult.content;
                        this.documentAttachType = 2;
                        break label251;
                     }

                     if (this.inlineResult.type.equals("photo")) {
                        var7 = this.inlineResult;
                        TLRPC.WebDocument var39 = var7.thumb;
                        if (var39 instanceof TLRPC.TL_webDocument) {
                           var36 = (TLRPC.TL_webDocument)var39;
                        } else {
                           var36 = (TLRPC.TL_webDocument)var7.content;
                        }
                        break label251;
                     }
                  }
               }

               var36 = null;
            }

            TLRPC.TL_webDocument var37 = var36;
            if (var36 == null) {
               TLRPC.WebDocument var48 = this.inlineResult.thumb;
               var37 = var36;
               if (var48 instanceof TLRPC.TL_webDocument) {
                  var37 = (TLRPC.TL_webDocument)var48;
               }
            }

            label242: {
               label312: {
                  if (var37 == null && this.currentPhotoObject == null && var41 == null) {
                     TLRPC.BotInlineMessage var45 = this.inlineResult.send_message;
                     if (var45 instanceof TLRPC.TL_botInlineMessageMediaVenue || var45 instanceof TLRPC.TL_botInlineMessageMediaGeo) {
                        TLRPC.GeoPoint var46 = this.inlineResult.send_message.geo;
                        double var12 = var46.lat;
                        double var14 = var46._long;
                        if (MessagesController.getInstance(this.currentAccount).mapProvider == 2) {
                           var47 = WebFile.createWithGeoPoint(this.inlineResult.send_message.geo, 72, 72, 15, Math.min(2, (int)Math.ceil((double)AndroidUtilities.density)));
                           var28 = null;
                           break label242;
                        }

                        var28 = AndroidUtilities.formapMapUrl(this.currentAccount, var12, var14, 72, 72, true, 15);
                        break label312;
                     }
                  }

                  var28 = null;
               }

               var47 = null;
            }

            var16 = var28;
            if (var37 != null) {
               var47 = WebFile.createWithWebDocument(var37);
               var16 = var28;
            }
         } else {
            var16 = null;
            var47 = null;
         }

         label313: {
            if (this.documentAttach != null) {
               for(var5 = 0; var5 < this.documentAttach.attributes.size(); ++var5) {
                  TLRPC.DocumentAttribute var50 = (TLRPC.DocumentAttribute)this.documentAttach.attributes.get(var5);
                  if (var50 instanceof TLRPC.TL_documentAttributeImageSize || var50 instanceof TLRPC.TL_documentAttributeVideo) {
                     var8 = var50.w;
                     var5 = var50.h;
                     break label313;
                  }
               }
            }

            var8 = 0;
            var5 = 0;
         }

         if (var8 == 0 || var5 == 0) {
            if (this.currentPhotoObject != null) {
               if (var41 != null) {
                  var41.size = -1;
               }

               var44 = this.currentPhotoObject;
               var8 = var44.w;
               var5 = var44.h;
            } else {
               var7 = this.inlineResult;
               if (var7 != null) {
                  int[] var52 = MessageObject.getInlineResultWidthAndHeight(var7);
                  var8 = var52[0];
                  var5 = var52[1];
               }
            }
         }

         int var17;
         label204: {
            if (var8 != 0) {
               var17 = var5;
               if (var5 != 0) {
                  break label204;
               }
            }

            var8 = AndroidUtilities.dp(80.0F);
            var17 = var8;
         }

         if (this.documentAttach != null || this.currentPhotoObject != null || var47 != null || var16 != null) {
            String var42;
            if (this.mediaWebpage) {
               var5 = (int)((float)var8 / ((float)var17 / (float)AndroidUtilities.dp(80.0F)));
               if (this.documentAttachType == 2) {
                  var28 = String.format(Locale.US, "%d_%d_b", (int)((float)var5 / AndroidUtilities.density), 80);
                  var42 = var28;
               } else {
                  var28 = String.format(Locale.US, "%d_%d", (int)((float)var5 / AndroidUtilities.density), 80);
                  StringBuilder var43 = new StringBuilder();
                  var43.append(var28);
                  var43.append("_b");
                  var42 = var43.toString();
               }
            } else {
               var42 = "52_52_b";
               var28 = "52_52";
            }

            ImageReceiver var18 = this.linkImageView;
            boolean var19;
            if (this.documentAttachType == 6) {
               var19 = true;
            } else {
               var19 = false;
            }

            var18.setAspectFit(var19);
            if (this.documentAttachType == 2) {
               TLRPC.Document var49 = this.documentAttach;
               if (var49 != null) {
                  this.linkImageView.setImage(ImageLocation.getForDocument(var49), (String)null, ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), var28, this.documentAttach.size, var38, this.parentObject, 0);
               } else if (var47 != null) {
                  this.linkImageView.setImage(ImageLocation.getForWebFile(var47), (String)null, ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), var28, -1, var38, this.parentObject, 1);
               } else {
                  this.linkImageView.setImage(ImageLocation.getForPath(var16), (String)null, ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), var28, -1, var38, this.parentObject, 1);
               }
            } else {
               TLRPC.PhotoSize var53 = this.currentPhotoObject;
               if (var53 != null) {
                  var6 = this.documentAttach;
                  if (var6 != null) {
                     this.linkImageView.setImage(ImageLocation.getForDocument(var53, var6), var28, ImageLocation.getForPhoto(var41, this.photoAttach), var42, this.currentPhotoObject.size, var38, this.parentObject, 0);
                  } else {
                     this.linkImageView.setImage(ImageLocation.getForPhoto(var53, this.photoAttach), var28, ImageLocation.getForPhoto(var41, this.photoAttach), var42, this.currentPhotoObject.size, var38, this.parentObject, 0);
                  }
               } else if (var47 != null) {
                  this.linkImageView.setImage(ImageLocation.getForWebFile(var47), var28, ImageLocation.getForPhoto(var41, this.photoAttach), var42, -1, var38, this.parentObject, 1);
               } else {
                  this.linkImageView.setImage(ImageLocation.getForPath(var16), var28, ImageLocation.getForPhoto(var41, this.photoAttach), var42, -1, var38, this.parentObject, 1);
               }
            }

            this.drawLinkImageView = true;
         }

         if (this.mediaWebpage) {
            var2 = MeasureSpec.getSize(var2);
            var1 = var2;
            if (var2 == 0) {
               var1 = AndroidUtilities.dp(100.0F);
            }

            this.setMeasuredDimension(var4, var1);
            var2 = (var4 - AndroidUtilities.dp(24.0F)) / 2;
            var5 = (var1 - AndroidUtilities.dp(24.0F)) / 2;
            this.radialProgress.setProgressRect(var2, var5, AndroidUtilities.dp(24.0F) + var2, AndroidUtilities.dp(24.0F) + var5);
            this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0F));
            this.linkImageView.setImageCoords(0, 0, var4, var1);
         } else {
            StaticLayout var51 = this.titleLayout;
            var5 = var3;
            if (var51 != null) {
               var5 = var3;
               if (var51.getLineCount() != 0) {
                  var51 = this.titleLayout;
                  var5 = 0 + var51.getLineBottom(var51.getLineCount() - 1);
               }
            }

            var51 = this.descriptionLayout;
            var2 = var5;
            if (var51 != null) {
               var2 = var5;
               if (var51.getLineCount() != 0) {
                  var51 = this.descriptionLayout;
                  var2 = var5 + var51.getLineBottom(var51.getLineCount() - 1);
               }
            }

            var51 = this.linkLayout;
            var5 = var2;
            if (var51 != null) {
               var5 = var2;
               if (var51.getLineCount() > 0) {
                  var51 = this.linkLayout;
                  var5 = var2 + var51.getLineBottom(var51.getLineCount() - 1);
               }
            }

            var2 = Math.max(AndroidUtilities.dp(52.0F), var5);
            this.setMeasuredDimension(MeasureSpec.getSize(var1), Math.max(AndroidUtilities.dp(68.0F), var2 + AndroidUtilities.dp(16.0F)) + this.needDivider);
            var2 = AndroidUtilities.dp(52.0F);
            if (LocaleController.isRTL) {
               var1 = MeasureSpec.getSize(var1) - AndroidUtilities.dp(8.0F) - var2;
            } else {
               var1 = AndroidUtilities.dp(8.0F);
            }

            this.letterDrawable.setBounds(var1, AndroidUtilities.dp(8.0F), var1 + var2, AndroidUtilities.dp(60.0F));
            this.linkImageView.setImageCoords(var1, AndroidUtilities.dp(8.0F), var2, var2);
            var2 = this.documentAttachType;
            if (var2 == 3 || var2 == 5) {
               this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0F));
               this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0F) + var1, AndroidUtilities.dp(12.0F), var1 + AndroidUtilities.dp(48.0F), AndroidUtilities.dp(56.0F));
            }
         }

      }
   }

   public void onProgressDownload(String var1, float var2) {
      this.radialProgress.setProgress(var2, true);
      int var3 = this.documentAttachType;
      if (var3 != 3 && var3 != 5) {
         if (this.buttonState != 1) {
            this.updateButtonState(false, true);
         }
      } else if (this.buttonState != 4) {
         this.updateButtonState(false, true);
      }

   }

   public void onProgressUpload(String var1, float var2, boolean var3) {
   }

   public void onSuccessDownload(String var1) {
      this.radialProgress.setProgress(1.0F, true);
      this.updateButtonState(false, true);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (!this.mediaWebpage && this.delegate != null && this.inlineResult != null) {
         boolean var5;
         boolean var7;
         label74: {
            int var2 = (int)var1.getX();
            int var3 = (int)var1.getY();
            AndroidUtilities.dp(48.0F);
            int var4 = this.documentAttachType;
            var5 = true;
            if (var4 != 3 && var4 != 5) {
               TLRPC.BotInlineResult var6 = this.inlineResult;
               if (var6 != null) {
                  TLRPC.WebDocument var8 = var6.content;
                  if (var8 != null && !TextUtils.isEmpty(var8.url)) {
                     if (var1.getAction() == 0) {
                        if (this.letterDrawable.getBounds().contains(var2, var3)) {
                           this.buttonPressed = true;
                           break label74;
                        }
                     } else if (this.buttonPressed) {
                        if (var1.getAction() == 1) {
                           this.buttonPressed = false;
                           this.playSoundEffect(0);
                           this.delegate.didPressedImage(this);
                        } else if (var1.getAction() == 3) {
                           this.buttonPressed = false;
                        } else if (var1.getAction() == 2 && !this.letterDrawable.getBounds().contains(var2, var3)) {
                           this.buttonPressed = false;
                        }
                     }
                  }
               }
            } else {
               var7 = this.letterDrawable.getBounds().contains(var2, var3);
               if (var1.getAction() == 0) {
                  if (var7) {
                     this.buttonPressed = true;
                     this.radialProgress.setPressed(this.buttonPressed, false);
                     this.invalidate();
                     break label74;
                  }
               } else if (this.buttonPressed) {
                  if (var1.getAction() == 1) {
                     this.buttonPressed = false;
                     this.playSoundEffect(0);
                     this.didPressedButton();
                     this.invalidate();
                  } else if (var1.getAction() == 3) {
                     this.buttonPressed = false;
                     this.invalidate();
                  } else if (var1.getAction() == 2 && !var7) {
                     this.buttonPressed = false;
                     this.invalidate();
                  }

                  this.radialProgress.setPressed(this.buttonPressed, false);
               }
            }

            var5 = false;
         }

         var7 = var5;
         if (!var5) {
            var7 = super.onTouchEvent(var1);
         }

         return var7;
      } else {
         return super.onTouchEvent(var1);
      }
   }

   public void setCanPreviewGif(boolean var1) {
      this.canPreviewGif = var1;
   }

   public void setDelegate(ContextLinkCell.ContextLinkCellDelegate var1) {
      this.delegate = var1;
   }

   public void setGif(TLRPC.Document var1, boolean var2) {
      this.needDivider = var2;
      this.needShadow = false;
      this.inlineResult = null;
      StringBuilder var3 = new StringBuilder();
      var3.append("gif");
      var3.append(var1);
      this.parentObject = var3.toString();
      this.documentAttach = var1;
      this.photoAttach = null;
      this.mediaWebpage = true;
      this.setAttachType();
      this.requestLayout();
      this.updateButtonState(false, false);
   }

   public void setLink(TLRPC.BotInlineResult var1, boolean var2, boolean var3, boolean var4) {
      this.needDivider = var3;
      this.needShadow = var4;
      this.inlineResult = var1;
      this.parentObject = var1;
      var1 = this.inlineResult;
      if (var1 != null) {
         this.documentAttach = var1.document;
         this.photoAttach = var1.photo;
      } else {
         this.documentAttach = null;
         this.photoAttach = null;
      }

      this.mediaWebpage = var2;
      this.setAttachType();
      this.requestLayout();
      this.updateButtonState(false, false);
   }

   public void setScaled(boolean var1) {
      this.scaled = var1;
      this.lastUpdateTime = System.currentTimeMillis();
      this.invalidate();
   }

   public boolean showingBitmap() {
      boolean var1;
      if (this.linkImageView.getBitmap() != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void updateButtonState(boolean var1, boolean var2) {
      int var3;
      String var4;
      File var10;
      label120: {
         var3 = this.documentAttachType;
         var4 = null;
         TLRPC.Document var5;
         if (var3 != 5 && var3 != 3) {
            if (this.mediaWebpage) {
               TLRPC.BotInlineResult var13 = this.inlineResult;
               if (var13 != null) {
                  TLRPC.Document var11 = var13.document;
                  if (var11 instanceof TLRPC.TL_document) {
                     var4 = FileLoader.getAttachFileName(var11);
                     var10 = FileLoader.getPathToAttach(this.inlineResult.document);
                     break label120;
                  }

                  TLRPC.Photo var12 = var13.photo;
                  if (var12 instanceof TLRPC.TL_photo) {
                     this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(var12.sizes, AndroidUtilities.getPhotoSize(), true);
                     var4 = FileLoader.getAttachFileName(this.currentPhotoObject);
                     var10 = FileLoader.getPathToAttach(this.currentPhotoObject);
                     break label120;
                  }

                  StringBuilder var14;
                  if (var13.content instanceof TLRPC.TL_webDocument) {
                     var14 = new StringBuilder();
                     var14.append(Utilities.MD5(this.inlineResult.content.url));
                     var14.append(".");
                     var14.append(ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, "jpg"));
                     var4 = var14.toString();
                     var10 = new File(FileLoader.getDirectory(4), var4);
                     break label120;
                  }

                  if (var13.thumb instanceof TLRPC.TL_webDocument) {
                     var14 = new StringBuilder();
                     var14.append(Utilities.MD5(this.inlineResult.thumb.url));
                     var14.append(".");
                     var14.append(ImageLoader.getHttpUrlExtension(this.inlineResult.thumb.url, "jpg"));
                     var4 = var14.toString();
                     var10 = new File(FileLoader.getDirectory(4), var4);
                     break label120;
                  }
               } else {
                  var5 = this.documentAttach;
                  if (var5 != null) {
                     var4 = FileLoader.getAttachFileName(var5);
                     var10 = FileLoader.getPathToAttach(this.documentAttach);
                     break label120;
                  }
               }
            }
         } else {
            var5 = this.documentAttach;
            if (var5 != null) {
               var4 = FileLoader.getAttachFileName(var5);
               var10 = FileLoader.getPathToAttach(this.documentAttach);
               break label120;
            }

            if (this.inlineResult.content instanceof TLRPC.TL_webDocument) {
               StringBuilder var6 = new StringBuilder();
               var6.append(Utilities.MD5(this.inlineResult.content.url));
               var6.append(".");
               var4 = this.inlineResult.content.url;
               String var9;
               if (this.documentAttachType == 5) {
                  var9 = "mp3";
               } else {
                  var9 = "ogg";
               }

               var6.append(ImageLoader.getHttpUrlExtension(var4, var9));
               var4 = var6.toString();
               var10 = new File(FileLoader.getDirectory(4), var4);
               break label120;
            }
         }

         var10 = null;
      }

      if (!TextUtils.isEmpty(var4)) {
         boolean var8;
         if (!var10.exists()) {
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var4, this);
            var3 = this.documentAttachType;
            float var7 = 0.0F;
            Float var15;
            if (var3 != 5 && var3 != 3) {
               this.buttonState = 1;
               var15 = ImageLoader.getInstance().getFileProgress(var4);
               if (var15 != null) {
                  var7 = var15;
               }

               this.radialProgress.setProgress(var7, false);
               this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var2);
            } else {
               if (this.documentAttach != null) {
                  var8 = FileLoader.getInstance(this.currentAccount).isLoadingFile(var4);
               } else {
                  var8 = ImageLoader.getInstance().isLoadingHttpFile(var4);
               }

               if (!var8) {
                  this.buttonState = 2;
                  this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var2);
               } else {
                  this.buttonState = 4;
                  var15 = ImageLoader.getInstance().getFileProgress(var4);
                  if (var15 != null) {
                     this.radialProgress.setProgress(var15, var2);
                  } else {
                     this.radialProgress.setProgress(0.0F, var2);
                  }

                  this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var2);
               }
            }

            this.invalidate();
         } else {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            var3 = this.documentAttachType;
            if (var3 != 5 && var3 != 3) {
               this.buttonState = -1;
            } else {
               var8 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
               if (!var8 || var8 && MediaController.getInstance().isMessagePaused()) {
                  this.buttonState = 0;
               } else {
                  this.buttonState = 1;
               }

               this.radialProgress.setProgress(1.0F, var2);
            }

            this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var2);
            this.invalidate();
         }

      }
   }

   public interface ContextLinkCellDelegate {
      void didPressedImage(ContextLinkCell var1);
   }
}

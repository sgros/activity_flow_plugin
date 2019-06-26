package org.telegram.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.Cells.StickerEmojiCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class ContentPreviewViewer {
   private static final int CONTENT_TYPE_GIF = 1;
   private static final int CONTENT_TYPE_NONE = -1;
   private static final int CONTENT_TYPE_STICKER = 0;
   @SuppressLint({"StaticFieldLeak"})
   private static volatile ContentPreviewViewer Instance;
   private static TextPaint textPaint;
   private boolean animateY;
   private ColorDrawable backgroundDrawable = new ColorDrawable(1895825408);
   private ImageReceiver centerImage = new ImageReceiver();
   private boolean clearsInputField;
   private ContentPreviewViewer.FrameLayoutDrawer containerView;
   private int currentAccount;
   private int currentContentType;
   private TLRPC.Document currentDocument;
   private float currentMoveY;
   private float currentMoveYProgress;
   private View currentPreviewCell;
   private TLRPC.InputStickerSet currentStickerSet;
   private ContentPreviewViewer.ContentPreviewViewerDelegate delegate;
   private float finalMoveY;
   private TLRPC.BotInlineResult inlineResult;
   private boolean isVisible = false;
   private int keyboardHeight = AndroidUtilities.dp(200.0F);
   private WindowInsets lastInsets;
   private float lastTouchY;
   private long lastUpdateTime;
   private float moveY = 0.0F;
   private Runnable openPreviewRunnable;
   private Activity parentActivity;
   private float showProgress;
   private Runnable showSheetRunnable = new Runnable() {
      // $FF: synthetic method
      public void lambda$run$0$ContentPreviewViewer$1(ArrayList var1, boolean var2, DialogInterface var3, int var4) {
         if (ContentPreviewViewer.this.parentActivity != null) {
            if ((Integer)var1.get(var4) == 0) {
               if (ContentPreviewViewer.this.delegate != null) {
                  ContentPreviewViewer.this.delegate.sendSticker(ContentPreviewViewer.this.currentDocument, ContentPreviewViewer.this.currentStickerSet);
               }
            } else if ((Integer)var1.get(var4) == 1) {
               if (ContentPreviewViewer.this.delegate != null) {
                  ContentPreviewViewer.this.delegate.openSet(ContentPreviewViewer.this.currentStickerSet, ContentPreviewViewer.this.clearsInputField);
               }
            } else if ((Integer)var1.get(var4) == 2) {
               DataQuery.getInstance(ContentPreviewViewer.this.currentAccount).addRecentSticker(2, ContentPreviewViewer.this.currentStickerSet, ContentPreviewViewer.this.currentDocument, (int)(System.currentTimeMillis() / 1000L), var2);
            }

         }
      }

      // $FF: synthetic method
      public void lambda$run$1$ContentPreviewViewer$1(DialogInterface var1) {
         ContentPreviewViewer.this.visibleDialog = null;
         ContentPreviewViewer.this.close();
      }

      // $FF: synthetic method
      public void lambda$run$2$ContentPreviewViewer$1(ArrayList var1, DialogInterface var2, int var3) {
         if (ContentPreviewViewer.this.parentActivity != null) {
            if ((Integer)var1.get(var3) == 0) {
               if (ContentPreviewViewer.this.delegate != null) {
                  ContentPreviewViewer.ContentPreviewViewerDelegate var5 = ContentPreviewViewer.this.delegate;
                  Object var4;
                  if (ContentPreviewViewer.this.currentDocument != null) {
                     var4 = ContentPreviewViewer.this.currentDocument;
                  } else {
                     var4 = ContentPreviewViewer.this.inlineResult;
                  }

                  var5.sendGif(var4);
               }
            } else if ((Integer)var1.get(var3) == 1) {
               DataQuery.getInstance(ContentPreviewViewer.this.currentAccount).removeRecentGif(ContentPreviewViewer.this.currentDocument);
               ContentPreviewViewer.this.delegate.gifAddedOrDeleted();
            } else if ((Integer)var1.get(var3) == 2) {
               DataQuery.getInstance(ContentPreviewViewer.this.currentAccount).addRecentGif(ContentPreviewViewer.this.currentDocument, (int)(System.currentTimeMillis() / 1000L));
               MessagesController.getInstance(ContentPreviewViewer.this.currentAccount).saveGif("gif", ContentPreviewViewer.this.currentDocument);
               ContentPreviewViewer.this.delegate.gifAddedOrDeleted();
            }

         }
      }

      // $FF: synthetic method
      public void lambda$run$3$ContentPreviewViewer$1(DialogInterface var1) {
         ContentPreviewViewer.this.visibleDialog = null;
         ContentPreviewViewer.this.close();
      }

      public void run() {
         if (ContentPreviewViewer.this.parentActivity != null) {
            boolean var1;
            ArrayList var4;
            ArrayList var5;
            int var6;
            if (ContentPreviewViewer.this.currentContentType == 0) {
               if (ContentPreviewViewer.this.currentStickerSet == null) {
                  return;
               }

               var1 = DataQuery.getInstance(ContentPreviewViewer.this.currentAccount).isStickerInFavorites(ContentPreviewViewer.this.currentDocument);
               BottomSheet.Builder var2 = new BottomSheet.Builder(ContentPreviewViewer.this.parentActivity);
               ArrayList var3 = new ArrayList();
               var4 = new ArrayList();
               var5 = new ArrayList();
               if (ContentPreviewViewer.this.delegate != null) {
                  if (ContentPreviewViewer.this.delegate.needSend()) {
                     var3.add(LocaleController.getString("SendStickerPreview", 2131560708));
                     var5.add(2131165723);
                     var4.add(0);
                  }

                  if (ContentPreviewViewer.this.delegate.needOpen()) {
                     var3.add(LocaleController.formatString("ViewPackPreview", 2131561054));
                     var5.add(2131165722);
                     var4.add(1);
                  }
               }

               if (!MessageObject.isMaskDocument(ContentPreviewViewer.this.currentDocument) && (var1 || DataQuery.getInstance(ContentPreviewViewer.this.currentAccount).canAddStickerToFavorites())) {
                  String var7;
                  if (var1) {
                     var6 = 2131559245;
                     var7 = "DeleteFromFavorites";
                  } else {
                     var6 = 2131558591;
                     var7 = "AddToFavorites";
                  }

                  var3.add(LocaleController.getString(var7, var6));
                  if (var1) {
                     var6 = 2131165724;
                  } else {
                     var6 = 2131165721;
                  }

                  var5.add(var6);
                  var4.add(2);
               }

               if (var3.isEmpty()) {
                  return;
               }

               int[] var9 = new int[var5.size()];

               for(var6 = 0; var6 < var5.size(); ++var6) {
                  var9[var6] = (Integer)var5.get(var6);
               }

               var2.setItems((CharSequence[])var3.toArray(new CharSequence[0]), var9, new _$$Lambda$ContentPreviewViewer$1$_tphIjLgQDrHLUWAgGWRuEayPA8(this, var4, var1));
               var2.setDimBehind(false);
               ContentPreviewViewer.this.visibleDialog = var2.create();
               ContentPreviewViewer.this.visibleDialog.setOnDismissListener(new _$$Lambda$ContentPreviewViewer$1$rUBBWoN2ti7pHLcw01tGVjaLoPY(this));
               ContentPreviewViewer.this.visibleDialog.show();
               ContentPreviewViewer.this.containerView.performHapticFeedback(0);
            } else if (ContentPreviewViewer.this.delegate != null) {
               ContentPreviewViewer.this.animateY = true;
               ContentPreviewViewer var10 = ContentPreviewViewer.this;
               var10.visibleDialog = new BottomSheet(var10.parentActivity, false, 0) {
                  protected void onContainerTranslationYChanged(float var1) {
                     if (ContentPreviewViewer.this.animateY) {
                        this.getSheetContainer();
                        ContentPreviewViewer var2;
                        if (ContentPreviewViewer.this.finalMoveY == 0.0F) {
                           ContentPreviewViewer.this.finalMoveY = 0.0F;
                           var2 = ContentPreviewViewer.this;
                           var2.startMoveY = var2.moveY;
                        }

                        ContentPreviewViewer.this.currentMoveYProgress = 1.0F - Math.min(1.0F, var1 / (float)super.containerView.getMeasuredHeight());
                        var2 = ContentPreviewViewer.this;
                        var2.moveY = var2.startMoveY + (ContentPreviewViewer.this.finalMoveY - ContentPreviewViewer.this.startMoveY) * ContentPreviewViewer.this.currentMoveYProgress;
                        ContentPreviewViewer.this.containerView.invalidate();
                        if (ContentPreviewViewer.this.currentMoveYProgress == 1.0F) {
                           ContentPreviewViewer.this.animateY = false;
                        }
                     }

                  }
               };
               ArrayList var11 = new ArrayList();
               var4 = new ArrayList();
               var5 = new ArrayList();
               if (ContentPreviewViewer.this.delegate.needSend()) {
                  var11.add(LocaleController.getString("SendGifPreview", 2131560693));
                  var5.add(2131165723);
                  var4.add(0);
               }

               if (ContentPreviewViewer.this.currentDocument != null) {
                  var1 = DataQuery.getInstance(ContentPreviewViewer.this.currentAccount).hasRecentGif(ContentPreviewViewer.this.currentDocument);
                  if (var1) {
                     var11.add(LocaleController.formatString("Delete", 2131559227));
                     var5.add(2131165348);
                     var4.add(1);
                  } else {
                     var11.add(LocaleController.formatString("SaveToGIFs", 2131560629));
                     var5.add(2131165720);
                     var4.add(2);
                  }
               } else {
                  var1 = false;
               }

               int[] var8 = new int[var5.size()];

               for(var6 = 0; var6 < var5.size(); ++var6) {
                  var8[var6] = (Integer)var5.get(var6);
               }

               ContentPreviewViewer.this.visibleDialog.setItems((CharSequence[])var11.toArray(new CharSequence[0]), var8, new _$$Lambda$ContentPreviewViewer$1$S1kRdazvJNKM_pErU5YcNROqRxU(this, var4));
               ContentPreviewViewer.this.visibleDialog.setDimBehind(false);
               ContentPreviewViewer.this.visibleDialog.setOnDismissListener(new _$$Lambda$ContentPreviewViewer$1$jPj7FNt8_HCUYPFDcuvR11RuA7g(this));
               ContentPreviewViewer.this.visibleDialog.show();
               ContentPreviewViewer.this.containerView.performHapticFeedback(0);
               if (var1) {
                  ContentPreviewViewer.this.visibleDialog.setItemColor(var11.size() - 1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
               }
            }

         }
      }
   };
   private Drawable slideUpDrawable;
   private float startMoveY;
   private int startX;
   private int startY;
   private StaticLayout stickerEmojiLayout;
   private BottomSheet visibleDialog;
   private LayoutParams windowLayoutParams;
   private FrameLayout windowView;

   public static ContentPreviewViewer getInstance() {
      ContentPreviewViewer var0 = Instance;
      ContentPreviewViewer var1 = var0;
      if (var0 == null) {
         synchronized(PhotoViewer.class){}

         Throwable var10000;
         boolean var10001;
         label206: {
            try {
               var0 = Instance;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label206;
            }

            var1 = var0;
            if (var0 == null) {
               try {
                  var1 = new ContentPreviewViewer();
                  Instance = var1;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label206;
               }
            }

            label193:
            try {
               return var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label193;
            }
         }

         while(true) {
            Throwable var22 = var10000;

            try {
               throw var22;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   public static boolean hasInstance() {
      boolean var0;
      if (Instance != null) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   // $FF: synthetic method
   static void lambda$onTouch$0(RecyclerListView var0, Object var1) {
      if (var0 instanceof RecyclerListView) {
         var0.setOnItemClickListener((RecyclerListView.OnItemClickListener)var1);
      }

   }

   @SuppressLint({"DrawAllocation"})
   private void onDraw(Canvas var1) {
      if (this.containerView != null) {
         ColorDrawable var2 = this.backgroundDrawable;
         if (var2 != null) {
            int var3;
            int var4;
            int var5;
            label61: {
               var2.setAlpha((int)(this.showProgress * 180.0F));
               var2 = this.backgroundDrawable;
               var3 = this.containerView.getWidth();
               var4 = this.containerView.getHeight();
               var5 = 0;
               var2.setBounds(0, 0, var3, var4);
               this.backgroundDrawable.draw(var1);
               var1.save();
               if (VERSION.SDK_INT >= 21) {
                  WindowInsets var15 = this.lastInsets;
                  if (var15 != null) {
                     var3 = var15.getStableInsetBottom() + this.lastInsets.getStableInsetTop();
                     var4 = this.lastInsets.getStableInsetTop();
                     break label61;
                  }
               }

               var4 = AndroidUtilities.statusBarHeight;
               var3 = 0;
            }

            int var6;
            if (this.currentContentType == 1) {
               var6 = Math.min(this.containerView.getWidth(), this.containerView.getHeight() - var3) - AndroidUtilities.dp(40.0F);
            } else {
               var6 = (int)((float)Math.min(this.containerView.getWidth(), this.containerView.getHeight() - var3) / 1.8F);
            }

            float var7 = (float)(this.containerView.getWidth() / 2);
            float var8 = this.moveY;
            int var9 = var6 / 2;
            if (this.stickerEmojiLayout != null) {
               var5 = AndroidUtilities.dp(40.0F);
            }

            var1.translate(var7, var8 + (float)Math.max(var9 + var4 + var5, (this.containerView.getHeight() - var3 - this.keyboardHeight) / 2));
            if (this.centerImage.getBitmap() != null) {
               var7 = this.showProgress;
               var8 = var7 * 0.8F / 0.8F;
               var3 = (int)((float)var6 * var8);
               this.centerImage.setAlpha(var7);
               ImageReceiver var16 = this.centerImage;
               var4 = -var3 / 2;
               var16.setImageCoords(var4, var4, var3, var3);
               this.centerImage.draw(var1);
               if (this.currentContentType == 1) {
                  Drawable var17 = this.slideUpDrawable;
                  if (var17 != null) {
                     var4 = var17.getIntrinsicWidth();
                     var6 = this.slideUpDrawable.getIntrinsicHeight();
                     var3 = (int)(this.centerImage.getDrawRegion().top - (float)AndroidUtilities.dp(this.currentMoveY / (float)AndroidUtilities.dp(60.0F) * 6.0F + 17.0F));
                     this.slideUpDrawable.setAlpha((int)((1.0F - this.currentMoveYProgress) * 255.0F));
                     this.slideUpDrawable.setBounds(-var4 / 2, -var6 + var3, var4 / 2, var3);
                     this.slideUpDrawable.draw(var1);
                  }
               }
            }

            if (this.stickerEmojiLayout != null) {
               var1.translate((float)(-AndroidUtilities.dp(50.0F)), (float)(-this.centerImage.getImageHeight() / 2 - AndroidUtilities.dp(30.0F)));
               this.stickerEmojiLayout.draw(var1);
            }

            var1.restore();
            long var10;
            long var12;
            if (this.isVisible) {
               if (this.showProgress != 1.0F) {
                  var10 = System.currentTimeMillis();
                  var12 = this.lastUpdateTime;
                  this.lastUpdateTime = var10;
                  this.showProgress += (float)(var10 - var12) / 120.0F;
                  this.containerView.invalidate();
                  if (this.showProgress > 1.0F) {
                     this.showProgress = 1.0F;
                  }
               }
            } else if (this.showProgress != 0.0F) {
               var10 = System.currentTimeMillis();
               var12 = this.lastUpdateTime;
               this.lastUpdateTime = var10;
               this.showProgress -= (float)(var10 - var12) / 120.0F;
               this.containerView.invalidate();
               if (this.showProgress < 0.0F) {
                  this.showProgress = 0.0F;
               }

               if (this.showProgress == 0.0F) {
                  this.centerImage.setImageBitmap((Drawable)null);
                  AndroidUtilities.unlockOrientation(this.parentActivity);
                  AndroidUtilities.runOnUIThread(new _$$Lambda$ContentPreviewViewer$3zyytvnhTcdtAb2UIfBrX_cZ_go(this));

                  try {
                     if (this.windowView.getParent() != null) {
                        ((WindowManager)this.parentActivity.getSystemService("window")).removeView(this.windowView);
                     }
                  } catch (Exception var14) {
                     FileLog.e((Throwable)var14);
                  }
               }
            }
         }
      }

   }

   private float rubberYPoisition(float var1, float var2) {
      float var3 = Math.abs(var1) * 0.55F / var2;
      float var4 = 1.0F;
      var2 = -((1.0F - 1.0F / (var3 + 1.0F)) * var2);
      if (var1 < 0.0F) {
         var1 = var4;
      } else {
         var1 = -1.0F;
      }

      return var2 * var1;
   }

   public void close() {
      if (this.parentActivity != null && this.visibleDialog == null) {
         AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
         this.showProgress = 1.0F;
         this.lastUpdateTime = System.currentTimeMillis();
         this.containerView.invalidate();

         try {
            if (this.visibleDialog != null) {
               this.visibleDialog.dismiss();
               this.visibleDialog = null;
            }
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

         this.currentDocument = null;
         this.currentStickerSet = null;
         this.delegate = null;
         this.isVisible = false;
      }

   }

   public void destroy() {
      this.isVisible = false;
      this.delegate = null;
      this.currentDocument = null;
      this.currentStickerSet = null;

      try {
         if (this.visibleDialog != null) {
            this.visibleDialog.dismiss();
            this.visibleDialog = null;
         }
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

      if (this.parentActivity != null) {
         FrameLayout var1 = this.windowView;
         if (var1 != null) {
            try {
               if (var1.getParent() != null) {
                  ((WindowManager)this.parentActivity.getSystemService("window")).removeViewImmediate(this.windowView);
               }

               this.windowView = null;
            } catch (Exception var2) {
               FileLog.e((Throwable)var2);
            }

            Instance = null;
         }
      }

   }

   public boolean isVisible() {
      return this.isVisible;
   }

   // $FF: synthetic method
   public void lambda$onDraw$4$ContentPreviewViewer() {
      this.centerImage.setImageBitmap((Bitmap)null);
   }

   // $FF: synthetic method
   public void lambda$onInterceptTouchEvent$1$ContentPreviewViewer(RecyclerListView var1, int var2, int var3) {
      if (this.openPreviewRunnable != null) {
         var1.setOnItemClickListener((RecyclerListView.OnItemClickListener)null);
         var1.requestDisallowInterceptTouchEvent(true);
         this.openPreviewRunnable = null;
         this.setParentActivity((Activity)var1.getContext());
         this.setKeyboardHeight(var2);
         this.clearsInputField = false;
         View var4 = this.currentPreviewCell;
         if (var4 instanceof StickerEmojiCell) {
            StickerEmojiCell var5 = (StickerEmojiCell)var4;
            this.open(var5.getSticker(), (TLRPC.BotInlineResult)null, var3, ((StickerEmojiCell)this.currentPreviewCell).isRecent());
            var5.setScaled(true);
         } else if (var4 instanceof StickerCell) {
            StickerCell var6 = (StickerCell)var4;
            this.open(var6.getSticker(), (TLRPC.BotInlineResult)null, var3, false);
            var6.setScaled(true);
            this.clearsInputField = var6.isClearsInputField();
         } else if (var4 instanceof ContextLinkCell) {
            ContextLinkCell var7 = (ContextLinkCell)var4;
            this.open(var7.getDocument(), var7.getBotInlineResult(), var3, false);
            if (var3 != 1) {
               var7.setScaled(true);
            }
         }

      }
   }

   // $FF: synthetic method
   public WindowInsets lambda$setParentActivity$2$ContentPreviewViewer(View var1, WindowInsets var2) {
      this.lastInsets = var2;
      return var2;
   }

   // $FF: synthetic method
   public boolean lambda$setParentActivity$3$ContentPreviewViewer(View var1, MotionEvent var2) {
      if (var2.getAction() == 1 || var2.getAction() == 6 || var2.getAction() == 3) {
         this.close();
      }

      return true;
   }

   public boolean onInterceptTouchEvent(MotionEvent var1, RecyclerListView var2, int var3, ContentPreviewViewer.ContentPreviewViewerDelegate var4) {
      this.delegate = var4;
      if (var1.getAction() == 0) {
         int var5 = (int)var1.getX();
         int var6 = (int)var1.getY();
         int var7 = var2.getChildCount();

         for(int var8 = 0; var8 < var7; ++var8) {
            View var13 = null;
            if (var2 instanceof RecyclerListView) {
               var13 = var2.getChildAt(var8);
            }

            if (var13 == null) {
               return false;
            }

            int var9 = var13.getTop();
            int var10 = var13.getBottom();
            int var11 = var13.getLeft();
            int var12 = var13.getRight();
            if (var9 <= var6 && var10 >= var6 && var11 <= var5 && var12 >= var5) {
               byte var15;
               label50: {
                  label49: {
                     if (var13 instanceof StickerEmojiCell) {
                        if (((StickerEmojiCell)var13).showingBitmap()) {
                           this.centerImage.setRoundRadius(0);
                           break label49;
                        }
                     } else if (var13 instanceof StickerCell) {
                        if (((StickerCell)var13).showingBitmap()) {
                           this.centerImage.setRoundRadius(0);
                           break label49;
                        }
                     } else if (var13 instanceof ContextLinkCell) {
                        ContextLinkCell var14 = (ContextLinkCell)var13;
                        if (var14.showingBitmap()) {
                           if (var14.isSticker()) {
                              this.centerImage.setRoundRadius(0);
                              break label49;
                           }

                           if (var14.isGif()) {
                              this.centerImage.setRoundRadius(AndroidUtilities.dp(6.0F));
                              var15 = 1;
                              break label50;
                           }
                        }
                     }

                     var15 = -1;
                     break label50;
                  }

                  var15 = 0;
               }

               if (var15 == -1) {
                  return false;
               }

               this.startX = var5;
               this.startY = var6;
               this.currentPreviewCell = var13;
               this.openPreviewRunnable = new _$$Lambda$ContentPreviewViewer$wFY_75sBoPTmhZIpv5QBGVqAeaE(this, var2, var3, var15);
               AndroidUtilities.runOnUIThread(this.openPreviewRunnable, 200L);
               return true;
            }
         }
      }

      return false;
   }

   public boolean onTouch(MotionEvent var1, RecyclerListView var2, int var3, Object var4, ContentPreviewViewer.ContentPreviewViewerDelegate var5) {
      this.delegate = var5;
      if (this.openPreviewRunnable != null || this.isVisible()) {
         View var16;
         if (var1.getAction() != 1 && var1.getAction() != 3 && var1.getAction() != 6) {
            if (var1.getAction() != 0) {
               if (this.isVisible) {
                  if (var1.getAction() == 2) {
                     if (this.currentContentType == 1) {
                        if (this.visibleDialog == null && this.showProgress == 1.0F) {
                           if (this.lastTouchY == -10000.0F) {
                              this.lastTouchY = var1.getY();
                              this.currentMoveY = 0.0F;
                              this.moveY = 0.0F;
                           } else {
                              float var6 = var1.getY();
                              this.currentMoveY += var6 - this.lastTouchY;
                              this.lastTouchY = var6;
                              var6 = this.currentMoveY;
                              if (var6 > 0.0F) {
                                 this.currentMoveY = 0.0F;
                              } else if (var6 < (float)(-AndroidUtilities.dp(60.0F))) {
                                 this.currentMoveY = (float)(-AndroidUtilities.dp(60.0F));
                              }

                              this.moveY = this.rubberYPoisition(this.currentMoveY, (float)AndroidUtilities.dp(200.0F));
                              this.containerView.invalidate();
                              if (this.currentMoveY <= (float)(-AndroidUtilities.dp(55.0F))) {
                                 AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
                                 this.showSheetRunnable.run();
                              }
                           }
                        }

                        return true;
                     }

                     int var7 = (int)var1.getX();
                     int var8 = (int)var1.getY();
                     int var9 = var2.getChildCount();

                     for(int var10 = 0; var10 < var9; ++var10) {
                        if (var2 instanceof RecyclerListView) {
                           var16 = var2.getChildAt(var10);
                        } else {
                           var16 = null;
                        }

                        if (var16 == null) {
                           return false;
                        }

                        int var11 = var16.getTop();
                        int var12 = var16.getBottom();
                        int var13 = var16.getLeft();
                        int var14 = var16.getRight();
                        if (var11 <= var8 && var12 >= var8 && var13 <= var7 && var14 >= var7) {
                           byte var22;
                           label164: {
                              if (var16 instanceof StickerEmojiCell) {
                                 this.centerImage.setRoundRadius(0);
                              } else if (var16 instanceof StickerCell) {
                                 this.centerImage.setRoundRadius(0);
                              } else {
                                 label163: {
                                    if (var16 instanceof ContextLinkCell) {
                                       ContextLinkCell var17 = (ContextLinkCell)var16;
                                       if (var17.isSticker()) {
                                          this.centerImage.setRoundRadius(0);
                                          break label163;
                                       }

                                       if (var17.isGif()) {
                                          this.centerImage.setRoundRadius(AndroidUtilities.dp(6.0F));
                                          var22 = 1;
                                          break label164;
                                       }
                                    }

                                    var22 = -1;
                                    break label164;
                                 }
                              }

                              var22 = 0;
                           }

                           if (var22 != -1) {
                              View var18 = this.currentPreviewCell;
                              if (var16 != var18) {
                                 if (var18 instanceof StickerEmojiCell) {
                                    ((StickerEmojiCell)var18).setScaled(false);
                                 } else if (var18 instanceof StickerCell) {
                                    ((StickerCell)var18).setScaled(false);
                                 } else if (var18 instanceof ContextLinkCell) {
                                    ((ContextLinkCell)var18).setScaled(false);
                                 }

                                 this.currentPreviewCell = var16;
                                 this.setKeyboardHeight(var3);
                                 this.clearsInputField = false;
                                 var16 = this.currentPreviewCell;
                                 if (var16 instanceof StickerEmojiCell) {
                                    StickerEmojiCell var19 = (StickerEmojiCell)var16;
                                    this.open(var19.getSticker(), (TLRPC.BotInlineResult)null, var22, ((StickerEmojiCell)this.currentPreviewCell).isRecent());
                                    var19.setScaled(true);
                                 } else if (var16 instanceof StickerCell) {
                                    StickerCell var20 = (StickerCell)var16;
                                    this.open(var20.getSticker(), (TLRPC.BotInlineResult)null, var22, false);
                                    var20.setScaled(true);
                                    this.clearsInputField = var20.isClearsInputField();
                                 } else if (var16 instanceof ContextLinkCell) {
                                    ContextLinkCell var21 = (ContextLinkCell)var16;
                                    this.open(var21.getDocument(), var21.getBotInlineResult(), var22, false);
                                    if (var22 != 1) {
                                       var21.setScaled(true);
                                    }
                                 }

                                 return true;
                              }
                           }
                           break;
                        }
                     }
                  }

                  return true;
               }

               if (this.openPreviewRunnable != null) {
                  if (var1.getAction() == 2) {
                     if (Math.hypot((double)((float)this.startX - var1.getX()), (double)((float)this.startY - var1.getY())) > (double)AndroidUtilities.dp(10.0F)) {
                        AndroidUtilities.cancelRunOnUIThread(this.openPreviewRunnable);
                        this.openPreviewRunnable = null;
                     }
                  } else {
                     AndroidUtilities.cancelRunOnUIThread(this.openPreviewRunnable);
                     this.openPreviewRunnable = null;
                  }
               }
            }
         } else {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ContentPreviewViewer$EMKDqwNyTHEkiYf1BXP5lN4E1U8(var2, var4), 150L);
            Runnable var15 = this.openPreviewRunnable;
            if (var15 != null) {
               AndroidUtilities.cancelRunOnUIThread(var15);
               this.openPreviewRunnable = null;
            } else if (this.isVisible()) {
               this.close();
               var16 = this.currentPreviewCell;
               if (var16 != null) {
                  if (var16 instanceof StickerEmojiCell) {
                     ((StickerEmojiCell)var16).setScaled(false);
                  } else if (var16 instanceof StickerCell) {
                     ((StickerCell)var16).setScaled(false);
                  } else if (var16 instanceof ContextLinkCell) {
                     ((ContextLinkCell)var16).setScaled(false);
                  }

                  this.currentPreviewCell = null;
               }
            }
         }
      }

      return false;
   }

   public void open(TLRPC.Document var1, TLRPC.BotInlineResult var2, int var3, boolean var4) {
      if (this.parentActivity != null && this.windowView != null) {
         this.stickerEmojiLayout = null;
         int var5;
         if (var3 == 0) {
            if (var1 == null) {
               return;
            }

            if (textPaint == null) {
               textPaint = new TextPaint(1);
               textPaint.setTextSize((float)AndroidUtilities.dp(24.0F));
            }

            var5 = 0;

            TLRPC.DocumentAttribute var6;
            TLRPC.InputStickerSet var12;
            while(true) {
               if (var5 >= var1.attributes.size()) {
                  var12 = null;
                  break;
               }

               var6 = (TLRPC.DocumentAttribute)var1.attributes.get(var5);
               if (var6 instanceof TLRPC.TL_documentAttributeSticker) {
                  var12 = var6.stickerset;
                  if (var12 != null) {
                     break;
                  }
               }

               ++var5;
            }

            if (var12 != null) {
               try {
                  if (this.visibleDialog != null) {
                     this.visibleDialog.setOnDismissListener((OnDismissListener)null);
                     this.visibleDialog.dismiss();
                     this.visibleDialog = null;
                  }
               } catch (Exception var11) {
                  FileLog.e((Throwable)var11);
               }

               AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
               AndroidUtilities.runOnUIThread(this.showSheetRunnable, 1300L);
            }

            this.currentStickerSet = var12;
            TLRPC.PhotoSize var14 = FileLoader.getClosestPhotoSizeWithSize(var1.thumbs, 90);
            this.centerImage.setImage(ImageLocation.getForDocument(var1), (String)null, ImageLocation.getForDocument(var14, var1), (String)null, "webp", this.currentStickerSet, 1);

            for(var5 = 0; var5 < var1.attributes.size(); ++var5) {
               var6 = (TLRPC.DocumentAttribute)var1.attributes.get(var5);
               if (var6 instanceof TLRPC.TL_documentAttributeSticker && !TextUtils.isEmpty(var6.alt)) {
                  this.stickerEmojiLayout = new StaticLayout(Emoji.replaceEmoji(var6.alt, textPaint.getFontMetricsInt(), AndroidUtilities.dp(24.0F), false), textPaint, AndroidUtilities.dp(100.0F), Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
                  break;
               }
            }
         } else {
            ImageLocation var7;
            ImageReceiver var15;
            if (var1 != null) {
               TLRPC.PhotoSize var8 = FileLoader.getClosestPhotoSizeWithSize(var1.thumbs, 90);
               var15 = this.centerImage;
               var7 = ImageLocation.getForDocument(var1);
               ImageLocation var9 = ImageLocation.getForDocument(var8, var1);
               var5 = var1.size;
               StringBuilder var16 = new StringBuilder();
               var16.append("gif");
               var16.append(var1);
               var15.setImage(var7, (String)null, var9, "90_90_b", var5, (String)null, var16.toString(), 0);
            } else {
               if (var2 == null) {
                  return;
               }

               TLRPC.WebDocument var13 = var2.content;
               if (var13 == null) {
                  return;
               }

               var15 = this.centerImage;
               ImageLocation var17 = ImageLocation.getForWebFile(WebFile.createWithWebDocument(var13));
               var7 = ImageLocation.getForWebFile(WebFile.createWithWebDocument(var2.thumb));
               var5 = var2.content.size;
               StringBuilder var18 = new StringBuilder();
               var18.append("gif");
               var18.append(var2);
               var15.setImage(var17, (String)null, var7, "90_90_b", var5, (String)null, var18.toString(), 1);
            }

            AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
            AndroidUtilities.runOnUIThread(this.showSheetRunnable, 2000L);
         }

         this.currentContentType = var3;
         this.currentDocument = var1;
         this.inlineResult = var2;
         this.containerView.invalidate();
         if (!this.isVisible) {
            AndroidUtilities.lockOrientation(this.parentActivity);

            try {
               if (this.windowView.getParent() != null) {
                  ((WindowManager)this.parentActivity.getSystemService("window")).removeView(this.windowView);
               }
            } catch (Exception var10) {
               FileLog.e((Throwable)var10);
            }

            ((WindowManager)this.parentActivity.getSystemService("window")).addView(this.windowView, this.windowLayoutParams);
            this.isVisible = true;
            this.showProgress = 0.0F;
            this.lastTouchY = -10000.0F;
            this.currentMoveYProgress = 0.0F;
            this.finalMoveY = 0.0F;
            this.currentMoveY = 0.0F;
            this.moveY = 0.0F;
            this.lastUpdateTime = System.currentTimeMillis();
         }
      }

   }

   public void reset() {
      Runnable var1 = this.openPreviewRunnable;
      if (var1 != null) {
         AndroidUtilities.cancelRunOnUIThread(var1);
         this.openPreviewRunnable = null;
      }

      View var2 = this.currentPreviewCell;
      if (var2 != null) {
         if (var2 instanceof StickerEmojiCell) {
            ((StickerEmojiCell)var2).setScaled(false);
         } else if (var2 instanceof StickerCell) {
            ((StickerCell)var2).setScaled(false);
         } else if (var2 instanceof ContextLinkCell) {
            ((ContextLinkCell)var2).setScaled(false);
         }

         this.currentPreviewCell = null;
      }

   }

   public void setDelegate(ContentPreviewViewer.ContentPreviewViewerDelegate var1) {
      this.delegate = var1;
   }

   public void setKeyboardHeight(int var1) {
      this.keyboardHeight = var1;
   }

   public void setParentActivity(Activity var1) {
      this.currentAccount = UserConfig.selectedAccount;
      this.centerImage.setCurrentAccount(this.currentAccount);
      if (this.parentActivity != var1) {
         this.parentActivity = var1;
         this.slideUpDrawable = this.parentActivity.getResources().getDrawable(2131165779);
         this.windowView = new FrameLayout(var1);
         this.windowView.setFocusable(true);
         this.windowView.setFocusableInTouchMode(true);
         if (VERSION.SDK_INT >= 21) {
            this.windowView.setFitsSystemWindows(true);
            this.windowView.setOnApplyWindowInsetsListener(new _$$Lambda$ContentPreviewViewer$_mc9Jej9PVWpKQwOMkaWMnnQlFE(this));
         }

         this.containerView = new ContentPreviewViewer.FrameLayoutDrawer(var1);
         this.containerView.setFocusable(false);
         this.windowView.addView(this.containerView, LayoutHelper.createFrame(-1, -1, 51));
         this.containerView.setOnTouchListener(new _$$Lambda$ContentPreviewViewer$SmRs4xgfa5hV52_8SV4XVqx9cV4(this));
         this.windowLayoutParams = new LayoutParams();
         LayoutParams var2 = this.windowLayoutParams;
         var2.height = -1;
         var2.format = -3;
         var2.width = -1;
         var2.gravity = 48;
         var2.type = 99;
         if (VERSION.SDK_INT >= 21) {
            var2.flags = -2147417848;
         } else {
            var2.flags = 8;
         }

         this.centerImage.setAspectFit(true);
         this.centerImage.setInvalidateAll(true);
         this.centerImage.setParentView(this.containerView);
      }
   }

   public interface ContentPreviewViewerDelegate {
      void gifAddedOrDeleted();

      boolean needOpen();

      boolean needSend();

      void openSet(TLRPC.InputStickerSet var1, boolean var2);

      void sendGif(Object var1);

      void sendSticker(TLRPC.Document var1, Object var2);
   }

   private class FrameLayoutDrawer extends FrameLayout {
      public FrameLayoutDrawer(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
      }

      protected void onDraw(Canvas var1) {
         ContentPreviewViewer.this.onDraw(var1);
      }
   }
}

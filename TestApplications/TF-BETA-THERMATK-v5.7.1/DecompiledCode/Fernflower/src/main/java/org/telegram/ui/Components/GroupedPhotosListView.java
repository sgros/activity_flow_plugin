package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

public class GroupedPhotosListView extends View implements OnGestureListener {
   private boolean animateAllLine;
   private int animateToDX;
   private int animateToDXStart;
   private int animateToItem = -1;
   private Paint backgroundPaint = new Paint();
   private long currentGroupId;
   private int currentImage;
   private float currentItemProgress = 1.0F;
   private ArrayList currentObjects = new ArrayList();
   public ArrayList currentPhotos = new ArrayList();
   private GroupedPhotosListView.GroupedPhotosListViewDelegate delegate;
   private int drawDx;
   private GestureDetector gestureDetector;
   private boolean ignoreChanges;
   private ArrayList imagesToDraw = new ArrayList();
   private int itemHeight;
   private int itemSpacing;
   private int itemWidth;
   private int itemY;
   private long lastUpdateTime;
   private float moveLineProgress;
   private boolean moving;
   private int nextImage;
   private float nextItemProgress = 0.0F;
   private int nextPhotoScrolling = -1;
   private android.widget.Scroller scroll;
   private boolean scrolling;
   private boolean stopedScrolling;
   private ArrayList unusedReceivers = new ArrayList();

   public GroupedPhotosListView(Context var1) {
      super(var1);
      this.gestureDetector = new GestureDetector(var1, this);
      this.scroll = new android.widget.Scroller(var1);
      this.itemWidth = AndroidUtilities.dp(42.0F);
      this.itemHeight = AndroidUtilities.dp(56.0F);
      this.itemSpacing = AndroidUtilities.dp(1.0F);
      this.itemY = AndroidUtilities.dp(3.0F);
      this.backgroundPaint.setColor(2130706432);
   }

   private void fillImages(boolean var1, int var2) {
      if (!var1 && !this.imagesToDraw.isEmpty()) {
         this.unusedReceivers.addAll(this.imagesToDraw);
         this.imagesToDraw.clear();
         this.moving = false;
         this.moveLineProgress = 1.0F;
         this.currentItemProgress = 1.0F;
         this.nextItemProgress = 0.0F;
      }

      this.invalidate();
      if (this.getMeasuredWidth() != 0 && !this.currentPhotos.isEmpty()) {
         int var3 = this.getMeasuredWidth();
         int var4 = this.getMeasuredWidth() / 2 - this.itemWidth / 2;
         int var7;
         int var8;
         int var9;
         int var10;
         if (!var1) {
            var9 = this.currentImage;
            var10 = var9 - 1;
         } else {
            int var5 = this.imagesToDraw.size();
            int var6 = 0;
            var7 = Integer.MIN_VALUE;
            var8 = Integer.MAX_VALUE;

            while(true) {
               var9 = var7;
               var10 = var8;
               if (var6 >= var5) {
                  break;
               }

               int var12;
               label73: {
                  ImageReceiver var11 = (ImageReceiver)this.imagesToDraw.get(var6);
                  var12 = var11.getParam();
                  var10 = this.currentImage;
                  int var13 = this.itemWidth;
                  int var14 = (var12 - var10) * (this.itemSpacing + var13) + var4 + var2;
                  if (var14 <= var3) {
                     var9 = var6;
                     var10 = var5;
                     if (var14 + var13 >= 0) {
                        break label73;
                     }
                  }

                  this.unusedReceivers.add(var11);
                  this.imagesToDraw.remove(var6);
                  var10 = var5 - 1;
                  var9 = var6 - 1;
               }

               var8 = Math.min(var8, var12 - 1);
               var7 = Math.max(var7, var12 + 1);
               var6 = var9 + 1;
               var5 = var10;
            }
         }

         Object var17;
         StringBuilder var18;
         if (var9 != Integer.MIN_VALUE) {
            for(var8 = this.currentPhotos.size(); var9 < var8; ++var9) {
               var7 = (var9 - this.currentImage) * (this.itemWidth + this.itemSpacing) + var4 + var2;
               if (var7 >= var3) {
                  break;
               }

               ImageLocation var15 = (ImageLocation)this.currentPhotos.get(var9);
               ImageReceiver var16 = this.getFreeReceiver();
               var16.setImageCoords(var7, this.itemY, this.itemWidth, this.itemHeight);
               if (this.currentObjects.get(0) instanceof MessageObject) {
                  var17 = this.currentObjects.get(var9);
               } else if (this.currentObjects.get(0) instanceof TLRPC.PageBlock) {
                  var17 = this.delegate.getParentObject();
               } else {
                  var18 = new StringBuilder();
                  var18.append("avatar_");
                  var18.append(this.delegate.getAvatarsDialogId());
                  var17 = var18.toString();
               }

               var16.setImage((ImageLocation)null, (String)null, var15, "80_80", 0, (String)null, var17, 1);
               var16.setParam(var9);
            }
         }

         if (var10 != Integer.MAX_VALUE) {
            while(var10 >= 0) {
               var8 = this.currentImage;
               var7 = this.itemWidth;
               var8 = (var10 - var8) * (this.itemSpacing + var7) + var4 + var2 + var7;
               if (var8 <= 0) {
                  break;
               }

               ImageLocation var20 = (ImageLocation)this.currentPhotos.get(var10);
               ImageReceiver var19 = this.getFreeReceiver();
               var19.setImageCoords(var8, this.itemY, this.itemWidth, this.itemHeight);
               if (this.currentObjects.get(0) instanceof MessageObject) {
                  var17 = this.currentObjects.get(var10);
               } else if (this.currentObjects.get(0) instanceof TLRPC.PageBlock) {
                  var17 = this.delegate.getParentObject();
               } else {
                  var18 = new StringBuilder();
                  var18.append("avatar_");
                  var18.append(this.delegate.getAvatarsDialogId());
                  var17 = var18.toString();
               }

               var19.setImage((ImageLocation)null, (String)null, var20, "80_80", 0, (String)null, var17, 1);
               var19.setParam(var10);
               --var10;
            }
         }
      }

   }

   private ImageReceiver getFreeReceiver() {
      ImageReceiver var1;
      if (this.unusedReceivers.isEmpty()) {
         var1 = new ImageReceiver(this);
      } else {
         var1 = (ImageReceiver)this.unusedReceivers.get(0);
         this.unusedReceivers.remove(0);
      }

      this.imagesToDraw.add(var1);
      var1.setCurrentAccount(this.delegate.getCurrentAccount());
      return var1;
   }

   private int getMaxScrollX() {
      return this.currentImage * (this.itemWidth + this.itemSpacing * 2);
   }

   private int getMinScrollX() {
      return -(this.currentPhotos.size() - this.currentImage - 1) * (this.itemWidth + this.itemSpacing * 2);
   }

   private void stopScrolling() {
      this.scrolling = false;
      if (!this.scroll.isFinished()) {
         this.scroll.abortAnimation();
      }

      int var1 = this.nextPhotoScrolling;
      if (var1 >= 0 && var1 < this.currentObjects.size()) {
         this.stopedScrolling = true;
         var1 = this.nextPhotoScrolling;
         this.animateToItem = var1;
         this.nextImage = var1;
         this.animateToDX = (this.currentImage - var1) * (this.itemWidth + this.itemSpacing);
         this.animateToDXStart = this.drawDx;
         this.moveLineProgress = 1.0F;
         this.nextPhotoScrolling = -1;
      }

      this.invalidate();
   }

   private void updateAfterScroll() {
      int var1 = this.drawDx;
      int var2 = Math.abs(var1);
      int var3 = this.itemWidth;
      int var4 = var3 / 2;
      int var5 = this.itemSpacing;
      byte var6 = -1;
      if (var2 > var4 + var5) {
         byte var11;
         if (var1 > 0) {
            var4 = var1 - (var3 / 2 + var5);
            var11 = 1;
         } else {
            var4 = var1 + var3 / 2 + var5;
            var11 = -1;
         }

         var4 = var11 + var4 / (this.itemWidth + this.itemSpacing * 2);
      } else {
         var4 = 0;
      }

      this.nextPhotoScrolling = this.currentImage - var4;
      var5 = this.delegate.getCurrentIndex();
      ArrayList var7 = this.delegate.getImagesArrLocations();
      ArrayList var8 = this.delegate.getImagesArr();
      ArrayList var9 = this.delegate.getPageBlockArr();
      var4 = this.nextPhotoScrolling;
      if (var5 != var4 && var4 >= 0 && var4 < this.currentPhotos.size()) {
         Object var10 = this.currentObjects.get(this.nextPhotoScrolling);
         if (var8 != null && !var8.isEmpty()) {
            var4 = var8.indexOf((MessageObject)var10);
         } else if (var9 != null && !var9.isEmpty()) {
            var4 = var9.indexOf((TLRPC.PageBlock)var10);
         } else {
            var4 = var6;
            if (var7 != null) {
               var4 = var6;
               if (!var7.isEmpty()) {
                  var4 = var7.indexOf((ImageLocation)var10);
               }
            }
         }

         if (var4 >= 0) {
            this.ignoreChanges = true;
            this.delegate.setCurrentIndex(var4);
         }
      }

      if (!this.scrolling) {
         this.scrolling = true;
         this.stopedScrolling = false;
      }

      this.fillImages(true, this.drawDx);
   }

   public void clear() {
      this.currentPhotos.clear();
      this.currentObjects.clear();
      this.imagesToDraw.clear();
   }

   public void fillList() {
      if (this.ignoreChanges) {
         this.ignoreChanges = false;
      } else {
         int var1;
         ArrayList var2;
         ArrayList var3;
         ArrayList var4;
         int var5;
         Object var6;
         int var7;
         int var8;
         int var9;
         int var11;
         MessageObject var13;
         boolean var14;
         label204: {
            label232: {
               var1 = this.delegate.getCurrentIndex();
               var2 = this.delegate.getImagesArrLocations();
               var3 = this.delegate.getImagesArr();
               var4 = this.delegate.getPageBlockArr();
               var5 = this.delegate.getSlideshowMessageId();
               this.delegate.getCurrentAccount();
               var6 = null;
               if (var2 != null && !var2.isEmpty()) {
                  var6 = (ImageLocation)var2.get(var1);
                  var7 = var2.size();
               } else {
                  label236: {
                     if (var3 != null && !var3.isEmpty()) {
                        MessageObject var16 = (MessageObject)var3.get(var1);
                        if (var16.getGroupIdForUse() == this.currentGroupId) {
                           var9 = Math.min(var1 + 10, var3.size());
                           var8 = var1;

                           for(var7 = 0; var8 < var9; ++var8) {
                              var13 = (MessageObject)var3.get(var8);
                              if (var5 == 0 && var13.getGroupIdForUse() != this.currentGroupId) {
                                 break;
                              }

                              ++var7;
                           }

                           var11 = Math.max(var1 - 10, 0);
                           var9 = var1 - 1;
                           var8 = var7;

                           while(true) {
                              var6 = var16;
                              var7 = var8;
                              if (var9 < var11) {
                                 break label236;
                              }

                              MessageObject var12 = (MessageObject)var3.get(var9);
                              if (var5 == 0) {
                                 var6 = var16;
                                 var7 = var8;
                                 if (var12.getGroupIdForUse() != this.currentGroupId) {
                                    break label236;
                                 }
                              }

                              ++var8;
                              --var9;
                           }
                        }

                        this.currentGroupId = var16.getGroupIdForUse();
                        var6 = var16;
                     } else {
                        if (var4 == null || var4.isEmpty()) {
                           var14 = false;
                           break label232;
                        }

                        TLRPC.PageBlock var10 = (TLRPC.PageBlock)var4.get(var1);
                        var7 = var10.groupId;
                        if ((long)var7 == this.currentGroupId) {
                           var9 = var4.size();
                           var8 = var1;

                           for(var7 = 0; var8 < var9 && (long)((TLRPC.PageBlock)var4.get(var8)).groupId == this.currentGroupId; ++var8) {
                              ++var7;
                           }

                           var9 = var1 - 1;
                           var8 = var7;

                           while(true) {
                              var6 = var10;
                              var7 = var8;
                              if (var9 < 0) {
                                 break label236;
                              }

                              var6 = var10;
                              var7 = var8;
                              if ((long)((TLRPC.PageBlock)var4.get(var9)).groupId != this.currentGroupId) {
                                 break label236;
                              }

                              ++var8;
                              --var9;
                           }
                        }

                        this.currentGroupId = (long)var7;
                        var6 = var10;
                     }

                     var14 = true;
                     break label232;
                  }
               }

               var14 = false;
               var9 = var7;
               break label204;
            }

            var9 = 0;
         }

         if (var6 != null) {
            boolean var17 = var14;
            if (!var14) {
               if (var9 == this.currentPhotos.size() && this.currentObjects.indexOf(var6) != -1) {
                  var9 = this.currentObjects.indexOf(var6);
                  var11 = this.currentImage;
                  var17 = var14;
                  if (var11 != var9) {
                     var17 = var14;
                     if (var9 != -1) {
                        if (this.animateAllLine) {
                           this.animateToItem = var9;
                           this.nextImage = var9;
                           this.animateToDX = (var11 - var9) * (this.itemWidth + this.itemSpacing);
                           this.moving = true;
                           this.animateAllLine = false;
                           this.lastUpdateTime = System.currentTimeMillis();
                           this.invalidate();
                        } else {
                           this.fillImages(true, (var11 - var9) * (this.itemWidth + this.itemSpacing));
                           this.currentImage = var9;
                           this.moving = false;
                        }

                        this.drawDx = 0;
                        var17 = var14;
                     }
                  }
               } else {
                  var17 = true;
               }
            }

            if (var17) {
               this.animateAllLine = false;
               this.currentPhotos.clear();
               this.currentObjects.clear();
               if (var2 != null && !var2.isEmpty()) {
                  this.currentObjects.addAll(var2);
                  this.currentPhotos.addAll(var2);
                  this.currentImage = var1;
                  this.animateToItem = -1;
               } else if (var3 != null && !var3.isEmpty()) {
                  if (this.currentGroupId != 0L || var5 != 0) {
                     var8 = Math.min(var1 + 10, var3.size());

                     for(var7 = var1; var7 < var8; ++var7) {
                        var13 = (MessageObject)var3.get(var7);
                        if (var5 == 0 && var13.getGroupIdForUse() != this.currentGroupId) {
                           break;
                        }

                        this.currentObjects.add(var13);
                        this.currentPhotos.add(ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(var13.photoThumbs, 56, true), var13.photoThumbsObject));
                     }

                     this.currentImage = 0;
                     this.animateToItem = -1;
                     var7 = Math.max(var1 - 10, 0);
                     --var1;

                     while(var1 >= var7) {
                        var13 = (MessageObject)var3.get(var1);
                        if (var5 == 0 && var13.getGroupIdForUse() != this.currentGroupId) {
                           break;
                        }

                        this.currentObjects.add(0, var13);
                        this.currentPhotos.add(0, ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(var13.photoThumbs, 56, true), var13.photoThumbsObject));
                        ++this.currentImage;
                        --var1;
                     }
                  }
               } else if (var4 != null && !var4.isEmpty() && this.currentGroupId != 0L) {
                  var8 = var4.size();

                  TLRPC.PageBlock var15;
                  for(var7 = var1; var7 < var8; ++var7) {
                     var15 = (TLRPC.PageBlock)var4.get(var7);
                     if ((long)var15.groupId != this.currentGroupId) {
                        break;
                     }

                     this.currentObjects.add(var15);
                     this.currentPhotos.add(ImageLocation.getForObject(var15.thumb, var15.thumbObject));
                  }

                  this.currentImage = 0;
                  this.animateToItem = -1;
                  --var1;

                  while(var1 >= 0) {
                     var15 = (TLRPC.PageBlock)var4.get(var1);
                     if ((long)var15.groupId != this.currentGroupId) {
                        break;
                     }

                     this.currentObjects.add(0, var15);
                     this.currentPhotos.add(0, ImageLocation.getForObject(var15.thumb, var15.thumbObject));
                     ++this.currentImage;
                     --var1;
                  }
               }

               if (this.currentPhotos.size() == 1) {
                  this.currentPhotos.clear();
                  this.currentObjects.clear();
               }

               this.fillImages(false, 0);
            }

         }
      }
   }

   public boolean onDown(MotionEvent var1) {
      if (!this.scroll.isFinished()) {
         this.scroll.abortAnimation();
      }

      this.animateToItem = -1;
      return true;
   }

   protected void onDraw(Canvas var1) {
      if (!this.imagesToDraw.isEmpty()) {
         int var2;
         int var3;
         int var4;
         int var5;
         ImageLocation var6;
         int var7;
         TLRPC.PhotoSize var26;
         label123: {
            var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.backgroundPaint);
            var2 = this.imagesToDraw.size();
            var3 = this.drawDx;
            var4 = (int)((float)this.itemWidth * 2.0F);
            var5 = AndroidUtilities.dp(8.0F);
            var6 = (ImageLocation)this.currentPhotos.get(this.currentImage);
            if (var6 != null) {
               var26 = var6.photoSize;
               if (var26 != null) {
                  var7 = Math.max(this.itemWidth, (int)((float)var26.w * ((float)this.itemHeight / (float)var26.h)));
                  break label123;
               }
            }

            var7 = this.itemHeight;
         }

         var7 = Math.min(var4, var7);
         float var8 = (float)(var5 * 2);
         float var9 = this.currentItemProgress;
         var5 = (int)(var8 * var9);
         int var10 = this.itemWidth;
         var10 = var10 + (int)((float)(var7 - var10) * var9) + var5;
         var7 = this.nextImage;
         if (var7 >= 0 && var7 < this.currentPhotos.size()) {
            label114: {
               var6 = (ImageLocation)this.currentPhotos.get(this.nextImage);
               if (var6 != null) {
                  var26 = var6.photoSize;
                  if (var26 != null) {
                     var7 = Math.max(this.itemWidth, (int)((float)var26.w * ((float)this.itemHeight / (float)var26.h)));
                     break label114;
                  }
               }

               var7 = this.itemHeight;
            }
         } else {
            var7 = this.itemWidth;
         }

         int var11 = Math.min(var4, var7);
         var9 = this.nextItemProgress;
         var4 = (int)(var8 * var9);
         var8 = (float)var3;
         float var12 = (float)((var11 + var4 - this.itemWidth) / 2);
         byte var28;
         if (this.nextImage > this.currentImage) {
            var28 = -1;
         } else {
            var28 = 1;
         }

         var3 = (int)(var8 + var12 * var9 * (float)var28);
         var7 = this.itemWidth;
         var11 = var7 + (int)((float)(var11 - var7) * this.nextItemProgress) + var4;
         int var13 = (this.getMeasuredWidth() - var10) / 2;

         for(var7 = 0; var7 < var2; ++var7) {
            ImageReceiver var27 = (ImageReceiver)this.imagesToDraw.get(var7);
            int var14 = var27.getParam();
            int var15 = this.currentImage;
            if (var14 == var15) {
               var27.setImageX(var13 + var3 + var5 / 2);
               var27.setImageWidth(var10 - var5);
            } else {
               int var16 = this.nextImage;
               int var17;
               int var18;
               if (var16 < var15) {
                  if (var14 < var15) {
                     if (var14 <= var16) {
                        var15 = var27.getParam();
                        var17 = this.currentImage;
                        var16 = this.itemWidth;
                        var18 = this.itemSpacing;
                        var27.setImageX((var15 - var17 + 1) * (var16 + var18) + var13 - (var18 + var11) + var3);
                     } else {
                        var27.setImageX((var27.getParam() - this.currentImage) * (this.itemWidth + this.itemSpacing) + var13 + var3);
                     }
                  } else {
                     var27.setImageX(var13 + var10 + this.itemSpacing + (var27.getParam() - this.currentImage - 1) * (this.itemWidth + this.itemSpacing) + var3);
                  }
               } else if (var14 < var15) {
                  var27.setImageX((var27.getParam() - this.currentImage) * (this.itemWidth + this.itemSpacing) + var13 + var3);
               } else if (var14 <= var16) {
                  var27.setImageX(var13 + var10 + this.itemSpacing + (var27.getParam() - this.currentImage - 1) * (this.itemWidth + this.itemSpacing) + var3);
               } else {
                  var16 = this.itemSpacing;
                  var17 = var27.getParam();
                  var18 = this.currentImage;
                  int var19 = this.itemWidth;
                  var15 = this.itemSpacing;
                  var27.setImageX(var13 + var10 + var16 + (var17 - var18 - 2) * (var19 + var15) + var15 + var11 + var3);
               }

               if (var14 == this.nextImage) {
                  var27.setImageWidth(var11 - var4);
                  var27.setImageX(var27.getImageX() + var4 / 2);
               } else {
                  var27.setImageWidth(this.itemWidth);
               }
            }

            var27.draw(var1);
         }

         long var20 = System.currentTimeMillis();
         long var22 = var20 - this.lastUpdateTime;
         long var24 = var22;
         if (var22 > 17L) {
            var24 = 17L;
         }

         this.lastUpdateTime = var20;
         var7 = this.animateToItem;
         if (var7 >= 0) {
            var8 = this.moveLineProgress;
            if (var8 > 0.0F) {
               var9 = (float)var24 / 200.0F;
               this.moveLineProgress = var8 - var9;
               if (var7 == this.currentImage) {
                  var8 = this.currentItemProgress;
                  if (var8 < 1.0F) {
                     this.currentItemProgress = var8 + var9;
                     if (this.currentItemProgress > 1.0F) {
                        this.currentItemProgress = 1.0F;
                     }
                  }

                  var7 = this.animateToDXStart;
                  this.drawDx = var7 + (int)Math.ceil((double)(this.currentItemProgress * (float)(this.animateToDX - var7)));
               } else {
                  this.nextItemProgress = CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0F - this.moveLineProgress);
                  if (this.stopedScrolling) {
                     var8 = this.currentItemProgress;
                     if (var8 > 0.0F) {
                        this.currentItemProgress = var8 - var9;
                        if (this.currentItemProgress < 0.0F) {
                           this.currentItemProgress = 0.0F;
                        }
                     }

                     var7 = this.animateToDXStart;
                     this.drawDx = var7 + (int)Math.ceil((double)(this.nextItemProgress * (float)(this.animateToDX - var7)));
                  } else {
                     this.currentItemProgress = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.moveLineProgress);
                     this.drawDx = (int)Math.ceil((double)(this.nextItemProgress * (float)this.animateToDX));
                  }
               }

               if (this.moveLineProgress <= 0.0F) {
                  this.currentImage = this.animateToItem;
                  this.moveLineProgress = 1.0F;
                  this.currentItemProgress = 1.0F;
                  this.nextItemProgress = 0.0F;
                  this.moving = false;
                  this.stopedScrolling = false;
                  this.drawDx = 0;
                  this.animateToItem = -1;
               }
            }

            this.fillImages(true, this.drawDx);
            this.invalidate();
         }

         if (this.scrolling) {
            var9 = this.currentItemProgress;
            if (var9 > 0.0F) {
               this.currentItemProgress = var9 - (float)var24 / 200.0F;
               if (this.currentItemProgress < 0.0F) {
                  this.currentItemProgress = 0.0F;
               }

               this.invalidate();
            }
         }

         if (!this.scroll.isFinished()) {
            if (this.scroll.computeScrollOffset()) {
               this.drawDx = this.scroll.getCurrX();
               this.updateAfterScroll();
               this.invalidate();
            }

            if (this.scroll.isFinished()) {
               this.stopScrolling();
            }
         }

      }
   }

   public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      this.scroll.abortAnimation();
      if (this.currentPhotos.size() >= 10) {
         this.scroll.fling(this.drawDx, 0, Math.round(var3), 0, this.getMinScrollX(), this.getMaxScrollX(), 0, 0);
      }

      return false;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.fillImages(false, 0);
   }

   public void onLongPress(MotionEvent var1) {
   }

   public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      this.drawDx = (int)((float)this.drawDx - var3);
      int var5 = this.getMinScrollX();
      int var6 = this.getMaxScrollX();
      int var7 = this.drawDx;
      if (var7 < var5) {
         this.drawDx = var5;
      } else if (var7 > var6) {
         this.drawDx = var6;
      }

      this.updateAfterScroll();
      return false;
   }

   public void onShowPress(MotionEvent var1) {
   }

   public boolean onSingleTapUp(MotionEvent var1) {
      int var2 = this.delegate.getCurrentIndex();
      ArrayList var3 = this.delegate.getImagesArrLocations();
      ArrayList var4 = this.delegate.getImagesArr();
      ArrayList var5 = this.delegate.getPageBlockArr();
      this.stopScrolling();
      int var6 = this.imagesToDraw.size();
      int var7 = 0;

      while(true) {
         if (var7 < var6) {
            ImageReceiver var8 = (ImageReceiver)this.imagesToDraw.get(var7);
            if (!var8.isInsideImage(var1.getX(), var1.getY())) {
               ++var7;
               continue;
            }

            var7 = var8.getParam();
            if (var7 < 0 || var7 >= this.currentObjects.size()) {
               return true;
            }

            if (var4 != null && !var4.isEmpty()) {
               var7 = var4.indexOf((MessageObject)this.currentObjects.get(var7));
               if (var2 == var7) {
                  return true;
               }

               this.moveLineProgress = 1.0F;
               this.animateAllLine = true;
               this.delegate.setCurrentIndex(var7);
            } else if (var5 != null && !var5.isEmpty()) {
               var7 = var5.indexOf((TLRPC.PageBlock)this.currentObjects.get(var7));
               if (var2 == var7) {
                  return true;
               }

               this.moveLineProgress = 1.0F;
               this.animateAllLine = true;
               this.delegate.setCurrentIndex(var7);
            } else if (var3 != null && !var3.isEmpty()) {
               var7 = var3.indexOf((ImageLocation)this.currentObjects.get(var7));
               if (var2 == var7) {
                  return true;
               }

               this.moveLineProgress = 1.0F;
               this.animateAllLine = true;
               this.delegate.setCurrentIndex(var7);
            }
         }

         return false;
      }
   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var2 = this.currentPhotos.isEmpty();
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = var3;
      if (!var2) {
         if (this.getAlpha() != 1.0F) {
            var5 = var3;
         } else {
            if (this.gestureDetector.onTouchEvent(var1) || super.onTouchEvent(var1)) {
               var4 = true;
            }

            var5 = var4;
            if (this.scrolling) {
               var5 = var4;
               if (var1.getAction() == 1) {
                  var5 = var4;
                  if (this.scroll.isFinished()) {
                     this.stopScrolling();
                     var5 = var4;
                  }
               }
            }
         }
      }

      return var5;
   }

   public void setDelegate(GroupedPhotosListView.GroupedPhotosListViewDelegate var1) {
      this.delegate = var1;
   }

   public void setMoveProgress(float var1) {
      if (!this.scrolling && this.animateToItem < 0) {
         if (var1 > 0.0F) {
            this.nextImage = this.currentImage - 1;
         } else {
            this.nextImage = this.currentImage + 1;
         }

         int var2 = this.nextImage;
         if (var2 >= 0 && var2 < this.currentPhotos.size()) {
            this.currentItemProgress = 1.0F - Math.abs(var1);
         } else {
            this.currentItemProgress = 1.0F;
         }

         this.nextItemProgress = 1.0F - this.currentItemProgress;
         boolean var3;
         if (var1 != 0.0F) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.moving = var3;
         this.invalidate();
         if (!this.currentPhotos.isEmpty() && (var1 >= 0.0F || this.currentImage != this.currentPhotos.size() - 1) && (var1 <= 0.0F || this.currentImage != 0)) {
            this.drawDx = (int)(var1 * (float)(this.itemWidth + this.itemSpacing));
            this.fillImages(true, this.drawDx);
         }
      }

   }

   public interface GroupedPhotosListViewDelegate {
      int getAvatarsDialogId();

      int getCurrentAccount();

      int getCurrentIndex();

      ArrayList getImagesArr();

      ArrayList getImagesArrLocations();

      ArrayList getPageBlockArr();

      Object getParentObject();

      int getSlideshowMessageId();

      void setCurrentIndex(int var1);
   }
}

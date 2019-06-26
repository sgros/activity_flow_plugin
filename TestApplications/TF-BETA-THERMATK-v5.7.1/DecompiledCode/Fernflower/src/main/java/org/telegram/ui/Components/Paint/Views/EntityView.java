package org.telegram.ui.Components.Paint.Views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import java.util.UUID;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Rect;

public class EntityView extends FrameLayout {
   private boolean announcedSelection = false;
   private EntityView.EntityViewDelegate delegate;
   private GestureDetector gestureDetector;
   private boolean hasPanned = false;
   private boolean hasReleased = false;
   private boolean hasTransformed = false;
   private int offsetX;
   private int offsetY;
   protected Point position = new Point();
   private float previousLocationX;
   private float previousLocationY;
   private boolean recognizedLongPress = false;
   protected EntityView.SelectionView selectionView;
   private UUID uuid = UUID.randomUUID();

   public EntityView(Context var1, Point var2) {
      super(var1);
      this.position = var2;
      this.gestureDetector = new GestureDetector(var1, new SimpleOnGestureListener() {
         public void onLongPress(MotionEvent var1) {
            if (!EntityView.this.hasPanned && !EntityView.this.hasTransformed && !EntityView.this.hasReleased) {
               EntityView.this.recognizedLongPress = true;
               if (EntityView.this.delegate != null) {
                  EntityView.this.performHapticFeedback(0);
                  EntityView.this.delegate.onEntityLongClicked(EntityView.this);
               }
            }

         }
      });
   }

   private boolean onTouchMove(float var1, float var2) {
      float var3 = ((View)this.getParent()).getScaleX();
      Point var4 = new Point((var1 - this.previousLocationX) / var3, (var2 - this.previousLocationY) / var3);
      float var5 = (float)Math.hypot((double)var4.x, (double)var4.y);
      if (this.hasPanned) {
         var3 = 6.0F;
      } else {
         var3 = 16.0F;
      }

      if (var5 > var3) {
         this.pan(var4);
         this.previousLocationX = var1;
         this.previousLocationY = var2;
         this.hasPanned = true;
         return true;
      } else {
         return false;
      }
   }

   private void onTouchUp() {
      if (!this.recognizedLongPress && !this.hasPanned && !this.hasTransformed && !this.announcedSelection) {
         EntityView.EntityViewDelegate var1 = this.delegate;
         if (var1 != null) {
            var1.onEntitySelected(this);
         }
      }

      this.recognizedLongPress = false;
      this.hasPanned = false;
      this.hasTransformed = false;
      this.hasReleased = true;
      this.announcedSelection = false;
   }

   protected EntityView.SelectionView createSelectionView() {
      return null;
   }

   public void deselect() {
      EntityView.SelectionView var1 = this.selectionView;
      if (var1 != null) {
         if (var1.getParent() != null) {
            ((ViewGroup)this.selectionView.getParent()).removeView(this.selectionView);
         }

         this.selectionView = null;
      }
   }

   public Point getPosition() {
      return this.position;
   }

   public float getScale() {
      return this.getScaleX();
   }

   protected Rect getSelectionBounds() {
      return new Rect(0.0F, 0.0F, 0.0F, 0.0F);
   }

   public UUID getUUID() {
      return this.uuid;
   }

   public boolean isSelected() {
      boolean var1;
      if (this.selectionView != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      return this.delegate.allowInteraction(this);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = var1.getPointerCount();
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = var3;
      if (var2 <= 1) {
         if (!this.delegate.allowInteraction(this)) {
            var5 = var3;
         } else {
            label40: {
               label48: {
                  float var6 = var1.getRawX();
                  float var7 = var1.getRawY();
                  var2 = var1.getActionMasked();
                  if (var2 != 0) {
                     label47: {
                        if (var2 != 1) {
                           if (var2 == 2) {
                              var5 = this.onTouchMove(var6, var7);
                              break label40;
                           }

                           if (var2 != 3) {
                              if (var2 == 5) {
                                 break label47;
                              }

                              if (var2 != 6) {
                                 var5 = var4;
                                 break label40;
                              }
                           }
                        }

                        this.onTouchUp();
                        break label48;
                     }
                  }

                  if (!this.isSelected()) {
                     EntityView.EntityViewDelegate var8 = this.delegate;
                     if (var8 != null) {
                        var8.onEntitySelected(this);
                        this.announcedSelection = true;
                     }
                  }

                  this.previousLocationX = var6;
                  this.previousLocationY = var7;
                  this.hasReleased = false;
               }

               var5 = true;
            }

            this.gestureDetector.onTouchEvent(var1);
         }
      }

      return var5;
   }

   public void pan(Point var1) {
      Point var2 = this.position;
      var2.x += var1.x;
      var2.y += var1.y;
      this.updatePosition();
   }

   public void rotate(float var1) {
      this.setRotation(var1);
      this.updateSelectionView();
   }

   public void scale(float var1) {
      this.setScale(Math.max(this.getScale() * var1, 0.1F));
      this.updateSelectionView();
   }

   public void select(ViewGroup var1) {
      EntityView.SelectionView var2 = this.createSelectionView();
      this.selectionView = var2;
      var1.addView(var2);
      var2.updatePosition();
   }

   public void setDelegate(EntityView.EntityViewDelegate var1) {
      this.delegate = var1;
   }

   public void setOffset(int var1, int var2) {
      this.offsetX = var1;
      this.offsetY = var2;
   }

   public void setPosition(Point var1) {
      this.position = var1;
      this.updatePosition();
   }

   public void setScale(float var1) {
      this.setScaleX(var1);
      this.setScaleY(var1);
   }

   public void setSelectionVisibility(boolean var1) {
      EntityView.SelectionView var2 = this.selectionView;
      if (var2 != null) {
         byte var3;
         if (var1) {
            var3 = 0;
         } else {
            var3 = 8;
         }

         var2.setVisibility(var3);
      }
   }

   protected void updatePosition() {
      float var1 = (float)this.getWidth() / 2.0F;
      float var2 = (float)this.getHeight() / 2.0F;
      this.setX(this.position.x - var1);
      this.setY(this.position.y - var2);
      this.updateSelectionView();
   }

   public void updateSelectionView() {
      EntityView.SelectionView var1 = this.selectionView;
      if (var1 != null) {
         var1.updatePosition();
      }

   }

   public interface EntityViewDelegate {
      boolean allowInteraction(EntityView var1);

      boolean onEntityLongClicked(EntityView var1);

      boolean onEntitySelected(EntityView var1);
   }

   public class SelectionView extends FrameLayout {
      public static final int SELECTION_LEFT_HANDLE = 1;
      public static final int SELECTION_RIGHT_HANDLE = 2;
      public static final int SELECTION_WHOLE_HANDLE = 3;
      private int currentHandle;
      protected Paint dotPaint = new Paint(1);
      protected Paint dotStrokePaint = new Paint(1);
      protected Paint paint = new Paint(1);

      public SelectionView(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.paint.setColor(-1);
         this.dotPaint.setColor(-12793105);
         this.dotStrokePaint.setColor(-1);
         this.dotStrokePaint.setStyle(Style.STROKE);
         this.dotStrokePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var3;
         label59: {
            label66: {
               int var2 = var1.getActionMasked();
               var3 = false;
               if (var2 != 0) {
                  label65: {
                     if (var2 != 1) {
                        if (var2 == 2) {
                           var2 = this.currentHandle;
                           float var4;
                           float var5;
                           if (var2 == 3) {
                              var4 = var1.getRawX();
                              var5 = var1.getRawY();
                              var3 = EntityView.this.onTouchMove(var4, var5);
                              break label59;
                           }

                           if (var2 == 0) {
                              break label59;
                           }

                           EntityView.this.hasTransformed = true;
                           Point var6 = new Point(var1.getRawX() - EntityView.this.previousLocationX, var1.getRawY() - EntityView.this.previousLocationY);
                           var5 = (float)Math.toRadians((double)this.getRotation());
                           double var7 = (double)var6.x;
                           double var9 = (double)var5;
                           double var11 = Math.cos(var9);
                           Double.isNaN(var7);
                           double var13 = (double)var6.y;
                           var9 = Math.sin(var9);
                           Double.isNaN(var13);
                           var4 = (float)(var7 * var11 + var13 * var9);
                           var5 = var4;
                           if (this.currentHandle == 1) {
                              var5 = var4 * -1.0F;
                           }

                           label42: {
                              var5 = var5 * 2.0F / (float)this.getWidth();
                              EntityView.this.scale(var5 + 1.0F);
                              float var15 = (float)(this.getLeft() + this.getWidth() / 2);
                              var4 = (float)(this.getTop() + this.getHeight() / 2);
                              float var16 = var1.getRawX() - (float)((View)this.getParent()).getLeft();
                              float var17 = var1.getRawY() - (float)((View)this.getParent()).getTop() - (float)AndroidUtilities.statusBarHeight;
                              var5 = 0.0F;
                              var2 = this.currentHandle;
                              if (var2 == 1) {
                                 var13 = Math.atan2((double)(var4 - var17), (double)(var15 - var16));
                              } else {
                                 if (var2 != 2) {
                                    break label42;
                                 }

                                 var13 = Math.atan2((double)(var17 - var4), (double)(var16 - var15));
                              }

                              var5 = (float)var13;
                           }

                           EntityView.this.rotate((float)Math.toDegrees((double)var5));
                           EntityView.this.previousLocationX = var1.getRawX();
                           EntityView.this.previousLocationY = var1.getRawY();
                           break label66;
                        }

                        if (var2 != 3) {
                           if (var2 == 5) {
                              break label65;
                           }

                           if (var2 != 6) {
                              break label59;
                           }
                        }
                     }

                     EntityView.this.onTouchUp();
                     this.currentHandle = 0;
                     break label66;
                  }
               }

               var2 = this.pointInsideHandle(var1.getX(), var1.getY());
               if (var2 == 0) {
                  break label59;
               }

               this.currentHandle = var2;
               EntityView.this.previousLocationX = var1.getRawX();
               EntityView.this.previousLocationY = var1.getRawY();
               EntityView.this.hasReleased = false;
            }

            var3 = true;
         }

         if (this.currentHandle == 3) {
            EntityView.this.gestureDetector.onTouchEvent(var1);
         }

         return var3;
      }

      protected int pointInsideHandle(float var1, float var2) {
         return 0;
      }

      protected void updatePosition() {
         Rect var1 = EntityView.this.getSelectionBounds();
         LayoutParams var2 = (LayoutParams)this.getLayoutParams();
         var2.leftMargin = (int)var1.x + EntityView.this.offsetX;
         var2.topMargin = (int)var1.y + EntityView.this.offsetY;
         var2.width = (int)var1.width;
         var2.height = (int)var1.height;
         this.setLayoutParams(var2);
         this.setRotation(EntityView.this.getRotation());
      }
   }
}

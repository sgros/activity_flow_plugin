package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.os.Looper;
import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Paint.Brush;
import org.telegram.ui.Components.Paint.Painting;
import org.telegram.ui.Components.Paint.RenderView;
import org.telegram.ui.Components.Paint.Swatch;
import org.telegram.ui.Components.Paint.UndoStore;
import org.telegram.ui.Components.Paint.Views.ColorPicker;
import org.telegram.ui.Components.Paint.Views.EditTextOutline;
import org.telegram.ui.Components.Paint.Views.EntitiesContainerView;
import org.telegram.ui.Components.Paint.Views.EntityView;
import org.telegram.ui.Components.Paint.Views.StickerView;
import org.telegram.ui.Components.Paint.Views.TextPaintView;

@SuppressLint({"NewApi"})
public class PhotoPaintView extends FrameLayout implements EntityView.EntityViewDelegate {
   private static final int gallery_menu_done = 1;
   private Bitmap bitmapToEdit;
   private Brush[] brushes = new Brush[]{new Brush.Radial(), new Brush.Elliptical(), new Brush.Neon()};
   private TextView cancelTextView;
   private ColorPicker colorPicker;
   private Animator colorPickerAnimator;
   int currentBrush;
   private EntityView currentEntityView;
   private FrameLayout curtainView;
   private FrameLayout dimView;
   private TextView doneTextView;
   private Point editedTextPosition;
   private float editedTextRotation;
   private float editedTextScale;
   private boolean editingText;
   private EntitiesContainerView entitiesView;
   private String initialText;
   private int orientation;
   private ImageView paintButton;
   private Size paintingSize;
   private boolean pickingSticker;
   private ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout;
   private android.graphics.Rect popupRect;
   private ActionBarPopupWindow popupWindow;
   private DispatchQueue queue = new DispatchQueue("Paint");
   private RenderView renderView;
   private boolean selectedStroke = true;
   private FrameLayout selectionContainerView;
   private StickerMasksView stickersView;
   private FrameLayout textDimView;
   private FrameLayout toolsView;
   private UndoStore undoStore;

   public PhotoPaintView(Context var1, Bitmap var2, int var3) {
      super(var1);
      this.bitmapToEdit = var2;
      this.orientation = var3;
      this.undoStore = new UndoStore();
      this.undoStore.setDelegate(new _$$Lambda$PhotoPaintView$R6Re0Kk5HfUX12qMoexuM_2zbtQ(this));
      this.curtainView = new FrameLayout(var1);
      this.curtainView.setBackgroundColor(-16777216);
      this.curtainView.setVisibility(4);
      this.addView(this.curtainView);
      this.renderView = new RenderView(var1, new Painting(this.getPaintingSize()), var2, this.orientation);
      this.renderView.setDelegate(new RenderView.RenderViewDelegate() {
         public void onBeganDrawing() {
            if (PhotoPaintView.this.currentEntityView != null) {
               PhotoPaintView.this.selectEntity((EntityView)null);
            }

         }

         public void onFinishedDrawing(boolean var1) {
            PhotoPaintView.this.colorPicker.setUndoEnabled(PhotoPaintView.this.undoStore.canUndo());
         }

         public boolean shouldDraw() {
            boolean var1;
            if (PhotoPaintView.this.currentEntityView == null) {
               var1 = true;
            } else {
               var1 = false;
            }

            if (!var1) {
               PhotoPaintView.this.selectEntity((EntityView)null);
            }

            return var1;
         }
      });
      this.renderView.setUndoStore(this.undoStore);
      this.renderView.setQueue(this.queue);
      this.renderView.setVisibility(4);
      this.renderView.setBrush(this.brushes[0]);
      this.addView(this.renderView, LayoutHelper.createFrame(-1, -1, 51));
      this.entitiesView = new EntitiesContainerView(var1, new EntitiesContainerView.EntitiesContainerViewDelegate() {
         public void onEntityDeselect() {
            PhotoPaintView.this.selectEntity((EntityView)null);
         }

         public EntityView onSelectedEntityRequest() {
            return PhotoPaintView.this.currentEntityView;
         }

         public boolean shouldReceiveTouches() {
            boolean var1;
            if (PhotoPaintView.this.textDimView.getVisibility() != 0) {
               var1 = true;
            } else {
               var1 = false;
            }

            return var1;
         }
      });
      this.entitiesView.setPivotX(0.0F);
      this.entitiesView.setPivotY(0.0F);
      this.addView(this.entitiesView);
      this.dimView = new FrameLayout(var1);
      this.dimView.setAlpha(0.0F);
      this.dimView.setBackgroundColor(1711276032);
      this.dimView.setVisibility(8);
      this.addView(this.dimView);
      this.textDimView = new FrameLayout(var1);
      this.textDimView.setAlpha(0.0F);
      this.textDimView.setBackgroundColor(1711276032);
      this.textDimView.setVisibility(8);
      this.textDimView.setOnClickListener(new _$$Lambda$PhotoPaintView$ZykewOMvdivcJ4QbiFmia3gfIL0(this));
      this.selectionContainerView = new FrameLayout(var1) {
         public boolean onTouchEvent(MotionEvent var1) {
            return false;
         }
      };
      this.addView(this.selectionContainerView);
      this.colorPicker = new ColorPicker(var1);
      this.addView(this.colorPicker);
      this.colorPicker.setDelegate(new ColorPicker.ColorPickerDelegate() {
         public void onBeganColorPicking() {
            if (!(PhotoPaintView.this.currentEntityView instanceof TextPaintView)) {
               PhotoPaintView.this.setDimVisibility(true);
            }

         }

         public void onColorValueChanged() {
            PhotoPaintView var1 = PhotoPaintView.this;
            var1.setCurrentSwatch(var1.colorPicker.getSwatch(), false);
         }

         public void onFinishedColorPicking() {
            PhotoPaintView var1 = PhotoPaintView.this;
            var1.setCurrentSwatch(var1.colorPicker.getSwatch(), false);
            if (!(PhotoPaintView.this.currentEntityView instanceof TextPaintView)) {
               PhotoPaintView.this.setDimVisibility(false);
            }

         }

         public void onSettingsPressed() {
            if (PhotoPaintView.this.currentEntityView != null) {
               if (PhotoPaintView.this.currentEntityView instanceof StickerView) {
                  PhotoPaintView.this.mirrorSticker();
               } else if (PhotoPaintView.this.currentEntityView instanceof TextPaintView) {
                  PhotoPaintView.this.showTextSettings();
               }
            } else {
               PhotoPaintView.this.showBrushSettings();
            }

         }

         public void onUndoPressed() {
            PhotoPaintView.this.undoStore.undo();
         }
      });
      this.toolsView = new FrameLayout(var1);
      this.toolsView.setBackgroundColor(-16777216);
      this.addView(this.toolsView, LayoutHelper.createFrame(-1, 48, 83));
      this.cancelTextView = new TextView(var1);
      this.cancelTextView.setTextSize(1, 14.0F);
      this.cancelTextView.setTextColor(-1);
      this.cancelTextView.setGravity(17);
      this.cancelTextView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
      this.cancelTextView.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
      this.cancelTextView.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
      this.cancelTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.toolsView.addView(this.cancelTextView, LayoutHelper.createFrame(-2, -1, 51));
      this.doneTextView = new TextView(var1);
      this.doneTextView.setTextSize(1, 14.0F);
      this.doneTextView.setTextColor(-11420173);
      this.doneTextView.setGravity(17);
      this.doneTextView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
      this.doneTextView.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
      this.doneTextView.setText(LocaleController.getString("Done", 2131559299).toUpperCase());
      this.doneTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.toolsView.addView(this.doneTextView, LayoutHelper.createFrame(-2, -1, 53));
      this.paintButton = new ImageView(var1);
      this.paintButton.setScaleType(ScaleType.CENTER);
      this.paintButton.setImageResource(2131165745);
      this.paintButton.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
      this.toolsView.addView(this.paintButton, LayoutHelper.createFrame(54, -1.0F, 17, 0.0F, 0.0F, 56.0F, 0.0F));
      this.paintButton.setOnClickListener(new _$$Lambda$PhotoPaintView$rf515UpLc_844_0_ExvqyyhAN3g(this));
      ImageView var5 = new ImageView(var1);
      var5.setScaleType(ScaleType.CENTER);
      var5.setImageResource(2131165749);
      var5.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
      this.toolsView.addView(var5, LayoutHelper.createFrame(54, -1, 17));
      var5.setOnClickListener(new _$$Lambda$PhotoPaintView$bh1PtB_nRqdfutcf4y5QOEZjU74(this));
      ImageView var4 = new ImageView(var1);
      var4.setScaleType(ScaleType.CENTER);
      var4.setImageResource(2131165747);
      var4.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
      this.toolsView.addView(var4, LayoutHelper.createFrame(54, -1.0F, 17, 56.0F, 0.0F, 0.0F, 0.0F));
      var4.setOnClickListener(new _$$Lambda$PhotoPaintView$SEafmfD5xvHbkIUST7pqiumb7sI(this));
      this.colorPicker.setUndoEnabled(false);
      this.setCurrentSwatch(this.colorPicker.getSwatch(), false);
      this.updateSettingsButton();
   }

   private int baseFontSize() {
      return (int)(this.getPaintingSize().width / 9.0F);
   }

   private Size baseStickerSize() {
      double var1 = (double)this.getPaintingSize().width;
      Double.isNaN(var1);
      float var3 = (float)Math.floor(var1 * 0.5D);
      return new Size(var3, var3);
   }

   private FrameLayout buttonForBrush(int var1, int var2, boolean var3) {
      FrameLayout var4 = new FrameLayout(this.getContext());
      var4.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      var4.setOnClickListener(new _$$Lambda$PhotoPaintView$gGzFKkJs5bEvFPWs_vHHaN5jxJY(this, var1));
      ImageView var5 = new ImageView(this.getContext());
      var5.setImageResource(var2);
      var4.addView(var5, LayoutHelper.createFrame(165, 44.0F, 19, 46.0F, 0.0F, 8.0F, 0.0F));
      if (var3) {
         var5 = new ImageView(this.getContext());
         var5.setImageResource(2131165412);
         var5.setScaleType(ScaleType.CENTER);
         var4.addView(var5, LayoutHelper.createFrame(50, -1.0F));
      }

      return var4;
   }

   private FrameLayout buttonForText(boolean var1, String var2, boolean var3) {
      FrameLayout var4 = new FrameLayout(this.getContext()) {
         public boolean onInterceptTouchEvent(MotionEvent var1) {
            return true;
         }
      };
      var4.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      var4.setOnClickListener(new _$$Lambda$PhotoPaintView$Ooj6P__OHnWplT4W_LITMgEHYP8(this, var1));
      EditTextOutline var5 = new EditTextOutline(this.getContext());
      var5.setBackgroundColor(0);
      var5.setEnabled(false);
      var5.setStrokeWidth((float)AndroidUtilities.dp(3.0F));
      int var6 = -16777216;
      int var7;
      if (var1) {
         var7 = -1;
      } else {
         var7 = -16777216;
      }

      var5.setTextColor(var7);
      if (var1) {
         var7 = var6;
      } else {
         var7 = 0;
      }

      var5.setStrokeColor(var7);
      var5.setPadding(AndroidUtilities.dp(2.0F), 0, AndroidUtilities.dp(2.0F), 0);
      var5.setTextSize(1, 18.0F);
      var5.setTypeface((Typeface)null, 1);
      var5.setTag(var1);
      var5.setText(var2);
      var4.addView(var5, LayoutHelper.createFrame(-2, -2.0F, 19, 46.0F, 0.0F, 16.0F, 0.0F));
      if (var3) {
         ImageView var8 = new ImageView(this.getContext());
         var8.setImageResource(2131165412);
         var8.setScaleType(ScaleType.CENTER);
         var4.addView(var8, LayoutHelper.createFrame(50, -1.0F));
      }

      return var4;
   }

   private PhotoPaintView.StickerPosition calculateStickerPosition(TLRPC.Document var1) {
      for(int var2 = 0; var2 < var1.attributes.size(); ++var2) {
         TLRPC.DocumentAttribute var3 = (TLRPC.DocumentAttribute)var1.attributes.get(var2);
         if (var3 instanceof TLRPC.TL_documentAttributeSticker) {
            TLRPC.TL_maskCoords var4 = var3.mask_coords;
            break;
         }
      }

      return new PhotoPaintView.StickerPosition(this.centerPositionForEntity(), 0.75F, 0.0F);
   }

   private Point centerPositionForEntity() {
      Size var1 = this.getPaintingSize();
      return new Point(var1.width / 2.0F, var1.height / 2.0F);
   }

   private void closeStickersView() {
      StickerMasksView var1 = this.stickersView;
      if (var1 != null && var1.getVisibility() == 0) {
         this.pickingSticker = false;
         ObjectAnimator var2 = ObjectAnimator.ofFloat(this.stickersView, "alpha", new float[]{1.0F, 0.0F});
         var2.setDuration(200L);
         var2.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               PhotoPaintView.this.stickersView.setVisibility(8);
            }
         });
         var2.start();
      }

   }

   private void createSticker(Object var1, TLRPC.Document var2) {
      PhotoPaintView.StickerPosition var3 = this.calculateStickerPosition(var2);
      StickerView var4 = new StickerView(this.getContext(), var3.position, var3.angle, var3.scale, this.baseStickerSize(), var2, var1);
      var4.setDelegate(this);
      this.entitiesView.addView(var4);
      this.registerRemovalUndo(var4);
      this.selectEntity(var4);
   }

   private void createText() {
      Swatch var1 = this.colorPicker.getSwatch();
      Swatch var2 = new Swatch(-1, 1.0F, var1.brushWeight);
      var1 = new Swatch(-16777216, 0.85F, var1.brushWeight);
      if (this.selectedStroke) {
         var2 = var1;
      }

      this.setCurrentSwatch(var2, true);
      TextPaintView var3 = new TextPaintView(this.getContext(), this.startPositionRelativeToEntity((EntityView)null), this.baseFontSize(), "", this.colorPicker.getSwatch(), this.selectedStroke);
      var3.setDelegate(this);
      var3.setMaxWidth((int)(this.getPaintingSize().width - 20.0F));
      this.entitiesView.addView(var3, LayoutHelper.createFrame(-2, -2.0F));
      this.registerRemovalUndo(var3);
      this.selectEntity(var3);
      this.editSelectedTextEntity();
   }

   private void duplicateSelectedEntity() {
      EntityView var1 = this.currentEntityView;
      if (var1 != null) {
         Object var2 = null;
         Point var3 = this.startPositionRelativeToEntity(var1);
         var1 = this.currentEntityView;
         if (var1 instanceof StickerView) {
            var2 = new StickerView(this.getContext(), (StickerView)this.currentEntityView, var3);
            ((EntityView)var2).setDelegate(this);
            this.entitiesView.addView((View)var2);
         } else if (var1 instanceof TextPaintView) {
            var2 = new TextPaintView(this.getContext(), (TextPaintView)this.currentEntityView, var3);
            ((EntityView)var2).setDelegate(this);
            ((TextPaintView)var2).setMaxWidth((int)(this.getPaintingSize().width - 20.0F));
            this.entitiesView.addView((View)var2, LayoutHelper.createFrame(-2, -2.0F));
         }

         this.registerRemovalUndo((EntityView)var2);
         this.selectEntity((EntityView)var2);
         this.updateSettingsButton();
      }
   }

   private void editSelectedTextEntity() {
      if (this.currentEntityView instanceof TextPaintView && !this.editingText) {
         this.curtainView.setVisibility(0);
         TextPaintView var1 = (TextPaintView)this.currentEntityView;
         this.initialText = var1.getText();
         this.editingText = true;
         this.editedTextPosition = var1.getPosition();
         this.editedTextRotation = var1.getRotation();
         this.editedTextScale = var1.getScale();
         var1.setPosition(this.centerPositionForEntity());
         var1.setRotation(0.0F);
         var1.setScale(1.0F);
         this.toolsView.setVisibility(8);
         this.setTextDimVisibility(true, var1);
         var1.beginEditing();
         ((InputMethodManager)ApplicationLoader.applicationContext.getSystemService("input_method")).toggleSoftInputFromWindow(var1.getFocusedView().getWindowToken(), 2, 0);
      }

   }

   private Size getPaintingSize() {
      Size var1 = this.paintingSize;
      if (var1 != null) {
         return var1;
      } else {
         int var2;
         if (this.isSidewardOrientation()) {
            var2 = this.bitmapToEdit.getHeight();
         } else {
            var2 = this.bitmapToEdit.getWidth();
         }

         float var3 = (float)var2;
         if (this.isSidewardOrientation()) {
            var2 = this.bitmapToEdit.getWidth();
         } else {
            var2 = this.bitmapToEdit.getHeight();
         }

         float var4 = (float)var2;
         var1 = new Size(var3, var4);
         var1.width = 1280.0F;
         var1.height = (float)Math.floor((double)(var1.width * var4 / var3));
         if (var1.height > 1280.0F) {
            var1.height = 1280.0F;
            var1.width = (float)Math.floor((double)(var1.height * var3 / var4));
         }

         this.paintingSize = var1;
         return var1;
      }
   }

   private boolean hasChanges() {
      boolean var1;
      if (!this.undoStore.canUndo() && this.entitiesView.entitiesCount() <= 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean isSidewardOrientation() {
      int var1 = this.orientation;
      boolean var2;
      if (var1 % 360 != 90 && var1 % 360 != 270) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   // $FF: synthetic method
   static void lambda$maybeShowDismissalAlert$6(Runnable var0, DialogInterface var1, int var2) {
      var0.run();
   }

   // $FF: synthetic method
   static void lambda$shutdown$5() {
      Looper var0 = Looper.myLooper();
      if (var0 != null) {
         var0.quit();
      }

   }

   private void mirrorSticker() {
      EntityView var1 = this.currentEntityView;
      if (var1 instanceof StickerView) {
         ((StickerView)var1).mirror();
      }

   }

   private void openStickersView() {
      StickerMasksView var1 = this.stickersView;
      if (var1 == null || var1.getVisibility() != 0) {
         this.pickingSticker = true;
         if (this.stickersView == null) {
            this.stickersView = new StickerMasksView(this.getContext());
            this.stickersView.setListener(new StickerMasksView.Listener() {
               public void onStickerSelected(Object var1, TLRPC.Document var2) {
                  PhotoPaintView.this.closeStickersView();
                  PhotoPaintView.this.createSticker(var1, var2);
               }

               public void onTypeChanged() {
               }
            });
            this.addView(this.stickersView, LayoutHelper.createFrame(-1, -1, 51));
         }

         this.stickersView.setVisibility(0);
         ObjectAnimator var2 = ObjectAnimator.ofFloat(this.stickersView, "alpha", new float[]{0.0F, 1.0F});
         var2.setDuration(200L);
         var2.start();
      }
   }

   private void registerRemovalUndo(EntityView var1) {
      this.undoStore.registerUndo(var1.getUUID(), new _$$Lambda$PhotoPaintView$tId4geOoF8wmSuxKFoBjX4PhzNE(this, var1));
   }

   private void removeEntity(EntityView var1) {
      EntityView var2 = this.currentEntityView;
      if (var1 == var2) {
         var2.deselect();
         if (this.editingText) {
            this.closeTextEnter(false);
         }

         this.currentEntityView = null;
         this.updateSettingsButton();
      }

      this.entitiesView.removeView(var1);
      this.undoStore.unregisterUndo(var1.getUUID());
   }

   private boolean selectEntity(EntityView var1) {
      EntityView var2 = this.currentEntityView;
      boolean var3;
      if (var2 != null) {
         if (var2 == var1) {
            if (!this.editingText) {
               this.showMenuForEntity(var2);
            }

            return true;
         }

         var2.deselect();
         var3 = true;
      } else {
         var3 = false;
      }

      this.currentEntityView = var1;
      var1 = this.currentEntityView;
      if (var1 != null) {
         var1.select(this.selectionContainerView);
         this.entitiesView.bringViewToFront(this.currentEntityView);
         var1 = this.currentEntityView;
         if (var1 instanceof TextPaintView) {
            this.setCurrentSwatch(((TextPaintView)var1).getSwatch(), true);
         }

         var3 = true;
      }

      this.updateSettingsButton();
      return var3;
   }

   private void setBrush(int var1) {
      RenderView var2 = this.renderView;
      Brush[] var3 = this.brushes;
      this.currentBrush = var1;
      var2.setBrush(var3[var1]);
   }

   private void setCurrentSwatch(Swatch var1, boolean var2) {
      this.renderView.setColor(var1.color);
      this.renderView.setBrushSize(var1.brushWeight);
      if (var2) {
         this.colorPicker.setSwatch(var1);
      }

      EntityView var3 = this.currentEntityView;
      if (var3 instanceof TextPaintView) {
         ((TextPaintView)var3).setSwatch(var1);
      }

   }

   private void setDimVisibility(final boolean var1) {
      ObjectAnimator var2;
      if (var1) {
         this.dimView.setVisibility(0);
         var2 = ObjectAnimator.ofFloat(this.dimView, "alpha", new float[]{0.0F, 1.0F});
      } else {
         var2 = ObjectAnimator.ofFloat(this.dimView, "alpha", new float[]{1.0F, 0.0F});
      }

      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            if (!var1) {
               PhotoPaintView.this.dimView.setVisibility(8);
            }

         }
      });
      var2.setDuration(200L);
      var2.start();
   }

   private void setStroke(boolean var1) {
      this.selectedStroke = var1;
      if (this.currentEntityView instanceof TextPaintView) {
         Swatch var2 = this.colorPicker.getSwatch();
         if (var1 && var2.color == -1) {
            this.setCurrentSwatch(new Swatch(-16777216, 0.85F, var2.brushWeight), true);
         } else if (!var1 && var2.color == -16777216) {
            this.setCurrentSwatch(new Swatch(-1, 1.0F, var2.brushWeight), true);
         }

         ((TextPaintView)this.currentEntityView).setStroke(var1);
      }

   }

   private void setTextDimVisibility(final boolean var1, EntityView var2) {
      if (var1 && var2 != null) {
         ViewGroup var3 = (ViewGroup)var2.getParent();
         if (this.textDimView.getParent() != null) {
            ((EntitiesContainerView)this.textDimView.getParent()).removeView(this.textDimView);
         }

         var3.addView(this.textDimView, var3.indexOfChild(var2));
      }

      var2.setSelectionVisibility(var1 ^ true);
      ObjectAnimator var4;
      if (var1) {
         this.textDimView.setVisibility(0);
         var4 = ObjectAnimator.ofFloat(this.textDimView, "alpha", new float[]{0.0F, 1.0F});
      } else {
         var4 = ObjectAnimator.ofFloat(this.textDimView, "alpha", new float[]{1.0F, 0.0F});
      }

      var4.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            if (!var1) {
               PhotoPaintView.this.textDimView.setVisibility(8);
               if (PhotoPaintView.this.textDimView.getParent() != null) {
                  ((EntitiesContainerView)PhotoPaintView.this.textDimView.getParent()).removeView(PhotoPaintView.this.textDimView);
               }
            }

         }
      });
      var4.setDuration(200L);
      var4.start();
   }

   private void showBrushSettings() {
      this.showPopup(new _$$Lambda$PhotoPaintView$xEc6bzU_od8KaDlhUDbuZvHSrF0(this), this, 85, 0, AndroidUtilities.dp(48.0F));
   }

   private void showMenuForEntity(EntityView var1) {
      int var2 = (int)((var1.getPosition().x - (float)(this.entitiesView.getWidth() / 2)) * this.entitiesView.getScaleX());
      int var3 = (int)((var1.getPosition().y - (float)var1.getHeight() * var1.getScale() / 2.0F - (float)(this.entitiesView.getHeight() / 2)) * this.entitiesView.getScaleY());
      int var4 = AndroidUtilities.dp(32.0F);
      this.showPopup(new _$$Lambda$PhotoPaintView$W57YTvVSDuzEA0lVLhPNgGrpM0g(this, var1), var1, 17, var2, var3 - var4);
   }

   private void showPopup(Runnable var1, View var2, int var3, int var4, int var5) {
      ActionBarPopupWindow var6 = this.popupWindow;
      if (var6 != null && var6.isShowing()) {
         this.popupWindow.dismiss();
      } else {
         if (this.popupLayout == null) {
            this.popupRect = new android.graphics.Rect();
            this.popupLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.getContext());
            this.popupLayout.setAnimationEnabled(false);
            this.popupLayout.setOnTouchListener(new _$$Lambda$PhotoPaintView$zZo9_q6rUClGLuFi2Fkr8DYrX2w(this));
            this.popupLayout.setDispatchKeyEventListener(new _$$Lambda$PhotoPaintView$JREJI5nlVBnmTQKnZtaJEmOzNLY(this));
            this.popupLayout.setShowedFromBotton(true);
         }

         this.popupLayout.removeInnerViews();
         var1.run();
         if (this.popupWindow == null) {
            this.popupWindow = new ActionBarPopupWindow(this.popupLayout, -2, -2);
            this.popupWindow.setAnimationEnabled(false);
            this.popupWindow.setAnimationStyle(2131624110);
            this.popupWindow.setOutsideTouchable(true);
            this.popupWindow.setClippingEnabled(true);
            this.popupWindow.setInputMethodMode(2);
            this.popupWindow.setSoftInputMode(0);
            this.popupWindow.getContentView().setFocusableInTouchMode(true);
            this.popupWindow.setOnDismissListener(new _$$Lambda$PhotoPaintView$67QsJI8M7iQb0wPE_b73gcxw_Ro(this));
         }

         this.popupLayout.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0F), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0F), Integer.MIN_VALUE));
         this.popupWindow.setFocusable(true);
         this.popupWindow.showAtLocation(var2, var3, var4, var5);
         this.popupWindow.startAnimation();
      }
   }

   private void showTextSettings() {
      this.showPopup(new _$$Lambda$PhotoPaintView$buAbz9vEN2US4kAn_glr3fxkqZ8(this), this, 85, 0, AndroidUtilities.dp(48.0F));
   }

   private Point startPositionRelativeToEntity(EntityView var1) {
      Point var5;
      if (var1 != null) {
         var5 = var1.getPosition();
         return new Point(var5.x + 200.0F, var5.y + 200.0F);
      } else {
         var5 = this.centerPositionForEntity();

         while(true) {
            int var2 = 0;

            boolean var3;
            for(var3 = false; var2 < this.entitiesView.getChildCount(); ++var2) {
               View var4 = this.entitiesView.getChildAt(var2);
               if (var4 instanceof EntityView) {
                  Point var6 = ((EntityView)var4).getPosition();
                  if ((float)Math.sqrt(Math.pow((double)(var6.x - var5.x), 2.0D) + Math.pow((double)(var6.y - var5.y), 2.0D)) < 100.0F) {
                     var3 = true;
                  }
               }
            }

            if (!var3) {
               return var5;
            }

            var5 = new Point(var5.x + 200.0F, var5.y + 200.0F);
         }
      }
   }

   private void updateSettingsButton() {
      EntityView var1 = this.currentEntityView;
      int var2 = 2131165746;
      if (var1 != null) {
         if (var1 instanceof StickerView) {
            var2 = 2131165743;
         } else if (var1 instanceof TextPaintView) {
            var2 = 2131165744;
         }

         this.paintButton.setImageResource(2131165745);
         this.paintButton.setColorFilter((ColorFilter)null);
      } else {
         this.paintButton.setColorFilter(new PorterDuffColorFilter(-11420173, Mode.MULTIPLY));
         this.paintButton.setImageResource(2131165745);
      }

      this.colorPicker.setSettingsButtonImage(var2);
   }

   public boolean allowInteraction(EntityView var1) {
      return this.editingText ^ true;
   }

   public void closeTextEnter(boolean var1) {
      if (this.editingText) {
         EntityView var2 = this.currentEntityView;
         if (var2 instanceof TextPaintView) {
            TextPaintView var3 = (TextPaintView)var2;
            this.toolsView.setVisibility(0);
            AndroidUtilities.hideKeyboard(var3.getFocusedView());
            var3.getFocusedView().clearFocus();
            var3.endEditing();
            if (!var1) {
               var3.setText(this.initialText);
            }

            if (var3.getText().trim().length() == 0) {
               this.entitiesView.removeView(var3);
               this.selectEntity((EntityView)null);
            } else {
               var3.setPosition(this.editedTextPosition);
               var3.setRotation(this.editedTextRotation);
               var3.setScale(this.editedTextScale);
               this.editedTextPosition = null;
               this.editedTextRotation = 0.0F;
               this.editedTextScale = 0.0F;
            }

            this.setTextDimVisibility(false, var3);
            this.editingText = false;
            this.initialText = null;
            this.curtainView.setVisibility(8);
         }
      }

   }

   public Bitmap getBitmap() {
      Bitmap var1 = this.renderView.getResultBitmap();
      if (var1 != null && this.entitiesView.entitiesCount() > 0) {
         Canvas var2 = new Canvas(var1);

         for(int var3 = 0; var3 < this.entitiesView.getChildCount(); ++var3) {
            View var4 = this.entitiesView.getChildAt(var3);
            var2.save();
            if (var4 instanceof EntityView) {
               EntityView var5 = (EntityView)var4;
               var2.translate(var5.getPosition().x, var5.getPosition().y);
               var2.scale(var4.getScaleX(), var4.getScaleY());
               var2.rotate(var4.getRotation());
               var2.translate((float)(-var5.getWidth() / 2), (float)(-var5.getHeight() / 2));
               if (var4 instanceof TextPaintView) {
                  Bitmap var8 = Bitmaps.createBitmap(var4.getWidth(), var4.getHeight(), Config.ARGB_8888);
                  Canvas var6 = new Canvas(var8);
                  var4.draw(var6);
                  var2.drawBitmap(var8, (android.graphics.Rect)null, new android.graphics.Rect(0, 0, var8.getWidth(), var8.getHeight()), (Paint)null);

                  try {
                     var6.setBitmap((Bitmap)null);
                  } catch (Exception var7) {
                     FileLog.e((Throwable)var7);
                  }

                  var8.recycle();
               } else {
                  var4.draw(var2);
               }
            }

            var2.restore();
         }
      }

      return var1;
   }

   public TextView getCancelTextView() {
      return this.cancelTextView;
   }

   public ColorPicker getColorPicker() {
      return this.colorPicker;
   }

   public TextView getDoneTextView() {
      return this.doneTextView;
   }

   public ArrayList getMasks() {
      int var1 = this.entitiesView.getChildCount();
      ArrayList var2 = null;

      ArrayList var5;
      for(int var3 = 0; var3 < var1; var2 = var5) {
         View var4 = this.entitiesView.getChildAt(var3);
         var5 = var2;
         if (var4 instanceof StickerView) {
            TLRPC.Document var7 = ((StickerView)var4).getSticker();
            var5 = var2;
            if (var2 == null) {
               var5 = new ArrayList();
            }

            TLRPC.TL_inputDocument var6 = new TLRPC.TL_inputDocument();
            var6.id = var7.id;
            var6.access_hash = var7.access_hash;
            var6.file_reference = var7.file_reference;
            if (var6.file_reference == null) {
               var6.file_reference = new byte[0];
            }

            var5.add(var6);
         }

         ++var3;
      }

      return var2;
   }

   public FrameLayout getToolsView() {
      return this.toolsView;
   }

   public void init() {
      this.renderView.setVisibility(0);
   }

   // $FF: synthetic method
   public void lambda$buttonForBrush$12$PhotoPaintView(int var1, View var2) {
      this.setBrush(var1);
      ActionBarPopupWindow var3 = this.popupWindow;
      if (var3 != null && var3.isShowing()) {
         this.popupWindow.dismiss(true);
      }

   }

   // $FF: synthetic method
   public void lambda$buttonForText$14$PhotoPaintView(boolean var1, View var2) {
      this.setStroke(var1);
      ActionBarPopupWindow var3 = this.popupWindow;
      if (var3 != null && var3.isShowing()) {
         this.popupWindow.dismiss(true);
      }

   }

   // $FF: synthetic method
   public void lambda$new$0$PhotoPaintView() {
      this.colorPicker.setUndoEnabled(this.undoStore.canUndo());
   }

   // $FF: synthetic method
   public void lambda$new$1$PhotoPaintView(View var1) {
      this.closeTextEnter(true);
   }

   // $FF: synthetic method
   public void lambda$new$2$PhotoPaintView(View var1) {
      this.selectEntity((EntityView)null);
   }

   // $FF: synthetic method
   public void lambda$new$3$PhotoPaintView(View var1) {
      this.openStickersView();
   }

   // $FF: synthetic method
   public void lambda$new$4$PhotoPaintView(View var1) {
      this.createText();
   }

   // $FF: synthetic method
   public void lambda$null$10$PhotoPaintView(View var1) {
      this.duplicateSelectedEntity();
      ActionBarPopupWindow var2 = this.popupWindow;
      if (var2 != null && var2.isShowing()) {
         this.popupWindow.dismiss(true);
      }

   }

   // $FF: synthetic method
   public void lambda$null$8$PhotoPaintView(EntityView var1, View var2) {
      this.removeEntity(var1);
      ActionBarPopupWindow var3 = this.popupWindow;
      if (var3 != null && var3.isShowing()) {
         this.popupWindow.dismiss(true);
      }

   }

   // $FF: synthetic method
   public void lambda$null$9$PhotoPaintView(View var1) {
      this.editSelectedTextEntity();
      ActionBarPopupWindow var2 = this.popupWindow;
      if (var2 != null && var2.isShowing()) {
         this.popupWindow.dismiss(true);
      }

   }

   // $FF: synthetic method
   public void lambda$registerRemovalUndo$7$PhotoPaintView(EntityView var1) {
      this.removeEntity(var1);
   }

   // $FF: synthetic method
   public void lambda$showBrushSettings$13$PhotoPaintView() {
      int var1 = this.currentBrush;
      boolean var2 = false;
      boolean var3;
      if (var1 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      FrameLayout var4 = this.buttonForBrush(0, 2131165732, var3);
      this.popupLayout.addView(var4);
      LayoutParams var5 = (LayoutParams)var4.getLayoutParams();
      var5.width = -1;
      var5.height = AndroidUtilities.dp(52.0F);
      var4.setLayoutParams(var5);
      if (this.currentBrush == 1) {
         var3 = true;
      } else {
         var3 = false;
      }

      var4 = this.buttonForBrush(1, 2131165728, var3);
      this.popupLayout.addView(var4);
      var5 = (LayoutParams)var4.getLayoutParams();
      var5.width = -1;
      var5.height = AndroidUtilities.dp(52.0F);
      var4.setLayoutParams(var5);
      var3 = var2;
      if (this.currentBrush == 2) {
         var3 = true;
      }

      var4 = this.buttonForBrush(2, 2131165730, var3);
      this.popupLayout.addView(var4);
      var5 = (LayoutParams)var4.getLayoutParams();
      var5.width = -1;
      var5.height = AndroidUtilities.dp(52.0F);
      var4.setLayoutParams(var5);
   }

   // $FF: synthetic method
   public void lambda$showMenuForEntity$11$PhotoPaintView(EntityView var1) {
      LinearLayout var2 = new LinearLayout(this.getContext());
      var2.setOrientation(0);
      TextView var3 = new TextView(this.getContext());
      var3.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
      var3.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      var3.setGravity(16);
      var3.setPadding(AndroidUtilities.dp(16.0F), 0, AndroidUtilities.dp(14.0F), 0);
      var3.setTextSize(1, 18.0F);
      var3.setTag(0);
      var3.setText(LocaleController.getString("PaintDelete", 2131560155));
      var3.setOnClickListener(new _$$Lambda$PhotoPaintView$sYCjwRMr_S5_hGejNJaJoYD8A4Q(this, var1));
      var2.addView(var3, LayoutHelper.createLinear(-2, 48));
      TextView var4;
      if (var1 instanceof TextPaintView) {
         var4 = new TextView(this.getContext());
         var4.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
         var4.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         var4.setGravity(16);
         var4.setPadding(AndroidUtilities.dp(16.0F), 0, AndroidUtilities.dp(16.0F), 0);
         var4.setTextSize(1, 18.0F);
         var4.setTag(1);
         var4.setText(LocaleController.getString("PaintEdit", 2131560157));
         var4.setOnClickListener(new _$$Lambda$PhotoPaintView$_rzL0nO_ZFC3zc_eG729dIHU_X0(this));
         var2.addView(var4, LayoutHelper.createLinear(-2, 48));
      }

      var4 = new TextView(this.getContext());
      var4.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
      var4.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      var4.setGravity(16);
      var4.setPadding(AndroidUtilities.dp(14.0F), 0, AndroidUtilities.dp(16.0F), 0);
      var4.setTextSize(1, 18.0F);
      var4.setTag(2);
      var4.setText(LocaleController.getString("PaintDuplicate", 2131560156));
      var4.setOnClickListener(new _$$Lambda$PhotoPaintView$mPRLCtV6_7XvBSiDh7u_Mg52bn8(this));
      var2.addView(var4, LayoutHelper.createLinear(-2, 48));
      this.popupLayout.addView(var2);
      LayoutParams var5 = (LayoutParams)var2.getLayoutParams();
      var5.width = -2;
      var5.height = -2;
      var2.setLayoutParams(var5);
   }

   // $FF: synthetic method
   public boolean lambda$showPopup$16$PhotoPaintView(View var1, MotionEvent var2) {
      if (var2.getActionMasked() == 0) {
         ActionBarPopupWindow var3 = this.popupWindow;
         if (var3 != null && var3.isShowing()) {
            var1.getHitRect(this.popupRect);
            if (!this.popupRect.contains((int)var2.getX(), (int)var2.getY())) {
               this.popupWindow.dismiss();
            }
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$showPopup$17$PhotoPaintView(KeyEvent var1) {
      if (var1.getKeyCode() == 4 && var1.getRepeatCount() == 0) {
         ActionBarPopupWindow var2 = this.popupWindow;
         if (var2 != null && var2.isShowing()) {
            this.popupWindow.dismiss();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$showPopup$18$PhotoPaintView() {
      this.popupLayout.removeInnerViews();
   }

   // $FF: synthetic method
   public void lambda$showTextSettings$15$PhotoPaintView() {
      FrameLayout var1 = this.buttonForText(true, LocaleController.getString("PaintOutlined", 2131560158), this.selectedStroke);
      this.popupLayout.addView(var1);
      LayoutParams var2 = (LayoutParams)var1.getLayoutParams();
      var2.width = -1;
      var2.height = AndroidUtilities.dp(48.0F);
      var1.setLayoutParams(var2);
      FrameLayout var4 = this.buttonForText(false, LocaleController.getString("PaintRegular", 2131560159), this.selectedStroke ^ true);
      this.popupLayout.addView(var4);
      LayoutParams var3 = (LayoutParams)var4.getLayoutParams();
      var3.width = -1;
      var3.height = AndroidUtilities.dp(48.0F);
      var4.setLayoutParams(var3);
   }

   public void maybeShowDismissalAlert(PhotoViewer var1, Activity var2, Runnable var3) {
      if (this.editingText) {
         this.closeTextEnter(false);
      } else if (this.pickingSticker) {
         this.closeStickersView();
      } else {
         if (this.hasChanges()) {
            if (var2 == null) {
               return;
            }

            AlertDialog.Builder var4 = new AlertDialog.Builder(var2);
            var4.setMessage(LocaleController.getString("DiscardChanges", 2131559273));
            var4.setTitle(LocaleController.getString("AppName", 2131558635));
            var4.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PhotoPaintView$zLiJ6Xhod_3MqoXM9DwNMRgBw8Y(var3));
            var4.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            var1.showAlertDialog(var4);
         } else {
            var3.run();
         }

      }
   }

   public boolean onEntityLongClicked(EntityView var1) {
      this.showMenuForEntity(var1);
      return true;
   }

   public boolean onEntitySelected(EntityView var1) {
      return this.selectEntity(var1);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var4 -= var2;
      var5 -= var3;
      if (VERSION.SDK_INT >= 21) {
         var2 = AndroidUtilities.statusBarHeight;
      } else {
         var2 = 0;
      }

      var3 = ActionBar.getCurrentActionBarHeight();
      int var6 = ActionBar.getCurrentActionBarHeight() + var2;
      int var7 = AndroidUtilities.displaySize.y - var3 - AndroidUtilities.dp(48.0F);
      float var8;
      float var9;
      if (this.bitmapToEdit != null) {
         if (this.isSidewardOrientation()) {
            var3 = this.bitmapToEdit.getHeight();
         } else {
            var3 = this.bitmapToEdit.getWidth();
         }

         var8 = (float)var3;
         if (this.isSidewardOrientation()) {
            var3 = this.bitmapToEdit.getWidth();
         } else {
            var3 = this.bitmapToEdit.getHeight();
         }

         var9 = (float)var3;
      } else {
         var8 = (float)var4;
         var9 = (float)(var5 - var3 - AndroidUtilities.dp(48.0F));
      }

      float var10 = (float)var4;
      float var11 = (float)Math.floor((double)(var10 * var9 / var8));
      float var12 = (float)var7;
      if (var11 > var12) {
         var10 = (float)Math.floor((double)(var12 * var8 / var9));
      }

      var3 = (int)Math.ceil((double)((var4 - this.renderView.getMeasuredWidth()) / 2));
      int var13 = (var5 - var6 - AndroidUtilities.dp(48.0F) - this.renderView.getMeasuredHeight()) / 2 + var6 - ActionBar.getCurrentActionBarHeight() + AndroidUtilities.dp(8.0F);
      RenderView var14 = this.renderView;
      var14.layout(var3, var13, var14.getMeasuredWidth() + var3, this.renderView.getMeasuredHeight() + var13);
      var8 = var10 / this.paintingSize.width;
      this.entitiesView.setScaleX(var8);
      this.entitiesView.setScaleY(var8);
      EntitiesContainerView var15 = this.entitiesView;
      var15.layout(var3, var13, var15.getMeasuredWidth() + var3, this.entitiesView.getMeasuredHeight() + var13);
      FrameLayout var16 = this.dimView;
      var16.layout(0, var2, var16.getMeasuredWidth(), this.dimView.getMeasuredHeight() + var2);
      var16 = this.selectionContainerView;
      var16.layout(0, var2, var16.getMeasuredWidth(), this.selectionContainerView.getMeasuredHeight() + var2);
      ColorPicker var17 = this.colorPicker;
      var17.layout(0, var6, var17.getMeasuredWidth(), this.colorPicker.getMeasuredHeight() + var6);
      var16 = this.toolsView;
      var16.layout(0, var5 - var16.getMeasuredHeight(), this.toolsView.getMeasuredWidth(), var5);
      this.curtainView.layout(0, 0, var4, var7);
      StickerMasksView var18 = this.stickersView;
      if (var18 != null) {
         var18.layout(0, var2, var18.getMeasuredWidth(), this.stickersView.getMeasuredHeight() + var2);
      }

      EntityView var19 = this.currentEntityView;
      if (var19 != null) {
         var19.updateSelectionView();
         this.currentEntityView.setOffset(this.entitiesView.getLeft() - this.selectionContainerView.getLeft(), this.entitiesView.getTop() - this.selectionContainerView.getTop());
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var1);
      var2 = MeasureSpec.getSize(var2);
      this.setMeasuredDimension(var3, var2);
      int var4 = AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(48.0F);
      float var5;
      float var6;
      if (this.bitmapToEdit != null) {
         if (this.isSidewardOrientation()) {
            var2 = this.bitmapToEdit.getHeight();
         } else {
            var2 = this.bitmapToEdit.getWidth();
         }

         var5 = (float)var2;
         if (this.isSidewardOrientation()) {
            var2 = this.bitmapToEdit.getWidth();
         } else {
            var2 = this.bitmapToEdit.getHeight();
         }

         var6 = (float)var2;
      } else {
         var5 = (float)var3;
         var6 = (float)(var2 - ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(48.0F));
      }

      float var7 = (float)var3;
      float var8 = (float)Math.floor((double)(var7 * var6 / var5));
      float var9 = (float)var4;
      float var10 = var8;
      if (var8 > var9) {
         var7 = (float)Math.floor((double)(var5 * var9 / var6));
         var10 = var9;
      }

      this.renderView.measure(MeasureSpec.makeMeasureSpec((int)var7, 1073741824), MeasureSpec.makeMeasureSpec((int)var10, 1073741824));
      this.entitiesView.measure(MeasureSpec.makeMeasureSpec((int)this.paintingSize.width, 1073741824), MeasureSpec.makeMeasureSpec((int)this.paintingSize.height, 1073741824));
      this.dimView.measure(var1, MeasureSpec.makeMeasureSpec(var4, Integer.MIN_VALUE));
      this.selectionContainerView.measure(var1, MeasureSpec.makeMeasureSpec(var4, 1073741824));
      this.colorPicker.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var4, 1073741824));
      this.toolsView.measure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
      StickerMasksView var11 = this.stickersView;
      if (var11 != null) {
         var11.measure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, 1073741824));
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (this.currentEntityView != null) {
         if (this.editingText) {
            this.closeTextEnter(true);
         } else {
            this.selectEntity((EntityView)null);
         }
      }

      return true;
   }

   public void shutdown() {
      this.renderView.shutdown();
      this.entitiesView.setVisibility(8);
      this.selectionContainerView.setVisibility(8);
      this.queue.postRunnable(_$$Lambda$PhotoPaintView$BRjjxWV4_4jng8wjr2bRzoAFJ_A.INSTANCE);
   }

   private class StickerPosition {
      private float angle;
      private Point position;
      private float scale;

      StickerPosition(Point var2, float var3, float var4) {
         this.position = var2;
         this.scale = var3;
         this.angle = var4;
      }
   }
}

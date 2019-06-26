// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ActionBar;
import android.os.Build$VERSION;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.app.Activity;
import org.telegram.ui.PhotoViewer;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.LinearLayout$LayoutParams;
import java.util.ArrayList;
import org.telegram.messenger.FileLog;
import android.graphics.Paint;
import org.telegram.messenger.Bitmaps;
import android.graphics.Bitmap$Config;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.ColorFilter;
import android.view.View$MeasureSpec;
import android.widget.PopupWindow$OnDismissListener;
import android.view.View$OnTouchListener;
import android.view.ViewGroup;
import android.os.Looper;
import android.content.DialogInterface;
import org.telegram.messenger.ApplicationLoader;
import android.view.inputmethod.InputMethodManager;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import org.telegram.ui.Components.Paint.Views.EditTextOutline;
import org.telegram.ui.Components.Paint.Swatch;
import org.telegram.tgnet.TLRPC;
import android.widget.ImageView$ScaleType;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Paint.Views.StickerView;
import org.telegram.ui.Components.Paint.Views.TextPaintView;
import android.view.MotionEvent;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.Paint.Painting;
import android.view.View;
import android.content.Context;
import org.telegram.ui.Components.Paint.UndoStore;
import org.telegram.ui.Components.Paint.RenderView;
import org.telegram.messenger.DispatchQueue;
import android.graphics.Rect;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import android.widget.ImageView;
import org.telegram.ui.Components.Paint.Views.EntitiesContainerView;
import android.animation.Animator;
import org.telegram.ui.Components.Paint.Views.ColorPicker;
import android.widget.TextView;
import org.telegram.ui.Components.Paint.Brush;
import android.graphics.Bitmap;
import android.annotation.SuppressLint;
import org.telegram.ui.Components.Paint.Views.EntityView;
import android.widget.FrameLayout;

@SuppressLint({ "NewApi" })
public class PhotoPaintView extends FrameLayout implements EntityViewDelegate
{
    private static final int gallery_menu_done = 1;
    private Bitmap bitmapToEdit;
    private Brush[] brushes;
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
    private Rect popupRect;
    private ActionBarPopupWindow popupWindow;
    private DispatchQueue queue;
    private RenderView renderView;
    private boolean selectedStroke;
    private FrameLayout selectionContainerView;
    private StickerMasksView stickersView;
    private FrameLayout textDimView;
    private FrameLayout toolsView;
    private UndoStore undoStore;
    
    public PhotoPaintView(final Context context, final Bitmap bitmapToEdit, final int orientation) {
        super(context);
        this.brushes = new Brush[] { new Brush.Radial(), new Brush.Elliptical(), new Brush.Neon() };
        this.selectedStroke = true;
        this.queue = new DispatchQueue("Paint");
        this.bitmapToEdit = bitmapToEdit;
        this.orientation = orientation;
        (this.undoStore = new UndoStore()).setDelegate((UndoStore.UndoStoreDelegate)new _$$Lambda$PhotoPaintView$R6Re0Kk5HfUX12qMoexuM_2zbtQ(this));
        (this.curtainView = new FrameLayout(context)).setBackgroundColor(-16777216);
        this.curtainView.setVisibility(4);
        this.addView((View)this.curtainView);
        (this.renderView = new RenderView(context, new Painting(this.getPaintingSize()), bitmapToEdit, this.orientation)).setDelegate((RenderView.RenderViewDelegate)new RenderView.RenderViewDelegate() {
            @Override
            public void onBeganDrawing() {
                if (PhotoPaintView.this.currentEntityView != null) {
                    PhotoPaintView.this.selectEntity(null);
                }
            }
            
            @Override
            public void onFinishedDrawing(final boolean b) {
                PhotoPaintView.this.colorPicker.setUndoEnabled(PhotoPaintView.this.undoStore.canUndo());
            }
            
            @Override
            public boolean shouldDraw() {
                final boolean b = PhotoPaintView.this.currentEntityView == null;
                if (!b) {
                    PhotoPaintView.this.selectEntity(null);
                }
                return b;
            }
        });
        this.renderView.setUndoStore(this.undoStore);
        this.renderView.setQueue(this.queue);
        this.renderView.setVisibility(4);
        this.renderView.setBrush(this.brushes[0]);
        this.addView((View)this.renderView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        (this.entitiesView = new EntitiesContainerView(context, (EntitiesContainerView.EntitiesContainerViewDelegate)new EntitiesContainerView.EntitiesContainerViewDelegate() {
            @Override
            public void onEntityDeselect() {
                PhotoPaintView.this.selectEntity(null);
            }
            
            @Override
            public EntityView onSelectedEntityRequest() {
                return PhotoPaintView.this.currentEntityView;
            }
            
            @Override
            public boolean shouldReceiveTouches() {
                return PhotoPaintView.this.textDimView.getVisibility() != 0;
            }
        })).setPivotX(0.0f);
        this.entitiesView.setPivotY(0.0f);
        this.addView((View)this.entitiesView);
        (this.dimView = new FrameLayout(context)).setAlpha(0.0f);
        this.dimView.setBackgroundColor(1711276032);
        this.dimView.setVisibility(8);
        this.addView((View)this.dimView);
        (this.textDimView = new FrameLayout(context)).setAlpha(0.0f);
        this.textDimView.setBackgroundColor(1711276032);
        this.textDimView.setVisibility(8);
        this.textDimView.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoPaintView$ZykewOMvdivcJ4QbiFmia3gfIL0(this));
        this.addView((View)(this.selectionContainerView = new FrameLayout(context) {
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return false;
            }
        }));
        this.addView((View)(this.colorPicker = new ColorPicker(context)));
        this.colorPicker.setDelegate((ColorPicker.ColorPickerDelegate)new ColorPicker.ColorPickerDelegate() {
            @Override
            public void onBeganColorPicking() {
                if (!(PhotoPaintView.this.currentEntityView instanceof TextPaintView)) {
                    PhotoPaintView.this.setDimVisibility(true);
                }
            }
            
            @Override
            public void onColorValueChanged() {
                final PhotoPaintView this$0 = PhotoPaintView.this;
                this$0.setCurrentSwatch(this$0.colorPicker.getSwatch(), false);
            }
            
            @Override
            public void onFinishedColorPicking() {
                final PhotoPaintView this$0 = PhotoPaintView.this;
                this$0.setCurrentSwatch(this$0.colorPicker.getSwatch(), false);
                if (!(PhotoPaintView.this.currentEntityView instanceof TextPaintView)) {
                    PhotoPaintView.this.setDimVisibility(false);
                }
            }
            
            @Override
            public void onSettingsPressed() {
                if (PhotoPaintView.this.currentEntityView != null) {
                    if (PhotoPaintView.this.currentEntityView instanceof StickerView) {
                        PhotoPaintView.this.mirrorSticker();
                    }
                    else if (PhotoPaintView.this.currentEntityView instanceof TextPaintView) {
                        PhotoPaintView.this.showTextSettings();
                    }
                }
                else {
                    PhotoPaintView.this.showBrushSettings();
                }
            }
            
            @Override
            public void onUndoPressed() {
                PhotoPaintView.this.undoStore.undo();
            }
        });
        (this.toolsView = new FrameLayout(context)).setBackgroundColor(-16777216);
        this.addView((View)this.toolsView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        (this.cancelTextView = new TextView(context)).setTextSize(1, 14.0f);
        this.cancelTextView.setTextColor(-1);
        this.cancelTextView.setGravity(17);
        this.cancelTextView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
        this.cancelTextView.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.cancelTextView.setText((CharSequence)LocaleController.getString("Cancel", 2131558891).toUpperCase());
        this.cancelTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.toolsView.addView((View)this.cancelTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
        (this.doneTextView = new TextView(context)).setTextSize(1, 14.0f);
        this.doneTextView.setTextColor(-11420173);
        this.doneTextView.setGravity(17);
        this.doneTextView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
        this.doneTextView.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.doneTextView.setText((CharSequence)LocaleController.getString("Done", 2131559299).toUpperCase());
        this.doneTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.toolsView.addView((View)this.doneTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 53));
        (this.paintButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.paintButton.setImageResource(2131165745);
        this.paintButton.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        this.toolsView.addView((View)this.paintButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(54, -1.0f, 17, 0.0f, 0.0f, 56.0f, 0.0f));
        this.paintButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoPaintView$rf515UpLc_844_0_ExvqyyhAN3g(this));
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        imageView.setImageResource(2131165749);
        imageView.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        this.toolsView.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(54, -1, 17));
        imageView.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoPaintView$bh1PtB_nRqdfutcf4y5QOEZjU74(this));
        final ImageView imageView2 = new ImageView(context);
        imageView2.setScaleType(ImageView$ScaleType.CENTER);
        imageView2.setImageResource(2131165747);
        imageView2.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        this.toolsView.addView((View)imageView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(54, -1.0f, 17, 56.0f, 0.0f, 0.0f, 0.0f));
        imageView2.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoPaintView$SEafmfD5xvHbkIUST7pqiumb7sI(this));
        this.colorPicker.setUndoEnabled(false);
        this.setCurrentSwatch(this.colorPicker.getSwatch(), false);
        this.updateSettingsButton();
    }
    
    private int baseFontSize() {
        return (int)(this.getPaintingSize().width / 9.0f);
    }
    
    private Size baseStickerSize() {
        final double v = this.getPaintingSize().width;
        Double.isNaN(v);
        final float n = (float)Math.floor(v * 0.5);
        return new Size(n, n);
    }
    
    private FrameLayout buttonForBrush(final int n, final int imageResource, final boolean b) {
        final FrameLayout frameLayout = new FrameLayout(this.getContext());
        frameLayout.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        frameLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoPaintView$gGzFKkJs5bEvFPWs_vHHaN5jxJY(this, n));
        final ImageView imageView = new ImageView(this.getContext());
        imageView.setImageResource(imageResource);
        frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(165, 44.0f, 19, 46.0f, 0.0f, 8.0f, 0.0f));
        if (b) {
            final ImageView imageView2 = new ImageView(this.getContext());
            imageView2.setImageResource(2131165412);
            imageView2.setScaleType(ImageView$ScaleType.CENTER);
            frameLayout.addView((View)imageView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(50, -1.0f));
        }
        return frameLayout;
    }
    
    private FrameLayout buttonForText(final boolean b, final String text, final boolean b2) {
        final FrameLayout frameLayout = new FrameLayout(this.getContext()) {
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                return true;
            }
        };
        frameLayout.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        frameLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$PhotoPaintView$Ooj6P__OHnWplT4W_LITMgEHYP8(this, b));
        final EditTextOutline editTextOutline = new EditTextOutline(this.getContext());
        editTextOutline.setBackgroundColor(0);
        editTextOutline.setEnabled(false);
        editTextOutline.setStrokeWidth((float)AndroidUtilities.dp(3.0f));
        final int n = -16777216;
        int textColor;
        if (b) {
            textColor = -1;
        }
        else {
            textColor = -16777216;
        }
        editTextOutline.setTextColor(textColor);
        int strokeColor;
        if (b) {
            strokeColor = n;
        }
        else {
            strokeColor = 0;
        }
        editTextOutline.setStrokeColor(strokeColor);
        editTextOutline.setPadding(AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(2.0f), 0);
        editTextOutline.setTextSize(1, 18.0f);
        editTextOutline.setTypeface((Typeface)null, 1);
        editTextOutline.setTag((Object)b);
        editTextOutline.setText((CharSequence)text);
        frameLayout.addView((View)editTextOutline, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 19, 46.0f, 0.0f, 16.0f, 0.0f));
        if (b2) {
            final ImageView imageView = new ImageView(this.getContext());
            imageView.setImageResource(2131165412);
            imageView.setScaleType(ImageView$ScaleType.CENTER);
            frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(50, -1.0f));
        }
        return frameLayout;
    }
    
    private StickerPosition calculateStickerPosition(final TLRPC.Document document) {
        for (int i = 0; i < document.attributes.size(); ++i) {
            final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                final TLRPC.TL_maskCoords mask_coords = documentAttribute.mask_coords;
                break;
            }
        }
        return new StickerPosition(this.centerPositionForEntity(), 0.75f, 0.0f);
    }
    
    private Point centerPositionForEntity() {
        final Size paintingSize = this.getPaintingSize();
        return new Point(paintingSize.width / 2.0f, paintingSize.height / 2.0f);
    }
    
    private void closeStickersView() {
        final StickerMasksView stickersView = this.stickersView;
        if (stickersView != null) {
            if (stickersView.getVisibility() == 0) {
                this.pickingSticker = false;
                final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.stickersView, "alpha", new float[] { 1.0f, 0.0f });
                ofFloat.setDuration(200L);
                ((Animator)ofFloat).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        PhotoPaintView.this.stickersView.setVisibility(8);
                    }
                });
                ofFloat.start();
            }
        }
    }
    
    private void createSticker(final Object o, final TLRPC.Document document) {
        final StickerPosition calculateStickerPosition = this.calculateStickerPosition(document);
        final StickerView stickerView = new StickerView(this.getContext(), calculateStickerPosition.position, calculateStickerPosition.angle, calculateStickerPosition.scale, this.baseStickerSize(), document, o);
        stickerView.setDelegate((EntityView.EntityViewDelegate)this);
        this.entitiesView.addView((View)stickerView);
        this.registerRemovalUndo(stickerView);
        this.selectEntity(stickerView);
    }
    
    private void createText() {
        final Swatch swatch = this.colorPicker.getSwatch();
        Swatch swatch2 = new Swatch(-1, 1.0f, swatch.brushWeight);
        final Swatch swatch3 = new Swatch(-16777216, 0.85f, swatch.brushWeight);
        if (this.selectedStroke) {
            swatch2 = swatch3;
        }
        this.setCurrentSwatch(swatch2, true);
        final TextPaintView textPaintView = new TextPaintView(this.getContext(), this.startPositionRelativeToEntity(null), this.baseFontSize(), "", this.colorPicker.getSwatch(), this.selectedStroke);
        textPaintView.setDelegate((EntityView.EntityViewDelegate)this);
        textPaintView.setMaxWidth((int)(this.getPaintingSize().width - 20.0f));
        this.entitiesView.addView((View)textPaintView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f));
        this.registerRemovalUndo(textPaintView);
        this.selectEntity(textPaintView);
        this.editSelectedTextEntity();
    }
    
    private void duplicateSelectedEntity() {
        final EntityView currentEntityView = this.currentEntityView;
        if (currentEntityView == null) {
            return;
        }
        EntityView entityView = null;
        final Point startPositionRelativeToEntity = this.startPositionRelativeToEntity(currentEntityView);
        final EntityView currentEntityView2 = this.currentEntityView;
        if (currentEntityView2 instanceof StickerView) {
            entityView = new StickerView(this.getContext(), (StickerView)this.currentEntityView, startPositionRelativeToEntity);
            entityView.setDelegate((EntityView.EntityViewDelegate)this);
            this.entitiesView.addView((View)entityView);
        }
        else if (currentEntityView2 instanceof TextPaintView) {
            entityView = new TextPaintView(this.getContext(), (TextPaintView)this.currentEntityView, startPositionRelativeToEntity);
            entityView.setDelegate((EntityView.EntityViewDelegate)this);
            ((TextPaintView)entityView).setMaxWidth((int)(this.getPaintingSize().width - 20.0f));
            this.entitiesView.addView((View)entityView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f));
        }
        this.registerRemovalUndo(entityView);
        this.selectEntity(entityView);
        this.updateSettingsButton();
    }
    
    private void editSelectedTextEntity() {
        if (this.currentEntityView instanceof TextPaintView) {
            if (!this.editingText) {
                this.curtainView.setVisibility(0);
                final TextPaintView textPaintView = (TextPaintView)this.currentEntityView;
                this.initialText = textPaintView.getText();
                this.editingText = true;
                this.editedTextPosition = textPaintView.getPosition();
                this.editedTextRotation = textPaintView.getRotation();
                this.editedTextScale = textPaintView.getScale();
                textPaintView.setPosition(this.centerPositionForEntity());
                textPaintView.setRotation(0.0f);
                textPaintView.setScale(1.0f);
                this.toolsView.setVisibility(8);
                this.setTextDimVisibility(true, textPaintView);
                textPaintView.beginEditing();
                ((InputMethodManager)ApplicationLoader.applicationContext.getSystemService("input_method")).toggleSoftInputFromWindow(textPaintView.getFocusedView().getWindowToken(), 2, 0);
            }
        }
    }
    
    private Size getPaintingSize() {
        final Size paintingSize = this.paintingSize;
        if (paintingSize != null) {
            return paintingSize;
        }
        int n;
        if (this.isSidewardOrientation()) {
            n = this.bitmapToEdit.getHeight();
        }
        else {
            n = this.bitmapToEdit.getWidth();
        }
        final float n2 = (float)n;
        int n3;
        if (this.isSidewardOrientation()) {
            n3 = this.bitmapToEdit.getWidth();
        }
        else {
            n3 = this.bitmapToEdit.getHeight();
        }
        final float n4 = (float)n3;
        final Size paintingSize2 = new Size(n2, n4);
        paintingSize2.width = 1280.0f;
        paintingSize2.height = (float)Math.floor(paintingSize2.width * n4 / n2);
        if (paintingSize2.height > 1280.0f) {
            paintingSize2.height = 1280.0f;
            paintingSize2.width = (float)Math.floor(paintingSize2.height * n2 / n4);
        }
        return this.paintingSize = paintingSize2;
    }
    
    private boolean hasChanges() {
        return this.undoStore.canUndo() || this.entitiesView.entitiesCount() > 0;
    }
    
    private boolean isSidewardOrientation() {
        final int orientation = this.orientation;
        return orientation % 360 == 90 || orientation % 360 == 270;
    }
    
    private void mirrorSticker() {
        final EntityView currentEntityView = this.currentEntityView;
        if (currentEntityView instanceof StickerView) {
            ((StickerView)currentEntityView).mirror();
        }
    }
    
    private void openStickersView() {
        final StickerMasksView stickersView = this.stickersView;
        if (stickersView != null && stickersView.getVisibility() == 0) {
            return;
        }
        this.pickingSticker = true;
        if (this.stickersView == null) {
            (this.stickersView = new StickerMasksView(this.getContext())).setListener((StickerMasksView.Listener)new StickerMasksView.Listener() {
                @Override
                public void onStickerSelected(final Object o, final TLRPC.Document document) {
                    PhotoPaintView.this.closeStickersView();
                    PhotoPaintView.this.createSticker(o, document);
                }
                
                @Override
                public void onTypeChanged() {
                }
            });
            this.addView((View)this.stickersView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        }
        this.stickersView.setVisibility(0);
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.stickersView, "alpha", new float[] { 0.0f, 1.0f });
        ofFloat.setDuration(200L);
        ofFloat.start();
    }
    
    private void registerRemovalUndo(final EntityView entityView) {
        this.undoStore.registerUndo(entityView.getUUID(), new _$$Lambda$PhotoPaintView$tId4geOoF8wmSuxKFoBjX4PhzNE(this, entityView));
    }
    
    private void removeEntity(final EntityView entityView) {
        final EntityView currentEntityView = this.currentEntityView;
        if (entityView == currentEntityView) {
            currentEntityView.deselect();
            if (this.editingText) {
                this.closeTextEnter(false);
            }
            this.currentEntityView = null;
            this.updateSettingsButton();
        }
        this.entitiesView.removeView((View)entityView);
        this.undoStore.unregisterUndo(entityView.getUUID());
    }
    
    private boolean selectEntity(EntityView currentEntityView) {
        final EntityView currentEntityView2 = this.currentEntityView;
        boolean b;
        if (currentEntityView2 != null) {
            if (currentEntityView2 == currentEntityView) {
                if (!this.editingText) {
                    this.showMenuForEntity(currentEntityView2);
                }
                return true;
            }
            currentEntityView2.deselect();
            b = true;
        }
        else {
            b = false;
        }
        this.currentEntityView = currentEntityView;
        currentEntityView = this.currentEntityView;
        if (currentEntityView != null) {
            currentEntityView.select((ViewGroup)this.selectionContainerView);
            this.entitiesView.bringViewToFront(this.currentEntityView);
            currentEntityView = this.currentEntityView;
            if (currentEntityView instanceof TextPaintView) {
                this.setCurrentSwatch(((TextPaintView)currentEntityView).getSwatch(), true);
            }
            b = true;
        }
        this.updateSettingsButton();
        return b;
    }
    
    private void setBrush(final int currentBrush) {
        final RenderView renderView = this.renderView;
        final Brush[] brushes = this.brushes;
        this.currentBrush = currentBrush;
        renderView.setBrush(brushes[currentBrush]);
    }
    
    private void setCurrentSwatch(final Swatch swatch, final boolean b) {
        this.renderView.setColor(swatch.color);
        this.renderView.setBrushSize(swatch.brushWeight);
        if (b) {
            this.colorPicker.setSwatch(swatch);
        }
        final EntityView currentEntityView = this.currentEntityView;
        if (currentEntityView instanceof TextPaintView) {
            ((TextPaintView)currentEntityView).setSwatch(swatch);
        }
    }
    
    private void setDimVisibility(final boolean b) {
        ObjectAnimator objectAnimator;
        if (b) {
            this.dimView.setVisibility(0);
            objectAnimator = ObjectAnimator.ofFloat((Object)this.dimView, "alpha", new float[] { 0.0f, 1.0f });
        }
        else {
            objectAnimator = ObjectAnimator.ofFloat((Object)this.dimView, "alpha", new float[] { 1.0f, 0.0f });
        }
        ((Animator)objectAnimator).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (!b) {
                    PhotoPaintView.this.dimView.setVisibility(8);
                }
            }
        });
        objectAnimator.setDuration(200L);
        objectAnimator.start();
    }
    
    private void setStroke(final boolean b) {
        this.selectedStroke = b;
        if (this.currentEntityView instanceof TextPaintView) {
            final Swatch swatch = this.colorPicker.getSwatch();
            if (b && swatch.color == -1) {
                this.setCurrentSwatch(new Swatch(-16777216, 0.85f, swatch.brushWeight), true);
            }
            else if (!b && swatch.color == -16777216) {
                this.setCurrentSwatch(new Swatch(-1, 1.0f, swatch.brushWeight), true);
            }
            ((TextPaintView)this.currentEntityView).setStroke(b);
        }
    }
    
    private void setTextDimVisibility(final boolean b, final EntityView entityView) {
        if (b && entityView != null) {
            final ViewGroup viewGroup = (ViewGroup)entityView.getParent();
            if (this.textDimView.getParent() != null) {
                ((EntitiesContainerView)this.textDimView.getParent()).removeView((View)this.textDimView);
            }
            viewGroup.addView((View)this.textDimView, viewGroup.indexOfChild((View)entityView));
        }
        entityView.setSelectionVisibility(b ^ true);
        ObjectAnimator objectAnimator;
        if (b) {
            this.textDimView.setVisibility(0);
            objectAnimator = ObjectAnimator.ofFloat((Object)this.textDimView, "alpha", new float[] { 0.0f, 1.0f });
        }
        else {
            objectAnimator = ObjectAnimator.ofFloat((Object)this.textDimView, "alpha", new float[] { 1.0f, 0.0f });
        }
        ((Animator)objectAnimator).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (!b) {
                    PhotoPaintView.this.textDimView.setVisibility(8);
                    if (PhotoPaintView.this.textDimView.getParent() != null) {
                        ((EntitiesContainerView)PhotoPaintView.this.textDimView.getParent()).removeView((View)PhotoPaintView.this.textDimView);
                    }
                }
            }
        });
        objectAnimator.setDuration(200L);
        objectAnimator.start();
    }
    
    private void showBrushSettings() {
        this.showPopup(new _$$Lambda$PhotoPaintView$xEc6bzU_od8KaDlhUDbuZvHSrF0(this), (View)this, 85, 0, AndroidUtilities.dp(48.0f));
    }
    
    private void showMenuForEntity(final EntityView entityView) {
        this.showPopup(new _$$Lambda$PhotoPaintView$W57YTvVSDuzEA0lVLhPNgGrpM0g(this, entityView), (View)entityView, 17, (int)((entityView.getPosition().x - this.entitiesView.getWidth() / 2) * this.entitiesView.getScaleX()), (int)((entityView.getPosition().y - entityView.getHeight() * entityView.getScale() / 2.0f - this.entitiesView.getHeight() / 2) * this.entitiesView.getScaleY()) - AndroidUtilities.dp(32.0f));
    }
    
    private void showPopup(final Runnable runnable, final View view, final int n, final int n2, final int n3) {
        final ActionBarPopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null && popupWindow.isShowing()) {
            this.popupWindow.dismiss();
            return;
        }
        if (this.popupLayout == null) {
            this.popupRect = new Rect();
            (this.popupLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.getContext())).setAnimationEnabled(false);
            this.popupLayout.setOnTouchListener((View$OnTouchListener)new _$$Lambda$PhotoPaintView$zZo9_q6rUClGLuFi2Fkr8DYrX2w(this));
            this.popupLayout.setDispatchKeyEventListener(new _$$Lambda$PhotoPaintView$JREJI5nlVBnmTQKnZtaJEmOzNLY(this));
            this.popupLayout.setShowedFromBotton(true);
        }
        this.popupLayout.removeInnerViews();
        runnable.run();
        if (this.popupWindow == null) {
            (this.popupWindow = new ActionBarPopupWindow((View)this.popupLayout, -2, -2)).setAnimationEnabled(false);
            this.popupWindow.setAnimationStyle(2131624110);
            this.popupWindow.setOutsideTouchable(true);
            this.popupWindow.setClippingEnabled(true);
            this.popupWindow.setInputMethodMode(2);
            this.popupWindow.setSoftInputMode(0);
            this.popupWindow.getContentView().setFocusableInTouchMode(true);
            this.popupWindow.setOnDismissListener((PopupWindow$OnDismissListener)new _$$Lambda$PhotoPaintView$67QsJI8M7iQb0wPE_b73gcxw_Ro(this));
        }
        this.popupLayout.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        this.popupWindow.setFocusable(true);
        this.popupWindow.showAtLocation(view, n, n2, n3);
        this.popupWindow.startAnimation();
    }
    
    private void showTextSettings() {
        this.showPopup(new _$$Lambda$PhotoPaintView$buAbz9vEN2US4kAn_glr3fxkqZ8(this), (View)this, 85, 0, AndroidUtilities.dp(48.0f));
    }
    
    private Point startPositionRelativeToEntity(final EntityView entityView) {
        if (entityView != null) {
            final Point position = entityView.getPosition();
            return new Point(position.x + 200.0f, position.y + 200.0f);
        }
        Point centerPositionForEntity = this.centerPositionForEntity();
        while (true) {
            int i = 0;
            boolean b = false;
            while (i < this.entitiesView.getChildCount()) {
                final View child = this.entitiesView.getChildAt(i);
                if (child instanceof EntityView) {
                    final Point position2 = ((EntityView)child).getPosition();
                    if ((float)Math.sqrt(Math.pow(position2.x - centerPositionForEntity.x, 2.0) + Math.pow(position2.y - centerPositionForEntity.y, 2.0)) < 100.0f) {
                        b = true;
                    }
                }
                ++i;
            }
            if (!b) {
                break;
            }
            centerPositionForEntity = new Point(centerPositionForEntity.x + 200.0f, centerPositionForEntity.y + 200.0f);
        }
        return centerPositionForEntity;
    }
    
    private void updateSettingsButton() {
        final EntityView currentEntityView = this.currentEntityView;
        int settingsButtonImage = 2131165746;
        if (currentEntityView != null) {
            if (currentEntityView instanceof StickerView) {
                settingsButtonImage = 2131165743;
            }
            else if (currentEntityView instanceof TextPaintView) {
                settingsButtonImage = 2131165744;
            }
            this.paintButton.setImageResource(2131165745);
            this.paintButton.setColorFilter((ColorFilter)null);
        }
        else {
            this.paintButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(-11420173, PorterDuff$Mode.MULTIPLY));
            this.paintButton.setImageResource(2131165745);
        }
        this.colorPicker.setSettingsButtonImage(settingsButtonImage);
    }
    
    public boolean allowInteraction(final EntityView entityView) {
        return this.editingText ^ true;
    }
    
    public void closeTextEnter(final boolean b) {
        if (this.editingText) {
            final EntityView currentEntityView = this.currentEntityView;
            if (currentEntityView instanceof TextPaintView) {
                final TextPaintView textPaintView = (TextPaintView)currentEntityView;
                this.toolsView.setVisibility(0);
                AndroidUtilities.hideKeyboard(textPaintView.getFocusedView());
                textPaintView.getFocusedView().clearFocus();
                textPaintView.endEditing();
                if (!b) {
                    textPaintView.setText(this.initialText);
                }
                if (textPaintView.getText().trim().length() == 0) {
                    this.entitiesView.removeView((View)textPaintView);
                    this.selectEntity(null);
                }
                else {
                    textPaintView.setPosition(this.editedTextPosition);
                    textPaintView.setRotation(this.editedTextRotation);
                    textPaintView.setScale(this.editedTextScale);
                    this.editedTextPosition = null;
                    this.editedTextRotation = 0.0f;
                    this.editedTextScale = 0.0f;
                }
                this.setTextDimVisibility(false, textPaintView);
                this.editingText = false;
                this.initialText = null;
                this.curtainView.setVisibility(8);
            }
        }
    }
    
    public Bitmap getBitmap() {
        final Bitmap resultBitmap = this.renderView.getResultBitmap();
        if (resultBitmap != null && this.entitiesView.entitiesCount() > 0) {
            final Canvas canvas = new Canvas(resultBitmap);
            for (int i = 0; i < this.entitiesView.getChildCount(); ++i) {
                final View child = this.entitiesView.getChildAt(i);
                canvas.save();
                if (child instanceof EntityView) {
                    final EntityView entityView = (EntityView)child;
                    canvas.translate(entityView.getPosition().x, entityView.getPosition().y);
                    canvas.scale(child.getScaleX(), child.getScaleY());
                    canvas.rotate(child.getRotation());
                    canvas.translate((float)(-entityView.getWidth() / 2), (float)(-entityView.getHeight() / 2));
                    if (child instanceof TextPaintView) {
                        final Bitmap bitmap = Bitmaps.createBitmap(child.getWidth(), child.getHeight(), Bitmap$Config.ARGB_8888);
                        final Canvas canvas2 = new Canvas(bitmap);
                        child.draw(canvas2);
                        canvas.drawBitmap(bitmap, (Rect)null, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), (Paint)null);
                        try {
                            canvas2.setBitmap((Bitmap)null);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                        bitmap.recycle();
                    }
                    else {
                        child.draw(canvas);
                    }
                }
                canvas.restore();
            }
        }
        return resultBitmap;
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
    
    public ArrayList<TLRPC.InputDocument> getMasks() {
        final int childCount = this.entitiesView.getChildCount();
        ArrayList<TLRPC.TL_inputDocument> list = null;
        ArrayList<TLRPC.TL_inputDocument> list2;
        for (int i = 0; i < childCount; ++i, list = list2) {
            final View child = this.entitiesView.getChildAt(i);
            list2 = list;
            if (child instanceof StickerView) {
                final TLRPC.Document sticker = ((StickerView)child).getSticker();
                if ((list2 = list) == null) {
                    list2 = new ArrayList<TLRPC.TL_inputDocument>();
                }
                final TLRPC.TL_inputDocument e = new TLRPC.TL_inputDocument();
                e.id = sticker.id;
                e.access_hash = sticker.access_hash;
                e.file_reference = sticker.file_reference;
                if (e.file_reference == null) {
                    e.file_reference = new byte[0];
                }
                list2.add(e);
            }
        }
        return (ArrayList<TLRPC.InputDocument>)list;
    }
    
    public FrameLayout getToolsView() {
        return this.toolsView;
    }
    
    public void init() {
        this.renderView.setVisibility(0);
    }
    
    public void maybeShowDismissalAlert(final PhotoViewer photoViewer, final Activity activity, final Runnable runnable) {
        if (this.editingText) {
            this.closeTextEnter(false);
            return;
        }
        if (this.pickingSticker) {
            this.closeStickersView();
            return;
        }
        if (this.hasChanges()) {
            if (activity == null) {
                return;
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
            builder.setMessage(LocaleController.getString("DiscardChanges", 2131559273));
            builder.setTitle(LocaleController.getString("AppName", 2131558635));
            builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$PhotoPaintView$zLiJ6Xhod_3MqoXM9DwNMRgBw8Y(runnable));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
            photoViewer.showAlertDialog(builder);
        }
        else {
            runnable.run();
        }
    }
    
    public boolean onEntityLongClicked(final EntityView entityView) {
        this.showMenuForEntity(entityView);
        return true;
    }
    
    public boolean onEntitySelected(final EntityView entityView) {
        return this.selectEntity(entityView);
    }
    
    protected void onLayout(final boolean b, int statusBarHeight, int n, int n2, int n3) {
        n2 -= statusBarHeight;
        n3 -= n;
        if (Build$VERSION.SDK_INT >= 21) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        n = ActionBar.getCurrentActionBarHeight();
        final int n4 = ActionBar.getCurrentActionBarHeight() + statusBarHeight;
        final int n5 = AndroidUtilities.displaySize.y - n - AndroidUtilities.dp(48.0f);
        float n6;
        float n7;
        if (this.bitmapToEdit != null) {
            if (this.isSidewardOrientation()) {
                n = this.bitmapToEdit.getHeight();
            }
            else {
                n = this.bitmapToEdit.getWidth();
            }
            n6 = (float)n;
            if (this.isSidewardOrientation()) {
                n = this.bitmapToEdit.getWidth();
            }
            else {
                n = this.bitmapToEdit.getHeight();
            }
            n7 = (float)n;
        }
        else {
            n6 = (float)n2;
            n7 = (float)(n3 - n - AndroidUtilities.dp(48.0f));
        }
        float n8 = (float)n2;
        final float n9 = (float)Math.floor(n8 * n7 / n6);
        final float n10 = (float)n5;
        if (n9 > n10) {
            n8 = (float)Math.floor(n10 * n6 / n7);
        }
        n = (int)Math.ceil((n2 - this.renderView.getMeasuredWidth()) / 2);
        final int n11 = (n3 - n4 - AndroidUtilities.dp(48.0f) - this.renderView.getMeasuredHeight()) / 2 + n4 - ActionBar.getCurrentActionBarHeight() + AndroidUtilities.dp(8.0f);
        final RenderView renderView = this.renderView;
        renderView.layout(n, n11, renderView.getMeasuredWidth() + n, this.renderView.getMeasuredHeight() + n11);
        final float n12 = n8 / this.paintingSize.width;
        this.entitiesView.setScaleX(n12);
        this.entitiesView.setScaleY(n12);
        final EntitiesContainerView entitiesView = this.entitiesView;
        entitiesView.layout(n, n11, entitiesView.getMeasuredWidth() + n, this.entitiesView.getMeasuredHeight() + n11);
        final FrameLayout dimView = this.dimView;
        dimView.layout(0, statusBarHeight, dimView.getMeasuredWidth(), this.dimView.getMeasuredHeight() + statusBarHeight);
        final FrameLayout selectionContainerView = this.selectionContainerView;
        selectionContainerView.layout(0, statusBarHeight, selectionContainerView.getMeasuredWidth(), this.selectionContainerView.getMeasuredHeight() + statusBarHeight);
        final ColorPicker colorPicker = this.colorPicker;
        colorPicker.layout(0, n4, colorPicker.getMeasuredWidth(), this.colorPicker.getMeasuredHeight() + n4);
        final FrameLayout toolsView = this.toolsView;
        toolsView.layout(0, n3 - toolsView.getMeasuredHeight(), this.toolsView.getMeasuredWidth(), n3);
        this.curtainView.layout(0, 0, n2, n5);
        final StickerMasksView stickersView = this.stickersView;
        if (stickersView != null) {
            stickersView.layout(0, statusBarHeight, stickersView.getMeasuredWidth(), this.stickersView.getMeasuredHeight() + statusBarHeight);
        }
        final EntityView currentEntityView = this.currentEntityView;
        if (currentEntityView != null) {
            currentEntityView.updateSelectionView();
            this.currentEntityView.setOffset(this.entitiesView.getLeft() - this.selectionContainerView.getLeft(), this.entitiesView.getTop() - this.selectionContainerView.getTop());
        }
    }
    
    protected void onMeasure(final int n, int n2) {
        final int size = View$MeasureSpec.getSize(n);
        n2 = View$MeasureSpec.getSize(n2);
        this.setMeasuredDimension(size, n2);
        final int n3 = AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(48.0f);
        float n4;
        float n5;
        if (this.bitmapToEdit != null) {
            if (this.isSidewardOrientation()) {
                n2 = this.bitmapToEdit.getHeight();
            }
            else {
                n2 = this.bitmapToEdit.getWidth();
            }
            n4 = (float)n2;
            if (this.isSidewardOrientation()) {
                n2 = this.bitmapToEdit.getWidth();
            }
            else {
                n2 = this.bitmapToEdit.getHeight();
            }
            n5 = (float)n2;
        }
        else {
            n4 = (float)size;
            n5 = (float)(n2 - ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(48.0f));
        }
        float n6 = (float)size;
        final float n7 = (float)Math.floor(n6 * n5 / n4);
        final float n8 = (float)n3;
        float n9 = n7;
        if (n7 > n8) {
            n6 = (float)Math.floor(n4 * n8 / n5);
            n9 = n8;
        }
        this.renderView.measure(View$MeasureSpec.makeMeasureSpec((int)n6, 1073741824), View$MeasureSpec.makeMeasureSpec((int)n9, 1073741824));
        this.entitiesView.measure(View$MeasureSpec.makeMeasureSpec((int)this.paintingSize.width, 1073741824), View$MeasureSpec.makeMeasureSpec((int)this.paintingSize.height, 1073741824));
        this.dimView.measure(n, View$MeasureSpec.makeMeasureSpec(n3, Integer.MIN_VALUE));
        this.selectionContainerView.measure(n, View$MeasureSpec.makeMeasureSpec(n3, 1073741824));
        this.colorPicker.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n3, 1073741824));
        this.toolsView.measure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        final StickerMasksView stickersView = this.stickersView;
        if (stickersView != null) {
            stickersView.measure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, 1073741824));
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.currentEntityView != null) {
            if (this.editingText) {
                this.closeTextEnter(true);
            }
            else {
                this.selectEntity(null);
            }
        }
        return true;
    }
    
    public void shutdown() {
        this.renderView.shutdown();
        this.entitiesView.setVisibility(8);
        this.selectionContainerView.setVisibility(8);
        this.queue.postRunnable((Runnable)_$$Lambda$PhotoPaintView$BRjjxWV4_4jng8wjr2bRzoAFJ_A.INSTANCE);
    }
    
    private class StickerPosition
    {
        private float angle;
        private Point position;
        private float scale;
        
        StickerPosition(final Point position, final float scale, final float angle) {
            this.position = position;
            this.scale = scale;
            this.angle = angle;
        }
    }
}

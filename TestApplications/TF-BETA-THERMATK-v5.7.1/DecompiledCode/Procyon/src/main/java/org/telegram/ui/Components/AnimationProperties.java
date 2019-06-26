// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.PhotoViewer;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import org.telegram.ui.Cells.DialogCell;
import android.util.Property;

public class AnimationProperties
{
    public static final Property<ClippingImageView, Float> CLIPPING_IMAGE_VIEW_PROGRESS;
    public static final Property<DialogCell, Float> CLIP_DIALOG_CELL_PROGRESS;
    public static final Property<ColorDrawable, Integer> COLOR_DRAWABLE_ALPHA;
    public static final Property<Paint, Integer> PAINT_ALPHA;
    public static final Property<PhotoViewer, Float> PHOTO_VIEWER_ANIMATION_VALUE;
    
    static {
        PAINT_ALPHA = new IntProperty<Paint>("alpha") {
            public Integer get(final Paint paint) {
                return paint.getAlpha();
            }
            
            public void setValue(final Paint paint, final int alpha) {
                paint.setAlpha(alpha);
            }
        };
        COLOR_DRAWABLE_ALPHA = new IntProperty<ColorDrawable>("alpha") {
            public Integer get(final ColorDrawable colorDrawable) {
                return colorDrawable.getAlpha();
            }
            
            public void setValue(final ColorDrawable colorDrawable, final int alpha) {
                colorDrawable.setAlpha(alpha);
            }
        };
        CLIPPING_IMAGE_VIEW_PROGRESS = new FloatProperty<ClippingImageView>("animationProgress") {
            public Float get(final ClippingImageView clippingImageView) {
                return clippingImageView.getAnimationProgress();
            }
            
            public void setValue(final ClippingImageView clippingImageView, final float animationProgress) {
                clippingImageView.setAnimationProgress(animationProgress);
            }
        };
        PHOTO_VIEWER_ANIMATION_VALUE = new FloatProperty<PhotoViewer>("animationValue") {
            public Float get(final PhotoViewer photoViewer) {
                return photoViewer.getAnimationValue();
            }
            
            public void setValue(final PhotoViewer photoViewer, final float animationValue) {
                photoViewer.setAnimationValue(animationValue);
            }
        };
        CLIP_DIALOG_CELL_PROGRESS = new FloatProperty<DialogCell>("clipProgress") {
            public Float get(final DialogCell dialogCell) {
                return dialogCell.getClipProgress();
            }
            
            public void setValue(final DialogCell dialogCell, final float clipProgress) {
                dialogCell.setClipProgress(clipProgress);
            }
        };
    }
    
    public abstract static class FloatProperty<T> extends Property<T, Float>
    {
        public FloatProperty(final String s) {
            super((Class)Float.class, s);
        }
        
        public final void set(final T t, final Float n) {
            this.setValue(t, n);
        }
        
        public abstract void setValue(final T p0, final float p1);
    }
    
    public abstract static class IntProperty<T> extends Property<T, Integer>
    {
        public IntProperty(final String s) {
            super((Class)Integer.class, s);
        }
        
        public final void set(final T t, final Integer n) {
            this.setValue(t, n);
        }
        
        public abstract void setValue(final T p0, final int p1);
    }
}

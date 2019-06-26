// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint.Views;

import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Style;
import android.graphics.Paint;
import org.telegram.ui.Components.Rect;
import android.view.ViewGroup;
import android.view.View;
import android.view.GestureDetector$OnGestureListener;
import android.view.MotionEvent;
import android.view.GestureDetector$SimpleOnGestureListener;
import android.content.Context;
import java.util.UUID;
import org.telegram.ui.Components.Point;
import android.view.GestureDetector;
import android.widget.FrameLayout;

public class EntityView extends FrameLayout
{
    private boolean announcedSelection;
    private EntityViewDelegate delegate;
    private GestureDetector gestureDetector;
    private boolean hasPanned;
    private boolean hasReleased;
    private boolean hasTransformed;
    private int offsetX;
    private int offsetY;
    protected Point position;
    private float previousLocationX;
    private float previousLocationY;
    private boolean recognizedLongPress;
    protected SelectionView selectionView;
    private UUID uuid;
    
    public EntityView(final Context context, final Point position) {
        super(context);
        this.hasPanned = false;
        this.hasReleased = false;
        this.hasTransformed = false;
        this.announcedSelection = false;
        this.recognizedLongPress = false;
        this.position = new Point();
        this.uuid = UUID.randomUUID();
        this.position = position;
        this.gestureDetector = new GestureDetector(context, (GestureDetector$OnGestureListener)new GestureDetector$SimpleOnGestureListener() {
            public void onLongPress(final MotionEvent motionEvent) {
                if (!EntityView.this.hasPanned && !EntityView.this.hasTransformed) {
                    if (!EntityView.this.hasReleased) {
                        EntityView.this.recognizedLongPress = true;
                        if (EntityView.this.delegate != null) {
                            EntityView.this.performHapticFeedback(0);
                            EntityView.this.delegate.onEntityLongClicked(EntityView.this);
                        }
                    }
                }
            }
        });
    }
    
    private boolean onTouchMove(final float previousLocationX, final float previousLocationY) {
        final float scaleX = ((View)this.getParent()).getScaleX();
        final Point point = new Point((previousLocationX - this.previousLocationX) / scaleX, (previousLocationY - this.previousLocationY) / scaleX);
        final float n = (float)Math.hypot(point.x, point.y);
        float n2;
        if (this.hasPanned) {
            n2 = 6.0f;
        }
        else {
            n2 = 16.0f;
        }
        if (n > n2) {
            this.pan(point);
            this.previousLocationX = previousLocationX;
            this.previousLocationY = previousLocationY;
            return this.hasPanned = true;
        }
        return false;
    }
    
    private void onTouchUp() {
        if (!this.recognizedLongPress && !this.hasPanned && !this.hasTransformed && !this.announcedSelection) {
            final EntityViewDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.onEntitySelected(this);
            }
        }
        this.recognizedLongPress = false;
        this.hasPanned = false;
        this.hasTransformed = false;
        this.hasReleased = true;
        this.announcedSelection = false;
    }
    
    protected SelectionView createSelectionView() {
        return null;
    }
    
    public void deselect() {
        final SelectionView selectionView = this.selectionView;
        if (selectionView == null) {
            return;
        }
        if (selectionView.getParent() != null) {
            ((ViewGroup)this.selectionView.getParent()).removeView((View)this.selectionView);
        }
        this.selectionView = null;
    }
    
    public Point getPosition() {
        return this.position;
    }
    
    public float getScale() {
        return this.getScaleX();
    }
    
    protected Rect getSelectionBounds() {
        return new Rect(0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public boolean isSelected() {
        return this.selectionView != null;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return this.delegate.allowInteraction(this);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int pointerCount = motionEvent.getPointerCount();
        final boolean b = false;
        final boolean b2 = false;
        boolean onTouchMove = b;
        if (pointerCount <= 1) {
            if (!this.delegate.allowInteraction(this)) {
                onTouchMove = b;
            }
            else {
                final float rawX = motionEvent.getRawX();
                final float rawY = motionEvent.getRawY();
                final int actionMasked = motionEvent.getActionMasked();
                Label_0166: {
                    while (true) {
                        Label_0114: {
                            if (actionMasked == 0) {
                                break Label_0114;
                            }
                            if (actionMasked != 1) {
                                if (actionMasked == 2) {
                                    onTouchMove = this.onTouchMove(rawX, rawY);
                                    break Label_0166;
                                }
                                if (actionMasked != 3) {
                                    if (actionMasked == 5) {
                                        break Label_0114;
                                    }
                                    if (actionMasked != 6) {
                                        onTouchMove = b2;
                                        break Label_0166;
                                    }
                                }
                            }
                            this.onTouchUp();
                            onTouchMove = true;
                            break Label_0166;
                        }
                        if (!this.isSelected()) {
                            final EntityViewDelegate delegate = this.delegate;
                            if (delegate != null) {
                                delegate.onEntitySelected(this);
                                this.announcedSelection = true;
                            }
                        }
                        this.previousLocationX = rawX;
                        this.previousLocationY = rawY;
                        this.hasReleased = false;
                        continue;
                    }
                }
                this.gestureDetector.onTouchEvent(motionEvent);
            }
        }
        return onTouchMove;
    }
    
    public void pan(final Point point) {
        final Point position = this.position;
        position.x += point.x;
        position.y += point.y;
        this.updatePosition();
    }
    
    public void rotate(final float rotation) {
        this.setRotation(rotation);
        this.updateSelectionView();
    }
    
    public void scale(final float n) {
        this.setScale(Math.max(this.getScale() * n, 0.1f));
        this.updateSelectionView();
    }
    
    public void select(final ViewGroup viewGroup) {
        final SelectionView selectionView = this.createSelectionView();
        viewGroup.addView((View)(this.selectionView = selectionView));
        selectionView.updatePosition();
    }
    
    public void setDelegate(final EntityViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setOffset(final int offsetX, final int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    
    public void setPosition(final Point position) {
        this.position = position;
        this.updatePosition();
    }
    
    public void setScale(final float n) {
        this.setScaleX(n);
        this.setScaleY(n);
    }
    
    public void setSelectionVisibility(final boolean b) {
        final SelectionView selectionView = this.selectionView;
        if (selectionView == null) {
            return;
        }
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        selectionView.setVisibility(visibility);
    }
    
    protected void updatePosition() {
        final float n = this.getWidth() / 2.0f;
        final float n2 = this.getHeight() / 2.0f;
        this.setX(this.position.x - n);
        this.setY(this.position.y - n2);
        this.updateSelectionView();
    }
    
    public void updateSelectionView() {
        final SelectionView selectionView = this.selectionView;
        if (selectionView != null) {
            selectionView.updatePosition();
        }
    }
    
    public interface EntityViewDelegate
    {
        boolean allowInteraction(final EntityView p0);
        
        boolean onEntityLongClicked(final EntityView p0);
        
        boolean onEntitySelected(final EntityView p0);
    }
    
    public class SelectionView extends FrameLayout
    {
        public static final int SELECTION_LEFT_HANDLE = 1;
        public static final int SELECTION_RIGHT_HANDLE = 2;
        public static final int SELECTION_WHOLE_HANDLE = 3;
        private int currentHandle;
        protected Paint dotPaint;
        protected Paint dotStrokePaint;
        protected Paint paint;
        
        public SelectionView(final Context context) {
            super(context);
            this.paint = new Paint(1);
            this.dotPaint = new Paint(1);
            this.dotStrokePaint = new Paint(1);
            this.setWillNotDraw(false);
            this.paint.setColor(-1);
            this.dotPaint.setColor(-12793105);
            this.dotStrokePaint.setColor(-1);
            this.dotStrokePaint.setStyle(Paint$Style.STROKE);
            this.dotStrokePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0f));
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            boolean access$900 = false;
            Label_0485: {
                while (true) {
                    Label_0427: {
                        if (actionMasked == 0) {
                            break Label_0427;
                        }
                        if (actionMasked != 1) {
                            if (actionMasked != 2) {
                                if (actionMasked != 3) {
                                    if (actionMasked == 5) {
                                        break Label_0427;
                                    }
                                    if (actionMasked != 6) {
                                        break Label_0485;
                                    }
                                }
                            }
                            else {
                                final int currentHandle = this.currentHandle;
                                if (currentHandle == 3) {
                                    access$900 = EntityView.this.onTouchMove(motionEvent.getRawX(), motionEvent.getRawY());
                                    break Label_0485;
                                }
                                if (currentHandle != 0) {
                                    EntityView.this.hasTransformed = true;
                                    final Point point = new Point(motionEvent.getRawX() - EntityView.this.previousLocationX, motionEvent.getRawY() - EntityView.this.previousLocationY);
                                    final float n = (float)Math.toRadians(this.getRotation());
                                    final double v = point.x;
                                    final double n2 = n;
                                    final double cos = Math.cos(n2);
                                    Double.isNaN(v);
                                    final double v2 = point.y;
                                    final double sin = Math.sin(n2);
                                    Double.isNaN(v2);
                                    float n3 = (float)(v * cos + v2 * sin);
                                    if (this.currentHandle == 1) {
                                        n3 *= -1.0f;
                                    }
                                    EntityView.this.scale(n3 * 2.0f / this.getWidth() + 1.0f);
                                    final float n4 = (float)(this.getLeft() + this.getWidth() / 2);
                                    final float n5 = (float)(this.getTop() + this.getHeight() / 2);
                                    final float n6 = motionEvent.getRawX() - ((View)this.getParent()).getLeft();
                                    final float n7 = motionEvent.getRawY() - ((View)this.getParent()).getTop() - AndroidUtilities.statusBarHeight;
                                    float n8 = 0.0f;
                                    final int currentHandle2 = this.currentHandle;
                                    Label_0369: {
                                        double n9;
                                        if (currentHandle2 == 1) {
                                            n9 = Math.atan2(n5 - n7, n4 - n6);
                                        }
                                        else {
                                            if (currentHandle2 != 2) {
                                                break Label_0369;
                                            }
                                            n9 = Math.atan2(n7 - n5, n6 - n4);
                                        }
                                        n8 = (float)n9;
                                    }
                                    EntityView.this.rotate((float)Math.toDegrees(n8));
                                    EntityView.this.previousLocationX = motionEvent.getRawX();
                                    EntityView.this.previousLocationY = motionEvent.getRawY();
                                    break Label_0422;
                                }
                                break Label_0485;
                            }
                        }
                        EntityView.this.onTouchUp();
                        this.currentHandle = 0;
                        access$900 = true;
                        break Label_0485;
                    }
                    final int pointInsideHandle = this.pointInsideHandle(motionEvent.getX(), motionEvent.getY());
                    if (pointInsideHandle != 0) {
                        this.currentHandle = pointInsideHandle;
                        EntityView.this.previousLocationX = motionEvent.getRawX();
                        EntityView.this.previousLocationY = motionEvent.getRawY();
                        EntityView.this.hasReleased = false;
                        continue;
                    }
                    break;
                }
            }
            if (this.currentHandle == 3) {
                EntityView.this.gestureDetector.onTouchEvent(motionEvent);
            }
            return access$900;
        }
        
        protected int pointInsideHandle(final float n, final float n2) {
            return 0;
        }
        
        protected void updatePosition() {
            final Rect selectionBounds = EntityView.this.getSelectionBounds();
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.getLayoutParams();
            layoutParams.leftMargin = (int)selectionBounds.x + EntityView.this.offsetX;
            layoutParams.topMargin = (int)selectionBounds.y + EntityView.this.offsetY;
            layoutParams.width = (int)selectionBounds.width;
            layoutParams.height = (int)selectionBounds.height;
            this.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.setRotation(EntityView.this.getRotation());
        }
    }
}

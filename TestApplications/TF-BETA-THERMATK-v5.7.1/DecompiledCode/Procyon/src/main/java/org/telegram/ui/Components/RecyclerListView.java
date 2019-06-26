// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.util.SparseIntArray;
import android.view.GestureDetector$OnGestureListener;
import android.graphics.Path$Direction;
import android.graphics.Color;
import android.text.Layout$Alignment;
import android.graphics.RectF;
import android.graphics.Path;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.StaticLayout;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import android.os.SystemClock;
import org.telegram.messenger.LocaleController;
import android.graphics.Canvas;
import java.util.Collection;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.StateSet;
import android.os.Build$VERSION;
import android.view.ViewConfiguration;
import android.graphics.drawable.TransitionDrawable;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable$Callback;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import android.view.GestureDetector;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerListView extends RecyclerView
{
    private static int[] attributes;
    private static boolean gotAttributes;
    private Runnable clickRunnable;
    private int currentChildPosition;
    private View currentChildView;
    private int currentFirst;
    private int currentVisible;
    private boolean disableHighlightState;
    private boolean disallowInterceptTouchEvents;
    private View emptyView;
    private FastScroll fastScroll;
    private GestureDetector gestureDetector;
    private ArrayList<View> headers;
    private ArrayList<View> headersCache;
    private boolean hiddenByEmptyView;
    private boolean ignoreOnScroll;
    private boolean instantClick;
    private boolean interceptedByChild;
    private boolean isChildViewEnabled;
    private long lastAlphaAnimationTime;
    private boolean longPressCalled;
    private AdapterDataObserver observer;
    private OnInterceptTouchListener onInterceptTouchListener;
    private OnItemClickListener onItemClickListener;
    private OnItemClickListenerExtended onItemClickListenerExtended;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemLongClickListenerExtended onItemLongClickListenerExtended;
    private OnScrollListener onScrollListener;
    private IntReturnCallback pendingHighlightPosition;
    private View pinnedHeader;
    private float pinnedHeaderShadowAlpha;
    private Drawable pinnedHeaderShadowDrawable;
    private float pinnedHeaderShadowTargetAlpha;
    private Runnable removeHighlighSelectionRunnable;
    private boolean scrollEnabled;
    private boolean scrollingByUser;
    private int sectionOffset;
    private SectionsAdapter sectionsAdapter;
    private int sectionsCount;
    private int sectionsType;
    private Runnable selectChildRunnable;
    private Drawable selectorDrawable;
    private int selectorPosition;
    private Rect selectorRect;
    private boolean selfOnLayout;
    private int startSection;
    private boolean wasPressed;
    
    @SuppressLint({ "PrivateApi" })
    public RecyclerListView(final Context context) {
        super(context);
        this.currentFirst = -1;
        this.currentVisible = -1;
        this.selectorRect = new Rect();
        this.scrollEnabled = true;
        this.observer = new AdapterDataObserver() {
            @Override
            public void onChanged() {
                RecyclerListView.this.checkIfEmpty();
                RecyclerListView.this.currentFirst = -1;
                if (RecyclerListView.this.removeHighlighSelectionRunnable == null) {
                    RecyclerListView.this.selectorRect.setEmpty();
                }
                RecyclerListView.this.invalidate();
            }
            
            @Override
            public void onItemRangeInserted(final int n, final int n2) {
                RecyclerListView.this.checkIfEmpty();
            }
            
            @Override
            public void onItemRangeRemoved(final int n, final int n2) {
                RecyclerListView.this.checkIfEmpty();
            }
        };
        this.setGlowColor(Theme.getColor("actionBarDefault"));
        (this.selectorDrawable = Theme.getSelectorDrawable(false)).setCallback((Drawable$Callback)this);
        try {
            if (!RecyclerListView.gotAttributes) {
                RecyclerListView.attributes = this.getResourceDeclareStyleableIntArray("com.android.internal", "View");
                RecyclerListView.gotAttributes = true;
            }
            final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(RecyclerListView.attributes);
            View.class.getDeclaredMethod("initializeScrollbars", TypedArray.class).invoke(this, obtainStyledAttributes);
            obtainStyledAttributes.recycle();
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
        super.setOnScrollListener((OnScrollListener)new OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                boolean b = false;
                if (n != 0 && RecyclerListView.this.currentChildView != null) {
                    if (RecyclerListView.this.selectChildRunnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(RecyclerListView.this.selectChildRunnable);
                        RecyclerListView.this.selectChildRunnable = null;
                    }
                    final MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                    try {
                        RecyclerListView.this.gestureDetector.onTouchEvent(obtain);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    RecyclerListView.this.currentChildView.onTouchEvent(obtain);
                    obtain.recycle();
                    final View access$200 = RecyclerListView.this.currentChildView;
                    final RecyclerListView this$0 = RecyclerListView.this;
                    this$0.onChildPressed(this$0.currentChildView, false);
                    RecyclerListView.this.currentChildView = null;
                    RecyclerListView.this.removeSelection(access$200, null);
                    RecyclerListView.this.interceptedByChild = false;
                }
                if (RecyclerListView.this.onScrollListener != null) {
                    RecyclerListView.this.onScrollListener.onScrollStateChanged(recyclerView, n);
                }
                final RecyclerListView this$2 = RecyclerListView.this;
                if (n == 1 || n == 2) {
                    b = true;
                }
                this$2.scrollingByUser = b;
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                if (RecyclerListView.this.onScrollListener != null) {
                    RecyclerListView.this.onScrollListener.onScrolled(recyclerView, n, n2);
                }
                if (RecyclerListView.this.selectorPosition != -1) {
                    RecyclerListView.this.selectorRect.offset(-n, -n2);
                    RecyclerListView.this.selectorDrawable.setBounds(RecyclerListView.this.selectorRect);
                    RecyclerListView.this.invalidate();
                }
                else {
                    RecyclerListView.this.selectorRect.setEmpty();
                }
                RecyclerListView.this.checkSection();
            }
        });
        this.addOnItemTouchListener((OnItemTouchListener)new RecyclerListViewItemClickListener(context));
    }
    
    private void checkIfEmpty() {
        final Adapter adapter = this.getAdapter();
        final int n = 0;
        if (adapter != null && this.emptyView != null) {
            final boolean b = this.getAdapter().getItemCount() == 0;
            final View emptyView = this.emptyView;
            int visibility;
            if (b) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            emptyView.setVisibility(visibility);
            int visibility2 = n;
            if (b) {
                visibility2 = 4;
            }
            this.setVisibility(visibility2);
            this.hiddenByEmptyView = true;
            return;
        }
        if (this.hiddenByEmptyView && this.getVisibility() != 0) {
            this.setVisibility(0);
            this.hiddenByEmptyView = false;
        }
    }
    
    private void ensurePinnedHeaderLayout(final View view, final boolean b) {
        if (view.isLayoutRequested() || b) {
            final int sectionsType = this.sectionsType;
            if (sectionsType == 1) {
                final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
                final int measureSpec = View$MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
                final int measureSpec2 = View$MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824);
                try {
                    view.measure(measureSpec2, measureSpec);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            else if (sectionsType == 2) {
                final int measureSpec3 = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
                final int measureSpec4 = View$MeasureSpec.makeMeasureSpec(0, 0);
                try {
                    view.measure(measureSpec3, measureSpec4);
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
    }
    
    private int[] getDrawableStateForSelector() {
        final int[] onCreateDrawableState = this.onCreateDrawableState(1);
        onCreateDrawableState[onCreateDrawableState.length - 1] = 16842919;
        return onCreateDrawableState;
    }
    
    private View getSectionHeaderView(final int n, View sectionHeaderView) {
        final boolean b = sectionHeaderView == null;
        sectionHeaderView = this.sectionsAdapter.getSectionHeaderView(n, sectionHeaderView);
        if (b) {
            this.ensurePinnedHeaderLayout(sectionHeaderView, false);
        }
        return sectionHeaderView;
    }
    
    private void highlightRowInternal(final IntReturnCallback pendingHighlightPosition, final boolean b) {
        final Runnable removeHighlighSelectionRunnable = this.removeHighlighSelectionRunnable;
        if (removeHighlighSelectionRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(removeHighlighSelectionRunnable);
            this.removeHighlighSelectionRunnable = null;
        }
        final ViewHolder viewHolderForAdapterPosition = this.findViewHolderForAdapterPosition(pendingHighlightPosition.run());
        if (viewHolderForAdapterPosition != null) {
            this.positionSelector(viewHolderForAdapterPosition.getLayoutPosition(), viewHolderForAdapterPosition.itemView);
            final Drawable selectorDrawable = this.selectorDrawable;
            if (selectorDrawable != null) {
                final Drawable current = selectorDrawable.getCurrent();
                if (current instanceof TransitionDrawable) {
                    if (this.onItemLongClickListener == null && this.onItemClickListenerExtended == null) {
                        ((TransitionDrawable)current).resetTransition();
                    }
                    else {
                        ((TransitionDrawable)current).startTransition(ViewConfiguration.getLongPressTimeout());
                    }
                }
                if (Build$VERSION.SDK_INT >= 21) {
                    this.selectorDrawable.setHotspot((float)(viewHolderForAdapterPosition.itemView.getMeasuredWidth() / 2), (float)(viewHolderForAdapterPosition.itemView.getMeasuredHeight() / 2));
                }
            }
            final Drawable selectorDrawable2 = this.selectorDrawable;
            if (selectorDrawable2 != null && selectorDrawable2.isStateful() && this.selectorDrawable.setState(this.getDrawableStateForSelector())) {
                this.invalidateDrawable(this.selectorDrawable);
            }
            AndroidUtilities.runOnUIThread(this.removeHighlighSelectionRunnable = new _$$Lambda$RecyclerListView$9OyE8_R_oHAnqiquiqoGBlUXLQE(this), 700L);
        }
        else if (b) {
            this.pendingHighlightPosition = pendingHighlightPosition;
        }
    }
    
    private void positionSelector(final int n, final View view) {
        this.positionSelector(n, view, false, -1.0f, -1.0f);
    }
    
    private void positionSelector(final int selectorPosition, final View view, final boolean b, final float n, final float n2) {
        final Runnable removeHighlighSelectionRunnable = this.removeHighlighSelectionRunnable;
        if (removeHighlighSelectionRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(removeHighlighSelectionRunnable);
            this.removeHighlighSelectionRunnable = null;
            this.pendingHighlightPosition = null;
        }
        if (this.selectorDrawable == null) {
            return;
        }
        final boolean b2 = selectorPosition != this.selectorPosition;
        int selectionBottomPadding;
        if (this.getAdapter() instanceof SelectionAdapter) {
            selectionBottomPadding = ((SelectionAdapter)this.getAdapter()).getSelectionBottomPadding(view);
        }
        else {
            selectionBottomPadding = 0;
        }
        if (selectorPosition != -1) {
            this.selectorPosition = selectorPosition;
        }
        this.selectorRect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom() - selectionBottomPadding);
        final boolean enabled = view.isEnabled();
        if (this.isChildViewEnabled != enabled) {
            this.isChildViewEnabled = enabled;
        }
        if (b2) {
            this.selectorDrawable.setVisible(false, false);
            this.selectorDrawable.setState(StateSet.NOTHING);
        }
        this.selectorDrawable.setBounds(this.selectorRect);
        if (b2 && this.getVisibility() == 0) {
            this.selectorDrawable.setVisible(true, false);
        }
        if (Build$VERSION.SDK_INT >= 21 && b) {
            this.selectorDrawable.setHotspot(n, n2);
        }
    }
    
    private void removeSelection(final View view, final MotionEvent motionEvent) {
        if (view == null) {
            return;
        }
        if (view != null && view.isEnabled()) {
            this.positionSelector(this.currentChildPosition, view);
            final Drawable selectorDrawable = this.selectorDrawable;
            if (selectorDrawable != null) {
                final Drawable current = selectorDrawable.getCurrent();
                if (current instanceof TransitionDrawable) {
                    ((TransitionDrawable)current).resetTransition();
                }
                if (motionEvent != null && Build$VERSION.SDK_INT >= 21) {
                    this.selectorDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                }
            }
        }
        else {
            this.selectorRect.setEmpty();
        }
        this.updateSelectorState();
    }
    
    private void updateSelectorState() {
        final Drawable selectorDrawable = this.selectorDrawable;
        if (selectorDrawable != null && selectorDrawable.isStateful()) {
            if (this.currentChildView != null) {
                if (this.selectorDrawable.setState(this.getDrawableStateForSelector())) {
                    this.invalidateDrawable(this.selectorDrawable);
                }
            }
            else if (this.removeHighlighSelectionRunnable == null) {
                this.selectorDrawable.setState(StateSet.NOTHING);
            }
        }
    }
    
    protected boolean allowSelectChildAtPosition(final float n, final float n2) {
        return true;
    }
    
    public boolean canScrollVertically(final int n) {
        return this.scrollEnabled && super.canScrollVertically(n);
    }
    
    public void cancelClickRunnables(final boolean b) {
        final Runnable selectChildRunnable = this.selectChildRunnable;
        if (selectChildRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(selectChildRunnable);
            this.selectChildRunnable = null;
        }
        final View currentChildView = this.currentChildView;
        if (currentChildView != null) {
            if (b) {
                this.onChildPressed(currentChildView, false);
            }
            this.currentChildView = null;
            this.removeSelection(currentChildView, null);
        }
        final Runnable clickRunnable = this.clickRunnable;
        if (clickRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(clickRunnable);
            this.clickRunnable = null;
        }
        this.interceptedByChild = false;
    }
    
    public void checkSection() {
        if ((this.scrollingByUser && this.fastScroll != null) || (this.sectionsType != 0 && this.sectionsAdapter != null)) {
            final LayoutManager layoutManager = this.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
                if (linearLayoutManager.getOrientation() == 1) {
                    final SectionsAdapter sectionsAdapter = this.sectionsAdapter;
                    if (sectionsAdapter != null) {
                        final int sectionsType = this.sectionsType;
                        View view = null;
                        final int n = 0;
                        if (sectionsType == 1) {
                            final int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                            final int currentVisible = Math.abs(linearLayoutManager.findLastVisibleItemPosition() - firstVisibleItemPosition) + 1;
                            if (firstVisibleItemPosition == -1) {
                                return;
                            }
                            if (this.scrollingByUser && this.fastScroll != null) {
                                final Adapter adapter = this.getAdapter();
                                if (adapter instanceof FastScrollAdapter) {
                                    this.fastScroll.setProgress(Math.min(1.0f, firstVisibleItemPosition / (float)(adapter.getItemCount() - currentVisible + 1)));
                                }
                            }
                            this.headersCache.addAll(this.headers);
                            this.headers.clear();
                            if (this.sectionsAdapter.getItemCount() == 0) {
                                return;
                            }
                            if (this.currentFirst != firstVisibleItemPosition || this.currentVisible != currentVisible) {
                                this.currentFirst = firstVisibleItemPosition;
                                this.currentVisible = currentVisible;
                                this.sectionsCount = 1;
                                this.startSection = this.sectionsAdapter.getSectionForPosition(firstVisibleItemPosition);
                                for (int i = this.sectionsAdapter.getCountForSection(this.startSection) + firstVisibleItemPosition - this.sectionsAdapter.getPositionInSectionForPosition(firstVisibleItemPosition); i < firstVisibleItemPosition + currentVisible; i += this.sectionsAdapter.getCountForSection(this.startSection + this.sectionsCount), ++this.sectionsCount) {}
                            }
                            int j = this.startSection;
                            int n2 = firstVisibleItemPosition;
                            while (j < this.startSection + this.sectionsCount) {
                                View view2;
                                if (!this.headersCache.isEmpty()) {
                                    view2 = this.headersCache.get(0);
                                    this.headersCache.remove(0);
                                }
                                else {
                                    view2 = null;
                                }
                                final View sectionHeaderView = this.getSectionHeaderView(j, view2);
                                this.headers.add(sectionHeaderView);
                                final int countForSection = this.sectionsAdapter.getCountForSection(j);
                                int n3;
                                if (j == this.startSection) {
                                    final int positionInSectionForPosition = this.sectionsAdapter.getPositionInSectionForPosition(n2);
                                    if (positionInSectionForPosition == countForSection - 1) {
                                        sectionHeaderView.setTag((Object)(-sectionHeaderView.getHeight()));
                                    }
                                    else if (positionInSectionForPosition == countForSection - 2) {
                                        final View child = this.getChildAt(n2 - firstVisibleItemPosition);
                                        int top;
                                        if (child != null) {
                                            top = child.getTop();
                                        }
                                        else {
                                            top = -AndroidUtilities.dp(100.0f);
                                        }
                                        if (top < 0) {
                                            sectionHeaderView.setTag((Object)top);
                                        }
                                        else {
                                            sectionHeaderView.setTag((Object)0);
                                        }
                                    }
                                    else {
                                        sectionHeaderView.setTag((Object)0);
                                    }
                                    n3 = countForSection - this.sectionsAdapter.getPositionInSectionForPosition(firstVisibleItemPosition);
                                }
                                else {
                                    final View child2 = this.getChildAt(n2 - firstVisibleItemPosition);
                                    if (child2 != null) {
                                        sectionHeaderView.setTag((Object)child2.getTop());
                                        n3 = countForSection;
                                    }
                                    else {
                                        sectionHeaderView.setTag((Object)(-AndroidUtilities.dp(100.0f)));
                                        n3 = countForSection;
                                    }
                                }
                                n2 += n3;
                                ++j;
                            }
                        }
                        else if (sectionsType == 2) {
                            this.pinnedHeaderShadowTargetAlpha = 0.0f;
                            if (sectionsAdapter.getItemCount() == 0) {
                                return;
                            }
                            final int childCount = this.getChildCount();
                            View view3 = null;
                            int k = 0;
                            int n4 = Integer.MAX_VALUE;
                            int a = 0;
                            int n5 = Integer.MAX_VALUE;
                            while (k < childCount) {
                                final View child3 = this.getChildAt(k);
                                final int bottom = child3.getBottom();
                                View view4;
                                int n6;
                                if (bottom <= this.sectionOffset + this.getPaddingTop()) {
                                    view4 = view;
                                    n6 = n5;
                                }
                                else {
                                    int n7;
                                    if (bottom < (n7 = n4)) {
                                        view = child3;
                                        n7 = bottom;
                                    }
                                    final int max = Math.max(a, bottom);
                                    if (bottom < this.sectionOffset + this.getPaddingTop() + AndroidUtilities.dp(32.0f)) {
                                        n4 = n7;
                                        a = max;
                                        view4 = view;
                                        n6 = n5;
                                    }
                                    else {
                                        n4 = n7;
                                        a = max;
                                        view4 = view;
                                        if (bottom < (n6 = n5)) {
                                            view3 = child3;
                                            n6 = bottom;
                                            view4 = view;
                                            a = max;
                                            n4 = n7;
                                        }
                                    }
                                }
                                ++k;
                                view = view4;
                                n5 = n6;
                            }
                            if (view == null) {
                                return;
                            }
                            final ViewHolder childViewHolder = this.getChildViewHolder(view);
                            if (childViewHolder == null) {
                                return;
                            }
                            final int adapterPosition = childViewHolder.getAdapterPosition();
                            final int sectionForPosition = this.sectionsAdapter.getSectionForPosition(adapterPosition);
                            if (sectionForPosition < 0) {
                                return;
                            }
                            if (this.currentFirst != sectionForPosition || this.pinnedHeader == null) {
                                this.pinnedHeader = this.getSectionHeaderView(sectionForPosition, this.pinnedHeader);
                                this.currentFirst = sectionForPosition;
                            }
                            if (this.pinnedHeader != null && view3 != null && view3.getClass() != this.pinnedHeader.getClass()) {
                                this.pinnedHeaderShadowTargetAlpha = 1.0f;
                            }
                            final int countForSection2 = this.sectionsAdapter.getCountForSection(sectionForPosition);
                            final int positionInSectionForPosition2 = this.sectionsAdapter.getPositionInSectionForPosition(adapterPosition);
                            final int paddingTop = this.getPaddingTop();
                            int sectionOffset;
                            if (a != 0 && a < this.getMeasuredHeight() - this.getPaddingBottom()) {
                                sectionOffset = n;
                            }
                            else {
                                sectionOffset = this.sectionOffset;
                            }
                            if (positionInSectionForPosition2 == countForSection2 - 1) {
                                final int height = this.pinnedHeader.getHeight();
                                int n9;
                                if (view != null) {
                                    final int n8 = view.getTop() - paddingTop - this.sectionOffset + view.getHeight();
                                    if (n8 < height) {
                                        n9 = n8 - height;
                                    }
                                    else {
                                        n9 = paddingTop;
                                    }
                                }
                                else {
                                    n9 = -AndroidUtilities.dp(100.0f);
                                }
                                if (n9 < 0) {
                                    this.pinnedHeader.setTag((Object)(paddingTop + sectionOffset + n9));
                                }
                                else {
                                    this.pinnedHeader.setTag((Object)(paddingTop + sectionOffset));
                                }
                            }
                            else {
                                this.pinnedHeader.setTag((Object)(paddingTop + sectionOffset));
                            }
                            this.invalidate();
                        }
                    }
                    else {
                        final int firstVisibleItemPosition2 = linearLayoutManager.findFirstVisibleItemPosition();
                        final int abs = Math.abs(linearLayoutManager.findLastVisibleItemPosition() - firstVisibleItemPosition2);
                        if (firstVisibleItemPosition2 == -1) {
                            return;
                        }
                        if (this.scrollingByUser && this.fastScroll != null) {
                            final Adapter adapter2 = this.getAdapter();
                            if (adapter2 instanceof FastScrollAdapter) {
                                this.fastScroll.setProgress(Math.min(1.0f, firstVisibleItemPosition2 / (float)(adapter2.getItemCount() - (abs + 1) + 1)));
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        super.dispatchDraw(canvas);
        final int sectionsType = this.sectionsType;
        float n = 0.0f;
        if (sectionsType == 1) {
            if (this.sectionsAdapter == null || this.headers.isEmpty()) {
                return;
            }
            for (int i = 0; i < this.headers.size(); ++i) {
                final View view = this.headers.get(i);
                final int save = canvas.save();
                final int intValue = (int)view.getTag();
                float n2;
                if (LocaleController.isRTL) {
                    n2 = (float)(this.getWidth() - view.getWidth());
                }
                else {
                    n2 = 0.0f;
                }
                canvas.translate(n2, (float)intValue);
                canvas.clipRect(0, 0, this.getWidth(), view.getMeasuredHeight());
                view.draw(canvas);
                canvas.restoreToCount(save);
            }
        }
        else if (sectionsType == 2) {
            if (this.sectionsAdapter == null || this.pinnedHeader == null) {
                return;
            }
            final int save2 = canvas.save();
            final int intValue2 = (int)this.pinnedHeader.getTag();
            if (LocaleController.isRTL) {
                n = (float)(this.getWidth() - this.pinnedHeader.getWidth());
            }
            canvas.translate(n, (float)intValue2);
            final Drawable pinnedHeaderShadowDrawable = this.pinnedHeaderShadowDrawable;
            if (pinnedHeaderShadowDrawable != null) {
                pinnedHeaderShadowDrawable.setBounds(0, this.pinnedHeader.getMeasuredHeight(), this.getWidth(), this.pinnedHeader.getMeasuredHeight() + this.pinnedHeaderShadowDrawable.getIntrinsicHeight());
                this.pinnedHeaderShadowDrawable.setAlpha((int)(this.pinnedHeaderShadowAlpha * 255.0f));
                this.pinnedHeaderShadowDrawable.draw(canvas);
                final long uptimeMillis = SystemClock.uptimeMillis();
                final long min = Math.min(20L, uptimeMillis - this.lastAlphaAnimationTime);
                this.lastAlphaAnimationTime = uptimeMillis;
                final float pinnedHeaderShadowAlpha = this.pinnedHeaderShadowAlpha;
                final float pinnedHeaderShadowTargetAlpha = this.pinnedHeaderShadowTargetAlpha;
                if (pinnedHeaderShadowAlpha < pinnedHeaderShadowTargetAlpha) {
                    this.pinnedHeaderShadowAlpha = pinnedHeaderShadowAlpha + min / 180.0f;
                    if (this.pinnedHeaderShadowAlpha > pinnedHeaderShadowTargetAlpha) {
                        this.pinnedHeaderShadowAlpha = pinnedHeaderShadowTargetAlpha;
                    }
                    this.invalidate();
                }
                else if (pinnedHeaderShadowAlpha > pinnedHeaderShadowTargetAlpha) {
                    this.pinnedHeaderShadowAlpha = pinnedHeaderShadowAlpha - min / 180.0f;
                    if (this.pinnedHeaderShadowAlpha < pinnedHeaderShadowTargetAlpha) {
                        this.pinnedHeaderShadowAlpha = pinnedHeaderShadowTargetAlpha;
                    }
                    this.invalidate();
                }
            }
            canvas.clipRect(0, 0, this.getWidth(), this.pinnedHeader.getMeasuredHeight());
            this.pinnedHeader.draw(canvas);
            canvas.restoreToCount(save2);
        }
        if (!this.selectorRect.isEmpty()) {
            this.selectorDrawable.setBounds(this.selectorRect);
            this.selectorDrawable.draw(canvas);
        }
    }
    
    @Override
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2, final int n3) {
        if (this.longPressCalled) {
            final OnItemLongClickListenerExtended onItemLongClickListenerExtended = this.onItemLongClickListenerExtended;
            if (onItemLongClickListenerExtended != null) {
                onItemLongClickListenerExtended.onMove((float)n, (float)n2);
            }
            array[0] = n;
            array[1] = n2;
            return true;
        }
        return super.dispatchNestedPreScroll(n, n2, array, array2, n3);
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.updateSelectorState();
    }
    
    @Override
    public View findChildViewUnder(final float n, final float n2) {
        final int childCount = this.getChildCount();
        for (int i = 0; i < 2; ++i) {
            for (int j = childCount - 1; j >= 0; --j) {
                final View child = this.getChildAt(j);
                float translationY = 0.0f;
                float translationX;
                if (i == 0) {
                    translationX = child.getTranslationX();
                }
                else {
                    translationX = 0.0f;
                }
                if (i == 0) {
                    translationY = child.getTranslationY();
                }
                if (n >= child.getLeft() + translationX && n <= child.getRight() + translationX && n2 >= child.getTop() + translationY && n2 <= child.getBottom() + translationY) {
                    return child;
                }
            }
        }
        return null;
    }
    
    public View getEmptyView() {
        return this.emptyView;
    }
    
    public ArrayList<View> getHeaders() {
        return this.headers;
    }
    
    public ArrayList<View> getHeadersCache() {
        return this.headersCache;
    }
    
    public OnItemClickListener getOnItemClickListener() {
        return this.onItemClickListener;
    }
    
    public View getPinnedHeader() {
        return this.pinnedHeader;
    }
    
    protected View getPressedChildView() {
        return this.currentChildView;
    }
    
    public int[] getResourceDeclareStyleableIntArray(final String str, final String name) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(".R$styleable");
            final Field field = Class.forName(sb.toString()).getField(name);
            if (field != null) {
                return (int[])field.get(null);
            }
            return null;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    public void hideSelector() {
        final View currentChildView = this.currentChildView;
        if (currentChildView != null) {
            this.onChildPressed(currentChildView, false);
            this.currentChildView = null;
            this.removeSelection(currentChildView, null);
        }
    }
    
    public void highlightRow(final IntReturnCallback intReturnCallback) {
        this.highlightRowInternal(intReturnCallback, true);
    }
    
    public void invalidateViews() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            this.getChildAt(i).invalidate();
        }
    }
    
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        final Drawable selectorDrawable = this.selectorDrawable;
        if (selectorDrawable != null) {
            selectorDrawable.jumpToCurrentState();
        }
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final FastScroll fastScroll = this.fastScroll;
        if (fastScroll != null && fastScroll.getParent() != this.getParent()) {
            final ViewGroup viewGroup = (ViewGroup)this.fastScroll.getParent();
            if (viewGroup != null) {
                viewGroup.removeView((View)this.fastScroll);
            }
            ((ViewGroup)this.getParent()).addView((View)this.fastScroll);
        }
    }
    
    @Override
    public void onChildAttachedToWindow(final View view) {
        if (this.getAdapter() instanceof SelectionAdapter) {
            final ViewHolder containingViewHolder = this.findContainingViewHolder(view);
            if (containingViewHolder != null) {
                view.setEnabled(((SelectionAdapter)this.getAdapter()).isEnabled(containingViewHolder));
            }
        }
        else {
            view.setEnabled(false);
        }
        super.onChildAttachedToWindow(view);
    }
    
    protected void onChildPressed(final View view, final boolean pressed) {
        if (this.disableHighlightState) {
            return;
        }
        view.setPressed(pressed);
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.selectorPosition = -1;
        this.selectorRect.setEmpty();
    }
    
    @Override
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final boolean enabled = this.isEnabled();
        boolean b = false;
        if (!enabled) {
            return false;
        }
        if (this.disallowInterceptTouchEvents) {
            this.requestDisallowInterceptTouchEvent(true);
        }
        final OnInterceptTouchListener onInterceptTouchListener = this.onInterceptTouchListener;
        if ((onInterceptTouchListener != null && onInterceptTouchListener.onInterceptTouchEvent(motionEvent)) || super.onInterceptTouchEvent(motionEvent)) {
            b = true;
        }
        return b;
    }
    
    @Override
    protected void onLayout(final boolean b, int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        final FastScroll fastScroll = this.fastScroll;
        if (fastScroll != null) {
            this.selfOnLayout = true;
            if (LocaleController.isRTL) {
                fastScroll.layout(0, n2, fastScroll.getMeasuredWidth(), this.fastScroll.getMeasuredHeight() + n2);
            }
            else {
                n = this.getMeasuredWidth() - this.fastScroll.getMeasuredWidth();
                final FastScroll fastScroll2 = this.fastScroll;
                fastScroll2.layout(n, n2, fastScroll2.getMeasuredWidth() + n, this.fastScroll.getMeasuredHeight() + n2);
            }
            this.selfOnLayout = false;
        }
        this.checkSection();
        final IntReturnCallback pendingHighlightPosition = this.pendingHighlightPosition;
        if (pendingHighlightPosition != null) {
            this.highlightRowInternal(pendingHighlightPosition, false);
        }
    }
    
    @Override
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        final FastScroll fastScroll = this.fastScroll;
        if (fastScroll != null) {
            fastScroll.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(132.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
        }
    }
    
    @Override
    protected void onSizeChanged(int i, final int n, final int n2, final int n3) {
        super.onSizeChanged(i, n, n2, n3);
        i = this.sectionsType;
        if (i == 1) {
            if (this.sectionsAdapter == null || this.headers.isEmpty()) {
                return;
            }
            for (i = 0; i < this.headers.size(); ++i) {
                this.ensurePinnedHeaderLayout(this.headers.get(i), true);
            }
        }
        else if (i == 2 && this.sectionsAdapter != null) {
            final View pinnedHeader = this.pinnedHeader;
            if (pinnedHeader != null) {
                this.ensurePinnedHeaderLayout(pinnedHeader, true);
            }
        }
    }
    
    @Override
    public void setAdapter(final Adapter adapter) {
        final Adapter adapter2 = this.getAdapter();
        if (adapter2 != null) {
            adapter2.unregisterAdapterDataObserver(this.observer);
        }
        final ArrayList<View> headers = this.headers;
        if (headers != null) {
            headers.clear();
            this.headersCache.clear();
        }
        this.currentFirst = -1;
        this.selectorPosition = -1;
        this.selectorRect.setEmpty();
        this.pinnedHeader = null;
        if (adapter instanceof SectionsAdapter) {
            this.sectionsAdapter = (SectionsAdapter)adapter;
        }
        else {
            this.sectionsAdapter = null;
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.observer);
        }
        this.checkIfEmpty();
    }
    
    public void setDisableHighlightState(final boolean disableHighlightState) {
        this.disableHighlightState = disableHighlightState;
    }
    
    public void setDisallowInterceptTouchEvents(final boolean disallowInterceptTouchEvents) {
        this.disallowInterceptTouchEvents = disallowInterceptTouchEvents;
    }
    
    public void setEmptyView(final View emptyView) {
        if (this.emptyView == emptyView) {
            return;
        }
        this.emptyView = emptyView;
        this.checkIfEmpty();
    }
    
    public void setFastScrollEnabled() {
        this.fastScroll = new FastScroll(this.getContext());
        if (this.getParent() != null) {
            ((ViewGroup)this.getParent()).addView((View)this.fastScroll);
        }
    }
    
    public void setFastScrollVisible(final boolean b) {
        final FastScroll fastScroll = this.fastScroll;
        if (fastScroll == null) {
            return;
        }
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        fastScroll.setVisibility(visibility);
    }
    
    public void setInstantClick(final boolean instantClick) {
        this.instantClick = instantClick;
    }
    
    public void setListSelectorColor(final int n) {
        Theme.setSelectorDrawableColor(this.selectorDrawable, n, true);
    }
    
    public void setOnInterceptTouchListener(final OnInterceptTouchListener onInterceptTouchListener) {
        this.onInterceptTouchListener = onInterceptTouchListener;
    }
    
    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    
    public void setOnItemClickListener(final OnItemClickListenerExtended onItemClickListenerExtended) {
        this.onItemClickListenerExtended = onItemClickListenerExtended;
    }
    
    public void setOnItemLongClickListener(final OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
        this.gestureDetector.setIsLongpressEnabled(onItemLongClickListener != null);
    }
    
    public void setOnItemLongClickListener(final OnItemLongClickListenerExtended onItemLongClickListenerExtended) {
        this.onItemLongClickListenerExtended = onItemLongClickListenerExtended;
        this.gestureDetector.setIsLongpressEnabled(onItemLongClickListenerExtended != null);
    }
    
    @Override
    public void setOnScrollListener(final OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }
    
    public void setPinnedHeaderShadowDrawable(final Drawable pinnedHeaderShadowDrawable) {
        this.pinnedHeaderShadowDrawable = pinnedHeaderShadowDrawable;
    }
    
    public void setPinnedSectionOffsetY(final int sectionOffset) {
        this.sectionOffset = sectionOffset;
        this.invalidate();
    }
    
    public void setScrollEnabled(final boolean scrollEnabled) {
        this.scrollEnabled = scrollEnabled;
    }
    
    public void setSectionsType(final int sectionsType) {
        this.sectionsType = sectionsType;
        if (this.sectionsType == 1) {
            this.headers = new ArrayList<View>();
            this.headersCache = new ArrayList<View>();
        }
    }
    
    public void setSelectorDrawableColor(final int n) {
        final Drawable selectorDrawable = this.selectorDrawable;
        if (selectorDrawable != null) {
            selectorDrawable.setCallback((Drawable$Callback)null);
        }
        (this.selectorDrawable = Theme.getSelectorDrawable(n, false)).setCallback((Drawable$Callback)this);
    }
    
    public void setVerticalScrollBarEnabled(final boolean verticalScrollBarEnabled) {
        if (RecyclerListView.attributes != null) {
            super.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
        }
    }
    
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        if (visibility != 0) {
            this.hiddenByEmptyView = false;
        }
    }
    
    @Override
    public void stopScroll() {
        try {
            super.stopScroll();
        }
        catch (NullPointerException ex) {}
    }
    
    public void updateFastScrollColors() {
        final FastScroll fastScroll = this.fastScroll;
        if (fastScroll != null) {
            fastScroll.updateColors();
        }
    }
    
    public boolean verifyDrawable(final Drawable drawable) {
        return this.selectorDrawable == drawable || super.verifyDrawable(drawable);
    }
    
    private class FastScroll extends View
    {
        private float bubbleProgress;
        private int[] colors;
        private String currentLetter;
        private long lastUpdateTime;
        private float lastY;
        private StaticLayout letterLayout;
        private TextPaint letterPaint;
        private StaticLayout oldLetterLayout;
        private Paint paint;
        private Path path;
        private boolean pressed;
        private float progress;
        private float[] radii;
        private RectF rect;
        private int scrollX;
        private float startDy;
        private float textX;
        private float textY;
        
        public FastScroll(final Context context) {
            super(context);
            this.rect = new RectF();
            this.paint = new Paint(1);
            this.letterPaint = new TextPaint(1);
            this.path = new Path();
            this.radii = new float[8];
            this.colors = new int[6];
            this.letterPaint.setTextSize((float)AndroidUtilities.dp(45.0f));
            for (int i = 0; i < 8; ++i) {
                this.radii[i] = (float)AndroidUtilities.dp(44.0f);
            }
            float n;
            if (LocaleController.isRTL) {
                n = 10.0f;
            }
            else {
                n = 117.0f;
            }
            this.scrollX = AndroidUtilities.dp(n);
            this.updateColors();
        }
        
        private void getCurrentLetter() {
            final LayoutManager layoutManager = RecyclerListView.this.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
                if (linearLayoutManager.getOrientation() == 1) {
                    final Adapter adapter = RecyclerListView.this.getAdapter();
                    if (adapter instanceof FastScrollAdapter) {
                        final FastScrollAdapter fastScrollAdapter = (FastScrollAdapter)adapter;
                        final int positionForScrollProgress = fastScrollAdapter.getPositionForScrollProgress(this.progress);
                        linearLayoutManager.scrollToPositionWithOffset(positionForScrollProgress, 0);
                        final String letter = fastScrollAdapter.getLetter(positionForScrollProgress);
                        if (letter == null) {
                            final StaticLayout letterLayout = this.letterLayout;
                            if (letterLayout != null) {
                                this.oldLetterLayout = letterLayout;
                            }
                            this.letterLayout = null;
                        }
                        else if (!letter.equals(this.currentLetter)) {
                            this.letterLayout = new StaticLayout((CharSequence)letter, this.letterPaint, 1000, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                            this.oldLetterLayout = null;
                            if (this.letterLayout.getLineCount() > 0) {
                                this.letterLayout.getLineWidth(0);
                                this.letterLayout.getLineLeft(0);
                                if (LocaleController.isRTL) {
                                    this.textX = AndroidUtilities.dp(10.0f) + (AndroidUtilities.dp(88.0f) - this.letterLayout.getLineWidth(0)) / 2.0f - this.letterLayout.getLineLeft(0);
                                }
                                else {
                                    this.textX = (AndroidUtilities.dp(88.0f) - this.letterLayout.getLineWidth(0)) / 2.0f - this.letterLayout.getLineLeft(0);
                                }
                                this.textY = (float)((AndroidUtilities.dp(88.0f) - this.letterLayout.getHeight()) / 2);
                            }
                        }
                    }
                }
            }
        }
        
        private void setProgress(final float progress) {
            this.progress = progress;
            this.invalidate();
        }
        
        private void updateColors() {
            final int color = Theme.getColor("fastScrollInactive");
            final int color2 = Theme.getColor("fastScrollActive");
            this.paint.setColor(color);
            this.letterPaint.setColor(Theme.getColor("fastScrollText"));
            this.colors[0] = Color.red(color);
            this.colors[1] = Color.red(color2);
            this.colors[2] = Color.green(color);
            this.colors[3] = Color.green(color2);
            this.colors[4] = Color.blue(color);
            this.colors[5] = Color.blue(color2);
            this.invalidate();
        }
        
        public void layout(final int n, final int n2, final int n3, final int n4) {
            if (!RecyclerListView.this.selfOnLayout) {
                return;
            }
            super.layout(n, n2, n3, n4);
        }
        
        protected void onDraw(final Canvas canvas) {
            final Paint paint = this.paint;
            final int[] colors = this.colors;
            final int n = colors[0];
            final float n2 = (float)(colors[1] - colors[0]);
            final float bubbleProgress = this.bubbleProgress;
            paint.setColor(Color.argb(255, n + (int)(n2 * bubbleProgress), colors[2] + (int)((colors[3] - colors[2]) * bubbleProgress), colors[4] + (int)((colors[5] - colors[4]) * bubbleProgress)));
            final int n3 = (int)Math.ceil((this.getMeasuredHeight() - AndroidUtilities.dp(54.0f)) * this.progress);
            this.rect.set((float)this.scrollX, (float)(AndroidUtilities.dp(12.0f) + n3), (float)(this.scrollX + AndroidUtilities.dp(5.0f)), (float)(AndroidUtilities.dp(42.0f) + n3));
            canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), this.paint);
            if (this.pressed || this.bubbleProgress != 0.0f) {
                this.paint.setAlpha((int)(this.bubbleProgress * 255.0f));
                final int dp = AndroidUtilities.dp(30.0f);
                int dp2 = n3 - AndroidUtilities.dp(46.0f);
                float n4;
                if (dp2 <= AndroidUtilities.dp(12.0f)) {
                    n4 = (float)(AndroidUtilities.dp(12.0f) - dp2);
                    dp2 = AndroidUtilities.dp(12.0f);
                }
                else {
                    n4 = 0.0f;
                }
                canvas.translate((float)AndroidUtilities.dp(10.0f), (float)dp2);
                float n6;
                float n7;
                if (n4 <= AndroidUtilities.dp(29.0f)) {
                    final float n5 = (float)AndroidUtilities.dp(44.0f);
                    n6 = AndroidUtilities.dp(4.0f) + n4 / AndroidUtilities.dp(29.0f) * AndroidUtilities.dp(40.0f);
                    n7 = n5;
                }
                else {
                    final float n8 = (float)AndroidUtilities.dp(29.0f);
                    n6 = (float)AndroidUtilities.dp(44.0f);
                    n7 = AndroidUtilities.dp(4.0f) + (1.0f - (n4 - n8) / AndroidUtilities.dp(29.0f)) * AndroidUtilities.dp(40.0f);
                }
                Label_0585: {
                    Label_0430: {
                        if (LocaleController.isRTL) {
                            final float[] radii = this.radii;
                            if (radii[0] != n7 || radii[6] != n6) {
                                break Label_0430;
                            }
                        }
                        if (LocaleController.isRTL) {
                            break Label_0585;
                        }
                        final float[] radii2 = this.radii;
                        if (radii2[2] == n7 && radii2[4] == n6) {
                            break Label_0585;
                        }
                    }
                    if (LocaleController.isRTL) {
                        final float[] radii3 = this.radii;
                        radii3[0] = (radii3[1] = n7);
                        radii3[6] = (radii3[7] = n6);
                    }
                    else {
                        final float[] radii4 = this.radii;
                        radii4[2] = (radii4[3] = n7);
                        radii4[4] = (radii4[5] = n6);
                    }
                    this.path.reset();
                    final RectF rect = this.rect;
                    float n9;
                    if (LocaleController.isRTL) {
                        n9 = (float)AndroidUtilities.dp(10.0f);
                    }
                    else {
                        n9 = 0.0f;
                    }
                    float n10;
                    if (LocaleController.isRTL) {
                        n10 = 98.0f;
                    }
                    else {
                        n10 = 88.0f;
                    }
                    rect.set(n9, 0.0f, (float)AndroidUtilities.dp(n10), (float)AndroidUtilities.dp(88.0f));
                    this.path.addRoundRect(this.rect, this.radii, Path$Direction.CW);
                    this.path.close();
                }
                StaticLayout staticLayout = this.letterLayout;
                if (staticLayout == null) {
                    staticLayout = this.oldLetterLayout;
                }
                if (staticLayout != null) {
                    canvas.save();
                    final float bubbleProgress2 = this.bubbleProgress;
                    canvas.scale(bubbleProgress2, bubbleProgress2, (float)this.scrollX, (float)(dp + n3 - dp2));
                    canvas.drawPath(this.path, this.paint);
                    canvas.translate(this.textX, this.textY);
                    staticLayout.draw(canvas);
                    canvas.restore();
                }
            }
            if ((this.pressed && this.letterLayout != null && this.bubbleProgress < 1.0f) || ((!this.pressed || this.letterLayout == null) && this.bubbleProgress > 0.0f)) {
                final long currentTimeMillis = System.currentTimeMillis();
                final long n11 = currentTimeMillis - this.lastUpdateTime;
                long n12 = 0L;
                Label_0757: {
                    if (n11 >= 0L) {
                        n12 = n11;
                        if (n11 <= 17L) {
                            break Label_0757;
                        }
                    }
                    n12 = 17L;
                }
                this.lastUpdateTime = currentTimeMillis;
                this.invalidate();
                if (this.pressed && this.letterLayout != null) {
                    this.bubbleProgress += n12 / 120.0f;
                    if (this.bubbleProgress > 1.0f) {
                        this.bubbleProgress = 1.0f;
                    }
                }
                else {
                    this.bubbleProgress -= n12 / 120.0f;
                    if (this.bubbleProgress < 0.0f) {
                        this.bubbleProgress = 0.0f;
                    }
                }
            }
        }
        
        protected void onMeasure(final int n, final int n2) {
            this.setMeasuredDimension(AndroidUtilities.dp(132.0f), View$MeasureSpec.getSize(n2));
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final int action = motionEvent.getAction();
            if (action != 0) {
                if (action != 1) {
                    if (action != 2) {
                        if (action != 3) {
                            return super.onTouchEvent(motionEvent);
                        }
                    }
                    else {
                        if (!this.pressed) {
                            return true;
                        }
                        final float y = motionEvent.getY();
                        float lastY = AndroidUtilities.dp(12.0f) + this.startDy;
                        final float n = this.getMeasuredHeight() - AndroidUtilities.dp(42.0f) + this.startDy;
                        if (y >= lastY) {
                            lastY = y;
                            if (y > n) {
                                lastY = n;
                            }
                        }
                        final float lastY2 = this.lastY;
                        this.lastY = lastY;
                        this.progress += (lastY - lastY2) / (this.getMeasuredHeight() - AndroidUtilities.dp(54.0f));
                        final float progress = this.progress;
                        if (progress < 0.0f) {
                            this.progress = 0.0f;
                        }
                        else if (progress > 1.0f) {
                            this.progress = 1.0f;
                        }
                        this.getCurrentLetter();
                        this.invalidate();
                        return true;
                    }
                }
                this.pressed = false;
                this.lastUpdateTime = System.currentTimeMillis();
                this.invalidate();
                return true;
            }
            final float x = motionEvent.getX();
            this.lastY = motionEvent.getY();
            final float n2 = (float)Math.ceil((this.getMeasuredHeight() - AndroidUtilities.dp(54.0f)) * this.progress) + AndroidUtilities.dp(12.0f);
            if ((!LocaleController.isRTL || x <= AndroidUtilities.dp(25.0f)) && (LocaleController.isRTL || x >= AndroidUtilities.dp(107.0f))) {
                final float lastY3 = this.lastY;
                if (lastY3 >= n2) {
                    if (lastY3 <= AndroidUtilities.dp(30.0f) + n2) {
                        this.startDy = this.lastY - n2;
                        this.pressed = true;
                        this.lastUpdateTime = System.currentTimeMillis();
                        this.getCurrentLetter();
                        this.invalidate();
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    public abstract static class FastScrollAdapter extends SelectionAdapter
    {
        public abstract String getLetter(final int p0);
        
        public abstract int getPositionForScrollProgress(final float p0);
    }
    
    public static class Holder extends ViewHolder
    {
        public Holder(final View view) {
            super(view);
        }
    }
    
    public interface IntReturnCallback
    {
        int run();
    }
    
    public interface OnInterceptTouchListener
    {
        boolean onInterceptTouchEvent(final MotionEvent p0);
    }
    
    public interface OnItemClickListener
    {
        void onItemClick(final View p0, final int p1);
    }
    
    public interface OnItemClickListenerExtended
    {
        void onItemClick(final View p0, final int p1, final float p2, final float p3);
    }
    
    public interface OnItemLongClickListener
    {
        boolean onItemClick(final View p0, final int p1);
    }
    
    public interface OnItemLongClickListenerExtended
    {
        boolean onItemClick(final View p0, final int p1, final float p2, final float p3);
        
        void onLongClickRelease();
        
        void onMove(final float p0, final float p1);
    }
    
    private class RecyclerListViewItemClickListener implements OnItemTouchListener
    {
        public RecyclerListViewItemClickListener(final Context context) {
            RecyclerListView.this.gestureDetector = new GestureDetector(context, (GestureDetector$OnGestureListener)new GestureDetector$OnGestureListener() {
                public boolean onDown(final MotionEvent motionEvent) {
                    return false;
                }
                
                public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
                    return false;
                }
                
                public void onLongPress(final MotionEvent motionEvent) {
                    if (RecyclerListView.this.currentChildView != null && RecyclerListView.this.currentChildPosition != -1) {
                        if (RecyclerListView.this.onItemLongClickListener != null || RecyclerListView.this.onItemLongClickListenerExtended != null) {
                            final View access$200 = RecyclerListView.this.currentChildView;
                            if (RecyclerListView.this.onItemLongClickListener != null) {
                                if (RecyclerListView.this.onItemLongClickListener.onItemClick(RecyclerListView.this.currentChildView, RecyclerListView.this.currentChildPosition)) {
                                    access$200.performHapticFeedback(0);
                                    access$200.sendAccessibilityEvent(2);
                                }
                            }
                            else if (RecyclerListView.this.onItemLongClickListenerExtended != null && RecyclerListView.this.onItemLongClickListenerExtended.onItemClick(RecyclerListView.this.currentChildView, RecyclerListView.this.currentChildPosition, motionEvent.getX() - RecyclerListView.this.currentChildView.getX(), motionEvent.getY() - RecyclerListView.this.currentChildView.getY())) {
                                access$200.performHapticFeedback(0);
                                access$200.sendAccessibilityEvent(2);
                                RecyclerListView.this.longPressCalled = true;
                            }
                        }
                    }
                }
                
                public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
                    return false;
                }
                
                public void onShowPress(final MotionEvent motionEvent) {
                }
                
                public boolean onSingleTapUp(final MotionEvent motionEvent) {
                    if (RecyclerListView.this.currentChildView != null && (RecyclerListView.this.onItemClickListener != null || RecyclerListView.this.onItemClickListenerExtended != null)) {
                        final RecyclerListView this$0 = RecyclerListView.this;
                        this$0.onChildPressed(this$0.currentChildView, true);
                        final View access$200 = RecyclerListView.this.currentChildView;
                        final int access$201 = RecyclerListView.this.currentChildPosition;
                        final float x = motionEvent.getX();
                        final float y = motionEvent.getY();
                        if (RecyclerListView.this.instantClick && access$201 != -1) {
                            access$200.playSoundEffect(0);
                            access$200.sendAccessibilityEvent(1);
                            if (RecyclerListView.this.onItemClickListener != null) {
                                RecyclerListView.this.onItemClickListener.onItemClick(access$200, access$201);
                            }
                            else if (RecyclerListView.this.onItemClickListenerExtended != null) {
                                RecyclerListView.this.onItemClickListenerExtended.onItemClick(access$200, access$201, x - access$200.getX(), y - access$200.getY());
                            }
                        }
                        final RecyclerListView this$2 = RecyclerListView.this;
                        final Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (this == RecyclerListView.this.clickRunnable) {
                                    RecyclerListView.this.clickRunnable = null;
                                }
                                final View val$view = access$200;
                                if (val$view != null) {
                                    RecyclerListView.this.onChildPressed(val$view, false);
                                    if (!RecyclerListView.this.instantClick) {
                                        access$200.playSoundEffect(0);
                                        access$200.sendAccessibilityEvent(1);
                                        if (access$201 != -1) {
                                            if (RecyclerListView.this.onItemClickListener != null) {
                                                RecyclerListView.this.onItemClickListener.onItemClick(access$200, access$201);
                                            }
                                            else if (RecyclerListView.this.onItemClickListenerExtended != null) {
                                                final OnItemClickListenerExtended access$400 = RecyclerListView.this.onItemClickListenerExtended;
                                                final View val$view2 = access$200;
                                                access$400.onItemClick(val$view2, access$201, x - val$view2.getX(), y - access$200.getY());
                                            }
                                        }
                                    }
                                }
                            }
                        };
                        this$2.clickRunnable = runnable;
                        AndroidUtilities.runOnUIThread(runnable, ViewConfiguration.getPressedStateDuration());
                        if (RecyclerListView.this.selectChildRunnable != null) {
                            final View access$202 = RecyclerListView.this.currentChildView;
                            AndroidUtilities.cancelRunOnUIThread(RecyclerListView.this.selectChildRunnable);
                            RecyclerListView.this.selectChildRunnable = null;
                            RecyclerListView.this.currentChildView = null;
                            RecyclerListView.this.interceptedByChild = false;
                            RecyclerListView.this.removeSelection(access$202, motionEvent);
                        }
                    }
                    return true;
                }
            });
            RecyclerListView.this.gestureDetector.setIsLongpressEnabled(false);
        }
        
        @Override
        public boolean onInterceptTouchEvent(final RecyclerView recyclerView, final MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            final boolean b = RecyclerListView.this.getScrollState() == 0;
            if ((actionMasked == 0 || actionMasked == 5) && RecyclerListView.this.currentChildView == null && b) {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                RecyclerListView.this.longPressCalled = false;
                if (RecyclerListView.this.allowSelectChildAtPosition(x, y)) {
                    final RecyclerListView this$0 = RecyclerListView.this;
                    this$0.currentChildView = this$0.findChildViewUnder(x, y);
                }
                if (RecyclerListView.this.currentChildView instanceof ViewGroup) {
                    final float n = motionEvent.getX() - RecyclerListView.this.currentChildView.getLeft();
                    final float n2 = motionEvent.getY() - RecyclerListView.this.currentChildView.getTop();
                    final ViewGroup viewGroup = (ViewGroup)RecyclerListView.this.currentChildView;
                    for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                        final View child = viewGroup.getChildAt(i);
                        if (n >= child.getLeft() && n <= child.getRight() && n2 >= child.getTop() && n2 <= child.getBottom() && child.isClickable()) {
                            RecyclerListView.this.currentChildView = null;
                            break;
                        }
                    }
                }
                RecyclerListView.this.currentChildPosition = -1;
                if (RecyclerListView.this.currentChildView != null) {
                    final RecyclerListView this$2 = RecyclerListView.this;
                    this$2.currentChildPosition = recyclerView.getChildPosition(this$2.currentChildView);
                    final MotionEvent obtain = MotionEvent.obtain(0L, 0L, motionEvent.getActionMasked(), motionEvent.getX() - RecyclerListView.this.currentChildView.getLeft(), motionEvent.getY() - RecyclerListView.this.currentChildView.getTop(), 0);
                    if (RecyclerListView.this.currentChildView.onTouchEvent(obtain)) {
                        RecyclerListView.this.interceptedByChild = true;
                    }
                    obtain.recycle();
                }
            }
            if (RecyclerListView.this.currentChildView != null && !RecyclerListView.this.interceptedByChild && motionEvent != null) {
                try {
                    RecyclerListView.this.gestureDetector.onTouchEvent(motionEvent);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            if (actionMasked != 0 && actionMasked != 5) {
                if ((actionMasked == 1 || actionMasked == 6 || actionMasked == 3 || !b) && RecyclerListView.this.currentChildView != null) {
                    if (RecyclerListView.this.selectChildRunnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(RecyclerListView.this.selectChildRunnable);
                        RecyclerListView.this.selectChildRunnable = null;
                    }
                    final View access$200 = RecyclerListView.this.currentChildView;
                    final RecyclerListView this$3 = RecyclerListView.this;
                    this$3.onChildPressed(this$3.currentChildView, false);
                    RecyclerListView.this.currentChildView = null;
                    RecyclerListView.this.interceptedByChild = false;
                    RecyclerListView.this.removeSelection(access$200, motionEvent);
                    if ((actionMasked == 1 || actionMasked == 6 || actionMasked == 3) && RecyclerListView.this.onItemLongClickListenerExtended != null && RecyclerListView.this.longPressCalled) {
                        RecyclerListView.this.onItemLongClickListenerExtended.onLongClickRelease();
                        RecyclerListView.this.longPressCalled = false;
                    }
                }
            }
            else if (!RecyclerListView.this.interceptedByChild && RecyclerListView.this.currentChildView != null) {
                RecyclerListView.this.selectChildRunnable = new _$$Lambda$RecyclerListView$RecyclerListViewItemClickListener$XWyZ4ltKefT0aojGOGmh_BAE5L4(this);
                AndroidUtilities.runOnUIThread(RecyclerListView.this.selectChildRunnable, ViewConfiguration.getTapTimeout());
                if (RecyclerListView.this.currentChildView.isEnabled()) {
                    final RecyclerListView this$4 = RecyclerListView.this;
                    this$4.positionSelector(this$4.currentChildPosition, RecyclerListView.this.currentChildView);
                    if (RecyclerListView.this.selectorDrawable != null) {
                        final Drawable current = RecyclerListView.this.selectorDrawable.getCurrent();
                        if (current instanceof TransitionDrawable) {
                            if (RecyclerListView.this.onItemLongClickListener == null && RecyclerListView.this.onItemClickListenerExtended == null) {
                                ((TransitionDrawable)current).resetTransition();
                            }
                            else {
                                ((TransitionDrawable)current).startTransition(ViewConfiguration.getLongPressTimeout());
                            }
                        }
                        if (Build$VERSION.SDK_INT >= 21) {
                            RecyclerListView.this.selectorDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                        }
                    }
                    RecyclerListView.this.updateSelectorState();
                }
                else {
                    RecyclerListView.this.selectorRect.setEmpty();
                }
            }
            return false;
        }
        
        @Override
        public void onRequestDisallowInterceptTouchEvent(final boolean b) {
            RecyclerListView.this.cancelClickRunnables(true);
        }
        
        @Override
        public void onTouchEvent(final RecyclerView recyclerView, final MotionEvent motionEvent) {
        }
    }
    
    public abstract static class SectionsAdapter extends FastScrollAdapter
    {
        private int count;
        private SparseIntArray sectionCache;
        private int sectionCount;
        private SparseIntArray sectionCountCache;
        private SparseIntArray sectionPositionCache;
        
        public SectionsAdapter() {
            this.cleanupCache();
        }
        
        private void cleanupCache() {
            final SparseIntArray sectionCache = this.sectionCache;
            if (sectionCache == null) {
                this.sectionCache = new SparseIntArray();
                this.sectionPositionCache = new SparseIntArray();
                this.sectionCountCache = new SparseIntArray();
            }
            else {
                sectionCache.clear();
                this.sectionPositionCache.clear();
                this.sectionCountCache.clear();
            }
            this.count = -1;
            this.sectionCount = -1;
        }
        
        private int internalGetCountForSection(final int n) {
            final int value = this.sectionCountCache.get(n, Integer.MAX_VALUE);
            if (value != Integer.MAX_VALUE) {
                return value;
            }
            final int countForSection = this.getCountForSection(n);
            this.sectionCountCache.put(n, countForSection);
            return countForSection;
        }
        
        private int internalGetSectionCount() {
            final int sectionCount = this.sectionCount;
            if (sectionCount >= 0) {
                return sectionCount;
            }
            return this.sectionCount = this.getSectionCount();
        }
        
        public abstract int getCountForSection(final int p0);
        
        public final Object getItem(final int n) {
            return this.getItem(this.getSectionForPosition(n), this.getPositionInSectionForPosition(n));
        }
        
        public abstract Object getItem(final int p0, final int p1);
        
        @Override
        public int getItemCount() {
            final int count = this.count;
            if (count >= 0) {
                return count;
            }
            int i = 0;
            this.count = 0;
            while (i < this.internalGetSectionCount()) {
                this.count += this.internalGetCountForSection(i);
                ++i;
            }
            return this.count;
        }
        
        @Override
        public final int getItemViewType(final int n) {
            return this.getItemViewType(this.getSectionForPosition(n), this.getPositionInSectionForPosition(n));
        }
        
        public abstract int getItemViewType(final int p0, final int p1);
        
        public int getPositionInSectionForPosition(final int n) {
            final int value = this.sectionPositionCache.get(n, Integer.MAX_VALUE);
            if (value != Integer.MAX_VALUE) {
                return value;
            }
            final int internalGetSectionCount = this.internalGetSectionCount();
            int i = 0;
            int n2 = 0;
            while (i < internalGetSectionCount) {
                final int n3 = this.internalGetCountForSection(i) + n2;
                if (n >= n2 && n < n3) {
                    final int n4 = n - n2;
                    this.sectionPositionCache.put(n, n4);
                    return n4;
                }
                ++i;
                n2 = n3;
            }
            return -1;
        }
        
        public abstract int getSectionCount();
        
        public final int getSectionForPosition(final int n) {
            final int value = this.sectionCache.get(n, Integer.MAX_VALUE);
            if (value != Integer.MAX_VALUE) {
                return value;
            }
            final int internalGetSectionCount = this.internalGetSectionCount();
            int i = 0;
            int n2 = 0;
            while (i < internalGetSectionCount) {
                final int n3 = this.internalGetCountForSection(i) + n2;
                if (n >= n2 && n < n3) {
                    this.sectionCache.put(n, i);
                    return i;
                }
                ++i;
                n2 = n3;
            }
            return -1;
        }
        
        public abstract View getSectionHeaderView(final int p0, final View p1);
        
        public abstract boolean isEnabled(final int p0, final int p1);
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return this.isEnabled(this.getSectionForPosition(adapterPosition), this.getPositionInSectionForPosition(adapterPosition));
        }
        
        @Override
        public void notifyDataSetChanged() {
            this.cleanupCache();
            super.notifyDataSetChanged();
        }
        
        public void notifySectionsChanged() {
            this.cleanupCache();
        }
        
        public abstract void onBindViewHolder(final int p0, final int p1, final ViewHolder p2);
        
        @Override
        public final void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            this.onBindViewHolder(this.getSectionForPosition(n), this.getPositionInSectionForPosition(n), viewHolder);
        }
    }
    
    public abstract static class SelectionAdapter extends Adapter
    {
        public int getSelectionBottomPadding(final View view) {
            return 0;
        }
        
        public abstract boolean isEnabled(final ViewHolder p0);
    }
}

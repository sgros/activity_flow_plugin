// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import java.util.Collection;
import java.util.Iterator;
import android.animation.AnimatorSet;
import java.util.List;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Property;
import android.animation.ObjectAnimator;
import android.view.ViewOutlineProvider;
import android.os.Build$VERSION;
import android.view.animation.DecelerateInterpolator;
import org.telegram.ui.Cells.DialogCell;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.animation.TimeInterpolator;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class DialogsItemAnimator extends SimpleItemAnimator
{
    private static final boolean DEBUG = false;
    private static final int changeDuration = 180;
    private static final int deleteDuration = 180;
    private static TimeInterpolator sDefaultInterpolator;
    private int bottomClip;
    ArrayList<ViewHolder> mAddAnimations;
    ArrayList<ArrayList<ViewHolder>> mAdditionsList;
    ArrayList<ViewHolder> mChangeAnimations;
    ArrayList<ArrayList<ChangeInfo>> mChangesList;
    ArrayList<ViewHolder> mMoveAnimations;
    ArrayList<ArrayList<MoveInfo>> mMovesList;
    private ArrayList<ViewHolder> mPendingAdditions;
    private ArrayList<ChangeInfo> mPendingChanges;
    private ArrayList<MoveInfo> mPendingMoves;
    private ArrayList<ViewHolder> mPendingRemovals;
    ArrayList<ViewHolder> mRemoveAnimations;
    private DialogCell removingDialog;
    private int topClip;
    
    static {
        DialogsItemAnimator.sDefaultInterpolator = (TimeInterpolator)new DecelerateInterpolator();
    }
    
    public DialogsItemAnimator() {
        this.mPendingRemovals = new ArrayList<ViewHolder>();
        this.mPendingAdditions = new ArrayList<ViewHolder>();
        this.mPendingMoves = new ArrayList<MoveInfo>();
        this.mPendingChanges = new ArrayList<ChangeInfo>();
        this.mAdditionsList = new ArrayList<ArrayList<ViewHolder>>();
        this.mMovesList = new ArrayList<ArrayList<MoveInfo>>();
        this.mChangesList = new ArrayList<ArrayList<ChangeInfo>>();
        this.mAddAnimations = new ArrayList<ViewHolder>();
        this.mMoveAnimations = new ArrayList<ViewHolder>();
        this.mRemoveAnimations = new ArrayList<ViewHolder>();
        this.mChangeAnimations = new ArrayList<ViewHolder>();
    }
    
    private void animateRemoveImpl(final ViewHolder e) {
        final View itemView = e.itemView;
        this.mRemoveAnimations.add(e);
        if (itemView instanceof DialogCell) {
            final DialogCell removingDialog = (DialogCell)itemView;
            this.removingDialog = removingDialog;
            if (this.topClip != Integer.MAX_VALUE) {
                final int measuredHeight = this.removingDialog.getMeasuredHeight();
                final int topClip = this.topClip;
                this.bottomClip = measuredHeight - topClip;
                this.removingDialog.setTopClip(topClip);
                this.removingDialog.setBottomClip(this.bottomClip);
            }
            else if (this.bottomClip != Integer.MAX_VALUE) {
                this.topClip = this.removingDialog.getMeasuredHeight() - this.bottomClip;
                this.removingDialog.setTopClip(this.topClip);
                this.removingDialog.setBottomClip(this.bottomClip);
            }
            if (Build$VERSION.SDK_INT >= 21) {
                removingDialog.setElevation(-1.0f);
                removingDialog.setOutlineProvider((ViewOutlineProvider)null);
            }
            final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)removingDialog, (Property)AnimationProperties.CLIP_DIALOG_CELL_PROGRESS, new float[] { 1.0f }).setDuration(180L);
            setDuration.setInterpolator(DialogsItemAnimator.sDefaultInterpolator);
            setDuration.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    animator.removeAllListeners();
                    removingDialog.setClipProgress(0.0f);
                    if (Build$VERSION.SDK_INT >= 21) {
                        removingDialog.setElevation(0.0f);
                    }
                    DialogsItemAnimator.this.dispatchRemoveFinished(e);
                    DialogsItemAnimator.this.mRemoveAnimations.remove(e);
                    DialogsItemAnimator.this.dispatchFinishedWhenDone();
                }
                
                public void onAnimationStart(final Animator animator) {
                    DialogsItemAnimator.this.dispatchRemoveStarting(e);
                }
            });
            setDuration.start();
        }
        else {
            final ViewPropertyAnimator animate = itemView.animate();
            animate.setDuration(180L).alpha(0.0f).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    animate.setListener((Animator$AnimatorListener)null);
                    itemView.setAlpha(1.0f);
                    DialogsItemAnimator.this.dispatchRemoveFinished(e);
                    DialogsItemAnimator.this.mRemoveAnimations.remove(e);
                    DialogsItemAnimator.this.dispatchFinishedWhenDone();
                }
                
                public void onAnimationStart(final Animator animator) {
                    DialogsItemAnimator.this.dispatchRemoveStarting(e);
                }
            }).start();
        }
    }
    
    private void endChangeAnimation(final List<ChangeInfo> list, final ViewHolder viewHolder) {
        for (int i = list.size() - 1; i >= 0; --i) {
            final ChangeInfo changeInfo = list.get(i);
            if (this.endChangeAnimationIfNecessary(changeInfo, viewHolder) && changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                list.remove(changeInfo);
            }
        }
    }
    
    private void endChangeAnimationIfNecessary(final ChangeInfo changeInfo) {
        final ViewHolder oldHolder = changeInfo.oldHolder;
        if (oldHolder != null) {
            this.endChangeAnimationIfNecessary(changeInfo, oldHolder);
        }
        final ViewHolder newHolder = changeInfo.newHolder;
        if (newHolder != null) {
            this.endChangeAnimationIfNecessary(changeInfo, newHolder);
        }
    }
    
    private boolean endChangeAnimationIfNecessary(final ChangeInfo changeInfo, final ViewHolder viewHolder) {
        final ViewHolder newHolder = changeInfo.newHolder;
        boolean b = false;
        if (newHolder == viewHolder) {
            changeInfo.newHolder = null;
        }
        else {
            if (changeInfo.oldHolder != viewHolder) {
                return false;
            }
            changeInfo.oldHolder = null;
            b = true;
        }
        viewHolder.itemView.setAlpha(1.0f);
        viewHolder.itemView.setTranslationX(0.0f);
        viewHolder.itemView.setTranslationY(0.0f);
        this.dispatchChangeFinished(viewHolder, b);
        return true;
    }
    
    private void resetAnimation(final ViewHolder viewHolder) {
        viewHolder.itemView.animate().setInterpolator(DialogsItemAnimator.sDefaultInterpolator);
        this.endAnimation(viewHolder);
    }
    
    @Override
    public boolean animateAdd(final ViewHolder e) {
        this.resetAnimation(e);
        final View itemView = e.itemView;
        if (itemView instanceof DialogCell) {
            ((DialogCell)itemView).setClipProgress(1.0f);
        }
        else {
            itemView.setAlpha(0.0f);
        }
        this.mPendingAdditions.add(e);
        return true;
    }
    
    void animateAddImpl(final ViewHolder e) {
        final View itemView = e.itemView;
        this.mAddAnimations.add(e);
        if (itemView instanceof DialogCell) {
            final DialogCell dialogCell = (DialogCell)itemView;
            final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)dialogCell, (Property)AnimationProperties.CLIP_DIALOG_CELL_PROGRESS, new float[] { 0.0f }).setDuration(180L);
            setDuration.setInterpolator(DialogsItemAnimator.sDefaultInterpolator);
            setDuration.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator animator) {
                    dialogCell.setClipProgress(0.0f);
                }
                
                public void onAnimationEnd(final Animator animator) {
                    animator.removeAllListeners();
                    DialogsItemAnimator.this.dispatchAddFinished(e);
                    DialogsItemAnimator.this.mAddAnimations.remove(e);
                    DialogsItemAnimator.this.dispatchFinishedWhenDone();
                }
                
                public void onAnimationStart(final Animator animator) {
                    DialogsItemAnimator.this.dispatchAddStarting(e);
                }
            });
            setDuration.start();
        }
        else {
            final ViewPropertyAnimator animate = itemView.animate();
            animate.alpha(1.0f).setDuration(180L).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator animator) {
                    itemView.setAlpha(1.0f);
                }
                
                public void onAnimationEnd(final Animator animator) {
                    animate.setListener((Animator$AnimatorListener)null);
                    DialogsItemAnimator.this.dispatchAddFinished(e);
                    DialogsItemAnimator.this.mAddAnimations.remove(e);
                    DialogsItemAnimator.this.dispatchFinishedWhenDone();
                }
                
                public void onAnimationStart(final Animator animator) {
                    DialogsItemAnimator.this.dispatchAddStarting(e);
                }
            }).start();
        }
    }
    
    @Override
    public boolean animateChange(final ViewHolder viewHolder, final ViewHolder viewHolder2, final int n, final int n2, final int n3, final int n4) {
        if (viewHolder.itemView instanceof DialogCell) {
            this.resetAnimation(viewHolder);
            this.resetAnimation(viewHolder2);
            viewHolder.itemView.setAlpha(1.0f);
            viewHolder2.itemView.setAlpha(0.0f);
            viewHolder2.itemView.setTranslationX(0.0f);
            this.mPendingChanges.add(new ChangeInfo(viewHolder, viewHolder2, n, n2, n3, n4));
            return true;
        }
        return false;
    }
    
    void animateChangeImpl(final ChangeInfo changeInfo) {
        final ViewHolder oldHolder = changeInfo.oldHolder;
        final ViewHolder newHolder = changeInfo.newHolder;
        if (oldHolder != null) {
            if (newHolder != null) {
                final AnimatorSet set = new AnimatorSet();
                set.setDuration(180L);
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)oldHolder.itemView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)newHolder.itemView, View.ALPHA, new float[] { 1.0f }) });
                this.mChangeAnimations.add(changeInfo.oldHolder);
                this.mChangeAnimations.add(changeInfo.newHolder);
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        oldHolder.itemView.setAlpha(1.0f);
                        set.removeAllListeners();
                        DialogsItemAnimator.this.dispatchChangeFinished(changeInfo.oldHolder, true);
                        DialogsItemAnimator.this.mChangeAnimations.remove(changeInfo.oldHolder);
                        DialogsItemAnimator.this.dispatchFinishedWhenDone();
                        DialogsItemAnimator.this.dispatchChangeFinished(changeInfo.newHolder, false);
                        DialogsItemAnimator.this.mChangeAnimations.remove(changeInfo.newHolder);
                        DialogsItemAnimator.this.dispatchFinishedWhenDone();
                    }
                    
                    public void onAnimationStart(final Animator animator) {
                        DialogsItemAnimator.this.dispatchChangeStarting(changeInfo.oldHolder, true);
                        DialogsItemAnimator.this.dispatchChangeStarting(changeInfo.newHolder, false);
                    }
                });
                set.start();
            }
        }
    }
    
    @Override
    public boolean animateMove(final ViewHolder viewHolder, int n, int n2, final int n3, final int n4) {
        final View itemView = viewHolder.itemView;
        n += (int)itemView.getTranslationX();
        n2 += (int)viewHolder.itemView.getTranslationY();
        this.resetAnimation(viewHolder);
        final int n5 = n3 - n;
        final int n6 = n4 - n2;
        if (n5 == 0 && n6 == 0) {
            this.dispatchMoveFinished(viewHolder);
            return false;
        }
        if (n5 != 0) {
            itemView.setTranslationX((float)(-n5));
        }
        if (n6 != 0) {
            itemView.setTranslationY((float)(-n6));
        }
        this.mPendingMoves.add(new MoveInfo(viewHolder, n, n2, n3, n4));
        return true;
    }
    
    void animateMoveImpl(final ViewHolder e, int n, int topClip, int topClip2, int measuredHeight) {
        final View itemView = e.itemView;
        n = topClip2 - n;
        topClip2 = measuredHeight - topClip;
        if (n != 0) {
            itemView.animate().translationX(0.0f);
        }
        if (topClip2 != 0) {
            itemView.animate().translationY(0.0f);
        }
        if (topClip > measuredHeight) {
            this.bottomClip = topClip - measuredHeight;
        }
        else {
            this.topClip = topClip2;
        }
        final DialogCell removingDialog = this.removingDialog;
        if (removingDialog != null) {
            if (this.topClip != Integer.MAX_VALUE) {
                measuredHeight = removingDialog.getMeasuredHeight();
                topClip = this.topClip;
                this.bottomClip = measuredHeight - topClip;
                this.removingDialog.setTopClip(topClip);
                this.removingDialog.setBottomClip(this.bottomClip);
            }
            else if (this.bottomClip != Integer.MAX_VALUE) {
                this.topClip = removingDialog.getMeasuredHeight() - this.bottomClip;
                this.removingDialog.setTopClip(this.topClip);
                this.removingDialog.setBottomClip(this.bottomClip);
            }
        }
        final ViewPropertyAnimator animate = itemView.animate();
        this.mMoveAnimations.add(e);
        animate.setDuration(180L).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                if (n != 0) {
                    itemView.setTranslationX(0.0f);
                }
                if (topClip2 != 0) {
                    itemView.setTranslationY(0.0f);
                }
            }
            
            public void onAnimationEnd(final Animator animator) {
                animate.setListener((Animator$AnimatorListener)null);
                DialogsItemAnimator.this.dispatchMoveFinished(e);
                DialogsItemAnimator.this.mMoveAnimations.remove(e);
                DialogsItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(final Animator animator) {
                DialogsItemAnimator.this.dispatchMoveStarting(e);
            }
        }).start();
    }
    
    @Override
    public boolean animateRemove(final ViewHolder e) {
        this.resetAnimation(e);
        this.mPendingRemovals.add(e);
        return true;
    }
    
    @Override
    public boolean canReuseUpdatedViewHolder(final ViewHolder viewHolder, final List<Object> list) {
        return false;
    }
    
    void cancelAll(final List<ViewHolder> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            list.get(i).itemView.animate().cancel();
        }
    }
    
    void dispatchFinishedWhenDone() {
        if (!this.isRunning()) {
            ((RecyclerView.ItemAnimator)this).dispatchAnimationsFinished();
            this.onAllAnimationsDone();
        }
    }
    
    @Override
    public void endAnimation(final ViewHolder o) {
        final View itemView = o.itemView;
        itemView.animate().cancel();
        for (int i = this.mPendingMoves.size() - 1; i >= 0; --i) {
            if (this.mPendingMoves.get(i).holder == o) {
                itemView.setTranslationY(0.0f);
                itemView.setTranslationX(0.0f);
                this.dispatchMoveFinished(o);
                this.mPendingMoves.remove(i);
            }
        }
        this.endChangeAnimation(this.mPendingChanges, o);
        if (this.mPendingRemovals.remove(o)) {
            if (itemView instanceof DialogCell) {
                ((DialogCell)itemView).setClipProgress(0.0f);
            }
            else {
                itemView.setAlpha(1.0f);
            }
            this.dispatchRemoveFinished(o);
        }
        if (this.mPendingAdditions.remove(o)) {
            if (itemView instanceof DialogCell) {
                ((DialogCell)itemView).setClipProgress(0.0f);
            }
            else {
                itemView.setAlpha(1.0f);
            }
            this.dispatchAddFinished(o);
        }
        for (int j = this.mChangesList.size() - 1; j >= 0; --j) {
            final ArrayList<ChangeInfo> list = this.mChangesList.get(j);
            this.endChangeAnimation(list, o);
            if (list.isEmpty()) {
                this.mChangesList.remove(j);
            }
        }
        for (int k = this.mMovesList.size() - 1; k >= 0; --k) {
            final ArrayList<MoveInfo> list2 = this.mMovesList.get(k);
            int l = list2.size() - 1;
            while (l >= 0) {
                if (list2.get(l).holder == o) {
                    itemView.setTranslationY(0.0f);
                    itemView.setTranslationX(0.0f);
                    this.dispatchMoveFinished(o);
                    list2.remove(l);
                    if (list2.isEmpty()) {
                        this.mMovesList.remove(k);
                        break;
                    }
                    break;
                }
                else {
                    --l;
                }
            }
        }
        for (int n = this.mAdditionsList.size() - 1; n >= 0; --n) {
            final ArrayList<ViewHolder> list3 = this.mAdditionsList.get(n);
            if (list3.remove(o)) {
                if (itemView instanceof DialogCell) {
                    ((DialogCell)itemView).setClipProgress(1.0f);
                }
                else {
                    itemView.setAlpha(1.0f);
                }
                this.dispatchAddFinished(o);
                if (list3.isEmpty()) {
                    this.mAdditionsList.remove(n);
                }
            }
        }
        this.mRemoveAnimations.remove(o);
        this.mAddAnimations.remove(o);
        this.mChangeAnimations.remove(o);
        this.mMoveAnimations.remove(o);
        this.dispatchFinishedWhenDone();
    }
    
    @Override
    public void endAnimations() {
        for (int i = this.mPendingMoves.size() - 1; i >= 0; --i) {
            final MoveInfo moveInfo = this.mPendingMoves.get(i);
            final View itemView = moveInfo.holder.itemView;
            itemView.setTranslationY(0.0f);
            itemView.setTranslationX(0.0f);
            this.dispatchMoveFinished(moveInfo.holder);
            this.mPendingMoves.remove(i);
        }
        for (int j = this.mPendingRemovals.size() - 1; j >= 0; --j) {
            this.dispatchRemoveFinished(this.mPendingRemovals.get(j));
            this.mPendingRemovals.remove(j);
        }
        for (int k = this.mPendingAdditions.size() - 1; k >= 0; --k) {
            final ViewHolder viewHolder = this.mPendingAdditions.get(k);
            final View itemView2 = viewHolder.itemView;
            if (itemView2 instanceof DialogCell) {
                ((DialogCell)itemView2).setClipProgress(0.0f);
            }
            else {
                itemView2.setAlpha(1.0f);
            }
            this.dispatchAddFinished(viewHolder);
            this.mPendingAdditions.remove(k);
        }
        for (int l = this.mPendingChanges.size() - 1; l >= 0; --l) {
            this.endChangeAnimationIfNecessary(this.mPendingChanges.get(l));
        }
        this.mPendingChanges.clear();
        if (!this.isRunning()) {
            return;
        }
        for (int index = this.mMovesList.size() - 1; index >= 0; --index) {
            final ArrayList<MoveInfo> o = this.mMovesList.get(index);
            for (int n = o.size() - 1; n >= 0; --n) {
                final MoveInfo moveInfo2 = o.get(n);
                final View itemView3 = moveInfo2.holder.itemView;
                itemView3.setTranslationY(0.0f);
                itemView3.setTranslationX(0.0f);
                this.dispatchMoveFinished(moveInfo2.holder);
                o.remove(n);
                if (o.isEmpty()) {
                    this.mMovesList.remove(o);
                }
            }
        }
        for (int index2 = this.mAdditionsList.size() - 1; index2 >= 0; --index2) {
            final ArrayList<ViewHolder> o2 = this.mAdditionsList.get(index2);
            for (int n2 = o2.size() - 1; n2 >= 0; --n2) {
                final ViewHolder viewHolder2 = o2.get(n2);
                final View itemView4 = viewHolder2.itemView;
                if (itemView4 instanceof DialogCell) {
                    ((DialogCell)itemView4).setClipProgress(0.0f);
                }
                else {
                    itemView4.setAlpha(1.0f);
                }
                this.dispatchAddFinished(viewHolder2);
                o2.remove(n2);
                if (o2.isEmpty()) {
                    this.mAdditionsList.remove(o2);
                }
            }
        }
        for (int index3 = this.mChangesList.size() - 1; index3 >= 0; --index3) {
            final ArrayList<ChangeInfo> o3 = this.mChangesList.get(index3);
            for (int index4 = o3.size() - 1; index4 >= 0; --index4) {
                this.endChangeAnimationIfNecessary(o3.get(index4));
                if (o3.isEmpty()) {
                    this.mChangesList.remove(o3);
                }
            }
        }
        this.cancelAll(this.mRemoveAnimations);
        this.cancelAll(this.mMoveAnimations);
        this.cancelAll(this.mAddAnimations);
        this.cancelAll(this.mChangeAnimations);
        ((RecyclerView.ItemAnimator)this).dispatchAnimationsFinished();
    }
    
    @Override
    public boolean isRunning() {
        return !this.mPendingAdditions.isEmpty() || !this.mPendingChanges.isEmpty() || !this.mPendingMoves.isEmpty() || !this.mPendingRemovals.isEmpty() || !this.mMoveAnimations.isEmpty() || !this.mRemoveAnimations.isEmpty() || !this.mAddAnimations.isEmpty() || !this.mChangeAnimations.isEmpty() || !this.mMovesList.isEmpty() || !this.mAdditionsList.isEmpty() || !this.mChangesList.isEmpty();
    }
    
    protected void onAllAnimationsDone() {
    }
    
    public void prepareForRemove() {
        this.topClip = Integer.MAX_VALUE;
        this.bottomClip = Integer.MAX_VALUE;
        this.removingDialog = null;
    }
    
    @Override
    public void runPendingAnimations() {
        final boolean empty = this.mPendingRemovals.isEmpty();
        final boolean b = this.mPendingMoves.isEmpty() ^ true;
        final boolean b2 = this.mPendingChanges.isEmpty() ^ true;
        final boolean b3 = this.mPendingAdditions.isEmpty() ^ true;
        if (!(empty ^ true) && !b && !b3 && !b2) {
            return;
        }
        final Iterator<ViewHolder> iterator = this.mPendingRemovals.iterator();
        while (iterator.hasNext()) {
            this.animateRemoveImpl(iterator.next());
        }
        this.mPendingRemovals.clear();
        if (b) {
            final ArrayList<MoveInfo> e = new ArrayList<MoveInfo>(this.mPendingMoves);
            this.mMovesList.add(e);
            this.mPendingMoves.clear();
            new _$$Lambda$DialogsItemAnimator$FzJ8o5Mz2rO6C7ZkKkUswV9PN8U(this, e).run();
        }
        if (b2) {
            final ArrayList<ChangeInfo> e2 = new ArrayList<ChangeInfo>(this.mPendingChanges);
            this.mChangesList.add(e2);
            this.mPendingChanges.clear();
            new _$$Lambda$DialogsItemAnimator$cc5l3oNCPWmFl8oTuclZaKvqaQ8(this, e2).run();
        }
        if (b3) {
            final ArrayList<ViewHolder> e3 = new ArrayList<ViewHolder>(this.mPendingAdditions);
            this.mAdditionsList.add(e3);
            this.mPendingAdditions.clear();
            new _$$Lambda$DialogsItemAnimator$zsvBbBTBPz9JWwcIM9KIgALVodc(this, e3).run();
        }
    }
    
    private static class ChangeInfo
    {
        public int fromX;
        public int fromY;
        public ViewHolder newHolder;
        public ViewHolder oldHolder;
        public int toX;
        public int toY;
        
        private ChangeInfo(final ViewHolder oldHolder, final ViewHolder newHolder) {
            this.oldHolder = oldHolder;
            this.newHolder = newHolder;
        }
        
        ChangeInfo(final ViewHolder viewHolder, final ViewHolder viewHolder2, final int fromX, final int fromY, final int toX, final int toY) {
            this(viewHolder, viewHolder2);
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ChangeInfo{oldHolder=");
            sb.append(this.oldHolder);
            sb.append(", newHolder=");
            sb.append(this.newHolder);
            sb.append(", fromX=");
            sb.append(this.fromX);
            sb.append(", fromY=");
            sb.append(this.fromY);
            sb.append(", toX=");
            sb.append(this.toX);
            sb.append(", toY=");
            sb.append(this.toY);
            sb.append('}');
            return sb.toString();
        }
    }
    
    private static class MoveInfo
    {
        public int fromX;
        public int fromY;
        public ViewHolder holder;
        public int toX;
        public int toY;
        
        MoveInfo(final ViewHolder holder, final int fromX, final int fromY, final int toX, final int toY) {
            this.holder = holder;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }
}

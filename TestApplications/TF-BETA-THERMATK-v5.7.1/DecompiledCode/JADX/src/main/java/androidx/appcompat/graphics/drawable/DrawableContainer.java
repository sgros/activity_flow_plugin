package androidx.appcompat.graphics.drawable;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.util.SparseArray;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.android.exoplayer2.util.NalUnitUtil;

class DrawableContainer extends Drawable implements Callback {
    private int mAlpha = NalUnitUtil.EXTENDED_SAR;
    private Runnable mAnimationRunnable;
    private BlockInvalidateCallback mBlockInvalidateCallback;
    private int mCurIndex = -1;
    private Drawable mCurrDrawable;
    private DrawableContainerState mDrawableContainerState;
    private long mEnterAnimationEnd;
    private long mExitAnimationEnd;
    private boolean mHasAlpha;
    private Rect mHotspotBounds;
    private Drawable mLastDrawable;
    private int mLastIndex = -1;
    private boolean mMutated;

    /* renamed from: androidx.appcompat.graphics.drawable.DrawableContainer$1 */
    class C00011 implements Runnable {
        C00011() {
        }

        public void run() {
            DrawableContainer.this.animate(true);
            DrawableContainer.this.invalidateSelf();
        }
    }

    static class BlockInvalidateCallback implements Callback {
        private Callback mCallback;

        public void invalidateDrawable(Drawable drawable) {
        }

        BlockInvalidateCallback() {
        }

        public BlockInvalidateCallback wrap(Callback callback) {
            this.mCallback = callback;
            return this;
        }

        public Callback unwrap() {
            Callback callback = this.mCallback;
            this.mCallback = null;
            return callback;
        }

        public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
            Callback callback = this.mCallback;
            if (callback != null) {
                callback.scheduleDrawable(drawable, runnable, j);
            }
        }

        public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
            Callback callback = this.mCallback;
            if (callback != null) {
                callback.unscheduleDrawable(drawable, runnable);
            }
        }
    }

    static abstract class DrawableContainerState extends ConstantState {
        boolean mAutoMirrored;
        boolean mCanConstantState;
        int mChangingConfigurations;
        boolean mCheckedConstantSize;
        boolean mCheckedConstantState;
        boolean mCheckedOpacity;
        boolean mCheckedPadding;
        boolean mCheckedStateful;
        int mChildrenChangingConfigurations;
        ColorFilter mColorFilter;
        int mConstantHeight;
        int mConstantMinimumHeight;
        int mConstantMinimumWidth;
        Rect mConstantPadding;
        boolean mConstantSize;
        int mConstantWidth;
        int mDensity = 160;
        boolean mDither;
        SparseArray<ConstantState> mDrawableFutures;
        Drawable[] mDrawables;
        int mEnterFadeDuration;
        int mExitFadeDuration;
        boolean mHasColorFilter;
        boolean mHasTintList;
        boolean mHasTintMode;
        int mLayoutDirection;
        boolean mMutated;
        int mNumChildren;
        int mOpacity;
        final DrawableContainer mOwner;
        Resources mSourceRes;
        boolean mStateful;
        ColorStateList mTintList;
        Mode mTintMode;
        boolean mVariablePadding;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:23:0x002d in {6, 14, 15, 19, 22} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        public synchronized boolean canConstantState() {
            /*
            r6 = this;
            monitor-enter(r6);
            r0 = r6.mCheckedConstantState;	 Catch:{ all -> 0x002a }
            if (r0 == 0) goto L_0x0009;	 Catch:{ all -> 0x002a }
            r0 = r6.mCanConstantState;	 Catch:{ all -> 0x002a }
            monitor-exit(r6);
            return r0;
            r6.createAllFutures();	 Catch:{ all -> 0x002a }
            r0 = 1;	 Catch:{ all -> 0x002a }
            r6.mCheckedConstantState = r0;	 Catch:{ all -> 0x002a }
            r1 = r6.mNumChildren;	 Catch:{ all -> 0x002a }
            r2 = r6.mDrawables;	 Catch:{ all -> 0x002a }
            r3 = 0;	 Catch:{ all -> 0x002a }
            r4 = 0;	 Catch:{ all -> 0x002a }
            if (r4 >= r1) goto L_0x0026;	 Catch:{ all -> 0x002a }
            r5 = r2[r4];	 Catch:{ all -> 0x002a }
            r5 = r5.getConstantState();	 Catch:{ all -> 0x002a }
            if (r5 != 0) goto L_0x0023;	 Catch:{ all -> 0x002a }
            r6.mCanConstantState = r3;	 Catch:{ all -> 0x002a }
            monitor-exit(r6);
            return r3;
            r4 = r4 + 1;
            goto L_0x0015;
            r6.mCanConstantState = r0;	 Catch:{ all -> 0x002a }
            monitor-exit(r6);
            return r0;
            r0 = move-exception;
            monitor-exit(r6);
            throw r0;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.graphics.drawable.DrawableContainer$DrawableContainerState.canConstantState():boolean");
        }

        public abstract void mutate();

        DrawableContainerState(DrawableContainerState drawableContainerState, DrawableContainer drawableContainer, Resources resources) {
            int i = 0;
            this.mVariablePadding = false;
            this.mConstantSize = false;
            this.mDither = true;
            this.mEnterFadeDuration = 0;
            this.mExitFadeDuration = 0;
            this.mOwner = drawableContainer;
            Resources resources2 = resources != null ? resources : drawableContainerState != null ? drawableContainerState.mSourceRes : null;
            this.mSourceRes = resources2;
            this.mDensity = DrawableContainer.resolveDensity(resources, drawableContainerState != null ? drawableContainerState.mDensity : 0);
            if (drawableContainerState != null) {
                this.mChangingConfigurations = drawableContainerState.mChangingConfigurations;
                this.mChildrenChangingConfigurations = drawableContainerState.mChildrenChangingConfigurations;
                this.mCheckedConstantState = true;
                this.mCanConstantState = true;
                this.mVariablePadding = drawableContainerState.mVariablePadding;
                this.mConstantSize = drawableContainerState.mConstantSize;
                this.mDither = drawableContainerState.mDither;
                this.mMutated = drawableContainerState.mMutated;
                this.mLayoutDirection = drawableContainerState.mLayoutDirection;
                this.mEnterFadeDuration = drawableContainerState.mEnterFadeDuration;
                this.mExitFadeDuration = drawableContainerState.mExitFadeDuration;
                this.mAutoMirrored = drawableContainerState.mAutoMirrored;
                this.mColorFilter = drawableContainerState.mColorFilter;
                this.mHasColorFilter = drawableContainerState.mHasColorFilter;
                this.mTintList = drawableContainerState.mTintList;
                this.mTintMode = drawableContainerState.mTintMode;
                this.mHasTintList = drawableContainerState.mHasTintList;
                this.mHasTintMode = drawableContainerState.mHasTintMode;
                if (drawableContainerState.mDensity == this.mDensity) {
                    if (drawableContainerState.mCheckedPadding) {
                        this.mConstantPadding = new Rect(drawableContainerState.mConstantPadding);
                        this.mCheckedPadding = true;
                    }
                    if (drawableContainerState.mCheckedConstantSize) {
                        this.mConstantWidth = drawableContainerState.mConstantWidth;
                        this.mConstantHeight = drawableContainerState.mConstantHeight;
                        this.mConstantMinimumWidth = drawableContainerState.mConstantMinimumWidth;
                        this.mConstantMinimumHeight = drawableContainerState.mConstantMinimumHeight;
                        this.mCheckedConstantSize = true;
                    }
                }
                if (drawableContainerState.mCheckedOpacity) {
                    this.mOpacity = drawableContainerState.mOpacity;
                    this.mCheckedOpacity = true;
                }
                if (drawableContainerState.mCheckedStateful) {
                    this.mStateful = drawableContainerState.mStateful;
                    this.mCheckedStateful = true;
                }
                Drawable[] drawableArr = drawableContainerState.mDrawables;
                this.mDrawables = new Drawable[drawableArr.length];
                this.mNumChildren = drawableContainerState.mNumChildren;
                SparseArray sparseArray = drawableContainerState.mDrawableFutures;
                if (sparseArray != null) {
                    this.mDrawableFutures = sparseArray.clone();
                } else {
                    this.mDrawableFutures = new SparseArray(this.mNumChildren);
                }
                int i2 = this.mNumChildren;
                while (i < i2) {
                    if (drawableArr[i] != null) {
                        ConstantState constantState = drawableArr[i].getConstantState();
                        if (constantState != null) {
                            this.mDrawableFutures.put(i, constantState);
                        } else {
                            this.mDrawables[i] = drawableArr[i];
                        }
                    }
                    i++;
                }
                return;
            }
            this.mDrawables = new Drawable[10];
            this.mNumChildren = 0;
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
        }

        public final int addChild(Drawable drawable) {
            int i = this.mNumChildren;
            if (i >= this.mDrawables.length) {
                growArray(i, i + 10);
            }
            drawable.mutate();
            drawable.setVisible(false, true);
            drawable.setCallback(this.mOwner);
            this.mDrawables[i] = drawable;
            this.mNumChildren++;
            this.mChildrenChangingConfigurations = drawable.getChangingConfigurations() | this.mChildrenChangingConfigurations;
            invalidateCache();
            this.mConstantPadding = null;
            this.mCheckedPadding = false;
            this.mCheckedConstantSize = false;
            this.mCheckedConstantState = false;
            return i;
        }

        /* Access modifiers changed, original: 0000 */
        public void invalidateCache() {
            this.mCheckedOpacity = false;
            this.mCheckedStateful = false;
        }

        /* Access modifiers changed, original: final */
        public final int getCapacity() {
            return this.mDrawables.length;
        }

        private void createAllFutures() {
            SparseArray sparseArray = this.mDrawableFutures;
            if (sparseArray != null) {
                int size = sparseArray.size();
                for (int i = 0; i < size; i++) {
                    this.mDrawables[this.mDrawableFutures.keyAt(i)] = prepareDrawable(((ConstantState) this.mDrawableFutures.valueAt(i)).newDrawable(this.mSourceRes));
                }
                this.mDrawableFutures = null;
            }
        }

        private Drawable prepareDrawable(Drawable drawable) {
            if (VERSION.SDK_INT >= 23) {
                drawable.setLayoutDirection(this.mLayoutDirection);
            }
            drawable = drawable.mutate();
            drawable.setCallback(this.mOwner);
            return drawable;
        }

        public final int getChildCount() {
            return this.mNumChildren;
        }

        public final Drawable getChild(int i) {
            Drawable drawable = this.mDrawables[i];
            if (drawable != null) {
                return drawable;
            }
            SparseArray sparseArray = this.mDrawableFutures;
            if (sparseArray != null) {
                int indexOfKey = sparseArray.indexOfKey(i);
                if (indexOfKey >= 0) {
                    Drawable prepareDrawable = prepareDrawable(((ConstantState) this.mDrawableFutures.valueAt(indexOfKey)).newDrawable(this.mSourceRes));
                    this.mDrawables[i] = prepareDrawable;
                    this.mDrawableFutures.removeAt(indexOfKey);
                    if (this.mDrawableFutures.size() == 0) {
                        this.mDrawableFutures = null;
                    }
                    return prepareDrawable;
                }
            }
            return null;
        }

        /* Access modifiers changed, original: final */
        public final boolean setLayoutDirection(int i, int i2) {
            int i3 = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            boolean z = false;
            for (int i4 = 0; i4 < i3; i4++) {
                if (drawableArr[i4] != null) {
                    boolean layoutDirection = VERSION.SDK_INT >= 23 ? drawableArr[i4].setLayoutDirection(i) : false;
                    if (i4 == i2) {
                        z = layoutDirection;
                    }
                }
            }
            this.mLayoutDirection = i;
            return z;
        }

        /* Access modifiers changed, original: final */
        public final void updateDensity(Resources resources) {
            if (resources != null) {
                this.mSourceRes = resources;
                int resolveDensity = DrawableContainer.resolveDensity(resources, this.mDensity);
                int i = this.mDensity;
                this.mDensity = resolveDensity;
                if (i != resolveDensity) {
                    this.mCheckedConstantSize = false;
                    this.mCheckedPadding = false;
                }
            }
        }

        /* Access modifiers changed, original: final */
        public final void applyTheme(Theme theme) {
            if (theme != null) {
                createAllFutures();
                int i = this.mNumChildren;
                Drawable[] drawableArr = this.mDrawables;
                int i2 = 0;
                while (i2 < i) {
                    if (drawableArr[i2] != null && drawableArr[i2].canApplyTheme()) {
                        drawableArr[i2].applyTheme(theme);
                        this.mChildrenChangingConfigurations |= drawableArr[i2].getChangingConfigurations();
                    }
                    i2++;
                }
                updateDensity(theme.getResources());
            }
        }

        public boolean canApplyTheme() {
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            for (int i2 = 0; i2 < i; i2++) {
                Drawable drawable = drawableArr[i2];
                if (drawable == null) {
                    ConstantState constantState = (ConstantState) this.mDrawableFutures.get(i2);
                    if (constantState != null && constantState.canApplyTheme()) {
                        return true;
                    }
                } else if (drawable.canApplyTheme()) {
                    return true;
                }
            }
            return false;
        }

        public final void setVariablePadding(boolean z) {
            this.mVariablePadding = z;
        }

        public final Rect getConstantPadding() {
            if (this.mVariablePadding) {
                return null;
            }
            if (this.mConstantPadding != null || this.mCheckedPadding) {
                return this.mConstantPadding;
            }
            createAllFutures();
            Rect rect = new Rect();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            Rect rect2 = null;
            for (int i2 = 0; i2 < i; i2++) {
                if (drawableArr[i2].getPadding(rect)) {
                    if (rect2 == null) {
                        rect2 = new Rect(0, 0, 0, 0);
                    }
                    int i3 = rect.left;
                    if (i3 > rect2.left) {
                        rect2.left = i3;
                    }
                    i3 = rect.top;
                    if (i3 > rect2.top) {
                        rect2.top = i3;
                    }
                    i3 = rect.right;
                    if (i3 > rect2.right) {
                        rect2.right = i3;
                    }
                    i3 = rect.bottom;
                    if (i3 > rect2.bottom) {
                        rect2.bottom = i3;
                    }
                }
            }
            this.mCheckedPadding = true;
            this.mConstantPadding = rect2;
            return rect2;
        }

        public final void setConstantSize(boolean z) {
            this.mConstantSize = z;
        }

        public final boolean isConstantSize() {
            return this.mConstantSize;
        }

        public final int getConstantWidth() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantWidth;
        }

        public final int getConstantHeight() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantHeight;
        }

        public final int getConstantMinimumWidth() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantMinimumWidth;
        }

        public final int getConstantMinimumHeight() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantMinimumHeight;
        }

        /* Access modifiers changed, original: protected */
        public void computeConstantSize() {
            this.mCheckedConstantSize = true;
            createAllFutures();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            this.mConstantHeight = -1;
            this.mConstantWidth = -1;
            int i2 = 0;
            this.mConstantMinimumHeight = 0;
            this.mConstantMinimumWidth = 0;
            while (i2 < i) {
                Drawable drawable = drawableArr[i2];
                int intrinsicWidth = drawable.getIntrinsicWidth();
                if (intrinsicWidth > this.mConstantWidth) {
                    this.mConstantWidth = intrinsicWidth;
                }
                intrinsicWidth = drawable.getIntrinsicHeight();
                if (intrinsicWidth > this.mConstantHeight) {
                    this.mConstantHeight = intrinsicWidth;
                }
                intrinsicWidth = drawable.getMinimumWidth();
                if (intrinsicWidth > this.mConstantMinimumWidth) {
                    this.mConstantMinimumWidth = intrinsicWidth;
                }
                int minimumHeight = drawable.getMinimumHeight();
                if (minimumHeight > this.mConstantMinimumHeight) {
                    this.mConstantMinimumHeight = minimumHeight;
                }
                i2++;
            }
        }

        public final void setEnterFadeDuration(int i) {
            this.mEnterFadeDuration = i;
        }

        public final void setExitFadeDuration(int i) {
            this.mExitFadeDuration = i;
        }

        public final int getOpacity() {
            if (this.mCheckedOpacity) {
                return this.mOpacity;
            }
            createAllFutures();
            int i = this.mNumChildren;
            Drawable[] drawableArr = this.mDrawables;
            int opacity = i > 0 ? drawableArr[0].getOpacity() : -2;
            for (int i2 = 1; i2 < i; i2++) {
                opacity = Drawable.resolveOpacity(opacity, drawableArr[i2].getOpacity());
            }
            this.mOpacity = opacity;
            this.mCheckedOpacity = true;
            return opacity;
        }

        public void growArray(int i, int i2) {
            Drawable[] drawableArr = new Drawable[i2];
            System.arraycopy(this.mDrawables, 0, drawableArr, 0, i);
            this.mDrawables = drawableArr;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public DrawableContainerState cloneConstantState() {
        throw null;
    }

    DrawableContainer() {
    }

    public void draw(Canvas canvas) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mDrawableContainerState.getChangingConfigurations();
    }

    @SuppressLint({"WrongConstant"})
    @TargetApi(23)
    private boolean needsMirroring() {
        return isAutoMirrored() && getLayoutDirection() == 1;
    }

    public boolean getPadding(Rect rect) {
        boolean z;
        Rect constantPadding = this.mDrawableContainerState.getConstantPadding();
        if (constantPadding != null) {
            rect.set(constantPadding);
            z = (constantPadding.right | ((constantPadding.left | constantPadding.top) | constantPadding.bottom)) != 0;
        } else {
            Drawable drawable = this.mCurrDrawable;
            z = drawable != null ? drawable.getPadding(rect) : super.getPadding(rect);
        }
        if (needsMirroring()) {
            int i = rect.left;
            rect.left = rect.right;
            rect.right = i;
        }
        return z;
    }

    public void getOutline(Outline outline) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            drawable.getOutline(outline);
        }
    }

    public void setAlpha(int i) {
        if (!this.mHasAlpha || this.mAlpha != i) {
            this.mHasAlpha = true;
            this.mAlpha = i;
            Drawable drawable = this.mCurrDrawable;
            if (drawable == null) {
                return;
            }
            if (this.mEnterAnimationEnd == 0) {
                drawable.setAlpha(i);
            } else {
                animate(false);
            }
        }
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public void setDither(boolean z) {
        DrawableContainerState drawableContainerState = this.mDrawableContainerState;
        if (drawableContainerState.mDither != z) {
            drawableContainerState.mDither = z;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                drawable.setDither(drawableContainerState.mDither);
            }
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        DrawableContainerState drawableContainerState = this.mDrawableContainerState;
        drawableContainerState.mHasColorFilter = true;
        if (drawableContainerState.mColorFilter != colorFilter) {
            drawableContainerState.mColorFilter = colorFilter;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }
    }

    public void setTintList(ColorStateList colorStateList) {
        DrawableContainerState drawableContainerState = this.mDrawableContainerState;
        drawableContainerState.mHasTintList = true;
        if (drawableContainerState.mTintList != colorStateList) {
            drawableContainerState.mTintList = colorStateList;
            DrawableCompat.setTintList(this.mCurrDrawable, colorStateList);
        }
    }

    public void setTintMode(Mode mode) {
        DrawableContainerState drawableContainerState = this.mDrawableContainerState;
        drawableContainerState.mHasTintMode = true;
        if (drawableContainerState.mTintMode != mode) {
            drawableContainerState.mTintMode = mode;
            DrawableCompat.setTintMode(this.mCurrDrawable, mode);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect rect) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.setBounds(rect);
        }
        drawable = this.mCurrDrawable;
        if (drawable != null) {
            drawable.setBounds(rect);
        }
    }

    public void setAutoMirrored(boolean z) {
        DrawableContainerState drawableContainerState = this.mDrawableContainerState;
        if (drawableContainerState.mAutoMirrored != z) {
            drawableContainerState.mAutoMirrored = z;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                DrawableCompat.setAutoMirrored(drawable, drawableContainerState.mAutoMirrored);
            }
        }
    }

    public boolean isAutoMirrored() {
        return this.mDrawableContainerState.mAutoMirrored;
    }

    public void jumpToCurrentState() {
        Object obj;
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
            this.mLastDrawable = null;
            this.mLastIndex = -1;
            obj = 1;
        } else {
            obj = null;
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
            if (this.mHasAlpha) {
                this.mCurrDrawable.setAlpha(this.mAlpha);
            }
        }
        if (this.mExitAnimationEnd != 0) {
            this.mExitAnimationEnd = 0;
            obj = 1;
        }
        if (this.mEnterAnimationEnd != 0) {
            this.mEnterAnimationEnd = 0;
            obj = 1;
        }
        if (obj != null) {
            invalidateSelf();
        }
    }

    public void setHotspot(float f, float f2) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            DrawableCompat.setHotspot(drawable, f, f2);
        }
    }

    public void setHotspotBounds(int i, int i2, int i3, int i4) {
        Rect rect = this.mHotspotBounds;
        if (rect == null) {
            this.mHotspotBounds = new Rect(i, i2, i3, i4);
        } else {
            rect.set(i, i2, i3, i4);
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            DrawableCompat.setHotspotBounds(drawable, i, i2, i3, i4);
        }
    }

    public void getHotspotBounds(Rect rect) {
        Rect rect2 = this.mHotspotBounds;
        if (rect2 != null) {
            rect.set(rect2);
        } else {
            super.getHotspotBounds(rect);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean onStateChange(int[] iArr) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            return drawable.setState(iArr);
        }
        drawable = this.mCurrDrawable;
        return drawable != null ? drawable.setState(iArr) : false;
    }

    /* Access modifiers changed, original: protected */
    public boolean onLevelChange(int i) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            return drawable.setLevel(i);
        }
        drawable = this.mCurrDrawable;
        return drawable != null ? drawable.setLevel(i) : false;
    }

    public boolean onLayoutDirectionChanged(int i) {
        return this.mDrawableContainerState.setLayoutDirection(i, getCurrentIndex());
    }

    public int getIntrinsicWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantWidth();
        }
        Drawable drawable = this.mCurrDrawable;
        return drawable != null ? drawable.getIntrinsicWidth() : -1;
    }

    public int getIntrinsicHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantHeight();
        }
        Drawable drawable = this.mCurrDrawable;
        return drawable != null ? drawable.getIntrinsicHeight() : -1;
    }

    public int getMinimumWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumWidth();
        }
        Drawable drawable = this.mCurrDrawable;
        return drawable != null ? drawable.getMinimumWidth() : 0;
    }

    public int getMinimumHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumHeight();
        }
        Drawable drawable = this.mCurrDrawable;
        return drawable != null ? drawable.getMinimumHeight() : 0;
    }

    public void invalidateDrawable(Drawable drawable) {
        DrawableContainerState drawableContainerState = this.mDrawableContainerState;
        if (drawableContainerState != null) {
            drawableContainerState.invalidateCache();
        }
        if (drawable == this.mCurrDrawable && getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        if (drawable == this.mCurrDrawable && getCallback() != null) {
            getCallback().scheduleDrawable(this, runnable, j);
        }
    }

    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        if (drawable == this.mCurrDrawable && getCallback() != null) {
            getCallback().unscheduleDrawable(this, runnable);
        }
    }

    public boolean setVisible(boolean z, boolean z2) {
        boolean visible = super.setVisible(z, z2);
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.setVisible(z, z2);
        }
        drawable = this.mCurrDrawable;
        if (drawable != null) {
            drawable.setVisible(z, z2);
        }
        return visible;
    }

    public int getOpacity() {
        Drawable drawable = this.mCurrDrawable;
        return (drawable == null || !drawable.isVisible()) ? -2 : this.mDrawableContainerState.getOpacity();
    }

    /* Access modifiers changed, original: 0000 */
    public int getCurrentIndex() {
        return this.mCurIndex;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0071  */
    public boolean selectDrawable(int r9) {
        /*
        r8 = this;
        r0 = r8.mCurIndex;
        r1 = 0;
        if (r9 != r0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r2 = android.os.SystemClock.uptimeMillis();
        r0 = r8.mDrawableContainerState;
        r0 = r0.mExitFadeDuration;
        r4 = -1;
        r5 = 0;
        r6 = 0;
        if (r0 <= 0) goto L_0x0035;
    L_0x0014:
        r0 = r8.mLastDrawable;
        if (r0 == 0) goto L_0x001b;
    L_0x0018:
        r0.setVisible(r1, r1);
    L_0x001b:
        r0 = r8.mCurrDrawable;
        if (r0 == 0) goto L_0x002e;
    L_0x001f:
        r8.mLastDrawable = r0;
        r0 = r8.mCurIndex;
        r8.mLastIndex = r0;
        r0 = r8.mDrawableContainerState;
        r0 = r0.mExitFadeDuration;
        r0 = (long) r0;
        r0 = r0 + r2;
        r8.mExitAnimationEnd = r0;
        goto L_0x003c;
    L_0x002e:
        r8.mLastDrawable = r5;
        r8.mLastIndex = r4;
        r8.mExitAnimationEnd = r6;
        goto L_0x003c;
    L_0x0035:
        r0 = r8.mCurrDrawable;
        if (r0 == 0) goto L_0x003c;
    L_0x0039:
        r0.setVisible(r1, r1);
    L_0x003c:
        if (r9 < 0) goto L_0x005c;
    L_0x003e:
        r0 = r8.mDrawableContainerState;
        r1 = r0.mNumChildren;
        if (r9 >= r1) goto L_0x005c;
    L_0x0044:
        r0 = r0.getChild(r9);
        r8.mCurrDrawable = r0;
        r8.mCurIndex = r9;
        if (r0 == 0) goto L_0x0060;
    L_0x004e:
        r9 = r8.mDrawableContainerState;
        r9 = r9.mEnterFadeDuration;
        if (r9 <= 0) goto L_0x0058;
    L_0x0054:
        r4 = (long) r9;
        r2 = r2 + r4;
        r8.mEnterAnimationEnd = r2;
    L_0x0058:
        r8.initializeDrawableForDisplay(r0);
        goto L_0x0060;
    L_0x005c:
        r8.mCurrDrawable = r5;
        r8.mCurIndex = r4;
    L_0x0060:
        r0 = r8.mEnterAnimationEnd;
        r9 = 1;
        r2 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r2 != 0) goto L_0x006d;
    L_0x0067:
        r0 = r8.mExitAnimationEnd;
        r2 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r2 == 0) goto L_0x007f;
    L_0x006d:
        r0 = r8.mAnimationRunnable;
        if (r0 != 0) goto L_0x0079;
    L_0x0071:
        r0 = new androidx.appcompat.graphics.drawable.DrawableContainer$1;
        r0.<init>();
        r8.mAnimationRunnable = r0;
        goto L_0x007c;
    L_0x0079:
        r8.unscheduleSelf(r0);
    L_0x007c:
        r8.animate(r9);
    L_0x007f:
        r8.invalidateSelf();
        return r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.graphics.drawable.DrawableContainer.selectDrawable(int):boolean");
    }

    private void initializeDrawableForDisplay(Drawable drawable) {
        if (this.mBlockInvalidateCallback == null) {
            this.mBlockInvalidateCallback = new BlockInvalidateCallback();
        }
        BlockInvalidateCallback blockInvalidateCallback = this.mBlockInvalidateCallback;
        blockInvalidateCallback.wrap(drawable.getCallback());
        drawable.setCallback(blockInvalidateCallback);
        try {
            if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha) {
                drawable.setAlpha(this.mAlpha);
            }
            if (this.mDrawableContainerState.mHasColorFilter) {
                drawable.setColorFilter(this.mDrawableContainerState.mColorFilter);
            } else {
                if (this.mDrawableContainerState.mHasTintList) {
                    DrawableCompat.setTintList(drawable, this.mDrawableContainerState.mTintList);
                }
                if (this.mDrawableContainerState.mHasTintMode) {
                    DrawableCompat.setTintMode(drawable, this.mDrawableContainerState.mTintMode);
                }
            }
            drawable.setVisible(isVisible(), true);
            drawable.setDither(this.mDrawableContainerState.mDither);
            drawable.setState(getState());
            drawable.setLevel(getLevel());
            drawable.setBounds(getBounds());
            if (VERSION.SDK_INT >= 23) {
                drawable.setLayoutDirection(getLayoutDirection());
            }
            if (VERSION.SDK_INT >= 19) {
                drawable.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
            }
            Rect rect = this.mHotspotBounds;
            if (VERSION.SDK_INT >= 21 && rect != null) {
                drawable.setHotspotBounds(rect.left, rect.top, rect.right, rect.bottom);
            }
            drawable.setCallback(this.mBlockInvalidateCallback.unwrap());
        } catch (Throwable th) {
            drawable.setCallback(this.mBlockInvalidateCallback.unwrap());
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A:{SYNTHETIC, RETURN, SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x006d A:{SKIP} */
    public void animate(boolean r14) {
        /*
        r13 = this;
        r0 = 1;
        r13.mHasAlpha = r0;
        r1 = android.os.SystemClock.uptimeMillis();
        r3 = r13.mCurrDrawable;
        r4 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r6 = 0;
        r7 = 0;
        if (r3 == 0) goto L_0x0038;
    L_0x0010:
        r9 = r13.mEnterAnimationEnd;
        r11 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1));
        if (r11 == 0) goto L_0x003a;
    L_0x0016:
        r11 = (r9 > r1 ? 1 : (r9 == r1 ? 0 : -1));
        if (r11 > 0) goto L_0x0022;
    L_0x001a:
        r9 = r13.mAlpha;
        r3.setAlpha(r9);
        r13.mEnterAnimationEnd = r7;
        goto L_0x003a;
    L_0x0022:
        r9 = r9 - r1;
        r9 = r9 * r4;
        r10 = (int) r9;
        r9 = r13.mDrawableContainerState;
        r9 = r9.mEnterFadeDuration;
        r10 = r10 / r9;
        r9 = 255 - r10;
        r10 = r13.mAlpha;
        r9 = r9 * r10;
        r9 = r9 / 255;
        r3.setAlpha(r9);
        r3 = 1;
        goto L_0x003b;
    L_0x0038:
        r13.mEnterAnimationEnd = r7;
    L_0x003a:
        r3 = 0;
    L_0x003b:
        r9 = r13.mLastDrawable;
        if (r9 == 0) goto L_0x0068;
    L_0x003f:
        r10 = r13.mExitAnimationEnd;
        r12 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1));
        if (r12 == 0) goto L_0x006a;
    L_0x0045:
        r12 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1));
        if (r12 > 0) goto L_0x0055;
    L_0x0049:
        r9.setVisible(r6, r6);
        r0 = 0;
        r13.mLastDrawable = r0;
        r0 = -1;
        r13.mLastIndex = r0;
        r13.mExitAnimationEnd = r7;
        goto L_0x006a;
    L_0x0055:
        r10 = r10 - r1;
        r10 = r10 * r4;
        r3 = (int) r10;
        r4 = r13.mDrawableContainerState;
        r4 = r4.mExitFadeDuration;
        r3 = r3 / r4;
        r4 = r13.mAlpha;
        r3 = r3 * r4;
        r3 = r3 / 255;
        r9.setAlpha(r3);
        goto L_0x006b;
    L_0x0068:
        r13.mExitAnimationEnd = r7;
    L_0x006a:
        r0 = r3;
    L_0x006b:
        if (r14 == 0) goto L_0x0077;
    L_0x006d:
        if (r0 == 0) goto L_0x0077;
    L_0x006f:
        r14 = r13.mAnimationRunnable;
        r3 = 16;
        r1 = r1 + r3;
        r13.scheduleSelf(r14, r1);
    L_0x0077:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.graphics.drawable.DrawableContainer.animate(boolean):void");
    }

    public Drawable getCurrent() {
        return this.mCurrDrawable;
    }

    /* Access modifiers changed, original: final */
    public final void updateDensity(Resources resources) {
        this.mDrawableContainerState.updateDensity(resources);
    }

    public void applyTheme(Theme theme) {
        this.mDrawableContainerState.applyTheme(theme);
    }

    public boolean canApplyTheme() {
        return this.mDrawableContainerState.canApplyTheme();
    }

    public final ConstantState getConstantState() {
        if (!this.mDrawableContainerState.canConstantState()) {
            return null;
        }
        this.mDrawableContainerState.mChangingConfigurations = getChangingConfigurations();
        return this.mDrawableContainerState;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            DrawableContainerState cloneConstantState = cloneConstantState();
            cloneConstantState.mutate();
            setConstantState(cloneConstantState);
            this.mMutated = true;
        }
        return this;
    }

    /* Access modifiers changed, original: protected */
    public void setConstantState(DrawableContainerState drawableContainerState) {
        this.mDrawableContainerState = drawableContainerState;
        int i = this.mCurIndex;
        if (i >= 0) {
            this.mCurrDrawable = drawableContainerState.getChild(i);
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                initializeDrawableForDisplay(drawable);
            }
        }
        this.mLastIndex = -1;
        this.mLastDrawable = null;
    }

    static int resolveDensity(Resources resources, int i) {
        if (resources != null) {
            i = resources.getDisplayMetrics().densityDpi;
        }
        return i == 0 ? 160 : i;
    }
}

package androidx.customview.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewParentCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import androidx.customview.widget.FocusStrategy.BoundsAdapter;
import androidx.customview.widget.FocusStrategy.CollectionAdapter;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.MessagesController;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
    private static final String DEFAULT_CLASS_NAME = "android.view.View";
    public static final int HOST_ID = -1;
    public static final int INVALID_ID = Integer.MIN_VALUE;
    private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private static final BoundsAdapter<AccessibilityNodeInfoCompat> NODE_ADAPTER = new C33021();
    private static final CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> SPARSE_VALUES_ADAPTER = new C33032();
    int mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
    private final View mHost;
    private int mHoveredVirtualViewId = Integer.MIN_VALUE;
    int mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
    private final AccessibilityManager mManager;
    private MyNodeProvider mNodeProvider;
    private final int[] mTempGlobalRect = new int[2];
    private final Rect mTempParentRect = new Rect();
    private final Rect mTempScreenRect = new Rect();
    private final Rect mTempVisibleRect = new Rect();

    /* renamed from: androidx.customview.widget.ExploreByTouchHelper$1 */
    static class C33021 implements BoundsAdapter<AccessibilityNodeInfoCompat> {
        C33021() {
        }

        public void obtainBounds(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, Rect rect) {
            accessibilityNodeInfoCompat.getBoundsInParent(rect);
        }
    }

    /* renamed from: androidx.customview.widget.ExploreByTouchHelper$2 */
    static class C33032 implements CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> {
        C33032() {
        }

        public AccessibilityNodeInfoCompat get(SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat, int i) {
            return (AccessibilityNodeInfoCompat) sparseArrayCompat.valueAt(i);
        }

        public int size(SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat) {
            return sparseArrayCompat.size();
        }
    }

    private class MyNodeProvider extends AccessibilityNodeProviderCompat {
        MyNodeProvider() {
        }

        public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int i) {
            return AccessibilityNodeInfoCompat.obtain(ExploreByTouchHelper.this.obtainAccessibilityNodeInfo(i));
        }

        public boolean performAction(int i, int i2, Bundle bundle) {
            return ExploreByTouchHelper.this.performAction(i, i2, bundle);
        }

        public AccessibilityNodeInfoCompat findFocus(int i) {
            i = i == 2 ? ExploreByTouchHelper.this.mAccessibilityFocusedVirtualViewId : ExploreByTouchHelper.this.mKeyboardFocusedVirtualViewId;
            if (i == Integer.MIN_VALUE) {
                return null;
            }
            return createAccessibilityNodeInfo(i);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:49:0x0151 in {4, 6, 15, 16, 19, 20, 22, 25, 32, 33, 34, 41, 42, 44, 46, 48} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private androidx.core.view.accessibility.AccessibilityNodeInfoCompat createNodeForChild(int r8) {
        /*
        r7 = this;
        r0 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.obtain();
        r1 = 1;
        r0.setEnabled(r1);
        r0.setFocusable(r1);
        r2 = "android.view.View";
        r0.setClassName(r2);
        r2 = INVALID_PARENT_BOUNDS;
        r0.setBoundsInParent(r2);
        r2 = INVALID_PARENT_BOUNDS;
        r0.setBoundsInScreen(r2);
        r2 = r7.mHost;
        r0.setParent(r2);
        r7.onPopulateNodeForVirtualView(r8, r0);
        r2 = r0.getText();
        if (r2 != 0) goto L_0x0037;
        r2 = r0.getContentDescription();
        if (r2 == 0) goto L_0x002f;
        goto L_0x0037;
        r8 = new java.lang.RuntimeException;
        r0 = "Callbacks must add text or a content description in populateNodeForVirtualViewId()";
        r8.<init>(r0);
        throw r8;
        r2 = r7.mTempParentRect;
        r0.getBoundsInParent(r2);
        r2 = r7.mTempParentRect;
        r3 = INVALID_PARENT_BOUNDS;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x0149;
        r2 = r0.getActions();
        r3 = r2 & 64;
        if (r3 != 0) goto L_0x0141;
        r3 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r2 = r2 & r3;
        if (r2 != 0) goto L_0x0139;
        r2 = r7.mHost;
        r2 = r2.getContext();
        r2 = r2.getPackageName();
        r0.setPackageName(r2);
        r2 = r7.mHost;
        r0.setSource(r2, r8);
        r2 = r7.mAccessibilityFocusedVirtualViewId;
        r4 = 0;
        if (r2 != r8) goto L_0x0071;
        r0.setAccessibilityFocused(r1);
        r0.addAction(r3);
        goto L_0x0079;
        r0.setAccessibilityFocused(r4);
        r2 = 64;
        r0.addAction(r2);
        r2 = r7.mKeyboardFocusedVirtualViewId;
        if (r2 != r8) goto L_0x007f;
        r8 = 1;
        goto L_0x0080;
        r8 = 0;
        if (r8 == 0) goto L_0x0087;
        r2 = 2;
        r0.addAction(r2);
        goto L_0x0090;
        r2 = r0.isFocusable();
        if (r2 == 0) goto L_0x0090;
        r0.addAction(r1);
        r0.setFocused(r8);
        r8 = r7.mHost;
        r2 = r7.mTempGlobalRect;
        r8.getLocationOnScreen(r2);
        r8 = r7.mTempScreenRect;
        r0.getBoundsInScreen(r8);
        r8 = r7.mTempScreenRect;
        r2 = INVALID_PARENT_BOUNDS;
        r8 = r8.equals(r2);
        if (r8 == 0) goto L_0x00f9;
        r8 = r7.mTempScreenRect;
        r0.getBoundsInParent(r8);
        r8 = r0.mParentVirtualDescendantId;
        r2 = -1;
        if (r8 == r2) goto L_0x00de;
        r8 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.obtain();
        r3 = r0.mParentVirtualDescendantId;
        if (r3 == r2) goto L_0x00db;
        r5 = r7.mHost;
        r8.setParent(r5, r2);
        r5 = INVALID_PARENT_BOUNDS;
        r8.setBoundsInParent(r5);
        r7.onPopulateNodeForVirtualView(r3, r8);
        r3 = r7.mTempParentRect;
        r8.getBoundsInParent(r3);
        r3 = r7.mTempScreenRect;
        r5 = r7.mTempParentRect;
        r6 = r5.left;
        r5 = r5.top;
        r3.offset(r6, r5);
        r3 = r8.mParentVirtualDescendantId;
        goto L_0x00b9;
        r8.recycle();
        r8 = r7.mTempScreenRect;
        r2 = r7.mTempGlobalRect;
        r2 = r2[r4];
        r3 = r7.mHost;
        r3 = r3.getScrollX();
        r2 = r2 - r3;
        r3 = r7.mTempGlobalRect;
        r3 = r3[r1];
        r5 = r7.mHost;
        r5 = r5.getScrollY();
        r3 = r3 - r5;
        r8.offset(r2, r3);
        r8 = r7.mHost;
        r2 = r7.mTempVisibleRect;
        r8 = r8.getLocalVisibleRect(r2);
        if (r8 == 0) goto L_0x0138;
        r8 = r7.mTempVisibleRect;
        r2 = r7.mTempGlobalRect;
        r2 = r2[r4];
        r3 = r7.mHost;
        r3 = r3.getScrollX();
        r2 = r2 - r3;
        r3 = r7.mTempGlobalRect;
        r3 = r3[r1];
        r4 = r7.mHost;
        r4 = r4.getScrollY();
        r3 = r3 - r4;
        r8.offset(r2, r3);
        r8 = r7.mTempScreenRect;
        r2 = r7.mTempVisibleRect;
        r8 = r8.intersect(r2);
        if (r8 == 0) goto L_0x0138;
        r8 = r7.mTempScreenRect;
        r0.setBoundsInScreen(r8);
        r8 = r7.mTempScreenRect;
        r8 = r7.isVisibleToUser(r8);
        if (r8 == 0) goto L_0x0138;
        r0.setVisibleToUser(r1);
        return r0;
        r8 = new java.lang.RuntimeException;
        r0 = "Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()";
        r8.<init>(r0);
        throw r8;
        r8 = new java.lang.RuntimeException;
        r0 = "Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()";
        r8.<init>(r0);
        throw r8;
        r8 = new java.lang.RuntimeException;
        r0 = "Callbacks must set parent bounds in populateNodeForVirtualViewId()";
        r8.<init>(r0);
        throw r8;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.customview.widget.ExploreByTouchHelper.createNodeForChild(int):androidx.core.view.accessibility.AccessibilityNodeInfoCompat");
    }

    private static int keyToDirection(int i) {
        return i != 19 ? i != 21 ? i != 22 ? 130 : 66 : 17 : 33;
    }

    public abstract int getVirtualViewAt(float f, float f2);

    public abstract void getVisibleVirtualViews(List<Integer> list);

    public abstract boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle);

    /* Access modifiers changed, original: protected */
    public void onPopulateEventForHost(AccessibilityEvent accessibilityEvent) {
    }

    /* Access modifiers changed, original: protected */
    public void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent) {
    }

    /* Access modifiers changed, original: protected */
    public void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
    }

    public abstract void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat);

    /* Access modifiers changed, original: protected */
    public void onVirtualViewKeyboardFocusChanged(int i, boolean z) {
    }

    public ExploreByTouchHelper(View view) {
        if (view != null) {
            this.mHost = view;
            this.mManager = (AccessibilityManager) view.getContext().getSystemService("accessibility");
            view.setFocusable(true);
            if (ViewCompat.getImportantForAccessibility(view) == 0) {
                ViewCompat.setImportantForAccessibility(view, 1);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("View may not be null");
    }

    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        if (this.mNodeProvider == null) {
            this.mNodeProvider = new MyNodeProvider();
        }
        return this.mNodeProvider;
    }

    public final boolean dispatchHoverEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (this.mManager.isEnabled() && this.mManager.isTouchExplorationEnabled()) {
            int action = motionEvent.getAction();
            if (action == 7 || action == 9) {
                int virtualViewAt = getVirtualViewAt(motionEvent.getX(), motionEvent.getY());
                updateHoveredVirtualView(virtualViewAt);
                if (virtualViewAt != Integer.MIN_VALUE) {
                    z = true;
                }
            } else if (action != 10 || this.mHoveredVirtualViewId == Integer.MIN_VALUE) {
                return false;
            } else {
                updateHoveredVirtualView(Integer.MIN_VALUE);
                return true;
            }
        }
        return z;
    }

    public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int i = 0;
        if (keyEvent.getAction() == 1) {
            return false;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode != 61) {
            if (keyCode != 66) {
                switch (keyCode) {
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                        if (!keyEvent.hasNoModifiers()) {
                            return false;
                        }
                        keyCode = keyToDirection(keyCode);
                        int repeatCount = keyEvent.getRepeatCount() + 1;
                        boolean z = false;
                        while (i < repeatCount && moveFocus(keyCode, null)) {
                            i++;
                            z = true;
                        }
                        return z;
                    case 23:
                        break;
                    default:
                        return false;
                }
            }
            if (!keyEvent.hasNoModifiers() || keyEvent.getRepeatCount() != 0) {
                return false;
            }
            clickKeyboardFocusedVirtualView();
            return true;
        } else if (keyEvent.hasNoModifiers()) {
            return moveFocus(2, null);
        } else {
            if (keyEvent.hasModifiers(1)) {
                return moveFocus(1, null);
            }
            return false;
        }
    }

    public final void onFocusChanged(boolean z, int i, Rect rect) {
        int i2 = this.mKeyboardFocusedVirtualViewId;
        if (i2 != Integer.MIN_VALUE) {
            clearKeyboardFocusForVirtualView(i2);
        }
        if (z) {
            moveFocus(i, rect);
        }
    }

    public final int getAccessibilityFocusedVirtualViewId() {
        return this.mAccessibilityFocusedVirtualViewId;
    }

    public final int getKeyboardFocusedVirtualViewId() {
        return this.mKeyboardFocusedVirtualViewId;
    }

    private void getBoundsInParent(int i, Rect rect) {
        obtainAccessibilityNodeInfo(i).getBoundsInParent(rect);
    }

    private boolean moveFocus(int i, Rect rect) {
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat;
        Object obj;
        SparseArrayCompat allNodes = getAllNodes();
        int i2 = this.mKeyboardFocusedVirtualViewId;
        int i3 = Integer.MIN_VALUE;
        if (i2 == Integer.MIN_VALUE) {
            accessibilityNodeInfoCompat = null;
        } else {
            accessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat) allNodes.get(i2);
        }
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = accessibilityNodeInfoCompat;
        if (i == 1 || i == 2) {
            obj = (AccessibilityNodeInfoCompat) FocusStrategy.findNextFocusInRelativeDirection(allNodes, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, accessibilityNodeInfoCompat2, i, ViewCompat.getLayoutDirection(this.mHost) == 1, false);
        } else if (i == 17 || i == 33 || i == 66 || i == 130) {
            Rect rect2 = new Rect();
            i2 = this.mKeyboardFocusedVirtualViewId;
            if (i2 != Integer.MIN_VALUE) {
                getBoundsInParent(i2, rect2);
            } else if (rect != null) {
                rect2.set(rect);
            } else {
                guessPreviouslyFocusedRect(this.mHost, i, rect2);
            }
            obj = (AccessibilityNodeInfoCompat) FocusStrategy.findNextFocusInAbsoluteDirection(allNodes, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, accessibilityNodeInfoCompat2, rect2, i);
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        if (obj != null) {
            i3 = allNodes.keyAt(allNodes.indexOfValue(obj));
        }
        return requestKeyboardFocusForVirtualView(i3);
    }

    private SparseArrayCompat<AccessibilityNodeInfoCompat> getAllNodes() {
        ArrayList arrayList = new ArrayList();
        getVisibleVirtualViews(arrayList);
        SparseArrayCompat sparseArrayCompat = new SparseArrayCompat();
        for (int i = 0; i < arrayList.size(); i++) {
            sparseArrayCompat.put(i, createNodeForChild(i));
        }
        return sparseArrayCompat;
    }

    private static Rect guessPreviouslyFocusedRect(View view, int i, Rect rect) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (i == 17) {
            rect.set(width, 0, width, height);
        } else if (i == 33) {
            rect.set(0, height, width, height);
        } else if (i == 66) {
            rect.set(-1, 0, -1, height);
        } else if (i == 130) {
            rect.set(0, -1, width, -1);
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        return rect;
    }

    private boolean clickKeyboardFocusedVirtualView() {
        int i = this.mKeyboardFocusedVirtualViewId;
        return i != Integer.MIN_VALUE && onPerformActionForVirtualView(i, 16, null);
    }

    public final boolean sendEventForVirtualView(int i, int i2) {
        if (i == Integer.MIN_VALUE || !this.mManager.isEnabled()) {
            return false;
        }
        ViewParent parent = this.mHost.getParent();
        if (parent == null) {
            return false;
        }
        return ViewParentCompat.requestSendAccessibilityEvent(parent, this.mHost, createEvent(i, i2));
    }

    public final void invalidateRoot() {
        invalidateVirtualView(-1, 1);
    }

    public final void invalidateVirtualView(int i) {
        invalidateVirtualView(i, 0);
    }

    public final void invalidateVirtualView(int i, int i2) {
        if (i != Integer.MIN_VALUE && this.mManager.isEnabled()) {
            ViewParent parent = this.mHost.getParent();
            if (parent != null) {
                AccessibilityEvent createEvent = createEvent(i, 2048);
                AccessibilityEventCompat.setContentChangeTypes(createEvent, i2);
                ViewParentCompat.requestSendAccessibilityEvent(parent, this.mHost, createEvent);
            }
        }
    }

    @Deprecated
    public int getFocusedVirtualView() {
        return getAccessibilityFocusedVirtualViewId();
    }

    private void updateHoveredVirtualView(int i) {
        int i2 = this.mHoveredVirtualViewId;
        if (i2 != i) {
            this.mHoveredVirtualViewId = i;
            sendEventForVirtualView(i, 128);
            sendEventForVirtualView(i2, 256);
        }
    }

    private AccessibilityEvent createEvent(int i, int i2) {
        if (i != -1) {
            return createEventForChild(i, i2);
        }
        return createEventForHost(i2);
    }

    private AccessibilityEvent createEventForHost(int i) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
        this.mHost.onInitializeAccessibilityEvent(obtain);
        return obtain;
    }

    public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(view, accessibilityEvent);
        onPopulateEventForHost(accessibilityEvent);
    }

    private AccessibilityEvent createEventForChild(int i, int i2) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i2);
        AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo = obtainAccessibilityNodeInfo(i);
        obtain.getText().add(obtainAccessibilityNodeInfo.getText());
        obtain.setContentDescription(obtainAccessibilityNodeInfo.getContentDescription());
        obtain.setScrollable(obtainAccessibilityNodeInfo.isScrollable());
        obtain.setPassword(obtainAccessibilityNodeInfo.isPassword());
        obtain.setEnabled(obtainAccessibilityNodeInfo.isEnabled());
        obtain.setChecked(obtainAccessibilityNodeInfo.isChecked());
        onPopulateEventForVirtualView(i, obtain);
        if (obtain.getText().isEmpty() && obtain.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
        }
        obtain.setClassName(obtainAccessibilityNodeInfo.getClassName());
        AccessibilityRecordCompat.setSource(obtain, this.mHost, i);
        obtain.setPackageName(this.mHost.getContext().getPackageName());
        return obtain;
    }

    /* Access modifiers changed, original: 0000 */
    public AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int i) {
        if (i == -1) {
            return createNodeForHost();
        }
        return createNodeForChild(i);
    }

    private AccessibilityNodeInfoCompat createNodeForHost() {
        AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain(this.mHost);
        ViewCompat.onInitializeAccessibilityNodeInfo(this.mHost, obtain);
        ArrayList arrayList = new ArrayList();
        getVisibleVirtualViews(arrayList);
        if (obtain.getChildCount() <= 0 || arrayList.size() <= 0) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                obtain.addChild(this.mHost, ((Integer) arrayList.get(i)).intValue());
            }
            return obtain;
        }
        throw new RuntimeException("Views cannot have both real and virtual children");
    }

    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        onPopulateNodeForHost(accessibilityNodeInfoCompat);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean performAction(int i, int i2, Bundle bundle) {
        if (i != -1) {
            return performActionForChild(i, i2, bundle);
        }
        return performActionForHost(i2, bundle);
    }

    private boolean performActionForHost(int i, Bundle bundle) {
        return ViewCompat.performAccessibilityAction(this.mHost, i, bundle);
    }

    private boolean performActionForChild(int i, int i2, Bundle bundle) {
        if (i2 == 1) {
            return requestKeyboardFocusForVirtualView(i);
        }
        if (i2 == 2) {
            return clearKeyboardFocusForVirtualView(i);
        }
        if (i2 == 64) {
            return requestAccessibilityFocus(i);
        }
        if (i2 != 128) {
            return onPerformActionForVirtualView(i, i2, bundle);
        }
        return clearAccessibilityFocus(i);
    }

    private boolean isVisibleToUser(Rect rect) {
        boolean z = false;
        if (!(rect == null || rect.isEmpty())) {
            if (this.mHost.getWindowVisibility() != 0) {
                return false;
            }
            ViewParent parent = this.mHost.getParent();
            while (parent instanceof View) {
                View view = (View) parent;
                if (view.getAlpha() <= 0.0f || view.getVisibility() != 0) {
                    return false;
                }
                parent = view.getParent();
            }
            if (parent != null) {
                z = true;
            }
        }
        return z;
    }

    private boolean requestAccessibilityFocus(int i) {
        if (this.mManager.isEnabled() && this.mManager.isTouchExplorationEnabled()) {
            int i2 = this.mAccessibilityFocusedVirtualViewId;
            if (i2 != i) {
                if (i2 != Integer.MIN_VALUE) {
                    clearAccessibilityFocus(i2);
                }
                this.mAccessibilityFocusedVirtualViewId = i;
                this.mHost.invalidate();
                sendEventForVirtualView(i, 32768);
                return true;
            }
        }
        return false;
    }

    private boolean clearAccessibilityFocus(int i) {
        if (this.mAccessibilityFocusedVirtualViewId != i) {
            return false;
        }
        this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
        this.mHost.invalidate();
        sendEventForVirtualView(i, MessagesController.UPDATE_MASK_CHECK);
        return true;
    }

    public final boolean requestKeyboardFocusForVirtualView(int i) {
        if (!this.mHost.isFocused() && !this.mHost.requestFocus()) {
            return false;
        }
        int i2 = this.mKeyboardFocusedVirtualViewId;
        if (i2 == i) {
            return false;
        }
        if (i2 != Integer.MIN_VALUE) {
            clearKeyboardFocusForVirtualView(i2);
        }
        this.mKeyboardFocusedVirtualViewId = i;
        onVirtualViewKeyboardFocusChanged(i, true);
        sendEventForVirtualView(i, 8);
        return true;
    }

    public final boolean clearKeyboardFocusForVirtualView(int i) {
        if (this.mKeyboardFocusedVirtualViewId != i) {
            return false;
        }
        this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
        onVirtualViewKeyboardFocusChanged(i, false);
        sendEventForVirtualView(i, 8);
        return true;
    }
}

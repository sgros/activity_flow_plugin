// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityNodeInfo$CollectionItemInfo;
import android.view.accessibility.AccessibilityNodeInfo$CollectionInfo;
import android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction;
import android.os.Build$VERSION;
import android.graphics.Rect;
import android.view.View;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;

public class AccessibilityNodeInfoCompat
{
    private final AccessibilityNodeInfo mInfo;
    public int mParentVirtualDescendantId;
    
    private AccessibilityNodeInfoCompat(final AccessibilityNodeInfo mInfo) {
        this.mParentVirtualDescendantId = -1;
        this.mInfo = mInfo;
    }
    
    private static String getActionSymbolicName(final int n) {
        switch (n) {
            default: {
                return "ACTION_UNKNOWN";
            }
            case 131072: {
                return "ACTION_SET_SELECTION";
            }
            case 65536: {
                return "ACTION_CUT";
            }
            case 32768: {
                return "ACTION_PASTE";
            }
            case 16384: {
                return "ACTION_COPY";
            }
            case 8192: {
                return "ACTION_SCROLL_BACKWARD";
            }
            case 4096: {
                return "ACTION_SCROLL_FORWARD";
            }
            case 2048: {
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            }
            case 1024: {
                return "ACTION_NEXT_HTML_ELEMENT";
            }
            case 512: {
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            }
            case 256: {
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            }
            case 128: {
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            }
            case 64: {
                return "ACTION_ACCESSIBILITY_FOCUS";
            }
            case 32: {
                return "ACTION_LONG_CLICK";
            }
            case 16: {
                return "ACTION_CLICK";
            }
            case 8: {
                return "ACTION_CLEAR_SELECTION";
            }
            case 4: {
                return "ACTION_SELECT";
            }
            case 2: {
                return "ACTION_CLEAR_FOCUS";
            }
            case 1: {
                return "ACTION_FOCUS";
            }
        }
    }
    
    public static AccessibilityNodeInfoCompat obtain(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        return wrap(AccessibilityNodeInfo.obtain(accessibilityNodeInfoCompat.mInfo));
    }
    
    private void setBooleanProperty(final int n, final boolean b) {
        final Bundle extras = this.getExtras();
        if (extras != null) {
            final int int1 = extras.getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", 0);
            int n2;
            if (b) {
                n2 = n;
            }
            else {
                n2 = 0;
            }
            extras.putInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", n2 | (int1 & n));
        }
    }
    
    public static AccessibilityNodeInfoCompat wrap(final AccessibilityNodeInfo accessibilityNodeInfo) {
        return new AccessibilityNodeInfoCompat(accessibilityNodeInfo);
    }
    
    public void addAction(final int n) {
        this.mInfo.addAction(n);
    }
    
    public void addChild(final View view) {
        this.mInfo.addChild(view);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat)o;
        if (this.mInfo == null) {
            if (accessibilityNodeInfoCompat.mInfo != null) {
                return false;
            }
        }
        else if (!this.mInfo.equals((Object)accessibilityNodeInfoCompat.mInfo)) {
            return false;
        }
        return true;
    }
    
    public int getActions() {
        return this.mInfo.getActions();
    }
    
    public void getBoundsInParent(final Rect rect) {
        this.mInfo.getBoundsInParent(rect);
    }
    
    public void getBoundsInScreen(final Rect rect) {
        this.mInfo.getBoundsInScreen(rect);
    }
    
    public CharSequence getClassName() {
        return this.mInfo.getClassName();
    }
    
    public CharSequence getContentDescription() {
        return this.mInfo.getContentDescription();
    }
    
    public Bundle getExtras() {
        if (Build$VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras();
        }
        return new Bundle();
    }
    
    public CharSequence getPackageName() {
        return this.mInfo.getPackageName();
    }
    
    public CharSequence getText() {
        return this.mInfo.getText();
    }
    
    public String getViewIdResourceName() {
        if (Build$VERSION.SDK_INT >= 18) {
            return this.mInfo.getViewIdResourceName();
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        int hashCode;
        if (this.mInfo == null) {
            hashCode = 0;
        }
        else {
            hashCode = this.mInfo.hashCode();
        }
        return hashCode;
    }
    
    public boolean isAccessibilityFocused() {
        return Build$VERSION.SDK_INT >= 16 && this.mInfo.isAccessibilityFocused();
    }
    
    public boolean isCheckable() {
        return this.mInfo.isCheckable();
    }
    
    public boolean isChecked() {
        return this.mInfo.isChecked();
    }
    
    public boolean isClickable() {
        return this.mInfo.isClickable();
    }
    
    public boolean isEnabled() {
        return this.mInfo.isEnabled();
    }
    
    public boolean isFocusable() {
        return this.mInfo.isFocusable();
    }
    
    public boolean isFocused() {
        return this.mInfo.isFocused();
    }
    
    public boolean isLongClickable() {
        return this.mInfo.isLongClickable();
    }
    
    public boolean isPassword() {
        return this.mInfo.isPassword();
    }
    
    public boolean isScrollable() {
        return this.mInfo.isScrollable();
    }
    
    public boolean isSelected() {
        return this.mInfo.isSelected();
    }
    
    public boolean isVisibleToUser() {
        return Build$VERSION.SDK_INT >= 16 && this.mInfo.isVisibleToUser();
    }
    
    public void recycle() {
        this.mInfo.recycle();
    }
    
    public boolean removeAction(final AccessibilityActionCompat accessibilityActionCompat) {
        return Build$VERSION.SDK_INT >= 21 && this.mInfo.removeAction((AccessibilityNodeInfo$AccessibilityAction)accessibilityActionCompat.mAction);
    }
    
    public void setAccessibilityFocused(final boolean accessibilityFocused) {
        if (Build$VERSION.SDK_INT >= 16) {
            this.mInfo.setAccessibilityFocused(accessibilityFocused);
        }
    }
    
    public void setBoundsInParent(final Rect boundsInParent) {
        this.mInfo.setBoundsInParent(boundsInParent);
    }
    
    public void setBoundsInScreen(final Rect boundsInScreen) {
        this.mInfo.setBoundsInScreen(boundsInScreen);
    }
    
    public void setCheckable(final boolean checkable) {
        this.mInfo.setCheckable(checkable);
    }
    
    public void setChecked(final boolean checked) {
        this.mInfo.setChecked(checked);
    }
    
    public void setClassName(final CharSequence className) {
        this.mInfo.setClassName(className);
    }
    
    public void setClickable(final boolean clickable) {
        this.mInfo.setClickable(clickable);
    }
    
    public void setCollectionInfo(final Object o) {
        if (Build$VERSION.SDK_INT >= 19) {
            final AccessibilityNodeInfo mInfo = this.mInfo;
            AccessibilityNodeInfo$CollectionInfo collectionInfo;
            if (o == null) {
                collectionInfo = null;
            }
            else {
                collectionInfo = (AccessibilityNodeInfo$CollectionInfo)((CollectionInfoCompat)o).mInfo;
            }
            mInfo.setCollectionInfo(collectionInfo);
        }
    }
    
    public void setCollectionItemInfo(final Object o) {
        if (Build$VERSION.SDK_INT >= 19) {
            final AccessibilityNodeInfo mInfo = this.mInfo;
            AccessibilityNodeInfo$CollectionItemInfo collectionItemInfo;
            if (o == null) {
                collectionItemInfo = null;
            }
            else {
                collectionItemInfo = (AccessibilityNodeInfo$CollectionItemInfo)((CollectionItemInfoCompat)o).mInfo;
            }
            mInfo.setCollectionItemInfo(collectionItemInfo);
        }
    }
    
    public void setContentDescription(final CharSequence contentDescription) {
        this.mInfo.setContentDescription(contentDescription);
    }
    
    public void setContentInvalid(final boolean contentInvalid) {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.setContentInvalid(contentInvalid);
        }
    }
    
    public void setDismissable(final boolean dismissable) {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.setDismissable(dismissable);
        }
    }
    
    public void setEnabled(final boolean enabled) {
        this.mInfo.setEnabled(enabled);
    }
    
    public void setError(final CharSequence error) {
        if (Build$VERSION.SDK_INT >= 21) {
            this.mInfo.setError(error);
        }
    }
    
    public void setFocusable(final boolean focusable) {
        this.mInfo.setFocusable(focusable);
    }
    
    public void setFocused(final boolean focused) {
        this.mInfo.setFocused(focused);
    }
    
    public void setHintText(final CharSequence hintText) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mInfo.setHintText(hintText);
        }
        else if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.HINT_TEXT_KEY", hintText);
        }
    }
    
    public void setLongClickable(final boolean longClickable) {
        this.mInfo.setLongClickable(longClickable);
    }
    
    public void setPackageName(final CharSequence packageName) {
        this.mInfo.setPackageName(packageName);
    }
    
    public void setParent(final View parent) {
        this.mInfo.setParent(parent);
    }
    
    public void setScrollable(final boolean scrollable) {
        this.mInfo.setScrollable(scrollable);
    }
    
    public void setSelected(final boolean selected) {
        this.mInfo.setSelected(selected);
    }
    
    public void setShowingHintText(final boolean showingHintText) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mInfo.setShowingHintText(showingHintText);
        }
        else {
            this.setBooleanProperty(4, showingHintText);
        }
    }
    
    public void setSource(final View source) {
        this.mInfo.setSource(source);
    }
    
    public void setText(final CharSequence text) {
        this.mInfo.setText(text);
    }
    
    public void setVisibleToUser(final boolean visibleToUser) {
        if (Build$VERSION.SDK_INT >= 16) {
            this.mInfo.setVisibleToUser(visibleToUser);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        final Rect rect = new Rect();
        this.getBoundsInParent(rect);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("; boundsInParent: ");
        sb2.append(rect);
        sb.append(sb2.toString());
        this.getBoundsInScreen(rect);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("; boundsInScreen: ");
        sb3.append(rect);
        sb.append(sb3.toString());
        sb.append("; packageName: ");
        sb.append(this.getPackageName());
        sb.append("; className: ");
        sb.append(this.getClassName());
        sb.append("; text: ");
        sb.append(this.getText());
        sb.append("; contentDescription: ");
        sb.append(this.getContentDescription());
        sb.append("; viewId: ");
        sb.append(this.getViewIdResourceName());
        sb.append("; checkable: ");
        sb.append(this.isCheckable());
        sb.append("; checked: ");
        sb.append(this.isChecked());
        sb.append("; focusable: ");
        sb.append(this.isFocusable());
        sb.append("; focused: ");
        sb.append(this.isFocused());
        sb.append("; selected: ");
        sb.append(this.isSelected());
        sb.append("; clickable: ");
        sb.append(this.isClickable());
        sb.append("; longClickable: ");
        sb.append(this.isLongClickable());
        sb.append("; enabled: ");
        sb.append(this.isEnabled());
        sb.append("; password: ");
        sb.append(this.isPassword());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("; scrollable: ");
        sb4.append(this.isScrollable());
        sb.append(sb4.toString());
        sb.append("; [");
        int n2;
        for (int i = this.getActions(); i != 0; i = n2) {
            final int n = 1 << Integer.numberOfTrailingZeros(i);
            n2 = (i & n);
            sb.append(getActionSymbolicName(n));
            if ((i = n2) != 0) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public AccessibilityNodeInfo unwrap() {
        return this.mInfo;
    }
    
    public static class AccessibilityActionCompat
    {
        public static final AccessibilityActionCompat ACTION_ACCESSIBILITY_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_SELECTION;
        public static final AccessibilityActionCompat ACTION_CLICK;
        public static final AccessibilityActionCompat ACTION_COLLAPSE;
        public static final AccessibilityActionCompat ACTION_CONTEXT_CLICK;
        public static final AccessibilityActionCompat ACTION_COPY;
        public static final AccessibilityActionCompat ACTION_CUT;
        public static final AccessibilityActionCompat ACTION_DISMISS;
        public static final AccessibilityActionCompat ACTION_EXPAND;
        public static final AccessibilityActionCompat ACTION_FOCUS;
        public static final AccessibilityActionCompat ACTION_HIDE_TOOLTIP;
        public static final AccessibilityActionCompat ACTION_LONG_CLICK;
        public static final AccessibilityActionCompat ACTION_MOVE_WINDOW;
        public static final AccessibilityActionCompat ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
        public static final AccessibilityActionCompat ACTION_NEXT_HTML_ELEMENT;
        public static final AccessibilityActionCompat ACTION_PASTE;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_HTML_ELEMENT;
        public static final AccessibilityActionCompat ACTION_SCROLL_BACKWARD;
        public static final AccessibilityActionCompat ACTION_SCROLL_DOWN;
        public static final AccessibilityActionCompat ACTION_SCROLL_FORWARD;
        public static final AccessibilityActionCompat ACTION_SCROLL_LEFT;
        public static final AccessibilityActionCompat ACTION_SCROLL_RIGHT;
        public static final AccessibilityActionCompat ACTION_SCROLL_TO_POSITION;
        public static final AccessibilityActionCompat ACTION_SCROLL_UP;
        public static final AccessibilityActionCompat ACTION_SELECT;
        public static final AccessibilityActionCompat ACTION_SET_PROGRESS;
        public static final AccessibilityActionCompat ACTION_SET_SELECTION;
        public static final AccessibilityActionCompat ACTION_SET_TEXT;
        public static final AccessibilityActionCompat ACTION_SHOW_ON_SCREEN;
        public static final AccessibilityActionCompat ACTION_SHOW_TOOLTIP;
        final Object mAction;
        
        static {
            final Object o = null;
            ACTION_FOCUS = new AccessibilityActionCompat(1, null);
            ACTION_CLEAR_FOCUS = new AccessibilityActionCompat(2, null);
            ACTION_SELECT = new AccessibilityActionCompat(4, null);
            ACTION_CLEAR_SELECTION = new AccessibilityActionCompat(8, null);
            ACTION_CLICK = new AccessibilityActionCompat(16, null);
            ACTION_LONG_CLICK = new AccessibilityActionCompat(32, null);
            ACTION_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(64, null);
            ACTION_CLEAR_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(128, null);
            ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(256, null);
            ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(512, null);
            ACTION_NEXT_HTML_ELEMENT = new AccessibilityActionCompat(1024, null);
            ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityActionCompat(2048, null);
            ACTION_SCROLL_FORWARD = new AccessibilityActionCompat(4096, null);
            ACTION_SCROLL_BACKWARD = new AccessibilityActionCompat(8192, null);
            ACTION_COPY = new AccessibilityActionCompat(16384, null);
            ACTION_PASTE = new AccessibilityActionCompat(32768, null);
            ACTION_CUT = new AccessibilityActionCompat(65536, null);
            ACTION_SET_SELECTION = new AccessibilityActionCompat(131072, null);
            ACTION_EXPAND = new AccessibilityActionCompat(262144, null);
            ACTION_COLLAPSE = new AccessibilityActionCompat(524288, null);
            ACTION_DISMISS = new AccessibilityActionCompat(1048576, null);
            ACTION_SET_TEXT = new AccessibilityActionCompat(2097152, null);
            AccessibilityNodeInfo$AccessibilityAction action_SHOW_ON_SCREEN;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SHOW_ON_SCREEN = AccessibilityNodeInfo$AccessibilityAction.ACTION_SHOW_ON_SCREEN;
            }
            else {
                action_SHOW_ON_SCREEN = null;
            }
            ACTION_SHOW_ON_SCREEN = new AccessibilityActionCompat(action_SHOW_ON_SCREEN);
            AccessibilityNodeInfo$AccessibilityAction action_SCROLL_TO_POSITION;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SCROLL_TO_POSITION = AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_TO_POSITION;
            }
            else {
                action_SCROLL_TO_POSITION = null;
            }
            ACTION_SCROLL_TO_POSITION = new AccessibilityActionCompat(action_SCROLL_TO_POSITION);
            AccessibilityNodeInfo$AccessibilityAction action_SCROLL_UP;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SCROLL_UP = AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_UP;
            }
            else {
                action_SCROLL_UP = null;
            }
            ACTION_SCROLL_UP = new AccessibilityActionCompat(action_SCROLL_UP);
            AccessibilityNodeInfo$AccessibilityAction action_SCROLL_LEFT;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SCROLL_LEFT = AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_LEFT;
            }
            else {
                action_SCROLL_LEFT = null;
            }
            ACTION_SCROLL_LEFT = new AccessibilityActionCompat(action_SCROLL_LEFT);
            AccessibilityNodeInfo$AccessibilityAction action_SCROLL_DOWN;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SCROLL_DOWN = AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_DOWN;
            }
            else {
                action_SCROLL_DOWN = null;
            }
            ACTION_SCROLL_DOWN = new AccessibilityActionCompat(action_SCROLL_DOWN);
            AccessibilityNodeInfo$AccessibilityAction action_SCROLL_RIGHT;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SCROLL_RIGHT = AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_RIGHT;
            }
            else {
                action_SCROLL_RIGHT = null;
            }
            ACTION_SCROLL_RIGHT = new AccessibilityActionCompat(action_SCROLL_RIGHT);
            AccessibilityNodeInfo$AccessibilityAction action_CONTEXT_CLICK;
            if (Build$VERSION.SDK_INT >= 23) {
                action_CONTEXT_CLICK = AccessibilityNodeInfo$AccessibilityAction.ACTION_CONTEXT_CLICK;
            }
            else {
                action_CONTEXT_CLICK = null;
            }
            ACTION_CONTEXT_CLICK = new AccessibilityActionCompat(action_CONTEXT_CLICK);
            AccessibilityNodeInfo$AccessibilityAction action_SET_PROGRESS;
            if (Build$VERSION.SDK_INT >= 24) {
                action_SET_PROGRESS = AccessibilityNodeInfo$AccessibilityAction.ACTION_SET_PROGRESS;
            }
            else {
                action_SET_PROGRESS = null;
            }
            ACTION_SET_PROGRESS = new AccessibilityActionCompat(action_SET_PROGRESS);
            AccessibilityNodeInfo$AccessibilityAction action_MOVE_WINDOW;
            if (Build$VERSION.SDK_INT >= 26) {
                action_MOVE_WINDOW = AccessibilityNodeInfo$AccessibilityAction.ACTION_MOVE_WINDOW;
            }
            else {
                action_MOVE_WINDOW = null;
            }
            ACTION_MOVE_WINDOW = new AccessibilityActionCompat(action_MOVE_WINDOW);
            AccessibilityNodeInfo$AccessibilityAction action_SHOW_TOOLTIP;
            if (Build$VERSION.SDK_INT >= 28) {
                action_SHOW_TOOLTIP = AccessibilityNodeInfo$AccessibilityAction.ACTION_SHOW_TOOLTIP;
            }
            else {
                action_SHOW_TOOLTIP = null;
            }
            ACTION_SHOW_TOOLTIP = new AccessibilityActionCompat(action_SHOW_TOOLTIP);
            Object action_HIDE_TOOLTIP = o;
            if (Build$VERSION.SDK_INT >= 28) {
                action_HIDE_TOOLTIP = AccessibilityNodeInfo$AccessibilityAction.ACTION_HIDE_TOOLTIP;
            }
            ACTION_HIDE_TOOLTIP = new AccessibilityActionCompat(action_HIDE_TOOLTIP);
        }
        
        public AccessibilityActionCompat(final int n, final CharSequence charSequence) {
            AccessibilityNodeInfo$AccessibilityAction accessibilityNodeInfo$AccessibilityAction;
            if (Build$VERSION.SDK_INT >= 21) {
                accessibilityNodeInfo$AccessibilityAction = new AccessibilityNodeInfo$AccessibilityAction(n, charSequence);
            }
            else {
                accessibilityNodeInfo$AccessibilityAction = null;
            }
            this(accessibilityNodeInfo$AccessibilityAction);
        }
        
        AccessibilityActionCompat(final Object mAction) {
            this.mAction = mAction;
        }
    }
    
    public static class CollectionInfoCompat
    {
        final Object mInfo;
        
        CollectionInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public static CollectionInfoCompat obtain(final int n, final int n2, final boolean b, final int n3) {
            if (Build$VERSION.SDK_INT >= 21) {
                return new CollectionInfoCompat(AccessibilityNodeInfo$CollectionInfo.obtain(n, n2, b, n3));
            }
            if (Build$VERSION.SDK_INT >= 19) {
                return new CollectionInfoCompat(AccessibilityNodeInfo$CollectionInfo.obtain(n, n2, b));
            }
            return new CollectionInfoCompat(null);
        }
    }
    
    public static class CollectionItemInfoCompat
    {
        final Object mInfo;
        
        CollectionItemInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public static CollectionItemInfoCompat obtain(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
            if (Build$VERSION.SDK_INT >= 21) {
                return new CollectionItemInfoCompat(AccessibilityNodeInfo$CollectionItemInfo.obtain(n, n2, n3, n4, b, b2));
            }
            if (Build$VERSION.SDK_INT >= 19) {
                return new CollectionItemInfoCompat(AccessibilityNodeInfo$CollectionItemInfo.obtain(n, n2, n3, n4, b));
            }
            return new CollectionItemInfoCompat(null);
        }
    }
}

package androidx.appcompat.app;

import android.graphics.drawable.Drawable;
import android.view.View;

@Deprecated
public abstract class ActionBar$Tab {
    public abstract CharSequence getContentDescription();

    public abstract View getCustomView();

    public abstract Drawable getIcon();

    public abstract CharSequence getText();

    public abstract void select();
}

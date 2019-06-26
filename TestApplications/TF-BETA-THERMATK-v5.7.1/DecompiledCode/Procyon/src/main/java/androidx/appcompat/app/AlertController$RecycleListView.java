// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.app;

import android.content.res.TypedArray;
import androidx.appcompat.R$styleable;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ListView;

public class AlertController$RecycleListView extends ListView
{
    private final int mPaddingBottomNoButtons;
    private final int mPaddingTopNoTitle;
    
    public AlertController$RecycleListView(final Context context) {
        this(context, null);
    }
    
    public AlertController$RecycleListView(final Context context, final AttributeSet set) {
        super(context, set);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R$styleable.RecycleListView);
        this.mPaddingBottomNoButtons = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.RecycleListView_paddingBottomNoButtons, -1);
        this.mPaddingTopNoTitle = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.RecycleListView_paddingTopNoTitle, -1);
    }
}

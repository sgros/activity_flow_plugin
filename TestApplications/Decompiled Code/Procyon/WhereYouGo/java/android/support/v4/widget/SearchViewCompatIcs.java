// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.widget.SearchView;
import android.view.View;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(14)
@RequiresApi(14)
class SearchViewCompatIcs
{
    public static View newSearchView(final Context context) {
        return (View)new MySearchView(context);
    }
    
    public static void setImeOptions(final View view, final int imeOptions) {
        ((SearchView)view).setImeOptions(imeOptions);
    }
    
    public static void setInputType(final View view, final int inputType) {
        ((SearchView)view).setInputType(inputType);
    }
    
    public static class MySearchView extends SearchView
    {
        public MySearchView(final Context context) {
            super(context);
        }
        
        public void onActionViewCollapsed() {
            this.setQuery((CharSequence)"", false);
            super.onActionViewCollapsed();
        }
    }
}
// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.urlinput;

import java.util.List;

public class UrlInputContract
{
    interface Presenter
    {
        void onInput(final CharSequence p0, final boolean p1);
        
        void setView(final View p0);
    }
    
    interface View
    {
        void setQuickSearchVisible(final boolean p0);
        
        void setSuggestions(final List<CharSequence> p0);
    }
}

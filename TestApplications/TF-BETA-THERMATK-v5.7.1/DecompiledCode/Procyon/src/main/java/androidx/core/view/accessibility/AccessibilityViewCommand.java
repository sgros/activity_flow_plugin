// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.view.accessibility;

import android.os.Bundle;
import android.view.View;

public interface AccessibilityViewCommand
{
    boolean perform(final View p0, final CommandArguments p1);
    
    public abstract static class CommandArguments
    {
        private static final Bundle sEmptyBundle;
        Bundle mBundle;
        
        static {
            sEmptyBundle = new Bundle();
        }
        
        public void setBundle(final Bundle mBundle) {
            this.mBundle = mBundle;
        }
    }
    
    public static final class MoveAtGranularityArguments extends CommandArguments
    {
    }
    
    public static final class MoveHtmlArguments extends CommandArguments
    {
    }
    
    public static final class MoveWindowArguments extends CommandArguments
    {
    }
    
    public static final class ScrollToPositionArguments extends CommandArguments
    {
    }
    
    public static final class SetProgressArguments extends CommandArguments
    {
    }
    
    public static final class SetSelectionArguments extends CommandArguments
    {
    }
    
    public static final class SetTextArguments extends CommandArguments
    {
    }
}

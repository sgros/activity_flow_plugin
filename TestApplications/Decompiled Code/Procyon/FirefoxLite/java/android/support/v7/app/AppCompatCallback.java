// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.app;

import android.support.v7.view.ActionMode;

public interface AppCompatCallback
{
    void onSupportActionModeFinished(final ActionMode p0);
    
    void onSupportActionModeStarted(final ActionMode p0);
    
    ActionMode onWindowStartingSupportActionMode(final ActionMode.Callback p0);
}

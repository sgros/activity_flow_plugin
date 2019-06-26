// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.os.Bundle;
import android.content.Context;
import android.widget.LinearLayout;

public class SlideView extends LinearLayout
{
    public SlideView(final Context context) {
        super(context);
    }
    
    public String getHeaderName() {
        return "";
    }
    
    public boolean needBackButton() {
        return false;
    }
    
    public boolean onBackPressed(final boolean b) {
        return true;
    }
    
    public void onCancelPressed() {
    }
    
    public void onDestroyActivity() {
    }
    
    public void onNextPressed() {
    }
    
    public void onShow() {
    }
    
    public void restoreStateParams(final Bundle bundle) {
    }
    
    public void saveStateParams(final Bundle bundle) {
    }
    
    public void setParams(final Bundle bundle, final boolean b) {
    }
}

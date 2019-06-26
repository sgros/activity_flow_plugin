// 
// Decompiled by Procyon v0.5.34
// 

package ar.com.daidalos.afiledialog;

import android.widget.LinearLayout;
import android.content.Context;

interface FileChooser
{
    Context getContext();
    
    LinearLayout getRootLayout();
    
    void setCurrentFolderName(final String p0);
}

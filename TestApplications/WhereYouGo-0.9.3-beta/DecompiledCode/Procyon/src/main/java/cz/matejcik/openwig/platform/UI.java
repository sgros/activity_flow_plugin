// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig.platform;

import cz.matejcik.openwig.EventTable;
import se.krka.kahlua.vm.LuaClosure;
import cz.matejcik.openwig.Media;

public interface UI
{
    public static final int DETAILSCREEN = 1;
    public static final int INVENTORYSCREEN = 2;
    public static final int ITEMSCREEN = 3;
    public static final int LOCATIONSCREEN = 4;
    public static final int MAINSCREEN = 0;
    public static final int TASKSCREEN = 5;
    
    void blockForSaving();
    
    void command(final String p0);
    
    void debugMsg(final String p0);
    
    void end();
    
    void playSound(final byte[] p0, final String p1);
    
    void pushDialog(final String[] p0, final Media[] p1, final String p2, final String p3, final LuaClosure p4);
    
    void pushInput(final EventTable p0);
    
    void refresh();
    
    void setStatusText(final String p0);
    
    void showError(final String p0);
    
    void showScreen(final int p0, final EventTable p1);
    
    void start();
    
    void unblock();
}

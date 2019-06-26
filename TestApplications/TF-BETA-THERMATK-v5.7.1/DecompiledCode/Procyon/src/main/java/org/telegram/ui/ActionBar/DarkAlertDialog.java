// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.content.Context;

public class DarkAlertDialog extends AlertDialog
{
    public DarkAlertDialog(final Context context, final int n) {
        super(context, n);
    }
    
    @Override
    protected int getThemeColor(final String s) {
        int n = 0;
        Label_0109: {
            switch (s.hashCode()) {
                case 1828201066: {
                    if (s.equals("dialogTextBlack")) {
                        n = 1;
                        break Label_0109;
                    }
                    break;
                }
                case -93324646: {
                    if (s.equals("dialogButton")) {
                        n = 2;
                        break Label_0109;
                    }
                    break;
                }
                case -451706526: {
                    if (s.equals("dialogScrollGlow")) {
                        n = 3;
                        break Label_0109;
                    }
                    break;
                }
                case -1849805674: {
                    if (s.equals("dialogBackground")) {
                        n = 0;
                        break Label_0109;
                    }
                    break;
                }
            }
            n = -1;
        }
        if (n == 0) {
            return -14277082;
        }
        if (n != 1 && n != 2 && n != 3) {
            return super.getThemeColor(s);
        }
        return -1;
    }
    
    public static class Builder extends AlertDialog.Builder
    {
        public Builder(final Context context) {
            super(new DarkAlertDialog(context, 0));
        }
        
        public Builder(final Context context, final int n) {
            super(new DarkAlertDialog(context, n));
        }
    }
}

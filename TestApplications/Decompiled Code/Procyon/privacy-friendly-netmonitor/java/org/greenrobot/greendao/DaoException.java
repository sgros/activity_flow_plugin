// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao;

import android.database.SQLException;

public class DaoException extends SQLException
{
    private static final long serialVersionUID = -5877937327907457779L;
    
    public DaoException() {
    }
    
    public DaoException(final String s) {
        super(s);
    }
    
    public DaoException(final String s, final Throwable t) {
        super(s);
        this.safeInitCause(t);
    }
    
    public DaoException(final Throwable t) {
        this.safeInitCause(t);
    }
    
    protected void safeInitCause(final Throwable t) {
        try {
            this.initCause(t);
        }
        catch (Throwable t2) {
            DaoLog.e("Could not set initial cause", t2);
            DaoLog.e("Initial cause is:", t);
        }
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.async;

import org.greenrobot.greendao.DaoException;

public class AsyncDaoException extends DaoException
{
    private static final long serialVersionUID = 5872157552005102382L;
    private final AsyncOperation failedOperation;
    
    public AsyncDaoException(final AsyncOperation failedOperation, final Throwable t) {
        super(t);
        this.failedOperation = failedOperation;
    }
    
    public AsyncOperation getFailedOperation() {
        return this.failedOperation;
    }
}

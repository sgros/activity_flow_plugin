// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDao;

public abstract class AbstractDaoTestStringPk<D extends AbstractDao<T, String>, T> extends AbstractDaoTestSinglePk<D, T, String>
{
    public AbstractDaoTestStringPk(final Class<D> clazz) {
        super(clazz);
    }
    
    @Override
    protected String createRandomPk() {
        final int nextInt = this.random.nextInt(30);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1 + nextInt; ++i) {
            sb.append((char)(97 + this.random.nextInt(25)));
        }
        return sb.toString();
    }
}

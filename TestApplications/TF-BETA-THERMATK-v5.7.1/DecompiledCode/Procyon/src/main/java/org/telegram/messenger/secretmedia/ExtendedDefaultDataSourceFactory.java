// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.secretmedia;

import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import android.content.Context;
import com.google.android.exoplayer2.upstream.DataSource;

public final class ExtendedDefaultDataSourceFactory implements Factory
{
    private final Factory baseDataSourceFactory;
    private final Context context;
    private final TransferListener listener;
    
    public ExtendedDefaultDataSourceFactory(final Context context, final TransferListener listener, final Factory baseDataSourceFactory) {
        this.context = context.getApplicationContext();
        this.listener = listener;
        this.baseDataSourceFactory = baseDataSourceFactory;
    }
    
    public ExtendedDefaultDataSourceFactory(final Context context, final String s) {
        this(context, s, null);
    }
    
    public ExtendedDefaultDataSourceFactory(final Context context, final String s, final TransferListener transferListener) {
        this(context, transferListener, new DefaultHttpDataSourceFactory(s, transferListener));
    }
    
    public ExtendedDefaultDataSource createDataSource() {
        return new ExtendedDefaultDataSource(this.context, this.listener, this.baseDataSourceFactory.createDataSource());
    }
}

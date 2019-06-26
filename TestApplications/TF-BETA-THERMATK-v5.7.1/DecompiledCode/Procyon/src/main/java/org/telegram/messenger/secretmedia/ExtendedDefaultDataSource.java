// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.secretmedia;

import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.upstream.DataSpec;
import android.net.Uri;
import java.util.Collections;
import java.util.Map;
import java.io.IOException;
import org.telegram.messenger.FileStreamLoadOperation;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.DataSchemeDataSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Predicate;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.util.List;
import android.content.Context;
import com.google.android.exoplayer2.upstream.DataSource;

public final class ExtendedDefaultDataSource implements DataSource
{
    private static final String SCHEME_ASSET = "asset";
    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_RAW = "rawresource";
    private static final String SCHEME_RTMP = "rtmp";
    private static final String TAG = "ExtendedDefaultDataSource";
    private DataSource assetDataSource;
    private final DataSource baseDataSource;
    private DataSource contentDataSource;
    private final Context context;
    private DataSource dataSchemeDataSource;
    private DataSource dataSource;
    private DataSource encryptedFileDataSource;
    private DataSource fileDataSource;
    private DataSource rawResourceDataSource;
    private DataSource rtmpDataSource;
    private final List<TransferListener> transferListeners;
    
    public ExtendedDefaultDataSource(final Context context, final DataSource dataSource) {
        this.context = context.getApplicationContext();
        Assertions.checkNotNull(dataSource);
        this.baseDataSource = dataSource;
        this.transferListeners = new ArrayList<TransferListener>();
    }
    
    @Deprecated
    public ExtendedDefaultDataSource(final Context context, final TransferListener transferListener, final DataSource dataSource) {
        this(context, dataSource);
        if (transferListener != null) {
            this.transferListeners.add(transferListener);
        }
    }
    
    @Deprecated
    public ExtendedDefaultDataSource(final Context context, final TransferListener transferListener, final String s, final int n, final int n2, final boolean b) {
        this(context, transferListener, new DefaultHttpDataSource(s, null, transferListener, n, n2, b, null));
    }
    
    @Deprecated
    public ExtendedDefaultDataSource(final Context context, final TransferListener transferListener, final String s, final boolean b) {
        this(context, transferListener, s, 8000, 8000, b);
    }
    
    public ExtendedDefaultDataSource(final Context context, final String s, final int n, final int n2, final boolean b) {
        this(context, new DefaultHttpDataSource(s, null, n, n2, b, null));
    }
    
    public ExtendedDefaultDataSource(final Context context, final String s, final boolean b) {
        this(context, s, 8000, 8000, b);
    }
    
    private void addListenersToDataSource(final DataSource dataSource) {
        for (int i = 0; i < this.transferListeners.size(); ++i) {
            dataSource.addTransferListener(this.transferListeners.get(i));
        }
    }
    
    private DataSource getAssetDataSource() {
        if (this.assetDataSource == null) {
            this.addListenersToDataSource(this.assetDataSource = new AssetDataSource(this.context));
        }
        return this.assetDataSource;
    }
    
    private DataSource getContentDataSource() {
        if (this.contentDataSource == null) {
            this.addListenersToDataSource(this.contentDataSource = new ContentDataSource(this.context));
        }
        return this.contentDataSource;
    }
    
    private DataSource getDataSchemeDataSource() {
        if (this.dataSchemeDataSource == null) {
            this.addListenersToDataSource(this.dataSchemeDataSource = new DataSchemeDataSource());
        }
        return this.dataSchemeDataSource;
    }
    
    private DataSource getEncryptedFileDataSource() {
        if (this.encryptedFileDataSource == null) {
            this.addListenersToDataSource(this.encryptedFileDataSource = new EncryptedFileDataSource());
        }
        return this.encryptedFileDataSource;
    }
    
    private DataSource getFileDataSource() {
        if (this.fileDataSource == null) {
            this.addListenersToDataSource(this.fileDataSource = new FileDataSource());
        }
        return this.fileDataSource;
    }
    
    private DataSource getRawResourceDataSource() {
        if (this.rawResourceDataSource == null) {
            this.addListenersToDataSource(this.rawResourceDataSource = new RawResourceDataSource(this.context));
        }
        return this.rawResourceDataSource;
    }
    
    private DataSource getRtmpDataSource() {
        if (this.rtmpDataSource == null) {
            try {
                this.addListenersToDataSource(this.rtmpDataSource = (DataSource)Class.forName("com.google.android.exoplayer2.ext.rtmp.RtmpDataSource").getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]));
            }
            catch (Exception cause) {
                throw new RuntimeException("Error instantiating RTMP extension", cause);
            }
            catch (ClassNotFoundException ex) {
                Log.w("ExtendedDefaultDataSource", "Attempting to play RTMP stream without depending on the RTMP extension");
            }
            if (this.rtmpDataSource == null) {
                this.rtmpDataSource = this.baseDataSource;
            }
        }
        return this.rtmpDataSource;
    }
    
    private DataSource getStreamDataSource() {
        final FileStreamLoadOperation fileStreamLoadOperation = new FileStreamLoadOperation();
        this.addListenersToDataSource(fileStreamLoadOperation);
        return fileStreamLoadOperation;
    }
    
    private void maybeAddListenerToDataSource(final DataSource dataSource, final TransferListener transferListener) {
        if (dataSource != null) {
            dataSource.addTransferListener(transferListener);
        }
    }
    
    @Override
    public void addTransferListener(final TransferListener transferListener) {
        this.baseDataSource.addTransferListener(transferListener);
        this.transferListeners.add(transferListener);
        this.maybeAddListenerToDataSource(this.fileDataSource, transferListener);
        this.maybeAddListenerToDataSource(this.assetDataSource, transferListener);
        this.maybeAddListenerToDataSource(this.contentDataSource, transferListener);
        this.maybeAddListenerToDataSource(this.rtmpDataSource, transferListener);
        this.maybeAddListenerToDataSource(this.dataSchemeDataSource, transferListener);
        this.maybeAddListenerToDataSource(this.rawResourceDataSource, transferListener);
    }
    
    @Override
    public void close() throws IOException {
        final DataSource dataSource = this.dataSource;
        if (dataSource != null) {
            try {
                dataSource.close();
            }
            finally {
                this.dataSource = null;
            }
        }
    }
    
    @Override
    public Map<String, List<String>> getResponseHeaders() {
        final DataSource dataSource = this.dataSource;
        Map<String, List<String>> map;
        if (dataSource == null) {
            map = Collections.emptyMap();
        }
        else {
            map = dataSource.getResponseHeaders();
        }
        return map;
    }
    
    @Override
    public Uri getUri() {
        final DataSource dataSource = this.dataSource;
        Uri uri;
        if (dataSource == null) {
            uri = null;
        }
        else {
            uri = dataSource.getUri();
        }
        return uri;
    }
    
    @Override
    public long open(final DataSpec dataSpec) throws IOException {
        Assertions.checkState(this.dataSource == null);
        final String scheme = dataSpec.uri.getScheme();
        if (Util.isLocalFileUri(dataSpec.uri)) {
            final String path = dataSpec.uri.getPath();
            if (path != null && path.startsWith("/android_asset/")) {
                this.dataSource = this.getAssetDataSource();
            }
            else if (dataSpec.uri.getPath().endsWith(".enc")) {
                this.dataSource = this.getEncryptedFileDataSource();
            }
            else {
                this.dataSource = this.getFileDataSource();
            }
        }
        else if ("tg".equals(scheme)) {
            this.dataSource = this.getStreamDataSource();
        }
        else if ("asset".equals(scheme)) {
            this.dataSource = this.getAssetDataSource();
        }
        else if ("content".equals(scheme)) {
            this.dataSource = this.getContentDataSource();
        }
        else if ("rtmp".equals(scheme)) {
            this.dataSource = this.getRtmpDataSource();
        }
        else if ("data".equals(scheme)) {
            this.dataSource = this.getDataSchemeDataSource();
        }
        else if ("rawresource".equals(scheme)) {
            this.dataSource = this.getRawResourceDataSource();
        }
        else {
            this.dataSource = this.baseDataSource;
        }
        return this.dataSource.open(dataSpec);
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        final DataSource dataSource = this.dataSource;
        Assertions.checkNotNull(dataSource);
        return dataSource.read(array, n, n2);
    }
}

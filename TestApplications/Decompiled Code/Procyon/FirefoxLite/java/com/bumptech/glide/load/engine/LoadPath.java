// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.util.Preconditions;
import android.support.v4.util.Pools;
import java.util.List;

public class LoadPath<Data, ResourceType, Transcode>
{
    private final Class<Data> dataClass;
    private final List<? extends DecodePath<Data, ResourceType, Transcode>> decodePaths;
    private final String failureMessage;
    private final Pools.Pool<List<Exception>> listPool;
    
    public LoadPath(final Class<Data> dataClass, final Class<ResourceType> clazz, final Class<Transcode> clazz2, final List<DecodePath<Data, ResourceType, Transcode>> list, final Pools.Pool<List<Exception>> listPool) {
        this.dataClass = dataClass;
        this.listPool = listPool;
        this.decodePaths = Preconditions.checkNotEmpty(list);
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed LoadPath{");
        sb.append(dataClass.getSimpleName());
        sb.append("->");
        sb.append(clazz.getSimpleName());
        sb.append("->");
        sb.append(clazz2.getSimpleName());
        sb.append("}");
        this.failureMessage = sb.toString();
    }
    
    private Resource<Transcode> loadWithExceptionList(final DataRewinder<Data> dataRewinder, final Options options, final int n, final int n2, final DecodePath.DecodeCallback<ResourceType> decodeCallback, final List<Exception> c) throws GlideException {
        final int size = this.decodePaths.size();
        int n3 = 0;
        Resource<Transcode> decode = null;
        Resource<Transcode> resource;
        while (true) {
            resource = decode;
            if (n3 >= size) {
                break;
            }
            final DecodePath decodePath = (DecodePath)this.decodePaths.get(n3);
            try {
                decode = decodePath.decode(dataRewinder, n, n2, options, (DecodePath.DecodeCallback<Object>)decodeCallback);
            }
            catch (GlideException ex) {
                c.add(ex);
            }
            if (decode != null) {
                resource = decode;
                break;
            }
            ++n3;
        }
        if (resource != null) {
            return resource;
        }
        throw new GlideException(this.failureMessage, new ArrayList<Exception>(c));
    }
    
    public Resource<Transcode> load(final DataRewinder<Data> dataRewinder, final Options options, final int n, final int n2, final DecodePath.DecodeCallback<ResourceType> decodeCallback) throws GlideException {
        final List<Exception> list = this.listPool.acquire();
        try {
            return this.loadWithExceptionList(dataRewinder, options, n, n2, decodeCallback, list);
        }
        finally {
            this.listPool.release(list);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LoadPath{decodePaths=");
        sb.append(Arrays.toString(this.decodePaths.toArray(new DecodePath[this.decodePaths.size()])));
        sb.append('}');
        return sb.toString();
    }
}

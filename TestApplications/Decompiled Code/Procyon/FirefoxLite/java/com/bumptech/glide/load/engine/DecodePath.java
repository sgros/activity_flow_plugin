// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import java.util.Collection;
import java.util.ArrayList;
import java.io.IOException;
import android.util.Log;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import android.support.v4.util.Pools;
import com.bumptech.glide.load.ResourceDecoder;
import java.util.List;

public class DecodePath<DataType, ResourceType, Transcode>
{
    private final Class<DataType> dataClass;
    private final List<? extends ResourceDecoder<DataType, ResourceType>> decoders;
    private final String failureMessage;
    private final Pools.Pool<List<Exception>> listPool;
    private final ResourceTranscoder<ResourceType, Transcode> transcoder;
    
    public DecodePath(final Class<DataType> dataClass, final Class<ResourceType> clazz, final Class<Transcode> clazz2, final List<? extends ResourceDecoder<DataType, ResourceType>> decoders, final ResourceTranscoder<ResourceType, Transcode> transcoder, final Pools.Pool<List<Exception>> listPool) {
        this.dataClass = dataClass;
        this.decoders = decoders;
        this.transcoder = transcoder;
        this.listPool = listPool;
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed DecodePath{");
        sb.append(dataClass.getSimpleName());
        sb.append("->");
        sb.append(clazz.getSimpleName());
        sb.append("->");
        sb.append(clazz2.getSimpleName());
        sb.append("}");
        this.failureMessage = sb.toString();
    }
    
    private Resource<ResourceType> decodeResource(final DataRewinder<DataType> dataRewinder, final int n, final int n2, final Options options) throws GlideException {
        final List<Exception> list = this.listPool.acquire();
        try {
            return this.decodeResourceWithList(dataRewinder, n, n2, options, list);
        }
        finally {
            this.listPool.release(list);
        }
    }
    
    private Resource<ResourceType> decodeResourceWithList(final DataRewinder<DataType> dataRewinder, final int n, final int n2, final Options options, final List<Exception> c) throws GlideException {
        final int size = this.decoders.size();
        Resource<ResourceType> resource = null;
        int n3 = 0;
        Resource<ResourceType> decode;
        while (true) {
            decode = resource;
            if (n3 >= size) {
                break;
            }
            final ResourceDecoder obj = (ResourceDecoder)this.decoders.get(n3);
            decode = resource;
            try {
                if (obj.handles(dataRewinder.rewindAndGet(), options)) {
                    decode = (Resource<ResourceType>)obj.decode(dataRewinder.rewindAndGet(), n, n2, options);
                }
            }
            catch (IOException | RuntimeException ex3) {
                final RuntimeException ex2;
                final RuntimeException ex = ex2;
                if (Log.isLoggable("DecodePath", 2)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to decode data for ");
                    sb.append(obj);
                    Log.v("DecodePath", sb.toString(), (Throwable)ex);
                }
                c.add(ex);
                decode = resource;
            }
            if (decode != null) {
                break;
            }
            ++n3;
            resource = decode;
        }
        if (decode != null) {
            return decode;
        }
        throw new GlideException(this.failureMessage, new ArrayList<Exception>(c));
    }
    
    public Resource<Transcode> decode(final DataRewinder<DataType> dataRewinder, final int n, final int n2, final Options options, final DecodeCallback<ResourceType> decodeCallback) throws GlideException {
        return this.transcoder.transcode(decodeCallback.onResourceDecoded(this.decodeResource(dataRewinder, n, n2, options)), options);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DecodePath{ dataClass=");
        sb.append(this.dataClass);
        sb.append(", decoders=");
        sb.append(this.decoders);
        sb.append(", transcoder=");
        sb.append(this.transcoder);
        sb.append('}');
        return sb.toString();
    }
    
    interface DecodeCallback<ResourceType>
    {
        Resource<ResourceType> onResourceDecoded(final Resource<ResourceType> p0);
    }
}

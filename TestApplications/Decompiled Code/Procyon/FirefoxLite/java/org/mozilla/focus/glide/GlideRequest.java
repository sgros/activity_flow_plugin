// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.glide;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

public class GlideRequest<TranscodeType> extends RequestBuilder<TranscodeType>
{
    GlideRequest(final Glide glide, final RequestManager requestManager, final Class<TranscodeType> clazz) {
        super(glide, requestManager, clazz);
    }
    
    @Override
    public GlideRequest<TranscodeType> apply(final RequestOptions requestOptions) {
        return (GlideRequest)super.apply(requestOptions);
    }
    
    @Override
    public GlideRequest<TranscodeType> clone() {
        return (GlideRequest)super.clone();
    }
    
    public GlideRequest<TranscodeType> fitCenter() {
        if (this.getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions)this.getMutableOptions()).fitCenter();
        }
        else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).fitCenter();
        }
        return this;
    }
    
    @Override
    public GlideRequest<TranscodeType> listener(final RequestListener<TranscodeType> requestListener) {
        return (GlideRequest)super.listener(requestListener);
    }
    
    @Override
    public GlideRequest<TranscodeType> load(final Object o) {
        return (GlideRequest)super.load(o);
    }
    
    @Override
    public GlideRequest<TranscodeType> load(final String s) {
        return (GlideRequest)super.load(s);
    }
    
    public GlideRequest<TranscodeType> placeholder(final int n) {
        if (this.getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions)this.getMutableOptions()).placeholder(n);
        }
        else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).placeholder(n);
        }
        return this;
    }
}

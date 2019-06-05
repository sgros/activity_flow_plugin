package org.mozilla.focus.glide;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

public class GlideRequest<TranscodeType> extends RequestBuilder<TranscodeType> {
    GlideRequest(Glide glide, RequestManager requestManager, Class<TranscodeType> cls) {
        super(glide, requestManager, cls);
    }

    public GlideRequest<TranscodeType> placeholder(int i) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).placeholder(i);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).placeholder(i);
        }
        return this;
    }

    public GlideRequest<TranscodeType> fitCenter() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).fitCenter();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).fitCenter();
        }
        return this;
    }

    public GlideRequest<TranscodeType> apply(RequestOptions requestOptions) {
        return (GlideRequest) super.apply(requestOptions);
    }

    public GlideRequest<TranscodeType> listener(RequestListener<TranscodeType> requestListener) {
        return (GlideRequest) super.listener(requestListener);
    }

    public GlideRequest<TranscodeType> load(Object obj) {
        return (GlideRequest) super.load(obj);
    }

    public GlideRequest<TranscodeType> load(String str) {
        return (GlideRequest) super.load(str);
    }

    public GlideRequest<TranscodeType> clone() {
        return (GlideRequest) super.clone();
    }
}

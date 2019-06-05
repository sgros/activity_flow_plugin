package org.mozilla.focus.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.request.RequestOptions;

public class GlideRequests extends RequestManager {
    public GlideRequests(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode) {
        super(glide, lifecycle, requestManagerTreeNode);
    }

    /* renamed from: as */
    public <ResourceType> GlideRequest<ResourceType> m13as(Class<ResourceType> cls) {
        return new GlideRequest(this.glide, this, cls);
    }

    public GlideRequest<Bitmap> asBitmap() {
        return (GlideRequest) super.asBitmap();
    }

    public GlideRequest<Drawable> asDrawable() {
        return (GlideRequest) super.asDrawable();
    }

    public GlideRequest<Drawable> load(Object obj) {
        return (GlideRequest) super.load(obj);
    }

    /* Access modifiers changed, original: protected */
    public void setRequestOptions(RequestOptions requestOptions) {
        if (requestOptions instanceof GlideOptions) {
            super.setRequestOptions(requestOptions);
        } else {
            super.setRequestOptions(new GlideOptions().apply(requestOptions));
        }
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.glide;

import com.bumptech.glide.request.RequestOptions;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

public class GlideRequests extends RequestManager
{
    public GlideRequests(final Glide glide, final Lifecycle lifecycle, final RequestManagerTreeNode requestManagerTreeNode) {
        super(glide, lifecycle, requestManagerTreeNode);
    }
    
    @Override
    public <ResourceType> GlideRequest<ResourceType> as(final Class<ResourceType> clazz) {
        return new GlideRequest<ResourceType>(this.glide, this, clazz);
    }
    
    @Override
    public GlideRequest<Bitmap> asBitmap() {
        return (GlideRequest<Bitmap>)(GlideRequest)super.asBitmap();
    }
    
    @Override
    public GlideRequest<Drawable> asDrawable() {
        return (GlideRequest<Drawable>)(GlideRequest)super.asDrawable();
    }
    
    @Override
    public GlideRequest<Drawable> load(final Object o) {
        return (GlideRequest<Drawable>)(GlideRequest)super.load(o);
    }
    
    @Override
    protected void setRequestOptions(final RequestOptions requestOptions) {
        if (requestOptions instanceof GlideOptions) {
            super.setRequestOptions(requestOptions);
        }
        else {
            super.setRequestOptions(new GlideOptions().apply(requestOptions));
        }
    }
}

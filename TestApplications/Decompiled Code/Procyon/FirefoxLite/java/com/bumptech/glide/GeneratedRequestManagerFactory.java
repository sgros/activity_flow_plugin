// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide;

import org.mozilla.focus.glide.GlideRequests;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerRetriever;

final class GeneratedRequestManagerFactory implements RequestManagerFactory
{
    @Override
    public RequestManager build(final Glide glide, final Lifecycle lifecycle, final RequestManagerTreeNode requestManagerTreeNode) {
        return new GlideRequests(glide, lifecycle, requestManagerTreeNode);
    }
}

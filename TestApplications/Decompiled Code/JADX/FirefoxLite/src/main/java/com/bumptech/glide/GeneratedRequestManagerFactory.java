package com.bumptech.glide;

import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerRetriever.RequestManagerFactory;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import org.mozilla.focus.glide.GlideRequests;

final class GeneratedRequestManagerFactory implements RequestManagerFactory {
    GeneratedRequestManagerFactory() {
    }

    public RequestManager build(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode) {
        return new GlideRequests(glide, lifecycle, requestManagerTreeNode);
    }
}

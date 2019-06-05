package com.bumptech.glide;

import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import org.mozilla.focus.glide.GlideRequests;

final class GeneratedRequestManagerFactory implements RequestManagerRetriever.RequestManagerFactory {
   public RequestManager build(Glide var1, Lifecycle var2, RequestManagerTreeNode var3) {
      return new GlideRequests(var1, var2, var3);
   }
}

package org.mozilla.focus.webkit.matcher;

import android.net.Uri;
import android.text.TextUtils;
import org.mozilla.focus.webkit.matcher.util.FocusString;
import org.mozilla.urlutils.UrlUtils;

class EntityList {
   private Trie.WhiteListTrie rootNode = Trie.WhiteListTrie.createRootNode();

   public EntityList() {
   }

   private boolean isWhiteListed(FocusString var1, FocusString var2, Trie var3) {
      Trie.WhiteListTrie var4 = (Trie.WhiteListTrie)var3.children.get(var1.charAt(0));
      if (var4 == null) {
         return false;
      } else if (var4.whitelist != null && var4.whitelist.findNode(var2) != null) {
         return true;
      } else {
         return var1.length() == 1 ? false : this.isWhiteListed(var1.substring(1), var2, var4);
      }
   }

   public boolean isWhiteListed(Uri var1, Uri var2) {
      if (!TextUtils.isEmpty(var1.getHost()) && !TextUtils.isEmpty(var2.getHost()) && !var1.getScheme().equals("data")) {
         return UrlUtils.isPermittedResourceProtocol(var2.getScheme()) && UrlUtils.isSupportedProtocol(var1.getScheme()) ? this.isWhiteListed(FocusString.create(var1.getHost()).reverse(), FocusString.create(var2.getHost()).reverse(), this.rootNode) : false;
      } else {
         return false;
      }
   }

   public void putWhiteList(FocusString var1, Trie var2) {
      this.rootNode.putWhiteList(var1, var2);
   }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit.matcher;

import org.mozilla.urlutils.UrlUtils;
import android.text.TextUtils;
import android.net.Uri;
import org.mozilla.focus.webkit.matcher.util.FocusString;

class EntityList
{
    private Trie.WhiteListTrie rootNode;
    
    public EntityList() {
        this.rootNode = Trie.WhiteListTrie.createRootNode();
    }
    
    private boolean isWhiteListed(final FocusString focusString, final FocusString focusString2, final Trie trie) {
        final Trie.WhiteListTrie whiteListTrie = (Trie.WhiteListTrie)trie.children.get((int)focusString.charAt(0));
        return whiteListTrie != null && ((whiteListTrie.whitelist != null && whiteListTrie.whitelist.findNode(focusString2) != null) || (focusString.length() != 1 && this.isWhiteListed(focusString.substring(1), focusString2, whiteListTrie)));
    }
    
    public boolean isWhiteListed(final Uri uri, final Uri uri2) {
        return !TextUtils.isEmpty((CharSequence)uri.getHost()) && !TextUtils.isEmpty((CharSequence)uri2.getHost()) && !uri.getScheme().equals("data") && (UrlUtils.isPermittedResourceProtocol(uri2.getScheme()) && UrlUtils.isSupportedProtocol(uri.getScheme())) && this.isWhiteListed(FocusString.create(uri.getHost()).reverse(), FocusString.create(uri2.getHost()).reverse(), this.rootNode);
    }
    
    public void putWhiteList(final FocusString focusString, final Trie trie) {
        this.rootNode.putWhiteList(focusString, trie);
    }
}

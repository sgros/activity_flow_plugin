package org.mozilla.focus.webkit.matcher;

import org.mozilla.focus.webkit.matcher.Trie.WhiteListTrie;
import org.mozilla.focus.webkit.matcher.util.FocusString;

class EntityList {
    private WhiteListTrie rootNode = WhiteListTrie.createRootNode();

    public void putWhiteList(FocusString focusString, Trie trie) {
        this.rootNode.putWhiteList(focusString, trie);
    }

    /* JADX WARNING: Missing block: B:13:0x0056, code skipped:
            return false;
     */
    public boolean isWhiteListed(android.net.Uri r4, android.net.Uri r5) {
        /*
        r3 = this;
        r0 = r4.getHost();
        r0 = android.text.TextUtils.isEmpty(r0);
        r1 = 0;
        if (r0 != 0) goto L_0x0056;
    L_0x000b:
        r0 = r5.getHost();
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x0056;
    L_0x0015:
        r0 = r4.getScheme();
        r2 = "data";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x0022;
    L_0x0021:
        goto L_0x0056;
    L_0x0022:
        r0 = r5.getScheme();
        r0 = org.mozilla.urlutils.UrlUtils.isPermittedResourceProtocol(r0);
        if (r0 == 0) goto L_0x0055;
    L_0x002c:
        r0 = r4.getScheme();
        r0 = org.mozilla.urlutils.UrlUtils.isSupportedProtocol(r0);
        if (r0 == 0) goto L_0x0055;
    L_0x0036:
        r4 = r4.getHost();
        r4 = org.mozilla.focus.webkit.matcher.util.FocusString.create(r4);
        r4 = r4.reverse();
        r5 = r5.getHost();
        r5 = org.mozilla.focus.webkit.matcher.util.FocusString.create(r5);
        r5 = r5.reverse();
        r0 = r3.rootNode;
        r4 = r3.isWhiteListed(r4, r5, r0);
        return r4;
    L_0x0055:
        return r1;
    L_0x0056:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.webkit.matcher.EntityList.isWhiteListed(android.net.Uri, android.net.Uri):boolean");
    }

    private boolean isWhiteListed(FocusString focusString, FocusString focusString2, Trie trie) {
        WhiteListTrie whiteListTrie = (WhiteListTrie) trie.children.get(focusString.charAt(0));
        if (whiteListTrie == null) {
            return false;
        }
        if (whiteListTrie.whitelist != null && whiteListTrie.whitelist.findNode(focusString2) != null) {
            return true;
        }
        if (focusString.length() == 1) {
            return false;
        }
        return isWhiteListed(focusString.substring(1), focusString2, whiteListTrie);
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit.matcher;

import org.mozilla.focus.webkit.matcher.util.FocusString;
import android.util.SparseArray;

class Trie
{
    public final SparseArray<Trie> children;
    public boolean terminator;
    
    private Trie(final char c, final Trie trie) {
        this.children = (SparseArray<Trie>)new SparseArray();
        this.terminator = false;
        if (trie != null) {
            trie.children.put((int)c, (Object)this);
        }
    }
    
    public static Trie createRootNode() {
        return new Trie('\0', null);
    }
    
    protected Trie createNode(final char c, final Trie trie) {
        return new Trie(c, trie);
    }
    
    public Trie findNode(final FocusString focusString) {
        if (this.terminator) {
            if (focusString.length() == 0 || focusString.charAt(0) == '.') {
                return this;
            }
        }
        else if (focusString.length() == 0) {
            return null;
        }
        final Trie trie = (Trie)this.children.get((int)focusString.charAt(0));
        if (trie == null) {
            return null;
        }
        return trie.findNode(focusString.substring(1));
    }
    
    public Trie put(final char c) {
        final Trie trie = (Trie)this.children.get((int)c);
        if (trie != null) {
            return trie;
        }
        final Trie node = this.createNode(c, this);
        this.children.put((int)c, (Object)node);
        return node;
    }
    
    public Trie put(final FocusString focusString) {
        if (focusString.length() == 0) {
            this.terminator = true;
            return this;
        }
        return this.put(focusString.charAt(0)).put(focusString.substring(1));
    }
    
    public static class WhiteListTrie extends Trie
    {
        Trie whitelist;
        
        private WhiteListTrie(final char c, final WhiteListTrie whiteListTrie) {
            super(c, whiteListTrie, null);
            this.whitelist = null;
        }
        
        public static WhiteListTrie createRootNode() {
            return new WhiteListTrie('\0', null);
        }
        
        @Override
        protected Trie createNode(final char c, final Trie trie) {
            return new WhiteListTrie(c, (WhiteListTrie)trie);
        }
        
        public void putWhiteList(final FocusString obj, final Trie whitelist) {
            final WhiteListTrie whiteListTrie = (WhiteListTrie)super.put(obj);
            if (whiteListTrie.whitelist == null) {
                whiteListTrie.whitelist = whitelist;
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Whitelist already set for node ");
            sb.append(obj);
            throw new IllegalStateException(sb.toString());
        }
    }
}

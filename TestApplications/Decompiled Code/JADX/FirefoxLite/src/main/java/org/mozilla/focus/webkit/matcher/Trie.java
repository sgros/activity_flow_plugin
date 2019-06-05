package org.mozilla.focus.webkit.matcher;

import android.util.SparseArray;
import org.mozilla.focus.webkit.matcher.util.FocusString;

class Trie {
    public final SparseArray<Trie> children;
    public boolean terminator;

    public static class WhiteListTrie extends Trie {
        Trie whitelist = null;

        public /* bridge */ /* synthetic */ Trie findNode(FocusString focusString) {
            return super.findNode(focusString);
        }

        private WhiteListTrie(char c, WhiteListTrie whiteListTrie) {
            super(c, whiteListTrie);
        }

        /* Access modifiers changed, original: protected */
        public Trie createNode(char c, Trie trie) {
            return new WhiteListTrie(c, (WhiteListTrie) trie);
        }

        public static WhiteListTrie createRootNode() {
            return new WhiteListTrie(0, null);
        }

        public void putWhiteList(FocusString focusString, Trie trie) {
            WhiteListTrie whiteListTrie = (WhiteListTrie) super.put(focusString);
            if (whiteListTrie.whitelist == null) {
                whiteListTrie.whitelist = trie;
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Whitelist already set for node ");
            stringBuilder.append(focusString);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    public Trie findNode(FocusString focusString) {
        if (this.terminator) {
            if (focusString.length() == 0 || focusString.charAt(0) == '.') {
                return this;
            }
        } else if (focusString.length() == 0) {
            return null;
        }
        Trie trie = (Trie) this.children.get(focusString.charAt(0));
        if (trie == null) {
            return null;
        }
        return trie.findNode(focusString.substring(1));
    }

    public Trie put(FocusString focusString) {
        if (focusString.length() != 0) {
            return put(focusString.charAt(0)).put(focusString.substring(1));
        }
        this.terminator = true;
        return this;
    }

    public Trie put(char c) {
        Trie trie = (Trie) this.children.get(c);
        if (trie != null) {
            return trie;
        }
        trie = createNode(c, this);
        this.children.put(c, trie);
        return trie;
    }

    private Trie(char c, Trie trie) {
        this.children = new SparseArray();
        this.terminator = false;
        if (trie != null) {
            trie.children.put(c, this);
        }
    }

    public static Trie createRootNode() {
        return new Trie(0, null);
    }

    /* Access modifiers changed, original: protected */
    public Trie createNode(char c, Trie trie) {
        return new Trie(c, trie);
    }
}

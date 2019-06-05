package org.mozilla.focus.webkit.matcher;

import android.util.SparseArray;
import org.mozilla.focus.webkit.matcher.util.FocusString;

class Trie {
   public final SparseArray children;
   public boolean terminator;

   private Trie(char var1, Trie var2) {
      this.children = new SparseArray();
      this.terminator = false;
      if (var2 != null) {
         var2.children.put(var1, this);
      }

   }

   // $FF: synthetic method
   Trie(char var1, Trie var2, Object var3) {
      this(var1, var2);
   }

   public static Trie createRootNode() {
      return new Trie('\u0000', (Trie)null);
   }

   protected Trie createNode(char var1, Trie var2) {
      return new Trie(var1, var2);
   }

   public Trie findNode(FocusString var1) {
      if (this.terminator) {
         if (var1.length() == 0 || var1.charAt(0) == '.') {
            return this;
         }
      } else if (var1.length() == 0) {
         return null;
      }

      Trie var2 = (Trie)this.children.get(var1.charAt(0));
      if (var2 == null) {
         return null;
      } else {
         return var2.findNode(var1.substring(1));
      }
   }

   public Trie put(char var1) {
      Trie var2 = (Trie)this.children.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = this.createNode(var1, this);
         this.children.put(var1, var2);
         return var2;
      }
   }

   public Trie put(FocusString var1) {
      if (var1.length() == 0) {
         this.terminator = true;
         return this;
      } else {
         return this.put(var1.charAt(0)).put(var1.substring(1));
      }
   }

   public static class WhiteListTrie extends Trie {
      Trie whitelist = null;

      private WhiteListTrie(char var1, Trie.WhiteListTrie var2) {
         super(var1, var2, null);
      }

      public static Trie.WhiteListTrie createRootNode() {
         return new Trie.WhiteListTrie('\u0000', (Trie.WhiteListTrie)null);
      }

      protected Trie createNode(char var1, Trie var2) {
         return new Trie.WhiteListTrie(var1, (Trie.WhiteListTrie)var2);
      }

      public void putWhiteList(FocusString var1, Trie var2) {
         Trie.WhiteListTrie var3 = (Trie.WhiteListTrie)super.put(var1);
         if (var3.whitelist == null) {
            var3.whitelist = var2;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Whitelist already set for node ");
            var4.append(var1);
            throw new IllegalStateException(var4.toString());
         }
      }
   }
}

package org.mozilla.focus.webkit.matcher;

import android.util.JsonReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mozilla.focus.webkit.matcher.util.FocusString;

public class BlocklistProcessor {
   private static final Set DISCONNECT_MOVED;
   private static final Set IGNORED_CATEGORIES;

   static {
      HashSet var0 = new HashSet();
      var0.add("Legacy Disconnect");
      var0.add("Legacy Content");
      IGNORED_CATEGORIES = Collections.unmodifiableSet(var0);
      var0 = new HashSet();
      var0.add("Facebook");
      var0.add("Twitter");
      DISCONNECT_MOVED = Collections.unmodifiableSet(var0);
   }

   private static void extractCategories(JsonReader var0, Map var1, BlocklistProcessor.ListType var2) throws IOException {
      var0.beginObject();
      LinkedList var3 = new LinkedList();

      while(var0.hasNext()) {
         String var4 = var0.nextName();
         if (IGNORED_CATEGORIES.contains(var4)) {
            var0.skipValue();
         } else if (var4.equals("Disconnect")) {
            extractCategory(var0, new BlocklistProcessor.ListCallback(var3, DISCONNECT_MOVED));
         } else {
            Trie var5;
            if (var2 == BlocklistProcessor.ListType.BASE_LIST) {
               if (var1.containsKey(var4)) {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Cannot insert already loaded category: ");
                  var6.append(var4);
                  throw new IllegalStateException(var6.toString());
               }

               var5 = Trie.createRootNode();
               var1.put(var4, var5);
            } else {
               var5 = (Trie)var1.get(var4);
               if (var5 == null) {
                  throw new IllegalStateException("Cannot add override items to nonexistent category");
               }
            }

            extractCategory(var0, new BlocklistProcessor.TrieCallback(var5));
         }
      }

      Trie var7 = (Trie)var1.get("Social");
      if (var7 == null && var2 == BlocklistProcessor.ListType.BASE_LIST) {
         throw new IllegalStateException("Expected social list to exist. Can't copy FB/Twitter into non-existing list");
      } else {
         Iterator var8 = var3.iterator();

         while(var8.hasNext()) {
            var7.put(FocusString.create((String)var8.next()).reverse());
         }

         var0.endObject();
      }
   }

   private static void extractCategory(JsonReader var0, BlocklistProcessor.UrlListCallback var1) throws IOException {
      var0.beginArray();

      while(var0.hasNext()) {
         extractSite(var0, var1);
      }

      var0.endArray();
   }

   private static void extractSite(JsonReader var0, BlocklistProcessor.UrlListCallback var1) throws IOException {
      var0.beginObject();
      String var2 = var0.nextName();
      var0.beginObject();

      while(true) {
         while(var0.hasNext()) {
            var0.skipValue();
            if (var0.peek().name().equals("STRING")) {
               var0.skipValue();
            } else {
               var0.beginArray();

               while(var0.hasNext()) {
                  var1.put(var0.nextString(), var2);
               }

               var0.endArray();
            }
         }

         var0.endObject();
         var0.endObject();
         return;
      }
   }

   public static Map loadCategoryMap(JsonReader var0, Map var1, BlocklistProcessor.ListType var2) throws IOException {
      var0.beginObject();

      while(var0.hasNext()) {
         if (var0.nextName().equals("categories")) {
            extractCategories(var0, var1, var2);
         } else {
            var0.skipValue();
         }
      }

      var0.endObject();
      return var1;
   }

   private static class ListCallback implements BlocklistProcessor.UrlListCallback {
      final Set desiredOwners;
      final List list;

      ListCallback(List var1, Set var2) {
         this.list = var1;
         this.desiredOwners = var2;
      }

      public void put(String var1, String var2) {
         if (this.desiredOwners.contains(var2)) {
            this.list.add(var1);
         }

      }
   }

   public static enum ListType {
      BASE_LIST,
      OVERRIDE_LIST;
   }

   private static class TrieCallback implements BlocklistProcessor.UrlListCallback {
      final Trie trie;

      TrieCallback(Trie var1) {
         this.trie = var1;
      }

      public void put(String var1, String var2) {
         this.trie.put(FocusString.create(var1).reverse());
      }
   }

   private interface UrlListCallback {
      void put(String var1, String var2);
   }
}

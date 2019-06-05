package com.google.zxing.client.result;

abstract class AbstractDoCoMoResultParser extends ResultParser {
   static String[] matchDoCoMoPrefixedField(String var0, String var1, boolean var2) {
      return matchPrefixedField(var0, var1, ';', var2);
   }

   static String matchSingleDoCoMoPrefixedField(String var0, String var1, boolean var2) {
      return matchSinglePrefixedField(var0, var1, ';', var2);
   }
}

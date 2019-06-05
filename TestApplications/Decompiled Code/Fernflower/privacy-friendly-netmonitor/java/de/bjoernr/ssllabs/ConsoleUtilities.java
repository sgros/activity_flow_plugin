package de.bjoernr.ssllabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConsoleUtilities {
   private static String newLine;

   public static String arrayValueMatchRegex(String[] var0, String var1) {
      Pattern var2 = Pattern.compile(var1);

      for(int var3 = 0; var3 < var0.length; ++var3) {
         Matcher var6 = var2.matcher(var0[var3]);

         while(var6.find()) {
            try {
               String var4 = var6.group(1);
               return var4;
            } catch (Exception var5) {
            }
         }
      }

      return null;
   }

   public static Map jsonToMap(JSONObject var0) throws JSONException {
      Object var1 = new HashMap();
      if (var0 != JSONObject.NULL) {
         var1 = toMap(var0);
      }

      return (Map)var1;
   }

   public static String mapToConsoleOutput(Map var0) {
      String var1 = "";
      Iterator var2 = var0.entrySet().iterator();

      String var4;
      StringBuilder var5;
      for(var4 = var1; var2.hasNext(); var4 = var5.toString()) {
         Entry var3 = (Entry)var2.next();
         StringBuilder var6 = new StringBuilder();
         var6.append(var4);
         var6.append((String)var3.getKey());
         var6.append(" = ");
         var6.append(var3.getValue().toString());
         var1 = var6.toString();
         var5 = new StringBuilder();
         var5.append(var1);
         var5.append(newLine);
      }

      return var4;
   }

   public static List toList(JSONArray var0) throws JSONException {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         Object var3 = var0.get(var2);
         Object var4;
         if (var3 instanceof JSONArray) {
            var4 = toList((JSONArray)var3);
         } else {
            var4 = var3;
            if (var3 instanceof JSONObject) {
               var4 = toMap((JSONObject)var3);
            }
         }

         var1.add(var4);
      }

      return var1;
   }

   public static Map toMap(JSONObject var0) throws JSONException {
      HashMap var1 = new HashMap();

      String var3;
      Object var5;
      for(Iterator var2 = var0.keys(); var2.hasNext(); var1.put(var3, var5)) {
         var3 = (String)var2.next();
         Object var4 = var0.get(var3);
         if (var4 instanceof JSONArray) {
            var5 = toList((JSONArray)var4);
         } else {
            var5 = var4;
            if (var4 instanceof JSONObject) {
               var5 = toMap((JSONObject)var4);
            }
         }
      }

      return var1;
   }
}

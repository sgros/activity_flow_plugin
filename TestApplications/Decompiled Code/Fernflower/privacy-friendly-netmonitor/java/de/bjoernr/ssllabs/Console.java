package de.bjoernr.ssllabs;

import java.io.PrintStream;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Console {
   public static void handleHostInformation(String[] var0) {
      String var1 = ConsoleUtilities.arrayValueMatchRegex(var0, "-h=(.+)");
      if (var1 == null) {
         printUsage();
      } else {
         String[] var2 = new String[]{"-p", "-c", "-m", "-a", "-i"};
         int var3 = var2.length;
         int var4 = 0;
         int var6 = var4;
         int var7 = var4;
         String var8 = null;
         String var9 = var8;

         int var10;
         for(var10 = var4; var4 < var3; ++var4) {
            String var11 = var2[var4];
            if (ConsoleUtilities.arrayValueMatchRegex(var0, var11) != null) {
               byte var5 = -1;
               int var12 = var11.hashCode();
               if (var12 != 1492) {
                  if (var12 != 1494) {
                     if (var12 != 1500) {
                        if (var12 != 1504) {
                           if (var12 == 1507 && var11.equals("-p")) {
                              var5 = 0;
                           }
                        } else if (var11.equals("-m")) {
                           var5 = 2;
                        }
                     } else if (var11.equals("-i")) {
                        var5 = 4;
                     }
                  } else if (var11.equals("-c")) {
                     var5 = 1;
                  }
               } else if (var11.equals("-a")) {
                  var5 = 3;
               }

               switch(var5) {
               case 0:
                  var10 = 1;
                  break;
               case 1:
                  var6 = 1;
                  break;
               case 2:
                  var8 = ConsoleUtilities.arrayValueMatchRegex(var0, "-m=(.+)");
                  break;
               case 3:
                  var9 = ConsoleUtilities.arrayValueMatchRegex(var0, "-a=(.+)");
                  break;
               case 4:
                  var7 = 1;
               }
            }
         }

         JSONObject var14 = (new Api()).fetchHostInformation(var1, (boolean)var10, false, (boolean)var6, var8, var9, (boolean)var7);

         Map var15;
         try {
            var15 = ConsoleUtilities.jsonToMap(var14);
         } catch (JSONException var13) {
            var15 = null;
         }

         System.out.println("Host information");
         System.out.println("");
         System.out.println(ConsoleUtilities.mapToConsoleOutput(var15));
      }
   }

   public static void handleInfo() {
      JSONObject var0 = (new Api()).fetchApiInfo();

      Map var2;
      try {
         var2 = ConsoleUtilities.jsonToMap(var0);
      } catch (JSONException var1) {
         var2 = null;
      }

      System.out.println("API information");
      System.out.println("");
      System.out.println(ConsoleUtilities.mapToConsoleOutput(var2));
   }

   public static void main(String[] var0) {
      printHeader();
      if (var0.length != 1 || !var0[0].equals("--info") && !var0[0].equals("-i")) {
         if (var0.length <= 0 || var0.length > 6 || !var0[0].equals("--host-information") && !var0[0].equals("-hi")) {
            printUsage();
         } else {
            handleHostInformation(var0);
         }
      } else {
         handleInfo();
      }

   }

   public static void printHeader() {
      System.out.println("");
      System.out.println("   ___                    _____ _____ _      _           _            ___  ______ _____ ");
      System.out.println("  |_  |                  /  ___/  ___| |    | |         | |          / _ \\ | ___ \\_   _|");
      System.out.println("    | | __ ___   ____ _  \\ `--.\\ `--.| |    | |     __ _| |__  ___  / /_\\ \\| |_/ / | |  ");
      System.out.println("    | |/ _` \\ \\ / / _` |  `--. \\`--. \\ |    | |    / _` | '_ \\/ __| |  _  ||  __/  | |  ");
      System.out.println("/\\__/ / (_| |\\ V / (_| | /\\__/ /\\__/ / |____| |___| (_| | |_) \\__ \\ | | | || |    _| |_ ");
      System.out.println("\\____/ \\__,_| \\_/ \\__,_| \\____/\\____/\\_____/\\_____/\\__,_|_.__/|___/ \\_| |_/\\_|    \\___/ ");
      System.out.println("by Bjoern Roland <https://github.com/bjoernr-de>");
      System.out.println("and contributors (https://github.com/bjoernr-de/java-ssllabs-api/graphs/contributors)");
      System.out.println("-------------------------------------------------");
      System.out.println("");
   }

   public static void printUsage() {
      StringBuilder var0 = new StringBuilder();
      var0.append("java-ssllabs-api-");
      var0.append(Api.getVersion());
      var0.append(".jar");
      String var3 = var0.toString();
      StringBuilder var1 = new StringBuilder();
      var1.append("java -jar ");
      var1.append(var3);
      String var4 = var1.toString();
      System.out.println("Help");
      System.out.println(var4);
      System.out.println("");
      System.out.println("-i, --info");
      System.out.println("\tFetch API information");
      System.out.println("");
      System.out.println("-hi, --host-information");
      System.out.println("\tMandatory parameter:");
      System.out.println("\t-h, --host (String)");
      System.out.println("");
      System.out.println("\tAdditional parameter:");
      System.out.println("\t-p, --publish (boolean) - default value is false");
      System.out.println("\t-c, --fromCache (boolean) - default value is false");
      System.out.println("\t-m, --maxAge (String)");
      System.out.println("\t-a, --all (String)");
      System.out.println("\t-i, --ignoreMismatch (boolean) - default value is false");
      System.out.println("");
      System.out.println("\tExample:");
      PrintStream var2 = System.out;
      var0 = new StringBuilder();
      var0.append("\t");
      var0.append(var4);
      var0.append(" -hi -h=https://ssllabs.com -p -c -m=\"1\"");
      var2.println(var0.toString());
   }
}

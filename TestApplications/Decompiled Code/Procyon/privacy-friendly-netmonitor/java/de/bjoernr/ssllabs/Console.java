// 
// Decompiled by Procyon v0.5.34
// 

package de.bjoernr.ssllabs;

import java.io.PrintStream;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONException;

public class Console
{
    public static void handleHostInformation(final String[] array) {
        final String arrayValueMatchRegex = ConsoleUtilities.arrayValueMatchRegex(array, "-h=(.+)");
        if (arrayValueMatchRegex == null) {
            printUsage();
            return;
        }
        final String[] array2 = { "-p", "-c", "-m", "-a", "-i" };
        final int length = array2.length;
        final int n;
        int i = n = 0;
        int n3;
        int n2 = n3 = n;
        String arrayValueMatchRegex3;
        String arrayValueMatchRegex2 = arrayValueMatchRegex3 = null;
        int n4 = n;
        while (i < length) {
            final String s = array2[i];
            if (ConsoleUtilities.arrayValueMatchRegex(array, s) != null) {
                int n5 = -1;
                final int hashCode = s.hashCode();
                if (hashCode != 1492) {
                    if (hashCode != 1494) {
                        if (hashCode != 1500) {
                            if (hashCode != 1504) {
                                if (hashCode == 1507) {
                                    if (s.equals("-p")) {
                                        n5 = 0;
                                    }
                                }
                            }
                            else if (s.equals("-m")) {
                                n5 = 2;
                            }
                        }
                        else if (s.equals("-i")) {
                            n5 = 4;
                        }
                    }
                    else if (s.equals("-c")) {
                        n5 = 1;
                    }
                }
                else if (s.equals("-a")) {
                    n5 = 3;
                }
                switch (n5) {
                    case 4: {
                        n3 = 1;
                        break;
                    }
                    case 3: {
                        arrayValueMatchRegex3 = ConsoleUtilities.arrayValueMatchRegex(array, "-a=(.+)");
                        break;
                    }
                    case 2: {
                        arrayValueMatchRegex2 = ConsoleUtilities.arrayValueMatchRegex(array, "-m=(.+)");
                        break;
                    }
                    case 1: {
                        n2 = 1;
                        break;
                    }
                    case 0: {
                        n4 = 1;
                        break;
                    }
                }
            }
            ++i;
        }
        final JSONObject fetchHostInformation = new Api().fetchHostInformation(arrayValueMatchRegex, (boolean)(n4 != 0), false, (boolean)(n2 != 0), arrayValueMatchRegex2, arrayValueMatchRegex3, (boolean)(n3 != 0));
        Map<String, Object> jsonToMap;
        try {
            jsonToMap = ConsoleUtilities.jsonToMap(fetchHostInformation);
        }
        catch (JSONException ex) {
            jsonToMap = null;
        }
        System.out.println("Host information");
        System.out.println("");
        System.out.println(ConsoleUtilities.mapToConsoleOutput(jsonToMap));
    }
    
    public static void handleInfo() {
        final JSONObject fetchApiInfo = new Api().fetchApiInfo();
        Map<String, Object> jsonToMap;
        try {
            jsonToMap = ConsoleUtilities.jsonToMap(fetchApiInfo);
        }
        catch (JSONException ex) {
            jsonToMap = null;
        }
        System.out.println("API information");
        System.out.println("");
        System.out.println(ConsoleUtilities.mapToConsoleOutput(jsonToMap));
    }
    
    public static void main(final String[] array) {
        printHeader();
        if (array.length == 1 && (array[0].equals("--info") || array[0].equals("-i"))) {
            handleInfo();
        }
        else if (array.length > 0 && array.length <= 6 && (array[0].equals("--host-information") || array[0].equals("-hi"))) {
            handleHostInformation(array);
        }
        else {
            printUsage();
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
        final StringBuilder sb = new StringBuilder();
        sb.append("java-ssllabs-api-");
        sb.append(Api.getVersion());
        sb.append(".jar");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("java -jar ");
        sb2.append(string);
        final String string2 = sb2.toString();
        System.out.println("Help");
        System.out.println(string2);
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
        final PrintStream out = System.out;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("\t");
        sb3.append(string2);
        sb3.append(" -hi -h=https://ssllabs.com -p -c -m=\"1\"");
        out.println(sb3.toString());
    }
}

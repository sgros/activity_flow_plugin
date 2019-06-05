package p004de.bjoernr.ssllabs;

import java.io.PrintStream;
import java.util.Map;
import org.json.JSONException;

/* renamed from: de.bjoernr.ssllabs.Console */
public class Console {
    public static void main(String[] strArr) {
        Console.printHeader();
        if (strArr.length == 1 && (strArr[0].equals("--info") || strArr[0].equals("-i"))) {
            Console.handleInfo();
        } else if (strArr.length <= 0 || strArr.length > 6 || !(strArr[0].equals("--host-information") || strArr[0].equals("-hi"))) {
            Console.printUsage();
        } else {
            Console.handleHostInformation(strArr);
        }
    }

    public static void handleInfo() {
        Map jsonToMap;
        try {
            jsonToMap = ConsoleUtilities.jsonToMap(new Api().fetchApiInfo());
        } catch (JSONException unused) {
            jsonToMap = null;
        }
        System.out.println("API information");
        System.out.println("");
        System.out.println(ConsoleUtilities.mapToConsoleOutput(jsonToMap));
    }

    public static void handleHostInformation(String[] strArr) {
        String[] strArr2 = strArr;
        String arrayValueMatchRegex = ConsoleUtilities.arrayValueMatchRegex(strArr2, "-h=(.+)");
        if (arrayValueMatchRegex == null) {
            Console.printUsage();
            return;
        }
        Map jsonToMap;
        String[] strArr3 = new String[]{"-p", "-c", "-m", "-a", "-i"};
        int length = strArr3.length;
        int i = 0;
        boolean z = i;
        boolean z2 = z;
        boolean z3 = z2;
        String str = null;
        String str2 = str;
        while (i < length) {
            String str3 = strArr3[i];
            if (ConsoleUtilities.arrayValueMatchRegex(strArr2, str3) != null) {
                int i2 = -1;
                int hashCode = str3.hashCode();
                if (hashCode != 1492) {
                    if (hashCode != 1494) {
                        if (hashCode != 1500) {
                            if (hashCode != 1504) {
                                if (hashCode == 1507 && str3.equals("-p")) {
                                    i2 = 0;
                                }
                            } else if (str3.equals("-m")) {
                                i2 = 2;
                            }
                        } else if (str3.equals("-i")) {
                            i2 = 4;
                        }
                    } else if (str3.equals("-c")) {
                        i2 = 1;
                    }
                } else if (str3.equals("-a")) {
                    i2 = 3;
                }
                switch (i2) {
                    case 0:
                        z = true;
                        break;
                    case 1:
                        z2 = true;
                        break;
                    case 2:
                        str = ConsoleUtilities.arrayValueMatchRegex(strArr2, "-m=(.+)");
                        break;
                    case 3:
                        str2 = ConsoleUtilities.arrayValueMatchRegex(strArr2, "-a=(.+)");
                        break;
                    case 4:
                        z3 = true;
                        break;
                    default:
                        break;
                }
            }
            i++;
        }
        try {
            jsonToMap = ConsoleUtilities.jsonToMap(new Api().fetchHostInformation(arrayValueMatchRegex, z, false, z2, str, str2, z3));
        } catch (JSONException unused) {
            jsonToMap = null;
        }
        System.out.println("Host information");
        System.out.println("");
        System.out.println(ConsoleUtilities.mapToConsoleOutput(jsonToMap));
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("java-ssllabs-api-");
        stringBuilder.append(Api.getVersion());
        stringBuilder.append(".jar");
        String stringBuilder2 = stringBuilder.toString();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("java -jar ");
        stringBuilder3.append(stringBuilder2);
        stringBuilder2 = stringBuilder3.toString();
        System.out.println("Help");
        System.out.println(stringBuilder2);
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
        PrintStream printStream = System.out;
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("\t");
        stringBuilder4.append(stringBuilder2);
        stringBuilder4.append(" -hi -h=https://ssllabs.com -p -c -m=\"1\"");
        printStream.println(stringBuilder4.toString());
    }
}

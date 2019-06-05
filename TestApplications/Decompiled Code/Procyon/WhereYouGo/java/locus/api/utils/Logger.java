// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.utils;

public class Logger
{
    private static ILogger logger;
    
    public static void logD(final String str, final String str2) {
        if (Logger.logger == null) {
            System.out.println(str + " - " + str2);
        }
        else {
            Logger.logger.logD(str, str2);
        }
    }
    
    public static void logE(final String str, final String str2) {
        if (Logger.logger == null) {
            System.err.println(str + " - " + str2);
        }
        else {
            Logger.logger.logE(str, str2);
        }
    }
    
    public static void logE(final String str, final String str2, final Exception ex) {
        if (Logger.logger == null) {
            System.err.println(str + " - " + str2 + ", e:" + ex.getMessage());
            ex.printStackTrace();
        }
        else {
            Logger.logger.logE(str, str2, ex);
        }
    }
    
    public static void logI(final String str, final String str2) {
        if (Logger.logger == null) {
            System.out.println(str + " - " + str2);
        }
        else {
            Logger.logger.logI(str, str2);
        }
    }
    
    public static void logW(final String str, final String str2) {
        if (Logger.logger == null) {
            System.out.println(str + " - " + str2);
        }
        else {
            Logger.logger.logW(str, str2);
        }
    }
    
    public static void registerLogger(final ILogger logger) {
        Logger.logger = logger;
    }
    
    public interface ILogger
    {
        void logD(final String p0, final String p1);
        
        void logE(final String p0, final String p1);
        
        void logE(final String p0, final String p1, final Exception p2);
        
        void logI(final String p0, final String p1);
        
        void logW(final String p0, final String p1);
    }
}

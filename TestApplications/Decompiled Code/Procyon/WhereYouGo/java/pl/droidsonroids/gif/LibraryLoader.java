// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.support.annotation.NonNull;
import android.annotation.SuppressLint;
import android.content.Context;

public class LibraryLoader
{
    static final String BASE_LIBRARY_NAME = "pl_droidsonroids_gif";
    static final String SURFACE_LIBRARY_NAME = "pl_droidsonroids_gif_surface";
    @SuppressLint({ "StaticFieldLeak" })
    private static Context sAppContext;
    
    private LibraryLoader() {
    }
    
    private static Context getContext() {
        Label_0034: {
            if (LibraryLoader.sAppContext != null) {
                break Label_0034;
            }
            try {
                LibraryLoader.sAppContext = (Context)Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                return LibraryLoader.sAppContext;
            }
            catch (Exception cause) {
                throw new IllegalStateException("LibraryLoader not initialized. Call LibraryLoader.initialize() before using library classes.", cause);
            }
        }
    }
    
    public static void initialize(@NonNull final Context context) {
        LibraryLoader.sAppContext = context.getApplicationContext();
    }
    
    static void loadLibrary(final Context context) {
        try {
            System.loadLibrary("pl_droidsonroids_gif");
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            Context context2 = context;
            if (context == null) {
                context2 = getContext();
            }
            ReLinker.loadLibrary(context2);
        }
    }
}

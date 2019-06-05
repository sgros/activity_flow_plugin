// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.module;

import java.util.Iterator;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import java.util.ArrayList;
import android.util.Log;
import java.util.List;
import android.content.Context;

public final class ManifestParser
{
    private final Context context;
    
    public ManifestParser(final Context context) {
        this.context = context;
    }
    
    private static GlideModule parseModule(String forName) {
        try {
            forName = (String)Class.forName(forName);
            try {
                final GlideModule instance = ((Class<GlideModule>)forName).newInstance();
                if (instance instanceof GlideModule) {
                    return instance;
                }
                forName = (String)new StringBuilder();
                ((StringBuilder)forName).append("Expected instanceof GlideModule, but found: ");
                ((StringBuilder)forName).append(instance);
                throw new RuntimeException(((StringBuilder)forName).toString());
            }
            catch (IllegalAccessException cause) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to instantiate GlideModule implementation for ");
                sb.append((Object)forName);
                throw new RuntimeException(sb.toString(), cause);
            }
            catch (InstantiationException cause2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Unable to instantiate GlideModule implementation for ");
                sb2.append((Object)forName);
                throw new RuntimeException(sb2.toString(), cause2);
            }
        }
        catch (ClassNotFoundException cause3) {
            throw new IllegalArgumentException("Unable to find GlideModule implementation", cause3);
        }
    }
    
    public List<GlideModule> parse() {
        if (Log.isLoggable("ManifestParser", 3)) {
            Log.d("ManifestParser", "Loading Glide modules");
        }
        final ArrayList<GlideModule> list = new ArrayList<GlideModule>();
        try {
            final ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
            if (applicationInfo.metaData == null) {
                if (Log.isLoggable("ManifestParser", 3)) {
                    Log.d("ManifestParser", "Got null app info metadata");
                }
                return list;
            }
            if (Log.isLoggable("ManifestParser", 2)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Got app info metadata: ");
                sb.append(applicationInfo.metaData);
                Log.v("ManifestParser", sb.toString());
            }
            for (final String str : applicationInfo.metaData.keySet()) {
                if ("GlideModule".equals(applicationInfo.metaData.get(str))) {
                    list.add(parseModule(str));
                    if (!Log.isLoggable("ManifestParser", 3)) {
                        continue;
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Loaded Glide module: ");
                    sb2.append(str);
                    Log.d("ManifestParser", sb2.toString());
                }
            }
            if (Log.isLoggable("ManifestParser", 3)) {
                Log.d("ManifestParser", "Finished loading Glide modules");
            }
            return list;
        }
        catch (PackageManager$NameNotFoundException cause) {
            throw new RuntimeException("Unable to find metadata to parse GlideModules", (Throwable)cause);
        }
    }
}

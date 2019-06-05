// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room;

import android.content.Context;

public class Room
{
    public static <T extends RoomDatabase> RoomDatabase.Builder<T> databaseBuilder(final Context context, final Class<T> clazz, final String s) {
        if (s != null && s.trim().length() != 0) {
            return (RoomDatabase.Builder<T>)new RoomDatabase.Builder(context, (Class<RoomDatabase>)clazz, s);
        }
        throw new IllegalArgumentException("Cannot build a database with null or empty name. If you are trying to create an in memory database, use Room.inMemoryDatabaseBuilder");
    }
    
    static <T, C> T getGeneratedImplementation(final Class<C> clazz, String string) {
        final String name = clazz.getPackage().getName();
        String s = clazz.getCanonicalName();
        if (!name.isEmpty()) {
            s = s.substring(name.length() + 1);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s.replace('.', '_'));
        sb.append(string);
        final String string2 = sb.toString();
        try {
            if (name.isEmpty()) {
                string = string2;
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(name);
                sb2.append(".");
                sb2.append(string2);
                string = sb2.toString();
            }
            return (T)Class.forName(string).newInstance();
        }
        catch (InstantiationException ex) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Failed to create an instance of ");
            sb3.append(clazz.getCanonicalName());
            throw new RuntimeException(sb3.toString());
        }
        catch (IllegalAccessException ex2) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Cannot access the constructor");
            sb4.append(clazz.getCanonicalName());
            throw new RuntimeException(sb4.toString());
        }
        catch (ClassNotFoundException ex3) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("cannot find implementation for ");
            sb5.append(clazz.getCanonicalName());
            sb5.append(". ");
            sb5.append(string2);
            sb5.append(" does not exist");
            throw new RuntimeException(sb5.toString());
        }
    }
    
    public static <T extends RoomDatabase> RoomDatabase.Builder<T> inMemoryDatabaseBuilder(final Context context, final Class<T> clazz) {
        return (RoomDatabase.Builder<T>)new RoomDatabase.Builder(context, (Class<RoomDatabase>)clazz, null);
    }
}

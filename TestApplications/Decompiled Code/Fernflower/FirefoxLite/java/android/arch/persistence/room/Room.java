package android.arch.persistence.room;

import android.content.Context;

public class Room {
   public static RoomDatabase.Builder databaseBuilder(Context var0, Class var1, String var2) {
      if (var2 != null && var2.trim().length() != 0) {
         return new RoomDatabase.Builder(var0, var1, var2);
      } else {
         throw new IllegalArgumentException("Cannot build a database with null or empty name. If you are trying to create an in memory database, use Room.inMemoryDatabaseBuilder");
      }
   }

   static Object getGeneratedImplementation(Class var0, String var1) {
      String var2 = var0.getPackage().getName();
      String var3 = var0.getCanonicalName();
      if (!var2.isEmpty()) {
         var3 = var3.substring(var2.length() + 1);
      }

      StringBuilder var4 = new StringBuilder();
      var4.append(var3.replace('.', '_'));
      var4.append(var1);
      var3 = var4.toString();

      StringBuilder var14;
      label50: {
         label49: {
            label48: {
               boolean var10001;
               label47: {
                  label46: {
                     try {
                        if (var2.isEmpty()) {
                           break label46;
                        }
                     } catch (ClassNotFoundException var11) {
                        var10001 = false;
                        break label50;
                     } catch (IllegalAccessException var12) {
                        var10001 = false;
                        break label49;
                     } catch (InstantiationException var13) {
                        var10001 = false;
                        break label48;
                     }

                     try {
                        var14 = new StringBuilder();
                        var14.append(var2);
                        var14.append(".");
                        var14.append(var3);
                        var1 = var14.toString();
                        break label47;
                     } catch (ClassNotFoundException var8) {
                        var10001 = false;
                        break label50;
                     } catch (IllegalAccessException var9) {
                        var10001 = false;
                        break label49;
                     } catch (InstantiationException var10) {
                        var10001 = false;
                        break label48;
                     }
                  }

                  var1 = var3;
               }

               try {
                  Object var15 = Class.forName(var1).newInstance();
                  return var15;
               } catch (ClassNotFoundException var5) {
                  var10001 = false;
                  break label50;
               } catch (IllegalAccessException var6) {
                  var10001 = false;
                  break label49;
               } catch (InstantiationException var7) {
                  var10001 = false;
               }
            }

            var14 = new StringBuilder();
            var14.append("Failed to create an instance of ");
            var14.append(var0.getCanonicalName());
            throw new RuntimeException(var14.toString());
         }

         var14 = new StringBuilder();
         var14.append("Cannot access the constructor");
         var14.append(var0.getCanonicalName());
         throw new RuntimeException(var14.toString());
      }

      var14 = new StringBuilder();
      var14.append("cannot find implementation for ");
      var14.append(var0.getCanonicalName());
      var14.append(". ");
      var14.append(var3);
      var14.append(" does not exist");
      throw new RuntimeException(var14.toString());
   }

   public static RoomDatabase.Builder inMemoryDatabaseBuilder(Context var0, Class var1) {
      return new RoomDatabase.Builder(var0, var1, (String)null);
   }
}

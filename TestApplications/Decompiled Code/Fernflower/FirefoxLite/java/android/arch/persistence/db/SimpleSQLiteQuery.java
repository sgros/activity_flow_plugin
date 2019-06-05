package android.arch.persistence.db;

public final class SimpleSQLiteQuery implements SupportSQLiteQuery {
   private final Object[] mBindArgs;
   private final String mQuery;

   public SimpleSQLiteQuery(String var1) {
      this(var1, (Object[])null);
   }

   public SimpleSQLiteQuery(String var1, Object[] var2) {
      this.mQuery = var1;
      this.mBindArgs = var2;
   }

   private static void bind(SupportSQLiteProgram var0, int var1, Object var2) {
      if (var2 == null) {
         var0.bindNull(var1);
      } else if (var2 instanceof byte[]) {
         var0.bindBlob(var1, (byte[])var2);
      } else if (var2 instanceof Float) {
         var0.bindDouble(var1, (double)(Float)var2);
      } else if (var2 instanceof Double) {
         var0.bindDouble(var1, (Double)var2);
      } else if (var2 instanceof Long) {
         var0.bindLong(var1, (Long)var2);
      } else if (var2 instanceof Integer) {
         var0.bindLong(var1, (long)(Integer)var2);
      } else if (var2 instanceof Short) {
         var0.bindLong(var1, (long)(Short)var2);
      } else if (var2 instanceof Byte) {
         var0.bindLong(var1, (long)(Byte)var2);
      } else if (var2 instanceof String) {
         var0.bindString(var1, (String)var2);
      } else {
         if (!(var2 instanceof Boolean)) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Cannot bind ");
            var5.append(var2);
            var5.append(" at index ");
            var5.append(var1);
            var5.append(" Supported types: null, byte[], float, double, long, int, short, byte,");
            var5.append(" string");
            throw new IllegalArgumentException(var5.toString());
         }

         long var3;
         if ((Boolean)var2) {
            var3 = 1L;
         } else {
            var3 = 0L;
         }

         var0.bindLong(var1, var3);
      }

   }

   public static void bind(SupportSQLiteProgram var0, Object[] var1) {
      if (var1 != null) {
         int var2 = var1.length;
         int var3 = 0;

         while(var3 < var2) {
            Object var4 = var1[var3];
            ++var3;
            bind(var0, var3, var4);
         }

      }
   }

   public void bindTo(SupportSQLiteProgram var1) {
      bind(var1, this.mBindArgs);
   }

   public String getSql() {
      return this.mQuery;
   }
}

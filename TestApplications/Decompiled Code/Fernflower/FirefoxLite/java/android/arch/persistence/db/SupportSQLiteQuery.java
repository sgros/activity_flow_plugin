package android.arch.persistence.db;

public interface SupportSQLiteQuery {
   void bindTo(SupportSQLiteProgram var1);

   String getSql();
}

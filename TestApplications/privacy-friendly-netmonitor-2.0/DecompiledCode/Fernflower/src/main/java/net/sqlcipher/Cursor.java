package net.sqlcipher;

public interface Cursor extends android.database.Cursor {
   int FIELD_TYPE_BLOB = 4;
   int FIELD_TYPE_FLOAT = 2;
   int FIELD_TYPE_INTEGER = 1;
   int FIELD_TYPE_NULL = 0;
   int FIELD_TYPE_STRING = 3;

   int getType(int var1);
}

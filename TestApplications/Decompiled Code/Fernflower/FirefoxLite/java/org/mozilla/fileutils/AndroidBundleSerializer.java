package org.mozilla.fileutils;

import android.os.Bundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AndroidBundleSerializer {
   private final Map dataTypeHandlers = new HashMap();

   public AndroidBundleSerializer() {
      this.register(new AndroidBundleSerializer.NullHandler());
      this.register(new AndroidBundleSerializer.ByteArrayHandler());
      this.register(new AndroidBundleSerializer.StringHandler());
   }

   private void register(AndroidBundleSerializer.DataTypeHandler var1) {
      String var2 = var1.getName();
      this.dataTypeHandlers.put(var2, var1);
   }

   public Bundle deserializeBundle(ObjectInputStream var1) throws IOException {
      List var6;
      try {
         var6 = (List)var1.readObject();
      } catch (ClassNotFoundException var5) {
         var5.printStackTrace();
         var6 = null;
      }

      if (var6 != null && var6.size() != 0) {
         Bundle var2 = new Bundle();
         Iterator var3 = var6.iterator();

         while(var3.hasNext()) {
            SerializedItem var4 = (SerializedItem)var3.next();
            if (var4 != null) {
               AndroidBundleSerializer.DataTypeHandler var7 = (AndroidBundleSerializer.DataTypeHandler)this.dataTypeHandlers.get(var4.getClassName());
               if (var7 != null) {
                  var7.restore(var2, var4);
               }
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public void serializeBundle(ObjectOutputStream var1, Bundle var2) throws IOException {
      if (var2 != null && var2.size() > 0) {
         ArrayList var3 = new ArrayList();
         Iterator var4 = var2.keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            Object var6 = var2.get(var5);
            String var7;
            if (var6 != null) {
               var7 = var6.getClass().getCanonicalName();
            } else {
               var7 = "null";
            }

            AndroidBundleSerializer.DataTypeHandler var8 = (AndroidBundleSerializer.DataTypeHandler)this.dataTypeHandlers.get(var7);
            if (var8 != null) {
               var3.add(var8.create(var2, var5));
            }
         }

         var1.writeObject(var3);
      }

   }

   private static class ByteArrayHandler implements AndroidBundleSerializer.DataTypeHandler {
      public ByteArrayHandler() {
      }

      public SerializedItem create(Bundle var1, String var2) throws IOException {
         byte[] var3 = var1.getByteArray(var2);
         SerializedItem var4 = new SerializedItem();
         var4.setClassName(this.getName());
         var4.setKey(var2);
         var4.setValue(var3);
         return var4;
      }

      public String getName() {
         return byte[].class.getCanonicalName();
      }

      public void restore(Bundle var1, SerializedItem var2) throws IOException {
         var1.putByteArray(var2.getKey(), var2.getValue());
      }
   }

   public interface DataTypeHandler {
      SerializedItem create(Bundle var1, String var2) throws IOException;

      String getName();

      void restore(Bundle var1, SerializedItem var2) throws IOException;
   }

   private static class NullHandler implements AndroidBundleSerializer.DataTypeHandler {
      public NullHandler() {
      }

      public SerializedItem create(Bundle var1, String var2) throws IOException {
         SerializedItem var3 = new SerializedItem();
         var3.setClassName(this.getName());
         var3.setKey(var2);
         var3.setValue((byte[])null);
         return var3;
      }

      public String getName() {
         return "null";
      }

      public void restore(Bundle var1, SerializedItem var2) throws IOException {
         var1.putByteArray(var2.getKey(), var2.getValue());
      }
   }

   private static class StringHandler implements AndroidBundleSerializer.DataTypeHandler {
      public StringHandler() {
      }

      public SerializedItem create(Bundle var1, String var2) throws IOException {
         byte[] var4;
         if (var1.getString(var2) != null) {
            var4 = var1.getString(var2).getBytes(Charset.forName("UTF-8"));
         } else {
            var4 = null;
         }

         if (var4 == null) {
            return null;
         } else {
            SerializedItem var3 = new SerializedItem();
            var3.setClassName(this.getName());
            var3.setKey(var2);
            var3.setValue(var4);
            return var3;
         }
      }

      public String getName() {
         return String.class.getCanonicalName();
      }

      public void restore(Bundle var1, SerializedItem var2) throws IOException {
         var1.putString(var2.getKey(), new String(var2.getValue(), Charset.forName("UTF-8")));
      }
   }
}

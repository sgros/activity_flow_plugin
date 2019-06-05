package androidx.versionedparcelable;

import android.os.Parcelable;
import java.lang.reflect.InvocationTargetException;

public abstract class VersionedParcel {
   private static Class findParcelClass(VersionedParcelable var0) throws ClassNotFoundException {
      return findParcelClass(var0.getClass());
   }

   private static Class findParcelClass(Class var0) throws ClassNotFoundException {
      return Class.forName(String.format("%s.%sParcelizer", var0.getPackage().getName(), var0.getSimpleName()), false, var0.getClassLoader());
   }

   protected static VersionedParcelable readFromParcel(String var0, VersionedParcel var1) {
      try {
         VersionedParcelable var6 = (VersionedParcelable)Class.forName(var0, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", VersionedParcel.class).invoke((Object)null, var1);
         return var6;
      } catch (IllegalAccessException var2) {
         throw new RuntimeException("VersionedParcel encountered IllegalAccessException", var2);
      } catch (InvocationTargetException var3) {
         if (var3.getCause() instanceof RuntimeException) {
            throw (RuntimeException)var3.getCause();
         } else {
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", var3);
         }
      } catch (NoSuchMethodException var4) {
         throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", var4);
      } catch (ClassNotFoundException var5) {
         throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", var5);
      }
   }

   protected static void writeToParcel(VersionedParcelable var0, VersionedParcel var1) {
      try {
         findParcelClass(var0).getDeclaredMethod("write", var0.getClass(), VersionedParcel.class).invoke((Object)null, var0, var1);
      } catch (IllegalAccessException var2) {
         throw new RuntimeException("VersionedParcel encountered IllegalAccessException", var2);
      } catch (InvocationTargetException var3) {
         if (var3.getCause() instanceof RuntimeException) {
            throw (RuntimeException)var3.getCause();
         } else {
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", var3);
         }
      } catch (NoSuchMethodException var4) {
         throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", var4);
      } catch (ClassNotFoundException var5) {
         throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", var5);
      }
   }

   private void writeVersionedParcelableCreator(VersionedParcelable var1) {
      Class var2;
      try {
         var2 = findParcelClass(var1.getClass());
      } catch (ClassNotFoundException var4) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getClass().getSimpleName());
         var3.append(" does not have a Parcelizer");
         throw new RuntimeException(var3.toString(), var4);
      }

      this.writeString(var2.getName());
   }

   protected abstract void closeField();

   protected abstract VersionedParcel createSubParcel();

   public boolean isStream() {
      return false;
   }

   protected abstract byte[] readByteArray();

   public byte[] readByteArray(byte[] var1, int var2) {
      return !this.readField(var2) ? var1 : this.readByteArray();
   }

   protected abstract boolean readField(int var1);

   protected abstract int readInt();

   public int readInt(int var1, int var2) {
      return !this.readField(var2) ? var1 : this.readInt();
   }

   protected abstract Parcelable readParcelable();

   public Parcelable readParcelable(Parcelable var1, int var2) {
      return !this.readField(var2) ? var1 : this.readParcelable();
   }

   protected abstract String readString();

   public String readString(String var1, int var2) {
      return !this.readField(var2) ? var1 : this.readString();
   }

   protected VersionedParcelable readVersionedParcelable() {
      String var1 = this.readString();
      return var1 == null ? null : readFromParcel(var1, this.createSubParcel());
   }

   public VersionedParcelable readVersionedParcelable(VersionedParcelable var1, int var2) {
      return !this.readField(var2) ? var1 : this.readVersionedParcelable();
   }

   protected abstract void setOutputField(int var1);

   public void setSerializationFlags(boolean var1, boolean var2) {
   }

   protected abstract void writeByteArray(byte[] var1);

   public void writeByteArray(byte[] var1, int var2) {
      this.setOutputField(var2);
      this.writeByteArray(var1);
   }

   protected abstract void writeInt(int var1);

   public void writeInt(int var1, int var2) {
      this.setOutputField(var2);
      this.writeInt(var1);
   }

   protected abstract void writeParcelable(Parcelable var1);

   public void writeParcelable(Parcelable var1, int var2) {
      this.setOutputField(var2);
      this.writeParcelable(var1);
   }

   protected abstract void writeString(String var1);

   public void writeString(String var1, int var2) {
      this.setOutputField(var2);
      this.writeString(var1);
   }

   protected void writeVersionedParcelable(VersionedParcelable var1) {
      if (var1 == null) {
         this.writeString((String)null);
      } else {
         this.writeVersionedParcelableCreator(var1);
         VersionedParcel var2 = this.createSubParcel();
         writeToParcel(var1, var2);
         var2.closeField();
      }
   }

   public void writeVersionedParcelable(VersionedParcelable var1, int var2) {
      this.setOutputField(var2);
      this.writeVersionedParcelable(var1);
   }
}

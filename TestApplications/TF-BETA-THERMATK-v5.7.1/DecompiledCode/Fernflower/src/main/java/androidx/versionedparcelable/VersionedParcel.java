package androidx.versionedparcelable;

import android.os.Parcelable;
import androidx.collection.ArrayMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class VersionedParcel {
   protected final ArrayMap mParcelizerCache;
   protected final ArrayMap mReadCache;
   protected final ArrayMap mWriteCache;

   public VersionedParcel(ArrayMap var1, ArrayMap var2, ArrayMap var3) {
      this.mReadCache = var1;
      this.mWriteCache = var2;
      this.mParcelizerCache = var3;
   }

   private Class findParcelClass(Class var1) throws ClassNotFoundException {
      Class var2 = (Class)this.mParcelizerCache.get(var1.getName());
      Class var3 = var2;
      if (var2 == null) {
         var3 = Class.forName(String.format("%s.%sParcelizer", var1.getPackage().getName(), var1.getSimpleName()), false, var1.getClassLoader());
         this.mParcelizerCache.put(var1.getName(), var3);
      }

      return var3;
   }

   private Method getReadMethod(String var1) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
      Method var2 = (Method)this.mReadCache.get(var1);
      Method var3 = var2;
      if (var2 == null) {
         System.currentTimeMillis();
         var3 = Class.forName(var1, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", VersionedParcel.class);
         this.mReadCache.put(var1, var3);
      }

      return var3;
   }

   private Method getWriteMethod(Class var1) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
      Method var2 = (Method)this.mWriteCache.get(var1.getName());
      Method var3 = var2;
      if (var2 == null) {
         Class var4 = this.findParcelClass(var1);
         System.currentTimeMillis();
         var3 = var4.getDeclaredMethod("write", var1, VersionedParcel.class);
         this.mWriteCache.put(var1.getName(), var3);
      }

      return var3;
   }

   private void writeVersionedParcelableCreator(VersionedParcelable var1) {
      Class var2;
      try {
         var2 = this.findParcelClass(var1.getClass());
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

   protected abstract boolean readBoolean();

   public boolean readBoolean(boolean var1, int var2) {
      return !this.readField(var2) ? var1 : this.readBoolean();
   }

   protected abstract byte[] readByteArray();

   public byte[] readByteArray(byte[] var1, int var2) {
      return !this.readField(var2) ? var1 : this.readByteArray();
   }

   protected abstract CharSequence readCharSequence();

   public CharSequence readCharSequence(CharSequence var1, int var2) {
      return !this.readField(var2) ? var1 : this.readCharSequence();
   }

   protected abstract boolean readField(int var1);

   protected VersionedParcelable readFromParcel(String var1, VersionedParcel var2) {
      try {
         VersionedParcelable var7 = (VersionedParcelable)this.getReadMethod(var1).invoke((Object)null, var2);
         return var7;
      } catch (IllegalAccessException var3) {
         throw new RuntimeException("VersionedParcel encountered IllegalAccessException", var3);
      } catch (InvocationTargetException var4) {
         if (var4.getCause() instanceof RuntimeException) {
            throw (RuntimeException)var4.getCause();
         } else {
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", var4);
         }
      } catch (NoSuchMethodException var5) {
         throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", var5);
      } catch (ClassNotFoundException var6) {
         throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", var6);
      }
   }

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
      return var1 == null ? null : this.readFromParcel(var1, this.createSubParcel());
   }

   public VersionedParcelable readVersionedParcelable(VersionedParcelable var1, int var2) {
      return !this.readField(var2) ? var1 : this.readVersionedParcelable();
   }

   protected abstract void setOutputField(int var1);

   public void setSerializationFlags(boolean var1, boolean var2) {
   }

   protected abstract void writeBoolean(boolean var1);

   public void writeBoolean(boolean var1, int var2) {
      this.setOutputField(var2);
      this.writeBoolean(var1);
   }

   protected abstract void writeByteArray(byte[] var1);

   public void writeByteArray(byte[] var1, int var2) {
      this.setOutputField(var2);
      this.writeByteArray(var1);
   }

   protected abstract void writeCharSequence(CharSequence var1);

   public void writeCharSequence(CharSequence var1, int var2) {
      this.setOutputField(var2);
      this.writeCharSequence(var1);
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

   protected void writeToParcel(VersionedParcelable var1, VersionedParcel var2) {
      try {
         this.getWriteMethod(var1.getClass()).invoke((Object)null, var1, var2);
      } catch (IllegalAccessException var3) {
         throw new RuntimeException("VersionedParcel encountered IllegalAccessException", var3);
      } catch (InvocationTargetException var4) {
         if (var4.getCause() instanceof RuntimeException) {
            throw (RuntimeException)var4.getCause();
         } else {
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", var4);
         }
      } catch (NoSuchMethodException var5) {
         throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", var5);
      } catch (ClassNotFoundException var6) {
         throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", var6);
      }
   }

   protected void writeVersionedParcelable(VersionedParcelable var1) {
      if (var1 == null) {
         this.writeString((String)null);
      } else {
         this.writeVersionedParcelableCreator(var1);
         VersionedParcel var2 = this.createSubParcel();
         this.writeToParcel(var1, var2);
         var2.closeField();
      }
   }

   public void writeVersionedParcelable(VersionedParcelable var1, int var2) {
      this.setOutputField(var2);
      this.writeVersionedParcelable(var1);
   }
}

package org.telegram.tgnet;

public abstract class AbstractSerializedData {
   public abstract int getPosition();

   public abstract int length();

   public abstract boolean readBool(boolean var1);

   public abstract byte[] readByteArray(boolean var1);

   public abstract NativeByteBuffer readByteBuffer(boolean var1);

   public abstract void readBytes(byte[] var1, boolean var2);

   public abstract byte[] readData(int var1, boolean var2);

   public abstract double readDouble(boolean var1);

   public abstract int readInt32(boolean var1);

   public abstract long readInt64(boolean var1);

   public abstract String readString(boolean var1);

   public abstract int remaining();

   public abstract void skip(int var1);

   public abstract void writeBool(boolean var1);

   public abstract void writeByte(byte var1);

   public abstract void writeByte(int var1);

   public abstract void writeByteArray(byte[] var1);

   public abstract void writeByteArray(byte[] var1, int var2, int var3);

   public abstract void writeByteBuffer(NativeByteBuffer var1);

   public abstract void writeBytes(byte[] var1);

   public abstract void writeBytes(byte[] var1, int var2, int var3);

   public abstract void writeDouble(double var1);

   public abstract void writeInt32(int var1);

   public abstract void writeInt64(long var1);

   public abstract void writeString(String var1);
}

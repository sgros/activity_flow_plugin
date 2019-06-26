// 
// Decompiled by Procyon v0.5.34
// 

package androidx.versionedparcelable;

import android.os.Parcelable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import androidx.collection.ArrayMap;

public abstract class VersionedParcel
{
    protected final ArrayMap<String, Class> mParcelizerCache;
    protected final ArrayMap<String, Method> mReadCache;
    protected final ArrayMap<String, Method> mWriteCache;
    
    public VersionedParcel(final ArrayMap<String, Method> mReadCache, final ArrayMap<String, Method> mWriteCache, final ArrayMap<String, Class> mParcelizerCache) {
        this.mReadCache = mReadCache;
        this.mWriteCache = mWriteCache;
        this.mParcelizerCache = mParcelizerCache;
    }
    
    private Class findParcelClass(final Class<? extends VersionedParcelable> clazz) throws ClassNotFoundException {
        Class<?> forName;
        if ((forName = this.mParcelizerCache.get(clazz.getName())) == null) {
            forName = Class.forName(String.format("%s.%sParcelizer", clazz.getPackage().getName(), clazz.getSimpleName()), false, clazz.getClassLoader());
            this.mParcelizerCache.put(clazz.getName(), forName);
        }
        return forName;
    }
    
    private Method getReadMethod(final String name) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        Method declaredMethod;
        if ((declaredMethod = this.mReadCache.get(name)) == null) {
            System.currentTimeMillis();
            declaredMethod = Class.forName(name, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", VersionedParcel.class);
            this.mReadCache.put(name, declaredMethod);
        }
        return declaredMethod;
    }
    
    private Method getWriteMethod(final Class clazz) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        Method declaredMethod;
        if ((declaredMethod = this.mWriteCache.get(clazz.getName())) == null) {
            final Class parcelClass = this.findParcelClass(clazz);
            System.currentTimeMillis();
            declaredMethod = parcelClass.getDeclaredMethod("write", clazz, VersionedParcel.class);
            this.mWriteCache.put(clazz.getName(), declaredMethod);
        }
        return declaredMethod;
    }
    
    private void writeVersionedParcelableCreator(final VersionedParcelable versionedParcelable) {
        try {
            this.writeString(this.findParcelClass(versionedParcelable.getClass()).getName());
        }
        catch (ClassNotFoundException cause) {
            final StringBuilder sb = new StringBuilder();
            sb.append(versionedParcelable.getClass().getSimpleName());
            sb.append(" does not have a Parcelizer");
            throw new RuntimeException(sb.toString(), cause);
        }
    }
    
    protected abstract void closeField();
    
    protected abstract VersionedParcel createSubParcel();
    
    public boolean isStream() {
        return false;
    }
    
    protected abstract boolean readBoolean();
    
    public boolean readBoolean(final boolean b, final int n) {
        if (!this.readField(n)) {
            return b;
        }
        return this.readBoolean();
    }
    
    protected abstract byte[] readByteArray();
    
    public byte[] readByteArray(final byte[] array, final int n) {
        if (!this.readField(n)) {
            return array;
        }
        return this.readByteArray();
    }
    
    protected abstract CharSequence readCharSequence();
    
    public CharSequence readCharSequence(final CharSequence charSequence, final int n) {
        if (!this.readField(n)) {
            return charSequence;
        }
        return this.readCharSequence();
    }
    
    protected abstract boolean readField(final int p0);
    
    protected <T extends VersionedParcelable> T readFromParcel(final String s, final VersionedParcel versionedParcel) {
        try {
            return (T)this.getReadMethod(s).invoke(null, versionedParcel);
        }
        catch (ClassNotFoundException cause) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", cause);
        }
        catch (NoSuchMethodException cause2) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", cause2);
        }
        catch (InvocationTargetException cause3) {
            if (cause3.getCause() instanceof RuntimeException) {
                throw (RuntimeException)cause3.getCause();
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", cause3);
        }
        catch (IllegalAccessException cause4) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", cause4);
        }
    }
    
    protected abstract int readInt();
    
    public int readInt(final int n, final int n2) {
        if (!this.readField(n2)) {
            return n;
        }
        return this.readInt();
    }
    
    protected abstract <T extends Parcelable> T readParcelable();
    
    public <T extends Parcelable> T readParcelable(final T t, final int n) {
        if (!this.readField(n)) {
            return t;
        }
        return this.readParcelable();
    }
    
    protected abstract String readString();
    
    public String readString(final String s, final int n) {
        if (!this.readField(n)) {
            return s;
        }
        return this.readString();
    }
    
    protected <T extends VersionedParcelable> T readVersionedParcelable() {
        final String string = this.readString();
        if (string == null) {
            return null;
        }
        return (T)this.readFromParcel(string, this.createSubParcel());
    }
    
    public <T extends VersionedParcelable> T readVersionedParcelable(final T t, final int n) {
        if (!this.readField(n)) {
            return t;
        }
        return this.readVersionedParcelable();
    }
    
    protected abstract void setOutputField(final int p0);
    
    public void setSerializationFlags(final boolean b, final boolean b2) {
    }
    
    protected abstract void writeBoolean(final boolean p0);
    
    public void writeBoolean(final boolean b, final int outputField) {
        this.setOutputField(outputField);
        this.writeBoolean(b);
    }
    
    protected abstract void writeByteArray(final byte[] p0);
    
    public void writeByteArray(final byte[] array, final int outputField) {
        this.setOutputField(outputField);
        this.writeByteArray(array);
    }
    
    protected abstract void writeCharSequence(final CharSequence p0);
    
    public void writeCharSequence(final CharSequence charSequence, final int outputField) {
        this.setOutputField(outputField);
        this.writeCharSequence(charSequence);
    }
    
    protected abstract void writeInt(final int p0);
    
    public void writeInt(final int n, final int outputField) {
        this.setOutputField(outputField);
        this.writeInt(n);
    }
    
    protected abstract void writeParcelable(final Parcelable p0);
    
    public void writeParcelable(final Parcelable parcelable, final int outputField) {
        this.setOutputField(outputField);
        this.writeParcelable(parcelable);
    }
    
    protected abstract void writeString(final String p0);
    
    public void writeString(final String s, final int outputField) {
        this.setOutputField(outputField);
        this.writeString(s);
    }
    
    protected <T extends VersionedParcelable> void writeToParcel(final T t, final VersionedParcel versionedParcel) {
        try {
            this.getWriteMethod(t.getClass()).invoke(null, t, versionedParcel);
        }
        catch (ClassNotFoundException cause) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", cause);
        }
        catch (NoSuchMethodException cause2) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", cause2);
        }
        catch (InvocationTargetException cause3) {
            if (cause3.getCause() instanceof RuntimeException) {
                throw (RuntimeException)cause3.getCause();
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", cause3);
        }
        catch (IllegalAccessException cause4) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", cause4);
        }
    }
    
    protected void writeVersionedParcelable(final VersionedParcelable versionedParcelable) {
        if (versionedParcelable == null) {
            this.writeString(null);
            return;
        }
        this.writeVersionedParcelableCreator(versionedParcelable);
        final VersionedParcel subParcel = this.createSubParcel();
        this.writeToParcel(versionedParcelable, subParcel);
        subParcel.closeField();
    }
    
    public void writeVersionedParcelable(final VersionedParcelable versionedParcelable, final int outputField) {
        this.setOutputField(outputField);
        this.writeVersionedParcelable(versionedParcelable);
    }
}

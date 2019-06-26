// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.io.DataInputStream;
import locus.api.utils.Logger;
import locus.api.utils.DataWriterBigEndian;
import java.util.List;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;

public abstract class Storable
{
    private static final String TAG = "Storable";
    
    public Storable() {
        this.reset();
    }
    
    public Storable(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this();
        this.read(dataReaderBigEndian);
    }
    
    public Storable(final byte[] array) throws IOException {
        this(new DataReaderBigEndian(array));
    }
    
    public static byte[] getAsBytes(List<? extends Storable> byteArray) {
        try {
            final DataWriterBigEndian dataWriterBigEndian = new DataWriterBigEndian();
            dataWriterBigEndian.writeListStorable((List<? extends Storable>)byteArray);
            byteArray = dataWriterBigEndian.toByteArray();
            return (byte[])byteArray;
        }
        catch (Exception ex) {
            Logger.logE("Storable", "getAsBytes(" + byteArray + ")", ex);
            byteArray = null;
            return (byte[])byteArray;
        }
    }
    
    public static Storable read(final Class<? extends Storable> clazz, final DataReaderBigEndian dataReaderBigEndian) throws IOException, InstantiationException, IllegalAccessException {
        final BodyContainer header = readHeader(dataReaderBigEndian);
        final Storable storable = (Storable)clazz.newInstance();
        storable.readObject(header.version, new DataReaderBigEndian(header.data));
        return storable;
    }
    
    private static BodyContainer readHeader(final DataInputStream dataInputStream) throws IOException {
        final BodyContainer bodyContainer = new BodyContainer();
        bodyContainer.version = dataInputStream.readInt();
        final int int1 = dataInputStream.readInt();
        if (int1 < 0 || int1 > 10485760) {
            throw new IOException("item size too big, size:" + int1 + ", max: 10MB");
        }
        dataInputStream.read(bodyContainer.data = new byte[int1]);
        return bodyContainer;
    }
    
    private static BodyContainer readHeader(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        final BodyContainer bodyContainer = new BodyContainer();
        bodyContainer.version = dataReaderBigEndian.readInt();
        final int int1 = dataReaderBigEndian.readInt();
        if (int1 < 0 || int1 > 20971520) {
            throw new IOException("item size too big, size:" + int1 + ", max: 20MB");
        }
        bodyContainer.data = dataReaderBigEndian.readBytes(int1);
        return bodyContainer;
    }
    
    public static List<? extends Storable> readList(final Class<? extends Storable> clazz, final DataInputStream dataInputStream) throws IOException {
        final ArrayList<Storable> list = new ArrayList<Storable>();
        final int int1 = dataInputStream.readInt();
        if (int1 != 0) {
            int i = 0;
            while (i < int1) {
                while (true) {
                    try {
                        final Storable storable = (Storable)clazz.newInstance();
                        storable.read(dataInputStream);
                        list.add(storable);
                        ++i;
                    }
                    catch (InstantiationException ex) {
                        Logger.logE("Storable", "readList(" + clazz + ", " + dataInputStream + ")", ex);
                        continue;
                    }
                    catch (IllegalAccessException ex2) {
                        Logger.logE("Storable", "readList(" + clazz + ", " + dataInputStream + ")", ex2);
                        continue;
                    }
                    break;
                }
            }
        }
        return list;
    }
    
    public static List<? extends Storable> readList(final Class<? extends Storable> clazz, final byte[] array) throws IOException {
        return new DataReaderBigEndian(array).readListStorable(clazz);
    }
    
    public static void readUnknownObject(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        readHeader(dataReaderBigEndian);
    }
    
    public static void writeList(final List<? extends Storable> list, final DataOutputStream dataOutputStream) throws IOException {
        int size;
        if (list == null) {
            size = 0;
        }
        else {
            size = list.size();
        }
        dataOutputStream.writeInt(size);
        if (size != 0) {
            for (int i = 0; i < list.size(); ++i) {
                dataOutputStream.write(((Storable)list.get(i)).getAsBytes());
            }
        }
    }
    
    public byte[] getAsBytes() {
        try {
            final DataWriterBigEndian dataWriterBigEndian = new DataWriterBigEndian();
            this.write(dataWriterBigEndian);
            return dataWriterBigEndian.toByteArray();
        }
        catch (IOException ex) {
            Logger.logE("Storable", "getAsBytes()", ex);
            return null;
        }
    }
    
    public Storable getCopy() throws IOException, InstantiationException, IllegalAccessException {
        return read(this.getClass(), new DataReaderBigEndian(this.getAsBytes()));
    }
    
    protected abstract int getVersion();
    
    public void read(final DataInputStream dataInputStream) throws IOException {
        final BodyContainer header = readHeader(dataInputStream);
        this.readObject(header.version, new DataReaderBigEndian(header.data));
    }
    
    public void read(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        final BodyContainer header = readHeader(dataReaderBigEndian);
        this.readObject(header.version, new DataReaderBigEndian(header.data));
    }
    
    public void read(final byte[] array) throws IOException {
        this.read(new DataReaderBigEndian(array));
    }
    
    protected abstract void readObject(final int p0, final DataReaderBigEndian p1) throws IOException;
    
    public abstract void reset();
    
    public void write(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeInt(this.getVersion());
        dataWriterBigEndian.writeInt(0);
        final int size = dataWriterBigEndian.size();
        this.writeObject(dataWriterBigEndian);
        final int n = dataWriterBigEndian.size() - size;
        if (n > 0) {
            dataWriterBigEndian.storePosition();
            dataWriterBigEndian.moveTo(size - 4);
            dataWriterBigEndian.writeInt(n);
            dataWriterBigEndian.restorePosition();
        }
    }
    
    protected abstract void writeObject(final DataWriterBigEndian p0) throws IOException;
    
    private static class BodyContainer
    {
        byte[] data;
        int version;
    }
}

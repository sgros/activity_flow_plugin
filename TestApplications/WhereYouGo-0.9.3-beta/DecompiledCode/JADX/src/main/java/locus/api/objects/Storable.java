package locus.api.objects;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public abstract class Storable {
    private static final String TAG = "Storable";

    private static class BodyContainer {
        byte[] data;
        int version;

        private BodyContainer() {
        }
    }

    public abstract int getVersion();

    public abstract void readObject(int i, DataReaderBigEndian dataReaderBigEndian) throws IOException;

    public abstract void reset();

    public abstract void writeObject(DataWriterBigEndian dataWriterBigEndian) throws IOException;

    public Storable() {
        reset();
    }

    public Storable(byte[] data) throws IOException {
        this(new DataReaderBigEndian(data));
    }

    public Storable(DataReaderBigEndian dr) throws IOException {
        this();
        read(dr);
    }

    public void read(byte[] data) throws IOException {
        read(new DataReaderBigEndian(data));
    }

    public void read(DataReaderBigEndian dr) throws IOException {
        BodyContainer bc = readHeader(dr);
        readObject(bc.version, new DataReaderBigEndian(bc.data));
    }

    private static BodyContainer readHeader(DataReaderBigEndian dr) throws IOException {
        BodyContainer bc = new BodyContainer();
        bc.version = dr.readInt();
        int size = dr.readInt();
        if (size < 0 || size > 20971520) {
            throw new IOException("item size too big, size:" + size + ", max: 20MB");
        }
        bc.data = dr.readBytes(size);
        return bc;
    }

    public void read(DataInputStream input) throws IOException {
        BodyContainer bc = readHeader(input);
        readObject(bc.version, new DataReaderBigEndian(bc.data));
    }

    private static BodyContainer readHeader(DataInputStream dis) throws IOException {
        BodyContainer bc = new BodyContainer();
        bc.version = dis.readInt();
        int size = dis.readInt();
        if (size < 0 || size > 10485760) {
            throw new IOException("item size too big, size:" + size + ", max: 10MB");
        }
        bc.data = new byte[size];
        dis.read(bc.data);
        return bc;
    }

    public void write(DataWriterBigEndian dw) throws IOException {
        dw.writeInt(getVersion());
        dw.writeInt(0);
        int startSize = dw.size();
        writeObject(dw);
        int totalSize = dw.size() - startSize;
        if (totalSize > 0) {
            dw.storePosition();
            dw.moveTo(startSize - 4);
            dw.writeInt(totalSize);
            dw.restorePosition();
        }
    }

    public Storable getCopy() throws IOException, InstantiationException, IllegalAccessException {
        return read(getClass(), new DataReaderBigEndian(getAsBytes()));
    }

    public byte[] getAsBytes() {
        try {
            DataWriterBigEndian dw = new DataWriterBigEndian();
            write(dw);
            return dw.toByteArray();
        } catch (IOException e) {
            Logger.logE(TAG, "getAsBytes()", e);
            return null;
        }
    }

    public static Storable read(Class<? extends Storable> claz, DataReaderBigEndian dr) throws IOException, InstantiationException, IllegalAccessException {
        BodyContainer bc = readHeader(dr);
        Storable storable = (Storable) claz.newInstance();
        storable.readObject(bc.version, new DataReaderBigEndian(bc.data));
        return storable;
    }

    public static void readUnknownObject(DataReaderBigEndian dr) throws IOException {
        readHeader(dr);
    }

    public static List<? extends Storable> readList(Class<? extends Storable> claz, byte[] data) throws IOException {
        return new DataReaderBigEndian(data).readListStorable(claz);
    }

    public static List<? extends Storable> readList(Class<? extends Storable> claz, DataInputStream dis) throws IOException {
        List<Storable> objs = new ArrayList();
        int count = dis.readInt();
        if (count != 0) {
            for (int i = 0; i < count; i++) {
                try {
                    Storable item = (Storable) claz.newInstance();
                    item.read(dis);
                    objs.add(item);
                } catch (InstantiationException e) {
                    Logger.logE(TAG, "readList(" + claz + ", " + dis + ")", e);
                } catch (IllegalAccessException e2) {
                    Logger.logE(TAG, "readList(" + claz + ", " + dis + ")", e2);
                }
            }
        }
        return objs;
    }

    public static byte[] getAsBytes(List<? extends Storable> data) {
        try {
            DataWriterBigEndian dw = new DataWriterBigEndian();
            dw.writeListStorable(data);
            return dw.toByteArray();
        } catch (Exception e) {
            Logger.logE(TAG, "getAsBytes(" + data + ")", e);
            return null;
        }
    }

    public static void writeList(List<? extends Storable> objs, DataOutputStream dos) throws IOException {
        int size;
        if (objs == null) {
            size = 0;
        } else {
            size = objs.size();
        }
        dos.writeInt(size);
        if (size != 0) {
            int n = objs.size();
            for (int i = 0; i < n; i++) {
                dos.write(((Storable) objs.get(i)).getAsBytes());
            }
        }
    }
}

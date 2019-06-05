package org.mozilla.fileutils;

import android.os.Bundle;
import com.adjust.sdk.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidBundleSerializer {
    private final Map<String, DataTypeHandler> dataTypeHandlers = new HashMap();

    public interface DataTypeHandler {
        SerializedItem create(Bundle bundle, String str) throws IOException;

        String getName();

        void restore(Bundle bundle, SerializedItem serializedItem) throws IOException;
    }

    private static class ByteArrayHandler implements DataTypeHandler {
        public String getName() {
            return byte[].class.getCanonicalName();
        }

        public SerializedItem create(Bundle bundle, String str) throws IOException {
            byte[] byteArray = bundle.getByteArray(str);
            SerializedItem serializedItem = new SerializedItem();
            serializedItem.setClassName(getName());
            serializedItem.setKey(str);
            serializedItem.setValue(byteArray);
            return serializedItem;
        }

        public void restore(Bundle bundle, SerializedItem serializedItem) throws IOException {
            bundle.putByteArray(serializedItem.getKey(), serializedItem.getValue());
        }
    }

    private static class NullHandler implements DataTypeHandler {
        public String getName() {
            return "null";
        }

        public SerializedItem create(Bundle bundle, String str) throws IOException {
            SerializedItem serializedItem = new SerializedItem();
            serializedItem.setClassName(getName());
            serializedItem.setKey(str);
            serializedItem.setValue(null);
            return serializedItem;
        }

        public void restore(Bundle bundle, SerializedItem serializedItem) throws IOException {
            bundle.putByteArray(serializedItem.getKey(), serializedItem.getValue());
        }
    }

    private static class StringHandler implements DataTypeHandler {
        public String getName() {
            return String.class.getCanonicalName();
        }

        public SerializedItem create(Bundle bundle, String str) throws IOException {
            byte[] bytes = bundle.getString(str) != null ? bundle.getString(str).getBytes(Charset.forName(Constants.ENCODING)) : null;
            if (bytes == null) {
                return null;
            }
            SerializedItem serializedItem = new SerializedItem();
            serializedItem.setClassName(getName());
            serializedItem.setKey(str);
            serializedItem.setValue(bytes);
            return serializedItem;
        }

        public void restore(Bundle bundle, SerializedItem serializedItem) throws IOException {
            bundle.putString(serializedItem.getKey(), new String(serializedItem.getValue(), Charset.forName(Constants.ENCODING)));
        }
    }

    public AndroidBundleSerializer() {
        register(new NullHandler());
        register(new ByteArrayHandler());
        register(new StringHandler());
    }

    private void register(DataTypeHandler dataTypeHandler) {
        this.dataTypeHandlers.put(dataTypeHandler.getName(), dataTypeHandler);
    }

    public void serializeBundle(ObjectOutputStream objectOutputStream, Bundle bundle) throws IOException {
        if (bundle != null && bundle.size() > 0) {
            ArrayList arrayList = new ArrayList();
            for (String str : bundle.keySet()) {
                Object obj = bundle.get(str);
                DataTypeHandler dataTypeHandler = (DataTypeHandler) this.dataTypeHandlers.get(obj != null ? obj.getClass().getCanonicalName() : "null");
                if (dataTypeHandler != null) {
                    arrayList.add(dataTypeHandler.create(bundle, str));
                }
            }
            objectOutputStream.writeObject(arrayList);
        }
    }

    public Bundle deserializeBundle(ObjectInputStream objectInputStream) throws IOException {
        List list;
        try {
            list = (List) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            list = null;
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        Bundle bundle = new Bundle();
        for (SerializedItem serializedItem : list) {
            if (serializedItem != null) {
                DataTypeHandler dataTypeHandler = (DataTypeHandler) this.dataTypeHandlers.get(serializedItem.getClassName());
                if (dataTypeHandler != null) {
                    dataTypeHandler.restore(bundle, serializedItem);
                }
            }
        }
        return bundle;
    }
}

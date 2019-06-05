// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.fileutils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import android.os.Bundle;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class AndroidBundleSerializer
{
    private final Map<String, DataTypeHandler> dataTypeHandlers;
    
    public AndroidBundleSerializer() {
        this.dataTypeHandlers = new HashMap<String, DataTypeHandler>();
        this.register((DataTypeHandler)new NullHandler());
        this.register((DataTypeHandler)new ByteArrayHandler());
        this.register((DataTypeHandler)new StringHandler());
    }
    
    private void register(final DataTypeHandler dataTypeHandler) {
        this.dataTypeHandlers.put(dataTypeHandler.getName(), dataTypeHandler);
    }
    
    public Bundle deserializeBundle(final ObjectInputStream objectInputStream) throws IOException {
        List<SerializedItem> list;
        try {
            list = (List<SerializedItem>)objectInputStream.readObject();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            list = null;
        }
        if (list != null && list.size() != 0) {
            final Bundle bundle = new Bundle();
            for (final SerializedItem serializedItem : list) {
                if (serializedItem != null) {
                    final DataTypeHandler dataTypeHandler = this.dataTypeHandlers.get(serializedItem.getClassName());
                    if (dataTypeHandler == null) {
                        continue;
                    }
                    dataTypeHandler.restore(bundle, serializedItem);
                }
            }
            return bundle;
        }
        return null;
    }
    
    public void serializeBundle(final ObjectOutputStream objectOutputStream, final Bundle bundle) throws IOException {
        if (bundle != null && bundle.size() > 0) {
            final ArrayList<SerializedItem> obj = new ArrayList<SerializedItem>();
            for (final String s : bundle.keySet()) {
                final Object value = bundle.get(s);
                String canonicalName;
                if (value != null) {
                    canonicalName = value.getClass().getCanonicalName();
                }
                else {
                    canonicalName = "null";
                }
                final DataTypeHandler dataTypeHandler = this.dataTypeHandlers.get(canonicalName);
                if (dataTypeHandler != null) {
                    obj.add(dataTypeHandler.create(bundle, s));
                }
            }
            objectOutputStream.writeObject(obj);
        }
    }
    
    private static class ByteArrayHandler implements DataTypeHandler
    {
        public ByteArrayHandler() {
        }
        
        @Override
        public SerializedItem create(final Bundle bundle, final String key) throws IOException {
            final byte[] byteArray = bundle.getByteArray(key);
            final SerializedItem serializedItem = new SerializedItem();
            serializedItem.setClassName(this.getName());
            serializedItem.setKey(key);
            serializedItem.setValue(byteArray);
            return serializedItem;
        }
        
        @Override
        public String getName() {
            return byte[].class.getCanonicalName();
        }
        
        @Override
        public void restore(final Bundle bundle, final SerializedItem serializedItem) throws IOException {
            bundle.putByteArray(serializedItem.getKey(), serializedItem.getValue());
        }
    }
    
    public interface DataTypeHandler
    {
        SerializedItem create(final Bundle p0, final String p1) throws IOException;
        
        String getName();
        
        void restore(final Bundle p0, final SerializedItem p1) throws IOException;
    }
    
    private static class NullHandler implements DataTypeHandler
    {
        public NullHandler() {
        }
        
        @Override
        public SerializedItem create(final Bundle bundle, final String key) throws IOException {
            final SerializedItem serializedItem = new SerializedItem();
            serializedItem.setClassName(this.getName());
            serializedItem.setKey(key);
            serializedItem.setValue(null);
            return serializedItem;
        }
        
        @Override
        public String getName() {
            return "null";
        }
        
        @Override
        public void restore(final Bundle bundle, final SerializedItem serializedItem) throws IOException {
            bundle.putByteArray(serializedItem.getKey(), serializedItem.getValue());
        }
    }
    
    private static class StringHandler implements DataTypeHandler
    {
        public StringHandler() {
        }
        
        @Override
        public SerializedItem create(final Bundle bundle, final String key) throws IOException {
            byte[] bytes;
            if (bundle.getString(key) != null) {
                bytes = bundle.getString(key).getBytes(Charset.forName("UTF-8"));
            }
            else {
                bytes = null;
            }
            if (bytes == null) {
                return null;
            }
            final SerializedItem serializedItem = new SerializedItem();
            serializedItem.setClassName(this.getName());
            serializedItem.setKey(key);
            serializedItem.setValue(bytes);
            return serializedItem;
        }
        
        @Override
        public String getName() {
            return String.class.getCanonicalName();
        }
        
        @Override
        public void restore(final Bundle bundle, final SerializedItem serializedItem) throws IOException {
            bundle.putString(serializedItem.getKey(), new String(serializedItem.getValue(), Charset.forName("UTF-8")));
        }
    }
}

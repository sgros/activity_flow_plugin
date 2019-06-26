package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectDescriptorFactory {
    protected static Map<Integer, Map<Integer, Class<? extends BaseDescriptor>>> descriptorRegistry = new HashMap();
    protected static Logger log = Logger.getLogger(ObjectDescriptorFactory.class.getName());

    static {
        HashSet<Class> hashSet = new HashSet();
        hashSet.add(DecoderSpecificInfo.class);
        hashSet.add(SLConfigDescriptor.class);
        hashSet.add(BaseDescriptor.class);
        hashSet.add(ExtensionDescriptor.class);
        hashSet.add(ObjectDescriptorBase.class);
        hashSet.add(ProfileLevelIndicationDescriptor.class);
        hashSet.add(AudioSpecificConfig.class);
        hashSet.add(ExtensionProfileLevelDescriptor.class);
        hashSet.add(ESDescriptor.class);
        hashSet.add(DecoderConfigDescriptor.class);
        for (Class cls : hashSet) {
            Descriptor descriptor = (Descriptor) cls.getAnnotation(Descriptor.class);
            int[] tags = descriptor.tags();
            int objectTypeIndication = descriptor.objectTypeIndication();
            Map map = (Map) descriptorRegistry.get(Integer.valueOf(objectTypeIndication));
            if (map == null) {
                map = new HashMap();
            }
            for (int valueOf : tags) {
                map.put(Integer.valueOf(valueOf), cls);
            }
            descriptorRegistry.put(Integer.valueOf(objectTypeIndication), map);
        }
    }

    public static BaseDescriptor createFrom(int i, ByteBuffer byteBuffer) throws IOException {
        BaseDescriptor unknownDescriptor;
        int readUInt8 = IsoTypeReader.readUInt8(byteBuffer);
        Map map = (Map) descriptorRegistry.get(Integer.valueOf(i));
        if (map == null) {
            map = (Map) descriptorRegistry.get(Integer.valueOf(-1));
        }
        Class cls = (Class) map.get(Integer.valueOf(readUInt8));
        String str = " and tag ";
        Logger logger;
        if (cls == null || cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
            logger = log;
            StringBuilder stringBuilder = new StringBuilder("No ObjectDescriptor found for objectTypeIndication ");
            stringBuilder.append(Integer.toHexString(i));
            stringBuilder.append(str);
            stringBuilder.append(Integer.toHexString(readUInt8));
            stringBuilder.append(" found: ");
            stringBuilder.append(cls);
            logger.warning(stringBuilder.toString());
            unknownDescriptor = new UnknownDescriptor();
        } else {
            try {
                unknownDescriptor = (BaseDescriptor) cls.newInstance();
            } catch (Exception e) {
                logger = log;
                Level level = Level.SEVERE;
                StringBuilder stringBuilder2 = new StringBuilder("Couldn't instantiate BaseDescriptor class ");
                stringBuilder2.append(cls);
                stringBuilder2.append(" for objectTypeIndication ");
                stringBuilder2.append(i);
                stringBuilder2.append(str);
                stringBuilder2.append(readUInt8);
                logger.log(level, stringBuilder2.toString(), e);
                throw new RuntimeException(e);
            }
        }
        unknownDescriptor.parse(readUInt8, byteBuffer);
        return unknownDescriptor;
    }
}

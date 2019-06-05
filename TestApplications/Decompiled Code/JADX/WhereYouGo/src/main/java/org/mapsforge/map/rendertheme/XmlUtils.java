package org.mapsforge.map.rendertheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.mapsforge.map.graphics.Bitmap;
import org.xml.sax.SAXException;

public final class XmlUtils {
    private static final String PREFIX_FILE = "file:";
    private static final String PREFIX_JAR = "jar:";

    public static void checkMandatoryAttribute(String elementName, String attributeName, Object attributeValue) throws SAXException {
        if (attributeValue == null) {
            throw new SAXException("missing attribute '" + attributeName + "' for element: " + elementName);
        }
    }

    public static Bitmap createBitmap(GraphicAdapter graphicAdapter, String relativePathPrefix, String src) throws IOException {
        if (src == null || src.length() == 0) {
            return null;
        }
        InputStream inputStream = createInputStream(relativePathPrefix, src);
        Bitmap bitmap = graphicAdapter.decodeStream(inputStream);
        inputStream.close();
        return bitmap;
    }

    public static SAXException createSAXException(String element, String name, String value, int attributeIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unknown attribute (");
        stringBuilder.append(attributeIndex);
        stringBuilder.append(") in element '");
        stringBuilder.append(element);
        stringBuilder.append("': ");
        stringBuilder.append(name);
        stringBuilder.append('=');
        stringBuilder.append(value);
        return new SAXException(stringBuilder.toString());
    }

    public static byte parseNonNegativeByte(String name, String value) throws SAXException {
        byte parsedByte = Byte.parseByte(value);
        checkForNegativeValue(name, (float) parsedByte);
        return parsedByte;
    }

    public static float parseNonNegativeFloat(String name, String value) throws SAXException {
        float parsedFloat = Float.parseFloat(value);
        checkForNegativeValue(name, parsedFloat);
        return parsedFloat;
    }

    public static int parseNonNegativeInteger(String name, String value) throws SAXException {
        int parsedInt = Integer.parseInt(value);
        checkForNegativeValue(name, (float) parsedInt);
        return parsedInt;
    }

    private static void checkForNegativeValue(String name, float value) throws SAXException {
        if (value < 0.0f) {
            throw new SAXException("Attribute '" + name + "' must not be negative: " + value);
        }
    }

    private static InputStream createInputStream(String relativePathPrefix, String src) throws FileNotFoundException {
        if (src.startsWith(PREFIX_JAR)) {
            String absoluteName = getAbsoluteName(relativePathPrefix, src.substring(PREFIX_JAR.length()));
            InputStream resourceAsStream = XmlUtils.class.getResourceAsStream(absoluteName);
            if (resourceAsStream != null) {
                return resourceAsStream;
            }
            throw new FileNotFoundException("resource not found: " + absoluteName);
        } else if (src.startsWith(PREFIX_FILE)) {
            File file = getFile(relativePathPrefix, src.substring(PREFIX_FILE.length()));
            if (!file.exists()) {
                throw new FileNotFoundException("file does not exist: " + file.getAbsolutePath());
            } else if (!file.isFile()) {
                throw new FileNotFoundException("not a file: " + file.getAbsolutePath());
            } else if (file.canRead()) {
                return new FileInputStream(file);
            } else {
                throw new FileNotFoundException("cannot read file: " + file.getAbsolutePath());
            }
        } else {
            throw new FileNotFoundException("invalid bitmap source: " + src);
        }
    }

    private static String getAbsoluteName(String relativePathPrefix, String name) {
        return name.charAt(0) == '/' ? name : relativePathPrefix + name;
    }

    private static File getFile(String parentPath, String pathName) {
        if (pathName.charAt(0) == File.separatorChar) {
            return new File(pathName);
        }
        return new File(parentPath, pathName);
    }

    private XmlUtils() {
        throw new IllegalStateException();
    }
}

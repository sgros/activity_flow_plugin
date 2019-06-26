// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.ParserException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.TrackOutput;

final class ScriptTagPayloadReader extends TagPayloadReader
{
    private long durationUs;
    
    public ScriptTagPayloadReader() {
        super(null);
        this.durationUs = -9223372036854775807L;
    }
    
    private static Boolean readAmfBoolean(final ParsableByteArray parsableByteArray) {
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        boolean b = true;
        if (unsignedByte != 1) {
            b = false;
        }
        return b;
    }
    
    private static Object readAmfData(final ParsableByteArray parsableByteArray, final int n) {
        if (n == 0) {
            return readAmfDouble(parsableByteArray);
        }
        if (n == 1) {
            return readAmfBoolean(parsableByteArray);
        }
        if (n == 2) {
            return readAmfString(parsableByteArray);
        }
        if (n == 3) {
            return readAmfObject(parsableByteArray);
        }
        if (n == 8) {
            return readAmfEcmaArray(parsableByteArray);
        }
        if (n == 10) {
            return readAmfStrictArray(parsableByteArray);
        }
        if (n != 11) {
            return null;
        }
        return readAmfDate(parsableByteArray);
    }
    
    private static Date readAmfDate(final ParsableByteArray parsableByteArray) {
        final Date date = new Date((long)(double)readAmfDouble(parsableByteArray));
        parsableByteArray.skipBytes(2);
        return date;
    }
    
    private static Double readAmfDouble(final ParsableByteArray parsableByteArray) {
        return Double.longBitsToDouble(parsableByteArray.readLong());
    }
    
    private static HashMap<String, Object> readAmfEcmaArray(final ParsableByteArray parsableByteArray) {
        final int unsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        final HashMap hashMap = new HashMap<String, Object>(unsignedIntToInt);
        for (int i = 0; i < unsignedIntToInt; ++i) {
            hashMap.put(readAmfString(parsableByteArray), readAmfData(parsableByteArray, readAmfType(parsableByteArray)));
        }
        return (HashMap<String, Object>)hashMap;
    }
    
    private static HashMap<String, Object> readAmfObject(final ParsableByteArray parsableByteArray) {
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        while (true) {
            final String amfString = readAmfString(parsableByteArray);
            final int amfType = readAmfType(parsableByteArray);
            if (amfType == 9) {
                break;
            }
            hashMap.put(amfString, readAmfData(parsableByteArray, amfType));
        }
        return hashMap;
    }
    
    private static ArrayList<Object> readAmfStrictArray(final ParsableByteArray parsableByteArray) {
        final int unsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        final ArrayList list = new ArrayList<Object>(unsignedIntToInt);
        for (int i = 0; i < unsignedIntToInt; ++i) {
            list.add(readAmfData(parsableByteArray, readAmfType(parsableByteArray)));
        }
        return (ArrayList<Object>)list;
    }
    
    private static String readAmfString(final ParsableByteArray parsableByteArray) {
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        final int position = parsableByteArray.getPosition();
        parsableByteArray.skipBytes(unsignedShort);
        return new String(parsableByteArray.data, position, unsignedShort);
    }
    
    private static int readAmfType(final ParsableByteArray parsableByteArray) {
        return parsableByteArray.readUnsignedByte();
    }
    
    public long getDurationUs() {
        return this.durationUs;
    }
    
    @Override
    protected boolean parseHeader(final ParsableByteArray parsableByteArray) {
        return true;
    }
    
    @Override
    protected void parsePayload(final ParsableByteArray parsableByteArray, final long n) throws ParserException {
        if (readAmfType(parsableByteArray) != 2) {
            throw new ParserException();
        }
        if (!"onMetaData".equals(readAmfString(parsableByteArray))) {
            return;
        }
        if (readAmfType(parsableByteArray) != 8) {
            return;
        }
        final HashMap<String, Object> amfEcmaArray = readAmfEcmaArray(parsableByteArray);
        if (amfEcmaArray.containsKey("duration")) {
            final double doubleValue = amfEcmaArray.get("duration");
            if (doubleValue > 0.0) {
                this.durationUs = (long)(doubleValue * 1000000.0);
            }
        }
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;
import java.util.UUID;

public final class PsshAtomUtil
{
    public static byte[] buildPsshAtom(final UUID uuid, final byte[] array) {
        return buildPsshAtom(uuid, null, array);
    }
    
    public static byte[] buildPsshAtom(UUID uuid, final UUID[] array, final byte[] src) {
        final int n = 0;
        int length;
        if (src != null) {
            length = src.length;
        }
        else {
            length = 0;
        }
        int capacity;
        final int n2 = capacity = length + 32;
        if (array != null) {
            capacity = n2 + (array.length * 16 + 4);
        }
        final ByteBuffer allocate = ByteBuffer.allocate(capacity);
        allocate.putInt(capacity);
        allocate.putInt(Atom.TYPE_pssh);
        int n3;
        if (array != null) {
            n3 = 16777216;
        }
        else {
            n3 = 0;
        }
        allocate.putInt(n3);
        allocate.putLong(uuid.getMostSignificantBits());
        allocate.putLong(uuid.getLeastSignificantBits());
        if (array != null) {
            allocate.putInt(array.length);
            for (int length2 = array.length, i = n; i < length2; ++i) {
                uuid = array[i];
                allocate.putLong(uuid.getMostSignificantBits());
                allocate.putLong(uuid.getLeastSignificantBits());
            }
        }
        if (src != null && src.length != 0) {
            allocate.putInt(src.length);
            allocate.put(src);
        }
        return allocate.array();
    }
    
    private static PsshAtom parsePsshAtom(byte[] array) {
        final ParsableByteArray parsableByteArray = new ParsableByteArray(array);
        if (parsableByteArray.limit() < 32) {
            return null;
        }
        parsableByteArray.setPosition(0);
        if (parsableByteArray.readInt() != parsableByteArray.bytesLeft() + 4) {
            return null;
        }
        if (parsableByteArray.readInt() != Atom.TYPE_pssh) {
            return null;
        }
        final int fullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        if (fullAtomVersion > 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported pssh version: ");
            sb.append(fullAtomVersion);
            Log.w("PsshAtomUtil", sb.toString());
            return null;
        }
        final UUID uuid = new UUID(parsableByteArray.readLong(), parsableByteArray.readLong());
        if (fullAtomVersion == 1) {
            parsableByteArray.skipBytes(parsableByteArray.readUnsignedIntToInt() * 16);
        }
        final int unsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        if (unsignedIntToInt != parsableByteArray.bytesLeft()) {
            return null;
        }
        array = new byte[unsignedIntToInt];
        parsableByteArray.readBytes(array, 0, unsignedIntToInt);
        return new PsshAtom(uuid, fullAtomVersion, array);
    }
    
    public static UUID parseUuid(final byte[] array) {
        final PsshAtom psshAtom = parsePsshAtom(array);
        if (psshAtom == null) {
            return null;
        }
        return psshAtom.uuid;
    }
    
    private static class PsshAtom
    {
        private final byte[] schemeData;
        private final UUID uuid;
        private final int version;
        
        public PsshAtom(final UUID uuid, final int version, final byte[] schemeData) {
            this.uuid = uuid;
            this.version = version;
            this.schemeData = schemeData;
        }
    }
}

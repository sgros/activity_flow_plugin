// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig.formats;

import cz.matejcik.openwig.platform.FileHandle;
import java.io.IOException;
import cz.matejcik.openwig.platform.SeekableFile;

public class CartridgeFile
{
    private static final int CACHE_LIMIT = 128000;
    private static final byte[] CART_ID;
    public String author;
    public String code;
    public String description;
    public String device;
    public String filename;
    private int files;
    public int iconId;
    private int[] ids;
    private byte[] lastFile;
    private int lastId;
    public double latitude;
    public double longitude;
    public String member;
    public String name;
    private int[] offsets;
    protected Savegame savegame;
    private SeekableFile source;
    public int splashId;
    public String startdesc;
    public String type;
    public String url;
    public String version;
    
    static {
        CART_ID = new byte[] { 2, 10, 67, 65, 82, 84, 0 };
    }
    
    protected CartridgeFile() {
        this.lastId = -1;
        this.lastFile = null;
    }
    
    private boolean fileOk() throws IOException {
        final byte[] array = new byte[CartridgeFile.CART_ID.length];
        this.source.seek(0L);
        this.source.readFully(array);
        for (int i = 0; i < array.length; ++i) {
            if (array[i] != CartridgeFile.CART_ID[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static CartridgeFile read(final SeekableFile source, final FileHandle fileHandle) throws IOException {
        final CartridgeFile cartridgeFile = new CartridgeFile();
        cartridgeFile.source = source;
        if (!cartridgeFile.fileOk()) {
            throw new IOException("invalid cartridge file");
        }
        cartridgeFile.scanOffsets();
        cartridgeFile.scanHeader();
        cartridgeFile.savegame = new Savegame(fileHandle);
        return cartridgeFile;
    }
    
    private void scanHeader() throws IOException {
        this.source.readInt();
        this.latitude = this.source.readDouble();
        this.longitude = this.source.readDouble();
        this.source.skip(8L);
        this.source.skip(8L);
        this.splashId = this.source.readShort();
        this.iconId = this.source.readShort();
        this.type = this.source.readString();
        this.member = this.source.readString();
        this.source.skip(8L);
        this.name = this.source.readString();
        this.source.readString();
        this.description = this.source.readString();
        this.startdesc = this.source.readString();
        this.version = this.source.readString();
        this.author = this.source.readString();
        this.url = this.source.readString();
        this.device = this.source.readString();
        this.source.skip(4L);
        this.code = this.source.readString();
    }
    
    private void scanOffsets() throws IOException {
        this.files = this.source.readShort();
        this.offsets = new int[this.files];
        this.ids = new int[this.files];
        for (int i = 0; i < this.files; ++i) {
            this.ids[i] = this.source.readShort();
            this.offsets[i] = this.source.readInt();
        }
    }
    
    public byte[] getBytecode() throws IOException {
        this.source.seek(this.offsets[0]);
        final byte[] array = new byte[this.source.readInt()];
        this.source.readFully(array);
        return array;
    }
    
    public byte[] getFile(final int lastId) throws IOException {
        byte[] lastFile;
        if (lastId == this.lastId) {
            lastFile = this.lastFile;
        }
        else if (lastId < 1) {
            lastFile = null;
        }
        else {
            final int n = -1;
            int n2 = 0;
            int n3;
            while (true) {
                n3 = n;
                if (n2 >= this.ids.length) {
                    break;
                }
                if (this.ids[n2] == lastId) {
                    n3 = n2;
                    break;
                }
                ++n2;
            }
            if (n3 == -1) {
                lastFile = null;
            }
            else {
                this.source.seek(this.offsets[n3]);
                if (this.source.read() < 1) {
                    lastFile = null;
                }
                else {
                    this.source.readInt();
                    final int int1 = this.source.readInt();
                    this.lastFile = null;
                    this.lastId = -1;
                    try {
                        final byte[] lastFile2 = new byte[int1];
                        this.source.readFully(lastFile2);
                        lastFile = lastFile2;
                        if (int1 < 128000) {
                            this.lastId = lastId;
                            this.lastFile = lastFile2;
                            lastFile = lastFile2;
                        }
                    }
                    catch (OutOfMemoryError outOfMemoryError) {
                        lastFile = null;
                    }
                }
            }
        }
        return lastFile;
    }
    
    public Savegame getSavegame() throws IOException {
        return this.savegame;
    }
}

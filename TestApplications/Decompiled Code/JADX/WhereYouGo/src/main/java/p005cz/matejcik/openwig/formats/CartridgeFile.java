package p005cz.matejcik.openwig.formats;

import java.io.IOException;
import p005cz.matejcik.openwig.platform.FileHandle;
import p005cz.matejcik.openwig.platform.SeekableFile;

/* renamed from: cz.matejcik.openwig.formats.CartridgeFile */
public class CartridgeFile {
    private static final int CACHE_LIMIT = 128000;
    private static final byte[] CART_ID = new byte[]{(byte) 2, (byte) 10, (byte) 67, (byte) 65, (byte) 82, (byte) 84, (byte) 0};
    public String author;
    public String code;
    public String description;
    public String device;
    public String filename;
    private int files;
    public int iconId;
    private int[] ids;
    private byte[] lastFile = null;
    private int lastId = -1;
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

    protected CartridgeFile() {
    }

    private boolean fileOk() throws IOException {
        byte[] buf = new byte[CART_ID.length];
        this.source.seek(0);
        this.source.readFully(buf);
        for (int i = 0; i < buf.length; i++) {
            if (buf[i] != CART_ID[i]) {
                return false;
            }
        }
        return true;
    }

    public static CartridgeFile read(SeekableFile source, FileHandle savefile) throws IOException {
        CartridgeFile cf = new CartridgeFile();
        cf.source = source;
        if (cf.fileOk()) {
            cf.scanOffsets();
            cf.scanHeader();
            cf.savegame = new Savegame(savefile);
            return cf;
        }
        throw new IOException("invalid cartridge file");
    }

    private void scanOffsets() throws IOException {
        this.files = this.source.readShort();
        this.offsets = new int[this.files];
        this.ids = new int[this.files];
        for (int i = 0; i < this.files; i++) {
            this.ids[i] = this.source.readShort();
            this.offsets[i] = this.source.readInt();
        }
    }

    private void scanHeader() throws IOException {
        this.source.readInt();
        this.latitude = this.source.readDouble();
        this.longitude = this.source.readDouble();
        this.source.skip(8);
        this.source.skip(8);
        this.splashId = this.source.readShort();
        this.iconId = this.source.readShort();
        this.type = this.source.readString();
        this.member = this.source.readString();
        this.source.skip(8);
        this.name = this.source.readString();
        this.source.readString();
        this.description = this.source.readString();
        this.startdesc = this.source.readString();
        this.version = this.source.readString();
        this.author = this.source.readString();
        this.url = this.source.readString();
        this.device = this.source.readString();
        this.source.skip(4);
        this.code = this.source.readString();
    }

    public byte[] getBytecode() throws IOException {
        this.source.seek((long) this.offsets[0]);
        byte[] ffile = new byte[this.source.readInt()];
        this.source.readFully(ffile);
        return ffile;
    }

    public byte[] getFile(int oid) throws IOException {
        if (oid == this.lastId) {
            return this.lastFile;
        }
        if (oid < 1) {
            return null;
        }
        int id = -1;
        for (int i = 0; i < this.ids.length; i++) {
            if (this.ids[i] == oid) {
                id = i;
                break;
            }
        }
        if (id == -1) {
            return null;
        }
        this.source.seek((long) this.offsets[id]);
        if (this.source.read() < 1) {
            return null;
        }
        int ttype = this.source.readInt();
        int len = this.source.readInt();
        this.lastFile = null;
        this.lastId = -1;
        try {
            byte[] ffile = new byte[len];
            this.source.readFully(ffile);
            if (len >= CACHE_LIMIT) {
                return ffile;
            }
            this.lastId = oid;
            this.lastFile = ffile;
            return ffile;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public Savegame getSavegame() throws IOException {
        return this.savegame;
    }
}

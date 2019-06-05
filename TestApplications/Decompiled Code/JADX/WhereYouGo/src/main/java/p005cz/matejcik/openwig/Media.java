package p005cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import p009se.krka.kahlua.p010vm.LuaTable;

/* renamed from: cz.matejcik.openwig.Media */
public class Media extends EventTable {
    private static int media_no;
    public String altText = null;
    /* renamed from: id */
    public int f98id;
    public String type = null;

    public static void reset() {
        media_no = 1;
    }

    public Media() {
        int i = media_no;
        media_no = i + 1;
        this.f98id = i;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(this.f98id);
        super.serialize(out);
    }

    public void deserialize(DataInputStream in) throws IOException {
        media_no--;
        this.f98id = in.readInt();
        if (this.f98id >= media_no) {
            media_no = this.f98id + 1;
        }
        super.deserialize(in);
    }

    /* Access modifiers changed, original: protected */
    public void setItem(String key, Object value) {
        if ("AltText".equals(key)) {
            this.altText = (String) value;
        } else if ("Resources".equals(key)) {
            LuaTable lt = (LuaTable) value;
            int n = lt.len();
            for (int i = 1; i <= n; i++) {
                String t = (String) ((LuaTable) lt.rawget(new Double((double) i))).rawget("Type");
                if (!"fdl".equals(t)) {
                    this.type = t.toLowerCase();
                }
            }
        } else {
            super.setItem(key, value);
        }
    }

    public String jarFilename() {
        return String.valueOf(this.f98id) + "." + (this.type == null ? "" : this.type);
    }

    public void play() {
        String mime = null;
        try {
            if ("wav".equals(this.type)) {
                mime = "audio/x-wav";
            } else if ("mp3".equals(this.type)) {
                mime = "audio/mpeg";
            }
            Engine.f39ui.playSound(Engine.mediaFile(this), mime);
        } catch (IOException e) {
        }
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import se.krka.kahlua.vm.LuaTable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class Media extends EventTable
{
    private static int media_no;
    public String altText;
    public int id;
    public String type;
    
    public Media() {
        this.altText = null;
        this.type = null;
        final int media_no = Media.media_no;
        Media.media_no = media_no + 1;
        this.id = media_no;
    }
    
    public static void reset() {
        Media.media_no = 1;
    }
    
    @Override
    public void deserialize(final DataInputStream dataInputStream) throws IOException {
        --Media.media_no;
        this.id = dataInputStream.readInt();
        if (this.id >= Media.media_no) {
            Media.media_no = this.id + 1;
        }
        super.deserialize(dataInputStream);
    }
    
    public String jarFilename() {
        final StringBuilder append = new StringBuilder().append(String.valueOf(this.id)).append(".");
        String type;
        if (this.type == null) {
            type = "";
        }
        else {
            type = this.type;
        }
        return append.append(type).toString();
    }
    
    public void play() {
        String s = null;
        try {
            if ("wav".equals(this.type)) {
                s = "audio/x-wav";
            }
            else if ("mp3".equals(this.type)) {
                s = "audio/mpeg";
            }
            Engine.ui.playSound(Engine.mediaFile(this), s);
        }
        catch (IOException ex) {}
    }
    
    @Override
    public void serialize(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.id);
        super.serialize(dataOutputStream);
    }
    
    @Override
    protected void setItem(final String s, final Object o) {
        if ("AltText".equals(s)) {
            this.altText = (String)o;
        }
        else if ("Resources".equals(s)) {
            final LuaTable luaTable = (LuaTable)o;
            for (int len = luaTable.len(), i = 1; i <= len; ++i) {
                final String anObject = (String)((LuaTable)luaTable.rawget(new Double(i))).rawget("Type");
                if (!"fdl".equals(anObject)) {
                    this.type = anObject.toLowerCase();
                }
            }
        }
        else {
            super.setItem(s, o);
        }
    }
}

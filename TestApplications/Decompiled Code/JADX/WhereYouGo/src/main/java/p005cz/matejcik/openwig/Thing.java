package p005cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;
import p009se.krka.kahlua.stdlib.BaseLib;

/* renamed from: cz.matejcik.openwig.Thing */
public class Thing extends Container {
    public Vector actions = new Vector();
    private boolean character = false;

    /* Access modifiers changed, original: protected */
    public String luaTostring() {
        return this.character ? "a ZCharacter instance" : "a ZItem instance";
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeBoolean(this.character);
        super.serialize(out);
    }

    public void deserialize(DataInputStream in) throws IOException {
        this.character = in.readBoolean();
        super.deserialize(in);
    }

    public Thing(boolean character) {
        this.character = character;
        this.table.rawset("Commands", new LuaTableImpl());
    }

    /* Access modifiers changed, original: protected */
    public void setItem(String key, Object value) {
        if ("Commands".equals(key)) {
            for (int i = 0; i < this.actions.size(); i++) {
                ((Action) this.actions.elementAt(i)).dissociateFromTargets();
            }
            this.actions.removeAllElements();
            LuaTable lt = (LuaTable) value;
            Object i2 = null;
            while (true) {
                i2 = lt.next(i2);
                if (i2 != null) {
                    Action a = (Action) lt.rawget(i2);
                    if (i2 instanceof Double) {
                        a.name = BaseLib.numberToString((Double) i2);
                    } else {
                        a.name = i2.toString();
                    }
                    a.setActor(this);
                    this.actions.addElement(a);
                    a.associateWithTargets();
                } else {
                    return;
                }
            }
        }
        super.setItem(key, value);
    }

    public int visibleActions() {
        int count = 0;
        for (int i = 0; i < this.actions.size(); i++) {
            Action c = (Action) this.actions.elementAt(i);
            if (c.isEnabled() && (c.getActor() == this || c.getActor().visibleToPlayer())) {
                count++;
            }
        }
        return count;
    }

    public boolean isItem() {
        return !this.character;
    }

    public boolean isCharacter() {
        return this.character;
    }
}

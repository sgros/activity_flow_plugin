// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import se.krka.kahlua.stdlib.BaseLib;
import se.krka.kahlua.vm.LuaTable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import se.krka.kahlua.vm.LuaTableImpl;
import java.util.Vector;

public class Thing extends Container
{
    public Vector actions;
    private boolean character;
    
    public Thing() {
        this.character = false;
        this.actions = new Vector();
    }
    
    public Thing(final boolean character) {
        this.character = false;
        this.actions = new Vector();
        this.character = character;
        this.table.rawset("Commands", new LuaTableImpl());
    }
    
    @Override
    public void deserialize(final DataInputStream dataInputStream) throws IOException {
        this.character = dataInputStream.readBoolean();
        super.deserialize(dataInputStream);
    }
    
    public boolean isCharacter() {
        return this.character;
    }
    
    public boolean isItem() {
        return !this.character;
    }
    
    @Override
    protected String luaTostring() {
        String s;
        if (this.character) {
            s = "a ZCharacter instance";
        }
        else {
            s = "a ZItem instance";
        }
        return s;
    }
    
    @Override
    public void serialize(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeBoolean(this.character);
        super.serialize(dataOutputStream);
    }
    
    @Override
    protected void setItem(final String anObject, final Object o) {
        if ("Commands".equals(anObject)) {
            for (int i = 0; i < this.actions.size(); ++i) {
                ((Action)this.actions.elementAt(i)).dissociateFromTargets();
            }
            this.actions.removeAllElements();
            final LuaTable luaTable = (LuaTable)o;
            Object next = null;
            while (true) {
                next = luaTable.next(next);
                if (next == null) {
                    break;
                }
                final Action obj = (Action)luaTable.rawget(next);
                if (next instanceof Double) {
                    obj.name = BaseLib.numberToString((Double)next);
                }
                else {
                    obj.name = next.toString();
                }
                obj.setActor(this);
                this.actions.addElement(obj);
                obj.associateWithTargets();
            }
        }
        else {
            super.setItem(anObject, o);
        }
    }
    
    public int visibleActions() {
        int n = 0;
        int n2;
        for (int i = 0; i < this.actions.size(); ++i, n = n2) {
            final Action action = this.actions.elementAt(i);
            if (!action.isEnabled()) {
                n2 = n;
            }
            else {
                if (action.getActor() != this) {
                    n2 = n;
                    if (!action.getActor().visibleToPlayer()) {
                        continue;
                    }
                }
                n2 = n + 1;
            }
        }
        return n;
    }
}

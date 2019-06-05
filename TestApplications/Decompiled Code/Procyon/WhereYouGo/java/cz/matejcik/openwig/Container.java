// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import se.krka.kahlua.stdlib.TableLib;
import java.io.IOException;
import java.io.DataInputStream;
import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.JavaFunction;

public class Container extends EventTable
{
    private static JavaFunction contains;
    private static JavaFunction moveTo;
    public Container container;
    public LuaTable inventory;
    
    static {
        Container.moveTo = new JavaFunction() {
            @Override
            public int call(final LuaCallFrame luaCallFrame, final int n) {
                ((Container)luaCallFrame.get(0)).moveTo((Container)luaCallFrame.get(1));
                return 0;
            }
        };
        Container.contains = new JavaFunction() {
            @Override
            public int call(final LuaCallFrame luaCallFrame, final int n) {
                luaCallFrame.push(LuaState.toBoolean(((Container)luaCallFrame.get(0)).contains((Thing)luaCallFrame.get(1))));
                return 1;
            }
        };
    }
    
    public Container() {
        this.inventory = new LuaTableImpl();
        this.container = null;
        this.table.rawset("MoveTo", Container.moveTo);
        this.table.rawset("Contains", Container.contains);
        this.table.rawset("Inventory", this.inventory);
        this.table.rawset("Container", this.container);
    }
    
    public static void register() {
        Engine.instance.savegame.addJavafunc(Container.moveTo);
        Engine.instance.savegame.addJavafunc(Container.contains);
    }
    
    public boolean contains(final Thing thing) {
        boolean b = true;
        Object o = null;
        while (true) {
            final Object next = this.inventory.next(o);
            if (next == null) {
                return false;
            }
            final Object rawget = this.inventory.rawget(next);
            o = next;
            if (!(rawget instanceof Thing)) {
                continue;
            }
            if (rawget == thing) {
                break;
            }
            o = next;
            if (((Thing)rawget).contains(thing)) {
                break;
            }
        }
        return b;
        b = false;
        return b;
    }
    
    @Override
    public void deserialize(final DataInputStream dataInputStream) throws IOException {
        super.deserialize(dataInputStream);
        this.inventory = (LuaTable)this.table.rawget("Inventory");
        final Object rawget = this.table.rawget("Container");
        if (rawget instanceof Container) {
            this.container = (Container)rawget;
        }
        else {
            this.container = null;
        }
    }
    
    public void moveTo(final Container container) {
        String name;
        if (container == null) {
            name = "(nowhere)";
        }
        else {
            name = container.name;
        }
        Engine.log("MOVE: " + this.name + " to " + name, 1);
        if (this.container != null) {
            TableLib.removeItem(this.container.inventory, this);
        }
        if (container != null) {
            TableLib.rawappend(container.inventory, this);
            if (container == Engine.instance.player) {
                this.setPosition(null);
            }
            else if (this.position != null) {
                this.setPosition(container.position);
            }
            else if (this.container == Engine.instance.player) {
                this.setPosition(ZonePoint.copy(Engine.instance.player.position));
            }
            this.container = container;
        }
        else {
            this.container = null;
            this.rawset("ObjectLocation", null);
        }
        this.table.rawset("Container", this.container);
    }
    
    @Override
    public Object rawget(Object anObject) {
        if ("Container".equals(anObject)) {
            anObject = this.container;
        }
        else {
            anObject = super.rawget(anObject);
        }
        return anObject;
    }
    
    public boolean visibleToPlayer() {
        boolean showThings = false;
        if (this.isVisible()) {
            if (this.container == Engine.instance.player) {
                showThings = true;
            }
            else if (this.container instanceof Zone) {
                showThings = ((Zone)this.container).showThings();
            }
        }
        return showThings;
    }
}

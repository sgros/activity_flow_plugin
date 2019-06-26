// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import java.util.Vector;

public class Action extends EventTable
{
    private Thing actor;
    private boolean enabled;
    public String notarget;
    private boolean parameter;
    private boolean reciprocal;
    private Vector targets;
    public String text;
    private boolean universal;
    
    public Action() {
        this.reciprocal = true;
        this.actor = null;
        this.targets = new Vector();
    }
    
    public Action(final LuaTable table) {
        this.reciprocal = true;
        this.actor = null;
        this.targets = new Vector();
        this.table = table;
        Object o = null;
        while (true) {
            final Object next = table.next(o);
            if (next == null) {
                break;
            }
            o = next;
            if (!(next instanceof String)) {
                continue;
            }
            this.setItem((String)next, table.rawget(next));
            o = next;
        }
    }
    
    public void associateWithTargets() {
        if (this.hasParameter()) {
            if (this.isReciprocal()) {
                for (int i = 0; i < this.targets.size(); ++i) {
                    final Thing thing = this.targets.elementAt(i);
                    if (!thing.actions.contains(this)) {
                        thing.actions.addElement(this);
                    }
                }
            }
            if (this.isUniversal() && !Engine.instance.cartridge.universalActions.contains(this)) {
                Engine.instance.cartridge.universalActions.addElement(this);
            }
        }
    }
    
    public void dissociateFromTargets() {
        if (this.hasParameter()) {
            if (this.isReciprocal()) {
                for (int i = 0; i < this.targets.size(); ++i) {
                    ((Thing)this.targets.elementAt(i)).actions.removeElement(this);
                }
            }
            if (this.isUniversal()) {
                Engine.instance.cartridge.universalActions.removeElement(this);
            }
        }
    }
    
    public Thing getActor() {
        return this.actor;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Vector getTargets() {
        return this.targets;
    }
    
    public boolean hasParameter() {
        return this.parameter;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public boolean isReciprocal() {
        return this.reciprocal;
    }
    
    public boolean isTarget(final Thing o) {
        return this.targets.contains(o) || this.isUniversal();
    }
    
    public boolean isUniversal() {
        return this.universal;
    }
    
    @Override
    protected String luaTostring() {
        return "a ZCommand instance";
    }
    
    public void setActor(final Thing actor) {
        this.actor = actor;
    }
    
    @Override
    protected void setItem(String string, final Object o) {
        if ("Text".equals(string)) {
            this.text = (String)o;
        }
        else if ("CmdWith".equals(string)) {
            final boolean boolEval = LuaState.boolEval(o);
            if (boolEval != this.parameter) {
                if (boolEval) {
                    this.parameter = true;
                    this.associateWithTargets();
                }
                else {
                    this.dissociateFromTargets();
                    this.parameter = false;
                }
            }
        }
        else if ("Enabled".equals(string)) {
            this.enabled = LuaState.boolEval(o);
        }
        else if ("WorksWithAll".equals(string)) {
            this.dissociateFromTargets();
            this.universal = LuaState.boolEval(o);
            this.associateWithTargets();
        }
        else if ("WorksWithList".equals(string)) {
            this.dissociateFromTargets();
            final LuaTable luaTable = (LuaTable)o;
            Object next = null;
            while (true) {
                next = luaTable.next(next);
                if (next == null) {
                    break;
                }
                this.targets.addElement(luaTable.rawget(next));
            }
            this.associateWithTargets();
        }
        else if ("MakeReciprocal".equals(string)) {
            this.dissociateFromTargets();
            this.reciprocal = LuaState.boolEval(o);
            this.associateWithTargets();
        }
        else if ("EmptyTargetListText".equals(string)) {
            if (o == null) {
                string = "(not available now)";
            }
            else {
                string = o.toString();
            }
            this.notarget = string;
        }
    }
    
    public int targetsInside(final LuaTable luaTable) {
        int n = 0;
        Object o = null;
        while (true) {
            final Object next = luaTable.next(o);
            if (next == null) {
                break;
            }
            final Object rawget = luaTable.rawget(next);
            o = next;
            if (!(rawget instanceof Thing)) {
                continue;
            }
            final Thing o2 = (Thing)rawget;
            o = next;
            if (!o2.isVisible()) {
                continue;
            }
            if (!this.targets.contains(o2)) {
                o = next;
                if (!this.isUniversal()) {
                    continue;
                }
            }
            ++n;
            o = next;
        }
        return n;
    }
    
    public int visibleTargets(final Container container) {
        int n = 0;
        Object o = null;
        while (true) {
            final Object next = container.inventory.next(o);
            if (next == null) {
                break;
            }
            final Object rawget = container.inventory.rawget(next);
            o = next;
            if (!(rawget instanceof Thing)) {
                continue;
            }
            final Thing o2 = (Thing)rawget;
            o = next;
            if (!o2.isVisible()) {
                continue;
            }
            if (!this.targets.contains(o2)) {
                o = next;
                if (!this.isUniversal()) {
                    continue;
                }
            }
            ++n;
            o = next;
        }
        return n;
    }
}

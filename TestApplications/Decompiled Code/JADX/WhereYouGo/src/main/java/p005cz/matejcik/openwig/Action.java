package p005cz.matejcik.openwig;

import java.util.Vector;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;

/* renamed from: cz.matejcik.openwig.Action */
public class Action extends EventTable {
    private Thing actor = null;
    private boolean enabled;
    public String notarget;
    private boolean parameter;
    private boolean reciprocal = true;
    private Vector targets = new Vector();
    public String text;
    private boolean universal;

    public Action(LuaTable table) {
        this.table = table;
        Object o = null;
        while (true) {
            o = table.next(o);
            if (o == null) {
                return;
            }
            if (o instanceof String) {
                setItem((String) o, table.rawget(o));
            }
        }
    }

    public void associateWithTargets() {
        if (hasParameter()) {
            if (isReciprocal()) {
                for (int j = 0; j < this.targets.size(); j++) {
                    Thing t = (Thing) this.targets.elementAt(j);
                    if (!t.actions.contains(this)) {
                        t.actions.addElement(this);
                    }
                }
            }
            if (isUniversal() && !Engine.instance.cartridge.universalActions.contains(this)) {
                Engine.instance.cartridge.universalActions.addElement(this);
            }
        }
    }

    public void dissociateFromTargets() {
        if (hasParameter()) {
            if (isReciprocal()) {
                for (int j = 0; j < this.targets.size(); j++) {
                    ((Thing) this.targets.elementAt(j)).actions.removeElement(this);
                }
            }
            if (isUniversal()) {
                Engine.instance.cartridge.universalActions.removeElement(this);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public String luaTostring() {
        return "a ZCommand instance";
    }

    /* Access modifiers changed, original: protected */
    public void setItem(String key, Object value) {
        if ("Text".equals(key)) {
            this.text = (String) value;
        } else if ("CmdWith".equals(key)) {
            boolean np = LuaState.boolEval(value);
            if (np == this.parameter) {
                return;
            }
            if (np) {
                this.parameter = true;
                associateWithTargets();
                return;
            }
            dissociateFromTargets();
            this.parameter = false;
        } else if ("Enabled".equals(key)) {
            this.enabled = LuaState.boolEval(value);
        } else if ("WorksWithAll".equals(key)) {
            dissociateFromTargets();
            this.universal = LuaState.boolEval(value);
            associateWithTargets();
        } else if ("WorksWithList".equals(key)) {
            dissociateFromTargets();
            LuaTable lt = (LuaTable) value;
            Object i = null;
            while (true) {
                i = lt.next(i);
                if (i != null) {
                    this.targets.addElement(lt.rawget(i));
                } else {
                    associateWithTargets();
                    return;
                }
            }
        } else if ("MakeReciprocal".equals(key)) {
            dissociateFromTargets();
            this.reciprocal = LuaState.boolEval(value);
            associateWithTargets();
        } else if ("EmptyTargetListText".equals(key)) {
            this.notarget = value == null ? "(not available now)" : value.toString();
        }
    }

    public int visibleTargets(Container where) {
        int count = 0;
        Object key = null;
        while (true) {
            key = where.inventory.next(key);
            if (key == null) {
                return count;
            }
            Thing o = where.inventory.rawget(key);
            if (o instanceof Thing) {
                Thing t = o;
                if (t.isVisible() && (this.targets.contains(t) || isUniversal())) {
                    count++;
                }
            }
        }
    }

    public int targetsInside(LuaTable v) {
        int count = 0;
        Object key = null;
        while (true) {
            key = v.next(key);
            if (key == null) {
                return count;
            }
            Thing o = v.rawget(key);
            if (o instanceof Thing) {
                Thing t = o;
                if (t.isVisible() && (this.targets.contains(t) || isUniversal())) {
                    count++;
                }
            }
        }
    }

    public boolean isTarget(Thing t) {
        return this.targets.contains(t) || isUniversal();
    }

    public Vector getTargets() {
        return this.targets;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasParameter() {
        return this.parameter;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isUniversal() {
        return this.universal;
    }

    public void setActor(Thing a) {
        this.actor = a;
    }

    public Thing getActor() {
        return this.actor;
    }

    public boolean isReciprocal() {
        return this.reciprocal;
    }
}

package p009se.krka.kahlua.stdlib;

import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;

/* renamed from: se.krka.kahlua.stdlib.TableLib */
public final class TableLib implements JavaFunction {
    private static final int CONCAT = 0;
    private static final int INSERT = 1;
    private static final int MAXN = 3;
    private static final int NUM_FUNCTIONS = 4;
    private static final int REMOVE = 2;
    private static TableLib[] functions;
    private static final String[] names = new String[4];
    private int index;

    static {
        names[0] = "concat";
        names[1] = "insert";
        names[2] = "remove";
        names[3] = "maxn";
    }

    public TableLib(int index) {
        this.index = index;
    }

    public static void register(LuaState state) {
        TableLib.initFunctions();
        LuaTable table = new LuaTableImpl();
        state.getEnvironment().rawset(BaseLib.TYPE_TABLE, table);
        for (int i = 0; i < 4; i++) {
            table.rawset(names[i], functions[i]);
        }
    }

    private static synchronized void initFunctions() {
        synchronized (TableLib.class) {
            if (functions == null) {
                functions = new TableLib[4];
                for (int i = 0; i < 4; i++) {
                    functions[i] = new TableLib(i);
                }
            }
        }
    }

    public String toString() {
        if (this.index < names.length) {
            return "table." + names[this.index];
        }
        return super.toString();
    }

    public int call(LuaCallFrame callFrame, int nArguments) {
        switch (this.index) {
            case 0:
                return TableLib.concat(callFrame, nArguments);
            case 1:
                return TableLib.insert(callFrame, nArguments);
            case 2:
                return TableLib.remove(callFrame, nArguments);
            case 3:
                return TableLib.maxn(callFrame, nArguments);
            default:
                return 0;
        }
    }

    private static int concat(LuaCallFrame callFrame, int nArguments) {
        int last;
        BaseLib.luaAssert(nArguments >= 1, "expected table, got no arguments");
        LuaTable table = (LuaTable) callFrame.get(0);
        String separator = "";
        if (nArguments >= 2) {
            separator = BaseLib.rawTostring(callFrame.get(1));
        }
        int first = 1;
        if (nArguments >= 3) {
            first = BaseLib.rawTonumber(callFrame.get(2)).intValue();
        }
        if (nArguments >= 4) {
            last = BaseLib.rawTonumber(callFrame.get(3)).intValue();
        } else {
            last = table.len();
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = first; i <= last; i++) {
            if (i > first) {
                buffer.append(separator);
            }
            buffer.append(BaseLib.rawTostring(table.rawget(LuaState.toDouble((long) i))));
        }
        return callFrame.push(buffer.toString());
    }

    public static void insert(LuaState state, LuaTable table, Object element) {
        TableLib.append(state, table, element);
    }

    public static void append(LuaState state, LuaTable table, Object element) {
        state.tableSet(table, LuaState.toDouble((long) (table.len() + 1)), element);
    }

    public static void rawappend(LuaTable table, Object element) {
        table.rawset(LuaState.toDouble((long) (table.len() + 1)), element);
    }

    public static void insert(LuaState state, LuaTable table, int position, Object element) {
        for (int i = table.len(); i >= position; i--) {
            state.tableSet(table, LuaState.toDouble((long) (i + 1)), state.tableGet(table, LuaState.toDouble((long) i)));
        }
        state.tableSet(table, LuaState.toDouble((long) position), element);
    }

    public static void rawinsert(LuaTable table, int position, Object element) {
        int len = table.len();
        if (position <= len) {
            Double dest = LuaState.toDouble((long) (len + 1));
            for (int i = len; i >= position; i--) {
                Double src = LuaState.toDouble((long) i);
                table.rawset(dest, table.rawget(src));
                dest = src;
            }
            table.rawset(dest, element);
            return;
        }
        table.rawset(LuaState.toDouble((long) position), element);
    }

    private static int insert(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        Object elem;
        if (nArguments >= 2) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "Not enough arguments");
        LuaTable t = (LuaTable) callFrame.get(0);
        int pos = t.len() + 1;
        if (nArguments > 2) {
            pos = BaseLib.rawTonumber(callFrame.get(1)).intValue();
            elem = callFrame.get(2);
        } else {
            elem = callFrame.get(1);
        }
        TableLib.insert(callFrame.thread.state, t, pos, elem);
        return 0;
    }

    public static Object remove(LuaState state, LuaTable table) {
        return TableLib.remove(state, table, table.len());
    }

    public static Object remove(LuaState state, LuaTable table, int position) {
        Object ret = state.tableGet(table, LuaState.toDouble((long) position));
        int len = table.len();
        for (int i = position; i < len; i++) {
            state.tableSet(table, LuaState.toDouble((long) i), state.tableGet(table, LuaState.toDouble((long) (i + 1))));
        }
        state.tableSet(table, LuaState.toDouble((long) len), null);
        return ret;
    }

    public static Object rawremove(LuaTable table, int position) {
        Object ret = table.rawget(LuaState.toDouble((long) position));
        int len = table.len();
        for (int i = position; i <= len; i++) {
            table.rawset(LuaState.toDouble((long) i), table.rawget(LuaState.toDouble((long) (i + 1))));
        }
        return ret;
    }

    public static void removeItem(LuaTable table, Object item) {
        if (item != null) {
            Object key = null;
            do {
                key = table.next(key);
                if (key == null) {
                    return;
                }
            } while (!item.equals(table.rawget(key)));
            if (key instanceof Double) {
                double k = ((Double) key).doubleValue();
                int i = (int) k;
                if (k == ((double) i)) {
                    TableLib.rawremove(table, i);
                    return;
                }
                return;
            }
            table.rawset(key, null);
        }
    }

    public static void dumpTable(LuaTable table) {
        System.out.print("table " + table + ": ");
        Object key = null;
        while (true) {
            key = table.next(key);
            if (key != null) {
                System.out.print(key.toString() + " => " + table.rawget(key) + ", ");
            } else {
                System.out.println();
                return;
            }
        }
    }

    private static int remove(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 1) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "expected table, got no arguments");
        LuaTable t = (LuaTable) callFrame.get(0);
        int pos = t.len();
        if (nArguments > 1) {
            pos = BaseLib.rawTonumber(callFrame.get(1)).intValue();
        }
        callFrame.push(TableLib.remove(callFrame.thread.state, t, pos));
        return 1;
    }

    private static int maxn(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 1) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "expected table, got no arguments");
        LuaTable t = (LuaTable) callFrame.get(0);
        Object key = null;
        int max = 0;
        while (true) {
            key = t.next(key);
            if (key == null) {
                callFrame.push(LuaState.toDouble((long) max));
                return 1;
            } else if (key instanceof Double) {
                int what = (int) LuaState.fromDouble(key);
                if (what > max) {
                    max = what;
                }
            }
        }
    }

    public static Object find(LuaTable table, Object item) {
        if (item == null) {
            return null;
        }
        Object key = null;
        do {
            key = table.next(key);
            if (key == null) {
                return null;
            }
        } while (!item.equals(table.rawget(key)));
        return key;
    }

    public static boolean contains(LuaTable table, Object item) {
        return TableLib.find(table, item) != null;
    }
}

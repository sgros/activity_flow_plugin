// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.JavaFunction;

public final class TableLib implements JavaFunction
{
    private static final int CONCAT = 0;
    private static final int INSERT = 1;
    private static final int MAXN = 3;
    private static final int NUM_FUNCTIONS = 4;
    private static final int REMOVE = 2;
    private static TableLib[] functions;
    private static final String[] names;
    private int index;
    
    static {
        (names = new String[4])[0] = "concat";
        TableLib.names[1] = "insert";
        TableLib.names[2] = "remove";
        TableLib.names[3] = "maxn";
    }
    
    public TableLib(final int index) {
        this.index = index;
    }
    
    public static void append(final LuaState luaState, final LuaTable luaTable, final Object o) {
        luaState.tableSet(luaTable, LuaState.toDouble(luaTable.len() + 1), o);
    }
    
    private static int concat(final LuaCallFrame luaCallFrame, int n) {
        BaseLib.luaAssert(n >= 1, "expected table, got no arguments");
        final LuaTable luaTable = (LuaTable)luaCallFrame.get(0);
        String rawTostring = "";
        if (n >= 2) {
            rawTostring = BaseLib.rawTostring(luaCallFrame.get(1));
        }
        int intValue = 1;
        if (n >= 3) {
            intValue = BaseLib.rawTonumber(luaCallFrame.get(2)).intValue();
        }
        if (n >= 4) {
            n = BaseLib.rawTonumber(luaCallFrame.get(3)).intValue();
        }
        else {
            n = luaTable.len();
        }
        final StringBuffer sb = new StringBuffer();
        for (int i = intValue; i <= n; ++i) {
            if (i > intValue) {
                sb.append(rawTostring);
            }
            sb.append(BaseLib.rawTostring(luaTable.rawget(LuaState.toDouble(i))));
        }
        return luaCallFrame.push(sb.toString());
    }
    
    public static boolean contains(final LuaTable luaTable, final Object o) {
        return find(luaTable, o) != null;
    }
    
    public static void dumpTable(final LuaTable obj) {
        System.out.print("table " + obj + ": ");
        Object next = null;
        while (true) {
            next = obj.next(next);
            if (next == null) {
                break;
            }
            System.out.print(next.toString() + " => " + obj.rawget(next) + ", ");
        }
        System.out.println();
    }
    
    public static Object find(final LuaTable luaTable, final Object o) {
        Object o2;
        if (o == null) {
            o2 = null;
        }
        else {
            Object o3 = null;
            Object next;
            do {
                next = luaTable.next(o3);
                if (next == null) {
                    o2 = null;
                    return o2;
                }
                o3 = next;
            } while (!o.equals(luaTable.rawget(next)));
            o2 = next;
        }
        return o2;
    }
    
    private static void initFunctions() {
        synchronized (TableLib.class) {
            if (TableLib.functions == null) {
                TableLib.functions = new TableLib[4];
                for (int i = 0; i < 4; ++i) {
                    TableLib.functions[i] = new TableLib(i);
                }
            }
        }
    }
    
    private static int insert(final LuaCallFrame luaCallFrame, int intValue) {
        BaseLib.luaAssert(intValue >= 2, "Not enough arguments");
        final LuaTable luaTable = (LuaTable)luaCallFrame.get(0);
        final int n = luaTable.len() + 1;
        Object o;
        if (intValue > 2) {
            intValue = BaseLib.rawTonumber(luaCallFrame.get(1)).intValue();
            o = luaCallFrame.get(2);
        }
        else {
            o = luaCallFrame.get(1);
            intValue = n;
        }
        insert(luaCallFrame.thread.state, luaTable, intValue, o);
        return 0;
    }
    
    public static void insert(final LuaState luaState, final LuaTable luaTable, final int n, final Object o) {
        for (int i = luaTable.len(); i >= n; --i) {
            luaState.tableSet(luaTable, LuaState.toDouble(i + 1), luaState.tableGet(luaTable, LuaState.toDouble(i)));
        }
        luaState.tableSet(luaTable, LuaState.toDouble(n), o);
    }
    
    public static void insert(final LuaState luaState, final LuaTable luaTable, final Object o) {
        append(luaState, luaTable, o);
    }
    
    private static int maxn(final LuaCallFrame luaCallFrame, int n) {
        BaseLib.luaAssert(n >= 1, "expected table, got no arguments");
        final LuaTable luaTable = (LuaTable)luaCallFrame.get(0);
        Object o = null;
        n = 0;
        while (true) {
            final Object next = luaTable.next(o);
            if (next == null) {
                break;
            }
            o = next;
            if (!(next instanceof Double)) {
                continue;
            }
            final int n2 = (int)LuaState.fromDouble(next);
            o = next;
            if (n2 <= n) {
                continue;
            }
            n = n2;
            o = next;
        }
        luaCallFrame.push(LuaState.toDouble(n));
        return 1;
    }
    
    public static void rawappend(final LuaTable luaTable, final Object o) {
        luaTable.rawset(LuaState.toDouble(luaTable.len() + 1), o);
    }
    
    public static void rawinsert(final LuaTable luaTable, final int n, final Object o) {
        int i = luaTable.len();
        if (n <= i) {
            Double double1 = LuaState.toDouble(i + 1);
            while (i >= n) {
                final Double double2 = LuaState.toDouble(i);
                luaTable.rawset(double1, luaTable.rawget(double2));
                double1 = double2;
                --i;
            }
            luaTable.rawset(double1, o);
        }
        else {
            luaTable.rawset(LuaState.toDouble(n), o);
        }
    }
    
    public static Object rawremove(final LuaTable luaTable, int i) {
        final Object rawget = luaTable.rawget(LuaState.toDouble(i));
        while (i <= luaTable.len()) {
            luaTable.rawset(LuaState.toDouble(i), luaTable.rawget(LuaState.toDouble(i + 1)));
            ++i;
        }
        return rawget;
    }
    
    public static void register(final LuaState luaState) {
        initFunctions();
        final LuaTableImpl luaTableImpl = new LuaTableImpl();
        luaState.getEnvironment().rawset("table", luaTableImpl);
        for (int i = 0; i < 4; ++i) {
            luaTableImpl.rawset(TableLib.names[i], TableLib.functions[i]);
        }
    }
    
    private static int remove(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 1, "expected table, got no arguments");
        final LuaTable luaTable = (LuaTable)luaCallFrame.get(0);
        int n2 = luaTable.len();
        if (n > 1) {
            n2 = BaseLib.rawTonumber(luaCallFrame.get(1)).intValue();
        }
        luaCallFrame.push(remove(luaCallFrame.thread.state, luaTable, n2));
        return 1;
    }
    
    public static Object remove(final LuaState luaState, final LuaTable luaTable) {
        return remove(luaState, luaTable, luaTable.len());
    }
    
    public static Object remove(final LuaState luaState, final LuaTable luaTable, int i) {
        final Object tableGet = luaState.tableGet(luaTable, LuaState.toDouble(i));
        int len;
        for (len = luaTable.len(); i < len; ++i) {
            luaState.tableSet(luaTable, LuaState.toDouble(i), luaState.tableGet(luaTable, LuaState.toDouble(i + 1)));
        }
        luaState.tableSet(luaTable, LuaState.toDouble(len), null);
        return tableGet;
    }
    
    public static void removeItem(final LuaTable luaTable, final Object o) {
        if (o != null) {
            Object o2 = null;
            Object next;
            do {
                next = luaTable.next(o2);
                if (next == null) {
                    return;
                }
                o2 = next;
            } while (!o.equals(luaTable.rawget(next)));
            if (next instanceof Double) {
                final double doubleValue = (double)next;
                final int n = (int)doubleValue;
                if (doubleValue == n) {
                    rawremove(luaTable, n);
                }
            }
            else {
                luaTable.rawset(next, null);
            }
        }
    }
    
    @Override
    public int call(final LuaCallFrame luaCallFrame, int n) {
        switch (this.index) {
            default: {
                n = 0;
                break;
            }
            case 0: {
                n = concat(luaCallFrame, n);
                break;
            }
            case 1: {
                n = insert(luaCallFrame, n);
                break;
            }
            case 2: {
                n = remove(luaCallFrame, n);
                break;
            }
            case 3: {
                n = maxn(luaCallFrame, n);
                break;
            }
        }
        return n;
    }
    
    @Override
    public String toString() {
        String s;
        if (this.index < TableLib.names.length) {
            s = "table." + TableLib.names[this.index];
        }
        else {
            s = super.toString();
        }
        return s;
    }
}

package p005cz.matejcik.openwig;

import p009se.krka.kahlua.p010vm.LuaState;

/* renamed from: cz.matejcik.openwig.Task */
public class Task extends EventTable {
    public static final int DONE = 1;
    public static final int FAILED = 2;
    public static final int PENDING = 0;
    private boolean active;
    private boolean complete;
    private int state = 1;

    public boolean isVisible() {
        return this.visible && this.active;
    }

    public boolean isComplete() {
        return this.complete;
    }

    public int state() {
        if (this.complete) {
            return this.state;
        }
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public String luaTostring() {
        return "a ZTask instance";
    }

    /* Access modifiers changed, original: protected */
    public void setItem(String key, Object value) {
        if ("Active".equals(key)) {
            boolean a = LuaState.boolEval(value);
            if (a != this.active) {
                this.active = a;
                callEvent("OnSetActive", null);
            }
        } else if ("Complete".equals(key)) {
            boolean c = LuaState.boolEval(value);
            if (c != this.complete) {
                this.complete = c;
                callEvent("OnSetComplete", null);
            }
        } else if ("CorrectState".equals(key) && (value instanceof String)) {
            String v = (String) value;
            int s = 1;
            if ("Incorrect".equalsIgnoreCase(v) || "NotCorrect".equalsIgnoreCase(v)) {
                s = 2;
            }
            if (s != this.state) {
                this.state = s;
                callEvent("OnSetCorrectState", null);
            }
        } else {
            super.setItem(key, value);
        }
    }
}

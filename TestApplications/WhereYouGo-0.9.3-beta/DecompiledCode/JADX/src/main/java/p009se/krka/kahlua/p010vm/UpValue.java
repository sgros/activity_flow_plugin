package p009se.krka.kahlua.p010vm;

/* renamed from: se.krka.kahlua.vm.UpValue */
public final class UpValue {
    public int index;
    public LuaThread thread;
    public Object value;

    public final Object getValue() {
        if (this.thread == null) {
            return this.value;
        }
        return this.thread.objectStack[this.index];
    }

    public final void setValue(Object object) {
        if (this.thread == null) {
            this.value = object;
        } else {
            this.thread.objectStack[this.index] = object;
        }
    }
}

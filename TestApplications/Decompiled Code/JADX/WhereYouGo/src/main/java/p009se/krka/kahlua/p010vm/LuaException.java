package p009se.krka.kahlua.p010vm;

import p009se.krka.kahlua.stdlib.BaseLib;

/* renamed from: se.krka.kahlua.vm.LuaException */
public class LuaException extends RuntimeException {
    private static final long serialVersionUID = 1;
    public Object errorMessage;

    public LuaException(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        if (this.errorMessage == null) {
            return BaseLib.TYPE_NIL;
        }
        return this.errorMessage.toString();
    }
}

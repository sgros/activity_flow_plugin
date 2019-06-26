// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.vm;

public class LuaException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    public Object errorMessage;
    
    public LuaException(final Object errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    @Override
    public String getMessage() {
        String string;
        if (this.errorMessage == null) {
            string = "nil";
        }
        else {
            string = this.errorMessage.toString();
        }
        return string;
    }
}

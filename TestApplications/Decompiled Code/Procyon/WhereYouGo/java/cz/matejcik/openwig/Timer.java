// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import se.krka.kahlua.vm.LuaState;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.TimerTask;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.JavaFunction;

public class Timer extends EventTable
{
    private static final int COUNTDOWN = 0;
    private static final int INTERVAL = 1;
    private static final Double ZERO;
    private static java.util.Timer globalTimer;
    private static JavaFunction start;
    private static JavaFunction stop;
    private static JavaFunction tick;
    private long duration;
    private long lastTick;
    private TimerTask task;
    private int type;
    
    static {
        Timer.start = new JavaFunction() {
            @Override
            public int call(final LuaCallFrame luaCallFrame, final int n) {
                ((Timer)luaCallFrame.get(0)).start();
                return 0;
            }
        };
        Timer.stop = new JavaFunction() {
            @Override
            public int call(final LuaCallFrame luaCallFrame, final int n) {
                ((Timer)luaCallFrame.get(0)).stop();
                return 0;
            }
        };
        Timer.tick = new JavaFunction() {
            @Override
            public int call(final LuaCallFrame luaCallFrame, final int n) {
                ((Timer)luaCallFrame.get(0)).callEvent("OnTick", null);
                return 0;
            }
        };
        ZERO = new Double(0.0);
    }
    
    public Timer() {
        this.task = null;
        this.type = 0;
        this.duration = -1L;
        this.lastTick = 0L;
        if (Timer.globalTimer == null) {
            Timer.globalTimer = new java.util.Timer();
        }
        this.table.rawset("Start", Timer.start);
        this.table.rawset("Stop", Timer.stop);
        this.table.rawset("Tick", Timer.tick);
    }
    
    public static void kill() {
        if (Timer.globalTimer != null) {
            Timer.globalTimer.cancel();
        }
        Timer.globalTimer = null;
    }
    
    public static void register() {
        Engine.instance.savegame.addJavafunc(Timer.start);
        Engine.instance.savegame.addJavafunc(Timer.stop);
        Engine.instance.savegame.addJavafunc(Timer.tick);
    }
    
    private void start(final long n, final boolean b) {
        this.task = new TimerTask();
        this.lastTick = System.currentTimeMillis();
        if (b) {
            this.callEvent("OnStart", null);
        }
        this.updateRemaining();
        switch (this.type) {
            case 0: {
                Timer.globalTimer.schedule(this.task, n);
                break;
            }
            case 1: {
                Timer.globalTimer.scheduleAtFixedRate(this.task, n, this.duration);
                break;
            }
        }
    }
    
    @Override
    public void deserialize(final DataInputStream dataInputStream) throws IOException {
        final boolean boolean1 = dataInputStream.readBoolean();
        this.lastTick = dataInputStream.readLong();
        super.deserialize(dataInputStream);
        if (boolean1) {
            if (this.lastTick + this.duration < System.currentTimeMillis()) {
                Engine.callEvent(this, "OnTick", null);
            }
            else {
                this.start(this.lastTick + this.duration - System.currentTimeMillis(), false);
            }
            if (this.type == 1) {
                this.start();
            }
        }
    }
    
    @Override
    protected String luaTostring() {
        return "a ZTimer instance";
    }
    
    @Override
    public void serialize(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeBoolean(this.task != null);
        dataOutputStream.writeLong(this.lastTick);
        super.serialize(dataOutputStream);
    }
    
    @Override
    protected void setItem(String s, final Object o) {
        if ("Type".equals(s) && o instanceof String) {
            s = (String)o;
            int type = this.type;
            if ("Countdown".equals(s)) {
                final int n = type = 0;
                if (this.type != 0) {
                    type = n;
                    if (this.task != null) {
                        this.task.restart = false;
                        type = n;
                    }
                }
            }
            else if ("Interval".equals(s)) {
                final int n2 = type = 1;
                if (1 != this.type) {
                    type = n2;
                    if (this.task != null) {
                        this.task.restart = true;
                        type = n2;
                    }
                }
            }
            this.type = type;
        }
        else if ("Duration".equals(s) && o instanceof Double) {
            final long n3 = (long)LuaState.fromDouble(o);
            this.table.rawset("Remaining", Timer.ZERO);
            this.duration = 1000L * n3;
        }
        else {
            super.setItem(s, o);
        }
    }
    
    public void start() {
        Engine.log("TIME: " + this.name + " start", 1);
        if (this.task == null) {
            if (this.duration == 0L) {
                this.callEvent("OnStart", null);
                this.callEvent("OnTick", null);
            }
            else {
                this.start(this.duration, true);
            }
        }
    }
    
    public void stop() {
        if (this.task != null) {
            Engine.log("TIME: " + this.name + " stop", 1);
            this.task.cancel();
            this.task = null;
            this.callEvent("OnStop", null);
        }
    }
    
    public void tick() {
        Engine.log("TIME: " + this.name + " tick", 1);
        Engine.callEvent(this, "OnTick", null);
        this.lastTick = System.currentTimeMillis();
        this.updateRemaining();
        if (this.type == 0 && this.task != null) {
            this.task.cancel();
            this.task = null;
        }
        if (this.type == 1 && this.task != null && !this.task.restart) {
            Engine.callEvent(this, "OnStart", null);
        }
    }
    
    public void updateRemaining() {
        if (this.task == null) {
            this.table.rawset("Remaining", Timer.ZERO);
        }
        else {
            this.table.rawset("Remaining", LuaState.toDouble(this.duration / 1000L - (System.currentTimeMillis() - this.lastTick) / 1000L));
        }
    }
    
    private class TimerTask extends java.util.TimerTask
    {
        public boolean restart;
        
        private TimerTask() {
            this.restart = false;
        }
        
        @Override
        public void run() {
            Timer.this.tick();
            Engine.refreshUI();
            if (this.restart) {
                this.cancel();
                Timer.this.task = null;
                Timer.this.start();
            }
        }
    }
}

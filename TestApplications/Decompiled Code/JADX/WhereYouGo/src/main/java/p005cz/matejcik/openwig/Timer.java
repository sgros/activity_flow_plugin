package p005cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaState;

/* renamed from: cz.matejcik.openwig.Timer */
public class Timer extends EventTable {
    private static final int COUNTDOWN = 0;
    private static final int INTERVAL = 1;
    private static final Double ZERO = new Double(0.0d);
    private static java.util.Timer globalTimer;
    private static JavaFunction start = new C04041();
    private static JavaFunction stop = new C04052();
    private static JavaFunction tick = new C04063();
    private long duration = -1;
    private long lastTick = 0;
    private TimerTask task = null;
    private int type = 0;

    /* renamed from: cz.matejcik.openwig.Timer$TimerTask */
    private class TimerTask extends java.util.TimerTask {
        public boolean restart;

        private TimerTask() {
            this.restart = false;
        }

        /* synthetic */ TimerTask(Timer x0, C04041 x1) {
            this();
        }

        public void run() {
            Timer.this.tick();
            Engine.refreshUI();
            if (this.restart) {
                cancel();
                Timer.this.task = null;
                Timer.this.start();
            }
        }
    }

    /* renamed from: cz.matejcik.openwig.Timer$1 */
    static class C04041 implements JavaFunction {
        C04041() {
        }

        public int call(LuaCallFrame callFrame, int nArguments) {
            ((Timer) callFrame.get(0)).start();
            return 0;
        }
    }

    /* renamed from: cz.matejcik.openwig.Timer$2 */
    static class C04052 implements JavaFunction {
        C04052() {
        }

        public int call(LuaCallFrame callFrame, int nArguments) {
            ((Timer) callFrame.get(0)).stop();
            return 0;
        }
    }

    /* renamed from: cz.matejcik.openwig.Timer$3 */
    static class C04063 implements JavaFunction {
        C04063() {
        }

        public int call(LuaCallFrame callFrame, int nArguments) {
            ((Timer) callFrame.get(0)).callEvent("OnTick", null);
            return 0;
        }
    }

    public static void register() {
        Engine.instance.savegame.addJavafunc(start);
        Engine.instance.savegame.addJavafunc(stop);
        Engine.instance.savegame.addJavafunc(tick);
    }

    /* Access modifiers changed, original: protected */
    public String luaTostring() {
        return "a ZTimer instance";
    }

    public Timer() {
        if (globalTimer == null) {
            globalTimer = new java.util.Timer();
        }
        this.table.rawset("Start", start);
        this.table.rawset("Stop", stop);
        this.table.rawset("Tick", tick);
    }

    /* Access modifiers changed, original: protected */
    public void setItem(String key, Object value) {
        if ("Type".equals(key) && (value instanceof String)) {
            String v = (String) value;
            int t = this.type;
            if ("Countdown".equals(v)) {
                t = 0;
                if (!(0 == this.type || this.task == null)) {
                    this.task.restart = false;
                }
            } else if ("Interval".equals(v)) {
                t = 1;
                if (!(1 == this.type || this.task == null)) {
                    this.task.restart = true;
                }
            }
            this.type = t;
        } else if ("Duration".equals(key) && (value instanceof Double)) {
            long d = (long) LuaState.fromDouble(value);
            this.table.rawset("Remaining", ZERO);
            this.duration = 1000 * d;
        } else {
            super.setItem(key, value);
        }
    }

    public void start() {
        Engine.log("TIME: " + this.name + " start", 1);
        if (this.task == null) {
            if (this.duration == 0) {
                callEvent("OnStart", null);
                callEvent("OnTick", null);
                return;
            }
            start(this.duration, true);
        }
    }

    private void start(long when, boolean callEvent) {
        this.task = new TimerTask(this, null);
        this.lastTick = System.currentTimeMillis();
        if (callEvent) {
            callEvent("OnStart", null);
        }
        updateRemaining();
        switch (this.type) {
            case 0:
                globalTimer.schedule(this.task, when);
                return;
            case 1:
                globalTimer.scheduleAtFixedRate(this.task, when, this.duration);
                return;
            default:
                return;
        }
    }

    public void stop() {
        if (this.task != null) {
            Engine.log("TIME: " + this.name + " stop", 1);
            this.task.cancel();
            this.task = null;
            callEvent("OnStop", null);
        }
    }

    public void tick() {
        Engine.log("TIME: " + this.name + " tick", 1);
        Engine.callEvent(this, "OnTick", null);
        this.lastTick = System.currentTimeMillis();
        updateRemaining();
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
            this.table.rawset("Remaining", ZERO);
            return;
        }
        this.table.rawset("Remaining", LuaState.toDouble((this.duration / 1000) - ((System.currentTimeMillis() - this.lastTick) / 1000)));
    }

    public static void kill() {
        if (globalTimer != null) {
            globalTimer.cancel();
        }
        globalTimer = null;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeBoolean(this.task != null);
        out.writeLong(this.lastTick);
        super.serialize(out);
    }

    public void deserialize(DataInputStream in) throws IOException {
        boolean resume = in.readBoolean();
        this.lastTick = in.readLong();
        super.deserialize(in);
        if (resume) {
            if (this.lastTick + this.duration < System.currentTimeMillis()) {
                Engine.callEvent(this, "OnTick", null);
            } else {
                start((this.lastTick + this.duration) - System.currentTimeMillis(), false);
            }
            if (this.type == 1) {
                start();
            }
        }
    }
}

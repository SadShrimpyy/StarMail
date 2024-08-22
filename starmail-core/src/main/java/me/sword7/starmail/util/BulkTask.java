package me.sword7.starmail.util;

import me.sword7.starmail.StarMail;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BulkTask extends BukkitRunnable {

    private State state;
    private int extensions = 0;
    private int duration;
    private int countdown;
    private int maxExtensions;

    public BulkTask(int duration, int maxExtensions) {
        this.state = State.RESTING;
        this.duration = duration;
        this.countdown = duration;
        this.maxExtensions = maxExtensions;
        runTaskTimer(StarMail.getPlugin(), 1 * 20, 1 * 20);
    }

    public BulkTask(int duration, int maxExtensions, State start) {
        this.state = start;
        this.duration = duration;
        this.countdown = duration;
        this.maxExtensions = maxExtensions;
        runTaskTimer(StarMail.getPlugin(), 1 * 20, 1 * 20);
    }

    @Override
    public void run() {
        if (state != State.RESTING) {
            countdown--;
            if (countdown <= 0) {
                runTask();
                reset();
            }
        }
    }

    private void reset() {
        this.state = State.RESTING;
        this.countdown = duration;
        this.extensions = 0;
    }

    protected abstract void runTask();

    public void onExtend() {
        state = State.COMBINING;
        countdown = duration;
        extensions++;
        if (extensions == maxExtensions) {
            runTask();
        }
    }

    public enum State {
        RESTING,
        COMBINING,
    }

}
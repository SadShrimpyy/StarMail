package me.sword7.starmail.util;

import me.sword7.starmail.StarMail;
import org.bukkit.scheduler.BukkitRunnable;

public class Cooldown extends BukkitRunnable {

    private Runnable r;
    private int countdown;

    public Cooldown(Runnable r, int seconds) {
        this.r = r;
        this.countdown = seconds;
        runTaskTimer(StarMail.getPlugin(), 20, 20);
    }


    @Override
    public void run() {
        countdown--;
        if (countdown <= 0) {
            cancel();
            r.run();
        }
    }

    public int getCountdown() {
        return countdown;
    }
}

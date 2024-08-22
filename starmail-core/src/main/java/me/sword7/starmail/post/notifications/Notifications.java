package me.sword7.starmail.post.notifications;

public class Notifications {

    private boolean onJoin;
    private boolean onReceive;

    public Notifications(){
        this.onJoin = true;
        this.onReceive = true;
    }

    public Notifications(boolean onJoin, boolean onReceive){
        this.onJoin = onJoin;
        this.onReceive = onReceive;
    }

    public boolean isOnJoin() {
        return onJoin;
    }

    public boolean isOnReceive() {
        return onReceive;
    }

    public void setOnJoin(boolean onJoin) {
        this.onJoin = onJoin;
    }

    public void setOnReceive(boolean onSend) {
        this.onReceive = onSend;
    }
}

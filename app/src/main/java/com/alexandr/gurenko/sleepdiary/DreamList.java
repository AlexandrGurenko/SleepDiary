package com.alexandr.gurenko.sleepdiary;

public class DreamList {

    private long startSleep;
    private long endSleep;
    private long duration;

    DreamList() {

    }

    public long getStartSleep() {
        return startSleep;
    }

    public void setStartSleep(long startSleep) {
        this.startSleep = startSleep;
    }

    public long getEndSleep() {
        return endSleep;
    }

    public void setEndSleep(long endSleep) {
        this.endSleep = endSleep;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

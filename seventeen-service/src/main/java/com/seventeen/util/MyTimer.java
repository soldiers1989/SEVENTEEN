package com.seventeen.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * csk
 */
public class MyTimer extends Timer {

    public TimerTask schedule(final Runnable r, long delay) {
        final TimerTask task = new TimerTask() { public void run() { r.run(); }};
        this.schedule(task, delay);
        return task;
    }

    public TimerTask schedule(final Runnable r, long delay,long period) {
        final TimerTask task = new TimerTask() { public void run() { r.run(); }};
        this.schedule(task, delay,period);
        return task;
    }

    public TimerTask schedule(final Runnable r, Date time) {
        final TimerTask task = new TimerTask() { public void run() { r.run(); }};
        this.schedule(task, time);
        return task;
    }
}
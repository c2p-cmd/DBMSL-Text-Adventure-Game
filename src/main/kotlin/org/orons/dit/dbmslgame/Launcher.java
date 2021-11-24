package org.orons.dit.dbmslgame;

import javafx.application.Application;

public class Launcher implements Runnable {
    private static String[] args;

    public static void main(String[] args) {
        Launcher.args = args;
        new Thread(
                new Launcher(),
                "Launcher Thread"
        ).start();
    }

    @Override
    public void run() {
        Main.main(args);
    }
}

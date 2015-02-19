package me.roguemc9000.uhc.utilities;

import me.roguemc9000.uhc.Core;

/**
 * Created by Nico on 10/6/2014.
 */
public class Border implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= Core.getShrinkSize(); i++) {
            if (Core.getStaticBorder() > 100) {
                Core.setBorder(Core.getStaticBorder() - 1);
            }
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

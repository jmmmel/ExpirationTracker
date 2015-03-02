package edu.mel06002byui.expirationtracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2/26/2015.
 */
public class BackgroundNotifier {
    private MainActivity monitorMain;
    private List<Grocery> expiresSoon;

    public BackgroundNotifier(MainActivity testMain) {
        monitorMain = testMain;
        expiresSoon = new ArrayList<>();
    }

    private void updateList() {

    }
}

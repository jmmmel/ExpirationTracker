package edu.mel06002byui.expirationtracker.test;

import junit.framework.TestCase;

import edu.mel06002byui.expirationtracker.BackgroundNotifier;
import edu.mel06002byui.expirationtracker.MainActivity;

public class NotifierTests extends TestCase {

    boolean updateTest(){

        MainActivity testMain = new MainActivity();
        BackgroundNotifier testNotifier = new BackgroundNotifier(testMain);

        return false;
    }
}
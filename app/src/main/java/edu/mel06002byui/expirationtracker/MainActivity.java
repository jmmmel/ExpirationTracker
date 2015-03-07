package edu.mel06002byui.expirationtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Set;
import java.util.TreeSet;


public class MainActivity extends ActionBarActivity {

    private Set<Grocery> allStoredItems;
    BackgroundNotifier monitor;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        allStoredItems = new TreeSet<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateSetOnCreate();
        displayToListView();
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     *  updates the list view to hold the set
     */
    void displayToListView(){

    }

    public void addItem(View view){
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);

    }

    protected void addGroceryItem(Grocery tempGrocery){
        allStoredItems.add(tempGrocery);
    }

    /**
     * This will read in from the database on opening and store into our allStoredItems
     */
    private void populateSetOnCreate(){

    }
}


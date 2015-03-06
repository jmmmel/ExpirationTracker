package edu.mel06002byui.expirationtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


public class MainActivity extends ActionBarActivity {

    private Set<Grocery> currentExpireList;
    BackgroundNotifier monitor;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentExpireList = new TreeSet<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateSetOnCreate();
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
     *
     */
    void display(){
        // Get the reference of GroceryList
        ListView groceryList = (ListView)findViewById(R.id.GroceryList);

        // create an arrayList of groceries

        ArrayAdapter<Grocery> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, allStoredItems.toArray());

        // Set Adapter
        groceryList.setAdapter(arrayAdapter);

    }

    public void addItem(View view){
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);

    }

    protected void addGroceryItem(){

    }

    private void populateSetOnCreate(){

    }
}


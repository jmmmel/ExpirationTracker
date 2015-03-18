package edu.mel06002byui.expirationtracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;


public class MainActivity extends ActionBarActivity {
    private static final String TAG_MAIN_ACTIVITY= "MainActivity";
    private Set<Grocery> allStoredItems = new TreeSet<>();
    BackgroundNotifier monitor;
    private AlertDialog.Builder dialogBuilder;
    private GrocerySQLiteHelper db = new GrocerySQLiteHelper(this);
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db.onCreate(db.getWritableDatabase());
        Log.i(TAG_MAIN_ACTIVITY, "Populating set");
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
        // Get the reference of GroceryList
        ListView groceryList = (ListView)findViewById(R.id.GroceryList);

        // create an arrayList of groceries
        ArrayList<Grocery> tempArray = new ArrayList<>();
        tempArray.addAll(allStoredItems);
        ArrayAdapter<Grocery> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, tempArray);

        // Set Adapter
        groceryList.setAdapter(arrayAdapter);

    }

    Button addButton;

    public void addItem(View view){
        //Intent intent = new Intent(this, AddItem.class);
       // startActivity(intent);

        final Dialog custom = new Dialog(MainActivity.this);

        custom.setContentView(R.layout.activity_add_item);
        custom.setTitle("Add Item");

        addButton = (Button)custom.findViewById(R.id.FinalAddbutton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameEdit = (EditText) custom.findViewById(R.id.nameText);
                DatePicker expDate = (DatePicker) custom.findViewById(R.id.datePicker);
                EditText quantityEdit = (EditText) custom.findViewById(R.id.QuantityText);
                String name = nameEdit.getText().toString();
                String quantity = quantityEdit.getText().toString();
                int quantityAsInt = Integer.parseInt(quantity);
                int Day = expDate.getDayOfMonth();
                int month = expDate.getMonth();
                int year = expDate.getYear();

                Grocery grocery = new Grocery(name, quantityAsInt, month, Day, year);
                Log.d("GroceryToBeAdded", grocery.toString());
                addGroceryItemToSet(grocery);

                displayToListView();
                custom.cancel();
            }
        });


        custom.show();
    }

    //public void finalAdd(View view){

   // }

    public void removeXml(View view){
        Intent intent = new Intent(this, removeXml.class);
        startActivity(intent);

    }

    protected void addGroceryItemToSet(Grocery tempGrocery){


        /*check if contains
            if true find it and increase quantity then update DB
            false add to DB then add to set from DB
         */
        if(allStoredItems.contains(tempGrocery)){
            Grocery toUpdate = findInSet(tempGrocery);
            toUpdate.addQuantity(tempGrocery.getQuantity());
            db.updateGroceryItem(toUpdate);
        }
        else{
            long newID = db.addGroceryToDatabase(tempGrocery);

            allStoredItems.add(db.getGroceryByID(((int) newID)));
        }
    }
    private Grocery findInSet(Grocery find){
        for(Grocery check: allStoredItems){
            if(check.equals(find))
                return check;
        }
        return find;
    }

    /**
     * read in from the database on opening and store into allStoredItems
     */
    private void populateSetOnCreate(){
        allStoredItems.addAll(db.getAllGroceries());

        Grocery tmpGrocery = new Grocery();
        tmpGrocery.setQuantity(5);
        tmpGrocery.setName("Peas");
        tmpGrocery.setDateWithString("03/25/2015");
        db.addGroceryToDatabase(tmpGrocery);
        allStoredItems.add(db.getGroceryByID(1));
    }
}


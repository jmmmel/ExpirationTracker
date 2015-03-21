package edu.mel06002byui.expirationtracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


public class MainActivity extends ActionBarActivity {
    private static final String TAG_MAIN_ACTIVITY= "MainActivity";
    private Set<Grocery> allStoredItems = new TreeSet<>();
    BackgroundNotifier monitor;
    private AlertDialog.Builder dialogBuilder;
    private GrocerySQLiteHelper db = new GrocerySQLiteHelper(this);
    ListView lv;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         lv = (ListView)findViewById(R.id.GroceryList);
        registerForContextMenu(lv);




        Log.i(TAG_MAIN_ACTIVITY, "Populating set");
        populateSetOnCreate();
        displayToListView();
    }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuinfo){
            super.onCreateContextMenu(menu, v, menuinfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menulong, menu);
    }

    //Buttons needed for dialog box
    private Button saveEdit;
    private Button cancelEdit;
    private Grocery selectedGrocery;

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int selectedPostion = ((AdapterView.AdapterContextMenuInfo)info).position;
        Iterator<Grocery> iterator = allStoredItems.iterator();
        selectedGrocery = null;
        for(int i = 0; i < selectedPostion + 1; i++)
            selectedGrocery = iterator.next();
        if(null == selectedGrocery) {
            Log.d("ContectMenu", "Didn't find a selected postition");
            return true;
        }
        Log.d("ContextMenu", selectedGrocery.toString());
        switch (item.getItemId())
        {
            case R.id.id_delete:
                allStoredItems.remove(selectedGrocery);
                db.deleteItemFromDB(selectedGrocery);
                displayToListView();
                Log.d("ContextMenu", "Position: " + selectedPostion);
                return true;

            case R.id.id_edit:
                final Dialog custom = new Dialog(MainActivity.this);

                custom.setContentView(R.layout.activity_edit_grocery);
                custom.setTitle("Edit Item");
                final EditText nameEdit = (EditText)custom.findViewById(R.id.editGroceryTextName);
                nameEdit.setText(selectedGrocery.getName());
                final EditText quantityEdit = (EditText)custom.findViewById(R.id.editGroceryTextQuantity);
                quantityEdit.setText("" + selectedGrocery.getQuantity());
                final DatePicker editDate = (DatePicker) custom.findViewById(R.id.editDatePicker);
                String date = selectedGrocery.dateAsString();
                String[] dateParse = date.split("-");
                int year = Integer.parseInt(dateParse[0]);
                int day = Integer.parseInt(dateParse[2]);
                int month = Integer.parseInt(dateParse[1])-1;
                editDate.updateDate(year,month,day);
                Log.d("ContextMenu","" + editDate.getYear() + "-" + editDate.getMonth()
                        + "-" + editDate.getDayOfMonth());
                saveEdit = (Button)custom.findViewById(R.id.editSaveButton);
                cancelEdit = (Button)custom.findViewById(R.id.editCancelButton);
                saveEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedGrocery.setName(nameEdit.getText().toString());
                        selectedGrocery
                                .setQuantity(Integer.parseInt(quantityEdit.getText().toString()));
                        String newDate = "";
                        newDate = "" + editDate.getYear() + "-" + (editDate.getMonth()+1)
                            + "-" + editDate.getDayOfMonth();
                        Log.d("ContextMenu", newDate + " Is the Date");
                        selectedGrocery.setDateWithString(newDate);
                        db.updateGroceryItem(selectedGrocery);

                        displayToListView();
                        custom.cancel();
                    }
                });
                cancelEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        custom.cancel();
                    }
                });
                custom.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
        switch (item.getItemId()){
            case R.id.action_start:
                startService(new Intent(this,BackgroundNotifier.class));
                return true;
            case R.id.action_stop:
                stopService(new Intent(this,BackgroundNotifier.class));
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


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

                addGroceryItemToSet(grocery);

                displayToListView();
                custom.cancel();
            }
        });


        custom.show();
    }

    public void menuDialog(View view){


    }



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
     * This will read in from the database on opening and store into our allStoredItems
     */
    private void populateSetOnCreate(){
        allStoredItems.addAll(db.getAllGroceries());
    }
}


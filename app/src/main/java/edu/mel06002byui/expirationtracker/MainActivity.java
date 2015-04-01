package edu.mel06002byui.expirationtracker;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


public class MainActivity extends ActionBarActivity {
    private SharedPreferences settings;
    private SharedPreferences.Editor prefEditor;
    private static final String TAG_MAIN_ACTIVITY= "MainActivity";
    protected static final String APPLICATION_SETTINGS= "notifySettings";
    private Set<Grocery> allStoredItems = new TreeSet<>();
    BackgroundNotifier monitor;
    private AlertDialog.Builder dialogBuilder;
    private GrocerySQLiteHelper db = new GrocerySQLiteHelper(this);
    ListView lv;
    // holds the result of the information for the item, dependent of the UPC of said item
    public String resultsForItem = "No Data Found";

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = settings.edit();
        setContentView(R.layout.activity_main);
         lv = (ListView)findViewById(R.id.GroceryList);
        registerForContextMenu(lv);
        Log.i(TAG_MAIN_ACTIVITY, "Populating set");
        populateSetOnCreate();
        displayToListView();
        if(settings.getBoolean("firstStart", true)) {
            prefEditor.putBoolean("firstStart", false);
            prefEditor.putBoolean("notifyStatus", true);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.AM_PM,Calendar.AM);
            c.set(Calendar.HOUR, 9);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.MILLISECOND, 0);
            prefEditor.putLong("alarm_time_as_long",c.getTimeInMillis());
            Set<String> temp = new TreeSet<>();
            temp.add("saturday_valid");
            prefEditor.putStringSet("notify_days", temp);
            prefEditor.commit();

            startSchedule();
        }

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
        //MenuItem toggler = menu.findItem(R.id.toggle_notify);
        //boolean backgroundServiceStarted = settings.getBoolean("notifyStatus", true);
       // toggler.setChecked(backgroundServiceStarted);

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


        /*
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.toggle_notify:

                if(item.isChecked()){
                    Log.d("TIMECHAMBER", "Turned Off");
                    item.setChecked(false);
                    prefEditor.putBoolean("notifyStatus", false);
                    cancelSchedules();

                }
                else{
                    Log.d("TIMECHAMBER", "Turned On");
                    item.setChecked(true);
                    prefEditor.putBoolean("notifyStatus", true);
                    startSchedule();
                }
                prefEditor.commit();
                Log.d("OptionsMenu","After Check");
                return true;
            case R.id.time_status:
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"timePicker");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        */
        startActivityForResult(new Intent().setClass(this, SetPreferences.class), 13);

        return true;
    }



    private void startSchedule() {

        try {
            AlarmManager alarms = (AlarmManager) this
                    .getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(getApplicationContext(),
                    BackgroundAlarm.class);
            intent.putExtra(BackgroundAlarm.BACKGROUND_ALARM,
                    BackgroundAlarm.BACKGROUND_ALARM);

            final PendingIntent pIntent = PendingIntent.getBroadcast(this,
                    1234567, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar today = Calendar.getInstance();
            long alarmTime = settings.getLong("alarm_time_as_long", today.getTimeInMillis());
            Log.d("TIMECHAMBER", "SystemTime: " + System.currentTimeMillis());
            Log.d("TIMECHAMBER", "AlarmTime: " + today.getTimeInMillis());
            Log.d("TIMECHAMBER", "Difference: " + (today.getTimeInMillis()-System.currentTimeMillis()));
            alarms.setRepeating(AlarmManager.RTC_WAKEUP,
                    alarmTime, AlarmManager.INTERVAL_DAY, pIntent);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void cancelSchedules() {

        Intent intent = new Intent(getApplicationContext(),
                BackgroundAlarm.class);
        intent.putExtra(BackgroundAlarm.BACKGROUND_ALARM,
                BackgroundAlarm.BACKGROUND_ALARM);

        final PendingIntent pIntent = PendingIntent.getBroadcast(this, 1234567,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarms = (AlarmManager) this
                .getSystemService(Context.ALARM_SERVICE);

        alarms.cancel(pIntent);
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
    private Button scanButton;
    Dialog custom;
    public void addItem(View view){
        //Intent intent = new Intent(this, AddItem.class);
        // startActivity(intent);

        custom = new Dialog(MainActivity.this);

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

        scanButton = (Button) custom.findViewById(R.id.Scanner);
        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startScanner();

            }
        });


        custom.show();
        Log.i("ResultForItem", resultsForItem);
    }

    public void startScanner() {
        IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
        scanIntegrator.initiateScan();
    }

    /**
     *
     * @param requestCode default value
     * @param resultCode default value
     * @param intent default value
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    //retrieve scan result
        if(requestCode == 13){
            settings = PreferenceManager.getDefaultSharedPreferences(this);
            if(settings.getBoolean("notifyStatus",true)) {
                startSchedule();
            }
            else{
                cancelSchedules();
            }
            return;
        }
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,
                intent);
        if(scanningResult != null){
            try {
                String scanContent = scanningResult.getContents();

                Log.i("Scan Results", scanContent);
                String scanFormat = scanningResult.getFormatName();
                Log.i("Scan Format", scanFormat);
                HTMLParser(scanContent);
                String NoDataFound = "No Data Found";

                if(!resultsForItem.equals(NoDataFound)) {

                    EditText myTextBox = (EditText) custom.findViewById(R.id.nameText);
                    Log.i("inside scan button", resultsForItem);
                    myTextBox.setText(resultsForItem);

                } else {
                    Toast results = Toast.makeText(getApplicationContext(), "No Information Found",
                            Toast.LENGTH_LONG);
                    results.show();
                }

            } catch (Exception e){
                Log.i("Scanner Exception", e.toString() );
                Toast results = Toast.makeText(getApplicationContext(), " Scan Canceled ",
                        Toast.LENGTH_LONG);
                results.show();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * The HTMLParser will parse an HTML doc to extract the description and size/weight of the item
     * that is being searched
     * @param scanResults results from the bar code scanner
     */
    public void HTMLParser(final String scanResults) {

        Thread thread = new Thread(new Runnable(){

            @Override
            public void run() {
                String itemInfo;
                Document HTML;
                try {
                    HTML = Jsoup.connect("http://www.upcdatabase.com/item/" + scanResults)
                            .maxBodySize(0).timeout(600000).get();
                    Elements content = HTML.body().getElementsByClass("data");

                    if(!content.isEmpty()){
                        for (int i = 0; i < content.size(); i++){
                            Log.i("CONTENT " + i, content.get(i).text());
                        }

                        // if true then there is an item in website database
                        if(content.get(0).text().contains("UPC-A EAN/UCC-13 Description")) {

                            // various steps to split the string to get the item information
                            String[] parsedString = content.get(0).text().split("UPC-A EAN/UCC-13 Description");
                            Log.i("parsedString", parsedString[0]);
                            Log.i("parsedString", parsedString[1]);

                            String[] parsedString2 = parsedString[1].split("Size/Weight");
                            Log.i("ParsedString 2", parsedString2[0]);
                            Log.i("ParsedString 2", parsedString2[1]);

                            String[] parsedString3 = parsedString2[1].split("Issuing Country");
                            Log.i("ParsedString 3", parsedString3[0]);
                            Log.i("ParsedString 3", parsedString3[1]);
                            // assign itemInfo the information that was found
                            itemInfo = parsedString2[0] + parsedString3[0];
                            resultsForItem = itemInfo;

                            Log.i("results in HTMLParser", resultsForItem);
                        }
                        else {
                            resultsForItem = "No Data Found";
                        }
                    }

                } catch (Exception e) {
                   Log.i("HTML Exception", e.toString() );
                }
            }
        });


        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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



    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}


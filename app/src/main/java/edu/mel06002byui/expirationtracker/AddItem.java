package edu.mel06002byui.expirationtracker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class AddItem extends ActionBarActivity {
    private static final String TAG_ADD_ITEM = "AddItem";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

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

    public void CancelAdd(View view){
        finish();
    }

    public void finalAdd(View view){
        EditText nameEdit = (EditText) findViewById(R.id.nameText);
        EditText expEdit = (EditText) findViewById(R.id.nameText);
        EditText purEdit = (EditText) findViewById(R.id.nameText);
        EditText quantityEdit = (EditText) findViewById(R.id.nameText);
        String name = nameEdit.getText().toString();
        String expiration = expEdit.getText().toString();
        String purchased = purEdit.getText().toString();
        String quantity = quantityEdit.getText().toString();

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // create elements
            Document doc = docBuilder.newDocument();
            Element item = doc.createElement("item");
            Attr attr = doc.createAttribute("name");
            attr.setValue(name);
            item.setAttributeNode(attr); // place attributes into the element
            attr = doc.createAttribute("expirationDate");
            attr.setValue(expiration);
            item.setAttributeNode(attr); // place attributes into the element
            attr = doc.createAttribute("purchasedDate");
            attr.setValue(purchased);
            item.setAttributeNode(attr); // place attributes into the element
            attr = doc.createAttribute("quantity");
            attr.setValue(quantity);
            item.setAttributeNode(attr); // place attributes into the element

            // Get file
            String filepath = "c:\\file.xml";
            DocumentBuilderFactory xmlDocFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlDocBuilder = docFactory.newDocumentBuilder();
            Document xmldoc = xmlDocBuilder.parse(filepath);

            // get the root element
            Node grocery = xmldoc.getFirstChild();

            // add element to root element
            grocery.appendChild(item);



        }catch(ParserConfigurationException pce){
            Log.e(TAG_ADD_ITEM,"Nothing to parse",pce);
            pce.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG_ADD_ITEM,"IO",e);
            e.printStackTrace();
        }


    }
}


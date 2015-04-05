package nu.geeks.stockadventure;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends Activity {

    ListView list;
    ArrayList<Business> businesses;
    ArrayAdapter<Business> adapter;
    Random rand = new Random();

    int N = 100; //Number of stocks.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.listView);

        businesses = new ArrayList<Business>();

        fillList();

        adapter = new ArrayAdapter<Business>(this, android.R.layout.simple_list_item_1, android.R.id.text1, businesses);

        list.setAdapter(adapter);
    }

    private void fillList(){

        //Fill list.
        //TODO - better time complexity
        for(int i = 0; i < N; i++){
            String name = "Error";
            boolean uniqueName = false;

           //Make sure no duplicate names.
            while(!uniqueName) {
                uniqueName = true;
                name = "" + (char) (rand.nextInt(25) + 65) + (char) (rand.nextInt(25) + 65) + (char) (rand.nextInt(25 ) + 65);
                    for (int j = 0; j < i; j++) {
                    if(businesses.get(j).compareName(name)){
                       uniqueName = false; //loop through all names and compare them to this one.
                    }
                    }
            }

            int value = rand.nextInt(1000) + 10;

            Business b = new Business(name, value);
            businesses.add(b);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

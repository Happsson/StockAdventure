package nu.geeks.stockadventure;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends Activity {

    TextView tSelectedStock;
    TextView tTotalCash;
    TextView tBuy;

    SeekBar amountBar;
    Button bBuy, bSell, bNextDay;

    ListView list;
    ArrayList<Business> businesses;
    ArrayAdapter<Business> adapter;
    Random rand = new Random();

    Business selectedBusiness;

    int N = 100; //Number of stocks.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize all buttons and such
        initializeView();

        //Update the (not selected) business.
        updateSelectedBusiness();

        //Set the clickListeners
        initializeListeners();


    }

    private void initializeView(){

        tSelectedStock = (TextView) findViewById(R.id.tSelectedStock);
        tTotalCash = (TextView) findViewById(R.id.tMOwn);
        tBuy = (TextView) findViewById(R.id.tMBet);
        amountBar = (SeekBar) findViewById(R.id.seekBar);
        bBuy = (Button) findViewById(R.id.bBuy);
        bSell = (Button) findViewById(R.id.bSell);

        bNextDay = (Button) findViewById(R.id.bNextDay);

        list = (ListView) findViewById(R.id.listView);

        businesses = new ArrayList<Business>();

        fillList();
        //TODO - lista ut hur man får sublist
        adapter = new ArrayAdapter<Business>(this, android.R.layout.simple_list_item_2, android.R.id.text1, businesses);

        list.setAdapter(adapter);

    }

    private void initializeListeners(){

        amountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStocks();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(businesses.get(position).getName())
                        .setMessage(businesses.get(position).getInfo())
                        .setPositiveButton("Ok!", null)
                        .show();

                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedBusiness = businesses.get(position);
                updateSelectedBusiness();

            }
        });

    }

    private void updateStocks(){
        for(Business b : businesses){
            b.nextDay();
        }
        adapter.notifyDataSetChanged();
        updateSelectedBusiness();
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

            Business b = new Business(name, value, (rand.nextInt(9)+1));
            businesses.add(b);
        }
    }

    private void updateSelectedBusiness(){
        if(selectedBusiness == null){
            tSelectedStock.setText("Select a stock");
        }else{
            tSelectedStock.setText(selectedBusiness.getName() + " @ " + selectedBusiness.getValue() + " €");
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

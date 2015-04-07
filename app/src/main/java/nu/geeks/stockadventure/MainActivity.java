package nu.geeks.stockadventure;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    TextView tDay;

    CountDownTimer countdownTimer;

    long speed = 10000;
    int moneyToBuyFor;
    int balance;
    int progressBar;
    int days = 0;

    boolean gameWon = false;

    SeekBar amountBar;
    Button bBuy, bSell, bNextDay;

    ListView list;
    ArrayList<Business> businesses;
    ArrayAdapter<Business> adapter;
    Random rand = new Random();

    ArrayList<Business> stockPortfolio;

    Business selectedBusiness;

    int N = 20; //Number of stocks.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockPortfolio = new ArrayList<Business>();


        //Initialize all buttons and such
        initializeView();

        //Update the (not selected) business.
        updateSelectedBusiness();

        progressBar = 50;
        balance = 1000;
        updateBalance();
        updateMoneyToByFor();

        //Set the clickListeners
        initializeListeners();

        updateStocksNextDay();

        welcome(); //Print welcome message.


    }

    private void welcome(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Welcome to Stock Adventure!")
                .setMessage("Press and hold on a stock to get more info. Every day the stocks you " +
                        "own will be at the top of the list." +
                        "\n\nPress the menu-button" +
                        " on your phone to access your stock portfolio. \n\n" +
                        "Make 10 000 000 € to win the game! Good luck!")
                .setPositiveButton("Got it!", null)
                .show();
    }

    private void updateBalance(){
        tTotalCash.setText("Tot " + balance + " €.");

        if(balance > 10000000 && !gameWon){
            gameWon = true;
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("AWESOME!!!")
                    .setMessage("Oh happy days! You did it! You made " + balance + " € in " +
                            "just " + days + " days!")
                    .setPositiveButton("Keep playing!", null)
                    .setNegativeButton("Restart game!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetGame();
                        }

                    })
                    .show();

        }
    }

    private void resetGame(){

        //TODO - Is this all that needs to be done?
        balance = 1000;
        days = 0;
        progressBar = 50;
        selectedBusiness = null;
        businesses.clear();
        fillList();
        updateMoneyToByFor();
        updateBalance();
        updateSelectedBusiness();
        gameWon = false;


    }

    private void initializeView(){

        tSelectedStock = (TextView) findViewById(R.id.tSelectedStock);
        tTotalCash = (TextView) findViewById(R.id.tMOwn);
        tBuy = (TextView) findViewById(R.id.tMBet);
        amountBar = (SeekBar) findViewById(R.id.seekBar);
        bBuy = (Button) findViewById(R.id.bBuy);
        bSell = (Button) findViewById(R.id.bSell);

        tDay = (TextView) findViewById(R.id.tDay);
        bNextDay = (Button) findViewById(R.id.bNextDay);

        list = (ListView) findViewById(R.id.listView);

        businesses = new ArrayList<Business>();

        fillList();

        adapter = new ArrayAdapter<Business>(this, android.R.layout.simple_list_item_2, android.R.id.text1, businesses){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(businesses.get(position).toString());
                text2.setText(businesses.get(position).getUserOwn());

                return view;
            }
        };

        list.setAdapter(adapter);

        setCounter(speed);


    }

    private void setCounter(long time){

        if(countdownTimer != null) countdownTimer.cancel();
        if(time != 0) {
            countdownTimer = new CountDownTimer(time, 10) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    updateStocksNextDay();
                    start();
                }
            }.start();
        }
    }

    private void updateMoneyToByFor(){
        if(balance < 0 ){
            //User is broke
            tBuy.setText("Use 0 €(" + progressBar + "%)");
        }else{
            //User is not broke.
            double percent = progressBar*0.01;
            moneyToBuyFor = (int) (balance*percent);
            tBuy.setText("Use " + moneyToBuyFor + " €(" + progressBar +"%)");
        }
    }
    private void initializeListeners(){

        amountBar.setProgress(progressBar); //Set to 50.

        amountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar = progress;
                updateMoneyToByFor();
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
                updateStocksNextDay();
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

        bSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedBusiness!=null){
                    sell(selectedBusiness);
                }else{
                    Toast.makeText(getApplicationContext(), "Select a stock to sell!", Toast.LENGTH_LONG).show();
                }
            }
        });

        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedBusiness!= null){
                    buy(selectedBusiness);
                }else{
                    Toast.makeText(getApplicationContext(), "Select a stock to buy!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void buy(Business business){
       if(moneyToBuyFor >= business.value) {
           balance -= business.buyStock(moneyToBuyFor);
           updateBalance();
           updateMoneyToByFor();
           if (business.userOwn > 0)
               stockPortfolio.add(business); //If user could afford it, add to list.
           adapter.notifyDataSetChanged(); //update listView
       }else if(balance >= business.value){
           Toast.makeText(getApplicationContext(), "You didn't use enough money.", Toast.LENGTH_LONG).show();
       }else{
            Toast.makeText(getApplicationContext(), "You can't afford this stock.", Toast.LENGTH_LONG).show();

        }

    }

    private void sell(Business business){
        if(business.userOwn==0){
            Toast.makeText(getApplicationContext(), "You don't own any shares of this stock.", Toast.LENGTH_LONG).show();
        }else{
           balance += business.sell();
            stockPortfolio.remove(business);
            updateBalance();
            updateMoneyToByFor();
            adapter.notifyDataSetChanged();
        }
    }

    private void updateStocksNextDay(){
        days++;
        tDay.setText("Day " + days);
        for(Business b : businesses){
            b.nextDay();
        }
        BusinessComparator comp = new BusinessComparator();

        adapter.sort(comp);
        adapter.notifyDataSetChanged();

        //list.setSelectionAfterHeaderView();

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
        // All menu items are added in the xml, not here.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //TODO - this is not where this should be done. Find out why stockPortfolio holds empty stocks
        for(Business b : stockPortfolio){
            if(b.userOwn == 0) stockPortfolio.remove(b); //Remove stocks not own.
        }

        //handle menu button presses.
        if (id == R.id.stockPortfolio) {
            if(stockPortfolio == null || stockPortfolio.size() == 0){
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("You don't own any stocks at the moment. Buy some stocks and" +
                                " you'll be able to keep track of them here!")
                        .setPositiveButton("Ok!", null)
                        .show();
            }else{
                StringBuilder stocks = new StringBuilder();
                stocks.append("Your stocks: \n");
                for(Business b : stockPortfolio){
                    stocks.append(b.name + " @ " + b.value + ". You bought " + b.userOwn + " shares" +
                            " for " + b.invested + " €. Net profit " + ((b.value*b.userOwn) - b.invested)+" €.\n\n");
                }
                String print = stocks.toString();

                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(print)
                        .setPositiveButton("Cool!", null)
                        .show();
            }

            return true;
        }


        /*
        TODO - this is in opposite order. Also should be changed to days per sec. Also
        needs to be implemented. :)
         */
        else if(id == R.id.decreaseSpeed){
            speed += 250;
            double speedSec = speed / 1000d;
            setCounter(speed);
            Toast.makeText(getApplicationContext(), "Speed is now " + speedSec + " sec per day", Toast.LENGTH_LONG).show();
        }else if(id == R.id.increaseSpeed){
            if((speed-250) > 0 ){
                speed -= 250;
                double speedSec = speed / 1000d;
                setCounter(speed);
                Toast.makeText(getApplicationContext(), "Speed is now " + speedSec + " sec per day", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getApplicationContext(), "Speed is zero, change day with button", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}

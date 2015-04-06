package nu.geeks.stockadventure;

import java.util.Comparator;
import java.util.Random;

/**
 * Created by hannespa on 15-04-05.
 */
public class Business{

    Random rand;
    String name;    //Stock name
    int value, previousValue;  //cost per share
    int riskFactor; //how risky the stock is between 1 and 10, 10 beeing riskiest
    int day; //Days the stock has been active.
    int userOwn; //number of shares
    boolean stockDead;
    int invested;

    /**
     * Constructor for a business. Set name, initial value/share and risk factor.
     *
     * @param name name of company
     * @param value price per share
     * @param riskFactor risk, 1 to 10. 1 being lowest risk and 10 being highest
     */
    public Business(String name, int value, int riskFactor) {
        this.name = name;
        this.value = value;
        this.riskFactor = riskFactor;
        previousValue = 0;
        day = 0;
        rand = new Random();
        userOwn = 0;
        stockDead = false;
        invested = 0;
    }

    /**
     * Get a string on the form "You own xx shares worth yy €."
     *
     * @return a string
     */
    public String getUserOwn(){
        return "You own " + userOwn + " shares worth " + (userOwn*value) + " €.";
    }

    /**
     * Buy stock for the given amount.
     * Calculates how many shares the amount can buy, and returns how much it costs.
     *
     * @param amount amount to buy for
     * @return amount bought for
     */
    public int buyStock(int amount){

        if(value != 0) {
            int numShares = (amount / value); //Number of shares the money will buy.
            userOwn = numShares;
            invested += numShares*value; //Update amount invested in this stock
            return (numShares*value); //return what's left.
        }else {
            return 0;
        }
    }

    /**
     * Get number of shares of this stock that the user owns.
     *
     * @return
     */
    public int getUserShares(){
        return userOwn;
    }

    /**
     * Update the value of the stock based on its risk factor. Incremets the day.
     * If the stock is dead, the value will always be 0.
     *
     */
    public void nextDay(){

        //TODO - this doesnt really work. To random, and big stocks gets to big.

        previousValue = value;
        day++;

        int riskValue = rand.nextInt(20);

        if(!stockDead) {
            int val = (value * riskFactor/100)+1; //make sure val isn't 0.
            if(val > 0) { //This is a bad fix, somethime val get bigger thatn INTEGTER.MAX_VALUE and flips.
                if (riskValue < riskFactor) { //Higher risk with risky stocks
                    value -= rand.nextInt(val);
                } else {
                    value += rand.nextInt(val);
                }
            }
        }

        if(value <= 1){
            stockDead = true;
            value = 0;
        }


    }

    /**
     * Check if the name of this stock / business is the same as that stock / business.
     *
     * @param that business / stock name.
     * @return true if identical.
     */
    public boolean compareName(String that){
        if(this.name.equals(that)) return true;
        else return false;
    }

    /**
     * Get current value of stock (price per share)
     *
     * @return value of this stock / business
     */
    public int getValue(){
        return this.value;
    }

    /**
     * Get the name of this stock / business
     * @return String name of the stock / business.
     */
    public String getName(){
        return this.name;
    }

    /**
     * How much the stock share price has changed since last day.
     *
     * @return int the change
     */
    public int getChangeLastDay(){
        if(day == 0) {
            return 0;
        }else {
            return value - previousValue;
        }
    }

    /**
     * Get the change as a string, where a positive value will have a '+' and a negative value
     * will have a '-'.
     *
     * @return
     */
    public String getChangeLastDayString(){
        int change = getChangeLastDay();

        if(change >= 0){
            return "+" + change;
        }else{
            return "" + change;
        }
    }

    /**
     * Get info about the stock in this format
     *
     *      "Name
     *          Risk factor: XX
     *          Current value: YY
     *          Change since yesterday: ZZ
     *          You have invested LL €
     *          Net profit: TT €.
     *         "
     *
     * @return String info
     */
    public String getInfo(){

        return name +
                "\n Risk factor: " + riskFactor +
                "\n Current value: " + value +
                "\n Change since yesterday: " + getChangeLastDayString() +
                "\n You have invested " + invested + " €." +
                "\n Net profit: " + ((userOwn*value) - invested) +" €.";

    }


    public int sell(){
        int investment = userOwn*value;
        userOwn = 0;
        invested = 0;
        return investment;

    }

    /**
     * if the stock is not dead, this returns on the format:
     *  "Name (XX €/share)"
     *  else it returns "Dead stock!"
     *
     * @return
     */
    @Override
    public String toString() {
        if(!stockDead) {
            return "" + name + " (" + value + " €/share)";
        }else{
            return "Dead stock!";
        }
    }
}

class BusinessComparator implements Comparator<Business> {
    @Override
    public int compare(Business o1, Business o2) {
        return o1.userOwn > o2.userOwn ? -1 : o1.userOwn == o2.userOwn ? 0 : 1;
    }
}
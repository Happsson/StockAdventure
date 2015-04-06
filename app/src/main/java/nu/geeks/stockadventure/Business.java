package nu.geeks.stockadventure;

import java.util.Random;

/**
 * Created by hannespa on 15-04-05.
 */
public class Business {

    Random rand;
    String name;
    int value, previousValue;
    int riskFactor;
    int day;
    int userOwn; //number of shares
    boolean stockDead;
    int[] previousValues;

    public Business(String name, int value, int riskFactor) {
        this.name = name;
        this.value = value;
        this.riskFactor = riskFactor;
        previousValue = 0;
        day = 1;
        rand = new Random();
        userOwn = 0;
        stockDead = false;
//        for(int i = 0; i < 10; i++) previousValues[i] = 0;
    }

    public String getUserOwn(){
        return "You own " + userOwn + " shares worh " + (userOwn*value) + " €.";
    }

    /**
     * Buy stock for the given amount.
     * Calculates how many shares the amount can buy, and returns how much it costs.
     *
     * @param amount amount to buy for
     * @return amount bought for
     */
    public int buyStock(int amount){
        //TODO - this.
        return 0;
    }

    public int getUserShares(){
        return userOwn;
    }

    public void nextDay(){
        previousValue = value;
        day++;

        int riskValue = rand.nextInt(20);

        if(!stockDead) {
            int val = (value * riskFactor/10)+1;
            if (riskValue < riskFactor) { //Higher risk with risky stocks
                value -= rand.nextInt(val);
            } else {
               value += rand.nextInt(val);
            }
        }

        if(value <= 1){
            stockDead = true;
            value = 0;
        }


    }

    public boolean compareName(String compare){
        if(this.name.equals(compare)) return true;
        else return false;
    }

    public int getValue(){
        return this.value;
    }

    public String getName(){
        return this.name;
    }

    public int getChangeLastDay(){
        return value - previousValue;
    }

    public String getChangeLastDayString(){
        int change = getChangeLastDay();

        if(change >= 0){
            return "+" + change;
        }else{
            return "" + change;
        }
    }

    public String getInfo(){

        return name +
                "\n Risk factor: " + riskFactor +
                "\n Current value: " + value +
                "\n Change since yesterday: " + getChangeLastDayString();
    }

    @Override
    public String toString() {
        if(!stockDead) {
            return "" + name + " (" + value + " €/share)";
        }else{
            return "Dead stock!";
        }
    }
}

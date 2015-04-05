package nu.geeks.stockadventure;

/**
 * Created by hannespa on 15-04-05.
 */
public class Business {

    String name;
    int value;

    public Business(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public boolean compareName(String compare){
        if(this.name.equals(compare)) return true;
        else return false;
    }
    @Override
    public String toString() {
        return  "" + name + "("+ value + " €/share)";
    }
}

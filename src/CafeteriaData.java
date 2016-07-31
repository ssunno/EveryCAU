import java.util.*;

public class CafeteriaData {
	ArrayList<String> mealsection = new ArrayList<String>();
    ArrayList<String> Time = new ArrayList<String>();
    //ArrayList<String> calorie = new ArrayList<String>();
    ArrayList<String> mealprice = new ArrayList<String>();
    ArrayList<String>[] menu= (ArrayList<String>[])new ArrayList[25];
    CafeteriaData(){
    	for(int i = 0 ; i < 25 ; i++){
    		menu[i] = new ArrayList<String>();
    	}
    }
	
}

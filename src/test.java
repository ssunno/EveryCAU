import java.text.SimpleDateFormat;
import java.util.*;

public class test {

	public static void main(String[] args) throws Exception {

		CafeteriaManager cafe = new CafeteriaManager();
		CafeteriaData cd = new CafeteriaData();
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String store = "308";
		String val = null;
		if(store.equals("308")){
			val = "08";
		}
		cd = cafe.getData(sdf.format(d), val);
		for(int i = 0 ; i < cd.mealprice.size();i++){
			System.out.println(d);
			System.out.println(cd.mealsection.get(i));
			System.out.println(cd.mealprice.get(i));
			System.out.println(cd.Time.get(i));
			for(int j = 0;j<cd.menu[i].size();j++){
				System.out.println(cd.menu[i].get(j));
			}
			
		}
		
	}
}
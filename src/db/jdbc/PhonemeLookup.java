package db.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.GenericDB;
import db.GenericfDBFactory;
import db.jdbc.beans.PhonemeType;

/** 
 * 
 * The phonemes are fetched from the database and placed in a static Map at load time. The application thereafter gets 
 * phoneme data from the Map rather than using expensive database each time. 
 * 
 * This class assumes usage of the GenericDB framework.
 * 
 * @author Jimmye
 *
 */
public class PhonemeLookup {
	
	static Map<String,PhonemeType> map = new HashMap<String,PhonemeType>();
	
	static {
		GenericDB db = GenericfDBFactory.getDB();
		db.openSU();
		List<Object>list = db.selectRows( new PhonemeType(), "");
		for (Object o : list){
			PhonemeType pho = (PhonemeType)o;
			map.put( pho.getId(), pho);
		}
		db.close();
	}
	
	static public PhonemeType get( String pho){
		return map.get(pho);
	}

}

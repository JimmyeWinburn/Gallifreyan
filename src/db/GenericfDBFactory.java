
package db;

import db.jdbc.JDBCconnection;


/**
 * 
 * Creates a new database access object based on an inputed string.
 * Alternatively, a default JDBC connection will be returned.
 * 
 * @author jwinburn
 *
 */
public class GenericfDBFactory {

	/**
	 * return a database connection based on the inputed string.
	 * If "null" is entered, the default JDBC connection is returned.
	 * 
	 * current acceptable strings are:
	 * JDBC 
	 * 
	 * 
	 * @param type
	 * @return
	 */
	static public GenericDB getDB( String type){
		if (type.equals("JDBC")){
			return new JDBCconnection();
		} else {
			return new JDBCconnection();
		}
	}

	/**
	 * return a default database connection, currently JDBC. 
	 * 
	 * @param type
	 * @return
	 */
	static public GenericDB getDB(){
		return new JDBCconnection();
	}

	
	
}


package db;

import java.util.List;


/**
 * interface for specifying methods for database access and manipulation.  There is no need to know about the underlying 
 * database access methods.  This interface is designed to be flexible.  Modules can be built for JDBC or Hibernate or 
 * any other database access, and the user would never need to deal with the complexities of those frameworks.  (Note: currently 
 * only the JDBC module has been implemented.)  
 * 
 * The framework supplies the benefits of ORMs (being able to view data rows as objects) without ORM problems (objects specified
 * in multiple locations and breaking when the database changes.)
 * 
 * The module uses the "bridge" design pattern.  The chances of building tightly coupled classes is mitigated.   
 * 
 * Database tables may be accessed and manipulated as objects.  All that is needed is a bean that specifies 
 * the name of the database table, the fields of the database, and getters/setters for those fields.  E.g. 
 * 
 * public class SM_BATCH_JOB {
 * 
 * 		private Long ID;
 * 		
 * 		private Long getID(){
 * 			return ID;
 * 		}
 * 		
 * 		private void setID( Long id){
 * 			ID = id;
 * 		}
 * }
 * 
 * Currently, members should be defined as either Long, String, or Timestamp.  Define numeric members as Long if they can contained nulls.  Define them as "long"
 * ONLY if they cannot contain nulls.       
 *  
 * The bean should also override toString(), hashCode(), and equals() for collections to be used efficiently.
 * 
 * Because the PaymonDB framework does not use annotations to define table names or field names, the table names and field names 
 * must be defined exactly as they as they appear in the database. (although either upper and lower case letters will be all
 * right.)  For example, if the database has a "DB_PROPERTIES" table and if that table has numeric and string fields that are defined as "ID" and "NAME", 
 * the bean would have to be specified as:
 * 
 * public class DB_PROPERTIES {
 * 		private long ID;    // long because it cannot contain a null
 * 		private String NAME;
 * }  
 * 
 * 
 * Here is an example of how to use this framework:
 * 
 * public class Test {
 * 
 *     public void Main(String args[]){
 *     
 *     	// Use the factory to get an instance of PaymonDB.  If you wish to use an instance different from the 
 *     	// default of JDBC for the Oracle Database "PAYMON", get an instance of the Factory with a different driver
 *     	// e.g. PaymonDB.getDB("HIBERNATE-ACHMON");
 *     
 *     	// Open access to the database with the role of DBA.
 *     	PaymonDB db = PaymonDB.getDBA();    
 *     		
 *      // insert a row
 *     	AvlMetricConfig avl = new AvlMetricConfig();
 *		avl.setID( 100000L);
 *		avl.setACTIVE(0L);
 *		List<Object> lst = new ArrayList<Object>();
 *		lst.add(avl);
 *		db.insertRows(lst);
 *       
 *      // fetch the row back. 
 *		List<Object> rows = db.selectRows( new AvlMetricConfig(), "where id > 1000" );
 *		for (Object row: rows){
 *			AvlMetricConfig avl = (AvlMetricConfig)row;
 *			System.out.println( avl);  // assumes that toString() has been defined for this bean.
 *		}
 *
 *		db.close();
 *	}
 *}
 *
 *  Drivers must be written to allow access to other databases or other database access frameworks.  Drivers must implement all the 
 *  methods and follow the contract of this interface. 
 *	
 * @author jwinburn  03/04/14
 * @revision  jwinburn 03/31/14
 * 		-no need for the special field that defines the tablename.  We can use the name of the class instead.
 * 		-fields can now be defined as private to maintain the object oriented paradigm.
 *
 */

public interface GenericDB {
	
	
	/**
	 * Initializes the database access.  This must be called before any of the other methods of an implementing 
	 * class or those methods will cause a null pointer error.  Access will be granted with the role of DBA.
	 * 
	 */
	void openDBA();
	
	
	/**
	 * Initializes the database access like openDBA, but access will be granted with the role of superuser.
	 * 
	 */
	void openSU();
	
	
	/**
	 * closes database access.  No method but open() may be called after this.  Calling close() is optional, but 
	 * recommended.
	 */
	void close();
	
	
	/**
	 * inserts rows from the list into the database table.  The list should contain objects of ONE table only.
	 * This method is optimized for entering multiple rows.  Single rows should be inserted with the insertRow() method. 
	 * @param obj  - an instance of a bean.
	 * @return  int - the number of rows inserted.
	 */
	int insertRows(List<Object> obj);
	
	
	/**
	 * inserts a single database table bean into the database.
	 * @param obj - the single bean instance to insert.
	 * @return int - 1 if the insert was successful.  0 if not successful.
	 */
	int insertRow( Object obj);
	
	
	/**
	 * Selects a list of beans of a single database table.  The beans are
	 * returned a a list of objects.  When iterating through this list,
	 * each object must be cast to the appropriate type to restore the bean.
	 * 
	 * @param obj
	 * @param where
	 * @return List<Object>  - the list will be empty if there were no 
	 *                           rows that satisfied the predicate or there
	 *                           was an error.
	 */
	List<Object> selectRows(Object obj, String where);
	
	
	/**
	 * Like selectRows() except that a single Object will be returned.  If
	 * there are more than one rows that satisfy the where clause, the first
	 * one will be returned.  Note that the Object returned can be cast as the 
	 * appropriate type.
	 * 
	 * @param obj
	 * @param where
	 * @return
	 */
	Object selectRow(Object obj, String where);
	
	
	
	/**
	 * update 0 or more rows of an arbitrary type.  The sql clause must begin with "set".
	 *   
	 * @param o  - an instance of the class to be modified. 
	 * @param sql
	 * @return
	 */
	int updateRows( Object o, String sql);
	
	
	/**
	 *   Deletes row from an arbitrary type based on the where clause.  
	 *   The where clause must begin with "where".  If the where clause
	 *   is specified as "", all the rows of the table will be deleted.
	 *   
	 * @param o
	 * @param where
	 * @return
	 */
	int deleteRows( Object o, String where);
}

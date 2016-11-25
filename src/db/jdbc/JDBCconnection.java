package db.jdbc;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;

import db.GenericDB;


/**
 * Implement of GenricDB interface
 * 
 * @see db.GenericDB.java 
 *
 * @author jwinburn
 *
 */
public class JDBCconnection implements GenericDB {

	private  Connection conn = null;




	@Override
	public void openDBA() {
		if (conn == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (Exception e) {
				System.out.println( "no driver");
			}
			
			try {
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gallifreyan",
		            "root", ".Morgan1066");
			} catch (Exception e) {
				System.out.println("error setting up connection: " + e.getMessage());
			}
			
		}


	}
	

	@Override
	public void openSU() {
		openDBA();
	}
	
	

	@Override
	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public int insertRows(List<Object> rows) {
		boolean currentAutoCommit = true;
		int result = 0;

		assert conn != null;
		assert rows != null;

		try {
			currentAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit( false);

			// get the fields of the object (private and public)
			Class<?> c = rows.get(0).getClass();		
			String tableName = c.getSimpleName();
			Field[] fields =  this.getDeclaredFields( c);


			// get prepared statement
			String prepared = "insert into " + tableName + " (";
			for (int i=0; i< fields.length; i++){
				prepared += fields[i].getName();
				if (i<fields.length -1) {
					prepared += ",";
				}
			}
			prepared += ") values (";
			for (int i=0; i< fields.length; i++){
				prepared += "?";
				if (i<fields.length -1) {
					prepared += ",";
				}

			}			
			prepared += ")";
			PreparedStatement ps = conn.prepareStatement( prepared);
			//out.println( prepared);

			// enter the fields of the prepared statement for all rows.
			for ( Object r: rows){
				for (int i=0; i< fields.length; i++){
 					if ( fields[i].getGenericType().toString().equals("long")){
						ps.setLong( i+1, fields[i].getLong( r));
 					} else if ( fields[i].getGenericType().toString().equals("int")){
						ps.setInt( i+1, fields[i].getInt( r));
					} else if (fields[i].getGenericType().toString().contains("String")){
						if (fields[i].get( r)==null){
							ps.setNull( i+1, java.sql.Types.VARCHAR );
						} else {
							ps.setString( i+1, (String) fields[i].get( r));
						}

					} else if (fields[i].getGenericType().toString().contains("Long")){
						if (fields[i].get( r)==null){
							ps.setNull( i+1, java.sql.Types.INTEGER );
						} else {
							ps.setLong( i+1, (Long) fields[i].get( r));
						}
					} else if (fields[i].getGenericType().toString().contains("Double")){
						if (fields[i].get( r)==null){
							ps.setNull( i+1, java.sql.Types.DOUBLE );
						} else {
							ps.setDouble( i+1, (Double) fields[i].get( r));
						}
					} else if (fields[i].getGenericType().toString().contains("Timestamp")) {
						if (fields[i].get( r)==null){
							ps.setNull( i+1, java.sql.Types.TIMESTAMP );
						} else {
							ps.setTimestamp( i+1, (Timestamp)fields[i].get(r));					
						}
					}
				}
				result += ps.executeUpdate();


			}
			ps.close();
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
			System.out.println("Exception in insertRow(): " + e.getMessage());
		} finally {
			try {
				if (conn != null){
					conn.setAutoCommit(currentAutoCommit);
				}
			} catch (SQLException e) {
			}
		}

		return result;		
	}
	
	
	
	@Override
	public int insertRow( Object row){
		assert row != null;
		
		List<Object> arr = new ArrayList<Object>();
		arr.add(row);
		return insertRows( arr);
	}

	

	@Override
	public List<Object> selectRows(Object obj, String where) {

		assert obj != null;
		

		// get ALL the fields of the bean.
		Class<?> c = obj.getClass();	
		String tableName = c.getSimpleName();
		Field[] fields =  this.getDeclaredFields( c);

		// build the list from the fetched rows.
		List<Object> arrVO = new ArrayList<Object>();
		try {
			Statement st = conn.createStatement();
			String sql = "select * from " + tableName  + " " + where;
			ResultSet rs = st.executeQuery( sql);
			while (rs.next()){
				Object objVO = obj.getClass().newInstance();
				for (int i=0; i< fields.length; i++){
					if ( fields[i].getGenericType().toString().equals("long")){
						fields[i].setLong(objVO, rs.getLong(i+1)  );
					} else if ( fields[i].getGenericType().toString().equals("int")){
							fields[i].setInt(objVO, rs.getInt(i+1)  );
					} else if ( fields[i].getGenericType().toString().contains("Long")){
						Long l = rs.getLong(i+1);
						if (rs.wasNull()){
							fields[i].set(objVO, null);							
						} else {
							fields[i].set(objVO, l);
						}
					} else if ( fields[i].getGenericType().toString().contains("Double")){
						Double l = rs.getDouble(i+1);
						if (rs.wasNull()){
							fields[i].set(objVO, null);							
						} else {
							fields[i].set(objVO, l);
						}
					} else if (fields[i].getGenericType().toString().contains("String")){
						String s = rs.getString(i+1);
						if (rs.wasNull()){
							fields[i].set(objVO, null);							
						} else {
							fields[i].set(objVO, s);
						}
					} else if (fields[i].getGenericType().toString().contains("Timestamp")) {
						Timestamp t = rs.getTimestamp(i+1);
						if (rs.wasNull()){
							fields[i].set(objVO, null);							
						} else {
							fields[i].set(objVO, t);
						}
					}
				}
				arrVO.add(objVO);
			}
			rs.close();
			st.close();
			//System.out.println("found " + arrVO.size() + " objects from query.");
		}catch (Exception e){
			System.out.println("Error in selectRows: " + e.getMessage());
		} 
		
		return arrVO;
	}

	
	
	@Override
	public Object selectRow(Object obj, String where){
		List<Object> arr = selectRows( obj, where);
		if (arr.size()>0){
			return arr.get(0);
		} else {
			return null;
		}			
	}

	
	@Override
	public int updateRows(Object o, String predicate) {
		int updatedRows = 0; 

		try {
			Statement st = conn.createStatement();
			String tableName = o.getClass().getSimpleName();
			String sql = "Update " + tableName +  " " + predicate;
			updatedRows = st.executeUpdate( sql); 
			st.close();
		} catch (Exception e) {
			System.out.println("Exception in update(): " + e.getMessage());
		}
		return updatedRows;		
	}
	

	
	
	@Override
	public int deleteRows(Object o, String where) {
		int deletedVOs = 0; 
		assert o != null;
		
		try {
			Statement st = conn.createStatement();
			String tableName = o.getClass().getSimpleName();
			String sql  = "delete from " + tableName +  " " + where;
			//System.out.println( "sql = " + sql);
			deletedVOs = st.executeUpdate( sql); 
			st.close();
		} catch (Exception e) {
			System.out.println("Exception in delete(): " + e.getMessage());
		}
		return deletedVOs;		
	}
	
	
	private Field[] getDeclaredFields( Class<?> c){
		Field[] fields = c.getDeclaredFields();
		for (Field f: fields){
			f.setAccessible(true);
		}
		return fields;
	}


}





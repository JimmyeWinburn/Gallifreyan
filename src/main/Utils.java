package main;


public class Utils {
	
	//I am a convert
	/** 
	 * a representation of 2 * PI
	 */
	public static double TAU = Math.PI * 2.0;
	
	
	/**
	 * tests whether a string is anything other than a valid string.
	 * @param string
	 * @return boolean
	 */
	public boolean isEmpty(String string){
		return (string!=null && string.trim().equals(""));
	}


}

package main;

import java.awt.Color;

import db.jdbc.beans.ParameterType;


// Driver to test the application
// This will not be needed when we plug the method into an actual UI but it
// highlights the demarcation between the presentation and the backend. 
//
// see Gallifreyan.java
public class Driver {
		
	public static void main(String[] args) throws Exception {

		ParameterType param = new ParameterType();
		param.setName(            "Tester");
		param.setBackgroundColor( Color.black);
		param.setForegroundColor( new Color( 120,255,0));
		param.setStrokeSize(      3);    
		param.setGlyphSize(       1000);
		param.setFrameSize(       1000);
	
		Gallifreyan component = new Gallifreyan( param); 

		component.writeToFile( "c:\\Gallifreyan\\" + param.getName() + ".png", "png");	
		component.display( param.getName(), param.getFrameSize());
		
	}

}

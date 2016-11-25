package main;



import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * draws a vowel to the graphics frame.
 * 
 * @author Jimmye
 *
 */
public class Vowel {
	
	private String 	id;
	private Long 	vowelPosition;
	private Long	vowelLineType;
	
	

	/**
	 * Draw a vowel.  The position of the vowel depends on whether it is associated with a consonent or not.   
	 * @param gr
	 * @param vowel
	 * @param withConsonant
	 * @param vowelCenterX
	 * @param vowelCenterY
	 */
	public static void drawVowel( Graphics2D gr, String vowel, boolean withConsonant, int vowelCenterX, int vowelCenterY ){
		int vowelX = vowelCenterX;
		int vowelY = vowelCenterY;
		double angle = 1.0;
		Vowel v = (Vowel)Gallifreyan.db.selectRow( new Vowel(), "where id like '" + vowel + "'" );
		if (withConsonant){
			switch (v.vowelPosition.intValue()){
				case 1: {  // outer
					vowelY = (int)(Gallifreyan.mainRadius * 1.20);
					break;
				}
				case 2: {   // inner				
					vowelY = (int)(vowelY * 1.00);
					break;					
				}
				case 3: { // on		
					angle = 0.9;
					vowelX = 0 - (int)(Gallifreyan.consonantRadius * Math.sin( Utils.TAU * angle));
					vowelY = vowelY - (int)(Gallifreyan.consonantRadius * Math.cos( Utils.TAU * angle));
				}
			}
		} else { // no consonant
			switch (v.vowelPosition.intValue()){
				case 1: {  //outer
					vowelY = (int)(Gallifreyan.mainRadius * 1.20);
					break;
				}
				case 2: {  // inner
					vowelY = (int)( vowelY * 0.8);
					break;					
				}
				case 3: {  //on
					vowelY = (int)(Gallifreyan.mainRadius * 1.0);
					break;					
				}
			}
		}

		Ellipse2D e1 = new Ellipse2D.Double( 
				vowelX - Gallifreyan.vowelRadius, 
				vowelY - Gallifreyan.vowelRadius, 
				Gallifreyan.vowelRadius * 2, 
				Gallifreyan.vowelRadius * 2	
				);
		gr.draw(e1);

		// draw vowel line
		drawVowelLines( gr, vowelCenterY, v.vowelLineType.intValue());
	}
	 
	
	// draw any lines associated with the vowel.
	private static void drawVowelLines( Graphics2D gr, int consonantCenterY, int lineType){
		switch (lineType) {
			case 1: {
				return;
			}
			case 2: {
				Line2D l1 = new Line2D.Double( 
						0, 
						consonantCenterY - Gallifreyan.vowelRadius, 
						0, 
						consonantCenterY - Gallifreyan.consonantRadius);
				gr.draw(l1);	
				return;
			}
			case 3: {
				Line2D l1 = new Line2D.Double( 
						0, 
						consonantCenterY + Gallifreyan.vowelRadius, 
						0, 
						consonantCenterY + Gallifreyan.consonantRadius);
				gr.draw(l1);	
				break;
			}
		}
			
	}

	
	

}

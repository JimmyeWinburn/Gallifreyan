package main;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * bean class with a method for drawing a consonant onto the graphics frame.
 * 
 * @author Jimmye
 *
 */
public class Consonant {
	
	private String id;
	private Long   idCircleType;
	private Long   numLines;
	private Long   dots;
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getIdCircleType() {
		return idCircleType;
	}

	public void setIdCircleType(Long idCircleType) {
		this.idCircleType = idCircleType;
	}

	public Long getNumLines() {
		return numLines;
	}

	public void setNumLines(Long numLines) {
		this.numLines = numLines;
	}

	public Long getDots() {
		return dots;
	}

	public void setDots(Long dots) {
		this.dots = dots;
	}


	
	
	/**
	 *  draw a consonant.  This can be associated with a vowel or not. 
	 *  
	 * @param gr
	 * @param consonant
	 * @param centerY
	 * @param centerX
	 * @param tauRadians
	 * @return
	 */
	public static int drawConsonant( Graphics2D gr, String consonant, int centerY, int centerX, double tauRadians){
		
		Consonant c = (Consonant)Gallifreyan.db.selectRow( new Consonant(), "where id like '" + consonant + "'" );
		
		gr.setColor( Gallifreyan.foregroundColor);
		gr.setStroke( new BasicStroke(Gallifreyan.strokeSize));

		// determine a consonant's center point and draw it.
		int circleType =  c.getIdCircleType().intValue();
		int consonantCenterY = Gallifreyan.mainRadius;
		switch (circleType){
		case 1: {
			consonantCenterY =  (int)(consonantCenterY *  .45);
			drawCresentCircle( gr, centerY, consonantCenterY, tauRadians);
			break;
		}
		case 2: {
			consonantCenterY =  (int)(consonantCenterY * .20);
			drawFullCircle( gr, centerY, consonantCenterY, tauRadians);
			break;
		}
		case 3: {
			consonantCenterY = (int)(consonantCenterY * 1.0);
			drawHalfCircle( gr, centerY, consonantCenterY, tauRadians);
			break;
		}
		case 4: {
			consonantCenterY = (int)(consonantCenterY * .85);
			drawHorizonCircle( gr, centerY, consonantCenterY, tauRadians);
			break;
		}
		case 5: {		
			consonantCenterY = (int)(consonantCenterY * .40);
			drawTouchingCircle( gr, centerY, consonantCenterY, tauRadians);
			break;
		}
		}

		// reset the drawing parameters.  Drawing the circles might have changed them.
		gr.setBackground( Gallifreyan.backgroundColor);
		gr.setColor( Gallifreyan.foregroundColor);
		gr.setStroke( new BasicStroke(Gallifreyan.strokeSize));

		// draw the dots of the consonant
		drawDots( gr, consonantCenterY, c.getDots().intValue());

		// draw the lines of the consonant
		drawLines( gr, consonantCenterY, c.getNumLines().intValue());
		
		return consonantCenterY;
	}
	
	
	// draw any consonant lines.  These lines go from the consonant circle across to the main circle.
	private static void drawLines( Graphics2D gr, int consonantY, int num){

		double[] angles = {};
		switch (num){
			case 1: {
				angles = new double[]{0.50};
				break;
			}
			case 2: {
				angles = new double[]{0.53, 0.47};
				break;
			}
			case 3: {
				angles = new double[]{0.55, 0.5, 0.45};
				break;
			}
		}
		
		Shape oldClip = gr.getClip();
		Area area = getClipCircle( consonantY);	
		gr.clip( area);
				
		for (double angle : angles) {
			double largeX = Gallifreyan.mainRadius * Math.sin( Utils.TAU * angle);
			double largeY = Gallifreyan.mainRadius * Math.cos( Utils.TAU * angle);	
			Line2D l1 = new Line2D.Double( 0 , consonantY, largeX, largeY);
			gr.draw(l1);	
		}		
		gr.setClip( oldClip);	
	}
	
	
	// get the clip area so that part of the lines in the middle of consonant circle are not visible.
	private static Area getClipCircle( int consonantY){
		Ellipse2D e1 = new Ellipse2D.Double(
				-Gallifreyan.mainRadius, 
				-Gallifreyan.mainRadius, 
				Gallifreyan.mainRadius*2, 
				Gallifreyan.mainRadius*2);			
		Area e1Area = new Area(e1);	
		Ellipse2D e2 = new Ellipse2D.Double( 
				0 - Gallifreyan.consonantRadius,
				consonantY - Gallifreyan.consonantRadius, 
				Gallifreyan.consonantRadius*2, 
				Gallifreyan.consonantRadius*2	
				);	
		Area e2Area = new Area(e2);
		e1Area.subtract( e2Area);
		return e1Area;
	}
	
	
	// draw any dots of the consonant on the consonant circle.
	private  static void drawDots( Graphics2D gr, int consonantX, int num){
		int dotOrbitRadius = (int)(Gallifreyan.consonantRadius * 1.0);		
		int dotRadius = Gallifreyan.dotRadius + (int)(Gallifreyan.strokeSize / 2.0);
		
		List<Double> angles = new ArrayList<Double>();
		if (num > 0 ){
			angles.add( .07);
		} 
		if (num > 1) {
			angles.add( .10);
		}
		if (num > 2) {
			angles.add(.13);
		}
		
		for (double angle : angles) {
			double dotX = dotOrbitRadius * Math.sin( Utils.TAU * angle);
			double dotY = dotOrbitRadius * Math.cos( Utils.TAU * angle);
		
			Ellipse2D e1 = new Ellipse2D.Double( 
				0  - dotX - dotRadius, 
				consonantX - dotY - dotRadius, 
				dotRadius * 2, 
				dotRadius * 2	
				);
			
			gr.fill(e1);
			gr.draw(e1);
		}
	}
	
	// draw a full circle for the consonant.
	private static void drawFullCircle( Graphics2D gr, int centerX, int consonantX, double tauRadians){
		Ellipse2D e = new Ellipse2D.Double( 
				0 - Gallifreyan.consonantRadius,
				consonantX - Gallifreyan.consonantRadius, 
				Gallifreyan.consonantRadius*2, 
				Gallifreyan.consonantRadius*2	
				);	
		gr.draw(e);
	}
	
	// draw a half circle for the consonant.  Notice that this requires a lot of erasing.
	private static void drawHalfCircle( Graphics2D gr, int centerX, int consonantX, double tauRadians){
		// draw arc
		Arc2D a1 = new Arc2D.Double( 
				0 - Gallifreyan.consonantRadius,
				consonantX - Gallifreyan.consonantRadius, 
				Gallifreyan.consonantRadius*2, 
				Gallifreyan.consonantRadius*2,
				19, 
				142, 
				Arc2D.OPEN);
		gr.draw(a1);

		// erase arc of the main circle
		gr.setPaint( Gallifreyan.backgroundColor);
		gr.setStroke( new BasicStroke(Gallifreyan.strokeSize + 5));
		Arc2D a3 = new Arc2D.Double( 
				-Gallifreyan.mainRadius, 
				-Gallifreyan.mainRadius, 
				Gallifreyan.mainRadius*2, 
				Gallifreyan.mainRadius*2,	
				237, 
				66, 
				Arc2D.OPEN);
		gr.draw(a3);			
		
	}
	

	// draw a crescent circle for the circle.  Notice this requires some erasing.
	// Sorry for the misspell.
	private static void drawCresentCircle( Graphics2D gr, int centerX, int consonantX, double tauRadians){

		Arc2D e = new Arc2D.Double( 
				0 - Gallifreyan.consonantRadius,
				consonantX - Gallifreyan.consonantRadius, 
				Gallifreyan.consonantRadius*2, 
				Gallifreyan.consonantRadius*2,
				307,
				285,
				Arc2D.OPEN
				);
		gr.draw(e);

		// erase arc of the main circle

		gr.setPaint( Gallifreyan.backgroundColor);
		gr.setStroke( new BasicStroke( Gallifreyan.strokeSize + 5));
		Arc2D a3 = new Arc2D.Double( 
				-Gallifreyan.mainRadius, 
				-Gallifreyan.mainRadius, 
				Gallifreyan.mainRadius*2, 
				Gallifreyan.mainRadius*2,	
				250, 
				40, 
				Arc2D.OPEN);
		gr.draw(a3);			
	}

			
	// Draw a consonant circle touching the main circle.  (mostly for numerals.)		
	private static void drawTouchingCircle( Graphics2D gr, int centerX, int consonantX, double tauRadians){
		Ellipse2D e = new Ellipse2D.Double( 
				0 - Gallifreyan.consonantRadius,
				consonantX - Gallifreyan.consonantRadius, 
				Gallifreyan.consonantRadius*2, 
				Gallifreyan.consonantRadius*2	
				);
		gr.draw(e);

	}

	// draw a consonant circle on the main circle.
	private static void drawHorizonCircle( Graphics2D gr, int centerX, int consonantX, double tauRadians) {
		// draw arc
		Ellipse2D e = new Ellipse2D.Double( 
				0 - Gallifreyan.consonantRadius,
				consonantX - Gallifreyan.consonantRadius, 
				Gallifreyan.consonantRadius*2, 
				Gallifreyan.consonantRadius*2	
				);
		gr.draw(e);
	}

	

}

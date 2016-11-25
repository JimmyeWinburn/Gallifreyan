package main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import db.GenericDB;
import db.GenericfDBFactory;
import db.jdbc.PhonemeLookup;
import db.jdbc.beans.ParameterType;
import db.jdbc.beans.PhonemeType;

/**
 * transliterates English letters to Gallifreyan circular letters.
 * 
 * @author Jimmye
 *
 */
public class Gallifreyan extends Component {


	private static final long serialVersionUID = 1L;
	

	// input parameters.
	static String name = null;	
	static String path = null;
	public static Color backgroundColor = null;
	public static Color foregroundColor = null;
	public static int strokeSize = 0;
	static int panelSize = 0;
	
	
	public static int mainRadius = 100;
	public static int consonantRadius = 60;
	public static int vowelRadius = 10;
	public static int dotRadius = 2;
	public static GenericDB db = GenericfDBFactory.getDB();
	
	
	// internal parameters
	private int sizeX; 
	private int sizeY;
	private int centerX;
	private int centerY;

	private String fileType = "png";
	private BufferedImage imageBuffer = null;
	private List<String> tokens;
	
	/**
	 * constructor with ParameterType parameter for those who follow the rule that
	 * more than 3 parameters is too much.
	 * 
	 * @param param
	 */
	public Gallifreyan( ParameterType param){
		this( 
				param.getName(),
				param.getBackgroundColor(),
				param.getForegroundColor(),
				param.getGlyphSize(),
				param.getFrameSize(),
				param.getStrokeSize()
		);
	}
	
	/**
	 * constructor who feel that more than 3 parameters is just fine.
	 * 
	 * @param _str
	 * @param _backgroundColor
	 * @param _foregroundColor
	 * @param _glyphSize
	 * @param _frameSize
	 * @param _strokeSize
	 */
	public Gallifreyan( 
			String _str, 
			Color  _backgroundColor,
			Color  _foregroundColor,
			int    _glyphSize,
			int    _frameSize,
			int    _strokeSize) {

		// Set up all the parameters needed to create the glyphs. 
		name =            _str;
		backgroundColor = _backgroundColor;
		foregroundColor = _foregroundColor;
		panelSize =       _glyphSize;
		strokeSize =      _strokeSize * panelSize / 300;
		sizeX =           _frameSize; 
		sizeY =           _frameSize;
		centerX = sizeX/2;
		centerY = sizeY/2;
		mainRadius = mainRadius * panelSize / 300;
		consonantRadius = consonantRadius * panelSize / 300;
		vowelRadius = vowelRadius * panelSize / 300;
		dotRadius = dotRadius * panelSize / 300;
		setSize(sizeX, sizeY);	
		
		// create image and draw to buffer.
		db.openSU();
		imageBuffer = getImage();		
		db.close();
	}

	

	
	/**
	 * writes the image to the specified file with the specified type.
	 * 
	 * @param path
	 * @param fileType
	 */
	public void writeToFile( String path, String fileType){
		try {
			ImageIO.write( imageBuffer, fileType, new File( path));
		} catch (IOException e) {
			System.out.println("Error writing file: " + e.getMessage());
		}
	}
	
	
	/**
	 * displays the glyphes that were defined at construction of the object. 
	 * @param name
	 * @param size
	 */
	public void display( String name, int size){
		Frame  frame = new Frame( name);
		frame.setSize(size,size);
		frame.setVisible(true);
		frame.add( this, BorderLayout.CENTER);					
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}			
		}); 	

	}

	
	// builds the image.  This is used by both the writing to a file and the display 
	// on the screen.
	private BufferedImage getImage(){
		
		BufferedImage imageBuffer = new BufferedImage( sizeX, sizeY, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D gr = imageBuffer.createGraphics();
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// To do the background color we have to manually paint the rectangle. 
		if (Gallifreyan.backgroundColor != null){
			gr.setColor( Gallifreyan.backgroundColor);
			gr.fillRect(0, 0, sizeX, sizeY);
		}
		
		gr.setColor( Gallifreyan.foregroundColor);
		gr.setStroke( new BasicStroke(strokeSize));
		
		// set the center point of the graph.
		AffineTransform at = AffineTransform.getTranslateInstance(centerX, centerY);
		gr.transform(at);

		// draw main circle	
		gr.drawOval(
				-Gallifreyan.mainRadius, 
				-Gallifreyan.mainRadius, 
				Gallifreyan.mainRadius*2, 
				Gallifreyan.mainRadius*2);	

		// draw each of the phonemes
		List<String> tokens = tokenizeGallifreyanString( name);
		for (int i=0; i< tokens.size(); i++){
			drawPhoneme( imageBuffer, tokens.get(i), Utils.TAU * i / tokens.size());
		}
				
		return imageBuffer;
	}
	
	
	// used by Java2D to draw the image onto a buffer that can be 
	// be displayed.  
	// Note: should be private but must be public because it's inherited from Component.
	@Override
	public void paint(Graphics g) {
		Graphics2D gr = (Graphics2D)g;
		imageBuffer = getImage();
		gr.drawImage( imageBuffer, 0,0,this);
		
	}
	
	
	// Draw an individual glyph (combination of a consonant and/or a vowel.)
	private void drawPhoneme( BufferedImage imageBuffer, String phoneme, double tauRadians){

		// Rotate the glyph to the correct position in the main circle.
		// note that we must write on a new instance of the graphics for each phoneme.
		Graphics2D gr = imageBuffer.createGraphics();
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	
		gr.setColor( Gallifreyan.foregroundColor);
		gr.setStroke( new BasicStroke( Gallifreyan.strokeSize));
		AffineTransform at = new AffineTransform();
		at.setToTranslation(centerX, centerY);
		at.rotate( -tauRadians);
		gr.transform(at);

		PhonemeType ph = PhonemeLookup.get(phoneme);
		
		if ( ph.getConsonant() != null){ // draw consonant with possible vowel
			int consonantCenterY = Consonant.drawConsonant( gr, ph.getConsonant(), centerY, centerX, tauRadians);			
			if (ph.getVowel() != null){  
				Vowel.drawVowel( gr, ph.getVowel(), true, 0, consonantCenterY);
			}			
		}  else {    // draw vowel with no consonant
			Vowel.drawVowel( gr, ph.getVowel(), false, 0, Gallifreyan.mainRadius);
		}
	}
	
	
	//
	// parses a string to return a List of individual Gallifreyan glyphs (i.e. a consonant, a vowel, or a consonant and a vowel)
	// 
	// @param str
	// @return List<String>
	 private List<String> tokenizeGallifreyanString( String str){
		List<String> list = new ArrayList<String>();
		char[] buf = str.toCharArray();
		for (int i=0; i<buf.length; ){
			boolean taken = false;
			if (i+2 < buf.length) {
				String pho3 = new String( "" + buf[i] + buf[i+1] + buf[i+2]);
				if (PhonemeLookup.get(pho3)!=null){
					list.add( pho3);
					i = i+3;
					taken = true;
				}
			}
			if (!taken && i+1 < buf.length){
				String pho2 = new String( "" + buf[i] + buf[i+1]);
				if (PhonemeLookup.get(pho2)!=null){
					list.add( pho2);
					i = i+2;
					taken = true;
				} 
			}
			if (!taken){
				String pho1 = new String( "" + buf[i]);
				if (PhonemeLookup.get(pho1)!=null){
					list.add( pho1);
					i = i+1;
				} else {
					return list;
				}
			}
		}
		return list;
	}

}


package db.jdbc.beans;

import java.awt.Color;

public class ParameterType {
	private String name = "Tester";
	private Color  backgroundColor = Color.black;
	private Color  foregroundColor = new Color( 120,255,0);
	private int    strokeSize = 3;
	private int    glyphSize  = 1000;
	private int    frameSize = 1000;
	
	public String getName() {
		return name;
	}
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	public Color getForegroundColor() {
		return foregroundColor;
	}
	public int getStrokeSize() {
		return strokeSize;
	}
	public int getGlyphSize() {
		return glyphSize;
	}
	public int getFrameSize() {
		return frameSize;
	}
	
	public void setName(String name) {
		if (!name.isEmpty()){
			this.name = name;
		}
	}
	public void setBackgroundColor(Color backgroundColor) {
		if (backgroundColor != null){
			this.backgroundColor = backgroundColor;
		}
	}
	public void setForegroundColor(Color foregroundColor) {
		if (foregroundColor != null){
			this.foregroundColor = foregroundColor;
		}
	}
	public void setStrokeSize(int strokeSize) {
		if (strokeSize >0) {
			this.strokeSize = strokeSize;
		}
	}
	public void setGlyphSize(int glyphSize) {
		if (glyphSize >0){
			this.glyphSize = glyphSize;
		}
	}
	public void setFrameSize(int frameSize) {
		if (frameSize >0){
			this.frameSize = frameSize;
		}
	}
}

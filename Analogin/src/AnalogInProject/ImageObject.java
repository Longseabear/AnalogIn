package AnalogInProject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ImageObject implements Serializable{

	transient BufferedImage buff;// transient make it so it won't be written
									// with defaultWriteObject (which would
									// error)
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		// write buff with imageIO to out
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		// read buff with imageIO from in
	}
}

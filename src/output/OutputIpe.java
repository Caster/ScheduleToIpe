package output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import model.Schedule;

/**
 * This class can be used to output a schedule to an IPE file.
 * 
 * @author Thom Castermans
 */
public class OutputIpe {
	
	private PrintStream output;
	
	/**
	 * Create a new object capable of outputting to the default output.
	 */
	public OutputIpe() {
		output = System.out;
	}
	
	/**
	 * Create a new object capable of outputting to the given file.
	 * 
	 * @param file The file to write to.
	 * @throws FileNotFoundException If given file cannot be found.
	 */
	public OutputIpe(File file) throws FileNotFoundException {
		output = new PrintStream(new FileOutputStream(file));
	}
	
	/**
	 * Output the given schedule to the file given at construction or standard output,
	 * depending on how this object was constructed.
	 * 
	 * @param schedule The schedule to be outputted.
	 */
	public void outputIpeFile(Schedule schedule) {
		outputHeader();
		// TODO: Output schedule...
		outputFooter();
	}
	
	private void outputFromFile(String path) {
		InputStream is = getClass().getResourceAsStream(path);
		// Read header from file and output it to the stream
		byte[] buffer = new byte[4096]; // tweaking this number may increase performance  
		int len;  
		try {
			while ((len = is.read(buffer)) != -1)  
			{  
			    output.write(buffer, 0, len);  
			}
			output.flush();
			output.println();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	private void outputFooter() {
		outputFromFile("/ipe_footer.txt");
	}
	
	private void outputHeader() {
		outputFromFile("/ipe_header.txt");
	}
	
	/* methods to write "shapes" to the Ipe file */
	
	private final String SQUARE = "<path layer=\"alpha\" stroke=\"black\"> \n" +
						"%f %f m \n" +
						"%f %f l \n" +
						"%f %f l \n" +
						"%f %f l \n" +
						"h \n" +
						"</path> \n";
	
	private void writeSquare(double x, double y, double width, double height) throws IOException{
		String square = String.format(SQUARE, x, y, x + width, y, x + width, y + height, x, y + height);
		output.write(square.getBytes());
	}
	
	private final String LINE = "<path stroke=\"black\"> \n" +
				"%f %f m \n" +
				"%f %f l \n" +
				"</path> \n";
	
	private void writeLine(double x1, double y1, double x2, double y2) throws IOException{
		String line = String.format(LINE, x1, y1, x2, y2);
		output.write(line.getBytes());
	}
	
	private final String STRING = "<text transformations=\"translations\" pos=\"%f %f\" " +
								"stroke=\"black\" type=\"label\" valign=\"baseline\">%s</text> \n";
	
	private void writeString(String text, double x, double y) throws IOException{
		String string = String.format(STRING, x, y, text);
		output.write(string.getBytes());
	}
	
	
}

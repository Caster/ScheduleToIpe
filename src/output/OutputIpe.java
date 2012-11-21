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
	
}

package output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * This class can be used to output a schedule to an IPE file.
 * 
 * @author Thom Castermans
 */
public class OutputIpe {
	
	private PrintStream output;
	
	public static void main(String[] args) {
		OutputIpe oi = new OutputIpe();
		oi.outputHeader();
		oi.outputFooter();
	}
	
	/**
	 * Create a new object capable of outputting to the default output.
	 */
	public OutputIpe() {
		output = System.out;
	}
	
	private void outputFromFile(String path) {
		InputStream is;
		try {
			is = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
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
		outputFromFile("res/ipe_footer.txt");
	}
	
	private void outputHeader() {
		outputFromFile("res/ipe_header.txt");
	}
	
}

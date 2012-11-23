package output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Set;

import model.Schedule;
import model.Task;

/**
 * This class can be used to output a schedule to an IPE file.
 * 
 * @author Thom Castermans
 */
public class OutputIpe {
	
	/** List of colors supported by Ipe. */
	public static final String[] IPE_COLORS = {
		"red",
		"green",
		"blue",
		"yellow",
		"orange",
		"gold",
		"purple",
		"gray",
		"brown",
		"navy",
		"pink",
		"seagreen",
		"turquoise",
		"violet",
		"darkblue",
		"darkcyan",
		"darkgray",
		"darkgreen",
		"darkmagenta",
		"darkorange",
		"darkred",
		"lightblue",
		"lightcyan",
		"lightgray",
		"lightgreen",
		"lightyellow"
	};
	/** Size of grid, used in outputting graph. */
	public static final int GRID_SIZE = 16;
	/** Offset for graph in Ipe file, over X-axis.
	 *  This is the X-coordinate of the upper-left corner of the drawing. */
	public static final int OFFSET_X = 16 + 10 * GRID_SIZE;
	/** Offset for graph in Ipe file, over Y-axis.
	 *  This is the Y-coordinate of the upper-left corner of the drawing. */
	public static final int OFFSET_Y = 832 - 5 * GRID_SIZE;
	/** Padding, used in drawing squares. */
	public static final double PADDING = 0.1;
	/** Space around text. */
	public static final int TEXT_MARGIN = GRID_SIZE / 5;
	
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
		Set<Task> tasks = schedule.getTasks();
		Task t;
		int i, j;
		
		// Ipe header
		outputHeader();
		
		// Draw axis
		writeLine(OFFSET_X, OFFSET_Y - GRID_SIZE * tasks.size(),
				OFFSET_X + GRID_SIZE * schedule.getLcm(), OFFSET_Y - GRID_SIZE * tasks.size());
		writeLine(OFFSET_X, OFFSET_Y, OFFSET_X, OFFSET_Y - GRID_SIZE * tasks.size());
		// write Y-axis task names
		j = 0;
		for (Task tt : tasks) {
			writeString(tt.getName(),
					OFFSET_X - TEXT_MARGIN, OFFSET_Y + GRID_SIZE * (j - tasks.size()) + GRID_SIZE / 2,
					"right", "center");
			j++;
		}
		
		// Draw tasks
		for (i = 0; i < schedule.getLcm(); i++) {
			// write X-axis scale
			writeString("$" + i + "$",
					OFFSET_X + GRID_SIZE * i, OFFSET_Y - GRID_SIZE * tasks.size() - TEXT_MARGIN,
					"center", "top");
			t = schedule.getTaskAt(i);
			if (t == null)  continue;
			j = 0;
			for (Task tt : tasks) {
				if (tt.equals(t))  break;
				j++;
			}
			writeSquareFilled(OFFSET_X + GRID_SIZE * i + PADDING, OFFSET_Y + GRID_SIZE * (j - tasks.size()) + PADDING,
					GRID_SIZE - 2 * PADDING, GRID_SIZE - 2 * PADDING, IPE_COLORS[j % IPE_COLORS.length]);
		}
		// write X-axis scale
		writeString("$" + i + "$", OFFSET_X + GRID_SIZE * i, OFFSET_Y - GRID_SIZE * tasks.size() - GRID_SIZE / 5, "center", "top");
		
		// Ipe footer
		outputFooter();
	}
	
	private void outputFromFile(String path) {
		InputStream is = null;
		try {
			is = new FileInputStream(path);
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
	
	/* methods to write "shapes" to the Ipe file */
	private final String SQUARE = 
			"<path layer=\"alpha\" stroke=\"black\"> \n" +
				"%f %f m \n" +
				"%f %f l \n" +
				"%f %f l \n" +
				"%f %f l \n" +
				"h \n" +
			"</path> \n";
	private final String SQUARE_FILLED = 
			"<path layer=\"alpha\" stroke=\"%s\" fill=\"%s\"> \n" +
				"%f %f m \n" +
				"%f %f l \n" +
				"%f %f l \n" +
				"%f %f l \n" +
				"h \n" +
			"</path> \n";
	
	@SuppressWarnings("boxing")
	private void writeSquare(double x, double y, double width, double height) {
		String square = String.format(SQUARE, x, y, x + width, y, x + width, y + height, x, y + height);
		try {
			output.write(square.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("boxing")
	private void writeSquareFilled(double x, double y, double width, double height, String color) {
		System.out.println(x);
		String square = String.format(SQUARE_FILLED, color, color, x, y, x + width, y, x + width, y + height, x, y + height);
		try {
			output.write(square.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private final String LINE =
			"<path stroke=\"black\"> \n" +
				"%f %f m \n" +
				"%f %f l \n" +
			"</path> \n";
	
	@SuppressWarnings("boxing")
	private void writeLine(double x1, double y1, double x2, double y2) {
		String line = String.format(LINE, x1, y1, x2, y2);
		try {
			output.write(line.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private final String STRING =
			"<text transformations=\"translations\" pos=\"%f %f\" " +
			"stroke=\"black\" type=\"label\" depth=\"0\" " +
			"halign=\"%s\" valign=\"%s\">%s</text> \n";
	
	@SuppressWarnings("boxing")
	private void writeString(String text, double x, double y, String halign, String valign) {
		String string = String.format(STRING, x, y, halign, valign, text);
		try {
			output.write(string.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}

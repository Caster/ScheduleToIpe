package output;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * Class that holds options for outputting to an Ipe file.
 * Upon construction, some default values are put in place,
 * which are read from a configuration file. These values
 * can be changed later. Of course, options can be read as well.
 * 
 * @author Thom Castermans
 */
public class OutputIpeOptions {

	/** Map of options. */
	Properties options = new Properties();
	
	/**
	 * Construct a new OutputIpeOptions object and populate
	 * all options with defaults.
	 */
	public OutputIpeOptions() {
		try {
			InputStream is = getClass().getResourceAsStream("/res/ipe_output_options_defaults.properties");
			options.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Return an option as a boolean, or {@link Boolean#FALSE} if
	 * there is no option with given key.
	 * 
	 * @param key The name of the option.
	 * @return Value of the option as a boolean, or {@link Boolean#FALSE}
	 *         if no option with given name exists.
	 */
	public boolean getBooleanOption(String key) {
		if (options.containsKey(key)) {
			return Boolean.parseBoolean(options.getProperty(key));
		}
		return false;
	}
	
	/**
	 * Set a specific option to a boolean value, if an option
	 * with the given name is present. Otherwise, no action
	 * is performed.
	 * 
	 * @param key Name of option to be changed.
	 * @param value New value for that option.
	 */
	public void setOption(String key, boolean value) {
		if (options.containsKey(key)) {
			options.setProperty(key, String.valueOf(value));
		}
	}
	
	/**
	 * Return an option as an integer, or {@link Integer#MIN_VALUE} if
	 * there is no option with given key.
	 * 
	 * @param key The name of the option.
	 * @return Value of the option as an integer, or {@link Integer#MIN_VALUE}
	 *         if no option with given name exists.
	 */
	public int getIntegerOption(String key) {
		if (options.containsKey(key)) {
			return Integer.parseInt(options.getProperty(key));
		}
		return Integer.MIN_VALUE;
	}
	
	/**
	 * Set a specific option to an integer value, if an option
	 * with the given name is present. Otherwise, no action
	 * is performed.
	 * 
	 * @param key Name of option to be changed.
	 * @param value New value for that option.
	 */
	public void setOption(String key, int value) {
		if (options.containsKey(key)) {
			options.setProperty(key, String.valueOf(value));
		}
	}
	
	/**
	 * Return an option as a string, or {@code ""} if
	 * there is no option with given key.
	 * 
	 * @param key The name of the option.
	 * @return Value of the option as a string, or {@code ""}
	 *         if no option with given name exists.
	 */
	public String getStringOption(String key) {
		if (options.containsKey(key)) {
			return options.getProperty(key);
		}
		return "";
	}
	
	/**
	 * Set a specific option to a string value, if an option
	 * with the given name is present. Otherwise, no action
	 * is performed.
	 * 
	 * @param key Name of option to be changed.
	 * @param value New value for that option.
	 */
	public void setOption(String key, String value) {
		if (options.containsKey(key)) {
			options.setProperty(key, value);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OutputIpeOptions [\n");
		Set<Entry<Object, Object>> map = options.entrySet();
		for (Entry<Object, Object> entry : map) {
			sb.append("  " + entry.getKey().toString() + " = \"" + entry.getValue().toString() + "\",\n");
		}
		sb.append("]");
		return sb.toString();
	}
}

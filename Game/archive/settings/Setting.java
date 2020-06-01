package settings;

/**
 * A setting object
 * Contains a name and its setting and get methods for each
 * 
 * @author Sam
 *
 */
public class Setting {
	private String name;
	private String value;
	
	/**
	 * Constructor, must pass both a name and its setting
	 * 
	 * @param name - the name of the setting in the file
	 * @param setting - the value of the setting
	 */
	public Setting(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Returns the name of the setting
	 * 
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the value of the setting
	 * 
	 * @return String value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns a string representation of the setting in the form name=value
	 * 
	 * @return String setting
	 */
	public String toString() {
		return name + " = " + value;
	}
	
	/**
	 * Returns the setting object defined in an input string
	 * 
	 * @param input - the input string in the same format as Setting.toString()
	 * @return Setting - the setting representation of the string
	 */
	public static Setting convertToSetting(String input) {
		String[] splitInput = input.split("(=)|(//)");
		if (splitInput.length < 2) return new Setting("", "");
		
		String name = splitInput[0].trim();
		String value = splitInput[1].trim();
		
		return new Setting(name, value);
	}
}

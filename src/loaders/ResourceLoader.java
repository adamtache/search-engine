package loaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Class to load resources for access.
 * 
 * @author adamtache
 *
 */
public class ResourceLoader {

	private static final String RESOURCE_PATH = "src/resources";
	private static final String RESOURCE_EXTENSION = "Resources.resources";
	private Properties myProperties;
	private BufferedReader myFileReader;
	private String fileName;

	/**
	 * Default constructor
	 * @throws IOException 
	 * 
	 */
	public ResourceLoader () throws IOException  {
		this.load(RESOURCE_PATH, RESOURCE_EXTENSION);
	}

	/**
	 * Loads language property file into property object
	 * 
	 */
	public void load(String path, String extension) {
		fileName = path + "/" + extension;
		myFileReader = null;
		try {
			myFileReader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		myProperties = new Properties();
		try {
			myProperties.load(myFileReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getString(String key) {
        return myProperties.getProperty(key);
    }
	
}
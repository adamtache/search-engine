package loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class FileLoader {

	private BufferedReader myFileReader;
	private String fileName;

	/**
	 * Loads language property file into property object
	 * @throws IOException 
	 * @throws SLogoException 
	 */
	public void load(String path, String extension) throws IOException {
		String slash = File.separator;
		fileName = path + slash + extension;
		FileInputStream fstream = new FileInputStream(fileName);
		myFileReader = new BufferedReader(new InputStreamReader(fstream));
	}
	
	public Set<String> getLines() throws IOException{
		Set<String> lines = new HashSet<>();
		String line = "";
	    while ((line = myFileReader.readLine()) != null) {
	    	lines.add(line);
	    }
		return lines;
	}

	public String getString(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
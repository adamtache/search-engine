package file_loading;

import java.io.IOException;

public class StopWordLoader extends FileLoader {

	private String path = "src/resources";
	private String extension = "stopwords.txt";
	
	public StopWordLoader() throws IOException{
		this.load();
	}
	
	public void load() throws IOException{
		super.load(path, extension);
	}
	
}
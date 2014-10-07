package kdkbot.filemanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Config {
	private Path filePath;
	private HashMap<String, String> values = new HashMap<String, String>();
	
	/**
	 * 
	 * @param filePath The path to the file that this config file belongs to
	 */
	public Config(Path filePath) throws Exception {
		this.filePath = filePath;
		verifyExists();
		loadConfigContents();
	}
	
	/**
	 * 
	 * @param filePath
	 */
	public Config(String filePath) throws Exception {
		this.filePath = Paths.get(filePath);
		verifyExists();
		loadConfigContents();
	}
	
	public Config(String filePath, boolean loadContents) throws Exception {
		this.filePath = Paths.get(filePath);
		verifyExists();
		if(loadContents) {
			loadConfigContents();
		}
	}
	
	/**
	 * Sets this configs Path location
	 * @param filePath the path to set to
	 */
	public void setPath(Path filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * Sets this configs Path location, based off of a string
	 * @param filePath the path to set to.
	 */
	public void setPath(String filePath) {
		this.filePath = Paths.get(filePath);
	}
	
	/**
	 * 
	 * @return The path for this config.
	 */
	public Path getPath() {
		return this.filePath;
	}
	
	/**
	 * Returns the setting with a given key value.
	 * @param key The setting to look for
	 * @return the value, or null if not exists, of a given setting
	 */
	public String getSetting(String key) {
		return this.values.get(key);
	}
	
	/**
	 * Sets the config setting to a given value specific by a given key name. Automatically re-saves after setting value
	 * @param key the name of the setting to change
	 * @param value the value to change the setting to
	 */
	public void setSetting(String key, String value) {
		this.values.put(key, value);
		this.saveSettings();
	}
	
	/**
	 * Saves this instances configuration file
	 */
	public void saveSettings() {
		HashMap<String, String> hash = this.values;
		try {
			Iterator hashMapIter = hash.entrySet().iterator();
			
			BufferedWriter write = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(this.filePath.toAbsolutePath().toString()),
						StandardCharsets.US_ASCII));
			
			while(hashMapIter.hasNext()) {
				Map.Entry pairs = (Map.Entry)hashMapIter.next();
				write.write(pairs.getKey() + "=" + pairs.getValue() + "\r\n");
			}
			
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves this instances configuration file with a provided HashMap of values to save to.
	 * Stores them in the file path provided by this configs instance.
	 * @param hash the HashMap containing the key value pairs to save.
	 */
	public void saveSettings(HashMap<String, Integer> hash) {
		try {
			Iterator hashMapIter = hash.entrySet().iterator();
			
			BufferedWriter write = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(this.filePath.toAbsolutePath().toString()),
						StandardCharsets.US_ASCII));
			
			while(hashMapIter.hasNext()) {
				Map.Entry pairs = (Map.Entry)hashMapIter.next();
				write.write(pairs.getKey() + "=" + pairs.getValue() + "\r\n");
			}
			
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves this instances configuration file with a provided String list of values to save to.
	 * Stores them in the file path provided by this configs instance.
	 * @param hash the HashMap containing the key value pairs to save.
	 */
	public void saveSettings(List<String> lines) {
		try {
			Iterator hashMapIter = lines.iterator();
			
			BufferedWriter write = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(this.filePath.toAbsolutePath().toString()),
						StandardCharsets.US_ASCII));
			
			while(hashMapIter.hasNext()) {
				write.write(hashMapIter.next() + "\r\n");
			}
			
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifies the existence of the file at a given path, if it does not exist, it will
	 * automatically create it.
	 */
	public void verifyExists() throws Exception {
		if(!Files.exists(this.filePath)) {
			// System.out.println("[DBG] [CFG] [VERIFY] Doesn't exist: " + this.filePath.toString());
			try {
				// System.out.println("[DBG] [CFG] [VERIFY] Attempting to create.");
				Files.createFile(this.filePath);
			} catch (IOException e) {
				// System.out.println("[DBG] [CFG] [VERIFY] Couldn't create, attempting to create directories first.");
				Files.createDirectories(this.filePath.getParent());
			}
		}
	}
	
	/**
	 * Loads the configuration contents at the instances given file path location into
	 * the instances provided values variable.
	 */
	public void loadConfigContents() throws Exception {
		FileInputStream fis = new FileInputStream(this.filePath.toAbsolutePath().toString());
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		String line;
		while((line = br.readLine()) != null) {
			// System.out.println("[DBG] [CFG] [LCC] Loaded line: " + line);
			String[] args = line.split("=");
			// System.out.println("[DBG] [CFG] [LCC] Args size: " + args.length);
			this.values.put(args[0], args[1]);
		}
		
		fis.close();
		isr.close();
		br.close();
	}
	
	/**
	 * Gets the configuration contents, in a list, where each list item contains an unmodified key value pair.
	 * @return A list containing the key value pairs.
	 */
	public List<String> getConfigContents() throws Exception {
		List<String> lines = Files.readAllLines(this.filePath.toAbsolutePath(), StandardCharsets.UTF_8);

		return lines;
	}
}

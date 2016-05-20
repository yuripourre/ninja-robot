package br.com.nrobot.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import br.com.etyllica.util.PathHelper;

public class ConfigLoader {

	//public static final String PATH_PREFIX = "";
	public static final String PATH_PREFIX = "../";
	private static final String PARAM_SERVER_IP = "server";
	private static final String PARAM_NAME = "name";

	public ConfigLoader() {
		super();
	}

	public static Config loadConfiguration() {
		
		String path = PathHelper.currentDirectory()+PATH_PREFIX+"config/config.txt";

		// This will reference one line at a time
		String line = null;

		Config config = new Config();

		try {

			FileReader fileReader = new FileReader(path);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((line = bufferedReader.readLine()) != null) {

				String str = line.trim();

				if(str.startsWith(PARAM_SERVER_IP)) {
					String param = param(str);
					config.setServerIp(param);
				} else if(str.startsWith(PARAM_NAME)) {
					String param = param(str);
					config.setName(param);
				}
			}

			bufferedReader.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return config;
	}

	private static String param(String s) {
		return s.split("=")[1].trim();
	}
	
}

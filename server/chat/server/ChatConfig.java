package chat.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChatConfig {
	
	private static final Object KEY_CONFIG_PORT = "port";
	
	public static ChatConfig readConfig(String path){
		Scanner sc = new Scanner(path);
		Map<String, String> config = new HashMap<>();
		
		while(sc.hasNext()){
			String line = sc.nextLine();
			String [] param = line.split("=");
			String key = param[0];
			String val = param[1];
			config.put(key, val);
		}
		return new ChatConfig(config);
	}
	
	private final Map<String, String> CONFIG;
	private ChatConfig(Map<String, String> config){
		this.CONFIG = config;
	}

	public int getPort() {
		return Integer.parseInt(CONFIG.get(KEY_CONFIG_PORT));
	}
}

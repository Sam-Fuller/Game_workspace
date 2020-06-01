package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import loader.Setting;

public class Settings {
	public static enum settings {
		xRes("x resolution"),
		yRes("y resolution"),

		monitor("monitor"),

		MSAA("multi-sampling depth"),
		vSync("vSync"),

		textRes("text resolution"),

		doubleClick("double click speed");

		private final String name;
		private Optional<Float> value = Optional.empty();

		private settings(String name) {
			this.name = name;
		}

		public float getValue() {
			if (!value.isPresent()) this.value = Optional.of(getSetting(this.name));

			return this.value.get();
		}
	}


	private static Map<String, Float> settingsMap = new HashMap<String, Float>();

	private static void addSetting(String name, float value) {
		settingsMap.put(name, value);
	}

	private static float getSetting(String name) {
		return settingsMap.get(name);
	}

	public static void load() {
		try(Stream<String> lines = Files.lines(Paths.get("resources/settings.conf"))) {
			lines.map(strsetting -> Setting.convertToSetting(strsetting)).forEach(setting -> addSetting(setting.getName(), setting.getValue()));
		} catch (IOException e) {

		}
	}
}
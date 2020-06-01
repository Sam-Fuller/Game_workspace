package settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Settings {
	public static int resX = 1920;
	public static int resY = 1080;
	
	public static int monitor = -1;
	
	public static int multiSampling = 0;
	public static boolean vSync = false;
	public static float textLargeRes = 256f;
	public static float segmentMultiplier = 2f;
	
	public static void loadSettings() {
		try(Stream<String> lines = Files.lines(Paths.get("resources/settings.conf"))) {
			List<Setting> list = lines
					.map(x -> Setting.convertToSetting(x))
					.collect(Collectors.toList());
			
			
			Optional<String> strresX = loadSingle(list, "x resolution");
			if (strresX.isPresent()) {
				try {
					resX = Integer.parseInt(strresX.get());
				} catch (NumberFormatException e) {}
			}
			
			Optional<String> strresY = loadSingle(list, "y resolution");
			if (strresY.isPresent()) {
				try {
					resY = Integer.parseInt(strresY.get());
				} catch (NumberFormatException e) {}
			}
			
			Optional<String> strmonitor = loadSingle(list, "monitor");
			if (strmonitor.isPresent()) {
				try {
					monitor = Integer.parseInt(strmonitor.get());
				} catch (NumberFormatException e) {}
			}
			
			Optional<String> strmultiSampling = loadSingle(list, "multi-sampling depth");
			if (strmultiSampling.isPresent()) {
				try {
					multiSampling = Integer.parseInt(strmultiSampling.get());
				} catch (NumberFormatException e) {}
			}
			
			Optional<String> strvSync = loadSingle(list, "vSync");
			if (strvSync.isPresent()) {
				try {
					vSync = Boolean.parseBoolean(strvSync.get());
				} catch (NumberFormatException e) {}
			}
			
			Optional<String> strtextLargeRes = loadSingle(list, "text resolution");
			if (strtextLargeRes.isPresent()) {
				try {
					textLargeRes = Float.parseFloat(strtextLargeRes.get());
				} catch (NumberFormatException e) {}
			}
			
			Optional<String> strsegmentMultiplier = loadSingle(list, "circle resolution multiplier");
			if (strsegmentMultiplier.isPresent()) {
				try {
					segmentMultiplier = Float.parseFloat(strsegmentMultiplier.get());
				} catch (NumberFormatException e) {}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Optional<String> loadSingle(List<Setting> settings, String settingName) {
		List<Setting> valid = settings.stream()
				.filter(x -> x.getName().equals(settingName))
				.collect(Collectors.toList());
		
		if (valid.size() == 0) return Optional.empty();
		
		return Optional.of(valid.get(0).getValue());
	}
	
}

package com.github.peakz.stateHandler;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileHandler {

	private final static Logger logger = LoggerFactory.getLogger(FileHandler.class);

	/**
	 * Creates a directory based on dirName.
	 * @param dirName
	 */
	public static void createDirectory(String dirName) {
		File file = new File(dirName);
		if (!file.exists()) {
			file.mkdirs();
			logger.info("Directory Created: " + dirName);
		}
	}

	/**
	 * Reads from file based on path.
	 * @param file
	 * @return fileContents
	 */
	public static List<String> readFromFile(String file) {
		try {
			if (!Paths.get(file).toFile().exists()) {
				Files.createFile(Paths.get(file));
			}
			List<String> fileContents;
			fileContents = Files.readAllLines(Paths.get(file));
			logger.trace("Reading from file: " + file);
			return fileContents;
		} catch (IOException e) {
			logger.error(e.getCause().toString());
			return null;
		}
	}

	/**
	 * Writes to File using file as path, on a specific line.
	 * @param file
	 * @param line
	 * @param text
	 */
	public static void writeToFile(String file, int line, String text) {
		try {
			List<String> fileContents = readFromFile(file);
			fileContents.set(line, text);
			Files.write(Paths.get(file), fileContents);
			logger.trace("Writing to file: " + file + " at line: " + line);
		} catch (IOException e) {
			logger.error(e.getCause().toString());
		}
	}

	/**
	 * Writes to File using file as the path. Checks if it should overwrite.
	 * @param file
	 * @param text
	 * @param overwrite
	 */
	public static void writeToFile(String file, String text, boolean overwrite) {
		try {
			if (!Files.exists(Paths.get(file))) {
				Files.createFile(Paths.get(file));
			}
			if (overwrite) {
				FileWriter fileWriter = new FileWriter(file, false);
				fileWriter.write(text);
				fileWriter.flush();
				fileWriter.close();
			} else {
				FileWriter fileWriter = new FileWriter(file, true);
				fileWriter.append(text + "\n");
				fileWriter.flush();
				fileWriter.close();
			}
			logger.trace("Writing to file: " + file);
		} catch (IOException e) {
			logger.error(e.getCause().toString());
		}
	}

	/**
	 * Reads from a .Json File using path and returns DAO based on objClass.
	 * @param file
	 * @param objClass
	 * @return newObject
	 */
	public static Object readFromJson(String file, Class<?> objClass) {
		Gson gson = new Gson();
		try (Reader reader = new InputStreamReader(new FileInputStream(new File(file)), StandardCharsets.UTF_8)) {
			Object newObject = gson.fromJson(reader, objClass);
			logger.trace("Reading Data from Json File: " + file + " applying to Object: " + objClass.getName());
			reader.close();
			return newObject;
		} catch (IOException e) {
			logger.error(e.getCause().toString());
		}
		logger.error("Failed to Read Data, Returning Null");
		return null;
	}

	/**
	 * saves data DAO object using path.
	 * @param file
	 * @param object
	 */
	public static void writeToJson(String file, Object object) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
			gson.toJson(object, writer);
			logger.trace("Saving " + object.getClass().getName() + " to Json File: " + file);
			writer.close();
		} catch (IOException e) {
			logger.error(e.getCause().toString());
		}
	}

	/**
	 * Checks if a file exists in the specified path.
	 * @param path
	 * @return boolean
	 */
	public static boolean exists(String path) {
		return Files.exists(Paths.get(path));
	}

	/**
	 * Checks if a path exists and contains a file with an object.
	 * If not, it will initialize a new file.
	 * @param path
	 * @param object
	 */
	public static void initFile(String path, Object object) {
		if (!exists(path)) writeToJson(path, object);
	}

	/**
	 * Checks if an empty file exists and if not, it will initialize a new file as null.
	 * @param path
	 */
	public static void initFile(String path) {
		if (!exists(path)) writeToFile(path, "", false);
	}

	/**
	 * Parses a json file and returns the data as a jsonObject.
	 * @param filePath
	 * @return jsonObject
	 */
	public static JsonObject fileToJsonObject(String filePath) {
		JsonObject jsonObject = new JsonObject();
		try {
			Reader reader = new InputStreamReader(new FileInputStream(new File(filePath)), StandardCharsets.UTF_8);
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(reader);
			reader.close();
			jsonObject = jsonElement.getAsJsonObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}

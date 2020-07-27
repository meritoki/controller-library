package com.meritoki.library.controller.node;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class NodeController {
	private static Logger logger = LogManager.getLogger(NodeController.class.getName());

	public static void main(String[] args) {
		System.out.println(getOperatingSystem());
	}

	public static String getSeperator() {
		return FileSystems.getDefault().getSeparator();
	}

	public static String getUserHome() {
		return System.getProperty("user.home");
	}

	public static String getOperatingSystem() {
		return System.getProperty("os.name");
	}

	public static boolean isWindows() {
		return (getOperatingSystem().toLowerCase().contains("windows"));
	}

	public static boolean isLinux() {
		return (getOperatingSystem().toLowerCase().contains("linux"));
	}

	public static BufferedImage openBufferedImage(String filePath, String fileName) {
		logger.info("openBufferedImage(" + filePath + ", " + fileName + ")");
		return openBufferedImage(new java.io.File(filePath + getSeperator() + fileName));
	}

	public static BufferedImage openBufferedImage(java.io.File file) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(file);
		} catch (IOException ex) {
			logger.error("IOException " + ex.getMessage());
		}
		return bufferedImage;
	}

	@JsonIgnore
	public static Object openJson(java.io.File file, Class className) {
		logger.debug("openJson(" + file + ", " + className + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			object = mapper.readValue(file, className);
		} catch (JsonGenerationException e) {
			logger.error(e);
		} catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return object;
	}

	public static <T> Object openJson(File file, TypeReference<List<T>> typeReference) {
		logger.debug("openJson(" + file + ", " + typeReference + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			object = mapper.readValue(file, typeReference);
		} catch (JsonGenerationException e) {
			logger.error(e);
		} catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return object;
	}
	
	public static Properties openProperties(String filePath, String fileName) {
		final Properties properties = new Properties();
		try {
			final FileInputStream fileInputStream = new FileInputStream(filePath+getSeperator()+fileName);
			properties.loadFromXML(fileInputStream);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundExcetion "+e.getMessage());
		} catch (IOException e) {
			logger.error("IOException "+e.getMessage());
		}
		return properties;
	}
	
	public static void saveProperties(Properties properties, String filePath, String fileName) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(filePath+getSeperator()+fileName);
			properties.storeToXML(outputStream, "retina");
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundExcetion "+e.getMessage());
		} catch (IOException e) {
			logger.error("IOException "+e.getMessage());
		}
	}

	@JsonIgnore
	public static List<String[]> openCsv(String fileName) {
		String line = "";
		List<String[]> stringArrayList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			while ((line = br.readLine()) != null) {
				logger.info(line);
				String[] array = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");// ",");
				stringArrayList.add(array);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringArrayList;
	}

	public static void saveJpg(String filePath, String fileName, BufferedImage bufferedImage) throws Exception {
	//		logger.info("saveJpg"+filePath + ", " + fileName + ", " + bufferedImage);
			saveJpg(new File(filePath + getSeperator() + fileName), bufferedImage);
		}

	@JsonIgnore
	public static void saveJpg(File file, BufferedImage bufferedImage) throws Exception {
		logger.info("saveJpg(" + file + ", " + bufferedImage + ")");
		ImageIO.write(bufferedImage, "jpg", file);
	}

	@JsonIgnore
	public static void saveJson(String path, String name, Object object) {
		logger.debug("saveJson(" + path + "," + name + ", object)");
		saveJson(new java.io.File(path + getSeperator() + name), object);
	}

	@JsonIgnore
	public static void saveJson(File file, Object object) {
		logger.debug("saveJson(" + file.getAbsolutePath() + ",object)");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
		} catch (IOException ex) {
			logger.error(ex);
		}
	}

	@JsonIgnore
	public static void saveProperties(String path, String name, Properties properties) {
		logger.info("saveProperties(" + path + "," + name + ", properties");
		try (OutputStream output = new FileOutputStream(path + name)) {
			properties.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	@JsonIgnore
	public static void saveYaml(String filePath, String fileName, Object object) {
		logger.info("saveYaml(" + filePath + ", " + fileName + ", object)");
		DumperOptions options = new DumperOptions();
		options.setPrettyFlow(true);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(options);
		FileWriter writer;
		try {
			writer = new FileWriter(filePath + getSeperator() + fileName);
			yaml.dump(object, writer);
		} catch (IOException ex) {
			logger.error(ex);
		}
	}

	@JsonIgnore
	public static void saveCsv(String filePath, String fileName, Object object) {
		logger.info("saveCsv(" + filePath + ", " + fileName + ", object)");
		saveCsv(new File(filePath+getSeperator()+fileName),object);
	}
	
	@JsonIgnore
	public static void saveCsv(File file, Object object) {
		logger.info("saveCsv(" + file + ", object)");
		try (PrintWriter writer = new PrintWriter(file)) {
			if (object instanceof StringBuilder)
				writer.write(((StringBuilder) object).toString());
			else if (object instanceof List) {
				String objectsCommaSeparated = String.join(",", (List<String>) object);
				writer.write(objectsCommaSeparated);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	@JsonIgnore
	public static List<String> executeCommand(String command) throws Exception {
		return executeCommand(command, 120);
	}

	@JsonIgnore
	public static List<String> executeCommand(String command, int timeout) throws Exception {
		logger.info("executeCommand(" + command + ", " + timeout + ")");
		UUID uuid = UUID.randomUUID();
		File processDirectory = new File("process");
		if (!processDirectory.exists()) {
			processDirectory.mkdir();
		}
		File outputFile = new File(processDirectory + getSeperator() + "output-" + uuid.toString());
		File errorFile = new File(processDirectory + getSeperator() + "error-" + uuid.toString());
		ProcessBuilder processBuilder = null;
		if (isLinux()) {
			processBuilder = new ProcessBuilder("bash", "-c", command).redirectError(errorFile)
					.redirectOutput(outputFile);
		} else if (isWindows()) {
			processBuilder = new ProcessBuilder("CMD", "/C", command).redirectError(errorFile)
					.redirectOutput(outputFile);
		}

		Process process = null;
		String output = null;
		String error = null;
		List<String> stringList = new ArrayList<>();
		String string;
		try {
			process = processBuilder.start();
			if (!process.waitFor(timeout, TimeUnit.SECONDS)) {
				process.destroy();
				logger.info("executeCommand(...) exitValue=" + process.exitValue());
			}
			output = (FileUtils.readFileToString(new File("output"), "UTF8"));
			error = (FileUtils.readFileToString(new File("error"), "UTF8"));
			if (error != null && !error.equals("")) {
				string = "error";
				stringList.add(string);
			} else if (output != null && !output.equals("")) {
				string = output;
				String[] stringArray = string.split("\n");
				for (String s : stringArray) {
					stringList.add(s);
				}
			}
		} catch (Exception e) {
			logger.error("executeCommand(...) Exception " + e.getMessage());
			throw new Exception("process timed out");
		} finally {
			logger.info("executeCommand(...) process.exitValue=" + process.exitValue());
			if (outputFile.exists() || errorFile.exists()) {
				outputFile.delete();
				errorFile.delete();
			}
		}
		return stringList;
	}
	
	public static File[] openPDF(String filePath, File pdf) {
		File[] fileArray = new File[0];
		try {
			String destinationDirectory = filePath;
			File destinationFile = new File(destinationDirectory);
			if (!destinationFile.exists()) {
				destinationFile.mkdir();
				System.out.println("Folder Created -> " + destinationFile.getAbsolutePath());
			}
			if (pdf.exists()) {
				System.out.println("Images copied to Folder: " + destinationFile.getName());
				PDDocument pdfDocument = PDDocument.load(pdf);
				List<PDPage> pdPageList = pdfDocument.getDocumentCatalog().getAllPages();
				fileArray = new File[pdPageList.size()];
				String fileName = pdf.getName().replace(".pdf", "");
				int pageNumber = 1;
				for (int i=0;i<pdPageList.size();i++) {
					PDPage page = pdPageList.get(i);
					File outputFile = new File(destinationDirectory + fileName + "_" + pageNumber + ".jpg");
					if(!outputFile.exists()) {
						BufferedImage image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
						System.out.println("Image Created -> " + outputFile.getName());
						ImageIO.write(image, "jpg", outputFile);
					}
					pageNumber++;
					fileArray[i] = outputFile;
				}
				pdfDocument.close();
				System.out.println("Converted Images are saved at -> " + destinationFile.getAbsolutePath());
			} else {
				System.err.println(pdf.getName() + " File not exists");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileArray;
	}

	public static void deleteDirectory(File directory) {
		directory.delete();
	}
}

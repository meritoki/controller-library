/*
 * Copyright 2020 Joaquin Osvaldo Rodriguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meritoki.library.controller.node;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.meritoki.library.controller.Controller;

public class NodeController extends Controller {
	protected static Logger logger = LoggerFactory.getLogger(NodeController.class.getName());

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
	
	public String getHostAddress() {
		String hostAddress = null;
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.warn("getHostAddress() UnknownHostException");
		}
		return hostAddress;
	}

	public String getHostName() {
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.warn("getHostName() UnknownHostException");
		}
		return hostName;
	}

	public static boolean isWindows() {
		return (getOperatingSystem().toLowerCase().contains("windows"));
	}

	public static boolean isLinux() {
		return (getOperatingSystem().toLowerCase().contains("linux"));
	}
	
	public static boolean isMac() {
		return (getOperatingSystem().toLowerCase().contains("mac"));
	}

	public static BufferedImage openBufferedImage(String filePath, String fileName) {
		logger.info("openBufferedImage(" + filePath + ", " + fileName + ")");
		return openBufferedImage(new java.io.File(filePath + getSeperator() + fileName));
	}

	public static BufferedImage openBufferedImage(java.io.File file) {
		BufferedImage bufferedImage = null;
		if(file != null) {
			try {
				bufferedImage = ImageIO.read(file);
			} catch (IOException ex) {
				logger.error("IOException " + ex.getMessage());
			}
		}
		return bufferedImage;
	}

	@JsonIgnore
	public static Object openJson(java.io.File file, Class className) {
		//logger.debug("openJson(" + file + ", " + className + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			object = mapper.readValue(file, className);
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return object;
	}

	public static <T> Object openJson(File file, TypeReference<List<T>> typeReference) {
		//logger.debug("openJson(" + file + ", " + typeReference + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			object = mapper.readValue(file, typeReference);
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return object;
	}
	
	public static Properties openPropertiesXML(String filePath, String fileName) {
		return openPropertiesXML(new File(filePath+getSeperator()+fileName));
	}

	public static Properties openPropertiesXML(File file) {
		final Properties properties = new Properties();
		try {
			final FileInputStream fileInputStream = new FileInputStream(file);
			properties.loadFromXML(fileInputStream);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundExcetion " + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException " + e.getMessage());
		}
		return properties;
	}
	

	public static Properties openPropertiesXML(InputStream inputStream) {
		//logger.debug("openPropertiesXML(" + inputStream + ")");
		Properties properties = null;
		if (inputStream != null) {
			try {
				properties = new Properties();
				properties.loadFromXML(inputStream);
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
				logger.error("propertiesLoadFromXML(" + inputStream + ") InvalidPropertiesFormatException");
				properties = null;
			} catch (IOException e) {
				logger.error("propertiesLoadFromXML(" + inputStream + ") IOException");
				properties = null;
			}
		}
		return properties;
	}
	
	

	public static void savePropertiesXML(Properties properties, String filePath, String fileName, String comment) {
		//logger.debug("savePropertiesStoreToXML(" + properties + ", " + filePath +", " + fileName + ", " + comment + ")");
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(filePath + getSeperator() + fileName);
			properties.storeToXML(outputStream, comment);
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundExcetion " + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException " + e.getMessage());
		} 
//		finally {
//			outputStream.close();
//		}
		
	}
	
	public static boolean savePropertiesXML(Properties properties, String fileName, String comment) {
		//logger.debug("propertiesStoreToXML(" + properties + ", " + fileName + ", " + comment + ")");
		boolean success = false;
		if (NodeController.newFile(fileName)) {
			Properties sortedProperties = new Properties() {
				private static final long serialVersionUID = 1L;

				public Set<Object> keySet() {
					return Collections.unmodifiableSet(new TreeSet(super.keySet()));
				}
			};
			sortedProperties.putAll(properties);
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(fileName);
				sortedProperties.storeToXML(fileOutputStream, comment);
				fileOutputStream.close();
				success = true;
			} catch (FileNotFoundException e) {
				logger.error("propertiesStoreToXML(" + properties + ", " + fileName + ", " + comment
						+ ") FileNotFoundException");
			} catch (IOException e) {
				logger.error("propertiesStoreToXML(" + properties + ", " + fileName + ", " + comment + ") IOException");
			}
		}
		return success;
	}

	@JsonIgnore
	public static List<String[]> openCsv(String fileName) {
		String line = "";
		List<String[]> stringArrayList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			while ((line = br.readLine()) != null) {
//				logger.info(line);
				String[] array = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");// ",");
				stringArrayList.add(array);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringArrayList;
	}

	public static void saveJpg(String filePath, String fileName, BufferedImage bufferedImage) throws Exception {
		// logger.info("saveJpg"+filePath + ", " + fileName + ", " + bufferedImage);
		saveJpg(new File(filePath + getSeperator() + fileName), bufferedImage);
	}

	@JsonIgnore
	public static void saveJpg(File file, BufferedImage bufferedImage) throws Exception {
		logger.info("saveJpg(" + file + ", " + bufferedImage + ")");
		ImageIO.write(bufferedImage, "jpg", file);
	}
	
	
	public static void savePng(String filePath, String fileName, BufferedImage bufferedImage) throws Exception {
		File file = new File(filePath + getSeperator() + fileName);
		savePng(file, bufferedImage);
	}

	/**
	 * If not working, check file name, may not be valid and save fails
	 * @param file
	 * @param bufferedImage
	 * @throws Exception
	 */
	@JsonIgnore
	public static void savePng(File file, BufferedImage bufferedImage) throws Exception {
//		logger.info("savePng(" + file + ", " + bufferedImage + ")");
		ImageIO.write(bufferedImage, "png", file);
	}

	@JsonIgnore
	public static void saveJson(String path, String name, Object object) {
//		logger.info("saveJson(" + path + "," + name + ", object)");
		saveJson(new java.io.File(path + getSeperator() + name), object);
	}

	@JsonIgnore
	public static void saveJson(File file, Object object) {
//		logger.info("saveJson(" + file.getAbsolutePath() + ","+Boolean.valueOf(object!=null)+")");
		file.getParentFile().mkdirs();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
		} catch (IOException ex) {
			logger.error(ex.getMessage());
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
			logger.error(ex.getMessage());
		}
	}

	@JsonIgnore
	public static void saveText(String filePath, String fileName, Object object) {
		logger.info("saveText(" + filePath + ", " + fileName + ", object)");
		saveText(new File(filePath + getSeperator() + fileName), object);
	}

	@JsonIgnore
	public static void saveText(File file, Object object) {
		logger.info("saveText(" + file + ", object)");
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
	
	
	
	public static void savePython(String fileName, String content) {
		try (PrintWriter out = new PrintWriter(fileName)) {
		    out.println(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@JsonIgnore
	public static Exit executeCommand(String command) throws Exception {
		return executeCommand(command, 120);
	}

	@JsonIgnore
	public static Exit executeCommand(String command, int timeout) throws Exception {
		logger.info("executeCommand(" + command + ", " + timeout + ")");
		Exit exit = new Exit();
		UUID uuid = UUID.randomUUID();
		File processDirectory = new File("process");
		if (!processDirectory.exists()) {
			processDirectory.mkdir();
		}
		File outputFile = new File(processDirectory + getSeperator() + "output-" + uuid.toString());
		File errorFile = new File(processDirectory + getSeperator() + "error-" + uuid.toString());
		ProcessBuilder processBuilder = null;
		if (isLinux() || isMac()) {
			logger.info("executeCommand("+ command + ", " + timeout + ") linux & mac");
			processBuilder = new ProcessBuilder("bash", "-c", command).redirectError(errorFile)
					.redirectOutput(outputFile);
		} else if (isWindows()) {
			logger.info("executeCommand("+ command + ", " + timeout + ") windows");
			processBuilder = new ProcessBuilder("cmd.exe", "/c", command).redirectError(errorFile)
					.redirectOutput(outputFile);
		} 
		Process process = null;
		String output = null;
		String error = null;
		String string;
		try {
			process = processBuilder.start();
			if (!process.waitFor(timeout, TimeUnit.SECONDS)) {
				process.destroy();
				logger.info("executeCommand("+ command + ", " + timeout + ") processs.exitValue=" + process.exitValue());
			}
			output = (FileUtils.readFileToString(outputFile, "UTF8"));
			error = (FileUtils.readFileToString(errorFile, "UTF8"));
			if (error != null && !error.equals("")) {
				string = error;
				String[] stringArray = string.split("\n");
				for (String s : stringArray) {
					exit.error.add(s);
				}
			} 
			if (output != null && !output.equals("")) {
				string = output;
				String[] stringArray = string.split("\n");
				for (String s : stringArray) {
					exit.list.add(s);
				}
			}
		} catch (Exception e) {
			logger.error("executeCommand("+ command + ", " + timeout + ") Exception: " + e.getMessage());
			throw new Exception("executeCommand("+ command + ", " + timeout + ") Exception: " + e.getMessage());
		} finally {
			//logger.debug("executeCommand(...) process.exitValue=" + process.exitValue());
			exit.value = process.exitValue();
		}
		return exit;
	}

	public void copyDirectory(File sourceDirectory, File destinationDirectory) {
		logger.info("copyDirectory(" + sourceDirectory + ", " + destinationDirectory + ")");
		File[] fileArray = getDirectoryFileArray(sourceDirectory);
		File file = null;
		String fileName = null;
		String destinationDirectoryAbsolutePath = null;
		byte[] byteArray = null;
		if (fileArray != null && destinationDirectory.isDirectory())
			for (int i = 0; i < fileArray.length; i++) {
				file = fileArray[i];
				fileName = file.getName();
				byteArray = readFile(file);
				destinationDirectoryAbsolutePath = destinationDirectory.getAbsolutePath();
				writeFile(String.valueOf(destinationDirectoryAbsolutePath) + File.separator + fileName, byteArray);
			}
	}

	public static boolean deleteDirectory(String directoryName) {
		logger.info("deleteDirectory(" + directoryName + ")");
		return deleteDirectory(new File(directoryName));
	}

	public static boolean deleteDirectory(File directory) {
		logger.info("deleteDirectory(" + directory + ")");
		boolean success = false;
		if (directory.isDirectory()) {
			String[] children = directory.list();
			for (int i = 0; i < children.length; i++) {
				success = deleteDirectory(new File(directory, children[i]));
				if (!success)
					return false;
			}
		}
		return directory.delete();
	}

	public File[] getDirectoryFileArray(String directoryName) {
		logger.info("getDirectoryFileArray(" + directoryName + ")");
		return getDirectoryFileArray(new File(directoryName));
	}

	public File[] getDirectoryFileArray(File directory) {
		logger.info("getDirectoryFileArray(" + directory + ")");
		File[] fileArray = null;
		if (directory.isDirectory())
			fileArray = directory.listFiles();
		return fileArray;
	}

	public static boolean newFile(String fileName) {
		boolean flag = false;
		if (StringUtils.isNotBlank(fileName))
			flag = newFile(new File(fileName));
		return flag;
	}

	public static boolean newFile(File file) {
		//logger.debug("newFile(" + file + ")");
		boolean success = false;
		String newFileAbsolutePath = FilenameUtils.normalize(file.getAbsolutePath());
		File newFile = new File(newFileAbsolutePath);
		if (!newFile.exists()) {
			File parentDirectory = newFile.getParentFile();
			if (parentDirectory != null && !parentDirectory.exists())
				parentDirectory.mkdirs();
			try {
				success = newFile.createNewFile();
			} catch (IOException e) {
				logger.error("newFile(" + file + ") IOException");
			}
		} else {
			success = true;
		}
		return success;
	}

	public boolean writeFile(String fileName, byte[] byteArray) {
		return writeFile(new File(fileName), byteArray);
	}

	public boolean writeFile(File file, byte[] byteArray) {
		logger.info("writeFile(" + file + ", " + byteArray + ")");
		FileOutputStream fileOutputStream = null;
		boolean success = false;
		newFile(file);
		try {
			fileOutputStream = new FileOutputStream(file);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(byteArray, 0, byteArray.length);
			bufferedOutputStream.flush();
			fileOutputStream.flush();
			bufferedOutputStream.close();
			fileOutputStream.close();
			success = true;
		} catch (FileNotFoundException e) {
			logger.error("writeFile(" + file + ", " + byteArray + ") FileNotFoundException");
			success = false;
		} catch (IOException e) {
			logger.error("writeFile(" + file + ", " + byteArray + ") IOException");
			success = false;
		}
		return success;
	}

	public boolean writeEncryptedFile(File file, byte[] byteArray, char[] password) {
		logger.info("writeEncryptedFile(" + file + ", " + byteArray + ", password)");
		boolean flag = false;
		try {
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			SecureRandom secureRandom = new SecureRandom();
			byte[] encryptedByteArray = new byte[0];
			byte[] salt = new byte[8];
			secureRandom.nextBytes(salt);
			KeySpec keySpec = new PBEKeySpec(password, salt, 1024, 256);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(1, secretKeySpec);
			AlgorithmParameters algorithmParameters = cipher.getParameters();
			byte[] initializationVector = ((IvParameterSpec) algorithmParameters
					.<IvParameterSpec>getParameterSpec(IvParameterSpec.class)).getIV();
			logger.info(Integer.valueOf(initializationVector.length)+"");
			byte[] cipherText = cipher.doFinal(byteArray);
			encryptedByteArray = appendByteArrays(encryptedByteArray, salt);
			encryptedByteArray = appendByteArrays(encryptedByteArray, initializationVector);
			encryptedByteArray = appendByteArrays(encryptedByteArray, cipherText);
			flag = writeFile(file, encryptedByteArray);
		} catch (NoSuchAlgorithmException e) {
			logger.error("writeEncryptedFile(" + file + ", " + byteArray + ", password) NoSuchAlgorithmException");
		} catch (InvalidKeySpecException e) {
			logger.error("writeEncryptedFile(" + file + ", " + byteArray + ", password) InvalidKeySpecException");
		} catch (InvalidKeyException e) {
			logger.error("writeEncryptedFile(" + file + ", " + byteArray + ", password) InvalidKeyException");
		} catch (NoSuchPaddingException e) {
			logger.error("writeEncryptedFile(" + file + ", " + byteArray + ", password) NoSuchPaddingException");
		} catch (IllegalBlockSizeException e) {
			logger.error("writeEncryptedFile(" + file + ", " + byteArray + ", password) IllegalBlockSizeException");
		} catch (BadPaddingException e) {
			logger.error("writeEncryptedFile(" + file + ", " + byteArray + ", password) BadPaddingException");
		} catch (InvalidParameterSpecException e) {
			logger.error("writeEncryptedFile(" + file + ", " + byteArray + ", password) InvalidParameterSpecException");
		}
		return flag;
	}

	public byte[] readFile(String fileName) {
		return readFile(new File(fileName));
	}

	public byte[] readFile(File file) {
		logger.info("readFile(" + file + ")");
		byte[] byteArray = new byte[0];
		if (file.isFile()) {
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(file);
				byteArray = readFile(fileInputStream, (int) file.length());
			} catch (FileNotFoundException e) {
				logger.error("readFile(" + file + ") FileNotFoundException");
			}
		} else {
			logger.error("readFile(" + file + ") (file.isFile() == false)");
		}
		return byteArray;
	}

	public byte[] readFile(InputStream inputStream, int fileLength) {
		logger.info("readFile(" + inputStream + ", " + fileLength + ")");
		byte[] byteArray = new byte[0];
		if (inputStream != null && fileLength > -1) {
			byteArray = new byte[fileLength];
			try {
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				bufferedInputStream.read(byteArray);
			} catch (IOException e) {
				logger.error("readFile(" + inputStream + ", " + fileLength + ") IOException");
			}
		}
		return byteArray;
	}

	public byte[] readFile(InputStream inputStream) {
		logger.info("readFile(" + inputStream + ")");
		byte[] byteArray = new byte[0];
		if (inputStream != null) {
			List<Byte> byteList = new ArrayList<Byte>();
			byte b = -1;
			try {
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				int integer;
				while ((integer = bufferedInputStream.read()) != -1)
					byteList.add(Byte.valueOf((byte) integer));
				int byteListSize = byteList.size();
				byteArray = new byte[byteListSize];
				for (int i = 0; i < byteListSize; i++)
					byteArray[i] = ((Byte) byteList.get(i)).byteValue();
			} catch (IOException e) {
				logger.error("readFile(" + inputStream + ") IOException");
			}
		}
		return byteArray;
	}

	public InputStream readEncryptedFile(File file, char[] password) {
		logger.info("readEncryptedFile(" + file + ", password)");
		InputStream inputStream = null;
		try {
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] byteArray = readFile(file);
			byte[] salt = Arrays.copyOfRange(byteArray, 0, 8);
			byte[] initializationVector = Arrays.copyOfRange(byteArray, 8, 24);
			byte[] cipherText = Arrays.copyOfRange(byteArray, 24, byteArray.length);
			KeySpec keySpec = new PBEKeySpec(password, salt, 1024, 256);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(2, secretKeySpec, new IvParameterSpec(initializationVector));
			String plainText = new String(cipher.doFinal(cipherText), "UTF-8");
			inputStream = new ByteArrayInputStream(plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			logger.error("readEncryptedFile(" + file + ", password) NoSuchAlgorithmException");
		} catch (InvalidKeySpecException e) {
			logger.error("readEncryptedFile(" + file + ", password) InvalidKeySpecException");
		} catch (InvalidKeyException e) {
			logger.error("readEncryptedFile(" + file + ", password) InvalidKeyException");
		} catch (NoSuchPaddingException e) {
			logger.error("readEncryptedFile(" + file + ", password) NoSuchPaddingException");
		} catch (IllegalBlockSizeException e) {
			logger.error("readEncryptedFile(" + file + ", password) IllegalBlockSizeException");
		} catch (BadPaddingException e) {
			logger.error("readEncryptedFile(" + file + ", password) BadPaddingException");
		} catch (UnsupportedEncodingException e) {
			logger.error("readEncryptedFile(" + file + ", password) UnsupportedEncodingException");
		} catch (InvalidAlgorithmParameterException e) {
			logger.error("readEncryptedFile(" + file + ", password) InvalidAlgorithmParameterException");
		}
		return inputStream;
	}

	private byte[] appendByteArrays(byte[] byteArray, byte[] postByteArray) {
		byte[] one = byteArray;
		byte[] two = postByteArray;
		byte[] combined = new byte[one.length + two.length];
		for (int i = 0; i < combined.length; i++)
			combined[i] = (i < one.length) ? one[i] : two[i - one.length];
		return combined;
	}
	
	public static String getBufferedImageChecksum(BufferedImage bufferedImage) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", outputStream);
		byte[] data = outputStream.toByteArray();
		System.out.println("Start MD5 Digest");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data);
		byte[] hash = md.digest();
		return returnHex(hash);
	}

	// Below method of converting Byte Array to hex
	// Can be found at: http://www.rgagnon.com/javadetails/java-0596.html
	static String returnHex(byte[] inBytes) throws Exception {
		String hexString = "";
		for (int i = 0; i < inBytes.length; i++) { // for loop ID:1
			hexString += Integer.toString((inBytes[i] & 0xff) + 0x100, 16).substring(1);
		} // Belongs to for loop ID:1
		return hexString;
	}

}

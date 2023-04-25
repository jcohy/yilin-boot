package com.saga.boot.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 描述: .
 *
 * <p>
 * Copyright © 2023 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jiac
 * @version 1.0.0 2023/4/25 22:55
 * @since 1.0.0
 */
public class FileUtils {

	private static final List<String> fileNames = new ArrayList<>();

	private static final List<String> excludeSuffix = Arrays.asList(".png", ".jpg", ".jepg", ".zip");

	/**
	 * 输出
	 *
	 * @param file     file
	 * @param consumer consumer
	 */
	public static void withPrintWriter(File file, Consumer<PrintWriter> consumer) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(file,true), true)) {
			consumer.accept(writer);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	// ===================================================================================

	/**
	 * 获取指定目录下的所有文件名，指定的扩展文件。{@code excludeSuffix}
	 *
	 * @param directory 指定目录
	 */
	public static List<String> readAllFilesNameWithExcludeSuffix(File directory) {
//        FileCopyUtils.copyToString()
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					readAllFilesNameWithExcludeSuffix(file);
				} else {
					if (include(file.getName())) {
						fileNames.add(file.getName());
					}
				}
			}
		}
		return fileNames;
	}

	/**
	 * 判断文件名是否包含指定后缀
	 *
	 * @param name 文件名
	 * @return /
	 */
	public static boolean include(String name) {
		long count = excludeSuffix.stream().filter(name::endsWith).count();
		return count == 0;
	}

	// ===================================================================================

	/**
	 * 获取指定目录下的所有文件名。value 为文件中的每一行
	 * @param directory 指定目录
	 */
	public static List<String> readFileTree(File directory) {
		fileNames.add(directory.getName());
		if (directory.isDirectory()) {
			Arrays.stream(Objects.requireNonNull(directory.listFiles())).forEach(FileUtils::readFileTree);
		}
		return fileNames;
	}

	/**
	 * 获取指定目录下的所有文件名。
	 * @param directory 指定目录
	 */
	public static List<String> readFileTreeWithPrefix(File directory,int level) {
		String prefix = "|==";
		fileNames.add("||" + "===".repeat(Math.max(0, level)) + " " + directory.getName());
		if (directory.isDirectory()) {
			Arrays.stream(Objects.requireNonNull(directory.listFiles())).forEach(file -> readFileTreeWithPrefix(file,level + 1));
		}
		return fileNames;
	}

	/**
	 * 递归遍历文件
	 * @param path /
	 * @throws IOException ex
	 */
//	public static MultiValueMap<String,String> listFiles(Path path) throws IOException {
//		return listFiles(path,"归档");
//	}

	/**
	 * 递归遍历文件
	 * @param path /
	 * @param contain 文件包含字符
	 * @throws IOException ex
	 */
//	public static MultiValueMap<String,String> listFiles(Path path,String contain) throws IOException {
//		return listFiles(path,true,contain);
//	}

	/**
	 * 递归遍历文件.key 为文件名或目录名
	 * @param path /
	 * @param contain 文件包含字符
	 * @param sort 是否排序
	 * @throws IOException
	 */
//	public static MultiValueMap<String,String> listFiles(Path path,boolean sort, String contain) throws IOException {
//		MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
//		Files.walkFileTree(path, new FileVisitor<>() {
//			@Override
//			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
//				if (file.getFileName().toString().contains(contain)) {
//					List<String> lines = FileUtils.readLines(file.toFile());
//					if(sort) {
//						lines = lines.stream().sorted().collect(Collectors.toList());
//					}
//					List<String> lists = new ArrayList<>(lines);
//					map.put(file.getFileName().toString(), lists);
//				} else {
//					String fileName = file.getFileName().toString();
//					String parentFile = file.getParent().getFileName().toString();
//					List<String> list = map.get(parentFile);
//					if(list != null) {
//						list.add(fileName);
//						map.put(parentFile,list);
//					} else {
//						map.put(parentFile, new ArrayList<>() {
//							{
//								add(fileName);
//							}
//						});
//					}
//
//				}
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult visitFileFailed(Path file, IOException exc) {
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
//				return FileVisitResult.CONTINUE;
//			}
//		});
//		return map;
//	}

	/**
	 * 递归遍历文件,根据文件大小遍历
	 * @param path /
	 * @throws IOException
	 */
//	public static MultiValueMap<String,String> listFilesWithSize(Path path) throws IOException {
//		MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
//		Files.walkFileTree(path, new FileVisitor<>() {
//			@Override
//			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
//				String size = formatFileSize(file.toFile().length());
//				String fileName = file.getFileName().toString();
//				List<String> lists;
//				if(map.containsKey(size)){
//					lists = map.get(size);
//				} else {
//					lists = new ArrayList<>();
//				}
//				if(!fileName.endsWith(".jpg") && !fileName.endsWith(".txt")) {
//					lists.add(fileName);
//					map.put(size,lists);
//				}
//
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult visitFileFailed(Path file, IOException exc) {
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
//				return FileVisitResult.CONTINUE;
//			}
//		});
//		return map;
//	}

	/**
	 * 转换文件大小
	 */
	public static String formatFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	// ===================================================================================
	/**
	 * 判断文件名是否为数字
	 *
	 * @param fileName 文件名
	 * @return /
	 */
	public static boolean isNumber(String fileName) {
		String str = fileName.substring(0, fileName.lastIndexOf("."));
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// ===================================================================================
	/**
	 * 读取文件的每一行，转为集合
	 *
	 * @param file file
	 * @return /
	 */
	public static List<String> readLines(File file) {
		try (LineNumberReader reader = new LineNumberReader(new FileReader(file, StandardCharsets.UTF_8))) {
			return reader.lines().collect(Collectors.toList());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 读取文件每一行内容，并排序输出.
	 * @param filePath 单个文件
	 */
	public static List<String> readLinesAndSort(String filePath) {
		return readLines(new File(filePath)).stream().sorted().collect(Collectors.toList());
	}

	// ===================================================================================
	/**
	 * 获取文件编码
	 * @param fileName fileName
	 * @return /
	 * @throws IOException e
	 */
	public static String getFileCharsetName(String fileName) throws IOException {
		InputStream inputStream = new FileInputStream(fileName);
		byte[] head = new byte[3];
		inputStream.read(head);

		String charsetName = "GBK";//或GB2312，即ANSI
		if (head[0] == -1 && head[1] == -2 ) //0xFFFE
			charsetName = "UTF-16";
		else if (head[0] == -2 && head[1] == -1 ) //0xFEFF
			charsetName = "Unicode";//包含两种编码格式：UCS2-Big-Endian和UCS2-Little-Endian
		else if(head[0]==-27 && head[1]==-101 && head[2] ==-98)
			charsetName = "UTF-8"; //UTF-8(不含BOM)
		else if(head[0]==-17 && head[1]==-69 && head[2] ==-65)
			charsetName = "UTF-8"; //UTF-8-BOM

		inputStream.close();

		//System.out.println(code);
		return charsetName;
	}



	// ===================================================================================

	/**
	 * 检查指定文件缺少的番号.
	 *
	 * @param file    文件
	 * @param prefix  前缀
	 * @param padding 填充大小
	 */
//	public static List<String> checkLack(File file, String prefix, int padding) {
//		List<String> lines = readLines(file)
//				.stream()
//				.filter(line -> line.startsWith(prefix))
//				.collect(Collectors.toList());
//		List<String> lacks = new ArrayList<>();
//
//		String preNumber = getFileNumber(prefix, padding, 1);
//
//		for (String line : lines) {
//			String number = "";
//			if (line.contains("&")) {
//				number = line.substring(0, line.indexOf("&"));
//			} else {
//				number = line.substring(0, line.indexOf("-"));
//			}
//			if (!number.equals(preNumber)) {
//				int index = Integer.parseInt(number.substring(prefix.length()));
//				int preIndex = Integer.parseInt(preNumber.substring(prefix.length()));
//				if (index - preIndex > 1) {
//					for (int j = preIndex + 1; j < index; j++) {
//						lacks.add(getFileNumber(prefix, padding, j));
//					}
//				}
//			}
//			preNumber = number;
//		}
//		return lacks;
//	}

	/**
	 * 获取文件番号
	 *
	 * @param prefix  前缀
	 * @param padding 填充大小
	 * @param number  编号
	 * @return 文件番号
	 */
//	private static String getFileNumber(String prefix, int padding, int number) {
//		return prefix + StringUtils.leftPad(String.valueOf(number), padding, "0");
//	}

	/**
	 * 获取所以文件番号
	 *
	 * @param prefix  前缀
	 * @param padding 填充大小
	 * @param max     最大编号
	 * @return 文件番号
	 */
//	private static List<String> getFileNumbers(String prefix, int padding, int max) {
//		List<String> numbers = new ArrayList<>(max);
//		for (int i = 1; i < max + 1; i++) {
//			numbers.add(prefix + StringUtils.leftPad(String.valueOf(i), padding, "0"));
//		}
//		return numbers;
//	}

	// ===================================================================================
	/**
	 * 根据文件路径获取文件名.
	 *
	 * @param filePath 文件路径
	 * @return 文件名
	 */
	public static String getFileNameByPath(String filePath) {
		return filePath.trim().substring(filePath.trim().lastIndexOf("/") + 1);
	}

	/**
	 * 获取文件名，去除后缀名.
	 *
	 * @param file 文件
	 * @return {string}
	 */
	public static String getNameWithoutExtension(String file) {
//		Assert.notNull(file, "file is null.");
		String fileName = getFileNameByPath(file);
		int dotIndex = fileName.lastIndexOf(".");
		return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
	}

	/**
	 * 获取文件扩展名
	 * @param fileName filename
	 * @return /
	 */
	public static String getExtensionName(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}

	// ===================================================================================
	/**
	 * 获取本地文件.
	 *
	 * @param path path
	 * @return /
	 */
//	public static File getNativeDocument(String path) {
//		try {
//			ClassPathResource classPathResource = new ClassPathResource(path);
//			return classPathResource.getFile();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	/**
	 * 重命名文件名。替换后缀
	 * @param filePath 目录
	 * @param replace 替换后的后缀
	 * @param suffix 原后缀
	 */
	public static void renameSuffix(String filePath, String replace, String suffix) {
		File directory = new File(filePath);
		if (directory.isDirectory()) {
			for (File file : Objects.requireNonNull(directory.listFiles())) {
				String name = file.getName();
				if( name.endsWith(replace)) {
					String substring = name.substring(0, name.lastIndexOf("."));
					file.renameTo(new File(file.getParent() + File.separator + substring + suffix));
				}
			}
		}
	}

	/**
	 * 重命名文件，当前缀不存在时，添加指定前缀
	 * @param path 路径
	 * @param prefix 前缀
	 */
	public static void addPrefix(String path,String prefix) {
		prefix = "【"+prefix+"】";
		File directory = new File(path);
		if (directory.isDirectory()) {
			for (File file : Objects.requireNonNull(directory.listFiles())) {
				String name = file.getName();
				if( !name.startsWith(prefix)) {
					file.renameTo(new File(file.getParent() + File.separator + prefix + name));
				}
			}
		}
	}

	/**
	 * 重命名文件，删除指定字符
	 * @param path 路径
	 * @param str 指定字符
	 */
	public static void removeStr(String path,String str) {
		File directory = new File(path);
		if (directory.isDirectory()) {
			for (File file : Objects.requireNonNull(directory.listFiles())) {
				String name = file.getName();
				if( name.contains(str)) {
					name = name.replace(str,"");
					file.renameTo(new File(file.getParent() + File.separator + name));
				}
			}
		}
	}

	/**
	 * 重命名文件，删除指定字符
	 * @param path 路径
	 * @param str 指定字符
	 */
	public static void replaceStr(String path,String str,String replace) {
		File directory = new File(path);
		if (directory.isDirectory()) {
			for (File file : Objects.requireNonNull(directory.listFiles())) {
				String name = file.getName();
				if( name.contains(str)) {
					name = name.replace(str,replace);
					file.renameTo(new File(file.getParent() + File.separator + name));
				}
			}
		}
	}

	/**
	 * 重命名文件名，删除指定目录中文件名中指定的字符
	 * @param filePath 指定目录
	 * @param str 指定字符
	 */
	public static void excludeString(String filePath,String str) {
		File directory = new File(filePath);
		if(directory.isDirectory()) {
			for (File file : Objects.requireNonNull(directory.listFiles())) {
				String parent = file.getParent();
				String name = file.getName();
				String newName = "";
				if(name.contains(str)) {
					newName = name.replace(str,"");
				}
				file.renameTo(new File(parent + File.separator + newName));
			}
		}
	}

	/**
	 * 将【】后缀移动到文件开头
	 * @param filePath filePath
	 */
	public static void moveSuffixToPrefix(String filePath,String suffix) {
		File directory = new File(filePath);
		if (directory.isDirectory()) {
			for (File file : Objects.requireNonNull(directory.listFiles())) {
				if(!file.isDirectory()) {
					String fileName = file.getName();
					String name = FileUtils.getNameWithoutExtension(fileName);
					if(name.endsWith("】") && fileName.endsWith(suffix)) {
						String parent = file.getParent();
						String extensionName = FileUtils.getExtensionName(fileName);
						int index = name.lastIndexOf("【");
						String str = name.substring(index);
						String newName = str + name.substring(0,index) + "." +extensionName;
						file.renameTo(new File(parent + File.separator + newName));
					}
				}
			}
		}
	}
}

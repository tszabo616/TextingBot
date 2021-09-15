package com.tas.application;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeToMP3 {
//	private static final Pattern VID_ID_PATTERN = Pattern.compile("(?<=v\\=|youtu\\.be\\/)\\w+"),
	private static final Pattern VID_ID_PATTERN = Pattern.compile(".*?com/watch.*?=(.*)"),
//	        MP3_URL_PATTERN = Pattern.compile("(?<=href=\\\")https{0,1}\\:\\/\\/(\\w|\\d){3}\\.ytapivmp3\\.com.+\\.mp3(?=\\\")");
			MP3_URL_PATTERN = Pattern.compile("(.*)(http.*\\.ytapivmp3[.]com.*?)(\".*)");
	
	public static List<List<String>> convertCSV(String filePath){
		List<List<String>> csv_list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		        csv_list.add(Arrays.asList(values));
		    }
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<List<String>> csvList = new ArrayList<>();
		for(int i=0; i<csv_list.size(); i++) {
			csvList.add(removeListBlanks(csv_list.get(i)));
		}
		return csvList;
	}
	
	private static List<String> removeListBlanks(List<String> list) {
		List<String> result = new ArrayList<String>();
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).length()>0) {
				result.add(list.get(i));
			}
		}
		return result;
	}
	
	public static void generateFile(String youtubeUrl, String filePath) throws Exception {
		String id = getID(youtubeUrl);
	    String converter = loadConverter(id);
	    String mp3url = getMP3URL(converter);
	    
	    URL url2 = new URL(mp3url);
	    InputStream is = url2.openStream();
		
	    byte[] bytes = is.readAllBytes();
		File file = new File(filePath);
        OutputStream os = new FileOutputStream(file);
        os.write(bytes);
        os.close();
	}
	
	private static byte[] load(String url) throws IOException {
	    URL url2 = new URL(url);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    InputStream is = url2.openStream();
	    byte[] byteChunk = new byte[2500];
	    int n;

	    while ((n = is.read(byteChunk)) > 0) {
	        baos.write(byteChunk, 0, n);
	    }
	    is.close();
	    baos.flush();
	    baos.close();
	    return baos.toByteArray();
	}

	private static String getMP3URL(String html) {
	    Matcher m = MP3_URL_PATTERN.matcher(html);
//	    m.find();
	    if (!m.find()) {
	        throw new IllegalArgumentException("Invalid MP3 URL.");
	    }
	    return m.group(2);
	}

	private static String loadConverter(String id) throws IOException {
	    String url = "https://www.320youtube.com/watch?v=" + id;
	    byte[] bytes = load(url);
	    return new String(bytes);
	}

	private static String getID(String youtubeUrl) {
	    Matcher m = VID_ID_PATTERN.matcher(youtubeUrl);
	    if (!m.find()) {
	        throw new IllegalArgumentException("Invalid YouTube URL.");
	    }
//	    return m.group();
	    return m.group(1);
	}
}

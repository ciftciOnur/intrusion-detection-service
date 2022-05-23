package tr.edu.yeditepe.intrusiondetection.application.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tr.edu.yeditepe.intrusiondetection.application.service.DiskManagementService;

@Service
@RequiredArgsConstructor
public class DiskManagementServiceImpl implements DiskManagementService{
	
	private String path = "/Users/onurciftci/eclipse-workspace/intrusion-detection-service/protectedFolder";
	
	@Override
	public void folderCheck() {
	    File theDir = new File(path);
	    if (!theDir.exists()){
	        theDir.mkdirs();
	    }
	}
	
	@Override
	public List<String> folderComponents (){
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		List <String> fileList = new ArrayList<String>();
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()&&!listOfFiles[i].getName().startsWith(".")) {
		    fileList.add(listOfFiles[i].getName());
		  } else if (listOfFiles[i].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[i].getName());
		  }
		}
		return fileList;
	}
	
	@Override
	public void searchAndWrite(String lineTag,String content) throws IOException {
		String foundLine = parseFile(path+"protected.ids",lineTag);
		if(Objects.isNull(foundLine)) {
		    Files.writeString(
		    		Path.of(path+"protected.ids"),
		            lineTag+"|||"+content + System.lineSeparator(),
		            StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		}
		else {
			String[] parts = foundLine.split("\\|\\|\\|");
			if(!content.equals(parts[1])) {
				lineChanger(foundLine,lineTag+"|||"+content,"protected.ids");
			}
		}
		
	}
	
    public String parseFile(String fileName,String searchStr) throws IOException{
    	File fileCheck = new File(fileName);
    	fileCheck.createNewFile(); 
        Scanner scan = new Scanner(new File(fileName));
        while(scan.hasNext()){
            String line = scan.nextLine().toString();
            if(line.contains(searchStr)){
                return line;
            }
        }
		return null;
    }
    
    public void lineChanger(String oldLine, String newLine, String fileName) throws IOException {
    	List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(path+fileName), StandardCharsets.UTF_8));
    	for (int i = 0; i < fileContent.size(); i++) {
    	    if (fileContent.get(i).equals(oldLine)) {
    	        fileContent.set(i, newLine);
    	        break;
    	    }
    	}

    	Files.write(Path.of(path+fileName), fileContent, StandardCharsets.UTF_8);
    }
    
    @Override
    public byte[] readFile(String fileName)throws IOException, NoSuchAlgorithmException{
    	MessageDigest digest = MessageDigest.getInstance("SHA-256");
    	return digest.digest(Files.readAllBytes(Paths.get(path+"/"+fileName)));

    }
    
    @Override
    public String findFileHash(String lineTag) throws IOException {
		String foundLine = parseFile(path+"protected.ids",lineTag);
		String[] parts = foundLine.split("\\|\\|\\|");
		return parts[1];
    }
}

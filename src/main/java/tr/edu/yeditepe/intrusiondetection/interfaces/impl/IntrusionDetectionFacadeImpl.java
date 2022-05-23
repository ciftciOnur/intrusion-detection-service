package tr.edu.yeditepe.intrusiondetection.interfaces.impl;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tr.edu.yeditepe.intrusiondetection.application.service.AesService;
import tr.edu.yeditepe.intrusiondetection.application.service.DiskManagementService;
import tr.edu.yeditepe.intrusiondetection.application.service.PasswordService;
import tr.edu.yeditepe.intrusiondetection.domain.model.LoginInfo;
import tr.edu.yeditepe.intrusiondetection.interfaces.IntrusionDetectionFacade;

@Component
@RequiredArgsConstructor
@Slf4j
public class IntrusionDetectionFacadeImpl implements IntrusionDetectionFacade {
	
	@Autowired
	private DiskManagementService diskManagementService;
	
	@Autowired
	private PasswordService passwordService;
	
	@Autowired
	private AesService aesService;
	
	@Autowired
	private Environment environment;
	
	private final LoginInfo loginInfo;
	
	@Override
	@Scheduled(fixedRateString = "${jobs.fixedDelay.oneMinute}")
	public void folderCheck() throws Exception {
		diskManagementService.folderCheck();
		List <String> fileList=diskManagementService.folderComponents();
		if(Objects.isNull(fileList)) {
			log.info("no files");
			return;
		}
		for(String fileName:fileList) {
			System.out.println(fileName);
			byte[] fileHashedContent = diskManagementService.readFile(fileName);
			saveFileStates(fileName,Base64.getEncoder().encodeToString(fileHashedContent));
		}
	}
	
	@Override
	public boolean login(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		loginInfo.setPasswordKey(passwordService.keySpecCreator(password));
		loginInfo.setLogin(true);
		loginInfo.setPassword(password);
		loginInfo.setIV(environment.getProperty("security.salt"));
		return true;
	}
	
	@Override
	public boolean saveFileStates(String fileName, String fileState) throws Exception {
		if(loginInfo.isLogin()==false)
			return false;
		String encryptedState = aesService.encrypt("AES/CBC/PKCS5Padding", fileState, loginInfo.getPasswordKey(), Base64.getDecoder().decode(loginInfo.getIV()));
		diskManagementService.searchAndWrite(fileName, encryptedState);
		return true;
		
	}
	
	@Override
	public List<String> checkChangedFiles() throws Exception {
		List<String> changedFiles = new ArrayList<String>();
		List<String> fileList = diskManagementService.folderComponents();
		for(String file:fileList) {
			String fileHash=diskManagementService.findFileHash(file);
			byte[] fileHashedContent = diskManagementService.readFile(file);
			String encryptedState = aesService.encrypt("AES/CBC/PKCS5Padding", Base64.getEncoder().encodeToString(fileHashedContent), loginInfo.getPasswordKey(), Base64.getDecoder().decode(loginInfo.getIV()));
			if(!fileHash.contentEquals(encryptedState))
				changedFiles.add(file);
		}
		return changedFiles;
	}
	
	@Override
	public Set<String> fileContentCheck() throws NoSuchAlgorithmException, IOException {
		diskManagementService.folderCheck();
		List <String> fileList=diskManagementService.folderComponents();
		List<String> duplicateControl = new ArrayList<String>();
		if(Objects.isNull(fileList)) {
			
			return null;
		}
		for(String fileName:fileList) {
			System.out.println(fileName);
			byte[] fileHashedContent = diskManagementService.readFile(fileName);
			duplicateControl.add(Base64.getEncoder().encodeToString(fileHashedContent));
		}
		return duplicateFinder(duplicateControl,fileList);
	}
	
	public Set<String> duplicateFinder(List<String> duplicates,List<String> fileNames){
	    final Set<String> setToReturn = new HashSet<>(); 
	    final Set<String> set1 = new HashSet<>();
	    int i=0;
	         
	    for (String yourInt : duplicates) {
	        if (!set1.add(yourInt)) {
	            setToReturn.add(fileNames.get(i));
	        }
	        i++;
	    }
	    return setToReturn;
	}
	
	

}

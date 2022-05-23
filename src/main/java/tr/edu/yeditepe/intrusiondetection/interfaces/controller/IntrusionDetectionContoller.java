package tr.edu.yeditepe.intrusiondetection.interfaces.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tr.edu.yeditepe.intrusiondetection.application.service.AesService;
import tr.edu.yeditepe.intrusiondetection.interfaces.IntrusionDetectionFacade;

@RestController
@RequestMapping("/api/intrusion-detection")
@RequiredArgsConstructor
public class IntrusionDetectionContoller {

	@Autowired
	private IntrusionDetectionFacade intrusionDetectionFacade;
	
	
    @GetMapping("/login")
    public boolean login(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return intrusionDetectionFacade.login(password);
    }
    
    @GetMapping("/duplicates")
    public Set<String> duplicates() throws NoSuchAlgorithmException, IOException {
		return intrusionDetectionFacade.fileContentCheck();
    }
    
    @GetMapping("/changed-files")
    public List<String> changedFiles() throws Exception {
		return intrusionDetectionFacade.checkChangedFiles();
    }
    
    @PostMapping("/file-states")
    public void fileStates() throws Exception {
		intrusionDetectionFacade.folderCheck();
    }

}

package tr.edu.yeditepe.intrusiondetection.interfaces;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

public interface IntrusionDetectionFacade {

	void folderCheck() throws Exception;

	boolean login(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException;

	boolean saveFileStates(String fileName, String fileState) throws Exception;

	Set<String> fileContentCheck() throws NoSuchAlgorithmException, IOException;

	List<String> checkChangedFiles() throws Exception;

}

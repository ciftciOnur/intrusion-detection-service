package tr.edu.yeditepe.intrusiondetection.application.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface DiskManagementService {

	List<String> folderComponents();

	void folderCheck();

	void searchAndWrite(String lineTag, String content) throws IOException;

	byte[] readFile(String path) throws IOException, NoSuchAlgorithmException;

	String findFileHash(String lineTag) throws IOException;

}

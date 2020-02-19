package client;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	/** 
	* Description: ��FTP�������ϴ��ļ� 
	* @param host FTP������hostname 
	* @param port FTP�������˿� 
	* @param username FTP��¼�˺� 
	* @param password FTP��¼���� 
	* @param basePath FTP����������Ŀ¼
	* @param filePath FTP�������ļ����·������������ڴ�ţ�/2015/01/01���ļ���·��ΪbasePath+filePath
	* @param filename �ϴ���FTP�������ϵ��ļ��� 
	* @param input ������ 
	* @return �ɹ�����true�����򷵻�false 
	*/  
	public static boolean uploadFile(String host, int port, String username, String password, String basePath, String filePath, String filename, InputStream input) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		System.out.println("uploadFile");
		try {
			int reply;
			ftp.connect(host, port);// ����FTP������
			// �������Ĭ�϶˿ڣ�����ʹ��ftp.connect(host)�ķ�ʽֱ������FTP������
			if(!ftp.login(username, password)) {
				System.out.println("��¼ʧ��");
				return false;
			}
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.println("isPositiveCompletion");
				//ftp.disconnect();
				//return result;
			}
			//�л����ϴ�Ŀ¼
			System.out.println("uploadFile xxxxxxxxxxxxxxxx");
			if (!ftp.changeWorkingDirectory(basePath+filePath)) {
				System.out.println("uploadFile xxxxxxxxxxxxxxxx");
				//���Ŀ¼�����ڴ���Ŀ¼
				String[] dirs = filePath.split("/");
				String tempPath = basePath;
				for (String dir : dirs) {
					if (null == dir || "".equals(dir)) continue;
					tempPath += "/" + dir;
					if (!ftp.changeWorkingDirectory(tempPath)) {
						if (!ftp.makeDirectory(tempPath)) {
							return result;
						} else {
							ftp.changeWorkingDirectory(tempPath);
						}
					}
				}
			}
			//�����ϴ��ļ�������Ϊ����������
			System.out.println("uploadFile BINARY_FILE_TYPE");
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.setBufferSize(3072);
			//�ϴ��ļ�
			ftp.setControlEncoding("UTF-8");
			ftp.enterLocalPassiveMode();
			if (!ftp.storeFile(filename, input)) {
				System.out.println("storeFile failed");
				return result;
			}
			System.out.println("uploadFile done");
			input.close();
			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
	
	/** 
	* Description: ��FTP�����������ļ� 
	* @param host FTP������hostname 
	* @param port FTP�������˿� 
	* @param username FTP��¼�˺� 
	* @param password FTP��¼���� 
	* @param remotePath FTP�������ϵ����·�� 
	* @param fileName Ҫ���ص��ļ��� 
	* @param localPath ���غ󱣴浽���ص�·�� 
	* @return 
	*/  
	public static boolean downloadFile(String host, int port, String username, String password, String remotePath, String fileName, String localPath) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		System.out.println("downloadFile " + fileName);
		try {
			int reply;
			System.out.println("connect to host " + host);
			ftp.connect(host, port);
			// �������Ĭ�϶˿ڣ�����ʹ��ftp.connect(host)�ķ�ʽֱ������FTP������
			ftp.login(username, password);// ��¼
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.println("isPositiveCompletion");
				ftp.disconnect();
				return result;
			}
			System.out.println("current work path is " + ftp.printWorkingDirectory());
			//ftp.changeWorkingDirectory(remotePath);// ת�Ƶ�FTP������Ŀ¼
			//System.out.println("current work path is " + ftp.printWorkingDirectory());
			ftp.enterLocalPassiveMode();
			FTPFile[] fs = ftp.listFiles();
			//System.out.println("get file list as below");
			for (FTPFile ff : fs) {
				//System.out.println("get file " + ff.getName());
				if (ff.getName().equals(fileName)) {
					System.out.println("start down load file " + ff.getName());
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
			System.out.println("down load compelte");
			ftp.logout();
			result = true;
		} catch (IOException e) {
				e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return result;
	}
}

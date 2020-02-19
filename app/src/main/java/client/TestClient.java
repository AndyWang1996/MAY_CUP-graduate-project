package client;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.json.JSONException;
import org.json.JSONObject;

public class TestClient {
	private String filePath = "";
	String path = Environment.getExternalStorageDirectory() + "/Download/";
	public String getSocket(String query, int flag) {
		// TODO Auto-generated method stub
		//FtpUtil.downloadFile("154.8.200.106", 21, "Administrator", "Wangyikai1017","/", "2019-05-11=10-34-29.json", "D:\\");
		//return;
		//*
		Socket socketClient = new Socket();
		TestClient client = new TestClient();
		SocketAddress addr = new InetSocketAddress("154.8.200.106", 27001);
		try {
			socketClient.connect(addr);
			if(!socketClient.isConnected()){
	        }else{
				if (flag == 1){
					filePath = client.getsome(socketClient,query,"");
				}else if (flag == 2){
					filePath = client.getsome(socketClient,"",query);
				}else{
					filePath = client.getAll(socketClient);
				}

	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//*/
		Log.i("PATH",filePath + path);
		return path + filePath;
	}
	
	public String getAll(Socket socketClient) throws IOException {
		Cosmetics cosmetics = new Cosmetics();
    	cosmetics._name = "";
    	cosmetics._minPrice = 0;
    	cosmetics._maxPrice = 0;
    	cosmetics._shades = "";
    	JSONObject msgObj = new JSONObject();
    	try {
			msgObj.put("msg_type", "query");
			msgObj.put("msg_content", cosmetics.getJsonObj());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socketClient.getOutputStream()));
    	System.out.println("send " + msgObj.toString());
    	dos.writeUTF(msgObj.toString());
    	dos.flush();
    	DataInputStream dis = new DataInputStream(new BufferedInputStream(socketClient.getInputStream()));
    	System.out.println("begin receive");
    	String length = dis.readUTF();
    	int leng = Integer.valueOf(length);
    	StringBuilder strBuilder = new StringBuilder("");
    	int recLen = 0;
    	System.out.println("RecieveThread recive need receive length " + leng);
    	do {
    		String strTmp = dis.readUTF();
    		System.out.println("RecieveThread recive data len " + strTmp.length());
    		strBuilder.append(strTmp);
    		int tmp = strBuilder.length();
    		System.out.println("RecieveThread recive data all len " + tmp);
    		recLen = tmp;
    	}while(recLen < leng);
    	String allData = strBuilder.toString();
    	System.out.println("get msg " + allData);
    	try {
			JSONObject tmpObj = new JSONObject(allData);
			filePath = tmpObj.getString("result_file");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(filePath != null && !filePath.isEmpty()) {
    		System.out.println("begin download file " + filePath);
    		//使锟斤拷ftp锟斤拷锟斤拷锟侥硷拷
    		FtpUtil.downloadFile("154.8.200.106", 21, "Administrator", "Wangyikai1017","/", filePath, path);
    	}

		File localdata = new File(path + filePath);
    	localdata.renameTo(new File(path + "data.json"));

		return filePath;
	}
	
	public String getsome(Socket socketClient, String query1, String query2) throws IOException {
		Cosmetics cosmetics = new Cosmetics();
    	cosmetics._name = query1;
    	cosmetics._minPrice = 0;
    	cosmetics._category = query2;
    	cosmetics._maxPrice = 9999;
    	cosmetics._shades = "";
    	JSONObject msgObj = new JSONObject();
    	try {
			msgObj.put("msg_type", "query");
			msgObj.put("msg_content", cosmetics.getJsonObj());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socketClient.getOutputStream()));
    	System.out.println("send " + msgObj.toString());
    	dos.writeUTF(msgObj.toString());
    	dos.flush();
    	DataInputStream dis = new DataInputStream(new BufferedInputStream(socketClient.getInputStream()));
    	System.out.println("begin receive");
    	String length = dis.readUTF();
    	int leng = Integer.valueOf(length);
    	StringBuilder strBuilder = new StringBuilder("");
    	int recLen = 0;
    	System.out.println("RecieveThread recive need receive length " + leng);
    	do {
    		String strTmp = dis.readUTF();
    		System.out.println("RecieveThread recive data len " + strTmp.length());
    		strBuilder.append(strTmp);
    		int tmp = strBuilder.length();
    		System.out.println("RecieveThread recive data all len " + tmp);
    		recLen = tmp;
    	}while(recLen < leng);
    	String allData = strBuilder.toString();
    	System.out.println("get msg " + allData);
    	try {
			JSONObject tmpObj = new JSONObject(allData);
			filePath = tmpObj.getString("result_file");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(filePath != null && !filePath.isEmpty()) {
    		System.out.println("begin download file~~ " + filePath);
    		FtpUtil.downloadFile("154.8.200.106", 21, "Administrator", "Wangyikai1017","/", filePath, path);
    	}
    	return filePath;
	}
}

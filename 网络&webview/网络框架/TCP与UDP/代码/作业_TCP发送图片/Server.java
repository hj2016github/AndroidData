package 作业_TCP发送图片;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
		/*1，创建Sockct接收
		 *2,接收后accpt()
		 *3,创建输入、输出流，用get方法获得Stream
		 *4，接收MP4，
		 *5.返回一句话；
		 *6，关闭
		 * */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = new ServerSocket(4444);
		Socket socket=serverSocket.accept();//建立连接；
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		DataInputStream dInStream = new DataInputStream(is);
		DataOutputStream dOutputStream = new DataOutputStream(os);
		//接收MP4；接收给一个数组；
		FileOutputStream fos = new FileOutputStream("D:/a.mp4");
		byte[] recevie = new byte[1024];
		int len = dInStream.read(recevie);
		while(len !=-1){
			fos.write(recevie, 0, len);
			len = dInStream.read(recevie);
		}
		dOutputStream.writeUTF("成功接收");
		//返回有文字问题，需要debug；
		fos.close();
		dOutputStream.close();
		dInStream.close();
		is.close();
		os.close();
		socket.close();
		serverSocket.close();
	}

}

package ��ҵ_TCP����ͼƬ;

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
		/*1������Sockct����
		 *2,���պ�accpt()
		 *3,�������롢���������get�������Stream
		 *4������MP4��
		 *5.����һ�仰��
		 *6���ر�
		 * */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = new ServerSocket(4444);
		Socket socket=serverSocket.accept();//�������ӣ�
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		DataInputStream dInStream = new DataInputStream(is);
		DataOutputStream dOutputStream = new DataOutputStream(os);
		//����MP4�����ո�һ�����飻
		FileOutputStream fos = new FileOutputStream("D:/a.mp4");
		byte[] recevie = new byte[1024];
		int len = dInStream.read(recevie);
		while(len !=-1){
			fos.write(recevie, 0, len);
			len = dInStream.read(recevie);
		}
		dOutputStream.writeUTF("�ɹ�����");
		//�������������⣬��Ҫdebug��
		fos.close();
		dOutputStream.close();
		dInStream.close();
		is.close();
		os.close();
		socket.close();
		serverSocket.close();
	}

}

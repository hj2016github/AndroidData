package ��ҵ_TCP����ͼƬ;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

	/**
	 * @param args ����һ��ͼƬ
	 * /**
	 * @param args ���ؽ�����Ϣ
	 * 1�����ļ�����ָ��MP4
	 * 1.����socket,ָ���˿ڣ�
	 * 2.�����������������scoket.get()
	 * 3.Date����װ�������MP4��
	 * 4.�������������أ�
	 */
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File file = new File("F:\\�����ͼʱ��仯.mp4");
		FileInputStream fis = new FileInputStream(file);
		
		Socket socket  = new Socket("127.0.0.1",4444);
		InputStream is = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		DataInputStream dis = new DataInputStream(is);
	    DataOutputStream dos = new DataOutputStream(out);
	  //����ֽ����飻
	    byte[] sentByte = new byte[1024];
		int len = fis.read(sentByte);
		while(len != -1){
			 dos.write(sentByte);//��MP4����������
			 len = fis.read(sentByte);
		}
	//���շ����������ַ�
	//	byte[] b = new byte[];//��Ҫָ�������С�����տ��������ޣ�
		//��������Ҫ��ѭ����
		String stringSever = new String(dis.readUTF());
		System.out.println(stringSever);
	//�ر�����
		fis.close();
		dos.close();
		dis.close();
		out.close();
		is.close();
		socket.close();
	}
}

package 作业_TCP发送图片;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

	/**
	 * @param args 发送一张图片
	 * /**
	 * @param args 返回接收信息
	 * 1，从文件读入指定MP4
	 * 1.创建socket,指定端口；
	 * 2.创建输入输出流；从scoket.get()
	 * 3.Date流包装——输出MP4；
	 * 4.接收输入流返回；
	 */
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File file = new File("F:\\世界地图时间变化.mp4");
		FileInputStream fis = new FileInputStream(file);
		
		Socket socket  = new Socket("127.0.0.1",4444);
		InputStream is = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		DataInputStream dis = new DataInputStream(is);
	    DataOutputStream dos = new DataOutputStream(out);
	  //获得字节数组；
	    byte[] sentByte = new byte[1024];
		int len = fis.read(sentByte);
		while(len != -1){
			 dos.write(sentByte);//把MP4发给服务器
			 len = fis.read(sentByte);
		}
	//接收服务器返回字符
	//	byte[] b = new byte[];//需要指定数组大小，接收可能是无限，
		//不合理。需要放循环；
		String stringSever = new String(dis.readUTF());
		System.out.println(stringSever);
	//关闭流：
		fis.close();
		dos.close();
		dis.close();
		out.close();
		is.close();
		socket.close();
	}
}

package UDP通信_Demo;
import java.net.*;
import java.io.*;
//接收方，receive；
public class TestUDPServer
{
	public static void main(String args[]) throws Exception
	{
		//集装箱；
		byte buf[] = new byte[1024];
		//不用指定接收人
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		//接收邮局必须指定
		DatagramSocket ds = new DatagramSocket(5678);
		while(true)
		{
			ds.receive(dp);//给字节数组赋值；
			//输入流包装，最后解析-打印；
			ByteArrayInputStream bais = new ByteArrayInputStream(buf);
			DataInputStream dis = new DataInputStream(bais);
			System.out.println(dis.readLong());//可以读取图片
		}
	}
}
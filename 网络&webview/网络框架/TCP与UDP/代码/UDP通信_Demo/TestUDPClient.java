package UDP通信_Demo;
import java.net.*;
import java.io.*;
//发送方，send
public class TestUDPClient
{
	public static void main(String args[]) throws Exception
	{
		long n = 10000L;
		//ByteArrayOutputStream虚拟内存，直接把数据放到内存，省去读取硬盘的步骤；
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//DataOutputStream收取图片，电影等；
		DataOutputStream dos = new DataOutputStream(baos);
		//  将一个 long 值以 8-byte 值形式写入基础输出流中，先写入高字节。
		//Data输出流输出；包装类的方法；给bao是赋值；
		dos.writeLong(n);
		//baos给buf赋值；
		byte[] buf = baos.toByteArray();//buf充满了数据；
        System.out.println(buf.length);
		
		DatagramPacket dp = new DatagramPacket(buf, buf.length, 
											   new InetSocketAddress("127.0.0.1", 5678)
											   );
		DatagramSocket ds = new DatagramSocket(9999);//可以不指定；
		ds.send(dp);
		ds.close();
		
	}
}
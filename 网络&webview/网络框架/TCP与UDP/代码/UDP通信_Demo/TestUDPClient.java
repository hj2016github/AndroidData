package UDPͨ��_Demo;
import java.net.*;
import java.io.*;
//���ͷ���send
public class TestUDPClient
{
	public static void main(String args[]) throws Exception
	{
		long n = 10000L;
		//ByteArrayOutputStream�����ڴ棬ֱ�Ӱ����ݷŵ��ڴ棬ʡȥ��ȡӲ�̵Ĳ��裻
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//DataOutputStream��ȡͼƬ����Ӱ�ȣ�
		DataOutputStream dos = new DataOutputStream(baos);
		//  ��һ�� long ֵ�� 8-byte ֵ��ʽд�����������У���д����ֽڡ�
		//Data������������װ��ķ�������bao�Ǹ�ֵ��
		dos.writeLong(n);
		//baos��buf��ֵ��
		byte[] buf = baos.toByteArray();//buf���������ݣ�
        System.out.println(buf.length);
		
		DatagramPacket dp = new DatagramPacket(buf, buf.length, 
											   new InetSocketAddress("127.0.0.1", 5678)
											   );
		DatagramSocket ds = new DatagramSocket(9999);//���Բ�ָ����
		ds.send(dp);
		ds.close();
		
	}
}
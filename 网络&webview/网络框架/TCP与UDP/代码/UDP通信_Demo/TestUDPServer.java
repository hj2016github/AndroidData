package UDPͨ��_Demo;
import java.net.*;
import java.io.*;
//���շ���receive��
public class TestUDPServer
{
	public static void main(String args[]) throws Exception
	{
		//��װ�䣻
		byte buf[] = new byte[1024];
		//����ָ��������
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		//�����ʾֱ���ָ��
		DatagramSocket ds = new DatagramSocket(5678);
		while(true)
		{
			ds.receive(dp);//���ֽ����鸳ֵ��
			//��������װ��������-��ӡ��
			ByteArrayInputStream bais = new ByteArrayInputStream(buf);
			DataInputStream dis = new DataInputStream(bais);
			System.out.println(dis.readLong());//���Զ�ȡͼƬ
		}
	}
}
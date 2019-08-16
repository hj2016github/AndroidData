package TCP通信_Demo;
import java.io.*; 
import java.net.*;
public class TestSockServer {
  public static void main(String[] args) {
    InputStream in = null; 
    OutputStream out = null;
    try {
      ServerSocket ss = new ServerSocket(5888);//指定端口；
      Socket socket = ss.accept();//建立连接；
      in = socket.getInputStream(); 
      out = socket.getOutputStream();
      DataOutputStream dos = new DataOutputStream(out);
      DataInputStream dis = new DataInputStream(in);
      String s = null;
      if((s=dis.readUTF())!=null) {
	      System.out.println(s);
	      System.out.println("from: "+socket.getInetAddress());
	      System.out.println("Port: "+socket.getPort());
	    }
      dos.writeUTF("客户端,我收到"); 
      dis.close();
      dos.close();
      socket.close();
    } catch (IOException e) {e.printStackTrace();}
  }
}
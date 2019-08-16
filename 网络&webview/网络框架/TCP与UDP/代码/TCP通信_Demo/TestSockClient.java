package TCP通信_Demo;
import java.net.*;
import java.io.*;
public class TestSockClient {
  public static void main(String[] args) {
    InputStream is = null; 
    OutputStream os = null;
    try {
      Socket socket = new Socket("localhost",5888);
//双向；
      is = socket.getInputStream();
      os = socket.getOutputStream();
//包装      
      DataInputStream dis = new DataInputStream(is);
      DataOutputStream dos = new DataOutputStream(os);
      dos.writeUTF("hi"); 
//输出读入流；
      String s = null;
      if((s=dis.readUTF())!=null);
      System.out.println(s); 
      dos.close();
      dis.close();
      socket.close();
    } catch (UnknownHostException e) {
       e.printStackTrace();
    } catch (IOException e) {e.printStackTrace();}
  }
}

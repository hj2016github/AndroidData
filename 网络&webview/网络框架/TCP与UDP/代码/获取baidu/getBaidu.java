package ��ȡbaidu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * @author lenovo
 *URL�Ĺ������ģʽ
 *setURLStreamHanderFactory(URLStreamHanderFactory fac);���û�����set��JVM���и�ӳ���
 *URLStreamHanderFactoryͨ��creatURLStreamHanderFactory(String protocol)�������ã�
 *����ģʽ��������һ�����߶��Э������
 *X x1 x2<---factory<---����getclass()=��creatURLStreamHanderFactory(String protocol)����
 *���ݴ���Ĳ���a=(String protocol)���Զ��᷵�ؽӿڵĶ�Ӧ�Ķ���
 *���δ����֪�������Ǹ�Э�飬���߲�ϣ������֪��Ҫ�����ĸ����࣬��ʹ�ù���ģʽ��
 */
public class getBaidu {
	public static void main(String[] args) throws Exception{
		System.out.println("ȡ������ҳ��");
		getContentByLanguage("ja");
		/*System.out.println('\n');
		System.out.println("ȡ������ҳ��");
		getContentByLanguage("zh-cn");*/
	}
	public static void getContentByLanguage(String country){
		try {
//���Ӱٶȣ��������ԣ���ʱû������
			URL urlBaidu = new URL("http://60.205.150.100:8001/Service/GetServiceInfo.ashx?Action=ValidateLogin&content={%27appid%27:%27101%27,%27username%27:%27admin%27,%27password%27:%27123456%27}");
			//URL urlBaidu=new URL("http://www.baidu.com");
			
			HttpURLConnection baiduConnection =(HttpURLConnection) urlBaidu.openConnection();
			baiduConnection.setRequestProperty("Accept-Language",country);
//�õ���Ϣͷ����		
			Map requests = baiduConnection.getRequestProperties();
			Set reqField=requests.keySet();
			Iterator itrReq = reqField.iterator();
			while(itrReq.hasNext()){//��������˼����ڵ��������������˼��һ�£�
				String field= (String) itrReq.next();
				System.out.println(field+ " : "+baiduConnection.getRequestProperty(field));
			}
//����baidu����Ϣͷ��			
			Map respons = baiduConnection.getHeaderFields();
			Set responsField=respons.keySet();
			Iterator itrRespons = responsField.iterator();
			while(itrRespons.hasNext()){
				String field= (String) itrRespons.next();
				System.out.println(field+ " : "+baiduConnection.getHeaderField(field));
			}
			baiduConnection.connect();//��ʱ����baidu��
//����baidu��ҳ��			
			InputStream baiduIn=baiduConnection.getInputStream();
			InputStreamReader baiduInBuffer=new InputStreamReader(baiduIn);
			BufferedReader baiduReader=new BufferedReader(baiduInBuffer);
			String strLine;
			while((strLine=baiduReader.readLine())!=null){
				System.out.println(strLine);
			}
			baiduReader.close();
			baiduConnection.disconnect();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}



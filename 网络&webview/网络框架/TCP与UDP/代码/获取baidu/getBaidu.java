package 获取baidu;

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
 *URL的工厂设计模式
 *setURLStreamHanderFactory(URLStreamHanderFactory fac);如果没有这个set，JVM则有个映射表；
 *URLStreamHanderFactory通过creatURLStreamHanderFactory(String protocol)进行设置；
 *工厂模式适用于有一个或者多个协议的情况
 *X x1 x2<---factory<---根据getclass()=（creatURLStreamHanderFactory(String protocol)），
 *根据传入的参数a=(String protocol)，自动会返回接口的对应的对象，
 *如果未来不知道调用那个协议，或者不希望别人知道要调用哪个子类，就使用工厂模式；
 */
public class getBaidu {
	public static void main(String[] args) throws Exception{
		System.out.println("取出日文页面");
		getContentByLanguage("ja");
		/*System.out.println('\n');
		System.out.println("取出中文页面");
		getContentByLanguage("zh-cn");*/
	}
	public static void getContentByLanguage(String country){
		try {
//链接百度，设置语言，此时没有连接
			URL urlBaidu = new URL("http://60.205.150.100:8001/Service/GetServiceInfo.ashx?Action=ValidateLogin&content={%27appid%27:%27101%27,%27username%27:%27admin%27,%27password%27:%27123456%27}");
			//URL urlBaidu=new URL("http://www.baidu.com");
			
			HttpURLConnection baiduConnection =(HttpURLConnection) urlBaidu.openConnection();
			baiduConnection.setRequestProperty("Accept-Language",country);
//得到消息头属性		
			Map requests = baiduConnection.getRequestProperties();
			Set reqField=requests.keySet();
			Iterator itrReq = reqField.iterator();
			while(itrReq.hasNext()){//迭代器的思想与节点流的输入输出的思想一致；
				String field= (String) itrReq.next();
				System.out.println(field+ " : "+baiduConnection.getRequestProperty(field));
			}
//返回baidu的消息头：			
			Map respons = baiduConnection.getHeaderFields();
			Set responsField=respons.keySet();
			Iterator itrRespons = responsField.iterator();
			while(itrRespons.hasNext()){
				String field= (String) itrRespons.next();
				System.out.println(field+ " : "+baiduConnection.getHeaderField(field));
			}
			baiduConnection.connect();//此时连接baidu；
//返回baidu的页面			
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



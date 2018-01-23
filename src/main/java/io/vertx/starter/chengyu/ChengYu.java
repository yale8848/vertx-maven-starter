package io.vertx.starter.chengyu;

import io.vertx.core.Vertx;
import io.vertx.starter.MainVerticle;
import io.vertx.starter.db.DBVerticle;
import net.dongliu.requests.Requests;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by yale on 2018/1/19.
 */
public class ChengYu {

  private static final String UA="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.48 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.39";

  private static String createPath(String path){
    File f = new File(path);
    if (!f.exists()){
      f.mkdirs();
    }
    return path;
  }
  private static void log(String text){
    System.out.println(text);
  }

  public static void main(String args[]){


    String url = "https://chengyu.911cha.com/zishu_%d.html";
    String urlp = "https://chengyu.911cha.com/zishu_%d_p%d.html";
    String path = "idiom";

    createPath(path);

    int [] pages= new int[]{0,0,0,4,20,4,3,3, 8,1,2,1,1};
    String [] clsName= new String[]{"l5","l5","l5","l5","l5","l5","l5","l5","l4","l4","l4","l3","l3"};
    for (int i = 3;i<=12;i++) {

      try {
        FileWriter fileWriter = new FileWriter(path+File.separator+""+i+".txt");
        for (int j=1;j<=pages[i];j++){
          String u = "";
          if (j != 1){
            u = String.format(urlp,i,j);
          }else{
            u = String.format(url,i);
          }
          log(u);
          String sel = String.format("ul.%s.center",clsName[i]);
          String html  = Requests.get(u).verify(false).timeout(30000).send().readToText();
          Document document = Jsoup.parse(html);
          //Connection connection = Jsoup.connect(u).ignoreContentType(true).ignoreHttpErrors(true).validateTLSCertificates(false).userAgent(UA);
           //Document document = connection.timeout(30000).get();

          Element element = document.selectFirst(sel);
          Elements elements =  element.getElementsByTag("a");
          if (elements!=null&&elements.size()>0){

            for (Element e:elements) {
              fileWriter.write(e.text()+"\r\n");
              fileWriter.flush();
            }
          }
          Thread.sleep(10000);
        }
        fileWriter.close();
      }catch (Exception e){
        e.printStackTrace();
      }


    }
  }
}

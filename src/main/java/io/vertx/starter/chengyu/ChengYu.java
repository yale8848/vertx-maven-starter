package io.vertx.starter.chengyu;

import io.vertx.core.Vertx;
import io.vertx.starter.MainVerticle;
import io.vertx.starter.db.DBVerticle;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by yale on 2018/1/19.
 */
public class ChengYu {


  private static void log(String text){
    System.out.println(text);
  }

  public static void main(String args[]){


    String url = "https://chengyu.911cha.com/zishu_%d.html";
    String urlp = "https://chengyu.911cha.com/zishu_%d_p%d.html";

    int [] pages= new int[]{0,0,0,4,20,4,3,3, 8,1,2,1,1};

    for (int i = 3;i<=12;i++) {


       for (int j=1;j<=pages[i];j++){
          String u = String.format(url,i);
          if (j != 1){
             u = String.format(urlp,i,j);
          }
         try {
           Jsoup.connect(url).get().selectFirst("");
         } catch (IOException e) {
           e.printStackTrace();
         }
       }
    }
  }
}

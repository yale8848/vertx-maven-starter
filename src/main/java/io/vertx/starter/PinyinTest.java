package io.vertx.starter;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.github.stuxuhai.jpinyin.PinyinResource;
import io.vertx.core.Vertx;
import io.vertx.starter.pinyin.PinyinResource1;
import net.dongliu.requests.Requests;

import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * test
 * Yale
 *
 * @create 2018-01-19 18:17
 **/
public class PinyinTest {

  protected static Reader newClassPathReader(String classpath) {
    InputStream is = PinyinResource.class.getResourceAsStream(classpath);

    try {
      return new InputStreamReader(is, "UTF-8");
    } catch (UnsupportedEncodingException var3) {
      return null;
    }
  }
  public static String hexStringToString(String s) throws UnsupportedEncodingException {
    if (s == null || s.equals("")||s.length()!=4) {
      return null;
    }
    byte[] baKeyword = new byte[2];

    int count=0;
    for (int i = 0; i < s.length(); ) {
        baKeyword[count++] = (byte) (0xff & Integer.parseInt(s.substring(i,i+2)
          , 16));
        i =i+2;
    }
    return   new String(baKeyword, "utf-16");
  }


  public static void main(String args[]){


    BufferedWriter fileWriter=null;
    try {
      fileWriter  = new BufferedWriter(new FileWriter("duoyin.txt"));
    } catch (IOException e) {
      e.printStackTrace();
    }


    Map<String, String> map =  PinyinResource1.getPinyinResource();

    System.out.println("map size :"+map.size());

    int count =0;
    for (Map.Entry<String, String> en:map.entrySet()) {
      StringBuilder sb = new StringBuilder();
      String key = en.getKey();


      try {
        key = URLEncoder.encode(key).replaceAll("%","").toLowerCase().trim();
        String resp = Requests.get("http://www.fantizi5.com/xingjinzi/json/"+key+".html").send().readToText();
        String[] text=null;
        if (resp!=null&&resp.length()>0){
          resp = resp.replaceAll("\r\n","");
          text = resp.split("\\$");
        }
        String tt="";
        if (text!=null&&text.length>0){

          for (String t :text) {

            String c = hexStringToString(t);
            if (c!=null){
              sb.append(c+",");
            }
          }
        }
      }catch (Exception e){
        e.printStackTrace();
      }

      if (sb.length()>0){
        sb.insert(0,en.getKey()+":");
        if (sb.charAt(sb.length()-1)==','){
          sb.deleteCharAt(sb.length()-1);
        }
        try {
          count++;
          fileWriter.write(sb.toString()+"\r\n");
          fileWriter.flush();
          System.out.println("success: "+ en.getKey()+" "+count);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    try {
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  public static void main1(String args[]){

    try {
      String str = "乐";
      String p = PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_MARK); // nǐ,hǎo,shì,jiè
      System.out.println(p);

      p = PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_NUMBER); // ni3,hao3,shi4,jie4
      System.out.println(p);
      p = PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
      System.out.println(p);
      boolean f = PinyinHelper.hasMultiPinyin('乐');
      String[]  ps = PinyinHelper.convertToPinyinArray('乐',PinyinFormat.WITHOUT_TONE);
      p = PinyinHelper.getShortPinyin(str); // nhsj
      System.out.println(p);

      Map<String, String> map =  PinyinResource1.getPinyinResource();
      p = Pinyin.toPinyin('乐');
      System.out.println(p);
      p = Pinyin.toPinyin("乐", ",");
      System.out.println(p);



    } catch (PinyinException e) {
      e.printStackTrace();
    }

  }
}

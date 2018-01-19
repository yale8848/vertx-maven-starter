package io.vertx.starter.pinyin;

import com.github.stuxuhai.jpinyin.PinyinResource;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PinyinResource1
 * Yale
 *
 * @create 2018-01-19 18:34
 **/
public class PinyinResource1 {
  public PinyinResource1() {
  }

  protected static Reader newClassPathReader(String classpath) {
    InputStream is = PinyinResource.class.getResourceAsStream(classpath);

    try {
      return new InputStreamReader(is, "UTF-8");
    } catch (UnsupportedEncodingException var3) {
      return null;
    }
  }

  protected static Reader newFileReader(String path) throws FileNotFoundException {
    try {
      return new InputStreamReader(new FileInputStream(path), "UTF-8");
    } catch (UnsupportedEncodingException var2) {
      return null;
    }
  }

  protected static Map<String, String> getResource(Reader reader) {
    ConcurrentHashMap map = new ConcurrentHashMap();

    try {
      BufferedReader br = new BufferedReader(reader);
      String line = null;

      while((line = br.readLine()) != null) {
        String[] tokens = line.trim().split("=");
        map.put(tokens[0], tokens[1]);
      }

      br.close();
      return map;
    } catch (IOException var5) {
      throw new RuntimeException(var5);
    }
  }

  public static Map<String, String> getPinyinResource() {
    return getResource(newClassPathReader("/data/pinyin.dict"));
  }

  protected static Map<String, String> getMutilPinyinResource() {
    return getResource(newClassPathReader("/data/mutil_pinyin.dict"));
  }

  public static Map<String, String> getChineseResource() {
    return getResource(newClassPathReader("/data/chinese.dict"));
  }
}

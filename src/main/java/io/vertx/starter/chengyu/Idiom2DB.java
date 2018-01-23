package io.vertx.starter.chengyu;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import org.joda.time.format.DateTimeFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;

/**
 * Yale
 *
 * @create 2018-01-23 10:45
 **/
public class Idiom2DB extends AbstractVerticle{

  private static Vertx vertx;
  private JDBCClient jdbc;
  private int count=0;
  public static void main(String args[]){
    vertx = Vertx.vertx();
    DeploymentOptions dep = new DeploymentOptions();
    dep.setWorker(true).setWorkerPoolSize(2);
    vertx.deployVerticle(Idiom2DB.class.getName(),dep);
  }
  private static void log(String text){
    System.out.println(text);
  }

  private void startdb() throws Exception {

    try {
      String path = "idiom";
      File f = new File(path);
      File [] files = f.listFiles();
      for (File ft:files) {
        String len = ft.getName().replace(".txt","");
        String p = ft.getAbsolutePath();
        BufferedReader reader = new BufferedReader(new FileReader(p));
        log(ft.getName());
        String line = "";
        while ((line = reader.readLine())!=null){
          if (line!=null&&line.length()>0){
            count++;
            log(count+"");
            final String l = line.trim();
            jdbc.getConnection(res -> {
              if (res.succeeded()) {
                SQLConnection connection = res.result();
                String sql = "INSERT INTO tb_idiom VALUES(null,?,?,NOW())";
                log(l);
                JsonArray params = new JsonArray().add(l).add(len);
                connection.updateWithParams(sql,params,updateResultAsyncResult -> {
                  UpdateResult result = updateResultAsyncResult.result();
                  System.out.println("result.getUpdated() "+result.getUpdated());

                });

                connection.close();

              } else {
                System.out.println("get connection error");
              }
            });
          }

        }
        log("finish: "+count);

      }
    }catch (Exception e){
      e.printStackTrace();

    }
  }
  private void iniConfig(){
    ConfigStoreOptions fileStore = new ConfigStoreOptions()
      .setType("file")
      .setConfig(new JsonObject().put("path", "config/db.json"));

    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
      .addStore(fileStore);

    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    retriever.getConfig(jsonObjectAsyncResult -> {
      JsonObject jo;
      jo = jsonObjectAsyncResult.result().getJsonObject("test");
      jdbc = JDBCClient.createShared(vertx, jo, "dxh-writer");

      jdbc.getConnection(ar -> {
        if (ar.failed()){

        }else{
          try {
            startdb();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });

    });
  }
  @Override
  public void start() throws Exception {
    iniConfig();
  }
}

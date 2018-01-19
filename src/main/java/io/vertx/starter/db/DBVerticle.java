package io.vertx.starter.db;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * DB thread
 * Yale
 *
 * @create 2018-01-19 14:42
 **/
public class DBVerticle extends AbstractVerticle{

  private JDBCClient jdbc;

  private void iniConfig(Future<Void> startFuture){
    ConfigStoreOptions fileStore = new ConfigStoreOptions()
      .setType("file")
      .setConfig(new JsonObject().put("path", "config/db.json"));

    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
     .addStore(fileStore);

    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    retriever.getConfig(jsonObjectAsyncResult -> {
      JsonObject jo;
      jo = jsonObjectAsyncResult.result().getJsonObject("test");
      jdbc = JDBCClient.createShared(vertx, jo, "My-Whisky-Collection");

      jdbc.getConnection(ar -> {
        if (ar.failed()){
          startFuture.fail(ar.cause());
        }else{
          startFuture.complete();
        }
      });

    });
  }

  private void configDB(Future<Void> startFuture){
    iniConfig(startFuture);
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    configDB(startFuture);


  }
}

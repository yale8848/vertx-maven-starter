package io.vertx.starter;

import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.starter.model.Whisky;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {




  private Map<Integer, Whisky> products = new LinkedHashMap<>();




  @Override
  public void start(Future<Void> startFuture) {

    JsonObject jsonObject = config();

    Router router = Router.router(vertx);

    router.route("/").handler(routingContext -> {
      HttpServerResponse response= routingContext.response();
      response.putHeader("content-type","text/html").
        end("hello");
    });

    router.route("/assets/*").handler(StaticHandler.create("assets"));

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(jsonObject.getInteger("http.port",8080),
          httpServerAsyncResult -> {
          if (httpServerAsyncResult.succeeded()){
            System.out.println("listen at 8080");
            startFuture.complete();
          }else{
            startFuture.fail(httpServerAsyncResult.cause());
          }
          });
  }



}

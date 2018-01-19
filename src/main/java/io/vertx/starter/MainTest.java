package io.vertx.starter;

import io.vertx.core.Vertx;
import io.vertx.starter.db.DBVerticle;

/**
 * MainTest
 * Yale
 *
 * @create 2018-01-19 10:03
 **/
public class MainTest {

  private static Vertx vertx;

  public static void main(String args[]){

    vertx = Vertx.vertx();
    vertx.deployVerticle(DBVerticle.class.getName());
    vertx.deployVerticle(MainVerticle.class.getName());
  }
}

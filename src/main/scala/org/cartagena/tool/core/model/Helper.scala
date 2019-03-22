package org.cartagena.tool.core.model

sealed trait Client

trait HttpClient extends Client

trait JdbcClient extends Client

trait NoClient extends Client

sealed trait Operations

trait HttpOperations extends Operations

trait JdbcOperations extends Operations

trait NoOperations extends Operations

trait Helper {
  self: Client with Operations =>
}

trait JdbcHelper extends {
  self: JdbcClient with JdbcOperations =>
}

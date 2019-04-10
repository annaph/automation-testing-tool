package org.cartagena.tool.core.model

sealed trait NativeClient

trait HttpNativeClient extends NativeClient

trait JdbcNativeClient extends NativeClient

trait NoNativeClient extends NativeClient

sealed trait Operations

trait HttpOperations extends Operations

trait JdbcOperations extends Operations

trait NoOperations extends Operations

trait Helper {
  self: NativeClient with Operations =>
}

trait JdbcHelper extends {
  self: JdbcNativeClient with JdbcOperations =>
}

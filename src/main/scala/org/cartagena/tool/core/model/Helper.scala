package org.cartagena.tool.core.model

sealed trait NativeClientComponent

trait HttpNativeClientComponent extends NativeClientComponent

trait JdbcNativeClientComponent extends NativeClientComponent

trait NoNativeClientComponent extends NativeClientComponent

sealed trait OperationsComponent

trait HttpOperations extends OperationsComponent

trait JdbcOperations extends OperationsComponent

trait NoOperations extends OperationsComponent

trait Helper {
  self: NativeClientComponent with OperationsComponent =>
}

trait JdbcHelper extends {
  self: JdbcNativeClientComponent with JdbcOperations =>
}

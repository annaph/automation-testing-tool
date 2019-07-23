package org.cartagena.tool.core.model

sealed trait NativeClientComponent

trait HttpNativeClientComponent extends NativeClientComponent

trait JdbcNativeClientComponent extends NativeClientComponent

trait JsonNativeClientComponent extends NativeClientComponent

sealed trait OperationsComponent

trait HttpOperationsComponent extends OperationsComponent

trait JsonOperationsComponent extends OperationsComponent

trait JdbcOperationsComponent extends OperationsComponent

trait NoOperationsComponent extends OperationsComponent

trait HelperComponent {
  self: NativeClientComponent with OperationsComponent =>
}

trait RestHelperComponent {
  self: HttpNativeClientComponent with HttpOperationsComponent =>
}

trait JsonHelperComponent {
  self: JsonNativeClientComponent with JsonOperationsComponent =>
}

trait JdbcHelperComponent extends {
  self: JdbcNativeClientComponent with JdbcOperationsComponent =>
}

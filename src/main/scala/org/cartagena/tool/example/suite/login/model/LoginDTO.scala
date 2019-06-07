package org.cartagena.tool.example.suite.login.model

case class LoginDTO(timestamp: Long,
                    status: Int,
                    error: String,
                    message: String, path: String)

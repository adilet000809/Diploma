package com.diploma.app.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User is already registered")
class NotUniqueUserException(message: String): RuntimeException(message)

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User email not found")
class UserNotFoundException(message: String): Exception(message)
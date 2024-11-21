package com.quo.booksManage.controller

import com.quo.booksManage.BadRequestException
import com.quo.booksManage.ConflictException
import com.quo.booksManage.NoContentException
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(NoContentException::class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun handleNoContentException(){}

    @ExceptionHandler(ConflictException::class,OptimisticLockingFailureException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflictException(){}

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(){}

}
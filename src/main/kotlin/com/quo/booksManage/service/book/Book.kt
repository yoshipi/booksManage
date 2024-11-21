package com.quo.booksManage.service.book

import java.math.BigDecimal

data class Book(
    val id: Int?,
    val title: String?,
    val price: BigDecimal?,
    val authorList: List<Int>?,
    val status: String?,
    val version: Long?
)
package com.quo.booksManage.service.author

import java.time.LocalDate

data class Author (
    val id: Int?,
    val name: String?,
    val birthDate: LocalDate?,
    val version: Long?

)
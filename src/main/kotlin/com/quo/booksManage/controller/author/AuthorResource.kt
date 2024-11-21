package com.quo.booksManage.controller.author

import jakarta.validation.constraints.*
import java.time.LocalDate

data class AuthorResource(

    @field:NotNull(groups = [PutMembers::class])
    @field:Null(groups = [PostMembers::class])
    val id: Int?,

    @field:NotEmpty(groups = [PostMembers::class])
    @field:Size(min = 1, max = 1000, groups = [PostMembers::class, PutMembers::class])
    val name: String?,

    @field:NotNull(groups = [PostMembers::class])
    // 生年月日（現在の日付よりも過去であること）
    @field:Past(groups = [PostMembers::class, PutMembers::class])
    val birthDate: LocalDate?,

    @field:NotNull(groups = [PutMembers::class])
    @field:Null(groups = [PostMembers::class])
    val version: Long?
) {
    interface PostMembers
    interface PutMembers
}


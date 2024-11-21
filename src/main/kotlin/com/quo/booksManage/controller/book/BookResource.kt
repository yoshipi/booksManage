package com.quo.booksManage.controller.book

import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class BookResource(

    @field:NotNull(groups = [PutMembers::class])
    @field:Null(groups = [PostMembers::class])
    val id: Int?,

    @field:NotEmpty(groups = [PostMembers::class])
    @field:Size(min = 1, max = 1000, groups = [PostMembers::class, PutMembers::class])
    val title: String?,

    @field:NotNull(groups = [PostMembers::class])
    // 価格（0以上であること）
    @field:PositiveOrZero(groups = [PostMembers::class, PutMembers::class])
    @field:Digits(integer = 10, fraction = 2, groups = [PostMembers::class, PutMembers::class])
    val price: BigDecimal?,

    //著者（最低1人の著者を持つ。複数の著者を持つことが可能）
    @field:NotNull(groups = [PostMembers::class])
    val authorList: List<Int>?,

    @field:NotEmpty(groups = [PostMembers::class])
    @field:Size(max = 1, groups = [PostMembers::class, PutMembers::class])
    val status: String?,

    @field:NotNull(groups = [PutMembers::class])
    @field:Null(groups = [PostMembers::class])
    val version: Long?
) {
    interface PostMembers
    interface PutMembers
}


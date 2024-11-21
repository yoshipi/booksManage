package com.quo.booksManage.controller.book

import com.quo.booksManage.BadRequestException
import com.quo.booksManage.StatusEnum
import com.quo.booksManage.service.book.Book
import com.quo.booksManage.service.book.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/book")
class BookController {

    @Autowired
    lateinit var bookServiceImpl: BookService

    @GetMapping
    fun getBooksByAuthorId(@RequestParam id: Int): ResponseEntity<List<BookResource>> {
        val results = bookServiceImpl.getBooks(id)
        return ResponseEntity.status(HttpStatus.OK).body(results.map { result ->
            BookResource(
                id = result.id,
                title = result.title,
                price = result.price,
                authorList = result.authorList,
                status = result.status,
                version = result.version
            )
        })
    }

    @PostMapping
    fun addBook(
        @Validated(BookResource.PostMembers::class)
        @RequestBody bookResource: BookResource
    ): ResponseEntity<BookResource> {

        if (StatusEnum.UNPUBLISHED.status != bookResource.status
            && StatusEnum.PUBLISHED.status != bookResource.status) throw BadRequestException()
        val result: Book = bookServiceImpl.addBook(
            Book(
                id = null,
                title = bookResource.title,
                price = bookResource.price,
                authorList = bookResource.authorList,
                status = bookResource.status,
                version = null
            )
        )
        val responseBookResource = BookResource(
            id = result.id,
            title = result.title,
            price = result.price,
            authorList = result.authorList,
            status = result.status,
            version = result.version
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBookResource)

    }

    @PutMapping
    fun updateBook(
        @Validated(BookResource.PutMembers::class)
        @RequestBody bookResource: BookResource
    ): ResponseEntity<BookResource> {

        if (StatusEnum.UNPUBLISHED.status != bookResource.status
            && StatusEnum.PUBLISHED.status != bookResource.status) throw BadRequestException()
        val result: Book = bookServiceImpl.updateBook(
            Book(
                id = bookResource.id,
                title = bookResource.title,
                price = bookResource.price,
                authorList = bookResource.authorList,
                status = bookResource.status,
                version = bookResource.version
            )
        )

        val responseBookResource = BookResource(
            id = result.id,
            title = result.title,
            price = result.price,
            authorList = result.authorList,
            status = result.status,
            version = result.version
        )
        return ResponseEntity.status(HttpStatus.OK).body(responseBookResource)
    }

}


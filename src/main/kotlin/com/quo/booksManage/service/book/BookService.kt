package com.quo.booksManage.service.book

interface BookService {
    fun getBooks(authorId: Int): List<Book>
    fun addBook(book: Book): Book
    fun updateBook(book: Book): Book

}
package com.quo.booksManage.service.author

interface AuthorService {
    fun addAuthor(author: Author): Author
    fun updateAuthor(author: Author): Author

}
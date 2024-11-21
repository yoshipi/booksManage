package com.quo.booksManage.service.book

import com.quo.booksManage.BadRequestException
import com.quo.booksManage.ConflictException
import com.quo.booksManage.NoContentException
import com.quo.booksManage.StatusEnum
import com.quo.booksManage.db.tables.Authors.AUTHORS
import com.quo.booksManage.db.tables.Books.BOOKS
import com.quo.booksManage.db.tables.BooksAuthors.BOOKS_AUTHORS
import org.springframework.stereotype.Service
import org.jooq.*
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class BookServiceImpl(private val create: DSLContext) : BookService {
    override fun getBooks(authorId: Int): List<Book> {
        val records = create.select(
            BOOKS.ID, BOOKS.TITLE, BOOKS.PRICE, BOOKS_AUTHORS.AUTHORS_ID, BOOKS.STATUS, BOOKS.VERSION
        ).from(BOOKS).join(BOOKS_AUTHORS).on(BOOKS.ID.eq(BOOKS_AUTHORS.BOOKS_ID))
            .where(BOOKS_AUTHORS.AUTHORS_ID.eq(authorId)).fetch()
        val authorList = records.map { it.get(BOOKS_AUTHORS.AUTHORS_ID) }
        return records.map { record ->
            Book(
                id = record.getValue(BOOKS.ID),
                title = record.getValue(BOOKS.TITLE),
                price = record.getValue(BOOKS.PRICE),
                authorList = authorList,
                status = record.getValue(BOOKS.STATUS),
                version = record.getValue(BOOKS.VERSION)
            )
        }

    }

    override fun addBook(book: Book): Book {
        // 著者が登録されているかチェック
        if(isAuthorExist(book.authorList!!)){
            throw BadRequestException()
        }

        // BOOKSテーブルへの登録
        val booksRecord = create.insertInto(BOOKS)
            .set(BOOKS.TITLE, book.title)
            .set(BOOKS.PRICE, book.price)
            .set(BOOKS.STATUS, book.status)
            .returning(BOOKS.ID, BOOKS.TITLE, BOOKS.PRICE, BOOKS.STATUS, BOOKS.VERSION)
            .fetchOne()

        // BOOKS_AUTHORSテーブルへのバルクインサート
        val bulkInsert = create.insertInto(BOOKS_AUTHORS)
            .columns(BOOKS_AUTHORS.BOOKS_ID, BOOKS_AUTHORS.AUTHORS_ID)
        book.authorList.map { bulkInsert.values(booksRecord!!.getValue(BOOKS.ID), it) }
        val authorsRecords = bulkInsert.returning(BOOKS_AUTHORS.AUTHORS_ID).fetch()

        return Book(
            id = booksRecord!!.getValue(BOOKS.ID),
            title = booksRecord.getValue(BOOKS.TITLE),
            price = booksRecord.getValue(BOOKS.PRICE),
            authorList = authorsRecords.map{it.getValue(BOOKS_AUTHORS.AUTHORS_ID) },
            status = booksRecord.getValue(BOOKS.STATUS),
            version = booksRecord.getValue(BOOKS.VERSION)
        )
    }

    override fun updateBook(book: Book): Book {
        val status = create.select(BOOKS.STATUS).from(BOOKS).where(BOOKS.ID.eq(book.id)).fetchOne()
            ?: throw NoContentException()

        //出版状況（未出版、出版済み。出版済みステータスのものを未出版には変更できない）
        if(StatusEnum.UNPUBLISHED.status != status.getValue(BOOKS.STATUS)) throw ConflictException()

        // BOOKS_AUTHORSから著者を削除と登録
        create.delete(BOOKS_AUTHORS).where(BOOKS_AUTHORS.AUTHORS_ID.notIn(book.authorList)).execute()
        val bulkInsert = create.insertInto(BOOKS_AUTHORS)
            .columns(BOOKS_AUTHORS.BOOKS_ID, BOOKS_AUTHORS.AUTHORS_ID)
        book.authorList!!.map { bulkInsert.values(book.id, it) }
        val authorsRecords = bulkInsert.onConflict(BOOKS_AUTHORS.BOOKS_ID, BOOKS_AUTHORS.AUTHORS_ID)
            .doNothing().returning(BOOKS_AUTHORS.AUTHORS_ID).fetch()

        val record = create.update(BOOKS)
            .set(BOOKS.TITLE, book.title)
            .set(BOOKS.PRICE, book.price)
            .set(BOOKS.STATUS, book.status)
            .set(BOOKS.VERSION, BOOKS.VERSION.plus(1))
            .where(BOOKS.ID.eq(book.id).and(BOOKS.VERSION.eq(book.version)))
            .returningResult(BOOKS.ID, BOOKS.TITLE, BOOKS.PRICE, BOOKS.STATUS, BOOKS.VERSION)
            .fetchOne()
        if (record == null) throw OptimisticLockingFailureException("Failed to update book with ID ${book.id} due to version is mismatch.")

        return Book(
            id = record.getValue(BOOKS.ID),
            title = record.getValue(BOOKS.TITLE),
            price = record.getValue(BOOKS.PRICE),
            authorList = authorsRecords.map{it.getValue(BOOKS_AUTHORS.AUTHORS_ID) },
            status = record.getValue(BOOKS.STATUS),
            version = record.getValue(BOOKS.VERSION)
        )

    }

    private fun isAuthorExist(authorList:List<Int>):Boolean {
        val authorCount = create.selectCount().from(AUTHORS).where(AUTHORS.ID.`in`(authorList)).fetchOne(0,Int::class.java)
        return authorCount != authorList.count()
    }
}
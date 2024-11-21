package com.quo.booksManage.service.author

import com.quo.booksManage.db.tables.Authors.AUTHORS
import org.jooq.DSLContext
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AuthorServiceImpl(private val create: DSLContext) : AuthorService {

    override fun addAuthor(author: Author): Author {
        val record = create.insertInto(AUTHORS)
            .set(AUTHORS.NAME, author.name)
            .set(AUTHORS.BIRTH_DATE, author.birthDate)
            .returning(AUTHORS.ID, AUTHORS.NAME, AUTHORS.BIRTH_DATE, AUTHORS.VERSION)
            .fetchOne()

        return Author(
            id = record!!.getValue(AUTHORS.ID),
            name = record.getValue(AUTHORS.NAME),
            birthDate = record.getValue(AUTHORS.BIRTH_DATE),
            version = record.getValue(AUTHORS.VERSION)
        )
    }

    override fun updateAuthor(author: Author): Author {
        val record = create.update(AUTHORS)
            .set(AUTHORS.NAME, author.name)
            .set(AUTHORS.BIRTH_DATE, author.birthDate)
            .set(AUTHORS.VERSION, AUTHORS.VERSION.plus(1))
            .where(AUTHORS.ID.eq(author.id).and(AUTHORS.VERSION.eq(author.version)))
            .returning(AUTHORS.ID, AUTHORS.NAME, AUTHORS.BIRTH_DATE, AUTHORS.VERSION)
            .fetchOne()
        if (record == null) throw OptimisticLockingFailureException("Failed to update author with ID ${author.id} due to version is mismatch.")

        return Author(
            id = record.getValue(AUTHORS.ID),
            name = record.getValue(AUTHORS.NAME),
            birthDate = record.getValue(AUTHORS.BIRTH_DATE),
            version = record.getValue(AUTHORS.VERSION)
        )

    }
}
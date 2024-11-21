package com.quo.booksManage.controller.author

import com.quo.booksManage.service.author.Author
import com.quo.booksManage.service.author.AuthorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/author")
class AuthorController {

    @Autowired
    lateinit var authorServiceImpl: AuthorService

    @PostMapping
    fun addAuthor(@Validated(AuthorResource.PostMembers::class)
                @RequestBody authorResource: AuthorResource
    ): ResponseEntity<AuthorResource> {

            val result: Author = authorServiceImpl.addAuthor(
                Author(
                    id = null,
                    name = authorResource.name,
                    birthDate = authorResource.birthDate,
                    version = null
                )
            )
            val responseAuthorResource = AuthorResource(
                id = result.id,
                name = result.name,
                birthDate = result.birthDate,
                version = result.version
            )
            return ResponseEntity.status(HttpStatus.CREATED).body(responseAuthorResource)

    }

    @PutMapping
    fun updateAuthor(@Validated(AuthorResource.PutMembers::class)
                   @RequestBody authorResource: AuthorResource
    ): ResponseEntity<AuthorResource> {

        val result: Author = authorServiceImpl.updateAuthor(
            Author(
                id = authorResource.id,
                name = authorResource.name,
                birthDate = authorResource.birthDate,
                version = authorResource.version
            )
        )

        val responseAuthorResource = AuthorResource(
            id = result.id,
            name = result.name,
            birthDate = result.birthDate,
            version = result.version
        )
        return ResponseEntity.status(HttpStatus.OK).body(responseAuthorResource)
    }
}


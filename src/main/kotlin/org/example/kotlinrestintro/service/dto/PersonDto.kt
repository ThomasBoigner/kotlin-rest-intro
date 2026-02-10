package org.example.kotlinrestintro.service.dto

import org.example.kotlinrestintro.domain.Person
import java.time.LocalDateTime
import java.util.UUID

data class PersonDto(
    val token: UUID,
    val firstName: String,
    val lastName: String,
    val creationTS: LocalDateTime
) {
    constructor(person: Person) : this(
        person.token,
        person.firstname,
        person.lastname,
        person.creationTS
    )
}

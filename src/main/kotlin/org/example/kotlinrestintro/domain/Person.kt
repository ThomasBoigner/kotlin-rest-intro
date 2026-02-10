package org.example.kotlinrestintro.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.time.LocalDateTime
import java.util.UUID

@Entity
class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var firstname: String,
    var lastname: String,
    val token: UUID = UUID.randomUUID(),
    val creationTS: LocalDateTime = LocalDateTime.now(),
    @OneToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], mappedBy = "owner")
    val albums: MutableList<Album> = mutableListOf(),
) {
    constructor(): this(
        firstname = "",
        lastname = ""
    )

    override fun toString(): String {
        return "Person(id=$id, firstname='$firstname', lastname='$lastname', token=$token, creationTS=$creationTS)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Person

        return token == other.token
    }

    override fun hashCode(): Int {
        return token.hashCode()
    }
}
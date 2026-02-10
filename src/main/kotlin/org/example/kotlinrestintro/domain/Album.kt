package org.example.kotlinrestintro.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.LocalDateTime
import java.util.UUID

@Entity
class Album(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var albumName: String,
    val token: UUID = UUID.randomUUID(),
    val creationTS: LocalDateTime = LocalDateTime.now(),
    @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    @JoinColumn(foreignKey = ForeignKey(name = "fk_person"))
    var owner: Person,
    @OneToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], mappedBy = "album")
    val photos: MutableList<Photo> = mutableListOf(),
) {
    constructor(): this(
        albumName = "",
        owner = Person(),
    )

    override fun toString(): String {
        return "Album(id=$id, albumName='$albumName', token=$token, creationTS=$creationTS)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Album

        return token == other.token
    }

    override fun hashCode(): Int {
        return token.hashCode()
    }
}
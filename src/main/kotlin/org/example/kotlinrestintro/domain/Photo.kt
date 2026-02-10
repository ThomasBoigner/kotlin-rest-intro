package org.example.kotlinrestintro.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime
import java.util.UUID

@Entity
class Photo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var photoName: String,
    var photoWidth: Int,
    var photoHeight: Int,
    val token: UUID = UUID.randomUUID(),
    val creationTS: LocalDateTime = LocalDateTime.now(),
    @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    @JoinColumn(foreignKey = ForeignKey(name = "fk_album"))
    var album: Album? = null
) {
    constructor(): this(
        photoName = "",
        photoWidth = 0,
        photoHeight = 0,
    )

    override fun toString(): String {
        return "Photo(creationTS=$creationTS, token=$token, photoHeight=$photoHeight, photoWidth=$photoWidth, photoName='$photoName', id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Photo

        return token == other.token
    }

    override fun hashCode(): Int {
        return token.hashCode()
    }
}
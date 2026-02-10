package org.example.kotlinrestintro.service.dto

import org.example.kotlinrestintro.domain.Album
import java.time.LocalDateTime
import java.util.UUID

data class AlbumDto(
    val token: UUID,
    val albumName: String,
    val creationTS: LocalDateTime,
    val owner: PersonDto
) {
    constructor(album: Album) : this(
        album.token,
        album.albumName,
        album.creationTS,
        PersonDto(album.owner)
    )
}
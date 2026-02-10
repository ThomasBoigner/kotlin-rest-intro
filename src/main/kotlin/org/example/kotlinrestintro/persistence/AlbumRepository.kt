package org.example.kotlinrestintro.persistence

import org.example.kotlinrestintro.domain.Album
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AlbumRepository: JpaRepository<Album, Long> {
    fun findAlbumByToken(token: UUID): Album?
    fun deleteAlbumByToken(token: UUID)
}
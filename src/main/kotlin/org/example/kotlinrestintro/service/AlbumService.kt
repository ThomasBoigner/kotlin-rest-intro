package org.example.kotlinrestintro.service

import org.example.kotlinrestintro.domain.Album
import org.example.kotlinrestintro.domain.Person
import org.example.kotlinrestintro.persistence.AlbumRepository
import org.example.kotlinrestintro.service.dto.AlbumDto
import org.example.kotlinrestintro.service.dto.commands.CreateAlbumCommand
import org.example.kotlinrestintro.service.dto.commands.UpdateAlbumCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class AlbumService(
    private val albumRepository: AlbumRepository,
    private val log: Logger = LoggerFactory.getLogger(AlbumService::class.java)
) {
    fun getAlbums(): List<AlbumDto> {
        log.debug("Trying to get all albums")
        val albums = albumRepository.findAll()
        log.info("Found all ({}) albums", albums.size)
        return albums.map { AlbumDto(it) }
    }

    fun getAlbum(albumToken: UUID): AlbumDto? {
        log.debug("Trying to get album with token {}", albumToken)
        val album = albumRepository.findAlbumByToken(albumToken)
        log.info(album?.let {"Found Album $album"} ?: "Album could not be found")
        return album?.let { AlbumDto(it) }
    }

    @Transactional(readOnly = false)
    fun createAlbum(command: CreateAlbumCommand): AlbumDto {
        log.debug("Trying to create a new album with command {}", command.albumName)
        val owner = Person(
            firstname = command.firstName,
            lastname = command.lastName,
        )

        val album = Album(
            albumName = command.albumName,
            owner = owner,
        )

        log.info("Created new album {}", album)
        return AlbumDto(albumRepository.save(album))
    }

    @Transactional(readOnly = false)
    fun updateAlbum(token: UUID, command: UpdateAlbumCommand): AlbumDto {
        log.debug("Trying to update album with token {} with command {}", token, command)
        val album = albumRepository.findAlbumByToken(token)
            ?: throw IllegalArgumentException("Album with token $token can not be found!")

        if (!command.albumName.isNullOrBlank()) { album.albumName = command.albumName }
        if (!command.firstName.isNullOrBlank()) { album.owner.firstname = command.firstName }
        if (!command.lastName.isNullOrBlank()) { album.owner.lastname = command.lastName }

        return AlbumDto(albumRepository.save(album))
    }

    @Transactional(readOnly = false)
    fun deleteAlbums() {
        log.debug("Trying to delete all albums")
        albumRepository.deleteAll()
    }

    @Transactional(readOnly = false)
    fun deleteAlbum(albumToken: UUID) {
        log.debug("Trying to delete album with token {}", albumToken)
        albumRepository.deleteAlbumByToken(albumToken)
    }
}
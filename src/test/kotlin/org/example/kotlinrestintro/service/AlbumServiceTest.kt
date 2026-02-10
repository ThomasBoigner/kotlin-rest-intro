package org.example.kotlinrestintro.service

import org.assertj.core.api.Assertions.assertThat
import org.example.kotlinrestintro.domain.Album
import org.example.kotlinrestintro.domain.Person
import org.example.kotlinrestintro.persistence.AlbumRepository
import org.example.kotlinrestintro.service.dto.AlbumDto
import org.example.kotlinrestintro.service.dto.commands.CreateAlbumCommand
import org.example.kotlinrestintro.service.dto.commands.UpdateAlbumCommand
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class AlbumServiceTest {
    lateinit var albumService: AlbumService
    @Mock
    lateinit var albumRepository: AlbumRepository

    @BeforeEach
    fun setUp() {
        albumService = AlbumService(albumRepository)
    }

    @Test
    fun ensureGetAlbumsWorksProperly() {
        // Given
        val album = Album(
            albumName = "Album 1",
            owner = Person(
                firstname = "John",
                lastname = "Smith"
            )
        )

        `when`(albumRepository.findAll()).thenReturn(listOf(album))

        // When
        val result = albumService.getAlbums()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result).contains(AlbumDto(album))
    }

    @Test
    fun ensureGetAlbumWorksProperly() {
        // Given
        val album = Album(
            albumName = "Album 1",
            owner = Person(
                firstname = "John",
                lastname = "Smith"
            ),
        )

        `when`(albumRepository.findAlbumByToken(album.token)).thenReturn(album)

        // When
        val result = albumService.getAlbum(album.token)

        // Then
        assertThat(result).isEqualTo(AlbumDto(album))
    }

    @Test
    fun ensureCreateAlbumWorksProperly() {
        // Given
        val command = CreateAlbumCommand(
            albumName = "Album 1",
            firstName = "John",
            lastName = "Smith",
        )
        `when`(albumRepository.save(any())).thenAnswer { it.arguments[0] }

        // When
        val result = albumService.createAlbum(command)

        // Then
        assertThat(result.albumName).isEqualTo(command.albumName)
        assertThat(result.owner.firstName).isEqualTo(command.firstName)
        assertThat(result.owner.lastName).isEqualTo(command.lastName)
    }

    @Test
    fun ensureUpdateAlbumWorksProperly() {
        // Given
        val album = Album(
            albumName = "Album 1",
            owner = Person(
                firstname = "John",
                lastname = "Smith"
            ),
        )

        val command = UpdateAlbumCommand(
            albumName = "Album 2",
            firstName = "Max",
            lastName = "Doe",
        )
        `when`(albumRepository.findAlbumByToken(album.token)).thenReturn(album)
        `when`(albumRepository.save(any())).thenAnswer { it.arguments[0] }

        // When
        val result = albumService.updateAlbum(album.token, command)

        // Then
        assertThat(result.albumName).isEqualTo(command.albumName)
        assertThat(result.owner.firstName).isEqualTo(command.firstName)
        assertThat(result.owner.lastName).isEqualTo(command.lastName)
    }

    @Test
    fun ensureUpdateAlbumThrowsExceptionWhenAlbumCanNotBeFound() {
        // Given
        val album = Album(
            albumName = "Album 1",
            owner = Person(
                firstname = "John",
                lastname = "Smith"
            ),
        )

        val command = UpdateAlbumCommand(
            albumName = "Album 2",
            firstName = "Max",
            lastName = "Doe",
        )
        `when`(albumRepository.findAlbumByToken(album.token)).thenReturn(null)

        // When
        assertThrows<IllegalArgumentException> { albumService.updateAlbum(album.token, command) }
    }

    @Test
    fun ensureDeleteAlbumsWorksProperly() {
        // When
        albumService.deleteAlbums()

        // Then
        verify(albumRepository).deleteAll()
    }

    @Test
    fun ensureDeleteAlbumWorksProperly() {
        // Given
        val token = UUID.randomUUID()

        // When
        albumService.deleteAlbum(token)

        // Then
        verify(albumRepository).deleteAlbumByToken(token)
    }
}
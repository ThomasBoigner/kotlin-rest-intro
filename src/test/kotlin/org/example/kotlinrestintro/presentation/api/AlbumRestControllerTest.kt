package org.example.kotlinrestintro.presentation.api

import org.example.kotlinrestintro.service.AlbumService
import org.example.kotlinrestintro.service.dto.AlbumDto
import org.example.kotlinrestintro.service.dto.PersonDto
import org.example.kotlinrestintro.service.dto.commands.CreateAlbumCommand
import org.example.kotlinrestintro.service.dto.commands.UpdateAlbumCommand
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import tools.jackson.databind.json.JsonMapper
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class AlbumRestControllerTest {
    lateinit var mockMvc: MockMvc
    @Mock
    lateinit var albumService: AlbumService
    lateinit var jsonMapper: JsonMapper

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(AlbumRestController(albumService)).build()
        jsonMapper = JsonMapper.builder().build()
    }

    @Test
    fun ensureGetAlbumsWorksProperly() {
        // Given
        val albumDto = AlbumDto(
            albumName = "Album 1",
            owner = PersonDto(
                firstName = "John",
                lastName = "Smith",
                token = UUID.randomUUID(),
                creationTS = LocalDateTime.now(),
            ),
            token = UUID.randomUUID(),
            creationTS = LocalDateTime.now(),
        )

        `when`(albumService.getAlbums()).thenReturn(listOf(albumDto))

        // Perform
        mockMvc.get("/api/albums") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonMapper.writeValueAsString(listOf(albumDto)) }
        }
    }

    @Test
    fun ensureGetAlbumsReturnsNoContentIfNoAlbumWasFound() {
        // Given
        `when`(albumService.getAlbums()).thenReturn(listOf())

        // Perform
        mockMvc.get("/api/albums") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isNoContent() }
        }
    }

    @Test
    fun ensureGetAlbumWorksProperly() {
        // Given
        val albumDto = AlbumDto(
            albumName = "Album 1",
            owner = PersonDto(
                firstName = "John",
                lastName = "Smith",
                token = UUID.randomUUID(),
                creationTS = LocalDateTime.now(),
            ),
            token = UUID.randomUUID(),
            creationTS = LocalDateTime.now(),
        )

        `when`(albumService.getAlbum(albumDto.token)).thenReturn(albumDto)

        // Perform
        mockMvc.get("/api/albums/${albumDto.token}") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonMapper.writeValueAsString(albumDto) }
        }
    }

    @Test
    fun ensureGetAlbumReturnsNotFoundIfNoAlbumWasFound() {
        // Given
        val token = UUID.randomUUID()

        `when`(albumService.getAlbum(token)).thenReturn(null)

        // Perform
        mockMvc.get("/api/albums/${token}") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun ensureCreateAlbumWorksProperly() {
        val command = CreateAlbumCommand(
            albumName = "Album 1",
            firstName = "John",
            lastName = "Smith",
        )

        val albumDto = AlbumDto(
            albumName = "Album 1",
            owner = PersonDto(
                firstName = "John",
                lastName = "Smith",
                token = UUID.randomUUID(),
                creationTS = LocalDateTime.now(),
            ),
            token = UUID.randomUUID(),
            creationTS = LocalDateTime.now(),
        )

        `when`(albumService.createAlbum(command)).thenReturn(albumDto)

        // Perform
        mockMvc.post("/api/albums") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(command)
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonMapper.writeValueAsString(albumDto) }
        }
    }

    @Test
    fun ensureUpdateAlbumWorksProperly() {
        // Given
        val command = UpdateAlbumCommand(
            albumName = "Album 1",
            firstName = "John",
            lastName = "Smith",
        )

        val albumDto = AlbumDto(
            albumName = "Album 1",
            owner = PersonDto(
                firstName = "John",
                lastName = "Smith",
                token = UUID.randomUUID(),
                creationTS = LocalDateTime.now(),
            ),
            token = UUID.randomUUID(),
            creationTS = LocalDateTime.now(),
        )

        `when`(albumService.updateAlbum(albumDto.token, command)).thenReturn(albumDto)

        // Perform
        mockMvc.patch("/api/albums/${albumDto.token}") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(command)
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonMapper.writeValueAsString(albumDto) }
        }
    }

    @Test
    fun deleteAlbumsWorksProperly() {
        // Perform
        mockMvc.delete("/api/albums") {
            accept = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun deleteAlbumWorksProperly() {
        // Perform
        mockMvc.delete("/api/albums/${UUID.randomUUID()}") {
            accept = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
        }
    }
}
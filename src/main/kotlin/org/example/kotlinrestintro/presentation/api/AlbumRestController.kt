package org.example.kotlinrestintro.presentation.api

import org.example.kotlinrestintro.service.AlbumService
import org.example.kotlinrestintro.service.dto.AlbumDto
import org.example.kotlinrestintro.service.dto.commands.CreateAlbumCommand
import org.example.kotlinrestintro.service.dto.commands.UpdateAlbumCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.*

@RestController
@RequestMapping(AlbumRestController.BASE_URL)
class AlbumRestController(
    private val albumService: AlbumService,
    private val log: Logger = LoggerFactory.getLogger(AlbumRestController::class.java)
) {
    companion object {
        const val BASE_URL = "/api/albums"
        const val PATH_INDEX = "/"
        const val PATH_VAR_ID = "/{id}"
        const val ROUTE_ID = BASE_URL + PATH_INDEX
    }

    @GetMapping(value = ["", PATH_INDEX])
    fun getAlbums(): HttpEntity<List<AlbumDto>> {
        log.debug("Received http GET request to retrieve all albums")
        val albums = albumService.getAlbums()
        return when (albums.isNotEmpty()) {
            true -> ResponseEntity.ok(albums)
            false -> ResponseEntity.noContent().build()
        }
    }

    @GetMapping(PATH_VAR_ID)
    fun getAlbumById(@PathVariable id: UUID): ResponseEntity<AlbumDto> {
        log.debug("Received http GET request to retrieve album with id {}", id)
        return albumService.getAlbum(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @PostMapping(value = ["", PATH_INDEX])
    fun createAlbum(@RequestBody command: CreateAlbumCommand): ResponseEntity<AlbumDto> {
        log.debug("Received http POST request to create an album with command {}", command)
        val album = albumService.createAlbum(command)
        return ResponseEntity.created(createSelfLink(album)).body(album)
    }

    @PatchMapping(PATH_VAR_ID)
    fun updateAlbum(@PathVariable id: UUID, @RequestBody command: UpdateAlbumCommand): ResponseEntity<AlbumDto> {
        return ResponseEntity.ok(albumService.updateAlbum(id, command))
    }

    @DeleteMapping(value = ["", PATH_INDEX])
    fun deleteAlbums(): HttpEntity<Void> {
        albumService.deleteAlbums()
        return ResponseEntity.ok().build()
    }

    @DeleteMapping(PATH_VAR_ID)
    fun deleteAlbum(@PathVariable id: UUID): ResponseEntity<Void> {
        albumService.deleteAlbum(id)
        return ResponseEntity.ok().build()
    }

    private fun createSelfLink(album: AlbumDto): URI {
        val selfLink = UriComponentsBuilder
            .fromPath(ROUTE_ID)
            .uriVariables(mapOf("token" to album.token))
            .build()
            .toUri()
        log.trace("Created self link {} for album {}", selfLink, album)
        return selfLink
    }
}
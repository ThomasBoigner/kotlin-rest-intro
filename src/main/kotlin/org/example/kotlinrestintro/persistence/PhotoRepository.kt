package org.example.kotlinrestintro.persistence

import org.example.kotlinrestintro.domain.Photo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PhotoRepository: JpaRepository<Photo, Long> {
}
package org.example.kotlinrestintro.persistence

import org.example.kotlinrestintro.domain.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository: JpaRepository<Person, Long> {
}
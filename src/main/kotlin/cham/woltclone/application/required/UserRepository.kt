package cham.woltclone.application.required

import cham.woltclone.domain.user.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface UserRepository: Repository<User, Long> {
    fun save(user: User): User
    fun findById(id: Long): User?
    fun findByEmailValue(email: String): User?
    fun existsByEmailValue(email: String): Boolean
    fun deleteById(id: Long)
    fun findAll(): List<User>
    
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    fun findActiveUsers(): List<User>
}
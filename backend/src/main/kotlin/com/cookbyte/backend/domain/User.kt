package com.cookbyte.backend.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "cookbyte_user")
@JsonIgnoreProperties(ignoreUnknown = true)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    var firstName: String,
    var lastName: String?,

    @Column(unique = true)
    @get:JvmName("getUsernameProperty")
    var username: String?,

    @Column(unique = true)
    val email: String?,

    @get:JvmName("getPasswordProperty")
    val password: String?,

    var image: String?
): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String {
        return password ?: ""
    }

    override fun getUsername(): String {
        return username ?: ""
    }


    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
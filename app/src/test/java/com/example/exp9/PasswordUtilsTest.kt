package com.example.exp9

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.regex.Pattern

class PasswordUtilsTest {

    @Test
    fun testPasswordHashingAndVerification() {
        val rawPassword = "SecurePass123"
        val hashStr = PasswordUtils.hashPassword(rawPassword)

        assertTrue(hashStr.contains(":"))
        val parts = hashStr.split(":")
        assertEquals(2, parts.size)

        // Correct password verifies to true
        assertTrue(PasswordUtils.verifyPassword(rawPassword, hashStr))

        // Incorrect password verifies to false
        assertFalse(PasswordUtils.verifyPassword("WrongPass123", hashStr))
    }

    @Test
    fun testUniqueSaltsForSamePassword() {
        val rawPassword = "Password123"
        val hash1 = PasswordUtils.hashPassword(rawPassword)
        val hash2 = PasswordUtils.hashPassword(rawPassword)

        // Different salt means different hash string representation
        assertNotEquals(hash1, hash2)

        // But both verify correctly
        assertTrue(PasswordUtils.verifyPassword(rawPassword, hash1))
        assertTrue(PasswordUtils.verifyPassword(rawPassword, hash2))
    }

    @Test
    fun testPasswordValidationRegex() {
        val pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")

        assertTrue(pattern.matcher("Password123").matches())
        assertTrue(pattern.matcher("abc12345").matches())

        // Less than 8 chars
        assertFalse(pattern.matcher("Pass1").matches())

        // No digits
        assertFalse(pattern.matcher("PasswordOnly").matches())

        // No letters
        assertFalse(pattern.matcher("12345678").matches())
    }
}

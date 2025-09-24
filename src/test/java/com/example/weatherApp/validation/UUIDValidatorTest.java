package com.example.weatherApp.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UUIDValidatorTest {

    @Test
    void testAllUuidValidationCases() {
        assertTrue(UUIDValidator.isValidUuid("123e4567-e89b-12d3-a456-426614174000"), "Lowercase valid");
        assertTrue(UUIDValidator.isValidUuid("123E4567-E89B-12D3-A456-426614174000"), "Uppercase valid");
        assertTrue(UUIDValidator.isValidUuid("00000000-0000-0000-0000-000000000000"), "All zeros valid");
        assertTrue(UUIDValidator.isValidUuid("ffffffff-ffff-ffff-ffff-ffffffffffff"), "All F's valid");

        assertFalse(UUIDValidator.isValidUuid(null), "Null should be invalid");
        assertFalse(UUIDValidator.isValidUuid(""), "Empty string should be invalid");
        assertFalse(UUIDValidator.isValidUuid("123e4567-e89b-12d3-a456-42661417400"), "Too short");
        assertFalse(UUIDValidator.isValidUuid("123e4567-e89b-12d3-a456-4266141740000"), "Too long");
        assertFalse(UUIDValidator.isValidUuid("123e4567-e89b-12d3-a456-42661417400g"), "Invalid character");
        assertFalse(UUIDValidator.isValidUuid("123e4567e89b12d3a456426614174000"), "No hyphens");
    }
}

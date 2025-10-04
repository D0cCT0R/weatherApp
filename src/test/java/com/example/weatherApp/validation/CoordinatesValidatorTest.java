package com.example.weatherApp.validation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CoordinatesValidatorTest {
    @Test
    void boundaryValuesTest() {
        assertTrue(CoordinatesValidator.isValidCoordinates(-90.0, -180.0));
        assertTrue(CoordinatesValidator.isValidCoordinates(90.0, 180.0));
        assertTrue(CoordinatesValidator.isValidCoordinates(0.0, 0.0));
        assertFalse(CoordinatesValidator.isValidCoordinates(-90.1, -180.0));
        assertFalse(CoordinatesValidator.isValidCoordinates(90.1, 180.0));
        assertFalse(CoordinatesValidator.isValidCoordinates(0.0, -180.1));
        assertFalse(CoordinatesValidator.isValidCoordinates(0.0, 180.1));
    }
}


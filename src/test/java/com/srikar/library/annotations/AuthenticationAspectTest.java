package com.srikar.library.annotations;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationAspect authenticationAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAuthenticateWithValidAuthentication() throws Throwable {
        // Arrange
        Object expectedResult = new Object();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(joinPoint.proceed()).thenReturn(expectedResult);

        // Act
        Object result = authenticationAspect.authenticate(joinPoint);

        // Assert
        assertEquals(expectedResult, result);
        verify(joinPoint).proceed();
    }

    @Test
    void testAuthenticateWithNoAuthentication() throws Throwable {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        Object result = authenticationAspect.authenticate(joinPoint);

        // Assert
        assertNotNull(result);
        verify(joinPoint, never()).proceed();
    }

    @Test
    void testAuthenticateWithNotAuthenticated() throws Throwable {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act
        Object result = authenticationAspect.authenticate(joinPoint);

        // Assert
        assertNotNull(result);
        verify(joinPoint, never()).proceed();
    }

    @Test
    void testAuthenticateWithException() throws Throwable {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(joinPoint.proceed()).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authenticationAspect.authenticate(joinPoint);
        });
        
        assertEquals("Test exception", exception.getMessage());
    }
}

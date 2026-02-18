package com.dogac.user_service.domain.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.exceptions.DuplicatePhoneNumberException;
import com.dogac.user_service.domain.exceptions.DuplicateUserEmailException;
import com.dogac.user_service.domain.repositories.UserRepository;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;

@ExtendWith(MockitoExtension.class)
class UserDomainServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @InjectMocks
    private UserDomainService userDomainService;

    @Test
    void shouldThrowExceptionWhenPhoneNumberAlreadyExists() {
        PhoneNumber phoneNumber = new PhoneNumber("+905551234567");
        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);
        assertThrows(DuplicatePhoneNumberException.class, () -> userDomainService.changePhoneNumber(user, phoneNumber));
        verify(user, never()).changePhoneNumber(phoneNumber);
    }

    @Test
    void shouldChangePhoneNumberWhenPhoneNumberIsUnique() {
        // given
        PhoneNumber phoneNumber = new PhoneNumber("+905551234567");
        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(false);

        // when
        userDomainService.changePhoneNumber(user, phoneNumber);

        // then
        verify(user).changePhoneNumber(phoneNumber);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        Email email = new Email("dogac@gmail.com");
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(DuplicateUserEmailException.class, () -> userDomainService.ensureEmailIsUnique(email));
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void shouldNotThrowExceptionWhenEmailIsUnique() {
        // given
        Email email = new Email("dogac@gmail.com");
        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertDoesNotThrow(() -> userDomainService.ensureEmailIsUnique(email));
        verify(userRepository).existsByEmail(email);
    }
}

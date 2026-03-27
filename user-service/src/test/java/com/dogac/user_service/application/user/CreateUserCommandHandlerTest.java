package com.dogac.user_service.application.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dogac.user_service.application.commandHandlers.CreateUserCommandHandler;
import com.dogac.user_service.application.commands.CreateUserCommand;
import com.dogac.user_service.application.dto.CreatedUserResponse;
import com.dogac.user_service.application.mapper.CreateUserMapper;
import com.dogac.user_service.application.security.JwtUserIdProvider;
import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.enums.UserType;
import com.dogac.user_service.domain.exceptions.DuplicatePhoneNumberException;
import com.dogac.user_service.domain.exceptions.DuplicateUserEmailException;
import com.dogac.user_service.domain.repositories.UserRepository;
import com.dogac.user_service.domain.services.UserDomainService;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;
import com.dogac.user_service.domain.valueobjects.UserId;

@ExtendWith(MockitoExtension.class)
class CreateUserCommandHandlerTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private UserDomainService userDomainService;

        @Mock
        private CreateUserMapper createUserMapper;

        @Mock
        private JwtUserIdProvider jwtUserIdProvider;

        @InjectMocks
        private CreateUserCommandHandler createUserCommandHandler;

        @Test
        void shouldCreateUserSuccessfully() {
                // given
                CreateUserCommand command = new CreateUserCommand(
                                "Dogac",
                                "Ulgac",
                                "dogac@gmail.com",
                                "+905551234567",
                                UserType.CUSTOMER,
                                List.of());

                User user = mock(User.class);
                CreatedUserResponse response = mock(CreatedUserResponse.class);

                UserId userId = UserId.generate();
                when(jwtUserIdProvider.currentUserId()).thenReturn(userId);
                when(createUserMapper.toEntity(command, userId)).thenReturn(user);
                when(createUserMapper.toResponse(user)).thenReturn(response);

                // when
                CreatedUserResponse result = createUserCommandHandler.handle(command);

                // then
                ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

                verify(userDomainService).ensureEmailIsUnique(emailCaptor.capture());

                Email capturedEmail = emailCaptor.getValue();
                assertEquals("dogac@gmail.com", capturedEmail.getValue());

                ArgumentCaptor<PhoneNumber> phoneCaptor = ArgumentCaptor.forClass(PhoneNumber.class);

                verify(userDomainService)
                                .ensurePhoneNumberIsUnique(phoneCaptor.capture());

                PhoneNumber capturedPhone = phoneCaptor.getValue();
                assertEquals("+905551234567", capturedPhone.getValue());

                ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

                verify(userRepository).save(userCaptor.capture());

                User savedUser = userCaptor.getValue();
                assertEquals(user, savedUser); // mapper’dan gelen user mı?

                verify(createUserMapper).toEntity(command, userId);
                verify(createUserMapper).toResponse(user);

                assertEquals(response, result);

        }

        @Test
        void shouldThrowExceptionWhenEmailIsNotUnique() {
                // given
                CreateUserCommand command = new CreateUserCommand(
                                "Dogac",
                                "Ulgac",
                                "dogac@gmail.com",
                                "+905551234567",
                                UserType.CUSTOMER,
                                List.of());

                UserId userId = UserId.generate();
                when(jwtUserIdProvider.currentUserId()).thenReturn(userId);
                when(createUserMapper.toEntity(command, userId)).thenReturn(mock(User.class));

                doThrow(new DuplicateUserEmailException("Email already exists"))
                                .when(userDomainService)
                                .ensureEmailIsUnique(any(Email.class));

                assertThrows(DuplicateUserEmailException.class,
                                () -> createUserCommandHandler.handle(command));
                verify(userRepository, never()).save(any());

        }

        @Test
        void shouldThrowExceptionWhenPhoneNumberIsNotUnique() {
                // given
                CreateUserCommand command = new CreateUserCommand(
                                "Dogac",
                                "Ulgac",
                                "dogac@gmail.com",
                                "+905551234567",
                                UserType.CUSTOMER,
                                List.of());
                UserId userId = UserId.generate();
                when(jwtUserIdProvider.currentUserId()).thenReturn(userId);
                when(createUserMapper.toEntity(command, userId)).thenReturn(mock(User.class));
                doThrow(new DuplicatePhoneNumberException("Phone number already exists"))
                                .when(userDomainService).ensurePhoneNumberIsUnique(any(PhoneNumber.class));

                assertThrows(DuplicatePhoneNumberException.class,
                                () -> createUserCommandHandler.handle(command));
                verify(userRepository, never()).save(any());
        }
}

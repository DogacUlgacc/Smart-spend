package com.dogac.user_service.domain.user;

import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.enums.UserStatus;
import com.dogac.user_service.domain.enums.UserType;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.FullName;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CreateUserTest {
    @Test
    void shouldCreateUserSuccessfully() {
        FullName fullName = new FullName("Dogac", "Dogac");
        Email email = Email.of("dogac@gmail.com");
        PhoneNumber phoneNumber = new PhoneNumber("+905555555555");
        UserType userType = UserType.CUSTOMER;

        User user = User.create(fullName, email, phoneNumber, userType);
        assertNotNull(user.getId());
        assertEquals(fullName,user.getFullName());
        assertEquals(email,user.getEmail());
        assertEquals(phoneNumber,user.getPhoneNumber());
        assertEquals(userType,user.getUserType());
        assertTrue(user.getAddresses().isEmpty());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void shouldThrowExceptionWhenFullNameIsNull() {
        Email email = Email.of("dogac@gmail.com");
        PhoneNumber phoneNumber = new PhoneNumber("+905555555555");
        UserType userType = UserType.CUSTOMER;
        assertThrows(NullPointerException.class,()->User.create(null,email,phoneNumber,userType));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        FullName fullName = new FullName("Dogac", "Dogac");
        PhoneNumber phoneNumber = new PhoneNumber("+905555555555");
        UserType userType = UserType.CUSTOMER;
        assertThrows(NullPointerException.class,() -> User.create(fullName,null,phoneNumber,userType));
    }

    @Test
    void shouldThrowExceptionWhenPhoneNumberIsNull() {
        FullName fullName = new FullName("Dogac", "Dogac");
        Email email = Email.of("dogac@gmail.com");
        UserType userType = UserType.CUSTOMER;
        assertThrows(NullPointerException.class,()-> User.create(fullName,email,null,userType));
    }
    @Test
    void shouldThrowExceptionWhenUserTypeIsNull() {
        FullName fullName = new FullName("Dogac", "Dogac");
        Email email = Email.of("dogac@gmail.com");
        PhoneNumber phoneNumber = new PhoneNumber("+905555555555");

        assertThrows(NullPointerException.class,()-> User.create(fullName,email,phoneNumber,null));
    }

    @Test
    void shouldInitializeWithActiveStatusAndEmptyAddresses(){

        FullName fullName = new FullName("Dogac", "Dogac");
        Email email = Email.of("dogac@gmail.com");
        PhoneNumber phoneNumber = new PhoneNumber("+905555555555");
        UserType userType = UserType.CUSTOMER;
        User user = User.create(fullName, email, phoneNumber, userType);
        assertEquals(UserStatus.ACTIVE,user.getStatus());
        assertTrue(user.getAddresses().isEmpty());
    }

    @Test
    void shouldCreatedAtAndUpdatedAtEquals(){
        FullName fullName = new FullName("Dogac", "Dogac");
        Email email = Email.of("dogac@gmail.com");
        PhoneNumber phoneNumber = new PhoneNumber("+905555555555");
        UserType userType = UserType.CUSTOMER;
        User user = User.create(fullName, email, phoneNumber, userType);
        assertEquals(user.getUpdatedAt(),user.getCreatedAt());
    }

    /*  TODO::/ This part should be in application layer because there is no DB! So we cannot check duplicate variables */
/*    @Test
    void shouldThrowsDuplicatePhoneNumberException(){
        FullName fullName = new FullName("Dogac", "Dogac");
        Email email = Email.of("dogac@gmail.com");
        PhoneNumber phoneNumber = new PhoneNumber("+905555555555");
        UserType userType = UserType.CUSTOMER;

        FullName fullName2 = new FullName("Dogac", "Dogac");
        Email email2 = Email.of("x@gmail.com");
        PhoneNumber phoneNumber2 = new PhoneNumber("+905555555555");
        UserType userType2 = UserType.CUSTOMER;

        User user = User.create(fullName, email, phoneNumber, userType);

        assertThrows(DuplicatePhoneNumberException.class, () -> User.create(fullName2, email2, phoneNumber2, userType2));
    }*/

}

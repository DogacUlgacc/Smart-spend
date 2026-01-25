package com.dogac.user_service.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dogac.user_service.application.bus.CommandBus;
import com.dogac.user_service.application.bus.QueryBus;
import com.dogac.user_service.application.commands.CreateUserCommand;
import com.dogac.user_service.application.commands.DeleteUserCommand;
import com.dogac.user_service.application.commands.UpdateUserCommand;
import com.dogac.user_service.application.dto.CreatedUserResponse;
import com.dogac.user_service.application.dto.UpdateUserRequest;
import com.dogac.user_service.application.dto.UpdatedUserResponse;
import com.dogac.user_service.application.dto.UserResponse;
import com.dogac.user_service.application.queries.GetAllUsersQuery;
import com.dogac.user_service.application.queries.GetUserByIdQuery;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    public UserController(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @PostMapping
    public ResponseEntity<CreatedUserResponse> createUser(@RequestBody @Valid CreateUserCommand command) {
        CreatedUserResponse response = commandBus.send(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatedUserResponse> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserRequest request) {

        UpdateUserCommand command = new UpdateUserCommand(
                id,
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.userType());
        UpdatedUserResponse response = commandBus.send(command);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable UUID id) {
        commandBus.send(new DeleteUserCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        GetUserByIdQuery query = new GetUserByIdQuery(id);
        UserResponse response = queryBus.execute(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getUsers() {
        GetAllUsersQuery query = new GetAllUsersQuery();
        List<UserResponse> responseList = queryBus.execute(query);
        return ResponseEntity.ok(responseList);
    }

}

package com.dogac.user_service.application.queryHandlers;

import org.springframework.stereotype.Component;

import com.dogac.user_service.application.core.QueryHandler;
import com.dogac.user_service.application.dto.UserResponse;
import com.dogac.user_service.application.mapper.UserResponseMapper;
import com.dogac.user_service.application.queries.GetUserByIdQuery;
import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.exceptions.UserNotFoundExcepiton;
import com.dogac.user_service.domain.repositories.UserRepository;
import com.dogac.user_service.domain.valueobjects.UserId;

@Component
public class GetUserByIdQueryHandler implements QueryHandler<GetUserByIdQuery, UserResponse> {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    public GetUserByIdQueryHandler(UserRepository userRepository, UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
    }

    @Override
    public UserResponse handle(GetUserByIdQuery query) {
        User user = userRepository.findById(UserId.from(query.id()))
                .orElseThrow(() -> new UserNotFoundExcepiton("User with given id is not found"));

        return userResponseMapper.toResponse(user);
    }

}

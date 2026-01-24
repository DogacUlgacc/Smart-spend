package com.dogac.user_service.application.queryHandlers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dogac.user_service.application.core.QueryHandler;
import com.dogac.user_service.application.dto.UserResponse;
import com.dogac.user_service.application.mapper.UserResponseMapper;
import com.dogac.user_service.application.queries.GetAllUsersQuery;
import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.repositories.UserRepository;

@Component
public class GetAllUsersQueryHandler implements QueryHandler<GetAllUsersQuery, List<UserResponse>> {

    private final UserResponseMapper userResponseMapper;
    private final UserRepository userRepository;

    public GetAllUsersQueryHandler(UserResponseMapper userResponseMapper, UserRepository userRepository) {
        this.userResponseMapper = userResponseMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponse> handle(GetAllUsersQuery query) {
        List<User> responseList = userRepository.findAll();
        return responseList.stream().map(userResponseMapper::toResponse).toList();
    }

}

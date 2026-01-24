package com.dogac.user_service.application.queries;

import java.util.List;

import com.dogac.user_service.application.core.Query;
import com.dogac.user_service.application.dto.UserResponse;

public record GetAllUsersQuery() implements Query<List<UserResponse>> {

}

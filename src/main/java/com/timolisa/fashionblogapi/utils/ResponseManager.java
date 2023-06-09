package com.timolisa.fashionblogapi.utils;

import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseManager<T> {
    public APIResponse<?> registrationSuccess(T data) {
        return new APIResponse<>("Registration successful", true, data);
    }
    public APIResponse<T> success(T data) {
        return new APIResponse<>("Request successful", true, data);
    }
    public APIResponse<String> success(String message) {
        return new APIResponse<>(message);
    }

    public APIResponse<Page<Post>> success(Page<Post> data) {
        return new APIResponse<>("Request successful", true, data);
    }
    public APIResponse<List<T>> success(List<T> data) {
        return new APIResponse<>("Request successful", true, data);
    }

    public APIResponse<T> error(String usernameAlreadyExists, boolean status) {
        return new APIResponse<>(usernameAlreadyExists, status);
    }

    public APIResponse<T> error(String message) {
        return new APIResponse<>("Registration unsuccessful due to Bad credentials");
    }

    public APIResponse<Page<T>> notFound(String message) {
        return new APIResponse<>(message);
    }
}

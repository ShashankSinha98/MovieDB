package com.shashank.moviedb.data.remote;

import java.io.IOError;
import java.io.IOException;

import retrofit2.Response;

// wrapper class for retrofit responses
public class ApiResponse<T> {


    // for error
    public ApiResponse<T> create(Throwable error) {
        String errorMsg = "Unknown error\nCheck network connection";
        if(error.getMessage()!=null && !error.getMessage().isEmpty()) errorMsg = error.getMessage();

        return new ApiErrorResponse(errorMsg);
    }

    // for response - can be success or failure
    public ApiResponse<T> create(Response<T> response) {

        if(response.isSuccessful()) {
            T body = response.body();

            if(body==null || response.code()==204) {
                return new ApiEmptyResponse();
            } else {
                return new ApiSuccessResponse(body);
            }
        } else {
            String errorMsg = "";

            try {
                errorMsg = response.errorBody().toString();
            } catch (Exception e) {
                e.printStackTrace();
                errorMsg = response.message();
            }

            return new ApiErrorResponse(errorMsg);
        }
    }


    // 3 diff types of API Responses

    public class ApiSuccessResponse<T> extends ApiResponse {
        private T body;

        public ApiSuccessResponse(T body) { this.body = body; }

        public T getBody() { return this.body; }
    }


    public class ApiErrorResponse<T> extends ApiResponse {
        private String errorMessage;

        public ApiErrorResponse(String errorMessage) { this.errorMessage = errorMessage; }

        public String getErrorMessage() { return this.errorMessage; }
    }


    public class ApiEmptyResponse<T> extends ApiResponse { }



}

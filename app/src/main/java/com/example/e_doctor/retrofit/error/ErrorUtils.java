package com.example.e_doctor.retrofit.error;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * Created by PC-JANINA on 8/4/2018.
 */

public class ErrorUtils {
    public static ServiceError parseError(Response<?> response) {
        Converter<ResponseBody, ServiceError> converter =
                RestClient.get_retrofit()
                        .responseBodyConverter(ServiceError.class, new Annotation[0]);

        ServiceError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ServiceError();
        }

        return error;
    }
}
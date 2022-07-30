package com.netflix.demo.fas;

import com.netflix.demo.exceptions.FlowsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FasExceptionMapper {

    @ExceptionHandler(FlowsNotFoundException.class)
    public ResponseEntity<FlowsNotFoundException> handleHourNotFoundException(final FlowsNotFoundException e) {
        HttpStatus status = HttpStatus.resolve(e.getErrorCode());

        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(e, status);
    }
}

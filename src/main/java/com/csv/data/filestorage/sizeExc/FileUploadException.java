package com.csv.data.filestorage.sizeExc;

import com.csv.data.filestorage.model.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FileUploadException extends ResponseEntityExceptionHandler {

    @SuppressWarnings("rawTypes")
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseMessage> handleSizeExceptions(MaxUploadSizeExceededException ex){

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ResponseMessage("FIle is too large",""));
    }

}

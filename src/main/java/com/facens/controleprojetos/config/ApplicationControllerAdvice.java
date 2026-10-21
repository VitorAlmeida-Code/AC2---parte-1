package com.facens.controleprojetos.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.facens.controleprojetos.dtos.ApiErrorDTO;
import com.facens.controleprojetos.exceptions.RegraNegocioException;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleRegraNegocioException(RegraNegocioException ex) {
        return new ApiErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleJsonInvalido(HttpMessageNotReadableException ex) {
        return new ApiErrorDTO("Dados da requisição inválidos. Verifique os campos e o formato das datas (aaaa-mm-dd).");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleTipoInvalido(MethodArgumentTypeMismatchException ex) {
        return new ApiErrorDTO("Valor inválido para o parâmetro '" + ex.getName() + "'.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleParametroAusente(MissingServletRequestParameterException ex) {
        return new ApiErrorDTO("O parâmetro '" + ex.getParameterName() + "' é obrigatório.");
    }
}

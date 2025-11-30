package br.com.ifba.prg04_rodrigo_back_end.infraestructure.exception;

public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String message) {
        super(message);
    }
}
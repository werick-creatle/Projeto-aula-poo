package hotelaria.projeto.hotelaria.model;

import org.springframework.http.HttpStatus;

public class Erro {
    private HttpStatus status;
    private String mensagem;
    private String exception;


    public Erro(HttpStatus status, String mensagem, String exception) {
        this.status = status;
        this.mensagem = mensagem;
        this.exception = exception;
    }


    public HttpStatus getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getException() {
        return exception;
    }
}
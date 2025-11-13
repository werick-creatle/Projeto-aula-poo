/*
* Classe para tratar os erros da api e exibir uma mensagem mais amigavel
*/

package hotelaria.projeto.hotelaria.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
@Builder
@Getter
public class Erro {
    private HttpStatus status;
    private String mensagem;
    private String exception;
}

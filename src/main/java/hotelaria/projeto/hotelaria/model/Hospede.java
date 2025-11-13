package hotelaria.projeto.hotelaria.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hospedes")
public class Hospede {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;

}

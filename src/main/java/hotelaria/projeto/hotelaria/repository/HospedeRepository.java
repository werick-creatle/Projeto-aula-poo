package hotelaria.projeto.hotelaria.repository;

import hotelaria.projeto.hotelaria.model.Hospede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospedeRepository extends JpaRepository<Hospede, Long> {

}
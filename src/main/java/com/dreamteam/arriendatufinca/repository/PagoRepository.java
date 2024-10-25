package com.dreamteam.arriendatufinca.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dreamteam.arriendatufinca.entities.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    
}

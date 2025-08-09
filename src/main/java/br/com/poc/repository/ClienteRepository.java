package br.com.poc.repository;

import br.com.poc.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByEmail(String email);
    
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    @Query("SELECT c FROM Cliente c WHERE c.nome LIKE %:termo% OR c.email LIKE %:termo%")
    List<Cliente> buscarPorTermo(@Param("termo") String termo);
    
    boolean existsByEmail(String email);
}
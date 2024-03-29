package br.com.poc.controller;

import br.com.poc.entity.Produto;
import br.com.poc.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping("/cadastrar")
    ResponseEntity<?> cadastrar (@RequestBody Produto produto) {
            produtoRepository.save(produto);
            return new ResponseEntity<>(produto, HttpStatus.CREATED);
    }

    @GetMapping
    List<Produto> listar() {
        return produtoRepository.findAll();
    }

    @PutMapping
    ResponseEntity<?> modificar (@RequestBody Produto produto) {
        return new ResponseEntity<>(produtoRepository.save(produto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity deletar(@PathVariable("id") Long id) {
        produtoRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}

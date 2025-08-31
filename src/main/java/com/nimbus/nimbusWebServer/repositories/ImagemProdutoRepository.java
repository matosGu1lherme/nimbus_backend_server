package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.produtos.ImagemProduto;
import com.nimbus.nimbusWebServer.models.produtos.id.ImagemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, ImagemId> {

    //findBy: O Spring Data JPA entende que você quer buscar dados.
    //Produto: Ele segue a relação (@ManyToOne) até a entidade Produto.
    //Id: Ele usa o ID do Produto para fazer a busca
    List<ImagemProduto> findByProdutoId(Long produtoId);
}

package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.dtos.ProdutoRequestDto;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.repositories.CategoriaRepository;
import com.nimbus.nimbusWebServer.repositories.ProdutoRepository;
import com.nimbus.nimbusWebServer.repositories.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final TagRepository tagRepository;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository,
            TagRepository tagRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.tagRepository = tagRepository;
    }

    public Produto salvarProduto(ProdutoRequestDto dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setSku(dto.sku());

        return produtoRepository.save(produto);
    }
}

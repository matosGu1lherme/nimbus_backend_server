package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.dtos.ItemCarrinhoRequestDto;
import com.nimbus.nimbusWebServer.dtos.ItemCarrinhoResponseDto;
import com.nimbus.nimbusWebServer.models.pedido.Carrinho;
import com.nimbus.nimbusWebServer.models.pedido.CarrinhoItem;
import com.nimbus.nimbusWebServer.models.produtos.Grade;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.models.produtos.id.GradeId;
import com.nimbus.nimbusWebServer.models.user.User;
import com.nimbus.nimbusWebServer.repositories.CarrinhoItemRepository;
import com.nimbus.nimbusWebServer.repositories.CarrinhoRepository;
import com.nimbus.nimbusWebServer.repositories.ProdutoRepository;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private CarrinhoItemRepository carrinhoItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public void adicionarItemCarrinho(ItemCarrinhoRequestDto itemCarrinhoRequestDto) {
        User userCarrinho = userRepository.findById(itemCarrinhoRequestDto.userId())
                .orElseThrow(() -> new RuntimeException("Não foi possivel criar o carrinho do usuario, email não encontrado!"));

        Carrinho carrinho = carrinhoRepository.findById(userCarrinho.getId())
                .orElseGet(() -> criarCarrinhoUsuario(userCarrinho));

        Produto produto = produtoRepository.findById(itemCarrinhoRequestDto.produtoId())
                .orElseThrow(() -> new RuntimeException("Não foi possivel adicionar item ao carrinho pois o produto não é valido ou não foi encontrado"));

        CarrinhoItem carrinhoItem;
        carrinhoItem = carrinhoItemRepository.findByCarrinhoUserIdAndProdutoId(userCarrinho.getId(), itemCarrinhoRequestDto.produtoId())
                .orElseGet(() -> {
                    CarrinhoItem novoItem= new CarrinhoItem();
                    novoItem.setCarrinho(carrinho);
                    novoItem.setProduto(produto);

                    Grade grade = new Grade();
                    GradeId gradeId = new GradeId(produto.getId(), itemCarrinhoRequestDto.tamanho());
                    grade.setId(gradeId);

                    novoItem.setGrade(grade);
                    novoItem.setValorMomentoCompra(itemCarrinhoRequestDto.valorMomentoCompra());

                    carrinho.getCarrinhoItems().add(novoItem);
                    return novoItem;
                });

        carrinhoItem.setQuantidade((itemCarrinhoRequestDto.quantidade()));
    }

    public List<ItemCarrinhoResponseDto> buscarItensCarrinhoPorId(UUID id) {
        Carrinho carrinho = carrinhoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não foi possivel resgastar itens do carrinho, carrinho não encontrado!"));

        List<ItemCarrinhoResponseDto> itemCarrinhoResponseDtoList = new ArrayList<>();
        for(CarrinhoItem itemCarrinho : carrinho.getCarrinhoItems()) {
            ItemCarrinhoResponseDto dto = ItemCarrinhoResponseDto.builder()
                    .produtoId(itemCarrinho.getProduto().getId())
                    .nome(itemCarrinho.getProduto().getNome())
                    .quantidade(itemCarrinho.getQuantidade())
                    .preco(itemCarrinho.getProduto().getPreco())
                    .url_imagem(itemCarrinho.getProduto().getImagens().getFirst().getUrl())
                    .build();

            itemCarrinhoResponseDtoList.add(dto);
        }

        return itemCarrinhoResponseDtoList;
    }

    public List<CarrinhoItem> buscarCarrinhoItensEntidadePorId(UUID userId) {
        return carrinhoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Não foram encontrados itens do carrinho"))
                .getCarrinhoItems();
    }

    public Carrinho criarCarrinhoUsuario(User user) {
        Carrinho carrinho = new Carrinho();
        carrinho.setUsuario(user);

        carrinhoRepository.save(carrinho);
        return carrinho;
    }

    @Transactional
    public void limparCarrinho(UUID carrinhoId) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Não foi possivel limpar o carrinho, carrinho não encontrado!"));

        carrinho.getCarrinhoItems().clear();
    }
}

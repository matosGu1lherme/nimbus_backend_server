package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.dtos.ItemCarrinhoRequstDto;
import com.nimbus.nimbusWebServer.models.pedido.Carrinho;
import com.nimbus.nimbusWebServer.models.pedido.CarrinhoItem;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.models.user.User;
import com.nimbus.nimbusWebServer.repositories.CarrinhoItemRepository;
import com.nimbus.nimbusWebServer.repositories.CarrinhoRepository;
import com.nimbus.nimbusWebServer.repositories.ProdutoRepository;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public void adicionarItemCarrinho(ItemCarrinhoRequstDto itemCarrinhoRequstDto) {
        User userCarrinho = userRepository.findByEmail(itemCarrinhoRequstDto.email())
                .orElseThrow(() -> new RuntimeException("Não foi possivel criar o carrinho do usuario, email não encontrado!"));

        Carrinho carrinho = carrinhoRepository.findById(userCarrinho.getId())
                .orElseGet(() -> criarCarrinhoUsuario(userCarrinho));

        Produto produto = produtoRepository.findById(itemCarrinhoRequstDto.produtoId())
                .orElseThrow(() -> new RuntimeException("Não foi possivel adicionar item ao carrinho pois o produto não é valido ou não foi encontrado"));

        CarrinhoItem carrinhoItem;
        carrinhoItem = carrinhoItemRepository.findByCarrinhoUserIdAndProdutoId(userCarrinho.getId(), itemCarrinhoRequstDto.produtoId())
                .orElseGet(() -> {
                    CarrinhoItem novoItem= new CarrinhoItem();
                    novoItem.setCarrinho(carrinho);
                    novoItem.setProduto(produto);
                    novoItem.setValorMomentoCompra(itemCarrinhoRequstDto.valorMomentoCompra());

                    carrinho.getCarrinhoItems().add(novoItem);
                    return novoItem;
                });

        int quantidadeAtual = Optional.ofNullable(carrinhoItem.getQuantidade()).orElse(0);
        carrinhoItem.setQuantidade((quantidadeAtual + itemCarrinhoRequstDto.quantidade()));
    }

    @Transactional
    public void removerItemCarrinho(ItemCarrinhoRequstDto itemCarrinhoRequstDto) {
        User user = userRepository.findByEmail(itemCarrinhoRequstDto.email())
                .orElseThrow(() -> new RuntimeException("Não foi possivel encontrar um carrinho vinculado ao email do usúario!"));

        CarrinhoItem carrinhoItem = carrinhoItemRepository.findByCarrinhoUserIdAndProdutoId(user.getId(), itemCarrinhoRequstDto.produtoId())
                .orElseThrow(() -> new RuntimeException("Não foi encontrado item no carrinho para realizar remoção"));

        carrinhoItem.setQuantidade(carrinhoItem.getQuantidade() - itemCarrinhoRequstDto.quantidade());
        if(carrinhoItem.getQuantidade() <= 0) {
            carrinhoItemRepository.delete(carrinhoItem);
        }
    }

    public Carrinho criarCarrinhoUsuario(User user) {
        Carrinho carrinho = new Carrinho();
        carrinho.setUsuario(user);

        carrinhoRepository.save(carrinho);
        return carrinho;
    }
}

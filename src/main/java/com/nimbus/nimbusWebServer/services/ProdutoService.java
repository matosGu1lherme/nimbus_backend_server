package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.dtos.ProdutoResponseDto;
import com.nimbus.nimbusWebServer.exception.customException.RecursoNaoEncontradoException;
import com.nimbus.nimbusWebServer.models.produtos.Categoria;
import com.nimbus.nimbusWebServer.models.produtos.ImagemProduto;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.models.produtos.Tipo;
import com.nimbus.nimbusWebServer.models.produtos.id.ImagemId;
import com.nimbus.nimbusWebServer.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final TipoRepository tipoRepository;
    private final CategoriaRepository categoriaRepository;
    private final TagRepository tagRepository;
    private final SkuSequenceService skuSequenceService;
    private final StorageService storageService;
    private final ImagemProdutoRepository imagemProdutoRepository;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            TipoRepository tipoRepository,
            CategoriaRepository categoriaRepository,
            TagRepository tagRepository,
            SkuSequenceService skuSequenceService,
            StorageService storageService,
            ImagemProdutoRepository imagemProdutoRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.tipoRepository = tipoRepository;
        this.categoriaRepository = categoriaRepository;
        this.tagRepository = tagRepository;
        this.skuSequenceService = skuSequenceService;
        this.storageService = storageService;
        this.imagemProdutoRepository = imagemProdutoRepository;
    }

    @Transactional
    public void salvarProduto(ProdutoResponseDto produto, MultipartFile[] imagensProduto) {
        Produto novoProduto =  new Produto();
        novoProduto.setNome(produto.nome());
        novoProduto.setPreco(produto.preco());
        novoProduto.setDescricao(produto.descricao());
        novoProduto.setTipo(tipoRepository.findById(produto.tipo_id())
                .orElseThrow(() -> new NoSuchElementException("Tipo com ID " + produto.tipo_id() + " não encontrado.")));
        novoProduto.setCategoria(categoriaRepository.findById(produto.categoria_id())
                .orElseThrow(() -> new NoSuchElementException("Categoria com ID " + produto.categoria_id() + " não encontrado.")));
        novoProduto.setSku(gerarProdutoSKU(novoProduto));

        try {
            novoProduto = produtoRepository.save(novoProduto);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        List<ImagemProduto> imagensProdutoList= new ArrayList<>();

        Integer sequence = 1;
        for (MultipartFile imagem : imagensProduto) {
            try {
                String filename = storageService.store(imagem);

                ImagemId imagemId = new ImagemId(novoProduto.getId(), sequence);
                sequence++;
                ImagemProduto novoImgPrdVinculo = new ImagemProduto();
                novoImgPrdVinculo.setId(imagemId);
                novoImgPrdVinculo.setProduto(novoProduto);
                novoImgPrdVinculo.setUrl(filename);
                imagensProdutoList.add(novoImgPrdVinculo);
            } catch (IOException e) {
                throw new RuntimeException("Não foi possivel salvar uma imagem no processo de salvar o produto. Nome do arquivo: " + imagem.getName(), e);
            }
        }
        try {
            imagemProdutoRepository.saveAll(imagensProdutoList);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<ProdutoResponseDto> buscarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        List<ProdutoResponseDto> produtosResponseDto = new ArrayList();
        for (Produto produto : produtos) {
            ProdutoResponseDto novoProdutoResponse = new ProdutoResponseDto(
                    produto.getId(),
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getTipo().getId(),
                    produto.getCategoria().getId(),
                    produto.getSku()
            );
            produtosResponseDto.add(novoProdutoResponse);
        }
        return produtosResponseDto;
    }

    public List<ProdutoResponseDto> buscarProdutosPorNomeDeCategoria(String categoriaNome) {
        List<Produto> produtos = produtoRepository.findByCategoriaNome(categoriaNome);
        List<ProdutoResponseDto> produtoResponseDtos = new ArrayList<>();
        for (Produto produto : produtos) {
            ProdutoResponseDto novoProdutoResponseDto = new ProdutoResponseDto(
                    produto.getId(),
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getTipo().getId(),
                    produto.getCategoria().getId(),
                    produto.getSku()
            );
            produtoResponseDtos.add(novoProdutoResponseDto);
        }
        return produtoResponseDtos;
    }

    public String gerarProdutoSKU(Produto produto) {
        if (produto.getTipo() == null || produto.getTipo().getId() == null) {
            throw new IllegalArgumentException("O tipo do produto não pode ser nulo.");
        }

        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("A categoria do produto não pode ser nula.");
        }

        String abreviacaoTipo = tipoRepository.findById(produto.getTipo().getId())
                .map(Tipo::getAbreviacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo não encontrado para o produto recebido - (ID tipo procurado:)" + produto.getTipo().getId()));

        String abreviacaoCategoria = categoriaRepository.findById(produto.getCategoria().getId())
                .map(Categoria::getAbreviacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrado para o produto recebido - (ID categoria procurada:)" + produto.getCategoria().getId()));

        String chaveSku = abreviacaoTipo + abreviacaoCategoria;
        return skuSequenceService.gerarSkuSequence(chaveSku);
    }

    public String retornarImgApresentacaoProd(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));

        ImagemProduto imagensProduto = produto.getImagens()
                .stream()
                .filter(imgProduto -> (imgProduto.getId().getSequencia() == 1))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Não foi possivel encontrar a imagem de apresentação do produto."));

        return imagensProduto.getUrl();
    }
}

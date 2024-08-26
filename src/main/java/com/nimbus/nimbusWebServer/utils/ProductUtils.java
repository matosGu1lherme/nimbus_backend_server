package com.nimbus.nimbusWebServer.utils;

import com.nimbus.nimbusWebServer.dtos.ProductDto;
import com.nimbus.nimbusWebServer.dtos.ProductImagesDto;
import com.nimbus.nimbusWebServer.models.ProductImagesModel;
import com.nimbus.nimbusWebServer.models.ProdutoModel;

import java.util.ArrayList;
import java.util.List;

public class ProductUtils {

    public static ProdutoModel makeProduct(ProductDto productDto) {
        ProdutoModel produto = ProdutoModel.builder()
                .nome(productDto.nome())
                .descricao(productDto.descricao())
                .valor(productDto.valor())
                .build();

        return produto;
    }

    public static List<ProductImagesModel> makeImages(ProdutoModel produtoModel, List<ProductImagesDto> imagesDtos) {
        List<ProductImagesModel> images = new ArrayList<>();
        for (ProductImagesDto productImageModelDto : imagesDtos) {
            images.add(ProductImagesModel.builder()
                    .ordem_apresentacao(productImageModelDto.ordem_apresentacao())
                    .path_imagem(productImageModelDto.path_imagem())
                    .produto(produtoModel)
                    .build());
        }

        return images;
    }
}

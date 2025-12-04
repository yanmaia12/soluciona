package com.soluciona.soluciona.service;

import com.soluciona.soluciona.dto.ServicePostRequestDTO;
import com.soluciona.soluciona.model.Categories;
import com.soluciona.soluciona.model.Profiles;
import com.soluciona.soluciona.model.ServicePost;
import com.soluciona.soluciona.enums.ServicePostStatus;
import com.soluciona.soluciona.repository.CategoriesRepository;
import com.soluciona.soluciona.repository.ProfilesRepository;
import com.soluciona.soluciona.repository.ServicePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServicePostService {

    @Autowired
    private ServicePostRepository servicePostRepository;

    @Autowired
    private ProfilesRepository profileRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    public ServicePost createService(ServicePostRequestDTO dto, UUID autorId) {

        Profiles autor = profileRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Utilizador (autor) não encontrado."));

        Categories categoria = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        ServicePost novoAnuncio = new ServicePost();
        novoAnuncio.setProfiles(autor);
        novoAnuncio.setCategories(categoria);

        novoAnuncio.setTitle(dto.getTitle());
        novoAnuncio.setDescription(dto.getDescription());
        novoAnuncio.setPrice(dto.getPrice());
        novoAnuncio.setLocation(dto.getLocation());
        novoAnuncio.setPhotos(dto.getPhotos());
        novoAnuncio.setLatitude(dto.getLatitude());
        novoAnuncio.setLongitude(dto.getLongitude());
        novoAnuncio.setFulLAddress(dto.getFullAddress());

        return servicePostRepository.save(novoAnuncio);
    }

    public List<ServicePost> listActiveServices() {
        return servicePostRepository.findByStatus(ServicePostStatus.ativo);
    }

    public ServicePost searchServiceById(Long id) {
        return servicePostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado."));
    }

    public ServicePost updateService(Long servicoId, ServicePostRequestDTO dto, UUID autorIdDoToken) {

        ServicePost anuncioOriginal = servicePostRepository.findById(servicoId)
                .orElseThrow(() -> new RuntimeException("Anúncio (serviço) não encontrado."));

        UUID donoDoAnuncioId = anuncioOriginal.getProfiles().getId();

        if (!donoDoAnuncioId.equals(autorIdDoToken)) {
            throw new RuntimeException("Acesso negado: Você não é o dono deste anúncio.");
        }

        Categories novaCategoria = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Nova categoria não encontrada."));

        anuncioOriginal.setTitle(dto.getTitle());
        anuncioOriginal.setDescription(dto.getDescription());
        anuncioOriginal.setPrice(dto.getPrice());
        anuncioOriginal.setLocation(dto.getLocation());
        anuncioOriginal.setPhotos(dto.getPhotos());
        anuncioOriginal.setCategories(novaCategoria);

        anuncioOriginal.setLatitude(dto.getLatitude());
        anuncioOriginal.setLongitude(dto.getLongitude());
        anuncioOriginal.setFulLAddress(dto.getFullAddress());

        return servicePostRepository.save(anuncioOriginal);
    }

    public void deleteService(Long servicoId, UUID autorIdDoToken) {

        ServicePost anuncioParaApagar = servicePostRepository.findById(servicoId)
                .orElseThrow(() -> new RuntimeException("Anúncio (serviço) não encontrado."));

        UUID donoDoAnuncioId = anuncioParaApagar.getProfiles().getId();

        if (!donoDoAnuncioId.equals(autorIdDoToken)) {
            throw new RuntimeException("Acesso negado: Você não é o dono deste anúncio.");
        }


        servicePostRepository.delete(anuncioParaApagar);
    }
}
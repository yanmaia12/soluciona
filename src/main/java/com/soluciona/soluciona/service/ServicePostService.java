package com.soluciona.soluciona.service;

import com.soluciona.soluciona.dto.GeoProviderDTO;
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

import java.util.ArrayList;
import java.util.Comparator;
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

    public List<GeoProviderDTO> findNearbyServices(Double latUser, Double lngUser, Double radiusKm) {
        List<ServicePost> allServices = servicePostRepository.findAll();
        List<GeoProviderDTO> nearby = new ArrayList<>();

        for (ServicePost service : allServices) {

            if (service.getLatitude() != null && service.getLongitude() != null) {

                double dist = calculateDistance(latUser, lngUser, service.getLatitude(), service.getLongitude());

                if (dist <= radiusKm) {
                    String photo = (service.getProfiles() != null) ? service.getProfiles().getProfilePicture() : null;
                    String providerName = (service.getProfiles() != null) ? service.getProfiles().getName() : "Profissional";

                    String link = "https://soluciona-frontend.onrender.com/servico/" + service.getId();

                    nearby.add(new GeoProviderDTO(
                            service.getId(),
                            providerName,
                            service.getTitle(),
                            String.format("%.1f km", dist), // Formata: "1.5 km"
                            5.0,
                            photo,
                            link
                    ));
                }
            }
        }

        nearby.sort(Comparator.comparingDouble(dto ->
                Double.parseDouble(dto.getDistance().replace(" km", "").replace(",", "."))
        ));

        return nearby;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
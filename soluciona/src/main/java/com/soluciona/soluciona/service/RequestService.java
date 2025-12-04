package com.soluciona.soluciona.service;

import com.soluciona.soluciona.dto.RequestCreateDTO;
import com.soluciona.soluciona.dto.RequestStatusUpdateDTO;
import com.soluciona.soluciona.enums.RequestStatus;
import com.soluciona.soluciona.model.Requests;
import com.soluciona.soluciona.model.ServicePost;
import com.soluciona.soluciona.repository.ProfilesRepository;
import com.soluciona.soluciona.repository.RequestsRepository;
import com.soluciona.soluciona.repository.ServicePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.soluciona.soluciona.model.Profiles;
import org.springframework.stereotype.Service;

import java.util.HashMap; // Para o Map
import java.util.List;
import java.util.Map; // Para o Map
import java.util.UUID;

@Service
public class RequestService {

    @Autowired
    private RequestsRepository requestsRepository;

    @Autowired
    private ProfilesRepository profilesRepository;

    @Autowired
    private ServicePostRepository servicePostRepository;

    public Requests createOrder(RequestCreateDTO dto, UUID clienteId) {

        Profiles cliente = profilesRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente (perfil) não encontrado."));

        ServicePost servico = servicePostRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Anúncio (serviço) não encontrado."));

        if (servico.getProfiles().getId().equals(clienteId)) {
            throw new RuntimeException("Você não pode solicitar o seu próprio serviço.");
        }

        Requests novoPedido = new Requests();
        novoPedido.setRequester(cliente);
        novoPedido.setServicePost(servico);

        return requestsRepository.save(novoPedido);
    }

    public Map<String, List<Requests>> seeMyOrders(UUID userId) {

        Profiles utilizadorLogado = profilesRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado."));

        List<Requests> pedidosQueFiz = requestsRepository.findByRequester(utilizadorLogado);

        List<Requests> pedidosQueRecebi = requestsRepository.findByServicePost_Profiles(utilizadorLogado);

        Map<String, List<Requests>> meusPedidos = new HashMap<>();
        meusPedidos.put("pedidosFeitos", pedidosQueFiz);
        meusPedidos.put("pedidosRecebidos", pedidosQueRecebi);

        return meusPedidos;
    }

    public Requests updateStatusOrder(Long pedidoId, RequestStatusUpdateDTO dto, UUID userIdDoToken) {

        Requests pedidoOriginal = requestsRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido (Request) não encontrado."));

        Profiles donoDoAnuncio = pedidoOriginal.getServicePost().getProfiles();

        if (!donoDoAnuncio.getId().equals(userIdDoToken)) {
            throw new RuntimeException("Acesso negado: Você não é o dono do anúncio deste pedido.");
        }

        RequestStatus novoStatus = dto.getStatus();
        pedidoOriginal.setStatus(novoStatus);

        return requestsRepository.save(pedidoOriginal);
    }
}
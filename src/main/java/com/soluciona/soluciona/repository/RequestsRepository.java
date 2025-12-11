package com.soluciona.soluciona.repository;

import com.soluciona.soluciona.model.Profiles;
import com.soluciona.soluciona.model.Requests;
import com.soluciona.soluciona.model.ServicePost;
import org.apache.coyote.Request;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestsRepository extends JpaRepository<Requests, Long> {

    List<Requests> findByRequester(Profiles requester);

    List<Requests> findByServicePost_Profiles(Profiles provider);

    List<Requests> findByServicePost(ServicePost servicePost);

}

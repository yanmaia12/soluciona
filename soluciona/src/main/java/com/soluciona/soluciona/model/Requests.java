package com.soluciona.soluciona.model;

import com.soluciona.soluciona.enums.RequestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor

public class Requests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServicePost servicePost;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private Profiles requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @CreationTimestamp
    @Column(name = "request_date", nullable = false, updatable = false)
    private LocalDateTime requestDate;

    @PrePersist
    protected void onCreate(){
        if (this.status == null){
            this.status = RequestStatus.pendente;
        }
    }
}

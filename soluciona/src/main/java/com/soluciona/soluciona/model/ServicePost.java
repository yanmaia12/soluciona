package com.soluciona.soluciona.model;

import com.soluciona.soluciona.enums.ServicePostStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "service_post")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ServicePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profiles profiles;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories categories;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private String price;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "photos", columnDefinition = "jsonb")
    private List<String> photos;

    @Column(name = "location")
    private String location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "full_address")
    private String fulLAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ServicePostStatus status;

    @PrePersist
    protected void onCreate(){
        if (this.status == null){
            this.status = ServicePostStatus.ativo;
        }
    }

}

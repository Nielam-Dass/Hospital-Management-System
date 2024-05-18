package com.dass.niel.hospital.management.system.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "visit"
)
public class Visit {
    @Id
    @SequenceGenerator(name = "visit_sequence", sequenceName = "visit_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visit_sequence")
    private Long visitId;

    @Column(name = "admitted_on", nullable = false)
    private LocalDate admittedOn;

    @Column(name = "discharged_on")
    private LocalDate dischargedOn;

    @Column(name = "reason", nullable = false)
    private String reason;

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "patient_id",
            referencedColumnName = "patientId"
    )
    private Patient patient;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "staff_visit",
            joinColumns = @JoinColumn(
                    name = "visit_id",
                    referencedColumnName = "visitId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "staff_id",
                    referencedColumnName = "staffId"
            )
    )
    private List<Staff> staffInvolved;

}

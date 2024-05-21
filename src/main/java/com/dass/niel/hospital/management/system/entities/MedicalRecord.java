package com.dass.niel.hospital.management.system.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "medical_record"
)
public class MedicalRecord {

    @Id
    @SequenceGenerator(name = "record_sequence", sequenceName = "record_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_sequence")
    private Long recordId;

    @Column(name = "created_on", nullable = false)
    private LocalDate createdOn;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "added_by",
            referencedColumnName = "staffId"
    )
    private Staff addedBy;

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "patient_id",
            referencedColumnName = "patientId"
    )
    private Patient patient;

    @Column(name = "record_file")
    private String recordFile;
}

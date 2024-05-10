package com.dass.niel.hospital.management.system.staff;

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
        name = "staff"
)
public class Staff {
    @Id
    @SequenceGenerator(name = "staff_sequence", sequenceName = "staff_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_sequence")
    private Long staffId;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "department")
    private String department;

    @Column(name = "hired_on", nullable = false)
    private LocalDate hiredOn;

    @Column(name = "salary", nullable = false)
    private Integer salary;
}

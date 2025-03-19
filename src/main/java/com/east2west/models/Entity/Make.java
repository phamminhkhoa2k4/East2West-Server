package com.east2west.models.Entity;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Makes")
public class Make {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "makeid")
    private int makeid;

    @Column(name = "logo")
    private String logo;

    @Column(name = "makename")
    private String makename;


}
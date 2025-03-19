    package com.east2west.models.Entity;

    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Table(name = "locationtype")
    public class LocationType {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "locationtypeid")
        private int locationtypeid;

        @Column(name = "locationtypename")
        private String locationtypename;

        @Column(name = "locationdtypedescription",columnDefinition = "text")
        private String locationtypedescription;

    }
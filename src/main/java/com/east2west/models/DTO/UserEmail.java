package com.east2west.models.DTO;

import com.east2west.models.Entity.Role;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserEmail {

    private String firstName;

    private String lastName;

    private String location;




    private List<TargetEmail> emails;

}

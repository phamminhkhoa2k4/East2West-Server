package com.east2west.models.Entity;
import com.east2west.models.enums.EComparisonMethod;
import com.east2west.models.enums.EIdentityType;
import com.east2west.models.enums.EStatusVerify;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private int userId;

    @Column(name = "username",unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "avatarurl")
    private String avatarUrl;

    @Column(name = "googleid")
    private String googleId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "userid"),inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "nation")
    private String nation;

    @Enumerated(EnumType.STRING)
    @Column(name = "identity_type")
    private EIdentityType identityType;

    @Column(name = "identity_forward")
    private  String identityForward;


    @Column(name = "identity_backward")
    private  String identityBackward;

    @Column(name = "comparison_method")
    private EComparisonMethod comparisonMethod;


    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private EStatusVerify status = EStatusVerify.NOT_VERIFYED;

    @Column(name = "selfie")
    private String selfie;
}
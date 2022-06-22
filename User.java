package vn.selex.user_api_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vn.selex.user_api_service.models.OrgRole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Setter @Getter @ToString
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String full_name;

    private String date_of_birth;

    private Integer gender; // 0 : male, 1 : female, 2 : other ...

    private String address;

    @Column(unique = true)
    private String identity_number;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(unique = true)
    private String phone;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String authorities = "CUSTOMER";

    @NotNull
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    private Org org;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    private Integer orgRole = OrgRole.NORMAL;

    @CreationTimestamp
    private Timestamp created_at;

    @UpdateTimestamp
    private Timestamp updated_at;

    public User(){}
}

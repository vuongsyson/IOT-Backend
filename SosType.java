package vn.selex.Support_Service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "sos_type")
@Getter @Setter @NoArgsConstructor
public class SosType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_type_id")
    private DeviceType deviceType;

    @NotNull
    @Column(name = "title", length = 512)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "sosType", fetch = FetchType.LAZY)
    private List<SosRequest> sosRequestList;
}

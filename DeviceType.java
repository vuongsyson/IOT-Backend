package vn.selex.Support_Service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "device_type")
@Getter @Setter @NoArgsConstructor
public class DeviceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "deviceType", cascade = CascadeType.ALL)
    private List<SosType> sosTypeList;

    public DeviceType(String typeName) {
        this.name = typeName;
    }
}

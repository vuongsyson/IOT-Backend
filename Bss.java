package vn.selex.bssApiService.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bss")
@Getter @Setter
public class Bss {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String address;

    @NotNull
    @Column(unique = true)
    private String serial_number;

    @NotNull
    private Integer hw_version;

    @NotNull
    private Integer sw_version;

    @NotNull
    private String manufacture_date;

    private Double lon;

    private Double lat;

    private Integer type_code = 0; // 0: default , 1 : , 2 : ,...

    @NotNull
    private Integer cab_num;

    private Integer cab_empty_num;

    private Integer bp_ready_num = 0;
    @NotNull

    private long swap_bp_no = 0;
    @JsonIgnore

    @OneToMany(mappedBy = "bss")
    private List<Cabinet> cabinetList;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    public Bss() {}
}

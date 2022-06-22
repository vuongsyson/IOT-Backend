package vn.selex.bp_api_service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "battery")
@Getter
 @Setter
@NoArgsConstructor
public class Battery {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "serial_no", unique = true)
    private String serialNo;

    @NotNull
    private Integer hw_version;

    @NotNull
    private Integer sw_version;

    @NotNull
    private String manufacture_date;

    @NotNull
    private Integer capacity;

    @NotNull
    private Integer max_charge;

    @NotNull
    private Integer max_discharge;

    @NotNull
    private Integer max_vol;

    @NotNull
    private Integer min_vol;

    @NotNull
    private Boolean used = false;

    private Integer soc;
    private Integer soh;
    private Integer temp;
    private Long owner_id;
    private Long renter_id;
    private Long cycle_count;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private RentalHistory rental;

    @CreationTimestamp
    private Date createAt;

    @UpdateTimestamp
    private Date updateAt;

}

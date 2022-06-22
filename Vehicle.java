package vn.selex.vehicle_api.entitys;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Vehicle", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"serial_number"})
})
@Getter @Setter @NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer clearance;

    private Integer max_power;

    private Integer max_speed;

    private Integer max_load;

    private Integer weight_total;

    private Integer max_distance;

    private Integer wheel_base;

    private Integer hw_version;

    private Integer sw_version;

    private String serial_number;

    private String manufacture_date;

    private Integer lot_number;

    private String color;

    private String vehicle_type;

    private Boolean used = false;

    @Column(name = "user_id")
    private Long userId;

    @CreationTimestamp
    private Date createAt;

    @UpdateTimestamp
    private Date updateAt;

    @Transient
    private VehicleState state;

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", clearance=" + clearance +
                ", max_power=" + max_power +
                ", max_speed=" + max_speed +
                ", max_load=" + max_load +
                ", weight_total=" + weight_total +
                ", max_distance=" + max_distance +
                ", wheel_base=" + wheel_base +
                ", hw_version=" + hw_version +
                ", sw_version=" + sw_version +
                ", serial_number='" + serial_number + '\'' +
                ", manufacture_date='" + manufacture_date + '\'' +
                ", lot_number=" + lot_number +
                ", color='" + color + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}

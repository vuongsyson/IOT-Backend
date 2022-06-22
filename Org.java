package vn.selex.vehicle_api.entitys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "org")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Org {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long org_id;

    @OneToMany
    private List<Vehicle> vehicles = new ArrayList<>();

    public void addVehicle(Vehicle vehicle){
        this.vehicles.add(vehicle);
    }
}

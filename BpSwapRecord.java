package vn.selex.swapping_service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "swap_bp_record")
@Getter @Setter @NoArgsConstructor
@ToString
public class BpSwapRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "old_bat")
    private String old_bat;

    @Column(name = "new_bat")
    private String new_bat;

    @Column(name = "old_cab")
    private String old_cab;

    @Column(name = "new_cab")
    private String new_cab;

    @Column(name = "bss")
    private String bss;

    @Column(name="user")
    private Long user;

    @Column(name = "state")
    private Integer state;

    @Column(name = "error")
    private Integer error;

    @CreationTimestamp
    private Date createdAt;
}


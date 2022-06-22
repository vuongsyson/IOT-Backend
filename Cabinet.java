package vn.selex.bssApiService.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "cabinet")
@Getter @Setter
public class Cabinet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long bssID;

    private Long bp_id;
    private Boolean bp_ready;

    private Long swap_no = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bss_id")
    @JsonIgnore
    private Bss bss;

/*   0 : action: bp get out -> cab not battery, default
     1 : action: add bp -> cab has battery
     2 : error        */
    private Integer state_code = 0;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    public Cabinet() {}

    public Cabinet(Long bssID, Bss bss) {
        this.bssID = bssID;
        this.bss = bss;
    }

    @Override
    public String toString() {
        return "Cabinet{" +
                "id=" + id +
                ", bp_sn='" + bp_id + '\'' +
                ", station=" + bss +
                ", state=" + state_code +
                ", createAt=" + createdAt +
                ", updateAt=" + updatedAt +
                '}';
    }
}

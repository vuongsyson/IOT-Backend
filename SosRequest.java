package vn.selex.Support_Service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "sos_request")
@Getter @Setter @NoArgsConstructor
public class SosRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(name = "phone_number")
    @NotNull
    private String phone;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "sos_type_id")
    private SosType sosType;

    @NotNull
    @Column(name = "device_serial_number")
    private String deviceSerialNumber;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    /*
     * state = 0: send request
     * state = 1: request received
     * state = 2: assignment
     * state = 3: handle start
     * state = 4: handle done
     * state = 5: users rated
    */
    @Column(name = "state")
    private Integer state = 0;

    @Column(name = "rating")
    private int rating; //0 -> 5

    @Column(name = "done")
    private Boolean done = false;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "done_time")
    private Date doneTime;
}

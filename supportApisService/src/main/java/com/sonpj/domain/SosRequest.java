package com.sonpj.domain;

import com.sonpj.domain.enumeration.SosState;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SosRequest.
 */
@Entity
@Table(name = "sos_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SosRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "device_serial_number", nullable = false)
    private String deviceSerialNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private SosState state;

    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "rating")
    private Integer rating;

    @Column(name = "done")
    private Boolean done;

    @Column(name = "done_time")
    private Instant doneTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SosRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public SosRequest userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return this.phone;
    }

    public SosRequest phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceSerialNumber() {
        return this.deviceSerialNumber;
    }

    public SosRequest deviceSerialNumber(String deviceSerialNumber) {
        this.setDeviceSerialNumber(deviceSerialNumber);
        return this;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public SosRequest description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return this.image;
    }

    public SosRequest image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public SosState getState() {
        return this.state;
    }

    public SosRequest state(SosState state) {
        this.setState(state);
        return this;
    }

    public void setState(SosState state) {
        this.state = state;
    }

    public Integer getRating() {
        return this.rating;
    }

    public SosRequest rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getDone() {
        return this.done;
    }

    public SosRequest done(Boolean done) {
        this.setDone(done);
        return this;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Instant getDoneTime() {
        return this.doneTime;
    }

    public SosRequest doneTime(Instant doneTime) {
        this.setDoneTime(doneTime);
        return this;
    }

    public void setDoneTime(Instant doneTime) {
        this.doneTime = doneTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SosRequest)) {
            return false;
        }
        return id != null && id.equals(((SosRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SosRequest{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", phone='" + getPhone() + "'" +
            ", deviceSerialNumber='" + getDeviceSerialNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", state='" + getState() + "'" +
            ", rating=" + getRating() +
            ", done='" + getDone() + "'" +
            ", doneTime='" + getDoneTime() + "'" +
            "}";
    }
}

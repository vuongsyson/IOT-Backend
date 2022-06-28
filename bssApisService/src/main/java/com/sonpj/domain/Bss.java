package com.sonpj.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bss.
 */
@Entity
@Table(name = "bss")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bss implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "hw_version")
    private Integer hwVersion;

    @Column(name = "sw_version")
    private Integer swVersion;

    @Column(name = "manufacture_date")
    private String manufactureDate;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "type_code")
    private Integer typeCode;

    @Column(name = "cab_num")
    private Integer cabNum;

    @Column(name = "cab_empty_num")
    private Integer cabEmptyNum;

    @Column(name = "bp_ready_num")
    private Integer bpReadyNum;

    @Column(name = "swap_bp_no")
    private Long swapBpNo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bss id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Bss name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Bss address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Bss serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getHwVersion() {
        return this.hwVersion;
    }

    public Bss hwVersion(Integer hwVersion) {
        this.setHwVersion(hwVersion);
        return this;
    }

    public void setHwVersion(Integer hwVersion) {
        this.hwVersion = hwVersion;
    }

    public Integer getSwVersion() {
        return this.swVersion;
    }

    public Bss swVersion(Integer swVersion) {
        this.setSwVersion(swVersion);
        return this;
    }

    public void setSwVersion(Integer swVersion) {
        this.swVersion = swVersion;
    }

    public String getManufactureDate() {
        return this.manufactureDate;
    }

    public Bss manufactureDate(String manufactureDate) {
        this.setManufactureDate(manufactureDate);
        return this;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Double getLon() {
        return this.lon;
    }

    public Bss lon(Double lon) {
        this.setLon(lon);
        return this;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return this.lat;
    }

    public Bss lat(Double lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getTypeCode() {
        return this.typeCode;
    }

    public Bss typeCode(Integer typeCode) {
        this.setTypeCode(typeCode);
        return this;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getCabNum() {
        return this.cabNum;
    }

    public Bss cabNum(Integer cabNum) {
        this.setCabNum(cabNum);
        return this;
    }

    public void setCabNum(Integer cabNum) {
        this.cabNum = cabNum;
    }

    public Integer getCabEmptyNum() {
        return this.cabEmptyNum;
    }

    public Bss cabEmptyNum(Integer cabEmptyNum) {
        this.setCabEmptyNum(cabEmptyNum);
        return this;
    }

    public void setCabEmptyNum(Integer cabEmptyNum) {
        this.cabEmptyNum = cabEmptyNum;
    }

    public Integer getBpReadyNum() {
        return this.bpReadyNum;
    }

    public Bss bpReadyNum(Integer bpReadyNum) {
        this.setBpReadyNum(bpReadyNum);
        return this;
    }

    public void setBpReadyNum(Integer bpReadyNum) {
        this.bpReadyNum = bpReadyNum;
    }

    public Long getSwapBpNo() {
        return this.swapBpNo;
    }

    public Bss swapBpNo(Long swapBpNo) {
        this.setSwapBpNo(swapBpNo);
        return this;
    }

    public void setSwapBpNo(Long swapBpNo) {
        this.swapBpNo = swapBpNo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bss)) {
            return false;
        }
        return id != null && id.equals(((Bss) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bss{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", hwVersion=" + getHwVersion() +
            ", swVersion=" + getSwVersion() +
            ", manufactureDate='" + getManufactureDate() + "'" +
            ", lon=" + getLon() +
            ", lat=" + getLat() +
            ", typeCode=" + getTypeCode() +
            ", cabNum=" + getCabNum() +
            ", cabEmptyNum=" + getCabEmptyNum() +
            ", bpReadyNum=" + getBpReadyNum() +
            ", swapBpNo=" + getSwapBpNo() +
            "}";
    }
}

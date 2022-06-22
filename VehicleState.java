package vn.selex.vehicle_state_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "mqtt_consumer")
public class VehicleState {

    @Column(name = "speed")
    private Integer speed;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "error")
    private Integer error;

    @Column(name = "serial_number")
    private String serial_number;

    @Column(name="status")
	private String status;

    @Column(name="odo")
	private Double odo;

    @Column(name="power")
	private Double power;

    @Column(name="throttle")
	private Double throttle;
    
	@Column(name="time")
	private Instant time;

    public VehicleState(){}

    @JsonProperty("speed")
    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    @JsonProperty("lat")
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @JsonProperty("lon")
    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @JsonProperty("error")
    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    @JsonProperty("serial_number")
    public String getSerialNumber() {
        return serial_number;
    }

    public void setSerialNumber(String serial_no) {
        this.serial_number = serial_no;
    }
    
	@JsonProperty("time")
	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}
	
	@JsonProperty("status")
    public String getStatus() {
		return status;
    }

	public void setStatus(String status) {
		
		this.status=status;
		
	}

	@JsonProperty("odo")
	public Double getOdo() {
		return odo;
	}

	public void setOdo(Double odo) {
		this.odo=odo;
	}

	@JsonProperty("power")
	public Double getPower() {
		return power;
	}

	public void setPower(Double power) {
		this.power = power;
	}

	@JsonProperty("throttle")
	public Double getThrottle() {
		return throttle;
	}

	public void setThrottle(Double throttle) {
		this.throttle = throttle;
	}
	
	
	
}

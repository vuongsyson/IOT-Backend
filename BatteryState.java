package vn.selex.device_state.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "mqtt_consumer")
@Getter @Setter @NoArgsConstructor
public class BatteryState {

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "vol")
    private Integer vol;

    @Column(name = "cur")
    private Integer cur;

    @Column(name = "soc")
    private Integer soc;

    @Column(name = "soh")
    private Integer soh;

    @Column(name = "state")
    private Integer state;

    @Column(name = "state")
    private Integer status;

}

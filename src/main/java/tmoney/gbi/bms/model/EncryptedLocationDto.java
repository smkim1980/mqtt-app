package tmoney.gbi.bms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import tmoney.gbi.bms.common.annotation.Encrypt;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Alias("encryptedLocationDto")
public class EncryptedLocationDto {

    private String deviceId;
    private String vehicleId;

    @Encrypt
    private String encryptedLatitude;
    @Encrypt
    private String encryptedLongitude;

    private Long occurDt;
}
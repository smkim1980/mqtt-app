package tmoney.gbi.bms.mapper;

import org.apache.ibatis.annotations.Mapper;
import tmoney.gbi.bms.model.EncryptedLocationDto;

import java.util.List;

@Mapper
public interface CommonInsertMapper {

    /**
     * 암호화된 위치 정보 리스트를 배치 삽입합니다.
     *
     * @param list a list of EncryptedLocationDto objects
     */
    void insertLocationBatch(List<EncryptedLocationDto> list);

}

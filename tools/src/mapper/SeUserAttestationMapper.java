package mapper;

import mapper.SeUserAttestation;

public interface SeUserAttestationMapper {
    int deleteByPrimaryKey(String id);

    int insert(SeUserAttestation record);

    int insertSelective(SeUserAttestation record);

    SeUserAttestation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SeUserAttestation record);

    int updateByPrimaryKey(SeUserAttestation record);
}
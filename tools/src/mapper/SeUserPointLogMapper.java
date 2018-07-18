package mapper;

import mapper.SeUserPointLog;

public interface SeUserPointLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(SeUserPointLog record);

    int insertSelective(SeUserPointLog record);

    SeUserPointLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SeUserPointLog record);

    int updateByPrimaryKey(SeUserPointLog record);
}
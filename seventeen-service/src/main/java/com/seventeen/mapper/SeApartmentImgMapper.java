package com.seventeen.mapper;

import com.seventeen.bean.SeApartmentImg;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SeApartmentImgMapper extends CoreMapper<SeApartmentImg>{

    @Select("<script>"
            +"delete  from se_apartment_img WHERE ap_id IN"
            + "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'>" + "#{id}"
            + "</foreach>"
            +"</script>")
    void deleteByApId(@Param("ids") String[] split);


    @Delete("<script>"
            +"delete  from se_apartment_img WHERE id IN"
            + "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'>" + "#{id}"
            + "</foreach>"
            +"</script>")
    void deleteByIdsArr(@Param("ids") String[] split);


    /**
     *
     * @param id
     * @return
     */
    @Select("select * from se_apartment_img where ap_id = #{id}")
    List<SeApartmentImg> selectByApids(@Param("id") String id);

    @Update("update se_apartment_img se set se.master='1' where se.id =#{id} ")
    void updateImgMaster(@Param("id") String id);
}
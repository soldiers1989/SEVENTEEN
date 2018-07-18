package com.seventeen.mapper;

import com.seventeen.bean.SeApartmentImg;
import org.apache.ibatis.annotations.*;

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


    @Select("<script> SELECT * FROM se_apartment_img sai1 WHERE\n" +
            "sai1.ap_id IN ( " +
            "SELECT " +
            "sai.ap_id " +
            "FROM " +
            "se_apartment_img sai " +
            "WHERE " +
            "sai.id = #{id}" +
            ") </script>")
    List<SeApartmentImg> selectById(@Param("id") String id);
    /**
     *
     * @param id
     * @return
     */
    @Select("select * from se_apartment_img where ap_id = #{id} order by master desc")
    List<SeApartmentImg> selectByApids(@Param("id") String id);

    @Update("<script> update se_apartment_img se set se.master='1' where se.id =#{id} </script>")
    void updateImgMaster(@Param("id") String id);
}
package com.wjf.forumsystem.mapper;

import com.wjf.forumsystem.entity.Forums;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ForumMapper {
    @Insert("insert into forums(forum_name,description) values (#{forumName},#{description})")
    public void addForum(Forums forums);
    @Update("update forums set forum_name=#{forumName},description=#{description} where forum_id=#{forumId}")
    void updateForum(Forums forums);
    @Select("select * from forums where forum_id=#{forumId}")
    Forums findById(Integer forumId);
    @Select("select * from forums")
    List<Forums> getForumsInfo();
    @Select("select * from forums where forum_name = #{forumName}")
    Forums findByName(String forumName);

    //多条件查询
    List<Forums> findForums(Integer forumId, String forumName);
}

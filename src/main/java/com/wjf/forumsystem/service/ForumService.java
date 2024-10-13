package com.wjf.forumsystem.service;

import com.wjf.forumsystem.entity.Forums;

import java.util.List;

public interface ForumService {
    void addForum(Forums forums);

    void updateForum(Forums forums);

    Forums findById(Integer forumId);

    List<Forums> getForumsInfo();

    List<Forums> selectForum(Integer forumId, String forumName);
}

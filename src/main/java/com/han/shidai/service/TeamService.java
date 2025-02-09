package com.han.shidai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.han.shidai.model.DTO.TeamQuery;
import com.han.shidai.model.domain.Team;
import com.han.shidai.model.domain.User;
import com.han.shidai.model.request.TeamJoinRequest;
import com.han.shidai.model.request.TeamQuitRequest;
import com.han.shidai.model.request.TeamUpdateRequest;
import com.han.shidai.model.vo.TeamUserVO;

import java.util.List;

/**
* @author lenovo
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-07-23 23:21:05
*/
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     * @param team
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     * @param teamQuery
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery,boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest,User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest,User loginUser);

    /**
     * 删除（解散队伍）
     * @param id
     * @return
     */
    boolean deleteTeam(long id,User loginUser);
}

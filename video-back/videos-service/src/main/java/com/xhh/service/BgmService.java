package com.xhh.service;


import com.xhh.pojo.Bgm;
import com.xhh.pojo.Users;

import java.util.List;

/**
 * @description
 * @author: weiXhh
 * @create: 2020-03-31 12:40
 **/
public interface BgmService {
    /**
     * 查询bgm列表
     * @return
     */
    public List<Bgm> queryBgmList();

    /**
     * 查询bgm通过id
     * @param bgmId
     * @return
     */
    public Bgm queryBgmById(String bgmId);
}

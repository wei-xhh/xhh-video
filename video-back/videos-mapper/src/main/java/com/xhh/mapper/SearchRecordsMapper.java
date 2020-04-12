package com.xhh.mapper;

import com.xhh.pojo.SearchRecords;
import com.xhh.utils.MyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    public List<String> getHotWords();
}
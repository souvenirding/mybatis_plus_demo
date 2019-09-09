package com.sz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sz.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentMapper extends BaseMapper<Student> {
}

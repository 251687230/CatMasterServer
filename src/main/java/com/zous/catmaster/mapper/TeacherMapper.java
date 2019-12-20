package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.Teacher;
import org.springframework.data.repository.CrudRepository;

public interface TeacherMapper extends CrudRepository<Teacher,String> {
}

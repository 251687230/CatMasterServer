package com.zous.catmaster.service;

import com.zous.catmaster.entity.Teacher;
import com.zous.catmaster.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherService {
    @Autowired
    TeacherMapper teacherMapper;

    public Optional<Teacher> getTeacher(String userId){
        return teacherMapper.findById(userId);
    }
}

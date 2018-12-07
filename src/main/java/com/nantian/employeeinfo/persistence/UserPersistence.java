package com.nantian.employeeinfo.persistence;

import com.nantian.employeeinfo.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPersistence {
	int deleteById(String id);

	int insert(User user);

	int insertSelective(User user);

	User selectById(String id);

	List<User> selectList(User user);

	int updateByPrimaryKeySelective(User user);

	int updateById(User user);
}

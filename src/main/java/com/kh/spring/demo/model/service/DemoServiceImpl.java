package com.kh.spring.demo.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.demo.model.dao.DemoDao;
import com.kh.spring.demo.model.vo.Dev;

@Service
public class DemoServiceImpl implements DemoService{

	@Autowired
	private DemoDao demoDao;

	@Override
	public int insertDev(Dev dev) {
//		int result = demoDao.insertDev(dev);
		return demoDao.insertDev(dev);
	}

	@Override
	public List<Dev> selectDevList() {
//		List<Dev> list = demoDao.selectDevList();
		return demoDao.selectDevList();
	}

	@Override
	public Dev selectOne(int no) {
		return demoDao.selectOne(no);
	}

	@Override
	public int updateDev(Dev dev) {
		return demoDao.updateDev(dev);
	}

	@Override
	public int deleteDev(int no) {
		return demoDao.deleteDev(no);
	}
}

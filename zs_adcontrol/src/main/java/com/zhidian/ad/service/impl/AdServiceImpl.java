package com.zhidian.ad.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhidian.ad.domain.Ad;
import com.zhidian.ad.service.AdService;
import com.zhidian.common.repository.BaseRepository;

@Service
public class AdServiceImpl implements AdService {
	@Resource
	private BaseRepository baseRepository;

	public List<Ad> getAdById() {
		return baseRepository.findList(Ad.class, "getAdList", null);
	}

}

package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ruoyi.acad.dao.SearchFavoritesMapper;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.domain.SearchFavorites;
import com.ruoyi.acad.service.ISearchFavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：创建搜索收藏夹<br/>
 * CreateTime ：2020年3月26日下午4:14:35<br/>
 * CreateUser：ys<br/>
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SearchFavoritesServiceImpl extends ServiceImpl<SearchFavoritesMapper, SearchFavorites> implements ISearchFavoritesService {

	@Autowired
	private SearchFavoritesMapper searchFavoritesMapper;
	
	/**
	 * 保存搜索文件夹实体
	 */
	@Override
	public ResponseResult saveModel(SearchFavorites model) throws Exception {
		
		//如果parentId不为空则为  二级
		if (model != null && model.getParentId() != null) {
			
			//获取收藏夹下词条数量
			Integer searchCount = searchFavoritesMapper.selectCount(
					new LambdaQueryWrapper<SearchFavorites>().eq(SearchFavorites::getParentId, model.getParentId()));
			
			if (searchCount >= 20) {
				return new ResponseResult(true, 204, "您最多可以创建20个收藏夹");
			}
			
			model.setContent(model.getClientSearchCriteria());
		} else {
			Integer parentCount = searchFavoritesMapper.selectCount(
					new LambdaQueryWrapper<SearchFavorites>().eq(SearchFavorites::getUserId, model.getUserId())
					.eq(SearchFavorites:: getParentId, null));
			
			if (parentCount > 15) {
				return new ResponseResult(true, 204, "收藏夹已有15条,你不能继续新增收藏夹");
			}
		}
		
		this.save(model);
		return new ResponseResult(true, 200, "成功");
	}

	/**
	 * 根据条件搜索
	 */
	@Override
	public List<SearchFavorites> getSearchFavoritesList(Integer userId, Integer parentId) throws Exception {
		
		List<SearchFavorites> list = new ArrayList<>();
		//如果收藏夹ID存在
		if (userId != null && parentId != null) {
			
			list = searchFavoritesMapper.selectList(
					new LambdaQueryWrapper<SearchFavorites>().eq(SearchFavorites::getParentId, parentId)
					.eq(SearchFavorites::getUserId, userId));
		} else {
			list = searchFavoritesMapper.selectList(
					new LambdaQueryWrapper<SearchFavorites>().eq(SearchFavorites::getParentId, null)
					.eq(SearchFavorites::getUserId, userId));
			
		}
		return list;
	}

	/**
	 * 修改收藏夹
	 */
	@Override
	public ResponseResult updateModel(SearchFavorites model) throws Exception {
		
		searchFavoritesMapper.updateById(model);
		return new ResponseResult(true, 200, "成功");
	}
}

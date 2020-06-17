package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.domain.SearchFavorites;

import java.util.List;

/**
 * Description：创建搜索收藏夹接口逻辑层<br/>
 * CreateTime ：2020年3月27日上午9:17:32<br/>
 * CreateUser：ys<br/>
 */
public interface ISearchFavoritesService extends IService<SearchFavorites> {

	/**
	 * Description:
	 * CreateTime:2020年3月27日上午9:17:28
	 * @param model
	 * @return
	 * @throws Exception
	 */
	ResponseResult saveModel(SearchFavorites model) throws Exception;

	/**
	 * Description:获取收藏夹及收藏夹下所有搜索词条
	 * CreateTime:2020年3月27日上午11:01:11
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	List<SearchFavorites> getSearchFavoritesList(Integer userId, Integer parentId) throws Exception;

	/**
	 * Description:修改收藏夹
	 * CreateTime:2020年3月27日下午2:06:11
	 * @param model
	 * @return
	 * @throws Exception
	 */
	ResponseResult updateModel(SearchFavorites model) throws Exception;

	//删除收藏夹
	void deleteModel(Long id) throws Exception;

}

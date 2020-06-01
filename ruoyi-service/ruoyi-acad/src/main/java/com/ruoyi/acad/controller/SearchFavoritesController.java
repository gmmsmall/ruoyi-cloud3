package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.domain.SearchFavorites;
import com.ruoyi.acad.service.ISearchFavoritesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description：搜索收藏控制层<br/>
 * CreateTime ：2020年3月26日下午4:16:03<br/>
 * CreateUser：ys<br/>
 */
@Slf4j
@RestController
@RequestMapping("/searchFavorites")
public class SearchFavoritesController{

	@Autowired
	private ISearchFavoritesService searchFavoritesService;
	
	/**
	 * Description:根据用户ID获取收藏夹
	 * CreateTime:2020年3月26日下午4:17:53	
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getListByUserId")
	public List<SearchFavorites> getListByUserId(Integer userId, Integer parentId) throws Exception {
		
		List<SearchFavorites> list = searchFavoritesService.getSearchFavoritesList(userId,parentId);
		return list;
	}
	
	/**
	 * Description:保存收藏
	 * CreateTime:2020年3月27日上午9:14:43
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveModel")
	public ResponseResult saveModel(@RequestBody SearchFavorites model) throws Exception {

		ResponseResult result = searchFavoritesService.saveModel(model);
		return result;
	}
	
	/**
	 * Description:修改收藏夹
	 * CreateTime:2020年3月27日下午2:05:52
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateModel")
	public ResponseResult updateModel(@RequestBody SearchFavorites model) throws Exception {
		
		ResponseResult result = searchFavoritesService.updateModel(model);
		return result;
	}
	
}

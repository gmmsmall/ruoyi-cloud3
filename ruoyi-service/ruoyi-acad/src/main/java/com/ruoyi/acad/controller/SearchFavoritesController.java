package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.domain.SearchFavorites;
import com.ruoyi.acad.service.ISearchFavoritesService;
import com.ruoyi.common.core.domain.RE;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Description：搜索收藏控制层<br/>
 * CreateTime ：2020年3月26日下午4:16:03<br/>
 * CreateUser：ys<br/>
 */
@Slf4j
@RestController
@RequestMapping("/searchFavorites")
@Api(tags = "收藏夹管理")
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
	@ApiOperation(value = "根据用户ID和parentId获取收藏夹列表")
	@ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
	public List<SearchFavorites> getListByUserId(@ApiParam(value = "用户id",required = true)@RequestParam Integer userId,
												 @ApiParam(value = "parentId",required = true)@RequestParam Integer parentId) throws Exception {
		return this.searchFavoritesService.getSearchFavoritesList(userId,parentId);
	}
	
	/**
	 * Description:保存收藏
	 * CreateTime:2020年3月27日上午9:14:43
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveModel")
	@ApiOperation(value = "新增收藏夹")
	@ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
	public RE saveModel(@Valid @RequestBody@ApiParam(value = "收藏夹参数",required = true) SearchFavorites model) throws Exception {
		this.searchFavoritesService.saveModel(model);
		return new RE().ok("保存成功");
	}
	
	/**
	 * Description:修改收藏夹
	 * CreateTime:2020年3月27日下午2:05:52
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateModel")
	@ApiOperation(value = "修改收藏夹")
	@ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
	public RE updateModel(@Valid @RequestBody@ApiParam(value = "收藏夹参数",required = true) SearchFavorites model) throws Exception {
		this.searchFavoritesService.updateModel(model);
		return new RE().ok("修改成功");
	}
	
}

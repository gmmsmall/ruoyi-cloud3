package com.ruoyi.javamail.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.javamail.bo.SendMailGroupBo;
import com.ruoyi.javamail.domain.ResponseResult;
import com.ruoyi.javamail.entity.SendMailGroup;
import com.ruoyi.javamail.entity.SendMailGroupItems;
import com.ruoyi.javamail.exception.FebsException;
import com.ruoyi.javamail.service.ISendMailGroupItemsService;
import com.ruoyi.javamail.service.ISendMailGroupService;
import com.ruoyi.javamail.util.FebsUtil;
import com.ruoyi.javamail.util.StringUtils;
import com.ruoyi.javamail.vo.SendMailGroupVo;
import com.ruoyi.javamail.web.ApiJsonObject;
import com.ruoyi.javamail.web.ApiJsonProperty;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author gmm
 */
@RestController
@RequestMapping("mail-group")
@Slf4j
@Api(tags = "分组管理")
public class SendMailGroupController extends BaseController {

    @Autowired
    private ISendMailGroupService groupService;

    @Autowired
    private ISendMailGroupItemsService groupItemsService;

    private String message;

    private boolean flag = true;
    /*@Autowired
    public UserManager userManager;
    @Autowired
    private IAosService aosService;*/

    /**
     * 获取当前用户下的可看的院士信息列表
     * @return
     * @throws FebsException
     */
/*    @PostMapping("/acadelist")
    @ApiOperation(value="获取当前用户下可看的院士信息列表", notes="")
    public ResponseResult acadelist() throws FebsException {
        Map<String,Object> map = new HashMap<>();
        try{

            Set<String> setlong = userManager.getUserAos(FebsUtil.getCurrentUser().getUsername());
            System.out.println("输出结果："+setlong);
            if(setlong != null && setlong.size() > 0){
                String aosIds = StringUtils.join(setlong.toArray(), ",");
                System.out.println("输出字符串："+aosIds);
                List<ClientBaseInfo> clientBaseInfoList = aosService.getListByAosIds(aosIds);
                map.put("data",aosService.transformClient(clientBaseInfoList));
                message = "获取成功";
            }else{
                message = "该用户没有看查看的院士信息";
            }
        }catch(Exception e){
            flag = false;
            message = "获取当前用户下可看的院士信息列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }

        return new ResponseResult(flag,200,message,map);
    }*/

    /**
     * 获取当前用户下的分组列表
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value="获取当前用户下的分组列表", notes="")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
    public List<SendMailGroupVo> groupList(){
        return this.groupService.groupList(FebsUtil.getCurrentUser().getUserId());
    }

    /**
     * 新增一个分组（仅名称）
     * @param sendMailGroupBo
     * @return
     * @throws FebsException
     */
    @PostMapping("/add")
    @ApiOperation(value="新增一个分组（仅名称）", notes="分组名称不能为空")
    @ApiResponses({@ApiResponse(code = 200,message = "新增成功")})
    public RE addTemplate(@Valid @RequestBody @ApiParam(value = "新增分组参数",required = true) SendMailGroupBo sendMailGroupBo){
        sendMailGroupBo.setAddperson(FebsUtil.getCurrentUser().getUsername());
        sendMailGroupBo.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
        this.groupService.saveGroup(sendMailGroupBo);
        return new RE().ok();
    }

    /**
     * 修改一个分组（主子表都需要修改）
     * @param groupmsg
     * @return
     * @throws FebsException
     */
    @PostMapping("/edit")
    @ApiOperation(value="修改一个分组", notes="")
    public ResponseResult editTemplate(@ApiJsonObject(name = "groupmsg", value = {
            @ApiJsonProperty(key = "zhu",description = "{\n" +
                    "\t\"id\": \"主键id\",\n" +
                    "\t\"name\": \"分组名称\",\n" +
                    "\t\"addtime\": \"新增时间\",\n" +
                    "\t\"addperson\": \"操作人\",\n" +
                    "\t\"addpersonid\": \"新增用户id\"\n" +
                    "}"),
            @ApiJsonProperty(key = "list",description = "[{\n" +
                    "\t\"fid\": \"主表id\",\n" +
                    "\t\"acadencode\": \"院士编码\",\n" +
                    "\t\"deleteflag\": \"是否删除\"\n" +
                    "}, {\n" +
                    "\t\"fid\": \"主表id\",\n" +
                    "\t\"acadencode\": \"院士编码\",\n" +
                    "\t\"deleteflag\": \"是否删除\"\n" +
                    "}]")
    })@RequestBody String groupmsg) throws FebsException{
        try {
            JSONObject json = JSONObject.parseObject(groupmsg);
            SendMailGroup sendMailGroup = JSONObject.toJavaObject(json.getJSONObject("zhu"),SendMailGroup.class);
            sendMailGroup.setEdittime(LocalDateTime.now());
            sendMailGroup.setEditperson(FebsUtil.getCurrentUser().getUsername());
            sendMailGroup.setEditpersonid(FebsUtil.getCurrentUser().getUserId());
            sendMailGroup.setDeleteflag("1");//未删除
            List<SendMailGroupItems> itemsList = (List<SendMailGroupItems>)JSONObject.parseArray(json.getString("list"),SendMailGroupItems.class);
            groupService.updateGroupById(sendMailGroup,itemsList);
            message = "修改成功";
        } catch (Exception e) {
            flag = false;
            message = "修改分组失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,null);
    }

    /**
     * 删除分组（单删或批量删）
     * @param ids
     * @return
     * @throws FebsException
     */
    @PostMapping("/delete")
    @ApiOperation(value="删除分组（单删或批量删）", notes="请求参数：主键id以逗号形式拼接成的字符串")
    @ApiImplicitParam(paramType="path", name = "ids", value = "分组id", required = true, dataType = "String")
    public ResponseResult deleteT(@RequestBody String ids) throws FebsException {
        try {
            JSONObject jsonObject = JSONObject.parseObject(ids);
            String idStr = jsonObject.getString("id");
            if(idStr != null && !idStr.equals("")){
                String[] idarr = idStr.split(StringPool.COMMA);
                groupService.deleteGroups(idarr);
                message = "删除成功";
            }else{
                flag = false;
                message = "请选择将要删除的数据";
            }

        } catch (Exception e) {
            flag = false;
            message = "删除分组失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,null);
    }

    /**
     * 获取分组列表详情
     * @param id
     * @return
     * @throws FebsException
     */
    @PostMapping("/info")
    @ApiOperation(value="获取分组列表详情", notes="请求参数：分组id")
    @ApiImplicitParam(paramType="path", name = "id", value = "分组id", required = true, dataType = "String")
    public ResponseResult templateOne(@RequestBody String id) throws FebsException{
        Map<String,Object> map = new HashMap<String,Object>();
        JSONObject json = JSONObject.parseObject(id);
        try{
            String idStr = json.getString("id");
            if(idStr != null && !idStr.equals("")){
                LambdaQueryWrapper<SendMailGroup> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(SendMailGroup::getId, Long.parseLong(idStr)).eq(SendMailGroup::getDeleteflag,"1");
                map.put("zhu",groupService.getMap(queryWrapper));//主表数据
                LambdaQueryWrapper<SendMailGroupItems> itemsLambdaQueryWrapper = new LambdaQueryWrapper<>();
                itemsLambdaQueryWrapper.eq(SendMailGroupItems::getFid,Long.parseLong(idStr)).eq(SendMailGroupItems::getDeleteflag,"1");
                map.put("list",groupItemsService.list(itemsLambdaQueryWrapper));
                message = "获取成功";
            }else{
                flag = false;
                message = "请选择将要获取的数据";
            }
        }catch(Exception e){
            flag = false;
            message = "获取分组列表详情失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,map);
    }

}

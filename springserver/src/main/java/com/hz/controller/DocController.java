package com.hz.controller;


import com.hz.demo.entity.ConvertResult;
import com.hz.demo.entity.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "文件列表接口")
@RequestMapping("DocList")
@Slf4j
public class DocController {

    //private static final Logger logger = LoggerFactory.getLogger(DocController.class);

    /**
     * 获取文档列表
     * @return ConvertResult对象
     */
    @ApiOperation(value ="获取文档列表",notes="获取文档列表")
    @GetMapping("/document")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页数",paramType = "query",dataType = "Integer",required = true),
            @ApiImplicitParam(name = "per_page",value = "每页数量",paramType = "query",dataType = "Integer",required = true)
    })
    public ConvertResult getDocList(@RequestParam("per_page")Integer per_page,
                                    @RequestParam("log_type")Integer log_type){
        return new ConvertResult(100000,"获取文档列表","获取文档列表成功");
    }

    @ApiOperation(value = "删除文档信息",notes = "删除文档信息")
    @DeleteMapping("/document/{docId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "docId",value = "文档id",paramType = "path",dataType = "String",required = true)
    })
    public ResponseResult<String> deleteDocuments(@PathVariable("docId") String docId){
        return new ResponseResult<>(100000, "删除文档信息成功", "删除文档信息成功");
    }

}

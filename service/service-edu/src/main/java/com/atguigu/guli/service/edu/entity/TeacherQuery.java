package com.atguigu.guli.service.edu.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="TeacherQuery对象", description="讲师查询对象")
public class TeacherQuery {
    @ApiModelProperty(value = "讲师姓名")
    private String name;
    @ApiModelProperty(value = "讲师级别")
    private Integer level;
    @ApiModelProperty(value = "开始时间")
    private String joinDateBegin;
    @ApiModelProperty(value = "结束时间")
    private String joinDateEnd;
}

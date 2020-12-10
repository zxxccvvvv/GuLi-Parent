package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("课程收藏信息")
@Data
public class CourseCollectVo {

    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "ID")
    private String courseId;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "价格")
    private String price;
    @ApiModelProperty(value = "课时数")
    private Integer lessonNum;
    @ApiModelProperty(value = "封面")
    private String cover;
    @ApiModelProperty(value = "收藏时间")
    private String gmtCreate;
    @ApiModelProperty(value = "讲师")
    private String teacherName;
}

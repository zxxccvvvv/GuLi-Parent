package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.atguigu.guli.service.edu.service.CourseCollectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/edu/course-collect")
@Slf4j
public class ApiCourseCollectController {

    @Autowired
    private CourseCollectService courseCollectService;

    @ApiOperation(value = "判断是否收藏")
    @GetMapping("/auth/is-collect/{courseId}")
    public R isCollect(
            @ApiParam("课程Id")
            @PathVariable String courseId,
            HttpServletRequest request
    ) {
        String memberId = JwtHelper.getId(request);
        boolean isCollect = courseCollectService.isCollect(courseId, memberId);
        return R.ok().data("isCollect", isCollect);
    }

    @ApiOperation("收藏课程")
    @PostMapping("/auth/save/{courseId}")
    public R save(
            @ApiParam("课程Id")
            @PathVariable String courseId,
            HttpServletRequest request
    ) {
        String memberId = JwtHelper.getId(request);
        courseCollectService.saveCourseCollect(courseId, memberId);
        return R.ok();
    }

    @ApiOperation(value = "获取课程收藏列表")
    @GetMapping("auth/list")
    public R collectList(HttpServletRequest request) {
        String memberId = JwtHelper.getId(request);
        List<CourseCollectVo> list = courseCollectService.selectList(memberId);
        return R.ok().data("items", list);
    }


    @ApiOperation(value = "取消收藏课程")
    @DeleteMapping("auth/remove/{courseId}")
    public R removeCollect(
            @ApiParam("课程id")
            @PathVariable String courseId,
            HttpServletRequest request
    ) {
        String memberId = JwtHelper.getId(request);
        boolean result = courseCollectService.removeCourseCollect(courseId, memberId);
        if (result) {
            return R.ok().message("已取消");
        } else {
            return R.error().message("取消失败");
        }
    }
}

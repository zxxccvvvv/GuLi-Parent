package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.query.WebCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin
@Api(tags = "课程")
@RestController
@RequestMapping("/api/edu/course")
public class ApiCourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("课程列表")
    @GetMapping("list")
    public R list(
            @ApiParam(value = "查询对象", required = false)
            WebCourseQuery webCourseQuery
    ) {
        List<Course> courseList = courseService.webSelectList(webCourseQuery);
        return R.ok().data("courseList", courseList);
    }

    @Autowired
    private ChapterService chapterService;

    @ApiOperation("根据ID查询课程")
    @GetMapping("get/{courseId}")
    public R getById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String courseId){

        //查询课程信息和讲师信息
        WebCourseVo webCourseVo = courseService.selectWebCourseVoById(courseId);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);

        return R.ok().data("course", webCourseVo).data("chapterVoList", chapterVoList);
    }

    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("get-course-dto/{courseId}")
    public R getCourseDtoById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String courseId) {
        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return R.ok().data("courseDto", courseDto);
    }

    @ApiOperation("根据课程id修改课程销量")
    @GetMapping("update-buy-count/{courseId}")
    public R updateBuyCount(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String courseId) {
        courseService.updateBuyCount(courseId);
        return R.ok();
    }

}

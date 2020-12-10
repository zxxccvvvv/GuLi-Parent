package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.feign.fallback.EduCourseServiceFallBack;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(value = "service-edu", fallback = EduCourseServiceFallBack.class)
public interface EduCourseService {

    @GetMapping("/api/edu/course/get-course-dto/{courseId}")
    R getCourseDtoById(
            @ApiParam(value = "课程Id", required = true)
            @PathVariable String courseId
    );

    @GetMapping("/api/edu/course/update-buy-count/{courseId}")
    R updateBuyCount(@PathVariable String courseId);
}

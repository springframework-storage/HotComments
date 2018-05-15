package com.naver.hackday.controller;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.exception.BadRequestException;
import com.naver.hackday.model.codingsquid.BaseCode;
import com.naver.hackday.model.codingsquid.BaseListRtn;
import com.naver.hackday.model.codingsquid.BaseResponse;
import com.naver.hackday.repository.cache.RedisRepository;
import com.naver.hackday.service.CommentListService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CommentController extends BaseRestController {

    @Getter
    private enum OrderType {
        ASC(0, "ASC"),
        DESC(1, "DESC");

        private final int typeInt;
        private final String typeString;

        OrderType(int typeInt, String typeString) {
            this.typeInt = typeInt;
            this.typeString = typeString;
        }
    }

    private CommentListService commentListService;
    private RedisRepository redisRepository;

    @Autowired
    public CommentController(@Qualifier("commentCachingServiceImpl") CommentListService commentListService,
                             @Qualifier("redisRepositoryImpl") RedisRepository redisRepository) {
        this.commentListService = commentListService;
        this.redisRepository = redisRepository;
    }

    @GetMapping(value = "/comments")
    public BaseResponse<BaseListRtn<CommentDto>> doGet(@RequestHeader(value = "post_id") Integer postId,
                                                       @RequestHeader(value = "user_id") Integer userId,
                                                       @RequestParam(value = "cursor") Integer cursor,
                                                       @RequestParam(value = "size", required = false) Integer size,
                                                       @RequestParam(value = "orderType", required = false) String orderType,
                                                       @RequestParam(value = "pageNo") Integer pageNo) {
        int paramSize;
        String paramOrderType;

        if (Objects.isNull(size)) paramSize = 10;
        else paramSize = size;

        if (Objects.isNull(orderType)) paramOrderType = OrderType.DESC.getTypeString();
        else paramOrderType = orderType;

        if (!OrderType.ASC.typeString.equals(paramOrderType)
                && !OrderType.DESC.typeString.equals(paramOrderType)) {
            throw new BadRequestException(BaseCode.BAD_REQUEST.getMessage());
        }

        return commentListService.doGet(cursor, paramSize, pageNo, paramOrderType, postId, userId);
    }

}

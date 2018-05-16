package com.naver.hackday.controller;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.exception.BadRequestException;
import com.naver.hackday.model.codingsquid.BaseCode;
import com.naver.hackday.model.codingsquid.BaseListRtn;
import com.naver.hackday.model.codingsquid.BaseResponse;
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

    @Autowired
    public CommentController(@Qualifier("commentCachingServiceImpl") CommentListService commentListService) {
        this.commentListService = commentListService;
    }
    //TODO size값 파라미터로 받지말고 디폴트로 걸어버리자
    @GetMapping(value = "/comments")
    public BaseResponse<BaseListRtn<CommentDto>> doGet(@RequestHeader(value = "postId") Integer postId,
                                                       @RequestHeader(value = "userId") Integer userId,
                                                       @RequestParam(value = "cursor") Integer cursor,
                                                       @RequestParam(value = "size", required = false) Integer pageSize,
                                                       @RequestParam(value = "orderType", required = false) String orderType,
                                                       @RequestParam(value = "pageNo") Integer pageNo) {
        int paramSize;

        if (Objects.isNull(pageSize)) paramSize = 10;
        else paramSize = pageSize;

        if (paramSize <= 0) {
            throw new BadRequestException(BaseCode.BAD_REQUEST.getMessage() + " -> pageSize 조건 확인");
        }

        String paramOrderType;

        if (Objects.isNull(orderType)) paramOrderType = OrderType.DESC.getTypeString();
        else paramOrderType = orderType;

        if (!OrderType.ASC.typeString.equals(paramOrderType)
                && !OrderType.DESC.typeString.equals(paramOrderType)) {
            throw new BadRequestException(BaseCode.BAD_REQUEST.getMessage() + " -> orderType 조건 확인");
        }
        //TODO cursor 조건 문서화 하기
        if (Objects.isNull(cursor) || cursor < 1 || cursor > pageSize * 5) {
            throw new BadRequestException(BaseCode.BAD_REQUEST.getMessage() + " -> cursor 조건 확인");
        }

        return commentListService.doGet(cursor - 1, paramSize, pageNo, paramOrderType, postId, userId);
    }

}

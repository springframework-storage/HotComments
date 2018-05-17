package com.naver.hackday.controller;

import com.naver.hackday.dto.CommentRtn;
import com.naver.hackday.exception.BadRequestException;
import com.naver.hackday.model.codingsquid.BaseCode;
import com.naver.hackday.model.codingsquid.BaseListRtn;
import com.naver.hackday.model.codingsquid.BaseResponse;
import com.naver.hackday.service.CommentListService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CommentController extends BaseRestController {

    private static Logger logger = LoggerFactory.getLogger("log.hackday");

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
    @SuppressWarnings("unchecked")
    @GetMapping(value = "/comments/{postId}")
    public BaseResponse<BaseListRtn<CommentRtn>> doGet(@PathVariable(value = "postId") Integer postId,
                                                       @RequestParam(value = "userId") Integer userId,
                                                       @RequestParam(value = "cursor") Integer cursor,
                                                       @RequestParam(value = "pageSize") Integer pageSize,
                                                       @RequestParam(value = "orderType") String orderType,
                                                       @RequestParam(value = "pageNo") Integer pageNo) {
        logger.debug("GET : /v1/comments?cursor : " + cursor + " pageNo : " + pageNo + " pageSize : " + pageSize + " userId : " + userId);

        if (pageSize <= 0) {
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
        if (Objects.isNull(cursor) || cursor < 1 || cursor > pageSize) {
            throw new BadRequestException(BaseCode.BAD_REQUEST.getMessage() + " -> cursor 조건 확인");
        }

        return commentListService.doGet(cursor - 1, pageSize, pageNo, paramOrderType, postId, userId);
    }

}

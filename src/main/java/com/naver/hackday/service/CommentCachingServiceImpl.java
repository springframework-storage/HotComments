package com.naver.hackday.service;


import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.dto.CommentRtn;
import com.naver.hackday.dto.ValidCachingKey;
import com.naver.hackday.model.codingsquid.BaseCode;
import com.naver.hackday.model.codingsquid.BaseListRtn;
import com.naver.hackday.model.codingsquid.BaseResponse;
import com.naver.hackday.model.codingsquid.ReactStatus;
import com.naver.hackday.repository.cache.RedisRepository;
import com.naver.hackday.util.cache.CachingKeyHelper;
import com.naver.hackday.util.cache.CachingServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CommentCachingServiceImpl implements CommentListService {

    private final CommentListService commentListService;
    private final RedisRepository redisRepository;
    private final CachingServiceHelper<String, List<CommentDto>> cachingServiceHelper;

    @Autowired
    public CommentCachingServiceImpl(@Qualifier("redisRepositoryImpl") RedisRepository redisRepository,
                                     @Qualifier("commentOriginServiceImpl") CommentListService commentListService) {
        this.redisRepository = redisRepository;
        this.cachingServiceHelper = new CachingServiceHelper<>();
        this.commentListService = commentListService;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Override
    public BaseResponse<BaseListRtn<CommentRtn>> doGet(int cursor, int pageSize, int pageNo, String orderType, int postId, int userId) {
        BaseResponse<BaseListRtn<CommentDto>> resultProxy;
        BaseResponse<BaseListRtn<CommentRtn>> result = new BaseResponse<>();
        BaseListRtn<CommentRtn> baseListRtn = new BaseListRtn<>();

        result.setReturnMessage(BaseCode.SUCCESS.getMessage());
        result.setReturnCode(BaseCode.SUCCESS.getCode());

        String postKey = Integer.toString(postId);
        String pageKey = Integer.toString(pageNo);
        String cachingKey = CachingKeyHelper.getCommentListKey(postKey, pageKey);
        String cachingValidKey = CachingKeyHelper.getValidationCommentListKey(postKey, pageKey);

        //TODO 트랜잭션 && pipeline적용
        if (cachingServiceHelper.isNeedCaching(
                () -> redisRepository.getData(cachingValidKey),
                redisRepository::deleteData,
                redisRepository::getData,
                cachingKey,
                CachingKeyHelper.REACT_LAST_TIME_KEY)) {

            resultProxy = commentListService.doGet(cursor, pageSize, pageNo, orderType, postId, userId);

            for (CommentDto comments : resultProxy.getResult().getDatas()) {
                redisRepository.setListToListRight(cachingKey, comments, 200000L);
            }
            redisRepository.setData(cachingValidKey,
                    new ValidCachingKey(cachingKey, Timestamp.valueOf(LocalDateTime.now()).getTime()),
                    200000L);

            Stream<CommentDto> dtoStream = resultProxy.getResult().getDatas().stream();
            List<CommentDto> dtos = dtoStream.skip(cursor)
                    .limit(pageSize)
                    .collect(Collectors.toList());

            List<CommentRtn> rtnList = convertCommentDtoToRtn(dtos, userId);
            baseListRtn.setDatas(rtnList);
            baseListRtn.setTotalSize(rtnList.size());

            result.setResult(baseListRtn);

            return result;
        }

        //TODO 데이터 크기 조정
        int endPoint = pageSize - 1;
        List<CommentDto> proxyDatas = cachingServiceHelper.useCaching(key -> redisRepository.getRangeFromListForCommentDto(key, cursor, endPoint), cachingKey);
        List<CommentRtn> resultDatas = convertCommentDtoToRtn(proxyDatas, userId);

        baseListRtn.setDatas(resultDatas);
        baseListRtn.setTotalSize(resultDatas.size());

        result.setResult(baseListRtn);

        return result;
    }

    private boolean isReactStatus(int userId, int commentId) {
        return redisRepository.isMember(CachingKeyHelper.getReactUserSetKey(Integer.toString(1)), userId);
    }

    private List<CommentRtn> convertCommentDtoToRtn(List<CommentDto> dtoList, int userId) {
        List<CommentRtn> commentRtnList = new ArrayList<>();

        for (CommentDto commentDto : dtoList) {
            CommentRtn commentRtn = new CommentRtn();
            commentRtn.setId(commentDto.getId());
            commentRtn.setPostId(commentDto.getPostId());
            commentRtn.setUserId(commentDto.getUserId());
            commentRtn.setContent(commentDto.getContent());
            commentRtn.setNCount(commentDto.getNCount());
            commentRtn.setPCount(commentDto.getPCount());
            commentRtn.setTotalCount(commentDto.getTotalCount());
            if (isReactStatus(userId, commentDto.getId())) {
                commentRtn.setReactStatus(ReactStatus.POSITIVE);
            }
            else {
                commentRtn.setReactStatus(ReactStatus.NEUTRAL);
            }
            commentRtnList.add(commentRtn);
        }
        return commentRtnList;
    }

}

package com.example.dongsungsi.service;

import com.example.dongsungsi.dao.TutorialDao;
import com.example.dongsungsi.model.Tutorial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * packageName : com.example.dongsungsi.service
 * fileName : TutorialServiceImpl
 * author : ds
 * date : 2022-05-25
 * description : DAO, Model 이용해서 업무로직 구현 클래스
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-05-25         ds          최초 생성
 */
@Service
public class TutorialServiceImpl implements TutorialService{

//    @Autowired : 스프링에 생성된 객체를 받아옴
    @Autowired
    private TutorialDao tutorialDao; // 객체 정의 { null } => 스프링 객체

//    향후에 로그 찍고 싶을때 사용하는 객체
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Optional<Tutorial> findById(long id) {
        return tutorialDao.findById(id);
    }

    @Override
    public List<Tutorial> findByPublished(String published) {
        return tutorialDao.findByPublished(published);
    }

    @Override
    public List<Tutorial> findByTitleContaining(String title) {

//        빈 값으로 초기화
        List<Tutorial> tutorials = Collections.emptyList();

        if(title == null) {
//            title(제목)이 없으면 전체검색을 함
            tutorials = tutorialDao.findAll();
        } else {
//            title(제목) 이 있으면 제목검색을 함
            tutorials = tutorialDao.findByTitleContaining(title);
        }

        return tutorials;
    }

    @Override
    public List<Tutorial> findAll() {
        return tutorialDao.findAll();
    }

    @Override
    public boolean save(Tutorial tutorial) {
        int queryResult = 0;

//        매개변수 tutorial 안의 값을 로그로 출력
        logger.info("tutorial {} : ", tutorial);

        if(tutorial.getId() == null) {
//            id 값이 없으면 insert 문 실행
            queryResult = tutorialDao.insertTutorial(tutorial);
        } else {
//            id 값이 있으면 update 문 실행
            queryResult = tutorialDao.updateTutorial(tutorial);
        }

        return (queryResult >= 1) ? true : false;
    }

    @Override
    public boolean deleteById(Long id) {
        int queryResult = 0;

        queryResult = tutorialDao.deleteTutorial(id);

        return (queryResult >= 1) ? true : false;
    }

    @Override
    public boolean deleteAll() {
        int queryResult = 0;

        queryResult = tutorialDao.deleteAll();
        return (queryResult >= 1) ? true : false;
    }
}











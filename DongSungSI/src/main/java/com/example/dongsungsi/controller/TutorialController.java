package com.example.dongsungsi.controller;

import com.example.dongsungsi.model.Tutorial;
import com.example.dongsungsi.service.TutorialService;
import com.example.dongsungsi.service.TutorialServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * packageName : com.example.dongsungsi.controller
 * fileName : TutorialController
 * author : ds
 * date : 2022-05-25
 * description : 일종의 메뉴 ( URL 정보 )
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-05-25         ds          최초 생성
 */
//@RestController : REST API 호출을 위한 어노테이션 , JSON형태로 들어옴
@RestController
// http://localhost:8000/api
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    TutorialServiceImpl tutorialService; // 객체 정의 ( null ) => 스프링객체 받기

    Logger logger = LoggerFactory.getLogger(this.getClass());

//    @PostMapping : insert 문 호출 / update 문 호출
//    ResponseEntity : frontend 요청시 결과를 전달할 객체
    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial>
            createTutorial(@RequestBody Tutorial tutorial) {

        logger.info("createTutorial : tutorial {} : ", tutorial);
//        insert or update 호출 ( id 값을 체크 )
        boolean bSuccess = tutorialService.save(tutorial);

        try {
            if(bSuccess == true) {
//                정상 실행 된 경우
//                ResponseEntity<>(매개변수객체, 상태정보)
                return new ResponseEntity<>(tutorial, HttpStatus.CREATED);
            }
//            정상 실행안된 경우 : NOT_FOUND 프론트엔드로 전송
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(Exception e) {
//            DB 에러가 났을경우 : INTERNAL_SERVER_ERROR 프론트엔드로 전송
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping : Select 문 실행
    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial>
            getTutorialById(@PathVariable("id") long id ) {
//        Optional<Tutorial> : Tutorial 이 null이면 "" 바꿔줌
//                           : 목적 ) null포인트 에러 방지
        Optional<Tutorial> tutorial = tutorialService.findById(id);

//        Optional 메소드 : 값이 있으면
        if(tutorial.isPresent()) {
//            ResponseEntity<>(객체, 상태정보) => 프론트엔드로 전송
            return new ResponseEntity<>(tutorial.get(), HttpStatus.OK);
        } else {
//            프론트엔드로 전송 : NOT_FOUND
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>>
        getTitleTutorials(@RequestParam(required = false) String title) {
//            title(제목)을 조회하는 서비스를 호출
        List<Tutorial> tutorials =
                tutorialService.findByTitleContaining(title);

        try {
            if (tutorials.isEmpty()) {
//                조회 데이터가 없으면
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial>
        updateTutorial(@PathVariable("id") long id,
                       @RequestBody Tutorial tutorial) {

        Optional<Tutorial> tutorialData = tutorialService.findById(id);

        if (tutorialData.isPresent()) {
//            tutorialData가 있으면 save 호출 (id가 있으면 update 가 됨)
            boolean bSuccess = tutorialService.save(tutorial);
            if (bSuccess == true) {
//                save 호출이 성공하면
                return new ResponseEntity<>(tutorial, HttpStatus.CREATED);
            }
//            save 호출이 실패하면
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
//            tutorialData가 없으면
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
//    @PutMapping : update 문 실행
//    아래 delete 메소드 호출은 내부적으로 ( 컬럼 delete_yn = 'Y' )
    @PutMapping("/tutorials/deletion/{id}")
    public ResponseEntity<HttpStatus>
        deleteTutorial(@PathVariable("id") long id) {

        boolean bSuccess = tutorialService.deleteById(id);

        try {
            if (bSuccess == true) {
//                update문이 성공했을 경우
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
//            update 문이 실패했을 경우
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
//            DB 에러가 날 경우
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials")
    public ResponseEntity<HttpStatus> deleteAllTutorials() {
        boolean bSuccess = tutorialService.deleteAll();

        try {
            if (bSuccess == true) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<List<Tutorial>> findByPublished() {
//        테이블 컬럼중에 published가 "Y" 인 데이터만 select
        List<Tutorial> tutorials = tutorialService.findByPublished("Y");

        try {
            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}








